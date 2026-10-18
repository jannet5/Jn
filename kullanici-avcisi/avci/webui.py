"""Tıklanan ekran: python -m avci ui  (tarayıcıda açılır, yalnızca bu bilgisayardan erişilir)."""
import hashlib
import json
import os
import re
import secrets
import threading
import time
import webbrowser
from collections import deque
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

from . import wordlists as wl
from .candidates import from_combos, from_names, from_pattern, from_words
from .checkers import FREE, TAKEN, Checker, InstagramProfileChecker, Result
from .diagnose import build_report, probe_instagram
from .engine import Engine
from .notify import Notifier
from .selftest import run_selftest
from .store import Store
from .translit import to_latin

HERE = os.path.dirname(os.path.abspath(__file__))
SPEEDS = {"yavas": 0.2, "normal": 0.4, "hizli": 0.8}


class DemoChecker(Checker):
    """Gerçek Instagram'sız deneme: isimlerin ~%2'si 'boş' çıkar. Bilinen hesap dolu."""
    name = "demo"

    def check(self, username: str) -> Result:
        time.sleep(0.03)
        if username == "instagram":
            return Result(TAKEN, "demo dolu")
        if username.startswith("zq"):  # bağlantı testinin uydurma ismi
            return Result(FREE, "demo boş")
        h = int(hashlib.sha1(username.encode()).hexdigest(), 16)
        return Result(FREE, "demo boş") if h % 50 == 0 else Result(TAKEN, "demo dolu")


class Controller:
    def __init__(self, db_path: str, demo: bool = False):
        self.store = Store(db_path)
        self.demo = demo
        self.checker: Checker = DemoChecker() if demo else InstagramProfileChecker()
        base = os.path.dirname(os.path.abspath(db_path))
        self.log_path = os.path.join(base, "avci.log")
        self.notifier = Notifier(found_file=os.path.join(base, "bulunanlar.txt"), bell=False)
        self.engine = None
        self.thread = None
        self.started_at = None
        self.logs = deque(maxlen=40)
        self.selftest = {"ok": None, "msg": "Henüz test edilmedi."}
        self.lock = threading.Lock()
        self.busy = ""

    # --- log ---------------------------------------------------------------
    def log(self, msg: str):
        line = f"{time.strftime('%H:%M:%S')}  {msg}"
        self.logs.append(line)
        try:  # kalıcı kayıt: pencere kapansa bile ne olduğu kalsın
            with open(self.log_path, "a", encoding="utf-8") as f:
                f.write(f"{time.strftime('%Y-%m-%d')} {line}\n")
        except OSError:
            pass

    def diag(self) -> str:
        raws = None
        if isinstance(self.checker, InstagramProfileChecker):
            raws, st = probe_instagram(self.checker)
            self.selftest = {"ok": st[0], "msg": st[1]}
        sel = (self.selftest["ok"], self.selftest["msg"]) if self.selftest["ok"] is not None else None
        return build_report(self.store, self.log_path, sel, list(self.logs), raws, self.demo)

    # --- işlemler ------------------------------------------------------------
    def running(self) -> bool:
        return bool(self.thread and self.thread.is_alive())

    def do_selftest(self):
        ok, msg = run_selftest(self.checker)
        self.selftest = {"ok": ok, "msg": msg}
        self.log(("✓ " if ok else "✗ ") + msg)
        return self.selftest

    def start(self, speed: str = "normal", minutes=None, force: bool = False):
        with self.lock:
            if self.running():
                return {"ok": False, "msg": "Zaten çalışıyor."}
            if self.store.stats()["toplam"] == 0:
                return {"ok": False, "msg": "Önce isim ekle ya da havuz kur (aday yok)."}
            if self.selftest["ok"] is not True and not force:
                self.do_selftest()
                if not self.selftest["ok"]:
                    return {"ok": False, "needs_force": True, "msg": self.selftest["msg"]}
            rps = SPEEDS.get(speed, 0.4)
            self.engine = Engine(self.store, self.checker, self.notifier, rps=rps,
                                 workers=1 if rps <= 0.4 else 2, log=self.log,
                                 hot_seconds=60 if self.demo else 120,
                                 confirm_gap=0.3 if self.demo else 3.0)
            if self.demo:
                self.engine.pacer.base = self.engine.pacer.cur = 0.05
            mins = float(minutes) if minutes else None
            self.thread = threading.Thread(target=self._run, args=(mins,), daemon=True)
            self.started_at = time.time()
            self.thread.start()
            self.log(f"▶ Başladı (hız: {speed}{', ' + str(mins) + ' dk' if mins else ', sürekli'}).")
            return {"ok": True, "msg": "Başladı."}

    def _run(self, mins):
        try:
            self.engine.run(minutes=mins)
        finally:
            self.log("■ Durdu.")

    def stop(self):
        if self.engine:
            self.engine.stop.set()
        if self.thread:
            self.thread.join(timeout=8)
        return {"ok": True, "msg": "Durduruldu."}

    def add_names(self, text: str, digits: bool, min_len: int, max_len: int):
        tokens = [t for t in re.split(r"[\s,;]+", text) if t.strip()]
        rows, cands = [], []
        for t in tokens[:500]:
            t = t.strip().lstrip("@")
            variants = to_latin(t, digits=digits) if not t.isascii() else [re.sub(r"[^a-z0-9]", "", t.lower())]
            variants = [v for v in variants if min_len <= len(v) <= max_len and (digits or v.isalpha())]
            rows.append({"girdi": t, "uygun": variants[:12], "toplam": len(variants)})
            cands += list(from_names(variants))
        added = self.store.add_many(cands, hot=True)
        self.log(f"Sıcak listeye {added} yeni isim eklendi.")
        return {"ok": True, "added": added, "rows": rows}

    def build(self, langs, combos, cvcv, min_len, max_len, digits, big):
        report = []
        total = 0
        for lang in langs:
            if big and not wl.has_big_list(lang):
                ok, msg = wl.fetch_lang(lang)
                report.append(msg)
            files = [f for f in wl.local_files(lang) if big or not f.endswith("_50k.txt")]
            if not files:
                report.append(f"{lang}: hazır liste yok, 'büyük liste' kutusunu işaretle.")
                continue
            for p in files:
                n = self.store.add_many(from_words(p, min_len, max_len, digits))
                total += n
                report.append(f"{os.path.basename(p)}: +{n} aday")
        for c in combos:
            n = self.store.add_many(from_combos(c, digits))
            total += n
            report.append(f"tüm {c} harfliler: +{n} aday")
        if cvcv:
            n = self.store.add_many(from_pattern("CVCV"))
            total += n
            report.append(f"okunur 4 harf (CVCV): +{n} aday")
        self.log(f"Havuz güncellendi: +{total} aday.")
        return {"ok": True, "added": total, "report": report}

    def state(self):
        s = self.store.stats()
        c = dict(self.engine.counters) if self.engine else {}
        found = [{"name": r["name"], "length": r["length"],
                  "at": time.strftime("%d.%m %H:%M", time.localtime(r["last_checked"] or time.time()))}
                 for r in self.store.found()]
        return {
            "running": self.running(), "stats": s, "counters": c, "logs": list(self.logs)[-12:],
            "found": found, "selftest": self.selftest, "demo": self.demo,
            "elapsed": int(time.time() - self.started_at) if self.started_at and self.running() else 0,
            "next": self.store.top_pending(8),
            "langs": [{"code": k, "name": v, "big": wl.has_big_list(k), "seed": bool(wl.local_files(k))}
                      for k, v in wl.LANG_NAMES.items()],
        }


