# 3D and WebGL: objects and models

## Contents

- Before any scene
- Choosing the tool
- Recipes
- Rotatable object with a scroll-driven exploded view (Three.js)
- Rotatable object with a scroll-driven exploded view (React Three Fiber)
- One context for two separated stages (Three.js)
- Swap the primitive stand-in for a glTF model

Camera paths, shader backgrounds, particles, image distortion, and WebGPU
are in `webgl-effects.md`. Read "Before any scene" here first in either case,
then only the recipe you need.

## Before any scene

Read `safety.md` first. Every recipe here and in `webgl-effects.md` imports
its `motion-safety.js` (`webglOk`, `afterLoad`, `watchVisibility`,
`reducedMotion`) and follows the same contract:

1. The section works without the scene: text in the HTML, a poster image in
   the stage.
2. Check WebGL, wait for `load`, then `import()` the scene module.
3. One canvas per page where possible; render only while visible; skip the
   render when nothing changed.
4. Reduced motion: no autoplay, no scrub, no glide. Direct manipulation stays.
5. Text never lives inside the canvas. The canvas is `aria-hidden`; the
   focusable stage around it carries the label and keyboard controls.

**Reduced motion and the scene.** If the direction says what a
reduced-motion visitor gets, do that; its statement wins. Otherwise the
default: the scene may load under reduced motion only if it is still and
moves only under the visitor's hand. Auto-rotation, a scroll-driven
explosion, and intro settles are replaced by still states (the recipes here
snap to five still states). Loading no scene at all, and showing the
posters, is the simplest correct choice; add view buttons that swap posters
if the visitor should still see several angles. To do that, wrap the mount
in `if (!reducedMotion.matches) { ... }`.

The example object in these recipes is invented for this skill: a stacked
desk speaker with four layers (base, housing, driver, grille). Swap in your
own layers; the technique does not depend on the object.

**What to import, the asset pipeline, and how to make posters** are in
`webgl-assets.md`. Read its first section before the first build: a single
loader import adds decoder files to the output. Read "Making the poster"
there when no photograph of the object exists.

## Choosing the tool

| Need | Tool | Why |
|---|---|---|
| Depth that CSS 3D transforms can fake (a tilted card, a flipped panel) | CSS | No WebGL at all |
| One full-screen shader plane, a gradient field | OGL (about 34 kB gzipped) | Small; a few lines of setup |
| A 3D object, lighting, models, particles, a page that is not React | Three.js | Loaders, materials, the whole toolkit; tree-shakes |
| The same in a React app | React Three Fiber, as an island | Declarative scene in components; add drei helpers only for what three and R3F do not cover |
| Authored vector animation with states (an icon, an illustration) | Rive or dotLottie | See `libraries.md` |

## Recipes

### Rotatable object with a scroll-driven exploded view (Three.js)

#### Use when

The intent names a product the visitor can turn, and a view that separates
it into its layers as they scroll. No real model exists yet: the scene is
built from primitives and named so a glTF file can replace it later. One
sticky canvas serves both moments, the hero and the exploded view.

#### Avoid when

A photograph or a short video shows the object as well (a 3D scene is the
heaviest thing a landing page can load); the page is React (use the next
recipe, which reuses this markup); the object has no meaningful inside or sides.

#### Code

New to this skill: `.product-track`, `.product-stage`, `.canvas-host`, `.object-intro`, `.object-layers`, `.is-live`. From `landing-skills:landing-build`, used unchanged: `.hero`, `.hero__title`, `.hero__lede` (placed inside `.object-intro`, unchanged).

`object-layers.js`: the layer list drives both the primitives and the
explode offsets. With a real model, these `id`s become node names.

```js
// An invented product for this example: a stacked desk speaker.
// Layers bottom to top. `lift` is the exploded offset in scene units at progress 1.
export const LAYERS = [
  { id: 'base',    shape: 'box',      size: [3, 0.3, 3],        y: 0,    lift: 0,   color: '#3b3f45', metalness: 0.6 },
  { id: 'housing', shape: 'cylinder', size: [1.3, 1.3, 1.6, 48], y: 0.95, lift: 0.8, color: '#b9b2a6', metalness: 0.1 },
  { id: 'driver',  shape: 'cylinder', size: [0.9, 0.6, 0.4, 48], y: 1.4,  lift: 1.9, color: '#202226', metalness: 0.3 },
  { id: 'grille',  shape: 'cylinder', size: [1.35, 1.35, 0.1, 48], y: 1.8, lift: 3,   color: '#8a6d4e', metalness: 0.8 },
];
```

`product-scene.js`, loaded only after the checks:

