# Aday: htdemucs ile vokal ayir (onbellekli) -> RMVPE (torch). extract(wav)->(times, f0_hz); f0==0 sessiz.
import os, sys, hashlib, numpy as np, soundfile as sf, librosa, torch
torch.set_num_threads(int(os.environ.get("OMP_NUM_THREADS", "2")))
sys.path.insert(0, "/home/user/Jn/melodi-zil/arastirma/ortak")
from rmvpe_model import RMVPE
CACHE = "/home/user/cache/demucs"; THRED = float(os.environ.get("RMVPE_THRED", "0.03"))
_dm = None; _rm = None
def _demucs():
    global _dm
    if _dm is None:
        from demucs.pretrained import get_model
        _dm = get_model("htdemucs").eval()
    return _dm
def _rmvpe():
    global _rm
    if _rm is None: _rm = RMVPE("/home/user/models/rmvpe/rmvpe.pt")
    return _rm
def vokal16k(wav_path):
    """Vokal kanalini 16 kHz mono dondurur; ayirma sonucu CACHE altinda tutulur."""
    st = os.stat(wav_path)
    key = hashlib.sha1(f"{os.path.abspath(wav_path)}|{st.st_size}|{st.st_mtime_ns}".encode()).hexdigest()[:20]
    p = f"{CACHE}/{key}.npy"
    if os.path.exists(p): return np.load(p)
    from demucs.apply import apply_model
    y, _ = librosa.load(wav_path, sr=44100, mono=True)
    x = torch.from_numpy(np.stack([y, y]).astype(np.float32))[None]
    ref = x.mean(1, keepdim=True); mu, sd = ref.mean(), ref.std() + 1e-8
    with torch.no_grad():
        out = apply_model(_demucs(), (x - mu) / sd, device="cpu", num_workers=0, split=True, overlap=0.25)
    m = _demucs(); v = (out[0, m.sources.index("vocals")] * sd + mu).mean(0).numpy()
    v16 = librosa.resample(v, orig_sr=44100, target_sr=16000).astype(np.float32)
    np.save(p, v16); return v16
def extract(wav_path):
    v = vokal16k(wav_path)
    f0, t = _rmvpe().infer_with_times(v, thred=THRED)
    return np.asarray(t, float), np.asarray(f0, float)
