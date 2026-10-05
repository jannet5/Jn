---
name: landing-copy
description: Use when writing or editing the text of a landing page, including headlines, value propositions, section order, calls to action, testimonials, pricing, or FAQ, or when page copy sounds generic, inflated, or machine-written, in English or Spanish.
---

# Landing copy

Write every word of the page before anyone designs or codes it, and save it
as `landing/copy.md` in the user's project. Copy comes first because the
sections, their order and their length decide the layout; a designer who
starts without copy fills the page with a template and the copy gets bent to
fit it.

The copy you write has two jobs: tell a stranger what this is, for whom, and
what to do next; and say only true things. Unguided agents fail mostly at the
second: given a short brief, they fill the gaps with plausible details the
business never stated, because a plausible detail is easy to write. This skill makes the
brief the only source of facts.

## Inputs

Read `landing/brief.md`, one line per field, in the shape that
`landing-skills:landing-page` defines. The fields this skill uses:

- Type: SaaS, product, service or studio, waitlist, event, or a mix.
- Language: the language with its country variant (`es-CL`, `en-GB`), and
  for Spanish the form of address (tú, usted, vos).
- Product: what it does, with every fact given.
- Audience: who it is for, and what they use or do today.
- Conversion action: the single action and what the click does: the form's
  fields and whether an endpoint exists, or the outside page it opens.
- Pricing.
- Proof available.
- Assets available, because the copy names what each visual shows.
- Brand constraints, including any voice material.
- Contact routes, which feed the footer.
- Wanted moments (motion, 3D, scroll effects), which are not copy; see step
  1.

The brief may also hold Stack and Domain; the copy does not use them.

If `landing-skills:landing-page` has already written the brief, read it and
do not rewrite it. Read the brief before asking the user anything, and ask
only for what it does not hold.

