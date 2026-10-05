# Libraries

## Contents

- When plain CSS is enough
- The libraries at a glance
- Per library: when to use, when not, licence, size
- Notes checked against the installed versions
- Rules for combining libraries

Versions and sizes are as of 2026-10-04: versions from npm, sizes
minified and gzipped from Bundlephobia for the whole package entry (real
builds tree-shake many of them smaller). Check the current version before
installing; these numbers drift.

## When plain CSS is enough

- Hover, focus, press, and a simple fade on load: CSS transitions and
  keyframes; `@starting-style` (Chrome 117, Firefox 129, Safari 17.5) for
  elements entering the DOM or switching from `display: none`.
- Scroll progress, reveal on view, simple parallax:
  `animation-timeline: scroll()` and `view()` behind `@supports`, with a
  static fallback (Chrome 115, Safari 26, not stable Firefox).
- Stacked and sticky sections: `position: sticky`.
- Height-auto reveals: `interpolate-size: allow-keywords` (Chromium 129 only,
  so treat it as an enhancement).
- Spring-like easing: `linear()` (Chrome 113, Firefox 112, Safari 17.2).
- Page-to-page fades and shared elements: View Transitions.

Reach for GSAP when you need sequencing across many elements, pinned and
scrubbed timelines, SplitText, or Draggable. Reach for Motion for React
layout animation, shared-element transitions, and gestures. Reach for OGL or
Three.js only for pixels CSS cannot produce.

## The libraries at a glance

| Library | Version | Licence | Size (min+gzip) | Best for |
|---|---|---|---|---|
| GSAP (core) | 3.15.0 | GSAP Standard "no charge" licence (custom, not open source) | 27.4 kB core; plugins are separate imports | Timelines, ScrollTrigger, SplitText, Draggable, orchestration |
| Motion (formerly Framer Motion) | 14.0.0 | MIT | 47.6 kB full `motion` entry | React component animation, layout and shared-layout, gestures, springs |
| Lenis | 1.3.26 | MIT | 5.5 kB | Smooth scroll over native scroll |
| Three.js | 0.186.1 | MIT | 184.9 kB whole module; tree-shakes | WebGL and WebGPU 3D, loaders, shaders |
| @react-three/fiber | 9.8.1 | MIT | 57.0 kB, excluding three | Three.js as React components |
| @react-three/drei | 10.7.9 | MIT | 521.5 kB main entry; import named helpers | R3F helpers: `useGLTF`, `Environment`, `Lightformer` |
| OGL | 1.0.11 | Unlicense | 34.2 kB | Small WebGL layer for shader planes, simple meshes, particles |
| Rive (`@rive-app/canvas`) | 2.44.0 | MIT | 59.0 kB (`canvas-lite` 51.6 kB); a separate WASM file may add to this (unverified) | Interactive vector animation with state machines |
| dotLottie (`@lottiefiles/dotlottie-web`) | 0.80.0 (pre-1.0) | MIT | 33.0 kB; a separate WASM file may add to this (unverified) | Lottie and `.lottie` playback, smaller files |
| lottie-web | 5.13.0 | MIT | 76.8 kB | Playing existing After Effects Lottie JSON |
| Theatre.js (`@theatre/core`) | 0.7.2 | core Apache-2.0; `@theatre/studio` AGPL-3.0-only | core 31.4 kB | Timeline-edited sequences with a visual editor |
| gltfjsx | 6.5.3 | MIT | CLI | glTF to an R3F component, `--transform` compression |
| @gltf-transform/cli | 4.5.1 | MIT | CLI | glTF inspection and optimisation |

## Per library: when to use, when not, licence, size

### GSAP

Use for a hero timeline with overlapping steps, any pinned or scrubbed scroll
sequence (ScrollTrigger), split-text reveals (SplitText), drag with inertia
(Draggable), and pointer followers (`gsap.quickTo`). `gsap.matchMedia()`
handles reduced motion and breakpoints, and reverts everything when a query
stops matching.

CSS is enough for single transitions, feedback states, and anything a
`view()` timeline can do.

Licence: GSAP is free for everyone, including commercial use, and every
plugin (ScrollTrigger, SplitText, MorphSVG and the rest) is included. The
licence is Webflow's own standard licence, effective 2025-04-30, not MIT and
not OSI open source. It prohibits use in tools that compete with Webflow's
visual animation builder, and states that AI-generated code is not a
prohibited use. Tell the user it is not MIT when you add it. The npm `next`
tag (3.0.0-beta.11) is old; it does not signal a version 4.

Size: 27.4 kB core plus each plugin imported.

### Motion

Use in React for animating components as they mount and unmount, layout and
shared-element transitions, drag with constraints, and springs. Its
`whileInView` uses a pooled IntersectionObserver.

