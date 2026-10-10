# Claude Code best practices | Code w/ Claude — Anthropic — 25 dk — https://www.youtube.com/watch?v=gv0WHhKelSE
> Kaynak notu: Transkript yok (bulut IP engeli). Açıklama tek satır. Plan, bu konuşmanın yazılı/güncel karşılığı olan resmi dokümana dayanır: `kaynaklar/gv0WHhKelSE-anthropic-best-practices-dokumani.md` (code.claude.com/docs/en/best-practices, eski anthropic.com/engineering/claude-code-best-practices). Güven: **yüksek** (içerik dokümandan aynen), konuşmadaki sıralama/anekdotlar: bilinmiyor.

## Tek paragraf
Anthropic'in 22 Mayıs 2025 "Code w/ Claude" konferansındaki resmi "best practices" konuşması. Mesaj tek bir kısıta dayanır: bağlam penceresi hızla dolar ve dolunca performans düşer; bütün pratikler bunu yönetmek içindir. Dört temel: (1) Claude'a işini **doğrulayacağı bir kontrol** ver (test, build, ekran görüntüsü), (2) **Explore → Plan → Code → Commit**, (3) prompt'ta spesifik bağlam, (4) ortamı kur: kısa CLAUDE.md, izin allowlist'i, CLI araçları (`gh`), MCP, hooks, skills, subagents. Üstüne oturum yönetimi (`/clear`, `/compact`, `/rewind`), mülakat → SPEC.md → yeni oturum, headless `claude -p`, çoklu oturum (Writer/Reviewer), fan-out ve adversarial review.

