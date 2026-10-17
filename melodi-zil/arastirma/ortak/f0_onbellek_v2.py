# RMVPE ciktisini (kare basina: cents, salience-max, salience-toplam, rms) bir kere onbellege yazar.
# thred sonradan uygulanir: f0 = cents>0 & smax>thred. Kullanim: python -I f0_onbellek_v2.py
import os, sys, glob, importlib.util, numpy as np, torch, librosa
torch.set_num_threads(2)
sys.path.insert(0, "/home/user/Jn/melodi-zil/arastirma/ortak")
from rmvpe_model import RMVPE
spec = importlib.util.spec_from_file_location("yardim", "/home/user/Jn/melodi-zil/arastirma/olc/yardim.py")
Y = importlib.util.module_from_spec(spec); spec.loader.exec_module(Y)
OUT = "/home/user/cache/f0_v2"; SR = 16000
m = RMVPE("/home/user/models/rmvpe/rmvpe.pt")
isler = [("vocadito", a) for a in Y.liste("vocadito")] + [("mir1k", a) for a in Y.liste("mir1k", 30)]
for veri, ad in isler:
    out = f"{OUT}/{veri}__{ad}.npz"
    if os.path.exists(out): continue
    wav, rt, rf, riv, rph = Y.yukle(veri, ad, "mix")
    y, _ = librosa.load(wav, sr=SR, mono=True); y = y.astype(np.float32)
    sal = m.salience(y)                                   # (T,360)
    center = np.argmax(sal, 1); pad = np.pad(sal, ((0, 0), (4, 4))); c4 = center + 4
    idx = np.arange(9)[None, :] + (c4 - 4)[:, None]
    s9 = np.take_along_axis(pad, idx, 1); cm = m.cents_mapping[idx]
    cents = (s9 * cm).sum(1) / np.maximum(s9.sum(1), 1e-9)
    smax = sal.max(1); ssum = s9.sum(1)
    rms = librosa.feature.rms(y=y, frame_length=1024, hop_length=160, center=True)[0][:len(cents)]
    rms = np.pad(rms, (0, max(0, len(cents) - len(rms))))
    np.savez_compressed(out, cents=cents.astype(np.float32), smax=smax.astype(np.float32), ssum=ssum.astype(np.float32),
                        rms=rms.astype(np.float32), dur=len(y) / SR)
    print(veri, ad, len(cents), flush=True)
