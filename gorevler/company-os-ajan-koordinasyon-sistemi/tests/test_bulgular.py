"""Denetim raporundaki beş P1 bulgunun davranışsal regresyon testleri.

Her test, eski koddaki hatanın *yeniden üretilebilir senaryosunu* bu
uygulamaya karşı çalıştırır ve artık reddedildiğini doğrular.
"""
import os
import sys
import tempfile
import threading
import time
import unittest

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "src"))

from company_os import artifacts as art  # noqa: E402
from company_os.artifacts import SafeRoot, UnsafePathError  # noqa: E402
from company_os.ledger import (GateFailed, InvalidTransition, Ledger, LedgerError,  # noqa: E402
                               LeaseLost)
from company_os.qa import acceptance_gate  # noqa: E402

PY = sys.executable
CHECK = [PY, "-c", "import sys; sys.exit(0 if open('out.txt').read() else 1)"]


class Clock:
    def __init__(self, t=1000.0):
        self.t = t
        self.lock = threading.Lock()

    def __call__(self):
        with self.lock:
            return self.t

    def advance(self, s):
        with self.lock:
            self.t += s


class Base(unittest.TestCase):
    def setUp(self):
        self.dir = tempfile.mkdtemp()
        self.db = os.path.join(self.dir, "l.db")
        self.ws = os.path.join(self.dir, "ws")
        self.clock = Clock()
        self.L = self.ledger()

    def ledger(self):
        return Ledger(self.db, self.ws, clock=self.clock)


class B1_ClaimTekGiris(Base):
    """Bulgu 1: transition() ready→running ile claim() atlanabiliyordu."""

    def test_transition_running_reddedilir(self):
        self.L.add_task("t", "T", "impl")
        with self.assertRaises(InvalidTransition):
            self.L.transition("t", "running")
        with self.assertRaises(InvalidTransition):
            self.L.transition("t", "succeeded")
        self.assertEqual(self.L.task("t")["state"], "ready")
        self.assertIsNone(self.L.task("t")["lease_token"])

    def test_ayri_baglantilarla_esz_amanli_claim_tek_kazanan(self):
        self.L.add_task("t", "T", "impl")
        wins, barrier = [], threading.Barrier(12)

        def go(i):
            L = Ledger(self.db, self.ws)  # ayrı SQLite bağlantısı
            barrier.wait()
            lease = L.claim(f"w{i}", task_id="t")
            if lease:
                wins.append(lease)
            L.close()

        ts = [threading.Thread(target=go, args=(i,)) for i in range(12)]
        [t.start() for t in ts]
        [t.join() for t in ts]
        self.assertEqual(len(wins), 1)
        t = self.L.task("t")
        self.assertEqual((t["state"], t["attempt"], t["lease_token"]), ("running", 1, wins[0].token))