## Önerdiği iş akışı (adım adım)
1. **Kurulum (bir kez)**: `/init` → CLAUDE.md taslağı; `/permissions` ile `npm run lint`, `git commit` gibi güvenli komutları allowlist'e al; `gh` CLI kur; gerekli MCP'leri `claude mcp add` ile bağla; "her seferinde istisnasız" olması gereken şeyleri **hook** yap (örn. her düzenlemeden sonra lint); nadiren gereken alan bilgisini `.claude/skills/<ad>/SKILL.md`'ye, uzman rolleri `.claude/agents/<ad>.md`'ye koy.
2. **Büyük iş → önce spec**: "I want to build [X]. Interview me in detail using the AskUserQuestion tool..." → SPEC.md yazdır → **yeni oturum** aç, spec'i uygula.
3. **Explore** (plan mode, `Shift+Tab` ×2 veya `claude --permission-mode plan`): "read /src/auth and understand how we handle sessions..." — dosya okur, değiştirmez.
4. **Plan**: "What files need to change? What's the session flow? Create a plan." → `Ctrl+G` ile planı editörde düzelt → onayla. Tek cümlede anlatılabilen diff için planı atla.
5. **Code + verify**: "implement ... from your plan. write tests for ..., run the test suite and fix any failures." Kontrolün sertliği: aynı prompt'ta → `/goal` → **Stop hook** → doğrulama subagent'ı. Kanıt iste (test çıktısı, komut sonucu, ekran görüntüsü).
6. **Review**: `/code-review` ya da fresh-context subagent: "Use a subagent to review the diff against PLAN.md ... Report gaps, not style preferences."
7. **Commit**: "commit with a descriptive message and open a PR".
8. **Oturum hijyeni**: ilgisiz iş → `/clear`; iki kez düzelttiysen `/clear` + daha iyi prompt; `/compact <odak>`; araştırmayı subagent'a ver.
9. **Ölçek**: `claude -p` ile CI/batch; paralel oturumlar (worktrees / cloud / agent view); Writer-Reviewer iki oturum; `/batch` veya `for ... claude -p ... --allowedTools ... --permission-mode dontAsk` fan-out.

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- CLAUDE.md örneği:
```
# Code style
- Use ES modules (import/export) syntax, not CommonJS (require)
- Destructure imports when possible (eg. import { foo } from 'bar')

# Workflow
- Be sure to typecheck when you're done making a series of code changes
- Prefer running single tests, and not the whole test suite, for performance
```
- CLAUDE.md testi: "Would removing this cause Claude to make mistakes?" Değilse sil. Tek satıra "IMPORTANT" ekle, çoğuna değil. `/doctor` budama önerir. `@path` ile içe aktar.
- Doğrulama prompt'u: "write a validateEmail function. example test cases: user@example.com is true, invalid is false, user@.com is false. run the tests after implementing"
- UI: "[paste screenshot] implement this design. take a screenshot of the result and compare it to the original. list differences and fix them"
- Hata: "the build fails with this error: [paste error]. fix it and verify the build succeeds. address the root cause, don't suppress the error"
- Mülakat: "I want to build [brief description]. Interview me in detail using the AskUserQuestion tool. Ask about technical implementation, UI/UX, edge cases, concerns, and tradeoffs. Don't ask obvious questions, dig into the hard parts I might not have considered. Keep interviewing until we've covered everything, then write a complete spec to SPEC.md."
- Writer/Reviewer: "Review the rate limiter implementation in @src/middleware/rateLimiter.ts. Look for edge cases, race conditions, and consistency with our existing middleware patterns." → "Here's the review feedback: [Session B output]. Address these issues."
- Hook prompt'ları: "Write a hook that runs eslint after every file edit" / "Write a hook that blocks writes to the migrations folder."
- Compaction kuralı (CLAUDE.md'ye): "When compacting, always preserve the full list of modified files and any test commands"
- Skill ve subagent dosya örnekleri (api-conventions, fix-issue, security-reviewer) → kaynak dosyasında aynen.

## Araçlar ve linkler
- Doküman: https://code.claude.com/docs/en/best-practices ; ilgili: /docs/en/memory (CLAUDE.md), /docs/en/skills, /docs/en/sub-agents, /docs/en/hooks-guide, /docs/en/mcp, /docs/en/worktrees, /docs/en/headless, /docs/en/workflows, /docs/en/agent-teams
- Komutlar: `/init`, `/context`, `/doctor`, `/permissions`, `/sandbox`, `/hooks`, `/plugin`, `/clear`, `/compact`, `/rewind`, `/btw`, `/rename`, `/code-review`, `/batch`, `/goal`, `/verify`, `claude -p`, `claude mcp add`, `claude --continue|--resume`, `claude --permission-mode plan|auto`

## Hatalar / "bunu yapma" uyarıları
- Kitchen sink session (ilgisiz işleri tek oturumda) → `/clear`.
- Üst üste düzeltme → iki başarısız düzeltmeden sonra `/clear` + daha iyi prompt.
- Şişmiş CLAUDE.md → acımasız buda; kuralı hook'a çevir.
- Trust-then-verify boşluğu → "If you can't verify it, don't ship it."
- Sonsuz keşif → kapsamı daralt ya da subagent kullan.
- Reviewer her zaman bir şey bulur → sadece doğruluk/gereksinimi etkileyenleri düzelt, aşırı mühendislikten kaçın.
- Plan mode'u küçük işlerde kullanma (overhead).

## Bizim fabrikaya alınacaklar (somut)
1. **Her hat için "doğrulama kontrolü" zorunlu**: web → Playwright ekran görüntüsü + Lighthouse; app → Expo build + Maestro/screenshot; reklam → karakter limiti/politika lint betiği; sosyal → şablon doğrulama. Kontrol Stop hook'u olarak `.claude/settings.json`'a girer; geçmeden tur bitmez.
2. **Sipariş = SPEC.md**: müşteri brief'i geldiğinde ana beyin "Interview me..." kalıbıyla eksikleri sorar, `SPEC.md` üretir; üretim alt sohbeti sadece spec ile **temiz bağlamda** açılır.
3. **Explore→Plan→Code→Commit** her üretim sohbetinin iskeleti; plan `Ctrl+G` ile insan tarafından onaylanır (kalite kapısı 1).
4. **Fresh-context review**: teslim öncesi `/code-review` + "review against SPEC.md, report gaps not style" subagent'ı (kalite kapısı 2); web hattında ayrıca tasarım review subagent'ı.
5. **CLAUDE.md ≤ 60 satır**, sadece koddan okunamayanlar: komutlar, stil farkları, repo adabı, env tuzakları. Alan bilgisi (DESIGN.md kuralları, Meta reklam politikası, ASO) → skill'lere.
6. **Ortak skill paketi (plugin)**: `brief-to-spec`, `design-review`, `ads-variants`, `social-calendar`, `publish-checklist` SKILL.md'leri + `security-reviewer`/`design-reviewer` agent'ları; `/plugin` ile her müşteri repo'suna tek komutla.
7. **Fan-out**: aynı şablonla çok müşteri (10 restoran sitesi) → `for ... claude -p ... --allowedTools --permission-mode dontAsk` veya `/batch`.
8. **Oturum disiplini** CLAUDE.md'ye: "Bir müşteri = bir oturum; hat değişince /clear; iki düzeltmeden sonra yeniden başlat."
