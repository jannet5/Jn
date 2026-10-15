# Çalışma günlüğü — Behance tasarım medyası klasörü

Tarih: 2026-10-03 · Ortam: Claude Code bulut kapsayıcısı (Linux), kendi görev dalı.

## 1. İstenen
Behance'te "mobile app" proje aramasındaki ilk projeyi açmak; içindeki tüm görsel, GIF ve videoları tek
klasörde toplamak; kaynak/hak bilgisini eklemek ve indirilemeyenleri dürüstçe listelemek.
Özel görev metinleri ve sohbet yalnız özel çalışma alanında tutuldu; bu depoya konmadı.
İndirilen medya projenin lisansı `no-use` olduğu için **public depoya konmadı**; yalnız özel ZIP'te.

## 2. Sırayla yapılanlar
1. **Düz HTTP denemesi** — `curl https://www.behance.net/search/projects/mobile%20app` → HTTP 403,
   gövdede `js_challenge_value` çerezi + `location.reload()` (sitenin bot koruması; proxy engeli değil).
2. **Araştırma** — gallery-dl Behance çıkarıcısının kaynağı okundu (modül türleri: image, video,
   mediacollection, embed; boyut sırası source→max_3840→fs→hd→disp). Topluluk araçları (MBBR userscript,
   Chrome eklentileri), Behance telif rehberi ve ek şartlar okundu. Bağlantılar `harita.md` içinde.
3. **Chromium (Playwright 1.56.1)** — İlk denemede `ERR_CERT_AUTHORITY_INVALID`: ortam proxy'si TLS'i
   yeniden sonlandırıyor. Çözüm: yalnız proxy CA'sının SPKI özetini güvenilir kılan
   `--ignore-certificate-errors-spki-list` (genel TLS doğrulaması kapatılmadı). İkinci denemede headless
   kimlikle HTTP 400; standart masaüstü Chrome UA ile sayfa açıldı (`arac/arama.mjs`).
4. **İlk projenin kimliği** — Ekran görüntüsü ve bağlantı listesiyle doğrulandı: ilk kart
   `Niva | AI family assistant`, `l=0`, "PROMOTED" etiketi yok. 5. kart (`bid=` parametreli) sponsorlu.
   İki ayrı oturumda aynı sonuç.
5. **A yolu: gallery-dl 1.32.14** — tarayıcı çerezleri Netscape biçimine çevrilip verildi → yine 403. Kırıldı.
6. **B yolu** — `arac/proje.mjs` proje sayfasını açıp sonuna kadar kaydırdı; sayfadaki
   `beconfig-store_state` JSON'u okundu: 20 modül (16 Image, 2 Embed=Vimeo, 2 MediaCollection×2 öğe),
   4 sahip, lisans `no-use`, kaynak dosya yok.
7. **CDN testi** — `project_modules/source/...` 200 döndü (PNG ve GIF); `max_3840` yok (302) → `source` yeterli.
8. **Video** — yt-dlp 2026.08.19 → 401; `--impersonate chrome` (curl_cffi) → 401; Chromium içinde
   iframe → "We couldn't verify the security of your connection" (Cloudflare). Daha ileri atlatma denenmedi.
   Vimeo resmi oEmbed API'siyle başlık/süre/kapak karesi alındı; videolar EKSİK işaretlendi ve kullanıcının
   kendi bilgisayarı için `videolari-indir.ps1` eklendi.
9. **İndirme** — `arac/indir.py`: 25 kalem → 23 indirildi, 2 eksik. Hata: kapak `.png` adlı ama WebP
   geliyordu → uzantı içerik türünden belirlenecek şekilde düzeltildi, yeniden çalıştırıldı.
10. **Görünüm kontrolü** — ffmpeg ile numaralı temas sayfası. İlk iki üretimde yalnız 18–25 görünüyordu;
    neden: girişlerde piksel formatı değişince (rgba↔rgb24) tile filtresi yeniden başlıyor. Küçük
    resimler rgb24'e çevrilince 23 öğenin tamamı göründü ve tek tek proje içeriğiyle uyumlu bulundu.
11. **Biçim kontrolü** — `file`: 14 PNG (çoğu 2800 px), 6 GIF, 1 WebP kapak, 2 JPG kapak karesi (1920×1080).
    ffprobe: 6 GIF'in hepsi animasyonlu (8–364 kare).
12. **Paket** — ZIP 28 girdi, 105.788.845 bayt;
    SHA-256 `abfb1b70d181d30cc26061588565fceadb93aee84db1cae63a54c741000125a1`.
    `testzip` temiz, ayrı klasöre açılıp `diff -r` ile birebir aynı, manifestteki 23 SHA-256 eşleşti.

