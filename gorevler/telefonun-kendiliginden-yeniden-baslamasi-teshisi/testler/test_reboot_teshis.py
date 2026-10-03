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
       "ozel.pdf", "sentetik/test/test:14",
       # serbest metin sınırları: düz kişi metni, IPv6, Windows/ev yolu, JSON sırrı, Bearer, 3. taraf sınıf
       "Ayşe", "Yılmaz", "notu", "sk-test-123", "api_key", "Bearer", "eyJhbGciOi", "C:\\Users", "Users",
       "ayse", "2001:db8", "fe80", "GizliVeri", "kullanici", "dosya", "sunucu"]
GERCEK_PAKETLER = ["com.ornek.benimuygulamam", "com.ornek.masum", "com.whatsapp"]


# Çocuk süreçler her platformda UTF-8 konuşur; Windows Türkçe kod sayfası (cp1254) varsayılmaz.
UTF8_ORTAM = {"PYTHONUTF8": "1", "PYTHONIOENCODING": "utf-8"}
TUZAK_IZ = "TUZAK_ADB_CALISTI"


def tuzak_adb(t: Path) -> Path:
    """PATH'in başına konan, çalışırsa iz bırakıp 97 ile çıkan tuzak `adb`.
    Sentetik testlerde gerçek (PATH'teki) adb'ye düşülürse önce bu yakalanır."""
    d = t / "tuzak-bin"
    d.mkdir(exist_ok=True)
    iz = t / TUZAK_IZ
    if os.name == "nt":
        for ad in ("adb.bat", "adb.cmd"):
            (d / ad).write_text(f'@echo off\r\necho tuzak>>"{iz}"\r\nexit /b 97\r\n', encoding="utf-8")
    else:
        f = d / "adb"
        f.write_text(f'#!/bin/sh\necho tuzak >> "{iz}"\nexit 97\n', encoding="utf-8")
        f.chmod(f.stat().st_mode | stat.S_IEXEC)
    return d


def ortam(t: Path, senaryo="normal", sahte=True, path=None, **ek) -> dict:
    env = dict(os.environ, **UTF8_ORTAM)
    env.pop(rt.ADB_KOMUTU_ORTAM, None)
    env["PATH"] = path if path is not None else f"{tuzak_adb(t)}{os.pathsep}{env.get('PATH', '')}"
    if sahte:  # ikiz açık Python komutuyla enjekte edilir; uzantısız betik/shebang yok
        env[rt.ADB_KOMUTU_ORTAM] = json.dumps([sys.executable, str(B / "sahte_adb.py")])
    env["SAHTE_ADB_SENARYO"] = senaryo
    env["SAHTE_ADB_IZ"] = str(t / "sahte_iz.txt")
    env.update(ek)
    return env


def py(args, env=None, timeout=120, encoding="utf-8"):
    return subprocess.run([sys.executable, *args], env=env if env is not None else dict(os.environ, **UTF8_ORTAM),
                          capture_output=True, text=True, encoding=encoding, timeout=timeout)


