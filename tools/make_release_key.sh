#!/bin/bash
# One-time: makes the PRIVATE key that signs the APK you hand to people, and the one-line text
# you paste into GitHub as a repository secret so CI can use it.
#
#   bash tools/make_release_key.sh
#
# Then in GitHub: repository > Settings > Secrets and variables > Actions > New repository secret
#   Name:   SENDE_SIGNING_B64
#   Secret: the single long line from release/SENDE_SIGNING_B64.txt
#
# Keep release/sende-dist.jks and release/sende-dist.properties somewhere safe (a password manager,
# a USB stick). Lose them and nobody can update an installed copy: Android only updates an app with
# a build signed by the same key. Never commit them (release/ is git-ignored).
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p release
if [ -e release/sende-dist.jks ]; then echo "release/sende-dist.jks already exists; refusing to overwrite it."; exit 1; fi

STORE_PW=$(openssl rand -hex 16)
keytool -genkeypair -v -keystore release/sende-dist.jks -storetype PKCS12 \
  -alias sende-dist -keyalg RSA -keysize 4096 -validity 10000 \
  -dname "CN=Sende, O=Sende, C=TR" -storepass "$STORE_PW" -keypass "$STORE_PW" >/dev/null 2>&1
KEY_PW=$STORE_PW  # PKCS12 uses one password for the store and the key

cat > release/sende-dist.properties <<PROPS
storePassword=$STORE_PW
keyAlias=sende-dist
keyPassword=$KEY_PW
PROPS

tar czf - -C release sende-dist.jks sende-dist.properties | base64 -w0 > release/SENDE_SIGNING_B64.txt
echo
echo "Done. Certificate fingerprint (put this in the README so people can check it):"
keytool -list -v -keystore release/sende-dist.jks -storepass "$STORE_PW" 2>/dev/null | grep -E "SHA256:" | head -1
echo
echo "Now paste the content of release/SENDE_SIGNING_B64.txt into the GitHub secret SENDE_SIGNING_B64."
