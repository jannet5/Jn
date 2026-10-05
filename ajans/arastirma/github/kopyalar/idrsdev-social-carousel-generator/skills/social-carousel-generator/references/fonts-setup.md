# Fonts Setup

`fonts.json` stores fonts as base64-encoded woff2 strings for headless Playwright rendering.
It is auto-generated — never edit it manually.

---

## Recommended: Auto-Download (Google Fonts)

The download script reads `font_primary` and `font_annotation` from `design-system.json`
and downloads the correct fonts automatically.

```bash
python3 scripts/download_fonts.py
```

Output: `fonts.json` in your project root.
Requires internet access to `fonts.googleapis.com` and `fonts.gstatic.com`.

To verify the result:
```bash
python3 scripts/verify_fonts.py
```

---

## Expected fonts.json Structure

Keys follow `{family_lowercase}_{weight_label}`:

```json
{
  "inter_regular": "BASE64...",
  "inter_600":     "BASE64...",
  "inter_bold":    "BASE64...",
  "caveat_bold":   "BASE64..."
}
```

If your primary font is Poppins, keys would be `poppins_regular`, `poppins_600`, `poppins_bold`.

---

## Manual Setup (if auto-download is unavailable)

Download fonts from Google Fonts manually:
- **Inter** — https://fonts.google.com/specimen/Inter (download Regular 400, SemiBold 600, Bold 700)
- **Caveat** — https://fonts.google.com/specimen/Caveat (download Bold 700)

Then encode to base64:

```python
#!/usr/bin/env python3
"""Run from your project root after downloading font files."""
import base64, json

def encode(path):
    with open(path, "rb") as f:
        return base64.b64encode(f.read()).decode("utf-8")

fonts = {
    "inter_regular": encode("Inter-Regular.ttf"),
    "inter_600":     encode("Inter-SemiBold.ttf"),
    "inter_bold":    encode("Inter-Bold.ttf"),
    "caveat_bold":   encode("Caveat-Bold.ttf"),
}

with open("fonts.json", "w") as f:
    json.dump(fonts, f)

print(f"fonts.json written — {len(json.dumps(fonts)) // 1024}KB")
```

---

## File Size

`fonts.json` is typically 1.5–2MB. This is expected — font binaries are large.
Do not commit it to public repositories.

---

## Non-Google Font Sources

If your fonts come from a CDN, Typekit, or custom files — download the `.woff2` or `.ttf` files
and use the manual encode script above. Key names must match the pattern the generator expects:
`{family_lowercase}_{weight_label}`.

Verify with:
```bash
python3 scripts/verify_fonts.py
```
