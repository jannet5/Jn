@echo off
setlocal
chcp 65001 >nul
rem Daha dogal sonuc: gurultu en fazla 30 dB azaltilir, konusma aralarinda hafif oda sesi kalir.
rem Cikis kodu: PowerShell betiginin kodu AYNEN geri dondurulur (0 = basarili).
if "%~1"=="" (
  echo Ses dosyalarini bu .bat dosyasinin uzerine surukleyip birakin.
  pause
  exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0Ses-Temizle.ps1" -Guc 30 %*
set "SES_KOD=%ERRORLEVEL%"
echo.
if not "%SES_KOD%"=="0" echo [HATA] Islem basarisiz oldu ^(cikis kodu %SES_KOD%^). Yukaridaki mesajlara bakin.
pause
exit /b %SES_KOD%
