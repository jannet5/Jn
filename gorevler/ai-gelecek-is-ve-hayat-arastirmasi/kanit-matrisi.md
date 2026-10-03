# Kanıt Matrisi: Olan ile Olacağı Söylenen

Bu tablo, raporun temel iddialarını **kanıtlanmış gelişmeler** ve **tahminler** olarak
ayırır. Kaynak kodları [`kaynaklar.md`](kaynaklar.md) dosyasındaki numaralardır.
`dogrulama/kontrol.py` her satırın geçerli bir sınıf ve en az bir mevcut kaynak
kodu taşıdığını otomatik kontrol eder.

Sınıflar: **KANIT** · **ERKEN-SINYAL** · **TAHMIN** · **TOPLULUK** · **DOGRULANMAMIS**

| ID | İddia | Sınıf | Kaynak | Not |
|---|---|---|---|---|
| K01 | Claude.ai sohbetlerinin %35'i bilgisayar/matematik meslek görevleriyle ilgili (Mart 2026) | KANIT | R6 | Tek şirket platformu |
| K02 | ABD'de ChatGPT sonrası genel istihdamda fark edilebilir bozulma yok | KANIT | R8, R1 | Yale + Stanford uyumlu |
| K03 | Yapay zekâya en açık mesleklerde 22–25 yaş istihdamı beklenenin %19 altında | ERKEN-SINYAL | R1 | Yazarlar nedensel değil diyor |
| K04 | Düşüş işten çıkarmadan değil, daha az işe alımdan; ücretler değişmiyor | ERKEN-SINYAL | R1 | |
| K05 | İsveç'te benzer yaş eğimi (%5,5, 2025 başı) | ERKEN-SINYAL | T10 | Çalışma doğrudan açılmadı |
| K06 | Dünyada çalışanların 4'te 1'i üretken yapay zekâya maruz; en yüksek grupta %3,3 | KANIT | R2 | Maruziyet ≠ kayıp |
| K07 | Kadınlar en yüksek maruziyet grubunda daha fazla (%4,7 / %2,4) | KANIT | R2 | |
| K08 | 2025 başında yapay zekâya bağlanan çıkarmalar toplamın %4,5'i | KANIT | R8 | Oxford Economics |
| K09 | Klarna yapay zekâ müşteri hizmetinden sonra yeniden insan alıyor | KANIT | R17 | |
| K10 | Deneyimli geliştiriciler 2025 başı araçlarla %19 daha yavaş çalıştı | KANIT | R7 | 16 kişi; dar kapsam |
| K11 | Waymo 14 ABD şehrinde, haftada ~500 bin ücretli yolculuk | KANIT | R20, R21 | |
| K12 | Ev insansı robotları bilmediği işte uzaktan insana dayanıyor | KANIT | R22 | |
| K13 | ABD'de elektrikçi istihdamı 2025–35'te %9 artacak; medyan 63.190 $ | KANIT | R4 | Resmî projeksiyon |
| K14 | Tesisatçı istihdamı 2024–34'te %5 artacak | KANIT | R5 | Resmî projeksiyon |
| K15 | NECA: sendikalı tarafta yılda ~20 bin elektrikçi kaybı, ~80 bin açık | KANIT | R19 | Sektör birliği beyanı |
| K16 | Türkiye'de kaynakçı, sıvacı, şoför gibi el işlerinde eleman temini zor (2024) | KANIT | R10 | İŞKUR |
| K17 | Elektrik tesisatçısı dahil tehlikeli işlerde MYK belgesi zorunlu; ustalık belgesi muaf | KANIT | R11 | |
| K18 | TÜİK Temmuz 2026: işsizlik %8,1, genç %14,5, atıl işgücü %30,6 | KANIT | R9 | Yapay zekâyla ilişkisi gösterilmedi |
| K19 | 2030'a kadar 170M iş yaratılıp 92M yok olacak, net +78M | TAHMIN | R3 | İşveren anketi |
| K20 | Mevcut becerilerin %39'u 2030'a kadar değişecek | TAHMIN | R3 | |
| K21 | Büro işleri, sekreterlik, muhasebe en hızlı azalacaklar arasında | TAHMIN | R3 | |
| K22 | Giriş seviyesi beyaz yaka işlerin yarısı 1–5 yılda silinebilir; işsizlik %10–20 | TAHMIN | R12 | Amodei |
| K23 | "Sıradan zihinsel emekte yapay zekâ herkesin yerini alacak"; tesisatçılık iyi tercih | TAHMIN | R13 | Hinton |
| K24 | Zanaatkârlar "altı haneli maaşlar" isteyebilecek; "sizin zamanınız" | TAHMIN | R14, R15 | Huang; çıkar çatışması notu |
| K25 | Junior yazılımcı sorunu iki yılda biter | TAHMIN | R16, T7 | Huang |
| K26 | İnşaat iş gücünün %41'i 2031'e kadar emekli olacak | TAHMIN | R18 | Deloitte projeksiyonu |
| K27 | Baumol etkisiyle emek yoğun hizmetlerin göreli fiyatı yükselebilir | TAHMIN | R23 | Teori sağlam, uygulaması tahmin |
| K28 | Ofis çalışanlarının %62'si koşullar uygunsa mavi yakaya geçmeyi düşünür | KANIT | R24 | Anket — niyet, davranış değil |
| K29 | "Kod yazan bir X ol" tavsiyesi yazılımcılar arasında yaygın | TOPLULUK | T4, T5, T16 | |
| K30 | Juniorlardan eskinin orta seviyesi bekleniyor | TOPLULUK | T1 | |
| K31 | CS diplomalı gençler bir yılda mülakat alamıyor | TOPLULUK | T1 | Tekil deneyim |
| K32 | İşten çıkarmalar yapay zekânın başarısından çok beklentiyle yapılıyor | TOPLULUK | T1, T6 | |
| K33 | Ustalar yapay zekâyı parça/teşhis için kullanıyor ama sık hata buluyor | TOPLULUK | T11 | |
| K34 | Basit tamirleri insanlar kendisi yapar; değer deneyim ve güvende | TOPLULUK | T12 | X tartışması |
| K35 | Hiçbir iş bağışık değil; yapay zekâ kullanan usta kullanmayanı geçer | TOPLULUK | T13 | Medium |
| K36 | Veri merkezi elektrikçileri 280–300 bin $ kazanıyor | DOGRULANMAMIS | D1 | İstisnai/abartılı olabilir |
| K37 | Türkiye'de yetkili elektrikçi aylık 80–120 bin TL, tesisatçı 70–110 bin TL | DOGRULANMAMIS | D2 | Kaynak gösterilmemiş |
| K38 | Ustanın müşterisi orta sınıf; onun geliri düşerse tadilat talebi düşer | TAHMIN | R3, R1 | Mantıksal çıkarım, ölçülmedi |
