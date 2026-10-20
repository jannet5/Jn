# Fabrika: başka bir yapay zekâ bu uygulamayı sıfırdan nasıl yapar

Bu dosya tek başına verilebilir. Aşağıdaki istemi (prompt) kopyala, başka bir yapay zekâya ver;
yanında `docs/fabrika/01-ADIMLAR.md` ve `docs/MIMARI.md` olsun.

## Kopyalanacak istem

```
Sen kıdemli bir Android mühendisisin. Aşağıdaki ürünü SIFIRDAN yapacaksın.

ÜRÜN
Sende: kullanıcının seçtiği uygulamaların (WhatsApp, Instagram, TikTok, SMS ve telefondaki
HERHANGİ bir uygulama) bildirimlerinden gelen müşteri mesajlarını yakalayıp TEK, sade bir ekranda
gösteren Android uygulaması. Hedef: Türkiye'de küçük işletme sahibi. Dünya çapında satılacak.
Veri telefondan asla çıkmaz.

DEĞİŞMEZ KURALLAR
1. Sideload sürümünde INTERNET izni YOK (derlenmiş manifestte doğrula). Play sürümünde yalnızca
   Play Billing'in getirdiği ağ izinleri olabilir; kendi kodun ağa hiçbir şey göndermez.
2. Tek ekran. Menü bir ok işaretinin arkasında. Geri tuşu uygulamadan ÇIKARMAZ: önce açık olanı
   kapatır, sonra arka plana alır.
3. Bir mesaja dokunmak "okundu" yapmaz. Yalnızca küçük "Okundu" yazı düğmesi yapar.
4. Kanal = telefondaki herhangi bir uygulama; ikon ve ad paket yöneticisinden gelir. Paket adı
   (com.whatsapp) hiçbir yerde görünmez. QUERY_ALL_PACKAGES kullanma; launcher intent <queries> kullan.
5. Tekrar koruması bildirim anahtarına DEĞİL mesaj içeriğine + mesajın zaman damgasına dayanır
   (sohbet uygulamaları her sohbet için tek bildirimi güncelleyip aynı anahtarı kullanır).
6. Metin kesilmez: bigText/text'in uzun olanı alınır, MessagingStyle'ın tüm satırları alınır.
7. Her özellik için test yaz; ekranı görmeden "bitti" deme (Roborazzi ile JVM'de ekran görüntüsü al).
8. Her adımın sonunda bu repodaki `01-ADIMLAR.md` kabul ölçütlerini çalıştır ve çıktısını göster.

TEKNOLOJİ
Kotlin 2.0.21, Jetpack Compose + Material3, Room 2.6.1, DataStore, Koin, AGP 8.11,
minSdk 26, targetSdk 36, Roborazzi 1.32 (JVM ekran görüntüsü), Robolectric 4.13.

NASIL ÇALIŞ
`docs/fabrika/01-ADIMLAR.md` içindeki M0'dan M12'ye sırayla git. Her adım için: planı yaz,
yap, testleri çalıştır, kanıtı göster, commit at. Bir adım 2 denemede geçmiyorsa DUR, nedenini
yaz ve alternatif yol öner; aynı başarısız şeyi saatlerce tekrarlama.
```

## Başarı ölçüsü

- Başka bir yapay zekâ bu istemle, `01-ADIMLAR.md` kabul ölçütlerini geçen bir uygulama üretebilir.
- Ölçüt: `./gradlew :app:testSideloadDebugUnitTest` yeşil, `assembleSideloadPreview` imzalı APK üretir,
  CI emülatör matrisi yeşil.
