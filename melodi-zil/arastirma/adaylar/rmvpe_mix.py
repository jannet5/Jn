# Aday: rmvpe_mix = RMVPE dogrudan karisik sese (vokal ayirma YOK).
# Sozlesme: extract(wav)->(times_sn, f0_hz); f0==0 sessiz. notes() yok -> f0'dan otomatik nota (olc.py).
import os, sys
sys.path.insert(0, "/home/user/Jn/melodi-zil/arastirma/ortak")
import numpy as np, torch, librosa
torch.set_num_threads(int(os.environ.get("OMP_NUM_THREADS", "2")))
from rmvpe_model import RMVPE
SR = 16000
THRED = float(os.environ.get("RMVPE_THRED", "0.2"))   # salience esigi (RVC varsayilani 0.03; karisimda 0.2 daha iyi OA/nota F)
MODEL = os.environ.get("RMVPE_MODEL", "/home/user/models/rmvpe/rmvpe.pt")
_m = None
def _model():
    global _m
    if _m is None: _m = RMVPE(MODEL)
    return _m
def extract(wav_path):
    y, _ = librosa.load(wav_path, sr=SR, mono=True)
    f0, t = _model().infer_with_times(y.astype(np.float32), thred=THRED)
    return t, f0

# ---- nota cikarimi (NOTA=1 ile acilir): f0 -> midi, medyan filtre, kisa bosluk doldurma, sonra yardim.f0_to_notes ----
import importlib.util
from scipy.ndimage import median_filter
_last = {}
_orig_extract = extract
def extract(wav_path):
    r = _orig_extract(wav_path); _last["p"] = wav_path; _last["r"] = r; return r
def notes(wav_path):
    spec = importlib.util.spec_from_file_location("yardim", "/home/user/Jn/melodi-zil/arastirma/olc/yardim.py")
    y = importlib.util.module_from_spec(spec); spec.loader.exec_module(y)
    t, f0 = _last["r"] if _last.get("p") == wav_path else _orig_extract(wav_path)
    if os.environ.get("NOTA", "0") != "1": return y.f0_to_notes(t, f0)
    W = int(os.environ.get("NOTA_W", "5")); GAP = int(os.environ.get("NOTA_GAP", "6"))
    v = f0 > 0
    m = np.where(v, 69 + 12 * np.log2(np.maximum(f0, 1e-9) / 440.0), 0.0)
    # kisa sessiz bosluklari (<=GAP kare) komsu perdeyle doldur
    i = 0; n = len(v)
    while i < n:
        if not v[i]:
            j = i
            while j < n and not v[j]: j += 1
            if 0 < i and j < n and (j - i) <= GAP and abs(m[i - 1] - m[j]) < 1.0:
                m[i:j] = np.linspace(m[i - 1], m[j], j - i + 2)[1:-1]; v[i:j] = True
            i = j
        else: i += 1
    # medyan filtre sadece sesli bolgelerde
    ms = median_filter(np.where(v, m, np.nan_to_num(m)), size=W, mode="nearest")
    f = np.where(v, 440.0 * 2 ** ((ms - 69) / 12), 0.0)
    return y.f0_to_notes(t, f)
