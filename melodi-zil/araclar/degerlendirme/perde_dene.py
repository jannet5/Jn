# Hazır perde modellerini (RMVPE, CREPE) bizim değerlendirme setinde dener; 10 ms f0 dizisini kaydeder ve mir_eval ile puanlar.
# Kullanım: python perde_dene.py <yontem: rmvpe|crepe-full|crepe-tiny> <ses_dizini (*.raw, f32le mono 22050)> <ref_dizini (*.ref.csv)> <cikti_dizini> [esik]
import sys, os, glob, numpy as np, torch, mir_eval, librosa  # gerekli kütüphaneler
sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "model"))  # rmvpe_model.py için
SR_IN = 22050  # setin örnekleme hızı
MODEL = "/tmp/claude-0/-home-user-Jn/ab7d1d1d-5d4e-5029-854d-1ba47b6a0560/scratchpad/dl_rmvpe/rmvpe.pt"  # RMVPE ağırlıkları (HF lj1995/VoiceConversionWebUI)
torch.set_num_threads(4)  # 4 çekirdek

_rm = None
def rmvpe_f0(x16, thr):  # 16 kHz ses → (zaman, f0, güven)
    global _rm
    if _rm is None:
        from rmvpe_model import E2E
        _rm = E2E(4, 1, (2, 2)); _rm.load_state_dict(torch.load(MODEL, map_location="cpu", weights_only=True)); _rm.eval()
    S = np.abs(librosa.stft(x16, n_fft=1024, hop_length=160, win_length=1024, window="hann", center=True))  # genlik
    mb = librosa.filters.mel(sr=16000, n_fft=1024, n_mels=128, fmin=30, fmax=8000, htk=True)  # mel filtreleri
    mel = np.log(np.maximum(mb @ S, 1e-5)).astype(np.float32)  # log-mel
    n = mel.shape[1]; pad = 32 * ((n - 1) // 32 + 1) - n  # 32'nin katına tamamla
    with torch.no_grad(): sal = _rm(torch.from_numpy(np.pad(mel, ((0, 0), (0, pad))))[None])[0, :n].numpy()  # [T, 360]
    cm = np.pad(20 * np.arange(360) + 1997.3794084376191, (4, 4)); sp = np.pad(sal, ((0, 0), (4, 4)))  # cent eşlemesi
    c = np.argmax(sal, 1) + 4; idx = c[:, None] + np.arange(-4, 5)[None]  # tepe çevresi 9 kutu
    w = np.take_along_axis(sp, idx, 1); cents = (w * cm[idx]).sum(1) / np.maximum(w.sum(1), 1e-9)  # yerel ağırlıklı ortalama
    conf = sal.max(1); f0 = 10 * 2 ** (cents / 1200); f0[conf <= thr] = 0  # eşik altı = sessiz
    return np.arange(n) * 0.01, f0, conf

def crepe_f0(x16, model, thr):
    import torchcrepe
    f0, per = torchcrepe.predict(torch.from_numpy(x16)[None], 16000, 160, 50, 1100, model, batch_size=512, device="cpu", return_periodicity=True, decoder=torchcrepe.decode.viterbi)
    f0 = f0[0].numpy(); per = per[0].numpy(); f0[per < thr] = 0
    return np.arange(len(f0)) * 0.01, f0, per

_fc = None
def fcpe_f0(x16, thr):  # FCPE (torchfcpe, MIT): hızlı ve küçük vokal perde modeli; güven çıktısı yok, eşik modelin içinde
    global _fc
    if _fc is None:
        from torchfcpe import spawn_bundled_infer_model
        _fc = spawn_bundled_infer_model(device="cpu")
    n = len(x16) // 160 + 1  # 10 ms kare sayısı
    with torch.no_grad(): f0 = _fc.infer(torch.from_numpy(x16)[None, :, None], sr=16000, decoder_mode="local_argmax", threshold=thr, f0_min=60, f0_max=1100, interp_uv=False, output_interp_target_length=n)
    f0 = f0.squeeze().numpy(); return np.arange(len(f0)) * 0.01, f0, (f0 > 0).astype(float)

def score(rt, rf, et, ef):
    r = mir_eval.melody.evaluate(rt, rf, et, ef)
    return [r["Raw Pitch Accuracy"], r["Raw Chroma Accuracy"], r["Overall Accuracy"], r["Voicing Recall"], r["Voicing False Alarm"]]

if __name__ == "__main__":
    meth, adir, rdir, odir = sys.argv[1:5]; thr = float(sys.argv[5]) if len(sys.argv) > 5 else {"rmvpe": 0.03, "fcpe": 0.006}.get(meth, 0.21)
    os.makedirs(odir, exist_ok=True); rows = {}
    for ref in sorted(glob.glob(os.path.join(rdir, "*.ref.csv"))):
        name = os.path.basename(ref)[:-len(".ref.csv")]; raw = os.path.join(adir, name + ".raw")
        if not os.path.exists(raw): continue
        x = np.fromfile(raw, dtype="<f4"); x16 = librosa.resample(x, orig_sr=SR_IN, target_sr=16000).astype(np.float32)  # 16 kHz
        t, f, c = rmvpe_f0(x16, thr) if meth == "rmvpe" else fcpe_f0(x16, thr) if meth == "fcpe" else crepe_f0(x16, meth.split("-")[1], thr)
        fr = librosa.feature.rms(y=x16, frame_length=1024, hop_length=160, center=True)[0][:len(t)]; fr = np.pad(fr, (0, len(t) - len(fr)))  # kare enerjisi
        edb = 20 * np.log10(np.maximum(fr, 1e-9) / max(np.percentile(fr, 95), 1e-9))  # %95'lik enerjiye göre dB (0 = yüksek, -40 = sessiz)
        np.savetxt(os.path.join(odir, name + ".f0.csv"), np.stack([t, f, c, edb], 1), delimiter=",", fmt="%.4f")  # kaydet: zaman, f0, güven, enerji dB
        a = np.loadtxt(ref, delimiter=",", ndmin=2); rows.setdefault(name[:3], []).append(score(a[:, 0], a[:, 1], t, f))
    for k, v in rows.items():
        m = np.mean(v, 0); print(f"{meth} {k} n={len(v)}  RPA={m[0]:.3f} RCA={m[1]:.3f} OA={m[2]:.3f} VR={m[3]:.3f} VFA={m[4]:.3f}")
