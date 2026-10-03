# Kaynaklar ve doğrulama durumu

Erişim tarihi: 2026-10-03. "Okundu" = sayfa WebFetch ile açılıp ilgili cümleler
alındı. "Arama özeti" = sayfa açılamadı, yalnız arama motoru özeti kullanıldı.

## Resmi teknik kaynaklar

| # | Kaynak | Durum | Kullanılan bilgi |
|---|---|---|---|
| R1 | Google Drive Yardım — Stream & mirror files with Drive for desktop: https://support.google.com/drive/answer/13401938?hl=en | Okundu | Akış: bulutta, yalnız açılan/sık kullanılan yerelde; "Files can only be accessed when Drive for desktop is running"; yansıtma tam yerel kopya |
| R2 | Google Drive Yardım — Manage Google Drive for desktop: Advanced guide: https://support.google.com/drive/answer/16631477?hl=en | Okundu | Sürücü harfi değiştirilebilir; "Unsynced changes are stored in a local cache…can be lost"; macOS File Provider önbelleği harici diske taşınamaz; 84.x/85.0.13.0 kurtarma |
| R3 | Google Workspace — Advanced Drive for desktop configuration: https://knowledge.workspace.google.com/admin/drive/advanced-drive-for-desktop-configuration | Okundu | `ContentCachePath` (varsayılan `%LOCALAPPDATA%\Google\DriveFS`), `ContentCacheMaxKbytes` (%20 sınırı, yönetici), `MinFreeDiskSpaceKBytes`, `DefaultMountPoint`, kayıt defteri yolları; "files are moved here before they're uploaded" |
| R4 | Google Drive Yardım — Customize Drive for desktop settings: https://support.google.com/drive/answer/13470231?hl=en | Okundu (2026-10-03) | Akış/yansıtma geçişi, senkron duraklatma vb. **Alt klasör, dosya türü veya desen hariç tutma ayarı sayfada geçmiyor.** Bu bir yokluk gözlemidir, "özellik yok" belgesi değildir |
| R5 | Claude Yardım — Google Workspace connectors (Using the Google Drive integration): https://support.claude.com/en/articles/10166901-using-the-google-drive-integration | Okundu ("Last updated: yesterday") | Drive: Docs, Sheets, Slides, PDF, görsel, Office; "Claude mirrors your existing permissions"; yalnız metin çıkarır; "+ → Add from Google Drive" |
| R6 | OpenAI Yardım — Google Drive app in ChatGPT: https://help.openai.com/articles/10929079 | **403 — Arama özeti** | Drive uygulaması hesabın erişebildiği dosyalara bağlanır; Library'de My Drive + seninle paylaşılanlar, Shared drives yok; @mention ile ekleme |
| R7 | Gemini Yardım — Upload & analyze files in Gemini Apps: https://support.google.com/gemini/answer/14903178?hl=en | Okundu | Add files/Upload; Add from Drive için Workspace bağlantısı + Keep Activity; 10 dosya/istem; video ≤2 GB ve toplam 5 dk; diğer ≤100 MB; kod klasörü ≤5.000 dosya/100 MB |
| R8 | Google Drive Yardım — Gemini in Drive: https://support.google.com/drive/answer/16684520 | Arama özeti | drive.google.com'da "Ask Gemini"; uygun abonelik gerekir |
| R9 | Claude Code genel bakış: https://code.claude.com/docs/en/overview | Okundu | Claude Code kod tabanını okur/düzenler; Desktop scheduled tasks "direct access to your local files"; Web oturumu yerel olmayan depolarla çalışır |
| R10 | Gradle — Directory layout: https://docs.gradle.org/current/userguide/directory_layout.html | Okundu | GRADLE_USER_HOME varsayılan `C:\Users\<ad>\.gradle`; caches, daemon, wrapper dists; proje `.gradle/` ve `build/` |
| R12 | Google Drive Yardım — Use Google Drive for desktop: https://support.google.com/drive/answer/10838124 | Okundu (2026-10-03) | "Your computer space and Google storage are not the same"; alan göstergesi diski gösterir, hesap kotasını değil; "Streaming a file uses almost no computer space"; paylaşılan dosyalar diski doldurabilir ama kotaya sayılmaz; Docs/Sheets/Slides tarayıcıda açılır |
| R11 | npm — folders: https://docs.npmjs.com/cli/v11/configuring-npm/folders | Okundu | Yerel `./node_modules`; önbellek Windows'ta `%LocalAppData%/npm-cache` |

## Topluluk / kullanıcı deneyimi

