# Çalışma günlüğü

Tarih: 3 Ekim 2026 · Ortam: Claude Code bulut oturumu, depo `jannet5/Jn`, dal
`claude/loving-goldberg-3vdzv0`. Çalışma yalnız bu klasörde yapıldı. Depodaki diğer projelere
(`android-app`, `windows-agent`, `docs`) dokunulmadı.

## 1. Ne istendi
Bir işletmenin Facebook üzerinden nasıl reklam açıp yayınlayacağının bütün süreçleri
yazılacak. Kullanıcı yazılanı birebir uygulayacak, o yüzden ekranın farklı çıkabileceği
yerlerde ne yapılacağı da yazılacak. Görev tanımındaki üç kabul maddesi: (1) başlangıçtan yayına
akış, (2) farklı ekranda alternatif yol, (3) güncel arayüz ve resmi kuralların doğrulanması.
Ek kurallar: araştırma işi hayali bir ürüne çevrilmeyecek. Kimlik, ödeme ve test sonucu
uydurulmayacak. Kullanıcının kaynak sohbet metni public depoya konmayacak.

## 2. Kaynak dosyaların işlenmesi
- Mesajda verilen `kaynak.txt` ve `gorev.md`, yalnız bulut çalışma alanının geçici
  (scratchpad) `ozel/` klasörüne kaydedildi. **Depoya eklenmedi.** Kaynak metin 5. satırdaki
  tek bir istekten oluşuyor, son kullanıcı düzeltmesi yok.
- Ürün kararı: istenen şey bir **kılavuz**. Uygulama ya da sunucu yazılmadı. Yazılan iki küçük
  araç (doğrulayıcı ve derleyici) kılavuzun kendisini test etmek ve indirilebilir biçime
  çevirmek için.

## 3. Araştırma (kullanılan gerçek araçlar ve yöntem)
1. **Web araması** (WebSearch, standart ve geniş mod): Meta Ads Manager 2026 adımları, işletme
   portfolyosu, Türkiye KDV ve ödeme, konum ücreti, yeni hesap kapanmaları (Reddit/Shopify), Ekşi
   ve Şikâyetvar deneyimleri, Advantage+ varsayılanları, Meta adına oltalama.
2. **Resmi Meta yardım sayfaları:** `WebFetch` bu sayfalarda yalnız başlığı döndürdü, çünkü
   içerik JavaScript ile yükleniyor. Çözüm: `curl` ile HTML indirildi. Makale gövdesinin
   sayfadaki `"__html":"…"` JSON alanlarında durduğu bulundu ve küçük bir Python çıkarıcıyla
   (`mh.py`, scratchpad) metne çevrildi. `?locale=tr_TR` parametresiyle **Türkçe arayüz
   adları** doğrudan Meta'nın kendi metninden alındı.
3. Makale kimlikleri, indirilen sayfalardaki iç bağlantılar taranarak bulundu (`links.py`). Bu
   yolla 45 makale TR (gerektiğinde EN) olarak okundu. Bunlardan 39'u kılavuzda kaynak olarak kullanıldı
   (`kaynaklar.md` §A).
4. Önemli bulgular:
   - Meta'nın kendi sayfası "seçenekler hesap türüne göre değişebilir" diyor. Kılavuzun "ekran
     farklıysa" yapısı bu yüzden zorunlu.
   - Türkiye'de 1 Temmuz 2026'dan beri **%5 konum ücreti** var (resmi makale + haberler).
   - Türkiye için mevcut bakiye yöntemine özel yerel bir ödeme yöntemi listelenmiyor. Kart ya da
     PayPal kullanılıyor.
   - Meta'nın Türkçe bölümündeki "reklam seti nasıl kurulur" bağlantısı kırık
     (`/business/help/XXXXX`). Reklam seti adımları başka resmi makalelerden derlendi ve bu
     durum kılavuzda açıkça yazıldı.

