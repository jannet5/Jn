"""Çevrimdışı birim + uçtan uca testler. Örnek veriler SENTETİKTİR (gerçek Instagram gönderisi değildir)."""
import json
import os
import subprocess
import sys
import tempfile
import unittest
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT))

from PIL import Image, ImageDraw  # noqa: E402

from viralforge import brain, importers, report, select, urls, verify  # noqa: E402
from viralforge.store import Workspace  # noqa: E402

FAKE = str(ROOT / "tests" / "fake_codex.py")


def make_img(path: Path, seed: int, side: int = 600):
    import random
    rnd = random.Random(seed)
    im = Image.new("RGB", (side, side), (rnd.randrange(256), 40, 90))
    d = ImageDraw.Draw(im)
    for _ in range(8):
        x, y = rnd.randrange(side), rnd.randrange(side)
        d.ellipse([x, y, x + 120, y + 120], fill=(rnd.randrange(256), rnd.randrange(256), 0))
    path.parent.mkdir(parents=True, exist_ok=True)
    im.save(path)


def seed_posts(ws: Workspace, n: int, side: int = 600):
    for i in range(n):
        code = f"SYN{i:05d}x"
        p = importers.new_post("p" if i % 2 else "reel", code)
        p["caption"] = f"Sentetik açıklama {i} — sence hangisi?"
        p["metrics"].update(likes=1000 + i * 7, comments=50 + i, views=None if i % 2 else 20000 + i)
        if i % 2:
            p["audio"]["status"] = "none"
        else:
            p["audio"].update(status="known", title="Original audio", artist=f"creator{i}", original=True)
        ws.save_post(p)
        make_img(ws.post_dir(code) / "media" / "01.jpg", seed=i, side=side)
        p["media"].append({"file": "media/01.jpg", "remote": None})
        ws.save_post(p)


