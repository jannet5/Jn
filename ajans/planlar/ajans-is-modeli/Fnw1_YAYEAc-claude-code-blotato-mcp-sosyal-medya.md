# Claude Code Runs My Social Media Now (Full Setup) — Zubair Trabzada | AI Workshop — 24 dk — https://www.youtube.com/watch?v=Fnw1_YAYEAc

## Tek paragraf: ne anlatıyor
Zubair, VS Code içindeki Claude Code eklentisiyle sosyal medya içeriğini "kod yazmadan" üretip yayınlamayı gösteriyor. Boş bir "social media" klasöründe Claude Code'a web araştırması yaptırıp LinkedIn gönderisi yazdırıyor, sonra aynı içeriği LinkedIn / Instagram / Twitter için ayrı `.md` dosyalarına böldürüyor (Instagram dosyasında carousel slayt önerileri de çıkıyor). Ardından Claude Code'un sorduğu çoktan seçmeli sorularla (ton, kitle, bakış açısı, emoji kuralı) **marka sesi** oluşturup `memory.md`'ye kaydettiriyor; gönderileri kısaltıp platform karakter limitlerine uydurtuyor. Sonra **Blotato MCP**'yi Claude Code'a ekliyor; Blotato'nun görsel şablonlarıyla LinkedIn gönderisinden **whiteboard infographic** ürettiriyor ve tek komutla "LinkedIn'e şimdi paylaş, Instagram'ı yarına, X'i Perşembe 26 Şubat'a zamanla" diyor. İlk denemede hesap bağlantıları süresi dolduğu için hata alıyor, Blotato'da yeniden bağlayıp tekrar deneyince gönderiler yayınlanıyor/zamanlanıyor. Kapanışta Skool topluluğunda "AI ajansı kurma" ve "Claude Code ile kur ve sat" kurslarına yönlendiriyor.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Video ücretsiz eğitim; gelir: Skool topluluğu ("Learn how to Make Money with AI", "how to start your AI agency", Claude Code classroom'da "automate with social media" projesi), Blotato affiliate (ücretsiz deneme "free trial for a week"), "Work with us to Implement AI Into Your Business" keşif görüşmesi.
- Açıklama: "✅ How to sell social media automation as a service to real businesses" — ama videoda satış/fiyat kısmı anlatılmıyor.
- Rakamlar: Claude ücretli plan "$20 a month or the max plan" (USD/ay). Hizmet fiyatı: rakam vermiyor.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Kanal: YouTube → Skool topluluğu / keşif görüşmesi linki. Mesaj şablonu: Videoda yok.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Claude (ücretli plan), Visual Studio Code + **Claude Code** eklentisi (Anthropic, "4.9 million downloads"), Blotato (MCP sunucusu, görsel şablonlar, hesap bağlama, takvim). n8n/Make yok; akış tamamen Claude Code sohbeti.

**A) Kurulum**
1. claude.ai'da hesap, ücretli plan ($20/ay veya Max).
2. VS Code indir → Extensions → "Claude Code" (yayıncı Anthropic) → Install → sağ üstte Claude Code ikonu.
3. File → Open Folder → masaüstünde boş "social media" klasörü.
4. Alt sağdaki mod: "Edit automatically" (her düzenlemede sormasın).

**B) İçerik üretimi — prompt'lar sırayla (aynen)**
1. Araştırma + taslak: "research SEO versus GEO and write a thought-provoking LinkedIn post about the future of search" — konu araştırıp düşündürücü LinkedIn gönderisi yazdırır (web arama izni onaylanır).
2. Platform dosyaları: "Create a file in this project for this LinkedIn post and also create a file for Instagram and another one for Twitter." — her platform için ayrı dosya; Instagram dosyasında "Slide 1, Slide 2…" carousel önerisi geliyor (gönderi metni değil, öneri).
3. Marka sesi: "Can you incorporate my brand voice for these posts and for posts moving forward?" — Claude Code çoktan seçmeli sorular soruyor:
   - Ton: bold & direct / casual & conversational / professional & authoritative / witty & provocative → seçilen: **casual and conversational**
   - Kitle: founders & CEOs / marketing professionals / general business / entrepreneurs & creators → **entrepreneurs and creators**
   - Bakış açısı: industry insider / educator & teacher / contrarian thinker → **educator and teacher**
   - Emoji/kurallar: no emojis / minimal emojis / emojis welcome / I have specific rules → **emojis welcome**
   → Cevaplar `memory.md`'ye yazılıyor; üç gönderi yeni sesle yeniden yazılıyor; sonraki tüm gönderiler bu sesi kullanıyor.
4. Kısaltma: "these posts are too long. Make sure they're short and to the point and then also make sure that they abide by all the platforms character limits." — kısa/vurucu hâle getirir ve her platformun karakter sınırını raporlar.

**C) Blotato MCP bağlama**
1. Blotato'da hesap → Settings → **APIs** → **Copy setup command** (komut `claude mcp add --transport ...` + header'da Blotato API key içeriyor).
2. Bu komut terminal içindir; VS Code içindeki Claude Code sohbetine yapıştırırken başına şu talimat ekleniyor (aynen):
   > "add the following MCP server as if you were running Claude MCP add command and add it to user scope .claude.json. Create the file if it doesn't exist."
   Türkçesi: aşağıdaki MCP sunucusunu "claude mcp add" çalıştırılmış gibi kullanıcı kapsamındaki .claude.json'a ekle, dosya yoksa oluştur.
