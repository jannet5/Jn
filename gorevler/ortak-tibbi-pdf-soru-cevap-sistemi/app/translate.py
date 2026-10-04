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
# ÖNEMLİ: Bu denetimler YÜZEYSELDİR. Geçmek "tam anlam doğru" demek değildir; yalnız aşağıdaki
# hata sınıflarının bulunmadığını gösterir. Kanıtlanamayan durumda çeviri reddedilir (temkinli).
NUM = r"(?<![\w.,])(\d+(?:[.,]\d+)?)(?![\w])"

# (sınıf, kaynakta sayıdan hemen sonra gelen birim, hedefte sayıdan hemen sonra gelen birim)
UNIT_CLASSES = [
    ("mg/dL", r"mg/dl", r"mg/dl|miligramos? por decilitro"),
    ("mg/kg", r"mg/kg", r"mg/kg|miligramos? por kilo\w*|milligrams? per kilo\w*"),
    ("mL/dk", r"ml/dk|ml/min", r"ml/min"),
    ("mcg", r"mcg|µg|mikrogram", r"mcg|µg|microgram\w*|mikrogramm\w*"),
    ("mmHg", r"mmhg", r"mm ?hg|mm/hg"),
    ("°C", r"° ?c\b", r"° ?c\b|grados?(?: celsius)?|degrees?|grad\b"),
    ("mg", r"mg\b|miligram", r"mg\b|miligramos?|milligram\w*|milligramm\w*"),
    ("mL", r"ml\b|mililitre", r"ml\b|mililitros?|millilit\w*"),
    ("kg", r"kg\b|kilogram", r"kg\b|kilo\w*"),
    ("%", r"%", r"%|por ciento|por cento|percent|pour cent|prozent"),
    ("dk", r"dk\b|dakika", r"min\b|minut\w*"),
    ("saat", r"saat", r"h\b|horas?|hours?|heures?|stunden?"),
]

TR_NEG_WORD = re.compile(
    r"\b(?:değil\w*|yok\w*|asla|hiçbir|hiç)\b"
    r"|\b\w+m[ae]z(?:l[ae]r)?\b|\b\w+m[ae]m[ae]l[ıi]\w*|\b\w+m[ıiuü]yor\w*|\b\w+m[ae]d[ae]n\b"
    r"|\b\w+m[ae]m[ıi]ş\w*|\b\w+s[ıiuü]z\b",
    re.I,
)
# Olumlu yüklem (eylem) kalıpları — tıp metninde sık edilgen/geniş zaman/ek-fiil biçimleri
TR_POS_PRED = re.compile(
    r"\b\w+(?:[ıiuü]l|n)[ıiuü]r\b|\b\w+(?:d|t)[ıiuü]r\b|\b\w+[ae]bil[ıi]r\w*|\b\w+m[ae]l[ıi]d[ıi]r\b"
    r"|\b\w+[ıiuü]yor\w*|\b\w+[ae]c[ae]k\w*|\b(?:eder|olur|alır|verir|geçer|gelişir)\b",
    re.I,
)
TGT_NEG = {
    "es": r"\b(?:no|nunca|sin|ni|ningun\w*|tampoco|jamas)\b",
    "en": r"\b(?:not|no|never|without|none|neither|nor)\b|n't\b",
    "pt": r"\b(?:nao|nunca|sem|nenhum\w*|nem)\b",
    "fr": r"\b(?:pas|jamais|sans|aucun\w*|ni)\b",
    "de": r"\b(?:nicht|kein\w*|nie|niemals|ohne)\b",
}
TR_BELOW = re.compile(r"(?:n[ıiuü]n|'[ıiuü]n)\s+alt[ıi]|alt[ıi]nda\w*|daha düşük|düşükse", re.I)
TR_ABOVE = re.compile(r"üzerinde\w*|üstünde\w*|\büzeri\b|\büstü\b|aşarsa|aşan|daha yüksek|yüksekse|fazla", re.I)
TGT_CMP = {  # dil: (altında, üzerinde)
    "es": (r"debajo|inferior|menos|menor|\bbajo\b", r"encima|superior|mas de|mayor|supera|\bsobre\b|exced"),
    "en": (r"below|under|less|lower", r"above|over|more than|greater|exceed|higher"),
    "pt": (r"abaixo|inferior|menos|menor", r"acima|superior|mais de|maior|excede|ultrapass"),
    "fr": (r"en dessous|au-dessous|inferieur|moins|\bsous\b", r"au-dessus|superieur|plus de|depass|exced"),
    "de": (r"unter|weniger|niedriger|kleiner", r"uber|mehr als|hoher|grosser|uberschreit"),
}


