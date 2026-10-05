---
name: cb-visual-qa
author: ChrisBrooksbank
description: Add visual QA to any project — Playwright smoke tests that verify pages actually render correctly (not just compile), a design.md system reference, and a PROMPT_visual-check.md for use in Ralph Wiggum loops
---

# Visual QA Setup

This skill adds two layers of protection against the "compiles fine but looks broken" problem:

1. **Playwright smoke tests** — automated browser tests that load each key page and check for visible content, no JS errors, and no layout collapse
2. **design.md** — a persistent design system reference that every AI iteration can read to stay consistent with your intended aesthetic
3. **PROMPT_visual-check.md** — a standalone Ralph loop prompt that runs a dedicated visual review pass

## The Problem This Solves

AI-generated code has a specific failure mode: it passes all type checks, builds cleanly, and even passes unit tests — but when you actually run the app you see:
- Blank white screens (missing "use client" in Next.js, hydration errors)
- Unstyled content (Tailwind dynamic classes purged, missing CSS import)
- Broken layouts (wrong flex direction, missing container, viewport overflow)
- Wrong colours or fonts (CSS variables not resolved, wrong Tailwind config)
- Console errors that indicate runtime failures not caught at build time

Playwright smoke tests catch all of these. `design.md` prevents Claude from drifting away from your intended aesthetic between iterations.

## Before Setup

**Step 1: Check existing state**

- Read `package.json` to detect framework (Next.js, Vite, plain HTML, etc.)
- Check if Playwright is already installed (`@playwright/test` in devDependencies)
- Check if `design.md` already exists
- Read `AGENTS.md` if present (to know existing test commands)

**Step 2: Interview the user**

Use AskUserQuestion to gather:

1. **Key routes**: "What are the 2-5 most important pages/routes to test? (e.g. `/`, `/dashboard`, `/login`)"

2. **Dev server command and port**: "What command starts your dev server and what port does it use? (e.g. `npm run dev` on port 3000)"

3. **Design intent** (optional): "Describe your design aesthetic in a sentence or two — colours, vibe, style. Or say 'skip' to generate a placeholder."

**Step 3: Generate files**

---

## Files to Generate

### 1. design.md

This file defines your visual intent. Every AI iteration should read it before touching UI code.

Template (fill in from user answers or make reasonable defaults for their stack):

```markdown
# Design System

> Read this before writing any UI code. This is the source of truth for visual intent.

## Aesthetic

[One sentence: e.g. "Clean, minimal SaaS — dark sidebar, white main area, subtle shadows."]

## Colours

| Role | Value | Notes |
|------|-------|-------|
| Primary | `#2563EB` | Blue — buttons, links, active states |
| Background | `#FFFFFF` | Page background |
| Surface | `#F8FAFC` | Cards, panels |
| Border | `#E2E8F0` | Dividers, input borders |
| Text primary | `#0F172A` | Headings, body |
| Text secondary | `#64748B` | Labels, captions |
| Error | `#DC2626` | Validation errors |
| Success | `#16A34A` | Confirmations |

## Typography

- **Font**: System UI stack or [specify font name]
- **Base size**: 16px
- **Scale**: 12 / 14 / 16 / 18 / 24 / 30 / 36px
- **Headings**: Semi-bold (600), tight leading
- **Body**: Regular (400), relaxed leading (1.6)

## Spacing

- Base unit: 4px
- Component padding: 16px (sm) / 24px (md) / 32px (lg)
- Section gaps: 48px / 64px / 96px
- Max content width: 1280px, centered

## Component Patterns

### Buttons
- Primary: filled, `bg-primary`, white text, rounded-md, px-4 py-2
- Secondary: outlined, `border-border`, rounded-md
- Destructive: `bg-error` or outlined with error colour
- States: hover (darken 10%), focus (ring-2 ring-primary), disabled (opacity-50)

### Cards
- `bg-surface`, `border border-border`, `rounded-lg`, `shadow-sm`
- Padding: 24px

### Forms
- Labels above inputs
- Input: `border border-border rounded-md px-3 py-2`, focus ring
- Error text: small, `text-error`, below the input

### Navigation
- [Describe your nav pattern: top bar / sidebar / both]

## Layout

