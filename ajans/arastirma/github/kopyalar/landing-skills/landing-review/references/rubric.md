# Review rubric

Every check a landing review makes, grouped by area. Each row says what a
pass looks like, how you check it, and the default severity when it fails.
The severity rules are under "How to use this rubric". The check ids (C1,
A3, ...) are for your notes and the checklist; the report uses the
area names `copy`, `art-direction`, `build`, `motion` and `launch`.

## Contents

- How to use this rubric
- Copy
- Copy tells to check by eye
- Art direction
- Visual tells to check by eye
- Build
- Motion and 3D
- Launch readiness
- What is not a finding
- Limits of this rubric

## How to use this rubric

"How to check" names the pass that finds it: **source** (reading files),
**scan** (the static scanner flags it; confirm by eye), **visual** (rendered
page, all four widths), **motion** (the motion and 3D pass), **brief**
(compare with `landing/brief.md`, or treat as unverified without one).

Severities:

- `blocker`: the page cannot ship. There are exactly three kinds, and no
  other finding is a blocker:
  - **Invented proof** (C1): a testimonial, customer name, logo, rating,
    review count, user count or metric the client did not supply.
    Unverified proof on a page without a brief counts too. Stock names such
    as "Sarah Johnson", "Trusted by" with no logos, and a round-number stat
    row are the usual forms. Fake reviews and testimonials carry legal risk
    under consumer protection rules in the US and the EU: flag the risk in
    the finding, and give no legal advice or verdict on compliance.
  - **A conversion action that does not work** (L1): a form with no
    handler, a success message shown without the data going anywhere, a
    call to action that links to itself or to `#`, a checkout that does not
    exist, a call to action that disappears when WebGL or JavaScript fails.
  - **Placeholder or demo text visible to visitors** (C14): lorem ipsum,
    "Your Company Name", a default title, an unlabelled gap. A labelled
    `[PLACEHOLDER: ...]` for an asset that does not exist yet, listed as an
    open item, is not a blocker; it is the right way to mark it.
- `major`: a visitor or the client would notice, and it costs trust,
  clarity or conversions.
- `minor`: polish. Worth fixing, not worth delaying a launch for.

Rules for severity and for what goes in the table:

- Use the default in the row. Where a row gives two cases ("minor (major in
  the hero)"), pick the case that applies.
- You may raise a severity when the context makes the problem worse. The
  Finding cell states the reason. Without a stated reason the default
  applies, and a reader of the report should treat the row as the default.
- You never lower a blocker, and you never lower a row below the cases it
  lists.
- Report a minor finding only when it is specific (a place, a value, a
  quote) and you confirmed it on the page or in the source.
- A check that passes is not a finding. How passes are recorded is in
  `report-format.md`; how every row is walked is in `SKILL.md` (Process).
- A pass is valid only when its evidence is the output of the row's own
  "How to check". Where it says list, the evidence is the list; where it
  says count, the count; where it says every section, every section,
  header and footer included.

The research behind the tell lists is mostly practitioner writing and
detector guides, not controlled studies, and the scanner's rules were tuned
on a small set of pages. So judge each tell in context. A choice is a tell
when it is the unconsidered default, and fine when it was chosen for a
reason specific to this client: check `landing/direction.md` for that reason before
reporting a visual default.

Tells co-occur. Fixing one of them barely changes how a page reads; fixing
the decision that produced the cluster does. When three or more findings
come from one missing decision, add one `major` cluster row naming that
decision (where it goes in the table: `report-format.md`). Only these count
toward a cluster:

- Art direction cluster ("no art direction was chosen"): A1, A2, A3, A5,
  A6, A10, A11, A14, and the visual tells to check by eye.
- Copy cluster ("the copy was not written from the brief"): C5, C6, C7, C8,
  C11, C12, and the copy tells to check by eye.

Proof, build, motion and launch findings never count toward a cluster; each
stands on its own.

## Copy

