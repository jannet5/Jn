---
name: landing-launch
description: Use when deploying or launching a landing page, or setting up hosting, a custom domain, analytics, conversion tracking, A/B tests, or form handling for one, especially on Cloudflare.
---

# Landing launch

Prepare a built landing page for launch: hosting, page analytics, a conversion event for its one call to action, a working lead form, an optional A/B test, and a launch checklist. The skill prepares and tests everything locally. It never deploys or changes DNS without the user's go-ahead.

Cloudflare commands, config keys and limits change. The ones here were checked against Cloudflare's documentation on 2026-10-04 and the Worker code was run with Wrangler 4.147.0. Before relying on a command or a limit, confirm it in the current Cloudflare documentation, and trust the documentation where it disagrees with this skill.

## Inputs

Read the files first and ask the user only for what they leave open, in one message.

| Input | Where | If missing |
|---|---|---|
| The built page, and `/thanks/` and `/form-error/` when the form posts to this skill's Worker | the build output (`dist/`, `public/`, an export folder), written by `landing-skills:landing-build` | Run the build. If there is no page yet, stop and say the page comes first (`landing-skills:landing-build`). |
| The conversion action: form or outside page, the form's fields, the endpoint status (`none yet` or a URL), or the outside URL | `Conversion action` in `landing/brief.md` | Read it off the page: the form's `action` and the `name` of every field, or the primary link. Confirm with the user in one question. |
| Every field `name` the form submits, and the endpoint status | the build's hand-off; otherwise the `name` attributes of the built form | Read them from the built form. |
| Domain | `Domain` in `landing/brief.md` | Ask. If it stays `unknown`, use the `workers.dev` address and mark the domain items pending. |
| Contact routes, for `/form-error/` and the footer | `Contact routes` in `landing/brief.md` | Ask. Until answered, a visible `[PLACEHOLDER: ...]`, item pending. |
| Form, thanks and error messages | `landing/copy.md` | Write short, plain messages in the page's language and list them for the user to approve. |
| Hosting choice | the user | Recommend Cloudflare (below). If they decline, go to `Not using Cloudflare`. |
| Privacy notice | the page's footer and form | Ask the user for it; until then a visible `[PLACEHOLDER: ...]`, item pending. Never invent one. |

What this skill writes: hosting config, the form Worker when one is needed, the analytics snippet and conversion event, and `landing/launch.md` with the checklist. It does not write copy or rewrite the build's pages.

Identify the stack (plain HTML, Astro, Vite with React, Next.js) and its output folder before writing any config.

## Recommendation

Suggest Cloudflare for all three jobs, and say why in these terms:

- **Hosting:** Workers static assets serve the page; static requests are free and unlimited on the Free plan, and Cloudflare recommends Workers over Pages for new projects. The same project can run a small Worker for the form, so the page needs no other backend.
- **Analytics:** Cloudflare Web Analytics counts traffic and Core Web Vitals without cookies or fingerprinting. It has no custom events, so the conversion is counted by the Worker in Workers Analytics Engine, in the same account, free on the Free plan.
- **A/B testing:** the same Worker can assign variants at the edge before the page is served. No client-side testing script, no flicker, and it works with JavaScript off.

State the costs too: a custom domain needs the domain's DNS on Cloudflare, and every request that runs the Worker counts against 100,000 a day on the Free plan.

## Process

1. **Read the inputs.** From `landing/brief.md`, take the conversion action (form or outside page, its fields, the endpoint status), the domain and the contact routes. From the built page, note every form with its `action` and field names, and every call-to-action link. Ask only for what is still missing.

   Then pick the case, which decides what you add:
   - **Form, endpoint `none yet`** (it posts to `/api/lead`, which nothing serves yet): the form Worker, steps 4 and 5.
   - **Form, endpoint exists:** no form Worker and no Turnstile. Check that the form posts to that endpoint and that the result pages match what it returns, and record that validation and spam protection are that endpoint's job.
   - **Outside page** (checkout, booking, app store): no form to handle. The link goes to the real URL and the click is counted with the beacon in step 5; the form items of the checklist are `n/a`.

