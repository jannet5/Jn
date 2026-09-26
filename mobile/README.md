# Kelime Kökeni - Mobil Uygulama

Bir kelime ya da isim yazıyorsun, uygulama internetten kökenini (hangi dilden geldiği, anlamı, nasıl evrildiği) getiriyor. Telefonuna hiçbir veri indirmiyor, her arama internet üzerinden backend'e gidiyor.

Bu proje **Expo (React Native)** ile yazıldı. Kod bu bulut oturumunda çalıştığı için doğrudan senin telefonuna bağlanamıyor — aşağıdaki adımları kendi bilgisayarında yapman gerekiyor. Toplam 10-15 dakika sürer, tek seferlik bir kurulum.

## 1. Ön hazırlık (bilgisayarında, tek seferlik)

1. [Node.js](https://nodejs.org) indir ve kur (LTS sürümü seç).
2. Bu repoyu bilgisayarına indir:
   ```bash
   git clone <bu-reponun-github-adresi>
   cd Jn/mobile
   npm install
   ```
3. Telefonuna **Expo Go** uygulamasını indir (Play Store'dan ücretsiz).

## 2. Backend adresini bağla

`backend/README.md`'deki adımlarla backend'i Render.com'a deploy ettikten sonra sana bir adres verecek (örn. `https://kelime-kokeni.onrender.com`).

```bash
cp .env.example .env
```

`.env` dosyasını aç, `EXPO_PUBLIC_API_URL` satırına o adresi yapıştır.

> Backend'i henüz deploy etmediysen ve sadece bilgisayarında test etmek istiyorsan: `backend` klasöründe `npm start` çalıştır, sonra `.env`'e bilgisayarının yerel ağ IP adresini yaz (örn. `http://192.168.1.34:3000` — `localhost` YAZMA, çünkü telefon ayrı bir cihaz). Bu durumda telefon ve bilgisayar aynı Wi-Fi ağında olmalı.

## 3. Uygulamayı başlat ve telefonunda aç

```bash
npx expo start
```

Terminalde bir QR kod belirecek.

- **Android:** Expo Go uygulamasını aç, "Scan QR code" ile bu kodu okut.
- Telefon ve bilgisayar farklı ağlardaysa (örn. bilgisayar ofis Wi-Fi'sinde, telefon mobil veride): `npx expo start --tunnel` kullan.

Uygulama telefonunda açılacak. Bir kelime/isim yaz, "Ara"ya bas — sonuç birkaç saniye içinde gelecek.

## Notlar

- Sonuç kartında **"Nişanyan Sözlük"** rozeti varsa bilgi doğrulanmış bir kaynaktan geliyor demektir. **"Yapay zeka tahmini"** rozeti varsa (genelde özel isimlerde, örn. "Ahmet") bilgi kesin bir sözlükten değil, yapay zekanın kendi bilgisinden geliyor — küçük bir uyarı notu da kartın altında görünür.
- Son aramaların input kutusunun altında küçük etiketler (chip) olarak görünür, onlara dokunarak tekrar arayabilirsin.
- Gerçek bir APK/uygulama mağazası uygulaması istersen (Expo Go'suz, doğrudan telefona kurulan), `eas build` ile derleme yapılabilir — istersen bunu da bir sonraki adımda kurarız.
