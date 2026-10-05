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