If `landing/brief.md` does not exist, ask the user for the fields above in
one numbered list. If you cannot ask, write `landing/brief.md` yourself in
the entry skill's shape: the same field names, one line each, `none` when
the user said there is none, `unknown` when nobody answered, and
`assumed: value (reason)` for a working choice the page does not claim (the
page type, the language of the user's own message). Never fill a gap with a
guess: an `unknown` price, proof item, date or click outcome becomes a
placeholder in the copy and an open question for the user.

If the Language field gives no country variant, or no form of address for
Spanish, ask, because vocabulary, form of address, number format and
currency all change with it.

## Process

### 1. List what the brief states

Before writing copy, make a working list of every fact the brief states:
features, numbers, prices, dates, places, proof, terms. This list is the only
source for claims on the page.

Wanted moments (motion, 3D, scroll effects) are not facts for this list: do
not write them as product facts or promises. A section may carry a `Note:`
saying where a wanted moment sits, for the direction, outside the page text.

### 2. Decide what to do with everything the brief does not state

A claim is any sentence that tells the visitor something about the business,
not only a number or a feature. These are claims too, and each must trace to
the list:

- A reason: why there is no fixed price, why the process works this way.
- A benefit beyond what the facts support: "so your plants never dry out
  again" adds a promise the brief may not make.
- A stronger verb or a wider scope than the brief's: if the brief says the
  business "checks" the work, the copy may not say it "takes charge of" or
  "guarantees" it. Keep the brief's own verbs and scope.
- A guess about this reader's own setup: "you already keep your recipes in
  one app" is not a fact from the brief. What the brief says about the
  audience as a group ("they plan meals on paper") may be restated.
- What follows a step: the price after a trial, "your order is confirmed",
  "you will get a code". Write it only if the brief says so.
- A promise: what the visitor's data is used for, who replies and when, what
  is sent after sign-up, a guarantee.
- A product or service step the brief does not describe: "we pack it the
  same day", "a technician calls the day before".
- Context added to a figure: where, what kind of work, since when.

Where the line falls, with an invented brief (a watering timer that waters
pots on a schedule; the audience waters by hand every evening):

- Allowed: "Set it once and stop watering by hand every evening." A
  consequence that follows directly from the brief's facts, including what
  it says about the audience's situation today, is a restatement.
- Not allowed: "Tap the plant in the app and pick a schedule." A step,
  mechanism, choice or outcome the brief does not state (what the user taps,
  what a customer chooses, what happens next) is a process claim; use a
  placeholder.

Quick test for a clause that starts "so you" or "so that": can you point to
the sentence in the brief that makes it true? If yes, keep it. If not, cut
it. The consequence allowance never covers proof, figures, testimonials or
counts; those come only from the brief.

When a sentence needs a claim that is not on the list, you have three
options: rephrase so it does not need it, ask the user, or replace the claim
with a placeholder. Inventing it is not an option, even when it is very
likely true. A claim that needs the user's confirmation comes out of the page
text and becomes a placeholder. A `Note:` beside an unconfirmed sentence is
not a placeholder: the note gets dropped in the build and the sentence ships.

Derived facts are fine when you show the derivation: a yearly price and a
monthly price let you state the saving, once you have checked the sum.

Relative time is not a fact a page can keep, because it goes wrong for every
later visitor: "ends in two weeks", "opened last spring", "next month", a
count of years turned into "since [year]" or the reverse. Write an absolute
date from the brief, or a placeholder for the date. Quote a count the brief
gives (an invented example: "11 years in business") as the brief gives it.

### 3. Write in the brief's language

Write all copy in the language of the brief: headings, buttons, form labels,
helper text, error messages, the confirmation message, the page title and
the meta description. Write it in that language from the start, never as a
translation of an English draft, because translation carries over English
rhythm and English stock phrases.

For Spanish, decide before the first line:

- The country, and its vocabulary and number format.
- The form of address (tú, usted, vos) for this country and audience, kept
  on every line.

The guidance for each country is in `references/headlines.md`, section
"Writing in Spanish". Notes in `landing/copy.md` meant for the designer or
developer may be in English; everything a visitor reads is in the brief's
language.

### 4. List the visitor's questions

Write down what this audience needs to know to take the conversion action:
what it is, whether it fits their case, what it costs, what the risk is, what
effort it takes, and what happens after they click. Add the questions this
specific brief raises (a course: is there a recording, can I join
late; a clinic: does insurance cover it, how long is the wait).
For a spec-led product whose buyers check every detail before buying, ask
what such a buyer needs to see before buying and placeholder what the brief
does not give. These questions decide the sections and the FAQ.

### 5. Choose the sections

Take the sequence for the page type from `references/structure.md` and
adjust it to the questions from step 4. Keep a section only if it answers one
of them and its content is real; drop a section whose content would have to
be invented. Name each section after the job it does, not with a label that
fits any product.

### 6. Write the headline and subhead

Write the one-sentence value proposition first, from the facts list, then cut
the headline and subhead from it. The headline says what it is and for whom
or for what outcome, so that a stranger can repeat it. The subhead says how,
or gives the proof. Method, real examples and worked rewrites in English and
Spanish are in `references/headlines.md`.

Lead with the most specific thing the brief holds: a concrete action with its
number ("in one tap"), a named result the reader can see, a place, a
price. A category description ("X for Y") is the fallback for when the brief
holds nothing more specific, not the first choice. Then run the swap test
from `references/tells.md` on the headline and every section heading,
including what to do when the brief holds nothing only this business has:
keep the plain line, invent nothing, and add an open question naming what
would make it specific. That open question is required whenever you keep a
plain line.

### 7. Write the sections

For each section, lead with its most important sentence. Prefer the named
thing over the category, the number from the brief over an adjective, the
reader's own words over the business's jargon. Answer each objection next to
the action it blocks. Say each fact once, in the place it does most work; a
proof figure appears once, in the brief's own words. State what each visual
must show, using the assets the brief lists, or a placeholder for a missing
asset with its shape and content.

When the visitor needs a section and the brief gives little for it, do not
restate the brief in bare sentences and stop. Write the first concrete step
the brief does support (the first lesson, the trial, the sign-up), say what
happens in that step as far as the brief says, and placeholder the rest.
A row of the form "X: we do it" is a stub too: write one natural sentence
per listed service or step from what the brief gives, and a placeholder for
the detail it does not give. See "Thin sections" in
`references/structure.md`.

### 8. Write the calls to action and the form

One primary action, with one label at the main actions. The header may use
a shorter form of the same label. One secondary action at most, lower in
commitment.

Under the primary button, in the hero and wherever it repeats, write one
line. It says first what the click does: creates an account, opens a
booking calendar, sends a message to the business. Cost, requirements or
reply time may follow as an addition, only from the brief. If the brief does
not say what the click does, that line is a placeholder. A reassurance such
as a price note does not stand in for it.

Then write the form copy in full: labels, format hints, the purpose line,
the submit label, the confirmation message and the error messages.

The purpose line says what the data is for, who replies and when. Write it
only from what the brief states. When the brief is silent, the purpose line
is a placeholder on its own, with no sentence in front of it. A sentence
like "we only use your details to contact you" is a promise.

A format hint is a format example only: a phone pattern, a postcode pattern.
What the field is for goes in the label. A hint is never a person's name. It
is never a place that implies a service area the brief does not give. Details in `references/headlines.md`, sections "Call-to-action
hierarchy and labels" and "Form copy".

### 9. Write proof and pricing

Use only the proof the brief gives, where the doubt arises, once, in the
brief's own terms. Context for a figure (what kind, where, since when) comes
only from the brief; otherwise ask or add a placeholder for it. Where there is none, use the honest substitutes and
placeholders. Show prices in the structure the business really offers. Read
`references/proof-and-pricing.md` for each kind of proof, the no-proof
protocol and pricing.

### 10. Write the footer

Include every contact route the brief's Contact routes field gives (email,
phone, address, messaging channel, hours). If it gives none, add a placeholder for at least
one route besides the form.