class B2_EskiWorker(Base):
    """Bulgu 2: süresi dolmuş worker, yerine geçen denemeye artifact/kanıt yazıp
    görevi başarılı işaretleyebiliyordu."""

    def _expire_and_reclaim(self):
        self.L.add_task("t", "T", "impl", max_attempts=3)
        old = self.L.claim("eski", ttl=10)
        self.L.put_artifact(old, "out.txt", "eski içerik")
        self.clock.advance(11)
        self.assertEqual(self.L.reconcile()["retried"], ["t"])
        new = self.L.claim("yeni", ttl=10)
        self.assertGreater(new.token, old.token)
        return old, new

    def test_eski_worker_hicbir_mutasyon_yapamaz(self):
        old, new = self._expire_and_reclaim()
        for fn in (lambda: self.L.put_artifact(old, "out.txt", "SAHTE"),
                   lambda: self.L.register_artifact(old, "out.txt"),
                   lambda: self.L.run_evidence(old, CHECK, ["out.txt"]),
                   lambda: self.L.complete(old, "sahte başarı"),
                   lambda: self.L.fail(old, "x"),
                   lambda: self.L.heartbeat(old),
                   lambda: self.L.post_message("eski", "*", "x", lease=old)):
            with self.assertRaises(LeaseLost):
                fn()
        t = self.L.task("t")
        self.assertEqual((t["state"], t["lease_token"], t["attempt"]), ("running", new.token, 2))
        self.assertEqual(self.L.artifacts("t"), [])  # yeni denemeye sızma yok
        self.assertEqual(self.L.evidence("t"), [])
        rejected = [e for e in self.L.events("t") if e["kind"] == "stale_write_rejected"]
        self.assertGreaterEqual(len(rejected), 7)

    def test_reconcile_edilmemis_suresi_dolmus_lease_de_reddedilir(self):
        self.L.add_task("t", "T", "impl")
        lease = self.L.claim("w", ttl=5)
        self.clock.advance(6)
        with self.assertRaises(LeaseLost):
            self.L.put_artifact(lease, "out.txt", "geç")

    def test_yeni_worker_normal_tamamlar(self):
        old, new = self._expire_and_reclaim()
        self.L.put_artifact(new, "out.txt", "yeni")
        self.L.run_evidence(new, CHECK, ["out.txt"])
        rep = self.L.complete(new, "ok")
        self.assertTrue(rep["passed"])
        self.assertEqual(rep["snapshot"]["out.txt"], art.sha256_bytes(b"yeni"))
        self.assertEqual(self.L.read_artifact("t", "out.txt"), b"yeni")


class B3_Reconcile(Base):
    """Bulgu 3: reconcile okuma ile güncelleme arasında başka reconciler görevi
    yeniden alırsa, ilki yeni canlı lease'i iptal ediyordu."""

    def test_reconcile_canli_yeni_lease_e_dokunmaz(self):
        self.L.add_task("t", "T", "impl")
        self.L.claim("w1", ttl=5)
        self.clock.advance(6)
        other = {}

        class SlowLedger(Ledger):
            hooked = False

            def _event(s, c, task_id, kind, **d):
                # İlk reconciler satırı okuduktan sonra, işlem açıkken ikinci
                # bağlantı reconcile+claim yapmaya çalışır.
                if kind == "lease_expired" and not SlowLedger.hooked:
                    SlowLedger.hooked = True

                    def rival():
                        t0 = time.time()
                        L2 = Ledger(self.db, self.ws, clock=self.clock)
                        L2.reconcile()
                        other["lease"] = L2.claim("w2", ttl=100)
                        other["waited"] = time.time() - t0
                        L2.close()
                    th = threading.Thread(target=rival)
                    th.start()
                    other["thread"] = th
                    time.sleep(0.5)
                return super()._event(c, task_id, kind, **d)

        R = SlowLedger(self.db, self.ws, clock=self.clock)
        R.reconcile()
        other["thread"].join()
        R.reconcile()  # rakibin yeni lease'inden sonra tekrar reconcile: dokunmamalı
        lease2 = other["lease"]
        self.assertIsNotNone(lease2)
        self.assertGreaterEqual(other["waited"], 0.4, "rakip yazma kilidini beklemeli")
        t = self.L.task("t")
        self.assertEqual((t["state"], t["lease_token"]), ("running", lease2.token))
        self.L.put_artifact(lease2, "out.txt", "canlı")  # lease hâlâ geçerli

    def test_esz_amanli_reconcile_claim_stres(self):
        """Gerçek saatle kısa TTL; birçok iş parçacığı reconcile/claim/complete yapar.
        Değişmez: her görev tam bir kez succeeded olur, kabul edilen tüm yazmalar
        o anki token'a aittir."""
        for i in range(15):
            self.L.add_task(f"t{i}", f"T{i}", "impl", max_attempts=50)
        errors = []

        def worker(n):
            L = Ledger(self.db, self.ws)
            try:
                for _ in range(400):
                    L.reconcile()
                    lease = L.claim(f"w{n}", ttl=0.05)
                    if lease is None:
                        if L.status()["counts"]["succeeded"] == 15:
                            return
                        continue
                    try:
                        if n % 2:
                            time.sleep(0.06)  # bazıları lease'i kaçırır
                        L.put_artifact(lease, "out.txt", f"{n}")
                        L.run_evidence(lease, CHECK, ["out.txt"])
                        L.complete(lease, f"w{n}")
                    except (LeaseLost, LedgerError):
                        pass
            except Exception as e:  # pragma: no cover
                errors.append(repr(e))
            finally:
                L.close()

        ts = [threading.Thread(target=worker, args=(n,)) for n in range(6)]
        [t.start() for t in ts]
        [t.join() for t in ts]
        self.assertEqual(errors, [])
        for t in self.L.tasks():
            self.assertEqual(t["state"], "succeeded", t)
            arts = self.L.artifacts(t["id"])
            ev = self.L.evidence(t["id"])
            succ = [e for e in self.L.events(t["id"]) if e["kind"] == "succeeded"]
            self.assertEqual(len(succ), 1)
            tok = succ[0]["detail"]["token"]
            self.assertTrue(all(a["token"] == tok for a in arts))
            self.assertTrue(all(e["token"] == tok for e in ev))


