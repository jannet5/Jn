# Çalışma günlüğü: PC ses kayıt kalitesi ve temizleme akışı

**Tarih:** 2026-10-03
**Ortam:** Claude Code bulut (Linux) konteyneri. Dal: `claude/laughing-faraday-jqcz19`. Çalışma klasörü: `gorevler/pc-ses-kayit-kalitesi-ve-temizleme-akisi/`

## 1. Ne istendi (özet)

- Kullanıcının yeni mikrofon alacak bütçesi yok.
- Mevcut PC mikrofonuyla "profesyonellerin kullandığı" kalitede kayıt istiyor.
- Önceki konuşmada Audacity önerilmiş. Kullanıcı şunları sordu:
  - "Herkes gerçekten Audacity mi kullanıyor?"
  - "Kötü mikrofonu prodüksiyona yaklaştıran program var mı?"
  - "Kayıt öncesi/sonrası takip tablosu yap."
  - "İki kaydı temizle düğmesi ne, Audacity'nin içinde mi?"
  - "Audacity içinde yapılan bir şey yok mu, hangisi daha iyi?"
- Görev tanımındaki zorunlu kabul maddeleri:
  1. Alternatifleri gerçek kullanıcı deneyimleriyle karşılaştır.
  2. Kayıt öncesi/sonrası ayarları takip akışına çevir.
  3. Mevcut iki kayıtta önce/sonra karşılaştırması yap ve prodüksiyon kalitesini ölçmeden vaat etme.
- Özel kaynak metni ve görev dosyası yalnız özel çalışma alanına kaydedildi. Public depoya konmadı.

## 2. Kararlar ve gerekçeler (sırayla)

1. **Araştırma iki paralel alt görevle yapıldı.**
   - (a) Araç karşılaştırması ve kullanıcı deneyimi.
   - (b) Kayıt tekniği, Windows/Audacity ayarları ve makro sözdizimi.
   - Sonuç ve bağlantılar: `arastirma.md`.
   - **Önemli bulgu:** Audacity 4.0 makro ve OpenVINO desteklemiyor. Bu yüzden 3.7.x seçildi.
2. **Kullanıcının iki gerçek kaydı yüklenmedi.** Uydurulmadı. Yerine açıkça "benzetim" etiketli iki bozuk-mikrofon kaydı üretildi (`test/arac/sim.py`). Temiz konuşma kaynağı: Microsoft DNS-Challenge test seti.
3. **Söz vermeden önce ölçüm** için şu yöntemler seçildi:
   - DNSMOS P.835 (referanssız kalite, Microsoft ONNX modeli)
   - STOI (anlaşılırlık)
   - EBU R128 LUFS / gerçek tepe
   - Gürültü tabanı
4. **Audacity yolu gerçekten çalıştırıldı.**
   - Ubuntu backports'tan Audacity 3.7.3 kuruldu. Xvfb + openbox sanal ekranında `mod-script-pipe` ile makro çalıştırıldı.
   - Noise Reduction ayarları yapılandırma dosyasından verildi.
5. **Temizleme aracı olarak DeepFilterNet3 seçildi.** Sebepleri:
   - ücretsiz
   - yerel çalışır
   - GPU istemez
   - Windows'ta tek dosya (`deep-filter.exe`)
   - ölçümde en iyi sonucu verdi
   - Kullanıcının Audacity tercihi korundu: kayıt ve düzenleme orada, isteğe bağlı makro yolu da orada.
