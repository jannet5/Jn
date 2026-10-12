# This AI Automation Posts to Instagram and Facebook Using Your Own Images — Josh Nocode (meta'da kanal alanı boş; videoda "Josh Nocode") — 27 dk — https://www.youtube.com/watch?v=DIXXW_jBS0w

## Tek paragraf: ne anlatıyor
İşletmenin kendi gerçek fotoğraflarını (spor salonu örneği: yeni ekipman, satılan ürünler, 5 yıldızlı yorum ekran görüntüleri) Google Drive'daki bir klasöre atınca, Make.com'un her gün fotoğrafı GPT-4o vision ile analiz edip marka kimliğine uygun Facebook caption + mesaj ve Instagram caption yazdığı, sonra Facebook Pages ve Instagram Business'a otomatik paylaştığı iki senaryolu bir sistemi sıfırdan kuruyor. "No fake AI images, just your real content" vurgusu var. Senaryo 1 (file sync): Drive'a düşen dosyayı temizleyip Google Sheet "photo log"a kaydediyor. Senaryo 2 (master): her sabah 8'de "not yet" durumundaki ilk fotoğrafı alıyor, analiz ediyor, iki kola (FB / IG) dağıtıp paylaşıyor ve Sheet'i işaretliyor. Instagram API'si Drive linkini indiremediği için fotoğraf önce public AWS S3 bucket'a yükleniyor. Sonda bu kurulumu "done for you" hizmet olarak sattığını söylüyor.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Kime: içerik üreticileri, işletme sahipleri, operasyon çalışanları, sosyal medya yöneticileri (örnek persona: spor salonu sahibi).
- Ne: "for those of you out there looking for a done for you solution, this is actually a service that I offer" — bu otomasyonu müşteri için kurma (done-for-you), görüşme rezervasyonuyla satış.
- Kaça: rakam vermiyor.
- Maliyet: OpenAI API'ye "something like $5" (5 USD) yükleme; "it hasn't been that expensive". AWS S3 fiyatı rakam vermiyor (pricing sayfasına yönlendiriyor). Make ücretsiz başlanıyor (affiliate linkle 1 ay Pro).

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
YouTube eğitim videosu → videoda "a link in the description below where you can book a call with me" (görüşme rezervasyonu). Mesaj şablonu vermiyor. (Açıklamada bu rezervasyon linki fiilen yok; sadece Make affiliate linki var.)

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Google Drive, Google Sheets, Make.com, OpenAI (GPT-4o, vision), Facebook Pages, Instagram for Business, AWS S3 (+ IAM).

Drive/Sheet hazırlığı:
1. Drive'da proje klasörü (örn. "tutorial") → alt klasör "photo library".
2. Google Sheet "social photo database", sayfa "photo log", sütunlar: A File ID | B File name | C Upload date and time | D Posted to Facebook | E Posted to Instagram | F Initiated master upload.
3. İkinci sayfa "brand identity": A1 = "brand identity", A2 = markayı anlatan 2-3 cümle.

Senaryo 1 — "upload new photos to social media database" (her 15 dk):
- Trigger: Google Drive "Watch files in a folder" (photo library, by created time, limit 10)
- → Text parser "Replace": pattern `[#\s]`, new value = empty string, global match yes, case sensitive no, text = dosya adı (hashtag ve boşlukları siler, sonraki hataları önler)
- → Google Sheets "Add a row": File ID, temiz dosya adı, upload date = `formatDate(createdTime; "MM/DD/YYYY h:mm A")`, D/E/F = "not yet"
- → Google Drive "Update a file": dosya adını temiz adla değiştir

Senaryo 2 — "turn my photos into Instagram and Facebook post" (her gün 08:00):
- Trigger: Google Sheets "Search rows" (photo log, filtre F = "not yet", limit 1)
- → Google Sheets "Update a row": F = "yes"
- → Filtre "photo has not been posted": total number of bundles ≠ 0
- → Google Drive "Download a file" (File ID)
- → OpenAI "Create a completion" — GPT-4o, max tokens 500, image input = indirilen dosya, image detail auto ("describe image prompt": nesneler, yerleşim, aktivite için nötr görsel tarif)
- → Google Sheets "Get a cell": brand identity!A2
- → Router
  - Kol 1 (Facebook): OpenAI GPT-4o "Prompt 2A Facebook caption" (girdi: image description, file name, brand identity; max 300 token) → OpenAI GPT-4o "Prompt 2B Facebook message" (girdi: FB caption, image description, file name, brand identity; birinci tekil şahıs mesaj; max 300) → Facebook Pages "Create a post with photos" (photo = Drive dosyası, caption = 2A, message = 2B) → Sheets "Update a row" D = yes
  - Kol 2 (Instagram): OpenAI GPT-4o "Instagram caption prompt" (kısa, vurucu, hizmet işletmesi odaklı; girdi: image description, file name, brand identity; max 300) → AWS S3 "Upload a file" → Tools "Set variable" `encoded slug` = `replace(fileName; space; +)` → Instagram for Business "Create a photo post" (photo URL = `https://<bucket>.s3.<region>.amazonaws.com/<encoded slug>`, caption = IG prompt sonucu) → Sheets "Update a row" E = yes

