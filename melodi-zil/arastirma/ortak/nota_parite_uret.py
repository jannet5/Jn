# Kotlin NoteTracker parite testi icin girdi/beklenen CSV uretir (5 klip). Kullanim: python -I nota_parite_uret.py
# Girdi: t, f0(Hz), salience, rms  (RMVPE onbellegi, esik 0.4).  Beklenen: nota_cikar_v2 ciktisi (varsayilan ayar: gercek rms ile, ve rms yokken salience-sahte-enerji ile).
import sys, os, numpy as np
sys.path.insert(0, "/home/user/Jn/melodi-zil/arastirma/ortak")
import nota_cikar_v2 as V2
CACHE = "/home/user/cache/f0_v2"; OUT = "/home/user/Jn/melodi-zil/app/src/test/resources/nota"
KLIPLER = [("vocadito", "vocadito_1"), ("vocadito", "vocadito_13"), ("vocadito", "vocadito_10"), ("mir1k", "Ani_1_01"), ("mir1k", "amy_10_05")]
os.makedirs(OUT, exist_ok=True)
for veri, ad in KLIPLER:
    z = np.load(f"{CACHE}/{veri}__{ad}.npz")
    f0 = 10 * 2 ** (z["cents"] / 1200); f0[f0 == 10] = 0; sal = z["smax"].astype(float); rms = z["rms"].astype(float)
    f0 = np.where(sal > 0.4, f0, 0.0)
    f0 = np.round(f0, 3); sal = np.round(sal, 4); rms = np.round(rms, 6)         # CSV'ye yazilan degerlerle ayni girdiyle hesapla
    t = np.arange(len(f0)) * 0.01
    with open(f"{OUT}/{ad}_girdi.csv", "w") as fh:
        fh.write("t,f0,sal,rms\n")
        for i in range(len(f0)): fh.write(f"{t[i]:.2f},{f0[i]:g},{sal[i]:g},{rms[i]:g}\n")
    for son, en in (("", rms), ("_sal", np.maximum(sal, 1e-3))):    # _sal: rms verilmezse Kotlin salience'i enerji yerine kullanir
        iv, hz, mid = V2.nota_cikar(t, f0, sal, en)
        with open(f"{OUT}/{ad}_beklenen{son}.csv", "w") as fh:
            fh.write("bas,bit,midi\n")
            for (a, b), m in zip(iv, mid): fh.write(f"{a:.4f},{b:.4f},{m:.4f}\n")
        print(ad, son or "_rms", len(iv), "nota", flush=True)
