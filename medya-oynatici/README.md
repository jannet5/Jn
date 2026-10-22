# Oynatıcı — sade MP3 & video oynatıcı (Android)

Açınca karşına iki büyük buton çıkar: **Müzik** ve **Video**. Gerisi o kadar basit.

## Kurulum (telefona)

1. `dist/` klasöründen telefonuna uygun APK'yı indir:
   - **`Oynatici-arm64-v8a.apk`** → neredeyse bütün telefonlar (2017 sonrası). Emin değilsen bunu al.
   - `Oynatici-armeabi-v7a.apk` → çok eski / ucuz telefonlar.
2. Dosyaya dokun, "bilinmeyen kaynaklardan yüklemeye izin ver" de, kur.
3. İlk açılışta bildirim izni ister (indirme ilerlemesi için) — izin ver.

Sunucu yok, hesap şart değil. İnternet sadece indirirken gerekir.

## Neler yapıyor

| Özellik | Nasıl |
|---|---|
| Müzik / Video bölümleri | Ana ekrandaki iki büyük buton |
| Dosya ekleme | **Ekle → Dosya ekle**: telefondan istediğin kadar ses/video seç |
| Klasör ekleme | **Ekle → Klasör ekle**: klasörü seç, içindeki 20 şarkı tek seferde, **klasör olarak** gelir (alt klasörler de korunur) |
| Kendi klasörün | **Ekle → Yeni klasör** (örn. "Gitar videoları", "Filmler") |
| Düzenleme | Satırdaki **⋮** (veya uzun bas): Kırp · Yeniden adlandır · Taşı · Sil |
| Çalma | Dosyaya dokun → klasördeki diğerleri sıraya girer. Müzik ekran kapalıyken de çalar, bildirimde kontroller var |
| İnternetten indirme | **Ekle → İnternetten indir** (ya da YouTube/TikTok/Instagram/X/Pinterest uygulamasında **Paylaş → Oynatıcı**). Link yapıştır; panoda link varsa kendiliğinden gelir |
| Desteklenen siteler | **YouTube, TikTok, Instagram, X (Twitter), Pinterest** ve yt-dlp'nin tanıdığı diğer siteler |
| Müzik bölümü | Kalite sorulmaz: **en yüksek kalitede MP3** |
| Video bölümü | **Kalite sorulur** (144p … 1080p, En yüksek), her telefonda açılan MP4 (H.264) |
| Oynatma listesi / Pinterest panosu | **Listeyi getir ve seç**: içerikler işaretli liste olarak gelir, istediklerini seçersin (Hepsi / Hiçbiri) |
| Kanal / profil linki | **"Son kaç içerik?"** sorulur (5/10/20/50 ya da yaz). YouTube'da **Videolar / Shorts**, Instagram'da **Reels / Gönderiler** seçilir |
| Klasör | Hepsi tek klasöre iner: **"Kanal adı, 20 içerik, Instagram"**. Klasör ikonu sitenin logosu olur |
| Linkler | Klasörün içine **Linkler.txt** konur (her içeriğin https linki alt alta). Uygulamada klasörün en üstündeki **Linkler** satırından kopyalayıp açabilirsin |
| Hesaplar | Ana ekrandaki kişi ikonu: Instagram, X, TikTok, Pinterest'e uygulama içinden giriş. **Şart değil**: herkese açık profiller ve gönderiler girişsiz iner; giriş sadece gizli hesaplar / yaş sınırlı içerik için |
| Kırpma | **⋮ → Kırp**: iki tutamaçla aralık seç **veya** süreleri yaz (`1.28` – `2.16`, `1:28` de olur). **Orijinal silinmez**, kesit yeni dosya olarak kaydedilir: `Şarkı (1.28-2.16).mp3` |

## Nasıl çalışıyor (teknik)

