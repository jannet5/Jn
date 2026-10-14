# CLAUDE CODE İLE 1 SAATTE PROFESYONEL WEB SİTESİ YAP VE SAT: 2026 REHBERİ — burhan kocabıyık — 24 dk — https://www.youtube.com/watch?v=ydO2_a97J6g

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu).
## Tek paragraf: ne yapıyor, sonuç ne
Burhan, kendi ajansı Leftflow için Antigravity (VS Code türevi) içindeki Claude Code eklentisiyle sıfırdan çok sayfalı bir site yaptırıyor. Önce mevcut Leftflow sitesinin tüm sayfalarını PDF olarak indirip Claude'a veriyor, bir prompt'la "proje altyapısı" MD dosyası ürettiriyor (Nano Banana + Fal.ai ile görsel/video üret, frontend-design skill'ini kullan, bitince Vercel'de canlıya al), Fal.ai API anahtarını veriyor ve bypass modunda ~25 dk bırakıyor. Sonuç v1: TR/EN/NL üç dilli, hızlı, kendi görsellerini üretmiş, sektör/endüstri sayfaları, form ve blog yazıları olan site — ama "benim için yeterince profesyonel değil". v2'de beğendiği bir ajans sitesini referans verip "birebir aynısını yap", logo animasyonu, Veo 3 video, Calendly takvimi ve form otomasyonu istiyor; Claude 99 sayfa üretiyor. Vercel deploy'u "yaptım" deyip yapmayınca GitHub+Vercel üzerinden manuel/yeniden deploy ettiriyor. Son 4 dk satış: Apify Google Maps scraper ile (ör. İstanbul Kadıköy restoranları) web sitesi olmayan/eski olan işletmeleri bulup cold e-posta atma; %7–12 dönüş, 100 e-postada ~4 toplantı, 10 toplantıda 1 satış; normalde 2.000–3.000 $ istenen siteler.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Claude Code** — Antigravity (veya VS Code) içinde Extensions'tan "Claude Code" eklentisi; panel ya da terminal olarak açılabiliyor. Modlar: auto-edit, **bypass permissions** (durmadan çalışsın diye kullanıyor), plan.
- **Antigravity** (Google'ın IDE'si) — Claude Code'u içinde çalıştırıyor. Link açıklamada yok.
- **frontend-design skill** — "arayüzün vibe-coded gibi durmaması için UI kitleri/skill kullanmalısın". Kaynak olarak **aitmpl.com** (Claude Code Templates; transkriptte "AI tmpl") içindeki skill listesinden frontend-design'ı kopyalayıp MD dosyasına ekletiyor. Ayrıca "test" skill'lerini de genelde eklediğini söylüyor (isim vermiyor).
- **Fal.ai** — API anahtarı alıp Claude'a veriyor; görsel/video üretimi Fal üzerinden.
- **Nano Banana / Nano Banana 2** (Google görsel modeli, Fal üzerinden) — görseller ve "Nano Banana image edit" ile referans görsellerin aynısını gerçek insan fotoğrafı gibi üretme.
- **Veo 3** — v2'de video üretimi için isteniyor.
- **GitHub** (repo, otomatik push) + **Vercel** (deploy, deployments sekmesinden kontrol). Vercel bağlantısı tek seferlik auth popup + kod ile.
- **Calendly** — toplantı takvimi entegrasyonu (transkriptte "Cendly"); ayrıca bir "strain e adresi" veriyor (altyazı belirsiz; muhtemelen Stripe/benzeri bir adres — doğrulanamadı).
- **21st.dev** (transkriptte "21'in içerisinde") — hazır animasyon/arka plan bileşenleri; bileşenin prompt'unu kopyalayıp Claude Code'a veriyor.
- **Apify** — Apify Store → Google Maps scraper; mahalle bazlı işletme + e-posta + web sitesi var/yok çıkarma.
- **Instantly** (açıklamada, cold e-posta): https://refer.instantly.ai/qxs0i1s20keo
- n8n (açıklamada): https://n8n.partnerlinks.io/a3jiiy602uuf
- Gumroad ürünleri (açıklama): https://benburhan.gumroad.com/l/pjsiw , https://benburhan.gumroad.com/l/bsrxgi
- Referans/kendi site: Leftflow (https://leftflow.ai/tr/ — linki at7rogD2lMY açıklamasında var).
- Not: Videoda "açıklamaya detaylı prompt ve hazır MD dosyası koyacağım" diyor ama **açıklamada bu prompt/MD linki yok** (sadece webinar, Skool, Gumroad, affiliate linkleri).

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
**v1 (sıfırdan, ~25 dk otonom):**
1. Antigravity indir → Extensions → Claude Code kur → boş klasör aç.
2. Kendi mevcut sitesini (Leftflow) analiz ettirip **tüm sayfalarını PDF** olarak indiriyor ("içindeki bütün sayfaların olmasına dikkat edin") — hem metinleri okusun hem benzer yapıyı kursun diye.
3. Önce bir chat'te prompt yazıp **MD dosyası** (proje altyapısı / talimat dosyası) oluşturtuyor: Nano Banana kullan, Fal.ai kullan, çok kaliteli web sitesi yap, frontend-design skill'ini kullan, bitince Vercel'de canlıya al. "Projeye başlarken Claude Code'un neler yapabilmesi gerektiğini mutlaka yazmanız lazım."
4. aitmpl.com'dan frontend-design skill'ini kopyalayıp MD'ye ekletiyor.
5. Fal.ai'den API anahtarı alıyor, Claude'a veriyor.
6. MD + PDF'ler + görseller Claude Code'a; "prompt'umu da iyileştir". Ekstra prompt şart değil, MD yeterli.
7. Mod: auto-edit → **bypass**.
8. Bütçe limiti: "Fal'ı çok fazla kullanma, maksimum 30 görsel veya 10 video, daha fazla oluşturma" — yoksa 100 istek = 100 $.
9. ~25 dk sonra site hazır: TR + EN + NL, hızlı, kendi görselleri, sektörler, form, endüstriler (sağlık vb.), yazılmış bloglar.

**v2 (ertesi gün, aynı klasör):**
10. Aynı klasörü açınca Claude tüm dosyaları görüyor; "ideal senaryoda her değişikliği otomatik GitHub'a yüklesin".
11. Prompt: her şey (mantık, içerik) kalsın ama daha detaylı: Nano Banana, video için Veo 3, Leftflow logosunu animasyonlu yap, beğendiği ajans sitesine bak (açılış animasyonu, takvimden toplantı alma), form otomasyonunu arka tarafa bağla. API + "strain" adresi + Calendly linki veriyor.
12. Claude referans sitenin tüm sayfalarını okuyor, hatada kendisi yeniden deniyor, sayfa sayfa inceliyor → **99 sayfa**.
13. "Vercel'e deploy et, burada görmek istiyorum" → Claude "canlıya aldım" diyor ama Vercel → Deployments'ta son deploy 2 gün önce → düzeltme prompt'u (aşağıda) → commit push ediliyor ama Vercel doğru sürümü almıyor (yeni paket sürümleri patlatabiliyor) → **manuel redeploy** → v2 canlı (video, animasyon, form, case study'ler).
14. Daha birebir kopya için: referans sayfanın PDF'ini (print → PDF) veya DevTools'tan layout/stil kodlarını kopyalayıp veriyor ("stili verdiğim için PDF'e gerek yok bence").
15. Son prompt: referans sitenin birebir aynısını kendin oluştur, öncekiyle alakası olmasın; AI görselleri kalitesiz → Nano Banana 2 + image edit ile gerçek insan resimleri, Fal üzerinden. (~30 dk daha sürecek, video bitmeden kapanıyor.)

**GitHub + Vercel kurulumu:** GitHub → organizasyon → yeni repo ("websitem") → URL'yi Claude'a ver → Claude push eder. Vercel'de ya manuel "new project → GitHub repo import", ya da Claude GitHub üzerinden kendisi deploy eder; sonra her push'ta Vercel otomatik çeker. Kontrol: Vercel → proje → Deployments (tarih).

**Satış:** Apify → Store → Google Maps scraper → kategori (restoranlar) + bölge (İstanbul Kadıköy) → e-posta + web sitesi var/yok listesi → site yoksa "site yapalım", varsa "güncelleyelim mi?" → kısa cold e-posta.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
(Transkriptten sözlü aktarım; altyazı hataları düzeltildi.)
- MD üretme (v1, özetle söylüyor): "Nano Banana'yı kullanmanı istiyorum, Fal.ai'yi kullanmanı istiyorum ve benim için çok kaliteli bir web sitesi oluşturmanı istiyorum." + "Arayüzün iyi olmasını sağlamak için frontend-design skill'ini kullanman gerekiyor." + "Bunu bir MD dosyası haline getir." + "Prompt'umu da iyileştir." + "Bittiğinde direkt Vercel'de canlıya alacağız; Vercel'e kendin bağlan ve uygulamayı canlıya al."
- Bütçe limiti: "Fal'ı çok fazla kullanma. Maksimum 30 tane görsel kullan veya 10 tane video kullan. Daha fazlasını oluşturma."
- v2: "Bütün her şey aynı kalsın, bütün mantığı, içeriği tutabilirsin ama ben ekstra daha detaylı bir şey istiyorum. Bunun için Nano Banana'yı kullan, video oluşturmak için Veo 3'ü kullan ve Leftflow logosunu animasyonlu bir hale çevir. Şöyle bir web sitesi var, çok beğendim, bu web sitesine bak. Bir de form otomasyonunu arka tarafa bağla."
- Deploy: "Bunu Vercel'e deploy et, ben burada görmek istiyorum. Bütün değişiklikleri bu web sitesinde görmek istiyorum."
- Deploy düzeltme: "Canlıya aldım dedin ancak son deployment iki gün önce yapılmış. GitHub'daki Leftflow projesini önce güncelle, sonrasında da Vercel'i güncelle."
- Son kopya + görsel düzeltme: "Bu web sitesinin birebir aynısını yapmanı istiyorum. Kendin birebir Horizons'ı oluştur, öncekiyle alakası olmasına gerek yok. Bir de AI generated görseller biraz kalitesiz duruyor, onları da düzelt. Yerine daha profesyonel, gerçek insan resimleri koy. Nano Banana 2'yi kullan … Hatta birebir aynılarını Nano Banana image edit ile oluştur. Bunun için de Fal kullan." ("Horizons" referans sitenin adı gibi görünüyor; altyazıda belirsiz.)
- Skill metni: Videoda yok (frontend-design'ın içeriği okunmuyor). Vaat edilen MD dosyası açıklamada yok.

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- "Vibe coded gibi durmasın istiyorsanız UI kit / skill kullanın" → frontend-design skill zorunlu.
- **Referans site kopyalama**: beğenilen sitenin tüm sayfaları PDF/print ya da DevTools layout + stil kodu olarak verilir; "birebir aynısını yap".
- Gerçek görsel üretimi: Nano Banana 2 + image edit ile "AI kokan" görseller yerine gerçek insan fotoğrafı gibi görseller; Veo 3 ile video; animasyonlu logo; açılış animasyonu.
- 21st.dev: "Yapay zeka her zaman kısa yolu düşünür"; siz belirli bir açılış/animasyonu seçip prompt'unu kopyalayıp verirseniz "yarışmaya katılabilecek" seviyede site.
- Çok dil (TR/EN/NL), çok sayfa (sektör, endüstri, blog, case study) — içerik zenginliği profesyonellik algısı.
- Font/renk kuralı: Videoda yok.

## Hatalar ve çözümleri
- **Claude "canlıya aldım" diyor ama almamış** → Vercel Deployments'ta son tarih kontrol; "son deployment iki gün önce" diye somut kanıtla geri bildirim; GitHub'ı önce, Vercel'i sonra güncelle dedirt.
- **Push edildi ama Vercel doğru sürümü almadı** (yeni paket sürümlerinde patlayabiliyor) → Vercel'den manuel redeploy.
- Belki yanlış projeyi güncelledi → Vercel'de diğer projelerin deployment tarihlerine bakarak doğrula.
- Claude zaman zaman hata alıp kendisi tekrar deniyor (bypass modunda müdahale gerekmiyor).
- Fal maliyet patlaması riski → görsel/video sayısı limiti.
- Dosyaları sadece yerelde tutmak riskli ("buluta koyduğunuzda patlayabilir") → her şey GitHub'a.
- AI görselleri kalitesiz → Nano Banana 2 image edit ile yeniden.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- **Fiyat referansı:** "Çok profesyonel insanların normalde **2.000 $, 3.000 $** para istedikleri web sitelerini siz basit bir şekilde yapabilirsiniz." (Kendi fiyatını söylemiyor.)
- Kendi maliyet notu: Webflow'a para veriyordu, artık vermiyor. Fal.ai: kontrolsüz 100 istek ≈ **100 $** (limit koy: maks 30 görsel / 10 video).
- **Lead bulma:** Apify Store → Google Maps scraper → kategori + semt (örn. "İstanbul Kadıköy" restoranlar) → e-postalar (web sitesinden de bulunuyor) + web sitesi olan/olmayan ayrımı. Web sitesi olmayana yeni site, olana "güncellemek ister misiniz" teklifi.
- **Kanal:** e-posta (kısa e-postalar), açıklamada Instantly linki.
- **Huni rakamları:** dönüş oranı **%7–12**; cevapların yarısı olumlu yarısı olumsuz; **%7–8 dönüşte her 100 kişiden ~4 toplantı**; **10 toplantıdan 1 satış** bile yeterli; "bir haftalık bir işle ilk projenizi bağlayabilirsiniz".
- Sonrası: DOA topluluğu (skool.com/doa/about), webinar (otomasyonkur.com/webinar-burhan) — kendi eğitim/yatırım modelinin reklamı.

## Bizim fabrikaya alınacaklar (somut)
1. **Proje MD'si (CLAUDE.md/brief) şablonu** — her müşteri projesi şu bölümlerle başlar: işletme bilgisi, referans site(ler), kullanılacak skill'ler (frontend-design), görsel/video üretim aracı + model adı, **Fal bütçe limiti (maks N görsel / M video)**, deploy hedefi (GitHub repo URL + Vercel), "işin bittiğinde canlı URL'yi ve deployment zamanını raporla".
2. **Müşterinin mevcut sitesini PDF'leyip içerik kaynağı yapma** — tüm sayfalar print→PDF (veya scrape) → Claude metinleri yeniden kullanır; "güncelleme" teklifinde birebir işe yarar.
3. **Referans site → layout/stil kopyalama adımı** — beğenilen premium site için DevTools'tan layout+CSS ya da PDF; "birebir aynısını yap, öncekiyle alakası olmasın" prompt kalıbı.
4. **Görsel kalite kuralı:** AI görsel çıktısı "AI gibi" duruyorsa Nano Banana 2 image edit ile gerçekçi insan fotoğrafı; video için Veo 3/Kling — model adını mutlaka prompt'ta belirt.
5. **Deploy doğrulama checklist'i:** Claude "deploy ettim" dese bile Vercel Deployments tarihini kontrol et; tutarsızsa "GitHub'ı güncelle → Vercel'i güncelle" + gerekirse manuel redeploy. (Otomatik: `vercel ls` / deployment zamanı kontrol scripti.)
6. **Çok dilli çıktı** varsayılanı (TR + EN; turizm bölgeleri için + DE/NL/RU) — yerel işletmeye ek değer.
7. **Lead pipeline:** Apify Google Maps scraper → semt+kategori → "web sitesi yok" ve "web sitesi eski" iki segment → kısa cold e-posta (Instantly). Hedef KPI: %7–12 yanıt, 100 e-posta → ~4 toplantı, 10 toplantı → 1 satış; ilk hedef her hafta 250–300 e-posta.
8. **Fiyat çapası:** müşteriye "ajansların 2.000–3.000 $ istediği site" çapasını kullan.
9. **21st.dev bileşeni ile "imza" hero/açılış** — her sitede en az bir seçilmiş animasyon; AI'ı kısa yoldan alıkoymak için bileşeni biz seçeriz.
