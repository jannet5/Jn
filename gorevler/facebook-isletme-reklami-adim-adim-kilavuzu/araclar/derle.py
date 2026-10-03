#!/usr/bin/env python3
"""KILAVUZ.md → KILAVUZ.html (tek dosya, çevrimdışı açılır) ve KILAVUZ.pdf (Chromium ile).

Gereksinim: `pip install markdown`; PDF için Node + Playwright (Chromium).
Kullanım:  python3 araclar/derle.py
"""
import os
import subprocess
from pathlib import Path

import markdown

KOK = Path(__file__).resolve().parent.parent
md = (KOK / "KILAVUZ.md").read_text(encoding="utf-8")
# Python-Markdown iç içe liste için 4 boşluk ister; GitHub 3 boşluğu da kabul eder → normalize et
import re as _re
md = _re.sub(r"^   (?=[-*>|]|\d+\. |\S)", "    ", md, flags=_re.M)
md = _re.sub(r"^      (?=[-*] )", "        ", md, flags=_re.M)
# Python-Markdown listeden önce boş satır ister (GitHub istemez) → gerekiyorsa ekle
_liste = _re.compile(r"^(\s*)([-*] |\d+\. |\|)")
_satirlar, _onceki = [], ""
for _s in md.split("\n"):
    if _liste.match(_s) and _onceki.strip() and not _liste.match(_onceki) \
            and not _onceki.startswith(("    ", "\t")) and not _onceki.lstrip().startswith(">"):
        _satirlar.append("")
    _satirlar.append(_s)
    _onceki = _s
md = "\n".join(_satirlar)
govde = markdown.markdown(md, extensions=["tables", "fenced_code", "toc", "sane_lists"])
# Düz yazılmış adresleri tıklanabilir yap (kod blokları ve mevcut bağlantılar hariç)
import re
govde = re.sub(r'(?<!href=")(?<!">)(https://[^\s<>"`*)]+[^\s<>"`*).,])', r'<a href="\1">\1</a>', govde)
# Görev kutularını görsel onay kutusuna çevir
govde = govde.replace("<li>[ ] ", '<li class="kutu">☐ ')

CSS = """
:root { --zemin:#ffffff; --yazi:#1c1e21; --soluk:#5b6470; --vurgu:#0866ff; --kutu:#f0f4fa; --cizgi:#d8dee6; --uyari:#fff6e0; }
@media (prefers-color-scheme: dark) { :root:not([data-theme="light"]) {
  --zemin:#17191c; --yazi:#e8eaed; --soluk:#a4adb8; --vurgu:#5b9dff; --kutu:#22262b; --cizgi:#3a4047; --uyari:#3a3220; } }
:root[data-theme="dark"] { --zemin:#17191c; --yazi:#e8eaed; --soluk:#a4adb8; --vurgu:#5b9dff; --kutu:#22262b; --cizgi:#3a4047; --uyari:#3a3220; }
html { -webkit-text-size-adjust:100%; }
body { background:var(--zemin); color:var(--yazi); font:16px/1.6 system-ui,-apple-system,"Segoe UI",Roboto,sans-serif;
  max-width:880px; margin:0 auto; padding:24px 16px 64px; }
h1 { font-size:1.7rem; line-height:1.25; margin-top:0; }
h2 { font-size:1.3rem; border-bottom:2px solid var(--cizgi); padding-bottom:4px; margin-top:2.2em; }
h3 { font-size:1.08rem; margin-top:1.6em; }
a, strong, td { overflow-wrap:anywhere; }
a { color:var(--vurgu); }
code { background:var(--kutu); padding:1px 5px; border-radius:4px; font-size:.92em; }
pre { background:var(--kutu); padding:12px; border-radius:8px; overflow-x:auto; font-size:.85rem; line-height:1.4; }
pre code { background:none; padding:0; }
blockquote { margin:1em 0; padding:10px 14px; background:var(--uyari); border-left:4px solid #e0a100; border-radius:6px; }
blockquote p { margin:.3em 0; }
table { border-collapse:collapse; width:100%; display:block; overflow-x:auto; font-size:.92rem; margin:1em 0; }
th, td { border:1px solid var(--cizgi); padding:6px 8px; vertical-align:top; text-align:left; }
th { background:var(--kutu); }
li { margin:.25em 0; }
li.kutu { list-style:none; margin-left:-1.2em; }
hr { border:0; border-top:1px solid var(--cizgi); margin:2em 0; }
@media print { body { max-width:none; padding:0; font-size:11pt; } a { color:inherit; }
  h2 { break-after:avoid; } table, pre, blockquote { break-inside:avoid; } }
"""

sayfa = f"""<!doctype html>
<html lang="tr"><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Facebook İşletme Reklamı Kılavuzu</title>
<style>{CSS}</style></head>
<body>{govde}</body></html>
"""
(KOK / "KILAVUZ.html").write_text(sayfa, encoding="utf-8")
print("KILAVUZ.html yazıldı:", len(sayfa), "bayt")

JS = r"""
const { chromium } = require(process.env.NP + '/playwright');
(async () => {
  const b = await chromium.launch();
  const p = await b.newPage();
  await p.goto('file://' + process.env.HTML);
  await p.pdf({ path: process.env.PDF, format: 'A4', printBackground: true,
                margin: { top: '14mm', bottom: '14mm', left: '12mm', right: '12mm' } });
  await p.setViewportSize({ width: 390, height: 844 });
  await p.screenshot({ path: process.env.PNG_MOBIL });
  await p.setViewportSize({ width: 1280, height: 900 });
  await p.evaluate(() => document.querySelector('h2:nth-of-type(5)')?.scrollIntoView());
  await p.screenshot({ path: process.env.PNG_MASAUSTU });
  const genis = await p.evaluate(() => document.documentElement.scrollWidth);
  console.log('masaustu scrollWidth', genis);
  await p.setViewportSize({ width: 390, height: 844 });
  console.log('mobil scrollWidth', await p.evaluate(() => document.documentElement.scrollWidth));
  await b.close();
})();
"""
npm_kok = subprocess.run(["npm", "root", "-g"], capture_output=True, text=True).stdout.strip()
cikti = Path(os.environ.get("CIKTI_DIZINI", KOK))
ortam = {**os.environ, "NP": npm_kok, "HTML": str(KOK / "KILAVUZ.html"), "PDF": str(KOK / "KILAVUZ.pdf"),
         "PNG_MOBIL": str(cikti / "onizleme-mobil.png"), "PNG_MASAUSTU": str(cikti / "onizleme-masaustu.png")}
r = subprocess.run(["node", "-e", JS], env=ortam, capture_output=True, text=True)
print(r.stdout, r.stderr[-800:])
