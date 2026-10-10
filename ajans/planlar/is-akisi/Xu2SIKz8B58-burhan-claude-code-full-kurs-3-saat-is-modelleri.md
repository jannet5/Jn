# CLAUDE CODE FULL KURS 3+ SAAT: Kur ve Sat (2026) — burhan kocabıyık — 186 dk — https://www.youtube.com/watch?v=Xu2SIKz8B58
> Kaynak notu: Transkript: var (youtube-transcript-api — yt-dlp değil, 2026-10-10) — `arastirma/youtube/transkriptler/Xu2SIKz8B58.txt` (Türkçe otomatik altyazı, 5.200 satır; "Cloud Code"=Claude Code, "Epify"=Apify, "N8"=n8n gibi tanıma hataları var). İş/satış bölümleri (51:14–2:50) transkriptten okundu; teknik ilk 50 dk ve son ~15 dk (ajan sistemleri, yol haritası) taranmadı → bölüm başlıkları açıklamadan (`transkriptler/Xu2SIKz8B58.aciklama.txt`). Güven: **orta-yüksek**. Kurs, 2 saatlik kursun (qm0ZlYezt1Q) teknik kısmını ~50 dakikaya sıkıştırıp kalanını **iş modelleri, müşteri bulma, vaka çalışmaları ve fiyatlandırmaya** ayırıyor — ajans için en değerli Türkçe kaynak.