class Base(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.ws = Workspace(Path(self.tmp.name) / "ws").ensure()
        os.environ["VF_CODEX_BIN"] = FAKE
        for k in ("FAKE_CODEX_LIMIT_AFTER", "FAKE_CODEX_COUNTER", "FAKE_CODEX_LOG", "FAKE_CODEX_NO_AUTH"):
            os.environ.pop(k, None)

    def tearDown(self):
        self.tmp.cleanup()


class TestUrls(Base):
    def test_normalize_variants(self):
        cases = {
            "http://instagram.com/p/AbC_12-x/?igsh=1": "https://www.instagram.com/p/AbC_12-x/",
            "https://www.instagram.com/reels/XyZ12345/": "https://www.instagram.com/reel/XyZ12345/",
            "instagram.com/someuser/p/Q1w2e3r4/": "https://www.instagram.com/p/Q1w2e3r4/",
            "https://m.instagram.com/tv/TvCode99/": "https://www.instagram.com/reel/TvCode99/",
        }
        for raw, want in cases.items():
            self.assertEqual(urls.normalize(raw)[2], want)
        self.assertIsNone(urls.normalize("https://www.instagram.com/explore/"))

    def test_import_dyi_zip_html_dedupe(self):
        d = Path(self.tmp.name)
        saved = {"saved_saved_media": [
            {"title": "u1", "string_map_data": {"Saved on": {"href": "https://www.instagram.com/p/AAAAA111/", "timestamp": 1}}},
            {"title": "u2", "string_map_data": {"Saved on": {"href": "https://www.instagram.com/reel/BBBBB222/"}}}]}
        z = d / "dyi.zip"
        with zipfile.ZipFile(z, "w") as zf:
            # Meta dışa aktarımı eğik çizgileri \/ biçiminde kaçışlar
            zf.writestr("your_instagram_activity/saved/saved_posts.json", json.dumps(saved).replace("/", "\\/"))
        (d / "list.html").write_text('<a href="https://instagram.com/p/AAAAA111/">x</a> https://www.instagram.com/p/CCCCC333/')
        r = importers.import_urls(self.ws, [str(z), str(d / "list.html")])
        self.assertEqual(r["total_posts"], 3)
        self.assertEqual(r["found_links"], 4)
        self.assertTrue(all(p["url"].startswith("https://") for p in self.ws.iter_posts()))


class TestMeta(Base):
    def test_og_description(self):
        og = importers.parse_og_description('12.5K likes, 1,204 comments - some.user on May 1, 2026: "Bunu bilen var mı?"')
        self.assertEqual(og["likes"], 12500)
        self.assertEqual(og["comments"], 1204)
        self.assertEqual(og["caption"], "Bunu bilen var mı?")

    def test_import_meta_capture_and_ytdlp(self):
        d = Path(self.tmp.name) / "dl"
        d.mkdir()
        (d / "vf-capture-DDDDD444.json").write_text(json.dumps({
            "url": "https://www.instagram.com/p/DDDDD444/",
            "og_description": '900 likes, 30 comments - a.b on June 2, 2026: "Kahve mi çay mı?"',
            "audio": {"status": "known", "artist": "Artist X", "title": "Song Y", "original": False}}))
        make_img(d / "vf-capture-DDDDD444.jpg", 1)
        (d / "reel.info.json").write_text(json.dumps({
            "webpage_url": "https://www.instagram.com/reel/EEEEE555/", "description": "Reel açıklaması",
            "like_count": 5000, "comment_count": 200, "view_count": 90000, "track": "Original audio",
            "artist": "creator", "timestamp": 1760000000}))
        importers.import_meta(self.ws, [str(d)])
        p = self.ws.load_post("DDDDD444")
        self.assertEqual((p["caption"], p["metrics"]["likes"], p["audio"]["title"]), ("Kahve mi çay mı?", 900, "Song Y"))
        self.assertTrue(all(select.completeness(self.ws, p).values()))
        r = self.ws.load_post("EEEEE555")
        self.assertEqual((r["metrics"]["views"], r["audio"]["original"]), (90000, True))
        self.assertFalse(select.completeness(self.ws, r)["photo"])  # görsel yoksa eksik sayılır


class TestGalleryDl(Base):
    def test_gallery_dl_sidecar(self):
        d = Path(self.tmp.name) / "gdl"
        make_img(d / "FFFFF666_1.jpg", 3)
        (d / "FFFFF666_1.jpg.json").write_text(json.dumps({
            "post_shortcode": "FFFFF666", "description": "Karusel", "likes": 777, "username": "gdl.user"}))
        importers.import_meta(self.ws, [str(d)])
        p = self.ws.load_post("FFFFF666")
        self.assertEqual((p["owner"], p["metrics"]["likes"], len(self.ws.media_files("FFFFF666"))), ("gdl.user", 777, 1))


class TestSelect(Base):
    def test_ranking_and_incomplete_excluded(self):
        seed_posts(self.ws, 6)
        bad = self.ws.load_post("SYN00000x")
        bad["caption"] = None
        self.ws.save_post(bad)
        r = select.select_top(self.ws, top=3)
        self.assertEqual((r["selected"], r["skipped_incomplete"]), (3, 1))
        ranks = sorted((p["rank"], p["score"]) for p in self.ws.iter_posts(selected_only=True))
        self.assertTrue(ranks[0][1] >= ranks[-1][1])


class TestPipeline(Base):
    def test_end_to_end_done(self):
        seed_posts(self.ws, 5)
        select.select_top(self.ws, top=5)
        self.assertEqual(brain.analyze_all(self.ws, jobs=2, log=lambda *_: None)["ok"], 5)
        self.assertEqual(brain.generate_all(self.ws, jobs=2, log=lambda *_: None)["ok"], 20)
        self.assertEqual(verify.verify_all(self.ws)["verified_images"], 20)
        c = verify.status(self.ws, target_posts=5)
        self.assertTrue(c["done"])
        self.assertTrue(verify.done_answer(c).startswith("Evet, bitti"))
        html = report.build_report(self.ws).read_text()
        self.assertEqual(html.count("✅"), 20)

    def test_not_done_answer_is_specific(self):
        seed_posts(self.ws, 2)
        select.select_top(self.ws, top=2)
        ans = verify.done_answer(verify.status(self.ws, target_posts=1000))
        self.assertIn("Henüz değil", ans)
        self.assertIn("2/1000", ans)

    def test_limit_stops_and_resume(self):
        seed_posts(self.ws, 3)
        select.select_top(self.ws, top=3)
        os.environ["FAKE_CODEX_COUNTER"] = str(Path(self.tmp.name) / "cnt")
        os.environ["FAKE_CODEX_LIMIT_AFTER"] = "5"  # 3 analiz + 2 görselden sonra limit
        brain.analyze_all(self.ws, jobs=1, log=lambda *_: None)
        g = brain.generate_all(self.ws, jobs=1, log=lambda *_: None)
        self.assertEqual((g["ok"], g["limit"]), (2, 1))
        os.environ.pop("FAKE_CODEX_LIMIT_AFTER")
        g2 = brain.generate_all(self.ws, jobs=1, log=lambda *_: None)
        self.assertEqual(g2["ok"], 10)  # kaldığı yerden; üretilmişler tekrar üretilmez

    def test_missing_login_stops_early(self):
        seed_posts(self.ws, 4)
        select.select_top(self.ws, top=4)
        log = Path(self.tmp.name) / "calls.jsonl"
        os.environ.update(FAKE_CODEX_LOG=str(log), FAKE_CODEX_NO_AUTH="1")
        try:
            r = brain.analyze_all(self.ws, jobs=1, log=lambda *_: None)
        finally:
            os.environ.pop("FAKE_CODEX_NO_AUTH")
        self.assertEqual((r["auth"], r["ok"]), (1, 0))
        # Hepsi (4) denenmez: ilk hatada durur; o anda başlamış en fazla 1 çağrı daha olabilir.
        self.assertLessEqual(len(log.read_text().splitlines()), 2)

    def test_api_key_not_passed(self):
        seed_posts(self.ws, 1)
        select.select_top(self.ws, top=1)
        log = Path(self.tmp.name) / "calls.jsonl"
        os.environ["FAKE_CODEX_LOG"] = str(log)
        os.environ["OPENAI_API_KEY"] = "sk-test-not-real"
        try:
            brain.analyze_all(self.ws, log=lambda *_: None)
        finally:
            os.environ.pop("OPENAI_API_KEY")
        calls = [json.loads(line) for line in log.read_text().splitlines()]
        self.assertTrue(calls and not any(c["api_key_seen"] for c in calls))
        self.assertIn("--output-schema", calls[0]["args"])
        self.assertIn("--image", calls[0]["args"])

    def test_invalid_analysis_rejected(self):
        bad = {"shortcode": "X", "why_it_worked": {"visual": "a", "audio": "", "caption": "c", "combined": "d"},
               "success_factors": [1], "audience_emotion": "", "hook_type": "",
               "variants": [{"id": "v1", "image_prompt": "same prompt words here repeated for test purposes only ok"}] * 4}
        errs = brain.validate_analysis(bad, "X")
        self.assertTrue(any("audio" in e for e in errs))
        self.assertTrue(any("v1..v4" in e for e in errs))


class TestVerify(Base):
    def test_duplicate_and_copy_detected(self):
        seed_posts(self.ws, 1)
        code = "SYN00000x"
        src = self.ws.media_files(code)[0]
        for i, vid in enumerate(("v1", "v2", "v3", "v4")):
            p = self.ws.variant_path(code, vid)
            p.parent.mkdir(parents=True, exist_ok=True)
            if vid == "v4":
                Image.open(src).save(p)  # kaynağın kopyası
            else:
                make_img(p, seed=100 if vid in ("v1", "v2") else 200 + i)  # v1 == v2
        r = verify.verify_post(self.ws, code)
        self.assertEqual(r["verified_variants"], 2)
        self.assertTrue(any("neredeyse aynı" in x for x in r["problems"]))
        self.assertTrue(any("kopya" in x for x in r["problems"]))

    def test_chatgpt_sheet_and_ingest(self):
        seed_posts(self.ws, 1)
        select.select_top(self.ws, top=1)
        brain.analyze_all(self.ws, log=lambda *_: None)
        sheet, n = report.build_chatgpt_sheet(self.ws)
        self.assertEqual(n, 4)
        self.assertNotIn("$imagegen", sheet.read_text())
        dl = Path(self.tmp.name) / "indirilenler"
        for i in range(1, 5):
            make_img(dl / f"SYN00000x_v{i}.jpg", seed=500 + i)
        (dl / "alakasiz.png").write_bytes(b"x")
        self.assertEqual(report.ingest_images(self.ws, str(dl)), {"ingested": 4, "unmatched": 1})
        self.assertEqual(verify.verify_post(self.ws, "SYN00000x")["verified_variants"], 4)


class TestCli(Base):
    def test_cli_bitti_mi_exit_codes(self):
        seed_posts(self.ws, 2)
        env = {**os.environ, "PYTHONPATH": str(ROOT)}
        run = lambda *a: subprocess.run([sys.executable, "-m", "viralforge", "-w", str(self.ws.root), *a],
                                        capture_output=True, text=True, env=env)
        r = run("run", "--target", "2")
        self.assertEqual(r.returncode, 0, r.stdout + r.stderr)
        self.assertIn("Evet, bitti", r.stdout)
        r = run("bitti-mi", "--target", "1000")
        self.assertEqual(r.returncode, 1)
        r = run("fetch")
        self.assertNotEqual(r.returncode, 0)
        self.assertIn("Kullanım Koşulları", r.stdout + r.stderr)


if __name__ == "__main__":
    unittest.main()
