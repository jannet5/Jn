# AI slop önleme kuralları (nokta)

Kaynaklar: https://www.superdesign.dev/blog/how-to-make-ai-ui-look-less-generic (okundu) + arama özetleri (skills.cat, claudeskills.info) + Things analizi.

Yasaklar:
1. Mor/indigo/mavi gradyan kimlik olarak kullanılmaz. Gradyan yok.
2. Her şeye aynı yuvarlak köşe + hafif gölge yok; satırlar düz, ayraç ince.
3. Üç ikonlu kart sırası, ortalanmış hero, "şablon kart" yok.
4. Inter/Roboto'yu tek başına hem başlık hem gövde yapma; karakterli bir font eşi seç (bir kez, bilinçli).
5. Beyaz+açık gri tek düze palet yok; nötr zemin + TEK vurgu rengi + anlam renkleri.
6. Boş durum için jenerik illüstrasyon/emoji yok.

Yapılacaklar:
- Önce gerçek referans seç (Things ilkesi: renk yalnızca anlam taşır), tek bir estetik adı koy.
- "Güzel yap" yerine sayı ver: boşluk 4/8 grid, satır ölçüsü, süreler (200/300/150ms).
- DESIGN.md'yi (renk, tip, boşluk, radius, hareket) ekranlardan ÖNCE yaz.
- Birkaç yön üret, karşılaştır; sonra "çıkarma turu": slop listesine karşı sil.
- Vibe ile değil ekran görüntüsüyle iterasyon (Roborazzi/emülatör PNG).
- Hareketi anlamlı yap: tamamlama, sürükleme, silme geri bildirimi.
