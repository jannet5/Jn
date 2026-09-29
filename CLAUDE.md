# CLAUDE.md

## Drive'a dosya yükleme

Yerleşik Google Drive bağlayıcısı (`create_file`) ~10 KB üzerindeki ikili dosyaları yükleyemez.
APK, .exe, zip gibi dosyaları kullanıcının Drive'ına atmak için `tools/drive-uploader/drive-upload.sh`
kullan. `DRIVE_UPLOAD_URL` ve `DRIVE_UPLOAD_SECRET` ortam değişkenleri tanımlı değilse kurulum
adımları `tools/drive-uploader/README.md`'de; kullanıcıdan anahtarı sohbete yapıştırmasını isteme,
ortam ayarlarına eklemesini söyle.

Sohbetteki dosya kartları (SendUserFile) kullanıcının Android Claude uygulamasında indirilemiyor
("Öğeler indirilemedi"); dosya teslimi için Drive'ı tercih et.
