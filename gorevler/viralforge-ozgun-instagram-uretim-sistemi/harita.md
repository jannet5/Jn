# ViralForge — bağlı uygulama haritası

Tarih: 2026-10-03. Bu harita araştırma → karar → uygulama → test → teslim zincirini bağlar.
Her düğümün altındaki "kanıt" satırı, teslimdeki gerçek dosyaya/komuta işaret eder.

```
HEDEF: Codex beyin → 1000 tutmuş IG gönderisi (https URL + foto + ses + açıklama)
        → her biri için görsel+ses+açıklama birlikte "neden tuttu" → 4 farklı özgün görsel → 4000
        → "bitti mi?" sorusuna sayıyla cevap
   │
   ├─ B1 Toplama (Instagram) ─────── A / B / C yolları ──► importers.py, capture/, fetch.py
   ├─ B2 Beyin (Codex)       ─────── A / B / C yolları ──► brain.py, schemas/, AGENTS.template.md
   ├─ B3 Görsel üretim (ChatGPT Pro) A / B / C yolları ──► brain.generate_*, report.chatgpt_sheet
   ├─ B4 Doğrulama + "bitti mi"  ───────────────────────► verify.py (dHash, boyut, kopya, sayım)
   ├─ B6 Dış komut başlatma (Windows/POSIX) A / B / C ──► launcher.py (brain, fetch, doctor ortak)
   └─ B5 Kalıcı teslim ─────────────────────────────────► git dalı + özel ZIP + SHA-256 + geri okuma
```

## B1 — Toplama: 1000 gönderi, her biri URL + foto + ses + açıklama

| Yol | Ne | Artı | Eksi / risk | Karar |
|---|---|---|---|---|
| **A** Instagram "Bilgilerini indir" (JSON) + bağlantı listesi → `import-urls`; eksik alanlar için kullanıcının KENDİ tarayıcısında tek tık `capture/` yer imi → `import-meta` | Meta'nın resmi kişisel veri dışa aktarımı; yakalama kullanıcı tıklamasıyla, kendi oturumunda | Ücretsiz, hesap riski en düşük, ses etiketi DOM'dan okunur | 1000 gönderi için 1000 tık (gönderi başına ~5 sn ≈ 1,5 saat) | **Varsayılan** |
| **B** `fetch` = yt-dlp + kullanıcının cookies.txt'si, 25 sn aralık, isteğe bağlı ses indirme + ffmpeg ses özellikleri | Tam otomatik; caption, beğeni, yorum, izlenme, kapak, reel sesi | Instagram Koşulları izinsiz otomatik toplamayı yasaklıyor → hesap kısıtlama riski; 429/401'de durur | Yalnız `--i-accept-instagram-terms-risk` ile |
| **C** Meta Graph API (Hashtag Search `top_media`) | Resmi API | İşletme/Creator hesap + Meta uygulaması + App Review; haftada 30 hashtag; ses alanı yok. Kullanıcı "illa meta gerekli mi?" diye sordu → **gerekli değil** | Kurulmadı; gerekirse `import-meta` JSON'u kabul eder |
| (ek) gallery-dl / Apify / başka dışa aktarıcı JSON'u | Mevcut araç çıktıları | — | Ücretli olabilir (Apify) | `import-meta` uyumlu (gallery-dl yan dosyası test edildi) |

"Tutmuş" seçimi (`select`): takipçi varsa etkileşim oranı, yoksa log ölçekli (beğeni + 3×yorum + 0,05×izlenme);
dört alanı eksik kayıt seçilmez. Ses: kayıt "known" (başlık/sanatçı/orijinal mi) veya "none" (müziksiz foto)
olmalı; "unknown" eksik sayılır — uydurulmaz.

Kanıt: `tests/test_viralforge.py` (DYI ZIP `\/` kaçışlı JSON, HTML, yt-dlp info.json, gallery-dl, yakalama dosyası),
`kabul/yakalama-kabul.md` (Playwright ile gerçek tarayıcıda yer imi → 2 dosya indirildi → içe alındı, 4 alan tamam).

## B2 — Beyin: Codex

