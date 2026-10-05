# KALİTE RAPORU: Melodi Zil v1.0.0 (2026-10-05)

## Makine kontrolü
| Kontrol | Sonuç |
|---|---|
| `gradle :app:testDebugUnitTest` (CoreTest 11 test: FFT, MIDI, sentetik çok sesli melodi %92 doğruluk, sentez 8 tını, pencere, WAV, bölüm seçici, bağlantı ayrıştırma, resampler) | ✅ geçti |
| Canlı YouTube testi (`YouTubeLiveTest`, JVM'den gerçek YouTube): çözümleme + 3.4 MB m4a indirme | ✅ geçti (ilk çözümlemede boş akış → 3 deneme kuralı eklendi, D-006) |
| Gerçek şarkı testi (`RealSongHarnessTest`, "Never Gonna Give You Up" 3:33): 337 nota, çıkarım 1.0 s, perdeler A♭ majör gamında, 8 tını × 30 sn WAV üretildi | ✅ |
| Android Lint (`lintDebug`): 0 hata, 0 fatal; kalan 1 uyarı (strings "nota nota" tekrarı yanlış pozitif) | ✅ |
| `bundleRelease` (R8 küçültme + kaynak kırpma, imzalı) → 6.0 MB AAB · `assembleRelease` → 2.9 MB APK | ✅ |
| Token denetimi: `grep -rn "Color(0x" ui/` yalnızca `Tokens.kt`; ekranlarda ham hex yok; ikon boyutları 20/24 (+48 boş durum) | ✅ |

## Tutarlılık turu (DESIGN.md'ye göre)
- Ekran kenarı 16, bölüm boşluğu 24, kart padding 16, kart = çerçeve (gölge yok): tüm 6 ekranda `Screen` + `AppCard` ile zorunlu.
- Her ekranda tek birincil buton: Tanıtım "Başla", Ana "Melodiye çevir", İşlem (yok; İptal ikincil), Sonuç "Zil sesi yap", Zillerim (boş durumda "İlk zilini yap"), Ayarlar (yok).
- Oklar: liste satırı `KeyboardArrowRight` 20 muted; geri `ArrowBack` 24.

## Kullanılabilirlik
- Dokunma alanı: butonlar 52, liste satırları ≥64, chip'ler M3 varsayılan 32 + 8 boşluk (chip'ler Material yönergesine uygun; 48 kuralının bilinçli istisnası).
- Küçük ekran: tüm ekranlar dikey kaydırılabilir (`Screen(scrollable=true)`), Tanıtım hariç (içerik 640dp'ye sığar).
- Klavye: `adjustResize`; bağlantı alanı ekranın üstünde.
- Koyu mod: tüm renkler Tokens.kt'de çift tanımlı; emülatör ekran görüntüleri aşağıda.

## Gerçek Android üzerinde doğrulama (emülatör, KVM'siz yazılım modu)
Ortam: Android SDK emülatörü, API 26 (Android 8.0) x86, `-no-accel -gpu swiftshader_indirect`. (API 30 imajı bu ortamda `DeviceStorageMonitorService` NPE döngüsüyle sistem sunucusunu çökertti; API 26 kullanıldı.) Ağ: bulut ortamının TLS-araya-giren proxy CA'sı sistem deposuna eklendi, `adb reverse` ile proxy'ye yönlendirildi — yalnızca test ortamı gereği.

EMULATOR_SONUCLARI

## Kullanıcı testi
⏳ Kullanıcı `dist/melodi-zil-1.0.0.apk` dosyasını telefonuna kurup 2-3 şarkıyla denemeli ("onay" / "sorun: …").

## Yayın
- Paket: `dist/melodi-zil-1.0.0.aab` (Play), `dist/melodi-zil-1.0.0.apk` (doğrudan kurulum), SHA-256 `dist/SHA256SUMS.txt`.
- Mağaza metni, ikon 512, öne çıkan görsel, veri güvenliği formu cevapları: `docs/PLAY_STORE.md`, `docs/store/`.
- Gizlilik politikası: `docs/gizlilik.html` (GitHub Pages'te yayınlanmalı) + uygulama içi.
- ⚠️ Politika riski: YouTube indirme özelliği Play incelemesinde reddedilebilir; B planı `docs/PLAY_STORE.md`'de.

## Fabrikaya geri bildirim
- En çok zaman: emülatör (KVM yok) ve YouTube erişimi (proxy CA). Kural önerisi: "Gerçek cihaz doğrulaması için önce API 26 x86 imajı dene; API 30+ yazılım modunda kararsız."
- Eksik kural: "Kullanıcı tamamını bitir derse kapılar nasıl geçilir" (D-004) ve "native Kotlin istisnası" (D-005) — SKILL.md güncellemesi önerildi.
- Yakalanan gerçek hata: Android 8-9'da `WRITE_EXTERNAL_STORAGE` çalışma zamanı izni istenmiyordu (manifest'te vardı) → `AppNav.saveWithPermission` eklendi. Ders: "minSdk < 29 ise MediaStore yazımı için çalışma zamanı izni akışı zorunlu."
