"""Birim + uçtan uca testler. Tüm veriler SENTETİK'tir; gerçek cihaz kanıtı değildir.

Çalıştırma: python3 testler/test_reboot_teshis.py
"""
import json
import os
import stat
import subprocess
import sys
import tempfile
import unittest
import zipfile
from pathlib import Path
from unittest import mock

B = Path(__file__).resolve().parent
ARAC = B.parent / "arac" / "reboot_teshis.py"
sys.path.insert(0, str(ARAC.parent))
import reboot_teshis as rt  # noqa: E402

PII = ["ali.veli@example.com", "555 123 45 67", "356938035643809", "/storage/emulated",
       "IMG_0001", "192.168.1.20", "SENTETIK0001", "a1b2c3d4e5f60718", "/sdcard/Download",
       "ozel.pdf", "sentetik/test/test:14"]
GERCEK_PAKETLER = ["com.ornek.benimuygulamam", "com.ornek.masum", "com.whatsapp"]


def calistir(t: Path, *args, senaryo="normal"):
    bin_ = t / "bin"
    bin_.mkdir(exist_ok=True)
    shim = bin_ / "adb"
    shim.write_text(f"#!/bin/sh\nexec {sys.executable} {B / 'sahte_adb.py'} \"$@\"\n")
    shim.chmod(shim.stat().st_mode | stat.S_IEXEC)
    env = dict(os.environ, PATH=f"{bin_}{os.pathsep}{os.environ['PATH']}", SAHTE_ADB_SENARYO=senaryo)
    return subprocess.run([sys.executable, str(ARAC), *args], env=env, capture_output=True, text=True,
                          timeout=120)


def paylasim_metni(cikti: Path) -> str:
    with zipfile.ZipFile(cikti / "paylasim.zip") as zf:
        return "\n".join(zf.read(n).decode("utf-8") for n in zf.namelist())


class Siniflama(unittest.TestCase):
    def test_boot_nedenleri(self):
        beklenen = {"kernel_panic": "cekirdek", "watchdog,sec": "cekirdek", "shutdown,battery": "guc",
                    "reboot,undervoltage": "guc", "shutdown,thermal,battery": "isi",
                    "reboot,userrequested": "kullanici", "reboot,shell": "kullanici", "reboot,ota": "kullanici",
                    "reboot,rescueparty": "yazilim", "cold": "belirsiz", "reboot": "belirsiz", "": "belirsiz"}
        for neden, sinif in beklenen.items():
            self.assertEqual(rt.boot_nedeni_sinifla(neden), sinif, neden)


class Maskeleme(unittest.TestCase):
    def test_maskele_kisisel_bicimleri_kaldirir(self):
        m = rt.maskele("a ali.veli@example.com +90 555 123 45 67 /storage/emulated/0/x.jpg 10.0.0.5 "
                       "imei=356938035643809 https://ornek.test/p?q=1 SER123", ["SER123"])
        for parca in ["ali.veli", "555 123", "/storage", "10.0.0.5", "356938035643809", "ornek.test", "SER123"]:
            self.assertNotIn(parca, m)

    def test_takma_ad_kararli_sistem_acik(self):
        t = rt.TakmaAd(goster=["com.benim.app"], tuz="sabit")
        self.assertEqual(t.ad("com.ornek.x"), t.ad("com.ornek.x"))
        self.assertTrue(t.ad("com.ornek.x").startswith("uyg-"))
        self.assertEqual(t.ad("com.android.systemui"), "com.android.systemui")
        self.assertEqual(t.ad("com.benim.app"), "com.benim.app")
        self.assertNotIn("com.ornek", t.metin("çağıran com.ornek.x.ui.Main"))

    def test_denetim_sizintiyi_yakalar(self):
        b = rt.paylasim_denetle({"r.md": "iletişim a@b.co ve com.gizli.uygulama"}, ["SER9"])
        self.assertIn("r.md: e-posta", b)
        self.assertIn("r.md: maskelenmemiş noktalı ad", b)
        self.assertEqual(rt.paylasim_denetle({"r.md": "seri SER9"}, ["SER9"]), ["r.md: özel değer (seri/paket)"])
        self.assertEqual(rt.paylasim_denetle({"r.md": "`sys.boot.reason` java.lang.X uyg-1a2b3c4d"}), [])


