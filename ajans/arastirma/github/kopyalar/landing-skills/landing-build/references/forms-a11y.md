# Lead-capture form and accessibility floor

Read this before writing the form, and again before the final checks. In
test builds an agent made without this skill, every form was dead or fake: two posted to `action="#"`, one stopped the
submit with a script, showed a thank-you line and reset the fields, so every
lead was lost while the visitor was told it had arrived.

## Contents

- What a working form is
- Choosing the fields
- Markup
- Labels, hints and placeholder text
- States
- The enhancement script
- What the endpoint must do
- The two result pages
- Spam protection
- Keyboard and focus
- Contrast
- Target size
- Landmarks, headings and links
- Images and placeholders for assistive tech
- FAQ disclosure

## What a working form is

| Dead form (each one appeared in an unguided build) | Working form |
|---|---|
| `<form action="#" method="post">` | `<form action="/api/lead" method="post">` pointing at an endpoint that stores or forwards the data |
| `<form id="f">` plus `e.preventDefault()` and a hidden "thanks" paragraph revealed by script | no script needed; the endpoint redirects to `/thanks/` or `/form-error/` |
| `onsubmit="return false"` | nothing on `onsubmit`; the enhancement script uses `fetch` and reports what the server said |

If the user has no endpoint yet, still write `action="/api/lead"`, mark it,
and say so in the hand-off:

```html
<!-- ENDPOINT PLACEHOLDER: /api/lead does not exist yet. Submissions fail until
     landing-skills:landing-launch adds the Worker, or the user supplies another URL. -->
<form class="lead-form" action="/api/lead" method="post">
```

`/api/lead` is the path `landing-skills:landing-launch` routes to its form
Worker. If the user already has a form service, use its URL instead and
check that it accepts a normal form POST and redirects back to your result
pages. A `mailto:` action opens the
visitor's mail program instead of sending anything, so do not use it as the
conversion form.

## Choosing the fields

Ask only for what the business needs to act on the lead.

- Waitlist: email only.
- Name: one "Full name" field, not first and last. People treat their name
  as one thing; Baymard found that 89% of sites split it anyway.
- Choice from a long or open list (city district, postcode, region): a text
  input with the matching `autocomplete` value, not a `<select>` that is
  missing options. One unguided build hard-coded a district list that left most
  of the city's districts out, behind an "Other" option.
- Phone: `type="tel"` with `autocomplete="tel"`; the format goes in a hint,
  and the server validates it. A strict `pattern` rejects numbers people
  write in other valid ways.
- Free text: a `<textarea>` with a hint saying what helps.

Give every field a `maxlength` (254 for an email address; a cap that fits
the content for the others). The hand-off lists every field the form
submits with its `name`, type, whether it is `required`, and that
`maxlength`. `landing-skills:landing-launch` builds its Worker's
validation, length caps and storage from that list, and sizes the request
limit from the caps, so a long free-text field gets a limit that fits it.
A field missing from the list is refused by that Worker and fails its
checklist, so list them all.

Field-count conversion figures in circulation come from aggregators the
research could not trace, so do not quote them to the user.

## Markup

Both examples are invented. Field ids must be unique in the page: if a page
has two forms (one in the hero, one at the end), suffix every id and every
`for`, `aria-describedby` and `aria-labelledby` that points at it in the
second form (`email-end`, `email-end-hint`, `leave_blank-end`). Duplicate
ids make clicking a label focus the wrong field and attach hints to the
wrong input. The `name` values stay the same, since the server reads names.

A waitlist form (English, email only):

