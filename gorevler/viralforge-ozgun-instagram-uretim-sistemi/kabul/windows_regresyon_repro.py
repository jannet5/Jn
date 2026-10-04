"""Windows hatalarının Linux karşılığı: (1) çalıştırma izni olmayan, boşluklu yoldaki .py taklidi
(Windows CreateProcess .py'yi zaten çalıştıramaz → WinError 193; Linux'ta izin yok → PermissionError);
(2) UTF-8 olmayan locale'de CLI çıktısı."""
import os, shutil, stat, subprocess, sys, tempfile
from pathlib import Path
root = Path(sys.argv[1])
sys.path.insert(0, str(root)); sys.path.insert(0, str(root / "tests"))
from test_viralforge import seed_posts
from viralforge import brain, select
from viralforge.store import Workspace
tmp = Path(tempfile.mkdtemp()) / "Program Files" / "fake codex"
tmp.mkdir(parents=True)
fake = tmp / "fake_codex.py"
shutil.copy(root / "tests" / "fake_codex.py", fake)
os.chmod(fake, stat.S_IRUSR | stat.S_IWUSR)
os.environ["VF_CODEX_BIN"] = str(fake)
ws = Workspace(tmp.parent / "ws").ensure()
seed_posts(ws, 2); select.select_top(ws, top=2)
try:
    r = brain.analyze_all(ws, log=print)
    print("analyze:", {k: v for k, v in r.items() if k != "failures"})
except Exception as e:
    print("ANALYZE HATASI:", type(e).__name__, e)
env = {k: v for k, v in os.environ.items() if not k.startswith(("LC_", "LANG", "PYTHONIOENCODING"))}
env.update(LC_ALL="C", PYTHONUTF8="0", PYTHONCOERCECLOCALE="0", PYTHONPATH=str(root))
r = subprocess.run([sys.executable, "-m", "viralforge", "-w", str(ws.root), "bitti-mi", "--target", "5"],
                   capture_output=True, env=env)
try:
    print("CLI exit", r.returncode, "stdout:", r.stdout.decode("utf-8").strip()[:120])
except UnicodeDecodeError as e:
    print("CLI çıktısı UTF-8 değil:", e)
print("CLI stderr son:", r.stderr.decode("utf-8", "replace").strip().splitlines()[-1:] )
