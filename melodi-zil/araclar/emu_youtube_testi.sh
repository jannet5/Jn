#!/usr/bin/env bash
# YouTube akışını emülatörde uçtan uca sürer: derleme + açılış bekle → kur → proxy CA → bağlantı yapıştır → işlem → sonuç → 8-Bit → kaydet → doğrula.
EMU=${EMU:-emulator-5554}; export EMU; A="/home/user/Jn/android-sdk/platform-tools/adb -s $EMU"; S=${S:?}; PORT=${PORT:?}; U="python3 /home/user/Jn/melodi-zil/araclar/ui.py"
cd /home/user/Jn/melodi-zil; until grep -q "^EXIT" .gradle-release11.log; do sleep 10; done; grep "^EXIT" .gradle-release11.log
n=0; until [ "$($A shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ] && $A shell pm path android >/dev/null 2>&1; do n=$((n+1)); [ $n -gt 90 ] && { echo BOOT_FAIL; exit 1; }; sleep 10; done; echo "BOOT ok"
ADB="$A" bash araclar/emulator_proxy_ca.sh $S/cacerts $PORT 2>&1 | tail -1
$A shell settings put global hide_error_dialogs 1
$A install -r -t app/build/outputs/apk/release/app-release.apk 2>&1 | tail -1
$A shell pm grant com.jn.melodizil android.permission.WRITE_EXTERNAL_STORAGE; $A shell appops set com.jn.melodizil WRITE_SETTINGS allow; $A logcat -c 2>/dev/null
$A shell am force-stop com.jn.melodizil; $A shell am start -n com.jn.melodizil/.MainActivity >/dev/null 2>&1; sleep 25; $U texts | grep -q "Bekle" && { $U tap "Bekle"; sleep 4; }
$U texts | grep -q "Başla" && { $U tap "Başla"; sleep 6; }
$U ss $S/y01_home.png; $U texts | head -2
# Paylaş akışını taklit et: YouTube uygulamasındaki "Paylaş" ile aynı intent (ACTION_SEND text/plain)
$A shell am start -a android.intent.action.SEND -t text/plain --es android.intent.extra.TEXT "https://youtu.be/dQw4w9WgXcQ" -n com.jn.melodizil/.MainActivity >/dev/null 2>&1; sleep 20
$U ss $S/y02_process.png; echo "PROCESS:"; $U texts | head -6
n=0; until $U texts | grep -qE "Melodin hazır|Tekrar dene|durdu"; do n=$((n+1)); [ $n -gt 100 ] && break; sleep 20; done; echo "polls=$n"; $U texts | head -4; $U ss $S/y03_result.png
$U tap "8-Bit"; sleep 25
$A shell input swipe 540 1600 540 400 400; sleep 6; $U tap "Zil sesi yap"; sleep 12
python3 - <<'PY'
import sys; sys.path.insert(0,"/home/user/Jn/melodi-zil/araclar"); import ui
n=ui.find(text="Kaydet"); print("kaydet bounds:", n.get("bounds") if n is not None else None)
if n is not None: ui.tap(text="Kaydet", wait=1)
PY
sleep 10; $U ss $S/y04_saved.png; echo "LOG:"; $A logcat -d 2>/dev/null | grep -E "MelodiZil" | head -3
echo "ringtone=$($A shell settings get system ringtone)"; $A shell content query --uri content://media/external/audio/media --projection _id:_display_name:is_ringtone --where "is_ringtone=1" 2>/dev/null | tail -2
$A shell input keyevent 3; sleep 6; $U ss $S/y05_launcher.png
$A logcat -d -b crash 2>/dev/null | grep -A5 "FATAL" | head -6
echo YOUTUBE_BITTI
