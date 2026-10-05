# Loading performance

Read this when loading fonts, placing images, or adding any script. The
page is static, so most of the work is not adding weight in the first place.

## Contents

- Tools named here
- Budget
- Fonts
- Subsetting without a dedicated tool
- Metric-matched fallback
- Variable fonts with a width axis
- Images
- Preventing layout shift
- JavaScript
- Measuring

## Tools named here

The scripts in this file and in `seo-meta.md` use four npm packages. All are
development tools: they run on your machine while building and nothing from
them ships in the page. Install them with `npm i -D`. Licences as reported by
`npm view <package> license`:

| Package | Version tested | Licence | Used for |
|---|---|---|---|
| `fontaine` | 1.0.0 | MIT | fallback font metrics |
| `subset-font` | 2.9.0 | BSD-3-Clause | subsetting fonts |
| `playwright` | 1.63.0 | Apache-2.0 | per-width font measurement, social card and icon capture |
| `sharp` | 0.35.5 | Apache-2.0 | image variants |

The local-server commands in `SKILL.md` use `serve` 14.2.6 (MIT) through
`npx`, or Python's built-in `http.server`.

## Budget

| Item | Budget | Where the number comes from |
|---|---|---|
| Largest Contentful Paint | 2.5 s or less (4.0 s is poor) at the 75th percentile | web.dev thresholds, in the research |
| Cumulative Layout Shift | 0.1 or less (0.25 is poor) | same |
| Interaction to Next Paint | 200 ms or less (500 ms is poor) | same |
| Font families | at most 2, plus one mono if the direction has one | research on type setting |
| Font files | 3 to 4 WOFF2 files; a variable file once a family needs 3 or more weights or styles | same |
| Preloaded fonts | 1 or 2, the ones used above the fold | same |
| JavaScript needed to render | 0 KB | this skill's rule: the page works with scripts off |
| Hero image | about 200 KB at its largest served width | a working figure, not a sourced one; it leaves room inside the LCP target on a slow connection. Go lower when you can |

For scale, two measurements made while writing this skill: a one-page Next
16.3.8 static export shipped about 175 KB of gzipped framework scripts
before any page code, while the same page in Astro shipped none beyond its
own form script. One unguided build shipped its whole app, 3D library
included, as one bundle of over 300 KB gzipped that had to load before any
hero text could paint, and an empty `<div id="root">` with JavaScript off. The adapter notes in `adapters.md`
show how each stack stays inside the budget.

## Fonts

- Self-host WOFF2 files (WOFF2 compresses about 30% better than WOFF). Get
  them from the source named in `direction.md`; Fontsource packages ship
  per-subset files such as `source-serif-4-latin-400-normal.woff2`.
- Subset to the scripts the page uses. A Latin font holds roughly 100 to
  1,000 glyphs; Fontsource's `latin` files are already subset. For other
  files, the research names subfont and glyphhanger as subsetting tools;
  when neither is installed, use the script in the next section. A Spanish
  page needs the Latin subset with accents, which `latin` covers.
- Declare each file with `@font-face`, using the exact family name the token
  stack uses:

  ```css
  @font-face {
    font-family: "Source Serif 4";
    src: url("/fonts/source-serif-4-latin-400-normal.woff2") format("woff2");
    font-weight: 400;
    font-style: normal;
    font-display: swap;
  }
  ```

- `font-display: swap` for the text face (44% of desktop pages use it).
  `optional` for a decorative display face when layout stability matters
  more than the face appearing on a slow first visit. Avoid `block`.
- Preload only the above-the-fold files, with `crossorigin` even on the same
  origin (font requests are CORS requests, and a preload without it is not
  reused):

  ```html
  <link rel="preload" href="/fonts/source-serif-4-latin-400-normal.woff2" as="font" type="font/woff2" crossorigin>
  ```

  Each extra preload competes with the hero image for bandwidth.
- The Google Fonts CSS endpoint works, but self-hosting removes a
  third-party connection and pins the version. The research notes that
  self-hosting is only faster when the host serves the files from a CDN
  over HTTP/2.

## Subsetting without a dedicated tool

