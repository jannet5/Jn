# CLAUDE CODE FULL KURS 3+ SAAT: Kur ve Sat (2026) — burhan kocabıyık — 186 dk — https://www.youtube.com/watch?v=Xu2SIKz8B58
> Kaynak notu: Transkript var (5.208 satır, youtube-transcript-api) — `arastirma/youtube/transkriptler/Xu2SIKz8B58.txt`, baştan sona okundu ([00:00:00]–[03:06:20]). Türkçe otomatik altyazı; tanıma hataları düzeltilerek okundu: "Cloud Code"=Claude Code, "Epify"=Apify, "N8/D8N"=n8n, "Cdream"=Seedream, "fay/F"=fal.ai, "kompozyo"=Composio, "Hatcar"=Hotjar, "Truster/Trustlemer"=TrustMRR, "heyecan"=HeyGen, "Open Cloud/Open Club"=OpenClaw (muhtemel). Güven: **yüksek**. Bölüm başlıkları için `transkriptler/Xu2SIKz8B58.aciklama.txt`; Anthropic dokümanları (Skills, Subagents, MCP, Routines) yalnız referans. Kurs teknik kısmı ~50 dakikaya sıkıştırıp kalanını **iş modelleri, müşteri bulma, vakalar, fiyatlandırma ve yol haritasına** ayırıyor — ajans için en değerli Türkçe kaynak.

## Tek paragraf
İlk 51 dakika Claude Code temelleri: VS Code/Antigravity + terminalde kurulum (eklenti ve masaüstü uygulaması yerine terminal önerisi), CLAUDE.md, Shift+Tab modları, `/btw`, `/compact`, `/clear`, `/schedule`, `/plugin`, beğenilen bir siteyi/uygulamayı (Protipal) klonlama, Skills, ekran görüntüsünden subagent kurma, MCP/connector. Sonrası "sat" tarafı. Ana tez [00:59:29]: **Opsiyon 1, uygulama yap ve abonelikle sat** (ölçeklenebilir, tavsiye edilen); **Opsiyon 2, otomasyonu kur ve müşteriye hazır teslim et** (ajans/proje mantığı). Sıfırdan başlayan biri "ilk 3.000 dolarına kadar" ajans gibi çalışmalı ve B2B'ye satmalı, sonra uygulama/aboneliğe ve B2C'ye geçmeli. Kanallar: B2B için e-posta (kendi verisi: 4 haftada 45.000 kişi %6,4; 6 ayda ~200.000 kişi %6,6), B2C için içerik + reklam. Vakalar: 10 markalı e-ticaret için reklam içeriği/kitle üretimi (yayını n8n yapıyor, analiz Shopify verisiyle, aylık sözleşme); Amazon satıcılarına ürün başı fiyatlı AI ürün görseli (3$/5$/10$, rakip stüdyo 50$); 12 yıllık çiçekçinin 120.000 müşterisini segmentleyip upsell; diş/saç ekimi kliniğine SEO (müşteri edinme maliyeti 2.000$). Müşteri bulma: Apify Google Maps → site incelemesi → LeadFinder → kişiye özel **taslak** Gmail. Operasyon: Routines (private GitHub repo + connector + zamanlama) veya Modal.com'a 7/24 deploy. Fiyat: LTV/CAC, ücretsiz plan yerine **kartlı 7 gün deneme**. Kendi "GOAT" sistemi: 20+ ajan, terminalde 7/24. Kapanışta 0'dan 10–25 bin $/ay'a uzanan bir yol haritası veriyor.

## Önerdiği iş akışı (adım adım)
**A. Kurulum ve temel (00:00–21:00)**
1. VS Code (veya ücretsiz Antigravity) indir → yeni klasör → New Terminal → `claude`. Windows'ta önce Git kur [00:05:55]. Terminal çalışmazsa yedek olarak VS Code eklentisi ya da masaüstü uygulaması. Yine de terminali öneriyor: eklenti "birçok plugini ve yeteneği doğru bir şekilde çalıştıramıyor" ve "çözdüğünü sanıyor" [00:35:48].
2. Plan [00:12:14]: basit site/uygulama için 20$ paket yeter (Opus 4.7 ile). Asıl takılınan saatlik limit; "2 saat sonra limitiniz açılır" [00:35:00]. Kendisinde 2 Max hesap var, aynı anda 12 terminal açık çalışıyor.
3. Shift+Tab ile mod seçimi [00:14:27]: auto (sormadan yapar), plan ("başlangıçta hiçbir şey yapma, sadece plan"), accept edits, normal (her adımda onay ister).
4. CLAUDE.md'yi "sistem prompt'unuz" diye tanımlıyor, her proje için ayrı ve "olabildiğince detaylı" yazılmalı [00:13:55]. İlk örnek: Gmail kategorize edip Google Sheet'e yazan sistemin yalnızca MD dosyasını yazdırıyor [00:15:09]. Bunun "N8N otomasyonunun birebir aynısı" olduğunu gösteriyor: gelen kutusu → oku → kategorize et → önem skoru → aksiyon öner → tabloya yaz [00:18:26].

