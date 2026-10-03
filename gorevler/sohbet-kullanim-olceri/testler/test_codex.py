import hashlib
import os
import subprocess
import sys
import unittest
from datetime import datetime, timezone

KOK = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.insert(0, KOK)

from sohbet_olcer import codex, kota  # noqa: E402

ORNEK = os.path.join(KOK, "testler", "ornekler", "codex", "sessions")
A = "aaaaaaaa-0000-4000-8000-000000000001"
B = "bbbbbbbb-0000-4000-8000-000000000002"
C = "cccccccc-0000-4000-8000-000000000003"
D = "dddddddd-0000-4000-8000-000000000004"
F = "ffffffff-0000-4000-8000-000000000006"
SIMDI = datetime(2026, 1, 1, 6, 10, tzinfo=timezone.utc)


def _hash_hepsi():
    sonuc = {}
    for kok, _, fs in os.walk(ORNEK):
        for f in fs:
            with open(os.path.join(kok, f), "rb") as h:
                sonuc[f] = hashlib.sha256(h.read()).hexdigest()
    return sonuc


class CodexTokenSayimi(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.once = _hash_hepsi()
        cls.ok = codex.oku([ORNEK])

    def test_konusmalar_ayri_ve_dogru_toplam(self):
        k = self.ok.konusmalar
        self.assertEqual(set(k), {A, B, C, D, F})
        self.assertEqual(k[A].sayac, {"input_tokens": 2000, "cached_input_tokens": 1500,
                                      "cache_write_input_tokens": 0, "output_tokens": 500,
                                      "reasoning_output_tokens": 120, "total_tokens": 2500})
        self.assertEqual(k[B].sayac["total_tokens"], 4000)
        self.assertEqual(k[F].sayac["total_tokens"], 110)

    def test_tekrar_degismeyen_ve_cata_kopyasi_sayilmaz(self):
        self.assertEqual(self.ok.tekrar_olay, 1)       # A'da birebir yinelenen satır
        self.assertEqual(self.ok.kopya_olay, 3)        # C'ye kopyalanan A satırları
        self.assertEqual(self.ok.degismeyen_olay, 1)   # sayaç aynı, yalnız kota güncel
        c = self.ok.konusmalar[C]
        self.assertEqual(c.sayac["total_tokens"], 600)
        self.assertEqual(c.meta["forked_from_id"], A)

    def test_sayac_geri_duserse_son_tur_eklenir(self):
        d = self.ok.konusmalar[D]
        self.assertEqual(d.sayac["total_tokens"], 5700)
        self.assertEqual(d.sayac_sifirlanma, 1)
        self.assertEqual(self.ok.yarim_son_satir, 1)

    def test_salt_okunur(self):
        self.assertEqual(_hash_hepsi(), self.once)
        with open(os.path.join(KOK, "sohbet_olcer", "codex.py"), encoding="utf-8") as f:
            kaynak = f.read()
        self.assertNotIn('"w"', kaynak)
        self.assertNotIn("'w'", kaynak)
        self.assertNotIn("auth.json\")", kaynak)


class CodexKota(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.ok = codex.oku([ORNEK])

    def test_pencere_adlari_dakikadan(self):
        self.assertEqual({g.pencere for g in self.ok.goruntuler}, {"codex_five_hour", "codex_seven_day"})
        self.assertEqual(codex.pencere_adi(60, "codex_other"), "codex_60m[codex_other]")

    def test_eski_resets_in_seconds_ile_yeni_resets_at_ayni_zaman(self):
        b = [g for g in self.ok.goruntuler if g.oturum == B and g.pencere == "codex_five_hour"]
        a = [g for g in self.ok.goruntuler if g.oturum == A and g.pencere == "codex_five_hour"]
        self.assertEqual(b[0].yenilenme, a[0].yenilenme)
        self.assertEqual(a[0].yenilenme.isoformat(), "2026-01-01T10:00:00+00:00")

    def test_haftalik_kalan_yenilenme_ve_kaynak_tarihi(self):
        d = kota.guncel_durum(self.ok.goruntuler, "codex_seven_day", SIMDI)
        g = d["goruntu"]
        self.assertEqual((g.kullanilan, g.kalan), (42.0, 58.0))
        self.assertEqual(g.zaman.isoformat(), "2026-01-01T06:05:01+00:00")  # kaynağın tarihi (eşit değerde en yenisi)
        self.assertEqual(g.yenilenme.isoformat(), "2026-01-05T08:00:00+00:00")
        self.assertEqual(len(d["eski_dusuk"]), 1)  # B'nin eskimiş %40'ı
        self.assertEqual(g.cozunurluk, 1.0)

    def test_simdiden_sonraki_kayit_kullanilmaz(self):
        g = kota.guncel_durum(self.ok.goruntuler, "codex_five_hour", SIMDI)["goruntu"]
        self.assertEqual(g.kullanilan, 16.0)

    def test_haftalik_pencere_yenilenmesi(self):
        sonra = datetime(2026, 1, 3, tzinfo=timezone.utc)
        g = kota.guncel_durum(self.ok.goruntuler, "codex_seven_day", sonra)["goruntu"]
        self.assertEqual(g.kullanilan, 2.0)  # yeni dönem, eski %96 değil
        d = self.ok.konusmalar[D]
        f = kota.fark_hesapla(self.ok.goruntuler, "codex_seven_day", d.ilk, d.son, sonra)
        self.assertEqual(f.durum, "yenilendi")

    def test_rapor_metni(self):
        m = codex.metin_raporu(self.ok, self.ok.konusmalar[A], SIMDI, "Europe/Istanbul")
        self.assertIn("Toplam 2.500 token", m)
        self.assertIn("Codex haftalık kota (hesap geneli, ölçüldü, Codex rollout kaydından): "
                      "yüzde 42 kullanıldı, yüzde 58 kaldı", m)
        self.assertIn("Kaynağın tarihi: 1 Ocak 2026 09:05 (4 dakika önce)", m)
        self.assertIn("Yenilenme zamanı: 5 Ocak 2026 11:00", m)
        self.assertIn("yüzde 10 → yüzde 16 (5–7 puan aralığında)", m)
        self.assertIn("Bu yalnız bu konuşmanın tüketimi değildir", m)
        self.assertIn("auth/kimlik bilgisi okunmaz", m)
        self.assertIn("\n\n", m)

    def test_kota_bilgisi_olmayan_kayit(self):
        ok = codex.oku([os.path.join(ORNEK, "2026", "01", "03")])
        m = codex.metin_raporu(ok, ok.konusmalar[F], SIMDI, "Europe/Istanbul")
        self.assertIn("Codex haftalık kota: erişilemiyor", m)
        self.assertNotIn("kullanıldı", m)


class CodexKomut(unittest.TestCase):
    def _c(self, *args):
        return subprocess.run([sys.executable, "-m", "sohbet_olcer", "codex", *args], cwd=KOK,
                              capture_output=True, text=True, encoding="utf-8", timeout=60)

    def test_kayit_yoksa_erisilemiyor(self):
        r = self._c("--kok", os.path.join(KOK, "testler", "yok"))
        self.assertEqual(r.returncode, 2)
        self.assertIn("erişilemiyor", r.stdout)

    def test_liste_ve_rapor(self):
        r = self._c("--kok", ORNEK, "--liste")
        self.assertEqual(r.returncode, 0)
        self.assertEqual(len(r.stdout.strip().splitlines()), 5)
        r = self._c("--kok", ORNEK, "--konusma", "aaaa", "--simdi", "2026-01-01T06:10:00Z")
        self.assertIn("Codex kullanım raporu, konuşma " + A, r.stdout)


if __name__ == "__main__":
    unittest.main(verbosity=2)
