# Stack adapters

Read the section for the detected stack. Each covers only what differs from
the plain HTML and CSS recipes in the other references; everything else
(section and container, the form, the head, the accessibility floor)
applies unchanged. Version notes say which version was built and checked
while writing this skill; on another major version, check before relying on
a detail.

## Contents

- Output folders
- Plain HTML and CSS
- Astro
- Next
- Vite with React
- Tailwind v4

## Output folders

`landing-skills:landing-launch` deploys the output folder as static assets.

| Stack | Build command | Output folder |
|---|---|---|
| plain HTML | none | `public/` (the folder you serve) |
| Astro, static | `npx astro build` | `dist/` |
| Next, static export | `npx next build` with `output: 'export'` | the export folder (`out/` in the Next 16.3.8 test build) |
| Vite with React | `npx vite build` | `dist/` |

None of these folders should contain `landing/*.md`: the brief and the
direction are working files, not pages.

## Plain HTML and CSS

The other references are written for this case. Two reminders:

- The tokens are served from `public/tokens.css`, a byte-for-byte copy of
  `landing/tokens.css`. Check with `cmp landing/tokens.css public/tokens.css`.
- The result pages are `public/thanks/index.html` and
  `public/form-error/index.html`, served at `/thanks/` and `/form-error/`.

## Astro

Checked with Astro 7.3.5 on Node 26; Astro 6 and 7 need Node 22.12.0 or
later.

- Static output needs no adapter: `astro build` writes plain files to
  `dist/`. Cloudflare bindings (a database, Analytics Engine) are not
  available to a static Astro build, so the form endpoint stays a separate
  Worker, which is what `landing-skills:landing-launch` builds.
- Import the tokens and your stylesheet in the layout's frontmatter. Astro
  bundles them and keeps the custom properties as written:

  ```astro
  ---
  // src/layouts/Base.astro
  import '../../landing/tokens.css';
  import '../styles.css';
  const { title, description } = Astro.props;
  // Absolute URLs only when the production domain is known (`site` in astro.config.mjs).
  const canonical = Astro.site && new URL(Astro.url.pathname, Astro.site);
  const ogImage = Astro.site && new URL('/og.png', Astro.site);
  ---
  <!doctype html>
  <html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>{title}</title>
    <meta name="description" content={description}>
    {canonical && <link rel="canonical" href={canonical}>}
    {canonical && <meta property="og:url" content={canonical}>}
    {ogImage && <meta property="og:image" content={ogImage}>}
  </head>
  <body><slot /></body>
  </html>
  ```

- Set `site` in `astro.config.mjs` (`export default defineConfig({ site: 'https://example.com' })`)
  once the production domain is known, so `Astro.site` gives absolute
  canonical and `og:` URLs. Until then leave `site` out; never put a
  placeholder in it. Tested with Astro 7.3.5: a bracketed placeholder as
  `site` stops the build with "Invalid URL"; with no `site`, an unguarded
  `new URL(path, Astro.site)` fails the build the same way, and the guarded
  layout above builds and simply omits the three tags. Note the gap in the
  hand-off (rule in `seo-meta.md`).
- Pass plain text to props: `title="Fern & Field"`, not `Fern &amp; Field`.
  Astro escapes expressions itself, and an entity in a prop comes out
  double-escaped (`&amp;amp;`) in the built HTML. This happened in the test
  build.
- Fonts: put the WOFF2 files in `public/fonts/` and use the plain
  `@font-face` and fallback recipe from `performance.md`, so the family
  names match the token stacks. Add the font preload `<link>` in the layout
  head.
- Images: `Picture` from `astro:assets` generates the formats and widths at
  build time and writes `width` and `height`:

  ```astro
  ---
  import { Picture } from 'astro:assets';
  import hero from '../assets/hero.jpg';
  ---
  <Picture src={hero} formats={['avif', 'webp']} widths={[640, 1280, 1920]}
    sizes="(min-width: 44rem) 58vw, 100vw" alt="..." loading="eager" fetchpriority="high" />
  ```

  In the test build this produced a `<picture>` with AVIF and WebP sources
  and a fallback `img` carrying `fetchpriority="high"`. Images in `public/`
  are not processed; use `src/assets/` for anything that needs variants.
- The result pages are `src/pages/thanks.astro` and
  `src/pages/form-error.astro`.
- `<script src="../form.js"></script>` in a component is bundled and emitted
  as a module script, which runs deferred; the form script from
  `forms-a11y.md` works unchanged. Astro adds no other JavaScript unless you
  add an interactive framework component.

