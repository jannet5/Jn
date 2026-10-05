# Forms: a real endpoint for the lead-capture form

## Contents

- Why this file exists
- Which case you are in
- What you add to the project
- The form markup
- The two result pages
- The Worker
- Every field the form submits
- Run it locally
- Production setup (needs the user)
- Other places to put the lead
- Exporting leads
- What this code does not cover

## Why this file exists

Unguided builds tend to ship forms that cannot collect a lead: an action that points nowhere with the submit cancelled, a submit handler that shows a thank-you message and throws the data away, or a button with no handler at all. A fake success message is worse than an error, because the visitor leaves believing they signed up. `landing-skills:landing-build` leaves the form posting to `/api/lead` with that endpoint marked as not existing yet, and lists every field name the form submits in its hand-off; this file makes the endpoint real.

The design:

- The page stays static. One Worker route, `POST /api/lead`, receives the form.
- The server decides everything. It validates every field the form submits, rejects bad input, and verifies the Turnstile token with Siteverify. Nothing the browser says is trusted, including the honeypot and the token.
- One endpoint serves two kinds of client. A `fetch()` call that sends `Accept: application/json` gets JSON. A plain HTML form post (JavaScript off) gets a `303` redirect to `/thanks/` or `/form-error/`, so the visitor always lands on a real page that tells the truth.
- A stored lead is counted once as a conversion in Workers Analytics Engine (see `analytics.md`).

All code below was run with `npx wrangler dev` (Wrangler 4.147.0) and exercised with curl and a browser: valid post, invalid email, a field the Worker does not handle, a form with three extra fields, missing and oversized token, oversized body and missing `Content-Length`, failed Turnstile, hostname mismatch, honeypot, JSON body, wrong method, Siteverify timeout, a failing database, duplicate email, JavaScript-off post. The Analytics Engine write runs locally without error, but local dev cannot show the stored data point; check it after the first deploy with the query in `analytics.md`.

## Which case you are in

Read the conversion action in `landing/brief.md` first. It says whether the click posts a form or opens an outside page, which fields the form has, and whether an endpoint exists (`none yet` or a URL).

