# Sell AI Automation to Local Businesses (Beginner Step-by-Step Guide) — Jason Wardrop — 13 dk — https://www.youtube.com/watch?v=iUNzRKOC25w

## Tek paragraf: ne anlatıyor
Yerel işletmelere (tesisatçı, elektrikçi, masaj terapisti, kuaför, restoran, emlakçı) aylık 300-500 $ aralığında, pratikte **297 $/ay** fiyatla iki parçalı bir AI hizmeti satma modeli: (1) **Missed Call Text Back** — işletme telefonu açamadığında arayana otomatik SMS ("I saw that we just missed your call, how can I help you"); (2) **Conversation AI** — gelen SMS / Facebook Messenger / Instagram DM / web sohbetine, işletmenin web sitesinden ve SSS'den eğitilmiş bot otomatik cevap verip takvime randevu yazıyor. Satışı "Missed Call Text Back ROI hesaplayıcısı" ile yapıyor: rakamları işletme sahibinin kendisi veriyor, kaybedilen aylık para gösteriliyor, 297 $'ın yanında "%1.000+ ROI" çıkıyor. Altyapı beyaz etiketli bir yazılım (açıklamadan anlaşıldığı üzere HighLevel; video içinde adı geçmiyor, "this simple software" diyor). Video, ücretsiz kurs + HighLevel affiliate hunisine yönlendiriyor.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- **Kime:** Küçük yerel hizmet işletmeleri — tesisatçı, elektrikçi, masaj terapisti, kuaför, restoran, emlak. ABD dışından da ABD işletmelerini hedefleyebileceğini söylüyor.
- **Ne:** Missed Call Text Back + Conversation AI (çok kanallı oto-cevap + randevu).
- **Kaça:** "$300 to $500 per month per client"; örnekte **297 $/ay (USD)**. "5 müşteri × 300 $ = ayda 1.500 $ ek gelir."
- **Neden bu fiyat:** Klasik ajanslar Facebook reklamı için **1.500-2.000 $/ay** alıyor; çoğu işletme bunu karşılayamıyor, "2-3 ay sonra iptal ediyor". 300 $ "gerçekten ödenebilir" fiyat; kayıp (churn) daha az.
- **Paketleme:** Önce Missed Call Text Back satılıyor (giriş), ardından Conversation AI ile otomatik cevap + randevu gösteriliyor. Kurulum ücreti rakamı vermiyor. Beyaz etiket: kendi logon, kendi alan adın, kendi ödeme sistemin → "ödemenin %100'ü sende".
- **Destek yükü:** "not a bunch of ongoing support and fulfillment" — yarı pasif.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
**Kanal:** Google araması ("plumbers near me" + şehir) → sponsorlu ve organik sonuçlardaki işletmeler. Gerekçe: "statistics show that 62% of incoming calls to these small local business owners go unanswered" (istatistiğin kaynağını vermiyor). Reklama para verip telefonu açmayan işletme = hazır müşteri. Soğuk mesaj/arama scripti videoda yok (ücretsiz kursa yönlendiriyor).

**ROI hesaplayıcı görüşmesi (aynen):**
- "hey what does the average what's your average client value like how much do your average client pay you" — Ortalama müşteri değerini sor (örnek: 500 $).
- "how many calls do you think you miss per week" — Haftalık kaçırılan çağrı (örnek: günde 3 → haftada ~20).
- "what's your average close rate if you got on a phone call how many of those could you actually close" — Kapanış oranı (örnek: %35).
- Sonuç pitch'i: "look your monthly money left on the table over 32 $200 guess what we only charge $297 per month so you have over 1,000% Roi if you simply just work with us so we want to go through and set this up for you so we can turn your missed calls into actual new clients and real money for your business how does that sound" — Masada kalan aylık ~32.200 $'ı gösterip 297 $'lık teklifi "no-brainer" yapıyor.
- İlke: "we're not putting these numbers in their head they gave us these numbers" — Rakamları müşteri söylüyor, ikna gücü buradan.

**Kendi siten:** Hazır "Missed Call Text Back" site şablonu klonlanıyor (logo + alan adı değiştir) — şablon linki kurs içinde, açıklamada yok.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
**Araç:** Beyaz etiketli CRM yazılımı (açıklamaya göre HighLevel), Google Business Profile bağlantısı. n8n/Make/Zapier yok.

