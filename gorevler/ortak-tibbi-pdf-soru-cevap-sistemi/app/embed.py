"""Çok dilli gömme (embedding) — yerel ONNX modeli, API anahtarı gerekmez.

paraphrase-multilingual-MiniLM-L12-v2 50+ dili aynı vektör uzayına eşler; böylece
İspanyolca soru, Türkçe PDF parçasıyla anlam üzerinden eşleşir.
"""
import threading

import numpy as np

from .config import settings

_model = None
_lock = threading.Lock()


def _get():
    global _model
    with _lock:
        if _model is None:
            from fastembed import TextEmbedding

            _model = TextEmbedding(settings.embed_model, cache_dir=settings.embed_cache)
        return _model


def embed(texts: list[str]) -> np.ndarray:
    vecs = np.array(list(_get().embed(texts, batch_size=32)), dtype=np.float32)
    norms = np.linalg.norm(vecs, axis=1, keepdims=True)
    norms[norms == 0] = 1
    return vecs / norms
