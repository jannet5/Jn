# SPEC — Vitrin Atölyesi Demo Uygulaması (ajansimiz-app)

**Ne:** İşletme sahibine telefonda "sizin uygulamanız böyle olur" diye gösterdiğimiz satış demosu. Backend yok, tüm durum yerel. Her konsept açıkça **"Örnek konsept"** etiketli; uydurma işletme adı/yorum/istatistik yok (mekân adları açıkça örnek: "Örnek Kafe" vb.).
**Kod:** `mobil/` (Expo SDK 57, Expo Router, TS strict, NativeWind 4, Reanimated 4, expo-haptics). Marka: `DESIGN.md` (Vitrin Atölyesi token'ları). Config: `mobil/src/site.config.ts` (marka adı, WhatsApp numarası).

## Nick Saraev'in 5 alanı
| Alan | Bu uygulamada |
|---|---|
| **Çekirdek işlev** | Satış görüşmesinde 30 saniyede "kendi uygulamam" hissini vermek: 3 sektör konseptini canlı, dokunulabilir göstermek. |
| **Çekirdek döngü** | Konsept seç → bir işlem tamamla (damga ekle / randevu onayla / sepete ekle) → **haptik + mikro animasyon + kutlama/onay** → ana ekrana dön, diğer konsepte geç. Her tamamlamada haptik (`Haptics.notificationAsync(Success)`) + scale/spring animasyonu. |
| **Yardımcı özellik** | "Bize ulaşın" ekranı: WhatsApp'tan ücretsiz check-up iste (önceden doldurulmuş mesaj, hangi konsepte baktığını içerir). Demo sıfırlama (sunum öncesi temiz başlangıç). |
| **5-7 ekran** | 5 ekran + 2 sheet: (1) Ana ekran / konsept seçimi, (2) Kafe sadakat kartı, (3) Berber randevu, (4) Restoran menü, (5) Bize ulaşın · Sheet: randevu onay, sepet. |
| **Geri getiren kanca** | Kafe: "1 damga daha → bedava kahve" ilerlemesi; Berber: randevu hatırlatma anahtarı ("1 saat önce hatırlat" — demoda yerel durum, gerçek üründe push). Satış tarafında kanca: "Bu sizin markanızla olsun mu?" CTA'sı her konseptin sonunda. |

## Gotcha moment
Kafe kartında 10. damga basılınca: güçlü haptik + damgalar dalga gibi zıplar + kart "Bedava kahve hazır" kutlamasına döner. İşletme sahibi telefonu elinde tutarken hisseder.

## Deneyim hikâyesi (4 sahne)
1. Ajans çalışanı kafe sahibine telefonu uzatır → ana ekranda 3 örnek konsept, büyük başlık "İşletmenizin uygulaması böyle olur".
2. Sahip "Kafe"ye dokunur → kahve tonlarında sadakat kartı; "Damga ekle"ye basar, her basışta titreşim ve damga animasyonu; 10'da kutlama.
3. Geri dönüp "Berber"i dener → gün/saat seçer → onay sheet'i → "Randevunuz alındı" toast'u + hatırlatma anahtarı.
4. "Restoran" → menüden ürün ekler → sepet sheet'i → "WhatsApp'tan sipariş ver" (örnek; mesaj metni gösterilir) → sonunda "Bize ulaşın" → ajansın WhatsApp'ı.

## Ekranlar ve akışlar
- **/ (Ana):** başlık + açıklama, 3 konsept satırı (farklı yükseklik, eşit kart ızgarası değil), altta "Bize ulaşın". Header: native stack başlığı.
- **/kafe:** 10 damgalık kart, "Damga ekle" (birincil), "Kartı sıfırla" (ikincil, ConfirmSheet ile). Boş durum = 0 damga ("İlk kahvenizde ilk damga"). 10'da kutlama durumu + "Hediyeyi kullan".
- **/berber:** 7 günlük gün şeridi, saat ızgarası (dolu saatler devre dışı), hizmet seçimi; "Randevu al" → onay sheet'i → onayla → toast + hatırlatma anahtarı. Hata: saat seçilmeden buton devre dışı + yerinde ipucu.
- **/restoran:** kategori sekmeleri, ürün satırları (+/- adet), alt çubukta sepet özeti; sepet sheet'i → WhatsApp sipariş metni (örnek, gerçek numara yok → toast "Demo: gerçek uygulamada işletmenin WhatsApp'ına gider"). Boş sepet durumu.
- **/iletisim:** tek birincil "WhatsApp'tan yaz" (config'deki numara; numara yoksa toast ile "numara henüz eklenmedi"), e-posta ikincil, ne yaptığımız 3 madde, "Demo'yu sıfırla".

## Veri
Yerel React state + AsyncStorage (damga sayısı, randevu, sepet). Menü/hizmet/saat listeleri `src/data/`. Tüm metinler Türkçe.

## Kapsam dışı
Gerçek push bildirimi, ödeme, giriş, backend, mağaza yayını (demo Expo Go / web ile gösterilir).
