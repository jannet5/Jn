# Ürün Spesifikasyonu (ortak, tüm sohbetlere eklenir)

## Ürün
Android görev/alışveriş listesi uygulaması. Kişi plan yaparken (dışarı çıkacak, alınacaklar vb.) hızlıca liste tutar.

## Zorunlu özellikler
1. **Sağ kenarda yuvarlak yüzen balon (bubble)**: telefonda sağ tarafta küçük yuvarlak; dokununca uygulama/liste **doğrudan** açılır (overlay izni + ön plan servisi; Android sürüm kısıtlarını araştır).
2. Listedeki her satırda **sağda sil** butonu.
3. Satıra dokununca **üstü çizilir** (tamamlandı) — silmeden **ayrı** bir davranış.
4. **Tutup sürükleyerek sıralama** (drag & drop reorder).
5. **Çok hızlı, basit, kasmayan** uygulama (soğuk açılış hızlı, 60fps liste, gereksiz bağımlılık yok).
6. Veriler yerelde kalıcı (Room/DataStore vb. — araştırıp seç).

## Tasarım kısıtı
- Arayüz **"AI slop" olmayacak**: jenerik mor gradyan, aşırı yuvarlak kart, anlamsız emoji, şablon görünüm YOK.
- İnternette hazır mobil uygulama tasarımları / açık kaynak uygulamalar var → **beğenilen, gerçek uygulamalardan ilham al / akışı kopyala** (lisansa uy: ilham/yapı serbest, kod kopyalıyorsan lisans uygun olmalı ve NOTLAR'a yaz).
- **Marka adı, logo, renk paleti, tipografi, ikonografi: tamamen yapay zekanın inisiyatifinde.** Kullanıcı hiçbirini dikte etmiyor; kendin karar ver, gerekçesini NOTLAR'a yaz.

## Ticari hedef
Google Play'de satılacak; gerçek kullanıcılar kullanacak. Bu yüzden: uygulama kimliği (package), imzalama (release keystore), izin gerekçeleri, gizlilik politikası taslağı, Play mağaza metni/ekran görüntüsü listesi, sürüm kodu — hazır olmalı.

## Çalışma ortamı
- **Sadece bulut** (CI/konteyner). Kullanıcının bilgisayarında hiçbir şey çalışmaz.
- Çıktı: **imzalı release APK** (mümkünse AAB de) → **kullanıcının Google Drive'ına** yükle, paylaşım linkini raporda ver.
- Gizli anahtar/keystore şifresini sohbete veya repoya **düz yazma**; gizli değişken kullan, bana ne yaptığını söyle.

## Doğrulama (her harita için zorunlu)
- Derleme logu, birim/UI testi çıktısı, emülatör (varsa) ekran görüntüleri; 6 özelliğin her biri için **kanıt**. Test edilmeyeni "yapıldı" yazma.
