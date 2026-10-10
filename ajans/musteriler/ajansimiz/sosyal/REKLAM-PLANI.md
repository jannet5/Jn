# REKLAM PLANI: Vitrin Atölyesi (Meta) · sadece plan, API çağrısı yapılmadı

Kaynak: `ajans/sistem/hatlar/reklam.md`. Her şey **PAUSED** başlar, bütçe ve yayına alma insan onaylı.
Kreatifler: `reklam/` (her biri `-feed` 1080×1350 + `-story` 1080×1920; story'de üst ~%14 ve alt ~%20 boş bırakıldı).

## 1. Kreatif skorları (ad-score: 6 boyut, her biri 0-100; toplam = ağırlıklı ortalama)
Ağırlık: hook 25 · netlik 20 · teklif 20 · kanıt 10 · CTA 15 · marka uyumu 10. Eşik: **<70 yayına girmez.**
Puanlar bizim öz değerlendirmemiz (henüz yayın verisi yok); ilk 7 günden sonra CTR/maliyetle güncellenecek.

| Kreatif | Ana fikir | Hook | Netlik | Teklif | Kanıt | CTA | Marka | **Toplam** | Karar |
|---|---|---|---|---|---|---|---|---|---|
| r1-google-da-yoksaniz | "Müşteri sizi arıyor. Bulabiliyor mu?" | 85 | 85 | 75 | 40 | 85 | 90 | **79** | Yayına |
| r2-once-sonra | Aynı işletme, iki ilk izlenim (örnek konsept) | 80 | 75 | 65 | 70 | 80 | 90 | **76** | Yayına |
| r3-fiyat-acik | "Site + bakım aylık 2.990 ₺" | 75 | 90 | 85 | 50 | 75 | 90 | **79** | Yayına |
| r4-7-gunde | "Bir haftada yayında" + 4 adım | 70 | 85 | 60 | 55 | 80 | 90 | **73** | Yedek (sınırda) |
| r5-ilk-5 | "İlk 5 işletmeye kurulum 0 ₺" | 85 | 85 | 90 | 45 | 80 | 90 | **82** | Yayına (5 yer dolunca durdur) |

Hesap: r1 = (85·25 + 85·20 + 75·20 + 40·10 + 85·15 + 90·10)/100 = 79,0. Diğerleri aynı formül.
Zayıf boyut her yerde **kanıt** (müşteri yok). Düzeltme: ilk vaka çalışması gelince r2'yi gerçek önce/sonra ile değiştir.
Meta kuralı kontrolü: abartılı "hemen al / sınırlı süre!!" yok; "0 ₺" ve fiyat gerçek (KARARLAR.md); kişisel özellik ima eden "siz ... misiniz?" kalıbı yok. Örnek konsept etiketi r2'de görselin içinde.

### Reklam metinleri (birincil metin · başlık)
| Kreatif | Birincil metin | Başlık |
|---|---|---|
| r1 | Müşteri işletmenizi telefondan arıyor. Karşısına düzgün bir site, güncel bir Google profili ve tek dokunuşla WhatsApp çıkıyor mu? Önce görün: işletmenize özel örnek ana sayfayı ücretsiz hazırlıyoruz. | Ücretsiz örnek ana sayfa |
| r2 | Soldaki tipik eski site, sağdaki telefona göre kurulmuş sayfa (örnek konsept). Sizin işletmeniz için de çizelim, ücretsiz. | Önce görün, sonra karar verin |
| r3 | Site, barındırma, bakım, Google profili ve WhatsApp butonu aylık 2.990 ₺ (KDV hariç, kurulum ayrı). 12 ay sonra site sizin. | Fiyatlar açık |
| r4 | 1. gün görüşme, 2-3. gün üç tasarım yönü, 7. gün yayında. Yerel işletmeye site, sosyal medya ve reklam tek yerden. | 7 günde yayında |
| r5 | Yeni açıldık. İlk 5 işletmeye kurulum ücreti yok; karşılığında işinizi vaka çalışması olarak paylaşma izni istiyoruz. | İlk 5 işletmeye kurulum 0 ₺ |

## 2. Kampanya kurulumu (Marketing API alanlarıyla; hepsi PAUSED)
```
Kampanya  name: "VA | Mesaj | İstanbul ilçe | 2026-10"
          objective: OUTCOME_ENGAGEMENT          # hedef: mesaj (Instagram Direct + WhatsApp)
          special_ad_categories: []              # ajans hizmeti, özel kategori değil
          status: PAUSED
          bütçe: Advantage kampanya bütçesi KAPALI (ilk hafta reklam seti bazında kontrol)
Reklam seti  "Kadıköy-Üsküdar 5 km" / "Beşiktaş-Şişli 5 km" / "Bakırköy-Ataköy 5 km"
          optimization_goal: CONVERSATIONS ; destination_type: MESSAGING_INSTAGRAM_DIRECT_WHATSAPP (enum adını kurulumda API dokümanından doğrula; WhatsApp numarası bağlanınca; yoksa INSTAGRAM_DIRECT)
          billing_event: IMPRESSIONS ; bid_strategy: LOWEST_COST_WITHOUT_CAP
          targeting: geo_locations.custom_locations [{latitude, longitude, radius: 5, distance_unit: "kilometer"}]
                     age_min 28, age_max 60 ; locales Türkçe
                     ilgi alanı katmanı YOK ilk hafta (Advantage+ kitle açık, yalnız konum + yaş sınırı)
          placements: Advantage+ (feed + story + reels); story'ye -story kreatifi, feed'e -feed (asset customization)
          status: PAUSED
Reklam    her sette r1, r3, r5 (+ r2 ikinci dalga); hazır mesaj karşılama: "Merhaba, işletmem için örnek ana sayfa istiyorum."
          hızlı sorular: "Örnek ana sayfa istiyorum" · "Fiyatları öğrenmek istiyorum" · "Nasıl çalışıyorsunuz?"
          status: PAUSED
```
Alternatif (form toplamak istersek): `OUTCOME_LEADS` + Instant Form (3 alan: ad, işletme adı, telefon; "daha yüksek niyet" form tipi; KVKK aydınlatma linki zorunlu: `TODO`).
Neden mesaj önce: tek CTA zaten "DM'den ÖRNEK"; form, müşteri yokken soğuk geliyor; DM konuşması örnek sayfa sürecine doğrudan bağlanıyor.

### İlçe yarıçapı önerisi (İstanbul)
| Set | Merkez | Yarıçap | Neden |
|---|---|---|---|
| A | Kadıköy (Moda-Bahariye arası) | 5 km | Kafe/restoran yoğunluğu yüksek; Üsküdar'ın bir kısmını da kapsar |
| B | Şişli-Beşiktaş sınırı (Mecidiyeköy) | 5 km | Hizmet ve klinik yoğunluğu |
| C | Bakırköy | 5 km | Avrupa yakasında esnaf yoğun, A/B ile çakışmaz |
Setler birbirini kesmesin (Meta'da çakışan kitle ihalede kendine rakip olur). İlk 7 günden sonra en düşük mesaj maliyetli set kalır, ikinci ilçe eklenir.

### Günlük bütçe (3 kademe, toplam; setlere eşit bölünür)
| Kademe | Günlük | 7 gün | Ne zaman |
|---|---|---|---|
| Deneme | 150 ₺ (3 × 50 ₺) | ~1.050 ₺ | İlk hafta: hangi ilçe/kreatif mesaj getiriyor |
| Standart | 300 ₺ | ~2.100 ₺ | Mesaj maliyeti hedefin altındaysa (hedef: `TODO: ilk hafta verisiyle belirlenecek`) |
| Hızlandırma | 600 ₺ | ~4.200 ₺ | Örnek → teklif → kapanış oranı görülünce; haftada en fazla %20-30 artırarak |
Not: TL bazlı CPM ve Meta'nın asgari günlük bütçesi hesap açılınca reklam yöneticisinde kontrol edilmeli; tablo öneri, veri değil.

## 3. Onay ve takip
1. İnsan kontrol: kreatif + metin + hedefleme + bütçe → ACTIVE.
2. Haftalık: harcama, CPM, CTR, mesaj başı maliyet, mesajdan "örnek isteyen" oranı. 3 gün boyunca CTR en düşük kreatif kapatılır, kazananın yeni varyasyonu (başlık/renk zemin) eklenir.
3. r5 (ilk 5 işletme) 5 yer dolunca durdurulur; yerine r2 (gerçek önce/sonra) girer.
