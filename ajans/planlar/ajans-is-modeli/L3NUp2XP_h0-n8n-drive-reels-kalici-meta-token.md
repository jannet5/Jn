# Easy n8n Instagram Automation: Post Reels With AI Generated Captions — Kirk Artman — 34 dk — https://www.youtube.com/watch?v=L3NUp2XP_h0

## Tek paragraf: ne anlatıyor
Google Drive klasörüne kısa videolar atılıyor; n8n her gün belirlenen saatte (ör. 09:00) klasörden bir video alıyor, OpenAI (GPT-4o mini) **dosya adından** caption yazıyor, Airtable'a kaydediliyor (public URL için), Facebook Graph API ile önce bir "container" oluşturulup 60 sn beklendikten sonra `media_publish` ile Reels olarak Instagram'a yayınlanıyor. Videonun asıl değeri: Meta developer app kurup System User üzerinden **süresi dolmayan (never expire) Instagram erişim token'ı** almanın adım adım anlatımı (normal token'lar 60 günde ölüyor).

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Yaratıcı: Skool topluluğu "Digital Automation Diagram" (workflow şablonları, prompt'lar, ek eğitimler); ücretsiz "DAD Kit" PDF; n8n/Airtable/vidIQ affiliate linkleri. Fiyat videoda yok.
- Bizim için ürün: "Klasöre at, her gün otomatik paylaşılsın" Reels yayın hattı — içerik çekimini müşteri yapar, yayın/caption/zamanlamayı sistem.

## Müşteri bulma: hangi kanal, hangi mesaj
- Yok (YouTube → Skool). Satış cümlesi olarak: "Yüzlerce videoyu bir klasöre doldurun, sistem her sabah 9'da bir tanesini caption ve hashtag ile paylaşsın."

## Üretim süreci: hangi araçlar, hangi otomasyonlar
Araçlar: n8n, Google Drive, OpenAI (GPT-4o mini), Airtable (ücretsiz), Meta Business Suite, developers.facebook.com, Facebook Graph API düğümü, TermsFeed (gizlilik politikası üretici).
1. **Drive trigger** — "On changes involving a specific folder", klasör By URL, Watch for: File Created. Klasör "Anyone with the link" olarak **paylaşılmalı**. Test etmeden önce klasörde dosya olmalı. Poll time: Every Day 09:00 → her çalışmada 1 dosya → günde 1 post.
2. **Drive download** — Operation: Download, File By ID = trigger'dan file id.
3. **OpenAI** — Message a model, GPT-4o mini; user mesajı = dosya adı; assistant prompt (aynen): "You are skilled at writing detailed captions based on a file name. Write a clear and engaging caption." → **Dosyaları caption ne olacaksa öyle adlandır** (örn. "exciting expansions discovering more with fal").
4. **Airtable** — Base "IG res pass through", tablo "Media"; alanlar: Name, Caption, URL. Token scope'ları: `data.records:read`, `data.records:write`, `schema.bases:read` + base erişimi. Token bir kez gösterilir. URL = Drive "web content link". Gerekçe: "meta API needs a publicly accessible URL ... those public URLs can't be from Dropbox or Google Drive".
5. **Meta app (zor kısım)** — Kişisel Facebook hesabı şart → developers.facebook.com → Create App → Other → Business → Instagram ürününü ekle → App settings > Basic: privacy policy, terms of service, user data deletion URL'leri (TermsFeed ile üretilmiş) → app'i **Live** yap.
6. **System user + kalıcı token** — Business Suite → Settings → System Users → Apps → "Connect an app ID" → app'e system user'ı "Full control / manage app" ata → Generate token → expiration **Never** → izinler: `business_management`, `instagram_basic`, `instagram_content_publish`, `instagram_manage_comments`, `instagram_manage_insights`, `instagram_manage_messages`, `pages_show_list`, `publish_video` (8 adet).
7. **Create container** — Graph API düğümü: POST, son sürüm, Node = Instagram hesap ID (Business Suite → Instagram accounts), Edge = `media`, query parametreleri: `video_url` = Airtable URL, `media_type` = `REELS`, `caption` = OpenAI içeriği + iki satır boşluk + sabit hashtag'ler.
8. **Wait 60 sn** — container hazır olmadan publish hata veriyor.
9. **Post to IG** — POST, Edge = `media_publish`, query `creation_id` = container id.

## Sosyal medya / reklam
- Format: Reels, günde 1, caption + sabit niş hashtag'leri.
- Reklam yok.

## Hatalar / uyarılar
- Normal IG token 60 günde biter → System User + "Never" şart; yoksa her 2 ayda kurulum tekrarı.
- Container hazır olmadan publish → hata; Wait düğümü ekle.
- Drive/Dropbox URL'leri Meta tarafından kabul edilmiyor (yaratıcının iddiası; Airtable aslında Drive linkini saklıyor — pratikte kendi depolama/CDN URL'si daha güvenli).
- Dosya adından caption = zayıf bağlam; marka sesi yok.
- Onay kapısı yok, tam otomatik.
- Meta app için gizlilik politikası/şartlar/veri silme sayfası gerekiyor.

## Bizim ajansa alınacaklar (somut)
1. **Meta kalıcı token SOP'u**: her yeni müşteri için yukarıdaki 6. adımı kontrol listesi olarak yazalım (müşterinin Business Portfolio'sunda bizim app'e system user erişimi; 8 izin; Never). Mevcut `t63IlcH1GJY` resmi API planıyla birleştir.
2. App için gereken gizlilik politikası / kullanım şartları / veri silme sayfalarını kendi ajans sitemizde (KVKK aydınlatma metniyle birlikte) Claude Code ile bir kez üretelim; tüm müşteri entegrasyonlarında aynı URL'ler.
3. Müşteri "Reels kutusu": müşteriye paylaşımlı Drive klasörü; dosya adı = konu. Bizim versiyonda caption dosya adından değil **müşteri profili + design.md + konu** ile Claude'dan; Airtable yerine kendi depolama (Supabase Storage/R2) public URL.
4. Akışa onay kapısı: caption + önizleme Telegram/WhatsApp'a → "onayla" sonrası container + Wait 60 sn + publish.
5. Paket: "Haftada 5 Reels, siz çekin klasöre atın, biz yazıp yayınlayalım" — aylık sabit ücretli sosyal medya basamağı.
