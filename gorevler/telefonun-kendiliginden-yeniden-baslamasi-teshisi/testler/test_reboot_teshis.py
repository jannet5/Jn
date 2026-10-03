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
            self.assertIn("system_server_crash: java.lang.SecurityException: sentetik örnek", rapor)
            for d in ("getprop.txt", "dropbox_icerik.txt", "paket_zamanlari.txt", "logcat_tum.txt"):
                self.assertTrue((cikti / d).exists(), d)

    def test_kanitsiz_kesin_neden_yok(self):
        with tempfile.TemporaryDirectory() as t:
            k = Path(t)
            (k / "getprop.txt").write_text("[sys.boot.reason]: [reboot]\n")
            rapor = rt.analiz(k)
            self.assertIn("Tek kesin neden ilan edilemez", rapor)

    def test_yumusak_yeniden_baslama_logcattan(self):
        # Satır biçimi API 30 emülatöründe gözlenen gerçek logcat biçimini taklit eder.
        with tempfile.TemporaryDirectory() as t:
            k = Path(t)
            (k / "getprop.txt").write_text("[sys.boot.reason]: [reboot]\n")
            (k / "logcat_events_boot.txt").write_text(
                "2026-10-03 11:44:08.165   308   308 I boot_progress_start: 87131\n"
                "2026-10-03 11:50:50.460   553   609 I watchdog: Blocked in handler on main thread (main)\n"
                "2026-10-03 11:51:44.954  1497  1497 I boot_progress_start: 543921\n")
            (k / "logcat_crash.txt").write_text(
                "2026-10-03 11:57:53.440  1756  1756 E AndroidRuntime: FATAL EXCEPTION: main\n"
                "2026-10-03 11:57:53.440  1756  1756 E AndroidRuntime: Process: com.android.systemui, PID: 1756\n"
                "2026-10-03 11:57:53.440  1756  1756 E AndroidRuntime: DeadSystemException: The system died; earlier logs\n")
            rapor = rt.analiz(k)
            self.assertIn("**2 kez**", rapor)
            self.assertIn("Watchdog uyarısı", rapor)
            self.assertIn("Yazılım tarafı OLASI", rapor)
            self.assertIn("| `com.android.systemui` | 1 |", rapor)

    def test_adb_yoksa_anlasilir_hata(self):
        env = dict(os.environ, PATH="/nonexistent")
        p = subprocess.run([sys.executable, str(B.parent / "arac/reboot_teshis.py"), "topla"],
                           env=env, capture_output=True, text=True)
        self.assertNotEqual(p.returncode, 0)
        self.assertIn("adb", p.stderr)

if __name__ == "__main__":
    unittest.main(verbosity=2)
