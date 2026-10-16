#!/usr/bin/env python3
"""bolumler/*.html parçalarını template.html ile birleştirir.
Çıktılar: index.html (bağımsız belge), dist/artifact.html (Artifact için gövde parçası)."""
import glob, html, json, os, re, sys
ROOT = os.path.dirname(os.path.abspath(__file__))
os.chdir(ROOT)
tpl = open('template.html', encoding='utf-8').read()
hero = json.load(open('hero_ayet.json', encoding='utf-8'))

def plain(h):
    h = re.sub(r'<svg.*?</svg>', ' ', h, flags=re.S)
    h = re.sub(r'<[^>]+>', ' ', h)
    return html.unescape(h)

frags = []
for f in sorted(glob.glob('bolumler/*.html')):
    s = open(f, encoding='utf-8').read().strip()
    s = re.sub(r'<script.*?</script>', '', s, flags=re.S | re.I)
    s = re.sub(r'</?(?:!doctype|html|head|body)(?=[\s>])[^>]*>', '', s, flags=re.I)
    m = re.search(r'<section[^>]*\bid="(b-\d+)"', s)
    if not m:
        print('UYARI: id yok', f, file=sys.stderr); continue
    sid = m.group(1)
    no = re.search(r'class="no"[^>]*>\s*([^<]+)', s)
    h2 = re.search(r'<h2[^>]*>(.*?)</h2>', s, flags=re.S)
    oz = re.search(r'class="ozet"[^>]*>(.*?)</p>', s, flags=re.S)
    words = len(plain(s).split())
    frags.append(dict(file=f, id=sid, no=(no.group(1).strip() if no else sid[2:]),
        title=re.sub(r'<[^>]+>', '', h2.group(1)).strip() if h2 else sid,
        ozet=re.sub(r'<[^>]+>', '', oz.group(1)).strip() if oz else '',
        words=words, dk=max(1, round(words / 190)), body=s,
        ayet=len(re.findall(r'class="ayet"', s)), hadis=len(re.findall(r'class="hadis"', s)),
        alim=len(re.findall(r'class="alim"', s)), svg=len(re.findall(r'<svg', s))))

# ---- görseller ----
credits = []
if os.path.exists('gorsel/credits.json'):
    try: credits = json.load(open('gorsel/credits.json', encoding='utf-8'))
    except Exception as e: print('credits.json okunamadı', e, file=sys.stderr)
credits = [c for c in credits if os.path.exists(os.path.join('gorsel', c.get('file', '')))]
used = set()
def pick(themes, hero_pref=False):
    cand = []
    for t in themes:
        cand += [c for c in credits if c.get('tema') == t and c['file'] not in used]
    if hero_pref:
        h = [c for c in credits if c['file'].startswith('kapak') and c['file'] not in used]
        cand = h + cand
    if not cand:
        cand = [c for c in credits if c['file'] not in used]
    if cand:
        used.add(cand[0]['file']); return cand[0]
    return None
THEMES = {'00': ['kozmos'], '01': ['yol', 'col'], '02': ['col', 'gok'], '03': ['gok', 'isik'],
          '04': ['kozmos', 'yun'], '05': ['col', 'dag'], '06': ['terazi', 'isik'], '07': ['kopru', 'su'],
          '08': ['bahce', 'su'], '09': ['isik', 'gok'], '10': ['su', 'isik'], '11': ['gok', 'bahce']}
kapak = pick(['kozmos', 'gok'], hero_pref=True)
kapak_stil = f"--kapak-img:url('gorsel/{kapak['file']}')" if kapak else ''

def ayrac(c):
    alt = html.escape(c.get('alt_tr') or c.get('title') or '', quote=True)
    kr = html.escape(f"Fotoğraf: {c.get('author','?')} · {c.get('license','')}")
    return f'<div class="ayrac" role="img" aria-label="{alt}" style="--img:url(\'gorsel/{c["file"]}\')"><span>{kr}</span></div>'

toc, parts = [], []
for fr in frags:
    toc.append(f'      <li><a href="#{fr["id"]}"><span class="nokta"></span><span class="t"><b>{html.escape(fr["title"])}</b><small>{fr["dk"]} dk</small></span></a></li>')
    num = fr['id'][2:]
    if num != '00':
        c = pick(THEMES.get(num, ['gok']))
        if c: parts.append(ayrac(c))
    parts.append(fr['body'])

