# BearCarousel

**English** | [繁體中文](README.zh-TW.md)

**Generate aligned Instagram carousel slides from JSON — and get told when you have made a bad one.**

You write the words in a JSON file. BearCarousel renders 1080x1350 PNGs and then
audits what it just drew: is the title actually centred, does the text pass WCAG
AA contrast against the pixels behind it, is there too much copy on the slide, is
the logo where the brand says it should be. Findings come back red, yellow or
green, and a red finding means the deck is not finished.

The point is not that it draws slides. Lots of things draw slides. The point is
that **the two hardest things for a non-designer to get right — alignment and
knowing when something is wrong — are done for you.**

## Why this exists

Text placement in image generators is usually eyeballed: pick a y coordinate,
estimate the cap height as `font_size * 0.88`, ship it. That estimate is wrong by
a different amount for every font, every weight, and every script, which is how
you end up with a title that is mathematically centred and visibly is not.

BearCarousel positions everything from **real measured ink boxes** — Pillow's
`textbbox`, per script run, per weight — and centres on the ink group, never on a
baseline. There is not a single hardcoded y coordinate for a main element
anywhere in the engine, and contributors are not allowed to add one
(see [CONTRIBUTING.md](CONTRIBUTING.md)).

The second half is the audit. Alignment being *possible* does not stop somebody
cramming 60 characters onto a slide in a purple that measures 3.1:1 against a
near-black background. The audit measures the deck that was actually rendered
and refuses to call it done.

## Quick start — three doors, pick yours

