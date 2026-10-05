# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

**Read [AGENTS.md](AGENTS.md) first.** It has the bootstrap steps, the content
JSON schema, and the hard rules — split into Part A (you are extending the tool)
and Part B (you are making a carousel for someone who does not code). This file
adds the commands, the architecture map, and working notes; AGENTS.md is the
actual instruction set.

## What this is

BearCarousel turns a JSON file of words into aligned Instagram carousel slides
(1080x1350 PNGs), then audits what it drew and reports which slides are broken.
Python 3.10+, Pillow for rendering, FastAPI for the local browser UI.

## Commands

```bash
pip install -r requirements.txt              # Pillow + browser-UI extras

# Tests — plain python works, pytest not required
python tests/test_engine.py
pytest tests/                                # also works
pytest tests/test_engine.py -k restructure   # single test by keyword

# The four CLI verbs (all support --json; always pass it when scripting)
python cli.py render --content examples/demo_content.json --out out/ --json
python cli.py audit  --content examples/demo_content.json --json
python cli.py export --content examples/demo_content.json --out deck.zip --json
python cli.py draft  --framework case --topic "..." --out my_post.json

# BYO-LLM drafting brief (rulepack projected into a prompt for any external LLM)
python cli.py prompt --framework case --topic "..." --anchor "真實案例/數字"

# Agent -> human handoff: start the browser UI preloaded with a content file
python cli.py open --content my_post.json --framework case --style deep-bold

# Browser UI: starts the FastAPI server and opens a browser.
# This is for the HUMAN to edit content visually — agents do the same work
# headlessly via cli.py --json and normally should NOT start it. Only run it
# when the user explicitly asks for the UI, and run it in the background
# (it blocks the foreground until killed).
python run.py

# Regenerate templates/frameworks/*.json from the rulepack (see Working notes)
python scripts/sync_frameworks.py            # --check verifies without writing
python scripts/make_thumbnails.py            # re-render template picker cards
```

`--framework` / `--style` / `--brand` take a bare id or a path; defaults are
`aida-teach` / `deep-bold` / `_blank`. `--content -` reads stdin. `--slides N`
restructures the narrative to N slides (drop optional, merge adjacent — never
truncate); pass the same N to render, audit and export. **Exit codes
are the gate:** `audit` and `export` exit 1 when any RED finding exists;
`render` and `draft` always exit 0. With `--json`, machine-readable output goes
to stdout and human progress to stderr, so stdout is safe to parse.

## Architecture

A deck is the product of three layers edited by different people at different
times, so they live in different files:

```
content  x  template  x  brand   ->   PNGs
 (words)    (framework +  (identity
             style)        tokens)
```

Merging is outermost-wins: brand, then style (a style may carry
`brand_overrides`), then framework, then content. The engine contains zero
brand constants — every colour, font, margin and audit threshold is read from
JSON at render time. A hex code or magic number in `engine/` is a bug.

Every slide is one of four patterns, chosen by the framework (content never
picks its own), and each pattern routes through exactly one alignment helper in
`engine/layout_strict.py`:

| Pattern | Helper | For |
|---|---|---|
| hook | `draw_text_at_visual_center` | one strong line |
| photo | `_dt_stroke` | stroked text over full-bleed photo |
| list | `stat_row_visual` | 3-5 columns of value-over-label |
| mockup | `centered_block_visual` | image block + caption |

Each helper returns the `Box` it actually drew; `engine/audit.py` checks that
real geometry rather than trusting the code that placed it. That loop —
measured ink boxes in, drawn boxes out, audit on the result — is the core of
the product.

The other half is the rulepack. `rules/base/*.json` holds every editorial and
design judgement (frameworks, boundaries, viral standards, restructure rules),
versioned via `rules/VERSION`. Each boundary rule carries `severity` (red
blocks, yellow warns) and an `enforced` flag (whether any code actually checks
it). `rules/README.md` documents the field contract.

## Repo layout

```
rules/              the rulepack — all judgement, versioned; base/ holds six files
                    (frameworks, boundaries, viral, restructure, authoring, evidence)
engine/
  typography.py     font resolution + real ink measurement
  layout_strict.py  the 4 alignment helpers — the core of the thing
  render.py         content x template x brand -> pixels
  audit.py          the quality gate + red/yellow/green severity
  restructure.py    adaptive slide count (drop optional, merge adjacent)
  color.py          WCAG contrast math + palette suggestion
  backgrounds.py    procedural background generators (recolour per brand)
  imaging.py        uploaded-asset prep, chiefly logo background removal
brands/             identity tokens; _blank.json is the starting point
templates/
  frameworks/       GENERATED from the rulepack — do not hand-edit
  styles/           type scale, weights, background treatment
scripts/            sync_frameworks.py, make_thumbnails.py
examples/           example content JSON
assets/fonts/       bundled Noto Sans TC + Lato (SIL OFL); searched before system fonts
api/  web/          local server + browser UI
tests/              test_engine.py — audit rules paired with negative controls
.claude/skills/     generate / restyle / audit
```

Read the module docstrings in `engine/` before changing anything there. They are
short, and each records the failure that motivated the rule it describes.

## The three rules that matter most

1. **All positioning goes through the four helpers in `layout_strict.py`.** Never
   hardcode a y coordinate, never derive a height from `size * ratio`. Measured
   ink boxes only. Helpers must return the `Box` they drew.
2. **No brand constants in `engine/`.** Colours, fonts, margins and audit
   thresholds all come from JSON at render time.
3. **No judgement in `engine/` either.** Character limits, framework shapes and
   editorial rules live in `rules/`. Updating the product's judgement means
   shipping a new rulepack, not editing code. `rules/README.md` explains how.

## Working notes

- **`templates/frameworks/*.json` are generated**, not authored. The rulepack
  (`rules/base/frameworks.json`) is the single source of truth for narrative
  structure; `scripts/sync_frameworks.py` projects it into what the renderer
  reads. Hand-editing a framework template fails
  `test_frameworks_stay_in_sync_with_rulepack`. To change a framework: edit the
  rulepack, run the sync script, commit both.
- **Slide-count restructuring** lives in `engine/restructure.py`, exposed as
  `--slides N` on the CLI, the `slides` parameter in the API, and
  `render_all(target_slides=...)` in Python.
- Tests pair every audit rule with a negative control that deliberately breaks
  the thing and asserts the audit rejects it. When adding an audit check, add
  both directions — a gate that only returns green is worse than none.
- **README and AGENTS are bilingual.** `README.md`/`AGENTS.md` (English) are
  canonical; `README.zh-TW.md`/`AGENTS.zh-TW.md` are full translations. Any
  edit to one side must update the other **in the same commit** — a stale
  translation of the agent instruction set is two sources of truth.

## Verify before claiming done

Rendering without an exception is not the same as the deck being correct. Before
saying a change works:

- Run the render, then run the audit and **read the findings**, not just the exit
  code.
- A `red` finding means not done. Fix it, or change the brand's `limits`
  deliberately and say that you did.
- For any layout change, open the PNGs and look at them. An audit that passes on a
  deck that looks wrong is a bug in the audit, and eyes are the only way to catch
  it.
- If you added a rule to `rules/`, check whether any code actually runs it. Mark it
  `enforced: false` until something does. Claiming enforcement that does not exist
  is worse than leaving the rule out.
- Report what you verified and what you did not. If you rendered but did not open
  the images, say so.
