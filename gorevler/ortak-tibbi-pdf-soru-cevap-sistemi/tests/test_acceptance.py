"""Kabul testleri: gerçek PDF + gerçek çok dilli model + gerçek HTTP API (TestClient)."""
import os
import sys
from pathlib import Path

import pytest

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / "scripts"))


@pytest.fixture(scope="module")
def env(tmp_path_factory):
    tmp = tmp_path_factory.mktemp("medpdf")
    os.environ["MEDPDF_DATA_DIR"] = str(tmp / "data")
    os.environ["MEDPDF_ADMIN_TOKEN"] = "test-admin"
    os.environ["MEDPDF_MONTHLY_FREE"] = "6"
    os.environ["MEDPDF_GRACE"] = "2"
    import make_sample_pdfs

    make_sample_pdfs.main(str(tmp / "samples"))
    from fastapi.testclient import TestClient

    from app.main import create_app

    app = create_app()
    return TestClient(app), tmp / "samples"


def new_device(client, ip):
    r = client.post("/api/device", headers={"x-forwarded-for": ip})
    assert r.status_code == 200, r.text
    return {"X-Device-Id": r.json()["device_id"]}


def upload(client, dev, path, **extra):
    with open(path, "rb") as f:
        return client.post(
            "/api/documents",
            headers=dev,
            files={"file": (path.name, f, "application/pdf")},
            data={"rights": "true", **extra},
        )


def test_shared_library_and_cross_lingual_retrieval(env):
    """Yalnız ARAMA kabulü: İspanyolca soru doğru Türkçe sayfayı bulur (cevap kabulü test_multilingual_answer.py'de)."""
    client, samples = env
    alice = new_device(client, "10.0.0.1")  # Türk öğrenci yükler
    r = upload(client, alice, samples / "acil-dahiliye-ozet-tr.pdf", subject="Acil")
    assert r.status_code == 200, r.text
    body = r.json()
    assert body["pages"] == 4 and not body["duplicate"]
    assert upload(client, alice, samples / "haematology-notes-en.pdf").status_code == 200

    # Aynı PDF tekrar yüklenirse kopya oluşmaz
    assert upload(client, alice, samples / "acil-dahiliye-ozet-tr.pdf").json()["duplicate"] is True

    # Başka cihaz (giriş yok) ortak kütüphaneyi görür
    docs = client.get("/api/documents").json()
    tr = next(d for d in docs if d["title"] == "SENTETİK TEST — Acil ve Dahiliye Özet Notları")
    assert tr["language"] == "tr" and tr["pages"] == 4

    pablo = new_device(client, "10.0.0.2")  # İspanyol öğrenci soru sorar
    cases = [
        ("¿Cuál es el tratamiento inicial del infarto agudo de miocardio?", 1, "aspirin"),
        ("¿Qué valor de HbA1c es el objetivo en la diabetes tipo 2?", 2, "7'nin altı"),
        ("¿Qué dosis de adrenalina se usa en la anafilaxia?", 3, "0,5 mg"),
    ]
    for q, page, word in cases:
        r = client.post("/api/ask", headers=pablo, json={"question": q})
        assert r.status_code == 200, r.text
        res = r.json()
        top = res["hits"][0]
        print(f"\n[ES→TR arama] {q}\n  → {top['title']} s.{top['page']} skor={top['score']}")
        assert top["doc_id"] == tr["id"] and top["page"] == page
        assert res["answer"]["question_language"] == "es"
        assert word.lower() in " ".join(top["highlights"]).lower()  # Türkçe kaynak cümlesi bulundu
        assert res["cost"] == 1

    # Türkçe soru İngilizce PDF'ten de yanıt bulur
    r = client.post("/api/ask", headers=pablo, json={"question": "Demir eksikliği anemisinde ilk tedavi nedir?"})
    assert r.json()["hits"][0]["title"] == "Haematology Notes"
    # EN→TR yerel çeviri yok: bunu açıkça bildirir, kaynak dilde alıntı verir
    assert r.json()["answer"]["translation_error"] == "unsupported_pair"


