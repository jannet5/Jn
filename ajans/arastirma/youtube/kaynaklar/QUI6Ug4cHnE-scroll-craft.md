# scroll-craft (Nate Herk) — QUI6Ug4cHnE videosunun skill'i

Kaynak: https://github.com/nateherkai/scroll-craft — kurulum: `git clone https://github.com/nateherkai/scroll-craft` → skills klasörüne; ya da plugin marketplace (.claude-plugin/marketplace.json)
Dosyalar: SKILL.md, engine/scrollcraft.{css,js}, references/{taste,feel,uniqueness,verify,assets,devices,hero-depth,worlds,worldflight,approved-collection}.md, scripts/{kie.mjs (görsel/video üretimi), shoot.mjs (ekran görüntüsü doğrulama), encode.sh, doctor.mjs}


---
## ../../../../README.md

# scroll-craft

**An agent skill for building premium, scroll-driven websites, with a real design standard.**

Use it with Codex, Claude Code, or another coding agent that can read instructions,
edit files, run commands, and inspect a browser. The skill contains the design
workflow, references, engine, and verification tools. It also ships as a Claude
Code plugin for convenient installation.

Most AI website output fails in one of two directions. It is either well behaved and forgettable, or it is a flashy scroll animation with 2.1:1 body text, a headline that wraps to six lines on a phone, and the same six sections every other AI page has. scroll-craft is built to fail neither way: it treats **interaction** and **craft** as one job rather than two.

