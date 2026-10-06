# Claude Design is Insanely Easy - Become a Pro in 14 Minutes! — Jeff Su — 14 dk — https://www.youtube.com/watch?v=VeWf0l4ci6Y

## Tek paragraf: ne yapıyor, sonuç ne
Jeff Su, Claude Design'da "hemen prompt'lama" yerine 3 dosya hazırlayarak tutarlı, markalı çıktılar alma sistemini anlatıyor: (1) hazır bir **DESIGN.md** (VoltAgent awesome-design-md reposundan Stripe'ınkini indirip ChatGPT'ye "tescilli içeriği temizle, gerisini aynen bırak" dedirtip yeniden adlandırıyor), (2) bunu Claude Design'da **Design System**'e dönüştürme (Create design system → dosya yükle → 10-20 dk → mockup'ları incele → geri bildirimle hex düzelt → voice-principles dosyası + logo + ikonlar yükle), (3) design system'den **şablon** üretme (slayt destesi; aynı mantık web sayfası/carousel/newsletter görseli için de geçerli). Sonra her yeni işte design system + şablon seçip içerik verip "ask me anything before you build" ile üretiyor; geri bildirimleri **CLAUDE.md**'ye yazdırarak kalıcı hale getiriyor (öğrenmeler birikir); tekil düzeltmeler için edit/annotate (daire çiz), deste geneli kararlar için "tweaks" toggle'ları; HTML olarak dışa aktarıyor (presenter view bile ekletmiş). Sonuç: ücretli workshop'ta kullandığı profesyonel deste; aynı sistem sosyal medya carousel ve e-posta görselleri için.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **awesome-design-md** (VoltAgent): https://github.com/VoltAgent/awesome-design-md — 73 markanın DESIGN.md'si (Stripe, Apple, Nike, Notion, Linear…). Metin: `kaynaklar/VeWf0l4ci6Y-awesome-design-md.md`
- Temizleme prompt'u (ChatGPT/herhangi bir LLM): aşağıda
- Claude Design (claude.ai/design; Opus model, effort max; "Create design system")
- Ek girdiler: voice principles dosyası, logo, sık kullanılan ikonlar; CLAUDE.md (proje içi kalıcı talimat)
- Export: PowerPoint/PDF/standalone HTML
- Jeff'in ücretsiz Cowork kiti: https://www.pressplay.cc/link/s/DE1C4C50

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. awesome-design-md → Stripe DESIGN.md indir.
2. ChatGPT'ye yükle: `Remove the proprietary content from this design file and replace them using your own judgment. Keep everything else exactly as is. Rename it to the "Subtle Gradient Design System".` → temiz md indir. (Claude Design gerçek markanın kılavuzunu doğrudan kopyalamaz; detaylı ama "güvenli" dosya gerek.)
3. Claude Design → yeni sohbet → Opus, effort max → **Create design system** → ad: "Gradient design system: subtle, beautiful, minimalistic" → diğer alanları atla, en altta dosyayı yükle → 10-20 dk.
4. Mockup'ları incele → Feedback: `Replace the dark 900 color with this hex code from my brand palette: #…`
5. Opsiyonel: voice-principles.md + logo + ikonlar yükle → `I've uploaded a voice principles file so the copywriting will sound like me, my business logo, and some commonly used icons.`
6. `Go ahead and build a slide deck template for workshops and trainings. You pick the number of slides.`
7. Şablon geri bildirimi: `Edit based on my feedback: add the gradient mesh to every slide with a white background, not just cover slides. Don't use orange backgrounds. Scale up the text to fill empty space. Add my logo to the bottom-left footer.`
8. Yeni iş: design system + şablon seç → içerik (markdown talking points) ekle → `Turn the attached talking points into a workshop deck and ask me anything you need before you build.` → soruları dikkatle yanıtla ("decide for me" seçeneği var).
9. Kalıcı geri bildirim: `Review my feedback below, and in addition to making the edits, create a CLAUDE.md file you'll read for all future slide decks. Feedback: make the eyebrow labels more prominent (wrap in a rectangular container), icons should have transparent backgrounds.`
10. Tekil: Edit (font/renk/bold), Annotate (daire çiz + `add more visual contrast to differentiate…`), Tweaks (`give me two toggles: one to toggle my logo, one to show slide numbers bottom right`) → default olarak kaydet.
11. Share → More formats → standalone HTML (tarayıcıda açılır; presenter view butonu ekletilebilir).

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Yukarıdaki 2, 4, 5, 6, 7, 8, 9, 10 adımlarındaki prompt'lar (İngilizce aynen).

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler
- **Hazırlık > prompt**: detaylı DESIGN.md (gerçek markadan türetilmiş) → ilk üretim bile iyi.
- Design system (nasıl görünür) ile şablon (nasıl dizilir) ayrımı; her iş markalı ve yapılı başlar.
- Voice principles → copy "em dash yok, kurumsal jargon yok, kulağa göre yazılmış".
- Geri bildirimlerin CLAUDE.md'de birikmesi → her teslimat öncekinden iyi (fabrikada aynen uygulanmalı).
- "Ask me anything before you build" → soruları ciddiye al, saatler kazandırır.
- Büyük kararlar tweaks ile tek yerden; küçükler annotate ile.

## Hatalar ve çözümleri
- Markanın orijinal DESIGN.md'si reddedilir → tescilli içeriği temizle.
- Turuncu arka planlı çirkin slayt → şablon geri bildirimi.
- Eyebrow etiketleri silik, ikonlar beyaz zeminli → CLAUDE.md'ye kural.
- Önce büyük (yapı/yön), sonra küçük düzeltmeler.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Yok (kendi ücretli workshop'unda kullanmış).

## Bizim fabrikaya alınacaklar (somut)
1. **Sektör bazlı DESIGN.md kütüphanesi**: awesome-design-md'den restoran/klinik/dükkan için uygun 3-5 markayı seç → "tescilli içeriği temizle" prompt'uyla jenerikleştir → `sistem/design-md/<stil>.md`.
2. Her müşteri için Claude Design'da design system + şablon (web, sosyal carousel, sunum) üret; müşteri geri bildirimlerini proje CLAUDE.md'sine yazdır.
3. Voice principles dosyası = müşterinin ses tonu (Türkçe, em dash yok, jargon yok).
4. Claude Design → Claude Code handoff ile siteye geç (bkz. y2n1NMrMNBo planı).
