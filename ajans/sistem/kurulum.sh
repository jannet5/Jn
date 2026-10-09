#!/usr/bin/env bash
# Fabrika kurulumu — bir proje klasöründe (ya da global) skill ve MCP'leri kurar.
# Kullanım: bash ajans/sistem/kurulum.sh [proje-klasoru]
# Not: Claude Code içi komutlar (/plugin ...) betikle çalışmaz; en altta elle yazılacaklar listelenir.
set -u
HEDEF="${1:-.}"
KOK="$(cd "$(dirname "$0")/../.." && pwd)"           # repo kökü
KOPYA="$KOK/ajans/arastirma/github/kopyalar"
cd "$HEDEF"
mkdir -p .claude/skills .claude/agents
ok(){ echo "  ✓ $1"; }; dene(){ echo "→ $1"; shift; "$@" >/dev/null 2>&1 && ok "tamam" || echo "  ! başarısız (elle dene)"; }

echo "== Skill'ler (npx skills) =="
dene "frontend-design (Anthropic)"        npx -y skills add anthropics/skills --skill frontend-design -y -a claude-code
dene "web-design-guidelines (Vercel)"     npx -y skills add vercel-labs/agent-skills --skill web-design-guidelines -y -a claude-code
dene "react-native-skills (Vercel)"       npx -y skills add vercel-labs/agent-skills --skill vercel-react-native-skills -y -a claude-code
dene "emil kowalski animasyon"            npx -y skills add emilkowalski/skills --skill '*' -y -a claude-code
dene "gsap"                               npx -y skills add greensock/gsap-skills --skill '*' -y -a claude-code
dene "ui-ux-pro-max"                      npx -y skills add nextlevelbuilder/ui-ux-pro-max-skill --skill ui-ux-pro-max -y -a claude-code
dene "expo skills"                        npx -y skills add expo/skills --skill '*' -y -a claude-code
dene "instagram-skills"                   npx -y skills add sergebulaev/instagram-skills --skill '*' -y -a claude-code
dene "marketingskills"                    npx -y skills add coreyhaines31/marketingskills --skill '*' -y -a claude-code
dene "impeccable"                         npx -y impeccable install

echo "== Repo kopyalarından =="
cp -r "$KOPYA/hallmark" .claude/skills/hallmark && ok hallmark
cp "$KOPYA/OneRedOak-claude-code-workflows/design-review-agent.md" .claude/agents/ && ok design-review-agent
cp -r "$KOK/ajans/sistem/skills/tr-caption" .claude/skills/tr-caption && ok tr-caption

echo "== Araçlar =="
dene "@google/design.md (lint)"           npm i -g @google/design.md
dene "playwright"                         npm i -D playwright

echo "== MCP =="
dene "playwright MCP"   claude mcp add playwright -- npx -y @playwright/mcp@latest
dene "context7 MCP"     claude mcp add context7 -- npx -y @upstash/context7-mcp@latest
dene "expo MCP"         claude mcp add --transport http expo https://mcp.expo.dev/mcp

cat <<'ELLE'

== Claude Code içinde elle (bir kez) ==
/plugin install superpowers@claude-plugins-official
/plugin install frontend-design@claude-plugins-official
/plugin install expo@claude-plugins-official
/impeccable init
ELLE
