# Sell AI Websites To Local Businesses (Copy Me) — Pavlo — 17 dk — https://www.youtube.com/watch?v=WWCzZ56VAT4

## Tek paragraf: ne anlatıyor
Pavlo, "düşük ücretli müşteriden dünyayı gezerken 20.000 $/ay'ın üzerine" çıkardığını söylediği sistemi anlatıyor: işletmeler siteyi nasıl yaptığınızı değil sonucu önemser; freelancer'lar H1/H2/hız gibi özellik sattığı için kaybeder. Akış: Outscraper ile Google Maps'ten liste çek → sitesi olan/olmayan ayrımı yap → "sitenizde birkaç bozuk şey buldum, yeni bir ana sayfa yaptım" mesajı → yanıt verene GHL "Funnel AI" ile saniyeler içinde site üret → demoda eski site ile yan yana göster → arkasına GHL Conversation AI (chat botu), AI sesli ajan, yorum yanıtlama ve sosyal medya paylaşımı ekleyerek 150 / 300 / 500 $/ay kademeli abonelik sat. Tek sektörde uzmanlaşınca prompt/FAQ dokümanları birikir ve "80% of this process can be automated".

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- **Kime:** Yerel hizmet işletmeleri; örnek niş peyzaj (landscaping), çatıcı, tesisatçı. Tek sektör önerisi: "if you only work with landscapers you're going to have the most in-depth prompt".
- **Ne:** AI ile üretilen site + "konuşma başlatan" arka uç (chat botu, sesli ajan, otomatik takip, yorum yönetimi, sosyal medya paylaşımı).
- **Fiyat kademeleri (videodaki rakamlar aynen, USD):**
  - **150 $/ay** — "if they literally just want a website that you just copied and pasted I would charge them $150 per month to host it" (ücretsiz ana sayfa + sitenin geri kalanını biz tamamlarız).
  - **Peşin özel site ücreti:** ek özellik/yerleşim isterse "you could charge $1,000 I've seen people charge 500 2,000"; kendi ajansı özel WordPress site için **4.000-6.000 $ peşin**.
  - **300 $/ay** — AI lead takibi (SMS, otomatik e-posta, otomasyon/workflow'lar) + Google yorum yönetimi + AI yorum yanıtlama + AI sosyal medya paylaşımı (Facebook, Instagram, LinkedIn).
  - **500 $/ay** — "AI employee": sesli AI ajan + Conversation AI (chat) — "I recommend keeping that for the $500 per month plan".
- **Maliyet aktarımı:** Telefon numarası/SMS/arama kullanım bedeli müşterinin kredi kartına yüklenir ("you're not buying the phone number... their credit card is going to be charged on this plan").
- **Değer argümanı:** Resepsiyonist/pazarlama asistanı 3.000-5.000 $/ay yerine 500 $/ay; en iyi müşteriler AI'ı resepsiyonun yanında kullanıyor.
- **Platform:** GHL 30 günlük deneme — "you can actually get clients before you ever start paying".

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- **Liste:** tryoutscraper (Outscraper) → kategori "Landscaping", şehir Las Vegas (veya eyalet/ülke) → "Get data" → tablo: işletme adı, site, adres, telefon, e-posta, sosyal medya linkleri, puan, yorum sayısı → "Websites" sütununa göre sırala → sitesi olanlara ayrı, olmayanlara ayrı mesaj.
- **Gözlem:** 106 yorumu ve 4 yıldızı olan işletmenin sitesi yok → "why don't they have a website to actually feature those reviews?"
- **Eski yöntem (yapma):** "hey do you need a new website" soğuk araması — "this is outdated".
- **Mesaj şablonu (aynen):**
  > "Hey ABC Landscaping, are you still open? Found a few things broken on your website. I went ahead and built you a new homepage and fixed a few things that will help customers. Do you want to see it?"
  (TR: "Hâlâ açık mısınız? Sitenizde birkaç bozuk şey buldum; yeni bir ana sayfa yapıp birkaç şeyi düzelttim, görmek ister misiniz?" — kimseye önceden site yapılmıyor; yanıt verip toplantı alanlara saniyeler içinde üretiliyor.)
- **Demo kurgusu:** Toplantı öncesi logo eklenmiş şablon/AI site hazır → "hey here's the first draft" → eski siteyle yan yana aç → sitesi yoksa: "do you think customers would trust you more if you had a website like this?" → ödeme sonrası veriler, yorumlar, renkler, personel fotoğrafları özelleştirilir.
- **Değer sorusu (aynen):**
  > "how much is one customer worth to you? is it 1,000 is it 100 is it 2,000 is it 5,000? cool, if I could help you get even an extra one customer per week or per month would that be worth it for you?"
  (TR: tek müşterinin değerini söyletip aylık ücreti küçültme tekniği.)

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
- **Araçlar:** Outscraper (lead listesi) · GoHighLevel (Funnel AI site üretimi, Conversation AI v2, AI Voice Agent, Reputation/auto-reply, Social Planner) · ChatGPT (FAQ üretimi) · Google Docs (müşteri bilgi formu) · Google Calendar · alternatif site araçları: WordPress, Webflow, Weebly.
- **Site üretimi (GHL Funnel AI):** Sites → New funnel → "Funnel AI" → işletme adı "ABC Landscaping" → sektör seç → ayarlar: hedef "generate more leads", ton "friendly" → Generate → görseller, içerik, örnek Google yorumları hazır → logo değiştir (sosyal medyadan/Google'dan bul). Alternatif: tek sektör şablonunu herkese kullan, sadece logo değiştir. Temel kural: telefon, e-posta, (varsa) adres üstte.
- **Chat botu (Conversation AI v2):** Settings → Conversation AI v2 → Create bot → Autopilot → kanal: chat widget / live chat (+ sosyal medya) → işletme adı → bekleme ~1 dakika → Save → Bot goals → "appointment booking" → Integrations'tan Google Calendar bağla; müşteri takvim bağlamak istemezse "don't book appointments, only send the booking link" → Bot training → müşteri sitesinin URL'sini gir → bot tüm siteyi tarar → Save → siteye entegre.
- **Sitesi olmayan müşteri için eğitim verisi:** Google Doc'a bilgilerini yazdır + ChatGPT prompt'u (aynen anlatıldığı gibi): "Landscaping in Las Vegas, here are the client details, give me an FAQ section" / çatı örneği: "give me the top questions and answers in roofing".
- **AI sesli ajan:** Conversations sekmesinin üstündeki "AI Voice Agent" → Create new agent → ses seç ("Rebecca") → müşteri adı → toplanacak bilgiler: ad, e-posta, adres (opsiyonel) → Advanced: FAQ dokümanını içe aktar, itiraz karşılama → Next → müşteri için satın alınan telefon numarasını seç (müşterinin kartı bağlı) → Save. Gelen aramalar AI'ya düşer, randevu alır.
- **Yorum yanıtlama:** Reputation settings → auto-respond for reviews → kaynak seç (Google) → her yoruma AI yanıt ("hey Bob thanks so much...").
- **Sosyal medya:** tüm hesapları bağla, AI ile post üret ve Facebook/Instagram/LinkedIn'e paylaş.
- **Form sonrası akış (anlatım):** Form dolduruldu → AI ile konuşma başlat → randevuya dönüştür; müşteri çatıdayken aramaları AI karşılar.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- Müşteri için: GHL ile AI üretimli postların Facebook, Instagram, LinkedIn'e otomatik paylaşımı 300 $/ay paketine dahil. Sıklık ve bütçe verilmiyor.
- Ajansın kendi reklamı: Videoda yok.

## Hatalar / uyarılar
- "Sitenizde bozuk şeyler buldum" mesajı, siteye gerçekten bakılmadan atılıyorsa yanıltıcı; TR'de dürüst varyant kullan (gerçekten bir kusur bulup yaz).
- Özellik satma hatası: "most freelancers fail because they sell features H1 tags H2 tags fast load times".
- Herkese önceden site yapma; yalnızca yanıt verenlere.
- AI site ilk taslak "is this going to be perfect? of course not" — müşteri beklentisini "first draft" diye yönet.
- Sesli AI/SMS kullanım ücretleri müşteriye yansıtılmazsa marj erir.
- Gelir iddiaları ("20.000 $/ay") affiliate pazarlaması; açıklamada yalnızca GHL affiliate ve kurs linkleri var, şablon linki yok.

## Bizim ajansa alınacaklar (somut)
1. **3 kademeli abonelik yapısını kopyala:** (a) Site + barındırma, (b) Site + WhatsApp/SMS takip + Google yorum + aylık sosyal medya postları, (c) + AI sesli/yazılı resepsiyonist. Videodaki oran 150/300/500 $; TR fiyatını bu oranla (1x / 2x / 3.3x) kuralım.
2. **"Bir müşteri size kaç para?" sorusunu satış scriptine koy:** Diş kliniği/estetik için tek hasta değeri yüksek; aylık ücreti "ayda 1 ekstra hasta" ile kıyasla.
3. **Sektör FAQ dokümanı üret:** Her nişimiz için ChatGPT/Claude ile "en sık 30 soru-cevap" + müşteri bilgi formu (Google Doc) → chat botu ve sesli asistanın eğitim seti; her müşteride tekrar kullan.
4. **Chat botuna takvim bağlama alternatifi:** Müşteri takvim paylaşmazsa "sadece randevu linki gönder" moduyla başla; sürtünmeyi azalt.
5. **Yorum yanıtlama + sosyal paylaşımı aynı pakete koy:** Düşük maliyetli, görünür iki hizmet; TR'de Google yorumlarına yanıt veren işletme az, fark yaratır.
6. **Kullanım ücretlerini ayrı faturala:** WhatsApp Business API / SMS / sesli dakika maliyetini müşterinin ödeme yöntemine bağla, sabit abonelik marjını koru.
