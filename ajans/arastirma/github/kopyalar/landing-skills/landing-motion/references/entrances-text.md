# Entrances and text

## Contents

- Rules for every entrance
- Orchestrated hero entrance (CSS)
- Split-text masked line reveal (GSAP SplitText)
- Clip-path reveal on entering view
- Counter for a sourced figure

## Rules for every entrance

- **The headline and hero image are visible at first paint.** An element at
  opacity 0 is not a Largest Contentful Paint candidate until it shows, so a
  hero that fades in from nothing delays LCP by the length of the fade. Move
  the headline (transform), do not fade it from zero. If the hero has a large
  image, that image is usually the LCP element: do not hide it either.
- **One timeline per hero.** All hero elements belong to one sequence with
  one lead element, overlapping starts, and one easing family. Separate
  tweens with separate delays drift apart and read as noise.
- **Entrances run once.** Never replay on scroll back.
- **Content hidden before a script runs needs a failsafe.** See "Hide before
  JavaScript, with a failsafe" in `safety.md`.
- Below the hero, an entrance needs a reason. "It is the next section" is
  not one.
- Class names follow the hero markup `landing-skills:landing-build` writes:
  `.hero`, `.hero__title`, `.hero__lede`, `.hero__action`, `.hero__media`,
  and `.button`. Match them to your page if it was built another way.

### Orchestrated hero entrance (CSS)

#### Use when

The intent names a hero entrance and the page has no other reason to load an
animation library. Covers most heroes: headline and image settle into place,
lead and action follow. No JavaScript, so it also runs when scripts fail.

#### Avoid when

The headline must be split into lines that animate separately (use the
SplitText recipe), or the sequence must coordinate with scroll or a 3D scene
(use a GSAP timeline).

#### Code

New to this skill: none. From `landing-skills:landing-build`, used unchanged: `.hero__title`, `.hero__lede`, `.hero__action`, `.hero__media`, `.button`.

The headline and the hero image only move; they are never transparent, so
either can be the LCP element from the first frame. The lead and action fade
and rise with overlapping delays: they are small text, not LCP candidates.

```html
<div class="hero">
  <h1 class="hero__title">[Headline]</h1>
  <p class="hero__lede">[Lead]</p>
  <p class="hero__action"><a class="button" href="/signup">[Primary action]</a></p>
  <div class="hero__media" role="img" aria-label="[PLACEHOLDER: product screenshot, 4:3]"></div>
</div>
```

```css
:root {
  --motion-ease-out: cubic-bezier(0.2, 0, 0, 1);
  --motion-dur-hero: 700ms;
  --motion-dur-enter: 450ms;
}

@keyframes hero-settle { from { transform: translateY(0.35em) } }
@keyframes hero-media-settle { from { transform: translateY(24px) scale(0.98) } }
@keyframes hero-rise { from { opacity: 0; transform: translateY(16px) } }
@keyframes hero-fade { from { opacity: 0 } }

@media (prefers-reduced-motion: no-preference) {
  .hero__title { animation: hero-settle var(--motion-dur-hero) var(--motion-ease-out) both }
  .hero__media { animation: hero-media-settle var(--motion-dur-hero) var(--motion-ease-out) 80ms both }
  .hero__lede { animation: hero-rise var(--motion-dur-enter) var(--motion-ease-out) 120ms both }
  .hero__action { animation: hero-rise var(--motion-dur-enter) var(--motion-ease-out) 200ms both }
}
```

Keyframes with only a `from` state animate to the element's own styles, so
the end state is whatever the static page already shows. Measured 100 ms
after load: title and media at opacity 1 while moving, lede at opacity 0.

#### Reduced motion

No travel. The lead and action fade in over 200 ms; the headline and image
appear at once.

```css
@media (prefers-reduced-motion: reduce) {
  .hero__lede, .hero__action { animation: hero-fade 200ms linear both }
}
```

#### Cost

