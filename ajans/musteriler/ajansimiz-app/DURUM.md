# DURUM — ajansimiz-app
Hat: mobil · Mod: otonom (kapılarda onay beklenmedi, kararlar aşağıda)
| Adım | Durum |
|---|---|
| Intake (BRIEF.md) | ✅ ana marka BRIEF'inden türetildi |
| SPEC.md (Nick 5 alan + gotcha) | ✅ |
| KAPI-1 DESIGN.md | ✅ marka token'ları aynı + 3 konsept mini teması · ⚠️ `design.md lint` çalıştırılmadı |
| Üretim (5 ekran + 2 sheet) | ✅ `mobil/` |
| KAPI-2 Görsel QA | ✅ 2 tur · 390×844 açık+koyu · 36 SS `ss/tur2-*` · konsol hatası 0 · yatay taşma 0 |
| Android emülatör | ⏭️ atlandı: konteynerde /dev/kvm ve Android SDK yok |
| Tek HTML artifact | ⏭️ atlandı: 5 rotalı Expo Router + 4.6 MB bundle, artifact.py tek sayfa varsayıyor → SS yeterli |
| KAPI-3 20 kontrol | ⬜ |
| KAPI-4 Teslim (Expo Go'da gerçek telefon) | ⬜ insan |

## Kararlar
- K1 · Proje klasörü `mobil/` (create-expo-app `app` adını Expo Router'ın `src/app`'iyle karışmasın diye değiştirdim).
- K2 · Expo SDK 57 (güncel), RN 0.86, Reanimated 4.5, React Compiler açık (şablon varsayılanı).
- K3 · NativeWind 4.2.7 + Tailwind 3.4: **yalnızca düzen/tipografi/boşluk**. Renkler `src/theme/tokens.ts`'ten inline (konsept temaları JS'te gerekli: ikon rengi, Reanimated, header). Tek kaynak tokens.ts = DESIGN.md.
- K4 · `@expo/ui` BottomSheet yerine kendi `AltSayfa`'mız: görev "kendi BottomSheet/ConfirmSheet/Toast" istedi + web export'ta SS alınabilmesi için. Portal ile kökte açılır (header'ı da örter), tutamaç + sürükle-kapat + perdeye dokun-kapat.
- K5 · Font Archivo 400/500/800 (marka). Mobilde font-stretch yok → yalnız ağırlık + negatif harf aralığı.
- K6 · Kartlarda çerçeve yok, yüzey rengi farkı (native-slop #9). Ana marka DESIGN'ındaki "1px çerçeve kart" kuralından bilinçli sapma.
- K7 · Restoran siparişi demo: gerçek WhatsApp açmaz, gidecek mesajı gösterir + toast. "Bize ulaşın" gerçek `wa.me` linki açar (numara config'de boşsa toast).

## KAPI-2 öz puan (design-review ölçütleri, 4 üzerinden)
Hiyerarşi 4 · Tutarlılık 3 · Boşluk 3 · Tipografi 4 · Durumlar 3 (boş/dolu/kutlama/devre dışı/onay var; yükleniyor yalnız kafe sayacında "–") · Erişilebilirlik 3 (roller/etiketler var, ekran okuyucuyla denenmedi) · Konsol 4 → **ort. 3.4 ≥ 3 ✅**
Native-slop grep: Alert.alert 0 · TouchableOpacity 0 · RN Button 0 · RN SafeAreaView 0 · elevation 0 · headerShown:false 0 · emoji ikon 0.
