# Typography

Read this at step 4 of the process, after the direction is written. Not
before: see "How to choose".

## Contents

- How to choose (requirements, number of faces, searching a catalogue,
  shortlist, convergence check)
- First-reach faces
- Licences and sources
- Faces to shortlist from
- Single-family options
- Sample pairings
- Overused faces and what to use instead
- Scale
- Measure, leading and tracking
- Font loading notes
- Recording the decision

## How to choose

Open this file only at step 4, after the `Subject` and `Direction` sections
of `direction.md` are written. The tables below are a sample. Read before the
direction exists, they become the answer: every agent with the same tone
word picks the same row, and two unrelated clients end up with the same
face.

### 1. Write what the face must do

From the anchors and the direction, write two to four requirements, each a
thing you could check on a specimen. Write them in `direction.md` before you
look at any table. Examples of the kind of requirement meant, for other
clients than yours:

- soft, rounded terminals that echo worn river stones;
- a heavy slab that holds up at large sizes on a dark ground, like a cast
  iron stove door;
- high stroke contrast at display size, like light cutting through a
  greenhouse frame;
- open, wide letters for long reading by older visitors;
- a single-storey a and round forms, for a friendly voice;
- old-style figures that sit in running text, for a page full of dates;
- one family that covers headline and body because the copy has one
  register.

A tone word ("warm", "technical") is not a requirement. It names a mood;
many faces fit a mood, and the first one in a table always wins.

Watch for one easy drift: deriving every requirement from the trade's
paperwork (the lettering on a form, a drawing, a label, a receipt). It is
the easiest artefact to name, so it pulls every client towards narrow,
even-stroke capitals. Draw requirements from the material, the place and
the moment of use as well; at most one may come from documents.

### 2. Decide how many faces

How many faces is a decision, with a reason written in `direction.md`:

