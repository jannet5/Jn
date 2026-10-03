#!/usr/bin/env python3
"""Ekşi sayfa JSON'larından kategorili site kataloğu üretir (yalnız standart kütüphane).

Girdi (her sayfa için bir nesne; tek nesne, dizi, JSON Lines ya da bu
dosyaları içeren klasör olabilir):
  {"page": 809, "page_url": "https://eksisozluk.com/...?p=809",
   "observed_at": "2026-10-03T12:00:00Z",
   "links": [{"label": "...", "url": "https://..."}]}

Adımlar:
  tekillestir  girdi(ler) -> siteler.json   (tam adres tekilleştirme, sayfa izi korunur)
  kontrol      siteler.json -> siteler.json  (güvenli ağ denetimi, HTTPS doğrulama)
  sablon       siteler.json -> aciklamalar.json
  uret         siteler.json + aciklamalar.json -> katalog.txt (+ rapor.json)
"""
import argparse
import concurrent.futures
import json
import os
import re
import sys
import urllib.parse

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import url_denetim  # noqa: E402

IZLEME_PARAMETRELERI = re.compile(
    r'^(utm_.*|fbclid|gclid|dclid|yclid|msclkid|mc_cid|mc_eid|igshid|ref_src)$', re.I)
# Kendi başına "site" olmayan bağlantılar (görsel barındırma, kısaltıcı, Ekşi içi).
ATLANACAK = {'eksisozluk.com', 'ekstat.com', 'imgur.com', 'hizliresim.com', 'prnt.sc'}
# Kısaltıcılar: hedefleri güvenli denetimle tek adım çözülür; çözülemezse atlanır.
KISALTICILAR = {'bit.ly', 'goo.gl', 'tinyurl.com', 't.co', 'ow.ly', 'is.gd', 'buff.ly', 'rb.gy', 'cutt.ly', 'shorturl.at'}
GORSEL_UZANTILAR = re.compile(r'\.(jpe?g|png|gif|webp|bmp|svg|heic|avif)$', re.I)


def dogrudan_gorsel_mi(url):
    """Bağlantı bir sayfa değil doğrudan bir resim dosyasıysa True."""
    p = urllib.parse.urlsplit(url)
    sorgu = dict(urllib.parse.parse_qsl(p.query))
    return bool(GORSEL_UZANTILAR.search(p.path)) or sorgu.get('format', '').lower() in ('jpg', 'jpeg', 'png', 'webp', 'gif')


ONARIM_KALIBI = re.compile(r'^https?://(https?)//(.+)$', re.I)
CALISAN = 'calisiyor'
# Ağ ve proxy modunun çalıştığını, siteleri 'ölü' işaretlemeden önce sınamak için.
ON_KONTROL_URL = 'https://example.com/'


def _host(url):
    return (urllib.parse.urlsplit(url).hostname or '').lower().rstrip('.')


def kok_alan(host):
    return host[4:] if host.startswith('www.') else host


def normalize(url):
    """Ham bağlantıyı temizler; ŞEMAYI DEĞİŞTİRMEZ.

    Dönüş: (temiz_url, sema) ya da (None, neden). Şemasız yazılmış alan adı
    'belirsiz' döner; HTTPS'i ancak ağ denetimi doğrularsa kullanılır.
    """
    ham = (url or '').strip()
    if not ham:
        return None, 'boş'
    # Yazım hatası onarımı: "http://https//alan/..." -> "https://alan/..." (kesin kalıp, tahmin yok)
    m = ONARIM_KALIBI.match(ham)
    if m:
        ham = m.group(1).lower() + '://' + m.group(2)
    if ham.startswith('//'):
        ham = 'https:' + ham
        sema = 'belirsiz'
    elif re.match(r'^[a-z][a-z0-9+.-]*:', ham, re.I):
        sema = ham.split(':', 1)[0].lower()
        if sema not in ('http', 'https'):
            return None, f'web adresi değil ({sema})'
    elif ham.startswith('/') or ham.startswith('?') or ham.startswith('#'):
        return None, 'göreli (site içi) bağlantı'
    else:
        ham = 'http://' + ham
        sema = 'belirsiz'
    p = urllib.parse.urlsplit(ham)
    host = (p.hostname or '').lower().rstrip('.')
    if not host or '.' not in host:
        return None, 'alan adı yok'
    try:
        host = host.encode('idna').decode('ascii')
    except UnicodeError:
        pass
    port = f':{p.port}' if p.port and p.port not in (80, 443) else ''
    sorgu = urllib.parse.urlencode(
        [(k, v) for k, v in urllib.parse.parse_qsl(p.query, keep_blank_values=True)
         if not IZLEME_PARAMETRELERI.match(k)], doseq=True)
    # "#/" ve "#!" tek sayfalık uygulamalarda farklı sayfa demektir; korunur.
    parca = p.fragment if p.fragment.startswith(('/', '!')) else ''
    gercek_sema = 'http' if sema == 'belirsiz' else sema
    return urllib.parse.urlunsplit((gercek_sema, host + port, p.path or '/', sorgu, parca)), sema


