#!/usr/bin/env bash
# Emülatörde yerel dosya akışını baştan sona sürer: kur → aç → dosya seç → sonuç ekranı → dinle → zil sesi yap → Zillerim → koyu mod. Çıktılar $S altına yazılır.
EMU=${EMU:-emulator-5554}; export EMU; A="/home/user/Jn/android-sdk/platform-tools/adb -s $EMU"; S=${S:-/tmp/claude-0/-home-user-Jn/ab7d1d1d-5d4e-5029-854d-1ba47b6a0560/scratchpad}; U="python3 /home/user/Jn/melodi-zil/araclar/ui.py"
n=0; until [ "$($A shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ] && $A shell pm path android >/dev/null 2>&1; do n=$((n+1)); [ $n -gt 90 ] && exit 1; sleep 10; done; echo "BOOT ok ($n)"
$A shell settings put global hide_error_dialogs 1; $A shell settings put global http_proxy :0
$A install -r -t /home/user/Jn/melodi-zil/app/build/outputs/apk/release/app-release.apk 2>&1 | tail -1
$A shell appops set com.jn.melodizil WRITE_SETTINGS allow; $A shell pm grant com.jn.melodizil android.permission.WRITE_EXTERNAL_STORAGE
$A push $S/Never_Gonna_Give_You_Up.m4a /sdcard/Download/ >/dev/null 2>&1; $A shell am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file:///sdcard/Download/Never_Gonna_Give_You_Up.m4a >/dev/null 2>&1
$A logcat -c 2>/dev/null; $A shell am start -n com.jn.melodizil/.MainActivity >/dev/null 2>&1; sleep 25
$U texts | grep -q "Başla" && { $U tap "Başla"; sleep 6; }
echo "HOME:"; $U texts | head -3
$U tap "Telefondan ses dosyası seç"; sleep 15; $U desc "Kök dizinleri göster"; sleep 6; $U tap "İndirilenler"; sleep 8; $U tap "Never_Gonna"; sleep 15
$U ss $S/03_process.png; echo "PROCESS:"; $U texts | head -6
n=0; until $U texts | grep -qE "Melodin hazır|Tekrar dene|durdu"; do n=$((n+1)); [ $n -gt 100 ] && break; sleep 15; done; echo "polls=$n"
echo "RESULT:"; $U texts; $U ss $S/04_result.png
$A logcat -d -b crash 2>/dev/null | grep -A8 "FATAL EXCEPTION" | head -12
echo "AKIS_BITTI"
