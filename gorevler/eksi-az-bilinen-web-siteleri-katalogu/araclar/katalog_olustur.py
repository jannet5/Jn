#!/usr/bin/env python3
"""Ekşi link listesinden kategorili site kataloğu üretir (yalnız standart kütüphane).

Adımlar:
  tekillestir  eksi-linkler.txt -> siteler.json   (HTTPS'e çevir, izleme parametrelerini at, tekilleştir)
  kontrol      siteler.json -> siteler.json        (her bağlantıyı HTTPS ile dene, durumunu yaz)
  sablon       siteler.json -> aciklamalar.json    (doldurulacak açıklama şablonu)
  uret         siteler.json + aciklamalar.json -> katalog.txt (kategorili liste, doğrulamalı)

Örnek:
  python katalog_olustur.py tekillestir eksi-linkler.txt -o siteler.json
  python katalog_olustur.py kontrol siteler.json
  python katalog_olustur.py sablon siteler.json -o aciklamalar.json
  python katalog_olustur.py uret siteler.json aciklamalar.json -o katalog.txt
"""
import argparse
import concurrent.futures
import json
import re
import ssl
import sys
import urllib.error
import urllib.parse
import urllib.request

IZLEME_PARAMETRELERI = re.compile(r'^(utm_.*|fbclid|gclid|yclid|mc_cid|mc_eid|ref|ref_src|igshid|si)$', re.I)

# Bu sunucularda aynı alan adında farklı yollar farklı içerik demektir; yol korunur.
# Diğer sitelerde ana sayfa ile alt sayfa aynı site sayılır (ilk görülen bağlantı tutulur).
PLATFORMLAR = {
    'github.com', 'gitlab.com', 'chromewebstore.google.com', 'chrome.google.com',
    'addons.mozilla.org', 'play.google.com', 'apps.apple.com', 'youtube.com',
    'reddit.com', 'x.com', 'twitter.com', 'instagram.com', 't.me', 'medium.com',
    'sites.google.com', 'docs.google.com', 'drive.google.com', 'huggingface.co',
    'notion.site', 'itch.io', 'archive.org', 'web.archive.org', 'tr.wikipedia.org',
    'en.wikipedia.org', 'linktr.ee', 'producthunt.com', 'microsoftedge.microsoft.com',
}
# Kendi başına "site" olmayan, entry içinde geçen dolaylı bağlantılar.
ATLANACAK = {'eksisozluk.com', 'ekstat.com', 'img.ekstat.com', 'imgur.com', 'i.imgur.com',
             'hizliresim.com', 'i.hizliresim.com', 'prnt.sc', 'bit.ly', 'goo.gl', 'tinyurl.com'}


def normalize(url):
    """Bağlantıyı HTTPS tam biçime getirir; site değilse None döner."""
    url = url.strip().strip('.,;:!?)("\'')
    if not url:
        return None
    if not re.match(r'^[a-z][a-z0-9+.-]*://', url, re.I):
        url = 'https://' + url
    p = urllib.parse.urlsplit(url)
    if p.scheme.lower() not in ('http', 'https') or not p.hostname or '.' not in p.hostname:
        return None
    host = p.hostname.lower().rstrip('.')
    try:
        host = host.encode('idna').decode('ascii')
    except UnicodeError:
        pass
    port = f':{p.port}' if p.port and p.port not in (80, 443) else ''
    sorgu = urllib.parse.urlencode(
        [(k, v) for k, v in urllib.parse.parse_qsl(p.query, keep_blank_values=True)
         if not IZLEME_PARAMETRELERI.match(k)])
    yol = p.path or '/'
    return urllib.parse.urlunsplit(('https', host + port, yol, sorgu, ''))


def kok_alan(host):
    return host[4:] if host.startswith('www.') else host


def tekil_anahtar(url):
    p = urllib.parse.urlsplit(url)
    host = kok_alan(p.hostname)
    if host in PLATFORMLAR:
        yol = p.path.rstrip('/').lower()
        return host + yol + ('?' + p.query if p.query and host in ('youtube.com',) else '')
    return host


def linkleri_oku(dosyalar):
    satirlar, meta = [], {}
    for d in dosyalar:
        with open(d, encoding='utf-8-sig') as f:
            for satir in f:
                satir = satir.rstrip('\n')
                if not satir.strip():
                    continue
                if satir.startswith('#'):
                    k, _, v = satir[1:].partition(':')
                    meta[k.strip()] = v.strip()
                    continue
                parca = satir.split('\t')
                satirlar.append({'url': parca[0],
                                 'sayfa': int(parca[1]) if len(parca) > 1 and parca[1].isdigit() else None,
                                 'entry': parca[2] if len(parca) > 2 else None})
    return satirlar, meta


