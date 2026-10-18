@echo off
rem Telefona WinRemote Monitor uygulamasini USB ile kurar.
rem Telefonda: Ayarlar ^> Telefon hakkinda ^> Yapim numarasi (7 kez dokun) ^> Gelistirici secenekleri ^> USB hata ayiklama ACIK.
setlocal
set "PT=%LOCALAPPDATA%\WinRemoteMonitor\platform-tools"
if not exist "%PT%\adb.exe" (
  echo Google platform-tools indiriliyor...
  mkdir "%LOCALAPPDATA%\WinRemoteMonitor" 2>nul
  curl.exe -L -o "%TEMP%\platform-tools.zip" https://dl.google.com/android/repository/platform-tools-latest-windows.zip || goto :fail
  tar.exe -xf "%TEMP%\platform-tools.zip" -C "%LOCALAPPDATA%\WinRemoteMonitor" || goto :fail
)
echo Telefonu USB ile baglayin ve telefonda cikan "USB hata ayiklamaya izin ver" sorusuna IZIN VER deyin.
"%PT%\adb.exe" wait-for-device
"%PT%\adb.exe" install -r "%~dp0app-release.apk" || goto :fail
echo.
echo Kuruldu. Telefonda WinRemote Monitor uygulamasini acin.
pause
exit /b 0
:fail
echo Kurulum basarisiz. Telefonda USB hata ayiklama acik mi? Kablo veri kablosu mu?
pause
exit /b 1
