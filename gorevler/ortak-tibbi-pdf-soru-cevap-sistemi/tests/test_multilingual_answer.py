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


# Denetimin REDDETMESİ gereken çeviriler. İlk 6'sı modellerin bu çalışmada ürettiği gerçek hatalı çıktılar,
# sonraki 4'ü ebeveyn kabulünde bulunan açıklar, kalanlar yeni bağımsız karşı örnekler.
BAD = [
    ("Çoğu erişkin için HbA1c hedefi yüzde 7'nin altıdır.",
     "Para la mayoría de los adultos, el objetivo de HbA1c es de seis por ciento.", "es"),
    ("Çoğu erişkin için HbA1c hedefi yüzde 7'nin altıdır.",
     "Para la mayoría de los adultos, HBA1C es el objetivo del 7%.", "es"),
    ("Metformin, eGFR 30 mL/dk altında kullanılmamalıdır.",
     "La metformina no debe usarse bajo un electroencefalograma de 30 ml/dc.", "es"),
    ("Sistolik kan basıncı 180 mmHg üzerinde ise trombolitik tedavi uygulanmaz.",
     "La presión sistólica es de 180 mm/hg y no se aplica tratamiento trombolítico.", "es"),
    ("Ateş 38,5 °C üzerindeyse parasetamol 15 mg/kg verilebilir.",
     "Si la temperatura es superior a 38,5oC, el paracetamol se puede administrar 15 miligramos por kilogramo.", "es"),
    ("Bu hastalarda beta bloker verilmez.", "En estos pacientes se administran betabloqueantes.", "es"),
    # ebeveyn kabulündeki 4 açık
    ("İlaç 10 mg ve sıvı 5 mL verilir.", "Se administran 5 mg de medicamento y 10 mL de líquido.", "es"),
    ("A ilacı 10 mg, B ilacı 5 mg verilir.", "Se administran 5 mg de A y 10 mg de B.", "es"),
    ("A ilacı verilmez, B ilacı verilir.", "Se administra A, pero no se administra B.", "es"),
    ("Değer 7 altında tutulur.", "O valor é mantido acima de 7.", "pt"),
    # yeni bağımsız karşı örnekler
    ("A ilacı 10 mg, B sıvısı 5 mL verilir.", "Se administran 10 mg de B y 5 mL de A.", "es"),
    ("A ilacı 10 mg, B ilacı 5 mg verilir.", "Se administran 10 mg de A y 5 mg de B.", "es"),  # doğru olsa bile kanıtlanamaz
    ("A ilacı verilmez, B ilacı verilir.", "A is not given, B is given.", "en"),            # doğru olsa bile kapsam kanıtlanamaz
    ("Değer 7 altında tutulur.", "La valeur est maintenue au-dessus de 7.", "fr"),
    ("Değer 7 altında tutulur.", "Der Wert wird über 7 gehalten.", "de"),
    ("Değer 7 altında tutulur.", "The value is kept at 7.", "en"),
    ("Aspirin verilir.", "No se administra aspirina.", "es"),
    ("Antibiyotik verilmez ve kültür alınmaz.", "No se administran antibióticos y se toman cultivos.", "es"),
    ("Tanı ölçütleri arasında açlık plazma glukozunun 126 mg/dL ve üzerinde olması ile HbA1c değerinin yüzde 6,5 ve üzerinde olması yer alır.",
     "Los criterios incluyen un valor de HbA1c de 126 mg/dL y una glucosa del 6,5% y superior.", "es"),
    ("İlaç 10 mg verilir.", "Se administran 5 mg.", "es"),
]
GOOD = [
    ("Çoğu erişkin için HbA1c hedefi yüzde 7'nin altıdır.",
     "Para la mayoría de los adultos, el objetivo de HbA1c es inferior al 7%.", "es"),
    ("Bu hastalarda beta bloker verilmez.", "En estos pacientes no se administran betabloqueantes.", "es"),
    ("Erişkinde adrenalin dozu 0,5 mg'dır ve gerekirse 5 dakika sonra tekrarlanır.",
     "En el adulto, la dosis de adrenalina es de 0,5 mg y, si es necesario, se repite después de 5 minutos.", "es"),
    ("İlaç 10 mg ve sıvı 5 mL verilir.", "Se administran 10 mg de medicamento y 5 mL de líquido.", "es"),
    ("A ilacı 10 mg, B sıvısı 5 mL verilir.", "Se administran 10 mg de A y 5 mL de B.", "es"),
    ("Değer 7 altında tutulur.", "O valor é mantido abaixo de 7.", "pt"),
    ("Değer 7 altında tutulur.", "La valeur est maintenue en dessous de 7.", "fr"),
    ("Değer 7 altında tutulur.", "Der Wert wird unter 7 gehalten.", "de"),
    ("Antibiyotikler viral enfeksiyonlarda etkili değildir ve bu hastalarda rutin olarak reçete edilmez.",
     "Los antibióticos no son efectivos en infecciones virales y no se prescriben rutinariamente en estos pacientes.", "es"),
    ("Tanı ölçütleri arasında açlık plazma glukozunun 126 mg/dL ve üzerinde olması ile HbA1c değerinin yüzde 6,5 ve üzerinde olması yer alır.",
     "Los criterios de diagnóstico incluyen una glucosa plasmática de ayuno de 126 mg/dL y un valor de HbA1c del 6,5% y superior.", "es"),
]


