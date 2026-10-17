# Perde egrisi (f0) -> temiz nota listesi, SURUM 2. nota_cikar.py'ye ek olarak:
#  (0) Viterbi'den ONCE sarkinin akort kaymasi cikarilir (durumlar tam yarim ton, olculen perde kaydirilir)
#  (a) salience/enerji cukurlarindan ayni perdeli tekrar notalari bolme (onset)
#  (b) ton bulma (Krumhansl-Schmuckler) + yalniz kisa/kararsiz notalari gamin icine cekme
#  (c) oktav sicramasi duzeltme (komsulardan 11-13 yt sapan kisa notalar)
#  (d) "kayma": nota baslangiclarini sabit bir sureyle oynatma (olcumde onset toleransi 50 ms)
# Imza: nota_cikar(t, f0, salience=None, enerji=None, ...) -> intervals Nx2 [sn], hz N, midi N
import numpy as np

def hz2midi(f): return 69 + 12 * np.log2(np.maximum(f, 1e-9) / 440.0)
def midi2hz(m): return 440.0 * 2 ** ((np.asarray(m, float) - 69) / 12)

VARSAYILAN = dict(
    lam=12.0, cap=5.0, min_dur=0.06, gap_fill=0.02, merge_gap=0.06, yuvarla="ayarli", aralik=(30, 100),
    onceden_akort=True, akort_guc=0.5,        # akort kaymasi Viterbi'den once cikarilir; yarisi uygulanir
    durum_medyan=True,                         # kat sonrasi nota perdesi = kare medyaninin yuvarlamasi
    guven_esik=0.5,                            # notanin ort. salience'i < 0.5 ise at (karisimda aksam sesi/gurultu)
    bol_sal=0.0, bol_enj=6.0, bol_pen=0.08, bol_min=0.08,   # onset: enerji cukuru >= 6 dB (salience cukuru kapali)
    gama_sure=0.0, gama_max=1.0, gama_belirsiz=0.5,          # gama cekme KAPALI (olcumde zarar verdi)
    oktav_sure=0.0,                                          # oktav duzeltme KAPALI (etkisiz: oktavsiz RPA = RPA)
    agirlik=0.0,                                             # salience agirlikli Viterbi KAPALI (zarar)
    kat_sure=0.0, kat_tol=1,                                 # kat_sure: komsuya katma esigi (0 -> min_dur); kat_tol: katilan komsu en cok kac yt farkli
    kayma=0.01)                                              # nota baslangiclarini 10 ms ileri al
# Not: f0 girdisi RMVPE salience-max > 0.4 karelerinden (esik 0.4); salience = ayni karelerin salience-max'i.

# ---------------- 0. hazirlik: sesli maske, kisa bosluk doldurma, akort kaymasi ----------------
def hazirla(t, f0, gap_fill=0.06, onceden_akort=True, akort_guc=1.0):
    t = np.asarray(t, float); f0 = np.asarray(f0, float)
    hop = float(np.median(np.diff(t)))
    v = f0 > 0
    m = np.where(v, hz2midi(f0), 0.0)
    n = len(v); i = 0; gf = int(round(gap_fill / hop))
    while i < n:
        if v[i]: i += 1; continue
        j = i
        while j < n and not v[j]: j += 1
        if 0 < i and j < n and (j - i) <= gf and abs(m[i - 1] - m[j]) < 1.5:
            m[i:j] = np.linspace(m[i - 1], m[j], j - i + 2)[1:-1]; v[i:j] = True
        i = j
    off = 0.0
    if onceden_akort and v.sum() > 10:
        fr = 2 * np.pi * (m[v] - np.round(m[v]))
        off = akort_guc * float(np.arctan2(np.sin(fr).sum(), np.cos(fr).sum()) / (2 * np.pi))   # [-0.5,0.5]; guc<1 -> kismi duzeltme
    return m, v, hop, off

# ---------------- 1. Viterbi: sesli parca basina "parcali sabit yarim ton" ----------------
def viterbi_notalar(m, v, off, lam=8.0, cap=3.0, aralik=(30, 100), w=None):
    """m: midi (sesli olmayan kareler 0), v: sesli maske. Doner: [[bas, bit(dahil degil), durum], ...] (durum: akort-duzeltilmis tam sayi)."""
    n = len(v); notes = []; i = 0
    while i < n:
        if not v[i]: i += 1; continue
        j = i
        while j < n and v[j]: j += 1
        seg = m[i:j] - off
        if len(seg) >= 2:
            lo = max(aralik[0], int(np.floor(seg.min())) - 1); hi = min(aralik[1], int(np.ceil(seg.max())) + 1)
            states = np.arange(lo, hi + 1, dtype=float); S = len(states); L = len(seg)
            d = np.minimum((seg[:, None] - states[None, :]) ** 2, cap)
            if w is not None: d = d * w[i:j, None]            # guvenilir (yuksek salience) kareler daha agir
            cost = np.zeros((L, S)); bp = np.zeros((L, S), int); cost[0] = d[0]; ar = np.arange(S)
            for k in range(1, L):
                bprev = int(np.argmin(cost[k - 1])); bmin = cost[k - 1, bprev] + lam
                stay = cost[k - 1] <= bmin
                cost[k] = d[k] + np.where(stay, cost[k - 1], bmin)
                bp[k] = np.where(stay, ar, bprev)
            path = np.zeros(L, int); path[-1] = int(np.argmin(cost[-1]))
            for k in range(L - 1, 0, -1): path[k - 1] = bp[k, path[k]]
            a = 0
            for k in range(1, L + 1):
                if k == L or path[k] != path[a]:
                    notes.append([i + a, i + k, float(states[path[a]]), True]); a = k
        else:
            notes.append([i, j, float(np.round(seg[0])), True])
        i = j
    return notes