**B. Komutlar ve eklentiler (21:00–27:00)**
- `/btw <soru>`: Claude uzun bir iş yaparken akışı bozmadan "şu anda ne yapıyorsun" diye sormak için [00:24:00].
- `/compact` sohbeti küçültür, `/clear` sıfırdan başlatır; token tasarrufu için düzenli temizle [00:24:39]. Otomatik hafıza açık, elleme.
- `/schedule` ile sürekli çalışan işler kurulur (örnek: "daily brief" her gün 08:00) [00:47:43]. Bilgisayar kapalıysa çalışmaz.
- `/plugin`: 193 eklentisi kurulu (Gmail, GitHub, Playwright, Slack, YouTube, Figma, Canva, Telegram, Supabase); marketplace'ten ekle/çıkar [00:25:30].
- `/model` ile aktif modeli görme (Opus 4.7) [01:34:15]; `/mcp` ile connector doğrulama [01:51:26].

**C. Sıfırdan web uygulaması: tutan bir ürünü klonla (27:00–37:40)**
1. Yeni klasör, plan modu. Prompt: "Sana vereceğim uygulamaya bakacaksın ve incele. Sadece bir plan yapacağız." [00:29:38]
2. Hedef sitede DevTools → Styles'ı kopyalayıp `tasarım` dosyasına yapıştır; `Cmd+Shift+P` → "Capture full size screenshot" [00:30:47]. İkisini birlikte ver.
3. Claude'un sorularına cevap ver: birebir klon / MVP / sadece landing → "sadece MVP", veri katmanı "şimdilik mock data" [00:33:53]. Sonra auto moda al.
4. Fikir kaynağı TrustMRR: Stripe ile doğrulanmış gelirleri olan uygulamalara bak ve "neredeyse birebirini kopyala" [00:34:06]. Kural: "Tutan bir şeyi birebir alıyorsunuz. Sonrasında bunu özelleştiriyorsunuz." [00:37:08]

**D. Skills, subagent'lar, MCP (37:40–51:14)**
- Skill = "o işi çok iyi yapan birisi tarafından eğitilmiş bir MD dosyası" [00:38:31]. 500+ skill'lik kataloglardan link kopyalanıp "bunu kur" denir [00:38:57].
- Skill ile subagent farkı [00:48:26]: subagent "çağırdığım", işi aksiyon olarak yapan sistem; skill ajanı yönlendirir ve yalnız gerektiğinde yüklenir. Bu yüzden "100 tane yetenek yazsan da hepsi tek oturumda yüklenmiyor".
- GOAT sistemi [00:41:44]: yönetici, müşteri bulan (scout), müşteri test eden, skorlayan, mail gönderen, tasarım/video/içerik ajanları, YouTube'a yükleyip analitik takip eden ajan, sosyal medya menajeri, reklam menajeri ve CEO ajanı. Panelde "çalışan / onay bekleyen / tamamlanan" görülüyor. Klasörde ajanların yanında API'lar, dokümantasyon, şablonlar ve pazar araştırması raporu duruyor.
- Subagent'ları ekran görüntüsünden kurmak [00:45:33]: "Bu görseldeki ajanları benim için kur. Ajan eşittir otomasyon." Örnek ajanlar: kod eleştiren, testi koşturup raporlayan, lead skorlayan, toplantı briefingi hazırlayan (şirketi araştırır, geçmiş etkileşimleri okur, "7 keşif sorusu üretir") [00:45:11].
- MCP/connector [00:49:39]: Drive, Gmail, Takvim, Shopify ("bütün ürünlerimi çek, analiz et, hepsi için ürün görseli oluştur" [00:50:57]), Apify.

