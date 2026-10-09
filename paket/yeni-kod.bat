@echo off
rem Yeni eslestirme kodu + QR uretir (kod 5 dakika gecerli).
set "DIR=C:\WinRemoteMonitor"
if not exist "%DIR%\winremotemonitor-agent.exe" (
  echo Ajan bulunamadi: %DIR%. Once KUR.bat calistirin.
  pause
  exit /b 1
)
cd /d "%DIR%"
winremotemonitor-agent.exe --pair
if exist pairing-qr.png start "" pairing-qr.png
echo.
pause
