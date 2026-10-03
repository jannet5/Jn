"""MCP stdio sunucusu (gerçek alt süreç) ve Agent Skills keşfi."""
import json
import os
import subprocess
import sys
import tempfile
import unittest

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
SRC = os.path.join(ROOT, "src")
sys.path.insert(0, SRC)

from company_os import skills  # noqa: E402
from company_os.mcp_client import StdioMCPClient  # noqa: E402

PY = sys.executable
SKILLS = os.path.join(ROOT, "ornekler", "skills")


def server_argv(db):
    return [PY, "-m", "company_os", "mcp-serve", "--db", db, "--skills", SKILLS]


class MCPSunucu(unittest.TestCase):
    def setUp(self):
        self.d = tempfile.mkdtemp()
        self.db = os.path.join(self.d, "l.db")
        env = dict(os.environ, PYTHONPATH=SRC)
        self.cli = StdioMCPClient(server_argv(self.db), env=env, timeout=30)
        self.init = self.cli.initialize()

    def tearDown(self):
        self.cli.close()

    def call(self, name, **a):
        r = self.cli.call_tool(name, a)
        return r, r.get("structuredContent")

    def test_el_sikisma_ve_arac_listesi(self):
        self.assertEqual(self.init["protocolVersion"], "2025-06-18")
        self.assertIn("tools", self.init["capabilities"])
        names = {t["name"] for t in self.cli.list_tools()}
        self.assertTrue({"plan_submit", "task_claim", "artifact_write", "evidence_run",
                         "task_complete", "skills_list", "message_post", "reconcile"} <= names)

    def test_claude_ajan_dongusu_mcp_uzerinden(self):
        plan = {"tasks": [{"id": "t1", "title": "kod inceleme python güvenlik", "role": "inceleyici",
                           "instructions": "subprocess çağrılarını incele"},
                          {"id": "t2", "title": "rapor", "role": "yazar", "depends_on": ["t1"]}]}
        _, out = self.call("plan_submit", plan=plan)
        self.assertEqual(out["created"], ["t1", "t2"])
        _, c = self.call("task_claim", worker="claude-sub-1", role="inceleyici")
        self.assertTrue(c["claimed"])
        self.assertIn("kod-inceleme", [s["name"] for s in c["skills"]])
        tok = c["lease"]["token"]
        self.call("artifact_write", task_id="t1", token=tok, path="bulgu.md", content="# Bulgular\n")
        _, ev = self.call("evidence_run", task_id="t1", token=tok,
                          argv=[PY, "-c", "assert open('bulgu.md').read().startswith('# ')"], refs=["bulgu.md"])
        self.assertEqual(ev["exit_code"], 0)
        self.call("message_post", sender="inceleyici", recipient="yazar", body="t1 bitti", task_id="t1", token=tok)
        _, done = self.call("task_complete", task_id="t1", token=tok, summary="ok")
        self.assertTrue(done["succeeded"], done)
        # sahte/eski token reddedilir
        r, _ = self.call("artifact_write", task_id="t1", token=tok, path="x.md", content="sızma")
        self.assertTrue(r["isError"])
        self.assertIn("LeaseLost", r["content"][0]["text"])
        # bağımlı görev girdileri ve mesajları alır
        _, c2 = self.call("task_claim", worker="claude-sub-2")
        self.assertEqual(c2["task"]["id"], "t2")
        self.assertEqual(c2["dependencies"][0]["artifacts"][0]["path"], "bulgu.md")
        self.assertEqual(c2["messages"][0]["body"], "t1 bitti")
        _, art = self.call("artifact_read", task_id="t1", path="bulgu.md")
        self.assertEqual(art["content"], "# Bulgular\n")
        # kanıtsız tamamlama reddedilir
        _, bad = self.call("task_complete", task_id="t2", token=c2["lease"]["token"])
        self.assertFalse(bad["succeeded"])

    def test_bilinmeyen_yontem_ve_arac(self):
        from company_os.mcp_client import MCPError
        with self.assertRaises(MCPError):
            self.cli.request("yok/yontem")
        with self.assertRaises(MCPError):
            self.cli.call_tool("yok")


class MCPDurumsuz(unittest.TestCase):
    def test_handshake_olmadan_ve_yeni_surum(self):
        d = tempfile.mkdtemp()
        msgs = [
            {"jsonrpc": "2.0", "id": 1, "method": "tools/list", "params": {"_meta": {"protocolVersion": "2026-07-28"}}},
            {"jsonrpc": "2.0", "id": 2, "method": "initialize", "params": {"protocolVersion": "2026-07-28"}},
            {"jsonrpc": "2.0", "id": 3, "method": "ping"},
            "bozuk json",
        ]
        inp = "\n".join(m if isinstance(m, str) else json.dumps(m) for m in msgs) + "\n"
        p = subprocess.run(server_argv(os.path.join(d, "l.db")), input=inp, capture_output=True, text=True,
                           env=dict(os.environ, PYTHONPATH=SRC), timeout=30)
        out = [json.loads(line) for line in p.stdout.splitlines()]
        self.assertGreater(len(out[0]["result"]["tools"]), 10)
        self.assertEqual(out[1]["result"]["protocolVersion"], "2026-07-28")
        self.assertEqual(out[2]["result"], {})
        self.assertEqual(out[3]["error"]["code"], -32700)


class Skills(unittest.TestCase):
    def test_kesif_ve_secim(self):
        sk = skills.discover([SKILLS, "/yok"])
        self.assertEqual([s.name for s in sk], ["kod-inceleme", "teknik-rapor"])
        self.assertIn("Kod inceleme kontrol listesi", sk[0].read())
        sel = skills.select(sk, text="python kodunda subprocess güvenlik incelemesi")
        self.assertEqual([s.name for s in sel], ["kod-inceleme"])
        self.assertEqual([s.name for s in skills.select(sk, names=["teknik-rapor"])], ["teknik-rapor"])
        self.assertEqual(skills.select(sk, text="alakasız"), [])

    def test_cok_satirli_frontmatter(self):
        d = tempfile.mkdtemp()
        os.makedirs(os.path.join(d, "x"))
        with open(os.path.join(d, "x", "SKILL.md"), "w") as fh:
            fh.write("---\nname: x-skill\ndescription: >\n  birinci satır\n  ikinci satır\n---\ngövde\n")
        s = skills.discover([d])[0]
        self.assertEqual((s.name, s.description, s.read()), ("x-skill", "birinci satır ikinci satır", "gövde\n"))

    def test_kit_kurulumu(self):
        from company_os.cli import main
        d = tempfile.mkdtemp()
        self.assertEqual(main(["install-kit", d]), 0)
        cfg = json.load(open(os.path.join(d, ".mcp.json")))
        self.assertIn("company-os", cfg["mcpServers"])
        agents = os.listdir(os.path.join(d, ".claude", "agents"))
        self.assertGreaterEqual(len(agents), 4)
        self.assertTrue(os.path.exists(os.path.join(d, ".claude", "skills", "company-os-orkestrasyon", "SKILL.md")))


if __name__ == "__main__":
    unittest.main()
