#!/usr/bin/env bash
# Derleme bitince kur, yerel dosya akışını sür, kaydet ve logcat ile MediaStore'u doğrula.
EMU=${EMU:-emulator-5554}; export EMU; A="/home/user/Jn/android-sdk/platform-tools/adb -s $EMU"; S=${S:-/tmp/claude-0/-home-user-Jn/ab7d1d1d-5d4e-5029-854d-1ba47b6a0560/scratchpad}; U="python3 /home/user/Jn/melodi-zil/araclar/ui.py"
cd /home/user/Jn/melodi-zil; until grep -q "^EXIT" .gradle-release6.log; do sleep 10; done; grep "^EXIT" .gradle-release6.log
$A install -r -t app/build/outputs/apk/release/app-release.apk 2>&1 | tail -1
$A shell appops set com.jn.melodizil WRITE_SETTINGS allow; $A shell pm grant com.jn.melodizil android.permission.WRITE_EXTERNAL_STORAGE; $A logcat -c 2>/dev/null
$A shell am start -n com.jn.melodizil/.MainActivity >/dev/null 2>&1; sleep 25; $U texts | grep -q "Bekle" && { $U tap "Bekle"; sleep 4; }
$U tap "Telefondan ses dosyası seç"; sleep 15; $U desc "Kök dizinleri göster"; sleep 6; $U tap "İndirilenler"; sleep 8; $U xy 950 1200; sleep 6; $U tap "Never_Gonna"; sleep 20
n=0; until $U texts | grep -qE "Melodin hazır|Tekrar dene|durdu"; do n=$((n+1)); [ $n -gt 100 ] && break; sleep 20; done; echo "polls=$n"; $U texts | head -3
$A shell input swipe 540 1600 540 500 400; sleep 5; $U tap "Zil sesi yap"; sleep 10; $U ss $S/05_sheet.png
python3 - <<'PY'
import sys, time, os; sys.path.insert(0,"/home/user/Jn/melodi-zil/araclar"); import ui
n=ui.find(text="Kaydet"); print("kaydet bounds:", n.get("bounds") if n is not None else None)
if n is not None: ui.tap(text="Kaydet", wait=2)
for _ in range(12):
    t=[x for x in ui.texts() if any(k in x for k in ("Kaydedildi","ayarlandı","Kaydedilemedi","izin"))]
    if t: print("MESAJ:", t); break
ui.screenshot(os.environ.get("S","/tmp")+"/06_saved.png")
PY
echo "LOG:"; $A logcat -d 2>/dev/null | grep -E "MelodiZil" | head -12
echo "ringtone=$($A shell settings get system ringtone)"; $A shell content query --uri content://media/external/audio/media --projection _id:_display_name:is_ringtone:_data --where "is_ringtone=1" 2>/dev/null | tail -2; $A shell ls -la /sdcard/Ringtones/MelodiZil/ 2>/dev/null
echo KAYDET_BITTI
