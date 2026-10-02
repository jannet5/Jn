# TekPanel — kuruluma hazır APK

`TekPanel-debug.apk`, bu depodaki kodun test/kullanım için derlenmiş debug sürümüdür.

- **İmza:** Android'in geliştirme (debug) anahtarıyla imzalı — Google Play'e yüklenemez, ama
  doğrudan bir telefona kurmak için geçerlidir.
- **SHA-256:** `3aac3ec4162c1e72bfe0150d7e64fa8f1e4b00ed09dc3e18802e2e2807a49ebd`
- **Nereden geldiği:** `commit 69ac86e`, `./gradlew :app:assembleDebug` ile üretildi.

## Telefona kurma

1. Bu dosyanın GitHub "raw" bağlantısını telefonunun tarayıcısında aç (bağlantı sohbette
   verildi). Dokununca indirme başlar.
2. İndirilen dosyaya dokun. Android ilk seferde "bu kaynaktan kuruluma izin ver" diye
   sorabilir (tarayıcı uygulaman için) — bu Android'in kendi güvenlik adımıdır, bir kere
   izin verince kurulum devam eder.
3. Kurulumdan sonra uygulamayı aç, bildirim erişimi isteğini onayla, kullanmak istediğin
   kanalları (WhatsApp, Instagram, vb.) aç.

## Bu dosyayı güncellemek

Kod her değiştiğinde bu APK otomatik güncellenmez; yeniden derleyip bu dosyanın üzerine
yazıp tekrar commit/push etmek gerekir:
```
./gradlew :app:assembleDebug
cp app/build/outputs/apk/debug/app-debug.apk dist/TekPanel-debug.apk
git add dist/TekPanel-debug.apk && git commit -m "Update debug APK" && git push
```
