# Telefon sürümünün birebir karşılığı: 22050 Hz'de STFT (2048/512, periyodik Hann) — 44.1k/4096/1024 ile aynı bin aralığı (10.77 Hz) ve süre.
# Genlik ×2 (4096 örneklik pencere eşdeğeri) → TFLite vokal U-Net → maske = çıktı / (2|X|) → vokal STFT → iSTFT (22050).
import sys, glob, os, shutil, numpy as np
N, H, T, F = 2048, 512, 512, 1024
win = 0.5 - 0.5 * np.cos(2 * np.pi * np.arange(N) / N)
def separate(x, it):
    pad = np.pad(x, (N // 2, N // 2)); n = 1 + (len(pad) - N) // H
    vin, vout = it.get_input_details()[0]["index"], it.get_output_details()[0]["index"]
    out = np.zeros(len(pad)); wsum = np.zeros(len(pad))
    for s in range(0, n, T):  # 512 karelik parçalar (telefonda da parça parça: bellek dostu)
        idx = list(range(s, min(s + T, n)))
        X = np.fft.rfft(np.stack([pad[i*H:i*H+N] * win for i in idx]), axis=1)  # [k, 1025]
        mag = np.abs(X[:, :F]).astype(np.float32) * 2  # ölçek eşleme
        inp = np.zeros((T, F), np.float32); inp[:len(idx)] = mag
        it.set_tensor(vin, np.stack([inp, inp], -1)[None]); it.invoke(); vm = it.get_tensor(vout)[0].mean(-1)[:len(idx)]
        mask = np.clip(vm / (mag + 1e-10), 0, 1); Xv = np.zeros_like(X); Xv[:, :F] = X[:, :F] * mask
        fr = np.fft.irfft(Xv, n=N, axis=1) * win
        for j, i in enumerate(idx): out[i*H:i*H+N] += fr[j]; wsum[i*H:i*H+N] += win ** 2
    return (out / np.maximum(wsum, 1e-8))[N // 2: N // 2 + len(x)]
if __name__ == "__main__":
    import tensorflow as tf
    model, src, dst = sys.argv[1:4]; os.makedirs(dst, exist_ok=True)
    it = tf.lite.Interpreter(model_path=model, num_threads=4); it.allocate_tensors()
    for r in sorted(glob.glob(os.path.join(src, "voc_*.raw"))):
        name = os.path.basename(r)[:-4]; v = separate(np.fromfile(r, dtype="<f4").astype(np.float64), it)
        (v / (np.abs(v).max() + 1e-9) * 0.9).astype("<f4").tofile(os.path.join(dst, name + ".raw"))
        for ext in (".ref.csv", ".refnotes.csv"):
            if os.path.exists(os.path.join(src, name + ext)): shutil.copy(os.path.join(src, name + ext), os.path.join(dst, name + ext))
    print("ayrıldı:", len(glob.glob(os.path.join(dst, "*.raw"))))