def tekil_anahtar(url):
    """Tam adres anahtarı: şema ve www hariç; yol, sorgu ve SPA parçası dahil.

    Aynı alan adındaki farklı araç/yollar ayrı kalır; yalnız birebir aynı
    sayfanın http/https, www'li/www'siz ve sondaki '/' farkı birleşir.
    """
    p = urllib.parse.urlsplit(url)
    host = kok_alan(p.hostname or '') + (f':{p.port}' if p.port and p.port not in (80, 443) else '')
    yol = p.path.rstrip('/') or ''
    return host + yol + ('?' + p.query if p.query else '') + ('#' + p.fragment if p.fragment else '')


def _sayfa_nesneleri(veri, kaynak_dosya):
    if isinstance(veri, list):
        for n in veri:
            yield from _sayfa_nesneleri(n, kaynak_dosya)
    elif isinstance(veri, dict) and 'links' in veri:
        yield veri
    elif isinstance(veri, dict) and isinstance(veri.get('pages'), list):
        yield from _sayfa_nesneleri(veri['pages'], kaynak_dosya)
    else:
        raise ValueError(f'{kaynak_dosya}: "links" alanı olan sayfa nesnesi bekleniyordu')


def girdileri_oku(yollar):
    """JSON / JSON Lines / klasör / eski TSV girdisini düz bağlantı kayıtlarına çevirir."""
    dosyalar = []
    for y in yollar:
        if os.path.isdir(y):
            dosyalar += sorted(os.path.join(y, f) for f in os.listdir(y)
                               if f.endswith(('.json', '.jsonl')))
        else:
            dosyalar.append(y)
    kayitlar, sayfalar, uyarilar = [], {}, []
    for d in dosyalar:
        with open(d, encoding='utf-8-sig') as f:
            metin = f.read()
        dolu = metin.strip()
        nesneler = []
        if dolu.startswith(('{', '[')):
            try:
                nesneler = list(_sayfa_nesneleri(json.loads(dolu), d))
            except json.JSONDecodeError:
                for no, satir in enumerate(dolu.splitlines(), 1):  # JSON Lines
                    if satir.strip():
                        nesneler += list(_sayfa_nesneleri(json.loads(satir), f'{d}:{no}'))
            for n in nesneler:
                sayfa = n.get('page')
                if sayfa in sayfalar:
                    uyarilar.append(f'sayfa {sayfa} birden fazla kez geldi ({d}); bağlantılar birleştirildi')
                sayfalar.setdefault(sayfa, {'page': sayfa, 'page_url': n.get('page_url'),
                                            'observed_at': n.get('observed_at'), 'link_sayisi': 0})
                for link in n.get('links') or []:
                    if isinstance(link, str):
                        link = {'url': link, 'label': ''}
                    sayfalar[sayfa]['link_sayisi'] += 1
                    kayitlar.append({'url': link.get('url', ''), 'label': (link.get('label') or '').strip(),
                                     'page': sayfa, 'page_url': n.get('page_url'),
                                     'observed_at': n.get('observed_at'), 'dosya': os.path.basename(d)})
        else:  # eski biçim: url<TAB>sayfa<TAB>entry
            for satir in metin.splitlines():
                if not satir.strip() or satir.startswith('#'):
                    continue
                parca = satir.split('\t')
                sayfa = int(parca[1]) if len(parca) > 1 and parca[1].isdigit() else None
                kayitlar.append({'url': parca[0], 'label': '', 'page': sayfa, 'page_url': None,
                                 'observed_at': None, 'dosya': os.path.basename(d)})
    return kayitlar, sayfalar, uyarilar