def make_handler(ctrl: Controller, token: str, port: int):
    with open(os.path.join(HERE, "ui.html"), encoding="utf-8") as fh:
        index = fh.read().replace("__TOKEN__", token)
    hosts = {f"127.0.0.1:{port}", f"localhost:{port}"}

    class H(BaseHTTPRequestHandler):
        def log_message(self, *a):
            pass

        def _send(self, code, body, ctype="application/json; charset=utf-8"):
            data = body if isinstance(body, bytes) else body.encode("utf-8")
            self.send_response(code)
            self.send_header("Content-Type", ctype)
            self.send_header("Content-Length", str(len(data)))
            self.send_header("Cache-Control", "no-store")
            self.end_headers()
            self.wfile.write(data)

        def _guard(self):
            if self.headers.get("Host") not in hosts:
                self._send(403, '{"ok":false,"msg":"yasak"}')
                return False
            return True

        def do_GET(self):
            if not self._guard():
                return
            if self.path.split("?")[0] == "/":
                return self._send(200, index, "text/html; charset=utf-8")
            if self.path == "/favicon.ico":
                self.send_response(204)
                self.end_headers()
                return
            if self.path == "/api/state" and self.headers.get("X-Token") == token:
                return self._send(200, json.dumps(ctrl.state(), ensure_ascii=False))
            self._send(404, '{"ok":false}')

        def do_POST(self):
            if not self._guard():
                return
            if self.headers.get("X-Token") != token:
                return self._send(403, '{"ok":false,"msg":"anahtar yanlış"}')
            try:
                n = int(self.headers.get("Content-Length") or 0)
                body = json.loads(self.rfile.read(n) or b"{}")
                res = self.route(self.path, body)
            except Exception as e:  # ekran kapanmasın, hata mesajı gelsin
                res = {"ok": False, "msg": f"Hata: {type(e).__name__}: {e}"}
            self._send(200, json.dumps(res, ensure_ascii=False))

        def route(self, path, b):
            if path == "/api/start":
                return ctrl.start(b.get("speed", "normal"), b.get("minutes"), bool(b.get("force")))
            if path == "/api/stop":
                return ctrl.stop()
            if path == "/api/selftest":
                return ctrl.do_selftest()
            if path == "/api/diag":
                return {"ok": True, "report": ctrl.diag()}
            if path == "/api/add":
                return ctrl.add_names(b.get("text", ""), bool(b.get("digits")),
                                      int(b.get("min", 3)), int(b.get("max", 7)))
            if path == "/api/build":
                return ctrl.build(b.get("langs", []), [int(x) for x in b.get("combos", [])],
                                  bool(b.get("cvcv")), int(b.get("min", 3)), int(b.get("max", 7)),
                                  bool(b.get("digits")), bool(b.get("big")))
            return {"ok": False, "msg": "bilinmeyen işlem"}

    return H


def create_server(db_path="avci.db", port=8765, demo=False):
    """Sunucuyu kurar (başlatmaz). (httpd, ctrl, token, url) döner."""
    ctrl = Controller(db_path, demo=demo)
    token = secrets.token_urlsafe(16)
    for p in range(port, port + 20):
        try:
            httpd = ThreadingHTTPServer(("127.0.0.1", p), make_handler(ctrl, token, p))
            return httpd, ctrl, token, f"http://127.0.0.1:{p}/"
        except OSError:
            continue
    raise SystemExit("Boş port bulunamadı.")


def serve(db_path="avci.db", port=8765, demo=False, open_browser=True):
    httpd, ctrl, token, url = create_server(db_path, port, demo)
    print(f"Ekran hazır: {url}\nKapatmak için bu pencerede Ctrl+C bas.", flush=True)
    if open_browser:
        threading.Timer(0.8, lambda: webbrowser.open(url)).start()
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nKapatılıyor...")
    finally:
        ctrl.stop()
        httpd.server_close()
    return httpd, ctrl, url
