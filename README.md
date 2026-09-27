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
| Windows ajanı (.exe) | `windows-agent/dist/winremotemonitor-agent.exe` | `b33a6c625584de53e769984c6754b4d10494efb26a0c55319b27efe6fb5d4264` |
| Android APK (release, imzalı) | `android-app/dist/app-release.apk` | `8945fc5dc196ddbdc816ce96be3a0faf0e2a349950b42eb25917433197adbc9b` |
| Android APK (debug) | `android-app/dist/app-debug.apk` | `524dc54095101f26c908a76a29ec293aeac003a3dae51ac8c12586f488da60c0` |

Bu değerler `windows-agent/dist/SHA256SUMS.txt` ve
`android-app/dist/SHA256SUMS.txt` dosyalarında da bulunur; ikisi de bu
depoya commit edilmiştir (ikili dosyaların kendisi `.gitignore` ile
depodan hariç tutulmuştur — büyük binary'leri git'te taşımamak için;
teslimat sırasında dosyaların kendisi ayrıca iletilir).

`android-app/dist/app-release.apk`, depoya işlenmiş bir geliştirme/lokal
imzalama anahtarıyla (`android-app/keystore/dev-release.jks`, parolası
bilerek gizli değildir — bkz. `android-app/README.md`) imzalanmıştır;
gerçek dağıtımdan önce gerçek bir imzalama anahtarıyla değiştirilmelidir.

## Doğrulanan vs. doğrulanamayan — kabul kriterleri karşılaştırması

Bu proje bir **Linux konteynerinde**, gerçek bir Windows makinesi ve
gerçek bir Android cihaz/emülatör OLMADAN geliştirildi. Aşağıdaki tablo
her kabul kriteri için gerçekten neyin doğrulandığını (kod çalıştırılıp
test edilerek) ve neyin sadece "derlendi/mantık doğru görünüyor" ama
gerçek donanımda hiç denenmediğini açıkça ayırır.

