---
version: alpha
name: Kavrulmuş Çekirdek Satış Paneli
description: Bir kafe zincirinin haftalık satış paneli için sıcak, okunaklı, veri yoğun tasarım sistemi.
colors:
  primary: "#7A3E1D"
  on-primary: "#FFFFFF"
  primary-hover: "#5E2E14"
  primary-container: "#F3DCC8"
  on-primary-container: "#5A2C12"
  secondary: "#6B5547"
  tertiary: "#C8662B"
  neutral: "#F7F2EC"
  background: "#F7F2EC"
  on-background: "#2B1D14"
  surface: "#FFFFFF"
  surface-muted: "#EFE6DC"
  on-surface: "#2B1D14"
  on-surface-muted: "#6B5547"
  outline: "#E2D6CA"
  focus-ring: "#C8662B"
  sidebar: "#2B1D14"
  on-sidebar: "#F7F2EC"
  sidebar-active: "#4A3427"
  on-sidebar-muted: "#CDBBAA"
  chart-bar: "#C8662B"
  chart-bar-muted: "#E8C4A6"
  success: "#2F6B3A"
  success-container: "#DDEFD9"
  on-success-container: "#1E4A27"
  warning-container: "#FBEBC4"
  on-warning-container: "#6B4A00"
  error: "#A3302A"
  error-container: "#F8DCD8"
  on-error-container: "#7A1F1A"
typography:
  headline-lg:
    fontFamily: Fraunces
    fontSize: 28px
    fontWeight: 600
    lineHeight: 1.2
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Fraunces
    fontSize: 20px
    fontWeight: 600
    lineHeight: 1.3
  metric-value:
    fontFamily: Fraunces
    fontSize: 30px
    fontWeight: 600
    lineHeight: 1.1
    letterSpacing: -0.01em
    fontFeature: "'tnum' 1, 'lnum' 1"
  body-md:
    fontFamily: Manrope
    fontSize: 15px
    fontWeight: 400
    lineHeight: 1.5
  body-sm:
    fontFamily: Manrope
    fontSize: 13px
    fontWeight: 400
    lineHeight: 1.45
  label-md:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: 600
    lineHeight: 1.3
  label-sm:
    fontFamily: Manrope
    fontSize: 12px
    fontWeight: 600
    lineHeight: 1.3
    letterSpacing: 0.04em
  data-md:
    fontFamily: Manrope
    fontSize: 14px
    fontWeight: 500
    lineHeight: 1.4
    fontFeature: "'tnum' 1"
rounded:
  none: 0px
  sm: 6px
  md: 10px
  lg: 16px
  full: 9999px
spacing:
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  gutter: 24px
  margin-mobile: 16px
  sidebar-width: 240px
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    typography: "{typography.label-md}"
    rounded: "{rounded.full}"
    padding: 10px
    height: 40px
  button-primary-hover:
    backgroundColor: "{colors.primary-hover}"
    textColor: "{colors.on-primary}"
  button-segment:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface-muted}"
    typography: "{typography.label-md}"
    rounded: "{rounded.full}"
    padding: 8px
    height: 36px
  button-segment-active:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
  nav-item:
    backgroundColor: "{colors.sidebar}"
    textColor: "{colors.on-sidebar-muted}"
    typography: "{typography.label-md}"
    rounded: "{rounded.md}"
    padding: 12px
  nav-item-active:
    backgroundColor: "{colors.sidebar-active}"
    textColor: "{colors.on-sidebar}"
  card:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface}"
    rounded: "{rounded.lg}"
    padding: 24px
  card-metric-value:
    textColor: "{colors.on-surface}"
    typography: "{typography.metric-value}"
  table-header:
    backgroundColor: "{colors.surface-muted}"
    textColor: "{colors.on-surface-muted}"
    typography: "{typography.label-sm}"
    padding: 12px
  table-row:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface}"
    typography: "{typography.data-md}"
    padding: 12px
  table-row-hover:
    backgroundColor: "{colors.neutral}"
    textColor: "{colors.on-surface}"
  badge-success:
    backgroundColor: "{colors.success-container}"
    textColor: "{colors.on-success-container}"
    typography: "{typography.label-sm}"
    rounded: "{rounded.full}"
    padding: 4px
  badge-warning:
    backgroundColor: "{colors.warning-container}"
    textColor: "{colors.on-warning-container}"
    typography: "{typography.label-sm}"
    rounded: "{rounded.full}"
    padding: 4px
  badge-error:
    backgroundColor: "{colors.error-container}"
    textColor: "{colors.on-error-container}"
    typography: "{typography.label-sm}"
    rounded: "{rounded.full}"
    padding: 4px
  badge-neutral:
    backgroundColor: "{colors.primary-container}"
    textColor: "{colors.on-primary-container}"
    typography: "{typography.label-sm}"
    rounded: "{rounded.full}"
    padding: 4px
  delta-up:
    textColor: "{colors.success}"
    typography: "{typography.label-sm}"
  delta-down:
    textColor: "{colors.error}"
    typography: "{typography.label-sm}"
  chart-bar:
    backgroundColor: "{colors.chart-bar}"
    rounded: "{rounded.sm}"
  chart-bar-muted:
    backgroundColor: "{colors.chart-bar-muted}"
    rounded: "{rounded.sm}"
  focus-indicator:
    backgroundColor: "{colors.focus-ring}"
    size: 3px