```js
// product-scene.js: loaded with import() only after webglOk() and afterLoad().
import * as THREE from 'three';
import { RoomEnvironment } from 'three/examples/jsm/environments/RoomEnvironment.js';
import { watchVisibility, reducedMotion } from './motion-safety.js';
import { LAYERS } from './object-layers.js';

// Stand-in built from primitives. Each layer is a named child so a glTF with the same node names drops in.
export function buildPrimitive() {
  const group = new THREE.Group();
  for (const l of LAYERS) {
    const geometry = l.shape === 'box' ? new THREE.BoxGeometry(...l.size) : new THREE.CylinderGeometry(...l.size);
    const mesh = new THREE.Mesh(
      geometry,
      new THREE.MeshStandardMaterial({ color: l.color, metalness: l.metalness, roughness: 0.4 }),
    );
    mesh.name = l.id;
    mesh.position.y = l.y;
    group.add(mesh);
  }
  return group;
}

export async function mountProductScene({ stage, canvasHost, explodeSection, loadModel = async () => buildPrimitive() }) {
  const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true, powerPreference: 'high-performance' });
  renderer.setPixelRatio(Math.min(devicePixelRatio, 2)); // cap DPR: 3x screens cost 2.25x the pixels of 2x
  let model;
  try {
    model = await loadModel(renderer);
  } catch (err) {
    renderer.dispose();
    throw err; // the caller keeps the poster
  }
  const canvas = renderer.domElement;
  canvas.setAttribute('aria-hidden', 'true');
  canvasHost.append(canvas);

  const scene = new THREE.Scene();
  const pmrem = new THREE.PMREMGenerator(renderer);
  scene.environment = pmrem.fromScene(new RoomEnvironment(), 0.04).texture; // built locally, no HDR download
  const camera = new THREE.PerspectiveCamera(30, 1, 0.1, 200);
  let dirty = true; // set whenever something visible changed; the loop renders only then

  const pivot = new THREE.Group();
  pivot.rotation.set(0.35, -0.5, 0);
  pivot.add(model);
  scene.add(pivot);
  const parts = LAYERS.map((l) => [model.getObjectByName(l.id), l]).filter(([obj]) => obj);

  // Frame the model at its fully exploded height, so nothing leaves the frame mid-scroll.
  for (const [obj, l] of parts) obj.position.y = l.y + l.lift;
  const sphere = new THREE.Box3().setFromObject(pivot).getBoundingSphere(new THREE.Sphere());
  for (const [obj, l] of parts) obj.position.y = l.y;
  const fitCamera = () => {
    const vFov = THREE.MathUtils.degToRad(camera.fov);
    const hFov = 2 * Math.atan(Math.tan(vFov / 2) * camera.aspect);
    const distance = sphere.radius / Math.sin(Math.min(vFov, hFov) / 2);
    camera.position.copy(sphere.center).add(new THREE.Vector3(0, 0.35, 1).normalize().multiplyScalar(distance));
    camera.lookAt(sphere.center);
  };

  // Size from the host element, not the window.
  const resize = () => {
    const { width, height } = canvasHost.getBoundingClientRect();
    renderer.setSize(width, height, false);
    camera.aspect = width / height;
    camera.updateProjectionMatrix();
    fitCamera();
    dirty = true;
  };
  const ro = new ResizeObserver(resize);
  ro.observe(canvasHost);

  // Rotate by horizontal drag; vertical swipes still scroll the page (touch-action: pan-y in CSS).
  let yaw = -0.5, targetYaw = -0.5, dragX = null;
  const onDown = (e) => { dragX = e.clientX; stage.setPointerCapture(e.pointerId); };
  const onMove = (e) => {
    if (dragX === null) return;
    targetYaw += (e.clientX - dragX) * 0.01;
    dragX = e.clientX;
    dirty = true;
  };
  const onUp = () => { dragX = null; };
  const onKey = (e) => {
    const step = { ArrowLeft: -0.3, ArrowRight: 0.3 }[e.key];
    if (step === undefined) return;
    e.preventDefault();
    targetYaw += step;
    dirty = true;
  };
  stage.addEventListener('pointerdown', onDown);
  stage.addEventListener('pointermove', onMove);
  stage.addEventListener('pointerup', onUp);
  stage.addEventListener('pointercancel', onUp);
  stage.addEventListener('keydown', onKey);

  // Explode progress, read from layout once per frame. No scroll listener, no framework state.
  let progress = -1;
  const readProgress = () => {
    if (!explodeSection) return 0;
    const r = explodeSection.getBoundingClientRect();
    const p = Math.min(1, Math.max(0, -r.top / (r.height - innerHeight)));
    return reducedMotion.matches ? Math.round(p * 4) / 4 : p; // reduced: five still states (0, 0.25 ... 1), no scrub
  };

  const tick = () => {
    const p = readProgress();
    if (p !== progress) { progress = p; dirty = true; }
    const ease = reducedMotion.matches ? 1 : 0.15; // reduced: no glide after the drag
    if (Math.abs(targetYaw - yaw) > 0.0005) { yaw += (targetYaw - yaw) * ease; dirty = true; }
    if (!dirty) return; // nothing changed: skip the GPU work
    pivot.rotation.y = yaw;
    const eased = 1 - Math.pow(1 - progress, 2);
    for (const [obj, l] of parts) obj.position.y = l.y + l.lift * eased;
    renderer.render(scene, camera);
    dirty = false;
    stage.classList.add('is-live'); // first frame drawn: now hide the poster
  };

  // Run the loop only while the stage is on screen and the tab is visible.
  const stopWatching = watchVisibility(stage, (visible) => renderer.setAnimationLoop(visible ? tick : null));

  // Context loss: fall back to the poster, come back when the browser restores the context.
  const onLost = (e) => { e.preventDefault(); stage.classList.remove('is-live'); };
  const onRestored = () => { dirty = true; };
  canvas.addEventListener('webglcontextlost', onLost);
  canvas.addEventListener('webglcontextrestored', onRestored);

  return function unmount() {
    stopWatching();
    renderer.setAnimationLoop(null);
    ro.disconnect();
    for (const [type, fn] of [['pointerdown', onDown], ['pointermove', onMove], ['pointerup', onUp], ['pointercancel', onUp], ['keydown', onKey]]) stage.removeEventListener(type, fn);
    scene.traverse((o) => { o.geometry?.dispose(); o.material?.dispose(); });
    scene.environment.dispose();
    pmrem.dispose();
    renderer.dispose();
    canvas.remove();
  };
}
```