## Next

Checked with Next 16.3.8 and the App Router.

- A landing page is a static export: `next.config.mjs` with
  `export default { output: 'export', trailingSlash: true, images: { unoptimized: true } };`.
  `trailingSlash: true` makes the export write `form-error/index.html`
  instead of `form-error.html` (both checked in the test build), so the
  `/thanks/` and `/form-error/` addresses the form endpoint redirects to
  exist as written.
  For Cloudflare, `landing-skills:landing-launch` deploys the export folder.
- `images: { unoptimized: true }` is required with `next/image` in an
  export. Without it, the test build wrote `src` and `srcset` URLs under
  `/_next/image?...`, an optimisation route that does not exist in static
  files, so every image would fail. With it, `next/image` writes one plain
  `src`; for AVIF and WebP variants write a `<picture>` by hand as in
  `performance.md`, with files made ahead of time.
- Head tags come from the Metadata API in `app/layout.jsx`, not from a
  hand-written `<head>`. This produced every tag in the head template,
  checked in the built HTML:

  ```jsx
  import '../landing/tokens.css';
  import './styles.css';

  // With the domain known. Until it is, delete metadataBase, alternates,
  // openGraph.url and openGraph.images; the rest stays.
  export const metadata = {
    metadataBase: new URL('https://example.com'),
    title: 'Fern & Field: [PLACEHOLDER: what it is, for whom]',
    description: '[PLACEHOLDER: one or two sentences from copy.md]',
    alternates: { canonical: '/' },
    openGraph: { type: 'website', url: '/', title: 'Fern & Field', images: [{ url: '/og.png', width: 1200, height: 630, alt: '...' }] },
    twitter: { card: 'summary_large_image' },
    icons: { icon: [{ url: '/icon-96.png', type: 'image/png', sizes: '96x96' }], apple: '/icon-180.png' },
  };
  export const viewport = { themeColor: '#f4efe4' };
  ```

  Strings in `metadata` are plain text; Next escapes them. With the
  domain unknown, remove `metadataBase`, `alternates`, `openGraph.url` and
  `openGraph.images` rather than writing a placeholder URL: the test build
  without them emitted the title, description, `og:type`, `og:title`,
  `og:description` and `twitter:card` and no URL tags.
- Fonts: `next/font` computes a metric-matched fallback automatically, but it
  renames the family to a generated name, so the stacks in `tokens.css` no
  longer match and the tokens would have to change. Keep the plain
  `@font-face` rules from `performance.md` in the global stylesheet (fonts in
  `public/fonts/`) and preload from the layout:

  ```jsx
  import { preload } from 'react-dom';

  export default function RootLayout({ children }) {
    preload('/fonts/face-latin-400-normal.woff2', { as: 'font', type: 'font/woff2', crossOrigin: '' });
    return (<html lang="en"><body>{children}</body></html>);
  }
  ```

  The test build emitted `<link rel="preload" ... as="font" crossorigin="" type="font/woff2">`.
- The form is a plain `<form action="/api/lead" method="post">` in a server
  component. A static export has no server of its own, so the endpoint is
  external, as in plain HTML. Load `form.js` from `public/` with a
  `<script src="/form.js" defer>`. If you rewrite it as a client component,
  keep its markup contract exactly (`disabled` on the button while sending,
  `data-state` and the message on `.form-status`), because
  `landing-skills:landing-motion` and `landing-skills:landing-launch` rely
  on it.
- The result pages are `app/thanks/page.jsx` and `app/form-error/page.jsx`,
  each exporting `metadata` with `robots: { index: false }`.
- Weight: the test export shipped about 175 KB gzipped of framework scripts
  for a page with no interactive components. The HTML is complete without
  them, so the page still works with JavaScript off, but the weight counts
  against the budget. Tell the user when the stack choice is open.
- Use JSX attribute names: `htmlFor`, `autoComplete`, `fetchPriority`,
  `tabIndex`, `aria-*` and `data-*` as written.

## Vite with React

Checked with Vite 8.3.2, `@vitejs/plugin-react` 6.1.1 and React 19.

A React app rendered into `<div id="root">` is blank with JavaScript off;
one test build lost its headline, price and call to action that way. For a
landing page, write the static page in `index.html` and mount React only
where the page needs it, as islands:

