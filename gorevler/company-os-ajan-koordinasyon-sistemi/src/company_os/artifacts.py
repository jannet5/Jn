"""Kök dizin içinde sembolik bağ / junction yönlendirmesine dayanıklı dosya yazma.

Denetim bulgusu 5 (artifacts.py:18): eski ``write_bundle`` yolu önce kontrol edip
sonra ``write_text`` ile açıyordu (TOCTOU). Burada:

* POSIX: kökten başlayarak her dizin bileşeni ``O_NOFOLLOW|O_DIRECTORY`` ile
  *tutamak (fd) üzerinden* açılır (openat zinciri). Kontrol ile kullanım aynı
  tutamak olduğu için arada bir bileşenin symlink'e çevrilmesi yazmayı köke
  dışına taşıyamaz.
* Geçici dosya ``O_CREAT|O_EXCL|O_NOFOLLOW`` ile oluşturulur: önceden konmuş bir
  symlink varsa açma başarısız olur.
* Son ad ``os.replace(src_dir_fd=, dst_dir_fd=)`` ile atomik değiştirilir;
  rename symlink'i izlemez, hedefteki symlink'in yerine dosyanın kendisi geçer.
* Windows'ta ``dir_fd`` desteklenmez. Taşınabilir yolda her bileşen
  ``lstat`` ile symlink/reparse-point açısından yazmadan önce ve sonra
  denetlenir, geçici dosya ``O_EXCL`` ile açılır. Bu yol kalan küçük bir yarış
  penceresini kapatamaz; ``SECURE_DIR_FD`` False ise rapor bunu söyler.
"""
from __future__ import annotations

import hashlib
import os
import secrets
import stat

SECURE_DIR_FD = (
    os.open in os.supports_dir_fd
    and os.mkdir in os.supports_dir_fd
    and os.rename in os.supports_dir_fd  # os.replace aynı renameat'i kullanır
    and hasattr(os, "O_NOFOLLOW")
    and hasattr(os, "O_DIRECTORY")
)

_FILE_ATTRIBUTE_REPARSE_POINT = 0x400
MAX_ARTIFACT_BYTES = 50 * 1024 * 1024


class UnsafePathError(ValueError):
    """Göreli yol kök dışına çıkıyor ya da yol üzerinde symlink/junction var."""


def split_relpath(relpath: str) -> list[str]:
    if not isinstance(relpath, str) or not relpath:
        raise UnsafePathError("boş yol")
    norm = relpath.replace("\\", "/")
    if norm.startswith("/") or (len(norm) > 1 and norm[1] == ":"):
        raise UnsafePathError(f"mutlak yol kabul edilmez: {relpath!r}")
    parts = [p for p in norm.split("/") if p not in ("", ".")]
    if not parts:
        raise UnsafePathError(f"geçersiz yol: {relpath!r}")
    for p in parts:
        if p == ".." or "\x00" in p or ":" in p:
            raise UnsafePathError(f"geçersiz bileşen {p!r}: {relpath!r}")
    return parts


