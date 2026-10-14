# AI ile Sıfırdan Yönetim Paneli Yaptım! İnanılmaz... (STITCH + AI STUDIO) — Ali UĞUR — 7 dk — https://www.youtube.com/watch?v=DFAkocd1nBU

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu). Öncelik: C (düşük izlenme).

## Tek paragraf: ne yapıyor, sonuç ne
"HazırWebsiteCim" firması için bir "Agency OS" (ajans yönetim paneli / müşteri takip paneli) yapıyor. Stitch'e tek cümlelik prompt veriyor (Web + Thinking 3.1 Pro) → Stitch önce marka kimliği (renk paleti, butonlar, tipografi), sonra ekranları üretiyor: dashboard, projeler, müşteriler, ödemeler, görevler, login ve register. Stitch'in "Dışa aktar → AI Studio" butonuyla ekranları + prompt'u Google AI Studio'ya aktarıyor, oraya "yönetim paneli için şifre zorunlu olsun, şifre Ali123, tüm ekranları detaylı kodla" ekleyip Build'e basıyor. AI Studio çalışan bir uygulama çıkarıyor (şifre ekranı, yanlış şifrede uyarı, proje ekleme çalışıyor); bir prompt'la Türkçeye çevirtiyor. Kodu zip olarak indirip `npm install` + `npm run build` yapıyor, çıkan `dist` klasörünü zipleyip Türkticaret cPanel'inde bir subdomain'e (demo2.hazirwebsitecim.com.tr) yüklüyor. Video "backend" diyor ama gösterilen şey frontend'de sabit şifreli bir panel; veritabanı bağlantısı gösterilmiyor ("sonraki videolarda").

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Stitch** (Google): https://stitch.withgoogle.com — "Web" modu + "Thinking 3.1 Pro"; "şu an ücretsiz, listemde en iyi ajan 1 numara"
- **Google AI Studio**: https://aistudio.google.com — Stitch'ten "AI Studio ile geliştir" exportu, Build, Code sekmesi → Export → zip indir
- Node/npm (`npm install`, `npm run build`)
- **cPanel (Türkticaret)** — subdomain, dosya yöneticisi
- Açıklamada etiket olarak React / Next.js geçiyor; konuşmacı build için "Next.js'i HTML/CSS/JS'ye çeviriyor" diyor, çıkan klasör `dist`.
- Veritabanı: "Next.js ve diğerleri Supabase kullanır, yapay zekaların geneli Supabase kullanıyor" diyor; bu videoda kurulmuyor.
- Not: Açıklamadaki zaman damgaları (14:30'a kadar, "⌘K arama çubuğu", "metrik kartları ve grafik bileşenleri" bölümleri) ~7 dakikalık videoyla uyuşmuyor; transkriptte ⌘K araması geçmiyor. Açıklama şablon/abartılı görünüyor.

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Stitch'e prompt: "HazırWebsiteCim firmam için bana bir Agency OS yani yönetim paneli tasarla. Müşterilerim için bir müşteri takip / müşteri paneli hazırla. Login ve register kısımları da olsun." → "Web" seç → "Thinking 3.1 Pro" seç → gönder.
2. Stitch önce marka kimliğini veriyor (renk paleti, butonlar, metin/tipografi), sonra ekranları tek tek üretiyor: dashboard, projeler, müşteriler (client), ödemeler, görevler, login, register (kayıt bilgi alanları).
3. "Kaydet ve uygula" → Dışa aktar menüsü → **AI Studio** → "AI Studio ile geliştir": prompt + ekranlar AI Studio'ya taşınıyor.
4. AI Studio'da prompt'a ek: "Yönetim paneli için şifre zorunluluğu olsun ve şifre Ali123 olsun. Gerekli tüm ekranları detaylıca kodla." → **Build**.
5. Test: şifre ekranı → yanlış şifrede uyarı → Ali123 ile giriş → dashboard, projeler, müşteriler, ödemeler (gelir ve faturalar), görevler, ayarlar.
6. Düzeltme prompt'u: "Yönetim panelini Türkçeye çevirir misin?"
7. Code sekmesi → Export → zip indir → masaüstünde çıkar → klasörü aç, terminal: `npm install` (yerel çalıştır, gerekirse düzenle).
8. cPanel → Alan adları/subdomain → hedef subdomain klasöründeki her şeyi sil.
9. Yerel sunucuyu Ctrl+C ile durdur → `npm run build` → `dist` klasörü oluşuyor → dosya gezgininde aç → `dist`'i zip'le.
10. cPanel → Yükle → zip'i sürükle bırak → Çıkar → zip bir alt klasör (`dist`) içinde açılıyor → içine gir, tümünü seç, Taşı ile domain köküne al, boş `dist`'i sil.
11. demo2.hazirwebsitecim.com.tr'de şifreyle giriş, proje ekleme canlıda çalışıyor.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
Transkriptten (sözlü aktarım):
- Stitch: "HazırWebsiteCim firmam için bana bir agency OS yani yönetim paneli tasarla. Müşterilerim için bir müşteri takip, müşteri paneli hazırla. Login ve register kısımları da olsun."
- AI Studio: "Yönetim paneli için şifre zorunluluğu olsun ve şifre Ali123 olsun. Gerekli tüm ekranları da detaylıca kodla."
- AI Studio: "Yönetim panelini Türkçeye çevirir misin?"
- Terminal: `npm install`, `npm run build`

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- Tasarım kuralı yazılmıyor; görünüm tamamen Stitch'in otomatik ürettiği marka kimliğine (renk paleti, tipografi, buton seti) bırakılıyor. Sıra önemli: Stitch önce marka sistemini, sonra ekranları üretiyor ve AI Studio'ya ekranlarla birlikte aktarılıyor, böylece kodlanan uygulama tasarımı koruyor.
- Font/animasyon/görsel üretimi: Videoda yok.

## Hatalar ve çözümleri
- cPanel'de zip çıkarınca site `dist` alt klasöründe kalıyor → içerikleri köke taşı, `dist`'i sil. (Aynı kanalın diğer videosundaki tuzakla aynı.)
- Panel İngilizce geliyor → "Türkçeye çevirir misin?" prompt'u.
- Login ekranında gereksiz alanlar var, "bunları kaldırabiliriz, kendimiz de yapabiliriz" diyor; düzeltmiyor.
- Bizim gözlemimiz (videoda dile getirilmiyor): şifre ("Ali123") istemci tarafında sabit ve AI Studio arayüzü şifreyi ekranda bile gösteriyor ("şifreyi otomatik uygula"); gerçek kimlik doğrulama/veritabanı yok, eklenen projeler muhtemelen tarayıcıda kalıyor. Gerçek müşteri verisi için kullanılamaz.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
Videoda yok. (Stitch'in şu an ücretsiz olduğunu söylüyor; HazırWebsiteCim'i "web geliştirme ve ajans çözümlerimiz" olarak anıyor, fiyat yok.)

## Bizim fabrikaya alınacaklar (somut)
Değer düşük-orta. Yeni teknik yok ama bir akışı netleştiriyor:
1. **Stitch → AI Studio tek tık köprüsü**: Stitch'teki "Dışa aktar → AI Studio" exportu ekranları + prompt'u birlikte taşıyor; tasarımı kaybetmeden çalışan React uygulamasına geçmenin en hızlı yolu. Müşteri paneli / dahili admin prototipi için hızlı demo aracı olarak not edilebilir (satış öncesi "tıklanabilir demo").
2. **Önce marka sistemi, sonra ekran** sırası (Stitch otomatik yapıyor) — bizim akışımızda da tasarım token'ları ekranlardan önce üretilmeli; bunu doğruluyor.
3. **Ajans içi araç fikri**: "Agency OS" ekran listesi (dashboard, projeler, müşteriler, ödemeler/faturalar, görevler, ayarlar + login/register) kendi müşteri takip panelimiz için başlangıç kapsamı.
4. **Statik build deploy**: `npm run build` → `dist` içeriği → cPanel kökü. Subdomain'e demo yayınlama (demo2.alanadi) müşteri onayı için iyi bir pratik.
5. Uyarı: sabit/istemci tarafı şifre ile "yönetim paneli" teslim etmek kabul edilemez; gerçek auth (ör. Supabase Auth + RLS) teslim checklist'inde zorunlu.