`subset-font` is an npm package that runs HarfBuzz's subsetter in Node,
with no Python or browser needed (`npm i -D subset-font`; tested with
2.9.0). It keeps variable axes: a subset of a `wght` and `wdth` variable
file still had both axes afterwards.

```js
// subset.mjs: subset a font to the characters a page uses, keeping variable axes.
// Usage: node subset.mjs <in.woff2|ttf|otf> <out.woff2> <file-with-the-page-text>...
import { readFile, writeFile } from 'node:fs/promises';
import subsetFont from 'subset-font';

const [input, output, ...textFiles] = process.argv.slice(2);
// Printable ASCII plus the accented letters and punctuation Latin-script pages use.
let text = ' !"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~'
  + 'ÁÉÍÓÚÜÑáéíóúüñ¿¡àèìòùâêîôûäëïöçÇ«»‘’“”–—…·€£';
for (const f of textFiles) text += await readFile(f, 'utf8');
const source = await readFile(input);
const result = await subsetFont(source, text, { targetFormat: 'woff2' });
await writeFile(output, result);
console.log(`${input}: ${source.length} bytes -> ${output}: ${result.length} bytes`);
```

Pass the built HTML (and `copy.md`) as text files so every character the
page shows is kept; markup characters in them are harmless. In the test, a
full desktop TrueType file of 379,588 bytes became a 26,572-byte WOFF2. Keep
the original file, check the subset renders every heading and the form, and
subset only fonts whose licence allows modification. The SIL Open Font
License does; for any other licence, check the terms named in
`direction.md` before subsetting.

If you could not subset a file (no Node, a licence that forbids it), ship it
as it is and record it in the hand-off: the file name, its size in bytes
(`wc -c < public/fonts/face.woff2`), and that it is not subset.

## Metric-matched fallback

While the web font loads, the browser draws the fallback. If the two have
different metrics, text reflows when the font arrives, which shows up as
layout shift. The fix is a local fallback face with `size-adjust`,
`ascent-override`, `descent-override` and `line-gap-override` set so it
occupies the same space. Research document 03 records `size-adjust` as
Baseline widely available since September 2023, citing MDN. A browser that
ignores these descriptors still uses the fallback face, just unadjusted, so
the text stays readable and only the shift on font arrival comes back.

Compute the values with Fontaine, which the research names for this job.
This script was run against a real WOFF2 (`npm i -D fontaine`, tested with
fontaine 1.0.0):

```js
// fallback-face.mjs
// Usage: node fallback-face.mjs public/fonts/face.woff2 "Face Name" "Georgia"
import { pathToFileURL } from 'node:url';
import { readMetrics, getMetricsForFamily, generateFontFace } from 'fontaine';

const [file, family, local = 'Arial'] = process.argv.slice(2);
const metrics = await readMetrics(pathToFileURL(file));
const fallbackMetrics = await getMetricsForFamily(local);
if (!metrics || !fallbackMetrics) throw new Error('Could not read metrics for the web font or the local fallback');
console.log(generateFontFace(metrics, { name: `${family} fallback`, font: local, metrics: fallbackMetrics }));
```

For Source Serif 4 regular against Georgia it prints:

```css
@font-face {
  font-family: "Source Serif 4 fallback";
  src: local("Georgia");
  size-adjust: 107.4471%;
  ascent-override: 96.4195%;
  descent-override: 31.1781%;
  line-gap-override: 0%;
}
```

Pass the local font of the same classification: a serif web font against
Georgia or Times New Roman, a sans against Arial. Without the third argument
the fallback metrics are missing and `size-adjust` comes out as 100%.

Hooking the fallback up without touching the tokens:

- If the token stack already names it (`"Source Serif 4", "Source Serif 4 fallback", Georgia, serif`),
  paste the printed rule as it is.
- If the stack names only a system font after the web font
  (`"Source Serif 4", Georgia, serif`), change the printed `font-family` to
  that system font's name, here `"Georgia"`, and keep
  `src: local("Georgia")`. The page's own `@font-face` then replaces Georgia
  with an adjusted copy of itself, for this page only. Tested in Chromium:
  text in the shadowed face measured exactly `size-adjust` times wider. If
  two token stacks fall back to the same system font, one adjusted copy
  cannot match both; adjust it for the text face, which has the most lines,
  and mention it in the hand-off so the direction can name a dedicated
  fallback.

