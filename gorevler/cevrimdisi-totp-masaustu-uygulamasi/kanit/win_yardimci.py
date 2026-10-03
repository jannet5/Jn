"""Windows (Wine) tarafı yardımcı: pencereyi X düğmesi gibi kapatır (WM_CLOSE) ve panoyu okur.

Kullanım:  python win_yardimci.py kapat | pano
"""
import ctypes
import sys
import time
from ctypes import wintypes

user32 = ctypes.windll.user32
WM_CLOSE = 0x0010


def pencereler(onek="Çevrimdışı TOTP"):
    bulunan = []

    @ctypes.WINFUNCTYPE(wintypes.BOOL, wintypes.HWND, wintypes.LPARAM)
    def geri(hwnd, _):
        uzunluk = user32.GetWindowTextLengthW(hwnd)
        tampon = ctypes.create_unicode_buffer(uzunluk + 1)
        user32.GetWindowTextW(hwnd, tampon, uzunluk + 1)
        if tampon.value.startswith(onek) and user32.IsWindowVisible(hwnd):
            bulunan.append(hwnd)
        return True

    user32.EnumWindows(geri, 0)
    return bulunan


if sys.argv[1] == "kapat":
    h = pencereler()
    for hwnd in h:
        user32.PostMessageW(hwnd, WM_CLOSE, 0, 0)
    time.sleep(3)
    print(f"WM_CLOSE gönderilen pencere={len(h)} kalan pencere={len(pencereler())}")
elif sys.argv[1] == "pano":
    import tkinter
    r = tkinter.Tk()
    r.withdraw()
    try:
        print(repr(r.clipboard_get()))
    except tkinter.TclError:
        print(repr(""))
    r.destroy()
