# Design System Schema

`design-system.json` is the single source of truth for all visual decisions.
The generator reads all values from this file — nothing is hardcoded.

---

## Required Sections

### meta
```json
{
  "meta": {
    "version": "1.0",
    "style": "Brand Name — Style Name",
    "slide_format": "4:5 portrait (1080x1350px)"
  }
}
```

### canvas
```json
{
  "canvas": {
    "width": "1080px",
    "height": "1350px",
    "ratio": "4:5",
    "padding": "96px 88px",
    "viewport_playwright": "1080x1350"
  }
}
```
Confirmed better than 1:1 on Instagram. Horizontal padding 88px, vertical 96px.

### colors
All values are hex or rgba strings. Token names are referenced by CSS toolkit.
```json
{
  "colors": {
    "bg":       "#FAF8F4",
    "white":    "#FFFFFF",
    "ink":      "#1A1A1A",
    "slate":    "#666666",
    "mist":     "#AAAAAA",
    "line":     "#EDEAE4",
    "orange":   "#E85D04",
    "ghost":    "rgba(0,0,0,0.04)",
    "wordmark": "#D0CCC5"
  }
}
```
Replace hex values for a new brand. Keep token names — the CSS toolkit references them by name.

### typography
**All size values must be concrete px values — never ranges.**
`"104-108px"` is malformed. Use `"104px"`. Ranges break CSS — the browser ignores invalid units.

```json
{
  "typography": {
    "font_primary": "Inter",
    "font_annotation": "Caveat",
    "weights": { "regular": 400, "semibold": 600, "bold": 700 },
    "scale": {
      "cover_headline":  { "size": "104px", "weight": 700, "line_height": 0.97, "letter_spacing": "-0.035em" },
      "slide_headline":  { "size": "56px",  "weight": 700, "line_height": 1.05, "letter_spacing": "-0.022em" },
      "stat_hero":       { "size": "200px", "weight": 700, "line_height": 0.88, "letter_spacing": "-0.05em"  },
      "step_number":     { "size": "140px", "weight": 700, "line_height": 0.88, "letter_spacing": "-0.05em"  },
      "body":            { "size": "22px",  "weight": 400, "line_height": 1.65 },
      "eyebrow":         { "size": "13px",  "weight": 600, "letter_spacing": "0.2em", "transform": "uppercase" },
      "wordmark":        { "size": "18px",  "weight": 700, "letter_spacing": "0.05em" },
      "slide_number":    { "size": "13px",  "weight": 600, "letter_spacing": "0.08em" },
      "annotation":      { "font": "Caveat","size": "36px", "weight": 700 },
      "swipe_hint":      { "font": "Caveat","size": "28px", "weight": 700 }
    }
  }
}
```

### spacing
```json
{
  "spacing": {
    "slide_padding":   "96px 88px",
    "card_padding":    "60px 64px 60px 72px",
    "card_radius":     "16px",
    "card_border":     "1px solid #EDEAE4",
    "wordmark_right":  "88px",
    "wordmark_bottom": "60px",
    "slidenum_left":   "88px",
    "slidenum_bottom": "60px"
  }
}
```

### ghost_numbers
```json
{
  "ghost_numbers": {
    "cover":     "total_slide_count",
    "body_card": "slide_position",
    "body_stat": "slide_position",
    "body_step": "slide_position",
    "cta":       "none",
    "grid_tile": "tile_number"
  }
}
```

### card_accent
```json
{
  "card_accent": {
    "_rule": "Short top accent — top 20% of card height only. Never full height.",
    "css": "position:absolute; top:0; left:0; width:5px; height:20%; background:#E85D04"
  }
}
```

