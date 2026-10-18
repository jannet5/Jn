# Köken: durum (2026-10-18)

## Bitenler (kanıtlı)
- Motor `web/etym.js`: canlı Vikisözlük (en/tr), TDK (`www.sozluk.gov.tr`), Etimoloji Türkçe (`api.etimolojiturkce.com`) çalışıyor; APK'da ek olarak Nişanyan (sayfa HTML'i, `<meta description>`) ve Etymonline denenir. Gerçek tarayıcıda (Chromium, 390x844) `pijama` ve `kitap` ile sonuç ekranı doğrulandı: her kaynak "Köken N · Kaynak adı" başlığıyla ayrı blokta görünüyor.
- APK `dist/Koken.apk` (~244 KB, v2+v3 imzalı, minSdk 24, yalnız INTERNET). Emülatörde kuruldu ve açıldı, çökmedi (önceki tur).
- Yayın hattı `.github/workflows/koken-yayin.yml`: her push'ta APK'yı GitHub Release'e (`koken-son-surum`) yükler, web'i `gh-pages` dalına koyar. Release adresi doğrulandı: GET 200, `application/vnd.android.package-archive`, `Content-Disposition: attachment`, sha256 dosyayla aynı.
- Kart (Artifact) yayında: Claude'a soran arama + APK/web/ZIP düğmeleri.

## İndirme adresleri
- APK (ana): https://github.com/jannet5/Jn/releases/download/koken-son-surum/Koken.apk
- APK (yedek, commit'e sabit): jsDelivr `@<sha>/koken/dist/Koken.apk`
- Web: `https://rawcdn.githack.com/jannet5/Jn/<sha>/koken/dist/koken.html` (ilk açılışta "Open the page")

## Açık kalanlar
1. GitHub Pages açık değil: Actions'ın `GITHUB_TOKEN`'ı Pages'i açamıyor (403). Tek seferlik elle: repo Settings → Pages → Branch: `gh-pages` / `/ (root)` → Save. Sonra web adresi `https://jannet5.github.io/Jn/` olur (githack ara sayfası kalkar, "ana ekrana ekle" çalışır).
2. Vikisözlük `action=parse` hız sınırına (429) takılırsa REST HTML yedeğine geçiş yok. Test ortamının ortak IP'si sık takılıyor; telefonda beklenmiyor.
3. Nişanyan ve Etymonline bu ortamda Node ile 403 veriyor (Cloudflare); APK köprüsü Chrome User-Agent'ı ile 200 aldı (JVM testi). Telefonda canlı doğrulanmadı.
4. Emülatörde internetli uçtan uca test: ayrı işçide yürüyor, sonucu `tests/android/INTERNETLI-TEST.md` içine yazılacak.
5. Release güncellenirken (sil → yeniden oluştur) birkaç saniye link 404 verir.