**E. İş modeli seçimi (51:14–1:07:00)**
1. İki opsiyon arasında [00:59:33] fiyat farkı yok, fark çalışma alışkanlığında. Proje mantığında teslim ediyorsan opsiyon 2; ölçek istiyorsan opsiyon 1.
2. B2B (CRM, ERP gibi) e-postayla, B2C içerik ve reklamla büyür; ajans modelinde de e-posta kullanılır [01:02:16].
3. Eski SaaS kalıbı [00:53:00]: önde basit bir arayüz, arkada n8n webhook'u (Dekor Vizyon, işletmelere resepsiyon hizmeti), abonelikle satış. Artık arka tarafı da Claude Code yazıyor.
4. Müşteri büyüklüğüne göre hizmet [01:06:05]: büyük şirketlere veri okuyan/analiz eden verimlilik sistemleri; küçüklere içerik üretimi, form, funnel ve e-posta otomasyonu. Destekledikleri uygulamalar ayda 1.000$ ile 100.000$ arası kazanıyor.

**F. Hazır n8n şablonunu Claude Code'a taşımak (54:22–1:21:00)**
1. n8n template sitesinde "Use for free" → JSON'u kopyala → yeni terminalde plan modunda "Bunun aynısını kurmak istiyorum" [00:54:51].
2. Claude n8n'i nerede çalıştıracağını sorarsa: "N8N istemiyorum. Claude içinde kuracağız." [00:55:49] Ardından "Sonrasında da her gün çalışacak şekilde ayarlayacağız."
3. Neden: şablondaki uzman prompt'ları hazır alırsın. Sıfırdan "en az 3 gününüz giderdi" [01:14:31].
4. UGC video örneği: anahtarlar `.env`'e konur. "Open A'ya ihtiyacın bence yok. Onu kaldır. Claude ve fal sana yeter." [01:08:15] Sonra "Bence Seedream 2 kullan… seslendirmeyi eklemeyi unutma" [01:13:13]. Claude Seedream, Türkçe TTS ve FFmpeg'i ekleyip beklenen maliyeti yazdı [01:18:30]. fal.ai Explorer'dan text-to-video ve reference-to-video modelleri de verildi.
5. İterasyon: "Ses biraz arkadan geliyor. Sesi video ile uyumlu yap" [01:19:49]. Ardından "zoom in ve zoom out'ları da yap ve alt yazı da ekle… birebir bu örnekteki gibi olsun. İyice incele." [01:20:22]
6. Sonuç ekranında yalnız "Senin yapman gerekenler" kısmına bak [01:07:11].

**G. Vakalar (1:09:40–1:21:20)**
- **Reklam sistemi** [01:10:13]: altında 10 marka olan e-ticaret şirketi. Normalde marka başına ~10 içerik üretebiliyor ve test edemiyor. Sistem 1.000 içerik × 100 kitle kombinasyonu üretiyor. Reklam içeriğini ve kitleyi Claude Code oluşturuyor, yayını yalnız n8n yapıyor, Shopify verisiyle sürekli analiz yapılıyor. Anlaşma aylık ve süresiz.
- **Ürün görseli** [01:15:10]: Amazon satıcısı 200 ürünlük liste gönderiyor; kredi mantığı var, uygulama yok ("bir uygulama yapsanız müşteri aslında o uygulamayı kullanmayacak"). Sistem Excel'den çekip üretiyor ve geri yüklüyor. Fiyat 3$ (görsel), 5$ (görsel + 3D), 10$ (görsel + 3D + video); müşteriler en çok 360° görsel istiyor. Pazarı "360 turntable furniture" ajanslarını arayarak buldu; rakip teklifi ürün başı 50$'dı ve ürün büyüdükçe artıyordu. Sistemi Claude Code'la bir günde kurdu [01:16:54].
- **Sosyal içerik / carousel** [01:21:27]: Instagram'da 1 yılda 100.000+ takipçi. Klasör yapısı: `örnekler/` (beğenilen başarılı içerikler), kendi görselleri ve logoları, Instagram/YouTube thumbnail örnekleri, örnek sunum PDF'i, `output/`. Topluluk üyelerinin başarılarını otomatik çekip carousel'e çeviriyor. Video kurgu için Remotion skill'i kullanıyor (altyazı, kırpma, zoom). Prompt [01:26:01]: "Bana bir kaydırmalı içerik oluştur. Konu B2B satış ile alakalı olsun. İnternetten bilgiler de topla. Aynı bizim formatımızda." Bu iş 4 dakika ve ~12.000 token sürdü [01:34:01]. İkinci örnekte sunumu birebir koruyup yalnız metinleri değiştirdi, görseller için GPT Image 2 (fal.ai üzerinden) önerildi [01:40:13].
- **Paylaşım**: Composio önerisi, "hiç başka bir yere para vermeyin" [01:37:32]. Bir kez yetkilendirilen Composio ile 100 video paylaşılıp sonuçları geri çekilerek analiz edilebiliyor. Blotato bir alternatif.
- **Çiçekçi** [00:09:55, 02:13:28]: 12 yıllık, 120.000 müşterilik veri. Hedefler segmentasyon, daha iyi reklam ve upsell ("çiçek alana çikolata"). Kullandıkları hazır uygulama yok.

