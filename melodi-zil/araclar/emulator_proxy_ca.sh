#!/usr/bin/env bash
# Emülatörün, bu bulut ortamındaki TLS-araya-giren proxy'ye güvenmesi için: proxy CA'larını sistem deposuna (tmpfs ile) ekler
# ve cihazın HTTP proxy ayarını adb reverse üzerinden host proxy'ye yönlendirir. Yalnızca test ortamı için; uygulamanın kendisiyle ilgisi yok.
set -e
A=${ADB:-/home/user/Jn/android-sdk/platform-tools/adb} # adb yolu
CERTS=${1:?sertifika klasörü} # <hash>.0 dosyaları
PORT=${2:?proxy portu} # host proxy portu
$A root >/dev/null; sleep 3; $A wait-for-device # root kabuğu
$A shell "mkdir -p /data/local/tmp/cacerts && cp /system/etc/security/cacerts/* /data/local/tmp/cacerts/" # mevcut sistem CA'ları yedekle
for f in "$CERTS"/*.0; do $A push "$f" /data/local/tmp/cacerts/ >/dev/null; done # proxy CA'larını ekle
$A shell "mount -t tmpfs tmpfs /system/etc/security/cacerts && cp /data/local/tmp/cacerts/* /system/etc/security/cacerts/ && chmod 644 /system/etc/security/cacerts/* && chcon u:object_r:system_file:s0 /system/etc/security/cacerts/*" # tmpfs ile üzerine yaz
$A reverse tcp:$PORT tcp:$PORT # cihazın 127.0.0.1:PORT'u → host proxy
$A shell settings put global http_proxy 127.0.0.1:$PORT # sistem proxy ayarı (OkHttp varsayılan ProxySelector bunu okur)
echo "CA sayısı: $($A shell ls /system/etc/security/cacerts | wc -l); proxy: $($A shell settings get global http_proxy)"