CSS is enough for hover and simple entrances; outside React, GSAP or plain
CSS usually covers the need with less. Do not use `whileInView` to put the
same reveal on every section: that is the uniform-reveal tell.

Licence MIT. Size 47.6 kB for the full entry (the docs advertise a smaller
`animate` subset; its exact size was not verified).

### Lenis

Use only when the page has scroll-linked animation that looks stepped under
a mouse wheel. It keeps native scrolling, so sticky positioning, anchors,
and assistive technology work.

Not needed for pages without scroll-linked animation. It does not support
CSS scroll snap.

Licence MIT. 5.5 kB. Runs a continuous rAF loop; Safari caps it at 60 fps,
30 fps in low-power mode.

### Three.js

Use for any 3D object, product viewer, particles, or shader effect beyond a
single plane, on a page that is not React (or inside R3F on a React page).

Not for effects CSS 3D transforms can do, and not for a single shader plane
(OGL is a fifth of the size).

Licence MIT. 184.9 kB for the whole module; a real scene imports less. Load
it in its own chunk after `load`.

### React Three Fiber and drei

Use on React pages that need a 3D scene. drei supplies loaders and lighting
helpers; import only the helpers you use.

Not on non-React pages. Two drei defaults reach third-party hosts at
runtime: `useGLTF` without a decoder path, and `<Environment preset>`.
Override both (`safety.md`).

Licence MIT. R3F 57.0 kB plus three; drei's main entry is 521.5 kB before
tree-shaking. R3F 9.8.1 requires React 19 (`>=19 <19.4`) and three 0.156 or
newer. drei uses `three-stdlib` instead of three's examples folder.

### OGL

Use for one shader background or a small particle effect where Three.js
would be most of the bytes.

Not for models, PBR materials, or anything needing three.js loaders.

Licence Unlicense (public domain). 34.2 kB. Last published 2025-01-27, so
check its issue tracker before relying on it for something new.

### Rive

Use for interactive vector animation driven by a state machine: an
illustrated product diagram that reacts to hover, an animated icon with
states. The animation is authored in Rive's editor.

Not for one-off effects you could write in CSS, or when nobody will author
the `.riv` file.

Licence MIT. 59.0 kB (`canvas-lite` 51.6 kB); whether a WASM file adds to that, and by how much, is unverified.

### dotLottie and lottie-web

Use dotLottie for new work that plays Lottie animations; it reads `.lottie`
files, which are smaller. Use lottie-web only when an existing integration
depends on it.

Not when the animation is decorative filler; pause loops off screen and do
not autoplay under reduced motion.

Licence MIT. dotLottie 33.0 kB (any WASM file on top is unverified), still pre-1.0; lottie-web
76.8 kB, last release 2025-05-21.

### Theatre.js

Use when a designer needs to keyframe a complex sequence in a visual editor
and you will ship only `@theatre/core`.

Never ship `@theatre/studio` to production: it is AGPL-3.0-only, unlike
the Apache-2.0 core. Keep it a dev-only import. The last release was 2024-05-19; weigh that before adopting it.

### gltfjsx and glTF Transform

Build-time tools, not shipped. glTF Transform inspects and compresses models
(`webgl-assets.md`, "Asset pipeline"). gltfjsx turns a model into an R3F
component; its `--transform` option compresses the model too. gltfjsx's last
release was 2024-11-04.

## Notes checked against the installed versions

These were checked in the installed packages on 2026-10-04 while testing the
recipes:

- **SplitText (GSAP 3.15.0):** `SplitText.create(target, vars)`; options
  include `type`, `mask: "lines" | "words" | "chars"`, `autoSplit`,
  `onSplit`, and `aria: "auto" | "hidden" | "none"`. With the default
  `aria: "auto"` the heading receives an `aria-label` with its full text.
- **Lenis 1.3.26:** `respectReducedMotion` defaults to `true` (smoothing off
  and instant programmatic scrolls under reduced motion); `anchors: true`
  routes in-page links through Lenis; `autoRaf: true` runs its own loop when
  GSAP's ticker is not used.
- **drei 10.7.9:** `useGLTF(url, dracoPath)`; without the second argument,
  the Draco decoder path is a Google CDN. Environment presets load from a
  third-party host.
- **three 0.186.1:** Draco decoders in `examples/jsm/libs/draco/gltf/`, Basis
  transcoder in `examples/jsm/libs/basis/`; `WebGPURenderer` in
  `three/webgpu` falls back to WebGL 2.

## Rules for combining libraries

- One animation engine per job. GSAP and Motion together only when the page
  is React and needs Motion's layout animation alongside GSAP's scroll
  timelines.
- One 3D library per page.
- Every library added goes in your summary to the user with its licence,
  and GSAP's custom licence is named as such.
- Drop-in effect collections (reveal-on-scroll libraries, animated beam and
  shiny-text components, count-up components) ship defaults that are
  recognised as generated. Write the effect the intent names instead.
