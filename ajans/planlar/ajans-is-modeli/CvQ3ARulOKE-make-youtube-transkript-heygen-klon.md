# NEW AI Social Media Automation will save you HOURS *Fully Automated* — Dan Kieft — 14 dk — https://www.youtube.com/watch?v=CvQ3ARulOKE

## Tek paragraf: ne anlatıyor
Make.com ile tek bir YouTube video linkinden Instagram (AI görsel + caption), LinkedIn, X postları ve HeyGen AI klonu ile dikey YouTube Short üreten ve yayınlayan bir otomasyonu, hazır blueprint + Google Doc kurulum rehberi üzerinden adım adım gösteriyor. Kaynak olarak RSS yerine elle seçilmiş YouTube videoları kullanıyor, çünkü "if you put in a bad video in your Google sheet, then everything else will be dictated based on that transcript of that bad video" — kalite kontrolü girdi seçiminde. Transkripti Dumpling AI çekiyor, Router ile 4 kola dağılıyor; klon videosu ister doğrudan YouTube'a (private) yükleniyor, ister Drive'a atılıp editöre e-postayla gönderiliyor. Günde bir kez, akşam 5'te çalıştırıyor. Video Make sponsorlu; ajans iş modeli anlatmıyor, kişisel marka/içerik üreticisi odaklı.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
Videoda yok (kendi hesapları için). Maliyet rakamları:
- OpenAI API: "just adding $5 to your account ... I've been using this for a while and I'm still in the same $5" — 5 USD.
- Dumpling AI: videoda "I'm still using the free credits"; kurulum dokümanında "They have a free trial ... after the trial you unfortunately have to pay", indirim kodu DAN20 (%20). Ücret rakamı vermiyor.
- Make: ücretsiz planla kullanılabilir; sponsor linkle 1 ay Pro ücretsiz.
- HeyGen: dokümanda "You can try it out with the free plan"; ElevenLabs profesyonel ses klonu "This ain't free tho" — rakam vermiyor.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
Videoda yok. (Tek "mesaj" şablonu, otomasyonun editöre attığı e-posta: aşağıda Üretim sürecinde.)

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Make.com, Google Sheets, Dumpling AI (YouTube transkript), OpenAI (chatgpt-4o-latest, o1-mini, DALL-E 3), Instagram Business (Facebook business sayfasına bağlı), LinkedIn, X (custom app gerekli), HeyGen (avatar klon), opsiyonel ElevenLabs (ses klonu), Google Drive, e-posta, YouTube. Tam doküman, prompt'lar ve blueprint modül dökümü: `ajans/arastirma/youtube/kaynaklar/CvQ3ARulOKE-dan-kieft-make-dokuman-promptlar.md`.

