# WinRemote Monitor - kaldırma betiği
# Ajanı durdurur, otomatik başlatmayı ve güvenlik duvarı kuralını kaldırır,
# kurulum klasörünü siler. -KeepData verilmezse eşleşmeler ve geçmiş
# (%ProgramData%\WinRemoteMonitor) da silinir.

[CmdletBinding()]
param(
    [string]$InstallDir = 'C:\WinRemoteMonitor',
    [switch]$KeepData
)

$ErrorActionPreference = 'Continue'
$netsh = Join-Path $env:SystemRoot 'System32\netsh.exe'
function Ok($msg) { Write-Host "    [tamam] $msg" -ForegroundColor Green }

Get-Process -Name 'winremotemonitor-agent' -ErrorAction SilentlyContinue | ForEach-Object {
    Stop-Process -Id $_.Id -Force
    Ok "Ajan durduruldu (PID $($_.Id))"
}
Start-Sleep -Seconds 1

$lnk = Join-Path ([Environment]::GetFolderPath('Startup')) 'WinRemote Monitor.lnk'
if (Test-Path $lnk) { Remove-Item $lnk -Force; Ok 'Otomatik başlatma kaldırıldı' }

& $netsh advfirewall firewall show rule name="WinRemoteMonitor" > $null 2>&1
if ($LASTEXITCODE -eq 0) {
    try {
        Start-Process -FilePath $netsh -ArgumentList 'advfirewall', 'firewall', 'delete', 'rule', 'name=WinRemoteMonitor' -Verb RunAs -WindowStyle Hidden -Wait
        Ok 'Güvenlik duvarı kuralı kaldırıldı'
    } catch {
        Write-Host '    [uyari] Yönetici onayı verilmedi; güvenlik duvarı kuralı duruyor.' -ForegroundColor Yellow
    }
}

if (Test-Path $InstallDir) { Remove-Item $InstallDir -Recurse -Force; Ok "$InstallDir silindi" }

$data = Join-Path $env:ProgramData 'WinRemoteMonitor'
if (-not $KeepData -and (Test-Path $data)) { Remove-Item $data -Recurse -Force; Ok "$data silindi (eşleşmeler ve geçmiş)" }

Write-Host "`nWinRemote Monitor kaldırıldı. Telefondaki uygulamayı ayrıca kaldırabilirsiniz." -ForegroundColor Green
