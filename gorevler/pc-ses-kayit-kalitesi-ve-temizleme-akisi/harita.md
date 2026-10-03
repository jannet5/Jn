# Harita: hedef → bağımlılıklar → A/B/C yolları → uygulama → test → teslim

```
HEDEF: Yeni mikrofon almadan, mevcut PC mikrofonuyla ölçülebilir şekilde daha iyi konuşma kaydı
  │     (kullanıcı tercihi: Audacity; Windows PC; bütçe yok)
  │
  ├─ BAĞIMLILIKLAR
  │    B1 Kayıt tekniği ve oda (yazılımın düzeltemediği kısım) ─────────► KILAVUZ §2–3
  │    B2 Windows/Audacity ayarları (izin, seviye, iyileştirmeler, 48 kHz mono) ► KILAVUZ §2
  │    B3 Temizleme aracı (ücretsiz, yerel, GPU'suz, Windows) ──────────► A/B/C
  │    B4 Ölçüm yöntemi (söz vermeden önce ölçmek) ─────────────────────► DNSMOS + STOI + LUFS
  │    B5 Gerçek kullanıcı kayıtları (iki kayıt) ───────────────────────► ✗ YÜKLENMEDİ (engel)
  │
  ├─ YOLLAR (B3)
  │    A) Audacity içi: yerleşik efektler + makro (HPF → Noise Reduction → Compressor → Loudness → Limiter)
  │         + İsteğe bağlı: OpenVINO DeepFilterNet3 eklentisi (yalnız 3.7.x, Windows)
  │    B) Audacity dışı yerel yapay zekâ "düğmesi": ffmpeg + DeepFilterNet3 (deep-filter.exe)   ◄── SEÇİLDİ (birincil)
  │    C) Bulut: Adobe Podcast Enhance / Auphonic (gizlilik, kota, robotik ses riski)            ◄── isteğe bağlı karşılaştırma
  │    (Elenen: NVIDIA Broadcast → RTX şart; iZotope RX → ücretli; Resemble/VoiceFixer → Python kurulumu ağır,
  │     içerik uydurma riski)
  │
  ├─ UYGULAMA
  │    U1 ses-temizleyici/Ses-Temizle.ps1  (+ .bat başlatıcılar, Kurulum.ps1, Kayit-Kontrol)   ← B yolu
  │    U2 audacity-makro/Ses-Temizle.txt                                                      ← A yolu
  │    U3 KILAVUZ.md: kayıt öncesi / sırasında / sonrası takip tabloları                     ← B1, B2
  │    U4 arastirma.md: karşılaştırma ve kaynaklar                                           ← kabul 1
  │
  ├─ TEST / KABUL
  │    T1 Bozuk mikrofon benzetimi: 2 kayıt (fan + yankı + dar bant; ikincisi uğultulu ve kısık) ← B5 yerine
  │    T2 Aynı girdilerle: ham / Audacity 3.7.3 makrosu (gerçek Audacity) / klasik / RNNoise / AI 18-30-100
  │    T3 Ölçüm: DNSMOS P.835 (seviyeden bağımsız), STOI (anlaşılırlık), LUFS, tepe, gürültü tabanı
  │    T4 Betik uçtan uca: PowerShell 7 (Linux), boşluklu/Türkçe karakterli dosya adı, mp3 girdi,
  │       hatalı dosya, -Kontrol modu
  │    T5 deep-filter.exe (Windows sürümü) Wine altında, Linux sürümüyle bit-bit aynı çıktı
  │    T6 Görsel: önce/sonra spektrogramı gerçekten açılıp incelendi
  │
  └─ KALICI TESLİM
       D1 Public dal claude/laughing-faraday-jqcz19 → gorevler/pc-ses-kayit-kalitesi-ve-temizleme-akisi/
          (yalnız kod, kılavuz, araştırma, ölçümler; özel sohbet/kaynak YOK)
       D2 Ürün ZIP'i (2. tur): YALNIZ ürün klasörü (git'teki gorevler/... ağacının birebir kopyası), kök = ürün
          klasörü; özel sohbet, ozel/, test sesleri ve başka proje geçmişi (git bundle) YOK; SHA-256 + açıp
          uzak commit'teki dosyalarla bayt bayt karşılaştırma. deep-filter.exe: Kurulum.bat indirir + SHA-256 doğrular.
  │
  └─ 2. TUR (bağımsız inceleme) → bkz. aşağıdaki "2. tur" tablosu ve test/TEST-RAPORU.md
```