def tekillestir(satirlar):
    siteler, sira = {}, []
    atlanan = []
    for s in satirlar:
        url = normalize(s['url'])
        if not url:
            atlanan.append({'ham': s['url'], 'neden': 'geçersiz bağlantı'})
            continue
        host = kok_alan(urllib.parse.urlsplit(url).hostname)
        if host in ATLANACAK or any(host.endswith('.' + a) for a in ATLANACAK):
            atlanan.append({'ham': s['url'], 'neden': 'site değil (görsel/kısaltıcı/ekşi içi)'})
            continue
        k = tekil_anahtar(url)
        if k not in siteler:
            siteler[k] = {'anahtar': k, 'url': url, 'gecis': 0, 'sayfalar': [], 'entryler': []}
            sira.append(k)
        kay = siteler[k]
        kay['gecis'] += 1
        if s['sayfa'] is not None and s['sayfa'] not in kay['sayfalar']:
            kay['sayfalar'].append(s['sayfa'])
        if s['entry'] and s['entry'] not in kay['entryler']:
            kay['entryler'].append(s['entry'])
    return [siteler[k] for k in sira], atlanan


def _baglam():
    return ssl.create_default_context()


def tek_kontrol(url, zaman_asimi=20):
    istek = urllib.request.Request(url, headers={
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 '
                      '(KHTML, like Gecko) Chrome/141.0 Safari/537.36',
        'Accept-Language': 'tr-TR,tr;q=0.9,en;q=0.8'})
    try:
        with urllib.request.urlopen(istek, timeout=zaman_asimi, context=_baglam()) as y:
            govde = y.read(200_000).decode('utf-8', 'replace')
            baslik = re.search(r'<title[^>]*>(.*?)</title>', govde, re.S | re.I)
            aciklama = re.search(
                r'<meta[^>]+name=["\']description["\'][^>]+content=["\']([^"\']*)', govde, re.I)
            return {'durum': 'calisiyor', 'kod': y.status, 'son_url': y.geturl(),
                    'baslik': re.sub(r'\s+', ' ', baslik.group(1)).strip()[:200] if baslik else '',
                    'meta': aciklama.group(1).strip()[:300] if aciklama else ''}
    except urllib.error.HTTPError as e:
        # 401/403/429 çoğunlukla bot koruması: site yaşıyor ama içerik okunamadı.
        durum = 'korumali' if e.code in (401, 403, 405, 429, 503) else 'olu'
        return {'durum': durum, 'kod': e.code, 'son_url': url, 'baslik': '', 'meta': ''}
    except Exception as e:  # DNS, TLS, zaman aşımı
        return {'durum': 'olu', 'kod': None, 'son_url': url, 'hata': type(e).__name__ + ': ' + str(e)[:150],
                'baslik': '', 'meta': ''}


def kontrol(siteler, es_zamanli=16):
    with concurrent.futures.ThreadPoolExecutor(es_zamanli) as h:
        for site, sonuc in zip(siteler, h.map(lambda s: tek_kontrol(s['url']), siteler)):
            site['kontrol'] = sonuc
    return siteler


CUMLE_SONU = re.compile(r'[.!?…]$')


def aciklama_dogrula(a):
    """Açıklama kurallarına uymayan alanları liste olarak döner (boşsa geçer)."""
    hatalar = []
    for alan in ('kategori', 'ne', 'yapabilirsin'):
        if not str(a.get(alan, '')).strip():
            hatalar.append(f'{alan} boş')
    ne = str(a.get('ne', '')).strip()
    yap = str(a.get('yapabilirsin', '')).strip()
    if ne:
        if not CUMLE_SONU.search(ne):
            hatalar.append('"ne" tam cümle değil (nokta yok)')
        if len(re.findall(r'[.!?](\s|$)', ne)) > 1:
            hatalar.append('"ne" birden fazla cümle')
        if not 40 <= len(ne) <= 220:
            hatalar.append(f'"ne" uzunluğu {len(ne)} (40-220 olmalı)')
    if yap:
        if not yap.startswith('Bu siteyle'):
            hatalar.append('"yapabilirsin" "Bu siteyle" ile başlamıyor')
        if not CUMLE_SONU.search(yap):
            hatalar.append('"yapabilirsin" tam cümle değil')
        if len(re.findall(r'[.!?](\s|$)', yap)) > 1:
            hatalar.append('"yapabilirsin" birden fazla cümle')
    return hatalar


AYRAC = '   ⟶   '


def tr_buyuk(metin):
    return metin.replace('i', 'İ').replace('ı', 'I').upper()


