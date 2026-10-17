#!/usr/bin/env bash
# Telefonun uygulamayı işlem sırasında öldürmesini taklit eder: işlem başlar → HOME → am kill (servis varken süreç ölmemeli) → su 0 kill -9 → yeniden aç → "yarıda kaldı / Tekrar dene" (boş ekran = HATA).
# Kullanım: APK=... AUDIO=<kisa_kesit.m4a|mp3> S=<ekran_goruntusu_klasoru> araclar/emu_kesinti.sh
EMU=${EMU:-emulator-5554}; export EMU; ADB_BIN=${ADB_BIN:-/home/user/android-sdk/platform-tools/adb}; export ADB_BIN
A="$ADB_BIN -s $EMU"; S=${S:?}; mkdir -p $S; U="python3 /home/user/Jn/melodi-zil/araclar/ui.py"
APK=${APK:-/home/user/Jn/melodi-zil/app/build/outputs/apk/debug/app-x86_64-debug.apk}; AUDIO=${AUDIO:?}; PKG=${PKG:-com.jn.melodizil.debug}
MODEL=${MODEL:-/home/user/models/rmvpe_q/rmvpe_int8.onnx}; AD=$(basename $AUDIO)
n=0; until [ "$($A shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = "1" ] && $A shell pm path android >/dev/null 2>&1; do n=$((n+1)); [ $n -gt 120 ] && { echo BOOT_FAIL; exit 1; }; sleep 5; done; echo "BOOT ok"
$A shell settings put global hide_error_dialogs 1
echo "KUR: $($A install -r -t $APK 2>&1 | tail -1)"
$A shell appops set $PKG WRITE_SETTINGS allow; $A shell pm grant $PKG android.permission.WRITE_EXTERNAL_STORAGE; $A shell pm grant $PKG android.permission.POST_NOTIFICATIONS 2>/dev/null
$A shell mkdir -p /sdcard/Android/data/$PKG/files/models; $A push $MODEL /sdcard/Android/data/$PKG/files/models/rmvpe_int8.onnx 2>&1 | tail -1
$A push $AUDIO /sdcard/Download/ >/dev/null 2>&1; $A shell am broadcast -a android.intent.action.MEDIA_SCANNER_SCAN_FILE -d file:///sdcard/Download/$AD >/dev/null 2>&1
$A logcat -c 2>/dev/null; $A shell am force-stop $PKG; $A shell am start -n $PKG/com.jn.melodizil.MainActivity >/dev/null 2>&1; sleep 25
$U texts | grep -q "Bekle" && { $U tap "Bekle"; sleep 4; }; $U texts | grep -q "Başla" && { $U tap "Başla"; sleep 6; }
$U tap "Telefondan ses dosyası seç"; sleep 15; $U desc "Kök dizinleri göster"; sleep 6; $U tap "İndirilenler"; sleep 8; $U tap "${AD%%.*}"; sleep 15
echo "ISLEM EKRANI:"; $U texts | head -9; $U ss $S/k01_isleniyor.png
echo "SERVIS:"; $A shell dumpsys activity services $PKG | grep -E "ServiceRecord|isForeground" | head -3
echo "BILDIRIM:"; $A shell dumpsys notification --noredact | grep -A2 "pkg=$PKG" | head -4
$A shell input keyevent 3; sleep 8
echo "pid_once=$($A shell pidof $PKG | tr -d '\r')"
$A shell am kill $PKG; sleep 3; echo "am_kill_sonrasi_pid=$($A shell pidof $PKG | tr -d '\r') (servis varken surec olmemeli; bossa HATA)"
$A root >/dev/null 2>&1; sleep 5; $A wait-for-device; P=$($A shell pidof $PKG | tr -d '\r'); $A shell "su 0 kill -9 $P" 2>/dev/null || $A shell kill -9 $P; sleep 4; echo "kill9_sonrasi_pid=$($A shell pidof $PKG | tr -d '\r')"
$A shell am start -n $PKG/com.jn.melodizil.MainActivity >/dev/null 2>&1; sleep 30
echo "YENIDEN ACILINCA:"; OUT=$($U texts); echo "$OUT" | head -8; $U ss $S/k02_yarida.png
[ -z "$OUT" ] && echo "HATA: bos ekran"
echo "$OUT" | grep -qE "Tekrar dene|yarıda" && echo "OK: yarida kaldi ekrani" || echo "UYARI: 'Tekrar dene' yok"
$U texts | grep -q "Tekrar dene" && { $U tap "Tekrar dene"; sleep 25; echo "TEKRAR DENE SONRASI:"; $U texts | head -8; $U ss $S/k03_tekrar.png; }
$A logcat -d -b crash 2>/dev/null | grep -A6 "FATAL" | head -8
echo KESINTI_BITTI