| Brief says | What launch does |
|---|---|
| Form, endpoint `none yet` (or the page's form posts to `/api/lead`, which nothing serves) | Everything in this file: the form Worker at `/api/lead`, Turnstile, D1, the conversion event. |
| Form, endpoint exists (a URL the user or another service owns) | Add no form Worker and no Turnstile. Check that the form's `action` is that URL; that the endpoint follows the contract below (JSON for `Accept: application/json`, otherwise a `303` to `/thanks/` or `/form-error/`), or that the result pages match what it really returns; and write in `landing/launch.md` that spam protection and validation are that endpoint's job. Count the conversion from the endpoint's own records; the `/api/convert` beacon in `analytics.md` is an option if the user wants it in Analytics Engine. |
| Outside page (checkout, booking, app store) | No form to handle. Make the link point at the real URL, count clicks with the `/api/convert` beacon (`analytics.md`), and mark the form items of the checklist `n/a`. |

The contract the endpoint must meet, whoever owns it: accept a `POST` of `application/x-www-form-urlencoded` or `multipart/form-data`; validate every field on the server; treat a filled `leave_blank` as success and store nothing; answer JSON `{"ok": true}` (2xx) or `{"ok": false, "error": "..."}` (4xx or 5xx) when `Accept` includes `application/json`; otherwise answer `303` with `Location: /thanks/` or `/form-error/`. A third-party endpoint that cannot do this still works without JavaScript if its own result page is acceptable; with JavaScript on, the build's script shows the error message unless the response is JSON with `ok: true`, so test it before launch.

## What you add to the project

| File | Purpose |
|---|---|
| `wrangler.jsonc` | Worker, assets, D1 and Analytics Engine bindings |
| `src/index.js` | Routes `/api/lead` and `/api/convert` |
| `src/lead.js` | The form handler |
| `schema.sql` | The `leads` table |
| `.dev.vars` | Turnstile test secret for local runs (git-ignored) |
| `<assets>/thanks/index.html`, `<assets>/form-error/index.html` | Only if missing: `landing-skills:landing-build` writes them. Never overwrite an existing one |

Add `.dev.vars*` and `.wrangler/` to `.gitignore`.

The handler below accepts exactly the fields listed in its `FIELDS` table, which ships with `email` only. Before anything else, list every field `name` the form submits (from the build's hand-off, or read the `name` attributes in the built page's form) and set up `FIELDS` and the table for them; see "Every field the form submits".

## The form markup

`landing-skills:landing-build` already writes the form, the honeypot, the `role="status"` line and an enhancement script. Do not replace them. Check that the form posts to `/api/lead` with `method="post"`, that the honeypot is named `leave_blank`, and that the script checks for `ok: true` and calls `window.turnstile?.reset()` on failure. What launch adds is Turnstile: the script tag and the widget `div` inside the form. The full markup and script below are for a page built without that skill.

Turnstile loads with `async defer` and renders into `div.cf-turnstile`; implicit rendering adds a hidden input named `cf-turnstile-response` to the enclosing form. Use the test sitekey below locally and the real sitekey from the user's Turnstile widget in production. The sitekey is public; it can sit in the HTML.

The honeypot field `leave_blank` is for bots. Hide it from sight and from assistive technology (`aria-hidden="true"` on the wrapper, `tabindex="-1"` on the input), or screen-reader users will be asked to fill a field that blocks them. Its name and label are chosen to match nothing a browser autofills (avoid `website`, `url`, `name`, `phone`, `company`, `address`): a honeypot that autofill fills throws away a real person's sign-up while telling them it worked. Browsers do not promise to honour `autocomplete="off"`, so this lowers the risk without removing it. That is the trade-off of a honeypot; if the user would rather lose some spam protection than any real lead, drop the field and rely on Turnstile.

The messages live in `data-` attributes so the script holds no copy. Take the wording from `landing/copy.md`. The example below is invented (a bakery's monthly newsletter) and is the markup that was tested; write the page's own wording. Do not promise a confirmation email: on the Workers Free plan, Email Service can only send to verified addresses in the user's own account, so it cannot email a visitor.

```html
<style>.hp{position:absolute;left:-10000px;width:1px;height:1px;overflow:hidden}</style>
<script src="https://challenges.cloudflare.com/turnstile/v0/api.js" async defer></script>

<form class="lead-form" action="/api/lead" method="post"
      data-sending="Sending…" data-sent="You are on the list." data-failed="That did not go through. Check the address and try again.">
  <label for="email">Email address</label>
  <input id="email" name="email" type="email" required autocomplete="email" maxlength="254">
  <div class="hp" aria-hidden="true">
    <label for="leave_blank">Leave this field empty</label>
    <input id="leave_blank" name="leave_blank" type="text" tabindex="-1" autocomplete="off">
  </div>
  <div class="cf-turnstile" data-sitekey="1x00000000000000000000AA"></div>
  <button type="submit">Get the newsletter</button>
  <p class="form-status" role="status"></p>
</form>
```

The script upgrades the form when JavaScript runs. It disables the button while sending (no double submits), shows the result in the `role="status"` element so screen readers announce it, and on failure resets the Turnstile widget, because a token is single-use and a retry needs a fresh one.

```html
<script type="module">
for (const form of document.querySelectorAll(".lead-form")) {
  form.addEventListener("submit", async (event) => {
    event.preventDefault();
    const button = form.querySelector("button[type=submit]");
    const status = form.querySelector(".form-status");
    button.disabled = true;
    status.textContent = form.dataset.sending;
    let ok = false;
    try {
      const res = await fetch(form.action, {
        method: "POST",
        body: new FormData(form),
        headers: { Accept: "application/json" },
      });
      ok = (await res.json()).ok === true;
    } catch {}
    if (ok) {
      form.reset();
      status.textContent = form.dataset.sent;
      return; // button stays disabled: one sign-up per page view
    }
    status.textContent = form.dataset.failed;
    window.turnstile?.reset(); // tokens are single-use; get a fresh one before the retry
    button.disabled = false;
  });
}
</script>
```

If the page has two forms (hero and a repeat at the bottom), give each its own ids and its own `div.cf-turnstile`. The script handles every `.lead-form`. It was tested with one widget, where `turnstile.reset()` cleared the spent token and a fresh one arrived. Cloudflare's guidance for a page that stays open after a submission is to render the widget explicitly, keep its widget id, and call `turnstile.reset(widgetId)`; with two forms, test a failed submission on each and switch to that pattern if the retry fails.

## The two result pages

With JavaScript off, the browser does a normal form post and follows the `303` redirect to `/thanks/` or `/form-error/`. `landing-skills:landing-build` writes both pages, in the site's layout, with `noindex`. Check that both exist in the build output and never overwrite one that does. Create one only if it is missing, and then:

- `/thanks/`: says the submission was received and what happens next, in the words of `landing/copy.md` (for a newsletter: when the first issue arrives, if the brief says so).
- `/form-error/`: says the submission did not go through and gives another way to reach the business, taken from the contact routes in `landing/brief.md` (an email address, a phone number, a messaging channel). If the brief has none, use a visible `[PLACEHOLDER: another way to reach the business]` and mark the checklist item pending.
- Both: the site's layout, tokens and footer, a link back to `/`, and `<meta name="robots" content="noindex">`.

Check the existing `/form-error/` too: it must offer that other contact route. Turnstile needs JavaScript, so a visitor with JavaScript off has no token and always lands here. Tested in a browser with JavaScript disabled: the post reached the Worker, which redirected to `/form-error/`. If the existing page lacks the contact route, report it as a finding for `landing-skills:landing-build` rather than rewriting the page.

## The Worker

`src/lead.js`:

```js
// POST /api/lead: validate on the server, verify Turnstile, store, count the conversion.
const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const SITEVERIFY = "https://challenges.cloudflare.com/turnstile/v0/siteverify";

// One entry per field the form submits, named exactly as in the form's `name` attributes.
// Each entry is also a column of the `leads` table. Every entry needs a positive integer `max`.
// `multiline: true` only for a <textarea>. A field the form sends that is not listed here is
// refused (400 unknown_field), so nothing is dropped or stored unvalidated.
const FIELDS = {
  email: { required: true, max: 254, lower: true, valid: (v) => EMAIL_RE.test(v) },
};
// "email" for a sign-up list (one row per address; a repeat sign-up is answered as success and
// stores nothing). null for an enquiry form (every submission is a new row).
const UNIQUE_BY = "email";
// Fields the Worker reads but does not store.
const CONTROL = new Set(["cf-turnstile-response", "leave_blank"]);
// About 9 bytes per character of every field (a plain form post percent-encodes non-ASCII text),
// plus 4 KB for the Turnstile token and encoding overhead.
const MAX_BYTES = 8192;

// Fail closed on a misconfigured table: the Worker refuses to start instead of storing
// uncapped values or building broken SQL.
for (const [key, rule] of Object.entries(FIELDS)) {
  if (!/^[a-z][a-z0-9_]{0,62}$/.test(key) || !Number.isInteger(rule.max) || rule.max < 1) {
    throw new Error(`lead.js: FIELDS.${key} needs a lowercase identifier name and a positive integer max`);
  }
}
if (UNIQUE_BY !== null && !Object.hasOwn(FIELDS, UNIQUE_BY)) {
  throw new Error("lead.js: UNIQUE_BY must be null or a key of FIELDS");
}

// Control characters: none in single-line fields, nor the line separators U+0085, U+2028 and
// U+2029; tab, line feed and carriage return (browsers send textarea line breaks as CR LF) and
// those separators are allowed in multiline ones. U+FFFD is what a byte that is not valid
// UTF-8 decodes to. trim() runs first, so leading or trailing whitespace is removed, not refused.
const BAD_LINE = /[\u0000-\u001f\u007f\u0085\u2028\u2029\ufffd]/;
const BAD_TEXT = /[\u0000-\u0008\u000b\u000c\u000e-\u001f\u007f\ufffd]/;

// fetch() callers send Accept: application/json and get JSON back.
// A plain HTML form post (JavaScript off) gets a 303 redirect to a static page.
function reply(req, ok, error, status) {
  if ((req.headers.get("Accept") ?? "").includes("application/json")) {
    return Response.json(ok ? { ok: true } : { ok: false, error }, { status });
  }
  return Response.redirect(new URL(ok ? "/thanks/" : "/form-error/", req.url), 303);
}

export async function handleLead(req, env, experiment = "none", variant = "none") {
  if (req.method !== "POST") {
    return new Response("Method Not Allowed", { status: 405, headers: { Allow: "POST" } });
  }

  // Refuse anything without a length or over MAX_BYTES before reading the body.
  const length = Number(req.headers.get("Content-Length"));
  if (!length || length > MAX_BYTES) return reply(req, false, "bad_request", 413);

  let form;
  try {
    form = await req.formData();
  } catch {
    return reply(req, false, "bad_request", 400); // the client's fault; nothing to log
  }

  // Honeypot: people never fill it. Answer as if it worked and store nothing.
  if (form.get("leave_blank")) return reply(req, true, null, 200);

  for (const name of form.keys()) {
    if (!Object.hasOwn(FIELDS, name) && !CONTROL.has(name)) return reply(req, false, "unknown_field", 400);
  }

  const values = {};
  for (const [name, rule] of Object.entries(FIELDS)) {
    let v = form.get(name);
    if (typeof v !== "string") v = ""; // a file upload is not a value
    v = v.trim();
    if (rule.lower) v = v.toLowerCase();
    if (!v && rule.required) return reply(req, false, `invalid_${name}`, 400);
    if (v.length > rule.max || (rule.multiline ? BAD_TEXT : BAD_LINE).test(v) ||
        (v && rule.valid && !rule.valid(v))) {
      return reply(req, false, `invalid_${name}`, 400);
    }
    values[name] = v || null;
  }

  const token = String(form.get("cf-turnstile-response") ?? "");
  if (!token || token.length > 2048) return reply(req, false, "invalid_token", 400);

  const body = new FormData();
  body.append("secret", env.TURNSTILE_SECRET);
  body.append("response", token);

  let result;
  try {
    const res = await fetch(SITEVERIFY, { method: "POST", body, signal: AbortSignal.timeout(5000) });
    result = await res.json();
  } catch (err) {
    console.error("lead: Siteverify request failed:", err);
    return reply(req, false, "verify_unavailable", 503);
  }
  if (!result.success) return reply(req, false, "verification_failed", 403);
  if (env.TURNSTILE_HOSTNAME && result.hostname !== env.TURNSTILE_HOSTNAME) {
    return reply(req, false, "verification_failed", 403);
  }

  // Column names come from FIELDS (checked above), never from the request; values are bound.
  // Only a repeat of UNIQUE_BY is ignored; any other constraint failure takes the error path.
  const columns = Object.keys(values);
  const sql = `INSERT INTO leads (${columns.join(", ")}, created_at) VALUES (${columns.map(() => "?").join(", ")}, ?)` +
    (UNIQUE_BY ? ` ON CONFLICT(${UNIQUE_BY}) DO NOTHING` : "");
  let meta;
  try {
    ({ meta } = await env.DB.prepare(sql)
      .bind(...Object.values(values), new Date().toISOString())
      .run());
  } catch (err) {
    console.error("lead: D1 insert failed:", err);
    return reply(req, false, "server_error", 500);
  }

  // The lead is stored. A failure to count it must not turn that into an error: the visitor
  // would submit again and the count is only a statistic.
  try {
    if (meta.changes > 0) {
      env.EVENTS.writeDataPoint({ indexes: [experiment], blobs: [variant, "conversion"], doubles: [1] });
    }
  } catch (err) {
    console.error("lead: conversion not counted:", err);
  }
  return reply(req, true, null, 200);
}
```

What each check is for:

- `Content-Length` first: a missing length or more than `MAX_BYTES` is refused with `413` before the body is read, so nobody can make the Worker parse a large upload. An email-only sign-up from a browser is a few hundred bytes (the tested fetch post was 382 bytes, multipart), well under the default 8 KB.
- `formData()` in a `try`: a JSON or garbage body gets `400 bad_request` instead of an exception.
- The honeypot answers as success and stores nothing, so a bot learns nothing.
- Configuration fails closed: when the module loads, every `FIELDS` key must be a lowercase SQL identifier and every entry must have a positive integer `max`, and `UNIQUE_BY` must be `null` or a key of `FIELDS`. Otherwise the Worker throws and does not start (tested: an entry without `max`, and a key `first-name`, each stopped `wrangler dev` with the message from the code), so a mistake shows up in the first local run, not as uncapped or broken inserts in production.
- Fields: any submitted name that is neither an own key of `FIELDS` (`Object.hasOwn`, so `constructor`, `__proto__` and `toString` are refused too) nor a control field (`cf-turnstile-response`, `leave_blank`) is refused with `400 unknown_field`, so a field added to the page later fails loudly in testing instead of vanishing. Each listed field is trimmed, checked for presence if `required`, capped at `max` characters, and checked with `valid` if given. A file upload counts as empty.
- Control characters: none in single-line fields (email, name, phone), and neither the line separators U+0085, U+2028 and U+2029; in a `multiline` field (a `<textarea>`) tab, line feed and carriage return are allowed, because browsers send textarea line breaks as CR LF, and so are those three separators, because they are line breaks in pasted text and harmless in a message. `trim()` runs first, so leading or trailing whitespace (a vertical tab, a form feed, U+2028) is removed rather than refused. `U+FFFD`, which is what a byte that is not valid UTF-8 decodes to, is refused in every field. Tested: NUL and a line feed in the email, a tab in the name, `%FF` in the name, NUL in the message, and each of U+2028, U+2029 and U+0085 inside a name were refused; CR LF, a tab and each of those three separators in the message were stored.
- Email: lower-cased, at most 254 characters, one `@` and a dot after it. The browser's `type="email"` is a convenience; this is the check that counts.
- Token: present and at most 2,048 characters (the documented maximum). Tokens are valid for 300 seconds and can be verified once; a reused one fails with `timeout-or-duplicate`.
- Siteverify gets `secret` and `response`. The optional `remoteip` (the visitor's IP from `CF-Connecting-IP`) is left out on purpose: the widget already ran in the visitor's browser, and leaving it out means the Worker sends no IP address of its own. Add `body.append("remoteip", req.headers.get("CF-Connecting-IP") ?? "")` only if the user wants that extra signal, and then name it in the privacy notice. The 5-second timeout returns `503 verify_unavailable` rather than hanging; tested by pointing the call at an unroutable address.
- `TURNSTILE_HOSTNAME`, when set, must equal the `hostname` Siteverify reports. Cloudflare's docs advise checking it. Locally the test keys report `example.com`.
- The insert: with `UNIQUE_BY = "email"` (a sign-up list) it ends in `ON CONFLICT(email) DO NOTHING`, so a repeat sign-up stores nothing and is answered as success by design: the address is already on the list. With `UNIQUE_BY = null` (an enquiry form) it is a plain `INSERT` and every submission is a row. Any other failure (a missing table, a `NOT NULL` column the Worker does not fill, a leftover `UNIQUE` constraint) takes the error path: `500 server_error`, or `/form-error/` without JavaScript. Nothing is swallowed and reported as received. Tested by dropping the table, by a `NOT NULL` column outside `FIELDS`, and by an old `UNIQUE(email)` table under the enquiry configuration.
- Logging: the Siteverify and D1 `catch` blocks call `console.error` with a fixed message and the error, which shows in `wrangler dev` and in the Worker's logs, so an operator can see why a 500 happened (locally: `lead: D1 insert failed: ... no such table: leads`). The message contains no form values; the response stays a generic code. A bad request body is the client's fault and is not logged.
- Counting: the Analytics Engine write is in its own `try` after the insert. If it throws, the error is logged and the visitor still gets success, because the lead is stored and an error would make them submit again; the count is only a statistic. Tested by removing the binding: the row was stored, the answer was `{"ok":true}`, the log said `lead: conversion not counted`.
- Errors are short codes. They do not echo input or explain which check failed beyond what the visitor can fix.

`src/index.js`:

```js
import { handleLead } from "./lead.js";

export default {
  async fetch(req, env) {
    const url = new URL(req.url);

    if (url.pathname === "/api/lead") return handleLead(req, env);

    // For a call to action that is a link (checkout, booking page): the page sends a beacon here.
    if (url.pathname === "/api/convert") {
      if (req.method !== "POST") return new Response(null, { status: 405, headers: { Allow: "POST" } });
      try {
        env.EVENTS.writeDataPoint({ indexes: ["none"], blobs: ["none", "conversion"], doubles: [1] });
      } catch (err) {
        console.error("convert: conversion not counted:", err);
      }
      return new Response(null, { status: 204 });
    }

    return new Response("Not Found", { status: 404 });
  },
};
```

If the page also runs an A/B test, replace this file with the one in `ab-testing.md`; it imports the same `lead.js`.

`wrangler.jsonc` (set `compatibility_date` to today's date; replace `./dist` with the build's output folder):

```jsonc
{
  "name": "landing",
  "main": "src/index.js",
  "compatibility_date": "2026-10-04",
  "assets": {
    "directory": "./dist",
    "binding": "ASSETS",
    "not_found_handling": "404-page",
    "run_worker_first": ["/api/*"]
  },
  "d1_databases": [
    { "binding": "DB", "database_name": "landing-leads", "database_id": "<unique-ID>" }
  ],
  "analytics_engine_datasets": [
    { "binding": "EVENTS", "dataset": "landing_events" }
  ]
}
```

`run_worker_first: ["/api/*"]` sends only the API paths to the Worker. Every page, image and stylesheet stays on the static asset path, which is free and unlimited, while Worker requests count against 100,000 a day on the Free plan.

`schema.sql`:

```sql
CREATE TABLE IF NOT EXISTS leads (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  email TEXT NOT NULL UNIQUE,
  created_at TEXT NOT NULL
);
```

`.dev.vars` (local only, never committed):

```
TURNSTILE_SECRET="1x0000000000000000000000000000000AA"
```

## Every field the form submits

The rule: every field the form submits is listed in `FIELDS`, validated, capped, and stored in its own column. Nothing is stored unvalidated, and nothing is dropped. A field the page sends that the Worker does not handle is a failed launch-checklist item, not a pass, even though the Worker now refuses such a post with `unknown_field`: the visitor still loses the submission.

Take the field names from the build's hand-off, or from the `name` attributes of the built form (`cf-turnstile-response` and `leave_blank` are control fields, not data). For each one decide:

- `required`: the same as the form's `required` attribute.
- `max`: a length cap in characters. Every field gets one.
- `valid`: a check when the field has a format you can check (email, phone digits, a postcode pattern the business gave you). When a field has no meaningful format (a name, a free-text message), store it as text with only the length cap. Do not invent a strict pattern: names and phone numbers come in more shapes than a pattern expects, and a rejected real visitor is a lost lead.
- `lower`: only for values that compare case-insensitively, like email.
- `multiline`: only for a `<textarea>`; it allows line breaks and tabs.

And decide `UNIQUE_BY`, which is the difference between the two kinds of form:

- **Sign-up list** (newsletter, early access): one row per address. `UNIQUE_BY = "email"` and `email TEXT NOT NULL UNIQUE` in the table. A repeat sign-up is answered as success and stores nothing, by design.
- **Enquiry form** (contact, quote, booking request): one row per submission. `UNIQUE_BY = null` and no `UNIQUE` on any column, because one person may write twice and both messages matter.

Then size `MAX_BYTES`: about 9 bytes for each character of every field's `max` added together (a plain form post percent-encodes non-ASCII text, `ñ` becomes `%C3%B1`), plus 4,096 for the Turnstile token and encoding overhead. Email only: 9 × 254 + 4,096 ≈ 6,400, so the default 8,192 holds.

Example (invented: a piano tuner's enquiry form with email plus three text fields, `name`, `phone`, `message`). This exact configuration was run locally:

```js
const FIELDS = {
  email: { required: true, max: 254, lower: true, valid: (v) => EMAIL_RE.test(v) },
  name: { required: true, max: 100 },
  phone: { required: false, max: 30, valid: (v) => /^[+\d][\d\s().-]{5,29}$/.test(v) },
  message: { required: false, max: 2000, multiline: true },
};
const UNIQUE_BY = null;
```

```js
const MAX_BYTES = 32768; // 9 × (254 + 100 + 30 + 2000) + 4096 = 25,552, rounded up
```

New table:

```sql
CREATE TABLE IF NOT EXISTS leads (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  email TEXT NOT NULL,
  name TEXT NOT NULL,
  phone TEXT,
  message TEXT,
  created_at TEXT NOT NULL
);
```

There is no `UNIQUE` on `email` here, because this is an enquiry form: a second enquiry from the same address is stored as its own row (tested) and counts as a conversion.

A table that already exists (the email-only sign-up schema, already applied) needs one of two changes.

**It stays a sign-up list and gains fields** (say, an optional `name`): add each column with `ALTER TABLE`. The `UNIQUE` on `email` stays, which is what a sign-up list wants. Tested: the new field stored, a repeat address answered as success with one row.

```sql
ALTER TABLE leads ADD COLUMN name TEXT;
```

Added columns cannot be `NOT NULL` without a default; the Worker's `required` check enforces presence instead.

**It becomes an enquiry form:** `ALTER TABLE` is not enough. SQLite cannot drop the old `UNIQUE(email)`, so the second enquiry from an address fails (tested: `500 server_error`, not a false success). Rebuild the table: create the new one, copy every existing column, drop the old one, rename.

First list the columns the table has now, because earlier `ALTER TABLE` runs may have added some:

```bash
npx wrangler d1 execute landing-leads --local --command "PRAGMA table_info(leads)"
```

Every column in that list goes into both column lists of the `INSERT ... SELECT`, so no stored data is left behind. The new columns (here `phone` and `message`) are not in the old table and start empty. For a table with `id`, `email`, `created_at` and an added `name`, `rebuild.sql` is:

```sql
CREATE TABLE leads_new (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  email TEXT NOT NULL,
  name TEXT,
  phone TEXT,
  message TEXT,
  created_at TEXT NOT NULL
);
INSERT INTO leads_new (id, email, name, created_at) SELECT id, email, name, created_at FROM leads;
DROP TABLE leads;
ALTER TABLE leads_new RENAME TO leads;
```

Tested on a table with three rows whose `name` column had been added by `ALTER TABLE` and held data: before, `PRAGMA table_info` listed `id, email, created_at, name`; after the four statements, every row kept its `id`, `email`, `name` and `created_at`, `phone` and `message` were `NULL`, and two enquiries from an address already in the table were both stored as new rows.

The order, locally first and then on the account with the user's go-ahead for each step:

1. Export the current rows if the table holds real leads.
2. List the columns (`PRAGMA table_info(leads)`, with `--remote` for the live table) and write `rebuild.sql` from that list.
3. Run it locally on a copy of the schema with test rows, and check the rows before and after.
4. Change `lead.js` to the enquiry configuration (`UNIQUE_BY = null`, the new `FIELDS`) and test it locally against the rebuilt table.
5. Run `rebuild.sql` with `--remote`, then deploy straight away. Between the two, the old Worker's `ON CONFLICT(email)` has no unique constraint to match and its inserts fail, so keep that gap short; the failures reach the error page, they are not stored as false successes.

Results with this configuration: all four fields stored, including a 2,000-character message of `ñ` sent as a plain form post (12,113 bytes uploaded, `303` to `/thanks/`); `phone` and `message` left out stored as `NULL`; missing `name` gave `invalid_name`; `phone=call-me` gave `invalid_phone`; a 2,001-character message gave `invalid_message`; an extra `company` field gave `unknown_field`; the rebuild path stored all four fields in the old table.

A form with no email field (a business that phones back) removes `email` from `FIELDS`, makes `phone` required, sets `UNIQUE_BY = null`, and leaves `UNIQUE` out of the schema.

## Run it locally

Local runs need no Cloudflare account and no login. Install Wrangler in the project (`npm install --save-dev wrangler`), then:

```bash
npx wrangler d1 execute landing-leads --local --file=./schema.sql
npx wrangler dev
```

Turnstile test keys work on `localhost`:

| Sitekey (HTML) | Behaviour |
|---|---|
| `1x00000000000000000000AA` | always passes, visible |
| `2x00000000000000000000AB` | always fails, visible |
| `1x00000000000000000000BB` | always passes, invisible |
| `3x00000000000000000000FF` | forces an interactive challenge |

| Secret (`.dev.vars`) | Behaviour |
|---|---|
| `1x0000000000000000000000000000000AA` | always passes |
| `2x0000000000000000000000000000000AA` | always fails |
| `3x0000000000000000000000000000000AA` | "token already spent" error |

With a test secret, any token passes or fails as the secret says, so curl can send the dummy token `XXXX.DUMMY.TOKEN.XXXX`. Production secrets reject it. Run these and compare:

```bash
# valid, fetch client: 200 {"ok":true}
curl -s -i -X POST http://localhost:8787/api/lead -H "Accept: application/json" -d email=ana@example.com -d cf-turnstile-response=XXXX.DUMMY.TOKEN.XXXX
# valid, plain form post: 303 to /thanks/
curl -s -i -X POST http://localhost:8787/api/lead -d email=ana@example.com -d cf-turnstile-response=XXXX.DUMMY.TOKEN.XXXX
# invalid email: 400 {"ok":false,"error":"invalid_email"}; without the Accept header, 303 to /form-error/
curl -s -i -X POST http://localhost:8787/api/lead -H "Accept: application/json" -d email=not-an-email -d cf-turnstile-response=XXXX.DUMMY.TOKEN.XXXX
# no token: 400 {"ok":false,"error":"invalid_token"}
curl -s -i -X POST http://localhost:8787/api/lead -H "Accept: application/json" -d email=ana@example.com
# rows stored: one per address, however many times it was sent
npx wrangler d1 execute landing-leads --local --command "SELECT email FROM leads"
```

Then switch `.dev.vars` to the failing secret `2x0000000000000000000000000000000AA`, restart `wrangler dev`, and post again: expect `403 {"ok":false,"error":"verification_failed"}`. Finally open the page in a browser, submit once with JavaScript on and once with it off, and confirm the status message and the two result pages.

`.dev.vars` values override `vars` from `wrangler.jsonc` in local dev (checked: a `TURNSTILE_HOSTNAME` var of `landing.example` failed locally, and setting `TURNSTILE_HOSTNAME="example.com"` in `.dev.vars` made it pass). So you can set the production hostname in `vars` and keep local runs working.

## Production setup (needs the user)

Steps 1, 2, 4 and 6 change or read the user's Cloudflare account. Prepare the files, explain each step, and run those only after the user says yes to that step. Never paste a production secret into a file that is committed.

1. Turnstile widget: the user creates one in the dashboard (Turnstile page) for the production hostname and gives you the sitekey. The secret key stays with them. The Free plan allows 20 widgets per account and 10 hostnames per widget. Preview URLs are other hostnames; add them to the widget if forms must work on previews.
2. D1 database: `npx wrangler d1 create landing-leads`. Copy the printed `database_id` into `wrangler.jsonc`. Then `npx wrangler d1 execute landing-leads --remote --file=./schema.sql`.
3. Production hostname: add `"vars": { "TURNSTILE_HOSTNAME": "<the production hostname>" }` to `wrangler.jsonc` and keep the test value in `.dev.vars`.
4. Secret: `npx wrangler secret put TURNSTILE_SECRET` prompts for the value. It creates a new version and deploys it immediately. Whether it works before the Worker's first deploy is not documented, so run it right after the first `npx wrangler deploy`; until it runs, the form fails closed (`verification_failed`, error page), it never fakes success. The alternative is `npx wrangler deploy --secrets-file <file>`, with the file git-ignored.
5. Privacy notice: the page loads Turnstile's script from Cloudflare (`challenges.cloudflare.com`), which runs in the visitor's browser, and the form sends what the visitor typed to the user's Cloudflare account. The privacy notice linked next to the form names both: what is collected, by field (every `FIELDS` key, in words: email address, name, phone number, message; and the IP address too if `remoteip` was added), who processes it (Cloudflare, for bot checks and storage), and why.
6. After the deploy, submit the live form once with a real address, check the row with `npx wrangler d1 execute landing-leads --remote --command "SELECT email, created_at FROM leads"`, and delete the test row if the user wants.

## Other places to put the lead

- D1 is the default: rows the user can list and export, 100,000 rows written a day free.
- Queues (on Workers Free: 10,000 operations a day, 24-hour retention, needs a consumer Worker) when the lead must be forwarded to another system asynchronously.
- Email Service can notify the owner at a verified destination address in their own account, free on all plans. It cannot email the visitor on Free.
- KV is a poor lead store: 1,000 writes a day on Free.

## Exporting leads

When the user exports the `leads` table to CSV for a spreadsheet, escape every value. A spreadsheet treats a cell as a formula when it starts with `=`, `+`, `-` or `@`, and some also when it starts with a tab or a carriage return. An address that passes the form's check can begin with `=`, `+` or `-`, and a free-text field (a name, a message) can begin with any of these characters. Prefix such values with a single quote, or export in a format the spreadsheet does not evaluate, and tell the user why.

## What this code does not cover

- Rate limiting. Turnstile and the honeypot stop most automated posts; if abuse shows up, add rate limiting then.
- Cross-origin posts. The form posts to its own origin, so no CORS headers are needed. Do not add `Access-Control-Allow-Origin: *` to `/api/*`.
- Payments. A checkout is a link to the user's payment provider; count it with the `/api/convert` beacon in `analytics.md`.
