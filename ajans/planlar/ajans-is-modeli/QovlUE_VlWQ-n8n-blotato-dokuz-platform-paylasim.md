# This Workflow Auto-Posts to 9 Different Socials (free template) — Nate Herk (meta'da kanal alanı boş; açıklamadaki iletişim bilgisinden) — 10 dk — https://www.youtube.com/watch?v=QovlUE_VlWQ

## Tek paragraf: ne anlatıyor
n8n içinde Blotato'nun "verified community node"u ile tek bir iş akışından 9 platforma (Instagram, TikTok, X, LinkedIn, Facebook, Threads, Bluesky, Pinterest, YouTube) otomatik paylaşım yapmayı gösteriyor. İçerik üretimi bu videonun konusu değil; sadece "yayınlama" kısmı. İçerik kuyruğu bir Google Sheet (başlık, medya, caption, status); akış "ready to post" durumundaki ilk satırı çekiyor, medya Google Drive'daysa dosya ID'sini çıkarıp Blotato'ya yüklüyor, sonra platform node'larıyla paylaşıp satırı "posted" olarak işaretliyor. Eski yöntemde her platform için ayrı HTTP request + JSON body gerekiyordu; native node ile bu ortadan kalkmış. Pinterest board ID bulma hilesi ve hata yönetimi notu da var. Şablon (JSON + Sheet) ücretsiz Skool topluluğunda veriliyor.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
Videoda yok (ajans modeli anlatılmıyor). Kanalın kendi gelir modeli: ücretsiz Skool topluluğu (şablonlar) → ücretli "plus" topluluğu (kurslar: "agent zero" ve "10 hours of 10 seconds", aylık hackathon). Plus fiyatı rakam vermiyor.
- Blotato: "use code Nate30 so you can get 30% off for 6 months" — 6 ay %30 indirim; Blotato'nun liste fiyatı videoda rakam vermiyor.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
Videoda yok. (Kanal kendi kitlesini YouTube → ücretsiz Skool → ücretli Skool hunisiyle topluyor.)

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: n8n (cloud), Blotato (verified community node), Google Sheets, Google Drive. Videodaki örnek içerikler Google Veo 3 ile üretilmiş "Glass Apple ASMR" videoları.

Ön kurulum:
1. n8n admin panel → "verified community nodes" seçeneğini aç; n8n sürümünü güncelle (yoksa Blotato node'u görünmez).
2. Blotato → Settings → sosyal hesapları bağla (TikTok, Instagram vb. "click on the button, log in").
3. Blotato → API → API key kopyala → n8n'de herhangi bir Blotato node'unda yeni credential oluştur; 10 Blotato node'unun hepsinde bu credential seçili olmalı.
4. Her platform node'unda bağlı hesabı listeden seç; platforma özel ayarlar (TikTok: privacy level, yorumları kapat, "is it AI generated" vb.).
5. Skool'dan JSON'u indir → n8n'e import; Google Sheet şablonunun kopyasını al (değişkenler eşleşsin).

Akış (sıralı):
- Trigger: Schedule (ör. her sabah)
- → Google Sheets "Get row(s)": filtre `status = ready to post`, "return only first matching row" (aynı anda 6 post atmamak için)
- → (Medya Drive'daysa) Google Drive dosya ID'sini çıkar (Set/Code)
- → Blotato "Upload media" (Drive URL + file ID şeması; değilse public URL ya da binary) — Blotato videoyu kendi URL'sine kopyalıyor, filigran eklemiyor
- → Blotato "Create post" x 9 (Instagram, TikTok, X, LinkedIn, Facebook, Threads, Bluesky, Pinterest, YouTube): text = başlık + caption, media = Blotato media URL
- → Google Sheets "Update row": status = posted
- Hata kolu: tek bir hata yolu satırı "errored" yapıyor; platform node'ları hata verirse satır güncellenmiyor.

Sheet sütunları: title, media (URL), caption, status (`posted` / `ready to post` / `errored`).

Günde birden fazla post için: "have six different scheduled triggers to go off every 5 minutes at like 6:00 a.m., 6:05, 6:10" — her biri tek satır çeken 6 ayrı zamanlayıcı.

Pinterest board ID: board sayfasında sağ tık → Inspect → Network → sayfayı yenile → filtreye `Boards` yaz → ilk satır → data objesindeki `id` → n8n Pinterest node'unda board alanına yapıştır.

Prompt: Videoda yok (sadece yayınlama akışı, LLM adımı yok).

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: kısa dikey video (ASMR örneği), aynı içerik 9 platforma.
- Sıklık: örnek günde 1 (sabah); 6/gün için 5 dk arayla 6 zamanlayıcı önerisi.
- Önerilen tam sistem: içerik üretim akışı Sheet'i doldurur → bu akış yayınlar → insan sadece "manually review if you're good with it".
- Reklam: Videoda yok.

## Hatalar / uyarılar
- Açıklamada açılabilecek şablon/Doc/GitHub linki yok; şablon JSON ve Google Sheet yalnızca Skool topluluğu içinde (giriş gerekli, talimat gereği atlandı). Kaynak dosyası kaydedilmedi.
- Şablonun hata yönetimi zayıf: "if these nodes error, it won't update" — platform hatasında satır "ready to post" kalır, tekrar paylaşılabilir.
- Bazı platformlar API ile paylaşım için hesap doğrulaması istiyor; Blotato dokümantasyonundaki "social platform requirements" sayfasına bakılmalı.
- Blotato node'u görünmüyorsa n8n sürümü eski veya verified community nodes kapalı.
- Kanal Blotato'dan ücret almadığını söylüyor ama açıklamada affiliate linki var.

## Bizim ajansa alınacaklar (somut)
1. Müşteri başına "İçerik Takvimi Sheet'i" standardı kuralım: sütunlar `tarih | başlık | medya_url | caption | platformlar | status (taslak / onaylı / ready to post / posted / errored)`; müşteri yalnız "onaylı" yapar, n8n sadece "ready to post" ilk satırı çeker.
2. Yayın katmanını Blotato (veya benzeri tek API) ile tek akışta toplayalım; restoran/klinik müşterilerine Instagram + Facebook + TikTok + YouTube Shorts + Google İşletme'yi tek içerikten besleyen "4 platform, tek çekim" paketi satalım.
3. Hata yönetimini şablondan iyi yapalım: her platform node'unda "continue on fail" + hata çıktısını Sheet'e ve ajans WhatsApp/Slack kanalına bildirim; satırı "errored" işaretleyip tekrar paylaşımı engelleyelim.
4. Günde 2-3 post isteyen paket için 5 dk arayla ayrı zamanlayıcılar modelini uygulayalım (öğle menüsü 11:30, akşam 18:00 gibi yerel saatlere göre).
5. Müşteri onboarding kontrol listesine "hesap API doğrulaması (Meta Business, TikTok Business, Pinterest board ID)" adımını ekleyelim; kurulum ücretine bu işi dahil edelim.
6. İçerik üretim akışı (ör. CvQ3ARulOKE / DIXXW_jBS0w planlarındaki caption üreticileri) Sheet'i "taslak" olarak doldursun, bu yayın akışı yalnız onaylı satırları alsın: üretim ve yayın iki ayrı akış olarak kalsın (videodaki "pair this workflow with a different one" önerisi).
