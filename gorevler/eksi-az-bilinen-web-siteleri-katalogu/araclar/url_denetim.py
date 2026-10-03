"""Kamu web adreslerini güvenli biçimde denetler (yalnız standart kütüphane).

Korumalar:
  - Yalnız http/https, yalnız 80/443 portu, kullanıcı:parola içeren adres yok.
  - localhost, .local/.internal gibi iç adlar ve noktasız adlar reddedilir.
  - Ad bir kez çözülür; dönen adreslerin HEPSİ kamu (global) olmalıdır.
    Özel, loopback, link-local, multicast, ayrılmış, CGNAT adresleri reddedilir.
  - Bağlantı çözülen IP'ye sabitlenir (DNS yeniden çözümleme / rebinding yok);
    TLS sertifikası ve SNI yine alan adına göre doğrulanır.
  - Yönlendirmeler otomatik izlenmez: her adım aynı kurallarla yeniden
    denetlenir, en fazla AZAMI_YONLENDIRME adım.
  - HTTPS_PROXY tanımlıysa varsayılan olarak proxy'ye çözülmüş IP için
    CONNECT gönderilir; proxy adı yeniden çözmez. Bazı proxy'ler (ör. bu
    bulut ortamınınki) Host başlığının CONNECT hedefiyle aynı olmasını
    ister ve IP'ye tünel açtırmaz. Böyle bir proxy için çağıran açıkça
    proxy_modu='ad' seçebilir: ad yine yerelde çözülüp denetlenir, ama
    bağlantıyı proxy kendi çözümüyle kurar. Bu modda sonuçta
    ip_sabit=False yazar; DNS yeniden çözümleme koruması proxy'nin
    kendi politikasına kalır.
"""
import http.client
import ipaddress
import os
import re
import socket
import ssl
import urllib.parse

AZAMI_YONLENDIRME = 5
AZAMI_GOVDE = 200_000
IZINLI_PORTLAR = {'http': 80, 'https': 443}
IC_AD_SONEKLERI = ('.localhost', '.local', '.internal', '.intranet', '.lan', '.home',
                   '.corp', '.localdomain', '.home.arpa', '.test', '.invalid', '.onion')
TARAYICI_UA = ('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 '
               '(KHTML, like Gecko) Chrome/141.0 Safari/537.36')


# Yanıt sitenin değil aradaki proxy/ortamın engeli olduğunda gövdede görülen imzalar.
ORTAM_ENGEL_IMZALARI = (
    'GitHub access to this repository is not enabled for this session',
    'upstream connect error or disconnect/reset before headers',
    'request rejected: Host header does not match CONNECT target',
)
# Kesin olmayan, geçici ya da ara ağ kaynaklı olabilen bağlantı hataları.
BELIRSIZ_HATALAR = (ConnectionResetError, TimeoutError, socket.timeout, ssl.SSLEOFError, BrokenPipeError,
                    ConnectionAbortedError)


class Reddedildi(Exception):
    """Adres güvenlik kurallarına uymuyor; ağa hiç çıkılmadı."""


def adres_kamu_mu(ip):
    a = ipaddress.ip_address(ip)
    if isinstance(a, ipaddress.IPv6Address) and a.ipv4_mapped:
        a = a.ipv4_mapped
    return a.is_global and not a.is_multicast


def varsayilan_cozucu(host, port):
    return sorted({ai[4][0] for ai in socket.getaddrinfo(host, port, type=socket.SOCK_STREAM)})


def url_kural_denetimi(url):
    """Ağa çıkmadan yapılan denetim; (sema, host, port, yol) döner ya da Reddedildi atar."""
    p = urllib.parse.urlsplit(url)
    sema = p.scheme.lower()
    if sema not in IZINLI_PORTLAR:
        raise Reddedildi(f'şema izinli değil: {sema or "(yok)"}')
    if p.username or p.password or '@' in p.netloc:
        raise Reddedildi('adreste kullanıcı bilgisi var')
    host = (p.hostname or '').lower().rstrip('.')
    if not host:
        raise Reddedildi('alan adı yok')
    try:
        port = p.port or IZINLI_PORTLAR[sema]
    except ValueError:
        raise Reddedildi('geçersiz port')
    if port != IZINLI_PORTLAR[sema]:
        raise Reddedildi(f'standart dışı port: {port}')
    try:
        ipaddress.ip_address(host)
        ip_literal = True
    except ValueError:
        ip_literal = False
    if ip_literal:
        if not adres_kamu_mu(host):
            raise Reddedildi(f'kamu olmayan IP: {host}')
    else:
        if host == 'localhost' or host.endswith(IC_AD_SONEKLERI) or '.' not in host:
            raise Reddedildi(f'iç ağ adı: {host}')
        if not re.fullmatch(r'[a-z0-9.-]+', host):
            try:
                host = host.encode('idna').decode('ascii')
            except UnicodeError:
                raise Reddedildi('geçersiz alan adı')
    # IRI -> URI: ASCII dışı karakterler yüzde-kodlanır (zaten kodlu olanlar korunur)
    guvenli = "/%:@!$&'()*+,;=-._~"
    yol = urllib.parse.quote(p.path or '/', safe=guvenli)
    if p.query:
        yol += '?' + urllib.parse.quote(p.query, safe=guvenli + '?')
    return sema, host, port, yol


