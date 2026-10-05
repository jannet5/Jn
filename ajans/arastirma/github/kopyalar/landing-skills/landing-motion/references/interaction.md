# Interaction

## Contents

- Rules for interaction
- Hover and press states
- Form-submit feedback
- Magnetic button label
- Cursor follower
- Tilt
- Drag comparison (native range input)
- Stepped dial (pointer, keyboard, or scroll)
- Same-document view transition
- Cross-document view transition

## Rules for interaction

- **Feedback is kept under reduced motion.** Colour changes, press states,
  and focus rings are feedback, not decoration. They stay, unless the direction says otherwise.
- **Pointer flourishes only for fine pointers.** Magnetic elements, cursor
  followers, and tilt run only under `(hover: hover) and (pointer: fine)`
  and `prefers-reduced-motion: no-preference`.
- **Never hide the native cursor**, never hide focus rings, and keep the
  native cursor on text and inputs. A follower decorates the cursor; it does
  not replace it.
- **The hit target stays where the visitor aimed.** A magnetic effect moves
  the label inside the button, not the button itself.
- **Every drag has a keyboard path.** The easiest way is a native control
  that already has one.
- **No flourishes on controls used many times in a row.** A pricing toggle
  pressed once can animate; a quantity stepper pressed ten times should not.
- **Extend what the build wrote.** `landing-skills:landing-build` already
  writes `.button` with hover, `:active`, `:focus-visible`, and `:disabled`
  states using the core tokens (`--color-action`, `--color-action-hover`,
  `--color-on-action`, `--color-ink`, `--radius-control`). Add timing to
  those states; do not redefine their colours, padding, or size. New
  components this skill adds use the BEM style the build uses
  (`.button--magnetic`, `.button__label`).

### Hover and press states

#### Use when

Always, on every link, button, and form control. This is the one motion
recipe every page gets, including pages whose intent is `none`.

#### Avoid when

Never skip it. Avoid only the generated version: cards that grow or lift on
hover, and `transition: all`.

#### Code

New to this skill: none. From `landing-skills:landing-build`, used unchanged: `.button` and its states.

```css
/* landing-build writes the states (hover colour, :active, :focus-visible, :disabled). Add only the timing. */
.button {
  transition:
    background-color 150ms cubic-bezier(0.2, 0, 0, 1),
    translate 100ms cubic-bezier(0.2, 0, 0, 1),
    opacity 150ms linear;
}
a:not(.button) { transition: color 150ms linear, text-decoration-color 150ms linear }
```

The colours come from the build's rules (`.button:hover` uses
`--color-action-hover`); this only makes the change take 150 ms instead of
snapping. The press is the build's `translate: 0 1px`, eased over 100 ms:
enough to feel, too little to misplace a click. The build writes the hover
rule inside `@media (hover: hover)` and keeps `:focus-visible`, `:active`,
and `:disabled` outside it; the transitions above work with that as it is.
If your page was not built by `landing-skills:landing-build`, write the
states the same way first.

Tested on top of the build's `.button` rules in Chromium and Firefox: the
background passes through an intermediate colour 60 ms into the hover and
ends on `--color-action-hover`; `:active` gives `translate: 0 1px`.

#### Reduced motion

Keep all of it by default. A 150 ms colour change and a 1 px press are
feedback. If the direction says to remove transitions under reduced motion,
follow it: removing a colour transition there is acceptable, though not
required. Do it per rule (`@media (prefers-reduced-motion: reduce) { .button
{ transition: none } }`), never with a global rule.

#### Cost

None worth measuring. Name the properties you transition; `transition: all`
animates layout properties by accident.

### Form-submit feedback

#### Use when

The page has a lead form. It is part of the feedback every page gets,
including pages whose intent is `none`. It extends the enhancement script
`form.js` that `landing-skills:landing-build` ships (it disables the button
while sending, writes the result into `.form-status` with `data-state`, and
keeps the input on failure); it does not replace its logic.

#### Avoid when

The form has no enhancement script and posts to a result page: the result
page is the feedback, and there is nothing to animate.

#### Code

New to this skill: none. From `landing-skills:landing-build`, used unchanged: `.lead-form`, `.form-status`, `.button`, and `form.js`.

Load it after `form.js`:

```html
<script src="/form.js" defer></script>
<script src="/form-feedback.js" defer></script>
```

