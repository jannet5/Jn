# How To Post To Instagram Via N8N [AI Automation] — WebSensePro — 16 dk — https://www.youtube.com/watch?v=PXcDqmamX2Q

## Tek paragraf: ne anlatıyor
WebSensePro, n8n'i resmi Instagram Graph API üzerinden bağlayıp tek görsel + açıklama paylaşan en temel akışı kuruyor. Odak "bağlantı" kısmı: developers.facebook.com'da yeni Business app oluşturma, Instagram hesabını **Instagram Tester** rolüyle ekleyip instagram.com/accounts/manage_access üzerinden daveti kabul etme, "Generate token" düğmesi çalışmadığı için **Graph API Explorer**'dan tüm Instagram izinleriyle token üretme, **Access Token Debugger** ile ~1 saatlik token'ı ~2 aylık uzun ömürlü token'a çevirme ve n8n'de "Facebook Graph API" credential'ı olarak kaydetme. Sonra iki Graph API düğümüyle iki aşamalı yayın: önce `media` edge'i ile container oluştur, sonra `media_publish` edge'i ile yayınla. Test görseli Unsplash URL'si; "görseli AI ile üret, zamanlanmış tetikle otomatik paylaş" önerisiyle bitiyor. Blog yazısında kopyala-yapıştır workflow JSON'u var (kaynaklara kaydedildi).

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- WebSensePro bir dijital ajans (web geliştirme, WordPress, Shopify, SEO, Google Ads, sosyal medya, "AI Automation"); video, ajansın AI otomasyon hizmetine inbound trafik. Açıklama: "If you are looking to get help related to AI Automation, feel free to reach us out at https://websensepro.com/contact-us".
- Fiyat: rakam vermiyor.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Kanal: YouTube eğitimi + blog (SEO) → iletişim formu. Mesaj şablonu: Videoda yok.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: n8n, Meta for Developers (App, App Roles, Graph API Explorer, Access Token Debugger), Instagram profesyonel hesap, Unsplash (test görseli). Prompt yok.

**A) Meta tarafı — credential kurulumu (sırayla)**
1. developers.facebook.com/apps → **Create app** → ad ("N8N YouTube tutorial") → Use case: **Other** → App type: **Business** → business portfolio boş bırakılabilir → Create app → Facebook şifresi.
2. Dashboard → Instagram → **Set up** → Add account → Instagram'a giriş (hesap **professional** olmalı, değilse geçiş isteniyor).
3. **App roles → Roles → Add people → Instagram Tester** → Instagram kullanıcı adını ekle → durum "pending".
4. Tarayıcıda **https://www.instagram.com/accounts/manage_access** → "Tester invites" → uygulamayı **Accept**. Roller sayfasında "pending" kalkmalı.
5. "API setup with Instagram login → Generate token" **çalışmıyor** (videoda) → **Tools → Graph API Explorer** → sağda doğru Meta app seçili → Permissions → Other → **tüm Instagram izinlerini** seç → **Generate Access Token** → yetkilendir (test için "tüm hesaplar", istenirse "opt in to current Instagram account only").
6. **Tools → Access Token Debugger** → token'ı yapıştır → Debug → ömür "about an hour" → **Extend Access Token** → yeni token ömrü "about two months" → tekrar debug ile kontrol.
7. n8n → yeni credential **Facebook Graph API** → yeniden adlandır → uzun ömürlü token'ı yapıştır → Save (yeşil = bağlandı).
8. IG hesap ID'si ("node"): Meta dashboard → Instagram API config → Generate access token bölümünde hesap adının altındaki numara.

**B) n8n akışı — düğüm düğüm (blog JSON'u "Instagram Posting Workflow")**
1. **Manual Trigger** ("When clicking ‘Test workflow’") — üretimde Schedule Trigger ile değiştirilecek.
2. **Set — "Set Image & Caption"**: alanlar
   - `imageUrl` = herkese açık görsel URL'si (Unsplash örneği)
   - `captionText` = "Hello from n8n! 👋 This is an automated post. #n8n #automation #instagram"
   - `Node` = `<your node id>` (IG hesap ID'si)
3. **Facebook Graph API — "Creating Container ID"**: Method **POST**, Graph API version **v22.0**, Node `={{ $json.Node }}`, Edge **`media`**, Query parameters: `image_url` = `{{ $json.imageUrl }}`, `caption` = `{{ $json.captionText }}` → çıktı: container `id`.
4. **Facebook Graph API — publish**: Method **POST**, **v22.0**, Node `={{ $('Set Image & Caption').item.json.Node }}`, Edge **`media_publish`**, Query parameters: `creation_id` = `{{ $json.id }}`, (ayrıca `image_url` ve `caption` da gönderiliyor — gerekli değil ama zararsız) → gönderi yayında.
5. Bağlantılar: Manual Trigger → Set → Creating Container ID → Publish.

Tam JSON: `ajans/arastirma/youtube/kaynaklar/PXcDqmamX2Q-websensepro-n8n-instagram-blog.md`.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: tek görsel + açıklama (carousel/reels yok). Sıklık: rakam vermiyor ("set a time" ile zamanlama öneriliyor). Reklam/bütçe: Videoda yok.

## Hatalar / uyarılar
- Meta'nın "API setup with Instagram login → Generate token" butonu videoda çalışmadı; Graph API Explorer yolu kullanıldı.
- Token'lar: Explorer token'ı ~1 saat, uzatılmış token ~2 ay → **iki ayda bir elle yenileme gerekir**, yoksa akış sessizce durur. (Kalıcı token için bkz. mevcut plan `L3NUp2XP_h0-n8n-drive-reels-kalici-meta-token.md`.)
- Test için "tüm Instagram hesaplarına" izin verildi; müşteri hesaplarında yalnızca ilgili hesaba izin verilmeli.
- Görsel URL'si **herkese açık** olmalı (Unsplash gibi); Google Drive paylaşım linki doğrudan çalışmaz.
- Instagram hesabı Professional (Business/Creator) olmalı.
- Workflow JSON içinde yazarın credential ID'si ve instanceId'si var; içe aktarınca kendi credential'ını seçmek gerekiyor.
- Videoda publish düğümünde API version'ı "20 22.0" diye okunuyor, JSON'da `v22.0`.

## Bizim ajansa alınacaklar (somut)
1. **Müşteri başına "Meta bağlantı kontrol listesi"** (yukarıdaki A adımları 1-8) Türkçe SOP olarak; ilk görüşmede müşteriden: Instagram'ı Professional'a çevirme + tester davetini kabul + FB sayfası bağlantısı. Kurulum ücreti bu işi kapsasın.
2. **Temel n8n şablonu**: bu 4 düğümlü akışı ajans şablonu olarak al; Manual Trigger → **Schedule Trigger**, Set → **Google Sheets satırı** (görsel URL + Türkçe açıklama + müşteri IG ID) ile değiştir. Tek akış, çok müşteri: IG ID ve token müşteri satırından.
3. **Token takvimi**: uzun ömürlü token ~2 ay → n8n'de her müşteri için token bitiş tarihi sütunu + 7 gün önce WhatsApp/e-posta uyarısı; mümkünse System User kalıcı token'a geç.
4. **Görsel barındırma**: AI ile üretilen görselleri herkese açık URL veren bir depoya (Cloudinary veya kendi sunucumuz) koy; Drive linki kullanma.
5. Satış argümanı: "Resmi Meta API ile paylaşım — üçüncü parti şifre paylaşımı yok, hesabınız güvende." Restoran/klinik sahiplerinin şifre verme çekincesini giderir.
