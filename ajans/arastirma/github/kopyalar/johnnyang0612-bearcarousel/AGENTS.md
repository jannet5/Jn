# AGENTS.md — instructions for an AI agent

> This file is for **AI agents**. Humans should read [README.md](README.md).
> 繁體中文版：[AGENTS.zh-TW.md](AGENTS.zh-TW.md)。兩份不一致時，**以這份英文版為準**；
> 改這份時必須在同一個 commit 裡同步改中文版。

BearCarousel turns a JSON file of words into aligned Instagram carousel slides
(1080x1350 PNGs), then audits what it drew and tells you which slides are broken.

There are two kinds of agent here, and they have different jobs:

| | Who | Job | Read |
|---|---|---|---|
| **A** | Build agent | Extend the tool itself: add frameworks, styles, engine features, fix bugs | [Part A](#part-a--build-agent) |
| **B** | The user's own agent | Someone who does not write code says "turn this topic into a carousel". You do the whole thing for them | [Part B](#part-b--the-users-agent) |

If you are not sure which you are: were you asked to change how the tool works,
or to produce a carousel with it? The second one is Part B.

Both read the same source of judgement.

## The rulepack is the source of judgement

`rules/` is a versioned, machine-readable data layer holding every editorial and
design rule this tool knows. The engine, the browser UI and you all read it.
Nothing is hardcoded.

| File | What it answers |
|---|---|
| `rules/base/frameworks.json` | Which six frameworks exist, how many slides each wants, what each slide should contain (`ask`) and an example |
| `rules/base/boundaries.json` | What makes a slide unshippable: character limits, contrast, safe area, colour count, language |
| `rules/base/viral.json` | What makes content worth finishing: hook types, per-slide standards, save and share drivers |
| `rules/base/restructure.json` | How to change the slide count without wrecking the narrative |
| `rules/base/authoring.json` | Per-slide writing instructions, each carrying its citation, evidence level and confidence |
| `rules/base/evidence.json` | The evidence ledger behind every built-in recommendation — including claims deleted after verification, kept so nobody re-adds them |
| `rules/VERSION` | Which rulepack version you are working against |

**Read the rulepack before drafting anything.** It is the difference between this
tool and an image generator.

One thing to understand about `boundaries.json`: each rule carries both a
`severity` (what the tool does today) and an `enforced` flag (whether any code
actually checks it). About a dozen rules are `enforced: false` — guidance only,
nothing will stop you breaking them. Those are exactly the rules where your
judgement is doing the work, so do not skip them because the gate is silent.
`rules/README.md` explains the field contract.

---

# Part A — build agent

## Bootstrap

```bash
pip install -r requirements.txt
```

That is the whole setup. The engine needs Pillow; the rest is for the browser UI.

The repo bundles Noto Sans TC and Lato (SIL OFL) in `assets/fonts/`, so a fresh
clone renders Chinese out of the box. If a brand asks for a font that is not
resolvable and no CJK face can be found at all, the engine raises
`FontUnavailable` — fix it by putting a TTF or OTF into `assets/fonts/` or by
setting `brand.fonts.cjk` to an absolute path. Do not work around it by removing
the Chinese.

## Project structure

```
rules/              the rulepack — judgement lives here, not in code
  base/             frameworks, boundaries, viral, restructure
engine/
  typography.py     font resolution + real ink measurement   <- read this first
  layout_strict.py  the 4 alignment helpers                  <- and this
  render.py         content x template x brand -> pixels
  audit.py          the quality gate + severity model
brands/             identity tokens (_blank.json is the template)
templates/
  frameworks/       which slides, in what order, on which pattern
  styles/           type scale, weights, background treatment
  backgrounds.json  procedural background presets
examples/           example content JSON
assets/fonts/       TTF/OTF files go here; searched before system fonts
api/main.py         FastAPI local server (browser UI backend)
web/index.html      the browser UI
cli.py              render / audit / export / draft / prompt / open
run.py              starts the server and opens a browser
```

The module docstrings explain the reasoning behind each rule. They are short and
they are the most useful thing in the repository — read them before changing
anything in `engine/`.

## Hard rules

**Route all drawing through the four helpers.** Every element goes through
`draw_text_at_visual_center` (hook), `centered_block_visual` (mockup),
`_dt_stroke` (photo) or `stat_row_visual` (list). Never hardcode a y coordinate.
Never compute a height as `size * ratio`. Positioning comes from measured ink
boxes only, and helpers must return the `Box` they drew so the audit can verify
them. See [CONTRIBUTING.md](CONTRIBUTING.md) for why — six earlier versions did it
the other way and all six shipped misaligned.

**No brand values in `engine/`.** No hex codes, no font names, no margins, no
thresholds. Everything is read from JSON at render time; that is what makes the
tool re-skinnable.

**No judgement in `engine/` either.** Character limits, framework shapes and
editorial rules belong in `rules/`. If you are about to write a number into a
Python file, ask which JSON file it should live in instead.

**Adding a rule is two releases, not one.** Ship it in the rulepack as
`enforced: false` first, implement the check in `audit.py`, then flip the flag.
Never describe a rule in the rulepack as enforced when no code runs it — a rule
nobody executes is worse than no rule, because it makes people think something is
guarding them.

**No emoji anywhere.** Pillow renders them as tofu boxes, so the audit
hard-blocks them. This includes the content JSON, the brand files, the rulepack
and anything else you write into the repo.

**This repo is public.** No customer names, no internal case codes, no
credentials, no private brand assets. The audit's language checks block some of
this in rendered copy, but nothing checks the files you write. That one is on you.

## Extending the tool

| Task | Where | Do not |
|---|---|---|
| New framework | `rules/base/frameworks.json`, then `python scripts/sync_frameworks.py` | Hand-edit `templates/frameworks/` — those files are generated from the rulepack, and `test_frameworks_stay_in_sync_with_rulepack` fails on drift |
| New visual style | `templates/styles/*.json` | Touch `engine/` |
| New background | `templates/backgrounds.json` | Ship a bitmap; these are procedural and recolour per brand |
| New audit check | `engine/audit.py` + a rule in `rules/base/boundaries.json` + a `limits` key in `brands/_blank.json` | Hardcode the threshold |
| Change a limit | `brands/*.json` `limits` block | Edit `engine/` |

Adding a framework or style means adding a JSON file, not changing code. If your
change requires an engine edit to add a template, the abstraction has leaked and
that is the bug to fix.

**`templates/frameworks/*.json` are generated, not authored.** The rulepack is
the single source of truth for narrative structure; `scripts/sync_frameworks.py`
projects it into the shape the renderer reads. The two diverged once and it
silently broke the restructure rules, so a test now fails on any drift. To
change a framework: edit the rulepack, run the sync script, commit both.

---

# Part B — the user's agent

The person you are working for cannot code and should never see a terminal. They
give you a topic. You give them a folder of finished slides.

## Two depths, one loop

Ask the user which depth they want, or infer it from what they gave you:

- **高速版 (fast)** — they give a topic plus at least one real anchor: a case
  they actually handled or a number they can stand behind. You draft the whole
  deck in one pass from that material. If they cannot produce a single real
  anchor, say plainly that the deck will be generic, write 【待補：需要什麼數字】
  where a figure belongs, and never fill the blank yourself.
- **進階訪談版 (interview)** — you interview them slide by slide *before*
  drafting. `rules/base/interview.json` is the script: per-role probes with
  `required` and `maps_to`, plus a fatigue policy — skip slides already
  covered by material they gave, merge adjacent questions, keep it to a few
  minutes, and offer the per-slide downgrade "no case here, write the generic
  version" (then mark that slide as generic when you deliver). This is the
  quality path. An AI deck nobody was interviewed for is fluent emptiness.

Both depths run the same loop; the only difference is where the words come from:

```
read rulepack -> intake（四題地基）-> get the topic (+ interview, if 訪談版)
    -> pick framework -> agree the structure -> draft each slide against its `ask`
    -> apply brand -> render -> audit（含力道 strength）-> fix RED, probe weak
    -> export（red/weak 未解會被拒；--override "理由" 才能硬出，留痕）
    -> hand off for preview (cli.py open)
```

**Intake comes before everything** — `rules/base/interview.json` `intake`:
who is this for (audience), what hurts (pain), what REAL material exists
(ground_truth — the question that decides whether this deck can be more than
generic), and the one action at the end (goal). Write the answers into
`content.meta`; the strength gate flags a deck whose `meta.ground_truth` is
empty, and the drafting brief feeds on all four.

### 1. Read the rulepack

Read every file in `rules/base/`. This is not optional preamble — the `ask`
fields are your drafting brief and the boundaries are the shape your copy has to
fit. Note `rules/VERSION` so you can tell the user which ruleset produced their
deck.

### 2. Get the topic

Take what they gave you. If it is one line, ask for the specifics the rules
require, and ask for all of it at once rather than one question at a time:

- What actually happened — the case, the numbers, the before and after.
- Who the reader is and what they are currently doing wrong.
- What the one action at the end should be.

`viral.json` says every slide wants a concrete number, and the audit warns on
slides without one. Numbers have to come from the user. **Never invent one.** A
made-up figure is the one failure mode the audit cannot catch and the user cannot
easily spot, and it is their name on the post.

In 訪談版, this step becomes the interview: walk `rules/base/interview.json`
slide by slide, asking each role's probes and pulling out numbers, cases and
the user's actual phrasing — the raw quotes usually beat anything you would
polish. Follow the fatigue policy in that file rather than marching through
every question. When you deliver, say which slides rest on real material and
which are generic: the tool verifies that a number is *present*, never that it
is *true*, and the user owns the truth part.

### 3. Pick the framework

Six frameworks in `rules/base/frameworks.json`, each with a `best_for`. Match the
material: a finished project with numbers is `case`; a contrarian take is
`contrast`; a takeaway checklist is `listicle`; a personal mistake is `story`; a
sales argument is `pas`; anything else is `aida-teach`.

Tell the user which you picked and why, in one line.

### 4. Agree the structure before writing

Use `framework.count.default` unless the user asked for a specific number. If
they did, pass `--slides N` and the engine applies `restructure.json` for you:
`priority: optional` slides are cut from the back first, then adjacent slides
that declare `mergeable_with` are merged. Hook and CTA are never cut and the
order never changes (`engine/restructure.py` enforces the invariants).
`draft --slides N` scaffolds the already-restructured shape, so you write into
the right number of boxes from the start — use the same `--slides N` on the
later render, audit and export calls.

Read the resulting structure back to the user before you write any copy:
"Eight slides: hook, the problem, the reframe, your numbers, the four steps, the
result, one line from you, then the CTA." Restructuring changes the narrative,
and they own the narrative. Cheaper to fix here than after rendering.

### 5. Draft each slide

For each slide, take its `ask` as the brief and its `example` as the tone, then
check the copy against `boundaries.json` before you write it into the file:

- Title: max 2 lines, 12 characters per line (20 if the line contains a number).
- Whole slide: 15-30 characters. Over 30 is a hard block.
- Bullets: 3-5 of them, max 18 characters each.
- One idea per slide. Second idea means a second slide.
- At least one concrete number.
- No emoji. Pillow cannot render them.
- Leave an open loop so there is a reason to swipe.

Character counting is by reader perception: CJK characters count 1 each, a run of
Latin letters counts as 1, a run of digits counts as 1.

Write it to a content JSON. `draft` will scaffold the shape for you:

```bash
python cli.py draft --framework case --topic "the topic" --out my_post.json
```

That emits the framework's **default-count plan** (expandable roles already
expanded — a listicle scaffolds 8 slides, not its 4 raw roles) with empty
`title`/`sub` and a `_guide` string per slide. Fill in the words. It
deliberately never invents copy.

