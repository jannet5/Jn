# İndirme: %100'de takılma ve barındırma ölçümleri

## Neden %100'de takılıyor olabilir
- Chrome'un yeni "zararlı APK kontrolü" ve Güvenli Tarama, bilinmeyen APK'yı indirme bittikten sonra tarar ve bekletir. Temmuz 2026'da Chrome 150'de APK'nın indirilenlerden kaybolması da bildirildi ([PiunikaWeb](https://piunikaweb.com/2026/07/09/chrome-150-disappearing-android-downloads-broken-video-controls/), [PCWelt](https://www.pcwelt.de/?p=2615507)).
- Yönlendirme zinciri: `github.com/.../raw/...` önce `raw.githubusercontent.com`'a yönlendiriyor ve dosyayı `application/octet-stream` veriyor.

## Ölçüm (bu ortamdan, GET ile)
| Adres | Sonuç | Content-Type |
|---|---|---|
| github.com/jannet5/Jn/raw/<dal>/koken/dist/Koken.apk | 302 → raw.githubusercontent | application/octet-stream |
| cdn.jsdelivr.net/gh/jannet5/Jn@<sha>/koken/dist/Koken.apk | 200, doğrudan | **application/vnd.android.package-archive** |
| cdn.jsdelivr.net/.../*.html | 200 | text/plain (web sayfası olarak açılmaz) |
| cdn.statically.io/... | 200 | text/plain |
| rawcdn.githack.com/jannet5/Jn/<sha>/koken/dist/koken.html | 200 | **text/html** (ilk açılışta "Open the page" ara sayfası) |
| rawcdn.githack.com/.../Koken.apk | 301 → raw.githubusercontent | octet-stream |
| htmlpreview.github.io | sayfa açılıyor ama betikler bozuluyor | kullanma |

## Seçilen yol
- **APK:** jsDelivr (doğru APK türü, tek adım). Yedek: ZIP olarak kaydet (karttaki düğme) → Dosyalar'da aç → `Koken.apk`.
- **Web:** githack (commit'e sabitlenmiş adres) → "Open the page".
- Takılırsa: Chrome'da Güvenli Tarama'yı "Standart"a al ya da Firefox/Samsung Internet ile indir.
