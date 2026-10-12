# N8N Instagram Automation | Step-by-Step Guide (Free Template) — Rajeevdaz — 17 dk — https://www.youtube.com/watch?v=m02TeQ9kHVo

## Tek paragraf: ne anlatıyor
Rajeev, üçüncü parti araç kullanmadan Instagram Reels'i otomatik paylaşan n8n akışını sıfırdan kuruyor ve "0'dan 43.000 takipçiye 6 ayda" bu akışla geldiğini söylüyor. Akış: günde iki kez (09:00 ve 18:10) çalışan Schedule Trigger → Google Sheets'ten "Status" boş olan ilk satırı al → Gemini 2.5 Flash'lı AI Agent ile "Outline" sütunundan SEO uyumlu kısa caption + 2 hashtag üret → videoyu Cloudinary'deki herkese açık URL'den (dosya adı Sheet'ten) Graph API ile REELS container'ı olarak yükle → 60 sn bekle → `media_publish` ile yayınla (hata olursa 5 sn arayla tekrar dene) → Sheet satırını "Posted" yap. Meta tarafında Instagram'ı Professional'a çevirme, Facebook sayfasına bağlama, Business Portfolio'da **System User** oluşturup varlık atama ve **60 gün veya "never"** süreli token üretme adım adım gösteriliyor. Ücretsiz JSON şablonu sitesinde (indirildi, kaynaklara kaydedildi).

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Video ücretsiz şablon + YouTube kanalı; sitesinde "Have an Automation project in mind? Let's discuss. Contact Me" ile otomasyon hizmeti/freelance işi çekiyor. Açıklama: "Perfect for creators, agencies, and automation lovers!"
- Fiyat: rakam vermiyor.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Kanal: YouTube + blog'da ücretsiz şablon (lead magnet) → iletişim. Mesaj şablonu: Videoda yok.
- Sosyal kanıt cümlesi: "This is the exact workflow I used to completely automate my Instagram posting without any third-party tools and the results. I went from 0 to 43,000 followers in just 6 months."

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: n8n, Google Sheets, Google Gemini (Gemini 2.5 Flash, ücretsiz API), Cloudinary (ücretsiz, "up to 25 GB"), Meta Business Suite (business.facebook.com), Meta for Developers, Instagram Professional hesap + Facebook sayfası.

**Google Sheet yapısı ("Reels Automation" / Sheet1) — 3 sütun**
- `File name` — video dosya adıyla birebir aynı (örn. `WhatsApp_AI_agent.mp4`). **Kural: dosya adında boşluk olmasın; alt çizgi veya tire kullan.**
- `Outline` — videonun tek satırlık konusu (caption girdisi).
- `Status` — boş = sırada; paylaşılınca akış "Posted" yazar.

**Cloudinary ayarı (dosya adları değişmesin diye)**
1. Ücretsiz hesap → Assets → Folders → yeni klasör "reels".
2. Settings → Upload → Upload preset → üç nokta → Edit:
   - "Use the file name of the uploaded file as the public ID" → **açık**
   - "Append a unique suffix" → **kapalı**
   - "Use the initial asset folder path" → **açık** → Save.
3. Videoları klasöre yükle → bir videoda "Copy original URL" → URL'nin dosya adından önceki kısmı sabit; sonuna Sheet'teki dosya adı eklenecek.

**Meta tarafı (System User token — uzun ömürlü)**
1. Instagram → Settings → Account type and tools → **Switch to professional account** → Creator veya Business → kategori.
2. facebook.com/pages → sayfa oluştur (veya mevcut) → Professional dashboard → **Linked accounts → Instagram → Connect** (otomatik Business Portfolio oluşur, doğrulama kodu).
3. business.facebook.com → Settings → Pages ve Instagram accounts listede mi kontrol.
4. Apps → **Create a new app ID** → developers.facebook.com → ad ("realflow") → Use case **Other** → **Other** → App type **Business** → aynı business portfolio → Create app.
5. business.facebook.com'u yenile → app listede.
6. **System users → Add** → ad → rol **Admin** (videoda hata verdi → **Employee** ile oluşturuldu).
7. **Assign assets**: Facebook sayfası = Full control; App = Full control; Instagram hesabı = Full control.
8. **Generate token** → app seç → süre **60 days veya never** → 6 izin: `business_management`, `instagram_basic`, `instagram_content_publish`, `pages_show_list`, `pages_manage_posts`, `pages_read_engagement` → token'ı n8n "Facebook Graph API" credential'ına yapıştır.
9. IG hesap ID: business.facebook.com → Instagram accounts → ID.