- One family when the copy has one register and the family has the range to
  separate headline from body: several weights, plus a width axis or an
  optical size axis. Several verified families qualify (see "Single-family
  options" below); the requirement decides which, not the fact that a family
  is variable.
- Two faces when the display face carries an anchor that would tire or blur
  at body size, or the page has long passages that need a calmer text face.
  The two should differ in classification: a serif with a sans, or a
  grotesque with a humanist sans. Two faces that look alike read as a
  mistake.
- A mono face only when the page shows text whose meaning depends on fixed
  character widths: code, commands, terminal output, aligned technical
  values. A mono companion was on 14 of 30 well-made landing pages studied,
  nearly all of them developer tools or studios, where code is the content.
  Counts, prices, dates and times do not need a mono face: use the text
  face's tabular figures (`font-variant-numeric: tabular-nums`).

Display plus text plus mono is not the default shape. Three weights per
family is enough. More families cost load time and blur the voice.

### 3. Search, then shortlist at least three candidates per role

Candidates come from a search, not from memory. Faces you can name without
looking are the ones every other reader of this skill also names (see
"First-reach faces" below), so a shortlist built from recall converges
however carefully you justify the pick.

For each role (display, text, or the single family), find at least three
candidates that meet the requirements, of which at least two you did not
have in mind before you searched. In `direction.md`, mark each candidate
"recalled" or "found by search".

#### Searching a catalogue

The search path itself can converge: two readers who fetch the same
catalogue, sort it the same way and read from the same place pick the same
face for unrelated clients. Make the path depend on your client.

1. Filter first on the hard requirements you derived from your anchors:
   classification (serif, slab, sans, display, handwriting, mono), axes
   (`wdth` for width, `opsz` for optical size, `wght` and its range), the
   weights and styles you need, the languages the page needs, and any
   feature a requirement names (tabular or old-style figures). Combine them;
   a filter on one trait alone lets most of the catalogue through.
2. Count the families that pass. Record the filter and the count in
   `direction.md`.
3. If only a handful pass, look at all of them. If more pass, look at a
   spread across the whole result: families from different parts of the
   list, not the first few of any sort order, popularity included. Then
   shortlist.

Be suspicious of a family that fits many briefs loosely. A face that would
suit almost any client is the face every reader reaches; the one that meets
your hard requirements closely is usually less obvious.

Where to search, for the sources in the licence table:

- Google Fonts. Use the catalogue's filters for category and properties when
  you can browse it. When you cannot, fetch the family metadata JSON the
  research used, at `fonts.google.com/metadata/fonts`: it lists family
  names, category, axes, popularity and whether each family is open source.
  Filter it yourself.
- Fontshare. Its API returns every family with its licence type and view
  count; filter by what you need.
- Collletttivo, The League of Moveable Type, Velvetyne. Small catalogues:
  read the whole font list. Some foundry sites render with JavaScript and
  return little to a plain fetch; then read the foundry's public source
  repository listing instead (Collletttivo publishes its fonts as
  repositories, Ronzino among them).
- Fontsource packages open-source fonts for self-hosting; use it to load a
  face you found elsewhere, and check the licence of the font inside.

The lists below in "Faces to shortlist from" are a starting sample of
verified faces, not a substitute for the search.

#### Checking the candidates

- Check each candidate against each requirement. You may not be able to see
  a rendering, so use what can be read as text: fetch the family's page or
  specimen page at its source and read its listed axes (`wght`, `wdth`,
  `opsz` and others), its styles (italics, widths), and any features or
  character sets it lists (tabular or old-style figures, small caps).
  The font file's feature list is another readable source when you have the
  file. Glyph shapes and stroke contrast usually cannot be read this way. To
  see them without a specimen page, download the font file and render a
  test line locally (a short HTML page that loads it, or any tool that draws
  text from a font file). If you cannot, record the trait as unconfirmed.
- When a trait cannot be confirmed from a readable source, write it in
  `direction.md` as unconfirmed and add it to a short list for the user to
  check by eye. Never state a trait as verified from memory: a family you
  remember as having a feature may not have it in the version you load.
- Check the licence for a face found outside this file the same way as for
  the table: source, licence name, and a "confirm at the source" note where
  the licence page could not be read.

### 4. Pick, and record the two that lost

Pick the candidate that meets the requirements best. In `direction.md`, list
the two rejected candidates for each role with the requirement each failed
or met less well.

Tie-break, when two candidates fit equally: prefer the one this skill's
lists name less often, since the common answer is the one other pages
already use. When neither is in any list, prefer the one whose confirmed
traits match more of your requirements; if that still ties, the one with
the smaller font files.

The convergence check. This is the one statement of the rule; the other
files point here. It applies to the face that carries the anchor (the
display face, or the single family) when that face appears in any of this
skill's lists: this file's faces lists, the single-family list, the sample
pairings, the "instead of" substitution table, "First-reach faces", or
`example.md`. Write one sentence that says what in the anchors requires
this face in particular, a sentence that would be false for a different
client. "It is warm and readable" fails; "its rounded, widened forms answer
the subject's fear of anything sharp or hurried" can pass, if the subject is
that. If you cannot write it, go back to step 3 and search again.

A face found outside all those lists needs no convergence sentence: the
shortlist record (requirements, filter and count, candidates marked
recalled or found, two rejected with reasons) stands in for it. A text face
whose job is reading carries less of the anchor; for it, the shortlist
record is enough.

Check the licence before you commit (next section). Where the licence is not
confirmed by the source (Fontshare self-hosting, Velvetyne per font, any
paid foundry, any face found outside this file's tables), say in
`direction.md` that the user must confirm it at the source before shipping.

## First-reach faces

Readers of this skill, working on unrelated clients, kept reaching for the
same faces. As display faces: Archivo, Bitter, Saira and Saira Semi
Condensed, Young Serif, Big Shoulders Display, Mona Sans. In shortlists,
picked or not: Archivo, Barlow Condensed, Bitter, Zilla Slab, Source Sans 3,
Red Hat Text, Public Sans, Hanken Grotesk. Bitter, Zilla Slab, Saira, Saira
Semi Condensed, Big Shoulders Display, Mona Sans and Red Hat Text are named
here only as observed habits; the research behind this skill did not verify
their source or licence, so check both if you consider one.

None of these is banned. A first-reach face falls under the convergence
check in step 4: pick it only with that sentence, or search again.

This list records what readers did in this skill's tests up to its last
revision. It cannot keep up with habits as they move, which is why the
search discipline in step 3 matters more than the list: treat any face you
thought of before searching, or that a common search path surfaces first,
as first-reach, listed here or not.

## Licences and sources

| Source | Terms as the research found them | What you do |
|---|---|---|
| Google Fonts | All families open source; commercial use allowed, including in products that are sold. Most are SIL Open Font License, some Apache or Ubuntu Font Licence. | Note "open licence, check the family page" in `direction.md`. The per-family licence is on the family's page. |
| SIL Open Font License 1.1 | Use, embed, modify and redistribute, including bundled with software that is sold. The font cannot be sold on its own. Modified versions cannot use a Reserved Font Name. | Safe to self-host. Keep the licence file with the font files. |
| Fontshare (Indian Type Foundry) | Free for personal and commercial use. Families are under the ITF Free Font License or SIL OFL. Self-hosting and redistribution terms could not be confirmed on a first-party page. | Tell the user to read the licence on Fontshare before shipping self-hosted files. Say so in `direction.md`. |
| Velvetyne | The foundry states all its fonts are libre and open source for personal and commercial work, with credit to the designer and foundry. The licence of each font was not confirmed. | Open the font's own page and confirm its licence before use. Credit the designer. |
| Collletttivo | SIL OFL; personal and commercial use in print and digital. | Safe to self-host. |
| The League of Moveable Type | Free and open source, SIL OFL, commercial use allowed. | Safe to self-host. |
| Fontsource | npm packages of open-source fonts for self-hosting. It states no licence of its own. | Check the licence of the font inside the package. |
| Paid foundries (Klim, Pangram Pangram, Commercial Type, Grilli Type, Dinamo, Colophon, Production Type) | Web licences are paid and usually metered. Terms were not confirmed. | Use only if the client already holds a web licence. Write that the user must confirm it. |

When Fontshare carries a family that is also on Google Fonts (Space Grotesk,
Epilogue, Familjen Grotesk), use the Google Fonts or OFL copy.

Newsreader is published by Production Type and released on Google Fonts. The
Google Fonts release is open; the foundry's paid origin does not change that.

## Faces to shortlist from

Grouped by kind of letter, not by tone, so the requirement leads. "In
catalogue" means the research confirmed the family in the Google Fonts
metadata or the Fontshare API. "Named" means the research recommends the
face as an alternative but did not record a catalogue check: confirm the
family page at the source before you use it.

Google Fonts faces carry an open licence (most are OFL); check the family
page. Fontshare faces carry the ITF Free Font License or OFL: tell the user
to confirm the self-hosting terms on Fontshare before shipping.

### Serifs for text, many with an optical size axis

| Face | Source | Status | Axes, range, note |
|---|---|---|---|
| Newsreader | Google Fonts | in catalogue | `opsz`, `wght`; long reading |
| Source Serif 4 | Google Fonts | in catalogue | `opsz` |
| Literata | Google Fonts | in catalogue | `opsz` |
| Hedvig Letters Serif | Google Fonts | in catalogue | `opsz` |
| Lora | Google Fonts | in catalogue | |
| Libre Caslon Text | Google Fonts | in catalogue | classical, restrained |
| Fraunces | Google Fonts | in catalogue | `opsz`, `wght`, `SOFT`, `WONK`; on the "tasteful free" list, justify it |

### Serifs for display, high contrast or strong character

| Face | Source | Status | Axes, range, note |
|---|---|---|---|
| Bodoni Moda | Google Fonts | in catalogue | `opsz`, `wght`; high contrast |
| Libre Caslon Display | Google Fonts | named | the display cut of Libre Caslon; confirm the family page |
| Cormorant Garamond | Google Fonts | in catalogue | delicate; large sizes only |
| Young Serif | Google Fonts | in catalogue | rarely used |
| Gloock | Google Fonts | named | display serif with contrast |
| Zodiak | Fontshare | in catalogue | ITF FFL |
| Gambetta | Fontshare | in catalogue | ITF FFL |
| Fanwood | League of Moveable Type | on the foundry's site | OFL |
| Borges | Collletttivo | listed by the foundry | OFL |
| Playfair Display | Google Fonts | in catalogue | ubiquitous; only with a reason |
| Instrument Serif | Google Fonts | in catalogue | one weight; flagged default, justify it |

### Grotesques and neutral sans

| Face | Source | Status | Axes, range, note |
|---|---|---|---|
| Hanken Grotesk | Google Fonts | in catalogue | neutral |
| Albert Sans | Google Fonts | in catalogue | neutral |
| Public Sans | Google Fonts | in catalogue | plain, institutional |
| IBM Plex Sans | Google Fonts | in catalogue | engineered; has a Plex Mono sibling |
| Archivo | Google Fonts | in catalogue | `wdth` axis; sturdy |
| Barlow, Barlow Condensed | Google Fonts | in catalogue | one superfamily in several widths |
| Instrument Sans | Google Fonts | in catalogue | `wdth`, `wght` |
| Libre Franklin | Google Fonts | named | sturdy |
| Bricolage Grotesque | Google Fonts | in catalogue | `opsz`, `wdth`, `wght`; quirky |
| Familjen Grotesk | Google Fonts, also Fontshare | in catalogue at Fontshare | quirky; use the Google Fonts copy |
| Schibsted Grotesk | Google Fonts | named | quirky |
| Epilogue | Google Fonts, also Fontshare | in catalogue at Fontshare | use the Google Fonts copy |
| Switzer | Fontshare | in catalogue | Swiss-style neutral; ITF FFL |
| Cabinet Grotesk | Fontshare | in catalogue | ITF FFL |
| Ronzino | Collletttivo | in the foundry's repository | OFL |
| League Spartan | League of Moveable Type | on the foundry's site | OFL |

### Humanist, rounded and geometric sans

| Face | Source | Status | Axes, range, note |
|---|---|---|---|
| Source Sans 3 | Google Fonts | in catalogue | humanist, plain |
| Figtree | Google Fonts | in catalogue | humanist, friendly |
| Atkinson Hyperlegible | Google Fonts | in catalogue | high legibility |
| Nunito | Google Fonts | in catalogue | rounded |
| Fredoka | Google Fonts | in catalogue | rounded; `wdth`, `wght` |
| Outfit | Google Fonts | in catalogue | geometric |
| Plus Jakarta Sans | Google Fonts | in catalogue | geometric; popular |
| Onest | Google Fonts | named | alternative to Plus Jakarta Sans |
| DM Sans | Google Fonts | in catalogue | `opsz`; popular, no evidence as a tell |
| General Sans | Fontshare | in catalogue | geometric; already common; ITF FFL |
| Satoshi | Fontshare | in catalogue | Fontshare's most viewed; becoming a default |

### Display faces with a loud character

| Face | Source | Status | Note |
|---|---|---|---|
| Caprasimo | Google Fonts | in catalogue | heavy display; few words |
| Shrikhand | Google Fonts | in catalogue | script-like; headings only |
| Space Mono | Google Fonts | in catalogue | a mono used as display |
| Velvetyne families (Gulax, Ouroboros, Compagnon, Jgs Font, others) | Velvetyne | listed by the foundry | experimental; confirm each font's licence on its page, credit the designer |
| Erode, Boska, Sentient | Fontshare | named as less viewed | check classification on the specimen; ITF FFL or OFL |

### Mono

| Face | Source | Status | Note |
|---|---|---|---|
| IBM Plex Mono | Google Fonts | in catalogue | |
| JetBrains Mono | Google Fonts | in catalogue | |
| Space Mono | Google Fonts | in catalogue | |
| Geist Mono | Google Fonts | in catalogue | flagged default; justify it |

## Single-family options

When step 2 says one family is enough, these verified families have the range
to set headline and body from one source. They are equally valid starting
points; the requirement decides which, and none of them is the answer for a
plain page.

| Family | What gives it range |
|---|---|
| Bricolage Grotesque | optical size, width and weight axes |
| Archivo | width axis |
| Instrument Sans | width and weight axes |
| Fredoka | width and weight axes; rounded |
| Barlow with Barlow Condensed | several widths in one superfamily |
| IBM Plex Sans with IBM Plex Mono | one superfamily, sans and mono |
| Newsreader | optical size and weight axes |
| Source Serif 4 | optical size axis |
| Literata | optical size axis |
| Hedvig Letters Serif | optical size axis |
| DM Sans | optical size axis |
| Fraunces | optical size, weight and two style axes; justify it |

Stripe and Pentagram load a single family on their landing pages, which
shows one family can carry a crafted page when weight, size and width do the
work.

## Sample pairings

The research verified 26 pairings, below, grouped by tone. They are a
sample, not the catalogue. Use them to see how a display and a text face
differ, and as candidates in step 3, never as the lookup that ends the
search. "Seen on" names live sites whose stylesheets loaded the display face
when the research scanned them; "none confirmed" means no live site was
found, so do not cite one.

### Technical and precise

| Display / text | Source, licence | Seen on | Notes |
|---|---|---|---|
| IBM Plex Sans / IBM Plex Mono | Google Fonts, open | Railway, Val Town | engineered, even strokes |
| Archivo / JetBrains Mono | Google Fonts, open | Railway, Bun | Archivo has a width axis |
| Space Mono / Hanken Grotesk | Google Fonts, open | Arc | mono as display, for a terminal voice |
| Geist / Geist Mono | Google Fonts, open | Vercel | flagged default: use only for a developer tool with a written reason and a distinctive layer elsewhere |

### Warm and editorial

| Display / text | Source, licence | Seen on | Notes |
|---|---|---|---|
| Fraunces / Newsreader | Google Fonts, open | Kiel Foods | both have an optical size axis. Fraunces is on the "tasteful free" list: justify it |
| Instrument Serif / Instrument Sans | Google Fonts, open | Raycast, Warp | flagged default: justify before use. Instrument Serif has one weight |
| Newsreader / Source Sans 3 | Google Fonts, open | Print (printmag) | long reading |
| Zodiak / General Sans | Fontshare, ITF FFL | Rive | General Sans is already common on Fontshare |
| Gambetta / Switzer | Fontshare, ITF FFL | Switzer on Framer and Every; Gambetta none confirmed | |

### Luxurious

| Display / text | Source, licence | Seen on | Notes |
|---|---|---|---|
| Libre Caslon (Text or Display) / Public Sans | Google Fonts, open | Framer | classical, restrained |
| Cormorant Garamond / Hanken Grotesk | Google Fonts, open | none confirmed | delicate at large sizes only |
| Bodoni Moda / Albert Sans | Google Fonts, open | none confirmed | optical size axis; high contrast for labels and fashion |
| Playfair Display / Source Sans 3 | Google Fonts, open | none confirmed | the stock "luxury" serif: prefer Bodoni Moda or Libre Caslon unless you have a reason |

### Playful

| Display / text | Source, licence | Seen on | Notes |
|---|---|---|---|
| Bricolage Grotesque / Figtree | Google Fonts, open | Zen Browser, Supahub | axes for optical size, width and weight |
| Fredoka / Nunito | Google Fonts, open | none confirmed | rounded; children's and casual products |
| Caprasimo / Outfit | Google Fonts, open | none confirmed | heavy display, use few words |
| Shrikhand / DM Sans | Google Fonts, open | none confirmed | script-like display, headings only |

### Utilitarian

| Display / text | Source, licence | Seen on | Notes |
|---|---|---|---|
| Public Sans / IBM Plex Mono | Google Fonts, open | Nuxt | plain and civic |
| Barlow Condensed / Barlow | Google Fonts, open | Bram.us | one superfamily; width contrast does the work |
| Atkinson Hyperlegible / Source Serif 4 | Google Fonts, open | none confirmed for Atkinson | high legibility text needs |

### Craft or artisanal

| Display / text | Source, licence | Seen on | Notes |
|---|---|---|---|
| Young Serif / Hanken Grotesk | Google Fonts, open | none confirmed | rarely used, so it does not read as a default |
| Fanwood / League Spartan | League of Moveable Type, OFL | the foundry's own site | |
| Borges / Ronzino | Collletttivo, OFL | Ronzino on the foundry's site | |

### Institutional

| Display / text | Source, licence | Seen on | Notes |
|---|---|---|---|
| Source Serif 4 / Source Sans 3 | Google Fonts, open | Gwern.net (Source Serif) | |
| Lora / Lato | Google Fonts, open | The New Yorker | Lato is ubiquitous: pair it only with a reason |
| Cabinet Grotesk / Satoshi | Fontshare, ITF FFL | Cabinet Grotesk on Freshysites; Satoshi on Framer, Trigger.dev | Satoshi is Fontshare's most viewed family and is becoming a default |

### Experimental

Velvetyne's catalogue (Gulax, Ouroboros, Compagnon, Jgs Font, among others)
suits art, music and event pages. Confirm each font's licence on its own page
before use and credit the designer.

### On Fontshare

The most viewed Fontshare families (Satoshi, Clash Display, General Sans) are
already common on generated and template pages. When a Fontshare face fits,
look first at less used ones: Zodiak, Gambetta, Erode, Boska, Sentient,
Switzer.

### Serif display on a technical product

A serif display on a developer or business product is a real option, and
several well-made pages use one (Resend, Notion, Cursor, Oura). Their faces
are licensed or custom. Free faces that serve the same role: Source Serif 4,
Newsreader, Libre Caslon.

## Overused faces and what to use instead

A face is a tell when it is the unconsidered default. The research separates
three cases. This is the one statement of these sets; `SKILL.md` and
`visual-tells.md` point here.

Always a tell:

- The system stack (`system-ui`, `-apple-system`, Segoe UI) or Arial,
  Helvetica or Roboto as the only face. It shows no typeface was chosen.
- A family named in CSS that is never loaded. The page renders in the
  fallback and the decision never reaches the visitor.

A tell unless justified: Georgia, Times New Roman, Inter, Geist, Space
Grotesk, Instrument Serif and Fraunces.

- Georgia and Times New Roman are installed fallbacks, and unguided agents
  in this skill's test reached for Georgia when they wanted "a serif"
  without choosing one. Use one only with a reason from the subject (a
  client whose printed material is set in it, for example), written down.
- Inter, Geist, Space Grotesk, Instrument Serif and Fraunces are the faces
  commentary names as the defaults of generated pages, and Space Grotesk,
  Geist and Instrument Serif recur as a set. They are a tell when:

- one of them is both the display and the text face,
- it has no mono or distinct display companion,
- no reason is recorded, or
- two or more of them appear together, worse with a cream and terracotta
  palette.

They are acceptable when chosen for a reason, paired, and backed by other
decisions in scale, tracking, palette and layout. Linear, Raycast and Ghost
set headlines in Inter with a mono face or a display cut; Vercel uses its own
Geist as its brand face.

Ubiquitous, flag at lower weight (Poppins, Montserrat, Lato, Open Sans,
Playfair Display): very common on the web, so they read as unchosen. Use
them only with a reason.

| Instead of | Try | Keeps |
|---|---|---|
| Inter | Hanken Grotesk, Albert Sans; IBM Plex Sans; Public Sans | neutral UI sans; engineered; institutional |
| Roboto | Public Sans, Source Sans 3 | plain sans |
| Arial, Helvetica | Switzer (Fontshare), Hanken Grotesk | Swiss-style neutral |
| Open Sans | Source Sans 3, Figtree | humanist, friendly |
| Poppins | Outfit, Plus Jakarta Sans; General Sans (Fontshare) | geometric |
| Montserrat | Archivo, Libre Franklin | wide, sturdy |
| Lato | Nunito, Source Sans 3 | soft or plain |
| Space Grotesk | Bricolage Grotesque, Familjen Grotesk, Schibsted Grotesk | quirky grotesk |
| Geist, Geist Mono | IBM Plex Sans and Plex Mono; JetBrains Mono | technical |
| Instrument Serif | Newsreader, Gloock; Fraunces only with a reason | display serif with contrast |
| Playfair Display | Bodoni Moda, Libre Caslon Text, Cormorant Garamond | high-contrast serif |

DM Sans, Plus Jakarta Sans and Manrope are popular but have no evidence as
AI tells. Use them freely when they fit, and still write the reason.

The "try" column is not a new default list. If you reach for the first item
in every row on every project, you have rebuilt the problem. Choose from the
direction.

## Scale

- Pick one ratio between 1.2 and 1.333 and six or seven steps. Drive the large
  steps with `clamp()` between a phone and a desktop size.
- The hero headline should be at least 2.5 times the body size. On the
  well-made pages studied, headlines of 1 to 8 words were 64 to 96px on
  desktop; longer statements were 42px or smaller.
- Starting values below are a synthesis, not sourced measurements. Check them
  by eye with the real copy, at 360px and 1440px wide.

| Role | Size | Line height | Tracking |
|---|---|---|---|
| Hero display | `clamp(2.75rem, 1.5rem + 5vw, 6rem)` | 0.95 to 1.05 | -0.02em to -0.04em |
| H2 | `clamp(2rem, 1.4rem + 2.5vw, 3.5rem)` | 1.05 to 1.15 | -0.01em to -0.02em |
| H3, lead | 1.25 to 1.5rem | 1.25 to 1.35 | -0.005em |
| Body | 1rem minimum; 1.0625 to 1.125rem for editorial | 1.5 to 1.6 sans, 1.45 to 1.55 serif | 0 |
| Caption | 0.8125 to 0.875rem | 1.4 | +0.005em |
| All-caps label | 0.6875 to 0.8125rem | 1.2 | +0.06em to +0.12em |
| Mono | 0.875 to 0.95em of body | 1.5 | 0 |

Condensed faces tolerate larger hero sizes; wide faces need smaller ones.
Tight negative tracking suits large sans headlines; most serifs need less.

Write the hero size for the real headline. Size follows length:

- Count the headline. The 64 to 96px band is for short headlines of about 1
  to 8 words. A long statement (a full sentence, 12 words or more, or around
  60 characters and up) belongs at 42px or smaller on desktop, as on the
  well-made pages that set long sentences.
- The test is the line count and the measure, checked at 1280 and 1440px in
  the hero's real columns: the headline should set in two to three
  lines, at roughly 15 to 35 characters per line. A fourth line fails
  `landing-skills:landing-review` row A6 at any width. More lines than that means
  the size is too large for the length; one line across the whole page means
  it can grow. These line and character counts are starting values from
  judgement, not measurements.
- A 100-character headline at 3.5rem (56px) fails the test; bring it down to
  about 2.5rem, or ask `landing-skills:landing-copy` for a shorter headline
  with the rest moved to the subhead.
- Test that it does not break into an orphan last word. If it does, change
  the size or the column width rather than the copy.

Give the hero token a range that fits this headline, not the table's
default.

## Measure, leading and tracking

- Measure: 45 to 90 characters per line; aim for 62 to 72ch for body text.
  Set `max-width` in `ch` on text blocks.
- Body leading: 120 to 145 percent of the size is the print guideline. The
  starting values above use 1.45 to 1.6 for body text on screen; judge by eye
  with the real face. Headings 0.95 to 1.15.
- Capitals and small labels need positive tracking. Large headlines usually
  need slightly negative tracking.
- Set `text-wrap: balance` on headings. It is supported in current Chrome,
  Safari and Firefox; older browsers ignore it, which is harmless.
- Use faces with an optical size axis (`opsz`) where you can: Fraunces,
  Newsreader, Source Serif 4, Bricolage Grotesque, Bodoni Moda, Literata.
  `font-optical-sizing: auto` is the default and adjusts the design to the
  size.
- Do not split a headline into one span per letter for animation without an
  accessible label on the heading; screen readers and text extraction break.

## Font loading notes

`landing-skills:landing-build` does the loading. Record what it needs:

- Which files: family, weights, styles, and whether a variable file is worth
  it. A variable file replaces several static files but is usually larger
  than one. Use it when you load three or more weights or styles of a family.
- Format WOFF2 only. Subset to the characters the page uses. When the page is
  not in English, check that every accented letter renders in the subset.
- Preload only the one or two files used above the fold, with `crossorigin`.
  Preloading more competes with the hero image.
- `font-display: swap` for the text face, with a fallback metric-matched by
  `size-adjust`, `ascent-override`, `descent-override` and
  `line-gap-override` on a local `@font-face`, so the swap does not shift
  layout. `optional` for a decorative display face when layout stability
  matters more than the face appearing on a slow first visit.
- Self-hosting is not faster by itself; it helps with a CDN and HTTP/2, and it
  gives privacy and version pinning. Google Fonts' CSS endpoint is fine when
  there is no strict budget.

In `tokens.css`, a font token is a stack: the chosen face, then a fallback
that is metrically close to it, then the generic family. Template, with
slots to replace:

```css
:root {
  --font-display: "<display face>", <closest fallback>, <generic family>;
  --font-text: "<text face>", <closest fallback>, <generic family>;
}
```

Choosing the fallback:

- It must be installed on your visitors' devices, so it comes from the fonts
  the operating systems ship, or a generic keyword such as `system-ui`,
  `serif` or `sans-serif`.
- Pick it for the chosen face's proportions, not its name: the same
  classification (serif or sans), and the closest width and x-height. A
  condensed face wants a narrow fallback if one exists; a wide face wants a
  wide one. Compare by setting the same sentence in both at the same size
  and measuring the line lengths and the height of the lowercase.
- Write the fallback and the reason in `direction.md`. The build then
  corrects the remaining difference with `size-adjust` and the override
  descriptors, so the swap does not shift the layout.
- Do not copy a fallback from an example; one chosen for another face is
  usually wrong for yours.

A system face as a fallback in the stack is fine. It is a tell only when it
is the face you chose.

## Recording the decision

In `direction.md`, the `Typography` section holds, in this order:

1. The requirements from step 1 of "How to choose".
2. The number of faces and the reason.
3. For each role: the search filter and the number of families that passed
   it; the candidates, each marked recalled or found by search (at least two
   found); the chosen face and the two that lost, each with the requirement
   it failed or met less well.
4. The typography table, one row per chosen face: role, face, source,
   licence (with a "confirm at the source" note where the licence is
   unconfirmed), weights, and why it fits the anchors.
5. The convergence sentence, where step 4's rule applies. Traits you could
   not confirm, listed for the user.
6. The fallback in each font stack and why it was chosen.
7. The scale as token names and what each is for.

Example of items 3 and 4 for a different client, whose anchor is a set of
engraved brass nameplates:

- Display requirement: high stroke contrast at large sizes, like engraving.
  Search: Google Fonts metadata filtered to serif and display categories
  with an `opsz` axis and a weight of 700 or more, plus the Fontshare API
  filtered to serif families; 23 families passed (an invented count for
  this example), and the shortlist was taken from across that result.
  Candidates: Zodiak (found by search; chosen), Bodoni Moda (found by
  search; lost: its fashion-magazine register fights the trade setting),
  Gloock (recalled; lost: not confirmed in the catalogue, so the licence
  check is extra work for no gain).

| Role | Face | Source | Licence | Weights | Why |
|---|---|---|---|---|---|
| Display | Zodiak | Fontshare | ITF Free Font License; confirm self-hosting terms on Fontshare before shipping | 700 | contrast at display size matches the engraved plates (checked on the Fontshare specimen) |
