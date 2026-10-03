# Yol C — DESIGN.md yaklaşımı (Google'ın açık kaynak CLI'si; Stitch web hesabı kullanılmadı)
1. DESIGN.md yaz (YAML token'lar + gerekçe bölümleri; spec: `npm run girdiler` → akis/girdiler/design-md-spec.md).
   Stitch hesabın varsa DESIGN.md'yi Stitch'ten dışa aktarıp doğrudan buraya koyabilirsin.
2. `npx --yes @google/design.md@0.4.0 lint DESIGN.md > lint.json` → hata 0, WCAG kontrast uyarısı yok olana kadar düzelt.
3. `npx --yes @google/design.md@0.4.0 export --format css-vars DESIGN.md > tokens.css`
   `npx --yes @google/design.md@0.4.0 export --format css-tailwind DESIGN.md > tokens-tailwind.css`
4. dist/index.html'i yalnız bu token'larla yaz (token dışı renk yok). Tek geçiş.
Bulgu (bu denemede): `css-vars` çıktısı tipografi token'larını içermiyor; `css-tailwind` içeriyor ama lineHeight/fontFeature yok.
