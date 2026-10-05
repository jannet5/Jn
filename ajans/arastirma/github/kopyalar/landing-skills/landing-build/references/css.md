# CSS recipes

Read this while writing the stylesheet. Every snippet was rendered in a
browser before it went in here. Token names in the examples (`--color-ink`,
`--space-gutter` and so on) are stand-ins: use the names in your
`landing/tokens.css`.

## Contents

- Using the tokens
- Section and container
- Fluid type and space with clamp()
- Logical properties
- Colour with oklch()
- Container queries
- Layouts that avoid the uniform card grid
- Header and navigation
- Interactive states
- Media placeholders
- Text setting

## Using the tokens

- Read tokens with `var()`. Never declare `--color-*`, `--font-*`,
  `--text-*`, `--space-*` or `--radius-*` in page CSS: the tokens belong to
  `landing-skills:landing-art-direction`, and a second declaration makes the
  page drift from the direction without anyone noticing.
- A value with no token is composed from tokens where you use it, for
  example `calc(var(--space-gutter) * 2)` or a `color-mix()` of two colour
  tokens, and listed in the hand-off. Never type a new literal value in its
  place.
- Text sits on the ground it was paired with. `landing-skills:landing-art-direction`
  pairs every ground with a text token (`--color-surface` with
  `--color-on-surface`, `--color-action` with `--color-on-action`) and
  checks the contrast of each pair, so use the pair and never a background
  token as a text colour.

Every `tokens.css` written by `landing-skills:landing-art-direction`
contains this core set under exactly these names, and the recipes rely on
them:

`--color-bg`, `--color-ink`, `--color-ink-muted`, `--color-line`,
`--color-action`, `--color-action-hover`, `--color-on-action`,
`--font-display`, `--font-text`, `--text-base`, `--text-display`,
`--space-gutter`, `--space-section-open`, `--space-section-dense`,
`--space-content-max`, `--radius-control`.

If one of them is missing, the tokens file predates that rule or was written
by hand. Ask for it, or suggest re-running art direction; do not supply the
value yourself.

Every other token is optional: the direction defines it only when the page
needs it. Every recipe in this skill writes each optional token with its
fallback inline, `var(--space-8, calc(var(--text-base) * 2))`, so a recipe
pasted into a project whose `tokens.css` holds only the core set still has
its gaps, padding and colours instead of silently collapsing to zero. Keep
the fallbacks when you copy a recipe. Each fallback is derived from a core
token, never a literal. List every optional token your file lacked in the
hand-off so the direction can add a real one. The table explains each
fallback:

| Optional token in the recipes | Used for | Fallback when the file lacks it |
|---|---|---|
| `--space-2`, `--space-3`, `--space-4`, `--space-6`, `--space-8` | gaps and padding inside components | `calc(var(--text-base) * N)` with N = 0.5, 0.75, 1, 1.5, 2 respectively; if the file has steps under other names, use those instead |
| `--text-sm` | hints, captions, placeholder labels | `var(--text-base)`: small text at body size is never a contrast or legibility risk |
| `--text-2xl` | section headings (h2) | the direction's heading token under its own name; if there is none, `calc(var(--text-base) * 2)` |
| `--color-line-strong` | input borders, which need 3:1 against the ground | `var(--color-ink-muted)`, after checking it reaches 3:1 against `--color-bg` (it is text-grade, so it normally does) |
| `--color-danger` | form error text | `var(--color-ink)`; the error wording carries the meaning, colour never does alone |
| `--color-<ground>` with `--color-on-<ground>` | any band or surface other than the page ground | `--color-bg` and `--color-ink`, so a band without its tokens renders as page ground. Text always takes the `on-` token paired with its ground, and never a background token |

- `tokens.css` should give sRGB hex values on `:root` and the `oklch()`
  values inside `@supports (color: oklch(0 0 0))`. A custom property keeps
  the last value declared even when the browser cannot parse it as a colour,
  so two declarations in one rule are not a fallback. If your tokens file
  has only `oklch()` values, the page still renders in browsers that support
  `oklch()` (94.25% global share per caniuse in the research); tell the user
  the fallback is missing rather than patching values in page CSS.