- **Kotlin + Jetpack Compose**, tek Activity, Material 3 koyu tema.
- **Oynatma:** AndroidX Media3 (ExoPlayer + MediaSessionService → arka planda çalma, bildirim/kilit ekranı kontrolleri).
- **İndirme:** [youtubedl-android](https://github.com/yausername/youtubedl-android) — yt-dlp ve ffmpeg'i **telefonun içinde** çalıştırır (Seal gibi uygulamaların kullandığı kütüphane). yt-dlp günde bir kendiliğinden güncellenir; indirme hata verirse önce güncelleyip bir kez daha dener.
  - Müzik: `-f bestaudio -x --audio-format mp3 --audio-quality 0`; Video: `-S res:<kalite>,vcodec:h264` + MP4
  - Liste/profil: önce `--flat-playlist -J -I 1:N` ile içerikler listelenir, seçilenler tek klasöre `001 - başlık.mp4` olarak iner
- **Site başına yöntem** (araştırıp emülatörde denendi):
  - YouTube kanal: `@kanal/videos` ya da `/shorts` (yt-dlp; çıplak kanal linki 3 sekmeyi karıştırdığı için sekme hep eklenir)
  - TikTok: yt-dlp, **masaüstü Firefox kimliğiyle** (varsayılan kimlik TikTok'un engeline takılıyor); fotoğraf gönderileri ayıklanır
  - Instagram: yt-dlp + uygulamayla gelen iki küçük **yt-dlp eklentisi** (`ytdlp-eklentileri-kaynak/`): girişsiz profil Reels/Gönderiler listesi ve curl_cffi olmadan reels indirme
  - X: tek gönderi yt-dlp ile girişsiz. Profilin son N videosu **girişsiz**, sırayla şu yollarla bulunur:
    1. **FxTwitter API** (FxEmbed'in açık, ücretsiz API'si: `api.fxtwitter.com/2/profile/id:<kimlik>/media`): profilin "Medya" sekmesini en yeniden eskiye verir. Gerekli, çünkü X girişsiz ziyaretçiye çoğu kişisel hesabın (ör. MKBHD, MrBeast) en yeni gönderilerini değil, karışık sıralı eski popüler gönderilerini gösteriyor. Not: üçüncü taraf bir servis; ona sadece profil adı gider.
    2. Olmazsa X'in **misafir erişimi** (gallery-dl `x.com/<kullanıcı>/tweets`, her seferinde yeni misafir anahtarı). Kurumsal hesaplarda (NASA, SpaceX…) en yeniden eskiye gelir.
    3. Misafir erişim karışık sırada (popüler) geldiyse X'in **yerleştirme (embed) akışı** denenir; o da karışıksa bulunanlar indirilir ve kartta "en yeniler için X'e giriş yap" notu çıkar.
    4. X'e giriş yapıldıysa doğrudan gallery-dl ile "Medya" sekmesi kullanılır.
  - Pinterest: pano ve profil listesi **gallery-dl** ile (girişsiz), video pinleri tek tek iner
- **gallery-dl:** saf Python; telefondaki Python 3.12 ile çalışır (`assets/gdl`: gallery-dl + requests/urllib3/idna/certifi/charset_normalizer). Haftada bir PyPI'den yenisi kendiliğinden indirilir.
- **Giriş:** sitenin kendi giriş sayfası WebView'de açılır; şifre uygulamaya kaydedilmez, oturum çerezi telefonda kalır ve indiricilere Netscape çerez dosyası olarak verilir.
- **Kırpma:** video → Media3 Transformer (telefonun donanım kodlayıcısı, saniyesi saniyesine; olmazsa ffmpeg ile kopyalayarak). Ses → ffmpeg `-c copy` (yeniden kodlama yok, kalite kaybı yok).
- **Dosyalar:** `Android/data/com.jn.oynatici/files/Muzik` ve `.../Video` altında gerçek klasörler. Not: uygulama silinirse bu dosyalar da silinir.

## Derleme

```bash
# Android SDK (platforms;android-36, build-tools;36.0.0) gerekli
echo "sdk.dir=/android-sdk/yolu" > local.properties
./gradlew assembleRelease
# çıktı: app/build/outputs/apk/release/app-<abi>-release.apk
```

## Ne test edildi (dürüstçe)

Gerçek release APK, Android 8.0 (API 26) emülatöründe kurulup uygulamanın kendi ekranlarından denendi:

| Test | Sonuç |
|---|---|
| Açılış ekranı, Müzik/Video bölümleri | ✅ |
| **Klasör ekle** (içinde alt klasör + 4 ses + 1 video olan klasör) | ✅ Klasör olarak geldi, alt klasör korundu, video müzik bölümüne alınmadı |
| **Dosya ekle** (video) | ✅ |
| Müzik çalma (arka plan servisi, medya oturumu) | ✅ |
| Video oynatma | ✅ |
| **Ses kırpma** — süreler yazılarak `1.28` → `3.00` | ✅ 92,0 sn'lik yeni MP3, orijinal yerinde (telefondaki ffmpeg çalışıyor) |
| **Video kırpma** — `0.05` → `0.20` | ✅ 15,0 sn'lik yeni MP4 (H.264 + AAC), orijinal yerinde |
| Telefondaki yt-dlp motoru | ✅ Emülatörde ayağa kalkıp linki işledi |
| yt-dlp seçenekleri (MP3 en iyi ses, ≤720p H.264, liste klasör adı) | ✅ Masaüstü yt-dlp ile doğrulandı |
| **YouTube'dan gerçek indirme** | ⚠️ Test edilemedi: YouTube bu bulut sunucusunun IP'sine indirmede 403 veriyor. Gerçek telefonda (ev/mobil internet) denenmeli |

### İnternetten indirme (2. sürüm) — emülatörde uygulamanın kendi ekranlarından

Emülatörün interneti sunucunun proxy'sinden geçirildi; uygulamanın kendi yt-dlp / gallery-dl'i kullanıldı.

| Test | Sonuç |
|---|---|
| TikTok profili `@tiktok`, son 3 | ✅ 3/3 video, "tiktok, 3 içerik, TikTok", TikTok logolu klasör, Linkler.txt 3 link |
| X tek gönderi (girişsiz) | ✅ H.264 MP4 |
| Instagram profili `natgeo`, Reels, son 3 (girişsiz) | ✅ 3/3, "National Geographic, 3 içerik, Instagram" |
| Pinterest panosu, listeden 2 tanesini seçip indirme | ✅ 2/2, H.264 720p, başlıklarıyla |
| Pinterest profili `pinterest`, son 5 | ✅ 5/5, "Pinterest, 5 içerik, Pinterest" |
| Instagram tek reel (`instagram.com/kullanici/reel/ID?igsh=…`) | ✅ tek içerik olarak tanındı ve indi |
| Kod incelemesi (4 alan, her bulgu ayrı ajanla doğrulandı) | ✅ doğrulanan bulgular düzeltildi: iptal yarışları, X çerez denemesi, gönderi sayımı, klasör adı kesilmesi, bayt sınırı, çerez dosyasının geri yazılması, link tanıma hataları |
| YouTube oynatma listesi seçim ekranı | ✅ 13 video listelendi, seçim çalışıyor (indirme bu sunucudan engelli) |
| X profili `NASA`, son 5 (girişsiz) | ✅ 5/5, "NASA, 5 içerik, X", X logolu klasör, Linkler.txt |
| X profili `SpaceX`, son 3, **Müzik (MP3)** (girişsiz) | ✅ 3/3 MP3 |
| X profili `MKBHD` (X'in girişsiz popüler gösterdiği hesap), son 3 ve son 10 | ✅ FxTwitter ile gerçekten en yeni 3 / 10 video (21 Ekim'den geriye), "Marques Brownlee, 10 içerik, X" |
| FxTwitter kapalıyken (ara proxy ile engellendi): `NASA` son 5 | ✅ misafir erişimle aynı en yeni 5 video |
| FxTwitter kapalıyken: `MKBHD` son 5 | ✅ yedek yollar çalıştı; X sadece popülerleri verdiği için 4 video indi ve kartta "en yeniler için X'e giriş yap" notu çıktı |
| Olmayan X kullanıcısı | ✅ "Bulunamadı (link ya da kullanıcı adı yanlış olabilir)" |
| X'e giriş yapılmış haliyle profil | ⚠️ Test hesabı olmadığı için denenmedi (girişsiz yol yeterli) |
| Uygulama içi giriş (WebView) | ⚠️ Emülatörün tarayıcısı internete çıkamadığı için denenmedi |