class Seri(unittest.TestCase):
    def test_cihaz_sec(self):
        liste = rt.cihazlari_ayristir("List of devices attached\nA1 device usb:1\nB2 unauthorized usb:2\n"
                                      "C3 no permissions (user in plugdev)\n")
        self.assertEqual([c["durum"] for c in liste], ["device", "unauthorized", "no permissions"])
        self.assertEqual(rt.cihaz_sec(liste, None), "A1")
        self.assertEqual(rt.cihaz_sec(liste, "A1"), "A1")
        with self.assertRaisesRegex(ValueError, "listesinde yok"):
            rt.cihaz_sec(liste, "ZZ")
        with self.assertRaisesRegex(ValueError, "hazır değil"):
            rt.cihaz_sec(liste, "B2")
        with self.assertRaisesRegex(ValueError, "boş"):
            rt.cihaz_sec([], None)

    def test_cli_seri_listede_yoksa_ilerlemez(self):
        with tempfile.TemporaryDirectory() as t:
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k", "--seri", "YOKSERI")
            self.assertEqual(p.returncode, 2)
            self.assertIn("listesinde yok", p.stderr)
            self.assertFalse((Path(t) / "k").exists())
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k", "--seri", "SENTETIK0002")
            self.assertEqual(p.returncode, 2)
            self.assertIn("unauthorized", p.stderr)
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k", senaryo="bos")
            self.assertEqual(p.returncode, 2)
            self.assertIn("boş", p.stderr)
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k", senaryo="coklu")
            self.assertEqual(p.returncode, 2)
            self.assertIn("--seri", p.stderr)

    def test_adb_yoksa_anlasilir_hata(self):
        env = dict(os.environ, PATH="/nonexistent")
        p = subprocess.run([sys.executable, str(ARAC), "hepsi"], env=env, capture_output=True, text=True)
        self.assertEqual(p.returncode, 2)
        self.assertIn("adb", p.stderr)