**H. B2B müşteri bulma sistemi (1:40:52–2:02:00)**
1. `business/müşteri-bulma-sistemi` klasörü → `claude` → tek uzun prompt (aşağıda).
2. Apify konsolunda API anahtarı oluşturulur ve ekranda göründüğü için hemen silinip yenilenir [01:44:15]. Apify Store'dan Google Maps scraper linki verilir. "Kendisi de bulabilir ama ben her zaman için yönlendirmeyi seviyorum." [01:44:09]
3. Claude Anthropic API anahtarı istediğinde: "Antropike ihtiyaç yok. Çünkü ben sistemin CLI'da lokalimde çalışmasını istiyorum. Schedule mantığında çalıştıracağım." [01:48:54]
4. Gmail app password çalışmadı → Claude.ai Settings → Connectors → Gmail, terminalde `/mcp` → claude.ai Gmail → authenticate [01:50:05]. Ek talimat: "Emailleri kendin normal CLI'daki yani terminaldeki Claude ile yaz. Her şeyi lokalde çalıştıracağız." Böylece ekstra API kullanılmıyor, abonelik yetiyor. Bulutta çalışacaksa Anthropic API anahtarı gerekir [01:51:15].
5. Claude bir UI kurmaya çalışınca bunu geçip doğrudan sohbetten istemek: "bana 20 tane lojistik firması bul. İstanbul'da olan" → "20 tanesi için yap. Sonrasında mailde taslak olarak bırak." [01:55:04] 11 lead'in e-postası bulunamadı.
6. Güçlendirme: "lead research assistant" skill'i (ideal müşteri profiline göre tarar) [01:58:25] ve LinkedIn'den lead çekip şirketi araştıran ve kişiye özel mail yazan bir n8n şablonunun JSON'u eklenir: "buna göre sistemi geliştir" [01:59:33].
7. Hacim gerekince Instantly [01:47:06]: "30-40 tane mail gönderdiğinizde problem yok" ama dönüş az olur. Kendi verisi: 4 haftada 79.000 mail, 45.000 kişi, ~%6.
8. Ek fikirler [02:01:23]: Apify Instagram scraper ile rakibin son 1 aydaki 100 içeriğini transkriptiyle çekip benzerlerini üretmek; bir videoyu 20 dile çevirmek; sistemi "uygulamaya çevir" deyip abonelik eklemek.

**I. 7/24 çalıştırma (2:02:18–2:12:38)**
- **Routines**: "Bunu private bir proje olarak GitHub'a yükle" → Routine formunda isim ("müşteri bulma otomasyonu"), zamanlama ("her gün saat 9:00" ya da "her 30 dakikada bir"), connector (Gmail), talimat ve repo doldurulur; "Run now" ile test edilir. Bulutta çalışır, lokal klasörle de kurulabilir. "Routine eşittir n8n otomasyonu gibi" [02:04:38].
- **Modal.com** [02:05:25]: Modal API token'ı ver ve "modal.com'a bu sistemi 7/24 çalışacak şekilde deploy etmeni istiyorum" de. İlk girişte ücretsiz kredi veriyor. "VPS'e n8n kurmak gibi" düşünülebilir; API kurmak için de iyi. Bilgisayarda çalıştırmanın handikapı: "Bilgisayarı diyelim ki kapattım… sistem gitti." Müşteri sistemleri bu yüzden Modal'a.
- **n8n'e çevirmek** [02:11:51]: n8n MCP'yi ver → "bu projeyi N8N içerisinde otomasyon olarak kurmanı istiyorum". Claude aynı işi hedef platforma göre kurabiliyor.

**J. Müşteri projelerinde çalışma yöntemi (2:12:59–2:15:20)**
1. Müşteriye üç şey sorulur: veri, kullandığı uygulamalar, hedefler.
2. Uygulamalar ve veri Claude Code'a bağlanır, Claude veriyi analiz eder. Kendi skill kütüphanesi de eklenir.
3. Hedefle sınırla: "senin hedeflerin bunlar. Bu hedefler için yapman gerekenleri planlayacaksın. Bunun dışında bir şey yapmayacaksın." [02:14:33]
4. Proje usulü çalışılır: belirli hedef ve belirli süre. Kendi ürünlerinde "ayda bir tane uygulama çıkartmayı hedefliyoruz" [02:15:16].

