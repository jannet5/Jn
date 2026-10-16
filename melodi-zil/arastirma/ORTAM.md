# ML ortami (Melodi Zil arastirma)

Sanal ortam: `/home/user/venv-ml` (Python 3.13.16, uv ile kuruldu, CPU).
Calistirma: `nice -n 10 /home/user/venv-ml/bin/python -I betik.py` (ve `OMP_NUM_THREADS=2`, `torch.set_num_threads(2)`).

## Kurulu surumler
torch 2.14.1+cpu, torchaudio 2.11.0+cpu (surumler farkli ama resample calisiyor), numpy 2.5.4, scipy 1.18.1,
librosa 1.0.0, soundfile 0.14.0, mir_eval 0.8.2, onnxruntime 1.31.0, onnx 1.23.2, demucs 4.1.0,
torchcrepe 0.0.24, basic-pitch 0.4.0 (onnx backend), torchfcpe 0.0.4, yt-dlp 2026.8.19, pretty_midi 0.2.11.

## Model yollari (/home/user/models)
- `/home/user/models/rmvpe/rmvpe.pt` (181 MB, torch) - HF lj1995/VoiceConversionWebUI
- `/home/user/models/rmvpe/rmvpe.onnx` (362 MB, onnxruntime; girdi `input`=mel, cikti `output`=360 sinif salience)
- `/home/user/models/demucs/955717e8-8726e21a.th` (84 MB, htdemucs) - dl.fbaipublicfiles.com (erisim var).
  Ayrica `/root/.cache/torch/hub/checkpoints/` altina kopyalandi, boylece `get_model("htdemucs")` indirmeden calisir.
- torchcrepe (tiny/full), torchfcpe (FCPE) ve basic-pitch (nmp.onnx) agirliklari pip paketinin icinde gelir.

## Duman testi (5 sn sentetik harmonik ses, 220 Hz -> 329.6 Hz, vibratolu; 2 thread, ajanlar paralel)
| Arac | Sonuc | Sure |
|---|---|---|
| RMVPE torch (.pt) | OK, 501 kare, 219.6 / 329.4 Hz | 4.4 sn (ilk yukleme dahil) |
| RMVPE onnx | OK, ayni sonuc (219.5 / 329.4 Hz) | 2.0 sn |
| torchcrepe (tiny) | OK, 220.3 / 329.3 Hz | 5.0 sn |
| torchfcpe (FCPE) | OK, 220.0 / 329.3 Hz | 0.25 sn |
| basic-pitch (onnx) | OK, notalar dondu (MIDI 57 ve 64) | 1.2 sn |
| demucs htdemucs | OK, 4 kaynak (drums, bass, other, vocals), cikti (1,4,2,220500) | 7.6 sn (5 sn ses) |
| librosa.yin, mir_eval, pretty_midi, onnx | OK | - |
| yt-dlp | KURULU ama YouTube'dan indirme CALISMIYOR | - |

## Nasil cagrilir
```python
import sys; sys.path.insert(0, "/home/user/Jn/melodi-zil/arastirma/ortak")
from rmvpe_model import RMVPE           # 16 kHz mono float32, 10 ms adim, f0=0 sessiz
f0 = RMVPE("/home/user/models/rmvpe/rmvpe.pt").infer_from_audio(audio16k, thred=0.03)   # torch
f0 = RMVPE("/home/user/models/rmvpe/rmvpe.onnx").infer_from_audio(audio16k, thred=0.03) # onnx
sal = RMVPE(...).salience(audio16k)     # (T,360) ham salience, 20 cent adim, 1997.38 cent'ten baslar
```
```python
# torchcrepe (16 kHz, hop 160)
import torchcrepe; p = torchcrepe.predict(torch.from_numpy(y)[None], 16000, 160, 50, 1000, model="tiny", device="cpu", batch_size=512)
# FCPE
from torchfcpe import spawn_bundled_infer_model
m = spawn_bundled_infer_model(device="cpu"); f0 = m.infer(torch.from_numpy(y)[None,:,None], sr=16000, decoder_mode="local_argmax", threshold=0.006)
# basic-pitch (onnx)
from basic_pitch.inference import predict, Model; from basic_pitch import ICASSP_2022_MODEL_PATH
mo, midi, notes = predict("ses.wav", Model(ICASSP_2022_MODEL_PATH))
# demucs htdemucs (44.1 kHz stereo girdi, (1,2,T))
from demucs.pretrained import get_model; from demucs.apply import apply_model
m = get_model("htdemucs"); out = apply_model(m, x, device="cpu", num_workers=0, split=True)  # kaynaklar: m.sources
```
Tum duman testi: `/home/user/venv-ml/bin/python -I /home/user/Jn/melodi-zil/arastirma/ortak/duman_test.py [libs rmvpe_pt rmvpe_onnx crepe fcpe bp demucs ytdlp]`

## Bilinen sorunlar
- **yt-dlp / YouTube**: bu makinenin IP'si icin YouTube "Sign in to confirm you're not a bot" (ve 429) veriyor.
  player_client (tv, ios, android_vr, web_safari, mweb, web_embedded) ve `--js-runtimes node` denendi, hepsi basarisiz.
  Cerez (cookies.txt) verilmedikce calismaz. Gercek sarki sesi icin baska yol gerek (kullanicidan wav/mp3, ya da
  lisansi uygun acik veri seti; HF/zenodo erisilebilir).
- `rmvpe_model.py` mel cikarma icin `torch.stft` kullanir (RVC'nin kendi STFT sinifi yerine); sonuc sentetik seste RVC ile uyumlu cikti.
- onnx modeli sigmoid salience dondurur; f0 cozumu (yerel ortalama cent) `to_local_average_cents` ile numpy'da yapilir.
