# Uygulama verisini üretir: KFGQPC Hafs metni + Medine mushafı (KFGQPC baskısı) sayfa/satır düzeni.
# Girdi: quran.com QDC API sayfa dosyaları (mushaf=5) ve Tanzil metadata (cüz/hizb/secde için)
# Çıktı: assets/quran.tsv, assets/mushaf.tsv, assets/surahs.tsv (başlangıç sayfaları güncellenir)
import json, sys  # standart
from collections import defaultdict  # gruplama
from pathlib import Path  # yol
qpc, tanzil, assets = Path(sys.argv[1]), Path(sys.argv[2]), Path(sys.argv[3])  # klasör/dosyalar
kelimeler = {}  # kelime id → (ayet, kelime)
for p in range(1, 605):  # sayfa dosyaları
    for v in json.load(open(qpc / f"{p:03d}.json"))["verses"]:
        for w in v["words"]: kelimeler[w["id"]] = (v["verse_key"], w)
ayet_kelime = defaultdict(list)  # ayet → kelimeler (ayet sonu hariç)
ayet_sayfa = {}  # ayet → başladığı sayfa
sayfalar = defaultdict(lambda: defaultdict(list))  # sayfa → satır → kelimeler
for wid in sorted(kelimeler):  # mushaf sırası
    vk, w = kelimeler[wid]
    sayfalar[w["page_number"]][w["line_number"]].append(w["text_qpc_hafs"])
    ayet_sayfa.setdefault(vk, w["page_number"])
    if w["char_type_name"] == "word": ayet_kelime[vk].append(w["text_qpc_hafs"])
    ilk = sayfalar[w["page_number"]][w["line_number"]]
    if len(ilk) == 1: sayfalar[w["page_number"]]["vk", w["line_number"]] = vk  # satırın ilk ayeti
assert len(ayet_kelime) == 6236
meta = {}  # Tanzil'den cüz/hizb/secde (aynı numaralandırma)
for s in json.loads(tanzil.read_text(encoding="utf-8"))["data"]["surahs"]:
    for a in s["ayahs"]: meta[f"{s['number']}:{a['numberInSurah']}"] = (a["juz"], a["hizbQuarter"], 0 if a["sajda"] is False else 1)
# quran.tsv
satirlar = []
for vk in sorted(ayet_kelime, key=lambda k: tuple(map(int, k.split(":")))):
    s, a = vk.split(":"); j, h, sec = meta[vk]
    satirlar.append("\t".join([s, a, str(ayet_sayfa[vk]), str(j), str(h), str(sec), " ".join(ayet_kelime[vk])]))
(assets / "quran.tsv").write_text("\n".join(satirlar) + "\n", encoding="utf-8")
# mushaf.tsv: sayfa satır tür veri
cikti, bekleyen = [], []
for p in range(1, 605):
    S = sayfalar[p]; bos = []
    for L in range(1, 16):
        if L not in S: bos.append((p, L)); continue
        vk = S["vk", L]; s, a = map(int, vk.split(":"))
        if bos or bekleyen:
            hepsi = bekleyen + bos
            if p in (1, 2): hepsi = hepsi[-(1 if s == 1 else 2):]  # ilk iki sayfada üst boşluk süs alanı
            gerek = ["H"] if s in (1, 9) else ["H", "B"]
            assert a == 1 and len(hepsi) == len(gerek), (p, L, vk, hepsi)
            for (pp, LL), t in zip(hepsi, gerek): cikti.append((pp, LL, f"{pp}\t{LL}\t{t}\t{s if t == 'H' else ''}"))
            bos, bekleyen = [], []
        cikti.append((p, L, f"{p}\t{L}\tW\t{vk}\t" + " ".join(S[L])))
    bekleyen = bos
cikti.sort()
(assets / "mushaf.tsv").write_text("\n".join(r for _, _, r in cikti) + "\n", encoding="utf-8")
# surahs.tsv başlangıç sayfaları
sur = []
for l in (assets / "surahs.tsv").read_text(encoding="utf-8").splitlines():
    c = l.split("\t"); c[7] = str(ayet_sayfa[f"{c[0]}:1"]); sur.append("\t".join(c))
(assets / "surahs.tsv").write_text("\n".join(sur) + "\n", encoding="utf-8")
print(len(satirlar), "ayet,", len(cikti), "mushaf satırı")
