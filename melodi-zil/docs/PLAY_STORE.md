# Google Play mağaza metni ve yükleme notları — Melodi Zil

## Uygulama adı (30)
Melodi Zil: Şarkıdan Zil Sesi

## Kısa açıklama (80)
Sevdiğin şarkının melodisini piyano, müzik kutusu, 8-bit… zil sesine çevir.

## Tam açıklama
Melodi Zil, bir şarkıyı **kesmez, melodiye çevirir.** Sözler ve vokal kaybolur; geriye eski Nokia zilleri gibi sade, tanınabilir bir ezgi kalır.

**Nasıl çalışır?**
1. Bir ses dosyası seç ya da 10 dakikanın altındaki bir YouTube bağlantısını yapıştır.
2. Melodi telefonunda otomatik çıkarılır; hiçbir şey sunucuya gönderilmez.
3. 8 formdan birini seç: Piyano, Müzik Kutusu, 8-Bit, Marimba, Flüt, Çan, Gitar, Synth.
4. "Otomatik" en yoğun bölümü (genelde nakarat) bulur; istersen kaydırıcıyla değiştir.
5. Zil sesi, bildirim ya da alarm olarak tek dokunuşla kaydet ve ayarla.

**Neden Melodi Zil?**
• Reklam yok, hesap yok, abonelik yok.
• Tüm işlem telefonunda: gizlilik tam, internet yalnızca indirme için.
• Zillerim sekmesinde kayıtlı zillerini yeniden ayarla, paylaş ya da sil.
• Açık ve koyu tema.

Not: Lütfen yalnızca kullanım hakkına sahip olduğun içerikleri kullan. Melodi Zil, YouTube ile bağlantılı değildir.

## Kategori
Müzik ve Ses · İçerik derecelendirmesi: Herkes

## Gizlilik politikası URL'si
`docs/gizlilik.html` dosyasını GitHub Pages'te yayınla (Settings → Pages → `melodi-zil/docs`) ve bu adresi Play Console'a gir. Uygulama içinde de aynı metin var (Ayarlar → Gizlilik politikası).

## Veri güvenliği formu (Play Console)
- Veri toplanıyor mu: **Hayır** (hiçbir veri toplanmaz/paylaşılmaz).
- Şifreleme: Yok (veri aktarımı yok). Silme isteği: Uygulanamaz.

## İzin gerekçeleri
- `WRITE_SETTINGS`: Varsayılan zil/bildirim/alarm sesini kullanıcı isteğiyle ayarlamak için. Kullanıcı sistem ekranından açar.
- `INTERNET`: Ses akışını indirmek için.
- `WRITE_EXTERNAL_STORAGE` (maxSdk 28): Android 8-9'da MediaStore'a yazmak için.

## ⚠️ Politika riski (dürüst not)
Google Play, YouTube içeriğini indiren uygulamaları YouTube Hizmet Şartları'nı ihlal ettiği gerekçesiyle reddedebilir ya da kaldırabilir. Riski azaltmak için:
1. Mağaza metninde ve ekran görüntülerinde **yerel dosya akışını** öne çıkar; YouTube'u ikincil olarak an.
2. Reddedilirse: `HomeScreen` içindeki bağlantı alanını kaldırıp yalnızca dosya seçimiyle yayınla (kod bunu destekler; `YouTubeSource` kullanılmaz).
3. Alternatif dağıtım: APK'yı doğrudan (GitHub Releases / F-Droid) yayınlamak YouTube özelliğini korur.

## Yükleme adımları
1. `keystore/upload.jks` bu oturumda üretilen **yükleme anahtarı**dır. Play Console'da "Play App Signing"i etkinleştir; Google uygulama imzasını kendisi yönetir, bu anahtar yalnızca yükleme için kullanılır. Anahtarı ve `keystore.properties` şifrelerini güvenli bir yerde sakla (kaybolursa Play Console'dan yükleme anahtarı sıfırlama istenir).
2. Play Console → Uygulama oluştur → Üretim → Yeni sürüm → `app/build/outputs/bundle/release/app-release.aab` yükle.
3. Mağaza kaydı: yukarıdaki metinler, 512×512 ikon (`docs/store/ikon_512.png`), 1024×500 öne çıkan görsel, en az 2 telefon ekran görüntüsü (`docs/store/ekran_*.png`).
4. Veri güvenliği formunu "veri toplanmıyor" olarak doldur, gizlilik URL'sini gir, içerik derecelendirme anketini doldur.
5. Sonraki sürümlerde `versionCode` artır.
