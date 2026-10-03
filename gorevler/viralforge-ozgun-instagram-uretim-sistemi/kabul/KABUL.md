# Kabul raporu — 2026-10-03 (bulut ortamı, Linux)

Etiketler: **GERÇEK** = gerçek araçla çalıştırıldı · **SENTETİK** = test verisi/test taklidi, gerçek Instagram veya
gerçek Codex çıktısı DEĞİLDİR · **YAPILMADI** = gerekli erişim yok.

| # | Zorunlu kapsam | Durum | Kanıt |
|---|---|---|---|
| 1 | 1000 gerçek başarılı içerikte URL + fotoğraf + ses + açıklama kaydı | Sistem hazır ve doğrulandı; **gerçek 1000 gönderi toplanmadı** | Aşağıda K1–K3 |
| 2 | Her örnekten "neden tuttu" analiziyle 4 özgün varyasyon | Sistem hazır, 1000×4 hacimde mekanik kanıt; **gerçek Codex analizi/görseli yok** | K4–K6 |
| 3 | ComfyUI yerine ChatGPT Pro görsel tercihi | Uygulandı: Codex `$imagegen` (ChatGPT plan limiti) + ChatGPT Pro web sayfası yolu; ComfyUI yok | K5, K7 |

## Çalıştırılmış kanıtlar
- **K1 GERÇEK araç / SENTETİK sayfa** — Yakalama yer imi Playwright + Chromium ile gerçek tarayıcıda çalıştırıldı:
  `vf-capture-TESTCODE1.json` + `.jpg` indirildi → `import-meta` → kayıt 4/4 alan tamam
  (`url_https, photo, audio, caption` = 1). Dosyalar: `yakalama-ornek-sentetik.json`, `yakalama-testi/`.
- **K2 GERÇEK** — 15 birim/uçtan uca test geçti (`birim-testler.txt`): DYI ZIP (`\/` kaçışlı JSON), HTML, yt-dlp
  info.json, gallery-dl yan dosyası, og:description ayrıştırma, seçim, şema doğrulama, kopya/benzer görsel tespiti,
  ChatGPT web sayfası + içe alma, CLI çıkış kodları.
- **K3 GERÇEK** — `codex-cli 0.160.0` ve `yt-dlp 2026.08.19` kuruldu; kullanılan tüm bayraklar `--help` ile doğrulandı.
- **K4 GERÇEK Codex çağrısı** — `analyze` gerçek Codex'i `--image --output-schema -o` ile çağırdı; Codex bayrakları
  kabul edip OpenAI'ye ulaştı, giriş olmadığından 401 döndü; sistem ilk hatada durup çözümü yazdı
  (`gercek-codex-giris-yok.txt`).
- **K5 SENTETİK ölçek** — `olcek_kabul.py`: 1000 sentetik gönderi → 1000 analiz → üretimde 1500. çağrıda "usage limit"
  simülasyonu → durdu → aynı komutla devam → **4000/4000 PNG doğrulandı**, `bitti-mi` = "Evet, bitti" (çıkış 0),
  104 sn (`olcek-kabul-cikti.json`). Görsel kontrol: `olcek-kontak-sentetik.png` (kaynak + 4 farklı varyasyon; soyut
  test görselleri, gerçek üretim değildir).
- **K6 GERÇEK** — Ücret koruması: ortamda `OPENAI_API_KEY` varken Codex alt sürecine aktarılmadığı test edildi.
- **K7** — `chatgpt-sheet` istem sayfası üretip `KOD_vN.png` içe alımı test edildi.

## YAPILMADI (somut engel → sizin yapacağınız tek adım)
1. **ChatGPT Pro girişi**: `codex login` → "Sign in with ChatGPT". Parola/oturum buluta verilmedi ve istenmedi.
2. **1000 gerçek gönderi**: Instagram hesabınızdan "Bilgilerini indir" (JSON) + yakalama yer imi. Bulut ortamı
   hesabınıza erişemez; Instagram Koşulları izinsiz otomatik toplamayı yasaklar, bu yüzden toplu kazıma yapılmadı.
3. **ChatGPT Pro danışması**: `danisma/chatgpt-pro-danisma.md` hazır; sizin oturumunuzda çalışır.
4. **Windows/telefon testi**: `kur-windows.ps1` Windows'ta çalıştırılmadı.
5. **4000 gerçek görsel**: Plan limitine bağlı; 1–2 günde değil, limit sıfırlamalarıyla birkaç günde tamamlanabilir.
   Sistem her durakta kaldığı yerden devam eder.

Bu adımlar sonrası tek komut: `python -m viralforge -w calisma run --target 1000` → `bitti-mi`.
