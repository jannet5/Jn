# Behance — "Niva | AI family assistant" medya klasörü

## Proje kimliği (arama sırasında doğrulandı)
- Arama: https://www.behance.net/search/projects/mobile%20app (2026-10-03, "Recommended" sıralama, oturum açılmadan)
- İlk sonuç: **Niva | AI family assistant** — kart konumu `l=0`, "PROMOTED" etiketi yok (sponsorlu değil). Aynı arama iki ayrı tarayıcı oturumunda açıldı; ikisinde de ilk sıradaydı.
- Proje: https://www.behance.net/gallery/241915081/Niva-AI-family-assistant (ID 241915081)
- Yayın: 2026-01-16 (UTC)
- Sahipler: Estrella Gracia (https://www.behance.net/estrellgracia), Luca Giordano (https://www.behance.net/lucagiordano2), Aina Sanz (https://www.behance.net/ainasanz), Isla Pearce Cano (https://www.behance.net/islapearcecano-)
- Araçlar: Adobe XD, Photoshop, Illustrator, After Effects
- Not: Behance arama sonuçları kişiselleştirme/zamana göre değişir; başka bir gün ya da hesapla "ilk proje" farklı olabilir.

## HAKLAR — önemli
Projenin Behance lisans alanı: **`no-use`** (tüm hakları saklı / kullanım izni yok).
Bu dosyalar yalnız **kişisel referans / ilham** amacıyla toplandı. Yeniden yayımlamayın, ticari işte kullanmayın,
kendi işiniz gibi sunmayın. Kullanım gerekiyorsa sahiplerden yazılı izin alın.
Behance kuralları: https://help.behance.net/hc/en-us/articles/204485044-Guide-Copyright-And-Posting-Content-On-Behance

## İçerik (20 proje modülü + kapak → 25 kalem)
| Tür | Adet | Not |
|---|---|---|
| PNG görsel | 14 | en büyük `source` sürümü (çoğu 2800 px genişlik) |
| GIF (animasyonlu) | 6 | `source` sürümü, 8–364 kare |
| Proje kapağı (WebP) | 1 | 808×632, Behance'in sunduğu en büyük kapak |
| Video kapak karesi (JPG) | 2 | Vimeo resmi oEmbed küçük resminin 1920×1080 sürümü |
| **Video (MP4)** | **0 / 2** | **EKSİK** — aşağıya bakın |

Her dosyanın kaynağı, boyut varyantı, bayt sayısı ve SHA-256 değeri `manifest.csv` / `manifest.json` içinde.
`00_onizleme_temas-sayfasi.png` tüm görsellerin numaralı küçük resim sayfasıdır.
Dosya adı biçimi: `<sıra>_modul<Behance modül no>_<tür>.<uzantı>` — sıra projedeki görünüm sırasıdır.

## EKSİKLER (dürüst liste)
| Sıra | Modül | Ne | Neden | Ne yapılabilir |
|---|---|---|---|---|
| 02 | 1 | Vimeo videosu 1154829526 "final", 9 sn, 3840×2160 kaynak | Vimeo oynatıcısı bulut ağından Cloudflare güvenlik doğrulaması ile HTTP 401 verdi (yt-dlp, tarayıcı taklidi ve gerçek Chromium'da aynı; 2026-10-03 ve 2026-10-12'de iki ayrı ortamda denendi) | `videolari-indir.ps1` betiğini kendi bilgisayarınızda çalıştırın |
| 16 | 13 | Vimeo videosu 1154828935 "Scene-1", 20 sn, 2800×1600 kaynak | aynı | aynı |

Bu iki video yerine yalnız kapak kareleri (`03_…`, `17_…`) eklendi; bunlar videonun kendisi değildir.
Behance projede indirilebilir kaynak dosya (`sourceFiles`) yok.
