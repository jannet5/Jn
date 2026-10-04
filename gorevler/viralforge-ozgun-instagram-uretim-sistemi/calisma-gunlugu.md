# Çalışma günlüğü — ViralForge

Ortam: Claude Code bulut oturumu (Linux), 2026-10-03. Depo `jannet5/Jn`, dal `claude/quirky-cerf-hp56r0`,
yalnız `gorevler/viralforge-ozgun-instagram-uretim-sistemi/` altında çalışıldı; mevcut projelere dokunulmadı.

## 1. Ne istendi (özet — tam kaynak public depoya konmadı)
Codex'in beyin olduğu bir sistem: Instagram'da tutmuş 1000 gönderiyi https bağlantısı, fotoğrafı, arka sesi ve
açıklamasıyla kaydetmek; her birinin neden tuttuğunu bu üçünü birlikte değerlendirerek anlamak; o mantıkla
birbirinden farklı 4 görsel (toplam 4000) üretmek; görseli ChatGPT Pro üretsin, ücretli olmasın; Meta API şart
mı sorusu; "bitti mi?" sorusuna net cevap. Özel kaynak dosyaları yalnız özel çalışma alanında tutuldu.

## 2. Araştırma (WebSearch/WebFetch) ve bulgular
- Codex CLI: `codex exec` betikten çalışır; `--image` görsel ekler, `--output-schema` JSON şemasına zorlar,
  `-o` son mesajı dosyaya yazar. Yerleşik görsel üretim (`$imagegen`, gpt-image-2) API anahtarı olmadan ChatGPT
  plan limitinden düşer, limitleri 3–5× hızlı tüketir, çıktılar `~/.codex/generated_images/`.
  → Kullanıcının "ChatGPT Pro üretsin, ücretli olmasın" isteği doğrudan karşılanabiliyor.
- Instagram Koşulları ve Meta Otomatik Veri Toplama Koşulları izinsiz otomatik toplamayı yasaklıyor; Instaloader
  anonim erişimde 401 "Please wait a few minutes" veriyor (topluluk deneyimleri). Graph API Hashtag Search
  resmi ama işletme hesabı + uygulama incelemesi + 30 hashtag/hafta sınırı istiyor, ses vermiyor.
  → Meta API gerekli değil; varsayılan yol resmi "Bilgilerini indir" + kullanıcı tıklamalı tarayıcı yakalama.
- Benzer sistemler (n8n/Make şablonları) ücretli API + Apify kullanıyor; "görsel+metin birlikte analiz → üretim
  manifesti" fikri alındı, ücretli parçalar alınmadı. Tüm bağlantılar `harita.md` içinde.

## 3. Kararlar
- Python standart kitaplık + Pillow; sunucu/veritabanı yok, her gönderi bir klasör (Codex dosyaları okuyabilsin).
- Beyin: `codex exec` (analiz) + `AGENTS.md` (etkileşimli Codex için yol tarifi). Üretim: `$imagegen`; yedek
  ChatGPT Pro web istem sayfası. API anahtarları alt sürece aktarılmaz.
- "Bitti mi?" yalnız dosya sayımlarından hesaplanır: 1000 seçili + 1000 geçerli analiz + 4000 doğrulanmış görsel.

## 4. Kullanılan gerçek araçlar ve komutlar
- `pip install yt-dlp instaloader pillow imagehash`, `npm i -g @openai/codex` → `codex-cli 0.160.0`,
  `yt-dlp 2026.08.19`; `codex exec --help`, `codex login status` ("Not logged in").
- `python3 -m unittest discover -s tests -v` (15 test), `python3 kabul/olcek_kabul.py <dizin>`,
  Playwright (Node, önceden kurulu Chromium) ile `kabul/yakalama-testi/run.js`.

## 5. Sorunlar ve çözümler
- DYI testinde çift kaçış hatası → test gerçek `\/` biçimine düzeltildi.
- Ölçek testinde limit simülasyonu tetiklenmedi: test taklidinin sayacı paralel yazımda bozuluyordu → `O_APPEND`
  ile atomik sayaç; yeniden koşuda 1500'de durdu, devamla 4000'e tamamlandı.
- Gerçek Codex girişsiz 401 döndü; sistem 1000 kez denemesin diye `AuthMissing` ile ilk hatada durma eklendi
  ve test edildi.