- [Describe: single column / two-column / sidebar + main, etc.]
- Mobile-first, breakpoints: sm (640), md (768), lg (1024), xl (1280)

## What NOT to Do

- Don't use inline styles for anything in design.md
- Don't hardcode hex values — use CSS variables or Tailwind config tokens
- Don't mix dark/light mode classes arbitrarily
- Don't use `!important`
- Don't create one-off colours outside this palette
```

### 2. tests/visual/smoke.spec.ts (or .js)

Playwright tests that verify real rendering, not just compilation.

**Critical principle:** These tests check that pages are ACTUALLY working, not just that they load. A blank white page with no errors still FAILS these tests.

Generate based on the user's framework and routes. Template for Next.js/React:

```typescript
import { test, expect } from '@playwright/test';

// Routes provided by user during setup
const ROUTES = [
  { path: '/', name: 'Home' },
  // Add more routes here
];

for (const route of ROUTES) {
  test(`${route.name} (${route.path}) renders visible content`, async ({ page }) => {
    // Capture console errors
    const consoleErrors: string[] = [];
    page.on('console', msg => {
      if (msg.type() === 'error') consoleErrors.push(msg.text());
    });

    // Capture uncaught exceptions
    const pageErrors: string[] = [];
    page.on('pageerror', err => pageErrors.push(err.message));

    await page.goto(route.path);

    // 1. No JS errors or uncaught exceptions
    expect(consoleErrors.filter(e => !e.includes('favicon'))).toEqual([]);
    expect(pageErrors).toEqual([]);

    // 2. Page has visible content — not blank white
    const body = page.locator('body');
    await expect(body).not.toBeEmpty();

    // 3. No "Error" text visible on screen (Next.js error overlay, React error boundary)
    await expect(page.getByText(/something went wrong/i)).not.toBeVisible();
    await expect(page.getByText(/application error/i)).not.toBeVisible();

    // 4. Page has meaningful rendered height — catches blank/collapsed layouts
    const bodyHeight = await body.evaluate(el => el.scrollHeight);
    expect(bodyHeight).toBeGreaterThan(100);

    // 5. No obviously broken layout: check main content area exists
    // Adjust selector to match your app's main content wrapper
    const mainContent = page.locator('main, [role="main"], #root > *, #__next > *').first();
    await expect(mainContent).toBeVisible();

    // 6. Visual snapshot — catches style regressions (update with: npx playwright test --update-snapshots)
    await expect(page).toHaveScreenshot(`${route.name.toLowerCase().replace(/\s+/g, '-')}.png`, {
      fullPage: false,
      animations: 'disabled',
    });
  });
}

test('No broken images on homepage', async ({ page }) => {
  const failedImages: string[] = [];
  page.on('response', response => {
    if (response.request().resourceType() === 'image' && !response.ok()) {
      failedImages.push(response.url());
    }
  });
  await page.goto('/');
  await page.waitForLoadState('networkidle');
  expect(failedImages).toEqual([]);
});
```

### 3. playwright.config.ts (or .js)

Only create if none exists. Sensible defaults:

```typescript
import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests/visual',
  fullyParallel: false,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 1 : 0,
  workers: 1,
  reporter: 'html',
  use: {
    baseURL: 'http://localhost:3000',  // update to match user's port
    trace: 'on-first-retry',
    screenshot: 'only-on-failure',
  },
  projects: [
    {
      name: 'chromium',
      use: { ...devices['Desktop Chrome'] },
    },
  ],
  webServer: {
    command: 'npm run dev',  // update to match user's dev command
    url: 'http://localhost:3000',
    reuseExistingServer: !process.env.CI,
    timeout: 120_000,
  },
  snapshotPathTemplate: 'tests/visual/__screenshots__/{testFilePath}/{arg}{ext}',
});
```

### 4. PROMPT_visual-check.md

A dedicated Ralph loop prompt for visual review passes. Run this after `build` iterations when UI has changed:

```markdown
# VISUAL CHECK MODE

You are in visual check mode. Your job is to verify the app looks correct and matches design.md, then fix any issues found.

## 0a. Read Context

Read these files in parallel:
- `design.md` — the visual intent
- `AGENTS.md` — build and test commands
- `IMPLEMENTATION_PLAN.md` — what was recently built