def _proxy():
    deger = os.environ.get('HTTPS_PROXY') or os.environ.get('https_proxy')
    if not deger:
        return None
    p = urllib.parse.urlsplit(deger if '://' in deger else 'http://' + deger)
    return p.hostname, p.port or 8080


def _soket(ip, port, zaman_asimi, proxy, proxy_hedefi=None):
    if not proxy:
        return socket.create_connection((ip, port), zaman_asimi)
    if proxy_hedefi:
        ip = proxy_hedefi
    s = socket.create_connection(proxy, zaman_asimi)
    hedef = f'[{ip}]:{port}' if ':' in ip else f'{ip}:{port}'
    s.sendall(f'CONNECT {hedef} HTTP/1.1\r\nHost: {hedef}\r\n\r\n'.encode())
    yanit = b''
    while b'\r\n\r\n' not in yanit and len(yanit) < 8192:
        parca = s.recv(1024)
        if not parca:
            break
        yanit += parca
    ilk = yanit.split(b'\r\n', 1)[0]
    if not re.match(rb'HTTP/1\.[01] 200', ilk):
        s.close()
        raise OSError('proxy CONNECT reddetti: ' + ilk.decode('latin1', 'replace'))
    return s


class _SabitIPBaglanti(http.client.HTTPSConnection):
    def __init__(self, host, ip, port, tls, zaman_asimi, proxy, baglam, proxy_hedefi=None):
        if tls:
            super().__init__(host, port, timeout=zaman_asimi, context=baglam)
        else:
            http.client.HTTPConnection.__init__(self, host, port, timeout=zaman_asimi)
        self._ip, self._tls, self._proxy, self._proxy_hedefi = ip, tls, proxy, proxy_hedefi
        self.default_port = 443 if tls else 80  # Host başlığına gereksiz ':80' eklenmesin

    def connect(self):
        s = _soket(self._ip, self.port, self.timeout, self._proxy, self._proxy_hedefi)
        self.sock = self._context.wrap_socket(s, server_hostname=self.host) if self._tls else s


def tek_istek(url, cozucu=varsayilan_cozucu, adres_izinli=adres_kamu_mu,
              zaman_asimi=20, proxy='ortam', baglam=None, proxy_modu='ip'):
    """Tek bir GET (yönlendirme izlemez). (kod, başlıklar, gövde, ip, ip_sabit) döner."""
    sema, host, port, yol = url_kural_denetimi(url)
    try:
        ipler = [str(ipaddress.ip_address(host))]
    except ValueError:
        ipler = cozucu(host, port)
    if not ipler:
        raise Reddedildi('ad çözülemedi')
    yasak = [ip for ip in ipler if not adres_izinli(ip)]
    if yasak:
        raise Reddedildi(f'{host} kamu olmayan adrese çözülüyor: {", ".join(yasak)}')
    ip = ipler[0]
    prx = _proxy() if proxy == 'ortam' else proxy
    ad_ile = bool(prx) and proxy_modu == 'ad'
    b = _SabitIPBaglanti(host, ip, port, sema == 'https', zaman_asimi, prx,
                         baglam or ssl.create_default_context(), proxy_hedefi=host if ad_ile else None)
    try:
        b.request('GET', yol, headers={'User-Agent': TARAYICI_UA, 'Accept': 'text/html,*/*;q=0.8',
                                       'Accept-Language': 'tr-TR,tr;q=0.9,en;q=0.8',
                                       'Accept-Encoding': 'identity', 'Connection': 'close'})
        y = b.getresponse()
        govde = y.read(AZAMI_GOVDE)
        return y.status, {k.lower(): v for k, v in y.getheaders()}, govde, ip, not ad_ile
    finally:
        b.close()