| ID | Check | A pass looks like | How to check | Severity |
|---|---|---|---|---|
| C1 | Proof is real | Every testimonial, customer name, logo, rating, review count, user count, stat and press mention traces to the brief or to a source the page names. Missing proof is a visible `[PLACEHOLDER: ...]` listed as an open item, or the section is absent. | brief, scan (`stock-testimonial`, `vague-attribution`) | blocker |
| C2 | Claims stay inside the brief | Specs, materials, process steps, services, plan limits, guarantees, response times, privacy or mailing-frequency promises, and "free, no obligation" lines all come from the brief. Plausible is not enough: the client has to stand behind it. | brief: list every claim, spec, promise and number on the page, and mark each with where the brief supports it. | major |
| C3 | Facts agree everywhere | The same fact reads the same in the hero, the spec table, the FAQ and the form note. No claim that renders or specs are final beside placeholder images; no delivery time reworded into a different promise in a second place. | source, brief | major |
| C4 | Dates resolve | Deadlines are absolute dates ("Offer ends [date]"), never relative ("ends in two weeks") on a page that stays up. | source | major |
| C5 | The headline says something | The H1 or H1 plus subhead name what it is, for whom, and what is different, in the brief's terms. Leads with what the visitor gets, not with what is wrong with their current tool. A product name alone, a soft benefit ("without the hassle"), or a negative with no audience fails. | source: copy the H1 and subhead into your notes. Put a competitor's name in front: if it is still true, it fails. Write whether the audience is named in the H1 or subhead, not only in the `<title>`. Record both results; a pass needs both. | major |
| C6 | Sections answer the buyer's questions | The order and the sections come from what this visitor must decide: what it costs, how long it takes, what happens after signing up or at the end of a trial, what is required of them, refunds, delivery, who is behind it, how data or payment is handled. Real objections get an answer, often in an FAQ. Fails: the template order (hero, features, steps, pricing, call to action) with no section that answers an objection. | source: list the sections in order. Write down the three questions this visitor most needs answered before converting (from the brief, or from the offer) and find where each is answered. Any unanswered question fails the row. | major |
| C7 | Price is a real answer | The price, or an honest range and what drives it, sits near the call to action. A pricing section that only says "it depends" is a non-answer. Derived figures are stated plainly: an annual price says how much it saves against monthly, in money, so the visitor does not do the sum. A slogan in place of a pricing heading ("Simple pricing. No surprises.") fails C7 and counts as a fragment-pair tell. | source, visual: find every price and check whether the visitor must calculate anything. | major |
| C8 | The call to action says what happens | One label per action, the same everywhere it appears (header, hero, submit). The label is a verb plus outcome. A line beside it says what happens next: what the visitor gets, what is needed, who replies and when. | source | major |
| C9 | Confirmation copy exists and is honest | After submit the visitor reads what happens next with a timeframe ("We call within [time the client commits to] to set the date"), and no promise the stack cannot keep (no confirmation email on a setup that cannot send one). | source | major |
| C10 | Proof has its moment and its context | Real proof is placed where the doubt arises, with context ("[number] projects of [the kind the brief names] in [the area the brief names]", not a bare number), and stated once. | visual, source | minor |
| C11 | No filler or repetition | No subhead that restates the headline, no section intro that restates its title, no block repeating content shown twice above, no stock reassurance line. | source | minor (major in the hero) |
| C12 | Headings are specific | Section headings name the job ("What it costs", "What you need before we start"). Fails: headings that could head any company's page ("Features", "Why choose us", "Our services", "Everything you need"), and feature headings that only restate the feature's name instead of what it does for the visitor. | source: list every H2 and H3. For each, ask whether it could sit unchanged on a competitor's page; if yes, it fails. Report each failing heading. | minor |
| C13 | Who is behind it, and how to reach them | The business identity and a direct contact suited to the audience: address and hours for a local service, the messaging app the audience actually uses, a support email for software, the maker for a product. | source, brief | major |
| C14 | No placeholder or demo text | No lorem ipsum, "Your Company Name", bracketed "[Your ...]" prompts, default titles ("My App", "Vite + React", "Create Next App"). Stock input placeholders ("you@example.com") are a minor polish point, not this blocker. | scan (`placeholder-text`) | blocker |
| C15 | One voice, one language | Spanish pages: one form of address (tú or usted) throughout including errors, one regional vocabulary, no English leftovers in buttons, placeholders or headings, ¿ and ¡ present, sentence case, prices unambiguous for the country ($ alone reads as pesos in Chile and Mexico). | source | minor (major in CTA or H1) |
| C16 | Copy matches the approved copy | When `landing/copy.md` exists, the page says what it says. Differences are findings unless clearly better and still inside the brief. | source | minor |

