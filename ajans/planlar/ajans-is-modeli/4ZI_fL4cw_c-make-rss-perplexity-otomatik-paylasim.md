# AI Social Media Automation: How to Post Daily Content 100% on Autopilot! — Solopreneur — 15 dk — https://www.youtube.com/watch?v=4ZI_fL4cw_c

## Tek paragraf: ne anlatıyor
Make.com üzerinde, hazır bir "blueprint" (senaryo dosyası) içe aktarılarak kurulan tam otomatik bir sosyal medya akışını adım adım gösteriyor. RSS.app ile seçilen konu/niş için bir haber akışı (Google News, web sitesi, Reddit, YouTube, Instagram vb.) oluşturuluyor; her yeni makale Perplexity ile özetleniyor, bir Router ile Facebook, Instagram ve X için ayrı ayrı ChatGPT ile platforma özel metin yazılıyor, Instagram için DALL-E 3 ile görsel üretiliyor ve hepsi otomatik paylaşılıyor. Senaryo 6 saatte bir (360 dk) çalışacak şekilde zamanlanıyor. Sonda Make'in "AI beta" asistanıyla yeni modül (ör. LinkedIn) ekletme ve hata ayıklama ipucu veriliyor. Video bir ajans iş modeli anlatmıyor; kendi hesabını/thought-leadership hesabını büyütmek isteyen solopreneur'a teknik eğitim.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
Videoda yok. (Kanal, açıklamada "Business Automation & Growth Services" danışmanlık sayfasına ve ücretsiz blueprint'e — e-posta karşılığı — yönlendiriyor; fiyat vermiyor.)

Videodaki maliyet rakamları:
- Perplexity API: "load like $5 honestly $5 will last you an eternity you can see I've only used 24 cents" — 5 USD yükle, kendisi 0,24 USD harcamış.
- OpenAI API: "you only need to load five or $10 here" — 5-10 USD.
- Make: ücretsiz hesapla başlanabiliyor (affiliate linkte 1 ay Pro bonusu).
- RSS.app: ücretsiz deneme (sonrası ücret rakamı vermiyor).

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
Videoda yok. Tek "müşteri" mantığı: düzenli, kaynaklı paylaşımla "that's what's going to establish you as a thought leader in your space" — kendi alanında otorite olma.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Make.com, RSS.app, Perplexity API, OpenAI (ChatGPT + DALL-E 3), Facebook Pages, Instagram (business), X (Twitter).

Kurulum:
1. Make hesabı aç → Scenarios → Create new scenario.
2. More → Import Blueprint → indirilen blueprint dosyasını seç (bütün modüller hazır gelir).
3. RSS.app'te New Feed → konu anahtar kelimesi yaz (örnekte "AI tools for entrepreneurs") veya Google News / web sitesi / Reddit / YouTube / Instagram'dan feed üret → XML URL'sini kopyala.
4. Make'te RSS modülüne XML URL'sini yapıştır → "Choose where to start" ile mevcut bir makaleyi seçip "Run once" ile test et.
5. Perplexity'de API sekmesinden 5 USD yükle, API key üret, Make bağlantısına yapıştır.
6. OpenAI platformunda billing'e 5-10 USD yükle, Organization ID + user API key al, ChatGPT modüllerini bağla.
7. Facebook ve Instagram modüllerinde "Add" → hesaba giriş (API key gerekmiyor).
8. Tüm modülleri net adlandır (ör. "LinkedIn post writer") — yanlış değişkeni seçmemek için.
9. Schedule setting → dakika cinsinden aralık (60 = saatte bir; örnekte 360 = 6 saatte bir) veya günlük → ON.

Akış (sıralı):
- Trigger: RSS.app "Watch RSS feed items" (yeni makale)
- → Perplexity "Create a completion" (makaleyi URL değişkeniyle özetle)
- → Router (3 kol)
  - Kol 1: ChatGPT "Facebook post writer" → Facebook Pages "Create a post"
  - Kol 2: ChatGPT "Instagram post writer" → OpenAI DALL-E 3 "Generate image" → Instagram "Create a photo post" (Photo URL = data image URL, Caption = ChatGPT çıktısı)
  - Kol 3: ChatGPT "X post writer" (280 karakter) → X "Create a post"

Prompt'lar (videoda okunduğu kadarıyla, tam metin blueprint'te):
- Perplexity: "You are an AI tools expert ... summarize this article [URL] ... be sure to include the URL for the actual tools mentioned" — makaleyi özetleyip geçen araçların linklerini de eklemesini istiyor; "AI tools expert" kısmı kendi sektörünle değiştirilmeli ("keto weight loss expert", "master accountant" örnekleri).
- ChatGPT (Facebook): "You're a social media expert your job is to write an engaging post for Facebook from the following article summary" + Perplexity çıktısı; karakter ve emoji kuralları ekli. Instagram için çok benzer, X için "the character limit is actually 280".
- DALL-E: "Create appropriate image for this Instagram post" + yazılan post + spesifikasyonlar; görselde metin olmaması kuralı var ("generative images in AI right now they don't do text well").
- Make AI beta asistanına: "add a LinkedIn module as well just like I have for Facebook insta and x" — yeni kolu kendisi kurar; "it gets you 80 90% of the way there".

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: niş haber/makale özeti → platforma göre yeniden yazılmış metin post; Instagram'da AI görsel + caption; X'te 280 karakterlik kısa post.
- Sıklık: girişte "three or four times a day" hedefi; örnek kurulum 6 saatte bir (günde 4 post/platform).
- Platformlar: Facebook, Instagram, X; LinkedIn/Pinterest/ek hesaplar Router'a eklenebilir.
- Reklam: Videoda yok.

