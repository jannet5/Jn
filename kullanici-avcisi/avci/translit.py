"""Her dilden kelimeyi Instagram'ın kabul ettiği harflere (a-z) çevirir.

Instagram kullanıcı adında yalnızca Latin harf, rakam, nokta ve alt çizgi olur.
Arapça/Rusça/Yunanca gibi yazılar bu yüzden Latin karşılığına (ör. حبيبي -> habibi) çevrilir.
"""
import itertools
import re
import unicodedata

try:  # Opsiyonel: Çince, Japonca, Hintçe, İbranice... gibi diğer tüm yazılar için
    from unidecode import unidecode
except ImportError:  # pragma: no cover
    unidecode = None

TATWEEL = "ـ"
SHADDA = "ّ"
SUKUN = "ْ"
HARAKAT = {
    "َ": "a", "ُ": "u", "ِ": "i",
    "ً": "an", "ٌ": "un", "ٍ": "in",
}

# Arapça/Farsça harf -> olası Latin karşılıkları (ilki varsayılan)
AR = {
    "ا": ["a"], "أ": ["a"], "إ": ["i", "e"], "آ": ["a"], "ٱ": ["a"],
    "ب": ["b"], "پ": ["p", "b"], "ت": ["t"], "ث": ["th", "s"],
    "ج": ["j", "g"], "چ": ["ch"], "ح": ["h", "7"], "خ": ["kh", "5"],
    "د": ["d"], "ذ": ["th", "z"], "ر": ["r"], "ز": ["z"], "ژ": ["zh", "j"],
    "س": ["s"], "ش": ["sh"], "ص": ["s"], "ض": ["d"], "ط": ["t"], "ظ": ["z"],
    "ع": ["a", "3"], "غ": ["gh", "g"], "ف": ["f"], "ڤ": ["v"],
    "ق": ["q", "k"], "ك": ["k"], "ک": ["k"], "گ": ["g"], "ل": ["l"],
    "م": ["m"], "ن": ["n"], "ه": ["h"], "ہ": ["h"], "ة": ["a", "h"],
    "و": ["w", "o", "u"], "ي": ["y", "i"], "ی": ["y", "i"], "ى": ["a"],
    "ئ": ["e", "i"], "ؤ": ["o", "u"], "ء": [""], "ٔ": [""],
}

CYR = {
    "а": "a", "б": "b", "в": "v", "г": "g", "д": "d", "е": "e", "ё": "e", "ж": "zh",
    "з": "z", "и": "i", "й": "y", "к": "k", "л": "l", "м": "m", "н": "n", "о": "o",
    "п": "p", "р": "r", "с": "s", "т": "t", "у": "u", "ф": "f", "х": "h", "ц": "ts",
    "ч": "ch", "ш": "sh", "щ": "sh", "ъ": "", "ы": "y", "ь": "", "э": "e", "ю": "yu",
    "я": "ya", "є": "ye", "і": "i", "ї": "yi", "ґ": "g", "қ": "q", "ү": "u", "ө": "o",
    "ң": "n", "һ": "h", "ә": "a",
}
GREEK = {
    "α": "a", "β": "v", "γ": "g", "δ": "d", "ε": "e", "ζ": "z", "η": "i", "θ": "th",
    "ι": "i", "κ": "k", "λ": "l", "μ": "m", "ν": "n", "ξ": "x", "ο": "o", "π": "p",
    "ρ": "r", "σ": "s", "ς": "s", "τ": "t", "υ": "y", "φ": "f", "χ": "ch", "ψ": "ps",
    "ω": "o",
}
LATIN_SPECIAL = {
    "ı": "i", "ß": "ss", "ø": "o", "æ": "ae", "œ": "oe", "ł": "l", "đ": "d", "ð": "d",
    "þ": "th", "ħ": "h", "ŧ": "t",
}

ONLY_ASCII = re.compile(r"[^a-z0-9]")
SEMI = set("aeiouyw")