**K. B2C uygulama + fiyatlandırma (2:17:03–2:37:54)**
1. UGC motoru tek prompt'la (aşağıda): viral içerik kütüphanesi + seçilen videonun benzerini üretme. Apify TikTok scraper ile gerçek veri çekildi. Arayüzün "AI slop değil" olduğunu söylüyor [02:32:54]. Ardından pricing sayfası istendi: "7 gün ücretsiz istiyorum ama sadece görüntü olsun şimdilik" [02:34:12].
2. LTV/CAC [02:23:29]. Örnek rakip: toplam 254.000$, ayda 7.000$ gelir, planlar ücretsiz/19$/104$, kâr marjı %5. Hesap: aylık 19$ ve %25 churn ile müşteri ~4 ay kalır, yani ~80$. Müşterilerin bir kısmı 99$'lık planı aldığı için LTV ~120$ olur. Reklamla CAC 50$; organik içerik eklenince ~40$'a düşer.
3. Kanal sırası [02:27:54]: organik içerik (önce TikTok, sonra blog) → tutanları reklama çıkar. B2B'de e-posta. Instagram DM "çok değerli olmayacaktır".
4. Trafik geldikten sonra Hotjar benzeri araçla tıklama ve oturum kayıtlarını izle (ör. ödeme sayfasında takılma) → düzelt → müşterinin değer almasını sağla → para kazanırsın [02:30:40]. ARSPAR'da "günde 3.000 kişiyi takip ediyorduk" [02:36:22].
5. "Mükemmel bir fikriniz olmak zorunda değil. Ne kadar hızlı test ediyorum, ne kadar hızlı fark ediyorum, ne kadar hızlı düzeltiyorum." [02:31:58]

**L. En çok kullandığı skill/repolar ve SEO vakası (2:38:11–2:52:20)**: aşağıda "Araçlar". SEO vakası [02:46:47]: yurt dışından hasta çeken diş + saç ekimi kliniği. Müşteri başı edinme maliyeti 2.000$, reklama ayda 100.000$'a kadar harcıyor. Kurulan sistem: tüm rakiplerin bloglarını çek → hangi yazının müşteri getirdiğini analiz et → n8n'de bulunan SEO otomasyonunu kur → yeni blogları üret ve yayınlayan sistemi de kur. Müşteri ilk kez trafiğin nereden geldiğini gördü. SEO'dan bir müşteri gelince ortalama CAC ~1.000$'a iniyor.