No JavaScript, compositor-only properties. Last element settles at 650 ms.
The headline and the image are opaque from the first frame, so the entrance
does not delay LCP. The lead and action are hidden for up to 200 ms; they are
not LCP candidates next to a headline and an image.

### Split-text masked line reveal (GSAP SplitText)

#### Use when

The headline is the signature moment and the page has a hero image or other
element that is the LCP candidate, or the headline is short enough that a
0.8 s line reveal is acceptable as its first appearance. Lines slide up from
behind a mask, one after another.

#### Avoid when

The headline is the LCP element on a page where loading speed is the
priority; body copy or long paragraphs (splitting multiplies DOM nodes and
costs main-thread time on every re-split); splitting into characters for
anything longer than a few words.

#### Code

New to this skill: `.motion-pending` (on `<html>`). From `landing-skills:landing-build`, used unchanged: `.hero__title`, `.hero__lede`, `.hero__action`, `.hero__media`, `.button`.

Uses the "Hide before JavaScript, with a failsafe" pattern from `safety.md`:
the headline is hidden only when motion is allowed, and shown after 2.5 s
whatever happens.

```html
<head>
  <script>
    if (matchMedia('(prefers-reduced-motion: no-preference)').matches) {
      document.documentElement.classList.add('motion-pending');
      setTimeout(() => document.documentElement.classList.remove('motion-pending'), 2500);
    }
  </script>
  <style>
    .motion-pending .hero__title, .motion-pending .hero__lede, .motion-pending .hero__action { visibility: hidden }
  </style>
</head>
<body>
  <div class="hero">
    <h1 class="hero__title">[Headline from copy.md]</h1>
    <p class="hero__lede">[Lead sentence]</p>
    <p class="hero__action"><a class="button" href="/order">[Primary action]</a></p>
    <img class="hero__media" src="/img/hero.avif" alt="[PLACEHOLDER: product photo, 3:2]" width="1200" height="800">
  </div>
</body>
```

```js
import { gsap } from 'gsap';
import { SplitText } from 'gsap/SplitText';
gsap.registerPlugin(SplitText);

const mm = gsap.matchMedia();
mm.add('(prefers-reduced-motion: no-preference)', () => {
  SplitText.create('.hero__title', {
    type: 'lines',
    mask: 'lines',
    autoSplit: true,
    onSplit(self) {
      gsap.set('.hero__title', { visibility: 'visible' });
      // Returning the animation lets autoSplit revert and replay it cleanly on re-split.
      return gsap.timeline({ defaults: { ease: 'power3.out' } })
        .from(self.lines, { yPercent: 100, duration: 0.8, stagger: 0.08 })
        .fromTo('.hero__lede, .hero__action', { autoAlpha: 0, y: 12 },
          { autoAlpha: 1, y: 0, duration: 0.5, stagger: 0.06 }, '-=0.45');
    },
  });
});
```

What the plugin handles (checked against GSAP 3.15.0): `mask: "lines"` wraps
each line in a clipping element; `autoSplit: true` re-splits after web fonts
load and on resize, which is why the animation is created inside `onSplit`
and returned; the default `aria: "auto"` puts the full text in an
`aria-label` on the heading and hides the split pieces from assistive
technology, so screen readers hear one sentence.

#### Reduced motion

`gsap.matchMedia` only runs the split when motion is allowed, and the inline
script never adds `motion-pending` under reduced motion. The headline is
plain text, visible at first paint, never split.

#### Cost

GSAP core about 27 kB gzipped plus the SplitText plugin, loaded as a module
after HTML parsing. Main-thread split on load and on every re-split. The
headline is hidden until the module runs (at most 2.5 s, through the
failsafe), so do not use this when the headline is the LCP element and speed
matters more than the reveal.

### Clip-path reveal on entering view

#### Use when

One image or figure below the hero deserves an entrance, such as the
product shot that opens the "how it works" section. The image wipes open
from the top once, when it is scrolled into view.

