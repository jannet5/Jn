# Araştırma: kötü mikrofonla "prodüksiyona yakın" ses (2026-10-03)

**Yöntem.** Web araması yapıldı ve sayfalar açıldı. İşaretler:
- **[A]** sayfa açılıp okundu
- **[Ö]** yalnız arama sonucu özetinde görüldü

Reddit sayfaları bu ortamdan açılamadı (erişim engelli). Topluluk deneyimi bu yüzden Audacity forumu, inceleme siteleri ve makalelerde alıntılanan kullanıcı yorumlarından derlendi. Fiyatlar ve sürümler araştırma tarihine aittir.

## 1. Ana bulgular

1. **"Herkes Audacity mi kullanıyor?"**
   - Kayıt ve düzenleme için evet, ücretsiz ve yaygın.
   - Ama Audacity'nin klasik **Noise Reduction** etkisi spektral çıkarma yöntemidir. Fazla zorlanınca ses "su altında / robotik" olur:
     - [Ö] https://forum.audacityteam.org/t/hollow-slight-underwater-sound-in-voice-recordings/15466
     - [Ö] https://josephnilo.com/blog/noise-reduction-voice-sounds-robotic/
   - Audacity'nin kendi audiobook sayfası 6–6–6 ile başlamayı öneriyor ve "12'ye çıkman gerekiyorsa ses şarap kadehi gibi/genizden gelebilir" diyor: [A] https://support.audacityteam.org/au3/audio-editing/audiobook-mastering
2. **Audacity 4.0 (3 Eylül 2026) çıktı ama makro ve betik kanalı yok.**
   - Changelog'da "Macro Manager and the scripting pipe" henüz yok diye listeleniyor: [A] https://www.audacityteam.org/changelog/
   - Geçiş rehberi: [A] https://support.audacityteam.org/new-in-audacity-4/audacity-3-to-4-transition-guide.md
   - Bir forum moderatörüne göre OpenVINO yapay zekâ eklentileri de 4.0'da çalışmıyor: [A] https://forum.audacityteam.org/t/openvino-installation-in-audacity-4/152508
   - **Sonuç:** bu akış için Audacity **3.7.x** (son sürüm 3.7.9, 1 Eylül 2026).
3. **Yapay zekâ temizleyiciler klasik yöntemden belirgin şekilde iyi**, ama sihir değil.
   - Adobe Podcast Enhance için "yeni mikrofon alma, bunu kullan" türü yazılar var: [Ö] https://windowscentral.com/software-apps/no-you-dont-need-a-new-microphone-with-adobe-podcast-beta-ai
   - Aynı araç için "beni kötü bir metin-okuma sesi gibi robotik yaptı / yapay zekâ gibi tınlattı" şikâyetleri de var: [A] https://thepodcastconsultant.com/blog/adobe-ai-tools
   - Bir incelemeci: "iyi bir USB mikrofon yine de her yapay zekâ temizliğini geçer": [A] https://daveswift.com/adobe-enhance/
   - İnsanların gerçekten mikrofon almayı bıraktığına dair güvenilir veri **bulunamadı**.
4. **Topluluk uzlaşısı: teknik yazılımdan önce gelir.**
   - Audacity forumu: "Yankılardan yazılımla temiz şekilde kurtulmanın yolu yok." Önce kalın battaniyeler, mikrofona yaklaşmak önerilir: [A] https://forum.audacityteam.org/t/what-can-i-do-to-reduce-the-echo-on-my-recording/58846
   - Yankı, sesin mikrofona zaman farkıyla birden çok kez ulaşmasıdır ve süzülemez: [A] https://forum.audacityteam.org/t/how-to-remove-echo-from-recording/26894
   - Noise Reduction sabit gürültü içindir: [A] https://manual.audacityteam.org/man/noise_reduction.html
   - Değişken gürültü temizlenemez ("daha sessiz bir odada tekrar okuyun"): [A] https://support.audacityteam.org/au3/audio-editing/audiobook-mastering

## 2. Karşılaştırma tablosu (Windows, bütçesiz kullanıcı)

