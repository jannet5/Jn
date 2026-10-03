# Laptop seçimi: HP OMEN ve alternatifleri, gerçek kullanıcı deneyimiyle (2. tur, düzeltilmiş)

**Gözlem tarihi:** 3 Ekim 2026 · **Durum:** satın alma yapılmadı, cihaz fiziksel olarak test edilmedi; rapor yalnızca kaynaklara dayanıyor.
**Kısa liste:** [`liste.md`](liste.md) · **Kaynaklar:** [`kaynaklar.md`](kaynaklar.md) · **Birincil kaynak okuma kaydı:** [`kanit/birincil-kaynak-okuma.md`](kanit/birincil-kaynak-okuma.md) · **Düzeltme kaydı:** §8

> **Kısa cevap: Tek ve kesin bir "kazanan" yok; seçim önceliğe göre değişiyor.** Ama Türkiye'de satılan **OMEN 16-ap0010nt'yi grafik tasarım için önermiyorum.** Sebebi arıza istatistiği değil, **ekranı**. Satıcı ilanında ve HP'nin beyanında bu panel sRGB renk alanının yalnızca **%62,5**'ini gösteriyor. Notebookcheck aynı tip paneli **farklı bir SKU'da (ap0091ng)** ölçtü ve **%58,1** buldu.
> - **Ekran doğruluğu, kutudan 32 GB RAM ve 2 M.2 yuvası öncelikliyse:** Lenovo Legion 5 15IRX10 **83LY00PYTR**, 99.999 TL. Ekran değeri ilan bilgisi; bu SKU için bağımsız ölçüm yok.
> - **Servis itibarı ve biraz daha düşük fiyat öncelikliyse:** Casper Excalibur **G915.1362-DF60X-C**, 93.599 TL. Ekran değeri ilan bilgisi; bağımsız ölçüm yok.
> - **Sıkı bütçe:** Lenovo LOQ 15AHP10 **83JG008UTRv1**, 73.799 TL. 16 GB geliyor; RAM'i sonradan 32 GB'a çıkarmak gerekiyor.
> - **OMEN'in güvenilirliği:** 2023–2024 OMEN 16 (wf/xf/xd) serisinde Hall sensörü ve siyah ekran **anekdotları** çok. Ama bunlar **2025 ap/am serisi için bir arıza oranı değil**; paydayı (kaç cihaz satıldığını) bilmiyoruz.

---

## 1. İhtiyaç profili

| İhtiyaç | Neden | Asgari | Hedef |
|---|---|---|---|
| RAM | Photoshop/Illustrator, IDE, tarayıcı, emülatör ya da Docker aynı anda açık | 16 GB, 2 SODIMM yuvası | **32 GB** (2×16, çift kanal) |
| Depolama | Oyunlar, tasarım arşivi, SDK'lar | 512 GB + boş M.2 yuvası | **1 TB**, ikinci M.2 yuvası |
| GPU | 1080p/1200p oyun, Adobe GPU hızlandırma, CUDA | RTX 5050 | **RTX 5060, 100–115 W** |
| Ekran | Tasarımda renk doğruluğu | **%95+ sRGB** (ilan veya ölçüm) | %100 sRGB, 16:10 |
| Güvenilirlik | Kullanıcının ana şartı | Belgelenmiş, yaygın bir arıza kümesi olmaması | Türkiye'de iyi servis |
| Fiyat | "Makul olsun, abartılı olmasın" | — | 65–100 bin TL (2026-10 TR piyasası) |

## 2. Teknik doğrulama: ilan mı, ölçüm mü?

Her satırda değerin türü yazıyor. **Ölçüm** bağımsız laboratuvar sonucu, **İlan** üretici veya satıcı beyanı demek. Bir SKU'nun ölçümü başka bir SKU'ya aktarılmadı.

