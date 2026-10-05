# Safety rails

## Contents

- Reduced motion: what to remove, what to keep
- Writing the reduced variant
- A site motion toggle
- Keeping motion from delaying the hero
- Hide before JavaScript, with a failsafe
- WebGL: detect, fall back, survive context loss
- Error boundary for React canvases
- Pausing off-screen work
- Frame and main-thread budgets
- Self-hosted assets
- 3D failures this file prevents
- How to verify

## Reduced motion: what to remove, what to keep

WCAG 2.3.3 (Animation from Interactions, Level AAA) names parallax
scrolling, unnecessary movement during scroll, and non-essential transitions
as vestibular triggers. web.dev's guidance is to remove decorative effects
and keep feedback, loading indicators, and essential motion. So: reduce, do
not strip everything. When `direction.md` states what reduced motion gets,
its statement wins over this table; the table is the default.

| Motion | Under `prefers-reduced-motion: reduce` |
|---|---|
| Parallax (scroll or pointer) | Remove |
| Scrubbed scroll animation, pinned sequences | Remove the scrub; show the end state or still steps |
| Autoplay loops, ambient shaders, particles, video | Do not start; show one still frame or the poster |
| A 3D scene | Load it only if it is still and moves only under the visitor's hand; auto-rotation, scroll-driven explosion, and intro settles become stills. Loading no scene and showing the posters is the simplest correct choice (`webgl-3d.md`, "Before any scene") |
| Smooth scrolling | Off (Lenis does this by default) |
| Large travel in entrances | Replace with a short opacity fade (150 to 250 ms) or nothing |
| Hover, press, focus, toggle feedback | Keep, unless the direction says otherwise (removing a colour transition is acceptable, not required) |
| Direct manipulation (drag to rotate, comparison slider) | Keep, without inertia or glide |
| State changes (tabs, storytelling steps) | Keep, switch instantly |

The tell to avoid: one global rule that sets every animation and transition
to a near-zero duration. It deletes feedback along with decoration, breaks
scripts that wait for `transitionend`, and shows that nobody decided what
each animation is for. The tell detector of `landing-skills:landing-review`
flags it as `reduced-motion-kill-switch`.

## Writing the reduced variant

**CSS:** declare motion inside `no-preference`, so the static page is the
default and the reduced variant is opt-in where needed.

```css
@media (prefers-reduced-motion: no-preference) {
  .hero__lede { animation: hero-rise 450ms cubic-bezier(0.2, 0, 0, 1) 120ms both }
}
@media (prefers-reduced-motion: reduce) {
  .hero__lede { animation: hero-fade 200ms linear both }
}
```

**JavaScript:** read the preference live. A value read once at module load
misses a visitor who turns the setting on while the page is open.

```js
export const reducedMotion = matchMedia('(prefers-reduced-motion: reduce)');
reducedMotion.addEventListener('change', () => {
  // stop loops, clear transforms, or restart them
});
// In a frame loop, check reducedMotion.matches each frame instead of caching it.
```

**GSAP:** `gsap.matchMedia()` runs a setup function while a query matches
and reverts everything it created when the query stops matching.

```js
const mm = gsap.matchMedia();
mm.add({
  motion: '(prefers-reduced-motion: no-preference)',
  reduce: '(prefers-reduced-motion: reduce)',
}, (context) => {
  if (context.conditions.reduce) { /* set end states */ return; }
  /* create tweens and ScrollTriggers */
});
```

Every recipe in this skill has its reduced variant in its `Reduced motion`
section. Every 3D loop in `webgl-3d.md` checks `reducedMotion.matches` per
frame.

## A site motion toggle

WCAG 2.3.3 also accepts a control on the site that turns motion off. It
helps visitors who have not set the system preference. This object has the
same `.matches` and `change` event as the `matchMedia` result, so it can
replace `reducedMotion` in the helpers below without other changes.

```html
<head>
  <script>
    // Apply a stored choice before first paint.
    try { if (localStorage.getItem('motion') === 'reduce') document.documentElement.dataset.motion = 'reduce'; } catch {}
  </script>
</head>
...
<button type="button" class="motion-toggle" aria-pressed="false">Reduce motion</button>
```