def _quantities(text: str, side: str) -> list[tuple[str, str]]:
    """Metindeki (değer, birim sınıfı) çiftlerini sırasıyla döndürür."""
    t = strip_accents(text.lower()) if side == "tgt" else text.lower()
    out = []
    for m in re.finditer(NUM, t):
        val = m.group(1).replace(",", ".")
        before, after = t[max(0, m.start() - 1):m.start()], t[m.end():m.end() + 30]
        after = re.sub(r"^'\w*", "", after)  # Türkçe ek: 0,5 mg'dır → 0,5 mg
        unit = "∅"
        if before == "%":
            unit = "%"
        else:
            for name, s_re, t_re in UNIT_CLASSES:
                pat = strip_accents(t_re) if side == "tgt" else s_re
                if re.match(r"\s*(?:" + pat + r")", after):
                    unit = name
                    break
        out.append((val, unit))
    return out


# Ondalık virgülü (6,5) bölmemek için virgül yalnız ardından boşluk gelirse ayırıcıdır
SRC_SEG = re.compile(r",\s|;|:\s|\s(?:ve|ile|ama|fakat|veya)\s", re.I)
TGT_SEG = re.compile(r",\s|;|:\s|\s(?:y|e|o|pero|and|or|but|et|ou|mais|und|oder|aber)\s", re.I)
UNIT_TOKENS = {"dl", "ml", "mg", "kg", "mmhg", "mcg", "iu", "min", "l", "c", "dk"}
ANCHOR = re.compile(r"\b(?=\w*[A-ZÇĞİÖŞÜ0-9])(?!\d+\b)\w+\b")


def _subject_binding(src: str, tgt: str, sq: list) -> str:
    """Birden çok nicelikte: kaynakta bir niceliğin bölümündeki varlık sözcükleri (A, B, HbA1c gibi,
    çeviride birebir geçen) çeviride de aynı niceliğin bölümünde olmalı. Bozuksa açıklama döndürür."""
    shared = {a for a in ANCHOR.findall(src) if a.lower() not in UNIT_TOKENS and re.search(r"(?<!\w)" + re.escape(a) + r"(?!\w)", tgt)}
    if not shared:
        return ""

    def seg_map(text, splitter, side):
        m = {}
        for seg in splitter.split(text):
            anchors = {a for a in shared if re.search(r"(?<!\w)" + re.escape(a) + r"(?!\w)", seg)}
            for q in _quantities(seg, side):
                m.setdefault(q, set()).update(anchors)
        return m

    sm, tm = seg_map(src, SRC_SEG, "src"), seg_map(tgt, TGT_SEG, "tgt")
    for q, anchors in sm.items():
        if anchors and tm.get(q, set()) != anchors:
            return f"{q[0]} {q[1]}: kaynakta {sorted(anchors)}, çeviride {sorted(tm.get(q, set()))}"
    return ""


def _tr_predicates(text: str) -> list[bool]:
    """Kaynak cümledeki yüklemlerin kutupluluğu (True = olumsuz), metin sırasıyla."""
    hits = [(m.start(), True) for m in TR_NEG_WORD.finditer(text)]
    neg_spans = [(m.start(), m.end()) for m in TR_NEG_WORD.finditer(text)]
    for m in TR_POS_PRED.finditer(text):
        if not any(a <= m.start() < b for a, b in neg_spans):
            hits.append((m.start(), False))
    return [neg for _, neg in sorted(hits)]


