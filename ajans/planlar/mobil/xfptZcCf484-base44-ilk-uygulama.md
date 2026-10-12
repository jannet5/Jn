# How to Build Your First App with Claude Code (FREE) — Mikey No Code — 20 dk — https://www.youtube.com/watch?v=xfptZcCf484

## Tek paragraf: ne yapıyor, sonuç ne
Başlığa rağmen Claude Code kullanılmıyor: video, Claude'u model olarak kullanan no-code platform **Base44**'ün (sponsorlu/affiliate) tanıtımı. Ham Claude Code'un hosting, veritabanı, auth, güvenlik yükünü kullanıcıya bıraktığını anlatıp Base44'ün bunları hazır verdiğini söylüyor. Canlı örnek: Base44 plan mode + Claude Opus 4.7 ile **kişisel görev yönetimi uygulaması** — kayıt/giriş, dashboard, görev CRUD, akıllı kategorilendirme, analitik; ardından tek prompt'la AI özellikleri (otomatik kategori, akıllı hatırlatma zamanlaması, doğal dil görev girişi, AI içgörüleri) ve tek prompt'la "production polish" (marka renkleri, logo placeholder, geçiş/animasyon, loading state, toast bildirimleri, mobil dokunma optimizasyonu). Tek tıkla publish → paylaşılabilir link. Ekran detayı/kod gösterilmiyor; değeri 3 prompt kalıbı ve "planla → kur → AI ekle → cilala → yayınla" sırası.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Base44 (no-code AI app builder; arka planda Claude) — plan mode, yerleşik auth, DB, hosting, security scanning, tek tık deploy. Affiliate: https://mikeyno-code.com/video152
- Claude Opus 4.7 (Base44 içinde seçili model)
- Ücretsiz App Store Submission Checklist: https://mikeyappchecklist.netlify.app/
- Masterclass (Skool): https://mikeyno-code.com/Skool-base44
- Karşılaştırmada anılanlar: Supabase, Firebase, Auth0

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:02:11] Ham Claude Code sorunları: dev ortamı, dosya organizasyonu, hosting, DB, auth, güvenlik sizin sorumluluğunuzda.
2. [00:03:51] Base44 = Claude etrafında hazır altyapı; ücretsiz katman kredi kartı istemiyor.
3. [00:08:00] Plan mode + Opus 4.7 seçili, mimari planlama prompt'u (#1). Claude soru soruyor, mimariyi ve özellik sırasını belirliyor.
4. [00:11:05] Planı onaylayıp build: auth native, görev DB'si, görev yönetimi, navigasyon, kullanıcı profili/tercihleri otomatik.
5. [00:12:35] Canlı test: dashboard, görev oluşturma, DB tepkisi.
6. [00:12:55] AI özellikleri prompt'u (#2).
7. [00:14:50] Production polish prompt'u (#3); Base44 yerleşik test araçlarıyla kontrol.
8. [00:16:35] Publish → canlı, paylaşılabilir link.
9. [00:17:10] Maliyet karşılaştırması: hosting $50-200/ay, DB $20-100/ay (Supabase/Firebase), auth $30-150/ay (Auth0) → toplam $100-450/ay.

## Kullandığı prompt'lar (varsa aynen)
1. Plan mode [00:08:10]:
```
I want to build a personal task management application. The app should include user registration and login, a main dashboard, task creation and management, smart categorization, progress tracking and analytics, and mobile-responsive design. Can you help me plan out the app architecture and features before we start building?
```
2. AI özellikleri [00:12:55]:
```
Integrate Claude AI for intelligent features. Add AI-powered task categorization that analyzes task descriptions and suggests appropriate categories automatically. Implement smart reminder timing where AI determines optimal notification schedules based on task priority and due dates. Create a natural language task input where users can type tasks in plain English and the AI extracts the details. Add an analytics dashboard where AI provides productivity insights and suggestions based on task completion patterns.
```
3. Production polish [00:14:59]:
```
Polish the task management app for production. Customize the branding with a professional color scheme. Add a custom logo placeholder and refine the UI design with smooth transitions and animations. Ensure the mobile experience is perfect with touch optimized controls, responsive layouts that work on all screen sizes, and mobile-friendly navigation. Add loading states for all actions, success, error notifications for user feedback, and improve the overall user experience with polished interactions. Make sure all features work seamlessly on both desktop and mobile devices.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Ayrı bir "polish" turu: profesyonel renk şeması, logo placeholder, smooth transitions/animations, touch-optimized kontroller, mobil navigasyon, her aksiyon için loading state, başarı/hata bildirimleri. Spesifik kütüphane, değer ya da referans yok; sonuç ekranları yüzeysel gösteriliyor.

## Hatalar ve çözümleri
- Videoda hata gösterilmiyor. Not: içerik büyük ölçüde reklam; "Claude Code" başlığı yanıltıcı (Base44 kullanılıyor). Mobil native uygulama değil, responsive web app.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Aşama sırası şablonu: (1) plan mode'da mimari + özellik sırası, (2) çekirdek build, (3) AI özellikleri tek prompt'ta, (4) ayrı "production polish" prompt'u, (5) yayın.
- Polish prompt'unu Expo'ya uyarlayıp standart son tur yap: loading state (skeleton), success/error toast (ham `Alert` yerine ör. sonner-native / burnt), haptics (`expo-haptics`), touch target ≥44pt, Reanimated geçişleri — "Android'in ham pop-up'ları" şikâyetine doğrudan çözüm.
- İşletme uygulamaları için AI özellik kalıpları: doğal dil girişinden alan çıkarma, otomatik kategorilendirme, öncelik/tarihe göre bildirim zamanlama, kullanım verisinden içgörü.
- Satış argümanı: hazır altyapı maliyeti ($100-450/ay karşılaştırması) — biz Expo + Supabase/Convex gibi hazır backend ile aynı "altyapı dert değil" vaadini verebiliriz.
- App Store Submission Checklist linkini (https://mikeyappchecklist.netlify.app/) yayın aşaması kontrol listemizle karşılaştır.
