# Yapay Zekâ ile Profesyonel Web Sitesi Kur (Adım Adım Rehber) — Uğur Akkaya Digital Pro — 19 dk — https://www.youtube.com/watch?v=9DYDT_jQwt8

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu). Öncelik: C (düşük izlenme).

## Tek paragraf: ne yapıyor, sonuç ne
Kodlama bilmeyenlere yönelik bir "Hostinger Horizons ile site yap, işletmelere sat" videosu. Hostinger'ın paketlerini (Premium = 3 site, Business = 50 site) ve Horizons'un kredi mantığını (her mesaj = 1 kredi) anlatıyor, referans linkini (%20 indirim) birkaç kez öne çıkarıyor. Ekranda iki örnek gösteriyor: (1) kendi eğitim/Meta reklam danışmanlığı sitesi (yönetici girişi, öğrenci girişi, başvuru formu, başvuruyu onayla/reddet paneli; veriler Supabase'de), (2) bir butik e-ticaret sitesi (kategori, sepet, checkout, admin'de ürün/stok/fiyat/görsel düzenleme, sipariş bildirimi, Meta Pixel kurulumu). Asıl değerli kısım açıklamaya koyduğu uzun "tek prompt": önce onboarding soruları sorduran, sonra Supabase + admin panel + tracking + e-posta + SEO + güvenlik + Vercel uyumlu deploy'u adım adım tarif eden bir e-ticaret kurulum prompt'u. Promptu baştan sona yazarken canlı göstermiyor; çoğunlukla hazır sonucu gezdiriyor. Tasarım tarafı tek bir "daha premium hissettirsin" düzeltmesiyle sınırlı.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Hostinger** web hosting (Premium / Business paketleri) + **Hostinger Horizons** (prompt ile site üreten AI builder, kredi bazlı) — açıklamadaki referans linki: https://hostinger.com?REFERRALCODE=R90GAMZEUPSK
- **Supabase** (Horizons içinden "Entegrasyonlar" bölümünden bağlanıyor; Auth, PostgreSQL, Storage, Edge Functions; Project URL + anon public key isteniyor)
- Meta Pixel, Meta Conversion API, GA4, Google Tag Manager (prompt'ta ve Horizons'un kurduğu entegrasyonlarda)
- Ödeme sağlayıcısı olarak Horizons'un sorduğu seçenekler: Stripe, PayTR, iyzico (videoda "şimdilik kalsın" diyor); eğitim satışı için Shopier / havale-EFT anılıyor
- ChatGPT'yi "asistan gibi" kullanabileceğini söylüyor (detay yok)
- Vercel (prompt'taki deploy hedefi)

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Hostinger hesabı aç (Google ile giriş) → paket seç: kendi işletmesi için Premium (3 site, 1 yıl ücretsiz domain, WordPress), site satıp gelir yapmak için Business (50 site).
2. Barındırmayı uzun dönem al (kendisi 48 ay öneriyor).
3. Horizons kredisini **aylık** al: kendi işletmesi için 30 kredi (her mesaj 1 kredi, "selam yazma, kredin gider") ya da garanti için 70; işi iş modeli yapacaklara 200 kredi/ay ("200 kredide 10-15 profesyonel site").
4. Açıklamadaki uzun e-ticaret prompt'unu Horizons'a yapıştır → Horizons önce onboarding sorularını soruyor (işletme adı, sektör, marka hissi, logo, renkler, dil, para birimi, WhatsApp vb.) → tek mesajda cevapla.
5. Horizons → Entegrasyonlar → Supabase → proje adı ver ("Butik") → Supabase'de Project Settings → Data API'den **Project URL** ve **anon public key**'i kopyalayıp Horizons sohbetine yapıştır. Horizons bazı adımlarda "Supabase'e git, SQL editöre şu kodu yapıştır" diye yönlendiriyor.
6. Horizons ek sorular soruyor: ödeme sağlayıcısı (Stripe/PayTR/iyzico — "şimdilik kalsın"), e-posta bildirimleri için hangi servis (Supabase dedi).
7. Sonuç: Meta Pixel, GA4, GTM entegrasyonları kurulmuş; admin'de toplam satış, sipariş/ürün sayısı, yeni sipariş bildirimi, ürün ekleme (ad, açıklama, kategori, stok, fiyat, indirimli fiyat, görsel).
8. Beğenilmeyen yer için tek cümle düzeltme: arka plan/şablon "daha premium hissettirsin, çok sade olmuş" → ana sayfa değişti.
9. Eğitim sitesi akışı (anlatım): ana sayfa ikna → "eğitime katıl" → form → ödeme (PayTR/Shopier ya da IBAN ile havale) → başvuru admin paneline düşer → WhatsApp'tan "ödedim" → admin onaylar → öğrenci videolara erişir. Onay/ret Supabase kaydını da güncelliyor/siliyor.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
Açıklamadaki "detaylı prompt komut" (e-ticaret kurulumu; emojili bölüm başlıkları sadeleştirildi, içerik aynen):

```
Aşağıdaki talimatları eksiksiz uygula.
Kendi başına varsayım yapma.
Önce onboarding sorularını sor, cevapları al, ardından sistemi kur.
ROLÜNÜN:
Sen production-grade e-commerce platformları kuran senior bir full-stack ajans yapay zekasısın.
Amacın hazır tema kurmak değil, ölçeklenebilir ve yönetilebilir bir e-ticaret altyapısı oluşturmaktır.
Bu kurulum HER kullanıcı için sıfırdan ve benzersiz olmalıdır.
Önceden yapılmış hiçbir sitenin kopyasını üretme.

ADIM 1 — ONBOARDING SORULARI (ÖNCE BUNLARI SOR)
Kuruluma başlamadan önce kullanıcıya tek mesajda cevaplayabileceği onboarding soruları sor.
Sorulması ZORUNLU alanlar:
İşletme adı / Sektör (kadın giyim / erkek giyim / unisex / aksesuar / farklı) /
Marka hissi (premium / minimal / modern / spor / lüks / soft) / Logo var mı? (varsa yükle) /
Ana renkler / Site dili / Para birimi / Hedef ülke / WhatsApp iletişim numarası / Domain var mı? /
Çoklu dil gerekli mi? / Çoklu para birimi gerekli mi? / Ürün varyantı olacak mı (beden/renk)?
Teknik sorular:
Supabase Project URL / Supabase Anon Key / Meta Pixel ID (opsiyonel) / Meta CAPI Token (opsiyonel) /
GA4 ID (opsiyonel) / GTM ID (opsiyonel)
Kullanıcıdan cevapları tek mesajda vermesini iste.
CEVAP GELMEDEN siteyi üretmeye başlama.

ADIM 2 — SİSTEM MİMARİSİ
Kullanıcı cevapladıktan sonra production-grade e-commerce sistemi kur.
Frontend: Modern component tabanlı yapı / Mobil öncelikli tasarım / SEO uyumlu sayfalar
Backend: Supabase Auth / Supabase PostgreSQL / Supabase Storage / Supabase Edge Functions
Role System: Admin / User

ADIM 3 — DATABASE & SUPABASE
Şu tabloları oluştur:
users, products, categories, orders, order_items, carts, payments, settings, admin_logs,
inventory_logs, cookie_consents, pixel_events, email_logs, notification_logs, refunds
Foreign key ilişkileri doğru kurulsun.
RLS policies aktif olsun.

ADIM 4 — ADMIN PANEL (SHOPIFY SEVİYESİ)
Admin paneli ayrı ve güvenli route olsun.
Özellikler: Ürün CRUD / Kategori yönetimi / Stok yönetimi / Sipariş yönetimi / Kullanıcı listeleme /
Site ayarları / Pixel & Analytics ayar alanı / Log sistemi / Analytics dashboard
Temiz, minimal ve admin dostu arayüz.

ADIM 5 — FRONTEND SAYFALARI
HomePage, ProductDetailPage, CartPage, CheckoutPage, OrderConfirmationPage, OrderHistoryPage, ProfilePage
Özellikler: Arama / Filtre / Sıralama / Pagination / Variant seçimi / Sepet senkronizasyonu

ADIM 6 — ORDER FLOW (ÖDEME ŞİMDİLİK AKTİF DEĞİL)
Ödeme altyapısı kurulabilir yapıda olsun ama aktif olmasın.
Checkout sonrası: status = pending_payment
Şunlar yapılmasın: Inventory düşümü / Purchase event tetikleme
Webhook geldiğinde aktif olacak şekilde hazır bırak.

ADIM 7 — TRACKING & ANALYTICS
Cookie consent banner ekle.
Onaydan sonra: Meta Pixel events: ViewContent / AddToCart / InitiateCheckout
Conversion API server-side hazır olsun.
GA4 + GTM dataLayer kur.

ADIM 8 — EMAIL & BİLDİRİMLER
Sipariş alındı maili / Sipariş statü maili / Admin yeni sipariş bildirimi / Stok uyarısı
Edge Functions üzerinden çalışsın.

ADIM 9 — SEO & PERFORMANS
Lazy loading / Image optimization / Code splitting / Core Web Vitals uyumu / Meta title / description alanları

ADIM 10 — GÜVENLİK
Role-based access / Admin endpoint koruması / Input validation / XSS & injection önlemleri / Security headers

ADIM 11 — TASARIM
Kullanıcının onboarding cevaplarına göre: benzersiz marka hissi üret.
Daha önce oluşturulmuş bir sitenin tasarımını tekrar etme.

ADIM 12 — DEPLOYMENT
Deployment yapısı production-ready olsun.
Environment variables hazırla.
Vercel uyumlu yapı kur.

SON KURAL
Bu kurulum: Demo değildir. Tema kurulumu değildir. Gerçek satış sistemine dönüşebilecek altyapıdır.
Kurulum bittikten sonra: Eksik kalan teknik ayarları checklist halinde raporla.
```

Videodaki düzeltme prompt'u (sözlü aktarım): "Sitenin arka planı / şablonu daha premium hissettirsin. Çok sade olmuş."

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- Tasarım kuralı neredeyse yok. Tek mekanizma: onboarding'de "marka hissi" (premium/minimal/modern/spor/lüks/soft) + ana renkler + logo sorulması ve prompt'taki "benzersiz marka hissi üret, daha önceki bir sitenin tasarımını tekrar etme" satırı. İddiası: "Bu prompt'u 100 kişi alsa 100 farklı site çıkar."
- Sonradan tek cümlelik "daha premium hissettirsin" düzeltmesi.
- Font, animasyon, görsel üretimi: Videoda yok.

## Hatalar ve çözümleri
- Ürün kategorilerinde yanlış etiket ("parfüme tişört yazılmış") — "önemli değil, düzenlenecek" diye geçiyor, çözüm gösterilmiyor.
- Supabase bağlantısı: Horizons kendisi URL + anon key istiyor ve SQL editörüne yapıştırılacak kodu veriyor; kullanıcıyı adım adım yönlendiriyor.
- Kredi israfı uyarısı: her mesaj 1 kredi → gereksiz mesaj atma; prompt'u "yüzlerce kredi harcayarak" olgunlaştırdığını söylüyor.
- "Sadece bir komut yazıp teslim etmek değil, sorunlarla karşılaşacaksınız" uyarısı var ama somut hata/çözüm örneği yok.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Kendi eğitim sitesi için "bir ajansa yaptırsam 30-40.000 TL isterlerdi" diyor; piyasada reklamı görülen siteler "10.000-15.000 TL".
- Satış önerisi: Facebook/Meta reklam hesabı aç, "web sitesi 5.000 TL" diye reklam bas — "millet gelir"; Business paketle 50 siteye kadar. Örnek fiyatlama olarak "bir siteyi 7.000'den tasarladığınızı düşünelim" diyor.
- Uyarılar: işin yükünü kaldırabilecek misin, şirket/işletme kurman gerekir.
- Kendisi de "uygun maliyetle" site yapma hizmeti satıyor (WhatsApp numarası açıklamada/yorumlarda) — fiyat vermiyor.
- Maliyet tarafı: Hostinger Business (48 ay önerisi) + Horizons aylık 30/70/200 kredi. Kesin TL rakamı söylemiyor.

## Bizim fabrikaya alınacaklar (somut)
Değer orta-düşük: video bir Hostinger referans tanıtımı, tasarım kalitesine dair hiçbir şey yok. Ama açıklamadaki prompt işe yarar bir **e-ticaret kapsam kontrol listesi**:
1. **Onboarding-önce kalıbı** — "CEVAP GELMEDEN üretme, tüm soruları tek mesajda sor" kuralı; bizim brief formumuza e-ticaret soruları eklenir: sektör, marka hissi (6 seçenek), para birimi, hedef ülke, WhatsApp, domain, çoklu dil/para birimi, varyant (beden/renk), Pixel/CAPI/GA4/GTM ID'leri.
2. **E-ticaret iskelet spesifikasyonu** — 15 tabloluk Supabase şeması (users…refunds, RLS açık), 7 sayfa listesi, admin özellik listesi; "ödeme kurulabilir ama kapalı, checkout → status=pending_payment, stok düşümü ve Purchase event webhook'a kadar bekler" yaklaşımı (müşteri ödeme sağlayıcısını seçmeden siteyi teslim etmek için iyi).
3. **Tracking paketi** — cookie consent sonrası ViewContent/AddToCart/InitiateCheckout + server-side CAPI + GA4/GTM dataLayer; Türk KOBİ'ye "Meta reklamına hazır site" diye satılabilecek bir kalem.
4. **Son adım: "eksik kalan teknik ayarları checklist halinde raporla"** — teslim öncesi kendi kendine eksik raporu; bizim teslim checklist'imize bağlanır.
5. **Fiyat referansı (TR pazarı)**: reklamlarda 10-15 bin TL, ajans 30-40 bin TL algısı, agresif giriş 5-7 bin TL. Bizim "fıstık gibi" kalite seviyesinin bu bandın üstünde konumlanması gerektiğini doğruluyor.
6. Eğitim/kurs satan danışman için akış (form → havale/PayTR → admin onay → öğrenci erişimi) KOBİ'ye hazır bir paket fikri olarak not.
Tasarım için alınacak bir şey yok; Horizons'a geçmek önerilmez (kredi bazlı, kod kontrolü bizde değil).
