# Cloud Notes

Bulut senkronizasyonlu, basit bir not alma uygulaması. Backend (Node.js/Express/SQLite)
ve mobilde yüklenebilir bir PWA (Progressive Web App) frontend'inden oluşur.

- Mimari araştırması ve alternatiflerin kıyaslaması: [`docs/RESEARCH.md`](docs/RESEARCH.md)
- Bu spesifik uygulanışın gerekçesi: [`docs/ADR-001-architecture.md`](docs/ADR-001-architecture.md)

## Çalıştırma

```bash
# Backend
cd backend
npm install
npm test          # 4 entegrasyon testi (auth, not CRUD, kullanıcı izolasyonu)
npm start         # http://localhost:3000

# Frontend (ayrı bir terminalde)
cd frontend
python3 -m http.server 8080   # veya herhangi bir statik dosya sunucusu
# http://localhost:8080/index.html adresini aç
```

Frontend varsayılan olarak `http://localhost:3000/api` adresine bağlanır
(`frontend/app.js` içindeki `API_BASE`). Telefonda test etmek için tarayıcı
menüsünden "Ana ekrana ekle" seçeneğini kullanabilirsin.

## Özellikler

- E-posta/şifre ile kayıt ve giriş (JWT tabanlı oturum)
- Kullanıcıya özel not oluşturma, düzenleme, silme — buluta (backend'e) senkronize
- Notlar kullanıcılar arasında izole (biri diğerinin notunu göremez/değiştiremez)
- Mobil ekrana kurulabilir PWA (manifest + service worker ile offline uygulama kabuğu)

## Durum

Uçtan uca çalışıyor ve test edilmiş: backend entegrasyon testleri (`node --test`)
ve gerçek tarayıcıda (Playwright, mobil viewport) kayıt→not ekleme→düzenleme→silme→
oturum kalıcılığı→çıkış akışı doğrulandı. Kapsam dışında kalanlar ve nedenleri için
`docs/ADR-001-architecture.md`'deki "Sonraki adımlar" bölümüne bak (native paketleme,
gerçek bulut barındırma, app store yayını — bunlar hesap/kimlik bilgisi gerektirir).
