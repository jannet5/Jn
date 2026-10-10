# How to Automate Social Media Posts with AI | Save Time & Boost Engagement! — Website Learners — 19 dk — https://www.youtube.com/watch?v=00obLp8vowQ

## Tek paragraf: ne anlatıyor
Make.com üzerinde, Google Sheet'ten yönetilen iki aşamalı (önce "Create & Post", sonra "Publish") bir sosyal medya otomasyonu kuruluyor. Sheet'te üç tür girdi var: düz metin, makale linki ve görsel URL'si; tek tek ya da kombinasyon hâlinde verilebiliyor. "Create & Post" seçilince Make; metni ChatGPT ile, linki Perplexity ile, görseli GPT vision ile özetliyor, üç özeti birleştiriyor, ardından Facebook, LinkedIn ve Instagram için ayrı metinler yazıp Sheet'e geri yazıyor; görsel verilmediyse Leonardo AI ile 1080x1080 tipografik görsel üretiyor. Kullanıcı metinleri Sheet'te inceleyip düzeltiyor, "Publish" seçince üç platforma yayınlanıyor ve post linkleri Sheet'e dönüyor. Hazır blueprint ve Sheet şablonu açıklamada ücretsiz veriliyor; video Make + GravityWrite affiliate'leriyle para kazanıyor (GravityWrite'ın "AI social media post creator" ürünü "coming soon" olarak tanıtılıyor).

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Video ajans satışı anlatmıyor; hedef kitle "business owner, content creator" — kendi hesabını otomatikleştirmek isteyenler.
- Yaratıcının geliri: Make affiliate linki, GravityWrite ("Get 1 Year of GravityWrite at $97" — USD), Webspacekit, Grammarly vb. kuponlar; ayrıca açıklamada "Want your website developed by us? Email us…" (web sitesi yapım hizmeti).
- Kurulum maliyeti (videodan, USD): OpenAI minimum kredi "$5 which will give you 2 million tokens"; Perplexity minimum "$3"; Leonardo AI için "subscribe to API plan" (rakam vermiyor); Make "get started for free".
- Müşteriye satış fiyatı / paket: rakam vermiyor.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
Videoda yok. (Tek satış cümlesi izleyiciye: "you don't need to write content from stats and you don't need to log into each platform to post manually every single time" — her platforma tek tek girme derdini bitirme vaadi.)

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Make.com, Google Sheets + "Make for Google Sheets" eklentisi (webhook), OpenAI (gpt-4o, gpt-4-vision-preview), Perplexity API (llama-3-sonar-large-32k-online), Leonardo AI API, Facebook Pages, Instagram for Business (Facebook işletme sayfasına bağlı olmalı), LinkedIn.
Blueprint ve tüm prompt'lar: `ajans/arastirma/youtube/kaynaklar/00obLp8vowQ-make-blueprint.md`; Sheet şablonu: `ajans/arastirma/youtube/kaynaklar/00obLp8vowQ-website-learners-make-sheet.md`.

Sheet sütunları: `Text | Link | Image Url | Action button | Progress | Facebook content | Linkedin content | Insta content | Image url | Post Action` (+ K/L/M: post linkleri).

