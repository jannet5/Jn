# Claude Code + Nano Banana 2 + Kling = $15K Animated Sites — Nick Saraev — 14 dk — https://www.youtube.com/watch?v=ZfYvv-0l9NA

## Tek paragraf: ne yapıyor, sonuç ne
Nick, 15 dakikada dört adet "3D scroll efektli" tanıtım sitesi (kulaklık, orman/globe, iç mimarlık, uzay istasyonu) üretiyor. Formül üç adım: (1) Claude Code'a birkaç madde + **taste-skill** (Leon Lin'in açık kaynak anti-slop tasarım skill'i) ile tek seferde siteyi ürettir, (2) Kling 3.0 (Higgsfield üzerinden) ile 5 saniyelik "exploded view / dönen globe / pan" videosu üret, (3) videoyu hero arka planı ve scroll'a bağlı kare-kare animasyon olarak siteye gömdür, Netlify'a ücretsiz yayınla. Toplam maliyet 3-5 $ token + ~1 $ video. Sonuç: canlı örnek atelieramerin.netlify.app (iç mimarlık sitesi, hero'da pan videosu, altında scroll ile patlayan ev animasyonu).

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **taste-skill** (Leonxlnx): https://github.com/Leonxlnx/taste-skill — `npx skills add https://github.com/Leonxlnx/taste-skill`. Metni: `ajans/arastirma/youtube/kaynaklar/ZfYvv-0l9NA-taste-skill.md`
- **Claude Code** (Antigravity IDE içinde çalıştırıyor; herhangi bir IDE/terminal olur)
- **Kling 3.0** (Higgsfield platformu, 7.5 kredi ≈ 0.36 $ / 5 sn 1080p video): https://bit.ly/higgsfield_ai_kling3
- **Nano Banana (Pro)**: önce globe gibi bir referans görseli üretip Kling'e besliyor
- **Netlify** ücretsiz hosting (Claude Code "make it live on Netlify" deyince kendisi deploy ediyor)
- Claude Code'un kendi seçtiği teknik: video → kareleri optimize JPEG'e çıkar → scroll pozisyonuna bağla (canvas frame sequence) + preload

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Claude Code'a skill linkini verip tek prompt: "Use this skill to design a high-end website about interior design." → ~2 dk bekle, tek seferde site çıkıyor.
2. Higgsfield → Kling 3.0, 5 sn, 16:9, 1080p. Prompt: pan videosu (aşağıda). 2-3 kez üretip en iyisini seç.
3. İndirilen mp4'ü `interior_design.mp4` diye adlandır; Claude Code'a: dosyayı hero arka planı yap, hero'yu ortala, "inward masking gradient" uygula (video kenarları site arka planına erisin).
4. Ekran görüntüsü alıp geri besleme: metin okunmuyor, alt metin küçük → düzelt.
5. Kling'de ikinci video: evin "exploding view" animasyonu (prompt aşağıda). İndir → `interior_design_exploding_view.mp4`.
6. Claude Code'a: hero'nun hemen altına scroll animasyonu; 2-3 metin bölümü akarken video kare kare ilerlesin.
7. Sorunlar: gradient zayıf (renk sınırı görünüyor) + lag → "gradient'ı güçlendir, lag'i düzelt, çok daha hızlı yüklensin" → Claude kareleri JPEG'e çıkarıp preload ekliyor.
8. "Hero'yu da hızlandır" → 5.3 MB video 252 KB'a sıkıştırılıyor.
9. "Make it live on Netlify" → canlı link.
10. Mobil: "mobile optimize the site" 3-4 kez.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Site: `Use this skill to design a high-end website about interior design.` (+ skill repo linki)
- Kling (hero): `Generate me a high-quality 3D render style video of panning through a 3D interior design scene. Make the background white. Make the assets super high-quality. This should read like something you'd see on a website or a landing page.`
- Kling (globe): `globe rotating in the exact same place, center of mass should not move, just rotating perfectly on its axis` (+ Nano Banana'da üretilmiş globe görseli)
- Kling (exploded view): `Generate me a high-quality exploding view animation of a home to showcase the interior design component of a service. No text, white background. It should explode in all directions including vertically and horizontally. And none of it should go outside of the bounds of the video itself.`
- Entegrasyon: `Take interior_design.mp4 (in Downloads) and make it the background of our hero header. Center the hero header so it looks clean, and apply an inward masking gradient so the animation background doesn't interfere with the website background.`
- Scroll: `I have interior_design_exploding_view.mp4 in Downloads. Create a scroll animation immediately underneath the hero header. As I scroll through, we should go through two or three sections of text that expose our design skills and it should show the exploding view frame by frame as we go.`
- Düzeltme: `Two problems: the fuzzy gradient is not strong enough so we see dividing colors between top and bottom; make the gradient stronger on top and bottom. Second, it's really laggy; fix the lag, make it load significantly faster.`
- `Add some sort of overlay to the text, it collides with the animation.` / `Make the hero header faster as well, it's pretty big.` / `This looks awesome. Make it live on Netlify.`
- taste-skill'in ana kuralları (özet): önce "Design Read" (tek satır: sayfa türü + kitle + vibe), üç kadran (DESIGN_VARIANCE / MOTION_INTENSITY / VISUAL_DENSITY, varsayılan 8/6/4), Inter + slate-900 + mor gradient + 3 eşit kart + ortalanmış hero yasak, em-dash yasak, eyebrow en fazla 1/3 bölüm, hero'da max 4 metin öğesi, gerçek görsel şart, `window.addEventListener("scroll")` yasak (Motion/GSAP ScrollTrigger kullan), reduced-motion zorunlu.

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler
- **Skill = zevk standardı**: spacing, lüks görünüm, tipografi, renk kuralları skill'den geliyor; tek prompt'la bile düzgün çıkıyor.
- **Gerçek hareketli görsel**: AI üretimi 5 sn video (beyaz fon, "landing page'de görülecek gibi" ifadesi) hero'da; CSS blob değil.
- **Maskeleme gradient'ı**: video kenarlarını sayfa rengine eriterek "yapıştırılmış video" hissini yok ediyor.
- **Scroll-bağlı kare animasyonu** (Apple AirPods tarzı): video → JPEG kareler → scroll pozisyonu. Lag'i çözmek için sıkıştırma + preload.
- **Metin üstüne overlay/gölge**: animasyonla çakışan metin için.
- **Ekran görüntüsü ile geri besleme döngüsü**: gördüğün kusuru SS ile ver, 1 cümle düzeltme.
- Video prompt'larında "no text, white background, stay within bounds" kuralları (metin bozuk çıkmasın, kadraj taşmasın).

## Hatalar ve çözümleri
- Hero metni okunmuyor → metni belirginleştir, alt metni büyüt, overlay ekle.
- Video ile sayfa arka planı arasında renk sınırı → maske gradient'ını güçlendir (üst + alt).
- Scroll animasyonu takılıyor/laggy → kareleri optimize JPEG'e çıkar, scroll'a bağla, preload; 2-3 kez "daha hızlı" de.
- Hero videosu ağır (5.3 MB) → sıkıştır (252 KB).
- Kling çıktısı tutmazsa → aynı prompt'u 2-3 kez paralel üret, en iyisini seç.
- Mobil optimize edilmemiş → "mobile optimize" komutunu birkaç kez tekrarla.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Eskiden 5-10K $'a sattığını söylüyor (web ajansı geçmişi). Şimdi maliyet: ~2-3 $ token (siteyi) + 1 $ Claude kullanım + video başına ~0.36 $ (Higgsfield Pro 29 $/600 kredi). Satış süreci anlatmıyor; "15K animated site" başlık iddiası.

## Bizim fabrikaya alınacaklar (somut)
1. **taste-skill'i fabrika skill'i olarak kur** (`npx skills add https://github.com/Leonxlnx/taste-skill`); DESIGN_VARIANCE/MOTION/DENSITY kadranlarını müşteri tipine göre preset'le (restoran = premium consumer 7/6/3, klinik = trust-first 3-4/2-3/4-5).
2. **Video varlık hattı**: Nano Banana ile ürün/mekân referans görseli → Kling/Higgsfield ile 5 sn beyaz fonlu "pan / rotate / exploded view" videosu; standart prompt şablonları yukarıda.
3. **Entegrasyon prompt şablonu**: hero video + inward mask gradient; altına scroll-bağlı frame sequence (JPEG kareler + preload).
4. **Kalite döngüsü**: SS → "metin okunmuyor / gradient zayıf / lag" → düzelt; en sonda "daha hızlı yükle" x2 ve "mobile optimize" x3.
5. **Yayın**: Netlify (ücretsiz) — Claude Code'a "make it live on Netlify".
6. taste-skill'in "AI tells" listesi (bölüm 9) → bizim anti-slop kontrol listesine aynen alınacak.
