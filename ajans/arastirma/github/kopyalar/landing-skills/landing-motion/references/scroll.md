# Scroll

## Contents

- Rules for scroll effects
- Scroll progress indicator (native)
- Scroll-linked entrance (native view timeline)
- Sticky storytelling (no library)
- Pinned scrubbed sequence (GSAP ScrollTrigger)
- Horizontal section (GSAP, native scroller underneath)
- Smooth scrolling (Lenis)

## Rules for scroll effects

- **Native first.** CSS scroll-driven animations (`animation-timeline:
  scroll()` and `view()`) run off the main thread. Support: Chrome and Edge
  115, Safari 26; Firefox stable does not have them. Gate every use with
  `@supports (animation-timeline: scroll())` and make the unsupported state a
  complete static page.
- **Scroll stays the visitor's.** Do not take over the wheel or keyboard to
  move between sections. Pin a section only when its content is a sequence
  that needs it, and keep the runway short: roughly one screen of scrolling
  per step the visitor must notice, never several screens per step (a
  judgement, not a measured rule). A runway many screens long for a handful
  of steps makes the visitor scroll through motion to reach content; show
  the whole sequence early and let each step add one thing.
- **Text in normal flow.** In storytelling, the steps are ordinary HTML in
  the document, readable with motion off. The visual changes beside them.
- **Re-measure after load.** Pinned sections measure the page; call
  `ScrollTrigger.refresh()` after fonts and images settle.
- **Reduced motion removes scrub.** Show the end state or a set of still
  states. Scroll-linked movement is a named vestibular trigger.

### Scroll progress indicator (native)

#### Use when

A long single page where knowing how far you are helps, such as a detailed
product story. A thin bar at the top fills as you scroll.

#### Avoid when

Short pages; pages with a sticky header that already carries a lot.

#### Code

New to this skill: `.scroll-progress`.

```html
<div class="scroll-progress" aria-hidden="true"></div>
```

```css
.scroll-progress { display: none }
@supports (animation-timeline: scroll()) {
  @media (prefers-reduced-motion: no-preference) {
    .scroll-progress {
      display: block;
      position: fixed; inset: 0 0 auto 0; height: 3px; z-index: 10;
      background: currentColor;
      transform-origin: 0 50%;
      animation: grow-x linear both;
      animation-timeline: scroll(root block);
    }
  }
}
@keyframes grow-x { from { transform: scaleX(0) } to { transform: scaleX(1) } }
```

#### Reduced motion

Hidden. It moves continuously with the scroll, and the information it adds is
small. The browser scrollbar already shows position.

#### Cost

No JavaScript, compositor-only, off the main thread. In Firefox the bar is
not shown.

### Scroll-linked entrance (native view timeline)

#### Use when

One element should settle into place as it crosses into view, tied to the
scroll position (it reverses if you scroll back before it finishes). Good for
a single product shot that leads a section.

#### Avoid when

You would apply it to every section (that is the uniform reveal); the
element is text the visitor reads while it moves.

#### Code

New to this skill: `.product-shot`.

```css
@supports (animation-timeline: view()) {
  @media (prefers-reduced-motion: no-preference) {
    .product-shot {
      animation: settle-in linear both;
      animation-timeline: view();
      animation-range: entry 10% cover 40%;
    }
  }
}
@keyframes settle-in {
  from { opacity: 0.4; transform: translateY(48px) scale(0.96) }
  to { opacity: 1; transform: none }
}
```

`animation-range: entry 10% cover 40%` starts when the element is 10 percent
into the viewport and finishes by the time it has covered 40 percent of its
path. It starts at 0.4 opacity, so the image is never invisible.

#### Reduced motion

Not applied. The image sits in place.

#### Cost

No JavaScript, off the main thread. Firefox shows the static image.

### Sticky storytelling (no library)

#### Use when

The intent names a sequence of product states to show while the visitor
reads the steps: setup to result, draft to finished, before to after. A
sticky stage on one side changes state as each step's text passes the middle
of the viewport.

#### Avoid when

The states need continuous, scrubbed motion between them (use the GSAP
pinned recipe); there are fewer than three steps (show them side by side).

#### Code

New to this skill: `.story`, `.story-stage`, `.story-steps`, `.is-active`.

```html
<section class="story" aria-labelledby="story-title">
  <h2 id="story-title" style="grid-column: 1 / -1">[Section heading]</h2>
  <div class="story-stage" aria-hidden="true">
    <div data-state="one" class="is-active">[PLACEHOLDER: screen for step one]</div>
    <div data-state="two">[PLACEHOLDER: screen for step two]</div>
    <div data-state="three">[PLACEHOLDER: screen for step three]</div>
  </div>
  <ol class="story-steps">
    <li data-step="one"><p>[Step one copy]</p></li>
    <li data-step="two"><p>[Step two copy]</p></li>
    <li data-step="three"><p>[Step three copy]</p></li>
  </ol>
</section>
```

