# Report format: landing/review.md

## Contents

- Structure
- Writing the rows
- The incomplete verdict
- Filled example

## Structure

`landing/review.md` has this shape, in this order:

1. Line 1, exactly one of: `Verdict: pass`, `Verdict: fail`,
   `Verdict: incomplete`. Nothing else on the line, no heading marker.
2. A blank line, then one Markdown table with the header
   `| Severity | Area | Location | Finding | Fix |`. Rows sorted blockers
   first, then major, then minor; inside a severity, in page order.
3. `## Passes`: one entry per pass (static, visual, motion and 3D) saying
   what ran, with which tool, and each check that could not run, with the
   reason and what you checked in the source instead. For the visual pass,
   one line per width saying what you saw, and the URL you reviewed.
4. `## Open items`: assets and facts the client must supply before launch,
   each with what is needed. Write "None" when there are none.
5. `## Checked and fine`: the rubric rows that passed, each with its id and
   the evidence in a few words ("C5: swap test fails with a competitor's
   name; audience named in the subhead"). A pass without evidence is not a
   pass. The full row-by-row record stays in `landing/review-checklist.md`.

Column values:

- **Severity**: `blocker`, `major` or `minor`.
- **Area**: `copy`, `art-direction`, `build`, `motion` or `launch`.
- **Location**: a file and line (`index.html:95`, `src/App.jsx:142`), or a
  width and place on the page (`1280, pricing`), or both. `all widths` when
  it shows everywhere.
- **Finding**: what is wrong, specifically enough to find it again, and how
  you confirmed it when that is not obvious. Quote the copy when the copy is
  the problem.
- **Fix**: one action. Name the owning skill when one exists
  (`landing-skills:landing-copy`, `landing-skills:landing-art-direction`,
  `landing-skills:landing-build`, `landing-skills:landing-motion`,
  `landing-skills:landing-launch`).

A pipe character inside a cell breaks the table; write it as `\|` or reword.

## Writing the rows

- Only confirmed failures go in the table. A rubric check that passes is
  not a row; at most it gets a line under "Checked and fine". Do not add
  rows to fill the table.
- Severities, including when one may be raised and what the Finding must
  then say, follow the rubric's "How to use" section.
- A fault proven from source when no rendered page was seen ends with
  "source only, not visually confirmed".
- One problem per row. Three dead links in three places are three rows,
  unless they share one cause and one fix.
- Do not add a row that only repeats a blocker's consequence under another
  rubric id (a missing success state on a form that has no handler at all).
  Put it in the blocker's Fix cell instead, unless its fix is different.
- Counts in a finding (tab stops, sections, sizes) come from what you
  measured on the page, never from an estimate.
- Describe what a scanner rule actually matched on that line: a palette hit
  on a tag colour is about the tag, not the page background.
- Say what you saw, not what you suspect. "The booking button links to
  `/book`, which returns 404 in the build; clicking it at 1280 shows the
  server's not-found page" beats "the booking flow may not work".
- A scanner finding goes in only after you looked at the line and the
  rendered page. Do not paste the scanner's message as the finding.
- Fixes for missing proof say "supply a real one or remove it", never
  "add a testimonial".
- A cluster row (the rubric says when one is due) goes last among the
  `major` rows and names the decision and the locations of the rows it
  groups. Some of those rows may be `minor` and sit below it.

## The incomplete verdict

When the verdict is `incomplete` (the rule is in `SKILL.md`, Verdict), the
report starts like this, and `## Passes` says exactly what was skipped and
why:

```markdown
Verdict: incomplete

| Severity | Area | Location | Finding | Fix |
|---|---|---|---|---|
| blocker | launch | index.html:31 | "Reserve a table" links to `href="#"`; there is no booking route or form anywhere in the source. Found by source reading; not seen rendered. | Link it to the real booking system, or build a form with a handler (landing-skills:landing-launch). |

## Passes

- Static: scanner ran (2 findings, both confirmed in source). Source read in full.
- Visual: skipped. No browser tool in this environment. The fallback
  script exited with code 1 and printed `capture failed at 360px` for each
  width; `npx playwright install chromium` failed without network access.
  The page has not been seen at any width, so it is not reviewed. If the
  blocker above stands, the page will fail once reviewed.
- Motion and 3D: skipped for the same reason. The source has one CSS
  transition on button hover and no scripts.
```

## Filled example

A fictional page: Spoke Shed, a mobile bicycle repair van. The brief gives
the service area, three fixed-price services, booking as the conversion
action, opening hours, and no testimonials or customer numbers. The client
has a photo of the van that was not supplied yet.

```markdown
Verdict: fail

| Severity | Area | Location | Finding | Fix |
|---|---|---|---|---|
| blocker | copy | index.html:61-74 | Testimonials section with three quotes signed by first names, plus a rating line. The brief supplies no reviews and no rating. | Remove the section, or supply real reviews with permission and their source (landing-skills:landing-copy). |
| blocker | launch | index.html:30, 77 | Both "Book a repair" buttons link to `/book`, which does not exist in the build; clicking at 1280 shows the server's 404. No booking can be made. | Link to the real booking page, or add a booking form with a handler (landing-skills:landing-launch). |
| major | copy | index.html:22 | H1 "Repairs, made simple." Fails the swap test: it fits any repair business and does not say the van comes to you, or where. | Name the service and the area, in the brief's words (landing-skills:landing-copy). |
| major | copy | index.html:48 | "Same-day repairs guaranteed" is not in the brief, which says bookings go to the next free slot. | Remove the guarantee, or confirm it with the client. |
| major | copy | index.html:30, 77, 103 | Three labels for one action: "Book a repair", "Get started", "Send". None says what happens next. | Use one label and add a line on when the client confirms the time. |
| major | art-direction | all widths | Accent `#6366F1` on every button and link, Inter as the only face, no `direction.md`. Nothing ties the look to bikes, the van or the area. | Run landing-skills:landing-art-direction and record a reason for each choice. |
| major | build | 360, header | The sticky header is 96 px tall at 360 and covers the first three lines of each section reached from an in-page link. | Shrink the header on narrow screens and set `scroll-margin-top` on sections (landing-skills:landing-build). |
| major | build | all widths, hero | The hero image has no `width` and `height`; the measurement script reports a layout shift of 0.21 on load as the text drops below it. | Set the image's dimensions or an `aspect-ratio` on its frame. |
| major | launch | index.html:3-20 | No analytics and no event on the booking click, so bookings cannot be counted. | Add analytics and a conversion event (landing-skills:landing-launch). |
| major | motion | src/main.js:4-20 | With reduced motion emulated, the scroll fades still move 40 px and the preference is never read; no reduced variant exists. | Give each animation a reduced variant: a short opacity fade, no travel (landing-skills:landing-motion). |
| major | build | 1280, footer | Footer text at 13 px in `#8a8a8a` on `#ffffff` measures about 3.4:1, below 4.5:1, and it carries the opening hours. | Darken the text and set the hours at body size. |
| major | art-direction | page | Cluster: the template accent and single default face (all widths), the three equal cards (1280 and 1440, services) and the uniform card treatment (all widths, cards) come from one missing step: no direction was chosen for this business. | Write the direction before fixing individual rows. |
| minor | art-direction | 1280 and 1440, services | Three equal cards with an icon, a title and two lines. The services differ in price and duration but look identical. | Lay the services out as a price list with durations. |
| minor | art-direction | all widths, cards | Every card, image frame and the form panel share one 12 px radius, 1 px border and the same shadow: one treatment on nine surfaces, so nothing reads as more important. | Keep the shadow for the booking panel only and give the service list a flat treatment. |
| minor | motion | src/main.js:4-20 | Every section fades up with the same 0.8 s easing; the motion has no role beyond decoration. | Keep one moment that explains something and drop the rest. |
| minor | build | index.html:3-9 | No favicon, no `og:image`, no canonical. | Add them to the head. |

## Passes

- Static: scanner ran on `index.html`, `styles.css` and `src/`, 3
  findings. Kept 2 after looking (`indigo-default-accent` and
  `overused-font`, both folded into the palette and type row, since there
  is no `direction.md`); dropped 1 (`default-shadow` on the input focus
  ring, a deliberate focus style). Source read in full: head, links, fonts,
  scripts, claims against the brief.
- Visual: browser tool, `http://localhost:8000/` served with
  `python3 -m http.server 8000` from the project folder. Viewport
  width confirmed with `window.innerWidth` at each width.
  - 360: no overflow; sticky header covers section starts; van placeholder
    sits above the services, labelled.
  - 768: no overflow; card titles wrap to two lines.
  - 1280: layout shift on load visible as the hero text jumps; footer
    contrast low.
  - 1440: same as 1280; services row leaves 300 px empty at the right.
  - Dark mode: none in the CSS; skipped.
  - JavaScript off: all content present; the sections stay visible.
- Motion and 3D: scroll test 60 fps, no frames over 33 ms. Reduced motion
  emulated: fades still run. No WebGL, video or scroll-linked effects, so
  the WebGL and bundle checks do not apply.

## Open items

- Photo of the van, 3:2, for the hero. Placeholder is labelled and sized
  correctly (index.html:35).
- Real reviews with permission, if the client has any.

## Checked and fine

- C7: the three prices match the brief and sit beside the booking button;
  nothing to calculate.
- C13: hours, service area and phone number in the footer and beside the
  form, in the brief's words.
- B7: Tabbed through all 14 interactive elements at 1280; focus visible on
  each, order follows the page; one H1.
- B12, C14: scanner and source show no builder marks or demo text.
- L7: `/a-path-that-does-not-exist` returns the site's own 404 page;
  `/robots.txt` and `/sitemap.xml` return 200.
```