13. **Teslim yüklemesi** — 101 MB'lık tek ZIP sohbet dosya yüklemesinde HTTP 502 verdi. ZIP `split -b 25M`
    ile 5 parçaya bölündü (4×25 MiB + 0,9 MiB); parçalar birleştirilince SHA-256 yine
    `abfb1b70…0125a1` çıktı. 5 parça + `parcalar.sha256` + `BIRLESTIR.txt` (Windows `copy /b` talimatı)
    başarıyla gönderildi.

## 3. Sonuç
- Teslim edildi: 23 dosya (14 PNG + 6 GIF + kapak + 2 video kapak karesi), manifest.csv/json, BENI_OKU.md
  (kimlik, sahipler, haklar, eksikler), önizleme sayfası, video indirme betiği.
- Eksik: 2 Vimeo videosu (9 sn ve 20 sn). Engel: Vimeo'nun bu bulut ağına uyguladığı Cloudflare doğrulaması.
- Yapılmayan testler: Windows'ta `videolari-indir.ps1` çalıştırılmadı (bu ortamda Windows ve ev ağı yok).
- Not: Behance arama sırası kişiye/zamana göre değişir; "ilk proje" 2026-10-03 oturumsuz aramaya göredir.

## 4. Yeniden üretme
```
node arac/arama.mjs <cikti>          # ilk kartları listeler + ekran görüntüsü
node arac/proje.mjs <proje-url> <cikti>   # sayfa durumu JSON (proje.html içinden)
python3 arac/indir.py <state.json> <hedef> [vimeo-oembed.json]
```

## 5. Devam (2026-10-12 / 2026-10-14) — indirme sayfası
- Kapsayıcı yenilendiği için geçici dosyalar silinmişti. Paket depodaki araçlarla yeniden üretildi:
  25 kalem → 23 indirildi, 2 eksik (aynı sonuç). Vimeo yeniden denendi: yine HTTP 401; atlatma denenmedi.
- `BENI_OKU.md` ve `videolari-indir.ps1` kaybolmasın diye `teslim-ekleri/` altına depoya eklendi
  (medya ve özel bağlam yok).
- ZIP sıkıştırmasız (STORED) yeniden kuruldu: 28 girdi, 106.650.703 bayt,
  SHA-256 `25b2183c09101fdafd470a422e0ad9e6bd674ec998d07ea0aa60aabe896aa02c`. testzip temiz,
  açılan içerik kaynak klasörle `diff -r` birebir, manifestteki 23 SHA-256 eşleşti.
- İndirme sayfası (özel artifact): yayın servisi ikili `.bin` dosyaları kabul etmedi; ZIP 10 MiB'lik
  11 base64 `.txt` parçasına bölündü, 64 MB yayın sınırı nedeniyle 3 yayında yüklendi. Sayfa parçaları
  çözüp ZIP'i kurar, SHA-256 doğrular ve platformun `downloads` izniyle kaydettirir; tek tek dosya da
  ZIP içinden kesilip doğrulanarak kaydedilir (`arac/indirme-sayfasi.tmpl.html`).
- Doğrulama: yayındaki 11 parça geri okundu → birleşik SHA-256 aynı. Sayfa yerel Chromium'da test
  kancasıyla çalıştırıldı: ZIP (106.650.703 bayt, aynı SHA-256), bir GIF ve betik doğru çıktı; JS hatası
  yok. Telefon genişliğinde Kaydet düğmeleri ekran dışındaydı → düzeltildi, 400 px'te yatay taşma yok.
- Test edilemeyen: claude.ai görüntüleyicisindeki gerçek kaydetme onay penceresi (yalnız sayfayı açan
  kullanıcı tetikleyebilir) ve Windows'ta `copy /b` birleştirmesi.
- Sohbet eki: tek ZIP 30 MiB sınırını aştı; 5 ham parça + `BIRLESTIR.txt` + `parcalar.sha256` gönderildi.

## 6. Devam (2026-10-15) — kalan istekler
- Kullanıcının bilgisayarına bağlı bir oturum veya araç yok; yerel disk isteklerine buradan erişilemiyor.
- Kalan işler için Windows PowerShell betiği hazırlandı (yalnız okur, hiçbir şey silmez): 2 videoyu
  yt-dlp ile indirir, verilen klasördeki alt klasörlerin boyutlarını listeler, verilen geçici klasörün
  boyutunu, içeriğini ve onu kullanan program olup olmadığını raporlar. Rapor masaüstüne yazılır.
- Test: taşınabilir PowerShell 7.4.6 ile sahte klasörlerde çalıştırıldı (yt-dlp yok / var-401 /
  klasör yok / son 7 günde değişen dosya var-yok dalları). Windows PowerShell 5.1'de denenmedi.
- İndirme sayfasına "İsteklerinin durumu" bölümü ve betiği kaydet/kopyala düğmeleri eklendi; tarayıcı
  testinde kaydedilen betik dosyayla birebir aynı çıktı (SHA-256 eşleşti). Betik sohbet eki olarak da verildi.