Markup: the stage holds the poster and the canvas host; the hero and the
exploded section are normal HTML beside it. The layer list is real text, so
screen readers and no-JS visitors get every layer.

The stage is focusable and takes arrow keys, so it is a `group` with
`aria-roledescription="3D viewer"`, not an `img` (an image role tells
assistive technology there is nothing to operate). A screen reader announces
it as "3D viewer" with the label: "Model of the speaker. Drag or use the left
and right arrow keys to rotate." The layers themselves are read from the
list beside it.

```html
<div class="product-track">
  <div class="product-stage" data-moment="object-viewer" tabindex="0" role="group" aria-roledescription="3D viewer"
       aria-label="Model of the speaker. Drag or use the left and right arrow keys to rotate.">
    <img src="/img/object-poster.avif" alt="" width="1200" height="800">
    <div class="canvas-host"></div>
  </div>
  <div class="object-intro">
    <div class="hero"><!-- the build's hero, unchanged -->
      <h1 class="hero__title">[Product name]</h1>
      <p class="hero__lede">[Lead sentence]</p>
    </div>
  </div>
  <section class="object-layers" data-moment="exploded-view" aria-labelledby="layers-title">
    <h2 id="layers-title">Four layers, top to bottom</h2>
    <ol>
      <li>Grille</li><li>Driver</li><li>Housing</li><li>Base</li>
    </ol>
  </section>
</div>
```

```css
.product-track { display: grid; grid-template-columns: minmax(0, 1fr) minmax(0, 1fr) }
.product-stage { grid-column: 2; grid-row: 1 / span 2; position: sticky; top: 0; height: 100vh; display: grid;
  touch-action: pan-y; cursor: grab }
.product-stage:focus-visible { outline: 2px solid currentColor; outline-offset: -6px }
.product-stage > * { grid-area: 1 / 1 }
.product-stage img { width: 100%; height: 100%; object-fit: contain }
.product-stage .canvas-host { position: relative; opacity: 0; transition: opacity 300ms }
.product-stage .canvas-host canvas { position: absolute; inset: 0; width: 100%; height: 100% }
.product-stage.is-live .canvas-host { opacity: 1 }
.product-stage.is-live img { visibility: hidden }
.object-intro { grid-column: 1; grid-row: 1; min-height: 100vh; display: grid; align-content: center }
.object-layers { grid-column: 1; grid-row: 2 }
.object-layers li { min-height: 45vh }
@media (max-width: 48rem) {
  .product-track { grid-template-columns: 1fr }
  /* Small screens: the stage sticks to the top half and the text scrolls beneath it, never over the model. */
  .product-stage { grid-column: 1; grid-row: 1; height: 50vh; z-index: 1; background: var(--color-bg) }
  .object-intro, .object-layers { grid-column: 1 }
  .object-intro { grid-row: 2; min-height: 50vh } .object-layers { grid-row: 3 }
}
```

Mount:

```js
import { webglOk, afterLoad } from './motion-safety.js';

const stage = document.querySelector('.product-stage');
if (webglOk()) {
  afterLoad()
    .then(() => import('./product-scene.js'))
    .then(({ mountProductScene }) => mountProductScene({
      stage,
      canvasHost: stage.querySelector('.canvas-host'),
      explodeSection: document.querySelector('.object-layers'),
    }))
    .catch((err) => console.warn('3D scene skipped, poster stays:', err));
}
```

How it meets the contract: rotation is horizontal drag on the stage plus
the left and right arrow keys, and `touch-action: pan-y` keeps vertical
swipes scrolling the page (a full-screen orbit control that captures touch
traps phone users in the canvas). Explode progress is read from the
section's position inside the frame loop, with no scroll listener. The loop
renders only when progress or rotation changed, and stops when the stage
leaves the viewport. The environment light is generated with
`RoomEnvironment`, so no HDR file is downloaded. Tested in Chromium: drag and
arrow keys rotate, scroll scrubs the explode, zero draw calls while idle or
off-screen, no third-party requests, poster returns on context loss.