def kapsam_raporu(sayfalar, ust=None, alt=None):
    """İstenen aralıktaki eksik sayfaları bulur (ör. 809..610)."""
    numaralar = sorted((p for p in sayfalar if isinstance(p, int)), reverse=True)
    if not numaralar:
        return {'gelen_sayfa': 0, 'eksik': [], 'aralik': None}
    ust = ust or numaralar[0]
    alt = alt or numaralar[-1]
    eksik = [p for p in range(ust, alt - 1, -1) if p not in sayfalar]
    return {'gelen_sayfa': len(numaralar), 'aralik': f'{ust}-{alt}', 'beklenen': ust - alt + 1,
            'eksik': eksik, 'aralik_disi': [p for p in numaralar if not alt <= p <= ust],
            'ilk_100': f'{ust}-{max(alt, ust - 99)}', 'sonraki_100': f'{ust - 100}-{alt}' if ust - alt >= 100 else None}


def kisalticilari_coz(kayitlar, cozucu=None):
    """Kısaltıcı bağlantıların hedefini güvenli denetimle tek adım okur.

    Hedef ancak kurallardan geçen bir kamu adresiyse kayda 'url' olarak yazılır;
    özgün kısa bağlantı 'kisaltici_url' alanında kalır. Hedef okunamazsa kayıt
    olduğu gibi kalır ve tekilleştirmede atlanır.
    """
    if cozucu is None:
        def cozucu(url):
            try:
                kod, basliklar, govde, _, _ = url_denetim.tek_istek(url, proxy_modu=KISALTICI_PROXY_MODU)
            except Exception:
                return None
            if kod in (301, 302, 303, 307, 308) and basliklar.get('location'):
                return urllib.parse.urljoin(url, basliklar['location'].strip())
            if kod == 200:  # bazı kısaltıcılar (t.co) hedefi meta-refresh ile verir
                m = re.search(rb'http-equiv=["\']?refresh["\']?[^>]*?url=([^"\'>\s]+)', govde, re.I)
                if m:
                    return urllib.parse.urljoin(url, m.group(1).decode('utf-8', 'replace').replace('&amp;', '&'))
            return None
    sonuc = []
    for k in kayitlar:
        url, _ = normalize(k['url'])
        host = kok_alan(_host(url)) if url else ''
        if host in KISALTICILAR:
            hedef = cozucu(url)
            if hedef:
                try:
                    url_denetim.url_kural_denetimi(hedef)
                    if kok_alan(_host(hedef)) not in KISALTICILAR:
                        k = dict(k, url=hedef, kisaltici_url=k['url'])
                except url_denetim.Reddedildi:
                    pass
        sonuc.append(k)
    return sonuc


KISALTICI_PROXY_MODU = 'ip'


