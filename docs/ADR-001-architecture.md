# ADR-001: PWA + Node/Express + SQLite olarak uygulanması

## Durum

Kabul edildi (bu dal için).

## Bağlam

`docs/RESEARCH.md` dosyasında mobil framework ve bulut backend seçenekleri
genel olarak kıyaslanmıştı, ancak spesifik bir uygulama fikri belirtilmemişti.
Bir sonraki adım olarak, uçtan uca **gerçekten çalışan ve test edilmiş** bir
"bulut mobil uygulama" ortaya koymak istendi.

Bu geliştirme ortamının (sandbox) kısıtları:
- Android SDK / Xcode / Flutter kurulu değil → native Android/iOS ya da
  Flutter uygulaması burada derlenip test edilemez.
- Node.js 22, JDK 21, Gradle, Python3 ve Playwright (Chromium) kurulu →
  bir web tabanlı uygulama uçtan uca (backend + frontend + tarayıcı testi)
  burada gerçekten çalıştırılıp doğrulanabilir.
- Firebase/AWS/Supabase gibi gerçek bulut sağlayıcılarına ait hesap/API
  anahtarı yok → gerçek bir bulut hesabına bağlanmak yerine, kendi kendine
  yeten (self-contained) bir backend gerekiyordu.

## Karar

"Cloud Notes" adında minimal ama uçtan uca eksiksiz bir not alma uygulaması:

- **Backend**: Node.js + Express + SQLite (`better-sqlite3`). E-posta/şifre
  ile kayıt-giriş (JWT, `bcryptjs` ile hash'lenmiş şifreler), kullanıcıya özel
  not CRUD (`/api/notes`). `backend/tests/api.test.js` içinde `node --test`
  ile çalışan entegrasyon testleri (auth akışı, not CRUD, kullanıcılar arası
  izolasyon).
- **Frontend**: Bağımlılıksız bir PWA (`frontend/`) — `manifest.json` ve
  `sw.js` ile telefonda ana ekrana eklenebilir, mobil-öncelikli arayüz.
  Backend'e `fetch` ile bağlanır, JWT'yi `localStorage`'da saklar.

Bu kombinasyon, gerçek bir mobil app mağazasına yayınlanabilecek native bir
uygulama değildir — ama **gerçekten çalışan, test edilmiş, buluta (kendi
sunucunuza) bağlı bir mobil deneyim** sunar ve bu sandbox'ta baştan sona
doğrulanabilen tek seçenekti.

## Doğrulama

- `cd backend && npm install && npm test` → 4/4 test geçti (health check,
  geçersiz kayıt reddi, tam auth+not CRUD akışı, kullanıcılar arası izolasyon).
- Playwright (headless Chromium, Pixel 5 viewport) ile gerçek tarayıcı testi:
  kayıt ol → not ekle → düzenle → sil → sayfa yenilendiğinde oturumun
  korunması → çıkış yap. Hepsi başarılı.

## Sonraki adımlar (bu depoyu devralacak biri için)

1. **Native'e geçiş**: Bu backend'in REST API'si değişmeden kalabilir;
   `docs/RESEARCH.md`'deki "platforma özgü derin entegrasyon" senaryosunda
   önerildiği gibi native Android/Kotlin istemcisi bu API'yi tüketebilir.
   Bunun için Android SDK kurulu bir ortam gerekir (bu sandbox'ta yok).
2. **Gerçek bulutta barındırma**: Backend şu an yerel SQLite ile çalışıyor.
   Prod'a almak için (a) bir sunucuya deploy edilmeli (örn. Fly.io, Render,
   bir VPS) ve (b) `JWT_SECRET` ortam değişkeni güvenli bir değerle
   ayarlanmalı. Bunlar bu oturumda mevcut olmayan bir hesap/kimlik bilgisi
   gerektirir.
3. **App store yayını**: PWA olarak telefona "ana ekrana ekle" ile
   kurulabilir; gerçek bir Play Store/App Store paketi isteniyorsa
   Capacitor/Cordova ile sarmalanabilir (ayrı bir adım).
