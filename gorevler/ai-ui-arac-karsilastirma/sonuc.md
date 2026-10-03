# Sonuç — hangi yol iyi sonuç verdi?

Ölçüm: `npm run karsilastir` (Playwright Chromium 141, 1440×900 ve 390×844), 2026-10-03.
Ayrıntılı tablo ve ekran görüntüleri: `sonuclar/rapor.html`, ham veri: `sonuclar/sonuclar.json`.

| Sıra | Yol | Puan | Brif | axe (ciddi düğüm) | Klavye odağı | AI varsayılan işareti | Süre / token* |
|---|---|---|---|---|---|---|---|
| 1 | **B — Claude Code + resmi frontend-design süreci** | **100** | 15/15 | 0 | 8/8 görünür | yok | ~7,3 dk / ~95 bin |
| 2 | C — Stitch DESIGN.md + `@google/design.md` CLI | 95 | 15/15 | 0 | 8/8 görünür | krem+terakota, 10 BÜYÜK HARF etiket, kart kiti | ~4 dk / ~81 bin |
| 3 | A — Claude Code, ham prompt | 84 | 15/15 | 22 (kontrast) | 7/14 görünür | krem+terakota, Inter, kart kiti, orta noktalar | ~2 dk / ~57 bin |
| 4 | D — shadcn/ui registry `dashboard-01` (v0 yığını, AI'sız) | 49 | 2/15 | 1 + 1 kritik (`button-name`) | 20/20 görünür | — (ama İngilizce şablon, 1,1 MB) | ~3 dk kurulum |

\* Alt ajanların kendi raporladığı süre ve token; aynı model (Claude, bu oturum) üzerinde.

"Geçen hafta" kullanıcı akışı: A, B, C'de tıklayınca veriler değişti (masaüstü + mobil); D'de bu seçenek yok.

## Gözle kontrol (ekran görüntülerinden, insan notu)
- **B**: tek kalın öğe grafik; özet ölçütler fiş gibi tek şeritte; bugünkü (başlamamış) gün taralı; geçen hafta kesikli çizgiyle. En özgün ve okunur sonuç.
- **C**: düzenli ve tutarlı (token disiplini işe yaramış), ama görünüm A ile neredeyse aynı aile (krem zemin, kahverengi/terakota, Fraunces). Mobilde üst menü yatay kaydırmalı; "Personel" ilk ekranda kesik.
- **A**: temiz ama tipik "AI paneli": yuvarlak gölgeli kartlar, BÜYÜK HARF tablo başlıkları, krem+terakota. Mobilde grafik etiketleri ~6 px, okunmuyor. 22 kontrast ihlali.
- **D**: kaliteli bileşenler ama brief'le ilgisi yok ("Acme Inc.", "Documents", İngilizce). Kendi başına "AI UI aracı" değil; v0 gibi araçların üzerine kurduğu taban.

## Öne çıkan bulgular
1. **Yakınsama gerçek.** A ve C birbirini görmeden aynı krem+terakota+Fraunces yönünü seçti; B'nin ilk taslağı da
   tam olarak `#F4F1EA` + `#D97757` (frontend-design skill'inin "kaçın" listesindeki ilk madde) idi. Onu değiştiren şey
   skill'in "brief'e karşı gözden geçir" adımı oldu (`denemeler/b-claude-frontend-design/plan.md`).
   Bu, HN'deki "AI beige slop" şikâyetini ve Superdesign/DEV testlerindeki "ilk taslaklar yakınsadı" bulgusunu doğruluyor.
2. **Ekran görüntüsüyle eleştiri döngüsü** erişilebilirlik ve mobil hatalarını kapattı (B: 0 axe ihlali, mobil sipariş satırları düzeltildi).
   Bedeli: ~3,5 kat süre.
3. **DESIGN.md + lint** kontrastı garanti etti (C: 0 axe ihlali, tek geçişte) ama estetik yönü kendisi düzeltmiyor;
   token'ları kim yazıyorsa onun varsayılanını taşıyor. CLI eksikleri: `css-vars` tipografiyi dışa aktarmıyor; hiçbir çıktı lineHeight içermiyor.
4. **Registry bloğu** hızlı ve erişilebilir bileşen verir ama içerik/dil uyarlaması olmadan brief'i karşılamaz;
   ayrıca TS strict modunda derlenmedi ve 404 veren avatar yolu taşıyor.

## Öneri (tekrar kullanılabilir akış)
1. Brief'i yaz (`akis/brief.md` örneği).
2. Tasarım yönünü **frontend-design süreciyle** planla (plan → brief'e karşı gözden geçir), yönü **DESIGN.md**'ye dök ve `lint` ile kontrastı doğrula.
3. Bileşen iskeleti gerekiyorsa **shadcn/ui registry**'den al, içeriği brief'e uyarla.
4. Ekran görüntüsüyle 1–2 eleştiri turu yap.
5. `npm run karsilastir` ile ölç; eşik: axe kritik/ciddi 0, mobil taşma yok, brif 15/15, "Geçen hafta" akışı çalışıyor.

## Sınırlar (dürüstçe)
- v0, Lovable, Bolt, Stitch web, Figma Make, Magic Patterns, UX Pilot, Subframe, Paper, Aura, 21st.dev Magic **denenmedi**: hesap/API anahtarı yok
  (21st.dev registry bu ortamdan 403). Aynı akışa eklemek için `README.md` → "Hesaplı araç ekleme".
- X araması giriş/ödeme duvarı (402) nedeniyle doğrudan okunamadı; araç listesi web, resmi repolar ve HN'den çıkarıldı.
- Puanlama sezgiseldir (ağırlıklar `akis/puan.mjs`); estetik yargıyı tam ölçemez, bu yüzden gözle kontrol notları ayrıca yazıldı.
- Tek brief, tek çalıştırma: araçların ortalama davranışını değil, bu denemeyi gösterir.
- Windows/telefon cihazında test yapılmadı; mobil yalnız 390×844 Chromium görünümüyle ölçüldü.
