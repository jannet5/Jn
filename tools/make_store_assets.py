#!/usr/bin/env python3
"""Store and ad graphics for one brand concept, drawn from the real app screenshots.

    ./gradlew :app:testSideloadDebugUnitTest -Proborazzi.test.record=true   # real screens -> app/build/shots(/magaza)
    python3 tools/make_store_assets.py 2

Reads docs/marka/konsept-N-belirtecler.json (colours, fonts, slogan) and the screenshots in
app/build/shots, writes PNGs to docs/marka/magaza/konsept-N/:

  play-ikon-512.png            Play icon
  play-one-cikan-1024x500.png  Play feature graphic
  play-ekran-1..6.png          Play phone screenshots, 1080x1920, with a caption each
  reklam-1080x1080.png         Feed ad (square)
  reklam-1080x1350.png         Feed ad (4:5)
  reklam-1080x1920.png         Story / Reels ad

No third-party logo is drawn anywhere (store and ad policies). Needs Pillow and cairosvg.
"""
import io
import json
import sys
from pathlib import Path

import cairosvg
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parent.parent
SHOTS = ROOT / "app/build/shots"
FONT_DIRS = [Path.home() / ".fonts", ROOT / "docs/marka/yazi"]
FONT_FILES = {
    "Bricolage Grotesque": "BricolageGrotesque[opsz,wdth,wght].ttf",
    "DM Sans": "DMSans[opsz,wght].ttf",
    "Manrope": "Manrope[wght].ttf",
    "Inter": "Inter[opsz,wght].ttf",
    "Space Grotesk": "SpaceGrotesk[wght].ttf",
    "Fraunces": "Fraunces[SOFT,WONK,opsz,wght].ttf",
    "Instrument Sans": "InstrumentSans[wdth,wght].ttf",
    "Sora": "Sora[wght].ttf",
}

CAPTIONS = [
    ("01-ana-ekran-kapali", "Tüm müşteri mesajların tek ekranda"),
    ("02-konusma-acik-mesajlar", "Dokun, mesajlar aynı kutuda açılsın"),
    ("04-resimli-mesaj", "Gelen resimler de kutuda görünür"),
    ("09-kanal-secici", "Hangi uygulamaları izleyeceğini sen seç"),
    ("03-konusma-tam-uzun", "Yanıtla ya da uygulamada aç"),
    ("13-koyu-tema", "Gece de rahat: koyu tema"),
]


def hex_rgb(value: str):
    value = value.lstrip("#")
    return tuple(int(value[i:i + 2], 16) for i in (0, 2, 4))


def find_font(family: str) -> Path:
    name = FONT_FILES[family]
    for directory in FONT_DIRS:
        if (directory / name).exists():
            return directory / name
    sys.exit(f"font {name} not found in {FONT_DIRS}")


def font(family: str, size: int, weight: int) -> ImageFont.FreeTypeFont:
    f = ImageFont.truetype(str(find_font(family)), size)
    axes = f.get_variation_axes()
    values = []
    for axis in axes:
        name = axis["name"] if isinstance(axis["name"], str) else axis["name"].decode()
        if "eight" in name:
            values.append(max(axis["minimum"], min(axis["maximum"], weight)))
        elif "ptical" in name:
            values.append(max(axis["minimum"], min(axis["maximum"], size)))
        else:
            values.append(axis["default"])
    f.set_variation_by_axes(values)
    return f


def wrap(draw: ImageDraw.ImageDraw, text: str, fnt, max_width: int):
    lines, line = [], ""
    for word in text.split():
        trial = (line + " " + word).strip()
        if draw.textlength(trial, font=fnt) <= max_width or not line:
            line = trial
        else:
            lines.append(line)
            line = word
    if line:
        lines.append(line)
    return lines


def paragraph(draw, xy, text, fnt, fill, max_width, line_height, align="left"):
    x, y = xy
    for line in wrap(draw, text, fnt, max_width):
        width = draw.textlength(line, font=fnt)
        draw.text((x + ((max_width - width) / 2 if align == "center" else 0), y), line, font=fnt, fill=fill)
        y += line_height
    return y