```js
// form-feedback.js: loaded after landing-build's form.js. It watches the states form.js sets
// (button.disabled, status data-state) and adds to them; it does not change the submit logic.
for (const form of document.querySelectorAll('form.lead-form')) {
  const status = form.querySelector('.form-status');
  const button = form.querySelector('[type="submit"]');
  status.tabIndex = -1; // focusable by script only

  // Busy: form.js disables the button while sending. Mirror it as aria-busy for styling and assistive tech.
  new MutationObserver(() => form.setAttribute('aria-busy', String(button.disabled)))
    .observe(button, { attributes: true, attributeFilter: ['disabled'] });

  // Result: form.js sets data-state to "success" or "error". Move focus to the message.
  new MutationObserver(() => { if (status.dataset.state) status.focus(); })
    .observe(status, { attributes: true, attributeFilter: ['data-state'] });
}
```

```css
.lead-form .button {
  transition: background-color 150ms cubic-bezier(0.2, 0, 0, 1), opacity 150ms linear;
}
@keyframes busy-pulse { to { opacity: 0.45 } }
@keyframes status-in { from { opacity: 0; transform: translateY(4px) } }
@media (prefers-reduced-motion: no-preference) {
  .lead-form[aria-busy="true"] .button { animation: busy-pulse 700ms ease-in-out infinite alternate }
  .form-status[data-state] { animation: status-in 200ms cubic-bezier(0.2, 0, 0, 1) }
}
```

What the visitor gets: while sending, the button is disabled (the build's
`:disabled` style) and pulses gently, `aria-busy="true"` is on the form, and
the status line reads the form's `data-sending` text. On a result, the
message fades in and receives focus, so a keyboard user is on it and a
screen reader reads it; `.form-status` is already a polite live region, so
some screen readers may read it twice. On failure the visitor's input stays,
because `form.js` only resets the form on success.

Tested with the build's markup and `form.js` against a stand-in endpoint
held open by the test: while waiting, `aria-busy="true"`, button disabled,
"Sending…" shown, pulse running; after a 200 with `{"ok": true}`, focus on
the status, success text, form cleared; after a 500, focus on the status,
error text, email kept.

#### Reduced motion

No pulse and no fade; the busy state, the text, and the focus move remain.
They are feedback.

#### Cost

Two `MutationObserver`s per form, no library.

### Magnetic button label

#### Use when

One primary call to action on a desktop-heavy page with a playful or
crafted tone, where the button drawing toward the pointer suits the brand.

#### Avoid when

More than one or two buttons; forms; touch devices; a tone that is sober or
institutional.

#### Code

New to this skill: `.button--magnetic`, `.button__label`. From `landing-skills:landing-build`, used unchanged: `.button`.

The button stays in place; only its label follows the pointer, so the click
target never moves away from where the visitor aimed.

```html
<a class="button button--magnetic" href="/order"><span class="button__label">[Primary action]</span></a>
```

```css
.button--magnetic .button__label { display: inline-block }
```

```js
import { gsap } from 'gsap';
const mm = gsap.matchMedia();

mm.add('(prefers-reduced-motion: no-preference) and (hover: hover) and (pointer: fine)', () => {
  const cleanups = gsap.utils.toArray('.button--magnetic').map((btn) => {
    const label = btn.querySelector('.button__label');
    const x = gsap.quickTo(label, 'x', { duration: 0.4, ease: 'power3.out' });
    const y = gsap.quickTo(label, 'y', { duration: 0.4, ease: 'power3.out' });
    const PULL = 0.3; // fraction of the pointer offset the label follows
    const move = (e) => {
      const r = btn.getBoundingClientRect();
      x((e.clientX - (r.left + r.width / 2)) * PULL);
      y((e.clientY - (r.top + r.height / 2)) * PULL);
    };
    const leave = () => { x(0); y(0); };
    btn.addEventListener('pointermove', move);
    btn.addEventListener('pointerleave', leave);
    return () => { btn.removeEventListener('pointermove', move); btn.removeEventListener('pointerleave', leave); };
  });
  return () => cleanups.forEach((fn) => fn());
});
```

#### Reduced motion

The media query does not match; the button behaves like any other `.button`.

#### Cost

GSAP core. Two `quickTo` tweens per button, retargeted on each pointer move.

### Cursor follower

#### Use when

A portfolio or studio page where a ring following the cursor, growing over
links, is part of the visual identity named in the direction.

#### Avoid when

Product and conversion pages, where it adds nothing; touch devices; when it
would replace the native cursor.

#### Code

New to this skill: `.cursor-dot`, `.is-visible`, `.is-over-link`.

