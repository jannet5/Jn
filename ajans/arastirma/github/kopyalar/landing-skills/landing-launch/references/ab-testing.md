# A/B testing at the edge

## Contents

- Before you start
- What it costs on the Free plan
- How the test works
- Files
- The Worker
- Run it locally
- Measuring conversion per variant
- Deciding the test is finished
- Ending the test
- Cookies and privacy
- Copy-only variants

## Before you start

Run a test only when the user asks for one or the brief names a question worth answering (two headlines, two offers). A test needs one change, one conversion event, and enough traffic to finish; see "Deciding the test is finished" before agreeing to run one on a new page with little traffic. With too few visitors the test cannot tell the variants apart, and the honest answer is to launch one version and revisit later.

## What it costs on the Free plan

Static assets are free and unlimited. A path listed in `run_worker_first` always runs the Worker, and Worker requests are limited to 100,000 a day per account on the Free plan. Above that, worker-first requests get a `429 Too Many Requests` instead of falling back to the static file. An A/B test on `/` makes every view of the home page a Worker request, so on a busy day the page itself can fail.

So:

- List exact paths: `["/", "/api/*", "/variants/*"]`. Never `"run_worker_first": true` on a landing page; it would send every image and stylesheet through the Worker too.
- Tell the user the limit and how many daily page views they expect. If they expect more than the limit, the test needs the Workers Paid plan or a smaller scope.
- Each request also has 10 ms of CPU time on Free. Keep the Worker to cookie parsing and one asset fetch, as below.

## How the test works

- Variant A is the page as built, at `/index.html`. Variant B is a second copy at `<assets>/variants/b/index.html` with the one change.
- On a request for `/` the Worker reads the cookie `ab_hero-v1`. With no cookie it picks `a` or `b` at random (50/50), serves that file, and sets the cookie for 30 days so the visitor sees the same version on every visit.
- The response carries `Cache-Control: private, no-store`, so no shared cache hands one visitor's variant to everyone. This is a defensive choice, not Cloudflare guidance: the docs say a `Set-Cookie` or `private`/`no-store` response bypasses Cloudflare's cache, and do not give an official header for A/B responses. Repeat visitors carry no `Set-Cookie`, so the header is what keeps their responses out of shared caches.
- The cookie is `HttpOnly`: only the Worker reads it, so page scripts have no access.
- `HEAD /` (link checkers, uptime monitors) is served a variant but gets no cookie and is not counted.
- Each Analytics Engine write is in a `try`: if counting fails, the error is logged and the page or the `204` is still served, because a statistic must never take the page down.
- The first assignment writes a `visitor` data point; a stored lead or a call-to-action beacon writes a `conversion` data point with the visitor's variant.
- `/variants/*` is routed to the Worker, which returns 404, so nobody reaches variant B directly or gets it indexed outside the test.

Write variant B's links and asset paths so they resolve from `/`, because the browser shows `/` while it renders the file from `/variants/b/`. Root-relative paths (`/styles.css`) are safest. Copy the head of variant A unchanged, including the canonical link to `/`.

Cloudflare's own A/B example works the same way with the cookie, but routes to `/control` and `/test` path prefixes on an origin server through `fetch()`. The version here reads from the static assets binding instead, because the page is static assets.

## Files

Keep `src/lead.js`, `schema.sql` and `.dev.vars` from `forms.md` as they are. Replace `src/index.js` with the Worker below, and widen `run_worker_first`:

```jsonc
{
  "name": "landing",
  "main": "src/index.js",
  "compatibility_date": "2026-10-04",
  "assets": {
    "directory": "./dist",
    "binding": "ASSETS",
    "not_found_handling": "404-page",
    "run_worker_first": ["/", "/api/*", "/variants/*"]
  },
  "d1_databases": [
    { "binding": "DB", "database_name": "landing-leads", "database_id": "<unique-ID>" }
  ],
  "analytics_engine_datasets": [
    { "binding": "EVENTS", "dataset": "landing_events" }
  ]
}
```

Set `compatibility_date` to today's date and `directory` to the build's output folder. A page with no form can drop the `d1_databases` block and the `/api/lead` line from the Worker.

## The Worker

`src/index.js`:

```js
import { handleLead } from "./lead.js";

// Change EXPERIMENT for each new test so old cookies and old data points do not mix in.
const EXPERIMENT = "hero-v1";
const COOKIE = "ab_" + EXPERIMENT;

function readVariant(req) {
  const m = (req.headers.get("Cookie") ?? "").match(new RegExp(`(?:^|;\\s*)${COOKIE}=(a|b)(?:;|$)`));
  return m ? m[1] : null;
}

export default {
  async fetch(req, env) {
    const url = new URL(req.url);
    const variant = readVariant(req);

    if (url.pathname === "/api/lead") return handleLead(req, env, EXPERIMENT, variant ?? "none");

    if (url.pathname === "/api/convert") {
      if (req.method !== "POST") return new Response(null, { status: 405, headers: { Allow: "POST" } });
      try {
        env.EVENTS.writeDataPoint({ indexes: [EXPERIMENT], blobs: [variant ?? "none", "conversion"], doubles: [1] });
      } catch (err) {
        console.error("convert: conversion not counted:", err);
      }
      return new Response(null, { status: 204 });
    }

    if (url.pathname !== "/") return new Response("Not Found", { status: 404 });

    // "/" only. Variant a is the page at /index.html; variant b lives at /variants/b/index.html.
    const assigned = variant ?? (Math.random() < 0.5 ? "a" : "b");
    const assetUrl = new URL(assigned === "a" ? "/" : "/variants/b/", url);
    const res = await env.ASSETS.fetch(new Request(assetUrl, req));
    const out = new Response(res.body, res);
    out.headers.set("Cache-Control", "private, no-store");
    if (!variant && res.ok && req.method === "GET") {
      // First visit: set the sticky cookie and count one visitor for this variant.
      // HEAD requests (link checkers, monitors) are served but neither assigned nor counted.
      out.headers.append("Set-Cookie", `${COOKIE}=${assigned}; Path=/; Max-Age=2592000; HttpOnly; Secure; SameSite=Lax`);
      try {
        env.EVENTS.writeDataPoint({ indexes: [EXPERIMENT], blobs: [assigned, "visitor"], doubles: [1] });
      } catch (err) {
        console.error("ab: visitor not counted:", err); // the page is still served
      }
    }
    return out;
  },
};
```

Behaviour checked with `npx wrangler dev` (Wrangler 4.147.0):

- `ASSETS.fetch` of `/variants/b/` returns `variants/b/index.html` with status 200 (no trailing-slash redirect).
- `/?utm_source=x` runs the Worker: `run_worker_first` matches the path, whatever the query string.
- `/index.html` is answered by the asset layer with a `307` redirect to `/`, which then runs the Worker. Variant A is not reachable around the test.
- Other pages (`/thanks/`) skip the Worker and keep the default `Cache-Control: public, max-age=0, must-revalidate`.

## Run it locally

```bash
npx wrangler dev
# first visit: Set-Cookie ab_hero-v1=a or b, Cache-Control: private, no-store
curl -s -i -c jar.txt http://localhost:8787/ | grep -i -E 'HTTP/|cache-control|set-cookie|<h1>'
# second visit with the cookie: same variant, no new Set-Cookie
curl -s -i -b jar.txt http://localhost:8787/ | grep -i -E 'HTTP/|cache-control|set-cookie|<h1>'
# forty new visitors: roughly half each (the local run gave 20 and 20)
for i in $(seq 1 40); do curl -s http://localhost:8787/ | grep -o '<h1>[^<]*'; done | sort | uniq -c
# direct access to the variant folder: 404
curl -s -i http://localhost:8787/variants/b/ | head -1
# conversion beacon: 204
curl -s -i -X POST -b 'ab_hero-v1=b' http://localhost:8787/api/convert | head -1
```

Use something that differs between the two files in place of `<h1>` if the headline is not the change.

## Measuring conversion per variant

With the SQL API call from `analytics.md`:

```sql
SELECT blob1 AS variant, blob2 AS event, SUM(_sample_interval) AS n
FROM landing_events
WHERE index1 = 'hero-v1'
GROUP BY variant, event
```

Conversion rate of a variant = its `conversion` count divided by its `visitor` count. Limits of these numbers:

- `visitor` counts first `GET` assignments. It includes bots and crawlers that fetch the page, and a visitor who blocks cookies is assigned again on each view and counted again.
- The variant on a conversion comes from the cookie the browser sends, and the client controls that cookie. Anyone can post to `/api/convert` with any variant, and a form post can carry a forged cookie. Both counts are approximate and can be forged.
- What that means for the decision: a lead conversion has passed Turnstile and the server's checks and is counted once per address, so it is the sturdier signal. A link call to action counted by beacon has neither check. Before trusting a link-CTA result, compare it with a number the beacon cannot fake, such as completed orders or bookings per variant from the user's provider, and if the two disagree, believe the provider.
- A form conversion is counted once per new email address on a sign-up list, and once per stored submission on an enquiry form (one visitor can count twice); a link beacon is counted per click.
- Conversions from visitors who arrived before the test have variant `none`. Leave them out.

## Deciding the test is finished

The Cloudflare documentation gives no sample-size or significance guidance. What follows is standard statistical practice for comparing two conversion rates, not a Cloudflare recommendation. Explain it to the user in these terms.

Fix three things before the test starts, and write them in `landing/launch.md`:

1. **The baseline rate** `p`: the current conversion rate. With no history, use the user's best estimate and say it is an estimate.
2. **The smallest difference worth acting on** `d`, in absolute points. Going from 4% to 5% is `d = 0.01`.
3. **Visitors per variant** `n`. A common approximation for a two-sided test at the 5% significance level with 80% power is `n ≈ 16 × p × (1 − p) / d²`.

| Baseline `p` | Difference `d` | `n` per variant |
|---|---|---|
| 4% | 1 point (4% to 5%) | about 6,150 |
| 4% | 2 points (4% to 6%) | about 1,540 |
| 10% | 2 points (10% to 12%) | about 3,600 |

Divide `2n` by the daily visitors to get the duration, and round up to whole weeks so weekdays and weekends are both covered. If that comes to months, the test is too small for this traffic; test a bigger change or skip the test.

When both variants reach `n` visitors, and not before, compare them with a two-proportion z-test. With `n_a`, `n_b` visitors and `c_a`, `c_b` conversions:

```
p_a = c_a / n_a        p_b = c_b / n_b        p = (c_a + c_b) / (n_a + n_b)
z   = (p_b − p_a) / sqrt( p × (1 − p) × (1/n_a + 1/n_b) )
```

If `|z| ≥ 1.96`, the difference is significant at the 5% level. Example: 6,200 visitors each, 248 conversions for A (4.0%) and 316 for B (5.1%) gives `z ≈ 2.93`: B wins. With 280 for B (4.5%), `z ≈ 1.42`: no detectable difference, so keep the simpler or cheaper version.

Do not stop early. Checking the numbers every day and stopping the first time one variant looks ahead inflates the false-positive rate well past 5%, because random swings on small samples are large and one of them will eventually look like a win. If the user wants to look along the way, let them look, but the decision waits for `n`.

## Ending the test

1. Copy the winning file to `<assets>/index.html` (if B won) and delete `<assets>/variants/`.
2. Put `src/index.js` from `forms.md` back, and `run_worker_first` back to `["/api/*"]`, so the home page leaves the Worker and stops counting against the request limit.
3. Restore any "no cookies" statement the test made you remove.
4. For the next test, change `EXPERIMENT`. The new cookie name means old assignments do not carry over and old data points stay separate.

Deploying these changes needs the user's go-ahead like any deploy.

## Cookies and privacy

The test cookie is first-party and holds only `a` or `b`. Still, the page now sets a cookie. Any "this site sets no cookies" line becomes false while the test runs; see "Privacy statements on the page" in `analytics.md`. Whether the cookie requires consent depends on the user's jurisdiction; say so and do not decide it for them.

## Copy-only variants

When the only change is text (a headline, a button label), the Worker can keep one HTML file and rewrite it as it streams with `HTMLRewriter`: select an element and change it with `setInnerContent` or `setAttribute`, then `transform` the asset response. An exception in a handler aborts the response, so keep handlers trivial. This approach was not run for this skill, so no code is given here. Two static files, as above, are easier to review and were tested; prefer them unless keeping two copies in sync is a real burden.
