# İSTEKLER: Melodi Zil — kullanıcının istediği her şey ve durumu
> Kaynak: sohbetteki kullanıcı mesajlarının tamamı. Durum: ✅ yapıldı ve doğrulandı · ⚠️ yapıldı, bir kısmı bu ortamda doğrulanamadı · 🛠️ bu turda yapılıyor

| # | İstek (kullanıcının sözü) | Nasıl karşılandı | Durum |
|---|---|---|---|
| 1 | "Bir uygulama üret android mobil" | Kotlin + Jetpack Compose, minSdk 26, targetSdk 35 | ✅ |
| 2 | "YouTube linki verildiği zaman" | Bağlantı yapıştırma + YouTube "Paylaş → Melodi Zil"; NewPipeExtractor ile ses akışı | ✅ 2026-10-10: emülatörde YouTube "Paylaş" ile gelen bağlantı → 340 nota → 8-Bit → zil olarak kaydedildi, sistem varsayılan zili oldu |
| 3 | "10 dakikanın altındaki" | 600 sn sınırı, uzun videoda Türkçe hata | ✅ |
| 4 | "şarkıyı … melodiye çevirecek", "vokal söyler … melodi haline çevirecek" | v1.1.0: cihazda vokal ayırma (Spleeter, TFLite) + Melodia melodi çıkarma; söz kaybolur, ezgi kalır | ✅ (ölçülen doğru perde %67-68; bkz. #18) |
| 5 | "telefon zil sesi yapabilecek" | MediaStore'a WAV + RingtoneManager ile varsayılan zil/bildirim/alarm | ✅ (emülatörde sistem zili değişti) |
| 6 | "Birkaç farklı formda olacak kişi istediğini seçebilecek" | 8 form: Piyano, Müzik Kutusu, 8-Bit, Marimba, Flüt, Çan, Gitar, Synth | ✅ |
| 7 | "Mvp değil baya Google Play atacak hale getir … bitir" | İmzalı AAB + APK, gizlilik politikası, mağaza metni, veri güvenliği cevapları, 6 ekran, açık/koyu tema, TR/EN | ✅ + Gradle Play Publisher ile `./gradlew publishApps` (taslak, dahili test) |
| 8 | "Link olarak ver indireyim" | jsDelivr linki (Ç-001) + sohbet eki | ✅ |
| 9 | "Yüzde yüze geliyor takılıyor" | Uygulama içi indirme 416 döngüsü (D-012) + link sorunu (Ç-001) düzeltildi; regresyon testi | ✅ |
| 10 | "Belleğe kaydet: bir şeyi çözünce nasıl çözdüğünü yaz" | Drive HAFIZA.md (K-001) + docs/COZUMLER.md | ✅ |
| 11 | "Artık indirme ile alakalı bir sorun yaşamak istemiyorum" | İndirme döngüsü testi, link başlık + SHA-256 doğrulaması kuralı | ✅ |
| 12 | "Tek paragraf: ne istedik, ne yaptın, elimizde ne var" | Verildi | ✅ |
| 13 | "İstediğim her şeyi çıkar, yapmış mısın diye bak, sonra yap" | Bu dosya; her satır tek tek kontrol edildi | ✅ |
| 14 | "Yapılmışları bul onları kullan … yıldız almış github repo, skills, mcp" | Kullanılanlar: NewPipeExtractor (~2.0k★, GPL-3.0), OkHttp, Coil, Material 3; uygulama-fabrikası skill'i; Google Drive MCP. Bu turda: Gradle Play Publisher 3.12.1 (4.3k★, MIT; 4.x Gradle 9.1 istediği için 3.x) + standart mağaza metadata klasörü `app/src/main/play/` (fastlane 42k★ ile aynı biçim). v1.1.0'da: Deezer Spleeter (26k★, MIT) vokal modeli TFLite'a aktarıldı, Essentia (3k★) Melodia ve mir_eval/mirdata ölçüm için. Bilerek kullanılmayan: Spotify basic-pitch (çok sesli transkripsiyon; zil için tek melodi gerekiyor) | ✅ |
| 15 | "Görsel üretme aracın ile tek görsel üstünde birçok varyasyon … başka yapay zekaya seçtir … uygula" | Görsel üretim modeli bu oturumda yok; ikon varyasyonları kodla tek sayfada çizilir, ayrı bir ajana (bağımsız değerlendirici) seçtirildi: C "Ses dalgası → nota" (20/25). 3 iyileştirmeyle uygulandı: ikon, açılış, mağaza görselleri (fabrika/IKON_SECIMI.md) | ✅ |
| 16 | "En son bitince drive hesabına at" | Drive `Uygulama Fabrikası/melodi-zil/`: belgeler, ikon/mağaza görselleri, İNDİR.md (APK/AAB linkleri). APK ikili dosyası Drive aracının boyut sınırı nedeniyle link olarak | ✅ |
| 17 | "%100'de kalıyor indirmiyor" | Sunucu tarafı Android Chrome kimliğiyle doğrulandı (sorun yok); telefon tarafı nedenleri (Chrome onayı, iç tarayıcı) ve 3 yol; APK sohbet eki | ✅ (Ç-010) |
| 18 | "Hiç alakası yok, müziğe hiç benzemiyor" | Ölçüm altyapısı + Melodia yöntemi + Spleeter vokal ayırma (TFLite) + ölçümlü ayarlar. Doğru perde %31→%67 (vokal seti), %41→%68 (gerçek şarkı); nota F 0.27→0.52 | ✅ (D-013) |
| 19 | "Bu ne ve ne işe yarıyor tek cümle … bitene kadar bitir sonra kart olarak ver" | Tek cümle verildi; emülatörde vokal ayırma doğrulandı; kart: https://claude.ai/artifact/VVuBCMtTF7mHr9VcPNq8VD | ✅ |
| 20 | "Bitti mi … tek cümlede tam anlat … artifact link olarak yüzde yüzde takılmasın" | Kartta link açılmaz, kopyalanıp Chrome'a yapıştırılır (iç tarayıcı takılması önlenir); "Yine de indir" adımı ayrıca yazılı; link Android Chrome kimliğiyle indirilip SHA-256 doğrulandı | ✅ |
| 21 | "Proje sohbetlerinde yazdığım her şey okunsun ve yapılsın … bitirilip verilsin … kart olarak verilsin" | Tüm mesajlar transkriptten tek tek çıkarıldı (bu liste); kalıcı kural HAFIZA K-003; tarayıcıya hiç girmeyen yol için 26 MB arm64 APK (sohbet eki sınırı 30 MiB altında) üretildi ve sohbete eklendi; kart istek listesiyle güncellendi | ✅ |
| 22 | "Böyle oluyor olmuyor herhalde" (işlem ekranı boş, ekran görüntüsü) | Kök neden: telefon uygulamayı işlem sırasında arka planda kapatıyor, geri açılınca ekran boş kalıyordu. Ön plan servisi + bildirim, "İşlem yarıda kaldı / Tekrar dene" ekranı, vokal ayırmada çökme koruması, doğru süre metni. Emülatörde süreç öldürme testiyle doğrulandı. v1.1.1 | ✅ (Ç-013) |

## Yeni tur (2026-10-16) — "baştan, çalışır halde" promptu
Durum: ⬜ başlamadı · 🛠️ yapılıyor · ✅ yapıldı+doğrulandı · ⚠️ kısmen

| # | İstek | Durum | Kanıt / not |
|---|---|---|---|
| 23 | Melodi dinleyince şarkı olduğu anlaşılsın (en önemli şart) | 🛠️ | ölçüm → algoritma sırası |
| 24 | Önce ölçüm altyapısı (mir_eval RPA + nota F), açık veri + ≥5 gerçek pop şarkısı | 🛠️ | araclar/degerlendirme |
| 25 | Hedef: doğru perde ≥ %80, nota F ≥ 0.65 (gerçek şarkıda) | ⬜ | |
| 26 | Hazır modelleri ölçerek seç (Demucs/Spleeter, RMVPE/CREPE/FCPE, basic-pitch); seçimi sayıyla gerekçelendir | 🛠️ | |
| 27 | Titreme/kayma/kırık nota temizle, tona (gam) oturt | ⬜ | |
| 28 | ≥3 bilinen şarkı için 30 sn melodi MP3 örneği (kullanıcı dinleyip onaylayacak) | ⬜ | |
| 29 | Kotlin+Compose+M3, minSdk 26, targetSdk 35 | ✅ (önceki kod) | |
| 30 | Uzun işler ön plan servisi + bildirim; boş ekran asla; süreç ölünce "Tekrar dene" | ⚠️ (önceki kod, yeniden test edilecek) | |
| 31 | Ekranda gerçek süre yaz | ⬜ | yeni modelle yeniden ölçülecek |
| 32 | NewPipeExtractor (GPL-3.0) + Play reddederse B planı | ⚠️ | B planı yazılacak |
| 33 | Test: API 26 + güncel Android'de uçtan uca; zil gerçekten değişti mi | ⬜ | |
| 34 | Test: süreç öldürme (am kill / kill -9) | ⬜ | |
| 35 | Test: tam uzunlukta (3–4 dk) şarkı | ⬜ | |
| 36 | APK sohbete dosya eki (<30 MB, gerekirse arm64 ayrı); link + Chrome "Yine de indir" notu; Chrome kimliğiyle indirip SHA-256 doğrula | ⬜ | |
| 37 | Tek kart (artifact): ne olduğu, indirme, önce/sonra ses, istek listesi, kullanım | ⬜ | |
| 38 | Çözülen her sorun: belirti → kök neden → çözüm → doğrulama | 🛠️ | docs/COZUMLER.md |
| 39 | Bitince Google Drive'a belgeler + indirme linkleri | ⬜ | |
| 40 | İmzalı AAB + APK, mağaza metni, gizlilik politikası | ⚠️ (önceki kod) | |
| 41 | Kullanıcı kabulü: 3 şarkıyı tanıdı / APK'yı kurdu / YouTube'dan zil yaptı | ⬜ | kullanıcıdan gelecek |
