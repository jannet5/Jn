# Denetim bulgularının doğrulanması

## Ne yapılamadı (somut engel)
Bulgular, kullanıcının yerel diskindeki mevcut Company OS kaynağına (`src/company_os/ledger.py`, `qa_runner.py`, `artifacts.py`) ait. Bu kod bulut ortamına **yüklenmedi**. Windows yolları konteynerden okunamıyor. Bu yüzden bulgular **o kodun güncel sürümü üzerinde yeniden çalıştırılamadı**. Eski denetim metni de güncel kanıt sayılmadı.

**Gereken gerçek girdi:** `company-os-native-v1-*` klasörünün kaynak kodu, yani `src/company_os/` ve testleri. ZIP olarak ya da bir depoya push edilerek sağlanabilir.

## Ne yapıldı
Her bulgu, uygulamadan bağımsız bir **davranış sözleşmesine** çevrildi (`tests/test_bulgular.py`). Bu sözleşmeyi karşılayan yeni bir defter yazıldı. Sonra her hata koda **geri enjekte edildi** ve testlerin onu yakaladığı gösterildi (`kabul/mutasyon_kontrolu.py`, çıktısı `kabul/04-mutasyon-kontrolu.txt`).

| # | Bulgu (özet) | Düzeltme | Regresyon testi | Hata enjekte edilince |
|---|---|---|---|---|
| 1 | `transition()` ile `ready→running` geçişi `claim()`'i atlıyor; iki yazar aynı anda running olabiliyor | `ADMIN_TRANSITIONS` beyaz listesinde `running` ve `succeeded` hedefi yok. `running` durumuna tek giriş `claim()`: `BEGIN IMMEDIATE` içinde `WHERE state='ready'` koşulu ve rowcount kontrolü | `B1_*`: 12 iş parçacığı, 12 ayrı SQLite bağlantısı, tek kazanan | 1/2 test başarısız |
| 2 | Süresi dolmuş worker, yerine geçen denemeye artifact veya kanıt ekleyip görevi başarılı yapabiliyor | Global monoton **fencing token**. Her mutasyon (`put_artifact`, `register_artifact`, `run_evidence`, `post_message`, `heartbeat`, `complete`, `fail`) aynı işlem içinde `state='running' AND lease_token=? AND lease_expires>now` koşulunu doğruluyor. Artifact'lar deneme başına ayrı dizine yazılıyor. Reddedilen yazmalar ayrı işlemde `stale_write_rejected` olayı olarak kaydediliyor | `B2_*`: reconcile edilmemiş ve edilmiş iki durumda 7 mutasyonun tümü `LeaseLost` | 2/3 test başarısız |
| 3 | `reconcile()` okuma ile güncelleme arasında başka bağlantı görevi yeniden alırsa yeni canlı lease'i iptal ediyor | Tek `BEGIN IMMEDIATE` işlemi ve satır başına `WHERE lease_token=<okunan> AND lease_expires<=now` koşulu. `RLock` değil, veritabanı kilidi kullanılıyor | `B3_*`: rakip bağlantı kilit bekliyor, yeni lease korunuyor; 6 iş parçacığı ve 15 görevlik stres testinde her görev tam bir kez succeeded, tüm kabul edilen yazmalar kazanan token'a ait | 2/2 test başarısız |
| 4 | `acceptance_gate()` kanıtı kontrol edilen snapshot'a bağlamıyor; boş harita ve ilgisiz artifact geçiyor | Snapshot çağırandan alınmıyor; defterden ve diskten üretiliyor. Snapshot boş olamaz. Disk hash'i kayıtla eşit olmalı. Kanıt mevcut deneme ve token'a ait olmalı ve çıkış kodu 0 olmalı. Kanıttaki `(yol, sha256)` referansları snapshot ile birebir eşleşmeli. Her artifact kapsanmalı. Kanıt komutunu defter kendisi çalıştırıyor; komut öncesi ve sonrası hash'ler karşılaştırılıyor | `B4_*`: 8 senaryo (boş, kanıtsız, eski deneme, sonradan değişen, diskte kurcalanan, kapsanmayan, başarısız çıkış, yabancı görev referansı) | 6/8 test başarısız |
| 5 | `write_bundle()` önce yolu kontrol edip sonra yazıyor (TOCTOU); junction ya da symlink yazmayı kök dışına taşıyabiliyor | POSIX: kökten `O_DIRECTORY\|O_NOFOLLOW` ile dir_fd zinciri, `O_CREAT\|O_EXCL\|O_NOFOLLOW` geçici dosya, fsync, `rename(src_dir_fd,dst_dir_fd)`, dizin fsync. Okuma `O_NOFOLLOW` ve `fstat` ile normal dosya kontrolü | `B5_*`: üst dizin symlink, hedefte symlink, symlink üzerinden okuma, **tutamak alındıktan hemen sonra üst dizinin symlink ile değiştirilmesi saldırısı** (dışarıya hiçbir şey yazılmadı) | 3/5 test başarısız |

## Windows notu (bulgu 5)
Python'da Windows'ta `dir_fd` desteklenmiyor. Taşınabilir yol her bileşeni `lstat` ile kontrol ediyor (symlink ve `FILE_ATTRIBUTE_REPARSE_POINT`, yani junction). Bu kontrol yazmadan **önce ve sonra** yapılıyor ve geçici dosya `O_EXCL` ile oluşturuluyor. Bu yol, kontrol ile `os.replace` arasındaki çok kısa pencereyi **tamamen kapatamıyor**. Tam kapatma için `CreateFileW` + `FILE_FLAG_OPEN_REPARSE_POINT` ve tutamak tabanlı `SetFileInformationByHandle(FileRenameInfo)` gerekiyor (ctypes ile; yapılmadı). Taşınabilir yol, Linux'ta `force_portable=True` ile testlerden geçti (yarış testi hariç). **Gerçek Windows'ta çalıştırılmadı.**

## Mevcut koda taşıma
Yeni modüllerin genel API'si eski adlarla yakın tutuldu: `Ledger.claim/transition/reconcile`, `qa.acceptance_gate`, `artifacts.write_bundle`. Kaynak yüklendiğinde iki adım var:
1. `tests/test_bulgular.py` içe aktarma satırları eski modüle yönlendirilip testler eski kodda çalıştırılır. Beklenen sonuç, bulguların **kırmızı** olarak doğrulanması.
2. İlgili fonksiyonlar bu paketteki karşılıklarıyla değiştirilir ve testler yeşile döner.