AWS S3 kurulumu:
1. Bucket oluştur (küçük harf, boşluksuz) → "Block all public access" kapat → bucket policy ile public okuma (Resource'ta bucket adı).
2. IAM → Create user → "AmazonS3FullAccess" → access key (third-party service) → Make bağlantısına access key + secret, bölge (örnekte US East Ohio).

Prompt'lar: Videoda bir Google Doc'tan kopyalanıyor (Describe image prompt, Prompt 2A Facebook caption, Prompt 2B Facebook message, Instagram caption prompt) ama prompt metinleri ekranda okunmuyor ve doküman açıklamada linklenmemiş — aynen alıntılanamadı. Videoda geçen tanımlar:
- Describe image: "analyze the attached image and provides a neutral visual description of what's shown including objects, layout, and activity" — fotoğrafın tarafsız tarifi.
- 2A: "generate a short brand aligned Facebook caption based on an image description, file name, and brand identity" — kısa, markaya uygun FB caption.
- 2B: "a first-person Facebook message that introduces and supports the post caption" — caption'ı destekleyen birinci şahıs gönderi metni.
- IG: "crafts a short, punchy Instagram caption using the image description, file name, and brand identity for a service-based business" — hizmet işletmesi için kısa, vurucu caption.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: işletmenin gerçek fotoğrafı + AI caption (+ FB'de ayrıca mesaj metni) + hashtag'ler (örnek çıktılarda hashtag var). Örnek konular: ekipman, ürün, müşteri yorumu ekran görüntüsü.
- Sıklık: günde 1 post (08:00), haftanın belirli günleri veya ayın günleri de seçilebilir.
- Dosya adı bilgisi de prompt'a girdiği için fotoğrafı anlamlı adlandırmak caption kalitesini artırır (bizim çıkarım, videoda file name girdisi var).
- Reklam: Videoda yok.

## Hatalar / uyarılar
- Prompt'ların bulunduğu Google Doc açıklamada linklenmemiş; açıklamada tek link Make affiliate linki (https://www.make.com/en/register?pc=joshnocode), açılmadı. Kaynak dosyası kaydedilmedi; prompt metinleri yok.
- Instagram API Drive linkinden görsel indiremiyor ("Even if it's publicly available") → public URL (S3) şart.
- Dosya adındaki # ve boşluklar akışı bozuyor → Text parser ile temizleniyor; S3 URL'sinde boşluklar `+` ile kodlanıyor.
- Master senaryo F sütununu paylaşımdan ÖNCE "yes" yapıyor; FB/IG kolu hata verirse fotoğraf tekrar denenmez (D/E "not yet" kalır, elle takip gerekir).
- S3 bucket'ı tamamen public ve IAM kullanıcısına S3 Full Access veriliyor — güvenlik açısından geniş yetki (bizim not).
- Örnek testte AI, Napolyon tablosunu "automation" markasına bağlayarak caption yazmış — marka kimliği metni her şeye uygulanıyor, alakasız görselde zorlama içerik çıkabilir.

## Bizim ajansa alınacaklar (somut)
1. Restoran/klinik/kuaför için "Fotoğrafını at, biz paylaşalım" paketi: müşteriye paylaşılan bir Drive klasörü (veya WhatsApp → Drive) verelim; her gün sabit saatte 1 gerçek fotoğraf AI caption ile IG + FB'ye çıksın. Yemek ve klinik görsellerinde AI görsel yerine gerçek fotoğraf kuralı.
2. Sheet şemasını aynen alalım ve genişletelim: `file_id | dosya_adi | yukleme_tarihi | fb | ig | gbp (Google İşletme) | onay | durum`; F'yi paylaşımdan önce değil, her kol başarıyla bitince işaretleyelim; hata kolunda ajansa bildirim.
3. "brand identity" hücresini Türkçe marka kartına çevirelim: 2-3 cümle + ton (samimi/kurumsal) + yasak kelimeler (klinikler için sağlık beyanı/vaat yasakları) + sabit hashtag ve adres/telefon satırı; tüm caption prompt'larına bu kart girsin.
4. Görsel → nötr tarif → platforma özel caption iki aşamalı prompt yapısını (describe → FB caption + FB message / IG caption) Türkçe yazıp şablonlaştıralım; dosya adlandırma rehberi verelim (ör. "lahmacun-yeni-firin.jpg").
5. Instagram için S3 yerine daha basit public barındırma (Cloudinary veya kendi sunucumuz/Supabase storage) kullanalım, bucket'ı yalnız okuma izniyle ve kısıtlı IAM yetkisiyle kuralım.
6. Bu kurulumu "done-for-you" olarak satma modelini alalım: kurulum ücreti + aylık bakım; fiyatı kendi maliyetimize göre belirleyelim (videoda rakam yok).