def calistir(t: Path, *args, senaryo="normal"):
    p = py([str(ARAC), *args], ortam(t, senaryo))
    # Güvence: PATH'teki adb hiçbir koşulda çalışmadı.
    assert not (t / TUZAK_IZ).exists(), "sentetik testte PATH'teki adb çalıştırıldı"
    return p


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

    def test_maskele_yeni_bicimler(self):
        m = rt.maskele('{"api_key": "sk-1"} Authorization: Bearer abc.def.ghi C:\\Users\\ali\\x '
                       '/home/ali/y 2001:db8::1 fe80::1ff:fe23:4567:890a saat 22:45:13', uzunluk=400)
        for parca in ["sk-1", "abc.def", "Users", "/home/ali", "2001:db8", "fe80"]:
            self.assertNotIn(parca, m)
        self.assertIn("22:45:13", m)  # zaman damgası IPv6 sanılmaz

    def test_denetim_yeni_bicimleri_yakalar(self):
        ornekler = {"ipv6": "adres 2001:db8::1", "windows/ev dizini yolu": "C:\\Users\\ayse",
                    "json/anahtar sırrı": '{"password": "x"}', "bearer/basic kimlik": "Bearer eyJabcdef.gh"}
        for kategori, metin in ornekler.items():
            self.assertIn(f"r.md: {kategori}", rt.paylasim_denetle({"r.md": metin}), kategori)
        self.assertEqual(rt.paylasim_denetle({"r.md": "| 2026-10-02 22:45:13 | watchdog |"}), [])

    def test_yapisal_siniflar(self):
        self.assertEqual(rt.hata_sinifi_paylasim("java.lang.IllegalStateException"), "java.lang.IllegalStateException")
        self.assertEqual(rt.hata_sinifi_paylasim("com.ornek.GizliException"), "<uygulama-istisnası>")
        self.assertEqual(rt.hata_sinifi_paylasim("android.foo.BilinmeyenException"), "<diğer-platform-istisnası>")
        self.assertEqual(rt.watchdog_kategori("Blocked in handler on main thread (main), dosya /sdcard/x"),
                         "handler-takılması:main")
        self.assertEqual(rt.watchdog_kategori("Blocked in handler on ui thread (Ayşe.thread)"),
                         "handler-takılması:<diğer>")
        self.assertEqual(rt.watchdog_kategori("Blocked in monitor com.android.server.StorageManagerService on "
                                              "foreground thread (android.fg)"),
                         "monitor-takılması:com.android.server.StorageManagerService")
        self.assertEqual(rt.watchdog_kategori("Ali Veli bir şey yazdı"), "<watchdog-diğer>")
        self.assertEqual(rt.sinyal_kategori("signal 11 (SIGSEGV), code 1 fault addr 0x0"), "sinyal:SIGSEGV")
        self.assertEqual(rt.sinyal_kategori("Abort message: 'Ayşe'nin dosyası'"), "<abort-mesajı>")

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
        with tempfile.TemporaryDirectory() as t:
            bos = Path(t) / "bos-path"
            bos.mkdir()
            p = py([str(ARAC), "hepsi", "--cikti", f"{t}/k"], ortam(Path(t), sahte=False, path=str(bos)))
            self.assertEqual(p.returncode, 2)
            self.assertIn("'adb' bulunamadı", p.stderr)
            self.assertFalse((Path(t) / "k").exists())


class AdbEnjeksiyonu(unittest.TestCase):
    """Sentetik testler gerçek adb'ye hiçbir koşulda düşmemeli."""

    def test_sahte_ikiz_kullanildi_tuzak_calismadi(self):
        with tempfile.TemporaryDirectory() as t:
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k")
            self.assertEqual(p.returncode, 0, p.stderr)
            iz = (Path(t) / "sahte_iz.txt").read_text(encoding="utf-8").splitlines()
            self.assertEqual(iz[0], "devices -l")
            self.assertTrue(any(x.startswith("-s SENTETIK0001 shell getprop") for x in iz))
            self.assertFalse((Path(t) / TUZAK_IZ).exists())

    def test_gecersiz_enjeksiyon_gercek_adbye_dusmez(self):
        for deger in ["json-degil", "[]", '["", "x"]', '{"a": 1}', '[1, 2]']:
            with self.subTest(deger=deger), tempfile.TemporaryDirectory() as t:
                p = py([str(ARAC), "hepsi", "--cikti", f"{t}/k"],
                       ortam(Path(t), sahte=False, **{rt.ADB_KOMUTU_ORTAM: deger}))
                self.assertEqual(p.returncode, 2)
                self.assertIn("gerçek adb'ye düşülmedi", p.stderr)
                self.assertFalse((Path(t) / TUZAK_IZ).exists())
                self.assertFalse((Path(t) / "k").exists())

    def test_olmayan_program_enjeksiyonu_gercek_adbye_dusmez(self):
        with tempfile.TemporaryDirectory() as t:
            yok = str(Path(t) / "yok" / "adb-yok.exe")
            p = py([str(ARAC), "hepsi", "--cikti", f"{t}/k"],
                   ortam(Path(t), sahte=False, **{rt.ADB_KOMUTU_ORTAM: json.dumps([yok])}))
            self.assertEqual(p.returncode, 2)
            self.assertIn("`adb devices -l` başarısız", p.stderr)
            self.assertFalse((Path(t) / TUZAK_IZ).exists())

    def test_tuzak_mekanizmasi_gercekten_yakalar(self):
        # Enjeksiyon YOKKEN araç PATH'teki adb'yi kullanır → tuzak çalışmalı (tuzağın kendisinin sınaması).
        with tempfile.TemporaryDirectory() as t:
            p = py([str(ARAC), "hepsi", "--cikti", f"{t}/k"], ortam(Path(t), sahte=False))
            self.assertEqual(p.returncode, 2)
            self.assertTrue((Path(t) / TUZAK_IZ).exists())

    def test_adb_komutu_birim(self):
        with mock.patch.dict(os.environ, {rt.ADB_KOMUTU_ORTAM: '["py", "x.py"]'}):
            self.assertEqual(rt.adb_komutu(), ["py", "x.py"])
        with mock.patch.dict(os.environ, {rt.ADB_KOMUTU_ORTAM: "adb"}):
            with self.assertRaises(ValueError):
                rt.adb_komutu()


