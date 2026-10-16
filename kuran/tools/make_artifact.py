#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Artifact (claude.ai) sürümünü üretir: sayfa iskeleti otomatik sarıldığı için <html>/<head>/<body> yok,
yazı tipi data: URI olarak CSS'e gömülür, service worker/manifest yok.
Kullanım: python3 make_artifact.py <www_klasoru> <cikti_klasoru>"""
import base64, re, sys, shutil
from pathlib import Path

WWW, OUT = Path(sys.argv[1]), Path(sys.argv[2])  # kaynak ve hedef klasör
OUT.mkdir(parents=True, exist_ok=True)  # hedefi oluştur
html = (WWW / "index.html").read_text(encoding="utf-8")  # kaynak sayfa
body = re.search(r"<body>(.*)</body>", html, re.S).group(1)  # sadece gövde içeriği
body = body.replace('<noscript><p style="padding:24px">Kur\'an Okuyucu için JavaScript gerekir.</p></noscript>\n', "")  # noscript gerekmez
head = ("<title>Kur'an Okuyucu</title>\n"
        '<meta name="theme-color" content="#fbf8f0">\n'
        '<link rel="stylesheet" href="style.css">\n')  # başlık + tema rengi + stil dosyası
(OUT / "index.html").write_text(head + body.strip() + "\n", encoding="utf-8")  # parça sayfayı yaz
css = (WWW / "style.css").read_text(encoding="utf-8")  # stil
font = base64.b64encode((WWW / "fonts/amiri-quran.woff2").read_bytes()).decode()  # yazı tipini base64'e çevir
css = css.replace("url(fonts/amiri-quran.woff2)", "url(data:font/woff2;base64,%s)" % font)  # data: URI olarak göm
(OUT / "style.css").write_text(css, encoding="utf-8")  # gömülü yazı tipli stil
for f in ("app.js", "data.js"):  # betikler aynen
    shutil.copy(WWW / f, OUT / f)
print("artifact klasörü hazır:", OUT)
