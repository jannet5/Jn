# I Built a Viral Instagram Carousel Machine (n8n + Nanobanana + Blotato) — Automate with Marc | AI Automation — 19 dk — https://www.youtube.com/watch?v=id22R7iBTjo

## Tek paragraf: ne anlatıyor
Marc, tek bir konu cümlesinden (chat mesajı) 5 slaytlık Instagram carousel'i otomatik üretip Instagram'a yayınlayan bir n8n akışını canlı kuruyor. Tez: "The real trick to going viral on Instagram right now isn't reels, it's carousels" — carousel'ler durdurur, kaydırtır, kaydettirir ve erişim getirir. Akış: Chat Trigger → GPT-5 ajanı (5 görsel prompt'u, yapılandırılmış JSON) → Split Out → Nano Banana (WaveSpeed API üzerinden, metin yazılı görsel) → Wait → sonucu çek → Blotato'ya medya yükle → Code ile URL'leri tek diziye topla → Google Sheets log → GPT-5 ile caption → Merge → Blotato ile Instagram'a carousel post. Görsellerin üstündeki yazı (text overlay) ayrı bir tasarım adımıyla değil, doğrudan Nano Banana prompt'unun içinde üretiliyor. Şablon n8n.io'da ücretsiz (ID 8877); tam JSON `ajans/arastirma/youtube/kaynaklar/id22R7iBTjo-n8n-carousel-sablonu-8877.md` dosyasına kaydedildi.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Video bir hizmet satmıyor; hedef kitle "thought leader, influencer, or a business trying to create consistent, high impact content". Demo senaryosu: podcast koçu hesabı.
- Gelir: Blotato affiliate linki (kupon **MARC20 = 3 ay %20 indirim**), n8n partner linki, Buy Me a Coffee. Hizmet fiyatı: rakam vermiyor.
- Blotato "paid tool"; API anahtarı için ücretli hesap şart (fiyat rakamı vermiyor).

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
Videoda yok.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: n8n, OpenAI GPT-5 (ajan + caption), WaveSpeed.ai (Nano Banana text-to-image API), Blotato (community node; medya barındırma + Instagram/TikTok/LinkedIn/Facebook/YouTube'a yayın), Google Sheets (log).

### Carousel tasarım kuralları (prompt'tan)
- Slayt sayısı: **5** (sabit; değiştirmek için Split döngüsü/prompt sayısı).
- Yapı: **Hook → Problem → Insight → Solution → CTA** ("Viral 5-Framework").
- Hook: "Big, bold, curiosity-driven opener." Örnek kapak: "Stop losing listeners in the first 60 seconds. Five podcast best practices to grow your show fast." + kaydırma oku.
- Her slayt: Header + Subheader metni; görsel prompt'unda "text space / blank space for text overlay" isteniyor.
- Renk/stil örnekleri prompt içinde: "dark background with neon accent", "cartoon style", "Infographic-style… colorful and clean", "sleek tech aesthetic", "Bright clean design… bold colors". Font adı: vermiyor.
- Format: PNG (`output_format=png`). Boyut/oran: vermiyor.

### n8n akışı (düğüm düğüm — şablon JSON + video)
1. **When chat message received** (`chatTrigger`) — örnek girdi: "create an IG carousel of five images around the topic of best practices for podcast". Alternatif: Schedule Trigger, Telegram, Slack.
2. **Image Prompt Generator** (`agent` v2.2) — Text: `{{ $json['IG Idea'] }}` (videoda "connected chat trigger node"), "Require Specific Output Format" AÇIK.
   - Alt düğüm **OpenAI Chat Model**: `gpt-5`.
   - Alt düğüm **Structured Output Parser**, JSON örneği:
     ```
     { "prompts": [ "{{prompt1}}", "{{prompt2}}", "{{prompt3}}", "{{prompt4}}", "{{prompt5}}" ] }
     ```
   - System message (aynen, kısaltılmış baş + kurallar; tamamı kaynak dosyada):
     ```
     You are an expert Viral Instagram Carousel creator. Your role is to create carousel image prompt for Nano Banana. You'll receive a user prompt to give you the topic/ideas for the carousel.

     Here's the framework that you should use in coming up with the content:
     Viral 5-Framework Carousel (Automation Demo)
     Slide 1 — Hook (Stop the Scroll)
     Big, bold, curiosity-driven opener.
     👉 "Stop wasting hours making Instagram posts 👇"
     Slide 2 — Problem (Pain Point)
     Call out the frustration that everyone feels.
     Slide 3 — Insight (The A-ha Moment)
     Drop the truth bomb about why carousels matter.
     Slide 4 — Solution (Your Workflow Demo)
     Show your automation stack clearly.
     Slide 5 — CTA (Engagement Driver)
     End with a strong action.
     👉 "Save this workflow. Share it with a friend.
     And start going viral without the grind."

     ##Rules
     Return the 5 prompts as a JSON object with a field called 'prompts' that contains an array of the prompt strings.
     ```
     Türkçe: 5 slaytlık çerçeveye göre her slayt için Nano Banana prompt'u + üst/alt başlık metni üretmesini, sonucu `prompts` dizisi olarak döndürmesini istiyor.
   - Prompt içindeki örnek görsel prompt'ları (aynen):
     ```
     Image 1: "Minimalist bold Instagram carousel cover, dark background with neon accent, eye-catching text space, viral growth theme."
     Header: Stop Wasting Hours on IG Posts / Subheader: Here's how to go viral without the grind 🚀
     Image 2: "Frustrated person at laptop surrounded by messy drafts and notes, cartoon style, editable blank space for text overlay."
     Image 3: "Infographic-style design showing upward trending arrow with Instagram logo, colorful and clean, strong highlight space for text."
     Image 4: "Automation workflow concept — arrows connecting AI brain → workflow nodes → Instagram app logo, sleek tech aesthetic."
     Image 5: "Bright clean design with Instagram 'save' and 'share' icons, motivational style, bold colors, clear CTA space."
     ```
     Türkçe: her slayt için sahne + stil + "metin alanı bırak" kalıbı.
3. **Split Out** — Field: `output.prompts` → 1 öğe girer, 5 öğe çıkar (Nano Banana tek seferde tek prompt alıyor).
4. **Nano Banana** (HTTP Request, POST) — `https://api.wavespeed.ai/api/v3/google/nano-banana/text-to-image`; WaveSpeed sitesindeki cURL "Import cURL" ile yapıştırılıyor; Auth: Generic → Header Auth (`Authorization: Bearer <API_KEY>`), "Send Headers" kapalı. Body: `enable_base64_output=false`, `enable_sync_mode=false`, `output_format=png`, `prompt={{ $json['output.prompts'] }}`. 5 API çağrısı yapar.
5. **Wait For Render** — videoda **30 sn**, şablonda **60 sn**. Üretim için: başarısızsa Nano Banana'ya geri dönen döngü (retry loop) kurulmalı (videoda atlandı).
6. **Get Nano Banana Image Result** (HTTP GET) — `https://api.wavespeed.ai/api/v3/predictions/{{ $json.data.id }}/result`, aynı header auth. (Videodaki hata: request ID'yi yapıştırmayı unutmak.)
7. **Upload media** (Blotato node, Resource: Media, Operation: Upload) — Media URL = önceki çıktı `outputs` URL'si. Neden: Instagram rastgele URL'leri (WaveSpeed, Google Drive) reddedebiliyor; Blotato "whitelisted".
8. **Code** — 5 öğeyi tek diziye toplar (aynen):
   ```js
   // Combine all 'url' fields from incoming items into a single array
   return [
     {
       json: {
         urls: items.map(item => item.json.url)
       }
     }
   ];
   ```
9. **Append row in sheet** (Google Sheets) — sayfa "IG carousel generated image log"; sütunlar: Image URL (= `urls` dizisi), Timestamp (= `{{ $now }}`). URL'ler kalıcı değil, sadece referans.
10. **Caption Generator** (OpenAI Message a Model, `gpt-5`) — Image Prompt Generator'a doğrudan ikinci dal olarak bağlanıyor (öğe sayısı 1→5→1 değiştiği için "can't determine which item" hatası çıktı; mimari değiştirildi). User mesajı:
    ```
    Prompt 1: {{ $json.output.prompts[0] }}
    Prompt 2: {{ $json.output.prompts[1] }}
    Prompt 3: {{ $json.output.prompts[2] }}
    Prompt 4: {{ $json.output.prompts[3] }}
    Prompt 5: {{ $json.output.prompts[4] }}
    ```
    System mesajı (aynen):
    ```
    You are an expert Instagram Caption Agent. You will receive a set of prompts that were used to generate Instagram carousel images. Based on these prompts, craft a compelling caption that is:

    High-Hook: Start with a strong, attention-grabbing opening line.
    SEO-Optimized: Naturally include relevant keywords that increase discoverability.
    Engaging: Encourage saves, shares, and comments through questions, CTAs, or relatable phrasing.
    Platform-Ready: Use a concise, mobile-friendly writing style with line breaks for readability.
    Hashtag-Savvy: Include a well-balanced mix of trending and niche hashtags (without overstuffing).

    The final caption should feel natural, authentic, and designed to maximize reach and engagement on Instagram.

    ##Rules
    Output only post-ready Instagram Caption without any explanation.
    ```
    Türkçe: 5 görsel prompt'una bakıp kancalı, SEO'lu, hashtag'li, açıklamasız tek caption yazdırıyor.
11. **Merge Caption + Images** — Mode: Combine, "All Possible Combinations" (`combineAll`); giriş 0 = Sheets dalı (urls), giriş 1 = caption.
12. **Post to Instagram** (Blotato, Create Post) — Account: bağlı IG hesabı; Text = caption `content`; Media URLs = `urls` dizisi.
- Öneri: Post'tan önce **human-in-the-loop** onay (şablon açıklaması: Slack approval loop).

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: 5 slaytlık eğitici carousel (Hook/Problem/Insight/Solution/CTA). Gerekçe: carousel'ler "stop, swipe, and save" → erişim.
- Sıklık: Schedule Trigger ile "specific time of the day or of the week"; şablon "weekly automation" öneriyor. Rakam vermiyor.
- Reklam/bütçe: Videoda yok.

## Hatalar / uyarılar
- Sonuç "80% there": Nano Banana bazı slayt metinlerini istenenden farklı yazdı → yayından önce insan onayı şart.
- n8n öğe eşleştirme hatası ("can't determine which item to use") — öğe sayısı değişen akışlarda caption dalını ayrı kol olarak kur, Merge ile birleştir.
- Wait sabit süre; render bitmezse akış kırılır → retry döngüsü gerekli.
- Log'daki görsel URL'leri geçici.
- Blotato ücretli; API anahtarı sadece ücretli planda.
- Yayınlanan şablon JSON'unda `Get Nano Banana Image Result → Upload media → Code` ve `Merge → Post to Instagram` bağlantıları ile Blotato düğüm parametreleri boş geliyor; içe aktarınca elle bağlanmalı.
- WebFetch n8n.io sayfasını özetle döndürdü; tam içerik `api.n8n.io/api/templates/workflows/8877` JSON'undan alındı.

## Bizim ajansa alınacaklar (somut)
1. **Şablon 8877'yi içe aktar, Türkçeleştir**: system prompt'a "Tüm slayt metinleri Türkçe, Türkçe karakterleri (ç, ğ, ı, ö, ş, ü) doğru yaz, işletme adı: …, marka renkleri: …" ekle; Chat Trigger yerine Google Sheets/Schedule Trigger (her müşteri için bir satır = bir konu).
2. **Sektöre özel 5 slayt çerçevesi**: restoran ("Bu hafta menüde ne var?" hook → sorun → şefin önerisi → kampanya → "Rezervasyon için DM"), klinik ("Diş eti kanaması normal değil" hook → … → "Ücretsiz ön muayene için ara"). Hook/Problem/Insight/Solution/CTA yapısını aynen koru.
3. **Blotato yerine resmi Graph API veya Postiz** (müşteri başına aylık maliyeti düşürmek için; bkz. t63IlcH1GJY planı) — ama Nano Banana URL'leri önce kendi depolamamıza (Cloudinary/R2) yüklenmeli.
4. **Onay adımı zorunlu**: Merge'den sonra WhatsApp/Telegram'a 5 görsel + caption gönder, müşteri "Onay" yazınca yayınla (Türkçe metin hataları için).
5. **Paket**: "Haftada 2 carousel + caption + otomatik paylaşım" — aylık abonelik paketinin bir kalemi olarak sat; fiyatı bizim fiyat listemizden belirle (videoda rakam yok).