def test_transparent_quota_never_cuts_open_thread(env):
    client, _ = env
    dev = new_device(client, "10.0.0.3")
    q = client.get("/api/quota", headers=dev).json()
    assert q["free_total"] == 6 and q["free_left"] == 6 and q["level"] == "ok"
    assert q["costs"]["question"] == 1 and q["reset_at"]

    ask = lambda tid=None: client.post(
        "/api/ask", headers=dev, json={"question": "¿Tratamiento de la anafilaxia?", "thread_id": tid}
    )
    r = ask().json()
    tid = r["thread_id"]
    levels = [r["quota"]["level"]]
    for _ in range(5):  # 6 hakkın tamamı aynı konu içinde
        r = ask(tid)
        assert r.status_code == 200
        levels.append(r.json()["quota"]["level"])
    assert "warn" in levels or "low" in levels  # bitmeden önce uyarı
    assert r.json()["quota"]["free_left"] == 0

    # Kredi bitti ama açık konu yarıda kesilmez: esneme payı kullanılır
    r = ask(tid)
    assert r.status_code == 200, r.text
    st = r.json()["quota"]
    assert st["grace_used"] == 1 and st["level"] == "grace"
    assert any(h["kind"] == "ask:grace" for h in st["history"])

    # Yeni konu kredisiz başlatılamaz → 402 ve kota bilgisi (sürpriz değil, açıklamalı)
    r = ask(None)
    assert r.status_code == 402
    assert r.json()["quota"]["available"] == 0

    # Açık konu payın sonuna kadar devam eder, sonra durur
    assert ask(tid).status_code == 200
    r = ask(tid)
    assert r.status_code == 402 and r.json()["quota"]["level"] == "blocked"

    # Gönüllü destek: yönetici bağış karşılığı kod üretir, kullanıcı kart girmeden ekler
    code = client.post(
        "/api/admin/codes", headers={"X-Admin-Token": "test-admin"}, json={"credits": 20, "note": "ko-fi"}
    ).json()["code"]
    r = client.post("/api/redeem", headers=dev, data={"code": code})
    assert r.json()["added"] == 20 and r.json()["quota"]["bonus_left"] == 20
    assert client.post("/api/redeem", headers=dev, data={"code": code}).status_code == 400
    assert ask(None).status_code == 200
    assert client.get("/api/quota", headers=dev).json()["bonus_left"] == 19


def test_upload_rules_and_reports(env):
    client, samples = env
    dev = new_device(client, "10.0.0.4")
    with open(samples / "haematology-notes-en.pdf", "rb") as f:
        r = client.post("/api/documents", headers=dev, files={"file": ("x.pdf", f, "application/pdf")})
    assert r.status_code == 400  # hak beyanı zorunlu
    r = client.post(
        "/api/documents", headers=dev, files={"file": ("x.pdf", b"hello", "application/pdf")},
        data={"rights": "true"},
    )
    assert r.status_code == 400
    assert client.get("/api/quota").status_code == 401  # cihaz kimliği olmadan kota yok
    assert client.post("/api/admin/codes", json={"credits": 5}).status_code == 403

    doc = next(d for d in client.get("/api/documents").json() if d["title"] == "Haematology Notes")
    r = client.post(f"/api/documents/{doc['id']}/report", headers=dev, json={"reason": "copyright"})
    assert r.status_code == 200
    assert all(d["id"] != doc["id"] for d in client.get("/api/documents").json())  # telif bildirimiyle gizlenir
    rep = client.get("/api/admin/reports", headers={"X-Admin-Token": "test-admin"}).json()
    assert rep[0]["reason"] == "copyright"


def test_device_flood_limit(env):
    client, _ = env
    codes = [client.post("/api/device", headers={"x-forwarded-for": "10.9.9.9"}).status_code for _ in range(6)]
    assert codes[:5] == [200] * 5 and codes[5] == 429


def test_ai_mode_falls_back_to_cited_excerpt(env, monkeypatch):
    """Anahtar yokken YZ kapalı (1 kredi); anahtar var ama servis hata verirse alıntı yanıt döner."""
    client, samples = env
    from app import search
    from app.config import settings

    dev = new_device(client, "10.0.0.5")
    upload(client, dev, samples / "acil-dahiliye-ozet-tr.pdf")  # tek başına çalışınca da kütüphane dolu olsun
    r = client.post("/api/ask", headers=dev, json={"question": "¿Tratamiento de la anafilaxia?", "use_ai": True})
    assert r.json()["cost"] == 1 and r.json()["answer"]["mode"] == "translated"

    monkeypatch.setattr(settings, "anthropic_key", "dummy-not-real")

    def boom(*a, **k):
        raise RuntimeError("network")

    monkeypatch.setattr(search.httpx, "post", boom)
    r = client.post("/api/ask", headers=dev, json={"question": "¿Tratamiento de la anafilaxia?", "use_ai": True})
    body = r.json()
    assert body["cost"] == 3 and body["answer"]["mode"] == "extractive"
    assert body["answer"]["ai_error"] == "RuntimeError" and body["answer"]["source"]["page"] == 3
