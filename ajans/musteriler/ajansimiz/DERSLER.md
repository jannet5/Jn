# DERSLER — ajansimiz (zincirin ilk gerçek üretim testi, 2026-10-09)

## Zincire geri bildirim — neyi değiştirelim

### hatlar/web.md
1. **W3 "yerel işletme zorunluları" bu müşteriye uymuyor ve çelişiyor**: fold üstü 3 alanlı form, yorumlar, gerçek fotoğraf, harita, her hizmet/şehir için ayrı sayfa — BRIEF ise tek sayfa, form yok (WhatsApp), yorum yok diyor. → Zorunluları "fiziksel yerel işletme" profiline bağla; BRIEF'te `profil: yerel | uzaktan-hizmet` alanı aç, hat ona göre dallansın.
2. **W3 "Ask me clarifying questions" + "önce stil yok" otomatik zincirde işlemiyor.** Kapılarda onay beklenmeyen modda W3/W4 tek adım olmalı. → "Otonom mod" bölümü ekle: W3+W4 birleşik, onay yerine DURUM.md'ye karar kaydı.
3. **W5 design-review ajanı / impeccable / hallmark yok** (`.claude/agents/design-review-agent.md` repoda değil). → Ya dosyaları ekle ya da "yoksa: SS'yi parça parça kırp + Read ile bak, şu 10 soruyu sor" yedek prosedürünü yaz.
4. **Tam sayfa SS'ler Read ile okunamayacak kadar küçülüyor** (11.000 px → 64 px genişlik). → Kuralı yaz: tam sayfa SS'yi 1500-1800 px dilimlere kırp, dilimlere bak.
5. **SS betiği hazır değildi** (CLAUDE.md `node scripts/ss.mjs` diyor ama dosya yok). → `sablon/web/scripts/ss.mjs` + `kullanici-testi.mjs` olarak şablona koy (bu projedekiler kullanılabilir).
6. **Playwright tuzakları**: npm'deki sürüm `/opt/pw-browsers`teki Chromium sürümüyle uyuşmuyor → `executablePath` şart; `scroll-behavior: smooth` SS'te reveal'leri tetiklemiyor → `behavior:'instant'`; reveal/çizim için ≥1.7 s bekle. Hat dosyasına "bilinen tuzaklar" kutusu ekle.
7. **W6 cila 5. madde ("more expensive")** ölçülebilir değil. → Somut kontrol listesine çevir (ölü boşluk, boş placeholder kutu, tekrar eden kart ritmi).
8. **Google Fonts render bloklıyor** → mobil Lighthouse 81. Şablona doğrudan `preload + media=print onload` kalıbını koy; KVKK için self-host (fontsource) seçeneği yaz.

### kapilar/KAPILAR.md
9. **KAPI-3 statik landing için N/A maddeler net değil** (yükleniyor/boş/hata, form durumları, NAP). → Her maddeye "uygulanabilirlik" sütunu: statik landing'de N/A olabilir, gerekçe zorunlu.
10. **"Büyütülmüş metinde düzen bozulmuyor" nasıl test edilecek yazmıyor.** Bulduğum yöntem: `html{font-size:200%}` enjekte et, `scrollWidth` ölç. Tailwind'in tek sütunlu `grid`i `grid-cols-1` olmadan taşıyor → şablona kural.
11. **"5 alt ajan, salt okuma"** maliyetli; limitli hesapta tek betik (`kullanici-testi.mjs`) + Lighthouse aynı işi gördü. → "Limit modunda: betik + Lighthouse yeterli" notu.
12. **10K puanlaması için ölçüt yok**; strong/mixed/missing kimin gözüyle? → Her alan için 1 satır "strong şudur" tanımı ekle.
13. **Lighthouse komutu yazılı değil.** Çalışan: `CHROME_PATH=/opt/pw-browsers/chromium-1194/chrome-linux/chrome npx -y lighthouse@12 <url> --chrome-flags="--headless=new --no-sandbox" --only-categories=performance,accessibility,best-practices,seo`.

### Şablonlar (CLAUDE.md / DESIGN.md)
14. **DESIGN.md hareket süresi 180-600 ms, grafik çizimi için kısa**; ben 600 ms'ye çektim. Ya "çizim animasyonları ≤1200 ms" istisnası ekle ya da bilinçli kalsın.
15. **DESIGN.md "ink" koyu modda zeminle aynı** (#0A100D vs #0E1512) → koyu blok kayboluyor. Token tablosuna `ink-border` ekle (bu projede #26322C kullandım).
16. **DESIGN.md'de body 16→17, nav linki, buton-küçük ölçüsü yok** → 44 px ikincil buton boyutunu ve nav link boyutunu token olarak ekle.
17. **Değer defteri örneği**: "uydurma sayı yok" kuralıyla örnek rapor çelişiyor (sayısız rapor zayıf görünüyor). Karar gerek: açıkça "örnek" etiketli kurgusal sayılar serbest mi? Şu an "—" kullandım.
18. **CLAUDE.md ortak komutlarda `/impeccable polish` ve `npx @google/design.md lint`** var ama ortamda yok → "varsa" diye işaretle.
19. **site.config'deki e-posta alanı** BRIEF'te yok; alan adı alınmadan e-posta yazmak yanlış beklenti yaratır → BRIEF'e "alan adı" alanı ekle.

## Bu projede CLAUDE.md'ye eklenecekler
- SS: `node scripts/ss.mjs <önek>` (preview açıkken) · kullanıcı testi: `node scripts/kullanici-testi.mjs`.
- Grid'lerde her zaman `grid-cols-1` ile başla; uzun e-posta/URL'lere `break-all`.
- Koyu modda ink bloklarına `.ink-block` sınıfı (çerçeve otomatik).

## Sektör skill önerisi
`landing-otonom` skill'i: şablon Astro projesi (config, global.css token iskeleti, ss.mjs, kullanici-testi.mjs, Lighthouse komutu) + KAPI-2 dilim-bakış prosedürü. Bu projede ~%40 süre altyapıya gitti.

## v2 (fiyatlı sürüm) dersleri
20. **Fiyat config'de, metin şablonda**: `teklif` + `paketler` + `tl()` ile 6 fiyat, lansman adedi, 12 ay, 7 gün tek dosyadan; kullanıcı testi fiyatları sayfada arıyor → config değişince test de doğrular. Şablona `site.config.ts` iskeleti olarak konmalı.
21. **row-span-2 + h-full tuzağı**: grid'de satır kaplayan kartın altındaki figcaption komşu satıra taşar. Çözüm: figure `flex flex-col`, iç kart `flex-1`.
22. **Karar dosyası ile dürüstlük kuralı çatışınca** ("En popüler", "peşinatsız") dürüst ifadeyi seç, DURUM.md'ye "bilinçli sapma" yaz. KARARLAR şablonuna "iptal koşulu" satırı eklenmeli.
23. **Karşılaştırma tablosu mobilde**: aynı veriden ikinci render (md:hidden kartlar), bizim kart `order-first`; tablo 375'te yatay kaydırma demek.
