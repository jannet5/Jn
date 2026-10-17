# Sarki dosyasi -> 30 sn melodi MP3 ornekleri (piyano / muzik kutusu / 8-bit) + RMVPE sure (RTF) olcumu.
# Kullanim: python -I ornek_uret.py <ses_dosyasi> <kisa_ad> [model_yolu]
# Sarki sesleri ve turetilen melodiler depoya girmez (/home/user/ornekler altinda kalir).
import sys, time, subprocess, json, os
import numpy as np
import soundfile as sf
sys.path.insert(0, "/home/user/Jn/melodi-zil/arastirma/ortak")
from rmvpe_model import RMVPE
from nota_cikar import nota_cikar

src, ad = sys.argv[1], sys.argv[2]
model = sys.argv[3] if len(sys.argv) > 3 else "/home/user/models/rmvpe_q/rmvpe_int8.onnx"
OUT = "/home/user/ornekler"; TMP = f"/home/user/data/sarkilar/tmp_{ad}"; os.makedirs(OUT, exist_ok=True); os.makedirs(TMP, exist_ok=True)
wav = f"{TMP}/tam16k.wav"
subprocess.run(["ffmpeg", "-y", "-v", "error", "-i", src, "-ac", "1", "-ar", "16000", wav], check=True)
y, sr = sf.read(wav, dtype="float32"); dur = len(y) / sr
t0 = time.time(); m = RMVPE(model); t_yukle = time.time() - t0
t0 = time.time(); f0 = m.infer_from_audio(y, thred=0.2); t_cikar = time.time() - t0
f0 = np.asarray(f0, float); hop = 0.01; t = np.arange(len(f0)) * hop
# nakarat gibi 30 sn: sesli-kare orani x enerji
W = 30.0; nf = int(W / hop)
rms = np.array([np.sqrt(np.mean(y[int(i * hop * sr):int((i + 1) * hop * sr) + 1] ** 2) + 1e-12) for i in range(len(f0))])
rmsn = rms / (np.percentile(rms, 95) + 1e-9)
voiced = (f0 > 0).astype(float)
best, bs = 0, -1
for s in range(0, max(1, len(f0) - nf + 1), 100):
    sc = voiced[s:s + nf].mean() * min(1.5, rmsn[s:s + nf].mean())
    if sc > bs: bs, best = sc, s
s0, s1 = best, min(len(f0), best + nf)
tw = t[s0:s1] - t[s0]; fw = f0[s0:s1]
iv, hz, midi = nota_cikar(tw, fw)
midi = np.asarray(midi, float)
# Sentez
SR = 32000
def oktav_kaydir(midi):
    if len(midi) == 0: return midi
    med = np.median(midi); k = 0
    while med + 12 * k < 60: k += 1
    while med + 12 * k > 76: k -= 1
    return midi + 12 * k
def sentez(tur):
    mm = oktav_kaydir(midi)
    if tur == "kutu": mm = mm + (12 if np.median(mm) < 72 else 0)
    n = int(W * SR) + SR; out = np.zeros(n, np.float32)
    for (a, b), p in zip(iv, mm):
        f = 440.0 * 2 ** ((p - 69) / 12); i0 = int(a * SR); dur_n = int((b - a) * SR); rel = int(0.08 * SR)
        L = dur_n + rel; tt = np.arange(L) / SR
        if tur == "piyano":
            ham = sum((1.0 / h ** 1.2) * np.sin(2 * np.pi * f * h * tt) for h in range(1, 9) if f * h < 8000)
            env = np.exp(-tt / max(0.35, 1.4 - f / 800)) * np.minimum(1, tt / 0.004)
            env *= np.where(np.arange(L) < dur_n, 1.0, np.exp(-(np.arange(L) - dur_n) / (0.03 * SR)))
            w = ham * env
        elif tur == "kutu":
            w = sum(g * np.sin(2 * np.pi * f * r * tt) * np.exp(-tt / d) for g, r, d in [(1, 1, 0.5), (0.5, 2.01, 0.25), (0.25, 3.98, 0.12), (0.12, 5.9, 0.06)])
            w = w * np.minimum(1, tt / 0.002)
        else:
            sq = np.sign(np.sin(2 * np.pi * f * tt)); env = np.where(np.arange(L) < dur_n, 1.0, np.exp(-(np.arange(L) - dur_n) / (0.01 * SR)))
            w = 0.5 * sq * env * np.minimum(1, tt / 0.002)
        out[i0:i0 + L] += (w[:max(0, min(L, n - i0))]).astype(np.float32)
    out = out[:int(W * SR)]; pk = np.abs(out).max() + 1e-9
    return out / pk * 0.8
for tur, ek in [("piyano", "piyano"), ("kutu", "muzikkutusu"), ("8bit", "8bit")]:
    w = sentez(tur if tur != "8bit" else "8bit"); sf.write(f"{TMP}/{ek}.wav", w, SR)
    subprocess.run(["ffmpeg", "-y", "-v", "error", "-i", f"{TMP}/{ek}.wav", "-b:a", "128k", f"{OUT}/{ad}_{ek}.mp3"], check=True)
# Orijinal 30 sn kesit (karsilastirma icin, yerelde kalir)
subprocess.run(["ffmpeg", "-y", "-v", "error", "-ss", f"{s0 * hop:.2f}", "-t", "30", "-i", src, "-b:a", "128k", f"{TMP}/orijinal30.mp3"], check=True)
sf.write(f"{TMP}/pencere16k.wav", y[int(s0 * hop * sr):int(s1 * hop * sr)], sr)
np.save(f"{TMP}/f0_pencere.npy", np.stack([tw, fw]))
r = dict(ad=ad, sure_sn=round(dur, 1), model=os.path.basename(model), rmvpe_sn=round(t_cikar, 2), RTF=round(t_cikar / dur, 3), model_yukle_sn=round(t_yukle, 2),
         pencere_bas_sn=round(s0 * hop, 1), sesli_oran=round(float(voiced[s0:s1].mean()), 3), nota_sayisi=int(len(iv)), medyan_midi=float(np.median(midi)) if len(midi) else None)
json.dump(r, open(f"{TMP}/sonuc.json", "w"), ensure_ascii=False); print(json.dumps(r, ensure_ascii=False))
