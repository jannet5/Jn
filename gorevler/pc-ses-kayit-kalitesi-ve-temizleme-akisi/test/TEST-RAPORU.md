# Test raporu: kabul doğrulaması (2026-10-03, bulut Linux ortamı)

## Neyi test edebildim, neyi edemedim

| Kabul maddesi | Durum | Kanıt |
|---|---|---|
| Audacity dahil alternatifleri gerçek kullanıcı deneyimiyle karşılaştır | **Yapıldı** | [../arastirma.md](../arastirma.md): kaynaklı tablo. Audacity makrosu ile diğer yöntemler aynı girdilerde ölçüldü (aşağıda). |
| Kayıt öncesi ve sonrası ayarları takip akışına çevir | **Yapıldı** | [../KILAVUZ.md](../KILAVUZ.md) §2–4 (☐ tabloları). Kayıt öncesi ölçüm aracı `Kayit-Kontrol.bat` çalıştırıldı (çıktı aşağıda). |
| Mevcut **iki kullanıcı kaydında** önce/sonra dinleme karşılaştırması | **Yapılamadı: kayıtlar yüklenmedi** | Gerçek kayıt uydurulmadı. Yerine iki **benzetim** kaydıyla aynı akış uçtan uca çalıştırıldı ve ölçüldü. Kullanıcı kendi iki kaydını `Ses-Temizle.bat`'a bıraktığında aynı `_onceSonra.wav` ve `_rapor.txt` üretilir. |
| "Prodüksiyon kalitesi" ölçmeden vaat etme | **Uyuldu** | Her iddia aşağıdaki sayılara dayanıyor. En iyi sonuç bile stüdyo referansının altında. |
| Windows'ta gerçek çalıştırma | **Kısmen** | `deep-filter.exe` (Windows sürümü) Wine altında çalıştırıldı, çıktısı Linux sürümüyle **bit-bit aynı** (en büyük fark 0.0). `.ps1` betiği PowerShell 7.4.6'da (Linux) uçtan uca çalıştı, ayrıştırıcı hatası 0. **Windows PowerShell 5.1, `.bat` başlatıcılar, winget kurulumu ve gerçek mikrofon kaydı test EDİLMEDİ** (Windows masaüstü yok). |

## Test girdisi (benzetim, açıkça sentetik)

- **Temiz konuşma:** Microsoft DNS-Challenge test seti `clean_fileid_{0,2,3,4,5,6}.wav` (16 kHz, İngilizce).
- **Bozulma:** [arac/sim.py](arac/sim.py) ile "ucuz/dahili mikrofon" benzetimi. Her kayıt 3 sn oda sesi + 3 cümle, 35 sn.

| | Oda yankısı | Mikrofon | Gürültü | Seviye |
|---|---|---|---|---|
| kayit1 | RT60 0.35 sn | 150 Hz–7 kHz bant, 2.5 kHz tepe | pembe+kahverengi fan + hışırtı, SNR 15 dB | tepe −14 dBFS |
| kayit2 | RT60 0.5 sn (daha uzak) | aynı | SNR 10 dB + 50 Hz şebeke uğultusu (3 harmonik) | tepe −20 dBFS (kısık) |

## Ölçümler

- **DNSMOS P.835** (Microsoft, referanssız, 1–5): SIG = konuşma, BAK = arka plan, OVRL = genel kalite. Seviye etkisini kaldırmak için her dosya ölçümden önce −25 dBFS RMS'e ölçeklendi.
- **STOI** (0–1): temiz referansa göre anlaşılırlık.
- **LUFS / gerçek tepe:** ffmpeg ebur128.
- **Gürültü tabanı:** 0.5–2.5 sn oda sesinin RMS'i. Hepsi aynı seviyede (~−16 LUFS) olduğu için kıyaslanabilir.

Ham sonuçlar: [sonuclar.csv](sonuclar.csv) ve [sonuclar.jsonl](sonuclar.jsonl). Yeniden üretme: [arac/karsilastirma.sh](arac/karsilastirma.sh).

