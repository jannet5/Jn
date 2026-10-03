#!/bin/bash
# Karşılaştırma testinin tamamını yeniden üretir (Linux; ffmpeg, pwsh, deep-filter, Python: numpy scipy soundfile pystoi onnxruntime librosa).
# Gerekli dış dosyalar (çalışma klasörüne):
#   clean_{0,2,3,4,5,6}.wav : https://media.githubusercontent.com/media/microsoft/DNS-Challenge/interspeech2020/master/datasets/test_set/synthetic/no_reverb/clean/clean_fileid_N.wav
#   sig_bak_ovr.onnx        : https://raw.githubusercontent.com/microsoft/DNS-Challenge/master/DNSMOS/DNSMOS/sig_bak_ovr.onnx
#   sh.rnnn                 : https://raw.githubusercontent.com/GregorR/rnnoise-models/master/somnolent-hogwash-2018-09-01/sh.rnnn
#   deep-filter (PATH'te veya ../ses-temizleyici/araclar/ içinde): https://github.com/Rikorose/DeepFilterNet/releases/tag/v0.5.6
# Audacity adımı için: Audacity 3.7.x + mod-script-pipe açık, makro ~/.audacity-data/Macros/Ses-Temizle.txt
set -e
BETIK="$(cd "$(dirname "$0")/../../ses-temizleyici" && pwd)/Ses-Temizle.ps1"
mkdir -p work bench
python3 sim.py
cd bench
for k in kayit1 kayit2; do
  cp ../work/${k}_ham.wav ${k}.wav
  ../ln.sh ${k}.wav ${k}__0_ham_seviye.wav "highpass=f=80"
  ffmpeg -loglevel error -y -i ${k}.wav -af "highpass=f=80,arnndn=m=../sh.rnnn" -c:a pcm_s24le t.wav && ../ln.sh t.wav ${k}__3_ffmpeg_rnnoise.wav && rm t.wav
  pwsh -NoProfile -File "$BETIK" -Mod klasik ${k}.wav >/dev/null && mv ${k}_temiz.wav ${k}__2_betik_klasik.wav
  for g in 18 30; do pwsh -NoProfile -File "$BETIK" -Guc $g ${k}.wav >/dev/null && mv ${k}_temiz.wav ${k}__4_betik_ai_guc$g.wav; done
  pwsh -NoProfile -File "$BETIK" ${k}.wav && mv ${k}_temiz.wav ${k}__4_betik_ai_varsayilan.wav
  # Audacity (her kayıt için Audacity yeniden başlatılmalı ki gürültü profili sıfırlansın):
  #   python3 ../audacity_pipe.py "Import2: Filename=$PWD/${k}.wav" "Select: Start=0.5 End=2.5 Track=0" "NoiseReduction:" \
  #       "SelectAll:" "Macro_Ses-Temizle:" "Export2: Filename=$PWD/${k}__1_audacity_makro.wav NumChannels=1"
done
cd ..
for f in bench/kayit*__*.wav; do k=$(basename $f .wav | grep -o "kayit[12]"); python3 olc.py $f work/${k}_referans_temiz.wav; done > sonuclar.jsonl