class KodlamaUTF8(unittest.TestCase):
    def test_varsayilan_kodlama_kullanilmaz(self):
        # PEP 597: açık encoding verilmeyen her open/read_text/subprocess(text) EncodingWarning → hata.
        with tempfile.TemporaryDirectory() as t:
            env = ortam(Path(t), PYTHONWARNDEFAULTENCODING="1", PYTHONWARNINGS="error::EncodingWarning")
            for args in (["hepsi", "--cikti", f"{t}/k", "--ham-sakla"],
                         ["isle", f"{t}/k/yerel-ham", "--cikti", f"{t}/k2"],
                         ["denetle", f"{t}/k/paylasim.zip"]):
                p = py([str(ARAC), *args], env)
                self.assertEqual(p.returncode, 0, f"{args[0]}: {p.stderr}")
                self.assertNotIn("EncodingWarning", p.stderr)

    def test_turkce_kod_sayfasi_surecte_cihaz_ciktisi_utf8(self):
        # Windows Türkçe boru kod sayfası benzetimi: aracın stdout'u cp1254, PYTHONUTF8 kapalı.
        with tempfile.TemporaryDirectory() as t:
            env = ortam(Path(t), PYTHONUTF8="0", PYTHONIOENCODING="cp1254")
            p = py([str(ARAC), "hepsi", "--cikti", f"{t}/k"], env, encoding="cp1254")
            self.assertEqual(p.returncode, 0, p.stderr)
            self.assertIn("kişisel veri içerir", p.stdout)
            yerel = (Path(t) / "k/rapor-yerel.md").read_text(encoding="utf-8")
            self.assertIn("Ayşe Yılmaz", yerel)  # ikizin UTF-8 baytları doğru çözüldü
            self.assertNotIn("\ufffd", yerel)


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

    def test_paylasimda_serbest_metin_yok_yalniz_yapisal_sinif(self):
        metin = paylasim_metni(self.k)
        oz = json.loads(zipfile.ZipFile(self.k / "paylasim.zip").read("paylasim/ozet-paylasim.json"))
        for o in oz["cokme_ozetleri"]:
            self.assertEqual(sorted(o), ["etiket", "ipucu", "paketler", "zaman"])
        self.assertTrue(all(sorted(w) == ["kategori", "zaman"] for w in oz["watchdog"]))
        self.assertTrue(all("hata" not in t for t in oz["toplama"]))
        self.assertIn("system_server_crash: java.lang.IllegalArgumentException", metin)
        self.assertIn("system_server_crash: <uygulama-istisnası>", metin)
        self.assertIn("system_server_watchdog: handler-takılması:main", metin)
        self.assertNotIn("<e-posta>", metin)  # maske etiketi bile yok: mesaj hiç taşınmıyor

    def test_yerel_ozet_de_ham_icerik_tutmaz(self):
        yerel = (self.k / "ozet-yerel.json").read_text(encoding="utf-8") + (self.k / "rapor-yerel.md").read_text(encoding="utf-8")
        for parca in ["ali.veli@example.com", "356938035643809", "192.168.1.20", "SENTETIK0001",
                      "a1b2c3d4e5f60718", "/storage/emulated", "at com.ornek.masum.Foo", "am_proc_start",
                      "userId=10150"]:
            self.assertNotIn(parca, yerel, parca)
        self.assertIn("com.ornek.benimuygulamam", yerel)  # yerel raporda gerçek ad kalır

    def test_masum_paket_adi_neden_ilan_edilmez(self):
        r = (self.k / "rapor-yerel.md").read_text(encoding="utf-8")
        self.assertNotIn("YÜKSEK", r)
        self.assertIn("belirsiz hipotez", r)
        self.assertIn("nedensellik değildir", r)
        self.assertIn("çökme kaydında geçen paket(ler): `com.ornek.masum`", r)
        self.assertNotRegex(r, r"(?i)kesin(?!lik| değil)(?! neden ilan edilemez)")

    def test_zaman_penceresi(self):
        oz = json.loads((self.k / "ozet-yerel.json").read_text(encoding="utf-8"))
        self.assertNotIn("2026-09-01 08:00:00", json.dumps(oz))
        self.assertNotIn("ESKI-PENCERE-DISI", json.dumps(oz))
        self.assertEqual(oz["cerceve_baslangiclari"], ["2026-10-02 22:40:00", "2026-10-02 22:45:30"])
        # En erken hata: events Watchdog (22:44:40) < dropbox watchdog (22:44:50) < crash tamponu (22:45:12)
        self.assertEqual(oz["crash_tamponu"]["ilk_hata"], "2026-10-02 22:44:40")

    def test_yan_yukleme_zaman_eslesmesi(self):
        r = (self.k / "rapor-yerel.md").read_text(encoding="utf-8")
        self.assertIn("| 2026-10-02 22:45:13 | `com.ornek.benimuygulamam` |", r)
        self.assertNotIn("`com.whatsapp`", r.split("## 6.")[1].split("## 7.")[0])

    def test_sha_dosyasi_dogru(self):
        import hashlib
        sha = (self.k / "paylasim.zip.sha256").read_text(encoding="utf-8").split()[0]
        self.assertEqual(sha, hashlib.sha256((self.k / "paylasim.zip").read_bytes()).hexdigest())
        self.assertEqual(len(sha), 64)


