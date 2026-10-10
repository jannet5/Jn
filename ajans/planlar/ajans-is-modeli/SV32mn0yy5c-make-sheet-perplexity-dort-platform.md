# This Social Media AI Automation Creates Unique Content Daily! (100% Automated!) — AI Andy — 17 dk — https://www.youtube.com/watch?v=SV32mn0yy5c

## Tek paragraf: ne anlatıyor
Make.com üzerinde tek bir senaryo: Google Sheet'e yapıştırılan bir haber linki → Perplexity (llama-3.1-sonar-small-128k-online) linki başlıklı bir özete çeviriyor → Router dört kola ayırıyor; her kolda platforma özel bir OpenAI Assistant (Tweet / LinkedIn / Facebook / Instagram "Oasis Bot") özeti o platformun diline çeviriyor → X ve LinkedIn'e doğrudan metin gidiyor, Instagram ve Facebook için DALL-E 3 ile görsel üretilip görsel+caption yayınlanıyor. Video Make sponsorlu; blueprint, Sheet ve dört prompt Notion sayfasında ücretsiz veriliyor (bkz. kaynaklar). Ana mesaj: "uzun prompt daha iyi değil; kısa prompt + platform tonu" ve "ChatGPT'nin tek bir metni dört platforma uyarlaması".

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Video kendi hesapları (AI Andy) için kurulum anlatıyor; ajans satışı yok.
- Yaratıcının gelir modeli: Make sponsorluğu (affiliate: "Pro Plan 1 month for free"), Skool topluluğu bekleme listesi ("tech support for AI Automation and talk to me directly"), Scrimba affiliate, e-posta listesi (tools/prompts lead magnet).
- Maliyet rakamları (videodan): Perplexity API'ye "$5" kredi yükledi; OpenAI API için "add some credits… incredibly cheap" (rakam vermiyor). Make "free forever platform".
- Paketleme fiyatı: rakam vermiyor.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Videoda yok (müşteri bulma anlatılmıyor). Tek "satış cümlesi" kendi izleyicisine: "you don't need to write content from scratch, you don't need to log into each platform and post yourself".

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Make.com, Google Sheets (tek sütun "URL", sheet adı "Ideas", "table contains headers: yes", "rows limit: 1"), Perplexity API, OpenAI Assistants (Playground'da test), DALL-E 3, X, LinkedIn, Instagram for Business (Meta Business Suite gerekir), Facebook Pages.

Akış (blueprint ile birebir, kaynak: `ajans/arastirma/youtube/kaynaklar/SV32mn0yy5c-ai-andy-make-blueprint.md`):
1. Trigger: **Google Sheets → Watch Rows** (yeni satırdaki makale URL'si).
2. **Perplexity AI → Create a Chat Completion**: model `llama-3.1-sonar-small-128k-online` ("fiyatı çok daha düşük ve iyi çalışıyor"), role user, max_tokens 50000, temperature 1. Prompt (videodan): `summarize this and start your output with a headline` — başlıkla başlatma, sonraki ChatGPT adımlarını kolaylaştırıyor.
3. **Router** → 4 kol:
   - Kol X: OpenAI → Message an Assistant ("Tweet Oasis Bot", message = `{{2.choices[].message.content}}`) → **Twitter → Create a Tweet** (text = assistant sonucu).
   - Kol LinkedIn: OpenAI → Message an Assistant ("LinkedIn Oasis Bot") → **LinkedIn → Create Post** (visibility PUBLIC, MAIN_FEED).
   - Kol Instagram: OpenAI → Message an Assistant ("Instagram Oasis Bot") → **OpenAI → Generate Image** (dall-e-3, 1024x1024, vivid, standard, prompt: `Make an image based on this text: {{7.result}}`) → **Instagram for Business → Create a Photo Post** (image_url = DALL-E URL, caption = assistant sonucu).
   - Kol Facebook: OpenAI → Message an Assistant ("Facebook Oasis Bot") → OpenAI → Generate Image (aynı ayarlar) → **Facebook Pages → Create a Post** (message = assistant sonucu, link = görsel URL).
4. Çalıştırma: "Run once" → dört platforma aynı anda gönderi (videoda canlı test: Waymo haberi ve "salmon farm AI vision" haberi).

Assistant talimatları (Notion kaynağından AYNEN; `SV32mn0yy5c-ai-andy-notion-promptlar.md`):
```
Tweet Oasis Bot:
Make a tweet from this text. Pull out a problem, a fact, a benefit, the tone should be like you're having a thought and you're just writing what you're thinking no emojis, no hashtags: Make it a coherent paragraph of text but keep it short

Linkedin Oasis Bot:
Make a Linkedin post from this text. Pull out a problem, a fact, a benefit, the tone should be professional and informational no emojis, no hashtags. Start with a compelling headline, Tailor the content to industry relevance, providing insights or a professional takeaway and end with engaging questions to get engagement. make it one paragraph but keep it short.

Facebook Oasis Bot:
Make a facebook post from this text. Pull out a problem, a fact, a benefit, the tone should be like you're having a thought and you're just writing what you're thinking. Start with a captivating emotional hook to reel the audience in with the first sentence, then Write a captivating caption that conveys the story succinctly, using emotive language to connect emotionally no emojis, no hashtags: Make it a coherent paragraph of text but keep it short.

Instagram Oasis Bot:
Make a tweet from this text. Pull out a problem, a fact, a benefit, the tone should be like you're having a thought and you're just writing what you're thinking no emojis, no hashtags: Make it a coherent paragraph of text but keep it short, ask for engagement at the end
```
Türkçe: Her prompt "problem + gerçek + fayda çıkar" iskeletini kullanıyor; sadece ton ve kapanış platforma göre değişiyor. Instagram prompt'u Tweet prompt'unun sonuna "ask for engagement at the end" eklenmiş hâli.

Prompt ipuçları (videodan):
- "the tone should be like you're having a thought and you're just writing what you're thinking" cümlesi tweet çıktısını "casual but insightful" yapıyor; ~10 kez denemiş.
- Tek kelime değişikliği çıktıyı uzatıyor: "make it one paragraph" → kısa; "put a line break between every second sentence but keep it short" → belirgin uzun.
- Prompt'u "10x better prompt" tarzı bir prompt-iyileştiriciden geçirince emoji/hashtag dolu, abartılı ("Future of parenting! Say hello to Waymo's autonomous ride for kids") çıktı veriyor — otomasyona sokması zor.
- Bağlantı için OpenAI'da "API key + Organization ID" gerekiyor; Make'te mapping alanlarını görebilmek için senaryoyu bir kez çalıştırıp çıktı balonunu üretmek gerekiyor.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: niş haber (AI haberleri) → yorum tarzı metin; Instagram/Facebook'ta DALL-E 3 görsel.
- Sıklık: Sheet'e link eklendikçe (rows limit 1 → her çalıştırmada 1 haber → 4 gönderi). Günlük zamanlama videoda anlatılmıyor.
- Reklam: Videoda yok. Bütçe: rakam vermiyor (API kredileri dışında).

## Hatalar / uyarılar
- "Massive prompting mistake": uzun prompt ve çok örnek → ChatGPT örneklerden alakasız parçaları gönderiye taşıyor (halüsinasyon), çıktı uzuyor. Kısa prompt öneriliyor.
- ChatGPT "no emojis, no hashtags" talimatına rağmen bazen emoji/hashtag koyuyor.
- X'e görsel eklemek anlatılmıyor ("a little bit too advanced for this video").
- Instagram için Meta Business Suite hesabı şart.
- Onay adımı yok: "Run once" doğrudan dört platforma yayınlıyor — müşteri hesabında riskli.
- DALL-E görseli haber metninden türetildiği için marka görseli değil, stok-benzeri "AI görsel" (ör. "crazy salmon farm image").
- Blueprint'teki assistant ID'leri ve sayfa/hesap ID'leri Andy'ye ait; kendi assistant'larımızı oluşturup yeniden seçmek gerekiyor.
- Kaynaklar daha önce çekilmişti; Google Sheet şablonu tek sütun (URL). Skool/affiliate linkleri atlandı.

## Bizim ajansa alınacaklar (somut)
1. **"Haber linki → 4 platform" hattını n8n'de standart ürün yap**: Google Sheet/Airtable satır → Perplexity özet ("summarize this and start your output with a headline") → platform başına ayrı LLM düğümü → Telegram onay → Blotato/Meta API ile yayın. Müşteriye "Haftada 3 sektör haberi, 4 platformda 12 gönderi" paketi olarak sat.
2. **Dört Oasis prompt'unu Türkçe'ye uyarlayıp prompt kütüphanesine koy**; iskelet aynı kalsın: "problem + gerçek + fayda", "düşünce yazar gibi ton", "emoji/hashtag yok", "kısa tut". Restoran/klinik için sektör haberi yerine "müşteri sorusu / sezon / kampanya" girdisi kullan.
3. **Kısa prompt kuralı**: ajans prompt'larında örnek gönderi sayısını 3 ile sınırla; müşteri sesini uzun örnek yığını yerine 5-6 sıfat + 2 yasak listesiyle tanımla (Andy'nin halüsinasyon uyarısı).
4. **Görsel üretimini DALL-E yerine şablon+AI ile değiştir**: haberden türetilmiş rastgele görsel yerine müşteri marka şablonuna (renk/logo) oturan Nano Banana/Flux görseli; aksi hâlde yerel işletme gönderisi "AI stok" görünür.
5. **Maliyet argümanı**: API tarafı aylık birkaç dolar (Perplexity'ye 5 $ kredi örneği) → "aylık içerik bütçenizin büyük kısmı emek değil sistem" cümlesini teklif sunumuna ekle.
