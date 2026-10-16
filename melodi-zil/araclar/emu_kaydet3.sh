#!/usr/bin/env bash
# Gerçek izin diyaloğu akışı (Android 8-9): izin yokken "Dosya seç" → sistem diyaloğu "İzin ver" → (sistem uygulamayı yeniden başlatır) → tekrar dosya seç → sonuç → kaydet → doğrula.
EMU=${EMU:-emulator-5554}; export EMU; A="/home/user/Jn/android-sdk/platform-tools/adb -s $EMU"; S=${S:-/tmp/claude-0/-home-user-Jn/ab7d1d1d-5d4e-5029-854d-1ba47b6a0560/scratchpad}; U="python3 /home/user/Jn/melodi-zil/araclar/ui.py"
cd /home/user/Jn/melodi-zil; until grep -q "^EXIT" .gradle-release8.log; do sleep 10; done; grep "^EXIT" .gradle-release8.log
$A install -r -t app/build/outputs/apk/release/app-release.apk 2>&1 | tail -1
$A shell am force-stop com.jn.melodizil; $A shell pm revoke com.jn.melodizil android.permission.WRITE_EXTERNAL_STORAGE; $A shell appops set com.jn.melodizil WRITE_SETTINGS allow; $A logcat -c 2>/dev/null
$A shell am start -n com.jn.melodizil/.MainActivity >/dev/null 2>&1; sleep 25; $U texts | grep -q "Bekle" && { $U tap "Bekle"; sleep 4; }
echo "PID1=$($A shell pidof com.jn.melodizil)"
$U tap "Telefondan ses dosyası seç"; sleep 10; echo "DIYALOG:"; $U texts | head -4; $U ss $S/11_permission.png
$U tap "İZİN VER" 2>/dev/null || $U tap "İzin ver" 2>/dev/null || $U tap "ALLOW" 2>/dev/null; sleep 12
echo "PID2=$($A shell pidof com.jn.melodizil) (farklıysa sistem yeniden başlattı)"; echo "GROUPS: $($A shell "cat /proc/\$(pidof com.jn.melodizil)/status | grep Groups")"
$U texts | head -3
$U texts | grep -q "Telefondan ses dosyası seç" || { $A shell am start -n com.jn.melodizil/.MainActivity >/dev/null 2>&1; sleep 20; }
$U texts | grep -q "İndirilenler" || { $U tap "Telefondan ses dosyası seç"; sleep 15; $U desc "Kök dizinleri göster"; sleep 6; $U tap "İndirilenler"; sleep 8; $U xy 950 1200; sleep 6; }
$U tap "Never_Gonna"; sleep 20
n=0; until $U texts | grep -qE "Melodin hazır|Tekrar dene|durdu"; do n=$((n+1)); [ $n -gt 100 ] && break; sleep 20; done; echo "polls=$n"; $U texts | head -3
$A shell input swipe 540 1600 540 400 400; sleep 6; $U tap "Zil sesi yap"; sleep 12; $U ss $S/05_sheet.png
python3 - <<'PY'
import sys; sys.path.insert(0,"/home/user/Jn/melodi-zil/araclar"); import ui
n=ui.find(text="Kaydet"); print("kaydet bounds:", n.get("bounds") if n is not None else None)
if n is not None: ui.tap(text="Kaydet", wait=1)
PY
sleep 8; $U ss $S/06_saved.png; echo "LOG:"; $A logcat -d 2>/dev/null | grep -E "MelodiZil" | head -6
echo "ringtone=$($A shell settings get system ringtone)"; $A shell content query --uri content://media/external/audio/media --projection _id:_display_name:is_ringtone:_data --where "is_ringtone=1" 2>/dev/null | tail -2; $A shell ls -la /sdcard/Ringtones/MelodiZil/ 2>/dev/null
$U tap "Zillerim" 2>/dev/null; sleep 8; $U ss $S/07_library.png; $U texts | head -4
echo KAYDET3_BITTI