def _temiz(html):
    metin = re.sub(r'<[^>]+>', ' ', html)
    for a, b in (('&amp;', '&'), ('&quot;', '"'), ('&#39;', "'"), ('&#x27;', "'"), ('&lt;', '<'), ('&gt;', '>'), ('&nbsp;', ' ')):
        metin = metin.replace(a, b)
    return re.sub(r'\s+', ' ', metin).strip()


def _meta(metin, ozellik, ad):
    """<meta name/property=... content=...> değerini sıra bağımsız okur."""
    for m in re.finditer(r'<meta\b[^>]*>', metin, re.I):
        etiket = m.group(0)
        if re.search(ozellik + r'\s*=\s*["\']' + re.escape(ad) + r'["\']', etiket, re.I):
            c = re.search(r'content\s*=\s*"([^"]*)"', etiket, re.I) or re.search(r"content\s*=\s*'([^']*)'", etiket, re.I)
            if c:
                return _temiz(c.group(1))
    return ''


def denetle(url, **kw):
    """Yönlendirmeleri kurallarla izleyerek denetler; sonuç sözlüğü döner.

    durum: 'calisiyor' (2xx), 'korumali' (401/403/405/429/503 — yaşıyor olabilir,
    ama doğrulanmadı), 'olu' (diğer 4xx/5xx, DNS/TLS/zaman aşımı),
    'reddedildi' (güvenlik kuralı; ağa çıkılmadı ya da yönlendirme engellendi).
    """
    zincir = []
    simdiki = url
    for _ in range(AZAMI_YONLENDIRME + 1):
        try:
            kod, basliklar, govde, ip, ip_sabit = tek_istek(simdiki, **kw)
        except Reddedildi as e:
            return {'durum': 'reddedildi', 'neden': str(e), 'url': url, 'son_url': simdiki, 'zincir': zincir}
        except BELIRSIZ_HATALAR as e:
            return {'durum': 'kontrol_edilmedi', 'neden': f'ulaşılamadı, kesin değil ({type(e).__name__})',
                    'url': url, 'son_url': simdiki, 'zincir': zincir}
        except (OSError, http.client.HTTPException, ssl.SSLError) as e:
            return {'durum': 'olu', 'neden': f'{type(e).__name__}: {str(e)[:150]}', 'url': url,
                    'son_url': simdiki, 'zincir': zincir}
        except Exception as e:  # denetleyicinin kendi hatası siteyi "ölü" yapmaz
            return {'durum': 'kontrol_edilmedi', 'neden': f'iç hata {type(e).__name__}: {str(e)[:150]}',
                    'url': url, 'son_url': simdiki, 'zincir': zincir}
        zincir.append({'url': simdiki, 'kod': kod, 'ip': ip, 'ip_sabit': ip_sabit})
        if kod in (301, 302, 303, 307, 308) and basliklar.get('location'):
            simdiki = urllib.parse.urljoin(simdiki, basliklar['location'].strip())
            continue
        metin = govde.decode('utf-8', 'replace')
        baslik = re.search(r'<title[^>]*>(.*?)</title>', metin, re.S | re.I)
        meta = _meta(metin, 'name', 'description')
        og = _meta(metin, 'property', 'og:description')
        og_baslik = _meta(metin, 'property', 'og:title')
        h1 = re.search(r'<h1[^>]*>(.*?)</h1>', metin, re.S | re.I)
        dil = re.search(r'<html[^>]+lang=["\']([a-zA-Z-]+)', metin, re.I)
        if 200 <= kod < 300:
            durum = 'calisiyor'
        elif kod in (401, 403, 405, 429, 503):
            durum = 'korumali'
        else:
            durum = 'olu'
        sonuc = {'durum': durum, 'kod': kod, 'url': url, 'son_url': simdiki, 'zincir': zincir,
                 'baslik': _temiz(baslik.group(1))[:200] if baslik else '',
                 'meta': meta[:300], 'og_aciklama': og[:300], 'og_baslik': og_baslik[:200],
                 'h1': _temiz(h1.group(1))[:200] if h1 else '', 'dil': dil.group(1) if dil else ''}
        if durum != 'calisiyor':  # engelin kaynağını (site mi, ara proxy mi) ayırt etmek için
            sonuc['govde_ozeti'] = re.sub(r'\s+', ' ', metin)[:160]
            imza = next((i for i in ORTAM_ENGEL_IMZALARI if i in metin), None)
            if imza:
                sonuc['durum'] = 'kontrol_edilmedi'
                sonuc['neden'] = 'ara ortam/proxy engeli: ' + imza
        return sonuc
    return {'durum': 'olu', 'neden': f'{AZAMI_YONLENDIRME} adımdan fazla yönlendirme', 'url': url,
            'son_url': simdiki, 'zincir': zincir}
