"""Ölçek kabulü: 1000 SENTETİK gönderi → 1000 analiz → 4000 görsel → doğrulama → bitti-mi.
Codex yerine tests/fake_codex.py (test taklidi) kullanılır; amaç 1000×4 hacimde boru hattının
(kaldığı yerden devam, limit, doğrulama, sayım) doğru çalıştığını kanıtlamaktır. Gerçek içerik değildir."""
import json, os, subprocess, sys, time
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT)); sys.path.insert(0, str(ROOT / "tests"))
from test_viralforge import seed_posts
from viralforge.store import Workspace

ws_dir = Path(sys.argv[1])
ws = Workspace(ws_dir).ensure()
t0 = time.time()
if not any(ws.iter_posts()):
    seed_posts(ws, 1000, side=256)
env = {**os.environ, "PYTHONPATH": str(ROOT), "VF_CODEX_BIN": str(ROOT / "tests" / "fake_codex.py")}
def vf(*a):
    r = subprocess.run([sys.executable, "-m", "viralforge", "-w", str(ws_dir), *a], capture_output=True, text=True, encoding="utf-8", env=env)
    return r
steps = {}
steps["select"] = json.loads(vf("select", "--top", "1000").stdout)
r = vf("analyze", "--jobs", "16"); steps["analyze"] = json.loads(r.stdout[r.stdout.rfind("{\n"):])
# Limit simülasyonu: 1500 üretimden sonra "usage limit" → dur, sonra devam
env["FAKE_CODEX_COUNTER"] = str(ws_dir / "cnt"); env["FAKE_CODEX_LIMIT_AFTER"] = "1500"
r = vf("generate", "--jobs", "16"); steps["generate_1"] = json.loads(r.stdout[r.stdout.rfind("{\n"):])
env.pop("FAKE_CODEX_LIMIT_AFTER")
r = vf("generate", "--jobs", "16"); steps["generate_2_resume"] = json.loads(r.stdout[r.stdout.rfind("{\n"):])
steps["verify"] = json.loads(vf("verify").stdout)
steps["report"] = vf("report").stdout.strip()
steps["status"] = json.loads(vf("status").stdout)
r = vf("bitti-mi"); steps["bitti_mi"] = {"exit": r.returncode, "answer": r.stdout.strip()}
pngs = list(ws_dir.glob("posts/*/variants/v*.png"))
steps["png_files_on_disk"] = len(pngs)
steps["seconds"] = round(time.time() - t0, 1)
for k in ("generate_1", "generate_2_resume", "analyze"):
    steps[k].pop("failures", None)
print(json.dumps(steps, ensure_ascii=False, indent=2))
