# Bulut Bağlantılı Mobil Uygulama — Mimari Araştırması

Bu doküman, sıfırdan başlanacak bir mobil uygulama için mobil framework ve bulut
backend seçeneklerini karşılaştırır. Henüz spesifik bir uygulama fikrine (ne tür
bir uygulama, hangi kullanıcı kitlesi) bağlanmamıştır — o karar netleşince bu
doküman daraltılacaktır.

## 1. Mobil framework seçenekleri

| Seçenek | Artı | Eksi | Ne zaman uygun |
|---|---|---|---|
| **Native (Kotlin/Android, Swift/iOS)** | En iyi performans, platform API'lerine tam erişim (bildirim dinleme, arka plan servisleri, widget) | İki ayrı kod tabanı, daha yavaş geliştirme | Platforma özgü derin entegrasyon gerekiyorsa (bu repodaki diğer projelerde olduğu gibi: bildirim yakalama, sistem izinleri) |
| **Flutter** | Tek kod tabanı, native'e yakın performans, güçlü UI kontrolü | Platform-spesifik API'lere native köprü (platform channel) yazmak gerekebilir | Görsel olarak zengin, çapraz platform ürün |
| **React Native** | Tek kod tabanı, geniş JS ekosistemi, hızlı iterasyon | Karmaşık native entegrasyonlarda köprü maliyeti, performans native kadar değil | Ekip zaten React/JS biliyorsa, orta karmaşıklıkta uygulama |
| **Kotlin Multiplatform (KMP)** | İş mantığını paylaş, UI'ı native bırak | Daha yeni ekosistem, öğrenme eğrisi | Native UI kalitesi + paylaşılan iş mantığı isteniyorsa |

## 2. Bulut backend seçenekleri

| Seçenek | Artı | Eksi | Ne zaman uygun |
|---|---|---|---|
| **Firebase (Auth, Firestore, Cloud Functions, FCM)** | Hızlı kurulum, mobil SDK'lar hazır, push notification dahil | Vendor lock-in, karmaşık sorgularda Firestore kısıtlı | MVP / hızlı lansman, küçük-orta ölçek |
| **Supabase** | Postgres tabanlı (SQL, ilişkisel sorgular), açık kaynak, self-host edilebilir | Firebase kadar olgun değil, bazı entegrasyonlar daha az hazır | İlişkisel veri modeli + Firebase kolaylığı isteniyorsa |
| **AWS Amplify / özel AWS (API Gateway + Lambda + RDS/DynamoDB)** | Tam kontrol, ölçeklenebilirlik, kurumsal entegrasyon | Kurulum karmaşıklığı, DevOps yükü daha fazla | Kurumsal gereksinimler, mevcut AWS altyapısı varsa |
| **Özel backend (Node/Go/Kotlin + Postgres, kendi sunucunda)** | Tam esneklik, vendor lock-in yok | En fazla geliştirme + işletim yükü | Uzun vadeli, özelleşmiş iş mantığı gerektiren ürün |

## 3. Öneri (genel amaçlı, uygulama fikri netleşene kadar)

- **Hızlı doğrulama / MVP** isteniyorsa: **Flutter + Firebase** — tek kod tabanı,
  push notification ve auth hazır, en az altyapı yükü.
- **Platforma özgü derin entegrasyon** (bildirim dinleme, arka plan servisi, sistem
  izinleri — bu repodaki TekPanel/Windows-monitor projelerinde olduğu gibi) gerekiyorsa:
  **Native Android/Kotlin + hafif özel backend (REST API)**.
- **İlişkisel veri modeli + SQL sorguları** önemliyse: **Supabase** Firebase'e
  tercih edilebilir.

## 4. Açık sorular (uygulama fikri netleşince cevaplanacak)

1. Uygulama tam olarak ne yapıyor? (hangi problem, hangi kullanıcı)
2. Tek platform mu (Android) yoksa Android+iOS mu hedefleniyor?
3. Offline çalışma gerekiyor mu, yoksa her zaman bağlantılı mı?
4. Gerçek zamanlı veri senkronizasyonu (chat, canlı bildirim) gerekiyor mu?
5. Beklenen kullanıcı ölçeği ve bütçe kısıtı nedir?

## Sonraki adım

Bu doküman genel bir kıyaslamadır. Spesifik uygulama fikri belirtildiğinde,
yukarıdaki tablo o fikre göre daraltılıp somut bir teknoloji kararına (ADR)
dönüştürülecektir.
