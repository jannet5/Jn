"""İş dağıtımı, bağımlılık aktarımı, yeniden deneme, engelleme ve sonuç toplama."""
import json
import os
import sys
import tempfile
import unittest

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
sys.path.insert(0, os.path.join(ROOT, "src"))
os.environ["PYTHONPATH"] = os.path.join(ROOT, "src") + os.pathsep + os.environ.get("PYTHONPATH", "")

from company_os.cli import expand_placeholders, main as cli_main  # noqa: E402
from company_os.ledger import Ledger  # noqa: E402
from company_os.orchestrator import publish, run_plan  # noqa: E402

PY = sys.executable


def cmd_task(tid, code, outputs, deps=(), check=None, **kw):
    t = {"id": tid, "title": tid, "role": kw.pop("role", "impl"), "depends_on": list(deps),
         "executor": {"type": "command", "argv": [PY, "-c", code], "outputs": outputs},
         "acceptance": [{"argv": [PY, "-c", check or f"open({outputs[0]!r}).read()"], "refs": outputs}]}
    t.update(kw)
    return t


class Orkestrasyon(unittest.TestCase):
    def setUp(self):
        self.d = tempfile.mkdtemp()
        self.db = os.path.join(self.d, "l.db")
        self.ws = os.path.join(self.d, "ws")

    def test_ornek_plan_uctan_uca(self):
        plan_path = os.path.join(ROOT, "ornekler", "plan-ornek.json")
        out = os.path.join(self.d, "sonuc")
        rc = cli_main(["run", plan_path, "--db", self.db, "--out", out, "--workers", "3",
                       "--report", os.path.join(self.d, "r.md")])
        self.assertEqual(rc, 0)
        L = Ledger(self.db, self.ws)
        self.assertEqual(L.task("kararlilik")["attempt"], 2)
        rapor = open(os.path.join(out, "rapor", "RAPOR.md"), encoding="utf-8").read()
        for s in ("## Envanter", "## MCP", "kod-inceleme", "## Girdi kanıtları", "Envanter hazır"):
            self.assertIn(s, rapor)
        manifest = json.load(open(os.path.join(out, "MANIFEST.json")))
        self.assertEqual(len(manifest), 5)

    def test_paralel_dagitim_ve_girdi_aktarimi(self):
        plan = {"tasks": [
            cmd_task("a", "open('a.txt','w').write('A')", ["a.txt"]),
            cmd_task("b", "open('b.txt','w').write('B')", ["b.txt"]),
            cmd_task("c", "import json;m=json.load(open('inputs/manifest.json'));"
                          "open('c.txt','w').write(open('inputs/a/a.txt').read()+open('inputs/b/b.txt').read()"
                          "+str(sorted(m['dependencies'])))", ["c.txt"], deps=["a", "b"]),
        ]}
        rep = run_plan(plan, self.db, self.ws, workers=2, ttl=10)
        self.assertTrue(rep["all_succeeded"], rep)
        L = Ledger(self.db, self.ws)
        self.assertEqual(L.read_artifact("c", "c.txt"), b"AB['a', 'b']")
        claimed = [e["detail"]["owner"] for e in L.events() if e["kind"] == "claimed"]
        self.assertEqual(len(claimed), 3)
        m = publish(L, os.path.join(self.d, "out"))
        self.assertEqual(set(m), {"a/a.txt", "b/b.txt", "c/c.txt"})

    def test_kabul_basarisizsa_deneme_hakki_biter_bagimli_engellenir(self):
        plan = {"tasks": [
            cmd_task("kotu", "open('x.txt','w').write('x')", ["x.txt"], check="raise SystemExit(1)",
                     max_attempts=2),
            cmd_task("sonra", "open('y.txt','w').write('y')", ["y.txt"], deps=["kotu"]),
        ]}
        rep = run_plan(plan, self.db, self.ws, workers=2, ttl=10)
        st = {t["id"]: (t["state"], t["attempt"]) for t in rep["tasks"]}
        self.assertEqual(st, {"kotu": ("failed", 2), "sonra": ("blocked", 0)})
        self.assertFalse(rep["all_succeeded"])
        # manuel yeniden deneme yolu
        L = Ledger(self.db, self.ws)
        L.transition("kotu", "ready", reason="test")
        self.assertEqual(L.task("kotu")["attempt"], 0)

    def test_dongu_ve_bilinmeyen_bagimlilik_reddedilir(self):
        L = Ledger(self.db, self.ws)
        from company_os.ledger import LedgerError
        with self.assertRaises(LedgerError):
            L.load_plan({"tasks": [{"id": "a", "depends_on": ["b"]}, {"id": "b", "depends_on": ["a"]}]})
        with self.assertRaises(LedgerError):
            L.load_plan({"tasks": [{"id": "a", "depends_on": ["yok"]}]})

    def test_yer_tutucu(self):
        self.assertEqual(expand_placeholders({"a": ["{plan_dir}/x"]}, "/p"), {"a": ["/p/x"]})


if __name__ == "__main__":
    unittest.main()
