# ViralForge — Codex beyinli özgün Instagram üretim sistemi

Tutmuş Instagram gönderilerini (https URL + fotoğraf + arka ses + açıklama) toplar, Codex'in her gönderiye
bakıp görsel + ses + açıklamayı **birlikte** değerlendirerek "neden tuttu" analizini yapmasını sağlar,
o mantıkla **birbirinden ve kaynaktan farklı 4 özgün görsel** ürettirir (ChatGPT Pro planı, `$imagegen`),
hepsini dosya düzeyinde doğrular ve "bitti mi?" sorusuna sayıyla cevap verir.

Bağlı harita, A/B/C yolları ve kaynaklar: [harita.md](harita.md) · Kabul kanıtları: [kabul/](kabul/) ·
Çalışma günlüğü: [calisma-gunlugu.md](calisma-gunlugu.md)

## Gerekenler (hepsi ücretsiz araç; ücret yalnız mevcut ChatGPT Pro aboneliği)
- Python 3.10+ ve `pip install -r requirements.txt` (Pillow; isteğe bağlı yt-dlp)
- Node 18+ ve `npm i -g @openai/codex`, sonra **`codex login`** → "Sign in with ChatGPT" (Pro hesabınız)
- Instagram hesabınıza tarayıcıdan giriş

Windows (PowerShell): `powershell -NoProfile -ExecutionPolicy Bypass -File .\kur-windows.ps1`
— mevcut Codex'i yeniden kurmaz (`-CodexPath <codex.cmd|codex.exe>` ile yol verilebilir, `-InstallCodex` yalnız
istenirse kurar), `codex login`'i kendisi çalıştırmaz; çıkış 0 hazır / 3 giriş gerekli / 2 Codex çalışmıyor.
Kurulum ve `codex login` sonrası tek komut: `powershell -NoProfile -ExecutionPolicy Bypass -File .\uret-windows.ps1`
— İndirilenler'deki `instagram*.zip` / `vf-capture-*.json` dosyalarını alır, üretir, doğrular, `bitti-mi` sonucunu
verir; limit dolarsa aynı komutla devam (çıkış 0 bitti / 1 devam / 3 giriş gerekli / 75 limit).
Codex yolu `VF_CODEX_BIN` ile de verilebilir (boşluklu tam yol olabilir; `.exe`, npm `.cmd`, `.js`, `.py` desteklenir).

## Kullanım (Codex'e de aynısını söyleyebilirsiniz)
```bash
python -m viralforge -w calisma init            # çalışma alanı + Codex için AGENTS.md
python -m viralforge -w calisma doctor          # codex girişi, API anahtarı koruması, araçlar

# 1) Gönderi bağlantıları: Instagram > Ayarlar > Bilgilerini indir (JSON) ZIP'i, veya herhangi bir liste/HTML
python -m viralforge -w calisma import-urls instagram-export.zip linkler.txt

# 2) Foto + açıklama + ses: gönderi sayfasında capture/bookmarklet.txt yer imine tıklayın
#    (her tık vf-capture-KOD.json + .jpg indirir), sonra:
python -m viralforge -w calisma import-meta ~/Downloads
#    (İsteğe bağlı, hesap riski kabul edilirse: fetch --cookies cookies.txt --audio --i-accept-instagram-terms-risk)

# 3) Seç → analiz → üret → doğrula → rapor → bitti mi (kaldığı yerden devam eder)
python -m viralforge -w calisma run --target 1000
python -m viralforge -w calisma bitti-mi
```
Tek tek adımlar: `select --top 1000`, `analyze --jobs 2`, `generate`, `verify`, `report`, `status`.

ChatGPT Pro web arayüzüyle üretmek isterseniz (Codex limiti dolduysa vb.): `chatgpt-sheet` → açılan
`chatgpt-pro-istemleri.html`'deki istemleri ChatGPT'ye yapıştırın, görselleri `KOD_v1.png` … adıyla indirin →
`ingest-images <klasör>`.

## Neler garanti, neler değil
- Kayıt eksiksizliği: URL https, ≥1 fotoğraf, ses kaydı ("known" veya müziksiz foto için "none"), açıklama.
  Bilinmeyen alan uydurulmaz; eksik kayıt seçilmez.
- Codex sesi dinlemez; ses adı/sanatçı/orijinal ses bilgisi ve (fetch --audio ile) süre/ses yüksekliği verilir.
- Plan limiti: görsel üretim limiti hızlı tüketir; komut durur (çıkış 75), sonra aynı komutla devam eder.
- Özgünlük: istemler kişi/logo/metin/birebir kompozisyon kopyalamayı yasaklar; `verify` kaynakla görsel
  benzerliği (dHash) ölçüp kopya şüphelisini reddeder.
- Instagram Kullanım Koşulları izinsiz otomatik toplamayı yasaklar; bu yüzden varsayılan yol resmi dışa aktarım +
  kullanıcı tıklamalı yakalamadır.

## Test
```bash
python -m unittest discover -s tests -v       # 24 test, çevrimdışı (tests/fake_codex.py = test taklidi)
python kabul/olcek_kabul.py /tmp/olcek        # 1000 sentetik gönderi → 4000 görsel, limit + devam
```