[![MIT](https://img.shields.io/badge/licence-MIT-blue.svg)](LICENSE)
[![Agent skill](https://img.shields.io/badge/agent-skill-3b82f6.svg)](plugins/nateherk-design/skills/scroll-craft/SKILL.md)
[![Claude Code plugin](https://img.shields.io/badge/Claude%20Code-plugin-d97757.svg)](https://code.claude.com/docs/en/plugins)

---

## New in 0.3.0: the approved ten-site standard

The skill now includes the process behind ten approved immersive websites:
AI Automation Society, PERKFORM, Glaido, Herkules Advisory, Serein, FORME,
Pelagic, NOEMA, OFFGRID, and Afterhours.

### See the worked examples in motion

A 50-second walkthrough of several approved sites, showing their layered heroes,
pointer response, and scroll transitions.

https://github.com/user-attachments/assets/d193073b-5af9-45de-93ae-95bf7c6934d5

- Plan independent depth planes, contact anchors, and opening/midpoint/exit states.
- Use authentic brand assets and verified product details before generating imagery.
- Choose photographic compositing or real 3D rendering to suit the subject.
- Give each site its own navigation, information order, useful controls, and ending.
- Art-direct phones separately and verify actual scroll frames, fallbacks, and packages.
- Honor explicit creative delegation without forcing a redundant interview.

Read the [worked examples and production workflow](plugins/nateherk-design/skills/scroll-craft/references/approved-collection.md)
and the [hero-depth guide](plugins/nateherk-design/skills/scroll-craft/references/hero-depth.md).
These examples describe design behavior; client assets and private form data are
not bundled. The existing engine and video compatibility fixes are preserved.

## Three builds, three completely different pages

Same skill, same engine, no shared skeleton. The differences below are not themes: they are different page grammars, different navigation models, different endings.

### [AI Automation Society](https://aiautomationsociety.ai) · an AI community
A dark editorial landing for a 450,000-member community. One stat carries the whole promise, a live product surface rises into the frame, and the proof stacks under it as you fall down the page.

![AI Automation Society, a dark editorial community landing](media/ais.webp)

### [Nate Herk](https://www.nateherk.com) · a creator portfolio
High-key and bright, the opposite of the first. A lit-glass hero with the numbers up front, a portrait held in the light, and two clear next steps instead of a wall of links.

![Nate Herk, a high-key lit-glass creator portfolio](media/nateherk.webp)

### PERKFORM · a protein coffee
A filmic one-shot that hard-cuts to two full-bleed inverted grounds mid-page. Loud, product-forward, and the only one of the three that raises its voice.

![PERKFORM, a filmic one-shot product page](media/perkform.webp)

---

## What it actually does

**Interaction, engagement, and being unrepeatable**

- **Scroll is the timeline.** Video scrubs frame by frame under the wheel, sections pin while their argument advances, rails pan sideways, headlines assemble line by line, the page ground shifts colour as you travel, and the pointer moves things that are not scrolling.
- **Eight mutually exclusive page grammars.** Filmic one-shot, chaptered editorial, live surface, continuous world, typographic poster, gallery, split stage, rhythmic cutlist. Each one *forbids* what the others require, so two builds cannot quietly converge.
- **A required signature move.** Every build invents one bespoke interaction that exists on that site alone. A recoloured spotlight does not count.
- **A fingerprint gate.** A new build must differ from every page you have already made on at least 4 of 6 dimensions: grammar, nav, hero, act shape, close, signature move. Fail it and you change the plan, not the record.

**Craft, and how the page actually feels**

- **A feeling curve before any act exists.** One line per act: the emotion, then what on screen causes it. Two adjacent acts with the same feeling means one is filler.
- **One engineered peak.** Peak-end rule, applied literally. The peak gets the asset budget, the silence in front of it, and the most scroll room. A page with three peaks has none.
- **A typography floor.** Two families maximum, tracking that tightens as size grows, 45 to 75ch measure, line height inverse to measure, and light-on-dark compensated on three axes.
- **A spacing scale with actual rhythm.** 4px base, more space above a heading than below it, fluid section padding so a phone does not inherit desktop air.
- **Colour with six roles and one accent**, secondary text tinted rather than flat grey, no pure black, and a documented escape for pages that hard-cut between light and dark grounds.
- **Depth as five tools, not one.** Offset shadows, edge light, scale-and-blur as distance, overlap, and grain.
- **Brand guidelines are inputs, not decoration.** Point it at a brand kit and its hard rules win, including rules that forbid things the skill would otherwise reach for.
- **A refuse list.** Identical feature-card grids, `01 / 06` counters, scroll cues, gradient text, em dashes, invented statistics, fake dashboards, AI-purple gradients, and the cream-and-brass artisan palette every craft brand defaults to.

**It checks its own work**

A headless browser walks the finished page at every scroll position, waits for the video playhead to settle, and reports:

- **dead scroll**: scroll that changes nothing on screen
- **cues that never reach full opacity**: copy the reader can only ever see faded
- **contrast measured on the composited page**, per line, at the brightest frame that ever passes under it, with the direction picked per line so light-on-dark and dark-on-light are both graded correctly
- **legs stuck on a poster**: a clip that silently never decoded, which looks exactly like a paused film

Then it writes a contact sheet, because a machine can prove a page works and cannot tell you it means anything.

---

## Install

### Codex and other coding agents

Clone or download this repository. The complete skill lives in
[`plugins/nateherk-design/skills/scroll-craft/`](plugins/nateherk-design/skills/scroll-craft/).
Keep that folder intact, including its references, scripts, templates, and engine.

- **Codex:** copy the complete `scroll-craft` folder into your project's
  `.agents/skills/` directory, then ask Codex to use the Scrollcraft skill.
- **Other agents with skill support:** place the folder in the agent's documented
  skill directory.
- **Any coding agent with file access:** leave the repository in your workspace
  and ask it to read and follow the skill directly:

```text
Read plugins/nateherk-design/skills/scroll-craft/SKILL.md and use it to build
my website. Follow the referenced design and verification workflow.
```

Adjust the path if the repository is in a subfolder. Agents use their own tools
for file access, shell commands, browser inspection, and user questions. The
design workflow is shared; tool names and automatic skill discovery can differ.
The ten-site rebuild documented here was built with Codex.

### Claude Code plugin

```bash
/plugin marketplace add nateherkai/scroll-craft
```
```bash
/plugin install nateherk-design
```

Then use it by describing what you want, or invoke it directly:

```
/nateherk-design:scroll-craft
```

If the install summary says `Run /reload-plugins to activate.`, run that.

To hack on the skill without installing:

```bash
claude --plugin-dir ./plugins/nateherk-design
```

## First run

From this repository's root, run:

```bash
node plugins/nateherk-design/skills/scroll-craft/scripts/doctor.mjs
node plugins/nateherk-design/skills/scroll-craft/scripts/workspace.mjs --ensure
```

If you installed the skill elsewhere, use that folder's `scripts/` path instead.

Run `doctor` before anything else. The three most common setup faults all surface later as misleading errors otherwise: a stripped ffmpeg reports a missing filter as a syntax error in *your* command, a missing WebP muxer reports as a bad filename, and `playwright-core` resolves from the wrong directory.

## Requirements

| | Why | Notes |
| --- | --- | --- |
| **Node 18+** | every script | |
| **A full ffmpeg build** | encoding clips so they *scrub* rather than play | Some toolchains put a stripped ffmpeg on PATH with ~50 filters and no `scale`. `doctor` finds a real build if one exists; `SCROLLCRAFT_FFMPEG` overrides. |
| **`playwright-core` + Chrome** | the verification pass | `npm i playwright-core` **in the build folder** |
| **`KIE_AI_API_KEY`** | only if you want assets *generated* | Optional. Building from your own photos and footage needs no key and no spend, and it is a first-class route. See `.env.example`. |

## The workspace

Your builds and your fingerprint registry live in one directory, resolved rather than assumed. First hit wins:

1. `SCROLLCRAFT_HOME`
2. the nearest `.scrollcraft.json` walking up from the current directory: `{ "workspace": "path/to/builds" }`
3. `<project root>/scrollcraft`

Builds land in `<workspace>/builds/<name>/`; your registry is `<workspace>/FINGERPRINTS.md`.

**Your registry starts empty, and that is correct.** The gate exists to stop you repeating *yourself*, so your first build has nothing to clear and every build after it does. [`EXAMPLES.md`](EXAMPLES.md) is the author's twelve-row table, included so you can see what a filled registry looks like and which shapes tend to collide. It is illustration, not constraint.

## What is in here

```
plugins/nateherk-design/
└── skills/scroll-craft/
    ├── SKILL.md            the procedure: brief, grammar, score, build, verify
    ├── references/
    │   ├── approved-collection.md  the ten-site workflow and worked examples
    │   ├── hero-depth.md   independent planes, contact anchors, mobile composition
    │   ├── uniqueness.md   eight page grammars, the signature move, the fingerprint gate
    │   ├── feel.md         the feeling curve, the engineered peak, the feel check
    │   ├── devices.md      nine scroll devices and the cue contract
    │   ├── worldflight.md  continuous-world mode: one fixed stage, no seams
    │   ├── worlds.md       art direction, and the style-preamble method
    │   ├── taste.md        the design floor: spacing, type, colour, depth, motion
    │   ├── assets.md       generation, camera moves, encoding for scrubbing
    │   ├── verify.md       the harness, and what it cannot tell you
    │   └── template.html   a starting skeleton, not a layout
    ├── engine/             scrollcraft.js + .css. The mechanism, never edited per project
    ├── templates/          the empty registry a new workspace is seeded from
    └── scripts/            doctor · workspace · kie · encode · serve · shoot · worldflight-assert
```

[`CHANGELOG.md`](plugins/nateherk-design/skills/scroll-craft/CHANGELOG.md) is worth reading on its own: it records what broke on each build and the rule that came out of it, rather than a feature list.

## The one rule that matters most

The engine is the mechanism and it is **never edited per project**. Theme it with six colour tokens and two fonts, write your own semantic HTML, and drive anything bespoke off the `--sc-p` custom property the engine publishes. A runtime that builds the page from a config object is exactly why every site built on one looks the same.

## Honest limitations

- **Only ever run on Windows.** The scripts look for ffmpeg and Chrome in Windows, macOS and Linux locations, but no build has been done on a Mac. `SCROLLCRAFT_FFMPEG` and `SCROLLCRAFT_CHROME` override the search.
- **Generated video is not free.** A ten-leg continuous-world flight is a real spend. A page built from your own assets costs nothing.
- **It is opinionated on purpose.** It will refuse the layouts and palettes that make AI pages recognisable, and it will argue with you about your peak. If you want a page that looks like everything else, this is the wrong tool.

## Licence

MIT. See [LICENSE](LICENSE).

---
## SKILL.md

---
name: scroll-craft
description: >
  Build premium scroll-driven landing pages for service, product, food, and
  drink brands. Plan the visitor journey, page grammar, emotional peak, and
  bespoke signature move. Create dimensional heroes with independent visual
  planes, restrained motion, and separate mobile composition. Use supplied
  photos and footage or generate photoreal assets through kie.ai, write semantic
  HTML, and verify desktop, mobile, and reduced-motion scroll states visually.
  Use for "scrollcraft", "scroll craft", "layered hero", "premium hero",
  "cinematic hero", "scrollytelling", "scroll animation site", "a site where
  scrolling plays a video", "Apple-style landing page", "3D scroll world",
  "interactive landing page", "make my brand a scroll experience", "this looks
  like a template", or requests for a distinctive website that feels like an
  experience rather than a document.
allowed-tools: Bash, Read, Write, Edit, Glob, Grep, AskUserQuestion
---

# scroll-craft

Scroll is the only input every visitor already knows how to use. This skill
treats it as a timeline: the wheel is a scrubber, the page is a film with real
text on top, and each section behaves differently enough that the visitor keeps
going to find out what the next one does.

**What you produce:** an interview brief, a page grammar, a customer-journey map,
a feeling curve with one engineered peak, a scroll score, one signature move,
generated assets, one real HTML page on a token-driven design floor, and a strip
of screenshots proving it holds up at every scroll position.

## What this is not

It is not "generate a flythrough and drop text on it." That approach produces
one device applied to a whole page, and every site built that way is
recognisable at a glance: same claymation diorama, same centred copy, same
`01 / 06` counter, same "scroll to explore" nudge. Five sections that behave
identically are one section shown five times.

Four rules follow from that, and they are the spine of this skill:

1. **Variety is the product.** A page uses at least four device families and
   never the same device twice in a row. Read [references/devices.md](references/devices.md).
2. **The world is photographic unless the brand is genuinely illustrated.**
   Soft matte low-poly clay diorama is banned as a default. Read
   [references/worlds.md](references/worlds.md).
3. **No continuous chain.** A single unbroken camera flight is the most
   expensive and most fragile thing you can build, and it exists only to hide
   cuts between scenes. Vary the device instead and the cut disappears for free,
   because the visitor is not watching one film. Chain only when the brief is
   literally "one continuous journey."
4. **A different world is not a different page.** The device kit varies how a
   page looks. Structure is a separate axis, and it has to be decided
   deliberately or every build inherits the same skeleton. The first four builds
   did exactly that. Read [references/uniqueness.md](references/uniqueness.md).

## Standing hero preference

**Dimensional layering is a baseline requirement for a premium marketing hero.**
Plan independently moving background, subject, foreground, and atmospheric
planes before requesting assets. A beautiful single background with text fades
does not satisfy this preference. Depth must come from visible separation,
occlusion, and controlled differences in movement, while the headline stays
readable and the scene tells one clear story.

Read [references/hero-depth.md](references/hero-depth.md) before planning the hero
or generating its assets. It covers clean plates, genuine alpha cutouts, shared
contact anchors, typography between planes, restrained scroll choreography,
mobile art direction, and visual acceptance, with the approved Sonder example.
Use the composition that fits the chosen grammar; do not repeat Sonder's scene
or impose pinning on a grammar that forbids it. Honor explicit static or simpler
directions, and keep depth in the static composition when motion is reduced.

## Approved collection standard

Read [references/approved-collection.md](references/approved-collection.md)
before planning a premium marketing site. It distills the approved ten-site
rebuild: real brand research, layer contracts, meaningful pointer/scroll depth,
appropriate photographic or 3D rendering, distinct navigation and endings,
useful interactions, mobile art direction, and evidence from the final package.
These are execution principles and worked examples, not a reusable page skeleton.

## Step 0: The brief and creative authority

**Establish the brief before generating anything.** Reuse answers and assets
already provided. When the user explicitly delegates creative direction (for
example, "use your judgment" or "I want to get out of your way"), write a
`Self-authored under explicit creative delegation` brief and proceed. Cover the
eight topics below, distinguish evidence from assumptions, and do not invent
user quotations or force another interview/approval checkpoint.

Otherwise, interview for the missing decisions. Ask actual questions, record
the answers, and avoid inferring an entire brand from its name.

The skill is a range instrument, not a house style. The human brings intent and
whatever assets they own; the interview is where that turns into the right kind
of page: one unbroken world, distinct scenes, printed chapters, a live surface.
The skill can do any of them. The interview decides which.

Keep it short. Eight questions, asked in one pass:

1. **Vibe in three to five words**, plus up to three references from any medium.
   A film, an album cover, a shop, a magazine, a game. Not "sites you like":
   naming sites is how a page ends up looking like an existing site.
2. **The scroll journey, section by section, in their words.** What the visitor
   should hit first, what comes next, what the last thing is. Their sequence,
   not a menu you offered.
3. **The energy curve.** Where it should feel calm, where it should feel
   intense. A page that is loud the whole way is as flat as one that is quiet
   the whole way.
4. **How should someone feel while scrolling, stage by stage, and what is the
   ONE moment they should remember?** Energy is loudness. This is emotion, and
   the two do not line up: on a loud page the quiet act can be the most intense.
   The stage-by-stage answer becomes the feeling curve, the one moment becomes
   the peak. Both are required in BRIEF.md. See [references/feel.md](references/feel.md).
5. **One thing this site should do that no site they have seen does.** This is
   the seed of the signature move. Push for a real answer; "be memorable" is not
   one.
6. **How far from premium-minimal they want to go.** Offer the range in
   [uniqueness.md §5](references/uniqueness.md): brutalist, maximalist, playful,
   retro, dense, editorial, premium-minimal. Their answer governs the aesthetic
   family, not your taste.
7. **One unbroken world, or distinct scenes?** Should the whole page feel like
   one continuous place the scroll flies through (worldflight, see
   [references/worldflight.md](references/worldflight.md)), or like separate
   scenes, chapters, or cuts? This is the single biggest structural fork, and it
   is their call, not a device you pick later. Offer both plainly; neither is
   the default.
8. **What assets do they already have?** Footage, photos, product shots, a
   brand kit, clips of themselves. Real assets anchor the world and cut
   generation cost; the answer decides what gets graded and encoded versus
   generated. "Nothing" is a fine answer and means a fully generated world.

Write the answers into `<workspace>/builds/<name>/BRIEF.md` before any act planning, in
their words, not paraphrased into marketing prose. Everything downstream reads
from that file.

BRIEF.md must contain, at minimum:

- The eight topics, with verbatim user answers where supplied and clearly labeled authored decisions where delegated.
- **The feeling curve.** One line per act: the emotion, then what on screen
  causes it. Written before the acts exist, added to as the score fills in.
- **The peak.** The one moment, written as the sentence a visitor would say to
  a friend, plus which act it lives in.
- **The completed tell-someone sentence.** "It's the site where ___", filled
  with an experience, not a device name.
- Any authored silence, so the verification pass can tell it from dead scroll.

[references/feel.md](references/feel.md) is the spec for all four.

**If the human is genuinely unreachable** and the run is fully autonomous, write
BRIEF.md yourself: answer all eight questions in the brand's voice, mark the file
`Self-authored, not interviewed` at the top, and say so in the final report. A
self-authored brief without delegation is a fallback. Explicit delegation above
is a normal supported workflow and does not require the human to be unreachable.

## Bootstrap

Environment, not a stage of the work. Do it once the interview is answered and
before Step 1.

**Run the preflight rather than checking by hand.** It knows the failure modes
that otherwise surface later as misleading errors, chiefly a stripped ffmpeg
that reports a missing filter as a syntax error in your command:

```bash
node <skill>/scripts/doctor.mjs
```

It reports node, a full ffmpeg build, playwright and Chrome, the API key, and
the resolved workspace. Required failures exit non-zero. Say plainly which items
are missing rather than working around them silently.

### The workspace

Builds and the fingerprint registry live in one directory, and **it is resolved,
never assumed**:

```bash
node <skill>/scripts/workspace.mjs --ensure     # prints it, creates it, seeds the registry
```

Resolution order, first hit wins:

1. `SCROLLCRAFT_HOME`
2. the nearest `.scrollcraft.json` walking up from the cwd, `{ "workspace": "..." }`
3. `<project root>/scrollcraft`, where the project root is the nearest ancestor
   holding a `.git`

So a build folder is `<workspace>/builds/<name>/` and the registry is
`<workspace>/FINGERPRINTS.md`. The registry starts **empty**: the gate exists to
stop you repeating yourself, so your first build has nothing to clear.

If you already keep builds somewhere else, drop a `.scrollcraft.json` at your
project root pointing at it and nothing moves.

### The rest

1. `KIE_AI_API_KEY`, **only if you are generating assets.** A build from the
   user's own photos and footage needs no key and no spend, and that is a
   first-class route, not a fallback. Confirm balance with
   `node <skill>/scripts/kie.mjs probe`. A still costs cents and a 5s clip costs
   more; a six-act page with two clips is a small spend, not a large one.
2. A brand kit if one exists (colours, logo, type, existing product shots). If
   the brand has a folder in this repo, read it before generating anything, and
   obey its hard rules. A brand that forbids invented numbers means no stat
   counters, however good they look.

Copy `engine/scrollcraft.js` and `engine/scrollcraft.css` into the build folder.
Never edit the engine per-project; it is the mechanism. Theme it with tokens and
write your own markup.

## Step 1: The brief, journey first

The subject is the user's to state. Ask it open, in plain prose, never as a
fabricated multiple-choice list of industries: a made-up menu biases them and
reads as you deciding their business for them.

Step 0 already covered vibe, sequence, energy and range. Do not ask any of it
again. Ask only what you cannot sensibly default:

1. **What is this, and who is it for?** One or two sentences in their words.
2. **What must the visitor believe by the end?** The single sentence the page
   exists to install. Not a feature list. If they give three, make them pick.
3. **What does the visitor do next?** One action. One label for it, used
   everywhere on the page.
4. **What do you already have?** Logo, palette, photography, product shots,
   footage, a brand doc. Real assets beat generated ones every time.
5. **Art direction**: offer the worlds in [references/worlds.md](references/worlds.md)
   as a real choice, and say they can go their own way.

Then write the **journey** before anything else: four to seven beats, each one a
shift in what the visitor knows or feels.

```
1  Recognition   they see their own morning
2  Tension       the cost of it, named plainly
3  Turn          the thing that changes
4  Substance     why it holds up
5  Range         what they can choose
6  Commitment    the one action
```

Beats are the spine. Sections serve beats; a section that serves no beat is cut,
however nice the shot is. Resolve the journey before generating assets. Show it
to the user when their decisions are needed; under explicit creative delegation,
record the chosen journey and proceed within the authorized scope.

## Step 2: Grammar, gate, then score

Three things in order, and the first two come before any act planning. Full
detail in [references/uniqueness.md](references/uniqueness.md).

**Pick a grammar.** Start with the eight defined grammars and their constraints.
A new grammar is allowed when its navigation, sequence, ending, and explicit
bans describe a different structure; a new label alone earns no credit. Filmic one-shot is the one the first four
builds all used, so choosing it again means saying in the report why the other
seven did not fit the interview. Nav, hero and close all follow from the
grammar; they are not decided separately.

**Invent the signature move.** One bespoke interaction that lives on this site
alone, coded in the page, not a parameter change to a kit device. Question 5 of
the interview is the seed. The engine stays untouched.

**Run the fingerprint gate.** Read your registry at
`<workspace>/FINGERPRINTS.md` (see **The workspace** in Bootstrap; run
`node <skill>/scripts/workspace.mjs` to print the path). The planned build must differ
from **every** existing row on at least 4 of 6 dimensions: grammar, nav
treatment, hero device, act-sequence shape, close pattern, signature move. Four
against each row individually. If it fails, change the plan, not the log.

**Write the feeling curve before the score table.** One line per act: the
emotion, then what causes it. Curve first, acts second, because a device chosen
before the feeling is a device looking for a reason. Two adjacent acts with the
same feeling means one is filler, and it is cheaper to cut it here than after
the assets exist. Name the peak in the same pass and give it the largest span on
the page. Full method in [references/feel.md](references/feel.md).

Then assign each beat a device. Do it deliberately and write it down as a table:

| Beat | Device | Why this one |
|---|---|---|
| Recognition | `scrub` | The camera moving under the reader's own hand is the strongest possible open |
| Tension | `pin` + kinetic | Copy assembles line by line while the frame holds still |
| Turn | `reveal` | A wipe is a change of state, which is what this beat is |
| Substance | `scrub` (macro) | Texture at a scale the eye cannot get otherwise |
| Range | `pan` | Lateral travel reads as "options", vertical reads as "argument" |
| Commitment | `pin` + pointer | The page stops moving and starts responding |

That table is a **filmic** score. It is the right shape for one grammar and the
wrong shape for the other seven, so read your grammar's leans-on and bans list
before filling in a row.

Checks before you build:

- The grammar's bans hold. A grammar that forbids `pin` forbids it here too,
  however well it would have worked.
- Four or more distinct device families. Fewer means the page has one idea.
- No device family twice in a row.
- At most two `scrub` acts. Video is the heaviest thing on the page, and the
  third one stops being a surprise.
- No two adjacent acts carry the same feeling. If they do, one is filler.
- One act is the peak and it has the largest span by a visible margin. The act
  before it is quieter than it is.
- Every act earns its scroll span. Eight to fourteen viewport-heights is a
  pacing reference for longer cinematic pages, not a quota. Shorter editorial,
  gallery, or working-surface grammars should stay short when the journey is
  complete. Never add filler or empty pinning to hit a length target.
- The act count and total length do not land in the 6-to-7 acts at 13.6-13.8vh
  band that all four prior builds hit. That band is a fingerprint dimension now.

## Step 3: Generate the assets

Full pipeline, prompt scaffolds and model notes: [references/assets.md](references/assets.md).

Short version:

```bash
node <skill>/scripts/kie.mjs still "<style preamble>\n\n<scene>" out/01-hero.png --ar 16:9 [--ref brand-can.png]
node <skill>/scripts/kie.mjs shot  "<camera move>" out/01-hero.png out/01.mp4 --dur 5
bash  <skill>/scripts/encode.sh out/01.mp4 assets/01.mp4
bash  <skill>/scripts/encode.sh out/01.mp4 assets/01-m.mp4 mobile
```

Four things that decide whether this looks premium or generated:

- **One style preamble, reused verbatim in every prompt.** This is what makes
  six separate images look like one shoot. Write it once, never paraphrase it.
- **Look at every asset before you use it.** Read the PNG. Generation is cheap
  and rerolling is cheaper than shipping a bad frame.
- **Encode for scrubbing, not playback.** `encode.sh` sets a dense GOP because
  seeking walks from the previous keyframe. A normal web encode plays perfectly
  and scrubs like mud.
- **Layer the hero.** Cut the scene into planes that scroll at slightly
  different rates, with the product parked between the mid and foreground
  planes. Depth from differential movement is the cheapest premium signal on
  the page. The recipe and its traps are under `parallax` in
  [references/devices.md](references/devices.md).

## Step 4: Build the page

Write real HTML. Real `<h1>`, real `<p>`, real links, real reading order. The
engine reads `data-sc-*` attributes off your markup and drives it; it never
generates DOM. A runtime that builds the page from a config object is exactly
why every site built on one looks the same.

Start from `references/template.html`. The device patterns are in
[references/devices.md](references/devices.md); the spacing, type, depth and
colour rules are in [references/taste.md](references/taste.md). Read taste.md
before writing markup, not after, and build without announcing the checklist.

Theme by overriding tokens, six values and two fonts:

```css
:root {
  --sc-canvas: #0A0806;  --sc-surface: #16110E;
  --sc-ink:    #F5EBDD;  --sc-ink-soft: #A2968A;
  --sc-accent: #FF5A3D;  --sc-accent-ink: #15110F;
  --sc-font-display: "Archivo", system-ui, sans-serif;
  --sc-font-text:    "Geist", system-ui, sans-serif;
}
```

## Step 5: Verify by scrolling it

Apply the final-source and deployment checks in
[approved-collection.md §7](references/approved-collection.md#7-require-visual-and-functional-evidence-before-delivery).
Use safe headless automation with native pointer lock/capture disabled in every
context. Inspect actual pixels, phone compositions, and intermediate states;
test useful controls, downloads, and form outcomes in the final package.

Not optional, and not "it should work." A scroll page has no single state:
every position is a different frame, and the failures live between the two you
happened to look at. Full procedure: [references/verify.md](references/verify.md).

```bash
cd <build project> && npm i playwright-core     # once
node <skill>/scripts/serve.mjs --root . --port 4500 &
node <skill>/scripts/shoot.mjs --url http://localhost:4500 --out lab/shots
node <skill>/scripts/shoot.mjs --url http://localhost:4500 --out lab/mobile --width 390 --height 844
node <skill>/scripts/shoot.mjs --url http://localhost:4500 --out lab/reduced --reduced-motion
```

The harness walks each act at six positions, waits for the scrub video to
actually settle, and reports **dead scroll**, **cues that never reach full
opacity**, and **contrast measured on the composited page at the brightest
frame under each line**. It writes a contact sheet.

Then do the part the harness cannot: **read `sheet.png`.** It proves a clip
advances; it cannot tell you the composition is good, the motion is smooth, or
the page means anything. Also tab through for focus order.

**Then run the feel check** ([references/feel.md §6](references/feel.md)). Scroll
the page cold, write one word per act for what you felt, and only then open
BRIEF.md and diff it against the intended curve. Where they disagree the page is
wrong, not the brief. Confirm on the sheet that the peak is the largest visual
change and holds the most scroll room, and that the last screen resolves instead
of fading to nothing.

**And say what a green run does not cover: a real phone.** Headless Chrome
cannot reproduce an iPhone's video decoder, autoplay policy, Low Power Mode,
or touch scrolling; a build once shipped four green rounds while the hero clip
sat frozen on the actual device. Mobile is a first-class target throughout,
not a pass at the end: portrait phone clips, touch-tuned lerp, grown tap
targets are all authored (see assets.md and verify.md). When any mobile defect
is reported, deploy `references/device-diag.html` beside the site on the
**first** round and let the device answer, rather than theorising from a
machine that cannot reproduce the failure. The full iOS clip-lifecycle notes
live in verify.md, "The phone is a different machine". To open the page on a
phone over Wi-Fi, start the server with `--lan` (by default it binds to this
machine only), and stop it when you are done: `--lan` makes everything under
`--root` readable by anyone on that network.

Fix what you found and shoot it again. Report what you actually verified and
what you did not.

## Hard rules

Ship-blockers, not preferences. Each one is a thing that makes a page read as
machine-made.

| Never | Instead |
|---|---|
| Clay diorama / low-poly / claymation as the default world | Photographic. See worlds.md |
| A "scroll" cue, arrow, or animated mouse icon | Nothing. They are looking at the hero; they know |
| `01 / 06` section counters | Delete them. Sequence is not information here |
| An eyebrow above every section heading | At most one per three sections. The heading carries itself |
| Em dash anywhere visible | Period, comma, colon, or parentheses |
| Centred copy in every act | Vary the anchor: lead, trail, centre, split |
| The same device twice in a row | Score the journey properly in Step 2 |
| Generating before the brief and creative authority are clear | Run Step 0. Record actual answers or explicitly delegated authored decisions in `BRIEF.md` |
| A page with no engineered peak, or with three competing ones | One peak. It gets the asset budget, the silence before it, and the most scroll room. See feel.md §2 |
| An ending that trails off, fades out, or just becomes a footer | The close resolves and holds. The last feeling is the one they carry |
| Planning acts before the feeling curve exists | Curve first, devices second. See feel.md §1 |
| Shipping without one bespoke signature move | Invent one. A recoloured spotlight or a retuned tilt is not one. See uniqueness.md §3 |
| A build that clears fewer than 4 of 6 fingerprint dimensions against any existing row | Change the plan, not `FINGERPRINTS.md` |
| Editing the engine to get a bespoke behaviour | Bespoke JS in the page, driven off `--sc-p` and your own `data-sc-*` |
| Reaching for filmic one-shot because it is what the last build did | Pick from all eight grammars, and say why the other seven lost |
| A full-frame dark overlay to fix contrast | A scrim only where the text sits |
| Text baked into a generated image | Real markup, always. It is selectable, translatable and sharp |
| Invented statistics in a counter | Only real numbers. No number, no counter |
| `transition: all`, or animating width/height/top/left | `transform` and `opacity`; `clip-path` for wipes |
| Gradient text, neon glow, zero-offset coloured halo shadows | Weight and size for emphasis; shadows with offset and blur |
| Autoplaying audio, or any audio at all on a scrub clip | Strip the track. `encode.sh` already does |
| Shipping without running Step 5 | Run Step 5 |

## Output

The build folder, including `BRIEF.md`, then a short report: the grammar and why
the other seven lost, the signature move, the fingerprint gate result against
each existing row, the journey, the feeling curve and the peak, the feel-check
diff (intended curve against felt curve, and what you changed), the score table
(device per beat), what you
generated, what you verified with screenshots, and anything you could not
verify. Say if the brief was self-authored rather than interviewed. Give the
local URL. Keep it brief; the page is the deliverable.

Then append the build's row to `<workspace>/FINGERPRINTS.md`.

Changes to the skill itself, and the build findings that drove them, are logged
in [CHANGELOG.md](CHANGELOG.md).

---
## references/taste.md

# The taste floor

Read this before writing markup, not after. Build without announcing the
checklist.

Everything here is a check on the **rendered result**, not on intention. "I used
a spacing scale" is not evidence; a computed value is.

---

## Spacing

Rhythm comes from the contrast between tight and generous, never from one value
repeated until everything weighs the same. If you can't point at which intervals
are the tight ones and which are the breaks, the page has no rhythm.

- Use the 4px-base scale (`--sc-1` … `--sc-11`). A 4-base gives the useful
  middle steps an 8-only scale misses.
- **More space above a heading than below it.** The gap belongs to the boundary
  between sections, not to the heading-and-body pair. Getting this backwards is
  the single most common spacing error, and it makes a page read as a list.
- Section padding is fluid (`--sc-section`). A phone should not inherit desktop
  air; 8rem of padding on a 375px screen is a scroll tax.
- Group by proximity before reaching for a container. If you added a border to
  show two things are related, the spacing was wrong first.
- Gutters scale with viewport (`--sc-gutter`). Full-bleed media goes edge to
  edge; text never does.

**Optical, not mathematical.** Equal computed padding around a shape with
uneven visual weight looks wrong. Correct against the render, not the number.

---

## Typography

- **Two families maximum.** Display carries voice, text carries prose. A third
  is a costume.
- **Tracking tightens as size grows.** A face set at 6rem with default tracking
  reads loose and amateur. The ramp handles this: `--sc-track-tight` on display,
  `--sc-track-normal` on body. This is optical correction, not decoration.
- **Body measure 45 to 75ch.** `--sc-measure` is 62ch. A full-width paragraph on
  a 1600px monitor is unreadable regardless of font size.
- **Line height inverse to measure.** Wider lines need more leading. Display at
  0.94 to 1.06, body at 1.6.
- **Light text on dark needs compensation on three axes**: slightly more line
  height, a touch more tracking, one step more weight. Dark-mode type set with
  light-mode metrics looks thin and blurry, and this is why.
- `text-wrap: balance` on headings, `pretty` on body. Free, and it removes the
  orphan word that makes a headline look accidental.
- Display max ~6rem outside a genuine hero moment. Bigger is not more confident.
- **Step the hero down one rung below ~700px.** `--sc-t-4xl` floors at 3.4rem,
  which is a *desktop* floor: at 390px it wraps a normal hero headline to six
  lines. `--sc-t-2xl` on the hero inside a phone media query fixes it. The
  portrait crop of the image is covered in assets.md; this is the portrait crop
  of the type, and it is missed more often.

**Font choice.** Inter is discouraged as a default: it is the most-used face in
AI-generated pages and it reads as a non-decision. Reach first for Geist,
Archivo, Outfit, Satoshi, Cabinet Grotesk, or the brand's own face. Inter is
correct when the brand asks for neutral, or when accessibility is the brief.

**Serif is not a synonym for premium.** "It feels editorial" is not a reason.
Use one only when the brand names it, or when the work is genuinely editorial,
luxury, or heritage and you can say why *this* serif fits *this* brand.

**Emphasis inside a headline** uses italic or bold of the same family. Dropping
a serif word into a sans headline for visual interest is amateur.

---

## Colour

- **Six roles, one accent.** Canvas, surface, ink, ink-soft, accent, accent-ink.
  The accent owns a region or a role; scattered tiny accents are confetti.
- **Lock the accent for the whole page.** A warm-grey site does not grow a blue
  CTA in section seven. **The one exception is a page that hard-cuts between
  light and dark grounds**, which physically cannot clear 4.5:1 on both with a
  single stop. That page carries a two-stop accent: one hue, two lightnesses,
  keyed to the ground family, redefined per section alongside the ink. Still one
  accent per ground, and still one hue for the page. Two different hues is not
  what this licenses.
- **Secondary text is tinted, never flat gray.** Derive it from the foreground
  or surface hue. `#888` on a warm dark ground looks dirty.
- **No pure black.** `#000` has no air in it. Off-black at minimum.
- Contrast, measured on the render: body ≥4.5:1, large text ≥3:1, controls and
  focus indicators ≥3:1.
- Drift keeps the whole page in one theme family. See devices.md §10.

**Redefining `--sc-ink` on a subtree does not re-ink the text under it.**
`color` is inherited as a *computed value*, so text whose `color` already
resolved on `<body>` keeps the body's ink no matter what the section redefines
the token to. Every page that inverts a ground mid-page hits this, and it fails
silently: an inverted section renders bone type on concrete at 1.15:1 while the
harness correctly classifies the line as light-on-dark and grades it in the
wrong direction. The fix is one declaration on the same subtree:

```css
.section--light { --sc-ink: #14110C; --sc-ink-soft: #4A443A; color: var(--sc-ink); }
```

Restate `color` wherever you restate the token. The same applies to any other
inherited property you drive from a token on a subtree.

**The premium-consumer palette trap.** Warm cream background, brass or clay
accent, espresso near-black text is the default reach for every artisan, food,
wellness and craft brief, and it makes every such brand look identical. Do not
default to it. Rotate: cold silver and chrome; deep forest with bone and amber;
true off-black with warm tan; cobalt against a single neutral; olive with brick.
Use cream-and-brass only when the brand names those colours.

**The AI-purple trap.** Violet-to-blue gradients, neon glow, glowing buttons.
Not unless the brand asks.

---

## Text over media

"No full-frame overlay" is the rule. Here is what to do instead, because the
rule on its own sends people to a slightly weaker full-frame overlay.

There are three shapes, and which one is right depends only on where the copy is:

1. **A corner** of density, sized to the copy block. `.sc-scrim--lead` /
   `.sc-scrim--trail`. Right when the copy is anchored to a corner on a wide
   screen. An edge gradient has to darken a whole band across the frame to cover
   one corner; a corner gradient puts the density where the text is and leaves
   the photograph alone.
2. **A band**, `.sc-scrim--band`, transparent above roughly 58%. Right whenever
   the copy spans the full width of the frame, which is what *both* corner
   anchors become below 860px. The engine already switches `.sc-scrim--trail` to
   a band there for exactly that reason.
3. **A column** of density under a text column, on an act where the copy holds
   one side of a full-bleed image. Leaves the other half of the frame untouched.

**`width` and `height` attributes are presentational hints, and they come in
pairs.** The reference template ships every `<img>` with both, correctly, because
they reserve the aspect ratio and stop the page reflowing as media arrives. The
trap is that overriding only one of them in CSS leaves the other resolving to the
attribute's raw pixel value, so `width: 100%` on a 1920x1080 image inside a
narrow column renders it 1080px tall and pushes everything under it off the fold.
It looks like a layout bug three elements away from its cause. **Override both or
neither**, usually `width: 100%; height: auto`, or an explicit height plus
`object-fit: cover` when the frame's shape is the design.

And the positive case behind all three: when a photographic ground sits behind a
text column, **mask the image away from the text** rather than laying anything
over it. A `mask-image` or a clip that ends where the column begins gives the
type a clean ground and gives the photograph its full contrast back, and it is
better than any scrim.

**A scrim must not be a child of the text it protects.** The verification pass
hides the copy element and everything inside it to photograph the frame
underneath, so a `::before` on the copy block is hidden too and the scrim is
never measured. Put it in a sibling element. See verify.md.

Then measure it. A scrim tuned by eye is routinely 9:1 where 4.5:1 was needed,
which is a photograph thrown away for nothing, or 2.8:1 on the one frame the
clip brightens under the copy. Both are invisible until the harness reports the
number.

---

## Depth

Depth is the axis that separates a premium page from a styled document, and it
is not one property. Five tools, used together:

1. **Shadow with offset and blur.** Real raised things cast light downward.
   A zero-offset coloured halo is decoration, not depth. Tint the shadow to the
   canvas hue; pure black shadows on a coloured ground look like dirt.
2. **Edge light.** A 1px top highlight (`--sc-edge`) sells a raised surface
   better than any amount of blur, because real lips catch light.
3. **Scale and blur as distance.** Things further away are smaller, softer, and
   lower contrast. Parallax without those reads as sliding, not depth.
4. **Overlap.** One element crossing another's boundary establishes more depth
   than any shadow. Free, and underused.
5. **Grain.** A flat dark ground bands on real displays. `.sc-grain` at 4-5%
   opacity is the difference between "a dark page" and "a lit room".

Three elevation steps (`--sc-e1/2/3`) and no more. If everything is elevated,
nothing is.

---

## Cards

Cards are the lazy container. Before using one, ask what it is doing that
proximity, a hairline, or space could not.

- **Never a grid of identical icon + heading + text cards as the page
  structure.** It is the most recognisable AI-page tell there is.
- **Never nest cards.**
- **Never three equal columns of feature cards.** Use an asymmetric grid, a
  two-column zigzag (max two in a row), a rail, or plain type on space.
- If a multi-cell grid has an empty trailing cell, the grid was planned wrong.
  Reshape it; do not paste a blank tile.
- Pick one corner-radius scale and hold it across the page. Pill buttons on a
  square-card page is broken, not eclectic.

---

## Motion

The scroll devices are the page's motion. Everything else is small and fast.

- `transform` and `opacity` only for anything continuous. `clip-path` is the
  sanctioned third for wipes. Never animate width, height, margin, padding, top
  or left, and never `transition: all`.
- **Never `ease-in` on UI.** It delays the moment the eye is already on.
  `ease-out` at 200ms feels faster than `ease-in` at 200ms.
- Built-in CSS easings are too weak. Use `--sc-ease-out`
  (`cubic-bezier(0.23, 1, 0.32, 1)`).
- **UI transitions under 300ms.** Hover 120-180ms, buttons 100-160ms. Scroll
  devices are exempt: they are paced by the hand, not by a duration.
- **Never `scale(0)`.** Enter from `scale(0.95)` + `opacity: 0`. Nothing in the
  real world appears from nothing.
- Press feedback on anything pressable: `scale(0.97)` or `translateY(1px)`.
- Stagger group entrances 30 to 80ms. Longer feels slow.
- Gate hover motion to `(hover: hover) and (pointer: fine)`; touch fires false
  hovers on tap.
- Reduced motion means **fewer and gentler, not zero**. Keep the opacity that
  carries comprehension, drop every position change.

---

## States and content

- Every interactive element gets hover, focus-visible, active and disabled.
  A page with only the resting state is half-built.
- **Focus-visible must be visible.** Themed to the accent, with offset.
- **Button text fits on one line at desktop.** A wrapped CTA is broken. Primary
  CTA labels are one to three words.
- **One label per intent.** "Get in touch" in the nav and "Let's talk" in the
  footer are the same button with two names. Pick one and use it everywhere.
- **Check button contrast.** White text on a light button, or a ghost button on
  a photo with no scrim, fails.
- Real copy, not lorem. Real names, not "John Doe". Real numbers or no numbers.
- **No invented statistics.** Fake precision (`4.1×`, `92%`, `48k`) is a legal
  and credibility liability, not a design element.

---

## Browser surfaces

The parts you did not draw still carry the design, and this is the cheapest
signal that a page was built rather than assembled. It is also the step that
gets skipped most reliably. `scrollcraft.css` themes all of these; if you fork
it, keep them:

selection colour, caret colour, focus ring, scrollbar, underline offset and
thickness, tabular numerals in anything that counts or tabulates.

---

## The refuse list

Category defaults, not bans on principle. The brief's own words can earn any of
them; reaching for one when the axis is free means you were not deciding.

**Structure**
- Identical cards as page structure. Nested cards. Three equal feature columns.
- The hero-metric template: big number, small label, supporting stats, accent.
- More than two consecutive image-left / text-right zigzag sections.
- The same layout family twice on one page.
- A split header: giant headline left, small explainer paragraph floating right.

**Labels**
- An eyebrow above every section heading. At most one per three sections.
- Section numbers (`01 / 06`, `002 · Capabilities`) unless the sequence itself
  is information the reader needs.
- Scroll cues: "scroll", "↓ scroll", "scroll to explore", animated mouse icons.
  They are looking at the hero. They know.
- Decoration text strips (`BRAND. MOTION. SPATIAL.`) across the hero bottom.
- Locale, time and weather strips unless the brand is genuinely about a place.
- Pills and tags overlaid on photos. Version stamps on a marketing page.

**Surface**
- Gradient text. Neon and outer glows. Hard offset zero-blur shadows outside a
  world that is actually neobrutalist.
- Glass and blur as decoration rather than as a specific effect.
- Coloured `border-left` above 1px on cards, callouts or list items.
- Monospace as a costume for "technical" rather than for code, data, or labels.
- Emoji standing in for an icon system. Use a real icon library.
- Custom cursors.

**Content**
- Em dash anywhere visible. Period, comma, colon, or parentheses.
- Div-built fake screenshots, fake dashboards, fake terminals.
- Text baked into a generated image. Real markup, always.
- Filler verbs: elevate, seamless, unleash, next-gen, revolutionize, supercharge.
- A hero that overflows the viewport. Headline max two lines, subtext max 20
  words, CTA visible without scrolling.
- More than four text elements in the hero. Trust logos, pricing teasers and
  micro-taglines move to their own section below it.

---

## The squint test

Blur the page until detail is gone. You should still be able to name the
primary element, the secondary element, and the major groups, in that order.

If everything greys into one even field, the problem is hierarchy, and no amount
of shadow, gradient or motion will fix it.

---
## references/feel.md

# The emotion axis

A page is not sections. It is a sequence of states a person passes through with
their hand on a wheel. The device kit decides how a page looks, the grammar
decides what a page is, and this file decides what it does to somebody.

Design the feeling before the acts. An act list written first will always be a
list of things that happen, and a page of things happening is a page nobody can
describe afterwards.

Read this after the interview, alongside [uniqueness.md](uniqueness.md), before
the score table in SKILL.md Step 2.

---

## 1. The feeling curve

Write the curve as its own artifact, in BRIEF.md, before a single act exists.
One line per act: the emotion, then the thing on screen that causes it.

The emotion column is the constraint. The cause column is the only place a
device name may appear, and it appears second, because the feeling picks the
device and never the other way round.

Useful states, not a closed list: curiosity, recognition, unease, doubt,
tension, awe, delight, relief, intimacy, confidence, resolve, calm.

**If two adjacent acts produce the same feeling, one of them is filler.** Cut it
or change what it does. Two acts of awe in a row is one act of awe followed by a
reader who has adjusted. Every emotion is defined by what preceded it, which is
why the curve matters more than any single peak: relief needs tension in front
of it, awe needs quiet in front of it, intimacy needs scale in front of it.

The curve also outranks the journey beats from Step 1. Beats say what the
visitor learns. The curve says what they feel while learning it. When they
disagree, the curve wins, because nobody remembers what they learned on a page
that made them feel nothing.

### Worked curve: a canned drink brand

```
1  Recognition   their own kitchen counter at 7am, shot at eye height
2  Fatigue       the two containers, the mess, held still while copy names it
3  Delight       a wipe, and the whole frame is one cold can, condensation running
4  Trust         macro texture at a scale the eye cannot get in a shop
5  Appetite      the flavours travelling sideways, each one landing whole
6  Resolve       everything stops, one can, one line, one place to buy it
```

### Worked curve: an infrastructure product for engineers

```
1  Familiar dread  the alert channel at 3am, real markup, already scrolling
2  Doubt           the log fills and nothing in it explains anything
3  Clarity         one panel resolves the whole trace, the noise falls away
4  Control         the visitor moves a selection and the surface answers
5  Competence      the real numbers arrive on telemetry they can check
6  Readiness       a live input with a cursor in it, not a button
```

### Worked curve: a landscape design-build firm

```
1  Stillness   a garden at dawn, almost nothing moving, held long
2  Longing     copy naming the space they actually have, small and honest
3  Curiosity   the drawing builds itself, survey to plan to planting
4  Weight      material facts as museum labels, stone, cedar, water
5  Warmth      the same garden five years on, people in it
6  Intent      a quiet line of running text, not a CTA island
```

### Worked curve: a live event or festival brand

```
1   Pulse       a cut before the reader has settled, sound implied not played
2   Appetite    faces, close, one per screen, gone
3   Envy        the year before, at speed, twelve cuts in a viewport-height
4   Urgency     the real date and the real capacity, counting
5   Belonging   one held frame, the crowd, the only slow moment on the page
6   Decision    abrupt, full bleed, the ticket line and nothing else
```

Note what the fourth curve does that the others do not: its one slow act is the
peak, because on a page made entirely of cuts, stopping is the loudest thing
available. The peak is defined by contrast with its own page, not by an absolute
amount of spectacle.

---

## 2. The peak

People remember one peak moment and the ending. The middle compresses into a
general impression and then goes. This is the peak-end rule and it is the single
most useful thing known about how anybody experiences a sequence.

So every build engineers **one deliberate peak**. Name it in BRIEF.md as the
sentence a visitor would say to a friend:

> the screen went black and then the whole ocean lit up under me

Not "the hero is impressive". A described moment, with a before and an after.

The peak gets three things, and it gets them at the expense of other acts:

| It gets | Because |
|---|---|
| The asset budget | The best generated frames or the only real footage go here, not to act two |
| The silence before it | An act of quiet, or an empty viewport, so the change has something to be a change from |
| The most scroll room | The largest `data-sc-span` on the page, and the `data-sc-dwell` that makes the camera settle exactly on it |

**A page with three peaks has none.** Three impressive acts flatten each other,
and the visitor leaves able to say the site was nice and unable to say what
happened. If a second act is competing, demote it: shorter span, less asset,
plainer device. Something has to be the biggest thing.

**The ending must resolve.** The last feeling is the one they carry, and a page
that trails off into a footer overwrites everything the peak did. Resolution
means the page arrives somewhere and stops: the divider collapses, the world
lands at a place, the type shrinks to its quietest setting, the surface hands
over an input. The close cue holds (see the cue contract in
[devices.md §2](devices.md)) so the final screen still has something on it. A
closing act that fades to an empty stage is the page apologising for existing.

---

## 3. The tell-someone test

Before building, complete this sentence:

> it's the site where ___

Then look at what filled the blank.

- "it has a scrub video" is a device name. No memory hook yet.
- "the background changes colour" is a device name wearing a description.
- "you dive to the bottom of the ocean and the pressure readout keeps climbing"
  is an experience. That is a hook.
- "you drag the letters of the logo apart and they snap back perfectly" is an
  experience. That is a hook.

The blank has to be something that happened **to the visitor**, phrased from
their side. If the sentence only makes sense to someone who has read the build
folder, it fails.

This sentence goes in BRIEF.md, and the signature move from
[uniqueness.md §3](uniqueness.md) usually lives inside it. If the signature move
and the tell-someone sentence point at different moments, one of them is
decoration. Merge them, or cut the one that is not the peak.

The test is also the fastest fingerprint check available. If the sentence would
be true of an existing build in
`<workspace>/FINGERPRINTS.md`, the page is not new yet.

---

## 4. Being in it, not watching it

A film plays whether you are there or not. The difference between a viewer and a
participant is whether the page acknowledges that somebody specific is here: how
fast they are moving, where their pointer is, whether they stopped.

Concrete techniques, in this skill's vocabulary:

- **Pointer parallax that moves the world, not a card.** `data-sc-spotlight`
  publishes `--sc-mx` / `--sc-my`. Drive a background layer's transform off them
  instead of a highlight, and the environment shifts as the visitor moves,
  slightly, the way a real space does when you lean.
- **Dwell-triggered detail.** Hold still on an act and something further arrives:
  a caption, a second line, a small annotation. Reward for stopping. Read
  `--sc-p` staying constant across a few frames, in the page's own JS, and reveal
  something that was never needed for comprehension.
- **Scroll velocity shaping intensity.** Fast scrolling raises grain, blur,
  chromatic offset, ground saturation. Slow scrolling settles it. The page feels
  like it is being driven rather than played back, and it costs one derived
  custom property.
- **The page addressing "you" at one moment that lands.** Not throughout, which
  is just copywriting. One line, at the emotional turn, in second person, when
  the visitor is already implicated. It works because it is the only time.
- **A trace of where they have been.** Anything that accumulates as they travel,
  so arriving at the end means having a record rather than reaching a footer.

**Embodiment is seasoning. One or two per page.** A page that reacts to
everything feels haunted, not alive: the visitor stops reading and starts
poking, which is the opposite of what any of this is for. Pick the one that
serves the peak and leave the rest.

Everything here is gated to `(hover: hover) and (pointer: fine)` and off under
reduced motion, same as the pointer devices. A technique that only exists on
desktop cannot be the thing that carries the page's meaning.

---

## 5. Pacing as emotion

Scroll distance is emotional time. It is the only clock this medium has, and it
is fully under your control, which makes it the cheapest emotional instrument in
the kit and the one most often left at default.

| Pacing | Reads as | Built with |
|---|---|---|
| Short acts, hard cuts | Adrenaline, pulse, impatience | Acts under 1.4vh, no `pin`, `dwell` at 0 |
| A long pin | Held breath, pressure, attention | `data-sc-span` 3+, overlapping cues, one idea |
| An empty viewport before a reveal | Silence before the drop | A ground-only act, no cue until the next one |
| A slow settle mid-act | The shot landing | `data-sc-dwell` 0.35 to 0.6 with the cue peak on the settle |
| A fast cue with a long plateau | Confidence, arrival | `data-sc-cue="0.1 0.9 0.08 0.4"` |
| A slow ramp in | Hesitation, dawning | Long `rampIn`, and use it once, because it is close to feeling broken |

Three rules follow.

**A continuous world is the exception to pacing variety.** Everything in this
section is about a page of acts, where varying the length is how you vary the
feeling. A worldflight is one camera move, and a camera that changes speed
between legs reads as broken rather than as expressive. There, hold one pace and
let the peak carry the shape by being the single long leg. See worldflight.md
section 7c.

**Give the peak room.** The peak act should have the largest span on the page by
a visible margin. If every act is 2.2vh, the page has no shape, whatever the
curve in BRIEF.md says.

**Compress the administrative parts.** Specs, logistics, FAQ, credentials: these
are information, not experience. Flow sections at short stagger, not pinned acts
with dwell. Spending scroll on them is spending the visitor's patience on the
part they will not remember.

**Silence has to be authored, not left over.** An empty screen you meant reads
as anticipation. An empty screen you did not mean reads as a page that failed to
load, and the harness reports both as dead scroll. If you are using the empty
viewport before the peak, say so in BRIEF.md so the verification pass knows the
difference.

The total-length budget from SKILL.md still holds at 8 to 14 viewport-heights.
Pacing is how that budget is spent, not permission to spend more of it. A page
that needs 20vh to land its curve has too many acts, not too little room.

---

## 6. The feel check

A verification pass, run after the harness in SKILL.md Step 5, against the
contact sheets and a live scroll. The harness measures whether the page works.
This measures whether it does what it was for.

Run it in this order, and do not reread BRIEF.md first. The whole value is in
arriving cold.

1. **Scroll the page top to bottom at a normal reading pace.** Once. No stopping
   to fix things.
2. **Write down what you felt, act by act.** One word per act, before looking at
   anything. If an act produces no word, write nothing for it, because nothing is
   the finding.
3. **Now open BRIEF.md and diff the two curves.**

**Where they disagree, the page is wrong, not the brief.** Rewriting the
intended curve to match what got built is the same failure as rewriting a
fingerprint row: it turns the artifact into a description of the accident.

Then three specific checks:

- **Does the peak read as the peak?** On the contact sheet it should be the
  largest visual change on the page and it should occupy the most scroll room.
  If a different act is the biggest thing on the sheet, that act is the real
  peak and the plan lost. Fix the page or admit the new peak in BRIEF.md and
  give it the budget.
- **Is there silence in front of the peak?** Look at the act before it. If it is
  as loud as the peak, the peak has nothing to arrive from.
- **Does the end resolve?** The last screen should be able to stand still with
  content on it. Blank final frame, a cue that faded out, or a footer that just
  begins means the page ended rather than finished.

Two adjacent acts that produced the same word in step 2 is the filler finding
from §1, caught late. Cutting one is almost always right, and almost always
improves the total length budget at the same time.

Report the diff in the final output: the intended curve, the felt curve, and
what you changed. A build that reports them as identical on the first pass
either got lucky or did not do the check cold.

---
## references/uniqueness.md

# The structure axis

## 1. The template trap

Four sites were built with this skill: a protein coffee brand, a personal brand,
a landscape design-build firm, and an agent observability product. Four
industries, four worlds, one light canvas and three dark. The owner looked at
them side by side and said they felt like a template. He was right, and the
evidence is in the files.

All four open with a full-bleed `scrub` under a fixed minimal top bar carrying a
wordmark and one CTA. All four anchor the hero headline in the lead corner with
a greet cue and kinetic lines. All four run a pinned type act where lines
crossfade. All four hand off to a flow section, pan a card rail with a
`data-sc-tilt="6"` on each card, and close on `data-sc-act="pin"` with
`data-sc-span="1.15"`, `data-sc-spotlight` on the stage and
`data-sc-magnet="0.26"` on the CTA. All four land between 13.6 and 13.8
viewport-heights across 6 or 7 acts with exactly one accent colour.

What actually varied was the order of the middle acts and the palette.

The device kit is an **aesthetic** axis. It changes how a page looks. It has no
opinion on what a page *is*, so every build reached for the same shape, because
the shape was never a decision anybody made.

> The world changes how a page LOOKS. The grammar changes what a page IS.
> A build that only changes world is a re-skin.

This file is the structure axis. Read it after the interview and before the
score table. It has three parts that are not optional: pick a **grammar**,
invent a **signature move**, and pass the **fingerprint gate**.

---

## 2. Page grammars

A grammar is the page's organising logic: what a section is, what the chrome is
for, how the visitor knows where they are, and what the ending is. Two pages in
the same grammar will feel related no matter how far apart their palettes are.
That is the whole finding above.

Each grammar below names what it **forbids**. The forbids are the point. They
are what stops a build drifting back to the filmic default halfway through,
which is what happens when a grammar is a preference instead of a constraint.

Pick one. Do not blend two: a chaptered page with a continuous world underneath
is a filmic one-shot with extra headings.

---

### 2.1 Filmic one-shot

The original skeleton, and now one choice among eight rather than the house
style.

**Fits:** a single linear argument with one emotional arc. Consumer products,
launches, anything where the visitor should feel carried rather than
navigating.

**The scroll feels like:** a film you are pushing through. Continuous, no seams,
each act handing off before the last has left.

**Forbids:** visible sequence (chapter numbers, an index, a progress readout);
hard cuts between grounds; any chrome that implies the page is a tool; more than
one entry point. If the visitor can jump, it is not one shot.

**Nav, hero, close:** fixed minimal bar, wordmark and one CTA. Full-bleed scrub
hero, corner-anchored kinetic headline on a greet cue. Pinned close with a
spotlight and a magnetic CTA.

**Leans on:** `scrub`, `pin`, `drift`, `kinetic`. **Bans:** nothing structural,
which is exactly why it is the default drift and why four builds landed here.

**Use it when the interview earns it, and say in the report why the other seven
did not fit.** This grammar now carries a burden of proof the others do not.

---

### 2.2 Chaptered editorial

The page is a printed feature. Chapters are the unit, not acts.

**Fits:** long-form substance. A method, a manifesto, a founder story, a
research-backed product, anything where the visitor should feel they read
something rather than watched something.

**The scroll feels like:** turning pages. Full-stop intertitles between
chapters, then dense asymmetric spreads. Hard cuts, not crossfades. Each chapter
lands on its own ground and stays there.

**Forbids:** `drift` as a continuous gradient (each chapter is a hard change of
ground, not an interpolation); the full-bleed scrub hero; pinned crossfade type
acts; a magnetic CTA; centred hero copy. Media never bleeds under type here, it
sits in its own column with a caption.

**Nav, hero, close:** no fixed bar. A folio in the margin, chapter number and
title, updating as chapters pass. The hero is a **title page**: type on the
paper ground, no media above the fold, the media starts in chapter one. The
close is a colophon or masthead plate, small type, the CTA set as a line of
running text rather than a button island.

**Leans on:** `flow` + `in`, `reveal` at chapter boundaries, `parallax` inside a
media column, `count` for real figures inside prose. **Bans:** `scrub` beyond
one chapter, `spotlight`, `magnet`.

---

### 2.3 Live surface

The page behaves like the product. Not a screenshot of it, and not a div-built
fake: the actual surface, running, with scroll driving its state.

**Fits:** software, tools, dashboards, editors, anything where the demo is the
argument. If the honest pitch is "watch what it does", this is the grammar.

**The scroll feels like:** operating something. Panels populate, a log fills, a
graph advances, a selection moves. The visitor is inside the thing.

**Forbids:** marketing chrome of any kind. No wordmark-plus-CTA bar, no scrims,
no full-bleed photography, no kinetic headline stacks, no hero claim laid over
footage. Copy lives in the surface's own idiom: labels, tooltips, empty states,
status lines, a help panel. A section heading in 6rem display type breaks this
grammar instantly.

**Nav, hero, close:** app chrome replaces nav. A sidebar, a tab strip, a status
bar, a breadcrumb, whatever the real product would have, and it is real enough
to be the navigation. The hero is the surface already in a state, not a title.
The close is an **actual input**: a command line, a field, a first-run step,
something the visitor puts a cursor in. A magnetic button is the wrong ending
for a page that spent its whole length being a tool.

**Leans on:** `pin` (the surface holds while state advances), `count` on real
telemetry, pointer devices where the real product would have them, and `--sc-p`
driven CSS for anything the kit does not cover. **Bans:** `scrub`, `kinetic`,
`spotlight`, `drift` past two stops.

**The honesty rule.** taste.md forbids fake dashboards and fake terminals, and
that rule is not suspended here. The surface has to be real markup running real
logic on real or clearly-labelled sample data. That labelled-sample escape is
how a concept product can still use this grammar: every panel is operable
markup computing its state from data arrays in the page, and the page says on
its face that the scenario is a demo. What stays banned is the painting of a
surface, an image or dummy divs posing as something that runs. If the panels
cannot actually compute, the grammar is unavailable. Pick another.

---

### 2.4 Continuous world

One canvas, fixed for the entire scroll, and the page travels through it.
Waypoints, not sections.

> **This grammar REQUIRES worldflight mode.** `data-sc-mode="worldflight"`, one
> fixed stage, one spacer, legs that crossfade. See references/worldflight.md.
>
> Building it out of pinned acts is not a lesser version of this grammar, it is
> a different and worse page, and it has already been tried. The owner's verdict
> on the act-based attempt: "awful... you're literally going from scrolling down
> to static page and then you start scrolling down again... weird clear page
> lines scrolling up... very cheap looking." Every one of those is the same
> defect. A pinned act is a block in the document; a document made of blocks has
> seams; and a world with seams is not a world. Do not reach for `scrub` acts
> here, however long you make the spans.

**Fits:** a journey with real geography. A supply chain, a process with physical
stages, a place, a build, anything where "where you are" is meaningful.

**The scroll feels like:** moving through a single space that never cuts. The
visitor never leaves the frame.

**Forbids:** section boundaries of any kind. No `sc-section` blocks, no acts at
all, no second stage, no `drift` steps (one continuous grade across the whole
travel, authored into the world, not interpolated between legs). Nothing may
scroll *over* the canvas: copy arrives inside it, at waypoints, in the fixed
copy layer. The only element in document flow is the spacer.

**Nav, hero, close:** the nav is a **map**. A waypoint list, a depth readout, a
position marker, and it is clickable, because a world you cannot skip around in
is a video. The hero is an establishing position inside the world, not a
separate title stage. The close is arrival at a place in the same canvas, and
the CTA is an object in that place.

**Leans on:** worldflight legs with `data-sc-linger`, copy windows against the
whole track, the `sc:waypoint` event driving a rail the page draws itself.
**Bans:** every act device, `flow`, `pan`, hard cuts, `src` swapping.

**This is the expensive one.** The chain warning in SKILL.md applies: a single
unbroken flight is the most fragile thing you can build, and the seam law in
worldflight.md section 6 is not optional. Choose this grammar only when the
brief is literally about travel through a place, and budget for the reroll.

---

### 2.5 Typographic poster

Type is the imagery. Media is minimal or entirely absent, and scale contrast
does every job that photography would have done.

**Fits:** a brand whose asset is a sentence. Manifestos, agencies with strong
verbal identity, launches with one claim, anything where a stock-looking image
would weaken the page rather than support it. Also the right answer when there
are no good assets and generating them would produce eight plausible, forgettable
frames.

**The scroll feels like:** words arriving at wildly different weights. A word at
40vw, then a paragraph at 16px, then silence. Rhythm comes from scale, not
motion.

**Forbids:** photographic ground, `scrub`, scrims (nothing to scrim), cards of
any kind, and decorative motion. If a device is doing the work instead of the
typography, the grammar has already failed.

**Nav, hero, close:** the wordmark is set as part of the composition, at
composition scale, not as a 14px bar item. There may be no persistent nav at
all. The hero is a single word or one line at extreme scale, filling the
viewport, with a real `<h1>` behind it. The close inverts the whole page: the
smallest type on the site, the CTA as a plain underlined link, quiet after all
that volume.

**Leans on:** `kinetic` (this is the one grammar where character splitting can
be right), `pin` with scale driven from `--sc-p`, `reveal` as a wipe across
letterforms, `drift` doing heavy lifting because the ground is most of the
frame. **Bans:** `scrub`, `pan` rails of cards, `tilt`, `parallax` on text.

**The typography floor doubles here.** taste.md caps display at ~6rem outside a
hero moment. This grammar is one continuous hero moment, so the cap lifts, but
the tracking, measure and optical-correction rules tighten: at 40vw, default
tracking is a visible defect and one bad kern is the whole page.

---

### 2.6 Gallery / catalog

Objects in a walkable collection. Museum labels, not marketing copy.

**Fits:** a range. Products with variants, a portfolio, a menu, a materials
library, case studies, anything where the visitor's real question is "what are
the options" rather than "should I believe you".

**The scroll feels like:** walking a room. Lateral drift with vertical scroll,
objects entering and leaving at their own pace, each one labelled with fact
rather than pitch.

**Forbids:** the argument-shaped pinned type act; a single hero claim; scrim
copy over media; persuasion in the object labels. A label reads
`Cedar. Air-dried 18 months. Kiln-finished.` and not `Craftsmanship you can
feel.` Every object gets the same label schema, no exceptions, because the
schema is what makes it a collection instead of a grid.

**Nav, hero, close:** the nav is an **index of objects**, and it jumps. The hero
is object one, already in view, already labelled, with no separate title
treatment: the collection starts at the top of the page. The close is either the
last object or an inquiry plate typeset exactly like a label, so the ask reads
as part of the collection.

**Leans on:** `pan` as the spine rather than as one act, `reveal` per object,
`tilt` on objects the visitor would pick up, `count` for real specs. **Bans:**
`kinetic` headlines, `spotlight`, `magnet`, more than one `scrub`.

**The rail copy constraint from devices.md §3 becomes structural here**, not a
caveat. Labels are read cropped for most of their life, so the schema has to
survive being half-visible.

---

### 2.7 Split stage

Two columns held in tension for the whole page, resolved by scroll.

**Fits:** any argument with two sides. Before and after, cost and saving, manual
and automated, what you have and what you would have. The comparison is the
product.

**The scroll feels like:** watching a balance tip. Both halves are always
present, both move, and the page is going somewhere specific: the moment one
side wins.

**Forbids:** full-bleed anything before the resolve; centred copy; the
corner-anchored hero; a symmetric close. Neither column may be decorative, both
carry real content the whole way down. The instant one side becomes a caption
for the other, this collapses into a zigzag layout with extra steps.

**Nav, hero, close:** no bar. The **divider is the chrome**, and it carries the
labels for both sides plus the progress of the argument. The hero establishes
the split at 50/50 on the first screen, with both headlines readable at once, so
the visitor understands the format before they scroll. The close is the
**collapse**: the divider travels to one edge, one column takes the full width,
and the CTA lives in the winning column. That collapse is the ending, and it
should be the single most satisfying moment on the page.

**Leans on:** `pin` with divider position driven from `--sc-p`, `reveal` per
side, `count` for the comparison figures if they are real. **Bans:** `pan`,
`spotlight`, `magnet`, more than one `scrub`, `drift` (two grounds, one per
side, and they hold).

---

### 2.8 Rhythmic cutlist

Short hard-cut acts at speed. No pinning, no dwell, no crossfades.

**Fits:** energy brands. Streetwear, sport, events, music, drinks, youth
products, anything where the visitor should feel a pulse rather than follow an
argument.

**The scroll feels like:** a cut every second. Twelve to twenty short sections
rather than six long ones, each one landing whole and gone. Total page length
stays inside the 8 to 14 viewport-height budget precisely because nothing is
held.

**Forbids:** any act over ~1.4 viewport-heights; `data-sc-dwell` above 0.1;
`pin` entirely; overlapping cue windows; slow easing. This grammar is the exact
inverse of the filmic one-shot: where that one hides its seams, this one is
made of them.

**Nav, hero, close:** the bar is loud, not minimal. Full-width, high-contrast,
possibly a marquee, possibly the CTA at the same weight as the wordmark. The
hero is one screen that cuts to the next in under a viewport, so there is no
settling shot and no greet-and-hold. The close is abrupt: the last cut is the
CTA, at full bleed, no spotlight, no drift-down.

**Leans on:** `flow` + `in` at short stagger, `reveal` on nearly every section,
`count` if the figures are real, hard `drift` steps between adjacent grounds.
**Bans:** `pin`, `spotlight`, `magnet`, `dwell`, `parallax`.

**The taste floor still applies at speed.** Fast is not an excuse for a
1.2 second entrance that the reader outruns. Cue windows here are short *and*
front-loaded, so a section is fully legible within the first third of its own
span.

**The peak problem, and how to resolve it.** This grammar bans `pin` and `dwell`
outright while feel.md insists the peak gets the most scroll room and the
biggest hold. Those pull in opposite directions, and the quiet failure is a
build that reaches for `pin` at its peak and still calls itself a cutlist.

**Hold in the fixed chrome layer, and keep every act short and unpinned.** The
loud bar this grammar already asks for is a persistent element that does not
belong to any act, so it can unfurl, run a long choreography and hold as long as
the peak needs while the acts underneath keep cutting at full speed. Drive it
from page scroll rather than from an act's `--sc-p`, since the whole point is
that it outlives the act it started in. The airfield build's departures board
runs its entire peak (unfurl, populate, cascade, reveal, hold, collapse) in
the chrome, with no pinned act anywhere on the page and nothing over 1.3vh.

The general form: **when a grammar bans the device your peak wants, move the
peak out of the act stack rather than breaking the grammar.** The bans are on
what the acts do, not on what the page can do.

---

## 3. The signature move

Every build must invent **one bespoke interaction that exists on that site
alone**. Not in the device kit, not in any prior build, not a parameter change.
Coded in the page, with `data-sc-*` attributes of your own naming or plain
inline JS reading `--sc-p`. The engine stays untouched, always.

This is the thing that makes a page memorable after the visitor closes the tab,
and it is the only part of a build that cannot be arrived at by following rules.

### What counts

- **Scroll-as-playhead over a persistent trace rail.** A thin horizontal trace
  fixed at the bottom edge, present the whole page, drawing a real waveform or
  route or timeline. Scroll position is the playhead. Passing an act stamps a
  marker on the trace that stays. By the footer the trace is a complete record
  of what the visitor just went through, and it doubles as navigation.
- **A wordmark the pointer can pull apart.** The letters follow the cursor with
  different masses, separate under a drag, and settle back into perfect lockup
  when released. Only on the hero, only once, and the settle has to be exact.
- **A line drawing that builds itself.** An SVG technical illustration whose
  `stroke-dashoffset` is driven from `--sc-p`, so scrolling literally draws the
  object, then the dimension lines arrive, then the callouts. Pairs with the
  technical-drawing world in worlds.md.
- **A running receipt.** A small fixed panel that accumulates a line every time
  the visitor passes a claim, with real numbers, so the close arrives with a
  totalled ledger of the argument they just read. Only works with real figures,
  which is the check on it.
- **One control that regrades the whole page.** A time-of-day handle, a
  temperature, a load level: one input, and every image, ground and accent on
  the page shifts together. It has to affect everything at once or it is a
  widget.

### What does not count

- A recoloured spotlight. A spotlight at a different radius. Two spotlights.
- `data-sc-tilt="9"` instead of `6`. Any parameter change to any kit device.
- A different easing curve on kinetic lines.
- Five cards in the rail instead of three, or the rail scrolling the other way.
- A third `scrub` act. More of a device is not a new device.
- Something the engine already does, given a project-specific class name.

The test: **describe the move to someone who has seen the other builds. If they
cannot tell it apart from something the kit already does, it is not a signature
move.** Reaching for a kit parameter here is the same failure as reaching for
the filmic default in §2, one level down.

---

## 4. The fingerprint gate

The registry lives at `<workspace>/FINGERPRINTS.md`, where `<workspace>` is
whatever `node <skill>/scripts/workspace.mjs` prints. It is per-user and it
starts empty: the gate is about not repeating **yourself**, so your first build
has nothing to clear and every build after it does.

A worked twelve-row registry ships as `EXAMPLES.md` in the scroll-craft
repository. Read it to see what a filled table looks like and which shapes tend
to collide. It is illustration, not constraint: those are somebody else's
builds and they do not gate yours.

**Before building:** read it. Every row is a shape that is now taken.

**Before writing markup:** check the planned build against every existing row on
these six dimensions.

| # | Dimension | What it records |
|---|---|---|
| 1 | Grammar | Which of §2, or a named new one |
| 2 | Nav treatment | What the chrome is and what it is for |
| 3 | Hero device | What the first screen does |
| 4 | Act-sequence shape | The device order, act count, total viewport-heights |
| 5 | Close pattern | How the last screen behaves and what the CTA sits in |
| 6 | Signature move | The one bespoke interaction, in a phrase |

**The gate: a new build must differ from EVERY existing row on at least 4 of the
6.** Not 4 of 6 on average across the table. Four against each row, individually.

Dimension 6 is free, because a signature move is unique by definition. So the
gate really asks for three more out of the remaining five, against each row, and
a build that changes only grammar and world will fail it.

**If the planned build fails the gate, change the plan, not the log.** Rewriting
a fingerprint row to make a new build fit is the one thing that makes this file
worthless. It is a record of what exists, not a description of what you wish
existed.

**After shipping:** append one row. Fill all six dimensions plus world and port.
Say plainly what it shares with prior rows, because the shared columns are what
the next build has to avoid.

---

## 5. Aesthetic range

Premium-minimal is a choice. It is not the costume this skill wears by default,
and four dark-or-paper pages with one accent each is what happens when nobody
decides otherwise.

The full range is available when the brand's vibe asks for it:

| Family | Reads as | Earned by |
|---|---|---|
| Brutalist | Blunt, structural, unstyled on purpose | Tools, infrastructure, anything anti-marketing |
| Maximalist | Dense, layered, loud, generous | Culture brands, events, food, anything abundant |
| Playful | Bouncy, coloured, informal | Kids, games, consumer apps, community |
| Retro | Specific to a decade, not vaguely nostalgic | Heritage brands, music, anything with a real lineage |
| Dense | Information-forward, small type, high count | Data products, catalogues, reference, finance |
| Editorial | Paper, folios, measure, restraint | Long-form substance |
| Premium-minimal | Quiet, dark, one accent, air | Luxury, and only when asked for |

Go where the interview points. If the human says "loud" and the page comes back
in charcoal with one accent, the interview was decorative.

**What does not flex:** the taste floor. Spacing scale and rhythm, type metrics
and measure, contrast ratios measured on the render, motion built from
`transform` and `opacity`, focus-visible on everything, reduced motion that
keeps meaning, real copy and real numbers. Every item in taste.md holds in every
aesthetic family.

A brutalist page still needs 4.5:1 body contrast. A maximalist page still needs a
spacing scale, and needs it more, because density without rhythm is just noise. A
playful page still cannot animate `top`. **The floor is what separates a chosen
aesthetic from a sloppy one**, and it is the reason range is safe to offer at
all.

Two specific traps stay banned in every family, because they are not aesthetics,
they are defaults with a look: the cream-and-brass artisan palette
(taste.md, Colour) and violet-to-blue AI gradients. Both are what a page reaches
for when nobody chose.

---
## references/verify.md

# Verify

For the approved collection’s functional, fallback, and deployment acceptance
procedure, also read [approved-collection.md §7](approved-collection.md#7-require-visual-and-functional-evidence-before-delivery). Test the final files, preserve
failed evidence, and identify the rerun that resolves each finding.

A scroll page cannot be checked by looking at it. It has no single state: every
scroll position is a different frame, and the failures live between the two you
happened to look at. So walk it mechanically.

```bash
cd <build project>
npm i playwright-core                       # once

node <skill>/scripts/serve.mjs --root . --port 4500 &
node <skill>/scripts/shoot.mjs --url http://localhost:4500 --out lab/shots
node <skill>/scripts/shoot.mjs --url http://localhost:4500 --out lab/mobile --width 390 --height 844
node <skill>/scripts/shoot.mjs --url http://localhost:4500 --out lab/reduced --reduced-motion
```

Then **read `sheet.png`**. The whole point of shooting contiguously is looking
at the frames side by side; a folder of PNGs does not get looked at that way.

Two setup facts that will otherwise waste a pass:

- **Serve it.** `file://` blocks the Blob fetch the engine uses for clips, so
  the page silently falls back to posters and proves nothing.
- **Real Chrome, not bundled Chromium.** Chromium ships without an h264
  decoder, so every clip fails to paint and the run "passes" against posters.
  `shoot.mjs` already resolves installed Chrome; override with
  `SCROLLCRAFT_CHROME`.

---

## What the harness reports

It samples **within each act** (default 6 positions per act) rather than
uniformly down the document. Uniform sampling moves every position whenever you
change any section's height, so findings appear and vanish with unrelated edits.

**DEAD SCROLL**: consecutive positions where nothing changed: no cue moved, no
clip time advanced, no rail travelled, no wipe progressed, no stage shifted.
Real dead scroll means the reader is turning the wheel and being given nothing.
Fix by shortening the act's span or adding a cue.

**Bespoke fixed stages must report their visible state.** A split stage, live
canvas, or other page-local system can use ordinary `flow` acts only as scroll
markers while every visible change happens on a fixed layer outside the engine.
The harness cannot infer that layer's semantics. Put `data-sc-verify-state` on
the fixed stage and update its value to a compact signature of the values that
actually paint: divider position, scene opacity, canvas phase, custom film
time, or similar. The detector then checks those flow spans too.

Do not publish raw scroll progress just to make the check green. If progress is
changing while the composition is not, that is the exact failure this path is
meant to catch. Round and publish the rendered values. For an intentional
resolved hold, set `data-sc-verify-hold="true"` only while the hold is active.
Reduced-motion fixed stages may use the same attribute for deliberately stable
frames, which still require manual contact-sheet review.

**FROZEN CLIP**: a scrub stage is on screen, the reader is scrolling, and the
clip's playhead is not moving. Dead scroll cannot see this, because the stage
itself *is* moving: a still photograph is sliding up the page, which is the
worst-looking failure this kit can produce and the one that most reliably makes
a page feel broken.

The harness samples each scrub act's **entry and exit slides**, not only its
pinned travel. That gap is why this went undetected for four builds: a pinned
act's samples were taken at `top + (h - vh) * p`, which never visits the viewport
of scroll on either side where the stage is visible and the clip is parked. A
hold on the first or last frame is always reported. A hold in the middle is only
reported once it outlasts any plausible `data-sc-dwell` settle, since that settle
is a deliberate effect. The check is skipped under reduced motion, where no clip
is ever fetched on purpose.

The fix is almost never per-page: the engine maps clip time across the stage's
whole visible life by default. A page that reports this has usually opted out
with `data-sc-clip-map="travel"`, or is running an engine copy from before that
default existed. See [devices.md §1](devices.md).

**CUES THAT NEVER PEAK**: an element that never reaches full opacity anywhere.
Usually a cue window too narrow for its act, or ramps that eat the whole window.
Widen the window or set explicit ramps. A kinetic heading is read through its
line units, not through the element: the engine forces the element itself to
opacity 1 and carries the real value on `.sc-split__i`, so reading the element
reports every kinetic headline as fully present even on frames where every line
is at 0.

**CONTRAST**: measured on the **composited page**, not on the source video. The
harness hides the text, re-shoots the same frame, and samples the real
background under each line, so scrims, gradients and blends are all included.
Elements with their own opaque background are graded against that fill instead.

Three things it gets right that a hand-rolled version usually does not:

- **The direction is picked per line.** Light type on a dark page fails on the
  brightest patch under it; dark type on a light page fails on the *darkest*
  one, and grading that against the brightest patch is the most lenient reading
  available, so a high-key page can report clean over text that is failing. The
  harness compares the ink to the mean background and grades against whichever
  extreme is on the ink's own side.
- **The sampled rect is clamped to the viewport.** The part of a pinned act's
  copy that has scrolled above the fold is not on screen, so what sits in those
  pixels is not behind anything the reader can see.
- **Fixed chrome is hidden with the text.** A fixed bar paints in *front* of
  what scrolls under it, so its own mark is not the background behind a headline
  passing beneath it.

This is the check no static audit can do: the frame under a headline changes as
the clip scrubs, so text can clear 4.5:1 against the poster and fail badly three
hundred pixels later.

### The scrim has to be a SIBLING of the copy, never a child

The pass hides `[data-sc-cue],[data-sc-cue] *,[data-sc-copy],[data-sc-copy] *`
before photographing the frame underneath a line. `visibility: hidden` hides an
element's pseudo-elements too, so a scrim written as `.mycopy::before` is hidden
along with the text it exists to protect, and the pass grades the line against
the raw film every time.

The tell is unmistakable once you know it: **you strengthen the scrim and the
reported numbers do not move at all.** Not "improve slightly", not "move by a
tenth": byte-identical, because the thing you changed was never in the
measurement. If a contrast number is unchanged to two decimals after a real
change, stop tuning and check what is actually being composited.

A very high mean against a very low worst (`1.21:1 (mean 12.83)`) is the same
finding seen from the other side: the type is fine almost everywhere and there
is a bright patch under it that nothing is covering.

Two shapes that work:

- `.sc-world__scrim` in the copy layer, which is what worldflight.md ships and
  which survives the hide because it carries no `data-sc-copy`.
- One plate per block, mounted as a sibling and driven from the page's own JS.
  `orrery` sizes each plate off its block's untransformed box (set
  `transform:'none'`, read the rect, put it back, so the engine's ±2vh copy
  drift does not skew the measurement) and each frame copies the block's own
  inline opacity onto its plate, so the plate tracks the engine's window with no
  duplicated window maths.

### Known limitations of the contrast pass

Real, and worth knowing before you trust a green run:

- **Cues are keyed by their text.** Two cues that share a string, which
  taste.md's "one label per intent" rule actively encourages, are collapsed into
  one row, and the reported worst frame is the worse of the two.
- **Lines under 0.85 opacity are skipped.** A headline parked at 0.6 over a
  bright frame is never graded, so "contrast clean" can still hide a legibility
  problem. Look at the sheet for anything that reads washed out.

  **Author the fade-outs to land between sample positions.** The harness samples
  a fixed number of positions per act, so a ramp-out that happens to straddle one
  puts a half-faded headline on the sheet: graded by nobody, and read by eye as
  ghost type over the frame. **Fix the ramp, not the sampling.** Shorten
  `rampOut` so the cue is at full opacity at one sample and gone by the next,
  rather than sitting at 0.5 on the sample in between. Widening the sample count
  only finds more half-faded frames; it does not make the page look better,
  because a real reader stopping on that pixel sees exactly what the sheet
  shows. A cue caught mid-fade over a bright frame is a real defect, not a
  sampling artefact.
- **The floor is not size-aware.** It reports below 3:1 as a failure and 3:1 to
  4.5:1 as thin. WCAG allows 3:1 for large text, so a display headline in the
  thin band is usually fine and a 16px caption in it is not.
- **Acts with no `[data-sc-cue]` elements are not graded at all.** Copy on plain
  canvas is a static case, but it is unmeasured.
- **Ordinary `flow` acts are excluded from dead-scroll checks.** Static flow is
  normally correct. A bespoke fixed experience built over flow markers must use
  `data-sc-verify-state`, or the harness will skip its visible timeline and can
  report a dead opening as healthy.
- **A `pan` act whose rail does not overflow is reported as healthy.** The
  `pigment` build ran a rail measuring 1368px inside a 1440px viewport, so it
  travelled zero for its entire 2.1vh span, and every pass printed `no dead
  scroll detected`. Measure `rail.scrollWidth - innerWidth` yourself; a green run
  does not cover it. See devices.md §3.

**Console errors and failed requests**: a 404 on a clip degrades to a poster
silently, which looks fine and is not.

---

## What the harness cannot tell you

Read the sheet for these. They are the ones that matter most.

- **Whether the composition is any good.** Copy landing on the busiest part of
  the frame, a subject cropped at an unfortunate point, an act whose end frame
  is a dark empty corner.
- **Whether the motion is smooth.** Contiguous frames prove the clip advances;
  they do not prove it advances evenly. Watch the contact sheet for a move that
  lurches, reverses, or stalls in the middle.
- **Whether the page means anything.** Six acts that each work and together say
  nothing is the most expensive failure available here.

---

## The manual passes

**Reduced motion.** Clips are never fetched, posters hold, copy still cues. The
page must remain comprehensible, not merely not-crash. This doubles as the
low-bandwidth check.

Comprehensible includes **reachable**: check that no content was deleted rather
than merely stilled. A `pan` rail is the case that bites, because zeroing its
transform parks it on its first screenful. The engine now hands the stage back
as a native scroll region, so confirm on the sheet that the rail shows real
content and that items past the fold can still be got to. Nothing in the harness
reports this; it reads as a page behaving correctly.

**Credit accounting.** `kie.mjs probe` reports a balance, not a delta, so a
build's spend is a before-and-after subtraction. That subtraction is only valid
if nothing else is generating against the same key. When builds run in parallel,
or when a settlement lands late, the deltas overlap and each build will claim
some of another's spend (three parallel builds each read the same 7597 → 7067 and
each reported 530). Either serialise generation, or cost the build from the
per-call model prices in [assets.md](assets.md) against the calls you actually
made, and treat the probe delta as a ceiling.

**And the per-call sum overstates real spend in the other direction.** Two
reconciliations against the account ledger, each with no other consumer, put
actual debits at roughly **0.4x** the documented unit rates: a fleet whose
per-call sums came to ~1447 credits was debited 530, and a three-build run whose
per-call sums came to 2252 was debited 856. Both land near the same ratio. So a
build report should say what the per-call sum is *and* that it is a planning
ceiling rather than the amount billed. Reporting the sum as the cost is the
honest default, because it never under-claims; reporting it as *measured* spend
is wrong. Neither number is the other's substitute: the probe delta bounds a
parallel run from above, the per-call sum bounds a serial one from above, and
only a ledger read with a single consumer settles it.

**Mobile.** Pinned stages use `100svh` so the URL bar does not cause a jump.
Copy reflows and does not collide with the fixed bar. Confirm the phone encodes
actually load. Check the portrait crop of every clip: a 16:9 move composed
around left-hand negative space loses exactly that space at 9:16
(see [assets.md](assets.md)). Mobile is a first-class target, not a check at
the end: the phone clips are cut portrait, the lerp is retuned for touch, tap
targets are grown, and every one of those is authored, not inherited.

### The phone is a different machine

Headless Chrome on the build box cannot reproduce an iPhone's video decoder,
its autoplay policy, Low Power Mode, or touch scrolling. On one build every
probe reported the hero clip scrubbing perfectly for **four consecutive
rounds while the real phone showed a frozen frame**. A green harness run says
the page is correct where the harness runs. It says nothing about iOS video.

What iOS does to a scrub clip, and what the engine now handles for you:

- iOS will not *paint* a muted video that has never been played. Seeks land,
  `seeked` fires, and the picture stays on one frame. The decoder has to be
  primed with one `play()`/`pause()`.
- The engine primes each clip at `loadedmetadata` (a muted inline `play()`
  needs no gesture outside Low Power Mode) and retries on `touchstart`,
  `touchend`, `pointerdown`, `click` and `scroll`. `touchend` matters: the
  HTML spec's activation-triggering events include `touchend` but **not**
  `touchstart`, so a Low Power Mode phone that rejects the touchstart attempt
  gets a valid one when the finger lifts.
- A prime must be re-attemptable per clip. A one-shot prime on first touch
  loses a race: the reader touches to scroll within the first second, while
  the hero's megabytes are still downloading, and the shot is spent on a
  sourceless element. The tell is exactly "the first clip is frozen and every
  later one works".
- iOS may leave a `play()` promise pending forever, and may leave `seeking`
  true forever. Both were permanent silent freezes; the engine now releases
  the priming flag on a timer and re-issues any seek stuck past 700ms. The
  reveal also fires on a 2.5s timeout, never only on `seeked`.

Do not re-implement any of that in page JS, and do not strip it when copying
the engine. If a phone still shows a frozen clip, the cause is past what this
machine can measure, which is what the next section is for.

### Ship the diagnostic with the site

You get one question per round with a real device, so make the round count.
`references/device-diag.html` is a standalone page that scrubs the suspect
clip two ways (blob URL, exactly as the engine loads it, and direct file src)
beside a known-good clip, prints a MOVING / FROZEN verdict over each pane,
and reports prime results, seek counts and distinct painted frames. Edit its
`TESTS` array to point at the build's own clips, deploy it next to the site,
and one screenshot from the phone isolates the layer: blob loading, the file,
the device's decode policy, or the engine's lifecycle. Deploy it **with** the
first mobile fix, not after the fourth.

### Ask what differs before asking what's broken

The debugging lesson that cost three wasted rounds: "desktop works, the phone
does not" reads as a platform difference and invites platform theories
(codecs, keyframes, resolution). **"One clip works and another does not, on
the same device"** cannot be a platform difference. Before theorising, write
down every way the working case differs from the broken one; the bug lives in
that list. On the build above the list had one entry: the hero is first, so
it loads while the first touch is being spent.

**Keyboard.** Tab through. Focus order matches visual order, the focus ring is
visible against every ground it crosses, and nothing reachable is parked at
opacity 0. Cues set `pointer-events: none` when faded, but a focusable element
inside a faded cue is still a trap.

The engine helps here but does not finish the job, and the gap is specific:

- **It handles the ordinary case.** On `focusin`, if the focused element is
  inside a `[data-sc-act]` and its own cue computes under 0.85, the engine
  scrolls it to the centre of the viewport with `behavior: 'instant'`
  (`smooth` would animate a multi-screen glide with focus off screen the whole
  way). On a `flow` act, centring the element also opens its cue, because the
  element's viewport position and the act's progress move together.
- **It does not fix a pinned act, and cannot with this approach.** A pinned
  stage is `position: sticky`, so the control holds *one* viewport position for
  the entire act. Centring it is then only achievable by scrolling backwards out
  of the act, which parks progress at 0 and leaves the cue dark. Measured: a CTA
  cued at 0.75 on a 3vh pinned act sits at viewport y=70 from progress 0 to
  0.875; `scrollIntoView({block:'center'})` from inside the act lands *before*
  the act's top, at progress 0, cue opacity 0. The control is on screen and
  still invisible.

**On a pinned act, park the act at the progress where the focused element's own
cue is open.** That is page-local work, because only the page knows which cue
belongs to which control, and because act progress runs through `dwell()` when
the act has any, so the scroll target is not a straight inverse of the cue
window. The descent build does exactly this. If a pinned act carries a focusable
control, write that handler and assert it; do not assume the engine covered you.

**Fresh eyes.** Look again later. Timing you tuned for twenty minutes reads
differently when you have forgotten what it is supposed to do.

---

## Failures worth knowing about

Each of these shipped once during this skill's own build, and each looked fine
until it was measured.

| Symptom | Cause |
|---|---|
| A hero headline wrapped to six lines | `max-width` in `ch` on a **container**: `ch` resolves against the container's font-size, not the display size of the heading inside it |
| Centred copy hanging off the left edge | `inset-inline` declared **after** `left: 50%`; the shorthand resets `left` to auto |
| An act that never pins, silently | An author rule setting `position` on the stage. The engine now warns in the console |
| A stray headline painted over a later section | Cues frozen at their last value when their act scrolled out of range |
| A clip stuck on its poster at the top of its act | The reveal waits for a `seeked` event, and a clip already at time 0 never seeks |
| A closing CTA that fades out before the page ends | A two-value cue on the last act, plus a tall section after it |
| Copied headings reading "even whenbreakfast" | Line-split spans abutting with no whitespace between them |
| A headline from act 2 overlapping act 3, failing contrast on the way | A one-value hold cue on a middle act. Only the last act may hold |
| A phone-only contrast failure on a trail-anchored act | The trail scrim aimed at the corner the copy leaves below 860px. The engine now switches it to a band |
| A rail act that shows one frozen screenful under reduced motion | `[data-sc-pan] { transform: none }` deleting the navigation. The engine now falls back to a scroll region |
| Two washed-out video acts no scrim tuning could rescue | Flat supplied footage with no white point. Grade the intermediate, not the CSS |
| A blank stage for the first viewport of a pinned act | A two-value first cue with no ground. Ground or greet |
| A rail heading dragged off-screen under reduced motion | The scroll-region fallback snap-centres a single wide track; keep the act heading outside the region, or give the rail multiple snap stops |
| Keyboard focus landing on a control nobody can see | The browser's scroll-into-view parks the element barely on screen, which is where its cue has not opened, and the opacity check still passes. **The engine now centres it on `focusin`** when the element is inside a `[data-sc-act]` and its cue is under 0.85. That fixes the off-screen half. See the note below for what it does not fix |
| A figure or drop numeral rendering as a plain bar | `data-sc-reveal` on type with `line-height` below 1. `clip-path` is relative to the border box, so the wipe eats the ascender and descender. See devices.md §4 |
| An image three times too tall, pushing its own label off the fold | `width` overridden in CSS while `height` still resolves to the HTML attribute. Override both or neither. See taste.md |
| An inverted section rendering its old ink, graded in the wrong direction | `--sc-ink` redefined on the subtree without restating `color`. See taste.md |
| A ground colour arriving a section late | `drift` on a page of short acts; several are part-way through at once. Paint grounds per section. See devices.md §10 |
| Every cue and reveal in a quiet act snapping 0 to 1 | A pinned act at `data-sc-span` ≤ 1, which is one pixel of travel. Minimum useful pinned span is ~1.2 |
| A clip that scrubs beautifully, stops, and then slides up the page as a still photograph | The clip was mapped to the act's pinned travel, which is 0 through the entire entry slide and 1 through the entire exit slide. The engine now maps clip time across the stage's whole visible life by default. See devices.md §1 |
| A custom fixed stage passing while its first screens do nothing | The page used `flow` markers, which are intentionally excluded from ordinary dead-scroll checks, but published no `data-sc-verify-state`. Report the actual rendered state and declare only genuine resolved holds |
| The hero clip frozen on a real iPhone, later clips fine, every probe green | iOS never paints an unplayed muted video, and the one-shot gesture prime was spent while the hero was still downloading. The engine now primes per clip at `loadedmetadata` and retries on every gesture, including `touchend` |
| A phone clip soft and stuttering while the same file is smooth on desktop | A landscape mobile encode in a portrait viewport: cover-fit decoded the full frame and threw three quarters of it away. Cut the phone clips portrait from the masters (see assets.md) |
| Four rounds of mobile fixes verified green, phone still broken | Headless Chrome cannot reproduce the iOS decoder, Low Power Mode, or touch. Deploy `references/device-diag.html` beside the site on the first mobile report and let the phone answer |

The first three are invisible to every check except looking at rendered output.
That is the argument for this whole pass.

Operational note: a `shoot.mjs` run can take the background server process down
with it when it finishes. Check the port before the next pass and restart
`serve.mjs` if it dropped.


## The harness will photograph the wrong site without telling you

`serve.mjs` fails with `EADDRINUSE` if something already holds the port. When
that server was started in the background, the failure is in a log nobody is
reading, and `shoot.mjs` then gets a perfectly good `200` from **whatever else
is on that port**. It walks that page, finds its worldflight, and writes a full
contact sheet and a clean report for a site you did not build.

Confirm the port is serving YOUR build before trusting any run:

```bash
curl -s http://localhost:45XX | grep -o "<title>.*</title>"
curl -s -o /dev/null -w "%{http_code}
" http://localhost:45XX/assets/leg01.mp4
```

A 404 on an asset you know exists is the fastest tell.

---
## references/assets.md

# Assets

Generation through kie.ai, then encoding for scrubbing. All of it via
`scripts/kie.mjs` and `scripts/encode.sh`.

```bash
node <skill>/scripts/kie.mjs probe                     # credit check first
node <skill>/scripts/kie.mjs still "<prompt>" out/01.png --ar 16:9 [--ref brand.png]
node <skill>/scripts/kie.mjs shot  "<move>"   out/01.png out/01.mp4 --dur 5 [--tail end.png]
bash  <skill>/scripts/encode.sh out/01.mp4 assets/01.mp4
bash  <skill>/scripts/encode.sh out/01.mp4 assets/01-m.mp4 mobile
```

Models: `seedream/5-pro-text-to-image` (and `-image-to-image` when you pass
`--ref`) for stills, `kling/v2-1-pro` for camera moves. `aspect_ratio`,
`quality` and `output_format` are all required by seedream; omitting any one
returns a bare "This field is required" that does not name the field.

**seedream rejects most aspect ratios, and does not say which it takes.** An
unsupported value fails at `createTask` with `"This aspect_ratio is not within
the range of allowed options"` and no list, which reads like a malformed request
rather than a menu problem. Nothing is charged, so the cost is a wasted round
trip and the time to work out that the string itself was fine.

| `--ar` | Status | Returns |
|---|---|---|
| `16:9` | works | 2736x1520 |
| `9:16` | works | 1520x2736 |
| `3:4` | works | 1776x2352 |
| `4:5` | **rejected** | n/a |

The returned pixel dimensions are near the ratio rather than exactly it, so
derive layout from the file, not from the string you asked for. Treat the table
as the verified set rather than as the whole allowed list: **use a value from it,
and if you need another, send one throwaway call before writing a wave of
prompts around it.** `4:5` is the one that catches people, because it is the
standard portrait social ratio and every other generator takes it. `3:4` is the
portrait to reach for here.

---

## How many assets

Fewer than you think. A six-act page needs roughly:

- **2 clips.** One hero move, one texture or detail move. That is the cap from
  SKILL.md, and it is a quality rule as much as a budget one.
- **4 to 6 stills.** Posters for the clips, plus whatever the flow and rail acts
  show.

Every clip also needs a poster, and the poster must be **the clip's own first
frame**, not a separate generation. Pull it with ffmpeg rather than generating
a lookalike; a poster that does not match causes a visible jump the moment the
video paints.

```bash
ffmpeg -y -i out/01.mp4 -frames:v 1 -q:v 2 assets/01-poster.png
```

**Use the same ffmpeg `encode.sh` resolved, not bare `ffmpeg`.** `encode.sh`
goes looking for a full build precisely because the one on PATH may be stripped,
but this poster line and the PSNR seam check below both call `ffmpeg` directly.
On a stripped build the WebP muxer is missing and you get `Unable to choose an
output format for 'poster.webp'`, which reads like a bad filename rather than a
missing encoder, and `-lavfi psnr` fails with `No such filter: 'psnr'`. Resolve
it once and reuse it:

```bash
FF=$(ls -d "$HOME"/AppData/Local/Microsoft/WinGet/Packages/Gyan.FFmpeg_*/ffmpeg-*-full_build/bin/ffmpeg.exe 2>/dev/null | head -1)
"$FF" -y -i assets/01.mp4 -frames:v 1 -vf scale=1600:-2 -c:v libwebp -quality 82 assets/p01.webp
```

---

## Stills

1. Pick a world in [worlds.md](worlds.md) and write its preamble once.
2. Every prompt is: **preamble, blank line, scene**. Verbatim preamble, every
   time. This is the single thing that makes separately generated images look
   like one shoot.
3. Name where the empty space goes, because copy sits on these images.
4. **Read every PNG before using it.** Generation is cheap; shipping a bad frame
   is not.

**Pass the real brand object as `--ref`.** For anything with packaging, a logo,
or a product, hand seedream the actual asset. A label that drifts between shots
is the first thing a client notices. It holds up remarkably well: the same can
reads correctly across a dark kitchen, a hard-light flat-lay and a studio
backdrop when the cutout is passed every time.

---

**Published unit costs** (kie.ai, as of August 2026): `seedream/5-pro` still =
**28 credits**, `kling/v2-1-pro` 5s clip = **160 credits**. A seven-still,
two-clip page is therefore around 520 credits. `kie.mjs probe` reports a balance,
not a delta, so plan the spend from these numbers and probe before and after to
confirm. Do not trust a probe delta as this build's cost if anything else is
running against the same key (see [verify.md](verify.md)).

**Plan against those rates; expect the ledger to debit roughly 0.4x of them.**
Two reconciliations against the account with no other consumer put actual debits
at about 38% of the per-call sum: 1447 summed against 530 debited, and 2252
summed against 856 debited. That puts a still nearer ~11 credits and a 5s kling
clip nearer ~61, though those are back-derived from two samples rather than
published, so do not quote them as rates.

The practical consequence: **every budget cap in this skill is conservative, and
a build that comes in at its per-call cap has real headroom.** Keep planning at
28 and 160, because a ceiling that never under-claims is the right one to hold a
build to, and because the ratio is an observation about billing rather than a
published price that will hold. But do not refuse a justified reroll on a cap
computed at the documented rates, and do not report a per-call sum as measured
spend.

---

## Real footage the client already has

Supplied footage is usually **flat**: shot log-ish or picture-profiled, with no
white point. Downscaled straight into the page it produces washed-out acts that
no amount of scrim tuning rescues, and the instinct to fix it with a CSS filter
over full-bleed media is the thing taste.md warns against, because a filter
flattens the whole frame.

Grade it into a pre-encode intermediate instead:

```bash
# 1. measure. YMIN/YMAX/YAVG tell you whether the clip ever reaches white
ffmpeg -i raw.mov -vf signalstats,metadata=print:key=lavfi.signalstats.YMAX -f null -

# 2. expand levels + a small saturation lift, and land the fps you want
ffmpeg -y -i raw.mov -vf "colorlevels=rimin=0.09:gimin=0.09:bimin=0.09:\
rimax=0.64:gimax=0.64:bimax=0.64,eq=saturation=1.08,fps=30,scale=1920:-2" \
  -c:v libx264 -crf 16 -an graded.mp4

# 3. then the normal dense-GOP encode for scrubbing
bash <skill>/scripts/encode.sh graded.mp4 assets/01.mp4
```

Three more things real footage needs that generated clips do not:

- **Trim before anyone enters frame.** "Nothing enters or leaves" is a hard
  requirement for a clip the reader can park anywhere in, and real recordings
  routinely have someone walk through at second nine.
- **Target 24 to 30fps before `encode.sh`.** A 60fps phone clip at a dense GOP is
  several times the size for motion no hand can resolve. Decimate with `fps=30`
  in the intermediate; the dense keyframes then cost half as much.
- **Re-encode, never stream-copy.** Cuts made with `-c copy` decode badly under
  a scrubber.

---

## Camera moves

`shot` takes a still and moves the camera through it.

What makes a clip scrub well is not the same as what makes it watch well:

- **One continuous move, one direction.** A dolly-in, a drift down, a slow
  orbit. Any cut, snap or direction reversal becomes a jolt under the wheel,
  because the reader controls the playhead and will sit on the reversal.
- **Slower than feels right when previewed.** The move is spread over two or
  three viewport-heights of scroll. A move that looks sedate at 24fps feels
  correct under a hand.
- **The subject stays in frame throughout.** The reader may park anywhere.
- **Nothing enters or leaves.** A person walking in is a different shot at
  frame 1 and frame 120, and the poster will match neither.

Prompt shape: what continues, how the camera moves, then the negatives.

> The camera pushes slowly and steadily forward toward the can, a smooth
> continuous dolly-in with a very slight downward tilt. The blurred window
> slides past on the left as parallax. The can stays perfectly still and in
> frame throughout. One single continuous take, no cuts, no camera shake, no
> zoom snap. Slow, cinematic, controlled.

The script already sends a negative prompt covering judder, warping, morphing,
flicker and scene changes, which are the failure modes that specifically wreck a
scrub.

### Seam locking, if you actually need a chain

`--tail` pins the last frame as well as the first. Leg N's tail is leg N+1's
head, so the joint is frame-identical:

```bash
ffmpeg -y -sseof -0.05 -i leg1.mp4 -frames:v 1 -q:v 2 seam1.png
node kie.mjs shot "<move>" seam1.png leg2.mp4 --dur 5
```

Extract the seam frame from the **encoded** clip, not the source: re-encoding
shifts frames slightly, and a seam built from the wrong file is a one-frame pop.

**Chaining on pre-generated anchors makes the legs parallel.** Extracting each
leg's head from the previous leg's *encoded* file forces the whole flight to
generate serially, which is roughly 45 minutes for ten legs. Generating every
anchor still first, then giving leg N `--tail` of anchor N+1 and head of anchor
N, means all ten clips run at once and every joint is still frame-locked to an
image both sides were built from. `orrery` did this for a ten-leg flight and
measured 28.5-39.8 dB across all nine joints, inside the band the serial
`descent` chain shipped at. The cost is that you must author the anchors as a
coherent descent, because the model resolves a head-and-tail conflict by pulling
the camera back.

Most pages should not do this at all. A chain exists only to hide cuts between
scenes, and varying the device between acts removes the cut instead of hiding
it, for free and with no failure mode. Chain only when the brief is literally
"one continuous journey".

---

## Encoding

`encode.sh` sets a dense GOP (`-g 8` desktop, `-g 4` mobile), strips audio, and
adds `+faststart`.

The reason is the whole trick: **a normal web encode places a keyframe every two
to five seconds.** Seeking to an arbitrary time makes the decoder walk forward
from the previous keyframe, so a sparse-GOP file plays perfectly and scrubs like
mud. Dense keyframes cost file size and buy responsiveness.

Expect roughly 3MB for a 5s 1080p desktop clip and 1.5MB for the 720p mobile
one. Two clips is about 9MB of video on the page, and the engine fetches each
only as its act approaches.

**Grain-heavy worlds run about double that.** Documentary grain, moving foliage
and film texture gave 5 to 6MB per desktop clip at the script's `-crf 20`;
`-crf 22` is a reasonable dial if a page needs the megabytes back. Longer real
footage costs proportionally more, so a page carrying 15 seconds of supplied
video will sit above the 9MB guideline. That can be the right trade, but make it
deliberately rather than discovering it at the end.

Audio is stripped because these clips are scrubbed, never played. A muted track
is dead weight and an autoplay-policy hazard.

**A stripped ffmpeg will fail here.** Some toolchains put an ffmpeg on PATH with
about 50 filters and no `scale`, `fps` or `tile`. It reports `No option name
near ...`, which reads like a syntax error in your command rather than a missing
filter. `encode.sh` counts filters and goes looking for a real build; override
with `SCROLLCRAFT_FFMPEG`.

---

## Portrait

A 16:9 clip covering a 9:16 viewport crops to the middle third, and a
composition built around negative space on the left loses exactly that space.

Options, in order of cost:

1. **Compose for both.** Keep the subject in the centre third and the copy in a
   bottom band rather than in side negative space, because the bottom band
   survives a centre crop and side space does not. Cheapest, and usually enough.
2. **Native portrait renders.** Generate a 9:16 still and clip for the hero only,
   wire them as `data-sc-src-mobile`, and swap the poster with `<picture>` so the
   frame-holder matches the clip it is holding for:

   ```html
   <picture>
     <source media="(max-width: 860px)" srcset="assets/01-hero-p.webp">
     <img class="sc-stage__poster" src="assets/01-hero.webp" alt="">
   </picture>
   <video data-sc-scrub data-sc-src="assets/01.mp4"
          data-sc-src-mobile="assets/01-p.mp4" playsinline muted></video>
   ```

   A portrait poster with a landscape clip (or the reverse) jumps visibly the
   moment the video paints, which is the whole failure the poster exists to
   prevent. `encode.sh` has no portrait mode; do it by hand:

   ```bash
   ffmpeg -y -i src.mp4 -vf "scale=720:-2" -c:v libx264 -crf 20 -g 4 \
     -pix_fmt yuv420p -an -movflags +faststart assets/01-p.mp4
   # cropping a 16:9 source to 9:16 instead of rendering native:
   #   -vf "crop=ih*9/16:ih,scale=720:-2"
   ```
3. **Drop the clip on phones.** Serve the poster and let the copy carry the act.
   Under reduced motion the engine already does exactly this, so the layout is
   known to work.

Also step the hero display size down on phones. `--sc-t-4xl` floors at 3.4rem,
which is a desktop floor: it wraps a normal hero headline to six lines at 390px.
See [taste.md](taste.md).

Do not solve it with `object-fit: contain`. Letterboxed video on a landing page
reads as a broken embed.

---
## references/hero-depth.md

# Premium hero depth

## Standing preference

For hero-led marketing websites, **layering is part of the baseline, not an optional polish pass**. A beautiful full-screen photograph with one parallax transform and some text fades can still feel flat. Design a memorable spatial relationship in the hero from the beginning, rather than waiting to be asked for more depth.

This preference applies to the hero, not to every section of every website. Preserve the requested brand, content, framework, and functionality. Honor explicit static or simpler directions. Working surfaces such as dashboards do not need an invented marketing hero.

## Plan the depth before generating assets

- Identify the background, focal subject, foreground, and any natural atmospheric layers. Name what moves independently, what overlaps, and what must stay physically connected.
- Give the visitor a clear visual payoff during a short scroll sequence. For example: the camera moves into a scene, the headline recedes behind a subject, a second narrative beat appears, and the scene settles into the next section.
- Choose the sequence for the brand. The Sonder mountains, red dress, five planes, second headline, and gallery-frame exit are an example, not a mandatory template.
- Layering must change perceived depth. Several stacked elements moving as one image do not meet this preference. Use visibly different translation or scale rates, occlusion, and near/far relationships.
- Keep the initial composition compelling and the full headline readable. Do not sacrifice comprehension just to prove that a subject can cover text.

## Prepare real compositing assets

Use supplied photography or authorized image-generation tools. For a photographic scene:

1. Create a clean background plate with the extracted subject removed and the space behind it rebuilt. Otherwise the moving cutout exposes a duplicate person or an empty hole.
2. Isolate the subject and relevant foreground into genuine alpha cutouts. Inspect the alpha channel; a generated checkerboard or white backdrop is not transparency.
3. Preserve framing, scale, lighting, color, and camera perspective. Separately generated layers often need measured alignment even when the prompt requested identical placement.
4. Keep shared contact points anchored. A person should remain on the rock, a product on its plinth, and a wheel on the road. Shared translation and a common contact-point pivot can support different layer scales without making the subject float.
5. Inspect cutout edges against contrasting backgrounds and through the motion. Remove matte halos, jagged edges, clipped fabric, and foreground seams using the permitted asset workflow. Retain originals and optimize delivery files without losing alpha.

Atmosphere can occupy both a rear and a front plane when it supports the scene. It should create separation, not obscure the subject or wash out the whole image.

## Choreograph restrained motion

- Use one coherent camera idea and purposeful transitions. Premium means controlled movement and good timing, not constant movement everywhere.
- Native sticky scrolling with independently transformed planes can provide depth without WebGL or a generated video. Choose heavier tools only when they improve the requested experience.
- Keep essential copy and calls to action as semantic HTML. Establish deliberate layer order for typography, subject, foreground, and atmosphere.
- Prefer composited transforms and opacity, a shared scroll progress value, and requestAnimationFrame updates. Avoid rendering the whole page again on every scroll tick.
- Subtle pointer response is optional. It must not be the only way to experience the hero. Never capture or lock the desktop cursor for this effect.
- Pause offscreen ambient work. Honor reduced-motion preferences and provide a usable static composition without extra pinned scroll space or hidden essential content.
- Load the required scene assets together before switching from a complete poster fallback. Avoid partial scenes, flashes, ghost subjects, or a broken hero when a layer fails.

## Art-direct mobile separately

Do not merely shrink the desktop composition. Adjust crop, subject position, contact-point pivot, type size, layer order, travel, and scroll duration as needed. Typography may sit above the subject on mobile even when it passes behind the subject on desktop. Preserve depth without hiding the headline, pushing the subject offscreen, or causing horizontal overflow.

## Acceptance

Use the verification tools and permissions applicable to the build. Inspect the actual opening, an intermediate scroll position, the final hero transition, and the mobile composition. Check:

- Distinct layers visibly move at different rates.
- No duplicate subjects, holes, cutout halos, floating contact points, or abrupt seams appear.
- The opening headline is readable, and the scroll produces a clear change in what the visitor sees or understands.
- The scene resolves cleanly into the next section.
- Mobile and motion-off states remain complete and usable.
- Assets load, and controls still work.

A successful build or transform unit test alone does not prove the visual effect is good. If visual verification could not be performed, state that limit instead of claiming it was checked.

## Approved example: Sonder Studio

On September 4, 2026, the original single-image hero was judged premium but lacking layers and wow factor. The approved revision has separate mountains, rear clouds, woman and red dress, volcanic ground, and foreground mist; different depth rates; a shared foot anchor; typography behind the subject; a second scroll beat; and a gallery-frame transition.

Local implementation, when available: `OtherWorlds/sonder-studio/` in the author’s optional example workspace. See `components/SceneHero.tsx`, `lib/scene-motion.ts`, and `VERIFICATION.md` for the worked example. The principles above are self-contained; this path is optional reference material.


## Further approved examples

The [ten-site rebuild](approved-collection.md) extends these principles to
products, community, software, advisory, hospitality, objects, and editorial
experiences. Read it for rendering choices and the complete delivery gate.