### svg_patterns
```json
{
  "svg_patterns": {
    "wavy_large": {
      "viewBox": "0 0 640 22", "height": "22", "width": "640",
      "path": "M0,11 Q80,3 160,11 Q240,19 320,11 Q400,3 480,11 Q560,19 640,11",
      "stroke": "#E85D04", "stroke_width": "3.5", "stroke_linecap": "round"
    },
    "wavy_small": {
      "viewBox": "0 0 380 18", "height": "18", "width": "380",
      "path": "M0,9 Q47,2 95,9 Q142,16 190,9 Q237,2 285,9 Q332,16 380,9",
      "stroke": "#E85D04", "stroke_width": "2.5", "stroke_linecap": "round"
    },
    "dashed_circle": {
      "viewBox": "0 0 156 156", "width": "156", "height": "156",
      "ellipse": "cx=78 cy=78 rx=70 ry=66 stroke=#E85D04 stroke-width=2 stroke-dasharray='7 5' opacity=0.6",
      "text_stat": "Caveat 44px #E85D04 — the stat (e.g. '3h')",
      "text_label": "Caveat 22px #BBBBBB — the label (e.g. 'every time')"
    },
    "orange_rule": {
      "element": "rect width=120 height=3 rx=1.5 fill=#E85D04 opacity=0.8"
    }
  }
}
```

### css_toolkit
Ready-to-use CSS strings. Drop directly into `style` attributes or class definitions.

Required keys:
```json
{
  "css_toolkit": {
    "slide_base":      "width:1080px; height:1350px; background:#FAF8F4; font-family:'Inter',sans-serif; display:flex; flex-direction:column; justify-content:center; padding:96px 88px; position:relative; overflow:hidden",
    "ghost_number":    "position:absolute; right:20px; bottom:-30px; font-size:320px; font-weight:700; color:rgba(0,0,0,0.04); line-height:1; user-select:none",
"card":            "background:#FFFFFF; border-radius:16px; padding:60px 64px 60px 72px; border:1px solid #EDEAE4; position:relative; overflow:hidden",
    "card_accent":     "position:absolute; top:0; left:0; width:5px; height:20%; background:#E85D04",
    "then_badge":      "display:inline-block; background:#F0EDE8; color:#999; font-size:12px; font-weight:700; padding:6px 16px; border-radius:20px; letter-spacing:0.12em; text-transform:uppercase",
    "now_badge":       "display:inline-block; background:rgba(232,93,4,0.1); color:#E85D04; font-size:12px; font-weight:700; padding:6px 16px; border-radius:20px; letter-spacing:0.12em; text-transform:uppercase",
    "strikethrough":   "text-decoration:line-through; text-decoration-color:#E85D04; text-decoration-thickness:5px; color:#C0BDB8",
    "orange_rule":     "width:120px; height:3px; background:#E85D04; border-radius:2px; margin:32px 0",
    "pill_button":     "display:inline-flex; align-items:center; gap:12px; background:#1A1A1A; color:#FFFFFF; font-size:24px; font-weight:700; padding:22px 44px; border-radius:100px",
    "swipe_hint":      "font-family:'Caveat',cursive; font-size:28px; font-weight:700; color:#CCCCCC; position:absolute; bottom:68px; left:50%; transform:translateX(-50%)",
    "wordmark":        "position:absolute; bottom:60px; right:88px; font-size:18px; font-weight:700; color:#D0CCC5; letter-spacing:0.05em",
    "slide_number":    "position:absolute; bottom:60px; left:88px; font-size:13px; font-weight:600; color:#D0CCC5; letter-spacing:0.08em",
    "eyebrow":         "font-size:13px; font-weight:600; color:#AAAAAA; letter-spacing:0.2em; text-transform:uppercase; margin-bottom:44px",
    "orange_dot_large":"position:absolute; top:96px; right:88px; width:52px; height:52px; border-radius:50%; background:#E85D04; opacity:0.8",
    "orange_dot_small":"position:absolute; top:132px; right:168px; width:22px; height:22px; border-radius:50%; background:#E85D04; opacity:0.3",
    "tile":            "background:#FAF8F4; border-radius:12px; padding:36px 36px 32px 48px; position:relative; overflow:hidden; display:flex; flex-direction:column; border:1px solid #EDEAE4",
    "tile_ghost":      "position:absolute; right:-8px; bottom:-16px; font-size:160px; font-weight:700; color:rgba(0,0,0,0.05); line-height:1; user-select:none",
    "bottom_strip":    "height:72px; display:flex; align-items:center; justify-content:space-between; border-top:1px solid #EDEAE4; margin-top:24px",
    "_card_height":    "Content-sized by default. Use flex:1 only when the card is the sole content element on the slide.",
    "_ghost_note":     "Always use ghost_number (320px, slide-level) for all slides. Card slides: place ghost OUTSIDE the card div, on the slide background. CTA: no ghost. body_step: step numbering goes in the eyebrow, not as a large decorative number.",
    "_badge_usage":    "Badges only on THEN/NOW contrast slides. Never on standard body cards."
  }
}
```

