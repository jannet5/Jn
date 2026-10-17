import re  # kalıp aramak için düzenli ifade modülü
import sys  # argüman okumak ve çıkış kodu vermek için sys modülü
from collections import defaultdict  # kural bazında gruplamak için varsayılan sözlük
from pathlib import Path  # dosya yollarını dolaşmak için Path sınıfı

UZANTILAR = {".tsx", ".ts", ".jsx", ".js"}  # denetlenecek dosya uzantıları
ATLA_KLASOR = {"node_modules", ".expo", "dist", "build", "ios", "android", ".git"}  # içine girilmeyecek klasörler
ATLA_YOL = ("components/ui/", "lib/theme.ts", "tailwind.config", "global.css", "metro.config", "babel.config")  # token tanımlanan ya da hazır gelen dosyalar (buralarda ham değer serbest)
IKON_BOYUTLARI = {16, 20, 24}  # izin verilen ikon boyutları
RENKLER = "slate|gray|zinc|neutral|stone|red|orange|amber|yellow|lime|green|emerald|teal|cyan|sky|blue|indigo|violet|purple|fuchsia|pink|rose"  # Tailwind'in hazır renk adları

HATALAR = [  # (kural adı, kalıp) çiftleri: bunlar HATA sayılır
    ("Ham hex renk (token kullan)", re.compile(r"#[0-9a-fA-F]{3,8}\b")),  # #fff, #1a2b3c gibi hex renkler
    ("rgb/hsl renk (token kullan)", re.compile(r"\b(rgba?|hsla?)\(")),  # rgb(), rgba(), hsl() renkleri
    ("Tailwind hazır renk (token kullan: bg-primary, text-muted-foreground…)", re.compile(rf"\b(bg|text|border|ring|fill|stroke|from|via|to|divide|placeholder|shadow)-({RENKLER})-\d{{2,3}}\b")),  # bg-blue-500 gibi sınıflar
    ("bg/text-white/black (token kullan)", re.compile(r"\b(bg|text|border)-(white|black)\b")),  # bg-white, text-black gibi sınıflar
    ("Keyfi Tailwind değeri (skala dışı)", re.compile(r"\b[a-z][a-z-]*-\[[^\]]+\]")),  # p-[13px], text-[15px] gibi sınıflar
    ("Inline sabit ölçü (token/sınıf kullan)", re.compile(r"\b(fontSize|lineHeight|padding\w*|margin\w*|gap|rowGap|columnGap|borderRadius)\s*:\s*\d")),  # style içinde sayı
    ("Inline renk (token kullan)", re.compile(r"\b(color|backgroundColor|borderColor|tintColor)\s*:\s*['\"]")),  # style içinde renk metni
    ("Yasak ikon kütüphanesi (sadece lucide-react-native)", re.compile(r"from\s+['\"](@expo/vector-icons|react-native-vector-icons|react-icons|phosphor-react-native|react-native-heroicons|@tabler/icons)")),  # başka ikon setleri
]  # hata listesi bitiyor

UYARILAR = [  # bunlar UYARI sayılır (bakılmalı ama işi durdurmaz)
    ("react-native'den ham Text/TouchableOpacity (components/ui kullan)", re.compile(r"import\s*\{[^}]*\b(Text|TouchableOpacity)\b[^}]*\}\s*from\s*['\"]react-native['\"]")),  # ham bileşen importu
    ("Emoji kullanımı (ikon yerine emoji olmaz)", re.compile("[\U0001F300-\U0001FAFF☀-➿]")),  # emoji karakterleri
]  # uyarı listesi bitiyor

BOYUT = re.compile(r"\bsize=\{(\d+)\}")  # size={N} kalıbı (ikon boyutu)
KALINLIK = re.compile(r"\bstrokeWidth=\{([\d.]+)\}")  # strokeWidth={N} kalıbı


def dosyalar(kok: Path):  # denetlenecek dosyaları bulan üretici fonksiyon
    for yol in kok.rglob("*"):  # kök altındaki tüm yollar dolaşılıyor
        if yol.suffix not in UZANTILAR or not yol.is_file():  # uzantı uygun değilse ya da dosya değilse
            continue  # bu yol atlanıyor
        if any(parca in ATLA_KLASOR for parca in yol.parts):  # yol atlanacak bir klasörden geçiyorsa
            continue  # bu yol atlanıyor
        goreli = yol.relative_to(kok).as_posix()  # kök klasöre göre göreli yol metni alınıyor
        if any(a in goreli for a in ATLA_YOL):  # token/hazır dosyalardan biriyse
            continue  # bu yol atlanıyor
        yield yol, goreli  # dosya ve göreli yolu döndürülüyor


