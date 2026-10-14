# Gemini 3.1 ile Tek Promptta Profesyonel Web Sitesi Yapmak (Creating a Professional Website in a Single Prompt with Gemini 3.1) — burhan kocabıyık — 8 dk — https://www.youtube.com/watch?v=at7rogD2lMY

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu).
## Tek paragraf: ne yapıyor, sonuç ne
Google Antigravity'yi indirip başlangıçta verilen ücretsiz kredilerle, Gemini 3.1 (sosyal medyada popüler olan "Gemini UI" yeteneği) modeline kendi hazırladığı bir **kılavuz MD dosyası**nı sürükle-bırak verip "Gemini UI 3.1'i kullanarak ve MD dosyasını okuyarak chatbot uygulaması yapan bir ajans için web sitesi oluştur; kendin ayarla, çok profesyonel görünsün" diyor. Kılavuz dosyası ajanın rolünü (çok kaliteli site yapmak), başta soracağı soruları (marka adı, görseli, ne koyulacağı, fiyatlar vb.) ve değişmeyecek görsel/kalite kurallarını (navigasyon, hero section, geçişler) içeriyor. Sonuç: geçişleri olan, metinleri yazılmış, Gemini ile görselleri üretilmiş tek promptluk site; lokalde çalıştırılıp GitHub'a push edilip Vercel'den deploy, sonra kendi domain'e bağlanıyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Google Antigravity** — antigravity.google'dan indiriliyor; başlangıçta ücretsiz kredi.
- **Gemini 3.1** (Antigravity içinde; "Gemini UI 3.1"). Alternatif: **Claude Sonnet** ("isterseniz kullanabilirsiniz ama Gemini yeterli ve ücretsiz").
- **Kılavuz MD dosyası** (Burhan'ın hazırladığı başlangıç/"kılavuz" dosyası) — "açıklamaya koyuyorum" diyor ama **açıklamada dosya linki yok** (sadece Skool/Gumroad/affiliate linkleri; belki skool.com/doa-zero "tüm kaynaklar ücretsiz" altında).
- Gemini'nin kendi görsel üretimi + **Veo 3** (video) — Antigravity içinde.
- **GitHub** + **Vercel** (+ domain).
- Açıklama linkleri: Leftflow https://leftflow.ai/tr/ , Instantly https://refer.instantly.ai/qxs0i1s20keo , n8n https://n8n.partnerlinks.io/a3jiiy602uuf , Gumroad https://benburhan.gumroad.com/l/pjsiw , https://benburhan.gumroad.com/l/bsrxgi , DOA skool.com/doa/about , skool.com/doa-zero/about.

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. antigravity.google → indir → ücretsiz kredilerle başla.
2. Açıklamadaki kılavuz MD dosyasını indir → Antigravity paneline sürükle-bırak.
3. Prompt (aşağıda): Gemini UI 3.1 + MD dosyasını oku + chatbot ajansı sitesi + "kendin ayarla, çok profesyonel olsun". (İstersen logo, renkler vb. detay ver.)
4. Ajan kılavuza göre başta soru soruyor (marka adı/görseli, siteye ne konacak, fiyatlar…), sonra siteyi yapıyor.
5. "Lokalde run et" → localhost linki → kontrol.
6. İsteğe bağlı: "benim için görsel ve video da oluştur web sitesine" → Gemini görsel + Veo 3 video ekliyor; kurucunun fotoğrafını verip siteye koydurabilirsin; "blog sayfası ekle" → blogları yazıp görsel üretiyor.
7. "Bu web sitesini canlıya alacağız, benim için GitHub'a pushla."
8. Vercel → New → Project → GitHub reposunu import → Vercel linki. Sonraki her GitHub güncellemesi otomatik yansıyor.
9. Domain: Vercel proje → Domains → kendi domain'ini ekle veya satın al.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Ana prompt (sözlü aktarım): "Gemini UI 3.1'i kullanarak ve MD dosyasını okuyarak bir web sitesi oluştur. Chatbot uygulaması yapan bir ajans için bir web sitesi olacak. Kendin ayarla. Çok profesyonel görüntü olsun."
- "Lokalde run et." / "Benim için görsel ve video da oluştur web sitesine." / "Bu web sitesini canlıya alacağız. Benim için GitHub'a pushla." / "Blog sayfası ekle."
- **Kılavuz MD dosyasının içeriği (videoda anlatılan kadarıyla, metin gösterilmiyor):**
  - Rol: "Çok kaliteli bir web sitesi oluşturacaksın, senin görevin bu."
  - Ajan görevleri: başlangıçta soru sor — markanın resmi/görseli, ismi; siteye neler konacak (ör. chatbot yapıyorsan chatbot'un fiyatı); ne tarz sistem kurulacağı.
  - "Hiçbir zaman değiştirmemesi gereken" bölüm: görüntünün nasıl olması gerektiği (kaliteli geçişler, navigasyon, hero section vb.).
  - "Bu aslında sizin prompt'unuz; kılavuzu verdikten sonra prompt yazmanıza bile gerek yok." Müşteriye/şirkete göre değiştirilebilir.
  - Dosyanın tam metni: Videoda yok (açıklamada da yok).

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- **Model seçimi**: Gemini 3.1'in UI/frontend yeteneği "inanılmaz iyi".
- **Kılavuz dosyasında sabit tasarım kuralları** (değiştirilmeyecek görsel standart: geçişler, navigasyon, hero) + başta soru sorma.
- Üretilen görselin renkleri site renkleriyle uyumlu ("renkleri barıştırıyor").
- Gemini görsel + Veo 3 video ile gerçek içerik; kurucu fotoğrafı gibi gerçek görsel eklenebilir.
- Font/renk kuralı detayı: Videoda yok.

## Hatalar ve çözümleri
Videoda yok. (Sadece uyarı: kod lokalde çalışır, canlı değildir → GitHub + Vercel gerekiyor.)

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Rakam yok. "Hem kendiniz için kullanabilirsiniz hem de müşterilerinize hizmet olarak satabilirsiniz"; "birçok ajansın, uygulamanın sitesini geliştirmesi lazım". Maliyet: Antigravity ücretsiz kredileri ile ücretsiz.

## Bizim fabrikaya alınacaklar (somut)
1. **Kılavuz MD şablonu (bizim versiyon)** üç bölümlü: (a) Rol, (b) Başlangıç soruları (marka adı, logo/görsel, hizmetler, fiyatlar, iletişim/rezervasyon, ton), (c) "Asla değişmez" kalite kuralları (nav, hero, geçişler, mobil, okunurluk). Prompt yerine bu dosya verilir; müşteri bazında sadece cevaplar değişir.
2. **Ucuz/ücretsiz ilk taslak hattı**: Antigravity + Gemini 3.1 ücretsiz kredilerle müşteriye satış öncesi "demo site" üret (satış toplantısına hazır site götürmek için maliyetsiz).
3. **Model A/B**: aynı kılavuzla Gemini 3.1 ve Claude çıktısını karşılaştırıp en iyisini seç.
4. Kurucu/işletme sahibi fotoğrafını siteye koyma adımı (yerel işletmede güven unsuru).
5. Blog/ek sayfaları görselli otomatik üretme ("blog sayfası ekle").
6. Deploy: GitHub push → Vercel import → domain; GitHub güncellemesi = otomatik yayın.