```css
@media (prefers-reduced-motion: no-preference) {
  :root:not([data-motion="reduce"]) .drifter { animation: drift 2s ease-in-out infinite alternate }
}
```

```js
class MotionPreference extends EventTarget {
  #mq = matchMedia('(prefers-reduced-motion: reduce)');
  constructor() { super(); this.#mq.addEventListener('change', () => this.dispatchEvent(new Event('change'))); }
  get matches() { return this.#mq.matches || document.documentElement.dataset.motion === 'reduce'; }
  set(reduce) {
    if (reduce) document.documentElement.dataset.motion = 'reduce';
    else delete document.documentElement.dataset.motion;
    try { localStorage.setItem('motion', reduce ? 'reduce' : 'auto'); } catch {}
    this.dispatchEvent(new Event('change'));
  }
}
export const reducedMotion = new MotionPreference();

const button = document.querySelector('.motion-toggle');
const sync = () => button.setAttribute('aria-pressed', String(reducedMotion.matches));
button.addEventListener('click', () => reducedMotion.set(!reducedMotion.matches));
reducedMotion.addEventListener('change', sync);
sync();
```

CSS rules then need the `:root:not([data-motion="reduce"])` guard as shown.
Use the toggle when the page has a lot of motion (a 3D scene, scrubbed
sequences); a page with a hero entrance and feedback states does not need it.

## Keeping motion from delaying the hero

- **Opacity 0 is invisible to LCP.** Elements at opacity 0 are not Largest
  Contentful Paint candidates, so the hero image or headline only counts once
  it shows. A one-second fade-in from zero adds up to a second to LCP. Move
  the headline with `transform` and leave its opacity alone.
- **A canvas is not an LCP candidate.** The LCP element list covers images,
  video posters, background images, and text blocks. A 3D hero with only a
  canvas has an empty hero until the scene draws. Put a poster image in the
  same box; it becomes the LCP element and the canvas replaces it later.
- **Load 3D after the load event.** Feature-check, then wait for `load`
  (hero image and fonts done), then `import()` the scene module. The `afterLoad`
  helper below does this.
- **Keep three.js out of the entry bundle.** A dynamic `import()` puts it in
  its own chunk. In a test build of the React island in `webgl-3d.md`
  (Vite 8, React 19, R3F 9.8.1, drei 10.7.9), the entry chunk was 1.9 kB
  gzipped with no three.js in it; React DOM (65 kB gzipped) and the scene
  chunk (264 kB gzipped, 971 kB raw) loaded after `load`.
- **The size warning on the lazy scene chunk is expected.** Vite still warns
  that a chunk is over 500 kB: that is the scene chunk, which three.js alone
  makes large. Do not raise `build.chunkSizeWarningLimit` to silence it. The
  check is a different one: the entry chunk named in the built `index.html`
  contains no three.js (search it for `WebGLRenderer`), and no
  `modulepreload` link in that HTML points at the scene chunk.
- **Reserve space.** Give the canvas host and poster a fixed size or aspect
  ratio, and image elements `width` and `height`, so nothing shifts.

## Hide before JavaScript, with a failsafe

When an entrance must start from hidden (a split-text reveal), hide only when
motion is allowed, and unhide after a deadline in case the script never
runs. Put this inline in `<head>`, before the stylesheet that uses the class.

```html
<script>
  if (matchMedia('(prefers-reduced-motion: no-preference)').matches) {
    document.documentElement.classList.add('motion-pending');
    // Failsafe: if the animation script never runs, show the content anyway.
    setTimeout(() => document.documentElement.classList.remove('motion-pending'), 2500);
  }
</script>
<style>
  .motion-pending .hero h1 { visibility: hidden }
</style>
```

The animation script sets the element visible when it starts. If the module
fails to load, the content appears at 2.5 s. With JavaScript disabled, the
class is never added. Tested by removing the animation script: the headline
became visible at the deadline.

## WebGL: detect, fall back, survive context loss

Shared helpers used by every recipe in `webgl-3d.md`. Save as
`motion-safety.js`.

