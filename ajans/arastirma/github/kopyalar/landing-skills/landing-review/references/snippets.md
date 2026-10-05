# Snippets for the review passes

Scripts to run in the page with your browser tool's "evaluate" or "run
script" capability, and one shell loop. `SKILL.md` says when to run each.

## Contents

- Viewport check
- WebGL available in the tool
- Measurement script (visual pass)
- Frame-rate script (motion pass)
- WebGL off (motion pass)
- Bundle sizes (motion pass)

## Viewport check

Run before trusting any capture. Some browser tools cannot resize below a
minimum window width and silently give you a wider page than you asked for.

```js
() => ({ innerWidth: window.innerWidth, clientWidth: document.documentElement.clientWidth })
```

`innerWidth` must equal the width you set (360, 768, 1280 or 1440). If it
does not, use the tool's device or viewport emulation instead of a window
resize, and check again.

## WebGL available in the tool

Run once, before the motion pass and before reporting a blank canvas. Many
headless browsers have no GPU and may have no WebGL at all, so a 3D page
renders blank there for reasons that have nothing to do with the page.

```js
() => {
  const c = document.createElement('canvas');
  return { webgl2: !!c.getContext('webgl2'), webgl: !!document.createElement('canvas').getContext('webgl') };
}
```

Both `false` means the tool has no WebGL. This is a check of your tool, and
it is separate from the deliberate WebGL-off test below.

## Measurement script

Reports horizontal overflow, the first ten elements wider than the viewport,
the cumulative layout shift during load, and the number of canvases. Run it
at each of the four widths.

```js
async () => {
  const doc = document.documentElement;
  const overflow = doc.scrollWidth > doc.clientWidth;
  const wide = [...document.querySelectorAll('body *')]
    .filter((el) => el.getBoundingClientRect().right > doc.clientWidth + 1)
    .slice(0, 10)
    .map((el) => el.tagName.toLowerCase() + (typeof el.className === 'string' && el.className ? '.' + el.className.trim().split(/\s+/).join('.') : ''));
  const cls = await new Promise((resolve) => {
    let total = 0;
    new PerformanceObserver((list) => {
      for (const e of list.getEntries()) if (!e.hadRecentInput) total += e.value;
    }).observe({ type: 'layout-shift', buffered: true });
    setTimeout(() => resolve(Number(total.toFixed(3))), 3000);
  });
  return { overflow, wide, cls, canvases: document.querySelectorAll('canvas').length };
}
```

Layout shift entries exist in Chromium-based browsers only. A CLS of 0.1 or
less is good and above 0.25 is poor; between is worth a look at what moves.
Lab numbers are unreliable under software rendering or network throttling:
report a layout shift only when you can name its visible cause, such as an
image without dimensions, a late font swap, or content injected above the
fold.
An element in `wide` that the page clips on purpose, inside an
`overflow: hidden` frame, is not overflow; check before reporting.

## Frame-rate script

Scrolls the whole page over four seconds and counts frames and frames over
33 ms. Use the browser tool's performance trace instead when it has one.

```js
async () => new Promise((resolve) => {
  const doc = document.documentElement;
  const max = doc.scrollHeight - innerHeight;
  let frames = 0, slow = 0, last = performance.now();
  const start = last;
  function tick(now) {
    frames++;
    if (now - last > 33) slow++;
    last = now;
    scrollTo(0, max * Math.min(1, (now - start) / 4000));
    if (now - start < 4000) requestAnimationFrame(tick);
    else resolve({ fps: Math.round(frames / ((now - start) / 1000)), framesOver33ms: slow });
  }
  requestAnimationFrame(tick);
})
```

At 60 Hz each frame has 16.7 ms; a frame over 33 ms is a visible stutter.
Your machine is faster than a mid-range phone, so a clean result here is not
proof for phones. Under software rendering (no GPU) the numbers mean
nothing: do not report them, and judge scrolling from the source instead.

## WebGL off

Inject this before the page's own scripts run (an init script, in tools that
support one), then reload. Every request for a WebGL context returns
`null`, as it does on a device without WebGL.

```js
const original = HTMLCanvasElement.prototype.getContext;
HTMLCanvasElement.prototype.getContext = function (type, ...rest) {
  if (/webgl/i.test(type)) return null;
  return original.call(this, type, ...rest);
};
```

## Bundle sizes

Run in the project after the build. Prints each script's path, raw bytes
and gzipped bytes. Replace `dist/assets` with the stack's output folder.

```bash
for f in dist/assets/*.js; do printf '%s %s %s\n' "$f" "$(wc -c < "$f")" "$(gzip -c "$f" | wc -c)"; done
```
