import json
import os
import sys
import tempfile
import unittest
from unittest import mock

KOK = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.insert(0, KOK)

from sohbet_olcer import kurulum  # noqa: E402

ORNEK_CODEX = os.path.join(KOK, "testler", "ornekler", "codex")


class Kurulum(unittest.TestCase):
    def setUp(self):
        self.d = tempfile.mkdtemp()
        self.ayar = os.path.join(self.d, "settings.json")

    def _yaz(self, veri):
        with open(self.ayar, "w", encoding="utf-8") as f:
            json.dump(veri, f)

    def _oku(self):
        with open(self.ayar, encoding="utf-8") as f:
            return json.load(f)

    def test_bos_kurulum_ve_tekrar_cogaltmaz(self):
        kod, _ = kurulum.kur(self.ayar)
        self.assertEqual(kod, 0)
        kurulum.kur(self.ayar)
        v = self._oku()
        self.assertIn("kaydet", v["statusLine"]["command"])
        self.assertEqual(len(v["hooks"]["Stop"]), 1)
        self.assertIn("kanca", v["hooks"]["Stop"][0]["hooks"][0]["command"])

    def test_mevcut_ayarlar_korunur_baska_statusline_degismez(self):
        eski = {"model": "x", "statusLine": {"type": "command", "command": "benim.sh"},
                "hooks": {"Stop": [{"hooks": [{"type": "command", "command": "bildirim.sh"}]}],
                          "PreToolUse": [{"matcher": "Bash", "hooks": []}]}}
        self._yaz(eski)
        kod, notlar = kurulum.kur(self.ayar)
        v = self._oku()
        self.assertEqual(v["statusLine"]["command"], "benim.sh")
        self.assertEqual(v["model"], "x")
        self.assertEqual(len(v["hooks"]["Stop"]), 2)
        self.assertIn("PreToolUse", v["hooks"])
        self.assertTrue(any("değiştirilmedi" in n for n in notlar))
        self.assertTrue(any(f.startswith("settings.json.yedek-") for f in os.listdir(self.d)))
        kurulum.kur(self.ayar, statusline_degistir=True)
        self.assertIn("kaydet", self._oku()["statusLine"]["command"])

    def test_kaldir_yalniz_kendi_girdilerini_siler(self):
        self._yaz({"hooks": {"Stop": [{"hooks": [{"type": "command", "command": "bildirim.sh"}]}]}})
        kurulum.kur(self.ayar)
        kurulum.kaldir(self.ayar)
        v = self._oku()
        self.assertNotIn("statusLine", v)
        self.assertEqual(v["hooks"]["Stop"], [{"hooks": [{"type": "command", "command": "bildirim.sh"}]}])

    def test_bozuk_json_dokunulmaz(self):
        with open(self.ayar, "w") as f:
            f.write("{bozuk")
        kod, notlar = kurulum.kur(self.ayar)
        self.assertEqual(kod, 2)
        with open(self.ayar) as f:
            self.assertEqual(f.read(), "{bozuk")

    def test_bosluklu_yol_tirnakli(self):
        kurulum.kur(self.ayar)
        k = self._oku()["statusLine"]["command"]
        self.assertTrue(k.startswith('"') and '" "' in k)


class YerelDogrulama(unittest.TestCase):
    def test_gercek_yerlesimle_rapor(self):
        d = tempfile.mkdtemp()
        claude = os.path.join(d, ".claude")
        proje = os.path.join(claude, "projects", "p")
        os.makedirs(proje)
        with open(os.path.join(proje, "S.jsonl"), "w") as f:
            f.write(json.dumps({"type": "assistant", "sessionId": "S", "uuid": "u1", "parentUuid": None,
                                "timestamp": "2026-01-01T06:00:00Z", "message": {"id": "m1", "usage": {
                                    "input_tokens": 5, "output_tokens": 7}}}) + "\n")
        for kok, _, dosyalar in os.walk(ORNEK_CODEX):  # copytree Wine'da CopyFile2 ister
            hedef = os.path.join(d, "codex", os.path.relpath(kok, ORNEK_CODEX))
            os.makedirs(hedef, exist_ok=True)
            for ad in dosyalar:
                with open(os.path.join(kok, ad), "rb") as a, open(os.path.join(hedef, ad), "wb") as b:
                    b.write(a.read())
        with mock.patch.dict(os.environ, {"CLAUDE_CONFIG_DIR": claude, "CODEX_HOME": os.path.join(d, "codex")}):
            metin, yol = kurulum.dogrula()
        self.assertTrue(os.path.exists(yol))
        self.assertIn("1 konuşma bulundu", metin)
        self.assertIn("Toplam 12 token", metin)
        self.assertIn("Claude kota kaydı: henüz yok", metin)
        self.assertIn("Codex rollout kaydı: 5 dosya, 5 konuşma", metin)
        self.assertIn("Kurulum: Stop kancası kurulu değil", metin)

    def test_hic_veri_yoksa_erisilemiyor(self):
        d = tempfile.mkdtemp()
        with mock.patch.dict(os.environ, {"CLAUDE_CONFIG_DIR": os.path.join(d, "c"), "CODEX_HOME": os.path.join(d, "x")}):
            metin, _ = kurulum.dogrula()
        self.assertIn("Claude Code transkripti: erişilemiyor", metin)
        self.assertIn("Codex rollout kaydı: erişilemiyor", metin)


if __name__ == "__main__":
    unittest.main(verbosity=2)
