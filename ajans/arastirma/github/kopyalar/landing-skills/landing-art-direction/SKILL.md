---
name: landing-art-direction
description: Use when choosing the visual direction of a landing page, including typefaces, colour palette, layout, imagery, or design tokens, or when a page looks templated, generic, or AI-generated.
---

# Landing art direction

You decide how the page looks before anyone writes markup. The output is two
files: `landing/direction.md`, a written direction with a reason for every
choice, and `landing/tokens.css`, the design tokens that
`landing-skills:landing-build` uses unchanged and `landing-skills:landing-motion`
reads for intent.

The job is to find the look that belongs to this client and no other. A
direction that would fit a different client in a different trade is a
template, however tasteful it looks.

## Why this skill exists

Agents without guidance gave unrelated clients the same look: cream, one
dark green or terracotta accent, system fonts or Georgia. A choice is a tell
when it is the unconsidered default, and fine when chosen for a reason
specific to the client. The evidence is in `references/visual-tells.md`.

## Inputs

1. `landing/brief.md`: the product, audience, conversion action, assets,
   brand constraints, language, stack, and `Wanted moments` (the motion, 3D
   or scroll effects the user asked for). The direction gives each wanted
   moment its role, purpose and reduced-motion form in the
   `Motion and 3D intent` section (step 8).
2. `landing/copy.md`: the approved copy, one H2 per section in page order,
   ending with `Placeholders`. The section list is your layout's skeleton and
   the words set the tone.

If either is missing, or the client has brand rules or an existing
stylesheet, read `references/inputs.md` first. In short: copy comes first,
so run `landing-skills:landing-copy` when you can; otherwise work from the
brief and mark what depends on the copy as provisional.

## Process

Do the steps in order. Steps 1 to 3 produce writing, not values.

Reading order is a rule. Write the `Subject` and `Direction` sections before
you open any file in `references/`, with two exceptions:
`references/inputs.md`, which you read at the start when an input is
missing, and `references/visual-tells.md`, which you read at step 2 for the
default test. Then open each reference only
at the step that uses it. The reason: a table read before the direction
exists becomes the answer, and readers who read the tables first pick the
same faces and palettes for unrelated clients.

Short path. For a page of one to three sections with no imagery decisions to
make, these steps still apply in full: subject (1), direction with rejected
defaults (2 and 3), typefaces found by search (4), palette with contrast
(5), and the core tokens (9). These shrink to a line each in `direction.md`:
the layout map (6), assets (7) and motion intent (8). The convergence checks
are never skipped.

### 1. Inventory the subject

From the brief and the copy, list in plain nouns, under three kinds of
source:

- Material and object: what the product is made of or handles, the tools,
  the textures. For software, the things its users work with and on.
- Place and light: where it is made or used, the light there, the time of
  day, the weather, the sound of the room.
- Gesture and moment: what someone does with it, a sound it makes, the
  moment of use that matters most (the first use, the finished job, the
  pause before deciding).

Then add the audience's setting when they meet the page, three to six words
from the copy that describe how it sounds, and one line on what the visitor
must believe before taking the action.

Choose three anchors, one from each kind of source: concrete things with a
colour, a texture, a rhythm or a character you can name. For example, for
other clients than yours: "the brass of a ship's compass" (material), "low
winter sun across a frozen canal" (place and light), "the clack of a shuttle
crossing a loom" (gesture). The palette, the type and the imagery derive
from these.

The trade's paperwork (forms, receipts, labels, drawings, spec sheets,
screens) counts as one kind of source and may supply at most one anchor: it
is the easiest thing to name, so it pulls every client to the same look. The
product's or company's name can supply one anchor when it refers to
something real (a river, a bird, a street), never the whole palette.

### 2. Write the direction and its reason

Write one or two sentences that name the look and tie it to the anchors, then
the reason: why this look serves this audience and this action.

Write two directions you considered and rejected, each with its reason. At
least one is the obvious default for the category (dark and neon for a
developer tool, cream and serif for a craft product, cold blue for health).
"The trade's document" (the page styled as the client's ledger, blueprint,
spec sheet, receipt, label or form) is a default on the same footing: if it
was your first idea, reject it with a reason, unless you can say what makes
it right for this client and wrong for a competitor with the same paperwork.

Then run two tests:

