#!/usr/bin/env python3
"""Instagram profil kartı + veri çekici (giriş duvarı olmadan).

instagram.com/<kullanici>/embed/ herkese açık uç noktası üzerinden takipçi sayısı,
gönderi sayısı, ad ve son ~15 gönderiyi (tür + açıklama + küçük görsel) çeker.
Chromium bu alan adını proxy arkasında açamadığı için (ERR_TOO_MANY_RETRIES)
veriyi requests ile alır, gerçek verilerden bir profil kartı HTML'i üretir ve
onun ekran görüntüsünü alır. Bio metni embed ucunda yok; o ayrıca aranmalı.

Kullanım: python3 ig_al.py <ajans-adi> <instagram-kullanici>
Çıktı:   ss/<ajans-adi>-instagram.png  +  ss/<ajans-adi>-instagram.json
"""
import base64
import html
import io
import json
import os
import re
import sys

import requests
from PIL import Image
from playwright.sync_api import sync_playwright

HERE = os.path.dirname(os.path.abspath(__file__))
SS_DIR = os.path.join(HERE, "ss")
EXEC = "/opt/pw-browsers/chromium"
# Not: tam Chrome UA ile Instagram JS-render varyantı (verisiz) dönüyor; kısa UA sunucu-render (verili) varyantı veriyor
EMBED_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/128.0.0.0"
UA = ("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
      "(KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36")


def _json_str(raw: str) -> str:
    return json.loads('"' + raw.replace("\\\\", "\\") + '"').replace("\\/", "/")


def veri_cek(kullanici: str) -> dict:
    r = requests.get(f"https://www.instagram.com/{kullanici}/embed/", headers={"User-Agent": EMBED_UA}, timeout=40)
    s = r.text
    d = {"username": kullanici, "http": r.status_code, "kaynak": "instagram.com/embed"}
    if r.status_code != 200 or "followers_count" not in s:
        d["hata"] = "embed sayfası veri içermiyor (hesap gizli/yok ya da engel)"
        return d

    def num(k):
        m = re.search(r'\\"%s\\":(\d+)' % k, s)
        return int(m.group(1)) if m else None

    def st(k):
        m = re.search(r'\\"%s\\":\\"((?:[^"\\]|\\\\.)*?)\\"' % k, s)
        return _json_str(m.group(1)) if m else None

    d["followers"] = num("followers_count")
    d["posts"] = num("posts_count")
    d["full_name"] = st("full_name")
    d["is_verified"] = '\\"is_verified\\":true' in s
    d["profile_pic"] = st("profile_pic_url")
    # gönderiler: __typename, shortcode, display_url, caption text sırasıyla geçiyor
    posts = []
    for blok in re.split(r'\\"__typename\\":\\"', s)[1:]:
        tn = blok.split('\\"', 1)[0]
        if tn not in ("GraphImage", "GraphVideo", "GraphSidecar"):
            continue
        sc = re.search(r'\\"shortcode\\":\\"(\w+)\\"', blok)
        du = re.search(r'\\"display_url\\":\\"((?:[^"\\]|\\\\.)*?)\\"', blok)
        cap = re.search(r'\\"text\\":\\"((?:[^"\\]|\\\\.)*?)\\"', blok)
        if not sc:
            continue
        posts.append({
            "tur": {"GraphImage": "görsel", "GraphVideo": "video/reel", "GraphSidecar": "carousel"}[tn],
            "shortcode": sc.group(1),
            "url": f"https://www.instagram.com/p/{sc.group(1)}/",
            "thumb": _json_str(du.group(1)) if du else None,
            "caption": (_json_str(cap.group(1))[:300] if cap else ""),
        })
    # aynı shortcode'u tekrar etme
    gorulen, tekil = set(), []
    for p in posts:
        if p["shortcode"] not in gorulen:
            gorulen.add(p["shortcode"])
            tekil.append(p)
    d["son_gonderiler"] = tekil[:15]
    d["tur_dagilimi"] = {}
    for p in tekil[:15]:
        d["tur_dagilimi"][p["tur"]] = d["tur_dagilimi"].get(p["tur"], 0) + 1
    return d


