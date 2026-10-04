"""Anahtarsız, yerel ve DOĞRULAMALI makine çevirisi (Türkçe kaynak → soru dili).

Yol (bkz. harita.md):
- Birincil model: Helsinki-NLP/opus-mt-tc-bible-big-trk-deu_eng_fra_por_spa (Apache-2.0),
  ikincil model: Helsinki-NLP/opus-mt-tr-es (Apache-2.0). İkisi de CTranslate2 int8 biçiminde, CPU'da çalışır.
- Her cümle için birden çok aday çeviri (n-best) üretilir. Sayı, birim, olumsuzluk ve
  karşılaştırma (altında/üzerinde) korunmamışsa aday reddedilir. Hiçbir aday geçmezse
  cümle "doğrulanamadı" olarak işaretlenir ve cevap metnine KONMAZ.
"""
import re
import threading
import unicodedata
from collections import Counter
from pathlib import Path

from .config import settings

TARGET_TAGS = {"es": ">>spa<<", "en": ">>eng<<", "pt": ">>por<<", "fr": ">>fra<<", "de": ">>deu<<"}
SOURCE_LANGS = {"tr"}  # yerel modelin desteklediği kaynak dilleri (Türkçe)

_lock = threading.Lock()
_models: dict = {}


class MTUnavailable(Exception):
    pass


def available() -> bool:
    return (Path(settings.mt_dir) / "bible-trk" / "model.bin").exists()


def supports(src: str, tgt: str) -> bool:
    return src in SOURCE_LANGS and tgt in TARGET_TAGS and available()


def _load(name: str):
    with _lock:
        if name not in _models:
            path = Path(settings.mt_dir) / name
            if not (path / "model.bin").exists():
                raise MTUnavailable(f"Çeviri modeli yok: {path}")
            import ctranslate2
            import sentencepiece as spm

            _models[name] = (
                ctranslate2.Translator(str(path), device="cpu", compute_type="int8",
                                       intra_threads=settings.mt_threads),
                spm.SentencePieceProcessor(model_file=str(path / "source.spm")),
                spm.SentencePieceProcessor(model_file=str(path / "target.spm")),
            )
        return _models[name]


# ---------------------------------------------------------------- normalizasyon
def strip_accents(s: str) -> str:
    return "".join(c for c in unicodedata.normalize("NFD", s) if unicodedata.category(c) != "Mn")


def normalize_source(s: str) -> str:
    """Modelin sayı hatası yaptığı Türkçe kalıpları çeviri öncesi açık biçime getirir."""
    s = re.sub(r"\byüzde\s+(\d+(?:[.,]\d+)?)", r"%\1", s, flags=re.I)  # "yüzde 7" → "%7"
    s = re.sub(r"(\d)\s*°\s*C\b", r"\1 °C", s)
    return s


# ---------------------------------------------------------------- denetimler
NUM_RE = re.compile(r"(?<![\w.,])\d+(?:[.,]\d+)?(?![\w])")

UNITS = [  # (kaynakta arama, hedefte kabul edilen biçimler)
    (r"mg/dl", r"mg/dl"),
    (r"mg/kg", r"mg/kg|miligramos? por kilo"),
    (r"ml/dk|ml/min", r"ml/min"),
    (r"\bmcg\b|µg|mikrogram", r"mcg|µg|microgram"),
    (r"\bmg\b|miligram", r"\bmg\b|miligram|milligram"),
    (r"\bml\b|mililitre", r"\bml\b|mililitr|millilit"),
    (r"\bkg\b|kilogram", r"\bkg\b|kilo"),
    (r"mmhg", r"mmhg|mm hg"),
    (r"°c", r"°c|grados|degrees"),
    (r"%", r"%|por ciento|percent|pour cent|prozent"),
    (r"\bdk\b|dakika", r"\bmin\b|minut"),
    (r"\bsaat", r"\bh\b|hora|hour|heure|stunde"),
]