## Tek paragraf
İlk 50 dakika Claude Code temelleri (kurulum, CLAUDE.md, komutlar/plugin'ler, web uygulaması, Skills, sub-agent, MCP); sonrası "sat" tarafı. Ana tez: iki opsiyon var — **(1) uygulama yap, abonelikle sat** (ölçeklenebilir, tavsiye edilen) ya da **(2) otomasyonu kur, müşteriye hazır teslim et** (ajans/proje mantığı, "nasıl birisine web site yapıyorsanız birebir aynısı"). Önerilen geçiş: "ilk 3.000 dolarınıza kadar" ajans ol, sonra uygulamaya/aboneliğe dön. Büyüme kanalı: **B2B → e-posta** (kendi gönderimleri: 4 haftada 45.000 kişi %6,4; 6 ayda ~200.000 kişi %6,6 dönüş), **B2C → içerik + reklam**. Vakalar: 10 markalı e-ticaret için Claude Code ile reklam içeriği + kitle üretimi, n8n sadece paylaşım, Shopify verisiyle analiz (aylık sözleşme); Amazon satıcıları için ürün başı fiyatlı AI ürün görseli hizmeti (3$/5$/10$, rakip ajans 50$); 12 yıllık çiçekçinin 120.000 müşterilik verisiyle segmentasyon + upsell. Müşteri bulma: Apify Google Maps → site inceleme → kişiye özel teklifli **taslak** e-postalar (Gmail). Operasyon: Routines ile bulutta günlük çalıştırma, Modal.com'a 7/24 deploy. Fiyat: LTV/CAC; ücretsiz plan yerine **kartlı 7 gün deneme**. Kendi "GOAT" sistemi: 20+ ajan 7/24 (müşteri bulma, sunum, çözüm, carousel/YouTube/thumbnail).

## Önerdiği iş akışı (adım adım)
**A. Teknik temel (0:00–51:14)**: kurulum → CLAUDE.md + ilk proje → komutlar/plugin/bağlantılar → sıfırdan web uygulaması → Skills ve ajan sistemi → sub-agent → MCP/connector. (2 saatlik kursla aynı iskelet; bkz. o plan.)
**B. İş modeli ve hizmet hatları (51:14–1:40:52)**:
1. Opsiyon seç (59:29): uygulama+abonelik (B2B veya B2C) ya da otomasyon teslimi (ajans). Yeni başlayan ve aylık <3.000$ ise: "çok basit bir uygulama yapmak ve bir uygulamayı kopyalamak".
2. Kanal: B2B → e-posta; B2C → içerik ve reklam ("B2C uygulamalar daha büyür").
3. Hazır sistemi kopyala: beğendiğin n8n otomasyonunu (içindeki prompt'larla) Claude Code'a ver, "birebir aynısını" kurdur — sıfırdan "en az 3 gününüz giderdi".
4. Hizmet hatları: UGC video (1:04:46; referans videoyu ver, "birebir aynısı", sonra "ses biraz arkadan geliyor ... uyumlu yap", "zoom in/out ve alt yazı ekle" ile iterasyon), e-ticaret reklam sistemi (1:10:02), ürün görseli (1:12:28), sosyal içerik (1:21:15), YouTube + carousel (1:27:12; mevcut sunumu birebir tutup yazıları değiştirme, GPT Image 2 ile görsel).
**C. Müşteri bulma (1:40:52–2:02:06)**: yeni klasör → Claude'a tek uzun prompt: Apify ile Google Maps'ten işletme çek (**maks. 50, sonra dur**) → web sitelerini incele → e-postaları LeadFinder'dan bul → şirkete özel, ihtiyaç duyabileceği çözümü içeren e-posta yaz → Gmail'e **taslak** olarak koy (gönderimi insan yapar).
**D. Operasyon (2:02:06–2:17:32)**: projeyi private GitHub repo'suna yükle → Routines'te repo + connector (Gmail) + talimat + zamanlama ("her gün saat 9:00", "her 30 dakikada bir") → bulutta çalışır; alternatif Modal.com'a 7/24 deploy. Müşteri projesi yöntemi (2:12:38): **veri, kullandıkları uygulamalar, hedefler** sorulur → uygulamaları/veri Claude Code'a bağlanır → hedeflerle sınırlanır → plan → sistem. Proje usulü: belli hedef + belli süre.
**E. Ürün ve fiyat (2:17:32–2:37:54)**: B2C UGC motoru; LTV/CAC (2:23:08): churn %25 → ~4 ay → ~80$, karışık paketlerle LTV ~120$; reklam CAC 50$ → organik içerikle ~40$'a çek; "ne kadar kazanıyorum değil, ne kadar cebime giriyor". Organik (TikTok, blog) → tutanı reklama çıkar. Trafik sonrası Hotjar benzeri araçla davranış analizi → düzelt.
**F. Araç kutusu ve yol haritası (2:37:54–son)**: en çok kullandığı skill'ler (Excalidraw, Anthropic resmi skill/plugin reposu, frontend-design, security review, büyük skill/ajan katalogları); SEO ile müşteri bulma vakası (2:43:17, yurt dışından müşteri çekmek isteyen müşteri); ajan sistemleri GOAT/Hermes/Paper Clip; yol haritası.

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- İş modeli: "Opsiyon bir uygulama yap. Abonelikle sat. ... Opsiyon 2. Otomasyonu kur. Onu müşteriye hazır şekilde ver." / "ölçeklenebilir bir iş modeli kurmak istiyorsanız opsiyon 1 olmalı" / "kendinizi ajans olarak konumlandırabilirsiniz ilk 3.000 dolarınıza kadar".
- Müşteri bulma prompt'u (aktarım, kısaltılmış): "Epify'dan hem Google Maps'ten müşterileri çıkartacak ... kişilerin şirketlerini inceleyecek. Şirketlere özel mailler çıkartacak ve müşterilerin ihtiyacı olabilecek bir çözümü de mailin içerisine koyacak ... mailleri başlangıçta göndermeden taslak olarak koyacak ... Epify'de maksimum 50 tane müşteri çıkartacaksın. 50 tane müşteri çıkarttıktan sonra dur."
- Müşteri projesi: "datalarını soruyorum. Kullandıkları uygulamaları soruyorum ve hedeflerini soruyorum." → "senin hedeflerin bunlar. Bu hedefler için yapman gerekenleri planlayacaksın. Bunun dışında bir şey yapmayacaksın."
- Ürün görseli fiyatı: "3 dolar olana ... görseller var sadece. 5 dolarda görsel + 3D model var. 10 dolar olanda görsel ... bir de ürün videosu"; müşteri listeyi gönderir, sistem Excel'den çeker-üretir-yükler; "Müşteriler 360 görsellerini istiyorlar."
- Reklam vakası: "Reklam içeriklerini cloud kode oluşturuyor. Reklam kitlesini cloud code kendisi belirliyor ... Sadece paylaşacak sistemi için D8N [n8n] kurduk" → "10 ayda, 12 ayda deneyeceği bir şeyi bir ayda denemiş oluyor."
- Fiyat: "Ücretsiz tamamen ücretsiz kullanmayı tavsiye etmiyorum" → "7 gün ücretsiz ama kartını girmesi gerekiyor ... Optin olması lazım."
- Rehberlik: "Kendisi de bulabilir ama ben her zaman için yönlendirmeyi seviyorum." / sonuç ekranında "Senin yapman gerekenler. Bu kısma bakmanız gerek yeterli."
- Site klonlama: DevTools'tan stilleri kopyala + `Cmd+Shift+P` → "full screenshot" → ikisini ver → "frontend design yeteneğini kullanarak oluştur".
- Kursun dosya paketi: https://benburhan.gumroad.com/l/bsrxgi ve https://benburhan.gumroad.com/l/pjsiw.

## Araçlar ve linkler
- Claude Code (Skills, Sub-agents, MCP/connectors, Routines, Plugins), n8n (yalnız yayın/paylaşım katmanı), Apify (Google Maps, TikTok/Instagram/YouTube scraper), LeadFinder (e-posta bulma), Gmail connector, Modal.com, GitHub, fal.ai + Seedream ("Cdream") + TTS + FFmpeg (UGC video), GPT Image 2 (görsel), Shopify verisi, Hotjar benzeri analiz, Excalidraw skill, frontend-design skill, security review; ajan çerçeveleri: GOAT (kendi), Hermes, Paper Clip, OpenClaw (adlar altyazıdan, doğrulanmalı)
- İlgili videoları: Skills rehberi (oGI1YmC2L00), %95'ini 30 dk'da (nXvWTR88sfU), 2 saatlik kurs (qm0ZlYezt1Q)

## Hatalar / "bunu yapma" uyarıları
- Çok hızlı büyümeye çalışmak: "aşama aşama ilerlemeniz lazım" (önce ajans, sonra ürün).
- Tamamen ücretsiz plan → maliyet cebinden çıkar, örnek uygulama %5 kârda; kartlı deneme kullan.
- Ciroya bakmak → kâra bak ("30.000 dolar sattığınızda da cebinize 10.000 dolar kalabilir").
- E-ticaret marjı düşük (~%10) → "tutan bir uygulama yapın".
- Müşteriye kullanmayacağı uygulama yapmak (Amazon satıcısı örneği) → liste al, sonuç gönder (hizmet olarak sat).
- Lead çekmeyi sınırsız bırakmak → "maksimum 50 ... sonra dur"; e-postaları doğrudan göndermek yerine taslak.
- API anahtarını ekranda/sohbette açık bırakmak → hemen sil/yenile; gereksiz anahtar ekletme ("Open A'ya ihtiyacın bence yok").
- Instagram DM ile B2B satış: "çok değerli olmayacaktır".

## Bizim fabrikaya alınacaklar (somut)
1. **Hizmet hattı haritası** bu kursla birebir örtüşüyor: web/app + **reklam sistemi** (Claude Code üretir/hedefler, n8n sadece yayın, Shopify/Meta verisiyle aylık analiz) + **ürün görseli** + **sosyal içerik** + **carousel/YouTube** → `sistem/` altında her hat için ayrı skill zinciri.
2. **Müşteri bulma hattı**: Apify Google Maps (sektör+şehir, **koşu başına ≤50**) → site incelemesi → kişiye özel sorun+çözüm e-postası (+ demo site linki) → Gmail **taslak**; insan onaylayıp gönderir; haftalık **routine** (private repo + Gmail connector). Hedef metrik: Burhan'ın %6-6,6 dönüş oranı referans.
3. **Müşteri kabul formu = 3 soru**: veri ne/nerede, hangi uygulamalar, hedefler ne → SPEC.md'ye; alt sohbet talimatı "sadece bu hedefler için planla, dışına çıkma".
4. **"Liste gönder, sonuç al" ürün paketi**: ürün görseli/360/video için ürün başı kademeli fiyat (bizde TL karşılığı; rakip stüdyo fiyatının ~1/10'u); girdi Excel/Sheet, çıktı aynı sheet'e link — uygulama yazmadan hizmet.
5. **Fiyatlandırma modeli**: ilk aşama proje (kurulum ücreti + aylık bakım/reklam yönetimi), ölçek aşaması abonelik; LTV/CAC tablosu `sistem/fiyat/MODEL.md`; kendi SaaS denemelerinde kartlı 7 gün deneme.
6. **Hazır otomasyonu kopyalama kalıbı**: beğenilen n8n/şablon akışı + içindeki prompt'lar Claude Code'a verilir, "birebir kur, sonra uyarla" — sıfırdan prompt aramak yerine.
7. **Referans-tabanlı içerik üretimi**: UGC/carousel için örnek video/sunum ver → "birebir aynısı" → iterasyon notları (ses senkronu, zoom, altyazı) skill'de checklist olur.
8. **Operasyon**: Routines (günlük/saatlik, bulutta) varsayılan; uzun süreli servisler Modal.com.
9. **SEO ile müşteri bulma** (2:43:17): kendi ajans sitemiz için yerel SEO içerik routine'i. Skill kataloğu karşılaştırması: frontend-design (AI-slop önleme), security review, Excalidraw (müşteri sunumu/diyagram) bizim kataloğa adaydır.
