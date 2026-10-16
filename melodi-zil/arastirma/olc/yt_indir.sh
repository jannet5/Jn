#!/bin/bash
# YouTube pop sarkilari indir: tam parca (m4a/opus -> wav 22050 mono) + 30 sn kesit. Kullanim: yt_indir.sh <cikti_klasoru>
OUT=${1:-/home/user/data/youtube}
YT=${YT:-/home/user/venv-ml/bin/yt-dlp}
mkdir -p "$OUT/tam" "$OUT/kesit30"
i=0
while IFS='|' read -r ad sorgu; do
  [ -z "$ad" ] && continue
  if [ ! -s "$OUT/tam/$ad.wav" ]; then
    for client in "" "--extractor-args youtube:player_client=android_vr" "--extractor-args youtube:player_client=tv" "--extractor-args youtube:player_client=web_embedded" "--extractor-args youtube:player_client=ios"; do
      nice -n 10 $YT $client --no-playlist -f "bestaudio" --max-filesize 40M -x --audio-format wav --postprocessor-args "ffmpeg:-ac 1 -ar 22050" -o "$OUT/tam/$ad.%(ext)s" "ytsearch1:$sorgu" > "$OUT/tam/$ad.log" 2>&1 && [ -s "$OUT/tam/$ad.wav" ] && break
    done
  fi
  [ -s "$OUT/tam/$ad.wav" ] && ffmpeg -loglevel error -y -ss 45 -t 30 -i "$OUT/tam/$ad.wav" "$OUT/kesit30/$ad.wav"
done <<'LIST'
tarkan_kuzu_kuzu|Tarkan Kuzu Kuzu official audio
tarkan_simarik|Tarkan Simarik official video
sezen_aksu_gulumse|Sezen Aksu Gulumse
ed_sheeran_shape_of_you|Ed Sheeran Shape of You official audio
adele_someone_like_you|Adele Someone Like You official audio
michael_jackson_billie_jean|Michael Jackson Billie Jean official audio
queen_bohemian_rhapsody|Queen Bohemian Rhapsody official video
LIST
