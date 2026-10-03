"""Katalog üretici testleri. Girdiler SENTETİK FIXTURE'dır, gerçek Ekşi verisi değildir."""
import json, os, subprocess, sys, tempfile, unittest
BURASI = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(BURASI, '..', 'araclar'))
import katalog_olustur as k

SENTETIK = os.path.join(BURASI, 'fixture', 'sentetik')
BETIK = os.path.join(BURASI, '..', 'araclar', 'katalog_olustur.py')
IYI = {'kategori': 'Araçlar',
       'ne': 'Bu site, dosyaları tarayıcı üzerinden başka biçimlere dönüştüren ücretsiz bir araçtır.',
       'yapabilirsin': 'Bu siteyle bir PDF dosyasını program kurmadan Word belgesine çevirebilirsin.'}


def sahte_denetleyici(tablo):
    """Ağa çıkmayan denetleyici: url -> sonuç tablosu; tabloda yoksa 'olu'."""
    def d(url):
        r = dict(tablo.get(url, {'durum': 'olu', 'neden': 'tabloda yok'}))
        r.setdefault('son_url', url)
        r.setdefault('url', url)
        d.cagrilar.append(url)
        return r
    d.cagrilar = []
    return d


class Normalize(unittest.TestCase):
    def test_sema_korunur_http_yukseltilmez(self):
        self.assertEqual(k.normalize('http://www.a.com/x?utm_source=e&id=3'), ('http://www.a.com/x?id=3', 'http'))
        self.assertEqual(k.normalize('https://a.com'), ('https://a.com/', 'https'))

    def test_semasiz_belirsiz(self):
        url, sema = k.normalize('mapc.am')
        self.assertEqual(sema, 'belirsiz')
        self.assertTrue(url.startswith('http://'))

    def test_web_disi_ve_goreli(self):
        self.assertIsNone(k.normalize('ftp://a.com/')[0])
        self.assertIsNone(k.normalize('/?q=x')[0])
        self.assertIsNone(k.normalize('javascript:alert(1)')[0])

    def test_spa_parcasi_korunur_diger_parca_atilir(self):
        self.assertEqual(k.normalize('https://a.com/#/takvim')[0], 'https://a.com/#/takvim')
        self.assertEqual(k.normalize('https://a.com/s#baslik')[0], 'https://a.com/s')


class JsonGirdi(unittest.TestCase):
    def setUp(self):
        self.kayitlar, self.sayfalar, self.uyarilar = k.girdileri_oku([SENTETIK])
        self.siteler, self.atlanan, self.ayni_alan = k.tekillestir(self.kayitlar)
        self.adres = {s['anahtar']: s for s in self.siteler}

    def test_klasor_tek_nesne_ve_dizi_okunur(self):
        self.assertEqual(sorted(self.sayfalar), [806, 808, 809])
        self.assertEqual(len(self.kayitlar), 14)

    def test_kapsam_eksik_sayfayi_bulur(self):
        r = k.kapsam_raporu(self.sayfalar, 809, 806)
        self.assertEqual(r['eksik'], [807])
        self.assertEqual(r['beklenen'], 4)

    def test_kapsam_200_sayfa_bolunmesi(self):
        r = k.kapsam_raporu({p: {} for p in range(809, 609, -1)}, 809, 610)
        self.assertEqual((r['eksik'], r['ilk_100'], r['sonraki_100']), ([], '809-710', '709-610'))

    def test_ayni_alan_farkli_yollar_birlesmez(self):
        self.assertIn('araclar.example/pdf-birlestir', self.adres)
        self.assertIn('araclar.example/resim-kucult', self.adres)
        self.assertIn('uygulama.example#/takvim', self.adres)
        self.assertIn('uygulama.example#/notlar', self.adres)
        self.assertIn('github.com/kisi/proje', self.adres)
        self.assertEqual(len(self.ayni_alan['araclar.example']), 2)

    def test_birebir_ayni_sayfa_birlesir_ve_iz_korunur(self):
        s = self.adres['eski.example/sayfa?id=7']
        self.assertEqual(sorted(s['semalar']), ['http', 'https'])
        self.assertEqual(s['url'], 'https://www.eski.example/sayfa/?id=7')  # açıkça HTTPS verilmiş hali
        self.assertEqual([x['page'] for x in s['kaynaklar']], [809, 808])
        self.assertTrue(all(x['page_url'] and x['observed_at'] for x in s['kaynaklar']))
        self.assertEqual(len(self.adres['indirici.example']['kaynaklar']), 2)

    def test_atlananlar_nedeniyle(self):
        nedenler = {a['ham']: a['neden'] for a in self.atlanan}
        self.assertIn('görsel', nedenler['https://i.imgur.com/abc.png'])
        self.assertIn('göreli', nedenler['/?q=x'])
        self.assertIn('ftp', nedenler['ftp://dosya.example/'])

    def test_varsayilan_durum_kontrol_edilmedi(self):
        self.assertTrue(all(s['kontrol']['durum'] == 'kontrol_edilmedi' for s in self.siteler))


