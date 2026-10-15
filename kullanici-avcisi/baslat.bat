@echo off
chcp 65001 >nul
cd /d "%~dp0"
title Kullanici Adi Avcisi

set PY=
where python >nul 2>nul && set PY=python
if "%PY%"=="" where py >nul 2>nul && set PY=py
if "%PY%"=="" (
  echo.
  echo  Python bulunamadi. Bir kerelik kurman gerekiyor:
  echo    1^) https://www.python.org/downloads/ adresinden Python'u indir
  echo    2^) Kurarken en alttaki "Add python.exe to PATH" kutusunu ISARETLE
  echo    3^) Kurulum bitince bu dosyaya tekrar cift tikla
  echo.
  start https://www.python.org/downloads/
  pause
  exit /b 1
)

echo Gerekli parcalar kontrol ediliyor (ilk seferde 1 dakika surebilir)...
%PY% -m pip install -q -r requirements.txt
if errorlevel 1 (
  echo.
  echo  Kurulum basarisiz. Internet bagli mi? Bu pencerenin fotografini gonder.
  pause
  exit /b 1
)

echo.
echo Ekran aciliyor... Bu pencereyi KAPATMA; arac calisirken acik kalmali.
%PY% -m avci ui
pause
