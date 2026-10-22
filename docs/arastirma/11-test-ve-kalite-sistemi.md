# Test ve kalite sistemi: "insan gibi denenmiş" nasıl olur, elimizde ne var

Araştırma kolu: test-sistemi (kendim, 22 Ekim 2026). Etiketler: [ölçtüm] bu oturumda ölçtüm,
[resmi] üreticinin sayfası, [çıkarım], [doğrulanamadı]. Ayrıntılı katman listesi: `docs/TEST_SISTEMI.md`.

## Soru

Gerçek telefonum yok, hesap açamıyorum, KVM yok. Yine de uygulamanın gerçek Android'de, gerçek bir uygulamanın
bildirimiyle çalıştığını nasıl kanıtlarım?

## Bu oturumda ölçtüklerim

- Bu sanal makinede donanım hızlandırma (KVM) yok; Android 11 (API 30, x86_64, AOSP "default") emülatörü yazılım
  emülasyonuyla açıldı. İlk açılış **631 saniye** (≈10,5 dk). [ölçtüm]
  Açıldıktan sonra SystemUI "yanıt vermiyor" (ANR) penceresi çıktı; "Wait" ile geçti. Betik bu yüzden her adımda
  bekler ve ekrandaki metni arar, sabit süreye güvenmez. [ölçtüm]
- `adb emu sms send <numara> <metin>` emülatöre gerçek bir SMS düşürür; sistemdeki **gerçek Messaging
  uygulaması** (`com.android.messaging`) kendi bildirimini kurar. Bu, elle yazılmış sahte bildirimden farklıdır
  ve bir hata buldu (S-23, `docs/SORUNLAR_COZUMLER.md`): uygulama tek bildirimin metnini "a", "a\nb", "a\nb\nc"
  diye büyütüyordu. [ölçtüm]
- `cmd notification allow_listener <paket>/<sınıf>` Android 11+ üzerinde dinleyici iznini verir. [ölçtüm]
- `uiautomator dump` Compose ekranının metinlerini ve konumlarını verir; betik düğmelere metne göre dokunur,
  sayaçları okur. [ölçtüm]

## Seçenekler (gerçek telefon yerine)

| Yol | Hesap | Sonuç |
|---|---|---|
| Yerel emülatör (bu oturum) | yok | Yapıldı, yavaş ama gerçek Android ve gerçek SMS uygulaması |
| GitHub Actions emülatör matrisi (API 26/30/34/36, KVM'li) | var (depo) | Her push'ta çalışıyor |
| Firebase Test Lab | Google hesabı | Ücretsiz planda günde 5 fiziksel + 10 sanal test [resmi](https://firebase.google.com/docs/test-lab/usage-quotas-pricing) |
| AWS Device Farm | AWS hesabı + kart | Fiyat sayfasında bir kerelik 1000 dk deneme yazıyor, sonrası dakikada 0,17 USD [resmi](https://aws.amazon.com/device-farm/pricing) |
| Kendi telefonun | yok | Tek dokunuş: APK bağlantısı; en gerçek yol, bende yapılamaz |

Fiziksel cihaz çiftliklerini (Firebase/AWS) kullanamadım: hesap ve ödeme aracı gerekiyor, bende yok. [doğrulanamadı: kullanılabilirlik]
Kurucu isterse Firebase'in ücretsiz planındaki 5 fiziksel test, Samsung/Xiaomi gibi pil kısıtlı cihazlarda
yakalamanın sessizce durup durmadığını görmek için en ucuz yol. [çıkarım]

## Karar

1. **Her sürümde** CI emülatör matrisi + R8 duman testi (var).
2. **Yeni:** `tools/sanal-telefon.sh`: gerçek Messaging uygulamasıyla uçtan uca kontrol (kanal seçimi, 4 SMS,
   sayaç, kart içeriği, geri tuşu, çökme/ANR taraması). Çıktı: ekran görüntüleri, bildirim dökümü, günlük.
3. Elle yazılmış bildirim testleri kalır (hızlı ve saf), ama "gerçek uygulama" testleri hata bulduğu için
   yakalama mantığı değişince sanal telefon betiği de çalıştırılır.
4. Gerçek WhatsApp/Instagram ve üretici pil yöneticileri sanal telefonda denenemez; bunlar kullanıcı telefonunda
   ya da fiziksel cihaz çiftliğinde denenecek. Bu sınır saklanmaz.

## Sende için çıkarımlar

- "Telefonda denenmedi" cümlesi yerine: Android 11 sanal telefonda gerçek SMS uygulamasıyla denendi; gerçek
  WhatsApp/Instagram ve üretici pil kısıtları denenmedi.
- Yeni bir bildirim biçimi bulunursa önce `FakeSbn` ile yeniden üretilir, sonra test yazılır (S-23 yolu).