**n8n akışı — düğüm düğüm (şablon JSON ile birebir)**
1. **Schedule Trigger** — Interval: Days, "Days between triggers" = 1; Trigger at hour 9 (dakika 0) + "Add rule" ile 18:10. (İstersen daha fazla kural.)
2. **Google Sheets — "Get row(s) in sheet"** — Doküman "Reels Automation", Sheet1; Filter: kolon `Status`, değer **boş**; Option: **Return only first matching row** = açık → her çalışmada tek satır.
3. **AI Agent** — Source for prompt: Define below; Prompt: `Video Topic: {{ $json.Outline }}`; System message (aynen):
   ```
   You are a social media expert who writes engaging and SEO optimized instagram captions.
   create a short and catchy caption for the given video topic.
   Make sure the tone sounds natural and appealing to the audience.
   Add two highly relevant hashtags at the end that fit the topic.
   ```
   Türkçesi: kısa, akılda kalıcı, doğal tonda caption ve sonda konuyla ilgili 2 hashtag. (Opsiyonel düğüm: Sheet'te hazır caption varsa atlanır.)
   - Alt düğüm **Google Gemini Chat Model** — model Gemini 2.5 Flash (ücretsiz API).
4. **Facebook Graph API — container** — POST, **v23.0**, Node = IG hesap ID, Edge = `media`, Query parameters:
   - `video_url` = `https://res.cloudinary.com/<cloud>/video/upload/Reels/{{ $('Get row(s) in sheet').item.json['File name'] }}` (expression)
   - `caption` = `{{ $json.output }}` (AI Agent çıktısı)
   - `media_type` = `REELS` (büyük harf)
   → çıktı: container `id` (video henüz yayında değil).
5. **Wait** — 60 saniye (Facebook'un container'ı işlemesi için; yoksa publish hata verebiliyor).
6. **Facebook Graph API — publish** — POST, v23.0, Node = IG ID, Edge = `media_publish`, Query: `creation_id` = `{{ $json.id }}`; Settings: **Retry on fail** açık, **Wait between tries 5000 ms** (videoda "3 kez daha" tekrar).
7. **Google Sheets — "Update row in sheet"** — Column to match on: `row_number` = `{{ $('Get row(s) in sheet').item.json.row_number }}`; File name ve Outline alanlarını sil; `Status` = `Posted`.
8. Workflow → **Activate**; Settings → **Time zone** doğru seçilmeli (yoksa yanlış saatte tetikler).

Tam JSON: `ajans/arastirma/youtube/kaynaklar/m02TeQ9kHVo-rajeevdaz-n8n-reels-sablonu.md`.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: Reels (eğitim videoları — "WhatsApp AI agent", "Google Gemini API" vb.).
- Sıklık: **günde 2 Reels** (09:00 ve 18:10), şablonda tanımlı.
- Caption: AI ile kısa + 2 hashtag.
- Sonuç iddiası: 6 ayda 0 → 43.000 takipçi.
- Reklam/bütçe: Videoda yok.

## Hatalar / uyarılar
- Dosya adında boşluk → URL bozulur; alt çizgi/tire kullan, Sheet'teki adla birebir aynı olmalı.
- Cloudinary varsayılanı dosya adını değiştirir/sonek ekler → upload preset ayarı şart.
- Facebook API Google Drive / OneDrive'dan doğrudan yüklemeye izin vermiyor; **public video link** gerekiyor.
- Container hemen hazır olmuyor → 60 sn Wait + retry.
- System user "Admin" rolüyle oluşturulurken hata verdi; "Employee" ile çözüldü.
- Kişisel/özel hesap bağlanamaz; Professional + Facebook sayfası bağlantısı şart.
- n8n saat dilimi ayarı yanlışsa paylaşım yanlış saatte gider (Türkiye için Europe/Istanbul).
- Şablon JSON'da yazarın IG ID'si (17841466876322) ve Cloudinary bulut adı (drzrtfh87) gömülü; değiştirilmeli.
- 43K takipçi iddiası doğrulanmamış.

## Bizim ajansa alınacaklar (somut)
1. **Ajans standart "Reels kuyruğu" akışı** bu şablon üzerine: Sheet sütunları `Müşteri | IG_ID | Dosya | Konu | Caption(opsiyonel) | Tarih | Status`; Schedule Trigger her müşteri için 2 slot (ör. restoran 11:30 öğle + 18:30 akşam; klinik 09:00 + 20:00). Sheets filtresi `Status boş + Müşteri`.
2. **Türkçe caption system message'ı**: şablondaki prompt'u Türkçeleştir + yerel bağlam ekle: "Sen Türkiye'deki yerel işletmeler için Instagram açıklaması yazan bir uzmansın. Kısa, samimi, Türkçe yaz; sonda işletmenin semtini içeren 2 alakalı hashtag ekle; CTA olarak 'Rezervasyon/randevu için DM veya WhatsApp' yaz."
3. **Meta bağlantıda System User token (never)** standardımız olsun: müşteri Business Portfolio'suna bizi partner olarak ekletip system user + 6 izin; token yenileme derdi biter (PXcDqmamX2Q'daki 2 aylık token yerine).
4. **Cloudinary preset** (public ID = dosya adı, sonek kapalı, klasör yolu açık) + dosya adlandırma kuralı `musteri_tarih_konu.mp4` — editör/AI video çıktıları bu adla yüklenir.
5. **Paket**: "Otomatik Reels Paketi" — ayda 30-60 Reels slotu, günde 1-2 paylaşım, AI caption, haftalık rapor Sheet'ten. Fiyatı biz belirleyeceğiz (videoda rakam yok); kurulum ücreti + aylık.
