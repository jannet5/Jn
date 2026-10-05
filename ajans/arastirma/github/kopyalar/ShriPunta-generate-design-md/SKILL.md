---
name: generate-design-md
description: "Generate, edit, validate, and export DESIGN.md files — the AI-readable design system format combining YAML design tokens (colors, typography, spacing, components) with markdown prose. Use when the user wants to: (1) create a DESIGN.md from scratch (from a description, vibe, or brand URL/image), (2) edit or update an existing DESIGN.md, (3) lint/validate a DESIGN.md with the CLI, (4) export tokens to Tailwind or DTCG format, or (5) understand the DESIGN.md spec/format."
---

DESIGN.md is a plain-text design system document for AI agents — YAML front matter with machine-readable tokens + markdown body with human-readable rationale. See `references/what-is-design-md.md` for concept overview.

Read `references/spec.md` and `references/examples/dev-focus-dark.design.md` before generating.

## Step 1: Choose a path

If the user hasn't already indicated how they want to generate the DESIGN.md, ask them to pick one:

> "How would you like to generate the DESIGN.md?
> 1. **Describe it** — tell me the vibe, brand, or aesthetic and I'll generate one
> 2. **Scan the codebase** — I'll extract colors, fonts, and spacing from your existing code"

### Path A: Describe it

Ask the user one question:

> "Describe the look and feel you're going for — colors, mood, typography style, any references or inspirations."

Use their answer to infer all tokens. Don't ask follow-up questions; make reasonable design decisions and note them in the output. Proceed to Step 2.

### Path B: Scan the codebase

Scan the project for design signals. Look for:
- CSS/SCSS files: custom properties (`--color-*`, `--font-*`, `--spacing-*`), hardcoded hex values, font-family declarations
- Tailwind config (`tailwind.config.js/ts`): `theme.colors`, `theme.fontFamily`, `theme.spacing`, `theme.borderRadius`
- Component files (`.tsx`, `.jsx`, `.vue`, `.svelte`): inline styles, className patterns, repeated color/font values
- Any existing design tokens file (`tokens.json`, `tokens.js`, etc.)

Extract as many token values as possible before generating. If the codebase has no design signals at all, fall back to Path A and ask for a description. Proceed to Step 2.

## Step 2: Generate the DESIGN.md

**Format**: YAML front matter first, then markdown body in canonical section order.

Build tokens in this order: `colors` → `typography` → `rounded` → `spacing` → `components`

Write the markdown body sections: Overview, Colors, Typography, Layout, Elevation & Depth, Shapes, Components, Do's and Don'ts. Omit sections with nothing meaningful to say.

Write the file to `DESIGN.md` in the project root.

## Step 3: Lint and fix

Run the linter immediately after writing:

```bash
npx @google/design.md lint DESIGN.md
```

Fix all `error`-severity findings automatically. For `warning`-severity findings, fix them too unless doing so would contradict the user's explicit intent. Re-run until exit code 0. Show the user the final lint summary.

See `references/linting-rules.md` for all 8 rules. Key constraints:
- Always define a `primary` color
- Component token references must resolve (e.g., `{colors.primary}` must exist in `colors`)
- Only use recognized component sub-token properties: `backgroundColor`, `textColor`, `typography`, `rounded`, `padding`, `size`, `height`, `width`
- Component `backgroundColor` + `textColor` pairs must meet WCAG AA 4.5:1 contrast

## Exporting tokens

```bash
npx @google/design.md export --format tailwind DESIGN.md   # → tailwind.config.js theme.extend
npx @google/design.md export --format dtcg DESIGN.md       # → W3C tokens.json
```

See `references/validate-with-cli.md` for full CLI reference including `diff` and programmatic API.
