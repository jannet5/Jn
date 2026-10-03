@echo off
setlocal
chcp 65001 >nul
rem KAYIT ONCESI TEST: 10-15 sn deneme kaydini buraya birakin. Temizleme yapmaz, yalniz olcer.
rem Cikis kodu: PowerShell betiginin kodu AYNEN geri dondurulur (0 = basarili).
if "%~1"=="" (
  echo Deneme kaydini bu .bat dosyasinin uzerine surukleyip birakin.
  pause
  exit /b 1
)
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0Ses-Temizle.ps1" -Kontrol %*
set "SES_KOD=%ERRORLEVEL%"
echo.
if not "%SES_KOD%"=="0" echo [HATA] Islem basarisiz oldu ^(cikis kodu %SES_KOD%^). Yukaridaki mesajlara bakin.
pause
exit /b %SES_KOD%
