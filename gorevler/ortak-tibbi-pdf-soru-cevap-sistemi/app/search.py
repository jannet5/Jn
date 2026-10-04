"""Ortak kütüphane üzerinde anlamsal (dilden bağımsız) arama ve yanıt oluşturma."""
import re
import threading

import httpx
import numpy as np

from .config import settings
from .db import DB
from .embed import embed
from .pdfproc import detect_language
from .translate import strip_accents, supports, translate_verified

LANG_NAMES = {
    "tr": "Türkçe", "en": "English", "es": "Español", "de": "Deutsch", "fr": "Français",
    "pt": "Português", "it": "Italiano", "ar": "العربية", "ru": "Русский", "unknown": "?",
}


class Index:
    """Görünür belgelerin tüm parça vektörlerini bellekte tek matris olarak tutar."""

    def __init__(self, db: DB):
        self.db = db
        self.lock = threading.Lock()
        self.dirty = True
        self.ids = np.zeros(0, dtype=np.int64)
        self.docs = np.zeros(0, dtype=np.int64)
        self.mat = np.zeros((0, 384), dtype=np.float32)

    def invalidate(self):
        self.dirty = True

    def _load(self):
        rows = self.db.q(
            "SELECT c.id, c.doc_id, c.vec FROM chunks c JOIN documents d ON d.id=c.doc_id"
            " WHERE d.hidden=0 ORDER BY c.id"
        )
        if rows:
            self.ids = np.array([r["id"] for r in rows], dtype=np.int64)
            self.docs = np.array([r["doc_id"] for r in rows], dtype=np.int64)
            self.mat = np.vstack([np.frombuffer(r["vec"], dtype=np.float32) for r in rows])
        else:
            self.ids = np.zeros(0, dtype=np.int64)
            self.docs = np.zeros(0, dtype=np.int64)
            self.mat = np.zeros((0, 384), dtype=np.float32)
        self.dirty = False

    def search(self, qvec: np.ndarray, doc_ids: list[int] | None, k: int) -> list[tuple[int, float]]:
        with self.lock:
            if self.dirty:
                self._load()
            if not len(self.ids):
                return []
            scores = self.mat @ qvec
            if doc_ids:
                scores = np.where(np.isin(self.docs, doc_ids), scores, -1.0)
            order = np.argsort(-scores)[:k]
            return [(int(self.ids[i]), float(scores[i])) for i in order if scores[i] > -1.0]


def _sentences(text: str) -> list[str]:
    text = re.sub(r"-\n(?=[a-zçğıöşü])", "", text)  # satır sonu tirelemesini birleştir
    text = re.sub(r"\s*\n\s*", " ", text)  # PDF satır kaydırmaları cümleyi bölmesin
    parts = re.split(r"(?<=[.!?;])\s+(?=[A-ZÇĞİÖŞÜ0-9¿¡\"(])", text)
    return [p.strip() for p in parts if len(p.strip()) > 25]


def _terms(text: str) -> set[str]:
    """Diller arası ortak kalan terimler (HbA1c, ilaç adları, sayılar) için 5 harflik kökler."""
    return {w[:5] for w in re.findall(r"\w+", text.lower()) if len(w) >= 4 or any(c.isdigit() for c in w)}


def retrieve(db: DB, index: Index, question: str, doc_ids=None, k: int = 5) -> list[dict]:
    qvec = embed([question])[0]
    hits = index.search(qvec, doc_ids, k)
    out = []
    for cid, score in hits:
        r = db.one(
            "SELECT c.text, c.page, d.id doc_id, d.title, d.language FROM chunks c"
            " JOIN documents d ON d.id=c.doc_id WHERE c.id=?",
            (cid,),
        )
        out.append({**dict(r), "score": round(score, 4), "chunk_id": cid})
    # Parça içindeki en ilgili cümleleri öne çıkar (kısa, alıntılanabilir yanıt)
    if out:
        sents, owners = [], []
        for i, h in enumerate(out[:3]):
            for s in _sentences(h["text"]):
                sents.append(s)
                owners.append(i)
        if sents:
            sv = embed(sents)
            qt = _terms(question)
            sc = sv @ qvec + np.array([min(0.3, 0.12 * len(qt & _terms(x))) for x in sents])
            for i, h in enumerate(out):
                best = [(sc[j], j) for j in range(len(sents)) if owners[j] == i]
                best = sorted(sorted(best, key=lambda x: -x[0])[:3], key=lambda x: x[1])
                h["highlights"] = [sents[j] for _, j in best]
    return out


def extractive_answer(question: str, hits: list[dict]) -> dict:
    qlang = detect_language(question)
    if not hits or hits[0]["score"] < 0.25:
        return {"mode": "extractive", "question_language": qlang, "confident": False, "answer": None}
    top = hits[0]
    lines = top.get("highlights") or [top["text"][:400]]
    return {
        "mode": "extractive",
        "question_language": qlang,
        "confident": top["score"] >= 0.4,
        "answer": " ".join(lines),
        "answer_language": top["language"],
        "source": {"doc_id": top["doc_id"], "title": top["title"], "page": top["page"]},
    }


