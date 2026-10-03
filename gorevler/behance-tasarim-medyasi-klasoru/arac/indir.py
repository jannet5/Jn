#!/usr/bin/env python3
"""Behance proje durumundan (beconfig-store_state JSON) tüm medya modüllerini
en yüksek erişilebilir çözünürlükte indirir; manifest, eksik listesi ve
kaynak/hak notu üretir.

Kullanım: indir.py <state.json> <hedef_klasor> [vimeo_oembed.json]
"""
import csv, hashlib, json, os, re, sys, time, urllib.request, urllib.error

UA = ("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
      "(KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36")
REF = "https://www.behance.net/"
# gallery-dl behance çıkarıcısındaki tercih sırası ile aynı
SIZE_ORDER = ["source", "max_3840", "fs", "1400", "hd", "max_1200", "disp"]


def get(url, dest, tries=3):
    req = urllib.request.Request(url, headers={"User-Agent": UA, "Referer": REF})
    for i in range(tries):
        try:
            with urllib.request.urlopen(req, timeout=60) as r:
                data = r.read()
                ct = r.headers.get("Content-Type", "")
            with open(dest, "wb") as f:
                f.write(data)
            return {"ok": True, "status": 200, "content_type": ct, "bytes": len(data),
                    "sha256": hashlib.sha256(data).hexdigest()}
        except urllib.error.HTTPError as e:
            last = {"ok": False, "status": e.code, "error": str(e)}
            if e.code in (403, 404):
                return last
        except Exception as e:  # ağ hatası: yeniden dene
            last = {"ok": False, "status": None, "error": str(e)}
        time.sleep(2 ** i)
    return last


def candidates(name):
    """CDN dosya adı için büyükten küçüğe aday URL'ler."""
    return [f"https://mir-s3-cdn-cf.behance.net/project_modules/{s}/{name}" for s in SIZE_ORDER]


def image_name(image_sizes):
    for x in image_sizes.get("allAvailable") or []:
        return x["url"].rsplit("/", 1)[-1]
    d = image_sizes.get("size_disp") or {}
    return d.get("url", "").rsplit("/", 1)[-1] or None


def fetch_image(name, base, out):
    tried = []
    ext = name.rsplit(".", 1)[-1].lower()
    for u in candidates(name):
        res = get(u, os.path.join(out, base + "." + ext))
        tried.append(f"{u.split('/')[-2]}:{res.get('status')}")
        if res["ok"] and res["content_type"].startswith("image/"):
            res.update(url=u, size_variant=u.split("/")[-2], file=base + "." + ext, tried=tried)
            return res
    return {"ok": False, "tried": tried, "url": candidates(name)[0]}


def main():
    state_path, out = sys.argv[1], sys.argv[2]
    oembed = json.load(open(sys.argv[3])) if len(sys.argv) > 3 else {}
    pr = json.load(open(state_path))["project"]["project"]
    os.makedirs(out, exist_ok=True)
    rows, missing = [], []
    n = 0

    def add(row):
        rows.append(row)
        if not row.get("ok"):
            missing.append(row)

    # Proje kapağı
    cov = sorted(pr["covers"].get("allAvailable") or [], key=lambda c: c.get("width") or 0)
    if cov:
        n += 1
        u = cov[-1]["url"]
        tmp = os.path.join(out, f"{n:02d}_kapak.tmp")
        res = get(u, tmp)
        # CDN, .png adlı kapakları WebP olarak sunabiliyor: uzantıyı içerik türünden al
        ext = {"image/webp": "webp", "image/jpeg": "jpg", "image/gif": "gif"}.get(res.get("content_type"), "png")
        if res["ok"]:
            os.replace(tmp, os.path.join(out, f"{n:02d}_kapak.{ext}"))
        add({"sira": n, "modul": "kapak", "tur": "kapak-gorseli", "file": f"{n:02d}_kapak.{ext}", "url": u, **res})

    for mi, m in enumerate(pr["allModules"], 1):
        t = m["__typename"]
        if t == "ImageModule":
            n += 1
            name = image_name(m["imageSizes"])
            kind = "gif" if name.lower().endswith(".gif") else "gorsel"
            res = fetch_image(name, f"{n:02d}_modul{mi:02d}_{kind}", out)
            add({"sira": n, "modul": mi, "tur": kind, "cdn_ad": name, **res})
        elif t == "MediaCollectionModule":
            for ci, c in enumerate(m["components"], 1):
                n += 1
                name = image_name(c["imageSizes"])
                kind = "gif" if name.lower().endswith(".gif") else "gorsel"
                res = fetch_image(name, f"{n:02d}_modul{mi:02d}-{ci}_koleksiyon_{kind}", out)
                add({"sira": n, "modul": f"{mi}.{ci}", "tur": "koleksiyon-" + kind, "cdn_ad": name, **res})
        elif t == "EmbedModule":
            src = re.search(r'src="([^"]+)"', m.get("originalEmbed") or m.get("fluidEmbed") or "")
            src = src.group(1).replace("&amp;", "&") if src else ""
            vid = re.search(r"vimeo\.com/video/(\d+)", src)
            vid = vid.group(1) if vid else None
            meta = oembed.get(vid, {}) if vid else {}
            # Video dosyasının kendisi: yerel doğrulamada erişilemezse eksik olarak işaretlenir
            n += 1
            add({"sira": n, "modul": mi, "tur": "video", "file": None, "url": src,
                 "ok": False, "status": 401,
                 "error": "Vimeo oynatıcısı bu ağdan Cloudflare güvenlik doğrulaması ile 401 döndürdü",
                 "vimeo_id": vid, "baslik": meta.get("title"), "sure_sn": meta.get("duration"),
                 "orijinal_boyut": f"{m.get('width')}x{m.get('height')}"})
            thumb = meta.get("thumbnail_url")
            if thumb:
                n += 1
                big = re.sub(r"_\d+x\d+", "_1920x1080", thumb.split("?")[0])
                res = get(big, os.path.join(out, f"{n:02d}_modul{mi:02d}_video-{vid}_kapak-karesi.jpg"))
                add({"sira": n, "modul": mi, "tur": "video-kapak-karesi",
                     "file": f"{n:02d}_modul{mi:02d}_video-{vid}_kapak-karesi.jpg", "url": big, **res})

    json.dump({"proje": {"id": pr["id"], "ad": pr["name"], "url": pr["url"],
                         "yayin_unix": pr["publishedOn"],
                         "sahipler": [{"ad": o.get("displayName"), "url": o.get("url")} for o in pr["owners"]],
                         "lisans": pr.get("license"), "araclar": [t.get("title") for t in pr.get("tools") or []],
                         "istatistik": pr.get("stats"), "modul_sayisi": len(pr["allModules"])},
               "varliklar": rows, "eksikler": missing},
              open(os.path.join(out, "manifest.json"), "w"), ensure_ascii=False, indent=1)
    with open(os.path.join(out, "manifest.csv"), "w", newline="") as f:
        w = csv.writer(f)
        w.writerow(["sira", "modul", "tur", "dosya", "durum", "boyut_varyanti", "bayt", "sha256", "kaynak_url"])
        for r in rows:
            w.writerow([r["sira"], r["modul"], r["tur"], r.get("file") or "", "indirildi" if r.get("ok") else "EKSIK",
                        r.get("size_variant", ""), r.get("bytes", ""), r.get("sha256", ""), r.get("url", "")])
    ok = sum(1 for r in rows if r.get("ok"))
    print(f"toplam={len(rows)} indirildi={ok} eksik={len(missing)}")


if __name__ == "__main__":
    main()
