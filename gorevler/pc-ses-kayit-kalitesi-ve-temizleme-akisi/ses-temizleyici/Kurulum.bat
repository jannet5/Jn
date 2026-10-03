@echo off
setlocal
chcp 65001 >nul
rem Kurulum: deep-filter.exe dogrulama/indirme, ffmpeg (winget), Audacity makrosu, calisma testi.
rem Cikis kodu: 0 basarili, 2 deep-filter, 3 ffmpeg, 4 calisma testi basarisiz.
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0Kurulum.ps1"
set "SES_KOD=%ERRORLEVEL%"
echo.
if not "%SES_KOD%"=="0" echo [HATA] Kurulum TAMAMLANMADI ^(cikis kodu %SES_KOD%^). Yukaridaki mesajlara bakin.
pause
exit /b %SES_KOD%