| Kayıt | Yöntem | DNSMOS OVRL | SIG | BAK | STOI | LUFS | Gerçek tepe | Gürültü tabanı (dBFS) |
|---|---|---|---|---|---|---|---|---|
| kayit1 | Ham kayıt (yalnız 80 Hz kesim + seviye eşitleme) | **1.19** | 1.48 | 1.20 | 0.740 | -16.2 | -1.4 | -32.0 |
| kayit1 | Audacity 3.7.3 makrosu (HPF+Noise Reduction 12 dB+Compressor+Loudness+Limiter) | **1.67** | 2.14 | 2.00 | 0.733 | -16.7 | -1.4 | -43.5 |
| kayit1 | Ses-Temizle klasik mod (ffmpeg afftdn) | **1.71** | 2.17 | 2.09 | 0.720 | -15.8 | -1.5 | -42.7 |
| kayit1 | ffmpeg arnndn (RNNoise sh modeli) | **1.92** | 2.44 | 2.29 | 0.697 | -16.0 | -1.5 | -47.5 |
| kayit1 | Ses-Temizle AI, -Guc 18 (hafif) | **2.55** | 3.31 | 2.98 | 0.760 | -16.0 | -1.5 | -43.5 |
| kayit1 | Ses-Temizle AI, -Guc 30 (doğal) | **2.85** | 3.33 | 3.61 | 0.763 | -16.1 | -1.5 | -54.8 |
| kayit1 | Ses-Temizle AI, varsayılan (tam güç) | **2.96** | 3.28 | 3.93 | 0.773 | -17.2 | -1.4 | -85.3 |
| kayit2 | Ham kayıt (yalnız 80 Hz kesim + seviye eşitleme) | **1.08** | 1.20 | 1.16 | 0.611 | -15.9 | -1.4 | -18.8 |
| kayit2 | Audacity 3.7.3 makrosu (HPF+Noise Reduction 12 dB+Compressor+Loudness+Limiter) | **1.14** | 1.28 | 1.25 | 0.591 | -16.7 | -1.4 | -37.5 |
| kayit2 | Ses-Temizle klasik mod (ffmpeg afftdn) | **1.51** | 2.04 | 1.99 | 0.591 | -15.7 | -1.5 | -36.6 |
| kayit2 | ffmpeg arnndn (RNNoise sh modeli) | **1.16** | 1.32 | 1.29 | 0.585 | -16.6 | -1.5 | -39.0 |
| kayit2 | Ses-Temizle AI, -Guc 18 (hafif) | **1.70** | 2.44 | 1.98 | 0.667 | -15.9 | -1.5 | -36.9 |
| kayit2 | Ses-Temizle AI, -Guc 30 (doğal) | **2.14** | 2.78 | 2.82 | 0.676 | -15.9 | -1.5 | -47.8 |
| kayit2 | Ses-Temizle AI, varsayılan (tam güç) | **2.46** | 2.79 | 3.81 | 0.677 | -16.9 | -1.5 | -74.2 |
| kayit1 | Temiz stüdyo referansı (üst sınır) | **3.29** | 3.59 | 4.02 | 1.000 | -27.8 | -6.0 | sayısal sessizlik |
| kayit2 | Temiz stüdyo referansı (üst sınır) | **3.23** | 3.54 | 4.02 | 1.000 | -26.3 | -6.0 | sayısal sessizlik |

### Yorum

1. **Ses-Temizle (AI, varsayılan) her iki kayıtta da açık ara en iyi.**
   - OVRL: 1.19 → 2.96 ve 1.08 → 2.46.
   - Stüdyo referansına göre bu, kalan aralığın sırasıyla %84'ü ve %64'ü kadar ilerleme demek.
   - Anlaşılırlık (STOI) hamdan **yüksek**: 0.740 → 0.773 ve 0.611 → 0.677.
2. **Audacity makrosu (gerçek Audacity 3.7.3 ile çalıştırıldı)**:
   - Orta gürültülü kayıtta işe yarıyor: 1.19 → 1.67.
   - Ağır gürültülü kayıtta neredeyse etkisiz: 1.08 → 1.14.
   - Anlaşılırlığı biraz düşürüyor. Bu, klasik spektral gürültü azaltmanın bilinen sınırıyla uyumlu.
3. **"Prodüksiyon kalitesi"ne ulaşılmadı.**
   - kayit2 (uzak mikrofon, yankılı oda) temizlenince bile 2.46'da kalıyor.
   - Yankı ve bant daralması yazılımla geri gelmiyor. Bu da kılavuzdaki "önce oda ve mesafe" önceliğini doğruluyor.
4. **Varsayılan güç seçimi:**
   - Ön seviye ayarı eklendikten sonra tam güç (100) iki kayıtta da en iyi sonucu verdi, bu yüzden varsayılan yapıldı.
   - Konuşma aralarını sayısal sessizliğe yakın bırakıyor. "Kesik kesik" gelirse `-Guc 30` (doğal) seçeneği sunuldu.
5. **Sıkıştırmanın sırası önemli.** İlk denemede gürültü bastırmadan sonra sıkıştırma yapılıyordu.

   | Sıra | kayit2 OVRL | STOI | gürültü tabanı |
   |---|---|---|---|
   | Önce temizle, sonra sıkıştır (ilk deneme) | 1.53 | 0.597 | −51.9 |
   | Önce sıkıştır, sonra temizle (son sürüm) | 2.14 | 0.676 | −47.8 |

   (Karşılaştırma `-Guc 30` ile yapıldı.)