TR_NEG = re.compile(
    r"\b(değil\w*|yok\w*|asla|hiçbir|hiç)\b"
    r"|\w+m[ae]z(?:l[ae]r)?\b|\w+m[ae]m[ae]l[ıi]\w*|\w+m[ıiuü]yor\w*|\w+m[ae]d[ae]n\b"
    r"|\w+m[ae]m[ıi]ş\w*|\w+s[ıiuü]z\b",
    re.I,
)
TGT_NEG = {
    "es": r"\b(no|nunca|sin|ni|ningun\w*|tampoco|jamas)\b",
    "en": r"\b(not|no|never|without|none|neither|nor)\b|n't\b",
    "pt": r"\b(nao|nunca|sem|nenhum\w*|nem)\b",
    "fr": r"\b(ne|pas|jamais|sans|aucun\w*|ni)\b|\bn'",
    "de": r"\b(nicht|kein\w*|nie|niemals|ohne)\b",
}
TR_BELOW = re.compile(r"(n[ıiuü]n|'[ıiuü]n)\s+alt[ıi]|alt[ıi]nda\w*|daha düşük|düşükse", re.I)
TR_ABOVE = re.compile(r"üzerinde\w*|üstünde\w*|\büzeri\b|\büstü\b|aşarsa|aşan|daha yüksek|yüksekse|fazla", re.I)
TGT_BELOW = {"es": r"debajo|inferior|menos|menor|\bbajo\b", "en": r"below|under|less|lower"}
TGT_ABOVE = {"es": r"encima|superior|mas de|mayor|supera|\bsobre\b|exced", "en": r"above|over|more than|greater|exceed|higher"}


def _nums(s: str) -> Counter:
    return Counter(n.replace(",", ".") for n in NUM_RE.findall(s))


def check(src: str, tgt: str, lang: str) -> list[str]:
    """Kaynak ve çeviri arasında anlamı bozan farkları listeler (boş liste = geçti)."""
    issues = []
    src_n, tgt_l = normalize_source(src), strip_accents(tgt.lower())
    if _nums(src_n) != _nums(tgt):
        issues.append(f"sayı: {sorted(_nums(src_n).elements())} ≠ {sorted(_nums(tgt).elements())}")
    low = src_n.lower()
    for s_pat, t_pat in UNITS:
        if re.search(s_pat, low) and not re.search(t_pat, tgt_l):
            issues.append(f"birim: {s_pat}")
            low = re.sub(s_pat, " ", low)
    if lang in TGT_NEG and bool(TR_NEG.search(src)) != bool(re.search(TGT_NEG[lang], tgt_l)):
        issues.append("olumsuzluk")
    for name, s_re, t_map in (("altında", TR_BELOW, TGT_BELOW), ("üzerinde", TR_ABOVE, TGT_ABOVE)):
        if lang in t_map and s_re.search(src) and not re.search(t_map[lang], tgt_l):
            issues.append(f"karşılaştırma: {name}")
    return issues


# ---------------------------------------------------------------- çeviri
def _candidates(name: str, prefix: str | None, sents: list[str], n: int) -> list[list[str]]:
    tr, sp_src, sp_tgt = _load(name)
    toks = [([prefix] if prefix else []) + sp_src.encode(normalize_source(s), out_type=str) + ["</s>"]
            for s in sents]
    res = tr.translate_batch(toks, beam_size=max(4, n), num_hypotheses=n, max_decoding_length=256)
    return [[sp_tgt.decode(h) for h in r.hypotheses] for r in res]


def translate_verified(sents: list[str], src: str, tgt: str, n: int = 6) -> list[dict]:
    if not supports(src, tgt):
        raise MTUnavailable(f"{src}→{tgt} yerel çeviri desteklenmiyor")
    pools = [("bible-trk", TARGET_TAGS[tgt])]
    if tgt == "es" and (Path(settings.mt_dir) / "opus-tr-es" / "model.bin").exists():
        pools.append(("opus-tr-es", None))
    out = [{"source": s, "text": None, "verified": False, "issues": [], "model": None, "first": None} for s in sents]
    pending = list(range(len(sents)))
    for name, prefix in pools:
        if not pending:
            break
        cands = _candidates(name, prefix, [sents[i] for i in pending], n)
        still = []
        for i, hyps in zip(pending, cands):
            if out[i]["first"] is None:
                out[i]["first"] = hyps[0]
            for h in hyps:
                iss = check(sents[i], h, tgt)
                if not iss:
                    out[i].update(text=h, verified=True, model=name, issues=[])
                    break
            else:
                out[i]["issues"] = check(sents[i], hyps[0], tgt)
                still.append(i)
        pending = still
    return out
