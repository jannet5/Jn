# KALİTE KAPILARI — geçmeden sonraki adıma geçilmez

## KAPI-1 · Tasarım temeli (üretimden önce)
- [ ] DESIGN.md var; renk, font, boyut, boşluk, köşe, gölge **sayıyla** yazılı; tahminler işaretli
- [ ] `npx @google/design.md lint DESIGN.md` temiz
- [ ] En fazla 2 font ailesi, 6 boyut, 3 ağırlık; 1 marka rengi + nötrler + anlamsal renkler
- [ ] Metin kontrastı ≥4.5:1 (açık ve koyu)
- [ ] 3 yön gösterildi, biri seçildi (karar dosyada)
- [ ] CLAUDE.md'de `@DESIGN.md` importu ve "yeni renk ekleme, dur ve sor" kuralı

## KAPI-2 · Görsel QA (her sayfa/ekran sonrası, ajan kendisi)
- [ ] Playwright SS: web 375/768/1440 · mobil 390×844 açık+koyu (+ emülatör/simülatör varsa)
- [ ] design-review ajanı puanı ≥3/4 (hiyerarşi, tutarlılık, boşluk, tipografi, durumlar, erişilebilirlik, konsol hatası yok)
- [ ] impeccable dedektörleri + hallmark slop-test temiz (mor gradyan, 3 eşit kart, emoji ikon, Inter display, uydurma metrik, her şeyde fade-up…)
- [ ] Mobilde native-slop listesi temiz (Alert.alert, JS modal, elevation, elle header…)
- [ ] En fazla 3 düzeltme turu; hâlâ <3 ise insana rapor

## KAPI-3 · Yayın öncesi 20 kontrol (5 alt ajan, salt okuma; düzeltmeyi tek ajan yapar)
> Limit modunda: `kullanici-testi.mjs` + Lighthouse aynı işi görür. Her madde için **uygulanabilir / N/A + gerekçe** yazılır (statik landing'de form durumları, NAP vb. N/A olabilir).
> Büyük metin testi: `html{font-size:200%}` enjekte et, `scrollWidth > innerWidth` olmamalı.
**Tutarlılık:** her değer DESIGN.md'den · kısıtlı palet, net hiyerarşi · aynı tür buton/kart/input aynı · çok düzse vurgu, kalabaliksa kes · koyu/açık zeminde okunur
**Mobil:** yatay kaydırma yok · mobil menü var · dokunma alanı ≥44pt · büyütülmüş metinde düzen bozulmuyor
**Durumlar:** yükleniyor/boş/hata ekranı · her butonda hover/basılı/devre dışı · form hatası yerinde + gönderiliyor/başarılı/başarısız · modal/dropdown/sekme geçişleri animasyonlu
**Gerçek kullanıcı gibi:** ana akışlar baştan sona tıklandı · ölü buton/kırık link yok · klavyeyle kullanılabilir
**Yayın:** ana sayfada tek cümlede ne yaptığımız · her sayfada tek ana buton · her sayfada title/description/favicon · placeholder metin yok
**10K puanlaması ("be honest") — strong şudur:**
| Alan | strong |
|---|---|
| Yön | Sayfa tek bakışta tek bir kişilik söylüyor; başka sektöre kopyalansa sırıtır |
| Tipografi | ≤6 boyut, net ölçek, başlıklarda karakter (genişlik/ağırlık), satır uzunluğu ~65 karakter |
| Renk | 1 sinyal rengi ≤3 kullanım yeri, nötrler bilinçli, iki temada da kontrast ≥4.5 |
| Hiyerarşi | Her bölümde büyük/orta/küçük okuma sırası belli, tek birincil buton |
| Görsel | Gerçek foto ya da konuya özel çizim; stok/placeholder yok |
| Hareket | Bölüm başına tek amaçlı etkileşim, 150-600ms, reduced-motion var |
| Mobil | 375'te ayrıca tasarlanmış (gizlenen, sıkılaşan öğeler), taşma yok |
| Görünmez | Lighthouse 4 kategori ≥90, title/description/favicon, konsol temiz |
mixed/missing kalmaz.
**Yerel işletme:** tıklanabilir telefon/WhatsApp · fold üstü form · yorumlar · H1 hizmet+şehir · NAP = Google İşletme Profili

## KAPI-4 · Teslim (insan, 15-20 dk)
- [ ] Before/after SS'leri ve canlı link ekte
- [ ] Gerçek telefonda açıldı (site) / Expo Go'da denendi (uygulama)
- [ ] Müşteri onayı yazılı
- [ ] "CLAUDE.md'ye / skill'e ne eklemeliyiz?" sorusu cevaplandı → `DERSLER.md`