**1. You do not use a terminal → download the app.**
Grab it from the
[latest release](https://github.com/johnnyang0612/bearcarousel/releases/latest),
double-click, and your browser opens with the tool running locally.
Everything stays on your machine; nothing is uploaded anywhere.

- **Windows** — `BearCarousel.exe`. Unsigned, so SmartScreen warns once about
  an unknown publisher: choose "More info → Run anyway".
- **macOS** — `BearCarousel-macos-arm64.tar.gz` for Apple Silicon (M1 and
  later) or `BearCarousel-macos-intel.tar.gz` for Intel Macs. Extract, then
  **right-click the `BearCarousel` file → Open** the first time; if macOS
  still refuses, allow it under System Settings → Privacy & Security →
  "Open Anyway". (Unsigned: notarization requires a paid Apple developer
  account, which a free tool does not have. The Gatekeeper warning is about
  the missing signature, not about what the app does — the code is all here
  to read.)

**2. You have an AI assistant (Claude Code, etc.) → hand it this repo.**
Tell it what you want a carousel about. [AGENTS.md](AGENTS.md) teaches it the
whole job — install, interview you for the real material, draft, render,
audit, and open the browser UI live-linked so you tweak words while it fixes
what the audit flags. This is the richest way to use the tool.

Claude Code users can skip the clone entirely — install it as a plugin once
and ask for a carousel from any project:

```
/plugin marketplace add johnnyang0612/bearcarousel
/plugin install bearcarousel@bearcarousel
```

**3. You are comfortable in a terminal:**

```bash
pip install bearcarousel
bearcarousel ui          # browser UI; also: draft / render / audit / export / open
```

The pip package is a thin launcher: first run downloads the matching release
tree into `~/.bearcarousel/`, after which everything is offline. Or work from
a checkout:

```bash
pip install -r requirements.txt
python run.py
```

`run.py` starts a local server and opens the browser UI. Nobody has to touch a
terminal again after that first line: you edit content in the browser, pick a
template, and download the PNGs.

If you prefer the terminal, or you are scripting this:

```bash
python cli.py render --content examples/demo_content.json --out out/
python cli.py audit  --content examples/demo_content.json
python cli.py export --content examples/demo_content.json --out deck.zip
python cli.py draft  --framework aida-teach --out my_post.json
```

| Command | Does |
|---|---|
| `render` | Writes one PNG per slide into `--out` (default `./out`) |
| `audit` | Renders, audits, prints the findings. **Exits 1 if anything is red.** |
| `export` | Zips the PNGs plus `caption.md`, `audit.json` and `content.json`. Also exits 1 on red. |
| `draft` | Scaffolds an empty content file shaped to a framework — structure and per-slide guidance, no invented words |

`--brand`, `--framework` and `--style` take either a bare id or a path, and
default to `_blank`, `aida-teach` and `deep-bold`, so the short forms above are
complete commands. `--content -` reads from stdin. `--json` prints
machine-readable output on stdout while human progress goes to stderr, so it
pipes cleanly — that is what an AI agent should use, see [AGENTS.md](AGENTS.md).

Output files are named `s01_hook.png`, `s02_problem.png` and so on: slide number
plus role, so they sort correctly when you drag them into Instagram.

> The bundled `examples/demo_content.json` points its photo slides at a portrait
> that is not in this repository (it is a specific person's photograph, and is
> excluded along with the rest of the private brand assets). Those slides still
> render — they fall back to the styled background. Point `image` at your own
> file to see the photo pattern properly.

## The three-layer model

A deck is the product of three things that are edited by different people at
different times, so they live in different files:

```
content  x  template  x  brand   ->   PNGs
 (words)   (framework    (identity
            + style)      tokens)
```

| Layer | File | Holds | Changes when |
|---|---|---|---|
| **Content** | `examples/*.json` | The actual words, per slide | Every post |
| **Template** | `templates/frameworks/*.json` | Which slide roles, in what order, using which layout pattern | You change the story shape |
| | `templates/styles/*.json` | Background treatment, type scale, weights | You want a different look |
| **Brand** | `brands/*.json` | Colours, fonts, canvas size, logo placement, audit thresholds | Once, per company |

Swap the brand file and the same words come out as a different company's deck.
Swap the style file and the same brand gets a different look. Nothing recompiles,
nothing is edited in Python.

This works because **the engine contains zero brand constants**. Every colour,
size, font, margin and audit threshold is read from JSON at render time. If you
find a hex code or a magic number in `engine/`, that is a bug.

Layering is outermost-wins: brand, then style (a style may declare
`brand_overrides`), then framework, then content.

## The four layout patterns

Every slide is one of four patterns, chosen by the framework, and each routes
through exactly one alignment helper in `engine/layout_strict.py`.

| Pattern | Helper | Use it for |
|---|---|---|
| **hook** | `draw_text_at_visual_center` | A single strong line. Openers, claims, the CTA. The whole slide is one sentence you want read in half a second. |
| **photo** | `_dt_stroke` | Text over full-bleed photography. Stroked, never panelled — a black panel over a founder portrait throws away the reason the slide is a photo slide. |
| **list** | `stat_row_visual` | 3 to 5 equal-width columns, each a big value over a small label. Numbered steps, stats, a checklist. |
| **mockup** | `centered_block_visual` | An image block — screenshot, chart, product shot — with a caption under it. The block is centred on its alpha centre of mass, so art with lopsided transparent padding still looks centred. |

Each helper returns the box it actually drew, which is how the audit can check
real geometry instead of trusting the code that placed it.

## What the audit checks

Thresholds in brackets are the defaults from `brands/_blank.json`. They are
brand-level settings, not engine constants — a brand can legitimately run wider
copy limits without anyone editing Python.

**Alignment**
- Main text horizontal centre drift from canvas centre (max 10px) — red
- Logo alpha centre of mass vs the brand's declared `logo.center_y` (max 5px) — red

**Contrast (WCAG 2.1)**
- Every drawn element measured against the mean colour of the background *behind
  it*. The renderer keeps the pre-text background image, so this is a measurement,
  not a guess reverse-engineered out of a flat PNG.
- Below AA (4.5:1) — yellow; below 3:1 — red. Stroked text is exempt.
- A photo slide with unstroked text — red. A photo slide with an opaque panel over
  the photo — red.

**Copy length**
- Title over 2 lines — red. Over 12 characters on a line — red (20 if the line
  contains a number, 6 for a cover). Counting is CJK characters plus Latin words,
  which is what a reader perceives.
- Whole slide over 30 characters — red. Under 15 — yellow.
- Bullets outside 3-5 items, or over 18 characters each — red.
- No concrete number anywhere on the slide — yellow.

**Brand furniture**
- Slide with no logo — red.
- Logo occupying more than 8% of the canvas — yellow.
- More than 3 chromatic colours — red. Black, white and grey are structural and
  are not counted; the rule is about hues competing for attention.

**Layout**
- Less than 40% whitespace on a non-photo slide — red.
- Body content intruding into the top or bottom 150px safe area — red. Logo,
  wordmark, chip and footer legitimately live there and are exempt.

**Language**
- Emoji — red. Pillow renders them as tofu boxes.
- Internal shorthand and jargon patterns — red. A stranger scrolling past does
  not know what your internal case codes mean.
- Abstract corporate filler — yellow.

**Deck-level**
- More than one CTA — red. A CTA that is not the last slide — yellow. No CTA at
  all — yellow.
- First slide with no title — red. First slide not a hook — yellow.

### Severity model

| | Meaning |
|---|---|
| **RED** | Hard block. The deck is not done while one exists. Fix it or change the brand threshold deliberately. |
| **YELLOW** | Warning. You may knowingly override it, but you have to actually look. |
| **GREEN** | Informational. |

Every finding carries a `fix` string saying what to do about it, plus the measured
value and the threshold it broke, so "too long" is never the whole message.

## Add your own brand

```bash
cp brands/_blank.json brands/mycompany.json
```

Then edit it. `_blank.json` is annotated inline and every key in it is read by the
engine. The parts you will care about first:

- `canvas` — defaults to 1080x1350 (Instagram 4:5 portrait)
- `colors` — `background`, `text`, `accent`, `muted`, `stroke`. Optionally
  `accent_light`: a saturated accent on a near-black ground often measures around
  3.2:1, which the audit will correctly fail, so list systems tend to need a
  lighter variant for the big numbers.
- `fonts` — see below
- `logo` — `path`, `corner`, `margin_x`, `center_y`, `height`. Alignment is by
  alpha mass, so a logo with the artwork sitting low in its canvas still lands
  optically correct.
- `wordmark` — optional text block beside the logo
- `limits` — every audit threshold listed above

Colours accept `#RGB`, `#RRGGBB` or `#RRGGBBAA`.

## Templates that ship

**Frameworks** (structure — how many slides, in what order, on which pattern):

| id | Use it for |
|---|---|
| `aida-teach` | Explaining one professional judgement you actually made. The general-purpose default. |
| `pas` | Problem, agitate, solution. The strongest persuasive shape. |
| `case` | One real case with real numbers. |
| `contrast` | The wrong way and the right way side by side. |
| `listicle` | N ways to do one thing; a checklist the reader can screenshot. |
| `story` | First-person narrative, for building trust rather than teaching. |

**Styles** (look — type scale, weights, background treatment):

| id | Look |
|---|---|
| `deep-bold` | Dark ground, huge titles, node texture. Lowest information density, easiest to finish reading. |
| `highlighter` | Off-white ground, dark text, highlighted key numbers. |
| `photo-restrained` | Photography-led, text plays a supporting role. |

Mix any framework with any style with any brand.

## Add your own template

Drop a JSON file into `templates/frameworks/` (which slides, in what order, on
which pattern) or `templates/styles/` (how it looks). No engine change, no
registration step — the file appearing in the directory is the registration.

A framework entry is a role, a pattern, and a note to whoever writes the copy:

```json
{ "role": "insight", "pattern": "hook", "guide": "The counter-intuitive line." }
```

`templates/_TEMPLATE_GUIDE.md` documents every field, in Traditional Chinese.

A style file sets the type scale per pattern — sizes, minimum sizes, weights,
vertical centre ratios — and may carry `brand_overrides` to change the background
treatment. Sizes are starting points: the renderer shrinks a title until it fits
its box rather than letting it overflow or guessing at a size.

## Fonts

Fonts are resolved **by name**, not by path, because a public tool cannot assume
your font folder looks like anyone else's.

Set them in your brand file:

```json
"fonts": {
  "cjk": "NotoSansTC",
  "latin": "Lato",
  "cjk_latin_spacing_em": 0.15
}
```

Resolution order for each name: exact path, then exact filename in any search
directory, then a fuzzy match (`"Noto Sans TC"` finds `notosanstc-vf.ttf`), then
a built-in fallback chain. Search directories are `assets/fonts/` first, then your
operating system's font directories on Windows, macOS or Linux.

**To use a specific font, drop the TTF or OTF into `assets/fonts/`.** That
directory is searched first, so it wins over anything installed system-wide, and
it makes a checkout reproducible on someone else's machine.

**Noto Sans TC and Lato ship in `assets/fonts/`** (both SIL OFL 1.1, license
texts alongside), so a fresh clone renders Chinese and Latin out of the box —
including inside a container with no system fonts. If you remove them and no
CJK face is resolvable elsewhere, the engine raises `FontUnavailable` at startup
rather than silently rendering tofu boxes. On Windows, Microsoft JhengHei is
found automatically as a fallback; on macOS, PingFang.

Two more things the typography layer does that you get for free:

- **Variable font weights.** Ask for `Black` and if the font does not have it, the
  nearest available weight on the ladder is used instead of failing.
- **Mixed-script spacing.** CJK and Latin runs are drawn with their own fonts and
  separated by `cjk_latin_spacing_em` at each boundary, because Chinese set
  against Latin with no optical gap reads as a typo.

Fonts you add yourself stay subject to their own licenses — check redistribution
terms before committing one to a public fork. The bundled Noto Sans TC and Lato
are redistributable under the SIL OFL; see [NOTICE](NOTICE).

## Project layout

```
engine/
  typography.py     font resolution + real ink measurement
  layout_strict.py  the 4 alignment helpers (the core of the thing)
  render.py         content x template x brand -> pixels
  audit.py          the quality gate
brands/             identity tokens (_blank.json is your starting point)
templates/
  frameworks/       which slides, in what order, on which pattern
  styles/           how they look
examples/           example content JSON
assets/fonts/       drop your TTF/OTF files here
api/  web/          local server + single-page browser UI
cli.py  run.py      terminal entry point / one-click browser entry point
```

The layer boundaries are deliberately narrow and are documented at the top of
each module. `layout_strict.py` is worth reading even if you never modify it —
its docstrings explain why each rule exists, and every one of them exists because
breaking it shipped a misaligned deck.

## Who makes this

BearCarousel is built by
[川輝科技 Brightstream Technology](https://www.brightstream.com.tw/?utm_source=bearcarousel&utm_medium=readme),
the team behind **熊董不上班 (Bear CEO)** — real cases from running a software
company in Taiwan, told with the numbers left in. The tool is free and stays
free; following along is how you support it:

- Instagram — https://www.instagram.com/beardon.zzz
- YouTube — https://www.youtube.com/@beardon_zzz
- Free LINE community — https://line.me/ti/g2/h2b4DMtYIMfnGxWP-hZOpFpxcTzbZrZMUD9DxA

## License

Apache-2.0. See [LICENSE](LICENSE) and [NOTICE](NOTICE).

Trademarks are not covered by the license. The Brightstream logo mark in the
browser UI's footer is maker attribution — trademark-protected, not Apache-2.0
content; forks that rebrand should replace it. No other company's brand tokens
or logo artwork are included in this repository.
