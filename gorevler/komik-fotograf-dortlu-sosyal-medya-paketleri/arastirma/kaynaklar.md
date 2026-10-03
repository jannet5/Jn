# Kaynaklar ve platform gerçekleri

## Fotoğraf kaynakları (tam liste ZIP içindeki foto/kaynaklar.csv'de, metadata: ../veri/secim-ham.json)
- Onedio — Başka Ülkede Hayatta Olmaz Dedirten 17 Komik Uyarı: https://onedio.com/haber/baska-ulkede-hayatta-olmaz-dedirten-komik-uyarilar-710325
- Onedio — Yurdum Esnafının Mizahını Konuşturduğu 19 Tekel Bayii: https://onedio.com/haber/yurdum-esnafinin-adeta-mizahini-konusturdugu-birbirinden-komik-19-komik-tekel-bayii-812671
- Onedio — Hangi Komik Apartman Mesajını Sen Yazardın?: https://onedio.com/haber/hangi-komik-apartman-mesajini-sen-yazardin-1207244
- Onedio — "Bu Neydi Şimdi?" Dedirten 21 Kare: https://onedio.com/haber/gorunce-insana-bu-neydi-simdi-dedirtecek-hem-absurt-hem-de-anlamsiz-21-kare-1077791
- Onedio — 17 Absürt Fotoğraf: https://onedio.com/haber/bakarken-bir-tarafinizin-huzunlenecegi-bir-tarafinizin-da-eglenecegi-17-absurt-fotograf-880236
- Haber7 — WC'nin anahtarı bizde yok: https://www.haber7.com/guncel/haber/3455735-gunde-defalarca-ayni-soruyla-karsilasan-esnaf-careyi-yazi-yazmakta-buldu-usandik-artik
- NTV — Apartman yöneticisinin güldüren isyanı: https://www.ntv.com.tr/turkiye/apartman-yoneticisinin-gulduren-isyani,DobIsP-d7kSrDgmYkVKDkg
- Ensonhaber — Samsun'da esnaftan çöp atanlara tepki: https://www.ensonhaber.com/yasam/samsunda-esnaftan-cop-atanlara-kufurlu-tepki
- X — https://x.com/moguzmuftuoglu/status/1792080247053013456 · https://x.com/olurunbu/status/1841432260416823354 (Onedio gömülerinden; veri cdn.syndication.twimg.com/tweet-result ile alındı)

## Erişilemeyenler
- x.com arama (giriş gerekli, curl yalnız JS kabuğu döndürdü), syndication.twitter.com zaman tüneli (429), Ekşi Sözlük (403), Reddit (403), tokchart (giriş).

## Resmî platform belgeleri
- Instagram karusele müzik (mobil; videolu karusele eklenemez): https://help.instagram.com/1223433768344104/
- Instagram müzik kütüphanesi (kişisel, ticari olmayan kullanım; bazı işletme hesapları erişemez): https://help.instagram.com/402084904469945/
- TikTok Foto Modu ve müzik: https://newsroom.tiktok.com/en-us/editing-tools
- TikTok işletme hesabı yalnız Commercial Music Library görür: https://ads.tiktok.com/help/article/how-to-use-the-commercial-music-library
- Instagram Graph API içerik yayınlama (Business/Creator, App Review, ≤10 öğe karusel, müzik parametresi yok): https://developers.facebook.com/docs/instagram-platform/content-publishing
- TikTok Content Posting API (denetimsiz istemci yalnız özel görünürlük): https://developers.tiktok.com/doc/content-posting-api-get-started
- TikTok foto gönderi API (≤35 görsel, `auto_add_music` yalnız önerilen müzik): https://developers.tiktok.com/doc/content-posting-api-reference-photo-post
- TikTok paylaşım kuralları: https://developers.tiktok.com/doc/content-sharing-guidelines
- Instagram kullanım şartları (izinsiz otomasyon yasağı): https://help.instagram.com/581066165581870
- TikTok kullanım şartları (otomatik betik yasağı): https://www.tiktok.com/legal/page/row/terms-of-service/en

## Topluluk deneyimleri
- tiktok-uploader (Playwright): "not fool proof… fail to upload after too many uploads": https://pypi.org/project/tiktok-uploader/
- instagrapi en iyi uygulamalar (her seferinde girişin şüpheli olduğu, hesap dondurma riski): https://subzeroid.github.io/instagrapi/latest/usage-guide/best-practices/
