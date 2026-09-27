# CepGözcü Agent (Windows bileşeni)

Windows bilgisayarında çalışan, CepGözcü Android uygulamasının bağlandığı yardımcı programdır.
.NET 8 üzerinde yazılmıştır, gerçek sistem verilerini toplar (WMI/PerformanceCounter yerine doğrudan
Windows API'leri ve `System.Diagnostics`/`DriveInfo`/`FileSystemWatcher` kullanır), yerel ağ üzerinden
HTTPS + WebSocket ile telefona veri sağlar ve kontrol komutlarını uygular.

## Mimari

```
src/
  CepGozcu.Agent.Core/    Taşınabilir çekirdek: metrik toplama, süreç yönetimi, uygulama
                          başlatıcı, disk izleme motoru (canlı FileSystemWatcher + periyodik
                          tarayıcı + büyüme analitiği + uyarılar), güvenlik (sertifika, eşleştirme,
                          oturum anahtarları), SQLite veri erişimi. Windows'a özgü kod P/Invoke ile
                          izole edilmiştir; geri kalanı Linux/macOS üzerinde de derlenip test
                          edilebilir (bkz. testler).
  CepGozcu.Agent.Host/    Çalıştırılabilir uygulama: Kestrel HTTPS sunucusu, WebSocket hub, eşleştirme
                          ve yerel yönetim (admin) uç noktaları, yönetim panelinin statik HTML/JS'i.
tests/
  CepGozcu.Agent.Tests/   xUnit testleri: birim testleri + gerçek dosya sistemi/süreç
                          entegrasyon testleri + gerçek ASP.NET Core hattı üzerinden uçtan uca
                          eşleştirme akışı testi.
scripts/
  publish.ps1             Tek dosyalık, bağımsız (self-contained) win-x64 .exe üretir.
  install.ps1             .exe'yi Windows servisi olarak kurar + güvenlik duvarı kuralı ekler.
  uninstall.ps1           Servisi ve (isteğe bağlı) verileri kaldırır.
```

## Geliştirme ortamında çalıştırma

Gereksinim: [.NET 8 SDK](https://dotnet.microsoft.com/download/dotnet/8.0).

```bash
cd windows-agent
dotnet run --project src/CepGozcu.Agent.Host
```

Konsolda yazdırılan adresi (`https://127.0.0.1:<port>/`) bir tarayıcıda açarak yönetim paneline
ulaşabilirsiniz (kendinden imzalı sertifika olduğu için tarayıcı bir uyarı gösterecektir — bu,
telefonun ayrı bir mekanizmayla (parmak izi sabitleme) doğruladığı, beklenen bir durumdur).

Varsayılan port `47811`'dir; `CEPGOZCU_PORT` ortam değişkeniyle değiştirilebilir. Veriler (SQLite
veritabanı, TLS sertifikası, izin verilenler listesi) varsayılan olarak
`%ProgramData%\CepGozcuAgent` altında tutulur (`CEPGOZCU_DATA_DIR` ile değiştirilebilir).

## Testleri çalıştırma

```bash
dotnet test
```

51 test içerir: gürültü filtresi, kritik süreç koruması, oturum anahtarı yaşam döngüsü, eşleştirme
durum makinesi, büyüme analitiği hesaplamaları — hepsi birim testi; ayrıca gerçek bir geçici
klasörde dosya oluşturma/büyütme/yeniden adlandırma/taşıma/silme senaryolarını çalıştırıp
`DiskScanner` ve `FileSystemWatcherManager`'ın ürettiği olayları doğrulayan entegrasyon testleri,
gerçek bir alt süreç başlatıp sonlandıran `ProcessService` testleri, ve gerçek ASP.NET Core
hattından (sahte/mock sunucu değil) PIN doğrulama → yerel onay → oturum anahtarı akışını uçtan uca
doğrulayan bir test.

## Kalıcı kurulum (Windows servisi olarak)

Windows'ta, Yönetici olarak PowerShell'de:

```powershell
cd windows-agent
./scripts/publish.ps1                       # tek dosyalık .exe üretir (dist/win-x64 altına)
./scripts/install.ps1 -SourceDir .\dist\win-x64
```

`install.ps1`:
- Dosyaları `%ProgramFiles%\CepGozcuAgent` altına kopyalar,
- Yalnızca **Özel/Etki Alanı** ağ profillerinde (asla Genel/Public) gelen bağlantılara izin veren
  bir güvenlik duvarı kuralı ekler,
- Windows açılışında otomatik başlayan bir servis kaydeder ve başlatır.

Kaldırmak için: `./scripts/uninstall.ps1` (isteğe bağlı `-RemoveData` ve `-RemoveInstallDir`
anahtarlarıyla verileri de silebilirsiniz).

`publish.ps1` bu Linux geliştirme ortamında da (macOS'ta da) çalışır — `dotnet publish -r win-x64`
çapraz derleme yaptığı için gerçek bir win-x64 .exe üretir; yalnızca *çalıştırmak* için Windows
gerekir. Bu depoda hazırlanan derleme bu şekilde üretilip doğrulanmıştır (bkz. depo kökündeki
README'de SHA-256 değeri).

## Eşleştirme akışı (özet)

1. Yönetim panelinde "Eşleştirme kodu oluştur"a basılır → QR kod (bağlantı bilgisi + sertifika
   parmak izi) ve ayrı olarak 6 haneli bir PIN gösterilir.
2. Telefon QR'ı okutur, PIN'i kullanıcıdan ister.
3. Doğru PIN girilirse istek "onay bekliyor" durumuna geçer; **bilgisayarın başında biri** yönetim
   panelinden onaylamadan hiçbir oturum anahtarı üretilmez.
4. Onaylanınca telefona bir oturum anahtarı (bearer token) verilir; bundan sonraki tüm istekler bu
   anahtar + sabitlenmiş TLS sertifikası ile kimlik doğrulanır ve denetim günlüğüne yazılır.

## Güvenlik modeli ve sınırlar

- **Yalnızca yerel ağ.** Sunucu, bağlanan IP'nin loopback veya özel (RFC1918/ULA) aralıkta olup
  olmadığını her istekte kontrol eder (`LanOnlyMiddleware`) — güvenlik duvarı kuralı yanlış
  yapılandırılsa bile uygulama katmanında ikinci bir sınır vardır. İnternet üzerinden (WAN/röle
  sunucusu) erişim v1'de **kasıtlı olarak yoktur**; bu, ekstra bir röle sunucusunun/bulut
  bağımlılığının getireceği saldırı yüzeyini ve karmaşıklığı reddetmenin bilinçli bir tercihidir.
- **Yönetim paneli yalnızca bu bilgisayardan** (`LoopbackOnlyMiddleware`) — eşleştirme onayı, izin
  verilenler listesi, izlenen klasörler yalnızca fiziksel olarak bu makineye erişimi olan biri
  tarafından değiştirilebilir; telefon (veya ağdaki başka biri) bunları asla değiştiremez.
- **Uygulama başlatma yalnızca izin verilenler listesiyle sınırlıdır** — telefon rastgele bir yol
  çalıştıramaz, yalnızca bu bilgisayarda önceden eklenmiş uygulamaları.
- **Kritik süreç koruması** sunucu tarafında zorunludur (`CriticalProcessGuard`) — istemci neyi
  "kritik" saydığına bakılmaksızın uygulanır.
- **Genel amaçlı komut çalıştırma yoktur.** Protokolde rastgele shell komutu çalıştıran bir uç
  nokta yoktur; yalnızca sabit bir mesaj kümesi (metrik oku, süreç listele/sonlandır, izinli
  uygulama başlat, disk geçmişini sorgula) vardır.
- **Cihaz anahtarı ile zorlu kimlik doğrulama yok (v1 sınırı):** Eşleştirme isteğinde gönderilen
  "cihaz genel anahtarı" alanı şu an yalnızca bilgi amaçlıdır (yönetim panelinde cihazları ayırt
  etmek için); gerçek yetkilendirme sabitlenmiş TLS kanalı + PIN + yerel onay + opak oturum
  anahtarına dayanır. Gelecekte cihaz başına imzalı challenge/response eklenmesi makul bir
  sertleştirme adımıdır ama v1'de yoktur — bu doğrudan söylenmelidir, örtük güven vaat edilmez.
- Sertifika kendinden imzalıdır ve hiçbir CA'ya güvenilmez; telefon parmak izini eşleştirme anında
  sabitler (TOFU — SSH host key modeliyle aynı mantık).
