# Nota ayarlama aleti: onbellekten (f0_onbellek_v2.py) okur, nota F (mir_eval) olcer. Python -I ile calistir.
import sys, os, pickle, itertools, json, time, importlib.util, numpy as np
sys.path.insert(0, "/home/user/Jn/melodi-zil/arastirma/ortak")
import nota_cikar_v2 as V2
import nota_cikar as V1
import mir_eval
CACHE = "/home/user/cache/f0_v2"; REFP = "/home/user/cache/nota_v2_ref.pkl"
spec = importlib.util.spec_from_file_location("yardim", "/home/user/Jn/melodi-zil/arastirma/olc/yardim.py")
Y = importlib.util.module_from_spec(spec); spec.loader.exec_module(Y)

def yukle():
    if os.path.exists(REFP): return pickle.load(open(REFP, "rb"))
    D = {}
    for veri, adlar in (("vocadito", Y.liste("vocadito")), ("mir1k", Y.liste("mir1k", 30))):
        for ad in adlar:
            z = np.load(f"{CACHE}/{veri}__{ad}.npz"); _, _, _, riv, rph = Y.yukle(veri, ad, "mix")
            D[(veri, ad)] = dict(cents=z["cents"], smax=z["smax"], rms=z["rms"], riv=riv, rph=rph)
    pickle.dump(D, open(REFP, "wb")); return D

def f0_of(d, thred):
    f0 = 10 * 2 ** (d["cents"] / 1200); f0[f0 == 10] = 0
    f0 = np.where(d["smax"] > thred, f0, 0.0)
    return np.arange(len(f0)) * 0.01, f0

def puan(riv, rph, iv, hz):
    if len(riv) == 0: return 0.0
    if len(iv) == 0: return 0.0
    return mir_eval.transcription.precision_recall_f1_overlap(riv, rph, iv, hz, onset_tolerance=0.05, pitch_tolerance=50.0, offset_ratio=None)[:3]

class Tarayici:
    def __init__(self, D): self.D = D; self.c1 = {}
    def calistir(self, anahtarlar, thred=0.2, **kw):
        P = dict(V2.VARSAYILAN); P.update(kw); sonuc = {}
        for k in anahtarlar:
            d = self.D[k]; t, f0 = f0_of(d, thred)
            ck = (k, thred, P["gap_fill"], P["onceden_akort"], P["akort_guc"], P["lam"], P["cap"], P["agirlik"])
            if ck not in self.c1:
                m, v, hop, off = V2.hazirla(t, f0, P["gap_fill"], P["onceden_akort"], P["akort_guc"])
                self.c1[ck] = (m, v, hop, off, V2.viterbi_notalar(m, v, off, P["lam"], P["cap"], P["aralik"], (np.clip(d["smax"], P["agirlik"], 1.0) if P["agirlik"] > 0 else None)))
            m, v, hop, off, nt = self.c1[ck]
            r = []
            for yv in (False, "ayarli"):
                P2 = dict(P); P2["yuvarla"] = yv
                iv, hz, _ = V2.son_isle(t, m, v, hop, off, nt, d["smax"], d["rms"], P2)
                r.append(puan(d["riv"], d["rph"], iv, hz))
            sonuc[k] = r
        return sonuc
def ozet(sonuc, kume):
    ks = [k for k in sonuc if kume(k)]
    if not ks: return None
    out = {}
    for j, ad in enumerate(("ham", "yuv")):
        a = np.array([sonuc[k][j] if not np.isscalar(sonuc[k][j]) else (0, 0, sonuc[k][j]) for k in ks], float)
        out[ad] = tuple(a.mean(0).round(3))      # P,R,F
    return out
