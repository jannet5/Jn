"""Ortak kütüphane üzerinde anlamsal (dilden bağımsız) arama ve yanıt oluşturma."""
import re
import threading

import httpx
import numpy as np

from .config import settings
from .db import DB
from .embed import embed
from .pdfproc import detect_language

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
