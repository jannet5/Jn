"""Uygulamanın kodlarını bağımsız oathtool (GNU OATH Toolkit) ile karşılaştırır.

GitHub parametreleri (docs.github.com): SHA1, 6 hane, 30 sn, base32 setup key.
Anahtar herkese açık bir örnektir (pyotp README), hiçbir hesaba ait değildir.
"""
import datetime
import os
import random
import subprocess
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "uygulama"))
import totp_masaustu as tm  # noqa: E402

ANAHTAR = "JBSWY3DPEHPK3PXP"
r = random.Random(30)
anlar = [59, 1111111109, 2000000000] + [r.randint(1_600_000_000, 2_200_000_000) for _ in range(47)]
hatali = 0
for t in anlar:
    zaman = datetime.datetime.fromtimestamp(t, datetime.timezone.utc).strftime("%Y-%m-%d %H:%M:%S UTC")
    oath = subprocess.run(["oathtool", "--totp=SHA1", "--base32", "--digits=6", "--time-step-size=30s",
                           "--now", zaman, ANAHTAR], capture_output=True, text=True, check=True).stdout.strip()
    uyg = tm.anahtar_coz(ANAHTAR).kod(t)
    hatali += uyg != oath
    print(f"{zaman}  uygulama={uyg}  oathtool={oath}  {'OK' if uyg == oath else 'FARKLI'}")
print(f"SONUÇ: {len(anlar) - hatali}/{len(anlar)} eşleşti")
sys.exit(1 if hatali else 0)
