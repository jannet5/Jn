# Marka araştırması ve 5 yön
Araştırma kolu: marka-arastirma

Tarih: 21 Ekim 2026
Kapsam: Sende (Android, Kotlin + Compose) için marka stratejisi, logo, renk,
tipografi, ikon, hareket, adaptive/temalı ikon, Play grafikleri, Facebook ve
Instagram reklam ölçüleri, Türkiye'de güven veren renk/ton, rakiplerin marka
dili ve en sonda 5 farklı marka yönü.

Önceki kollarla çakışan konu tekrar edilmedi. Rakipler ve şikâyetler için 01-03,
arayüz ve genel marka süreci için 06 numaralı dosyaya bak. Bu dosya onların
üstüne ölçüm ve doğrulama ekler.

## Kısa özet (önce bunu oku)

- Bildirim uygulamaları kategorisi görsel olarak çok tek tip. 118 uygulama
  ikonunu indirip ölçtüm.
  - 27 saf bildirim uygulamasının 20'si (%74) mavi veya turkuaz.
  - 35 bildirim ikonunun 21'i (%60) zil veya konuşma balonu çiziyor.
  - Ölçüm yöntemi ve kaynak listesi Bölüm 2'de.
- Mesajlaşma tarafı mavi, yeşil ve mor. Görece boş bölgeler: mürdüm (en boş,
  ΔE ~15), adaçayı (~12), hardal ve vermilion (ΔE 5-7, daha kalabalık).
  Sende bu bölgelerden birine yaslanmalı.
- Sende'nin mevcut 5 konseptinin ölçümü: K1 kehribarı (#F0B240) rakip
  FilterBox'ın sarısına yalnız ΔE 4,6 uzak. K4 zinoberi Akbank kırmızısına ΔE 4,5.
  Mevcut koyu zemin (#0C0D10), WhatsApp'ın koyu tuvaline ΔE 2,2 (WhatsApp rengi
  resmî değil, üçüncü taraf kaynak). Yani koyu tema WhatsApp gibi görünebilir.
- Yazı tipi tuzağı 1: 55 ailenin 53'ü ğ ş ı İ ö ü ç harflerini tam taşıyor.
  Fredoka ve Bagel Fat One taşımıyor.
- Yazı tipi tuzağı 2: Türk lirası simgesi ₺ 24 ailede yok. Sora, Outfit, Geist,
  Instrument Serif, JetBrains Mono bunların arasında. Fiyat yazan yerde boş
  kutu çıkar. Gözle de doğruladım.
- Değişken yazı tipleri yalnız Android 8+ (API 26) ve yalnız pakete gömülü
  dosyayla çalışıyor; indirilebilir yazı tipinde desteklenmiyor. Sende minSdk 26
  olduğu için uygun; yazı tipleri APK'ya gömülmeli.
- Play politikası bir tuzak daha koyuyor: simgede "yeni mesaj noktası" gibi
  yanıltıcı sembol yasak. Bildirim uygulaması için logoda rozet noktası riskli.
- Play öne çıkan görselinde saf beyaz, siyah, koyu gri kaçınılmalı. Koyu bir
  marka için görsel zemini vurgu rengi olmalı.
- İncelediğim 3 rakibin (Notisave, WAMR, NotiWeave) mağaza görselleri cihaz
  çerçeveli. Play "cihaz görseli"ni önermiyor. Çerçevesiz, ürün ekranı ve tek
  cümle fayda ile ayrışılır.
- Reklam: 9:16'da üst %14, alt %35, yanlar %6 boş bırak. 4:5 (1080x1350) ana
  ölçü olsun. Türkiye'de Android payı yaklaşık %74; reklamı Android'e hedefle.
- Dürüstlük notu: Meta'nın kendi reklam sayfaları bu ortamdan açılamadı. Reklam
  ölçüleri üçüncü taraf kaynaklara dayanıyor ve öyle işaretli.
- 5 yön: Sessiz Oda (koyu, sakin), Dükkân (sıcak), Süzgeç (teknik), Defter
  (editoryal), Afiş (cesur). Her birinin rengi, kontrastı ve rakibe uzaklığı
  ölçüldü. Tavsiyem: önce Dükkân ve Sessiz Oda'yı görselleştir (Bölüm 14).

## Okuma işaretleri

- [resmi]: platform veya şirketin kendi belgesi, doğrudan okundu.
- [ölçüm]: bu çalışmada kendim ölçtüm veya hesapladım.
- [görsel]: görseli indirip gerçekten açtım (sonra sildim).
- [blog]: bağımsız yazar, ajans, haber.
- [topluluk]: Reddit, forum.
- [tek kaynak]: tek bağımsız kaynak; dikkatli oku.
- [çıkarım]: kaynaklardan benim çıkardığım sonuç.
- [doğrulanamadı]: kanıt bulunamadı veya okunamadı.

## Nasıl araştırdım

- Araçlar
  - Web arama ve sayfa okuma araçları, curl, anonim raw.githubusercontent.com.
  - Python: fonttools (yazı tipi harf tablosu, kırpma), Pillow (ikon rengi),
    kendi yazdığım renk kütüphanesi (WCAG kontrastı, CIEDE2000 renk farkı).
  - Reddit için arşiv API'si (arctic-shift), yalnız herkese açık gönderiler.
- Ölçtüklerim
  - 55 yazı tipi ailesinin gerçek dosyası (google/fonts, 21 Ekim 2026): harf
    tablosu, lisans, boyut, eksen. Seçilenleri Türkçe cümleyle çizip baktım.
  - 118 uygulama ikonu (Play sayfalarından, 256 px). Baskın renk, simge türü.
  - 5 yönün tüm metin/zemin çiftlerinin kontrast oranı.
  - Her yön renginin rakip ikon renklerine ΔE (CIEDE2000) uzaklığı.
  - Yazı tiplerinin Latin + Latin-Ext kırpılmış boyutu.
- ΔE okuma kılavuzu: 2-10 "bir bakışta fark edilir", 11 ve üstü "daha çok
  benzer renkten çok farklı renk". Bu tablo yazarın kendi testi, resmî değil.
  - https://zschuessler.github.io/DeltaE/learn/
- İndirdiğim başkalarının görselleri (ikonlar, mağaza ekran görüntüleri) not
  alındıktan sonra silindi. Yalnız sayısal sonuçlar kaldı.
- Kod kopyalanmadı. Lisans notları ilgili yerlerde.
- Meta reklam sayfaları, bazı akademik sayfalar ve StatCounter açılmadı.
  "Denenen ve çalışmayan yollar" bölümünde ayrıntı var.

## 1. Marka stratejisi ve isimlendirme

### Ürün cümlesi ve konum (öneri) [çıkarım]

- Cümle: "Sende, müşteri mesajlarını telefondan çıkarmadan tek ekranda toplar."
- Üç ilke
  - Sende kalır: veri cihazda. Kanıtı ölçülebilir olmalı (aşağıya bak).
  - Tek ekran: sade, az düğme, çabuk.
  - Esnaf hızı: kaçan mesaj yok, "okundu" demek bir dokunuş.
- "Ne değiliz" listesi: casus uygulama, "silinen mesajı gör" aracı, ağır yapay
  zekâ asistanı, oyuncak. Kategori bugün "silinen mesaj" ve "görüldü atmadan
  oku" diyor; işletmeye yazılmış bildirim uygulaması bulunamadı.
  - Kaynak: 01 ve 03 numaralı dosyalar (Play ve topluluk taraması).

### Ad: Sende

- Anlam: Türkçede "sen" zamirinin bulunma hâli (sende = "sana ait, yanında").
  Okunuşu /sænˈde/. Slogan "Mesajların Sende." bu anlamı doğrudan kullanıyor.
  - https://en.wiktionary.org/wiki/sende
- Yazım tuzağı: "sen de" (sen de, you too) çok sık "sende" diye yanlış yazılıyor.
  Arama ve sosyal medyada gürültü yaratır.
  - https://en.wiktionary.org/wiki/sende
- Küresel okuma: Danca ve Norveççe'de "sende" = göndermek (send). Almanca'da
  "senden"in çekimi. İskandinav ve Alman pazarında ad "send" gibi okunur.
  Mesajlaşma çevresinde zararsız, ama anlam Türkçe'deki gibi taşınmaz.
  - https://en.wiktionary.org/wiki/sende
- Çakışma: Play'de aynı adlı iki uygulama var; biri aynı alanda (GlobalSix
  "Sende", WhatsApp satış CRM'i). Ayrıntı docs/MARKA_ADI_KONTROL.md'de.
  - https://play.google.com/store/apps/details?id=com.sende.sende&hl=tr&gl=TR
- İsim testi: "Smile & Scratch" ölçütlerine göre ad kısa, söylenir, anlamlı
  (smile tarafı). Zayıf yanı: yaygın bir sözcük olduğu için arama ve ayırt
  edilebilirlik (scratch tarafı).
  - https://bkconnection.com/books/title/Hello-My-Name-Is-Awesome [tek kaynak]
- Sonuç [çıkarım]: ad ortak bir sözcük olduğu için ayrıştırıcı iş görsel ve
  ses dilinde yapılmalı: tek renk, tek simge, her yerde aynı tutarlılık.
  - Bayçu ve Ustaoğlu (2015) Türk bankalarında rengin hatırlanmasını tutarlı
    kullanıma bağlıyor (Bölüm 6).
  - Laura Ries: "tek renk daha etkili, insanlar tek rengi çok daha iyi hatırlar".
    - https://www.capital.com.tr/yonetim/pazarlama/renkler-ne-diyor
- Play başlığı: "Sende: Mesajlar Tek Ekranda" (27 karakter). Başlık en çok 30
  karakter; emoji, tekrar eden özel karakter ve gereksiz BÜYÜK HARF yasak.
  - https://support.google.com/googleplay/android-developer/answer/9898842

### Premium his nereden geliyor (kaynaklı)