- gallery-dl'in `foto.jpg.json` yan dosya biçimi eşleşmiyordu → içe alıcı genişletildi, test eklendi.
- Pillow `getdata` uyarısı → `tobytes`.

## 6. Doğrulamalar
`kabul/KABUL.md`: 15/15 test, gerçek tarayıcıda yakalama → içe alma, gerçek Codex bayrak kabulü (401'e kadar),
1000→4000 sentetik ölçek + limit/devam, görsel kontak sayfası gözle kontrol edildi (4 varyasyon farklı).

## 7. Kalan engeller (uydurulmadı)
ChatGPT Pro girişi (`codex login`), 1000 gerçek gönderinin kullanıcı hesabından toplanması, ChatGPT Pro danışması,
Windows testi, plan limitine bağlı 4000 gerçek görsel süresi. Ayrıntı ve tek komutlar `kabul/KABUL.md`'de.

## 8. Teslim
Dal push + geri okuma; özel ZIP (kaynak dosyaları dahil) + SHA-256 + geri okuma: `kabul/teslim-kaniti.md`.

## 9. Takip — Windows denetim hatası (2026-10-04)
- İstenen: ebeveynin native Windows denetiminde bulduğu WinError 193 (5 test) ve CLI UnicodeDecodeError'ı (1 test)
  kök nedeninden gidermek; test taklidini aktif yorumlayıcıyla, gerçek Codex'i `.cmd/.exe` ve boşluklu yollarla
  desteklemek; shell=True kullanmamak; doctor'ı aynı çözümleyiciye bağlamak; Windows kurulumunu mevcut Codex'i
  kullanacak, dış komut hatalarını yakalayacak ve girişi zorlamayacak hâle getirmek.
- Araştırma: Python subprocess belgesi (batch dosyaları sistem kabuğunda açılabilir), BatBadBut/CVE-2024-24576,
  Node CVE-2024-27980, npm cmd-shim biçimi, PEP 540 UTF-8 modu, Codex Windows kurulum yolları, Codex npm başlatıcı
  sorunu, WinError 193 topluluk örnekleri, PowerShell çıktı kodlaması. Bağlantılar `harita.md` B6.
- Komutlar: `git worktree add … d08360e` (eski sürüm), `kabul/windows_regresyon_repro.py` (eski: PermissionError +
  UnicodeEncodeError; yeni: geçti), `npm i cmd-shim` (9.0.2) ile gerçek `codex.cmd` fikstürü, PowerShell 7.4.6
  tarball'ı ile `kur-windows.ps1` ayrıştırma ve akış denemesi, `python3 -m unittest discover -s tests -v` (24/24).
- Kararlar: cmd-shim okunup node ile shell'siz başlatma (A); shell=True reddedildi (B); cmd.exe yalnız katı
  tırnaklı yedek (C). `.ps1` UTF-8 BOM + CRLF (Windows PowerShell 5.1 BOM'suz dosyayı ANSI okur); `.gitattributes`.
- Sorun/çözüm: npm'in uzantısız `codex` sh betiği bazı Python sürümlerinde `which` sonucu olabilir → Windows'ta
  yanındaki `.exe/.cmd` tercih edilir (test edildi).
- Dürüst sınır: native Windows çalıştırılmadı; gerçek 1000 gönderi / 4000 görsel hâlâ yok (ChatGPT Pro girişi ve
  Instagram verisi gerekiyor). Sentetik ölçek testi bunların yerine geçmez.

## 10. Ebeveyn native Windows kabulü (2026-10-04) — yalnız belgelendirme
- Bildirilen: commit `c5b404e…` 41 dosya Git blob SHA-1 + SHA-256 ile geri okundu; gerçek Windows, Python 3.11,
  Pillow 11.3, `python -X utf8 -m unittest discover -s tests -v` → 24/24, 19,783 sn, çıkış 0; WinError 193 ve
  cp1254 hataları kapandı.
- Bu dilimde kod değişmedi; testler ve ölçek istenmediği için yeniden çalıştırılmadı. Kayıt: `kabul/KABUL.md` (K9),
  `kabul/windows-regresyon.md`.
- Açık kalanlar: `kur-windows.ps1` ve `-X utf8`'siz koşu Windows'ta çalıştırılmadı; ChatGPT Pro girişi, 1000 gerçek
  Instagram kaynağı ve 4000 gerçek görsel yok. Sentetik 4000 sonucu gerçek üretim sayılmaz.
