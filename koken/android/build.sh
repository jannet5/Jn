#!/bin/sh
# Tek komutla imzalı APK üretir: koken/dist/Koken.apk (+ sha256). Web dosyaları ../web klasöründen alınır.
set -e
cd "$(dirname "$0")"                                  # betiğin klasörüne geç
export ANDROID_HOME="${ANDROID_HOME:-/opt/android-sdk}" # Android SDK konumu
export ANDROID_SDK_ROOT="$ANDROID_HOME"
GRADLE="${GRADLE:-gradle}"                            # gradle komutu
$GRADLE --no-daemon -q assembleRelease -PwebDir="${WEB_DIR:-$(pwd)/../web}"  # derle ve imzala
mkdir -p ../dist
cp app/build/outputs/apk/release/app-release.apk ../dist/Koken.apk  # çıktıyı dist'e kopyala
( cd ../dist && sha256sum Koken.apk > Koken.apk.sha256 && cat Koken.apk.sha256 )
