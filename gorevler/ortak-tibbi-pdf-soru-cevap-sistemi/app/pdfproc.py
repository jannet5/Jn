"""PDF'ten sayfa bazlı metin çıkarma, parçalama ve kaba dil tespiti."""
import re

import pymupdf

STOPWORDS = {
    "tr": "ve bir bu ile için olarak da de en daha olan gibi veya ise hasta tedavi tanı".split(),
    "en": "the and of to in is for with that are as by on be or patient treatment".split(),
    "es": "el la de que y en los las del se por con para una un es paciente tratamiento".split(),
    "de": "der die und das ist mit von den zu des im nicht eine ein patienten behandlung".split(),
    "fr": "le la les de et des est en du une un pour par avec patient traitement".split(),
    "pt": "o a de que e do da em os as para com uma um paciente tratamento".split(),
    "it": "il la di che e del della in per con una un sono paziente trattamento".split(),
    "ar": "في من على إلى عن مع هذا التي الذي المريض العلاج".split(),
    "ru": "и в на с по не что как для это пациент лечение".split(),
}


def detect_language(text: str) -> str:
    words = re.findall(r"\w+", text.lower())[:4000]
    if not words:
        return "unknown"
    scores = {lang: sum(1 for w in words if w in set(sw)) for lang, sw in STOPWORDS.items()}
    if re.search(r"[çğışöü]", text.lower()):
        scores["tr"] = scores.get("tr", 0) * 1.5 + 1
    if re.search(r"[ñ¿¡]", text.lower()):
        scores["es"] = scores.get("es", 0) * 1.5 + 2
    lang, best = max(scores.items(), key=lambda kv: kv[1])
    return lang if best > 0 else "unknown"


def extract_pages(data: bytes, max_pages: int) -> tuple[list[tuple[int, str]], str]:
    doc = pymupdf.open(stream=data, filetype="pdf")
    if doc.needs_pass:
        raise ValueError("Şifreli PDF desteklenmiyor.")
    if doc.page_count > max_pages:
        raise ValueError(f"PDF çok uzun ({doc.page_count} sayfa, sınır {max_pages}).")
    pages = []
    for i, page in enumerate(doc, start=1):
        text = page.get_text("text", sort=True)
        text = re.sub(r"[ \t]+", " ", text)
        text = re.sub(r"\n{2,}", "\n", text).strip()
        if text:
            pages.append((i, text))
    title = (doc.metadata or {}).get("title") or ""
    doc.close()
    return strip_repeated_lines(pages), title


def strip_repeated_lines(pages: list[tuple[int, str]]) -> list[tuple[int, str]]:
    """Sayfaların yarısından fazlasında aynen tekrarlanan üst/alt bilgi satırlarını ayıklar."""
    if len(pages) < 3:
        return pages
    from collections import Counter

    counts = Counter(line.strip() for _, t in pages for line in set(t.split("\n")) if line.strip())
    rep = {line for line, c in counts.items() if c > len(pages) / 2 and not re.fullmatch(r"\d+", line)}
    out = []
    for no, t in pages:
        kept = "\n".join(line for line in t.split("\n") if line.strip() not in rep).strip()
        if kept:
            out.append((no, kept))
    return out


def chunk_pages(pages, size: int = 900, overlap: int = 150) -> list[tuple[int, str]]:
    """Cümle sınırına yakın keserek ~size karakterlik, sayfa numaralı parçalar üretir."""
    out = []
    for page_no, text in pages:
        start = 0
        while start < len(text):
            end = min(len(text), start + size)
            if end < len(text):
                cut = max(text.rfind(". ", start, end), text.rfind("\n", start, end))
                if cut > start + size // 2:
                    end = cut + 1
            piece = text[start:end].strip()
            if len(piece) > 40:
                out.append((page_no, piece))
            if end >= len(text):
                break
            start = max(end - overlap, start + 1)
    return out