def check(src: str, tgt: str, lang: str) -> list[str]:
    """Anlamı bozabilecek farkları listeler. Boş liste = yüzeysel denetimler geçti (tam doğruluk değil)."""
    issues = []
    src_n = normalize_source(src)
    tgt_l = strip_accents(tgt.lower())
    if lang not in TGT_NEG or lang not in TGT_CMP:
        return [f"unsupported — {lang} için denetim yok; çeviri doğrulanmış sayılmaz"]

    # 1) Nicelik + birim çiftleri (değer tek başına değil, birimiyle birlikte)
    sq, tq = _quantities(src_n, "src"), _quantities(tgt, "tgt")
    if Counter(sq) != Counter(tq):
        issues.append(f"quantity — nicelik/birim çiftleri farklı: {sq} ≠ {tq}")
    elif len(sq) >= 2 and sq != tq:
        issues.append("quantity_order — nicelik sırası değişti; hangi niceliğin neye ait olduğu kanıtlanamıyor")
    elif len(sq) >= 2:
        bad = _subject_binding(src_n, tgt, sq)
        if bad:
            issues.append(f"quantity_subject — nicelik farklı bir özneye bağlanmış: {bad}")
    units = Counter(u for _, u in sq if u != "∅")
    if any(c >= 2 for c in units.values()):
        issues.append("same_unit_multi — aynı birimli birden çok nicelik; nicelik-özne bağı kanıtlanamıyor")

    # 2) Sayıya bağlı olmayan birim sözcükleri (ör. "mg cinsinden")
    low = src_n.lower()
    for name, s_re, t_re in UNIT_CLASSES:
        if re.search(s_re, low) and not re.search(strip_accents(t_re), tgt_l):
            issues.append(f"unit — birim düşmüş: {name}")
            low = re.sub(s_re, " ", low)

    # 3) Olumsuzluk: yüklem bazında; karışık olumlu/olumsuz çok eylemde kapsam kanıtlanamaz
    preds = _tr_predicates(src)
    n_neg = sum(preds)
    t_neg = len(re.findall(TGT_NEG[lang], tgt_l)) + (len(re.findall(r"\bn'", tgt_l)) if lang == "fr" else 0)
    if n_neg == 0 and t_neg:
        issues.append("negation_added — çeviride kaynakta olmayan olumsuzluk var")
    elif n_neg and not t_neg:
        issues.append("negation_dropped — çeviride olumsuzluk düşmüş")
    elif n_neg and n_neg != len(preds):
        issues.append("negation_scope — birden çok eylem ve karışık olumlu/olumsuz; hangi eylemin olumsuz olduğu kanıtlanamıyor")
    elif n_neg and lang != "fr" and t_neg != n_neg:
        issues.append(f"negation_count — olumsuzluk sayısı farklı: kaynak {n_neg} ≠ çeviri {t_neg}")

    # 4) Karşılaştırma yönü (altında / üzerinde), beş hedef dilin hepsinde
    below_t, above_t = TGT_CMP[lang]
    s_below, s_above = bool(TR_BELOW.search(src)), bool(TR_ABOVE.search(src))
    t_below, t_above = bool(re.search(below_t, tgt_l)), bool(re.search(above_t, tgt_l))
    if s_below and not t_below:
        issues.append("comparator — 'altında' düşmüş" + (" ve yön tersine dönmüş" if t_above and not s_above else ""))
    if s_above and not t_above:
        issues.append("comparator — 'üzerinde' düşmüş" + (" ve yön tersine dönmüş" if t_below and not s_below else ""))
    return issues


CHECKS_DONE = ["nicelik+birim+sıra", "birim", "olumsuzluk (yüklem bazında)", "karşılaştırma yönü"]


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
                if not out[i]["issues"]:  # gerekçe, gösterilen ilk adayla (birincil model) tutarlı kalsın
                    out[i]["issues"] = check(sents[i], out[i]["first"], tgt)
                still.append(i)
        pending = still
    return out
