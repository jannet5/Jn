# ÇÖZÜMLER — "Bir şeyi çözünce nasıl çözdüğünü yaz" (K-001)
> Her çözüm: belirti → kök neden → çözüm → doğrulama. Diğer yapay zekalar / geliştiriciler aynı sorunla uğraşmasın diye.

## Ç-001: APK linki telefonda indirilirken %100'de takılıyor
- Belirti: Kullanıcı sohbetteki GitHub `raw` linkine basıyor; indirme %100'e gelip bekliyor, dosya açılmıyor.
- Kök neden: `github.com/.../raw/...` 302 ile `raw.githubusercontent.com`'a yönlendirir ve dosyayı `application/octet-stream` olarak verir; uygulama içi tarayıcılar (sohbet uygulamasının WebView'ı) bu akışı tamamlayamıyor. GitHub Releases bu oturum türünde oluşturulamıyor (REST 403 "not permitted").
- Çözüm: jsDelivr CDN linki kullanıldı: `https://cdn.jsdelivr.net/gh/jannet5/Jn@ccr-2465bb14-nqlbmk/melodi-zil/dist/melodi-zil-1.0.0.apk` — doğru içerik türü (`application/vnd.android.package-archive`) ve `Content-Length` döner, 20 MB'a kadar GitHub dosyalarını sunar. Ek olarak kullanıcıya "linki Chrome'da aç" dendi ve APK sohbet eki olarak da gönderildi.
- Doğrulama: `curl -sI` başlıkları + `curl -o` ile indirilen dosyanın SHA-256'sı depodaki `dist/SHA256SUMS.txt` ile aynı (163ccd60…).
- Not: jsDelivr dal adıyla önbellekler; yeni APK yüklenince link 24 saate kadar eski dosyayı verebilir. Kesin tazelik için commit SHA'sı ile link ver: `@<commit>` ya da `https://purge.jsdelivr.net/gh/...` ile önbelleği temizle.

## Ç-002: Uygulama içi indirme ("Ses indiriliyor") %100'de takılıyor
- Belirti: YouTube'dan ses indirme çubuğu %100'e gelip ilerlemiyor.
- Kök neden: `YouTubeSource.download` Range parçalarıyla indirirken 416 (aralık dosya sonunu aştı) yanıtında döngüden çıkmıyordu (`return@use` yalnızca lambda'dan çıkar). itag'daki tahmini boyut gerçek dosyadan büyükse tetiklenir.
- Çözüm: `finished` bayrağı; 416, kısa parça, 200 ya da toplam boyuta ulaşınca döngü biter; Content-Range'deki gerçek toplam tahmine tercih edilir. (DERSLER D-012)
- Doğrulama: `DownloadLoopTest` (yerel soket HTTP sunucusu, boyut 2 kat abartılı): eski kod başarısız, yeni kod geçer.

## Ç-003: Android 13 altında açılışta/çözümlemede çökme (NoSuchMethodError URLDecoder.decode)
- Çözüm: `coreLibraryDesugaring("com.android.tools:desugar_jdk_libs_nio:2.1.5")`. Doğrulama: `dexdump -d classes*.dex | grep "URLDecoder;.decode:(Ljava/lang/String;Ljava/nio/charset/Charset;)"` → 0. (D-008)

## Ç-004: Android 8-9'da zil kaydedilmiyor
- Kök neden: MediaStore `_data` yolu ister (D-010); depolama izni süreç başladıktan sonra verilince GID gelmez (D-011).
- Çözüm: <Android 10'da dosya `Ringtones/MelodiZil/` altına yazılıp `_data` ile insert; izin "Dosya seç / Melodiye çevir" anında istenir.
- Doğrulama: emülatörde `settings get system ringtone` değişti, `content query --where is_ringtone=1` satır döndü.

## Ç-005: Bottom sheet'in Kaydet butonu ekran dışında
- Çözüm: `rememberModalBottomSheetState(skipPartiallyExpanded = true)` + `verticalScroll` + `navigationBarsPadding()`. (D-009)

## Ç-006: YouTube bazen boş akış listesi döndürüyor
- Çözüm: `resolve` 3 deneme (400/800/1200 ms). (D-006) · Bulut ortamı IP'si bot olarak engellenirse "Got HTML document, expected JSON" gelir; bu ortam sorunudur, telefonda denenir.

## Ç-007: Emülatör (KVM yok) ile gerçek Android doğrulaması
- API 30 imajı yazılım modunda sistem sunucusunu çökertti; API 26 x86 `-no-accel -gpu swiftshader_indirect` 3 dk'da açıldı. TLS-araya-giren proxy için CA'lar `araclar/emulator_proxy_ca.sh` ile sistem deposuna eklendi; arayüz `araclar/ui.py` (uiautomator) ile sürüldü. Metin yazarken `input text` parça parça (4 parça, 4 sn bekleme), aksi halde karakter düşüyor.

## Ç-008: Gradle Play Publisher 4.x "minimum Gradle 9.1.0" hatası
- Belirti: `id("com.github.triplet.play") version "4.1.1"` ile derleme yapılandırma aşamasında düşüyor.
- Çözüm: Gradle 8.x + AGP 8.7 projede GPP 3.12.1 kullan (aynı görevler: publishApps, publishBundle, publishListing). Anahtar yoksa `enabled.set(false)` ile normal derleme etkilenmez.

## Ç-009: Görsel varyasyon üretip seçtirme (görsel model yokken)
- Yöntem: Varyasyonları SVG olarak kodla çiz (aynı tuval, aynı renk, tek değişken: şekil), Playwright/Chromium ile tek sayfaya render et (büyük + 48px), ayrı bir ajana yalnızca görsel + ölçütlerle seçtir, önerilerini uygula. SVG path verisi doğrudan Android `vector` `pathData`'sına geçer. Araç: `docs/store/ikon_secimi/ikonlar.py`, `son.py`.

## Ç-010: APK telefonda %100'e geliyor ama inmiyor (Ç-001'in devamı, 2026-10-10)
- Belirti: Link doğru, ama telefonda indirme çubuğu %100'de kalıyor, dosya açılmıyor.
- Teşhis: Sunucu tarafı Android Chrome kimliğiyle kontrol edildi: HTTP 200, `application/vnd.android.package-archive`, doğru `Content-Length`, `accept-ranges: bytes`. Yani sorun sunucuda değil, telefonda.
- Kök neden (olası iki yol): (1) Chrome APK dosyaları için "Bu dosya türü cihazınıza zarar verebilir" onayı ister. Onay verilmezse indirme %100'de "bekliyor" kalır; uyarı ekranın altında kolayca gözden kaçar. (2) Link sohbet uygulamasının kendi iç tarayıcısında açılırsa, iç tarayıcı dosyayı sisteme teslim edemez ve %100'de takılır.
- Çözüm: Kullanıcıya üç yol verilir: (a) APK'yı sohbet eki olarak gönder (tarayıcıya hiç girmez); (b) linki uzun basıp kopyala, Chrome'da aç, alttaki uyarıda "Yine de indir"e bas, sonra Dosyalar > İndirilenler'den aç; (c) kurulumda "Bu kaynaktan izin ver" anahtarını Chrome (ya da Dosyalar) için aç.
- Kural: "İndirme takılıyor" denince önce NEREDE takıldığını ayır: uygulama içi adım mı (ekrandaki adım adı), tarayıcı indirmesi mi. Sunucuyu `curl -sSI -A "<Android Chrome UA>"` ile kontrol et; sorun yoksa telefon tarafı yönergesi ver, kodu değiştirme.
