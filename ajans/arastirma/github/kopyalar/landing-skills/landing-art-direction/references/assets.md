# Imagery and assets

Read this at step 7 of the process. For every visual in the section map,
record what it shows, its aspect ratio, and whether the asset exists.

## Contents

- What a landing image is for
- Photography
- Product shots and interface
- Illustration
- Icons
- Logo and wordmark
- When the assets do not exist yet
- The asset request list

## What a landing image is for

An image on a landing page shows the real thing: the product, the place, the
work, the people who do it. On 30 well-made pages studied, the hero showed
product UI, the product photographed, a video of it, a WebGL scene, or only
type. None used stock photos of people at laptops.

So ask of each planned image: what real thing does it show, and does the
visitor need to see it to act? If the answer is "it fills the space", cut it
and let the layout breathe.

## Photography

- Use the client's own photographs. Record for each: subject, crop, aspect
  ratio, and where it sits.
- People appear only when they are the client's real people, photographed in
  light and colour that match the palette (Dawn uses one warm-lit portrait in
  its brown palette). Get written permission to publish faces and names.
- Never stock photos of people, and never AI-generated people. Visitors read
  them as invented, and on a page that asks for trust they cost more than
  they add.
- Crop on purpose. Pick the ratio from the layout (21:9 or 16:9 for a
  full-bleed band, 4:5 or 3:4 for portraits and objects beside text, 1:1 for
  grids of equal items), and keep the same ratio for images that sit
  together.
- If the palette was estimated from a description, check it against the
  photographs when they arrive and adjust the anchor values.
- When the product's defining feature is visual (colourways, finishes,
  materials), give it room: a photo or a large sample, never a small dot.

## Product shots and interface

For software, the strongest visual is the product doing the job the section
describes.

- Real screenshots when they exist. Crop to the part the section talks about
  rather than showing the whole window at a size where nothing is legible.
- Browser-window chrome (three grey dots and a bar) around a screenshot adds
  nothing. Show the UI itself, on the page's ground or a surface.
- When no screenshot exists, use a labelled placeholder at the screenshot's
  ratio (next section). Do not draw an invented screen and present it as the
  product.
- You may build a small UI fragment in HTML to explain how something works
  (Lightfield builds its page from product-realistic fragments). Then caption
  it as an illustration, fill it with content specific to the audience's real
  work rather than generic filler names and round sums, and never present
  invented data as a real customer's account.
- When the brief asks for a stand-in before the real asset exists (a model
  built from simple shapes, a drawing of the product), build it to the real object's
  proportions and layout, in palette colours, so it reads as the product.
  A random pattern of colours reads as noise. Keep it replaceable by the
  real asset.

## Illustration

- Use illustration when it can carry the brand on its own: PostHog's hedgehog
  system is the example from the research. It is drawn for the brand, in one
  hand, with light and dark variants when the page has both themes.
- Do not use flat stock illustration packs (people with oversized limbs,
  generic devices). They now read as filler.
- Abstract 3D blobs, gradient orbs and floating geometric shapes are
  decoration with no meaning. If the page has no image, use type and space.
- Technical drawings, plans, diagrams and maps are illustration too, and
  often the most honest kind: a bridge engineer has elevations, a bicycle
  frame builder has a geometry chart, a walking-tour guide has a route map.

## Icons

- One icon set, one stroke weight, one size scale, or no icons. Many items
  read better with a word or a number than with an icon.
- No emoji as icons or bullets.
- No sparkles icon to mean "AI" or "premium". Name the feature in words.
- A thin line icon at the top of every card is the shadcn and Lucide default
  look. If you use icons, they should mean something specific to the item.
- Icons that carry meaning need 3:1 contrast against their ground.

## Logo and wordmark

- Use the client's logo file. Ask for it as SVG.
- When there is only a name, set it as a wordmark in the display face with
  one considered detail taken from the direction (a weight, a cut, a rule, a
  character from the subject). Plain uppercase text in the body face reads
  as a missing logo.
- Do not invent a symbol and present it as the client's mark. Offer the
  wordmark as a proposal and list a real logo in the asset request.

## When the assets do not exist yet

This is common: the client will send photos later, or the product has no
screenshots yet. The correct behaviour is a labelled placeholder at the right
aspect ratio, plus a request list. It is not a failure.

A good placeholder:

- has the final aspect ratio, so the layout is judged at its real proportions
  and nothing shifts when the photo arrives;
- uses the palette's surface colour and a fine rule, so the page still reads
  as designed;
- says what goes there in plain words, in the visible text and in the
  accessible name;
- uses the same `[PLACEHOLDER: ...]` form the copy uses, so one search finds
  every gap before launch.

```html
<figure class="placeholder" style="--ratio: 4 / 5">
  <p>[PLACEHOLDER: photograph, the bench from above with tools laid out, 4:5, at least 1600 px wide]</p>
</figure>
```

```css
.placeholder {
  aspect-ratio: var(--ratio);
  display: grid;
  place-items: center;
  margin: 0;
  padding: var(--space-4);
  background: var(--color-surface);
  border: 1px solid var(--color-line);
  color: var(--color-ink-muted);
  font-size: var(--text-sm);
  text-align: center;
}
```

Adapt the token names to the ones in your `tokens.css`. The text inside has
to meet 4.5:1 on the surface colour, like any other text.

Keep placeholders to the visuals the section map needs. A page that is half
empty boxes means the layout depends on images that do not exist; plan some
sections that work with type alone, so the page stands while the photos are
pending.

When the client will never have photographs, plan the page for type,
drawings or diagrams instead, and do not leave permanent placeholders.

## The asset request list

End the `Imagery and assets` section of `direction.md` with a numbered list
the user can forward to the client as written. For each asset:

1. What it shows, in one line a photographer could act on.
2. Orientation and ratio, and minimum width in pixels. Starting values from
   judgement, not measured: at least 2400 px wide for a full-bleed band, at
   least 1200 px for a half-width image.
3. Any permission needed: people's consent to publish their face and name,
   rights to a third party's logo.

Example for a different client:

1. Photograph of the workshop bench from above, tools laid out, landscape
   16:9, at least 2400 px wide.
2. A portrait of each of the two founders at the bench, 4:5, at least 1200 px
   wide, with written consent to publish.
3. The logo as SVG.
