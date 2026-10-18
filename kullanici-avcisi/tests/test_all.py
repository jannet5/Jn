import json
import os
import re
import tempfile
import threading
import time
import unittest
from http.server import BaseHTTPRequestHandler, HTTPServer

from avci.candidates import from_combos, from_pattern, from_words, quality
from avci.checkers import FREE, LIMITED, TAKEN, InstagramProfileChecker, MockChecker
from avci.engine import Engine
from avci.notify import Notifier
from avci.store import Store
from avci.translit import to_latin

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
VALID = re.compile(r"^[a-z]+$")


def tmpdb():
    return Store(os.path.join(tempfile.mkdtemp(), "t.db"))


def engine(store, checker, **kw):
    nt = Notifier(found_file=os.path.join(tempfile.mkdtemp(), "f.txt"), bell=False)
    kw.setdefault("rps", 200)
    kw.setdefault("workers", 2)
    kw.setdefault("hot_seconds", 0.3)
    kw.setdefault("cool_hours", 1)
    kw.setdefault("confirm", 2)
    kw.setdefault("confirm_gap", 0.05)
    kw.setdefault("quiet", True)
    return Engine(store, checker, nt, **kw), nt


class Translit(unittest.TestCase):
    def test_arabic(self):
        self.assertIn("habibi", to_latin("حبيبي"))
        self.assertIn("salam", to_latin("سلام"))
        self.assertIn("qamar", to_latin("قمر"))

    def test_harakat_and_shadda(self):
        self.assertIn("muhammad", to_latin("مُحَمَّد"))

    def test_other_languages(self):
        self.assertEqual(to_latin("Привет"), ["privet"])
        self.assertEqual(to_latin("şeker"), ["seker"])
        self.assertEqual(to_latin("Ελλάδα"), ["ellada"])
        self.assertTrue(to_latin("東京"))  # unidecode varsa

    def test_output_only_az(self):
        for w in ["حبيبي", "عَشق", "Привет", "şeker", "naïve", "mañana", "名前", "7abibi"]:
            for v in to_latin(w):
                self.assertRegex(v, VALID)

    def test_digits_flag(self):
        self.assertTrue(any("7" in v for v in to_latin("حبيبي", digits=True)))
        self.assertFalse(any(any(c.isdigit() for c in v) for v in to_latin("حبيبي")))


class Candidates(unittest.TestCase):
    def test_wordlist_length_filter(self):
        got = list(from_words(os.path.join(ROOT, "wordlists", "ar_seed.txt"), 3, 7))
        self.assertGreater(len(got), 100)
        for n, _, _ in got:
            self.assertTrue(3 <= len(n) <= 7)
            self.assertRegex(n, VALID)
        self.assertEqual(len({n for n, _, _ in got}), len(got))  # tekrar yok

    def test_combo_and_pattern_counts(self):
        self.assertEqual(sum(1 for _ in from_combos(3)), 26 ** 3)
        self.assertEqual(sum(1 for _ in from_pattern("CVCV")), 21 * 5 * 21 * 5)

    def test_readable_beats_skeleton(self):
        self.assertGreater(quality("qamar"), quality("qmr"))
        self.assertGreater(quality("habibi"), quality("hbyby"))


class StoreTests(unittest.TestCase):
    def test_lease_order_and_no_duplicates(self):
        st = tmpdb()
        st.add_many([("aaa", "t", 10), ("bbb", "t", 90), ("ccc", "t", 50)])
        st.add_many([("hot", "m", 1)], hot=True)
        order = [st.lease() for _ in range(4)]
        self.assertEqual(order, ["hot", "bbb", "ccc", "aaa"])
        self.assertIsNone(st.lease())  # hepsi kirada

    def test_found_never_leased_again(self):
        st = tmpdb()
        st.add_many([("abc", "t", 1)])
        st.record("abc", "found", 0)
        self.assertIsNone(st.lease(now=time.time() + 10 ** 7))


