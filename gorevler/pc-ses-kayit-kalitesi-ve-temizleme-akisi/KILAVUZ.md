# Kötü mikrofonla en iyi kayıt: takip kılavuzu

Bu kılavuz yeni mikrofon almadan, PC'nin mevcut mikrofonuyla alınabilecek en temiz konuşma kaydı içindir.
Sırayla okuyun: önce kısa cevap, sonra **kayıt öncesi**, **kayıt sırasında** ve **kayıt sonrası** tabloları.
Tablolardaki her satırı uygulayıp ☐ kutusunu işaretleyin.

---

## 1. Kısa cevap: "Sadece Audacity mi kullanacağım?"

**Hayır, tek başına Audacity yetmez.** Kurulacak düzen şu:

| İş | Araç | Neden |
|---|---|---|
| Kaydı almak, kesip düzenlemek | **Audacity 3.7.x** (ücretsiz) | Kayıt ve kesme için yeterli, herkesin kullandığı araç. |
| Gürültüyü temizlemek | **Ses-Temizle düğmesi** (bu pakette) | Testlerde açık ara en iyi sonucu verdi. İçinde DeepFilterNet3 yapay zekâsı var. Bilgisayarda, internetsiz çalışır. |
| Yalnız Audacity içinde kalmak isterseniz | **Audacity makrosu "Ses-Temizle"** (bu pakette) | Tek tıkla 5 adım uygular. Testlerde düğmeden belirgin şekilde zayıf kaldı. |

**Testte ne ölçtük?** İki "kötü mikrofon" test kaydında (fan sesi, uğultu, oda yankısı) ölçülen kalite (DNSMOS, 1–5 arası, yüksek iyi):

| Durum | Puan (kayıt 1 / kayıt 2) |
|---|---|
| Ham kayıt | **1.19 / 1.08** |
| Audacity makrosu | **1.67 / 1.14** |
| Ses-Temizle düğmesi | **2.96 / 2.46** |
| Gerçek stüdyo kaydı (üst sınır) | 3.29 / 3.23 |

Ayrıntı: [test/TEST-RAPORU.md](test/TEST-RAPORU.md)

**Prodüksiyon kalitesi sözü veremiyorum.** Hiçbir yazılım kötü bir odayı ya da uzaktaki mikrofonu tam olarak kurtaramaz; kullanıcı toplulukları ve Audacity forumu da aynısını söylüyor ([araştırma](arastirma.md)). Kazancın en büyük kısmı iki şeyden geliyor:
- **doğru kayıt**: Bölüm 2–3
- **yapay zekâ ile temizleme**: Bölüm 4

---

## 2. KAYIT ÖNCESİ (bir kez ayarlanır)

