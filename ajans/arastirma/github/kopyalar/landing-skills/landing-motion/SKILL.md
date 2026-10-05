---
name: landing-motion
description: Use when adding animation, scroll effects, parallax, page transitions, hover or cursor interactions, 3D, WebGL, shaders, or particles to a landing page, or when choosing between GSAP, Motion, Lenis, Three.js, React Three Fiber, Rive, or Lottie.
---

# Landing motion

Add motion and 3D to a landing page that already works without them. Every
effect you add has a stated purpose, a reduced-motion variant, and a fallback
that leaves the page complete. The page before you started is the fallback:
if a script fails, a browser lacks a feature, or the visitor asks for less
motion, they get that page, not a blank one.

## Inputs

1. **`landing/direction.md`, section `Motion and 3D intent`.** This is the
   brief for your work. It says `none`, or it names each moment and what it is
   for. Implement what it names. Do not add moments it does not name.
2. **The static page** built by `landing-skills:landing-build`, or any
   existing page. It must render all copy and the conversion action with
   JavaScript disabled. Check that first: if it does not, fix that before
   adding motion, because every recipe here assumes the static page is the
   fallback.
3. **`landing/tokens.css`** if present. Use its colours in shaders and
   fallbacks. Put motion tokens (durations, easings) in your own stylesheet;
   leave `tokens.css` as the art direction wrote it.

When an input is missing:

- No `direction.md`: ask the user what should move and why. If you cannot ask,
  the default is `none` (see "Deciding what moves"); say so in your summary.
- No static page: stop and build one first (`landing-skills:landing-build`),
  or ask for it. Motion layered on a page that only exists in JavaScript is
  how a failed 3D scene takes the headline and the call to action down with it.
- The stack: read `package.json`. React (Vite, Next) gets the React variants
  where a recipe has one; everything else gets the plain HTML, CSS, and
  JavaScript variant. A client-only React app (everything rendered into an
  empty `#root`) fails the no-JavaScript check by construction: the copy must
  be in the HTML first, through server rendering, prerendering, or the
  island pattern in `references/webgl-3d.md`.

## Deciding what moves

Start from the intent in `direction.md`. Write down, for each moment it names:
the element, the role (entrance, feedback, scroll-linked, ambient, or
interactive 3D), and what the visitor learns from it. If you cannot finish
that sentence, cut the moment.

`none` is a valid answer, often the right one, and the default when the
intent is missing. Many pages are complete with no motion beyond feedback
states, especially when photographs or copy carry them; a page with almost
no motion loses nothing by it.

Under `none` this is a short check, not new work. `landing-skills:landing-build`
already writes hover, focus, press, and disabled states and a form script
with sending, success, and error states. Open the page in a browser and
confirm four things:

1. Tabbing shows a visible focus ring on every link, button, and field.
2. Hover and press states respond on buttons and links.
3. After a form submit, the status message appears and takes focus
   (`references/interaction.md`, "Form-submit feedback", adds this if it is
   missing).
4. Nothing moves on its own: no autoplay, no loop, no reveal.

Optionally add timing to the hover and press states
(`references/interaction.md`, "Hover and press states"). Then stop. If you
could not open a browser, say so and list each of the four as "not run"; do
not report the page as checked.

When the intent names moments:

- **One signature moment per page.** The hero sequence, the product demo, the
  3D object. Everything else is quieter than it.
- **Vary by role, never by section.** Feedback is fast (100 to 200 ms),
  entrances are slower, scroll-linked motion follows the scroll. Applying the
  same fade-up, easing, and stagger to every section is the most recognisable
  generated-page pattern, and it teaches the visitor nothing.
- **Most content does not move.** Text the visitor is reading never moves:
  no parallax on paragraphs, no reveal that hides a block until it is
  scrolled to.
- **Demonstrate, do not decorate.** Motion that shows how the product works
  (a sequence of states, an exploded view, a before and after) earns its cost.
  Motion that only says "this page is animated" does not.

Read `references/foundations.md` before writing any motion. It has the
durations, easings, and the list of patterns that read as generated, each
with what to do instead.

## Fitting recipes to the page

Recipes attach to the page that exists; they do not restyle it.

- The static page from `landing-skills:landing-build` leaves a stand-in for
  each moment the direction names: a poster image or labelled placeholder at
  its final size. Mark that element with `data-moment="<name>"` (the name
  from `Motion and 3D intent`) if the build has not, and attach the recipe
  to it: add the recipe's container class to that element, or select it by
  the attribute.
- Each recipe's Code section opens with a line naming the classes it adds,
  as new to this skill, and the build's classes it uses unchanged. Add the
  new classes; never redefine the build's own (`.hero`, `.hero__title`,
  `.button`, `.section`, `.split`, and the rest). Where a recipe needs a
  layout the build's element does not have, wrap it in a new element of
  your own.
- Use the build's tokens (`--color-*`, `--space-*`, `--radius-control`)
  for any colour or size a recipe needs, never a new literal value.

## Choosing a recipe

