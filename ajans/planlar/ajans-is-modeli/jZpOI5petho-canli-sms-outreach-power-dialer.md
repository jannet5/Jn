# How To Get UNLIMITED Website Clients (LIVE Outreach) — Pavlo — 10 dk — https://www.youtube.com/watch?v=jZpOI5petho

## Tek paragraf: ne anlatıyor
Pavlo (Pavel), reklamsız ve "soğuk arama yapmadan" yerel işletmelerden web sitesi müşterisi bulmak için GoHighLevel içinde kurduğu iki adımlı sistemi gösteriyor: (1) Google Maps'ten lead listesi çıkar, listeyi temizleyip yalnızca cep telefonu (mobile) olan işletmeleri bırak; (2) bu listeyi HighLevel'a aktar, günde 50-100 SMS gönder, otomatik takip mesajlarıyla olumlu cevap verenleri "power dialer" ile hemen ara, aynı gün ya da ertesi gün Zoom/Google Meet demosunda şablon siteyi göster. Altın kural: SMS'te sohbet etme, herkesi ara. Video aynı zamanda GoHighLevel affiliate linki ve ücretsiz kurs/snapshot reklamı.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Kime: yerel hizmet işletmeleri (örnekte Las Vegas'taki landscaper/peyzajcılar, roofer/çatıcılar).
- Ne: şablon tabanlı web sitesi (müşterinin logosu konmuş hazır şablon / klonlanmış site), GoHighLevel üzerinde.
- Fiyat: site fiyatı vermiyor. Kanıt olarak portföyündeki bir ajansın canlı ödeme ekranını gösteriyor:
  - "today, we made over $6,000. We had 10 successful payments" (bugün 6.000 USD+, 10 ödeme)
  - "22 successful payments yesterday"
  - "two new customers today, one new customer yesterday"
  - "on the 17th, we made $10,000 in one day, $7,000 the day prior" ve o günlerde 3 ve 6 yeni müşteri.
- Paketleme: videoda yok (abonelik mi tek seferlik mi söylenmiyor).

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
Kanal: Google Maps scrape → cep telefonu filtresi → toplu SMS + otomatik takip → power dialer ile arama → aynı/ertesi gün Zoom demo.

Lead listesi adımları:
1. Ücretsiz Chrome eklentisi (adı söylenmiyor): Google'da "landscapers / roofers" ara → Maps sekmesi → "extract business data" → tablo. Google 123 sonuçta sınırladı; haritada bölge bölge zoom yapıp tekrar çalıştır, listeye ekle → "hundreds and hundreds" kişi.
2. Ücretli araç: tryoutscraper.com (Outscraper) → Services → Google Maps scraper → bölge seç → filtreler (sitesi olan/olmayan, doğrulanmış profil, iyi/kötü puan) → "get data". Rakam: "I'll get about 4,000 businesses here, and it'll cost me around $12." (4.000 işletme ≈ 12 USD). Telefon doğrulama için 100 ücretsiz kredi.
3. Telefon tipine göre sırala (mobile / fixed / landline), mobile olmayanların hepsini sil → "over 1,000 mobile lines". Mantık: "if there's a cell phone linked, it's going to be the business owner's cell phone. This is how we sidestep the front desk." (Cep numarası = genelde patronun numarası, sekreteri atlarsın.)
4. HighLevel → Contacts → Import → dosyayı sürükle bırak.

İlk SMS (aynen, tek mesaj bu):
> "Hey man, this is Pavel, looking you guys up on Google."

