# Visual tells

Read this at step 2 (the default test) and again before handing off. A tell
is a visual default that marks a page as generated or templated. Each one
below comes with the way out.

Two things to keep in mind:

- Tells come in clusters. Indigo accent, gradient text, pill badge and three
  cards usually arrive together because they come from the same scaffold.
  Fixing one while keeping the rest still reads as generated. Fix the
  cluster.
- Swapping each default for its fashionable replacement builds the next
  default. The research found that the "tasteful" escape (cream and
  terracotta, Instrument Serif or Fraunces, mono everything) is already a
  recognised pattern. The way out of every tell is the same: derive the
  choice from the client's subject and write the reason down.

## Contents

- The convergence this skill exists to prevent
- Cluster 1: the indigo scaffold
- Cluster 2: the tasteful counter-default
- Cluster 3: unconsidered type
- Cluster 4: uniform surfaces
- Cluster 5: template composition
- Cluster 6: filler imagery and icons
- Cluster 7: decoration that moves
- Cluster 8: the drift of careful readers
- Detector rules covered here
- What the detector cannot see

## The convergence this skill exists to prevent

In a test for this skill, agents without guidance built landing pages for
unrelated clients in different trades. Most of them landed on the same look:
a cream or off-white background, a single dark green or terracotta accent,
and the system font stack or Georgia. The one exception was a dark product
page, and it chose near-black with one warm orange accent and the system
stack. None of those values came from anything in the clients' subjects.

Your own first idea is likely to land in the same place, which is why the
process makes you write the reason down before you pick a value. The rule
that follows from the research: a choice is a tell when it is the
unconsidered default, and fine when it is chosen for a reason specific to
the client. Cream is fine for a paper goods shop that photographs on paper.
Inter is fine on a developer tool that pairs it with a mono face and says
why. The sets of faces that are always a tell, or a tell unless justified,
are stated once in `typography.md` ("Overused faces").

The defaults to compare against in the default test: cream with one dark
green or terracotta accent; system or Georgia type; near-black with a warm
accent; indigo or violet; the trade's document (Cluster 8); a cool grey
ground with a dark band, condensed capitals and one accent (Cluster 8); and
every cluster below.

## Cluster 1: the indigo scaffold

What it looks like: near-black or white page, Tailwind indigo or violet
accent, a purple-to-blue gradient in the hero, a headline filled with a
gradient, a pill badge above it, glass cards over blurred colour orbs.

Why it reads as generated: Tailwind UI shipped its buttons as `bg-indigo-500`
(`#6366F1`), and Tailwind v4's indigo and violet stops (`#615FFF`, `#8E51FF`,
`#4F39F6`, `#7F22FE`, slate `#0F172B`) recur across generated sites. Commentary
calls the indigo-to-purple gradient the loudest single tell. Anthropic's own
guidance on frontend output names purple gradients on white among the
defaults to avoid.

Way out:

- Accent from the subject's anchor, in OKLCH, outside hue 260 to 290 unless
  the brand is that colour (see `colour.md`).
- Flat colour. A gradient only between two neighbours in the palette, and
  only where it describes something (light, depth, a material), never as the
  hero's main device.
- Headline in a solid ink colour. Size, weight and the typeface carry it.
- No badge above the headline. Fold "new" or "now in beta" into the headline
  or the first sentence, or drop it.
- Opaque surfaces from the palette. Blur only where real content scrolls
  under a sticky bar.
- No orbs or blobs. Use a real image, a texture from the subject, or a plain
  ground.

## Cluster 2: the tasteful counter-default

What it looks like: cream or warm paper background, terracotta or one dark
green accent, an oversized serif headline (often italic), Instrument Serif or
Fraunces, sometimes mono labels everywhere.

Why it reads as generated: it is what models produce when told to avoid the
indigo look. One widely shared anti-slop guide describes cream with a
terracotta accent as Claude's own interface colour. In this skill's test,
most unguided pages chose cream with one dark green or terracotta accent,
whatever the subject.

Way out:

- Cream is fine when it comes from the subject (the client's letterhead, a
  limewashed wall, undyed linen) and is recorded with its anchor. The tell
  is the bundle: cream plus terracotta plus a fashionable display serif.
- Accent from the subject. A terracotta accent is fine when it was derived
  from the subject and the reason is recorded.
- Serif display only with a reason tied to the anchors, and a face chosen
  from the tone (see `typography.md`), not the first serif on a "good free
  fonts" list.

## Cluster 3: unconsidered type

What it looks like: the system font stack, Arial or Roboto as the only face;
Georgia or Times as an unexplained "serif choice"; Inter for everything; a face declared in CSS
and never loaded; Space Grotesk, Geist and Instrument Serif together.

Why it reads as generated: commentary names Inter, Roboto, Arial, system
fonts and Space Grotesk as the faces generated pages default to, and Space
Grotesk, Instrument Serif and Geist as a recurring set. Most unguided pages
in this skill's test used only system fonts or Georgia.

