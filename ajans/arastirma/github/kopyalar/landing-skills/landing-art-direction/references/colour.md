# Colour

Read this at step 5 of the process, after the direction and the anchors are
written.

## Contents

- Why OKLCH
- Method: from the subject to a palette (the ink and the ground are derived
  too)
- Tinted neutrals
- The accent and its one job (links, first-reach accents, colour
  convergence check)
- Cream, paper and other warm grounds
- Dark mode designed on purpose
- Contrast: the pass or fail test
- Writing the tokens (text tokens per ground, accent on a band, dark theme
  skeleton)
- Browser support and fallbacks

## Why OKLCH

`oklch(L C H)` has three numbers: lightness L from 0 to 1, chroma C from 0
(grey) to about 0.37 on current screens, and hue H from 0 to 360. Hue 0 is
near magenta and about 41 is red, so HSL hue numbers do not carry over.

L is perceptually even across hues, which HSL's lightness is not. That lets
you build ramps where step 5 of the accent and step 5 of the neutral look
equally light, and change hue without the colour jumping in brightness.

OKLCH L is still not a contrast ratio. See "Contrast" below.

## Method: from the subject to a palette

1. Take the three anchors from `direction.md`: a material, a product
   colour, a place, the light at a time of day. Estimate each as OKLCH and write the three
   numbers down. If a photo or logo exists, sample it. If not (the brief
   gives only names such as a colourway or a material, or a description),
   estimate from them and mark the value provisional in `direction.md`.
   Write which asset will confirm it, and what changes if the real colour
   differs: which tokens move, and which contrast pairs must be checked
   again.
2. Choose the brand hue from the dominant anchor. Do not start in the
   indigo and violet region (hue about 260 to 290) unless the subject is that
   colour or the brand requires it. That region is the most common default of
   generated pages, from Tailwind's indigo buttons.
3. Build the neutral ramp in the same hue (or the warm or cool partner of the
   accent) with low chroma. See "Tinted neutrals".
4. Build the accent ramp: 9 to 12 steps, L from about 0.97 down to 0.15. Put
   peak chroma in the middle of the ramp, at L 0.55 to 0.70, and reduce it
   towards both ends. Peak chroma about 0.12 to 0.20 for a calm brand, up to
   0.22 to 0.25 for a loud one. For comparison, Tailwind's blue-500 is
   `oklch(62.3% 0.214 259.815)`.
5. Give success, warning and danger their own hues at the same L steps as the
   accent, so they sit at the same visual weight. Landing pages often need
   only danger (form errors) and success (form sent).
6. Assign roles. A 12-step ramp maps well to roles: steps 1 and 2 page
   backgrounds, 3 to 5 component backgrounds (rest, hover, pressed), 6 to 8
   borders, 9 and 10 solid fills, 11 low-contrast text, 12 high-contrast text.
   A landing page usually needs eight to twelve colour tokens in total. Name
   tokens by role (`--color-ink`, `--color-action`), not by hue, so
   `landing-skills:landing-build` never has to guess.

Starting L steps for a 12-step ramp, a synthesis to adjust by eye:
0.98, 0.95, 0.91, 0.86, 0.79, 0.71, 0.62, 0.53, 0.44, 0.35, 0.26, 0.18.

Hover and pressed states can come from the token with relative colour
syntax, and tints from `color-mix()`. Both belong in the build CSS behind
`@supports`, since relative colour reaches fewer browsers:

```css
@supports (color: oklch(from red l c h)) {
  .button:hover { background: oklch(from var(--color-action) calc(l - 0.06) c h); }
}
.notice { background: color-mix(in oklch, var(--color-action) 12%, var(--color-bg)); }
```

When the state matters for contrast (a pressed button with text on it),
write it as its own token, such as `--color-action-hover`, and check its
ratio.

### The ink and the ground are derived too

The darkest text colour and the page ground are the two values agents most
often reach for without deriving: a blue-tinted near-black on a cool pale
ground. That pair is one option among many. The ink's hue follows the
subject like every other colour:

- a warm ink (hue roughly 40 to 80) from tar, smoke, walnut, peat, rust;
- a green ink (hue roughly 130 to 170) from moss, pine, deep water, slate
  roofs that have weathered green;
- a neutral ink (chroma under about 0.005) from iron, charcoal, black paint;
- a cool ink (hue roughly 200 to 250) from steel, night sky, a winter sea,
  when the subject really is cool.

The ground follows the same anchor or its partner. A tinted near-white, a
deeper tone, or a dark ground are all valid; record which anchor gave it.

### A worked derivation

The values below belong to one invented client. Do not reuse them; the
colour convergence check below counts them.

Subject: a river-ferry operator whose boats are painted moss green, with
signal-yellow markings on the boarding ramps. Anchors: the moss-green hull
paint (hue 150) and the signal yellow (hue 95).

| Role | oklch | hex | Note |
|---|---|---|---|
| bg | 0.97 0.01 140 | #f2f7f0 | hull green, barely tinted |
| ink | 0.26 0.035 150 | #17291b | green-black from the hull; 14.12:1 on bg |
| ink-muted | 0.47 0.03 150 | #4f6052 | 6.19:1 on bg |
| marker (fill) | 0.84 0.15 95 | #e9c944 | large yellow fills; ink on it is 9.42:1 |
| marker-text | 0.50 0.11 95 | #776100 | the same hue as text on bg, 5.50:1 |

The yellow at full strength fails as text on a light ground, so the text
version drops to L 0.50 and chroma 0.11. Same hue, two jobs, two tokens.

## Tinted neutrals

Pure `#fff` and `#000` read as unconsidered next to a palette that has a
hue. Give the neutrals the brand hue at low chroma:

- Light end: C 0.004 to 0.02. Around 0.005 to 0.015 the tint is felt more
  than seen.
- Middle and dark end: up to about 0.03 to 0.045. Tailwind's slate runs
  0.003 at its lightest, 0.046 at 500 and 0.042 at 950.
- Above 0.03 on a background, it reads as a coloured page, not a neutral.
  That can be the point, but decide it.

Well-made pages keep neutral chroma tiny and the hue constant: Supabase's
dark ground is `oklch(0.19 0.0025 157.5)`, Linear's is `#08090A`, Cursor's
is a warm black `#14120B`.

## The accent and its one job

- One accent. Use it for the primary action and at most one highlight per
  section. Neutrals carry everything else. The 60-30-10 split
  is a convention with no source behind it; the useful part is that the
  accent is the smallest share.
- The accent is the colour of "do this". If it also colours icons, borders,
  headings and backgrounds, it stops pointing at the action.
- `--color-action` is the control colour: the fill of the primary button.
  Inline links in running text use the ink with an underline by default.
  Use the action colour for links only when it passes 4.5:1 as text on the
  ground where the link sits; a bright accent that works as a fill often
  fails as text, and then it stays on the buttons.
- Status colours (error, success, warning) are not a second accent. They
  mark a state where it occurs and nowhere else. Keep them clearly apart in
  hue from the action colour, so an error message never reads as a button;
  if the action colour is red, move the error hue away from it and pair the
  error with a text label or icon. Check their contrast like any text.
