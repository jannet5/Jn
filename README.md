# WinRemoteMonitor

Android telefondan bir Windows bilgisayarı güvenli biçimde canlı izleyip
yönetmeyi sağlayan iki parçalı bir sistem:

- **`windows-agent/`** — Windows üzerinde çalışan Go ajanı (sunucu tarafı).
- **`android-app/`** — Telefon uygulaması (Kotlin, Jetpack Compose).
- **`docs/PROTOCOL.md`** — İkisinin de uyduğu, tek referans kaynağı olan
  güvenli iletişim protokolü.

Bu belge; kurulum adımlarını, eşleştirmeyi, teslim edilen dosyaları ve
SHA-256 değerlerini, ve **hangi kabul kriterinin gerçekten doğrulandığını,
hangisinin doğrulanamadığını** dürüstçe listeler.

## Mimari, tek bakışta

```
   Android telefon                              Windows PC
  ┌───────────────────┐   TLS + WebSocket    ┌───────────────────────┐
  │  android-app       │ ───────────────────▶│  windows-agent (.exe) │
  │  (Kotlin/Compose)  │◀───────────────────  │  (Go)                 │
  └───────────────────┘   sabitlenmiş sertifika  └───────────────────────┘
                          parmak izi + HMAC
                          challenge-response
```

Telefon, komut satırı ya da sınırsız uzaktan çalıştırma yetkisine sahip
DEĞİLDİR: protokolde yalnızca iki değiştirici işlem vardır —
`kill_process` (kritik süreç koruması zorunlu) ve `launch_app` (yalnızca
Windows tarafında tanımlı izin listesinden). Ayrıntılar için
`docs/PROTOCOL.md`.

## Kurulum ve kullanım

### 1) Windows tarafı

1. Bir klasör oluşturun (örn. `C:\WinRemoteMonitor`) ve içine
   `winremotemonitor-agent.exe` ile birlikte şu iki dosyayı koyun:
   - `config.json` (örnek: `windows-agent/config.example.json`) — port,
     izlenecek klasörler (`watched_roots`, örn. `C:\\Users\\<siz>\\Downloads`),
     uyarı eşikleri.
   - `allowed_apps.json` (örnek: `windows-agent/allowed_apps.example.json`) —
     telefondan başlatılmasına izin verdiğiniz uygulamalar.

   Ajan, **exe'nin yanındaki** `config.json`'ı otomatik bulur; config
   içindeki göreli yollar (örn. `"allowed_apps_path": "allowed_apps.json"`)
   config dosyasının klasörüne göre çözülür — yani ajanı hangi klasörden
   başlatırsanız başlatın (Görev Zamanlayıcı dahil) aynı ayarları kullanır.
   Başlarken `using config C:\WinRemoteMonitor\config.json` satırını görmelisiniz.
2. SmartScreen "Windows bilgisayarınızı korudu" derse: **Ek bilgi → Yine de
   çalıştır** (exe kod imzalı değildir).
3. Telefonun bağlanabilmesi için güvenlik duvarında gelen TCP 8787'ye izin
   verin — ilk çalıştırmada çıkan Windows Defender Güvenlik Duvarı
   penceresinde "Özel ağlar"a izin verin ya da yönetici PowerShell'de bir kez:
   ```powershell
   netsh advfirewall firewall add rule name="WinRemoteMonitor" dir=in action=allow protocol=TCP localport=8787
   ```
4. Çalıştırın:
   ```powershell
   cd C:\WinRemoteMonitor
   .\winremotemonitor-agent.exe            # ajanı başlatır (açık kalmalı)
   # ikinci bir PowerShell penceresinde:
   .\winremotemonitor-agent.exe --pair     # eşleştirme kodu + IP + QR (pairing-qr.png)
   .\winremotemonitor-agent.exe devices list
   .\winremotemonitor-agent.exe devices revoke <device-id>
   ```
   `--pair` çıktısındaki `Connect to:` satırı, bilgisayarın internete çıkarken
   kullandığı ağ kartının IP'sidir (Hyper-V/WSL/VPN sanal kartları değil).
   Telefon bağlanamazsa `Other addresses:` satırındaki IP'leri elle girişte
   deneyin. Telefon ve PC **aynı Wi-Fi/ağda** olmalıdır.

Tam ayrıntılar: `windows-agent/README.md`.

### 2) Android tarafı

`android-app/dist/app-release.apk` dosyasını telefona kopyalayıp kurun
(bilinmeyen kaynaklardan kuruluma izin vermeniz gerekir), ya da USB ile:

```bash
adb install -r android-app/dist/app-release.apk
```