6. **Betik Windows PowerShell 5.1 uyumlu yazıldı.**
   - Üçlü operatör ve `??` yok.
   - Harici programlar `System.Diagnostics.Process` ile çalıştırılıyor (5.1'deki stderr→hata tuzağına düşmemek için).
   - Dosya UTF-8 **BOM'lu** ve CRLF. BOM olmazsa 5.1 Türkçe karakterleri bozar.
7. **Gerekçeli ayar değişiklikleri** (ölçüm sonucuna göre):
   - **Sıkıştırma artık gürültü bastırmadan önce yapılıyor.** Önceki sıra gürültü tabanını ~17 dB kötüleştiriyordu. Yeni sırada kayit2 OVRL 1.53 → 2.14 oldu.
   - **Ön seviye ayarı eklendi (−20 LUFS).** Kısık girişte DeepFilterNet tam güçte konuşmayı da yiyordu (STOI 0.437). Düzeltme sonrası STOI 0.677 oldu ve tam güç varsayılan yapıldı.
   - **Klasik mod (afftdn) düzeltildi.** `tn=1` hiç azaltma yapmıyordu. Ölçülen tabana göre `nf`, `nt=w` ve `nr=20` kullanıldı.
8. **ffmpeg ZIP'e konmadı** (boyut ve GPL dağıtımı). Kurulum betiği winget ile `Gyan.FFmpeg` kuruyor; manifestin varlığı winget-pkgs deposunda doğrulandı. **deep-filter.exe ZIP'e kondu** (MIT/Apache, SHA-256 doğrulamalı).

## 3. Kullanılan gerçek araçlar ve komutlar (özet)

- **Kurulumlar:**
  - `pip install numpy scipy soundfile pystoi onnxruntime librosa`
  - pesq derlenemedi, bu yüzden PESQ kullanılmadı.
  - `apt-get install -t noble-backports audacity xvfb xdotool openbox wine64`
  - PowerShell 7.4.6 (GitHub sürüm arşivi)
- **İndirilen dosyalar:**
  - DeepFilterNet `deep-filter` 0.5.6: Linux ve Windows ikilileri, GitHub sürümleri.
  - RNNoise modelleri: GregorR/rnnoise-models.
  - DNSMOS `sig_bak_ovr.onnx` ve `dnsmos_local.py`: microsoft/DNS-Challenge.
- **Karşılaştırma:**
  - `test/arac/karsilastirma.sh` (ffmpeg, pwsh, deep-filter, ölçüm)
  - Audacity için `test/arac/audacity_pipe.py` komutları:
    ```
    Import2 → Select 0.5–2.5 → NoiseReduction: → SelectAll: → Macro_Ses-Temizle: → Export2
    ```
- **Windows ikilisi:**
  ```
  wine deep-filter.exe -D -a 100 -o outw w.wav
  ```
  Linux çıktısıyla karşılaştırıldı: fark 0.0 (bit-bit aynı). Wine altında RTF 3.0, yani emülasyon yavaş. Gerçek Windows hızı ölçülmedi.

## 4. Sorunlar ve çözümler

| Sorun | Çözüm |
|---|---|
| `github.com/.../raw/...` erişimi 403 verdi (depo oturuma ekli değil) | `raw.githubusercontent.com` ve `media.githubusercontent.com` (LFS) kullanıldı. |
| Audacity betik kanalı açılmadı (modül "yeni" durumunda, değeri 4) | `audacity.cfg` içinde `mod-script-pipe=1` ve `ModuleDateTime` girildi. |
| Pencere yöneticisi yokken diyaloglar görünmedi ve kurtarma penceresi takıldı | openbox kuruldu. Otomatik kurtarma dosyaları (`/var/tmp/audacity-root`) temizlendi. |
| Betik kanalı istemcisi son yanıttan sonra takıldı | Yanıt sonu `BatchCommand finished` satırına göre okunacak şekilde düzeltildi. |
| Kendi kabuk komutu `pkill -f` ile öldürüldü | Süreçler PID ile sonlandırıldı. |
| PowerShell ilk dosya adını `-Mod` parametresine bağladı | `[CmdletBinding(PositionalBinding = $false)]` eklendi. |
| DNSMOS seviyeye duyarlı, karşılaştırmayı bozuyordu | Ölçümden önce −25 dBFS RMS'e ölçekleme eklendi. Tüm tablo yeniden ölçüldü. |
| loudnorm `linear` modu bazı dosyalarda hedefin ~1 dB altında kaldı (−17.2 LUFS) | Tepe sınırı nedeniyle. Kabul edildi ve raporda yazıldı. |

## 5. Doğrulamalar

- Tüm sayılar `test/sonuclar.csv` dosyasında.
- Görsel: `test/gorseller/kayit2_karsilastirma.png` açılıp incelendi.
- Betik uçtan uca senaryoları ve ayrıştırıcı denetimi: `test/TEST-RAPORU.md`.
- **Yapılamayanlar:**
  - kullanıcının iki gerçek kaydı
  - Windows PowerShell 5.1, `.bat` ve winget testi
  - OpenVINO eklentisi
  - Adobe Podcast (hesap ve yükleme gerektirir; kullanıcı adına dış hizmete ses yüklenmedi)
  - kulakla dinleme (bulutta ses çalınamaz; dinleme kullanıcıya bırakıldı ve önce/sonra dosyaları teslim edildi)

## 6. Teslim

- **Public dal:** yalnız kod, kılavuz, araştırma ve ölçümler. Commit ve push sonrası uzak daldan geri okunarak doğrulandı.
- **Özel ZIP:** public içerik + `deep-filter.exe` + test sesleri (ham / temiz / önce-sonra) + `ozel/` (kaynak.txt, gorev.md). SHA-256 alındı; ZIP açılıp dosya özetleri yeniden karşılaştırıldı. Kullanıcıya indirilebilir dosya olarak gönderildi.
