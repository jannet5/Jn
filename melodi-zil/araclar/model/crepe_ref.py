# Telefondaki CREPE yolunun birebir Python karşılığı (Kotlin CrepeMelody'yi doğrulamak ve ayar seçmek için).
# Kullanım (sp ortamı): python crepe_ref.py <crepe_tiny.tflite> <ses_dizini (*.raw f32le mono 22050)> <cikti_dizini> [hop_ornek=160] [kapı_dB=0 (0 = model her karede)]
# Çıktı: <ad>.f0.csv = zaman, f0_hz, güven (seçilen kutunun olasılığı), enerji_dB (%95'lik kare enerjisine göre)
import sys, os, glob, numpy as np
from scipy.signal import resample_poly  # 22050 → 16000
W, NB, LO, HI = 1024, 360, 39, 308  # pencere, kutu sayısı, 50 Hz ve 1100 Hz kutuları (torchcrepe fmin/fmax)
LOGT = np.log((12 - np.abs(np.arange(-11, 12))) / 144.0)  # bantlı geçiş (torchcrepe: max(12-|i-j|, 0), satır toplamı 144)

def frames_energy(x, hop):  # kare enerjisi (dB, %95'liğe göre); kareler merkezli (W/2 sıfır dolgu)
    p = np.pad(x, (W // 2, W // 2)); n = 1 + len(x) // hop
    rms = np.array([np.sqrt(np.mean(p[i * hop:i * hop + W] ** 2)) for i in range(n)])
    return p, n, 20 * np.log10(np.maximum(rms, 1e-9) / max(np.percentile(rms, 95), 1e-9))

def viterbi(obs):  # obs: [n, R] softmax gözlem olasılığı (aralık içi) → kutu yolu (aralık içi indeks)
    n, R = obs.shape; lo = np.log(np.maximum(obs, 1e-30)); v = lo[0] - np.log(NB); bp = np.zeros((n, R), np.int8)
    for t in range(1, n):
        best = np.full(R, -np.inf); arg = np.zeros(R, np.int8)
        for k, d in enumerate(range(-11, 12)):  # önceki durum i = j + d
            src = np.full(R, -np.inf); a, b = max(0, -d), min(R, R - d); src[a:b] = v[a + d:b + d] + LOGT[k]
            upd = src > best; best[upd] = src[upd]; arg[upd] = d
        v = best + lo[t]; bp[t] = arg
    path = np.zeros(n, int); path[-1] = int(np.argmax(v))
    for t in range(n - 1, 0, -1): path[t - 1] = path[t] + bp[t, path[t]]
    return path

def track(x16, it, hop, gate):
    p, n, edb = frames_energy(x16, hop); probs = np.zeros((n, NB), np.float32); run = np.ones(n, bool) if gate <= 0 else edb > -gate
    idx = np.nonzero(run)[0]; B = 256; inp = it.get_input_details()[0]["index"]; outp = it.get_output_details()[0]["index"]
    for s in range(0, len(idx), B):  # toplu çalıştır
        ii = idx[s:s + B]; fr = np.stack([p[i * hop:i * hop + W] for i in ii]).astype(np.float32)
        fr = (fr - fr.mean(1, keepdims=True)) / np.maximum(fr.std(1, ddof=1, keepdims=True), 1e-10)  # kare normalizasyonu
        it.resize_tensor_input(inp, [len(ii), W]); it.allocate_tensors(); it.set_tensor(inp, fr); it.invoke(); probs[ii] = it.get_tensor(outp)
    r = probs[:, LO:HI].astype(np.float64); e = np.exp(r - r.max(1, keepdims=True)); obs = e / e.sum(1, keepdims=True)  # softmax (torchcrepe viterbi)
    obs[~run] = 1.0 / (HI - LO)  # model çalışmayan karede gözlem yok (tekdüze)
    path = viterbi(obs) + LO; conf = probs[np.arange(n), path]; conf[~run] = 0  # güven: seçilen kutunun olasılığı
    f0 = 10 * 2 ** ((20 * path + 1997.3794084376191) / 1200)
    return np.arange(n) * hop / 16000.0, f0, conf, edb

def dump(model, raw, outdir, sec=6.0, hop=320, gate=15.0):  # Kotlin testi için örnek: 16 kHz ses, model çıktıları (çalışan kareler sırasıyla), beklenen iz
    import tensorflow as tf
    it = tf.lite.Interpreter(model_path=model, num_threads=4); x = np.fromfile(raw, dtype="<f4")
    x16 = resample_poly(x, 320, 441).astype(np.float32)[: int(sec * 16000)]
    rows = []; orig = it.invoke
    def rec():  # her toplu çağrının çıktısını sırayla kaydet
        orig(); rows.append(it.get_tensor(it.get_output_details()[0]["index"]).copy())
    it.invoke = rec
    t, f, c, e = track(x16, it, hop, gate)
    os.makedirs(outdir, exist_ok=True); x16.astype("<f4").tofile(os.path.join(outdir, "crepe_ses16k.raw"))
    np.concatenate(rows).astype("<f4").tofile(os.path.join(outdir, "crepe_olasilik.raw"))
    np.savetxt(os.path.join(outdir, "crepe_beklenen.csv"), np.stack([t, f, c, e], 1), delimiter=",", fmt="%.5f")

if __name__ == "__main__":
    if sys.argv[1] == "dump": dump(sys.argv[2], sys.argv[3], sys.argv[4]); sys.exit()  # python crepe_ref.py dump <tflite> <ses.raw> <cikti_dizini>
    import tensorflow as tf
    model, adir, odir = sys.argv[1:4]; hop = int(sys.argv[4]) if len(sys.argv) > 4 else 160; gate = float(sys.argv[5]) if len(sys.argv) > 5 else 0
    it = tf.lite.Interpreter(model_path=model, num_threads=4); os.makedirs(odir, exist_ok=True)
    for raw in sorted(glob.glob(os.path.join(adir, "*.raw"))):
        x = np.fromfile(raw, dtype="<f4"); x16 = resample_poly(x, 320, 441).astype(np.float32)
        t, f, c, e = track(x16, it, hop, gate)
        np.savetxt(os.path.join(odir, os.path.basename(raw)[:-4] + ".f0.csv"), np.stack([t, f, c, e], 1), delimiter=",", fmt="%.4f")
