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

Bu ortamda ne gerçek bir Windows makinesi ne de bir Android cihaz/emülatör
var (aşağıda neden olmadığı somut olarak açıklanıyor). Ama "iki taraf ayrı
ayrı test edildi, birlikte hiç çalıştırılmadı" durumunda bırakmamak için,
şunu gerçekten yaptım:

1. `windows-agent`'ı **bu Linux makinesinde canlı bir işlem olarak
   çalıştırdım** (Windows'a değil, bu makinenin kendi işletim sistemine
   derlenmiş halde — kod tamamen aynı, sadece hedef OS farklı).
2. Android uygulamasının **gerçek, üretimde kullanılan** protokol/kripto/
   repository sınıflarını (`PairingClient`, `WinRemoteRepository`,
   `Protocol.kt`, `HmacAuth`, `CertPinning`) bir JVM testi içinde bu canlı
   ajana **gerçekten bağladım**: gerçek TLS+parmak izi sabitleme, gerçek
   eşleştirme, gerçek HMAC challenge-response, gerçek CPU/RAM/disk metrik
   akışı, gerçek bir süreci öldürme, izinli listeden gerçek bir uygulama
   başlatma, gerçek dosya oluşturma/silme olaylarını gerçek `fsnotify`
   üzerinden alma. Bu test `android-app/app/src/test/kotlin/com/jn/winremote/e2e/LiveAgentEndToEndTest.kt`
   dosyasında duruyor (canlı bir ajan olmadan otomatik olarak atlanır;
   nasıl gerçek çalıştırılacağı dosyanın başında yazıyor) ve **gerçekten
   çalıştırıldı, geçti**.

Bu, iki bağımsız implementasyonun birbiriyle gerçekten uyuştuğunu
kanıtlıyor — ve nitekim **iki gerçek hatayı bu şekilde buldum ve
düzelttim**, sadece birim testleriyle asla yakalanamayacak türden:

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

## Doğrulanan vs. doğrulanamayan — kabul kriterleri karşılaştırması

Aşağıdaki tablo her kabul kriteri için gerçekten neyin doğrulandığını
(yukarıdaki canlı uçtan uca test dahil) ve neyin hâlâ yalnızca gerçek
Windows/Android donanımında doğrulanabileceğini açıkça ayırır.

