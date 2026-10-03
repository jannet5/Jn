# AI UI araçlarını gerçek denemelerle karşılaştırma

Bu bir mobil uygulama değil: aynı brief'i farklı AI UI yollarıyla ürettirip **aynı otomatik ölçütle** karşılaştıran,
tekrar kullanılabilir bir iş akışı ve onunla yapılmış gerçek dört deneme.

**Kapsam:** A ve B Claude Code çıktısıdır; C Google Stitch web hesabı değil, açık kaynak DESIGN.md CLI yaklaşımıdır;
D v0 hesabı değil, statik shadcn/ui bloğudur. Puanlar tek brief'in tek çalıştırmasıdır, genel araç sıralaması değildir.
Hesap gerektiren araçlar (v0, Stitch web, Lovable, Bolt, Figma Make…) erişim olmadığı için ölçülmedi.

| Dosya | İçerik |
|---|---|
| `arastirma.md` | Araç envanteri (18 araç), topluluk/bağımsız testler, resmi doğrulamalar, seçilen yolun gerekçesi |
| `harita.md` | Hedef → bağımlılık → A/B/C/D yolları → uygulama → test → teslim haritası |
| `akis/brief.md` | Ortak brief |
| `akis/tarifler/` | Her yolun birebir prompt/komut tarifi |
| `akis/degerlendir.mjs` | Otomasyon: ekran görüntüsü, axe, mobil taşma, klavye odağı, AI-varsayılan işaretleri, brif kapsamı, "Geçen hafta" akışı |
| `denemeler/*/dist/index.html` | Dört denemenin çıktısı |
| `sonuclar/rapor.html` | Karşılaştırma sayfası (ekran görüntüleriyle) |
| `sonuc.md` | Sonuç, bulgular, öneri, sınırlar |
| `calisma-gunlugu.md` | Sırayla yapılanlar, komutlar, sorunlar ve çözümler |

## Çalıştırma
```bash
npm ci                      # playwright + @axe-core/playwright
npm run girdiler            # frontend-design SKILL.md ve DESIGN.md spec'ini resmi kaynaklardan indirir
npm test                    # ölçütlerin birim testleri (6 test)
npm run build:d             # (isteğe bağlı) Deneme D'yi yeniden derler
npm run karsilastir         # tüm denemeleri ölçer → sonuclar/
```
Playwright tarayıcısı yoksa: `npx playwright install chromium`.

## Hesaplı araç ekleme (v0, Stitch, Lovable, Bolt, Figma Make…)
1. Aracı kendi hesabınla aç, `akis/brief.md` metnini **değiştirmeden** ver, ilk sonucu al.
2. Çıktıyı `denemeler/<arac-adi>/dist/index.html` olarak koy (statik dışa aktarım; React ise `npm run build` çıktısı).
3. `npm run karsilastir` — yeni deneme tabloya otomatik eklenir.
