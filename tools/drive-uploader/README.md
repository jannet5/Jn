# Drive yükleyicisi

Claude oturumlarının büyük dosyaları (APK, .exe, zip) Google Drive'a atabilmesi için küçük bir
araç. Neden gerekli: Claude'un yerleşik Google Drive bağlayıcısı ~10 KB üzerindeki ikili
dosyaları yükleyemiyor ([bilinen hata](https://github.com/anthropics/claude-code/issues/54137)).

İki parçadan oluşur:

- `Code.gs` — Drive hesabınızda çalışan bir Google Apps Script web uygulaması. Yalnızca dosya
  **alır ve kaydeder**; Drive'ınızdaki dosyaları okuyamaz, listeleyemez, silemez. Her istek
  gizli bir anahtarla doğrulanır. Dosyalar Drive'da **"Claude Yüklemeleri"** klasörüne düşer.
- `drive-upload.sh` — oturumdan dosyayı gönderen ve Drive'ın döndürdüğü MD5 ile doğrulayan betik.

## Kurulum (bir kez, ~5 dakika)

1. **script.google.com** → **Yeni proje**. Varsayılan kodu silin, `Code.gs` içeriğini yapıştırın,
   kaydedin (proje adı örn. "Claude Yükleyici").
2. Üstteki işlev listesinden **`setupSecret`**'i seçip **Çalıştır**. Google izin isteyecek:
   hesabınızı seçin → "Google bu uygulamayı doğrulamadı" uyarısında **Gelişmiş → (proje adı)
   sayfasına git** → **İzin ver**. (Uyarı çıkar çünkü uygulama sizin kendi projeniz; yayımlanmış
   değil.)
3. Alttaki **Yürütme günlüğü**'nde `Gizli anahtar (DRIVE_UPLOAD_SECRET): ...` satırı görünür.
   Bu değeri kopyalayın. **Kimseyle, sohbette de dahil, paylaşmayın.**
4. Sağ üstte **Dağıt → Yeni dağıtım** → tür: **Web uygulaması**.
   - Şu kullanıcı olarak yürüt: **Ben**
   - Erişimi olan kullanıcılar: **Herkes**
   **Dağıt**'a basın ve çıkan **Web uygulaması URL**'sini (`.../exec` ile biter) kopyalayın.
5. Claude Code'da oturumun başlık çubuğundaki bulut ortam menüsü → **Düzenle** → ortam
   değişkenlerine ekleyin:
   ```
   DRIVE_UPLOAD_URL=<4. adımdaki URL>
   DRIVE_UPLOAD_SECRET=<3. adımdaki anahtar>
   ```
   Yeni açılan oturumlar bu değişkenleri görür.

## Kullanım

```bash
tools/drive-uploader/drive-upload.sh android-app/app/build/outputs/apk/release/app-release.apk CepGozcu-1.0.0.apk
```

Çıktıda dosyanın Drive linki ve doğrulanmış MD5'i yer alır.

## Güvenlik notları

- "Herkes" erişimi, web uygulamasının Google girişi olmadan çağrılabilmesi için gerekli. Koruma
  gizli anahtardır: anahtarı bilmeyen hiçbir istek kabul edilmez.
- Anahtarın ele geçtiğinden şüphelenirseniz Apps Script'te **`rotateSecret`**'i çalıştırın; eski
  anahtar anında geçersiz olur. Ortam değişkenini yeni değerle güncelleyin.
- Tamamen kapatmak için: **Dağıt → Dağıtımları yönet → Arşivle**.
- Uygulama tam Drive izni ister (Apps Script'in `DriveApp` servisi bunu gerektirir), ancak kod
  yalnızca kendi klasörüne dosya ekler. Kodun tamamı `Code.gs`'dedir, kısa ve okunabilirdir.
