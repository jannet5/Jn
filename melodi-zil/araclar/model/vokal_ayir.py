# Telefondaki işlem hattının birebir Python karşılığı (doğrulama için): 22050 mono → 44100 → STFT(4096/1024, periyodik Hann) →
# TFLite vokal U-Net (512 karelik parçalar) → maske = çıktı/|X| → vokal STFT → iSTFT → 22050 mono.
import sys, glob, os, shutil, numpy as np  # temel
from scipy.signal import resample_poly  # yeniden örnekleme
N, H, T, F = 4096, 1024, 512, 1024  # Spleeter ayarları
win = 0.5 - 0.5 * np.cos(2 * np.pi * np.arange(N) / N)  # periyodik Hann

def stft(x):
    pad = np.pad(x, (N // 2, N // 2)); n = 1 + (len(pad) - N) // H  # merkezli
    fr = np.stack([pad[i*H:i*H+N] * win for i in range(n)]); return np.fft.rfft(fr, axis=1)  # [kare, 2049]

def istft(X, length):
    n = X.shape[0]; out = np.zeros(N + (n - 1) * H); wsum = np.zeros_like(out)
    fr = np.fft.irfft(X, n=N, axis=1) * win
    for i in range(n): out[i*H:i*H+N] += fr[i]; wsum[i*H:i*H+N] += win ** 2
    out = out / np.maximum(wsum, 1e-8); return out[N // 2: N // 2 + length]

def separate(x22, interp):
    x = resample_poly(x22, 2, 1)  # 44100
    X = stft(x); mag = np.abs(X[:, :F]).astype(np.float32)  # 0-11 kHz genlik
    nT = mag.shape[0]; pad = (-nT) % T; magp = np.pad(mag, ((0, pad), (0, 0)))
    vin, vout = interp.get_input_details()[0]["index"], interp.get_output_details()[0]["index"]
    vm = np.zeros_like(magp)
    for s in range(0, magp.shape[0], T):  # 512 karelik parçalar
        seg = np.stack([magp[s:s+T], magp[s:s+T]], axis=-1)[None]  # mono → 2 kanal
        interp.set_tensor(vin, seg); interp.invoke(); vm[s:s+T] = interp.get_tensor(vout)[0].mean(axis=-1)
    mask = np.clip(vm[:nT] / (mag + 1e-10), 0, 1)  # maske
    Xv = np.zeros_like(X); Xv[:, :F] = X[:, :F] * mask  # 11 kHz üstü sıfır (mask_extension: zeros)
    v = istft(Xv, len(x)); return resample_poly(v, 1, 2)  # 22050

if __name__ == "__main__":
    import tensorflow as tf
    model, src, dst = sys.argv[1], sys.argv[2], sys.argv[3]; os.makedirs(dst, exist_ok=True)
    it = tf.lite.Interpreter(model_path=model, num_threads=4); it.allocate_tensors()
    for r in sorted(glob.glob(os.path.join(src, "voc_*.raw"))):
        name = os.path.basename(r)[:-4]; x = np.fromfile(r, dtype="<f4").astype(np.float64)
        v = separate(x, it); v = (v / (np.abs(v).max() + 1e-9) * 0.9).astype("<f4"); v.tofile(os.path.join(dst, name + ".raw"))
        shutil.copy(os.path.join(src, name + ".ref.csv"), os.path.join(dst, name + ".ref.csv"))
    print("ayrıldı:", len(glob.glob(os.path.join(dst, "*.raw"))))
