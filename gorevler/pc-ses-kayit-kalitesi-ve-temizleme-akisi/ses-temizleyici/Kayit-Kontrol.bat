@echo off
chcp 65001 >nul
rem KAYIT ONCESI TEST: 10-15 sn deneme kaydini buraya birakin. Temizleme yapmaz;
rem seviye (kirpilma/kisiklik) ve oda gurultusu icin ne yapmaniz gerektigini soyler.
if "%~1"=="" (
  echo Deneme kaydini bu .bat dosyasinin uzerine surukleyip birakin.
  pause
  exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0Ses-Temizle.ps1" -Kontrol %*
echo.
pause
