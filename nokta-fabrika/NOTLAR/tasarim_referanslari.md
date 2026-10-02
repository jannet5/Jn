# Tasarım referansları (nokta)

Durum notu: Aşağıda yalnızca gerçekten açıp okuduklarım var. WebFetch küçük bir model üzerinden özet döndürüyor; sayılar "kaynak yazıya göre" ve doğrulanmamıştır.
Okunamayanlar: Material 3 liste spec sayfası (m3.material.io/components/lists/specs; içerik gelmedi), Mobbin/Dribbble/Behance (denenmedi), Tasks.org (arama sonucu çıkmadı), Google Tasks / TickTick / Notion / Apple Reminders (ayrıca okunmadı).

## Things 3 (okundu, 3. taraf analiz)
Kaynak: https://blakecrosley.com/guides/design/things (Things'in resmi spec'i DEĞİL; web yorumu)
- Satır: dikey 12px, yatay 16px padding; onay kutusu 20-22px yuvarlak; kutu-metin boşluğu 12px.
- Arayüzün ~%95'i nötr; renk yalnızca anlam taşır (sarı Bugün #f5c518, kırmızı gecikmiş #ff3b30, indigo Akşam #5856d6, yeşil tamam #34c759).
- Tamamlama sırası: daire dolar 200ms -> onay işareti yaylı (spring) ~300ms -> satır kayarak çıkar 150ms; toplam ~500ms. Ses eşlik ediyor.
- Sürükleme: kaldırılan satır 1.02x ölçek + gölge; bırakma göstergesi 4px yükseklikte çizgi.
- Kenarlık rgba(0,0,0,0.08), metin #1d1d1f.
- Nokta için: en iyi ilham. Renk=anlam kuralı, tamamlama animasyonunun üç adımı, 1.02 ölçekli sürükleme.

## Todoist (kısmen / güvenilmez)
Kaynak: https://tessl.io/registry/skills/github/ihlamury/design-skills/todoist-ui-skills
- Üçüncü taraf "skill" sayfası; çıkan değerler tutarsız (örn. metin rengi #A6A6A6, başlık 55px) -> GÜVENİLMEZ, kullanma. Sadece 4px grid, Inter, 4.5:1 kontrast vurgusu tutarlı.
- Arama sonucuna göre Todoist görev notlarını ve saati satır altında gösteriyor (snippet, sayfa okunmadı).

## Microsoft To Do (yalnız haber özeti)
Kaynak: https://www.neowin.net/news/microsoft-refreshes-the-ui-for-to-do-with-smaller-headers-more-color (snippet)
- Her öğe kendi yuvarlak köşeli dikdörtgeninde, arka plandan hafif ayrışıyor; küçük başlıklar, daha çok renk.
- Sürükle-sırala: basılı tut, sürükle, bırak (Microsoft destek sayfası snippet'i).
- Nokta için: kartlı satır "şablon kart" riski taşır; düz satır + ince ayraç daha güvenli.

## AnyList (okunmadı, snippet)
Kaynak: https://jaredsinclair.com/2014/05/09/friday-app-design-review-anylis.html (2014 incelemesi, eski)
- Beyaz zemin, yalnızca ince yatay çizgiler, tek vurgu rengi, süssüz metin.

## Bring! / Listonic (snippet)
- Bring: sade, kalın ikonlar; kullanıcılar kullanım sıklığına göre sıralama istiyor.
- Listonic: işaretlemek yerine kaydırma ile işlem isteyen kullanıcılar için swipe eklenmiş (https://listonic.com/the-new-listonic).

## Material 3 / Android notu
M3 liste ölçüleri okunamadı. Kendi bilgimden (doğrulanmadı): tek satırlı liste öğesi 56dp, dokunma hedefi min 48dp. Uygulamadan önce developer.android.com / m3.material.io'dan doğrula.

## Nokta için türetilen öneriler (benim sentezim, kaynaklardan çıkarım)
- Satır ~56dp, yatay 16dp, yuvarlak onay 22dp, metin 16sp normal ağırlık; tamamlanınca metin soluk + üstü çizili, ardından satır kayıp çıkar.
- Tek vurgu rengi; geri kalanı nötr. Boş durum: tek cümle + ekleme alanına odak, illüstrasyon yok.
- Sürükleme: 1.02-1.05 ölçek, hafif gölge, haptic; bırakma çizgisi.