def _img64(url: str, boyut=(300, 300)) -> str:
    try:
        r = requests.get(url, headers={"User-Agent": UA}, timeout=25)
        im = Image.open(io.BytesIO(r.content)).convert("RGB")
        im.thumbnail((boyut[0] * 2, boyut[1] * 2))
        w, h = im.size
        k = min(w, h)
        im = im.crop(((w - k) // 2, (h - k) // 2, (w - k) // 2 + k, (h - k) // 2 + k)).resize(boyut)
        b = io.BytesIO()
        im.save(b, "JPEG", quality=70)
        return "data:image/jpeg;base64," + base64.b64encode(b.getvalue()).decode()
    except Exception:
        return ""


def kart_html(d: dict) -> str:
    pp = _img64(d["profile_pic"], (150, 150)) if d.get("profile_pic") else ""
    kutular = []
    for p in d.get("son_gonderiler", [])[:12]:
        rozet = {"video/reel": "▶ Reel/Video", "carousel": "▣ Carousel", "görsel": "Görsel"}[p["tur"]]
        src = _img64(p["thumb"]) if p.get("thumb") else ""
        cap = html.escape(p["caption"][:90])
        kutular.append(
            f'<div class="k"><div class="im" style="background-image:url({src})"><span class="r">{rozet}</span></div>'
            f'<div class="c">{cap}</div></div>')
    ad = html.escape(d.get("full_name") or "")
    tick = ' <span class="v">✔</span>' if d.get("is_verified") else ""
    return f"""<!doctype html><html><head><meta charset="utf-8"><style>
body{{margin:0;background:#fff;font-family:-apple-system,Segoe UI,Roboto,Arial,sans-serif;color:#262626;width:1000px}}
.h{{display:flex;gap:40px;padding:36px 48px;border-bottom:1px solid #dbdbdb;align-items:center}}
.pp{{width:150px;height:150px;border-radius:50%;background:#efefef center/cover;background-image:url({pp});flex:none}}
.u{{font-size:28px;font-weight:400}} .n{{font-weight:600;margin-top:6px}} .v{{color:#0095f6}}
.s{{display:flex;gap:40px;margin:16px 0 0;font-size:16px}} .s b{{font-weight:600}}
.g{{display:grid;grid-template-columns:repeat(3,1fr);gap:6px;padding:12px 48px}}
.im{{aspect-ratio:1;background:#eee center/cover;position:relative}}
.r{{position:absolute;top:8px;left:8px;background:rgba(0,0,0,.65);color:#fff;font-size:12px;padding:3px 7px;border-radius:4px}}
.c{{font-size:11px;color:#555;padding:6px 2px;height:30px;overflow:hidden;line-height:15px}}
.f{{padding:10px 48px 20px;font-size:12px;color:#8e8e8e}}
</style></head><body>
<div class="h"><div class="pp"></div><div>
<div class="u">{html.escape(d['username'])}{tick}</div><div class="n">{ad}</div>
<div class="s"><span><b>{d.get('posts') or '?'}</b> gönderi</span><span><b>{d.get('followers') or '?'}</b> takipçi</span></div>
<div style="margin-top:8px;font-size:13px;color:#8e8e8e">Son gönderi türleri: {html.escape(', '.join(f'{v} {k}' for k, v in d.get('tur_dagilimi', {}).items()))}</div>
</div></div>
<div class="g">{''.join(kutular)}</div>
<div class="f">Kaynak: instagram.com/{html.escape(d['username'])}/embed/ (herkese açık uç) — giriş duvarı nedeniyle profil sayfası yerine gerçek veriden üretilmiş kart.</div>
</body></html>"""


def main():
    ad, kullanici = sys.argv[1], sys.argv[2]
    os.makedirs(SS_DIR, exist_ok=True)
    d = veri_cek(kullanici)
    with open(os.path.join(SS_DIR, f"{ad}-instagram.json"), "w", encoding="utf-8") as f:
        json.dump(d, f, ensure_ascii=False, indent=1)
    if "hata" in d:
        print(f"  [ig] {kullanici}: {d['hata']} -> SS alınamadı")
        return
    print(f"  [ig] {kullanici}: {d['followers']} takipçi, {d['posts']} gönderi, türler {d['tur_dagilimi']}")
    h = kart_html(d)
    out = os.path.join(SS_DIR, f"{ad}-instagram.png")
    with sync_playwright() as p:
        b = p.chromium.launch(executable_path=EXEC, headless=True, args=["--no-sandbox"])
        pg = b.new_page(viewport={"width": 1000, "height": 900})
        pg.set_content(h, wait_until="load")
        png = pg.screenshot(full_page=True)
        b.close()
    im = Image.open(io.BytesIO(png)).convert("RGB")
    buf = io.BytesIO()
    im.save(buf, "JPEG", quality=80)
    # PNG istendi: paletli PNG ile 400KB altına indir
    buf = io.BytesIO()
    im.quantize(colors=256, method=Image.Quantize.FASTOCTREE).save(buf, "PNG", optimize=True)
    if buf.tell() > 400 * 1024:
        im = im.resize((800, int(im.height * 0.8)))
        buf = io.BytesIO()
        im.quantize(colors=256, method=Image.Quantize.FASTOCTREE).save(buf, "PNG", optimize=True)
    open(out, "wb").write(buf.getvalue())
    print(f"  [ig] {out} ({buf.tell()//1024} KB)")


if __name__ == "__main__":
    main()
