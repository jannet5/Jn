# Create Your AI Social Media Agent in MINUTES! Zapier Agent Tutorial — Helena Liu — 20 dk — https://www.youtube.com/watch?v=2r4QNDuzOGQ

## Tek paragraf: ne anlatıyor
Kod yazmadan, Zapier Agents (agent.zapier.com) üzerinde "AI sosyal medya yöneticisi" kurulumu. Tek ajan, dört davranış (behavior): (1) her sabah 07:00'de nişle ilgili dünkü haberleri araştırıp Google Sheet'e satır olarak yazar, (2-3-4) Sheet'e yeni satır düştüğünde Facebook, LinkedIn ve Instagram için platforma özel gönderi yazar ve doğrudan yayınlar; Instagram için DALL-E 3 ile görsel de üretir. Bilgi tabanı olarak canlı bir Google Doc (örnek gönderiler) bağlanır; doc güncellenince ajanın bilgisi de güncellenir.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Video kendi hesabın için anlatıyor ama satış argümanı net: "Sosyal medya yöneticisine şirketler yılda 30-60 bin $ ödüyor; bu sistem ayda 10-50 $'a çalışıyor."
- Bizim için ürün: işletmeye "otomatik haber/sektör içerikli günlük paylaşım sistemi" kurulumu + aylık bakım.
- Yaratıcının kendi geliri: ücretsiz kurs → ürün/eğitim (productcamps.com).

## Müşteri bulma: hangi kanal, hangi mesaj
- Video inbound (YouTube). Satış mesajı olarak kullanılabilir cümle: "Sosyal medya yöneticisi maaşı yerine, ayda birkaç dolarlık sistem — her gün sizin yerinize paylaşır."

## Üretim süreci: hangi araçlar, hangi otomasyonlar
Araçlar: Zapier Agents (ücretsiz, Zapier hesabı ile), Google Docs (bilgi tabanı), Google Sheets (haber kuyruğu), Facebook Pages, LinkedIn, Instagram for Business, OpenAI DALL-E 3.

Akış:
1. **Araştırma davranışı** — Trigger: Schedule (her gün 07:00). Prompt özeti (yaratıcının kendi sözleriyle): "Your goal is to identify and compile a list of AI-related brands and companies that are being covered in the news online. Conduct the research every day about news that was published yesterday. Extract: date of the article, title, link, and summarize the article." Action: Google Sheets → Create Spreadsheet Row (sheet ve worksheet SABİT seçilir; sütun değerlerini ajan üretir).
2. **Facebook davranışı** — Trigger: Google Sheets → New Spreadsheet Row. Prompt: "Write a high-engagement Facebook post designed to grab attention and spark conversation. Do not include any links. Look at the article from the Google Sheet and use it to create the post. Provide value, provide actionable steps, make it feel relevant and current, encourage discussion." + ton/stil + örnek gönderiler (Insert Data ile Google Doc bağlanır). Action: Facebook Pages → Create Page Post (mesajı ajan üretir).
3. **LinkedIn davranışı** — Aynı trigger. Prompt: "You are a skilled LinkedIn content strategist. Your goal is to write a high-converting LinkedIn post that grabs attention, delivers value and drives action." + anahtar öğeler + örnekler. Action: LinkedIn → Create Share Update.
4. **Instagram davranışı** — Aynı trigger. Prompt: "You are a skilled Instagram content strategist. Your goal is to write engaging Instagram captions that grab attention, feel natural, and encourage likes, shares and comments." + örnek caption'lar + "generate an image". Action 1: OpenAI → Generate Image (DALL-E 3). Action 2: Instagram for Business → Publish a Photo.

İpuçları: her davranışa TEK görev ver (halüsinasyonu azaltır); platform başına ayrı davranış (LinkedIn ile Instagram üslubu farklı); Drive'da yüzlerce dosya varsa sheet'i ajana seçtirme, sabit değer ver; test sırasında chat'ten verilen geri bildirimi ajan kendi talimatına ekliyor ("make the post double length, new line per sentence").

## Sosyal medya / reklam
- İçerik türü: sektör haberi + yorum (günlük). Facebook: link yok, tartışma odaklı. LinkedIn: değer + CTA. Instagram: caption + AI görsel.
- Sıklık: günde 1 haber → 3 platforma 3 gönderi.
- Reklam yok.

## Hatalar / uyarılar
- Zapier Agents'ta özel HTTP isteği yok → Flux/Stable Diffusion bağlanamıyor, sadece DALL-E (video tarihinde).
- Ajana dosya seçtirmek hatalı sonuç verebiliyor → sabit değer.
- Tam otomatik yayın: onay kapısı yok; marka hesabında riskli, bizde onay adımı şart.
- Açıklamadaki kaynak linki (productcamps.com/resources) içerik vermedi (boş sayfa) — prompt'lar transkriptteki özetle sınırlı.

## Bizim ajansa alınacaklar (somut)
1. "Sektör haberi → 3 platform gönderi" hattını n8n'de kuralım (Zapier yerine, HTTP/Flux/Nano Banana serbestliği için). Yapı aynı: Schedule → haber arama → Sheet/DB → platform başına ayrı LLM düğümü → onay (Telegram/Slack) → yayın.
2. Müşteri başına "örnek gönderi dokümanı" (canlı Google Doc) = marka sesi bilgi tabanı. Onboarding'de 10 örnek gönderi topla.
3. Platform başına ayrı prompt; yukarıdaki üç rol cümlesini kendi prompt kütüphanemize ekle.
4. Satış argümanı: "SMM maaşı vs. ayda X TL sistem" karşılaştırma slaytı.