## Copy tells to check by eye

The scanner catches literal stock phrases. These it cannot judge, because
they are tells only by count, by position, or in context.

Severity: `minor` each, unless the item names its own severity below.
They count toward the copy cluster (see How to use this rubric).

**Counts and clusters**

- The same call-to-action label four or more times with nothing about what
  happens next. `major`, reported under C8.
- A round-number stat row: 99.9%, 10x, 24/7, 10k+, 50+, 4.9/5, with no
  source. `blocker` when the figures were not supplied (C1).
- "Trusted by", "Loved by teams", "as seen in" with no logos or no named
  customers. `blocker` (C1).
- Fragment pairs and triads: "Build faster. Ship smarter.", "Fast. Simple.
  Secure.", "rápido, confiable y escalable". One specific claim instead.
- Em dashes in headlines and buttons. Periods, commas or colons instead.
- Title Case headings ("How It Works For You"). In Spanish it is also wrong
  by the language's rules.

**Spanish phrases that are tells only in context.** These were kept out of
the scanner because ordinary sentences use them. They are tells when they
open a section, carry no fact, or could sit on any business's page.
`minor` each where it appears in body copy; `major` in the H1, the hero
lead or a call to action.

| Phrase | Tell when | Fine when |
|---|---|---|
| Cuando se trata de... | It opens a section as a run-up to a generic claim | It introduces a real condition: "Cuando se trata de menores, la ley exige autorización." |
| Es importante señalar que, Vale la pena señalar que | It pads a sentence whose fact could stand alone | Rarely; cut it and keep the fact |
| sin costuras, de forma fluida | Calque of "seamless(ly)" describing a process | Literal: clothing, tubes |
| de vanguardia, innovador, transformador | An adjective standing where the technique or figure should be | Part of a proper name |
| En la era digital, En el mundo actual | Opening line of the hero or "about" | Almost never on a landing |
| Potencia tu negocio / tu marca | A verb with no measurable object | "Potencia" as a real noun (engine power, 1.2 kW) |
| No es X, es Y; ¿La trampa?, ¿El detalle clave? | Manufactured contrast or hook question | A real question in the FAQ, in the reader's words |
| Soluciones a tu medida; Te acompañamos en cada paso | Stock reassurance, fails the swap test | Followed at once by the concrete steps and who does them |

For English the same logic applies to "streamline", "leverage", "robust",
"powerful", "journey", "landscape": a tell when the word replaces a fact.

## Art direction