## Hatalar / uyarılar
- Açıklamadaki blueprint linki (https://solopreneur2020.kartra.com/page/automated-social-blueprint) e-posta opt-in formunun arkasında; doğrudan indirme linki yok, blueprint dosyası ve tam prompt metinleri kaydedilemedi.
- Her modülü ayrı test et; "Choose where to start" kullanmazsan senaryo yeni makale gelene kadar bekler.
- AI görsellerde metin bozuk çıkabildiği için görsel prompt'una "metin ekleme" kuralı konmuş.
- Make AI asistanı mükemmel değil, kurulumdan sonra modülleri elle bağlayıp test etmek gerekiyor.
- Riskler (videoda değinilmiyor, bizim not): haber metnini otomatik yeniden yazıp onaysız paylaşmak marka/hukuk riski taşır; "100% autopilot" yerel işletme hesabında onay adımı olmadan uygun değil.

## Bizim ajansa alınacaklar (somut)
1. Make'te "RSS → özet → Router → platform başına yazar" iskeletini Türkçe kuralım: kaynak olarak Google News TR araması (ör. "diş sağlığı", "İzmir restoran") + müşteri sektör blogları; Perplexity yerine maliyet için tek bir LLM özet modülü. Kurulum maliyeti aylık API olarak ~5-10 USD bandında kalır (videodaki rakam).
2. Otomatik yayın yerine "taslak + onay" kolu ekleyelim: Router çıkışları önce Google Sheet / WhatsApp onayına düşsün, işletme sahibi "OK" deyince paylaşılsın; tam otopilotu yalnız sektör-bilgi hesaplarında kullanalım.
3. Klinikler için "haftalık sağlık bilgisi" paketi: RSS (Sağlık Bakanlığı duyuruları, sektör haberleri) → Türkçe sade özet → Instagram + Facebook + Google İşletme profili postu; görsel prompt'una "metin yok, marka renkleri" kuralı.
4. Platform başına ayrı prompt kuralını standartlaştıralım (FB uzun + emoji, IG caption + hashtag, X/Threads ≤280 karakter, LinkedIn profesyonel) ve modülleri "IG yazar / FB yazar" diye adlandırma disiplinini şablona koyalım.
5. Make AI asistanını yeni müşteri kolu eklerken (LinkedIn, Pinterest) hızlandırıcı olarak kullanalım; ama teslimden önce her kolu "Run once" ile test eden kontrol listesi tutalım.
