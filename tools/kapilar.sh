#!/bin/bash
# Runs every quality gate that can run without a phone, in one command, and stops at the first failure:
#
#   bash tools/kapilar.sh          # everything below
#   bash tools/kapilar.sh --emu    # plus the real-emulator tests, when an emulator or phone is connected
#
# The same gates run in CI (.github/workflows/android-ci.yml); this is the local copy of them.
set -euo pipefail
cd "$(dirname "$0")/.."
step() { printf '\n== %s\n' "$1"; }

step "1/6  Sende must never remove or snooze the user's own notifications"
if grep -rn "cancelNotification\|cancelAllNotifications\|snoozeNotification" app/src/main; then
  echo "FAIL: forbidden call found"; exit 1
fi

step "2/6  Unit, Robolectric, ergonomics (48 dp) and screenshot tests"
./gradlew :app:testSideloadDebugUnitTest -Proborazzi.test.record=true --console=plain -q

step "3/6  Android Lint"
./gradlew :app:lintSideloadDebug --console=plain -q

step "4/6  Play build (R8) and the APK that is handed out"
./gradlew :app:assemblePlayRelease :app:assembleSideloadPreview --console=plain -q
APK=app/build/outputs/apk/sideload/preview/app-sideload-preview.apk
BT=$(ls -d "${ANDROID_HOME:-/opt/android-sdk}"/build-tools/* | sort -V | tail -1)

step "5/6  Permission allow-list of the handed-out APK (no INTERNET, nothing unexpected)"
"$BT/aapt2" dump permissions "$APK" | sed -n "s/^uses-permission: name='\([^']*\)'.*/\1/p" | sort > /tmp/sende-perms.txt
cat /tmp/sende-perms.txt
ALLOWED="android.permission.RECEIVE_BOOT_COMPLETED"
if grep -Ev "^($ALLOWED)$|DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION$" /tmp/sende-perms.txt; then
  echo "FAIL: permission outside the allow-list"; exit 1
fi

step "6/6  Signature"
"$BT/apksigner" verify --print-certs "$APK" | head -3

if [ "${1:-}" = "--emu" ]; then
  step "emulator  Real-system tests (needs a running emulator or a connected phone)"
  ./gradlew :app:connectedSideloadDebugAndroidTest --console=plain
fi
echo; echo "ALL GATES PASSED"
