---
name: landing-review
description: Use when reviewing, auditing, or critiquing a landing page, before declaring landing page work finished, or when asked whether a page looks AI-generated, templated, or unprofessional.
---

# Landing review

You audit a landing page and write a verdict to `landing/review.md`. The page
may come from the `landing-skills` pipeline or from anywhere else. You read
the source, look at the rendered page at four widths, test the motion and 3D
when there is any, and judge everything against one rubric.

Pages that look finished are often broken in ways nobody looked for: a form
that discards every lead while thanking the visitor, specs and promises the
client never gave, a 3D page that goes blank when WebGL is unavailable,
section padding cancelled by another rule at every width. Reviewers who
capture four widths and look at two miss what shows at only one. Your job is
to look at everything and to report only what you confirmed.


`<skill-dir>` below means the directory that contains this `SKILL.md`; the
scripts are in its `scripts/` folder. When the skill was loaded from a
plugin, the path of this file was given to you when it was loaded; if not,
find it with `find ~ -path '*landing-review/scripts/detect-tells.mjs' 2>/dev/null`.
Run every command from the project being reviewed, with the scripts named
by their full path.

## Inputs

You need the page, and you use whatever context exists.

1. **The page.** A project directory, a single HTML file, or a deployed URL.
   Find the entry: `index.html`, `src/pages/index.astro`, `src/App.jsx`,
   `app/page.tsx`, or whatever the stack uses. Note the stack; it decides how
   you serve the page in the visual pass.
2. **`landing/brief.md`**: product, audience, conversion action, the facts and
   proof the client supplied, the assets supplied, language. This is your
   source of truth for what the page may claim.
3. **`landing/copy.md`**: the approved copy. Compare the page against it.
4. **`landing/direction.md`** and **`landing/tokens.css`**: the visual
   direction with its reasons, and the tokens. A choice that looks like a
   default is acceptable when the direction gives a reason specific to the
   client.

When files are missing:

- **No brief.** Ask the user four things before judging claims: what the
  product is, who it is for, what the one conversion action is, and which
  facts, numbers, testimonials and logos are real. If you cannot ask, take
  the facts from what the page itself says about the business, and treat
  every testimonial, customer name, logo, rating and metric as unverified:
  report each one as a blocker with the fix "confirm this is real and
  supplied by the client, or remove it". Unverified proof that ships is the
  same risk as invented proof.
- **No copy.md or direction.md.** Review the copy and the visuals on their
  own merits with the rubric. You lose the ability to say "this differs from
  the approved copy", nothing else. Do not ask for these files.
- **No `landing/` folder.** Create `landing/` and `landing/shots/` in the
  project; the report and the screenshots go there.
- **The page does not build or will not serve.** Report it under rubric
  row B15, with the error. If no rendered page can be
  obtained any other way (a deployed URL, an earlier build), the visual pass
  cannot run and the verdict is `incomplete` (see Verdict).

Read `references/rubric.md` before you start the passes. It holds every
check with what a pass looks like and the severity when it fails. Read
`references/report-format.md` before you write `landing/review.md`.
`references/snippets.md` holds the scripts the passes run.

## Process

Run the passes in this order, then write the report:

1. Static pass: scan the source, then read it.
2. Visual pass: render the page and look at it at four widths.
3. Motion and 3D pass: only when the page has motion or a 3D scene.
4. Walk the whole rubric in `references/rubric.md`, area by area: copy,
   art direction, build, motion, launch readiness. Write a working
   checklist to `landing/review-checklist.md` with one line for every row
   id: `fail` with the location, `pass` with evidence, or `n/a` with the
   reason. A pass counts only when its evidence is the output of that row's
   own "How to check": the list where it says list, the count where it says
   count, and every section, header and footer included, where it covers
   every section. A row ticked from memory of the page is not walked. Read
   copy and art direction as
   carefully on a short static page as on a complex one, and walk every row
   even when one blocker is already obvious: the review lists everything
   the next round must fix, not only what decides the verdict.
5. Write `landing/review.md` as `references/report-format.md` describes:
   failures in the table, passes with their evidence under "Checked and
   fine". Then run the self-check at the end of this file.

Keep notes as you go: for every finding, where you saw it (file and line, or
width and section) and how you confirmed it.

## Static pass

Point the scanner at the page's source paths, not at a whole repository:

```bash
node <skill-dir>/scripts/detect-tells.mjs index.html styles.css
node <skill-dir>/scripts/detect-tells.mjs src --json
```