class JsonLinesVeTekrar(unittest.TestCase):
    def test_jsonl_ve_ayni_sayfanin_tekrari(self):
        satirlar = [
            {'page': 700, 'page_url': 'https://eksisozluk.com/x--1?p=700', 'observed_at': 't1',
             'links': [{'label': 'a', 'url': 'https://a.example/'}]},
            {'page': 700, 'page_url': 'https://eksisozluk.com/x--1?p=700', 'observed_at': 't2',
             'links': ['https://b.example/']},
        ]
        with tempfile.TemporaryDirectory() as d:
            yol = os.path.join(d, 'sayfalar.jsonl')
            with open(yol, 'w', encoding='utf-8') as f:
                f.write('\n'.join(json.dumps(s) for s in satirlar))
            kayitlar, sayfalar, uyarilar = k.girdileri_oku([yol])
        self.assertEqual([x['url'] for x in kayitlar], ['https://a.example/', 'https://b.example/'])
        self.assertEqual(sayfalar[700]['link_sayisi'], 2)
        self.assertEqual(len(uyarilar), 1)

    def test_links_alani_yoksa_hata(self):
        with tempfile.TemporaryDirectory() as d:
            yol = os.path.join(d, 'bozuk.json')
            with open(yol, 'w', encoding='utf-8') as f:
                json.dump({'page': 1}, f)
            with self.assertRaises(ValueError):
                k.girdileri_oku([yol])


class SiteDenetle(unittest.TestCase):
    def test_http_dogrulamasiz_https_olmaz(self):
        site = {'url': 'http://eski.example/s'}
        d = sahte_denetleyici({'http://eski.example/s': {'durum': 'calisiyor', 'kod': 200}})
        r = k.site_denetle(site, d)
        self.assertEqual(d.cagrilar, ['https://eski.example/s', 'http://eski.example/s'])
        self.assertIsNone(r['https_url'])
        self.assertEqual(k.gosterilecek_url({'url': site['url'], 'kontrol': r}), ('http://eski.example/s', False))

    def test_https_calisirsa_kullanilir(self):
        d = sahte_denetleyici({'https://eski.example/s': {'durum': 'calisiyor', 'kod': 200}})
        r = k.site_denetle({'url': 'http://eski.example/s'}, d)
        self.assertEqual(r['https_url'], 'https://eski.example/s')

    def test_http_https_e_yonlenirse_varis_adresi(self):
        d = sahte_denetleyici({'https://eski.example/s': {'durum': 'olu'},
                               'http://eski.example/s': {'durum': 'calisiyor', 'son_url': 'https://yeni.example/'}})
        r = k.site_denetle({'url': 'http://eski.example/s'}, d)
        self.assertEqual(r['https_url'], 'https://yeni.example/')

    def test_https_yonlenip_http_ye_duserse_dogrulanmaz(self):
        d = sahte_denetleyici({'https://a.example/': {'durum': 'calisiyor', 'son_url': 'http://a.example/'}})
        r = k.site_denetle({'url': 'https://a.example/'}, d)
        self.assertIsNone(r['https_url'])

    def test_korumali_calisan_sayilmaz(self):
        d = sahte_denetleyici({'https://k.example/': {'durum': 'korumali', 'kod': 403}})
        r = k.site_denetle({'url': 'https://k.example/'}, d)
        self.assertEqual(r['durum'], 'korumali')
        self.assertIsNone(r['https_url'])


class Dogrulama(unittest.TestCase):
    def test_iyi_gecer(self):
        self.assertEqual(k.aciklama_dogrula(IYI), [])

    def test_kurallar(self):
        self.assertTrue(any('tam cümle' in x for x in k.aciklama_dogrula(dict(IYI, ne='Dosya dönüştürme aracı, ücretsiz ve hızlı çalışan bir site'))))
        self.assertTrue(any('birden fazla' in x for x in k.aciklama_dogrula(dict(IYI, ne='Bu bir dönüştürücü sitedir. Ayrıca ücretsiz olarak çalışır.'))))
        self.assertTrue(k.aciklama_dogrula(dict(IYI, ne='Dönüştürücü.')))
        self.assertTrue(k.aciklama_dogrula(dict(IYI, yapabilirsin='PDF çevirirsin.')))
        self.assertTrue(k.aciklama_dogrula(dict(IYI, yapabilirsin='Bu siteyle ')))