- Raycast: tek marka kırmızısı (#FF6363), tercih edilen koyu zemin (#151515),
  koyu ve açık için ayrı logo ve yazı işareti, "uygulama ikonunun renklerini
  değiştirme". Yani: bir renk, bir zemin, bir ikon, net kurallar.
  - https://raycast.com/press
- Cash App: tek yeşil, "her yerde ama düşünerek". Ölçtüğüm ikon rengi #00E013.
  - https://design.cash.app/color (06'da okundu)
- Monzo: mercan ağırlıklı yol; ikincil renkler erişilebilirlik için seçildi.
  - https://monzo.com/blog/weve-had-a-little-makeover (06'da okundu)
- Linear 2026: "kazanmadığın dikkat için yarışma", "yapı hissedilsin, görülmesin".
  - https://linear.app/now/behind-the-latest-design-refresh (06'da okundu)
- Duolingo: renk + maskot + özel yazı tipi; "nötr yazı yerine kendi harfi".
  - https://www.johnsonbanks.co.uk/templates/work/duolingo (06'da okundu)
- Görselde premium yardımcı uygulamalar [görsel]: Notion (beyaz kutuda siyah N
  harfi), Olauncher (tek yeşil halka), Headspace (turuncu zeminde sade yüz),
  Duolingo (maskot). Ortak nokta: tek, basit, ayırt edilir bir şekil ve tek renk.
- "Premium", ürünün davranışıyla eşleşmeli: arayüz kasıyorsa renk ve yazı
  tipi kurtarmaz. Statik ekran görüntüsü geri bildirimi hareketi görmez.
  - https://levelup.gitconnected.com/why-your-app-feels-cheap-its-not-the-colors-b5e64c7529f8
    [blog, tek kaynak; sayfa tam açılmadı, arama özeti]

### Güven: markanın asıl işi

- Bildirim erişimi korkutan bir izin. Önceki kol Reddit ve Play yorumlarında
  "tüm bildirimlerimi okuyor" korkusunu üçüncü en büyük şikâyet olarak buldu.
  - "Buzzkill. İnternet erişim izni bile yok." (en üst yanıt)
    - https://www.reddit.com/r/androidapps/comments/pokkkt/_/hcx7sof/
  - "Bildirim dinleyen hiçbir uygulamaya güvenmiyorum." (2022)
    - https://www.reddit.com/r/Android/comments/vsaagp/_/if6g38q/
- Marka sonucu: güven iddiası renkten değil kanıttan gelmeli ve ölçülebilir
  olmalı. Dikkat: "internet izni yok" yalnız sideload sürümünde doğru. Play
  sürümü Billing yüzünden INTERNET izni taşıyor (docs/SORUNLAR_COZUMLER.md,
  S-14). Play mağaza metni "internet izni yok" demesin; "mesajların cihazında
  kalır, veri toplamıyoruz" desin ve Veri güvenliği formuyla uyumlu olsun.

## 2. Rakiplerin ve komşu kategorilerin marka dili

### Ölçüm

- Örnek: 118 uygulama ikonu, Play sayfalarından 256 px indirildi, baskın renk
  ve simge sınıflandırıldı.
  - 35 bildirim yakalayıcı/yönetici (27'si saf; 8'i komşu ürün: Beeper,
    TekPanel, respond.io, Meta Business Suite, Shopify Inbox, Good Lock,
    Pushbullet, SMS Organizer).
  - 23 mesajlaşma, 24 verimlilik, 12 Türk finans/güvenlik, 9 Türk hizmet,
    15 küresel fintech/güven.
  - Bazı Türk paket kimliklerini tahmin ettim ve açılmadı (Papara, Enpara, N11
    vb.). Ölçülemediler, "Denenen yollar"da yazılı.
- Kaynak: her uygulamanın Play sayfası, örnek:
  - https://play.google.com/store/apps/details?id=com.tenqube.notisave
  - https://play.google.com/store/apps/details?id=com.drilens.wamr
  - https://play.google.com/store/apps/details?id=com.vianex.notiweave
  - https://play.google.com/store/apps/details?id=com.whatsapp
  - https://play.google.com/store/apps/details?id=com.turkcell.bip
  - Bildirim uygulamalarının paket kimlikleri 01 numaralı dosyadaki listeden.
    Mesajlaşma, verimlilik, Türk ve küresel markaların paketlerini kendim
    seçtim; liste betikte (scratch klasörü), repoya eklenmedi.

### Renk bulguları [ölçüm]

- Bildirim yöneticileri (27): mavi 16, turkuaz 4, sarı 2, turuncu 1, yeşil 1,
  kırmızı 1, siyah 1, beyaz 1. Mavi + turkuaz = 20 (%74).
- Mesajlaşma (23): mavi 10, yeşil 3, turkuaz 3, kırmızı 2, mor 1, pembe 1,
  sarı 1, siyah 1, beyaz 1. Mavi + yeşil + turkuaz = 16 (%70).
- Verimlilik (24): mavi 12, yeşil 3, turkuaz 3, kırmızı 2, diğer 4.
- Ölçülen mesajlaşma renkleri
  - WhatsApp #25D366, WhatsApp Business #00D648, Telegram #33A9E5,
    Messenger #0866FF, Signal #2833FD, BiP #0DB2FF, Viber #8B61F2,
    LINE #22BA4F, Instagram #FF006E (degrade).
- Türk markaları (ölçülen ikon rengi)
  - Kırmızı: Akbank #DC0005, Vodafone #E60000, e-Devlet #D11B22, Ziraat #B31616.
  - Mavi: İşCep #00428A, Turkcell #0D4BA0 (+ sarı #FDBD11), BiP #0DB2FF.
  - Yeşil: Garanti BBVA #009242.
  - Sarı: VakıfBank #FCBA00, sahibinden #FFE800.
  - Turuncu: Trendyol #FF6720, Hepsiburada #FD5B01, ING #FF6701.
  - Pembe: Yemeksepeti #FA0050. Mor+sarı: Getir (#5D3EBC + #FFD10D).
- Renk haritası: tüm ölçülen ikon renklerine en uzak bölgeler (ΔE en büyük)
  [ölçüm]
  - Mavi, turkuaz ve yeşil bölgeler en kalabalık (ΔE 1-4).
  - En boş: magenta-mürdüm (ΔE 9-14), sarı-zeytin (ΔE 7-16), koyu hardal ve
    kahve (ΔE 14-16), pastel gül ve şeftali (ΔE 10-13).

### Simge bulguları [görsel]

- 35 bildirim ikonundan: 14 zil (düz, degrade, dişli, kalkanlı, saatli;
  biri zil tutan yapay zekâ robotu), 7 konuşma balonu (jelatin "i" balonu
  dâhil), 4 liste çizgisi. Zil veya balon: 21 (%60).
- 23 mesajlaşma ikonundan 7'si balon (WhatsApp, WhatsApp Business, Messenger,
  Google Mesajlar, LINE, Signal, Viber). Diğerleri harf, kelime işareti,
  maskot, uçak.
- 10M+ kurulumlu Notisave degrade bir zil kullanıyor (kurulum sayısı 01'den).
- İsim kalıbı: "Notisave, Notistar, NotiWeave, NotiRecall, Notifiltr,
  Notification ...". Kategori adlandırması da birbirine benziyor.

### Mağaza görselleri [görsel]

- Notisave: lavanta-mavi zemin, ortalı 2 satır kalın başlık ("Stay undisturbed",
  "All chats in one place"), koyu cihaz çerçevesi, koyu arayüz, gerçek yüz
  fotoğrafları (örnek veri).
- NotiWeave: açık mavi zemin, büyük daire deseni, çerçeveli koyu arayüz,
  vurgulu kelime, ekranda WhatsApp, Slack, Instagram, LinkedIn, Snapchat,
  Discord logoları.
- WAMR: sarı, yeşil, mavi, kırmızı düz renk blokları, heyecanlı insan
  fotoğrafı, "90.000.000+ downloads" yaprak rozetli ilk görsel.
  - Play, grafiklerde "Million Downloads" gibi ifadeleri istemiyor
    (Bölüm 11). Rozet bu öneriye aykırı görünüyor.
- Üçünde de cihaz çerçevesi var; Play ise "cihaz görseli"ni önermiyor.
- Dil: "deleted messages", "read without view receipts". Korku ve merak.

### Neden sıradan görünüyorlar [çıkarım]

- Aynı iki renk (mavi, turkuaz), aynı iki simge (zil, balon), düz glif, düz zemin.
- Mağazada aynı kalıp: çerçeveli cihaz, iki satır başlık, koyu arayüz.
- Aynı vaat: geçmiş ve silinen mesaj. İşletme sahibine hitap yok.
- Aynı isim kalıbı: "Noti...".

### Nasıl ayrışılır [çıkarım]

- Renk: mavi, turkuaz, WhatsApp yeşili, Instagram pembesi dışında, boş bölgelerden
  birine yerleş (adaçayı, hardal, vermilion, mürdüm). Rakip ikon renginden
  ΔE ≥ 10 hedefle.
- Simge: S harfi veya özgün bir şekil. Zil, balon, kalkan, robot yok.
- Mağaza: cihaz çerçevesi yok; ürünün kendi ekranı ve tek cümle fayda.
- Dil: "silinen mesajı gör" değil "müşterin yazdı, kaçmasın". Esnaf sesi.
- Tutarlılık: tek renk, tek simge, tüm temas noktalarında aynı.

## 3. Mevcut 5 konseptin ölçümü (docs/marka)

- Dosyalar okundu: docs/marka/konsept-N-belirtecler.json. 06 numaralı dosya
  zaten kalıp (slop) riskini yazmıştı; burada sayıyla destekliyorum.
- Vurgu renginin en yakın rakip ikon rengine ΔE uzaklığı [ölçüm]
  - K1 kehribar #F0B240: FilterBox #FFAE00'e 4,6; VakıfBank'a 5,0.
  - K2 kiremit #B03F1B: Ziraat'a 7,5. İkon zemini #0E4F4A: N26'ya 5,0.
  - K3 limon #C8F23A: Forest'a 9,4; Snapchat'a 10,5.
  - K4 zinober #C4361B: Akbank'a 4,5; e-Devlet'e 5,1.
  - K5 kobalt #1E35C8: PayPal'a 5,9; Truecaller'a 6,5. Mercan #FF6B4A:
    Trendyol'a 7,5.
- Koyu zemin ve WhatsApp'ın koyu tuvali (#0B141A, sonra #0A1014'e karanlıklaşan)
  - K1 #0B0C10: 4,2 ve 2,3. K3 #0D0F11: 3,9 ve 1,8. Mevcut Brand.kt #0C0D10:
    4,2 ve 2,2.
  - https://www.oppadu.com/tools/design-systems-site/brand/whatsapp.html
    [üçüncü taraf, resmî değil]
  - WABetaInfo'nun #0b141a → #0a1014 geçişi arama özetinden [tek kaynak].
  - Sonuç: koyu temada mavi-yeşil siyah yerine sıcak veya belirgin tonlu zemin
    seç. Yeni yönlerde #17140F ve #141312 kullandım (ΔE 4-8).
- K3 yazı tipi uyarısı: JetBrains Mono ve K5'in Sora'sı ₺ simgesini taşımıyor
  (Bölüm 7). K5'in Sora'sı fiyat satırında boş kutu çıkarır.

## 4. Logo: işaret ve yazı işareti

### Kurallar (kaynaklı)

- Ürün ikonu için logo en az 48x48 dp, en çok 66x66 dp; iç 66 dp güvenli bölge.
  - https://developer.android.com/develop/ui/views/launch/icon_design_adaptive
- Katmanlar kenarlarında maske veya gölge olmasın; temiz kenar.
  - https://developer.android.com/develop/ui/views/launch/icon_design_adaptive
