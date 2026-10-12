# HAT: MÜŞTERİ BULMA (satış)

İlke (17 YouTube planının ortak sonucu): **önce yap, sonra sat.** "Site ister misiniz?" diye sorulmaz; 15-20 dakikada işletmeye özel örnek ana sayfa üretilir, "sizin için hazırladım, bakar mısınız?" denir.
Kaynak: `planlar/ajans-is-modeli/OZET.md`, `arastirma/x/notlar/ajans-is-modeli.md`.

## Günlük rutin (1-2 saat)
1. **Liste:** "sektör + ilçe" Google Maps araması → 50 işletme → Sheet: ad, telefon, Instagram, site durumu, yorum sayısı, PageSpeed. (Otomasyon: n8n + Apify/Outscraper → sabah Telegram'a filtreli liste.)
2. **Filtre:** 20-100 iyi yorumu olan, sitesi olmayan ya da eski siteli işletme. Çok iyi siteliler atlanır.
3. **Örnek:** web hattının hızlı modu (W2 hazır sektör DESIGN.md'si + W3) → işletmenin adı, logosu, Instagram fotoğraflarıyla tek sayfa → önizleme linki (`scripts/artifact.py` ya da Cloudflare Pages).
4. **Mesaj:** aşağıdaki şablonlar. Kanal sırası: WhatsApp bire bir > Instagram DM > telefon > yüz yüze.
5. **Takip:** 24-48 saat sonra hatırlatma → doğrudan soru → "gelecek ay mı?" → 4-5 temastan sonra bırak.

## Şablonlar (TR)
- **WhatsApp, sitesi yok:** "Merhaba, [İşletme] mi? Google'da yorumlarınız çok iyi ama web sitenizi bulamadım. Ben [Ad], yerel işletmelere müşteri getiren siteler yapıyorum. Sizin için ücretsiz bir örnek ana sayfa hazırladım; göndermemi ister misiniz?" (Link cevap gelince.)
- **WhatsApp, eski site:** "Merhaba [İşletme], sitenizde kolay düzelecek birkaç şey fark ettim: telefonda geç açılıyor, WhatsApp butonu yok, menü eski. Daha fazla müşteri getiren yeni bir versiyonunu hazırladım, bakmak ister misiniz?"
- **Restoran:** "Merhaba [Restoran], menünüz Instagram'da ama telefondan sipariş ya da rezervasyon alınamıyor. Müşterinin 30 saniyede rezervasyon yaptığı bir sayfa hazırladım, göstereyim mi?"
- **Klinik/kuaför:** "Merhaba [İşletme], randevu için herkes aramak zorunda kalıyor. Gece bile randevu alan bir sayfa hazırladım; 10 dakikada göstereyim mi?"
- **Instagram (müşteri gibi açılış):** "Merhaba, profilinizi yeni gördüm, işleriniz çok güzel. [İlçe]'nin tamamında hizmet veriyor musunuz?" → cevap gelince sesli mesaj: "Bio'daki linke tıkladım, site bulamadım. Sizin için bir taslak hazırladım, 10 dakikalık görüşmede göstereyim mi?"
- **Tanıdıklara (ilk 100):** "Yerel işletmelere site + sosyal medya + reklam işi kuruyorum. Sitesi olmayan ya da büyümek isteyen bir işletme sahibi tanıyorsan beni ona iletir misin? Karşılığında ilk ayı bizden."
- **Keşif soruları:** Siteyi en son ne zaman güncellediniz? · İşler nereden geliyor? · Daha fazla iş alabilir misiniz? · Bir müşteri size ortalama kaç para kazandırır? → "Ayda 1 ekstra müşteri gelse sistem kendini öder mi?"
- **İtirazlar:** Pahalı → "Bir müşteri kaç para?" · Gerek yok → "Müşteri sizi doğrulamak için nereye bakıyor?" · Kendim yaparım → "Ne zamandır planlıyorsunuz? Haftaya hazır olsa?"

## Diğer kanallar
Form testi (işletmenin formuna mesaj at, 1 saatte dönmeyen sıcak aday) · Loom/ekran kaydı denetimi (2 dk, 3 tespit) · referans ortaklığı (fotoğrafçı, muhasebeci; %20-30) · web ajanslarına white-label otomasyon · kendi Google İşletme Profilimiz · 20-30 müşteriden sonra küçük reklam bütçesi.

## Kurallar
- İzinsiz toplu SMS/e-posta yok (KVKK/İYS). Bire bir mesaj, yüz yüze, DM.
- Sahte yorum, sıralama garantisi yok. "Garanti vermiyoruz, şunu ölçüyoruz" dili.
- Her örnek site `musteriler/_adaylar/<isletme>/` altında; kapanırsa gerçek müşteri klasörüne taşınır.