class B4_KanitBaglama(Base):
    """Bulgu 4: acceptance_gate boş haritaları ve ilgisiz artifact'a ait eşleşen
    haritaları kabul ediyordu."""

    def setUp(self):
        super().setUp()
        self.L.add_task("t", "T", "impl", max_attempts=5)
        self.lease = self.L.claim("w", ttl=100)

    def test_bos_snapshot_reddedilir(self):
        with self.assertRaises(GateFailed) as cm:
            self.L.complete(self.lease)
        self.assertTrue(any("boş snapshot" in r for r in cm.exception.reasons))

    def test_kanitsiz_artifact_reddedilir(self):
        self.L.put_artifact(self.lease, "out.txt", "x")
        with self.assertRaises(GateFailed) as cm:
            self.L.complete(self.lease)
        self.assertTrue(any("kanıt yok" in r for r in cm.exception.reasons))

    def test_eski_deneme_kaniti_sayilmaz(self):
        self.L.put_artifact(self.lease, "out.txt", "x")
        self.L.run_evidence(self.lease, CHECK, ["out.txt"])
        self.clock.advance(101)
        self.L.reconcile()
        new = self.L.claim("w2", ttl=100)
        self.L.put_artifact(new, "out.txt", "x")  # aynı içerik, aynı hash
        rep = acceptance_gate(self.L, "t")
        self.assertFalse(rep["passed"])
        self.assertTrue(any("kanıt yok" in r for r in rep["reasons"]))

    def test_kanittan_sonra_artifact_degisirse_reddedilir(self):
        self.L.put_artifact(self.lease, "out.txt", "v1")
        self.L.run_evidence(self.lease, CHECK, ["out.txt"])
        self.L.put_artifact(self.lease, "out.txt", "v2")  # kayıtlı yeniden yazım
        with self.assertRaises(GateFailed) as cm:
            self.L.complete(self.lease)
        self.assertTrue(any("eşleşmiyor" in r for r in cm.exception.reasons))

    def test_diskte_gizlice_degisen_artifact_reddedilir(self):
        self.L.put_artifact(self.lease, "out.txt", "v1")
        self.L.run_evidence(self.lease, CHECK, ["out.txt"])
        path = self.L.store.abspath(self.L.attempt_dir("t", 1) + "/out.txt")
        with open(path, "w") as fh:
            fh.write("kurcalandı")
        rep = acceptance_gate(self.L, "t")
        self.assertFalse(rep["passed"])
        self.assertTrue(any("diskteki hash" in r for r in rep["reasons"]))

    def test_kapsanmayan_artifact_ve_basarisiz_kanit(self):
        self.L.put_artifact(self.lease, "out.txt", "x")
        self.L.put_artifact(self.lease, "ek.txt", "y")
        self.L.run_evidence(self.lease, CHECK, ["out.txt"])
        self.L.run_evidence(self.lease, [PY, "-c", "raise SystemExit(3)"], ["out.txt"])
        rep = acceptance_gate(self.L, "t")
        self.assertFalse(rep["passed"])
        self.assertTrue(any("ek.txt" in r and "kapsamıyor" in r for r in rep["reasons"]))
        self.assertTrue(any("çıkış kodu 3" in r for r in rep["reasons"]))

    def test_baska_gorevin_artifact_ina_referans_verilemez(self):
        self.L.add_task("u", "U", "impl")
        other = self.L.claim("w9", task_id="u", ttl=100)
        self.L.put_artifact(other, "yabanci.txt", "z")
        with self.assertRaises(LedgerError):
            self.L.run_evidence(self.lease, CHECK, ["yabanci.txt"])

    def test_dogru_akis_gecer(self):
        self.L.put_artifact(self.lease, "out.txt", "x")
        self.L.run_evidence(self.lease, CHECK, ["out.txt"])
        self.assertTrue(self.L.complete(self.lease)["passed"])
        self.assertEqual(self.L.task("t")["state"], "succeeded")


