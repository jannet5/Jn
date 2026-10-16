# ÜRÜN TANIMI: Melodi Zil

## MVP özellikleri (6)
| # | Özellik | Dayanağı | Öncelik |
|---|---|---|---|
| 1 | YouTube bağlantısından (<10 dk) ses alıp **baskın melodiyi** nota nota çıkarma, cihazda | YORUM: "YouTube'dan zil" talebi; FIKIR: temel fark | P0 |
| 2 | **8 form**: piyano, müzik kutusu, 8-bit, marimba, flüt, çan, gitar, synth — anında önizleme | Kullanıcı isteği: "birkaç farklı form, kişi seçebilecek" | P0 |
| 3 | **Otomatik bölüm** (en yoğun 15/20/30/40 sn) + manuel kaydırıcı | RAKIP: "dalga formu küçük ekranda zor" | P0 |
| 4 | **Kaydet ve ayarla**: zil / bildirim / alarm, isteğe bağlı varsayılan yapma, izin akışı | YORUM: "ayarlanmıyor", RAKIP: kayıt sonrası sheet | P0 |
| 5 | **Zillerim**: liste, yeniden ayarla, paylaş, sil | YORUM: "zillerim nerede / yeni telefona gelmedi" | P1 |
| 6 | **Yerel ses dosyasından** aynı akış (YouTube politikası riskine karşı ve offline kullanım) | FIKIR: risk 1 | P1 |

## Kapsam dışı (v1.0)
- Kişiye özel zil (WRITE_CONTACTS) — v1.1
- Çıktı formatı/bit hızı seçimi — WAV yeterli
- Çok sesli (akor) sentez, davul — melodi yeter
- Hesap, bulut, paylaşımlı kütüphane, reklam, abonelik
- iOS

## Ana akışlar
### Akış 1: Bağlantıdan zil (asıl akış, 5 dokunuş)
1. YouTube'da "Paylaş → Melodi Zil" (ya da Ana sayfa → yapıştır → "Melodiye çevir")
2. İşlem ekranı (otomatik; 4 adım, iptal edilebilir)
3. Sonuç: form seç (chip), dinle
4. "Zil sesi yap" → sheet: tür + varsayılan anahtarı → "Kaydet"
5. İzin gerekiyorsa dialog → sistem ekranı → geri dönüşte otomatik uygulanır

### Akış 2: Dosyadan zil
1. Ana sayfa → "Telefondan ses dosyası seç" → sistem seçici
2-5. Akış 1 ile aynı (adım 1 "Dosya açılıyor/kopyalanıyor")

### Akış 3: Kayıtlı zili yeniden ayarla
1. Zillerim → satır → sheet (zil/bildirim/alarm yap, paylaş, sil)

## Navigasyon
Alt sekmeler (3): Ana sayfa · Zillerim · Ayarlar
Stack ekranları: İşlem, Sonuç
Modal'lar: Kayıt sheet'i, kütüphane eylem sheet'i, izin dialog'u, silme onayı, bilgi dialog'ları
Onboarding: 1 ekran (ilk açılış)

## Veri modeli
| Varlık | Alanlar | Nerede |
|---|---|---|
| SavedRingtone | id, title, instrumentId, kind, uri, createdAtMillis, lengthSec, sourceVideoId?, thumbnailUrl? | Yerel: DataStore (JSON) |
| Prefs | theme, onboarded, lastInstrument | Yerel: DataStore |
| Zil dosyası | WAV 44.1 kHz mono 16-bit | MediaStore: Ringtones/MelodiZil, Notifications/MelodiZil, Alarms/MelodiZil |
| Melody (geçici) | notes[midi,start,dur,vel], durationSec | Bellek (ViewModel) |

## Backend: gerekli mi, neden
**Gerekmez.** İndirme (NewPipeExtractor + OkHttp), çözme (MediaCodec), melodi çıkarma (STFT + harmonik toplam + Viterbi) ve sentez cihazda. Sunucu olmaması: maliyet sıfır, gizlilik tam, YouTube'un bot engellemesine karşı daha dayanıklı (istekler kullanıcı IP'sinden).

## Para modeli ve paywall konumu
v1.0: Tamamen ücretsiz, reklamsız (yorum analizindeki #1 şikayet reklam). İleride: tek seferlik "Destek ol" satın alması (Ayarlar'da, paywall yok).