| Yol | Ne | Karar |
|---|---|---|
| **A** `codex exec --image <foto…> --output-schema analysis.schema.json -o analysis.json -` (ChatGPT girişiyle) | Görseli gerçekten görür; ses meta/özellikleri + açıklama + metrikler istemde; çıktı şemaya zorlanır, Python ayrıca doğrular (3+ kanıtlı faktör, v1..v4, istemler arası Jaccard ≤ 0,6), hatalıysa 1 kez düzeltme ister | **Seçildi** |
| **B** Codex etkileşimli oturum + `AGENTS.md` | Kullanıcı Codex'e "devam et / bitti mi?" der; Codex CLI komutlarını çalıştırır | Aynı sisteme bağlı (init AGENTS.md yazar) |
| **C** ChatGPT web'e elle yapıştırma | Codex girişi yoksa | Yalnız üretim için sayfa var (B3-C) |

Ücret koruması: `OPENAI_API_KEY`/`CODEX_API_KEY` alt sürece aktarılmaz → Codex ChatGPT plan limitini kullanır,
API faturası çıkmaz (test: `test_api_key_not_passed`). Codex sesi doğrudan dinlemez; ses adı/sanatçı/orijinal-ses
bilgisi + (B yolunda) süre ve ses yüksekliği sayısal olarak verilir — bu sınır açıkça yazılıdır.

## B3 — Görsel üretim: ChatGPT Pro (ComfyUI değil)

| Yol | Ne | Karar |
|---|---|---|
| **A** `codex exec '$imagegen …'` — Codex'in yerleşik gpt-image-2 aracı, ChatGPT plan limitinden düşer, API anahtarı gerekmez | Tam otomatik; görsel hedef yola kopyalanmazsa `$CODEX_HOME/generated_images` içinden alınır | **Seçildi** (kullanıcının "ChatGPT Pro üretsin, ücretli olmasın" tercihi) |
| **B** `chatgpt-sheet` → ChatGPT Pro web'de kopyala-yapıştır, `KOD_vN.png` indir → `ingest-images` | Codex limiti dolarsa veya kullanıcı web'i tercih ederse | Hazır ve test edildi |
| **C** API (`--allow-api-billing`) | Ücretli | Varsayılan kapalı |

