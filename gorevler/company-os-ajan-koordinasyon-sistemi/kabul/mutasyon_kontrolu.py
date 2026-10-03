"""Negatif kontrol: denetimdeki her hatayı koda geri enjekte eder ve ilgili
regresyon testlerinin bunu YAKALADIĞINI (başarısız olduğunu) gösterir.
Bu, testlerin yalnız 'yeşil' olmadığının, hatayı gerçekten ayırt ettiğinin kanıtıdır.
"""
import os
import sys
import unittest

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.insert(0, os.path.join(ROOT, "src"))
sys.path.insert(0, ROOT)

from company_os import artifacts, ledger, qa  # noqa: E402
from tests import test_bulgular as T  # noqa: E402


def run(cls):
    r = unittest.TextTestRunner(stream=open(os.devnull, "w"), verbosity=0).run(
        unittest.defaultTestLoader.loadTestsFromTestCase(cls))
    return r.testsRun, len(r.failures) + len(r.errors)


def mut1():  # transition() ready→running'e izin ver (claim'i atla)
    ledger.ADMIN_TRANSITIONS.add(("ready", "running"))
    ledger.ADMIN_TRANSITIONS.add(("running", "succeeded"))
    return lambda: (ledger.ADMIN_TRANSITIONS.discard(("ready", "running")),
                    ledger.ADMIN_TRANSITIONS.discard(("running", "succeeded")))


def mut2():  # fencing kontrolünü kaldır
    orig = ledger.Ledger._check_lease
    ledger.Ledger._check_lease = lambda self, c, lease: c.execute(
        "SELECT * FROM tasks WHERE id=?", (lease.task_id,)).fetchone()
    return lambda: setattr(ledger.Ledger, "_check_lease", orig)


def mut3():  # reconcile: koşulsuz güncelleme (token/süre karşılaştırması yok)
    orig = ledger.Ledger.reconcile

    def bad(self):
        out = {"retried": [], "blocked": []}
        rows = self.conn.execute("SELECT id FROM tasks WHERE state='running' AND lease_expires<=?",
                                 (self.clock(),)).fetchall()
        for r in rows:  # okuma ile yazma ayrı, koşulsuz
            self._event(self.conn, r["id"], "lease_expired")
            self.conn.execute("UPDATE tasks SET state='ready', lease_token=NULL, lease_expires=NULL "
                              "WHERE id=?", (r["id"],))
            out["retried"].append(r["id"])
        return out
    ledger.Ledger.reconcile = bad
    return lambda: setattr(ledger.Ledger, "reconcile", orig)


def mut4():  # kapı: kanıtı snapshot'a bağlama, her şeyi geçir
    orig = qa.acceptance_gate
    fake = lambda L, tid, conn=None: {"passed": True, "reasons": [], "snapshot": {}}  # noqa: E731
    qa.acceptance_gate = fake
    T.acceptance_gate = fake
    return lambda: (setattr(qa, "acceptance_gate", orig), setattr(T, "acceptance_gate", orig))


def mut5():  # güvenli yazma yerine kontrol-sonra-yaz (eski write_bundle)
    orig = artifacts.SafeRoot.write_bytes

    def naive(self, rel, data):
        p = os.path.join(self.root, *artifacts.split_relpath(rel))
        os.makedirs(os.path.dirname(p), exist_ok=True)
        with open(p, "wb") as fh:
            fh.write(data)
        return artifacts.sha256_bytes(data)
    artifacts.SafeRoot.write_bytes = naive
    return lambda: setattr(artifacts.SafeRoot, "write_bytes", orig)


CASES = [("Bulgu 1 claim atlatma", mut1, T.B1_ClaimTekGiris),
         ("Bulgu 2 fencing yok", mut2, T.B2_EskiWorker),
         ("Bulgu 3 koşulsuz reconcile", mut3, T.B3_Reconcile),
         ("Bulgu 4 bağlanmamış kanıt", mut4, T.B4_KanitBaglama),
         ("Bulgu 5 TOCTOU yazma", mut5, T.B5_GuvenliYazma)]

ok = True
for name, mut, cls in CASES:
    base_n, base_f = run(cls)
    undo = mut()
    try:
        n, f = run(cls)
    finally:
        undo()
    caught = f > 0 and base_f == 0
    ok &= caught
    print(f"{name:28s} düzgün kod: {base_n - base_f}/{base_n} geçti | hata enjekte: {f}/{n} test BAŞARISIZ "
          f"→ {'YAKALANDI' if caught else 'KAÇTI'}")
print("MUTASYON KONTROLÜ:", "GEÇTİ" if ok else "BAŞARISIZ")
sys.exit(0 if ok else 1)
