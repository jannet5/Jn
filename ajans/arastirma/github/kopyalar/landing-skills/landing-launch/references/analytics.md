# Analytics: page traffic and the conversion event

## Contents

- Two tools, and why
- Web Analytics: page traffic
- The conversion event: Workers Analytics Engine
- Reading the numbers
- Privacy statements on the page
- Check after the first deploy

## Two tools, and why

The plan for this skill was Cloudflare Web Analytics plus a conversion event for the page's single call to action. Web Analytics cannot do the second part. Its FAQ answers "Does Web Analytics support custom events?" with "Not yet". It counts visits, page views, load time and Core Web Vitals; it cannot count a click or a form submission.

So the skill uses two tools:

- **Cloudflare Web Analytics** for page traffic: a script tag on every page, no cookies.
- **Workers Analytics Engine** for the conversion, written by the Worker on the server when the conversion happens. A stored lead is counted in `lead.js`; a link call to action (checkout, booking page) sends a beacon to `/api/convert`.

Never tell the user that Web Analytics tracks clicks or conversions. It does not.

## Web Analytics: page traffic

Setup is a dashboard step for the user: Web Analytics > Add a site > enter the hostname > Manage site > copy the snippet. It gives a site token. Paste the snippet before `</body>` on every page, including `/thanks/`, `/form-error/` and `404.html`:

```html
<script
  type="module"
  src="https://static.cloudflareinsights.com/beacon.min.js"
  data-cf-beacon='{"token": "$SITE_TOKEN"}'
></script>
```

Until the user supplies the token, write the snippet inside an HTML comment on every page, with `$SITE_TOKEN` in place, and mark the checklist item pending. A live snippet with a placeholder token loads a third-party script that fails with a CORS error on every visit. Do not invent a token. Once it arrives, remove the comment markers and put the token in.

Use the manual snippet by default. Automatic injection exists for hostnames proxied through Cloudflare, but whether it works on a Workers static assets site is not documented. It also fails on responses with `Cache-Control: public, no-transform` and on DNS-only (CNAME) hostnames.

If the page sends a Content Security Policy, add `https://static.cloudflareinsights.com/beacon.min.js` to `script-src` and `cloudflareinsights.com` to `connect-src`. The manual snippet cannot be pinned with Subresource Integrity, because the script is not versioned.

What it gives the user: visits and page views, page load time, Core Web Vitals (LCP, INP, CLS; CLS only in Chromium browsers), broken down by dimensions in the dashboard. It does not log query strings, so there are no UTM campaign reports. Data is unsampled for 7 days, then aggregated to about 10%, and kept for 6 months. One token covers the subdomains of one apex domain. An account can have 10 sites that are not proxied through Cloudflare.

## The conversion event: Workers Analytics Engine

Analytics Engine stores data points that a Worker writes. Add the binding to `wrangler.jsonc` (already in the configs in `forms.md` and `ab-testing.md`); the dataset is created on the first write, with no dashboard step:

```jsonc
"analytics_engine_datasets": [
  { "binding": "EVENTS", "dataset": "landing_events" }
]
```

Every data point in this skill has the same shape:

```js
env.EVENTS.writeDataPoint({ indexes: [experiment], blobs: [variant, event], doubles: [1] });
```

- `index1` is the experiment name (`"none"` when no test runs). Analytics Engine accepts exactly one index; a call with more is dropped.
- `blob1` is the variant (`"a"`, `"b"`, or `"none"`), `blob2` the event (`"conversion"`, or `"visitor"` from the A/B Worker).
- `writeDataPoint` does not need `await`.

Pick the recording point from the brief's conversion action:

| Conversion action | Where it is recorded | Code |
|---|---|---|
| Lead form (newsletter, contact request, early-access sign-up) | In `lead.js`, after a new row is stored: once per address on a sign-up list, where a repeat address stores nothing; once per stored submission on an enquiry form | `forms.md` |
| Form that posts to the user's existing endpoint | The endpoint's own records; optionally the beacon below, sent when the enhancement script receives `ok: true` | `forms.md`, "Which case you are in" |
| Link to an outside page (checkout, booking calendar, app store) | `navigator.sendBeacon("/api/convert")` on click; the Worker writes the point | below |
| Both on one page | Both; they share the dataset and the shape | |

When the page also has the form Worker, its `src/index.js` in `forms.md` already has the `/api/convert` route. When the call to action is only a link (no form, or a form posting to an endpoint the user already has), use this beacon-only Worker instead: no D1, no Turnstile, no secret.

`src/index.js`:

```js
// Beacon-only Worker: the call to action is a link to an outside page (checkout, booking).
export default {
  async fetch(req, env) {
    const url = new URL(req.url);
    if (url.pathname !== "/api/convert") return new Response("Not Found", { status: 404 });
    if (req.method !== "POST") return new Response(null, { status: 405, headers: { Allow: "POST" } });
    try {
      env.EVENTS.writeDataPoint({ indexes: ["none"], blobs: ["none", "conversion"], doubles: [1] });
    } catch (err) {
      console.error("convert: conversion not counted:", err);
    }
    return new Response(null, { status: 204 });
  },
};
```

