# Inputs and missing files

Read this at the start, when `landing/brief.md` or `landing/copy.md` is
missing, or when the client has brand rules or an existing stylesheet.

## No brief

Ask for the minimum before deciding anything: what it is, who it is for,
what single action a visitor should take, which assets exist (photos,
screenshots, logo), and which brand rules are fixed (colours, fonts, logo).
Write the answers to `landing/brief.md`.

## No copy

The correct order is copy first, because the number and length of sections
decide the layout. When you can, run `landing-skills:landing-copy` first.

When you cannot (the user wants to go ahead, or that skill is not
available), work from the brief:

- Write `(provisional: no copy.md yet)` at the top of the `Layout` section of
  `direction.md`, and revisit the section map when the copy exists.
- Steps that need the copy's key message (the "must believe" line, the sound
  of the copy, the hero headline length) take it from the brief's product,
  audience and conversion action instead. Write your own one-line version,
  mark it provisional, and check it against the copy when it arrives. If the
  copy's message differs, revisit the direction, not only the layout.

## Existing brand rules

They win. Use the client's colours and faces, record them as given, and
spend your decisions on what the rules leave open (scale, neutrals, layout,
imagery). If a brand colour is a default hue such as Tailwind indigo, keep it
and write that it is the client's colour. If a brand font has a paid
licence, ask the user to confirm they hold a web licence.

## Existing tokens or stylesheet

Read them. Keep values that carry a decision, and replace values that are
framework defaults.
