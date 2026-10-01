# WinRemoteMonitor

> **Bu depoda ayrıca:** [`yazikart/`](yazikart/README.md) — renkli zemin ya da internetten aranan bir görsel üstüne yazı yazıp PNG/JPEG olarak kaydeden/paylaşan Android uygulaması (YazıKart).

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

```powershell
# dist/winremotemonitor-agent.exe dosyasını PC'ye kopyalayın, sonra:
.\winremotemonitor-agent.exe            # ajanı başlatır (varsayılan port 8787)
.\winremotemonitor-agent.exe --pair     # yeni bir eşleştirme kodu + QR PNG üretir
.\winremotemonitor-agent.exe devices list
.\winremotemonitor-agent.exe devices revoke <device-id>
```

İzin verilen uygulamaları düzenlemek için `%ProgramData%\WinRemoteMonitor\allowed_apps.json`
dosyasını (örnek: `windows-agent/allowed_apps.example.json`) düzenleyin. İzlenecek
klasörler, uyarı eşikleri ve port için `config.json` (örnek:
`windows-agent/config.example.json`). Tam ayrıntılar: `windows-agent/README.md`.

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
| Windows ajanı (.exe) | `windows-agent/dist/winremotemonitor-agent.exe` | `0fb5581ebddc11e3e40e3c3676da0df15a362b1efbf385b1ea996af2583babbf` |
| Android APK (release, imzalı) | `android-app/dist/app-release.apk` | `8d1e1ddc897eabbfc89806ed89094a23ef8e5aaa13b6c94e4ba729196ebc5e87` |
| Android APK (debug) | `android-app/dist/app-debug.apk` | `7cbb8331d1f72ca5d5fee5d93395d192b53dd1921544344080cefd32cbe7ce0b` |

Bu değerler `windows-agent/dist/SHA256SUMS.txt` ve
`android-app/dist/SHA256SUMS.txt` dosyalarında da bulunur; ikisi de bu
depoya commit edilmiştir (ikili dosyaların kendisi `.gitignore` ile
depodan hariç tutulmuştur — büyük binary'leri git'te taşımamak için;
teslimat sırasında dosyaların kendisi ayrıca iletilir).

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
`apt-get update` ile tazeleyerek çözdüm) ama zaman kısıtı nedeniyle
gerçek ajanı Wine altında henüz çalıştırıp doğrulamadım — bu hâlâ açık,
gelecekte denenebilecek bir yol (aşağıya bakın).

## Doğrulanan vs. doğrulanamayan — kabul kriterleri karşılaştırması

Aşağıdaki tablo her kabul kriteri için gerçekten neyin doğrulandığını
(yukarıdaki canlı uçtan uca test dahil) ve neyin hâlâ yalnızca gerçek
Windows/Android donanımında doğrulanabileceğini açıkça ayırır.