- A declaration that contains `var()` is not checked when the browser parses
  it. So this does not fall back:

  ```css
  /* Broken in browsers without color-mix(): the second line still wins
     at parse time, then fails at computed time and the background is unset. */
  .notice { background: var(--color-bg); background: color-mix(in oklch, var(--color-action) 12%, var(--color-bg)); }
  ```

  Put newer colour functions inside `@supports` instead (see "Colour with
  oklch()").

## Section and container

Two unguided builds (pages an agent made without this skill) lost all
vertical rhythm the same way:

```css
/* Broken (reconstructed in general form): the class's shorthand also sets
   the top and bottom padding to 0. A class beats a type selector, so every
   section that carries the class ends up with no vertical padding. */
.inner { max-width: 64rem; margin: 0 auto; padding: 0 1.5rem; }
section { padding-top: 5rem; padding-bottom: 5rem; }
```
```html
<section class="inner">...</section>
```

The fix is to give each axis one owner, on two different elements:

```css
.section { padding-block: var(--space-section-open); }
.section--dense { padding-block: var(--space-section-dense); }
.container {
  inline-size: min(100% - 2 * var(--space-gutter), var(--space-content-max));
  margin-inline: auto;
}
```
```html
<section class="section" id="how-it-works">
  <div class="container">...</div>
</section>
```

Why it holds:

- The container sets no padding, so it cannot cancel the section's. The
  gutter comes from subtracting it from the width inside `min()`.
- Only longhands (`padding-block`, `margin-inline`) appear on layout
  classes. A shorthand on a layout class is the bug, whatever selector it
  sits under.
- The footer is a section too (`<footer class="section">` with a container
  inside), so it gets the same rhythm. In one unguided build the footer
  text touched its top border because it lost its padding the same way.
- The header uses the container for width and sets its own
  `padding-block` on the inner element if it needs one, again a longhand.

Section rhythm on phones. The open spacing that suits a wide screen can
take a large share of a long page at 360 px. Use the dense token on every
section at narrow widths and the direction's rhythm from a width up; the
content stays, only the gaps shrink:

```css
@media (max-width: 47.99rem) {
  .section, .section--dense { padding-block: var(--space-section-dense); }
}
```

Lines between sections: decide whether a hairline spans the viewport or the
text column, then put it on the element of that width. A border on
`.section` runs edge to edge; a border on `.container` matches the text.
A border on a third, in-between box (one unguided build drew it on a fixed-width
band whose text sat inside extra padding) sticks out past the text on wide
screens and looks like a mistake.

```css
.section + .section { border-block-start: 1px solid var(--color-line); } /* full width */
.rule { border-block-start: 1px solid var(--color-line); }                /* add to .container for column width */
```

## Fluid type and space with clamp()

`clamp(MIN, PREFERRED, MAX)` picks the preferred value and holds it between
the two limits. For a size that grows linearly from MIN at a small viewport
to MAX at a large one:

1. slope = (MAX − MIN) ÷ (large viewport − small viewport), both in rem
2. intercept = MIN − slope × small viewport
3. write `clamp(MIN, intercept + slope × 100vw, MAX)`

Worked example: 2.75rem at 360px (22.5rem) to 6rem at 1440px (90rem).
Slope = 3.25 ÷ 67.5 = 0.048148, so 4.8148vw. Intercept = 2.75 − 0.048148 ×
22.5 = 1.6667rem.

```css
h1 { font-size: clamp(2.75rem, 1.6667rem + 4.8148vw, 6rem); } /* 44px at 360, 96px at 1440 */
```

Keep a rem term in the middle value. A size made of `vw` alone does not grow
when the visitor zooms the page.

The research's starting values (a synthesis, check by eye): hero
`clamp(2.75rem, 1.5rem + 5vw, 6rem)`, H2 `clamp(2rem, 1.4rem + 2.5vw,
3.5rem)`, body at least 1rem. Well-made reference pages set the h1 between
64 and 96 px on desktop for 1 to 8 words. Usually these sizes already live in
`--text-*` tokens; this section is for reading them and for any space value
the tokens leave out.

## Logical properties

Write `padding-block`, `padding-inline`, `margin-inline: auto`,
`inline-size`, `max-inline-size`, `inset-inline-start` and
`border-block-start` instead of the top, left, width forms. Two reasons:
the axis is written into the property name, which is what keeps section and
container from overlapping, and the layout stays correct if the page is
ever set right to left.

## Colour with oklch()

Colour values come from the tokens. In page CSS you mostly derive states.

Support figures from the research (caniuse, read 2026-10-04): `oklch()`
Chrome 111, Safari 15.4, Firefox 113, 94.25% global; `color-mix()` Chrome
111, Safari 16.2, Firefox 113, 93.91%; relative colour syntax Chrome 131,
Safari 18.0, Firefox 133, 92.29%. None is universal, so each derived value
sits in `@supports` with a plain token as the base:

```css
.button { background: var(--color-action); color: var(--color-on-action); }

/* Hover only where a pointer can hover, so a tap on a touch screen
   does not leave the hover colour stuck. */
@media (hover: hover) {
  .button:hover { background: var(--color-action-hover); }
}
.notice { background: var(--color-bg); }
@supports (color: color-mix(in oklch, red, blue)) {
  .notice { background: color-mix(in oklch, var(--color-action) 12%, var(--color-bg)); }
}
@supports (color: oklch(from red l c h)) {
  .tag { background: oklch(from var(--color-action) 0.95 0.03 h); }
}
```

No JavaScript theming. If the direction defines a dark theme, it arrives as
a second set of token values; add `<meta name="color-scheme" content="light dark">`
only when that set exists.

## Container queries

Use a container query when a component changes shape with its own width
rather than the viewport's: a feature block that is full width in one
section and half width in another.

```css
.feature { container-type: inline-size; }
.split { display: grid; gap: var(--space-8, calc(var(--text-base) * 2)); }            /* base: stacked */
@container (min-width: 44rem) {
  .split { grid-template-columns: minmax(0, 5fr) minmax(0, 7fr); align-items: center; }
}
```
```html
<div class="container feature">
  <div class="split">
    <div>text</div>
    <div>media</div>
  </div>
</div>
```

The query styles descendants of the container, never the container itself,
which is why `.split` sits inside `.feature`. The research has no support
figures for container queries, so write the stacked base first: a browser
that ignores the query still shows a readable single column.

`minmax(0, 5fr)` rather than `5fr` stops a long word or a wide image from
forcing the column wider than the screen.

## Layouts that avoid the uniform card grid

On 30 well-made reference pages, no main section was a three-card "icon,
title, sentence" grid; sections were named after a job and given their own
shape. Pick the shape from the content.

### Hero: text and media with named areas

One option among many: the composition of the hero is the direction's
decision, so follow `direction.md` when it gives one. This recipe shows the
mechanism, named grid areas, which keeps parts from landing on top of one
another the way hand-numbered column and row spans did in one test build.

```css
.hero { display: grid; gap: var(--space-gutter); grid-template-areas: "title" "media" "lede" "action"; }
.hero__title  { grid-area: title; }
.hero__lede   { grid-area: lede; }
.hero__action { grid-area: action; }
.hero__media  { grid-area: media; }
@container (min-width: 56rem) {
  .hero {
    grid-template-columns: minmax(0, 7fr) minmax(0, 5fr);
    grid-template-rows: auto auto 1fr;
    grid-template-areas:
      "title  media"
      "lede   media"
      "action media";
  }
}
```
```html
<div class="container feature">
  <div class="hero">
    <h1 class="hero__title">...</h1>
    <p class="hero__lede">...</p>
    <p class="hero__action"><a class="button" href="#book">...</a></p>
    <div class="hero__media">...</div>
  </div>
</div>
```

- The narrow order is written out in the first `grid-template-areas`: here
  the media comes straight after the headline, so a phone shows the product
  before the fold, not under the form. The DOM keeps the reading order;
  only a non-interactive element (the media) moves, so keyboard order is
  unaffected.
- Every area name appears in one rectangle per template, and each child
  sits in one area, so two children cannot share a cell.
- The `.feature` wrapper supplies the container the `@container` rule needs.

Check at 360, 768, 1280 and 1440 px in the console; it prints `0` when no
two parts overlap:

```js
(() => { const r = [...document.querySelector('.hero').children].map((e) => e.getBoundingClientRect()); return r.flatMap((a, i) => r.slice(i + 1).filter((b) => a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom)).length; })()
```

### One feature, one section

A text column and a media column of unequal width. Vary the ratio and the
side between sections when the direction calls for it, instead of mirroring
the same split all the way down.

```css
.split--media-first > :first-child { order: 1; }   /* inside the container query */
```

### A lead item and a short list

One thing matters most; the rest support it. Like every `@container` rule,
this one needs an ancestor with `container-type: inline-size`; the
`.feature` wrapper from "Container queries" provides it. Without one, the
query never matches and the list stays stacked.

```css
.lead-list { display: grid; gap: var(--space-8, calc(var(--text-base) * 2)); }
@container (min-width: 48rem) {
  .lead-list { grid-template-columns: minmax(0, 3fr) minmax(0, 2fr); }
}
```
```html
<!-- Invented example: a weekend bread class. -->
<div class="container feature">
  <div class="lead-list">
    <article><h3>You bake three loaves and take them home</h3><p>...</p></article>
    <ul><li>Starter in a jar to keep</li><li>Printed timings sheet</li></ul>
  </div>
</div>
```

### Steps as an ordered list

Steps are a sequence, so they are an `ol`. Large numerals carry the order.

```css
.steps { list-style: none; padding: 0; margin: 0; counter-reset: step; display: grid; gap: var(--space-8, calc(var(--text-base) * 2)); }
.steps > li { counter-increment: step; display: grid; grid-template-columns: 3ch 1fr; align-items: baseline; gap: var(--space-4, var(--text-base)); }
.steps > li::before { content: counter(step); font-family: var(--font-display); font-size: var(--text-2xl, calc(var(--text-base) * 2)); line-height: 1; }
```

### Specifications as a definition list

Product details (size, material, weight, ship date) read faster as
label and value pairs than as cards.

```css
.specs { display: grid; grid-template-columns: max-content 1fr; gap: var(--space-2, calc(var(--text-base) * 0.5)) var(--space-8, calc(var(--text-base) * 2)); margin: 0; }
.specs dt { color: var(--color-ink-muted); }
.specs dd { margin: 0; }
```
```html
<!-- Invented example: a sailing jacket. -->
<dl class="specs"><dt>Weight</dt><dd>[PLACEHOLDER: weight in grams]</dd><dt>Shell</dt><dd>[PLACEHOLDER: fabric and rating]</dd></dl>
```

### An index of work

Studios and shops can list work as rows of name, discipline and year, each
row a link, instead of a grid of identical thumbnails.

```css
.index { list-style: none; margin: 0; padding: 0; }
.index a { display: grid; grid-template-columns: minmax(0, 1fr) auto; gap: var(--space-4, var(--text-base)); padding-block: var(--space-3, calc(var(--text-base) * 0.75)); border-block-end: 1px solid var(--color-line); color: inherit; text-decoration: none; }
```

## Header and navigation

Keep the section links visible at every width. With three or four links,
let the list wrap under the logo on phones:

```css
.site-header .container { display: flex; flex-wrap: wrap; align-items: center; justify-content: space-between; gap: var(--space-3, calc(var(--text-base) * 0.75)) var(--space-6, calc(var(--text-base) * 1.5)); padding-block: var(--space-4, var(--text-base)); }
.site-nav ul { display: flex; flex-wrap: wrap; gap: var(--space-2, calc(var(--text-base) * 0.5)) var(--space-4, var(--text-base)); list-style: none; margin: 0; padding: 0; }
.site-nav a, .wordmark { display: inline-flex; align-items: center; min-block-size: 2.75rem; color: inherit; }
```

`display: none` on the links at small widths, with no menu in their place,
left one unguided build's sections unreachable from the header on phones.

A fixed or sticky header covers the top of whatever it scrolls to and, in
one unguided build, overlapped the h1 at 360px. Prefer a header in normal flow. If
the direction asks for a sticky one, add
`html { scroll-padding-block-start: <header height>; }` and check the hero at
360px.

## Interactive states

Every control gets default, hover, `:focus-visible`, `:active` and, for
buttons that submit, `:disabled`. Hover rules go inside
`@media (hover: hover)`, as in "Colour with oklch()", so touch screens do
not keep a stuck hover colour; `:focus-visible`, `:active` and `:disabled`
stay outside it, because keyboard and touch users need them.

Under `prefers-reduced-motion: reduce`, movement is what gets reduced, not
feedback. Never write a global rule that zeroes every transition or
animation; a short colour change on hover, focus or press stays, and
`landing-skills:landing-motion` removes only parallax, scrubbing and
autoplay. This skill writes no transitions, so it has nothing to reduce.

```css
:focus-visible { outline: 3px solid var(--color-ink); outline-offset: 3px; }
.band { background: var(--color-band, var(--color-bg)); color: var(--color-on-band, var(--color-ink)); }
.band :focus-visible { outline-color: var(--color-on-band, var(--color-ink)); }   /* one line per ground that holds controls */
.button { display: inline-flex; align-items: center; justify-content: center; min-block-size: 2.75rem; padding-inline: var(--space-6, calc(var(--text-base) * 1.5)); border: 0; border-radius: var(--radius-control); font: inherit; text-decoration: none; cursor: pointer; }
.button:active { translate: 0 1px; }
.button:disabled { cursor: progress; opacity: 0.7; }
```

`display: inline-flex` matters on links: `min-block-size` does nothing on
an inline `<a>`, and in testing an `a.button` without it rendered well under
44 px tall.

The ring sits outside the control (`outline-offset`), on the ground, so it
needs 3:1 against that ground. Choose its colour per ground from the pair
that reaches 3:1. Default to `--color-ink`: art direction checked it as
text on the page ground, so it clears 3:1 there. On every other ground use
that ground's `on-` token. The action colour makes a good ring only where
it measures 3:1 against the ground; in one test build it measured 1.06:1.
Check each pair from the hex values in `tokens.css` with the contrast
script in `forms-a11y.md`, then tab through each band and look.

Respond to hover with a change of colour or underline; a card or button that grows on
hover is a catalogued tell.

## Media placeholders

When the photograph does not exist yet, hold its place at the final ratio
and say what goes there:

```css
.media-placeholder {
  aspect-ratio: 4 / 3;               /* the ratio from direction.md */
  display: grid; place-items: center; padding: var(--space-6, calc(var(--text-base) * 1.5));
  border: 1px dashed currentColor;    /* follows the text colour of whatever ground it sits on */
  font-size: var(--text-sm, var(--text-base)); text-align: center;
}
```
```html
<div class="media-placeholder">[PLACEHOLDER: photo, the workshop bench from above, 4:3]</div>
```

A placeholder that stands in for a motion or 3D moment from the
direction's `Motion and 3D intent` also carries `data-moment="<name>"`
with the direction's name for it (see `SKILL.md`, step 10).

Plain visible text, no `role="img"`. Keep the placeholder's size close to the
real image's so the page height is honest: one unguided build stacked
several placeholder boxes with tall minimum heights and pushed the product image
below the form on phones.

Give each placeholder the ratio and treatment `direction.md` gives that
image, so the frames read as the planned layout and not as a row of
identical boxes: its own aspect ratio, its width (full width, column,
inset beside text), and the ground it sits on. Set the ratio per element
and vary the frame with a modifier:

```css
.media-placeholder--filled { border: 0; background: var(--color-surface, var(--color-bg)); color: var(--color-on-surface, var(--color-ink)); }
```
```html
<section class="section">
  <!-- A full-width image sits in the section, outside the container. -->
  <div class="media-placeholder media-placeholder--filled" style="aspect-ratio: 21 / 9">[PLACEHOLDER: photo, the workshop from the street door, 21:9, full width]</div>
  <div class="container feature">
    <div class="split">
      <p>...</p>
      <!-- A portrait beside text, not stacked full width, keeps the page short. -->
      <div class="media-placeholder" style="aspect-ratio: 4 / 5">[PLACEHOLDER: portrait, the mechanic at the stand, 4:5]</div>
    </div>
  </div>
</section>
```

`var(--color-surface, var(--color-bg))` uses the optional surface pair when
the file has it and the page ground otherwise; the fallback inside `var()`
is a token, never a literal. The dashed border uses `currentColor`, so on a
dark band it takes the band's `on-` colour with the text instead of
vanishing; a fixed muted colour did vanish there in testing.

## Text setting

- Measure: 45 to 90 characters per line, target 62 to 72 (`max-inline-size:
  65ch` on paragraphs, or the measure token).
- `text-wrap: balance` on headings. Research figures: Chrome 130, Safari
  17.5, Firefox 121 to 124 depending on the source, 92.73% global. Browsers
  without it wrap normally, so it needs no fallback.
- Nothing that carries information (price, ship date, what happens after the
  form) goes below the body size. Captions and hints use the smallest text
  token and still meet 4.5:1 contrast on the surface they sit on. Unguided builds
  set the price and delivery note in small print and put small captions on
  a tinted band below AA.
- Caps labels need positive tracking (`--text-tracking-*` if the tokens
  define it).