| # | Kaynak | Durum | Kullanılan bilgi |
|---|---|---|---|
| T1 | Google Developer forumu — Ability to exclude specific subfolders from Google Drive for Desktop sync: https://discuss.google.dev/t/ability-to-exclude-specific-subfolders-from-google-drive-for-desktop-sync/257296 | Okundu | 2025-08'den 2026-09'a kadar özellik isteği; `.driveignore` önerisi; threadde Google'dan resmi yanıt görünmüyor. Yanıt yokluğu "özellik yok" kanıtı değildir; yalnız talebin sürdüğünü gösterir |
| T2 | Gradle forumu — Using Gradle in a computer cluster…: https://discuss.gradle.org/t/using-gradle-in-a-computer-cluster-causes-daemons-to-lock-each-other-out/46064 | Okundu | Björn Kautler: "I don't think it is a good idea to have the GRADLE_USER_HOME on a network drive…" (2023). Topluluk uzmanı görüşüdür, Gradle'ın resmi "unsupported" belgesi değildir; senaryo ağ diskini birden çok makinenin paylaşmasıdır |
| T3 | odrive forumu — Git with odrive: https://forum.odrive.com/t/git-with-odrive/1471 | Arama özeti | Senkron aracının `.git/refs` içine dosya eklemesi depoyu bozar |
| T4 | Adobe topluluğu — Google Drive for desktop or local disk: https://community.adobe.com/t5/premiere-pro-discussions/google-drive-for-desktop-or-local-disk/m-p/15537350/highlight/true | Arama özeti | Düzenleme sırasında medya yerel diskte olmalı; bulut sürücüler büyük video için uygun değil |
| T5 | Adobe topluluğu — "Google Drive for desktop": https://community.adobe.com/t5/premiere-pro-discussions/quot-google-drive-for-desktop-quot/m-p/12574594 | Arama özeti | İlk proje yüklemesi yavaş, sonra kabul edilebilir |
| T6 | Engadget — Drive for desktop recovery tool: https://www.engadget.com/updated-google-drive-for-desktop-offers-a-recovery-tool-for-missing-files-042758933.html | Arama özeti | 2023 v84 senkron hatası; yüklenmemiş yerel değişiklikler kayboldu (R2 ile doğrulandı) |

## Hazır araçlar (karşılaştırma için; test edilmedi)

| Araç | Bağlantı | Ne sağlar |
|---|---|---|
| Insync ignore rules | https://www.insynchq.com/i/features/ignore-rules | .gitignore benzeri hariç tutma |
| Better-Drive | https://pkg.go.dev/github.com/n24q02m/better-drive@v1.2.0 | `.driveignore` dosyası |
| odrive | https://forum.odrive.com/t/syncing-google-team-drives-and-file-type-exclusions/6403 | Uzantı kara listesi |

**Seçim gerekçesi:** Kullanıcının mevcut kurulumu (Drive for desktop + G:) korunur;
yeni senkron aracı dayatılmaz. Aktif projeyi yerelde tutup Drive'a bundle/ZIP koymak
ek yazılım gerektirmez ve kabul/03b'de uçtan uca doğrulandı. Denetim aracı yalnız
Python standart kütüphanesi kullanır.

## Kaynak metindeki (kaynak.txt) iddiaların karşılaştırması

| Kaynak iddiası | Sonuç |
|---|---|
| "yer SSD'den değil Drive'daki 5 TB'dan düşer" | **Kısmen.** Akış modunda dosyalar bulutta tutulur ve Google kotasına sayılır. Yerel disk ayrıca önbellek için kullanılır; ikisi ayrı ölçülerdir (R12). Yansıtma modunda tam yerel kopya olur. 5 TB doğrulanmadı. |
| "SSD dolmaz (sadece cache + çevrimdışı)" | **Garanti değil.** Önbellek, çevrimdışı dosyalar, yüklenmeyi bekleyen dosyalar ve senkronize edilen paylaşılan dosyalar yerel diski doldurabilir (R1, R3, R12). |
| "Drive uygulaması açık, internet var" şartı | **Doğru** (R1: yalnız uygulama çalışırken erişilir). Çevrimdışı işaretli dosyalar internetsiz açılır. |
| "video, büyük zip, rastgele okuma takılır" | Topluluk deneyimi **destekliyor** (T4, T5). Bu çalışmada Windows'ta ölçülmedi. |
| "Web AI'ya G:\ yazmak yetmez" | **Doğru**, ama mutlak değil: erişim mount, izin/sandbox, yükleme, bağlayıcı ve köprü koşullarına bağlıdır. kabul/02 yalnız bu bulut konteynerini kapsar. |
| "paylaşım linki ver" | **Koşullu.** Servis linki açabilmeli ve link herkese açık olmalı. |
| "servisin Drive bağlayıcısı varsa" | **Doğru.** Claude (R5), ChatGPT (R6) ve Gemini (R7) bağlayıcı sunuyor; izin modeli OAuth, yol değil. Gerçek hesapla test edilmedi. |
