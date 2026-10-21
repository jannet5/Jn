# Sende — telefona kurulacak APK

## Tek bağlantı (her zaman en yeni sürüm)

https://github.com/jannet5/Jn/releases/download/son-surum/Sende.apk

- Hesap gerekmez, giriş yok. Telefonun tarayıcısında aç, indirme başlar.
- Dosya GitHub'ın kendi sunucusundan "ek dosya" olarak iner (doğru dosya türü ve indirme başlığıyla).
  Eski raw/jsDelivr bağlantılarında görülen "yüzde 100'de takılma" sorunu bu yolla beklenmiyor;
  ama bunu henüz gerçek bir telefonda DENEMEDİM (aşağıya bak).
- Mobil veriyle de iner (2,8 MB). Aynı ağda olmak gerekmez.
- Her yeni değişiklikte CI bu bağlantıdaki dosyayı kendiliğinden yeniler ve eskisini siler.
  Sürüm sayfası: https://github.com/jannet5/Jn/releases/tag/son-surum
  Sayfanın açıklamasında yapı tarihi, saat (Türkiye saati) ve dosyanın SHA-256 değeri yazar.

## Kurulum (1 dakika)

1. Bağlantıyı aç; indirme bitince dosyaya dokun.
2. Android "bu kaynaktan kuruluma izin ver" derse izin ver (yalnızca bu kurulum için).
3. Sende'yi aç. İlk ekranda zorunlu bir adım yok. Üstte "Bildirim erişimini aç" kartı çıkar:
   düğmeye dokun, listede Sende'yi bul, anahtarı aç.
4. Anahtar gri ve oynamıyorsa: Android 13+ APK'yı Play dışından kurduğun için kilitler.
   Uygulama içindeki "Nasıl yapılır?" rehberi adımları gösterir: Uygulama bilgisi > sağ üst üç nokta >
   "Kısıtlanmış ayarlara izin ver" > sonra anahtarı aç.

## Güven: bu dosya neden zararsız

- Bu APK'nın izin listesi çok kısa: yalnızca "açılışta yeniden bağlan" (RECEIVE_BOOT_COMPLETED).
  İnternet izni YOK; yani mesajlarını bir yere gönderemez (CI her değişiklikte bunu denetler).
- Aynı sabit imza anahtarıyla imzalıdır; sonraki sürümler üstüne kurulur, verilerin silinmez.
- Paket adı: com.sende.app.preview. Google Play'e çıkacak sürüm ayrıdır (com.sende.app).
- Doğrulama: indirdiğin dosyanın SHA-256 değeri, sürüm sayfasındaki değerle aynı olmalı.

## Doğrulanan / doğrulanmayan (dürüst durum)

- Doğrulandı: CI gerçek emülatörde bu APK'yı kuruyor, açıyor, dinleyiciyi bağlıyor, çökme yok
  (`.github/scripts/preview-smoke.sh`). Bağlantı HTTP 302 ile GitHub'ın dosya sunucusuna gidiyor,
  `Content-Disposition: attachment` ve `application/vnd.android.package-archive` başlıklarıyla.
- Doğrulanmadı: gerçek bir telefonda indirme ve kullanım (bu ortamda telefon yok). Önceki
  denemelerde telefonda indirme takılmıştı; yeni bağlantı farklı bir yol, sonucu sen söyleyeceksin.

## Elle üretmek isteyene

```
./gradlew :app:assembleSideloadPreview
# çıktı: app/build/outputs/apk/sideload/preview/app-sideload-preview.apk
```