Content JSON shape:

```json
{
  "topic": "One line describing what this deck is about",
  "slides": [
    { "role": "hook", "title": "The strongest line\nyou have", "sub": "Supporting line", "swipe_hint": true },
    { "role": "list", "title": "Four things to check",
      "stats": [ { "value": "01", "label": "Short label" },
                 { "value": "02", "label": "Short label" },
                 { "value": "03", "label": "Short label" } ] },
    { "role": "caseA", "title": "Text over the photo", "sub": "Context", "image": "path/to/photo.jpg" },
    { "role": "cta", "title": "One clear action", "sub": "Where to do it" }
  ]
}
```

| Key | Type | Notes |
|---|---|---|
| `role` | string | Must line up positionally with the framework's roles |
| `title` | string | The main line. `\n` forces a break |
| `sub` | string | Optional supporting line |
| `bullets` | string[] | 3-5 items, max 18 chars each |
| `stats` | object[] | `list` pattern. `{ "value": ..., "label": ... }`, max 5 used |
| `image` | string | `photo` and `mockup` patterns. Resolved against repo root and `assets/` |
| `chip` | string | Small pill in the top corner |
| `keyword` | string | **CTA slide only, and it should always be there**: the one word readers must comment/search (1-4 chars hits hardest). The renderer blows it up on the accent emphasis layer as the slide's biggest element; the audit REDs a keyword that is not the largest ink on the slide |
| `mini_title` | string | Optional, 4-7 characters |
| `swipe_hint` | bool | Draws the next-slide affordance |
| `meta` | object | Top-level, not per-slide: `{audience, pain, ground_truth, goal}` from the intake. The strength gate checks `ground_truth` |

