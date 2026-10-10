# Değerlendirme seti hazırlar: (1) Vocadito solo vokal + sentetik pop eşlik (bas, akor, davul) karışımı, (2) Orchset gerçek orkestra kayıtları.
# Çıktı: <cikti>/<ad>.raw (f32le mono 22050 Hz) ve <ad>.ref.csv (zaman_sn, f0_hz; 0 = melodi yok)
import sys, os, csv, numpy as np, soundfile as sf, mirdata  # gerekli kütüphaneler
from scipy.signal import resample_poly  # yeniden örnekleme
SR = 22050  # analiz örnekleme hızı (uygulamayla aynı)
rng = np.random.default_rng(7)  # tekrarlanabilir gürültü

def mono_rs(path):  # dosyayı mono 22050'ye çevir
    x, sr = sf.read(path, always_2d=True); x = x.mean(axis=1)  # mono
    from math import gcd; g = gcd(sr, SR); return resample_poly(x, SR // g, sr // g).astype(np.float32)  # yeniden örnekle

def hz(m): return 440.0 * 2 ** ((m - 69) / 12)  # MIDI → Hz

def tone(f, n, kind):  # basit enstrüman sesleri
    t = np.arange(n) / SR
    if kind == "bass": y = np.sin(2*np.pi*f*t) + 0.3*np.sin(4*np.pi*f*t)  # bas
    else: y = sum(np.sin(2*np.pi*f*h*t)/h for h in range(1, 8))  # testere benzeri pad
    env = np.minimum(1, t / 0.02) * np.exp(-t * (1.5 if kind == "bass" else 0.4))  # zarf
    return (y * env).astype(np.float32)

def accompaniment(notes, total, bpm):  # vokal notalarından uyumlu eşlik üret
    out = np.zeros(total, np.float32); beat = int(SR * 60 / bpm); bar = beat * 4  # ölçü uzunluğu
    for b0 in range(0, total, bar):  # her ölçü
        t0, t1 = b0 / SR, (b0 + bar) / SR  # ölçünün zamanı
        ns = [m for (s, e, m) in notes if s < t1 and e > t0]  # ölçüdeki vokal notaları
        root = (int(np.median(ns)) % 12 if ns else 0) + 36  # kök: vokalin orta notası (pitch class), bas oktavı
        third = 4 if ((root + 4) % 12) in [n % 12 for n in ns] or not ns else 3  # majör/minör üçlü
        seg = min(bar, total - b0)
        for k in range(4):  # bas her vuruşta
            s = b0 + k * beat
            if s < total: n = min(beat, total - s); out[s:s+n] += 0.45 * tone(hz(root), n, "bass")
        for iv in (12, 12 + third, 19):  # akor: kök+üçlü+beşli bir oktav yukarı (orta bölge, vokalle çakışır)
            out[b0:b0+seg] += 0.12 * tone(hz(root + iv + 12), seg, "pad")
        for k in range(8):  # davul: kick 1-3, snare 2-4, hi-hat sekizlik
            s = b0 + k * beat // 2
            if s >= total: break
            n = min(int(0.12 * SR), total - s); t = np.arange(n) / SR
            if k % 4 == 0: out[s:s+n] += 0.6 * np.sin(2*np.pi*(60 + 80*np.exp(-t*30))*t) * np.exp(-t*25)  # kick
            if k % 4 == 2: out[s:s+n] += 0.25 * rng.standard_normal(n) * np.exp(-t*20)  # snare
            out[s:s+n] += 0.06 * rng.standard_normal(n) * np.exp(-t*60)  # hi-hat
    return out

def write(name, x, ref, cikti):
    x = x / (np.abs(x).max() + 1e-9) * 0.9  # normalize
    x.astype("<f4").tofile(os.path.join(cikti, name + ".raw"))  # ham PCM
    with open(os.path.join(cikti, name + ".ref.csv"), "w", newline="") as f: csv.writer(f).writerows(ref)  # referans f0

if __name__ == "__main__":
    data, cikti, snr_db = sys.argv[1], sys.argv[2], float(sys.argv[3]) if len(sys.argv) > 3 else 0.0  # vokal/eşlik oranı (dB)
    os.makedirs(cikti, exist_ok=True)
    voc = mirdata.initialize("vocadito", data_home=os.path.join(data, "vocadito"))
    for tid, tr in sorted(voc.load_tracks().items()):  # her vokal kaydı
        v = mono_rs(tr.audio_path)
        f0 = tr.f0; notes = [(iv[0], iv[1], int(round(69 + 12*np.log2(p/440)))) for iv, p in zip(tr.notes_a1.intervals, tr.notes_a1.pitches)] if tr.notes_a1 else []
        acc = accompaniment(notes, len(v), bpm=96)
        vr, ar = np.sqrt(np.mean(v**2)) + 1e-9, np.sqrt(np.mean(acc**2)) + 1e-9
        mix = v + acc * (vr / ar) * 10 ** (-snr_db / 20)  # vokal ile eşlik aynı enerjide (snr_db=0)
        ref = [(round(t, 4), round(fr, 2) if c > 0 and fr > 0 else 0.0) for t, fr, c in zip(f0.times, f0.frequencies, f0.voicing)]
        write(f"voc_{tid}", mix, ref, cikti)
    orc = mirdata.initialize("orchset", data_home=os.path.join(data, "orchset"))
    for tid, tr in sorted(orc.load_tracks().items())[:30]:  # ilk 30 orkestra parçası (gerçek kayıt)
        x = mono_rs(tr.audio_path_mono)
        m = tr.melody; ref = [(round(t, 4), round(fr, 2) if fr > 0 else 0.0) for t, fr in zip(m.times, m.frequencies)]
        write(f"orc_{tid}", x, ref, cikti)
    print("hazır:", len(os.listdir(cikti)) // 2, "parça")
