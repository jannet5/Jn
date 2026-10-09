# Codex: bu klasörü kur ve çalıştır

Sen bu sistemin beynisin. Kullanıcıyla Türkçe ve kısa konuş. Sırayla:

1. Bu klasörde PowerShell aç ve çalıştır:
   `powershell -NoProfile -ExecutionPolicy Bypass -File .\kur-windows.ps1`
   - Python yoksa: kullanıcıya https://www.python.org/downloads/ (3.10+, "Add to PATH") kurmasını söyle.
   - Codex bulunamazsa: `kur-windows.ps1 -InstallCodex` (Node.js 18+ gerekir).
   - Çıkış 3 = giriş gerekli: kullanıcıdan KENDİSİNİN `codex login` yazıp ChatGPT ile girmesini iste.
     Şifre isteme, girişi sen yapmaya çalışma.
2. Kullanıcıdan Instagram > Ayarlar > "Bilgilerini indir" (JSON) ZIP'ini İndirilenler klasörüne koymasını iste.
   Eksik foto/açıklama/şarkı için `capture/bookmarklet.txt` yer imini gönderi sayfalarında kullanabilir.
3. Çalıştır: `powershell -NoProfile -ExecutionPolicy Bypass -File .\uret-windows.ps1`
   - Çıkış 0: "Evet, bitti" → rapor: `calisma\rapor.html`
   - Çıkış 75: ChatGPT limiti doldu → limit sıfırlanınca aynı komutu tekrar çalıştır.
   - Çıkış 1: devam gerekiyor → `python -m viralforge -w calisma status` çıktısına bakıp eksik adımı söyle.
4. Kullanıcı "bitti mi?" derse: `.venv\Scripts\python.exe -m viralforge -w calisma bitti-mi` çıktısını aynen ver.

Kurallar: kaynak gönderileri kopyalama; ücretli API anahtarı kullanma; bilinmeyen bilgiyi uydurma.
Ayrıntı: README.md, harita.md, viralforge/AGENTS.template.md.
