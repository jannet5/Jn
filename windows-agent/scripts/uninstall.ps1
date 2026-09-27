#Requires -Version 5.1
<#
.SYNOPSIS
  Removes the CepGözcü Agent Windows service, its firewall rule, and (optionally) its data.

.PARAMETER RemoveData
  Also deletes the agent's ProgramData folder (paired devices, disk history, allowlist, audit
  log, TLS certificate). Omit this to keep that data for a future reinstall.

.PARAMETER RemoveInstallDir
  Also deletes the installed program files (Program Files\CepGozcuAgent by default).
#>
param(
    [string]$InstallDir = (Join-Path $env:ProgramFiles "CepGozcuAgent"),
    [switch]$RemoveData,
    [switch]$RemoveInstallDir
)

$ErrorActionPreference = "Stop"
$ServiceName = "CepGozcuAgent"

$currentPrincipal = New-Object Security.Principal.WindowsPrincipal([Security.Principal.WindowsIdentity]::GetCurrent())
if (-not $currentPrincipal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)) {
    throw "Bu betiği Yönetici (Administrator) olarak çalıştırmalısınız."
}

$existing = Get-Service -Name $ServiceName -ErrorAction SilentlyContinue
if ($existing) {
    Write-Host "Servis durduruluyor: $ServiceName" -ForegroundColor Cyan
    Stop-Service -Name $ServiceName -Force -ErrorAction SilentlyContinue
    sc.exe delete $ServiceName | Out-Null
} else {
    Write-Host "Servis zaten kurulu değil." -ForegroundColor Yellow
}

Write-Host "Güvenlik duvarı kuralı kaldırılıyor..." -ForegroundColor Cyan
Remove-NetFirewallRule -DisplayName "CepGozcu Agent" -ErrorAction SilentlyContinue

if ($RemoveInstallDir -and (Test-Path $InstallDir)) {
    Write-Host "Program dosyaları siliniyor: $InstallDir" -ForegroundColor Cyan
    Remove-Item -Recurse -Force $InstallDir
}

if ($RemoveData) {
    $dataDir = Join-Path $env:ProgramData "CepGozcuAgent"
    if (Test-Path $dataDir) {
        Write-Host "Veri klasörü siliniyor: $dataDir" -ForegroundColor Cyan
        Remove-Item -Recurse -Force $dataDir
    }
}

Write-Host "Kaldırma tamamlandı." -ForegroundColor Green
