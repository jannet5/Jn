S="${S:?S= derleme ve wine prefix dizinini verin}"
export WINEPREFIX=$S/wine64pfx WINEDEBUG=-all
EXE=$S/build/dist/CevrimdisiTOTP/CevrimdisiTOTP.exe
wine $EXE </dev/null >/dev/null 2>&1 &
sleep 10
xdotool mousemove 220 60 click 1; sleep 0.5; xdotool type --delay 40 "GitHub - exe testi"
xdotool mousemove 185 92 click 1; sleep 0.5; xdotool type --delay 40 "JBSWY3DPEHPK3PXP"
import -window root $S/exe-2-girdi.png
xdotool mousemove 390 92 click 1; sleep 2
xdotool mousemove 322 207 click 1; sleep 1
BEKLENEN=$($S/venv/bin/python -c "import pyotp;print(pyotp.TOTP('JBSWY3DPEHPK3PXP').now())")
PANO=$(timeout 5 xclip -o -selection clipboard 2>/dev/null)
import -window root $S/exe-3-eklendi.png
echo "beklenen_kod=$BEKLENEN pano=$PANO"
[ "$BEKLENEN" = "$PANO" ] && echo "EXE_KOPYALA_GECTI" || echo "EXE_KOPYALA_KALDI"
wineserver -k; sleep 2
wine $EXE </dev/null >/dev/null 2>&1 &
sleep 10
import -window root $S/exe-4-yeniden-acilis.png
wineserver -k