def uret(siteler, aciklamalar, meta=None):
    """Kategorili düz metin katalog ve doğrulama raporu döner."""
    gruplar, eksik, hatali, disarida = {}, [], [], []
    for s in siteler:
        k = s.get('kontrol', {})
        if k.get('durum') == 'olu':
            disarida.append(s)
            continue
        a = aciklamalar.get(s['url']) or aciklamalar.get(s['anahtar'])
        if not a:
            eksik.append(s['url'])
            continue
        h = aciklama_dogrula(a)
        if h:
            hatali.append((s['url'], h))
            continue
        gruplar.setdefault(a['kategori'].strip(), []).append((s, a))

    satirlar = ['EKŞİ SÖZLÜK — "az kişinin bildiği muhteşem web siteleri" KATALOĞU', '']
    if meta:
        for k in ('kaynak', 'toplam_sayfa', 'aralik', 'toplama_tarihi'):
            if k in meta:
                satirlar.append(f'{k.replace("_", " ")}: {meta[k]}')
    toplam = sum(len(v) for v in gruplar.values())
    satirlar += [f'site sayısı: {toplam} (tekilleştirilmiş, çalışan)', '']
    for kat in sorted(gruplar, key=lambda x: (x.lower() == 'diğer', x.lower())):
        ogeler = sorted(gruplar[kat], key=lambda sa: sa[0]['url'])
        satirlar += ['', f'■ {tr_buyuk(kat)}  ({len(ogeler)} site)', '']
        for s, a in ogeler:
            satirlar.append(f'•  {s["url"]}{AYRAC}{a["ne"].strip()}  {a["yapabilirsin"].strip()}')
            satirlar.append('')
    rapor = {'katalogda': toplam, 'kategori': len(gruplar), 'aciklamasi_eksik': eksik,
             'kurala_uymayan': [{'url': u, 'hatalar': h} for u, h in hatali],
             'calismayan': [{'url': s['url'], 'kontrol': s.get('kontrol')} for s in disarida]}
    return '\n'.join(satirlar).rstrip() + '\n', rapor


def main(argv=None):
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    alt = ap.add_subparsers(dest='komut', required=True)
    a1 = alt.add_parser('tekillestir'); a1.add_argument('linkler', nargs='+'); a1.add_argument('-o', default='siteler.json')
    a2 = alt.add_parser('kontrol'); a2.add_argument('siteler')
    a3 = alt.add_parser('sablon'); a3.add_argument('siteler'); a3.add_argument('-o', default='aciklamalar.json')
    a4 = alt.add_parser('uret'); a4.add_argument('siteler'); a4.add_argument('aciklamalar')
    a4.add_argument('-o', default='katalog.txt'); a4.add_argument('--rapor', default='rapor.json')
    ar = ap.parse_args(argv)

    if ar.komut == 'tekillestir':
        satirlar, meta = linkleri_oku(ar.linkler)
        siteler, atlanan = tekillestir(satirlar)
        json.dump({'meta': meta, 'ham_satir': len(satirlar), 'siteler': siteler, 'atlanan': atlanan},
                  open(ar.o, 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
        print(f'{len(satirlar)} ham satır → {len(siteler)} tekil site, {len(atlanan)} atlandı → {ar.o}')
    elif ar.komut == 'kontrol':
        veri = json.load(open(ar.siteler, encoding='utf-8'))
        kontrol(veri['siteler'])
        json.dump(veri, open(ar.siteler, 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
        say = {}
        for s in veri['siteler']:
            say[s['kontrol']['durum']] = say.get(s['kontrol']['durum'], 0) + 1
        print('kontrol:', say)
    elif ar.komut == 'sablon':
        veri = json.load(open(ar.siteler, encoding='utf-8'))
        sablon = {s['url']: {'kategori': '', 'ne': '', 'yapabilirsin': 'Bu siteyle ',
                             '_baslik': s.get('kontrol', {}).get('baslik', ''),
                             '_meta': s.get('kontrol', {}).get('meta', '')}
                  for s in veri['siteler'] if s.get('kontrol', {}).get('durum') != 'olu'}
        json.dump(sablon, open(ar.o, 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
        print(f'{len(sablon)} site için şablon → {ar.o}')
    elif ar.komut == 'uret':
        veri = json.load(open(ar.siteler, encoding='utf-8'))
        aciklamalar = json.load(open(ar.aciklamalar, encoding='utf-8'))
        metin, rapor = uret(veri['siteler'], aciklamalar, veri.get('meta'))
        open(ar.o, 'w', encoding='utf-8').write(metin)
        json.dump(rapor, open(ar.rapor, 'w', encoding='utf-8'), ensure_ascii=False, indent=1)
        print(f"katalog: {rapor['katalogda']} site, {rapor['kategori']} kategori → {ar.o}; "
              f"eksik açıklama {len(rapor['aciklamasi_eksik'])}, kurala uymayan "
              f"{len(rapor['kurala_uymayan'])}, çalışmayan {len(rapor['calismayan'])}")
        return 1 if rapor['aciklamasi_eksik'] or rapor['kurala_uymayan'] else 0
    return 0


if __name__ == '__main__':
    sys.exit(main())
