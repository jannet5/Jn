# Social Carousel Generator

A Claude Code skill that generates brand-consistent social media carousel slides (4:5 PNG) from a design system JSON.

## What It Does

- Generates carousel slides for Instagram, LinkedIn, and other platforms
- Uses your brand's design system (colors, typography, spacing) for consistency
- Outputs individual PNGs + a bundled zip file
- Supports multiple slide types: cover, body card, stat, step, strikethrough, CTA, grid

## Installation

1. Download `social-carousel-generator.skill` from the [latest release](https://github.com/idrsdev/social-carousel-generator/releases)
2. Go to **Claude.ai → Project Settings → Skills**
3. Upload the `.skill` file

## Setup

**Quick start** (runs all steps at once):
```bash
bash setup.sh
```

Or manually:

1. **Install dependencies** (once per machine):
   ```bash
   python3 -m venv venv
   source venv/bin/activate
   pip install playwright
   playwright install chromium
   ```

2. **Add a design system** to your project root:
   - Copy `skills/social-carousel-generator/references/design-system.sample.json` to `design-system.json`
   - Update `meta.wordmark_text` with your brand name
   - Customize colors, typography, and styles as needed

3. **Generate fonts** (once per project):
   ```bash
   python3 skills/social-carousel-generator/scripts/download_fonts.py
   ```

## Usage

Just ask Claude to generate carousel slides:

- "Create 3 slides on early sleep benefits"
- "Generate a 5-slide carousel about productivity tips"
- "Make Instagram carousel slides for day 12 (if relevant)"

The skill handles layout selection, HTML rendering, and PNG screenshot generation automatically.

## Sample Output

> **Prompt:** *"Generate a 6-slide carousel about deep focus rules"*

<p>
  <img src="samples/01.png" width="160" />
  <img src="samples/02.png" width="160" />
  <img src="samples/03.png" width="160" />
  <img src="samples/04.png" width="160" />
  <img src="samples/05.png" width="160" />
  <img src="samples/06.png" width="160" />
</p>

Slide types shown: `cover` · `body_stat` · `body_card` · `body_step` · `body_card` · `cta`

## Slide Types

| Type | Description |
|------|-------------|
| `cover` | Opening slide with headline, wavy underline, and swipe hint |
| `body_card` | Content card with headline, body text, and accent bar |
| `body_stat` | Large statistic with supporting text |
| `body_step` | Numbered step with instructions |
| `body_strikethrough` | THEN/NOW contrast slide |
| `cta` | Closing call-to-action with pill button |
| `grid_static` | 2x2 tile grid layout |

## Custom Fonts

Update `typography.font_primary` and `typography.font_annotation` in your `design-system.json`, then re-run the font download script.

## Building the .skill file

```bash
bash make_skill.sh
```

## Author

[Malik Idrees](https://linkedin.com/in/malikidrees) · [idrees.dev](https://idrees.dev)

## License

MIT
