# Worked example

Read this at the output step, after your own direction, faces and palette
are chosen, and only for the format of the two files: which sections, which
tables, how much detail. It is not a starting point, and its choices are not
candidates for yours.

## Contents

- Do not reuse these values
- The client
- direction.md
- tokens.css
- What came from where

## Do not reuse these values

Every face, colour, ratio and layout row below was derived from one invented
client's subject: a swimming pool. None of it transfers. If your finished
direction contains a face, a hue or a layout row from this example, go back
to your own anchors and check that they produced it. If you cannot point to
the anchor in your client's subject that leads to the value, replace it. The
convergence checks in `typography.md` and `colour.md` count this file as
one of the lists they check against.

## The client

Lowther Lane Swim School (invented) teaches adults who cannot swim, in small
evening groups, at a 1930s municipal baths. Conversion action: book a first
lesson. Proof: none yet, placeholders in the copy. Assets: none; the client
will send photographs of the pool hall and the two teachers later. No brand
rules, no logo file.

## direction.md

```markdown
# Direction: Lowther Lane Swim School

## Subject

- Material and object: the 1930s wall tiles, pale green-blue; the water,
  which deepens from aqua at the shallow end to a dark blue at the deep end;
  black lane lines on the pool floor.
- Place and light: the hall in the evening, light from high windows
  rippling across the tile; the echo that softens every voice.
- Gesture and moment: letting go of the wall for the first time and
  floating; the slow breath out that follows.
- Audience setting: an adult who is afraid of water, reading on a phone in
  the evening, deciding whether they would be embarrassed.
- How the copy sounds: plain, patient, specific about what happens and when.
- What the visitor must believe: "Nobody will watch me struggle, and the
  first lesson starts in the shallow end."
- Anchors: (1) the tile and the water's shallow-to-deep gradient
  (material), (2) evening light moving on the tile (place and light),
  (3) the first float: slow, buoyant, nothing sharp (moment).

## Direction

Inside the pool: the page is the tiled wall itself, a full aqua ground with
lighter, evening-lit panels for the long passages; wide, rounded headlines
that take their time; and the deep-end blue kept for the one place you book,
the step towards deeper water.

Reason: the audience fears the place and fears being rushed. Being inside
the familiar tile before they arrive makes the building known, and soft,
unhurried type answers the moment they are dreading.

Rejected:
- Fitness brand (near-black, neon accent, heavy italic display): signals
  performance to people afraid of being watched.
- Spa calm (cream background, light serif display, soft lifestyle photos):
  the category default for wellness; nothing in it belongs to this pool.
- Poolside signage (the pool's painted depth markings and notices as the
  type, condensed capitals): my first idea, and the trade's document
  default. Every pool has the same signs, so a competitor could use it, and
  signs speak in instructions to an audience that needs reassurance.

Swap test: a gym or a yoga studio has no tile wall, no shallow-to-deep water
and no first float; the direction holds.

## Typography

Requirements:
- Display: rounded, soft forms and wide proportions, so headlines feel
  unhurried (anchor 3); must not read as a children's brand at large sizes.
- Text: a serif for the long reassurance passages, so they read like a
  letter (direction), with an optical size axis so the body and the larger
  lead paragraphs come from one family.

Number of faces: two. A rounded wide display would tire over long passages.
No mono: the page shows times and prices but no code or aligned technical
values; times use the text face's tabular figures.

Search (Google Fonts metadata):
- Display filter: sans or display category, a `wdth` axis, a weight range
  reaching 500. A handful of families passed (the count is recorded in a
  real direction; this invented example does not claim one), so all were
  looked at.
- Text filter: serif category, an `opsz` axis, weights 400 and 600. More
  passed; a spread across the whole result was looked at, not the top of a
  sort order.

Shortlist:
- Display. Fredoka (found by search; chosen: rounded; its family page lists
  width and weight axes, so the headline can be set wider than the text).
  Nunito (recalled; lost: also rounded, but no width axis is listed for it
  in the sources read, and wide proportions are a requirement). Bricolage
  Grotesque (found by search; lost: its quirky grotesque character adds
  play that an anxious audience does not need).
- Text. Literata (found by search; chosen), Newsreader (found by search)
  and Source Serif 4 (recalled), both lost on the tie-break: all three are
  serifs with an optical size axis and meet the requirement; the other two
  are the text faces this skill's tables name most often.

Convergence sentence (display): the learner's first float is slow and
nothing about it is sharp, and the headline has to feel the same; a client
whose subject is not overcoming a fear has no reason for a rounded, widened
display.

Unconfirmed, for the user to check by eye: how soft Fredoka's terminals
look at 5rem, and whether it reads as childish in sentence case at that
size. If it does, Nunito at a heavier weight is the fallback choice.

| Role | Face | Source | Licence | Weights | Why |
|---|---|---|---|---|---|
| Display | Fredoka | Google Fonts | open licence, check the family page | 500, set wide on the width axis | rounded and wide: the first float |
| Text | Literata | Google Fonts | open licence, check the family page | 400, 600 | serif for letter-like passages; optical size axis for body and lead paragraphs |

Fallbacks: no face preinstalled on common systems is rounded, so the
display stack falls back to `system-ui`, a sans, and the build measures the
width difference and corrects it with `size-adjust`. The text face falls
back to Georgia, a preinstalled serif, measured and corrected the same way.
Measurement to make on the real build: set the hero line in the web face
and in each fallback at the same size, and compare the line lengths and
the lowercase height; the build's `size-adjust` uses that difference.

Display in sentence case, one weight. Scale: major third (1.25) from a 17px
body, because serif text reads small at 16px. `--text-sm` captions,
`--text-base` body, `--text-lg` lead paragraphs, `--text-xl` H3, `--text-2xl`
H2, `--text-display` the hero and the one statement section; the hero
headline is seven words, so it gets a display size, kept below the usual top
because wide faces need less. Leading: 1.0 for display, 1.15 for headings,
1.55 for serif body. No tracking changes: the page sets no capitals.

## Colour

| Role | oklch | hex | Anchor |
|---|---|---|---|
| bg | 0.90 0.04 190 | #c1e7e4 | the tiled wall at full strength: the page is the pool |
| surface | 0.965 0.014 190 | #e9f7f5 | tile in evening light; panels for long passages |
| on-surface | 0.21 0.006 200 | #151919 | text on the panels |
| ink | 0.21 0.006 200 | #151919 | the black lane lines seen through water: nearly neutral |
| ink-muted | 0.40 0.015 200 | #3f4a4b | the same, lighter |
| line | 0.76 0.035 195 | #98b8b8 | grout, decorative rules only |
| line-strong | 0.52 0.03 200 | #556e70 | form field borders |
| action | 0.40 0.07 225 | #0e4f64 | the deep-end water; booking buttons only |
| action-hover | 0.34 0.06 225 | #083e4f | deeper still, pressed |
| on-action | 0.97 0.012 190 | #edf8f7 | text on the button |

The ground is deliberately a coloured page (chroma 0.04): the direction puts
the visitor inside the tiled pool. Long passages sit on the lighter surface
panels so reading stays calm. Inline links use the ink with an underline;
the action colour stays the button fill.

Status: form errors use the ink with a text label and an icon, so nothing
competes with the booking button.

Provisional: the tile and water values are estimated from the description
of the building. The pool hall photograph will confirm them. If the real
tile is greener or darker, `bg` and `surface` move with it and every pair
on them must be checked again; the action stays at L 0.40 so the button
label keeps its ratio.

Contrast (WCAG 2.2, computed):

| Pair | Ratio | Use |
|---|---|---|
| ink on bg | 13.33:1 | body text on the ground |
| ink-muted on bg | 6.89:1 | captions on the ground |
| on-surface on surface | 16.06:1 | long passages |
| ink-muted on surface | 8.30:1 | captions on panels |
| on-action on action | 8.31:1 | button label |
| on-action on action-hover | 10.65:1 | button label, pressed |
| line-strong on bg | 4.09:1 | input borders on the ground (3:1 needed) |
| line-strong on surface | 4.93:1 | input borders on panels |
| line on bg | 1.59:1 | decorative rules only, never a control edge |

Theme: light only. The client's evenings are lit pool halls, and there is no
dark material to work from. No dark theme ships, so nothing is inverted; the
build sets `color-scheme: light`.

## Layout

12-column grid, content max 75rem, side gutter `--space-gutter`. Text at
`--space-measure` (64ch). Spacing steps: `--space-2` to `--space-16` on a
4px base for gaps inside components and between blocks; two section
paddings, dense for logistics and open for the statement, the open one very
open because room is part of the direction.

| Copy section | Layout |
|---|---|
| Hero | on the aqua ground; headline left on columns 1-8 in the display face; booking button and the next start date under it; pool hall photo full-bleed below at 21:9 |
| You do not need to be able to swim | loud by scale on the same ground: one statement at `--text-display`, columns 2-9, open rhythm, nothing else |
| What the first lesson is like | surface panel, columns 2-11; timeline set as real clock times (7:00 pm, 7:10 pm, ...) on a 2px ink rule like a lane line; dense rhythm |
| The course: eight Tuesdays | on the ground; dates in a two-column list |
| Your teachers | two portraits at 4:5 beside short bios on surface panels, columns 1-6 and 7-12 |
| Price and booking | one surface panel: price, what it includes, the button |
| Questions people ask before booking | dense, on the ground; two columns on wide screens, one on phones |

Boldness: the hero, which shows the real place, and the "You do not need to
be able to swim" statement, which answers the must-believe line. No inverted
dark band: no section changes state, so the loud statement uses scale on
the same ground.

## Imagery and assets

| Visual | Ratio | Status |
|---|---|---|
| Pool hall in evening light, empty, from the shallow end | 21:9 (16:9 crop on phones) | placeholder |
| Teacher portraits x2, poolside, natural light | 4:5 | placeholder |
| Wordmark | n/a | proposal: "Lowther Lane" in Fredoka 500 |

Request from the client:
1. Photograph of the empty pool hall from the shallow end in evening light,
   landscape, at least 2400 px wide.
2. A portrait of each teacher by the pool, 4:5, at least 1200 px wide.
3. Written permission from each teacher to publish their photo and name.
4. The school's logo as SVG, if one exists; otherwise approval of the
   typeset wordmark.

No stock swimmers or smiling-class photos: the audience would read them as
people who can already swim.

## Surfaces and details

- Radius scale of two values, because the page has two kinds of element:
  `--radius-none` on panels and images (the tile is square) and
  `--radius-control` 6px on buttons and inputs, soft enough to sit with the
  rounded display while still reading as a control.
- Rules: 2px ink rules echo the lane lines; 1px `--color-line` elsewhere.
- No shadows: the water and the tile are flat.
- No icons except the one beside form errors: every other item already has
  a word or a time.

## Motion and 3D intent

none. The audience is anxious, and nothing on the page should move unless
they ask it to. The page is complete as static.
```

