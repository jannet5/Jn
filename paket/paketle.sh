#!/usr/bin/env bash
# Builds the customer zip: windows-agent/dist exe + android-app/dist release
# APK + the install scripts in this folder. Run ../windows-agent/build.sh and
# the Android release build first.
set -euo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")"
out=${1:-dist}
name=WinRemoteMonitor-Kurulum
stage="$out/$name"
rm -rf "$stage" "$out/$name.zip"
mkdir -p "$stage"
cp ../windows-agent/dist/winremotemonitor-agent.exe ../android-app/dist/app-release.apk "$stage/"
cp KUR.bat KALDIR.bat yeni-kod.bat kur.ps1 kaldir.ps1 allowed_apps.json YAPAY_ZEKA_ICIN_KURULUM.md "$stage/"
(cd "$stage" && sha256sum winremotemonitor-agent.exe app-release.apk > SHA256SUMS.txt)
(cd "$out" && zip -q -r -X "$name.zip" "$name")
echo "$out/$name.zip"
(cd "$out" && sha256sum "$name.zip")
