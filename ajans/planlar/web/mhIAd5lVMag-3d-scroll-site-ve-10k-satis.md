# Everyone Builds 3D Websites with Claude Code. I Sell Them for $10K. — AI Chris Lee — 10 dk — https://www.youtube.com/watch?v=mhIAd5lVMag

## Tek paragraf: ne yapıyor, sonuç ne
Chris Lee, 20 dakikada "3D scroll" landing page üretip bunu yerel işletmelere 3.000-10.000 $'a nasıl sattığını anlatıyor. Teknik: Nano Banana 2 (Google AI Studio, ücretsiz) ile **iki görsel** (ürünün montajlı hali, beyaz fon, 2048 px; aynı açı/ışıkla "exploded" hali, ilk görsel referans verilerek) → Kling 3.0'da start-frame + end-frame ile 3-5 sn geçiş videosu → Claude Code'a "scroll'a bağlı kare animasyonu (three.js), aşağı inince ileri yukarı çıkınca geri" prompt'u → 3 prompt'luk düzeltme → Vercel'e tek tıkla deploy. Aylık maliyet 40 $ (Claude Pro 20 + Vercel Pro 20). Satış: Instagram'da 500-5.000 takipçili, 2015 görünümlü siteli yerel işletmeleri bul → **önce iş yap** (20 dk'da onların logosu/renkleriyle ana sayfa) → 30 sn Loom'da yan yana göster → DM: "ana sayfa bedava, sadece referans istiyorum" → tam site + ek sayfalar + bakım ücretli. Soğuk kanal: Google Maps'ten 50 işletme, before/after SS ile 3 cümlelik e-posta (kişisel e-posta %10,5 yanıt). Fiyat: tek sayfa 5.000 $, çok sayfa/çok 3D bölüm 5-10K $ (ajans three.js fiyatlarına çapa: 5-15K $, 3-8 hafta). Sınırlar: statik site; CMS/blog/e-ticaret/login yok — SaaS ve e-ticarete satma, hizmet işletmelerine (restoran, diş kliniği, emlakçı, spor salonu, berber) sat.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code (VS Code eklentisi, Pro 20 $/ay)
- **Nano Banana 2** (Google AI Studio ücretsiz, ~600-750 görsel/ay; API 0,045-0,067 $/görsel)
- **Kling 3.0** (start+end frame); alternatif Runway ML, Pika
- three.js (Claude yazıyor), Vercel (Pro 20 $/ay müşteri işi için), Loom (30 sn demo), Instagram DM + Google Maps + e-posta
- Chris'in programı: Execution Squad / ücretsiz eğitim https://aichrislee.com/live

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Görsel 1: `Generate a product photo of <ürün>, assembled, clean white background, 2048 resolution.` (çözünürlüğü yaz: fark gece-gündüz.)
2. Görsel 2: görsel 1'i referans olarak ver → `Same product, same angle, same lighting, exploded view: all parts separated and floating (Apple-style teardown).`
3. Kling 3.0: start frame = görsel 1, end frame = görsel 2 → 3-5 sn video.
4. Claude Code: `Build a scroll-driven frame animation website using three.js. The video plays forward as the user scrolls down and reverses when they scroll up. Add a hero headline and a clean gradient background.`
5. Düzeltme 2: `Slow down the scroll speed by 40%. Move the headline to center-left and change the background to a dark gradient.` Düzeltme 3: `Add a subtle parallax effect to the headline on scroll.` (~8 dk, 3 prompt)
6. Vercel: VS Code'dan tek tık deploy, ~60 sn; müşterinin domaini 1 dk.
7. Satış (sıcak): Instagram → yerel işletme, 500-5K takipçi, bio'da eski site → 20 dk'da ana sayfasını yeniden yap (logo/renk/ürün fotoğrafı) → Loom (mevcut site | yeni site yan yana) → DM.
8. Satış (soğuk): Google Maps → kategori → eski görünümlü siteler → 50'lik liste → her biri için before/after SS (tam site gerekmez) → 3 cümlelik e-posta → 50'de ~5 yanıt → 1 evet.
9. Fiyat çapası: ajans three.js projesi 5-15K $ ve haftalar; sen aynı görsel kaliteyi günler içinde, daha ucuza.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Teknik prompt'lar yukarıda (4-5).
- Sıcak DM: `Hey, I saw your website and rebuilt a version of your homepage using 3D animation. Took me 20 minutes. Here's what it looks like. [Loom] If you want it, it's yours. No charge for the homepage. I just want a testimonial.`
- Soğuk e-posta: `I rebuilt your homepage with 3D animation. Here's a side-by-side comparison. If you're interested, I'll send over the full version for free. I just need a testimonial.`

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler
- 2K çözünürlük ve iki görsel arasında tutarlılık (referans görsel) → temiz "Apple teardown" hissi.
- Start/end frame ile kontrollü video → scroll'a bağlı ileri/geri oynatma = "50K $'lık interaktif 3D" algısı.
- Koyu gradient arka plan, başlık sol-orta, hafif parallax; scroll hızını yavaşlatma.
- Müşterinin gerçek logosu/renkleri/ürün fotoğrafı ile kişiselleştirilmiş demo.

## Hatalar ve çözümleri
- İlk sürümde scroll hızı, başlık konumu ve "hastane bekleme odası" arka planı → 2 düzeltme prompt'u; mükemmel ilk deneme yok.
- Kling yavaş/yoksa → Runway/Pika aynı mantık.
- "Size site yapabilirim" DM'i %99 cevapsız → önce iş yap, demo göster, ana sayfayı bedava ver.
- Yanlış müşteri (SaaS, e-ticaret) → statik sınırları dürüstçe söyle; hizmet işletmelerine odaklan.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- **Tek sayfa landing: 5.000 $; çok sayfa + çoklu 3D bölüm: 5.000-10.000 $** (genel aralık 3-10K $). Aylık maliyet 40 $. Ana sayfa bedava (referans karşılığı) → tam site, ek sayfalar, özel animasyonlar, bakım ücretli. Pazar: her 4 yerel işletmeden 1'inin sitesi yok; olanların yarısının mobil yüklenmesi >3 sn. Hedef: ayda 1 müşteri 3K = 36K $/yıl; 3 müşteri × 5K = junior developer maaşı üstü.

## Bizim fabrikaya alınacaklar (somut)
1. **Demo-önce satış hattı**: Instagram/Google Maps tarama → 20 dk'lık kişiselleştirilmiş ana sayfa → Loom yan yana → DM/e-posta şablonu (Türkçeleştir: "Ana sayfa bizden, sadece yorum istiyoruz").
2. **3D scroll hero paketi**: 2 görsel (montajlı + exploded, 2K, referanslı) + Kling start/end frame + three.js scroll-scrub prompt'u + 2 standart düzeltme prompt'u.
3. **Fiyat listesi çapası**: tek sayfa / çok sayfa / bakım; ajans fiyatlarına referans; statik sınırlarını teklifte açıkça yaz.
4. Hedef segment listesi: restoran, berber, diş kliniği, emlakçı, spor salonu (SaaS/e-ticaret hariç).
5. Operasyon maliyeti tablosu (Claude Pro + Vercel Pro + ücretsiz Nano Banana) → marj hesabı teklif dosyasına.
