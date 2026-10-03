"""Güvenli URL denetimi testleri. Yerel sunucu ve sahte DNS çözücü kullanır (SENTETİK)."""
import http.server, os, socket, sys, threading, unittest
BURASI = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(BURASI, '..', 'araclar'))
import url_denetim as u


class Kurallar(unittest.TestCase):
    def reddet(self, url):
        with self.assertRaises(u.Reddedildi, msg=url):
            u.url_kural_denetimi(url)

    def test_ic_ve_ozel_adresler(self):
        for url in ['http://localhost/', 'http://LOCALHOST./', 'http://app.localhost/', 'http://127.0.0.1/',
                    'http://10.0.0.1/', 'http://192.168.1.1/', 'http://172.16.5.4/', 'http://169.254.169.254/latest/meta-data',
                    'http://100.64.0.1/', 'http://0.0.0.0/', 'http://[::1]/', 'http://[fe80::1]/', 'http://[fc00::1]/',
                    'http://[::ffff:127.0.0.1]/', 'http://224.0.0.1/', 'http://yazici.local/', 'http://nas.internal/',
                    'http://intranet/']:
            self.reddet(url)

    def test_sema_port_kullanici(self):
        for url in ['file:///etc/passwd', 'ftp://a.com/', 'gopher://a.com/', 'javascript:alert(1)',
                    'https://a.com:8443/', 'http://a.com:22/', 'https://kisi:parola@a.com/', 'https://a.com@10.0.0.1/']:
            self.reddet(url)

    def test_ascii_disi_yol_kodlanir(self):
        self.assertEqual(u.url_kural_denetimi('https://a.com/şehir rehberi?q=ğ&x=%C3%BC')[3],
                         '/%C5%9Fehir%20rehberi?q=%C4%9F&x=%C3%BC')

    def test_kamu_adres_gecer(self):
        self.assertEqual(u.url_kural_denetimi('https://Example.com/a?b=1'), ('https', 'example.com', 443, '/a?b=1'))
        self.assertEqual(u.url_kural_denetimi('http://93.184.215.14/')[1], '93.184.215.14')


class MetaOkuma(unittest.TestCase):
    def test_sira_bagimsiz_ve_varlik_cozumu(self):
        html = '<meta content="Ara &amp; bul" name="description"><meta property="og:description" content=\'OG metni\'>'
        self.assertEqual(u._meta(html, 'name', 'description'), 'Ara & bul')
        self.assertEqual(u._meta(html, 'property', 'og:description'), 'OG metni')
        self.assertEqual(u._meta(html, 'name', 'keywords'), '')


class Cozumleme(unittest.TestCase):
    def test_karisik_cevap_reddedilir(self):
        r = u.denetle('https://karisik.example.com/', cozucu=lambda h, p: ['93.184.215.14', '10.0.0.5'], proxy=None)
        self.assertEqual(r['durum'], 'reddedildi')
        self.assertIn('10.0.0.5', r['neden'])

    def test_ipv4_esleme_ipv6_reddedilir(self):
        r = u.denetle('https://esleme.example.com/', cozucu=lambda h, p: ['::ffff:127.0.0.1'], proxy=None)
        self.assertEqual(r['durum'], 'reddedildi')

    def test_baglanti_kopmasi_olu_sayilmaz(self):
        def kopan(*a, **k):
            raise ConnectionResetError(104, 'reset')
        eski = u.tek_istek
        u.tek_istek = kopan
        try:
            r = u.denetle('https://kopan.example.com/')
        finally:
            u.tek_istek = eski
        self.assertEqual(r['durum'], 'kontrol_edilmedi')

    def test_gecici_dns_olu_sayilmaz_kalici_olu_sayilir(self):
        import socket as s
        def gecici(h, p): raise s.gaierror(s.EAI_AGAIN, 'Temporary failure in name resolution')
        def yok(h, p): raise s.gaierror(s.EAI_NONAME, 'Name or service not known')
        self.assertEqual(u.denetle('https://g.example.com/', cozucu=gecici, proxy=None)['durum'], 'kontrol_edilmedi')
        self.assertEqual(u.denetle('https://y.example.com/', cozucu=yok, proxy=None)['durum'], 'olu')

    def test_bos_cevap(self):
        self.assertEqual(u.denetle('https://bos.example.com/', cozucu=lambda h, p: [], proxy=None)['durum'], 'reddedildi')