3. `.claude.json` okuma/yazma izinlerini onayla → "Done" → Claude Code'u yeniden başlat (sağ üst ikon → yeni Claude Code paneli).

**D) Görsel üretimi (Blotato şablonu)**
1. LinkedIn dosyası seçiliyken prompt (aynen): "make a whiteboard infographic from the LinkedIn post" → "MCP Blotato visual templates" iznine "yes, allow".
2. Çıktı: "SEO versus GEO, get found versus get cited" whiteboard infographic; Blotato → Videos → generations listesinde de görünüyor.
3. Diğer şablonlar (Blotato → Create new video): **newspaper infographic, TV wall infographic, trail marker infographic, t-shirt** vb. — Claude Code'a "whiteboard yerine newspaper infographic yap" denebiliyor.

**E) Yayın ve zamanlama**
1. Blotato → Settings → Accounts → "Login with Facebook / LinkedIn / …" ile hesapları bağla (aynı tarayıcıda oturum açık olmalı). Bağlı: Instagram, LinkedIn, Twitter (+ açıklamada Facebook, TikTok, Threads, Pinterest, Bluesky, YouTube).
2. Prompt (aynen): "post the LinkedIn post now and schedule the Instagram post for tomorrow and the Twitter or X post for Thursday, February 26th" → MCP iznini "tüm projeler için" onayla.
3. Hata: hesap bağlantıları süresi dolmuş, üç gönderi de başarısız → Blotato'da Reconnect (LinkedIn, Instagram, Twitter).
4. Prompt (aynen): "I reconnected everything. Try again." → LinkedIn hemen yayında (infographic + marka sesli metin), Instagram yarına, X Perşembe'ye zamanlandı.
5. Kontrol: Blotato → **Calendar** → upcoming posts.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türleri: düşünce liderliği metin gönderisi (LinkedIn), Instagram carousel önerisi, X gönderisi, whiteboard/newspaper/TV wall infographic görselleri ("they get a lot of views… a lot of engagement").
- Strateji: tek fikir → platforma özel dosyalar → kısa ve karakter limitine uygun → farklı günlere yayılmış zamanlama.
- Sıklık: rakam vermiyor. Reklam/bütçe: Videoda yok.

## Hatalar / uyarılar
- Sosyal hesap bağlantılarının süresi doluyor → tüm gönderiler başarısız olabiliyor; Blotato'da periyodik "Reconnect" gerekli. (Transkriptte hata mesajı "reconnect your accounts to Buffer" diye geçiyor — muhtemelen Blotato'nun yanlış okunması.)
- Blotato'nun "copy setup command"ı terminal içindir; VS Code sohbetinde ek talimat gerekiyor, sonra Claude Code yeniden başlatılmalı.
- İlk taslaklar çok uzun → kısaltma + karakter limiti talimatı şart.
- "Edit automatically" modu ve "allow MCP for all projects" izinleri hız kazandırır ama müşteri hesaplarında yanlışlıkla yayın riski taşır; onaysız yayın yapılmamalı.
- Tam MCP komutu/URL ekranda okunmuyor; kendi API key'inizle Blotato'dan kopyalanmalı (API key paylaşılmamalı).
- Açıklamadaki linkler: Skool (2 adet, biri bit.ly/aiw-lite-about → skool.com/aiworkshop-lite), Blotato affiliate, keşif görüşmesi — talimat gereği açılmadı; şablon/prompt kaynağı yok (Claude Code komutu Skool classroom'da).
- "How to sell as a service" vaadi açıklamada var, videoda içerik yok.

## Bizim ajansa alınacaklar (somut)
1. **Müşteri başına Claude Code proje klasörü**: `musteriler/<isletme>/` içinde `marka-sesi.md` (memory), `platformlar/instagram.md`, `facebook.md`, `google-isletme.md`. Onboarding'de videodaki 4 soruyu Türkçe ve yerel işletmeye uyarlayıp soralım: Ton (samimi / kurumsal / esprili / premium), Kitle (semt sakinleri / aileler / öğrenciler / turistler), Bakış (usta-şef anlatıcı / uzman doktor / mahalle esnafı), Emoji (yok / az / serbest).
2. **Tek komutla çok platform**: "Bu haftanın kampanyasından Instagram carousel (5 slayt), Facebook gönderisi ve Google İşletme gönderisi dosyası oluştur; karakter limitlerine uy" — standart haftalık prompt'umuz olsun.
3. **Yayın katmanı seçimi**: Blotato MCP (hızlı, çok platform, şablonlu infographic) ile Postiz/kendi Graph API akışımızı karşılaştıralım; küçük müşteriler için Blotato + Claude Code, yüksek hacimde n8n + Graph API. Hesap bağlantısı süresi dolmasın diye haftalık "bağlantı kontrol" adımı.
4. **Infographic formatı** klinik/hizmet işletmeleri için: "whiteboard infographic" tarzında "X mi Y mi?" karşılaştırma görselleri (ör. "İmplant mı köprü mü?", "Lazer mi ağda mı?") — eğitici, kaydedilen içerik.
5. **Onay kapısı**: Claude Code'un doğrudan yayınlaması yerine önce taslak + görseli müşteriye WhatsApp'tan onaya gönder, onaydan sonra "schedule" komutu. Satışta: "İçeriğiniz markanızın sesiyle yazılır, siz onaylamadan hiçbir şey paylaşılmaz."
