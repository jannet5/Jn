# How I'm Creating Unlimited Viral AI Carousel Images with Claude (100% Works) — Sean Standberry — 12 dk — https://www.youtube.com/watch?v=OFoNYWIS7IA

## Tek paragraf: ne anlatıyor
Sean Standberry, GoHighLevel (HighLevel) alt hesabındaki Claude destekli "Ask AI" panelinde tek bir prompt ile yerel bir pastane için 12 carousel × 5 slayt = 60 görsel, her post için caption + hashtag üretip hepsini Instagram'a bir aylık takvimle (haftada 3, Pzt/Çar/Cuma 10:00 EST) zamanlamasını gösteriyor. Üretim ~20 dakika sürüyor. Ardından bunun küçük işletmelere aylık $500'lık "carousel içerik paketi" olarak nasıl satılacağını, satış görüşmesinden önce örnek carousel hazırlama taktiğini, brand board (logo, renk, font) kurulumunu ve tekrar kullanılabilir prompt şablonunu veriyor.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Kendi ajansı (iddia, ekranda ödeme işlemcisi gösteriliyor): geçen ay **$400.000+ gelir**, **~300 aktif müşteri**, müşteri başına **~$1.000/ay**. "What used to take us 10 hours per client per month now takes about 20 minutes."
- Hedef müşteri: "bakeries, restaurants, gyms, salons, dentists, real estate agents" — sosyal medyada paylaşmaları gerektiğini bilen ama zamanı/becerisi/bütçesi olmayanlar.
- **Starter paket: $500/ay (USD)** — ayda 12 carousel (haftada 3), her biri 5 slayt, caption + hashtag + zamanlama; "The client doesn't have to lift a finger."
- **Premium paket: $1.000/ay (USD)** — günlük paylaşım + Instagram story grafikleri + takipçiyi lead'e çeviren otomasyonlar.
- Ölçek hesabı (aynen): 10 müşteri × $500 = **$5.000/ay** ("maybe one day" iş); 20 müşteri = **$10.000/ay** ("working just two days a week"); 20, 30, hatta 50 müşteriye ekip kurmadan.
- Manuel karşılaştırma: Canva ile carousel başına **20-30 dk**; 12 carousel = müşteri başına **4-6 saat** sadece grafik.
- Araç: HighLevel, **30 gün ücretsiz deneme** (affiliate linki); her müşteri ayrı sub-account.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Kanal belirtmiyor; taktik **satış görüşmesinden önce müşteriye özel örnek carousel üretmek**: diş hekimi için "teeth whitening tips, new patient specials, before and after smile transformations"; spor salonu için "workout tips, nutrition guides, member success stories".
- Görüşmede ekran paylaşıp söylenecek cümle (aynen):
  > "Hey, I already put together some posts for you. Here's what it looks like."
  Türkçe: "Sizin için birkaç post hazırladım bile, bakın nasıl duruyor" — hazır işi göstererek güven kazanma.
- Elde tutma: müşteri "we don't do wedding cakes, can you change that one?" derse tek carousel'i Ask AI'a yeniden ürettir; "Five new slides done in under a minute."

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: GoHighLevel sub-account → sağ üstte **Ask AI** (Claude destekli; görsel üretir, caption yazar, hesap içinde aksiyon alır), Brand Board (logo, renk, font), bağlı Instagram hesabı (Social Planner). n8n/Make yok.

Akış (sıralı):
1. Müşteri sub-account'u aç → Instagram'ı bağla → Brand Board'a logo, marka renkleri, fontları yükle (veya Ask AI'a müşterinin web sitesini ver).
2. Önce **1 test carousel** (5 slayt) üret — "That one test takes 30 seconds" — renk/tarz tutuyorsa tam ay prompt'unu çalıştır.
3. Ask AI'a aylık prompt'u gir → AI 12 konuya böler → "generate image" adımlarıyla 60 görsel (~20 dk).
4. AI bağlı Instagram hesabını kontrol eder → zamanlama önerisi sunar → "Reply confirmed to schedule all posts." → `confirm` yazılır → 12 post zamanlanır.

Demo prompt'u (aynen):
```
Create 12 Instagram carousel posts for a local bakery business, five slides each. Each carousel should cover a different topic, things like custom cakes, seasonal specials, behind-the-scenes baking, ingredient quality, customer testimonials, and promotions.
```
Türkçe: Pastane için 12 carousel, her biri 5 slayt, her biri farklı konu.

