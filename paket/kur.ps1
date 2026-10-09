# WinRemote Monitor - Windows kurulum betiği
#
# Ne yapar (sırayla):
#   1. Ajanı C:\WinRemoteMonitor klasörüne kopyalar (eski sürüm çalışıyorsa durdurur).
#   2. config.json yoksa oluşturur: Downloads / Masaüstü / Belgeler izlenir.
#      allowed_apps.json yoksa örneği koyar. Var olan dosyalara DOKUNMAZ.
#   3. Windows Güvenlik Duvarı'nda gelen TCP 8787'ye izin verir (tek bir
#      yönetici onayı - UAC - ister).
#   4. Oturum açılınca otomatik başlasın diye Başlangıç klasörüne kısayol koyar.
#   5. Ajanı arka planda başlatır, eşleştirme kodu + QR üretir ve QR'ı açar.
#
# Ajanı yönetici OLMADAN çalıştırır: veri klasörü (%ProgramData%\WinRemoteMonitor)
# normal kullanıcıya ait olmalı, yoksa sonraki açılışlarda ajan veritabanına
# yazamaz. Sadece güvenlik duvarı kuralı yönetici olarak eklenir.
#
# Windows PowerShell 5.1 ile uyumludur (Windows 10/11'de hazır gelir).

[CmdletBinding()]
param(
    [string]$InstallDir = 'C:\WinRemoteMonitor',
    [int]$Port = 8787,
    [switch]$NoFirewall,
    [switch]$NoAutostart,
    [switch]$NoPair
)

$ErrorActionPreference = 'Stop'
$here = Split-Path -Parent $MyInvocation.MyCommand.Path
$exeName = 'winremotemonitor-agent.exe'
$exeSrc = Join-Path $here $exeName
$exeDst = Join-Path $InstallDir $exeName
# Sistem araçlarını tam yoluyla çağır (PATH'e güvenme).
$netsh = Join-Path $env:SystemRoot 'System32\netsh.exe'

function Step($msg) { Write-Host "`n==> $msg" -ForegroundColor Cyan }
function Ok($msg)   { Write-Host "    [tamam] $msg" -ForegroundColor Green }
function Warn($msg) { Write-Host "    [uyari] $msg" -ForegroundColor Yellow }

function Write-Utf8NoBom($path, $text) {
    # PowerShell 5.1'in Set-Content -Encoding UTF8'i BOM ekler; düz UTF-8 yaz.
    [System.IO.File]::WriteAllText($path, $text, (New-Object System.Text.UTF8Encoding $false))
}

function Test-Port($port) {
    $client = New-Object System.Net.Sockets.TcpClient
    try {
        $iar = $client.BeginConnect('127.0.0.1', $port, $null, $null)
        if (-not $iar.AsyncWaitHandle.WaitOne(500)) { return $false }
        $client.EndConnect($iar)
        return $true
    } catch { return $false } finally { $client.Close() }
}

if ($env:OS -ne 'Windows_NT') { throw 'Bu betik Windows içindir.' }
if (-not (Test-Path $exeSrc)) { throw "$exeName bulunamadı. Betiği zip'ten çıkarılmış klasörün içinden çalıştırın." }

# 1) Dosyaları kopyala ------------------------------------------------------
Step "Ajan $InstallDir klasörüne kuruluyor"
Get-Process -Name 'winremotemonitor-agent' -ErrorAction SilentlyContinue | ForEach-Object {
    Warn "Çalışan eski ajan durduruluyor (PID $($_.Id))"
    Stop-Process -Id $_.Id -Force
    Start-Sleep -Seconds 1
}
New-Item -ItemType Directory -Force -Path $InstallDir | Out-Null
Copy-Item $exeSrc $exeDst -Force
# İnternetten indirilen zip'teki dosyalar "web'den geldi" işareti taşır;
# kullanıcı kurulumu bilerek başlattığı için SmartScreen engelini kaldır.
Unblock-File -Path $exeDst -ErrorAction SilentlyContinue
Ok "Ajan kopyalandı: $exeDst"

$appsDst = Join-Path $InstallDir 'allowed_apps.json'
if (Test-Path $appsDst) {
    Ok 'allowed_apps.json zaten var, korunuyor'
} else {
    Copy-Item (Join-Path $here 'allowed_apps.json') $appsDst
    Ok 'allowed_apps.json oluşturuldu (Not Defteri, Hesap Makinesi)'
}