class _Isleyici(http.server.BaseHTTPRequestHandler):
    istekler = []
    YOLLAR = {
        '/ok': (200, {}, b'<html lang="tr"><head><title>Deneme Sitesi</title><meta name="description" content="sahte"></head><body><h1>Ana <b>Baslik</b></h1></body></html>'),
        '/korumali': (403, {}, b'cf'),
        '/yok': (404, {}, b''),
        '/yonlen-iyi': (302, {'Location': '/ok'}, b''),
        '/yonlen-ozel': (302, {'Location': 'http://ozel.example.com/'}, b''),
        '/yonlen-localhost': (301, {'Location': 'http://localhost/ok'}, b''),
        '/yonlen-ip': (307, {'Location': 'http://169.254.169.254/latest/meta-data'}, b''),
        '/yonlen-port': (302, {'Location': 'http://genel.example.com:8080/ok'}, b''),
        '/yonlen-dosya': (302, {'Location': 'file:///etc/passwd'}, b''),
        '/dongu': (302, {'Location': '/dongu'}, b''),
        '/ortam': (403, {}, b'{"message":"GitHub access to this repository is not enabled for this session."}'),
        '/%C5%9Fehir': (200, {}, b'<title>Sehir</title>'),
        '/yonlen-baska-ad': (302, {'Location': 'http://ikinci.example.com/ok'}, b''),
    }

    def do_GET(self):
        _Isleyici.istekler.append((self.headers.get('Host'), self.path))
        kod, basliklar, govde = self.YOLLAR.get(self.path, (404, {}, b''))
        self.send_response(kod)
        for k, v in basliklar.items():
            self.send_header(k, v)
        self.send_header('Content-Length', str(len(govde)))
        self.end_headers()
        self.wfile.write(govde)

    def log_message(self, *a):
        pass


class _SunucuTabani(unittest.TestCase):
    """Port kuralı 80 istediği için sunucu 127.0.0.1:80'de açılır.

    Test, yalnız 127.0.0.1'e izin veren bir adres politikası ve sahte bir
    çözücü kullanır; böylece "kamu" sayılan tek hedef bu yerel sunucudur ve
    ona yönlendiren/başka yere giden her adım gerçek kurallarla denetlenir.
    """

    @classmethod
    def setUpClass(cls):
        try:
            cls.sunucu = http.server.ThreadingHTTPServer(('127.0.0.1', 80), _Isleyici)
        except OSError as e:
            raise unittest.SkipTest(f'127.0.0.1:80 açılamadı: {e}')
        threading.Thread(target=cls.sunucu.serve_forever, daemon=True).start()

    @classmethod
    def tearDownClass(cls):
        cls.sunucu.shutdown()
        cls.sunucu.server_close()

    def setUp(self):
        _Isleyici.istekler.clear()
        self.cozumler = []

    def cozucu(self, tablo):
        def c(host, port):
            self.cozumler.append(host)
            sonuc = tablo[host]
            return sonuc.pop(0) if isinstance(sonuc, list) and sonuc and isinstance(sonuc[0], list) else sonuc
        return c



class YerelSunucu(_SunucuTabani):
    def denetle(self, yol, tablo=None, varsayilan_politika=False):
        tablo = tablo or {'genel.example.com': ['127.0.0.1'], 'ozel.example.com': ['10.0.0.7'],
                          'ikinci.example.com': ['127.0.0.1']}
        politika = u.adres_kamu_mu if varsayilan_politika else (lambda ip: ip == '127.0.0.1')
        return u.denetle('http://genel.example.com' + yol, cozucu=self.cozucu(tablo), adres_izinli=politika,
                         proxy=None, zaman_asimi=5)

    def test_varsayilan_politika_yerel_sunucuya_hic_baglanmaz(self):
        r = self.denetle('/ok', varsayilan_politika=True)
        self.assertEqual(r['durum'], 'reddedildi')
        self.assertEqual(_Isleyici.istekler, [])

    def test_calisan_baslik_ve_host_basligi(self):
        r = self.denetle('/ok')
        self.assertEqual((r['durum'], r['kod'], r['baslik'], r['meta']), ('calisiyor', 200, 'Deneme Sitesi', 'sahte'))
        self.assertEqual((r['h1'], r['dil']), ('Ana Baslik', 'tr'))
        self.assertEqual(_Isleyici.istekler, [('genel.example.com', '/ok')])

    def test_ascii_disi_yol_gercek_istekte(self):
        r = self.denetle('/şehir')
        self.assertEqual((r['durum'], r['baslik']), ('calisiyor', 'Sehir'))

    def test_ortam_engeli_korumali_sayilmaz(self):
        r = self.denetle('/ortam')
        self.assertEqual(r['durum'], 'kontrol_edilmedi')
        self.assertIn('proxy engeli', r['neden'])

    def test_korumali_ve_olu(self):
        self.assertEqual(self.denetle('/korumali')['durum'], 'korumali')
        self.assertEqual(self.denetle('/yok')['durum'], 'olu')

    def test_guvenli_yonlendirme_izlenir(self):
        r = self.denetle('/yonlen-iyi')
        self.assertEqual(r['durum'], 'calisiyor')
        self.assertEqual([z['kod'] for z in r['zincir']], [302, 200])

    def test_ozel_ip_ye_cozulen_yonlendirme_engellenir(self):
        r = self.denetle('/yonlen-ozel')
        self.assertEqual(r['durum'], 'reddedildi')
        self.assertIn('10.0.0.7', r['neden'])
        self.assertEqual(len(_Isleyici.istekler), 1)

    def test_localhost_ip_port_sema_yonlendirmeleri_engellenir(self):
        for yol in ('/yonlen-localhost', '/yonlen-ip', '/yonlen-port', '/yonlen-dosya'):
            _Isleyici.istekler.clear()
            r = self.denetle(yol)
            self.assertEqual(r['durum'], 'reddedildi', yol)
            self.assertEqual(len(_Isleyici.istekler), 1, yol)

    def test_yonlendirme_dongusu_sinirli(self):
        r = self.denetle('/dongu')
        self.assertEqual(r['durum'], 'olu')
        self.assertEqual(len(_Isleyici.istekler), u.AZAMI_YONLENDIRME + 1)

    def test_dns_yeniden_cozumleme_engellenir(self):
        # İlk çözüm izinli, ikinci çözüm özel IP: ikinci adım bağlanmadan reddedilir.
        tablo = {'genel.example.com': [['127.0.0.1'], ['10.0.0.9']], 'ikinci.example.com': ['127.0.0.1']}
        r = self.denetle('/dongu', tablo=tablo)
        self.assertEqual(r['durum'], 'reddedildi')
        self.assertIn('10.0.0.9', r['neden'])
        self.assertEqual(len(_Isleyici.istekler), 1)

    def test_her_adimda_tek_cozum_ve_ip_sabitleme(self):
        r = self.denetle('/yonlen-baska-ad')
        self.assertEqual(r['durum'], 'calisiyor')
        self.assertEqual(self.cozumler, ['genel.example.com', 'ikinci.example.com'])
        self.assertEqual([z['ip'] for z in r['zincir']], ['127.0.0.1', '127.0.0.1'])
        self.assertEqual([h for h, _ in _Isleyici.istekler], ['genel.example.com', 'ikinci.example.com'])