| ID | Check | A pass looks like | How to check | Severity |
|---|---|---|---|---|
| A1 | The palette comes from the subject | Colours traceable to the product, material, place or brand, with the reason in `direction.md` or evident on the page. Not a bundle from a fashionable list: cream with terracotta and a display serif; Tailwind indigo or violet; a purple-to-blue gradient; an off-white or near-black field with one muted "tasteful" accent that nothing on the page explains. Test: could a different client in a different trade use the same palette unchanged? Cream or dark alone is never the finding; the unconsidered choice is. | visual, source, scan (`palette-cream-terracotta`, `indigo-default-accent`, `purple-blue-gradient`) | major |
| A2 | A typeface was chosen | At least one face is loaded and chosen for the client, with a reason. The system stack or Georgia and Times as the only faces means no decision was made (and the page looks different per operating system). A face named in CSS but never loaded is a bug. Inter, Geist, Space Grotesk, Instrument Serif and Fraunces need a recorded reason and a distinct pairing; two of the last four together is the "tasteful default" set. | source, scan (`overused-font`) | major |
| A3 | Real images, or honest placeholders | Product photos, screenshots or drawings that show the actual thing. When an asset does not exist yet: a labelled placeholder at the right aspect ratio, listed as an open item. Fails: a placeholder for an asset the brief says was supplied (use the real file); no image slot at all on a page about something visual; HTML and CSS boxes dressed as product UI, filled with invented data, standing in for screenshots; stock illustrations; AI-generated people. | visual, source | major |
| A4 | The product's defining feature has weight | What makes the product different is the biggest thing on the page where it is discussed (the finish, the mechanism, the result the service delivers). Not a tiny swatch or a line in a list. | visual | major |
| A5 | The layout has rhythm | The page as a whole: sections differ in density, width or scale where the content differs; one full-bleed or off-grid moment; headings sized by importance. Fails: every section the same padding in the same container, alternating flat bands, one flat surface top to bottom, every H2 the same size. A5 is about the whole page; anything that goes wrong at one width belongs to A6. | visual | major |
| A6 | Composition holds at every width | Owns every per-width problem, including headline wraps. No half-empty rows at 1280 and 1440 (a narrow card left-aligned in a wide container, one column far taller than its neighbour); no headline wrapping to four lines or leaving a one-word last line; no eyebrow, label, button or nav item wrapping awkwardly at 360; consistent alignment (a centred block on an otherwise left-aligned page needs a reason). | visual: for each of the four widths, write what you checked against this row; every width needs a line. | minor (major in the hero) |
| A7 | Text is readable over images and scenes | Headline and copy never sit on top of busy imagery or a 3D model where they lose contrast. | visual | major |
| A8 | Mobile keeps the content | At 360 the product is visible before the form, overlays do not cover the scene they explain, and legends or labels are not hidden. | visual | major |
| A9 | Stand-ins read as the product | A 3D or drawn stand-in has a plausible layout and colouring (parts in the places and proportions the real product has), not a random pattern. | visual | major |
| A10 | Surfaces have hierarchy | A radius scale and shadows only on elevated items; the treatment says which surface matters more. Fails: one radius, border and shadow on every card, image frame and panel; a glass panel without content behind it; blurred colour orbs; gradient text. | visual, source: list each card-like surface and its radius, border and shadow (read the CSS or computed styles). Count the distinct treatments. One treatment on every surface fails. | minor |
| A11 | Ornament earns its place | No `01 02 03` counters or outline numerals on content that is already ordered or is not a sequence; no pill badge above the headline; no emoji or sparkles as icons; no device or window frame drawn around something that is not a real screenshot. | visual, scan (`pill-badge`, `emoji-bullet`, `sparkles-icon`) | minor |
| A12 | The mark is deliberate | A supplied logo, or a wordmark set with intent in the chosen face. A plain bold text logo with no logo listed as needed is a gap. | visual, brief | minor |
| A13 | Dark mode is designed | When the page has a dark theme, tokens are tuned for it, contrast holds, images and borders still work. Not a straight inversion. | visual (dark captures) | minor (major if text fails contrast) |
| A14 | Section layouts come from their content | Each section's layout fits what it shows: a sequence reads as a sequence, a comparison as a comparison. Fails: the zig-zag feature template (text and image alternating sides on the same grid), three or six equal cards, or the other visual tells below, with no reason in `direction.md`. | visual: name the layout pattern of every section in your notes. Two or more sections with the same template, or any pattern from the list below, fails. | minor |

## Visual tells to check by eye

Count and cluster tells the scanner cannot see. Severity: `minor` each,
reported under A14 unless another row owns it. They count toward the art
direction cluster.

- Three equal cards with an icon, a heading and two lines; or six identical
  cards. Content shaped to a grid instead of a grid shaped to content.
- The zig-zag feature template: text left and image right, then mirrored,
  same grid each time, uneven text columns beside short images.
