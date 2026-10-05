# Slide Generation Pipeline

Complete working Python generator. Copy, adapt, run.

## Dependencies

```bash
pip install playwright --break-system-packages
playwright install chromium
```

## Full Generator Script

```python
#!/usr/bin/env python3
"""
carousel_generator.py
Usage: python3 carousel_generator.py --day 6 --out ./output
"""

import asyncio, json, os, argparse
from playwright.async_api import async_playwright


# ── LOAD DESIGN SYSTEM ────────────────────────────────────────────────────────

def load_design_system(ds_path="design-system.json", fonts_path="fonts.json"):
    with open(ds_path) as f: DS = json.load(f)
    with open(fonts_path) as f: F = json.load(f)
    return DS, F


# ── FONT CSS ──────────────────────────────────────────────────────────────────

def build_font_css(F):
    return f"""
@font-face{{font-family:'Inter';font-weight:400;src:url('data:font/woff2;base64,{F["inter_regular"]}') format('woff2');}}
@font-face{{font-family:'Inter';font-weight:600;src:url('data:font/woff2;base64,{F["inter_600"]}') format('woff2');}}
@font-face{{font-family:'Inter';font-weight:700;src:url('data:font/woff2;base64,{F["inter_bold"]}') format('woff2');}}
@font-face{{font-family:'Caveat';font-weight:700;src:url('data:font/woff2;base64,{F["caveat_bold"]}') format('woff2');}}
"""


# ── GHOST NUMBER ──────────────────────────────────────────────────────────────

def ghost_value(DS, slide_type, slide_pos, total):
    rule = DS["ghost_numbers"].get(slide_type, "none")
    if rule == "total_slide_count": return str(total).zfill(2)
    if rule == "slide_position":    return str(slide_pos).zfill(2)
    return None


# ── BASE HTML WRAPPER ─────────────────────────────────────────────────────────

def slide_html(DS, F, content):
    C   = DS["colors"]
    CSS = DS["css_toolkit"]
    fc  = build_font_css(F)
    return f"""<!DOCTYPE html><html><head><meta charset="UTF-8"><style>
{fc}
*{{margin:0;padding:0;box-sizing:border-box;}}
body{{background:{C["bg"]};}}
.slide{{ {CSS["slide_base"]}; }}
.ey{{ {CSS["eyebrow"]}; }}
.wm{{ {CSS["wordmark"]}; }}
.sn{{ {CSS["slide_number"]}; }}
.card{{ {CSS["card"]}; }}
.ca{{ {CSS["card_accent"]}; }}
.gh{{ {CSS["ghost_number"]}; }}
</style></head><body>
<div class="slide" id="s1">
{content}
</div></body></html>"""


# ── SLIDE BUILDERS ────────────────────────────────────────────────────────────

def cover_slide(DS, F, eyebrow, headline, sub, total):
    C   = DS["colors"]
    T   = DS["typography"]["scale"]
    CSS = DS["css_toolkit"]
    SVG = DS["svg_patterns"]
    gh  = ghost_value(DS, "cover", 1, total)

    wavy = f'<svg style="display:block;" viewBox="{SVG["wavy_large"]["viewBox"]}" height="{SVG["wavy_large"]["height"]}" width="{SVG["wavy_large"]["width"]}"><path d="{SVG["wavy_large"]["path"]}" fill="none" stroke="{SVG["wavy_large"]["stroke"]}" stroke-width="{SVG["wavy_large"]["stroke_width"]}" stroke-linecap="round"/></svg>'

    content = f"""
  <div class="ey">{eyebrow}</div>
  <div style="font-size:{T["cover_headline"]["size"]};font-weight:{T["cover_headline"]["weight"]};color:{C["ink"]};line-height:{T["cover_headline"]["line_height"]};letter-spacing:{T["cover_headline"]["letter_spacing"]};margin-bottom:16px;">{headline}</div>
  {wavy}
  <div style="font-size:{T["body"]["size"]};color:{C["slate"]};line-height:{T["body"]["line_height"]};margin-top:48px;max-width:680px;">{sub}</div>
  {"<div class='gh'>" + gh + "</div>" if gh else ""}
  <span class="wm">YOURBRAND</span>
  <div style="{CSS["swipe_hint"]}">swipe →</div>"""

    return slide_html(DS, F, content)


def body_card_slide(DS, F, eyebrow, headline, body, pos, total, badge=None):
    C   = DS["colors"]
    T   = DS["typography"]["scale"]
    CSS = DS["css_toolkit"]
    gh  = ghost_value(DS, "body_card", pos, total)

    badge_html = ""
    if badge == "THEN":
        badge_html = f'<div style="{CSS["then_badge"]};margin-bottom:28px;">THEN</div>'
    elif badge == "NOW":
        badge_html = f'<div style="{CSS["now_badge"]};margin-bottom:28px;">NOW</div>'

    content = f"""
  <div class="ey">{eyebrow}</div>
  <div class="card">
    <div class="ca"></div>
    {badge_html}
    <div style="font-size:{T["slide_headline"]["size"]};font-weight:{T["slide_headline"]["weight"]};color:{C["ink"]};line-height:{T["slide_headline"]["line_height"]};letter-spacing:{T["slide_headline"]["letter_spacing"]};margin-bottom:24px;">{headline}</div>
    <div style="font-size:{T["body"]["size"]};color:{C["slate"]};line-height:{T["body"]["line_height"]};">{body}</div>
  </div>
  {"<div class='gh'>" + gh + "</div>" if gh else ""}
  <span class="wm">YOURBRAND</span>
  <span class="sn">{str(pos).zfill(2)} / {str(total).zfill(2)}</span>"""

    return slide_html(DS, F, content)


def cta_slide(DS, F, eyebrow, headline, sub, total, brand_url):
    C   = DS["colors"]
    T   = DS["typography"]["scale"]
    CSS = DS["css_toolkit"]

    content = f"""
  <div style="{CSS["orange_dot_large"]}"></div>
  <div style="{CSS["orange_dot_small"]}"></div>
  <div class="ey">{eyebrow}</div>
  <div style="font-size:92px;font-weight:700;color:{C["ink"]};line-height:1.0;letter-spacing:-0.03em;margin-bottom:28px;">{headline}</div>
  <div style="font-size:{T["body"]["size"]};color:{C["slate"]};line-height:{T["body"]["line_height"]};max-width:680px;margin-bottom:64px;">{sub}</div>
  <div style="{CSS["pill_button"]}">{brand_url} <span style="opacity:0.5;font-size:22px;">→</span></div>
  <span class="wm">YOURBRAND</span>
  <span class="sn">{str(total).zfill(2)} / {str(total).zfill(2)}</span>"""

    return slide_html(DS, F, content)


# ── PLAYWRIGHT SCREENSHOT ─────────────────────────────────────────────────────

async def shoot(slides, output_dir, canvas):
    w = int(canvas["width"].replace("px", ""))
    h = int(canvas["height"].replace("px", ""))
    os.makedirs(output_dir, exist_ok=True)
    paths = []
    async with async_playwright() as p:
        browser = await p.chromium.launch()
        for n, html in slides.items():
            page = await browser.new_page(viewport={"width": w, "height": h})
            await page.set_content(html)
            await page.wait_for_timeout(900)
            path = os.path.join(output_dir, f"{str(n).zfill(2)}.png")
            await page.locator("#s1").screenshot(path=path, type="png")
            paths.append(path)
            print(f"  slide {str(n).zfill(2)}.png ✓")
        await browser.close()
    return paths


# ── MAIN ──────────────────────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--day", type=int, required=True)
    parser.add_argument("--out", default="./output")
    args = parser.parse_args()

    DS, F = load_design_system()
    output_dir = os.path.join(args.out, f"day{args.day}")

    # Build slides — replace this section with your actual post content
    # Example: a 4-slide Layout A carousel
    total = 4
    slides = {
        1: cover_slide(DS, F,
            eyebrow="Brand · Hook",
            headline="Your headline<br>goes here.",
            sub="Supporting sentence. Keep it short.",
            total=total),
        2: body_card_slide(DS, F,
            eyebrow="Point 01",
            headline="Feature or benefit.",
            body="One idea per slide. Under 20 words of body copy.",
            pos=2, total=total),
        3: body_card_slide(DS, F,
            eyebrow="Point 02",
            headline="Second feature.",
            body="Another idea. Keep it tight.",
            pos=3, total=total),
        4: cta_slide(DS, F,
            eyebrow="Brand · Call to action",
            headline="First file<br>is free.",
            sub="No card. No setup. Upload any audio and get back everything — in moments.",
            total=total,
            brand_url="brand.app"),
    }

    paths = asyncio.run(shoot(slides, output_dir, DS["canvas"]))
    print(f"\nDone. {len(paths)} slides → {output_dir}/")


if __name__ == "__main__":
    main()
```

## Running It

```bash
python3 carousel_generator.py --day 6 --out ./output
```

## Adapting for a New Post

Replace the `slides` dict in `main()` with your actual content. Each slide is built by calling one of the slide builder functions:

- `cover_slide(DS, F, eyebrow, headline, sub, total)` 
- `body_card_slide(DS, F, eyebrow, headline, body, pos, total, badge=None)`
- `cta_slide(DS, F, eyebrow, headline, sub, total, brand_url)`

For additional slide types (body_stat, body_step, body_strikethrough, grid_static), build equivalent functions following the same pattern: extract values from DS and F, compose an HTML string, return `slide_html(DS, F, content)`.

## Output Location

Slides land in `{out}/day{N}/01.png`, `02.png`, etc.

Rename per `DS["generation"]["naming"]`: `day{N}-{slideNum_padded}.png`
