# FİKİR: Melodi Zil

## Tek cümle
Sevdiği şarkıyı zil sesi yapmak isteyen ama "sözlü/orijinal kayıt" değil, eski Nokia zilleri gibi **sade bir melodi** isteyen kişiler için; YouTube bağlantısı verilen (<10 dk) şarkının baskın melodisini otomatik çıkarıp piyano, müzik kutusu, 8-bit gibi 8 farklı formda zil sesine çeviren Android uygulaması.

## Hedef kullanıcı
- Kim: 16-45 yaş, telefonunu kişiselleştirmeyi seven, teknik bilgisi olmayan kullanıcı.
- Ne zaman/nerede kullanır: Yeni bir şarkıyı sevdiğinde, YouTube'da dinlerken "Paylaş → Melodi Zil" ile; ya da bağlantıyı yapıştırarak.
- Şu an sorunu nasıl çözüyor: (a) MP3 kesme uygulamalarıyla orijinal kaydı kesiyor (vokalli, "melodi" değil); (b) Zedge gibi kataloglarda hazır melodi arıyor (istediği şarkı yok, reklam çok); (c) YouTube'da "ringtone piano version" arayıp indirme sitelerinden mp3 indiriyor (zahmetli, virüs riski).

## 4 Test
| Test | Sonuç | Açıklama |
|---|---|---|
| Sorun | ✅ | Yorum analizinde "melodi/piano/instrumental" isteği doğrudan az (3 yorum) ama "ringtone maker" kategorisinde 100M+ indirme var; mevcut araçların hepsi "kes" yapıyor, hiçbiri "melodiye çevir" yapmıyor. Kullanıcı kendisi de bunu talep etti. |
| Talep | ✅ | Rakipler: 19 uygulama, 4'ü 10M+, 2'si 100M+ indirme. "Melody Ringtones", "Nota Ringtones" gibi katalog uygulamaları var (hazır melodiler); üretken (kendi şarkından) olanı yok. |
| Fark | ✅ | Tek cümle: "Kesmez, melodiye çevirir." Vokal kaybolur, ezgi kalır; 8 farklı tını; işlem tamamen telefonda, reklam yok. |
| Boyut | ✅ | Tek kişi: evet. Sunucu yok (işlem cihazda), 6 ekran, 1 ağır algoritma (melodi çıkarma). Bu oturumda tamamı üretildi. |

## Fikir adayları (kategori taramasından)
Kullanıcı fikri hazır verdi; tarama yapılmadı.

## Riskler (dürüst)
1. **Google Play politikası:** YouTube içeriği indiren uygulamalar YouTube Hizmet Şartları'na aykırı sayılır ve Play "Kullanıcı Verileri / Fikri Mülkiyet" politikaları gereği reddedilebilir ya da yayından kaldırılabilir. Bu riski azaltmak için uygulama **yerel ses dosyasından** da çalışır ve mağaza metninde YouTube'u ön plana çıkarmamak önerilir (bkz. KALITE_RAPORU §Yayın).
2. **YouTube tarafı değişiklikleri:** Akış çözümleme NewPipeExtractor'a dayanır; YouTube sık değişir. Kütüphane sürümünü güncel tutmak gerekir.
3. **Melodi kalitesi:** Çok enstrümanlı/rap/elektronik parçalarda melodi belirsiz olabilir. "Otomatik bölüm" ve manuel kaydırıcı bunu dengeler.