The framework decides each slide's pattern. Content does not choose its own.

### 6. Apply the brand

Copy `brands/_blank.json`, rename it, set the colours, fonts and `logo.path`.

`_blank.json` ships with `logo.path` set to `null`. The audit reports that at deck
level as a warning, and once you do set a logo path, any slide missing it becomes
a hard block. Set it early.

### 7. Render, audit, fix, repeat

```bash
python cli.py render --content my_post.json --framework case --style deep-bold --brand mybrand --out out/ --json
python cli.py audit  --content my_post.json --framework case --style deep-bold --brand mybrand --json
python cli.py export --content my_post.json --framework case --style deep-bold --brand mybrand --out deck.zip --json
```

`--framework`, `--style` and `--brand` accept a bare id or a path, and default to
`aida-teach`, `deep-bold`, `_blank`. `--content -` reads stdin. `--slides N`
restructures to N slides (see step 4) — pass the same value to render, audit and
export or they will disagree about the deck. List what is available with
`ls templates/frameworks/ templates/styles/ brands/` — the sets grow, so do not
assume a fixed list.

**Every command supports `--json`. Always pass it.** Machine-readable output goes
to stdout, human progress goes to stderr, so stdout is safe to parse or pipe.
(`draft` accepts the flag for consistency but always emits JSON anyway.)