**M. Yol haritası (2:52:35–3:00:50)**
- 1. hafta: Claude Code'u öğren.
- 2. ay: ilk otomasyonu şirketlere sat ("Sermaye yok… bilgi satacaksınız"). Satılan şey 7/24 çalışan, problem çözen bir sistem, basit bir arayüzü olabilir.
- İlk satış tipik olarak ~500$ (500–1.200$), bazen ek olarak aylık 100$.
- 6. ay: 5–15 müşteri, ayda ~2.500–7.500$ (tek seferlik + aylık).
- 6–12. ay: ekip, reklam, B2C'ye geçiş. Seviyeler: 0 → 1.000–3.000$ → 5.000$ → 10–25 bin $/ay. "Buradan buraya geçmeniz 6-7 ay sürer minimum."
- E-posta dönüş oranı ölçeği [02:57:12]: "%7 ile %12 arasında bir dönüş oranı mükemmel. %3 ile %7 arası vasat, %3'ün altı kötüdür."

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- CLAUDE.md'ye konacaklar [00:16:42]: şirket/proje pozisyonu, hedef kullanıcı, mesaj tonu ve dil, "daha öncesinde cevaplandırdığım maillerdeki gibi konuş", kullanılabilir araçlar (MCP'ler, n8n, subagent), veri formatı, karar verme kuralları, hata davranışları, output şablonları (ör. mail formatları).
- Kurucu için CLAUDE.md örneği [00:20:00]: "Türkçe konuş. İşte çok kısa bir şekilde devam et. Plan moddan başla. Küçük adımlar al." Ajans operatörü için: müşteri çıkarma, faturalandırma ve iletişim stili yazılır.
- İlk MD prompt'u [00:15:12]: "Gmail'ime erişim sağlayacaksın ve gelen bütün mailleri kategorize etmeni istiyorum… Ancak senin görevin sadece bir MD dosyası oluşturmak."
- Başlangıç kuralı [00:21:53]: "Sıfırdan başlayan birisi için N8N içerisindeki popüler olan şablonları alın. Birebir bunun aynısını Claude Code'a verin ve kurmasını isteyin."
- İş modeli [00:59:33]: "Opsiyon bir uygulama yap. Abonelikle sat… Opsiyon 2. Otomasyonu kur. Onu müşteriye hazır şekilde ver." / [01:05:50] "kendinizi ajans olarak konumlandırabilirsiniz ilk 3.000 dolarınıza kadar".
- Müşteri bulma prompt'u [01:41:38], kısaltılmış: "Apify'dan hem Google Maps'ten müşterileri çıkartacak… kişilerin şirketlerini inceleyecek. Şirketlere özel mailler çıkartacak ve müşterilerin ihtiyacı olabilecek bir çözümü de mailin içerisine koyacak… mailleri başlangıçta göndermeden taslak olarak koyacak… Apify'da maksimum 50 tane müşteri çıkartacaksın. 50 tane müşteri çıkarttıktan sonra dur… emaillerini LeadFinder'dan bul."
- UGC motoru prompt'u [02:18:26], kısaltılmış: "Mock data kullan… İki arayüz istiyorum senden. Birincisi viral içerikleri bulup onu gösterecek. İkincisi… onun videosunu oluşturacak… promptu göstereceksin. Kişi promptu editleyebilecek… Başka herhangi bir şeye odaklanma… giriş olmasın… Landing page'in frontend design'ı kullan."
- Müşteri projesi talimatı [02:14:33]: "Bu hedefler için yapman gerekenleri planlayacaksın. Bunun dışında bir şey yapmayacaksın."
- Reklam vakası [01:11:34]: "Reklam içeriklerini Claude Code oluşturuyor. Reklam kitlesini Claude Code kendisi belirliyor… Sadece paylaşacak sistemi için n8n kurduk." Sonuç: "10 ayda, 12 ayda deneyeceği bir şeyi bir ayda denemiş oluyor."
- Ürün görseli [01:15:59]: "3 dolar olana… görseller var sadece. 5 dolarda görsel + 3D model var. 10 dolar olanda… bir de ürün videosu." / "Müşteriler 360 görsellerini istiyorlar."
- Fiyat [02:24:36]: "Ücretsiz tamamen ücretsiz kullanmayı tavsiye etmiyorum." / [02:29:52] "7 gün ücretsiz ama kartını girmesi gerekiyor… Optin olması lazım." 7 günün sonunda "%30'undan, %40'ından ödeme almaya devam edersiniz".
- Kâr [02:26:37]: "ne kadar kazanıyorum değil, ne kadar cebime giriyor bakmanız lazım."
- Yönlendirme [01:44:09]: "Kendisi de bulabilir ama ben her zaman için yönlendirmeyi seviyorum."
- Site klonlama [02:43:33]: DevTools stilleri + `Cmd+Shift+P` "full screenshot" + URL → "bu yeteneği [frontend-design] kullanarak oluştur". Kapsamlı skill/plugin kullanımı: link ver → "Önce bunu kur" → sitenin konusunu söyle → "Bu da rakibim" [02:42:59].
- Değer ölçütü [01:34:33]: "Claude Code'a ne eklediğiniz önemli değil. Claude Code'dan ne çıkarttığınız önemli."
- Kursun dosya paketi: https://benburhan.gumroad.com/l/bsrxgi ve https://benburhan.gumroad.com/l/pjsiw (açıklamadan).

