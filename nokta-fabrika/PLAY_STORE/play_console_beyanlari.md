# Play Console beyanları (hazır cevaplar)

## Data safety
- Veri topluyor mu / paylaşıyor mu? **Hayır.** Uygulama internet izni istemiyor, SDK yok, reklam/analitik yok.
- Gizlilik politikası URL'si: `gizlilik_politikasi_tr.md` / `privacy_policy_en.md` herkese açık bir adreste yayınlanmalı (kullanıcı yayınlar; ben yayınlamadım).

## Foreground service: specialUse (App content)
- Tür: specialUse
- Alt tür metni (manifestteki ile aynı): "Floating overlay bubble that opens the user's list"
- İşlev: Kullanıcının açtığı yüzen balonun, diğer uygulamaların üzerinde görünür kalması. Balon kapalıyken servis çalışmaz.
- Kesinti etkisi: Servis durdurulursa balon kaybolur, kullanıcı listeye uygulama simgesinden ulaşır; veri kaybı olmaz.
- Video: balonu açma, başka uygulamada görünmesi, dokununca panelin açılması, kapatma (kaydedilecek).

## İzinler
- SYSTEM_ALERT_WINDOW: çekirdek özellik (yüzen balon). Kullanıcı sistem ayar sayfasından kendisi verir; mağaza açıklamasında belirtildi. Google'ın overlay politikasını Policy status'ta tekrar doğrula (NOTLAR'da doğrulanamadı olarak işaretli).
- FOREGROUND_SERVICE, FOREGROUND_SERVICE_SPECIAL_USE, POST_NOTIFICATIONS.

## İçerik derecelendirme
Anket: şiddet/cinsellik/kumar yok, kullanıcı etkileşimi/paylaşım yok (yalnızca metin paylaşma menüsü), konum yok, satın alma yok → muhtemelen "Herkes". Anketi sen kendin doldur ve doğru beyan et.

## Fiyatlandırma (karar senin)
Ücretli indirme için ödeme profili gerekir. Fiyat, ülkeler ve vergi ayarı sana ait kararlar; ben seçmedim.

## Yayın kontrol listesi
- [ ] Play Console geliştirici hesabı (kayıt ücreti, kimlik doğrulama) — sana ait, ben yapamam
- [ ] Yeni kişisel hesapsa: 12 test kullanıcısı + 14 gün kapalı test (NOTLAR/play_store_gereksinimleri.md; doğrulanması gereken güncel kural)
- [ ] Play App Signing: AAB'yi upload key ile imzala (harita-d keystore'u yedekle)
- [ ] Gizlilik politikasını yayınla, e-postayı doldur
- [ ] Mağaza görselleri ve FGS videosu
- [ ] targetSdk 36 (31 Ağustos 2026 şartı)