```html
<h2 id="waitlist-title">Hear when the next term opens</h2>
<form class="lead-form" action="/api/lead" method="post" aria-labelledby="waitlist-title"
      data-sending="Sending…"
      data-sent="You are on the list. We will email you once, when the next term opens."
      data-failed="That did not go through. Check your connection and try again, or email hello@bread.example.">
  <div class="field">
    <label for="email">Email address</label>
    <input id="email" name="email" type="email" autocomplete="email" required maxlength="254" aria-describedby="email-hint">
    <p class="hint" id="email-hint">One email when the next term opens. Nothing else.</p>
  </div>
  <div class="trap" aria-hidden="true">
    <label for="leave_blank">Leave this empty</label>
    <input id="leave_blank" name="leave_blank" type="text" tabindex="-1" autocomplete="off">
  </div>
  <button class="button" type="submit">Join the waitlist</button>
  <p class="form-status" role="status" aria-live="polite"></p>
</form>
```

A booking form for a local service (Spanish as written in Spain, four
fields). It has no email field, because the business phones back, so the
hand-off says so: `landing-skills:landing-launch` then removes `email` from
its Worker and makes the phone required. Its field list for the hand-off:

```text
nombre        text      required  maxlength 100
codigo_postal text      required  maxlength 10
telefono      tel       required  maxlength 30
descripcion   textarea  required  maxlength 1000
```


```html
<h2 id="recogida-title">Pide la recogida de tu bici</h2>
<form class="lead-form" action="/api/lead" method="post" aria-labelledby="recogida-title"
      data-sending="Enviando…"
      data-sent="Hemos recibido tu solicitud. Te llamamos hoy para acordar la recogida."
      data-failed="No pudimos enviar tu solicitud. Revisa tu conexión e inténtalo de nuevo.">
  <div class="field">
    <label for="nombre">Nombre completo</label>
    <input id="nombre" name="nombre" type="text" autocomplete="name" required maxlength="100">
  </div>
  <div class="field">
    <label for="cp">Código postal</label>
    <input id="cp" name="codigo_postal" type="text" inputmode="numeric" autocomplete="postal-code" required maxlength="10" aria-describedby="cp-hint">
    <p class="hint" id="cp-hint">Para saber si recogemos en tu zona.</p>
  </div>
  <div class="field">
    <label for="telefono">Teléfono</label>
    <input id="telefono" name="telefono" type="tel" autocomplete="tel" required maxlength="30" aria-describedby="telefono-hint">
    <p class="hint" id="telefono-hint">Un móvil en el que podamos llamarte hoy.</p>
  </div>
  <div class="field">
    <label for="descripcion">Qué le pasa a la bici</label>
    <textarea id="descripcion" name="descripcion" rows="4" required maxlength="1000" aria-describedby="descripcion-hint"></textarea>
    <p class="hint" id="descripcion-hint">Dos o tres líneas bastan: qué notas y desde cuándo.</p>
  </div>
  <div class="trap" aria-hidden="true">
    <label for="leave_blank">Deja este campo vacío</label>
    <input id="leave_blank" name="leave_blank" type="text" tabindex="-1" autocomplete="off">
  </div>
  <button class="button" type="submit">Pedir recogida</button>
  <p class="form-status" role="status" aria-live="polite"></p>
</form>
```

```css
.field { display: grid; gap: var(--space-2, calc(var(--text-base) * 0.5)); margin-block-end: var(--space-6, calc(var(--text-base) * 1.5)); }
.field label { font-weight: 600; }
.field input, .field textarea {
  min-block-size: 2.75rem; padding: var(--space-3, calc(var(--text-base) * 0.75));
  border: 1px solid var(--color-line-strong, var(--color-ink-muted)); border-radius: var(--radius-control);
  background: var(--color-bg); color: var(--color-ink); font: inherit;
}
.field input::placeholder, .field textarea::placeholder { color: var(--color-ink-muted); opacity: 1; }
.hint { font-size: var(--text-sm, var(--text-base)); color: var(--color-ink-muted); margin: 0; }
.trap { position: absolute; inset-inline-start: -100vw; }
.form-status:empty { display: none; }
.form-status { padding: var(--space-3, calc(var(--text-base) * 0.75)) var(--space-4, var(--text-base)); border-inline-start: 3px solid currentColor; }
.form-status[data-state="error"] { color: var(--color-danger, var(--color-ink)); }
/* A form on a band: hints and status sit on the band, so they take its text pair. */
.band .hint, .band .form-status, .band .form-status[data-state="error"] { color: var(--color-on-band-muted, var(--color-on-band, var(--color-ink))); }
```