class Uret(unittest.TestCase):
    def test_yalniz_dogrulanmis_calisan_ana_listede(self):
        kay = [{'page': 809, 'page_url': 'u', 'observed_at': 't', 'label': '', 'ham_url': ''}]
        siteler = [
            {'anahtar': 'a.example', 'url': 'https://a.example/', 'kaynaklar': kay,
             'kontrol': {'durum': 'calisiyor', 'https_url': 'https://a.example/'}},
            {'anahtar': 'h.example', 'url': 'http://h.example/', 'kaynaklar': kay,
             'kontrol': {'durum': 'calisiyor', 'https_url': None}},
            {'anahtar': 'k.example', 'url': 'https://k.example/', 'kaynaklar': kay, 'kontrol': {'durum': 'korumali'}},
            {'anahtar': 'n.example', 'url': 'https://n.example/', 'kaynaklar': kay, 'kontrol': {'durum': 'kontrol_edilmedi'}},
            {'anahtar': 'o.example', 'url': 'https://o.example/', 'kaynaklar': kay, 'kontrol': {'durum': 'olu'}},
            {'anahtar': 'r.example', 'url': 'http://localhost/', 'kaynaklar': kay, 'kontrol': {'durum': 'reddedildi'}},
            {'anahtar': 'e.example', 'url': 'https://e.example/', 'kaynaklar': kay, 'kontrol': {'durum': 'calisiyor', 'https_url': 'https://e.example/'}},
        ]
        acik = {s: dict(IYI, kategori='Eğitim' if s == 'h.example' else 'Araçlar')
                for s in ('a.example', 'h.example', 'k.example', 'n.example', 'o.example', 'r.example')}
        metin, rapor = k.uret(siteler, acik, {'sayfa aralığı': '809-610'})
        ana, _, dogrulanamayan = metin.partition('ÇALIŞTIĞI DOĞRULANAMAYANLAR')
        self.assertIn('doğrulanmış çalışan site: 2', metin)
        self.assertIn('■ EĞİTİM  (1 site)', ana)
        self.assertIn('•  https://a.example/' + k.AYRAC + IYI['ne'], ana)
        self.assertIn('http://h.example/', ana)
        self.assertIn('[yalnız HTTP; HTTPS doğrulanamadı]', ana)
        self.assertIn('(sayfa 809)', ana)
        self.assertNotIn('k.example', ana)
        self.assertNotIn('n.example', ana)
        self.assertIn('k.example', dogrulanamayan)
        self.assertIn('bot koruması', dogrulanamayan)
        self.assertIn('henüz denetlenmedi', dogrulanamayan)
        k_satiri = next(s for s in dogrulanamayan.splitlines() if 'k.example' in s)
        self.assertNotIn('yalnız HTTP', k_satiri)  # HTTPS adres, yalnız doğrulanamadı
        self.assertNotIn('o.example', metin)
        self.assertNotIn('localhost', metin)
        self.assertNotIn('|', metin)
        self.assertEqual(rapor['aciklamasi_eksik'], ['e.example'])
        self.assertEqual(len(rapor['calismayan_veya_reddedilen']), 2)


class KomutSatiri(unittest.TestCase):
    def test_uctan_uca_aga_cikmadan(self):
        with tempfile.TemporaryDirectory() as d:
            s, a, t, r = (os.path.join(d, x) for x in ('s.json', 'a.json', 'k.txt', 'r.json'))
            cikti = subprocess.run([sys.executable, BETIK, 'tekillestir', SENTETIK, '-o', s, '--ust', '809', '--alt', '806'],
                                   check=True, capture_output=True, text=True).stdout
            self.assertIn('eksik sayfa 1', cikti)
            veri = json.load(open(s, encoding='utf-8'))
            for site in veri['siteler']:  # ağ yerine sabit sonuç
                site['kontrol'] = {'durum': 'calisiyor', 'https_url': site['url'] if site['url'].startswith('https') else None}
            json.dump(veri, open(s, 'w', encoding='utf-8'))
            subprocess.run([sys.executable, BETIK, 'sablon', s, '-o', a], check=True, capture_output=True)
            sablon = json.load(open(a, encoding='utf-8'))
            self.assertEqual(subprocess.run([sys.executable, BETIK, 'uret', s, a, '-o', t, '--rapor', r],
                                            capture_output=True).returncode, 1)
            json.dump({u: IYI for u in sablon}, open(a, 'w', encoding='utf-8'))
            self.assertEqual(subprocess.run([sys.executable, BETIK, 'uret', s, a, '-o', t, '--rapor', r],
                                            capture_output=True).returncode, 0)
            with open(t, encoding='utf-8') as f:
                metin = f.read()
            self.assertIn('eksik sayfa: 807', metin)
            self.assertIn(f'doğrulanmış çalışan site: {len(veri["siteler"])}', metin)


if __name__ == '__main__':
    unittest.main(verbosity=2)