The fallback face describes one weight. Bold text that falls back to a
face declared from the regular local font is drawn in the regular face, so
it takes less room than the bold web font. Declare one fallback face per
weight in use, each with `font-weight` set and `src: local()` naming the
matching local face by its full name (`"Arial Bold"`, `"Georgia Bold"`).

## Variable fonts with a width axis

A family with a `wdth` axis used at a narrow width (`font-stretch: 75%`)
sets far fewer pixels per line than its default instance, and Fontaine
reads only the default. Give the fallback one `@font-face` per
`font-stretch` value the page uses, each with its own `font-stretch`
descriptor and its own measurement. The browser picks the fallback face
whose descriptor matches the element, exactly as it does for weights.

This script measures each width in a real browser (`npm i -D fontaine
playwright`; tested with fontaine 1.0.0 and Playwright 1.63.0, after
`npx playwright install chromium`):

```js
// fallback-widths.mjs: one fallback @font-face per font-stretch value of a font with a wdth axis.
// Usage: SAMPLE="the h1 text" node fallback-widths.mjs <font.woff2> "<Family>" "<Local face>" <weight> <stretch>...
//   e.g. SAMPLE="Bikes fixed and back the same week" node fallback-widths.mjs public/fonts/face-wdth.woff2 "Face" "Arial Bold" 700 75 100
// The local face is a full name: "Arial Bold" for bold text, "Arial" for regular.
import { readFile } from 'node:fs/promises';
import { pathToFileURL } from 'node:url';
import { chromium } from 'playwright';
import { readMetrics } from 'fontaine';

const [file, family, local, weight, ...stretches] = process.argv.slice(2);
const m = await readMetrics(pathToFileURL(file));
if (!m || !stretches.length) throw new Error('Usage: <font.woff2> <family> <local> <weight> <stretch>...');
const sample = process.env.SAMPLE || 'The quick brown fox jumps over the lazy dog 0123456789';
const data = (await readFile(file)).toString('base64');

const browser = await chromium.launch();
const page = await browser.newPage();
await page.setContent(`<style>@font-face{font-family:"Probe";src:url(data:font/woff2;base64,${data}) format("woff2");font-weight:1 1000;font-stretch:1% 1000%}@font-face{font-family:"LocalProbe";src:local("${local}")}</style>`);
const widths = await page.evaluate(async ({ sample, weight, stretches }) => {
  await document.fonts.load(`${weight} 100px Probe`, sample);
  const measure = (font, stretch) => {
    const s = document.createElement('span');
    s.style.cssText = `font: ${weight} 100px ${font}; font-stretch: ${stretch}%; white-space: nowrap`;
    s.textContent = sample; document.body.append(s);
    const w = s.getBoundingClientRect().width; s.remove(); return w;
  };
  const base = measure('LocalProbe', 100); // the exact local face the output names
  return stretches.map((st) => [st, measure('Probe', st) / base]);
}, { sample, weight, stretches });
await browser.close();

const pct = (n) => `${(n * 100).toFixed(4)}%`;
for (const [stretch, ratio] of widths) {
  console.log(`@font-face {
  font-family: "${family} fallback";
  src: local("${local}");
  font-weight: ${weight};
  font-stretch: ${stretch}%;
  size-adjust: ${pct(ratio)};
  ascent-override: ${pct(m.ascent / m.unitsPerEm / ratio)};
  descent-override: ${pct(Math.abs(m.descent) / m.unitsPerEm / ratio)};
  line-gap-override: ${pct(m.lineGap / m.unitsPerEm / ratio)};
}`);
}
```

Run it once per weight in use, with every stretch value the page uses for
that weight. Set `SAMPLE` to the text the face will actually set, the h1
for a display face. Test result with a variable grotesque at 700 and
`font-stretch: 75%`, a 60px headline: the web font measured 1032px wide;
the fallback faces from this script measured 1032px with `SAMPLE` set to
that headline and 1036px with the default sample; a single fallback face
made for the default width measured about 1310px, 27% too wide, which is
the reflow a visitor would see when the font arrives. Heights matched in every
case.

