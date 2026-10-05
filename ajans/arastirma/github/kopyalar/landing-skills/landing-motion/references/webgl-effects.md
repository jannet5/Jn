# 3D and WebGL: effects

## Contents

- Before you start
- Scroll-driven camera path
- Shader background (OGL)
- Particle field (Three.js)
- Image hover distortion (Three.js)
- WebGPU status

## Before you start

Read "Before any scene" in `webgl-3d.md` and `safety.md`; for imports,
output checks, and posters, see `webgl-assets.md`. These recipes use
the same `motion-safety.js` helpers and the same contract: the section is
complete without the effect, the effect loads after `load`, it draws only
while visible, and reduced motion leaves it still.

### Scroll-driven camera path

#### Use when

A guided tour of a scene: the camera moves between a few viewpoints as the
visitor scrolls through steps of text, each viewpoint showing what its step
describes.

#### Avoid when

The steps could be still images (cheaper and sharper); more than four or
five stops; the path would spin the camera (strong vestibular trigger).

#### Code

New to this skill: `.tour`, `.tour-stage`, `.tour-steps`, `.is-live`.

```js
// scroll-camera.js: the camera travels a fixed path as the visitor scrolls through one section.
import * as THREE from 'three';
import { reducedMotion, watchVisibility } from './motion-safety.js';

// Each stop is where the camera sits for one step of the text, and what it looks at.
const STOPS = [
  { at: [0, 2, 10], look: [0, 1, 0] },
  { at: [6, 3, 4],  look: [0, 1.5, 0] },
  { at: [0, 6, 2],  look: [0, 0.5, 0] },
];

export function mountScrollCamera({ host, section, scene }) {
  const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
  renderer.setPixelRatio(Math.min(devicePixelRatio, 2));
  renderer.domElement.setAttribute('aria-hidden', 'true');
  host.append(renderer.domElement);
  const camera = new THREE.PerspectiveCamera(35, 1, 0.1, 100);
  const path = new THREE.CatmullRomCurve3(STOPS.map((s) => new THREE.Vector3(...s.at)));
  const lookPath = new THREE.CatmullRomCurve3(STOPS.map((s) => new THREE.Vector3(...s.look)));
  const look = new THREE.Vector3();

  const ro = new ResizeObserver(() => {
    renderer.setSize(host.clientWidth, host.clientHeight, false);
    camera.aspect = host.clientWidth / host.clientHeight;
    camera.updateProjectionMatrix();
    last = -1;
  });
  ro.observe(host);

  let last = -1;
  const tick = () => {
    const r = section.getBoundingClientRect();
    let p = Math.min(1, Math.max(0, -r.top / (r.height - innerHeight)));
    if (reducedMotion.matches) p = Math.round(p * (STOPS.length - 1)) / (STOPS.length - 1); // cut between stops
    if (p === last) return; // camera has not moved: skip the render
    last = p;
    camera.position.copy(path.getPointAt(p));
    camera.lookAt(lookPath.getPointAt(p, look));
    renderer.render(scene, camera);
    host.classList.add('is-live');
  };
  const stopWatching = watchVisibility(host, (visible) => renderer.setAnimationLoop(visible ? tick : null));

  return () => { stopWatching(); renderer.setAnimationLoop(null); ro.disconnect(); renderer.dispose(); renderer.domElement.remove(); };
}
```

```html
<section class="tour">
  <div class="tour-stage"><img src="/img/tour-poster.avif" alt="" width="1200" height="800"></div>
  <ol class="tour-steps"><li>[Step one]</li><li>[Step two]</li><li>[Step three]</li></ol>
</section>
```

```css
.tour { display: grid; grid-template-columns: 1fr 1fr }
.tour-stage { position: sticky; top: 0; height: 100vh; grid-column: 2; grid-row: 1 }
.tour-stage img, .tour-stage canvas { position: absolute; inset: 0; width: 100%; height: 100% }
.tour-stage img { object-fit: cover }
.tour-stage.is-live img { visibility: hidden }
.tour-steps { grid-column: 1; grid-row: 1 }
.tour-steps li { min-height: 80vh }
```

