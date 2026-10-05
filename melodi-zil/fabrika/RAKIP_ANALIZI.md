# RAKİP ANALİZİ: Melodi Zil

Kaynak: `araclar/rakip_bul.py` ile Google Play araması ("ringtone maker", "zil sesi yapma", "youtube ringtone", "melody ringtone maker"), 2026-10-05. Ham tablo: `RAKIPLER_HAM.csv`.

## Rakipler
| # | Uygulama | Play paket adı | Puan | İndirme | Para modeli | Büyük/orta |
|---|---|---|---|---|---|---|
| 1 | Ringtone Maker (Big Bang Inc.) | com.herman.ringtone | 4.70 | 100M+ | Ücretsiz + reklam, Pro 1.99$ | Büyük |
| 2 | MP3 Cutter and Ringtone Maker (InShot) | ringtone.maker.mp3.cutter.audio | 4.67 | 50M+ | Ücretsiz + reklam + abonelik | Büyük |
| 3 | Zedge | net.zedge.android | 4.67 | 500M+ | Reklam + Premium abonelik | Büyük (katalog) |
| 4 | Ringtone Maker: Music Cutter | ringtonemaker.musiccutter.customringtones.freeringtonemaker | 4.62 | 10M+ | Reklam | Orta |
| 5 | Ringtone Maker-Audio Cutter (AndroidRock) | com.androidrocker.audiocutter | 4.54 | 10M+ | Reklam | Orta |
| 6 | Ringtone Maker & MP3 Cutter (Mobile_V5) | tool.music.ringtonemaker | 4.40 | 10M+ | Reklam | Orta |
| 7 | Ringtones for Android™ (Trela) | com.bestringtonesapps.freeringtonesforandroid | 4.19 | 100M+ | Reklam + ücretli sesler | Büyük (katalog) |
| 8 | Melody Ringtones Pro (Ghauri Bros) | com.ghauribros.MelodyRingtones | – | 10K+ | Reklam | Küçük (katalog, hazır melodiler) |
| 9 | Nota Ringtones Pro | com.ghauribros.NotaRingtones | – | 100K+ | Reklam | Küçük (katalog) |
| 10 | Video to MP3 & Ringtone Maker (TarrySoft) | com.video.tomp3 | 4.60 | 1M+ | Reklam | Orta |
| 11 | Garage Ringtones Pro | garage.ringtone.maker.audio.mp3.converter.pro | 4.26 | 100K+ | 2.49$ tek seferlik | Küçük |
| 12 | Ringtone Maker, MP3 Cutter (Hihoay) | com.taymay.ringtone | 3.62 | 500K+ | Reklam | Küçük |

(+7 benzer; tam liste CSV'de.) **Hiçbiri şarkıdan melodi üretmiyor**; "melody" adlı olanlar hazır katalog.

## Derin inceleme (en iyi 3)

### Ringtone Maker (com.herman.ringtone) — 100M+
- Ana akış: Aç → şarkı listesi (1 dokunuş) → şarkı seç (2) → dalga formunda başlangıç/bitiş tut (3-4) → kaydet (5) → "zil yap?" dialog (6). Toplam 5-6 dokunuş.
- Navigasyon: Tek liste ekranı + düzenleyici; menüler eski (Holo dönemi).
- Onboarding: Yok; doğrudan izin ister.
- Paywall: Yok, banner reklam; Pro ayrı uygulama.
- Akıllıca 5 şey: (1) kayıttan hemen sonra "zil/bildirim/alarm yap" sorusu, (2) kişiye özel zil atama, (3) kayıt sonrası dosya adı önerisi, (4) hafif, hızlı, (5) sistem zil klasörüne doğru kaydediyor.
- Kötü 5 şey (yorumlarla): (1) eski görünüm, (2) "doesn't work after update / can't hear anything" (ses çıkmıyor şikayetleri), (3) izin akışı kafa karıştırıcı, (4) dalga formu küçük ekranda kullanışsız, (5) yeni telefona geçişte ziller kayboluyor.
- Görsel dil: Sistem varsayılanları, marka yok.

### MP3 Cutter and Ringtone Maker (InShot) — 50M+
- Ana akış: Aç → "Zil yap" kartı (1) → dosya seç (2) → dalga formu + tutamaçlar (3-4) → kaydet (5) → paylaş/ayarla (6).
- Navigasyon: Ana ekranda 4 büyük kart (zil yap, birleştir, kaydet, …) + alt "Dosyalarım".
- Onboarding: 1 ekran izin açıklaması.
- Paywall: Reklamları kaldır aboneliği, her kayıttan sonra tam ekran reklam.
- Akıllıca 5 şey: (1) büyük kartlı ana ekran, net eylemler, (2) fade in/out seçenekleri, (3) çıktı formatı/bit hızı seçimi, (4) "Dosyalarım" sekmesi ile yeniden ayarlama, (5) modern Material görünüm.
- Kötü 5 şey: (1) "overloaded with ads" en sık şikayet, (2) kaydedince kalite düşüyor, (3) ses seviyesi ayarı çalışmıyor, (4) kayıt konumu belirsiz, (5) eski Android'de hata.
- Görsel dil: Mor/pembe gradyanlar, yoğun.

### Zedge — 500M+
- Ana akış: Aç → arama (1) → sonuç (2) → önizleme (3) → "Ayarla" (4) → tür seç (5). Reklam araya girer.
- Navigasyon: 5 alt sekme (keşfet, arama, koleksiyon, …).
- Onboarding: 3 sayfa + bildirim izni.
- Paywall: Premium abonelik, kredili indirme.
- Akıllıca 5 şey: (1) önizleme oynatıcı tek dokunuş, (2) "ayarla" sheet'inde zil/bildirim/alarm/kişi seçimi, (3) koleksiyonlar, (4) kaliteli görsel dil, (5) sesli önizleme sırasında dalga animasyonu.
- Kötü 5 şey: (1) "çok reklam", (2) "ses gelmiyor", (3) istenen şarkı yok, (4) ağır uygulama, (5) premium baskısı.
- Görsel dil: Koyu tema, mor vurgu, büyük görseller.

## Çalacağımız fikirler
| Fikir | Nereden | Neden iyi |
|---|---|---|
| Kayıttan hemen sonra zil/bildirim/alarm seçtirme (tek sheet) | Ringtone Maker, Zedge | Kullanıcının asıl amacı "ayarlamak"; ekstra adım istemiyor |
| "Dosyalarım/Zillerim" sekmesi ile yeniden ayarlama, silme, paylaşma | InShot | Yorumlarda "ziller yeni telefona gelmedi / nerede" şikayetleri |
| Önizleme tek dokunuş, çalarken ilerleme göstergesi | Zedge | Ses gelmiyor şikayetlerine karşı görsel geri bildirim |
| Ana ekranda tek net eylem | InShot (kartlar) | Kafa karışıklığı yok |
| Fade-out ve kısa/uzun seçenekleri | InShot | Zil "aniden kesilmesin" |

## Farkımız
1. **Kesmez, melodiye çevirir:** vokal/söz yok, sade ezgi — hiçbir rakipte yok.
2. **8 farklı form** (piyano, müzik kutusu, 8-bit, marimba, flüt, çan, gitar, synth) tek dokunuşla değişir, anında dinlenir.
3. **Otomatik nakarat bulma:** en yoğun 30 saniye kendiliğinden seçilir; dalga formuyla uğraşmak yok.
4. **Reklam yok, hesap yok, sunucu yok:** tüm işlem telefonda; en sık şikayet (reklam) kökten çözülür.
5. **YouTube "Paylaş" ile tek dokunuşta başlar.**
