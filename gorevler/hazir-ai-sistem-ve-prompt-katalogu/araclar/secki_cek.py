#!/usr/bin/env python3
"""prompts.chat CSV anlık görüntüsünden seçilmiş prompt'ları BİREBİR çeker.

Prompt metinleri değiştirilmez (CC0). Türkçe başlık, kategori ve kullanım notu
ayrı alanlarda tutulur. Çıktı: veri/prompts_chat_secki.json
"""
import csv
import hashlib
import json
from pathlib import Path

KOK = Path(__file__).resolve().parent.parent
CSV = KOK / "veri" / "kaynak-anlik" / "prompts_chat_2026-10-03.csv"
CIKTI = KOK / "veri" / "prompts_chat_secki.json"

# (CSV'deki 'act' adı, Türkçe başlık, kategori, ne zaman işe yarar)
SECKI = [
    ("Prompt Generator", "Prompt üretici", "Prompt yazma", "Ne istediğini bir başlıkla söyle, sana tam bir rol prompt'u yazsın."),
    ("Prompt Enhancer", "Prompt iyileştirici", "Prompt yazma", "Kendi kısa prompt'unu daha ayrıntılı ve etkili hale getirir."),
    ("English Translator and Improver", "İngilizce çevirmen ve düzeltici", "Dil", "Türkçe yaz, düzgün ve zengin İngilizceye çevirsin."),
    ("English Language Tutor for Turkish Speakers", "Türkler için İngilizce öğretmeni", "Dil", "Türkçe konuşanlara özel İngilizce pratik."),
    ("Proofreader", "Redaktör (yazım/dilbilgisi)", "Yazı", "Metindeki yazım ve dilbilgisi hatalarını düzeltir."),
    ("Professional Email Writer for Any Occasion", "Profesyonel e-posta yazarı", "Ofis", "Her durum için resmi e-posta taslağı."),
    ("Meeting Summary and Action Plan Generator", "Toplantı özeti ve eylem planı", "Ofis", "Toplantı notunu özet + kim/ne/ne zaman listesine çevirir."),
    ("Excel Sheet", "Metin tabanlı Excel", "Ofis", "Formülleri sohbet içinde tablo gibi çalıştırır."),
    ("Job Interviewer", "İş mülakatçısı", "Kariyer", "Mülakat provası; pozisyonu kendin yaz."),
    ("Career Counselor", "Kariyer danışmanı", "Kariyer", "Hangi meslek/yol sana uygun, beceri ve ilgine göre."),
    ("Recruiter", "İşe alım uzmanı", "Kariyer", "Aday bulma stratejisi ve ilan yazımı."),
    ("Startup Idea Generator", "Girişim fikri üretici", "İş", "Bir 'keşke' cümlesinden iş planı taslağı."),
    ("Product Manager", "Ürün yöneticisi", "İş", "Ürün gereksinim dokümanı (PRD) yazımı."),
    ("Social Media Manager", "Sosyal medya yöneticisi", "Pazarlama", "Kampanya, içerik takvimi, topluluk yönetimi."),
    ("YouTube Script Engine — High Retention", "YouTube senaryo motoru", "Pazarlama", "İzleyiciyi tutan video senaryosu."),
    ("Accountant", "Muhasebeci / finans planı", "Finans", "Bütçe ve finans planı fikirleri (profesyonel danışmanlığın yerine geçmez)."),
    ("Legal Advisor", "Hukuk danışmanı (ön bilgi)", "Hukuk", "Durumunu anlatıp genel yol haritası al (avukat yerine geçmez)."),
    ("Code Reviewer", "Kod inceleyici", "Yazılım", "Kodunu yapıştır, hata/iyileştirme önerisi al."),
    ("Commit Message Generator", "Commit mesajı üretici", "Yazılım", "Değişiklik özetinden düzgün commit mesajı."),
    ("SQL Terminal", "SQL terminali", "Yazılım", "Örnek veritabanında SQL pratiği."),
    ("Linux Terminal", "Linux terminali", "Yazılım", "Komut pratiği; gerçek sistemi riske atmadan."),
    ("Math Teacher", "Matematik öğretmeni", "Eğitim", "Konuyu basit anlatım ve örnekle öğretir."),
    ("Socratic Universal Tutor", "Sokratik öğretmen", "Eğitim", "Cevabı vermez, sorularla düşündürür."),
    ("Book Summarizer", "Kitap özetleyici", "Eğitim", "Kitabın ana fikirleri ve dersleri."),
    ("Debate Coach", "Münazara koçu", "Eğitim", "Argüman hazırlığı ve pratik."),
    ("Personal Trainer", "Kişisel antrenör", "Sağlık ve yaşam", "Hedefine göre egzersiz planı (sağlık sorunun varsa doktora danış)."),
    ("Dietitian", "Diyetisyen", "Sağlık ve yaşam", "Tarif ve beslenme planı fikirleri (tıbbi tavsiye değildir)."),
    ("Travel Guide", "Gezi rehberi", "Sağlık ve yaşam", "Bulunduğun yere göre gezilecek yer önerisi."),
    ("Storyteller", "Hikâye anlatıcı", "Yaratıcı", "Çocuklara/yetişkinlere hikâye."),
    ("Midjourney Prompt Generator", "Midjourney prompt üretici", "Yaratıcı", "Görsel fikrini ayrıntılı görsel prompt'una çevirir."),
]


def main() -> None:
    csv.field_size_limit(10**9)
    satirlar = list(csv.DictReader(CSV.open(encoding="utf-8")))
    ada_gore = {}
    for s in satirlar:
        ada_gore.setdefault(s["act"], s)  # ilk eşleşme
    cikti, eksik = [], []
    for act, tr, kat, fayda in SECKI:
        s = ada_gore.get(act)
        if s is None:
            eksik.append(act)
            continue
        cikti.append({
            "act": act,
            "baslik_tr": tr,
            "kategori": kat,
            "fayda": fayda,
            "tur": s["type"],
            "katkici": s["contributor"],
            "prompt": s["prompt"],
            "sha256": hashlib.sha256(s["prompt"].encode("utf-8")).hexdigest(),
        })
    if eksik:
        raise SystemExit(f"CSV'de bulunamadı: {eksik}")
    CIKTI.write_text(json.dumps({
        "kaynak": "https://raw.githubusercontent.com/f/prompts.chat/main/prompts.csv",
        "anlik_goruntu": CSV.name,
        "anlik_goruntu_sha256": hashlib.sha256(CSV.read_bytes()).hexdigest(),
        "lisans": "CC0 1.0 (prompts.chat prompt içeriği)",
        "secki": cikti,
    }, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"{len(cikti)} prompt yazıldı -> {CIKTI.relative_to(KOK)}")


if __name__ == "__main__":
    main()
