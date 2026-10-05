---
name: landing-build
description: Use when implementing a landing page in code, including HTML structure, CSS, responsive layout, fonts, images, meta tags, social cards, structured data, lead-capture forms, accessibility, or loading performance, in plain HTML, Astro, Next, Vite with React, or Tailwind.
---

# Landing build

Turn the approved copy, the visual direction and the design tokens into a
static page that is complete with JavaScript switched off. Complete means a
visitor can read every section, reach every section from the header, and send
the form. Motion and 3D come later, from `landing-skills:landing-motion`, and
sit on top of a page that already works.

Two bugs showed up in more than one test build made without this skill, and this skill is shaped
to make both impossible:

1. A container class with a `padding` shorthand overrode the section's
   vertical padding, so every section sat flush against the next. The fix is
   structural: the section owns the block axis, the container owns the inline
   axis, and neither uses the shorthand (step 5).
2. A dead form: `action="#"`, `onsubmit="return false"`, or a script that
   showed "Thanks" and threw the data away. The fix: a real `action`, a real
   `method`, a server-backed success page, and no success message the page
   cannot back (step 8).

## Inputs

Read these from the user's project before writing code.

| File | What you take from it |
|---|---|
| `landing/brief.md` | the fields `landing-skills:landing-page` writes. You use: Language (for `lang`); Conversion action (either a form with its fields and its endpoint or `none yet`, or an outside URL); Stack (as named, or `detect`); Domain (the production domain, or `unknown`); Contact routes (for the footer and `/form-error/`); Assets available |
| `landing/copy.md` | one H2 per page section in page order; the last H2, `Placeholders`, lists every gap |
| `landing/direction.md` | typefaces with source and licence, the layout row for each section, imagery with aspect ratios, surfaces, and the final H2 `Motion and 3D intent` |
| `landing/tokens.css` | the custom properties: `--color-*`, `--font-*`, `--text-*`, `--space-*`, `--radius-*` |

When an input is missing:

- No `copy.md`: do not write marketing copy yourself. Ask the user for the
  text or suggest `landing-skills:landing-copy`. If the user hands you text,
  use it as written. Never invent proof: no testimonials, logos, counts or
  ratings. A missing piece becomes `[PLACEHOLDER: what is needed]`.
- No `direction.md` or `tokens.css`: suggest
  `landing-skills:landing-art-direction` first. If the user wants to go on
  without it, write `landing/tokens.css` yourself with the five prefixes,
  derive the values from the brand constraints the user gives, and tell the
  user these tokens are provisional.
- No `brief.md`: ask for the language, the conversion action (a form and
  where it should post, or an outside URL), the production domain and a
  contact route.
- A brief field that says `unknown` or `none yet` is a gap, not an error:
  build with the rule this skill gives for it (no URL tags without a
  domain, the `/api/lead` placeholder without an endpoint, a placeholder
  contact line without contact routes) and list it in the hand-off. Detect the stack from the project files
  (see "Stack detection") rather than asking when you can.

## Process

### 1. Detect the stack

Use the table in "Stack detection". Read the matching section of
`references/adapters.md` before writing files; it covers only what differs
from the plain recipes in the other references.

### 2. Make an inventory

Before any markup, write down:

- Each H2 of `copy.md` and the `<section>` it becomes, with its `id`.
- Every token name in `tokens.css`. You will use these names and no others
  for colour, type, spacing and radius.
- Every image the direction asks for, its aspect ratio, and whether the file
  exists.
- The conversion action from the brief: an outside URL (a link, no form),
  or a form with its fields and endpoint. With the endpoint `none yet`, the
  form posts to `/api/lead` and that gap goes in the hand-off (step 11).
- The Domain (known or `unknown`) and the Contact routes, which the head,
  the footer and `/form-error/` need.

### 3. Write the head

Title, description, canonical, Open Graph and social card, favicon set,
`theme-color`, `lang`, structured data. Templates and rules are in
`references/seo-meta.md`. Write the title and description from `copy.md`; a
default title such as the framework's starter title is a tell.

### 4. Load the fonts

Self-host the WOFF2 files named in the direction, declare `@font-face` with
family names that match the stacks in `tokens.css` exactly, add a
metric-matched fallback, and preload only the one or two files used above
the fold. `references/performance.md` has the recipe and a tested script that
computes the fallback values. A page that names a font and never loads it, or
falls through to `system-ui`, renders differently on every machine.

### 5. Build the layout skeleton

Every band of the page is a `<section class="section">` with one
`<div class="container">` inside it. The two never share a job:

```css
/* The section owns vertical rhythm, the container owns width and gutters. */
.section { padding-block: var(--space-section-open); }
.container {
  inline-size: min(100% - 2 * var(--space-gutter), var(--space-content-max));
  margin-inline: auto;
}
```

The token names above are examples; use the names in your `tokens.css`.
`references/css.md` lists every token role the recipes assume and what to
do when your file has no token for one.
The container sets no padding at all, so no rule order or specificity can
erase the section's spacing. Never write the `padding` or `margin`
shorthand on a section or container class: a shorthand sets both axes and
silently replaces whichever one the other class set. This is the rule that
removes bug 1. `references/css.md` shows the broken version beside this one,
plus the full-bleed and hairline cases.

### 6. Build the sections

- Use semantic elements: `header`, `nav`, `main`, `section`, `footer`, `ol`
  for steps, `dl` or `table` for specifications, `figure` for media with a
  caption.
- One `h1`, in the hero. Each section opens with an `h2` taken from
  `copy.md`. Do not skip levels.
- Lay each section out as its row in `direction.md` says. When the direction
  leaves the shape open, pick from the recipes in `references/css.md`; the
  number and shape of blocks follow the content, so three equal cards appear
  only when the content is three equal things.
- Use the tokens for every colour, font, size, space and radius. Never
  redeclare a token in page CSS. If a value you need has no token, compose
  it from tokens (`calc()`, `color-mix()`) and list it in the hand-off so the
  direction can add it.
- Use fluid sizes with `clamp()`, logical properties, `oklch()` colour with
  the fallbacks in `references/css.md`, and container queries for components
  that change shape with their own width.

### 7. Place the media

Real images get `width` and `height`, `alt` text that says what is in them,
modern formats with a fallback, and `fetchpriority="high"` on the hero image
only (`references/performance.md`). An asset that does not exist yet becomes
a visible, labelled placeholder box at the final aspect ratio, with text such
as `[PLACEHOLDER: photo, the workshop bench from above, 3:2]`. Do not give
the box `role="img"`: it would announce a photograph that does not exist.
Give each placeholder the ratio, width and ground the direction gives that
image, so the frames show the planned layout rather than a row of identical
dashed boxes (`references/css.md`, Media placeholders). Never draw a fake
product screen and present it as the product.

### 8. Build the conversion action

The brief's Conversion action decides what you build:

- **An outside URL** (checkout, booking page, app store): the action is a
  link, `<a class="button" href="https://...">` with the label from
  `copy.md`. There is no form, no `form.js`, and no `/thanks/` or
  `/form-error/` page; skip the rest of this step. If the brief says the
  click should be counted, `landing-skills:landing-launch` adds the beacon.
- **A form with an endpoint the user already has**: the form posts to that
  URL and the `/api/lead` placeholder is not used. Build the result pages
  only if that endpoint redirects to them; otherwise say in the hand-off
  where the endpoint sends the visitor.
- **A form with the endpoint `none yet`**: the form posts to the
  placeholder `/api/lead`, which `landing-skills:landing-launch` builds as a
  Worker.

For a form, read `references/forms-a11y.md`. The non-negotiable parts:

- `<form action="..." method="post">` with the endpoint from the brief, or
  `action="/api/lead"` with an HTML comment beside it saying the endpoint
  does not exist yet, listed in the hand-off.
- Every field has a `name`, a visible `<label>`, the right `type` and
  `autocomplete`, and a hint linked with `aria-describedby` when the format
  needs explaining.
- Without JavaScript the browser posts the form and the endpoint answers
  with a `303` redirect to one of two static pages you build now:
  `/thanks/` and `/form-error/`. Once `landing-skills:landing-launch` adds
  Turnstile, a visitor without JavaScript has no token and always lands on
  `/form-error/`, so that page must offer another way to get in touch,
  taken from the brief's Contact routes.
- Always ship `form.js` from `references/forms-a11y.md` as progressive
  enhancement. The form works without it; the file is still part of the
  build, because `landing-skills:landing-motion` extends the states it sets
  and `landing-skills:landing-launch` checks them. It shows success only
  when the response is a 2xx and its JSON body says `ok: true`. A 2xx
  alone proves only that something answered: `fetch` follows a redirect and
  reports the page it lands on as 200, and a static host can answer a POST
  with an ordinary HTML page. Both happened in testing, and both are
  failures. On failure the script says so and keeps what the visitor typed.
  Never reset the form or show "Thanks" before the server confirmed.

### 9. Meet the accessibility floor