class EksikVeri(unittest.TestCase):
    def test_izin_hatasi_yapisal_ve_arizasizlik_iddiasi_yok(self):
        with tempfile.TemporaryDirectory() as t:
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k", senaryo="izin")
            self.assertEqual(p.returncode, 0, p.stderr)
            oz = json.loads((Path(t) / "k/ozet-yerel.json").read_text(encoding="utf-8"))
            izinli = {s["ad"] for s in oz["toplama"] if s["izin_hatasi"]}
            self.assertIn("dropbox_liste.txt", izinli)
            self.assertIn("paketler_3.txt", izinli)
            self.assertTrue(all(s["cikis_kodu"] == 255 for s in oz["toplama"] if s["ad"] == "dropbox_liste.txt"))
            r = (Path(t) / "k/rapor-yerel.md").read_text(encoding="utf-8")
            self.assertIn("Eksik veri, ilgili arızanın olmadığı anlamına gelmez", r)
            self.assertIn("| dropbox_liste.txt | izin yok |", r)
            self.assertIn("Uygulama listesi/kurulum zamanları toplanamadı", r)
            self.assertNotIn("pencerede kayıt yok", r)
            self.assertIn("| logcat_onceki_acilis.txt | çıkış kodu 1 |", r)

    def test_zaman_asimi_yapisal_ve_devam(self):
        with tempfile.TemporaryDirectory() as t:
            p = calistir(Path(t), "hepsi", "--cikti", f"{t}/k", "--zaman-asimi", "3", senaryo="zamanasimi")
            self.assertEqual(p.returncode, 0, p.stderr)
            oz = json.loads((Path(t) / "k/ozet-yerel.json").read_text(encoding="utf-8"))
            s = [x for x in oz["toplama"] if x["ad"] == "logcat_crash.txt"][0]
            self.assertTrue(s["zaman_asimi"])
            self.assertIsNone(s["cikis_kodu"])
            r = (Path(t) / "k/rapor-yerel.md").read_text(encoding="utf-8")
            self.assertIn("| logcat_crash.txt | zaman aşımı | crash tamponu |", r)
            # crash tamponu yokken ilk hata diğer kaynaklardan (events/dropbox) bulunur
            self.assertIn("İlk hata zamanı: 2026-10-02 22:44:40", r)