class B5_GuvenliYazma(unittest.TestCase):
    """Bulgu 5: yol kontrolü ile yazma arasında symlink/junction yarışı."""

    portable = False

    def setUp(self):
        base = tempfile.mkdtemp()
        self.root = os.path.join(base, "root")
        self.outside = os.path.join(base, "disari")
        os.makedirs(self.root)
        os.makedirs(self.outside)
        self.sr = SafeRoot(self.root, force_portable=self.portable)

    def test_gecersiz_yollar(self):
        for p in ("../x", "/etc/passwd", "a/../../x", "C:/x", "", "a/\x00"):
            with self.assertRaises(UnsafePathError):
                self.sr.write_bytes(p, b"x")

    def test_ust_dizin_symlink_ise_reddedilir(self):
        os.symlink(self.outside, os.path.join(self.root, "d"))
        with self.assertRaises(UnsafePathError):
            self.sr.write_bytes("d/f.txt", b"x")
        self.assertEqual(os.listdir(self.outside), [])

    def test_hedefteki_symlink_takip_edilmez(self):
        victim = os.path.join(self.outside, "victim.txt")
        with open(victim, "w") as fh:
            fh.write("orijinal")
        os.symlink(victim, os.path.join(self.root, "f.txt"))
        self.sr.write_bytes("f.txt", b"yeni")
        with open(victim) as fh:
            self.assertEqual(fh.read(), "orijinal")
        self.assertFalse(os.path.islink(os.path.join(self.root, "f.txt")))
        self.assertEqual(self.sr.read_bytes("f.txt"), b"yeni")

    def test_symlink_uzerinden_okuma_reddedilir(self):
        with open(os.path.join(self.outside, "gizli"), "w") as fh:
            fh.write("s")
        os.symlink(os.path.join(self.outside, "gizli"), os.path.join(self.root, "g"))
        with self.assertRaises(UnsafePathError):
            self.sr.read_bytes("g")

    def test_yaris_kontrol_sonrasi_ust_dizin_degistirilse_bile_disari_yazilmaz(self):
        if self.portable:
            self.skipTest("tutamak zinciri yalnız POSIX yolunda")
        os.makedirs(os.path.join(self.root, "d"))
        real_open = os.open
        swapped = {}

        def evil_open(path, flags, *a, **kw):
            fd = real_open(path, flags, *a, **kw)
            if path == "d" and not swapped:  # dizin tutamağı alındıktan hemen sonra saldırı
                swapped["x"] = True
                os.rename(os.path.join(self.root, "d"), os.path.join(self.root, "d.orig"))
                os.symlink(self.outside, os.path.join(self.root, "d"))
            return fd

        art.os.open = evil_open
        try:
            self.sr.write_bytes("d/f.txt", b"x")
        finally:
            art.os.open = real_open
        self.assertTrue(swapped)
        self.assertEqual(os.listdir(self.outside), [])
        self.assertTrue(os.path.exists(os.path.join(self.root, "d.orig", "f.txt")))


@unittest.skipUnless(hasattr(os, "symlink"), "symlink yok")
class B5_TasinabilirYol(B5_GuvenliYazma):
    """Windows yolu (dir_fd yok) Linux üzerinde zorla çalıştırılır."""
    portable = True


if __name__ == "__main__":
    unittest.main()