class UctanUca(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.tmp = tempfile.TemporaryDirectory()
        cls.t = Path(cls.tmp.name)
        cls.p = calistir(cls.t, "hepsi", "--cikti", str(cls.t / "k"))
        cls.k = cls.t / "k"

    @classmethod
    def tearDownClass(cls):
        cls.tmp.cleanup()

    def test_basarili_ve_ham_yazilmaz(self):
        self.assertEqual(self.p.returncode, 0, self.p.stderr)
        dosyalar = sorted(x.name for x in self.k.iterdir())
        self.assertEqual(dosyalar, ["ozet-yerel.json", "paylasim", "paylasim.zip", "paylasim.zip.sha256",
                                    "rapor-yerel.md"])
        with zipfile.ZipFile(self.k / "paylasim.zip") as zf:
            self.assertEqual(sorted(zf.namelist()), ["paylasim/ozet-paylasim.json", "paylasim/rapor-paylasim.md"])

    def test_paylasimda_pii_ve_gercek_paket_yok(self):
        metin = paylasim_metni(self.k)
        for parca in PII + GERCEK_PAKETLER:
            self.assertNotIn(parca, metin, parca)
        self.assertIn("uyg-", metin)
        self.assertIn("<e-posta>", metin)

    def test_yerel_ozet_de_ham_icerik_tutmaz(self):
        yerel = (self.k / "ozet-yerel.json").read_text() + (self.k / "rapor-yerel.md").read_text()
        for parca in ["ali.veli@example.com", "356938035643809", "192.168.1.20", "SENTETIK0001",
                      "a1b2c3d4e5f60718", "/storage/emulated", "at com.ornek.masum.Foo", "am_proc_start",
                      "userId=10150"]:
            self.assertNotIn(parca, yerel, parca)
        self.assertIn("com.ornek.benimuygulamam", yerel)  # yerel raporda gerçek ad kalır

    def test_masum_paket_adi_neden_ilan_edilmez(self):
        r = (self.k / "rapor-yerel.md").read_text()
        self.assertNotIn("YÜKSEK", r)
        self.assertIn("belirsiz hipotez", r)
        self.assertIn("nedensellik değildir", r)
        self.assertIn("çökme kaydında geçen paket(ler): `com.ornek.masum`", r)
        self.assertNotRegex(r, r"(?i)kesin(?!lik| değil)(?! neden ilan edilemez)")

    def test_zaman_penceresi(self):
        oz = json.loads((self.k / "ozet-yerel.json").read_text())
        self.assertNotIn("2026-09-01 08:00:00", json.dumps(oz))
        self.assertNotIn("ESKI-PENCERE-DISI", json.dumps(oz))
        self.assertEqual(oz["cerceve_baslangiclari"], ["2026-10-02 22:40:00", "2026-10-02 22:45:30"])
        # En erken hata: events Watchdog (22:44:40) < dropbox watchdog (22:44:50) < crash tamponu (22:45:12)
        self.assertEqual(oz["crash_tamponu"]["ilk_hata"], "2026-10-02 22:44:40")

    def test_yan_yukleme_zaman_eslesmesi(self):
        r = (self.k / "rapor-yerel.md").read_text()
        self.assertIn("| 2026-10-02 22:45:13 | `com.ornek.benimuygulamam` |", r)
        self.assertNotIn("`com.whatsapp`", r.split("## 6.")[1].split("## 7.")[0])

    def test_sha_dosyasi_dogru(self):
        import hashlib
        sha = (self.k / "paylasim.zip.sha256").read_text().split()[0]
        self.assertEqual(sha, hashlib.sha256((self.k / "paylasim.zip").read_bytes()).hexdigest())
        self.assertEqual(len(sha), 64)


class EksikVeri(unittest.TestCase):
    def test_izin_hatasi_yapisal_ve_arizasizlik_iddiasi_yok(self):
        with tempfile.TemporaryDirectory() as t:
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k", senaryo="izin")
            self.assertEqual(p.returncode, 0, p.stderr)
            oz = json.loads((Path(t) / "k/ozet-yerel.json").read_text())
            izinli = {s["ad"] for s in oz["toplama"] if s["izin_hatasi"]}
            self.assertIn("dropbox_liste.txt", izinli)
            self.assertIn("paketler_3.txt", izinli)
            self.assertTrue(all(s["cikis_kodu"] == 255 for s in oz["toplama"] if s["ad"] == "dropbox_liste.txt"))
            r = (Path(t) / "k/rapor-yerel.md").read_text()
            self.assertIn("Eksik veri, ilgili arızanın olmadığı anlamına gelmez", r)
            self.assertIn("| dropbox_liste.txt | izin yok |", r)
            self.assertIn("Uygulama listesi/kurulum zamanları toplanamadı", r)
            self.assertNotIn("pencerede kayıt yok", r)
            self.assertIn("| logcat_onceki_acilis.txt | çıkış kodu 1 |", r)

    def test_zaman_asimi_yapisal_ve_devam(self):
        with tempfile.TemporaryDirectory() as t:
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k", "--zaman-asimi", "3", senaryo="zamanasimi")
            self.assertEqual(p.returncode, 0, p.stderr)
            oz = json.loads((Path(t) / "k/ozet-yerel.json").read_text())
            s = [x for x in oz["toplama"] if x["ad"] == "logcat_crash.txt"][0]
            self.assertTrue(s["zaman_asimi"])
            self.assertIsNone(s["cikis_kodu"])
            r = (Path(t) / "k/rapor-yerel.md").read_text()
            self.assertIn("| logcat_crash.txt | zaman aşımı | crash tamponu |", r)
            # crash tamponu yokken ilk hata diğer kaynaklardan (events/dropbox) bulunur
            self.assertIn("İlk hata zamanı: 2026-10-02 22:44:40", r)


class GuvenliModVeSizinti(unittest.TestCase):
    def _oz(self):
        with tempfile.TemporaryDirectory() as t:
            calistir(Path(t), "hepsi", "--cikti", f"{t}/k")
            return json.loads((Path(t) / "k/ozet-yerel.json").read_text())

    def test_guvenli_mod_olasilik_dili(self):
        oz = self._oz()
        d = rt.degerlendir(oz, "kapandi")
        self.assertIn("olası", d["guvenli_mod"])
        self.assertIn("firmware/sürücü", d["guvenli_mod"])
        self.assertTrue(d["duzey"].startswith("olası"))
        self.assertIn("kesin değil", d["duzey"])
        d = rt.degerlendir(oz, "kapanmadi")
        self.assertIn("**olası** (kesin değil", d["guvenli_mod"])
        self.assertIn("fabrika ayarı firmware'i değiştirmez", rt.SONRAKI_TESTLER)
        self.assertNotIn("neredeyse kesin", rt.SONRAKI_TESTLER)

    def test_goster_ile_kendi_paketi_acik(self):
        with tempfile.TemporaryDirectory() as t:
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k", "--goster", "com.ornek.benimuygulamam")
            self.assertEqual(p.returncode, 0, p.stderr)
            metin = paylasim_metni(Path(t) / "k")
            self.assertIn("com.ornek.benimuygulamam", metin)
            self.assertNotIn("com.ornek.masum", metin)

    def test_maskeleme_bozulursa_paket_uretilmez(self):
        oz = self._oz()
        # Takma ad/maskeleme uygulanmayan bir alana sızıntı enjekte et: son savunma hattı denetimdir.
        oz["dropbox_olaylari"][0]["etiket"] = "sizinti ali@ornek.co"
        with tempfile.TemporaryDirectory() as t:
            z, bulgular = rt.ciktilari_yaz(oz, Path(t) / "k", "bilinmiyor", [], ["SENTETIK0001"])
            self.assertIsNone(z)
            self.assertTrue(any("e-posta" in b for b in bulgular))
            self.assertFalse((Path(t) / "k/paylasim").exists())
            self.assertFalse((Path(t) / "k/paylasim.zip").exists())

    def test_ham_sakla_paylasima_girmez(self):
        with tempfile.TemporaryDirectory() as t:
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k", "--ham-sakla")
            self.assertEqual(p.returncode, 0, p.stderr)
            self.assertTrue((Path(t) / "k/yerel-ham/getprop.txt").exists())
            self.assertIn("PAYLAŞMAYIN", p.stdout)
            with zipfile.ZipFile(Path(t) / "k/paylasim.zip") as zf:
                self.assertFalse(any("yerel-ham" in n for n in zf.namelist()))
            # isle: ham klasörden aynı özeti yeniden üretir (cihaz gerekmeden)
            p2 = subprocess.run([sys.executable, str(ARAC), "isle", f"{t}/k/yerel-ham", "--cikti", f"{t}/k2"],
                                capture_output=True, text=True)
            self.assertEqual(p2.returncode, 0, p2.stderr)
            self.assertIn("SHA-256", p2.stdout)

    def test_denetle_komutu(self):
        with tempfile.TemporaryDirectory() as t:
            calistir(Path(t), "hepsi", "--cikti", f"{t}/k")
            p = subprocess.run([sys.executable, str(ARAC), "denetle", f"{t}/k/paylasim.zip"],
                               capture_output=True, text=True)
            self.assertEqual(p.returncode, 0)
            self.assertIn("DENETİM TEMİZ", p.stdout)


if __name__ == "__main__":
    unittest.main(verbosity=2)
