# DERSLER: Melodi Zil (uygulamaya özel kararlar ve fabrika için dersler)

### D-004: Kapılar tek oturumda geçildi
- Kullanıcı ne dedi: "MVP değil baya Google Play atacak hale getir ve ver bitir yani tamamen"
- Kök neden: Fabrika her aşamada "onay" bekler; kullanıcı tek seferde bitmiş ürün istedi ve oturum boyunca yanıt veremezdi.
- Yeni kural (bu uygulama): Kapılar bekletilmedi; her kapının çıktısı yine dosyaya yazıldı (FIKIR, RAKIP, YORUM, URUN, DESIGN, EKRANLAR, KALITE_RAPORU). Tasarım yönü için 3 alternatif önizleme üretilmedi, tek yön seçildi ("Net & Oyuncu"). Kullanıcı "sorun:" ya da "düzelt:" yazarsa ilgili aşamaya dönülür.
- Fabrika önerisi: SKILL.md'ye "kullanıcı açıkça 'tamamını bitir' derse kapılar onay beklemeden geçilir, her kapının çıktısı yine yazılır, DURUM.md'de not düşülür" kuralı eklenmeli.

### D-005: Teknoloji Expo yerine Kotlin + Jetpack Compose
- Kullanıcı ne dedi: "Bir uygulama üret android mobil… şarkıyı melodiye çevirecek… zil sesi yapabilecek"
- Kök neden: İş yükünün kalbi cihaz üstü ses işleme (MediaCodec çözme, STFT/Viterbi melodi çıkarma, sentez, MediaStore + RingtoneManager ile zil ayarlama). Expo/React Native'de bunlar için 3-4 özel native modül yazmak gerekir, EAS hesabı olmadan Play paketi üretilemez. Depoda zaten Kotlin/Compose ile üretilmiş ve imzalı APK çıkarmış bir Android projesi (android-app) vardı; aynı araç zinciri (JDK 21, Gradle 8.14, SDK) hazırdı.
- Yeni kural (bu uygulama): Native Kotlin + Compose + Material 3. Tasarım kuralları (token, 16 kenar, çerçeveli kart, 20/24 ikon, 4 durum, tek birincil eylem) aynen uygulandı; `tasarim_denetimi.py` yerine Android Lint + manuel token denetimi.
- Fabrika önerisi: SKILL.md "Teknoloji" bölümüne istisna: "Cihaz üstü ağır medya/ses/sensör işleme gerekiyorsa ve kullanıcı onay veremiyorsa native Kotlin + Compose seçilebilir; neden DERSLER'e yazılır."

### D-006: YouTube bazen boş akış listesi döndürür
- Ne oldu: Aynı video için NewPipeExtractor ilk istekte 0 ses akışı, ikinci istekte 5 akış döndürdü (bot/istemci varyansı).
- Yeni kural: `YouTubeSource.resolve` en fazla 3 kez dener (400/800/1200 ms bekleme). Bu kural koddadır.

### D-007: Yorumlarda #1 şikayet reklam
- Yeni kural: v1.0'da reklam SDK'sı yok, paywall yok. Para modeli ileride yalnızca Ayarlar'daki "Destek ol" satın alması olabilir; akışa reklam girmez.

### D-008: Emülatör testi gerçek çökme yakaladı — Java 10+ API'si Android 13 altında yok
- Ne oldu: Release APK Android 8.0 emülatöründe "Melodiye çevir" basınca çöktü: `NoSuchMethodError: URLDecoder.decode(String, Charset)` (NewPipeExtractor içinden). JVM testleri (Java 21) bunu göremez.
- Kök neden: `desugar_jdk_libs` (standart) bu çağrıyı geri taşımıyor; NewPipe uygulaması `desugar_jdk_libs_nio` kullanıyor.
- Yeni kural: NewPipeExtractor ile `coreLibraryDesugaring("com.android.tools:desugar_jdk_libs_nio:2.1.5")` zorunlu. Doğrulama: `dexdump -d classes*.dex | grep "URLDecoder;.decode:(Ljava/lang/String;Ljava/nio/charset/Charset;)"` → 0 olmalı. Ayrıca işlem hattı `Error` türlerini de yakalar (çökme yerine hata ekranı).
- Fabrika dersi: "JVM'de geçen test, Android'de çalışır" demek değil; en az bir kez düşük API'li gerçek Android'de (emülatör) ana akış koşulmalı.

### D-009: Bottom sheet'in birincil butonu ekran dışında kaldı
- Ne oldu: Emülatörde (1080×1920, 420 dpi) "Zil sesi yap" sheet'i yarım açıldı; "Kaydet" butonu gezinme çubuğunun altında, görünmez. Kullanıcı sürüklemeden kaydedemez.
- Kök neden: `ModalBottomSheet` varsayılanı yarı açık durum; içerik kaydırılabilir değildi; navigationBars padding yoktu.
- Yeni kural: Her sheet `rememberModalBottomSheetState(skipPartiallyExpanded = true)`, içerik `verticalScroll` + `navigationBarsPadding()`. Sheet'in birincil butonu açılır açılmaz görünür olmalı (640dp yükseklikte test edilir).

### D-010: Android 8-9'da MediaStore kaydı NPE ile reddedildi
- Ne oldu: Emülatörde (Android 8.0) "Kaydet" sonrası dosya oluşmadı; MediaProvider `insertFile` NPE (`_data` yolu yok).
- Kök neden: Android 10 öncesi MediaStore, uygulamanın dosyayı kendisi yazıp `_data` (tam yol) vermesini bekler; `RELATIVE_PATH`/`IS_PENDING` yalnızca Android 10+.
- Yeni kural: `RingtoneStore.save` iki dal: Q+ → RELATIVE_PATH + openOutputStream; <Q → `getExternalStoragePublicDirectory(DIRECTORY_RINGTONES)/MelodiZil/x.wav` yazılır, `_data` ile insert. Kayıt sonrası `settings get system ringtone` ve `content query --where is_ringtone=1` ile doğrulanır.

### D-011: Android 8-9'da depolama izni verilince uygulama yeniden başlar
- Ne oldu: Emülatörde izin süreç başladıktan sonra verildi; süreç sdcard_rw grubunu almadığı için klasör oluşturulamadı (FileNotFoundException). Gerçek cihazda sistem, izin verilince uygulamayı öldürüp yeniden başlatır; kayıt anında istenirse çıkarılan melodi kaybolur.
- Yeni kural: Android 10 altında `WRITE_EXTERNAL_STORAGE` izni "Melodiye çevir"/"Dosya seç" anında, işlem başlamadan istenir (`AppNav.startWithPermission`). Kayıt anındaki kontrol yalnızca yedek.

### D-012: İndirme çubuğu %100'de takılıyor (kullanıcı telefon testi)
- Kullanıcı ne dedi: "Yüzde yüze geliyor takılıyor"
- Kök neden: `YouTubeSource.download` parça parça (Range) indirirken 416 (aralık dosya sonunu aştı) yanıtında yalnızca lambda'dan çıkıyordu; `while(true)` aynı isteği sonsuza kadar yineliyordu. YouTube'un itag'da bildirdiği boyut gerçek dosyadan büyük olunca bu yol tetikleniyordu.
- Yeni kural: İndirme döngüsü açık `finished` bayrağıyla biter (416, kısa parça, 200 ya da toplam boyuta ulaşma). Content-Range'deki gerçek toplam, tahmine her zaman tercih edilir. Ders: `return@use` döngüyü bitirmez; döngü çıkışları bayrakla ve testle doğrulanır.
