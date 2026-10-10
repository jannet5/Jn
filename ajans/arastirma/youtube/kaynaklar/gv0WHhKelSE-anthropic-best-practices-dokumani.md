# Claude Code Best Practices (resmi doküman) — gv0WHhKelSE "Claude Code best practices | Code w/ Claude" konuşmasının yazılı karşılığı

- URL: https://code.claude.com/docs/en/best-practices (eski https://www.anthropic.com/engineering/claude-code-best-practices buraya yönleniyor)
- Tür: Anthropic resmi doküman. Çekildi: 2026-10-09 (WebFetch). Video transkripti yok; bu doküman konuşmanın güncel/yazılı sürümü.

## Ana ilke
"Most best practices are based on one constraint: Claude's context window fills up fast, and performance degrades as it fills." → Bağlam penceresi en önemli kaynak; `/context`, status line ile sürekli izle.

## 1. Claude'a işini doğrulayacağı bir yol ver
"Give Claude a check it can run: tests, a build, a screenshot to compare. It's the difference between a session you watch and one you walk away from."
Before → After örnekleri (aynen):
- "implement a function that validates email addresses" → "write a validateEmail function. example test cases: user@example.com is true, invalid is false, user@.com is false. run the tests after implementing"
- "make the dashboard look better" → "[paste screenshot] implement this design. take a screenshot of the result and compare it to the original. list differences and fix them"
- "the build is failing" → "the build fails with this error: [paste error]. fix it and verify the build succeeds. address the root cause, don't suppress the error"
Kapı sertliği: tek prompt içinde → `/goal` koşulu (her turda yeniden değerlendirilir) → **Stop hook** (deterministik, geçmeden tur bitmez) → doğrulama subagent'ı / dynamic workflow (ikinci görüş).
"Have Claude show evidence rather than asserting success."

## 2. Explore → Plan → Code → Commit
- Plan mode: `Shift+Tab` (⏸ plan mode on) veya `claude --permission-mode plan`.
- Explore: "read /src/auth and understand how we handle sessions and login. also look at how we manage environment variables for secrets."
- Plan: "I want to add Google OAuth. What files need to change? What's the session flow? Create a plan." → `Ctrl+G` planı editörde düzenle.
- Implement: "implement the OAuth flow from your plan. write tests for the callback handler, run the test suite and fix any failures."
- Commit: "commit with a descriptive message and open a PR"
- Uyarı: "If you could describe the diff in one sentence, skip the plan."

## 3. Spesifik bağlam ver (Before → After, aynen)
- "add tests for foo.py" → "write a test for foo.py covering the edge case where the user is logged out. avoid mocks."
- "why does ExecutionFactory have such a weird api?" → "look through ExecutionFactory's git history and summarize how its api came to be"
- "add a calendar widget" → "look at how existing widgets are implemented on the home page to understand the patterns. HotDogWidget.php is a good example. follow the pattern to implement a new calendar widget ... build from scratch without libraries other than the ones already used in the codebase."
- "fix the login bug" → "users report that login fails after session timeout. check the auth flow in src/auth/, especially token refresh. write a failing test that reproduces the issue, then fix it"
- `@dosya` referansı, görsel yapıştır, URL ver (`/permissions` ile domain allowlist), `cat error.log | claude -p "explain this error"`.