@pytest.mark.parametrize("src,tgt,lang", BAD)
def test_check_rejects_meaning_breaking_or_unprovable(src, tgt, lang):
    from app.translate import check

    issues = check(src, tgt, lang)
    print(f"\n RET {lang}: {tgt}\n   → {issues}")
    assert issues, tgt


@pytest.mark.parametrize("src,tgt,lang", GOOD)
def test_check_accepts_faithful_translations(src, tgt, lang):
    from app.translate import check

    assert check(src, tgt, lang) == []


def test_unsupported_language_is_never_reported_as_checked():
    from app.translate import check

    issues = check("Değer 7 altında tutulur.", "Il valore è mantenuto sotto 7.", "it")
    assert issues and issues[0].startswith("unsupported")


# ---- Gerçek modellerle regresyon (sahte çeviri yok); beklentiler koddan bağımsız yazıldı
ACCEPT = [  # (kaynak, {dil: çeviride bulunması gereken olgu kalıpları})
    ("Bu hastalarda aspirin kullanılmaz.",
     {"es": [r"\bno\b", r"aspirina"], "pt": [r"\bnão\b", r"aspirina"], "de": [r"\bnicht\b", r"aspirin"]}),
    ("Değer 7 altında tutulur.",
     {"es": [r"(bajo|debajo|inferior|menos)\D{0,6}7\b"], "pt": [r"abaixo de 7"], "de": [r"unter 7"]}),
    ("Parasetamol 500 mg ağızdan verilir.", {"es": [r"500 mg"], "pt": [r"500 mg"], "de": [r"500 mg"]}),
    ("Kan basıncı 140 mmHg üzerinde ise tedavi başlanır.",
     {"es": [r"encima de 140 mmHg"], "pt": [r"acima de 140 mmHg"], "de": [r"über 140 mmHg"]}),
    ("Penisilin alerjisi olan hastaya amoksisilin verilmez.",
     {"es": [r"\bno\b", r"amoxicilina"], "pt": [r"\bnão\b", r"amoxicilina"], "de": [r"\bnicht\b", r"amoxicillin"]}),
]
REFUSE = [  # (kaynak, beklenen ret kodu) — politika gereği, model çevirisi doğru olsa bile
    ("A ilacı 10 mg, B ilacı 5 mg verilir.", "same_unit_multi"),
    ("A ilacı verilmez, B ilacı verilir.", "negation_scope"),
]


@pytest.mark.parametrize("lang", ["es", "pt", "de"])
def test_real_model_regression(env, lang):
    from app.translate import translate_verified

    res = translate_verified([s for s, _ in ACCEPT] + [s for s, _ in REFUSE], "tr", lang)
    for (src, facts), r in zip(ACCEPT, res[: len(ACCEPT)]):
        print(f"\n [{lang}] KABUL? {r['verified']} | {r['text'] or r['first']}")
        assert r["verified"], f"doğru tekli cümle gereksiz reddedildi: {src} → {r['first']} {r['issues']}"
        for pat in facts[lang]:
            assert re.search(pat, r["text"], re.I), f"{pat} yok: {r['text']}"
    for (src, code), r in zip(REFUSE, res[len(ACCEPT):]):
        print(f"\n [{lang}] RET   {r['issues']} | ilk aday: {r['first']}")
        assert not r["verified"] and r["text"] is None
        assert any(i.startswith(code) for i in r["issues"])


def test_answer_hides_unprovable_translation_and_shows_original(env):
    """Cevap düzeyinde: kanıtlanamayan cümle cevap metnine girmez, orijinali gerekçesiyle döner."""
    from app.search import translated_answer

    text = ("Varfarin kullanan hastada aspirin verilmez, parasetamol verilir. "
            "Parasetamol 500 mg, ibuprofen 400 mg olarak yazılır.")
    hit = {"text": text, "page": 9, "doc_id": 99, "title": "SENTETİK TEST", "language": "tr", "score": 0.9}
    a = translated_answer("¿Paracetamol o aspirina?", [hit], "es")
    print("\n", a)
    assert a["answer"] is None and a["reason"] == "translation_unverified" and not a["sentences"]
    codes = {i.split(" ")[0] for u in a["unverified"] for i in u["issues"]}
    assert {"negation_scope", "same_unit_multi"} <= codes
    assert all(u["text"] is None for u in a["unverified"])  # çeviri hiç gösterilmez
    assert a["check_level"] == "surface"
