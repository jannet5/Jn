"""Çok dilli CEVAP kabulü: sentetik Türkçe PDF → İspanyolca soru → kaynağa dayalı İspanyolca cevap.

Beklenen değerler aşağıda elle, kaynak metinden bağımsız olarak yazılmıştır (uygulama kodundan
türetilmez). Gerçek yerel çeviri modeli kullanılır; sahte (mock) çeviri YOKTUR. Model dosyaları
yoksa test atlanmaz, başarısız olur (MEDPDF_REQUIRE_MT=0 ile bilinçli olarak atlanabilir).
"""
import os
import re
import sys
from pathlib import Path

import pymupdf
import pytest

ROOT = Path(__file__).resolve().parent.parent
sys.path.insert(0, str(ROOT))
sys.path.insert(0, str(ROOT / "scripts"))

ES_NEG = re.compile(r"\b(no|nunca|sin|ningún|ninguna)\b", re.I)

# (soru, beklenen sayfa, cevapta OLMASI gerekenler, cevapta OLMAMASI gerekenler)
ANSWERABLE = [
    ("¿Cuál es el tratamiento inicial del infarto agudo de miocardio?", 1,
     [r"aspirina"], []),
    ("¿Qué valor de HbA1c es el objetivo en la diabetes tipo 2?", 2,
     [r"(inferior|menos|por debajo)\s+(al|del|de)\s+7\s?%"], [r"\bseis\b", r"\b6 por ciento"]),
    ("¿Qué dosis de adrenalina se usa en la anafilaxia?", 3,
     [r"0,5 mg", r"\b5 minutos"], [r"(?<![\d,.])5 mg\b", r"0,05"]),
    ("¿Los antibióticos son eficaces en las infecciones virales?", 4,
     [r"antibióticos no son (efectivos|eficaces)"], []),
    ("¿Cuándo se administra oxígeno en el infarto?", 1,
     [r"oxígeno", r"(inferior|menos|por debajo)\s+(al|del|de)\s+90\s?%"], []),
]
UNANSWERABLE = [
    "¿Cuál es la dosis de insulina en la cetoacidosis diabética?",
    "¿Qué antibiótico se usa en la meningitis bacteriana?",
    "¿Cuál es el tratamiento de la crisis asmática?",
]


def numbers(s: str) -> list[str]:
    s = re.sub(r"\byüzde\s+", "%", s)
    return sorted(n.replace(",", ".") for n in re.findall(r"(?<![\w.,])\d+(?:[.,]\d+)?(?![\w])", s))


@pytest.fixture(scope="module")
def env(tmp_path_factory):
    from app import translate

    if not translate.available():
        if os.environ.get("MEDPDF_REQUIRE_MT", "1") == "1":
            pytest.fail("Yerel çeviri modelleri yok: scripts/fetch_mt_models.sh çalıştırın")
        pytest.skip("MEDPDF_REQUIRE_MT=0: çeviri modelleri bilinçli olarak atlandı")
    tmp = tmp_path_factory.mktemp("mlans")
    from app.config import settings

    settings.data_dir = tmp / "data"
    settings.monthly_free_credits = 1000  # bu dosya kotayı değil cevabı sınar (kota: test_acceptance.py)
    import make_sample_pdfs

    make_sample_pdfs.main(str(tmp / "samples"))
    from fastapi.testclient import TestClient

    from app.main import create_app

    client = TestClient(create_app())
    dev = {"X-Device-Id": client.post("/api/device", headers={"x-forwarded-for": "10.7.0.1"}).json()["device_id"]}
    pdf = tmp / "samples" / "acil-dahiliye-ozet-tr.pdf"
    with open(pdf, "rb") as f:
        r = client.post("/api/documents", headers=dev, files={"file": (pdf.name, f, "application/pdf")},
                        data={"rights": "true"})
    assert r.status_code == 200, r.text
    # Sayfa metinleri, uygulamadan bağımsız olarak doğrudan PDF'ten okunur
    doc = pymupdf.open(pdf)
    pages = {i + 1: re.sub(r"\s+", " ", p.get_text()) for i, p in enumerate(doc)}
    assert "SENTETİK TEST BELGESİ" in pages[1]  # belge açıkça test olarak işaretli
    return client, dev, pages


