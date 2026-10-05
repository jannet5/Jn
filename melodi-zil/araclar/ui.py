#!/usr/bin/env python3
# Emülatörde arayüzü adb + uiautomator ile süren yardımcı: metin bul, dokun, yaz, ekran görüntüsü al.
import subprocess, sys, re, time, xml.etree.ElementTree as ET  # alt süreç, argümanlar, regex, XML
import os  # ortam değişkeni
ADB = ["/home/user/Jn/android-sdk/platform-tools/adb", "-s", os.environ.get("EMU", "emulator-5554")]  # hedef cihaz (EMU ile değiştirilebilir)
def sh(*a, **k): return subprocess.run(ADB + ["shell"] + list(a), capture_output=True, text=True, **k).stdout  # adb shell
def dump():  # ekran ağacını XML olarak al
    for _ in range(5):  # birkaç deneme
        sh("uiautomator", "dump", "/sdcard/ui.xml"); x = sh("cat", "/sdcard/ui.xml")  # dump ve oku
        if "<hierarchy" in x: return ET.fromstring(x[x.index("<hierarchy"):])  # XML
        time.sleep(1)  # bekle
    raise SystemExit("ui dump alınamadı")  # hata
def nodes(): return [n for n in dump().iter("node")]  # tüm düğümler
def find(text=None, desc=None, cls=None, contains=False):  # metin/açıklama ile düğüm bul
    for n in nodes():
        t, d, c = n.get("text", ""), n.get("content-desc", ""), n.get("class", "")
        ok = True
        if text is not None: ok &= (text.lower() in t.lower()) if contains else (t == text)
        if desc is not None: ok &= (desc.lower() in d.lower()) if contains else (d == desc)
        if cls is not None: ok &= cls in c
        if ok: return n
    return None
def center(n):  # düğümün merkezi
    x1, y1, x2, y2 = map(int, re.findall(r"\d+", n.get("bounds")))
    return (x1 + x2) // 2, (y1 + y2) // 2
def tap(text=None, desc=None, contains=False, wait=1.5):  # metne dokun
    n = find(text=text, desc=desc, contains=contains)
    if n is None: raise SystemExit(f"bulunamadı: {text or desc}")
    x, y = center(n); sh("input", "tap", str(x), str(y)); time.sleep(wait); return x, y
def tap_xy(x, y, wait=1.5): sh("input", "tap", str(x), str(y)); time.sleep(wait)  # koordinata dokun
def type_text(s): sh("input", "text", s.replace(" ", "%s").replace("&", "\\&")); time.sleep(0.5)  # metin yaz
def screenshot(path): subprocess.run(ADB + ["exec-out", "screencap", "-p"], stdout=open(path, "wb")); print("ss", path)  # ekran görüntüsü
def texts(): return [n.get("text") for n in nodes() if n.get("text")]  # ekrandaki metinler
if __name__ == "__main__":  # komut satırı: ui.py tap "Başla" | ui.py ss dosya.png | ui.py texts
    cmd = sys.argv[1]
    if cmd == "tap": tap(sys.argv[2], contains=True)
    elif cmd == "desc": tap(desc=sys.argv[2], contains=True)
    elif cmd == "ss": screenshot(sys.argv[2])
    elif cmd == "texts": print("\n".join(texts()))
    elif cmd == "type": type_text(sys.argv[2])
    elif cmd == "xy": tap_xy(int(sys.argv[2]), int(sys.argv[3]))
