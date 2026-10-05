# The 7 Levels of Building ELITE Websites with Claude Code — Chase AI — 38 dk — https://www.youtube.com/watch?v=1PXFAFMgdns

## Tek paragraf: ne yapıyor, sonuç ne
Chase, "Argus" adlı hayali bir SaaS (içerik üreticileri için sosyal medya trend istihbaratı) landing page'i üzerinden Claude Code ile front-end tasarımda 7 seviyelik bir yol haritası anlatıyor: (1) çıplak prompt → mor gradient'lı AI slop, (2) skill'ler (frontend-design + UI UX Pro Max) → "tasarlanmış ama hâlâ AI şablonu", (3) görsel referans ekran görüntüleri (Awwwards/godly.website/Pinterest/Dribbble) → "%50 yaklaşıyor", (4) **site teardown**: beğenilen sitenin HTML+CSS+JS kaynağını Claude'a verip klonlatma ve "bunu nasıl yapmışlar" diye öğrenme, (5) kendi bileşenleri ve varlıkları (21st.dev bileşenleri, Midjourney ile üretilmiş hero görseli, Kling/Veo ile 15 sn subtle video), (6) dış görsel araçlarla (Stitch, pencil.dev, paper.design, Figma) yeniden tasarım iterasyonu + mikro dokunuşlar (yükleme gecikmesi, font, ticker, sayaç animasyonu, scroll progress bar), (7) WebGL/shader/3D "frontier" (henüz erişilemez, Awwwards SOTD seviyesi). Sonuç: 20 dakikalık tinkering sonrası karakteri olan, video hero'lu, glassmorphism kartlı, mikro-animasyonlu Argus sitesi.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code (plan mode ile soru-cevap), `/plugin marketplace add` + `/plugin install` ile skill kurulumu (ya da URL'yi yapıştırıp "install this skill")
- **UI UX Pro Max** (52K yıldız): https://github.com/nextlevelbuilder/ui-ux-pro-max-skill ; **frontend-design** (Anthropic): https://github.com/anthropics/skills/tree/main/skills/frontend-design — ikisi de `kaynaklar/VMvZuhcDdnw-frontend-design-ve-ui-ux-pro-max.md`
- **Site teardown skill** (Chase'in kendi skill'i; CSS/JS dosyalarını WebFetch özetine düşmeden tam çeker) — ücretsiz Skool topluluğunda: https://www.skool.com/chase-ai-community (kapalı; metni açık değil)
- İlham: Awwwards (SOTD/SOTM), godly.website, Pinterest ("SaaS landing page"), Dribbble; örnek site: OpenHands (all-hands.dev)
- Bileşen kaynakları: 21st.dev ("copy prompt"), CodePen, Monet
- Görsel: Midjourney v7 (konsept-art tarzı hero), alternatif Nano Banana Pro / Seedream; video: Kling 3.0 / Veo 3.1 (start+end frame, 15 sn subtle loop); upscale Midjourney'de
- Dış tasarım araçları: Google **Stitch** (ücretsiz; SS ver → redesign → variants), pencil.dev (Cursor/VS Code içinde canvas), paper.design, Figma
- Google Fonts (font seçimi)
- Claude Code **WebSearch** ile "best web design practices" aratıp premium dokunuş önerisi aldırma
- Ücretli: Chase AI Plus / Claude Code Masterclass

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. **L1**: Plan mode'da düz prompt → Claude tech stack (Next.js/HTML/Astro), hedef (waitlist + ürün vitrini), stil (dark&techy / clean&minimal) soruyor → sonuç mor gradient'lı generic sayfa. Ders: bilmediğin terimi Claude'a sor, tasarım sözlüğü biriktir.
2. **L2**: `/plugin marketplace add nextlevelbuilder/ui-ux-pro-max-skill` → install. Prompt: `/ui-ux-pro-max  I want you to recreate the landing page using the skill. Ask me any questions you need.` → tipografi önerisini kabul, CTA rengi turuncu (mor yasak). Sonuç belirgin daha iyi ama hâlâ "tasarlanmış AI şablonu".
3. **L3**: OpenHands sitesinin birçok SS'ini al → `Actually, I want our website to match the style of Open Hands. [URL] Here's some screenshots for reference.` → %50 yaklaşır; hero'yu SS ile tekrar tekrar düzeltme tuzağına düşme.
4. **L4 (teardown)**: Sitede Ctrl+U → tüm HTML'i kopyala (1152 satır) → Claude'a yapıştır + `Take a look at the CSS and JS files as well. Use the site teardown skill and use this info to better clone the original Open Hands site.` → çok daha yakın klon. Sonra öğren: `How is the background working? How can we match that effect?` (mouse ile silinen arka plan efekti vb.). Her klon = sözlük + teknik kazanımı.
5. **L5**: 21st.dev'den buton/carousel/nav prompt'u → `Let's integrate this button design.`; Claude ile tagline ("See what's next", Argus = 10.000 gözlü mitolojik figür) ve görsel hikâye fikirleri → Midjourney'de `I need a background image that will be the landing page for a website called Argus. The tagline is "see what's next".` → seçilen görselle `I want to change the front end entirely. Use the following image as the background for the hero. Put the info about Argus on the left-hand side, leave negative space on the right to show off the image, and make the tagline "see what's next".` → upscale → Kling/Veo ile start-frame (+end-frame) 15 sn subtle video → `Use the upscaled image for mobile, replace the hero with this video on desktop.` Kartlar vb. diğer bölümler için aynı döngü: ilham → Claude → iterate.
6. **L6**: Stitch'e mevcut sitenin SS'leri → `I really like the hero and the colors, but update the entire bottom half; it feels flat; bring the imagery and colors from the top.` → üretilen tasarımı sağ tık kopyala → Claude Code'a `What do you think about this glassmorphism effect with the image in the background? Let's try that out.` → 20 dk tinkering: yükleme gecikmesi (ağırlık hissi), font değişimi, scroll bölümü, video→görsel geçişinde ticker şeridi (doğal sınır), glass kartlar "let's give them some weight, make them pop off the page", 0→10M sayaç animasyonu, metin üstünde ışık süpürmesi, scroll progress bar. `Do a web search for the best web design practices for making these cards pop, and come up with other things that make the website look more premium in a subtle fashion.` → fikir listesi, bazıları alınır.
7. **L7**: WebGL/shader/3D (ör. igloo.inc) — izlemelik; Awwwards SOTD.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- `Let's create a landing page for Argus, my social media web app, that acts as an intelligence app for content creators to find and identify trending topics in their niche.` (L1, plan mode)
- `/ui-ux-pro-max — I want you to recreate the landing page using the skill. Ask me any questions you need.`
- `Actually, I want our website to match the style of Open Hands. Here's some screenshots for reference.`
- `Here's the HTML for Open Hands. Take a look at the CSS and JS files as well. Use the site teardown skill and use this info to better clone the original Open Hands site.`
- `How is the background working? How can we match that effect?`
- `I actually want to change the front end entirely. I want to use the following image as the background for the hero. Let's put the info about the app Argus on the left-hand side, leave some negative space on the right to show off the image, and make the tagline "see what's next".`
- `Let's give them some weight. Let's have them pop off the page.` / `Do a web search for the best web design practices … come up with some other things that can make our website look a bit more premium yet in a subtle fashion.`
- Site teardown skill'in kendisi paylaşılmadı (Skool'da). Mantığı: WebFetch küçük modelle özetler → CSS/JS'i tam olarak çekip ver (bizde: `curl` ile dosyaları indirip projeye koymak aynı işi görür).

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler
- **Tat sorunu = kelime dağarcığı sorunu**: tasarım sözlüğü olmadan prompt yazılamaz; görsel referans + teardown bu açığı kapatır.
- **Göster, anlatma**: SS'ler; ama SS→kod çevirisi kayıplı → **kaynak kodu ver** (HTML + CSS + JS) → birebir yakın klon, sonra kendi spin'in.
- **Görsel hikâye anlatımı**: ürüne bağlı tagline + ona bağlı konsept-art hero (Midjourney), arka planda subtle 15 sn video, mobilde statik görsel.
- **Bölüm bölüm iterasyon**: her bölüm için ilham → bileşen → Claude → iterate.
- **Mikro dokunuşlar** (sayaç, ışık süpürmesi, yükleme gecikmesi, ticker, progress bar, glass kartlara ağırlık) → "umursanmış" hissi; tek tek fark edilmez, toplamı karakter yaratır.
- Mor gradient ve generic kart grid'inden kaçış; CTA rengini bilinçli seç.
- Stitch gibi görsel araçla yarım saatlik ideasyon → fikirleri Claude'a geri taşı.
- Font: Google Fonts'tan bilinçli seçim; Claude hepsini kullanabilir.