```css
.story { display: grid; grid-template-columns: 1fr 1fr; gap: 4rem; padding-inline: 2rem }
.story-stage { position: sticky; top: 10vh; height: 80vh; align-self: start; display: grid; place-items: center }
.story-stage [data-state] { grid-area: 1 / 1; width: 100%; aspect-ratio: 4/3; opacity: 0 }
.story-stage [data-state].is-active { opacity: 1 }
.story-steps > li { min-height: 70vh; display: flex; align-items: center }
@media (prefers-reduced-motion: no-preference) {
  .story-stage [data-state] { transition: opacity 300ms cubic-bezier(0.2, 0, 0, 1) }
}
@media (max-width: 48rem) {
  .story { grid-template-columns: 1fr }
  .story-stage { top: 0; height: 45vh; z-index: 1; background: Canvas }
}
```

```js
const steps = [...document.querySelectorAll('[data-step]')];
const states = document.querySelectorAll('.story-stage [data-state]');
const inBand = new Set();
const stepObserver = new IntersectionObserver((entries) => {
  for (const e of entries) e.isIntersecting ? inBand.add(e.target) : inBand.delete(e.target);
  const current = steps.filter((s) => inBand.has(s)).pop(); // lowest step in the band wins
  if (!current) return;
  states.forEach((s) => s.classList.toggle('is-active', s.dataset.state === current.dataset.step));
}, { rootMargin: '-45% 0px -45% 0px' }); // a thin band across the middle of the viewport
steps.forEach((el) => stepObserver.observe(el));
```

Keep the set of steps in the band, not just the latest event: two steps can
touch the band at once, and when one leaves, the other must take over even
though it fired no event of its own.

With JavaScript off, the first state shows and every step's text is
readable. If the stage images carry information, also give each step its own
image inside the `li`, so visitors without JavaScript see each state where
its text is.

#### Reduced motion

States switch instantly (the transition is only defined under
`no-preference`). A state change is not movement, so the sequence stays.

#### Cost

One `IntersectionObserver`, no scroll listener, no library.

### Pinned scrubbed sequence (GSAP ScrollTrigger)

#### Use when

Parts must move continuously in step with the scroll while the section stays
in place: an assembly coming apart, a diagram building up, a sequence of
layers. The section pins, the timeline scrubs, then the page continues.

#### Avoid when

Steps can be discrete states (use sticky storytelling, no library); the
effect is decoration rather than explanation; you would pin more than one or
two sections on the page.

#### Code

New to this skill: `.assembly`, `.assembly-parts`.

```html
<section class="assembly" aria-labelledby="assembly-title">
  <h2 id="assembly-title">[Section heading]</h2>
  <div class="assembly-parts" aria-hidden="true">
    <div></div><div></div><div></div><div></div><div></div>
  </div>
</section>
```

```js
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
gsap.registerPlugin(ScrollTrigger);

const mm = gsap.matchMedia();
mm.add({
  motion: '(prefers-reduced-motion: no-preference)',
  reduce: '(prefers-reduced-motion: reduce)',
}, (context) => {
  const parts = gsap.utils.toArray('.assembly-parts > div');
  if (context.conditions.reduce) {
    // Reduced: show the end state, no pin, no scrub.
    gsap.set(parts, { y: (i) => (i - 2) * -70 });
    return;
  }
  gsap.timeline({
    scrollTrigger: {
      trigger: '.assembly',
      pin: true,
      start: 'top top',
      end: '+=150%',   // runway: 1.5 screens for five parts
      scrub: 0.5,
    },
  }).to(parts, { y: (i) => (i - 2) * -70, ease: 'none', stagger: 0.1 });
});

// Pinned sections measure the page. Re-measure once fonts and images have settled.
window.addEventListener('load', () => ScrollTrigger.refresh());
```

`gsap.matchMedia` re-runs the callback when the preference changes, and
reverts everything the previous run created, so switching reduced motion on
while the page is open removes the pin.

#### Reduced motion

No pin, no scrub: the parts are set to their end state once. The section
scrolls like any other.

#### Cost

