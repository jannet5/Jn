# ViralForge Windows kurulumu (PowerShell). Bulut ortamında Windows'ta ÇALIŞTIRILMADI; adımlar standarttır.
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
if (-not (Get-Command python -ErrorAction SilentlyContinue)) { Write-Host "Python 3.10+ kurun: https://www.python.org/downloads/"; exit 1 }
if (-not (Get-Command npm -ErrorAction SilentlyContinue)) { Write-Host "Node.js 18+ kurun: https://nodejs.org/"; exit 1 }
python -m venv .venv
& .\.venv\Scripts\python.exe -m pip install -r requirements.txt
npm i -g @openai/codex
Write-Host "Simdi ChatGPT Pro hesabinizla giris: codex login  (Sign in with ChatGPT secin)"
codex login
& .\.venv\Scripts\python.exe -m viralforge -w calisma init
& .\.venv\Scripts\python.exe -m viralforge -w calisma doctor
Write-Host "Hazir. Sonraki: .\.venv\Scripts\python.exe -m viralforge -w calisma import-urls <instagram-export.zip>"
