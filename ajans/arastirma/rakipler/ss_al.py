#!/usr/bin/env python3
"""Rakip ajans ekran görüntüsü aracı.

Kullanım:
  python3 ss_al.py [--reduced-motion] [--bekle SN] <ajans-adi> <site-url> [instagram-kullanici]
  --reduced-motion: prefers-reduced-motion=reduce + animasyon/transition kapatma CSS'i (siyah/boş ekran için)
  --bekle SN: networkidle sonrası ek bekleme (8-10 sn önerilir) + ikinci lazy-load kaydırması
Çıktı: ss/<ajans-adi>-web.png, ss/<ajans-adi>-mobil.png, ss/<ajans-adi>-instagram.png
PNG'ler 1600px genişliği geçmez ve 400KB altına sıkıştırılır (PIL).
"""
import io
import os
import sys
import time

from PIL import Image
from playwright.sync_api import sync_playwright

HERE = os.path.dirname(os.path.abspath(__file__))
SS_DIR = os.path.join(HERE, "ss")
EXEC = "/opt/pw-browsers/chromium"
UA = ("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
      "(KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36")
MOB_UA = ("Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 "
          "(KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1")
MAX_W = 1600
MAX_BYTES = 400 * 1024
MAX_H = 6000  # çok uzun sayfaları kırp


def sikistir(png_bytes: bytes, out_path: str) -> int:
    im = Image.open(io.BytesIO(png_bytes)).convert("RGB")
    if im.height > MAX_H:
        im = im.crop((0, 0, im.width, MAX_H))
    if im.width > MAX_W:
        im = im.resize((MAX_W, int(im.height * MAX_W / im.width)), Image.LANCZOS)
    # Önce palet (256 renk) PNG dene, sığmazsa küçült
    scale = 1.0
    while True:
        w = int(im.width * scale)
        h = int(im.height * scale)
        cur = im if scale == 1.0 else im.resize((w, h), Image.LANCZOS)
        buf = io.BytesIO()
        cur.quantize(colors=256, method=Image.Quantize.FASTOCTREE).save(
            buf, "PNG", optimize=True)
        if buf.tell() <= MAX_BYTES or scale < 0.3:
            break
        scale *= 0.8
    with open(out_path, "wb") as f:
        f.write(buf.getvalue())
    return buf.tell()


def kapat_cerezler(page):
    """Yaygın çerez/popup kapatma denemeleri."""
    secimler = [
        "button:has-text('Kabul')", "button:has-text('Kabul Et')", "button:has-text('Tümünü Kabul')",
        "button:has-text('Accept')", "button:has-text('Accept all')", "button:has-text('Accept All')",
        "button:has-text('I agree')", "button:has-text('Got it')", "button:has-text('Tamam')",
        "button:has-text('Anladım')", "#onetrust-accept-btn-handler", ".cky-btn-accept",
        "button:has-text('Allow all')", "button:has-text('OK')",
    ]
    for s in secimler:
        try:
            loc = page.locator(s).first
            if loc.is_visible(timeout=400):
                loc.click(timeout=1000)
                time.sleep(0.4)
        except Exception:
            pass


def lazy_kaydir(page, adim=900, maks=16000):
    """Lazy-load görseller için sayfayı aşağı kaydır, sonra başa dön."""
    try:
        yuk = page.evaluate("document.body.scrollHeight")
        y = 0
        while y < min(yuk, maks):
            page.evaluate(f"window.scrollTo(0, {y})")
            time.sleep(0.1)
            y += adim
            yuk = page.evaluate("document.body.scrollHeight")
        page.evaluate("window.scrollTo(0, 0)")
        time.sleep(0.6)
    except Exception:
        pass


AYAR = {"reduced": False, "bekle": 0.0}


