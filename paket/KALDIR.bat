@echo off
rem WinRemote Monitor kaldirma - cift tiklayin.
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0kaldir.ps1" %*
echo.
pause
