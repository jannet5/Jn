# 30 sn pencere: Demucs vokal + RMVPE f0'si ile dogrudan-karisim RMVPE f0'si arasindaki uyum (etiketsiz guven olcusu).
import sys, json, subprocess, numpy as np, soundfile as sf, torch, mir_eval
sys.path.insert(0, "/home/user/Jn/melodi-zil/arastirma/ortak")
from rmvpe_model import RMVPE
from demucs.pretrained import get_model
from demucs.apply import apply_model
torch.set_num_threads(2)
ad = sys.argv[1]; TMP = f"/home/user/data/sarkilar/tmp_{ad}"
subprocess.run(["ffmpeg", "-y", "-v", "error", "-i", f"{TMP}/orijinal30.mp3", "-ac", "2", "-ar", "44100", f"{TMP}/o44.wav"], check=True)
x, sr = sf.read(f"{TMP}/o44.wav", dtype="float32"); x = torch.from_numpy(x.T.copy())[None]
dm = get_model("htdemucs"); out = apply_model(dm, x, device="cpu", num_workers=0, split=True)
v = out[0, dm.sources.index("vocals")].mean(0).numpy()
sf.write(f"{TMP}/vokal44.wav", v, 44100)
subprocess.run(["ffmpeg", "-y", "-v", "error", "-i", f"{TMP}/vokal44.wav", "-ac", "1", "-ar", "16000", f"{TMP}/vokal16.wav"], check=True)
vy, _ = sf.read(f"{TMP}/vokal16.wav", dtype="float32")
m = RMVPE("/home/user/models/rmvpe_q/rmvpe_int8.onnx")
fv = np.asarray(m.infer_from_audio(vy, thred=0.2), float)
tw, fm = np.load(f"{TMP}/f0_pencere.npy")
n = min(len(fv), len(fm)); fv, fm = fv[:n], fm[:n]; t = np.arange(n) * 0.01
rv = (fv > 0); mv = (fm > 0)
res = {"ad": ad, "vokal_sesli_oran": round(float(rv.mean()), 3), "karisim_sesli_oran": round(float(mv.mean()), 3)}
if rv.sum() > 10:
    ref = fv; est = np.where(mv, fm, -fm) if False else fm
    res["RPA_vokal_ref_vs_karisim"] = round(float(mir_eval.melody.raw_pitch_accuracy(rv, ref, mv, est)), 3)
    res["RCA_oktavsiz"] = round(float(mir_eval.melody.raw_chroma_accuracy(rv, ref, mv, est)), 3)
    res["sesli_ortusme"] = round(float((rv & mv).sum() / max(1, rv.sum())), 3)
print(json.dumps(res, ensure_ascii=False)); json.dump(res, open(f"{TMP}/uyum.json", "w"), ensure_ascii=False)