#### Reduced motion

Explode progress snaps to five still states (0, 0.25, 0.5, 0.75, 1) instead
of scrubbing; rotation
follows the drag or key press with no glide. There is no autoplay spin to
remove. If you add an idle spin, start it only under `no-preference` and stop
it on the first interaction.

#### Cost

three.js is about 185 kB gzipped as a whole module before tree-shaking;
this recipe imports the core and `RoomEnvironment`. Loaded after `load`, so
the hero's text and poster are not delayed. GPU work only on frames where
something changed. DPR capped at 2.

Scroll runway: the exploded view takes the height of the layer list (four
items at 45vh, about 1.8 screens) on top of the hero's screen. Judgement:
about half a screen to one screen per layer the visitor must notice, and the
whole sequence done within two to three screens. A full-page screenshot shows
this runway as a long band with the object only at the top, because the
canvas is sticky; that is expected. `landing-skills:landing-review` captures
such sections with a full-page capture first and switches to viewport captures
(top, then several scroll positions) only if that fails or is cut off.

#### Fallback

The poster image in the stage. It shows with JavaScript off, when
`webglOk()` is false, when a module or model fails to load, and during
context loss. With WebGL disabled in Chromium, no 3D module was downloaded
and the poster stayed.

### Rotatable object with a scroll-driven exploded view (React Three Fiber)

#### Use when

The same intent on a page built with Vite and React, or any React project
where the scene is easier to write as components. The scene is a React
island: the page itself is static HTML, and React mounts only into the
stage after `load`.

#### Avoid when

The page is not already React: do not add React for one scene, use the
Three.js recipe. Do not render the whole page from React in a client-only
app: see "No JavaScript" below.

#### Code

New to this skill: the same as the Three.js recipe. From `landing-skills:landing-build`, used unchanged: the same as the Three.js recipe.

**No JavaScript.** A client-only React app renders into an empty
`<div id="root">`, so with JavaScript off, or before the bundle runs, the
page has no headline and no call to action. That fails rail 1 of this skill.
To pass, the hero and the rest of the copy must exist as HTML before any
script runs: either server rendering or prerendering (Next, Astro, a
prerender step), or, smallest, the island pattern below, which is also what
`landing-skills:landing-build` writes for Vite with React. `index.html` holds
the whole page; React renders only the canvas into the stage's
`.canvas-host`.

`index.html` uses the same stage markup and CSS as the Three.js recipe, and
loads one small script:

```html
<script type="module" src="/src/islands.jsx"></script>
...
<div class="product-track">
  <div class="product-stage" data-moment="object-viewer" tabindex="0" role="group" aria-roledescription="3D viewer"
       aria-label="Model of the speaker. Drag or use the left and right arrow keys to rotate.">
    <img src="/img/object-poster.avif" alt="" width="1200" height="800">
    <div class="canvas-host"></div>
  </div>
  <div class="object-intro">
    <div class="hero"><!-- the build's hero, unchanged -->
      <h1 class="hero__title">[Product name]</h1>
      <p class="hero__lede">[Lead sentence]</p>
    </div>
  </div>
  <section class="object-layers" data-moment="exploded-view" aria-labelledby="layers-title">
    <h2 id="layers-title">Four layers, top to bottom</h2>
    <ol>
      <li>Grille</li><li>Driver</li><li>Housing</li><li>Base</li>
    </ol>
  </section>
</div>
```

`islands.jsx`, the entry chunk, holds no React and no three.js:

```jsx
// islands.jsx: the only script in index.html. No React or three.js in this file's chunk.
import { webglOk, afterLoad } from './motion-safety.js';

const stage = document.querySelector('.product-stage');
if (stage && webglOk()) {
  afterLoad()
    .then(() => Promise.all([import('react-dom/client'), import('./Scene3D.jsx')]))
    .then(([{ createRoot }, { default: SceneIsland }]) => {
      createRoot(stage.querySelector('.canvas-host')).render(
        <SceneIsland
          stage={stage}
          explodeSection={document.querySelector('.object-layers')}
          onFirstFrame={() => stage.classList.add('is-live')}
        />,
      );
    })
    .catch((err) => console.warn('3D scene skipped, poster stays:', err));
}
```

`Scene3D.jsx`, its own chunk with React DOM, R3F, drei, and three.js:

```jsx
// Scene3D.jsx: React island for the stage. Loaded with import() after webglOk() and afterLoad().
import { Component, useEffect, useRef } from 'react';
import { Canvas, useFrame } from '@react-three/fiber';
import { PMREMGenerator } from 'three';
import { RoomEnvironment } from 'three/examples/jsm/environments/RoomEnvironment.js';
import { LAYERS } from './object-layers.js';
import { reducedMotion, watchVisibility } from './motion-safety.js';

// When the real model exists, add GltfModel.jsx (see "Swap the primitive stand-in") and render
// <GltfModel url="/models/object.glb" /> in place of <PrimitiveModel />. Until then, import no loader:
// a loader import alone puts its decoder files in the build.

class SceneBoundary extends Component {
  state = { failed: false };
  static getDerivedStateFromError() { return { failed: true }; }
  componentDidCatch(error) { console.warn('3D scene failed, poster stays:', error); }
  render() { return this.state.failed ? null : this.props.children; }
}

function PrimitiveModel() {
  return (
    <group>
      {LAYERS.map((l) => (
        <mesh key={l.id} name={l.id} position-y={l.y}>
          {l.shape === 'box' ? <boxGeometry args={l.size} /> : <cylinderGeometry args={l.size} />}
          <meshStandardMaterial color={l.color} metalness={l.metalness} roughness={0.4} />
        </mesh>
      ))}
    </group>
  );
}

// Reads scroll and drag from refs inside the frame loop. React never re-renders while scrolling.
function Rig({ explodeSection, input, onFirstFrame, children }) {
  const pivot = useRef(null);
  const yaw = useRef(input.current.targetYaw);
  const first = useRef(true);
  useFrame((state) => {
    const r = explodeSection.getBoundingClientRect();
    let p = Math.min(1, Math.max(0, -r.top / (r.height - innerHeight)));
    if (reducedMotion.matches) p = Math.round(p * 4) / 4; // five still states: 0, 0.25, 0.5, 0.75, 1
    const eased = 1 - Math.pow(1 - p, 2);
    for (const l of LAYERS) {
      const obj = pivot.current.getObjectByName(l.id);
      if (obj) obj.position.y = l.y + l.lift * eased;
    }
    const gap = input.current.targetYaw - yaw.current;
    yaw.current += reducedMotion.matches ? gap : gap * 0.15; // reduced: no glide after the drag
    pivot.current.rotation.y = yaw.current;
    if (Math.abs(input.current.targetYaw - yaw.current) > 0.0005) state.invalidate(); // still gliding: one more frame
    if (first.current) { first.current = false; onFirstFrame(); }
  });
  return <group ref={pivot} rotation-x={0.35}>{children}</group>;
}

function Scene({ stage, explodeSection, onFirstFrame }) {
  const input = useRef({ targetYaw: -0.5 });
  const invalidate = useRef(() => {});

  useEffect(() => {
    let visible = false;
    const redraw = () => { if (visible) invalidate.current(); };
    let dragX = null;
    const down = (e) => { dragX = e.clientX; stage.setPointerCapture(e.pointerId); };
    const move = (e) => {
      if (dragX === null) return;
      input.current.targetYaw += (e.clientX - dragX) * 0.01;
      dragX = e.clientX;
      redraw();
    };
    const up = () => { dragX = null; };
    const key = (e) => {
      const step = { ArrowLeft: -0.3, ArrowRight: 0.3 }[e.key];
      if (step === undefined) return;
      e.preventDefault();
      input.current.targetYaw += step;
      redraw();
    };
    const events = [['pointerdown', down], ['pointermove', move], ['pointerup', up], ['pointercancel', up], ['keydown', key]];
    events.forEach(([t, f]) => stage.addEventListener(t, f));
    addEventListener('scroll', redraw, { passive: true }); // asks for one frame; no React state involved
    const stopWatching = watchVisibility(stage, (v) => { visible = v; redraw(); });
    return () => {
      events.forEach(([t, f]) => stage.removeEventListener(t, f));
      removeEventListener('scroll', redraw);
      stopWatching();
    };
  }, [stage]);

  return (
    <Canvas
      frameloop="demand"            // draw only when invalidate() is called
      resize={{ scroll: false }}    // the default re-measures, and re-renders React, on every scroll
      dpr={[1, 2]}
      camera={{ fov: 30, position: [0, 4, 14] }}
      onCreated={(state) => {
        invalidate.current = state.invalidate;
        state.camera.lookAt(0, 1.5, 0);
        // Lighting generated on the GPU from a built-in room: no HDR download, no helper library.
        state.scene.environment = new PMREMGenerator(state.gl).fromScene(new RoomEnvironment(), 0.04).texture;
      }}
      aria-hidden="true"
    >
      <Rig explodeSection={explodeSection} input={input} onFirstFrame={onFirstFrame}>
        <PrimitiveModel />
      </Rig>
    </Canvas>
  );
}

export default function SceneIsland(props) {
  return <SceneBoundary><Scene {...props} /></SceneBoundary>;
}
```

What matters here:

- **No React renders while scrolling.** Scroll progress and rotation live in
  refs and are read in `useFrame`. `resize={{ scroll: false }}` turns off the
  `<Canvas>` default that re-measures the canvas on scroll and re-renders
  React each time its box moves. Measured with a React `Profiler` around the
  island: 12 wheel steps while the stage was moving gave 10 commits with the
  default and 0 with the prop; 0 during scroll while the stage was stuck.
- **Draws only on change.** `frameloop="demand"` renders a frame only when
  `invalidate()` is called: from drag, arrow keys, and scroll (while the
  stage is visible), and from `useFrame` while the glide is still settling.
  Measured: 0 draws per second while idle and after a glide settled, 0 while
  scrolling with the stage off screen.
