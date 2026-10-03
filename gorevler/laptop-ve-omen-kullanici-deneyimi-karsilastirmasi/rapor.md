# Laptop seçimi: HP OMEN ve alternatifleri, gerçek kullanıcı deneyimiyle

**Gözlem tarihi:** 3 Ekim 2026
**Kapsam:** Grafik tasarım, yazılım geliştirme ve oyun için, abartılı olmayan ama donanımı iyi bir laptop. HP OMEN'in kullanıcı deneyimleri (Ekşi Sözlük, Reddit, Şikayetvar, HP forumu) ve editoryal testler alternatiflerle karşılaştırıldı.
**Kısa liste:** [`liste.md`](liste.md) · **Kaynakların tamamı:** [`kaynaklar.md`](kaynaklar.md) · **Yol haritası:** [`harita.md`](harita.md)

> **Kısa cevap:** Hayır. Bu kadar seçenek arasında en mantıklısı **OMEN değil**.
> - OMEN'in uygun fiyatlı 32 GB sürümü (16-ap0010nt, **84.150 TL**) kâğıt üstünde cazip. Ama ekranı sRGB renk alanının ancak **%58–62'sini** gösteriyor; bu, grafik tasarım için zayıf.
> - 2023–2025 OMEN 16 ve OMEN MAX'ta **kapak (Hall) sensörü, siyah ekran ve anakart arızası** şikâyetleri Reddit, Şikayetvar ve HP forumunda tekrar tekrar geçiyor. Türkiye'de servis süreciyle ilgili şikâyetler de yoğun.
> - Bu tablo "her cihaz bozuluyor" demek değil. Ama rakiplerden daha belirgin bir risk kümesi var.
> - **Önerim:** Lenovo Legion 5 15IRX10, 32 GB / 1 TB, **99.999 TL**.
> - Biraz daha ucuzu için: **Casper Excalibur G915** 32 GB / 1 TB (**93.599 TL**) ya da **Lenovo LOQ 15** (**73.799 TL**, RAM'i sonradan 32 GB'a çıkarılır).

---

## 1. İhtiyaç profili

| İhtiyaç | Neden | Asgari | Hedef |
|---|---|---|---|
| RAM | Photoshop/Illustrator + IDE + tarayıcı + emülatör/Docker aynı anda açık kalıyor | 16 GB, 2 slot (yükseltilebilir) | **32 GB** (2×16, çift kanal) |
| Depolama | Oyunlar 100 GB'ı aşabiliyor; tasarım arşivi ve SDK'lar yer kaplıyor | 512 GB + boş M.2 yuvası | **1 TB**, ikinci M.2 yuvası olsun |
| GPU | 1080p/1200p oyun, Adobe GPU hızlandırma, CUDA | RTX 5050 | **RTX 5060, 100–115 W** |
| CPU | Derleme ve dışa aktarma süreleri | 8 çekirdek H serisi | HX ya da Ryzen 7/9 |
| Ekran | Grafik tasarımda renklerin doğru görünmesi | **%95+ sRGB**, 300 nit | %100 sRGB, 16:10, 1600p, 350+ nit |
| Güvenilirlik | Kullanıcının ana şartı: kronik arızası olmasın | Belirgin bir arıza kümesi olmasın | Türkiye'de servisi iyi bilinen marka |
| Fiyat | "Makul olsun, abartılı olmasın" | — | **65–100 bin TL** bandı (2026-10 TR piyasası) |

**Neden ekran bu kadar önemli?** Notebookcheck'in ölçtüğü ucuz oyun panelleri sRGB'nin yalnızca %58–65'ini gösteriyor. Örnekler: OMEN 16-ap0091ng %58, Nitro V 16 %58,2, Cyborg 15 %64,8. Bu panellerde hazırlanan bir tasarım başka ekranlarda farklı renkte görünür. Bu yüzden ekran, puanlamada en ağır iki ölçütten biri.