| Önerilen TR SKU | Nesil | Panel (değer türü) | GPU gücü (kaynak) | RAM / M.2 (kaynak) | Bu SKU için bağımsız ölçüm |
|---|---|---|---|---|---|
| Lenovo Legion 5 **15IRX10 83LY00PYTR** | Gen 10 (2025), Intel i7-13650HX | 15,3" 1920×1200 IPS 165 Hz 300 nit **%100 sRGB (İlan:** PSREF 10.06.2026 + epey ilanı) | RTX 5060 **115 W TGP** (PSREF) | 2 SODIMM çift kanal, PSREF'e göre "32 GB'a kadar"; **2× M.2 2280 PCIe4 x4** (PSREF) | **Yok.** Notebookcheck'in Legion 5 ölçümleri 15AHP10 AMD/OLED ve 15IRX10 RTX 5070/OLED cihazlarında yapıldı; aktarılmadı |
| Casper Excalibur **G915.1362-DF60X-C** | 2025/26, Intel i7-13620H (H serisi) | 16" 1920×1200 165 Hz 350 nit **%100 sRGB (İlan:** casper.com.tr + epey) | RTX 5060 **115 W** (İlan) | 2 SODIMM, 2 M.2 (İlan: epey/casper) | **Yok** |
| Lenovo LOQ **15AHP10 83JG008UTRv1** | 2025, Ryzen 7 250 | 15,6" FHD IPS 144 Hz 300 nit **%100 sRGB (İlan:** PSREF 15AHP10, 24.08.2026 + epey) | RTX 5060 **100 W TGP** (PSREF) | 1×16 GB takılı, 2 SODIMM, PSREF'e göre "32 GB'a kadar"; 2× M.2 2280 (**biri x4, biri x2**) (PSREF) | **Yok.** Notebookcheck'in LOQ incelemesi 2026 model **15AHP11** üzerine; aktarılmadı |
| Acer Nitro 16S AI **AN16S-61 (NH.QXUEY.001-32)** | 2025, Ryzen AI 9 365 | 16" 2560×1600 180 Hz 400 nit **%100 sRGB (İlan:** epey) | RTX 5060 115 W (İlan) | 32 GB; lehimli mi SODIMM mi **bilinmiyor**. Kodun sonundaki "-32", RAM'in satıcı tarafından yükseltildiğini düşündürüyor ama **doğrulanmadı** | **Yok** |
| HP OMEN 16-**ap0010nt (CE2B2EA)** | 2025, Ryzen AI 7 350 | 16" 1920×1200 144 Hz 300 nit **%62,5 sRGB (İlan:** epey; HP beyanı) | Bu SKU için TGP beyanı bulunamadı | 2×16 GB (İlan: epey) | **Yok.** Notebookcheck, farklı SKU **ap0091ng**'de (Ryzen 9 8940HX) AUODBB2 panelini **%58,1 sRGB** ölçtü; GPU 115 W (25 W Dynamic Boost dahil) |

## 3. OMEN: Ekşi Sözlük ve Reddit'te ne deniyor?

### 3.1 Erişim ve kapsam (önce okuyun)
- **Reddit:** reddit.com bu bulut ortamından 403 veriyor. Gönderiler, herkese açık bir Reddit arşivi olan **Arctic Shift API** üzerinden alındı.
  - **Metadata:** 8 sorguda **614 ham kayıt** geldi; bunlar **585 farklı URL ve 29 tekrar** demek. Depodaki [`kanit/reddit-arctic-shift-ozet.json`](kanit/reddit-arctic-shift-ozet.json) dosyasında **yalnızca metadata** var: başlık, tarih, puan, yorum sayısı ve URL. Gönderi gövdesi ve yorum metni bu dosyada yok.
  - **Tam metin:** Gövde ve yorum metni yalnızca raporda atıf yapılan **11 gönderi** için okundu (yenileme: 2026-10-03 13:29 UTC). Her gönderi için gövde ve yorum SHA-256 değerleri ile en fazla 180 karakterlik kısa alıntı [`kanit/reddit-alinti-kaniti.json`](kanit/reddit-alinti-kaniti.json) dosyasında. **614 kaydın hepsi tam metin olarak incelenmedi.**
  - **Yanlılık:** Sorgu terimleri sorun odaklı seçildi ("hall sensor", "hinge" gibi). Bu yüzden örneklem olumsuza yanlı ve **bir arıza oranı vermiyor**.
