#!/usr/bin/env bash
# Telefonun uygulamayı işlem sırasında arka planda öldürmesini taklit eder:
# işlem başlar → ön plan servisi ve bildirim var mı → HOME → süreç kill -9 (düşük bellek öldürmesi gibi) → yeniden aç → "yarıda kaldı" ekranı → Tekrar dene.
EMU=${EMU:-emulator-5554}; export EMU; A="/home/user/Jn/android-sdk/platform-tools/adb -s $EMU"; S=${S:?}; U="python3 /home/user/Jn/melodi-zil/araclar/ui.py"
APK=${APK:-/home/user/Jn/melodi-zil/app/build/outputs/apk/release/app-x86-release.apk}
n=0; until [ "$($A shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ] && $A shell pm path android >/dev/null 2>&1; do n=$((n+1)); [ $n -gt 120 ] && { echo BOOT_FAIL; exit 1; }; sleep 10; done; echo "BOOT ok"
$A shell settings put global hide_error_dialogs 1
$A install -r -t $APK 2>&1 | tail -1
$A shell pm grant com.jn.melodizil android.permission.WRITE_EXTERNAL_STORAGE
$A push $S/Kesit_30sn.m4a /sdcard/Download/ >/dev/null 2>&1; $A shell am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file:///sdcard/Download/Kesit_30sn.m4a >/dev/null 2>&1
$A logcat -c 2>/dev/null; $A shell am force-stop com.jn.melodizil; $A shell am start -n com.jn.melodizil/.MainActivity >/dev/null 2>&1; sleep 30
$U texts | grep -q "Bekle" && { $U tap "Bekle"; sleep 4; }; $U texts | grep -q "Başla" && { $U tap "Başla"; sleep 6; }
$U tap "Telefondan ses dosyası seç"; sleep 15; $U desc "Kök dizinleri göster"; sleep 6; $U tap "İndirilenler"; sleep 8; $U xy 950 1200; sleep 6; $U tap "Kesit_30sn"; sleep 25
echo "ISLEM EKRANI:"; $U texts | head -9; $U ss $S/k01_isleniyor.png
echo "SERVIS:"; $A shell dumpsys activity services com.jn.melodizil | grep -E "ServiceRecord|isForeground" | head -3
echo "BILDIRIM:"; $A shell dumpsys notification --noredact | grep -A2 "pkg=com.jn.melodizil" | head -4
$A shell input keyevent 3; sleep 8
P1=$($A shell pidof com.jn.melodizil | tr -d '\r'); echo "pid_once=$P1"
$A shell am kill com.jn.melodizil; sleep 3; P2=$($A shell pidof com.jn.melodizil | tr -d '\r'); echo "am_kill_sonrasi_pid=$P2 (servis varken telefon öldürmemeli)"
$A root >/dev/null 2>&1; sleep 5; $A wait-for-device; P=$($A shell pidof com.jn.melodizil | tr -d '\r'); $A shell kill -9 $P; sleep 4; echo "kill9_sonrasi_pid=$($A shell pidof com.jn.melodizil | tr -d '\r')"
$A shell am start -n com.jn.melodizil/.MainActivity >/dev/null 2>&1; sleep 30
echo "YENIDEN ACILINCA:"; $U texts | head -8; $U ss $S/k02_yarida.png
$U texts | grep -q "Tekrar dene" && { $U tap "Tekrar dene"; sleep 25; echo "TEKRAR DENE SONRASI:"; $U texts | head -8; $U ss $S/k03_tekrar.png; }
$A logcat -d -b crash 2>/dev/null | grep -A6 "FATAL" | head -8
echo KESINTI_BITTI
