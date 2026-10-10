# AJANS İŞ MODELİ — ÖZET (YouTube araştırması: iş modeli + sosyal medya + reklam)

Kaynak: `ajans/arastirma/youtube/index.md` içindeki 13 bölüm (5 mevcut + 8 yeni arama), 152 video sıralandı (41 A, 56 B, 55 C); 105 transkript + 309 açıklama çekildi; bu klasörde 17 video planı var. Rakamlar videolardaki gibi (USD); TR karşılıkları önerimizdir.

---

## 1. Ortak kalıplar (17 planın kesişimi)

1. **Önce yap, sonra sat ("mockup-first").** Mikey No Code, Pavlo (3 video), Abou Toure, Savvy Couple, Self-Made Web Designer, Spencer Reist — hepsi aynı şeyi söylüyor: işletmeye "site ister misiniz?" diye sorma; 10-15 dakikada mockup üret, "sizin için yaptım, bakar mısınız?" de. Mesaj kanalından (DM, SMS, telefon, e-posta, yüz yüze) bağımsız çalışıyor; iddia edilen yanıt oranları %30-40 (Mikey) ile %1-3 (Pavlo, soğuk toplu temas) arasında.
2. **Site ücretsiz/ucuz, sistem aylık.** Satılan şey web sitesi değil: lead yakalama + otomatik takip + yorum yönetimi + sosyal paylaşım + (ileri seviye) AI chat/sesli asistan. "Tek seferlik ücretle satıp gitmek en büyük hata" (Mikey). GHL okulunun formülü: "You're paying for the system that makes money, not just a website."
3. **Niş + şehir.** Lead listesi her zaman "sektör + ilçe/şehir" aramasıyla (Google Maps, Instagram arama, Outscraper/Apify). Hedef profil: iyi yorumları olan (20-100), sitesi olmayan veya eski siteli ev hizmeti işletmeleri (tesisat, elektrik, boya, tadilat, peyzaj, temizlik), sonra klinik/restoran/kuaför.
4. **Değer matematiği ile kapanış.** "Bir müşteri size kaç para?" → aylık ücreti "ayda 1 ekstra müşteri" ile kıyasla. Özellik anlatma, sonuç anlat ("gece 2'de telefondan randevu alır, sabah dolu takvimle uyanırsınız").
5. **Onay kapılı otomasyon.** Tüm içerik otomasyonları (Zapier Agents, Make, n8n) aynı iskelet: kaynak (haber/RSS/Sheet/scraping) → platform başına ayrı LLM promptu → görsel → **insan onayı** (Slack/Telegram/Doc) → yayın. Tam otomatik yayın herkesçe "henüz güvenilir değil" deniyor.
6. **Marka sesi = kısa kural listesi + 3 örnek.** Hashtag/emoji/uzun tire yok; doğrudan konuş; "problem + gerçek + fayda" iskeleti (AI Andy'nin Oasis promptları, Vibe Marketer'ın LinkedIn promptu).
7. **Gerçekçilik.** Zubair Trabzada: tek kişilik AI ajansı tavanı ~10-15 bin $/ay; "300 bin $/ay" videoları yalan. Sam Robinson: 30 müşteri = 4.000 $/ay MRR, 1 yıl 03:30'da kalkıp 3.000 soğuk arama. Nick Saraev (TxMAUMUn-is, karşı görüş): yerel işletmeye satış düşük bilet, yüksek kayıp; ya hacim/sistem kur ya da daha büyük işletmeye git.

## 2. Fiyat aralıkları (videolardan, USD)

| Model | Kaynak | Fiyat |
|---|---|---|
| Temel site + hosting + bakım (abonelik) | Mikey, Sam Robinson, Pavlo, Nick Ponte okulu | **150-200 $/ay**, 0 $ peşin |
| Site + lead takip (form→SMS/e-posta, CRM, yorum yönetimi, sosyal paylaşım) | Mikey, Pavlo (WWCzZ56VAT4, 7Tqs83MjBY8), hA91LtOslDs | **300-500 $/ay** (Pavlo tek paket 297 $/ay) |
| Tam otomasyon (randevu, panel, e-posta/SMS kampanyaları, AI sesli asistan) | Mikey, Pavlo | **1.000-2.000 $/ay** / 500 $/ay |
| Tek seferlik özel site (mockup gösterildikten sonra) | Savvy Couple, Abou Toure, Pavlo GHL | **1.000-5.000 $** (örnekler: 1.500 $, 3.000 $) |
| Yüksek bilet özel tasarım + aylık yazılım | Pavlo (cJvwRc3iK0w, hA91LtOslDs) | **3.000-6.000 $ peşin + 100-300 $/ay** |
| Broşür sitesi, düşük fiyat / yüksek hacim | Self-Made Web Designer | **1.000-2.000 $**, ayda 5 adet |
| Araç maliyeti | GHL 300 $/ay sınırsız alt hesap; Base44 0-160 $/ay; Lovable 25-50 $/ay (+50-200 $ backend); Relume ücretsiz 1 proje; Make/Perplexity API birkaç $ | — |

