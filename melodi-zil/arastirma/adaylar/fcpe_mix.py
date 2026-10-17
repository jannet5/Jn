# Aday: fcpe_mix = torchfcpe (FCPE, ~10 M parametre) dogrudan karisik sese (vokal ayirma YOK).
# Sozlesme: extract(wav)->(times_sn, f0_hz); f0==0 sessiz.
# Ortam degiskenleri: FCPE_THR (guven esigi, varsayilan 0.006), FCPE_DEC (local_argmax|argmax),
#   FCPE_FMIN / FCPE_FMAX (Hz, son-islem; disi sessiz), FCPE_BACKEND (torch|onnx), FCPE_ONNX (onnx yolu)
import os, sys
import numpy as np, torch, librosa
torch.set_num_threads(int(os.environ.get("OMP_NUM_THREADS", "2")))
SR = 16000
THR = float(os.environ.get("FCPE_THR", "0.006"))
DEC = os.environ.get("FCPE_DEC", "local_argmax")
FMIN = float(os.environ.get("FCPE_FMIN", "0"))
FMAX = float(os.environ.get("FCPE_FMAX", "0"))
_m = None
def _model():
    global _m
    if _m is None:
        from torchfcpe import spawn_bundled_infer_model
        _m = spawn_bundled_infer_model(device="cpu")
    return _m
def extract(wav_path):
    y, _ = librosa.load(wav_path, sr=SR, mono=True)
    with torch.no_grad():
        f0 = _model().infer(torch.from_numpy(y.astype(np.float32))[None, :, None], sr=SR, decoder_mode=DEC, threshold=THR)
    f0 = f0[0, :, 0].numpy().astype(float)
    f0[~np.isfinite(f0)] = 0.0
    if FMIN > 0: f0[f0 < FMIN] = 0.0
    if FMAX > 0: f0[f0 > FMAX] = 0.0
    t = np.arange(len(f0)) * 0.01
    return t, f0
