# İKON SEÇİMİ: Melodi Zil (2026-10-10)

## Süreç
1. Tek sayfada 6 özgün ikon konsepti çizildi (`docs/store/ikon_secimi/ikon_varyasyonlari.png`, kaynak `ikonlar.py`). Her biri büyük boyutta ve gerçek ana ekran boyutunda (48px) yan yana gösterildi.
   - Not: Bu oturumda görsel üreten bir yapay zeka modeli yok. Varyasyonlar SVG olarak kodla çizildi; bu sayede seçilen tasarım doğrudan Android vektör ikonuna çevrilebildi.
2. Seçim, benim tercihimi görmeyen ayrı bir yapay zeka ajanına (bağımsız değerlendirici) yaptırıldı. Ajana yalnızca görsel, uygulamanın amacı, marka kişiliği ve 5 ölçüt verildi.
3. Seçilen ikon, değerlendiricinin 3 iyileştirme önerisiyle yeniden çizildi ve uygulamaya, açılış ekranına, Play mağaza ikonuna ve öne çıkan görsele uygulandı.

## Değerlendirici puanları (1–5)
| İkon | 48px okunurluk | Fikri anlatma | Ayırt edicilik | Marka uyumu | İşçilik | Toplam |
|---|---|---|---|---|---|---|
| A Çan + nota (eski) | 4 | 4 | 2 | 4 | 4 | 18 |
| B Nota + halkalar | 5 | 3 | 2 | 4 | 3 | 17 |
| **C Ses dalgası → nota** | 4 | 5 | 3 | 4 | 4 | **20** |
| D Piyano tuşlu çan | 3 | 2 | 4 | 3 | 4 | 16 |
| E Müzik kutusu | 2 | 2 | 4 | 3 | 3 | 14 |
| F 8-bit nota | 3 | 2 | 5 | 4 | 2 | 16 |

**Seçim: C.** Gerekçe (değerlendiricinin sözleriyle özet): Uygulamanın ana fikrini, yani "şarkı girer, sadece melodi çıkar" dönüşümünü anlatan tek ikon. 48px'te de dengeli okunuyor; zil + nota kalıbı rakiplerde çok yaygın.

## Uygulanan iyileştirmeler
1. Dalga çubukları nota sapıyla aynı kalınlığa getirildi (3 → 4.5 birim).
2. Dalga ile nota arasındaki boşluk açıldı (dönüşüm hissi).
3. Grup optik merkeze alındı; bayrağın yanına sönük tek bir "çalma halkası" eklendi (zil sesi mesajı).

## Nerede kullanıldı
- `app/src/main/res/drawable/ic_launcher_foreground.xml` (uygulama ikonu, tematik/monokrom ikon, açılış ekranı)
- `docs/store/ikon_512.png`, `docs/store/one_cikan_1024x500.png`
- `app/src/main/play/listings/*/graphics/` (Gradle Play Publisher ile mağazaya yüklenecek görseller)
