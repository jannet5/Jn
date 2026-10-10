# Sell AI Websites To Local Businesses In 2026 (Full Course) — Abou Toure — 14 dk — https://www.youtube.com/watch?v=H4dNdlzVGnw

## Tek paragraf: ne anlatıyor
Abou Toure, birkaç yıldır kullandığı "ücretsiz web sitesi mockup'ı" yöntemini anlatıyor: Instagram'da niş+şehir araması (örn. "landscaping Miami") ile 20 işletme aç, 3 postunu beğen + takip et, müşteri gibi soru sorarak ana gelen kutusuna gir, cevap gelince sesli mesajla "sitenizi zaten hazırladım, 10-15 dakikalık görüşmede göstereyim" de. Mockup'ı Relume (relume.io) ile üretiyor: şirketi birkaç kelimeyle tarif et → AI site haritası → wireframe + metinler (2 dakikadan az), bölümleri "shuffle", renk/font "surprise me", "pitch concepts" linkini müşteriye gönder; istersen Figma'ya aktarıp logo/fotoğraf ekle (20-30 dk) veya Webflow'a aktarıp canlı site yap. Bir müşteriyi sadece Instagram DM ile 1.500 $'a imzaladığını ekran görüntüsüyle gösteriyor.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- **Kime:** Yerel hizmet işletmeleri (örnek niş: peyzaj/landscaping; Miami, Toronto GTA). Sitesi olmayan veya kötü sitesi olan, Instagram'da aktif işletmeler.
- **Ne:** Ücretsiz wireframe/mockup (Relume) → ücretli "tam yayınlanmış web sitesi" (Webflow'a aktarım, domain + hosting).
- **Kaça (USD):** "thousands of dollars"; somut örnek 1.500 $ (Instagram DM'den gelen peyzaj müşterisi). Aylık ücret vermiyor.
- **Paketleme:** 1) Ücretsiz mockup (5 dk/ilgili müşteri; Figma'da görsellerle 20-30 dk) 2) 10-15 dk'lık görüşme 3) Tam site satışı. Relume'de birden çok "pitch concept" üretip tek linkle farklı tasarım alternatifleri sunuyor.
- **Araç maliyeti:** Relume ücretsiz plan = 1 proje (her müşteride öncekini silip yeniden üretiyor).

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- **Kanallar:** Soğuk arama (ayrı "cold calling masterclass"ı Instagram DM "cold calling script" yazana gönderiyor) ve soğuk Instagram DM (bu videoda). Reklam bütçen varsa reklam da olur, ama sıfır bütçe varsayımı.
- **Instagram akışı:**
  1. Arama → "landscaping Miami" (niş + şehir) → çıkan gönderilerden ~20 hesabı yeni sekmelerde aç.
  2. Her hesapta 3 post beğen + takip et.
  3. Müşteri gibi soru sor (ana gelen kutusuna düşmek için; "requests" klasörüne değil).
  4. Cevap gelince sesli mesaj (voice note) ile pitch; sesli mesaj cevap oranını metinden yükseltiyor.
  5. Hemen telefon numaranı gönder → SMS'e taşı → görüşme.

**Adım 1 – ilk mesaj (aynen):**
> "Hey, I just found your profile and love your work. Do you work in all of Miami?"

(TR: Satış yok, sadece müşteri gibi bir soru; imla hatası yapma uyarısı. GTA örneğinde: "Hey, just found your profile, do you work in all of the GTA?")

**Adım 2 – cevap gelince pitch (sesli mesaj metni, aynen):**
> "Hey, I just found your profile and like I said your work looks amazing. I slid up, I noticed your website could use improvement [sitesi yoksa:] I slid up and I tried to click a link in your bio and I couldn't find a website. I was curious if you ever thought about upgrading your website / I was curious if you ever thought about getting a website. No worries, I already have it fully built out for you. I just want to show it to you if you have time for a 10 to 15 minute call."

(TR: "Zaten hazırladım, sadece göstermek istiyorum" ile riski sıfırlıyor; 10-15 dk görüşme istiyor.)

**Gerçek dönüşüm örneği:** Müşteri SMS'ten "Yeah of course, I just want to see the website" yazdı → 1.500 $'a imzalandı. Videoda Miami peyzaj firması ilk mesaja birkaç dakikada cevap verdi.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
- **Araçlar:** Instagram, Relume (relume.io – AI sitemap/wireframe/style guide), Figma + Relume Figma Kit eklentisi, Webflow + Relume Webflow app, Unsplash (geçici görsel), müşterinin Instagram fotoğrafları.
- **Relume akışı:**
  1. relume.io → Start free → New project → şirketi 1 cümleyle tarif et (prompt örneği aynen: `"Abou Landscaping is a landscape [company]..."` — birkaç kelime yeterli).
  2. "Generate site map" → AI tam site haritası.
  3. "Wireframe" → AI tüm sayfaları ve metinleri (copywriting) yazar (<2 dk).
  4. İstemediğin bölümü seç → Delete; beğenmediğin bölüm → "Shuffle component" veya "Regenerate copy"; sağ ok ile alternatif hero varyantları.
  5. "Style guide" → renk paleti shuffle, font ara (örn. Poppins), "Surprise me" ile renk+font tamamen yenilenir → aynı wireframe'i farklı müşterilere farklı görünümle tekrar kullan.
  6. "Pitch concepts" → birden çok konsept → "Copy link" → müşteriye gönder (Figma export'a gerek kalmaz).
  7. Canlı siteye çevirmek için: Export → Webflow → "Clone style guide" → Webflow'da Create site → Apps → Relume → Install → projeyi seç → konsepti yükle → "Import all pages" → Share read-only link (müşteri butonlara tıklayarak gezer) → Publish → custom domain + hosting Webflow'dan.
  8. Özelleştirme için: Export → Figma → Relume Figma Kit → Plugins → Relume → wireframe import ("2 saat 15 dakika tasarruf" gösteriyor) → yeni Figma dosyasına kopyala → mega menüyü sil → işletme fotoğraflarını ve logoyu yerleştir, FAQ ve metni düzenle.
- n8n/Make otomasyonu yok.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
Videoda yok (sadece "paran varsa reklam yap" cümlesi; bütçe/kurgu yok).

## Hatalar / uyarılar
- DM'de imla hatası yapma; ilk mesaj satış kokmasın (yoksa requests klasöründe kalır).
- Düz metin yerine sesli mesaj daha iyi cevap oranı veriyor (kendi deneyimi; test et).
- Mockup ne kadar iyi görünürse müşteri o kadar ilgili; görselsiz çıplak wireframe gönderme, logo + gerçek fotoğraf ekle.
- Relume ücretsiz planda 1 proje sınırı; ücretli plan maliyeti söylenmiyor.
- Açıklamadaki Relume linki affiliate (relume.1stcollab.com) — açılmadı; kaynak dosyası yok. Cold calling scripti videoda yok (Instagram DM ile isteniyor).

## Bizim ajansa alınacaklar (somut)
1. **Instagram "3 beğeni + takip + müşteri sorusu" protokolü:** Günde 20 hesap (örn. "diş kliniği İstanbul", "peyzaj Ankara", "kafe Kadıköy" aramaları); ilk mesaj Türkçe: "Merhaba, profilinizi yeni gördüm, işleriniz çok güzel. [İlçe]'nin tamamında hizmet veriyor musunuz?" → cevap gelince sesli mesajla: "Bio'daki linke tıkladım, site bulamadım / siteniz yenilenebilir gibi geldi. Merak etmeyin, sizin için zaten bir taslak hazırladım, 10-15 dakikalık görüşmede göstereyim mi?"
2. **Relume'u mockup fabrikası yap:** Her niş için 1 kez wireframe üret, style guide'da "surprise me" ile her müşteriye farklı renk/font ver; "pitch concepts" linkiyle 2-3 alternatif göster. Hedef: ilgili müşteri başına ≤15 dk.
3. **Sesli mesaj + numara ver → WhatsApp'a taşı:** DM'de kalma; ilk cevaptan sonra numara gönder, görüşmeyi WhatsApp/telefonda kapat.
4. **Fiyat çıpası:** Mockup gösterildikten sonra tam site teklifi (videodaki 1.500 $ örneğinin TR karşılığı olarak 20.000-40.000 ₺ kurulum) + aylık hosting/bakım (videoda yok, biz ekleyelim).
5. **Teslim altyapısı:** Relume → Webflow (ya da bizim Lovable/Base44 hattımız) ile yayın; müşteriye "read-only canlı link" göstererek onay al, sonra domain bağla.
