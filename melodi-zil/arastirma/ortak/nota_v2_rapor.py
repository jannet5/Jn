import sys; sys.path.insert(0,"/home/user/Jn/melodi-zil/arastirma/ortak")
from nota_v2_tara import *
import json
D = yukle(); T = Tarayici(D)
def num(k): return int(k[1].split("_")[1])
voc = sorted([k for k in D if k[0]=="vocadito"], key=num); mir = sorted([k for k in D if k[0]=="mir1k"])
kumeler = {"Vocadito ilk20 (ayar)": voc[:20], "Vocadito son20 (dogrulama)": voc[20:], "Vocadito 40": voc,
           "MIR-1K karisim cift15": mir[0::2], "MIR-1K karisim tek15": mir[1::2], "MIR-1K karisim 30": mir}
def prf(rr, ks):
    out = {}
    for j, ad in enumerate(("ham", "yuvarlanmis")):
        a = np.array([rr[k][j] for k in ks], float); out[ad] = [round(float(x), 3) for x in a.mean(0)]
    return out
# 1) taban (v1) ve v2 genel API ile
r1 = {}
for k in D:
    d = D[k]; t, f0 = f0_of(d, 0.2); o = []
    for yv in (False, "ayarli"):
        iv, hz, _ = V1.nota_cikar(t, f0, yuvarla=yv); o.append(puan(d["riv"], d["rph"], iv, hz))
    r1[k] = o
r2 = {}
for k in D:
    d = D[k]; t, f0 = f0_of(d, 0.4); o = []
    for yv in (False, "ayarli"):
        iv, hz, _ = V2.nota_cikar(t, f0, d["smax"], d["rms"], yuvarla=yv); o.append(puan(d["riv"], d["rph"], iv, hz))
    r2[k] = o
S = {"v1_esik0.2": {ad: prf(r1, ks) for ad, ks in kumeler.items()}, "v2_esik0.4": {ad: prf(r2, ks) for ad, ks in kumeler.items()}}
# 2) tarayici ile tutarlilik + ablasyon (kapatilan ozellik -> F, ham/yuv ortalamasi degil ayri)
abl = {}
tum = list(D)
dene = {"tam v2": {}, "akort Viterbi'den sonra (onceden_akort=False)": dict(onceden_akort=False), "akort_guc=1": dict(akort_guc=1.0), "akort_guc=0": dict(akort_guc=0.0),
        "durum_medyan kapali": dict(durum_medyan=False), "guven_esik=0": dict(guven_esik=0.0), "enerji onset kapali": dict(bol_enj=0.0),
        "salience onset acik (0.12)": dict(bol_sal=0.12), "gama cekme acik (0.25 sn)": dict(gama_sure=0.25), "oktav duzeltme acik (0.25 sn)": dict(oktav_sure=0.25),
        "kayma=0": dict(kayma=0.0), "kat_tol=2": dict(kat_tol=2), "lam=8,cap=3,min_dur=0.12,gap_fill=0.06 (v1 degerleri)": dict(lam=8.0, cap=3.0, min_dur=0.12, gap_fill=0.06)}
for ad, kw in dene.items():
    rr = T.calistir(tum, thred=0.4, **kw)
    abl[ad] = {kn: prf(rr, kumeler[kn]) for kn in ("Vocadito 40", "MIR-1K karisim 30")}
S["ablasyon"] = abl
json.dump(S, open("/home/user/Jn/melodi-zil/arastirma/sonuc/nota_v2_sonuc.json", "w"), indent=1, ensure_ascii=False)
for sur in ("v1_esik0.2", "v2_esik0.4"):
    print(sur)
    for ad in kumeler: print("  ", ad, S[sur][ad])
print("ABLASYON")
for ad in abl: print("  ", ad, abl[ad]["Vocadito 40"]["yuvarlanmis"][2], abl[ad]["Vocadito 40"]["ham"][2], "|", abl[ad]["MIR-1K karisim 30"]["yuvarlanmis"][2], abl[ad]["MIR-1K karisim 30"]["ham"][2])
