# Yapay Zeka İle 5 Dakikada Web Site Nasıl Yapılır? CODEX İle Web Site Kur ve Yayınla! — Özgür W — 11 dk — https://www.youtube.com/watch?v=Lk3rSo1cXBs

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu).
## Tek paragraf: ne yapıyor, sonuç ne
OpenAI Codex masaüstü uygulamasıyla (ChatGPT hesabıyla giriş), bir klasöre iki Markdown dosyası (web sitesi sayfa yapısı + işletme bilgileri) ve bir fotoğraf koyup @ ile etiketleyerek "web tasarım ajansı" için koyu temalı, buzlu cam + gradyan efektli statik HTML/CSS/JS site ürettiriyor. Tally'den ücretsiz iletişim formu gömdürüyor, sonra "turuncu koyu tema, daha fütüristik, kaymaları düzelt, çok sayfalı yap" revizyonu ve ayrı bir hero tasarımı ile animasyonlu girişli çok sayfalı site elde ediyor. Hostinger Premium'a (12 ay ~1.439 TL, ücretsiz domain + e-posta) File Manager → public_html ile sürükle-bırak yükleyip yayınlıyor; güncelleme, profesyonel e-posta kurulumu ve Google Analytics etiketi ekleme de gösteriliyor. Basit/orta seviye, "fıstık" değil ama hızlı ve ucuz uçtan uca akış.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **OpenAI Codex** masaüstü (Windows) — ChatGPT hesabıyla giriş; ücretsiz de kullanılabilir, 20 $ ChatGPT planı önerilir, 200 $ planda sınırsıza yakın. Ayarlar: izin (varsayılan / tam erişim), model, **zeka seviyesi: Yüksek** ("daha güzel tasarım yapsın"; ekstra yüksek hakları hızlı bitirir), hız: Standart (Hızlı hakları bitirir).
- Codex özellikleri: proje = mevcut klasör; @ ile dosya/görsel etiketleme; sağda + ile dosya gezgini, yan sohbet, Codex içinde site önizleme, terminal; koda yorum ekleyip yorumla prompt gönderme; sol altta kalan kullanım hakları.
- **Tally** (tally.so; transkriptte "teli.O") — ücretsiz form; standart embed kodu.
- **Hostinger** — açıklamada affiliate link: https://www.hostg.xyz/SHIo5 ("%80 indirimli hosting & domain").
- **Google Analytics** (+ başlıkta Search Console; transkriptte sadece GA etiketi anlatılıyor) — Meta da benzer.
- Yazar linkleri: https://ozgurw.com/bio , https://instagram.com/ozgurwai
- Skill / UI kütüphanesi: Videoda yok.

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Google'da "Codex" → indir (Windows) → ChatGPT hesabıyla bağlan.
2. Projeler → "mevcut bir klasörü kullan" → yeni boş klasör ("yeni web site").
3. İki dosya hazırla (yapay zekayla örnek olarak üretilmiş): **web sitesi yapısı** (sayfa yapısı, bilgiler, müşteri yorumları) ve **işletme bilgileri** (adres, ana hizmetler, öne çıkanlar, paketler) + fotoğraf(lar). Dokümanı **Markdown (.md)** olarak indir ("yapay zeka modelleri bu şekilde daha iyi anlar"), klasöre koy.
4. Prompt'ta @ ile dosyaları ve görseli etiketle, ana prompt'u yaz (aşağıda).
5. Tally'de form: Ad Soyad, E-posta, Telefon; dili Türkçe, buton "Gönder" → Publish → Embed → Standard → kodu kopyala → Codex'e "Formu buradaki koddan göm" + kod.
6. Zeka seviyesi Yüksek, hız Standart → gönder.
7. Klasörden index.html aç → kaymalar var.
8. Revizyon prompt'u (turuncu koyu tema, fütüristik, kaymaları düzelt, çok sayfalı) → hizmetler, rezervasyon, blog, iletişim sayfaları. Blog'u gereksiz buluyor (yönetim paneli gerektirir, kaldırılabilir).
9. Ek olarak ayrı bir hero tasarımı yaptırıyor → animasyonlu giriş.
10. Hostinger: Premium plan (3 siteye kadar, 1 yıl ücretsiz domain, 1 yıl ücretsiz profesyonel e-posta, SSL) → 12 ay → ödeme.
11. Panel → Site ekle → "Özel PHP/HTML web sitesi" → domain seç → lokasyon (Almanya otomatik) → Dosya Yöneticisi → public_html → default.php sil → sadece site dosyalarını (html sayfaları, görsel, script, style) sürükle-bırak (işletme bilgileri .md'lerini yükleme) → anında yayında.
12. Güncelleme: tüm dosyaları tekrar sürükle → "Hedef klasördeki tüm dosyaları değiştir".
13. E-posta: panel → E-postalar → ücretsiz e-posta talep et → site seç → ücretsiz paket → "hello@domain" + şifre → posta kutusu.
14. Google Analytics: yeni mülk → Web → URL → Google etiket kodunu kopyala → Codex'e "Google etiketini web siteme ekle" → dosyaları Hostinger'a tekrar yükle.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Ana prompt: "@web-site-sayfa-yapisi @isletme-bilgileri @gorsel — Özgür W Yapay Zeka web tasarım ajansı için modern, mobil uyumlu, estetik ve koyu temalı, buzlu cam ve gradyan efektler olan HTML, CSS, JavaScript yapısında bir web sitesi hazırla."
- Form: "Formu buradaki koddan göm." + Tally embed kodu
- Revizyon: "Turuncu koyu temada olmasını istiyorum. Çok daha fütüristik bir tasarıma dönüştür. Tasarım kaymalarını düzelt. Çok sayfalı yapıya geç."
- Hero: ayrıca hero tasarımı istendi (prompt metni okunmuyor).
- Analytics: "Google etiketini web siteme ekle." + etiket kodu
- Skill metni: Videoda yok.

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- Prompt'ta stil anahtar kelimeleri: **koyu tema, buzlu cam (glassmorphism), gradyan, modern, mobil uyumlu, estetik**; revizyonda "fütüristik, turuncu koyu tema".
- Zengin girdi: sayfa yapısı + işletme bilgileri + müşteri yorumları + gerçek fotoğraf, **.md formatında**.
- Codex zeka seviyesi **Yüksek** → daha iyi tasarım.
- Ayrı bir **hero tasarım** turu ile animasyonlu giriş.
- Font, görsel üretimi, animasyon kütüphanesi: Videoda yok.

## Hatalar ve çözümleri
- İlk sürümde tasarım kaymaları → "Tasarım kaymalarını düzelt" + büyük revizyon prompt'u.
- Node.js vb. yapılar build ve uyumlu (daha pahalı) hosting ister → statik site için HTML/CSS/JS seç.
- Blog sayfası yönetim paneli olmadan anlamsız → gerekirse kaldır.
- Hostinger'da default.php silinmeli; işletme bilgisi .md dosyaları yüklenmemeli.
- Güncellemede "hedef klasördeki tüm dosyaları değiştir" seçilmeli.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Müşteriye satış fiyatı: Videoda yok ("ister kendimiz ister müşterilerimiz için" diyor; kurumsal, bireysel, landing page olabilir).
- Maliyet kalemleri:
  - Codex: ChatGPT ücretsiz kullanılabilir; önerilen **20 $/ay** ChatGPT planı; **200 $** plan sınırsıza yakın.
  - Hostinger Premium: 48 ay seçilirse aylık **69 TL**, 12 ay seçilirse aylık **99 TL**; 12 ay hosting + ücretsiz domain + e-posta toplam **1.439 TL**. Premium = 3 siteye kadar, 1 yıl ücretsiz alan adı, 1 yıl ücretsiz profesyonel e-posta, SSL. En küçük paket de HTML için yeter ama domain ayrıca alınır.
  - Tally formu ücretsiz.
- Gelir modeli: Hostinger affiliate linki (açıklama).

## Bizim fabrikaya alınacaklar (somut)
1. **İki girdi dosyası standardı** (.md): `site-yapisi.md` (sayfalar, bölümler, müşteri yorumları) + `isletme-bilgileri.md` (adres, hizmetler, öne çıkanlar, paketler/fiyatlar) + gerçek fotoğraflar — müşteri onboarding formundan otomatik üretilsin.
2. **Ücretsiz form entegrasyonu**: Tally embed (Ad Soyad / E-posta / Telefon, Türkçe "Gönder") — backend gerektirmeyen statik sitede iletişim/rezervasyon formu.
3. **Teslim paketine eklenecekler**: Google Analytics etiketi (+ Search Console), profesyonel e-posta (info@/hello@ işletme domaini) kurulumu — yerel işletmeye "tam paket" olarak satılabilir ek hizmetler.
4. **Statik HTML/CSS/JS varsayılanı** — ucuz paylaşımlı hosting (Hostinger Premium: 1 planda 3 müşteri sitesi; 12 ay ~1.439 TL) ile maliyet düşük; Node/build gerektiren yapıyı sadece gerekiyorsa kullan.
5. **Hostinger deploy checklist'i**: public_html → default.php sil → yalnız site dosyalarını yükle (.md girdi dosyalarını değil) → güncellemede "tümünü değiştir".
6. Blog'u sadece yönetim paneli/CMS ihtiyacı netse ekle; yoksa çıkar (yerel işletmede bakımı yapılmayan blog kötü görünür).
7. Codex'i yedek üretim hattı olarak tut (zeka seviyesi Yüksek, hız Standart); ama "fıstık" kalite için bu videodaki akış tek başına yetmez — skill/referans/görsel üretimi katmanı bizim diğer planlarımızdan eklenmeli.
