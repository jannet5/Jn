# I Built an AI Content Agent With N8N and Claude (Step-by-Step) — Greg Isenberg (konuk: The Boring Marketer / Vibe Marketer) — 24 dk — https://www.youtube.com/watch?v=m9iaJNJE2-M

## Tek paragraf: ne anlatıyor
Podcast formatında bir n8n akışı: tek bir anahtar kelime girilince Apify ile YouTube'daki en çok etkileşim alan 20-30 video ve X'teki ilgili gönderiler çekilir, videolar transkript edilir, hepsi tek metin bloğunda birleştirilir; OpenAI "içerik fikri ajanı" bu araştırmadan taze açılar üretir; Perplexity (OpenRouter üzerinden) her fikir için gerçek istatistik/vaka araştırır; Claude 3.7 Sonnet marka sesi + direct-response kurallarıyla LinkedIn gönderisini yazar; OpenAI görsel üretir; taslak Google Doc'a yazılır ve Slack'e "Approve" butonlu mesaj gider; onaylanınca LinkedIn'e yayınlanır. İddia: haftada 10-15 saat tasarruf, "doğrulanmış" içerik (zaten performans göstermiş içerikten türetildiği için). Şablon JSON ücretsiz: thevibemarketer.com/greg.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Doğrudan satış yok; konuk, bu akışın "danışmanlık / freelance / ajans operasyonu için omurga" olabileceğini söylüyor. Fiyat rakamı yok.
- Gelir hunisi: ücretsiz şablon ↔ e-posta listesi.

## Müşteri bulma: hangi kanal, hangi mesaj
Videoda yok (içerik ile inbound).

## Üretim süreci: hangi araçlar, hangi otomasyonlar
Araçlar: n8n, Apify (YouTube + X scraper aktörleri, HTTP Request düğümü), OpenAI (fikir ajanı + görsel), OpenRouter (tek API anahtarıyla Perplexity Sonar / Claude / OpenAI seçimi), Claude 3.7 Sonnet (yazım), Google Docs, Slack (human-in-the-loop onay), LinkedIn.

Akış (renk kodlu aşamalar):
1. **Chat trigger**: kullanıcı anahtar kelime girer (örn. "n8n").
2. **Araştırma**: HTTP Request → Apify YouTube scraper (yüksek etkileşimli 20-30 video) + HTTP Request → Apify X scraper (kendi hesabındaki otomasyon/AI gönderileri). Videolar transkript edilir. Merge → tek büyük metin bloğu (hook'lar, başlıklar, içerikler, X gönderileri).
3. **Content Idea Generator (AI Agent, OpenAI)**: prompt özü (aynen aktarılan parçalar): `Using all of this content, create a list of actionable content ideas that are strictly related to marketing.` Her fikir için: kısa net başlık, `scroll-stopping hook`, önerilen format, benzersiz açı (`a unique point of view around how marketers can scale their efforts using AI and leveraging n8n`), teknik jargon yok; kendi en iyi X gönderilerinden ton/temalar çıkarılır. Kopya değil ilham.
4. **Research Agent (Perplexity Sonar via OpenRouter)**: gerçek kullanım örnekleri, istatistikler, trendler, framework/vaka çalışmaları, popüler görüşler → halüsinasyonu azaltır.
5. **Merge** fikir + araştırma → **LinkedIn Content Agent (Claude 3.7 Sonnet)**. Prompt kuralları (aynen): `Write with authentic expertise and direct communication. Be confident and straightforward. Don't be overly formal or academic. Speak directly to the reader. Avoid corporate jargon and marketing speak.` · `Create a narrative-driven post that gives readers actionable insights and takeaways that will impact their business in positive ways.` · `Only use data if it's relevant.` · `Don't use hashtags` (emoji de yok) · Çıktı formatı: title, content · Rol: `You're a LinkedIn content strategist and conversion copywriter` → hook → ilgi → istek → aksiyon değeri → net CTA. Greg'in eklemesi: `don't use any em dashes` (AI işareti).
6. **Görsel**: OpenAI image gen, basit prompt ("3D image communicating an AI system"); öneri: Drive'da 50 kişisel fotoğraf klasörü, rastgele seç (LinkedIn'de yüz fotoğrafı daha iyi).
7. **Human in the loop**: Google Doc'a yaz → Slack kanalına link + **Approve** butonu → LinkedIn'e yayın.

Prompt yazma yöntemi: temel prompt yaz → ChatGPT/Claude'a "improve this prompt, here's what I want to achieve" + örnek içerikler ver. İlke: "know what questions to ask, know what good looks like." Her göreve en iyi model (araştırma: Perplexity, yazım: Claude).

## Sosyal medya / reklam
- İçerik türü: LinkedIn narrative post (kişisel hikâye + 3 somut yol + CTA), görsel eşlikli. Örnek başlık: "Three ways to scale your marketing without hiring another team."
- Sıklık: belirtilmedi; test-et-öğren: gizli hesapta dene, tutmayanı sil, tutanı çoğalt, diğer kanallara uyarla.
- Reklam yok.

## Hatalar / uyarılar
- Modele az bağlam verip "çöp" demek; bağlam = scraping + marka sesi.
- Tam otomatik yayın henüz güvenilir değil → onay adımı şart.
- Hashtag, emoji, em-dash = AI kokusu.
- Apify/OpenRouter/n8n ücretli; maliyet verilmedi.
- Şablon linki e-posta karşılığı (lead magnet); bu oturumda indirilmedi.

## Bizim ajansa alınacaklar (somut)
1. **Araştırma-önce içerik hattı** kendi Instagram/LinkedIn'imiz ve müşteriler için: n8n → Apify (Instagram/YouTube'da sektörün en iyi içerikleri) → fikir ajanı → Perplexity doğrulama → Claude yazım → onay → yayın. Zapier/Make yerine n8n (OpenRouter + HTTP serbest).
2. **Onay kapısı standardı**: Google Doc + Telegram/Slack "Onayla" butonu; müşteri hesaplarında asla onaysız yayın.
3. **Marka sesi prompt bloğu**: yukarıdaki kurallar Türkçeleştirilip her müşteri için "ses dosyası" olarak saklansın (hashtag yok, emoji sınırlı, uzun tire yok, doğrudan konuş).
4. **Prompt iyileştirme döngüsü**: her yeni müşteri promptu Claude'a "iyileştir + 3 örnek" ile rafine.
5. **Gerçek fotoğraf havuzu**: müşteriden 30-50 fotoğraf toplayıp rastgele eşleştirme; AI görsel yerine gerçek işletme fotoğrafı.