Pick the lowest rung that produces the effect: CSS, then a small script,
then GSAP or Motion, then WebGL. Each recipe has `Use when`, `Avoid when`,
`Code`, `Reduced motion`, and `Cost`; 3D recipes add `Fallback`.

| Need | Read |
|---|---|
| Durations, easings, springs, stagger, what reads as generated | `references/foundations.md` |
| Hero entrance, split-text headline, clip-path or masked reveal, counter | `references/entrances-text.md` |
| Scroll progress, scroll-linked entrance, sticky storytelling, pinned or horizontal sections, smooth scroll | `references/scroll.md` |
| Depth layers on scroll, pointer depth, image or video parallax, parallax in React | `references/parallax.md` |
| Hover and press timing, form-submit feedback, magnetic button, cursor follower, tilt, drag, a stepped dial, view transitions | `references/interaction.md` |
| 3D object to rotate, exploded view on scroll, one context for two separated stages, glTF model, React Three Fiber island | `references/webgl-3d.md` |
| What to import and leave out, unused decoder files in the build, making posters, model compression and textures | `references/webgl-assets.md` |
| Scroll-driven camera path, shader background, particles, image distortion, WebGPU | `references/webgl-effects.md` |
| Which library, its licence and size, when CSS is enough | `references/libraries.md` |
| Reduced-motion variants, WebGL fallback, pausing, budgets, protecting the hero | `references/safety.md` |

For any 3D or WebGL work, read `references/safety.md` first (the scene
recipes import its helpers), then "Before any scene" in
`references/webgl-3d.md`, then only the recipe you need.

## Safety rails

These hold for every effect. `references/safety.md` has the code.

1. **The hero paints without JavaScript.** Headline, lead, and call to action
   are in the HTML and visible at first paint. Do not start the headline or
   hero image at opacity 0: an element at opacity 0 is not a Largest
   Contentful Paint candidate until it shows, so a long entrance delays LCP.
   Load 3D code after the load event, with a poster image in its place until
   the first frame is drawn.
2. **Reduce, do not delete.** The direction's own statement about reduced
   motion wins; what follows is the default. Under
   `prefers-reduced-motion: reduce`, remove parallax, scrubbed scroll
   animation, autoplay loops, and large travel. Keep feedback and short
   fades. A 3D scene loads only if it is still and user-driven; skipping it
   and showing the posters is the simplest correct choice. Write the reduced variant per effect, and
   listen for the preference changing while the page is open. A global rule
   that zeroes every animation's duration is itself a recognised tell.
3. **WebGL is optional.** Check for a context before downloading any 3D code.
   In React, wrap the canvas in an error boundary. Handle context loss. The
   fallback is a real image, never an empty box.
4. **One canvas.** A second WebGL context doubles memory and GPU work. Put
   related 3D moments in one sticky canvas.
5. **Nothing runs off screen.** Canvases, video, and loops stop when out of
   view or when the tab is hidden.
6. **Scroll never re-renders a framework.** Read scroll position in the frame
   loop or let CSS drive it. No `setState` per scroll event or per frame.
7. **Animate `transform` and `opacity` only.** Never `top`, `left`, `width`,
   or `height`.
8. **Self-host every asset.** Models, decoders, environment maps, and fonts
   come from your origin. Some helpers default to third-party CDNs; override
   them.
9. **Interaction has a keyboard path.** Anything the pointer can rotate,
   drag, or toggle, the keyboard can too.

## Definition of done

Check each item in a browser. A browser tool is best; without one, use the
capture script of `landing-skills:landing-review` for screenshots and say
which checks you could not do.

- Every moment named in `Motion and 3D intent` exists, and nothing else was
  added beyond feedback states.
- With JavaScript disabled: all copy and the conversion action are present.
- With reduced motion emulated: the page is complete and readable, nothing
  auto-plays, no parallax, no scrub; feedback states still respond.
- With WebGL disabled (Chromium flag `--disable-webgl`): the poster or static
  image shows where the scene would be, and the hero text and call to action
  are untouched.
- The hero text is visible before any 3D code has loaded (throttle the
  network to check).
- No two sections share the same entrance animation by default; the
  signature moment is the only large one.
- Canvases stop drawing when scrolled out of view (Performance panel, or a
  draw-call counter as in `references/safety.md`).
- The network panel shows no request to a third-party host for 3D assets.
- In a production build, three.js and other 3D code are in a chunk that is
  not part of the entry bundle.
- Keyboard: every rotate, drag, and toggle works with keys; focus stays
  visible.

The tell detector `detect-tells.mjs` belongs to
`landing-skills:landing-review` and is an optional aid. If it is available,
run it on the page; it flags pattern-level tells such as stock reveal
attributes and the reduced-motion kill switch. The required check is by eye:
scroll the whole page once with motion on and once with reduced motion, and
confirm every moving thing has the role you wrote down for it.

Finish with a short summary for the user: each moment added and its purpose,
libraries added with their licences, the assets still needed (for example a
real model to replace a primitive stand-in, a poster image), and any check
you could not run.
