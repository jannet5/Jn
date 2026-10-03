"""drive_denetim.py birim testleri: python3 -m unittest -v test_drive_denetim"""
import io
import json
import os
import tempfile
import unittest
from contextlib import redirect_stderr, redirect_stdout
from pathlib import Path

import drive_denetim as dd


def _yok(_):
    return False


def _birim_yok(_):
    return None, None


class YolTestleri(unittest.TestCase):
    def test_g_my_drive_drive_olarak_isaretlenir(self):
        r = dd.yol_siniflandir(r"G:\My Drive\isler\gorsel.png", var_mi=_yok, birim_bilgisi=_birim_yok)
        self.assertTrue(r.windows_bicimi)
        self.assertEqual(r.surucu, "G:")
        self.assertTrue(r.drive_sanal_surucu_olasi)
        self.assertFalse(r.bu_makinede_var)
        self.assertIn("kendiliğinden açamaz", r.web_ai_erisimi)
        self.assertIn("Drive for desktop çalışıyor", r.yerel_arac_erisimi)

    def test_turkce_drive_im_ve_etiket(self):
        r = dd.yol_siniflandir(r"H:\Drive'ım\video.mp4", var_mi=_yok,
                               birim_bilgisi=lambda k: ("Google Drive", "FAT32"))
        self.assertTrue(r.drive_sanal_surucu_olasi)
        self.assertTrue(any("birim etiketi" in i for i in r.drive_isareti))

    def test_yerel_c_yolu_drive_degil(self):
        r = dd.yol_siniflandir(r"C:\dev\proje\app.py", var_mi=_yok, birim_bilgisi=_birim_yok)
        self.assertFalse(r.drive_sanal_surucu_olasi)
        self.assertIn("yerel disk hızında", r.yerel_arac_erisimi)
        # Web AI notu her durumda geçerli: yerel yol da web AI'ya görünmez.
        self.assertIn("kendiliğinden açamaz", r.web_ai_erisimi)

    def test_macos_cloudstorage(self):
        r = dd.yol_siniflandir("/Users/m/Library/CloudStorage/GoogleDrive-a@b.com/My Drive/x.zip",
                               var_mi=_yok, birim_bilgisi=_birim_yok)
        self.assertTrue(r.drive_sanal_surucu_olasi)
        self.assertFalse(r.windows_bicimi)


    def test_gdoc_kisayol_dosyasi(self):
        r = dd.yol_siniflandir(r"G:\My Drive\notlar.gdoc", var_mi=_yok, birim_bilgisi=_birim_yok)
        self.assertTrue(any("Google Docs/Sheets/Slides dosyası" in i for i in r.drive_isareti))

    def test_web_ai_notu_kosullari_ayirir(self):
        n = dd.WEB_AI_NOTU
        for parca in ("yüklemek", "OAuth", "yerel ajan", "herkese açık link", "sandbox"):
            self.assertIn(parca, n)


