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

| Adım | Sonuç |
|---|---|
| Release APK (R8) kurulumu, açılış, tanıtım → ana sayfa | ✅ (ekran görüntüleri 01, 02) |
| "Telefondan ses dosyası seç" → sistem seçici → m4a dosyası (3:33) | ✅ |
| Android 8 depolama izni diyaloğu, işlem öncesi | ✅ (11_permission.png; D-011) |
| MediaCodec ile çözme → melodi çıkarımı cihazda | ✅ 340 nota (JVM'de 337; fark yeniden örnekleme kaynaklı), ~6 dk (yazılım emülatörü; gerçek telefonda saniyeler) |
| Sonuç ekranı: piyano rulosu, 8 chip, uzunluk, bölüm kaydırıcısı | ✅ (04_result.png) |
| Tını değiştirme (Müzik Kutusu) → yeniden sentez | ✅ |
| "Dinle" → AudioTrack ile önizleme, "Durdur" durumu | ✅ (AudioFlinger karıştırıcıya veri yazdı) |
| "Zil sesi yap" sheet → Kaydet | ✅ (D-009 sonrası sheet tam açık) |
| Dosya: `/storage/emulated/0/Ringtones/MelodiZil/Never_Gonna_Give_You_Up - Müzik Kutusu.wav`, 2.6 MB, 44.1 kHz mono 16-bit, 30.0 s, tepe −1 dBFS | ✅ (`ffprobe` ile doğrulandı; MP3 kopyası kullanıcıya gönderildi) |
| MediaStore satırı `is_ringtone=1` | ✅ (`content query`) |
| Sistem varsayılan zili → `content://0@media/external/audio/media/36` | ✅ (`settings get system ringtone`, öncesi: dahili 103) |
| Zillerim listesi, eylem sheet'i (zil/bildirim/alarm yap, paylaş, sil), ana sayfa "Son zillerin" | ✅ (07_library.png, 02b_home_recent.png) |
| Koyu mod (Ayarlar → Koyu) | ✅ (09, 10) |
| YouTube bağlantısı akışı cihazda | ⚠️ Doğrulanamadı: bu bulut ortamının çıkış IP'si test sırasında YouTube tarafından bot olarak engellendi ("Sign in to confirm you're not a bot"); aynı hata JVM testinde de alındı. Aynı kod yolu 07:35'te JVM'den gerçek YouTube'a karşı başarıyla çalıştı (çözümleme + indirme). Kullanıcının telefonunda (normal IP) denenmeli. |

Emülatörün bulduğu ve düzeltilen gerçek hatalar: D-008 (Android 13 altı çökme), D-009 (sheet butonu ekran dışı), D-010 (Android 8-9 MediaStore `_data`), D-011 (izin zamanlaması). Hepsi düzeltildi ve yeniden doğrulandı.

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
- Yakalanan gerçek hatalar (4): bkz. DERSLER D-008…D-011. Ders: "JVM testi yetmez; en düşük desteklenen API'de gerçek Android akışı koşulmadan 'bitti' denmez."