Türkçe açıklama: Pitch yok, merak uyandıran tek satırlık kişisel mesaj; "en iyi test ettiğim mesaj" diyor.
Türkçe uyarlama: `Merhaba, ben [Ad], Google'da sizin işletmeye bakıyordum.` (WhatsApp Business'tan, kişisel numarayla.)

Takip: "first follow-up, second follow-up" var ama metinleri videoda okunmuyor (snapshot içinde). Örnek thread: kişi ilk mesaja cevap vermedi, "We had to follow up three times, and he said, 'Show me the website.'" (3 takipten sonra "siteyi göster" dedi.)

Arama yapılmayan durumda SMS cevabı (aynen, tek izin verilen cevap):
> "Hey, I just tried to call you."

Türkçe açıklama: SMS'te sohbet etme; aradın açmadıysa sadece bunu yaz. Uyarlama: `Merhaba, az önce sizi aradım.`

Sıcak lead'e anlık demo daveti (aynen):
> "Hey, can you jump on Zoom or on Google Meets right now?"

Türkçe açıklama: İlgi anında soğumadan aynı dakika ekran paylaşımı. Uyarlama: `Şu an 5 dakikanız varsa Google Meet'ten size hazırladığım siteyi göstereyim mi?`

Oranlar/hacim (aynen):
- "We send about 50 a day. We are going to work our way up to sending 100 a day."
- Ölçek: her HighLevel alt hesabı ücretsiz, "100 per day per sub account" → "1,000 text messages ... a day".
- "Typically, we'll get anywhere from 2 to 4% positive reply rate on these SMS messages, and I would consider that good." (%2-4 olumlu cevap = iyi.)

Demo görüşmesi akışı: müşterinin Google Business profilini göster → rakiplerini göster ("They all have websites. Look at all their reviews") → şablon siteni göster (logolu ya da logosuz; "they will still like it because it's better than nothing").

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Chrome Maps extractor eklentisi, Outscraper, GoHighLevel (Contacts, Workflows, Conversations → Manual Actions, Pipeline), Smart Cloner (rakip site klonlama).

SMS workflow (GoHighLevel):
1. Trigger: kontak içeri aktarıldı / kampanyaya eklendi
2. İlk SMS → pipeline aşaması "initial text"
3. Cevap yoksa → 1. takip → 2. takip (farklı pipeline aşamaları)
4. Olumlu cevap → bildirim: "Hey, call these people as soon as humanly possible"
5. "Not interested" → otomatik "do not disturb" kategorisi, bir daha mesaj yok

Power dialer workflow:
1. Kontakları seç (20 ya da hepsi) → "trigger automation" → power dialer workflow'una ekle → "add to automation"
2. Workflow: opportunity oluştur/güncelle (cold call kampanyası) → "power dialer" etiketi → notlara nitelendirme soruları → manual call aksiyonu
3. Conversations → Manual Actions → "Let's start" → sıradaki numarayı otomatik arar
4. Arama sonu: no answer / voicemail / redial / next
5. "cold call" etiketi → "did they book a call" evet/hayır → sonuç: interested / not interested / call back → farklı workflow ve pipeline aşamaları

Demo sitesi: sektörde şablon yoksa Smart Cloner ile rakip sitesi klonlanıp müşterinin logosu konuyor ("It's a temporary solution"); satıştan sonra site özgünleştiriliyor ("make it unique, make it different than the one that you copied").

Prompt yok.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
Videoda yok. ("No paid ads" — sistem tamamen organik outreach.)

## Hatalar / uyarılar
- "Most people do this completely wrong": lead listesini temizlemeden herkese yazmak; sabit hatlara SMS/arama boşa gider.
- SMS'te uzun sohbet etme; amaç aramak ve demo ayarlamak.
- Herkesi ara ("call every single person"); sadece SMS ile kapanış bekleme.
- Klonlanan siteyi satıştan sonra mutlaka değiştir (telif/kopya riski).
- Kanıt ekranları ve "bedava kurs" GoHighLevel affiliate satışı için; gelir rakamları kişisel portföy, tipik değil (açıklamadaki feragat).
- Türkiye notu: izinsiz toplu ticari SMS İYS (İleti Yönetim Sistemi) ve 6563 sayılı kanuna takılır; ticari elektronik ileti için onay gerekir. Bireysel, kişisel WhatsApp mesajı + telefon araması daha güvenli; KVKK açısından scrape edilen veriyle toplu gönderim yapma.
- Açıklamadaki linkler (gohighlevel affiliate, swasaccelerator, laptopceo) kurs/affiliate satışı; açılmadı. Takip SMS metinleri ve power dialer snapshot'ı videoda gösterilmiyor.

## Bizim ajansa alınacaklar (somut)
1. Lead listesi: Google Maps'ten ilçe ilçe zoom yaparak (Kadıköy, Çankaya…) restoran/klinik/kuaför listesi çıkar; Outscraper (~4.000 kayıt ≈ 12 USD) ile "sitesi yok" + "puanı 4+" filtresi; sadece 05xx cep numaralarını bırak (patrona direkt ulaşım).
2. İlk temas tek satır, pitch yok: `Merhaba, ben [Ad], Google'da [İşletme Adı]'na bakıyordum.` → cevap gelirse ARA, yazışmayı uzatma; açmazsa `Az önce sizi aradım.` Günde 50 kişiyle başla, 100'e çık; hedef %2-4 olumlu dönüş.
3. Takip: cevap yoksa 2 ve 5. gün kısa takip (ör. `Size hazırladığım bir site taslağı var, 2 dakikada göstereyim mi?`); 3 takipten sonra bırak. "İstemiyorum" diyeni otomatik kara listeye al.
4. Pipeline: Google Sheet ya da basit CRM'de aşamalar (ilk mesaj → takip 1 → takip 2 → olumlu → arandı → demo → kapandı); olumlu cevapta Telegram/WhatsApp bildirimi (n8n).
5. Demo kiti: her sektör için 1 hazır AI site şablonu (restoran, diş kliniği, kuaför); müşteri logosu ve fotoğrafıyla 10 dk'da kişiselleştir; görüşmede önce kendi Google profilini, sonra web sitesi olan rakiplerini göster, sonra şablonu.
6. "Şu an Meet'e girelim mi?" refleksi: sıcak cevaba aynı saat içinde ekran paylaşımı öner; ertesi güne bırakma.