class EngineTests(unittest.TestCase):
    def test_catches_released_name_and_ignores_taken(self):
        st = tmpdb()
        names = ["alpha", "bravo", "charlie", "delta"]
        st.add_many([(n, "t", 50) for n in names])
        st.set_hot("bravo")
        chk = MockChecker(taken=names, release_at={"bravo": 0.6})
        eng, nt = engine(st, chk)
        res = eng.run(minutes=0.05)
        self.assertEqual(res["bulunanlar"], ["bravo"])
        self.assertEqual([r["name"] for r in st.found()], ["bravo"])
        self.assertIn("bravo", open(nt.found_file).read())

    def test_flapping_name_is_not_reported(self):
        st = tmpdb()
        st.add_many([("ghost", "t", 50)])
        chk = MockChecker(taken=["ghost"], flaky={"ghost"})
        eng, _ = engine(st, chk)
        res = eng.run(minutes=0.03)
        self.assertEqual(res["bulunanlar"], [])

    def test_rate_limit_does_not_lose_names(self):
        st = tmpdb()
        names = [f"name{chr(97 + i)}" for i in range(15)]
        st.add_many([(n, "t", 50) for n in names])
        chk = MockChecker(taken=names, limit_every=3)
        eng, _ = engine(st, chk)
        eng.run(minutes=0.1)
        s = st.stats()
        self.assertEqual(s["bekleyen"], 0)  # hepsi sonunda kontrol edildi
        self.assertGreater(eng.counters["yavaslama"], 0)

    def test_resume_after_restart_keeps_state(self):
        path = os.path.join(tempfile.mkdtemp(), "r.db")
        st = Store(path)
        st.add_many([("one", "t", 50), ("two", "t", 40)])
        chk = MockChecker(taken=["one"], release_at={})
        eng, _ = engine(st, chk)
        eng.run(minutes=0.03)
        self.assertEqual([r["name"] for r in st.found()], ["two"])
        # yeniden başlat: aynı DB, bulunan tekrar bulunmaz, dolu isim henüz zamanı gelmedi
        st2 = Store(path)
        chk2 = MockChecker(taken=["one"])
        eng2, _ = engine(st2, chk2)
        eng2.run(minutes=0.02)
        self.assertEqual(chk2.calls, 0)

    def test_taken_name_is_rechecked_and_caught_when_it_frees(self):
        st = tmpdb()
        st.add_many([("later", "t", 50)])
        st.set_hot("later")
        chk = MockChecker(taken=["later"], release_at={"later": 1.0})
        eng, _ = engine(st, chk, hot_seconds=0.2)
        res = eng.run(minutes=0.08)
        self.assertEqual(res["bulunanlar"], ["later"])


class InterpretInstagram(unittest.TestCase):
    def test_mapping(self):
        f = InstagramProfileChecker.interpret
        self.assertEqual(f(404, {}, "").status, FREE)
        self.assertEqual(f(200, {}, json.dumps({"data": {"user": {"id": "1"}}, "status": "ok"})).status, TAKEN)
        self.assertEqual(f(200, {}, json.dumps({"data": {"user": None}, "status": "ok"})).status, FREE)
        self.assertEqual(f(200, {}, "<html>login</html>").status, LIMITED)
        r = f(429, {"Retry-After": "42"}, "")
        self.assertEqual((r.status, r.retry_after), (LIMITED, 42.0))
        self.assertEqual(f(302, {}, "").status, LIMITED)


class NotifyTests(unittest.TestCase):
    def test_telegram_and_discord_payloads(self):
        got = []

        class H(BaseHTTPRequestHandler):
            def do_POST(self):
                body = self.rfile.read(int(self.headers["Content-Length"]))
                got.append((self.path, json.loads(body)))
                self.send_response(200)
                self.end_headers()

            def log_message(self, *a):
                pass

        srv = HTTPServer(("127.0.0.1", 0), H)
        threading.Thread(target=srv.serve_forever, daemon=True).start()
        base = f"http://127.0.0.1:{srv.server_port}"
        os.environ["TG_API_BASE"] = base
        try:
            nt = Notifier(found_file=os.path.join(tempfile.mkdtemp(), "f.txt"), bell=False,
                          telegram_token="TOK", telegram_chat="42", discord_webhook=base + "/hook")
            nt.found("habibi")
            for _ in range(50):
                if len(got) >= 2:
                    break
                time.sleep(0.1)
        finally:
            os.environ.pop("TG_API_BASE", None)
            srv.shutdown()
            srv.server_close()
        paths = {p for p, _ in got}
        self.assertIn("/botTOK/sendMessage", paths)
        self.assertIn("/hook", paths)
        self.assertTrue(all("habibi" in json.dumps(b) for _, b in got))


