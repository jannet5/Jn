# Kelime Kökeni - Backend

Mobil uygulamanın bağlandığı sunucu. İki kaynağı birleştirir:

1. **Nişanyan Sözlük verisi** ([etimolojiturkce.com](https://api.etimolojiturkce.com) API'si üzerinden) — yaygın Türkçe kelimeler için.
2. **Yapay zeka (Claude)** — Nişanyan'da bulunamayan kelimeler/isimler için (örn. "Ahmet" gibi özel isimler), ayrıca bulunan ham veriyi okunaklı bir anlatıya çevirmek için.

Aynı kelime tekrar sorulduğunda 1 hafta boyunca önbellekten (cache) döner, böylece her aramada tekrar tekrar yapay zekaya para ödenmez.

## Yerelde çalıştırma

```bash
cd backend
npm install
cp .env.example .env
# .env dosyasını aç, ANTHROPIC_API_KEY=... satırına kendi key'ini yapıştır
npm start
```

Sunucu `http://localhost:3000` adresinde ayağa kalkar. Test etmek için:

```bash
curl http://localhost:3000/api/etymology/ahmet
```

> Not: `ANTHROPIC_API_KEY` tanımlamazsan, sadece Nişanyan Sözlük'te bulunan kelimeler için ham veri döner; bulunamayan kelimelerde (isimler gibi) 503 hatası alırsın. Yapay zeka key'i olmadan da bazı sonuçlar görmek için `kalem`, `kitap`, `elma`, `masa` gibi yaygın kelimeleri deneyebilirsin.

## Ücretsiz olarak internete açma (Render.com)

Telefonundaki uygulamanın bu sunucuya ulaşabilmesi için sunucunun internette bir adreste çalışması gerekiyor. Render.com'un ücretsiz katmanı bunun için yeterli.

1. Bu repoyu kendi GitHub hesabına (zaten `jannet5/jn` reposu var) push'lu tut.
2. [render.com](https://render.com)'a GitHub hesabınla giriş yap.
3. **New +** → **Web Service** seç, `jannet5/jn` reposunu bağla.
4. Ayarlar:
   - **Root Directory:** `backend`
   - **Build Command:** `npm install`
   - **Start Command:** `npm start`
   - **Instance Type:** Free
5. **Environment Variables** kısmına ekle:
   - `ANTHROPIC_API_KEY` → kendi Anthropic API key'in ([console.anthropic.com](https://console.anthropic.com/)'dan alınır)
6. **Create Web Service**'e bas, birkaç dakika içinde sana `https://senin-servisin.onrender.com` gibi bir adres verecek.
7. Bu adresi kopyala — mobil uygulamanın `.env` dosyasına (`mobile/.env` içindeki `EXPO_PUBLIC_API_URL`) yapıştıracaksın.

> Ücretsiz katmanda sunucu 15 dakika kullanılmazsa uykuya geçer, ilk istek birkaç saniye gecikebilir. Kişisel kullanım için sorun değil.

## Ortam değişkenleri

| Değişken | Zorunlu mu | Açıklama |
|---|---|---|
| `ANTHROPIC_API_KEY` | Hayır (ama önerilir) | Yoksa AI özellikleri devre dışı kalır |
| `ANTHROPIC_MODEL` | Hayır | Varsayılan: `claude-haiku-4-5-20251001` |
| `PORT` | Hayır | Varsayılan: `3000` |
