---
name: landing-page
description: Use when asked to build, create, design, or redesign a landing page, marketing page, product page, waitlist page, or homepage, or when starting landing page work without a brief, approved copy, or a design direction in place.
---

# Landing page

This is the entry point for landing page work. Settle the brief in
`landing/brief.md`, then run the six skills of this plugin in a fixed order,
so the page is never laid out before its copy or coded before its direction.

Start like this. If `landing/brief.md` exists, read it and ask only for what
it lacks. Otherwise ask first (see "When information is missing"), then write
it. Read the project too (README, `package.json`, an existing site) and do
not ask for what it already answers. No copy, design or code before the
brief is written.

## The brief

One line per field, in the user's words:

1. Type: SaaS, product, service or studio, waitlist, event, or a mix.
2. Language: the language with its country or regional variant (`es-CL`,
   `en-GB`). For Spanish, also the form of address (tú, usted, vos), because
   vocabulary, number format and currency change with the country.
3. Product: what it does, with every fact the user gives: features, numbers,
   places, dates.
4. Audience: who it is for, and what they use or do today.
5. Conversion action: the single action, and what the click does. Either it
   posts a form (which fields, and the endpoint if one exists, or `none yet`),
   or it opens an outside page (checkout, booking, app store) at a given URL.
6. Pricing: the prices in the structure the business offers.
7. Proof available: testimonials, metrics, customer names, logos, press,
   exactly as supplied.
8. Assets available: photos, screenshots, logo, video, 3D models, with paths.
9. Brand constraints: colours, typefaces, logo rules, voice material, and
   anything required or ruled out.
10. Stack: the stack the user named. If they named none, write `detect`;
    `landing-skills:landing-build` detects it from the project files, owns
    that table, and writes the resolved stack back into this field.
11. Domain: the production domain. `landing-skills:landing-build` writes the
    canonical URL, `og:url`, `og:image` and the sitemap only when it is known.
12. Contact routes: email, phone, address, messaging channel, hours, as given.
    They go in the footer and on the form's error page.
13. Wanted moments: motion, 3D or scroll effects the user asked for, in their
    words. These are not claims for the copy: the direction decides their
    role and `landing-skills:landing-motion` builds them.

Write `none` when the user said there is none, and `unknown` when nobody
answered. An invented example:

```markdown
# Brief: Keyline Tuning
Type: service
Language: en-GB
Product: piano tuning at the customer's home in Greater Manchester; one tuning takes about 90 minutes
Audience: people with an upright piano at home who have not had it tuned in years
Conversion action: book a visit through a form (name, postcode, phone, preferred week); endpoint: none yet
Pricing: unknown
Proof available: none
Assets available: none
Brand constraints: unknown
Stack: detect
Domain: unknown
Contact routes: phone, number unknown
Wanted moments: none
```

## Order of work

1. Content, `landing-skills:landing-copy`. The copy decides the sections,
   their order and their length; a layout drawn first is a template the copy
   gets bent to fit. Ask the user to approve `landing/copy.md` or answer its
   open questions. Unattended, go on and say the copy is unapproved.
2. Design, `landing-skills:landing-art-direction`. The direction decides the
   tokens; code written before them hardcodes default colours and fonts.
3. Code, `landing-skills:landing-build`. The static page must work with
   JavaScript off before motion is added, because it is the fallback when a
   script fails, WebGL is missing, or the visitor asks for reduced motion.
4. Motion, `landing-skills:landing-motion`. Always pass through it. Under a
   motion intent of `none` it is a short check of feedback states.
5. Review, `landing-skills:landing-review`, before calling the work done. A
   page that looks finished can still discard every lead or carry a claim
   nobody gave. Done means `landing/review.md` starts with `Verdict: pass`.
6. Launch, `landing-skills:landing-launch`. It prepares and tests launch
   locally; deploys, DNS and account changes run only with the user's
   explicit go-ahead, a rule that skill owns. Unattended, with nobody to
   ask, stop after preparing and report what is ready to run.

After a `fail`, send each finding to the skill named by its Area (`copy`,
`art-direction`, `build`, `motion` or `launch`: `landing-skills:landing-` plus
the Area), then review again. A fix passes through the steps after it: a
section added to the copy needs a layout row and markup. After `incomplete`,
get the page served and review again. After two failed rounds, stop and
report the open findings to the user instead of looping.

## Handoffs

| Step | Skill | Reads | Writes |
|---|---|---|---|
| Brief | `landing-skills:landing-page` | the conversation, the project | `landing/brief.md` |
| Content | `landing-skills:landing-copy` | `landing/brief.md` | `landing/copy.md`: one H2 per section in page order, last H2 `Placeholders` |
| Design | `landing-skills:landing-art-direction` | `landing/brief.md`, `landing/copy.md` | `landing/direction.md`, last H2 `Motion and 3D intent`; `landing/tokens.css` |
| Code | `landing-skills:landing-build` | `landing/brief.md`, `landing/copy.md`, `landing/direction.md`, `landing/tokens.css` | the static page; for a form, `form.js`, `/thanks/` and `/form-error/`; plain HTML goes in `public/` with a copy of `tokens.css` |
| Motion | `landing-skills:landing-motion` | `Motion and 3D intent` in `landing/direction.md`, the static page, `landing/tokens.css` | motion on the page, in its own files; `tokens.css` unchanged |
| Review | `landing-skills:landing-review` | the page, `landing/brief.md`, `landing/copy.md`, `landing/direction.md`, `landing/tokens.css` | `landing/review.md`, `landing/review-checklist.md`, screenshots in `landing/shots/` |
| Launch | `landing-skills:landing-launch` | the built page and its form field names; conversion action, domain and contact routes in `landing/brief.md`; form messages in `landing/copy.md` | hosting config; when the form has no endpoint, the form Worker and the Turnstile widget added to the build's form; analytics and the conversion event; `landing/launch.md` with the checklist |

Each skill also works alone, so the user can start in the middle. Run the
skill the request names. It needs these files, and when one is missing it
handles the gap itself (it asks, or works provisionally and says so), so do
not write a missing file just to unblock it:

- Copy: `landing/brief.md`.
- Art direction: `landing/brief.md` and `landing/copy.md`.
- Build: `landing/copy.md`, `landing/direction.md`, `landing/tokens.css`,
  and `landing/brief.md` when it exists.
- Motion: the static page, `landing/direction.md`, `landing/tokens.css` if present.
- Review: only the page. Reviewing an existing site uses only
  `landing-skills:landing-review`.
- Launch: the built page.

## When information is missing

Compare the request with the fields and ask for every missing one in a
single numbered message, in two groups:

1. What nothing can start without: the product facts, the audience, the
   conversion action and what the click does, the language and country.
2. What has a safe default, stated in the question so the user can accept
   it: the stack (detected from the project, or plain HTML), wanted moments
   (`none`), the domain (left out of the page until known), and the rest.

Never fill a gap with an invented audience, price, testimonial, metric,
customer name or date. A plausible guess ends up as a claim on the page, and
a visitor who catches one doubts every other line.

When the user cannot answer, or nobody can be asked:

- A fact about the business (price, proof, audience, what the click does,
  contact routes) is written `unknown`. `landing-skills:landing-copy` turns
  it into a visible `[PLACEHOLDER: what is needed]` and lists it.
- A working choice the page does not claim may be written as
  `assumed: <value> (reason)`: the page type and the language of
  the user's own message. Record a regional variant only when it is certain;
  for Spanish, the country and the form of address stay `unknown` until
  asked, because the copy depends on them.

Then go on, and tell the user which fields are `unknown` or `assumed`.