### 11. Remove the tells

Read the draft against `references/tells.md`: inflated phrases, empty
adjectives, triads, negation pivots, stock reassurance, generic headings and
proof tells, in English and Spanish. Rewrite each with a fact; a synonym is
the next stock phrase. Then run the self-check below.

### 12. Hand over for approval

Give the user `landing/copy.md` and the list of placeholders, and ask them to
approve the copy or answer the open questions before design starts.
`landing-skills:landing-art-direction` and `landing-skills:landing-build`
read this file, so changes are cheapest now. When nobody can answer (an
unattended run), hand over the file as it is, with the `Placeholders` list
and the open questions at the end, and say in your reply that the copy is
unapproved.

## The proof rule

Testimonials, metrics, ratings, user counts, press mentions and customer
logos are never invented. Use exactly the proof in the brief. Anything
missing becomes a visible placeholder of the form
`[PLACEHOLDER: what is needed]` and is listed in the `Placeholders` section,
so the user sees it and supplies it or deletes it before launch.

The reason is the user's risk. Fake reviews and testimonials, AI-generated
ones included, are banned in the US with civil penalties, and reported to be
always unfair in the EU; details and their limits are in
`references/proof-and-pricing.md`. And a reader who catches one invented
claim doubts every other line on the page.

Rules that follow from it:

- A placeholder never looks real: no sample names, companies, quotes or
  numbers inside it.
- The same rule covers claims that are not proof: specifications,
  inclusions, reasons, benefits, promises, response times, guarantees,
  dates (step 2). If the brief does not state it, it is a placeholder or a
  question, and not a sentence with a note beside it.
- Proof the brief does give is used well: near the decision, stated once, in
  the brief's terms, with context only from the brief.

Placeholder economy. A placeholder is for what only the client can supply,
not a way to avoid writing. Group related gaps into one placeholder (one
for "shipping cost and delivery time", not two). Never repeat the same gap,
or the same fact, in two sections. Before finishing, check that everything
the brief does state is used in the page text; a fact left in the brief
while a placeholder sits on the page is a missed sentence. A section that
would hold only a placeholder is dropped, and its gap is kept once in the
`Placeholders` list.

## Output format

Write `landing/copy.md`: a short preamble, then one H2 per page section in
page order, then an H2 `Placeholders` as the last section. Inside each
section, label each piece of text by its role so the builder knows which
element it becomes. Notes for design start with `Note:`.

The example's brief is invented: a 26 cm cast-iron pan, pre-seasoned,
2.4 kg, lifetime warranty, 75 USD, bought through the site's checkout; one
photo of the pan; no proof.

