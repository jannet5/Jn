"""Birim + uçtan uca testler (SENTETİK veriyle; gerçek cihaz kanıtı değildir)."""
import os, shutil, stat, subprocess, sys, tempfile, unittest
from pathlib import Path
B = Path(__file__).resolve().parent
sys.path.insert(0, str(B.parent / "arac"))
import reboot_teshis as rt

class Sinif(unittest.TestCase):
    def test_siniflar(self):
        beklenen = {
            "kernel_panic": "cekirdek", "watchdog,sec": "cekirdek",
            "shutdown,battery": "guc", "reboot,undervoltage": "guc",
            "shutdown,thermal,battery": "isi", "reboot,userrequested": "kullanici",
            "reboot,ota": "kullanici", "reboot,rescueparty": "yazilim",
            "cold": "belirsiz", "reboot": "belirsiz", "": "belirsiz",
        }
        for neden, sinif in beklenen.items():
            self.assertEqual(rt.boot_nedeni_sinifla(neden), sinif, neden)

    def test_dropbox_ayristir(self):
        o = rt.dropbox_olaylari((B / "ornek-veri/dropbox_liste.txt").read_text())
        self.assertEqual([e for _, e in o][:4],
                         ["data_app_crash", "system_server_crash", "SYSTEM_RESTART", "SYSTEM_BOOT"])

class UctanUca(unittest.TestCase):
    def test_topla_ve_analiz(self):
        with tempfile.TemporaryDirectory() as t:
            bin_ = Path(t) / "bin"; bin_.mkdir()
            shim = bin_ / "adb"
            shim.write_text(f"#!/bin/sh\nexec {sys.executable} {B/'sahte_adb.py'} \"$@\"\n")
            shim.chmod(shim.stat().st_mode | stat.S_IEXEC)
            env = dict(os.environ, PATH=f"{bin_}{os.pathsep}{os.environ['PATH']}")
            cikti = Path(t) / "kayit"
            p = subprocess.run([sys.executable, str(B.parent / "arac/reboot_teshis.py"), "hepsi",
                                "--cikti", str(cikti)], env=env, capture_output=True, text=True)
            self.assertEqual(p.returncode, 0, p.stderr)
            rapor = (cikti / "rapor.md").read_text()
            self.assertIn("uygulama tetiklemeli olma olasılığı YÜKSEK", rapor)
            self.assertIn("`com.ornek.benimuygulamam` | null", rapor)      # yan yükleme tespiti
            self.assertIn("2026-10-02 22:45:13 | `com.ornek.benimuygulamam`", rapor)  # zaman eşleşmesi
            self.assertNotIn("com.whatsapp`", rapor.split("## 6.")[1].split("## 7.")[0])
            self.assertIn("%90 üstü", rapor)
            for d in ("getprop.txt", "dropbox_icerik.txt", "paket_zamanlari.txt", "logcat_tum.txt"):
                self.assertTrue((cikti / d).exists(), d)

    def test_kanitsiz_kesin_neden_yok(self):
        with tempfile.TemporaryDirectory() as t:
            k = Path(t)
            (k / "getprop.txt").write_text("[sys.boot.reason]: [reboot]\n")
            rapor = rt.analiz(k)
            self.assertIn("Tek kesin neden ilan edilemez", rapor)

    def test_adb_yoksa_anlasilir_hata(self):
        env = dict(os.environ, PATH="/nonexistent")
        p = subprocess.run([sys.executable, str(B.parent / "arac/reboot_teshis.py"), "topla"],
                           env=env, capture_output=True, text=True)
        self.assertNotEqual(p.returncode, 0)
        self.assertIn("adb", p.stderr)

if __name__ == "__main__":
    unittest.main(verbosity=2)