def mark(svg_path: Path, color: str, size: int) -> Image.Image:
    svg = svg_path.read_text(encoding="utf-8")
    import re
    svg = re.sub(r'fill="#[0-9A-Fa-f]{6}"', f'fill="{color}"', svg)
    png = cairosvg.svg2png(bytestring=svg.encode(), output_width=size, output_height=size)
    return Image.open(io.BytesIO(png)).convert("RGBA")


def lockup(img: Image.Image, svg_path: Path, color_hex: str, family: str, x: int, baseline: int, size: int, fill) -> int:
    """The brand's own S as the first letter, then "ende" in the wordmark font; returns the right edge."""
    glyph = mark(svg_path, color_hex, int(size * 1.3))
    box = glyph.split()[3].getbbox()
    glyph = glyph.crop(box)
    img.alpha_composite(glyph, (x, baseline - glyph.height))
    d = ImageDraw.Draw(img)
    fnt = font(family, size, 700)
    gap = int(size * 0.07)
    d.text((x + glyph.width + gap, baseline), "ende", font=fnt, fill=fill, anchor="ls")
    return x + glyph.width + gap + int(d.textlength("ende", font=fnt))


def phone(shot: Image.Image, width: int, crop_height: int = None) -> Image.Image:
    """The real screen in a plain rounded frame; crop_height cuts the bottom so a phone can bleed off an edge."""
    bezel = max(10, width // 40)
    radius = width // 9
    screen_w = width - 2 * bezel
    screen_h = int(shot.height * screen_w / shot.width)
    screen = shot.convert("RGB").resize((screen_w, screen_h), Image.LANCZOS)
    full_h = screen_h + 2 * bezel
    canvas = Image.new("RGBA", (width, full_h), (0, 0, 0, 0))
    d = ImageDraw.Draw(canvas)
    d.rounded_rectangle((0, 0, width - 1, full_h - 1), radius=radius, fill=(20, 18, 16, 255))
    mask = Image.new("L", (screen_w, screen_h), 0)
    ImageDraw.Draw(mask).rounded_rectangle((0, 0, screen_w - 1, screen_h - 1), radius=radius - bezel, fill=255)
    canvas.paste(screen, (bezel, bezel), mask)
    if crop_height:
        canvas = canvas.crop((0, 0, width, min(crop_height, full_h)))
    return canvas


def shadowed(canvas: Image.Image, image: Image.Image, xy, blur=40, offset=24, opacity=90):
    from PIL import ImageFilter
    shadow = Image.new("RGBA", canvas.size, (0, 0, 0, 0))
    alpha = image.split()[3].point(lambda v: opacity if v > 0 else 0)
    shadow.paste(Image.new("RGBA", image.size, (0, 0, 0, 255)), (xy[0], xy[1] + offset), alpha)
    shadow = shadow.filter(ImageFilter.GaussianBlur(blur))
    canvas.alpha_composite(shadow)
    canvas.alpha_composite(image, xy)


def main():
    if len(sys.argv) != 2 or not sys.argv[1].isdigit():
        sys.exit(__doc__)
    n = int(sys.argv[1])
    data = json.loads((ROOT / f"docs/marka/konsept-{n}-belirtecler.json").read_text(encoding="utf-8"))
    out = ROOT / f"docs/marka/magaza/konsept-{n}"
    out.mkdir(parents=True, exist_ok=True)

    base_colors = data["renkler"]
    ground = hex_rgb(base_colors["ikonArkaplan"])           # brand colour behind the mark
    paper = hex_rgb(base_colors["arkaplan"])
    ink = hex_rgb(base_colors["metin"])
    accent = hex_rgb(base_colors["vurgu"])
    on_accent = hex_rgb(base_colors["vurguUstu"])
    danger = hex_rgb(base_colors["tehlike"])
    heading, body = data["tipografi"]["baslik"], data["tipografi"]["govde"]
    slogan = data.get("slogan", "Mesajların Sende.")
    icon_svg = ROOT / f"docs/marka/konsept-{n}-ikon-on.svg"
    mono_svg = ROOT / f"docs/marka/konsept-{n}-ikon-mono.svg"
    paper_hex = "#%02X%02X%02X" % paper

    def shot(name: str) -> Image.Image:
        # Prefer the store set (invented channel names, no third-party brand), else the plain screenshots.
        path = SHOTS / "magaza" / f"{name}.png"
        if not path.exists():
            path = SHOTS / f"{name}.png"
        if not path.exists():
            sys.exit(f"missing {path}: run the screenshot tests first (see the header)")
        return Image.open(path)

    # ---- Play icon 512 ------------------------------------------------------------------
    icon = Image.new("RGBA", (512, 512), ground + (255,))
    glyph = mark(icon_svg, paper_hex, 512)
    icon.alpha_composite(glyph)
    icon.convert("RGB").save(out / "play-ikon-512.png")

    # ---- Play screenshots ---------------------------------------------------------------
    for i, (name, caption) in enumerate(CAPTIONS, start=1):
        dark_panel = i % 2 == 1
        bg = ground if dark_panel else paper
        fg = paper if dark_panel else ink
        img = Image.new("RGBA", (1080, 1920), bg + (255,))
        d = ImageDraw.Draw(img)
        end = paragraph(d, (80, 110), caption, font(heading, 78, 700), fg, 920, 92)
        scr = phone(shot(name), 820)
        top = max(end + 70, 420)
        shadowed(img, scr, ((1080 - scr.width) // 2, top))
        img.convert("RGB").save(out / f"play-ekran-{i}.png")

    # ---- Play feature graphic 1024x500 ----------------------------------------------------
    feature = Image.new("RGBA", (1024, 500), ground + (255,))
    d = ImageDraw.Draw(feature)
    lockup(feature, icon_svg, paper_hex, heading, 64, 150, 76, paper)
    paragraph(d, (64, 214), slogan, font(heading, 58, 700), paper, 520, 66)
    paragraph(d, (64, 340), "Müşteri bildirimlerin tek sade ekranda. Veriler telefonunda kalır.", font(body, 26, 400), tuple(min(255, c + 40) for c in paper), 480, 34)
    scr = phone(shot("01-ana-ekran-kapali"), 330)
    shadowed(feature, scr, (640, 60), blur=26, offset=14)
    feature.convert("RGB").save(out / "play-one-cikan-1024x500.png")

    # ---- Ads ------------------------------------------------------------------------------
    def ad(width, height, name, headline_size, phone_width, phone_top, stack):
        img = Image.new("RGBA", (width, height), ground + (255,))
        d = ImageDraw.Draw(img)
        margin = 72
        lockup(img, icon_svg, paper_hex, heading, margin, margin + 78, 62, paper)
        text_w = width - 2 * margin if stack else int(width * 0.52)
        y = margin + 170
        y = paragraph(d, (margin, y), "Tüm müşteri mesajların tek ekranda.", font(heading, headline_size, 700), paper, text_w, int(headline_size * 1.12))
        y = paragraph(d, (margin, y + 22), "Bildirimlerin tek yerde toplanır. Veriler telefonunda kalır.", font(body, 34 if stack else 30, 400), tuple(min(255, c + 45) for c in paper), text_w, 46)
        pill_font = font(body, 34, 600)
        label = "Yakında Google Play'de"
        pw = int(d.textlength(label, font=pill_font)) + 70
        py = y + 40
        d.rounded_rectangle((margin, py, margin + pw, py + 78), radius=39, fill=danger if n == 2 else accent)
        d.text((margin + 35, py + 17), label, font=pill_font, fill=paper if n == 2 else on_accent)
        scr = phone(shot("01-ana-ekran-kapali"), phone_width)
        px = (width - phone_width) // 2 if stack else width - phone_width - 56
        top = py + 78 + 56 if stack else phone_top
        shadowed(img, scr, (px, top), blur=36, offset=20)
        img.convert("RGB").save(out / name)

    ad(1080, 1080, "reklam-1080x1080.png", 62, 430, 150, stack=False)
    ad(1080, 1350, "reklam-1080x1350.png", 72, 560, 640, stack=True)
    ad(1080, 1920, "reklam-1080x1920.png", 84, 720, 880, stack=True)
    print("written to", out)


if __name__ == "__main__":
    main()
