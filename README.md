# Kelime Kökeni

Bir kelime ya da isim yazınca kökenini (hangi dilden geldiğini, anlamını, nasıl evrildiğini) internetten getiren mobil uygulama. Google'da tek tek araştırmak yerine, tek bir arama kutusu.

**Aradan geçen sunucu yok.** Telefon, kendi internet bağlantısı üzerinden doğrudan veri kaynaklarına bağlanır — deploy edilecek, ayakta tutulacak hiçbir backend yoktur.

## Nasıl çalışıyor

```
[Telefon]  --internet-->  Nişanyan Sözlük verisi (etimolojiturkce-api, yaygın Türkçe kelimeler)
  (Expo/React                \-> bulunamazsa (örn. "Ahmet" gibi özel isimler):
   Native uygulaması)              --internet--> Claude (Anthropic) - kendi API key'inle, doğrudan
```

Tek klasör: **`mobile/`** — telefonda çalışan React Native (Expo) uygulaması.
- Veritabanı taşımaz, her arama internete gider.
- Anthropic API key'ini uygulamanın kendi Ayarlar (⚙︎) ekranından girersin; key telefonun güvenli deposunda (Keystore/Keychain) saklanır, hiçbir sunucuya gönderilmez.
- Aynı kelime 1 hafta telefonda önbelleklenir, tekrar sorulduğunda yapay zekaya tekrar ödeme yapılmaz.

## Neden bu şekilde kuruldu

- **Yerel veritabanı yok:** Telefona koca bir sözlük gömmek yerine, herkesin zaten sahip olduğu internet bağlantısı üzerinden anlık sorgu yapılıyor.
- **Backend yok:** Yönetilecek, deploy edilecek, uykuya dalıp uyandırılması gereken bir sunucu yok. Telefon doğrudan Nişanyan verisine ve (gerekirse) Claude'a bağlanıyor.
- **Nişanyan Sözlük öncelikli:** Türkçe kelimeler için en güvenilir, akademik kaynak bu — ama özel isimleri kapsamıyor (örn. "Ahmet" onda yok).
- **Yapay zeka yedek olarak:** Sözlükte bulunamayan her şey (isimler, nadir kelimeler, yabancı sözcükler) için Claude'a soruluyor; uygulama bunu açıkça "yapay zeka tahmini" rozetiyle işaretliyor, sözlük garantisiyle karıştırmıyor.

## Kurulum

Tek adım: [`mobile/README.md`](mobile/README.md) — telefonuna kurup Anthropic API key'ini eklemen ~10 dakika sürer.

## Kullanılan açık kaynak / veri kaynakları

- [btk/etimolojiturkce-api](https://github.com/btk/etimolojiturkce-api) — Nişanyan Sözlük verisini JSON API olarak sunan proje.
- [Anthropic Claude API](https://console.anthropic.com/) — bulunamayan kelime/isimler için ve ham veriyi okunaklı hale getirmek için.
