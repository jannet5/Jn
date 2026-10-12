# How we Automated a Meta Ad Agency with AI — Ben AI — 29 dk — https://www.youtube.com/watch?v=Rm3ObJXsuOI

## Tek paragraf: ne anlatıyor
Ben van Sprundel (Ben AI), ajansının bir müşterisi (yüzlerce VSL — video sales letter — tarzı video reklam çalıştıran, birden çok ürün satan bir şirket) için n8n + Airtable ile kurdukları "ad agency in a box" sistemini gösteriyor. Ön yüz Airtable Interfaces (insan karar verici), arka uç n8n. Döngü 5 faz: (1) ürün + müşteri profili derin araştırması (Perplexity Sonar Reasoning), (2) reklam açısı/fikir üretimi ve insanın seçmesi, (3) VSL senaryosu üretimi + geri bildirimle revizyon (Gemini 2.5 Pro), (4) Meta reklam performansının günlük/haftalık Airtable'a çekilmesi (Facebook Graph API), (5) kazanan reklamların videosunun bulunup (Scrapfly) transkript edilmesi (OpenAI) ve yeni ürünlerin açı/senaryo üretimine "winning ad script" olarak beslenmesi. Video üretimi (HeyGen ile) henüz otomatik değil. Şablon "ücretsiz" olarak açıklamada veriliyor (e-posta kapısı arkasında).

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Kime: Meta'da çok sayıda video reklam çalıştıran e-ticaret/ürün şirketleri ve Meta reklam ajansları (çok markalı yönetim desteklendiği için "ajans için kutuda ajans").
- Ne: Özel kurulum AI otomasyon sistemi (Ben AI'ın ajansı "Work with my AI Agency" ile kuruyor) + ücretli toplulukta (AI Accelerator) şablonlar ve birebir teknik destek + ücretsiz şablon (lead magnet).
- Fiyat: rakam vermiyor (ne kurulum ücreti ne topluluk ücreti söyleniyor). Kanal tanıtımında "I built two $1M ARR AI Businesses" deniyor (USD, yıllık tekrarlayan gelir).
- Paketleme: 1) ücretsiz şablon → 2) ücretli topluluk (birebir tech help) → 3) done-for-you ajans kurulumu. Sonuç iddiası: müşteri "huge uptake in the return on ad spend" gördü (oran vermiyor). Demo tablosunda bir reklam için "27 ROAS" örneği gösteriliyor.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Kanal: YouTube eğitim videosu + açıklamadaki ücretsiz şablon (Kit e-posta opt-in) → topluluk / ajans.
- Kapanış mesajı (aynen):
  > "If you need some more help, you want some hands-on uh help to set up this template, etc., you can work directly with our agency to set up systems like these, or you can check out my uh paid community"
  Türkçe: Kurulumda yardım isteyen ajansla çalışabilir ya da ücretli topluluğa katılabilir.