def main() -> None:  # programın ana fonksiyonu
    if len(sys.argv) != 2:  # tam bir argüman verilmediyse
        sys.exit("Kullanım: python araclar/tasarim_denetimi.py <expo-proje-klasoru>")  # kullanım yazdırılıp çıkılıyor
    kok = Path(sys.argv[1])  # denetlenecek klasör alınıyor
    if not kok.is_dir():  # klasör yoksa
        sys.exit(f"Klasör bulunamadı: {kok}")  # hata verilip çıkılıyor

    hatalar = defaultdict(list)  # kural adına göre hata konumları tutulacak sözlük
    uyarilar = defaultdict(list)  # kural adına göre uyarı konumları tutulacak sözlük
    kalinliklar = defaultdict(list)  # strokeWidth değerine göre konumlar tutulacak sözlük
    sayac = 0  # denetlenen dosya sayısı

    for yol, goreli in dosyalar(kok):  # her uygun dosya dolaşılıyor
        sayac += 1  # dosya sayacı artırılıyor
        for no, satir in enumerate(yol.read_text(encoding="utf-8", errors="ignore").splitlines(), 1):  # dosya satır satır numarasıyla okunuyor
            s = satir.strip()  # satırın başındaki ve sonundaki boşluklar atılıyor
            if s.startswith(("//", "*", "/*")):  # yorum satırıysa
                continue  # denetlenmiyor
            konum = f"{goreli}:{no}"  # dosya:satır biçiminde konum metni hazırlanıyor
            for ad, kalip in HATALAR:  # her hata kuralı deneniyor
                if kalip.search(satir):  # satır kurala takılıyorsa
                    hatalar[ad].append(f"{konum}  →  {s[:90]}")  # konum ve satırın kısa hali hatalara ekleniyor
            for ad, kalip in UYARILAR:  # her uyarı kuralı deneniyor
                if kalip.search(satir):  # satır kurala takılıyorsa
                    uyarilar[ad].append(f"{konum}  →  {s[:90]}")  # konum uyarılara ekleniyor
            for m in BOYUT.finditer(satir):  # satırdaki her size={N} bulunuyor
                if int(m.group(1)) not in IKON_BOYUTLARI:  # boyut izinli değilse
                    hatalar["İkon boyutu 16/20/24 dışında"].append(f"{konum}  →  size={m.group(1)}")  # hata olarak ekleniyor
            for m in KALINLIK.finditer(satir):  # satırdaki her strokeWidth={N} bulunuyor
                kalinliklar[m.group(1)].append(konum)  # değere göre konum kaydediliyor
            if goreli.startswith("app/") and "/_layout" not in goreli and "export default" in satir:  # ekran dosyasında default export satırıysa
                icerik = yol.read_text(encoding="utf-8", errors="ignore")  # dosyanın tamamı okunuyor
                if "<Screen" not in icerik and "<Stack" not in icerik and "<Tabs" not in icerik:  # ekran Screen ile sarılmamışsa
                    uyarilar["Ekran <Screen> ile sarılmamış"].append(konum)  # uyarı ekleniyor

    if len(kalinliklar) > 1:  # birden fazla farklı strokeWidth kullanılmışsa
        for deger, konumlar in kalinliklar.items():  # her değer dolaşılıyor
            hatalar["Farklı strokeWidth değerleri (tek değer olmalı)"].append(f"strokeWidth={deger}: {len(konumlar)} yer, örn. {konumlar[0]}")  # hata ekleniyor

    print(f"\n🔍 Tasarım denetimi: {kok}  ({sayac} dosya)\n")  # rapor başlığı yazdırılıyor
    for baslik, grup, isaret in (("HATALAR", hatalar, "❌"), ("UYARILAR", uyarilar, "⚠️ ")):  # önce hatalar sonra uyarılar yazdırılacak
        toplam = sum(len(v) for v in grup.values())  # gruptaki toplam bulgu sayısı hesaplanıyor
        print(f"{isaret} {baslik}: {toplam}")  # grup başlığı ve sayısı yazdırılıyor
        for ad, liste in grup.items():  # her kural dolaşılıyor
            print(f"   • {ad} ({len(liste)})")  # kural adı ve sayısı yazdırılıyor
            for k in liste[:15]:  # ilk 15 bulgu gösteriliyor
                print(f"       {k}")  # bulgu yazdırılıyor
            if len(liste) > 15:  # 15'ten fazla bulgu varsa
                print(f"       … +{len(liste) - 15} tane daha")  # kalan sayı yazdırılıyor
        print()  # gruplar arasına boş satır konuyor

    toplam_hata = sum(len(v) for v in hatalar.values())  # toplam hata sayısı hesaplanıyor
    print("✅ Temiz, devam edebilirsin." if toplam_hata == 0 else "⛔ Hatalar düzeltilmeden 'bitti' denemez.")  # sonuç mesajı yazdırılıyor
    sys.exit(1 if toplam_hata else 0)  # hata varsa 1, yoksa 0 koduyla çıkılıyor


if __name__ == "__main__":  # dosya doğrudan çalıştırıldıysa
    main()  # ana fonksiyon çağrılıyor