Limit: görsel turları plan limitini 3–5× hızlı tüketir. 4000 görsel tek oturumda bitmeyebilir; komut
"usage limit" görünce durur (çıkış 75), kalanı aynı komutla devam eder (test: 1500'de limit → devam → 4000).

## B4 — Doğrulama ve "bitti mi?"

`verify`: dosya açılıyor mu, kısa kenar ≥ 512 px, 4 varyasyon arası dHash mesafesi ≥ 6, kaynakla mesafe ≥ 10
(kopya şüphesi). `bitti-mi`: 1000 seçili + 1000 geçerli analiz + 1000×4 doğrulanmış görsel → "Evet, bitti"
(çıkış 0); değilse kalan adımı sayıyla söyler (çıkış 1). Bahane değil, sonraki komut.

## B6 — Dış komut başlatıcı (2026-10-04 Windows denetimi sonrası)

Bulgu (ebeveynin native Windows / Python 3.11 / Pillow 11.3.0 denetimi, d08360e): 15 testin 6'sı hatalı.
`VF_CODEX_BIN=tests/fake_codex.py` doğrudan CreateProcess'e verildi → **WinError 193** (Windows shebang okumaz);
CLI alt süreci UTF-8 modunda değildi → **UnicodeDecodeError**. Linux karşılığı d08360e'de yeniden üretildi
(çalıştırma izni olmayan .py → PermissionError; C locale → UnicodeEncodeError) ve düzeltmeden sonra geçti.

| Yol | Ne | Karar |
|---|---|---|
| **A** Tek çözümleyici `launcher.resolve()`: `.py` → `sys.executable`, `.js` → node, npm `codex.cmd` → cmd-shim dosyası okunur, hedef `codex.js` **node ile shell'siz** çalıştırılır; `.exe` doğrudan; Windows'ta uzantısız npm sh betiği yerine yanındaki `.exe/.cmd` | cmd.exe hiç devreye girmez → BatBadBut tipi argüman enjeksiyonu yok; boşluklu yollar liste argümanıyla güvenli | **Seçildi** |
| **B** `shell=True` / Node `{shell:true}` | Python ve Node belgeleri `.bat/.cmd` için bunu önerir ama istem/yol içeriğini shell yorumlar | **Reddedildi** (varsayılan asla değil) |
| **C** cmd-shim çözülemeyen `.cmd`: `"%COMSPEC%" /d /s /c "<her argüman tırnaklı>"` | Tırnak içinde hâlâ yorumlanan `% ! "` ve satır sonu içeren argüman **reddedilir** (LaunchError) | Yalnız yedek |

Ortak kurallar: tüm alt süreç G/Ç'si `encoding="utf-8", errors="replace"`; Python alt süreçlerine `PYTHONUTF8=1`,
`PYTHONIOENCODING=utf-8`; CLI kendi stdout/stderr'ini UTF-8'e ayarlar; başlatma hatası (`FileNotFoundError`,
`PermissionError`, `OSError`/WinError 193) → `LaunchError` → analiz/üretim ilk hatada **"launch"** durumuyla durur;
zaman aşımı → çıkış 124 (kontrollü başarısızlık); giriş yok → `AuthMissing`; kota → `LimitReached` (çıkış 75).
`doctor` aynı çözümleyiciyi kullanır, `codex login status` okur, girişi **başlatmaz**; çıkış 0 hazır / 2 Codex
çalışmıyor / 3 etkileşimli giriş gerekli. `kur-windows.ps1` mevcut Codex'i yeniden kurmaz (`-InstallCodex` yoksa),
her dış komutun çıkış kodunu kontrol eder, `codex login`'i hiç çalıştırmaz.

Kanıt: `tests/test_viralforge.py::TestLauncher` (8 test) + `TestCli` (UTF-8 olmayan locale, doctor 0/2/3),
`tests/fixtures/npm global/codex.cmd` (gerçek cmd-shim 9.0.2 çıktısı), `kabul/windows-regresyon.md`.

## B5 — Teslim

Kod/harita/günlük: `jannet5/Jn` dalı `claude/quirky-cerf-hp56r0`, klasör `gorevler/viralforge-ozgun-instagram-uretim-sistemi/`.
Özel kaynak (kaynak.txt, gorev.md) public depoya konmadı; yalnız özel ZIP içinde. ZIP + SHA-256 + geri okuma: `kabul/teslim-kaniti.md`.

## Kırılma noktaları → alternatif

- Instagram 401/429 (B yolu) → otomatik dur, A yoluna dön (yakalama yer imi).
- Codex girişi yok → `doctor` söyler; analiz yapılamaz, üretim için B3-B sayfası (yine analiz gerekir) → `codex login`.
- Codex plan limiti → bekle + aynı komut; acil ise B3-B.
- `$imagegen` dosyayı kopyalamazsa → generated_images yedeği (paralel üretimde `--jobs 1` önerilir).

## Kaynaklar (erişim 2026-10-03)

Resmî:
- Codex CLI özellikleri (görsel girdi, yerleşik görsel üretim): https://developers.openai.com/codex/cli/features — yönlendirme: https://learn.chatgpt.com/docs/codex/cli.md
- Codex non-interactive (`codex exec`, `--output-schema`, `-o`, `--ephemeral`, sandbox): https://learn.chatgpt.com/docs/non-interactive-mode.md
- Instagram Hashtag Search / top_media (30 hashtag/7 gün, yetki gereksinimleri): https://developers.facebook.com/docs/instagram-platform/instagram-api-with-facebook-login/hashtag-search , https://developers.facebook.com/docs/instagram-platform/instagram-graph-api/reference/ig-hashtag/top-media
- Instagram Audio API: https://developers.facebook.com/documentation/instagram-platform/content-publishing/audio-api
- Instagram scraping/otomatik toplama yardım sayfası: https://help.instagram.com/740480200552298
- Instagram dışa aktarım rehberi: https://takeoutday.org/guides/how-to-export-instagram-data
- Yerelde doğrulandı: `codex-cli 0.160.0` → `exec --image`, `--output-schema`, `-o`, `--ephemeral`, `-C`, `--skip-git-repo-check`; `yt-dlp 2026.08.19` → `-J`, `--ignore-no-formats-error`, `--cookies`.

Windows başlatma / kodlama (B6, erişim 2026-10-04):
- Python subprocess (Windows argüman kuralları, batch dosyalarının sistem kabuğunda açılabileceği uyarısı, `encoding`): https://docs.python.org/3/library/subprocess.html
- Python UTF-8 Mode (PEP 540; `PYTHONUTF8`, Windows ANSI kod sayfası): https://peps.python.org/pep-0540/ ; Python 3.15'te varsayılan UTF-8 (PEP 686) özeti: https://pydevtools.com/blog/python-315-utf8-default/
- BatBadBut / CVE-2024-24576 ve Python'un durumu: https://discuss.python.org/t/is-python-affected-by-cve-2024-24576/50740 , https://github.com/rust-lang/rust/issues/123728
- Node.js CVE-2024-27980 (`.bat/.cmd` shell'siz spawn → EINVAL): https://nodejs.org/en/blog/vulnerability/april-2024-security-releases-2
- npm cmd-shim (`%dp0%` biçimi; yerelde cmd-shim 9.0.2 ile üretildi): https://github.com/pnpm/cmd-shim , https://cdn.jsdelivr.net/npm/npm5v@5.6.1/node_modules/cmd-shim/README.md
- Codex CLI Windows kurulum yolları (npm, winget, install.ps1, WSL): https://itecsonline.com/post/how-to-install-codex-cli-on-windows-2026-guide , https://codex.danielvaughan.com/2026/04/08/installing-codex-cli/
- Codex npm başlatıcısının Windows'ta sh.exe'ye takılması ve Node tabanlı giriş noktasına geçiş: https://upd.dev/openai/codex/issues/14264
- WinError 193 topluluk örnekleri (.py doğrudan Popen): https://issues.apache.org/jira/browse/PROTON-595 , https://internals.rust-lang.org/t/x-py-1-is-not-a-valid-win32-application/11371 , https://github.com/Pymol-Scripts/Pymol-script-repo/issues/130
- PowerShell yerel komut çıktısı kodlaması (`[Console]::OutputEncoding` ≠ `$OutputEncoding`): https://github.com/PowerShell/PowerShell/issues/7233

Topluluk / deneyim:
- Codex CLI görsel iş akışları (`$imagegen`, generated_images, limit tüketimi): https://codex.danielvaughan.com/2026/06/04/codex-cli-visual-workflows-image-input-gpt-image-2-generation-asset-pipelines-v0137/ , https://codex.danielvaughan.com/2026/04/27/codex-cli-image-generation-gpt-image-2-visual-development-workflows/
- codex-imagegen beceri örnekleri: https://skills.sh/nathanonn/agent-skills/codex-imagegen , https://www.skills.sh/giulioco/skills/codex-imagegen
- Codex Pro limit sorunları (GitHub issue aynaları): https://upd.dev/openai/codex/issues/28016 , https://upd.dev/openai/codex/issues/29243
- Görsel üretim limiti deneyimi: https://community.openai.com/t/image-creation-limit-720-hour-wait/1340856
- Instaloader 401 "Please wait a few minutes" deneyimleri: https://kitemetric.com/blogs/troubleshooting-instagram-scraping-with-instaloader , https://forum.freecodecamp.org/t/i-am-unable-to-access-posts-from-public-accounts-on-instagram/752099
- yt-dlp ile reel meta/ses: https://skills.sh/openclaw/skills/instagram-reels
- gallery-dl Instagram çerez/metadata: https://releasebot.io/updates/mikf/gallery-dl
- Benzer sistemler (viral içerik → AI analiz → yeni görsel): https://www.neura.market/workflow/automate-instagram-content-creation-ai-trending-posts , https://www.neura.market/workflow/reverse-engineer-viral-tiktok-instagram-reels , https://growwstacks.com/case-studies/apps/automate-instagram-content-creation-from-top-trends-with-ai/
- Instagram scraping hukuki/risk özetleri: https://scrapeops.io/websites/instagram/ , https://opentermsarchive.org/en/memos/instagram-blocks-data-retrieval-and-clarifies-what-information-it-retrieves/

Karşılaştırma sonucu: n8n/Make şablonları ücretli API (OpenAI/Flux/Gemini) + Apify kullanıyor; kullanıcı
"ücretli olmasın, Codex beyin, ChatGPT Pro üretsin" dediği için bunlar yerine Codex CLI (ChatGPT girişi) +
`$imagegen` seçildi. Onların "görsel+metin birlikte analiz → üretim manifesti" fikri şema olarak alındı.
