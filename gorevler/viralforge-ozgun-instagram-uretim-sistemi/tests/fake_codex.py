#!/usr/bin/env python3
"""TEST TAKLİDİ — gerçek Codex değildir. Yalnızca boru hattı mekaniğini (komut satırı, şema, dosya yolları,
limit/devam, ücret koruması) çevrimdışı sınamak için `VF_CODEX_BIN` ile kullanılır."""
import hashlib
import json
import os
import random
import re
import sys
from pathlib import Path

args = sys.argv[1:]
# Konsol/locale kodlamasından bağımsız: stdin baytları UTF-8 olarak çözülür (gerçek Codex de UTF-8 bekler).
prompt = sys.stdin.buffer.read().decode("utf-8") if "-" in args else ""
log = os.environ.get("FAKE_CODEX_LOG")
if log:
    with open(log, "a", encoding="utf-8") as fh:
        fh.write(json.dumps({"args": args, "api_key_seen": bool(os.environ.get("OPENAI_API_KEY"))}) + "\n")

limit_after = os.environ.get("FAKE_CODEX_LIMIT_AFTER")
counter = os.environ.get("FAKE_CODEX_COUNTER")
if limit_after and counter:
    # Paralel çağrılarda güvenli sayaç: her çağrı O_APPEND ile 1 bayt ekler, sıra = dosya boyu.
    fd = os.open(counter, os.O_WRONLY | os.O_CREAT | os.O_APPEND)
    os.write(fd, b".")
    os.close(fd)
    n = os.path.getsize(counter) - 1
    if n >= int(limit_after):
        print("ERROR: You've hit your usage limit. Try again at 18:00.", file=sys.stderr)
        sys.exit(1)

if os.environ.get("FAKE_CODEX_NO_AUTH"):
    print("unexpected status 401 Unauthorized: Missing bearer or basic authentication in header", file=sys.stderr)
    sys.exit(1)

if os.environ.get("FAKE_CODEX_SLEEP"):
    import time
    time.sleep(float(os.environ["FAKE_CODEX_SLEEP"]))

if args[:2] == ["login", "status"]:
    if os.environ.get("FAKE_CODEX_LOGGED_OUT"):
        print("Not logged in")
        sys.exit(1)
    print("Logged in using ChatGPT (fake)")
    sys.exit(0)

if "--output-schema" in args:
    out = args[args.index("-o") + 1]
    code = re.search(r'shortcode alanı tam olarak "([^"]+)"', prompt).group(1)
    subjects = ["a lighthouse keeper's breakfast table at dawn", "neon-lit rooftop garden with koi pond",
                "paper-craft mountain village under snowfall", "macro shot of dew on a bicycle bell"]
    palettes = ["teal and amber", "magenta cyan duotone", "pastel watercolor", "monochrome high contrast"]
    data = {
        "shortcode": code,
        "why_it_worked": {"visual": "tek odak, yüksek kontrast", "audio": "bilinmiyor" if "bilinmiyor" in prompt else "ritimli trend ses",
                          "caption": "merak boşluğu bırakan kısa soru", "combined": "görsel kanca + soru + ritim birlikte kaydırmayı durduruyor"},
        "success_factors": [{"factor": f"faktör {i}", "evidence": "test", "modality": m}
                            for i, m in enumerate(["visual", "caption", "combined"])],
        "audience_emotion": "merak", "hook_type": "soru",
        "variants": [{"id": f"v{i + 1}", "angle": f"açı {i + 1}", "how_it_differs": "farklı sahne",
                      "image_prompt": f"Photorealistic scene of {subjects[i]}, {palettes[i]} palette, variant {i + 1} for {code}, shallow depth of field",
                      "caption_draft": "Sence hangisi?", "audio_suggestion": "lofi", "aspect_ratio": "1:1"} for i in range(4)],
        "originality_guardrails": ["kişi/logo kopyalanmadı"], "confidence": 0.5,
    }
    Path(out).write_text(json.dumps(data, ensure_ascii=False), encoding="utf-8")
    sys.exit(0)

m = re.search(r"copy the final PNG to this exact path: (.+?\.png)", prompt)
if m:
    from PIL import Image, ImageDraw
    seed = int(hashlib.sha256(prompt.encode()).hexdigest()[:8], 16)
    rnd = random.Random(seed)
    side = int(os.environ.get("FAKE_CODEX_SIDE", "512"))
    im = Image.new("RGB", (side, side), tuple(rnd.randrange(256) for _ in range(3)))
    d = ImageDraw.Draw(im)
    for _ in range(12):
        x0, y0 = rnd.randrange(side), rnd.randrange(side)
        d.rectangle([x0, y0, x0 + rnd.randrange(side // 2), y0 + rnd.randrange(side // 2)],
                    fill=tuple(rnd.randrange(256) for _ in range(3)))
    Path(m.group(1)).parent.mkdir(parents=True, exist_ok=True)
    im.save(m.group(1))
    print(m.group(1))
    sys.exit(0)

print("fake codex: anlaşılmayan çağrı", file=sys.stderr)
sys.exit(2)