| ☐ | Ne yapılacak | Nasıl | Neden |
|---|---|---|---|
| ☐ | Audacity **3.7.x** kurun | https://www.audacityteam.org/download/ (3.7.9 için "older versions" sayfası) | 4.0 çıktı (Eylül 2026) ama makro ve OpenVINO AI eklentilerini henüz desteklemiyor. |
| ☐ | Ses-Temizle'yi kurun | ZIP'i açın, `ses-temizleyici\Kurulum.bat` dosyasına çift tıklayın. | deep-filter.exe'yi doğrular, ffmpeg'i kurar (winget) ve Audacity makrosunu ekler. Sonra kendi kendini test eder. Sonda **"[TAMAM] Çalışma testi geçti"** görmeden kurulumu bitmiş saymayın; hata olursa pencere nedenini (ağ / izin / paket bulunamadı) ve çıkış kodunu yazar. |
| ☐ | Mikrofon izni | Ayarlar > Gizlilik ve güvenlik > Mikrofon > "Masaüstü uygulamalarının mikrofona erişmesine izin ver" **Açık** | Audacity bir masaüstü uygulamasıdır. İzin kapalıysa sessiz kayıt alırsınız. |
| ☐ | Giriş seviyesi | Ayarlar > Sistem > Ses > Giriş > mikrofonunuz > "Giriş ses düzeyi". Başlangıç için 70–85 | Bunu "Kayıt-Kontrol" testine göre ince ayarlayacaksınız (aşağıda). |
| ☐ | Ses iyileştirmelerini kapatın | Aynı sayfada "Ses geliştirmeleri / Audio enhancements" **Kapalı**. Varsa "Voice Clarity / Ses netliği" de kapalı. | Windows'un otomatik seviye ve gürültü işlemesi sesi pompalar ve boğuklaştırır. Temizliği biz kendimiz, kontrollü yapacağız. |
| ☐ | Audacity: Ses Ayarları (Audio Setup) | Ana makine: **Windows WASAPI** (sorun çıkarsa MME). Kayıt aygıtı: mikrofonunuzun **adı** ("Sound Mapper" değil). Kanal: **1 (Mono)**. | Çoğu mikrofon tek kanallıdır. Audacity belgeleri de mono'yu önerir. |
| ☐ | Audacity: Proje hızı | Sol alttaki Proje Hızı: **48000 Hz**. Örnek biçimi: 32-bit float (varsayılan, dokunmayın). | Yapay zekâ temizleyici 48 kHz ile çalışır. |
| ☐ | Mekân seçimi | Perdenin, yatağın, yorganın, içi kıyafet dolu dolabın önü. Sert duvar köşesinden ve boş odadan uzak durun. | Bkz. **Bölüm 6, soru 1**. Yankı sonradan temizlenemez. |
| ☐ | Gürültü kaynakları | Fan, klima ve buzdolabı kapalı ya da uzakta. Telefon sessizde. Laptop fanı bağırıyorsa ağır programları kapatın. | Sabit gürültü kısmen temizlenir, değişken gürültü (konuşma, trafik) temizlenmez. |
| ☐ | **Kayıt-Kontrol testi** | 10–15 sn normal sesle konuşup kaydedin. WAV olarak dışa aktarın. Dosyayı `Kayit-Kontrol.bat` üzerine bırakın. | Seviye, kırpılma ve oda gürültüsü için ne yapmanız gerektiğini Türkçe söyler. "Kayıt seviyesi uygun" ve "gürültü tabanı kabul edilebilir/çok iyi" görene kadar tekrarlayın. |

**Hedef değerler** (Kayıt-Kontrol bunları ölçer):

| Ölçüm | İyi | Kabul edilebilir | Kötü |
|---|---|---|---|
| En yüksek tepe | −12 ile −6 dBFS arası | −24 ile −3 arası | −0.3 üstü (kırpılma) veya −24 altı |
| Audacity kayıt göstergesi (normal konuşma) | −18 ile −12 dB | — | sarı/kırmızıya giriyorsa düşürün |
| Gürültü tabanı (konuşmadığınız an) | −60 dBFS ve altı | −50 dBFS'e kadar | −50 üstü |
| Konuşma–gürültü farkı | 35 dB+ | 25 dB+ | 25 dB altı (yazılım da kurtaramaz) |

---

## 3. KAYIT SIRASINDA (her kayıtta)

| ☐ | Ne yapılacak | Neden |
|---|---|---|
| ☐ | **Kayda 3 sn sessizlikle başlayın.** Kıpırdamayın, konuşmayın. | Temizleyicinin gürültü örneği bu sessizlikten alınır. Audacity'nin "Gürültü profili" de buradan seçilir. |
| ☐ | Mikrofona **10–20 cm** (bir karış kadar) yakın durun. Laptop mikrofonuysa ekrana doğru, yakından ve hep aynı mesafeden konuşun. | Mesafe yarıya inince sesiniz odaya göre belirgin şekilde güçlenir. Bu, hiçbir yazılımın veremeyeceği kazançtır. |
| ☐ | Mikrofona dosdoğru değil, **hafif yandan** konuşun. Harici mikrofonda araya ince bir çorap veya tül germek patlamaları azaltabilir (doğrulanmamış topluluk ipucu). | "P", "B" patlamaları mikrofonu boğar. Bunlar sonradan zor düzelir. |
| ☐ | Kayıt göstergesini izleyin: normal konuşma **−18 ile −12 dB** arasında olsun, kırmızıya hiç girmesin. | Kırpılan ses geri getirilemez. Çok kısık ses ise temizlerken gürültüyle birlikte büyür. |
| ☐ | Hata yaparsanız durmayın. 2 sn bekleyip cümleyi baştan söyleyin, sonra kesersiniz. | Akıcı kayıt, sonradan kesmesi kolay kayıt demektir. |
| ☐ | Bitince **Dosya > Dışa Aktar > WAV** (16 veya 24-bit). Proje dosyası (.aup3) ayrıca kaydedilebilir. | Temizleyici WAV, MP3 ve M4A kabul eder. WAV kayıpsızdır. |

