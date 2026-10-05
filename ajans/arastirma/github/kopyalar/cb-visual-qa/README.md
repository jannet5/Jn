# cb-visual-qa

A Claude Code slash command that adds visual QA to any project — Playwright smoke tests that verify pages actually render correctly (not just compile), a `design.md` aesthetic reference, and a `PROMPT_visual-check.md` for Ralph Wiggum loops.

## The Problem

AI-generated frontends have a specific failure mode: they **compile clean, pass type checks, and even pass unit tests — but look broken when you actually run them**:

- Blank white screens (missing `"use client"`, hydration errors)
- Unstyled content (Tailwind purged dynamic classes, missing CSS import)
- Broken layouts (wrong flex direction, missing container)
- Wrong colours/fonts (CSS variables not resolved, missing Tailwind config tokens)
- Runtime JS errors not caught at build time

This skill catches all of these before you ever open a browser.

## What It Sets Up

| File | Purpose |
|------|---------|
| `design.md` | Your design system — colours, typography, spacing, components. Every AI iteration reads this to stay consistent. |
| `tests/visual/smoke.spec.ts` | Playwright tests checking real rendering: no blank pages, no JS errors, no layout collapse, screenshot baselines. |
| `playwright.config.ts` | Playwright config wired to your dev server. |
| `PROMPT_visual-check.md` | Ralph loop prompt for dedicated visual review passes. |

## Installation

Copy the slash command to your Claude Code commands directory:

```bash
# macOS/Linux
cp commands/cb-visual-qa.md ~/.claude/commands/

# Windows (PowerShell)
copy commands\cb-visual-qa.md $env:USERPROFILE\.claude\commands\
```

## Usage

In any project with a frontend, run:

```
/cb-visual-qa
```

Claude will ask you for:
1. The key routes to test (e.g. `/`, `/dashboard`, `/login`)
2. Your dev server command and port
3. A brief description of your design aesthetic (optional)

Then it generates all files above, installs Playwright, and walks you through creating baseline screenshots.

## Running Visual Tests

```powershell
# Install browser (first time only)
npx playwright install chromium --with-deps

# Create baselines (first time — INSPECT THESE before committing)
npm run test:visual:update

# Run tests after any UI change
npm run test:visual

# Open HTML report on failure
npx playwright show-report
```

## Using with Ralph Wiggum

Once set up, the `cb-ralph-wiggum` skill automatically runs visual tests after each UI task (via step 2b in `PROMPT_build.md`), provided `AGENTS.md` lists the visual test command.

For a dedicated visual review pass after several build iterations:

```powershell
# PowerShell
.\loop.ps1 visual    # if you add a visual mode to your loop
# or:
Get-Content PROMPT_visual-check.md | claude -p --dangerously-skip-permissions --model sonnet
```

## What the Tests Check

Each route test verifies:

1. **No JS console errors** — catches runtime failures invisible at build time
2. **No uncaught exceptions** — catches React render errors, null reference errors
3. **Visible content** — body is not empty (catches blank white screen)
4. **No error overlays** — catches Next.js error boundary triggers
5. **Meaningful height** — layout has not collapsed to 0px
6. **Main content visible** — catches missing root elements
7. **Screenshot baseline** — catches visual regressions between iterations

## What `design.md` Does

By committing your design intent as a file, every Claude iteration can read it with fresh context. Without it, each Ralph iteration has no memory of your aesthetic decisions and will drift toward generic defaults. With it:

- Colours stay consistent across components
- Spacing follows your defined scale
- Components are built to your documented patterns
- Deviations get flagged in the visual check prompt

## License

MIT
