# Aday: crepe_tiny_mix = torchcrepe "tiny" (~0.5 M parametre) dogrudan karisik sese (vokal ayirma YOK).
# Sozlesme: extract(wav)->(times_sn, f0_hz); f0==0 sessiz.
# Ortam degiskenleri: CREPE_MODEL (tiny|full), CREPE_THR (periodicity esigi, 0.21 varsayilan torchcrepe),
#   CREPE_FMIN / CREPE_FMAX (Hz), CREPE_DEC (viterbi|argmax|weighted_argmax)
import os
import numpy as np, torch, librosa, torchcrepe
torch.set_num_threads(int(os.environ.get("OMP_NUM_THREADS", "2")))
SR = 16000
MODEL = os.environ.get("CREPE_MODEL", "tiny")
THR = float(os.environ.get("CREPE_THR", "0.21"))
FMIN = float(os.environ.get("CREPE_FMIN", "65"))
FMAX = float(os.environ.get("CREPE_FMAX", "1100"))
DEC = os.environ.get("CREPE_DEC", "viterbi")
def extract(wav_path):
    y, _ = librosa.load(wav_path, sr=SR, mono=True)
    x = torch.from_numpy(y.astype(np.float32))[None]
    dec = {"viterbi": torchcrepe.decode.viterbi, "argmax": torchcrepe.decode.argmax,
           "weighted_argmax": torchcrepe.decode.weighted_argmax}[DEC]
    with torch.no_grad():
        f0, per = torchcrepe.predict(x, SR, 160, FMIN, FMAX, model=MODEL, decoder=dec, return_periodicity=True,
                                     batch_size=512, device="cpu", pad=True)
    f0 = f0[0].numpy().astype(float); per = per[0].numpy()
    f0 = np.where(per >= THR, f0, 0.0)
    t = np.arange(len(f0)) * 0.01
    return t, f0