---

## 4. KAYIT SONRASI: iki yol var, ilki önerilir

### Yol 1: Ses-Temizle düğmesi (önerilen, en iyi ölçüm)

| ☐ | Adım | Sonuç |
|---|---|---|
| ☐ | WAV dosyanızı (birden fazla da olur) **`Ses-Temizle.bat`** üzerine sürükleyip bırakın. | Siyah bir pencere açılır ve adımları yazar (35 sn'lik test kaydı bulut sunucusunda ~20 sn sürdü; sizin bilgisayarınızda farklı olabilir). |
| ☐ | Kaydın yanında 3 dosya oluşur. | `ad_temiz.wav` (bitmiş ses, −16 LUFS, 48 kHz/24-bit), `ad_onceSonra.wav` ve `ad_rapor.txt`. **Hiçbir dosyanın üzerine yazılmaz:** aynı adlı dosya varsa yeniler `ad_temiz (2).wav` gibi numaralı ad alır. Ham kaydınıza hiç dokunulmaz. İşlem yarıda kalırsa klasörde yarım dosya bırakılmaz. |
| ☐ | **`ad_onceSonra.wav`** dosyasını **kulaklıkla** dinleyin. Windows'un normal oynatıcısı yeterli, Audacity açmanız gerekmez. | Önce ham kayıt çalar, "bip"ten sonra temiz kayıt. İkisi aynı ses yüksekliğine getirilmiştir, böylece "yüksek olan daha iyi geliyor" yanılgısı olmaz. |
| ☐ | Ses boğuk, robotik ya da konuşma araları "kesik kesik" geldiyse aynı ham dosyayı **`Ses-Temizle-Dogal.bat`** üzerine bırakın. | Gürültüyü en fazla 30 dB azaltır, aralarda hafif oda sesi kalır. Testte ölçüm biraz düşük ama daha doğal. |
| ☐ | İsterseniz `ad_temiz.wav`'ı Audacity'de açıp kesin ve paylaşın. **Tekrar Noise Reduction uygulamayın.** | Çift temizlik sesi metalikleştirir. |

**Bu "düğme" nedir?** Bkz. **Bölüm 6, soru 3**.

### Yol 2: Tamamen Audacity içinde (makro)

Audacity 3.7.x gerekir. Kurulum.bat makroyu ekler; sizin daha önce değiştirdiğiniz bir `Ses-Temizle.txt` varsa ona dokunmaz, yenisini `Ses-Temizle (2).txt` adıyla koyar. Elle kurmak için Araçlar > Makro Yöneticisi > İçe Aktar > `audacity-makro\Ses-Temizle.txt`.

| ☐ | Adım (her kayıt için) | Not |
|---|---|---|
| ☐ | Kaydın başındaki **3 sn sessizliği** fareyle seçin. | Yalnız sessizlik olsun, nefes ya da konuşma olmasın. |
| ☐ | **Effect > Noise Removal and Repair > Noise Reduction…** (Türkçe arayüzde: Etki > Gürültü Kaldırma ve Onarma > Gürültü Azaltma) > **Get Noise Profile** (Gürültü Profilini Al) | Pencere kapanır, profil hafızaya alınır. |
| ☐ | İlk sefere özel: aynı pencereyi tekrar açın, **Noise reduction 12 dB, Sensitivity 6, Frequency smoothing 3** yazıp pencereyi **Close/Kapat** ile kapatın. | Makro, Noise Reduction'ı **son kullanılan ayarlarla** çalıştırır. Ses metalik/"şişede" gibi gelirse 6–9 dB'e düşürün. Audacity'nin resmî önerisi 6–6–6 ile başlayıp yükseltmek. |
| ☐ | **Ctrl+A** ile tümünü seçin, sonra **Tools > Apply Macro > Ses-Temizle** (Araçlar > Makro Uygula). | Sırayla şunlar uygulanır: 80 Hz alt kesim, gürültü azaltma, 3:1 sıkıştırma, −16 LUFS ses yüksekliği ve −1.5 dB sınırlayıcı. |
| ☐ | Dinleyin, sonra **File > Export > Export as WAV** (veya MP3, 192 kbps+). | — |

### Yol 3 (isteğe bağlı): Audacity içinde yapay zekâ, Intel OpenVINO eklentisi

- Audacity **3.7.x** için ücretsiz "OpenVINO AI" eklentisinde **Noise Suppression** ve **DeepFilterNet3** modeli var. Bu, Ses-Temizle'nin kullandığı modelle aynı ailedir.
- Kaynaklar: https://github.com/intel/openvino-plugins-ai-audacity/releases ve MuseHub'daki "OpenVINO AI Tools".
- **Bu bulut testinde denenemedi**, çünkü yalnız Windows'ta çalışıyor. Kurarsanız: Effect > OpenVINO AI Effects > Noise Suppression > DeepFilterNet3. Ardından Yol 2'deki makroyu, Noise Reduction adımını atlayarak kullanabilirsiniz.

### Yol 4 (bulut, isteğe bağlı): Adobe Podcast "Enhance Speech"

- Ücretsiz katman: günde 1 saat, dosya başına 30 dk / 500 MB.
- Ses Adobe sunucusuna yüklenir (gizlilik).
- Kullanıcıların sık şikâyeti: "robotik / yapay zekâ sesi gibi" sonuç, özellikle V2'de.
- Karşılaştırma için deneyebilirsiniz ama ana yol olarak önermiyorum.

---

## 5. Sorun giderme

| Belirti | Çözüm |
|---|---|
| Pencere "ffmpeg bulunamadı" diyor | `Kurulum.bat`'ı tekrar çalıştırın. winget yoksa https://www.gyan.dev/ffmpeg/builds/ adresinden "release essentials" indirin ve `bin\ffmpeg.exe`'yi `ses-temizleyici\araclar\` içine koyun. |
| "deep-filter bulunamadı; klasik moda geçiliyor" | `araclar\deep-filter.exe` silinmiş olabilir. `Kurulum.bat` onu yeniden indirir ve SHA-256 ile doğrular. |
| Pencerede "[HATA] Islem basarisiz oldu (cikis kodu N)" | 1 = dosya bırakılmadı, 2 = ffmpeg yok (Kurulum.bat), 3 = en az bir dosya işlenemedi (üstteki HATA satırı nedenini söyler: bozuk/sessiz dosya, araç hatası vb.). Kurulum'da 2 = deep-filter, 3 = ffmpeg, 4 = çalışma testi. |
| Eski çıktının üzerine yazmak istiyorum | PowerShell'den `Ses-Temizle.ps1 -UzerineYaz dosya.wav`. Varsayılan bilerek korumalıdır. |
| Windows "bilinmeyen yayımcı / SmartScreen" uyarısı | Dosyalar imzasız betiktir. "Daha fazla bilgi > Yine de çalıştır". İsterseniz önce `.ps1` dosyasını Not Defteri'nde okuyun, her satır açıklamalıdır. |
| Rapor "KIRPILMA riski" diyor | Windows giriş seviyesini 10–15 puan düşürüp **tekrar kaydedin**. Kırpılma temizlenemez. |
| Rapor "Konuşma–gürültü farkı düşük" diyor | Mikrofona yaklaşın, fanı kapatın, yumuşak yüzeylerin önüne geçin. Bu durumda yazılım tek başına yetmez. |
| Temiz ses "su altında / kesik kesik" | `Ses-Temizle-Dogal.bat` kullanın. Yol 2'deyseniz Noise Reduction'ı 6–9 dB'e indirin. |

---

## 6. Sorduğun sorulara kısa cevaplar

**1. "Perde, yatak, yorgan gibi yumuşak yüzeylerin yakınında kayıt al" neden?**
Sesiniz yalnız mikrofona gitmez, duvara, masaya ve tavana da çarpıp milisaniyeler sonra mikrofona tekrar gelir. Bu "oda yankısı" sesi boğuk ve uzak yapar. Sert yüzeyler sesi geri yansıtır, yumuşak ve kalın yüzeyler (yorgan, perde, kıyafet dolu dolap) emer.

Gürültü temizleyiciler sabit uğultuyu ayıklar. Yankı ise sizin kendi sesinizin kopyası olduğu için ayıklanamaz. Audacity forumundaki deneyimli kullanıcıların ifadesiyle "yankıdan yazılımla temiz şekilde kurtulmanın yolu yok". Boş bir dolap da işe yaramaz ("kutu içinde" gibi tınlar), içi kıyafet dolu olmalı.

**2. "Dinlemek için illa Audacity'yi mi açacağız?"**
Hayır. `ad_onceSonra.wav` ve `ad_temiz.wav` normal WAV dosyalarıdır. Çift tıklayınca Windows Medya Oynatıcısı veya hangi oynatıcıyı kullanıyorsanız onunla çalar. Audacity yalnız kesip düzenlemek içindir.

**3. "İki ses kaydını temizle düğmesi ne? Başlatıcı ne? Audacity'nin içinde mi çalışıyor?"**
Audacity'nin içinde **çalışmıyor**, Audacity'den bağımsız. Sistem şöyle:
- `Ses-Temizle.bat` yalnızca bir "başlatıcı". Üzerine bıraktığınız dosyaların yolunu alıp `Ses-Temizle.ps1` betiğine verir.
- `Ses-Temizle.ps1` bir PowerShell betiği. Sırayla şunları yapar:
  1. ffmpeg ile sesi 48 kHz mono'ya çevirir ve 80 Hz altını keser.
  2. Seviyeyi ayarlar ve 3:1 sıkıştırır.
  3. **deep-filter.exe** (DeepFilterNet3 yapay zekâsı) ile gürültüyü bastırır.
  4. Ses yüksekliğini −16 LUFS'e getirir.
  5. Önce/sonra dosyası ve ölçüm raporu yazar.
- Hepsi sizin bilgisayarınızda, internetsiz çalışır. Ses hiçbir yere yüklenmez.
- Birden fazla dosyayı birlikte bırakırsanız hepsini sırayla temizler; "iki kaydı temizle" bunu ifade ediyordu.

**4. "Audacity içinde yapılan bir şey yok mu? Hangisi daha iyi?"**
Var: Yol 2 (makro) ve Yol 3 (OpenVINO eklentisi). Aynı iki test kaydında ölçülen sonuç:

| Yöntem | Kalite (DNSMOS OVRL) | Anlaşılırlık (STOI) |
|---|---|---|
| Audacity makrosu (klasik Noise Reduction) | 1.67 / 1.14 | 0.733 / 0.591 (hamdan biraz düşük) |
| Ses-Temizle düğmesi | **2.96 / 2.46** | **0.773 / 0.677** (hamdan yüksek) |

**Sonuç:**
- Temizlik için **düğme daha iyi**.
- Audacity, kayıt ve kesme için kalıyor.
- Hepsini Audacity içinde yapmak isterseniz en yakın sonuç Yol 3'tür (OpenVINO DeepFilterNet3), ama o yol bu testte ölçülmedi.

**5. "Düğmeler hangileri?"**

| Dosya | Ne zaman |
|---|---|
| `Kurulum.bat` | Bir kez, ilk başta |
| `Kayit-Kontrol.bat` | Kayıttan önce, 10–15 sn deneme kaydını bırakın |
| `Ses-Temizle.bat` | Kayıttan sonra, normal kullanım |
| `Ses-Temizle-Dogal.bat` | Normal sonuç fazla "işlenmiş" geldiyse |
| Audacity > Tools > Apply Macro > **Ses-Temizle** | Audacity içinde kalmak isterseniz |