class WebUiTests(unittest.TestCase):
    def ctrl(self, demo=True):
        from avci.webui import Controller
        return Controller(os.path.join(tempfile.mkdtemp(), "u.db"), demo=demo)

    def test_add_names_converts_and_filters(self):
        c = self.ctrl()
        r = c.add_names("حبيبي ab şeker @Qamar", False, 3, 7)
        rows = {x["girdi"]: x["uygun"] for x in r["rows"]}
        self.assertIn("habibi", rows["حبيبي"])
        self.assertEqual(rows["ab"], [])
        self.assertEqual(rows["şeker"], ["seker"])
        self.assertEqual(rows["Qamar"], ["qamar"])
        self.assertGreaterEqual(c.store.stats()["sicak_liste"], 10)

    def test_blocked_connection_asks_before_starting(self):
        c = self.ctrl(demo=False)
        c.checker = MockChecker(limit_every=1)  # hep "yavaşla"
        c.add_names("habibi", False, 3, 7)
        r = c.start()
        self.assertFalse(r["ok"])
        self.assertTrue(r.get("needs_force"))
        self.assertIn("429", r["msg"])
        self.assertFalse(c.running())

    def test_start_stop(self):
        c = self.ctrl()
        c.add_names("habibi qamar salam", False, 3, 7)
        self.assertTrue(c.start()["ok"])
        self.assertTrue(c.running())
        self.assertFalse(c.start()["ok"])  # ikinci kez başlatılamaz
        time.sleep(1.0)
        c.stop()
        self.assertFalse(c.running())

    def test_build_respects_big_flag(self):
        c = self.ctrl()
        r = c.build(["ar"], [], False, 3, 7, False, big=False)
        self.assertGreater(r["added"], 100)
        self.assertFalse(any("50k" in line for line in r["report"]))

    def test_http_token_and_state(self):
        import urllib.error
        import urllib.request
        httpd, ctrl, token, url = __import__("avci.webui", fromlist=["x"]).create_server(
            os.path.join(tempfile.mkdtemp(), "h.db"), port=8851, demo=True)
        threading.Thread(target=httpd.serve_forever, daemon=True).start()
        try:
            with self.assertRaises(urllib.error.HTTPError) as e:
                urllib.request.urlopen(url + "api/state")
            self.assertEqual(e.exception.code, 404)
            req = urllib.request.Request(url + "api/state", headers={"X-Token": token})
            data = json.loads(urllib.request.urlopen(req).read())
            self.assertIn("stats", data)
            self.assertTrue(data["demo"])
            self.assertIn(token, urllib.request.urlopen(url).read().decode())
        finally:
            httpd.shutdown()
            httpd.server_close()


class FakeInstagram:
    """Yerel sahte Instagram: gerçek HTTP, gerçek istek başlıkları. mode='all429' ise her şeye 'yavaşla' der."""

    def __init__(self, mode="normal"):
        self.mode, self.seen = mode, []
        outer = self

        class H(BaseHTTPRequestHandler):
            def log_message(self, *a):
                pass

            def do_GET(self):
                from urllib.parse import parse_qs, urlparse
                q = parse_qs(urlparse(self.path).query)
                name = (q.get("username") or [""])[0]
                outer.seen.append({"name": name, "app_id": self.headers.get("X-IG-App-ID"),
                                   "ua": self.headers.get("User-Agent"), "referer": self.headers.get("Referer")})
                code, body, extra = 404, "", {}
                if outer.mode == "all429" or name == "limited":
                    code, extra = 429, {"Retry-After": "7"}
                elif name in ("instagram", "taken"):
                    code, body = 200, json.dumps({"data": {"user": {"id": "1"}}, "status": "ok"})
                elif name == "wall":
                    code, body = 200, "<html>login</html>"
                self.send_response(code)
                self.send_header("Content-Type", "application/json" if body.startswith("{") else "text/html")
                for k, v in extra.items():
                    self.send_header(k, v)
                self.send_header("Content-Length", str(len(body.encode())))
                self.end_headers()
                self.wfile.write(body.encode())

        self.srv = HTTPServer(("127.0.0.1", 0), H)
        self.url = f"http://127.0.0.1:{self.srv.server_port}/api/v1/users/web_profile_info/"
        threading.Thread(target=self.srv.serve_forever, daemon=True).start()

    def checker(self):
        return InstagramProfileChecker(url=self.url, home=None, timeout=5)

    def close(self):
        self.srv.shutdown()
        self.srv.server_close()


