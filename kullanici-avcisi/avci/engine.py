"""Motor: sürekli kontrol eder, yavaşlama isteğine uyar, bulunanı doğrular ve duyurur."""
import random
import sys
import threading
import time
from typing import Optional

from .checkers import ERROR, FREE, LIMITED, TAKEN, Checker
from .notify import Notifier
from .store import Store


class Pacer:
    """Tüm iş parçacıkları için ortak hız ayarı. 429 gelince yavaşlar, sakinleşince hızlanır."""

    def __init__(self, rps: float):
        self.base = 1.0 / max(rps, 0.001)
        self.cur = self.base
        self.next = 0.0
        self.cooldown_until = 0.0
        self.streak = 0
        self.lock = threading.Lock()

    def wait(self, stop: threading.Event):
        with self.lock:
            now = time.monotonic()
            start = max(now, self.next, self.cooldown_until)
            self.next = start + self.cur * random.uniform(0.8, 1.3)
        delay = start - now
        if delay > 0:
            stop.wait(delay)

    def limited(self, retry_after: Optional[float]) -> float:
        with self.lock:
            self.streak += 1
            self.cur = min(self.cur * 1.5, 60.0)
            cool = retry_after if retry_after else min(30.0 * 2 ** (self.streak - 1), 1800.0)
            self.cooldown_until = max(self.cooldown_until, time.monotonic() + cool)
            return cool

    def ok(self):
        with self.lock:
            self.streak = 0
            self.cur = max(self.base, self.cur * 0.97)


class Engine:
    def __init__(self, store: Store, checker: Checker, notifier: Notifier, rps: float = 0.5,
                 workers: int = 2, cool_hours: float = 6.0, hot_seconds: float = 120.0,
                 confirm: int = 2, confirm_gap: float = 3.0, quiet: bool = False):
        self.store, self.checker, self.notifier = store, checker, notifier
        self.pacer = Pacer(rps)
        self.workers = workers
        self.cool = cool_hours * 3600
        self.hot = hot_seconds
        self.confirm = confirm
        self.confirm_gap = confirm_gap
        self.quiet = quiet
        self.stop = threading.Event()
        self.counters = {"kontrol": 0, "alinmis": 0, "bulunan": 0, "yavaslama": 0, "hata": 0}
        self.clock = threading.Lock()
        self.found_names = []
        self._last_limit_log = 0.0

    # ------------------------------------------------------------------
    def _count(self, key: str, n: int = 1):
        with self.clock:
            self.counters[key] += n

    def _interval(self, hot: bool) -> float:
        base = self.hot if hot else self.cool
        return base * random.uniform(0.9, 1.2)

    def _is_hot(self, name: str) -> bool:
        with self.store.lock:
            r = self.store.db.execute("SELECT hot FROM names WHERE name=?", (name,)).fetchone()
        return bool(r and r[0])

    def _confirm(self, name: str):
        """Aday bulununca 'confirm' kez daha, aralıklı bak. Hepsi FREE ise sağlam. LIMITED ise None."""
        for _ in range(self.confirm):
            if self.stop.wait(self.confirm_gap):
                return None
            self.pacer.wait(self.stop)
            r = self.checker.check(name)
            self._count("kontrol")
            if r.status == LIMITED:
                self.pacer.limited(r.retry_after)
                return None
            if r.status != FREE:
                return r
            self.pacer.ok()
        return True

    def _work_one(self, name: str):
        self.pacer.wait(self.stop)
        if self.stop.is_set():
            self.store.release_lease(name, 0)
            return
        r = self.checker.check(name)
        self._count("kontrol")
        now = time.time()
        if r.status == TAKEN:
            self.pacer.ok()
            self._count("alinmis")
            self.store.record(name, "taken", now + self._interval(self._is_hot(name)))
        elif r.status == FREE:
            self.pacer.ok()
            verdict = self._confirm(name)
            if verdict is True:
                self.store.record(name, "found", now, note=r.detail)
                self.store.event(name, "found", r.detail)
                self._count("bulunan")
                self.found_names.append(name)
                self.notifier.found(name, r.detail)
            elif verdict is None:  # doğrulama sırasında yavaşlama/durdurma: kısa süre sonra yeniden bak
                self.store.set_hot(name, True)
                self.store.record(name, "pending", now + 20, note="doğrulama bekliyor", count_check=False)
            else:  # doğrulamada dolu çıktı: geçici görüntüymüş
                self._count("alinmis")
                self.store.record(name, "taken", now + self._interval(True), note="geçici boş görünüm")
                self.store.event(name, "flap", "ilk bakışta boş, doğrulamada dolu")
        elif r.status == LIMITED:
            cool = self.pacer.limited(r.retry_after)
            self._count("yavaslama")
            self.store.release_lease(name, now)  # bekleme zaten Pacer'da
            if time.monotonic() - self._last_limit_log > 15:  # ekranı doldurmasın
                self._last_limit_log = time.monotonic()
                self._log(f"⚠ Instagram yavaşla dedi ({r.detail}). {cool:.1f} sn bekliyorum, hızı düşürüyorum.")
        else:
            self._count("hata")
            self.store.release_lease(name, now + 30)

    def _worker(self, deadline: Optional[float]):
        idle_logged = False
        while not self.stop.is_set():
            if deadline and time.monotonic() >= deadline:
                break
            name = self.store.lease()
            if name is None:
                if not idle_logged:
                    self._log("Sırada iş yok; yeni tekrar kontrol zamanı bekleniyor...")
                    idle_logged = True
                self.stop.wait(0.5)
                continue
            idle_logged = False
            try:
                self._work_one(name)
            except Exception as e:  # tek isim yüzünden sistem durmasın
                self._count("hata")
                self.store.release_lease(name, time.time() + 60)
                self._log(f"hata ({name}): {type(e).__name__}: {e}")

    def _log(self, msg: str):
        if not self.quiet:
            print(msg, flush=True)

    def _progress(self, deadline: Optional[float], every: float = 10.0):
        t0 = time.monotonic()
        while not self.stop.wait(every):
            s = self.store.stats()
            el = time.monotonic() - t0
            c = self.counters
            self._log(
                f"[{el/60:5.1f} dk] kontrol {c['kontrol']} (≈{c['kontrol']/max(el,1):.2f}/sn) | "
                f"bekleyen {s['bekleyen']} | dolu {s['alinmis']} | BULUNAN {s['bulunan']} | "
                f"yavaşlama {c['yavaslama']}")

    def run(self, minutes: Optional[float] = None) -> dict:
        deadline = time.monotonic() + minutes * 60 if minutes else None
        threads = [threading.Thread(target=self._worker, args=(deadline,), daemon=True)
                   for _ in range(self.workers)]
        prog = threading.Thread(target=self._progress, args=(deadline,), daemon=True)
        for t in threads:
            t.start()
        prog.start()
        try:
            while any(t.is_alive() for t in threads):
                for t in threads:
                    t.join(timeout=0.5)
        except KeyboardInterrupt:
            self._log("\nDurduruluyor (Ctrl+C)... kayıtlar güvende, kaldığı yerden devam eder.")
        finally:
            self.stop.set()
            for t in threads:
                t.join(timeout=5)
        return dict(self.counters, bulunanlar=list(self.found_names))
