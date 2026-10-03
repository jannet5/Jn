@echo off
setlocal
chcp 65001 >nul
rem Ses dosyalarini (wav, mp3, m4a...) bu dosyanin uzerine surukleyip birakin. Tam guc AI temizleme.
rem Cikis kodu: PowerShell betiginin kodu AYNEN geri dondurulur (0 = basarili).
if "%~1"=="" (
  echo Ses dosyalarini bu .bat dosyasinin uzerine surukleyip birakin.
  pause
  exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0Ses-Temizle.ps1" %*
set "SES_KOD=%ERRORLEVEL%"
echo.
if not "%SES_KOD%"=="0" echo [HATA] Islem basarisiz oldu ^(cikis kodu %SES_KOD%^). Yukaridaki mesajlara bakin.
pause
exit /b %SES_KOD%
