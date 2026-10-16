# Olcum aleti yardimcilari: veri yukleme, f0 -> nota donusumu, puanlama.
import os, glob, csv, numpy as np, soundfile as sf

MIR1K = "/home/user/data/mir1k/veri/MIR-1K"
MIR1K_SES = "/home/user/data/mir1k/ses"   # turetilmis mix/vokal wav onbellegi (olc.py yazar)
VOCADITO = "/home/user/data/vocadito/veri"

def hz2midi(f): return 69 + 12 * np.log2(np.maximum(f, 1e-9) / 440.0)
def midi2hz(m): return 440.0 * 2 ** ((np.asarray(m, float) - 69) / 12)

def f0_to_notes(times, f0, min_dur=0.06, max_gap=0.05, tol=0.5):
    """f0 egrisini notalara boler: yarim ton yuvarlama, medyan, kisa notalari at.
    Doner: intervals Nx2 [sn], pitches_hz N."""
    times = np.asarray(times, float); f0 = np.asarray(f0, float)
    if len(times) < 2: return np.zeros((0, 2)), np.zeros(0)
    hop = float(np.median(np.diff(times)))
    v = f0 > 0
    m = np.where(v, hz2midi(f0), 0.0)
    notes = []; i = 0; n = len(m)
    while i < n:
        if not v[i]: i += 1; continue
        j = i; seg = [m[i]]
        while j + 1 < n and v[j + 1] and abs(m[j + 1] - np.median(seg[-7:])) < 1.0 + tol: j += 1; seg.append(m[j])
        notes.append([times[i], times[j] + hop, float(np.median(seg))]); i = j + 1
    # ayni perdeli, kisa bosluklu notalari birlestir
    out = []
    for s, e, p in notes:
        if out and s - out[-1][1] <= max_gap and abs(round(p) - round(out[-1][2])) < 1: out[-1][1] = e
        else: out.append([s, e, p])
    out = [(s, e, p) for s, e, p in out if e - s >= min_dur]
    if not out: return np.zeros((0, 2)), np.zeros(0)
    iv = np.array([[s, e] for s, e, _ in out]); ph = midi2hz([round(p) for _, _, p in out])
    return iv, ph

def liste(veri, limit=None):
    """Klip adlari (sirali); limit verilirse esit aralikli ornekler (tek sarkici yanlisini onler)."""
    if veri == "mir1k": adlar = sorted(os.path.basename(p)[:-4] for p in glob.glob(f"{MIR1K}/Wavfile/*.wav"))
    elif veri == "vocadito":
        adlar = sorted((os.path.basename(p)[:-4] for p in glob.glob(f"{VOCADITO}/Audio/*.wav")), key=lambda s: int(s.split("_")[1]))
    else: raise SystemExit(f"bilinmeyen veri: {veri}")
    if limit and limit < len(adlar): adlar = [adlar[int(k)] for k in np.linspace(0, len(adlar) - 1, limit)]
    return adlar

def yukle(veri, ad, mod="mix"):
    """Doner: wav_yolu, ref_times, ref_f0, ref_intervals, ref_pitches_hz"""
    if veri == "mir1k":
        p = np.loadtxt(f"{MIR1K}/PitchLabel/{ad}.pv", ndmin=1)
        t = 0.02 * (np.arange(len(p)) + 1)                    # ilk kare merkezi 20 ms, hop 20 ms
        f = np.where(p > 0, midi2hz(p), 0.0)
        d = os.path.join(MIR1K_SES, mod); os.makedirs(d, exist_ok=True); wav = os.path.join(d, ad + ".wav")
        if not os.path.exists(wav):
            x, sr = sf.read(f"{MIR1K}/Wavfile/{ad}.wav", always_2d=True)   # sol=enstruman, sag=vokal
            y = x[:, 1] if mod == "vokal" else 0.5 * (x[:, 0] + x[:, 1])   # karisim = sol+sag (olcekli)
            sf.write(wav, y.astype(np.float32), sr)
        iv, ph = f0_to_notes(t, f)                                          # referans notalar f0'dan
        return wav, t, f, iv, ph
    f0 = np.loadtxt(f"{VOCADITO}/Annotations/F0/{ad}_f0.csv", delimiter=",", ndmin=2)
    nt = np.loadtxt(f"{VOCADITO}/Annotations/Notes/{ad}_notesA1.csv", delimiter=",", ndmin=2)
    iv = np.stack([nt[:, 0], nt[:, 0] + nt[:, 2]], axis=1); ph = nt[:, 1]
    return f"{VOCADITO}/Audio/{ad}.wav", f0[:, 0], f0[:, 1], iv, ph

def puanla_f0(ref_t, ref_f, est_t, est_f):
    import mir_eval
    est_f = np.asarray(est_f, float); est_t = np.asarray(est_t, float)
    r = mir_eval.melody.evaluate(ref_t, ref_f, est_t, est_f)
    # oktav-hatasiz RPA: tahmin, referansin 2^k (|k|<=2) katina 50 cent icindeyse dogru
    if len(est_t):
        _f, _v = mir_eval.melody.resample_melody_series(est_t, np.abs(est_f), est_f > 0, ref_t)
        et = np.where(_v.astype(bool), _f, 0.0)
    else: et = np.zeros_like(ref_t)
    rv = ref_f > 0; ev = et > 0; both = rv & ev
    if rv.sum() == 0: oct_rpa = float("nan")
    else:
        c = 1200 * np.log2(np.where(both, et, 1) / np.where(both, ref_f, 1))
        ok = both & (np.min(np.abs(c[:, None] - 1200 * np.arange(-2, 3)[None, :]), axis=1) < 50)
        oct_rpa = float(ok.sum() / rv.sum())
    return {"RPA": r["Raw Pitch Accuracy"], "RCA": r["Raw Chroma Accuracy"], "OA": r["Overall Accuracy"],
            "VR": r["Voicing Recall"], "VFA": r["Voicing False Alarm"], "RPA_oktavsiz": oct_rpa}

def puanla_nota(ref_iv, ref_p, est_iv, est_p):
    import mir_eval
    if len(ref_iv) == 0: return {"nota_P": float("nan"), "nota_R": float("nan"), "nota_F": float("nan")}
    if len(est_iv) == 0: return {"nota_P": 0.0, "nota_R": 0.0, "nota_F": 0.0}
    p, r, f, _ = mir_eval.transcription.precision_recall_f1_overlap(ref_iv, ref_p, est_iv, est_p, onset_tolerance=0.05, pitch_tolerance=50.0, offset_ratio=None)
    return {"nota_P": p, "nota_R": r, "nota_F": f}