# ---------------- yardimcilar ----------------
def _kisalari_katla(notes, md, tol=2):
    """md kareden kisa notalari, perdesi <=2 yt olan komsuya kat (komsu bitisikse); yoksa birakir (sonra atilir)."""
    changed = True
    while changed:
        changed = False
        for k, (s, e, p, h) in enumerate(notes):
            if e - s < md:
                prev = notes[k - 1] if k > 0 and notes[k - 1][1] == s else None
                nxt = notes[k + 1] if k + 1 < len(notes) and notes[k + 1][0] == e else None
                cand = [x for x in (prev, nxt) if x is not None and abs(x[2] - p) <= tol]
                if cand:
                    tgt = min(cand, key=lambda x: abs(x[2] - p))
                    if tgt is prev: prev[1] = e
                    else: nxt[0] = s
                del notes[k]; changed = True; break
    return notes

def _cukur_bol(notes, x, esik, pen, minp):
    """Her notanin icinde x (salience ya da dB) serisinin belirgin yerel cukurlarinda notayi ikiye boler (ayni perde, sert sinir)."""
    if esik <= 0: return notes
    out = []
    for s, e, p, h in notes:
        cand = []
        for i in range(s + minp, e - minp + 1):
            lo = max(s, i - pen); hi = min(e, i + pen + 1)
            if x[i] > x[lo:hi].min() + 1e-12: continue                   # yerel en kucuk degil
            sol = x[lo:i + 1].max(); sag = x[i:hi].max()
            pr = min(sol, sag) - x[i]
            if pr >= esik: cand.append((pr, i))
        cand.sort(reverse=True); kes = []
        for pr, i in cand:
            if all(abs(i - c) >= minp for c in kes): kes.append(i)
        kes.sort(); a = s; hh = h
        for c in kes:
            out.append([a, c, p, hh]); a = c; hh = True
        out.append([a, e, p, hh])
    return out

def _ton_maskesi(pc_agirlik):
    maj = np.array([6.35, 2.23, 3.48, 2.33, 4.38, 4.09, 2.52, 5.19, 2.39, 3.66, 2.29, 2.88])
    mnr = np.array([6.33, 2.68, 3.52, 5.38, 2.60, 3.53, 2.54, 4.75, 3.98, 2.69, 3.34, 3.17])
    best = (-9, 0, 0)
    for tonik in range(12):
        for mod, prof in ((0, maj), (1, mnr)):
            r = np.corrcoef(pc_agirlik, np.roll(prof, tonik))[0, 1]
            if r > best[0]: best = (r, tonik, mod)
    _, tonik, mod = best
    gam = [0, 2, 4, 5, 7, 9, 11] if mod == 0 else [0, 2, 3, 5, 7, 8, 10]
    mask = np.zeros(12, bool); mask[[(tonik + g) % 12 for g in gam]] = True
    return mask

# ---------------- 2. ana islev ----------------
def nota_cikar(t, f0, salience=None, enerji=None, **kw):
    """t: kare zamanlari [sn]; f0: Hz (0 = sessiz); salience: kare basina RMVPE salience-max (0..1) ya da None;
    enerji: kare basina RMS ya da None. Doner: intervals Nx2 [sn], pitches_hz N, midi N."""
    P = dict(VARSAYILAN); P.update(kw)
    t = np.asarray(t, float); f0 = np.asarray(f0, float)
    if len(t) < 3: return np.zeros((0, 2)), np.zeros(0), np.zeros(0)
    m, v, hop, off = hazirla(t, f0, P["gap_fill"], P["onceden_akort"], P["akort_guc"])
    w = np.clip(np.asarray(salience, float), P["agirlik"], 1.0) if (P["agirlik"] > 0 and salience is not None) else None
    notes = viterbi_notalar(m, v, off, P["lam"], P["cap"], P["aralik"], w)
    return son_isle(t, m, v, hop, off, notes, salience, enerji, P)