## 4. Kararlar
| Karar | Gerekçe |
|---|---|
| Ana yol: Reklam Yöneticisi (Yol B) | Resmi makalelerin tüm amaç ve ayarları burada anlatılıyor. Topluluk deneyimi öne çıkar butonunun kontrol eksikliğinde birleşiyor. |
| Yedek yollar: Öne çıkar (A), telefon (C) | Kullanıcının ekranı ya da cihazı farklı olabilir |
| Menü adlarını Türkçe Meta metninden almak | "Yazdığın gibi olmazsa" riskini azaltmanın en güvenilir yolu |
| Üçüncü taraf reklam araçlarını önermemek | Tek işletmenin ilk reklamı için ücretli ve gereksiz katman, ek erişim izni demek |
| Vergi konusunda kesin tavsiye vermemek | KDV uygulaması işletmeye göre değişiyor, mali müşavir yönlendirmesi yapıldı |
| HTML + PDF üretmek | Kullanıcı adımları telefonda açık tutarak bilgisayarda uygulayabilsin |

## 5. Sorunlar ve çözümler
| Sorun | Çözüm |
|---|---|
| WebFetch Meta yardım sayfalarında boş içerik döndürdü | curl + gömülü JSON çıkarıcı |
| Meta Reklam Kütüphanesi ve jonloomer.com curl'e 403 verdi (bot koruması) | Doğrulayıcıya gerçek Chromium ile ikinci kontrol eklendi. İkisi de açıldı ("Ad Library", "Advantage+ Campaign Creation…"). |
| Chromium proxy üzerinden `ERR_CERT_AUTHORITY_INVALID` verdi | TLS doğrulaması kapatılmadı. Yalnız ortamın kendi proxy CA'sının açık anahtar özeti `--ignore-certificate-errors-spki-list` ile güvenilir eklendi. Denemede kullanılan güvensiz bayraklı geçici betik silindi. |
| Mobil görünümde yatay taşma (604 px) | Adresler bağlantıya çevrildi, `overflow-wrap:anywhere` eklendi → 390 px |
| Python-Markdown iç içe listeleri ve bir uyarı kutusunu bozdu | Derleyiciye girinti ve boş satır normalleştirme eklendi, MD'de iki yerde boş satır düzeltildi. Ham markdown kalıntısı 0. |
| Kabul belgesinde sorun giderme satır sayısı yanlış yazılmıştı (22) | Sayıldı (20) ve düzeltildi |

## 6. Doğrulamalar (çalıştırılan komutlar)
- `python3 araclar/dogrula.py --rapor kabul-raporu-otomatik.md` → çıkış kodu **0**. 68
  bağlantı (66 × HTTP 200, 2 × bot korumalı ama Chromium'da açıldı), 18 resmi makalede kilit
  ifade kontrolü 18/18, 3 kabul maddesi ✅.
- `python3 araclar/derle.py` → `KILAVUZ.html`, `KILAVUZ.pdf` (A4, 23 sayfa, `pdfinfo` ile).
  Chromium ekran görüntüleri (390 px mobil, 1280 px masaüstü, §3.5 ve §10 yakın plan) ve PDF
  9. sayfa gözle incelendi.
- **Yapılmayan:** Gerçek Facebook hesabıyla reklam oluşturma ve yayınlama, Windows ve telefon
  cihaz testi. Kimlik ve ödeme kullanıcıda. Kılavuzu kullanıcı kendi hesabında uygulayacak.

## 7. Kalıcı teslim
- Bu klasör `claude/loving-goldberg-3vdzv0` dalına commit ve push edildi, uzaktan geri okundu.
  Commit kimliği ve geri okuma sonucu oturum özetinde yer alıyor.
- Kaynak sohbet metnini **içermeyen** özel ZIP bulut çalışma alanında üretildi. SHA-256'sı
  hesaplanıp ZIP geri açılarak doğrulandı (ayrıntı oturum özetinde). ZIP public depoya konmadı.
