# Bağlı harita: Facebook işletme reklamı kılavuzu

Araştırma tarihi: **3 Ekim 2026**. Bu harita, işin hedefinden kalıcı teslime kadar her halkayı ve
bir halka koptuğunda hangi alternatife geçileceğini gösterir. Okları takip et: her kutu bir
öncekine bağlı.

```
[H] HEDEF
 │   Bir işletme sahibinin hiç bilmeden Facebook'ta (Meta) reklamı açıp yayına alabilmesi;
 │   ekran farklı çıkarsa ne yapacağını önceden bilmesi.
 ▼
[B] BAĞIMLILIKLAR (kullanıcının elinde olması gerekenler)
 │   B1 Gerçek kişisel Facebook hesabı (+ iki faktörlü doğrulama)
 │   B2 İşletme Facebook Sayfası (yoksa açılır)
 │   B3 İşletme portfolyosu (eski adı Business Manager) — tavsiye edilir, zorunlu değil
 │   B4 Reklam hesabı (TRY + Europe/Istanbul)
 │   B5 İnternet alışverişine açık kart (CVV + 3D Secure SMS'i gelen telefon)
 │   B6 Görsel/video + metin + yönlendirme (site / WhatsApp / Messenger / telefon)
 │   B7 Vergi bilgisi (VKN) — fatura/KDV için
 ▼
[Y] YOL SEÇİMİ  ──────────────────────────────────────────────────────────────
 │   A) Sayfadan "Gönderiyi öne çıkar"   → en hızlı, en az seçenek      (KILAVUZ §7)
 │   B) Meta Reklam Yöneticisi            → ÖNERİLEN, tam kontrol        (KILAVUZ §4–§6)
 │   C) Meta Business Suite / telefon     → masaüstü yoksa yedek yol     (KILAVUZ §8)
 │
 │   Karar kuralı: Bilgisayar var + ölçmek istiyorum → B.  Bugün 5 dakikada bir gönderiyi
 │   yaymak istiyorum → A.  Sadece telefon var → C (iPhone'dan ödeme yapma, bkz. §9).
 ▼
[U] UYGULAMA ADIMLARI (Yol B; A ve C aynı hazırlığı kullanır)
 │   U1 Hesap güvenliği (2FA)            ─┐ kırılırsa → §10 "Giriş/2FA" satırları
 │   U2 Sayfa oluştur/bul                 │ kırılırsa → §3.2 dallar
 │   U3 İşletme portfolyosu               │ kırılırsa → §3.3 dallar (atla → kişisel reklam hesabı)
 │   U4 Sayfa + Instagram'ı portfolyoya   │ kırılırsa → §3.4 dallar
 │   U5 Reklam hesabı (TRY, GMT+3)        │ kırılırsa → §3.5 dallar
 │   U6 Ödeme yöntemi                     │ kırılırsa → §3.6 + §10 "Ödeme" satırları
 │   U7 Kampanya → Reklam seti → Reklam   │ kırılırsa → §5 dallar + §10 hata kodları
 │   U8 Yayınla → Değerlendirme           │ reddedilirse → §6.2 / §10
 │   U9 İlk 7 gün izleme                 ─┘ "Öğrenmeyle sınırlı" vb. → §6.3
 ▼
[T] TEST / KABUL
 │   T1 Kılavuzdaki her bağlantı canlı mı? (araclar/dogrula.py → HTTP kontrolü)
 │   T2 Meta resmi makale başlıkları kılavuzdaki iddialarla eşleşiyor mu? (kaynaklar.md)
 │   T3 Görev kabul maddeleri (akış / alternatif yol / resmi doğrulama) kılavuzda var mı?
 │   T4 Yazdırılabilir HTML/PDF gerçekten açılıyor mu? (Chromium ekran görüntüsü)
 │   T5 Gerçek hesapla reklam yayını  →  YAPILAMAZ (kimlik/ödeme gerekir). Ayrı yazıldı.
 ▼
[K] KALICI TESLİM
     K1 Bu klasör, görev dalına push edilir ve uzaktan geri okunur.
     K2 Kaynak sohbet metni İÇERMEYEN ZIP + SHA-256, bulut çalışma alanında üretilip geri okunur.
```

## A / B / C yollarının karşılaştırması

| | A) Gönderiyi öne çıkar | B) Reklam Yöneticisi | C) Business Suite / mobil |
|---|---|---|---|
| Nereden | Facebook Sayfası → Reklam merkezi → Reklam ver | adsmanager.facebook.com → **+ Oluştur** | business.facebook.com veya Meta Business Suite / Ads Manager uygulaması |
| Süre | 5–10 dk | 20–40 dk (ilk sefer) | 10–20 dk |
| Amaç seçimi | Sınırlı ("Hedef") | 6 amacın tamamı | Sınırlı–orta |
| Hedef kitle | Basit (yaş, konum, ilgi) | Tam (özel hedef kitle, hariç tutma, reklam alanı) | Basit |
| Ölçüm (Piksel, dönüşüm) | Yok/zayıf | Var | Kısmen |
| Ne zaman | Duyuru, kampanya ilanı, hızlı test | Satış, mesaj, potansiyel müşteri, ciddi bütçe | Bilgisayar yoksa |
| Risk | Bütçe boşa gidebilir (topluluk görüşü) | Seçenek kalabalığı | iPhone'dan ödemede Apple ücreti |

**Seçilen ana yol: B.** Gerekçe: Meta'nın kendi makaleleri tüm amaçları, dönüşüm konumlarını
ve bütçe kontrollerini Reklam Yöneticisi üzerinden anlatıyor; topluluk deneyimleri (Reddit,
Shopify Community, ajans blogları) "öne çıkar" butonunun kontrol eksikliği nedeniyle bütçe
harcattığında birleşiyor. A ve C, B kırıldığında veya bilgisayar olmadığında yedek yol olarak
kılavuzda tam adımlarıyla yer alıyor.

## Yol kırılırsa yeniden yönlendirme kuralları

| Kırılma | Belirti | Geçiş |
|---|---|---|
| Portfolyo açılamıyor | "Sınıra ulaştınız", e-posta onayı gelmiyor | Portfolyosuz devam: Facebook'un her kişiye verdiği varsayılan reklam hesabı ile Ads Manager (§3.3-c) |
| Reklam hesabı kapalı/kısıtlı | Kırmızı uyarı, "Reklam verme kısıtlandı" | İşletme Desteği Ana Sayfası → itiraz (§10). Yeni hesap açarak kaçmaya çalışma (politika ihlali sayılabilir). |
| Kart kabul edilmiyor | Hata 1359188, "Ödeme yöntemi eklenemedi" | Kartı internet alışverişine aç → başka banka kartı → mevcut bakiye (ön ödeme) yöntemi (§3.6) |
| Reklam reddedildi | Yayın sütununda "Reddedildi" | Düzenle-yeniden gönder veya "Değerlendirme talep et" (§6.2) |
| Ads Manager ekranı kılavuzdan farklı | Butonlar farklı yerde/isimde | §0.3 "Ekran farklıysa genel kural" + aynı adımın mobil/Business Suite yolu |
| Bilgisayar yok | — | Yol C |
