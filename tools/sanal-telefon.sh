#!/usr/bin/env bash
# Sende on a virtual phone with a REAL app: the stock Android "Messaging" (SMS) app posts the notifications,
# Sende captures them, and this script reads the screen back like a person would.
#
#   tools/sanal-telefon.sh <apk> <output-dir>
#
# Needs: a running emulator on Android 11+ (system image "default"/AOSP, which ships com.android.messaging),
# English locale, `adb` and `python3` on PATH. Writes screenshots, the notification dump and the screen text
# into <output-dir>. Exit code 0 only if every check passed.
#
# Why this exists (docs/SORUNLAR_COZUMLER.md S-23): hand-written test notifications never showed that the
# stock SMS app rewrites ONE notification as "a", "a\nb", "a\nb\nc"; only a real app did.
set -euo pipefail

APK=${1:?usage: sanal-telefon.sh <apk> <output-dir>}
OUT=${2:?usage: sanal-telefon.sh <apk> <output-dir>}
PKG=${SENDE_PACKAGE:-com.sende.app.preview}
mkdir -p "$OUT"
FAILED=0

say() { printf '%s\n' "$*" | tee -a "$OUT/olay-gunlugu.txt"; }
fail() { say "FAIL: $*"; FAILED=1; }
pass() { say "ok:   $*"; }

wait_boot() {
  adb wait-for-device
  until [ "$(adb shell getprop sys.boot_completed | tr -d '\r')" = "1" ]; do sleep 5; done
  # A slow emulator can still be scanning packages: the package manager answers only when it is ready.
  until adb shell pm path android >/dev/null 2>&1; do sleep 3; done
}

# Prints the visible screen as "text<TAB>left top right bottom", one line per node that has text.
ui_dump() {
  adb shell uiautomator dump /sdcard/sende-ui.xml >/dev/null 2>&1 || true
  adb exec-out cat /sdcard/sende-ui.xml | python3 -I -c '
import re, sys, xml.etree.ElementTree as ET
raw = sys.stdin.read()
if not raw.strip():
    sys.exit(0)
for n in ET.fromstring(raw).iter("node"):
    text = n.get("text") or n.get("content-desc") or ""
    if text:
        print(text.replace("\n", " ") + "\t" + " ".join(re.findall(r"\d+", n.get("bounds"))))
'
}

# Taps the centre of the first node whose text is exactly $1.
tap_text() {
  local want=$1 coords
  for _ in $(seq 1 20); do
    coords=$(ui_dump | python3 -I -c '
import sys
want = sys.argv[1]
for row in sys.stdin:
    text, box = row.rstrip("\n").split("\t")
    if text == want:
        l, t, r, b = map(int, box.split())
        print((l + r) // 2, (t + b) // 2)
        break
' "$want")
    if [ -n "$coords" ]; then
      # shellcheck disable=SC2086
      adb shell input tap $coords
      return 0
    fi
    sleep 3
  done
  fail "could not find \"$want\" on screen"
  return 1
}

shot() { adb exec-out screencap -p >"$OUT/$1.png"; }

# How many nodes on screen contain the text $1.
count_texts() {
  ui_dump | python3 -I -c '
import sys
want = sys.argv[1]
print(sum(1 for row in sys.stdin if want in row.split("\t")[0]))
' "$1"
}

# The number printed right after the node with text $1 (the unread badge of a channel chip).
badge_after() {
  ui_dump | python3 -I -c '
import sys
want = sys.argv[1]
texts = [row.split("\t")[0] for row in sys.stdin]
for i, t in enumerate(texts[:-1]):
    if t == want and texts[i + 1].isdigit():
        print(texts[i + 1])
        break
' "$1"
}

sms() {
  adb emu sms send "$1" "$2" >/dev/null
  sleep "${SMS_PAUSE:-8}"
}

say "== virtual phone check, $(date -u +%FT%TZ) =="
wait_boot
adb shell settings put global window_animation_scale 0
adb shell settings put global transition_animation_scale 0
adb shell settings put global animator_duration_scale 0
adb shell input keyevent 82 || true
say "android sdk: $(adb shell getprop ro.build.version.sdk | tr -d '\r')"

# A fresh install every time: no leftover data, and a local build with a lower versionCode still installs.
adb uninstall "$PKG" >/dev/null 2>&1 || true
adb install "$APK" >/dev/null
# A clean Messaging app: its old conversations and notifications would otherwise be replayed into the count.
adb shell pm clear com.android.messaging >/dev/null 2>&1 || true
adb shell pm list packages | grep -q 'com.android.messaging' || { fail "stock Messaging app is not on this system image"; exit 1; }
adb shell cmd notification allow_listener "$PKG/com.sende.app.capture.SendeNotificationListenerService" >/dev/null
adb logcat -c
adb shell am start -n "$PKG/com.sende.app.MainActivity" >/dev/null
sleep 5

# 1. The channel picker lists the real installed apps; choose all of them.
tap_text "Set up channels" && sleep 4
tap_text "All" && sleep 4
shot 01-kanal-secici
adb shell input keyevent 4
sleep 3

# 2. Three SMS from one number, one from another.
sms 5551234 "Merhaba, yarin icin 3 kisilik masa ayirtabilir miyim?"
sms 5551234 "Saat 19:00 olsun, pencere kenari varsa harika olur"
sms 5551234 "Bir de cocuk sandalyesi gerekecek"
sms 5559876 "Siparisim ne zaman gelir? 2 gundur bekliyorum"
sleep 5
shot 02-dort-sms

all=$(badge_after "All")
if [ "$all" = "4" ]; then pass "4 unread in total for 4 SMS"; else fail "expected 4 unread in total, screen says '${all:-nothing}'"; fi
if [ "$(count_texts '555-1234')" -ge 1 ]; then pass "sender 555-1234 shown"; else fail "sender 555-1234 missing"; fi
if [ "$(count_texts '555-9876')" -ge 1 ]; then pass "sender 555-9876 shown"; else fail "sender 555-9876 missing"; fi

# 3. Open the first number's card completely: each text exactly once.
tap_text "555-1234" && sleep 3
tap_text "555-1234" && sleep 3
shot 03-kart-acik
for line in "Merhaba, yarin icin" "Saat 19:00 olsun" "Bir de cocuk sandalyesi"; do
  n=$(count_texts "$line")
  if [ "$n" = "1" ]; then pass "\"$line...\" appears once"; else fail "\"$line...\" appears $n times (expected 1)"; fi
done

# 4. Back: first closes the open card, then the app goes to the background without exiting.
adb shell input keyevent 4
sleep 3
shot 04-geri-1
adb shell input keyevent 4
sleep 3
if adb shell dumpsys activity activities | grep -m1 mResumedActivity | grep -q "$PKG"; then
  fail "second back press left Sende in front"
else
  pass "second back press sent Sende to the background"
fi
if [ -n "$(adb shell pidof "$PKG" | tr -d '\r')" ]; then pass "process still alive after back"; else fail "process died after back (not allowed)"; fi

adb shell dumpsys notification --noredact | grep -v ZenRule >"$OUT/bildirim-dokumu.txt" || true
adb logcat -d >"$OUT/logcat.txt" || true
if grep -E "FATAL EXCEPTION|ANR in $PKG" "$OUT/logcat.txt" | grep -q .; then fail "crash or ANR in logcat"; else pass "no crash or ANR in logcat"; fi

if [ "$FAILED" = "0" ]; then say "ALL CHECKS PASSED"; else say "SOME CHECKS FAILED"; fi
exit "$FAILED"