#### Avoid when

More than one or two images on the page (it becomes the uniform reveal);
the image is the LCP element; galleries.

#### Code

New to this skill: `.reveal-frame`, `.is-pending`, `.is-revealed`.

Hidden only by the script that reveals it, after the observer exists: if
the script fails or never loads, the image was never hidden. Runs once, then
stops observing.

```html
<figure class="reveal-frame" data-reveal>
  <img src="/img/workflow.avif" alt="[PLACEHOLDER: describe the photo]" width="1600" height="900">
</figure>
```

```css
.reveal-frame.is-pending { clip-path: inset(0 0 100% 0) }
.reveal-frame.is-revealed {
  clip-path: inset(0 0 0 0);
  transition: clip-path 900ms cubic-bezier(0.2, 0, 0, 1);
}
```

```js
// Content is hidden only by this script, after the observer exists: if the script fails, nothing is hidden.
const revealObserver = new IntersectionObserver((entries, obs) => {
  for (const e of entries) {
    if (!e.isIntersecting) continue;
    e.target.classList.replace('is-pending', 'is-revealed');
    obs.unobserve(e.target);
  }
}, { rootMargin: '0px 0px -15% 0px' });
document.querySelectorAll('[data-reveal]').forEach((el) => {
  el.classList.add('is-pending');
  revealObserver.observe(el);
});
```

Use it on an image below the first screen: the class is added when the
script runs, so an image already in view at that moment would flash.
Tested by throwing an error before the observer is created: the image stayed
visible.

#### Reduced motion

Replace the wipe with a 250 ms fade.

```css
@media (prefers-reduced-motion: reduce) {
  .reveal-frame.is-pending { clip-path: none; opacity: 0 }
  .reveal-frame.is-revealed { opacity: 1; transition: opacity 250ms linear }
}
```

#### Cost

A few lines of script, no library. `clip-path` repaints the element during
the 900 ms transition but does not trigger layout. Reserve the image's space
with `width` and `height` attributes so nothing shifts.

### Counter for a sourced figure

#### Use when

The page states a real figure with a named source (a measured result, a
public count), and the number counting up helps the visitor notice it. The
final value is in the HTML.

#### Avoid when

The figure is round, estimated, or has no source: a count-up on an unsourced
number is a recognised generated-page tell, and the number should not be on
the page at all until it is real. Avoid drop-in count-up components; they are
also a recognised tell.

#### Code

New to this skill: `.figure-number`.

```html
<p>
  <span class="figure-number" data-count-to="1284">1,284</span> [what was counted]
  <small>[PLACEHOLDER: source of this figure]</small>
</p>
```

```css
.figure-number { font-variant-numeric: tabular-nums } /* digits keep their width while changing */
```

```js
// The final value is already in the HTML. Only animate it when motion is allowed.
const format = new Intl.NumberFormat(document.documentElement.lang);
function countTo(el, duration = 1200) {
  const target = Number(el.dataset.countTo);
  const start = performance.now();
  const step = (now) => {
    const t = Math.min(1, (now - start) / duration);
    const eased = 1 - Math.pow(1 - t, 3); // ease-out cubic
    el.textContent = format.format(Math.round(target * eased));
    if (t < 1) requestAnimationFrame(step);
  };
  requestAnimationFrame(step);
}
if (matchMedia('(prefers-reduced-motion: no-preference)').matches) {
  const countObserver = new IntersectionObserver((entries, obs) => {
    for (const e of entries) {
      if (!e.isIntersecting) continue;
      countTo(e.target);
      obs.unobserve(e.target);
    }
  }, { threshold: 0.6 });
  document.querySelectorAll('[data-count-to]').forEach((el) => countObserver.observe(el));
}
```

#### Reduced motion

The script does nothing; the final number from the HTML stays.

#### Cost

One `requestAnimationFrame` loop for 1.2 s per counter, once. No layout
shift because of `tabular-nums`.