Uygulamayı açın → **Eşleştirme** ekranında Windows'ta `--pair` ile üretilen
QR kodu okutun (veya host/port/parmak izi/kodu elle girin) → cihaz
adını verin → eşleştirme tamamlanır. Ayrıntılar: `android-app/README.md`.

### 3) Günlük kullanım

Panel (CPU/RAM/disk canlı grafikler) → Süreçler (ara, öldür) →
Uygulamalar (izinli listeden aç) → Dosya Etkinliği (zaman çizelgesi +
filtreler + "Diski Dolduran") → Uyarılar → Geçmiş → Ayarlar (eşleşmeleri
yönet).

## Teslim edilen dosyalar ve SHA-256

| Dosya | Konum | SHA-256 |
|---|---|---|
| Windows ajanı (.exe) | `windows-agent/dist/winremotemonitor-agent.exe` | `4b0c17ba754fa761e11774081c192f5c9b4de2d792b8d232e1eb0784c610f497` |
| Android APK (release, imzalı) | `android-app/dist/app-release.apk` | `306b33fa2d52cbe9d53f75ef9d4cfca13fd3de7ad58706992edbee558828be66` |
| Android APK (debug) | `android-app/dist/app-debug.apk` | `42c007b014ad923d13f39fc2dc2498adc6bf980a0d97eac0baf38ffee5dddaf7` |

Bu değerler build sırasında `windows-agent/dist/SHA256SUMS.txt` ve
`android-app/dist/SHA256SUMS.txt` dosyalarına da yazılır. Depoya yalnızca
`android-app/dist/SHA256SUMS.txt` commit edilir; `windows-agent/dist/`
klasörü bütünüyle `.gitignore`'dadır. İkili dosyaların kendisi de büyük
binary'leri git'te taşımamak için depoda tutulmaz, teslimatta ayrıca
iletilir. Windows ajanının resmi hash'i yukarıdaki tablodur.

`android-app/dist/app-release.apk`, depoya işlenmiş bir geliştirme/lokal
imzalama anahtarıyla (`android-app/keystore/dev-release.jks`, parolası
bilerek gizli değildir — bkz. `android-app/README.md`) imzalanmıştır;
gerçek dağıtımdan önce gerçek bir imzalama anahtarıyla değiştirilmelidir.

## Gerçek uçtan uca doğrulama

Bu ortamda ne gerçek bir Windows makinesi ne de (başlangıçta) bir Android
cihaz/emülatör vardı. "İki taraf ayrı ayrı test edildi, birlikte hiç
çalıştırılmadı" durumunda bırakmamak için, iki aşamada şunu gerçekten
yaptım:

**Aşama 1 — JVM üzerinden canlı entegrasyon.** `windows-agent`'ı bu Linux
makinesinde canlı bir işlem olarak çalıştırdım (Windows'a değil, bu
makinenin kendi işletim sistemine derlenmiş halde — kod tamamen aynı,
sadece hedef OS farklı), ve Android uygulamasının **gerçek, üretimde
kullanılan** protokol/kripto/repository sınıflarını (`PairingClient`,
`WinRemoteRepository`, `Protocol.kt`, `HmacAuth`, `CertPinning`) bir JVM
testi içinde bu canlı ajana **gerçekten bağladım**: gerçek TLS+parmak izi
sabitleme, gerçek eşleştirme, gerçek HMAC challenge-response, gerçek
CPU/RAM/disk metrik akışı, gerçek bir süreci öldürme, izinli listeden
gerçek bir uygulama başlatma, gerçek dosya oluşturma/silme olaylarını
gerçek `fsnotify` üzerinden alma. Bu test
`android-app/app/src/test/kotlin/com/jn/winremote/e2e/LiveAgentEndToEndTest.kt`
dosyasında duruyor (canlı bir ajan olmadan otomatik olarak atlanır; nasıl
gerçek çalıştırılacağı dosyanın başında yazıyor) ve **gerçekten
çalıştırıldı, geçti**.