- Centred headline, centred paragraph, two centred buttons.
- The same padding on every section in one max-width container.
- The default section order: hero, logo bar, three features, testimonials,
  pricing, FAQ, CTA, four-column footer, regardless of the offer.
- Three pricing tiers with a ringed "most popular" middle when the business
  has one plan or recommends none.
- Oversized italic serif headline as the hero's only idea.
- Pill-shaped buttons and tags everywhere.

## Build

| ID | Check | A pass looks like | How to check | Severity |
|---|---|---|---|---|
| B1 | The head is complete | Specific `<title>`, meta description, `lang` matching the copy, favicon, `og:title`, `og:description`, `og:image`, Twitter card, canonical, `theme-color`. With `Domain: unknown` in `landing/brief.md`, `landing-build` leaves out canonical, `og:url` and `og:image` on purpose: list them under Open items, not as findings. | source | minor each (major: no title or wrong `lang`) |
| B2 | Works without JavaScript | The built HTML contains the headline, offer, price and a working way to convert. Not an empty `<div id="root">` with no `<noscript>`. A form that only works through a JavaScript handler with no `action` fails here too. | source, visual (JS off) | major (blocker if the conversion cannot happen at all, see L1) |
| B3 | Links go somewhere | No `href="#"`; the logo links to `/`; in-page links point at existing ids. | scan (`dead-anchor`), source | minor (blocker on the main CTA, see L1) |
| B4 | CSS does what it says | Computed spacing matches the intent everywhere: no section padding cancelled by a more specific rule, no headings flush under borders, no borders wider than the content column, no text touching its rule. | visual, source: list every section in page order, header and footer included, with its computed top and bottom padding read in the browser. Any 0 where the CSS asks for more, or any heading or text touching the block or rule above, fails. The list is the evidence. | major |
| B5 | No overflow, no overlap, no shift | No horizontal scroll at 360, 768, 1280, 1440; a fixed header does not cover the headline; nothing clipped; nothing jumps during load. Report a layout shift only with its visible cause (an image without dimensions, a late font swap, content injected above the fold), since lab numbers are unreliable. | visual (measurement script) | major |
| B6 | Navigation survives mobile | Section links removed at 360 are replaced by a menu; touch targets are at least 44 by 44 px. | visual | major (minor for target size alone) |
| B7 | Keyboard and screen reader basics | Skip link, `<nav>` landmark when there are links, visible focus on every control, logical focus order, one H1, headings that do not change with scroll, a keyboard path for any draggable or rotatable canvas, a text equivalent for content only shown in a canvas. | visual: Tab through the whole page and write the number of tab stops and any stop without visible focus; the count comes from the tab-through. Source: heading outline. | major |
| B8 | Forms are usable | Visible labels (a placeholder is not a label; a visually hidden label leaves a sighted visitor with nothing once they type), `name`, `type`, `autocomplete` and `inputmode` set, validation messages in the page's language, a success and an error state, focus moved to the result, the button disabled while submitting. Lists of options include every valid answer or accept free text. | visual, source: type into every field. If its purpose is no longer visible, the label fails. Submit empty and invalid, and read the messages. | major (minor for single attributes) |
| B9 | Text is legible | Body text at least 4.5:1 and large text 3:1 against its background, including muted text, captions, placeholder text in inputs and text on tinted bands. Text that carries information (price, terms, ship time, captions, tags, notes, footer) is not set much smaller than body text. | visual, source: write the body size, then list every font size below it with the elements that use it and what they carry. Measure contrast for each text colour on each background it sits on, from the two colours. A pass needs both lists; contrast alone does not pass this row. | major |
| B10 | Placeholders are honest to assistive tech | An empty image slot is not announced as a photo (`role="img"` with a description of a picture that does not exist); a fake UI mock is not read as live content. | source | minor |
| B11 | Essential assets are local | Fonts, models, environment maps and images the page needs are served by the site, not fetched from a third-party host at runtime where a block or outage breaks the page. | source, motion (network list) | major |
| B12 | No builder marks | No "Edit with Lovable", "Made with Bolt", builder domains or badges. | scan (`builder-fingerprint`) | major |
| B13 | Search basics for the page type | `robots` meta where needed; for a local service, structured data for the business. | source | minor |
| B14 | Images have the right alt text | Every `<img>` has an `alt`: a short description where the image carries content, `alt=""` where it is decoration. Placeholder images say what will go there. No missing `alt`, no file names as alt text. | source: list every `<img>` (and CSS or `role="img"` image) with its alt; every one must be accounted for. | major for content images, minor for decoration |
| B15 | The page builds and serves | The documented build command succeeds and the output serves over HTTP without errors in the console that break content. | source, visual: run the build; note errors. | major |

