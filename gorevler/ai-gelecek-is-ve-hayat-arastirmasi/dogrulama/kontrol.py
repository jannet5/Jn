"""Araştırma teslimi için kabul kontrolleri.

Çalıştırma: python3 dogrulama/kontrol.py   (çıkış kodu 0 = tüm kontroller geçti)
"""
import pathlib
import re
import sys

KOK = pathlib.Path(__file__).resolve().parent.parent
hatalar = []


def oku(ad):
    return (KOK / ad).read_text(encoding="utf-8")


def kontrol(kosul, mesaj):
    print(("GECTI " if kosul else "KALDI ") + mesaj)
    if not kosul:
        hatalar.append(mesaj)


# 1) Zorunlu dosyalar
for ad in ["README.md", "harita.md", "arastirma.md", "kaynaklar.md", "kanit-matrisi.md",
           "meslek-risk-tablosu.md", "youtube-video-listesi.md", "calisma-gunlugu.md"]:
    kontrol((KOK / ad).is_file(), f"dosya var: {ad}")

kaynak = oku("kaynaklar.md")
matris = oku("kanit-matrisi.md")
rapor = oku("arastirma.md")
risk = oku("meslek-risk-tablosu.md")
yt = oku("youtube-video-listesi.md")

# 2) Kaynaklar: her satırda tam HTTPS bağlantısı, http:// yok, kodlar benzersiz
satirlar = [s for s in kaynak.splitlines() if re.match(r"\| [RTD]\d+b? \|", s)]
kodlar = [re.match(r"\| ([RTD]\d+b?) \|", s).group(1) for s in satirlar]
kontrol(len(satirlar) >= 40, f"en az 40 kaynak satırı ({len(satirlar)})")
kontrol(all("https://" in s for s in satirlar), "her kaynak satırında tam https:// bağlantısı")
kontrol("http://" not in kaynak, "güvensiz http:// bağlantısı yok")
kontrol(len(kodlar) == len(set(kodlar)), "kaynak kodları benzersiz")
platformlar = {"Hacker News", "Medium", "X (aktaran haber)", "Usta forumu", "Türkçe Substack"}
kontrol(all(p in kaynak for p in platformlar), "forum/Medium/X/usta forumu/Türkçe topluluk kaynakları var")

# 3) Kanıt matrisi: geçerli sınıf + var olan kaynak kodu; hem KANIT hem TAHMIN var
gecerli = {"KANIT", "ERKEN-SINYAL", "TAHMIN", "TOPLULUK", "DOGRULANMAMIS"}
msatir = [s for s in matris.splitlines() if re.match(r"\| K\d+ \|", s)]
kontrol(len(msatir) >= 30, f"kanıt matrisinde en az 30 iddia ({len(msatir)})")
siniflar = []
for s in msatir:
    hucre = [h.strip() for h in s.strip("|").split("|")]
    kid, sinif, kref = hucre[0], hucre[2], hucre[3]
    siniflar.append(sinif)
    kontrol(sinif in gecerli, f"{kid} geçerli sınıf ({sinif})")
    refs = re.findall(r"[RTD]\d+b?", kref)
    kontrol(bool(refs) and all(r in kodlar for r in refs), f"{kid} kaynakları kaynaklar.md'de var ({kref})")
kontrol({"KANIT", "TAHMIN"} <= set(siniflar), "matriste hem KANIT hem TAHMIN sınıfı var")

# 4) Rapor: etiketler ve zorunlu bölümler
for etiket in ["[KANIT", "[ERKEN SİNYAL", "[TAHMİN", "[TOPLULUK"]:
    kontrol(etiket in rapor, f"raporda {etiket}] etiketi kullanılmış")
for bolum in ["Bölüm 1", "Bölüm 2", "Bölüm 3", "Bölüm 4", "Bölüm 5", "Bölüm 6", "Sonsöz"]:
    kontrol(bolum in rapor, f"raporda {bolum} var")
kontrol("YouTube transkript erişimi IP" in rapor, "raporda YouTube eksikliği açıkça yazılmış")

# 5) Risk tablosu: yazılım ve fiziksel ayrı bölümler, her satırda dayanak kodu var
kontrol("## A. Yazılım" in risk and "## B. Fiziksel" in risk, "risk tablosu yazılım/fiziksel ayrı")
rsatir = [s for s in risk.splitlines() if s.startswith("| ") and "K" in s.split("|")[-2]]
kontrol(len(rsatir) >= 15, f"risk tablosunda en az 15 dayanaklı meslek ({len(rsatir)})")
mkodlar = {re.match(r"\| (K\d+) \|", s).group(1) for s in msatir}
for s in rsatir:
    refs = re.findall(r"K\d+", s.split("|")[-2])
    kontrol(all(r in mkodlar for r in refs), f"risk dayanakları matriste var: {s.split('|')[1].strip()}")

# 6) YouTube listesi: 15+ benzersiz video
vids = set(re.findall(r"watch\?v=([\w-]{11})", yt))
kontrol(len(vids) >= 15, f"en az 15 benzersiz YouTube videosu ({len(vids)})")

# 7) Gizlilik: özel kaynak metninden imza ifadeler ve kişisel yol yok
yasak = ["Kanka şunun hakkında", "C:\\Users", "Documents\\Codex", "womwom", "kaynak.txt'"]
tum = "\n".join(p.read_text(encoding="utf-8") for p in KOK.rglob("*.md"))
tum += "\n".join(p.read_text(encoding="utf-8") for p in KOK.rglob("*.py") if p.name != "kontrol.py")
for y in yasak:
    kontrol(y not in tum, f"özel içerik sızmamış: {y!r}")
kontrol(not any(KOK.rglob("kaynak.txt")) and not any(KOK.rglob("gorev.md")),
        "kaynak.txt / gorev.md depoda yok")

print()
print(f"SONUC: {'TUMU GECTI' if not hatalar else str(len(hatalar)) + ' KONTROL KALDI'}")
sys.exit(1 if hatalar else 0)
