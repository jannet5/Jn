@echo off
REM Cevrimdisi TOTP - Windows paketleme (PyInstaller, onedir)
REM Gereken: Python 3.10+ (python.org), internet yalnizca pip kurulumu icin.
setlocal
cd /d "%~dp0"
py -m pip install --upgrade customtkinter pyotp keyring pyinstaller || goto :hata
REM customtkinter veri dosyalari pyinstaller-hooks-contrib icindeki hook ile otomatik eklenir.
REM CustomTkinter belgesi --onefile yerine --onedir onerir.
py -m PyInstaller --noconfirm --clean --onedir --windowed --exclude-module PIL --name CevrimdisiTOTP uygulama\totp_masaustu.py || goto :hata
echo.
echo Hazir: dist\CevrimdisiTOTP\CevrimdisiTOTP.exe
exit /b 0
:hata
echo Paketleme basarisiz oldu.
exit /b 1
