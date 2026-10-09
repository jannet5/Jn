@echo off
rem WinRemote Monitor kurulumu - cift tiklayin.
powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0kur.ps1" %*
echo.
pause
