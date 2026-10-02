# RAPOR_C — nokta, Harita C (minimal / sıfır bağımlılık)

## Sonuç
- `./gradlew testDebugUnitTest assembleRelease bundleRelease` BAŞARILI (NOKTA_C_KS_PASS ortam değişkeniyle imzalı; ~45 sn).
- Birim testi: **27 test, 0 hata** (NoktaListTest 19, NoktaJsonTest 8).
- **APK 34.404 bayt (33,6 KB)**, **AAB 54.577 bayt (53,3 KB)**. `dist/nokta-c.apk`, `dist/nokta-c.aab`, `dist/SHA256SUMS.txt`.
- `apksigner verify`: v2 DOĞRULANDI (v1/v3 kapalı; minSdk 26 için yeterli). AAB: `jarsigner -verify` "jar verified".
- Release: minify + resource shrink açık, R8 full mode.
- APK içeriği (8 dosya): classes.dex 50.204 B (sıkıştırılmış ~25 KB), AndroidManifest 3,6 KB, resources.arsc 1,9 KB, 3 küçük XML (adaptive icon + vektörler). PNG yok, font yok, native yok.

## Mimari (Harita C kuralları)
- Bağımlılık: Kotlin stdlib (AGP otomatik) dışında YOK; AndroidX yok (`android.useAndroidX=false`). Test: junit 4.13.2 + org.json 20240303 (yalnız testImplementation; çalışma anında platformun org.json'u).
- UI XML'siz, tamamen kodla. `NoktaListView`: tek özel View; liste, üstünü çizme, silme, sürükle-sırala, kaydırma/fling (OverScroller), otomatik kenar kaydırma, tek `ValueAnimator` (180 ms) ile yer değiştirme/üstü çizme/solma animasyonu, StaticLayout ile çok satırlı metin (6 satırda `…`), erişilebilirlik için elle yazılmış `AccessibilityNodeProvider` (satır = CheckBox, eylemler: tıkla, sil, yukarı/aşağı taşı).
- Sürükleme: sağdaki tutamaç hemen, satırın başka yeri uzun basmayla (haptic). Sürüklenen satır 1.02x + hafif gölge, diğerleri animasyonla kayar; bırakınca tek `move` (tek undo adımı).
- Depolama: `filesDir/nokta.json` ({"v":1,"items":[{id,t,d}]}), atomik yazma (tmp+rename), tek iş parçacıklı yazıcı. Bozuk dosya `nokta.json.bozuk` olarak yedeklenir, uygulama boş açılır.
- Mantık `NoktaList` (saf Kotlin): ekle (en üste), toggle, sil, toplu temizle, taşı, çok seviyeli undo (50 anlık görüntü), metin dışa aktarma.
- Balon: `BalloonService` (specialUse önplan servisi) + `TYPE_APPLICATION_OVERLAY` View; sürükle, bırakınca sol/sağ kenara yapışır (200 ms), dokununca MainActivity açılır (açılışta imleç hazır, klavye açık), üstünde açık madde sayısı. Kapatma: bildirimdeki "balonu kapat" ve uygulamadaki "balon" düğmesi. Y konumu saklanır.
- Özellikler: geri al şeridi (silme ve "tamamlananları sil" sonrası 6 sn), tamamlananları toplu temizle, hızlı ekleme, koyu tema (sistemle; colors.xml `-night`), metin paylaşma (hesapsız/reklamsız/çevrimdışı).

## Tasarım kararları ve gerekçe
- Ad: **nokta** (korundu). Logo: tek düz nokta (adaptive icon: kâğıt zemin + #B8371A daire; monochrome katman da var). Ad = nokta, logo = nokta; başka süs yok.
- Renk: sıcak kâğıt zemin #F3EFE6 / mürekkep #1C1B18 + TEK vurgu kiremit #B8371A (açık tema; zemin üstünde ~5:1). Koyu: #151412 / #ECE7DC / #FF7A4D. Renk yalnız anlam taşır (Things ilkesi, NOTLAR/tasarim_referanslari.md): vurgu = tamamlandı dairesi, + düğmesi, geri al, balon. Gradyan yok, mor/mavi yok, kart yok, gölge yalnız sürüklenen satırda.
- Tipografi: yalnız platform fontları (APK'ya 0 bayt): küçük harfli "nokta" yazısı `serif` (Noto Serif), gövde `sans-serif`. Tek aile Inter/Roboto yasağına uygun, karakter serif markadan geliyor.
- Satır 56dp, yatay 16-20dp, onay 22dp daire, ince ayraç (metin hizasından başlar), süreler 180/200 ms. Boş durum tek cümle, illüstrasyon/emoji yok.
- Dokunma alanları: onay+metin tüm satır, tutamaç 48dp, sil 48dp, alt düğmeler 48dp yükseklik, balon 56dp. contentDescription'lar Türkçe ve durumu söyler (balon açık/kapalı, "N açık madde").
- Kaldırdığım şey (slop turu): satır kartı, ikon süsü, boş durum görseli, ayrı ayarlar ekranı.

## Boyut/hız için yapılanlar
R8 full mode + `-allowaccessmodification -repackageclasses`; resource shrink; Kotlin null/param assertion'ları kapalı (`-Xno-*-assertions`); META-INF/kotlin metadata dışlandı; `dependenciesInfo` kapalı; vektör ikon (PNG yok); XML layout yok (inflate maliyeti yok); allowBackup kapalı; tek dosya JSON, Room/ORM yok; Store yalnız ilk açılışta küçük dosyayı okur.
- Başlangıç süresi ÖLÇÜLEMEDİ: bu ortamda KVM/emülatör yok. Ana oturum `adb shell am start -W` ile ölçsün. Beklenti yalnız yapıdan: tek Activity, 50 KB dex, yansıma yok.

## Kullanılan kaynaklar (gerçekten)
ORTAK_SPEC.md, HARITALAR.md, NOTLAR/ai_slop_onleme.md, tasarim_referanslari.md (Things ölçüleri, 3. taraf analiz), araclar.md, play_store_gereksinimleri.md (FGS specialUse, izinler), yorum_analizi.md (undo, toplu temizle, hızlı ekleme gerekçeleri). Araçlar: Gradle 8.14.3 (nokta-app wrapper kopyası), AGP 8.5.2, Kotlin 1.9.24, JDK 21, build-tools 34.0.0 (apksigner, aapt2), keytool. sistemler.md yoktu. Web araştırması yapılmadı; Material ölçüleri kendi bilgim.

## Sorunlar ve çözümler
- Test derleme hatası: `encode(emptyList())` tür çıkarımı; `emptyList<Item>()` ile düzeldi.
- Maven 429 yaşanmadı.
- Animasyon yarışı (iptal edilen animatörün bitiş geri çağrısı hayalet satırları siliyordu) `stopAnim()` bayrağıyla önlendi (kod incelemesiyle; cihazda denenmedi).

## Bilinçli sapmalar / eksikler
- targetSdk/compileSdk **34** (SDK'da yalnız android-34 var; spec notu 36 öneriyor). Play için platform 36 kurulup yükseltilmeli.
- Madde metni düzenleme yok (sil + yeniden ekle). İçe aktarma yok, yalnız dışa aktarma.
- Satır en çok 6 satır gösterilir (taşan `…`; metin tam saklanır ve paylaşımda tamdır).
- Önyüklemede balon otomatik başlamaz (uygulama açılınca başlar).
- Bildirim izni reddedilirse servis çalışır, bildirim görünmez (Play yönergesi notu); izin sonucu ele alınmıyor.

## TEST EDİLMEYENLER (uçtan uca doğrulandı İDDİA EDİLMİYOR)
Yalnızca JVM birim testi (liste mantığı, undo, JSON) ve derleme/imza doğrulaması yapıldı. Hiç cihaz/emülatör çalıştırması yok. Cihazda doğrulanacaklar: Canvas çizimi ve ölçüler, dokunma/tutamaç/uzun basma sürükleme, otomatik kaydırma, üstünü çizme animasyonu, silme + geri al şeridi, klavyenin açılışta çıkması, koyu tema, çok satır/uzun metin, TalkBack eylemleri (provider kodu hiç çalıştırılmadı), balon izin akışı (ayar ekranına yönlendirme ve dönüşte başlama), overlay çizimi/yapışma/dokununca açma, önplan servisi bildirimi (Android 14 specialUse), bildirim izni, dosyaya kalıcılık, paylaşım, başlangıç süresi. Lint ayrıca çalıştırılmadı (yalnız lintVital geçti).
Yeniden derleme için: `export NOKTA_C_KS_PASS=$(cat keystore/.pass)` (keystore/ git dışı; şifre rapora yazılmadı).
