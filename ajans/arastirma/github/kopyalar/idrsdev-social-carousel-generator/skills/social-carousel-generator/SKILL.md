---
name: social-carousel-generator
description: Generate brand-consistent social media carousel slides as PNG images from a design system JSON. Use this skill whenever the user wants to create, generate, or produce carousel slides, social media posts with multiple slides, Instagram carousels, or any slide images driven by a brand design system. Also trigger when the user mentions slide generation, carousel production, image generation for a content calendar day, or asks to "generate images for day X". This skill outputs PNG files for social posting — not HTML carousels for web embedding. Requires design-system.json in the project root. Fonts are downloaded automatically via scripts/download_fonts.py.
---

# Social Carousel Generator

Generates carousel slides as 1080×1350px PNG files from a brand design system JSON.
All design values (colors, typography, spacing, CSS patterns) come from `design-system.json`.
Output: per-slide PNGs.

> **PNG output only.** This skill generates static images for social media posting (Instagram, LinkedIn, etc.).
> If you need an interactive HTML carousel for a website, use a different approach.

---

## Execution Rules

- Write `_generate.py` in the output directory. Overwrite it on each run — never delete it.
- Never use `python3 -c "..."` for generation — the base64 font data is too large for inline commands.
- Never ask the user to review or approve the generator script — just write it and run it.
- The script is reusable infrastructure. See `references/generate.sample.py` for a working example.

---

## Setup (run once per machine)

```bash
pip install playwright --break-system-packages
playwright install chromium
```

---

## Required Files

| File | Purpose | How to get it |
|------|---------|---------------|
| `design-system.json` | All brand tokens, CSS, slide types | See `references/design-system-schema.md` |
| `fonts.json` | Fonts as base64 (Inter + Caveat) | Run `python3 scripts/download_fonts.py` |

**fonts.json is auto-generated — never edit it manually.**
If it exists in your project already, skip the download step.

---

## Startup Check

**1. design-system.json**
If missing:
  1. Ask the user if they have a reference slide image or brand sample to generate it from.
  2. If they don't, copy `references/design-system.sample.json` to the project root as `design-system.json` and ask the user to update `meta.wordmark_text` with their brand name.
  3. See `references/design-system-schema.md` for the full schema.
If present: proceed silently — do not ask to review it unless the user raises it.

**2. fonts.json**
```bash
# If missing — downloads fonts specified in design-system.json automatically:
python3 scripts/download_fonts.py

# To verify an existing fonts.json:
python3 scripts/verify_fonts.py
```

**3. Layout check per post**
- Layout A or B → Visual Brief is sufficient. Generate directly.
- Layout C or D (THEN/NOW, Hub & Spoke) → write slide-by-slide spec first, then generate.

---

## Reading the Design System

```python
import json

with open("design-system.json") as f: DS = json.load(f)
with open("fonts.json") as f:         F  = json.load(f)

WM  = DS["meta"]["wordmark_text"] # brand name for bottom-right wordmark
C   = DS["colors"]               # hex color tokens
T   = DS["typography"]["scale"]  # type scale — concrete px values only
CSS = DS["css_toolkit"]          # ready-to-use CSS strings
SVG = DS["svg_patterns"]         # wavy underline paths, dashed circle, etc.
GH  = DS["ghost_numbers"]        # ghost value rules per slide type
CVS = DS["canvas"]               # width, height, padding
```

**Critical:** All values in `T` must be concrete px strings — never ranges.
`T["cover_headline"]["size"]` → `"104px"` ✓
`T["cover_headline"]["size"]` → `"104-108px"` ✗ — malformed, fix before generating.

---

## Font Face CSS

Fonts are embedded as base64 — never use network URLs in headless Playwright.

```python
def build_font_css(DS: dict, F: dict) -> str:
    primary    = DS["typography"].get("font_primary",    "Inter")
    annotation = DS["typography"].get("font_annotation", "Caveat")
    pk = primary.lower().replace(" ", "_")
    ak = annotation.lower().replace(" ", "_")
    return f"""
@font-face{{font-family:'{primary}';font-weight:400;src:url('data:font/woff2;base64,{F[pk+"_regular"]}') format('woff2');}}
@font-face{{font-family:'{primary}';font-weight:600;src:url('data:font/woff2;base64,{F[pk+"_600"]}') format('woff2');}}
@font-face{{font-family:'{primary}';font-weight:700;src:url('data:font/woff2;base64,{F[pk+"_bold"]}') format('woff2');}}
@font-face{{font-family:'{annotation}';font-weight:700;src:url('data:font/woff2;base64,{F[ak+"_bold"]}') format('woff2');}}
"""
```

Font key naming: `{family_lowercase}_{weight_label}` — e.g. `inter_regular`, `inter_600`, `inter_bold`, `caveat_bold`.

---

## Base Slide HTML

Every slide is a full HTML document. Screenshot target: `#s1`.

```python
def slide_html(DS: dict, F: dict, content: str) -> str:
    C   = DS["colors"]
    CSS = DS["css_toolkit"]
    return f"""<!DOCTYPE html><html><head><meta charset="UTF-8"><style>
{build_font_css(DS, F)}
*{{margin:0;padding:0;box-sizing:border-box;}}
body{{background:{C["bg"]};}}
.slide{{ {CSS["slide_base"]}; }}
.ey{{ {CSS["eyebrow"]}; }}
.wm{{ {CSS["wordmark"]}; }}
.sn{{ {CSS["slide_number"]}; }}
.card{{ {CSS["card"]}; }}
.ca{{ {CSS["card_accent"]}; }}
</style></head><body>
<div class="slide" id="s1">{content}</div>
</body></html>"""
```

