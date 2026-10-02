# nokta — DESIGN.md (Harita A)

Bu dosya ekranlardan ÖNCE yazıldı. Kod `ui/theme/` altında bu sayıları birebir kullanır.

## 1. Karar: ad, estetik, kimlik
- **Ad: "nokta"** (değiştirmedim). Gerekçe: balonun kendisi bir nokta; ad = ikon = onay işareti. Tek harfli, Türkçe, telaffuzu zor değil. Bir noktayı (liste maddesi) başka noktaya sürüklüyorsun.
- **Estetik adı: "Sinyal lambası".** Zemin söner, tek bir turuncu nokta yanar. Nokta ne zaman yanık? (a) balon, (b) tamamlanmış satırın işareti, (c) imleç/odak. Başka hiçbir yerde renk yok. Kâğıt/mürekkep, krem, gradyan, mor yok.
- Things 3 gözleminden (tasarim_referanslari.md; üçüncü taraf analiz, doğrulanmamış): satır dikey 12 / yatay 16, 20-22 yuvarlak işaret, ~%95 nötr, renk yalnızca anlam; tamamlama 200ms dolum -> 300ms yaylı onay -> 150ms çıkış; sürüklemede 1.02x ölçek + gölge. Google Tasks gözlemi (yorum_analizi.md: "The looks so dull", kompakt görünüm isteği): sade ama sönük kalmasın diye tipografiye karakter, yoğunluk 56dp (Google Tasks'ın iddia edilen ~ geniş satırından bir tık sıkı) seçildi.
- Anlam kuralı: renk = "yanık nokta". Silme kırmızısı YOK (silme zaten geri alınabilir; tehlike hissi gereksiz). Üstü çizili = soluk ink + çizgi, renk değil.

## 2. Renk (hex, WCAG kontrast hesabı rapora bakınız)
| token | açık | koyu | kullanım |
|---|---|---|---|
| bg | #F2F3EF | #111312 | ekran + satır zemini (satır kart değil) |
| ink | #181B19 | #E9ECE7 | metin, ikon |
| inkSoft | #181B19 @ 55% | #E9ECE7 @ 55% | alınmış (çizili) metin, yardımcı metin, tutamaç |
| line | #181B19 @ 10% | #E9ECE7 @ 12% | satır ayracı 1dp |
| dot (TEK vurgu) | #E5481C | #FF6A3A | balon, işaret dolgusu, imleç, geri al eylemi |
| onDot | #181B19 | #111312 | nokta üstündeki iç nokta/işaret |
| inverse | #181B19 | #E9ECE7 | geri al şeridi zemini |
| onInverse | #F2F3EF | #111312 | geri al şeridi metni |
- Gradyan yok. Gölge yalnızca sürüklenen satırda (elevation 6dp) ve balonda (4dp).
- Metin kontrastı: ink/bg ≈ 15:1 (açık), 14:1 (koyu). inkSoft %55 ≈ 3.3:1 — yalnızca "alınmış" ve ikincil metin için bilinçli düşük (Things'te de tamamlanan sönük).
- Material3 `ColorScheme` bu tokenlardan türetilir: primary=dot, onPrimary=onDot, background/surface=bg, onSurface=ink, outlineVariant=line. Başka M3 rengi kullanılmaz (dynamic color KAPALI: markayı sistem duvar kâğıdına bırakmıyoruz).

## 3. Tipografi (iki font, bilinçli eş)
- **Familjen Grotesk** (SIL OFL, değişken wght 400-700, 90 KB): yalnızca wordmark "nokta" ve bölüm/sayı başlıkları. Karakteri: hafif tuhaf, sıkı grotesk. Roboto/Inter DEĞİL.
- **Hanken Grotesk** (SIL OFL, değişken wght 100-900, 133 KB): gövde, satır metni, giriş alanı. Türkçe karakterler (ğ ş ı İ ö ü ç) doğrulandı.
- Ölçek (sp / satır yüksekliği / ağırlık / letter-spacing):
  - wordmark: Familjen 28/32/700/-0.5
  - satır: Hanken 17/24/400/0
  - giriş: Hanken 17/24/400/0 (placeholder inkSoft)
  - alt bilgi ("3 bekliyor · 2 alındı"): Hanken 13/16/500/+0.2
  - geri al şeridi: Hanken 15/20/500
  - menü: Hanken 16/24/400
- Yazı tipi ölçeklenmesi (sp) açık; satır `minHeight` 56dp, uzun metin 3 satıra kadar, sonra `…`.

## 4. Boşluk, ölçü (4dp grid)
- Ekran yatay kenar: 16dp. Üst çubuk: 56dp yükseklik (wordmark sol, menü sağ).
- Satır: minHeight 56dp, dikey iç boşluk 12dp, ayraç 1dp (soldan 56dp içeride başlar, işaretin altında değil metin hizasından).
- Sol -> sağ: tutamaç alanı 40x56 (grip ikonu 3x2 nokta, 4dp çapında noktalar, 4dp aralık) | işaret (nokta) 22dp çap, 1.5dp halka | 12dp | metin (esner) | sil alanı 48x48 (ikon 20dp, "x" çizgisi 1.75dp).
- Dokunma hedefleri >= 48x48dp (tutamaç 40x56 genişlik ama yükseklik 56; Android min 48 için genişlik 48'e çıkarıldı: tutamaç 48x56).
- Giriş alanı (altta): yükseklik 56dp, üstünde 1dp line ayracı, zemin bg, imleç dot rengi, klavye üstüne `imePadding`. Gönder: klavyede Enter/Ekle; ayrıca 48dp "+" yok (ekstra kontrol istemiyoruz).
- Balon: 52dp çap, kenardan 8dp içeride durur, yalnız kenara yaslanır (sağ varsayılan, y=%35). Gölge 4dp.

## 5. Köşe yarıçapı (her şeye aynı değil)
- Satır, ekran, ayraç: 0dp (düz).
- Geri al şeridi: 8dp, alt kenardan 72dp (giriş alanının üstünde), yatay 16dp.
- İşaret, balon, tutamaç noktaları: daire.
- Sürüklenen satır: 0dp (yalnız ölçek+gölge).

## 6. Hareket
- İşaret dolgusu: 200ms, `FastOutSlowIn`. Metin soluklaşma: 200ms. Çizgi anında (TextDecoration), soluklukla birlikte okunur.
- Silme: satır `animateItem` ile fade-out + yukarı kayma 150ms (placementSpec tween 150).
- Sürükleme: ölçek 1.03 (Things 1.02, mobilde elle kapatıldığı için biraz fazla), elevation 6dp, `HapticFeedbackType.GestureThresholdActivate` başlangıçta, bırakmada `GestureEnd`. Tutamaçla anında; satırın geri kalanında uzun basma (sistem timeout ~400ms).
- Geri al şeridi: 5000ms görünür, giriş/çıkış 150ms.
- Yeni satır: ekleniş sonunda liste en alta kayar (animateScrollToItem), ayrıca fade-in 150ms.

## 7. Bileşenler ve davranış (kısa)
- Satıra dokun: toggle(alındı) = işaret dolar + çizili. Sil "x": öğeyi kaldırır, geri al şeridi çıkar. Tutamaç/uzun bas: sürükle.
- Menü (...): "Alınanları temizle (N)", "Dışa aktar (paylaş)", "Balonu aç/kapat", "Sürüm".
- Boş durum: tek cümle "Henüz bir şey yok. Aşağıya yaz." + giriş alanı odakta. İllüstrasyon/emoji yok.
- İzin yok durumu: listenin üstünde tek satır "Balon için ekran üstü izni gerekli" + metin düğmesi "İzin ver". Kart, hero yok.
- Koyu tema: sistem temasına uyar (`isSystemInDarkTheme`). Balon da `Configuration.uiMode` ile açık/koyu dot seçer.

## 8. İkon
- Uyumlu (adaptive) ikon: arka plan düz #181B19, ön plan: dot (#E5481C) 36dp çaplı daire (108dp tuvalin ortası, güvenli alan 66dp içinde), içinde 10dp çaplı #181B19 iç nokta, merkezden (+5, -5)dp ofset: "bakan göz / sinyal" gibi, tek hareket. Monokrom katman: yalnızca dış daire, iç nokta delik.
- Balon: aynı daire, 52dp, iç nokta 14dp merkezde değil aynı ofsetle.

## 9. AI-slop denetim listesi (ai_slop_onleme.md'ye karşı)
1. Gradyan: yok. 2. Aynı radius+gölge: yok, radius 0 / 8 / daire. 3. Kart/üç ikon/hero: yok. 4. Inter/Roboto: yok; Familjen + Hanken. 5. Beyaz+gri: yok; yeşilimsi soğuk nötr + TEK turuncu nokta. 6. Boş durum illüstrasyon/emoji: yok.