class TaramaTestleri(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        k = Path(self.tmp.name)
        (k / "web" / "node_modules" / "lodash").mkdir(parents=True)
        (k / "web" / "node_modules" / "lodash" / "index.js").write_bytes(b"x" * 100)
        (k / "web" / "node_modules" / "a" / "node_modules" / "b").mkdir(parents=True)
        (k / "web" / "node_modules" / "a" / "node_modules" / "b" / "i.js").write_bytes(b"y" * 10)
        (k / "android" / ".gradle" / "8.9").mkdir(parents=True)
        (k / "android" / ".gradle" / "8.9" / "fileHashes.lock").write_bytes(b"\0" * 17)
        (k / "android" / "app" / "build").mkdir(parents=True)
        (k / "android" / "app" / "build" / "app.apk").write_bytes(b"z" * 1000)
        (k / "arsiv").mkdir()
        (k / "arsiv" / "foto.jpg").write_bytes(b"j" * 50)
        self.kok = k

    def tearDown(self):
        self.tmp.cleanup()

    def test_aktif_klasorler_bulunur_ve_ic_ice_sayilmaz(self):
        r = dd.tara(str(self.kok), drive_icinde=True)
        adlar = sorted(k.ad for k in r.bulunan)
        self.assertEqual(adlar, [".gradle", "build", "node_modules"])  # iç node_modules ayrı sayılmaz
        nm = next(k for k in r.bulunan if k.ad == "node_modules")
        self.assertEqual(nm.dosya_sayisi, 2)
        self.assertEqual(nm.bayt, 110)
        self.assertEqual(r.toplam_dosya, 4)
        self.assertTrue(r.risk.startswith("YÜKSEK"))

    def test_yerel_kok_risk_dusuk(self):
        r = dd.tara(str(self.kok), drive_icinde=False)
        self.assertTrue(r.risk.startswith("Yerel diskte"))

    def test_temiz_arsiv(self):
        r = dd.tara(str(self.kok / "arsiv"), drive_icinde=True)
        self.assertEqual(r.bulunan, [])
        self.assertIn("arşiv/depolama için uygun", r.risk)

    def test_cli_json(self):
        buf = io.StringIO()
        with redirect_stdout(buf):
            kod = dd.main(["--json", "tara", str(self.kok), "--drive", "evet"])
        self.assertEqual(kod, 0)
        veri = json.loads(buf.getvalue())
        self.assertEqual(len(veri["bulunan"]), 3)

    def test_cli_olmayan_klasor(self):
        self.assertEqual(dd.main(["tara", str(self.kok / "yok")]), 2)


class OnbellekVeOlcumTestleri(unittest.TestCase):
    def test_windows_onbellek_yolu(self):
        p = dd.onbellek_yolu(env={"LOCALAPPDATA": r"C:\Users\Kullanici\AppData\Local"}, platform="win32")
        self.assertEqual(p.parts[-2:], ("Google", "DriveFS"))

    def test_linux_desteklenmez(self):
        self.assertIsNone(dd.onbellek_yolu(env={}, platform="linux"))
        if dd.onbellek_yolu() is None:  # bu makine Windows/macOS değilse
            self.assertIn("desteklenmeyen", dd.onbellek_raporu()["durum"])

    def test_onbellek_olcumu(self):
        with tempfile.TemporaryDirectory() as t:
            (Path(t) / "content_cache").mkdir()
            (Path(t) / "content_cache" / "blob").write_bytes(b"c" * 2048)
            r = dd.onbellek_raporu(Path(t))
            self.assertEqual(r["bayt"], 2048)
            self.assertEqual(r["insan"], "2.0 KB")

    def test_olc(self):
        with tempfile.TemporaryDirectory() as t:
            f = Path(t) / "veri.bin"
            f.write_bytes(os.urandom(256 * 1024))
            r = dd.olc(str(f), rastgele_okuma=8)
            self.assertEqual(r["bayt"], 256 * 1024)
            self.assertGreater(r["sirali_MBps"], 0)
            self.assertEqual(r["rastgele_okuma_sayisi"], 8)


class GecersizGirdiTestleri(unittest.TestCase):
    """--rastgele için geçersiz sayılar traceback değil, argparse kullanım hatası (çıkış 2) vermeli."""

    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.dosya = Path(self.tmp.name) / "v.bin"
        self.dosya.write_bytes(b"a" * 8192)

    def tearDown(self):
        self.tmp.cleanup()

    def _cli(self, *arglar):
        hata, cikti = io.StringIO(), io.StringIO()
        with redirect_stderr(hata), redirect_stdout(cikti):
            try:
                kod = dd.main(list(arglar))
            except SystemExit as e:
                kod = e.code
        return kod, hata.getvalue(), cikti.getvalue()

    def test_gecersiz_rastgele_degerleri_reddedilir(self):
        for deger in ("0", "-1", "-64", "abc", "1.5", "", "100001", "1e3"):
            with self.subTest(deger=deger):
                kod, hata, cikti = self._cli("olc", str(self.dosya), "--rastgele", deger)
                self.assertEqual(kod, 2)
                self.assertIn("usage:", hata)
                self.assertIn("--rastgele", hata)
                self.assertNotIn("Traceback", hata)
                self.assertEqual(cikti, "")

    def test_sinir_degerleri_kabul_edilir(self):
        for deger in ("1", "100000"):
            with self.subTest(deger=deger):
                kod, hata, cikti = self._cli("--json", "olc", str(self.dosya), "--rastgele", deger)
                self.assertEqual(kod, 0, hata)
                self.assertEqual(json.loads(cikti)["rastgele_okuma_sayisi"], int(deger))

    def test_olc_fonksiyonu_sifiri_kontrollu_reddeder(self):
        for ad in ("rastgele_okuma", "blok", "rastgele_blok"):
            with self.subTest(ad=ad):
                with self.assertRaises(ValueError):
                    dd.olc(str(self.dosya), **{ad: 0})

    def test_bos_dosya_bolme_hatasi_vermez(self):
        bos = Path(self.tmp.name) / "bos.bin"
        bos.write_bytes(b"")
        r = dd.olc(str(bos), rastgele_okuma=2)
        self.assertEqual(r["bayt"], 0)
        self.assertEqual(r["sirali_MBps"], 0)

    def test_olcum_soguk_olarak_etiketlenmez(self):
        r = dd.olc(str(self.dosya), rastgele_okuma=1)
        self.assertIn("DEĞİLDİR", r["not"])
        self.assertIn("ilk gözlenen okuma", r["not"])
        self.assertTrue(r["onbellek_durumu"].startswith("bilinmiyor"))


if __name__ == "__main__":
    unittest.main()
