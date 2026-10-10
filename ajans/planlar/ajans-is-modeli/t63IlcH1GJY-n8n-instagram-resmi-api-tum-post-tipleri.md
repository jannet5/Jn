# Automate Every Instagram Post Type (Reels, Stories, Carousels) with n8n + Official API — Lakshit Ukani | AI Automation — 25 dk — https://www.youtube.com/watch?v=t63IlcH1GJY

## Tek paragraf: ne anlatıyor
Üçüncü parti araç kullanmadan, doğrudan Meta'nın resmi Instagram Graph API'si ile n8n üzerinden tek görsel, story (görsel + video), reel ve carousel paylaşımını uçtan uca kuran teknik eğitim. Önce developers.facebook.com'da uygulama oluşturma, izinler, Instagram tester ekleme, Graph API Explorer ile token üretme ve token'ı 2 aya uzatma anlatılıyor; sonra Instagram API'nin iki adımlı mantığı (önce "container" oluştur → container ID al → sonra "media_publish" ile yayınla) açıklanıyor; ardından her post tipi için hem n8n HTTP Request düğümü hem de n8n'in hazır Facebook Graph API düğümü ile aynı akış gösteriliyor. Yaratıcı 40-50 saat dokümantasyon okuduğunu söylüyor; şablon Skool topluluğunda.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Video kendisi bir hizmet satmıyor; eğitim içeriği. Yaratıcının geliri: Skool topluluğu (şablon indirme + dokümantasyon sayfası) ve "Want AI Integration for Your Business?" ile cal.com üzerinden 60 dk görüşme → işletmelere AI entegrasyon danışmanlığı. Fiyat vermiyor.
- Bizim için anlamı: aylık abonelik aracı (Buffer/Postiz/Blotato) ödemeden, müşteri başına sıfır maliyetli bir yayınlama altyapısı; sattığımız şey "içerik + otomatik yayın" paketinin arka planı olur.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
Videoda yok. (Sadece açıklamada "✅ Want AI Integration for Your Business? Work Mail / Schedule Call" inbound CTA'sı var.)

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: n8n (HTTP Request düğümü + Facebook Graph API düğümü), Meta for Developers (developers.facebook.com), Graph API Explorer, Access Token Debugger, Instagram Business hesabı + bağlı Facebook Sayfası, canlıda barındırılan medya URL'leri (görsel/video/kapak).

### A) API kurulumu (sırayla)
1. developers.facebook.com → My Apps → Create App. Uygulama adında "IG", "Instagram", "Facebook" gibi terimler YASAK (hata verir); örn. "n8n integration tutorial".
2. Use case: **Other** → App type: **Business** (Consumer tipi gereken API erişimini vermez). Business portfolio gerekmez → Create App.
3. Dashboard → Instagram → **Set up**. "Content no longer available" hatası çıkarsa: sayfayı yenile / App Roles'a gir çık / WhatsApp Set up'a bir kez tıklayıp geri dön; bazen 5 dk, bazen 1-2 gün sürüyor.
4. App Roles → Roles → **Add People → Instagram Tester** → paylaşım yapılacak Instagram hesabını ekle.
5. Instagram'da `instagram.com/accounts/manage_access` → "Tester invites" → uygulamayı **Accept**.
6. Doğrulama: Dashboard → "API setup with Instagram login" → hesap listede görünüyorsa tamam.
7. Token üretme — iki yöntem:
   - "API setup with Instagram login → Generate token": yaratıcıya göre "nadiren çalışıyor"; n8n Facebook düğümünde kimlik doğrulama başarısız oldu.
   - **"API setup with Facebook login" → Tools → Graph API Explorer** (her zaman çalışıyor): yeni uygulamayı seç → Add permission: Instagram ile başlayan **9 izin** (instagram_basic, instagram_branded_content_*, instagram_content_publish, instagram_manage_* vb.) + **pages_show_list** = toplam 10 izin → Generate Access Token → Facebook ile giriş → Instagram'a bağlı Facebook Sayfasını seç → tüm izinleri onayla.
   - Ön koşul: Facebook Sayfası ↔ Instagram hesabı bağlı olmalı (Sayfa → Settings → Linked accounts).
8. Token süresi 1 saat → Tools → **Access Token Debugger** → token'ı yapıştır → Debug → **Extend Access Token** → yeni token'ı tekrar debug et → "valid for 2 months".
9. Instagram hesap ID'si (akışta "node" adıyla kullanılıyor): Dashboard → API setup with Instagram login → Generate access token bölümündeki hesap ID'sini kopyala.

### B) Instagram API mantığı
İki adım: (1) **Containerization** — medyayı `POST /{ig-user-id}/media` ile Meta sunucusuna gönder → `id` (container ID) döner; (2) **Publish** — `POST /{ig-user-id}/media_publish?creation_id={container_id}`. Video/reel'de container hazır olana kadar `GET /{container_id}?fields=status_code` ile **polling** (döngüde bekle-kontrol et).

### C) n8n akışı (düğüm düğüm)
1. **Manual Trigger** (demo; gerçekte Schedule/Webhook/Sheet olabilir).
2. **Set (Edit Fields)** — alanlar: `node` (= Instagram hesap ID), `post_type` (örn. `http_image`, `fb_image`, `http_story_image`, `fb_story_image`, `http_story_video`, `fb_story_video`, `http_reel`, `fb_reel`, `http_carousel`), `image_url`, `caption`, `video_url`, `cover_url`.
3. **Switch** — `post_type` değerine göre 9 dala yönlendirir (her tip için HTTP Request sürümü + Facebook Graph API düğümü sürümü; carousel sadece HTTP).
4. **Container oluşturma** (dala göre):
   - HTTP Request: `POST https://graph.facebook.com/v22.0/{{ $json.node }}/media`; Auth: Generic → Header Auth, `Authorization: Bearer <token>` (ya da düz header). Query parametreleri:
     - Tek görsel: `image_url`, `caption`
     - Story görsel: `media_type=STORIES`, `image_url`
     - Story video: `media_type=STORIES`, `video_url`
     - Reel: `media_type=REELS`, `video_url`, `caption`, `cover_url`, `audio_name`
     - Carousel: önce her görsel için ayrı container: `image_url`, `is_carousel_item` (videoda sadece image_url geçildi) → her birinden ID al → sonra ana container: `media_type=CAROUSEL`, `children=<id1>,<id2>` (virgülle), `caption`
   - Facebook Graph API düğümü: Credential = sadece access token; Host URL default, Method POST, Version **22.0**, Node = hesap ID, **Edge = `media`**, Query parametreleri aynı (image_url/caption/media_type/video_url/cover_url).
5. **Set** — dönen `id`'yi `container_id` değişkenine eşle (birden fazla dal buraya birleştiği için).
6. **Wait** — 5 sn (video için daha uzun sürebilir).
7. **HTTP Request (GET)** `https://graph.facebook.com/v22.0/{{ container_id }}?fields=status_code`; Auth: Query Auth (`access_token=<token>`).
8. **IF** — `status_code == "FINISHED"` → devam; değilse (`IN_PROGRESS`) → Wait'e geri dön (polling döngüsü).
9. **IF** — `post_type.split("_")[0] == "http"` → HTTP yayın dalı; değilse Facebook düğümü dalı.
10. **Yayın** — HTTP: `POST https://graph.facebook.com/v22.0/{{ node }}/media_publish`, query `creation_id={{ container_id }}`. Facebook düğümü: Node = hesap ID, **Edge = `media_publish`**, query `creation_id`.
11. Dönen `id` = yayınlanan medya ID. Instagram'da post/story/reel/carousel görünür.

Not: Yaratıcının "Helpful documentation" sayfasında Meta'nın media ve media_publish referansları var; container'da ayrıca `alt_text`, `location_id`, `user_tags`, `product_tags` parametreleri geçilebiliyor.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
Videoda yok. (Teknik altyapı videosu; içerik stratejisi/bütçe vermiyor.)

## Hatalar / uyarılar
- Uygulama adında "IG/Instagram/Facebook" geçerse Meta reddediyor.
- "Consumer" tip uygulama gerekli izinleri vermiyor → **Business** seç.
- Instagram "Set up" ekranı "content no longer available" verebiliyor; saatler/günler sürebilen Meta tarafı gecikmesi.
- Instagram-login ile üretilen token n8n Facebook düğümünde çalışmadı → Facebook-login + Graph API Explorer kullan.
- İlk token 1 saatlik; Debugger ile uzatmazsan akış 1 saat sonra durur. Uzatılmış token da 60 gün → takvime yenileme hatırlatması koy (video uzun ömürlü/sistem kullanıcı token'ına değinmiyor).
- Medya URL'leri **herkese açık, doğrudan erişilebilir** olmalı (Drive paylaşım linki olmaz).
- Video/reel container'ı hemen hazır olmaz → Wait + status_code polling şart; yoksa publish hata verir.
- n8n Facebook düğümünde carousel desteği yok → carousel için HTTP Request zorunlu.
- Açıklamadaki şablon Skool (kapalı topluluk) arkasında → indirilemedi; akış transkriptten yeniden çıkarıldı.

## Bizim ajansa alınacaklar (somut)
1. **Yayın altyapısını bu akışla kuralım**: müşteri başına ayrı Meta uygulaması değil, ajansın tek Business uygulaması + her müşterinin Instagram hesabı tester/Sayfa yetkisiyle bağlanır; token'lar n8n credential'ında müşteri adıyla tutulur (video "isimlendirme çok önemli" diyor). Postiz/Blotato aboneliğine ödeme yapmadan müşteri başı maliyet 0 TL.
2. **Tek "IG Publisher" alt-akışı (sub-workflow)**: girdi = `{hesap_id, post_type, image_url(ler), video_url, caption, cover_url}`; içeride Switch → container → Wait/polling → publish. İçerik üreten diğer akışlar (carousel, reel, story) bunu çağırır.
3. Medya barındırma için Cloudinary/S3/Supabase Storage standardı: tüm görseller yayın öncesi public URL'ye yüklenir.
4. **Token takvimi**: n8n'de her 50 günde bir "token yenile" hatırlatması (Telegram/Slack) — müşteri hesabı sessizce durmasın.
5. Satış paketi dili: "Postlar, story'ler, reel'ler ve carousel'ler resmi Instagram API ile otomatik yayınlanır; üçüncü parti uygulama şifrenizi almaz" (güven argümanı, Türkiye'de KOBİ'lerin hesap kaptırma korkusuna cevap).
6. Onay kapısı ekle: bu akış doğrudan yayınlıyor; bizde publish'ten önce Telegram/WhatsApp onay düğümü koyalım.
