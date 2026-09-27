#Requires -Version 5.1
<#
.SYNOPSIS
  Builds a self-contained, single-file CepGözcü Agent executable for Windows x64.

.DESCRIPTION
  Produces a standalone CepGozcu.Agent.Host.exe that bundles the .NET 8 runtime, so the target
  PC does not need .NET installed separately. Run this from the windows-agent/ folder (or pass
  -RepoRoot). Requires the .NET 8 SDK (https://dotnet.microsoft.com/download/dotnet/8.0) — this
  script itself can be run on Windows, Linux or macOS since `dotnet publish -r win-x64` cross
  compiles; only *running* the resulting .exe requires Windows.

.PARAMETER OutputDir
  Where to place the published output. Defaults to .\dist\win-x64 next to this script.

.EXAMPLE
  ./scripts/publish.ps1
  ./scripts/publish.ps1 -OutputDir C:\Temp\CepGozcuAgent-build
#>
param(
    [string]$OutputDir = (Join-Path $PSScriptRoot "..\dist\win-x64")
)

$ErrorActionPreference = "Stop"
$repoRoot = Resolve-Path (Join-Path $PSScriptRoot "..")
$hostProject = Join-Path $repoRoot "src\CepGozcu.Agent.Host\CepGozcu.Agent.Host.csproj"

Write-Host "Publishing $hostProject -> $OutputDir" -ForegroundColor Cyan

dotnet publish $hostProject `
    -c Release `
    -r win-x64 `
    --self-contained true `
    -p:PublishSingleFile=true `
    -p:IncludeNativeLibrariesForSelfExtract=true `
    -o $OutputDir

if ($LASTEXITCODE -ne 0) {
    throw "dotnet publish failed with exit code $LASTEXITCODE"
}

$exePath = Join-Path $OutputDir "CepGozcu.Agent.Host.exe"
if (-not (Test-Path $exePath)) {
    throw "Expected output not found: $exePath"
}

$hash = (Get-FileHash -Algorithm SHA256 $exePath).Hash
Write-Host ""
Write-Host "Build complete." -ForegroundColor Green
Write-Host "  Executable: $exePath"
Write-Host "  SHA-256:    $hash"
Write-Host ""
Write-Host "Next: run ./scripts/install.ps1 -SourceDir `"$OutputDir`" as Administrator to install it as a service." -ForegroundColor Yellow
