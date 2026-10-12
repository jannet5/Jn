# n8n Instagram Automation — Full Postiz Workflow Guide 2026 — Automation Stack — 11 dk — https://www.youtube.com/watch?v=9p3levei5Aw

## Tek paragraf: ne anlatıyor
Video, Instagram'ı Meta Graph API ile uğraşmadan **Postiz** (sosyal medya zamanlama aracı) üzerinden n8n'e bağlamayı gösteriyor. Instagram Postiz'e "Add channel" ile bağlanıyor, n8n'e Postiz topluluk düğümü kuruluyor, sonra iş akışı elle değil **n8n'in yerleşik AI asistanına** tek cümlelik prompt'la yaptırılıyor: Form (görsel + caption + tarih) → Postiz'e dosya yükle → Instagram gönderisini zamanla. Credential için Postiz API key, düğüm içinde Postiz channel ID giriliyor. İlk denemede schedule düğümü hata veriyor → AI asistana "neden başarısız, düzelt" deniyor; saat seçeneği olmadığı için ikinci prompt'la "tam saat" alanı ekletiliyor. Asıl yayın hatası n8n'den değil Instagram'dan: **görsel en-boy oranı desteklenmiyor**; uygun oranlı görselle sorun çözülüyor. Ayrıca n8n (GMT+1) ile Postiz (GMT−1) saat dilimi farkı yüzünden gönderilerin farklı saate düştüğü gösteriliyor. Video, açıklamadaki Postiz affiliate linkine yönlendiriyor.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Affiliate içerik: "I'm affiliated with but not sponsored by the tools I feature" — Postiz satın alımından komisyon.
- Hedef kitle (açıklama): "social media managers, creators and automation fans".
- Fiyat: rakam vermiyor (Postiz planı/ücreti söylenmiyor; "best Postiz deal" linki).

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Kanal: YouTube + affiliate link. Mesaj şablonu: Videoda yok.
- Satış vaadi (açıklamadan aynen): "Nothing to type, nothing to expire." — kod yazma ve süresi dolan Meta token derdi yok mesajı.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Postiz (Add channel, Settings → Developers → API key, Calendar → channel ID, bildirimler, "Post now"), n8n (Postiz community node, n8n AI workflow builder/asistan, Form trigger), Instagram.

**A) Postiz tarafı (sırayla)**
1. Postiz'e giriş → sol panelde **Add channel** → **Instagram** → (giriş yapılmamışsa giriş) → **Allow**.
   - Uyarı: Instagram hesabı daha önce başka bir Postiz hesabına bağlıysa önce oradan silinmeli.
2. **Settings → Developers → API key → Copy** (n8n credential için).
3. **Calendar** → kanalı bul → üç nokta → **Copy channel ID**.

**B) n8n kurulumu**
1. Sol üst **+** → New workflow.
2. **+** → "Postiz" ara → **Install node** (community node).
3. AI asistana prompt (aynen, transkriptteki gibi):
   > "I want to upload an image and captions to post and schedule it to Instagram" + "I downloaded the Postiz node so you can use it"
   Türkçesi: görsel ve açıklama yükleyip Postiz üzerinden Instagram'a zamanlamak istiyorum, Postiz düğümünü kurdum onu kullan. (Asistan arama/düzenleme izni istiyor; onaylamak gerekiyor.)
4. Asistanın istediği: **Connect to Postiz** → API key yapıştır → Save → Continue.
5. "Schedule Instagram post" düğümünde **channel ID** yapıştır (en kritik alan; içerik öğeleri diğer alanlar) → Apply.

**C) AI'ın kurduğu akış — düğüm düğüm (videoda gösterildiği kadarıyla)**
1. **Form Trigger** — alanlar: görsel dosyası (upload), caption, schedule date (+ ikinci prompt sonrası **exact time**, 24 saat formatı "22:00").
2. **Postiz — Upload file** — formdaki görseli Postiz'e yükler.
3. **Postiz — Schedule Instagram post** — channel ID + caption + yüklenen görsel + tarih/saat.
   (Düğüm adları ekranda okunmuyor; akış "upload an image and caption from a form → upload the file to Postiz → schedule the Instagram post" olarak tarif ediliyor.)
4. Publish → ad ("Postiz schedule") → Got it → Execute workflow ile test.

**D) Hata düzeltme prompt'ları (aynen)**
- Schedule düğümü hata verdiğinde AI ikonuna: "the execution failed. Look into it and see what's wrong to help me fix it." — AI hatayı inceleyip düzeltiyor.
- Saat ekleme: "I want to choose ... an exact time instead of only date" — AI forma tam saat alanı ekliyor. (Sadece tarihle gönderi gece 01:00'e düşüyordu.)

**E) Tetik alternatifleri (videoda önerilen)**
- Form yerine Google Drive vb. bağlanabilir; başa manuel tetik, belirli tarih ya da başka bir olayla tetikleme eklenebilir.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: tek görsel + caption Instagram gönderisi. Sıklık: rakam vermiyor. Reklam/bütçe: Videoda yok.
- Postiz'de "Post now" ile anında paylaşım; zamanlanmış gönderi "2-3 dakika" içinde yayınlanıp bildirim + link geliyor.

## Hatalar / uyarılar
- **Instagram en-boy oranı hatası**: "the aspect ratio is not supported on Instagram" — hata n8n'de değil, Postiz bildirimlerinde görünüyor. Görseller Instagram'ın kabul ettiği oranda olmalı (4:5, 1:1, 1.91:1 aralığı — bu oranlar videoda söylenmiyor, Instagram kuralı).
- **Saat dilimi farkı**: n8n GMT+1, Postiz GMT−1 → 22:00 seçilen gönderi farklı saate düştü; ikisinin saat dilimi eşitlenmeli.
- Sadece tarih alanı varken saat 01:00'e zamanlandı → saat alanı şart.
- Bir Instagram hesabı aynı anda tek Postiz hesabına bağlanabiliyor.
- n8n AI asistanı izin istiyor; gözetimsiz bırakılmamalı, ilk çıktı hatalı olabiliyor.
- Açıklamadaki tek link (bit.ly Postiz deal) affiliate — talimat gereği açılmadı; şablon/workflow linki yok.

## Bizim ajansa alınacaklar (somut)
1. **Postiz'i (açık kaynak, kendi sunucumuza kurulabilir) çok müşterili yayın katmanı olarak değerlendirelim**: her müşterinin Instagram/Facebook/TikTok'u Postiz'de kanal; n8n sadece içerik üretir ve Postiz'e iletir — Meta token yenileme derdi Postiz'e geçer ("nothing to expire").
2. **Müşteri onay formu**: n8n Form Trigger (görsel + caption + tarih + saat) → Postiz'e zamanla. Restoran sahibi/sosyal medya sorumlumuz telefondan form doldurarak gönderi planlar; Sheets kuyruğuna alternatif hızlı giriş.
3. **Ön kontrol düğümü ekleyelim**: Postiz'e göndermeden önce görsel oranını kontrol eden (1080x1350 / 1080x1080) ve gerekirse kırpan bir adım — videodaki asıl hata buydu.
4. **Saat dilimi standardı**: n8n, Postiz ve Sheets hepsi Europe/Istanbul; kurulum kontrol listesine madde.
5. **n8n AI asistanını iç hız aracı olarak kullan**: yeni müşteri akışı ilk taslağını prompt'la çıkar, hatada "Look into it and see what's wrong to help me fix it" ile düzelt; ama ajans şablonunu her zaman elle gözden geçir.
