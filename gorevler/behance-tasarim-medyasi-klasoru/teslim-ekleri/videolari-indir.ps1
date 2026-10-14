# Niva (Behance 241915081) projesindeki 2 Vimeo videosunu kendi bilgisayarınızda indirir.
# Bulut ortamında Vimeo, Cloudflare güvenlik doğrulamasıyla 401 döndürdüğü için bu iki dosya
# pakete eklenemedi. Ev/ofis bağlantısında genellikle çalışır.
# Gerekli: yt-dlp (https://github.com/yt-dlp/yt-dlp) -> winget install yt-dlp.yt-dlp
# Kullanım: bu klasörde PowerShell açın ve:  .\videolari-indir.ps1
$ErrorActionPreference = "Stop"
$ref = "https://www.behance.net/gallery/241915081/Niva-AI-family-assistant"
yt-dlp --referer $ref -o "02_modul01_video-1154829526_final.%(ext)s"   "https://player.vimeo.com/video/1154829526"
yt-dlp --referer $ref -o "16_modul13_video-1154828935_Scene-1.%(ext)s" "https://player.vimeo.com/video/1154828935"