- **Ekşi Sözlük:** Her yoldan 403 alındı ve Wayback arşivinde kopya yok. Ekşi maddeleri **yalnızca arama motoru özetine dayanıyor**.
- **HP Support Community:** 403 verdi; yalnızca arama özeti var. **Şikayetvar** sayfaları curl ile açılıp okundu.

### 3.2 Reddit: atıf yapılan gönderiler (tam metin okundu; 10'u OMEN, 1'i Acer için §4'te)
Hepsi **anekdot**. Model ve nesil her maddede belirtildi.

**Sorun bildirenler:**
- **OMEN 16 2023 wf, RTX 4060** (Hindistan): 3 ayda Hall sensörü arızası, toplam 4 anakart değişimi, servis hasarı. Aynı başlıkta 50 yorum çekildi; içlerinde "6 yıldır OMEN 2020 sorunsuz" diyen karşı anekdotlar da var. — https://www.reddit.com/r/HPOmen/comments/1wk5wdn/indians_dont_buy_hp_omen/
- **OMEN MAX 16 (2025, Arrow Lake HX + RTX 50):** Isınınca siyah ekran oluyor; anakart ve panel değiştirilmiş. Servis portalındaki durum metni Türkçe. Değişimden sonra kullanıcının 33 dakikalık stres testinde sorun tekrarlamamış. — https://www.reddit.com/r/HPOmen/comments/1w3dcrt/ · https://www.reddit.com/r/HPOmen/comments/1w6emm6/
- **OMEN 16 (alt seri belirtilmemiş):** Menteşe kırılmış, panel ayrılmış; servis süreci sorunlu (Hindistan). — https://www.reddit.com/r/HPOmen/comments/1vwus34/
- **OMEN 16 2023:** Ekran titriyor, siyah ekran oluyor, Hall şüphesi; yorumlarda kullanıcının kendi onarımı anlatılıyor. — https://www.reddit.com/r/HPOmen/comments/1wtdlc6/hall_sensor_or_different_issue_omen_16_2023/
- **OMEN 6800H + RTX 3070 Ti, 2 yıl:** Performans iyi ama sorun çok yaşamış; uzatılmış garanti tavsiye ediyor. — https://www.reddit.com/r/HPOmen/comments/1v3bt5v/

**Memnun olanlar:**
- **OMEN 16-xd0020AX, 1 yıl:** Yapı kalitesini övüyor, menteşeyi eleştiriyor. — https://www.reddit.com/r/HPOmen/comments/1u2brki/
- **RTX 5050'li OMEN (am0277TX):** Yorumlarda "sıfır sorun" anekdotları. — https://www.reddit.com/r/HPOmen/comments/1veovdy/
- **OMEN 16-am0073dx sorusu:** 10 aylık bir sahibi "light bleed dışında sorun yok" diyor. "Hall sorunu 2024 sonrası düzeldi" iddiası da var ama **doğrulanmadı**. — https://www.reddit.com/r/HPOmen/comments/1wugasl/
- **OMEN 16 (Ryzen AI 7 350/5060) mı, LOQ mu?** Tek yanıt: "Legion al, RAM ekle." Tek bir görüş. — https://www.reddit.com/r/HPOmen/comments/1vb7crp/

**Sayım uyarısı:** Metadata'ya göre Eylül 2026'da r/HPOmen'de onlarca siyah ekran, titreme ve Hall başlığı açılmış; bunların çoğu 2023–2024 modelleri. Bu yalnızca **başlık sayısı**, oran değil.