2. **Fix the dead ends first.** Search the build for a form whose action goes nowhere or whose submit is cancelled, a submit handler that only shows a thank-you message, links to a bare `#`, and buttons that link to the section they sit in. Each of these means the conversion does not exist. A form gets the Worker endpoint in step 4; a link gets the user's real URL, or a visible `[PLACEHOLDER: ...]` and a pending checklist item.

3. **Hosting config.** Write `wrangler.jsonc` for the stack, install Wrangler in the project, add `404.html` if missing, and add `_headers` or `_redirects` only when there is something to put in them. Read `references/hosting.md`.

4. **Form endpoint (form, endpoint `none yet`).** Add the form Worker: server validation, Turnstile verification with Siteverify, D1 storage, and the JavaScript and no-JavaScript response paths. Add the Turnstile widget to the build's form; keep the build's markup and enhancement script. Check that `/thanks/` and `/form-error/` exist and that `/form-error/` offers a contact route from the brief. Create a result page only if it is missing, and never overwrite one: `landing-skills:landing-build` owns them. Read `references/forms.md`.

   **Every field gets handled.** List every field `name` the form submits and give each an entry in the Worker's `FIELDS` table (required or not, a length cap, a format check where one is meaningful, otherwise text with a cap), a column in the `leads` table, and a `MAX_BYTES` that fits them. Nothing is stored unvalidated. A submitted field the Worker does not handle fails the checklist; the Worker refuses such a post with `unknown_field`, which makes the gap visible in testing, but the visitor's submission is still lost until the field is added.

5. **Analytics and the conversion event.** Add the Web Analytics snippet to every page and record the conversion in Analytics Engine: in the form Worker when you added one, by beacon for a link. With an existing endpoint, the conversion is counted from that endpoint's records, and the beacon is an option. Read `references/analytics.md`.

6. **A/B test, only if asked.** Agree the one change, the baseline, the smallest difference worth acting on and the sample size before writing any code. Then add the variant file and the A/B Worker. Read `references/ab-testing.md`.

7. **Test locally.** Run `npx wrangler dev` with Turnstile test keys in `.dev.vars` and exercise every path with curl and a browser: valid post with every field, invalid email, each field over its cap, failed Turnstile, JavaScript off, and for a test: assignment, the sticky cookie on a second request, the conversion endpoint. Then run `npx wrangler deploy --dry-run` and check the asset count and size. Local runs need no account. Fix anything that fails before going on.

8. **Stop for the go-ahead.** Deploying, creating a preview, adding a custom domain or route, creating a D1 database, running `d1 execute --remote`, setting a secret, creating the Turnstile widget, and adding the site to Web Analytics all change the user's Cloudflare account, and a deploy or DNS change is visible to the public at once. The user may be mid-review, the domain may still serve an old site, and a wrong DNS change can take a live site down. So list each outward-facing step with its exact command and what it will change, and run each one only when the user says yes to it at that moment. A yes to one step is not a yes to the next. If the user prefers to run them, hand over the commands in order.

9. **After deploy (with the user).** Submit the live form once, confirm the row in D1 and the conversion in Analytics Engine, confirm Web Analytics shows the visit, and check the items in the checklist that can only be checked live.

10. **Write `landing/launch.md`.** Record the hosting setup, the conversion action and how it is counted, the A/B plan if any, the commands for the outward-facing steps, and the launch checklist with each item marked `done`, `pending`, `fail` or `n/a` and the reason for every one that is not `done`. Show the checklist to the user.

## Launch checklist

Copy this into `landing/launch.md` and mark every line `done`, `pending (reason)`, `fail (reason)` or `n/a (reason)`. An item you could not check is pending, never done. The form items are `n/a` when the call to action is an outside page.