Skip link, landmarks, a `nav` whose links point at real section ids, the
logo linking to `/`, a visible focus style on every interactive element,
targets at least 44 by 44 px, and a contrast check of every text and
background pair the page actually renders: body text on each surface,
captions, hints, placeholder text, text on the accent. Details and numbers
are in `references/forms-a11y.md`. A navigation that hides its links on
phones with no replacement fails this floor; let the links wrap instead.

### 10. Leave motion out

Under reduced motion it is movement that gets reduced, never feedback, so
write no global rule that zeroes transitions. Build every element in its
final, visible state. No entrance animations, no
`opacity: 0` waiting for a script, no scroll libraries.

For every moment named in the direction's `Motion and 3D intent`, leave a
stand-in: a still poster image, or a labelled placeholder, at the moment's
final size and aspect ratio, so the layout does not shift when
`landing-skills:landing-motion` mounts the real thing. Mark it with
`data-moment="<name>"`, using the short name the direction gives that
moment; motion finds its target by that attribute. The stand-in is
complete without motion: a real image with `alt` text, or placeholder text
that says what the moment shows.

```html
<div class="hero__media" data-moment="exploded-view">
  <img src="/img/exploded-poster.jpg" width="1600" height="1000" alt="The device's layers spread apart: case, board, battery, lens">
</div>
```

When the intent is `none`, add no `data-moment` elements.

### 11. Check and hand off

Run "Definition of done". Then tell the user, in a short list:

- what was built and where (files, output folder), and the resolved stack
  written into the brief's Stack field if it said `detect`,
- every `[PLACEHOLDER: ...]` still on the page and every asset to supply,
- the conversion action as built: a link to an outside URL, a form posting
  to the user's endpoint, or a form posting to `/api/lead`, which does not
  exist yet,
- every field the form submits, one line each: its `name`, its type
  (`email`, `tel`, `text`, `textarea`), whether it is `required`, and its
  `maxlength` from the markup. `landing-skills:landing-launch` sets up its
  Worker's validation, length caps, storage and request-size limit from
  this list, and refuses a field it was not told about,
- values composed because no token existed, and every optional token
  `tokens.css` lacked with the fallback the recipes used for it,
- the production domain if the brief says `unknown`, and the URL tags left
  out because of it (canonical, `og:url`, `og:image`, sitemap),
- contact routes the brief left `unknown`, which the footer and
  `/form-error/` show as placeholders,
- every font file that was not subset, with its size in bytes,
- every Definition of done check that was not run, marked "not run" with
  the reason,
- each moment from `Motion and 3D intent`: its `data-moment` name and where
  its stand-in sits (section and element), or "none" when the intent is
  `none`,
- the next skill: `landing-skills:landing-motion` when the direction's
  `Motion and 3D intent` names a moment, then `landing-skills:landing-review`,
  then `landing-skills:landing-launch` for hosting and the form endpoint.

## Stack detection

Check the project root in this order and stop at the first match. The
`Stack` field in `brief.md` decides when the project folder is empty.

Once the stack is resolved, write it back: if the brief's Stack field says
`detect`, replace that one word with the resolved stack (for example
`Astro`, or `plain HTML and CSS`) and change nothing else in
`landing/brief.md`. Later skills read the field and should not meet an
unresolved word. A stack the user named stays as written. When there is no
`landing/brief.md`, do not create one.

| Signal in the project | Stack | Adapter section |
|---|---|---|
| `astro.config.mjs` (or `.ts`, `.js`), or `astro` in `package.json` dependencies | Astro | Astro |
| `next.config.mjs` (or `.js`, `.ts`), or `next` in dependencies | Next | Next |
| `vite.config.js` (or `.ts`) with `react` and `@vitejs/plugin-react` in dependencies | Vite with React | Vite with React |
| `vite.config.js` (or `.ts`) without React | Vite serving plain HTML | none: write the page as plain HTML and CSS in `index.html`; the multi-page `build.rollupOptions.input` note in the Vite with React section still applies |
| None of the above | plain HTML and CSS | none: the references are written for this case |

Tailwind is detected separately and combines with any row: `tailwindcss` in
dependencies and a CSS file containing `@import "tailwindcss"` means
Tailwind v4, covered in the Tailwind v4 section of `references/adapters.md`.
A `tailwind.config.js` with `@tailwind base;` in the CSS means Tailwind v3,
which this skill does not cover; tell the user and write plain CSS beside it
rather than guessing at v3 configuration.

For plain HTML, put the page in `public/` (`public/index.html`,
`public/styles.css`, `public/thanks/index.html`,
`public/form-error/index.html`) so the served folder holds
nothing but the site. Copy `landing/tokens.css` to `public/tokens.css`
byte for byte and link it before `styles.css`; re-copy it whenever the
tokens change. Do not serve the `landing/` folder: it holds the brief and
the direction, which are not for visitors.