Kopyalanabilir şablon prompt (transkriptteki gibi, yer tutucular parantezde):
```
Create [number] Instagram carousel posts for a [niche] business, [slides] slides each. Topics should include [list 4-6 themes]. Generate all carousel images with professional designs, bold text overlays, and a [color description] color palette. Write captions with hashtags for each post. Then schedule all posts to Instagram [frequency] per week on [days] at [time] starting [date].
```
Türkçe: Sayı, sektör, slayt, 4-6 tema, renk paleti, caption+hashtag ve zamanlama talimatını tek prompt'ta veren şablon.

Prompt stratejisi — 5 kural (aynen özet):
1. Carousel sayısını ve slayt sayısını açıkça yaz ("Create 12 carousel posts, five slides each"); belirsiz bırakırsan "three carousels with seven slides" gibi sonuç gelir.
2. Konu çeşitliliği ver: "custom cakes, seasonal specials, behind-the-scenes, ingredient quality, customer stories, promotions, tips, and tricks".
3. Görsel stili tarif et: "professional designs and bold text overlays and clean modern layout a color palette that matches the business".
4. Zamanlama talimatını aynı prompt'a koy: sıklık, günler, saat, başlangıç tarihi.
5. "Secret weapon": Brand Board — logo, marka renkleri, fontlar yüklenince her görselde otomatik kullanılır.

Tasarım kuralları (çıktıdan):
- Slayt sayısı: **5**. Kapak: "bold headlines that make you want to swipe". İç slaytlar: net text overlay + destek metin. Son slayt: web sitesine/siparişe yönlendiren CTA.
- Renk (pastane örneği): "golden amber, creamy whites, rich browns, dusty rose" — 60 görselde tutarlı.
- Font: Brand Board'dan (isim vermiyor).
- Caption: "a hook, the body copy, and relevant hashtags".

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik: 5 slaytlık carousel; konular: imza cupcake'ler, sahne arkası, düğün pastası tasarımları, pasta lezzetleri, pastacının hikâyesi, doğum günü pastası sipariş ipuçları, bahar menüsü.
- Sıklık: **12 post/ay, haftada 3 — Pazartesi, Çarşamba, Cuma 10:00 EST**, 4 hafta (6 Nisan – 1 Mayıs örneği). Premium: günlük + story.
- Reklam/bütçe: Videoda yok.

## Hatalar / uyarılar
- Gelir rakamları ve "20 dakikalık iş" iddiası video sahibinin beyanı; açıklamada "success should not be considered typical" uyarısı var. Video HighLevel affiliate satışı içeriyor.
- Vague prompt → generic sonuç; slayt/carousel sayısı tutmayabilir.
- Tüm 12'yi üretmeden önce 1 test carousel yap.
- AI görsellerindeki metin doğruluğu/yazım hatası konusu videoda hiç ele alınmıyor (Türkçe karakterler için mutlaka kontrol gerekli).
- Açıklamadaki linkler affiliate/kurs (go.seanstandberry.com) — talimat gereği açılmadı; şablon/prompt dokümanı linki yok.

## Bizim ajansa alınacaklar (somut)
1. **Paket yapısını kopyala**: "Başlangıç: ayda 12 carousel (haftada 3, 5 slayt), caption + hashtag + otomatik paylaşım" ve "Premium: günlük paylaşım + story + DM/lead otomasyonu" — iki kademe; TL fiyatı bizim fiyat listemizden (video referansı $500 / $1.000).
2. **Şablon prompt'u Türkçeleştir** ve n8n/Claude akışımıza gir: `[sayı] carousel, [5] slayt, konular: [4-6 tema], renk paleti: [marka renkleri], Türkçe caption + hashtag, Pzt/Çar/Cum 10:00 paylaş`. Restoran temaları: günün menüsü, şefin önerisi, sahne arkası, müşteri yorumu, kampanya, rezervasyon çağrısı.
3. **Müşteri başına "brand board" dosyası** tut (logo, 2-3 HEX renk, 1-2 font, ses tonu) — her üretimde prompt'a otomatik eklensin.
4. **Satış öncesi hazır örnek**: soğuk ziyaret/DM'den önce işletmeye özel 1-2 carousel üret; mesaj: "Sizin için birkaç post hazırladım, bakın nasıl duruyor — ayda 12 tanesi otomatik paylaşılsın ister misiniz?"
5. **Önce 1 test carousel, sonra toplu üretim** kuralını iş akışına koy; ilk ay müşteri onayından sonra tüm ayı zamanla.
