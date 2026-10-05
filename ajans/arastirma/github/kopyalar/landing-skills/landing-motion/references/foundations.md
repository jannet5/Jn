# Foundations

## Contents

- The decision ladder
- Roles, and one signature moment
- Duration
- Easing
- Springs
- Choreography and stagger
- Motion tokens
- Compositor-only properties
- What reads as generated, and what to do instead
- Before and after

## The decision ladder

Climb only as far as the effect needs:

1. **CSS.** Transitions for state changes, `@starting-style` for elements
   entering the DOM, `position: sticky` for stacked or pinned sections,
   `animation-timeline: scroll()` and `view()` for scroll-linked effects where
   supported (Chromium 115, Safari 26, not stable Firefox: gate them with
   `@supports` and keep a static fallback).
2. **A small script.** `IntersectionObserver` for "when this enters view",
   a `requestAnimationFrame` loop for anything that follows the pointer.
3. **GSAP or Motion.** Sequencing many elements on one timeline, pinned and
   scrubbed timelines, SplitText, Draggable, physics-feeling springs in React.
4. **WebGL (OGL, Three.js, React Three Fiber).** Only for pixels CSS cannot
   produce: a 3D object, a shader field, particles, image distortion.

Each rung adds bytes, main-thread work, and failure modes. If the effect is
the same on a lower rung, use the lower rung.

## Roles, and one signature moment

Give every animation one of these roles before you write it:

| Role | Examples | Character |
|---|---|---|
| Feedback | hover, press, focus, toggle, form submit | Fast, small, always kept under reduced motion |
| Entrance | hero sequence, a figure revealing once | Medium, eases out, runs once |
| Scroll-linked | progress bar, pinned sequence, exploded view | Follows the scroll, linear mapping, no easing of its own |
| Ambient | shader field, slow particles | Slow, low contrast, stops off screen, removed under reduced motion |
| Interactive 3D | rotate a product, drag a slider | Under the visitor's hand, keyboard path required |

Pick one signature moment for the page and give it the most travel and the
longest duration. Everything else is smaller. A page where every section has
an entrance has no signature moment.

## Duration

These are judgement values, assembled from practitioner guidance (UI
feedback under 300 ms, 180 ms reading as quicker than 400 ms, longer for
marketing moments), not measured thresholds. Use them as starting values,
then adjust by eye on a real device:

| Motion | Duration |
|---|---|
| Press state | 80 to 120 ms |
| Hover, focus, toggle feedback | 150 to 200 ms |
| UI element entering (menu, toast, disclosure) | 200 to 300 ms |
| Secondary element in a hero entrance | 400 to 500 ms |
| The signature hero moment | 600 to 900 ms total |
| Leaving | about two thirds of the matching entrance |

UI feedback stays under 300 ms because it answers an action, and a slow
answer reads as lag: 180 ms feels more responsive than 400 ms. Marketing
moments may run longer because they are watched, not waited on. Do not
animate actions triggered by the keyboard, such as moving focus or opening a
menu with Enter: keyboard users are moving fast and the motion is in their
way.

## Easing

- **Entering: ease-out.** Fast start, gentle stop. The element arrives and
  settles. `cubic-bezier(0.2, 0, 0, 1)` is a strong decelerate curve.
- **Leaving: ease-in.** Gentle start, fast exit. `cubic-bezier(0.3, 0, 0.8, 0.15)`.
- **Scroll-linked: linear** (`ease: "none"` in GSAP). The scroll position is
  already the visitor's own easing; adding another makes the element lag
  behind their hand.
- **Avoid** the browser default `ease` and `ease-in-out` on entrances, and
  `linear` on anything that is not scroll-linked or a continuous loop. Avoid
  bounce easing: it is on the list of generated-page tells.

## Springs

A spring curve overshoots slightly and settles. Use it for things the visitor
moved or toggled (a switch, a dragged card returning), where the overshoot
reads as physical. Do not use it on entrances of text.

In CSS, `linear()` approximates a spring (Chrome 113, Firefox 112, Safari
17.2). Give older browsers a `cubic-bezier` fallback through `@supports`,
as in the tokens below. In GSAP, `ease: "back.out(1.2)"` is the closest
built-in; in Motion, use `type: "spring"`. Keep the overshoot small: the
`linear()` token below peaks at 4 percent past the end.

## Choreography and stagger

- **One lead element.** In an entrance, one thing moves first and furthest
  (usually the headline or the product). The rest follow with less travel.
- **Overlap, do not queue.** Start the next element before the previous one
  ends (GSAP position parameter `"-=0.3"`, or CSS delays shorter than the
  durations). Queued animations make the visitor wait.
