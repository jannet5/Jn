# I Audited 50 Agencies Selling AI Agents to Local Businesses (90% Are Doing It Wrong) — Vendasta — 11 dk — https://www.youtube.com/watch?v=3fIXJyrG50c

## Tek paragraf: ne anlatıyor
Vendasta ekibinden Kendall, iki hafta boyunca 50 AI ajansının tekliflerini, sitelerini, landing page'lerini, DM'lerini ve LinkedIn'ini inceliyor (demolara kaydolup hunilerine giriyor). Hepsi aynı şeyi satıyor: cevapsız çağrıya SMS (missed call text back), AI chatbot, randevu alma, AI resepsiyonist. Bazıları 1,5K–2K–5K USD'ye kapatırken diğerleri aynı teknolojiyi aylık 97 USD'ye bile satamıyor; fark teknoloji ya da niş değil, konumlandırma. Beş hata: (1) teknolojiyle açılış, sonuçla değil; (2) fiyatla rekabet, ROI ile değil; (3) aceleci kurulum (entegrasyonların %90+'ı ilk 60 günde başarısız); (4) kanıtsız muğlak vaatler; (5) herkese hizmet. Sonunda "mükemmel AI ajans landing page"inin bölüm bölüm yapısını veriyor. Not: Vendasta kendi platformunu satıyor (kanal sponsorlu/kurumsal içerik).

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Kime: yerel işletmeler (berber, diş, HVAC, çatı, solar, med spa, kayropraktör, emlak, oto tamir, müzayede).
- Ne: "AI employees / AI workforce" — her biri isimli ve tek bir sonuca bağlı rol (sohbet, sesli resepsiyonist, içerik üreticisi vb.). Örnek başlık (aynen): `"our new AI workforce is here"`.
- Fiyat rakamları (aynen, USD):
  - Başarısızlar: missed call text back veya voice AI "$97 to $200 a month".
  - Başarılı kapanışlar: "1.5, 2, even 5K".
  - AI resepsiyonist "$3,500 a month", toptan maliyet "around $165" → "Same tools, 10x to 35x higher prices."
  - Bir yerel KOBİ HubSpot, Constant Contact ve Orca'yı iptal edip ajansa aylık 3.500 USD ödüyor (araç konsolidasyonu).
  - Diş kliniklerine özel AI resepsiyonist: "a hundred up to a thousand dollars a month".
  - Açıklamada: bir ajans bir veterinere aylık 75.000 USD gelir kazandırmış; tek bir 250K USD'lik AI anlaşması; Vendasta partneri "over $745,000 in revenue last year"; Vendasta "over 66,000 agencies".
