# TekPanel — kuruluma hazır APK'lar

## Önerilen: `TekPanel-preview.apk` (2.5 MB)

Küçültülmüş (R8) ama doğrudan kurulabilir sürüm — debug anahtarıyla imzalı, Google Play'e
yüklenecek sürüm **değil**, test/kullanım içindir.

| | |
|---|---|
| GitHub | https://raw.githubusercontent.com/jannet5/Jn/claude/tekpanel-greenfield-spec-6ngokp/dist/TekPanel-preview.apk |
| jsDelivr (GitHub'dan bağımsız CDN) | https://cdn.jsdelivr.net/gh/jannet5/Jn@ac96d7a1772b1e64db8688541df9c7fe4c41471a/dist/TekPanel-preview.apk |
| Paket | `com.tekpanel.app.preview`, versionName 0.1.0, minSdk 26 (Android 8+), targetSdk 36 |
| Boyut | 2 545 292 bayt |
| SHA-256 | `a78f7e20a921ca6529c96ea30511f1d70609f24e5c0cd52067153611c207c421` |
| İmza | APK Signature Scheme v2, debug sertifikası |

**Doğrulanan (2026-10-03):** iki adres de HTTP 200 ve aynı SHA-256'yı veriyor; CI'da
(KVM'li Android 11 emülatörü, run 37161872623) kuruldu, açıldı, paylaşım ekranı açıldı,
bildirim erişimi verildi, dinleyici canlı listede görüldü, çökme yok
(`.github/scripts/preview-smoke.sh`).

**Doğrulanmayan (açık kabul maddesi):** gerçek bir telefonda indirme/kurulum/kullanım.
Önceki `.apk` ve `.zip` bağlantıları kullanıcının telefonunda başarısız oldu; telefon
tarafındaki hata noktası bilinmiyor.

## Eski: `TekPanel-debug.apk` (21 MB) ve `TekPanel-debug.apk.zip`

Aynı kodun küçültülmemiş debug derlemesi. SHA-256
`3aac3ec4162c1e72bfe0150d7e64fa8f1e4b00ed09dc3e18802e2e2807a49ebd`.

## Telefona kurma

1. Bağlantıyı telefonun tarayıcısında aç; indirme başlar.
2. İndirilen dosyaya dokun. Android ilk seferde "bu kaynaktan kuruluma izin ver" diye sorabilir.
3. Uygulamayı aç, bildirim erişimini ver, kanalları seç.

## Güncellemek

```
./gradlew :app:assemblePreview
cp app/build/outputs/apk/preview/app-preview.apk dist/TekPanel-preview.apk
```
jsDelivr bağlantısı commit'e sabitlidir; dosya değişince yeni commit hash'iyle güncellenmeli.