- No gradient as the main colour device. If the brand has a signature
  gradient (Stripe's animated canvas is one), it is a brand asset, recorded
  as such; otherwise use flat colour.
- No coloured glows, no blurred colour orbs, no coloured left border on
  cards. These fake emphasis; spacing, size and a plain rule do it honestly.
- A shadow, if any, is tinted with the ink colour, for example
  `0 1px 2px color-mix(in oklch, var(--color-ink) 12%, transparent)`, and
  used only on elements that
  really sit above others. A black shadow at 10 percent on every card is the
  framework default.
- Choose a temperature that departs from the category default when the brand
  allows. Observed: a warm brown on a mental-health page (Dawn, `#321C04`)
  where the category runs cold blue and green; a warm black on a coding tool
  (Cursor); a paper tone on an agent payments product (Agentcard,
  `#F2F1EC`) in a category that usually goes dark.

### First-reach accents

Besides the defaults this file already names (Tailwind indigo and violet,
terracotta on cream), readers of this skill kept reaching for two accents
across unrelated clients:

- acid lime or chartreuse (a very light, very saturated yellow-green, OKLCH
  hue roughly 115 to 130), usually on a dark band;
- a stamp or vermilion red (a saturated red, hue roughly 25 to 35) on a pale
  ground.

Neither is banned. Each is allowed only when derived from one of your
anchors, and it falls under the colour convergence check below.
Treat any accent you had in mind before deriving the palette as first-reach,
listed here or not; this list will go stale as habits move.

A warning sign: when the accent fails contrast on the page ground and needs
a border or a dark band to work, the colour is fighting the page. One reader
measured 1.26:1 for a lime accent on its ground and added an ink border to
rescue it. When that happens, choose another accent, or give that colour
another role (a large fill behind ink text, a band ground) and derive a
separate action colour that passes on the ground.

### Colour convergence check

This is the one statement of the rule; `SKILL.md` points here. Run it once
the palette is derived, not before. If your ink, ground or accent lands
within a few hundredths of L and C and about 15 degrees of hue of a value
shown anywhere in this skill (this file, `example.md`), or is one of the
first-reach accents above, either derive it again from your anchors, or keep
it and write the sentence that says why this client needs it, one that would
be false for another client.

## Cream, paper and other warm grounds

Cream is not a tell by itself. Aesop (`#FFFEF2`), Agentcard, PostHog
(`#EEEFE9`) and Arc (`#FFFCEC`) use warm paper grounds, each for a reason in
the brand.

The tell is the bundle chosen from a fashionable list: cream background,
terracotta accent and Instrument Serif or Fraunces as the display face. It is
the second-order default that agents reach for when told to avoid the purple
look. Most unguided test pages for this skill also landed on cream with one
dark green or terracotta accent.

The separator is derivation. Use a warm ground when you can name the anchor
it comes from (the client's paper stock, undyed linen, a limewashed wall),
and record it in OKLCH. The accent follows the same rule: terracotta is fine
when it was derived from the subject, with the reason recorded, and a preset
when it was not. If you cannot name the anchor, the ground is a preset.

## Dark mode designed on purpose

Ship a dark theme only when the brand or the audience calls for it. Notion
commits to light, Linear to dark; both are fine. A dark theme made by
inverting the light one is a tell, and so is permanent dark mode with
medium-grey body text.

When you design one, design it as a second palette:

1. Keep the same hues.
2. Background L 0.15 to 0.20 with C 0.01 to 0.02 in the neutral hue. Avoid
   pure black.
3. Raise each elevation level (cards, menus) by L +0.02 to +0.04. Lighter
   surfaces show depth; shadows barely read on dark.
4. Body text L 0.90 to 0.93, not pure white. Secondary text L 0.72 to 0.78.
5. Accent: reduce chroma by 10 to 25 percent and raise L by 0.05 to 0.12, so
   it keeps contrast without glowing.
6. Re-check every pair. Do not assume the light theme's ratios carry over.

The ferry example above, as a dark theme:

| Role | oklch | hex | Contrast |
|---|---|---|---|
| bg | 0.18 0.02 150 | #0b140d | |
| surface (one level up) | 0.21 0.022 150 | #111b13 | |
| ink | 0.92 0.01 150 | #e0e6e1 | 14.84:1 on bg |
| ink-muted | 0.75 0.015 150 | #a8b1a9 | 8.47:1 on bg, 7.97:1 on surface |
| marker | 0.88 0.12 95 | #f0d777 | 13.09:1 on bg; chroma cut 20 percent |
| (rejected) grey text | 0.55 0.015 150 | #6c746d | 3.89:1: fails body text |

In CSS, put the dark values in a `@media (prefers-color-scheme: dark)` block
that redefines the same `--color-*` tokens on `:root`. The build sets
`color-scheme: light dark` on the page so form controls follow. Support for
`light-dark()` was not confirmed by the research; the media query is the safe
route. "Writing the tokens" below shows how the dark block fits the
hex-first pattern.

Record the theme decision in the `Colour` section of `direction.md`: light
only, dark only, or both. The tokens file holds no `color-scheme` property;
`landing-skills:landing-build` sets `color-scheme: light`, `dark` or
`light dark` on the page to match, so form controls and scrollbars follow
the theme you designed.

## Contrast: the pass or fail test

WCAG 2.2 ratios decide pass or fail:

- Normal text: at least 4.5:1 (SC 1.4.3).
- Large text, at least 18pt (24px) or 14pt (about 18.7px) bold: at least 3:1.
- Controls and meaningful graphics (input borders, focus rings, icons that
  carry meaning) against what is next to them: at least 3:1 (SC 1.4.11).
- Do not round: 4.499:1 fails.
- Exempt: decoration, disabled controls, logotypes, incidental text.

APCA is a draft for WCAG 3, and the draft says the contrast algorithm is not
settled. You may look at APCA as an extra design check; it is not the test.

OKLCH lightness is not a contrast ratio. The same L gives different ratios
on different grounds and at different hues. Computed examples:

| Neutral grey at L | on white | on black |
|---|---|---|
| 0.40 | 9.21:1 | 2.28:1 |
| 0.50 | 6.00:1 | 3.50:1 |
| 0.55 | 4.85:1 | 4.33:1 |
| 0.60 | 3.95:1 | 5.32:1 |
| 0.70 | 2.67:1 | 7.86:1 |

Chromatic colours at C 0.15 on white: at L 0.55, hue 30 gives 5.22:1, hue
145 gives 4.56:1 and hue 260 gives 4.93:1, so all pass, hue 145 by a hair.
At L 0.60 the same hues give 4.23:1, 3.71:1 and 4.00:1, and all fail for
body text. Compute every pair.

### How to check

Save this as a scratch file (outside the user's project) and run it with
Node. It converts OKLCH to sRGB and prints the WCAG 2.2 ratio and the hex
value to use as the fallback.

```js
// contrast.mjs: node contrast.mjs "<foreground oklch()>" "<background oklch()>"
// Sanity test: node contrast.mjs "oklch(0 0 0)" "oklch(1 0 0)" prints 21.00:1
function toLinearSrgb(str) {
  const [L, C, H] = str.match(/[\d.]+/g).map(Number);
  const l = L > 1 ? L / 100 : L; // accepts 0.62 or 62%
  const a = C * Math.cos((H * Math.PI) / 180);
  const b = C * Math.sin((H * Math.PI) / 180);
  const l_ = (l + 0.3963377774 * a + 0.2158037573 * b) ** 3;
  const m_ = (l - 0.1055613458 * a - 0.0638541728 * b) ** 3;
  const s_ = (l - 0.0894841775 * a - 1.291485548 * b) ** 3;
  return [
    4.0767416621 * l_ - 3.3077115913 * m_ + 0.2309699292 * s_,
    -1.2684380046 * l_ + 2.6097574011 * m_ - 0.3413193965 * s_,
    -0.0041960863 * l_ - 0.7034186147 * m_ + 1.707614701 * s_,
  ].map((v) => Math.min(1, Math.max(0, v))); // clipped: out-of-gamut colours are approximate
}
const luminance = (rgb) => 0.2126 * rgb[0] + 0.7152 * rgb[1] + 0.0722 * rgb[2];
const hex = (rgb) => '#' + rgb.map((v) => {
  const s = v <= 0.0031308 ? 12.92 * v : 1.055 * v ** (1 / 2.4) - 0.055;
  return Math.round(s * 255).toString(16).padStart(2, '0');
}).join('');
const [fg, bg] = process.argv.slice(2).map(toLinearSrgb);
const [hi, lo] = [luminance(fg), luminance(bg)].sort((x, y) => y - x);
const ratio = (hi + 0.05) / (lo + 0.05);
console.log(`${hex(fg)} on ${hex(bg)}: ${ratio.toFixed(2)}:1`,
  ratio >= 4.5 ? 'passes 4.5:1 (body text)' : ratio >= 3 ? 'passes 3:1 only (large text, UI parts)' : 'fails');
```

It takes `oklch()` values with plain numbers (no `none`, no `from`). Values
with very high chroma may be outside sRGB; the script clips them, so treat
their ratio as approximate and pull chroma down. Once the page is built,
confirm with the browser's devtools contrast readout on the real rendering.

Record every pair you will use in the contrast table of `direction.md`,
including the pairs that fail and are kept only for decoration, with the
reason.

## Writing the tokens

Hex values first on `:root`, then the `oklch()` values in an `@supports`
block. Two declarations of one custom property in the same rule do not give a
fallback: the custom property takes the last value whether or not the
browser can use it as a colour.

The skeletons below are templates. Each `<slot>` stands for a value you
derived and recorded in `direction.md`; the CSS is not valid until every slot
is replaced. They hold no colours on purpose, so that no value travels from
this file into a client's palette.

```css
:root {
  --color-bg: <ground, hex>;
  --color-ink: <ink, hex>;
}
@supports (color: oklch(0 0 0)) {
  :root {
    --color-bg: <ground, oklch()>;
    --color-ink: <ink, oklch()>;
  }
}
```

### Text tokens for each ground

Every ground that carries text gets its own text token, named
`--color-on-<ground>`: `--color-on-surface`, `--color-on-band`,
`--color-on-action`. Add `--color-on-<ground>-muted` if captions sit there
too. The value may equal `--color-ink`; the separate name still matters,
because it tells `landing-skills:landing-build` which colour goes on which
ground, and it keeps a background token from being used as a text colour on
a dark or coloured band. Each pair gets a computed ratio in the contrast
table of `direction.md`.

```css
:root {
  --color-band: <band ground, hex>;
  --color-on-band: <text on the band, hex>;
  --color-on-band-muted: <captions on the band, hex>;
}
```

The accent needs the same care. An accent that passes on the page ground can
fail on a dark band: in the ferry example, the marker-text yellow (L 0.50)
gives 5.50:1 on the light ground and 2.57:1 on a band of the green ink. Two
ways out, both recorded with their ratios:

- a lighter step of the accent as its own token for use on the band, such as
  `--color-action-on-band` (in the ferry example the fill yellow at L 0.84
  gives 9.42:1 on the band); or
- keep the accent off the band, and put the band's action on the page
  ground.

### With a dark theme

Each block gets its own dark section. Order matters: inside `@supports`, the
dark rule comes after the light one so it wins in dark mode.

```css
:root {
  --color-bg: <light ground, hex>;
  --color-ink: <light ink, hex>;
}
@media (prefers-color-scheme: dark) {
  :root {
    --color-bg: <dark ground, hex>;
    --color-ink: <dark ink, hex>;
  }
}
@supports (color: oklch(0 0 0)) {
  :root {
    --color-bg: <light ground, oklch()>;
    --color-ink: <light ink, oklch()>;
  }
  @media (prefers-color-scheme: dark) {
    :root {
      --color-bg: <dark ground, oklch()>;
      --color-ink: <dark ink, oklch()>;
    }
  }
}
```

A browser without `oklch()` uses the first two blocks. A browser with it
reads all four in order, and the last matching block wins.

## Browser support and fallbacks

Figures from caniuse, read on 2026-10-04:

| Feature | Chrome, Edge | Safari | Firefox | Global |
|---|---|---|---|---|
| `oklch()` | 111 | 15.4 | 113 | 94.25% |
| `color-mix()` | 111 | 16.2 | 113 | 93.91% |
| Relative colour syntax | 131 | 18.0 | 133 | 92.29% |

About 6 percent of visitors need the hex fallback for `oklch()`, and about 8
percent cannot use relative colour. Hence hex first, and `@supports` around
anything relative.
