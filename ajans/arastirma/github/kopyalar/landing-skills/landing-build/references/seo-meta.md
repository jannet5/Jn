# Head, meta tags and structured data

Read this when writing the `<head>`. All four unguided test builds shipped without
social tags, three without a favicon, and none with structured data, so a
shared link showed a bare URL and search results had nothing to work with.

## Contents

- The head, in order
- Title and description
- Canonical URL
- Open Graph and social cards
- When the production domain is not known yet
- Favicon set
- Making the social card and icons
- Structured data
- robots.txt and sitemap
- The result pages

## The head, in order

This head passed an HTML validator and was rendered in a browser. The
business in it is invented for the example. Replace every value. A value
you do not have goes in the hand-off as a gap, never as an invented value;
for a missing production domain, follow "When the production domain is not
known yet" below.

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Larchmont Bread School: weekend sourdough classes for home bakers</title>
  <meta name="description" content="Two-day sourdough classes for eight people at a time. Leave your email to hear when the next term opens.">
  <link rel="canonical" href="https://bread.example/">
  <meta name="theme-color" content="#f4efe4">

  <link rel="icon" href="/icon-96.png" type="image/png" sizes="96x96">
  <link rel="apple-touch-icon" href="/icon-180.png">

  <meta property="og:type" content="website">
  <meta property="og:url" content="https://bread.example/">
  <meta property="og:site_name" content="Larchmont Bread School">
  <meta property="og:title" content="Weekend sourdough classes at Larchmont Bread School">
  <meta property="og:description" content="Eight bakers, two days, three loaves to take home. Hear first when the next term opens.">
  <meta property="og:image" content="https://bread.example/og.png">
  <meta property="og:image:width" content="1200">
  <meta property="og:image:height" content="630">
  <meta property="og:image:alt" content="Students shaping dough at a long floured table">
  <meta name="twitter:card" content="summary_large_image">

  <link rel="preload" href="/fonts/source-serif-4-latin-400-normal.woff2" as="font" type="font/woff2" crossorigin>
  <link rel="stylesheet" href="/tokens.css">
  <link rel="stylesheet" href="/styles.css">
  <script type="application/ld+json">
  {
    "@context": "https://schema.org",
    "@type": "WebSite",
    "name": "Larchmont Bread School",
    "url": "https://bread.example/"
  }
  </script>
  <script src="/form.js" defer></script>
</head>
```

- `lang` is the page language from the brief (`es-ES` for Spanish as written in Spain,
  `en` for English). Screen readers pick their pronunciation from it.
- `charset` and `viewport` come first; without the viewport tag phones
  render the desktop layout zoomed out.
- `theme-color` is the page's background or brand colour as a hex value
  from the tokens' hex set. It is the one place a token's colour is typed
  as a literal, because a meta tag cannot read a custom property. Copy the
  hex of `--color-bg` (or the token the direction names), and change it
  whenever that token changes.

## Title and description

- Title: the product's name and what it is or who it is for, in words from
  `copy.md`. Put the name first so it survives truncation. A starter title
  ("Vite + React", "Create Next App", "My App") is a catalogued tell and
  the detector flags it.
- Description: one or two plain sentences that a stranger could repeat: what
  it is, for whom, and the action. Same language as the page. Search
  engines may show their own excerpt instead; write it anyway, since it is
  the summary you control.
- The description is copy, so the rules of `landing-skills:landing-copy`
  apply: plain words, a named thing or a number, no stock superlatives.

## Canonical URL

An absolute URL to the page's production address, ending the same way the
host serves it (`https://bread.example/`). It tells search engines which
address is the real one when the page is also reachable through a preview
URL. If the production domain is not known yet, leave the tag out (see
"When the production domain is not known yet").

## Open Graph and social cards

The Open Graph protocol requires four properties: `og:title`, `og:type`,
`og:image`, `og:url`. Add `og:description` and `og:site_name`.

- `og:image` is an absolute URL. 1200 by 630 px is a common size for the
  large preview; give `og:image:width` and `og:image:height` so a
  crawler knows the size without downloading the file.
- `og:image:alt` is, in the protocol's words, "a description of what is in
  the image (not a caption)".
- The image shows the real product, the product's name set in the page's
  type, or both. If no asset exists yet, make the card from the wordmark and
  the h1 in the direction's typeface and colours, and list the photograph
  as wanted. A card set in a system font looks like another site.
