#!/usr/bin/env bash
# Yeni müşteri projesi: ajans/musteriler/<ad>/ klasörü + şablonlar + kurulum
# Kullanım: bash ajans/sistem/yeni-musteri.sh <musteri-adi> <web|mobil|web+mobil> [tek-aksiyon]
set -eu
AD="$1"; HAT="$2"; AKSIYON="${3:-}"
[ -n "$AKSIYON" ] || AKSIYON="WhatsApp'tan yaz"
KOK="$(cd "$(dirname "$0")/../.." && pwd)"
D="$KOK/ajans/musteriler/$AD"
mkdir -p "$D"/{ref,ss}
for f in CLAUDE.md BRIEF.md DESIGN.md; do
  sed -e "s|{{MUSTERI}}|$AD|g" -e "s|{{HAT}}|${HAT%%+*}|g" -e "s|{{AKSIYON}}|$AKSIYON|g" "$KOK/ajans/sistem/sablon/$f" > "$D/$f"
done
printf "# DERSLER — %s\n" "$AD" > "$D/DERSLER.md"
printf "# DURUM — %s\nHat: %s\n| Adım | Durum |\n|---|---|\n| Intake | ⏳ |\n| KAPI-1 DESIGN.md | ⬜ |\n| Üretim | ⬜ |\n| KAPI-2 Görsel QA | ⬜ |\n| KAPI-3 20 kontrol | ⬜ |\n| KAPI-4 Teslim | ⬜ |\n" "$AD" "$HAT" > "$D/DURUM.md"
echo "Hazır: $D"
echo "Sıradaki: BRIEF.md'yi intake formuyla doldur, sonra: bash ajans/sistem/kurulum.sh $D"
