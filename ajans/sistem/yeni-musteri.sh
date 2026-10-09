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
mkdir -p "$D/.claude/agents" "$D/.claude/skills"
KOPYA="$KOK/ajans/arastirma/github/kopyalar"
cp "$KOPYA/OneRedOak-claude-code-workflows/design-review-agent.md" "$D/.claude/agents/" 2>/dev/null || true
cp -r "$KOPYA/hallmark" "$D/.claude/skills/hallmark" 2>/dev/null || true
cp -r "$KOK/ajans/sistem/skills/tr-caption" "$D/.claude/skills/tr-caption"
case "$HAT" in web*) mkdir -p "$D/scripts"; cp "$KOK/ajans/sistem/sablon/web/scripts/"* "$D/scripts/";; esac
printf "# DERSLER — %s\n" "$AD" > "$D/DERSLER.md"
printf "# DURUM — %s\nHat: %s\n| Adım | Durum |\n|---|---|\n| Intake | ⏳ |\n| KAPI-1 DESIGN.md | ⬜ |\n| Üretim | ⬜ |\n| KAPI-2 Görsel QA | ⬜ |\n| KAPI-3 20 kontrol | ⬜ |\n| KAPI-4 Teslim | ⬜ |\n" "$AD" "$HAT" > "$D/DURUM.md"
echo "Hazır: $D"
echo "Sıradaki: BRIEF.md'yi intake formuyla doldur, sonra: bash ajans/sistem/kurulum.sh $D"