class InstagramCheckerOverHttp(unittest.TestCase):
    def test_full_http_path(self):
        fake = FakeInstagram()
        try:
            c = fake.checker()
            self.assertEqual(c.check("taken").status, TAKEN)
            self.assertEqual(c.check("zqnothere").status, FREE)
            r = c.check("limited")
            self.assertEqual((r.status, r.retry_after), (LIMITED, 7.0))
            self.assertEqual(c.check("wall").status, LIMITED)  # 200 ama giriş duvarı
            first = fake.seen[0]
            self.assertEqual(first["app_id"], "936619743392459")
            self.assertIn("Mozilla", first["ua"])
            self.assertIn("/taken/", first["referer"])
        finally:
            fake.close()

    def test_network_error_is_error_not_crash(self):
        c = InstagramProfileChecker(url="http://127.0.0.1:1/x", home=None, timeout=2)
        r = c.check("abc")
        self.assertEqual(r.status, "error")
        self.assertIn("ağ hatası", r.detail)

    def test_selftest_ok_and_blocked(self):
        from avci.selftest import run_selftest
        fake = FakeInstagram()
        try:
            ok, msg = run_selftest(fake.checker())
            self.assertTrue(ok, msg)
        finally:
            fake.close()
        blocked = FakeInstagram("all429")
        try:
            ok, msg = run_selftest(blocked.checker())
            self.assertFalse(ok)
            self.assertIn("429", msg)
            self.assertIn("hotspot", msg)
        finally:
            blocked.close()


class DiagnoseTests(unittest.TestCase):
    def test_report_has_raw_answers_and_verdict(self):
        from avci.diagnose import build_report, probe_instagram
        fake = FakeInstagram()
        try:
            raws, sel = probe_instagram(fake.checker())
        finally:
            fake.close()
        rep = build_report(tmpdb(), None, sel, ["12:00:00  deneme olayı"], raws)
        for needle in ("AVCI TANI RAPORU", "[Havuz]", "BAŞARILI", "http: 404", "http: 200",
                       "decision: free", "decision: taken", "deneme olayı", "şifre"):
            self.assertIn(needle, rep)

    def test_report_explains_blocked_connection(self):
        from avci.diagnose import build_report, probe_instagram
        fake = FakeInstagram("all429")
        try:
            raws, sel = probe_instagram(fake.checker())
        finally:
            fake.close()
        rep = build_report(tmpdb(), None, sel, None, raws)
        self.assertIn("BAŞARISIZ", rep)
        self.assertIn("429", rep)
        self.assertIn("'Retry-After': '7'", rep)

    def test_controller_diag_and_persistent_log(self):
        from avci.webui import Controller
        fake = FakeInstagram()
        try:
            c = Controller(os.path.join(tempfile.mkdtemp(), "d.db"), demo=False)
            c.checker = fake.checker()
            c.log("kalıcı kayıt denemesi")
            rep = c.diag()
        finally:
            fake.close()
        self.assertIn("kalıcı kayıt denemesi", rep)  # kayıt dosyasından da okunur
        self.assertTrue(os.path.exists(c.log_path))
        self.assertIn("BAŞARILI", rep)

    def test_purpose_document_is_shipped(self):
        doc = open(os.path.join(ROOT, "AMAC.md"), encoding="utf-8").read()
        for needle in ("Bu araç ne için yazıldı", "python -m avci tani", "checkers.py", "TEST EDİLEMEDİ"):
            self.assertIn(needle, doc)


class StoreScale(unittest.TestCase):
    def test_lease_is_fast_on_big_pool(self):
        st = tmpdb()
        st.add_many([(f"n{i}", "t", i % 100) for i in range(60000)])
        t0 = time.time()
        for _ in range(50):
            self.assertIsNotNone(st.lease())
        self.assertLess(time.time() - t0, 1.5)  # eskiden her sorgu tüm tabloyu sıralıyordu
        self.assertEqual(st.stats()["toplam"], 60000)


if __name__ == "__main__":
    unittest.main()
