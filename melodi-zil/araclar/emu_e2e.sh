#!/usr/bin/env bash
# Yeni uygulamayı emülatörde uçtan uca sürer: kur → izin → model+ses it → dosya seç → melodi → zil yap → sistem zili değişti mi.
# Kullanım: APK=... AUDIO=... S=<ekran_goruntusu_klasoru> araclar/emu_e2e.sh
EMU=${EMU:-emulator-5554}; export EMU; ADB_BIN=${ADB_BIN:-/home/user/android-sdk/platform-tools/adb}; export ADB_BIN
A="$ADB_BIN -s $EMU"; S=${S:?}; mkdir -p $S; U="python3 /home/user/Jn/melodi-zil/araclar/ui.py"
APK=${APK:-/home/user/Jn/melodi-zil/app/build/outputs/apk/debug/app-x86_64-debug.apk}; AUDIO=${AUDIO:?}; PKG=${PKG:-com.jn.melodizil.debug}
MODEL=${MODEL:-/home/user/models/rmvpe_q/rmvpe_int8.onnx}; AD=$(basename $AUDIO)
n=0; until [ "$($A shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ] && $A shell pm path android >/dev/null 2>&1; do n=$((n+1)); [ $n -gt 120 ] && { echo BOOT_FAIL; exit 1; }; sleep 5; done; echo "BOOT ok $(date -u +%T)"
$A shell settings put global hide_error_dialogs 1
echo "KUR: $($A install -r -t $APK 2>&1 | tail -1) $(date -u +%T)"
$A shell appops set $PKG WRITE_SETTINGS allow; $A shell pm grant $PKG android.permission.WRITE_EXTERNAL_STORAGE; $A shell pm grant $PKG android.permission.READ_EXTERNAL_STORAGE 2>/dev/null
$A shell mkdir -p /sdcard/Android/data/$PKG/files/models; $A push $MODEL /sdcard/Android/data/$PKG/files/models/rmvpe_int8.onnx 2>&1 | tail -1
$A push $AUDIO /sdcard/Download/ >/dev/null 2>&1; $A shell am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file:///sdcard/Download/$AD >/dev/null 2>&1
$A logcat -c 2>/dev/null; $A shell am start -n $PKG/com.jn.melodizil.MainActivity >/dev/null 2>&1; sleep 25
$U texts | grep -q "Bekle" && { $U tap "Bekle"; sleep 4; }; $U texts | grep -q "Başla" && { $U tap "Başla"; sleep 6; }
echo "ANA EKRAN:"; $U texts | head -4; $U ss $S/01_ana.png
T0=$(date +%s); $U tap "Telefondan ses dosyası seç"; sleep 15; $U desc "Kök dizinleri göster"; sleep 6; $U tap "İndirilenler"; sleep 8; $U tap "${AD%%.*}"; sleep 20
$U ss $S/02_islem.png; echo "ISLEM EKRANI:"; $U texts | head -8
n=0; until $U texts | grep -qE "Melodin hazır|Tekrar dene|durdu|yarıda"; do n=$((n+1)); [ $n -gt ${BEKLE:-60} ] && break; sleep 10; done
echo "SONUC (sure $(( $(date +%s) - T0 )) sn, polls=$n):"; $U texts | head -8; $U ss $S/03_sonuc.png
$A logcat -d -b crash 2>/dev/null | grep -A8 "FATAL EXCEPTION" | head -12
$A shell input swipe 540 1600 540 500 400; sleep 4; $U tap "Zil sesi yap"; sleep 8; $U ss $S/04_kaydet_sayfasi.png
$U tap "Kaydet"; sleep 6; $U ss $S/05_kaydedildi.png; $U texts | head -6
echo "ringtone=$($A shell settings get system ringtone)"
$A shell content query --uri content://media/external/audio/media --projection _id:_display_name:is_ringtone --where "is_ringtone=1" 2>/dev/null | tail -2
echo E2E_BITTI