def is_arabic_script(text: str) -> bool:
    return any(
        "؀" <= c <= "ۿ" or "ݐ" <= c <= "ݿ"
        or "ﭐ" <= c <= "﷿" or "ﹰ" <= c <= "﻿"
        for c in text
    )


def _clean(s: str, digits: bool) -> str:
    return re.sub(r"[^a-z0-9]" if digits else r"[^a-z]", "", s)


def _generic(word: str) -> str:
    out = []
    for ch in word.lower():
        if ch in LATIN_SPECIAL:
            out.append(LATIN_SPECIAL[ch])
        elif ch in CYR:
            out.append(CYR[ch])
        elif ch in GREEK:
            out.append(GREEK[ch])
        elif "a" <= ch <= "z" or "0" <= ch <= "9":
            out.append(ch)
        else:
            base = "".join(c for c in unicodedata.normalize("NFKD", ch) if not unicodedata.combining(c)).lower()
            if base in GREEK or base in CYR:  # vurgulu Yunanca/Kiril harf (ά, ё...)
                out.append(GREEK.get(base) or CYR.get(base) or "")
            elif base and all("a" <= c <= "z" or "0" <= c <= "9" for c in base):
                out.append(base)
            elif unidecode is not None:
                out.append(unidecode(ch).lower())
    return "".join(out)


def _insert_vowels(s: str, every: bool) -> str:
    """Arapçada kısa ünlüler yazılmaz: ardışık iki ünsüz arasına 'a' ekle (hbb -> habab)."""
    out = [s[0]] if s else []
    inserted_once = False
    for prev, cur in zip(s, s[1:]):
        if prev not in SEMI and cur not in SEMI and prev.isalpha() and cur.isalpha() \
                and (every or not inserted_once):
            out.append("a")
            inserted_once = True
        out.append(cur)
    return "".join(out)


def arabic_variants(word: str, digits: bool = False, cap: int = 12) -> list[str]:
    word = word.replace(TATWEEL, "")
    has_harakat = any(c in HARAKAT or c == SUKUN for c in word)
    # Her harf: seçenekler + ardından gelen işaretler (ünlü, şedde). Şedde harfi ikiler, ünlüyü değil.
    tokens = []  # [seçenekler, ünlü, ikilenmiş_mi]
    for c in word:
        if c in HARAKAT:
            if tokens:
                tokens[-1][1] = HARAKAT[c]
        elif c == SHADDA:
            if tokens:
                tokens[-1][2] = True
        elif c == SUKUN:
            continue
        elif c in AR:
            opts = AR[c] if digits else [o for o in AR[c] if not any(d.isdigit() for d in o)]
            tokens.append([opts or [""], "", False])
        elif c.lower() in LATIN_SPECIAL or "a" <= c.lower() <= "z" or "0" <= c <= "9":
            tokens.append([[c.lower()], "", False])
    # Belirsiz harflerden en çok ilk 4'ü değişsin; kalanı varsayılan kalsın
    amb = [i for i, t in enumerate(tokens) if len(t[0]) > 1][:4]
    results = []
    for combo in itertools.product(*[range(min(2, len(tokens[i][0]))) for i in amb]):
        pick = dict(zip(amb, combo))
        parts = []
        for i, (opts, vowel, dbl) in enumerate(tokens):
            ch = opts[pick.get(i, 0)]
            parts.append(ch * 2 if dbl else ch)
            parts.append(vowel)
        s = "".join(parts)
        results.append(s)
        if not has_harakat and len(s) > 1:
            results.append(_insert_vowels(s, every=True))
            results.append(_insert_vowels(s, every=False))
    seen, out = set(), []
    for s in results:
        s = _clean(s, digits)
        if s and s not in seen:
            seen.add(s)
            out.append(s)
    return out[:cap * 3]


def to_latin(word: str, digits: bool = False) -> list[str]:
    """Bir kelimenin olası kullanıcı adı yazımlarını döndürür (a-z, isteğe bağlı rakam)."""
    word = word.strip()
    if not word:
        return []
    if is_arabic_script(word):
        return arabic_variants(word, digits=digits)
    s = _generic(word)
    s = _clean(s, digits)
    return [s] if s else []
