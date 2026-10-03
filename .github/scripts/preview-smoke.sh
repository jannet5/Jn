#!/usr/bin/env bash
# Launch smoke test for the minified, sideloadable "preview" APK on a real emulator.
# Instrumented tests run against the unminified debug build, so this is the only thing that
# proves R8 didn't strip something Room/Koin/serialization need at runtime.
set -euo pipefail

APK=app/build/outputs/apk/preview/app-preview.apk
PKG=com.tekpanel.app.preview
LISTENER="$PKG/com.tekpanel.app.capture.TekPanelNotificationListenerService"

adb install -r "$APK"
adb logcat -c

adb shell cmd notification allow_listener "$LISTENER"
adb shell am start -W -n "$PKG/com.tekpanel.app.MainActivity"
sleep 5

# Share intake writes a real row through CaptureCoordinator -> Room in the minified build.
adb shell am start -W -a android.intent.action.SEND -t text/plain \
  --es android.intent.extra.TEXT "CI smoke paylasim" \
  -n "$PKG/com.tekpanel.app.capture.ShareIntakeActivity"
sleep 5

adb shell cmd notification post -S bigtext -t "CI" ci_tag "CI smoke bildirim"
sleep 3

if adb logcat -d | grep -q "FATAL EXCEPTION"; then
  echo "CRASH detected:"
  adb logcat -d | grep -A40 "FATAL EXCEPTION"
  exit 1
fi

adb shell pidof "$PKG" >/dev/null || { echo "app process is not running"; exit 1; }

adb shell dumpsys notification | grep -A10 "Live notification listeners" | tee /dev/stderr | grep -q "$PKG" \
  || { echo "notification listener is not bound"; exit 1; }

echo "PREVIEW SMOKE OK"