| Kriter | Durum | Not |
|---|---|---|
| Telefon–PC güvenli eşleşme ve bağlantı | **Uçtan uca doğrulandı (Linux üzerinde)** | Gerçek Android istemci kodu, gerçek ajana canlı bağlanıp eşleşti, TLS parmak izi sabitleme + HMAC challenge-response ile kimlik doğruladı. Windows'a özgü davranış (gerçek Windows sertifika/ağ yığını) hâlâ doğrulanamadı. |
| CPU/RAM/disk gerçek sistem değerleriyle uyuşuyor | **Uçtan uca doğrulandı (Linux üzerinde)** | Telefon tarafı, ajanın gerçek `gopsutil` metriklerini canlı akıştan aldı ve mantıklı değerler olduğunu doğruladı. Windows'taki gerçek değerler hâlâ görülmedi. |
| Gerçek çalışan süreçler listeleniyor | **Uçtan uca doğrulandı (Linux üzerinde)** | Telefon tarafı gerçek süreç listesini ajandan gerçekten çekti. Windows'ta denenmedi. |
| İzinli test uygulaması açılıp kapatılabiliyor | **Uçtan uca doğrulandı (Linux üzerinde)** | Telefon tarafı, izin listesinden gerçek bir uygulamayı gerçekten başlattı (gerçek PID döndü, gerçekten çalıştığı doğrulandı) ve gerçekten kapattı. Windows'ta/gerçek bir Android cihazda denenmedi. |
| Kritik süreç koruması çalışıyor | **Kural mantığı doğrulandı + gerçek kill uçtan uca doğrulandı** | Koruma kuralları (§5) kapsamlı tablo-testli; telefon tarafı gerçek, korumasız bir süreci ajan üzerinden gerçekten öldürdü ve sürecin gerçekten öldüğünü doğruladı. Gerçek Windows kritik süreç isimleri/sahiplik biçimleri hâlâ doğrulanamadı. |
| Dosya olayları (oluştur/büyüt/taşı/yeniden adlandır/sil) doğru gösteriliyor | **Uçtan uca doğrulandı (Linux üzerinde)** | Telefon tarafı, izlenen klasörde gerçekten oluşturulan/silinen bir dosyanın olaylarını canlı akıştan gerçekten aldı. Windows'un `ReadDirectoryChangesW` arka ucu hâlâ doğrulanamadı. |
| Diski dolduran dosya/klasörler zaman içinde anlaşılabiliyor | **Mantık + uçtan uca sorgu yolu doğrulandı** | Anlık görüntü farkı/büyüme hesaplama mantığı birim testli; `top_growth` sorgusu telefon tarafından canlı ajana gerçekten soruldu ve yanıtlandı. Gerçek, uzun süreli bir Windows diskinde hiç çalıştırılmadı. |
| Çevrimdışı kalma / yeniden bağlanma düzgün çalışıyor | **Mantık doğrulandı** | Üstel geri çekilme (backoff) sıralaması test edildi; gerçek bir ağ kesintisi sonrası yeniden bağlanma hiç canlı denenmedi. |
| Yetkisiz istemciler veri okuyamıyor/işlem yapamıyor | **Doğrulandı** | Açık bir test, kimliği doğrulanmamış bağlantının her korumalı mesaj tipinde reddedildiğini kanıtlıyor. |
| Otomatik testler ve release build başarıyla tamamlanıyor | **Doğrulandı** | Go: `go vet` temiz, **125/125 test geçti** (canlı ajana karşı gerçek bir TLS+WebSocket entegrasyon testi dahil). Android: **81 testten 80'i geçti, 1'i canlı ajan olmadan bilinçli olarak atlandı** (o test de canlı ajanla ayrıca gerçekten çalıştırılıp geçti); `assembleDebug`+`assembleRelease` başarılı. |
| Güncel, kurulabilir Android APK üretiliyor | **Doğrulandı (yapısal olarak) + gerçek protokol kodu uçtan uca çalıştı** | Gerçek, geçerli, imzalı bir APK üretildi ve doğrulandı (`apksigner verify`); APK'nın içindeki gerçek kod bir cihaz yerine JVM üzerinden canlı ajana bağlanarak çalıştırıldı. APK'nın bizzat bir telefona kurulup açılması hâlâ denenmedi (emülatör/cihaz yok). |
| Windows tarafında kolay kurulup çalıştırılabilir çıktı | **Kısmen** | Tek bir çalıştırılabilir `.exe` + örnek config dosyaları + adım adım talimat var; **gerçek bir kurulum sihirbazı/MSI yok** — bu bilinen bir eksiklik (aşağıya bakın). |
| Kurulum ve kullanım adımları açık | **Doğrulandı** | Bu belge + `windows-agent/README.md` + `android-app/README.md`. |
| Teslim dosyalarının konumu ve SHA-256'sı bildiriliyor | **Doğrulandı** | Yukarıdaki tablo. |

## Bilinen eksikler / yapılamayanlar (dürüstçe)

1. **Gerçek Windows makinesinde hiç çalıştırılmadı.** Bu ortamda Windows
   yok (kontrol ettim: `/dev/kvm` yok, CPU'da `vmx`/`svm` yok — donanım
   hızlandırmasız bir Windows/Android emülatörü bu konteynerde pratikte
   kullanılamaz). `.exe` gerçek bir Windows PE ikili dosyası olarak
   üretildi ve tüm mantık, yukarıdaki gerçek uçtan uca test dahil, bu
   Linux makinesinde gerçek verilerle çalıştırıldı — ama Windows'a özgü
   davranışlar (gerçek süreç sahibi biçimleri, Defender/güvenlik duvarı
   etkileşimi, yönetici hakları gereksinimi, `ReadDirectoryChangesW`)
   hâlâ doğrulanamadı.
2. **Gerçek Android cihaz/emülatör hiç kullanılmadı** (aynı KVM eksikliği
   nedeniyle). Yukarıdaki uçtan uca test, Android uygulamasının gerçek
   protokol/kripto/repository kodunu bir JVM içinde canlı ajana bağladı —
   bu, telefon–ajan arasındaki GERÇEK VERİ AKIŞINI kanıtlar (ve nitekim
   iki gerçek hata da bu sayede bulundu) — ama QR kamera taramasının
   gerçek bir kareyi çözmesi, UI'ın bir ekranda gerçekten render edilmesi,
   ve arka planda/Doze modunda bağlantının davranışı hâlâ doğrulanamadı
   (bir foreground service eklenmedi — bilinçli bir kapsam kısıtlamasıdır).
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