```markdown
# Copy: Ferrum

Language: en-US. Address: you.
Primary action: Buy the pan
Page title: Ferrum: a 26 cm cast-iron pan, seasoned before it ships
Meta description: A 26 cm cast-iron pan, 2.4 kg, seasoned before it ships, with a lifetime warranty. 75 USD.

## Hero
H1: A 26 cm cast-iron pan, seasoned before it ships.
Subhead: 2.4 kg, 75 USD, and a warranty for life.
Primary action: Buy the pan
Under the action: Opens checkout. [PLACEHOLDER: shipping cost and delivery time]
Visual: photo of the pan (asset from the brief).

## Ready for the first use
H2: Seasoned before it ships
Body: The pan arrives seasoned, so you can cook in it the day it arrives.
Note: one short section; the photo carries it.

## Warranty
H2: Covered for life
Body: The warranty lasts for the life of the pan. [PLACEHOLDER: what the warranty covers and how to claim it]

## Placeholders
1. Hero, under the action: shipping cost and delivery time. Blocks launch.
2. Warranty: what it covers and how to claim. Blocks launch.
3. Proof: no reviews yet; reviews section dropped. Add real reviews with count and source once collected.
```

The H2 is the section's name in page order; the `H2:` line is the heading the
visitor reads, which may differ. In the `Placeholders` section, list every
placeholder in the copy with: the section it is in, what is needed, and
whether the page can launch without it. Then list open questions for the
user that are not placeholders (for example, a section you dropped for lack
of content that could come back). Write the section titled `Placeholders`
even when there are none, and say so.

Keep the file free of quoted tells: it is scanned by the review detector,
and a stock phrase in a note fails the scan the same way as one in the copy.

## Self-check

Go through each line before handing the file over.

- [ ] Every claim traces to the brief's facts list, or is a derivation you
      showed, or is a placeholder. Reasons, benefits, promises, steps and
      context on figures count as claims. Nothing is "probably true", and no
      unconfirmed sentence survives with only a note beside it.
- [ ] No testimonial, name, logo, rating, count or metric that the brief does
      not give.
- [ ] Placeholders are few and grouped: each one asks for something only
      the client can supply, no gap appears twice, every fact the brief
      states is used in the text, and no FAQ question exists only to hold a
      placeholder.
- [ ] Every placeholder has the form `[PLACEHOLDER: what is needed]` and
      appears in the `Placeholders` section.
- [ ] Swap test (`references/tells.md`) on the headline, subhead, section
      headings and reassurance lines. Each failing line is rewritten with a
      fact from the brief, or kept plain with an open question naming what
      would make it specific. A plain line with no such open question fails.
- [ ] The headline leads with the most specific fact the brief holds.
- [ ] No section is a bare restatement of the brief; a thin section gives
      the first concrete step and placeholders the rest.
- [ ] Each proof figure appears once, in the brief's terms.
- [ ] A stranger reading only the headline can say what the product is.
- [ ] The subhead adds how, for whom or proof; it does not restate the
      headline.
- [ ] No section heading fits any product; each names a job or a point.
- [ ] Each section answers a question from step 4; no section exists only
      because pages usually have it.
- [ ] The brief's own verbs and scope are kept; no statement about the
      reader's setup and no "what follows" (price after a trial, a
      confirmation) that the brief does not give.
- [ ] One primary action with one label at the main actions (a shorter
      form of it in the header is fine); under it, a line saying what the
      click does, or a placeholder for it.
- [ ] No "X: we do it" rows; each service or step is a natural sentence.
- [ ] The form copy is complete: labels, purpose line, submit label,
      confirmation, errors. The purpose line, the reply channel and the
      response time are from the brief, or a placeholder with no sentence in
      front of it.
- [ ] Prices match the brief exactly, any derived saving is checked, and no
      inclusion or limit is invented.
- [ ] Dates are absolute, or placeholders.
- [ ] The footer has a contact route besides the form, or a placeholder.
- [ ] Everything the visitor reads is in the brief's language. For Spanish,
      the checklist in `references/headlines.md`, "Writing in Spanish", is
      met.
- [ ] One narrator on every line, body text included. Scan each sentence
      that opens with the business's name or a noun for it. On a "nosotros"
      page (invented example), "La escuela organiza grupos de seis" becomes
      "Organizamos grupos de seis".
- [ ] Required: read the whole file against `references/tells.md` by eye,
      in either language, and rewrite every match.
- [ ] Optional: if the landing-skills plugin is installed, also run the
      detector of `landing-skills:landing-review` on `landing/copy.md`. The
      command is at the end of `references/tells.md`.
- [ ] Sentences are short and use the reader's words; each section leads with
      its most important sentence.