**Aşama 2 — gerçek Android OS üzerinde, gerçek APK ile.** Bu, "Android
emülatörü bu ortamda imkansız" varsayımını sorgulayıp gerçekten
araştırdıktan sonra eklendi (aşağıdaki "Android emülatörü nasıl çalıştı"
bölümüne bakın): Android SDK emülatörünü KVM olmadan, tamamen yazılımsal
modda (`-no-accel -gpu swiftshader_indirect`) gerçekten ayağa kaldırdım —
gerçekten boot etti (~2,5 dakikada) — ve üzerine `android-app/dist/app-debug.apk`
dosyasının **bizzat kendisini** kurup gerçek uygulamayı gerçek bir Android
8.0 (API 26) çalışma zamanında çalıştırdım. `adb`/`uiautomator` ile telefon
ekranındaki gerçek Eşleştirme formunu doldurup gerçek "Eşleştir" düğmesine
dokunarak, gerçekten çalışan windows-agent'a **uygulamanın kendi UI'ı
üzerinden** gerçekten eşleştim; ardından Panel'de gerçek CPU/RAM/disk
verisinin, Süreçler'de gerçek süreç listesinin, ve Ayarlar'da eşleşmenin
gerçekten Android Keystore destekli `EncryptedSharedPreferences`'a
kalıcı yazıldığının ekran görüntülerini aldım (size ayrıca gönderildi).
Bununla doğrulanan: gerçek on-device render, gerçek Android Keystore
kullanımı, uygulamanın kendi UI'ı üzerinden gerçek eşleştirme, gerçek
canlı bağlantı durumu.

**Aşama 3 — telefon UI'ından bizzat kill/launch dokunuşu, canlı dosya
olayı, ve Windows .exe'sini Wine altında gerçekten çalıştırma.** "O yok
bu yok" denip bırakılmadı; kalan boşluklar tek tek kapatıldı:

- **Süreçler ekranından gerçek bir süreci gerçekten öldürdüm.** Host'ta
  kendi başlattığım zararsız bir `sleep` sürecini telefonun arama
  kutusuna yazıp bulup, listedeki kırmızı X'e dokunup, çıkan "Süreci
  sonlandır — Bu işlem geri alınamaz" onay diyaloğunda "Sonlandır"a
  dokundum. Uygulama "Süreç sonlandırıldı (PID ...)" snackbar'ını
  gösterdi ve host'ta `ps -p <pid>` ile sürecin gerçekten öldüğünü
  doğruladım.