Conversion
- [ ] The call to action leads to a working action: the form posts to `/api/lead` (or the user's existing endpoint), or the link goes to the user's real URL. No `href="#"`, no self-anchor, no fake success.
- [ ] Every field `name` the form submits has a `FIELDS` entry with a length cap and a `leads` column. Any field not handled is a fail, never a pass.
- [ ] With an existing endpoint: it answers JSON with `ok` for `Accept: application/json` and redirects to `/thanks/` or `/form-error/` otherwise, or the result pages match what it does return; validation and spam protection are recorded as its job.
- [ ] When the form posts to this skill's Worker: `/thanks/` and `/form-error/` exist (the build's own pages, not overwritten), and `/form-error/` offers a contact route from the brief. With an existing endpoint, the pages match what it returns; with an outside link, `n/a`.
- [ ] Form tested locally: valid post, invalid email, failed Turnstile, JavaScript off (lands on `/form-error/`).
- [ ] The button disables while sending; success and error are announced in a `role="status"` element.
- [ ] The honeypot is hidden from sight and from assistive technology.
- [ ] Live form tested once after deploy; the row is in D1.
- [ ] No promise of a confirmation email unless the user has a way to send one.

Measurement
- [ ] Web Analytics snippet live with the real token on every page (pending: the snippet sits in an HTML comment until the token exists), including `/thanks/`, `/form-error/` and `404.html`.
- [ ] The conversion is written to Analytics Engine, and a live test conversion shows up in the SQL query.
- [ ] Web Analytics shows the live visit.
- [ ] During an A/B test: visitor and conversion counts per variant appear, and the sample size and end rule are written in `landing/launch.md`.

Hosting
- [ ] `npx wrangler deploy --dry-run` passes; asset files under 20,000, none over 25 MiB.
- [ ] Custom domain serves the page over HTTPS; `workers_dev` set as the user wants.
- [ ] Redirects in `_redirects` tested live.
- [ ] Unknown paths return the custom `404.html` with status 404.

Page
- [ ] Title, meta description, canonical URL and Open Graph and social card tags present; the card image loads from the live URL and the preview looks right when the link is pasted into a sharing debugger or a chat app.
- [ ] `robots.txt` and a sitemap that lists the live URL; result pages and previews are `noindex`.
- [ ] Favicon present.
- [ ] Privacy notice linked next to the form and in the footer, naming Turnstile (a Cloudflare script) and every field the form stores, by name; contact details (email, phone or the messaging channel the audience actually uses) and who runs the site.
- [ ] Any "no cookies" statement is still true (it is not while an A/B test sets its cookie).
- [ ] Every `[PLACEHOLDER: ...]` resolved: real photos and screenshots in place, prices and dates confirmed.

Accounts and secrets
- [ ] Turnstile widget created for the production hostname; the real sitekey is in the HTML.
- [ ] `TURNSTILE_SECRET` set with `wrangler secret put` (never committed); `.dev.vars*` and `.wrangler/` in `.gitignore`.
- [ ] Every outward-facing step was confirmed by the user before it ran.

## Not using Cloudflare

The build is plain static output: HTML, CSS, JavaScript and assets in one folder. Any static host or the user's own server can serve it as it is.

If the user does not want Cloudflare, add no Cloudflare files: no `wrangler.jsonc`, no Worker, no Turnstile, no Web Analytics snippet, no `.dev.vars`. Remove any you added earlier in the session. Then go straight to the launch checklist and work through the items that do not name a Cloudflare product. For the form and the conversion, the checklist items still apply: the form needs a real endpoint on the user's server or service, validated on the server, and the conversion needs a way to be counted. Name what is missing, mark it pending, and leave the choice of tool to the user. This skill does not give recipes for other hosts.

## Self-check

Before you report:

- Nothing was deployed, previewed, routed or created in the user's account without a yes for that step.
- Every config change passed `npx wrangler deploy --dry-run`, and the local curl checks gave the expected status codes.
- No file claims that Web Analytics counts clicks or conversions.
- No secret, token or account id is written into a committed file.
- `landing/launch.md` exists, and every checklist line is marked, with a reason for each that is not `done`, and no submitted form field is left unhandled.

## References

- `references/hosting.md`: config per stack, commands, the dry run, custom domain, `_redirects` and `_headers`, caching, 404, previews. Read at step 3.
- `references/forms.md`: the form markup, the tested form Worker, local tests with Turnstile test keys, production setup. Read at step 4 when the page has a form.
- `references/analytics.md`: Web Analytics setup, the conversion event in Analytics Engine, the query, privacy statements, what to check after deploy. Read at step 5.
- `references/ab-testing.md`: the cost on the Free plan, the tested A/B Worker, measuring per variant, deciding when the test is finished, ending it. Read at step 6, and before agreeing to run a test.
