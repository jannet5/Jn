@echo off
chcp 65001 >nul
rem Ses dosyalarını (wav, mp3, m4a...) bu dosyanın üzerine sürükleyip bırakın.
rem Yapay zekâ (DeepFilterNet3) ile tam güçte temizler. Sonuçlar kaydın yanına yazılır.
if "%~1"=="" (
  echo Ses dosyalarini bu .bat dosyasinin uzerine surukleyip birakin.
  pause
  exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0Ses-Temizle.ps1" %*
echo.
pause
