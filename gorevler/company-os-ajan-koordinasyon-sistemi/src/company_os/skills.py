"""Agent Skills (``<dizin>/<ad>/SKILL.md``) keşfi ve göreve göre seçimi.

Biçim Claude Code / anthropics/skills ile aynıdır: YAML ön bilgide en az
``name`` ve ``description``. Aşamalı açıklama ilkesine uygun olarak listede
yalnız ad+açıklama döner; gövde ``read()`` ile istenince yüklenir.
"""
from __future__ import annotations

import os
import re
from dataclasses import dataclass

DEFAULT_DIRS = (".claude/skills", "skills")
_WORD = re.compile(r"[\wçğıöşüÇĞİÖŞÜ]{3,}", re.UNICODE)


@dataclass
class Skill:
    name: str
    description: str
    path: str
    meta: dict

    def read(self) -> str:
        with open(self.path, encoding="utf-8") as fh:
            return _split_frontmatter(fh.read())[1]

    def as_dict(self) -> dict:
        return {"name": self.name, "description": self.description, "path": self.path}


def _split_frontmatter(text: str) -> tuple[dict, str]:
    if not text.startswith("---"):
        return {}, text
    end = text.find("\n---", 3)
    if end < 0:
        return {}, text
    meta: dict = {}
    key = None
    for line in text[3:end].splitlines():
        if not line.strip() or line.lstrip().startswith("#"):
            continue
        m = re.match(r"^([A-Za-z0-9_-]+):\s*(.*)$", line)
        if m:
            key, val = m.group(1), m.group(2).strip()
            if val in (">", "|", ">-", "|-"):
                meta[key] = ""
            else:
                meta[key] = val.strip("'\"")
        elif key is not None:  # çok satırlı değer
            meta[key] = (meta[key] + " " + line.strip()).strip()
    body = text[end + 4:].lstrip("\n")
    return meta, body


def discover(dirs) -> list[Skill]:
    found: dict[str, Skill] = {}
    for d in dirs:
        d = os.path.expanduser(d)
        if not os.path.isdir(d):
            continue
        for entry in sorted(os.listdir(d)):
            p = os.path.join(d, entry, "SKILL.md")
            if not os.path.isfile(p):
                continue
            with open(p, encoding="utf-8") as fh:
                meta, _ = _split_frontmatter(fh.read())
            name = meta.get("name") or entry
            if name not in found:  # ilk dizin önceliklidir (proje > kullanıcı)
                found[name] = Skill(name, meta.get("description", ""), p, meta)
    return list(found.values())


def _words(s: str) -> set[str]:
    return {w.lower() for w in _WORD.findall(s or "")}


def select(skills: list[Skill], *, names=(), text: str = "", limit: int = 3) -> list[Skill]:
    """Açıkça adlandırılanlar + görev metniyle açıklaması en çok örtüşenler."""
    by_name = {s.name: s for s in skills}
    chosen = [by_name[n] for n in names if n in by_name]
    if text and len(chosen) < limit:
        tw = _words(text)
        scored = []
        for s in skills:
            if s in chosen:
                continue
            score = len(tw & _words(s.name.replace("-", " ") + " " + s.description))
            if score >= 2:
                scored.append((score, s.name, s))
        scored.sort(key=lambda x: (-x[0], x[1]))
        chosen += [s for _, _, s in scored[: limit - len(chosen)]]
    return chosen