- **Small stagger, short lists.** 40 to 80 ms between items, and only for
  items that form one group, such as the lines of one headline. Do not
  stagger every list on the page.
- **Total wait.** The last element of a hero entrance should be settled
  within about a second of first paint. Past that, the visitor is waiting to
  read.

## Motion tokens

Put these in your stylesheet, beside `tokens.css`, not inside it. Tested in
Chromium and Firefox; the `linear()` value applies in both.

```css
:root {
  --motion-dur-press: 100ms;
  --motion-dur-feedback: 180ms;
  --motion-dur-enter: 450ms;
  --motion-dur-hero: 700ms;
  --motion-ease-out: cubic-bezier(0.2, 0, 0, 1);
  --motion-ease-in: cubic-bezier(0.3, 0, 0.8, 0.15);
  --motion-ease-spring: cubic-bezier(0.34, 1.2, 0.64, 1);
}
@supports (transition-timing-function: linear(0, 1)) {
  :root {
    --motion-ease-spring: linear(0, 0.2 6%, 0.58 15%, 0.86 25%, 1.01 36%, 1.04 45%, 1.01 60%, 1);
  }
}

/* Usage: enter with the spring, leave with ease-in and a shorter duration. */
.toast { transition: transform var(--motion-dur-enter) var(--motion-ease-spring) }
.toast.leaving { transition: transform var(--motion-dur-feedback) var(--motion-ease-in) }
```

## Compositor-only properties

Animate `transform` and `opacity`. They can run on the compositor without
layout or paint. Animating `top`, `left`, `width`, `height`, `margin`, or
`padding` triggers layout on every frame, janks on phones, and can count as
layout shift. `clip-path` repaints but does not lay out; it is acceptable for
a one-off reveal.

`will-change` is a last resort. Set it just before a heavy animation and
remove it after. Applied to many elements it costs memory and creates a
stacking context on each one.

## What reads as generated, and what to do instead

This list is a synthesis of practitioner opinion, not a measured dataset.
The pattern that unites it: a technique applied uniformly, without a role.
Every technique below is fine when it has a role, differs by role, and has a
reduced-motion variant.

| Pattern | Why it reads as generated | Instead |
|---|---|---|
| Every section fades up 20 to 30 px with the same easing and 0.6 to 1 s | Identical reveal on all content tells the visitor nothing | Most sections static. One entrance for the hero; scroll-linked motion only where it explains something |
| A reveal library's attribute on every block (AOS and similar) | Library default shipped unchanged | Remove it. Write the one or two entrances the intent names |
| Same stagger on every list and grid | Uniform, role-less | Stagger only the lines of the signature headline, or nothing |
| Every card lifts or grows on hover | Hover lift and scale on cards is a catalogued tell | Hover changes what the card offers: underline the link, change the border colour. Press state on the real control |
| Count-up on a round, unsourced number | A count-up on an unsourced figure is a catalogued tell | Count only a real figure with a named source, final value in the HTML, or show it static |
| Bounce easing | Catalogued tell | `--motion-ease-out`, or a spring with small overshoot on toggles |
| Drop-in effect components (beams, shiny text, meteors, number tickers) with stock colours | Recognisable library defaults | Motion that shows the product working |
| Pinned panel taller than a short screen | The last row is cut off and cannot be reached while pinned | Size the panel with `100svh`, check it at 360x640 and 1280x600, and keep its content inside that height; if it cannot fit, do not pin |
| Scroll-jacking with no content reason | Takes the scroll away for decoration | Native scroll; pin only a sequence that needs it, with a short runway |
| Tilt on every card | Uniform effect | Tilt one object that benefits from showing depth, or none |
| Reveals that replay every time you scroll back | Motion repeats with no new information | Run entrances once (`unobserve` after the first intersection) |
| Text moving while being read | Parallax or drift on paragraphs | Move only decorative layers and images |
| Gradient blobs drifting in the background | Ambient decoration with no subject | A texture or image from the brand, or a plain background |

## Before and after

Before: a generated page, the same reveal on every section.

```html
<section class="features reveal">...</section>
<section class="pricing reveal">...</section>
<section class="faq reveal">...</section>
<style>
  .reveal { opacity: 0; transform: translateY(24px); transition: all 0.8s ease-in-out }
  .reveal.visible { opacity: 1; transform: none }
</style>
```

Problems: every section has the same role-less entrance; `transition: all`
animates whatever changes; `ease-in-out` on an entrance; content is hidden
until a script runs, so with JavaScript off the page is blank below the hero.

After: the intent named one moment, a sequence of product states while the
visitor scrolls through "how it works". Only that section moves (sticky
storytelling in `references/scroll.md`); features, pricing, and FAQ are
static; buttons get feedback states from `references/interaction.md`.
