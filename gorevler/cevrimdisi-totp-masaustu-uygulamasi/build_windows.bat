@echo off
REM Cevrimdisi TOTP - Windows paketleme (PyInstaller, onedir)
REM Gereken: Python 3.12 x64 (python.org). Internet yalnizca kilitli paketleri indirmek icin.
REM Bagimliliklar hash'li kilit dosyasindan, yalitilmis sanal ortama kurulur.
setlocal
cd /d "%~dp0"
py -3.12 -m venv .derleme-venv || goto :hata
.derleme-venv\Scripts\python.exe -m pip install --require-hashes --no-deps --only-binary=:all: -r requirements-build-windows.lock || goto :hata
REM customtkinter veri dosyalari pyinstaller-hooks-contrib icindeki hook ile otomatik eklenir.
REM CustomTkinter belgesi --onefile yerine --onedir onerir.
.derleme-venv\Scripts\python.exe -m PyInstaller --noconfirm --clean --onedir --windowed --exclude-module PIL --name CevrimdisiTOTP uygulama\totp_masaustu.py || goto :hata
echo.
echo Hazir: dist\CevrimdisiTOTP\CevrimdisiTOTP.exe
exit /b 0
:hata
echo Paketleme basarisiz oldu.
exit /b 1
