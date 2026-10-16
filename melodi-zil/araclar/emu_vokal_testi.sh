#!/usr/bin/env bash
# TFLite vokal ayırmanın gerçek Android'de çalıştığını doğrular: kur → 30 sn kesit → dosya akışı → sonuç → logcat kontrolü.
EMU=${EMU:-emulator-5554}; export EMU; A="/home/user/Jn/android-sdk/platform-tools/adb -s $EMU"; S=${S:?}; U="python3 /home/user/Jn/melodi-zil/araclar/ui.py"
n=0; until [ "$($A shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ] && $A shell pm path android >/dev/null 2>&1; do n=$((n+1)); [ $n -gt 120 ] && { echo BOOT_FAIL; exit 1; }; sleep 10; done; echo "BOOT ok"
$A shell settings put global hide_error_dialogs 1
$A install -r -t /home/user/Jn/melodi-zil/app/build/outputs/apk/release/app-release.apk 2>&1 | tail -1
$A shell pm grant com.jn.melodizil android.permission.WRITE_EXTERNAL_STORAGE; $A shell appops set com.jn.melodizil WRITE_SETTINGS allow
$A push $S/Kesit_30sn.m4a /sdcard/Download/ >/dev/null 2>&1; $A shell am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file:///sdcard/Download/Kesit_30sn.m4a >/dev/null 2>&1
$A logcat -c 2>/dev/null; $A shell am force-stop com.jn.melodizil; $A shell am start -n com.jn.melodizil/.MainActivity >/dev/null 2>&1; sleep 30
$U texts | grep -q "Bekle" && { $U tap "Bekle"; sleep 4; }; $U texts | grep -q "Başla" && { $U tap "Başla"; sleep 6; }
$U tap "Telefondan ses dosyası seç"; sleep 15; $U desc "Kök dizinleri göster"; sleep 6; $U tap "İndirilenler"; sleep 8; $U xy 950 1200; sleep 6; $U tap "Kesit_30sn"; sleep 20
echo "ADIMLAR:"; $U texts | head -8; $U ss $S/v01_process.png
T0=$(date +%s); n=0; until $U texts | grep -qE "Melodin hazır|Tekrar dene|durdu"; do n=$((n+1)); [ $n -gt 180 ] && break; sleep 15; $U texts | grep -q "Vokal ayrıştırılıyor" && [ -z "$SEP" ] && { SEP=1; $U ss $S/v02_separating.png; }; done
echo "sure_sn=$(( $(date +%s) - T0 )) polls=$n"; $U texts | head -4; $U ss $S/v03_result.png
echo "LOG:"; $A logcat -d 2>/dev/null | grep -E "MelodiZil|tflite|TfLite|XNNPACK" | head -8
$A logcat -d -b crash 2>/dev/null | grep -A6 "FATAL" | head -8
echo VOKAL_BITTI
