# Paketlenmiş exe kabul akışı (Wine + Xvfb). Kullanım: S=<derleme+wineprefix dizini> O=<çıktı dizini> xvfb-run -a -s "-screen 0 640x620x24" bash exe_akis.sh
S="${S:?S= derleme ve wine prefix dizinini verin}"
export WINEPREFIX=$S/wine64pfx WINEDEBUG=-all PYTHONIOENCODING=utf-8
EXE=$S/build/dist/CevrimdisiTOTP/CevrimdisiTOTP.exe
O=${O:-$S}
YARDIMCI="$(cd "$(dirname "$0")" && pwd)/win_yardimci.py"
winpy() { wine 'C:\Py312\python.exe' "$YARDIMCI" "$@" </dev/null 2>&1 | tr -d '\r'; }
wine $EXE </dev/null >/dev/null 2>&1 &
sleep 22
import -window root $O/exe-1-acilis.png
xdotool mousemove 220 60 click 1; sleep 0.5; xdotool type --delay 40 "GitHub - exe testi"
xdotool mousemove 185 92 click 1; sleep 0.5; xdotool type --delay 40 "JBSWY3DPEHPK3PXP"
import -window root $O/exe-2-girdi.png
xdotool key Return; sleep 2
xdotool mousemove 322 197 click 1; sleep 1
BEKLENEN=$($S/venv/bin/python -c "import pyotp;print(pyotp.TOTP('JBSWY3DPEHPK3PXP').now())")
P1=$(winpy pano)
import -window root $O/exe-3-eklendi.png
echo "Enter ile eklendi + Kopyala: beklenen_kod=$BEKLENEN pano(Windows)=$P1"
[ "'$BEKLENEN'" = "$P1" ] && echo "EXE_KOPYALA_GECTI" || echo "EXE_KOPYALA_KALDI"
winpy kapat
sleep 2
CALISIYOR=$(ps -eo args | grep -c "^Z:.*CevrimdisiTOTP\.exe$")
P2=$(winpy pano)
echo "X düğmesi eşdeğeri (WM_CLOSE) ile normal kapanış: exe_süreç=$CALISIYOR pano_sonrası(Windows)=$P2"
[ "$CALISIYOR" = "0" ] && [ "$P2" = "''" ] && echo "EXE_ERKEN_KAPANIS_PANO_TEMIZ_GECTI" || echo "EXE_ERKEN_KAPANIS_KALDI"
wine $EXE </dev/null >/dev/null 2>&1 &
sleep 22
import -window root $O/exe-4-yeniden-acilis.png
xdotool mousemove 386 210 click 1; sleep 2
import -window root $O/exe-5-sil-onayi.png
xdotool mousemove 286 352 click 1; sleep 2
import -window root $O/exe-6-silindi.png
winpy kapat
wineserver -k
