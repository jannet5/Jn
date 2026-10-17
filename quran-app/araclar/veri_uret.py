# Kur'an verisini (Tanzil Uthmani, alquran.cloud üzerinden) uygulamanın okuyacağı sade TSV dosyalarına çeviren betik
import json  # JSON okumak için
import sys  # argümanlar için
from pathlib import Path  # dosya yolları için

KAYNAK = Path(sys.argv[1])  # indirilen quran-uthmani.json yolu
HEDEF = Path(sys.argv[2])  # assets klasörü

# Diyanet yazımıyla 114 surenin Türkçe adı ve ek arama adları (virgülle)
TR = """Fâtiha|Elham,Fatiha
Bakara|
Âl-i İmrân|Ali Imran,Al-i Imran
Nisâ|
Mâide|
En'âm|Enam
A'râf|Araf
Enfâl|
Tevbe|Berâe
Yûnus|
Hûd|
Yûsuf|
Ra'd|Rad
İbrâhîm|
Hicr|
Nahl|
İsrâ|Benî İsrâil
Kehf|
Meryem|
Tâhâ|Taha
Enbiyâ|
Hac|Hacc
Mü'minûn|Muminun
Nûr|
Furkân|
Şuarâ|
Neml|
Kasas|
Ankebût|
Rûm|
Lokmân|
Secde|
Ahzâb|
Sebe'|Sebe
Fâtır|
Yâsîn|Yasin,Yasin-i Şerif
Sâffât|
Sâd|
Zümer|
Mü'min|Gâfir,Mumin
Fussilet|Hâ-Mîm Secde
Şûrâ|
Zuhruf|
Duhân|
Câsiye|
Ahkâf|
Muhammed|Kıtâl
Fetih|Feth
Hucurât|
Kâf|
Zâriyât|
Tûr|
Necm|
Kamer|
Rahmân|
Vâkıa|Vakia
Hadîd|
Mücâdele|
Haşr|
Mümtehine|
Saf|
Cuma|Cum'a
Münâfikûn|
Teğâbün|
Talâk|
Tahrîm|
Mülk|Tebâreke
Kalem|
Hâkka|
Meâric|
Nûh|
Cin|
Müzzemmil|
Müddessir|
Kıyâme|
İnsân|Dehr
Mürselât|
Nebe'|Amme,Nebe
Nâziât|
Abese|
Tekvîr|
İnfitâr|
Mutaffifîn|
İnşikâk|
Bürûc|
Târık|
A'lâ|Ala
Gâşiye|
Fecr|
Beled|
Şems|
Leyl|
Duhâ|
İnşirâh|Şerh,Elem Neşrah
Tîn|
Alak|İkra
Kadr|
Beyyine|
Zilzâl|Zelzele
Âdiyât|
Kâria|
Tekâsür|
Asr|
Hümeze|
Fîl|
Kureyş|
Mâûn|
Kevser|
Kâfirûn|
Nasr|
Tebbet|Mesed,Leheb
İhlâs|
Felak|
Nâs|""".strip().split("\n")
assert len(TR) == 114  # 114 sure olmalı

veri = json.loads(KAYNAK.read_text(encoding="utf-8"))["data"]["surahs"]  # sureler okunuyor
BESMELE_KELIME = 4  # besmele 4 kelimedir
ayet_satirlari = []  # ayet TSV satırları
sure_satirlari = []  # sure TSV satırları
for s in veri:  # her sure dolaşılıyor
    no = s["number"]  # sure numarası
    ad_tr, ek = TR[no - 1].split("|")  # Türkçe ad ve ek adlar
    ilk_sayfa = s["ayahs"][0]["page"]  # surenin başladığı sayfa
    tur = "M" if s["revelationType"] == "Meccan" else "D"  # Mekkî / Medenî
    sure_satirlari.append("\t".join([str(no), s["name"].replace("ـ", ""), ad_tr, ek, s["englishName"], str(len(s["ayahs"])), tur, str(ilk_sayfa)]))  # sure satırı
    for a in s["ayahs"]:  # her ayet dolaşılıyor
        metin = a["text"].replace("﻿", "").strip()  # BOM ve boşluklar atılıyor
        if a["numberInSurah"] == 1 and no not in (1, 9):  # ilk ayette besmele öne eklenmiş (Fâtiha ve Tevbe hariç)
            kelimeler = metin.split(" ")  # kelimelere bölünüyor
            assert "".join(c for c in kelimeler[3] if "\u0621" <= c <= "\u064A" or c == "\u0671").endswith("رحيم"), (no, kelimeler[:4])  # 4. kelime "er-rahîm" olmalı (harekesiz karşılaştırma)
            metin = " ".join(kelimeler[BESMELE_KELIME:])  # besmele ayrılıyor (başlık olarak çizilecek)
        secde = 0 if a["sajda"] is False else 1  # secde ayeti mi
        ayet_satirlari.append("\t".join([str(no), str(a["numberInSurah"]), str(a["page"]), str(a["juz"]), str(a["hizbQuarter"]), str(secde), metin]))  # ayet satırı

HEDEF.mkdir(parents=True, exist_ok=True)  # hedef klasör
(HEDEF / "quran.tsv").write_text("\n".join(ayet_satirlari) + "\n", encoding="utf-8")  # ayetler yazılıyor
(HEDEF / "surahs.tsv").write_text("\n".join(sure_satirlari) + "\n", encoding="utf-8")  # sureler yazılıyor
print(len(ayet_satirlari), "ayet,", len(sure_satirlari), "sure")  # özet