What it reads: any file you name directly, whatever its extension. In a
directory, it reads `.html`, `.htm`, `.css`, `.scss`, `.js`, `.jsx`, `.mjs`,
`.ts`, `.tsx`, `.astro`, `.vue`, `.svelte`, `.md` and `.mdx` files, skips
the folders `node_modules`, `.git`, `dist`, `build`, `.next`, `.astro`,
`.output`, `.vercel` and `.wrangler`, and skips any file over 1 MB. Each line
is checked against 25 rules. A finding prints as
`file:line  [rule-id] message` followed by a `fix:` line. Exit code 1 means
it found something, 0 means it found nothing, 2 means a bad path or usage.
`--self-test` checks the scanner itself.

Because it reads Markdown, it also reports phrases quoted in notes, reports,
the brief and earlier reviews. Ignore every finding in a file that is not
page source: `landing/*.md`, READMEs, notes, previous `review.md` files.

Treat every finding as a prompt to look, never as a verdict. Open the line,
see what it actually contains and what the page does with it, and only then
decide. Describe what the rule matched: a palette hit on a tag colour is
not a finding about the page background. Examples of what the look decides:

- `dead-anchor` on a logo link is a minor build fault. On the main
  call-to-action it means the conversion does not work: a blocker.
- `overused-font` means a common face leads the stack. It is a finding only
  when `direction.md` gives no reason and the face is not paired with a
  distinct display or mono face.
- `palette-cream-terracotta` fires on two exact hex values. Cream alone is
  never a tell; the bundle of cream, terracotta accent and a fashionable
  serif is.

A clean scan proves nothing. Invented claims, a page that is blank without
JavaScript, an oversized bundle, and a call to action that links to a valid
in-page id but never leads to a purchase all leave no pattern in source.
Most failures are absences, counts, contradictions, composition and broken
behaviour, which a line-by-line regex cannot see. A page can scan clean and
fail on every other pass.

So after the scan, read the source yourself. The checks to make by reading
are in `references/rubric.md`, marked "source". The ones that catch the most:

- The `<head>`: title, `lang`, meta description, favicon, Open Graph tags.
- Every form: where it submits, what the handler does with the data, and
  whether the success message waits for the server.
- Every call-to-action `href`: where it actually leads.
- Fonts: is each `font-family` loaded by a `@font-face`, a stylesheet link or
  a framework font import, or is it a system stack only?
- Every claim, number, name, spec and promise on the page, checked against
  the brief. Every asset the brief says was supplied, checked against what
  the page shows.
- CSS that cancels itself, such as a container class whose `padding`
  shorthand is more specific than the section rule and silently resets the
  section's vertical padding to zero. Confirm in the browser by reading the
  computed padding.
- `prefers-reduced-motion`: present or absent, and what it does.
- The body of the built HTML: if it is an empty `<div id="root">` with no
  `<noscript>`, the page is blank without JavaScript.

## Visual pass

You need a rendered page. The scanner and the source cannot show you
overlaps, empty halves of a layout, wrapped headlines, collapsed spacing or
text sitting on top of a 3D scene.

### Serve the page

Always review over HTTP, plain HTML included. Several checks need a server:
root-relative asset paths (`/styles.css`, `/assets/...`) fail over
`file://`, and rubric row L7 requests a missing path, `/robots.txt` and
`/sitemap.xml`. Install dependencies first if `node_modules` is missing.

- **Deployed URL:** use it directly.
- **Plain HTML and CSS:** in the folder that holds `index.html`, run
  `python3 -m http.server 8000` and open `http://localhost:8000/`.
- **Vite:** `npm run build`, then `npx vite preview` (port 4173 by default).
- **Astro:** `npm run build`, then `npx astro preview` (port 4321 by default).
- **Next.js:** `npm run build`, then `npm run start` (port 3000 by default).
- **Any other build:** build it, then run the same `python3 -m http.server`
  command in the output folder.

Write the URL you reviewed under Passes. Open a `file://` path only as a
last resort, when nothing can be served; then L7 is not run, and any asset
with a root-relative path may be missing for that reason alone. List both
under Passes, and do not report a missing asset that the server would
have found.

Review an existing build output only when it is current with the source.
`find src index.html -newer dist/index.html` (with the stack's source and
output paths) lists source files changed since the build; if it lists any,
or there is no build, rebuild. State under Passes which you reviewed and
why.

### Choose the capture method, in this order

1. **A browser tool**, if the environment has one. You need these
   capabilities: navigate to a URL, set the viewport width, take a
   full-page screenshot, run a script in the page, and emulate media
   features (colour scheme, reduced motion) and JavaScript off.