- `twitter:card` set to `summary_large_image` asks for the large preview.
- Check by pasting the production URL into a link preview after launch;
  previews cannot fetch `localhost`.

## When the production domain is not known yet

One rule: never put a bracketed placeholder or any other made-up host
inside a URL attribute. `https://[PLACEHOLDER: production domain]/` is not
a valid URL; Astro refuses it as `site` ("Invalid URL", tested), and a
crawler or a validator would treat it as broken.

Instead, until the user gives the domain:

- Leave out the tags that need an absolute URL: `link rel="canonical"`,
  `og:url`, `og:image` with its `width`, `height` and `alt` (the protocol's
  examples and link previews expect an absolute URL), the `twitter:image`
  if you added one, the `url` and `image` properties in JSON-LD, the
  `Sitemap:` line in `robots.txt`, and `sitemap.xml` itself.
- Keep everything that does not: `title`, `description`, `og:type`,
  `og:title`, `og:description`, `og:site_name`, `twitter:card`, favicons,
  `theme-color`.
- Make `og.png` anyway (see "Making the social card and icons") and put it
  in the output folder, so adding the tag later is one line.
- List in the hand-off: "Production domain unknown. Left out: canonical,
  og:url, og:image (file ready at /og.png), sitemap. Add them once the
  domain is known." `landing-skills:landing-launch` sets the domain when it
  deploys.

In a framework, leave the site setting unset and guard the tags; see
`adapters.md`.

## Favicon set

```html
<link rel="icon" href="/icon-96.png" type="image/png" sizes="96x96">
<link rel="apple-touch-icon" href="/icon-180.png">
```

- Google Search accepts BMP, GIF, ICO, PNG, JPEG, PPM and TIFF favicons, not
  SVG, and wants a square image larger than 48 by 48 px. A 96 by 96 PNG
  covers search and browser tabs; render the apple-touch-icon at 180 by
  180. Add an SVG icon only when the client has an SVG logo; text in an SVG
  favicon is drawn in a system font, not the page's.
- Make the icon from the wordmark or its first letter, in the direction's
  typeface and colours. If the client has a logo file, use it.

## Making the social card and icons

Render them from HTML with the page's own tokens and loaded font, then
capture at the exact size. Keep the source pages in `landing/`, which is
not served to visitors, and serve the project root locally while you
capture.

`landing/og-card.html` (invented content; use the h1 and wordmark from
`copy.md`, and the display face and its axes from `direction.md`):

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Social card</title>
  <link rel="stylesheet" href="tokens.css">
  <style>
    @font-face { font-family: "Archivo"; src: url("../public/fonts/archivo-latin-wdth-normal.woff2") format("woff2"); font-weight: 100 900; font-stretch: 62% 125%; }
    html, body { margin: 0; inline-size: 1200px; block-size: 630px; overflow: hidden; }
    body { display: grid; align-content: space-between; padding: 72px; box-sizing: border-box;
      background: var(--color-bg); color: var(--color-ink); font-family: var(--font-text); }
    h1 { margin: 0; font-family: var(--font-display); font-size: 88px; line-height: 0.95; font-weight: 700; font-stretch: 75%; max-inline-size: 16ch; }
    p { margin: 0; font-size: 32px; color: var(--color-ink-muted); }
  </style>
</head>
<body>
  <p>Larchmont Bread School</p>
  <h1>Weekend sourdough classes for home bakers</h1>
</body>
</html>
```

`landing/icon.html` fills whatever viewport it is captured at:

```html
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Icon</title>
  <link rel="stylesheet" href="tokens.css">
  <style>
    @font-face { font-family: "Archivo"; src: url("../public/fonts/archivo-latin-wdth-normal.woff2") format("woff2"); font-weight: 100 900; font-stretch: 62% 125%; }
    html, body { margin: 0; block-size: 100%; }
    body { display: grid; place-items: center; background: var(--color-action); color: var(--color-on-action);
      font-family: var(--font-display); font-weight: 700; font-size: 70vmin; line-height: 1; }
  </style>
</head>
<body>L</body>
</html>
```

The sizes in these two files are image sizes, not page styles, so plain
pixel values are fine there; colours and families still come from the
tokens. The `@font-face` path points at the font file you already
self-host (in a framework, its `public/fonts/`).

`capture.mjs` (`npm i -D playwright`, then `npx playwright install
chromium`; tested with Playwright 1.63.0). It refuses to write a card when
the page did not load or the font is missing, which is how a card ends up
set in Arial:

```js
// capture.mjs: render an HTML card to PNG at an exact size, after its fonts have loaded.
// Usage: node capture.mjs <url> <out.png> <width> <height> "<font to require>"
import { chromium } from 'playwright';

