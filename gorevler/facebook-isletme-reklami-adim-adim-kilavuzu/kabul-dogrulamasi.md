# Kabul doğrulaması

Tarih: 3 Ekim 2026 · Ortam: Claude Code bulut konteyneri (Linux), Chromium 141, Python 3.

## Görev kabul maddeleri → kanıt

| # | Kabul maddesi (görev tanımından) | Somut çıktı | Çalıştırılmış kanıt | Durum |
|---|---|---|---|---|
| 1 | Başlangıçtan yayına kadar akışı anlat | `KILAVUZ.md` §3 (hesap → Sayfa → portfolyo → reklam hesabı → ödeme) → §4 kampanya → §5 reklam seti/reklam → **Yayınla** → §6 değerlendirme ve ilk 7 gün. Ayrıca §13 yayın öncesi kontrol listesi. | `araclar/dogrula.py` bölüm 3: "Başlangıçtan yayına akış ✅" | ✅ Belge olarak tamam |
| 2 | Farklı ekran çıktığında alternatif yolu göster | 12 adet `➜ Farklıysa` dalı, §0.3 genel kural, Yol A (§7 öne çıkar), Yol C (§8 telefon), §10'da 20 satırlık belirti → sebep → çözüm tablosu (Meta hata kodlarıyla), `harita.md` "yol kırılırsa" tablosu | `dogrula.py` bölüm 3: "12 adet 'Farklıysa' dalı ✅" | ✅ Belge olarak tamam |
| 3 | Gerçek güncel arayüz ve resmi kuralları doğrula | 39 resmi Meta kaynağı (`kaynaklar.md` §A). Menü adları Türkçe yardım makalelerinden alındı. 18 makalede kılavuzun dayandığı kilit ifadeler canlı sayfada arandı. | `dogrula.py` bölüm 2: 18/18 ✅. Bölüm 1: 68 bağlantının 66'sı HTTP 200 verdi. 2'si curl'de 403 aldı (Meta Reklam Kütüphanesi, jonloomer.com bot koruması) ama gerçek Chromium'da açıldı. **Toplam hata: 0** | ✅ Resmi metin düzeyinde doğrulandı |
| 4 | Gerçek hesapla yayın (canlı arayüz) | — | **Yapılmadı.** Kullanıcının Facebook kimliği, işletme bilgisi ve kartı gerekiyor. Uydurulmadı. | ⛔ Engelli (somut engel aşağıda) |
| 5 | İndirilebilir kalıcı teslim | `KILAVUZ.html` (tek dosya, çevrimdışı açılır, açık/koyu tema), `KILAVUZ.pdf` (A4, 23 sayfa) | `araclar/derle.py`. Chromium ile 390 px mobil ve 1280 px masaüstü ekran görüntüsü alındı, gözle kontrol edildi. Yatay taşma yok (scrollWidth = görünüm genişliği). PDF'te 12 "Farklıysa" dalı metin olarak doğrulandı. | ✅ |

## Görsel kontrol notları
- İlk derlemede mobilde yatay taşma vardı: kalın yazılmış uzun adresler kırılmıyordu
  (scrollWidth 604 > 390). Adresler tıklanabilir bağlantıya çevrildi ve `overflow-wrap:anywhere`
  eklendi. Sonuçta scrollWidth 390.
- Python-Markdown'ın GitHub'dan farklı liste kuralları yüzünden iç içe listeler ve bir uyarı
  kutusu düz metin ya da kod bloğu olarak çıkıyordu. Derleyiciye normalleştirme eklendi. HTML'de
  ham `**` ya da `>` kalıntısı kalmadığı betikle sayıldı (0).

## Somut engel ve senden gereken gerçek girdi
Canlı kabul (madde 4) için şu adımları **senin** yapman gerekiyor. Bu ortamdan yapılması ne
mümkün ne de uygun:
1. Kendi Facebook hesabınla business.facebook.com'a giriş (kimlik ve 2FA sende).
2. Kart bilgisi ve 3D Secure onayı (ödeme sende).
3. İlk reklamı yayınlayıp **Yayın** sütununda "Değerlendirmede → Aktif" geçişini görmek.

Ekranın kılavuzdan farklı çıktığı her yerin ekran görüntüsünü (kart numarası, e-posta ve
telefon gibi kişisel bilgileri karartarak) paylaşırsan kılavuz o dala göre güncellenebilir.

## Yeniden çalıştırma
```
python3 araclar/dogrula.py --rapor kabul-raporu-otomatik.md   # çıkış kodu 0 = geçti
pip install markdown && python3 araclar/derle.py               # HTML + PDF yeniden üretir
```
Otomatik raporun tamamı: `kabul-raporu-otomatik.md`.