```js
import { webglOk, afterLoad } from './motion-safety.js';
if (webglOk()) {
  afterLoad().then(async () => {
    const THREE = await import('three');
    const { mountScrollCamera } = await import('./scroll-camera.js');
    const scene = new THREE.Scene();
    scene.add(new THREE.HemisphereLight('#ffffff', '#444444', 3));
    for (let i = 0; i < 5; i++) {
      const m = new THREE.Mesh(new THREE.BoxGeometry(1, 1 + i * 0.4, 1), new THREE.MeshStandardMaterial({ color: '#a86' }));
      m.position.set(i * 1.5 - 3, 0.5, 0); scene.add(m);
    }
    mountScrollCamera({ host: document.querySelector('.tour-stage'), section: document.querySelector('.tour'), scene });
  }).catch((err) => console.warn('Camera tour skipped, poster stays:', err));
}
```

Replace the boxes with your scene (or the product model from the recipes
above). `CatmullRomCurve3` makes a smooth path through the stops;
`getPointAt` samples it by distance, so the speed is even.

#### Reduced motion

The camera cuts between the stops instead of travelling: progress is
rounded to the nearest stop.

#### Cost

three.js. Renders only when scroll progress changed, only while visible.

#### Fallback

The poster image in `.tour-stage`, with the steps as normal text.

### Shader background (OGL)

#### Use when

A section needs a slow, living colour field drawn from the brand palette,
and the direction names it as ambient texture.

#### Avoid when

A still gradient or texture says the same (it usually does); behind body
text, where movement competes with reading; more than one per page.

#### Code

New to this skill: `.bg-field`, `.is-live`.

```js
// shader-bg.js: a slow two-colour field behind a section. OGL keeps it small.
import { Renderer, Program, Mesh, Triangle, Color } from 'ogl';
import { reducedMotion, watchVisibility } from './motion-safety.js';

const vertex = /* glsl */ `
  attribute vec2 position;
  attribute vec2 uv;
  varying vec2 vUv;
  void main() { vUv = uv; gl_Position = vec4(position, 0.0, 1.0); }
`;
const fragment = /* glsl */ `
  precision mediump float;
  uniform float uTime;
  uniform vec3 uColorA;
  uniform vec3 uColorB;
  varying vec2 vUv;
  void main() {
    float wave = sin(vUv.x * 3.0 + uTime * 0.15) * 0.5 + sin(vUv.y * 4.0 - uTime * 0.1) * 0.5;
    float t = smoothstep(-0.8, 0.8, wave + (vUv.y - 0.5));
    gl_FragColor = vec4(mix(uColorA, uColorB, t), 1.0);
  }
`;

export function mountShaderBackground(host, { colorA, colorB }) {
  const renderer = new Renderer({ dpr: Math.min(devicePixelRatio, 1.5), alpha: false });
  const gl = renderer.gl;
  gl.canvas.setAttribute('aria-hidden', 'true');
  host.append(gl.canvas);
  const program = new Program(gl, {
    vertex, fragment,
    uniforms: { uTime: { value: 0 }, uColorA: { value: new Color(colorA) }, uColorB: { value: new Color(colorB) } },
  });
  const mesh = new Mesh(gl, { geometry: new Triangle(gl), program });

  const draw = () => renderer.render({ scene: mesh });
  const resize = () => { renderer.setSize(host.clientWidth, host.clientHeight); draw(); };
  const ro = new ResizeObserver(resize);
  ro.observe(host);

  let raf = 0;
  let visible = false; // last value from watchVisibility: in view and tab shown
  const loop = (t) => { program.uniforms.uTime.value = t / 1000; draw(); raf = requestAnimationFrame(loop); };
  const update = () => {
    cancelAnimationFrame(raf);
    if (visible && !reducedMotion.matches) raf = requestAnimationFrame(loop); // reduced: one still frame
  };
  const stopWatching = watchVisibility(host, (v) => { visible = v; update(); });
  const onMotionPref = update;
  reducedMotion.addEventListener('change', onMotionPref);
  host.classList.add('is-live');

  return () => {
    stopWatching();
    reducedMotion.removeEventListener('change', onMotionPref);
    cancelAnimationFrame(raf);
    ro.disconnect();
    gl.getExtension('WEBGL_lose_context')?.loseContext();
    gl.canvas.remove();
  };
}
```

```css
.bg-field { position: relative; min-height: 70vh; display: grid; place-items: center; color: var(--color-on-bg, #fff);
  background: linear-gradient(160deg, var(--color-bg, #1f3a4d), var(--color-action, #6b4f3a)); } /* the fallback is the same two colours */
.bg-field canvas { position: absolute; inset: 0; width: 100%; height: 100%; z-index: 0 }
.bg-field > :not(canvas) { position: relative; z-index: 1 }
```