const [url, out, width, height, font] = process.argv.slice(2);
const browser = await chromium.launch();
const page = await browser.newPage({ viewport: { width: Number(width), height: Number(height) } });
const response = await page.goto(url);
if (!response.ok()) throw new Error(`${url} answered ${response.status()}`);
await page.evaluate(() => document.fonts.ready);
if (font && !(await page.evaluate((f) => document.fonts.check(f), font))) {
  throw new Error(`Font not loaded: ${font}. Check the @font-face url in the card.`);
}
await page.screenshot({ path: out });
await browser.close();
console.log(`${out}: ${width}x${height}`);
```

```bash
python3 -m http.server 8000      # from the project root, in a second terminal
node capture.mjs http://localhost:8000/landing/og-card.html public/og.png 1200 630 '700 88px "Archivo"'
node capture.mjs http://localhost:8000/landing/icon.html public/icon-96.png 96 96 '700 60px "Archivo"'
node capture.mjs http://localhost:8000/landing/icon.html public/icon-180.png 180 180 '700 60px "Archivo"'
```

Tested: the three files came out at exactly 1200 by 630, 96 by 96 and 180
by 180 in the loaded face; a missing card answered 404 and a broken font
path stopped the script with "Font not loaded" instead of writing an
image. Look at each PNG before shipping it.

## Structured data

Use JSON-LD in a `<script type="application/ld+json">`, the format Google
recommends. Mark up only what is visible on the page. Google's guidelines
say not to add structured data "about information that is not visible to the
user, even if the information is accurate". So no rating, review count or
price in JSON-LD that the page does not show, and never a rating you do not
have.

Pick by landing type:

| Landing | Types | Notes |
|---|---|---|
| any home page | `WebSite` with `name` and `url` | Google uses it to choose the site name; home page only |
| local service (studio, trade) | `LocalBusiness` or a fitting subtype | there is no `ArchitecturalService` type, and `ProfessionalService` is deprecated |
| physical product, pre-order or waitlist | `Product` with an `Offer` | `availability` says the real state |
| SaaS | `SoftwareApplication` with an `Offer` | price as on the page |
| event | `Event` | date, place, and the real status |

Local service (invented: a bicycle repair workshop in one city):

```html
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "LocalBusiness",
  "name": "Taller Ribera",
  "description": "Reparación y puesta a punto de bicicletas en Valencia, con recogida a domicilio.",
  "url": "https://ribera.example/",
  "telephone": "[PLACEHOLDER: public phone number]",
  "address": {
    "@type": "PostalAddress",
    "addressLocality": "Valencia",
    "addressCountry": "ES"
  },
  "areaServed": "Valencia"
}
</script>
```

Replace every placeholder before launch or remove the property; do not ship
JSON-LD that contains placeholder text. With the domain unknown, leave out
`url` and `image`.

Product taking pre-orders (invented: a sailing jacket):

```html
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "Product",
  "name": "Tern 2",
  "description": "A waterproof sailing jacket with a fleece-lined collar and a hood that folds into it.",
  "image": "https://tern.example/img/tern-2.jpg",
  "offers": {
    "@type": "Offer",
    "price": "340.00",
    "priceCurrency": "EUR",
    "availability": "https://schema.org/PreOrder",
    "url": "https://tern.example/"
  }
}
</script>
```

For a waitlist with no sale open, leave the `Offer` out until there is one.

## robots.txt and sitemap

A one-page site is found without either, but both are cheap and the review
checks for them. Both need the production domain; while it is unknown,
ship `robots.txt` without the `Sitemap:` line and no `sitemap.xml`:

```text
# public/robots.txt
User-agent: *
Allow: /
Sitemap: https://bread.example/sitemap.xml
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url><loc>https://bread.example/</loc></url>
</urlset>
```

## The result pages

The two pages the form endpoint redirects to, `/thanks/` and `/form-error/`
(see `forms-a11y.md`), each get their own short title, the same
stylesheets, a link back home, and `<meta name="robots" content="noindex">`
so they do not show up in search. Leave both out of the sitemap.