```html
<!-- index.html: the whole page is here as HTML -->
<link rel="stylesheet" href="/landing/tokens.css">
<link rel="stylesheet" href="/src/styles.css">
<script type="module" src="/src/islands.jsx"></script>
...
<div id="viewer">
  <img src="/poster.jpg" width="1600" height="1000" alt="...">
</div>
```

```jsx
// src/islands.jsx
const viewer = document.getElementById('viewer');
if (viewer) {
  // React and the island load in their own chunks, after the static page has rendered.
  Promise.all([import('react-dom/client'), import('./Viewer.jsx')]).then(
    ([{ createRoot }, { default: Viewer }]) => createRoot(viewer).render(<Viewer />),
  );
}
```

- Vite resolves the root-relative `/landing/tokens.css` link from the
  project root and bundles it with the page CSS; only the bundle is served,
  not the `landing/` folder.
- In the test build the entry script was 1.4 KB gzipped and React DOM
  (68 KB gzipped) loaded only because an island existed. With JavaScript
  off the poster image and every section rendered.
- The island's static content (the poster) is what shows until React
  replaces it, so give it the final size; `landing-skills:landing-motion`
  supplies the island itself.
- Extra HTML pages such as the two result pages need listing as build inputs:

  ```js
  // vite.config.js
  import { defineConfig } from 'vite';
  import react from '@vitejs/plugin-react';

  export default defineConfig({
    plugins: [react()],
    build: { rollupOptions: { input: { main: 'index.html', thanks: 'thanks/index.html', formError: 'form-error/index.html' } } },
  });
  ```

- Do not raise `build.chunkSizeWarningLimit` to silence a large-chunk
  warning; split the code instead.

## Tailwind v4

Checked with `tailwindcss` 4.3.3 and the standalone `@tailwindcss/cli`. In
Vite and Astro projects Tailwind runs through `@tailwindcss/vite`; in Next
through `@tailwindcss/postcss`. Both packages exist at 4.3.3; their setup
was not exercised here, so follow the project's existing config.

Tailwind v4 keeps its theme in CSS variables with namespaces that match
most token prefixes: `--color-*`, `--font-*`, `--text-*`, `--radius-*`, and
`--spacing-*` for spacing. Point those namespaces at the existing tokens
without redefining any value:

```css
@import "tailwindcss";
@import "../landing/tokens.css";

/* `reference` stops Tailwind from emitting its own copies of these
   variables; `inline` makes each utility use var(--token) directly. */
@theme inline reference {
  --color-bg: var(--color-bg);
  --color-ink: var(--color-ink);
  --color-action: var(--color-action);
  --font-display: var(--font-display);
  --font-text: var(--font-text);
  --text-base: var(--text-base);
  --text-display: var(--text-display);
  --spacing-section: var(--space-section-open);
  --spacing-gutter: var(--space-gutter);
  --radius-control: var(--radius-control);
}

@layer base {
  body { background: var(--color-bg); color: var(--color-ink); font-family: var(--font-text); font-size: var(--text-base); }
}
```

Why `inline reference`: with a plain `@theme` block, or `@theme inline`
alone, the compiled CSS contained `--color-bg: var(--color-bg);` in
Tailwind's theme layer, a second declaration of the token that only works
because the unlayered `tokens.css` happens to win the cascade. With
`inline reference` the compiled CSS declares no token at all and the
utilities read the tokens directly: in the test, `bg-bg` compiled to
`background-color: var(--color-bg)` and `py-section` to
`padding-block: var(--space-section-open)`, and the browser showed the
token values.

- List one line per token you use; names map one to one except
  `--space-*`, which becomes `--spacing-*`.
- Set the body font in `@layer base` as above. Tailwind's preflight
  otherwise applies its own sans stack.
- The section and container rule in Tailwind: `py-*` on the section, the
  width on the inner element. Utilities resolve by their order in the
  generated CSS, not in the class list; in the test, `p-6 py-section` kept
  the section padding in either class order because Tailwind emits `py-*`
  after `p-*`. The bug comes back through hand-written CSS: an unlayered
  class with a `padding` shorthand beats every utility, because unlayered
  styles win over Tailwind's layers. So keep shorthands off layout elements
  here too.

  ```html
  <section class="py-section">
    <div class="mx-auto w-[min(100%-2*var(--space-gutter),var(--space-content-max))]">...</div>
  </section>
  ```

  Measured at 360 px wide with a 1.5rem gutter token, the inner element
  was 312 px wide.
- Projects on Tailwind v3 (`tailwind.config.js`, `@tailwind base;`) are not
  covered here.
