# AI UI araçları — araştırma ve envanter

Tarih: 2026-10-03. Bu dosya genel araştırma çıktısıdır; özel sohbet içeriği içermez.

## 1. Başlangıç noktası: X araması
İstenen kaynak `https://x.com/search?q=ai%20ui&src=typed_query&f=top` idi. Bu bulut ortamından
giriş yapılmadan açılamadı (WebFetch yanıtı **HTTP 402 Payment Required**). X hesabı/oturumu
olmadığı için tweet'ler tek tek okunamadı. Yerine aynı konunun (X'te paylaşılan "AI UI" araçları)
web, blog, Hacker News ve resmi sayfalardaki karşılığı tarandı. Reddit JSON araması da bu ortamdan
**403** döndü; topluluk deneyimi için Hacker News (Algolia API) ve bağımsız/üretici bloglar kullanıldı.

## 2. Araç envanteri (ne işe yarar, nasıl kullanılır, bu çalışmada denendi mi)

| # | Araç | Ne için | Çıktı | Erişim şartı | Bu çalışmada |
|---|------|---------|-------|--------------|--------------|
| 1 | Google Stitch — https://stitch.withgoogle.com/ | Prompt/görselden ekran tasarımı, tasarım sistemi | Görsel + HTML, Figma'ya aktarım, DESIGN.md, MCP | Google hesabı | Web arayüzü **denenemedi** (hesap yok). Açık kaynak **DESIGN.md formatı + CLI'si gerçekten denendi** (Deneme C) |
| 2 | DESIGN.md (Stitch formatı) — https://github.com/google-labs-code/design.md | Tasarım sistemini ajanlara aktaran token+gerekçe dosyası; lint/diff/export CLI | Lint JSON, Tailwind v4 `@theme`, DTCG | Yok (npm) | **Denendi**: `npx @google/design.md lint/export` |
| 3 | v0 (Vercel) — https://v0.app | React/Next + shadcn/ui bileşen üretimi | React kodu | Vercel hesabı | **Denenemedi** (hesap). Altındaki shadcn/ui registry'si denendi (Deneme D) |
| 4 | shadcn/ui registry + CLI — https://ui.shadcn.com/docs/registry | Hazır blok/bileşen dağıtımı (v0 ve birçok AI aracının hedef yığını) | Projeye kopyalanan kaynak kod | Yok | **Denendi**: `npx shadcn add dashboard-01` |
| 5 | Claude Code (ham) — https://code.claude.com/docs | Ajanın doğrudan UI kodu yazması | HTML/React | Bu oturum | **Denendi** (Deneme A) |
| 6 | Anthropic frontend-design skill — https://github.com/anthropics/claude-code/tree/main/plugins/frontend-design | "AI beige slop"u engelleyen tasarım süreci (plan → brief kontrolü → yap → ekran görüntüsüyle eleştir) | Süreç kuralı | Yok | **Denendi** (Deneme B, resmi SKILL.md kuralları uygulandı) |
| 7 | 21st.dev Magic MCP — https://github.com/21st-dev/magic-mcp | IDE içinden bileşen üretimi, topluluk bileşen kütüphanesi | React bileşen | API anahtarı | **Denenemedi**: `https://21st.dev/r/...` bu ortamdan 403; Magic için anahtar yok |
| 8 | Lovable — https://lovable.dev | Tam yığın uygulama | Uygulama + deploy | Hesap | Denenemedi (hesap) |
| 9 | Bolt — https://bolt.new | Hızlı prototip/uygulama | Uygulama | Hesap | Denenemedi (hesap) |
| 10 | Figma Make — https://www.figma.com/make/ | Figma içinde prompt→prototip | Prototip | Figma hesabı | Denenemedi (hesap) |
| 11 | Magic Patterns — https://www.magicpatterns.com | Tasarım sistemine uyumlu React | React | Hesap | Denenemedi (hesap) |
| 12 | UX Pilot — https://uxpilot.ai | Wireframe/hi-fi + ısı haritası, Figma eklentisi | Görsel | Hesap | Denenemedi (hesap) |
| 13 | Subframe — https://www.subframe.com | Görsel editör + React/Tailwind, MCP | React | Hesap | Denenemedi (hesap) |
| 14 | Paper — https://paper.design | Kod-yerel tasarım kanvası, MCP | HTML/CSS | Hesap | Denenemedi (hesap) |
| 15 | Aura — https://www.aura.build | Prompt→Tailwind sayfa, şablon remix | HTML/Tailwind | Hesap | Denenemedi (hesap) |
| 16 | Superdesign — https://superdesign.dev | IDE içi tasarım ajanı | HTML | Hesap/anahtar | Denenemedi |
| 17 | Uizard / Banani / Flowstep / Variant | Prompt→ekran, kanvas | Görsel | Hesap | Denenemedi |
| 18 | axe-core + Playwright — https://github.com/dequelabs/axe-core-npm | Erişilebilirlik denetimi (değerlendirme aracı) | JSON ihlal listesi | Yok | **Kullanıldı** (değerlendirme hattı) |

