# Deneme C — Google Stitch DESIGN.md akışı (`@google/design.md@0.4.0`)

Çalışma klasörü: `denemeler/c-design-md-cli/`

## Üretilen dosyalar

| Dosya | Açıklama |
|---|---|
| `DESIGN.md` | YAML token'lar (30 renk, 8 tipografi, 5 köşe, 8 boşluk, 20 bileşen) + spec sırasıyla düzyazı bölümleri |
| `lint-ilk.json` | İlk lint çalıştırması (0 hata, 3 uyarı) |
| `lint.json` | Son lint çalıştırması (0 hata, 0 uyarı, 1 bilgi) |
| `tokens.css` | `export --format css-vars` çıktısı |
| `tokens-tailwind.css` | `export --format css-tailwind` çıktısı (Tailwind v4 `@theme`) |
| `dist/index.html` | Tek dosyalık panel (satır içi CSS/JS, yalnızca Google Fonts `<link>`) |

## Çalıştırılan komutlar (sırasıyla)

```bash
npx --yes @google/design.md@0.4.0 --help
npx --yes @google/design.md@0.4.0 export --help
npx --yes @google/design.md@0.4.0 lint DESIGN.md > lint.json      # 1. çalıştırma -> lint-ilk.json olarak kopyalandı
cp lint.json lint-ilk.json
npx --yes @google/design.md@0.4.0 lint DESIGN.md > lint.json      # 2. çalıştırma (düzeltme sonrası)
npx --yes @google/design.md@0.4.0 export --format css-vars DESIGN.md > tokens.css
npx --yes @google/design.md@0.4.0 export --format css-tailwind DESIGN.md > tokens-tailwind.css
```

CLI yardım çıktısı: komutlar `lint | diff | export | spec`. Export kullanımı
`design.md export [OPTIONS] <FILE> --format`; biçimler `css-tailwind, json-tailwind,
tailwind, dtcg, css-vars`; `--prefix` isteğe bağlı. Görevde verilen argüman sırası
olduğu gibi çalıştı, çıkış kodları 0.

## CLI çıktı özetleri

**Lint 1 (`lint-ilk.json`)** — `errors: 0, warnings: 3, infos: 1`.
Üç uyarı da `orphaned-tokens` kuralı: `colors.focus-ring`, `colors.chart-bar`,
`colors.chart-bar-muted` tanımlı ama hiçbir bileşen tarafından referans verilmiyor.
WCAG kontrast bulgusu yok.

**Lint 2 (`lint.json`)** — `errors: 0, warnings: 0, infos: 1`
(“Design system defines 30 colors, 8 typography scales, 5 rounding levels,
8 spacing tokens, 20 components.”). Kontrast uyarısı yok.

**Export css-vars** — `:root { … }` içinde 30 `--color-*`, 8 `--spacing-*`,
5 `--rounded-*` değişkeni. Hex değerleri küçük harfe çevrilmiş.
**Tipografi token'ları css-vars çıktısında hiç yok.**

**Export css-tailwind** — `@theme { … }` içinde renkler, `--font-*` (aile),
`--text-*` (boyut), `--tracking-*`, `--font-weight-*`, `--radius-*`, `--spacing-*`.
`lineHeight` ve `fontFeature` değerleri bu çıktıda da yok.

## Sorunlar ve çözümler

1. **Sahipsiz token uyarıları (orphaned-tokens).** Grafik ve odak halkası renkleri
   yalnızca sayfada kullanılacaktı, bileşen tanımı yoktu. Çözüm: `components` altına
   `chart-bar`, `chart-bar-muted` (backgroundColor + rounded.sm) ve `focus-indicator`
   (backgroundColor + size: 3px) eklendi. `focus-indicator`'a bilinçli olarak
   `textColor` verilmedi; karamel (#C8662B) üstünde espresso metin ~4:1 olacağı için
   kontrast denetimine takılabilirdi.
2. **css-vars dışa aktarımı tipografiyi içermiyor.** Sayfada tipografi değişkenleri
   için `tokens-tailwind.css` içindeki `--font-*`, `--text-*`, `--tracking-*`,
   `--font-weight-*` satırları (değiştirilmeden) ikinci bir `:root` bloğuna kopyalandı.
3. **lineHeight / fontFeature hiçbir dışa aktarımda yok.** Satır yükseklikleri
   DESIGN.md'deki birimsiz değerlerle (`1.1`–`1.5`) ayrı bir `:root` bloğunda
   `--leading-*` olarak tanımlandı; `'tnum'`/`'lnum'` font-feature değerleri doğrudan
   CSS'e yazıldı. Bunlar renk değil, token kaynağı DESIGN.md.
4. **Renk kuralı doğrulaması.** `dist/index.html` içinde token bloklarının dışında
   hiç hex/rgb/hsl renk yok (Python regex ile kontrol edildi). Tek türetilmiş değer
   kart gölgesi: `color-mix(in srgb, var(--color-on-surface) 6%, transparent)`.
5. Bileşen düzeyindeki bazı sabit boyutlar (padding 12px/10px, buton yüksekliği
   36/40px) bileşen token'larından birebir alındı; css-vars bileşenleri dışa
   aktarmadığı için CSS'e elle yazıldı.

## Tasarım kararları

- **Konsept:** “Kavrulmuş çekirdek” — espresso yan menü, süt köpüğü (#F7F2EC)
  zemin, beyaz kartlar, tek etkileşim rengi kavrulmuş kahve (#7A3E1D), grafik ve
  odak halkası için karamel (#C8662B). Durumlar: yeşil / kehribar / kırmızı rozetler,
  her zaman metinle birlikte.
- **Yazı tipleri:** Fraunces (başlıklar, ölçüt değerleri) + Manrope (arayüz, tablo,
  tabular rakamlar).
- **Erişilebilirlik:** “İçeriğe geç” bağlantısı; tarih aralığı ARIA `radiogroup`
  (ok/Home/End tuşları, roving tabindex); `aria-current="page"` menü; tüm
  odaklanabilir öğelerde 3px karamel `:focus-visible` halkası; grafik çubuklarında
  `aria-label` ve ekran okuyucu özeti; tablo `caption` + `scope="col"`, yatay
  kaydırılabilir bölge `tabindex="0"`; değişimler ok + metin (“artış/düşüş”).
- **Duyarlı yerleşim:** ≤1180px ölçütler 2 sütun, grafik/tablo alt alta; ≤860px yan
  menü yatay kaydırmalı üst menüye döner; ≤520px (390px hedef) segment kontrol tam
  genişlik, kart iç boşluğu 16px, tablo kart içinde yatay kayar.
- **Veri:** Üç aralık için gömülü örnek veri (Bu hafta / Geçen hafta / Son 30 gün);
  seçim değişince ölçütler, grafik ve tablo yeniden çizilir. “Son 30 gün”de grafik
  son 7 günü gösterir.

## Doğrulama

- `node` + `jsdom` ile duman testi: 4 ölçüt kartı, 7 çubuk, 7 tablo satırı oluştu;
  “Geçen hafta” tıklanınca başlık/ölçüt/değişim güncellendi.
- Görev gereği ekran görüntüsü yinelemesi yapılmadı (tek geçiş).