```css
.cursor-dot {
  position: fixed; top: 0; left: 0; width: 28px; height: 28px; margin: -14px 0 0 -14px;
  border: 1.5px solid currentColor; border-radius: 50%; pointer-events: none; z-index: 100;
  opacity: 0; transition: opacity 200ms, scale 200ms cubic-bezier(0.2, 0, 0, 1);
}
.cursor-dot.is-visible { opacity: 1 }
.cursor-dot.is-over-link { scale: 1.8 }
```

```js
import { gsap } from 'gsap';
const mm = gsap.matchMedia();

// Decoration on top of the native cursor, never a replacement.
mm.add('(prefers-reduced-motion: no-preference) and (hover: hover) and (pointer: fine)', () => {
  const dot = document.createElement('div');
  dot.className = 'cursor-dot';
  dot.setAttribute('aria-hidden', 'true');
  document.body.append(dot);
  const x = gsap.quickTo(dot, 'x', { duration: 0.35, ease: 'power3.out' });
  const y = gsap.quickTo(dot, 'y', { duration: 0.35, ease: 'power3.out' });
  const move = (e) => {
    x(e.clientX); y(e.clientY);
    dot.classList.add('is-visible');
    dot.classList.toggle('is-over-link', !!e.target.closest('a, button'));
  };
  const hide = () => dot.classList.remove('is-visible');
  addEventListener('pointermove', move);
  document.documentElement.addEventListener('pointerleave', hide);
  return () => { removeEventListener('pointermove', move); document.documentElement.removeEventListener('pointerleave', hide); dot.remove(); };
});
```

#### Reduced motion

Not created. It is continuous motion following every pointer movement.

#### Cost

GSAP core; a fixed element composited on its own layer; work on every
pointer move.

### Tilt

#### Use when

One object, such as a product card or a physical item, where tilting toward
the pointer shows that it has depth.

#### Avoid when

Applying it to every card (a catalogued generated pattern); cards that hold
text to read; touch devices.

#### Code

New to this skill: `.tilt-stage`, `.tilt-card`, `.is-tracking`.

```html
<div class="tilt-stage"><div class="tilt-card" aria-hidden="true">[PLACEHOLDER: product image]</div></div>
```

```css
.tilt-stage { perspective: 900px }
.tilt-card { transform-style: preserve-3d; transition: transform 400ms cubic-bezier(0.2, 0, 0, 1) }
.tilt-card.is-tracking { transition: transform 80ms linear }
```

```js
const tiltQuery = matchMedia('(prefers-reduced-motion: no-preference) and (hover: hover) and (pointer: fine)');
document.querySelectorAll('.tilt-card').forEach((card) => {
  const MAX = 8; // degrees
  card.addEventListener('pointermove', (e) => {
    if (!tiltQuery.matches) return;
    const r = card.getBoundingClientRect();
    const nx = (e.clientX - r.left) / r.width - 0.5;
    const ny = (e.clientY - r.top) / r.height - 0.5;
    card.classList.add('is-tracking');
    card.style.transform = `rotateX(${(-ny * MAX * 2).toFixed(2)}deg) rotateY(${(nx * MAX * 2).toFixed(2)}deg)`;
  });
  card.addEventListener('pointerleave', () => {
    card.classList.remove('is-tracking');
    card.style.transform = '';
  });
});
```

The tilt range (8 degrees each way here) is a judgement; start small and
increase only if the depth does not read.

#### Reduced motion

The handler returns early; the card stays flat.

#### Cost

No library. One `getBoundingClientRect` per pointer move over the card.

### Drag comparison (native range input)

#### Use when

A before and after: a raw and a processed photo, an old and a new
interface, a surface before and after treatment. The visitor drags a divider across two images.

#### Avoid when

The two states are better shown side by side at small sizes; you need free
two-dimensional dragging (GSAP Draggable or Motion's `drag` do that, but you
must then add a keyboard path yourself).

#### Code

New to this skill: `.compare`, `.after`.

A native `<input type="range">` gives dragging, arrow keys, and a screen
reader label for free.

```html
<div class="compare">
  <img src="/img/before.avif" alt="[Before: describe]">
  <img class="after" src="/img/after.avif" alt="[After: describe]">
  <input type="range" min="0" max="100" value="50" aria-label="Before and after comparison position">
</div>
```

```css
.compare { position: relative; aspect-ratio: 3/2; --pos: 50% }
.compare img { position: absolute; inset: 0; width: 100%; height: 100%; object-fit: cover }
.compare .after { clip-path: inset(0 0 0 var(--pos)) }
.compare input[type="range"] { position: absolute; inset: auto 0 -2.5rem 0; width: 100% }
```