### Görsel kontrol

[gorseller/kayit2_karsilastirma.png](gorseller/kayit2_karsilastirma.png): kayit2'nin ilk 12 sn'si, sırasıyla ham / Audacity makrosu / Ses-Temizle AI. Görsel açılıp incelendi:
- Ham kayıtta tüm bant gürültüyle dolu.
- Audacity makrosu gürültüyü kısmen düşürüyor ama konuşmanın arkasında ve aralarda kırmızı/turuncu gürültü kalıyor.
- AI sonucunda aralar koyu (gürültü yok). Konuşma sırasında ise arkada hafif mor gürültü bulutu kalıyor. Kulakta bu, konuşma ile birlikte gelip giden hafif bir hışırtı olarak duyulabilir.

## Betik uçtan uca testleri (PowerShell 7.4.6, Linux)

| Test | Sonuç |
|---|---|
| İki dosya birlikte (`Ses-Temizle.ps1 kayit1_ham.wav kayit2_ham.wav`) | Çıkış kodu 0. Her biri için `_temiz.wav`, `_onceSonra.wav` ve `_rapor.txt` oluştu. Toplam süre 38.8 sn (ölçümler dahil). |
| Boşluklu ve Türkçe karakterli MP3 (`boşluklu ad ğüş.mp3`) | Çalıştı, çıktılar doğru adla oluştu. |
| Olmayan dosya | Diğer dosyalar işlendi. Hata Türkçe raporlandı ve çıkış kodu 3 oldu. |
| `-Kontrol` modu | Ölçüm ve Türkçe öneriler (örnek aşağıda). |
| deep-filter yokken | "klasik moda geçiliyor" uyarısı verip çalışmaya devam etti. |
| Ayrıştırıcı denetimi (`Parser::ParseFile`) | `Ses-Temizle.ps1` ve `Kurulum.ps1`: 0 hata. |
| Kurulum testi sinyali (lavfi ton + pembe gürültü) | Üretildi ve temizlendi. |

Örnek `-Kontrol` çıktısı (kayit1):
```
Ölçüm   LUFS -34.9  Tepe -14.0 dBFS  Gürültü tabanı -49.8 dBFS  Konuşma-gürültü farkı 18.8 dB
 - Kayıt seviyesi uygun aralıkta.
 - Gürültü tabanı yüksek: fan/klima kapatın, mikrofona yaklaşın, yumuşak yüzeyli köşede kaydedin.
 - Konuşma ile gürültü arası fark düşük (<25 dB): yazılım bunu tam kurtaramaz, kayıt ortamını iyileştirin.
```

## Audacity makrosunun gerçek çalıştırılması

- **Ortam:** Audacity 3.7.3 (Ubuntu backports), Xvfb sanal ekranı, `mod-script-pipe`.
- **Gürültü profili:** Profil yokken seçili 0.5–2.5 sn'ye `NoiseReduction:` uygulandı. Belgelenmiş davranışa göre bu, seçimi profil olarak alır. Arayüzdeki "Get Noise Profile" ile aynı sonucu verir.
- **Makro:** Ardından `SelectAll:` ve `Macro_Ses-Temizle:` çalıştırıldı.
- **Sonuç:** 6/6 komut "BatchCommand finished: OK". Çıktı −16.7 LUFS ve −1.4 dBTP oldu. Yani yeni Compressor/Limiter parametre adları 3.7.x'te geçerli.
- **Noise Reduction ayarları:** audacity.cfg'de `[Effects/NoiseReduction] Gain=12 Sensitivity=6 FreqSmoothing=3` olarak verildi. Kullanıcı bunları pencereden bir kez girer.
- **Ekran görüntüsü:** Effect > Noise Removal and Repair > Noise Reduction… menü yolu doğrulandı.
- **Sınır:** Audacity 3.7.9 (Windows) ile değil, 3.7.3 (Linux) ile test edildi. Makro komutları 3.7 serisinde aynıdır, ama sürüm farkı not edilmiştir.

## Kullanıcının yapması gereken son kabul (Windows'ta)

1. `Kurulum.bat` dosyasını çalıştırın. Sonunda "[TAMAM] Çalışma testi geçti" yazmalı.
2. İki gerçek kaydınızı birlikte `Ses-Temizle.bat` üzerine bırakın.
3. `*_onceSonra.wav` dosyalarını kulaklıkla dinleyin ve `*_rapor.txt` dosyalarındaki "gürültü tabanı düştü" satırına bakın.