```html
<section class="bg-field"><h2>[Section heading]</h2></section>
```

```js
import { webglOk, afterLoad } from './motion-safety.js';
const host = document.querySelector('.bg-field');
if (webglOk()) {
  afterLoad()
    .then(() => import('./shader-bg.js'))
    .then(({ mountShaderBackground }) => mountShaderBackground(host, { colorA: getComputedStyle(host).getPropertyValue('--color-bg').trim() || '#1f3a4d', colorB: getComputedStyle(host).getPropertyValue('--color-action').trim() || '#6b4f3a' }))
    .catch((err) => console.warn('Shader skipped, CSS gradient stays:', err));
}
```

Pass colours from `tokens.css` as hex. `getComputedStyle` returns the token
as written, so if the tokens use `oklch()`, convert first (OGL's `Color` and
`THREE.Color` read hex); the hex fallbacks in the code below apply when a
token is missing. The CSS gradient uses the same two
colours, so the fallback and the shader look related.

#### Reduced motion

One still frame is drawn; the loop never starts, and stops if the
preference turns on.

#### Cost

OGL about 34 kB gzipped. A full-screen fragment shader every frame while
visible, at a pixel ratio capped at 1.5. Measured: about 60 draws per second
on screen, zero off screen.

#### Fallback

The CSS `linear-gradient` on `.bg-field`. With WebGL disabled, no canvas is
created.

### Particle field (Three.js)

#### Use when

The subject has a natural particle reading (dust in a beam of light, a star
field behind an astronomy product) and the direction asks for it.

#### Avoid when

Particles stand in for "technology" with no subject behind them; that is
decoration. Phones with many particles; behind text.

#### Code

New to this skill: `.bg-field`, `.is-live`.

```js
// particles.js: a drifting point field. Motion lives in the vertex shader, so the CPU does nothing per frame.
import * as THREE from 'three';
import { reducedMotion, watchVisibility } from './motion-safety.js';

export function mountParticles(host, { count = 4000, color = getComputedStyle(host).getPropertyValue('--color-on-bg').trim() || '#d9c9a8' } = {}) {
  const renderer = new THREE.WebGLRenderer({ alpha: true, antialias: false });
  renderer.setPixelRatio(Math.min(devicePixelRatio, 1.5));
  renderer.domElement.setAttribute('aria-hidden', 'true');
  host.append(renderer.domElement);
  const scene = new THREE.Scene();
  const camera = new THREE.PerspectiveCamera(50, 1, 0.1, 50);
  camera.position.z = 6;

  const positions = new Float32Array(count * 3);
  const seeds = new Float32Array(count);
  for (let i = 0; i < count; i++) {
    positions.set([(Math.random() - 0.5) * 12, (Math.random() - 0.5) * 7, (Math.random() - 0.5) * 6], i * 3);
    seeds[i] = Math.random() * 100;
  }
  const geometry = new THREE.BufferGeometry();
  geometry.setAttribute('position', new THREE.BufferAttribute(positions, 3));
  geometry.setAttribute('aSeed', new THREE.BufferAttribute(seeds, 1));
  const material = new THREE.ShaderMaterial({
    transparent: true, depthWrite: false,
    uniforms: { uTime: { value: 0 }, uColor: { value: new THREE.Color(color) }, uPixelRatio: { value: renderer.getPixelRatio() } },
    vertexShader: /* glsl */ `
      uniform float uTime; uniform float uPixelRatio; attribute float aSeed;
      void main() {
        vec3 p = position;
        p.x += sin(uTime * 0.2 + aSeed) * 0.15;
        p.y += cos(uTime * 0.15 + aSeed * 1.3) * 0.15;
        vec4 mv = modelViewMatrix * vec4(p, 1.0);
        gl_PointSize = 3.0 * uPixelRatio * (4.0 / -mv.z);
        gl_Position = projectionMatrix * mv;
      }`,
    fragmentShader: /* glsl */ `
      uniform vec3 uColor;
      void main() {
        float d = length(gl_PointCoord - 0.5);
        if (d > 0.5) discard;
        gl_FragColor = vec4(uColor, smoothstep(0.5, 0.1, d) * 0.8);
      }`,
  });
  scene.add(new THREE.Points(geometry, material));

  const draw = () => renderer.render(scene, camera);
  const ro = new ResizeObserver(() => {
    renderer.setSize(host.clientWidth, host.clientHeight, false);
    camera.aspect = host.clientWidth / host.clientHeight;
    camera.updateProjectionMatrix();
    draw();
  });
  ro.observe(host);

  const clock = new THREE.Timer();
  const tick = (t) => { clock.update(t); material.uniforms.uTime.value = clock.getElapsed(); draw(); };
  let visible = false; // last value from watchVisibility: in view and tab shown
  const update = () => renderer.setAnimationLoop(visible && !reducedMotion.matches ? tick : null);
  const stopWatching = watchVisibility(host, (v) => { visible = v; update(); });
  const onMotionPref = update;
  reducedMotion.addEventListener('change', onMotionPref);
  host.classList.add('is-live');

  return () => {
    stopWatching();
    reducedMotion.removeEventListener('change', onMotionPref);
    renderer.setAnimationLoop(null);
    ro.disconnect();
    geometry.dispose(); material.dispose(); renderer.dispose();
    renderer.domElement.remove();
  };
}
```