**Exit codes are your gate.** `audit` and `export` return **1 when any RED finding
exists**, 0 otherwise. `render` and `draft` always return 0. Do not parse text to
decide whether a deck is done — check the exit code, then read the findings.

**The strength gate（力道守門）.** The audit report carries a `strength` block
and `summary.weak`: weak hook (no number / no contrast / generic phrasing),
weak CTA (no `keyword`, no timing), hollow sub (no fact — remove it and the
sentence still stands), and a deck with no `meta.ground_truth`. Every weak item
comes with the SAME `suggestion` and `probe` the UI shows — **ask the user the
probe instead of shipping**; that is the entire point of this tool. `export`
refuses while red or weak items remain. If the user explicitly insists, pass
`--override "their reason"` — it ships, exits 0, and the reason is recorded in
the delivered `audit.json`. Never override on your own initiative.

Reading the result:

```json
{
  "passed": false,
  "summary": { "total": 8, "passed": 6, "red": 2, "yellow": 5 },
  "slides": [
    { "slide": 1, "role": "hook", "passed": true,
      "align_ok": true, "contrast_ok": true, "wordcount_ok": true,
      "logo_ok": true, "readability_ok": true, "findings": [], "fixes": [] }
  ],
  "deck_findings": []
}
```

Check `passed` first. If false, walk `slides[].findings` and `deck_findings`:

| Field | Use |
|---|---|
| `severity` | `red` blocks. `yellow` is a warning you may accept with a stated reason. `green` is informational |
| `dimension` | `align`, `contrast`, `copy`, `brand`, `layout`, `language` or `readability` |
| `message` | What is wrong, with the measured number |
| `fix` | **What to do about it. Follow this.** |
| `measured` / `threshold` | The number and the limit it broke |

`fix` is written to be actionable — read it before deciding anything yourself.
Findings are not spread evenly: most of what you will hit is `copy` (too many
characters), `contrast` and `readability`. All three are fixed by editing the
content JSON or the brand colours, never by touching Python.

Fix the flagged slide, re-render, re-audit. Loop until the exit code is 0.

### 8. Deliver

`export` produces a zip containing `slides/*.png`, `caption.md`, `audit.json` and
`content.json` — the whole deliverable, including the audit report. PNGs are named
`s01_hook.png`, `s02_problem.png` and so on, so they sort in posting order.

To let the user preview and tweak without a terminal, hand off to the browser
UI instead of (or before) zipping:

```bash
python cli.py open --content my_post.json --framework case --style deep-bold
```

That starts the local UI with the deck already loaded — **and live-linked**.
The content file becomes shared state between you and the browser:

- **You write the file → the UI hot-reloads** within a couple of seconds.
- **The user edits in the UI → the file updates** (debounced, written
  atomically), so just read it again to see their changes.

That makes this loop possible, and it is the loop the user actually wants:

```
you draft -> cli.py open -> user tweaks words in the browser
    -> you re-read the file, re-run audit, fix what is red, rewrite the file
    -> UI hot-reloads -> repeat until the audit passes -> export
```

**Always re-read the file before rewriting it** — last write wins, and
clobbering an edit the user just made in the browser is how you lose their
trust. Run `python cli.py open` in the background (it blocks while serving).

One more handoff tool: `python cli.py prompt --framework case --topic "..."
--anchor "..."` emits the full drafting brief for any external LLM (use it
when someone else's model is doing the drafting, not you). The UI's own
訪談/快速 modes read the same rulepack you do.

Tell the user which rulepack version produced it, and list any `yellow` findings
you chose to accept along with why. Do not present a deck as clean when it merely
has no reds.

## Hard rules for Part B

**Weak items get probed, not shipped.** When strength flags a slide, ask the
user its `probe` question and rewrite with what comes back. Shipping fluent
emptiness because the user seemed satisfied is the failure mode this product
exists to prevent. Overriding the gate is the USER's explicit call, never yours.

**Never ship a RED.** Not "mostly fine", not "only two reds". Red means broken:
unreadable contrast, text outside the safe area, a title that does not fit. If you
genuinely believe a red is wrong for this brand, the fix is to change that
threshold in the brand's `limits` block and tell the user you did — not to ignore
the finding, suppress the check, or export anyway.

**One CTA, and it goes last.** More than one is a deck-level red. Not last is a
yellow. None at all is a yellow. Pick one action.

**No emoji.** Pillow renders them as tofu boxes and the audit hard-blocks them.

**Never invent facts.** Numbers, cases and quotes come from the user. The audit
checks whether a number is present, never whether it is true.

**Route all drawing through the four helpers.** If a slide needs something the
patterns cannot do, say so — do not write custom positioning code to get around
it. Hardcoded coordinates are how every earlier version shipped misaligned.

**Look at the images before you say it is done.** An audit that passes on a deck
that looks wrong is a bug in the audit, and eyes are the only way to catch it.
Report what you verified and what you did not.

## Things that will trip you up

**A fresh `_blank` brand has no logo**, so `logo.path` is `null` and the deck-level
audit warns about it. This is expected on first run, not a broken tool.

**Colour count is capped at 3.** Neutrals do not count — black, white and grey are
structural. The rule targets hues competing for attention. If the audit says four,
one is an accent you added without noticing.

**A saturated accent on a dark background usually fails contrast.** Around 3.2:1
is typical against a 4.5:1 bar. Brands carry `colors.accent_light` for exactly
this; use it for large numbers rather than lowering the threshold.

**Passing contrast does not mean readable.** The `readability` dimension checks
two things contrast misses: a busy background (patterns average out to a
comfortable ratio while each glyph fights a different local value) and scale
(Instagram renders a 1080px slide at roughly 400px in feed, so type is about 2.7x
smaller than it looks while you design it). Both can hard-block.

**Photo slides need stroked text, not a panel.** Unstroked text over a photo is a
red, and so is covering the photo with an opaque panel. `_dt_stroke` with
`stroke_width=4` is the answer. If the photo is so busy that even stroked text is
unreadable, change the photo.

**Whitespace has a floor of 40% on non-photo slides.** Hitting it means too much
content on one slide. Split it.

**The bundled example references a private photo.** `examples/demo_content.json`
points at a portrait excluded from this repository. Those slides render with the
styled background instead. Expected, not a bug.

## Do not

- **Do not edit `brands/_blank.json` limits to make your output pass.** Each of
  those numbers corresponds to a real slide nobody finished reading. If a brand
  genuinely needs different limits, copy the file first and change the copy.
- **Do not put brand values or judgement thresholds into `engine/`.**
- **Do not commit logos, rendered PNGs, or fonts that are not licensed for
  redistribution.** The bundled Noto Sans TC and Lato are SIL OFL and ship with
  their license texts in `assets/fonts/`; any font you add there needs the same
  treatment. `.gitignore` excludes the private brand assets and output
  directories for a reason.
- **Do not claim a deck is done without running the audit and reading it.**
  Rendering without an exception is not the same as the deck being correct — that
  gap is precisely what the audit exists to close.
