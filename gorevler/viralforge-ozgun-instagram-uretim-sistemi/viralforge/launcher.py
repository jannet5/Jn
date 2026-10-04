"""Dış komut (Codex, yt-dlp, ffmpeg) başlatıcı — Windows ve POSIX için tek çözümleyici.

Neden gerekli:
- Windows CreateProcess `.py`/`.js` dosyasını doğrudan çalıştıramaz (WinError 193); shebang okunmaz.
  → `.py` aktif yorumlayıcıyla (sys.executable), `.js`/`.mjs`/`.cjs` node ile başlatılır.
- npm global kurulumu `codex.cmd` üretir. `.cmd`/`.bat` CreateProcess tarafından cmd.exe ile yorumlanır ve
  argüman kaçışı güvenilir değildir (BatBadBut, CVE-2024-24576; Node CVE-2024-27980). → cmd-shim dosyası
  okunup hedef `codex.js` bulunur ve `node codex.js ...` olarak shell'siz başlatılır.
  Hedef çözülemezse cmd.exe yolu yalnızca argümanlarda cmd özel karakteri yoksa kullanılır.
- Alt süreç metni her zaman açıkça UTF-8 kodlanır/çözülür; konsol/locale kodlamasına (cp1254/cp1252) bağlı değildir.
shell=True hiçbir yolda kullanılmaz.
"""
from __future__ import annotations

import os
import re
import shutil
import subprocess
import sys
from dataclasses import dataclass, field
from pathlib import Path

IS_WINDOWS = os.name == "nt"
SCRIPT_PY = {".py", ".pyw"}
SCRIPT_JS = {".js", ".mjs", ".cjs"}
BATCH = {".cmd", ".bat"}
# Batch yolunda HER argüman çift tırnak içine alınır; tırnak içinde cmd.exe'nin hâlâ yorumladığı
# karakterler (% ! " satır sonu) varsa çağrı reddedilir. & | < > ( ) ^ tırnak içinde literaldir.
CMD_META = re.compile(r'[%!"\r\n]')
_SHIM_TARGET = re.compile(r'"%~?dp0%?\\([^"]+?\.(?:js|mjs|cjs))"', re.IGNORECASE)


class LaunchError(RuntimeError):
    """Komut bulunamadı/başlatılamadı; kullanıcıya gösterilecek açık mesaj taşır."""


@dataclass
class Launcher:
    name: str
    argv: list[str]                      # dış komutun önüne eklenecek önek (ör. [node, codex.js])
    kind: str                            # exe | python | node | npm-shim | batch
    source: str                          # nereden çözüldü (VF_CODEX_BIN, PATH, ...)
    notes: list[str] = field(default_factory=list)

    def command(self, args: list[str]) -> list[str] | str:
        if self.kind == "batch":
            bad = [a for a in (*self.argv, *args) if CMD_META.search(a)]
            if bad:
                raise LaunchError(
                    f"{self.name}: .cmd/.bat başlatıcısı cmd.exe özel karakterli argümanla güvenli çağrılamaz "
                    f"({bad[0]!r}). Çözüm: Codex'i npm ile kurun (cmd-shim çözülür) veya VF_CODEX_BIN'i codex.exe'ye yönlendirin.")
            comspec = os.environ.get("COMSPEC") or str(Path(os.environ.get("SystemRoot", r"C:\Windows")) / "System32" / "cmd.exe")
            # Tek komut satırı dizgesi CreateProcess'e olduğu gibi gider; /s en dıştaki tırnak çiftini kaldırır.
            inner = " ".join(f'"{a}"' for a in (*self.argv, *args))
            return f'"{comspec}" /d /s /c "{inner}"'

        return [*self.argv, *args]


def _node(near: Path | None = None) -> str:
    if near is not None:
        for cand in (("node.exe",) if IS_WINDOWS else ("node",)):
            p = near / cand
            if p.is_file():
                return str(p)
    found = shutil.which("node")
    if not found:
        raise LaunchError("node bulunamadı: .js tabanlı Codex için Node.js 18+ gerekli (https://nodejs.org/).")
    return found


def parse_cmd_shim(path: Path) -> Path | None:
    """npm cmd-shim (.cmd) içindeki hedef JS yolunu çözer; bulamazsa None."""
    try:
        text = path.read_text(encoding="utf-8", errors="replace")
    except OSError:
        return None
    m = _SHIM_TARGET.search(text)
    if not m:
        return None
    target = (path.parent / Path(m.group(1).replace("\\", "/"))).resolve()
    return target if target.is_file() else None


WINDOWS_RUNNABLE = {".exe", ".com"} | BATCH | SCRIPT_PY | SCRIPT_JS


