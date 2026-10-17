# DESIGN.md: Kur'an Oku
> Görsel dilin tek doğruluk kaynağı. Kod karşılığı: `app/src/main/java/app/kuranoku/ui/theme/Theme.kt` ve `PagePresets.kt`.
> Kullanıcı kararı (2026-10-04): "Ben seçmeyeyim, hepsi uygulamada olsun, isteyen istediğini seçsin." → 3 yön (Mushaf, Sade, Lacivert) + Gece, Mavi, Sepya hazır görünüm, 4 yazı tipi, serbest sayfa/yazı rengi uygulamanın içinde.

## 1. Kişilik
- 3 sıfat: sakin, gerçek (basılı mushaf hissi), hafif
- Olmayacak 3 şey: reklam/kalabalık, parlak "teknoloji" renkleri, süs için süs
- Tek cümlelik his: "Elimde basılı Kur'an varmış gibi."

## 2. Referanslar
| Uygulama | Neyini alıyoruz |
|---|---|
| Quran for Android | Açılınca kaldığı sayfa, 604 sayfa düzeni, dokununca çubuklar |
| Apple Books / Kindle "Aa" | Arkadaki sayfayı karartmayan, canlı değişen görünüm paneli |
| Hayrat Kuran | Sayfa numarası sağ altta, mushaf çerçevesi |
| Spotlight | Tek kutu, yazdıkça öneri, Enter ilk öneriye gider |

## 3. Renkler (arayüz)
| Token | Rol | Açık | Koyu | Kontrast |
|---|---|---|---|---|
| background/surface | Ekran zemini | #FBF8F1 | #14171D | — |
| onSurface | Ana metin | #1D1B18 | #ECE7DC | 16.2 / 14.6 |
| primary / onPrimary | Marka, seçili | #1F5A46 / #FFFFFF | #D6B56A / #1E1A10 | 8.0 / 8.8 |
| muted | İkincil metin, ikon | #6A645A | #A49C8D | 5.5 / 6.6 |
| secondaryContainer | Seçili satır | #EDE5D3 | #2B3040 | — |
| outline / border | Çerçeve | #D8D0BF | #3A3F4C | — |
| error | Uyarı | #B3261E | #F2B8B5 | 6.2 / 10.5 |
Arayüz açık/koyu modu **sayfa renginden** gelir: sayfa parlaklığı < 0.25 ise koyu arayüz.

## 3b. Sayfa görünümleri (okuma alanı)
| Görünüm | Sayfa | Yazı | Süs | Doku | Çerçeve | Yazı/sayfa kontrastı |
|---|---|---|---|---|---|---|
| Mushaf | #EFE4CC | #2B2118 | #A88234 | ✓ | ✓ | 12.5 |
| Sade | #FFFFFF | #111111 | #0F6E66 | — | — | 18.9 |
| Lacivert | #13203A | #ECDFC2 | #D6B56A | ✓ | ✓ | 12.3 |
| Gece | #151515 | #D2CBBD | #8FA89A | — | — | 11.3 |
| Mavi | #E6EEF7 | #16365F | #3D6AA1 | ✓ | ✓ | 10.4 |
| Sepya | #F3E7D3 | #4A3420 | #8C5A2B | ✓ | — | 9.5 |
Özel renk: 14 sayfa + 12 yazı rengi kutusu ve ton/canlılık/açıklık seçici. Kontrast < 3 olursa panelde uyarı çıkar.

## 4. Tipografi
Arayüz: sistem yazı tipi (Roboto) — ek dosya yok, APK hafif. Ağırlıklar 400 · 500 · 700.
| Token | Boyut | Satır | Ağırlık | Kullanım |
|---|---|---|---|---|
| bodySmall | 12 | 16 | 400 | alt başlık |
| bodyMedium | 14 | 20 | 400 | açıklama |
| body/titleMedium | 16 | 24 | 400/500 | gövde, liste başlığı |
| titleLarge | 20 | 28 | 700 | ekran başlığı |
| headlineSmall | 24 | 32 | 700 | (yedek) |
Kur'an: Amiri Quran (varsayılan), Scheherazade New, Noto Naskh Arabic, Amiri. Temel boyut 22sp; ölçek %65–%255, %1 adım. Satır yüksekliği: 2.05 / 1.85 / 1.9 / 2.0 × boyut.

## 5. Boşluk
Skala 4·8·12·16·24·32·48 · Ekran kenarı 16 · Bölümler arası 24 · Bölüm içi 8–12 · Kart iç 16 · Liste satırı ≥ 64

## 6. Köşe
sm 8 (rozet) · md 14 (buton, kart, input) · lg 24 (alt panel) · tam daire (renk kutusu)

## 7. Derinlik
Kartlar yalnız **ince çerçeve** (1dp outline, seçiliyken 2dp primary). Gölge yok. Okuma çubukları %96 opak zemin.

## 8. İkonlar
Lucide (SVG yolları → ImageVector), strokeWidth 2, boyut 16/20/24. Liste oku ChevronRight 20 muted. Geri ChevronLeft 24.

## 9. Bileşen kuralları
| Bileşen | Kural |
|---|---|
| Birincil buton | ≥48dp, radius 14, tek ekranda tek |
| İkincil buton | Outlined, ≥48dp, radius 14 |
| Input | Outlined, radius 14, solunda Search 20 muted |
| Liste satırı | ≥64dp, rozet 36dp + başlık/alt başlık + sağ içerik + ChevronRight 20 |
| Üst çubuk | 56dp, geri 24 + başlık 20/700 |
| Boş durum | 56dp ikon kutusu, başlık 20, açıklama 14 muted, isteğe bağlı buton |

## 10. Hareket
Yazı boyutu butonları 260 ms; ayet vurgusu 1.8 sn bekle + 1.4 sn sön; çubuklar kaydır+sön; ekran geçişi yana 1/6 kaydır+sön.

## 11. Yapılmayacaklar
Reklam, internet izni, gradyan (kağıt kenar gölgesi hariç), emoji ikon, iç içe kart, ikiden fazla vurgu rengi.
