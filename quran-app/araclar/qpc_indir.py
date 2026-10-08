# Quran.com API'den 604 sayfanın kelime/satır verisini (KFGQPC Hafs metni) indirir
import json, sys, time, urllib.request  # standart kütüphaneler
from pathlib import Path  # dosya yolu
hedef = Path(sys.argv[1])  # çıktı klasörü
for p in range(1, 605):  # her sayfa
    f = hedef / f"{p:03d}.json"  # sayfa dosyası
    if f.exists() and f.stat().st_size > 100: continue  # inmişse atla
    url = f"https://api.quran.com/api/v4/verses/by_page/{p}?words=true&word_fields=text_qpc_hafs,line_number,page_number&fields=text_qpc_hafs&per_page=50"
    for deneme in range(5):  # ağ hatasında tekrar dene
        try:
            f.write_bytes(urllib.request.urlopen(url, timeout=30).read()); break  # yaz
        except Exception as e:
            time.sleep(2 * (deneme + 1))  # bekle
    time.sleep(0.05)  # sunucuyu yormamak için
print("bitti")
