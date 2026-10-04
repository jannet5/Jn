<#
ViralForge Windows kurulumu / ön kontrolü (PowerShell 5.1+ veya 7+).

  powershell -NoProfile -ExecutionPolicy Bypass -File .\kur-windows.ps1
  powershell -NoProfile -ExecutionPolicy Bypass -File .\kur-windows.ps1 -CodexPath "C:\Users\Ad\AppData\Roaming\npm\codex.cmd"
  powershell -NoProfile -ExecutionPolicy Bypass -File .\kur-windows.ps1 -InstallCodex   # Codex yoksa ve kurmak istiyorsanız

- Mevcut Codex'i (PATH'teki codex.cmd / codex.exe veya -CodexPath) YENİDEN KURMADAN kullanır.
- npm ile kurulum yalnız -InstallCodex verilirse yapılır.
- `codex login` ASLA otomatik çalıştırılmaz; giriş gerekiyorsa ayrı ve açık bir mesajla söylenir.
- Her dış komutun çıkış kodu kontrol edilir; hata olursa hangi adımın neden başarısız olduğu yazılır.
Çıkış kodu: 0 hazır · 2 Codex çalıştırılamıyor · 3 etkileşimli giriş gerekli · 1 kurulum hatası
#>
[CmdletBinding()]
param(
    [string]$CodexPath = "",
    [switch]$InstallCodex,
    [string]$Workspace = "calisma"
)
$ErrorActionPreference = "Stop"
Set-Location -LiteralPath $PSScriptRoot
# ViralForge çıktısı UTF-8'dir; PowerShell yerel komut çıktısını bu kodlamayla çözer.
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8
$env:PYTHONUTF8 = "1"

function Fail([string]$msg) { Write-Host "HATA: $msg" -ForegroundColor Red; exit 1 }

function Invoke-Checked([string]$Step, [string]$Exe, [string[]]$Arguments) {
    Write-Host "-> $Step" -ForegroundColor Cyan
    try { & $Exe @Arguments } catch { Fail "$Step başlatılamadı: $($_.Exception.Message)" }
    if ($LASTEXITCODE -ne 0) { Fail "$Step başarısız (çıkış kodu $LASTEXITCODE): $Exe $($Arguments -join ' ')" }
}

# 1) Python 3.10+ (önce py başlatıcısı, sonra python)
$py = $null; $pyArgs = @()
if (Get-Command py -ErrorAction SilentlyContinue) { $py = (Get-Command py).Source; $pyArgs = @("-3") }
elseif (Get-Command python -ErrorAction SilentlyContinue) { $py = (Get-Command python).Source }
else { Fail "Python bulunamadı. https://www.python.org/downloads/ (3.10+), kurulumda 'Add to PATH' işaretleyin." }
& $py @pyArgs -c "import sys; sys.exit(0 if sys.version_info >= (3, 10) else 1)"
if ($LASTEXITCODE -ne 0) { Fail "Python 3.10+ gerekli ($py)." }

# 2) Sanal ortam + bağımlılıklar
$venvPy = Join-Path $PSScriptRoot ".venv\Scripts\python.exe"
if (-not (Test-Path -LiteralPath $venvPy)) { Invoke-Checked "Sanal ortam oluşturma" $py ($pyArgs + @("-m", "venv", ".venv")) }
Invoke-Checked "Bağımlılık kurulumu (Pillow)" $venvPy @("-m", "pip", "install", "--disable-pip-version-check", "-q", "-r", "requirements.txt")

# 3) Codex: mevcut olanı kullan; yalnız istenirse kur
if ($CodexPath) {
    if (-not (Test-Path -LiteralPath $CodexPath)) { Fail "-CodexPath bulunamadı: $CodexPath" }
    $env:VF_CODEX_BIN = (Resolve-Path -LiteralPath $CodexPath).Path
    [Environment]::SetEnvironmentVariable("VF_CODEX_BIN", $env:VF_CODEX_BIN, "User")
    Write-Host "Codex yolu kullanıcı ortamına kaydedildi: VF_CODEX_BIN=$env:VF_CODEX_BIN"
} elseif ($env:VF_CODEX_BIN) {
    Write-Host "Mevcut VF_CODEX_BIN kullanılıyor: $env:VF_CODEX_BIN"
} elseif ($cmd = Get-Command codex -ErrorAction SilentlyContinue) {
    Write-Host "Mevcut Codex bulundu, yeniden kurulmayacak: $($cmd.Source)"
} elseif ($InstallCodex) {
    if (-not (Get-Command npm -ErrorAction SilentlyContinue)) { Fail "npm yok. Node.js 18+ kurun: https://nodejs.org/" }
    Invoke-Checked "Codex CLI kurulumu (npm i -g @openai/codex)" (Get-Command npm).Source @("i", "-g", "@openai/codex")
} else {
    Write-Host "Codex bulunamadı. Kuruluysa: -CodexPath <codex.cmd|codex.exe>; kurmak için: -InstallCodex" -ForegroundColor Yellow
}

# 4) Çalışma alanı + doctor (çalışma zamanıyla aynı başlatıcı çözümü)
Invoke-Checked "Çalışma alanı" $venvPy @("-m", "viralforge", "-w", $Workspace, "init")
Write-Host "-> doctor" -ForegroundColor Cyan
& $venvPy -m viralforge -w $Workspace doctor
$code = $LASTEXITCODE
switch ($code) {
    0 { Write-Host "HAZIR. Sonraki: $venvPy -m viralforge -w $Workspace import-urls <instagram-export.zip>" -ForegroundColor Green }
    3 { Write-Host "ETKİLEŞİMLİ GİRİŞ GEREKLİ (otomatik yapılmadı): bu pencerede 'codex login' yazın, 'Sign in with ChatGPT' seçip tarayıcıda onaylayın, sonra bu betiği tekrar çalıştırın." -ForegroundColor Yellow }
    2 { Write-Host "Codex çalıştırılamıyor: yukarıdaki doctor çıktısındaki 'codex_cli' / 'codex_calisma' alanına bakın." -ForegroundColor Red }
    default { Write-Host "doctor beklenmeyen çıkış kodu: $code" -ForegroundColor Red }
}
exit $code
