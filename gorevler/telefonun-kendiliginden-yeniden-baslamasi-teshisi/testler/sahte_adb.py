#!/usr/bin/env python3
"""SENTETİK test ikizi: gerçek adb yerine fikstür çıktısı döndürür.
Buradaki veriler gerçek bir telefona/kişiye ait DEĞİLDİR (e-posta, telefon, IMEI vb.
uydurma test değerleridir); aracın ayrıştırma, maskeleme ve karar mantığını sınar.
Senaryo: SAHTE_ADB_SENARYO = normal | izin | zamanasimi | bos | coklu
Çağrı biçimi (platformdan bağımsız): REBOOT_TESHIS_ADB_KOMUTU='["<python>", "<bu dosya>"]'"""
import os, sys, time
# Gerçek adb cihaz çıktısını UTF-8 verir; ikiz de süreç kod sayfasından (ör. cp1254) bağımsız UTF-8 yazar.
sys.stdout.reconfigure(encoding="utf-8")
sys.stderr.reconfigure(encoding="utf-8")
D = os.path.join(os.path.dirname(os.path.abspath(__file__)), "ornek-veri")
SEN = os.environ.get("SAHTE_ADB_SENARYO", "normal")
a = sys.argv[1:]
if os.environ.get("SAHTE_ADB_IZ"):  # testler ikizin gerçekten çağrıldığını bu izden doğrular
    with open(os.environ["SAHTE_ADB_IZ"], "a", encoding="utf-8") as iz:
        iz.write(" ".join(a) + "\n")
if a[:1] == ["-s"]:
    a = a[2:]
if a == ["devices", "-l"]:
    print("List of devices attached")
    if SEN != "bos":
        print("SENTETIK0001           device product:test model:Test_Model device:test transport_id:1")
        print("SENTETIK0002           unauthorized usb:1-1 transport_id:2")
    if SEN == "coklu":
        print("SENTETIK0003           device product:test model:Test_Model device:test transport_id:3")
    sys.exit(0)
k = " ".join(a[1:]).replace("dumpsys -t 60 ", "dumpsys ") if a[:1] == ["shell"] else ""
def f(ad): sys.stdout.write(open(os.path.join(D, ad), encoding="utf-8").read())
PROP = {"sys.boot.reason": "reboot", "ro.boot.bootreason": "reboot", "sys.boot.reason.last": "reboot",
        "ro.product.manufacturer": "SENTETIK", "ro.product.model": "Test-Model",
        "ro.build.version.release": "14", "ro.build.version.sdk": "34",
        "ro.build.version.security_patch": "2026-08-01",
        "ro.build.fingerprint": "sentetik/test/test:14/TEST/1:user/release-keys"}
if SEN == "izin" and k in ("dumpsys dropbox", "pm list packages -3 -i") or \
        (SEN == "izin" and k.startswith("dumpsys dropbox --print")):
    sys.stderr.write("java.lang.SecurityException: Permission Denial: can't dump DropBox\n"); sys.exit(255)
if SEN == "zamanasimi" and k.startswith("logcat -b crash"):
    time.sleep(6); sys.exit(0)
if k == "service check dropbox": print("Service dropbox: found")
elif k == "settings get secure android_id": print("a1b2c3d4e5f60718")
elif k.startswith("getprop "): print(PROP.get(k.split()[1], ""))
elif k.startswith("cat /proc/uptime"): print("5000.00 4000.00\n1791028800\n2026-10-03 12:00:00")
elif k.startswith("dumpsys dropbox --print system_server_crash"): f("ss_crash.txt")
elif k.startswith("dumpsys dropbox --print system_server_watchdog"): f("ss_watchdog.txt")
elif k.startswith("dumpsys dropbox --print"): print("Drop box contents: 0 entries")
elif k == "dumpsys dropbox": f("dropbox_liste.txt")
elif k == "dumpsys battery": f("battery.txt")
elif k == "dumpsys thermalservice": print("IsStatusOverride: false\nThermal Status: 0\nCached temperatures:")
elif k == "df /data": print("Filesystem 1K-blocks Used Available Use% Mounted on\n/dev/block/dm-5 110000000 104500000 5500000 95% /data")
elif k.startswith("pm list packages -3"):
    print("package:com.ornek.benimuygulamam installer=null\npackage:com.ornek.masum installer=com.android.vending\npackage:com.whatsapp installer=com.android.vending")
elif k.startswith("dumpsys package com.ornek.benimuygulamam"):
    print("  Packages:\n    versionName=0.3-debug\n    firstInstallTime=2026-09-30 20:10:00\n    lastUpdateTime=2026-10-02 22:41:00\n    userId=10150")
elif k.startswith("dumpsys package com.ornek.masum"):
    print("    versionName=5.1\n    firstInstallTime=2025-03-01 10:00:00\n    lastUpdateTime=2025-06-01 09:00:00")
elif k.startswith("dumpsys package com.whatsapp"):
    print("    versionName=2.26.1\n    firstInstallTime=2025-01-10 10:00:00\n    lastUpdateTime=2026-08-01 09:00:00")
elif k.startswith("logcat -b crash"): f("logcat_crash.txt")
elif k.startswith("logcat -b events"): f("logcat_events.txt")
elif k.startswith("logcat -L"): sys.stderr.write("logcat: Logcat read failure\n"); sys.exit(1)
else: print("")