def sha256_bytes(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def _is_link_like(st: os.stat_result) -> bool:
    if stat.S_ISLNK(st.st_mode):
        return True
    attrs = getattr(st, "st_file_attributes", 0)
    return bool(attrs & _FILE_ATTRIBUTE_REPARSE_POINT)


class SafeRoot:
    """Yazma ve okumaların kesinlikle içinde kaldığı kök dizin."""

    def __init__(self, root: str, *, force_portable: bool = False):
        os.makedirs(root, exist_ok=True)
        self.root = os.path.realpath(root)
        self.secure = SECURE_DIR_FD and not force_portable

    # ---------------- POSIX: tutamak zinciri ----------------
    def _open_dirs(self, parts: list[str], create: bool) -> int:
        flags = os.O_RDONLY | os.O_DIRECTORY | os.O_NOFOLLOW
        fd = os.open(self.root, os.O_RDONLY | os.O_DIRECTORY)
        try:
            for p in parts:
                try:
                    nfd = os.open(p, flags, dir_fd=fd)
                except FileNotFoundError:
                    if not create:
                        raise
                    try:
                        os.mkdir(p, 0o755, dir_fd=fd)
                    except FileExistsError:
                        pass
                    try:
                        nfd = os.open(p, flags, dir_fd=fd)
                    except OSError as e:
                        raise UnsafePathError(f"dizin bileşeni güvensiz: {p!r} ({e})") from e
                except OSError as e:  # ELOOP (symlink), ENOTDIR ...
                    raise UnsafePathError(f"dizin bileşeni güvensiz: {p!r} ({e})") from e
                os.close(fd)
                fd = nfd
            return fd
        except BaseException:
            os.close(fd)
            raise

    def _write_secure(self, parts: list[str], data: bytes) -> None:
        dfd = self._open_dirs(parts[:-1], create=True)
        name = parts[-1]
        tmp = f".{name}.{secrets.token_hex(8)}.tmp"
        try:
            try:
                ffd = os.open(
                    tmp,
                    os.O_WRONLY | os.O_CREAT | os.O_EXCL | os.O_NOFOLLOW,
                    0o644,
                    dir_fd=dfd,
                )
            except FileExistsError as e:
                raise UnsafePathError(f"geçici ad önceden var: {tmp}") from e
            try:
                view = memoryview(data)
                while view:
                    n = os.write(ffd, view)
                    view = view[n:]
                os.fsync(ffd)
            finally:
                os.close(ffd)
            try:
                st = os.stat(name, dir_fd=dfd, follow_symlinks=False)
                if stat.S_ISDIR(st.st_mode):
                    raise UnsafePathError(f"hedef bir dizin: {name}")
            except FileNotFoundError:
                pass
            os.replace(tmp, name, src_dir_fd=dfd, dst_dir_fd=dfd)
            tmp = None
            try:
                os.fsync(dfd)
            except OSError:
                pass
        finally:
            if tmp is not None:
                try:
                    os.unlink(tmp, dir_fd=dfd)
                except OSError:
                    pass
            os.close(dfd)

    def _read_secure(self, parts: list[str]) -> bytes:
        dfd = self._open_dirs(parts[:-1], create=False)
        try:
            try:
                ffd = os.open(parts[-1], os.O_RDONLY | os.O_NOFOLLOW, dir_fd=dfd)
            except OSError as e:
                if isinstance(e, FileNotFoundError):
                    raise
                raise UnsafePathError(f"dosya güvensiz: {parts[-1]!r} ({e})") from e
        finally:
            os.close(dfd)
        return self._read_fd(ffd)

    # ---------------- Taşınabilir (Windows) yol ----------------
    def _check_components(self, parts: list[str], create: bool) -> str:
        cur = self.root
        for p in parts:
            cur = os.path.join(cur, p)
            try:
                st = os.lstat(cur)
            except FileNotFoundError:
                if not create:
                    raise
                os.mkdir(cur, 0o755)
                st = os.lstat(cur)
            if _is_link_like(st) or not stat.S_ISDIR(st.st_mode):
                raise UnsafePathError(f"dizin bileşeni güvensiz: {cur}")
        real = os.path.realpath(cur)
        if os.path.commonpath([real, self.root]) != self.root:
            raise UnsafePathError(f"kök dışına çıkıyor: {cur}")
        return cur

    def _write_portable(self, parts: list[str], data: bytes) -> None:
        d = self._check_components(parts[:-1], create=True)
        final = os.path.join(d, parts[-1])
        tmp = os.path.join(d, f".{parts[-1]}.{secrets.token_hex(8)}.tmp")
        flags = os.O_WRONLY | os.O_CREAT | os.O_EXCL | getattr(os, "O_BINARY", 0)
        flags |= getattr(os, "O_NOFOLLOW", 0)
        ffd = os.open(tmp, flags, 0o644)
        try:
            with os.fdopen(ffd, "wb", closefd=True) as fh:
                fh.write(data)
                fh.flush()
                os.fsync(fh.fileno())
            # yazdıktan sonra yeniden denetle (yarış penceresini daraltır)
            self._check_components(parts[:-1], create=False)
            if _is_link_like(os.lstat(tmp)):
                raise UnsafePathError("geçici dosya yer değiştirdi")
            if os.path.isdir(final) and not os.path.islink(final):
                raise UnsafePathError(f"hedef bir dizin: {final}")
            os.replace(tmp, final)
            tmp = None
        finally:
            if tmp is not None and os.path.lexists(tmp):
                os.unlink(tmp)

    def _read_portable(self, parts: list[str]) -> bytes:
        d = self._check_components(parts[:-1], create=False)
        path = os.path.join(d, parts[-1])
        if _is_link_like(os.lstat(path)):
            raise UnsafePathError(f"dosya symlink: {path}")
        flags = os.O_RDONLY | getattr(os, "O_BINARY", 0) | getattr(os, "O_NOFOLLOW", 0)
        return self._read_fd(os.open(path, flags))

    @staticmethod
    def _read_fd(ffd: int) -> bytes:
        try:
            st = os.fstat(ffd)
            if not stat.S_ISREG(st.st_mode):
                raise UnsafePathError("normal dosya değil")
            if st.st_size > MAX_ARTIFACT_BYTES:
                raise UnsafePathError("artifact boyut sınırını aşıyor")
            chunks = []
            while True:
                b = os.read(ffd, 1 << 20)
                if not b:
                    break
                chunks.append(b)
            return b"".join(chunks)
        finally:
            os.close(ffd)

    # ---------------- Genel API ----------------
    def write_bytes(self, relpath: str, data: bytes) -> str:
        if len(data) > MAX_ARTIFACT_BYTES:
            raise UnsafePathError("artifact boyut sınırını aşıyor")
        parts = split_relpath(relpath)
        if self.secure:
            self._write_secure(parts, data)
        else:
            self._write_portable(parts, data)
        return sha256_bytes(data)

    def read_bytes(self, relpath: str) -> bytes:
        parts = split_relpath(relpath)
        return self._read_secure(parts) if self.secure else self._read_portable(parts)

    def sha256(self, relpath: str) -> str:
        return sha256_bytes(self.read_bytes(relpath))

    def abspath(self, relpath: str) -> str:
        return os.path.join(self.root, *split_relpath(relpath))


def write_bundle(root: str, files: dict[str, str | bytes]) -> dict[str, str]:
    """Eski API ile uyumlu: birden çok dosyayı güvenli yazar, {yol: sha256} döner."""
    sr = SafeRoot(root)
    out = {}
    for rel, content in files.items():
        data = content.encode("utf-8") if isinstance(content, str) else content
        out[rel] = sr.write_bytes(rel, data)
    return out