| Kriter | Durum | Not |
|---|---|---|
| Telefon–PC güvenli eşleşme ve bağlantı | **Kısmen doğrulandı** | Protokol, TLS+parmak izi sabitleme, eşleştirme, HMAC challenge-response uçtan uca gerçek bir TLS soketi üzerinden test edildi (Go tarafı, gerçek `gorilla/websocket` istemcisiyle). Android istemcisinin gerçek bir Windows ajanına canlı bağlanması hiç denenmedi. |
| CPU/RAM/disk gerçek sistem değerleriyle uyuşuyor | **Kısmen doğrulandı** | Gerçek `gopsutil` kod yolu bu Linux makinesinin gerçek değerleriyle test edildi (sahte veri yok). Windows'taki gerçek değerler hiç görülmedi. |
| Gerçek çalışan süreçler listeleniyor | **Kısmen doğrulandı** | Aynı şekilde — gerçek süreç listeleme kodu, bu makinenin gerçek süreçleriyle (kendi PID'i dahil) test edildi; Windows'ta denenmedi. |
| İzinli test uygulaması açılıp kapatılabiliyor | **Doğrulanamadı** | `launch_app`/`kill_process` mantığı ve izin-listesi/koruma kontrolleri birim testlidir; telefon–ajan arasında gerçek bir aç/kapat işlemi hiç çalıştırılmadı. |
| Kritik süreç koruması çalışıyor | **Kısmen doğrulandı** | Kural mantığı (§5) kapsamlı tablo-testli; gerçek Windows süreç sahibi/isim biçimleri (`OpenProcessToken` çıktısı vb.) doğrulanamadı. |
| Dosya olayları (oluştur/büyüt/taşı/yeniden adlandır/sil) doğru gösteriliyor | **Kısmen doğrulandı** | `fsnotify` tabanlı gerçek dosya olayları bu Linux dosya sisteminde test edildi; Windows'un `ReadDirectoryChangesW` arka ucu hiç çalıştırılmadı. |
| Diski dolduran dosya/klasörler zaman içinde anlaşılabiliyor | **Mantık doğrulandı** | Anlık görüntü farkı/büyüme hesaplama mantığı birim testli; gerçek, uzun süreli bir Windows diskinde hiç çalıştırılmadı. |
| Çevrimdışı kalma / yeniden bağlanma düzgün çalışıyor | **Mantık doğrulandı** | Üstel geri çekilme (backoff) sıralaması test edildi; gerçek bir ağ kesintisi sonrası yeniden bağlanma hiç canlı denenmedi. |
| Yetkisiz istemciler veri okuyamıyor/işlem yapamıyor | **Doğrulandı** | Açık bir test, kimliği doğrulanmamış bağlantının her korumalı mesaj tipinde reddedildiğini kanıtlıyor. |
| Otomatik testler ve release build başarıyla tamamlanıyor | **Doğrulandı** | Go: `go vet` temiz, **123/123 test geçti**. Android: **80/80 JVM birim testi geçti**, `assembleDebug`+`assembleRelease` başarılı. |
| Güncel, kurulabilir Android APK üretiliyor | **Doğrulandı (yapısal olarak)** | Gerçek, geçerli, imzalı bir APK üretildi ve doğrulandı (`apksigner verify`); bir cihaza gerçekten kurulup açılması hiç denenmedi (emülatör/cihaz yok). |
| Windows tarafında kolay kurulup çalıştırılabilir çıktı | **Kısmen** | Tek bir çalıştırılabilir `.exe` + örnek config dosyaları + adım adım talimat var; **gerçek bir kurulum sihirbazı/MSI yok** — bu bilinen bir eksiklik (aşağıya bakın). |
| Kurulum ve kullanım adımları açık | **Doğrulandı** | Bu belge + `windows-agent/README.md` + `android-app/README.md`. |
| Teslim dosyalarının konumu ve SHA-256'sı bildiriliyor | **Doğrulandı** | Yukarıdaki tablo. |

## Bilinen eksikler / yapılamayanlar (dürüstçe)

1. **Gerçek Windows makinesinde hiç çalıştırılmadı.** Bu ortamda Windows
   yok. `.exe` gerçek bir Windows PE ikili dosyası olarak üretildi ve tüm
   Windows-bağımsız mantık bu Linux makinesinde gerçek verilerle test
   edildi, ama Windows'a özgü davranışlar (gerçek süreç sahibi
   biçimleri, Defender/güvenlik duvarı etkileşimi, yönetici hakları
   gereksinimi, `ReadDirectoryChangesW`) doğrulanamadı.
2. **Gerçek Android cihaz/emülatör hiç kullanılmadı.** QR kamera taraması
   gerçek bir kareyi hiç çözmedi; UI hiçbir ekranda gerçekten render
   edilmedi/screenshot alınmadı; arka planda/Doze modunda bağlantının
   davranışı bilinmiyor (bir foreground service eklenmedi — bilinçli bir
   kapsam kısıtlamasıdır).
3. **Uçtan uca canlı bağlantı hiç denenmedi** — iki taraf da ayrı ayrı
   gerçek verilerle test edildi, ama telefon gerçekten bir Windows
   ajanına hiç bağlanmadı.
4. **Windows tarafında kurulum sihirbazı/servis sarmalayıcı yok** — sadece
   çalıştırılabilir bir konsol uygulaması var; otomatik başlatma (Görev
   Zamanlayıcı/Windows Hizmeti) elle kurulmalı, talimatları
   `windows-agent/README.md`'de.
5. **Eşleştirme durumu bellekte tutuluyor** — ajan yeniden başlatılırsa
   bekleyen (henüz kullanılmamış) bir eşleştirme kodu geçersiz olur;
   zaten eşleşmiş cihazlar etkilenmez (onlar sqlite'ta kalıcıdır).
6. **Protokolde iki küçük belirsizlik** vardı ve geliştirme sırasında
   çözüldü — ayrıntılar `docs/PROTOCOL.md` §2/§8/§10'da ve her iki
   `README.md`'de: cihaz sırrının saklanma biçimi (tek yönlü özet değil,
   geri döndürülebilir şifreleme) ve QR yükünün tam alan adları.

Bu maddelerin dışında iddia edilen hiçbir özellik "bitti" olarak
sunulmamıştır; yukarıdaki tablo tam liste.
