# İSTEKLER: Melodi Zil — kullanıcının istediği her şey ve durumu
> Kaynak: sohbetteki kullanıcı mesajlarının tamamı. Durum: ✅ yapıldı ve doğrulandı · ⚠️ yapıldı, bir kısmı bu ortamda doğrulanamadı · 🛠️ bu turda yapılıyor

| # | İstek (kullanıcının sözü) | Nasıl karşılandı | Durum |
|---|---|---|---|
| 1 | "Bir uygulama üret android mobil" | Kotlin + Jetpack Compose, minSdk 26, targetSdk 35 | ✅ |
| 2 | "YouTube linki verildiği zaman" | Bağlantı yapıştırma + YouTube "Paylaş → Melodi Zil"; NewPipeExtractor ile ses akışı | ✅ 2026-10-10: emülatörde YouTube "Paylaş" ile gelen bağlantı → 340 nota → 8-Bit → zil olarak kaydedildi, sistem varsayılan zili oldu |
| 3 | "10 dakikanın altındaki" | 600 sn sınırı, uzun videoda Türkçe hata | ✅ |
| 4 | "şarkıyı … melodiye çevirecek", "vokal söyler … melodi haline çevirecek" | Cihaz üstü melodi çıkarma (STFT + harmonik toplam + Viterbi); vokal/söz kaybolur, ezgi kalır | ✅ (gerçek şarkıda 337-340 nota, sentetik testte %92) |
| 5 | "telefon zil sesi yapabilecek" | MediaStore'a WAV + RingtoneManager ile varsayılan zil/bildirim/alarm | ✅ (emülatörde sistem zili değişti) |
| 6 | "Birkaç farklı formda olacak kişi istediğini seçebilecek" | 8 form: Piyano, Müzik Kutusu, 8-Bit, Marimba, Flüt, Çan, Gitar, Synth | ✅ |
| 7 | "Mvp değil baya Google Play atacak hale getir … bitir" | İmzalı AAB + APK, gizlilik politikası, mağaza metni, veri güvenliği cevapları, 6 ekran, açık/koyu tema, TR/EN | ✅ + Gradle Play Publisher ile `./gradlew publishApps` (taslak, dahili test) |
| 8 | "Link olarak ver indireyim" | jsDelivr linki (Ç-001) + sohbet eki | ✅ |
| 9 | "Yüzde yüze geliyor takılıyor" | Uygulama içi indirme 416 döngüsü (D-012) + link sorunu (Ç-001) düzeltildi; regresyon testi | ✅ |
| 10 | "Belleğe kaydet: bir şeyi çözünce nasıl çözdüğünü yaz" | Drive HAFIZA.md (K-001) + docs/COZUMLER.md | ✅ |
| 11 | "Artık indirme ile alakalı bir sorun yaşamak istemiyorum" | İndirme döngüsü testi, link başlık + SHA-256 doğrulaması kuralı | ✅ |
| 12 | "Tek paragraf: ne istedik, ne yaptın, elimizde ne var" | Verildi | ✅ |
| 13 | "İstediğim her şeyi çıkar, yapmış mısın diye bak, sonra yap" | Bu dosya; her satır tek tek kontrol edildi | ✅ |
| 14 | "Yapılmışları bul onları kullan … yıldız almış github repo, skills, mcp" | Kullanılanlar: NewPipeExtractor (~2.0k★, GPL-3.0), OkHttp, Coil, Material 3; uygulama-fabrikası skill'i; Google Drive MCP. Bu turda: Gradle Play Publisher 3.12.1 (4.3k★, MIT; 4.x Gradle 9.1 istediği için 3.x) + standart mağaza metadata klasörü `app/src/main/play/` (fastlane 42k★ ile aynı biçim). Bilerek kullanılmayan: Spotify basic-pitch (çok sesli transkripsiyon; zil için tek melodi gerekiyor, TFLite modeli APK'yı büyütür) | ✅ |
| 15 | "Görsel üretme aracın ile tek görsel üstünde birçok varyasyon … başka yapay zekaya seçtir … uygula" | Görsel üretim modeli bu oturumda yok; ikon varyasyonları kodla tek sayfada çizilir, ayrı bir ajana (bağımsız değerlendirici) seçtirildi: C "Ses dalgası → nota" (20/25). 3 iyileştirmeyle uygulandı: ikon, açılış, mağaza görselleri (fabrika/IKON_SECIMI.md) | ✅ |
| 16 | "En son bitince drive hesabına at" | Drive `Uygulama Fabrikası/melodi-zil/`: belgeler, ikon/mağaza görselleri, İNDİR.md (APK/AAB linkleri). APK ikili dosyası Drive aracının boyut sınırı nedeniyle link olarak | ✅ |
