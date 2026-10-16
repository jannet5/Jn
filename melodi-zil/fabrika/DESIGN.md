# DESIGN.md: Melodi Zil
> Görsel dilin TEK doğruluk kaynağı. Kodda `ui/theme/Tokens.kt` ve `ui/theme/Theme.kt` bu dosyayla birebir aynıdır. Her alan SAYI ile doldurulmuştur.

## 1. Kişilik
- 3 sıfat: **Net, Oyuncu, Güvenilir**
- Olmayacak 3 şey: Gradyan/neon yığını (InShot gibi) · Reklam bantları ve kampanya kartları · Eski Holo/sistem-varsayılanı görünüm (Ringtone Maker gibi)
- Tek cümlelik his: "Bir müzik aleti kadar sade, bir oyun kadar eğlenceli."

## 2. Referanslar
| Uygulama | Neyini alıyoruz |
|---|---|
| Shazam (ana ekran) | Tek büyük eylem, bol boşluk, büyük başlık |
| Zedge (ayarla sheet'i) | Zil/bildirim/alarm seçimi tek alt sayfada |
| InShot Ringtone Maker (Dosyalarım) | Kayıtlı dosyalar listesi + yeniden ayarlama |
| GarageBand (enstrüman seçimi) | İkonlu chip'lerle tını seçimi, anında önizleme |
| Google Saat / Signal (ayarlar) | Gruplanmış kart içinde liste satırları |

Başlangıç dosyası: Material 3 referans token seti (shadcn "zinc" nötrleri + tek violet vurgu) — awesome-claude-design "clean-saas" yönüne en yakın.

## 3. Renkler
| Token | Rol | Açık | Koyu | Kontrast (metin/arka plan) |
|---|---|---|---|---|
| background | Ekran arka planı | #FFFFFF | #121218 | — |
| foreground | Ana metin | #16151D | #F2F1F7 | 17.4:1 / 16.9:1 |
| card | Kart yüzeyi | #F4F3F9 | #1C1B24 | — |
| primary / primary-foreground | Marka, birincil eylem | #5B3DF5 / #FFFFFF | #A596FF / #1B0F5C | 6.1:1 (beyaz üstünde) / 7.5:1 (koyu bg üstünde); buton metni 6.1:1 / 9.8:1 |
| secondary / secondary-foreground | Seçili chip, sekme göstergesi | #ECE8FF / #3B23B8 | #2A2347 / #D8D0FF | 8.9:1 / 9.6:1 |
| muted / muted-foreground | Skeleton yüzeyi / ikincil metin, ikon | #EDECF3 / #6B6880 | #26252F / #A09EB3 | 5.4:1 / 7.1:1 |
| border | Çerçeve, ayraç | #E3E1EC | #2C2B38 | — |
| destructive | Hata, sil | #D63B3B | #FF6B6B | 4.6:1 / 7.4:1 |
| success | Tamamlanan adım | #1F9D5B | #4CCB85 | 3.3:1 (yalnız ikon, ≥3:1 yeter) / 9.1:1 |

Kontrastlar WCAG göreli parlaklık formülüyle hesaplandı; tüm gövde metinleri ≥4.5:1.

## 4. Tipografi
Başlık fontu: Sistem (Roboto) · Gövde fontu: Sistem (Roboto) · Ağırlıklar: 400 · 500 · 700
| Token | Boyut | Satır yüksekliği | Ağırlık | Kullanım (Compose stili) |
|---|---|---|---|---|
| caption | 12 | 16 | 500 | Alt sekme etiketi (labelSmall) |
| body-sm | 14 | 20 | 400 / 500 | İkincil metin, chip etiketi (bodyMedium / labelMedium) |
| body | 16 | 24 | 400 / 500 | Gövde, liste satırı, buton (bodyLarge / labelLarge) |
| title | 20 | 28 | 500 | Bölüm başlığı, header (titleMedium) |
| heading | 24 | 32 | 700 | İşlem ekranı başlığı (headlineSmall) |
| display | 32 | 40 | 700 | Ekran büyük başlığı (displaySmall) |

## 5. Boşluk
Skala: 4·8·12·16·24·32·48·64 · Ekran kenarı: **16** · Bölümler arası: **24** · Bölüm içi: **8-12** · Kart padding: **16** · Liste satırı yüksekliği: **≥64**

## 6. Köşe
radius-sm: **8** (input, chip, skeleton, kapak) · radius-md: **16** (kart, buton) · radius-lg: **24** (bottom sheet) · radius-full: 9999

## 7. Derinlik
Kart stili: **1dp çerçeve (border) + kart yüzeyi; gölge YOK.** Koyu modda aynı kural (yüzey #1C1B24 + çerçeve #2C2B38). Elevation her yerde 0.

## 8. İkonlar
Tek set: **Material Icons Rounded** (Compose `material-icons-extended`) · Boyutlar: **20** (liste, buton, chip) / **24** (header, alt sekme) / **48** (yalnızca boş/hata durumu illüstrasyonu) · Renk: yanındaki metin ya da muted-foreground.
Ok eşlemesi: liste satırı → `KeyboardArrowRight` 20 muted · geri → `ArrowBack` 24 · kapat → sheet sürükleme tutamacı (sistem) · ileri butonu içinde ok yok.
Enstrüman ikonları: Piano, MusicNote, SportsEsports, GraphicEq, Air, NotificationsActive, Audiotrack, Waves.

## 9. Bileşen kuralları
| Bileşen | Kural |
|---|---|
| Birincil buton | 52 yüksek, radius 16, labelLarge, tam genişlik, ekranda **tek**; yükleniyorken spinner + devre dışı |
| İkincil buton | Aynı ölçüler, çerçeveli (outline), onSurface metin |
| Input | 56 yüksek, radius 8, label içeride (M3 outlined), yardım/hata metni altta |
| Kart | padding 16, radius 16, 1dp çerçeve, gölge yok, iç içe kart yok |
| Liste satırı | ≥64 yüksek, sol ikon 20 muted, sağ ok 20 muted, alt ayraç border rengi |
| Header | Büyük başlık (display 32) ekran üstünde; stack ekranlarında 24 geri oku + title |
| Alt sekme | 3 sekme, ikon 24, labelSmall, aktif gösterge secondary |
| Chip (form seçimi) | FilterChip radius 8, ikon 20, seçili: secondary / secondary-foreground |
| Boş durum | ikon 48 muted, title, body-sm muted, birincil buton |
| Hata durumu | ikon 48 destructive, body, "Tekrar dene" birincil + "Geri" ikincil |
| Yükleniyor | SkeletonBox (muted, radius 8) ya da adım listesi (işlem ekranı) |
| Piyano rulosu | 140 yüksek, kart yüzeyinde çizim, notalar primary (alpha şiddete göre), oktav kılavuzları muted %25 |

## 10. Hareket
Süreler: 150 ms (chip/sekme), 250 ms (sheet) · Easing: Material standart · Nerede: yalnızca M3 bileşenlerinin kendi animasyonları; özel animasyon yok.

## 11. Yapılmayacaklar
- Gradyan yok · Emoji ikon yok · İç içe kart yok · İkinci vurgu rengi yok · Gölge yok · Ham hex kodda yok (yalnız Tokens.kt) · Ekranda iki birincil buton yok

## 12. Ekran yazarken hazır istem
"Sadece Tokens.kt token'ları ve ui/components. Kişilik: Net, Oyuncu, Güvenilir. Referans: Shazam/Zedge/GarageBand. Kart stili: çerçeve. Yapılmayacaklara uy."
