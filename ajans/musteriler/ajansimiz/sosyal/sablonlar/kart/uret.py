import os, re, json, glob, shutil
from PIL import Image

SOS = '/home/user/jn/ajans/musteriler/ajansimiz/sosyal'
OUT = os.path.join(os.path.dirname(os.path.abspath(__file__)), 'cikti')  # yayınlanan kart: claude.ai/artifact/GH6rTbv7pTWV2HqhiHwo62
shutil.rmtree(OUT, ignore_errors=True); os.makedirs(OUT)

def kopya(rel):
    hedef = os.path.join(OUT, 'img', rel); os.makedirs(os.path.dirname(hedef), exist_ok=True)
    shutil.copy(os.path.join(SOS, rel), hedef); return 'img/' + rel

# --- caption'lar ---
md = open(f'{SOS}/CAPTIONLAR.md').read()
gunler = []
for blok in re.split(r'\n(?=## Gün )', md)[1:]:
    bas = blok.split('\n', 1)[0]
    m = re.match(r'## Gün (\d+) · (.+?) · `gonderiler/([^/]+)/`(.*)', bas)
    gun, baslik, klasor, ek = m.groups()
    govde = blok.split('\n', 1)[1].split('\n---')[0]
    def bolum(etiket, son):
        mm = re.search(r'\*\*' + etiket + r' \(([^)]*)\):\*\*(.*?)(?=' + son + ')', govde, re.S)
        if not mm: return None
        metin = '\n'.join(l for l in mm.group(2).strip().split('\n') if not l.startswith('>'))
        return {'tip': mm.group(1), 'metin': metin.strip()}
    A = bolum('A', r'\*\*B '); B = bolum('B', r'\*\*Hashtag')
    hs = re.search(r'\*\*Hashtag:\*\* (.+)', govde).group(1).strip()
    fb = re.search(r'\*\*Facebook:\*\* (.+)', govde)
    not_ = re.search(r'^Plan bu güne.*$', govde, re.M)
    gorseller = sorted(glob.glob(f'{SOS}/gonderiler/{klasor}/*.jpg'))
    gorseller = [kopya(os.path.relpath(g, SOS)) for g in gorseller]
    tur = 'Reel' if 'Reel' in baslik else ('Carousel' if len(gorseller) > 1 else 'Tekil')
    gunler.append({'gun': int(gun), 'baslik': re.sub(r'^(Reel|Carousel|Önce/Sonra #\d): ?', lambda x: x.group(0) if 'Önce' in x.group(0) else '', baslik).replace(' (tekil)', '').replace(' (kapak + senaryo TAKVIM.md)', ''),
                   'tur': tur, 'ornek': 'Örnek konsept' in ek, 'gorseller': gorseller, 'A': A, 'B': B, 'hashtag': hs,
                   'fb': fb.group(1) if fb else None, 'not': not_.group(0) if not_ else None})

# --- reel senaryoları ---
tak = open(f'{SOS}/TAKVIM.md').read()
senaryolar = {}
for blok in re.split(r'\n(?=### Gün )', tak)[1:]:
    gun = int(re.match(r'### Gün (\d+)', blok).group(1))
    baslik = blok.split('\n', 1)[0].replace('### ', '')
    satirlar = [l for l in blok.split('\n') if l.startswith('|') and not l.startswith('|---') and not l.startswith('| Sn')]
    rows = [[c.strip().replace('**', '') for c in l.strip('|').split('|')] for l in satirlar]
    notlar = [l for l in blok.split('\n')[1:] if l and not l.startswith('|')]
    senaryolar[gun] = {'baslik': baslik, 'satirlar': rows, 'notlar': notlar}
for g in gunler:
    if g['gun'] in senaryolar: g['senaryo'] = senaryolar[g['gun']]

# --- reklam ---
rp = open(f'{SOS}/REKLAM-PLANI.md').read()
skor = {}; metin = {}
for l in rp.split('\n'):
    c = [x.strip() for x in l.strip('|').split('|')]
    if l.startswith('| r') and len(c) == 10:
        skor[c[0]] = {'fikir': c[1].strip('"'), 'puan': [int(x) for x in c[2:8]], 'toplam': int(c[8].strip('*')), 'karar': c[9]}
    elif l.startswith('| r') and len(c) == 3:
        metin[c[0]] = {'birincil': c[1], 'baslik': c[2]}
reklamlar = []
for id_, s in skor.items():
    k = id_.split('-')[0]
    reklamlar.append({'id': id_, **s, **metin[k], 'feed': kopya(f'reklam/{id_}-feed.jpg'), 'story': kopya(f'reklam/{id_}-story.jpg')})

# --- profil ---
pr = open(f'{SOS}/profil/PROFIL.md').read()
bio = re.search(r'```\n(.*?)\n```', pr, re.S).group(1)
profil = {'bio': bio, 'ad': 'Vitrin Atölyesi | Site · Sosyal · Reklam', 'gorsel': kopya('profil/profil-gorseli.jpg'),
          'kapaklar': [(n, kopya(f'profil/one-cikan-{f}.jpg')) for n, f in [('Örnekler', 'ornekler'), ('Fiyat', 'fiyat'), ('Süreç', 'surec'), ('SSS', 'sss')]]}
# ızgara
Image.open(f'{SOS}/izgara.png').convert('RGB').save(f'{OUT}/img/izgara.jpg', quality=88)

veri = {'gunler': gunler, 'reklamlar': reklamlar, 'profil': profil}
sablon = open(os.path.join(os.path.dirname(os.path.abspath(__file__)), 'sablon.html')).read()
open(f'{OUT}/index.html', 'w').write(sablon.replace('/*VERI*/', json.dumps(veri, ensure_ascii=False)))
dosyalar = sorted(os.path.relpath(p, OUT) for p in glob.glob(f'{OUT}/img/**/*.jpg', recursive=True))
json.dump({d: d for d in dosyalar}, open(f'{OUT}/files.json', 'w'), ensure_ascii=False)
print(len(gunler), 'gün', len(reklamlar), 'reklam', len(dosyalar), 'dosya', sum(os.path.getsize(f'{OUT}/{d}') for d in dosyalar) // 1024, 'KB')
for g in gunler: print(g['gun'], g['tur'], g['baslik'], len(g['gorseller']), bool(g['A']), bool(g['B']), 'senaryo' in g)
