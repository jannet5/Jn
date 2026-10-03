# Yol B — Claude Code + resmi frontend-design süreci
Önce `npm run girdiler` (SKILL.md'yi resmi repodan indirir). Prompt (denemede birebir kullanıldı):

> Build a web UI for the brief in akis/brief.md (read it), strictly following the official Anthropic frontend-design skill whose full text is at akis/girdiler/frontend-design-SKILL.md (read it fully first and follow its process: plan/brainstorm → review against brief → build → critique).
> Outputs (inside denemeler/b-claude-frontend-design/): 1. plan.md — the skill's planning pass (named hex colors, typefaces + roles, layout sentences with ASCII wireframes, principles) and the "review against brief" revisions, in Turkish. 2. dist/index.html — single self-contained file. 3. elestiri.md — screenshot critique log: take real Playwright screenshots at 1440x900 and 390x844, view them, critique against the skill and brief, revise. At most 2 revision rounds.
> Rules: only write inside that folder; do not read other folders under denemeler/; no git.

Claude Code'da eklenti olarak da kurulabilir: `/plugin install frontend-design@claude-plugins-official`
(https://github.com/anthropics/claude-code/tree/main/plugins/frontend-design).