- Play 512 ikonu: tam kare ver, köşe yuvarlamayı (boyutun %30'u) ve gölgeyi
  Play ekler; logo keyline ızgarasına oturur, sade çizim tam kadraj olabilir;
  tercihen saydam olmayan marka zemini.
  - https://developer.android.com/distribute/google-play/resources/icon-design-specifications
- Yazı işaretinin (logotype) kontrast zorunluluğu yok ama okunabilir kalmalı:
  "logo veya marka adının parçası olan metinde kontrast gereksinimi yok".
  - https://www.w3.org/WAI/WCAG22/Understanding/contrast-minimum.html
- Rozet yasağı: simgede "yeni mesaj noktası göstergesi" gibi yanıltıcı sembol
  yasak. Bildirim uygulaması için kritik: logodaki renkli nokta, özellikle
  sağ üst köşede, rozet gibi okunabilir.
  - https://support.google.com/googleplay/android-developer/answer/9898842
- WhatsApp: "telefon logosuna karışacak kadar benzer görsel" yasak; adı ürün
  adının parçası yapmak yasak. Yeşil telefon+balon yok.
  - https://www.meta.com/brand/resources/whatsapp/whatsapp-brand/
- Google: başka bir uygulama, şirket veya ürünle ilişki varmış gibi yanıltan
  simge, başlık ve öğeler "taklit" sayılır.
  - https://support.google.com/googleplay/android-developer/answer/9888374

### Sende için işaret ve yazı işareti [çıkarım]

- İki bileşen: marka işareti (S), yazı işareti ("Sende" kelimesi). Dört yerleşim:
  sadece işaret (ikon), yatay (işaret + yazı), dikey, tek renk silüet.
- Sınama listesi (her yön için aynı)
  - 48 px ve 24 px'te okunuyor mu.
  - Tek renk (themed icon, bildirim küçük ikonu) hâlinde okunuyor mu.
  - WhatsApp, Telegram, Instagram, BiP, Notisave ikonlarının yanında
    ayırt ediliyor mu.
  - Zil, balon, kalkan, rozet noktası, telefon+balon çağrışımı var mı (olmasın).
- "S" tek başına yeterli ayırt edicilik vermez (K1-K5'in hepsi S). Ayırt edici
  olan, S'nin nasıl çizildiği ve renkle birlikte tutarlılığı.
- Yazı işareti: "Sende" tek kelime, baş harf büyük. Slogandaki nokta metinde
  kalsın, logonun parçası olmasın (rozet riski).

## 5. Renk sistemi

### Kontrast kuralları

- Metin kontrastı: en az 4,5:1, büyük metinde 3:1. Yuvarlama yok: 4,499 geçmez.
  - https://www.w3.org/WAI/WCAG22/Understanding/contrast-minimum.html
- Metin dışı (simge, kenarlık, durum): en az 3:1.
  - https://www.w3.org/WAI/WCAG22/Understanding/non-text-contrast.html
- Material tonu: kontrast oranı ton (L*) farkından hesaplanıyor.
  Kaynak koddaki formülle hesapladım [ölçüm]: beyaza karşı ton 60 = 3,15:1,
  ton 50 = 4,48:1 (≈4,5), ton 40 = 6,48:1.
  - https://raw.githubusercontent.com/material-foundation/material-color-utilities/main/typescript/contrast/contrast.ts
  - Resmî sayfadaki "40 fark = 3:1, 50 fark = 4,5:1" cümlesini doğrulayamadım;
    yukarıdaki hesap aynı sonucu veriyor.
- Radix 12 basamaklı ölçek: 9. basamak düz dolgu (en doygun), 11 ve 12 metin.
  Kendi tonal ölçeğini kurarken iyi bir şablon.
  - https://www.radix-ui.com/colors/docs/palette-composition/understanding-the-scale
    [arama özeti, tek kaynak]
- Ben her yön için 5 kural koydum: metin/zemin ≥ 7:1, ikincil ≥ 4,5:1, vurgu
  dolgu üstü metin ≥ 4,5:1, vurgu/zemin ≥ 3:1 (yoksa yalnız dolgu), tehlike ≥ 4,5:1.
  Kuralı geçemeyen çiftler yönlerin içinde açıkça yazıldı.

### Rakip ve WhatsApp'a mesafe

- WhatsApp, Telegram, Instagram yan yana duracağı için marka rengi bu
  renklere en az ΔE 14 uzak olsun (benim eşiğim, kaynaklı değil). 5 yönün
  ana renkleri en yakın mesajlaşma ikonuna 14-41 uzak (Bölüm 13'te tek tek).
- Koyu zemin: WhatsApp koyu tuvaline ΔE yaklaşık 4-8 uzak tut (düşük ışıkta
  zaten küçük sayılar çıkar); asıl fark ton (sıcak/soğuk) farkıyla gelir.

### Renk körlüğü

- Durum rengine tek başına güvenme: "okundu", "hata", "dinleme duraklamış"
  için ikon veya kelime de ekle. [çıkarım]

## 6. Türkiye'de güven veren renk ve ton

### Akademik veri

- Bayçu ve Ustaoğlu (2015), Selçuk Üniversitesi Sosyal Bilimler Enstitüsü
  Dergisi: 13 ulusal bankanın logo renk çağrışımı, 200 kişilik anket
  (Eskişehir, kolayda örnekleme).
  - Bulgu: Ziraat'ın kırmızısı "canlılık ve güven", Akbank'ın kırmızı-beyazı
    "doğrudan Türk bayrağı" çağrışımı yaratmış.
  - Bulgu: bankalar en çok mavi ve yeşil kullanıyor; bu renkler güven
    çağrıştırıyor, ama "yaşanan deneyimler güveni azaltıp yok edebilir".
  - Bulgu: İş Bankası'nın laciverti, Garanti'nin yeşili hatırlanırlıkta
    öne çıkıyor; sebep tutarlı ve her mecrada aynı kullanım.
  - Zayıflık: küçük, kolayda örneklem, 2015. Güven için tek başına kanıt değil.
  - https://dergipark.org.tr/en/download/article-file/1724733
- Küresel çapraz kültür çalışması (Madden, Hewett, Roth 2000): mavi Avusturya,
  Çin, ABD'de en sevilen renk; Brezilya'da ikinci. Güven bulgusu yok. İkincil
  alıntılar; makale doğrudan okunamadı.
  - https://ixdf.org/literature/author/thomas-j-madden [doğrulanamadı,
    yalnız yazar sayfası ve arama özeti]
- "Güvenilir mavi, güvensiz kırmızı" çalışması (ABD örneklemi) ResearchGate'te
  403 verdi; yalnız arama özeti gördüm. [doğrulanamadı]

### Basın ve sektör görüşü (eski, görüş yazısı)

- Capital 500'ün ilk 100 şirketinde mavi %37, kırmızı %24, yeşil %13 (2014).
  Uzmanlara göre mavi güven ve güç, yeşil gençlik ve doğallık, kırmızı enerji
  ve dinamizm.
  - https://www.capital.com.tr/yonetim/pazarlama/renkler-ne-diyor
- Capital (2007): Türkiye'de ilk 100 şirketin logosunda baskın renk kırmızı.
  Bir uzman bunu "ekonomik ve siyasi sürekliliğin olmadığı ortamda markaların
  endişesi" diye yorumluyor (görüş). Aynı yazı: gri ve mavi tüketiciye "daha
  mesafeli, daha ciddi, güvenilir" mesaj verir; turuncu gençlik ve teknoloji.
  - https://www.capital.com.tr/yonetim/pazarlama/logolar-neden-kirmizi
- Takım renkleri: Fenerbahçe sarı-lacivert, Galatasaray sarı-kırmızı. Bir marka
  için bu çiftlerden kaçınma kuralı veya araştırması bulunamadı. [doğrulanamadı]
  - https://www.cnnturk.com/spor/futbol/unlu-futbol-kuluplerinin-logolarindaki-sir-447056
  - Yine de dikkat [çıkarım]: sarıyı lacivert veya kırmızıyla eşleme. Dükkân
    yönünde sarı kakao ile eşleşiyor.

### Sende için ne çıkıyor [çıkarım]

- Türkiye'de güven görsel olarak "mavi + lacivert + yeşil + kırmızı" bankacılık
  dili. Bu dilin içinde kalmak güvenli ama ayrışmaz; zaten rakipler orada.
- Güveni renkten çok tutarlılık ve kanıt taşır (Bayçu çalışması + Reddit
  yorumları). Marka rengi her yerde aynı, iddialar ölçülebilir olmalı.
- "Güven" yönü için en güvenli renk: Defter yönündeki mürekkep mavisi. Ama
  kategoride en kalabalık renk de o (ΔE 3,5-5,2). Ayrışmayı tipografi ve kâğıt
  diliyle sağlıyor.
- Hitap: "sen" (Sende adı zaten "sen"). "siz" daha resmî. Markalarda sen/siz
  tercihine dair güvenilir kaynak bulunamadı. [doğrulanamadı]
  - Türkçe'de "sen" tekil ve samimi, "siz" çoğul veya saygılı hitap.
    - https://turkishclass101.com/lesson/intermediate-fun-easy-turkish-14-learn-turkish-grammar-informal-vs-formal-turkish?lp=136
  - Ton dört eksende ölçülür: resmî-samimi, ciddi-şakacı, saygılı-saygısız,
    düz-coşkulu.
    - https://www.nngroup.com/articles/tone-of-voice-dimensions/

### Pazar bağlamı (hedef kitle)

- TÜİK 2026: internet kullananların %90'ı WhatsApp, %77,6'sı YouTube,
  %71,1'i Instagram kullanıyor. Erkeklerde WhatsApp %92,5.
  - https://www.psmmag.com/turkiye-nin-en-cok-kullandigi-uygulama-whatsapp-oldu
  - https://n24.com.tr/kisa-kisa/tuik-2026-dijital-raporu-en-cok-kullanilan-3-uygulama
  - Bu iki haber TÜİK bülteninden aktarıyor; bültenin kendisi okunmadı.
- Instagram: Meta reklam araçlarına göre Türkiye'de 62,3 milyon kullanıcı
  (yetişkinlerin %92,1'i), Facebook 34,7 milyon, TikTok 44,9 milyon (18+).
  Reklam erişimi aylık aktif kullanıcıya eşit değil.
  - https://datareportal.com/reports/digital-2026-turkey
- Android payı: StatCounter Temmuz 2026'da mobilde Android %74,2, iOS %25,8
  (arama özetinden; sayfa JavaScript ile açılmadı).
  - https://gs.statcounter.com/os-market-share/mobile/turkey [tek kaynak]
- Berber, kuaför ve küçük işletmede WhatsApp Business kullanımını ölçen Türk
  araştırması bulunamadı. [doğrulanamadı] Rakamlı blog iddialarını
  (örn. "%98 açılma") kullanma; kaynakları belirsiz.

## 7. Tipografi

### Ölçüm yöntemi [ölçüm]

- 55 ailenin gerçek dosyası (google/fonts, "main", 21 Ekim 2026) indirildi.
- Kontrol edilen: ğ Ğ ş Ş ı İ ö Ö ü Ü ç Ç â î û Â Î Û, ₺ (U+20BA), tipografik
  tırnaklar, üç nokta, ok, orta nokta, birleşik üst nokta (U+0307), TRK dil
  özelliği (locl), tabular rakam (tnum), eksenler, boyut, lisans.
- Seçilen ailelerin hepsini "Mesajların Sende. İŞÇİ ığüşöç ĞÜŞ ₺1.299 kâr"
  cümlesiyle çizip gözle baktım [görsel].
- Kaynak şablonu: https://raw.githubusercontent.com/google/fonts/main/ofl/inter/METADATA.pb
  ve aynı klasörlerdeki dosyalar (inter, manrope, plusjakartasans, sora,
  dmsans, outfit, bricolagegrotesque, spacegrotesk, instrumentserif, fraunces,
  geist, ...). Hepsi OFL.

### İstenen 11 aile

- Inter: Türkçe harfler tam. ₺ var. OFL. Değişken (opsz 14-32, wght 100-900).
  856 KB; Latin+Latin-Ext kırpılınca 263 KB; opsz 14 ve wght 400-700'e
  sabitlenince 158 KB. TRK özelliği yok.
  - https://raw.githubusercontent.com/google/fonts/main/ofl/inter/METADATA.pb
- Manrope: Türkçe tam. ₺ var. OFL. wght 200-800. 161 KB (kırpılmış 83 KB).
  Birleşik nokta U+0307 yok, ama hazır İ var; gündelik kullanımda sorun yok.
  - https://raw.githubusercontent.com/google/fonts/main/ofl/manrope/METADATA.pb
- Plus Jakarta Sans: Türkçe tam. ₺ var. OFL, ayrılmış ad yok. wght 200-800.
  172 KB (kırpılmış 96 KB; wght 400-800 ile 76 KB). tnum var.
  - https://raw.githubusercontent.com/google/fonts/main/ofl/plusjakartasans/METADATA.pb
- Sora: Türkçe tam. ₺ YOK. OFL. wght 100-800. 109 KB.
  - https://raw.githubusercontent.com/google/fonts/main/ofl/sora/METADATA.pb
- DM Sans: Türkçe tam. ₺ var. OFL. opsz 9-40, wght 100-1000. 235 KB (kırpılmış
  203 KB; eksen büyük, fazla küçülmüyor).
  - https://raw.githubusercontent.com/google/fonts/main/ofl/dmsans/METADATA.pb
- Outfit: Türkçe tam. ₺ YOK. OFL. wght 100-900. 108 KB. TRK özelliği yok.
  - https://raw.githubusercontent.com/google/fonts/main/ofl/outfit/METADATA.pb
- Bricolage Grotesque: Türkçe tam. ₺ var. OFL. opsz 12-96, wdth 75-100,
  wght 200-800. 399 KB (kırpılmış 309 KB). Karakterli, ağır.
  - https://raw.githubusercontent.com/google/fonts/main/ofl/bricolagegrotesque/METADATA.pb
- Space Grotesk: Türkçe tam. ₺ var. OFL. wght 300-700. 133 KB. Yapay zekâ
  görsel dilinde sık anılan font (06'daki kataloglar).
  - https://raw.githubusercontent.com/google/fonts/main/ofl/spacegrotesk/METADATA.pb
- Instrument Serif: Türkçe tam. ₺ YOK. OFL. Tek ağırlık (Regular + Italic).
  68 KB. Yalnız büyük başlık için; gövdeye uygun değil.
  - https://raw.githubusercontent.com/google/fonts/main/ofl/instrumentserif/METADATA.pb
- Fraunces: Türkçe tam. ₺ var. OFL. opsz 9-144, wght 100-900, SOFT, WONK
  eksenleri. 352 KB (kırpılmış 237 KB).
  - https://raw.githubusercontent.com/google/fonts/main/ofl/fraunces/METADATA.pb
- Geist: Türkçe tam. ₺ YOK. OFL. wght 100-900. 165 KB. Geist Mono da ₺ yok.
  - https://raw.githubusercontent.com/google/fonts/main/ofl/geist/METADATA.pb

### Eklenenler (öneride kullandıklarım ve tuzaklar)

- Türkçe harfi EKSİK: Fredoka (ğ Ğ ş Ş İ yok), Bagel Fat One (aynı). Google
  Fonts'ta "latin-ext" etiketi olsa da dosyayı denetle.
  - https://raw.githubusercontent.com/google/fonts/main/ofl/fredoka/METADATA.pb
  - https://raw.githubusercontent.com/google/fonts/main/ofl/bagelfatone/METADATA.pb
- ₺ olmayan diğerleri: Figtree, Funnel Display/Sans, Gabarito, Golos Text,
  Google Sans Flex (3,8 MB), Hanken Grotesk, Instrument Sans, JetBrains Mono,
  Playfair Display, Radio Canada Big, Red Hat Display, Rethink Sans, Syne,
  Urbanist, Young Serif, DM Serif Display. Gözle de doğruladım: Sora, Outfit,
  Geist, Instrument Serif, JetBrains Mono, Hanken ₺ yerine boş kutu çizdi.
- ₺ ve Türkçe tam, kullanıma uygun: Newsreader, Nunito, Parkinsans, Mona Sans,
  Hubot Sans, IBM Plex Sans/Mono, Public Sans, Unbounded, Epilogue, Onest,
  Albert Sans, Host Grotesk, Familjen Grotesk, Lexend, Work Sans, Quicksand.
  - https://raw.githubusercontent.com/google/fonts/main/ofl/newsreader/METADATA.pb
  - https://raw.githubusercontent.com/google/fonts/main/ofl/parkinsans/METADATA.pb
  - https://raw.githubusercontent.com/google/fonts/main/ofl/unbounded/METADATA.pb
- Not: Google Fonts "main" dalı zamanla güncellenir; bir fontta ₺ sonradan
  eklenmiş olabilir. Seçimden önce dosya sürümünü yeniden denetle.

### Lisans: OFL ve gömme

- Mobil uygulamaya gömmek serbest (orijinal veya değiştirilmiş sürüm).
  - https://openfontlicense.org/ofl-faq/
- Dosyayı kırpmak veya eksen sabitlemek "değişiklik". "Reserved Font Name" (RFN)
  taşıyan ailelerde değiştirilmiş sürüm aynı adı kullanamaz.
  - RFN var: Mona Sans ("Mona"), Hubot Sans ("Hubot"), IBM Plex ("Plex").
    - https://raw.githubusercontent.com/google/fonts/main/ofl/monasans/OFL.txt
    - https://raw.githubusercontent.com/google/fonts/main/ofl/ibmplexmono/OFL.txt
  - RFN yok: Plus Jakarta Sans, Nunito, Parkinsans, Newsreader, Public Sans,
    Unbounded, Epilogue, Inter, Manrope, DM Sans, Onest.
    - https://raw.githubusercontent.com/google/fonts/main/ofl/plusjakartasans/OFL.txt
  - Çözüm: RFN'li fontu kırpma veya kırpacaksan yeniden adlandır [çıkarım].
- Lisans metni uygulamayla birlikte (açık kaynak lisanslar ekranı) verilsin.

### Android uygulaması [resmi]

- Değişken yazı tipleri yalnız Android 8+ (API 26); indirilebilir yazı tipinde
  değişken font desteklenmiyor; Compose'ta FontVariation ile eksen verilir.
  - https://developer.android.com/develop/ui/compose/text/fonts
- Sende'nin ağı yok ve sideload sürümü Google Play hizmetlerine bağlı olmamalı:
  indirilebilir yazı tipi sağlayıcısı com.google.android.gms. Dosyayı
  res/font'a göm. [çıkarım]
- Boyut: iki aile kırpılıp eksen sınırlanırsa 160-260 KB. Mevcut release APK
  2,55 MB (docs/RELEASE.md), yani yaklaşık %6-10 artış.
  - Newsreader 441 → 114 KB (opsz 16, wght 400-700), Mona Sans 340 → 116 KB
    (wdth 100, wght 400-800), Unbounded 760 → 148 KB (wght 600-900),
    Inter 856 → 158 KB, Plus Jakarta 172 → 76 KB, Nunito 270 → 95 KB.
  - Arapça (values-ar) için bu Latin fontlar harf taşımaz; sistem yedeği
    devreye girer. Marka tutarlılığı istenirse IBM Plex Sans Arabic veya Noto
    Sans Arabic (OFL) ayrıca eklenir. [çıkarım, boyutu ölçülmedi]
- Büyük harf tuzağı: Kotlin String.uppercase() "invariant locale" kullanır.
  Türkçe'de "işlendi" → "IŞLENDI" çıkar, doğrusu "İŞLENDİ". Mono etiket ve
  BÜYÜK HARF stilinde uppercase(Locale("tr")) veya LocaleList kullan.
  - https://kotlinlang.org/api/core/kotlin-stdlib/kotlin.text/uppercase.html
  - Türkçe'de noktalı ve noktasız I ayrı harf.
    - https://en.wikipedia.org/wiki/Dotted_and_dotless_I

### Eşleştirme ilkeleri [çıkarım]

- İki rol yeter: başlık ve gövde (+ isteğe bağlı mono etiket). Üçüncü aile yok.
- Başlık karakterli, gövde tarafsız ve okunaklı; tabular rakam (tnum) olsun
  (saat ve sayaç satırları).
- Koyu zeminde gövde ağırlığını 400'den 450-500'e çek.
- Mesaj metni serif olacaksa yalnız ekran için tasarlanmış (Newsreader'ın opsz
  ekseni) bir aile seç.

## 8. İkon dili

- Material Symbols: Apache-2.0; Outlined, Rounded, Sharp aileleri; ağırlık,
  dolgu (FILL), derece (GRAD), optik boyut (opsz 20-48) eksenleri. Üçüncü taraf
  logoları yasal nedenle içermez.
  - https://raw.githubusercontent.com/google/material-design-icons/master/README.md
  - https://raw.githubusercontent.com/google/material-design-icons/master/LICENSE
- Phosphor: MIT; ağırlık gruplarına ayrılmış SVG (duotone dâhil).
  - https://raw.githubusercontent.com/phosphor-icons/core/main/LICENSE
  - https://raw.githubusercontent.com/phosphor-icons/core/main/README.md
- Lucide: ISC.
  - https://raw.githubusercontent.com/lucide-icons/lucide/main/LICENSE
- Tabler: MIT.
  - https://raw.githubusercontent.com/tabler/tabler-icons/main/LICENSE
- Iconoir: MIT. Heroicons: MIT.
  - https://raw.githubusercontent.com/iconoir-icons/iconoir/main/LICENSE
  - https://raw.githubusercontent.com/tailwindlabs/heroicons/master/LICENSE
- Remix Icon: Ocak 2026'da yeni özel lisans (v1.0). Ticari kullanıma izin var,
  ama simgeleri tek başına paket olarak satmak yasak. Standart Apache/MIT değil;
  hukuki gözden geçirme olmadan seçme.
  - https://raw.githubusercontent.com/Remix-Design/RemixIcon/master/License
- Kurallar [çıkarım]
  - Tek aile, tek ağırlık, tek boyut ızgarası (24 dp). Karıştırma.
  - Ağırlığı gövde yazı ağırlığıyla eşle (ince metin = ince çizgi).
  - Seçili durum: dolgu veya vurgu rengi, ama yalnız renk değil.
  - Üçüncü taraf uygulama ikonları telefondan gelir; kendi ikon setine WhatsApp,
    Instagram logosu çizme.
  - Compose'a 25-30 simgeyi Android vektör XML olarak aktar; ikon fontunu
    gömme (Material Symbols değişken fontu MB'lar boyutunda).
    [çıkarım, boyut doğrulanmadı]

## 9. Hareket kimliği

- Material 3'ün iki hareket şeması var. Yay (spring) jetonları [kaynak kod]:
  - Standard: varsayılan uzamsal 0,9 sönüm / 700 sertlik; hızlı 0,9 / 1400;
    yavaş 0,9 / 300. Efekt: 1,0 sönüm, 1600 / 3800 / 800 sertlik.
    - https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/StandardMotionTokens.kt
  - Expressive: varsayılan uzamsal 0,8 / 380; hızlı 0,6 / 800; yavaş 0,8 / 200.
    - https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/ExpressiveMotionTokens.kt
  - Süre jetonları: kısa 50-200 ms, orta 250-400 ms, uzun 450-600 ms,
    çok uzun 700-1000 ms.
    - https://raw.githubusercontent.com/androidx/androidx/androidx-main/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/tokens/MotionTokens.kt
- Açılış (splash) animasyonu: AnimatedVectorDrawable, telefonda en çok 1000 ms
  önerilir, gecikmeli başlangıç en çok 166 ms; açılış zemini tek düz renk.
  - https://developer.android.com/develop/ui/views/launch/splash-screen
- "Hareket kimliği" = uygulamanın tek karakteri, birkaç jetonla tanımlanır
  [çıkarım]: sönüm oranı (zıplama), sertlik (hız), süre bandı, giriş biçimi
  (kayma, solma, kesme), bir "imza anı" (yeni mesaj geldiğinde ne olur),
  haptik. 5 yönün her biri bu 5 parametreyle farklılaşıyor (Bölüm 13).
- Hareketi azalt ayarında: yalnız çapraz solma; imza anı kalır ama hareketsiz.
  Ayrıntı ve haptik için 06, Bölüm 6.
- Reklam ve mağaza için aynı karakter: 6-10 saniyelik ekran kaydında imza anı
  ilk 2 saniyede görünsün (Bölüm 12).

## 10. Uygulama ikonu: adaptive, temalı, bildirim, açılış

- Adaptive icon: tüm katmanlar 108x108 dp; dış 18 dp maske ve paralaks için
  ayrılmış; 66x66 dp merkez hiçbir maskede kırpılmaz; logo 48-66 dp.
  Ön plan ve arka plan zorunlu; vektör tercih.
  - https://developer.android.com/develop/ui/views/launch/icon_design_adaptive
- Temalı (monochrome) ikon: Android 13+'ta kullanıcı açarsa, tek katmanlı
  <monochrome> dosyası duvar kâğıdı rengiyle boyanır. Android 16 QPR2'den
  itibaren sistem, kendi katmanı olmayan uygulamalar için otomatik tema yapar.
  - https://developer.android.com/develop/ui/views/launch/icon_design_adaptive
  - Otomatik sonucun tasarlanandan farklı görünebileceği uyarısı bir blogdan
    (arama özeti); yine de kendi katmanını ver [çıkarım].
  - Kural [blog, tek kaynak]: tek renk, yalnız alfa kanalı şekli belirler.
    - https://medium.com/@enikebraimoh/android-themed-icons-a-comprehensive-guide-3abb33ab51a7
      [arama özeti]
  - Sende kuralı [çıkarım]: monochrome katman S'nin düz silüeti; degrade,
    ince detay, iç boşluklu gölge yok. Logoda ince çizgi veya yarık varsa
    (Süzgeç yönü) monochrome sürümde kalın tut.
- Bildirim küçük ikonu: durum çubuğunda tek renkli (monochrome) görünür.
  Android 12'den itibaren renk, uygulamanın bildirim renginden (setColor)
  türetilir; çoğu stilde bu renk yalnız ön plan servisi bildirimine uygulanır.
  Büyük ikon yalnız kişi fotoğrafı içindir, uygulama ikonu için kullanma.
  - https://developer.android.com/design/ui/mobile/guides/home-screen/notifications
  - https://developer.android.com/develop/ui/compose/notifications
  - 24x24 dp ve beyaz-şeffaf kuralı: eski resmî tasarım belgesinin yansısı
    [eski kaynak, tek kaynak].
    - https://stuff.mit.edu/afs/sipb/project/android/docs/design/style/iconography.html
  - Sende kuralı [çıkarım]: küçük ikon S'nin sadeleştirilmiş tek renk
    silüeti; rozet noktası yok.
- Açılış ekranı (Android 12+): ikon arka plansız 288x288 dp ve 192 dp daire
  içine sığmalı; arka planlıysa 240x240 dp ve 160 dp daire. Dairenin dışı
  maskelenir. AVD en çok 1000 ms.
  - https://developer.android.com/develop/ui/views/launch/splash-screen
- Play listesi simgesi: Bölüm 11. Adaptive ikon ile aynı S ve aynı renk olsun;
  Play listesi temalı ikonu göstermez, ana ekran gösterir. [çıkarım]
- Test listesi [çıkarım]
  - Açık, koyu ve duvar kâğıdı renkli temalı ana ekranda.
  - Daire, kare, squircle, damla maskelerinde.
  - 48 px ve 32 px'te.
  - Yan yana: WhatsApp, WhatsApp Business, Telegram, Instagram, BiP, Notisave.

## 11. Google Play grafikleri

Tümü [resmi]: https://support.google.com/googleplay/android-developer/answer/9866151
(sayfayı doğrudan indirip okudum).

- Okuma notu: sayfada "Requirements" (zorunlu; uymazsan kaldırma veya askıya
  alma riski) ve "Highly recommended" (öneri; uymazsan sayfan etkilenmez ama
  öne çıkma ve görsellerin kırpılması etkilenir) ayrı. Aşağıda ayırt ettim.

- Uygulama simgesi: 512x512 px, 32 bit PNG (alfalı), en çok 1024 KB, sRGB;
  tam kare; Play köşe yuvarlaklığını (%30) ve gölgeyi ekler. "Sıralama, fiyat,
  Play kategorisi önerisi veren rozet veya yazı yok."
  - https://developer.android.com/distribute/google-play/resources/icon-design-specifications
- Öne çıkan görsel (feature graphic): 1024x500, JPEG veya 24 bit PNG (alfasız).
  - Simgeyle benzer belirgin marka öğesi kullanma (kopya olur); simgenin
    uzantısı olsun.
  - Odak ve ana öğeler merkezde; kesilme bölgelerine logo, ad, slogan, ana
    arayüz koyma. Arka plan öğeleri kenarlarda.
  - "Saf beyaz, siyah veya koyu gri kullanma; Play'in zeminine karışır." Canlı
    renk öner.
  - Cihaz görseli, üçüncü taraf logosu (izinsiz), mağaza rozeti yok.
  - Alt metin en çok 140 karakter.
- Ekran görüntüleri: yayın için toplam en az 2; cihaz türü başına en çok 8.
  JPEG veya 24 bit PNG (alfasız); kenar 320-3840 px; uzun kenar kısanın en çok
  2 katı.
  - Öneri ve ana sayfa biçimleri için uygulamada en az 4 ekran, en az 1080 px,
    9:16 dikey (en az 1080x1920).
  - Gerçek uygulama deneyimini göster; ilk üç ekranda arayüz öncelikli.
  - Slogan yalnız gerekliyse ve görselin en çok %20'si.
  - İstenmeyen (öneri): "Best, #1, Top, New, Free, Discount, Sale, Million
    Downloads", "Download now" gibi eylem çağrısı, kullanıcı yorumu, fiyat
    veya kampanya. Simge için aynı kural zorunlu.
  - Parmak yok; "cihaz görseli" (device imagery) yok, çünkü çabuk eskir.
    Cihaz çerçevesi bu sınıfa girer [çıkarım; sayfa "çerçeve" demiyor, yalnız
    Wear OS için "cihaz çerçevesi koyma" diyor].
  - Üçüncü taraf marka logosu izinsiz yok.
  - Durum çubuğu temiz: bildirim ve operatör adı yok; pil ve ağ simgeleri dolu.
  - Yerelleştir; her görsele alt metin ekle.
  - "Stilize ekran görüntüleri, arayüzü birden çok görsele yaymak" serbest.
- Play Console'da simge, öne çıkan görsel ve ekran görüntüleri mağaza girişi
  denemesiyle A/B test edilebilir (varsayılan dilde ve 5 yerel dile kadar).
  - https://support.google.com/googleplay/android-developer/answer/6227309
- Başlık ve metadata: simge dâhil görsellerde yanıltıcı sembol yok.
  - https://support.google.com/googleplay/android-developer/answer/9898842

### Sende için sonuçlar [çıkarım]

- "Ekran görüntüsü çerçevesi": yok. Rakiplerin üçü çerçeve kullanıyor
  (Bölüm 2); Play bunu önermiyor. Ayrışma da buradan geliyor.
- Yerine: ürünün gerçek ekranı, kenara taşan kırpılmış, üstte tek cümle fayda
  (görselin %20'sinden az). İlk 3 görsel: (1) tek ekran, (2) müşteri bazlı
  grup, (3) "Sende kalır" güven görseli (veri cihazda).
- Üçüncü taraf logoları: Sende'nin arayüzü gerçek uygulama ikonlarını telefondan
  çekiyor. Mağaza görsellerinde WhatsApp, Instagram ikonu belirgin olmasın
  (WhatsApp kuralı: WhatsApp'ı malzemenin en belirgin öğesi yapma). Pazarlama
  görsellerinde kanal balonlarını nötr harf kutuları olarak çiz; gerçek
  ekran görüntüsünde ikonlar küçük kalsın. Play'in bunu nasıl yorumlayacağı
  doğrulanamadı.
  - https://www.meta.com/brand/resources/whatsapp/whatsapp-brand/
- Öne çıkan görsel zemini her yönde marka vurgu rengi (koyu yönde bile).
- Status bar: Roborazzi ile JVM'de üretilen ekran görüntüsünde durum çubuğu
  zaten yok; temiz çıkar.

## 12. Facebook ve Instagram reklam görselleri

Bu bölümün büyük kısmı üçüncü taraf kaynaklara dayanıyor. Meta'nın kendi reklam
rehberi ve yardım merkezi bu ortamdan açılamadı. Rakamlar "Meta'ya atıfla"
yazılmış blog rakamları; reklam vermeden önce Ads Manager önizlemesiyle doğrula.

### Ölçüler

- 1:1: 1080x1080. Kenarlardan yaklaşık 100 px boşluk (Meta bir rakam
  yayımlamıyor; çalışma değeri).
- 4:5: 1080x1350. Feed için ana ölçü. Üstten ve alttan yaklaşık 250 px boşluk.
- 9:16: 1080x1920 (Story ve Reels). Üst %14 (~269 px), alt %35 (~672 px),
  yanlar %6 (~65 px) boş. Sağ alt köşe %40. Reels'te yasal uyarı varsa alt
  %40 boş. Merkezde kalan çalışma alanı ~950x979 px.
  - https://adsuploader.com/blog/meta-ads-safe-zones
- Kive (14 Ekim 2026), Meta Ads Guide'a atıfla: Feed 4:5 için 1440x1800,
  Story/Reels 9:16 için 1440x2560 öneriliyor; JPG/PNG, en çok 30 MB; oran
  toleransı Instagram'da %1, Facebook feed ve reels'te %3; en küçük genişlik
  500-600 px; başlık sınırı Facebook feed'de 27, Instagram feed'de 40 karakter;
  birincil metin Instagram'da 125 karakter.
  - https://kive.ai/learn/meta-ad-sizes [tek kaynak]
  - Çıkarım: 1080 tabanında tasarla, 1440'a (x1,333) vektörle ölçekle; ikisini
    de ver. Aynı oranlar olduğu için güvenli bölge yüzdeleri değişmez.
- Instagram profil ızgarası Ocak 2025'ten beri tüm küçük resimleri 3:4 ortadan
  kırpıyor; 4:5 gönderilerin yan şeritleri ızgarada kesilir. Reklamlar için
  etkisi doğrulanamadı; yine de logo ve metni merkeze yakın tut.
  - https://buffer.com/resources/instagram-grid/ [arama özeti]
  - https://www.planoly.com/blog/guide-to-instagrams-new-vertical-grid.md
    [arama özeti]
- Metin oranı: Meta'nın "%20 metin" sınırı Eylül 2020'de kaldırıldı (artık
  dağıtımı cezalandırmıyor); yine de az metin genelde daha iyi. Play
  ekran görüntülerinde ise tagline %20 sınırı resmî kural.
  - https://adzooma.com/blog/facebook-advertising-removes-20-text-rule-on-images
    [arama özeti; kaynaklar aşama konusunda çelişiyor]
- Ads Manager'da "Safe zone guardrail" önizleme katmanı var (Kive ve
  AdsUploader aktarıyor). Aç ve kontrol et.

### İyi çalışan kalıplar (kanıt zayıf; test et)

- Meta'nın kendi (2023) verisi: Reels'te dikey ve sesli videoda doğrudan
  eylem kampanyalarında eylem başına maliyet %4,8 daha düşük. Meta'nın kendi
  A/B testi, bağımsız doğrulama yok. "34,5% daha ucuz" iddiasını doğrulayamadım.
  - https://www.facebook.com/business/news/reels-ads-updates-performance-features-automated-creative-suitability-solutions
- Videoda ilk 2-3 saniye ürünün arayüzünü göster; ekran kaydı ve gösteri
  tarzı videolar soyut marka videosundan iyi çalışıyor (satıcı blogları,
  kontrollü veri yok, arama özeti; hangi cümlenin hangi sayfada olduğunu
  tek tek doğrulamadım).
  - https://adlibrary.com/posts/meta-ads-for-app-install-campaigns [blog]
  - https://billo.app/blog/meta-ads-best-practices/ [blog]
  - Ses isteğe bağlı olsun, altyazı koy: Meta'nın eski araştırmasından arama
    özeti [tek kaynak, doğrulanamadı].
- Reddit r/FacebookAds (Ekim 2026): "video bu yıl çoğu hesapta daha iyi, ama
  açı biçimden önemli; statikle açıyı ucuza test et, kazananı videoya çevir;
  videonun yalnız ilk 3 saniyesini değiştir."
  - https://www.reddit.com/r/FacebookAds/comments/1x5syyb/static_or_video/
    [topluluk, küçük örnek]
- İki rakip uygulamanın 325 reklamının karşılaştırması: Chargemap gerçek
  sürücü röportajları ve görsel karışımı; Octopus %96 video, bunların %57'si
  çizgi film tarzı uygulama gezintisi, %28'i sayı kartı. Bir yorumcu
  röportajın "güven faktörü"nün daha büyük olduğunu söylüyor. Reklam
  Kütüphanesi harcama göstermez; hangisinin para kazandığı bilinmiyor.
  - https://www.reddit.com/r/FacebookAds/comments/1x8n5to/i_analysed_325_ads_from_two_ev_charging_apps_same/
    [topluluk, tek analiz]
- Başlangıçta en az 3 ayrı kavram test et (biri özellik, biri sonuç, biri
  güven). [çıkarım; satıcı blogları benzerini söylüyor ama sayfayı
  doğrulamadım]

### Sende için reklam kalıpları [çıkarım]

- Kalıp 1, "dağınıktan tek ekrana": solda karışık WhatsApp+Instagram bildirim
  yığını (nötr), sağda Sende tek ekranı. 4:5. Başlık en çok 7 kelime.
- Kalıp 2, "kaçan müşteri": 9:16, 8 saniye; bir bildirim kayboluyor, Sende'de
  duruyor. İmza anı ilk 2 saniyede.
- Kalıp 3, esnaf sesi: gerçek bir berber veya kuaför kendi telefonundan 10
  saniye anlatıyor (UGC). Güven için en güçlü, üretimi en pahalı. Metin ve
  izin: kişiden yazılı onay al.
- Kalıp 4, güven kartı: "Mesajların cihazında kalır." + Veri güvenliği
  özetinin kısa hâli. İddia ölçülebilir olmalı.
- Kalıp 5, yön testi: kısa listedeki iki yönün görselini aynı mesajla, küçük
  bütçeyle, yalnız Android cihazlara göster; tıklama ve kurulum maliyetini
  karşılaştır. Marka yönü seçimi için ek veri olur; tek başına karar değil.
- Yapma: "silinen mesajı gör", "görüldü atmadan oku" gibi iddia.
  - Kategorinin talebi bu ama (01, 03), Meta'nın gözetim/casus yazılım reklam
    politikası metnini okuyamadım [doğrulanamadı]; Play'in ve Meta'nın
    marka kurallarına takılma riski var. Esnaf diliyle yaz.
  - WhatsApp: logosunu en belirgin öğe yapma; adı fiil olarak kullanma
    ("WhatsApp'lamak" yok); doğru yazım "WhatsApp".
    - https://www.meta.com/brand/resources/whatsapp/whatsapp-brand/
- Hedefleme: Android (Türkiye'de ~%74), 25-54 yaş, işletme sahibi ilgisi.
  Instagram'da 62,3 milyon kişiye ulaşılabilir (Bölüm 6).
- Mağaza ile mesaj uyumu: reklamda söylenen cümle mağaza ilk ekranında da
  görünsün.

## 13. 5 FARKLI MARKA YÖNÜ

Ortak kurallar (5 yönün hepsi için)

- Simge: zil, balon, kalkan, robot, telefon+balon, rozet noktası YOK.
- Renk: mavi, turkuaz, WhatsApp yeşili, Instagram pembesi bölgesinden uzak.
- Zemin: koyu temada mavi-yeşil siyah (WhatsApp) yerine sıcak veya tonlu.
- Yazı: yalnız ₺ ve Türkçe harf taşıyan, OFL, RFN'siz (veya yeniden adlı) aileler.
- Marka sesi: "silinen mesaj" değil, işletme dili. Hitap "sen".
- Kontrast: Bölüm 5 kuralları; geçemeyen çift yönün içinde yazılı.
- Mağaza: cihaz çerçevesi yok; öne çıkan görsel zemini marka vurgu rengi.

Her yönün önceki konseptle ilişkisi "K-n'den fark" satırında.

### Yön 1: Sessiz Oda (sakin, koyu, premium)

- Metafor: gece geç saat, ışığı kısık, kapısı kapalı çalışma odası. Mesajlar
  kapıdan girer, kapı arkadan kapanır. Ses yok, acele yok.
- Renk (koyu, varsayılan)
  - zemin #17140F (sıcak kömür), yüzey #1F1B16, yüzey yüksek #29241E,
    çizgi #38322A.
  - metin #F2EEE6, ikincil metin #B0A99C.
  - vurgu "Adaçayı" #A9C7B2 (üstündeki metin #17140F), tehlike #F28B82.
- Renk (açık): zemin #F4F2EE, yüzey #FFFFFF, metin #1A1712, ikincil #5A5246,
  vurgu #2F5D4B (üstü #FFFFFF), tehlike #B3261E.
- Ölçüm [ölçüm]
  - metin/zemin 15,9:1; ikincil 7,9:1; vurgu/zemin 10,1:1.
  - Açık tema: ikincil 6,9:1; vurgu 6,7:1; beyaz üstü 7,5:1.
  - Rakip uzaklığı: adaçayı en yakın ikona (Spark Mail turkuazı) ΔE 12,1;
    WhatsApp yeşiline 20,0. Koyu zemin WhatsApp tuvaline ΔE 6-8.
- Yazı çifti: başlık Plus Jakarta Sans 600-700; gövde Onest 400-500 (gövdede
  koyu zeminde 450-500). İkisi OFL, RFN yok, ₺ ve tnum var. ~160 KB.
- Logo fikri: tek parça kalın çizgiyle çizilmiş S; alt ucu kendi gövdesine
  dönüp küçük kapalı bir halka oluşturur (kapalı döngü = "Sende kalır").
  Yazı işareti: alçak, geniş aralıklı "Sende".
- İkon stili: Material Symbols Rounded, wght 300, opsz 24, FILL 0; seçili
  durumda FILL 1. Çizgiler ince, uçlar yumuşak.
- Hareket karakteri: "Nefes". Yavaş, zıplamasız. Kart açılışı Standard yavaş
  uzamsal (0,9 / 300), 420-500 ms; solmalar 1,0 / 800. İmza anı: yeni mesajda
  vurgu rengi 600 ms'de alfa olarak yanıp söner, hareket yok. Haptik yok.
- Ton: sakin, kısa, alçak sesli (resmî-samimi ortada, ciddi, saygılı, düz).
  - "Hepsi burada." / "3 yeni mesaj. Acele yok." / "Dinleme duraklamış.
    Yeniden başlat."
- Kime hitap eder: çok mesaj alıp gürültüden yorulan, gizliliğe önem veren
  işletme sahibi (salon sahibi, danışman, klinik, butik). Geceleri de telefona
  bakanlar. Ödeme isteği daha yüksek olabilir (varsayım, ölçülmedi).
- Riski
  - "Koyu zemin + tek vurgu" kalıbı, yapay zekâ görsel dilinde varsayılan
    sayılıyor (06, Anthropic yönergesi). Bilinçli kabul; farkı sıcak kömür,
    sessiz yeşil, halka-S ve ses.
  - Play öne çıkan görselinde koyu zemin yasak: görsel zemini adaçayıdır.
  - Gündüz, güneşte ve "esnaf" için soğuk durabilir; açık tema eşit kalitede olmalı.
  - Yeşil çağrışımı: WhatsApp'a yakınlık algısı (ΔE 20); muted tonla azaltıldı,
    ikon önizlemesiyle sına.
- K1'den fark: K1 mavi-siyah (#0B0C10) + kehribar (#F0B240; FilterBox'a ΔE 4,6)
  + Manrope/Inter. Burada sıcak kömür + adaçayı + Plus Jakarta/Onest.
- Mağaza/reklam: öne çıkan görsel adaçayı zemin, kömür S; reklam: çok boşluk,
  tek cümle, 4:5.

### Yön 2: Dükkân (sıcak, dost canlısı, esnaf)

- Metafor: mahalle dükkânının tezgâhı: tente, çay bardağı, veresiye defteri.
  Esnaf müşterisini tanır, "hayırlı işler" der.
- Renk (açık, varsayılan)
  - zemin #FFF8E8, yüzey #FFFFFF, yüzey yüksek #FFEFC9, çizgi #EBD9AE.
  - metin "Kakao" #2B1B10, ikincil #6E5640.
  - vurgu "Hardal" #E3A008 (yalnız dolgu; üstündeki metin #2B1B10),
    vurgu-metin #8A5A00, başarı #2F7A57, tehlike #C0392B.
- Renk (koyu varyant): zemin #1F150D, yüzey #2A1D13, metin #F7EBD6,
  ikincil #C7B49A, vurgu #F0B429 (üstü #1F150D).
- Ölçüm [ölçüm]
  - metin/zemin 15,7:1; ikincil 6,5:1; hardal üstü kakao 7,3:1;
    vurgu-metin/zemin 5,6:1; başarı 4,9:1; tehlike 5,1:1.
  - Hardal/zemin yalnız 2,1:1: DÜŞÜK. Hardal asla ince çizgi, ikon veya
    metin olarak kullanılmaz; yalnız dolgu.
  - Koyu varyant: metin 15,2:1; ikincil 8,9:1; vurgu 9,6:1.
  - Rakip uzaklığı: hardal en yakın FilterBox'a (#FFAE00) ΔE 5,4; Headspace'e
    6,5; Past Notifications'a 7,2; VakıfBank'a 7,3 (sahibinden'e 20,7).
    Ayrışma renkten çok tente şekli ve yazıdan gelmeli.
- Yazı çifti: başlık Parkinsans 600-800; gövde Nunito 500-600. İkisi OFL,
  RFN yok, ₺ var. ~170 KB. Yuvarlak uçlar sıcaklık verir.
- Logo fikri: üst kenarı dükkân tentesi gibi dalgalı yuvarlak kare; içinde
  kalın, yuvarlak köşeli kakao S. Tente = "küçük işletme" (hedef kitle) ve
  özgün (ölçtüğüm 118 ikonun hiçbirinde yok; Türk grubunu tek tek görsel
  incelemedim).
- İkon stili: Phosphor Bold, yuvarlak uç; seçili durumda dolgu (Fill).
  Biraz büyük (26 dp), cömert dokunma alanı.
- Hareket karakteri: "Selam". Hafif zıplamalı. Kart açılışı Expressive
  varsayılan uzamsal (0,8 / 380); dokunuş geri bildirimi hızlı (0,6 / 800).
  İmza anı: yeni mesaj kartı 0,96 → 1,02 → 1 ölçeğinde "pop" ile gelir; hafif
  haptik.
- Ton: samimi, mahalle dili, kısa (samimi, hafif şakacı, saygılı, coşkulu).
  - "Hayırlı işler! 3 yeni mesaj var." / "Müşterin yazdı, bakar mısın?" /
    "Hepsi okundu. Çayını al."
  - "Hayırlı işler" Türk esnafında yaygın bir selamlama [çıkarım, kaynaksız].
- Kime hitap eder: berber, kuaför, güzellik salonu, butik, pastane; Instagram
  DM ve WhatsApp'tan randevu ve sipariş alan; 25-45 yaş. Ana hedef kitle.
- Riski
  - Sarı + yuvarlak yazı "çocuk uygulaması" veya "ucuz" okunabilir; premium
    his için çok boşluk, az dekor, ağır kakao metin şart.
  - Hardal beyaz zeminde 2,1:1 olduğu için erişilebilirlik tuzağı: her hardal
    öğe dolgu + koyu metin.
  - Sarı-turuncu hardal FilterBox, Headspace, VakıfBank ile yakın (ΔE 5-7).
  - Sarı+lacivert (Fenerbahçe) veya sarı+kırmızı (Galatasaray) eşlemesinden
    kaçın; bu yüzden kakao seçildi. Takım çağrışımı için kural bulunamadı.
  - Çay bardağı gibi yerel semboller küreselde anlaşılmaz; tente evrensel.
- K2'den fark: K2 krem + kiremit (#B03F1B) + teal ikon; Bricolage/DM Sans/
  JetBrains Mono. Burada sarı-kakao + tente + Parkinsans/Nunito. Krem + kiremit
  + serif "varsayılan" kalıbından (06) uzak; zemin sıcak ama marka rengi sarı.
- Mağaza/reklam: hardal zeminli öne çıkan görsel; 4:5 reklamda hardal panel,
  kakao büyük başlık, çizim (stok fotoğraf yok).

### Yön 3: Süzgeç (teknik, keskin)

- Metafor: filtre ve ölçü aleti. Gürültüyü süzer, sinyali işaretler; stabilo
  ile altı çizilen tek satır.
- Renk (açık, varsayılan)
  - zemin #EEF0F2, yüzey #FFFFFF, yüzey yüksek #E3E6EA, çizgi #C9CED5.
  - metin #0E1114, ikincil #4B5560.
  - vurgu "Sinyal" #F2431D (üstündeki metin SİYAH #0E1114, beyaz değil),
    vurgu-metin #C2310F, tehlike #B3261E.
- Renk (koyu): zemin #141312, yüzey #1B1A18, yüzey yüksek #242321,
  çizgi #34322F, metin #EEEDEA, ikincil #A3A09A, vurgu #FF5A36 (üstü #141312),
  tehlike #FF8A80.
- Ölçüm [ölçüm]
  - Açık: metin/zemin 16,6:1; ikincil 6,6:1; vurgu/zemin 3,28:1 (sınırı
    geçiyor); siyah üstü vurgu 5,05:1; BEYAZ üstü vurgu 3,75:1 = DÜŞÜK
    (beyaz yazı yasak); vurgu-metin 4,9:1.
  - Koyu: metin 15,9:1; ikincil 7,1:1; vurgu 6,0:1.
  - Rakip uzaklığı: Todoist'e ΔE 6,3, Gmail'e 6,6 (kırmızı-turuncu bölge).
  - Koyu zemin #141312, WhatsApp tuvaline ΔE 4-6; ilk denediğim #0E1114
    ΔE 1,1 çıktığı için elendi.
- Yazı çifti: Mona Sans (başlık ve gövde; genişlik ekseni 75-125) + IBM Plex
  Mono (saat, sayaç, etiket). ₺ ve Türkçe tam. RFN var ("Mona", "Plex"):
  kırpmadan kullanırsan ~600 KB; kırpacaksan yeniden adlandır, ~220 KB.
  Etiketlerde uppercase(Locale("tr")).
- Logo fikri: geometrik S, yatay üç ince yarıkla dilimlenmiş (tarama çizgisi):
  gürültü dilimlenir, S kalır. Küçük boyutta yarıklar kalınlaşır.
- İkon stili: Material Symbols Sharp, wght 400, FILL 0, köşeli uçlar, 20 dp
  ızgara; sayılar ve etiketler mono.
- Hareket karakteri: "Klik". Zıplamasız, çabuk. Standard hızlı uzamsal
  (0,9 / 1400) ve efekt (1,0 / 3800); 120-180 ms; açılış kesme maskesiyle
  (clip). İmza anı: sayaç rakamı "tık" ile döner, kart tek kare çakar.
  Haptik: tık.
- Ton: kısa, komut gibi, sayı odaklı (resmî-nötr, ciddi, saygılı, düz).
  - "12 bildirim süzüldü. 3 mesaj kaldı." / "Dinleme: kapalı. Aç."
- Kime hitap eder: birden çok kanalı yöneten e-ticaret satıcısı, serbest
  çalışan, "kontrol" ve filtre isteyen teknik meraklı.
- Riski
  - Soğuk ve teknik: esnaf için "karmaşık" ve yabancı hissettirebilir.
  - Mono etiket ve teknik hava yapay zekâ/geliştirici aracı kalıbı (06).
  - Kırmızı-turuncu "hata" çağrışımı taşır; hata durumunda ikonla ayır.
  - Todoist, Gmail, Vodafone, Hepsiburada, Trendyol'a yakın bölge (ΔE 6-9).
  - Mona/Plex RFN lisans yükü.
- K3'ten fark: K3 koyu grafit + limon (#C8F23A) + Space Grotesk/Inter/JetBrains
  Mono (₺ yok). Burada açık gri + vermilion + Mona/Plex; koyu zemin WhatsApp'tan
  uzaklaştırıldı.
- Mağaza/reklam: öne çıkan görsel vermilion zemin, siyah dilimli S; reklam:
  büyük rakam ("12 → 3"), mono etiket.

### Yön 4: Defter (editoryal, serif)

- Metafor: esnafın defteri ve sabah postası. Mesajlar satır satır kayda geçer;
  cetvelli sayfa, kenar çizgisi, ayraç.
- Renk (açık; koyu varyant ikinci aşama)
  - zemin "Kâğıt" #F7F6F2, yüzey #FFFFFF, yüzey yüksek #ECEAE3,
    çizgi (cetvel) #D8D6CF.
  - metin "Mürekkep" #14161C, ikincil #55596A.
  - vurgu "Mavi mürekkep" #1F3A93 (üstü #FFFFFF), başarı #2E6B4F,
    tehlike "Kırmızı kalem" #B3261E.
- Ölçüm [ölçüm]
  - metin/zemin 16,7:1; ikincil 6,4:1; vurgu/zemin 9,3:1; beyaz üstü 10,1:1;
    tehlike 6,0:1; başarı 5,8:1.
  - Rakip uzaklığı: PayPal'a ΔE 3,5; İşCep'e 5,2. Yani kategorideki en
    kalabalık renk; ayrışma tipografiden ve kâğıt dilinden gelmeli.
- Yazı çifti: başlık ve mesaj gövdesi Newsreader (opsz 6-72; ekran okuma için
  tasarlı); arayüz etiketi Public Sans. İkisi OFL, RFN yok, ₺ var. ~180 KB
  (Newsreader opsz 16 ve wght 400-700'e sabit 114 KB + Public Sans 64 KB).
- Logo fikri: serif S, ciltli defter kapağı gibi mürekkep mavisi kare üzerinde;
  kapağın içinde sağ üstte ince bir ayraç şeridi (okunmamış mesajı işaretler).
  Ayraç 66 dp güvenli bölgede kalır.
- İkon stili: Phosphor Light, ince çizgi; seçili durum ikon dolgusu değil,
  altı çizili ince çizgi (hairline). Çizgiler cetvel gibi hizalı.
- Hareket karakteri: "Sayfa". Orta hızlı, zıplamasız. Standard varsayılan
  uzamsal (0,9 / 700), 320-420 ms; kaydırma ve çapraz solma. İmza anı: yeni
  mesajda cetvel çizgisi soldan sağa 240 ms'de "çekilir", mürekkep mavisi
  tik düşer. Haptik yok.
- Ton: edebî ama kısa, tam cümle, nazik (resmî, ciddi, saygılı, düz).
  - "Bugünün postası." / "Üç yeni mesaj kayda geçti." / "Hepsi okundu."
  - "siz" hitabı da yakışır; ama ad "sen" olduğu için karar gerek. [çıkarım]
- Kime hitap eder: formal esnaf: muhasebeci, emlakçı, klinik, avukat, diş
  hekimi; 35+; "kayıt tutma" kültürü olanlar.
- Riski
  - Serif + kâğıt tonu "editoryal varsayılan" kalıbında (06, Anthropic
    yönergesi: ince çizgili gazete düzeni).
  - Mesajı serifle okumak telefonda yorucu olabilir; 16 sp altına inme.
  - Yavaş, eski ve resmî görünebilir; genç kitleyi kaçırır.
  - Mavi mürekkep: en kalabalık hue (ΔE 3,5-5,2); ayrışma tipografiye bağlı.
- K4'ten fark: K4 krem + zinober (#C4361B; Akbank'a ΔE 4,5) + Fraunces/
  Instrument Sans (₺ yok). Burada soğuk kâğıt + mavi mürekkep + Newsreader/
  Public Sans; kırmızı yalnız hata için.
- Mağaza/reklam: öne çıkan görsel mürekkep mavisi zemin, kâğıt renkli serif
  başlık; reklam: tek cümlelik başlık + cetvelli arka plan.

### Yön 5: Afiş (cesur, grafik)

- Metafor: mahalle duvarındaki büyük harfli afiş: kes-yapıştır, iri tipografi,
  düz renk. Gürültüyü bağırarak değil, kalın bir şekille keser.
- Renk (açık, varsayılan)
  - zemin #FFFFFF, yüzey #FBF6FA, yüzey yüksek #F1E6EE, çizgi #E3D3DF.
  - metin #1A0E18, ikincil #5E4A5B.
  - vurgu "Mürdüm" #5B1B52 (üstü #FFF6F1); ikinci vurgu "Şeftali" #FF9B85
    (yalnız dolgu; üstü #1A0E18); tehlike #B3261E.
- Renk (koyu): zemin #1A0E18, yüzey #241522, yüzey yüksek #2F1D2C,
  çizgi #432C3F, metin #FBEFF7, ikincil #C4A9BE, vurgu #FF9B85 (üstü #1A0E18),
  tehlike #FF8A80.
- Ölçüm [ölçüm]
  - Açık: metin/zemin 18,8:1; ikincil 8,1:1; mürdüm/zemin 12,2:1; açık üstü
    mürdüm 11,4:1; şeftali üstü koyu 9,2:1.
  - Şeftali/beyaz 2,0:1 DÜŞÜK; yalnız dolgu. Mürdüm üstünde şeftali 6,0:1.
  - Koyu: metin 16,8:1; ikincil 8,7:1; vurgu 9,2:1.
  - Rakip uzaklığı: mürdüm en yakın ikona (Proton VPN) ΔE 14,9; şeftali
    Asana'ya 12,3. Beş yönün en ayrışanı.
  - Koyu zemin WhatsApp tuvaline ΔE 11-12 (belirgin tonlu).
- Yazı çifti: başlık Unbounded 700-900 (geniş, ağır); gövde Epilogue 400-600.
  İkisi OFL, RFN yok, ₺ ve tnum var. ~260 KB (Unbounded wght 600-900'e
  sabit 148 KB + Epilogue 114 KB).
- Logo fikri: iri S ikonun kenarına taşan biçimde kırpılmış; iki düz renk
  parça (mürdüm, şeftali) çapraz bir kesikle birbirine kaymış, yapışkan etiket
  (sticker) gibi. S 66 dp güvenli bölgede okunur kalmalı.
- İkon stili: Material Symbols Rounded, FILL 1, wght 600-700, 28 dp; büyük,
  kalın, dolgulu.
- Hareket karakteri: "Kes-yapıştır". Zıplamalı ve iddialı. Expressive hızlı
  uzamsal (0,6 / 800), 240-320 ms; geniş şekil silmeleri, 40 ms kademeli
  giriş. İmza anı: yeni mesaj kartı büyük bir şeftali parçayla kayarak girer
  ve oturur. "Ekranı tertemiz yap": kartlar tek tek kesilir, güçlü haptik.
- Ton: iddialı, kısa, bağıran (samimi, kısmen şakacı, saygısızlığa yakın,
  coşkulu).
  - "Gürültü yok. Müşteri var." / "Okundu. Bitti. Sıradaki."
- Kime hitap eder: 18-30 yaş genç girişimci, sosyal medya satıcısı, içerik
  üreticisi; reklam akışında dikkat çekmesi gerekenler.
- Riski
  - Mürdüm + şeftali Türkiye'de alışılmadık; "bağıran" görünüm güven ve
    premium hissi zayıflatabilir.
  - Berber ve erkek esnaf için pembe-mor tonları uzak durdurabilir [çıkarım,
    ölçülmedi]. Instagram kullanıcılarının %53,5'i erkek (DataReportal).
  - Kalın harf kırpması adaptive ikon 66 dp güvenli bölgeye sığmazsa tanınmaz.
  - Trend hızlı eskir (neo-grafik, afiş estetiği).
  - Unbounded geniş olduğu için dar başlıklarda taşar; Türkçe uzun kelimelerde
    ("bildirimleriniz") satır kırılması sınanmalı.
- K5'ten fark: K5 kobalt (#1E35C8; PayPal'a ΔE 5,9) + mercan + Sora (₺ yok) +
  DM Sans, iki balon parçası. Burada mürdüm-şeftali + Unbounded/Epilogue +
  kesik S.
- Mağaza/reklam: öne çıkan görsel mürdüm zemin, şeftali dev başlık; reklam:
  9:16'da iri tipografi, merkezde.

## 14. Yönlerin karşılaştırması ve seçim önerisi

- Hangi soruya cevap
  - Sessiz Oda: "Premium ve gizli hissettiren bir araç" (güven, sakinlik).
  - Dükkân: "Esnafın kendi aracı" (hedef kitleye en yakın, en çok ayrışan).
  - Süzgeç: "Kontrol ve verim" (teknik kitle).
  - Defter: "Güvenilir kayıt" (en güvenli, en sıradan).
  - Afiş: "Dikkat çek" (en cesur ve en riskli).
- Kategoriden uzaklık (renk) [ölçüm]: Afiş (ΔE 15) > Sessiz Oda (12) > Dükkân
  (5-7) ≈ Süzgeç (6-7) > Defter (3-5).
- Kategoriden uzaklık (simge) [çıkarım]: Dükkân (tente) ve Defter (ayraç) en
  özgün; Sessiz Oda (halka-S) ve Süzgeç (dilimli S) orta; Afiş (kesik S)
  yüksek ama S kalıbı.
- Tavsiyem (kanıtlı, ama karar kurucunun)
  - Kısa liste: Dükkân ve Sessiz Oda. Dükkân hedef kitle (esnaf) ve mağazada
    ayrışma için; Sessiz Oda "premium ve güven" için. İkisi de rakiplerin
    mavi-turkuaz bölgesinden uzak.
  - Defter en güvenli ama ayrışmıyor; Süzgeç niş; Afiş reklam dili olarak
    ilginç ama ürün kimliği olarak riskli.
  - Karıştırma: "ikon Dükkân, arayüz Sessiz Oda" gibi melez yapma. Tutarlılık
    hatırlanırlığın şartı (Bayçu).
- Seçim süreci [çıkarım]
  - 1) İki yönün 48 px ikonunu WhatsApp, Telegram, Instagram, BiP, Notisave
    ikonlarının yanına koy; 5 saniye sına.
  - 2) Her iki yön için themed ikon ve bildirim küçük ikonu denemesi.
  - 3) Yayınlandıktan sonra Play mağaza girişi denemesiyle ikon ve ilk 3 ekran
    görüntüsünü A/B test et.
  - 4) Reklamda 3 kavramı aynı bütçeyle test et.

## Sende için çıkarımlar

### Karar ve strateji

- Yap: tek renk, tek simge, tek ses seç ve tüm yüzeylerde aynı kullan. Neden:
  ad ortak bir sözcük; ayrışma görselden ve tutarlılıktan gelecek (Bayçu,
  Ries).
- Yap: ana rengi kategorinin boş bölgelerinden seç ve rakip ikon rengine
  ΔE ≥ 10 hedefle. Neden: 27 bildirim uygulamasının %74'ü mavi/turkuaz.
- Yapma: mavi, turkuaz, WhatsApp yeşili, Instagram pembesi; zil, balon,
  kalkan, robot simgesi. Neden: kategori sıradan, WhatsApp marka kuralları
  benzer görseli yasaklıyor.
- Yap: "Sende kalır" iddiasını kanıtla taşı. Yapma: Play metninde "internet
  izni yok" deme (Play sürümü Billing yüzünden INTERNET izni taşıyor).
  Neden: ölçülmemiş izin iddiası yasak (S-14) ve güveni zedeler.
- Yap: kısa liste Dükkân + Sessiz Oda. Neden: hedef kitle ve ayrışma.

### Logo ve ikon

- Yap: logo 48-66 dp, monochrome katman düz silüet, 108 dp katmanlar, 66 dp
  güvenli bölge; küçük ikon tek renk S silüeti.
  - https://developer.android.com/develop/ui/views/launch/icon_design_adaptive
- Yapma: logoda rozet noktası (özellikle sağ üst). Neden: Play, "yeni mesaj
  noktası" gibi yanıltıcı simgeyi yasaklıyor.
  - https://support.google.com/googleplay/android-developer/answer/9898842
- Yap: Play 512 simgesini tam kare, opak zeminle ver; köşe ve gölgeyi Play ekler.
- Yap: açılış ekranı ikonunu 288/192 dp kuralıyla hazırla; animasyon ≤ 1000 ms.

### Renk

- Yap: her çiftin kontrastını hesapla (metin ≥ 4,5, ideal ≥ 7; metin dışı ≥ 3).
  Yapma: hardal veya şeftaliyi açık zeminde çizgi/simge/metin olarak kullanma
  (2,0-2,1:1). Yapma: vermilion üstüne beyaz yazı (3,75:1).
- Yap: koyu temayı mavi-yeşil siyahtan uzak tut (WhatsApp tuvali #0B141A →
  #0A1014; mevcut #0C0D10 ΔE 2,2). Sıcak kömür veya belirgin tonlu zemin kullan.
- Yap: durum için renk + ikon/kelime. Renk körlüğü için.
- Yap: temanın tek kaynağı jeton dosyası olsun (06, Bölüm 7).

### Tipografi

- Yap: yazı tiplerini res/font'a göm; en çok 2 aile; eksenleri sınırla,
  Latin+Latin-Ext'e kırp (toplam 160-260 KB, yani mevcut 2,55 MB APK'nın
  yaklaşık %6-10'u).
- Yapma: Fredoka ve Bagel Fat One (Türkçe harf eksik). Yapma: ₺ gösteren
  yerlerde Sora, Outfit, Geist, Instrument Serif, JetBrains Mono, Hanken
  Grotesk, Figtree (₺ yok). Fiyatı olan metne ₺ taşıyan aile ver; ya da "TL"
  yaz.
- Yap: Türkçe BÜYÜK HARF için uppercase(Locale("tr")). Neden: "işlendi"
  "IŞLENDI" olur.
- Yap: RFN'li fontu (Mona, Hubot, Plex) kırpmayacaksan olduğu gibi kullan,
  kırpacaksan yeniden adlandır.
- Yap: ekran görüntüsü testine "ğ ş ı İ ö ü ç ₺ 200% yazı ölçeği" cümlesi ekle
  (Roborazzi hattı zaten var; 06 Bölüm 8).

### Play ve reklam

- Yap: öne çıkan görsel zemini canlı marka rengi; saf beyaz, siyah, koyu gri
  kullanma. Yap: kesilme bölgelerine logo/ad koyma; merkeze odaklan.
- Yap: ekran görüntüleri 9:16, en az 1080x1920, en az 4 adet; tagline ≤ %20.
  Yapma: cihaz çerçevesi, parmak, "Best, #1, Free, Download now", yorum
  alıntısı, rozet. Yap: durum çubuğu temiz; alt metin ekle.
- Yap: reklamı 1080x1350 (4:5) ana ölçüyle tasarla, 9:16'da üst %14, alt
  %35, yanlar %6 boş bırak; 1440 ölçeğini de üret. Reklam öncesi Ads
  Manager güvenli bölge katmanıyla kontrol et.
- Yap: 3 kavramı küçük bütçeyle test et; önce statik, kazanan açıyı videoya
  çevir. Android hedefle.
- Yapma: "silinen mesajı gör" iddiası; WhatsApp logosunu belirgin yapma.
  Neden: marka kuralı ve politika riski; doğrulanamadı kısmı var (Bölüm 12).

### Ölç ve doğrula

- 48 px yan yana ikon testi, 5 saniye testi, temalı ikon testi.
- Yayın sonrası Play mağaza girişi denemeleri (ikon, öne çıkan görsel, ilk
  üç ekran).
- Hareketi yönün karakteriyle test et (video kaydı; statik ekran hareketi
  göstermez).

## Denenen ve çalışmayan yollar

- Meta reklam rehberi ve yardım merkezi (resmî ölçüler, güvenli bölge)
  - curl: HTTP 400 ("Sorry, something went wrong"). Sayfalar bot
    engelli veya JavaScript ile çiziliyor.
  - Web okuma aracı: 404 veya yalnız sayfa başlığı döndü, gövde yok.
  - Okuyucu proxy (r.jina.ai): facebook.com için 403.
  - Headless Chromium (Playwright, PLAYWRIGHT_BROWSERS_PATH ile): 3 denemede
    ERR_CERT_AUTHORITY_INVALID (proxy CA'sı tarayıcı depolarında tanınmadı).
    TLS doğrulamasını kapatmadım. Sonuç: reklam ölçüleri üçüncü taraf
    kaynaklara (Kive, AdsUploader) dayandı, öyle işaretlendi.
- Meta casus yazılım/gözetim reklam politikası: transparency.meta.com sayfası
  404; arama özeti ilgisiz çıktı. Doğrulanamadı.
- Reddit başlık araması (arşiv API'si): başlık filtreli sorgular zaman aşımına
  uğradı. Çözüm: tarih penceresiyle gönderileri çekip yerelde süzdüm; yalnız
  son ~3 haftayı kapsıyor, küçük örnek.
- Reddit'te doğrudan konu bulma (web arama): sonuçlar Reddit çıkmadı; yalnız
  blog ve satıcı sayfaları.
- ResearchGate (renk ve güven çalışması): 403. ProAndroidDev (temalı ikon
  rehberi): 403. m3.material.io renk rolleri: 404. StatCounter: JavaScript,
  rakamlar arama özetinden.
- Material "ton farkı 40 = 3:1, 50 = 4,5:1" resmî cümlesi bulunamadı; kaynak
  koddaki kontrast formülüyle kendim hesapladım.
- Bir arama sonucu bildirim erişimi için CISPA makalesi diye geldi; açınca
  WebView izinleri hakkında çıktı (ilgisiz). Kullanmadım.
- Türk uygulama paket kimliği tahminleri açılmadı: Papara, Enpara, Param,
  Tosla, QNB (alternatif), Albaraka, Garanti Bonus, Çiçeksepeti, N11, Dolap,
  GittiGidiyor, Hepsipay, Paycell, Türk Telekom. Ayrıca 1Password,
  Standard Notes, Pocket, Linear, Skype paketleri. Bu uygulamalar ölçüme
  girmedi.
- PDF okuma: sistemde pdftotext yok; web okuma aracı PDF'i ham verdi. Çözüm:
  PDF'i indirip kendi betiğimle (pypdf, sistem şifreleme modülünü devre dışı
  bırakarak) metne çevirdim.
- Takım renkleri ve marka çağrışımı, markalarda sen/siz tercihi, berber ve
  kuaförde WhatsApp Business kullanım oranı: arandı, güvenilir kaynak
  bulunamadı.
- Android bildirim küçük ikonunun boyutu (24 dp) ve beyaz-şeffaf kuralı: güncel
  resmî sayfalarda yazmıyor (yalnız "durum çubuğunda tek renk" ve Android 12
  renk kuralı var). Eski tasarım belgesinin yansısını kullandım.
- Behance ve Dribbble: bu kolda açılmadı (06'da okuyucu proxyyle bakılmıştı).

## Doğrulanamayanlar ve güvenilirlik notları

- Meta reklam ölçüleri ve güvenli bölge yüzdeleri: Meta'nın kendi sayfası
  okunamadı. 14/35/6 yüzdeleri birden çok bağımsız satıcı yazısında aynı
  (AdsUploader, Kive, ve arama sonuçlarındaki diğerleri), ama ikisi de
  Meta'ya atıf yapan ikincil kaynak. 1080 ve 1440 farkı çözülmedi.
- 4:5 için "250 px boşluk" ve 1:1 için "100 px" Meta rakamı değil, çalışma
  değeri (AdsUploader'ın kendi notu).
- Instagram 3:4 ızgarasının reklam yerleşimine etkisi bilinmiyor.
- Meta'nın "%34,5 daha düşük maliyet" iddiası doğrulanamadı; yalnız "%4,8 daha
  düşük eylem başı maliyet" (Ekim 2023, kendi testi) okundu.
- WhatsApp koyu tema renkleri üçüncü taraf ve haber özetinden; resmî değil.
  Telegram koyu renkleri doğrulanamadı.
- Türkiye Android payı (%74) StatCounter'dan arama özetiyle; sayfa açılmadı.
- TÜİK rakamları (%90 WhatsApp) iki haber sitesinden; bültenin kendisi
  okunmadı.
- Bayçu ve Ustaoğlu: 200 kişi, kolayda örneklem, Eskişehir, 2015. Capital
  yazıları 2007 ve 2014; görüş ağırlıklı.
- Madden, Hewett, Roth (2000) makalesi okunamadı; ikincil alıntı.
- Wiktionary kullanıcı katkılı; anlam ve kullanım notu için yeterli, resmî
  sözlük değil. "sende kalsın" deyimi TDK'da bulunamadı.
- ΔE yorum tablosu yazarın kendi testi.
- Ürün ikonları 256 px küçük resimden ölçüldü; baskın renk bölme (quantization)
  kaba. 8 komşu ürün (Beeper, Pushbullet vb.) saf rakip sayılmadı.
- Fontlar google/fonts "main" dalından, 21 Ekim 2026; sürümler değişebilir.
  Gözle yalnız çizilen cümleyi baktım; uzun metin satır aralığı sınanmadı.
- Material Symbols'u Compose'a vektör XML olarak aktarma ve fontun boyutu
  bu projede denenmedi.
- Reklam kalıplarının çoğu satıcı blogu ve küçük Reddit örneği; kontrollü
  A/B verisi yok. Test et.
- "Hayırlı işler" selamlamasının yaygınlığı kaynaksız (çıkarım).
- Marka adı tescili ve alan adı: docs/MARKA_ADI_KONTROL.md'de; burada
  yeniden araştırılmadı.

## Temizlik ve lisans notu

- İndirilen başkalarının görselleri (118 uygulama ikonu iki turda, 4 uygulamanın
  mağaza ekran görüntüleri; üçünü açıp inceledim) ve oluşturduğum özet panoları
  not alındıktan hemen sonra silindi. Kalan: yalnız sayısal sonuçlar ve betikler.
- İndirilen yazı tipi dosyaları (OFL), kırpılmış kopyalar ve kendi çizdiğim
  örnek sayfaları ölçüm içindi; uygulamaya eklenmedi, çalışma klasöründen silindi.
- Başkasının kodu kopyalanmadı. Kaynak kod yalnız sabitleri okumak için açıldı:
  AndroidX (Apache-2.0), Material Color Utilities (Apache-2.0), google/fonts
  meta dosyaları ve OFL.txt metinleri.
- Betikler (yazı tipi denetimi, ikon rengi ölçümü, kontrast/ΔE hesabı) yalnız
  scratch klasöründe; repoya eklenmedi.
