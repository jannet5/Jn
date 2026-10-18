#!/usr/bin/env bash
# Builds the single delivery zip: Windows installer, Android APK, USB phone
# installer, AI guide and the Google Play kit (AAB, listing, images, answers).
#
#   ./paketle.sh <upload.properties>
#
# The upload key (keystore + properties) is copied into google-play/GIZLI/;
# it is never committed. Run ../windows-agent/build.sh and Inno Setup
# (installer/setup.iss) first so windows-agent/dist has the Setup.exe.
set -euo pipefail
cd "$(dirname "${BASH_SOURCE[0]}")"
props=${1:?usage: paketle.sh /path/to/upload.properties}
out=dist
name=WinRemoteMonitor-Paket
stage="$out/$name"
rm -rf "$stage" "$out/$name.zip"
mkdir -p "$stage/google-play/GIZLI"

(cd ../android-app && ./gradlew -q bundleRelease assembleRelease -PsigningProps="$props")

cp ../windows-agent/dist/WinRemoteMonitor-Kurulum.exe "$stage/"
cp ../android-app/app/build/outputs/apk/release/app-release.apk "$stage/"
cp telefona-kur.bat YAPAY_ZEKA_ICIN_KURULUM.md "$stage/"

cp ../android-app/app/build/outputs/bundle/release/app-release.aab "$stage/google-play/"
cp -r ../play-store/. "$stage/google-play/"
rm -f "$stage/google-play/icon.svg"
keystore=$(sed -n 's/^storeFile=//p' "$props")
case "$keystore" in /*) ;; *) keystore="$(dirname "$props")/$keystore" ;; esac
cp "$keystore" "$stage/google-play/GIZLI/upload-keystore.jks"
sed 's#^storeFile=.*#storeFile=upload-keystore.jks#' "$props" > "$stage/google-play/GIZLI/upload.properties"
cat > "$stage/google-play/GIZLI/OKU-BENI.txt" <<'TXT'
BU KLASOR GIZLIDIR. Google Play yukleme anahtariniz ve parolasi burada.
- Guvenli bir yere yedekleyin (or. sifreli USB + bulut). Kimseyle paylasmayin.
- Guncelleme yayinlarken: ./gradlew bundleRelease -PsigningProps=<bu klasor>/upload.properties
- Kaybolursa Play Console > Uygulama butunlugu > "Yukleme anahtarini sifirla".
TXT

(cd "$stage" && sha256sum WinRemoteMonitor-Kurulum.exe app-release.apk google-play/app-release.aab > SHA256SUMS.txt)
(cd "$out" && zip -q -r -X "$name.zip" "$name")
echo "$out/$name.zip"
(cd "$out" && sha256sum "$name.zip")
