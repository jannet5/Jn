#!/bin/bash
# Mac / Linux: çift tıkla (Mac) ya da ./baslat.command
cd "$(dirname "$0")" || exit 1
if ! command -v python3 >/dev/null 2>&1; then
  echo "Python3 bulunamadı. https://www.python.org/downloads/ adresinden kur, sonra tekrar çalıştır."
  read -r -p "Kapatmak için Enter..." _; exit 1
fi
echo "Gerekli parçalar kontrol ediliyor..."
python3 -m pip install -q --user -r requirements.txt 2>/dev/null || python3 -m pip install -q --user --break-system-packages -r requirements.txt
echo "Ekran açılıyor... Bu pencereyi KAPATMA."
python3 -m avci ui
