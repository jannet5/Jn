#!/bin/bash
# BAT sarmalayıcılarının (1) PowerShell çıkış kodunu AYNEN taşıdığını, (2) argümanları (boşluklu/Türkçe) bozmadan
# ilettiğini test eder. ORTAM: Linux + Wine cmd.exe + mingw ile derlenen SAHTE powershell.exe.
# Bu gerçek Windows cmd.exe / Windows PowerShell testi DEĞİLDİR; yalnız BAT mantığını sınar.
set -u
export LC_ALL=C.UTF-8 WINEDEBUG=-all
KOK="$(cd "$(dirname "$0")/../../ses-temizleyici" && pwd)"
IS="$(mktemp -d /tmp/battest.XXXXXX)"
x86_64-w64-mingw32-gcc -municode -O2 -o "$IS/powershell.exe" "$(dirname "$0")/sahte_powershell.c" || exit 2
cp "$KOK"/*.bat "$IS/"; cd "$IS"
LOG="Z:${IS//\//\\}\\log.txt"
G=0; K=0
dene() { # $1 bat, $2 sahte kod, $3 beklenen kod, $4 logda beklenen parça (boş = log yok), geri kalan: argümanlar
  local b="$1" kod="$2" bek="$3" parca="$4"; shift 4; rm -f log.txt
  printf '\r\n\r\n' | FAKE_LOG="$LOG" FAKE_KOD="$kod" timeout 120 wine cmd /c "$b" "$@" > cikti.txt 2>&1; local g=$?
  local argv=""; [ -f log.txt ] && argv="$(tr -d '\r\357\273\277' < log.txt | tr '\n' ' ')"
  if [ "$g" == "$bek" ] && { [ -z "$parca" ] && [ ! -f log.txt ] || [[ "$argv" == *"$parca"* ]]; }; then
    echo "  GEÇTİ: $b (sahte $kod) -> $g  $argv"; G=$((G+1))
  else echo "  KALDI: $b (sahte $kod) -> $g, beklenen $bek; argv: $argv"; K=$((K+1)); fi
}
dene Ses-Temizle.bat 0 0 'Ses-Temizle.ps1] [Z:\k\a.wav]' 'Z:\k\a.wav'
dene Ses-Temizle.bat 3 3 '[Z:\k\boşluklu ad ğüşİ.wav] [Z:\k\b.wav]' 'Z:\k\boşluklu ad ğüşİ.wav' 'Z:\k\b.wav'
dene Ses-Temizle.bat 2 2 '[Z:\k\a.wav]' 'Z:\k\a.wav'
dene Ses-Temizle-Dogal.bat 0 0 'Ses-Temizle.ps1] [-Guc] [30] [Z:\k\a.wav]' 'Z:\k\a.wav'
dene Ses-Temizle-Dogal.bat 3 3 '[-Guc] [30]' 'Z:\k\a.wav'
dene Kayit-Kontrol.bat 0 0 '[-Kontrol] [Z:\k\a.wav]' 'Z:\k\a.wav'
dene Kayit-Kontrol.bat 3 3 '[-Kontrol]' 'Z:\k\a.wav'
dene Kurulum.bat 0 0 'Kurulum.ps1]'
dene Kurulum.bat 3 3 'Kurulum.ps1]'
dene Kurulum.bat 4 4 'Kurulum.ps1]'
dene Ses-Temizle.bat 0 1 ''          # argümansız: PowerShell çağrılmaz, kod 1
echo "SONUÇ: $G geçti, $K kaldı"
cd /; rm -rf "$IS"
[ "$K" -eq 0 ]