# 2) config.json ------------------------------------------------------------
$cfgDst = Join-Path $InstallDir 'config.json'
if (Test-Path $cfgDst) {
    Ok 'config.json zaten var, korunuyor'
} else {
    $roots = @()
    $candidates = @(
        (Join-Path $env:USERPROFILE 'Downloads'),
        [Environment]::GetFolderPath('Desktop'),
        [Environment]::GetFolderPath('MyDocuments')
    )
    foreach ($c in $candidates) {
        if ($c -and (Test-Path $c) -and ($roots -notcontains $c)) { $roots += $c }
    }
    $cfg = [ordered]@{
        port              = $Port
        watched_roots     = $roots
        allowed_apps_path = 'allowed_apps.json'
    }
    Write-Utf8NoBom $cfgDst ($cfg | ConvertTo-Json -Depth 4)
    Ok ("config.json oluşturuldu. İzlenen klasörler: " + ($roots -join ', '))
}

# 3) Güvenlik duvarı --------------------------------------------------------
if ($NoFirewall) {
    Warn 'Güvenlik duvarı adımı atlandı (-NoFirewall)'
} else {
    Step "Güvenlik duvarında TCP $Port açılıyor"
    try {
        & $netsh advfirewall firewall show rule name="WinRemoteMonitor" > $null 2>&1
        if ($LASTEXITCODE -eq 0) {
            Ok 'Kural zaten var'
        } else {
            $ruleArgs = @('advfirewall', 'firewall', 'add', 'rule', 'name=WinRemoteMonitor', 'dir=in', 'action=allow', 'protocol=TCP', "localport=$Port")
            $p = Start-Process -FilePath $netsh -ArgumentList $ruleArgs -Verb RunAs -WindowStyle Hidden -Wait -PassThru
            if ($p.ExitCode -eq 0) { Ok 'Kural eklendi' } else { Warn "Kural eklenemedi (çıkış kodu $($p.ExitCode))" }
        }
    } catch {
        Warn "Güvenlik duvarı kuralı eklenemedi: $($_.Exception.Message)"
        Warn 'Telefon bağlanamazsa ilk çalıştırmada çıkan güvenlik duvarı penceresinde izin verin.'
    }
}

# 4) Otomatik başlatma ------------------------------------------------------
$startup = [Environment]::GetFolderPath('Startup')
$lnkPath = Join-Path $startup 'WinRemote Monitor.lnk'
$logPath = Join-Path $InstallDir 'agent.log'
$launch = "Start-Process -FilePath '$exeDst' -WorkingDirectory '$InstallDir' -WindowStyle Hidden -RedirectStandardError '$logPath'"
if ($NoAutostart) {
    Warn 'Otomatik başlatma atlandı (-NoAutostart)'
} else {
    Step 'Oturum açılınca otomatik başlatma ayarlanıyor'
    $sh = New-Object -ComObject WScript.Shell
    $lnk = $sh.CreateShortcut($lnkPath)
    $lnk.TargetPath = Join-Path $env:SystemRoot 'System32\WindowsPowerShell\v1.0\powershell.exe'
    $lnk.Arguments = "-NoProfile -WindowStyle Hidden -Command `"$launch`""
    $lnk.WorkingDirectory = $InstallDir
    $lnk.WindowStyle = 7
    $lnk.Description = 'WinRemote Monitor ajanını arka planda başlatır'
    $lnk.Save()
    Ok "Başlangıç kısayolu: $lnkPath"
}

# 5) Başlat ve eşleştir -----------------------------------------------------
Step 'Ajan arka planda başlatılıyor'
Start-Process -FilePath $exeDst -WorkingDirectory $InstallDir -WindowStyle Hidden -RedirectStandardError $logPath
$up = $false
for ($i = 0; $i -lt 30; $i++) {
    if (Test-Port $Port) { $up = $true; break }
    Start-Sleep -Milliseconds 500
}
if (-not $up) {
    Write-Host "`nAjan başlamadı. Günlük ($logPath):" -ForegroundColor Red
    if (Test-Path $logPath) { Get-Content $logPath -Tail 20 }
    exit 1
}
Ok "Ajan çalışıyor (port $Port). Günlük: $logPath"

if (-not $NoPair) {
    Step 'Eşleştirme kodu üretiliyor'
    Push-Location $InstallDir
    try { & $exeDst --pair } finally { Pop-Location }
    $qr = Join-Path $InstallDir 'pairing-qr.png'
    if (Test-Path $qr) { Invoke-Item $qr }
    Write-Host ''
    Write-Host 'Telefonda: uygulamayı açın -> "QR Tara" -> açılan QR resmini okutun -> "Eşleştir".' -ForegroundColor Green
    Write-Host 'Kod 5 dakika geçerlidir. Yeni kod için: yeni-kod.bat' -ForegroundColor Green
}
