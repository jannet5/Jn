# Kelime Kökeni

Bir kelime ya da isim yazınca kökenini (hangi dilden geldiğini, anlamını, nasıl evrildiğini) internetten getiren mobil uygulama. Google'da tek tek araştırmak yerine, tek bir arama kutusu.

## Nasıl çalışıyor

```
[Mobil Uygulama]  --internet-->  [Backend]  --> Nişanyan Sözlük verisi (yaygın Türkçe kelimeler)
   (Expo/React                                \-> bulunamazsa: Claude (yapay zeka),
    Native)                                        özellikle özel isimler için (Ahmet, Ayşe, vb.)
```

- **`mobile/`** — Telefonda çalışan React Native (Expo) uygulaması. Veritabanı taşımaz, her arama internete gider.
- **`backend/`** — Aradaki sunucu. İki kaynağı birleştirir: [etimolojiturkce.com](https://api.etimolojiturkce.com) API'si üzerinden Nişanyan Sözlük verisi, bulunamazsa Anthropic'in Claude modeliyle üretilen açıklama. Aynı kelime bir hafta önbellekte tutulur, tekrar sorulduğunda yapay zekaya tekrar ödeme yapılmaz.

## Neden bu şekilde kuruldu

- **Yerel veritabanı yok:** Telefona koca bir sözlük gömmek yerine, herkesin zaten sahip olduğu internet bağlantısı üzerinden anlık sorgu yapılıyor.
- **Nişanyan Sözlük öncelikli:** Türkçe kelimeler için en güvenilir, akademik kaynak bu — ama özel isimleri kapsamıyor (örn. "Ahmet" onda yok).
- **Yapay zeka yedek olarak:** Sözlükte bulunamayan her şey (isimler, nadir kelimeler, yabancı sözcükler) için Claude'a soruluyor; bu durumda uygulama bunu açıkça "yapay zeka tahmini" rozetiyle işaretliyor, sözlük garantisiyle karıştırmıyor.

## Kurulum

1. Backend'i deploy et: [`backend/README.md`](backend/README.md)
2. Mobil uygulamayı telefonunda test et: [`mobile/README.md`](mobile/README.md)

## Kullanılan açık kaynak / veri kaynakları

- [btk/etimolojiturkce-api](https://github.com/btk/etimolojiturkce-api) — Nişanyan Sözlük verisini JSON API olarak sunan proje.
- [Anthropic Claude API](https://console.anthropic.com/) — bulunamayan kelime/isimler için ve ham veriyi okunaklı hale getirmek için.
