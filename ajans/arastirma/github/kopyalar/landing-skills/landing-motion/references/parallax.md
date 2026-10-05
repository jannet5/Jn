# Parallax

## Contents

- Rules for parallax
- Layered depth (CSS view timeline)
- Layered depth (JavaScript, one shared loop)
- Parallax layer in React
- Pointer-driven depth
- Image and video parallax

## Rules for parallax

- **Parallax is a named vestibular trigger.** WCAG 2.3.3 (Animation from
  Interactions) lists parallax scrolling and unnecessary movement during
  scroll among the triggers; effects include nausea and migraine. Under
  `prefers-reduced-motion: reduce`, remove it entirely. Slowing it down is
  not enough.
- **Decorative layers only.** Move images, shapes, and backgrounds. Never
  move the text column or any copy the visitor is reading: drifting body text
  is harder to read and gains nothing.
- **Small depth.** Depth values between 0.1 and 0.3 (in the recipes below,
  a layer travels `depth × 40vh` each way across the section) read as depth;
  above that the layer separates from its content. This range is a
  judgement from building the recipes, not a measured threshold.
- **Transform only, one loop.** No `top` or `margin`, no scroll listener per
  element, no layout reads interleaved with writes. A scroll listener that
  reads `getBoundingClientRect` and writes a transform for each element on
  every scroll event, with no `requestAnimationFrame`, is the pattern that
  janks on phones.
- **CSS first.** Where scroll-driven animations exist (Chromium 115,
  Safari 26), CSS runs parallax off the main thread. The JavaScript loop is
  the fallback for Firefox, or skip parallax there and show the static
  layout.

### Layered depth (CSS view timeline)

#### Use when

A detail section has a decorative composition (product image over a shape,
a cut-out over a texture) and the intent names parallax for it. Layers move
at different speeds as the section crosses the viewport.

#### Avoid when

The layers contain text; the section is short and the movement would be a
few pixels; every section would get it.

#### Code

New to this skill: `.detail`, `.detail-art`, `.layer`. From `landing-skills:landing-build`, used unchanged: `.section`, `.split`.

The named timeline `--detail` is set on the section, so every layer is
measured against the same static box, not against its own moving position.
`detail`, `detail-art`, and `layer` are classes this skill adds; on a page
from `landing-skills:landing-build`, add `detail` to the existing `.section`
and put `detail-art` on the media column of its `.split`.

```html
<section class="detail">
  <div class="detail-text"><h2>[Detail heading]</h2><p>[Detail copy]</p></div>
  <div class="detail-art" aria-hidden="true">
    <div class="layer back" data-depth="0.1" style="--depth: 0.1"></div>
    <div class="layer front" data-depth="0.25" style="--depth: 0.25"></div>
  </div>
</section>
```

```css
.detail { position: relative; min-height: 80vh; display: grid; grid-template-columns: 1fr 1fr; align-items: center; overflow: clip; view-timeline: --detail }
.detail-art { position: relative; height: 70vh }
.detail-art .layer { position: absolute; inset: 10% }
.detail-art .layer.front { inset: 25% }

@supports (animation-timeline: view()) {
  @media (prefers-reduced-motion: no-preference) {
    .detail [data-depth] {
      animation: depth-shift linear both;
      animation-timeline: --detail;
      animation-range: cover;
    }
  }
}
@keyframes depth-shift {
  from { transform: translate3d(0, calc(var(--depth) * 40vh), 0) }
  to { transform: translate3d(0, calc(var(--depth) * -40vh), 0) }
}
```

`data-depth` is for the JavaScript fallback below; `--depth` is for CSS.
Both carry the same number.

#### Reduced motion

The animation is only declared under `no-preference`; under reduce the
layers sit in their static positions.

#### Cost

No JavaScript, off the main thread. `overflow: clip` on the section keeps
moving layers from creating horizontal scroll.

### Layered depth (JavaScript, one shared loop)

#### Use when

You want the CSS recipe's parallax in browsers without scroll-driven
animations (Firefox stable), or the stack needs layers registered from
components. One module serves every layer on the page.

#### Avoid when

Static layers in Firefox are acceptable (often they are); the page has no
CSS version to fall back from.

#### Code

New to this skill: none beyond the previous recipe.

`parallax.js`:

```js
// parallax.js: one scroll listener and one rAF for every layer on the page.
const reduce = matchMedia('(prefers-reduced-motion: reduce)');
const layers = new Map(); // layer element -> { depth, frame }
const visibleFrames = new Set();
let queued = 0;

const io = new IntersectionObserver((entries) => {
  for (const e of entries) e.isIntersecting ? visibleFrames.add(e.target) : visibleFrames.delete(e.target);
  schedule();
}, { rootMargin: '25% 0px' });

function update() {
  queued = 0;
  if (reduce.matches) return;
  const vh = innerHeight;
  // Read every rect first, then write every transform: no layout thrash.
  const rects = new Map([...visibleFrames].map((f) => [f, f.getBoundingClientRect()]));
  for (const [layer, { depth, frame }] of layers) {
    const r = rects.get(frame);
    if (!r) continue;
    const t = (vh - r.top) / (vh + r.height); // 0 entering at the bottom, 1 leaving at the top
    const shift = depth * 40 * (1 - 2 * t);   // in vh, the same curve as the CSS version
    layer.style.transform = `translate3d(0, ${shift.toFixed(2)}vh, 0)`;
  }
}
function schedule() { if (!queued) queued = requestAnimationFrame(update); }

addEventListener('scroll', schedule, { passive: true });
addEventListener('resize', schedule);
reduce.addEventListener('change', () => {
  if (reduce.matches) for (const layer of layers.keys()) layer.style.transform = '';
  schedule();
});

// frame: the static element whose position drives the layer. Never the moving layer itself.
export function addParallax(layer, depth = 0.2, frame = layer.parentElement) {
  layers.set(layer, { depth, frame });
  io.observe(frame);
  return () => {
    layers.delete(layer);
    if (![...layers.values()].some((l) => l.frame === frame)) { io.unobserve(frame); visibleFrames.delete(frame); }
    layer.style.transform = '';
  };
}
```

Use it only where CSS cannot run:

```js
import { addParallax } from './parallax.js';
if (!CSS.supports('animation-timeline: view()')) {
  document.querySelectorAll('.detail [data-depth]').forEach((el) => addParallax(el, Number(el.dataset.depth), el.closest('.detail')));
}
```

Tested in Firefox against the CSS version in Chromium: the two produce the
same offsets at the same scroll positions.

#### Reduced motion

`update()` does nothing under reduce, and switching the preference on while
the page is open clears every transform at once.

#### Cost

One passive scroll listener and at most one `getBoundingClientRect` per
visible section per frame, all reads before all writes. Off-screen sections
are skipped through the `IntersectionObserver`.

### Parallax layer in React

#### Use when

A React page (Vite, Next) needs parallax layers in detail sections. The
component renders the CSS variables so CSS drives it where it can, and
registers with the shared loop where it cannot.

#### Avoid when

You are tempted to compute the offset in React state. A `useState` updated on
scroll re-renders the component tree on every scroll event; that is the
mistake this recipe exists to avoid.

#### Code

New to this skill: none beyond the first parallax recipe.

Uses `parallax.js` from the previous recipe and the CSS from the first one.

```jsx
import { useEffect, useRef } from 'react';
import { addParallax } from './parallax.js';

// CSS drives the layer where scroll-driven animations exist; the shared JS loop drives it elsewhere.
// Decorative layers only: never wrap text the visitor is reading.
export function ParallaxLayer({ depth = 0.2, className, children }) {
  const ref = useRef(null);
  useEffect(() => {
    if (CSS.supports('animation-timeline: view()')) return;
    return addParallax(ref.current, depth, ref.current.closest('.detail'));
  }, [depth]);
  return (
    <div ref={ref} className={className} data-depth={depth} style={{ '--depth': depth }} aria-hidden="true">
      {children}
    </div>
  );
}
```

```jsx
<section className="detail">
  <div><h2>[Detail heading]</h2><p>[Detail copy, which does not move]</p></div>
  <div className="detail-art" aria-hidden="true">
    <ParallaxLayer depth={0.1} className="layer back" />
    <ParallaxLayer depth={0.25} className="layer front" />
  </div>
</section>
```

Tested in a Vite and React 19 app: the app component rendered once and did
not re-render while scrolling.

#### Reduced motion

Inherited: the CSS is only declared under `no-preference`, and the shared
loop does nothing under reduce.

#### Cost

No React renders after mount. The cost is the CSS or the shared loop.

### Pointer-driven depth

#### Use when

A hero or showcase composition on desktop where layers shifting slightly
with the pointer adds a sense of space around the product.

#### Avoid when

Touch devices (no hover, the effect never shows); a section with text in the
moving layers; any section where the visitor's pointer is busy with controls.

#### Code

New to this skill: `.scene`.

```html
<section class="scene" aria-hidden="true">
  <div data-pointer-depth="0.2"></div>
  <div data-pointer-depth="0.5"></div>
</section>
```