## Yol kırılmaları ve çözümleri (çalışma sırasında yaşananlar)

| Kırılma | Ne yapıldı |
|---|---|
| Audacity 4.0 makro ve eklenti desteklemiyor | A yolu Audacity 3.7.x'e sabitlendi. Kılavuzda belirtildi. |
| `NoiseReduction` betikle profil alamıyor (resmî belge: "scripting'de yok") | Belgelenmiş davranış kullanıldı: profil yokken seçili bölgeye uygulanınca o bölge profil olur. Kullanıcı için arayüzden "Get Noise Profile" adımı eklendi. Bulutta gerçekten çalıştırılıp doğrulandı. |
| Sıkıştırma gürültü bastırmadan sonra yapılınca gürültü tabanı ~17 dB kötüleşti | Sıra değişti: seviye + sıkıştırma → yapay zekâ → loudnorm. Ölçümler: kayıt 2'de OVRL 1.53 → 2.14 (Guc 30), STOI 0.597 → 0.676. |
| Çok kısık girişte DeepFilterNet tam güçte konuşmayı da bastırdı (STOI 0.437) | Ön seviye ayarı (−20 LUFS) ile çözüldü. Tam güç artık en iyi sonuç (STOI 0.677) ve varsayılan yapıldı. |
| ffmpeg afftdn `tn=1` ile hiç azaltma yapmadı | `nt=w`, `nr=20` ve ölçülen tabana göre `nf` ile düzeltildi. Klasik mod artık Audacity makrosuyla eşit veya daha iyi. |
| Kullanıcının iki gerçek kaydı yok | Uydurulmadı. Benzetim kayıtlarıyla kabul yapıldı. Gerçek kayıtlar için düğme aynı önce/sonra dosyasını ve raporu üretir (kılavuz §4). |

## 2. tur: bağımsız inceleme bulguları

```
İNCELEME BULGUSU                       → DÜZELTME                                    → TEST [L]=Linux PS7, [W]=Wine+sahte PS
1 BAT çıkış kodunu maskeliyor          → SES_KOD sakla, exit /b SES_KOD              → [W] bat_wine_testi.sh 11/11
2 winget sonucu körlemesine başarı     → kod sınıflama + ffmpeg -version doğrulaması → [L] 12 sınıf + 5 uçtan uca senaryo
3 sabit TEMP test WAV + joker silme    → GUID klasör, ayrı süreç, içerik doğrulama   → [L] Kurulum 10/10, kalan klasör 0
4 makro -Force üzerine yazma           → aynıysa "güncel", farklıysa "(2)" yeni dosya → [L] kullanıcı makrosu SHA aynı
5 ffmpeg -y kullanıcı dosyası üzerine  → GUID klasöre -n, File.Move, (2) adlandırma   → [L] eski çıktı/kullanıcı dosyası SHA aynı
6 hata yolunda GUID klasörü kalıyor    → finally temizliği                            → [L] 4 hata senaryosunda kalan klasör 0
+ testin bulduğu: tam güç sesi silince -inf → Türkçe hata, sessiz kayıt tespiti, kurulum testi -Guc 30 → [L] 10/10
YAPILMADI: Windows PowerShell 5.1, gerçek cmd.exe, gerçek winget, gerçek mikrofon, kullanıcının iki gerçek kaydı
```

| Kırılma | Ne yapıldı |
|---|---|
| Linux'ta süreç çıkış kodu 8 bit; winget'in negatif 32 bit kodları sahte süreçle test edilemedi | Sınıflandırma fonksiyonu Kurulum.ps1'in AST'sinden alınıp doğrudan test edildi. Uçtan uca testler 0–255 aralığındaki kodlarla yapıldı. |
| Parantezli/boşluklu BAT adı ("Ses-Temizle (dogal).bat") komut satırından çağrılınca yanlış dosya çalıştı | Ad `Ses-Temizle-Dogal.bat` oldu. Belgeler güncellendi. |
| Wine'da Türkçe argümanlar ilk denemede bozuk göründü | Sebep test ortamının yerel ayarıydı (LANG boş). `LC_ALL=C.UTF-8` ile argümanların bozulmadan iletildiği doğrulandı. Gerçek Windows davranışı ayrıca test edilmedi. |