Mount it like the shader background:

```js
import { webglOk, afterLoad } from './motion-safety.js';
const host = document.querySelector('.bg-field');
if (webglOk()) {
  afterLoad()
    .then(() => import('./particles.js'))
    .then(({ mountParticles }) => mountParticles(host))
    .catch((err) => console.warn('Particles skipped, the section stays as it is:', err));
}
```

Motion is computed in the vertex shader from a per-particle seed, so the CPU
only updates one uniform per frame. For simulated motion (particles that
react to each other or the pointer), the technique is GPGPU with render
targets; that is a larger build than a landing page usually needs.

#### Reduced motion

One still frame; the loop does not run.

#### Cost

three.js. 4,000 points is light for the GPU; increase with care on phones.
Pixel ratio capped at 1.5.

#### Fallback

The section's own background. Particles add nothing the content depends on.

### Image hover distortion (Three.js)

#### Use when

One or two images on a desktop-heavy page with a visual, editorial tone,
where a ripple following the pointer suits the direction.

#### Avoid when

Galleries (one canvas per image does not scale; a shared canvas is a
different build); product photos the visitor must inspect; touch devices.

#### Code

New to this skill: `.distort-frame`, `.is-live`.

```js
// image-distort.js: a ripple under the pointer on one image. The <img> stays in the DOM underneath.
import * as THREE from 'three';

export async function mountImageDistortion(img) {
  const frame = img.parentElement; // position: relative, same box as the image
  const texture = await new THREE.TextureLoader().loadAsync(img.currentSrc || img.src);
  texture.colorSpace = THREE.SRGBColorSpace;
  const renderer = new THREE.WebGLRenderer({ alpha: true });
  renderer.setPixelRatio(Math.min(devicePixelRatio, 2));
  renderer.outputColorSpace = THREE.SRGBColorSpace;
  const canvas = renderer.domElement;
  canvas.setAttribute('aria-hidden', 'true');
  frame.append(canvas);
  const scene = new THREE.Scene();
  const camera = new THREE.OrthographicCamera(-0.5, 0.5, 0.5, -0.5, 0, 1);
  const material = new THREE.ShaderMaterial({
    uniforms: { uTexture: { value: texture }, uMouse: { value: new THREE.Vector2(0.5, 0.5) }, uHover: { value: 0 } },
    vertexShader: /* glsl */ `varying vec2 vUv; void main() { vUv = uv; gl_Position = vec4(position.xy * 2.0, 0.0, 1.0); }`,
    fragmentShader: /* glsl */ `
      uniform sampler2D uTexture; uniform vec2 uMouse; uniform float uHover; varying vec2 vUv;
      void main() {
        float d = distance(vUv, uMouse);
        float ripple = sin(d * 40.0) * 0.012 * uHover * smoothstep(0.35, 0.0, d);
        gl_FragColor = texture2D(uTexture, vUv + normalize(vUv - uMouse + 1e-5) * ripple);
        #include <colorspace_fragment>
      }`,
  });
  scene.add(new THREE.Mesh(new THREE.PlaneGeometry(1, 1), material));

  const ro = new ResizeObserver(() => { renderer.setSize(frame.clientWidth, frame.clientHeight, false); renderer.render(scene, camera); });
  ro.observe(frame);

  // Render only while the hover value is moving. Idle image = zero GPU work.
  let target = 0;
  const tick = () => {
    const u = material.uniforms.uHover;
    u.value += (target - u.value) * 0.12;
    renderer.render(scene, camera);
    if (Math.abs(target - u.value) < 0.001) { u.value = target; renderer.render(scene, camera); renderer.setAnimationLoop(null); }
  };
  const onMove = (e) => {
    const r = frame.getBoundingClientRect();
    material.uniforms.uMouse.value.set((e.clientX - r.left) / r.width, 1 - (e.clientY - r.top) / r.height);
    target = 1;
    renderer.setAnimationLoop(tick);
  };
  const onLeave = () => { target = 0; renderer.setAnimationLoop(tick); };
  frame.addEventListener('pointermove', onMove);
  frame.addEventListener('pointerleave', onLeave);
  frame.classList.add('is-live');

  return () => {
    frame.removeEventListener('pointermove', onMove);
    frame.removeEventListener('pointerleave', onLeave);
    renderer.setAnimationLoop(null);
    ro.disconnect();
    texture.dispose(); material.dispose(); renderer.dispose();
    canvas.remove();
    frame.classList.remove('is-live');
  };
}
```

