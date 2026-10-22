# Sende test ve kalite sistemi

Amaç: "insan gibi kullanılıp denenmiş" bir uygulama; ama her seferinde elle değil, tek komutla ve
başka bir sohbette/yapay zekada aynen tekrarlanabilir. Her katman bir soruyu cevaplar.

## Katmanlar

1. **Saf mantık (JVM, saniyeler).** Filtre, normalizer, parmak izi, satır takipçisi, kanal sıralaması,
   ödeme/deneme kuralı. Soru: "karar doğru mu?"
2. **Gerçek Android nesneleriyle (Robolectric).** Gerçek `Notification`/`StatusBarNotification`,
   gerçek Room, gerçek DataStore: bildirimden kayda bütün zincir, göç (v1→v2), ViewModel akışları.
   Soru: "bildirim gelince mesaj tam bir kez, doğru sohbette kaydoluyor mu?"
3. **Ekran görüntüsü (Roborazzi).** Aynı Compose ekranı JVM'de PNG'ye çizilir (`app/build/shots/`):
   açık/koyu tema, Fransızca, Arapça (sağdan sola), tüm durumlar. Gözle bakılır. Soru: "nasıl görünüyor?"
4. **Ergonomi testi (`ErgonomicsTest`).** Ana ekrandaki her tıklanabilir öğenin dokunma alanı en az
   48×48 dp olmalı; normal ve %200 yazı boyutunda, kapalı/açık kart, menü ve izin kartında.
   Küçülen bir düğme derlemeyi kırar. Soru: "başparmakla basılıyor mu?"
5. **Kaydırma kararlılığı (`ScrollStabilityTest`).** Listenin derinindeki karta dokununca kaydırma
   konumu değişmemeli ("en üste zıplama" hatası). Soru: "dokununca ekran kaçıyor mu?"
6. **Gerçek sistem (emülatör matrisi: Android 8, 11, 14, 16).** Dinleyici gerçekten bağlanıyor mu;
   başka uygulamadan (Android 11+: `cmd notification post` ile kabuk) gelen bildirim sistemden
   dinleyiciye, oradan gerçek Room'a düşüyor mu; aynı bildirim tekrar gelince çoğalmıyor mu, aynı
   anahtarla gelen yeni mesaj kaybolmuyor mu. Soru: "gerçek Android'de çalışıyor mu?"
7. **Küçültülmüş APK duman testi (`preview-smoke.sh`).** R8'li APK emülatöre kurulur, açılır,
   paylaşım girişiyle Room'a yazılır, çökme ve dinleyici bağlantısı denetlenir. Soru: "R8 bir şeyi
   kırdı mı?"
8. **Güvenlik/izin kapıları.** Sideload manifestinde INTERNET yok; elden verilen APK'nın izin listesi
   yalnız "açılışta yeniden bağlan"; "kullanıcının kendi bildirimini silme/erteleme" kodu yok;
   ClamAV taraması (CI'da, yayından önce). Soru: "güvenilir mi?"
9. **Lint + Play R8 derlemesi.** Çeviri, erişilebilirlik, yedek kuralı, eksik R8 kuralı.

## Komutlar

- Hepsi, yerelde: `bash tools/kapilar.sh` (emülatör/telefon bağlıysa `--emu` ile gerçek sistem
  testleri de).
- Yalnız ergonomi: `./gradlew :app:testSideloadDebugUnitTest --tests "com.sende.app.ui.ErgonomicsTest"`.
- Ekran görüntüleri: `./gradlew :app:testSideloadDebugUnitTest -Proborazzi.test.record=true`.
- Her push'ta CI aynısını (ve emülatör matrisini) kendiliğinden çalıştırır; hepsi geçmeden herkese
  açık `son-surum` APK'sı yenilenmez.

## Gerçek kullanıcıyı taklit

- Başka uygulama yerine kabuk bildirimi: `adb shell cmd notification post -S bigtext -t Baslik etiket metin`
  (MessagingStyle için `-S messaging --conversation Ad --message Kisi:metin`). Önemli: Android'in
  kabuk komutu tırnakları çözmez; her argüman tek kelime olmalı.
- Yük/ayakta kalma: yüzlerce bildirim art arda atılıp sayı ve süre izlenebilir (`diagnostics`
  ekranındaki sayaçlar mesaj içeriği tutmaz).
- Gerçek WhatsApp/Instagram, gerçek telefon, gerçek müşteri: yalnız kurucu yapabilir; test hattı
  bunun yerine geçmez, yalnız önünü açar.

## Hata ayıklama kuralı (öğrendiklerimiz)

- Bir test başarısız olunca mesajı kendini açıklamalı: uygulamanın o an ne gördüğü (karar, sayaçlar,
  son hata) tek satırda yazılsın. Gradle konsolu mesajın yalnız ilk satırını gösterir.
- "Uyku" ile bekleme yok: sistem bir şeyi tanıdığını söyleyene (sayaç artana) kadar sorgula.
- Her uçtan uca test boş bir gelen kutusuyla başlar; önceki koşunun verisi sonucu yeşile çevirmemeli.

## Henüz yapılamayanlar (dürüst)

- Gerçek telefonda 3 farklı üretici (Samsung/Xiaomi/Oppo) ve 7 günlük canlı bildirim denemesi.
- TalkBack ile elle gezinme; Macrobenchmark/Baseline Profile ile kasma ölçümü.
- VirusTotal raporu (API anahtarı ister).