class GuvenliModVeSizinti(unittest.TestCase):
    def _oz(self):
        with tempfile.TemporaryDirectory() as t:
            calistir(Path(t), "hepsi", "--cikti", f"{t}/k")
            return json.loads((Path(t) / "k/ozet-yerel.json").read_text(encoding="utf-8"))

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
            p2 = py([str(ARAC), "isle", f"{t}/k/yerel-ham", "--cikti", f"{t}/k2"])
            self.assertEqual(p2.returncode, 0, p2.stderr)
            self.assertIn("SHA-256", p2.stdout)

    def test_denetle_komutu(self):
        with tempfile.TemporaryDirectory() as t:
            calistir(Path(t), "hepsi", "--cikti", f"{t}/k")
            p = py([str(ARAC), "denetle", f"{t}/k/paylasim.zip"])
            self.assertEqual(p.returncode, 0)
            self.assertIn("Tanımlı örüntüler bulunmadı", p.stdout)
            self.assertIn("garantisi değildir", p.stdout)
            self.assertNotIn("TEMİZ", p.stdout)


class EskiPaylasimArtefaktlari(unittest.TestCase):
    """Önceki başarılı paket dururken yeni çalıştırma başarısız olursa eski paket teslim gibi kalmamalı."""

    def _onceki_basarili(self, t: Path) -> Path:
        p = calistir(t, "hepsi", "--cikti", str(t / "k"))
        self.assertEqual(p.returncode, 0, p.stderr)
        k = t / "k"
        for ad in rt.PAYLASIM_ARTEFAKTLARI:
            self.assertTrue((k / ad).exists(), ad)
        return k

    def _hic_paylasim_yok(self, k: Path):
        for ad in rt.PAYLASIM_ARTEFAKTLARI:
            self.assertFalse((k / ad).exists(), ad)
        self.assertEqual(list(k.glob(rt.GECICI_ONEK + "*")), [])

    def test_sizinti_enjekte_eski_zip_sha_silinir(self):
        with tempfile.TemporaryDirectory() as t:
            k = self._onceki_basarili(Path(t))
            oz = json.loads((k / "ozet-yerel.json").read_text(encoding="utf-8"))
            oz["dropbox_olaylari"][0]["etiket"] = "sizinti ali@ornek.co"
            z, bulgular = rt.ciktilari_yaz(oz, k, "bilinmiyor", [], ["SENTETIK0001"])
            self.assertIsNone(z)
            self.assertTrue(any("e-posta" in b for b in bulgular))
            self._hic_paylasim_yok(k)
            self.assertTrue((k / "rapor-yerel.md").exists())

    def test_cli_basarisizlikta_cikis_4_ve_eski_paket_yok(self):
        with tempfile.TemporaryDirectory() as t:
            k = self._onceki_basarili(Path(t))
            # Cihazsız CLI yolu (isle) için ham klasör hazırla, sonra denetime bulgu enjekte et.
            calistir(Path(t), "hepsi", "--cikti", str(Path(t) / "h"), "--ham-sakla")
            with mock.patch.object(rt, "paylasim_denetle", return_value=["x: sahte bulgu"]):
                kod = rt.main(["isle", str(Path(t) / "h" / "yerel-ham"), "--cikti", str(k)])
            self.assertEqual(kod, 4)
            self._hic_paylasim_yok(k)

    def test_yarida_kesilmede_artefakt_kalmaz(self):
        with tempfile.TemporaryDirectory() as t:
            k = self._onceki_basarili(Path(t))
            oz = json.loads((k / "ozet-yerel.json").read_text(encoding="utf-8"))
            with mock.patch.object(rt, "_zip_yaz", side_effect=OSError("disk dolu")):
                with self.assertRaises(OSError):
                    rt.ciktilari_yaz(oz, k, "bilinmiyor", [], ["SENTETIK0001"])
            self._hic_paylasim_yok(k)

    def test_yeniden_calistirma_yeni_paketi_tutarli_yazar(self):
        import hashlib
        with tempfile.TemporaryDirectory() as t:
            k = self._onceki_basarili(Path(t))
            eski = (k / "paylasim.zip.sha256").read_text(encoding="utf-8")
            p = calistir(Path(t), "hepsi", "--cikti", str(k), "--guvenli-mod", "kapandi")
            self.assertEqual(p.returncode, 0, p.stderr)
            yeni = (k / "paylasim.zip.sha256").read_text(encoding="utf-8").split()[0]
            self.assertNotEqual(eski.split()[0], yeni)
            self.assertEqual(yeni, hashlib.sha256((k / "paylasim.zip").read_bytes()).hexdigest())
            self.assertEqual(list(k.glob(rt.GECICI_ONEK + "*")), [])
            self.assertIn("kapandı", paylasim_metni(k))


if __name__ == "__main__":
    unittest.main(verbosity=2)