- Swap test. Replace the client with a business in another trade. If the
  direction still fits, it is generic; rewrite it until it names something
  only this client has.
- Default test. Read `references/visual-tells.md` and compare your plan with
  the defaults it lists. If the plan matches one, give the reason specific
  to this client in writing or change the plan.

### 3. Commit in writing

Write the `Subject` and `Direction` sections of `landing/direction.md` now,
before any token exists. Everything after this must trace back to them.
When a later choice does not, change the choice or revise the direction on
purpose and say so in the file.

### 4. Typography

Work from the anchors, not from a tone word: a tone word fits many faces,
and every reader with the same word picks the same one. Use the copy's sound
words only as a check on the result.

1. Write two to four requirements the face must meet, each checkable on a
   specimen and traced to an anchor (soft, rounded forms like worn river
   stones; a heavy slab; old-style figures in running text). Write them
   before you open `references/typography.md`.
2. Decide how many faces: one family, two faces, or a mono face as well,
   by the criteria in `references/typography.md` ("Decide how many faces").
   Display plus text plus mono is not the default shape.
3. Open `references/typography.md` and follow its steps 3 and 4: filter the
   catalogues on your hard requirements, record the filter and the number
   of matches, look at a spread of the result, shortlist at least three per
   role with at least two found by search, confirm traits from readable
   sources, pick, record two rejected candidates, and run its convergence
   check. Its "First-reach faces" section lists the faces readers reach for
   first.
4. Choose a metric-close fallback for each font stack, by the method in
   the same file ("Font loading notes"), and record why.
5. Size the hero for its real headline; the same file has the numbers.

### 5. Colour

Derive the palette from the anchors in OKLCH by the method in
`references/colour.md`, including the ink and the ground, which are derived
like every other colour. Record every colour with its role, `oklch()` value,
sRGB hex fallback and anchor, and the WCAG 2.2 ratio of every text and
background pair you will use. OKLCH lightness is not a contrast ratio;
compute the ratio with the script in that file.

When the palette is derived, run the colour convergence check in
`references/colour.md`. The same file covers status colours, link colour,
first-reach accents, provisional values when no asset can be sampled, and a
dark theme designed on purpose.

### 6. Layout

Map each H2 in `copy.md` to a layout decision: width, alignment, density,
ground, and what visual it carries. Choose the one or two loud sections by
the test in `references/layout.md` ("Where to spend boldness"); an inverted
dark band is a choice with a criterion there, not the default.

### 7. Imagery and assets

For each visual the section map needs, record what it shows, its aspect
ratio, and whether the asset exists. `references/assets.md` covers
photography, illustration, product shots, icons, logos and the no-assets
case. A missing asset gets a labelled placeholder at its final ratio and a
line in the request list. No stock photos of people, no invented
screenshots, no drawn logo mark; a wordmark typeset in the display face is
an acceptable stand-in, recorded as a proposal.

### 8. Motion and 3D intent

Decide whether the page needs motion or 3D at all; `none` is a valid and
often correct answer. For each moment you name, give its place, what it
shows, its purpose (explain the product, confirm an action, guide attention
to one thing), and its reduced-motion version. A 3D scene also needs a still
poster image.

Keep to one signature moment by default. More is justified when each
explains a different thing a still image cannot show. When the brief's
`Wanted moments` asks for several, the brief wins: name each, give each its purpose and
reduced-motion form, and place them so no two share a screen.
`landing-skills:landing-motion` implements exactly what you write here.

### 9. Write the tokens

Write `landing/tokens.css` as described in "Output format". Every colour and
font token appears in `direction.md` with its reason; each scale (text,
space, radius) is explained once, with its base or ratio and what its steps
are for. Define only tokens the page will use.

Radius: two values are enough when the design has two kinds of element; add
a third only for a real third kind. `--radius-control` is always one of
them. Theme: record light, dark or both in `Colour`; the tokens file holds no
`color-scheme` property, and the build sets it to match.

### 10. Self-check, then hand off

Run the checks in "Self-check". Then tell the user what you decided in a few
lines, list the assets to request, and name the next skill:
`landing-skills:landing-build`.

## Output format

Read `references/example.md` now, for the format of both files only. Its
values belong to an invented client and are not candidates for yours.

### `landing/direction.md`

Use these H2s in this order. The last H2 is always `Motion and 3D intent`.