The web font's own `@font-face` declares the ranges it covers, so one file
serves every width and weight:

```css
@font-face {
  font-family: "Face";
  src: url("/fonts/face-wdth.woff2") format("woff2");
  font-weight: 100 900;
  font-stretch: 62% 125%;
  font-display: swap;
}
```

Use the axis ranges the file reports; Fontsource names variable files with
the axes they carry (`-wdth-normal.woff2`).

Framework font loaders do this automatically but rename the family, which
breaks the match with the token stack. `adapters.md` says what to do per
stack.

## Images

- Serve modern formats with a fallback through `<picture>`. The browser
  takes the first `source` whose `type` it supports, so no support claim is
  needed: AVIF first, WebP second, JPEG in the `img`.
- Always give `width` and `height` (the intrinsic size). With
  `height: auto` in CSS, the browser reserves the right box before the file
  arrives.
- Use `srcset` with `w` descriptors and a `sizes` value that matches the
  layout, so a phone does not download the desktop file.
- The hero image is the likely LCP element: `loading="eager"` (the default)
  and `fetchpriority="high"`. Never `loading="lazy"` on it. Browsers that do
  not know `fetchpriority` ignore it.
- Every image below the fold: `loading="lazy"` and `decoding="async"`.

```html
<picture>
  <source type="image/avif" srcset="/img/hero-640.avif 640w, /img/hero-1280.avif 1280w, /img/hero-1920.avif 1920w" sizes="(min-width: 44rem) 58vw, 100vw">
  <source type="image/webp" srcset="/img/hero-640.webp 640w, /img/hero-1280.webp 1280w, /img/hero-1920.webp 1920w" sizes="(min-width: 44rem) 58vw, 100vw">
  <img src="/img/hero-1280.jpg" srcset="/img/hero-640.jpg 640w, /img/hero-1280.jpg 1280w, /img/hero-1920.jpg 1920w" sizes="(min-width: 44rem) 58vw, 100vw"
       width="1920" height="1440" alt="A loaf scored with a wheat pattern, cooling on a rack" fetchpriority="high">
</picture>
```

To make the files for a plain HTML page, this script was run with sharp
0.35.5 (`npm i -D sharp`). The quality values are starting points; compare
the output by eye.

```js
// images.mjs
// Usage: node images.mjs source/hero.jpg public/img/hero 640 1280 1920
import sharp from 'sharp';

const [src, outBase, ...widths] = process.argv.slice(2);
for (const w of widths.map(Number)) {
  const img = sharp(src).resize({ width: w, withoutEnlargement: true });
  await img.clone().avif({ quality: 50 }).toFile(`${outBase}-${w}.avif`);
  await img.clone().webp({ quality: 75 }).toFile(`${outBase}-${w}.webp`);
  await img.clone().jpeg({ quality: 78, mozjpeg: true }).toFile(`${outBase}-${w}.jpg`);
}
```

Astro and Next have their own image components; see `adapters.md`.

## Preventing layout shift

- Every image, video, iframe, canvas and placeholder has a reserved box:
  `width` and `height` attributes, or `aspect-ratio` in CSS.
- The metric-matched fallback above, for every web font.
- Nothing is inserted above content that is already on screen: no cookie bar
  or banner pushed in at the top after load. Status messages for the form
  live in an element that is already in the markup.
- A 3D scene or animation that `landing-skills:landing-motion` will mount
  gets its poster image now, at the final size.

## JavaScript

- The static page needs none to render. The only script this skill writes
  is the form enhancement in `forms-a11y.md`, shipped with every form,
  loaded with `defer` (or as a module), under 2 KB.
- Anything heavy that the direction asks for later (3D, animation
  libraries) loads after first paint in its own chunk. `adapters.md` shows
  how in Vite with React.
- Do not raise a bundler's chunk size warning limit to silence it, as one
  unguided build did; the warning was right.

## Measuring

Run the page from a local server (not `file://`), then record LCP, CLS and
the total transfer size with the browser's performance tools or Lighthouse,
at a phone width. Check that the LCP element is the hero image or the h1 and
that it is visible without waiting for a script.