## Definition of done

Each item is a check you can run, not an impression. Tick an item only
after running it. If you could not run a check (no browser, no way to turn
JavaScript off, no keyboard access), leave it unticked and name it in the
hand-off as "not run", with the reason. A skipped check reported as passed
is the same failure as a fake form success.

- [ ] Serve the output folder over HTTP and check that URL, not a
      `file://` path: root-relative links such as `/tokens.css` and
      `/form.js` do not load from `file://`, and screenshots taken that way
      show an unstyled page. Plain HTML: `npx serve public` (or
      `python3 -m http.server -d public`); Astro: `npx astro build` then
      `npx astro preview`; Next export: `npx next build` then
      `npx serve out`; Vite: `npx vite build` then `npx vite preview`.
- [ ] With JavaScript disabled in the browser, every section renders, every
      header link scrolls to its section, and submitting the form sends a
      POST to the form's `action`.
- [ ] No section lost its vertical padding. In the browser console:
      `[...document.querySelectorAll('.section')].filter(s => parseFloat(getComputedStyle(s).paddingBlockStart) === 0)`
      returns an empty array (adjust the selector to your section class).
- [ ] Page length at 360 px is recorded in the hand-off. In the console:
      `(() => { const h = document.documentElement.scrollHeight; const pad = [...document.querySelectorAll('.section')].reduce((t, s) => { const c = getComputedStyle(s); return t + parseFloat(c.paddingBlockStart) + parseFloat(c.paddingBlockEnd); }, 0); return { height: h, sectionPadding: Math.round(pad / h * 100) + '%' }; })()`
      When the page reads as long for its content, find where the height
      goes: section padding (switch narrow widths to
      `--space-section-dense`, as in `references/css.md`), placeholders
      taller than their images, media stacked where it could sit beside
      text. Never cut content to shorten the page. In a test page the
      narrow-width rule took section padding from 29% of the height to 20%.
- [ ] No horizontal overflow at 360, 768, 1280 and 1440 px wide:
      `document.documentElement.scrollWidth - innerWidth` is `0` at each width.
      No text overlaps another element in screenshots at those widths.
- [ ] Exactly one `h1`; a `title` naming the product; a meta description;
      `og:title`, `og:type`; `twitter:card`; a favicon link; `theme-color`.
      With the production domain known, also `link rel="canonical"`,
      `og:url`, `og:image` and the sitemap; with it unknown, none of those,
      no bracketed text in any URL attribute, and the gap in the hand-off.
- [ ] `theme-color` holds the hex of the token it copies (usually
      `--color-bg`). It is the only token colour typed as a literal; check
      it still matches `tokens.css`.
- [ ] No `href="#"`, no `action="#"` or empty `action`, no
      `onsubmit="return false"`. The logo links to `/`.
- [ ] For a form: every field has a `name` and a visible label, `form.js`
      is loaded, and `/thanks/` and `/form-error/` exist, both `noindex`.
      For an outside URL: the action is a link to that URL and no form
      or result page was built. A failed submission shows an
      error and keeps the input. The hand-off lists every field name.
- [ ] The page CSS uses the tokens and declares none of them. This finds
      redeclared tokens and should print nothing (for frameworks, point it at
      the source CSS and components instead of `public/`):
      `grep -rnE --include='*.css' -e '--(color|font|text|space|radius)-[A-Za-z0-9-]+[[:space:]]*:' public | grep -v 'public/tokens.css'`
      For plain HTML, also `cmp landing/tokens.css public/tokens.css`.
- [ ] Every font in the token stacks is loaded with `@font-face`, and
      `document.fonts.check('16px "Face Name"')` is `true` after load.
- [ ] Every image has `width` and `height` (or sits in a box with
      `aspect-ratio`), and only the hero image has `fetchpriority="high"`.
- [ ] Tab from the top of the page: the skip link comes first, every control
      shows a visible focus style, and the order follows the reading order.
- [ ] Contrast of every rendered text pair is at least 4.5:1 (3:1 for large
      text and for focus rings and input borders against their background).
- [ ] Read the page top to bottom at 360 px and at 1440 px looking for
      generated-page tells: a default or system-only font, gradient text, an
      equal three-card grid the content did not ask for, a pill badge over
      the headline, inflated phrases, invented proof, dead links. Fix each
      one. Optional extra: if `landing-skills:landing-review` is installed,
      run its tell detector on the output folder; it finds the same things
      by pattern and can miss or over-report.
- [ ] The hand-off list from step 11 is written, including every placeholder
      and the endpoint status.
