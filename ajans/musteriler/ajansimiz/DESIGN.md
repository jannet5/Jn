# DESIGN.md — Vitrin Atölyesi
> Tek doğruluk kaynağı. Bilinçli olarak 2. nesil "slop"tan da kaçınıldı: krem + terracotta + Fraunces YOK, Inter YOK, mor gradyan YOK.

## 1. Kişilik
3 sıfat: net, sıcak, iş bilen · Olmayacak: ajans jargonu, soğuk kurumsal, oyuncak/çocuksu · His: "mahalledeki en iyi ustanın atölyesi, ama dijital".
Yön (seçilen): **"Atölye Mürekkebi"** — kâğıt beyazı zemin, koyu mürekkep yeşili metin, tek sıcak sinyal rengi (çay/nar kırmızısı). Büyük, sıkışık, karakterli başlıklar; çok boşluk.

## 2. Renk
| Token | Rol | Açık | Koyu | Kontrast |
|---|---|---|---|---|
| background | zemin | #FFFFFF | #0E1512 | — |
| surface | ikincil bölüm zemini | #F3F5F2 | #151E1A | — |
| foreground | ana metin | #0E1512 | #EEF2EF | 18.9:1 / 16.4:1 |
| muted-foreground | ikincil metin | #4F5B55 | #A3AFA9 | 7.2:1 / 8.1:1 |
| border | çizgi | #DCE2DE | #26322C | — |
| primary | tek sinyal rengi (CTA, vurgu) | #D7381E | #FF5A3C | buton metni beyaz 4.8:1 / koyu metin 5.6:1 |
| primary-foreground | CTA metni | #FFFFFF | #0E1512 | |
| ink | koyu bloklar (değer defteri, son CTA) | #0E1512 | #000000 değil → #0A100D | |
| success | rapor artışı | #1F7A4D | #3DBE7E | |
Gradyan yok. Primary yalnızca: CTA butonları, hero'daki tek vurgulu kelime, aktif durumlar, rapor grafiği çizgisi.

## 3. Tipografi
Tek aile: **Archivo** (Google Fonts, değişken genişlik + ağırlık, Türkçe karakter tam). Başlık: Archivo, `font-stretch: 112%`, ağırlık 800. Gövde: Archivo 100%, 400/500. Ağırlıklar: 400 · 500 · 800.
| Token | Boyut (mobil → masaüstü) | Satır | Harf aralığı | Ağırlık |
|---|---|---|---|---|
| display | 44 → 88 | 0.95 | -0.035em | 800 |
| h2 | 32 → 56 | 1.0 | -0.03em | 800 |
| h3 | 22 → 28 | 1.15 | -0.015em | 800 |
| lead | 18 → 21 | 1.5 | 0 | 400 |
| body | 16 → 17 | 1.6 | 0 | 400 |
| small | 14 | 1.45 | 0.01em | 500 |
Eyebrow: small, 500, normal harf (ALL-CAPS değil), önünde 8px primary nokta.

## 4. Boşluk ve düzen
Skala: 4·8·12·16·24·32·48·64·96·128 · Kenar: mobil 20, masaüstü 40 · Bölüm arası: mobil 96, masaüstü 128 · Bölüm içi: 24-48 · Maks genişlik: 1200 · Izgara: 12 sütun, 24 gutter. Sol hizalı düzen (ortalanmış bölüm yalnızca son CTA).

## 5. Köşe ve derinlik
radius sm 8 · md 14 · lg 24 · pill 999 · Kart stili: **1px çerçeve (border token), gölge yok**. Tek istisna: hero cihaz çerçeveleri yumuşak gölge `0 30px 60px -20px rgba(14,21,18,.25)`.

## 6. Bileşenler
- Birincil buton: yükseklik 52, yatay padding 24, radius pill, 500 ağırlık 16px; hover: 2px yukarı + koyulaşma (#B92E17); basılı: scale .98; odak: 3px ring primary %35. İçinde WhatsApp ikonu (16).
- İkincil buton: aynı ölçü, şeffaf, 1px foreground çerçeve.
- Kart: padding 24 (masaüstü 32), radius lg, border.
- Nav: 64 yükseklik, scroll'da arka plan %85 opak + blur 12, alt çizgi border.
- SSS: details/summary, + işareti 45° döner.
- İkonlar: tek set (Lucide, inline SVG), stroke 1.75, boyut 16/20/24.

## 7. Hareket
Süreler 180-600ms, easing `cubic-bezier(.2,.7,.2,1)` (ease-out). Bölüm başına tek etkileşim, aynı fade-up her yerde YOK:
- Hero: başlık kelime kelime yukarı kayarak (stagger 60ms); cihaz çerçevesi scroll'la hafif eğim değiştirir (≤6°).
- Sektör şeridi: sürekli yatay kayma (40s), hover'da durur.
- Dertler: satırlar soldan çizgi çizilerek açılır.
- Hizmetler: kart hover'da içteki mini önizleme 4px kayar.
- Adımlar: numaralar sayaç değil, çizgi ilerleme göstergesi scroll'la dolar.
- Değer defteri: grafik çizgisi görünür olunca çizilir (stroke-dashoffset).
`prefers-reduced-motion: reduce` → tüm hareketler kapalı, içerik direkt görünür.

## 8. Görsel dil
Fotoğraf yok (henüz gerçek iş yok, stok yasak). Görseller CSS/SVG ile çizilmiş cihaz çerçeveleri ve mini site önizlemeleri; her konsept kendi mini paletiyle (kafe: kahve tonları, berber: siyah-altın, klinik: buz mavisi) ama ana sayfanın paletini bozmayacak kadar küçük.

## 9. Yapılmayacaklar
Sahte yorum/logo/istatistik · "Dijital dönüşüm yolculuğu" · emoji ikon · eşit 3/4 kart ızgarası · ALL-CAPS eyebrow · "→" her butonda · ortalanmış her bölüm · krem+terracotta · Inter.
