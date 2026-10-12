# Creamaxi

Durum: Site içeriğine erişilemedi. 12 Ekim 2026'da `curl` (www.creamaxi.com ve creamaxi.com) ajan proxy'sinden "CONNECT tunnel failed, response 502" (organizasyon politikası/ulaşılamıyor), WebFetch ise "getaddrinfo ENOTFOUND www.creamaxi.com" döndü; WebSearch ("creamaxi.com Creamaxi ajans") siteye veya ajansa ait hiçbir sonuç vermedi. Mevcut ekran görüntüleri tamamen siyah. Aşağıdaki alanlarda veri olmadığı için "bulunamadı" yazıldı, hiçbir şey uydurulmadı.

- **Ülke / şehir:** Türkiye (görev notunda Türk ajansı olarak verildi); şehir bulunamadı
- **Web:** https://www.creamaxi.com/ (erişilemedi)
- **Instagram:** bulunamadı (sitenin HTML'ine erişilemediği için link çıkarılamadı; WebSearch'te çıkmadı; `ss/` altında Instagram verisi yok)
- **Diğer sosyal:** Facebook: bulunamadı | LinkedIn: bulunamadı | TikTok: bulunamadı | YouTube: bulunamadı
- **Kuruluş / ekip büyüklüğü (biliniyorsa):** bulunamadı
- **Hedef müşteri:** bulunamadı (site içeriği görülemedi, bu yüzden bir varsayım yapılmadı)

## Hizmetler
- bulunamadı (site erişilemedi)

## Paketler ve fiyatlar
| Paket | Kapsam | Fiyat | Not |
|---|---|---|---|
| bulunamadı | bulunamadı | bulunamadı (fiyat/teklif akışı görülemedi) | kaynak: https://www.creamaxi.com/ erişilemedi; WebSearch'te fiyat bilgisi çıkmadı |

## Site yapısı (ana sayfa, yukarıdan aşağıya sırayla)
1. Hero: bulunamadı. `ss/creamaxi-web.png` (1440x900) ve `ss/creamaxi-mobil.png` (390x844) Read ile açıldı: ikisi de düz, koyu (#0a0a0a) renkte tamamen boş; metin, logo, menü, görsel yok. Görev notunda site "ağır animasyonlu" olarak tarif edildi; boş ekranın sebebi (açılış/yükleme animasyonu, JS gecikmesi, engelleme) doğrulanamadı.
2. Diğer bölümler: bulunamadı (sıra çıkarılamadı)
- **Dil / ton:** bulunamadı
- **CTA metinleri:** bulunamadı
- **İletişim yöntemi:** bulunamadı (WhatsApp, form, telefon, takvim; hiçbiri görülemedi)
- **Teknoloji ipuçları:** bulunamadı (kaynak koda erişilemedi; SS'in düz siyah olması yalnızca içeriğin bu ortamda render edilmediğini gösteriyor)
- **Güven unsurları:** bulunamadı

## Instagram
- **Takipçi:** bulunamadı | **Gönderi:** bulunamadı | **Takip:** bulunamadı
- **Bio metni:** bulunamadı
- **Link-in-bio:** bulunamadı
- **Post türleri (son 12-15 gönderi):** bulunamadı
- **Öne çıkan gönderi örnekleri:** bulunamadı
- **Paylaşım sıklığı / tutarlılık:** bulunamadı

## Portföy örnekleri
- bulunamadı

## İyi yaptıkları (3+)
Şablondaki "3+" kuralı karşılanamadı: görülen tek veri tamamen siyah SS'ler ve hiçbir içerik görülemediği için doğrulanabilir bir iyi uygulama yazılmadı (uydurma yok).
1. bulunamadı (içerik görülemedi)
2. bulunamadı (içerik görülemedi)
3. bulunamadı (içerik görülemedi)

## Kötü yaptıkları / açıkları (3+)
Bunlar yalnızca bu oturumda doğrulanabilen gözlemlerdir; siteyi gerçek bir ziyaretçinin gördüğü haliyle yargılamak için yeterli değildir.
1. Ekran görüntüsü aracı hem masaüstü hem mobilde tamamen siyah/boş bir ekran aldı (`ss/creamaxi-web.png`, `ss/creamaxi-mobil.png`). Ağır animasyon veya yükleme ekranı varsa, JS'i geç çalışan/yavaş bağlantıdaki ziyaretçi ve botlar da içeriği göremiyor olabilir; sebep doğrulanamadı.
2. Arama görünürlüğü doğrulanamadı: WebSearch'te "creamaxi.com Creamaxi ajans" için siteye ait hiçbir sonuç çıkmadı (araç yalnızca ABD sonuçlarıyla sınırlı olduğu için bu tek başına güçlü bir kanıt değil).
3. Dijital ayak izi doğrulanamadı: Instagram, sosyal hesaplar, iletişim yöntemi, fiyat ve portföy hiçbir kaynakta bulunamadı; bu ajansı bir rakip olarak değerlendirmek için yeterli veri yok.

## Ekran görüntüleri
- Web: `ss/creamaxi-web.png` (düz siyah, içerik yok) | Mobil: `ss/creamaxi-mobil.png` (düz siyah, içerik yok) | Instagram: SS alınamadı: Instagram kullanıcı adı bulunamadı

## Bizim için notlar
- Çalınacak fikir: Yok (içerik görülemediği için). Yeniden denenebilir: `ss_al.py`'nin `--reduced-motion` ve `--bekle SN` bayraklarıyla (OZET.md'de bahsedilen) ve proxy kısıtı olmayan bir ortamdan siteyi tekrar çekmek; o zaman bu not güncellenmeli.
- Kaçınılacak: Açılış animasyonu veya yükleme ekranı yüzünden içeriğin ilk saniyelerde hiç görünmemesi; kendi sitemizde ilk metin/başlık animasyondan bağımsız (statik HTML) gelmeli, JS gecikirse bile okunmalı.
