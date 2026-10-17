#!/usr/bin/env python3
"""Yerel Kur'an verisinden (api.quran.com: uthmani + Diyanet İşleri meali, id 77) ayet bloğu üretir.
Kullanım:
  python3 araclar/ayet.py "Nahl 16:83"            -> HTML <blockquote class="ayet"> bloğu
  python3 araclar/ayet.py "Yûnus 10:36-37"        -> aralık (ayetler birleştirilir)
  python3 araclar/ayet.py --metin 5:49 10:36      -> sadece Arapça + Türkçe düz metin (araştırma için)
Sure adı (ilk kelime[ler]) cite'ta aynen yazılır; numara sure:ayet olmalı."""
import json, os, re, sys, html
D = json.load(open(os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'veri', 'kuran_tam.json'), encoding='utf-8'))
def span(ch, a, b):
    ks = [f'{ch}:{i}' for i in range(a, b + 1)]
    ar = ' '.join(D['ar'][k].replace('۞', '').strip() for k in ks)
    tr = ' '.join(D['tr'][k].strip() for k in ks if D['tr'][k].strip())
    return ar, tr
def parse(ref):
    m = re.search(r'(\d{1,3}):(\d{1,3})(?:\s*[-–]\s*(\d{1,3}))?', ref)
    return int(m.group(1)), int(m.group(2)), int(m.group(3) or m.group(2))
args = sys.argv[1:]
if args and args[0] == '--metin':
    for r in args[1:]:
        ch, a, b = parse(r); ar, tr = span(ch, a, b); print(f'[{r}]\n{ar}\n{tr}\n')
else:
    for r in args:
        ch, a, b = parse(r); ar, tr = span(ch, a, b)
        print(f'''<blockquote class="ayet">
  <p class="ar" lang="ar" dir="rtl">{ar}</p>
  <p class="tr">{html.escape(tr, quote=False)}</p>
  <cite>{html.escape(r)} · Diyanet İşleri meali</cite>
</blockquote>''')
