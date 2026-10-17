# Köken: indirme ve barındırma notları

Sorun: Android'de (Chrome) APK GitHub'dan inerken %100'de takılıyor. Ayrıca kurulumsuz web bağlantısı isteniyor.
Ölçümler 17 Ekim 2026'da sandbox'tan `curl` ile (GET, başlıklar) yapıldı. Dal: `ccr-db720415-rxd9br`, şu anki uç commit: `ea0bb681935db6065f86a3bd964b427886694d06`.

## Takılmanın bilinen nedenleri (araştırma)

1. **Chrome 150 Android hatası (Temmuz 2026).** İndirme %100 görünür, "bitti" der, ama dosya listeden/Dosyalar'dan kaybolur. Çoğu rapor APK. Google bunu P0 olarak inceliyor, nedeni onaylanmadı; şüphe Chrome'un yeni APK güvenlik denetiminde. Kullanıcı çözümü: İndirilenler'de **"Tekrar indir" > "Aç"** ([PiunikaWeb](https://piunikaweb.com/2026/07/09/chrome-150-disappearing-android-downloads-broken-video-controls/)). En olası neden budur ama kesin değil.
2. **Güvenli Tarama (Standart/Gelişmiş Koruma) APK denetimi.** Chrome her APK'da "Dosya zararlı olabilir" der; Gelişmiş Korumada şüpheli dosya derin taramaya gider ve baloncuk ilerleme göstermeden bekleyebilir ([Android Authority](https://androidauthority.com/google-chrome-downloads-security-3464740), [Guiding Tech](https://www.guidingtech.com/google-chrome-download-stuck-at-100-heres-what-to-do/)). "Takılma" için resmî hata kaydı bulamadım; bu bir çıkarım.
3. **Play Protect.** Kurulumda "Uygulamayı tara" penceresi sonrası "Yükleniyor"da takılabilir. Yukarı kaydırıp iptal edip yeniden denemek işe yarıyor ([Volt forumu](https://forum.volt.build/t/google-play-protect/2050)).
4. **"Bilinmeyen uygulamaları yükle" izni.** Android 8'den beri her kaynak uygulama için ayrı; Chrome'a izin verilmemiş olabilir. Aç-kapa yapmak da yardımcı olur.
5. **Genel Chrome sorunları:** kapalı İndirme Yöneticisi, dolu bellek, VPN, Wi-Fi/veri geçişi, bozuk önbellek ([Digital Citizen](https://www.digitalcitizen.life/fix-chrome-not-downloading-files-on-android/), [ChromeStory](https://chromestory.com/2026/05/chrome-android-download-stuck/)).
6. **Sunucu başlıkları.** Doğru tür `application/vnd.android.package-archive`; bazı Chrome/Brave sürümlerinde yalnız `Content-Disposition` yetmedi, `Content-Type` da gerekti ([Anvil forumu](https://anvil.works/forum/t/brave-chrome-browsers-on-android-ignore-file-extension-specified-in-anvil/8124)). `text/plain` verilen APK (statically) hiç uygun değil.
7. **Yönlendirme zinciri.** GitHub Release bağlantısı imzalı bir adrese gider; süresi yaklaşık 1 saat (ölçtüm: `se=` değeri +1 saat). Duraklatılıp saatler sonra sürdürülen indirme bu yüzden bozulabilir (çıkarım).

## Ölçüm tablosu

| URL (kısaltılmış) | Durum | Content-Type | Content-Disposition | Not |
|---|---|---|---|---|
| `github.com/jannet5/Jn/raw/B/koken/dist/probe.apk` | 302 | text/html (yönlendirme gövdesi) | yok | `raw.githubusercontent.com`'a tek atlama |
| `raw.githubusercontent.com/jannet5/Jn/B/koken/dist/probe.apk` | 200 | application/octet-stream | yok | 137 bayt, nosniff, önbellek 300 sn, her zaman güncel |
| `raw.githubusercontent.com/.../koken/dist/Koken.apk` | 200 | application/octet-stream | yok | 243042 bayt, sha256 `f88093a2...` depodakiyle aynı |
| `raw.githubusercontent.com/.../koken/web/probe.html` | 200 | **text/plain** | yok | Sayfa olarak AÇILMAZ, kaynak kodu gösterir (CSP sandbox) |
| `cdn.jsdelivr.net/gh/jannet5/Jn@B/koken/dist/probe.apk` | 200 | application/vnd.android.package-archive | yok | Doğru MIME |
| `cdn.jsdelivr.net/gh/jannet5/Jn@B/koken/dist/Koken.apk` | 200 | application/vnd.android.package-archive | yok | **ESKİ dosya verdi:** 240941 bayt, sha256 `713391ac...` (depoda 243042 bayt). Dal adresi 12 saate kadar önbellekte kalıyor |
| `cdn.jsdelivr.net/gh/jannet5/Jn@ea0bb68.../koken/dist/Koken.apk` | 200 | application/vnd.android.package-archive | yok | **243042 bayt, sha256 depoyla aynı**, immutable (commit'e sabit) |
| `cdn.jsdelivr.net/gh/jannet5/Jn@B/koken/web/probe.html` | 200 | **text/plain** | yok | Sayfa olarak AÇILMAZ |
| `raw.githack.com/jannet5/Jn/B/koken/web/probe.html` | 200 | text/html; charset=utf-8 | yok | Sayfa olarak AÇILIR. `curl -I` (HEAD) 403 verdi, GET 200: telefon GET kullanır |
| `rawcdn.githack.com/jannet5/Jn/ea0bb68.../koken/dist/koken.html` | 200 | text/html; charset=utf-8 | yok | Commit'e sabit kalıcı CDN, tek dosya web sürümü |
| `raw.githack.com/.../koken/dist/probe.apk` | 301 | text/html | yok | APK'yı githack vermiyor, `raw.githubusercontent.com`'a yönlendiriyor |
| githack: `app.js` / `style.css` / `sw.js` | 200 | application/javascript / text/css / application/javascript | yok | Çok dosyalı sürüm açılır |
| githack: `manifest.webmanifest` | 301 | text/html > raw'da text/plain | yok | Manifest yanlış türde: "Ana ekrana ekle" (PWA kurulumu) çalışmaz |
| `cdn.statically.io/gh/jannet5/Jn/B/...probe.html` | 301 > 301 (http) > 200 | **text/plain** | yok | HTML ve APK ikisi de text/plain. KULLANMAYIN |
| `htmlpreview.github.io/?https://github.com/.../blob/B/koken/web/probe.html` | 200 | text/html | yok | Yükleyici sayfa; dosyayı tarayıcıda JS ile `raw`'dan çekip çiziyor. Gerçek tarayıcıda denenmedi |
| `gitcdn.link/repo/jannet5/Jn/B/...probe.html` | 200 | text/html | yok | Gövde `/lander`'a yönlendiren JS: ölü/park alan adı. KULLANMAYIN |
| GitHub Releases (`.../releases/download/...`), örnek: halka açık termux-app | 302 > 200 | application/vnd.android.package-archive | `attachment; filename=....apk` | Bu depoda henüz sürüm YOK (API `[]`). Başlıklar başka bir depodan ölçüldü |

Sandbox proxy engelledi: `api.github.com/repos/jannet5/Jn/pages` (Pages durumu okunamadı; `has_pages=false` repo API'sinden doğrulandı). Başka host engellenmedi.
Not: `B` = `ccr-db720415-rxd9br`.

## Tavsiye edilen sıra

**APK (indirme)**
1. **GitHub Release (sabit adres):** `https://github.com/jannet5/Jn/releases/download/son-surum/Koken.apk`. `docs/release-workflow.yml` dosyasını `.github/workflows/koken-release.yml` olarak kopyalayıp push edince oluşur. En temiz başlıklar (`attachment` + doğru MIME), sha256 doğrulamalı.
2. **jsDelivr, commit'e sabit:** `https://cdn.jsdelivr.net/gh/jannet5/Jn@<COMMIT_SHA>/koken/dist/Koken.apk`. Şu an çalışır: `.../Jn@ea0bb681935db6065f86a3bd964b427886694d06/koken/dist/Koken.apk`. Her push'ta `<COMMIT_SHA>` değişir. Dal adını (`@ccr-...`) KULLANMAYIN, eski dosya verebilir.
3. Yedek: `https://raw.githubusercontent.com/jannet5/Jn/ccr-db720415-rxd9br/koken/dist/Koken.apk` (güncel, ama `octet-stream`).

**Web (kurulumsuz)**
1. **GitHub Pages:** `https://jannet5.github.io/Jn/` (PWA, "Ana ekrana ekle" çalışır). Şu an KAPALI (`has_pages=false`). Açmak: Ayarlar > Pages > Source = "GitHub Actions", sonra Ayarlar > Variables'a `KOKEN_PAGES=true` ekleyin. Ayarlar > Environments > `github-pages` > Deployment branches içinde `ccr-*` dallarına izin gerekebilir (varsayılan dal bu depoda `ccr-` değil).
2. **githack, tek dosya:** `https://rawcdn.githack.com/jannet5/Jn/<COMMIT_SHA>/koken/dist/koken.html` (kalıcı) veya `https://raw.githack.com/jannet5/Jn/ccr-db720415-rxd9br/koken/dist/koken.html` (dal, her push'ta güncellenir). Çalışır ama PWA kurulumu olmaz (manifest yanlış türde). Son çare: htmlpreview adresi.

Pages neden birinci: kalıcı HTTPS, doğru türler, servis çalışanı (çevrimdışı) ve manifest çalışır, aynı yerden APK de verilebilir. Release neden APK için birinci: tek güvenilir `Content-Disposition: attachment`. İkisi de Actions'ta `GITHUB_TOKEN` ile çalışır, ek anahtar gerekmez. Not: workflow'ta `workflow_dispatch` düğmesi yalnız varsayılan daldaki dosyada görünür, bu yüzden `push` tetikleyicisi esas alındı.

## Telefonda takılırsa ne yapılır (sırayla)

1. 1-2 dakika bekleyin; bildirim çubuğundan ve Chrome ⋮ > **İndirilenler**'den dosyaya bakın.
2. Dosya "bitti" ama görünmüyor ya da açılmıyorsa: İndirilenler'de **Tekrar indir**'e dokunun, bitince **Aç**'a basın (Chrome 150 hatası için bildirilen çözüm).
3. **Dosyalar / İndirilenler** uygulamasını açın; `.apk` orada duruyorsa üstüne dokunup kurun.
4. Aynı adresi **başka tarayıcıda** açın (Firefox, Samsung Internet, Edge). Olmuyorsa APK'yı bilgisayardan indirip Drive/USB ile telefona atın.
5. Chrome'u Play Store'dan **güncelleyin**.
6. Chrome ⋮ > Ayarlar > Gizlilik ve güvenlik > **Güvenli Tarama**: "Gelişmiş koruma" açıksa geçici olarak "Standart"a alın, indirin, sonra geri alın (sadece güvendiğiniz dosya için).
7. VPN'i kapatın, Wi-Fi/mobil veri arasında geçin, boş yer açın, Telefon Ayarları > Uygulamalar > Chrome > Depolama > **Önbelleği temizle**, "İndirme Yöneticisi" uygulamasının etkin olduğunu kontrol edin.
8. Kurulum ekranında: Chrome (veya Dosyalar) için **"Bu kaynaktan izin ver"**'i açın. Play Protect "Uygulamayı tara" derse **Tara**'ya basın; "Yükleniyor"da takılırsa yukarı kaydırıp iptal edin ve yeniden deneyin.
9. Hiçbiri olmazsa kurulum şart değil: yukarıdaki **web** adresini açın.

## Güvenlik notu

`koken/android/koken-release.jks` (imza anahtarı) halka açık depoda ve `.gitignore`'da yok. Gerçek dağıtımdan önce kaldırılmalı; anahtar sızmışsa başkası aynı imzayla sahte güncelleme üretebilir.
