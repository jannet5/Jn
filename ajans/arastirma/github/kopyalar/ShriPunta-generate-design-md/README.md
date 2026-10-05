# generate-design-md

A [Claude Code](https://docs.anthropic.com/en/docs/claude-code) skill that generates, edits, validates, and exports `DESIGN.md` files — the AI-readable design system format.

## What is DESIGN.md?

`DESIGN.md` is a plain-text design system document for AI agents. It combines YAML front matter with machine-readable design tokens (colors, typography, spacing, components) and a markdown body with human-readable design rationale.

Think of it as the design counterpart to `AGENTS.md`:

| File | Who reads it | What it defines |
| :--- | :--- | :--- |
| `README.md` | Humans | What the project is |
| `AGENTS.md` | Coding agents | How to build the project |
| `DESIGN.md` | Design agents | How the project should look and feel |

When a design agent reads your `DESIGN.md`, every screen it generates follows the same visual rules — your color palette, your typography, your component patterns.

## What this skill does

- **Generate** a `DESIGN.md` from a description, vibe, or by scanning your existing codebase for design signals
- **Edit** an existing `DESIGN.md` — update tokens, add components, adjust typography
- **Validate** with the `@google/design.md` linter — checks broken token references, WCAG contrast ratios, section order, and more
- **Export** tokens to Tailwind CSS or W3C DTCG format

## Screenshots

**Choosing a generation path:**

![Generation path prompt](assets/ask-a-question-design-md.png)

**Scanning a codebase, generating, and linting in one shot:**

![Scan, generate, and lint](assets/scan-generate-lint-design.md.png)

## Install

Clone this repo directly into your skills directory:

```bash
git clone https://github.com/shripunta/generate-design-md.git ~/.claude/skills/generate-design-md
```

## Usage

Type `/generate-design-md` in the skill menu in Claude Code (or any other agent that supports skills) to invoke the skill.

The agent will ask how you want to generate the design system:

(Option 1.) **Describe it** — tell the agent the vibe, brand, or aesthetic
(Option 2.) **Scan the codebase** — agent extracts colors, fonts, and spacing from your existing code

After generating, the agent automatically runs the linter and fixes any errors.

## Example output

```yaml
---
name: DevFocus Dark
colors:
  primary: "#2665fd"
  secondary: "#475569"
  surface: "#0b1326"
  on-surface: "#dae2fd"
typography:
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: 400
rounded:
  md: 8px
---

## Overview
A focused, minimal dark interface for a developer productivity tool.

## Colors
- **Primary** (#2665fd): CTAs, active states, key interactive elements
- **Surface** (#0b1326): Page backgrounds
```

## CLI tool

The skill uses `@google/design.md` for linting and exporting. You can also run it directly:

```bash
# Lint
npx @google/design.md lint DESIGN.md

# Export to Tailwind
npx @google/design.md export --format tailwind DESIGN.md

# Export to W3C DTCG tokens.json
npx @google/design.md export --format dtcg DESIGN.md
```

## References

- [`references/what-is-design-md.md`](references/what-is-design-md.md) — concept overview
- [`references/spec.md`](references/spec.md) — full token schema and section structure
- [`references/linting-rules.md`](references/linting-rules.md) — all 8 lint rules with resolutions
- [`references/validate-with-cli.md`](references/validate-with-cli.md) — full CLI reference
- [`references/examples/`](references/examples/) — example DESIGN.md files:
  - [`dev-focus-dark.design.md`](references/examples/dev-focus-dark.design.md) — minimal dark theme for a developer tool
  - [`saas-dashboard-light.design.md`](references/examples/saas-dashboard-light.design.md) — professional light-mode B2B dashboard
  - [`consumer-app-playful.design.md`](references/examples/consumer-app-playful.design.md) — vibrant, rounded consumer mobile app
  - [`editorial-minimal.design.md`](references/examples/editorial-minimal.design.md) — print-inspired editorial / long-form blog

## Attribution

The `DESIGN.md` format, specification, linting rules, and CLI (`@google/design.md`) are created and open-sourced by Google. Full documentation at [stitch.withgoogle.com/docs/design-md](https://stitch.withgoogle.com/docs/design-md/overview/). Source: [github.com/google/design.md](https://github.com/google/design.md).

This skill packages that specification as a Claude Code skill for convenient in-agent use. `SKILL.md` and example files are original work by Shridhar Puntambekar.

## License

Apache 2.0 — see [LICENSE](LICENSE).

- `references/` — Copyright 2025 Google LLC
- `SKILL.md`, `references/examples/` — Copyright 2026 Shridhar Puntambekar
