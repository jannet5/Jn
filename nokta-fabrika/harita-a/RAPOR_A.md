# RAPOR_A — Harita A (Compose + tasarım sistemi)

## Teslim
- APK: `dist/nokta-a.apk` 1.264.971 B (~1.21 MiB), release, minify+shrinkResources açık, apksigner: **v2 doğrulandı** (v1/v3 yok; minSdk 26).
- AAB: `dist/nokta-a.aab` 3.189.416 B (~3.04 MiB). `dist/SHA256SUMS.txt`.
- applicationId `app.nokta.a` (nokta-app'in `app.nokta.list`'i ile çakışmaz), minSdk 26, targetSdk/compileSdk 36, sürüm 1.0.0.
- Komut: `./gradlew clean testDebugUnitTest recordRoborazziDebug assembleRelease bundleRelease` -> BUILD SUCCESSFUL.

## Gerçekten kullanılan araç / kaynak
- ORTAK_SPEC.md, HARITALAR.md, NOTLAR/{tasarim_referanslari, ai_slop_onleme, araclar, yorum_analizi, play_store_gereksinimleri}.md okundu (Things/Google Tasks ölçüleri bu notlardan; kendim siteleri yeniden açmadım, bu yüzden Things sayıları "3. taraf analiz, doğrulanmamış" statüsünde).
- Maven metadata (curl): reorderable en yeni sürüm **3.1.0** (maven-metadata, lastUpdated 2026-04-20; `release` etiketi yok, listenin sonu 3.1.0), Compose BOM, AGP, Kotlin, Roborazzi 1.76.0, Robolectric 4.17 sürümleri buradan doğrulandı.
- Gradle 8.14.3 (nokta-app wrapper kopyası), AGP 8.13.2, Kotlin 2.3.21 + compose compiler plugin, Compose BOM 2026.03.01 (Compose 1.10.6), reorderable 3.1.0, activity-compose 1.12.4, lifecycle 2.10.0, core-ktx 1.17.0.
- Roborazzi 1.76.0 + Robolectric 4.17 (native graphics) + compose ui-test: ekran görüntüsü çalıştı (`screens/*.png`).
- Fontlar: Familjen Grotesk + Hanken Grotesk (google/fonts GitHub raw, SIL OFL); fontTools ile Türkçe glif kontrolü.
- keytool (RSA 2048, keystore `keystore/`, git'e girmiyor; şifre rapora yazılmadı), apksigner, aapt2.
- Kullanılmayan: adb/emülatör, Paparazzi, Android CLI, adb-mcp, VoltAgent/designmd (hazır DESIGN.md almadım, kendiminkini yazdım).

## DESIGN.md özeti (tam sayılar DESIGN.md içinde)
- Ad "nokta" kaldı (balon = nokta = onay işareti). Estetik "Sinyal lambası": soğuk-yeşilimsi nötr zemin (#F2F3EF / #111312) + TEK vurgu nokta #E5481C / #FF6A3A; renk yalnızca balon, alınmış işareti, imleç ve geri al eyleminde. Gradyan, kart, hero, kırmızı silme yok.
- Tipografi: Familjen Grotesk (wordmark 28sp/700) + Hanken Grotesk (satır 17/24, meta 13/16).
- Ölçüler: satır min 56dp, kenar 16dp, tutamaç 48x56, işaret 22dp, sil 48x48, ayraç 1dp; radius 0 (satır) / 8 (geri al şeridi) / daire.
- Hareket: dolum 200ms, çıkış 150ms, sürükleme 1.03x + 6dp gölge + haptic, geri al 5sn.
- Kontrast (hesaplandı): ink/bg 15.6:1; inkSoft 3.8:1 açık, 5.4:1 koyu; dot/bg 3.6:1 açık, 6.6:1 koyu.

## Özellikler (kodda var)
Sağ kenar balon (WindowManager TYPE_APPLICATION_OVERLAY, klasik View, kenara yapışma, uzun basınca gizle, uygulama öndeyken gizlenir, FGS specialUse + bildirimde "Kapat", açılışta BootReceiver); satıra dokun = üstü çizili, sağda x = sil, tutamaç + uzun basma = sürükle (reorderable); geri al şeridi (silme + toplu temizleme); "Alınanları temizle"; altta giriş alanı, açılışta odak; koyu tema sistemle; metin olarak dışa aktar (paylaş); JSON dosya depolama (atomik yazma, bozuk dosya yedeklenir); TR + EN metin.

## Testler
- `testDebugUnitTest`: **35 test, 0 hata** (ListStateTest 16, ControllerTest 8, JsonExportTest 7, ScreenshotTest 4). Mantık: ekleme/boşluk/limit, toggle, taşıma, silme+geri al konumu, sonradan ekleme sonrası geri al, çift geri al, toplu temizle+geri al, JSON gidiş-dönüş (Türkçe, tırnak), bozuk JSON, yinelenen id, dışa/içe aktarma, kalıcılık yeniden yükleme.
- Ekran görüntüsü: Roborazzi ilk denemede çalıştı (`recordRoborazziDebug`); `screens/light.png, dark.png, empty_light.png, undo_dark.png` gözle incelendi. Roborazzi karşılaştırma (verify) baseline'ı yok; yalnız kayıt.

## Sorunlar ve çözümler
1. Maven 429 (ilk build): arka planda yeniden deneme döngüsüyle aşıldı (ikinci denemede çözüldü).
2. Compose BOM 2026.09 (1.12.x) compileSdk 37 + AGP 9 ister; AGP 8.13.2/Gradle 8.14 ile uyumsuz -> BOM 2026.03.01 + eşleşen androidx sürümlerine inildi.
3. SDK'da yalnız android-34 vardı; **paylaşılan /root/android-sdk'ya `platforms;android-36` kuruldu** (sdkmanager) - diğer haritaları etkilemez ama ortak ortam değişti.
4. Koyu temada geri al metni (turuncu, açık şerit) düşük kontrastlıydı -> açık-tema dot rengi kullanıldı (şerit zemininde 3.3:1).
5. Kotlin 2.3'te `kotlinOptions` kaldırıldı -> `compilerOptions`.

## TEST EDİLMEDİ (ana oturum cihazda doğrulamalı; uçtan uca doğrulandığı İDDİA EDİLMİYOR)
- Hiçbir şey cihaz/emülatörde çalıştırılmadı: balon görünümü, kenara yapışma, dokunma->uygulama açma, uzun basma gizleme, overlay izin akışı, POST_NOTIFICATIONS isteği, FGS specialUse başlangıcı (Android 14+/16), BootReceiver.
- Gerçek sürükle-sırala hareketi, uzun basma sürükleme, haptic, klavye/imePadding, açılışta imleç odağı, geri al şeridi zamanlaması, paylaş menüsü, animasyonlar (Roborazzi sabit kare).
- R8 ile küçültülmüş release'in çalışma zamanı davranışı (derlendi, çalıştırılmadı; reorderable/Compose için ek keep kuralı yazılmadı).
- Balon ekran döndürmede yeniden konumlama yok (ilk dokunuşta sınırlara sıkıştırılır). Yedekten geri yükleme arayüzü yok (Export.fromText + denetleyici importLines var, UI bağlı değil).
- Lint çalıştırılmadı (abortOnError=false). Play Console beyanları (specialUse videosu, gizlilik politikası) yapılmadı. Kontrast dışında erişilebilirlik (TalkBack) denenmedi.