---

## Slide Types

Compose each type using CSS strings from the design system. Never invent values.

### cover
```
eyebrow → headline → wavy_underline → sub → ghost(total) → wordmark → swipe_hint
Ghost:  CSS["ghost_number"] at 320px, full-slide
Wavy:   SVG["wavy_large"]["path"] + stroke + stroke_width
Swipe:  CSS["swipe_hint"] — annotation font, bottom-center
```

### body_card
```
eyebrow → card[accent + optional_badge + headline + body] → ghost(position) → wordmark + slide_number
Ghost:  CSS["ghost_number"] at 320px, on the slide background (OUTSIDE the card)
Accent: CSS["card_accent"] — 5px left border, top 20% card height only
```

### body_stat
```
eyebrow → large_number + unit → orange_rule → body → ghost(position) → wordmark + slide_number
Ghost:  CSS["ghost_number"] at 320px, full-slide
Rule:   CSS["orange_rule"]
```

### body_step
```
eyebrow → card[accent + headline + body] → ghost(position) → wordmark + slide_number
Ghost:  CSS["ghost_number"] at 320px, on the slide background (OUTSIDE the card)
Note:   Step numbering goes in the eyebrow (e.g. "STEP 01" or "RULE 02"). Do NOT add a large decorative
        step number inside the card — it collides with the ghost.
```

### body_strikethrough
```
eyebrow → card[accent + THEN_badge + eyebrow_dash + strikethrough_hl + bold_hl + body + dashed_circle]
Strikethrough: CSS["strikethrough"]
Badge:         CSS["then_badge"]
Circle:        SVG["dashed_circle"] — annotation font text inside dashed ellipse
```

### cta
```
orange_dots → eyebrow → headline → sub → pill_button → wordmark + slide_number
Ghost: none
Pill:  CSS["pill_button"] — ask the user what to show (e.g. their domain, handle, or a generic CTA like "Follow for more")
Dots:  CSS["orange_dot_large"] + CSS["orange_dot_small"]
```

### grid_static
```
eyebrow → headline → 2x2_grid[tiles] → bottom_strip
Tile:    CSS["tile"] + short left accent + tile_number + name + body + CSS["tile_ghost"]
Ghost:   CSS["tile_ghost"] at 160px — tile number (01-04), NOT slide position
Bottom:  CSS["bottom_strip"] — CTA text left, wordmark right
```

---

## Ghost Number Rules

```python
def ghost_value(DS: dict, slide_type: str, pos: int, total: int) -> str | None:
    rule = DS["ghost_numbers"].get(slide_type, "none")
    if rule == "total_slide_count": return str(total).zfill(2)
    if rule == "slide_position":    return str(pos).zfill(2)
    return None
```

**Always use `ghost_number` (320px, slide-level) for consistent positioning:**
- All slides with ghost → `ghost_number`, positioned on the slide background (bottom-right)
- Card slides → ghost goes OUTSIDE the card div, on the slide — card partially overlaps it for depth
- CTA → no ghost

---

## Card Rules

**Default — content-sized:**
`CSS["card"]` as-is. No `flex:1`. Cream space around the card is intentional.

**Exception — single-card slides (pricing, single feature):**
Add `flex:1; display:flex; flex-direction:column; justify-content:center;`
when the card is the ONLY content between the eyebrow and slide number.

**Badges — THEN/NOW contrast slides only:**
- `CSS["then_badge"]` — muted gray, for THEN slides
- `CSS["now_badge"]` — accent tint, for NOW slides
Never badge a standard body_card.

---

## Playwright Screenshot

```python
import asyncio, os
from playwright.async_api import async_playwright

async def shoot(slides: dict, output_dir: str, canvas: dict) -> list[str]:
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
        await browser.close()
    return paths
```

**Troubleshooting blank slides or wrong fonts:**
1. Run `python3 scripts/verify_fonts.py`
2. Confirm font-family names in CSS match `@font-face` declarations exactly
3. Increase `wait_for_timeout` to 1500 if fonts still don't render

---

## Output

Naming from `DS["generation"]["naming"]`: `day{N}-{slideNum_padded}.png`

Present all PNGs to user. On approval: update post status → Ready.

---

## Custom Fonts

1. Update `design-system.json`:
   ```json
   "typography": { "font_primary": "Poppins", "font_annotation": "Caveat" }
   ```
2. Run `python3 scripts/download_fonts.py` — reads the updated font names and downloads accordingly.
3. `fonts.json` will contain `poppins_regular`, `poppins_600`, `poppins_bold`, `caveat_bold`.
4. `build_font_css()` derives key names from family name automatically — no code changes needed.

---

## Reference Files

Read on demand only:

| File | When to read |
|------|-------------|
| `references/design-system-schema.md` | Building or fixing design-system.json |
| `references/slide-generation-pipeline.md` | Writing a complete generator from scratch |
| `references/fonts-setup.md` | Manual font setup, font issues, non-Google Fonts sources |
| `references/generate.sample.py` | Working 4-slide generator (cover + 2 cards + CTA) — copy to `output/_generate.py` and customise |