## Motion and 3D

Skip this section and say so when the page has no motion or 3D. Even then,
check M1's smooth-scroll point and M10.

| ID | Check | A pass looks like | How to check | Severity |
|---|---|---|---|---|
| M1 | Reduced motion is a designed variant | Under `prefers-reduced-motion: reduce`: parallax, scrub, autoplay loops, smooth scrolling and large travel removed; short fades and feedback kept; nothing left invisible; the preference is followed live (a `change` listener), not read once at load. A global `animation-duration: 0.01ms` kill switch, or `scroll-behavior: smooth` with no reduced-motion override, fails. | motion (emulate), source, scan (`reduced-motion-kill-switch`) | major (minor for smooth scroll alone) |
| M2 | Every animation has a role | Each motion is an entrance, feedback, or scroll-linked explanation, and varies by role. One signature moment. Fails: the same fade-up on every section, hover lift or scale on every card, stagger on every list, bounce easing, count-up on a stat, drop-in effect components with stock colours. | motion, scan (`fade-up-everywhere`, `hover-scale`, `stock-effect-component`) | minor (major when it is the whole page) |
| M3 | Scrolling is smooth | No frames over 33 ms in the scroll test; scroll handlers do no layout reads per event or are batched in `requestAnimationFrame`; scroll position does not go through framework state that re-renders the whole component tree on every scroll event or frame (use refs, CSS variables or a scroll-driven animation); only `transform` and `opacity` animate; `will-change` is not blanket. | motion (frame test), source | major |
| M4 | The page survives without WebGL | With WebGL disabled the headline, offer and call to action stay, with a poster where the scene was. An error boundary or support check catches the failure. | motion (WebGL off) | major (blocker if the CTA disappears, see L1) |
| M5 | Hero text does not wait for the scene | Headline and call to action paint before the 3D code and assets load; the canvas has a poster or reserved space so nothing shifts. | motion (blocked requests), source | major |
| M6 | The bundle is proportionate | 3D code split from the hero's code and loaded after first paint; models compressed; the build's size warning not silenced by raising its limit; no single chunk carrying the whole library set before any text paints. Report the largest chunk's size raw and gzipped. | motion (bundle sizes), source | major |
| M7 | Hidden work is paused | Canvases, videos and loops stop off-screen and in hidden tabs; one WebGL context where one would do; geometry reused or instanced instead of duplicated per scene. | source, visual (canvas count) | minor (major on phones if the scroll test stutters) |
| M8 | Text stays still while read | No parallax or drift on paragraphs the visitor is reading. | motion | major |
| M9 | Scroll length fits the content | Pinned or scroll-driven sections take about as much scrolling as their content needs; the key reveal does not arrive last after several screens; keyboard and native scrolling still work; a pinned panel's content fits a 360x640 and a 1280x600 viewport with no row cut off. | motion: also resize the browser to both sizes | minor (major when content is cut off) |
| M10 | State changes give feedback | Every link, button and field responds on hover, focus and press, with a short transition rather than an instant jump; submitting shows progress and then the result. A page with no other motion is fine. | visual: Tab through every interactive element and hover each with the pointer; note in the checklist what changes for each. Submit the form and note what happens between click and result. | minor |
| M11 | Motion the direction asked for exists | When `direction.md` names a motion moment, or the copy promises a sequence (first action to result), the page shows it. A product mock or demo that never changes state while the copy describes the change fails. | brief, visual: name the sequence the copy promises, then find where the page shows it moving. | minor |
| M12 | Libraries are sane | One animation engine per job; no editor or studio code shipped in production. | source | minor |

