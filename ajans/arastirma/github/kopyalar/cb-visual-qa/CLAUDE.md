# CLAUDE.md

## Project Overview

A Claude Code slash command skill (`/cb-visual-qa`) that adds visual QA infrastructure to any project — Playwright smoke tests verifying pages actually render, a `design.md` aesthetic reference file, and a `PROMPT_visual-check.md` for Ralph Wiggum loops.

## Repository Structure

This is a Claude Code skill repository, not a runnable application.

- `commands/cb-visual-qa.md` — The slash command prompt that Claude Code executes

## Installation / Usage

Users copy the command file to their Claude Code commands directory and invoke `/cb-visual-qa` in any project.

## Development

To modify the skill, edit `commands/cb-visual-qa.md`. The command is a Markdown prompt with embedded instructions for Claude.

Test changes by installing locally and running against a test project.