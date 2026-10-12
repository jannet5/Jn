#!/usr/bin/env python3
"""Statik site build'ini (tek index.html) müşteriye telefondan gösterilecek Artifact sayfasına çevirir.

Kullanım: python3 artifact.py dist/index.html cikti.html "Sayfa Adı"
Yaptıkları:
 1. <!doctype>/<html>/<head>/<body> sarmalını söker (yayın ortamı kendi iskeletini ekler).
 2. <title>'ı kısa bir isimle değiştirir (açıklama yok).
 3. Koyu mod token'larını viewer'ın tema seçicisine bağlar:
    @media(prefers-color-scheme:dark){:root{..}} -> :root:not([data-theme=light]) + :root[data-theme=dark] kopyası.
 4. Sabit üst menüye telefon çentiği boşluğu ekler (env(safe-area-inset-top)).
 5. "Durağan halde tamam" kuralı: kaydırmayla açılan (opacity:0) öğeleri baştan görünür yapar.
"""
import re, sys

src, out, name = sys.argv[1], sys.argv[2], sys.argv[3]
h = open(src, encoding="utf-8").read()

head = re.search(r"<head>(.*?)</head>", h, re.S).group(1)
body = re.search(r"<body[^>]*>(.*)</body>", h, re.S).group(1)

# head'den sadece işe yarayanlar: style, google fonts link'leri, inline script'ler
keep = []
for m in re.finditer(r"<style[^>]*>.*?</style>|<link[^>]*fonts\.g[^>]*>|<script[^>]*>.*?</script>", head, re.S):
    keep.append(m.group(0))
head_keep = "\n".join(keep)

# 3. koyu mod seçicileri
def dark_fix(css):
    def rep(m):
        decl = m.group(1)
        return ("@media (prefers-color-scheme:dark){:root:not([data-theme=light]){%s;color-scheme:dark}}"
                ":root[data-theme=dark]{%s;color-scheme:dark}" % (decl, decl))
    return re.sub(r"@media \(prefers-color-scheme:\s*dark\)\s*\{\s*:root\s*\{([^}]*)\}\s*\}", rep, css)
head_keep = dark_fix(head_keep)
body = dark_fix(body)

extra = """<style>
/* Artifact uyarlaması */
header.fixed{padding-top:env(safe-area-inset-top,0px)}
.pain .pain-body,.ledger-dot{opacity:1!important;transform:none!important}
.ledger-line{stroke-dashoffset:0!important}
body{background:var(--background);color:var(--foreground)}
</style>"""

page = f"<title>{name}</title>\n{head_keep}\n{extra}\n{body}"
open(out, "w", encoding="utf-8").write(page)
print(f"OK {out} ({len(page)//1024} KB); koyu blok sayısı: {page.count('data-theme=dark')}")