**Akış 1 — Missed Call Text Back (~2 dk kurulum):**
1. İşletme sahibi yazılımda Google Business Profile hesabına "Connect" ile giriş yapar → işletme bilgileri otomatik çekilir.
2. Settings → en alta in → "Missed Call Text Back" etkinleştir.
3. Varsayılan mesaj (aynen): "hi this is [location name] I saw that we just missed your call how can I help you" — İşletme adı otomatik doldurulur; sahibi metni özelleştirebilir.
4. "Save Missed Call Text Settings".
5. Tetik: gelen çağrı cevapsız → arayana otomatik SMS.

**Akış 2 — Conversation AI (otomatik cevap):**
1. Conversation AI → "Autopilot" aç.
2. Kanalları seç: SMS, Facebook, Instagram, Chat Widget / Live Chat → Save.
3. Bot Training → işletmenin web sitesi URL'sini yapıştır → "Get Data" → bot tüm sayfaları tarar.
4. SSS ekle (işletmeler "aynı 5-6 soruyu" alır), ör. "What services do you offer?" → cevap.
5. Bot Trial'da test et → cevabı beğenirsen başparmak yukarı, beğenmezsen aşağı + doğru cevabı yaz → "Train Bot".
6. Bot müşteriye cevap verip işletme takvimine randevu yazar.

**Tam müşteri akışı:** Cevapsız çağrı → otomatik SMS → müşteri SMS ile cevap → Conversation AI site verisiyle cevaplar → randevu takvime düşer.

Prompt metni yok (bot site taramasıyla eğitiliyor).

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
Videoda yok. (Sadece bağlam: klasik ajansların Facebook reklam yönetimi için 1.500-2.000 $/ay aldığı ve müşterinin 2-3 ayda bıraktığı söyleniyor.)

## Hatalar / uyarılar
- Yüksek aylık ücret (1.500-2.000 $) → 2-3 ayda iptal ve sürekli yeni müşteri arama döngüsü.
- ROI rakamlarını kendin uydurma; müşteriden al ("not just kind of like winging it").
- "62% cevapsız çağrı" istatistiğinin kaynağı verilmiyor; Türkiye için doğrulanmalı.
- Video yoğun şekilde affiliate/kurs hunisi: "smash like + comment" ile kurs, HighLevel affiliate, sonunda sponsor (aimessagebots.com). Gelir vaatleri ("freedom to work from home") pazarlama dili; açıklamadaki yasal not: sonuçlar tipik değil.
- Satış scripti, site şablonu ve ROI hesaplayıcı linki açıklamada yok; yalnızca kurs/affiliate (bit.ly) linkleri var — talimat gereği açılmadı, kaynak dosyası kaydedilmedi.
- Türkiye bağlamı: SMS otomasyonu için İYS/ETK izinleri ve operatör kısıtları; Türkiye'de müşteriler SMS yerine WhatsApp kullanıyor (videoda yok, bizim not).

## Bizim ajansa alınacaklar (somut)
1. **"Kaçan Çağrı → WhatsApp" giriş ürünü:** Cevapsız çağrıda otomatik WhatsApp/SMS mesajı: "Merhaba, [İşletme] olarak az önce aramanızı kaçırdık, size nasıl yardımcı olabiliriz?" — WhatsApp Business API veya HighLevel benzeri CRM ile kur. Web sitesi paketine eklenti ya da tek başına aylık ürün.
2. **Site + AI asistan aylık paketi:** Müşterinin (bizim yaptığımız) web sitesinden eğitilen sohbet botu; Instagram DM + WhatsApp + site sohbetine cevap verip randevu/rezervasyon alsın. Fiyat mantığı videodaki gibi "ödenebilir aylık": örn. 2.500-4.000 TL/ay; büyük ajans reklam retainer'ı yerine düşük, kalıcı ücret.
3. **ROI hesaplayıcı görüşmesi (TR):** "Ortalama bir müşteri size kaç TL bırakıyor? Haftada kaç arama/mesaj kaçırıyorsunuz? Telefonla konuştuğunuz 10 kişiden kaçı müşteri oluyor?" → "Ayda masada ~X TL kalıyor; biz ayda Y TL alıyoruz." Basit bir web hesaplayıcı (kendi sitemizde) yap.
4. **Kurulum standardı:** Site taraması + müşteriden en sık sorulan 5-6 soruyu (fiyat, saat, adres, rezervasyon, paket servis, otopark) al → bot SSS'i; test sohbetinde düzelt; teslimde müşteriye gösterilecek demo.
5. **Hedef liste:** Google Maps'te reklam veren (sponsorlu) ama telefona geç dönen işletmeler — gizli müşteri olarak arayıp açmayanları not et; ilk mesajda "Dün sizi aradım, ulaşamadım; aynı şey müşterilerinizin başına da geliyor olabilir" ile aç.
