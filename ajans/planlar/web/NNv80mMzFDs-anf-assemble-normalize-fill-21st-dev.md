# Stop Making Ugly Websites with Claude Code — Ed Hill | AI Automation — 10 dk — https://www.youtube.com/watch?v=NNv80mMzFDs

## Tek paragraf: ne yapıyor, sonuç ne
Ed Hill, "Qovo Robotics" (ev robotu) için aynı brief'i 3 seviyede deniyor: (1) düz prompt → çok metin, jenerik layout, her yerde mor (Tailwind'in eski varsayılan Indigo-500'ü; kurucusu bunun için "özür diledi"), 3'lü grid; (2) Anthropic **frontend-design** plugin'i → mor gidiyor, metin iyileşiyor ama hâlâ 3'lü grid ve "premium" his yok; (3) kendi **ANF çerçevesi** (Assemble → Normalize → Fill): 21st.dev'den gerçek tasarımcıların yaptığı bileşenlerin "Copy Prompt" çıktısını `components/1.txt, 2.txt…` dosyalarına koyup Claude Code'a "sırayla bu bileşenlerden site yap" → sonra tek bir "normalize" prompt'uyla farklı kaynaklardan gelen parçaları tek site gibi uyumlaştır (font/renk/spacing) → sonra "research" prompt'u (sektörü araştır) + "fill" prompt'u ile jenerik içeriği (yorumlar, fiyat, SSS) gerçekçi sektör içeriğiyle doldur. Sonuç: light/dark modlu, insan tasarımı bileşenlerden oluştuğu için "AI yapmış" görünmeyen site; birkaç dakikada.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- VS Code + Claude Code eklentisi (Pro 20 $/ay, yoğun kullanımda Max); Whisper Flow (sesle yazma)
- **frontend-design plugin** (`/plugins` → Manage Plugins → "frontend" → install; kaynağı görülebilir) — metin: `kaynaklar/VMvZuhcDdnw-frontend-design-ve-ui-ux-pro-max.md`
- **21st.dev** — bileşen kütüphanesi; "Copy Prompt → Prompt Type: Claude Code"
- Ed'in normalize / research / fill prompt'ları + kullandığı bileşen listesi: https://edhill.ai/video-resources/NNv80mMzFDs (e-posta kapılı; metin açıkta değil, mantığı aşağıda yeniden yazıldı)
- Dev server: `npm run dev`

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. **Seviye 1** (karşılaştırma için): `Build me a landing page for Qovo Robotics. We sell a personal home robot called Qovo that follows voice commands, carries things, and learns your daily routines. Do not use any skill.` → HTML'i Chrome'a sürükle → mor + 3'lü grid + fazla metin.
2. **Seviye 2**: frontend-design plugin'ini kur, aynı prompt (skill cümlesi olmadan) → Claude skill'i otomatik seçiyor → daha iyi ama şablon hissi.
3. **Seviye 3 / Assemble**: yeni klasör → 21st.dev'de beğendiğin hero, özellikler, yorumlar, fiyat, SSS, footer bileşenlerini bul → her birinin "Copy Prompt"ını `components/1.txt … n.txt` olarak kaydet → `Create a website with all the components inside the components folder, in order.` → `npm run dev`.
4. **Normalize**: tek prompt'la bütün sayfayı tek tasarım diline çek (tek font ailesi, tek palet, tutarlı spacing/radius/gölge, light+dark mod, yan kaydırma gibi hataları temizle).
5. **Fill**: yeni oturum → research prompt'u ("siteyi tara, doldurulması gereken alanları çıkar, robotik sektörünü araştır") → fill prompt'u ("araştırmaya göre tüm metinleri/yorumları/fiyatları/SSS'yi doldur") → ürün adı Apex, gerçekçi yorumlar ("10.000 saat, sıfır kaza"), sektöre uygun fiyat/SSS.
6. Kalan küçük hatalar (font, side-scroll) → tek tek düzelt.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Brief: yukarıda (1). Assemble: `Create a website with all the components inside the components folder in order.`
- Normalize/Research/Fill prompt'larının tam metni e-posta kapılı. Bizim yeniden yazımımız:
  - Normalize: `Bu sayfadaki bileşenler farklı kaynaklardan geldi. Hepsini tek bir tasarım sistemine normalize et: tek başlık + tek gövde fontu, 4-5 renklik tek palet (mor yok), tutarlı spacing/radius/gölge ölçeği, light ve dark mod, yatay taşma yok. İçeriği değiştirme.`
  - Research: `Siteyi baştan sona tara; placeholder/jenerik olan her metni (başlık, yorum, fiyat, SSS) listele. Sonra <sektör> hakkında web araştırması yap: gerçek ürün özellikleri, fiyat aralıkları, müşteri endişeleri, SSS. Bulguları research.md'ye yaz.`
  - Fill: `research.md'ye dayanarak sitedeki bütün placeholder içeriği gerçekçi, sektöre özgü metinle doldur. Yorumlar somut ve ölçülebilir olsun, fiyatlar sektöre uygun olsun. Tasarımı değiştirme.`

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler
- **İnsan tasarımı bileşenler** (21st.dev) → gölge/animasyon/detay zaten çözülmüş; AI sadece monte ediyor.
- **Normalize adımı** → parçalar tek site gibi (en çok atlanan adım).
- **Fill adımı** → jenerik "Lorem/John Doe" içerik gitmiş; sektör araştırmasına dayalı somut copy.
- Mor/Indigo ve 3'lü grid'den bilinçli kaçış; light/dark mod.

## Hatalar ve çözümleri
- Mor her yerde → Tailwind varsayılanı; skill veya normalize ile yasakla.
- 3'lü grid şablonu → skill tek başına yetmiyor; bileşen tabanlı montaj.
- Bileşenler uyumsuz (fontlar vs.) → normalize prompt'u.
- İçerik jenerik → research + fill.
- Yan kaydırma / font beğenmeme → tek tek düzeltme.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Yok (Skool topluluğu reklamı).

## Bizim fabrikaya alınacaklar (somut)
1. **ANF = fabrika web hattının omurgası**: `components/` klasörü + sektör başına önceden seçilmiş 21st.dev bileşen setleri (restoran, klinik, berber…), tek "assemble" komutu.
2. **Normalize prompt'u** standart ikinci adım (DESIGN.md/marka skill'iyle birlikte).
3. **Research + Fill** → müşteri bilgisi + sektör araştırmasıyla içerik; "John Doe" yasağı.
4. Her müşteri sitesinde light/dark mod kontrolü ve yatay taşma testi.