class _SahteProxy(threading.Thread):
    """CONNECT hedefini kaydeden, adları kendisi 127.0.0.1'e çözen sahte proxy (SENTETİK)."""
    def __init__(self):
        super().__init__(daemon=True)
        self.s = socket.socket()
        self.s.bind(('127.0.0.1', 0))
        self.s.listen(8)
        self.port = self.s.getsockname()[1]
        self.hedefler = []

    def run(self):
        while True:
            try:
                c, _ = self.s.accept()
            except OSError:
                return
            veri = b''
            while b'\r\n\r\n' not in veri:
                veri += c.recv(1024)
            hedef = veri.split(b' ')[1].decode()
            self.hedefler.append(hedef)
            port = int(hedef.rsplit(':', 1)[1])
            u_ = socket.create_connection(('127.0.0.1', port))
            c.sendall(b'HTTP/1.1 200 Connection Established\r\n\r\n')
            def aktar(a, b):
                try:
                    while (d := a.recv(4096)):
                        b.sendall(d)
                except OSError:
                    pass
                finally:
                    for x in (a, b):
                        try:
                            x.shutdown(socket.SHUT_RDWR)
                        except OSError:
                            pass
            threading.Thread(target=aktar, args=(c, u_), daemon=True).start()
            threading.Thread(target=aktar, args=(u_, c), daemon=True).start()


class ProxyModlari(_SunucuTabani):
    def setUp(self):
        super().setUp()
        self.proxy = _SahteProxy()
        self.proxy.start()

    def tearDown(self):
        self.proxy.s.close()

    def pdenetle(self, url, tablo, mod):
        return u.denetle(url, cozucu=self.cozucu(tablo), adres_izinli=lambda ip: ip == '127.0.0.1',
                         proxy=('127.0.0.1', self.proxy.port), proxy_modu=mod, zaman_asimi=5)

    def test_ip_modu_proxyye_cozulmus_ip_gonderir(self):
        r = self.pdenetle('http://genel.example.com/ok', {'genel.example.com': ['127.0.0.1']}, 'ip')
        self.assertEqual(r['durum'], 'calisiyor')
        self.assertEqual(self.proxy.hedefler, ['127.0.0.1:80'])
        self.assertTrue(r['zincir'][0]['ip_sabit'])

    def test_ad_modu_adi_gonderir_ve_isaretler(self):
        r = self.pdenetle('http://genel.example.com/ok', {'genel.example.com': ['127.0.0.1']}, 'ad')
        self.assertEqual(r['durum'], 'calisiyor')
        self.assertEqual(self.proxy.hedefler, ['genel.example.com:80'])
        self.assertFalse(r['zincir'][0]['ip_sabit'])

    def test_ad_modunda_da_yerel_denetim_ozel_adresi_durdurur(self):
        r = self.pdenetle('http://ozel.example.com/', {'ozel.example.com': ['10.0.0.7']}, 'ad')
        self.assertEqual(r['durum'], 'reddedildi')
        self.assertEqual(self.proxy.hedefler, [])


if __name__ == '__main__':
    unittest.main(verbosity=2)
