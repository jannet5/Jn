# KFGQPC Hafs kelime/satır verisinden (quran.com API) mushaf sayfa düzeni dosyası üretir.
# Çıktı satırları:  sayfa<TAB>satır<TAB>tür<TAB>veri
#   tür H = sure başlığı (veri: sure no), B = besmele, W = kelimeler (veri: "sure:ayet<TAB>kelime kelime ...")
import json, sys  # standart
from collections import defaultdict  # gruplama
from pathlib import Path  # yol
kaynak, hedef = Path(sys.argv[1]), Path(sys.argv[2])  # klasörler
kelimeler = {}  # kelime id → (ayet anahtarı, kelime)
for p in range(1, 605):  # her sayfa dosyası
    for v in json.load(open(kaynak / f"{p:03d}.json"))["verses"]:  # her ayet
        for w in v["words"]: kelimeler[w["id"]] = (v["verse_key"], w)  # tekrarlar tek
sayfalar = defaultdict(lambda: defaultdict(list))  # sayfa → satır → kelimeler
for wid in sorted(kelimeler):  # mushaf sırasıyla
    vk, w = kelimeler[wid]
    sayfalar[w["page_number"]][w["line_number"]].append((vk, w["char_type_name"], w["text_qpc_hafs"]))
cikti = []  # satırlar
bekleyen = []  # bir önceki sayfanın sonunda kalan boş satırlar (sonraki surenin başlığı)
for p in range(1, 605):
    satirlar = sayfalar[p]  # bu sayfanın dolu satırları
    son = 15  # her sayfa 15 satır (ilk iki sayfada üst yarı süs alanıdır)
    bos = []  # ardışık boş satırlar
    for L in range(1, son + 1):
        if L not in satirlar: bos.append((p, L)); continue  # boş satır: başlık/besmele adayı
        ilk_vk, tur, _ = satirlar[L][0]  # satırın ilk kelimesi
        s, a = map(int, ilk_vk.split(":"))  # sure, ayet
        bas = a == 1 and (L == 1 or satirlar.get(L - 1, [(None,)])[0][0] is None or (L - 1) not in satirlar or satirlar[L - 1][-1][0] != ilk_vk)  # sure burada mı başlıyor
        if bos or bekleyen:  # önünde boş satır var
            hepsi = bekleyen + bos  # önceki sayfadan taşanlar dahil
            if p in (1, 2): hepsi = hepsi[-(1 if s == 1 else 2):]  # ilk iki sayfada üstteki boş satırlar süs alanı
            assert a == 1, (p, L, ilk_vk, hepsi)  # boş satırlar sadece sure başında olur
            gerek = ["H"] if s in (1, 9) else ["H", "B"]  # Fâtiha'da besmele ayettir, Tevbe'de yok
            assert len(hepsi) == len(gerek), (p, L, ilk_vk, hepsi, gerek)  # sayı tutmalı
            for (pp, LL), t in zip(hepsi, gerek): cikti.append(f"{pp}\t{LL}\t{t}\t{s if t == 'H' else ''}")  # başlık/besmele satırı
            bos, bekleyen = [], []
        vk = ilk_vk  # satırın ilk ayeti
        cikti.append(f"{p}\t{L}\tW\t{vk}\t" + " ".join(t for _, _, t in satirlar[L]))  # kelime satırı
    bekleyen = bos  # sayfa sonunda kalan boşluklar sonraki sayfaya
assert not bekleyen
cikti.sort(key=lambda r: (int(r.split("\t")[0]), int(r.split("\t")[1])))  # sayfa/satır sırası
hedef.write_text("\n".join(cikti) + "\n", encoding="utf-8")  # yaz
print(len(cikti), "satır")