# Soru içindeki "genel" sözcükler: cevaplanabilirlik denetiminde konu terimi sayılmaz
GENERIC = set("""
cual cuales que quien como cuanto cuanta cuantos donde cuando por para partir desde con sin una uno unos unas los las del
el la de en se es son esta estan usa usan utiliza utilizan usar emplea debe deben puede pueden hay hace
valor valores dosis tratamiento tratamientos inicial primer primera primero objetivo meta paciente pacientes
adulto adultos nino ninos enfermedad indicado indica recomienda administra administran administrar dar da
dan tipo forma cosa caso casos eficaz eficaces efectivo efectivos sirve manejo terapia medicamento farmaco
what which how much many when where does do is are the of in for with used use dose treatment initial first
target goal patient patients adult adults value effective given give
""".split())


FUNCTION = set("cual cuales como cuanto cuanta donde cuando para partir desde hasta esta estan este estos esas "
                "unos unas what which when where with from does".split())


def _bonus_terms(question: str) -> set[str]:
    words = re.findall(r"\w+", strip_accents(question.lower()))
    return {w[:5] for w in words if len(w) >= 4 and w not in FUNCTION}


def _topic_terms(question: str) -> set[str]:
    words = re.findall(r"\w+", strip_accents(question.lower()))
    return {w[:5] for w in words if len(w) >= 4 and w not in GENERIC and not w.isdigit()}


def translated_answer(question: str, hits: list[dict], target: str, max_hits: int = 3) -> dict:
    """Türkçe kaynaktan, soru dilinde, sayfaya dayalı ve doğrulanmış cevap (anahtarsız, yerel).

    İlk birkaç kaynak bölüm sırayla denenir; sorunun konu terimlerinin hepsi (en fazla ¼ eksik)
    bölümün çevirisinde geçmiyorsa o bölüm cevap sayılmaz. Hiçbiri uymuyorsa "cevap yok" döner.
    """
    base = {"mode": "translated", "question_language": target, "answer_language": target,
            "confident": False, "answer": None, "sentences": [], "unverified": []}
    cands = [h for h in hits[:max_hits] if h["score"] >= 0.25 and supports(h["language"], target)]
    if not cands:
        return {**base, "no_answer": True, "reason": "no_passage"}
    topic = _topic_terms(question)
    best_missing = None
    qvec = embed([question])[0]
    for rank, hit in enumerate(cands):
        sents = _sentences(hit["text"]) or [hit["text"][:400]]
        tr = translate_verified(sents, hit["language"], target)
        shown = [r["text"] or r["first"] or "" for r in tr]
        chunk = strip_accents(" ".join(shown).lower())
        missing = sorted(t for t in topic if t not in chunk)
        if len(missing) > len(topic) // 4:
            if best_missing is None or len(missing) < len(best_missing):
                best_missing = missing
            continue
        # Cevap cümleleri: soru diliyle aynı dilde (çeviri) anlam + soru terimi örtüşmesine göre en iyi 3
        sv = embed(shown)
        bonus = _bonus_terms(question)
        score = [float(sv[i] @ qvec) + 0.12 * sum(t in strip_accents(shown[i].lower()) for t in bonus)
                 for i in range(len(shown))]
        top = sorted(sorted(range(len(shown)), key=lambda i: -score[i])[:3])
        cite = {"doc_id": hit["doc_id"], "title": hit["title"], "page": hit["page"], "hit_rank": rank + 1}
        for i in top:
            r = tr[i]
            item = {"text": r["text"], "source_text": r["source"], "model": r["model"], "issues": r["issues"], **cite}
            (base["sentences"] if r["verified"] else base["unverified"]).append(item)
        if not base["sentences"]:
            return {**base, "no_answer": False, "reason": "translation_unverified", "source": cite}
        n = rank + 1
        base["answer"] = " ".join(f"{x['text']} [{n}]" for x in base["sentences"])
        return {**base, "no_answer": False, "confident": hit["score"] >= 0.4, "source": cite}
    return {**base, "no_answer": True, "reason": "topic_missing", "missing_terms": best_missing or []}


def ai_answer(question: str, hits: list[dict]) -> dict:
    """İsteğe bağlı: Claude ile soru dilinde, kaynak numaralı yanıt (MEDPDF_ANTHROPIC_API_KEY gerekir)."""
    ctx = "\n\n".join(
        f"[{i + 1}] ({h['title']}, s.{h['page']}, dil={h['language']})\n{h['text']}"
        for i, h in enumerate(hits)
    )
    system = (
        "You answer medical-education questions ONLY from the numbered excerpts of a shared PDF library. "
        "Reply in the same language as the question even if excerpts are in another language. "
        "Cite excerpts like [1]. If the excerpts do not contain the answer, say so plainly. "
        "End with a one-line note that this is educational, not clinical advice."
    )
    r = httpx.post(
        "https://api.anthropic.com/v1/messages",
        headers={
            "x-api-key": settings.anthropic_key,
            "anthropic-version": "2023-06-01",
            "content-type": "application/json",
        },
        json={
            "model": settings.anthropic_model,
            "max_tokens": 700,
            "system": system,
            "messages": [{"role": "user", "content": f"Excerpts:\n{ctx}\n\nQuestion: {question}"}],
        },
        timeout=60,
    )
    r.raise_for_status()
    text = "".join(b.get("text", "") for b in r.json().get("content", []) if b.get("type") == "text")
    return {"mode": "ai", "question_language": detect_language(question), "confident": True, "answer": text}