- **Lights without downloads, without drei.** The environment is three's
  `RoomEnvironment`, generated on the GPU in `onCreated`. A drei `preset`
  would fetch an HDR file from a third-party host, and drei's helpers add to
  the chunk: replacing drei's `Environment` and `Lightformer` with
  `RoomEnvironment` took the scene chunk from 264 to 245 kB gzipped.
- **No loader until there is a model.** `Scene3D.jsx` imports no glTF loader
  while it shows the primitive stand-in; see the glTF swap recipe.

Tested in Chromium: drag and arrow keys rotate, scroll scrubs the explode, no
third-party requests, no errors. The console shows one warning from React
Three Fiber 9.8.1 itself with three 0.186: `THREE.Clock: This module has been
deprecated. Please use THREE.Timer instead.` It comes from the library, not
from this code, and does not affect the scene. (Headless test browsers using
a software renderer also log "GPU stall due to ReadPixels" driver messages;
those are from the test renderer.)

#### Reduced motion

`useFrame` checks `reducedMotion.matches` every frame: explode progress snaps
to five still states (0, 0.25, 0.5, 0.75, 1), and rotation follows the drag
or key press with no glide. Measured: a 60 px scroll inside a step changed
nothing; a large scroll moved to the next state.

#### Cost

In a Vite 8 production build: entry chunk 1.9 kB gzipped (no three.js),
React DOM 65 kB gzipped and the scene chunk 245 kB gzipped (917 kB raw),
both fetched after `load`. Vite warns that the scene chunk is over 500 kB;
that warning is expected for a lazy three.js chunk, and `safety.md` says how
to check the split instead of raising the limit. React Three Fiber 9.8.1
needs React 19 (peer range `>=19 <19.4`).

#### Fallback

JavaScript off: the static page with the poster (tested: headline, all
layer items, and the poster present). `webglOk()` false: neither React nor
the scene chunk is requested; the poster stays (tested with WebGL disabled).
Context creation fails anyway: `SceneBoundary` catches it inside the island
and the static page is untouched (tested with WebGL disabled and the check
bypassed).

### One context for two separated stages (Three.js)

#### Use when

The rotatable object and the exploded view sit far apart on the page (the
hero at the top, the exploded view several sections later), so one sticky
canvas cannot span both. One WebGL context and one scene module serve both
stages: the canvas moves into whichever stage is more in view.

#### Avoid when

The two moments are adjacent: one sticky canvas (the first recipe) is
simpler. The two stages show different scenes that share nothing: then two
contexts may be justified, but load the second only when its stage nears the
viewport and dispose the first.

#### Code

Code reuses `product-scene.js` and `object-layers.js` from the first recipe.

New to this skill: `.object-stage`, `.hero-stage`, `.explode-section`,
`.canvas-host`, `.is-live`. Each stage attaches to the `data-moment`
element the static page left for it.

```html
<div class="object-stage hero-stage" data-moment="hero-object" tabindex="0" role="group" aria-roledescription="3D viewer"
     aria-label="Model of the speaker. Drag or use the left and right arrow keys to rotate.">
  <img src="/img/object-poster.avif" alt="" width="1200" height="800"><div class="canvas-host"></div>
</div>
<!-- other sections of the page, any length -->
<section class="explode-section" data-moment="exploded-view">
  <div class="object-stage"><img src="/img/object-poster-exploded.avif" alt="" width="1200" height="800"><div class="canvas-host"></div></div>
</section>
```

```css
.object-stage { position: relative; display: grid; grid-template: minmax(0, 1fr) / minmax(0, 1fr); touch-action: pan-y }
.object-stage > * { grid-area: 1 / 1; min-width: 0 }
.object-stage img { width: 100%; height: 100%; object-fit: contain }
.object-stage .canvas-host { position: relative; opacity: 0 }
.object-stage .canvas-host canvas { position: absolute; inset: 0; width: 100%; height: 100% }
.object-stage.is-live .canvas-host { opacity: 1 }
.object-stage.is-live img { visibility: hidden }
.hero-stage { height: 80vh }
.explode-section { height: 250vh }
.explode-section .object-stage { position: sticky; top: 0; height: 100vh }
```

`two-stages.js` reuses `buildPrimitive` from `product-scene.js` and the
layer list:

```js
// two-stages.js: one WebGL context for two stages far apart on the page.
// The canvas moves into whichever stage is most in view; the stage it leaves shows its poster again.
import * as THREE from 'three';
import { RoomEnvironment } from 'three/examples/jsm/environments/RoomEnvironment.js';
import { reducedMotion } from './motion-safety.js';
import { buildPrimitive } from './product-scene.js';
import { LAYERS } from './object-layers.js';

export function mountTwoStages({ heroStage, explodeStage, explodeSection }) {
  const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
  renderer.setPixelRatio(Math.min(devicePixelRatio, 2));
  const canvas = renderer.domElement;
  canvas.setAttribute('aria-hidden', 'true');

  const scene = new THREE.Scene();
  scene.environment = new THREE.PMREMGenerator(renderer).fromScene(new RoomEnvironment(), 0.04).texture;
  const camera = new THREE.PerspectiveCamera(30, 1, 0.1, 100);
  camera.position.set(0, 5, 14);
  camera.lookAt(0, 1.5, 0);
  const pivot = new THREE.Group();
  pivot.rotation.set(0.35, -0.5, 0);
  const model = buildPrimitive();
  pivot.add(model);
  scene.add(pivot);
  const parts = LAYERS.map((l) => [model.getObjectByName(l.id), l]).filter(([o]) => o);

  let current = null; // the stage that holds the canvas
  let dirty = true;
  let targetYaw = -0.5, yaw = -0.5;

  const ro = new ResizeObserver(() => {
    if (!current) return;
    const host = current.querySelector('.canvas-host');
    renderer.setSize(host.clientWidth, host.clientHeight, false);
    camera.aspect = host.clientWidth / host.clientHeight;
    camera.updateProjectionMatrix();
    dirty = true;
  });

  function moveTo(stage) {
    if (stage === current) return;
    current?.classList.remove('is-live');           // poster shows again where we leave
    if (current) ro.unobserve(current.querySelector('.canvas-host'));
    current = stage;
    stage.querySelector('.canvas-host').append(canvas); // same context, new parent
    ro.observe(stage.querySelector('.canvas-host'));
    dirty = true;
  }

  const readProgress = () => {
    if (current !== explodeStage) return 0;          // the hero always shows the assembled object
    const r = explodeSection.getBoundingClientRect();
    const p = Math.min(1, Math.max(0, -r.top / (r.height - innerHeight)));
    return reducedMotion.matches ? Math.round(p * 4) / 4 : p;
  };
  let progress = -1;
  const tick = () => {
    const p = readProgress();
    if (p !== progress) { progress = p; dirty = true; }
    if (Math.abs(targetYaw - yaw) > 0.0005) { yaw += (targetYaw - yaw) * (reducedMotion.matches ? 1 : 0.15); dirty = true; }
    if (!dirty) return;
    pivot.rotation.y = yaw;
    const eased = 1 - Math.pow(1 - progress, 2);
    for (const [obj, l] of parts) obj.position.y = l.y + l.lift * eased;
    renderer.render(scene, camera);
    dirty = false;
    current.classList.add('is-live');
  };

  // Which stage is most in view decides where the canvas lives. Neither in view: no loop at all.
  const ratios = new Map([[heroStage, 0], [explodeStage, 0]]);
  const decide = () => {
    const [best, ratio] = [...ratios].sort((a, b) => b[1] - a[1])[0];
    const running = ratio > 0 && !document.hidden;
    if (running) moveTo(best);
    renderer.setAnimationLoop(running ? tick : null);
  };
  const io = new IntersectionObserver((entries) => {
    for (const e of entries) ratios.set(e.target, e.isIntersecting ? e.intersectionRatio : 0);
    decide();
  }, { threshold: [0, 0.1, 0.25, 0.5, 0.75, 1] });
  io.observe(heroStage);
  io.observe(explodeStage);
  document.addEventListener('visibilitychange', decide);

  // Context loss: both stages fall back to their poster; the next draw makes the current one live again.
  canvas.addEventListener('webglcontextlost', (e) => { e.preventDefault(); heroStage.classList.remove('is-live'); explodeStage.classList.remove('is-live'); });
  canvas.addEventListener('webglcontextrestored', () => { dirty = true; });

  // Rotation on the hero stage only: drag and arrow keys.
  let dragX = null;
  heroStage.addEventListener('pointerdown', (e) => { dragX = e.clientX; heroStage.setPointerCapture(e.pointerId); });
  heroStage.addEventListener('pointermove', (e) => { if (dragX === null) return; targetYaw += (e.clientX - dragX) * 0.01; dragX = e.clientX; });
  heroStage.addEventListener('pointerup', () => { dragX = null; });
  heroStage.addEventListener('keydown', (e) => {
    const step = { ArrowLeft: -0.3, ArrowRight: 0.3 }[e.key];
    if (step === undefined) return;
    e.preventDefault();
    targetYaw += step;
  });
}
```

```js
import { webglOk, afterLoad } from './motion-safety.js';
if (webglOk()) {
  afterLoad()
    .then(() => import('./two-stages.js'))
    .then(({ mountTwoStages }) => mountTwoStages({
      heroStage: document.querySelector('[data-moment="hero-object"]'),
      explodeStage: document.querySelector('[data-moment="exploded-view"] .object-stage'),
      explodeSection: document.querySelector('[data-moment="exploded-view"]'),
    }))
    .catch((err) => console.warn('3D scene skipped, posters stay:', err));
}
```

How it behaves (tested in Chromium): at the top the canvas is in the hero
stage and the hero poster is hidden; between the two stages the loop is off
(0 draws); in the exploded section the canvas has moved there, the hero
stage shows its poster again, and scroll scrubs the explosion; back at the
top the canvas returns. One canvas and one WebGL context the whole time.
Re-parenting a canvas keeps its context, so nothing is rebuilt. With WebGL
disabled, both posters stay.