| Araç | Ücret | Yerel / bulut | GPU | Anlık / sonradan | Bilinen sorunlar | Kaynak |
|---|---|---|---|---|---|---|
| Audacity 3.7.x yerleşik (Noise Reduction, Compressor, Loudness Normalization) | Ücretsiz | Yerel | Hayır | Sonradan | Fazla ayarda robotik, "su altı" ses | [A] https://manual.audacityteam.org/man/noise_reduction.html |
| OpenVINO AI eklentisi (Noise Suppression: DeepFilterNet2/3, denseunet) | Ücretsiz | Yerel (CPU/iGPU/NPU) | Hayır | Sonradan | Ani "kapı" etkisi, boğukluk; yalnız 3.7.x | [A] https://github.com/intel/openvino-plugins-ai-audacity, [A] https://forum.audacityteam.org/t/a-caution-about-openvino-ai-noise-suppression/117904 |
| **DeepFilterNet3 (deep-filter.exe)** | Ücretsiz (MIT/Apache) | Yerel | Hayır | Sonradan | 48 kHz WAV ister | [A] https://github.com/rikorose/deepfilternet |
| Adobe Podcast Enhance Speech | Ücretsiz: günde 1 sa, dosya ≤30 dk; Premium 9.99 $/ay | **Bulut** | Hayır | Sonradan | Robotik/peltek ses, hece yutma; V2 aşırı işleme | [A] https://thepodcastconsultant.com/blog/adobe-ai-tools, [A] https://gauravtiwari.org/adobe-podcast-review/ |
| NVIDIA Broadcast | Ücretsiz | Yerel | **RTX 2060+** | Anlık | — | [A] https://www.nvidia.com/broadcast-app-faq |
| NVIDIA RTX Voice (eski) | Ücretsiz | Yerel | GTX de olur | Anlık | Eski, yerini Broadcast aldı | [Ö] https://nvidia.com/en-us/geforce/guides/nvidia-rtx-voice-setup-guide |
| Krisp | 60 dk/gün ücretsiz | Yerel | Hayır | Anlık | Görüşme odaklı | [Ö] https://www.meetjamie.ai/en/blog/krisp-ai-pricing |
| SteelSeries Sonar ClearCast AI | Ücretsiz | Yerel | Hayır | Anlık | Yüksek ayarda robotik ses | [Ö] https://evezone.evetech.co.za/deep-dives/steelseries-clearcast-mic-settings |
| Windows 11 Voice Clarity | Ücretsiz | Yerel | Hayır | Anlık | Yalnız "iletişim modu" uygulamalarında (Audacity'de değil) | [Ö] https://www.techradar.com/computing/windows/windows-11s-ai-powered-voice-clarity-feature-improves-your-video-chats-plus-setup-has-a-new-look-finally |
| OBS gürültü bastırma (RNNoise / Speex / NVIDIA) | Ücretsiz | Yerel | Yalnız NVIDIA seçeneği | Anlık | "Hafif" gürültü için | [A] https://obsproject.com/kb/noise-suppression-filter |
| RNNoise VST (werman) | Ücretsiz | Yerel | Hayır | Anlık | Yalnız 48 kHz | [A] https://github.com/werman/noise-suppression-for-voice |
| ffmpeg (afftdn, arnndn, loudnorm) | Ücretsiz | Yerel | Hayır | Sonradan | Komut satırı | [A] https://ffmpeg.org/ffmpeg-filters.html, [A] https://github.com/GregorR/rnnoise-models |
| Resemble Enhance, VoiceFixer, ClearerVoice | Ücretsiz | Yerel (Python) | İsteğe bağlı | Sonradan | Resemble "içerik uydurabiliyor" | [A] https://github.com/resemble-ai/resemble-enhance, [Ö] https://arxiv.org/pdf/2603.02641 |
| Auphonic | 2 sa/ay ücretsiz (jingle ekler) | Bulut | Hayır | Sonradan | — | [A] https://auphonic.com/pricing |
| iZotope RX 12 | Elements 99 $, Standard 399 $ | Yerel | Hayır | Sonradan | Ücretli; sektör standardı | [Ö] https://www.soundonsound.com/news/izotope-rx-12-here |
| Descript Studio Sound | Ücretli plan | Bulut | Hayır | Sonradan | — | [Ö] https://sonix.ai/resources/descript-pricing/ |

## 3. Kayıt ve ayar kaynakları (kılavuzdaki değerlerin dayanağı)

### Windows
- Mikrofon kurulumu ve giriş seviyesi: [A] https://support.microsoft.com/en-us/windows/hardware/drivers/how-to-set-up-and-test-microphones-in-windows
- Masaüstü uygulamalarının mikrofon izni: [A] https://support.microsoft.com/windows/privacy/windows-camera-microphone-and-privacy
- Ses geliştirmelerini kapatma: Microsoft sayfası bunu oynatma aygıtı için anlatıyor. Kayıt aygıtına uygulamak çıkarımdır: [A] https://support.microsoft.com/en-us/windows/fix-sound-or-audio-problems-in-windows-73025246-b61c-40fb-671a-2535c7cd56c8

### Audacity ayarları
- Ana makine seçenekleri (MME varsayılan; WASAPI 24-bit destekler): [A] https://manual.audacityteam.org/man/devices_preferences.html
- Mono önerisi ve −18 ile −12 dB seviye: [A] https://support.audacityteam.org/au3/basics/recording-your-voice-and-microphone.md
- 32-bit float varsayılanında kalın: [A] https://manual.audacityteam.org/man/quality_preferences.html
- Makrolar:
  - Noise Reduction makroda son ayarlarla ve var olan profille çalışır: [A] https://manual.audacityteam.org/man/macros.html, [A] https://support.audacityteam.org/au3/audio-editing/macros/macros-examples
  - Betik komut adları (`High-passFilter`, `LoudnessNormalization` ...): [A] https://manual.audacityteam.org/man/scripting_reference.html
  - 3.6+ yeni Compressor/Limiter parametre adları (kaynak kod): [A] https://github.com/audacity/audacity/blob/release-3.7.5/src/effects/CompressorEditor.h
  - Bu adlar bu çalışmada Audacity 3.7.3 üzerinde gerçekten çalıştırılarak da doğrulandı (bkz. test raporu).

### Ses yüksekliği hedefi
- Apple Podcasts −16 LKFS ±1, gerçek tepe ≤ −1 dBFS: [A] https://podcasters.apple.com/support/audio-requirements
- −16 LUFS stereo / −19 mono, Spotify ve YouTube −14: [A] https://podnews.net/article/lufs-lkfs-for-podcasters
- ACX (sesli kitap) alternatifi: RMS −23 ile −18, tepe < −3 dB, gürültü tabanı < −60 dB: [A] https://help.acx.com/s/article/acx-audio-submission-requirements

### Doğrulanamayanlar
Aşağıdakiler kılavuzda "doğrulanmamış topluluk ipucu" diye işaretlendi:
- "Özel erişim (exclusive mode) kapatılmalı"
- "48 kHz/24-bit Windows varsayılan biçimi" için resmî Microsoft önerisi
- Çoraptan pop filtresi
- Compressor için −18 dB / 3:1 değerleri (topluluk değeri; testte işe yaradı)

## 4. Seçim gerekçesi

- **Kullanıcının tercihi Audacity.** Korundu: kayıt, düzenleme ve isteğe bağlı makro yolu Audacity'de.
- **Temizlik için DeepFilterNet3 seçildi.** Sebepleri:
  1. Ücretsiz ve açık kaynak.
  2. Windows için hazır tek dosya (`deep-filter.exe`).
  3. GPU, hesap veya internet gerektirmez; ses buluta gitmez.
  4. OpenVINO Audacity eklentisinin de sunduğu modelle aynı aile.
  5. **Bu çalışmadaki ölçümde açık ara en iyi sonucu verdi.** Klasik Noise Reduction, RNNoise ve afftdn ile karşılaştırma: [test/TEST-RAPORU.md](test/TEST-RAPORU.md)
- **Elenenler:**
  - NVIDIA Broadcast: RTX kart gerekir, donanım belirsiz.
  - Adobe Podcast: bulut, gizlilik, robotik ses şikâyetleri, günlük sınır. İsteğe bağlı karşılaştırma yolu olarak bırakıldı.
  - iZotope RX: ücretli, bütçe yok.