1. `Subject`: the inventory and the three anchors.
2. `Direction`: the sentence, the reason, the rejected directions with
   reasons, and the result of the swap test.
3. `Typography`: the record listed in `references/typography.md`
   ("Recording the decision").
4. `Colour`: anchors with OKLCH (marked provisional when estimated), role
   table, contrast table, theme decision, any convergence sentence.
5. `Layout`: grid, rhythm, a row per copy section, where the boldness goes.
6. `Imagery and assets`: each visual with aspect ratio and status, then the
   asset request list.
7. `Surfaces and details`: radius, borders, shadows, icons, with reasons.
8. `Motion and 3D intent`: `none` with a reason, or one entry per moment.

### `landing/tokens.css`

Custom properties on `:root` only, with five prefixes and no others:
`--color-*`, `--font-*`, `--text-*` (sizes, leading, tracking), `--space-*`
(spacing, rhythm, measure) and `--radius-*`. No other selectors, no
`@font-face`, no component styles.

Write the sRGB hex values in `:root`, then the `oklch()` values inside an
`@supports (color: oklch(0 0 0))` block. A custom property takes the last
declared value even when the browser cannot parse it as a colour, so two
declarations in one rule are not a fallback. `references/colour.md` has the
skeletons, including the dark theme.

Every `tokens.css` contains this core set, with exactly these names, so
`landing-skills:landing-build` can rely on them:

| Token | For |
|---|---|
| `--color-bg` | the page ground |
| `--color-ink` | body text and headings on the page ground; inline links, underlined |
| `--color-ink-muted` | captions and secondary text on the page ground |
| `--color-line` | rules and dividers |
| `--color-action` | the control colour: the primary action's fill |
| `--color-action-hover` | the action's hover and pressed state |
| `--color-on-action` | text on the action fill |
| `--font-display` | headlines (the same family as `--font-text` when one family is used) |
| `--font-text` | body text |
| `--text-base` | body size |
| `--text-display` | the hero headline size, fitted to its length |
| `--space-gutter` | side padding of the page |
| `--space-section-open` | padding of the sections that carry the claim |
| `--space-section-dense` | padding of quick-reading sections |
| `--space-content-max` | maximum content width |
| `--radius-control` | corners of buttons and inputs |

Everything else is free, within the five prefixes. Every additional ground
that carries text gets a paired token, `--color-<ground>` with
`--color-on-<ground>`, and each pair has a computed ratio. Never use a
background token as a text colour. An accent used on a dark band needs its
own passing step for that band or stays off it (`references/colour.md`).

## Self-check

Before handing off, confirm each line. Fix what fails.

- [ ] You reread `references/visual-tells.md` and checked the direction and
      tokens against every cluster and the table of tells the detector
      cannot see. This check is required.
- [ ] `direction.md` has the eight H2s in order and ends with
      `Motion and 3D intent`.
- [ ] Three anchors from three kinds of source, at most one from paperwork;
      the swap test passes; two rejected directions, one the category
      default.
- [ ] Typography: the record in `references/typography.md` ("Recording the
      decision") is complete, including the search filter and count, the
      recalled or found marks, and the convergence sentence where its rule
      applies.
- [ ] No face from the sets in `references/typography.md` ("Overused faces")
      without its written reason.
- [ ] The hero headline size fits its length.
- [ ] Colour: every colour has an anchor or a role, an `oklch()` value and a
      hex fallback; the colour convergence check in `references/colour.md`
      was run after deriving.
- [ ] Every text pair you will use, including text on panels, bands and
      buttons, has a computed WCAG 2.2 ratio of at least 4.5:1 (3:1 for
      large text and control borders).
- [ ] One accent with one job; status colours and link colour follow
      `references/colour.md`.
- [ ] The section map covers every H2 in `copy.md` except `Placeholders`;
      at least two sections differ in width, density or ground.
- [ ] Every missing asset has a placeholder with its ratio and a line in
      the request list.
- [ ] `tokens.css` uses only the five prefixes on `:root`, has oklch in
      `@supports`, contains the full core set, pairs every extra text ground
      with an `on-` token, and holds no unused tokens.
- [ ] Optional: if `landing-skills:landing-review` is available, its static
      detector can scan `landing/` as an extra check. That skill owns the
      scan; the by-eye check above is the one you must do.