`--color-ink-muted` was checked against the page ground only. Hints and the
status line sit on whatever ground holds the form, so on a band they take
that ground's muted text token (`--color-on-<ground>-muted` when the
direction defines one) or its `on-` token, as the `.band` rule shows; the
error state there uses the band's text colour too, since `--color-danger`
was checked on the page ground. The placeholder text stays
`--color-ink-muted`: it sits on the input's own background, which is the
page ground. Check each of these pairs with the contrast script below.

The `padding` shorthand on inputs is fine: they are components, not layout
classes. The input border needs 3:1 against the background (see Contrast).

## Labels, hints and placeholder text

- Every control has a visible `<label for>`. A placeholder is not a label: it
  disappears on typing, and two unguided builds used it as the only cue. A
  visually hidden label is still required where a visible one is
  impossible, but a lead form has room for visible ones.
- Format help and reassurance go in a hint below the field, linked with
  `aria-describedby`, so screen readers read it with the label.
- If you use placeholder text at all, use it for an example only, and set
  its colour from the tokens with `opacity: 1`. The browser default grey
  failed contrast in two unguided builds.
- Browsers write their own validation messages ("Please fill out this
  field") in the browser's language, not the page's. The hints carry the
  page-language guidance; the server repeats the checks.
- Say what happens after submitting, next to the button: who replies, when,
  and that nothing else is sent. On Cloudflare Workers Free the Worker
  cannot email arbitrary visitors, only verified addresses of the account,
  so do not promise a confirmation email unless the user's setup sends one.

## States

| State | What the visitor sees | Where it comes from |
|---|---|---|
| default | labels, hints, the button | markup |
| hover, focus | colour change on the button, focus ring on every control | CSS |
| invalid before sending | browser validation from `required` and `type` | the browser |
| sending | button disabled, "Sending…" in the status line | enhancement script |
| success without JS | the `/thanks/` page | endpoint `303` redirect |
| success with JS | the success text in the status line, form cleared | script, only after a 2xx with `ok: true` |
| error with JS | the error text, input kept | script |
| error without JS | the `/form-error/` page | endpoint `303` redirect |

## The enhancement script

Always ship it. The form works without JavaScript, which is what makes the
script progressive enhancement rather than a requirement, but the file is
part of every build with a form: `landing-skills:landing-motion` extends
the states it sets (`disabled` while sending, `data-state` on the status
line) and `landing-skills:landing-launch` checks them.

Lets the visitor stay on the page. It reads its messages from the form's
`data-` attributes, so the same file serves any language. Tested in a
browser against a stand-in endpoint: success shows only after a 2xx
response with `{"ok": true}`, a 500 shows the error and leaves the email in
the field, and with JavaScript off the form posts and lands on `/thanks/`.
Check both conditions, never `response.ok` alone. In the same test, an
endpoint that ignored `Accept` and redirected, and a host that answered the
POST with an HTML page, both gave a 200 to `fetch`; the `ok: true` check
turned each into the error message instead of a false "Thanks".
It sends `FormData` (multipart), and the browser sets `Content-Length`,
which the `landing-skills:landing-launch` Worker requires; that Worker
accepted a multipart fetch post in its own test. The attribute names and
the success check match that skill's script, so either one works with
either skill's markup.

```js
// form.js, loaded with <script src="/form.js" defer>
for (const form of document.querySelectorAll('form.lead-form')) {
  const status = form.querySelector('.form-status');
  const button = form.querySelector('[type="submit"]');
  form.addEventListener('submit', async (event) => {
    event.preventDefault();
    button.disabled = true;
    status.removeAttribute('data-state');
    status.textContent = form.dataset.sending;
    try {
      const response = await fetch(form.action, {
        method: 'POST',
        body: new FormData(form),
        headers: { Accept: 'application/json' },
      });
      const result = await response.json();
      if (!response.ok || result.ok !== true) throw new Error(`HTTP ${response.status}`);
      status.dataset.state = 'success';
      status.textContent = form.dataset.sent;
      form.reset();
    } catch {
      status.dataset.state = 'error'; // keep what the visitor typed
      status.textContent = form.dataset.failed;
      window.turnstile?.reset(); // Turnstile tokens are single-use; a retry needs a fresh one
    } finally {
      button.disabled = false;
    }
  });
}
```

The submit handler runs only after the browser's own validation passed, so
`required` and `type="email"` still work.

## What the endpoint must do

This is what the `landing-skills:landing-launch` Worker does. Write it into
the hand-off as the contract when someone else owns the endpoint:

- Accept `POST` with `application/x-www-form-urlencoded` or
  `multipart/form-data` (the script sends `FormData`, which is multipart),
  with a `Content-Length` no larger than the request limit. The launch
  Worker sizes that limit from the fields' length caps (8 KB by default,
  which fits an email-only form), so the caps in the hand-off matter.
