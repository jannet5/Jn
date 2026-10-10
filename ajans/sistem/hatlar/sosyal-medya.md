# HAT: SOSYAL MEDYA (Instagram + Facebook)

Hedef: müşterinin DESIGN.md'sinden üretilen, markaya tutarlı aylık içerik; onaylanınca otomatik yayın.
Aylık standart: 12 post (9'lu feed ızgarası tutarlı) · 4 carousel · 4 reels kurgusu · story şablonları · Türkçe caption + yerel hashtag.

## Bant
```
S0 Marka dosyası   DESIGN.md + design-system.json (renk/font token) + voice.md (marka sesi, 10 örnek cümle)
S1 Takvim          ayın konuları: ürün, sahne arkası, müşteri yorumu, kampanya, before/after, SSS → TAKVIM.md (tarih, format, amaç)
S2 Metin           tr-caption skill (bizim) + sergebulaev ig-carousel-planner (10 slayt omurgası) + ig-hashtag-strategist (3-5 niş, yerel: #kadıköykahve)
S3 Görsel          HTML şablon (DESIGN token'ları) → Playwright 420×525 @ dsf 2.5714 → 1080×1350 PNG → JPEG q92
                   ürün/mekân: gerçek foto > Nano Banana (-r urun.png, 4:5) ; 9'lu feed tek prompt (prompts.md #11)
S4 Kontrol         kontrast ≥4.5:1, taşma yok, yetim satır yok, logo/renk doğru, "uydurma bilgi yok" → PNG'yi geri okuyup kontrol
S5 Onay            müşteriye tek sayfa önizleme (ızgara + caption) → "onay / düzelt"
S6 Yayın           Instagram Graph API (Business hesap + bağlı FB sayfası; container → publish; sadece JPEG, public HTTPS URL)
                   ya da Postiz (self-host, zamanlama) / Publora MCP
S7 Rapor           IG Insights (reach, saves, shares, views) → aylık değer defterine
```

## Kurallar
- Hook ilk 125 karakterde. Carousel: 1. slayt vaat + açık döngü, son slayt kaydedilebilir özet + tek CTA.
- AI kokan kelimeler ve em-dash yığını yasak (humanizer). Spesifik bilgi uydurulmaz ("Never invent the specifics").
- Sağlık müşterisinde öncesi/sonrası görsel yasak (Meta politikası).
- Kendi ajans hesabımız: haftada 3-5 **before/after** (eski site → yeni site, eski feed → yeni feed), süreç videosu (ekran kaydı hızlandırılmış), müşteri sonucu.

## Kendi hesabımızın ilk 30 günü
Hazır gün gün plan: `planlar/ajans-is-modeli/OZET.md` §5. Haftalık ritim: Pzt önce/sonra · Sal işletme sahibine ipucu carousel'i · Çar kulis reel'i (ekran kaydı) · Per sosyal kanıt · Cum teklif. Her gönderide tek CTA: "Ücretsiz örnek ana sayfa için DM'den ÖRNEK yaz." Rakiplerde neredeyse hiç olmayan iki format bizim imzamız: **önce/sonra** ve **"kaç saatte teslim ettik"**.
Otomasyon: n8n içerik hattı (kaynak → platform başına prompt → marka şablonunda görsel → Telegram'da onay → Graph API). Tam otomatik yayın yok, her zaman onay kapısı.

## İlk üretimden kurallar (ajansimiz/sosyal DERSLER)
- **CTA:** sosyal hesapta platform aksiyonu ("DM'den ÖRNEK yaz") + bio'da WhatsApp; karar KARARLAR.md'de.
- **Hashtag:** ilk 2 hafta A/B (tek gün etiketli, çift gün etiketsiz), sonra karar.
- **Kanıt yoksa** sosyal kanıt günü = dürüstlük gönderisi ("henüz yorum yok, bunun yerine şunu yapıyoruz"). Sahte yorum/sayı asla.
- **Önce/sonra müşterisizken:** temsili eski site çizimi + "Temsili, gerçek bir işletme değil" alt yazısı + "Sizin Kafe" gibi yer tutucu ad. Gerçek işletmenin sitesi izinsiz kullanılmaz.
- **Izgara:** profil 3:4 kırpar; kapakta önemli yazı ortadaki 3:4 alanda. S4'te `izgara.png` önizlemesi zorunlu.
- **Otomatik kontrol** (`sablonlar/uret.mjs`: kontrast, taşma, çakışma, yetim satır; `.main>*{flex-shrink:0}` şart) + yine de her görsele gözle bakılır.
- Şablon: `musteriler/ajansimiz/sosyal/sablonlar/` (HTML + uret.mjs + son-islem.py, 57 görsel ~10 sn). Yeni müşteride kopyala, token'ları değiştir.