- Paketleme: AI çalışanlar + 2–3 saatlik keşif/onboarding + sürekli ayar; bağlam ve entegrasyonlar platformda kilitli → geçiş maliyeti yüksek ("they can't leave").

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Kanallar: web sitesi/landing page, soğuk e-posta, keşif görüşmesi, Google Ads metni, LinkedIn, DM. Mesaj her yerde aynı "sonuç" çerçevesine çekilmeli: "your website headline, your cold email, the first thing out of your mouth on a discovery call, and your Google ad copy, everywhere."
- Alıntılanan müşteri içgörüsü (Quora, aynen): `"Jim the barber down your high street wants his own AI agent, but he doesn't give two shits about what tech you're using or what database. All he cares about is what the thing does and what benefit is there for him."` — esnaf teknolojiyi değil faydayı satın alır.
- Kaçınılacak muğlak mesajlar (aynen): `"Save time, be more efficient, grow your business."` ve `"AI solutions for your business. We help companies grow with AI. Automation for any industry."` — herkesle rekabet = kimseyle rekabet edememek.
- ROI kapanış cümlesi (aynen): `"the problem costs you X amount of dollars per year, our system eliminates that, implementation costs Y amount of dollars, and your ROI is Z."` — rakamlar müşterinin kendi verisinden gelince itiraz edilemez.
- Satıştan önce deneme: landing page'de telefon numarası + AI chat widget; ziyaretçi demo almadan AI resepsiyonistle konuşur. `"If your AI is good enough to sell, it should be good enough to demo itself."`
- Niş seçimi için önerilen yol: kişisel bağ (baba çatıcı, anne kuaför, üniversitede diş muayenehanesinde çalışmak, HVAC'lı arkadaş). Yoksa: roofers, HVAC, solar, med spas, chiropractors, real estate, auto repair, auctions.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
- Platform: Vendasta (AI employees). Ses/dil modeli herkes için aynı; değer "last mile": yapılandırma, alan bilgisi, sürekli ayar, teknik olmayan alıcıya paketleme.
- Hatalı kurulum örneği: telefon numarasını bağla + genel karşılama → 2 gün sonra AI yanlış fiyat söyler, takvime erişemez → sahip tek kötü aramada kapatır → müşteri kaybı.
- Doğru onboarding: partner ajansın "persona tool"u ~2,5 saat; çıkan bilgiyle tüm AI çalışanlar (chat, sesli resepsiyonist, içerik üreticileri) aynı ses tonuyla doldurulur. Genel öneri: kurulumdan önce 2–3 saat bağlam toplama.
- Onboarding kontrol listesi (açıklamadaki PDF, 6 katman — tam metin: `ajans/arastirma/youtube/kaynaklar/3fIXJyrG50c-ai-resepsiyonist-egitim-kontrol-listesi.md`):
  1. Business Identity: ad, konum, hizmet bölgesi; çalışma saatleri (tatil/sezon); marka ses tonu; farklılaştırıcılar.
  2. Services & Pricing: hizmet listesi, fiyat yapısı ("we provide custom quotes" dahil), kampanyalar, verilMEYEN hizmetler.
  3. FAQ Library — sahibine sorulacaklar (aynen): `"What are the 20 questions your receptionist answers every single day?"`, `"What questions make you cringe when customers ask?"`, `"What is the one thing people always get wrong about your business?"`, `"What complaints do you receive that could be prevented with better upfront information?"`
  4. Calendar & Booking Logic: doğrudan randevu yetkisi, Google Calendar/Outlook bağlantısı, randevu tipleri ve süreleri, kişi/oda yönlendirme, tampon ve kapalı süreler. PDF iddiası: takvim erişimiyle rezervasyon 60 saniyenin altında, "%30 more appointments".
  5. No-Show & Follow-Up: rezervasyon sonrası anında onay → 24 saat önce hatırlatma → 2–4 saat önce aynı gün hatırlatma → no-show tespiti + otomatik yeniden randevu dizisi → randevu sonrası yorum/referans isteği → her mesajda opt-out. PDF iddiası: hatırlatmalar no-show'u %30–45 azaltır.
  6. Escalation Rules: insana devir koşulları; acil anahtar kelimeler ('emergency', 'complaint', 'cancel', 'lawyer'); mesai dışı eskalasyon; veri gizliliği sınırları; cevap veremediğinde yedek mesaj; bağlamla kesintisiz devir; kızgın arayan/kapsam dışı/teknik arıza testleri.
- ROI formülü (videoda aynen, USD): manuel saat "$25 an hour times 20 hours a week times 52 weeks, that's $26,000 a year"; hata azaltma (kaçan randevu, çift rezervasyon); personel ("a new $45,000 hire"); gelir yakalama "62% of missed calls times your average customer value times your call volume". "Even a 1% improvement in conversion can mean five-figure gains."
- ROI hesaplayıcı (açıklama linki, özet: `ajans/arastirma/youtube/kaynaklar/3fIXJyrG50c-ai-calisan-roi-hesaplayici.md`): girdi lead hacmi + mevcut yanıt süresi → yıllık gelir artışı, yıllık kazanılan saat, mevcut vs AI'lı gelir.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- Google Ads metni de "sonuç" diliyle yazılmalı (genel ilke). Post sıklığı, reklam kurgusu, bütçe: Videoda yok.
- "Mükemmel landing page" sırası (aynen yapı):
  1. Açılış: teknik olmayan, müşterinin yaşadığı sıkıntıya doğrudan seslenen cesur iddia.
  2. Hemen altında CTA: işletmeyi ara ve AI çalışanla doğrudan konuş.
  3. Nasıl çalışıyoruz: 6 isimli çalışan, her birinin rolü + tek cümlelik sonucu.
  4. ROI hesaplayıcı (ziyaretçinin kendi girdileriyle).
  5. Spesifik sonuçlu vaka çalışmaları + altında gerçek müşteri yorumları.
  6. Keşif süreci: 6 katman adım adım (iş akışı detayı değil, ne kadar titiz olduğumuz).
  7. Altta iletişim formu yerine etkileşimli lead mıknatısı: "AI readiness assessment" — kişiye özel öneri + görüşme öncesi işletme bilgisi.

## Hatalar / uyarılar
1. Teknolojiyle açılmak ("AI", "otomasyon", veritabanı) — esnaf sonucu satın alır.
2. 97–200 USD/ay fiyat savaşı — herkes aynı araçları kullanıyor; ROI göstermeden fiyat konuşma.
3. Aceleci kurulum: "over 90% of AI integrations fail in the first 60 days"; tek kötü arama = müşteri kaybı.
4. Kanıtsız vaatler; vaka çalışması ve canlı deneme yoksa güven yok.
5. Herkese hizmet; niş yoksa fiyatla rekabet edersin.
6. Teknoloji yığınını göstermek: sahip "I can do this myself" diye düşünür.
7. Kaynak notu: rakamlar Vendasta'nın kendi partner örnekleri; bağımsız doğrulama yok, platform satışı amaçlı.
- Linkler: ROI hesaplayıcı açıldı (etkileşimli; giriş alanları/formüller statik metinde yok), Drive PDF indirildi ve kaydedildi. Vendasta demo linki (satış) atlandı.

## Bizim ajansa alınacaklar (somut)
1. Teklifi "isimli dijital çalışanlar" olarak paketle: örn. "Resepsiyonist Ayşe (WhatsApp/telefon 7/24 cevap + randevu)", "Yorum Takipçisi (randevu sonrası Google yorum isteği)", "İçerik Asistanı (haftalık Instagram postu)", "Hatırlatıcı (no-show azaltma)". Her birine tek cümlelik sonuç yaz; web sitemizde ve tekliflerde bu dili kullan, "yapay zekâ/otomasyon" kelimesini başlığa koyma.
2. Türkçe ROI hesap kartı (Google Sheet): sekreter saat ücreti × haftalık saat × 52; kaçan arama sayısı × ortalama hasta/müşteri değeri; no-show sayısı × randevu değeri. Kapanışta: "Bu sorun size yılda X TL'ye mal oluyor, kurulum Y TL, geri dönüş Z ay."
3. Onboarding'i satılabilir ürün yap: 6 katmanlı kontrol listesini Türkçeleştir (işletme kimliği, hizmet/fiyat, en sık 20 soru + "can sıkan sorular", randevu mantığı, hatırlatma/yorum dizisi, insana devir kuralları + KVKK sınırları). 2–3 saatlik keşif görüşmesini kurulum ücretine dahil et ve tekliflerde göster.
4. Fiyat stratejisi: aylık 97 USD karşılığı ucuz "SMS geri dönüş" yerine niş odaklı paket (örn. diş klinikleri için WhatsApp AI resepsiyonist + hatırlatma + yorum) — videodaki diş nişi bandı 100–1.000 USD/ay; Türkiye için bu aralığın yerel karşılığını ROI hesabıyla savun. Minimum kurulum ücreti + aylık.
5. Kendi landing page'imizi bu sırayla kur: sıkıntı odaklı başlık → "AI asistanımızı şimdi arayın/WhatsApp'tan yazın" canlı demo → isimli çalışanlar → ROI hesaplayıcı → vaka + yorumlar → 6 adımlı keşif süreci → "Dijital hazırlık testi" lead mıknatısı.
6. Canlı demo hattı: kendi işletmemiz için WhatsApp/telefon AI resepsiyonisti kur; soğuk mesajlarda "şu numarayı arayıp deneyin" linki ver.