```js
document.querySelectorAll('.compare').forEach((el) => {
  const input = el.querySelector('input[type="range"]');
  const set = () => el.style.setProperty('--pos', input.value + '%');
  input.addEventListener('input', set);
  set();
});
```

#### Reduced motion

Unchanged: the divider moves only under the visitor's hand.

#### Cost

No library. A `clip-path` repaint per input event.

### Stepped dial (pointer, keyboard, or scroll)

#### Use when

The product has a control that turns in fixed detents (a thermostat dial, a
radio's tuning knob, a volume selector with clicks) and showing it turn
explains what it does. The example is an invented thermostat dial that
steps from 16 to 26 degrees. It is driven by the visitor (drag or keys) or,
for a demonstration, by the section's scroll position.

#### Avoid when

The control is decorative and the page would be the same without it; a
continuous slider would do (use `<input type="range">`).

#### Code

New to this skill: `.dial-section`, `.dial`, `.dial-face`, `.dial-mark`, `.dial-readout`.

The dial is a `role="slider"`, so screen readers announce the value and the
keyboard keys a slider is expected to answer (arrows, Page Up and Down,
Home, End) work. `data-drive="pointer"` turns it by drag;
`data-drive="scroll"` walks it through its detents as its section crosses
the viewport. `data-rest` is the resting step.

```html
<section class="dial-section" data-moment="thermostat-dial">
  <div class="dial" role="slider" tabindex="0" aria-label="Target temperature"
       aria-valuemin="16" aria-valuemax="26" aria-valuenow="20" aria-valuetext="20 degrees"
       data-rest="20" data-drive="pointer">
    <div class="dial-face" aria-hidden="true"><span class="dial-mark"></span></div>
  </div>
  <p class="dial-readout" aria-hidden="true"><span>20</span> °C</p>
</section>
```

```css
.dial { inline-size: 12rem; aspect-ratio: 1; border-radius: 50%; touch-action: none; cursor: grab }
.dial[data-drive="scroll"] { touch-action: pan-y } /* the page keeps scrolling under a finger on the dial */
.dial:focus-visible { outline: 3px solid var(--color-ink); outline-offset: 4px }
.dial-face {
  block-size: 100%; border-radius: 50%; border: 2px solid currentColor; position: relative;
  rotate: var(--dial-angle, 0deg);
}
.dial-mark { position: absolute; inset-block-start: 8%; inset-inline-start: calc(50% - 2px); inline-size: 4px; block-size: 18%; background: currentColor }
@media (prefers-reduced-motion: no-preference) {
  /* A short overshoot reads as the dial clicking into its detent. */
  .dial-face { transition: rotate 180ms cubic-bezier(0.34, 1.4, 0.64, 1) }
}
```

```js
// dial.js: a control that turns in fixed detents. Pointer, keyboard (it is a slider), or scroll.
const reduce = matchMedia('(prefers-reduced-motion: reduce)');
const DEG_PER_STEP = 24;

document.querySelectorAll('.dial').forEach((dial) => {
  const min = Number(dial.getAttribute('aria-valuemin'));
  const max = Number(dial.getAttribute('aria-valuemax'));
  const rest = Number(dial.dataset.rest);
  const readout = dial.closest('[data-moment]').querySelector('.dial-readout span');

  const set = (value) => {
    const v = Math.min(max, Math.max(min, Math.round(value)));
    dial.setAttribute('aria-valuenow', v);
    dial.setAttribute('aria-valuetext', `${v} degrees`);
    dial.querySelector('.dial-face').style.setProperty('--dial-angle', `${(v - rest) * DEG_PER_STEP}deg`);
    if (readout) readout.textContent = v;
  };
  const value = () => Number(dial.getAttribute('aria-valuenow'));
  set(rest);

  // Keyboard: the keys a slider is expected to answer.
  dial.addEventListener('keydown', (e) => {
    const delta = { ArrowUp: 1, ArrowRight: 1, ArrowDown: -1, ArrowLeft: -1, PageUp: 3, PageDown: -3 }[e.key];
    if (e.key === 'Home') set(min); else if (e.key === 'End') set(max); else if (delta) set(value() + delta); else return;
    e.preventDefault();
  });

  if (dial.dataset.drive === 'pointer') {
    // Drag up or right to turn up; one detent per 24 px of travel.
    let start = null;
    dial.addEventListener('pointerdown', (e) => { start = { x: e.clientX, y: e.clientY, v: value() }; dial.setPointerCapture(e.pointerId); });
    dial.addEventListener('pointermove', (e) => {
      if (!start) return;
      set(start.v + ((e.clientX - start.x) - (e.clientY - start.y)) / 24);
    });
    dial.addEventListener('pointerup', () => { start = null; });
  }

  if (dial.dataset.drive === 'scroll') {
    // Scroll-driven: the dial walks through its detents as its section crosses the viewport.
    // Reduced motion: it stays at its resting step and only keyboard input changes it.
    const section = dial.closest('[data-moment]');
    let queued = 0;
    const update = () => {
      queued = 0;
      if (reduce.matches) return set(rest);
      const r = section.getBoundingClientRect();
      const p = Math.min(1, Math.max(0, (innerHeight - r.top) / (innerHeight + r.height)));
      set(min + p * (max - min));
    };
    addEventListener('scroll', () => { if (!queued) queued = requestAnimationFrame(update); }, { passive: true });
    reduce.addEventListener('change', update);
    update();
  }
});
```

The angle is always a whole number of detents (24 degrees here), so the
face never stops between clicks. Tested in Chromium and Firefox: two Up
arrows go from 20 to 22 (48 degrees), End goes to 26, a 72 px drag to the
left goes 3 detents, and in scroll mode the dial reads 16 before the section
enters, 20 mid-way, and 26 once it has passed.

#### Reduced motion

No turning transition: the face jumps to the detent. In scroll mode the dial
stays at its resting step (tested: 20 at every scroll position) and only
keyboard input changes it, because a control that moves by itself
while the page scrolls is the kind of movement reduced motion removes.

#### Cost

No library. One passive scroll listener and one `requestAnimationFrame` per
frame in scroll mode; nothing in pointer mode until the visitor drags.

### Same-document view transition

#### Use when

A state change inside the page should cross-fade or morph instead of
snapping: a monthly and yearly price toggle, a filter, a tab.

#### Avoid when

Changes that happen often (each keystroke in a search field); large
re-renders where the snapshot cost shows.

#### Code

New to this skill: `.price`. From `landing-skills:landing-build`, used unchanged: `.button`.

```html
<button class="button" type="button" data-billing-toggle aria-pressed="false">Show yearly price</button>
<span class="price" data-price>[Monthly price]</span>
```

```css
.price { view-transition-name: price; font-variant-numeric: tabular-nums }
::view-transition-old(price), ::view-transition-new(price) { animation-duration: 200ms }
```

```js
const reduceMotion = matchMedia('(prefers-reduced-motion: reduce)');
function withTransition(update) {
  if (!document.startViewTransition || reduceMotion.matches) return update();
  document.startViewTransition(update);
}
const toggle = document.querySelector('[data-billing-toggle]');
toggle.addEventListener('click', () => withTransition(() => {
  const yearly = toggle.getAttribute('aria-pressed') !== 'true';
  toggle.setAttribute('aria-pressed', String(yearly));
  toggle.textContent = yearly ? 'Show monthly price' : 'Show yearly price';
  document.querySelector('[data-price]').textContent = yearly ? '[Yearly price]' : '[Monthly price]';
}));
```

Support for `startViewTransition`: Chrome 111, Firefox 144, Safari 18. The
sources disagree on Safari: MDN's compatibility data says 18, caniuse lists
27 as the first full support, so treat 18 as the floor and partial gaps
there as unverified. Elsewhere `withTransition` applies the update directly.

#### Reduced motion

The update is applied directly, with no transition.

#### Cost

No library. The browser snapshots the old and new state; keep the update
small.

### Cross-document view transition

#### Use when

A landing page links to a few same-origin pages (legal pages, a thank-you
page after the form) and a short cross-fade between them, with the header
staying in place, fits the direction.

#### Avoid when

Cross-origin navigation (not supported); single-page landings with no
navigation.

#### Code

New to this skill: none. From `landing-skills:landing-build`, used unchanged: `.site-header`.

On every page that takes part:

```css
@media (prefers-reduced-motion: no-preference) {
  @view-transition { navigation: auto; }
}
::view-transition-old(root), ::view-transition-new(root) { animation-duration: 250ms }
.site-header { view-transition-name: site-header }
```

Support: Chrome 126, Safari 18.2. Firefox support is reported differently by
two sources, so treat it as absent. Without support, navigation is a normal
page load.

#### Reduced motion

The opt-in is inside the `no-preference` query, so no transition runs.

#### Cost

No JavaScript. The browser holds a snapshot of the old page until the new
one renders.