- Validate every field on the server; the browser checks are a courtesy.
- If the `leave_blank` field is filled, answer as if it worked and store
  nothing.
- If the request's `Accept` header includes `application/json`, answer
  JSON: `{"ok": true}` with a 2xx, or `{"ok": false, "error": "..."}` with a
  4xx or 5xx.
- Otherwise answer `303 See Other` with `Location: /thanks/` on success and
  `Location: /form-error/` on failure.

## The two result pages

Build both now, in the page's layout, tokens and footer, each with a link
back to `/` and `<meta name="robots" content="noindex">`.

- `/thanks/`: says the submission arrived and what happens next, in the
  words of `copy.md`. Promise nothing the business has not said it will do.
- `/form-error/`: says plainly that the submission did not go through,
  without blaming the visitor, and gives another way to get in touch: the
  email address, phone number or messaging channel from `copy.md`, or
  `[PLACEHOLDER: another way to reach the business]` listed in the hand-off.
  Once Turnstile is on, every visitor without JavaScript lands here, so this
  page is the no-JavaScript path's real ending, and the other contact route
  is what keeps that lead.

```html
<!-- form-error/index.html (invented example) -->
<main id="main" class="section">
  <div class="container">
    <h1>Your message did not reach us</h1>
    <p>Nothing was sent, so please try once more, or write to us directly at
      <a href="mailto:hello@bread.example">hello@bread.example</a>.</p>
    <p><a href="/">Back to Larchmont Bread School</a></p>
  </div>
</main>
```

## Spam protection

- The honeypot (`.trap` above) is hidden from sight, from the tab order
  (`tabindex="-1"`) and from screen readers (`aria-hidden="true"` on its
  wrapper), so only bots fill it in.
- Name it `leave_blank`, the name the `landing-skills:landing-launch` Worker
  checks. Avoid names browsers autofill (`website`, `url`, `name`, `phone`,
  `company`, `address`): a honeypot that autofill fills throws away a real
  person's submission while telling them it worked. Browsers do not promise
  to honour `autocomplete="off"`, so this lowers the risk without removing
  it.
- Cloudflare Turnstile is added by `landing-skills:landing-launch`. Its
  widget is a script, so once it is on, a submission without JavaScript has
  no token, fails verification and always lands on `/form-error/`. Tell the
  user that trade-off when handing off.

## Keyboard and focus

- First Tab stop: a skip link to `<main id="main">`.

  ```css
  .skip-link { position: absolute; inset-inline-start: var(--space-4, var(--text-base)); inset-block-start: -10rem; }
  .skip-link:focus { inset-block-start: var(--space-4, var(--text-base)); background: var(--color-ink); color: var(--color-bg); padding: var(--space-3, calc(var(--text-base) * 0.75)) var(--space-4, var(--text-base)); z-index: 1; }
  ```
  ```html
  <a class="skip-link" href="#main">Skip to content</a>
  ```

