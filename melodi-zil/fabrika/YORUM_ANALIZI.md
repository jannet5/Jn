# YORUM ANALİZİ: Melodi Zil
Toplam yorum: 2327 (12 rakip, tr + en, en yeni) · Kaynaklar: yorumlar/*.csv · Yıldız dağılımı: 5★ 1693 · 4★ 141 · 3★ 42 · 2★ 45 · 1★ 406

## En sık 10
| # | Madde | Kova | Sayı | Rakipler | Alıntı |
|---|---|---|---|---|---|
| 1 | Çok fazla reklam | Şikayet | 153 | Zedge, InShot, Mobile_V5, Trela | "overloaded with ads and nonsense… if you think that makes me want to pay" |
| 2 | Ses kalitesi / ses çıkmıyor | Şikayet | 146 | Zedge, Ringtone Maker Pro, AndroidRock | "I can't hear ANYTHING!!!" / "SESLERE TIKLIYORUM SES GELMİYOR" |
| 3 | Kolay / basit | Sevilen | 142 | AndroidRock, Ringtone Maker | "it's a simple, easy recorder/editor" |
| 4 | Kesme/trim işlevi | Sevilen | 104 | hepsi | "take any supported sound bytes and make them into distinctive rings" |
| 5 | Premium / ücret baskısı | Şikayet | 70 | Zedge, Trela | "Ücret istemek nasıl bir utanmazlıktır ya ücretsiz ayarlanmıyo" |
| 6 | Kaydetme / ayarlama çalışmıyor | Şikayet | 54 | AndroidRock, Ringtone Maker | "everything you try to do gives you some form of error" |
| 7 | Format / bit hızı | Talep | 24 | InShot | "mp3 ve m4a… istediğiniz bit hızında çıktı sağlıyor" |
| 8 | İzin karmaşası | Şikayet | 12 | Mobile_V5 | "izin verdim ama hâlâ ayarlanmıyor" |
| 9 | Çökme | Şikayet | 6 | Ringpod | "your app keeps on crashing" |
| 10 | YouTube'dan alma / melodi | Talep | 6 + 3 | AndroidRock | "İki şarkıyı melodi olarak ayarladım gayet güzel… telefona myt üzerinden indirdim" |

## Talepler
| Talep | Sayı | Alıntı | MVP'ye girsin mi |
|---|---|---|---|
| YouTube'daki şarkıyı doğrudan zil yapmak | 6 | "telefona myt üzerinden indirdim sonra kestim" (dolambaçlı yol) | ✅ Ana özellik |
| Melodi / enstrümantal versiyon | 3 | "melodi olarak ayarladım" | ✅ Ana özellik |
| Çıktı formatı seçimi | 24 | "istediğiniz bit hızında" | ❌ Kapsam dışı (WAV sabit; zil için yeterli) |
| Kişiye özel zil | 8 | "assign to contact" | ❌ v1.1 (WRITE_CONTACTS izni gerekir) |

## Şikayetler
| Şikayet | Sayı | Alıntı | Bizde nasıl önleriz |
|---|---|---|---|
| Reklam | 153 | "her adımda reklam… uygulamadan çıkarken bile" | Reklam yok, SDK yok |
| Ses gelmiyor / kalite | 146 | "ses gelmiyor" | Önizleme AudioTrack ile doğrudan; piyano rulosu + oynatma çizgisi görsel geri bildirim; -1 dBFS normalizasyon |
| Ayarlanmıyor / hata | 54 | "gives you some form of error" | WRITE_SETTINGS akışı açık dialog ile anlatılır; kayıt MediaStore'a doğru klasöre (Ringtones/Notifications/Alarms) |
| Ücret baskısı | 70 | "ücretsiz ayarlanmıyo" | Tamamen ücretsiz, paywall yok |
| İzin karmaşası | 12 | — | Tek isteğe bağlı izin, gerektiği anda, Türkçe açıklamayla |

## Sevilenler
| Özellik | Sayı | Alıntı |
|---|---|---|
| Basitlik, az adım | 142 | "simple, easy" |
| Hemen zil/bildirim/alarm yapma | 104 | "make them into distinctive rings" |
| Kayıtlı dosyalara geri dönüp yeniden ayarlama | 31 | "my ringtones didn't transfer" (olmayınca şikayet) |

## Tasarım / UX
| Yorum | Sayı | Alıntı | Tasarım dersi |
|---|---|---|---|
| Eski görünüm | 9 | "looks like 2012" | Material 3, tek marka rengi, kartlar çerçeveli |
| Dalga formu küçük ekranda zor | 7 | "hard to drag the markers" | Tutamaç yerine tek kaydırıcı + "Otomatik" |
| Çok renk / gradyan | 4 | "garish" | Gradyan yok, 1 vurgu rengi |
