# Notivo – Google Play mağaza taslağı (Türkçe)

## Marka kararı
- **NotiBox** adı Play'de zaten var (yakın işlevli bir uygulama) → bırakıldı.
- **Notivo**: kısa, telaffuzu kolay, "noti(fication)" çağrışımlı. Web aramalarında bu adla bildirim uygulaması çıkmadı,
  ancak bu **Play'de müsaitlik garantisi değildir**: Play Console'da isim denemesi ve TÜRKPATENT/EUIPO marka sorgusu yapılmalı.
- Play başlık sınırı 30 karakter; anahtar kelime başa yakın, "en iyi/#1" gibi ifadeler ve emoji yasak.

## Başlık (≤30)
`Notivo: Bildirim Geçmişi` (24)

## Kısa açıklama (≤80)
`Silinen bildirimleri geri gör, tüm mesajlarını tek kutuda topla, ara ve ertele.`

## Uzun açıklama (taslak)
Bildirimini yanlışlıkla mı sildin? Notivo tüm uygulamalarından gelen bildirimleri tek bir yerde toplar,
böylece sonradan arayabilir, filtreleyebilir ve geri dönüp okuyabilirsin.

• Tüm bildirimler tek kutuda – uygulamaya, güne ve konuşmaya göre gruplu
• Bildirim geçmişi ve arama
• Gönderen mesajı sildiğinde bunu işaretler (yalnızca bildirimde görünen metinler)
• Kurallar: anahtar kelimeye göre engelle veya sessize al
• Ertele / hatırlat, günlük özet
• Yıldızla, okundu işaretle, uygulama içinden yanıtla (uygulama destekliyorsa)
• İstatistik: hangi uygulama seni en çok bölüyor?
• Yedekle / geri yükle, CSV dışa aktar
• Parmak izi / ekran kilidi
• %100 cihaz içi: İnternet izni yok, hesap yok, reklam yok

Anahtar kelimeler (açıklamada doğal geçsin): bildirim geçmişi, silinen mesajlar, bildirim kaydedici, bildirim yöneticisi, mesaj toplayıcı.

## Play Console'da hazırlanacaklar
- Gizlilik politikası URL'si (metin: `ui/Screens.kt` → `PRIVACY`; bir web sayfasında yayınla)
- Veri güvenliği formu: veri toplanmıyor/paylaşılmıyor (İnternet izni yok)
- Bildirim erişimi izin beyanı: ana işlev "bildirim geçmişi/yönetimi"
- Ekran görüntüleri (telefon), 512×512 ikon, 1024×500 öne çıkan görsel
- Kendi upload key'in ve `gradle bundleRelease` ile `.aab`