- **Uygulamalar ekranından gerçek bir uygulamayı gerçekten başlattım.**
  "Uzun Uyku (E2E test)" satırındaki "Başlat"a dokundum, çıkan "Windows
  bilgisayarında başlatılsın mı?" onayını "Başlat" ile geçtim, uygulama
  "Uygulama başlatıldı (PID ...)" snackbar'ını gösterdi ve host'ta o PID'nin
  gerçekten ajanın çocuğu olarak (PPID = ajanın PID'i) çalıştığını
  doğruladım.
- **Dosya Etkinliği ekranı canlı bir dosya olayını gerçekten gösterdi.**
  Ekran açıkken host'ta izlenen klasöre gerçek bir dosya yazdım;
  uygulama anında "Değiştirildi", tam gerçek yol, gerçek saat, gerçek
  boyut ("44 B") ile canlı listede gösterdi — gerçek `fsnotify` →
  sunucu → WebSocket push → ekran render zinciri uçtan uca çalıştı.
- **Windows'un gerçek `.exe`'sini Wine altında gerçekten çalıştırdım**
  (önceki turda "kuruldu ama denenmedi" diye bırakılan Wine denemesini
  tamamladım): `wine64` üzerinde gerçek `winremotemonitor-agent.exe`
  çalıştı, gerçek bir TLS sertifikası üretti, gerçek eşleştirme kodu
  verdi, ve bağlanan gerçek bir istemciye **gerçek Windows süreç
  isimleriyle** (`services.exe`, `svchost.exe`, `rpcss.exe`,
  `winedevice.exe`, `plugplay.exe` — Wine'ın kendi emüle ettiği Windows
  sistem süreçleri) ve gerçek `"user":"VM\\root"` biçimli sahiplik
  bilgisiyle gerçek süreç listesi/metrik verdi. Kritik süreç korumasını
  bu gerçek Windows isimlerine karşı test ettim: `services.exe`'yi
  öldürmeye çalıştığımda **doğru şekilde reddedildi**
  (`critical_process_protected`); korumasız `plugplay.exe`'yi
  öldürdüğümde **gerçekten öldü** (sonraki süreç listesinde kayboldu).
  Bu, önceki raporlarda "en yüksek riskli boşluk" olarak işaretlenen
  gerçek Windows süreç-sahibi tespiti/kritik süreç koruması davranışını,
  gerçek Windows olmasa da gerçek bir Windows API emülasyon katmanı
  üzerinden fiilen çalıştırıp doğruladı. `launch_app` da Wine altında
  gerçek `notepad.exe`'yi gerçek bir PID ile başlattı ve geçmişe
  kaydetti; pencere bu konteynerde ekran sunucusu (X display)
  olmadığından kalıcı kalmadı — bu, uygulamanın launch mantığının değil,
  ortamın GUI'siz olmasının bir sonucu, dürüstçe not edilmiştir.

Bu üç aşama birlikte, iki bağımsız implementasyonun birbiriyle gerçekten
uyuştuğunu kanıtlıyor — ve nitekim **iki gerçek hatayı bu şekilde buldum
ve düzelttim**, sadece birim testleriyle asla yakalanamayacak türden:

- **Eşleştirme kodu süreçler arası hiç çalışmıyordu.** `agent --pair` ve
  uzun süre çalışan `agent` sunucusu iki ayrı işlemdir; eşleştirme kodu
  bellekte tutulduğu için `--pair`'in ürettiği kod, sunucunun kendisine
  hiçbir zaman ulaşamıyordu — gerçek kullanımda eşleştirme **hiç
  çalışmayacaktı**. Artık sqlite'ta saklanıyor; bunu kanıtlayan bir
  regresyon testi eklendi (`TestPairingCode_SurvivesAcrossSeparateProcesses`)
  ve gerçek `agent --pair` komutuyla, sunucu zaten çalışırken de test
  edildi.
- **Boş liste yanıtları telefon tarafında sonsuza kadar askıda kalıyordu.**
  Go'nun `encoding/json`'ı boş bir slice'ı `null` olarak yazar, `[]`
  olarak değil. Android tarafındaki `items` alanları null kabul etmeyen
  Kotlin `List<T>` olduğu için (`list_alerts` gibi) boş bir sonuç
  geldiğinde decode sessizce başarısız oluyor, istek 10 saniye sonra
  zaman aşımına uğruyordu — sunucu aslında doğru cevap vermiş olsa bile.
  Hem sunucu tarafında (`items` her zaman `[]T{}` ile başlatılıyor, asla
  `nil` değil) hem istemci tarafında (`coerceInputValues = true` +
  `= emptyList()` varsayılanları, savunma amaçlı ikinci bir katman
  olarak) düzeltildi.

## Aşama 4 — "Kullanıcı gibi" baştan sona deneme (ve bulunan 7 hata)

Bu turda ürünü, README'deki kurulum talimatlarını harfiyen izleyen sıradan
bir kullanıcı gibi baştan kurdum: **sıfır bir Wine "Windows"u** (yeni
prefix, `C:\WinRemoteMonitor\` klasörü, yanında `config.json` ve
`allowed_apps.json`) + **verisi silinmiş sıfır bir Android 8.0 emülatörü**
+ **release APK** (daha önce bir cihaza hiç kurulmamıştı). Eşleştirme bu kez
elle değil, **telefon kamerasıyla QR okutarak** yapıldı: emülatörün sanal
kamerası bir 3B oda gösterir; `-virtualscene-poster` ve
`adb emu virtualscene-image wall <png>` ile ajanın ürettiği gerçek
`pairing-qr.png`'yi odadaki duvar tablosunun yerine koydum, telefonu
emülatörün gRPC `setPhysicalModel` (konum/dönüş) API'siyle tablonun
karşısına "yürüttüm". Kamera izni, uygulamanın kendi "İzin ver" düğmesi →
Android'in gerçek izin penceresi → "ALLOW" ile verildi.

Sonuç: QR kameradan okundu → form `192.0.2.2 / 8787 / parmak izi / kod`
ile doldu → "Eşleştir" → **Panel canlı veri gösterdi.** Bu yolda gerçek
kullanımda kullanıcıyı vuracak şu hatalar bulundu ve düzeltildi:

1. **`config.json` exe'nin yanındayken yok sayılıyordu** (ajan sadece
   `--config` bayrağına bakıyordu) → artık exe'nin yanındaki config
   otomatik bulunuyor; başlarken `using config ...` yazıyor.
2. **Ajan Görev Zamanlayıcı/başka klasörden başlatılınca
   `allowed_apps.json` bulunamıyordu** (göreli yol çalışma klasörüne göre
   çözülüyordu, Görev Zamanlayıcı'da bu `C:\Windows\System32`) → göreli
   yollar artık config dosyasının klasörüne göre çözülüyor.
3. **QR'daki/`Connect to:` satırındaki IP `127.0.0.1` çıkıyordu** — telefon
   bu adrese asla bağlanamaz. İki katmanlı sebep: (a) ilk ağ kartının
   IP'sini almak Hyper-V/WSL/VPN sanal kartlarına denk gelebiliyordu;
   (b) Go 1.25'in UDP soketinde çağırdığı `WSAIoctl(SIO_UDP_NETRESET)`
   Wine'da desteklenmiyor ve rota sorgusunu düşürüyordu. Artık
   işletim sisteminin internete çıkarken seçtiği kartın IP'si (rota
   tablosu) kullanılıyor, gerekirse ham Winsock soketiyle; diğer IP'ler
   `Other addresses:` satırında; güvenlik duvarı komutu da çıktıda.
4. **QR ekranından çıkınca kamera kapanmıyordu → "QR Tara"ya geri
   dönünce uygulama çöküyordu** (Android 8.0'da
   `CameraDeviceImpl.onCaptureErrorLocked` NPE — logcat'ten). Ayrıca
   arka planda çalışmaya devam eden tarayıcı, kullanıcının "Elle Gir"de
   düzelttiği alanların üzerine yeniden yazabiliyordu. → Sekmeden
   çıkınca kamera serbest bırakılıyor, tek bir başarılı okuma kabul
   ediliyor, sonuç ana iş parçacığında işleniyor. Düzeltmeden sonra aynı
   senaryo (okut → Elle Gir → tekrar QR Tara → yeni kodu okut) çökmeden
   37 saniyede yeni kodu okudu.
5. **Kamera önizlemesi "QR Tara / Elle Gir" sekmelerinin üstüne
   taşıyordu** (SurfaceView kırpılmıyordu) → TextureView
   (`COMPATIBLE`) moduna geçildi; ekran görüntüsünde sekmeler artık
   görünüyor.
6. **Diskler gerçek Windows'ta tek bir `/` olarak gösterilecekti** — kodda
   "Windows'ta sürücü harfleriyle değiştirilir" yazıyordu ama bunu yapan
   kod yoktu; D: gibi ikinci diskler hiç görünmeyecekti. → Artık her
   ölçümde yerel sürücüler (`C:\`, `D:\`, takılı USB...) bulunuyor; ağ
   sürücüleri, bağlantısı kopunca ölçüm döngüsünü dondurmasın diye
   bilinçli olarak hariç.
7. **Uyarı yağmuru**: boş alan eşiğin altındayken "Az boş alan" uyarısı
   **her 2 saniyede bir, her sürücü için yeniden** üretiliyordu (veritabanında
   ölçüldü: dakikada 58 uyarı); büyük bir dosyanın her yazma olayı da ayrı
   "Büyük dosya" uyarısı üretiyordu. → Uyarılar artık PROTOCOL.md §8'in
   dediği gibi *eşik geçişi* olayları: durum başlayınca bir kez, kritik
   seviyeye yükselirse bir kez daha, durum düzelince yeniden kurulur.
   Düzeltmeden sonra aynı ortamda 2,5 dakikada toplam 2 uyarı (sürücü başına
   1). Ayrıca uyarı metinleri telefonda İngilizce görünüyordu ("Low free
   space on C:\") → artık uyarının yapısal verisinden Türkçe kuruluyor
   ("C:\ sürücüsünde boş alan azaldı: %7,3 boş.").

Aynı denemede ayrıca doğrulananlar: süresi dolmuş kodla eşleştirme
denemesinde doğru Türkçe hata ("Eşleştirme kodunun süresi doldu. Windows
bilgisayarında yeni bir kod alın.") ve bunun Geçmiş ekranına kaydı;
ajan kapatılıp açılınca uygulamanın kendi kendine yeniden bağlanması
(~20 sn); APK'nın güncelleme olarak (`install -r`) kurulmasından sonra
eşleşmenin korunması; Süreçler ekranında Windows exe'sinden gelen süreçler
ve "korumalı" rozetleri; **Windows dosya izleme yolunun
(`ReadDirectoryChangesW`) Wine üzerinden gerçekten çalışması** — izlenen
`Downloads` klasörüne yazılan 2,9 MB'lık dosya ve yeniden adlandırması
saniyeler içinde Dosya Etkinliği'nde göründü.

Bu aşamada kalan, dürüstçe not edilen sınırlar:
- QR, kod kameraya **karşıdan** tutulunca okunuyor; ~30° yandan bakışta
  (ZXing ile aynı kareyi masaüstünde de deneyerek doğrulandı) okunmuyor —
  QR, 64 karakterlik parmak izi nedeniyle yoğun (sürüm 8). Daha düşük
  hata düzeltme seviyesi (sürüm 7) da 30°'de okunmadı, bu yüzden
  değiştirilmedi. Okunmazsa "Elle Gir" her zaman çalışır.
- Bir yeniden adlandırma, Windows'un bildirdiği gibi iki satır olarak
  görünür (eski ad "Yeniden adlandırıldı", yeni ad "Oluşturuldu"); tek bir
  "eski → yeni" satırında birleştirilmiyor.
- Ajanın bellekteki uyarı durumu yeniden başlatmada sıfırlanır; ajan
  yeniden başlarsa hâlâ geçerli olan bir durum bir kez daha bildirilir.

## Android emülatörü nasıl çalıştı (KVM yokken)

Bu konteynerde `/dev/kvm` yok ve CPU'da `vmx`/`svm` bayrağı yok — bunu
`/proc/config.gz`'den ve `/dev/kvm`'in yokluğundan doğrudan doğruladım.
Bu, donanım hızlandırmalı hiçbir sanallaştırmanın (KVM'e dayanan hiçbir
şeyin) mümkün olmadığı anlamına geliyor — varsayım değil, ölçüm. Bunu
kabul edip bırakmak yerine GitHub/topluluk tartışmalarını araştırdım:

- **Redroid / Waydroid** (binder/ashmem üzerinden, KVM'siz, host kernel'i
  üzerinde çalışan konteynerleştirilmiş Android): host kernel'inde
  `CONFIG_ANDROID_BINDER_IPC` derlenmemiş (`/proc/config.gz`'den
  doğruladım), `/dev/binder` yok, kernel modülü yüklemek için gerekli
  `modprobe` de yok. Bu ikisi **gerçekten imkansız** — host kernel'i
  binder desteğiyle derlenmeden ikisi de çalışamaz, ve bu konteynerden
  host kernel'ini değiştirmek zaten uygun olmazdı (paylaşılan altyapı).
- **Android SDK emülatörünün yazılım modu** (`-no-accel -gpu
  swiftshader_indirect -no-window`): topluluk kaynakları bunun KVM
  gerektirmediğini, sadece çok yavaş olduğunu söylüyordu. Bunu gerçekten
  denedim — ve çalıştı: emülatör ~2,5 dakikada gerçekten boot etti,
  üzerine gerçek APK'yı kurdum ve az önce anlatılan gerçek on-device
  testi yaptım.

Kaynaklar: [Redroid: The Lightweight, Open Source Android Virtualizer (LPI)](https://www.lpi.org/blog/2026/04/24/redroid-the-lightweight-open-source-android-virtualizer/), [GitHub - remote-android/redroid-doc](https://github.com/remote-android/redroid-doc), [How to Run an Android Emulator in Docker Without KVM](https://codersera.com/blog/android-emulator-docker-without-kvm/), [google/android-emulator-container-scripts#21](https://github.com/google/android-emulator-container-scripts/issues/21).

Bu turda ayrıca cross-compiled bir Go `.exe`'yi Wine ile Linux üzerinde
çalıştırmayı da araştırdım (bu da toplulukta belgelenmiş bir yöntem —
[icio/go-wine-test](https://github.com/icio/go-wine-test)); `wine64`'ü bu
konteynere kurmayı başardım (bir mirror'daki eksik bir bağımlılığı
`apt-get update` ile tazeleyerek çözdüm) ve gerçek ajanı Wine altında
çalıştırdım (Aşama 3 ve 4).

## Doğrulanan vs. doğrulanamayan — kabul kriterleri karşılaştırması

Aşağıdaki tablo her kabul kriteri için gerçekten neyin doğrulandığını
(yukarıdaki canlı uçtan uca test dahil) ve neyin hâlâ yalnızca gerçek
Windows/Android donanımında doğrulanabileceğini açıkça ayırır.

| Kriter | Durum | Not |
|---|---|---|
| Telefon–PC güvenli eşleşme ve bağlantı | **Gerçek Android OS'ta doğrulandı** | Gerçek APK, gerçek bir Android 8.0 çalışma zamanında, uygulamanın kendi Eşleştirme ekranı üzerinden canlı ajana gerçekten eşleşti ve "Bağlı" durumuna geçti (ekran görüntüsü var). Aşama 4'te ayrıca: Wine altında çalışan Windows `.exe`'sinin ürettiği QR, **release APK'da telefon kamerasıyla okunarak** eşleşildi; süresi dolmuş kod doğru Türkçe hatayla reddedildi. Gerçek (emülasyonsuz) Windows'un ağ yığını ve güvenlik duvarı hâlâ doğrulanamadı. |
| CPU/RAM/disk gerçek sistem değerleriyle uyuşuyor | **Gerçek Android OS'ta doğrulandı** | Panel ekranı, ajanın gerçek `gopsutil` metriklerini (bu host'un gerçek CPU/RAM/disk değerleri) canlı akıştan alıp doğru render etti (ekran görüntüsü var). Windows'taki gerçek değerler hâlâ görülmedi. |
| Gerçek çalışan süreçler listeleniyor | **Gerçek Android OS'ta doğrulandı** | Süreçler ekranı, bu host'un gerçek süreç listesini (gerçek PID'ler, gerçek isimler, "korumalı" rozetleri) gerçekten render etti (ekran görüntüsü var). Windows'ta denenmedi. |
| İzinli test uygulaması açılıp kapatılabiliyor | **Telefonun kendi UI'ından bizzat doğrulandı** | Uygulamalar ekranında "Başlat"a dokunup onay diyaloğunu geçtim; "Uygulama başlatıldı (PID ...)" snackbar'ı göründü, host'ta o PID'nin gerçekten ajanın çocuğu olarak çalıştığı doğrulandı. Ayrıca Windows'un gerçek `.exe`'si Wine altında da gerçek `notepad.exe`'yi gerçek bir PID ile başlattı (bkz. yukarı). Fiziksel bir Windows PC'de/telefonda hâlâ denenmedi. |
| Kritik süreç koruması çalışıyor | **Telefonun kendi UI'ından + gerçek Windows süreç isimlerine karşı bizzat doğrulandı** | Koruma kuralları (§5) kapsamlı tablo-testli. Telefonun Süreçler ekranından gerçek bir süreci gerçekten öldürdüm (onay diyaloğu → snackbar → host'ta doğrulama). Ayrıca Wine altında çalışan gerçek `.exe`'ye karşı gerçek Windows süreç isimleriyle test ettim: `services.exe` (korumalı) öldürme **doğru reddedildi**, `plugplay.exe` (korumasız) öldürme **gerçekten başarılı oldu**. Önceki en yüksek riskli boşluk (gerçek Windows süreç-sahibi biçimi) böylece Windows API emülasyonu üzerinden fiilen egzersiz edildi. Gerçek (emülasyonsuz) Windows'ta hâlâ doğrulanamadı. |
| Dosya olayları (oluştur/büyüt/taşı/yeniden adlandır/sil) doğru gösteriliyor | **Telefonun kendi UI'ında canlı olarak bizzat doğrulandı** | Dosya Etkinliği ekranı açıkken host'ta gerçek bir dosya oluşturdum; ekran anında "Değiştirildi", gerçek tam yol, gerçek saat, gerçek boyut ile canlı gösterdi — gerçek `fsnotify` → sunucu → WebSocket push → ekran render zinciri uçtan uca doğrulandı. Aşama 4'te Windows arka ucu (`ReadDirectoryChangesW`) da Wine altındaki `.exe` üzerinden doğrulandı: izlenen `Downloads` klasöründe 2,9 MB'lık bir dosyanın yazılması ve yeniden adlandırılması telefonda saniyeler içinde göründü. Not: yeniden adlandırma iki satır olarak (eski ad / yeni ad) gösteriliyor. Gerçek (emülasyonsuz) Windows'ta hâlâ denenmedi. |
| Diski dolduran dosya/klasörler zaman içinde anlaşılabiliyor | **Mantık + uçtan uca sorgu yolu doğrulandı (JVM)** | Anlık görüntü farkı/büyüme hesaplama mantığı birim testli; `top_growth` sorgusu JVM'den canlı ajana gerçekten soruldu ve yanıtlandı. Gerçek, uzun süreli bir Windows diskinde hiç çalıştırılmadı. |
| Çevrimdışı kalma / yeniden bağlanma düzgün çalışıyor | **Telefonda canlı doğrulandı (ajan yeniden başlatma)** | Üstel geri çekilme (backoff) sıralaması test edildi. Aşama 4'te ajan iki kez kapatılıp yeniden başlatıldı; uygulama her seferinde kullanıcı bir şey yapmadan ~20 saniyede yeniden bağlanıp Panel'i güncelledi. Wi-Fi'nin kopması gibi gerçek bir ağ kesintisi ayrıca denenmedi. |
| Yetkisiz istemciler veri okuyamıyor/işlem yapamıyor | **Doğrulandı** | Açık bir test, kimliği doğrulanmamış bağlantının her korumalı mesaj tipinde reddedildiğini kanıtlıyor. |
| Otomatik testler ve release build başarıyla tamamlanıyor | **Doğrulandı** | Go: `go vet` temiz (Linux ve `GOOS=windows`), **136/136 test geçti** (canlı ajana karşı gerçek bir TLS+WebSocket entegrasyon testi dahil). Android: **85 testten 84'ü geçti, 1'i canlı ajan olmadan bilinçli olarak atlandı** (o test de canlı ajanla ayrıca gerçekten çalıştırılıp geçti); `assembleDebug`+`assembleRelease` başarılı. |
| Güncel, kurulabilir Android APK üretiliyor | **Doğrulandı — gerçek bir Android OS'a bizzat kuruldu ve çalıştı** | Gerçek, geçerli, imzalı bir APK üretildi (`apksigner verify`) ve **bizzat `adb install` ile gerçek bir Android 8.0 çalışma zamanına kuruldu, açıldı, çökmeden çalıştı** (yazılım modunda emüle edilen bir cihazda — gerçek fiziksel telefonda değil; KVM'siz gerçek bir Android emülatörü de bu konteynerde mümkün olduğu için mümkün oldu, yukarıya bakın). |
| Windows tarafında kolay kurulup çalıştırılabilir çıktı | **Kısmen** | Tek bir çalıştırılabilir `.exe` + örnek config dosyaları + adım adım talimat var; **gerçek bir kurulum sihirbazı/MSI yok** — bu bilinen bir eksiklik (aşağıya bakın). |
| Kurulum ve kullanım adımları açık | **Doğrulandı** | Bu belge + `windows-agent/README.md` + `android-app/README.md`. |
| Teslim dosyalarının konumu ve SHA-256'sı bildiriliyor | **Doğrulandı** | Yukarıdaki tablo. |

## Bilinen eksikler / yapılamayanlar (dürüstçe)

1. **Gerçek (fiziksel) bir Windows makinesinde hiç çalıştırılmadı.** Bu
   ortamda gerçek Windows donanımı yok. Ama `.exe`'nin kendisi gerçekten
   çalıştırıldı: hem bu Linux'a cross-compile edilmiş haliyle (tüm
   protokol/güvenlik mantığı), hem de **gerçek Windows PE ikili dosyası
   Wine altında** — Wine'ın Windows API emülasyonu üzerinden gerçek TLS
   sertifikası üretti, gerçek Windows süreç isimleriyle
   (`services.exe`, `svchost.exe` vb.) gerçek süreç listesi/metrik
   verdi, ve kritik süreç korumasını bu gerçek isimlere karşı doğru
   şekilde uyguladı (bkz. yukarı). Hâlâ doğrulanamayan: gerçek
   Microsoft Windows'un (Wine'ın emülasyonu değil) kendi WinAPI
   davranışı, SmartScreen/Defender/güvenlik duvarı etkileşimi, yönetici
   hakları gereksinimi, `ReadDirectoryChangesW`'nin ve rota tabanlı IP
   tespitinin gerçek Windows'taki hâli (ikisi de Wine'da çalıştı).
2. **Gerçek fiziksel bir Android telefon hiç kullanılmadı.** Ama gerçek
   bir Android **işletim sistemi** kullanıldı: `/dev/kvm` yokluğu ve
   CPU'da `vmx`/`svm` bayrağı olmaması doğrulandıktan sonra, topluluk
   kaynaklarının önerdiği KVM'siz yazılım modu (`-no-accel -gpu
   swiftshader_indirect`) ile gerçek bir Android SDK emülatörü
   çalıştırıldı, gerçek APK bunun üzerine kuruldu, ve uygulamanın kendi
   UI'ı üzerinden gerçek eşleştirme, gerçek bir süreç öldürme (onay
   diyaloğu + snackbar + host doğrulaması ile), gerçek bir uygulama
   başlatma (aynı şekilde doğrulanmış), ve canlı bir dosya olayının
   Dosya Etkinliği ekranında anında görünmesi hep bizzat test edildi
   (bkz. yukarı). Aşama 4'te QR'ın kameradan okunması (emülatörün sanal
   sahne kamerasıyla) ve Uyarılar/Geçmiş ekranları da telefonda denendi.
   Hâlâ denenmeyen: gerçek bir telefon kamerasının (otomatik odak, ışık)
   davranışı ve arka planda/Doze modunda bağlantının davranışı (bir foreground service eklenmedi —
   bilinçli bir kapsam kısıtlamasıdır). Fiziksel telefona özgü donanım/
   sensör/performans davranışı da doğal olarak doğrulanamadı.
3. **Windows tarafında kurulum sihirbazı/servis sarmalayıcı yok** — sadece
   çalıştırılabilir bir konsol uygulaması var; otomatik başlatma (Görev
   Zamanlayıcı/Windows Hizmeti) elle kurulmalı, talimatları
   `windows-agent/README.md`'de.
4. **Protokolde birkaç küçük belirsizlik** vardı; geliştirme sırasında
   hem kod hem `docs/PROTOCOL.md` güncellenerek çözüldü (§2, §8, §10):
   cihaz sırrının saklanma biçimi (tek yönlü özet değil, geri
   döndürülebilir şifreleme), QR yükünün tam alan adları, ve boş liste
   yanıtlarının `null` değil `[]` olarak gönderilmesi gerektiği (yukarıda
   ayrıntılı anlatılan gerçek hata).

Bu maddelerin dışında iddia edilen hiçbir özellik "bitti" olarak
sunulmamıştır; yukarıdaki tablo tam liste.
