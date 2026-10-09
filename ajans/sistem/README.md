# FABRİKA ZİNCİRİ — "start" verince fıstık gibi ürün

> Kaynak: 400 YouTube transkripti, 118 X kaynağı, 230+ GitHub reposu, rakip ajans ekran görüntüleri (`ajans/arastirma/`, `ajans/planlar/`).
> Teşhis (herkesin ortak sonucu): çirkin çıktının nedeni model değil. **Varsayılanları yasaklayan kural dosyası + DESIGN.md + ajanın kendi ekran görüntüsüne bakıp düzelttiği kontrol kapısı** eksikti. Bu zincir bu üçünü zorunlu yapar.

## Bant (her müşteri işi bu sırayla akar)

```
 0 KURULUM (tek sefer)      kurulum.sh → skill'ler + MCP'ler + şablonlar
 1 INTAKE (10 dk, insan)    02-intake.md formu → BRIEF.md
 2 REFERANS + DESIGN.md     3-5 referans SS → DESIGN.md (sayılarla) → KAPI-1
 3 SPEC + PLAN              superpowers brainstorm/spec/plan → SPEC.md, ekran/bölüm listesi
 4 ÜRETİM (ajan)            hatlar/web.md | hatlar/mobil.md   (story story, taze bağlam)
 5 GÖRSEL QA (ajan)         Playwright SS 375/768/1440 (+ iOS/Android) → design-review → KAPI-2
 6 CİLA (3 ayrı geçiş)      tipografi → boşluk → hareket → mobil turu → KAPI-3 (20 kontrol)
 7 TESLİM                   web: Cloudflare Pages / Vercel · mobil: EAS preview linki → mağaza
 8 PAZARLAMA (retainer)     hatlar/sosyal-medya.md + hatlar/reklam.md (aynı DESIGN.md'den)
 9 DERS                     "CLAUDE.md'ye ne eklemeli?" + çözülen iş → skill (sektör şablonu)
```

İnsanın işi sadece 3 nokta: **intake (1)**, **yön onayı (2 sonu)**, **son QA (7 öncesi, 15-20 dk)**. Gerisi ajan.

## Dosyalar
| Dosya | Ne |
|---|---|
| `01-teklif-ve-paketler.md` | Ne satıyoruz, paketler, fiyat çapaları, retainer |
| `02-intake.md` | Müşteriden alınacak bilgi (10 dk form) → BRIEF.md |
| `hatlar/web.md` | Web sitesi hattı: adım adım, prompt'larıyla |
| `hatlar/mobil.md` | Mobil uygulama hattı (Expo) |
| `hatlar/sosyal-medya.md` | Instagram/Facebook içerik hattı |
| `hatlar/reklam.md` | Meta/Google reklam hattı |
| `kapilar/KAPILAR.md` | 4 kalite kapısı, geçme koşulları |
| `sablon/` | Her müşteri projesine kopyalanan CLAUDE.md, BRIEF.md, DESIGN.md |
| `skills/tr-caption/` | Türkçe Instagram caption skill'i (piyasada yok, biz yazdık) |
| `kurulum.sh` | Bütün skill/MCP kurulumu tek komut |
| `orkestrasyon.md` | Ana beyin + alt sohbetler nasıl çalışır, limit dersleri |

## Başlatma (start)
```bash
# yeni müşteri: ajans/musteriler/<ad>/ oluşturur, şablonları kopyalar
bash ajans/sistem/yeni-musteri.sh <musteri-adi> <web|mobil|web+mobil>
# sonra o klasörde Claude Code'a:
"BRIEF.md'yi oku, ajans/sistem/hatlar/web.md hattını baştan sona uygula. Kapılarda dur ve rapor ver."
```