def tekillestir(kayitlar):
    siteler, sira, atlanan = {}, [], []
    # Son sayfadan geriye işlenir; aynı sayfada bağlantıların sırası korunur.
    kayitlar = sorted(kayitlar, key=lambda k: -k['page'] if isinstance(k['page'], int) else 0)
    for k in kayitlar:
        url, sema = normalize(k['url'])
        if not url:
            atlanan.append({'ham': k['url'], 'neden': sema, 'page': k['page']})
            continue
        host = kok_alan(_host(url))
        if host in KISALTICILAR:
            atlanan.append({'ham': k['url'], 'neden': 'kısaltıcı hedefi çözülemedi', 'page': k['page']})
            continue
        if dogrudan_gorsel_mi(url):
            atlanan.append({'ham': k['url'], 'neden': 'doğrudan görsel dosyası, site değil', 'page': k['page']})
            continue
        if any(host == a or host.endswith('.' + a) for a in ATLANACAK):
            atlanan.append({'ham': k['url'], 'neden': 'site değil (görsel/kısaltıcı/Ekşi içi)', 'page': k['page']})
            continue
        anahtar = tekil_anahtar(url)
        if anahtar not in siteler:
            siteler[anahtar] = {'anahtar': anahtar, 'url': url, 'semalar': [], 'kaynaklar': [],
                                'kontrol': {'durum': 'kontrol_edilmedi'}}
            sira.append(anahtar)
        s = siteler[anahtar]
        if sema not in s['semalar']:
            s['semalar'].append(sema)
        if sema == 'https':  # aynı sayfanın açıkça HTTPS verilmiş hali varsa onu temel al
            s['url'] = url
        iz = {'page': k['page'], 'page_url': k['page_url'], 'observed_at': k['observed_at'],
              'label': k['label'][:200], 'ham_url': k.get('kisaltici_url') or k['url']}
        if k.get('kisaltici_url'):
            iz['kisaltici_hedefi'] = k['url']
        if ONARIM_KALIBI.match(iz['ham_url']):
            iz['onarildi'] = True
        s['kaynaklar'].append(iz)
    liste = [siteler[a] for a in sira]
    # Aynı alan adında farklı yollar: birleştirilmez, insan incelemesi için raporlanır.
    alanlar = {}
    for s in liste:
        alanlar.setdefault(kok_alan(_host(s['url'])), []).append(s['url'])
    ayni_alan = {h: u for h, u in alanlar.items() if len(u) > 1}
    return liste, atlanan, ayni_alan


def site_denetle(site, denetleyici=url_denetim.denetle):
    """HTTPS verilmişse onu, verilmemişse önce HTTPS'i dener; HTTP'yi sessizce yükseltmez.

    'https_url' yalnız gerçekten 2xx dönen bir HTTPS adresi varsa doldurulur.
    """
    url = site['url']
    p = urllib.parse.urlsplit(url)
    https_aday = urllib.parse.urlunsplit(('https',) + tuple(p)[1:])

    def https_sonucu(sonuc, istenen):
        if sonuc['durum'] != CALISAN:
            return None
        if istenen.startswith('https://') and sonuc['son_url'].startswith('https://'):
            return istenen  # istenen HTTPS adres (gerekirse yönlenerek) çalıştı
        if sonuc['son_url'].startswith('https://'):
            return sonuc['son_url']  # HTTP adres HTTPS'e yönlendi; gerçek varış adresi
        return None

    hs = denetleyici(https_aday)
    hs['denenen'] = [https_aday]
    hs['https_url'] = https_sonucu(hs, https_aday)
    if p.scheme == 'https' or hs['https_url'] or hs['durum'] == 'reddedildi':
        return hs
    ht = denetleyici(url)
    ht['denenen'] = [https_aday, url]
    ht['https_url'] = https_sonucu(ht, url)
    ht['https_denemesi'] = {'url': https_aday, 'durum': hs['durum'], 'kod': hs.get('kod'), 'neden': hs.get('neden')}
    return ht


def kontrol(siteler, es_zamanli=12, denetleyici=url_denetim.denetle, ara_kayit=None, ilerleme=None):
    """Siteleri paralel denetler. ara_kayit(siteler) her 100 sonuçta çağrılır;
    durumu zaten 'kontrol_edilmedi' dışında olanlar (önceki çalıştırma) atlanır."""
    bekleyen = [s for s in siteler if s.get('kontrol', {}).get('durum', 'kontrol_edilmedi') == 'kontrol_edilmedi'
                and not s.get('kontrol', {}).get('neden', '').startswith('iç hata')]
    with concurrent.futures.ThreadPoolExecutor(es_zamanli) as h:
        isler = {h.submit(site_denetle, s, denetleyici): s for s in bekleyen}
        for i, is_ in enumerate(concurrent.futures.as_completed(isler), 1):
            site = isler[is_]
            try:
                site['kontrol'] = is_.result()
            except Exception as e:
                site['kontrol'] = {'durum': 'kontrol_edilmedi', 'neden': f'iç hata {type(e).__name__}: {e}'[:200]}
            if ilerleme and i % 100 == 0:
                ilerleme(f'{i}/{len(bekleyen)}')
            if ara_kayit and i % 100 == 0:
                ara_kayit(siteler)
    return siteler