def from_path(path: str | Path, name: str, source: str) -> Launcher:
    p = Path(path)
    if not p.is_file():
        raise LaunchError(f"{name}: dosya yok: {p}")
    if IS_WINDOWS and p.suffix.lower() not in WINDOWS_RUNNABLE:
        # npm, %APPDATA%\npm içine uzantısız bir sh betiği (codex) ve codex.ps1 de koyar; CreateProcess bunları
        # çalıştıramaz (WinError 193). Yanındaki codex.exe / codex.cmd tercih edilir.
        for ext in (".exe", ".cmd", ".bat"):
            sib = p.with_suffix(ext)
            if sib.is_file():
                return from_path(sib, name, source + f" ({p.name} yerine {sib.name})")
        raise LaunchError(f"{name}: {p} Windows'ta çalıştırılabilir değil (.exe/.cmd değil); "
                          "VF_CODEX_BIN'i codex.cmd veya codex.exe tam yoluna ayarlayın.")
    ext = p.suffix.lower()
    if ext in SCRIPT_PY:
        return Launcher(name, [sys.executable, str(p)], "python", source)
    if ext in SCRIPT_JS:
        return Launcher(name, [_node(p.parent), str(p)], "node", source)
    if ext in BATCH:
        target = parse_cmd_shim(p)
        if target is not None:
            return Launcher(name, [_node(p.parent), str(target)], "npm-shim", source,
                            [f"{p.name} → {target} (cmd.exe atlanır)"])
        return Launcher(name, [str(p)], "batch", source,
                        ["cmd-shim hedefi çözülemedi; cmd.exe yolu yalnız güvenli argümanlarla"])
    if not IS_WINDOWS and ext == "" and p.is_file() and not os.access(p, os.X_OK):
        # POSIX'te çalıştırma izni olmayan betik: shebang'e göre yorumlayıcı seç.
        head = p.read_bytes()[:128]
        if head.startswith(b"#!") and b"python" in head.split(b"\n", 1)[0]:
            return Launcher(name, [sys.executable, str(p)], "python", source)
        if head.startswith(b"#!") and b"node" in head.split(b"\n", 1)[0]:
            return Launcher(name, [_node(p.parent), str(p)], "node", source)
    return Launcher(name, [str(p)], "exe", source)


def resolve(name: str, env_var: str | None = None) -> Launcher:
    """Önce ortam değişkeni (tam yol; boşluk içerebilir, BÖLÜNMEZ), sonra PATH (Windows'ta PATHEXT ile)."""
    override = (os.environ.get(env_var) or "").strip().strip('"').strip("'") if env_var else ""
    if override:
        if os.sep in override or "/" in override or Path(override).is_file():
            return from_path(override, name, env_var)
        name_to_find = override
    else:
        name_to_find = name
    found = shutil.which(name_to_find)
    if not found and IS_WINDOWS:
        for ext in (".exe", ".cmd", ".bat"):
            found = shutil.which(name_to_find + ext)
            if found:
                break
    if not found:
        raise LaunchError(f"{name} bulunamadı (PATH'te yok{', ' + env_var + ' boş' if env_var else ''}).")
    return from_path(found, name, env_var + "→PATH" if override else "PATH")


def utf8_env(env: dict | None = None) -> dict:
    """Python alt süreçlerinin stdin/stdout/stderr'ini UTF-8'e zorlar (konsol kod sayfasından bağımsız)."""
    env = dict(os.environ if env is None else env)
    env["PYTHONUTF8"] = "1"
    env["PYTHONIOENCODING"] = "utf-8"
    return env


def run(launcher: Launcher, args: list[str], *, input_text: str | None = None, timeout: float | None = None,
        env: dict | None = None, cwd: str | Path | None = None) -> subprocess.CompletedProcess:
    """UTF-8 metin G/Ç ile, shell'siz çalıştırır. Başlatma hatası LaunchError'a, zaman aşımı
    returncode=124 olan sonuca çevrilir (çağıran kontrollü işler)."""
    cmd = launcher.command(args)
    try:
        return subprocess.run(cmd, input=input_text, capture_output=True, text=True, encoding="utf-8",
                              errors="replace", timeout=timeout, env=utf8_env(env), cwd=cwd, shell=False)
    except FileNotFoundError as e:
        raise LaunchError(f"{launcher.name} başlatılamadı (bulunamadı): {launcher.argv[0]} — {e}") from e
    except PermissionError as e:
        raise LaunchError(f"{launcher.name} başlatılamadı (izin): {launcher.argv[0]} — {e}") from e
    except OSError as e:  # Windows: WinError 193 vb.
        raise LaunchError(f"{launcher.name} başlatılamadı: {launcher.argv[0]} — {e}") from e
    except subprocess.TimeoutExpired as e:
        out = e.stdout.decode("utf-8", "replace") if isinstance(e.stdout, bytes) else (e.stdout or "")
        return subprocess.CompletedProcess(cmd, 124, out, f"zaman aşımı ({timeout} sn)")
