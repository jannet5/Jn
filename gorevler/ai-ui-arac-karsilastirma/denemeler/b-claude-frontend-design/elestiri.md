# Ekran görüntüsü eleştiri günlüğü

Araç: Playwright (chromium, `ss.mjs`), 1440×900 ve 390×844, tam sayfa. Ek karelerde klavye odağı (1440), "Son 30 gün" durumu (1440) ve açık mobil menü (390) de çekildi. Yazı tiplerinin yüklendiği (Onest 400/500/600, Unbounded 500/600/700) ve yatay taşma olmadığı (scrollWidth = görünüm genişliği) her turda kontrol edildi; sayfa JS hatası yok.

Son görüntüler: `elestiri-1440.png`, `elestiri-390.png`.

## Tur 0 — ilk derleme (gözlem)

Masaüstü:
- Planın tuttuğu yerler: fıstık zemin + şişe yeşili menü + kavrum çubuklar, krem/terrakota kalıbından belirgin şekilde uzak. Cesaret grafikte; çubuklar ciro arttıkça koyulaşıyor, Cumartesi çini mavisi alt çizgiyle seçili. Pazar "henüz başlamadı" taramasıyla dürüst gösteriliyor. Unbounded başlık ("Bu hafta") tipin kendisini tasarım öğesi yapıyor.
- Sorun 1: Her ölçütün altında "geçen haftanın aynı günlerine göre" dört kez tekrar ediyor; aynı bilgi başlığın altında zaten var. Skill: "her yazılı öğe tek bir iş yapsın".
- Sorun 2: Lejanttaki "Koyu çubuk, daha yüksek ciro" gereksiz; renk zaten yüksekliği tekrar kodluyor. Chanel kuralı: bir aksesuar çıkar.
- Sorun 3: Grafikte her çubuk ayrı sekme durağı (7 durak) — klavye kullanıcısı tabloya ulaşmak için 7 kez Tab'a basıyor.
- Sorun 4: Tam sayfa görüntüde koyu yan menü zemini 900 px'te bitiyor, altı fıstık kalıyor.
- Sorun 5: Okuma satırındaki "+%5,8" renksiz; ölçütlerdeki iyi/kötü renk diliyle tutarsız.

Mobil (390):
- Sorun 6: Sipariş satırları kırık: saat ikinci satıra düşmüş, şube girintiyle birinci satırda; okuma sırası bozuk.
- Ölçüt şeridinin 2×2 hali, tarih seçicinin tam genişlik hali ve "Menü" düğmesi iyi çalışıyor.

## Tur 1 — revizyonlar

1. Değişim metni kısaltıldı: "+%6,1 artış", "−0,5 puan düşüş". Karşılaştırma tabanı ekran okuyucu için gizli metin olarak kaldı; görsel olarak başlık altındaki dönem cümlesinde bir kez söyleniyor.
2. Renk lejantı kaldırıldı; yalnızca "geçen haftanın aynı günü" kesikli çizgi lejantı kaldı.
3. Grafik çubuklarında gezici tabindex: grup tek sekme durağı, günler arası sol/sağ ok, Home/End. Yardım metni buna göre yeniden yazıldı.
4. Yan menü zemini sayfa ızgarasına taşındı, tüm yükseklik boyunca sürüyor.
5. Okuma satırındaki fark iyi/kötü rengini ve kalınlığını aldı.
6. Mobil sipariş satırı yeniden kuruldu: 1. satır "saat · şube ······ tutar", 2. satır "ürünler ······ durum" (3 sütunlu ızgara).

Tur 1 sonrası kontrol: mobil satırlar düzgün; sekme ile grafiğe gelip ok tuşuyla Perşembe seçildiğinde çini odak halkası ve okuma satırı doğru güncelleniyor.

## Tur 2 — revizyonlar

- "Son 30 gün" görünümünde her günün altında "ort." yazıyordu; başlık altı zaten "gün başına ortalama" diyor. Tekrar kaldırıldı (satır yüksekliği korunarak).
- Mobilde "sol ve sağ ok tuşları" yardım metni dokunmatik kullanıcı için gürültü; dar ekranda görsel olarak gizlendi, ekran okuyucuda duruyor. Grafik böylece telefonda ilk ekrana daha yakın.
- Açık mobil menü kontrol edildi: aktif sayfa fıstık zeminle belirgin, Esc ile kapanıp odak "Menü" düğmesine dönüyor.

## Bilinçli olarak bırakılanlar

- Kesikli "önceki dönem" çizgisi çubuğun 4 px dışına taşıyor; çubuk dışındaki karşılaştırma işareti olarak okunabilirliğe yardım ettiği için bırakıldı.
- Şubeler / Ürünler / Personel / Ayarlar gerçek sayfalar değil; tıklanınca ne olmadığını ve kullanıcının nereye bakabileceğini söyleyen kısa bir boş durum ve "Genel bakışa dön" düğmesi gösteriliyor.
- Hareket: yalnızca ilk yüklemede çubukların sırayla dolması ve aralık değişince yükseklik geçişi; `prefers-reduced-motion` ile kapanıyor.
