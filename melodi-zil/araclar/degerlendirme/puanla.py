# Puanlama: notaları (veya Essentia çıktısını) 10 ms f0 dizisine çevirip mir_eval melodi ölçütleriyle referansa karşı puanlar.
# RPA = doğru perde oranı (melodi olan karelerde, ±50 cent); RCA = oktav hatasını yok sayan RPA; OA = genel doğruluk (sessizlik dahil); VR/VFA = melodi var/yok tespiti.
import sys, os, glob, csv, numpy as np, mir_eval  # gerekli kütüphaneler

def load_ref(p):
    a = np.loadtxt(p, delimiter=",", ndmin=2); return a[:, 0], a[:, 1]  # zaman, f0

def notes_to_f0(p, times):
    f = np.zeros_like(times)  # 0 = sessiz
    if os.path.getsize(p) == 0: return f
    for s, d, m in np.loadtxt(p, delimiter=",", ndmin=2):  # her nota
        f[(times >= s) & (times < s + d)] = 440 * 2 ** ((m - 69) / 12)  # notanın frekansı
    return f

def essentia_f0(raw, times):  # karşılaştırma: Essentia PredominantPitchMelodia (Salamon & Gómez 2012 orijinal uygulaması)
    import essentia.standard as es
    x = np.fromfile(raw, dtype="<f4")
    # EqualLoudness 22050 Hz desteklemiyor; Melodia doğrudan çalıştırılır
    pitch, _ = es.PredominantPitchMelodia(sampleRate=22050, frameSize=2048, hopSize=128)(x)
    t = np.arange(len(pitch)) * 128 / 22050
    return np.interp(times, t, pitch, left=0, right=0) * (np.interp(times, t, (pitch > 0).astype(float)) > 0.5)

def note_f1(refp, estp):  # nota düzeyi: doğru zamanda (±50 ms başlangıç) doğru perde (±50 cent); bitiş dikkate alınmaz
    r = np.loadtxt(refp, delimiter=",", ndmin=2)
    e = np.loadtxt(estp, delimiter=",", ndmin=2) if os.path.getsize(estp) else np.zeros((0, 3))
    ei = np.stack([e[:, 0], e[:, 0] + e[:, 1]], axis=1) if len(e) else np.zeros((0, 2)); ep = 440 * 2 ** ((e[:, 2] - 69) / 12) if len(e) else np.zeros(0)
    p, rc, f, _ = mir_eval.transcription.precision_recall_f1_overlap(r[:, :2], r[:, 2], ei, ep, offset_ratio=None)
    return [p, rc, f]

def score(ref_t, ref_f, est_f):
    r = mir_eval.melody.evaluate(ref_t, ref_f, ref_t, est_f)
    return [r["Raw Pitch Accuracy"], r["Raw Chroma Accuracy"], r["Overall Accuracy"], r["Voicing Recall"], r["Voicing False Alarm"]]

if __name__ == "__main__":
    d = sys.argv[1]; with_ess = "--essentia" in sys.argv
    rows = {"voc": [], "orc": []}; ess = {"voc": [], "orc": []}; ceil = {"voc": [], "orc": []}  # tavan: referansın yarım tona yuvarlanmış hali (nota çıktısının ulaşabileceği en iyi puan)
    for ref in sorted(glob.glob(os.path.join(d, "*.ref.csv"))):
        name = os.path.basename(ref)[:-8]; t, f = load_ref(ref)
        rows[name[:3]].append(score(t, f, notes_to_f0(os.path.join(d, name + ".notes.csv"), t)))
        q = np.where(f > 0, 440 * 2 ** (np.round(69 + 12 * np.log2(np.maximum(f, 1e-9) / 440)) / 12 - 69 / 12), 0); ceil[name[:3]].append(score(t, f, q))
        if with_ess: ess[name[:3]].append(score(t, f, essentia_f0(os.path.join(d, name + ".raw"), t)))
    nf = []  # nota düzeyi puanlar
    for rn in sorted(glob.glob(os.path.join(d, "*.refnotes.csv"))):
        name = os.path.basename(rn)[:-len(".refnotes.csv")]; est = os.path.join(d, name + ".notes.csv")
        if os.path.exists(est): nf.append(note_f1(rn, est))
    print("set   yöntem     RPA    RCA    OA     VR     VFA   (n)")
    for k in rows:
        for lab, R in (("bizim", rows[k]), ("essentia", ess[k]), ("tavan", ceil[k])):
            if R: m = np.mean(R, axis=0); print(f"{k}   {lab:9s} " + " ".join(f"{v:.3f}" for v in m) + f"  ({len(R)})" + (f"  NOTA P/R/F {np.mean(nf, axis=0)[0]:.3f}/{np.mean(nf, axis=0)[1]:.3f}/{np.mean(nf, axis=0)[2]:.3f}" if (k == "voc" and lab == "bizim" and nf) else ""))
