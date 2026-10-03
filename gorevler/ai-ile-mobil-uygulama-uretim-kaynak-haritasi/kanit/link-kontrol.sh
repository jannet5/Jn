#!/usr/bin/env bash
# harita.md içindeki tüm https bağlantılarını çıkarır ve kontrol eder.
# github.com depoları `git ls-remote` ile (bu ortamda github HTML'i proxy'de 403), diğerleri curl ile.
# Kullanım: bash kanit/link-kontrol.sh  (görev klasöründen)
set -u
cd "$(dirname "$0")/.."
OUT=kanit/link-kontrol.md
grep -oE 'https://[^ )>`"|]+' harita.md | sed -E 's/[.,;:]+$//' | grep -v '{' | grep -v '<' | sort -u > kanit/linkler.txt
{
  echo "# Bağlantı kontrolü"
  echo
  echo "Tarih (UTC): $(date -u +%Y-%m-%dT%H:%M:%SZ) · Kaynak: harita.md · Toplam: $(wc -l < kanit/linkler.txt)"
  echo
  echo "| URL | Yöntem | Sonuç |"
  echo "|---|---|---|"
  while read -r u; do
    if [[ "$u" =~ ^https://github\.com/[^/]+/[^/]+$ ]]; then
      h=$(timeout 40 git ls-remote "$u" HEAD 2>/dev/null | head -1 | cut -c1-12)
      if [ -n "$h" ]; then echo "| $u | git ls-remote | OK (HEAD $h) |"; else echo "| $u | git ls-remote | HATA |"; fi
    else
      c=000
      for deneme in 1 2 3; do
        c=$(curl -sS -o /dev/null -L --max-time 30 -A "Mozilla/5.0 (link-kontrol)" -w "%{http_code}" "$u" 2>/dev/null); c=${c:-000}
        [ "$c" != "000" ] && break; sleep 2
      done
      if [[ "$u" =~ ^https://mcp\. ]] && [[ "$c" =~ ^(401|405|406)$ ]]; then
        echo "| $u | curl | OK-MCP ($c) — canlı MCP uç noktası, OAuth/POST bekliyor |"; continue
      fi
      case "$c" in
        2??) s="OK ($c)";;
        403|429|405) s="BOT-ENGELI/HIZ ($c) — sunucu yanıt veriyor, tarayıcıda kontrol";;
        *) s="HATA ($c)";;
      esac
      echo "| $u | curl | $s |"
    fi
  done < kanit/linkler.txt
} > "$OUT"
echo "OK=$(grep -c '| OK' "$OUT") BOT=$(grep -c 'BOT-ENGELI' "$OUT") HATA=$(grep -c '| HATA' "$OUT")"
