---
name: bearcarousel
description: Turn a topic into finished Instagram carousel slides — aligned, audited 1080x1350 PNGs. Use when the user asks to 做輪播/做 IG 輪播圖/把主題或筆記變成輪播貼文, or to make/draft/restyle an Instagram carousel. Works in ANY directory; bootstraps the BearCarousel engine on first use, then drives its CLI and live-linked browser UI.
---

# BearCarousel — 主題進，成品輪播出 / topic in, finished carousel out

BearCarousel renders aligned 1080x1350 slides from a content JSON, audits what
it drew (alignment, WCAG contrast, copy limits, logo, readability), and exits
non-zero on any RED finding. Your job is to drive it end to end for a user who
never touches a terminal.

## 1. Bootstrap（第一次在這台機器上用）

Check once: `bearcarousel --version`. If missing:

```bash
pip install bearcarousel
```

Fallback if PyPI is unreachable:
`pip install "bearcarousel @ git+https://github.com/johnnyang0612/bearcarousel#subdirectory=pypi"`
— or clone https://github.com/johnnyang0612/bearcarousel and use
`python cli.py` / `python run.py` directly from the checkout.

The first `bearcarousel` command downloads the matching app tree (rulepack,
templates, fonts, UI) into `~/.bearcarousel/app/<version>/`. Everything after
that is offline.

## 2. Read the app's own instruction set, then follow it

The complete playbook ships WITH the app — do not improvise from this skill:

- `~/.bearcarousel/app/<version>/AGENTS.md`（English, canonical）
- `~/.bearcarousel/app/<version>/AGENTS.zh-TW.md`（繁體中文）

Read it and follow **Part B**: read the rulepack in `rules/base/` → choose
高速版 (fast, needs one real anchor) or 進階訪談版 (interview, per-slide
probes from `rules/base/interview.json`) → draft → render → audit → fix every
RED → hand off.

All CLI verbs are proxied by the launcher, from any directory:

```bash
bearcarousel draft  --framework case --slides 8 --out my_post.json
bearcarousel render --content my_post.json --json
bearcarousel audit  --content my_post.json --json     # exit 1 on RED
bearcarousel open   --content my_post.json            # live-linked browser UI
bearcarousel export --content my_post.json --out deck.zip --json
bearcarousel ui                                       # bare UI, no content
```

`open` is the handoff: the user's browser gets the deck live-linked — you
rewrite the content file and the UI hot-reloads; they edit words and the file
updates for you to re-read. Run it in the background; always re-read the file
before rewriting it.

## Hard floor (before you even read AGENTS.md)

- **Never invent a number, case, or quote.** Real material comes from the
  user; missing figures are written as 【待補：…】, never filled in.
- **Never deliver with a RED finding.** Exit code 1 from audit/export means
  not done.
- **No emoji in slide content** — the renderer draws them as tofu and the
  audit hard-blocks.