@pytest.mark.parametrize("question,page,must,must_not", ANSWERABLE)
def test_spanish_answer_grounded_in_turkish_page(env, question, page, must, must_not):
    client, dev, pages = env
    r = client.post("/api/ask", headers=dev, json={"question": question})
    assert r.status_code == 200, r.text
    a = r.json()["answer"]
    print(f"\n[ES soru] {question}\n[ES cevap] {a.get('answer')}")
    for s in a.get("sentences", []):
        print(f"   TR kaynak s.{s['page']}: {s['source_text']}")
    assert a["mode"] == "translated" and a["no_answer"] is False
    assert a["answer_language"] == "es" and a["answer"]
    from app.pdfproc import detect_language

    assert detect_language(a["answer"]) == "es"  # cevap gerçekten İspanyolca
    text = a["answer"]
    for pat in must:
        assert re.search(pat, text, re.I), f"beklenen yok: {pat}"
    for pat in must_not:
        assert not re.search(pat, text, re.I), f"olmaması gereken var: {pat}"
    for s in a["sentences"]:
        assert s["page"] == page  # atıf doğru sayfaya
        assert re.sub(r"\s+", " ", s["source_text"]) in pages[page]  # kaynak cümle o sayfada gerçekten var
        assert numbers(s["text"]) == numbers(s["source_text"])  # sayılar korunmuş
        assert bool(ES_NEG.search(s["text"])) == bool(
            re.search(r"değil|yok\b|\w+m[ae]z\b|\w+m[ae]m[ae]l", s["source_text"])
        )  # olumsuzluk korunmuş
        assert f"[{s['hit_rank']}]" in text  # cevap metninde atıf işareti var


@pytest.mark.parametrize("question", UNANSWERABLE)
def test_says_no_answer_when_source_lacks_it(env, question):
    client, dev, _ = env
    a = client.post("/api/ask", headers=dev, json={"question": question}).json()["answer"]
    print(f"\n[ES soru] {question} → no_answer={a.get('no_answer')} eksik={a.get('missing_terms')}")
    assert a["no_answer"] is True and a["answer"] is None and not a["sentences"]


def test_missing_models_are_reported_not_hidden(env, monkeypatch, tmp_path):
    client, dev, _ = env
    from app.config import settings

    monkeypatch.setattr(settings, "mt_dir", str(tmp_path / "bos"))
    a = client.post("/api/ask", headers=dev, json={"question": "¿Qué dosis de adrenalina se usa en la anafilaxia?"}).json()["answer"]
    assert a["mode"] == "extractive" and a["translation_error"] == "mt_unavailable"
    assert a["answer_language"] == "tr"  # çeviri yoksa bunu gizlemeden kaynak dilde gösterir


# Gerçek modellerin bu çalışmada ürettiği HATALI çeviriler (bkz. calisma-gunlugu.md) — denetim reddetmeli
BAD = [
    ("Çoğu erişkin için HbA1c hedefi yüzde 7'nin altıdır.",
     "Para la mayoría de los adultos, el objetivo de HbA1c es de seis por ciento."),
    ("Çoğu erişkin için HbA1c hedefi yüzde 7'nin altıdır.",
     "Para la mayoría de los adultos, HBA1C es el objetivo del 7%."),
    ("Metformin, eGFR 30 mL/dk altında kullanılmamalıdır.",
     "La metformina no debe usarse bajo un electroencefalograma de 30 ml/dc."),
    ("Sistolik kan basıncı 180 mmHg üzerinde ise trombolitik tedavi uygulanmaz.",
     "La presión sistólica es de 180 mm/hg y no se aplica tratamiento trombolítico."),
    ("Ateş 38,5 °C üzerindeyse parasetamol 15 mg/kg verilebilir.",
     "Si la temperatura es superior a 38,5oC, el paracetamol se puede administrar 15 miligramos por kilogramo."),
    ("Bu hastalarda beta bloker verilmez.", "En estos pacientes se administran betabloqueantes."),
]
GOOD = [
    ("Çoğu erişkin için HbA1c hedefi yüzde 7'nin altıdır.",
     "Para la mayoría de los adultos, el objetivo de HbA1c es inferior al 7%."),
    ("Bu hastalarda beta bloker verilmez.", "En estos pacientes no se administran betabloqueantes."),
    ("Erişkinde adrenalin dozu 0,5 mg'dır ve gerekirse 5 dakika sonra tekrarlanır.",
     "En el adulto, la dosis de adrenalina es de 0,5 mg y, si es necesario, se repite después de 5 minutos."),
]


@pytest.mark.parametrize("src,tgt", BAD)
def test_check_rejects_known_bad_translations(src, tgt):
    from app.translate import check

    assert check(src, tgt, "es"), tgt


@pytest.mark.parametrize("src,tgt", GOOD)
def test_check_accepts_faithful_translations(src, tgt):
    from app.translate import check

    assert check(src, tgt, "es") == []
