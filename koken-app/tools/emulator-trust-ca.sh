#!/bin/bash
# Sadece HATA AYIKLAMA (debug) derlemesi için: kurumsal/ara proxy CA sertifikasını güvenilir yapar.
# Emülatörde proxy arkasından test ederken kullanılır. Üretilen app/src/debug/ klasörü .gitignore'dadır
# ve release APK'ya HİÇBİR ŞEKİLDE girmez.
# Kullanım: tools/emulator-trust-ca.sh /yol/proxy-ca.crt
set -e
CA="${1:?kullanım: $0 <ca.crt>}"
D="$(cd "$(dirname "$0")/.." && pwd)/app/src/debug"
mkdir -p "$D/res/raw" "$D/res/xml"
cp "$CA" "$D/res/raw/proxy_ca.crt"
cat > "$D/res/xml/network_security_config.xml" <<XML
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <debug-overrides>
        <trust-anchors>
            <certificates src="system" />
            <certificates src="@raw/proxy_ca" />
        </trust-anchors>
    </debug-overrides>
</network-security-config>
XML
cat > "$D/AndroidManifest.xml" <<XML
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application android:networkSecurityConfig="@xml/network_security_config" />
</manifest>
XML
echo "debug güven ayarı hazır: $D"