Way out: a display face and a text face chosen for the tone, from a source
with a known licence, with the reason written down (see `typography.md`).
Inter or Geist are acceptable with a reason and a mono or display companion,
as Linear and Vercel show.

## Cluster 4: uniform surfaces

What it looks like: the same radius, the same 1px border and the same soft
black shadow on every card, button, image and panel; pill buttons
everywhere; coloured left borders on callout cards.

Why it reads as generated: when every element has the same treatment,
nothing is above anything else. These are framework defaults
(`rounded-2xl shadow-lg`, `shadow-sm`, `rounded-lg`) left unchanged.

Way out:

- A radius scale with a reason: two or three values (for example 0 for
  sections and images, 4px for controls), tied to the subject where possible
  (square tiles, rounded pebbles, machined edges).
- Shadow only on elements that sit above the page (a menu, a dialog), tinted
  with the ink colour. Flat surfaces separated by space or a rule elsewhere.
- One button shape for the primary action. Pills only if the direction calls
  for them.
- Emphasis by size, weight, position or a plain rule, not a coloured left
  border.

## Cluster 5: template composition

What it looks like: centred headline, centred paragraph, two centred buttons;
logo bar; three equal feature cards; zig-zag text and image rows; testimonials
grid; three-tier pricing with a ringed middle; FAQ; centred final CTA; four
column footer. Every section the same padding and width.

Why it reads as generated: the order and shapes come from a scaffold, not
from the questions this visitor has. Content is cut to fit the grid.

Way out: the section map in `layout.md`. Order follows the copy, layout
follows each section's content, at least two neighbouring sections differ in
width, density or ground, and one or two moments get the boldness.

## Cluster 6: filler imagery and icons

What it looks like: stock or AI-generated people, flat illustration packs,
abstract 3D shapes, browser-chrome frames around screenshots, a thin line
icon on every card, emoji as icons, a sparkles icon for "AI", stock avatars
next to invented names.

Why it reads as generated: none of it shows the client's real thing. Icons
from the default set appear on every shadcn site.

Way out: real photographs, real screenshots, drawings and diagrams from the
subject, or type alone. Labelled placeholders at the right ratio when the
asset is pending (see `assets.md`). Icons from one set, only where they
mean something, or none. Words instead of sparkles.

## Cluster 7: decoration that moves

What it looks like: every section fades up on scroll the same way, cards grow
on hover, a button label ends in an arrow glyph, drop-in effect components
(border beams, shiny text, count-up numbers, bouncing elements).

Why it reads as generated: the same effect on everything tells the visitor
nothing. These are library defaults dropped in.

Way out: decide motion in the `Motion and 3D intent` section of
`direction.md`: `none`, or a named moment with a purpose. Hover states that
change meaning (colour, underline, a pressed state). Button labels that say
what happens, without an arrow. `landing-skills:landing-motion` implements
the intent.

## Cluster 8: the drift of careful readers

What it looks like: the page styled as the trade's own document (a ledger,
a blueprint, a spec sheet, a receipt), with a cool grey or pale blue
ground, a blue-tinted near-black ink, one dark band, narrow even-stroke
capitals like document lettering, and a single accent.

Why it reads as generated: it is where agents following this skill drift
when they derive everything from the easiest thing to name, the trade's
paperwork. Three unrelated clients run through an earlier version of this
skill all landed on it, two of them with the same type family. Each choice
had a reason; the combination is still a house style.

Way out:

- Anchors from three kinds of source: material or object, place or light,
  gesture or moment. Paperwork supplies one anchor at most.
- Put "the trade's document" in the rejected directions unless you can say
  why it is right for this client and wrong for a competitor with the same
  paperwork.
- Derive the ink and the ground from the anchors: warm, green or neutral
  inks are as valid as a cool one, and a ground can be tinted, deep or dark.
- Choose the display face from requirements that come from the material or
  the moment, not only from lettering, and find candidates by searching a
  catalogue rather than by recall (`typography.md`, "Searching a catalogue"
  and "First-reach faces").
- Treat the inverted dark band as a choice with a reason: use it only when
  the content needs a change of ground at that point. Otherwise make the
  section loud by scale on the same ground, a full-bleed image, a tinted
  step of the palette, or a change of width (`layout.md`).
- Check the accent against the first-reach accents in `colour.md` (acid
  lime on a dark band, stamp red on a pale ground); keep one only when it is
  derived from an anchor, with the reason recorded.

## Detector rules covered here

`landing-skills:landing-review` ships a static detector. These are its
visual rules, what each catches, and where the way out is.