Akış (blueprint'ten, sıralı):
- Trigger: Google Sheets "Watch rows" (sheet "new sources", başlık A1:Z1, limit 1) — sütun: YouTube URL
- → Dumpling AI "Get YouTube transcript" (timestamps: No, dil: English)
- → Router
  - Instagram: ChatGPT (chatgpt-4o-latest) caption → DALL-E 3 (1024x1024, vivid, "Make an image based on this text: {caption}") → Instagram "Create photo post"
  - LinkedIn: ChatGPT post → LinkedIn "Create post" (PUBLIC)
  - X: ChatGPT (max 200 karakter) → X "Create a tweet"
  - YouTube Short: o1-mini script → HeyGen "Create avatar video V2" (720x1280, pitch 50, emotion Excited) → Sleep 5 dk → Sleep 3 dk (videoda "wait for like 8 minutes") → HeyGen "Get video" → HTTP "Get a file" → Router
    - Drive'a yükle → editöre e-posta
    - ChatGPT başlık → YouTube "Upload video" (privacy: private)

Prompt'lar (dokümandan aynen):

Instagram caption — kısa, düşünce akışı tonunda, hashtag/emoji yok:
```
Make a short instagram caption from this youtube transcript. Pull out a problem, a fact a benefit, the tone should be like you're having a thought about this idea and you're just writing out what you're thinking. Don't use hashtags and also no emojis. Keep it short and concise.
```
LinkedIn ve X aynı kalıp; LinkedIn'de "Make a short LinkedIn post", X'te "Make a short twitter tweet ... Use max 200 characters." (sonuna Dumpling transkripti eklenir).

YouTube Short / Reel script (o1-mini) — hook/geçiş/ana nokta/kapanış kalıplarıyla 80-120 kelimelik konuşma metni:
```
I want you to act as an expert YouTube short scriptwriter who specializes in transforming long-form content into viral 30-50 second scripts. You understand the exact patterns that drive engagement and keep viewers watching until the end.
Your task is to convert the following transcript into an youtube short script using these exact patterns:

HOOK (Choose one pattern):
"So [surprising fact/statistic]"
"[Industry/Topic] is about to change forever"
"This [new thing] just [dramatic action]"
"You're doing [common activity] wrong"

TRANSITION (Choose one):
"Here's what you need to know"
"Check this out"
"Let me show you"
"Here's how it works"

MAIN POINTS:
Use these transition phrases: • "First up is" • "Then we've got" • "The best part is"
Include these emphasis words: • "game-changing" • "groundbreaking" • "This is huge"
Use these transition words: • "Now" • "Next" • "And then"

CLOSING (Choose one pattern):
Question + Call to Action
Bold Prediction + Engagement Ask
Quick Summary + Value Statement End with:
"Pretty cool, right?"
"What do you think?"
"Follow for more [topic]"

Requirements:
Keep it between 80-120 words
Use casual, conversational tone
Make it punchy and high-energy
Break complex ideas into simple points
Write it exactly how you would speak it

ONLY output the exact video script. do not output anything else. Never include your thoughts, emojis, notes or formatting.

Here's the transcript to convert:
```

YouTube başlığı — 54 karakterden kısa, net, tek başlık:
```
Using the text input that I give to you I want you to come up with 1 YouTube Title for my video. Avoid creating titles that are too vague or misleading. Title should be clear, concise, and directly related to the content of the video. Titles should ideally be less than 54 characters.  Only create 1 title.

Here is the text input you can use as inspiration
```

Editöre giden otomatik e-posta (blueprint) — Drive linkiyle editöre iş bildirimi:
```
Subject: New video to edit! {{27.createdTime}}
Hey [editor name]

I got a new video that you can edit.
You can find it here {{27.webViewLink}}
```

HeyGen ayarları (dokümandan): Talking style Normal, input text = ChatGPT result, voice pitch 50, voice emotion excited, width 720 / height 1280; ElevenLabs API key HeyGen'e "3rd party voice" olarak eklenir.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türleri: IG tek görsel + kısa caption; LinkedIn metin post; X kısa tweet (≤200 karakter); YouTube Short (AI avatar konuşan kafa, 30-50 sn).
- Ton: "having a thought about this idea" — düşünce paylaşımı, hashtag ve emoji yok.
- Sıklık: günde 1 kez, "each day it will be running at like 5:00 p.m."; girdi (YouTube linki) elle seçiliyor.
- Facebook kendi kullanmadığı için eklenmemiş; "pretty much the same process as with instagram".
- Reklam: Videoda yok.

## Hatalar / uyarılar
- Tüm linkler açıldı (kurulum dokümanı, 2 prompt dokümanı, Sheet, blueprint JSON); Sheet şablonu sadece `URL` başlığından ibaret.
- Blueprint hatası: X kolundaki ChatGPT modülünün prompt'u "Make a short LinkedIn post ... Use max 200 characters." — dokümandaki "twitter tweet" ifadesi yerine LinkedIn kalmış; içe aktarınca düzeltilmeli.
- Blueprint'te yazarın kendi Instagram accountId, HeyGen avatar/voice ID'leri, Drive klasörü ve editör e-postası gömülü; hepsi değiştirilmeli.
- X bağlantısı zor: Make'te custom app oluşturmak gerekiyor (Make yardım sayfası + YouTube videosu linkli).
- HeyGen render süresi ~8 dk; sabit Sleep ile bekleniyor — render uzarsa "Get video" boş döner (polling yok).
- Kötü kaynak video = kötü içerik; girdi seçimi elle yapılmalı.
- YouTube yüklemesi bilerek "private" — yayına almadan önce kontrol için.

## Bizim ajansa alınacaklar (somut)
1. "Tek kaynak → çok format" akışını Türkçe kuralım: girdi = müşterinin kendi uzun videosu/röportajı/Google yorumları (ör. diş hekiminin 5 dk anlatımı); Sheet'e link at → transkript → IG caption + FB + LinkedIn + Reels/Shorts script. Girdi seçimini insan yapar, çıktılar onaya düşer.
2. Prompt kalıplarını Türkçeleştirip şablona alalım: "bir sorun, bir gerçek, bir fayda çıkar; hashtag/emoji yok; kısa" caption kalıbı ve hook/geçiş/kapanış kalıplı 80-120 kelimelik Reels script'i (Türkçe hook örnekleri: "Bunu yanlış yapıyorsunuz", "[Sektör] değişiyor").
3. "AI klon Reels" paketini klinik/emlak/avukat gibi yüzü önemli müşterilere ek hizmet olarak sunalım: HeyGen avatar + ses klonu, haftada 3 Shorts; YouTube'a "private" yükle + müşteriye onay e-postası/WhatsApp (editöre e-posta kolunun uyarlaması). Fiyatı HeyGen/ElevenLabs abonelik maliyetinin üstüne koyalım (videoda rakam yok, kendimiz hesaplayacağız).
4. HeyGen bekleme kısmını sabit Sleep yerine "status = completed olana kadar tekrar sor" döngüsüyle kuralım; blueprint'teki X prompt hatası gibi durumlar için her kolu test eden teslim kontrol listesi tutalım.
5. Görsel üretiminde DALL-E yerine daha iyi model (videoda önerilen Flux/Recraft) ve restoranlar için AI görsel yerine müşterinin gerçek fotoğrafı (bkz. DIXXW_jBS0w planı) — yemek görselinde AI kullanmayalım.
