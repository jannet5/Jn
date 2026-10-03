import json, os, subprocess, sys, tempfile, unittest
BURASI = os.path.dirname(os.path.abspath(__file__))
sys.path.insert(0, os.path.join(BURASI, '..', 'araclar'))
import katalog_olustur as k

FIXTURE = os.path.join(BURASI, 'fixture', 'eksi-linkler.txt')

class Normalize(unittest.TestCase):
    def test_https_ve_izleme(self):
        self.assertEqual(k.normalize('http://www.a.com/x?utm_source=e&id=3'), 'https://www.a.com/x?id=3')
        self.assertEqual(k.normalize('mapc.am'), 'https://mapc.am/')
        self.assertIsNone(k.normalize('ftp://a.com/'))
        self.assertIsNone(k.normalize('localhost'))

class Tekillestir(unittest.TestCase):
    def setUp(self):
        satirlar, self.meta = k.linkleri_oku([FIXTURE])
        self.siteler, self.atlanan = k.tekillestir(satirlar)
        self.urller = [s['url'] for s in self.siteler]

    def test_meta(self):
        self.assertEqual(self.meta['aralik'], '5-1')

    def test_hepsi_https_ve_tekil(self):
        self.assertTrue(all(u.startswith('https://') for u in self.urller))
        self.assertEqual(len(set(s['anahtar'] for s in self.siteler)), len(self.siteler))

    def test_ayni_site_bir_kez(self):
        tekrar = [s for s in self.siteler if s['anahtar'] == 'tekrar.example.com']
        self.assertEqual(len(tekrar), 1)
        self.assertEqual(tekrar[0]['gecis'], 2)
        self.assertEqual(sorted(tekrar[0]['sayfalar']), [3, 4])

    def test_platform_yolu_korunur(self):
        self.assertIn('https://github.com/kisi/proje', self.urller)
        self.assertIn('https://github.com/kisi/baska', self.urller)

    def test_gorsel_atlanir(self):
        self.assertFalse(any('imgur' in u for u in self.urller))
        self.assertEqual(len(self.atlanan), 1)

    def test_sondan_baslama_sirasi(self):
        self.assertEqual(self.siteler[0]['sayfalar'], [5])

IYI = {'kategori': 'Araçlar', 'ne': 'Bu site, internetteki dosyaları tarayıcı üzerinden başka biçimlere dönüştüren ücretsiz bir araçtır.',
       'yapabilirsin': 'Bu siteyle bir PDF dosyasını program kurmadan Word belgesine çevirebilirsin.'}

class Dogrulama(unittest.TestCase):
    def test_iyi_gecer(self):
        self.assertEqual(k.aciklama_dogrula(IYI), [])

    def test_eksik_cumle(self):
        h = k.aciklama_dogrula(dict(IYI, ne='Dosya dönüştürme aracı, ücretsiz ve hızlı çalışan bir site'))
        self.assertTrue(any('tam cümle' in x for x in h))

    def test_iki_cumle(self):
        h = k.aciklama_dogrula(dict(IYI, ne='Bu bir dönüştürücü sitedir. Ayrıca ücretsiz olarak çalışır ve hızlıdır.'))
        self.assertTrue(any('birden fazla' in x for x in h))

    def test_cok_kisa(self):
        self.assertTrue(k.aciklama_dogrula(dict(IYI, ne='Dönüştürücü.')))

    def test_yapabilirsin_kalibi(self):
        self.assertTrue(k.aciklama_dogrula(dict(IYI, yapabilirsin='PDF çevirirsin.')))

class Uret(unittest.TestCase):
    def test_kategorili_liste_ve_rapor(self):
        siteler = [
            {'anahtar': 'a.com', 'url': 'https://a.com/', 'kontrol': {'durum': 'calisiyor'}},
            {'anahtar': 'b.com', 'url': 'https://b.com/', 'kontrol': {'durum': 'korumali'}},
            {'anahtar': 'olu.com', 'url': 'https://olu.com/', 'kontrol': {'durum': 'olu'}},
            {'anahtar': 'c.com', 'url': 'https://c.com/', 'kontrol': {'durum': 'calisiyor'}},
        ]
        acik = {'https://a.com/': IYI, 'https://b.com/': dict(IYI, kategori='Eğitim')}
        metin, rapor = k.uret(siteler, acik, {'aralik': '809-610'})
        self.assertIn('■ ARAÇLAR  (1 site)', metin)
        self.assertIn('■ EĞİTİM  (1 site)', metin)
        self.assertIn('•  https://a.com/' + k.AYRAC + IYI['ne'] + '  ' + IYI['yapabilirsin'], metin)
        self.assertNotIn('olu.com', metin)
        self.assertNotIn('|', metin)  # tablo değil, liste
        self.assertEqual(rapor['aciklamasi_eksik'], ['https://c.com/'])
        self.assertEqual(len(rapor['calismayan']), 1)
        self.assertIn('aralik: 809-610', metin)

class KomutSatiri(unittest.TestCase):
    def test_uctan_uca(self):
        betik = os.path.join(BURASI, '..', 'araclar', 'katalog_olustur.py')
        with tempfile.TemporaryDirectory() as d:
            s, a, t = (os.path.join(d, x) for x in ('s.json', 'a.json', 'k.txt'))
            subprocess.run([sys.executable, betik, 'tekillestir', FIXTURE, '-o', s], check=True)
            veri = json.load(open(s, encoding='utf-8'))
            for site in veri['siteler']:
                site['kontrol'] = {'durum': 'calisiyor'}
            json.dump(veri, open(s, 'w', encoding='utf-8'))
            subprocess.run([sys.executable, betik, 'sablon', s, '-o', a], check=True)
            sablon = json.load(open(a, encoding='utf-8'))
            eksik = subprocess.run([sys.executable, betik, 'uret', s, a, '-o', t, '--rapor', os.path.join(d, 'r.json')])
            self.assertEqual(eksik.returncode, 1)  # boş şablon reddedilir
            json.dump({u: IYI for u in sablon}, open(a, 'w', encoding='utf-8'))
            tam = subprocess.run([sys.executable, betik, 'uret', s, a, '-o', t, '--rapor', os.path.join(d, 'r.json')])
            self.assertEqual(tam.returncode, 0)
            with open(t, encoding='utf-8') as f:
                self.assertIn('site sayısı: 7', f.read())

if __name__ == '__main__':
    unittest.main(verbosity=2)
