# Google Play'e yayınlama rehberi (Kur'an Okuyucu)

Hazır olanlar bu klasörde ve `../dist/` içinde. Aşağıdakileri sırayla yapman yeterli.

## 0. Önce bilmen gerekenler
- Play yeni uygulamalarda **APK değil AAB** ister: `dist/kuran.aab` yükle. `dist/kuran.apk` sadece telefona doğrudan kurmak içindir.
- Hedef Android sürümü **36** (31 Ağustos 2026'dan beri zorunlu). Uygulama buna göre derlendi.
- Geliştirici hesabı: tek seferlik 25 USD. Kimlik doğrulaması istenir. Yeni **kişisel** hesaplarda, üretime çıkmadan önce kapalı testte belirli sayıda test kullanıcısıyla (genelde 12 kişi, 14 gün) test şartı olabilir. Play Console'da "Test et ve yayınla" bölümünde güncel şartı gör.
- **İmza anahtarı (`kuran-upload.jks`) sana ayrıca gönderildi. Kaybetme, kimseyle paylaşma, depoya koyma.** Play App Signing açıkken bu "yükleme anahtarıdır". Kaybedersen Play Destek'ten sıfırlatılabilir.

## 1. Uygulamayı oluştur
Play Console → Uygulama oluştur
- Ad: `Kur'an Okuyucu`
- Dil: Türkçe
- Uygulama (oyun değil), Ücretsiz

## 2. Mağaza girişi (Ana mağaza girişi)
`ilan-tr.md` içindeki metinleri yapıştır (kısa açıklama, uzun açıklama).
- Uygulama simgesi: `assets/simge-512.png`
- Öne çıkan grafik: `assets/one-cikan-gorsel-1024x500.png`
- Telefon ekran görüntüleri: `assets/ekran-1…6-*.png` (hepsini yükle)
- E-posta (zorunlu): kendi e-postanı yaz.

## 3. Uygulama içeriği bölümleri
| Bölüm | Cevap |
|---|---|
| Gizlilik politikası URL | Aşağıdaki "Gizlilik politikası linki" |
| Reklamlar | Hayır, uygulamada reklam yok |
| Uygulama erişimi | Tüm işlevler özel erişim gerektirmeden kullanılabilir |
| İçerik derecelendirme | Anketi doldur: kategori "Referans/Yardımcı", şiddet/cinsellik/kumar/uyuşturucu yok. Dini metin içerir, bunu belirt. Beklenen: herkes |
| Hedef kitle | 13+ ya da "Tüm yaşlar" seç. Çocuklara özel değil |
| Haber uygulaması | Hayır |
| COVID-19, finans, sağlık | Hayır |
| Veri güvenliği | **Veri toplanmıyor, paylaşılmıyor.** Hiçbir veri türünü işaretleme. Güvenlik uygulamaları: veri şifreleme/silme soruları uygulanmaz (veri toplanmadığı için) |
| Reklam kimliği | Kullanmıyor |

Uygulama **internet izni istemez**, hiçbir üçüncü taraf SDK içermez. Bu yüzden Veri güvenliği formu "veri toplanmıyor" olarak doldurulur.

## 4. Gizlilik politikası linki
Hazır metin: `../docs/kuran-gizlilik.md` (Türkçe + İngilizce). Yayında şu adres kullanılabilir:
`https://github.com/jannet5/Jn/blob/<commit>/docs/kuran-gizlilik.md` (güncel permalink sohbette verildi).
Kendi sitende yayınlamak istersen metni aynen kopyalayabilirsin.

## 5. Sürüm yükle
Test et ve yayınla → Üretim (ya da önce Dahili/Kapalı test) → Yeni sürüm oluştur
- Play Uygulama İmzalama: **Devam / Kabul et**
- `dist/kuran.aab` dosyasını yükle
- Sürüm notları: `ilan-tr.md` içindeki "Sürüm notları"
- İncelemeye gönder. İlk inceleme birkaç gün sürebilir.

## Güncelleme çıkarırken
- `android/app/build.gradle.kts` içinde `versionCode` değerini 1 artır (2, 3...) ve `versionName`'i güncelle.
- Aynı anahtarla yeniden imzala (`keystore.properties` + `keystore/` klasörünü `android/` altına geri koy).
- Derleme: `cd android && gradle bundleRelease` → `app/build/outputs/bundle/release/app-release.aab`

## Neden reddedilmez (kısa kontrol)
- Sadece web sitesi sarmalayıcı değil: bütün içerik uygulamanın içinde, çevrimdışı çalışır (Minimum işlev politikası).
- Telif: Kur'an metni Tanzil kaynağından, yazı tipi SIL OFL lisanslıdır. Uygulama içinde kaynak notu vardır.
- Hedef API 36, 64-bit uyumlu (yerel kod yok), AAB, kenardan kenara ekran ve yeni geri hareketi desteklenir.
