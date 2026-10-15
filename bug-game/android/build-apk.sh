#!/usr/bin/env bash
# Gradle'sız APK derleme (sadece Android SDK build-tools + JDK). Kullanım: ANDROID_HOME=... ./build-apk.sh
set -euo pipefail
cd "$(dirname "$0")"
SDK=${ANDROID_HOME:-$HOME/android-sdk}; BT=$SDK/build-tools/34.0.0; JAR=$SDK/platforms/android-34/android.jar
OUT=build-manual; rm -rf $OUT; mkdir -p $OUT/res $OUT/classes $OUT/dex $OUT/apk/assets
node ../game/build.js >/dev/null
cp ../game/bocek-ringi.html $OUT/apk/assets/index.html
$BT/aapt2 compile --dir app/src/main/res -o $OUT/res/res.zip
$BT/aapt2 link -o $OUT/base.apk -I $JAR --manifest app/src/main/AndroidManifest.xml $OUT/res/res.zip \
  --min-sdk-version 24 --target-sdk-version 34 --version-code 1 --version-name 1.0 --rename-manifest-package com.bocekringi.app -A $OUT/apk/assets
javac -source 8 -target 8 -bootclasspath $JAR -cp $JAR -d $OUT/classes app/src/main/java/com/bocekringi/app/*.java 2>&1 | grep -v "warning\|JAVA_TOOL" || true
$BT/d8 --min-api 24 --lib $JAR --output $OUT/dex $(find $OUT/classes -name '*.class')
cp $OUT/base.apk $OUT/unsigned.apk && (cd $OUT/dex && zip -q ../unsigned.apk classes.dex)
$BT/zipalign -f -p 4 $OUT/unsigned.apk $OUT/aligned.apk
KS=${KEYSTORE:-$OUT/../debug.keystore}
[ -f "$KS" ] || keytool -genkeypair -keystore "$KS" -storepass android -keypass android -alias bocek -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Bocek Ringi" 2>/dev/null
$BT/apksigner sign --ks "$KS" --ks-pass pass:android --key-pass pass:android --out ../dist/bocek-ringi.apk $OUT/aligned.apk
$BT/apksigner verify ../dist/bocek-ringi.apk && echo "APK hazır: bug-game/dist/bocek-ringi.apk"