## 2. Teknik doğrulama (resmî ve editoryal)

| Model | GPU gücü | Ekran (ölçüm veya resmî bilgi) | RAM / M.2 | Kaynak |
|---|---|---|---|---|
| HP OMEN 16-ap (2025, RTX 5060) | 115+25 W (ap0091ng) | 1920×1200 144 Hz, ~300 nit, **%58 sRGB** (ölçüm); TR modeli ap0010nt için satıcı bilgisi %62,5 sRGB | 2 SO-DIMM; test cihazında **1 M.2** ve tek kanal 1×16 GB | [Notebookcheck](https://www.notebookcheck.net/The-best-budget-gamer-HP-Omen-16-laptop-review.1135455.0.html), [epey](https://www.epey.com/laptop/hp-omen-gaming-16-ap0010nt-ce2b2ea.html) |
| HP (HyperX) OMEN 16 2026, RTX 5070 | — | QHD+ 240 Hz, 570 nit, tam sRGB | 2 SO-DIMM, 2 M.2 | [Notebookcheck](https://www.notebookcheck.net/HP-HyperX-Omen-16-review-One-of-the-fastest-AMD-CPUs-for-mainstream-gamers.1348846.0.html) |
| Lenovo Legion 5 Gen 10 | RTX 5060 115 W | IPS 1920×1200 165 Hz %100 sRGB ya da OLED %100 DCI-P3 | 2 SO-DIMM (64 GB'a kadar), 2 M.2; Notebookcheck puanı **%93,6** (15AHP10 OLED test cihazı) | [Notebookcheck](https://www.notebookcheck.net/The-best-mainstream-gamer-in-2025-Lenovo-Legion-5-15-Laptop-Review.1047975.0.html), [PSREF 15IRX10](https://psref.lenovo.com/syspool/Sys/PDF/Legion/Legion_5_15IRX10/Legion_5_15IRX10_Spec.pdf) |
| Lenovo LOQ 15 (2025/26) | RTX 5060 100–105 W | PSREF'teki iki panelin ikisi de %100 sRGB; 15AHP11 ölçümü %99,8 sRGB | 2 SO-DIMM, 2 M.2 | [PSREF LOQ 15IRX10](https://psref.lenovo.com/syspool/Sys/PDF/LOQ/LOQ_15IRX10/LOQ_15IRX10_Spec.pdf), [Notebookcheck](https://www.notebookcheck.net/Eyesore-or-eye-catcher-Lenovo-LOQ-15-gaming-laptop-review.1338691.0.html) |
| Casper Excalibur G915 | RTX 5060 115 W | 16" 1920×1200 165 Hz 350 nit %100 sRGB (üretici bilgisi, **bağımsız ölçüm yok**) | 2 SO-DIMM, 2 M.2 | [casper.com.tr](https://www.casper.com.tr/excalibur-g915-p-204) |
| Acer Nitro 16S AI / Nitro V 16S AI | 16S: RTX 5060 115 W | 2560×1600 180 Hz 400 nit %100 sRGB (satıcı bilgisi) | Slot bilgisi doğrulanamadı | [epey 16S](https://www.epey.com/laptop/acer-nitro-16s-ai-an16s-61-nh-qxuey-001-32.html), [epey V16S](https://www.epey.com/laptop/acer-nitro-v-16s-ai-anv16s-41-r481.html) |
| Acer Nitro V 16 / V 15 (temel panel) | ~75–85 W | **%58–60 sRGB** (ölçüm) | V15'te tek M.2 | [Notebookcheck V16](https://www.notebookcheck.net/Acer-Nitro-V-16-AI-Review-Affordable-gaming-laptop-with-great-battery-life.1156010.0.html) |
| ASUS TUF A16 (2025) | 115 W'a kadar | %100 sRGB seçenekleri var | 2 SO-DIMM, 2 M.2 | [ASUS spec](https://www.asus.com/laptops/for-gaming/tuf-gaming/asus-tuf-gaming-a16-2025/techspec/) |
| MSI Cyborg 15 | RTX 5050 **45 W** | %64,8 sRGB | 1 M.2 | [Notebookcheck](https://www.notebookcheck.net/MSI-Cyborg-15-review-The-perfect-budget-gaming-laptop-for-2026.1218188.0.html) |

## 3. OMEN: Ekşi Sözlük ve Reddit'te ne deniyor?

### 3.1 Erişim durumu
- **Reddit:** reddit.com bu bulut ortamından 403 hatası veriyor. Bunun yerine Reddit'in herkese açık gönderi arşivi olan **Arctic Shift API** kullanıldı. 8 sorguyla **614 gönderi** çekildi; başlık, tarih ve bağlantılar [`kanit/reddit-arctic-shift-ozet.json`](kanit/reddit-arctic-shift-ozet.json) dosyasında. Aşağıdaki Reddit özetleri bu gönderilerin ve yorumlarının gerçek metninden yapıldı.
- **Ekşi Sözlük:** Hem doğrudan erişim hem WebFetch 403 verdi (bot koruması); Wayback arşivinde de kopya yok. Ekşi bulguları **yalnızca arama motoru özetlerine** dayanıyor ve öyle işaretlendi. Ekşi'yi tam okumak için bir tarayıcıdan elle bakmak gerekiyor (bkz. §7).
- **Şikayetvar:** curl ile açıldı. Şikâyetler gerçek sayfadan okundu.
- **HP Support Community:** 403 verdi. Yalnızca arama özetleri var.

### 3.2 Reddit (gerçek gönderiler, 2025–2026)
**Sorun bildirenler:**
- **"Indians don't buy HP Omen"** (r/HPOmen, 18.09.2026, 27 puan, 75 yorum): OMEN 16 2023 wf, RTX 4060. Cihaz 3 ayda Hall sensörü nedeniyle ölmüş; şimdiye kadar **4 kez anakart değişmiş**; yetkili servisler hasar vermiş. — https://www.reddit.com/r/HPOmen/comments/1wk5wdn/indians_dont_buy_hp_omen/
- **OMEN MAX 16 (2025, RTX 5070, Türkiye):** Isınınca siyah ekran oluyor. HP önce "yalnızca panel arızası" demiş, sonra anakart ve paneli değiştirmiş. Servis portalında "KARGO DEPARTMANINA ÇIKIŞ" görünüyor; yani olay Türkiye'de. Kullanıcının sonraki 33 dakikalık stres testinde yeni kart sorunsuz çıkmış. — https://www.reddit.com/r/HPOmen/comments/1w3dcrt/ ve https://www.reddit.com/r/HPOmen/comments/1w6emm6/
- **Menteşe:** "My frustrating experience with HP Support" (24.08.2026). Garantideki OMEN 16'nın menteşesi kırılmış, panel yerinden çıkmış; servis garantili cihaza bakmayı reddetmiş. — https://www.reddit.com/r/HPOmen/comments/1vwus34/ — Aynı dönemde "Hinges popping out while closing" ve "Is this normal (hinge)" (25 yorum) başlıkları da var.
- **Siyah ekran ve titreme:** Yalnızca Eylül 2026'da r/HPOmen'de **10'dan fazla** siyah ekran, titreme veya Hall sensörü başlığı açılmış. Örnekler: OMEN 16-xd0001na, 16-wd, 16-wf ve 2023 modelleri. — Liste: [`kanit/reddit-arctic-shift-ozet.json`](kanit/reddit-arctic-shift-ozet.json) (`hall` sorgusu)
- **2 yıllık uzun kullanım** (OMEN, 6800H + 3070 Ti, 21 puan): "Performans zirve" ama "verdiğinden fazla sorun çıkardı". Fanlar gürültülü. Önerisi: uzatılmış garanti ya da kaza koruması al, 8 ayda bir iç temizlik yap. — https://www.reddit.com/r/HPOmen/comments/1v3bt5v/

**Memnun olanlar:**
- **OMEN 16-xd0020AX, 1 yıl:** "Yapı kalitesi 10/10." Tek eleştirisi: "menteşeler daha iyi olmalı." — https://www.reddit.com/r/HPOmen/comments/1u2brki/
- **RTX 5050'li OMEN (am0277TX, Kasım 2025'ten beri):** AAA oyunlarda "0 sorun". Başka yorumlar da "yeni modellerde Hall sorunu yok" diyor. — https://www.reddit.com/r/HPOmen/comments/1veovdy/
- **OMEN 16-am (2025, RTX 5060), 10 aylık sahibi:** F14 BIOS'a güncellemiş, "light bleed dışında sorun yok". Başka bir yorumcu, Hall sorununun 2024 sonrası modellerde düzeldiğini söylüyor; bu doğrulanmamış bir topluluk iddiası. — https://www.reddit.com/r/HPOmen/comments/1wugasl/
- **OMEN 16 + RTX 5060 mı, LOQ mu?** diye soran kullanıcıya gelen cevap: "Legion'u al, kalan parayla 16 GB RAM ekle." — https://www.reddit.com/r/HPOmen/comments/1vb7crp/

### 3.3 Ekşi Sözlük (yalnızca arama özeti; sayfalar 403 verdi)
- OMEN dizüstüler için "felaket laptop" diyen ve aldığına pişman olan yazar. — https://eksisozluk.com/entry/70654268
- Metro 2033'te GPU 80 °C, CPU 90 °C'ye çıkıyor; ısınma şikâyeti. — https://eksisozluk.com/entry/99930815
- OMEN 15-ek1008nt: günlük kullanımda 45–50 °C, sessiz; olumlu. — https://eksisozluk.com/hp-omen-15-ek1008nt--7422951
- OMEN 15 (2020): ilk haftalar sorunsuz, ısınma ayarla yönetilebiliyor. — https://eksisozluk.com/hp-omen-15-2020--6667691
- Ana başlık: https://eksisozluk.com/hp-omen--4677836 · HP teknik servis başlığı: https://eksisozluk.com/hp-teknik-servis--449193?p=8

### 3.4 Şikayetvar ve HP forumu (Türkiye ve dünya)
| Sorun | Model / yıl | Kanıt | Durum |
|---|---|---|---|
| **Hall (kapak) sensörü:** ekran ve klavye kapanıyor, cihaz açılmıyor | OMEN 16 wf (2023), xd (2024), xf | [Şikayetvar 1](https://www.sikayetvar.com/hp/hp-omen-dizustu-bilgisayarda-kronik-donanim-arizasi-ve-yetersiz-teknik-destek-sureci), [2](https://www.sikayetvar.com/hp/hp-omen-16-modelinde-hall-sensor-arizasi-icin-ucretsiz-onarim-talebi), [3](https://www.sikayetvar.com/hp/hp-omen-16-hall-sensor-arizasi-garanti-kapsaminda-cozum-bekliyor) + Reddit | Sayfa açıldı |
| **Siyah ekran / anakart** (3–6 ayda) | OMEN 16 2023–2025, MAX 16 | [Şikayetvar](https://www.sikayetvar.com/hp/hp-omen-16-bilgisayarinda-tekrarlanan-anakart-arizasi-ve-degisim-istegi), [Şikayetvar](https://www.sikayetvar.com/hp/hp-omen-16-ana-kart-arizasi-ve-garanti-degisimi-reddi), [HP forum](https://h30434.www3.hp.com/t5/Gaming-Notebooks/HP-Omen-16-Motherboard-issues/td-p/9278296) | Şikayetvar açıldı, HP forum arama özeti |
| **Menteşe / çerçeve** (servis "kullanıcı hatası" deyip 6.171 TL istemiş) | OMEN 16 wd; OMEN 15'te tarihsel olarak çok yaygın | [Şikayetvar](https://www.sikayetvar.com/hp/hp-omen-laptop-mentese-kirilmasi-kullanici-hatasi-iddiasi-ve-haksiz-ucret-talebi), [HP forum 2016](https://h30434.www3.hp.com/t5/Gaming-Notebooks/2016-HP-Omen-Hinge-Issues-POST-EM-ALL-HERE/td-p/6189559) | Açıldı / arama özeti |
| **Isınıp ani kapanma** | OMEN MAX 16 ah0001NT (2 ayda başlamış) | [Şikayetvar](https://www.sikayetvar.com/hp/hp-omen-max-laptopda-tekrarlayan-asiri-isinma-ve-ani-kapanma-problemi) | Açıldı |
| **BIOS güncellemesi takılması** | OMEN 16 (Ağustos 2026) | [Şikayetvar](https://www.sikayetvar.com/hp/hp-omen-16-bios-guncellemesi-takildi-acil-destek-gerekiyor) | Açıldı |
| **Servis** (17–24 gün bekleme, 6–11 bin TL "kullanıcı hatası" faturası) | Genel | [Şikayetvar HP OMEN (406 şikâyet)](https://www.sikayetvar.com/hp/omen) | Açıldı |

### 3.5 Hüküm: OMEN "kronik arızalı mal" mı?
- **Kanıtla söylenebilecek:** 2023–2024 OMEN 16 (wf/xf/xd) serisinde Hall sensörü, siyah ekran ve anakart arızalarından oluşan, **birçok ülkede tekrar eden ve belgelenmiş bir küme** var. 2025 OMEN MAX'ta da benzer bir Türkiye vakası görülüyor.
- **Söylenemeyecek:** Arıza oranını gösteren bir veri yok. Şikâyet siteleri doğası gereği olumsuz deneyime kayar. 2025 OMEN 16 (ap/am) neslinde olumlu raporlar çoğunlukta; bu nesil için henüz belirgin bir küme yok, ama süre de kısa.
- **Pratik sonuç:** OMEN alınacaksa uzatılmış garanti veya HP Care Pack alınmalı. Ekranın kararması ya da kapak açısına göre gidip gelmesi gibi belirtiler garanti süresi içinde hemen bildirilmeli.

## 4. Alternatifler: kronik sorunlar ve olumlu deneyimler

| Model | Kronik / tekrarlayan şikâyetler | Olumlu | Türkiye servisi (Şikayetvar) |
|---|---|---|---|
| **Lenovo Legion 5 Gen 10** | dGPU modunda ekran glitch'i raporları ([Lenovo forum](https://gaming.lenovo.com/emea/members/360486-mind12), başlık doğrulandı); oyunda fan sesi yüksek (50 dB) | Notebookcheck: "2025'in en iyi ana akım oyun laptopu", bakımı kolay. Reddit'te sık verilen tavsiye: "Legion al + RAM ekle" | Lenovo 6/100, [Legion sayfası 139 şikâyet](https://www.sikayetvar.com/lenovo/legion): servis gecikmesi, parça bulunamaması |
| **Lenovo LOQ 15** | 170 W adaptör oyunda yetmiyor, şarjdayken pil düşüyor; ekran hasarına "kullanıcı hatası" denmesi; 9. nesilde anakart iddiaları (arama özeti). Reddit'te "Dead LOQ 15IAX9 (GPU short)" başlığı | Soğutma iyi; klavye ve touchpad övülüyor | [LOQ: 327 şikâyet](https://www.sikayetvar.com/lenovo/loq) |
| **Casper Excalibur G915 / G870** | G870'te ısınma ve FPS düşüşü (3'ten fazla şikâyet), "sıvı teması" gerekçesiyle 37 bin TL istenmesi. G915 için bağımsız test yok | Casper SSS: RAM veya SSD'yi kendiniz değiştirmek garantiyi bozmuyor | **Casper 76/100**, listedeki en iyi not ([sayfa](https://www.sikayetvar.com/casper)) |
| **Acer Nitro (V/16S)** | Şarjdayken pil düşmesi, servisin ısınmayı çözememesi, mavi ekran ([Nitro: 286 şikâyet](https://www.sikayetvar.com/acer/nitro)) | Fiyat/performans ve pil ömrü iyi. Reddit'te "Love my Acer Nitro!" | **Acer 2/100**, en zayıf servis notu ([sayfa](https://www.sikayetvar.com/acer)) |
| **ASUS TUF A16 / F16** | Şarjdayken fanların aniden 6600+ rpm'e çıkması (BIOS sonrası, ROG forum, arama özeti); MUX geçişinde siyah ekran | Pil, dengeli soğutma, sağlam kasa | ASUS 4.775 şikâyet ([sayfa](https://www.sikayetvar.com/asus?k=tuf)). TR fiyatı bu sınıfta pahalı |
| **MSI Katana / Cyborg** | Donma ve mavi ekran, menteşe ([Katana](https://www.sikayetvar.com/msi-turkiye?k=katana)). Cyborg'da 45 W GPU ve %64,8 sRGB ekran | QHD Katana'nın performansı iyi | Servispoint, deneyimler karışık |
| **Monster Abra / Tulpar** | Menteşe, şarj soketi, tekrarlayan anakart arızası ([Tulpar T7: 862](https://www.sikayetvar.com/monster-notebook/tulpar-t7), [Abra A5: 1.978](https://www.sikayetvar.com/monster-notebook/abra-a5)) | 4 yıl garanti | Monster 14/100, toplam 8.641 şikâyet |

Not: Şikâyet sayıları satış hacmiyle orantılıdır; bir **oran** değildir. Aynı ölçütle karşılaştırmak için marka notu (yanıt ve çözüm oranı) da birlikte verildi.

## 5. Güncel fiyatlar (Türkiye, 03.10.2026)

Fiyatlar epey.com'un pazaryeri satıcı listelerinden alındı. Kaynak siteler Hepsiburada, Trendyol, n11, Idefix, PTT AVM ve Amazon.com.tr; sayfada "x dakika önce güncellendi" bilgisi var. Kilit 9 sayfa bu oturumda ikinci kez bağımsız olarak açılıp fiyatlar teyit edildi. akakce, cimri, Hepsiburada, Vatan ve MediaMarkt doğrudan 403 verdi.

| Model | Yapılandırma | En düşük | Satıcı | Bağlantı |
|---|---|---|---|---|
| Lenovo Legion 5 15IRX10 83LY00PYTR | i7-13650HX · RTX 5060 · 32 GB · 1 TB · 15,3" 1200p 165 Hz %100 sRGB | **99.999 TL** | Nethouse (HB/Amazon/Trendyol/n11) | https://www.epey.com/laptop/lenovo-legion-5-15irx10-83ly00pytr.html |
| Casper Excalibur G915.1362-DF60X-C | i7-13620H · RTX 5060 115 W · 32 GB · 1 TB · 16" 1200p 165 Hz %100 sRGB | **93.599 TL** | Casper (n11) | https://www.epey.com/laptop/casper-excalibur-g915-1362-df60x-c.html |
| Acer Nitro 16S AI AN16S-61 | Ryzen AI 9 365 · RTX 5060 115 W · 32 GB · 1 TB · 16" 1600p 180 Hz %100 sRGB | **92.999 TL** | Teknorya (HB) | https://www.epey.com/laptop/acer-nitro-16s-ai-an16s-61-nh-qxuey-001-32.html |
| HP OMEN 16-ap0010nt | Ryzen AI 7 350 · RTX 5060 · 32 GB · 1 TB · 16" 1200p 144 Hz **%62,5 sRGB** | **84.150 TL** | Kapında (Trendyol) | https://www.epey.com/laptop/hp-omen-gaming-16-ap0010nt-ce2b2ea.html |
| Lenovo LOQ 15AHP10 83JG008UTRv1 | Ryzen 7 250 · RTX 5060 · 16 GB (1 slot boş) · **2 TB** · 15,6" FHD 144 Hz | **73.799 TL** | Betaplus (n11) | https://www.epey.com/laptop/lenovo-loq-15ahp10-83jg008utrv1.html |
| Acer Nitro V 16S AI ANV16S-41-R481 | Ryzen 7 260 · RTX 5060 · 16 GB · 512 GB · 16" 1600p 180 Hz %100 sRGB | **64.859 TL** | Adlertech (Idefix) | https://www.epey.com/laptop/acer-nitro-v-16s-ai-anv16s-41-r481.html |
| HP OMEN 16-ap0032nt | Ryzen 9 8940HX · **RTX 5070** · 32 GB · 1 TB · QHD+ 240 Hz 500 nit | 123.048 TL | VPBilişim (HB) | https://www.epey.com/laptop/hp-omen-gaming-16-ap0032nt-ca8e9ea.html |
| HP Victus 15-fa2019nt | Core 5 210H · RTX 5060 · 16 GB · 1 TB · 15,6" FHD 144 Hz | 59.796 TL | KapındaSepet (Trendyol) | https://www.epey.com/laptop/hp-victus-gaming-15-fa2018nt-c21sgea.html |
| ASUS TUF A16 FA608UM-RV069W | Ryzen 7 260 · RTX 5060 115 W · 16 GB · 512 GB | 89.999 TL | Nötron (Trendyol) | https://www.epey.com/laptop/asus-tuf-gaming-a16-fa608um-rv069w.html |

**Uyarı:** Bunlar pazaryeri satıcılarının fiyatları. Almadan önce faturalı satış ve distribütör garantisi kontrol edilmeli. Arama özetlerindeki eski fiyatlar güncelden %20–40 düşük çıktığı için **kullanılmadı**.

## 6. Karar

### 6.1 Puanlama
Her ölçüt 1–5 arası puanlandı, ağırlıklarla 100 üzerinden toplam hesaplandı. Ağırlıklar: ekran (tasarım) %25, güvenilirlik ve topluluk %25, RAM/depolama ve yükseltme %20, performans %15, fiyat %15.

| Sıra | Model | Ekran | Güven | Bellek | Perf. | Fiyat | **Toplam** | Fiyat |
|---|---|---|---|---|---|---|---|---|
| 1 | Lenovo Legion 5 15IRX10 | 4 | 4 | 5 | 5 | 3 | **84,0** | 99.999 TL |
| 2 | Casper Excalibur G915 | 4 | 3,5 | 5 | 4 | 4 | **81,5** | 93.599 TL |
| 3 | Acer Nitro 16S AI | 5 | 3 | 3 | 5 | 4 | **79,0** | 92.999 TL |
| 4 | HP OMEN 16-ap0032nt (RTX 5070) | 5 | 2,5 | 4 | 5 | 2 | 74,5 | 123.048 TL |
| 5 | Lenovo LOQ 15AHP10 | 3 | 3,5 | 4 | 3,5 | 5 | 74,0 | 73.799 TL |
| 6 | Acer Nitro V 16S AI | 5 | 3 | 2 | 3,5 | 5 | 73,5 | 64.859 TL |
| 7 | **HP OMEN 16-ap0010nt (RTX 5060)** | **2** | **2,5** | 4 | 4 | 4 | **62,5** | 84.150 TL |

### 6.2 Neden Legion 5?
1. **Gereken her şey kutudan çıkıyor:** 32 GB çift kanal RAM, 1 TB SSD ve ikinci M.2 yuvası; ek masraf yok.
2. Ekranı %100 sRGB. Tasarım için yeterli; OMEN'in %58–62'lik panelinden belirgin şekilde iyi.
3. Gen 10 kasası editoryal olarak sınıfının en iyi puanlısı (Notebookcheck %93,6, AMD/OLED test cihazı; önerilen SKU Intel/IPS). Reddit'te de "Legion al" tavsiyesi sık geçiyor.
4. OMEN 16'daki gibi belgelenmiş bir Hall sensörü ya da anakart kümesi yok. Bilinen riskleri: Gen 10 ekran glitch raporları ve Lenovo Türkiye servis notunun düşük olması.

### 6.3 Bütçeye göre yollar
- **A, ana öneri:** Legion 5 15IRX10, 32 GB / 1 TB, **99.999 TL**.
- **B, biraz daha ucuz ve servisi daha iyi:** Casper Excalibur G915, 32 GB / 1 TB, **93.599 TL**. Türkiye'de servis notu en iyi marka (76/100). Ekranı kâğıt üstünde %100 sRGB ama bağımsız ölçüm yok. İşlemcisi H serisi, HX değil.
- **C, sıkı bütçe:** Lenovo LOQ 15AHP10, 16 GB / 2 TB, **73.799 TL**. Boş slota 16 GB DDR5-5600 SO-DIMM eklenip 32 GB'a çıkarılır (modül fiyatı bu çalışmada araştırılmadı). Ekran FHD 144 Hz 300 nit; bu SKU'nun sRGB değeri satıcı sayfasında yazmıyor, almadan önce kontrol edilmeli.
- **Ekran öncelikliyse:** Acer Nitro 16S AI (92.999 TL), 1600p %100 sRGB. Ancak Acer'in Türkiye servis notu en zayıfı (2/100).
- **OMEN illa istenirse:** 16-ap0010nt değil, **16-ap0032nt** (QHD+ 500 nit, RTX 5070, 123.048 TL) alınmalı, yanına Care Pack eklenmeli. Bu durumda bütçe "abartılı olmasın" sınırını aşar.

### 6.4 Hangi modele kesinlikle dikkat?
- **Cyborg 15:** 45 W GPU, renk alanı dar panel.
- **Temel panelli Nitro V 15/16:** %58–60 sRGB.
- **G870 ve Monster Abra:** 250 nit, renk değeri belirsiz panel.
- **Herhangi bir FHD OMEN 16-ap/am:** Ekranı tasarım için zayıf.

## 7. Yapılamayanlar ve sınırlar
- **Ekşi Sözlük doğrudan okunamadı.** Her yoldan 403 alındı, Wayback'te kopya yok. Ekşi maddeleri yalnızca arama motoru özetine dayanıyor.
- **Reddit doğrudan açılamadı.** Bunun yerine herkese açık Arctic Shift arşivi kullanıldı (614 gönderi ve seçili yorumlar). Bazı geniş sorgular arşivin hız sınırına takıldı.
- **Fiyatlar** pazaryeri satıcılarına ait ve anlık; stok ve kampanyayla değişir. akakce, cimri ve Hepsiburada doğrudan açılamadı.
- **Fiziksel test yapılmadı.** Windows üzerinde benchmark, cihaz testi ya da mağaza ziyareti yok; bu bir bulut ortamı. Ekran ve termal değerler Notebookcheck ölçümlerine ya da üretici/satıcı bilgisine dayanıyor; hangisi olduğu tabloda yazıyor.
- **Arıza oranı verisi** hiçbir marka için kamuya açık değil. Güvenilirlik puanı tekrar eden şikâyet kümelerine, editoryal bulgulara ve servis notuna göre verildi; bu öznel bir değerlendirme.
- **RAM modülü fiyatı** araştırılmadı; C yolundaki yükseltme maliyeti açık bırakıldı.