def gosterilecek_url(site):
    """Katalogda yazılacak adres: doğrulanmış HTTPS, yoksa işaretlenecek özgün adres."""
    k = site.get('kontrol', {})
    if k.get('https_url'):
        return k['https_url'], True
    return site['url'], False


CUMLE_SONU = re.compile(r'[.!?…]$')


DAYANAKLAR = ('site_metasi', 'genel_bilgi', 'yok')


def aciklama_dogrula(a):
    if a.get('dayanak') == 'yok':  # bilinçli olarak açıklama yazılmadı; uydurma yok
        return [] if not str(a.get('ne', '')).strip() else ['dayanak "yok" iken açıklama yazılmış']
    hatalar = []
    if a.get('dayanak') not in (None,) + DAYANAKLAR:
        hatalar.append(f'geçersiz dayanak: {a.get("dayanak")}')
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
        if len(yap) <= len('Bu siteyle') + 5:
            hatalar.append('"yapabilirsin" boş şablon')
    return hatalar


AYRAC = '   ⟶   '
DURUM_ADI = {'korumali': 'bot koruması/erişim engeli — çalıştığı doğrulanamadı',
             'kontrol_edilmedi': 'henüz denetlenmedi', 'olu': 'çalışmıyor',
             'reddedildi': 'güvenlik kuralıyla denetim dışı bırakıldı'}


def tr_buyuk(metin):
    return metin.replace('i', 'İ').replace('ı', 'I').upper()


def _iz(site):
    sayfalar = sorted({k['page'] for k in site.get('kaynaklar', []) if k.get('page') is not None}, reverse=True)
    return 'sayfa ' + ', '.join(map(str, sayfalar)) if sayfalar else ''


def uret(siteler, aciklamalar, meta=None):
    """Kategorili düz metin katalog ve doğrulama raporu döner.

    Ana listeye YALNIZ 'calisiyor' durumundaki siteler girer. Korumalı ve
    denetlenmemiş siteler ayrı bölümde, durumları yazılarak listelenir.
    """
    gruplar, dogrulanamayan, eksik, hatali, olu, bilgisiz = {}, [], [], [], [], []
    for s in siteler:
        durum = s.get('kontrol', {}).get('durum', 'kontrol_edilmedi')
        if durum in ('olu', 'reddedildi'):
            olu.append(s)
            continue
        a = aciklamalar.get(s['anahtar']) or aciklamalar.get(s['url'])
        if not a:
            eksik.append(s['anahtar'])
            continue
        h = aciklama_dogrula(a)
        if h:
            hatali.append((s['anahtar'], h))
            continue
        if a.get('dayanak') == 'yok':
            bilgisiz.append((s, durum))
        elif durum == CALISAN:
            gruplar.setdefault(a['kategori'].strip(), []).append((s, a))
        else:
            dogrulanamayan.append((s, a, durum))

    satirlar = ['EKŞİ SÖZLÜK — "az kişinin bildiği muhteşem web siteleri" KATALOĞU', '']
    for k, v in (meta or {}).items():
        satirlar.append(f'{k}: {v}')
    toplam = sum(len(v) for v in gruplar.values())
    satirlar += [f'doğrulanmış çalışan site: {toplam}', '']

    def satir(s, a, ek=''):
        url, https_ok = gosterilecek_url(s)
        not_ = '  [yalnız HTTP; HTTPS doğrulanamadı]' if not https_ok and url.startswith('http://') else ''
        iz = _iz(s)
        return f'•  {url}{AYRAC}{a["ne"].strip()}  {a["yapabilirsin"].strip()}{not_}{ek}' + \
            (f'   ({iz})' if iz else '')

    for kat in sorted(gruplar, key=lambda x: (x.lower() == 'diğer', x.lower())):
        ogeler = sorted(gruplar[kat], key=lambda sa: sa[0]['anahtar'])
        satirlar += ['', f'■ {tr_buyuk(kat)}  ({len(ogeler)} site)', '']
        for s, a in ogeler:
            satirlar += [satir(s, a), '']
    if dogrulanamayan:
        satirlar += ['', f'■ ÇALIŞTIĞI DOĞRULANAMAYANLAR  ({len(dogrulanamayan)} site)', '',
                     'Bu sitelere ulaşıldı ama içerik doğrulanamadı ya da hiç denetlenmedi;', 'kendin açıp bakman gerekir.', '']
        for s, a, durum in sorted(dogrulanamayan, key=lambda x: x[0]['anahtar']):
            satirlar += [satir(s, a, f'  [durum: {DURUM_ADI.get(durum, durum)}]'), '']
    if bilgisiz:
        satirlar += ['', f'■ NE İŞE YARADIĞI DOĞRULANAMAYANLAR  ({len(bilgisiz)} site)', '',
                     'Bu sitelerin sayfasından ya da güvenilir bilgiden ne yaptıkları anlaşılamadı;',
                     'uydurma açıklama yazılmadı. Ekşi\'deki link etiketi yanında verildi.', '']
        for s, durum in sorted(bilgisiz, key=lambda x: x[0]['anahtar']):
            url, https_ok = gosterilecek_url(s)
            etiket = next((k['label'] for k in s.get('kaynaklar', []) if k.get('label') and k['label'] != k.get('ham_url')), '')
            not_ = '  [yalnız HTTP; HTTPS doğrulanamadı]' if not https_ok and url.startswith('http://') else ''
            satirlar += [f'•  {url}{AYRAC}[açıklama yok]' + (f'  Ekşi etiketi: "{etiket}"' if etiket else '') +
                         f'{not_}  [durum: {DURUM_ADI.get(durum, "çalışıyor") if durum != CALISAN else "çalışıyor"}]   ({_iz(s)})', '']
    rapor = {'dogrulanmis_calisan': toplam, 'kategori': len(gruplar),
             'aciklamasi_dogrulanamayan': [{'anahtar': s['anahtar'], 'durum': d} for s, d in bilgisiz],
             'dogrulanamayan': [{'anahtar': s['anahtar'], 'durum': d} for s, _, d in dogrulanamayan],
             'aciklamasi_eksik': eksik, 'kurala_uymayan': [{'anahtar': u, 'hatalar': h} for u, h in hatali],
             'calismayan_veya_reddedilen': [{'anahtar': s['anahtar'], 'url': s['url'], 'kontrol': s.get('kontrol'),
                                             'kaynaklar': s.get('kaynaklar')} for s in olu]}
    return '\n'.join(satirlar).rstrip() + '\n', rapor