2. **The fallback script**, when there is no browser tool or it cannot
   capture. Run it even if you were told no browser is available: it starts
   its own browser through `npx` and may work anyway.

   ```bash
   node <skill-dir>/scripts/screenshot.mjs <url-or-file> landing/shots
   node <skill-dir>/scripts/screenshot.mjs <url-or-file> landing/shots --dark
   ```

   It writes `360.png`, `768.png`, `1280.png` and `1440.png` (with
   `--dark`, `360-dark.png` and so on), full page, after a 1.5 second wait,
   and prints each file path. If it fails with "Executable doesn't exist",
   run `npx playwright install chromium` and retry. It has failed when it
   exits with a non-zero code, prints `capture failed at <width>px`, or
   writes no image.
3. **Neither works.** Only then has the visual pass not run. Do the static
   pass and the source reading anyway, then follow the Verdict rule for
   `incomplete`.

The fallback cannot run scripts in the page, emulate reduced motion, turn
off JavaScript or WebGL, or scroll. With it, judge what you can from the
images, check reduced motion, JavaScript-off and WebGL-off by reading the
source, and list under Passes each check you could not run live, with the
reason.

### Capture all four widths

For each width, 360, 768, 1280 and 1440 (height 800 is fine):

1. Set the width, load the page, wait for fonts and images.
2. Confirm the real width with the viewport check in
   `references/snippets.md`. Some tools will not shrink a window below a
   minimum and silently give you a wider page; use the tool's device or
   viewport emulation instead, and check again.
3. Take a full-page screenshot into `landing/shots/`.
4. Run the measurement script from `references/snippets.md` and read its
   caveats: overflow, elements wider than the viewport, layout shift,
   canvas count.
5. Scroll through the page in the live browser, top to bottom.

Then open and read every image: all four widths, and the dark ones if you
took them. Write one line per width in your notes saying what you saw
before you move on. Looking at two widths and assuming the others is how
failures that show at only some widths get missed: a narrow block leaving
half the row empty only at the wide widths, or section links hidden with no
menu only at the narrowest.

If a full-page capture fails or comes back cut off, which happens on tall
pages with canvases, capture the viewport instead at the top and at several
scroll positions (a quarter, half, three quarters, the bottom) at each
width, and say so under Passes. The fallback script only takes full-page
captures; if it fails that way, record which widths are missing.

Full-page captures of scroll-driven pages can show blank bands where
parallax or sticky sections sit off-screen at scroll position zero. Before
reporting a blank area, scroll to it in the live page. If it renders there,
it is a capture artefact, not a defect.

### Look for

At every width, with the rubric open:

- Horizontal scroll, clipped text, overlapping elements, text on top of an
  image or canvas where it becomes hard to read.
- Spacing: sections with no vertical padding, headings flush against the
  block above, borders that stick out past the content.
- Composition: half-empty rows at 1280 and 1440, headlines that wrap to four
  lines or leave a single word on the last line, eyebrows, labels and
  buttons that wrap at 360, cards of uneven height.
- Navigation at 360: are the section links still reachable, are buttons at
  least 44 by 44 px, does a fixed header cover the headline?
- Order at 360: is the product visible above the form, or pushed below it?
- Images: real assets, or labelled placeholders at the right aspect ratio
  for assets that do not exist yet.
- Keyboard: Tab through the page once at 1280. Focus must be visible on
  every link, button and field, in a sensible order, and any interactive
  canvas needs a keyboard path. Count the tab stops as you go; any number
  in the report comes from this tab-through, never from an estimate.

### Emulate

- **Dark colour scheme**, when the CSS has a `prefers-color-scheme: dark`
  rule or a theme toggle: capture all four widths again. Dark mode must be
  designed, not inverted: check contrast and that images and borders still
  work. Skip this when the page has no dark mode, and say so.
- **Reduced motion**, when the page animates: covered in the next pass.
- **JavaScript off**: reload. The headline, the offer, the price and a way to
  convert must still be there. If your tool cannot turn JavaScript off, read
  the built HTML instead: what is in `<body>` before any script runs is what
  a visitor without JavaScript gets.

## Motion and 3D pass

Run this pass when the page has animation beyond simple hover colour
changes, scroll-linked effects, smooth scrolling, video, or a canvas or WebGL
scene. When it has none of these, skip it and write in the report that the
page has no motion or 3D to test. A static page is a legitimate choice; only
missing feedback on state changes is a finding then (see the rubric).

Check each of these and record the result. The scripts are in
`references/snippets.md`.

First run the "WebGL available in the tool" check. Headless browsers often
have no GPU, and some have no WebGL, so a 3D page renders blank there for
reasons unrelated to the page. If the tool has no WebGL, relaunch it with
software rendering if it offers that, or record under Passes that the
scene could not be seen in this environment, and do not report the blank
canvas as a page fault. This is separate from the deliberate WebGL-off test
in step 3.