## Launch readiness

| ID | Check | A pass looks like | How to check | Severity |
|---|---|---|---|---|
| L1 | The conversion action works end to end | The form posts to a real handler that stores or forwards the data; success shows only after the handler confirms; failures show an error. Each call to action reaches a real signup, checkout or booking. Fails: `action="#"`, `onsubmit="return false"`, `preventDefault` followed by a thank-you with nothing sent, a CTA that only scrolls within the page when it should start a purchase or signup, a checkout that does not exist, a CTA that vanishes when JavaScript or WebGL fails. | source, visual (submit it), scan (`dead-form`, `dead-anchor`) | blocker |
| L2 | Everything the brief asked for exists | Each deliverable the brief names is present and wired (a replaceable model path, a booking field, a second language). | brief | major |
| L3 | Conversions can be measured | An analytics snippet and a conversion event on submit or on the main call to action. Page-view analytics alone does not count conversions. | source | major |
| L4 | Personal data is handled openly | A privacy notice or link beside any form collecting personal data, and a privacy link in the footer. Terms and refund conditions on a page that takes payment. Do not claim the page is legally compliant; flag the gap. | source | major |
| L5 | Forms resist spam | A honeypot hidden from assistive technology as well as visually, or a challenge such as Turnstile verified on the server. | source | minor (major once the form is live) |
| L6 | Contact routes fit the audience | Phone, email or a messaging app where visitors expect them, beside or below the form. | source, brief | major for local services, minor otherwise |
| L7 | The site has its edges | A custom 404 page, `robots.txt`, a sitemap (the sitemap only once the domain is known; otherwise an open item). | source and visual: on the served build, request `/a-path-that-does-not-exist`, `/robots.txt` and `/sitemap.xml`, and note each status and what is shown. Check the build output for a 404 page. A server's default not-found page fails. | minor |
| L8 | Open items are listed | Every missing asset (photos, screenshots, logo, favicon art) and every `[PLACEHOLDER: ...]` is listed as an open item with what is needed. The page does not launch until they are supplied. A missing asset that is unlabelled or unlisted is a finding. An asset the brief says was supplied but the page still shows as a placeholder is a finding too (A3). | source, visual, brief | major when unlisted or supplied |

Review runs before `landing-skills:landing-launch`, so rows L3, L5 and L7 will
often fail on a page launch has not touched yet. That is not circular: they are
major or minor, which never fail the verdict, and they go to the next round or
to launch. L1 is the exception that does fail it, when the form posts to the
`/api/lead` placeholder: sending that blocker to launch is how the Worker
gets built.

## What is not a finding

Report these as fine, so the reader knows you checked:

- A labelled placeholder with the right aspect ratio, listed as needed, in
  place of a photograph or screenshot the brief describes but did not
  supply. That is the correct behaviour.
- Proof taken from the brief, used once, in context.
- A deliberate default with a recorded reason: Inter paired with a mono face
  for a developer tool, cream for a paper goods shop that photographs on
  paper.
- A page with no motion, when state changes still give feedback.
- Blank bands in a full-page capture of a scroll-driven page that render
  when you scroll to them.
- Uppercase tracked labels and `rounded-full` on a single primary button;
  ordinary on well-made pages.

## Limits of this rubric

- The scanner's rules and these tell lists come from practitioner writing
  and a small set of test pages. They are prompts. A finding needs your eyes.
- Frame-rate numbers from your machine overstate a phone's performance.
- Contrast needs a measured ratio, not an impression; use the browser
  tool's contrast reading or compute it from the two colours.
- Legal points are flagged as risks only (see `blocker` above and L4).