`wrangler.jsonc` (today's date, the build's output folder):

```jsonc
{
  "name": "landing",
  "main": "src/index.js",
  "compatibility_date": "2026-10-04",
  "assets": {
    "directory": "./dist",
    "not_found_handling": "404-page",
    "run_worker_first": ["/api/*"]
  },
  "analytics_engine_datasets": [
    { "binding": "EVENTS", "dataset": "landing_events" }
  ]
}
```

Tested locally: `POST /api/convert` answered `204`, `GET` answered `405`, `/api/lead` answered `404`, pages were served from the assets, and `npx wrangler deploy --dry-run` listed only the Analytics Engine binding.

Add this to the page, with the link's own class. Example (invented): a yoga studio whose call to action opens its booking calendar.

```html
<a class="cta-link" href="[PLACEHOLDER: booking calendar URL from the user]">Book a first class</a>
<script>
document.querySelector(".cta-link").addEventListener("click", () => navigator.sendBeacon("/api/convert"));
</script>
```

`sendBeacon` is the browser API made for sending a small request while the page is being left. Tested in a browser: the click sent `POST /api/convert`, the Worker answered `204`, and the browser followed the link. The beacon counts clicks, not completed payments; only the payment provider knows those. Anyone can post to `/api/convert`, so treat that number as approximate. A lead counted in `lead.js` is only counted after the server accepted it, which makes it the more reliable of the two.

Never point the call to action at itself (`href="#book"` on a button inside the `#book` section). If the user has not given the real URL yet, use a visible `[PLACEHOLDER: ...]` and list it as pending.

Only `/api/*` (and `/` during an A/B test) run the Worker, so the conversion endpoint costs one Worker request per conversion. Free-plan limits: 100,000 data points written and 10,000 read queries a day, 20 blobs and 20 doubles per point, 250 points per Worker invocation, retention three months. Cloudflare does not currently bill for Analytics Engine.

When the user wants a list of individual conversions (who signed up and when), that is the D1 `leads` table, not Analytics Engine.

## Reading the numbers

Analytics Engine is read through its SQL API with an API token that has the permission `Account | Account Analytics | Read`. The user creates the token; it never goes into the repository.

```bash
curl -X POST "https://api.cloudflare.com/client/v4/accounts/$ACCOUNT_ID/analytics_engine/sql" \
  -H "Authorization: Bearer $API_TOKEN" \
  -d "SELECT blob1 AS variant, blob2 AS event, SUM(_sample_interval) AS n FROM landing_events GROUP BY variant, event"
```

Count with `SUM(_sample_interval)`, not `COUNT(*)`: Analytics Engine may sample, and `_sample_interval` restores the true count. Add `WHERE index1 = 'hero-v1'` to read one experiment.

Lead totals with rows: `npx wrangler d1 execute landing-leads --remote --command "SELECT COUNT(*) FROM leads"`. This reads the user's account, so the user runs it or approves it.

## Privacy statements on the page

What is documented: Web Analytics "does not collect or use your visitors' personal data", uses no cookies or `localStorage`, and does not fingerprint by IP address or user agent. A footer line such as "Visits are counted with Cloudflare Web Analytics, which sets no cookies" is accurate for the beacon.

It stops being accurate the moment the A/B Worker runs: that Worker sets a first-party cookie (`ab_<experiment>`, 30 days) to remember the variant. While a test runs, remove any "no cookies" claim or rewrite it to name the test cookie and its purpose. Restore it when the test ends and the cookie is no longer set.

The form adds two more things the privacy notice must name: Turnstile, a script loaded from Cloudflare that runs a bot check in the visitor's browser, and every field the form stores in the user's Cloudflare D1 database, by name (the `FIELDS` keys in `forms.md`, in words). The form Worker in `forms.md` does not send the visitor's IP address to Siteverify; if `remoteip` is added, name that as well.

Whether any of this removes the need for a consent banner is a legal question the documentation does not answer. Do not tell the user a banner is unnecessary; tell them it depends on their jurisdiction and to check. A page that collects email addresses also needs a privacy notice linked next to the form and in the footer.

## Check after the first deploy

Local `wrangler dev` accepts `writeDataPoint` calls without error but stores nothing you can read back. These lines were therefore exercised locally only up to the call, and must be checked on the live site:

- `lead.js`: `env.EVENTS.writeDataPoint({ indexes: [experiment], blobs: [variant, "conversion"], doubles: [1] });`
- `index.js` (`forms.md`): the `/api/convert` `writeDataPoint` line.
- The beacon-only `index.js` above: its `writeDataPoint` line.
- A/B `index.js`: the `/api/convert` line and the `"visitor"` line.

After deploying: load the page, submit the form once (or click the link call to action), wait a few minutes, run the SQL query above and confirm a `conversion` row (and `visitor` rows during a test). Check the Web Analytics dashboard shows the visit. Data can take a few minutes to appear.