## tokens.css

```css
/* Lowther Lane Swim School. Reasons for each value are in direction.md. */
:root {
  --color-bg: #c1e7e4;
  --color-surface: #e9f7f5;
  --color-on-surface: #151919;
  --color-ink: #151919;
  --color-ink-muted: #3f4a4b;
  --color-line: #98b8b8;
  --color-line-strong: #556e70;
  --color-action: #0e4f64;
  --color-action-hover: #083e4f;
  --color-on-action: #edf8f7;

  --font-display: "Fredoka", system-ui, sans-serif;
  --font-text: "Literata", Georgia, serif;

  --text-sm: 0.875rem;
  --text-base: 1.0625rem;
  --text-lg: 1.3125rem;
  --text-xl: 1.625rem;
  --text-2xl: clamp(2rem, 1.4rem + 2.5vw, 3.25rem);
  --text-display: clamp(2.5rem, 1.25rem + 5vw, 5.5rem);
  --text-leading-display: 1;
  --text-leading-heading: 1.15;
  --text-leading-body: 1.55;

  --space-2: 0.5rem;
  --space-4: 1rem;
  --space-8: 2rem;
  --space-16: 4rem;
  --space-gutter: clamp(1rem, 0.5rem + 2.5vw, 2.5rem);
  --space-section-dense: clamp(3rem, 2rem + 4vw, 5rem);
  --space-section-open: clamp(6rem, 3rem + 12vw, 13rem);
  --space-measure: 64ch;
  --space-content-max: 75rem;

  --radius-none: 0;
  --radius-control: 6px;
}

@supports (color: oklch(0 0 0)) {
  :root {
    --color-bg: oklch(0.9 0.04 190);
    --color-surface: oklch(0.965 0.014 190);
    --color-on-surface: oklch(0.21 0.006 200);
    --color-ink: oklch(0.21 0.006 200);
    --color-ink-muted: oklch(0.4 0.015 200);
    --color-line: oklch(0.76 0.035 195);
    --color-line-strong: oklch(0.52 0.03 200);
    --color-action: oklch(0.4 0.07 225);
    --color-action-hover: oklch(0.34 0.06 225);
    --color-on-action: oklch(0.97 0.012 190);
  }
}
```

## What came from where

The ground is the tiled wall and the panels are the same tile in evening
light. The ink is the lane line seen through water, nearly neutral. The one
strong colour is the deep end, and it has one job: booking is the step
towards deeper water. The display face is the first float. The text face won
a tie-break among three serifs that all met the requirement. The core token
names are all present; `surface` and `on-surface` are this page's own
additions. The number of faces, the absence of a mono face, the missing dark
theme, the absence of a dark band and the `none` motion intent each have a
reason in the subject or the audience.