#### Reduced motion

The explosion snaps to five still states; a 60 px scroll inside a step
changed nothing in the test. Rotation follows input with no glide. Or skip
the scene under reduced motion and keep both posters (see "Before any
scene").

#### Cost

One context, one copy of the meshes, one environment map. The exploded
section here is 250vh tall: about 1.5 screens of scroll runway beyond its
own screen. See the first recipe's Cost for the runway judgement.

#### Fallback

Each stage keeps its own poster: the hero poster at rest, the exploded
stage's poster showing the layers apart. A stage the canvas has left shows
its poster again.

### Swap the primitive stand-in for a glTF model

#### Use when

The real model arrives. The scene, controls, explode, and fallbacks stay;
only the model source changes.

#### Avoid when

The model has not been optimised (see "Asset pipeline" in `webgl-assets.md`): an
unprocessed export can be many times the size of a compressed one.

#### Code

New to this skill: none.

Name the nodes in the model after the layer `id`s (`base`, `housing`,
`driver`, `grille`). The explode code finds them with `getObjectByName`, so
a model with the same names drops in. Check the names before wiring:

```bash
npx @gltf-transform/cli inspect public/models/object.glb
```

Three.js, for a Draco-compressed GLB:

```js
// load-model.js: import only when the page loads a Draco-compressed GLB.
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import { DRACOLoader } from 'three/examples/jsm/loaders/DRACOLoader.js';

// three 0.186 points DRACOLoader at its own decoder files with `new URL(..., import.meta.url)`,
// so Vite copies them into the build and serves them from your origin. No setDecoderPath needed.
export async function loadGlb(url) {
  const draco = new DRACOLoader();
  const loader = new GLTFLoader().setDRACOLoader(draco);
  try {
    return (await loader.loadAsync(url)).scene;
  } finally {
    draco.dispose();
  }
}
```

With a bundler (Vite here), three 0.186's `DRACOLoader` finds its decoder
through `new URL(..., import.meta.url)`, so the bundler copies the decoder
files into the build and they are served from your origin. Do not also copy
them into `public/` or set a CDN path. Without a bundler, copy
`node_modules/three/examples/jsm/libs/draco/` to your site and call
`draco.setDecoderPath('/draco/')`. Load a `KTX2Loader` only when the model
has KTX2 textures, and `MeshoptDecoder` only when it was compressed with
Meshopt; each adds files or bytes of its own.

Pass it to the scene:

```js
import { webglOk, afterLoad } from './motion-safety.js';

const stage = document.querySelector('.product-stage');
if (webglOk()) {
  afterLoad()
    .then(() => Promise.all([import('./product-scene.js'), import('./load-model.js')]))
    .then(([{ mountProductScene }, { loadGlb }]) => mountProductScene({
      loadModel: () => loadGlb('/models/object.glb'), // a file in public/models/
      stage,
      canvasHost: stage.querySelector('.canvas-host'),
      explodeSection: document.querySelector('.object-layers'),
    }))
    .catch((err) => console.warn('3D scene skipped, poster stays:', err));
}
```

React Three Fiber: add `GltfModel.jsx`, import it in `Scene3D.jsx`, and
render `<GltfModel url="/models/object.glb" />` in place of
`<PrimitiveModel />`:

```jsx
// GltfModel.jsx: add only when a Draco-compressed GLB exists.
import { useLoader } from '@react-three/fiber';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import { DRACOLoader } from 'three/examples/jsm/loaders/DRACOLoader.js';

// three 0.186: DRACOLoader's default decoder URLs are bundled from three itself, so they are served
// from your origin. Do not set a CDN path.
const withDraco = (loader) => loader.setDRACOLoader(new DRACOLoader());

export function GltfModel({ url }) {
  const { scene } = useLoader(GLTFLoader, url, withDraco);
  return <primitive object={scene} />;
}
```

drei's `useGLTF` would also work, but its default Draco path is a Google
CDN; three's own loader through R3F's `useLoader` avoids drei entirely.

Tested: a GLB exported from the primitive group, compressed with
`gltf-transform draco` and with `gltf-transform optimize --compress draco`,
loaded in both versions with node names intact. Requests went only to the
page's origin: the GLB, `draco_wasm_wrapper.js`, and `draco_decoder.wasm`.
A missing file (404) left the poster in place. In a production build served
from the output folder, the model loaded the same way.

#### Reduced motion

Unchanged from the scene recipes.

#### Cost

The model's bytes, plus the Draco decoder on first use: measured requests
were the 59 kB wrapper and the 286 kB WASM file. Importing `DRACOLoader`
also puts three more decoder variants in the build output that are never
requested (in our build, five files, 1.3 MB raw, of which 345 kB are used).
They cost deploy size, not page weight. Decoding runs in a worker but still
takes time on phones; load after `load`, as the mount does.

#### Fallback

If the model fails to load, `mountProductScene` disposes the renderer and
rethrows; the caller's `catch` leaves the poster. In React, the error reaches
`SceneBoundary` and the poster stays.