def ozet_maddeleri(body):
    m = re.search(r'<div class="ozetle">(.*?)</div>', body, flags=re.S)
    return re.findall(r'<li>(.*?)</li>', m.group(1), flags=re.S) if m else []
kartlar = []
for fr in frags:
    mad = ozet_maddeleri(fr['body'])
    if not mad: continue
    kartlar.append(f'<article class="kisa-kart"><h3><a href="#{fr["id"]}"><span>{fr["id"][2:]}</span> {html.escape(fr["title"])}</a></h3><ul>' + ''.join(f'<li>{x}</li>' for x in mad[:5]) + '</ul></article>')
kisa = ('<section class="kisa" id="kisa"><div class="kisa-ic"><p class="etiket-k">Vaktin azsa</p><h2>Yolculuk bir bakışta</h2>'
        '<p class="kisa-not">Her durağın en önemli birkaç cümlesi. Ayrıntı ve kaynaklar için başlığa dokun.</p><div class="kisa-grid">' + ''.join(kartlar) + '</div></div></section>')
toc.insert(0, '      <li><a href="#kisa"><span class="nokta"></span><span class="t"><b>Yolculuk bir bakışta</b><small>5 dk</small></span></a></li>')
toplam_dk = sum(f['dk'] for f in frags)
kredi = ''.join(f'<li>{html.escape(c.get("title",c["file"]))}: {html.escape(c.get("author","?"))}, {html.escape(c.get("license",""))} (<a href="{html.escape(c.get("source_url","#"),quote=True)}" rel="noopener">kaynak</a>)</li>' for c in credits if c['file'] in used)
alt = f'''      <h2>Yöntem ve kaynaklar</h2>
      <p>Ayetler Diyanet İşleri Başkanlığı meâlinden aynen alınmıştır. Hadislerin kaynağı ve derecesi her alıntının altında yazılıdır; zayıf rivayetler ayrıca işaretlenmiştir. Bu sayfa bir fetva kitabı değildir. Dinî konularda size en yakın güvenilir âlime ve ilim ehline danışın.</p>
      <p>Bölümlerin arkasındaki araştırma notları (açılan kaynaklar, doğrulama işaretleri) deponun <code>ahiret/research</code> klasöründedir.</p>
      <details class="detay"><summary>Fotoğraf kredileri</summary><ul>{kredi or '<li>Görsel havuzu hazırlanıyor.</li>'}</ul></details>'''

def fill(t):
    return (t.replace('{{KAPAK_STIL}}', kapak_stil).replace('{{N_BOLUM}}', str(len(frags)))
             .replace('{{TOPLAM_DK}}', str(toplam_dk)).replace('{{HERO_AR}}', hero['ar'])
             .replace('{{HERO_TR}}', html.escape(hero['tr'])).replace('{{HERO_REF}}', hero['key'])
             .replace('{{TOC}}', '\n'.join(toc)).replace('{{KISA}}', kisa).replace('{{BOLUMLER}}', '\n'.join(parts))
             .replace('{{ALT_BILGI}}', alt))
out = fill(tpl)
os.makedirs('dist', exist_ok=True)
open('dist/artifact.html', 'w', encoding='utf-8').write(out)
i = out.index('</style>') + len('</style>')
doc = ('<!doctype html>\n<html lang="tr">\n<head>\n<meta charset="utf-8">\n'
       '<meta name="viewport" content="width=device-width,initial-scale=1,viewport-fit=cover">\n'
       + out[:i] + '\n</head>\n<body>\n' + out[i:] + '\n</body>\n</html>\n')
open('index.html', 'w', encoding='utf-8').write(doc)
print(f'{len(frags)} bölüm, {sum(f["words"] for f in frags)} kelime, ~{toplam_dk} dk, ayet {sum(f["ayet"] for f in frags)}, hadis {sum(f["hadis"] for f in frags)}, alıntı {sum(f["alim"] for f in frags)}, şema {sum(f["svg"] for f in frags)}, görsel {len(used)}')
for f in frags: print(' ', f['id'], f['title'], f['words'], 'kelime', f['svg'], 'şema')