## Araçlar ve linkler
- **Ortam**: Claude Code (terminal; Opus 4.7; Pro 20$ / Max), VS Code, Antigravity (ücretsiz, kendi ajanı limitli), Cursor/Codex alternatifleri. ChatGPT'yi yalnız sesi yazıya çevirmek için kullanıyor [01:43:44].
- **Claude Code özellikleri**: CLAUDE.md, Shift+Tab modları, `/btw`, `/compact`, `/clear`, `/schedule`, `/plugin`, `/model`, `/mcp`, Skills, Subagents, Connectors (Gmail, Drive, Takvim, Shopify, Apify), Routines.
- **Veri/lead**: Apify (Google Maps, TikTok, Instagram, YouTube transkript, e-ticaret ve emtia scraper'ları), LeadFinder, Instantly (toplu e-posta), LinkedIn n8n şablonu, "lead research assistant" skill.
- **İçerik/medya**: fal.ai (Explorer; Seedream 2, GPT Image 2, text/reference-to-video), Nano Banana + Veo 3 (n8n UGC şablonunda), HeyGen, TTS + FFmpeg, Remotion skill.
- **Yayın/analiz**: Composio (sosyal paylaşım + metrik çekme), Blotato, Hotjar, Shopify verisi, TrustMRR (doğrulanmış gelir listesinden fikir bulma).
- **Deploy**: GitHub (private repo), Claude Routines, Modal.com, n8n (yalnız yayın katmanı veya n8n MCP ile hedef platform).
- **En çok kullandığı skill/repolar** [02:38:11]: Excalidraw skill (sunum ve diyagramları Claude yazıyor, görselleri fal.ai'deki ChatGPT image ile üretiyor); Anthropic'in resmi plugin/örnek reposu; n8n benzeri bir otomasyon görselleştirme repo'su (adı altyazıda "Deffi", doğrulanmalı); "Codex plugin" ve çok ajanlı popüler bir repo (GOAT'un ilk sürümünde kullanmış); OpenClaw benzeri asistan (Mac mini'de abonelikle çalıştırma); **frontend-design** (AI slop'u önlemek için); **security review** komutu; 800 skill + 400 ajanlık katalog sitesi; Hermes Agent (ajanların kendi hatalarından öğrenmesi için, "hype" diyor); Paperclip ("20 dijital çalışan" şirket orkestrası, "popüler olmaması gerektiği halde popüler").
- **Kendi ürünleri ve referanslar**: GOAT (20+ ajan), LeftFlow.ai (ajansı), Protipal (1 yılda 100.000$, TrustMRR'da), ARSPAR (satılmış). İlgili videolar: Skills rehberi (oGI1YmC2L00), %95'ini 30 dk'da (nXvWTR88sfU), 2 saatlik kurs (qm0ZlYezt1Q).

## Hatalar / "bunu yapma" uyarıları
- Claude Code'u VS Code eklentisinde kullanmak: plugin ve skill'ler düzgün çalışmıyor, "çözdüğünü sanıyor" [00:35:55].
- `/clear`'ı kaydetmeden yapmak: konuşma gider. Sohbeti hiç temizlememek de token şişirir [00:24:46].
- AI'ı doğrudan Facebook reklam paneline bağlamak: "reklam panelinde çok fazla harcama olduğu için" bağlamıyor [00:56:10].
- Her bulunan şablonu ve skill'i eklemek: "hedeften sizi uzaklaştıracak… token kullanımınız milyonlara ulaşır". "Faydalı olacaksa" al [02:00:24].
- Lead çekmeyi sınırsız bırakmak → "maksimum 50… sonra dur". E-postaları doğrudan göndermek yerine taslak bırak. 30–40 mail ile anlamlı dönüş beklemek de hata; hacim için Instantly.
- API anahtarını ekranda/sohbette bırakmak → hemen sil/yenile. Anahtarlar `.env`'e girer, `.env` GitHub'a gitmez [01:53:07]. Gereksiz anahtar ekletme ("OpenAI'a ihtiyacın bence yok").
- Lokal sistemi müşteriye teslim etmek: bilgisayar ya da terminal kapanınca sistem durur → Routines veya Modal.
- Tamamen ücretsiz plan: maliyet cebinden çıkar (örnek uygulama %5 kârda) → kartlı 7 gün deneme.
- Ciroya bakmak → kâra bak ("30.000 dolar sattığınızda da cebinize 10.000 dolar kalabilir"). E-ticaret marjı ~%10 → "tutan bir uygulama yapın".
- Müşteriye kullanmayacağı bir uygulama yapmak (Amazon satıcısı örneği) → liste al, sonuç gönder.
- Çok hızlı büyümeye çalışmak: "aşama aşama ilerlemeniz lazım". İlk müşteriden 10.000$ beklemek: "Böyle bir şey yok."
- Instagram DM ile B2B satış: "çok değerli olmayacaktır".
- Gerçek ihtiyaç yerine "havalı mükemmel şeyler" kurmak: "gerçekte ihtiyacınız var mı sorusu önemli" [02:10:31].
- Prompt'u ChatGPT'de düzenletmek: "Apify"yi bozdu, "Metni olduğu gibi ver" [01:43:22].

## Bizim fabrikaya alınacaklar (somut)
1. **Hizmet hattı haritası**: web/app + **reklam sistemi** (Claude Code üretir ve hedefler, n8n/Composio sadece yayın, Shopify/Meta verisiyle aylık analiz; reklam paneline AI doğrudan bağlanmaz) + **ürün görseli** + **sosyal içerik/carousel/UGC** + **SEO blog** → `sistem/` altında her hat için ayrı skill zinciri. Hat seçimi müşteri büyüklüğüne göre: büyüğe veri analizi, küçüğe içerik/form/funnel/e-posta.
2. **Müşteri bulma hattı**: Apify Google Maps (sektör + şehir, **koşu başına ≤50**) → site incelemesi → LeadFinder → kişiye özel sorun+çözüm e-postası (+ demo site linki) → Gmail **taslak** (connector; e-postayı terminaldeki Claude yazar, ekstra API yok). İnsan onaylayıp gönderir. Haftalık **routine** (private repo + Gmail connector). Hacim büyüyünce Instantly. Hedef dönüş metriği: <%3 kötü, %3–7 vasat, %7–12 mükemmel. Burhan'ın %6–6,6'sı referans.
3. **Müşteri kabul formu = 3 soru**: veri ne ve nerede, hangi uygulamalar, hedefler ne → SPEC.md'ye. Alt sohbet talimatı: "Bu hedefler için planla, bunun dışında bir şey yapma." Proje = belirli hedef + belirli süre.
4. **"Liste gönder, sonuç al" ürün paketi**: ürün görseli/360°/video için ürün başı kademeli fiyat (bizde TL karşılığı; rakip stüdyo fiyatının ~1/10'u). Girdi Excel/Sheet, çıktı aynı sheet'e link; uygulama yazmadan hizmet. Pazar keşfi: o hizmeti manuel yapan ajansları bul, fiyat teklifi iste, AI ile 1/10 fiyata kur.
5. **Fiyatlandırma modeli**: ilk aşama proje (ilk satış hedefi ~500$ eşdeğeri + aylık bakım), ölçek aşaması abonelik. `sistem/fiyat/MODEL.md`'de LTV/CAC tablosu (churn → ömür → LTV; reklam CAC'ı ile organik CAC karşılaştırması). Kendi SaaS denemelerinde kartlı 7 gün deneme, tamamen ücretsiz plan yok.
6. **Hazır otomasyonu kopyalama kalıbı**: beğenilen n8n şablonunun JSON'u (içindeki prompt'larla) plan modunda "Bunun aynısını kur, n8n istemiyorum, Claude içinde" diye verilir, sonra uyarlanır. Fikir kaynağı olarak TrustMRR'da gelir doğrulanmış uygulamaların "birebir MVP + mock data" klonu.
7. **Referans tabanlı içerik üretimi**: her içerik skill'inin klasör şablonu `örnekler/` + `marka-görselleri/` + `thumbnail-örnekleri/` + `örnek-sunum.pdf` + `output/`. Çalışma sırası: örnek video/sunum ver → "birebir aynısı" → iterasyon checklist'i (ses senkronu, zoom in/out, altyazı, geçişler). Model seti: fal.ai (Seedream 2 video, GPT Image 2 görsel) + TTS + FFmpeg + Remotion.
8. **Operasyon**: Routines (günlük/30 dk, bulutta; private repo + connector) varsayılan. Müşteriye teslim edilen ve sürekli çalışan servisler Modal.com'a. Müşteri n8n istiyorsa n8n MCP ile aynı mantık n8n'e çevrilir. Lokal `/schedule` yalnız kendi işlerimiz için.
9. **SEO hattı** (diş/saç ekimi vakası): rakip blogları çek → müşteri getiren yazıları analiz et → yeni blog üret ve yayınla → trafik kaynağı raporu. Satış argümanı CAC'ı yarıya indirmek (2.000$ → ~1.000$). Kendi ajans sitemiz için de yerel SEO routine'i.
10. **Skill kataloğu adayları**: frontend-design (AI slop önleme; site klonu = stiller + tam sayfa ekran görüntüsü + URL), security review (teslim öncesi zorunlu adım), Excalidraw (müşteri sunumu/diyagram), lead research assistant, Remotion. Kural: skill yalnız hedefe hizmet ediyorsa eklenir.
11. **Orkestra panosu (GOAT kalıbı)**: tek görevli ajanlar (scout → test → skor → mail → içerik → rapor) ve panoda "çalışan / onay bekleyen / tamamlanan" durumu. Toplantı öncesi brifing ajanı (şirket araştırması + geçmiş etkileşimler + 7 keşif sorusu) satış hattına eklenir.
12. **CLAUDE.md şablonu** (her müşteri projesi): pozisyon, hedef kullanıcı, ton/dil, araçlar, veri formatı, karar kuralları, hata davranışı, çıktı şablonları; üstte "Türkçe, kısa, plan moddan başla, küçük adımlar". Anahtarlar yalnız `.env`'de.
13. **B2C büyüme döngüsü** (kendi ürünlerimizde): organik (TikTok + blog) → tutanı reklama çıkar → Hotjar ile davranış analizi → düzelt → fiyat testleri. "Ayda bir uygulama" ritmi bir hedef olarak değerlendirilebilir.