| Rule id | Catches | Way out |
|---|---|---|
| `palette-cream-terracotta` | the cream `#F4F1EA` and terracotta `#D97757` pair | Cluster 2; `colour.md`, cream and warm grounds |
| `indigo-default-accent` | Tailwind indigo and violet hex values and `indigo-500`/`600` utilities | Cluster 1; `colour.md`, method step 2 |
| `purple-blue-gradient` | `from-purple`/`indigo`/`violet` to `blue`/`pink`/`cyan`/`purple` utilities | Cluster 1 |
| `gradient-text` | `background-clip: text`, `bg-clip-text` | Cluster 1 |
| `gradient-orb` | `rounded-full` with `blur-3xl`, or `blur-[100px]` | Cluster 1 |
| `glass-card` | `backdrop-blur` with `bg-white/5`, `/10` or `/20` | Cluster 1 |
| `pill-badge` | a `rounded-full` uppercase or tracked span or div | Cluster 1 |
| `overused-font` | Inter, Roboto, Open Sans, Space Grotesk, Geist or Instrument Serif as the leading `font-family` or in a Google Fonts URL | Cluster 3; `typography.md` |
| `default-shadow` | a black shadow at 10 percent opacity | Cluster 4 |
| `emoji-bullet` | an emoji at the start of a heading or list item | Cluster 6 |
| `sparkles-icon` | a Sparkles import from `lucide-react` | Cluster 6 |
| `stock-testimonial` | stock names and placeholder avatar services | Cluster 6; never invent people or proof |
| `hover-scale` | cards or buttons that scale to 1.05 or 1.1 on hover | Cluster 7 |
| `arrow-in-button` | a `→` at the end of a button or link label | Cluster 7 |
| `fade-up-everywhere` | `data-aos="fade-up"` | Cluster 7 |
| `stock-effect-component` | Magic UI effects, count-up components, `animate-bounce` | Cluster 7 |

The detector's other rules cover copy, code and conversion (inflated
phrases in English and Spanish, negation pivots, vague attribution,
placeholder text, builder fingerprints, dead forms and links, the
reduced-motion kill switch). `landing-skills:landing-copy`,
`landing-skills:landing-build` and `landing-skills:landing-motion` handle
those.

A detector hit is a prompt to look, not a verdict. Inter with a recorded
reason and a mono companion is a deliberate choice; say so in `direction.md`.

The detector reads source lines. It does not see `--font-*` values in
`tokens.css` (they are not `font-family` declarations), and it does not see
the rendered page. So the self-check in `SKILL.md` still applies even when
the detector reports nothing.

## What the detector cannot see

These tells exist only in combination, in counts, or on the rendered page.
Check them by reading `direction.md` and, once built, by looking at the page
at 360 and 1440px.

| Tell | How to check | Way out |
|---|---|---|
| The tasteful bundle (cream ground, terracotta or dark green accent, fashionable display serif) with values other than the two the detector knows | Compare your palette with "The convergence this skill exists to prevent" | Anchor from the subject; record it |
| Two or more of Space Grotesk, Geist, Instrument Serif, Fraunces on one page | Read the typography table | Keep one with a reason, or none |
| One of Inter, Geist, Space Grotesk, Instrument Serif or Fraunces as both display and text, with no companion | Read the typography table | Pair it or replace it |
| Any face in the "always a tell" or "tell unless justified" sets of `typography.md` ("Overused faces") without its reason | Read the font tokens and the typography table | A chosen face with a licence, or the face with its reason |
| A face declared and never loaded | In the build, check every `font-family` has an `@font-face` or a stylesheet link | Load it or remove it |
| The same radius and shadow on every element | Count distinct radius and shadow values in the build; one of each used everywhere is the tell | A radius scale; shadow only on raised elements |
| Uniform section padding | Count distinct section paddings; one value on every section is the tell | Two or three rhythm tokens with jobs |
| Three or six equal cards with an icon, title and two lines | Read the section map | A list, a 2 and 1 split, or a sequence |
| Every heading the same size and every section the same width | Read the section map | Vary by the section's job |
| Everything centred in the hero, dual CTA | Read the hero row of the map | Left-aligned text with an asymmetric visual, or a reason for centring |
| Template section order | Compare the map with the scaffold in Cluster 5 | Order from the copy |
| Decorative `01 02 03` numbering | Read the map | Numbers only when they mean something |
| Coloured left border on cards | Read surfaces and details | A plain rule or space |
| Browser-window chrome on screenshots | Read the assets table | Show the UI itself |
| Dark mode by inversion, or medium-grey body text on dark | Read the dark palette and its contrast table | A designed dark palette, body text L 0.90 to 0.93 |
| Barely passing or failing contrast | The contrast table, computed | Adjust L until the ratio passes, with margin |
| Stock or generated people, flat illustration packs, abstract 3D blobs | Read the assets table | Real assets or labelled placeholders |
| Generic mock data in product fragments (filler client names, round sums) | Read the fragments | Content from the audience's real work, captioned as an illustration |
| Text over a busy visual, unreadable at some width | Look at 360 and 1440px | Give the text its own columns or ground |
| The same reveal or hover on every element | Read the motion intent | `none`, or named moments with purposes |
| Cool grey ground, blue-tinted near-black ink, one dark band, condensed even-stroke capitals, one accent, often styled as the trade's document | Read the anchors, the rejected directions and the colour table together | Cluster 8 |
