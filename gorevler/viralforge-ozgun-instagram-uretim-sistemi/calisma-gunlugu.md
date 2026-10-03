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
