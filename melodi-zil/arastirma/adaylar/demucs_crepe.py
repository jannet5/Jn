# Aday: htdemucs ile vokal ayir (onbellekli) -> torchcrepe (viterbi, periodicity esigi). extract(wav)->(times, f0_hz); f0==0 sessiz.
import os, sys, hashlib, numpy as np, soundfile as sf, librosa, torch
torch.set_num_threads(int(os.environ.get("OMP_NUM_THREADS", "2")))
CACHE = "/home/user/cache/demucs"
MODEL = os.environ.get("CREPE_MODEL", "tiny"); PTHR = float(os.environ.get("CREPE_PTHR", "0.21"))
_dm = None
def _demucs():
    global _dm
    if _dm is None:
        from demucs.pretrained import get_model
        _dm = get_model("htdemucs").eval()
    return _dm
def vokal16k(wav_path):
    """Vokali 16 kHz mono dondurur. Onbellek: <klip>_vocals.wav (16k); demucs_rmvpe'nin .npy onbellegi de yeniden kullanilir."""
    os.makedirs(CACHE, exist_ok=True)
    st = os.stat(wav_path)
    key = hashlib.sha1(f"{os.path.abspath(wav_path)}|{st.st_size}|{st.st_mtime_ns}".encode()).hexdigest()[:20]
    pn = f"{CACHE}/{key}.npy"
    if os.path.exists(pn): return np.load(pn)
    pw = f"{CACHE}/{os.path.splitext(os.path.basename(wav_path))[0]}_vocals.wav"
    if os.path.exists(pw):
        y, sr = sf.read(pw, dtype="float32"); return y if sr == 16000 else librosa.resample(y, orig_sr=sr, target_sr=16000)
    from demucs.apply import apply_model
    y, _ = librosa.load(wav_path, sr=44100, mono=True)
    x = torch.from_numpy(np.stack([y, y]).astype(np.float32))[None]
    ref = x.mean(1, keepdim=True); mu, sd = ref.mean(), ref.std() + 1e-8
    with torch.no_grad():
        out = apply_model(_demucs(), (x - mu) / sd, device="cpu", num_workers=0, split=True, overlap=0.25)
    m = _demucs(); v = (out[0, m.sources.index("vocals")] * sd + mu).mean(0).numpy()
    v16 = librosa.resample(v, orig_sr=44100, target_sr=16000).astype(np.float32)
    tmp = pw + f".tmp{os.getpid()}.wav"; sf.write(tmp, v16, 16000); os.replace(tmp, pw)
    return v16
def extract(wav_path):
    import torchcrepe
    v = vokal16k(wav_path)
    a = torch.from_numpy(v)[None]
    with torch.no_grad():
        f0, per = torchcrepe.predict(a, 16000, 160, 50, 1000, model=MODEL, decoder=torchcrepe.decode.viterbi,
                                     return_periodicity=True, device="cpu", batch_size=512)
        per = torchcrepe.filter.median(per, 3); f0 = torchcrepe.filter.mean(f0, 3)
    f0 = f0[0].numpy().astype(float); per = per[0].numpy()
    f0[per < PTHR] = 0.0
    t = np.arange(len(f0)) * 0.01
    return t, f0
