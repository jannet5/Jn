# DERSLER: sosyal medya + reklam hattının ilk gerçek testi (2026-10-10)

## Hatta eksik / yanlış maddeler (sosyal-medya.md)
1. **CTA çakışması:** BRIEF tek aksiyonu "WhatsApp'tan ücretsiz check-up", OZET §5 ise "DM'den ÖRNEK yaz" diyor. Instagram hesabında DM ÖRNEK kullanıldı. Hat: "sosyal hesapta CTA = BRIEF aksiyonu mu, platform aksiyonu mu?" kuralı eklenmeli; tek karar KARARLAR.md'ye.
2. **Hashtag çelişkisi:** OZET §5 "hashtag yok", tr-caption "3-5 yerel hashtag". Çözüm önerisi: ilk 2 hafta A/B (tek gün etiketli, çift gün etiketsiz), sonra karar.
3. **Sosyal kanıt günü müşteri yokken tanımsız:** plan gün 10 "ilk müşteri yorumu" diyor. Hata: yorum yokken bu gün sahte kanıta iter. Hat kuralı: "Kanıt yoksa dürüstlük gönderisi (henüz yorum yok + ne yapıyoruz)" şablonu eklendi (gün 10).
4. **"En popüler" etiketi:** 01-teklif "ortadaki En popüler" diyor; müşteri yokken bu bir uydurma istatistik. Gönderide "Önerilen" kullanıldı. Hata düzeltilmeli.
5. **Önce/sonra müşterisizken:** "önce" görseli için gerçek bir işletmenin sitesi kullanılamaz (izin + taklit riski). Çözüm: temsili eski site çizimi + "Temsili, gerçek bir işletme değil" alt yazısı + "Sizin Tesisat / Sizin Kafe" yer tutucu adlar. Hatta yazılı kural olmalı.
6. **Izgara oranı:** Instagram profil ızgarası artık 3:4 kırpıyor; 4:5 gönderinin kenarları, 9:16 reel kapağının üstü/altı kesiliyor. Hat: "kapakta önemli yazı ortadaki 3:4 alanda" kuralı + `izgara.png` önizlemesi S4'e eklenmeli.
7. **Kalite kapısı S4'e otomatik test:** `sablonlar/uret.mjs` her slaytta kontrast (WCAG ≥4.5), kutu dışına taşma, `.main` içi çakışma ve yetim satır arıyor. İlk turda 12 sorun yakaladı ama 2'sini KAÇIRDI (flex öğesi büzülünce taşma görünmüyor). Ders: `.main>*{flex-shrink:0}` olmadan test kör; yine de gözle bakmak şart.
8. **Primary rengi surface zeminde 4.28:1** (DESIGN tablosu beyaz için 4.7 diyor). Yüzey (#F3F5F2) üstünde vurgu metni DESIGN'daki koyu ton #B92E17 ile yazıldı. DESIGN.md'ye "surface üstünde primary metin = #B92E17" satırı eklenmeli.

## Reklam hattı (reklam.md)
9. **ad-score ağırlıkları tanımsız:** hat "6 boyut" diyor ama ağırlık yok. Kullanılan: hook 25 · netlik 20 · teklif 20 · kanıt 10 · CTA 15 · marka 10. Hatta yazılmalı.
10. **Kanıt boyutu müşterisiz hep düşük** (40-70). Eşik 70 korunursa kanıtsız kreatifler sınırda kalıyor; "lansman modunda kanıt yerine şeffaflık (fiyat açık, örnek konsept)" puanlanmalı.
11. **Bütçe TL karşılığı yok:** hat bütçe kademesi vermiyor. Önerilen 150/300/600 ₺/gün; ilk hesapta gerçek CPM ile güncellenmeli.
12. **Mesaj hedefi + WhatsApp numarası:** OUTCOME_ENGAGEMENT mesaj hedefi WhatsApp numarası olmadan sadece Instagram Direct'e gider. Kullanıcıdan numara bekleniyor (KARARLAR "kullanıcıda kalanlar").

## Teknik
- Playwright global (`/opt/node22/lib/node_modules/playwright`), `executablePath: /opt/pw-browsers/chromium`. Font: `@fontsource-variable/archivo` wdth dosyaları `sablonlar/font/`a kopyalandı (latin + latin-ext; ı/ş/ğ latin-ext'te).
- 4:5 için viewport 420×525, dsf 18/7 tam 1080×1350 veriyor; 9:16 ve 1:1 için 360 px genişlik + dsf 3.
- Yeniden üretim: `cd sosyal/sablonlar && node uret.mjs && python3 son-islem.py` (~10 sn, 57 görsel).