- Satış argümanı: "So you can imagine the amount of work saved for this company by automating these processes" — iş yükü tasarrufu + ROAS artışı.
- Soğuk DM/e-posta şablonu videoda yok.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Airtable (veritabanı + Interfaces ön yüzü + Automations), n8n, Perplexity API (Sonar Reasoning modeli), Google Gemini 2.5 Pro (yaratıcı yazım), n8n Structured Output Parser (ayrı düğümde), Facebook Graph API / Marketing API, Scrapfly (web scraper), Code node (ChatGPT'ye yazdırılmış URL çıkarıcı), HTTP Request, OpenAI transcribe, HeyGen (planlanan video üretimi).

Airtable yapısı:
- Tablolar: Projects/Brands, Products, Angles, Scripting, Ad Performance Analytics, Best Performing Scripts.
- Product alanları: ürün adı, product notes (demografi vb. opsiyonel bağlam), status, product details (Perplexity çıktısı: core customer profile, product deep dive, teknik detaylar, fiyat, brand identity, market positioning), winning ad script (faz 5'ten bağlanan).
- 5 Airtable Automation: best ads, generate product details, generate product angles, generate scripts, feedback iteration.
- Her otomasyon: Trigger "When record matches a condition" (ör. "generate product details" kutusu işaretlendiğinde) → Action 1: kaydın status alanını güncelle ("generating...") → Action 2: "Run script" ile n8n webhook'unu çağır; yalnızca Airtable record ID + `action` parametresi gönderilir (ör. action = "generate project details"). Dört üretim otomasyonu aynı webhook'u kullanır; n8n'de Switch düğümü `action`'a göre yönlendirir.

n8n akışı 1 — Ürün araştırması:
1. Webhook (action = generate product details) → Switch
2. Airtable Get Record (record ID ile)
3. Prompt oluştur (değişkenler: product name, product notes) — prompt metni videoda açık okunmuyor; özü "research about the customer profile etc."
4. Perplexity — Sonar Reasoning modeli (derin araştırma)
5. Airtable Update: product details + customer ICP profile

n8n akışı 2 — Açı üretimi:
1. Webhook (action = generate angles) → Switch
2. Airtable Get Record
3. LLM chain; değişkenler: product name, product details (Perplexity çıktısı), product notes, winning script (varsa). Prompt talimatı (videoda okunan kısım): "provide at least five and up to eight" farklı açı; her açı için angle, concept, VSSL structure, video suggestions, emotional triggers, cognitive biases, direct response techniques; çıktı array formatında.
4. Ayrı bir Structured Output Parser düğümü (Ben'in notu: karmaşık veri yapısında çıktı ayrıştırıcıyı ayrı düğüme koymak "works far better")
5. Record ID'yi Switch düğümünden al → Split Out (ör. 6 açı → 6 ayrı satır)
6. Airtable: her açıyı ürüne bağlı kayıt olarak oluştur
7. Airtable Update: status = "angles generated"

Açı çıktısının içeriği (demo): concept summary; VSL yapısı "hook → problem → introduce smart alternative → introduce solution → proof/credibility → offer → CTA"; video önerileri; duygusal tetikleyiciler; bilişsel önyargılar; direct response teknikleri. Örnek açılar: "practical and financial concerns", "fear and anxiety" ("the invisible poison in your home air you're breathing right now"). İnsan shortlist yapar.

n8n akışı 3 — Senaryo üretimi:
1. Webhook (action = generate scripts) → Switch; Airtable'da script length seçilir (ör. "1 to 3 minutes")
2. Airtable Get Record
3. LLM chain (Gemini 2.5 Pro); değişkenler: angle + açı detayları + ürün bilgisi. Prompt'un açılışı (videoda görünen): "You [are an] elite video sales script writer" + "VSSL script structure" bölümü (tam metin şablonda).
4. Airtable Update: script
Örnek üretilen hook (aynen): "What if the biggest health threat to your family isn't outside but hiding right here in the air you're breathing inside your home right now?" → sonra "Lead problem agitation".

n8n akışı 4 — Senaryo revizyonu:
1. Webhook (action = feedback) → Switch
2. Get Record → LLM: kullanıcı geri bildirimi (ör. "please redo the hook. I didn't like the angle") + orijinal senaryo + ürün detayları → revize senaryo
3. Airtable Update (status "revision in progress" → güncel senaryo)

n8n akışı 5 — Kazanan reklamın senaryosunu çekme (Airtable'da "best ads" seçilince webhook):
1. Webhook → Airtable Get Record (video ID)
2. Facebook Graph API GET (video ID) → id, description, length, permalink
3. Set: "facebook.com" + permalink = tam Facebook URL
4. Scrapfly ile sayfayı scrape et (Facebook'tan doğrudan video/ses alınamıyor)
5. Code node: HTML'den indirilebilir video URL'ini ayıkla (kod ChatGPT'ye yazdırılmış)
6. IF: URL var mı (video reklam değilse yok)
7. HTTP Request: videoyu indir
8. OpenAI transcribe → transkript
9. Airtable: "Best performing video scripts" tablosuna yaz (faz 5); buradan yeni ürünlere bağlanır

n8n akışı 6 — Reklam performans analitiği:
1. Schedule Trigger (haftada 1; günlük de olabilir)
2. Graph API: business portföyündeki tüm ad account'lar → Split Out + Loop
3. Her hesap için kampanyalar → Loop → IF aktif mi
4. Aktif kampanyalar için ad set'ler → Split Out → IF aktif mi
5. Wait düğümü (Graph API rate limit için)
6. Ad content (reklamlar) → Split Out → filtre 1: aktif mi, filtre 2: video ID var mı (sadece video reklam)
7. Set/Map: kampanya adı, ad set, reklam, spend, ROAS, CPC, clicks, conversions, impressions (fields parametresinde istenen metrikler)
8. Airtable'a yaz (faz 4 tablosu)

Meta Graph API token alma adımları (aynen sırasıyla):
1. developers.facebook.com → My Apps → Facebook'a giriş
2. Create App → use cases: "Other" → "Business" → business portfolio / ad account seç (resmi işletme doğrulaması gerekmiyor, WhatsApp'tan kolay)
3. Marketing API → Setup → üç izni de seç → "Get token"
4. n8n'de Facebook Graph API düğümüne access token olarak gir

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: VSL video reklamlar (kameraya konuşan kişi, aynı senaryonun farklı hook/versiyon iterasyonları, "day one, day two" gibi seriler); birden çok ürün için.
- Senaryo uzunluğu seçeneği: örnekte "1 to 3 minutes".
- Reklam kurgusu: açı (angle) testi → kazanan açının senaryosunu yeni ürünlere taşıma (flywheel); kampanya → ad set → reklam hiyerarşisinde sadece aktif video reklamlar izleniyor.
- Ölçülen metrikler: spend, ROAS, CPC, clicks, conversions, impressions.
- Post sıklığı / organik: Videoda yok.
- Bütçe: rakam vermiyor.

## Hatalar / uyarılar
- Ücretsiz şablon linki (https://c.benai.co/hwaa-free → https://benai.kit.com/hwaam-free) Kit e-posta opt-in sayfası; HTTP 403 döndü, workflow JSON'u ve prompt'ların tam metni alınamadı. Prompt'lar transkriptte yalnızca kısmen okunuyor.
- Facebook Graph API kurulumu yeni başlayanlar için "a bit tricky"; rate limit'e takılmamak için Wait düğümü şart.
- Facebook reklam URL'i doğrudan transkripte verilemez; Scrapfly + kod ile gerçek video URL'i ayıklamak gerekiyor; video olmayan reklamlarda URL çıkmaz (IF kontrolü).
- Karmaşık JSON çıktılarda output parser'ı LLM düğümünün içine değil ayrı düğüme koy.
- Video üretimi (HeyGen) henüz otomatik değil; insan onayı (açı seçimi, senaryo inceleme) sistemin parçası.
- Demo verisi gerçek müşteri değil (gizlilik), performans analitiği akışı canlı gösterilmedi.

## Bizim ajansa alınacaklar (somut)
1. Airtable (veya ücretsiz alternatif olarak NocoDB/Google Sheet) + n8n ile "Reklam Masası": tablolar Müşteri → Ürün/Hizmet (ör. "Lahmacun menüsü", "İmplant tedavisi") → Açılar → Senaryolar → Reklam Performansı → Kazanan Senaryolar. Tek webhook + `action` parametresi + Switch mimarisini aynen kopyala.
2. Araştırma adımı: her yeni yerel müşteri için Perplexity Sonar ile "işletme + mahalle + rakipler + hedef müşteri profili" araştırması; çıktıyı müşteri kartına kaydet, tüm açı/senaryo üretiminde bağlam olarak kullan.
3. Açı üretimi prompt'u: "en az 5, en fazla 8 açı; her biri için concept, video yapısı (hook → problem → çözüm → kanıt → teklif → CTA), duygusal tetikleyici, bilişsel önyargı" — Türkçe yerel işletmeye uyarlanmış hali; müşteri panelinden "beğendiklerini işaretle" onay adımı (Airtable Interface müşteriye gösterilebilir, satışta etkileyici).
4. Haftalık Meta performans çekici (Graph API: ad account → kampanya → ad set → reklam, sadece aktif, Wait ile) → Airtable/Sheet; her müşteriye otomatik haftalık rapor. Reklam paketinin standart parçası yap.
5. Kazanan döngüsü: en iyi sonuç başı maliyetli (yerelde: mesaj/arama başı maliyet) reklamın senaryosunu "kazanan senaryo" olarak işaretle → aynı sektördeki diğer müşterilerin yeni reklamlarına şablon olarak besle (restoranlar arası, klinikler arası öğrenme).
6. Paket önerisi (bizim, videoda fiyat yok): "AI Reklam Yönetimi" aylık paket = araştırma + ayda 5-8 açı + 3-6 senaryo + haftalık otomatik rapor; video çekimi/HeyGen avatar videosu ek ücretli modül.