GSAP core plus ScrollTrigger. ScrollTrigger inserts a pin spacer (the
section's height plus the runway), so the page grows by the runway; give the
section a stable height so it does not shift. Per-frame work while the
section is pinned.

Runway: `end: '+=150%'` adds 1.5 screens of scroll on top of the section's
own screen, so the pinned section occupies 2.5 screens of page height
(measured: a 2000 px pin spacer on an 800 px viewport). Judgement for the
length: about half a screen to one screen per step the visitor must notice,
and rarely more than two to three screens in total. A full-page screenshot
shows the runway as a tall, mostly empty band, because the content is pinned
while the page scrolls past; that is expected. `landing-skills:landing-review`
captures such sections with a full-page capture first and switches to viewport captures only if that fails or is cut off.

### Horizontal section (GSAP, native scroller underneath)

#### Use when

A set of panels reads naturally side by side (finishes, variants, a
timeline) and the intent wants them to slide past as the visitor scrolls
down.

#### Avoid when

The panels hold long text; on narrow screens (the recipe turns itself off
below 48rem); when a plain grid would show the panels at once.

#### Code

New to this skill: `.rail`, `.rail-track`, `.rail-panel`, `.is-pinned`.

The base is a native horizontal scroller with scroll snap: that is the no-JS,
reduced-motion, and small-screen version. The script upgrades it.

```html
<section class="rail" aria-label="[What the panels are]">
  <div class="rail-track">
    <article class="rail-panel"><h3>[Panel one]</h3></article>
    <article class="rail-panel"><h3>[Panel two]</h3></article>
    <article class="rail-panel"><h3>[Panel three]</h3></article>
  </div>
</section>
```

```css
.rail { overflow-x: auto; scroll-snap-type: x mandatory; overscroll-behavior-x: contain }
.rail-track { display: flex; width: max-content }
.rail-panel { flex: 0 0 100vw; height: 100vh; scroll-snap-align: start; display: grid; place-items: center }
.rail.is-pinned { overflow: hidden }
```

```js
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
gsap.registerPlugin(ScrollTrigger);

const mm = gsap.matchMedia();
mm.add('(prefers-reduced-motion: no-preference) and (min-width: 48rem)', () => {
  const rail = document.querySelector('.rail');
  const panels = gsap.utils.toArray('.rail-panel');
  rail.classList.add('is-pinned');
  gsap.to('.rail-track', {
    xPercent: -100 * (panels.length - 1) / panels.length,
    ease: 'none',
    scrollTrigger: {
      trigger: rail,
      pin: true,
      scrub: 0.5,
      snap: 1 / (panels.length - 1),
      end: () => '+=' + rail.scrollWidth,
    },
  });
  return () => rail.classList.remove('is-pinned'); // runs when the query stops matching
});
```

The track is tweened, not the panels, so the distance is a fraction of the
track's own width: `(n - 1) / n` of it.

#### Reduced motion

The media query does not match, so the section stays a native horizontal
scroller the visitor moves by hand, with snap points.

#### Cost

GSAP core plus ScrollTrigger; adds the track's width as vertical runway. A
keyboard user tabbing into a panel in the pinned version is scrolled by the
browser normally, because the page scroll is still native.

### Smooth scrolling (Lenis)

#### Use when

The page has scrubbed scroll effects that look stepped with a mouse wheel's
coarse increments, and you want wheel input smoothed. Lenis keeps native
scrolling underneath, so `position: sticky`, anchors, and assistive
technology keep working.

#### Avoid when

The page has no scroll-linked animation: smooth scrolling alone is
decoration that changes how the page feels to scroll, and some visitors
dislike it. Pages that rely on CSS scroll snap (Lenis does not support it).

#### Code

New to this skill: none (Lenis adds `.lenis` to `<html>` itself).

```js
import Lenis from 'lenis';
import 'lenis/dist/lenis.css';
import { gsap } from 'gsap';
import { ScrollTrigger } from 'gsap/ScrollTrigger';
gsap.registerPlugin(ScrollTrigger);

const lenis = new Lenis({
  anchors: true,             // in-page links scroll through Lenis
  // respectReducedMotion defaults to true in 1.3.26: smoothing is off under reduced motion.
});
lenis.on('scroll', ScrollTrigger.update);
gsap.ticker.add((time) => lenis.raf(time * 1000)); // GSAP's ticker drives Lenis: one rAF loop
gsap.ticker.lagSmoothing(0);
```

Without GSAP, pass `autoRaf: true` to the constructor instead of the ticker
lines.

If the page also sets `html { scroll-behavior: smooth }` in CSS, wrap it in
`@media (prefers-reduced-motion: no-preference)`; an unguarded smooth
`scroll-behavior` is a common miss.

#### Reduced motion

In Lenis 1.3.26 the `respectReducedMotion` option defaults to `true`: under
reduced motion, smoothing is disabled (scroll tracks the input 1:1) and
programmatic scrolls, including anchor jumps, are instant. Checked in the
installed source and in a browser test. Leave it on.

#### Cost

About 5.5 kB gzipped. A continuous rAF loop while the page is open. Safari
caps it at 60 fps and low-power mode at 30 fps.