```css
.scene { position: relative; height: 70vh; overflow: clip }
.scene [data-pointer-depth] { position: absolute; inset: 20% } /* each layer holds an image or shape */
```

```js
import { gsap } from 'gsap';

const mm = gsap.matchMedia();
mm.add('(prefers-reduced-motion: no-preference) and (hover: hover) and (pointer: fine)', () => {
  const scene = document.querySelector('.scene');
  const MAX = 24; // px of travel for depth 1
  const movers = gsap.utils.toArray('[data-pointer-depth]', scene).map((el) => ({
    depth: Number(el.dataset.pointerDepth),
    x: gsap.quickTo(el, 'x', { duration: 0.6, ease: 'power3.out' }),
    y: gsap.quickTo(el, 'y', { duration: 0.6, ease: 'power3.out' }),
  }));
  const onMove = (e) => {
    const r = scene.getBoundingClientRect();
    const nx = (e.clientX - r.left) / r.width - 0.5;  // -0.5 to 0.5
    const ny = (e.clientY - r.top) / r.height - 0.5;
    for (const m of movers) { m.x(-nx * MAX * m.depth * 2); m.y(-ny * MAX * m.depth * 2); }
  };
  const onLeave = () => movers.forEach((m) => { m.x(0); m.y(0); });
  scene.addEventListener('pointermove', onMove);
  scene.addEventListener('pointerleave', onLeave);
  return () => { // undone when the media query stops matching
    scene.removeEventListener('pointermove', onMove);
    scene.removeEventListener('pointerleave', onLeave);
  };
});
```

`gsap.quickTo` creates one reusable tween per property, so each pointer
event retargets it instead of creating new tweens.

#### Reduced motion

The media query does not match: no listeners, layers stay still.

#### Cost

GSAP core. One `getBoundingClientRect` per pointer event inside the scene.

### Image and video parallax

#### Use when

A full-width photograph or video band between sections should drift a
little slower than the page.

#### Avoid when

The image carries detail the visitor must see (the crop moves); product
screenshots with text.

#### Code

New to this skill: `.media-frame`.

The media is 20 percent taller than its frame; that extra height is the
travel room. The frame's aspect ratio is fixed, so nothing shifts while the
media loads.

```html
<figure class="media-frame">
  <img src="/img/band.avif" alt="[PLACEHOLDER: describe the photo]" width="1600" height="1080" loading="lazy">
</figure>
```

```css
.media-frame { position: relative; margin: 0; aspect-ratio: 16 / 9; overflow: clip; view-timeline: --media }
.media-frame > img, .media-frame > video {
  position: absolute; top: 0; left: 0;
  width: 100%; height: 120%; /* 20% taller than the frame: the travel room */
  object-fit: cover;
}
@supports (animation-timeline: view()) {
  @media (prefers-reduced-motion: no-preference) {
    .media-frame > img, .media-frame > video {
      animation: media-drift linear both;
      animation-timeline: --media;
      animation-range: cover;
    }
  }
}
/* 20% of the frame is 1/6 of the media's own height. */
@keyframes media-drift { from { transform: translateY(-16.667%) } to { transform: translateY(0) } }
```

The media must be absolutely positioned: a percentage height on an in-flow
child of an aspect-ratio box does not resolve, and the frame grows to the
image's height instead of cropping it.

For video, use the same frame and gate autoplay:

```html
<figure class="media-frame">
  <video src="/media/band.webm" muted loop playsinline preload="metadata" poster="/img/band-poster.avif" aria-hidden="true"></video>
</figure>
```

```js
import { watchVisibility, reducedMotion } from './motion-safety.js';

// Autoplay only while the video is on screen, the tab is visible, and motion is allowed.
// Reduced motion: the poster stays.
document.querySelectorAll('.media-frame > video').forEach((video) => {
  let visible = false;
  const update = () => {
    if (visible && !reducedMotion.matches) video.play().catch(() => {});
    else video.pause();
  };
  watchVisibility(video, (v) => { visible = v; update(); }, '0px');
  reducedMotion.addEventListener('change', update);
});
```

A video that carries information needs visible controls instead of this
decorative treatment.

#### Reduced motion

No drift (the animation is not declared), and the video never starts; it
pauses if the preference turns on while it plays. It also pauses when it
scrolls out of view or the tab is hidden (`watchVisibility` from
`safety.md`). Tested: playing in view, paused on a hidden tab, playing again
when shown, paused off screen, paused when reduced motion turned on.

#### Cost

No JavaScript for the image. In Firefox the image is shown cropped and still.
Video costs its download; `preload="metadata"` defers most of it until play.