---

# Kavrulmuş Çekirdek Satış Paneli

## Overview

A warm, calm operations dashboard for a café chain's weekly sales review. The
audience is regional managers and branch leads who open it on a laptop on Monday
morning or on a phone between shifts. The feel is **"roastery ledger"**: the
warmth of roasted coffee and steamed milk on top of a disciplined, numbers-first
layout. Information is dense but never cramped; every figure should be readable
at a glance and comparable to the previous period.

## Colors

The palette is drawn from the coffee bar: espresso, caramel and milk foam, with
restrained functional colors for status.

- **Primary (#7A3E1D) — Roasted Bean:** the single interactive color. Used for
  primary buttons, the active date-range segment and links.
- **Secondary (#6B5547) — Mocha Grey:** secondary text, captions, table headers.
- **Tertiary (#C8662B) — Caramel:** data color for chart bars and the keyboard
  focus ring. Never used for body text.
- **Neutral (#F7F2EC) — Milk Foam:** the page background; white surfaces sit on it.
- **Sidebar (#2B1D14) — Espresso:** the navigation rail, with Milk Foam text.
- **Status:** green (`success`) for positive change and completed orders, amber
  (`warning-container`) for orders in preparation, red (`error`) for negative
  change and refunds/cancellations. Status is always paired with a text label or
  arrow, never color alone.

## Typography

Two families: **Fraunces** (a warm, slightly old-style serif) for headlines and
metric values, giving the café character; **Manrope** for all UI text, labels and
table data. Numeric styles (`metric-value`, `data-md`) enable tabular, lining
figures so currency columns align. Small labels (`label-sm`) use light positive
tracking and may be uppercase.

## Layout

A fixed 240px espresso sidebar on desktop; content area uses a 24px gutter and
an 8px-based spacing scale (`xs` 4 → `xl` 32). Metric cards sit in a 4-column
grid that collapses to 2 columns on tablet and 1–2 columns at 390px. On mobile
the sidebar becomes a horizontally scrollable top navigation, the margin drops to
16px, and the orders table scrolls horizontally inside its card.

## Elevation & Depth

Mostly flat. Hierarchy comes from **tonal layering**: Milk Foam page, white
cards with a 1px `outline` border. No heavy shadows; at most a faint warm shadow
on cards. Focus is shown with a 3px Caramel outline offset by 2px.

## Shapes

Soft and friendly: cards use `lg` (16px), nav items and inputs `md` (10px),
buttons, segments and badges are fully pill-shaped (`full`). Chart bars use
`sm` (6px) top corners.

## Components

- **Buttons:** pill-shaped; primary is Roasted Bean with white text. The date
  range selector is a segmented control of `button-segment` items; the active
  one uses `button-segment-active`.
- **Navigation:** `nav-item` on the espresso sidebar; the current page uses
  `nav-item-active` and `aria-current="page"`.
- **Cards:** white, 16px radius, 24px padding; metric cards show a label, a
  `metric-value`, and a `delta-up`/`delta-down` line with an arrow and the
  previous-period comparison.
- **Table:** `table-header` row on Latte (`surface-muted`), `table-row` body
  rows with hover tint, amounts right-aligned in tabular figures.
- **Badges:** pill status badges — `badge-success` (Tamamlandı),
  `badge-warning` (Hazırlanıyor), `badge-error` (İade/İptal), `badge-neutral`
  (Yolda/other).

## Do's and Don'ts

- Do use Roasted Bean only for interactive elements and the active state.
- Do pair every status color with a text label or arrow symbol.
- Do keep all text at WCAG AA (4.5:1) or better against its background.
- Do show a visible Caramel focus ring on every focusable element.
- Don't use Caramel for text; it is a data and focus color only.
- Don't add drop shadows heavier than the faint card shadow.
- Don't mix sharp and rounded corners in the same view.