```html
<figure class="distort-frame"><img src="/img/feature.avif" alt="[Describe the photo]" width="1200" height="800"></figure>
```

```css
.distort-frame { position: relative; width: 640px; aspect-ratio: 3 / 2 }
.distort-frame img { display: block; width: 100%; height: 100%; object-fit: cover }
.distort-frame canvas { position: absolute; inset: 0; width: 100%; height: 100%; pointer-events: none }
```

```js
import { webglOk, afterLoad } from './motion-safety.js';
const decorative = matchMedia('(prefers-reduced-motion: no-preference) and (hover: hover) and (pointer: fine)');
if (decorative.matches && webglOk()) {
  afterLoad()
    .then(() => import('./image-distort.js'))
    .then(({ mountImageDistortion }) => mountImageDistortion(document.querySelector('.distort-frame img')))
    .catch((err) => console.warn('Distortion skipped, the image stays:', err));
}
```

The `<img>` stays in the DOM with its `alt` text; the canvas sits on top
with `pointer-events: none` and `aria-hidden`.

#### Reduced motion

Not mounted: the media query in the mount requires `no-preference` and a
fine pointer.

#### Cost

three.js plus one texture upload. Renders only while the hover value is
moving; zero draws while idle (measured).

#### Fallback

The `<img>` itself.

## WebGPU status

`WebGPURenderer` from `three/webgpu` uses WebGPU where the browser has it and
falls back to a WebGL 2 backend automatically. Support, from the gpuweb
implementation-status wiki as of 2026-10-04:

| Browser | WebGPU |
|---|---|
| Chrome / Edge | 113 desktop (Windows, macOS, ChromeOS), Android 121, Linux Intel Gen12+ 144 |
| Firefox | Windows 141, macOS Apple Silicon 145, other macOS 147; Linux and Android still Nightly |
| Safari | 26 (macOS, iOS, iPadOS, visionOS) |

Global availability is about 86 percent (caniuse). Never make it a
requirement: the fallback covers everyone else. Use it when you need TSL node
materials or compute; for a product viewer, `WebGLRenderer` is enough. React
Three Fiber 9 targets `WebGLRenderer` by default.

```js
import * as THREE from 'three/webgpu';

const renderer = new THREE.WebGPURenderer({ antialias: true }); // falls back to WebGL 2 by itself
await renderer.init(); // required before the first synchronous render call
renderer.setPixelRatio(Math.min(devicePixelRatio, 2));
host.append(renderer.domElement);

const scene = new THREE.Scene();
scene.add(new THREE.Mesh(new THREE.BoxGeometry(), new THREE.MeshBasicNodeMaterial({ color: '#a86' })));
const camera = new THREE.PerspectiveCamera(50, 1, 0.1, 10);
camera.position.z = 3;
renderer.setSize(host.clientWidth, host.clientHeight);
renderer.render(scene, camera);

const usingWebGPU = !!renderer.backend.isWebGPUBackend; // false when it fell back to WebGL 2
```

`await renderer.init()` is required before the first synchronous render.
`host` is the element that holds the canvas. Tested in headless Chromium,
which had no WebGPU adapter: the renderer fell back to its WebGL 2 backend
and rendered without errors, as did `forceWebGL: true`. The WebGPU backend
itself was not exercised.
