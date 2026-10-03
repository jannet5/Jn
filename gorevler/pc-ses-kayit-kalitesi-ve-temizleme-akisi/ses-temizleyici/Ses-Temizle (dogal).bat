@echo off
chcp 65001 >nul
rem Daha dogal sonuc: gurultu en fazla 30 dB azaltilir, konusma aralarinda hafif oda sesi kalir.
rem Tam guc sesi boguk / kesik kesik yaptiysa bunu kullanin.
if "%~1"=="" (
  echo Ses dosyalarini bu .bat dosyasinin uzerine surukleyip birakin.
  pause
  exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0Ses-Temizle.ps1" -Guc 30 %*
echo.
pause
