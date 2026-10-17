# ÜRÜN TANIMI: Kur'an Oku
## MVP özellikleri
| # | Özellik | Dayanağı | Öncelik |
|---|---|---|---|
| 1 | Tüm Kur'an gömülü (Uthmani metin, 604 sayfa, 30 cüz) — çevrimdışı, ilk açılışta indirme yok | Yorum #1, #4 (158 yorum) | P0 |
| 2 | Tek kutulu akıllı arama + canlı öneri: sure adı (Türkçe/aksansız/Arapça), cüz no, sayfa no, sure:ayet | Yorum #6; kullanıcı isteği | P0 |
| 3 | Kademeli yazı boyutu: kaydırıcı (%1 adım) + iki parmakla yumuşak büyütme, animasyonlu | Yorum #10; kullanıcı isteği | P0 |
| 4 | Sayfa görünümleri: Dokulu kağıt (kabartma yazı), Düz beyaz, Gece, Özel (sayfa rengi + yazı rengi + doku aç/kapa) | Kullanıcı isteği; yorum #9 | P0 |
| 5 | Kaldığın yerden devam (otomatik) + kalıcı yer imleri | Yorum #7 | P0 |
| 6 | Yazı tipi seçimi (2-4 Kur'an yazı tipi) | Kullanıcı isteği | P1 |
| 7 | Reklamsız, internet izni yok, ekran açık kalsın seçeneği | Yorum #3, #5 | P0 |

## Kapsam dışı
Meal, tefsir, ses/hafız, namaz vakti, kıble, hesap/bulut, bildirim, reklam, ezber modülü.

## Ana akışlar
### Akış 1: Kaldığı yerden okuma
1. Uygulama açılır → son okunan sayfa (0 dokunuş). 2. Sağdan sola kaydırarak sayfa çevir.
### Akış 2: Sure/cüz/sayfaya git
1. Üstteki ara düğmesi → 2. "ma" yaz → öneri: Mâide, Meâric, Mâûn… → dokun → sayfa açılır (3 dokunuş). "20" → 20. Cüz, 20. Sayfa önerileri.
### Akış 3: Görünüm ayarı
1. Sayfaya dokun → alt çubuk → "Aa" → sheet: boyut kaydırıcısı, sayfa stili, renkler, yazı tipi. Değişiklik anında canlı görünür.
### Akış 4: Yer imi
1. Sayfaya dokun → yer imi ikonu → kaydedildi. İçindekiler → Yer imleri sekmesi.

## Navigasyon
Alt sekme yok (okuma uygulaması, tam ekran). Okuma ekranı = ana ekran. Stack: Arama, İçindekiler (Sureler / Cüzler / Yer imleri sekmeleri). Sheet: Görünüm.

## Veri modeli
| Varlık | Alanlar | Nerede |
|---|---|---|
| Sure | no, arapça ad, türkçe ad, arama anahtarları, ayet sayısı, iniş yeri, başlangıç sayfası | assets (gömülü) |
| Ayet | sure, no, metin, sayfa, cüz | assets (gömülü, sıkıştırılmış) |
| Ayarlar | yazı boyutu, stil, sayfa rengi, yazı rengi, doku, yazı tipi, ekran açık | DataStore (yerel) |
| Okuma | son sayfa | DataStore |
| Yer imi | sayfa, tarih | DataStore |

## Backend: gerekli değil.
## Para modeli: Ücretsiz, reklamsız. Paywall yok.

## Teknoloji kararı (onaya sunuldu)
Fabrikanın varsayılanı Expo/React Native. Bu uygulamada **Kotlin + Jetpack Compose (yerli Android)** öneriyorum:
- Kullanıcının ana şikayeti "uygulamalar ağır": Compose APK ≈ 5-8 MB, Expo ≈ 30-45 MB.
- Arapça metin ölçekleme ve sayfa çevirmede yerli metin motoru daha akıcı.
- APK'yı bu bulut ortamında Expo hesabı (EAS) olmadan imzalayıp derleyebiliyoruz.
Tasarım kuralları aynen uygulanır: tüm değerler token'dan gelir; ikonlar Lucide setinden SVG→Compose ImageVector olarak aktarılır (16/20/24, tek strokeWidth).