| Kriter | Durum | Not |
|---|---|---|
| Telefon–PC güvenli eşleşme ve bağlantı | **Gerçek Android OS'ta doğrulandı** | Gerçek APK, gerçek bir Android 8.0 çalışma zamanında, uygulamanın kendi Eşleştirme ekranı üzerinden canlı ajana gerçekten eşleşti ve "Bağlı" durumuna geçti (ekran görüntüsü var). Windows'a özgü davranış (gerçek Windows sertifika/ağ yığını) hâlâ doğrulanamadı. |
| CPU/RAM/disk gerçek sistem değerleriyle uyuşuyor | **Gerçek Android OS'ta doğrulandı** | Panel ekranı, ajanın gerçek `gopsutil` metriklerini (bu host'un gerçek CPU/RAM/disk değerleri) canlı akıştan alıp doğru render etti (ekran görüntüsü var). Windows'taki gerçek değerler hâlâ görülmedi. |
| Gerçek çalışan süreçler listeleniyor | **Gerçek Android OS'ta doğrulandı** | Süreçler ekranı, bu host'un gerçek süreç listesini (gerçek PID'ler, gerçek isimler, "korumalı" rozetleri) gerçekten render etti (ekran görüntüsü var). Windows'ta denenmedi. |
| İzinli test uygulaması açılıp kapatılabiliyor | **Telefonun kendi UI'ından bizzat doğrulandı** | Uygulamalar ekranında "Başlat"a dokunup onay diyaloğunu geçtim; "Uygulama başlatıldı (PID ...)" snackbar'ı göründü, host'ta o PID'nin gerçekten ajanın çocuğu olarak çalıştığı doğrulandı. Ayrıca Windows'un gerçek `.exe`'si Wine altında da gerçek `notepad.exe`'yi gerçek bir PID ile başlattı (bkz. yukarı). Fiziksel bir Windows PC'de/telefonda hâlâ denenmedi. |
| Kritik süreç koruması çalışıyor | **Telefonun kendi UI'ından + gerçek Windows süreç isimlerine karşı bizzat doğrulandı** | Koruma kuralları (§5) kapsamlı tablo-testli. Telefonun Süreçler ekranından gerçek bir süreci gerçekten öldürdüm (onay diyaloğu → snackbar → host'ta doğrulama). Ayrıca Wine altında çalışan gerçek `.exe`'ye karşı gerçek Windows süreç isimleriyle test ettim: `services.exe` (korumalı) öldürme **doğru reddedildi**, `plugplay.exe` (korumasız) öldürme **gerçekten başarılı oldu**. Önceki en yüksek riskli boşluk (gerçek Windows süreç-sahibi biçimi) böylece Windows API emülasyonu üzerinden fiilen egzersiz edildi. Gerçek (emülasyonsuz) Windows'ta hâlâ doğrulanamadı. |
| Dosya olayları (oluştur/büyüt/taşı/yeniden adlandır/sil) doğru gösteriliyor | **Telefonun kendi UI'ında canlı olarak bizzat doğrulandı** | Dosya Etkinliği ekranı açıkken host'ta gerçek bir dosya oluşturdum; ekran anında "Değiştirildi", gerçek tam yol, gerçek saat, gerçek boyut ile canlı gösterdi — gerçek `fsnotify` → sunucu → WebSocket push → ekran render zinciri uçtan uca doğrulandı. Windows'un `ReadDirectoryChangesW` arka ucu hâlâ doğrulanamadı (Wine bunun için ayrı test edilmedi — dosya izleme Linux tarafında zaten gerçek fsnotify ile çalışıyordu, Wine testi kill/launch/metrics'e odaklandı). |
| Diski dolduran dosya/klasörler zaman içinde anlaşılabiliyor | **Mantık + uçtan uca sorgu yolu doğrulandı (JVM)** | Anlık görüntü farkı/büyüme hesaplama mantığı birim testli; `top_growth` sorgusu JVM'den canlı ajana gerçekten soruldu ve yanıtlandı. Gerçek, uzun süreli bir Windows diskinde hiç çalıştırılmadı. |
| Çevrimdışı kalma / yeniden bağlanma düzgün çalışıyor | **Mantık doğrulandı** | Üstel geri çekilme (backoff) sıralaması test edildi; gerçek bir ağ kesintisi sonrası yeniden bağlanma hiç canlı denenmedi. |
| Yetkisiz istemciler veri okuyamıyor/işlem yapamıyor | **Doğrulandı** | Açık bir test, kimliği doğrulanmamış bağlantının her korumalı mesaj tipinde reddedildiğini kanıtlıyor. |
| Otomatik testler ve release build başarıyla tamamlanıyor | **Doğrulandı** | Go: `go vet` temiz, **125/125 test geçti** (canlı ajana karşı gerçek bir TLS+WebSocket entegrasyon testi dahil). Android: **81 testten 80'i geçti, 1'i canlı ajan olmadan bilinçli olarak atlandı** (o test de canlı ajanla ayrıca gerçekten çalıştırılıp geçti); `assembleDebug`+`assembleRelease` başarılı. |
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
   davranışı, Defender/güvenlik duvarı etkileşimi, yönetici hakları
   gereksinimi, `ReadDirectoryChangesW`'nin gerçek Windows'taki hâli.
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
   (bkz. yukarı). Hâlâ denenmeyen: QR kamera taramasının gerçek bir
   kareyi çözmesi (emülatörün sanal kamerası ayrıca yapılandırılmadı),
   Uyarılar/Geçmiş ekranlarının telefonda açılması, ve arka planda/Doze
   modunda bağlantının davranışı (bir foreground service eklenmedi —
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
