<#
ViralForge tek komut üretim (Windows PowerShell 5.1+ / 7+). kur-windows.ps1'den SONRA çalıştırın.

  powershell -NoProfile -ExecutionPolicy Bypass -File .\uret-windows.ps1
  powershell -NoProfile -ExecutionPolicy Bypass -File .\uret-windows.ps1 -Indirilenler "D:\IG" -Hedef 1000

Sıra: doctor (giriş yoksa durur, girişi kendisi BAŞLATMAZ) → İndirilenler'deki Instagram dışa aktarım
ZIP/JSON'larını ve vf-capture-*.json yakalamalarını içe al → run (seç → analiz → üret → doğrula → rapor) → bitti-mi.
Kaldığı yerden devam eder; plan limiti dolarsa (çıkış 75) daha sonra aynı komutu tekrar çalıştırın.
Çıkış: 0 bitti · 1 devam gerekiyor · 2 Codex yok · 3 giriş gerekli · 75 plan limiti
#>
[CmdletBinding()]
param(
    [string]$Indirilenler = (Join-Path $env:USERPROFILE "Downloads"),
    [string]$Workspace = "calisma",
    [int]$Hedef = 1000
)
$ErrorActionPreference = "Stop"
Set-Location -LiteralPath $PSScriptRoot
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8
$env:PYTHONUTF8 = "1"

$py = Join-Path $PSScriptRoot ".venv\Scripts\python.exe"
if (-not (Test-Path -LiteralPath $py)) { Write-Host "Önce kur-windows.ps1 çalıştırın (.venv yok)." -ForegroundColor Red; exit 1 }

function VF([string[]]$a) { & $py -m viralforge -w $Workspace @a | Out-Host; return $LASTEXITCODE }

# 1) Ön kontrol
$c = VF @("doctor")
if ($c -eq 3) { Write-Host "GİRİŞ GEREKLİ: bu pencerede 'codex login' yazıp ChatGPT ile girin, sonra bu betiği tekrar çalıştırın." -ForegroundColor Yellow; exit 3 }
if ($c -ne 0) { Write-Host "Codex çalıştırılamıyor (doctor çıkış $c). kur-windows.ps1 -CodexPath <codex.cmd> deneyin." -ForegroundColor Red; exit $c }

# 2) Kaynak içe alma (bulunanlar; hiçbiri yoksa açıkça söyler)
if (-not (Test-Path -LiteralPath $Indirilenler)) { Write-Host "Klasör yok: $Indirilenler" -ForegroundColor Red; exit 1 }
$exports = @(Get-ChildItem -LiteralPath $Indirilenler -File | Where-Object {
    ($_.Extension -eq ".zip" -and $_.Name -match "instagram") -or $_.Name -match "^(saved_posts|liked_posts).*\.json$" -or $_.Name -match "linkler.*\.txt$" })
$captures = @(Get-ChildItem -LiteralPath $Indirilenler -File -Filter "vf-capture-*.json")
if ($exports.Count -gt 0) {
    Write-Host "-> Bağlantı içe alma: $($exports.Count) dosya" -ForegroundColor Cyan
    $c = VF (@("import-urls") + ($exports | ForEach-Object { $_.FullName }))
    if ($c -ne 0) { Write-Host "import-urls başarısız (çıkış $c)" -ForegroundColor Red; exit 1 }
}
if ($captures.Count -gt 0) {
    Write-Host "-> Yakalama içe alma: $($captures.Count) gönderi" -ForegroundColor Cyan
    $c = VF @("import-meta", $Indirilenler)
    if ($c -ne 0) { Write-Host "import-meta başarısız (çıkış $c)" -ForegroundColor Red; exit 1 }
}
if ($exports.Count -eq 0 -and $captures.Count -eq 0) {
    Write-Host "UYARI: $Indirilenler içinde Instagram dışa aktarımı (instagram*.zip) veya vf-capture-*.json yok; yalnız mevcut çalışma alanı işlenecek." -ForegroundColor Yellow
}
VF @("status") | Out-Null

# 3) Üretim zinciri + sonuç
$c = VF @("run", "--target", "$Hedef")
if ($c -eq 75) { Write-Host "Plan limiti doldu; ilerleme kayıtlı. Limit sıfırlanınca bu betiği tekrar çalıştırın." -ForegroundColor Yellow; exit 75 }
$c = VF @("bitti-mi", "--target", "$Hedef")
Write-Host "Rapor: $(Join-Path $PSScriptRoot $Workspace)\rapor.html"
if ($c -eq 0) { exit 0 }
Write-Host "Devam gerekiyor: eksik foto/açıklama/ses için gönderi sayfalarında yakalama yer imine tıklayın veya limiti bekleyip tekrar çalıştırın." -ForegroundColor Yellow
exit 1