1. **Frame rate during scroll.** Use the browser tool's performance trace
   while scrolling if it has one, otherwise the frame-rate script. Under
   software rendering the numbers are meaningless: do not report them, and
   judge scrolling from the source alone. Read the
   scroll handlers too: layout reads such as `getBoundingClientRect` on every
   scroll event, state updates that re-render a whole framework tree on
   every scroll event or frame, and animating `top`, `left`, `width` or
   `height` all cause jank on phones.
2. **Reduced motion.** Emulate `prefers-reduced-motion: reduce` and reload.
   Parallax, scrubbed scroll animation, autoplay loops, smooth scrolling and
   large movement should be gone; short fades and feedback may stay; no
   content may be left invisible. Then switch the emulation off and on
   without reloading: a page that reads the preference once at load ignores
   a visitor who changes it.
3. **WebGL disabled.** Inject the WebGL-off script before the page's own
   scripts and reload. The page must still show the headline, the offer and
   the call to action, with a poster image where the scene was. A common
   failure: the scene throws, nothing catches the error, and a single-page
   app unmounts entirely, taking the call to action with it. If your tool
   cannot inject scripts, read the source for a WebGL check, an error
   boundary around the canvas, and a fallback.
4. **JavaScript disabled.** As in the visual pass, with attention to the
   hero: the headline and call to action must not depend on the scene's
   code.
5. **Hero text before the scene.** Block the scene's script or model
   requests with the tool's request blocking, or throttle the network, and
   reload. The headline, the call to action and a poster or reserved space
   must appear before the scene does. Without blocking, read the source: is
   the hero text in the HTML, and is the 3D code loaded after first paint?
6. **Bundle size.** After the build, run the bundle-size loop. Report the
   largest chunk raw and gzipped, and whether the 3D code is split from the
   code that renders the hero. A raised `chunkSizeWarningLimit` or similar
   setting that silences the build's size warning is a finding by itself.
   Count the models, textures and environment maps, and note any fetched
   from a third-party host at runtime.

The remaining motion checks (one role per animation, no identical reveal on
every section, paused off-screen work, number of WebGL contexts, text that
moves while being read) are in the rubric's motion section.

## Verdict

Decide the verdict with these rules, in this order:

1. **`incomplete`** only when no rendered page was seen at all: there was no
   usable browser tool and the fallback script also failed or could not run.
   A page nobody looked at is not reviewed, whatever the source suggests.
   Write the report anyway, with every finding from the static pass and the
   source reading, and say under Passes which passes were skipped and why.
   Do not call the page reviewed, passed or ready. If you found blockers,
   list them and say the page would fail on them once reviewed.
2. **`fail`** when there is at least one blocker.
3. **`pass`** otherwise. Major and minor findings can remain on a passing
   page; they are listed for the next round.

When the page was rendered but some checks could not run (the fallback
cannot emulate reduced motion, a tool cannot turn off WebGL), the verdict
still comes from the findings. List each check that could not run under
Passes, with the reason, and what you checked in the source instead.

What counts as a blocker, the default severity of every check, when a
severity may be raised, and which findings form a cluster row are all in
the "How to use this rubric" section of `references/rubric.md`. Apply them
as written there.

When no rendered page was seen but the source alone proves a fault that a
rubric row says to confirm in the browser, report it anyway, marked as
`references/report-format.md` describes.

## Output

`landing/review.md`, laid out exactly as `references/report-format.md`
describes: the verdict line, the findings table, Passes, Open items, and
Checked and fine.

## Self-check

Before you hand over the report:

- The first line is one of the three verdict lines, exactly.
- You confirmed the real viewport width, and opened and read every
  screenshot: four widths, plus dark ones if taken, with one note per width.
- Every scanner finding in the report was confirmed by looking at the page
  or the line, and none comes from a file that is not page source.
- Every claim, number, name and promise on the page was checked against the
  brief, or treated as unverified when there is no brief.
- Every form and every call to action was followed to where it really goes.
- You served the page over HTTP, or Passes says why not and which checks
  that cost.
- `landing/review-checklist.md` has a result for every rubric row id. Each
  pass carries the output of that row's own "How to check" (the list, the
  count, every section including header and footer), and each `n/a` its
  reason.
- Every severity and every cluster row follows the rubric's rules, and the
  layout follows `references/report-format.md`.
- The motion pass ran, or the report says the page has no motion or 3D.
- Every check that could not run is listed under Passes with its reason.
  The verdict is `incomplete` only if no rendered page was seen at all.
- No finding asks for proof to be invented; fixes for missing proof say
  "supply a real one or remove the section".
