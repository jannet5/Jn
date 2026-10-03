"""Gerçek dosya doğrulaması: görsel açılıyor mu, boyutu yeterli mi, 4'ü birbirinden ve kaynaktan farklı mı.
'Bitti mi?' sorusunun cevabı yalnızca bu sayımlardan çıkar."""
from __future__ import annotations

from pathlib import Path

from PIL import Image

from .brain import validate_analysis
from .select import completeness
from .store import TARGET_POSTS, VARIANT_IDS, Workspace, read_json, write_json


def dhash(path: Path, size: int = 8) -> int:
    with Image.open(path) as im:
        im = im.convert("L").resize((size + 1, size), Image.LANCZOS)
        px = list(im.tobytes())
    bits = 0
    for r in range(size):
        for c in range(size):
            bits = (bits << 1) | (px[r * (size + 1) + c] > px[r * (size + 1) + c + 1])
    return bits


def hamming(a: int, b: int) -> int:
    return bin(a ^ b).count("1")


def verify_post(ws: Workspace, code: str, min_side: int = 512, min_pair_dist: int = 6,
                min_source_dist: int = 10) -> dict:
    res: dict = {"shortcode": code, "variants": {}, "problems": []}
    hashes = {}
    for vid in VARIANT_IDS:
        p = ws.variant_path(code, vid)
        info = {"exists": p.exists(), "ok": False}
        if p.exists():
            try:
                with Image.open(p) as im:
                    im.verify()
                with Image.open(p) as im:
                    info["size"] = list(im.size)
                if min(info["size"]) < min_side:
                    res["problems"].append(f"{vid} çok küçük {info['size']}")
                else:
                    hashes[vid] = dhash(p)
                    info["ok"] = True
            except Exception as e:  # bozuk dosya
                res["problems"].append(f"{vid} açılamadı: {e}")
        else:
            res["problems"].append(f"{vid} yok")
        res["variants"][vid] = info
    vids = list(hashes)
    for i in range(len(vids)):
        for j in range(i + 1, len(vids)):
            d = hamming(hashes[vids[i]], hashes[vids[j]])
            if d < min_pair_dist:
                res["problems"].append(f"{vids[i]} ile {vids[j]} neredeyse aynı (dHash mesafe {d})")
                res["variants"][vids[j]]["ok"] = False
    for src in ws.media_files(code)[:4]:
        sh = dhash(src)
        for vid, h in hashes.items():
            d = hamming(sh, h)
            if d < min_source_dist:
                res["problems"].append(f"{vid} kaynak görsele çok yakın (kopya şüphesi, mesafe {d})")
                res["variants"][vid]["ok"] = False
    res["verified_variants"] = sum(1 for v in res["variants"].values() if v["ok"])
    write_json(ws.verify_path(code), res)
    return res


def verify_all(ws: Workspace, **kw) -> dict:
    total = 0
    for post in ws.iter_posts(selected_only=True):
        total += verify_post(ws, post["shortcode"], **kw)["verified_variants"]
    return {"verified_images": total}


def status(ws: Workspace, target_posts: int = TARGET_POSTS) -> dict:
    c = {"posts_total": 0, "complete_records": 0, "url_https": 0, "photo": 0, "audio": 0, "caption": 0,
         "selected": 0, "analyses_valid": 0, "images_present": 0, "images_verified": 0, "posts_with_4_verified": 0}
    for post in ws.iter_posts():
        c["posts_total"] += 1
        comp = completeness(ws, post)
        for k, v in comp.items():
            c[k] += int(v)
        c["complete_records"] += int(all(comp.values()))
        if not post.get("selected"):
            continue
        c["selected"] += 1
        code = post["shortcode"]
        data = read_json(ws.analysis_path(code))
        if data and not validate_analysis(data, code):
            c["analyses_valid"] += 1
        c["images_present"] += sum(ws.variant_path(code, v).exists() for v in VARIANT_IDS)
        ver = read_json(ws.verify_path(code)) or {}
        n = ver.get("verified_variants", 0)
        c["images_verified"] += n
        c["posts_with_4_verified"] += int(n == 4)
    c["target_posts"] = target_posts
    c["target_images"] = target_posts * 4
    c["done"] = (c["selected"] >= target_posts and c["analyses_valid"] >= target_posts
                 and c["posts_with_4_verified"] >= target_posts)
    return c


def done_answer(c: dict) -> str:
    if c["done"]:
        return (f"Evet, bitti. {c['selected']} tutmuş gönderi (URL+fotoğraf+ses+açıklama kayıtlı), "
                f"{c['analyses_valid']} geçerli analiz, {c['images_verified']} doğrulanmış özgün görsel.")
    steps = []
    if c["selected"] < c["target_posts"]:
        steps.append(f"seçili eksiksiz gönderi {c['selected']}/{c['target_posts']} "
                     f"(toplanan {c['posts_total']}, eksiksiz kayıt {c['complete_records']})")
    if c["analyses_valid"] < c["target_posts"]:
        steps.append(f"analiz {c['analyses_valid']}/{c['target_posts']}")
    if c["posts_with_4_verified"] < c["target_posts"]:
        steps.append(f"doğrulanmış görsel {c['images_verified']}/{c['target_images']}")
    return "Henüz değil — kalan: " + "; ".join(steps) + ". Sonraki adım: `python -m viralforge run` ile devam."
