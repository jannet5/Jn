"""PNG → JPEG q92 (Graph API sadece JPEG), boyut kontrolü (<400KB), izgara.png (profil ızgarası önizlemesi).
Kullanım: python3 son-islem.py [--onizleme KLASOR]  (sosyal/sablonlar içinden)"""
import sys, glob, os
from PIL import Image, ImageDraw

KOK = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
pngler = sorted(glob.glob(f'{KOK}/**/*.png', recursive=True))
pngler = [p for p in pngler if os.path.basename(p) not in ('izgara.png',) and '/_onizleme/' not in p]

hata = 0
for p in pngler:
    im = Image.open(p)
    if os.path.getsize(p) > 400_000:  # düz renkli tasarımda nadir; olursa 256 renge indir
        im.convert('RGB').quantize(256, dither=Image.Dither.NONE).save(p, optimize=True)
    if os.path.getsize(p) > 400_000:
        print('BÜYÜK', p, os.path.getsize(p)); hata += 1
    im.convert('RGB').save(p[:-4] + '.jpg', quality=92, optimize=True)
    beklenen = {(1080, 1350), (1080, 1920), (1080, 1080)}
    if im.size not in beklenen:
        print('ÖLÇÜ', p, im.size); hata += 1

# Izgara: profil 3 sütun, en yeni gönderi sol üstte. Instagram ızgarası 3:4 kırpar (ortadan).
gonderiler = sorted(glob.glob(f'{KOK}/gonderiler/*/'), reverse=True)
kapaklar = []
for g in gonderiler:
    aday = [f'{g}gorsel.png', f'{g}01.png']
    kapaklar.append(next(a for a in aday if os.path.exists(a)))
W, H, B = 360, 480, 4
satir = (len(kapaklar) + 2) // 3
izg = Image.new('RGB', (3 * W + 2 * B, satir * H + (satir - 1) * B), 'white')
for i, k in enumerate(kapaklar):
    im = Image.open(k).convert('RGB')
    w, h = im.size; hh = w * 4 // 3
    im = im.crop((0, (h - hh) // 2, w, (h - hh) // 2 + hh)) if hh <= h else im
    if hh > h:  # 4:5 → 3:4 kenarlardan kırpılır
        ww = h * 3 // 4; im = im.crop(((w - ww) // 2, 0, (w - ww) // 2 + ww, h))
    im = im.resize((W, H), Image.LANCZOS)
    izg.paste(im, ((i % 3) * (W + B), (i // 3) * (H + B)))
izg.save(f'{KOK}/izgara.png', optimize=True)

# Gözle kontrol için temas sayfaları (repo dışı klasöre yazılabilir)
if '--onizleme' in sys.argv:
    hedef = sys.argv[sys.argv.index('--onizleme') + 1]; os.makedirs(hedef, exist_ok=True)
    gruplar = {}
    for p in pngler:
        gruplar.setdefault(os.path.dirname(p), []).append(p)
    for d, ps in gruplar.items():
        for j in range(0, len(ps), 6):
            parca = ps[j:j + 6]; ims = [Image.open(x).convert('RGB') for x in parca]
            sc = 520 / 1080
            ims = [x.resize((int(x.width * sc), int(x.height * sc)), Image.LANCZOS) for x in ims]
            cols = min(3, len(ims)); rows = (len(ims) + cols - 1) // cols
            cw = max(x.width for x in ims); ch = max(x.height for x in ims)
            sh = Image.new('RGB', (cols * cw + (cols - 1) * 8, rows * ch + (rows - 1) * 8), '#888')
            for i, x in enumerate(ims): sh.paste(x, ((i % cols) * (cw + 8), (i // cols) * (ch + 8)))
            sh.save(f'{hedef}/{os.path.relpath(d, KOK).replace("/", "_")}-{j // 6}.png')
print(f'{len(pngler)} PNG işlendi, {len(kapaklar)} gönderi ızgarada, {hata} hata.')
