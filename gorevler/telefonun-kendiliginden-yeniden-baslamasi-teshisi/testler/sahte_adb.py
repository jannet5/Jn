#!/usr/bin/env python3
"""SENTETİK test ikizi: gerçek adb yerine fikstür çıktısı döndürür.
Buradaki veriler gerçek bir telefona ait DEĞİLDİR; yalnız aracın ayrıştırma ve
karar mantığını uçtan uca sınamak içindir."""
import os, sys
D = os.path.join(os.path.dirname(os.path.abspath(__file__)), "ornek-veri")
a = sys.argv[1:]
if a[:1] == ["-s"]:
    a = a[2:]
if a == ["devices"]:
    print("List of devices attached\nSENTETIK0001\tdevice\n"); sys.exit(0)
k = " ".join(a[1:]).replace("dumpsys -t 60 ", "dumpsys ") if a[:1] == ["shell"] else ""
def f(ad): print(open(os.path.join(D, ad), encoding="utf-8").read())
if k == "service check dropbox": print("Service dropbox: found")
elif k == "getprop": f("getprop.txt")
elif k.startswith("dumpsys dropbox --print system_server_crash"): f("ss_crash.txt")
elif k.startswith("dumpsys dropbox --print"): print("Drop box contents: 0 entries")
elif k == "dumpsys dropbox": f("dropbox_liste.txt")
elif k == "dumpsys battery": f("battery.txt")
elif k == "df /data": print("Filesystem 1K-blocks Used Available Use% Mounted on\n/dev/block/dm-5 110000000 104500000 5500000 95% /data")
elif k.startswith("pm list packages -3"): print("package:com.ornek.benimuygulamam installer=null\npackage:com.whatsapp installer=com.android.vending")
elif k.startswith("dumpsys package com.ornek.benimuygulamam"):
    print("    versionName=0.3-debug\n    firstInstallTime=2026-09-30 20:10:00\n    lastUpdateTime=2026-10-02 22:41:00")
elif k.startswith("dumpsys package com.whatsapp"):
    print("    versionName=2.26.1\n    firstInstallTime=2025-01-10 10:00:00\n    lastUpdateTime=2026-08-01 09:00:00")
else: print("")
