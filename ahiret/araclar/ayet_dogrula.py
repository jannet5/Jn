#!/usr/bin/env python3
"""bolumler/*.html içindeki her `blockquote.ayet` bloğunu api.quran.com ile karşılaştırır.
Kullanım: python3 araclar/ayet_dogrula.py [bolumler/03-olum-berzah.html ...]  (argümansız: hepsi)
Arapça: uthmani metin; Türkçe: Diyanet İşleri (id 77). Eşleşme oranı < 0.98 ise UYARI yazar."""
import glob, html, json, os, re, sys, unicodedata, urllib.request, difflib
ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
CACHE = os.path.join(ROOT, 'araclar', '.api_cache.json')
cache = json.load(open(CACHE, encoding='utf-8')) if os.path.exists(CACHE) else {}
def get(url):
    if url in cache: return cache[url]
    for _ in range(3):
        try:
            r = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
            cache[url] = json.load(urllib.request.urlopen(r, timeout=25)); return cache[url]
        except Exception as e: err = e
    raise err
def ar_norm(s):
    s = re.sub(r'[۝۞۩٠-٩\d]', '', s)            # ayet sonu işaretleri / rakamlar
    s = ''.join(c for c in unicodedata.normalize('NFD', s) if not unicodedata.category(c).startswith('M'))  # harekeler
    s = s.replace('ٱ', 'ا').replace('ٰ', '')                # elif vasl
    return re.sub(r'\s+', ' ', s).strip()
def tr_norm(s):
    s = html.unescape(re.sub(r'<[^>]+>', '', s)).lower()
    s = re.sub(r'[“”"\'’‘`´.,;:!?()\[\]…\-–—]', ' ', s)
    return re.sub(r'\s+', ' ', s).strip()
def verses(ch, a, b):
    ar = get(f'https://api.quran.com/api/v4/quran/verses/uthmani?chapter_number={ch}')['verses']
    tr = get(f'https://api.quran.com/api/v4/quran/translations/77?chapter_number={ch}')['translations']
    A = {int(v['verse_key'].split(':')[1]): v['text_uthmani'] for v in ar}
    T = {i + 1: re.sub(r'<sup.*?</sup>', '', t['text']) for i, t in enumerate(tr)}
    return ' '.join(A[i] for i in range(a, b + 1)), ' '.join(T[i] for i in range(a, b + 1))
files = sys.argv[1:] or sorted(glob.glob(os.path.join(ROOT, 'bolumler', '*.html')))
bad = 0; total = 0
for f in files:
    s = open(f, encoding='utf-8').read()
    for m in re.finditer(r'<blockquote class="ayet">(.*?)</blockquote>', s, flags=re.S):
        blk = m.group(1)
        cite = re.sub(r'<[^>]+>', '', (re.search(r'<cite>(.*?)</cite>', blk, flags=re.S) or [None, ''])[1])
        ar = re.search(r'class="ar"[^>]*>(.*?)</p>', blk, flags=re.S); tr = re.search(r'class="tr"[^>]*>(.*?)</p>', blk, flags=re.S)
        refs = re.findall(r'(\d{1,3}):(\d{1,3})(?:\s*[–\-]\s*(\d{1,3}))?', cite.split('·')[0]); ref = refs[0] if refs else None
        total += 1
        if not (ref and ar and tr): print('UYARI yapı/ref okunamadı:', os.path.basename(f), cite[:60]); bad += 1; continue
        try:
            parts = [verses(int(r[0]), int(r[1]), int(r[2] or r[1])) for r in refs]
            A = ' '.join(p[0] for p in parts); T = ' '.join(p[1] for p in parts)
            ch, a, b = int(refs[0][0]), int(refs[0][1]), int(refs[-1][2] or refs[-1][1])
            full = ' '.join(re.sub(r'<sup.*?</sup>', '', t['text']) for t in get(f'https://api.quran.com/api/v4/quran/translations/77?chapter_number={ch}')['translations'])
        except Exception as e: print('HATA API', refs, e); continue
        ra = difflib.SequenceMatcher(None, ar_norm(re.sub(r'<[^>]+>', '', ar.group(1))), ar_norm(A)).ratio()
        tw = tr_norm(tr.group(1)).split(); fw = tr_norm(full).split(); fg = {tuple(fw[i:i+4]) for i in range(len(fw)-3)}
        grams = [tuple(tw[i:i+4]) for i in range(max(0, len(tw)-3))]
        rt = (sum(g in fg for g in grams) / len(grams)) if grams else 1.0   # Diyanet kaydı bazı ayetleri birleştirdiği için bölüm düzeyinde 4-gram oranı
        if ra < 0.98 or rt < 0.98:
            bad += 1; print(f'UYARI {os.path.basename(f)} {ch}:{a}-{b} arapça={ra:.3f} türkçe={rt:.3f}  [{cite[:50]}]')
json.dump(cache, open(CACHE, 'w', encoding='utf-8'), ensure_ascii=False)
print(f'{total} ayet bloğu denetlendi, {bad} uyarı')
