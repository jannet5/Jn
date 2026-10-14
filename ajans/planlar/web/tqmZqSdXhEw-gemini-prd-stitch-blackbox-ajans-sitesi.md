# YAPAY ZEKA İLE SIFIRDAN AJANS WEBSİTESİ KURDUM! (Gemini + Blackbox AI) — Ali UĞUR — 6 dk — https://www.youtube.com/watch?v=tqmZqSdXhEw

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu). Öncelik: C (düşük izlenme).

## Tek paragraf: ne yapıyor, sonuç ne
Kendi ajansı "Ugrsoft" (ursoft.com.tr) için üç adımlı bir zincir kuruyor: Gemini (Pro) ile PRD yazdırıyor → PRD'yi Stitch'e yapıştırıp tasarım (logo, marka kimliği, renk, tipografi, buton, ana sayfa/hizmetler/müşteri paneli/iletişim ekranları) ürettiriyor → aynı PRD'yi VS Code içindeki Blackbox AI (ücretsiz sürüm) eklentisine verip HTML/CSS siteyi kodlatıyor. Çıkan sitede hizmetler, referanslar, güvenlik/uyum, sertifikalar, SSS, teklif talep formu, müşteri portalı ve admin paneli var. Klasörü zipleyip Türkticaret.net cPanel'ine yükleyerek yayınlıyor. Sonunda kendisi de eksikleri söylüyor (paneller ortalanmamış, müşteri portalına giriş yok, admin paneline doğrudan erişilebiliyor). 6 dakikalık hızlı bir demo; prompt'lar tek cümle.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Google Gemini** (Pro modu) — PRD (ürün gereksinim dokümanı) üretimi
- **Stitch** (Google; videoda "Sitch/Sitge") — "Web" modu + "Thinking with 3.1 Pro" seçili; tasarım + export. Link açıklamada yok (diğer videoda: https://stitch.withgoogle.com)
- **Blackbox AI** — VS Code eklentisi, ücretsiz sürüm; alternatif olarak Claude eklentisi anılıyor
- **VS Code**
- **cPanel (Türkticaret.net hosting)** — dosya yöneticisi ile zip yükle/çıkar
- Link: açıklamada hiçbir URL yok.

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Gemini'de Pro seçili: "Ugrsoft firmam için bir ajansın PRD'sini (dokümantasyonunu) hazırla." → PRD: giriş, hedef kitle (KOBİ'ler ve işletmeler), hizmetler (web tasarım/geliştirme, sosyal medya, dijital reklam yönetimi, raporlama), müşteri iletişimi, güvenlik.
2. PRD'nin tamamını kopyala → Stitch'e yapıştır, en başa "PRD'ye uyarak bana bir web sitesi tasarla" ekle → "Web" seç → "Thinking with 3.1 Pro" seç → gönder.
3. Stitch çıktısı: PRD özeti, logo, marka kimliği (renkler, tipografi, butonlar), ana sayfa, hizmetler, müşteri paneli, iletişim ekranları. "Dışa aktar" ile PRD/ürün gereksinim dosyasını kopyalıyor.
4. VS Code'da proje klasörü aç → Blackbox eklentisine PRD'yi yapıştır: "Bu PRD'ye uygun olarak web sitesi tasarla, gerektiği yerde ben kodları vereceğim."
5. Blackbox bir plan çıkarıp "bu plana göre ilerlememi onaylıyor musunuz?" diye soruyor → "evet" → kodu yazıyor → "projeyi başlat" ile çalıştırıyor.
6. Yayın: Türkticaret hesabı → cPanel → domain yönetimi → ursoft.com.tr klasöründe ne varsa tümünü seç, kalıcı sil (404 sayfası vardı).
7. VS Code Explorer'da proje klasörünü zip'le → cPanel'de domain klasöründe "Yükle" → zip'i sürükle → "Çıkar".
8. Zip, domain klasörünün altında ikinci bir klasör oluşturuyor → o klasöre gir, tümünü seç, üst klasöre taşı → ursoft.com.tr açılıyor.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
Transkriptten (sözlü aktarım, tam metin ekranda okunmuyor):
- Gemini: "Ugrsoft firmam için bir ajansın PRD'sini yani dokümantasyonunu hazırla."
- Stitch: "PRD'ye uyarak bana bir web sitesi tasarla." + PRD metni
- Blackbox: "Bu PRD'ye uygun olarak web sitesi tasarla. Gerektiği yerde ben kodları vereceğim." + PRD metni
- Blackbox onayı: "Evet, onaylıyorum." / "Projeyi başlat."

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- Tasarım kuralı/font/animasyon tarifi: Videoda yok.
- Tek iddia: "İnternetteki AI siteleri birbirine benziyor, bu daha farklı oldu çünkü daha detaylandırdık" — farkı içerik bölümlerinin zenginliğine bağlıyor (müşteri, referans, güvenlik ve uyum, sertifikalar, süreç, sonuçlar/vakalar, SSS, teklif formu).
- Görsel tarafı Stitch'in marka kimliği çıktısına (logo, renk, tipografi, buton) bırakılmış.

## Hatalar ve çözümleri
- cPanel'de zip çıkarınca site domain kökünde değil alt klasörde kalıyor → klasöre gir, tümünü seç, domain köküne taşı. (Çözüm gösterildi.)
- Yayın öncesi domain klasöründeki eski dosyaları sil.
- Kalan eksikler (çözülmedi, sadece söylendi): panellerin ortalanmaması, müşteri paneli görünmemesi, admin paneline girişsiz erişim, müşteri portalında giriş sistemi yok. Videonun sonunda "AI'nin admin paneli/backend tarafında eksikleri var" değerlendirmesi açıklamada vurgulanıyor.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
Videoda yok. (Blackbox'ın ücretsiz sürümünü kullandığını söylüyor; hosting ücreti belirtilmiyor.)

## Bizim fabrikaya alınacaklar (somut)
Değer düşük. Yeni bir şey yok; "önce PRD/brief, sonra tasarım aracı, sonra kodlayan ajan" sırasını doğruluyor.
1. **PRD-önce akışı** zaten bizde var; buradaki tek ek: aynı PRD metnini hem tasarım aracına (Stitch) hem kodlayan ajana vermek → tasarım ile kod aynı kaynaktan beslenir.
2. **Ajans sitesi bölüm listesi** (kendi ajans sitemiz için kontrol): hizmetler, süreç, sonuçlar/vakalar, referanslar, güvenlik ve uyum, sertifikalar, SSS, ücretsiz ön görüşme / teklif talep formu, müşteri portalı.
3. **cPanel deploy notu**: zip'i domain klasörüne çıkarınca iç içe klasör tuzağı → içerikleri köke taşı (ya da klasörü değil içeriğini zip'le — VMvZuhcDdnw'daki Hostinger notuyla aynı ders).
4. Uyarı olarak: girişsiz erişilen admin paneli gibi AI çıktısı güvenlik açıkları teslim checklist'inde kontrol edilmeli.