```js
// motion-safety.js: shared guards for every canvas and loop on the page.
export const reducedMotion = matchMedia('(prefers-reduced-motion: reduce)');

// True when a WebGL 2 context can be created. Run it before downloading any 3D code.
export function webglOk() {
  try {
    const canvas = document.createElement('canvas');
    return !!canvas.getContext('webgl2', { failIfMajorPerformanceCaveat: true });
  } catch {
    return false;
  }
}

// Resolves after the load event (hero image, fonts, critical CSS are in) and one more frame.
export function afterLoad() {
  return new Promise((resolve) => {
    const go = () => requestAnimationFrame(() => resolve());
    if (document.readyState === 'complete') go();
    else addEventListener('load', go, { once: true });
  });
}

// Calls onChange(true) while el is near the viewport and the tab is visible, onChange(false) otherwise.
export function watchVisibility(el, onChange, rootMargin = '100px') {
  let inView = false;
  const emit = () => onChange(inView && !document.hidden);
  const io = new IntersectionObserver(([entry]) => { inView = entry.isIntersecting; emit(); }, { rootMargin });
  io.observe(el);
  document.addEventListener('visibilitychange', emit);
  return () => { io.disconnect(); document.removeEventListener('visibilitychange', emit); };
}
```

`failIfMajorPerformanceCaveat: true` asks the browser to refuse a context
when it would be much slower than native, such as software rendering; the
poster is the better experience there. (The option's behaviour comes from
general knowledge, not a fetched source; in our headless test with a
software renderer, the context was still granted.)

The mount pattern every scene uses:

```js
import { webglOk, afterLoad } from './motion-safety.js';

if (webglOk()) {
  afterLoad()
    .then(() => import('./product-scene.js'))
    .then(({ mountProductScene }) => mountProductScene({ /* elements */ }))
    .catch((err) => console.warn('3D scene skipped, poster stays:', err));
}
```

Any failure (no WebGL, a module that fails to load, a model that 404s)
leaves the poster in place. The poster is a real image of the object, or a
labelled placeholder with the right aspect ratio while the real one is
missing, never an empty box.

**Context loss.** The GPU can drop a context (driver reset, too many
contexts, a backgrounded mobile tab). Listen on the canvas:

```js
canvas.addEventListener('webglcontextlost', (e) => { e.preventDefault(); stage.classList.remove('is-live'); });
canvas.addEventListener('webglcontextrestored', () => { dirty = true; });
```

`preventDefault()` tells the browser you want the context back. Removing
`is-live` shows the poster again until the next frame is drawn. Three.js
restores its own GPU state on `webglcontextrestored`. Tested with the
`WEBGL_lose_context` extension: the poster returned on loss and the scene
redrew on restore.

## Error boundary for React canvases

A React Three Fiber `<Canvas>` that cannot create a context throws during
commit. Without an error boundary, React unmounts the whole tree, and the
hero text and call to action disappear with the scene. Wrap the scene (the
island in `webgl-3d.md` does this inside its own chunk):

```jsx
import { Component } from 'react';

class SceneBoundary extends Component {
  state = { failed: false };
  static getDerivedStateFromError() { return { failed: true }; }
  componentDidCatch(error) { console.warn('3D scene failed, poster stays:', error); }
  render() { return this.state.failed ? null : this.props.children; }
}
```

Keep `webglOk()` as well: the boundary catches the failure, the check avoids
downloading the 3D chunk at all. Tested with WebGL disabled and the check
bypassed: the boundary caught the renderer error and the page kept its
heading and poster.

## Pausing off-screen work

- Canvases: start the render loop only while `watchVisibility` reports
  `true`. In three.js, `renderer.setAnimationLoop(visible ? tick : null)`. In
  React Three Fiber, use `frameloop="demand"` and call `invalidate()` only
  while visible, from input, scroll, and while a glide settles.
- Render on demand: if nothing changed since the last frame (no scroll
  progress, no drag, no animation), skip `renderer.render`. A static scene
  then costs nothing per frame.
- Video: play while `watchVisibility` reports `true`, pause otherwise.
- Hidden tabs: browsers stop `requestAnimationFrame` in background tabs;
  `watchVisibility` also stops loops on `visibilitychange` so timers and
  video stop too.

To check, count draw calls in the browser console:

```js
window.__draws = 0;
for (const C of [WebGLRenderingContext, WebGL2RenderingContext]) for (const fn of ['drawArrays', 'drawElements']) {
  const o = C.prototype[fn];
  C.prototype[fn] = function (...a) { window.__draws++; return o.apply(this, a); };
}
// Run before the scene starts (in a test, as an init script), scroll the canvas out of view,
// read window.__draws twice a second apart: the number should not change.
```

## Frame and main-thread budgets

- **Frame:** 16.7 ms per frame at 60 Hz, for everything: your scripts,
  style, layout, paint, and the GPU. A per-frame JavaScript callback should
  take a small fraction of that.
- **Core Web Vitals** (75th percentile): LCP good at 2.5 s or less, poor
  above 4.0 s; INP good at 200 ms or less, poor above 500 ms; CLS good at 0.1
  or less, poor above 0.25.
- **Device pixel ratio:** cap it at 2 for 3D scenes and lower (1.5) for
  full-screen shaders and particles. Pixel count grows with the square of
  the ratio.
- **3D working targets** (heuristics, no authoritative source): hero model
  about 1 to 2 MB over the wire, textures 1024 to 2048 px, one scene under
  100,000 to 150,000 triangles on mobile. Treat them as a starting point and
  measure on a mid-range phone.
- **Long tasks:** SplitText on large copy, shader compilation, and GLB
  decoding all run on the main thread or block the GPU. Do them after `load`,
  never during the hero's first second.

## Self-hosted assets

Fetch nothing at runtime from a third-party host: models, Draco and Basis
decoders, HDR environment maps, fonts, textures. A blocked or slow CDN
leaves the scene unlit or stalled, and every visitor's request goes to a host
you do not control. Two defaults to override:

- drei's `useGLTF` uses a Draco decoder path on a Google CDN unless you pass
  a path: `useGLTF(url, '/draco/')`.
- drei's `<Environment preset="...">` downloads an HDR file from a
  third-party host. Use `<Environment>` with `<Lightformer>` children (built
  in the scene, no download), `files="/env/studio.hdr"` from your origin, or
  three.js's `RoomEnvironment` (generated locally).

Copy the decoders from `node_modules/three/examples/jsm/libs/draco/gltf/` and
`node_modules/three/examples/jsm/libs/basis/` into the site's public folder.

## 3D failures this file prevents

Each of these appeared in an unguided build of a 3D product page:

| Failure | Prevention |
|---|---|
| No WebGL check and no error boundary: a failed canvas unmounted the whole React tree, headline and call to action included | `webglOk()` before import, `SceneBoundary` inside the scene chunk, and the headline and call to action written in static HTML that React never owns (the island pattern) |
| No poster: the hero was an empty colour until three.js loaded | Poster image in the stage, canvas fades in on first frame |
| One large JavaScript chunk with three.js, loaded before the hero painted, the bundler's size warning raised to hide it | `afterLoad()` then `import()`; check that the entry chunk has no three.js |
| Two canvases mounted at once for two views of the same object | One sticky canvas spanning both moments |
| Scroll progress kept in React state: the whole app re-rendered on every scroll event and every frame | Read scroll position inside the frame loop from a ref; no state; `resize={{ scroll: false }}` on the R3F `<Canvas>`, whose default scroll tracking re-renders React while the canvas moves (measured: 10 commits in 12 wheel steps by default, 0 with the prop) |
| Reduced motion read once at load, and scroll-driven motion left untouched | `reducedMotion.matches` checked every frame; scrub quantised to still steps |
| Environment lighting fetched from a third-party CDN at runtime | Self-hosted or generated environment |
| Parallax via a scroll listener with layout reads per element, applied to the text column too | `parallax.md`: CSS view timeline or the shared loop, decorative layers only |
| Rotation by pointer only | Arrow keys on the focusable stage |

## How to verify

- Reduced motion: emulate it in the browser's rendering panel or with
  Playwright's `reducedMotion: 'reduce'`. Scroll the page; nothing should
  drift, scrub, or loop.
- WebGL off: launch Chromium with `--disable-webgl --disable-3d-apis`. The
  poster should show and the console should only show your "skipped" warning.
- JavaScript off: all copy and the conversion action are present.
- Network: throttle to a slow profile and confirm the hero text paints
  before any 3D request starts; confirm no 3D request goes to another host.
- Off-screen: the draw-call counter above stays flat when the canvas is out
  of view.