def son_isle(t, m, v, hop, off, notes_in, salience, enerji, P):
    n = len(t)
    notes = [list(x) for x in notes_in]
    md = max(1, int(round(P["min_dur"] / hop)))
    kd = max(md, int(round(P["kat_sure"] / hop))) if P["kat_sure"] > 0 else md
    notes = _kisalari_katla(notes, kd, P["kat_tol"])
    if P["durum_medyan"]:      # kat sonrasi notanin perdesini gercek kareler(in medyani)ndan yeniden yuvarla
        for z in notes:
            sg = m[z[0]:z[1]][v[z[0]:z[1]]]
            if len(sg): z[2] = float(np.round(np.median(sg) - off))
    # (a) onset: salience ve/veya enerji cukurlarindan bol
    if P["bol_sal"] > 0 and salience is not None:
        x = np.convolve(np.pad(np.asarray(salience, float), (1, 1), mode="edge"), np.ones(3) / 3, mode="valid")
        notes = _cukur_bol(notes, x, P["bol_sal"], int(round(P["bol_pen"] / hop)), max(1, int(round(P["bol_min"] / hop))))
    if P["bol_enj"] > 0 and enerji is not None:
        db = 20 * np.log10(np.asarray(enerji, float) + 1e-5)
        db = np.convolve(np.pad(db, (1, 1), mode="edge"), np.ones(3) / 3, mode="valid")
        notes = _cukur_bol(notes, db, P["bol_enj"], int(round(P["bol_pen"] / hop)), max(1, int(round(P["bol_min"] / hop))))
    notes.sort(key=lambda z: z[0])
    # (c) oktav duzeltme: kisa nota komsu(lar)dan 11-13 yt saparsa +-12 oynat
    if P["oktav_sure"] > 0:
        os_ = int(round(P["oktav_sure"] / hop))
        for k in range(len(notes)):
            s, e, p, h = notes[k]
            if e - s > os_: continue
            kom = []
            if k > 0 and notes[k - 1][1] >= s - 3: kom.append(notes[k - 1][2])
            if k + 1 < len(notes) and notes[k + 1][0] <= e + 3: kom.append(notes[k + 1][2])
            if not kom: continue
            ref = float(np.mean(kom)); sap = p - ref
            if 11 <= abs(sap) <= 13: notes[k][2] = p - 12 * np.sign(sap)
    # kesirli (gercek) perde: akort geri eklenmis
    cont = np.array([float(np.median(m[s:e])) for s, e, _, _ in notes]) if notes else np.zeros(0)
    # (b) ton bulma + kisa/kararsiz notalari gamin icine cek
    if P["gama_sure"] > 0 and len(notes) >= 4:
        w = np.array([e - s for s, e, _, _ in notes], float)
        pc = np.zeros(12)
        for (s, e, p, _), ww in zip(notes, w): pc[int(round(p + off)) % 12] += ww   # off: akort geri eklenince pc
        mask = _ton_maskesi(pc)
        gs = int(round(P["gama_sure"] / hop))
        for k, (s, e, p, h) in enumerate(notes):
            tuned = cont[k] - off
            kararsiz = abs(tuned - np.round(tuned)) > P["gama_belirsiz"] * 0.999
            if (e - s) <= gs or kararsiz:
                if mask[int(round(p + off)) % 12]: continue
                best = None
                for c in (p - 2, p - 1, p + 1, p + 2):
                    if mask[int(round(c + off)) % 12]:
                        dd = abs(tuned - c)
                        if best is None or dd < best[0]: best = (dd, c)
                if best is not None and best[0] <= P["gama_max"]: notes[k][2] = best[1]
    # ayni perdeli komsulari birlestir (sert siniri = onset olanlari birlestirme)
    mg = int(round(P["merge_gap"] / hop)); out = []
    for s, e, p, h in notes:
        if out and (not h) and s - out[-1][1] <= mg and abs(out[-1][2] - p) < 0.01: out[-1][1] = e
        else: out.append([s, e, p, h])
    out = [x for x in out if x[1] - x[0] >= md]
    if P["guven_esik"] > 0 and salience is not None:
        sa = np.asarray(salience, float)
        out = [x for x in out if float(np.mean(sa[x[0]:x[1]])) >= P["guven_esik"]]
    if not out: return np.zeros((0, 2)), np.zeros(0), np.zeros(0)
    iv = np.array([[max(0.0, t[s] - hop / 2), t[min(e, n) - 1] + hop / 2] for s, e, _, _ in out]) + P["kayma"]
    iv[:, 0] = np.maximum(iv[:, 0], 0.0)
    durum = np.array([p for _, _, p, _ in out]) + off
    conts = np.array([float(np.median(m[s:e])) for s, e, _, _ in out])
    yv = P["yuvarla"]
    if yv:
        if yv == "duz": mid = np.round(conts)
        elif P["onceden_akort"]: mid = durum
        else: mid = durum + _ayarli_kayma(conts, out)
    else:
        # ham: surekli perde; oktav duzeltilmis notada 12 kaydir
        mid = np.where(np.abs((durum - conts)) > 6, conts + 12 * np.round((durum - conts) / 12), conts)
    return iv, midi2hz(mid), mid

def _ayarli_kayma(cont, out):
    w = np.array([e - s for s, e, _, _ in out], float); fr = 2 * np.pi * (cont - np.round(cont))
    return float(np.arctan2((w * np.sin(fr)).sum(), (w * np.cos(fr)).sum()) / (2 * np.pi))
