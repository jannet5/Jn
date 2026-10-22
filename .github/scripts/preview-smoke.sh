#!/usr/bin/env bash
# Launch smoke test for the minified, sideloadable "preview" APK on a real emulator.
# Instrumented tests run against the unminified debug build, so this is the only thing that
# proves R8 didn't strip something Room/Koin/serialization need at runtime.
set -euo pipefail

APK=app/build/outputs/apk/sideload/preview/app-sideload-preview.apk
PKG=com.sende.app.preview
LISTENER="$PKG/com.sende.app.capture.SendeNotificationListenerService"

adb install -r "$APK"
adb logcat -c

adb shell cmd notification allow_listener "$LISTENER"
adb shell am start -W -n "$PKG/com.sende.app.MainActivity"
sleep 5

# Share intake writes a real row through CaptureCoordinator -> Room in the minified build.
# adb shell re-tokenizes on the device, so the multi-word extra needs its own inner quotes.
adb shell am start -W -a android.intent.action.SEND -t text/plain \
  --es android.intent.extra.TEXT "'CI smoke paylasim'" \
  -n "$PKG/com.sende.app.capture.ShareIntakeActivity"
sleep 5

adb shell cmd notification post -S bigtext -t "CI" ci_tag "CI smoke bildirim"
sleep 3

# Dump the log to a file first: grep -q closing a pipe early would kill adb and, under pipefail,
# make a healthy run look like a failure (or hide a crash).
adb logcat -d > logcat.txt
if grep -q "FATAL EXCEPTION" logcat.txt; then
  echo "CRASH detected:"
  grep -A40 "FATAL EXCEPTION" logcat.txt
  exit 1
fi

adb shell pidof "$PKG" >/dev/null || { echo "app process is not running"; exit 1; }

adb shell dumpsys notification > dumpsys.txt
LISTENERS=$(grep -A10 "Live notification listeners" dumpsys.txt || true)
echo "$LISTENERS"
case "$LISTENERS" in *"$PKG"*) ;; *) echo "notification listener is not bound"; exit 1 ;; esac

echo "PREVIEW SMOKE OK"
