# Aday: fcpe_vit = FCPE latent (360 sinif) uzerinde Viterbi ile surekli perde yolu (karisimda vokal yolunu secmek icin).
# extract(wav)->(times_sn, f0_hz). Ortam: FCPE_THR (guven esigi), FCPE_BAND (Viterbi gecis genisligi, bin), FCPE_FMIN/FMAX (Hz).
import os
import numpy as np, torch, librosa
torch.set_num_threads(int(os.environ.get("OMP_NUM_THREADS", "2")))
SR = 16000
THR = float(os.environ.get("FCPE_THR", "0.006"))
BAND = int(os.environ.get("FCPE_BAND", "8"))
FMIN = float(os.environ.get("FCPE_FMIN", "80"))
FMAX = float(os.environ.get("FCPE_FMAX", "1100"))
_m = None
def _model():
    global _m
    if _m is None:
        from torchfcpe import spawn_bundled_infer_model
        _m = spawn_bundled_infer_model(device="cpu")
    return _m
def _trans(n, band):
    i = np.arange(n)
    tr = np.maximum(band - np.abs(i[:, None] - i[None, :]), 0).astype(float)
    return tr / tr.sum(1, keepdims=True)
def extract(wav_path):
    y, _ = librosa.load(wav_path, sr=SR, mono=True)
    m = _model()
    with torch.no_grad():
        wav = torch.from_numpy(y.astype(np.float32))[None, :, None]
        mel = m.wav2mel(wav, SR)
        lat = m.model.forward(mel)[0].numpy().astype(np.float64)       # (T,360) sigmoid
    cents = m.model.cent_table.numpy().astype(np.float64)               # 360
    f_bins = 10.0 * 2 ** (cents / 1200.0)
    conf = lat.max(1)
    keep = (f_bins >= FMIN) & (f_bins <= FMAX)
    idx = np.where(keep)[0]
    p = lat[:, idx] + 1e-6
    p = (p / p.sum(1, keepdims=True)).T                                  # (S,T)
    path = librosa.sequence.viterbi(p, _trans(len(idx), BAND))
    # yerel agirlikli ortalama (alt-bin hassasiyeti)
    f0 = np.zeros(lat.shape[0])
    for t_, k in enumerate(path):
        lo, hi = max(0, k - 4), min(len(idx), k + 5)
        w = lat[t_, idx[lo:hi]]
        c = (cents[idx[lo:hi]] * w).sum() / max(w.sum(), 1e-9)
        f0[t_] = 10.0 * 2 ** (c / 1200.0)
    f0[conf <= THR] = 0.0
    return np.arange(len(f0)) * 0.01, f0
