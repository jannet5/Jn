# 3D and WebGL: assets

## Contents

- What to import, and what to leave out
- Making the poster
- Asset pipeline

The scene recipes are in `webgl-3d.md` and `webgl-effects.md`. This file is
the reference they point to for models, compression, textures, the build
output, and posters.

## What to import, and what to leave out

Import only what the scene uses, because a bundler puts
every imported module, and every file those modules reference, into the
output:

- No compressed model yet: import no loader. In three 0.186, importing
  `DRACOLoader` or `KTX2Loader` alone makes Vite copy their decoder files
  into the build (measured: five Draco files, 1.3 MB raw, from one
  `DRACOLoader` import). Add the loader in the same change that adds a
  Draco-compressed model ("Swap the primitive stand-in" in `webgl-3d.md`).
- Lighting: three's `RoomEnvironment` through `PMREMGenerator` gives
  reflections for metal and plastic with no download and no extra library.
  Do not add drei for `Environment` alone.
- Check the output after `vite build`: `find dist -name 'draco_*' -o -name
  'basis_*'`. Files there with no compressed model on the page mean an
  unused loader import; remove the import, not the files. In our builds:
  the primitive scene's output had no decoder files; the GLB version had
  them, and only two of the five were requested at run time.

## Making the poster

Every stage needs a poster: it is what visitors see with reduced motion (if
the scene is skipped), with WebGL off, with JavaScript off, and on a slow
connection until the scene draws. It must stand on its own: the object,
framed as the scene frames it, at the stage's aspect ratio. Use a real
photograph when one exists. When none exists, render the scene itself once
and save that frame:

```js
// make-poster.mjs: render the scene once in a real browser and save it as the poster image.
// usage: node make-poster.mjs <page-url> <stage-selector> <out.png> [scrollY]
// Run against the dev server. Needs Playwright (npm i playwright && npx playwright install chromium).
import { chromium } from 'playwright';

const [url, selector, out, scrollY = '0'] = process.argv.slice(2);
const browser = await chromium.launch({ args: ['--use-angle=swiftshader', '--enable-unsafe-swiftshader'] });
const page = await browser.newPage({ viewport: { width: 1440, height: 900 }, deviceScaleFactor: 2 });
await page.goto(url, { waitUntil: 'load' });
await page.evaluate((y) => scrollTo(0, Number(y)), scrollY);
await page.waitForSelector(`${selector}.is-live`, { timeout: 20000 }); // first frame drawn
await page.waitForTimeout(500);                                        // let any glide settle
await page.locator(`${selector} canvas`).screenshot({ path: out });
console.log('poster written:', out);
await browser.close();
```

```bash
npm run dev   # the page with the scene, on Vite's default port
node make-poster.mjs http://localhost:5173/ .product-stage public/img/object-poster.png
# a later state: scroll first (here to the end of the exploded view)
node make-poster.mjs http://localhost:5173/ "[data-moment=exploded-view] .object-stage" public/img/object-poster-exploded.png 3000
```

It screenshots the stage's canvas after the first frame (`.is-live`) at
twice the pixel density, so no code is added to the page. Tested on both
recipes: a 1440 by 1800 image of the resting object and a 2880 by 1800 image
of the exploded stage. Convert the PNG to AVIF or WebP with your image tool,
and give the `<img>` the stage's size so nothing shifts when the canvas
replaces it. The poster carries the page's background, so make it on the
final colours.

## Asset pipeline

- **Format:** GLB (binary glTF).
- **Inspect first:** `npx @gltf-transform/cli inspect model.glb` lists
  meshes, node names, vertex counts, and texture sizes.
- **Optimise:** `npx @gltf-transform/cli optimize in.glb out.glb --compress draco`
  runs dedupe, prune, and compression in one step. Its defaults do not suit
  every scene, so inspect the output and check that the node names your code
  uses survived. Separate commands exist for each step: `dedup`, `prune`,
  `draco`, `meshopt`, `simplify`, `resize`, `webp`, `etc1s`, `uastc`
  (glTF Transform CLI 4.5.1).
- **Geometry compression:** Draco needs a WASM decoder; Meshopt is the
  other option. The tools describe Draco as giving smaller files and Meshopt
  as decoding faster and suiting animation and quantisation, but which to
  choose for a given model is unverified: try both on your model and compare
  size and load time. Both loaders are wired in `load-model.js`.
- **Textures:** KTX2 (Basis) is the GPU texture format. That KTX2 textures
  stay compressed in GPU memory while WebP, PNG, and JPEG decode to full
  size is general knowledge, unverified against a fetched source. The `etc1s` and `uastc`
  commands produce KTX2 and need the KTX-Software tools installed;
  `KTX2Loader` needs `setTranscoderPath` and `detectSupport(renderer)`, as in
  `load-model.js`.
- **React:** `npx gltfjsx model.glb --transform` writes a compressed,
  resized, deduplicated GLB and a React component for it. Its texture output
  is WebP, which is not GPU-compressed.
- **Lazy loading:** never in the critical path. Scene modules and models
  load after `load`, or when the stage nears the viewport; a poster holds the
  space.
- **Self-host** models, decoders, and environment maps (`safety.md`).
- **Working targets** (heuristics, not sourced rules): about 1 to 2 MB for a
  hero model over the wire, textures 1024 to 2048 px, under 100,000 to
  150,000 triangles per scene on mobile.
