# Çalışma kuralları (kullanıcının kendi isteği)

- Kullanıcının bu projede yazdığı **her isteği** oku, maddelere ayır ve **hepsini yap**. Hiçbirini atlama, "sonra" deme.
- İş **bitmeden** bitti deme. Bitir, test et, commit + push yap.
- Sonucu her zaman **kart olarak** ver: APK/indirme linkleri, kısa kullanım ve istek → durum listesi olan bir Artifact sayfası. Kullanıcı kartı telefondan açıp kullanıyor.
- Kullanıcıyla Türkçe, samimi ve kısa konuş. Kullanıcı genelde telefondan yazıyor, bilgisayarı yok.
- Test edilemeyen bir şey varsa (örneğin YouTube bu sunucuyu engellediği için gerçek indirme) bunu açıkça söyle; "çalışıyor" deme.

# Projeler

- `medya-oynatici/` — **Oynatıcı**: sade Android MP3/video oynatıcı (Kotlin + Compose). Klasörler, dosya/klasör ekleme, YouTube'dan sunucusuz indirme (yt-dlp + ffmpeg telefonda), kırpma. Ayrıntı ve test tablosu: `medya-oynatici/README.md`. Hazır APK'lar: `medya-oynatici/dist/`.
- `android-app/`, `windows-agent/` — WinRemoteMonitor (ayrı, eski proje).