## 1. Run Visual Tests

Run the visual test command from AGENTS.md (e.g. `npm run test:visual`).

If this is the first run and no baseline screenshots exist:
1. Start the dev server if needed
2. Run `npx playwright test --update-snapshots` to create baselines
3. Inspect the generated screenshots in `tests/visual/__screenshots__/`

## 2. Analyse Failures

For each failing test, determine the root cause:

**Blank/empty page** → Check for:
- Missing `"use client"` directive in Next.js components using hooks
- Hydration mismatch (server/client state differs)
- JavaScript error preventing render (check console errors in test output)
- Missing CSS import

**Broken layout** → Check for:
- Missing flex/grid container
- Wrong CSS class names (Tailwind purging dynamic classes)
- Overflow hidden cutting content
- Wrong z-index stacking

**Wrong colours/fonts** → Check for:
- CSS variable not defined in `:root`
- Tailwind config missing colour token
- Font not loaded (check network tab in test trace)

**Screenshot diff** → Check for:
- Intentional change (update snapshot if correct)
- Unintentional regression (fix the code)

## 3. Fix Issues

Fix each issue found. After fixing:
1. Re-run `npm run test:visual`
2. If tests pass, continue
3. If tests still fail, investigate further

## 4. Cross-check with design.md

Even if all tests pass, open the screenshots and compare against design.md:
- Are the colours right?
- Is the typography consistent?
- Do components match the documented patterns?
- Is spacing correct?

Note any deviations in IMPLEMENTATION_PLAN.md under ## Notes.

## 5. Update AGENTS.md if needed

If you discovered the visual test command or setup needed adjustment, update AGENTS.md.

## 6. Exit

After all visual tests pass and the design matches intent, exit cleanly.

---

## 99999. GUARDRAILS

- **DON'T update snapshots blindly** — inspect them first, update only if the change is correct
- **DON'T skip the design.md check** — automated tests catch crashes, not aesthetic drift
- **DON'T mark as done if screenshots show blank/broken pages** — tests may have passed on error due to wrong selectors
- **DO check the Playwright HTML report** (`playwright-report/index.html`) for detailed failure info
- **DO look at actual screenshot files** — reading code alone won't tell you if it looks right
```

### 5. Update AGENTS.md

Add visual test commands to `AGENTS.md`. If AGENTS.md doesn't exist, create it:

```markdown
## Visual Test Commands

```bash
npx playwright install chromium --with-deps  # First time only
npm run test:visual                          # Run visual tests
npm run test:visual:update                   # Update baseline screenshots
npx playwright show-report                  # Open HTML report
```
```

Add these scripts to `package.json`:
```json
"test:visual": "playwright test",
"test:visual:update": "playwright test --update-snapshots"
```

---

## Post-Setup Instructions

After generating all files:

### First Run

```powershell
# Install Playwright browser (first time only)
npx playwright install chromium --with-deps

# Create baseline screenshots (first time only — inspect these!)
npm run test:visual:update

# View the baselines
# Look in: tests/visual/__screenshots__/
```

### Ongoing Usage

```powershell
# Run after any UI changes
npm run test:visual

# If a change is intentional, update the baseline
npm run test:visual:update

# View the HTML report on failure
npx playwright show-report
```

### Using with Ralph Loop

Add this to `AGENTS.md` so Ralph picks it up automatically via step 2b:

```
## Visual Test Commands
npm run test:visual    # Run after any UI task
```

For a dedicated visual review pass (run after several build iterations):

```powershell
# PowerShell
Get-Content PROMPT_visual-check.md | claude -p --dangerously-skip-permissions --model sonnet
```
```bash
# Bash
cat PROMPT_visual-check.md | claude -p --dangerously-skip-permissions --model sonnet
```

### What Good Looks Like

After running `npm run test:visual:update`, open `tests/visual/__screenshots__/` and check:
- Pages have visible content (not blank white)
- Layout matches your design intent
- Navigation is present
- No error overlays

If any screenshot looks wrong at this point, fix it BEFORE committing — these become your regression baseline.

---

## Attribution

Built to solve the "compiles but looks broken" problem common with AI-generated frontends. Uses Playwright Test for browser automation.