## Hatalar ve çözümleri
- Düz prompt → generic slop → skill + referans + teardown.
- SS ile hero'yu defalarca düzeltme döngüsü (asla tam oturmaz) → kaynak kodu teardown.
- WebFetch CSS/JS'i özetler → teardown skill / dosyaları tam indir.
- Video loop'ta kesinti → subtle hareket + 15 sn uzunluk (kullanıcı sonunu görmez) + end-frame kullan.
- Mobilde video performansı → mobilde statik upscaled görsel.
- Video hero → görsel arka plan geçişi sert → arada ticker şeridi (doğal sınır).
- Stitch glass kartları Claude'da düz çıktı → "weight / pop off the page" prompt'u + web search ile en iyi pratikler.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Yok. (Kendi eğitim ürünü: Chase AI Plus.)

## Bizim fabrikaya alınacaklar (somut)
1. **Seviye modeli fabrika standardı olsun**: hiçbir müşteri sitesi L1-L2'de teslim edilmez; minimum L5 (kendi varlıkları + bileşen), premium paket L6.
2. **Teardown adımı**: her sektör için 2-3 "örnek alınan site" seç, HTML/CSS/JS'i indirip `referanslar/` altına koy; Claude'a "bunu nasıl yapmışlar" diye teknik sözlük çıkarttır (GSAP, scroll-trigger, maske efektleri…). Bizim teardown skill'imizi yazalım (curl ile CSS/JS tam indirme).
3. **Görsel hikâye şablonu**: işletme → tagline → hero konsept görseli (Nano Banana/Midjourney) → 15 sn subtle video (Kling) → mobilde statik.
4. **Mikro dokunuş listesi** (sayaç, ışık süpürmesi, ticker, progress bar, glass ağırlık, yükleme sekansı) → polish checklist'ine.
5. **Stitch** ile alt bölümlerin redesign'ı ideasyon aracı olarak süreçte.
6. "Web search ile premium pratik öner" prompt'u → polish turunun standart adımı.