def _yaz_json(yol, veri):
    with open(yol, 'w', encoding='utf-8') as f:
        json.dump(veri, f, ensure_ascii=False, indent=1)


def _oku_json(yol):
    with open(yol, encoding='utf-8') as f:
        return json.load(f)


def main(argv=None):
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    alt = ap.add_subparsers(dest='komut', required=True)
    a1 = alt.add_parser('tekillestir')
    a1.add_argument('girdiler', nargs='+')
    a1.add_argument('-o', default='siteler.json')
    a1.add_argument('--ust', type=int, help='beklenen ilk (en büyük) sayfa, ör. 809')
    a1.add_argument('--alt', type=int, help='beklenen son (en küçük) sayfa, ör. 610')
    a1.add_argument('--kisaltici-coz', action='store_true', help='t.co, bit.ly gibi bağlantıların hedefini güvenli denetimle oku')
    a1.add_argument('--proxy-ad-ile', action='store_true')
    a2 = alt.add_parser('kontrol')
    a2.add_argument('siteler')
    a2.add_argument('--proxy-ad-ile', action='store_true',
                    help="IP'ye tünel açtırmayan proxy için: ad yerelde denetlenir, bağlantıyı proxy çözer (ip_sabit=False)")
    a3 = alt.add_parser('sablon')
    a3.add_argument('siteler')
    a3.add_argument('-o', default='aciklamalar.json')
    a4 = alt.add_parser('uret')
    a4.add_argument('siteler')
    a4.add_argument('aciklamalar')
    a4.add_argument('-o', default='katalog.txt')
    a4.add_argument('--rapor', default='rapor.json')
    ar = ap.parse_args(argv)

    if ar.komut == 'tekillestir':
        kayitlar, sayfalar, uyarilar = girdileri_oku(ar.girdiler)
        if ar.kisaltici_coz:
            global KISALTICI_PROXY_MODU
            KISALTICI_PROXY_MODU = 'ad' if ar.proxy_ad_ile else 'ip'
            kayitlar = kisalticilari_coz(kayitlar)
        siteler, atlanan, ayni_alan = tekillestir(kayitlar)
        kapsam = kapsam_raporu(sayfalar, ar.ust, ar.alt)
        _yaz_json(ar.o, {'kapsam': kapsam, 'sayfalar': sorted(sayfalar.values(), key=lambda s: -(s['page'] or 0)),
                         'ham_baglanti': len(kayitlar), 'siteler': siteler, 'atlanan': atlanan,
                         'ayni_alan_farkli_yol': ayni_alan, 'uyarilar': uyarilar})
        print(f'{kapsam["gelen_sayfa"]} sayfa, {len(kayitlar)} ham bağlantı → {len(siteler)} tekil adres, '
              f'{len(atlanan)} atlandı, eksik sayfa {len(kapsam["eksik"])} → {ar.o}')
        for u in uyarilar:
            print('uyarı:', u)
    elif ar.komut == 'kontrol':
        veri = _oku_json(ar.siteler)
        mod = 'ad' if ar.proxy_ad_ile else 'ip'
        on = url_denetim.denetle(ON_KONTROL_URL, proxy_modu=mod)
        if on['durum'] != CALISAN:
            print(f'ön kontrol başarısız ({ON_KONTROL_URL} → {on["durum"]}: {on.get("neden") or on.get("kod")}). '
                  'Ağ yok ya da proxy IP tüneline izin vermiyor olabilir (o durumda --proxy-ad-ile). '
                  'Hiçbir site işaretlenmedi.', file=sys.stderr)
            return 2
        kontrol(veri['siteler'], denetleyici=lambda u: url_denetim.denetle(u, proxy_modu=mod),
                ara_kayit=lambda _: _yaz_json(ar.siteler, veri), ilerleme=lambda m: print('ilerleme', m, flush=True))
        veri['kontrol_modu'] = {'proxy_modu': mod, 'proxy_var': bool(url_denetim._proxy())}
        _yaz_json(ar.siteler, veri)
        say = {}
        for s in veri['siteler']:
            say[s['kontrol']['durum']] = say.get(s['kontrol']['durum'], 0) + 1
        print('kontrol:', say)
    elif ar.komut == 'sablon':
        veri = _oku_json(ar.siteler)
        sablon = {s['anahtar']: {'kategori': '', 'ne': '', 'yapabilirsin': 'Bu siteyle ',
                                 '_url': gosterilecek_url(s)[0], '_durum': s['kontrol']['durum'],
                                 '_baslik': s['kontrol'].get('baslik', ''), '_meta': s['kontrol'].get('meta', ''),
                                 '_etiketler': sorted({k['label'] for k in s['kaynaklar'] if k['label']})[:5]}
                  for s in veri['siteler'] if s['kontrol']['durum'] not in ('olu', 'reddedildi')}
        _yaz_json(ar.o, sablon)
        print(f'{len(sablon)} adres için şablon → {ar.o}')
    elif ar.komut == 'uret':
        veri = _oku_json(ar.siteler)
        meta = {}
        if veri.get('kapsam', {}).get('aralik'):
            k = veri['kapsam']
            meta = {'sayfa aralığı': k['aralik'], 'gelen sayfa': f"{k['gelen_sayfa']} / {k.get('beklenen')}",
                    'eksik sayfa': ', '.join(map(str, k['eksik'])) or 'yok'}
        metin, rapor = uret(veri['siteler'], _oku_json(ar.aciklamalar), meta)
        with open(ar.o, 'w', encoding='utf-8') as f:
            f.write(metin)
        rapor['kapsam'] = veri.get('kapsam')
        _yaz_json(ar.rapor, rapor)
        print(f"katalog: {rapor['dogrulanmis_calisan']} doğrulanmış çalışan, {len(rapor['dogrulanamayan'])} "
              f"doğrulanamayan, {rapor['kategori']} kategori → {ar.o}; eksik açıklama "
              f"{len(rapor['aciklamasi_eksik'])}, kurala uymayan {len(rapor['kurala_uymayan'])}")
        return 1 if rapor['aciklamasi_eksik'] or rapor['kurala_uymayan'] else 0
    return 0


if __name__ == '__main__':
    sys.exit(main())
