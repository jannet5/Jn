# Referans aday: librosa.pyin (klasik, ML'siz). Sozlesme: extract(wav)->(times, f0_hz); f0==0 sessiz.
import numpy as np, librosa
SR = 16000; HOP = 320  # 20 ms
def extract(wav_path):
    y, sr = librosa.load(wav_path, sr=SR, mono=True)
    f0, vf, _ = librosa.pyin(y, fmin=librosa.note_to_hz("C2"), fmax=librosa.note_to_hz("C6"), sr=SR, frame_length=1024, hop_length=HOP)
    t = librosa.times_like(f0, sr=SR, hop_length=HOP)
    return t, np.where(vf & np.isfinite(f0), f0, 0.0)