Kurulum sırası (videodaki 6 parça):
1. Make hesabı aç.
2. Blueprint'i indir → Create new scenario → üç nokta → Import blueprint → Save.
3. Sheet'i kopyala (File → Make a copy) → Extensions → Add-ons → "Make" eklentisini kur → Make for Google Sheets → Settings → Webhook alanına Make'te oluşturulan webhook URL'sini yapıştır → tüm Google Sheets modüllerinde Google hesabını seç.
4. API anahtarlarını al: OpenAI ($5 kredi), Perplexity ($3 kredi), Leonardo (API planı).
5. Anahtarları Make modüllerine bağla (bir kere bağlantı oluştur, diğer ChatGPT modüllerinde aynı bağlantıyı seç).
6. Facebook sayfası, Instagram işletme hesabı (Facebook'a bağlı), LinkedIn bağla → Save → senaryoyu aktif et.

Akış (blueprint'ten, sıralı):
1. Trigger: Google Sheets → Watch Updated Cells (eklenti webhook'u) → Get Sheet Content.
2. Router — Kol A "Content Generation" (filtre: Action button = "Create & Post" ve FB/LinkedIn/Insta hücreleri boş):
   1. Progress hücresi = "Processing🔃".
   2. Alt router: Text varsa → gpt-4o özet; Link varsa → Perplexity özet; Image Url varsa → GPT vision açıklama; her biri değişkene yazılır.
   3. Aggregator → değişkenleri al → gpt-4o "combine these data and summarize".
   4. Router: Facebook / LinkedIn / Instagram metinleri (gpt-4o) → ilgili sütunlara yaz.
   5. Görsel yoksa: gpt-4o ~35 karakterlik başlık → Leonardo AI tipografik görsel → Image url sütunu; Progress = "Completed✅".
3. Router — Kol B "Post Content" (filtre: Post Action = "Publish" ve üç metin dolu):
   1. Facebook Pages → Create Post With Photos → Get Post → permalink Sheet'e.
   2. Instagram for Business → Create Photo Post → Get Media → permalink Sheet'e.
   3. HTTP Get File → LinkedIn Share Image → embed linki Sheet'e.

Prompt'lar (blueprint'ten AYNEN):
```
Analyze the text content {{2.`0`}} and summarize it 
```
Düz metin girdisini özetliyor.
```
{{2.`1`}} Analyze this URL and summarize the entire content with key points and the main context. 
```
Perplexity ile linkteki makaleyi okuyup ana noktaları çıkarıyor.
```
Analyze the image to understand its content and context.Provide a detailed description and summary of the image. {{2.`2`}}
```
Görsel URL'sini GPT vision ile tarif ediyor.
```
{{22.`text output`}}{{22.`URL output`}}{{22.`Image output`}}combine these data and summarzie completely
```
Üç kaynağın özetini tek özete indiriyor.
```
Based on this following summary"{{23.result}}", 

create a Facebook post as a digital marketing specialist. The content should be engaging, relatable, and visually appealing, with an inspirational message. Use emojis and relevant hashtags. Do not any image placeholders.

 If a URL is provided here: '{{74.`1`}}', then include it at the end of the post like this: "If you want to know more about this, follow this link: <URL>. 
If URL is not provided above, then do not write anything about URL.
```
Facebook metni: ilham verici ton, emoji + hashtag, link varsa sona ekle.
```
Based on this following summary "{{23.result}}",

create a LinkedIn post as a digital marketing specialist. The content should be professional, insightful, and thought-provoking, with a focus on delivering value and sparking conversation. Use relevant hashtags. Do not include any image placeholders.

If a URL is provided here: '{{74.`1`}}', then include it at the end of the post like this: 'To learn more, follow this link: <URL>'.

If a URL is not provided above, then do not write anything about a URL
```
LinkedIn metni: profesyonel, tartışma başlatan ton.
```
Based on this following summary "{{23.result}}",

Create an  catchy Instagram post as a digital marketing specialist. The content should be visually engaging, relatable, and inspiring, Use emojis and relevant hashtags. Do not include any image placeholders.

If a URL is provided here: '{{74.`1`}}', then include it at the end of the post like this: 'Want to know more? Check out this link: <URL>'.

If a URL is not provided above, then do not write anything about a URL.
```
Instagram caption'ı: akılda kalıcı, emoji + hashtag.
```
{{23.result}}Extract a catchy title around 35 characters from this summary, perfect for a social media post image.
```
Görselin üstüne yazılacak ~35 karakterlik başlığı çıkarıyor.
```
Create a typography image with this title '{{60.result}}' in bold, 3D, modern metallic, and energetic fonts. Ensure the text is clear . Use a vibrant and dynamic color palette that stands out. The text should have a pronounced 3D effect with depth and shadow, making it pop off the screen. Surround the text with minimal dynamic, abstract geometric shapes, lines, and energy beams to convey motion and excitement. The background should feature a modern pattern to enhance the futuristic feel. The overall composition should be balanced and engaging, with a sleek, modern aesthetic. Set the image dimensions to 1080x1080 pixels, perfect for social media sharing.
```
Leonardo AI'ya 1080x1080 3D tipografik "başlık görseli" ürettiriyor.

Videodaki testler: (1) "what jobs AI will replace" makale linki, (2) görsel URL'si, (3) düz metin — üçü de üretilip yayınlandı.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: tek görsel + caption (Facebook, Instagram, LinkedIn). Görsel ya kullanıcının verdiği fotoğraf ya da AI tipografik başlık görseli.
- Sıklık: Sheet'e her satır eklenip "Create & Post" seçildikçe; zamanlama yok (zamanlamayı ileride çıkacak GravityWrite aracı vaat ediyor). Rakam vermiyor.
- Reklam kurgusu / bütçe: Videoda yok.

## Hatalar / uyarılar
- İnsan onayı adımı var (Create & Post → incele/düzenle → Publish) — müşteri hesapları için doğru tasarım; "Run once" ile doğrudan yayın yok.
- Instagram için işletme hesabı + Facebook işletme sayfasına bağlantı şart; Facebook için business page şart.
- Blueprint'teki modeller eskimiş (gpt-4-vision-preview, llama-3-sonar-large-32k-online) ve spreadsheetId'ler yaratıcının kendi tablolarına bağlı — import sonrası değiştirilmeli.
- Prompt'larda yazım hataları var ("summarzie", "Do not any image placeholders") — kendi sürümümüzde düzeltilmeli.
- Leonardo AI görselleri hep aynı "3D metalik yazı" stilinde; yerel işletmede gerçek ürün/mekân fotoğrafı daha inandırıcı.
- Mevcut kaynak dosyası `00obLp8vowQ-website-learners-make-sheet.md` içinde sütun adları boş görünüyor (kayıt sırasında kaybolmuş); doğru sütunlar yeni `00obLp8vowQ-make-blueprint.md` dosyasında.
- Video tutorial + affiliate odaklı; müşteri bulma ve fiyatlandırma bilgisi yok.

## Bizim ajansa alınacaklar (somut)
1. **"Onaylı yayın tablosu" standardı:** Her müşteri için bu Sheet yapısını Türkçe sütunlarla kuralım (`Metin | Link | Fotoğraf URL | Üret | Durum | Facebook | Instagram | Google İşletme | Görsel | Yayınla | Linkler`). Müşteri (ör. restoran sahibi) sadece fotoğraf + 1 cümle bırakır, biz/müşteri onaylar, "Yayınla" ile çıkar. Onay adımı satışta "hiçbir şey sizin onayınız olmadan paylaşılmaz" cümlesine dönüşür.
2. **Üç girdili mantık = yerel işletme için ideal:** Fotoğraf URL'si (yemek, klinik öncesi/sonrası, vitrin) → vision modeli tarif etsin → Türkçe caption. Prompt'ları Türkçe ve sektöre göre yeniden yazalım (ör. "Bir İstanbul restoranının sosyal medya uzmanı olarak, samimi, iştah açıcı, 2-3 emoji, 5 yerel hashtag (#kadıköy…) ile Instagram metni yaz; adres ve rezervasyon telefonunu sona ekle").
3. **LinkedIn yerine Google İşletme Profili kolu:** Yerel işletmede LinkedIn değersiz; üçüncü kolu Google Business Profile gönderisine (veya WhatsApp Kanal/Status metnine) çevirelim.
4. **Leonardo yerine marka şablonu:** AI tipografik görsel sadece "duyuru/kampanya" postlarında (ör. "Bu hafta %20 indirim"), logolu sabit şablonla; diğer postlarda müşterinin gerçek fotoğrafı.
5. **Paket önerisi (bizim fiyat, videodan değil):** "Sosyal Medya Otopilot — Başlangıç": ayda 12 post, 2 platform, onaylı tablo, aylık API maliyeti bize ~5 USD seviyesinde (OpenAI min. kredi videoda "$5 → 2 million tokens") — müşteriye aylık sabit ücret + kurulum; web sitesi/uygulama müşterilerine upsell olarak sunulur.