- Tab order follows the DOM, so write the DOM in reading order. No positive
  `tabindex`.
- Never remove the focus outline without a replacement. A `:focus-visible`
  ring of 3px with an offset shows on keyboard use and not on mouse clicks.
- Anything that opens or closes is a real `<button>` or `<details>`, which
  the keyboard can already operate.

## Contrast

WCAG 2.2 is the pass or fail test:

- Text: 4.5:1; large text (at least 18pt, or 14pt bold): 3:1.
- Input borders, focus rings, icons that carry meaning: 3:1 against the
  colours next to them.
- Do not round: 4.499:1 fails.

Check every pair the page renders, not just ink on background: hints and
captions on each surface colour, placeholder text in inputs, text on the
action colour, muted text on tinted bands. Unguided builds failed on exactly
these: small captions just under 4.5:1 on a tinted band, and muted body
text near the limit on a coloured band. OKLCH lightness is not a contrast
ratio, so compute each pair from the hex values in `tokens.css`. This
script does it with no dependencies:

```js
// contrast.mjs: WCAG 2.x contrast ratio of two sRGB hex colours.
// Usage: node contrast.mjs "#1d2a24" "#f4efe4"
const luminance = (hex) => {
  const [r, g, b] = hex.replace('#', '').match(/../g).map((h) => {
    const c = parseInt(h, 16) / 255;
    return c <= 0.04045 ? c / 12.92 : ((c + 0.055) / 1.055) ** 2.4;
  });
  return 0.2126 * r + 0.7152 * g + 0.0722 * b;
};
const [a, b] = process.argv.slice(2).map(luminance);
const ratio = (Math.max(a, b) + 0.05) / (Math.min(a, b) + 0.05);
// Truncate, never round: 4.499 must not print as 4.50.
console.log(Math.floor(ratio * 1000) / 1000);
```

Checked against known pairs: black on white prints 21, `#777777` on white
prints 4.478 (a fail for body text).

## Target size

Buttons, links in the header and form controls are at least 44 by 44 px:
`min-block-size: 2.75rem` and enough inline padding. That is the figure the
research carries (Apple's guidance, cited by Unbounce); WCAG 2.2 also has a
target-size criterion of its own, so check it for small inline controls.
One unguided build's small header button fell well short of 44 px on a phone.

## Landmarks, headings and links

- `header`, `nav` with an `aria-label`, `main`, `footer`. One `h1`.
- The logo or wordmark links to `/`. Every nav link and in-page button
  points at a real section `id` or URL. `href="#"` jumps to the top and is a
  catalogued tell.
- Section links stay reachable on phones (see the header recipe in
  `css.md`).
- The footer carries the things a visitor looks for before trusting the
  page: who runs it, how to contact them, and links to terms, shipping or
  refunds when the offer involves money. Content comes from `copy.md`;
  missing items are placeholders.

## Images and placeholders for assistive tech

- A real image: `alt` that says what it shows ("Two loaves on a board, one
  cut open to show the crumb"), not "image of".
- A decorative image: `alt=""`.
- A placeholder for a missing asset: visible text in a plain `div`. No
  `role="img"` and no `aria-label`, which would announce a photograph that
  does not exist.
- A product mock built from HTML: do not dress it as a screenshot. If the
  real screenshot is missing, use a labelled placeholder and list it.

## FAQ disclosure

Use `<details>` and `<summary>`. They open and close with the keyboard and
announce their state without any script, so the FAQ works with JavaScript
off. Each question is the `summary` text; keep a heading above the list.

```html
<section class="section" id="faq" aria-labelledby="faq-title">
  <div class="container">
    <h2 id="faq-title">Questions before you book</h2>
    <details>
      <summary>Can I cancel a booking?</summary>
      <p>[PLACEHOLDER: cancellation terms and refund deadline]</p>
    </details>
  </div>
</section>
```
