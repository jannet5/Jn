#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Kur'an verisini üretir: www/data.js
Kaynak 1: alquran.cloud "quran-uthmani" (Tanzil Uthmani metni + sayfa/cüz alanları)
Kaynak 2: Tanzil metadata (quran-data.js) -> sayfa/cüz başlangıçlarını ÇAPRAZ doğrulamak için
Metne ASLA dokunulmaz: sadece (a) BOM karakteri ve (b) ayet başına eklenmiş besmele öneki ayıklanır.
Kullanım: python3 build_data.py <ham_veri_klasoru> <cikti_data.js>
"""
import hashlib  # özet (sha256) hesaplamak için
import json  # alquran.cloud JSON dosyasını okumak için
import re  # Tanzil meta dosyasındaki dizileri ayıklamak için
import sys  # komut satırı argümanları için
from pathlib import Path  # dosya yolları için

HAM = Path(sys.argv[1])  # ham veri klasörü (cloud-uthmani.json, tanzil-meta.js)
CIKTI = Path(sys.argv[2])  # üretilecek data.js yolu

# --- Türkçe sure adları (Diyanet yazımı) + arama için ek adlar --------------------------
TR = [
    "Fâtiha", "Bakara", "Âl-i İmrân", "Nisâ", "Mâide", "En'âm", "A'râf", "Enfâl", "Tevbe", "Yûnus",
    "Hûd", "Yûsuf", "Ra'd", "İbrâhîm", "Hicr", "Nahl", "İsrâ", "Kehf", "Meryem", "Tâhâ",
    "Enbiyâ", "Hac", "Mü'minûn", "Nûr", "Furkân", "Şuarâ", "Neml", "Kasas", "Ankebût", "Rûm",
    "Lokmân", "Secde", "Ahzâb", "Sebe'", "Fâtır", "Yâsîn", "Sâffât", "Sâd", "Zümer", "Mü'min",
    "Fussilet", "Şûrâ", "Zuhruf", "Duhân", "Câsiye", "Ahkâf", "Muhammed", "Fetih", "Hucurât", "Kâf",
    "Zâriyât", "Tûr", "Necm", "Kamer", "Rahmân", "Vâkıa", "Hadîd", "Mücâdele", "Haşr", "Mümtehine",
    "Saff", "Cuma", "Münâfikûn", "Teğâbün", "Talâk", "Tahrîm", "Mülk", "Kalem", "Hâkka", "Meâric",
    "Nûh", "Cin", "Müzzemmil", "Müddessir", "Kıyâme", "İnsân", "Mürselât", "Nebe'", "Nâziât", "Abese",
    "Tekvîr", "İnfitâr", "Mutaffifîn", "İnşikâk", "Bürûc", "Târık", "A'lâ", "Gâşiye", "Fecr", "Beled",
    "Şems", "Leyl", "Duhâ", "İnşirâh", "Tîn", "Alak", "Kadir", "Beyyine", "Zilzâl", "Âdiyât",
    "Kâria", "Tekâsür", "Asr", "Hümeze", "Fîl", "Kureyş", "Mâûn", "Kevser", "Kâfirûn", "Nasr",
    "Tebbet", "İhlâs", "Felak", "Nâs",
]
EK = {  # aramada bulunsun diye alternatif adlar (sure numarası -> metin)
    40: "Gâfir", 94: "Şerh Elem neşrah", 111: "Mesed Leheb", 112: "Tevhid Kulhuvallah", 1: "Elhamdü Ümmü'l-Kitab",
    17: "Beni İsrail", 47: "Kıtal", 76: "Dehr", 96: "İkra", 22: "Hacc", 32: "Secde Elif Lam Mim",
}
assert len(TR) == 114  # 114 sure olmalı

# --- Ham verileri oku ----------------------------------------------------------------------
bulut = json.loads((HAM / "cloud-uthmani.json").read_text(encoding="utf-8"))  # alquran.cloud JSON
suralar_bulut = bulut["data"]["surahs"]  # sure listesi
assert len(suralar_bulut) == 114  # 114 sure

meta = (HAM / "tanzil-meta.js").read_text(encoding="utf-8")  # Tanzil meta metni


def meta_dizisi(ad):  # QuranData.<ad> = [ ... ]; dizisini [[sure, ayet], ...] olarak ayıklar
    m = re.search(r"QuranData\.%s\s*=\s*\[(.*?)\n\];" % ad, meta, re.S)  # dizinin gövdesini bul
    govde = re.sub(r"//[^\n]*", "", m.group(1))  # satır yorumlarını at
    cift = [(int(a), int(b)) for a, b in re.findall(r"\[\s*(\d+)\s*,\s*(\d+)\s*\]", govde)]  # [s, a] çiftleri
    return [c for c in cift if c[0] <= 114]  # sondaki [115, 1] bitiş işaretçisini at


tanzil_sayfa = meta_dizisi("Page")  # 604 sayfanın [sure, ayet] başlangıçları
tanzil_cuz = meta_dizisi("Juz")  # 30 cüzün [sure, ayet] başlangıçları
assert len(tanzil_sayfa) == 604, len(tanzil_sayfa)  # 604 sayfa
assert len(tanzil_cuz) == 30, len(tanzil_cuz)  # 30 cüz

# --- Ayetleri düz listeye dök ------------------------------------------------------------------
def iskelet(x):  # harf iskeleti: harekeleri/işaretleri at, sadece harfleri bırak (karşılaştırma için)
    return "".join(c for c in x if "\u0621" <= c <= "\u064a" or c in "\u0671\u0670")  # temel Arap harfleri + elif vasla


BESMELE = suralar_bulut[0]["ayahs"][0]["text"].lstrip("﻿")  # 1:1 metni = besmele (BOM'suz)
ayetler = []  # global sıralı ayet metinleri
anahtar = []  # her ayet için (sure, ayet no)
bulut_sayfa = []  # alquran.cloud'daki sayfa alanı
bulut_cuz = []  # alquran.cloud'daki cüz alanı
suralar = []  # [ilk global indeks, ayet sayısı, tr ad, ar ad, tür, ek arama]
ayiklanan_besmele = 0  # kaç ayetten besmele öneki ayıklandı (kontrol için)

for sn, sure in enumerate(suralar_bulut, 1):  # her sure için
    assert sure["number"] == sn  # sıra doğru mu
    suralar.append([len(ayetler), len(sure["ayahs"]), TR[sn - 1], sure["name"], "Mekkî" if sure["revelationType"] == "Meccan" else "Medenî", (EK.get(sn, "") + " " + sure["englishName"]).strip()])
    for an, ay in enumerate(sure["ayahs"], 1):  # sure içindeki her ayet
        assert ay["numberInSurah"] == an  # ayet sırası doğru mu
        t = ay["text"].lstrip("﻿")  # BOM'u at (sadece 1:1'de var)
        if an == 1 and sn not in (1, 9):  # 1. ve 9. sure hariç her surenin ilk ayetine besmele eklenmiş
            kelimeler = t.split(" ")  # ayeti kelimelere böl
            onek = " ".join(kelimeler[:4])  # ilk 4 kelime besmele olmalı
            assert iskelet(onek) == iskelet(BESMELE), (sn, onek)  # harf iskeleti besmele ile aynı olmalı
            if onek != BESMELE:  # kaynakta yazım farkı varsa raporla (besmele zaten 1:1'den çizilir)
                print("  ! sure %d besmele öneki kaynakta farklı yazılmış, ayıklandı: %r" % (sn, onek))
            t = " ".join(kelimeler[4:])  # ilk 4 kelimeyi (besmele önekini) çıkar
            ayiklanan_besmele += 1  # sayaç
        assert t and t == t.strip(), (sn, an)  # boş / kenarlarında boşluk olan metin olmasın
        ayetler.append(t)  # ayeti ekle
        anahtar.append((sn, an))  # anahtarı ekle
        bulut_sayfa.append(ay["page"])  # sayfa alanını ekle
        bulut_cuz.append(ay["juz"])  # cüz alanını ekle

assert len(ayetler) == 6236, len(ayetler)  # toplam 6236 ayet
assert ayiklanan_besmele == 112, ayiklanan_besmele  # 114 - 2 (Fâtiha, Tevbe) = 112

# --- Sayfa başlangıçları: Tanzil'den üret, alquran.cloud ile çapraz doğrula -----------------------
indeks = {k: i for i, k in enumerate(anahtar)}  # (sure, ayet) -> global indeks
sayfa_bas = [indeks[k] for k in tanzil_sayfa]  # Tanzil'e göre sayfa başlangıç indeksleri
assert sayfa_bas[0] == 0 and sayfa_bas == sorted(set(sayfa_bas)), "sayfa başlangıçları artan olmalı"
for p in range(604):  # her sayfa için
    son = sayfa_bas[p + 1] if p < 603 else 6236  # sayfanın bittiği yer
    for g in range(sayfa_bas[p], son):  # sayfadaki her ayet için
        assert bulut_sayfa[g] == p + 1, ("sayfa uyuşmazlığı", anahtar[g], bulut_sayfa[g], p + 1)  # iki kaynak aynı olmalı
sayfa_bas.append(6236)  # sentinel (son sayfanın bitişi)

# --- Cüz başlangıçları (sayfa olarak) ------------------------------------------------------------
cuz_sayfa = []  # her cüzün başladığı sayfa
for c, k in enumerate(tanzil_cuz, 1):  # her cüz için
    g = indeks[k]  # cüzün ilk ayetinin global indeksi
    assert bulut_cuz[g] == c and (g == 0 or bulut_cuz[g - 1] == c - 1), ("cüz uyuşmazlığı", c)  # çapraz doğrulama
    cuz_sayfa.append(bulut_sayfa[g])  # cüzün başladığı sayfa
assert cuz_sayfa == sorted(cuz_sayfa) and len(set(cuz_sayfa)) == 30  # artan ve benzersiz

# --- En uzun kelimeler: yazı boyutu üst sınırı, en geniş kelime ekrana sığsın diye çalışma anında ölçülür ---
kelimeler_hepsi = {k for t in ayetler for k in t.split(" ")}  # tüm benzersiz kelimeler
en_uzun = sorted(kelimeler_hepsi, key=lambda k: (-len(k), k))[:80]  # karakter sayısına göre ilk 80 (işaretler dahil)

# --- Özet (sha256) -> metnin sonradan bozulmadığını kanıtlamak için ----------------------------
ozet = hashlib.sha256("\n".join(ayetler).encode("utf-8")).hexdigest()  # tüm ayetlerin özeti

# --- data.js yaz ------------------------------------------------------------------------------------
cikti = ["/* Kur'an verisi. tools/build_data.py üretir, elle DEĞİŞTİRME. sha256(ayetler)=%s */" % ozet,  # başlık
         "window.Q={",  # nesne başlıyor
         "b:%s," % json.dumps(BESMELE, ensure_ascii=False),  # besmele
         "s:%s," % json.dumps(suralar, ensure_ascii=False, separators=(",", ":")),  # sure bilgileri
         "w:%s," % json.dumps(en_uzun, ensure_ascii=False, separators=(",", ":")),  # en uzun kelimeler
         "p:%s," % json.dumps(sayfa_bas, separators=(",", ":")),  # sayfa başlangıçları
         "j:%s," % json.dumps(cuz_sayfa, separators=(",", ":")),  # cüz başlangıç sayfaları
         "a:[",  # ayetler başlıyor
         ",\n".join(json.dumps(t, ensure_ascii=False) for t in ayetler),  # her ayet bir satır
         "]};"]  # bitiş
CIKTI.parent.mkdir(parents=True, exist_ok=True)  # klasörü oluştur
CIKTI.write_text("\n".join(cikti) + "\n", encoding="utf-8")  # dosyayı yaz
(CIKTI.parent.parent / "tools" / "CHECKSUM.txt").write_text(ozet + "  ayetler(6236, besmele ayrı, \\n ile birleşik, utf-8)\n", encoding="utf-8")  # özeti ayrı dosyaya da yaz
print("OK ayet=%d sayfa=%d cuz=%d besmele_ayiklanan=%d sha256=%s boyut=%d bayt" % (len(ayetler), 604, 30, ayiklanan_besmele, ozet, CIKTI.stat().st_size))  # rapor