**TR için önerilen merdiven (önerimiz, test edilecek):**
- **Vitrin:** kurulum 0-5.000 ₺ + **2.500-3.500 ₺/ay** (site, hosting, bakım, Google İşletme Profili).
- **Müşteri Makinesi:** kurulum 5.000-10.000 ₺ + **5.000-7.500 ₺/ay** (+ WhatsApp/form lead yakalama ve 3 adımlı otomatik takip, cevapsız arama geri mesajı, Google yorum isteme + yanıtlama, ayda 12 sosyal medya postu).
- **Büyüme:** kurulum 15.000-30.000 ₺ + **10.000-15.000 ₺/ay** (+ randevu/online sipariş, mobil uygulama, AI chat/sesli asistan, Meta/Google reklam yönetimi; reklam bütçesi hariç).
- **Özel tasarım tek seferlik:** 20.000-50.000 ₺ + Vitrin aboneliği.
- Kullanım bazlı maliyetler (WhatsApp API, SMS, sesli dakika, reklam bütçesi) müşterinin kartına; abonelik marjı sabit kalsın.

## 3. En iyi müşteri bulma yöntemleri + Türkçe mesaj şablonları

**Sıralama (videoların kanıt ağırlığına göre):**
1. **Google Maps taraması + mockup + WhatsApp/telefon** (Mikey, Pavlo, Savvy Couple, Sam Robinson, Spencer Reist).
2. **Instagram "3 beğeni + takip + müşteri sorusu → sesli mesaj pitch"** (Abou Toure; 1.500 $'lık gerçek kapanış).
3. **Yakın çevre / First 100** (Self-Made Web Designer, Sam Robinson: ilk 4 müşteri eşinin sitesinden).
4. **Loom/kısa video denetimi** (Self-Made Web Designer: günde 2-3).
5. **Yüz yüze: çarşı, ticaret odası, co-working atölyesi, "15 dakikada site" tişörtü** (Mikey, SMWD).
6. **Referans ortaklığı** (fotoğrafçı, logo, muhasebeci; %20-30 komisyon).
7. **Google İşletme Profili** ajans için (SMWD: "bölgede ilk siz çıktınız").
8. 20-30 müşteri sonrası küçük Meta/Google reklam bütçesi (Sam Robinson).

**Lead rutini:** her gün "sektör + ilçe" araması → 50 işletme → Sheet (ad, telefon, Instagram, site durumu, yorum sayısı, PageSpeed) → filtre: 20-100 yorum, sitesi yok/eski (çok iyi siteli ve 90'lar görünümlü siteli olanları atla) → mockup → mesaj.

**Şablonlar (TR):**
- *WhatsApp / SMS, sitesi olmayan:* "Merhaba, [İşletme] mi? Google'da yorumlarınız çok iyi ama web sitenizi bulamadım. Ben [Ad], yerel işletmelere Google'da öne çıkaran, müşteri toplayan siteler yapıyorum. Sizin için ücretsiz bir örnek ana sayfa hazırladım; göndermemi ister misiniz?" (link cevap gelince.)
- *WhatsApp, sitesi olan:* "Merhaba [İşletme], sitenizde kolay düzelecek birkaç şey fark ettim (telefonda açılış, WhatsApp butonu yok, eski menü). Daha fazla müşteri getiren yeni bir versiyon hazırladım, bakmak ister misiniz?"
- *Restoran:* "Merhaba [Restoran], menünüz Instagram'da ama telefondan sipariş/rezervasyon alınamıyor. Müşterilerin telefondan 30 saniyede sipariş verdiği bir sayfa hazırladım, göstereyim mi?"
- *Klinik/kuaför:* "Merhaba [Klinik], randevu için herkes aramak zorunda kalıyor. Gece 2'de bile randevu alan bir sayfa hazırladım; 10 dakikada göstereyim mi?"
- *Instagram 1. mesaj (müşteri gibi):* "Merhaba, profilinizi yeni gördüm, işleriniz çok güzel. [İlçe]'nin tamamında hizmet veriyor musunuz?" → cevap gelince *sesli mesaj:* "Bio'daki linke tıkladım, site bulamadım / siteniz yenilenebilir gibi geldi. Merak etmeyin, sizin için zaten bir taslak hazırladım, 10-15 dakikalık görüşmede göstereyim mi? Numaram: …" → WhatsApp'a taşı.
- *Telefon (Spencer Reist uyarlaması):* "Merhaba, [Ad] Bey/Hanım ile mi görüşüyorum? Ben [Ad]. Şu an Google'da işletmenize bakıyorum; yorumlarınız çok iyi ama siteniz yok. İnanmayacaksınız ama sizin için Google'dan daha çok iş getirecek bir site hazırladım. Yarın 10-15 dakikalık görüntülü görüşmede göstersem olur mu?" → saat → e-posta/WhatsApp → "30 dakika önce hatırlatma atacağım."
- *First 100 (tanıdıklara):* "Yerel işletmelere web sitesi + sosyal medya + reklam işi kuruyorum. Sitesi olmayan ya da işini büyütmek isteyen bir işletme sahibi tanıyorsan beni ona iletir misin? Karşılığında ilk ayı bizden."
- *Loom video (2 dk):* "3 şey fark ettim: 1) ana sayfa telefonda 6 saniyede açılıyor, 2) WhatsApp butonu yok, 3) yorumlarınız sitede görünmüyor. Düzeltilmiş hâlini şurada gösteriyorum… İsterseniz konuşuruz, istemezseniz de sağlıcakla."
- *Keşif soruları:* "Siteyi en son ne zaman güncellediniz?" · "İşler nereden geliyor, kulaktan kulağa mı?" · "Daha fazla iş alabilir misiniz?" · "Bir müşteri size ortalama kaç para kazandırır?" → "Ayda 1 ekstra müşteri gelse sistem kendini öder mi?"
- *İtirazlar:* Pahalı → "Bir müşteri kaç para?" · Gerek yok → "Müşteri sizi doğrulamak için nereye bakıyor?" · Kendim yaparım → "Ne zamandır planlıyorsunuz? Haftaya hazır olsa?"

## 4. Hemen kuracağımız otomasyonlar (öncelik sırasıyla)

1. **Lead toplayıcı (n8n + Apify/Outscraper):** "sektör + ilçe" → Maps verisi (ad, telefon, site, yorum, Instagram) → PageSpeed skoru → Google Sheet; filtreli liste her sabah Telegram'a. (Kaynak: Mikey, Pavlo, Sam Robinson.)
2. **Mockup fabrikası (Claude Code hattımız):** niş şablonu (restoran, klinik, kuaför, tesisat, emlak) + işletme verisi (Maps + Instagram fotoğrafları) → 15 dakikada canlı önizleme linki; Relume/Base44/Lovable alternatif. Prompt zinciri Mikey planında.
3. **"Quick Five" lead takip akışı:** form/WhatsApp → anında onay mesajı → 10 dk yanıt yoksa arama görevi → 1. gün / 3. gün / 7. gün takip; cevapsız arama → otomatik WhatsApp ("Aramanızı kaçırdık, nasıl yardımcı olabiliriz?"). (Pavlo 7Tqs83MjBY8, hA91LtOslDs.)
4. **Google yorum motoru:** iş bitince QR/WhatsApp ile yorum isteği; yeni yoruma AI taslak yanıt → onay → yayın. (Pavlo WWCzZ56VAT4.)
5. **İçerik hattı (n8n):** kaynak (sektör haberi/RSS/müşteri fotoğrafları/yorumlar) → fikir ajanı → Perplexity doğrulama → platform başına Claude promptu (IG/FB/LinkedIn ayrı) → Nano Banana/Flux görsel marka şablonunda → Google Doc + Telegram "Onayla" → Instagram Graph API / Blotato yayın. Resmi Instagram API alt-akışı t63IlcH1GJY planında; token yenileme hatırlatması 50 günde bir. (AI Andy, Helena Liu, Vibe Marketer, Lakshit Ukani.)
6. **Carousel üretici:** Claude ile 6-8 slaytlık metin → HTML/Canva şablonu → PNG → IG carousel; QeV04PFPNck / jFAH0txMwiI transkriptleri kaynak (planları henüz yazılmadı).
7. **Onboarding kayıt:** tanışma görüşmesi kaydı → transkript → müşteri klasörü → site metni + marka sesi promptu otomatik taslak. (Sam Robinson.)
8. **Reklam standardı (Meta):** hizmet grubu başına 1 kampanya, 1 reklam seti, sadece konum hedefi, 20+ kreatif; Leads hedefi; audience segments (mevcut/etkileşimli/yeni). (Ben Heath 13s-G9Uj51A, -6okjIwMmmU.)

## 5. Kendi Instagram/Facebook hesabımız — ilk 30 gün içerik planı

**Konumlandırma:** "Yerel işletmeye yapay zekâ ile 1 haftada site + sosyal medya + reklam." Hedef kitle: ilçe bazlı işletme sahipleri. Ton: doğrudan, hashtag yok, emoji en fazla 1, uzun tire yok. Haftada 5 gönderi + her gün 2-3 story. Her gönderi tek CTA: "Ücretsiz örnek ana sayfa için DM'den 'ÖRNEK' yaz."

**Sütunlar (her hafta tekrar):**
- Pzt — **Önce/Sonra**: gerçek (veya izinli demo) bir işletmenin eski sitesi vs. 15 dakikalık mockup (carousel 5 slayt).
- Sal — **İşletme sahibine ipucu** (Content King): "Google'da çıkmak için profilinizde eksik 3 şey", "WhatsApp butonu neden siparişi %X artırır" (carousel 6-8 slayt, Claude ile).
- Çar — **Kulis/vlog reel** (30-45 sn): "Bugün Kadıköy'de 3 kafe için site yaptık" (süreç, ekran kaydı, sesli anlatım).
- Per — **Sosyal kanıt**: müşteri yorumu kartı / Google yorum ekranı / "ilk hafta 14 WhatsApp mesajı geldi" rakamı.
- Cum — **Teklif/Kampanya**: "Bu ay 5 işletmeye ücretsiz örnek site", paket kartı, SSS.
- Story günlük: anket ("Siteniz telefonda 3 saniyede açılıyor mu?"), süreçten kare, DM'den gelen soru-cevap, "bugün aradığımız 10 işletme" sayacı.

**Günlük plan:**
- 1-2: Profil kurulumu (bio: "Yerel işletmeye AI ile site + sosyal medya + reklam · 1 haftada yayında · DM: ÖRNEK"), highlight'lar (Örnekler, Fiyat, Süreç, Yorumlar), Facebook sayfası + Google İşletme Profili, 5 niş demo sitesi hazır.
- 3: Tanıtım reel'i "Biz kimiz, 15 dakikada ne yapıyoruz" (ekran kaydı).
- 4: Carousel "Yerel işletmenin sitesinde olması gereken 7 şey".
- 5: Önce/Sonra #1 (tesisatçı demo).
- 6: Story anketi + SSS "Site aylık mı, tek seferlik mi?"
- 7: Reel: Google Maps'te sitesi olmayan işletme bulma süreci (eğitici, "size de bakalım mı?").
- 8: Carousel "WhatsApp'tan sipariş alan restoran sayfası nasıl olur".
- 9: Önce/Sonra #2 (kafe).
- 10: Sosyal kanıt: ilk müşteri yorumu (First 100'den gelen).
- 11: Teklif kartı: 3 paket, fiyat bandı, "ilk 5 işletmeye kurulum ücretsiz".
- 12: Reel: 60 saniyede mockup üretimi (hızlandırılmış ekran).
- 13: Carousel "Google yorumlarına cevap vermeyen işletme neyi kaçırıyor".
- 14: Story: haftanın özeti, DM sayısı.
- 15: Önce/Sonra #3 (klinik/kuaför).
- 16: Reel: "Cevapsız aramaya otomatik WhatsApp" demo.
- 17: Carousel "Instagram'da satış yapan işletmenin 5 alışkanlığı".
- 18: Sosyal kanıt: ekran görüntüsü (gelen lead'ler, izinli).
- 19: Teklif: "Bu hafta 3 ücretsiz site denetimi" (Loom formatı).
- 20: Reel: ekip/kurucu hikâyesi (neden yerel işletme).
- 21: Story Q&A.
- 22: Önce/Sonra #4 (emlak/peyzaj).
- 23: Carousel "Meta reklamında yerel işletmenin yaptığı 5 hata" (Ben Heath'ten).
- 24: Reel: AI chat asistanının site üzerinde randevu alması.
- 25: Sosyal kanıt: müşteriyle 30 sn video yorum.
- 26: Teklif: referans programı ("getirdiğin işletmenin ilk ayı %20 sana").
- 27: Reel: "1 haftada yayına alma takvimi" (süreç).
- 28: Carousel "Sitenizi telefondan 10 saniyede test edin" (PageSpeed).
- 29: Önce/Sonra #5 + ay sonu toplu kolaj.
- 30: Reel: 30 gün özeti, rakamlar (üretilen mockup, gelen DM, kapanan müşteri), sonraki ay teklifi.

**Üretim:** tüm carousel ve caption'lar madde 4.5'teki n8n hattından, görseller marka şablonuyla; her Pazar 1 saatte haftanın 5 gönderisi onaylanır. Kendi hesabımız = otomasyonun canlı vitrini ("bu hesabı da aynı sistem yönetiyor").

## 6. Uyarılar (karşı görüşler)
- Yerel işletme düşük bilet + yüksek kayıp olabilir (Nick Saraev); 20-30 müşteriye kadar soğuk temas zorunlu, sonra referans/reklam (Sam Robinson).
- Sponsorlu videolar (Base44, GHL, Make, Relume affiliate) araç puanlarını şişiriyor; aracı değil süreci kopyala.
- İzinsiz toplu SMS/e-posta TR'de KVKK/İYS riski; WhatsApp bire bir, yüz yüze ve Instagram DM öncelikli.
- Tam otomatik yayın yok; her müşteri hesabında onay kapısı.
- Sahte Google yorumu asla; yorum stratejisi vaadi, sıralama garantisi değil.

## 7. Durum ve eksikler
- Plan yazılan 17 video: bkz. bu klasör. Plan bekleyen A/B transkriptleri (öncelikli): QeV04PFPNck, jFAH0txMwiI, id22R7iBTjo, OFoNYWIS7IA (carousel); L3NUp2XP_h0, PXcDqmamX2Q, m02TeQ9kHVo, 9p3levei5Aw (n8n IG); IIJbt7bxh5w, kdev1F8o5y8, zbt5wZLiLGU, -np6T0ljYwo, OZ_ZriNypbg, nn7jO7EUjjM, M7niLhbbPP8, iUNzRKOC25w, TxMAUMUn-is, 3fIXJyrG50c, ZT4LqD2_GwM, Dgs1tQngbec (satış); m6HC_o4Cvko, OrIxvWCdkEc, coq11ZnucH4, jZpOI5petho, orZpEVklDlI, hq5dPOun_00, XJXNdbjxy9Q (outreach); fj7cmY-El-c, GwveA7jMsLY, xJ99dBYhpRI, Rm3ObJXsuOI, eRiu97HAtxE (reklam); R9c_JQrEtu8, 4ZI_fL4cw_c, QovlUE_VlWQ, CvQ3ARulOKE, Jsx-rImkdQk, DIXXW_jBS0w, Fnw1_YAYEAc, 00obLp8vowQ, uhbeDrdmCoI, IIXupZAGvDk (içerik).
- Transkripti alınamayan (altyazısız): xDSPmIO1_jk, eea_h19WF00, LFO4cP0KMwk, QG_6Yqulus0. YouTube IP engeli (timedtext 429) nedeniyle ~45 video (çoğu C) çekilmedi; `index.md` ve `arastirma/youtube/transkriptler/` güncel durumu gösterir.

## Ek (2026-10-10): carousel/n8n + reklam planları
Yeni planlar: `QeV04PFPNck-claude-proje-html-carousel.md`, `jFAH0txMwiI-claude-code-design-md-carousel-skill.md`, `L3NUp2XP_h0-n8n-drive-reels-kalici-meta-token.md`, `fj7cmY-El-c-restoran-sosyal-medya-kursu.md`, `GwveA7jMsLY-restoran-google-ads-kurulumu.md`.
- **Carousel = HTML→PNG, görsel model değil**: referanslardan stil JSON'u / `design.md` çıkar ("Turn this into a design system in an HTML file and a design MD file."), müşteri klasöründe sakla; aynı dosya site + sosyal + reklam kreatifinde kullanılır. Türkçe karakter ve gerçek font garantisi.
- **Carousel skill'i**: Hook → Acı → Adımlar → Sonuç → CTA; ilk çalıştırmada kitle/teklif/ton sorulup profile yazılır; çıktı slaytlar + IG caption + LinkedIn metni.
- **Meta kalıcı token**: Business Suite System User → app'e full control → token "Never" + 8 izin (`instagram_content_publish`, `publish_video` dahil); Reels için container → Wait 60 sn → `media_publish`; video URL'si public olmalı.
- **Restoran sosyal medya kuralları**: her post'ta konum, kampanya/ilan story'de, feed evergreen; sadece organikte kanıtlanmış içeriğe reklam; videoda 20-30 başarılı örneği tersine mühendislik; menü kalemi = karakter.
- **Google Ads yerel kurulum**: Search-only, Clicks + max CPC (Keyword Planner üst sıra ortalaması), Presence (interest değil), ilçe bazlı konum, exact match, başlık 1'e pinli keyword insertion, günlük bütçe = max CPC × 10, PMax/Display/lead form yok.
- Plan bekleyenler listesinden bu 5 id düşüldü.
