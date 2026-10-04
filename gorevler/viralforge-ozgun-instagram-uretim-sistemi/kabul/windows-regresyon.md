# Windows denetim bulgusu → düzeltme → regresyon (2026-10-04)

## Bulgu (ebeveyn, native Windows, Python 3.11, Pillow 11.3.0, commit d08360e)
`python -X utf8 -m unittest discover -s tests -v` → 15 testin 9'u geçti, 6'sı hata:
- 5 testte `OSError: [WinError 193]` — `brain.analyze_all → analyze_one → _run → subprocess.run → CreateProcess`;
  `VF_CODEX_BIN=tests/fake_codex.py` doğrudan çalıştırılabilir sanıldı.
- CLI testinde `UnicodeDecodeError: 'utf-8' codec can't decode byte 0xe7` — alt Python süreci UTF-8 modunu devralmadı.

## Kök neden
1. Komut çözümleyici yoktu: yol olduğu gibi CreateProcess'e veriliyordu (.py/.js/.cmd ayrımı yok).
2. Alt süreç metin G/Ç'si locale kodlamasına bırakılmıştı (`text=True`, `encoding` yok); CLI stdout'u konsol kod
   sayfasıyla yazıyordu.

## Linux'ta yeniden üretim (gerçek Windows değildir) — `kabul/windows_regresyon_repro.py`
Aynı betik eski ve yeni koda karşı (çalıştırma izni olmayan, `Program Files/fake codex/` boşluklu yolda .py taklidi;
`LC_ALL=C PYTHONUTF8=0 PYTHONCOERCECLOCALE=0`):

| | d08360e (eski) | düzeltme |
|---|---|---|
| analyze | `PermissionError [Errno 13]` (WinError 193'ün POSIX karşılığı) | `ok: 2` |
| CLI `bitti-mi` | `UnicodeEncodeError: 'ascii' codec can't encode character '\xfc'`, stdout boş | `Henüz değil — …` UTF-8 |

## Düzeltme
`viralforge/launcher.py` (tek çözümleyici; `brain`, `fetch`, `doctor` ortak): `.py` → `sys.executable`;
`.js` → node; npm `codex.cmd` → cmd-shim hedefi `codex.js` node ile shell'siz; Windows'ta uzantısız npm sh betiği
yerine yanındaki `.exe/.cmd`; çözülemeyen `.cmd` için yalnız `% ! "`/satır sonu içermeyen, her argümanı tırnaklı
`cmd.exe /d /s /c` yedeği; `shell=True` yok. G/Ç her yerde UTF-8; CLI stdout/stderr UTF-8; başlatma hatası,
zaman aşımı (124), giriş yok (`AuthMissing`), kota (`LimitReached`) ayrı ve kontrollü.

## Linux kontrolleri (bu oturumda çalıştırıldı)
- `python3 -m unittest discover -s tests -v` → 24/24 OK (`birim-testler.txt`). Yeni: `TestLauncher` 8 test, `TestCli` 2 test.
- Gerçek cmd-shim 9.0.2 ile üretilmiş `tests/fixtures/npm global/codex.cmd` → node ile `codex.js`; `a&b|c<d>e "q" %PATH% !x! ^z`
  ve `Program Files/görsel (1).png` argümanları ile Türkçe stdin birebir ulaştı (shell yorumlaması yok).
- `kur-windows.ps1`: PowerShell 7.4.6 (Linux) ayrıştırıcı 0 hata; Linux'ta sahte `.venv\Scripts\python.exe` ile
  akış: mevcut gerçek Codex 0.160 yeniden kurulmadan bulundu → `doctor` → "Not logged in" → çıkış **3**, giriş başlatılmadı.
- Ölçek (sentetik, test taklidi): `olcek-kabul-cikti.json`.

## YAPILMADI: native Windows çalıştırması
Bu bulut oturumunda Windows yok. Ebeveynin Windows'ta tekrar çalıştıracağı komutlar (depo kökünde, PowerShell):
```powershell
git fetch origin claude/quirky-cerf-hp56r0; git checkout <YENİ_COMMIT>
cd gorevler\viralforge-ozgun-instagram-uretim-sistemi
python -X utf8 -m unittest discover -s tests -v
python -m unittest discover -s tests -v          # UTF-8 modu OLMADAN da geçmeli
powershell -NoProfile -ExecutionPolicy Bypass -File .\kur-windows.ps1   # çıkış 0 hazır / 3 giriş gerekli / 2 Codex yok
```
`node` yoksa npm-shim testleri "node yok" diye atlanır (skip), hata vermez.
