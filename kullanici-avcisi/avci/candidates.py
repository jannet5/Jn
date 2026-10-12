"""Aday kullanıcı adı üretimi: kelime listesi (her dil), harf kombinasyonu, desen."""
import itertools
import os
import string
from typing import Iterable, Iterator, Tuple

from .translit import to_latin

LENGTH_BASE = {1: 130, 2: 120, 3: 100, 4: 80, 5: 60, 6: 40, 7: 25}
CONSONANTS = "bcdfghjklmnpqrstvwxyz"
VOWELS = "aeiou"

# (isim, kaynak, puan)
Candidate = Tuple[str, str, float]


def length_score(n: int) -> float:
    return LENGTH_BASE.get(n, max(5, 25 - (n - 7) * 5))


def iter_wordfile(path: str) -> Iterator[Tuple[str, int]]:
    """Satır başına bir kelime ya da 'kelime sıklık' biçimli dosya. (kelime, sıra) verir."""
    rank = 0
    with open(path, encoding="utf-8", errors="ignore") as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith("#"):
                continue
            rank += 1
            yield line.split()[0], rank


def from_words(path: str, min_len: int, max_len: int, digits: bool = False,
               tag: str = None) -> Iterator[Candidate]:
    """Kelime dosyasından aday üretir. Sık kullanılan kelimeler daha yüksek puan alır."""
    tag = tag or os.path.splitext(os.path.basename(path))[0]
    seen = set()
    for word, rank in iter_wordfile(path):
        bonus = max(0.0, 30.0 - rank / 200.0)  # ilk 6000 kelime bonus alır
        for latin in to_latin(word, digits=digits):
            if min_len <= len(latin) <= max_len and latin not in seen:
                seen.add(latin)
                yield latin, f"words:{tag}", length_score(len(latin)) + bonus + quality(latin)


def from_combos(length: int, digits: bool = False) -> Iterator[Candidate]:
    """Tüm harf kombinasyonları (3 harf: 17.576, 4 harf: 456.976 ...). Söylenebilir olanlar önde."""
    alphabet = string.ascii_lowercase + (string.digits if digits else "")
    for tup in itertools.product(alphabet, repeat=length):
        name = "".join(tup)
        yield name, f"combo:{length}", length_score(length) + pronounce_bonus(name)


def from_pattern(pattern: str) -> Iterator[Candidate]:
    """Desen: C=ünsüz V=ünlü L=herhangi harf D=rakam, diğer karakterler aynen. Örn. CVCV, CVCCV."""
    pools = []
    for ch in pattern.upper():
        pools.append({"C": CONSONANTS, "V": VOWELS, "L": string.ascii_lowercase,
                      "D": string.digits}.get(ch, ch.lower()))
    for tup in itertools.product(*pools):
        name = "".join(tup)
        yield name, f"pattern:{pattern.upper()}", length_score(len(name)) + pronounce_bonus(name)


def pronounce_bonus(name: str) -> float:
    """Ünlü-ünsüz dönüşümlü isimler (kali, rima) akılda kalıcıdır: puan ver."""
    if not name.isalpha():
        return 0.0
    kinds = [c in VOWELS for c in name]
    alternations = sum(1 for a, b in zip(kinds, kinds[1:]) if a != b)
    bonus = 10.0 * alternations / max(1, len(name) - 1)
    if any(a == b == "q" for a, b in zip(name, name[1:])):
        bonus -= 5
    return bonus


def quality(name: str) -> float:
    """Okunabilirlik puanı: sesli harfsiz ('qmr') ve 3+ ardışık ünsüzlü ('hbyby') yazımlar geride kalır."""
    if not name.isalpha():
        return 0.0
    score = pronounce_bonus(name)
    if not any(c in "aeiouy" for c in name):
        score -= 25
    run = longest = 0
    for c in name:
        run = run + 1 if c not in "aeiou" else 0
        longest = max(longest, run)
    if longest >= 3:
        score -= 10
    return score


def from_names(names: Iterable[str]) -> Iterator[Candidate]:
    for n in names:
        n = n.strip().lower().lstrip("@")
        if n:
            yield n, "manual", 1000.0
