# I Cold Called 5,000 Businesses to Sell Websites. Here's What I Learned. — Sam Robinson — 16 dk — https://www.youtube.com/watch?v=rBkvcMJwYbc

## Tek paragraf: ne anlatıyor
Vermont'ta belediye planlamacısı olarak çalışan Sam, Odin Project ile HTML/CSS öğrenip, eşine ücretsiz site yapıp (ilk referanslar oradan), bir yıl boyunca her sabah 03:30-04:00'te kalkarak Google Maps'ten günde 50 işletme listeleyip öğle aralarında arabadan soğuk arama yaparak abonelik modelli (0 $ peşin, 150-300 $/ay) bir web ajansı kurdu; 30 müşteri = ~4.000 $/ay tekrarlayan gelirle işten ayrıldı. 2 yıl sonunda ayda 5-10 bin $ (5.000 $ abonelik + ek projeler/sosyal medya). Üretim: Figma ana sayfa tasarımı (dış kaynak, 300 $) + Code Stitch bileşen kütüphanesi ile sayfa builder'sız statik site (8-15 saat), PageSpeed 100. Mesaj: numara oyunu, vazgeçme, aşırı planlama yapma.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Kime: yerel küçük işletmeler (müteahhit/usta, terapist vb.), niş + şehir bazlı (örn. "contractors Lexington Kentucky").
- Ne: özel kodlu, çok hızlı statik site + Google İşletme Profili optimizasyonu; ek: GBP gönderileri, sosyal medya yönetimi, toplu projeler.
- Fiyat: **0 $ peşin, 150-300 $/ay** pakete göre (örnek 170 $/ay). 30 müşteri ≈ 4.000 $/ay MRR. Şimdi: ~5.000 $/ay MRR + ek satışlarla 5-10 bin $/ay (son 30 gün 8.900 $).
- Maliyet: tasarımcıya ana sayfa için 10-15 saat ≈ 300 $; Code Stitch aboneliği.
- Hedef filtre: çok profesyonel sitesi olanı arama (para harcamış, bırakmaz); 1980'ler görünümlü siteyi de arama (tasarıma değer vermiyor); ortadakiler + iyi Google yorumları.

## Müşteri bulma: hangi kanal, hangi mesaj
1. **Yakın çevre ile başla**: "Hey, I know how to build websites. You run a business. Can I build you a website for free?" → eşinin sitesi Instagram'da paylaşıldı → "who did your website?" → 2., 3., 4. müşteri (600-800 $).
2. **Soğuk arama rutini**: her gün Google Maps'ten niş+şehir → 50 işletme → PageSpeed Insights testi, tasarım/teknik hata denetimi, yorum sayısı/kalitesi → konuşma notları → her boş anda ara (araba, öğle arası, 17:00-18:00 başka saat dilimleri). Yılda ~3.000, toplamda 5.000+ arama. Script verilmiyor; ilke: "understanding their pain points and selling them on how I could fix them"; zamanla konuşma tonu doğallaşır.
3. **Yüz yüze networking**: referansların büyük kaynağı.
4. **20-30 müşteri sonrası**: referanslar + sosyal medya + küçük bütçeli Google/Meta reklamı ile satış hunisi; "soğuk arama acımasız, sürekli yapma".
5. Ücretsiz web scraping aracı (açıklamadaki link: uglysitescraper.com) ile eski siteli işletme bulma.

## Üretim süreci: hangi araçlar, hangi otomasyonlar
- **Onboarding**: tasarım anketi (hizmetler, marka, neden bu iş, tasarım tercihleri) VEYA 30-40 dk tanışma görüşmesi → mutlaka kayıt + AI not alıcı → transkript ChatGPT'ye "proje silosu" olarak verilir; site metni, GBP gönderileri, sosyal medya için işletmenin dili buradan.
- Gerekli girdiler: logo, marka kiti/renkler, beğendiği site örnekleri, hizmet listesi (yaptıkları/yapmadıkları).
- **Tasarım**: Figma'da ana sayfa (dış kaynak, 300 $), müşteri onayı → revizyon azalır, "özel hissettirir".
- **Geliştirme**: Code Stitch (HTML/CSS/JS bileşen kütüphanesi) ile statik site; WordPress/Wix/Squarespace yok; 8-15 saat; PageSpeed tam puan.
- Otomasyon yok; tavsiye: "7 gün tek beceriye odaklan (web geliştirme ya da yorum otomasyonu), kendi sitenizi yapın, aramaya başlayın."

## Sosyal medya / reklam
- Müşteriye ek hizmet: Google İşletme Profili gönderileri, sosyal medya yönetimi (arka planda ek gelir).
- Kendi pazarlaması: 20-30 müşteri sonrası küçük Google/Meta bütçesi; rakam yok.

## Hatalar / uyarılar
- Aşırı planlama; ilk müşteri gelmeden süreçleri kusursuzlaştırmaya çalışma.
- Ücretsiz/yakın müşteriyi "gerçek değil" diye baştan savma; 5.000 $'lık iş gibi yap, referans makinesi.
- Soğuk aramada ilk 5 müşteriyi çoğu kişi alamıyor; sebep uygulamamak.
- Çok iyi siteli ve çok kötü siteli işletmeleri aramak zaman kaybı.
- Soğuk arama sürdürülebilir değil; referans/reklam/sosyal medyaya geçiş planı gerek.

## Bizim ajansa alınacaklar (somut)
1. **Abonelik modeli teyidi**: 0 peşin + aylık 150-300 $ dengi (TR: 2.500-6.000 TL/ay bandı) ile 30 müşteri hedefi = ilk işten-ayrılma eşiği.
2. **Günlük 50 lead rutini**: Maps → PageSpeed → yorum filtresi → Sheet; biz bunu n8n + Apify ile otomatikleştirelim, aramayı insan yapsın.
3. **Onboarding kayıt kuralı**: her tanışma görüşmesi kaydedilir, transkript müşteri klasörüne; site metni + sosyal medya promptları buradan beslenir.
4. **Figma/mockup-önce**: ana sayfa tasarımı onaylanmadan kod yok; bizde Claude Code mockup bu rolü oynar.
5. **Statik + hızlı site standardı**: PageSpeed 95+ her teslimatta kontrol kapısı.
6. **İlk 3 müşteri yakın çevreden**, bedava/indirimli ama tam kalite; hepsinden Instagram paylaşımı ve yorum iste.