**Neden hepsi denenmedi?** Kullanıcı hesapları, parolalar veya API anahtarları bu ortamda yok;
uydurulmadı. Hesap gerektiren araçlar için aynı brief ve aynı değerlendirme betiğiyle sonradan
denenebilecek talimat `akis/README.md` içinde ("Hesaplı araçlar nasıl eklenir").

## 3. Topluluk ve bağımsız testler ne diyor
- **Aynı prompt testi, 8 araç** (Superdesign blogu, 2026-06-28; yazar Superdesign'ı geliştiriyor — çıkar çatışmasını kendisi belirtiyor):
  ilk taslakta araçlar "yakınsamış"; Stitch'te yer tutucu etiketler, Uizard'da kalabalık; ham Claude Code ajanı da cilalı sonuç vermiş.
  Asıl fark yineleme, çok ekranlı tutarlılık ve düzenlenebilir kod kalitesinde.
  https://superdesign.dev/blog/i-tested-8-ai-ui-generators
- **Aynı prompt, 5 araç, 6 ekran** (DEV Community, 2026-07-06): Flowstep 6/6 – 1,5 dk, Stitch 5/6 – 2,5 dk, Figma Make 6/6 – 5,5 dk,
  Lovable 6/6 – 10 dk, Base44 6/6 – 4 dk. Sonuç: "tek bir en iyi araç yok", tasarım-sistemi üreticileri ile uygulama kurucuları farklı iş görür.
  https://dev.to/hadil/top-5-ai-ui-design-tools-in-2026-i-tested-them-all-with-the-same-prompt-hm7
- **Ürün tasarımcısı incelemesi — Stitch** (Bitovi, 2025-05-27): sayfalar arası tutarsız navigasyon, jenerik görseller,
  Figma çıktısında aşırı katman; "tasarımcılar için şu an yararlı değil". https://bitovi.com/blog/google-stitch-a-product-designers-review
- **Hacker News "AI beige slop"** (2026-02-10): herkes aynı şeyi görüyor — tam genişlik gradyan, yuvarlak kartlar, fazla gölge, Inter, mor gradyan.
  https://news.ycombinator.com/item?id=46956964
- **Hacker News "consistent UI from Claude Code"** (2026-01-20): resmi frontend-design skill'in ilkesel/çağrışımsal yapısı tartışılıyor; şüpheci yorumlar da var.
  https://news.ycombinator.com/item?id=46699260
- **Hacker News "Stitch's DESIGN.md format is now open-source"** (2026-04-23) ve resmi duyuru (2026-04-21):
  https://news.ycombinator.com/item?id=47882713 · https://blog.google/innovation-and-ai/models-and-research/google-labs/stitch-design-md/
- Karşılaştırma blogları (çoğu üretici blogu, yanlı olabilir): https://www.nxcode.io/resources/news/vibe-design-tools-compared-stitch-v0-lovable-2026 ·
  https://uxmagic.ai/blog/best-ai-design-tools-2026 · https://www.komposo.ai/blog/best-ai-ui-generators-2026 · https://flowstep.ai/blog/best-ai-ui-design-tools/

## 4. Resmi doğrulamalar
- frontend-design SKILL.md içeriği resmi repodan okundu: iki geçişli süreç, kaçınılacak AI varsayılanları
  (krem+terakota, siyah+asit yeşili, SaaS kart kiti, ALL-CAPS eyebrow, ortanokta ayırıcılar), kalite tabanı
  (mobil uyum, görünür klavye odağı, reduced-motion, erişilebilir kontrast).
  https://github.com/anthropics/claude-code/blob/main/plugins/frontend-design/skills/frontend-design/SKILL.md
- DESIGN.md CLI komutları kaynak koddan doğrulandı: `lint`, `diff`, `export --format css-tailwind|json-tailwind|dtcg|css-vars`, `spec`.
  https://github.com/google-labs-code/design.md/blob/main/packages/cli/src/commands/export.ts
- Stitch'in resmi MCP uç noktası Google Cloud OAuth ister (bu ortamda yok). https://pasqualepillitteri.it/en/news/647/google-stitch-mcp-export-claude-code-design-to-code

## 5. Seçilen yol ve gerekçe
Topluluğun ortak bulgusu: ilk taslaklar birbirine benziyor; fark **tasarım sistemi verme**, **ekran görüntüsüyle
eleştiri döngüsü** ve **ölçülebilir kontrol**den çıkıyor. Bu yüzden teslim edilen şey bir mobil uygulama değil,
tekrar kullanılabilir bir **üret → ölç → karşılaştır** akışıdır: aynı brief, birden çok üretim yolu, aynı otomatik
değerlendirme betiği (ekran görüntüsü, axe erişilebilirlik, mobil taşma, "AI varsayılanı" işaretleri, yük boyutu).
Hesap gerektirmeyen dört yol gerçekten denendi; hesap gerektirenler aynı akışa eklenebilir.
