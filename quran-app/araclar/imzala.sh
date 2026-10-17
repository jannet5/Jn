#!/usr/bin/env bash
# Hazır bir APK'yı kendi anahtarınla imzalar (eski sürümün üstüne kurulabilmesi için ilk sürümdeki anahtar gerekir).
# Kullanım: araclar/imzala.sh <giris.apk> <anahtar.jks> <anahtar-adi> <cikis.apk>
# Anahtar şifresi KS_PASS ortam değişkeninden okunur (yoksa sorulur; ekranda görünmez).
set -euo pipefail
GIRIS=${1:?giris.apk}; ANAHTAR=${2:?anahtar.jks}; AD=${3:?anahtar-adi}; CIKIS=${4:?cikis.apk}
SDK=${ANDROID_HOME:-/root/android-sdk}
APKSIGNER=$(ls -d "$SDK"/build-tools/*/apksigner | sort -V | tail -1)
if [ -z "${KS_PASS:-}" ]; then read -r -s -p "Anahtar şifresi: " KS_PASS; echo; fi
export KS_PASS
"$APKSIGNER" sign --ks "$ANAHTAR" --ks-key-alias "$AD" --ks-pass env:KS_PASS --key-pass env:KS_PASS --out "$CIKIS" "$GIRIS"
"$APKSIGNER" verify --print-certs "$CIKIS"
