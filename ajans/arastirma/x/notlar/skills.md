# Skill'ler, CLAUDE.md ve Claude Code iş akışı — X.com bulguları

## 1. Boris Cherny (Claude Code yaratıcısı) — 13 ipuçlu setup thread'i (tam metin)

Kaynak: https://x.com/bcherny/status/2007179832300581177 (54k beğeni, 8.2M görüntülenme)

> I'm Boris and I created Claude Code. Lots of people have asked how I use Claude Code, so I wanted to show off my setup a bit. My setup might be surprisingly vanilla! Claude Code works great out of the box, so I personally don't customize it much.

1. **5 Claude paralel** terminalde; tab'lar 1-5 numaralı; sistem bildirimleriyle hangisinin input beklediğini görür. (code.claude.com/docs/en/terminal-config#iterm-2-system-notifications)
2. Ayrıca **5-10 Claude claude.ai/code'da** paralel. Local oturumu `&` ile web'e devreder, `--teleport` ile geri alır; sabahları iOS app'ten oturum başlatır.
3. **Opus + thinking her şey için.** Daha büyük/yavaş ama daha az yönlendirme gerekir, tool kullanımı daha iyi → sonuçta daha hızlı.
4. **Tek ortak CLAUDE.md**, git'te, ekip haftada birkaç kez katkı yapar. "Claude bir şeyi yanlış yaptığında CLAUDE.md'ye ekleriz." Her ekip kendi dosyasını günceller.
5. Code review'da coworker PR'ında **@.claude** etiketleyip CLAUDE.md'ye madde ekletir (Claude Code GitHub action, `/install-github-action`). "Compounding Engineering".
6. **Çoğu oturum Plan mode'da başlar** (shift+tab ×2). PR hedefiyse planı Claude ile gidip gelerek beğenene kadar düzeltir, sonra auto-accept → genelde 1-shot. "A good plan is really important!"
7. **Slash komutlar** günde defalarca yapılan her "inner loop" için; `.claude/commands/` git'te. Örn. `/commit-push-pr` inline bash ile git status'u önceden hesaplar (docs/en/slash-commands#bash-command-execution).
8. **Subagent'lar**: `code-simplifier` (iş bitince kodu sadeleştirir), `verify-app` (uçtan uca test talimatları). Çoğu PR'da yapılan ortak işleri otomatikleştirir.
9. **PostToolUse hook** ile formatlama (CI'daki format hatalarını önler).
10. `--dangerously-skip-permissions` **kullanmaz**; `/permissions` ile güvenli bash komutlarını önceden izinler, `.claude/settings.json`'da ekiple paylaşır.
11. Claude tüm araçlarını kullanır: Slack MCP (arama/post), BigQuery (`bq` CLI), Sentry logları; `.mcp.json` ekiple paylaşılır.
12. Uzun işler için: (a) bitince background agent ile doğrulat, (b) Stop hook ile deterministik doğrula, (c) **ralph-wiggum plugin** (Geoffrey Huntley). Sandbox'ta `--permission-mode=dontAsk`.
13. **En önemlisi: Claude'a işini doğrulayacak bir yol ver → sonuç kalitesi 2-3x.** claude.ai/code'a giden her değişikliği Claude Chrome extension ile tarayıcıda test eder, UX iyi olana kadar iterasyon yapar. Doğrulama alana göre değişir: bash komutu, test suite, tarayıcı, telefon simülatörü. "Make sure to invest in making this rock-solid."

## 2. Anthropic'ten Thariq — "How We Use Skills" (article özeti)

Kaynak: https://x.com/trq212/article/2033949937936085378 (16k beğeni, 7M görüntülenme)

Skill = sadece markdown değil; script, asset, data içeren **klasör**; dinamik hook kaydedebilir. 9 skill kategorisi (Anthropic içinde yüzlerce skill kataloglanmış):
1. **Library & API reference** (internal lib footgun'ları, CLI subcommand'leri, frontend-design = "design system'imizde daha iyi ol")
2. **Product verification** (playwright/tmux ile test sürücüleri; "bir mühendisin bir haftasını sadece doğrulama skill'lerine ayırması değer"; video kaydet, her adımda assertion)
3. Data fetching & analysis (dashboard id'leri, sorgu kalıpları)
4. Business process automation (standup-post, ticket oluştur, weekly-recap; önceki sonuçları log'a yaz)
5. Code scaffolding & templates
6. Code quality & review (adversarial-review: taze gözlü subagent eleştirir → düzelt → nitpick kalana kadar)
7. CI/CD & deployment (babysit-pr, deploy, cherry-pick)
8. Runbooks
9. Infrastructure ops

Yazma ipuçları: **Bariz olanı söyleme** (Claude'u normal düşüncesinin dışına iten bilgiye odaklan — frontend-design böyle doğdu). **Gotchas bölümü** en yüksek sinyal; zamanla biriktir. **Dosya sistemi = progressive disclosure**: `references/api.md`, `assets/template.md`. **Railroad etme**: bilgi ver, esneklik bırak. Setup bilgisini `config.json`'da tut; yoksa kullanıcıya sor (AskUserQuestion). **Description alanı model içindir**: "ne zaman tetiklenir" yazar, özet değil. Hafıza: append-only log / JSON / SQLite; `${CLAUDE_PLUGIN_DATA}` kalıcı klasör. Script ve kütüphane ver → Claude kompozisyona odaklanır. **On-demand hook'lar**: `/careful` (rm -rf, DROP TABLE, force-push blokla), `/freeze` (belirli dizin dışına yazma). Dağıtım: `./.claude/skills` repo'da (küçük ekip) veya plugin marketplace (ölçek). Ölçüm: PreToolUse hook ile skill kullanım logu.

## 3. .claude/ klasörünün anatomisi (Akshay Pachaar article + Charlie Hills + Aayan)

- **CLAUDE.md** proje hafızası: build/test/lint komutları, mimari kararlar, non-obvious gotcha'lar, import/naming/error-handling kuralları, modül yapısı. **<200 satır** (uzadıkça uyum düşer). Yazma: linter'a ait şeyler, link verebileceğin dokümantasyon, teori paragrafları.
- 4 konum, **birleştirilir (override değil)**: managed policy → `~/.claude/CLAUDE.md` → `./CLAUDE.md` / `./.claude/CLAUDE.md` → `./CLAUDE.local.md` (gitignored). Çelişirse Claude rastgele birini seçer. Monorepo'da `claudeMdExcludes` ile üst dosyaları dışla.
- İçerik system prompt'un İÇİNDE değil, sonrasında user mesajı olarak gelir → **kesin uyum garantisi yok**. "Her seferinde olmalı" → **hook**. CLAUDE.md davranışı şekillendirir, hook zorlar.
- Satır ekleme testi: Claude aynı hatayı 2. kez yaptı mı / review repo'ya özgü bir şeyi yakaladı mı / aynı düzeltmeyi tekrar yazdın mı / yeni ekip arkadaşı buna ihtiyaç duyar mı? Satır silme testi: "**bunu kaldırınca Claude hata yapar mı?** Yapmazsa sil."
- `/init` üretir; `CLAUDE_CODE_NEW_INIT=1` → interaktif: hangi artefaktlar (CLAUDE.md, skills, hooks), subagent ile keşif, soru, önizleme. Üretileni **yarıya indir**.
- Kontrol edilebilir yaz: "2-space indentation" > "format code properly"; "Run npm test before committing" > "test your changes".
- Teşhis: kuralı sürekli bozuyorsa dosya uzun/kural gömülü; dosyada cevabı olan şeyi soruyorsa satır belirsiz.
- **rules/**: CLAUDE.md kalabalıklaşınca concern bazlı dosyalar; YAML frontmatter `paths` ile sadece ilgili dosyalarda yüklenir.
- **hooks** (settings.json): PreToolUse (güvenlik kapısı), PostToolUse (format/lint), Stop (kalite kapısı: testler geçmeli), UserPromptSubmit, PreCompact, Notification. **Exit code 2 tek bloklayan koddur**; 1 sadece loglar (güvenlik hook'unda en sık hata).
- **commands/** `/project:review` gibi; **skills/** otomatik tetiklenir; **agents/** izole context'li personalar (code-reviewer, security-auditor).
- Karar kuralı (Hills): proje talimatı → CLAUDE.md · tekrarlayan workflow → commands/ · bağlam tetikli → skills/ · uzman review → agents/.
- Subagent (santi): `/agents` veya `.claude/agents/<ad>.md` (name, description, tools, model); description'a "use proactively" yaz → otomatik; basit işlerde Haiku ucuz; repo'da paylaşılır.

## 4. affaan — Everything Claude Code (kısa rehber, 10 ay)
- Skills ~/.claude/skills (workflow), commands ~/.claude/commands (hızlı prompt); zincirlenebilir (`/refactor-clean`, `/tdd`, `/e2e`). Codemap güncelleyen skill → keşifte context yakmaz.
- Hook'ları `/hookify` plugin ile konuşarak yaz.
- Rules örnekleri: emoji yok, **frontend'de mor ton yok**, deploy öncesi test, modüler kod, console.log commit etme.
- **MCP bağlam yönetimi kritik**: 200k context çok tool ile 70k'ya düşer. 20-30 MCP config'de, **<10 aktif / <80 tool**. Chrome in Claude built-in.
- Kısayollar: Ctrl+U satır sil, `!` bash, `@` dosya, Shift+Enter çok satır, Esc Esc interrupt/restore; `/fork` paralel; git worktree; `/rewind`, `/checkpoints`, `/statusline`, `/compact`.
- Repo: github.com/affaan-m/ECC (210k★): test yazmadan "bitti" dememe, broken commit engelleme, oturumlar arası hafıza; `/ecc:plan`, `npx ecc-agentshield scan --fix`, `/analyze-repo`, `/instinct-status`.

## 5. Diğer repo/plugin bulguları
- **claude-code-setup** (resmi): `/plugin install claude-code-setup@claude-plugins-official` → projeyi tarar, hook/skill/MCP/subagent önerir, adım adım config. (Hasan Toor 1.3k beğeni, santi, arc.)
- **GStack** (Garry Tan, 115k★): sanal startup ekibi; `/office-hours` (6 soru, kötü fikri öldür), `/plan-eng-review`, `/review`, `/qa` (gerçek tarayıcıda test + regression), `/ship`.
- **Superpowers** (obra, 177k★): brainstorm → spec → plan → TDD → subagent execution → review → finalize; `/plugin install superpowers@claude-plugins-official`. Test yoksa implementasyon yazmayı reddeder.
- **simplify / code-simplifier** (Anthropic): davranışı değiştirmeden temizle; her oturum sonunda.
- **skill-creator** (Anthropic): workflow tarif et → SKILL.md + 3-5 test prompt'u + iterasyon; v2.1+ varsayılan.
- **web-design-guidelines** (vercel-labs/agent-skills): 100+ a11y/perf/UX kuralı, file:line ihlal listesi; frontend-design üretir, bu denetler.
- Mnimiy'nin 247→23 listesi: Tier S = frontend-design, superpowers, simplify, skill-creator, web-design-guidelines. **Çakışanlar**: frontend-design ⟂ impeccable; ui-ux-pro-max ⟂ web-design-guidelines (biri üretim, biri denetim). Hepsini birden kurma, context yanar.
- Spec Kit (github/spec-kit), Kiro, OpenSpec: spec-driven; dosyalar constitution.md / product.md / **design.md** / architecture.md / features/ / tasks/ (edinsoncode).
- Task Master AI: PRD → bağımlılıklı görevler. Context7: güncel docs. Playwright MCP: `claude mcp add playwright -s user -- npx @playwright/mcp@latest`.
- OneRedOak/claude-code-workflows: Design Review workflow (PR'da Playwright ile UI/UX/a11y).
- Vibe Kanban (`npx vibe-kanban`): ajanlara kart ata, git worktree ile paralel, diff review (AIMevzulari, TR).
- Happy (slopus/happy): telefondan Claude/Codex oturumuna bağlan.
- Ruben Hassid skill'leri: /grill-me (10-15 soru sormadan build yok), /humanizer, /fact-checker, /prompt-master, /linkedin-hook.
- Skill paylaşımı (JJ Englert): GitHub repo'da tüm Cowork/Claude Code workspace; .gitignore ile kişisel dosyalar.

## 6. Kalfa OS (Komünite, Türkçe) — github.com/komunite/kalfa-os
Claude Code için Türkçe operasyon katmanı: **994 skill / 16 kategori, 10 uzman agent (kalıcı hafıza), 22 komut, 9 hook, 6 katman hafıza.** Kurulum `npx @komunite/kalfa init` (`--dry-run`, `--force`, `--target`). Ritüeller: `/start` → `/sync` → `/clear` → `/wrap-up`; kalite: `/audit`, `/review`, `/release`, `/handoff`. `.claude/skills/INDEX.md`'den skill seç. `memory.md`, `knowledge-base.md`, `.claude/workspace/TaskBoard.md`. "TAMLIK KAPISI" hook'u TODO/TBD/FIXME bırakmayı engeller. `jq` gerekir. MIT. Özgenur Korlu eleştirisi: "skill nasıl yazılır" rehberi eksik.

## 7. nateherk — Claude'u para kazandıran ortak yapan 4 upgrade
1. Yes-man'i öldür: "roast council" (fikri build etmeden stres testi; sycophancy %88). 2. İşini kendisi doğrulasın/stres etsin, sonra "bitti" desin. 3. Context yönetimi (uzadıkça aptallaşmasın). 4. Darboğaz olma: paralel subagent + `/goal` ile bitene kadar döngü.

## Bizim fabrika için çıkarımlar
- `sistem/` altında: tek CLAUDE.md (<200 satır) + `rules/` (web/app/reklam/sosyal hatları için path-scoped) + `commands/` (`/yeni-musteri`, `/site-uret`, `/cila`, `/yayinla`) + `skills/` (frontend-design, expo, marketing, aso) + `agents/` (tasarım-eleştirmen, qa-tarayıcı, güvenlik) + hooks (PostToolUse format, Stop: testler/Playwright screenshot).
- Boris #13 ve Thariq #2: **doğrulama skill'ine yatırım** — Playwright ile 375px/1440px screenshot döngüsü, Expo simülatör testi. Bu, "fıstık gibi ürün" kapısı.
- Her müşteri işi bitince `code-simplifier` + "CLAUDE.md'ye ne eklemeliyiz?" adımı (compounding).
- Kalfa OS'u inceleyip Türkçe komut/hook fikirlerini al; ama 994 skill'i doğrudan kurma (context).