### layouts
```json
{
  "layouts": {
    "A": "cover → body_card × N → body_stat (optional) → cta",
    "B": "cover → body_step 01 → body_step 02 → body_step 03 → cta",
    "C": "cover → body_card(THEN) + body_strikethrough(THEN) → body_card(NOW) + body_stat(NOW) → cta",
    "D": "cover → body_card(overview) → body_card(output) × N → cta"
  }
}
```

### generation
```json
{
  "generation": {
    "font_loading": "Load Inter Regular/600/Bold + Caveat Bold from fonts.json as base64 at runtime.",
    "screenshot":   "Playwright element screenshot on #s1, viewport matches canvas dimensions.",
    "naming":       "day{N}-{slideNum_padded}.png — e.g. day15-01.png",
    "content_source": "Copy from Visual Brief field (Layout A/B) or slide-by-slide spec in page body (Layout C/D)."
  }
}
```

---

## Minimal Starter Template

Copy this and replace brand values to start a new project:

```json
{
  "meta": { "version": "1.0", "style": "BrandName — Style", "slide_format": "4:5 portrait (1080x1350px)" },
  "canvas": { "width": "1080px", "height": "1350px", "ratio": "4:5", "padding": "96px 88px", "viewport_playwright": "1080x1350" },
  "colors": {
    "bg": "#FAF8F4", "white": "#FFFFFF", "ink": "#1A1A1A", "slate": "#666666",
    "mist": "#AAAAAA", "line": "#EDEAE4", "orange": "#E85D04",
    "ghost": "rgba(0,0,0,0.04)", "wordmark": "#D0CCC5"
  },
  "typography": {
    "font_primary": "Inter", "font_annotation": "Caveat",
    "weights": { "regular": 400, "semibold": 600, "bold": 700 },
    "scale": {
      "cover_headline": { "size": "104px", "weight": 700, "line_height": 0.97, "letter_spacing": "-0.035em" },
      "slide_headline": { "size": "56px",  "weight": 700, "line_height": 1.05, "letter_spacing": "-0.022em" },
      "body":           { "size": "22px",  "weight": 400, "line_height": 1.65 },
      "eyebrow":        { "size": "13px",  "weight": 600, "letter_spacing": "0.2em", "transform": "uppercase" },
      "wordmark":       { "size": "18px",  "weight": 700, "letter_spacing": "0.05em" },
      "slide_number":   { "size": "13px",  "weight": 600, "letter_spacing": "0.08em" }
    }
  },
  "spacing": {
    "slide_padding": "96px 88px", "card_padding": "60px 64px 60px 72px",
    "card_radius": "16px", "card_border": "1px solid #EDEAE4",
    "wordmark_right": "88px", "wordmark_bottom": "60px",
    "slidenum_left": "88px", "slidenum_bottom": "60px"
  },
  "ghost_numbers": {
    "cover": "total_slide_count", "body_card": "slide_position",
    "body_stat": "slide_position", "body_step": "slide_position",
    "cta": "none", "grid_tile": "tile_number"
  },
  "card_accent": { "css": "position:absolute; top:0; left:0; width:5px; height:20%; background:#E85D04" }
}
```

Add `svg_patterns`, `css_toolkit`, `layouts`, and `generation` sections from the full schema above to complete it.
