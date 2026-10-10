# DESIGN.md — ajansimiz-app (Vitrin Atölyesi, mobil)
> Marka token'ları `../ajansimiz/DESIGN.md` ile **aynı**. Bu dosya yalnızca mobil uyarlamayı ve konsept mini temalarını ekler. Kod karşılığı: `mobil/tailwind.config.js` + `mobil/src/theme/tokens.ts`.

## 1. Kişilik
net, sıcak, iş bilen · "Atölye Mürekkebi" yönü (ana markadan miras; 3 yön kararı ana DESIGN.md'de verildi).
Uygulama kabuğu (ana ekran, iletişim) markanın renginde; konsept ekranları **kendi mini temasıyla** (müşterinin markası gibi), üstte marka şeridi "Örnek konsept".

## 2. Renk — marka (açık / koyu)
background #FFFFFF / #0E1512 · surface #F3F5F2 / #151E1A · foreground #0E1512 / #EEF2EF · muted-foreground #4F5B55 / #A3AFA9 · border #DCE2DE / #26322C · primary #D7381E / #FF5A3C · primary-foreground #FFFFFF / #0E1512 · success #1F7A4D / #3DBE7E
## 2b. Konsept mini temaları (yalnızca kendi ekranında)
| Konsept | zemin (açık/koyu) | yüzey | metin | vurgu | vurgu-metin | kontrast buton · metin · ikincil/yüzey (açık / koyu) |
|---|---|---|---|---|---|---|
| Kafe | #FBF7F2 / #17110C | #F1E8DD / #241A13 | #2A1A10 / #F3E9DF | #8A4B24 / #D99560 | #FFFFFF / #17110C | 6.7·15.7·5.8 / 7.5·15.6·7.6 |
| Berber | #F6F5F1 / #0D0D0D | #EBE8E0 / #1A1A1A | #111111 / #F2EFE8 | #111111 / #C9A24A | #FFFFFF / #0D0D0D | 18.9·17.3·6.4 / 8.1·16.9·7.0 |
| Restoran | #FFFDF8 / #121410 | #F3F0E6 / #1C1F19 | #1B1E17 / #EEEDE6 | #2F6B3A / #7CC489 | #FFFFFF / #121410 | 6.4·16.6·6.2 / 8.9·15.8·7.1 |
(WCAG sRGB formülüyle hesaplandı; hepsi ≥4.5:1. İkincil metin + kenar renkleri `mobil/src/theme/tokens.ts`'te.)

## 3. Tipografi
Archivo (@expo-google-fonts/archivo) 400 · 500 · 800. Mobilde font-stretch yok (RN desteklemiyor) → başlıkta 800 + negatif harf aralığı.
display 40/40 -1.2 · h2 28/30 -0.6 · h3 20/24 -0.2 · lead 17/26 · body 16/24 · small 14/20 500. Sayaçlar tabular-nums.

## 4. Boşluk
4·8·12·16·24·32·48 · kenar 20 · satır içi 12 · grup 24 · bölüm 40.

## 5. Köşe ve derinlik
radius 8 / 14 / 24 / pill · kartlar: yüzey rengi farkı, çerçeve yok (native-slop #9) · gölge yalnız sheet: `0 -8px 30px rgba(14,21,18,.18)` · borderCurve continuous.

## 6. Bileşenler
Birincil buton 52 yükseklik, pill, 16/500, basılı scale .97 + koyulaşma, devre dışı %40 opak · İkincil: şeffaf + 1px foreground · Liste satırı ≥56, basılı arka plan değişir (squish yok) · Toast: alttan, 2.4 sn · ConfirmSheet / BottomSheet: alttan kayar, tutamaçlı, arka plana dokununca kapanır, birincil buton açılır açılmaz görünür (D-009) · Header: native stack başlığı.

## 7. Hareket
Arayüz 180-260ms, spring (damping 16-18); giriş yalnız kutlamada. ease-in yok. `useReducedMotion` → animasyonsuz.

## 8. Görsel dil
Fotoğraf yok. İkon: lucide-react-native tek set, stroke 1.75. Emoji yok.

## 9. Yapılmayacaklar
Ana marka listesi + Alert.alert, RN Button, TouchableOpacity, RN SafeAreaView, elevation, JS modal.