### 3.3 Ekşi Sözlük (yalnızca arama özeti)
- OMEN dizüstüler için "felaket laptop" diyen bir yazar. — https://eksisozluk.com/entry/70654268
- Isınma şikâyeti (GPU 80 °C, CPU 90 °C). — https://eksisozluk.com/entry/99930815
- OMEN 15-ek1008nt günlük kullanımda serin ve sessiz (olumlu). — https://eksisozluk.com/hp-omen-15-ek1008nt--7422951
- OMEN 15 2020, ilk haftalar sorunsuz. — https://eksisozluk.com/hp-omen-15-2020--6667691
- Ana başlık: https://eksisozluk.com/hp-omen--4677836

Bu maddeler eski OMEN 15 nesline ait; güncel 2025 OMEN 16'ya aktarılamaz.

### 3.4 Şikayetvar (sayfalar okundu) ve HP forumu (arama özeti)
| Konu | Model / yıl | Kanıt türü | Kaynak |
|---|---|---|---|
| Hall sensörü | OMEN 16 wf (2023), xd (2024) | Şikâyet anekdotu (Şikayetvar) | [1](https://www.sikayetvar.com/hp/hp-omen-dizustu-bilgisayarda-kronik-donanim-arizasi-ve-yetersiz-teknik-destek-sureci), [2](https://www.sikayetvar.com/hp/hp-omen-16-modelinde-hall-sensor-arizasi-icin-ucretsiz-onarim-talebi), [3](https://www.sikayetvar.com/hp/hp-omen-16-hall-sensor-arizasi-garanti-kapsaminda-cozum-bekliyor) |
| Anakart / siyah ekran | OMEN 16 (alt seri çoğunlukla belirtilmemiş) | Şikâyet anekdotu | [Şikayetvar](https://www.sikayetvar.com/hp/hp-omen-16-bilgisayarinda-tekrarlanan-anakart-arizasi-ve-degisim-istegi), [Şikayetvar](https://www.sikayetvar.com/hp/hp-omen-16-ana-kart-arizasi-ve-garanti-degisimi-reddi), [HP forum, arama özeti](https://h30434.www3.hp.com/t5/Gaming-Notebooks/HP-Omen-16-Motherboard-issues/td-p/9278296) |
| Menteşe; servisin "kullanıcı hatası" deyip ücret istemesi | OMEN 16-wd0006NT | Şikâyet anekdotu | [Şikayetvar](https://www.sikayetvar.com/hp/hp-omen-laptop-mentese-kirilmasi-kullanici-hatasi-iddiasi-ve-haksiz-ucret-talebi) |
| Isınma ve ani kapanma | OMEN MAX 16 ah0001NT | Şikâyet anekdotu | [Şikayetvar](https://www.sikayetvar.com/hp/hp-omen-max-laptopda-tekrarlayan-asiri-isinma-ve-ani-kapanma-problemi) |
| BIOS güncellemesinde takılma | OMEN 16 | Şikâyet anekdotu | [Şikayetvar](https://www.sikayetvar.com/hp/hp-omen-16-bios-guncellemesi-takildi-acil-destek-gerekiyor) |
| Toplam | HP OMEN | Sayı (oran değil) | [Şikayetvar HP OMEN](https://www.sikayetvar.com/hp/omen) |

### 3.5 Hüküm
- **Kanıtla söylenebilen:** 2023–2024 OMEN 16 (wf/xf/xd) için Hall sensörü, siyah ekran ve anakart anekdotları birden çok ülkede ve platformda tekrarlanıyor. 2025 OMEN MAX 16'da da Türkiye'den en az bir vaka var.
- **Söylenemeyen:** Arıza **oranı**. 2025 OMEN 16 ap/am serisinin güvenilirliği bu kaynaklardan ölçülemez. Elimizdeki az sayıdaki anekdotun çoğu olumlu ama süre de kısa.
- **"Kronik arızalı mal" mı?** Bu kaynaklarla **ne kanıtlanabiliyor ne çürütülebiliyor**. Eski nesilde belirgin bir şikâyet kümesi var; yeni nesil için veri yetersiz.

## 4. Alternatifler: kronik sorunlar ve olumlu deneyimler

Tablodakiler şikâyet **sayısı** ve **anekdottur**; bir oran değildir. Marka servis notu Şikayetvar'ın kendi ölçütüdür.

| Model (nesil) | Şikâyet / anekdot | Olumlu | Türkiye servisi (Şikayetvar) |
|---|---|---|---|
| Lenovo Legion 5 Gen 10 | dGPU modunda ekran glitch'i raporları ([Lenovo forum başlığı](https://gaming.lenovo.com/emea/members/360486-mind12), yalnızca başlık görüldü); oyunda fan sesi (Notebookcheck, AMD/OLED test cihazı) | Notebookcheck 15AHP10 incelemesi olumlu (%93,6; farklı SKU) | Lenovo 6/100; [Legion: 139 şikâyet](https://www.sikayetvar.com/lenovo/legion) |
| Lenovo LOQ (genel) | 170 W adaptör şikâyetleri, ekran hasarına "kullanıcı hatası" denmesi ([LOQ: 327 şikâyet](https://www.sikayetvar.com/lenovo/loq)); Reddit'te 15IAX9 için "GPU short" başlığı (eski nesil) | Soğutma ve klavye övülüyor (anekdot ve inceleme) | Lenovo 6/100 |
| **Casper Excalibur G915** | [G915 sayfası: 11 şikâyet](https://www.sikayetvar.com/casper/excalibur-g915): GPU artifact, ~1 ayda açılmama. Yeni model, küçük örneklem. **G870'in ısınma şikâyetleri G915'e aktarılmadı** | Bir teşekkür kaydı | **Casper 76/100**, listedeki en yüksek not ([sayfa](https://www.sikayetvar.com/casper)) |
| Acer Nitro (V/16S) | Şarjdayken pil düşmesi, ısınma, mavi ekran ([Nitro: 286](https://www.sikayetvar.com/acer/nitro)) | Fiyat/performans; Reddit'te Nitro Lite 16 için memnuniyet anekdotu ([gönderi](https://www.reddit.com/r/GamingLaptops/comments/1wusru5/)) | Acer 2/100 ([sayfa](https://www.sikayetvar.com/acer)) |
| ASUS TUF A16/F16 | Pildeyken fan yükselmesi (ROG forum, arama özeti) | Pil, kasa | ASUS ([TUF arama](https://www.sikayetvar.com/asus?k=tuf)); bu sınıfta TR fiyatı yüksek |
| MSI Katana/Cyborg | Donma, mavi ekran, menteşe ([Katana](https://www.sikayetvar.com/msi-turkiye?k=katana)); Cyborg 15'te 45 W GPU ve %64,8 sRGB (ölçüm, Notebookcheck) | — | Karışık |
| Monster Abra/Tulpar | Menteşe, şarj soketi, anakart ([Tulpar T7](https://www.sikayetvar.com/monster-notebook/tulpar-t7), [Abra A5](https://www.sikayetvar.com/monster-notebook/abra-a5)) | 4 yıl garanti | Monster 14/100 |

## 5. Güncel fiyatlar (Türkiye, 03.10.2026, epey.com pazaryeri listeleri)

Kilit sayfalar bu turda **yeniden açıldı**; satıcılar ve fiyatlar epey'in "x dakika/saat önce güncellendi" bilgisiyle birlikte alındı. Ham anlık görüntü: [`kanit/fiyat-anlik-2026-10-03.json`](kanit/fiyat-anlik-2026-10-03.json).

| Model (tam kod) | Yapılandırma (ilan) | En düşük | Satıcı | Bağlantı |
|---|---|---|---|---|
| Lenovo Legion 5 15IRX10 **83LY00PYTR** | i7-13650HX · RTX 5060 · 32 GB · 1 TB · 15,3" 1200p 165 Hz %100 sRGB · FreeDOS | **99.999 TL** | Nethouse (HB/Amazon/Trendyol/n11) | https://www.epey.com/laptop/lenovo-legion-5-15irx10-83ly00pytr.html |
| Casper Excalibur **G915.1362-DF60X-C** | i7-13620H · RTX 5060 115 W · 32 GB · 1 TB · 16" 1200p 165 Hz %100 sRGB · FreeDOS | **93.599 TL** | Casper Excalibur (n11) | https://www.epey.com/laptop/casper-excalibur-g915-1362-df60x-c.html |
| Acer Nitro 16S AI AN16S-61 **NH.QXUEY.001-32** | Ryzen AI 9 365 · RTX 5060 115 W · 32 GB · 1 TB · 16" 1600p 180 Hz · FreeDOS | **92.999 TL** | Teknorya (HB) | https://www.epey.com/laptop/acer-nitro-16s-ai-an16s-61-nh-qxuey-001-32.html |
| HP OMEN 16-**ap0010nt (CE2B2EA)** | Ryzen AI 7 350 · RTX 5060 · 32 GB · 1 TB · 16" 1200p 144 Hz **%62,5 sRGB** · FreeDOS | **84.149,99 TL** | Kapında (Trendyol) | https://www.epey.com/laptop/hp-omen-gaming-16-ap0010nt-ce2b2ea.html |
| Lenovo LOQ 15AHP10 **83JG008UTRv1** | Ryzen 7 250 · RTX 5060 · 16 GB (1×16) · 2 TB · 15,6" FHD 144 Hz %100 sRGB · FreeDOS | **73.799 TL** | Betaplus (n11) | https://www.epey.com/laptop/lenovo-loq-15ahp10-83jg008utrv1.html |
| HP Victus 15 **C21SGEA** (epey'de sayfa başlığı "15-fa2019nt", URL ve başlıkta "15-fa2018nt") | Core 5 210H · RTX 5060 · 16 GB · 1 TB · 15,6" FHD 144 Hz %62,5 sRGB | **55.692,55 TL** | KapındaSepet (Trendyol) | https://www.epey.com/laptop/hp-victus-gaming-15-fa2018nt-c21sgea.html |
| Acer Nitro V 16S AI **ANV16S-41-R481** *(1. tur verisi, bu turda yeniden açılmadı)* | Ryzen 7 260 · RTX 5060 · 16 GB · 512 GB · 16" 1600p 180 Hz %100 sRGB | 64.859 TL | Adlertech (Idefix) | https://www.epey.com/laptop/acer-nitro-v-16s-ai-anv16s-41-r481.html |
| HP OMEN 16-**ap0032nt (CA8E9EA)** *(1. tur, alt ajan verisi)* | Ryzen 9 8940HX · RTX 5070 · 32 GB · 1 TB · QHD+ 240 Hz 500 nit | 123.048 TL | VPBilişim (HB) | https://www.epey.com/laptop/hp-omen-gaming-16-ap0032nt-ca8e9ea.html |

**Uyarılar:**
- Bunlar pazaryeri satıcılarının fiyatları. Distribütör garantisini ve faturalı satışı ayrıca kontrol edin.
- Victus'ta model kodu eşleşmesi epey'in kendi sayfasında bile tutarsız; kesin eşleşme için **C21SGEA** ürün numarası esas alınmalı.
- Fiyatlar anlık; stok ve kampanyayla değişir.

## 6. Karar

### 6.1 Neden sayısal puan yok?
1. turdaki 100 üzerinden ağırlıklı puanlama **kaldırıldı**. Güvenilirlik sütunu, paydası olmayan anekdotlara dayanıyordu; "2,5/5" gibi bir puan kesinlik izlenimi veriyordu. Ekran puanları da kısmen başka SKU'ların ölçümlerine dayanıyordu. Bunun yerine her ölçütün **kanıt türünü** gösteren bir matris ve **koşullu** öneriler var.

### 6.2 Kanıt matrisi
| Ölçüt | Legion 5 83LY00PYTR | Casper G915.1362-DF60X-C | LOQ 15AHP10 83JG008UTRv1 | Nitro 16S NH.QXUEY.001-32 | OMEN 16-ap0010nt |
|---|---|---|---|---|---|
| Tasarım için ekran | %100 sRGB · İlan | %100 sRGB · İlan | %100 sRGB · İlan | %100 sRGB · İlan | **%62,5 sRGB · İlan** (aynı tip panel farklı SKU'da %58,1 ölçüldü) |
| 32 GB kutudan | Evet | Evet | Hayır (16 GB, 1 yuva boş) | Evet (satıcı yükseltmesi olabilir) | Evet |
| 2. M.2 yuvası | Var (x4) · PSREF | Var · İlan | Var (x2) · PSREF | Bilinmiyor | Doğrulanamadı |
| GPU gücü | 115 W · PSREF | 115 W · İlan | 100 W · PSREF | 115 W · İlan | Bu SKU için bilinmiyor |
| Topluluk şikâyeti | Az sayıda glitch raporu (anekdot) | 11 şikâyet, yeni model (anekdot) | LOQ genelinde adaptör/servis (anekdot) | Acer servisi zayıf (not) | Eski nesilde belirgin küme; ap/am için veri yetersiz |
| TR servis notu | 6/100 | **76/100** | 6/100 | 2/100 | Şikâyet yoğun (HP OMEN sayfası) |
| Fiyat | 99.999 TL | 93.599 TL | 73.799 TL | 92.999 TL | 84.150 TL |

### 6.3 Koşullu öneriler
- **Ekran doğruluğu, 32 GB ve genişleme öncelikliyse:** **Legion 5 15IRX10 83LY00PYTR** (99.999 TL). Gerekçe: PSREF'te 115 W GPU, iki x4 M.2 yuvası, çift kanal RAM ve %100 sRGB panel seçeneği var. Belirsizlik: bu SKU'nun paneli bağımsız olarak ölçülmedi. Lenovo'nun Türkiye servis notu düşük.
- **Servis itibarı öncelikliyse:** **Casper G915.1362-DF60X-C** (93.599 TL). Belirsizlik: hiçbir bağımsız ölçüm yok. İşlemci H serisi. G915'e özgü 11 şikâyet var ama örneklem küçük.
- **Bütçe öncelikliyse:** **LOQ 15AHP10 83JG008UTRv1** (73.799 TL), artı 16 GB DDR5-5600 SO-DIMM. Modül fiyatı araştırılmadı. Belirsizlikler: GPU 100 W; ikinci M.2 yuvası x2 hızında; panel ölçülmedi.
- **En geniş ekran isteniyorsa:** **Nitro 16S** (92.999 TL). Belirsizlikler: RAM'in satıcı yükseltmesi olma ihtimali, slot yapısı ve Acer'in düşük servis notu.
- **OMEN illa isteniyorsa:** ap0010nt yerine %100 sRGB panelli bir OMEN SKU'su aranmalı. Notebookcheck, HP'nin seride 1600p %100 sRGB seçenekler sunduğunu yazıyor; örneğin 1. tur verisindeki **ap0032nt**, 123.048 TL. Bu durumda bütçe aşılır, yanına HP Care Pack düşünülmeli.

### 6.4 Satın almadan önce kullanıcının kendisinin yapması gereken kontroller
Bu adımların **hiçbiri bu çalışmada yapılmadı**:
1. Kutu veya etiket üzerinde tam ürün kodunu (83LY00PYTR / G915.1362-DF60X-C / 83JG008UTRv1) ve panel satırında "%100 sRGB" yazdığını görmek.
2. Faturalı satış ve distribütör garantisi (Türkiye resmî garantisi) olduğunu teyit etmek.
3. Teslimde ölü piksel, ışık sızması ve menteşe kontrolü yapmak. Garanti süresinde BIOS ve sürücü güncellemelerini takip etmek.

## 7. Yapılamayanlar ve sınırlar
- **Ekşi Sözlük okunamadı** (403, arşivde kopya yok). Ekşi bölümü yalnızca arama özetine dayanıyor.
- **Reddit:** Tam metin yalnızca 11 gönderi için okundu. 585 benzersiz kaydın geri kalanı yalnızca metadata olarak var. Bazı geniş sorgular arşivin hız sınırına takıldı.
- **Bağımsız ölçüm:** Önerilen dört SKU'nun hiçbiri için bağımsız ölçüm bulunamadı. Panel ve GPU değerleri üretici ya da satıcı ilanı.
- **Arıza oranı:** Hiçbir model için kamuya açık bir arıza oranı yok. Şikâyet sayıları satış hacmine bağlı ve olumsuz yönde yanlı.
- **Bağlantı testi:** `test/dogrula.py` bağlantıların **HTTPS biçiminde** olup olmadığını ve HTTP durum kodlarını kontrol eder.
  - 403 yanıtı sayfanın canlı olduğunu kanıtlamaz; bot koruması içeriği gizler.
  - Bağlantı testi **olgusal doğruluk testi değildir**. Olgusal kontrol, kaynakların bu oturumda elle okunmasıyla yapıldı (bkz. `kanit/birincil-kaynak-okuma.md`).
- **Fiziksel test, Windows benchmark'ı ve satın alma yapılmadı.** Kullanıcı tarafında da yapılmış sayılmadı.

## 8. Düzeltme kaydı (1. tur commit `279251a9ab8b2426881a91ad756427494aaf7090` → 2. tur)
| 1. turdaki hata | Düzeltme |
|---|---|
| "614 gönderi" ve "gerçek metinden özet" | 614 ham kayıt = 585 benzersiz URL + 29 tekrar. JSON dosyası yalnızca metadata; tam metin yalnızca 11 atıflı gönderi için okundu ve SHA-256 ile kayıtlı |
| Hall anekdotlarından OMEN'e "2,5/5 güvenilirlik" puanı ve kesin sıralama | Sayısal puan ve kesin sıralama kaldırıldı; anekdotların 2025 ap/am için oran olmadığı yazıldı |
| ap0091ng'nin %58 ölçümü TR ap0010nt'ye mal edilmişti | TR SKU için ilan değeri %62,5; ölçümün farklı SKU'ya ait olduğu her yerde belirtildi |
| "115+25 W" | 115 W, 25 W Dynamic Boost dahil (Notebookcheck metni) |
| Legion 15AHP10 AMD/OLED ölçümü önerilen 15IRX10 Intel/IPS'e uygulanmıştı | Aktarım kaldırıldı; önerilen SKU için kaynak PSREF 15IRX10 ve ilan |
| LOQ için 15IRX10 PSREF ve 15AHP11 ölçümü kullanılmıştı | LOQ 15AHP10 PSREF'i (24.08.2026) okundu; 15AHP11 ölçümü aktarılmadı |
| Casper'da G870 şikâyetleri G915'e yüklenmişti | G915'in kendi Şikayetvar sayfası okundu (11 şikâyet); G870 ayrıldı |
| Victus fa2019nt / fa2018nt karışıklığı | Ürün numarası C21SGEA esas alındı, tutarsızlık açıkça yazıldı; fiyat yenilendi |
| Legion için "64 GB'a kadar" | PSREF: "Up to 32GB offering" |
| "403 = erişilebilir bağlantı" izlenimi | Bağlantı testi ile olgusal doğrulama ayrıldı |