def site_ss(browser, url, out_base):
    sonuc = {}
    for ad, vp, ua, mobil in (("web", (1440, 900), UA, False), ("mobil", (390, 844), MOB_UA, True)):
        ctx = browser.new_context(viewport={"width": vp[0], "height": vp[1]}, user_agent=ua,
                                  is_mobile=mobil, has_touch=mobil, device_scale_factor=1,
                                  locale="tr-TR", ignore_https_errors=True,
                                  reduced_motion="reduce" if AYAR["reduced"] else "no-preference")
        page = ctx.new_page()
        try:
            page.goto(url, wait_until="domcontentloaded", timeout=45000)
            try:
                page.wait_for_load_state("networkidle", timeout=15000 if AYAR["bekle"] else 8000)
            except Exception:
                pass
            if AYAR["reduced"]:
                try:
                    page.emulate_media(reduced_motion="reduce")
                    page.add_style_tag(content="*,*::before,*::after{animation-duration:0s!important;"
                                       "animation-delay:0s!important;transition:none!important;}"
                                       "[data-aos],.elementor-invisible,.wow,.reveal{opacity:1!important;"
                                       "visibility:visible!important;transform:none!important;}")
                except Exception:
                    pass
            if AYAR["bekle"]:
                time.sleep(AYAR["bekle"])
            kapat_cerezler(page)
            lazy_kaydir(page)
            if AYAR["bekle"]:
                lazy_kaydir(page, adim=600)
                time.sleep(2)
            if mobil:
                # Taşan (yatay overflow) sayfalarda mobil SS'i viewport genişliğine kırp
                yuk = min(page.evaluate("document.documentElement.scrollHeight"), MAX_H)
                png = page.screenshot(full_page=True, timeout=60000,
                                      clip={"x": 0, "y": 0, "width": vp[0], "height": yuk})
            else:
                png = page.screenshot(full_page=True, timeout=60000)
            out = f"{out_base}-{ad}.png"
            n = sikistir(png, out)
            sonuc[ad] = (out, n)
            print(f"  [{ad}] {out} ({n//1024} KB)")
        except Exception as e:
            print(f"  [{ad}] HATA: {e}")
            sonuc[ad] = None
        finally:
            ctx.close()
    return sonuc


def instagram_ss(browser, kullanici, out_base):
    adaylar = [
        (f"https://www.instagram.com/{kullanici}/", "instagram"),
        (f"https://imginn.com/{kullanici}/", "imginn"),
        (f"https://www.picuki.com/profile/{kullanici}", "picuki"),
        (f"https://dumpoir.com/v/{kullanici}", "dumpoir"),
    ]
    for url, kaynak in adaylar:
        ctx = browser.new_context(viewport={"width": 1200, "height": 900}, user_agent=UA,
                                  locale="en-US", ignore_https_errors=True)
        page = ctx.new_page()
        try:
            page.goto(url, wait_until="domcontentloaded", timeout=40000)
            try:
                page.wait_for_load_state("networkidle", timeout=10000)
            except Exception:
                pass
            kapat_cerezler(page)
            time.sleep(1.5)
            html = page.content().lower()
            basarisiz = (
                ("instagram.com" in url and ("login" in page.url or "giriş yap" in html and "takipçi" not in html
                                             or ("log in" in html and "followers" not in html)))
                or "page not found" in html and "followers" not in html
                or "sayfa bulunamadı" in html
                or "sorry, this page isn't available" in html
                or ("cloudflare" in html and "followers" not in html)
                or ("just a moment" in html)
                or (("followers" not in html) and ("takipçi" not in html) and ("abonnés" not in html))
            )
            if basarisiz:
                print(f"  [ig:{kaynak}] uygun değil ({page.url})")
                continue
            lazy_kaydir(page, maks=4000)
            png = page.screenshot(full_page=False, timeout=60000, clip={"x": 0, "y": 0, "width": 1200, "height": 2200})
            out = f"{out_base}-instagram.png"
            n = sikistir(png, out)
            print(f"  [ig:{kaynak}] {out} ({n//1024} KB)")
            return out, kaynak
        except Exception as e:
            print(f"  [ig:{kaynak}] HATA: {e}")
        finally:
            ctx.close()
    print("  [ig] SS alınamadı")
    return None, None


def main():
    argv = []
    it = iter(sys.argv[1:])
    for a in it:
        if a == "--reduced-motion":
            AYAR["reduced"] = True
        elif a == "--bekle":
            AYAR["bekle"] = float(next(it))
        else:
            argv.append(a)
    if len(argv) < 2:
        print(__doc__)
        sys.exit(1)
    ad, url = argv[0], argv[1]
    ig = argv[2] if len(argv) > 2 else None
    os.makedirs(SS_DIR, exist_ok=True)
    out_base = os.path.join(SS_DIR, ad)
    with sync_playwright() as p:
        browser = p.chromium.launch(executable_path=EXEC, headless=True,
                                    args=["--disable-blink-features=AutomationControlled", "--no-sandbox"])
        print(f"== {ad}: {url}")
        site_ss(browser, url, out_base)
        if ig:
            print("  [ig] Instagram için ig_al.py kullan (embed verisinden kart)")
        browser.close()


if __name__ == "__main__":
    main()
