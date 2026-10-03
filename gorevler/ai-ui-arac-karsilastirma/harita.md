# Bağlı harita: hedef → bağımlılık → yollar → uygulama → test → teslim

```mermaid
flowchart TD
  H["HEDEF: AI UI araçlarını gerçek denemelerle karşılaştır;<br/>mobil APK değil, tekrar kullanılabilir üret→ölç→karşılaştır akışı"]
  H --> R["1. Araştırma (arastirma.md)"]
  R --> R1["X araması → 402, giriş yok"]
  R1 -->|yol kırıldı| R2["Alternatif: web + resmi repo + HN Algolia"]
  R --> R3["Reddit JSON → 403"]
  R3 -->|yol kırıldı| R2
  R2 --> B["2. Bağımlılıklar"]
  B --> B1["Node 22, npm, Playwright Chromium (/opt/pw-browsers)"]
  B --> B2["npm: @google/design.md, shadcn, vite, tailwind v4, @axe-core/playwright"]
  B --> B3["Hesap/anahtar: YOK → v0, Lovable, Bolt, Stitch web, Figma Make, 21st Magic denenemez"]
  B1 & B2 --> Y["3. Aynı brief (akis/brief.md) ile üretim yolları"]
  Y --> A["Yol A: Claude Code ham prompt"]
  Y --> BB["Yol B: Claude Code + resmi frontend-design süreci"]
  Y --> C["Yol C: açık kaynak DESIGN.md CLI → lint → export → Claude üretimi<br/>(Stitch web hesabı DEĞİL)"]
  Y --> D["Yol D: statik shadcn/ui dashboard-01 bloğu, AI'sız<br/>(v0 hesabı DEĞİL)"]
  Y -.->|hesap yok: ölçülmedi| E["Yol E+: v0 / Stitch web / Lovable…<br/>hesap gelince denemeler/ içine"]
  A & BB & C & D --> U["4. Uygulama: denemeler/*/dist/index.html"]
  U --> T["5. Test/ölçüm: akis/degerlendir.mjs<br/>ekran görüntüsü 1440 + 390, axe, yatay taşma,<br/>AI-varsayılan işaretleri, yük boyutu, brief kapsamı"]
  T -->|başarısız| U
  T --> K["6. Karşılaştırma: sonuclar/rapor.html + sonuclar.json"]
  K --> P["7. Kalıcı teslim: dal push + geri okuma;<br/>bağımsız temiz Git kökü + bundle, özel ZIP + SHA-256"]
```

## Zincirin düz metni
1. **Hedef** — kaynak satırı 1 ("siteleri çıkar, araştır, plan yap, tek tek uygula, hangisi iyi"), satır 5 ("halledene kadar"),
   satır 19 ("mobil uygulama değil, sistem"). Çıktı: araç envanteri + çalışan karşılaştırma akışı + gerçek deneme sonuçları.
2. **Bağımlılıklar** — yalnız hesapsız erişilebilen araçlar gerçek denemeye girebilir. Hesap gerektirenler envanterde
   "denenemedi" olarak, nedeniyle yazılır.
3. **Yollar** — A/B/C/D aynı brief'le; her biri tek statik `dist/index.html` üretir (D, Vite build ile).
   Yol kırılma planı: D'de shadcn CLI başarısız olursa registry JSON'u doğrudan indirip elle yerleştir;
   C'de CLI başarısız olursa repo klonundan `bun`/`tsx` ile çalıştır.
4. **Test** — `npm run karsilastir` tüm denemeleri aynı ölçütle puanlar; eşik: axe kritik/ciddi ihlal 0, mobilde yatay taşma yok.
5. **Teslim** — genel çıktılar dalda; özel kaynak yalnız ZIP'te.
