# How to Use Claude Design To Make Sites 10X More Beautiful — Mikey Website — 23 dk — https://www.youtube.com/watch?v=y2n1NMrMNBo

## Tek paragraf: ne yapıyor, sonuç ne
Mikey, Claude Design'da (sol chat, sağ canvas; Pro/Max/Team/Enterprise research preview) bir kahve dükkânı landing page'ini "make me a landing page for a coffee shop" gibi tek cümlelik prompt'tan çıkan jenerik sürümden, 4 aşamalı bir akışla "niyetli ve profesyonel" sürüme taşıyor: (1) **spesifik prompt** (hedef + layout + içerik + kitle), (2) **chat ile büyük değişiklikler** (bölüm sırası, görsel yön: "warmer, more editorial, richer tones, generous spacing"), (3) **inline comment** ile eleman bazlı ince ayar (CTA butonu, kart padding/köşe, hero başlık), (4) **varyasyon**: mevcut sürümü kaydettirip hero için 3 alternatif layout üretip karşılaştırma (split / centered / inset frame + ürün kartı) → en güçlüsünü seç. Sonra export (zip, PDF, PPT, Canva, standalone HTML, **Claude Code handoff**) ve paylaşım linki. Ders: ilk üretim ürün değil ham madde; en büyük fark prompt'ta, sonra yapı → detay → alternatif sırası.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Design (claude.ai/design): chat + canvas, comment mode, "show me alternatives", export/handoff, share link
- Mikey'nin masterclass'ı (ücretli/ücretsiz erişim): https://mikeyno-code.com/Skool-base44

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Baseline: `Make me a landing page for a coffee shop.` → jenerik.
2. Spesifik prompt (4 öğe: goal, layout, content, audience): `Create a landing page for a small-batch specialty coffee roaster. The goal is to get local people to come in and buy beans. Lay out a full-width hero with one strong image and a short tagline, then a section telling the brand story, a section showing the beans, and a "visit us" block near the bottom. The brand is warm, crafted and slightly premium: single-origin coffee roasted in small batches; keep the copy in that voice. This is for local coffee lovers who care about quality and craft, not a big chain.` → takip sorularını yanıtla.
3. Chat ile yapı: `Move the featured beans section higher up, just below the hero. Show three featured coffees in a clean grid, each with a name and a short description. I want the actual product to be one of the first things people see.`
4. Chat ile görsel yön: `Make the whole page feel warmer and more editorial. Use deeper, richer tones and more generous spacing, the kind of polished look you'd expect from a high-end roaster.`
5. Comment mode: CTA'ya tıkla → "increase size slightly, more padding, primary brand color more prominently"; kart → "more internal spacing, softer corners"; hero başlık → "increase size, tighten typography". (Comment çalışmazsa aynı talimatı chat'e yapıştır.)
6. Kaydet: `Before we try anything new, save what we have now so we can come back to it.`
7. Varyasyon: `Show me three alternative layouts for the hero section only. Keep everything else on the page the same.` → inset-frame + ürün kartı seçildi + geçici hero görseli.
8. Export: paylaşım/sunum için PDF/PPT/Canva; canlı site için standalone HTML veya Claude Code handoff; yorum için share link (view/comment/edit).

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Yukarıdaki 1-7 adımlarındaki prompt'lar.

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler
- Prompt'ta 4 öğe (hedef, layout, içerik, kitle) → tipografi karakter kazanıyor, copy markaya ait sesleniyor.
- Ürünü hero'nun hemen altına taşıma (satış odaklı yapı).
- "Editorial, richer tones, generous spacing" yön komutu.
- Önce büyük sonra küçük; küçük düzeltmeler birikerek "rafine" hissi.
- Hero için alternatif üretip karşılaştırma (tasarımcı yaklaşımı).

## Hatalar ve çözümleri
- Jenerik ilk üretim → prompt'u spesifikleştir (başa dönme).
- Inline comment bazen kaydolmaz (preview) → chat'e kopyala.
- Küçük düzeltmeleri erken yapmak → büyük değişiklikte yeniden yapılır; sırayı koru.
- Yeni yön denemeden önce mevcut sürümü kaydettir.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Yok.

## Bizim fabrikaya alınacaklar (somut)
1. **Brief → prompt şablonu** (4 öğe): hedef / layout (bölüm listesi) / içerik-ses / kitle; müşteri onboarding formu bunu doğrudan doldursun.
2. **Üretim sırası standardı**: spesifik prompt → yapı (chat) → görsel yön (chat) → eleman ince ayarı (comment) → kritik bölümler için 3 alternatif → seçim.
3. Claude Design'ı tasarım onayı aracı olarak kullan: müşteriye share link (comment izni) → onay → Claude Code handoff ile kodlama.
4. "Önce kaydet, sonra dene" kuralı.
