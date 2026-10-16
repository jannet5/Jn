@echo off
chcp 65001 >nul
cd /d "%~dp0"
rem Once Windows Python baslaticisi (py), yoksa python denenir.
py -3 olc.py dogrula-yerel %*
if %errorlevel%==9009 python olc.py dogrula-yerel %*
echo.
pause
