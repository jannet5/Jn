# Perde egrisi (f0) -> temiz nota listesi. Telefonda (Kotlin) ayni algoritma yazilacak; burasi referans.
# Adimlar: (1) kisa sessiz bosluk doldur, (2) tek tek sesli parcalarda Viterbi ile "parcali sabit yarim ton"
# yaklasimi (titreme/kayma gecis cezasi ile yutulur), (3) kisa notalari komsuya kat, (4) ayni perdeli komsulari birlestir.
import numpy as np

def hz2midi(f): return 69 + 12 * np.log2(np.maximum(f, 1e-9) / 440.0)
def midi2hz(m): return 440.0 * 2 ** ((np.asarray(m, float) - 69) / 12)

def nota_cikar(t, f0, lam=8.0, cap=3.0, min_dur=0.12, gap_fill=0.06, yuvarla="ayarli", aralik=(30, 100)):
    """Doner: intervals Nx2 [sn], pitches_hz N, midi N (yuvarlanmis ya da sureklidegerli)."""
    t = np.asarray(t, float); f0 = np.asarray(f0, float)
    if len(t) < 3: return np.zeros((0, 2)), np.zeros(0), np.zeros(0)
    hop = float(np.median(np.diff(t)))
    v = f0 > 0
    m = np.where(v, hz2midi(f0), 0.0)
    # (1) kisa bosluklari doldur (iki yaninda perde yakinsa)
    n = len(v); i = 0; gf = int(round(gap_fill / hop))
    while i < n:
        if v[i]: i += 1; continue
        j = i
        while j < n and not v[j]: j += 1
        if 0 < i and j < n and (j - i) <= gf and abs(m[i - 1] - m[j]) < 1.5:
            m[i:j] = np.linspace(m[i - 1], m[j], j - i + 2)[1:-1]; v[i:j] = True
        i = j
    states = np.arange(aralik[0], aralik[1] + 1, dtype=float)
    notes = []   # [bas_idx, bit_idx(dahil degil), midi]
    i = 0
    while i < n:
        if not v[i]: i += 1; continue
        j = i
        while j < n and v[j]: j += 1
        seg = m[i:j]
        if len(seg) >= 2:
            # Viterbi: maliyet = min((m-s)^2, cap) , gecis cezasi lam * |s-s'| ^ 0 (sabit) -> basit "degisim" cezasi
            d = np.minimum((seg[:, None] - states[None, :]) ** 2, cap)
            L = len(seg); S = len(states)
            cost = np.zeros((L, S)); bp = np.zeros((L, S), int)
            cost[0] = d[0]
            for k in range(1, L):
                best_prev = int(np.argmin(cost[k - 1])); bmin = cost[k - 1, best_prev] + lam
                stay = cost[k - 1]
                use_stay = stay <= bmin
                cost[k] = d[k] + np.where(use_stay, stay, bmin)
                bp[k] = np.where(use_stay, np.arange(S), best_prev)
            path = np.zeros(L, int); path[-1] = int(np.argmin(cost[-1]))
            for k in range(L - 1, 0, -1): path[k - 1] = bp[k, path[k]]
            a = 0
            for k in range(1, L + 1):
                if k == L or path[k] != path[a]:
                    notes.append([i + a, i + k, states[path[a]]]); a = k
        else:
            notes.append([i, j, float(np.round(seg[0]))])
        i = j
    # (3) kisa notalari at/kat, (4) ayni perdeli yakin notalari birle
    md = max(1, int(round(min_dur / hop)))
    changed = True
    while changed:
        changed = False
        for k, (s, e, p) in enumerate(notes):
            if e - s < md:
                prev = notes[k - 1] if k > 0 and notes[k - 1][1] == s else None
                nxt = notes[k + 1] if k + 1 < len(notes) and notes[k + 1][0] == e else None
                cand = [x for x in (prev, nxt) if x is not None and abs(x[2] - p) <= 2]
                if cand:
                    tgt = min(cand, key=lambda x: abs(x[2] - p))
                    if tgt is prev: prev[1] = e
                    else: nxt[0] = s
                del notes[k]; changed = True; break
    out = []
    for s, e, p in notes:
        if out and s - out[-1][1] <= int(round(0.06 / hop)) and abs(out[-1][2] - p) < 0.01: out[-1][1] = e
        else: out.append([s, e, p])
    out = [x for x in out if x[1] - x[0] >= md]
    if not out: return np.zeros((0, 2)), np.zeros(0), np.zeros(0)
    iv = np.array([[max(0.0, t[s] - hop / 2), t[min(e, n) - 1] + hop / 2] for s, e, _ in out])
    cont = np.array([float(np.median(m[s:e])) for s, e, _ in out])   # notanin gercek (kesirli) perdesi
    if yuvarla == 'ayarli':
        # sarkinin akort kaymasini bul (sureye gore agirlikli dairesel ortalama), yuvarlamayi ona gore yap
        w = np.array([e - s for s, e, _ in out], float); fr = 2 * np.pi * (cont - np.round(cont))
        off = np.arctan2((w * np.sin(fr)).sum(), (w * np.cos(fr)).sum()) / (2 * np.pi)
        mid = np.round(cont - off) + off
    elif yuvarla: mid = np.round(cont)
    else: mid = cont
    return iv, midi2hz(mid), mid
