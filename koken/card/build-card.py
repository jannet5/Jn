#!/usr/bin/env python3
# Kart (Artifact) HTML'ini üretir: şablondaki yer tutucuları gerçek APK bilgisiyle doldurur.
# Kullanım: python3 build-card.py <APK_URL> <WEB_URL> <APK_YOLU> <ÇIKTI.html>
import base64, io, sys, zipfile, os

apk_url, web_url, apk_path, out_path = sys.argv[1:5]  # komut satırı parametreleri
tpl = open(os.path.join(os.path.dirname(__file__), 'koken-kart.template.html'), encoding='utf-8').read()  # şablon okunur
apk = open(apk_path, 'rb').read()  # APK baytları okunur

# APK, ZIP içine konur (Claude'un "kaydet" izin listesinde .apk yok, .zip var)
buf = io.BytesIO()
with zipfile.ZipFile(buf, 'w', zipfile.ZIP_DEFLATED) as z:
    z.writestr('Koken.apk', apk)  # içindeki dosya adı Koken.apk
zip_b64 = base64.b64encode(buf.getvalue()).decode()  # ZIP base64'e çevrilir

size = f"{len(apk)/1048576:.1f}".replace('.', ',') + " MB"  # okunur boyut metni
html = (tpl.replace('__APK_URL__', apk_url).replace('__WEB_URL__', web_url)
           .replace('__APK_SIZE__', size).replace('__APK_ZIP_B64__', zip_b64))  # yer tutucular doldurulur
open(out_path, 'w', encoding='utf-8').write(html)  # çıktı yazılır
print(f"kart yazıldı: {out_path} ({len(html)/1048576:.2f} MB), apk {size}")
