#!/usr/bin/env bash
# kaynaklar.txt içindeki her URL'ye HTTP isteği atıp durum kodunu yazar.
# 2xx/3xx = erişilebilir; 403/429 = sunucu bot engeli (sayfa var, tarayıcıdan açılır); diğerleri = KONTROL.
set -u
dosya="$(dirname "$0")/../kaynaklar.txt"
sorun=0
while IFS='|' read -r tur url; do
  tur="$(echo "$tur" | xargs)"; url="$(echo "$url" | xargs)"
  [[ -z "$url" || "$tur" == \#* ]] && continue
  kod=$(curl -sS -L --http1.1 -o /dev/null -w '%{http_code}' --max-time 45 -A 'Mozilla/5.0' "$url" 2>/dev/null || true)
  case "$kod" in 2*|3*) d=OK ;; 403|429) d=ENGEL ;; *) d=KONTROL; sorun=$((sorun+1)) ;; esac
  printf '%-8s %s %-8s %s\n' "$d" "$kod" "$tur" "$url"
done < "$dosya"
echo "KONTROL gereken: $sorun"
[ "$sorun" -eq 0 ]
