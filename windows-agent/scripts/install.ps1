#Requires -Version 5.1
<#
.SYNOPSIS
  Installs CepGözcü Agent as a Windows service, restricted to the local/private network.

.DESCRIPTION
  - Copies the published files into an install directory (Program Files by default).
  - Opens an inbound firewall rule for the agent's port, scoped to Private/Domain network
    profiles only (never Public) — this is the primary enforcement of "LAN only", backed up
    by the agent's own IP-range check at the application layer (see LanOnlyMiddleware).
  - Registers and starts a Windows service that starts automatically at boot.

  Must be run as Administrator (creating a service and a firewall rule both require it).

.PARAMETER SourceDir
  Folder containing the published CepGozcu.Agent.Host.exe (output of publish.ps1).

.PARAMETER InstallDir
  Where to install the agent. Defaults to "$env:ProgramFiles\CepGozcuAgent".

.PARAMETER Port
  TCP port the agent listens on (HTTPS + WebSocket). Defaults to 47811.

.EXAMPLE
  ./scripts/install.ps1 -SourceDir .\dist\win-x64
#>
param(
    [Parameter(Mandatory = $true)][string]$SourceDir,
    [string]$InstallDir = (Join-Path $env:ProgramFiles "CepGozcuAgent"),
    [int]$Port = 47811
)

$ErrorActionPreference = "Stop"
$ServiceName = "CepGozcuAgent"

$currentPrincipal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
if (-not $currentPrincipal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    throw "Bu betiği Yönetici (Administrator) olarak çalıştırmalısınız."
}

$exeName = "CepGozcu.Agent.Host.exe"
$sourceExe = Join-Path $SourceDir $exeName
if (-not (Test-Path $sourceExe)) {
    throw "Kaynak klasörde $exeName bulunamadı: $SourceDir (önce ./scripts/publish.ps1 çalıştırın)"
}

Write-Host "Kurulum klasörü: $InstallDir" -ForegroundColor Cyan
New-Item -ItemType Directory -Force -Path $InstallDir | Out-Null
Copy-Item -Path (Join-Path $SourceDir "*") -Destination $InstallDir -Recurse -Force

$existing = Get-Service -Name $ServiceName -ErrorAction SilentlyContinue
if ($existing) {
    Write-Host "Var olan servis durduruluyor ve kaldırılıyor..." -ForegroundColor Yellow
    Stop-Service -Name $ServiceName -Force -ErrorAction SilentlyContinue
    sc.exe delete $ServiceName | Out-Null
    Start-Sleep -Seconds 1
}

$targetExe = Join-Path $InstallDir $exeName
Write-Host "Servis oluşturuluyor: $ServiceName" -ForegroundColor Cyan
New-Service -Name $ServiceName `
    -BinaryPathName "`"$targetExe`"" `
    -DisplayName "CepGözcü Agent" `
    -Description "CepGözcü Android uygulamasının bu bilgisayarı izlemesini/yönetmesini sağlayan yerel ağ servisi." `
    -StartupType Automatic | Out-Null

# Windows services don't inherit a user's environment variables; the supported way to give a
# service its own env vars is the Environment multi-string value under its registry key.
$regPath = "HKLM:\SYSTEM\CurrentControlSet\Services\$ServiceName"
Set-ItemProperty -Path $regPath -Name "Environment" -Value @("CEPGOZCU_PORT=$Port") -Type MultiString

Write-Host "Güvenlik duvarı kuralı ekleniyor (yalnızca özel/etki alanı ağları, port $Port)..." -ForegroundColor Cyan
Remove-NetFirewallRule -DisplayName "CepGozcu Agent" -ErrorAction SilentlyContinue
New-NetFirewallRule -DisplayName "CepGozcu Agent" `
    -Direction Inbound `
    -Action Allow `
    -Protocol TCP `
    -LocalPort $Port `
    -Profile Private,Domain `
    -Program $targetExe | Out-Null

Write-Host "Servis başlatılıyor..." -ForegroundColor Cyan
Start-Service -Name $ServiceName
Start-Sleep -Seconds 2
Get-Service -Name $ServiceName | Format-Table -AutoSize

Write-Host ""
Write-Host "Kurulum tamamlandı." -ForegroundColor Green
Write-Host "Yönetim paneli: https://127.0.0.1:$Port/  (bu bilgisayarda bir tarayıcıda açın)"
Write-Host "Kaldırmak için: ./scripts/uninstall.ps1"
