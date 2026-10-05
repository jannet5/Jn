# Layout

Read this at step 6 of the process, with `landing/copy.md` open. Every H2 in
the copy gets a row in the section map of `direction.md`.

The research behind this skill did not measure grids, gutters or max-widths
on the well-made pages it studied. The numbers here are starting values from
judgement. Check them at 360, 768, 1280 and 1440px wide with the real copy.

## Contents

- The section map
- Grid
- Vertical rhythm
- Variety between sections
- Alignment
- Where to spend boldness (the inverted dark band is a choice)
- Hero patterns
- Navigation
- Failures seen in unguided pages, and the fix

## The section map

Write one row per copy section:

| Copy section | Width | Alignment | Density | Ground | Visual |
|---|---|---|---|---|---|
| (H2 from copy.md) | columns or full-bleed | left, or centred with reason | open or dense | bg, surface, accent band | what it shows, or none |

The map decides whether sections differ. If every row says the same
thing, the page will be one uniform column of boxes. Change at least width,
density or ground between neighbouring sections, and make one or two rows
unlike all the others.

A section exists to answer a question the visitor has before acting. Its
layout follows its content: a timeline wants a line, a comparison wants
columns, a single claim wants space around it, a set of three equal items
wants a list more often than three cards.

## Grid

A 12-column grid gives halves, thirds, quarters and asymmetric splits (7 and
5, 8 and 4) from one system. Record the max content width, the column gap
and the side gutter as tokens.

```css
.wrap {
  display: grid;
  grid-template-columns: repeat(12, minmax(0, 1fr));
  column-gap: var(--space-6);
  max-width: var(--space-content-max);
  margin-inline: auto;
  padding-inline: var(--space-gutter);
}
.statement { grid-column: 2 / span 8; }
.aside { grid-column: 9 / -1; }

@media (max-width: 48rem) {
  .statement,
  .aside { grid-column: 1 / -1; }
}
```

Starting values: content max-width 68 to 80rem, column gap 1.5rem, side
gutter `clamp(1rem, 0.5rem + 2.5vw, 2.5rem)`. Text blocks stay within their
measure (62 to 72ch) even when their columns are wider.

Full-bleed sections sit outside `.wrap` and put a `.wrap` inside for their
content. Use them for the one or two moments the direction makes loud: a
photograph, a coloured band, a large statement.

## Vertical rhythm

Uniform padding on every section (the same `py-24`, the same 64px top and
bottom) flattens the page. Give the section padding two or three values with
a job each, as tokens:

- `--space-section-dense` for sections read quickly: FAQ, specs, logistics.
- `--space-section-open` for the one or two sections that carry the claim.
- Inside a section, space by relationship: a heading sits closer to its own
  text than to the previous block. A useful check is that the gap above a
  heading is at least twice the gap below it.

Section breaks can come from a change of ground, a rule, or space alone. Use
more than one kind on a page.

## Variety between sections

Tells to avoid and what to do instead:

- Three equal cards with an icon, a title and two lines. Use a list with real
  weight differences, a 2 and 1 split where one item leads, prose with inline
  emphasis, or a sequence on a line. If the three items truly are equal and
  short, a plain list is clearer than cards.
- The zig-zag: text left and image right, then mirrored, repeated. Vary the
  ratio (7 and 5, then full-bleed, then 4 and 8), or let one feature take a
  whole section with its own visual.
- Every h2 at the same size. Give the section that carries the claim a larger
  heading step or a different treatment; let logistics headings be smaller.
- Decorative `01 02 03` on content that is not a sequence, or on an ordered
  list that already numbers itself. Use numbers only when they mean
  something: clock times, dimensions, prices, dates, step counts the visitor
  will follow.

## Alignment

Pick the page's main axis, usually the left edge of the text column, and
keep it. Mixed alignment reads as accidental.

- Left-aligned text with an asymmetric visual is the safe strong default for
  hero and body sections. Text set to about 60ch, visual in the remaining
  columns.
- Centre a section only when it is a single short statement and you decide
  it should stand apart. Write the reason in the map. A centred closing CTA
  under a left-aligned page needs that reason, or it should share the left
  axis.
- When two columns have very different heights (a short heading beside a
  long form), fill the short one with content that belongs there (what
  happens after submitting, response times, the address), or make it
  `position: sticky` so it stays beside the form while scrolling:

```css
.form-intro { position: sticky; top: var(--space-8); align-self: start; }
```

- A narrow block in a wide container (a 420px pricing card in a 1040px row)
  leaves a hole. Either centre it on purpose with something on both sides,
  pair it with content (what is included, the guarantee, a short FAQ), or
  give it the columns it needs.

## Where to spend boldness

Pick one or two places where the page is loud: a very large headline, a
full-bleed photograph, a band of the accent ground, a type-only statement.
Everything else stays quiet so those moments read. A page where every
section has a big visual idea has none.

Choose them by this test: the section that answers the "must believe" line
in `direction.md` (the visitor's main fear or doubt), and the section that
shows the real thing (product, place, work) when an asset or a strong type
treatment can carry it. Short or logistic sections (FAQ, specs, footer, the
logo row) are never the loud ones.

### The inverted dark band is a choice

A full-width section that flips to a dark ground with light text appeared
in every test run of this skill, whatever the client. It works, which is why
it became a habit. Use it only when the content needs a change of ground at
that point, and write the reason in the section map: for example, the
section moves the reader from one state to another (day to night, before to
after, outside to inside), or it shows something that is itself dark (a
product in a dark finish, a night scene, a screen in dark mode).

Other ways to make one section loud without inverting the ground:

- Scale on the same ground: one statement at display size, alone, with open
  rhythm above and below and nothing else in the section.
- A full-bleed image or drawing that carries the section, with the text set
  beside or below it on the page ground.
- A step of the palette instead of an inversion: a tinted surface or a light
  band from the anchor colour, with the ink unchanged.
- A change of width or alignment: the one section that breaks the main
  column, runs off-grid, or sits on a different axis.

If you do use a dark band, it is one of the page's one or two loud moments,
and its text and accent pairs get their own contrast check
(`colour.md`, accent on a band).

## Hero patterns

The well-made pages studied showed the real thing in the hero in one of five
ways. Pick one from the direction and the available assets:

1. Product UI, drawn as image or SVG (Linear, Lightfield).
2. Video, scrubbed or looping, with first and last frame stills (Mercury).
3. A WebGL scene (Lusion, basement.studio).
4. The product photographed (Apple, Fellow, Teenage Engineering).
5. Type only (Figma's Config, Wabi).

None of the 30 pages used stock photography of people at laptops. Type-only
is a strong choice when there is no asset yet: a large headline on the
direction's ground, set well, needs no picture.

Hero layout notes:

- The headline and the primary action are visible without scrolling at 360px
  wide.
- When text overlaps a visual (a canvas, a photograph), test the overlap at
  360 and 1440px. If any letter sits on a busy or light area, move the text
  into its own columns, give it a ground, or move the visual.
- A hero video needs a visible pause control.

## Navigation

Short, 6 to 9 items on the pages studied, with sign-in and the call to action
at the end. On a one-page landing, three or four anchor links and the action
are usually enough. A skip link to the main content goes first.

Small brand-owned details in the nav or footer help a page read as made by
someone: a count in parentheses, a shortcut hint such as "Press / to chat", a "copy logo as SVG" link.
Use them only when they fit the client.

## Failures seen in unguided pages, and the fix

| Seen | Fix |
|---|---|
| Hero text block left-aligned with an empty right half at 1280 and 1440px | Give the right columns the hero visual or its placeholder at the real ratio, or widen the headline to 8 to 10 columns at a larger size |
| Hero headline capped at 4rem, wrapping to four lines with an orphan word | Size the headline for its words; widen its columns; use `text-wrap: balance` |
| Alternating flat bands with the same padding and container | Two or three section padding tokens; one full-bleed section; one section off the main column |
| Form column runs far taller than the heading column beside it | Fill the short column with useful content or make it sticky |
| Pricing card in the left half of a wide row | Pair it with what the price includes, or centre it as a deliberate single object |
| Centred closing CTA on a left-aligned page, crammed under pricing | Same axis as the page, and section padding that separates it |
| The only proof (a figure or a milestone) set small under the CTA | Give proof the size it deserves: a figure at heading size with its label |
| All sections one width, one ground, one heading size | The section map: vary width, density and ground |
| On phones, a fixed panel covers most of a hero visual and hides its labels | Put the panel below the scene on small screens; never hide content to make room |