## 4. Ortam
### CLAUDE.md
- `/init` ile başla, zamanla buda. Her satır için: "Would removing this cause Claude to make mistakes?" Değilse sil. "Bloated CLAUDE.md files cause Claude to ignore your actual instructions!"
- Örnek (aynen):
```
# Code style
- Use ES modules (import/export) syntax, not CommonJS (require)
- Destructure imports when possible (eg. import { foo } from 'bar')

# Workflow
- Be sure to typecheck when you're done making a series of code changes
- Prefer running single tests, and not the whole test suite, for performance
```
- ✅ Dahil et: Claude'un tahmin edemeyeceği bash komutları; varsayılandan farklı stil kuralları; test talimatları/test runner; repo adabı (branch/PR); projeye özgü mimari kararlar; env değişkenleri; bariz olmayan tuzaklar.
- ❌ Hariç: koddan okunabilen her şey; standart dil kuralları; API dokümanı (link ver); sık değişen bilgi; uzun anlatımlar; dosya dosya açıklama; "write clean code" gibi bariz şeyler.
- Tek bir kural atlanıyorsa o satıra "IMPORTANT" ekle; çok satıra eklersen hiçbiri öne çıkmaz. `/doctor` kesilebilecekleri önerir. `@path/to/import` ile ek dosya içe aktar. Git'e ekle.
- Bazen gereken alan bilgisi → skill'e koy (talep üzerine yüklenir, her oturumu şişirmez).
### İzinler
- `/permissions` allowlist (örn. `npm run lint`, `git commit`), `/sandbox`; auto mode (sınıflandırıcı riskli olanı engeller).
### CLI araçları
- `gh`, `aws`, `gcloud`, `sentry-cli` → bağlam açısından en verimli yol. "Use 'foo-cli-tool --help' to learn about foo tool, then use it to solve A, B, C."
### MCP
- `claude mcp add --transport http notion https://mcp.notion.com/mcp`
### Hooks
- "Use hooks for actions that must happen every time with zero exceptions." CLAUDE.md tavsiye, hook deterministik. "Write a hook that runs eslint after every file edit" / "Write a hook that blocks writes to the migrations folder." `.claude/settings.json`, `/hooks`.
### Skills (`.claude/skills/<ad>/SKILL.md`) — örnekler aynen
```
---
name: api-conventions
description: REST API design conventions for our services
---
# API Conventions
- Use kebab-case for URL paths
- Use camelCase for JSON properties
- Always include pagination for list endpoints
- Version APIs in the URL path (/v1/, /v2/)
```
```
---
name: fix-issue
description: Fix a GitHub issue
disable-model-invocation: true
---
Analyze and fix the GitHub issue: $ARGUMENTS.

1. Use `gh issue view` to get the issue details
2. Understand the problem described in the issue
3. Search the codebase for relevant files
4. Implement the necessary changes to fix the issue
5. Write and run tests to verify the fix
6. Ensure code passes linting and type checking
7. Create a descriptive commit message
8. Push and create a PR
```
`/fix-issue 1234`. Yan etkili akışlar için `disable-model-invocation: true`.
### Subagents (`.claude/agents/<ad>.md`) — örnek aynen
```
---
name: security-reviewer
description: Reviews code for security vulnerabilities
tools: Read, Grep, Glob, Bash
model: opus
---
You are a senior security engineer. Review code for:
- Injection vulnerabilities (SQL, XSS, command injection)
- Authentication and authorization flaws
- Secrets or credentials in code
- Insecure data handling

Provide specific line references and suggested fixes.
```
"Use a subagent to review this code for security issues."
### Plugins: `/plugin` (skill+hook+subagent+MCP paketi).

## 5. İletişim
- Kod tabanına kıdemli mühendise sorar gibi sor.
- **Mülakat yöntemi** (büyük özellikler için, prompt aynen):
```
I want to build [brief description]. Interview me in detail using the AskUserQuestion tool.

Ask about technical implementation, UI/UX, edge cases, concerns, and tradeoffs. Don't ask obvious questions, dig into the hard parts I might not have considered.

Keep interviewing until we've covered everything, then write a complete spec to SPEC.md.
```
Spec bitince **yeni oturum** aç ve uygula. İyi spec: dosya/arayüzleri adlandırır, kapsam dışını söyler, uçtan uca doğrulama adımıyla biter.

## 6. Oturum yönetimi
- `Esc` durdur; `Esc Esc` / `/rewind` geri al; "Undo that"; `/clear` ilgisiz işler arasında.
- "If you've corrected Claude more than twice on the same issue in one session... Run /clear and start fresh with a more specific prompt."
- `/compact <talimat>`; CLAUDE.md'ye "When compacting, always preserve the full list of modified files and any test commands"; `/btw` bağlama girmeyen yan soru.
- Araştırmayı subagent'a ver: "Use subagents to investigate how our authentication system handles token refresh, and whether we have any existing OAuth utilities I should reuse."
- Checkpoints (`/rewind`), `/rename`, `claude --continue`, `claude --resume`.

## 7. Otomasyon ve ölçek
- `claude -p "prompt"` (CI, pre-commit); `--output-format json|stream-json`.
- Paralel: worktrees, cross-session messaging, Desktop app, cloud, `claude agents` (agent view), agent teams.
- **Writer/Reviewer** iki oturum: A "Implement a rate limiter for our API endpoints" → B "Review the rate limiter implementation in @src/middleware/rateLimiter.ts. Look for edge cases, race conditions, and consistency with our existing middleware patterns." → A "Here's the review feedback: [Session B output]. Address these issues."
- Fan-out: `/batch <instruction>` (5-30 subagent, her biri kendi worktree'sinde) veya döngü:
```
for file in $(cat files.txt); do
  claude -p "Migrate $file from Python 2 to Python 3. Return OK or FAIL." \
    --allowedTools "Edit,Bash(git commit *)" \
    --permission-mode dontAsk
done
```
- `claude --permission-mode auto -p "fix all lint errors"`
- **Adversarial review**: `/code-review` ya da "Use a subagent to review the rate limiter diff against PLAN.md. Check that every requirement is implemented, the listed edge cases have tests, and nothing outside the task's scope changed. Report gaps, not style preferences." Uyarı: reviewer hep bir şey bulur; sadece doğruluk/gereksinimi etkileyenleri düzelt.

## 8. Sık hatalar (aynen)
- The kitchen sink session → `/clear` between unrelated tasks.
- Correcting over and over → after two failed corrections, `/clear` and write a better initial prompt.
- The over-specified CLAUDE.md → ruthlessly prune; convert to a hook.
- The trust-then-verify gap → "If you can't verify it, don't ship it."
- The infinite exploration → scope narrowly or use subagents.
