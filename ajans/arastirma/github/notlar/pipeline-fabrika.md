# Pipeline / Fabrika / İş Akışı Sistemleri — Araştırma Notları

**Kategori:** Claude Code / Codex ile "start" denince fikir → spec → tasarım → kod → test → görsel QA → yayın zincirini çalıştıran repo'lar ve iş akışı sistemleri.
**Amaç:** İşletmelere web sitesi + mobil uygulama + reklam + sosyal medya üreten yapay zekâ ajansı için "çikolata fabrikası" gibi işleyen, her istasyonunda kalite kapısı olan bir zincir kurmak.
**Tarih:** 2026-10-05. Yıldız sayıları GitHub API'den (search_repositories), son commit tarihleri klonlanan repolardan (`git log -1`).
**Birebir kopyalar:** `/home/user/Jn/ajans/arastirma/github/kopyalar/<repo-adi>/` (yalnızca .md; her klasörde `KAYNAK.md`).

---

## 1. Özet tablo

Değer puanı (1-5): bizim ajans zinciri için doğrudan kullanılabilirlik × kalite kapısı gücü × bakım/olgunluk.

| Repo/Kaynak | Yıldız | Son güncelleme | Lisans | Ne işe yarar | Nasıl kullanılır | Değer |
|---|---|---|---|---|---|---|
| [obra/superpowers](https://github.com/obra/superpowers) | 295.549 | 2026-09-25 | MIT | Skill tabanlı SDLC metodolojisi: brainstorm → spec → plan → subagent-driven implement → review → finish. Sert "HARD-GATE"ler. | `/plugin install superpowers@claude-plugins-official`; Codex/Cursor/Gemini için de kurulum var | **5** |
| [github/spec-kit](https://github.com/github/spec-kit) | 140.218 | 2026-10-05 | MIT | Spec-Driven Development: constitution → specify → clarify → plan → tasks → implement → converge; spec kalite checklist'i | `uv tool install specify-cli && specify init my-project --integration claude` | **5** |
| [garrytan/gstack](https://github.com/garrytan/gstack) | 135.325 | 2026-10-05 | MIT | "Open source software factory": office-hours → plan-ceo/eng/design-review → autoplan → review → qa (gerçek tarayıcı) → ship → canary. 23 uzman rol. | `git clone --depth 1 https://github.com/garrytan/gstack ~/.claude/skills/gstack && ./setup` | **5** |
| [bmad-code-org/BMAD-METHOD](https://github.com/bmad-code-org/BMAD-METHOD) | 53.805 | 2026-10-04 | MIT | Ajan rolleri (PM, Architect, UX, Dev, QA) + skill'ler: brief → PRD → UX (DESIGN.md+EXPERIENCE.md) → architecture → spec → ticket → build (review lensleri) | `npx skills add bmad-code-org/BMAD-METHOD` veya `/plugin marketplace add bmad-code-org/bmad-plugins`; `uv` gerekli | **4** |
| [EveryInc/compound-engineering-plugin](https://github.com/EveryInc/compound-engineering-plugin) | 25.396 | 2026-10-05 | MIT | 36 skill; döngü: brainstorm → plan → work → simplify → code-review → compound (öğrenmeyi dokümante et). `/lfg` tam otonom boru hattı (browser test + PR + CI izleme dahil) | `/plugin marketplace add EveryInc/compound-engineering-plugin && /plugin install compound-engineering` | **5** |
| [snarktank/ralph](https://github.com/snarktank/ralph) | 21.912 | 2026-02-01 | MIT | Ralph döngüsü: prd.json'daki her story için taze Claude/Amp örneği; progress.txt hafıza; UI story'lerinde zorunlu browser doğrulaması | `cp ralph.sh CLAUDE.md scripts/ralph/`; `./scripts/ralph/ralph.sh --tool claude 20` | **5** |
| [anthropics/claude-code plugins/](https://github.com/anthropics/claude-code/tree/main/plugins) | 149.486 | 2026-10-03 | Anthropic | Resmi örnekler: `feature-dev` (7 fazlı), `ralph-wiggum` (Stop hook döngüsü), `frontend-design` (anti-slop tasarım), `code-review` (5 paralel ajan) | `/plugin install feature-dev@claude-plugins-official` vb. | **4** |
| [ghuntley.com/ralph](https://ghuntley.com/ralph/) | — | — | — | Ralph'ın orijinali: `while :; do cat PROMPT.md \| claude-code; done`; "loop başına tek şey"; backpressure (test), placeholder yasağı | Blog; prompt şablonları metinde | **4** |
| [michaelshimeles/ralphy](https://github.com/michaelshimeles/ralphy) | 2.974 | 2026-02-05 | — | Çok motorlu Ralph CLI (Claude/Codex/Cursor/Qwen…); `--parallel` ile worktree başına ajan; `.ralphy/config.yaml` kuralları; GitHub Issues kaynak | `npm i -g ralphy-cli; ralphy --prd PRD.md --parallel` | **3** |
| [Th0rgal/open-ralph-wiggum](https://github.com/Th0rgal/open-ralph-wiggum) | 1.896 | 2026-06-02 | MIT | `ralph "prompt"` tek komut döngü; çoklu ajan desteği | `bun install -g …; ralph "görev" --agent claude` | **2** |
| [tzachbon/smart-ralph](https://github.com/tzachbon/smart-ralph) | 554 | 2026-09-02 | MIT | Spec-driven Ralph: research → requirements → design → tasks → implement (POC→refactor→test→quality gates); spec-executor "uçtan uca kanıt" zorunluluğu | `/plugin marketplace add tzachbon/smart-ralph; /plugin install ralph-specum@smart-ralph` | **4** |
| [bmad-code-org/bmad-loop](https://github.com/bmad-code-org/bmad-loop) | 147 | 2026-10-04 | MIT | **LLM'siz** deterministik Python orkestratör: pick story → implement → adversarial review → verify → commit; tmux; hook event dosyaları; worktree izolasyonu | `uv sync; bmad-loop init; bmad-loop validate; bmad-loop run` | **4** |
| [open-gsd/gsd-core](https://github.com/open-gsd/gsd-core) (eski glittercowboy/get-shit-done) | 10.197 | 2026-10-05 | MIT | Faz döngüsü: discuss → plan → execute (paralel dalgalar, taze 200k bağlam) → verify (goal-backward) → ship; `ui-phase`/`ui-review` ile 6 sütunlu UI kontratı ve denetimi | `npx @opengsd/gsd-core@latest; /gsd-new-project` | **4** |
| [gotalab/cc-sdd](https://github.com/gotalab/cc-sdd) | 3.701 | 2026-09-23 | MIT | Kiro tarzı SDD: discovery → spec-init → requirements (EARS) → design (File Structure Plan) → tasks (Boundary/Depends) → kiro-impl (taze implementer + bağımsız reviewer + auto-debug) | `npx cc-sdd@latest` (Claude varsayılan; `--codex-skills`) | **4** |
| [markshust/hcf](https://github.com/markshust/hcf) | 76 | 2026-09-21 | MIT | Planlama (PM ile human-in-loop) + otonom paralel TDD worker'lar; 8 hook noktası (pre-plan… post-commit) ile ajan enrolmanı; devils-advocate | `/plugin marketplace add markshust/hcf; /plugin install hcf@hcf; /project-setup` | **3** |
| [nagisanzenin/claude-code-production-grade-plugin](https://github.com/nagisanzenin/claude-code-production-grade-plugin) | — | 2026-08-19 | MIT | DEFINE → BUILD → HARDEN → SHIP → SUSTAIN; 14 ajan; 3 kapı; **Loop Engine** ("bir iş, itiraz edemeyeceği bir kontrol onayladığında biter"); functional drive (uygulamayı gerçekten tıklar) | `/plugin marketplace add nagisanzenin/claude-code-plugins; /plugin install production-grade@nagisanzenin` | **4** |
| [SemanticMatter/autonomous-development](https://github.com/SemanticMatter/autonomous-development) | 3 | 2026-09-24 | MIT | Claude (uygulayıcı) + Codex (bağımsız planlayıcı/reviewer) çapraz-model akış; `controller.py` state machine; worktree izolasyonu | `claude plugin marketplace add /path; /autonomous-development:autonomous-feature "…"` | **3** |
| [coleam00/context-engineering-intro](https://github.com/coleam00/context-engineering-intro) | 13.884 | 2026-03-16 | MIT | PRP (Product Requirements Prompt): `/generate-prp INITIAL.md` → `/execute-prp` ; çalıştırılabilir "validation gates" | Repo'yu şablon olarak klonla; `.claude/commands/` kopyala | **3** |
| [buildermethods/agent-os](https://github.com/buildermethods/agent-os) | 5.471 | 2026-08-29 | MIT | Kod standartlarını keşfet/indeksle/enjekte et + `shape-spec` (plan mode'da) | `scripts/project-install.sh`; `/shape-spec` | **2** |
| [Yeachan-Heo/oh-my-claudecode](https://github.com/Yeachan-Heo/oh-my-claudecode) | 39.595 | 2026-10-03 | MIT | `/autopilot`: idea → spec → plan → parallel exec → QA döngüsü (5×) → çok perspektifli doğrulama; `/ralph` PRD+reviewer; `/team` | `/plugin marketplace add https://github.com/Yeachan-Heo/oh-my-claudecode; /plugin install oh-my-claudecode; /omc-setup` | **3** |
| [eyaltoledano/claude-task-master](https://github.com/eyaltoledano/claude-task-master) | 28.147 | 2026-04-23 | MIT+CC | PRD → görev grafiği MCP sunucusu; loop komutu | `claude mcp add taskmaster-ai -- npx -y task-master-ai` | **2** |
| [ruvnet/claude-flow (Ruflo)](https://github.com/ruvnet/claude-flow) | — | 2026-10-05 | MIT | Meta-harness: swarm, hafıza, `ruflo-sparc` 5 fazlı metodoloji; çok ağır | `npx ruflo init` | **2** |
| [OneRedOak/claude-code-workflows](https://github.com/OneRedOak/claude-code-workflows) | 3.896 | 2025-09-14 | MIT | **En çok kopyalanan design-review ajanı**: Playwright MCP ile canlı önizlemede 7 fazlı inceleme (etkileşim, 3 viewport, polish, WCAG, robustness, konsol) + CLAUDE.md "Quick Visual Check" snippet'i | `design-review-agent.md` → `.claude/agents/`; snippet → CLAUDE.md | **5** |
| [ChrisBrooksbank/cb-visual-qa](https://github.com/ChrisBrooksbank/cb-visual-qa) | 1 | 2026-04-12 | MIT | "Derlenir ama bozuk görünür" sorununa Playwright smoke test + `design.md` + Ralph için `PROMPT_visual-check.md` | `cp commands/cb-visual-qa.md ~/.claude/commands/; /cb-visual-qa` | **4** |
| [humbleteam/design-review](https://github.com/humbleteam/design-review) | 4 | 2026-10-02 | MIT | 0-4 puanlı UX kritiği; her bulguda Before/After/Why + Nielsen/WCAG atıfı; karşılaştırma modu (A/B kazanan) | SKILL.md → `.claude/skills/design-review/` | **4** |
| GSD `gsd-ui-checker` / `gsd-ui-auditor` | (gsd) | 2026-05-31 | MIT | UI-SPEC.md kontratını 6 boyutta BLOCK/FLAG/PASS; uygulanmış UI'ı 6 sütunda 1-4 puanlar (screenshot + grep) | gsd-core içinde `/gsd-ui-phase`, `/gsd-ui-review` | **4** |
| `frontend-design` skill (Anthropic) | (cc) | 2026-10-03 | Anthropic | Anti-"AI slop" tasarım: 5 klişe tespiti, iki geçişli plan → kritik; "tek yerde cesaret" | `/plugin install frontend-design@claude-plugins-official` | **4** |
| [yybd/app-factory](https://github.com/yybd/app-factory) | 1 | 2026-09-29 | MIT | 40 skill, 7 track: App Store/Play yayın zinciri (identity → binary → privacy → media → copy → deliver → ship → ASO); web-track page-builder + web-design-guidelines | `claude plugin marketplace add yybd/app-factory; python3 …/factory/enable.py` | **4** |
| [jezemm/appfactory-plugin](https://github.com/jezemm/appfactory-plugin) | 0 | 2026-10-02 | Tescilli | Cümleden yayınlanmış app'e: PHASE-0 idea → 2 research (5 lens + 25 persona) → 4 spec → 5 build (verify loop + UX polish) → 6 ship. Hosted MCP fabrikaya bağımlı | Yalnızca okuma/ilham (lisans kısıtlı) | **3** |
| [wshobson/agents](https://github.com/wshobson/agents) | 40.212 | 2026-10-04 | MIT | 94 plugin / 202 agent / 184 skill; rol havuzu (frontend-dev, ui-ux-designer, mobile-dev…) | `/plugin marketplace add wshobson/agents` | **2** |
| [VoltAgent/awesome-claude-code-subagents](https://github.com/VoltAgent/awesome-claude-code-subagents) | 25.509 | 2026-10-05 | MIT | 161 subagent tanımı, 10 kategori | `claude plugin marketplace add VoltAgent/awesome-claude-code-subagents` | **2** |
| [hesreallyhim/awesome-claude-code](https://github.com/hesreallyhim/awesome-claude-code) | 55.095 | 2026-10-05 | CC BY | Küratörlü liste; "Agent Orchestration" + "Ralph Wiggum" bölümleri (ccpm, gstack, ralph-orchestrator, frankbria/ralph-claude-code, awesome-ralph) | Referans | **3** |
| [davila7/claude-code-templates](https://github.com/davila7/claude-code-templates) | 32.391 | 2026-10-05 | MIT | aitmpl.com: tek komutla agent/command/hook/MCP kurulumu | `npx claude-code-templates@latest --agent … --yes` | **2** |
| [anthropics/claude-cookbooks patterns/agents](https://github.com/anthropics/claude-cookbooks/tree/main/patterns/agents) | 53.207 | 2026-10-05 | MIT | "Building Effective Agents" örnekleri: prompt chaining, routing, parallelization, orchestrator-workers, evaluator-optimizer, async multi-agent | Notebook'lar | **3** |
| [t3-oss/create-t3-turbo](https://github.com/t3-oss/create-t3-turbo) | 6.110 | 2025-12-11 | MIT | Next.js 15 + Expo SDK 54 + tRPC v11 + Drizzle + better-auth + shadcn Turborepo starter (CLAUDE.md/AGENTS.md **yok**) | `npx create-turbo@latest -e https://github.com/t3-oss/create-t3-turbo` | **4** |
| [roninoss/create-expo-stack](https://github.com/roninoss/create-expo-stack) | 2.575 | 2026-09-15 | MIT | Expo CLI üreteci (expo-router, NativeWind/Unistyles/Tamagui, Supabase/Firebase) | `npx rn-new@latest` | **3** |
| [automazeio/ccpm](https://github.com/automazeio/ccpm) | 8.398 | — | MIT | GitHub Issues + git worktree ile paralel ajan proje yönetimi | awesome-claude-code'dan | **3** |
| [frankbria/ralph-claude-code](https://github.com/frankbria/ralph-claude-code), [mikeyobrien/ralph-orchestrator](https://github.com/mikeyobrien/ralph-orchestrator), [snwfdhmp/awesome-ralph](https://github.com/snwfdhmp/awesome-ralph) | 9.654 / 3.163 / 926 | — | MIT | Ralph varyantları: akıllı çıkış tespiti, rate limit, circuit breaker (frankbria); Rust orkestratör (mikeyobrien); kaynak listesi | Referans | **2** |

---

## 2. Sistem incelemeleri

### 2.1 obra/superpowers — skill'lerle SDLC (omurga adayı #1)

**Akış şeması (metin):**
```
Kullanıcı isteği
  └─ using-superpowers (otomatik tetikleyici: her işte önce skill ara)
      └─ brainstorming  ──► sınıflandır: spike | bounded | architectural
            │   architectural: context keşfi → soru (tek tek) → 2-3 yaklaşım → bölüm bölüm tasarım onayı
            │   → docs/superpowers/specs/YYYY-MM-DD-<topic>-design.md → spec self-review → KULLANICI ONAYI
            └─ writing-plans ──► docs/superpowers/plans/… (Task N: Files / Interfaces / Step 1-5, TDD)
                   → plan self-review (spec coverage, type consistency, review focus) → KULLANICI ONAYI + yöntem seçimi
                     ├─ subagent-driven-development: her task için taze implementer + task reviewer (spec ✅ + quality) → fix loop (max 5 tur, 4-5. tur daha güçlü model) → final whole-branch review
                     └─ executing-plans: aynı oturumda uygula + sonda tek reviewer
                   → verification-before-completion → finishing-a-development-branch (merge/PR seçenekleri)
```

**Aşamalar ve kontrol kapıları:**
| Aşama | Kapı | Not |
|---|---|---|
| Brainstorm | `<HARD-GATE>`: path'in ön koşulu tamamlanmadan tek satır kod yok; "onay sunulan aşamayı onaylar" | Tek seferde tek soru; YAGNI |
| Spec | Self-review (placeholder, çelişki, scope, belirsizlik) + kullanıcı okur | Spec dosyası commit'lenir |
| Plan | Self-review 5 madde; "plan koddan uzunsa transkripttir" | Global Constraints + Review Focus bölümleri |
| Implement | Taze subagent / task; ledger (`.superpowers/sdd/progress.md`) compaction'a dayanır; dört durdurma nedeni dışında insana sormaz ("Rulings, not stalls") | Model seçimi: mekanik → ucuz, tasarım → en güçlü |
| Review | Task reviewer rapora güvenmez ("Do Not Trust the Report"); Critical/Important → fix loop; Minor → ledger | Reviewer asla kendi alt-ajan açmaz |
| Final | Whole-branch review (en güçlü model) → tek fix dalgası → tek re-review | "Rulings I made" listesi insana döner |

**Kurulum:** `/plugin install superpowers@claude-plugins-official` (Claude Code); Codex: `/plugins` → Superpowers; Cursor: `/add-plugin superpowers`.

**Güçlü:** Dünyanın en çok yıldızlı skill çerçevesi; gerçek oturum kayıplarından damıtılmış kurallar (ledger, batch dispatch, no-nested-reviewer); harness-bağımsız. **Zayıf:** Görsel/tasarım kapısı yok (brainstorming'de "visual companion" var ama QA değil); yayın/pazarlama aşaması yok; insan onayı iki noktada zorunlu (tam otonomi için ayar gerek).

**Bize uyarlama:** Fikir → spec → plan → SDD omurgası olarak alın. Görsel QA kapısını `task-reviewer-prompt.md`'ye ek bir "UI task ise screenshot kanıtı zorunlu" maddesiyle enjekte edin (bkz. §4).

### 2.2 github/spec-kit — Spec-Driven Development

**Akış:** `/speckit-constitution` (proje ilkeleri, bir kez) → `/speckit-specify` (WHAT/WHY; max 3 `[NEEDS CLARIFICATION]`; `checklists/requirements.md` kalite kontrolü) → `/speckit-clarify` (opsiyonel) → `/speckit-plan` (research.md, data-model.md, contracts/, quickstart.md; Constitution Check kapısı) → `/speckit-tasks` → `/speckit-analyze` (tutarlılık) → `/speckit-implement` → `/speckit-converge` (spec/plan/tasks ile kod arasındaki farkı yeni task olarak ekler; "Converged" diyene kadar tekrar).

**Kontrol kapıları:** spec kalite checklist'i (içerik, tamlık, hazırlık), Constitution Check (plan öncesi ve sonrası), `.specify/extensions.yml` hook'ları (`before_specify`, `after_plan`… zorunlu/opsiyonel), converge döngüsü.

**Kurulum:** `uv tool install specify-cli && specify init my-project --integration claude`; uzantılar: `specify extension add bug|assess`.

**Güçlü:** GitHub'ın resmi, 140k yıldız; şablonlar düzenlenebilir; "success criteria teknoloji-bağımsız ve ölçülebilir" disiplini; hook mekanizması bizim kapılarımızı takmaya uygun. **Zayıf:** Uygulama aşaması zayıf (tek oturum), tasarım/görsel kapı yok, çok şablon-ağır.

**Bize uyarlama:** Spec/plan/tasks dosya formatını standart olarak benimse; `extensions.yml` `after_implement` hook'una görsel QA komutu tak.

### 2.3 garrytan/gstack — "open source software factory"

**Akış:** `/office-hours` (6 zorlayıcı soru, tasarım dokümanı) → `/plan-ceo-review` → `/plan-eng-review` → `/plan-design-review` (0-10 boyut puanı, AI slop tespiti) → (`/autoplan` hepsini otomatik koşar, yalnızca "zevk kararları"nı sorar) → kod → `/review` → `/qa <url>` (gerçek tarayıcı, bug bul-düzelt-regresyon testi) → `/design-review` (canlı sitede görsel denetim + atomik düzeltme commit'leri + before/after screenshot) → `/ship` → `/land-and-deploy` → `/canary` → `/retro`.

**Kontrol kapıları:** plan aşamasında üç bağımsız uzman incelemesi; `/review` CI'yi geçen prod bug'larını arar; `/qa` üç seviye (Quick/Standard/Exhaustive) ve "keşif notu" kanıtı; `/ship` test+doküman denetimi; `/cso` güvenlik.

**Kurulum:** `git clone --single-branch --depth 1 https://github.com/garrytan/gstack.git ~/.claude/skills/gstack && cd ~/.claude/skills/gstack && ./setup` (Bun gerekir; kendi Chromium'unu indirir). Codex/Cursor/Kiro/Copilot için `--host`.

**Güçlü:** Uçtan uca gerçekten çalışan, günlük kullanılan fabrika; tasarım kapısı hem plan (`/plan-design-review`) hem canlı (`/design-review`) seviyesinde; `/design-consultation` DESIGN.md üretir; `/design-shotgun` 4-6 mockup varyantı. **Zayıf:** Çok büyük ve kendine özgü runtime (preamble script'leri, telemetri, Bun); skill'ler otomatik üretilmiş ve uzun; mobil (Expo) için `ios-*` skill'leri var ama Android yok.

**Bize uyarlama:** Tasarım kapıları (`plan-design-review`, `design-review`, `design-consultation`) ve `/qa` doğrudan alınacak parçalar. Tüm gstack'i yüklemek yerine bu 4 skill'in mantığını kendi skill'lerimize aktarın.

### 2.4 BMAD-METHOD — ajan rolleri ve planlama yolları

**Akış (v6, skill tabanlı):** `bmad` hub → analiz (brainstorming, forge-idea, deep-recon, product-brief, prfaq) → planlama (prd, **ux** → `DESIGN.md` + `EXPERIENCE.md`, spec) → solutioning (architecture, ticket) → `bmad-build` (oneshot/full rota; review: none/quick/thorough) → `bmad-review` (lensler: adversarial, edge-case-hunter, verification-gap, structure, prose) → correct-course / retrospective.

**Kontrol kapıları:** "Choose a planning path" — niyet net mi? (bmad-spec'e ver, değilse önce analiz); UX skill'inde opsiyonel **Reviewer Gate** (paralel lens alt-ajanları `review-{lens}.md` yazar); build'de `claims-check` (planın iddiaları ile diff karşılaştırılır) ve `deletion-check`; edge-case-hunter sonuçları JSON (`location/trigger_condition/guard_snippet/potential_consequence`).

**Kurulum:** `npx skills add bmad-code-org/BMAD-METHOD` (skill seç: `bmad`, `bmod-core-tools`, `bmod-method`, `bmad-build`), projede `bmad setup`; `uv` zorunlu.

**Güçlü:** Rol ayrımı en olgun; UX çıktısı Google Labs `DESIGN.md` standardında (bizim design-system kopyalarımızla uyumlu); review lensleri JSON şema disiplinli. **Zayıf:** Ağır kurulum (`_bmad/` klasörü, Python render script'leri); görsel QA yok (lensler kod/metin odaklı); çıktı klasör yapısı kendi düzenini dayatır.

**Bize uyarlama:** PM/UX/Architect "persona" prompt'larını ve `DESIGN.md + EXPERIENCE.md` çift spine'ını alın; `bmad-loop` ile otonom sprint koşturma opsiyonu.

### 2.5 Ralph ailesi — otonom döngü

**Orijinal (ghuntley):** `while :; do cat PROMPT.md | claude-code ; done`. Her iterasyonda deterministik bağlam: `@PROMPT.md`/`@fix_plan.md`, `@specs/*`, `@AGENT.md`. Kural: **loop başına tek şey**; test = backpressure; "DO NOT IMPLEMENT PLACEHOLDER… WE WANT FULL IMPLEMENTATIONS"; aramada 500 subagent, build/test'te 1 subagent.

**snarktank/ralph (Ryan Carson):** `/prd` skill'i (3-5 A/B/C soruyla PRD) → `/ralph` (PRD → `prd.json` story'leri: tek iterasyonda bitecek kadar küçük, bağımlılık sırası, "Typecheck passes" her story'de, UI story'de "Verify in browser using dev-browser skill") → `ralph.sh` (her iterasyon taze `claude --print --dangerously-skip-permissions < CLAUDE.md`; `<promise>COMPLETE</promise>` görünce çıkar; branch değişince arşivler) → `progress.txt` (append-only; başta "Codebase Patterns").

**Anthropic ralph-wiggum plugin:** Harici bash döngüsü yerine **Stop hook** oturum içinde çıkışı engelleyip aynı prompt'u geri besler: `/ralph-loop "…" --completion-promise "COMPLETE" --max-iterations 50`; `/cancel-ralph`.

**ralphy:** çoklu motor, `--parallel --max-parallel 5` ile worktree başına ajan + otomatik merge (AI çatışma çözer) ya da `--create-pr`; `.ralphy/config.yaml` (commands, rules, boundaries.never_touch).

**smart-ralph:** döngüden önce spec katmanı (research/requirements/design/tasks ajanları) ve `spec-executor` ("Tests pass — NOT ENOUGH… ONLY mark TASK_COMPLETE when you have PROOF… screenshot/API response/log").

**bmad-loop:** kontrol döngüsünde LLM yok; Python her story için ayrı dev ve review oturumu açar (review implementer bağlamını devralmaz → anchoring yok); diskteki artefaktları doğrular (spec status, diff boş değil, test/lint) sonra commit; `deferred-work.md` ledger; CRITICAL escalation'da insan için `bmad-loop resolve`.

**Kontrol kapıları:** typecheck/test (her story), browser doğrulaması (UI story), `passes:true` sadece kanıtla, max-iterations, promise string, progress/ledger hafızası.

**Güçlü:** Basit, dayanıklı, taze bağlam = context rot yok. **Zayıf:** Prompt kalitesine çok bağımlı; görsel kalite kapısı "dev-browser ile bak" seviyesinde; paralellik çatışma riski.

**Bize uyarlama:** Fabrikanın "uygulama bandı": prd.json → ralph döngüsü; her UI story'nin kabul kriterine screenshot+design-review puanı ekleyin (§4.6).

### 2.6 EveryInc/compound-engineering — döngü + otonom `/lfg`

**Akış:** `/ce-brainstorm` (WHAT; lightweight ise chat'te biter, yoksa `docs/plans/…-plan.md` requirements-only) → `/ce-plan` (HOW; Direct / Chat brief / Durable çıktı sözleşmesi; `ce-doc-review` zorunlu) → `/ce-work` (worker'lara sınırlı birimler, host orchestrator canonical commit) → `/ce-simplify-code` → `/ce-code-review` (persona reviewer'lar paralel + çapraz-model adversarial) → `/ce-compound` (`docs/solutions/` öğrenme kaydı; sonraki brainstorm/plan bunu okur).
`/lfg` = tümü hands-off: plan → work → simplify → code-review (fix uygula) → compound → **`ce-test-browser mode:pipeline`** → commit-push-pr → `ce-babysit-pr` (CI onarım döngüsü, bütçeli) → `<promise>DONE</promise>`.

**Kontrol kapıları:** plan readiness check, code-review receipt olmadan ship yok, browser test (her etkilenen route Pass/Fail/Skip, pipeline'da soru sormaz), `ce-polish` (kullanıcı canlı sayfada söyler, ajan düzeltir), `ce-dogfood`, `ce-noslop`.

**Kurulum:** `/plugin marketplace add EveryInc/compound-engineering-plugin` → `/plugin install compound-engineering` → `/ce-setup`.

**Güçlü:** 14 harness; "compound" fikri (her iş bir sonrakini kolaylaştırır) ajans için altın — müşteri projelerinden öğrenme `docs/solutions/` + Compound Packs (org kuralları, ref-pinned git). `ce-test-browser` host-native tarayıcı/agent-browser politikası net. **Zayıf:** Skill metinleri çok uzun ve referans dosyalarına yayılmış; görsel estetik kapısı (slop/design score) yok, sadece işlevsel browser test.

**Bize uyarlama:** `/lfg`'yi şablon alıp 8. adımdan sonra "design-review (0-4 puan ≥3 değilse fix)" adımı ekleyin; `ce-compound` → ajans "bilgi bankası".

### 2.7 GSD (gsd-core) — faz döngüsü ve UI kontratı

**Akış:** `/gsd-new-project` (araştırma + roadmap) → her faz için **discuss** (CONTEXT.md kararlar) → **plan** (research + planner + plan-checker) → **execute** (paralel dalgalar, her executor taze 200k bağlam) → **verify** (`gsd-verifier` goal-backward: "SUMMARY.md kanıt değildir") → **ship** (PR, arşiv). UI için `/gsd-ui-phase` (`gsd-ui-researcher` UI-SPEC.md yazar, `gsd-ui-checker` 6 boyutta BLOCK/FLAG/PASS) ve `/gsd-ui-review` (`gsd-ui-auditor` 6 sütun 1-4 puan, Playwright MCP varsa desktop/mobile/tablet screenshot).

**Kontrol kapıları:** plan-checker, nyquist-auditor, verifier (BLOCKER/WARNING), ui-checker (CTA "Submit/OK" → BLOCK; >4 font boyutu → BLOCK; 4'ün katı olmayan spacing → BLOCK; accent "tüm etkileşimli elemanlar" → BLOCK), ui-auditor registry güvenlik taraması.

**Güçlü:** UI kontrat/denetim ikilisi somut ve gerçekten puanlıyor; "adversarial stance" prompt'ları. **Zayıf:** Repo taşındı, komut sayısı 60+, ağır.

**Bize uyarlama:** `gsd-ui-checker` 6 boyutunu spec kapısına, `gsd-ui-auditor` 6 sütununu post-build kapısına taşıyın.

### 2.8 cc-sdd (Kiro tarzı) ve smart-ralph — spec → otonom uygulama

**cc-sdd akış:** `/kiro-discovery` (rota: mevcut spec'i genişlet | spec'siz uygula | tek spec | çoklu spec; `brief.md`, `roadmap.md`) → `/kiro-spec-init` → `/kiro-spec-requirements` (EARS) → `/kiro-spec-design` (Mermaid + **File Structure Plan**) → `/kiro-spec-tasks` (`_Boundary:_`, `_Depends:_`) → `/kiro-impl` (iterasyon başına tam bir alt-görev; taze implementer TDD RED→GREEN feature flag arkasında; bağımsız reviewer `APPROVED|REJECTED`; 3. ret → debug subagent; `kiro-verify-completion`; seçici `git add`).
**Kapılar:** her fazda insan onayı (`spec.json` approvals), sınır ihlali denetimi, `## Implementation Notes` öğrenme aktarımı.

**Bize uyarlama:** Task formatı (Boundary/Depends) + "iterasyon başına tek alt-görev + fresh reviewer" kuralı.

### 2.9 HCF, production-grade, autonomous-development — hook/kapı mimarileri

- **HCF:** planlama insanlı (must-answer vs will-default soru sınıfları), uygulama otonom paralel TDD worker'lar; **8 hook noktası** (`pre-plan, post-plan, pre-implementation, pre-batch, post-batch, post-implementation, pre-commit, post-commit`) — ajanlar frontmatter'daki `phase:` ile kendini kaydeder; `devils-advocate` varsayılan post-plan. Bizim kapı mimarimiz için en temiz model.
- **production-grade:** 3 kapı (Requirements, Architecture, Production Readiness) + 2 paralel dalga; **Loop Engine**: "A task is done when a check it cannot argue with says so"; `PostToolUse` hook her dosya düzenlemesinden sonra hızlı oracle (typecheck+lint <15s); QA `tests/`'in sahibi, testi zayıflatan kod ajanına Critical; **functional drive** (ajan uygulamayı açıp her buton/form/linke basar; ölü kontrol = Critical); receipt JSON olmadan kapı açılmaz; "re-anchoring" (her faz geçişinde spec diskten yeniden okunur).
- **autonomous-development:** Claude uygular, Codex taze ve salt-okunur olarak plan yazar/review eder; `controller.py next-action` state machine; `run-check` ile her doğrulama loglanır; Stop hook `stop_gate.py` evaluate geçmeden bitirmez.

### 2.10 Görsel QA / tasarım inceleme sistemleri (kalite kapısı odak)

| Sistem | Girdi | Yöntem | Çıktı/Kapı |
|---|---|---|---|
| **OneRedOak design-review agent** | PR diff + canlı önizleme | Playwright MCP: 1440/768/375 viewport, etkileşim, hover/focus, WCAG 2.1 AA, overflow stres, konsol | `[Blocker]/[High]/[Medium]/[Nit]` + screenshot kanıt; CLAUDE.md "Quick Visual Check" her frontend değişikliğinden hemen sonra |
| **humbleteam/design-review** | screenshot / URL / HTML | Rubric: 0-4 bant; fix maliyeti bandı belirler; her bulgu Before/After/Why + Nielsen#/WCAG SC atıfı; max 6 bulgu; karşılaştırma modu | `score X/4`, "Fix this first" |
| **cb-visual-qa** | route listesi | Playwright smoke: konsol hatası yok, body boş değil, "something went wrong" yok, height>100, main görünür, screenshot baseline, kırık görsel yok | `npm run test:visual`; `design.md` + `PROMPT_visual-check.md` (Ralph) |
| **GSD ui-checker / ui-auditor** | UI-SPEC.md / kod + screenshot | 6 boyut: copywriting, visuals, color (60/30/10), typography (≤4 boyut, ≤2 ağırlık), spacing (4'ün katı), experience | BLOCK/FLAG/PASS; 1-4 puan + top-3 fix; UI-REVIEW.md |
| **Anthropic frontend-design skill** | brief | 5 "AI-generated" klişe listesi (krem+serif+terracotta, siyah+asit yeşil, broadsheet, SaaS-card kit, template chrome); iki geçiş: token planı → brief'e karşı kritik → kod → screenshot ile öz-kritik | Tasarım üretim standardı |
| **gstack plan-design-review / design-review** | plan / canlı site | 0-10 boyut puanı + "10 nasıl olur"; canlı: bul-düzelt-atomik commit-before/after | Plan düzeltilir; commit'ler |
| **appfactory UX-POLISH** | /app yüzeyleri | 9 boyut (ilk izlenim, akışlar, first-run, microinteractions 8/10 bar, …); `appfactory verify` exit 0 olana kadar; tüm screenshot'ları yan yana koy | `app.review` kapısı |
| **compound ce-test-browser / ce-polish** | değişen route'lar | host-native tarayıcı → agent-browser; Pass/Fail/Skip; polish'te kullanıcı canlı söyler | pipeline'da soru sormaz |
| **production-grade functional drive** | çalışan app | her buton/form/link; ölü kontrol Critical | BUILD çıkış kapısı |
| **yybd web-design-guidelines** | dosyalar | Vercel Web Interface Guidelines'ı fetch edip `file:line` bulgular; offline fallback checklist | rapor (fix etmez) |

**Sentez:** Hiçbir tek repo "estetik puan + işlevsel browser test + regresyon baseline" üçünü birlikte vermiyor. Kombinasyon: cb-visual-qa (smoke+baseline, deterministik) → OneRedOak agent (etkileşim/WCAG/responsive, kanıt) → humbleteam rubric (0-4 puan, atıflı; ≥3 geçer) → frontend-design/ui-checker anti-slop kontrolü (klişe tespiti, token kısıtları).

### 2.11 Çoklu ajan paralel çalışma (kısa)

- **git worktree:** superpowers `using-git-worktrees`, ralphy `--parallel` (worktree+branch/ajan, AI merge), ccpm (GitHub Issues → worktree), bmad-loop `[scm] isolation="worktree"`, autonomous-development `EnterWorktree`, compound `ce-worktree`. Claude Code'un yerel `--worktree` bayrağı ve `.worktreeinclude` var.
- **Agent Teams (Claude Code, 2026):** orkestratör + worker'lar; oh-my-claudecode `/team 3:executor`, production-grade "Teams/TaskList", GSD "paralel dalgalar".
- **Kural (superpowers):** "Never dispatch multiple implementation subagents in parallel (conflicts)" — paralellik ancak dosya sahipliği ayrıksa (HCF/cc-sdd `_Boundary:_`, appfactory PARALLEL.md "name the files it owns").

### 2.12 Expo + Next.js monorepo starter'ları

- **create-t3-turbo:** `apps/expo` (SDK 54, RN 0.81, Expo Router, NativeWind v5) + `apps/nextjs` (Next 15) + `packages/api|auth|db|ui` + `tooling/`. Ajan kuralı dosyası yok → bizim CLAUDE.md/AGENTS.md eklenecek. En iyi başlangıç.
- **create-expo-stack:** yalnızca mobil; seçenek matrisi zengin.
- **Vercel turborepo-react-native** şablonu, **nexpo** (Solito + Tamagui) alternatifleri.
- Not: "expo nextjs monorepo + CLAUDE.md" araması sonuç vermedi; ajans kendi `AGENTS.md`'sini (yybd `factory-setup`, gstack `agents-digest/gstack-AGENTS.md` örnek alınarak) yazmalı.

### 2.13 Anthropic cookbooks ajan kalıpları (kısa)
Prompt chaining, routing, parallelization, orchestrator-workers, evaluator-optimizer, async multi-agent, latency/budget altında takımlar. Bizim zincir = **prompt chaining** (istasyonlar) + **orchestrator-workers** (ekran/sayfa başına worker) + **evaluator-optimizer** (design-review ↔ fix döngüsü).

---

## 3. Değerli SKILL.md / workflow / prompt metinleri (birebir)

### 3.1 superpowers — brainstorming HARD-GATE ve checklist
Kaynak: https://github.com/obra/superpowers/blob/main/skills/brainstorming/SKILL.md
```
<HARD-GATE>
Before taking any implementation action, including invoking an
implementation skill, writing product code, scaffolding, installing
product dependencies, or creating an external project, complete the
selected path's prerequisites:

- Spike: the human partner approves the question and probe.
- Bounded: the human partner approves the short in-chat design.
- Architectural: the human partner reviews and approves the written spec,
  then reviews the written implementation plan and selects its execution
  method. Conversational design approval only permits writing the spec;
  written-spec approval only permits invoking writing-plans.

A reply approves the stage actually presented. Approval of an idea or
feature scope does not approve artifacts that do not exist yet. Resume
at the earliest incomplete stage; do not turn one approval into permission
to skip the rest of the selected path. Read-only project exploration is
allowed while those prerequisites remain incomplete.
</HARD-GATE>

**Architectural:**
1. **Explore project context** — check files, docs, recent commits
2. **Offer the visual companion just-in-time** — NOT upfront. ...
3. **Ask clarifying questions** — one at a time, understand purpose/constraints/success criteria
4. **Propose 2-3 approaches** — with trade-offs and your recommendation
5. **Present design** — in sections scaled to their complexity, get user approval after each section
6. **Write design doc** — save to `docs/superpowers/specs/YYYY-MM-DD-<topic>-design.md` and commit
7. **Spec self-review** — quick inline check for placeholders, contradictions, ambiguity, scope (see below)
8. **User reviews written spec** — ask user to review the spec file before proceeding
9. **Transition to implementation** — invoke writing-plans skill to create implementation plan

**Spec Self-Review:**
1. **Placeholder scan:** Any "TBD", "TODO", incomplete sections, or vague requirements? Fix them.
2. **Internal consistency:** Do any sections contradict each other? Does the architecture match the feature descriptions?
3. **Scope check:** Is this focused enough for a single implementation plan, or does it need decomposition?
4. **Ambiguity check:** Could any requirement be interpreted two different ways? If so, pick one and make it explicit.
```

### 3.2 superpowers — writing-plans plan başlığı ve task yapısı
Kaynak: https://github.com/obra/superpowers/blob/main/skills/writing-plans/SKILL.md
````markdown
# [Feature Name] Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** [One sentence describing what this builds]
**Architecture:** [2-3 sentences about approach]
**Tech Stack:** [Key technologies/libraries]
**Spec:** [path to the spec/design doc this plan implements]

## Global Constraints
[The spec's project-wide requirements — version floors, dependency limits, naming and copy rules, platform requirements — one line each, with exact values copied verbatim from the spec.]

## Review Focus
[The five input classes or failure modes the spec implies but no task's tests exercise that are most likely to bite a person using this software — one line each ...]

---
### Task N: [Component Name]

**Files:**
- Create: `exact/path/to/file.py`
- Modify: `exact/path/to/existing.py:123-145`
- Test: `tests/exact/path/to/test.py`

**Interfaces:**
- Consumes: [what this task uses from earlier tasks — exact signatures]
- Produces: [what later tasks rely on — exact function names, parameter and return types.]

- [ ] **Step 1: Write the failing test**
- [ ] **Step 2: Run test to verify it fails**   Run: `pytest tests/path/test.py::test_name -v`  Expected: FAIL with "function not defined"
- [ ] **Step 3: Implement `function(input: InputType) -> ResultType` in `exact/path/to/file.py`**
- [ ] **Step 4: Run test to verify it passes**
- [ ] **Step 5: Commit**
````
Self-review: 1. Spec coverage 2. Step scan 3. Type consistency 4. Review Focus 5. Proportion ("A plan several times longer than the spec it implements is a transcript of the program, not a plan.")

### 3.3 superpowers — subagent-driven-development çekirdek kuralları + implementer/reviewer prompt'ları
Kaynak: https://github.com/obra/superpowers/tree/main/skills/subagent-driven-development
```
**Core principle:** Fresh subagent per task + task review (spec + quality) + broad final review = high quality, fast iteration

**Rulings, not stalls.** A running plan does not wait on a human. ... Record every decision in the ledger as
`Ruling: <what you decided> — <why> — <what it costs if wrong>`, and keep going.

Four things stop you, and only these: an irreversible or destructive operation; a security-sensitive action;
a side effect outside this worktree that norms say you ask about first (a merge, a push to a shared branch,
a publish); and a plan so broken that every path forward is a guess.

## Model Selection
Mechanical implementation tasks (isolated functions, clear specs, 1-2 files): use a fast, cheap model.
Integration and judgment tasks: standard model. Architecture and design tasks: most capable model.
Fix-loop escalation (rounds 4-5): a model at least one tier above the implementer that got stuck.
**Always specify the model explicitly when dispatching a subagent.**

### 4. The fix loop  — Five rounds maximum per task:
Rounds 1-3 — resume the original implementer. Rounds 4-5 — dispatch a fresh implementer on a more capable model.
Never fix findings yourself in the controller session.
```
Implementer prompt (özet, tam metin `kopyalar/superpowers/subagent-driven-development/implementer-prompt.md`):
```
Subagent (general-purpose):
  description: "Implement Task N: [task name]"
  model: [MODEL — REQUIRED]
  prompt: |
    You are implementing Task N: [task name]
    ## Task Description
    Read your task brief first: [BRIEF_FILE]
    ## Before You Begin — If you have questions ... **Ask them now.**
    ## Your Job
    1. Implement exactly what the task specifies 2. Write tests (TDD) 3. Verify 4. Commit 5. Self-review 6. Report back
    ## You Do Not Dispatch Subagents
    Do all of this task's work yourself. Never spawn a subagent ... and above all never spawn a reviewer to check your work.
    ## When You're in Over Your Head
    It is always OK to stop and say "this is too hard for me." Bad work is worse than no work.
    (Report statuses: DONE | DONE_WITH_CONCERNS | NEEDS_CONTEXT | BLOCKED)
```
Task reviewer prompt (özet):
```
    You are reviewing one task's implementation: first whether it matches its requirements, then whether it is well-built.
    ## What Was Requested — Read the task brief: [BRIEF_FILE]  Global constraints: [GLOBAL_CONSTRAINTS]
    ## What the Implementer Claims They Built — Read the implementer's report: [REPORT_FILE]
    ## Diff Under Review — Base/Head/Diff file. Read the diff file once ... Do not re-run git commands.
    Your review is read-only on this checkout.
    ## You Do Not Dispatch Subagents
    ## Do Not Trust the Report
    Treat the implementer's report as unverified claims about the code. ... Judge the code on its merits
    (Verdicts: Spec ✅/❌ + Quality Approved/Issues with Critical/Important/Minor)
```

### 3.4 snarktank/ralph — CLAUDE.md (döngü prompt'u, tam metin)
Kaynak: https://github.com/snarktank/ralph/blob/main/CLAUDE.md
```markdown
# Ralph Agent Instructions

You are an autonomous coding agent working on a software project.

## Your Task

1. Read the PRD at `prd.json` (in the same directory as this file)
2. Read the progress log at `progress.txt` (check Codebase Patterns section first)
3. Check you're on the correct branch from PRD `branchName`. If not, check it out or create from main.
4. Pick the **highest priority** user story where `passes: false`
5. Implement that single user story
6. Run quality checks (e.g., typecheck, lint, test - use whatever your project requires)
7. Update CLAUDE.md files if you discover reusable patterns (see below)
8. If checks pass, commit ALL changes with message: `feat: [Story ID] - [Story Title]`
9. Update the PRD to set `passes: true` for the completed story
10. Append your progress to `progress.txt`

## Progress Report Format

APPEND to progress.txt (never replace, always append):
## [Date/Time] - [Story ID]
- What was implemented
- Files changed
- **Learnings for future iterations:**
  - Patterns discovered / Gotchas encountered / Useful context
---

## Consolidate Patterns
If you discover a **reusable pattern** that future iterations should know, add it to the `## Codebase Patterns` section at the TOP of progress.txt ...

## Quality Requirements
- ALL commits must pass your project's quality checks (typecheck, lint, test)
- Do NOT commit broken code
- Keep changes focused and minimal
- Follow existing code patterns

## Browser Testing (If Available)
For any story that changes UI, verify it works in the browser if you have browser testing tools configured (e.g., via MCP):
1. Navigate to the relevant page
2. Verify the UI changes work as expected
3. Take a screenshot if helpful for the progress log

## Stop Condition
After completing a user story, check if ALL stories have `passes: true`.
If ALL stories are complete and passing, reply with:
<promise>COMPLETE</promise>

## Important
- Work on ONE story per iteration
- Commit frequently
- Keep CI green
- Read the Codebase Patterns section in progress.txt before starting
```
`ralph.sh` çekirdeği:
```bash
for i in $(seq 1 $MAX_ITERATIONS); do
  OUTPUT=$(claude --dangerously-skip-permissions --print < "$SCRIPT_DIR/CLAUDE.md" 2>&1 | tee /dev/stderr) || true
  if echo "$OUTPUT" | grep -q "<promise>COMPLETE</promise>"; then exit 0; fi
  sleep 2
done
```
`/ralph` skill'inin story kuralları: "Each story must be completable in ONE Ralph iteration"; sıra: schema → backend → UI → dashboard; "Always include 'Typecheck passes'"; UI story'de "Verify in browser using dev-browser skill"; bad criteria: "Works correctly", "Good UX".

### 3.5 ghuntley Ralph — orijinal prompt parçaları
Kaynak: https://ghuntley.com/ralph/
```
while :; do cat PROMPT.md | claude-code ; done

Your task is to implement missing stdlib and compiler functionality...
choose the most important thing. Before making changes search codebase
(don't assume not implemented) using subagents.

After implementing functionality or resolving problems, run the tests for that unit of code that was improved.

DO NOT IMPLEMENT PLACEHOLDER OR SIMPLE IMPLEMENTATIONS.
WE WANT FULL IMPLEMENTATIONS. DO IT OR I WILL YELL AT YOU
```
Kural: "Only one thing [per loop]"; arama için 500 subagent, build/test için 1 subagent (backpressure).

### 3.6 Anthropic ralph-wiggum plugin — kullanım
Kaynak: https://github.com/anthropics/claude-code/tree/main/plugins/ralph-wiggum
```
/ralph-loop "Build a REST API for todos. Requirements: CRUD operations, input validation, tests. Output <promise>COMPLETE</promise> when done." --completion-promise "COMPLETE" --max-iterations 50
```
İyi prompt: "Phase 1: … Phase 2: … Output <promise>COMPLETE</promise> when all phases done." + "After 15 iterations, if not complete: document what's blocking progress".

### 3.7 spec-kit — specify komutunun kalite checklist'i
Kaynak: https://github.com/github/spec-kit/blob/main/templates/commands/specify.md
```markdown
# Specification Quality Checklist: [FEATURE NAME]
## Content Quality
- [ ] No implementation details (languages, frameworks, APIs)
- [ ] Focused on user value and business needs
- [ ] Written for non-technical stakeholders
- [ ] All mandatory sections completed
## Requirement Completeness
- [ ] No [NEEDS CLARIFICATION] markers remain
- [ ] Requirements are testable and unambiguous
- [ ] Success criteria are measurable
- [ ] Success criteria are technology-agnostic (no implementation details)
- [ ] All acceptance scenarios are defined
- [ ] Edge cases are identified
- [ ] Scope is clearly bounded
- [ ] Dependencies and assumptions identified
## Feature Readiness
- [ ] All functional requirements have clear acceptance criteria
- [ ] User scenarios cover primary flows
- [ ] Feature meets measurable outcomes defined in Success Criteria
- [ ] No implementation details leak into specification
```
Kurallar: "LIMIT: Maximum 3 [NEEDS CLARIFICATION] markers"; önceliklendirme "scope > security/privacy > user experience > technical details"; soru formatı A/B/C/Custom tablo. Converge: "Close the gap between what a feature's specification, plan, and tasks call for and what the codebase currently implements."

### 3.8 Anthropic feature-dev plugin — 7 faz (özet)
Kaynak: https://github.com/anthropics/claude-code/blob/main/plugins/feature-dev/commands/feature-dev.md
```
Phase 1: Discovery — clarify, summarize, confirm
Phase 2: Codebase Exploration — launch 2-3 code-explorer agents in parallel, each returns 5-10 key files; read them all
Phase 3: Clarifying Questions — CRITICAL. DO NOT SKIP. Present all questions in a list; wait for answers
Phase 4: Architecture Design — 2-3 code-architect agents (minimal / clean / pragmatic); recommendation; ask user
Phase 5: Implementation — DO NOT START WITHOUT USER APPROVAL
Phase 6: Quality Review — 3 code-reviewer agents (simplicity/DRY, bugs, conventions); present findings; ask fix now/later
Phase 7: Summary
```

### 3.9 OneRedOak — design-review agent (tam metin) + CLAUDE.md snippet
Kaynak: https://github.com/OneRedOak/claude-code-workflows/blob/main/design-review/design-review-agent.md
```markdown
---
name: design-review
description: Use this agent when you need to conduct a comprehensive design review on front-end pull requests or general UI changes. ... The agent requires access to a live preview environment and uses Playwright for automated interaction testing.
tools: Grep, LS, Read, ..., mcp__playwright__browser_navigate, mcp__playwright__browser_take_screenshot, mcp__playwright__browser_resize, mcp__playwright__browser_snapshot, mcp__playwright__browser_console_messages, ...
model: sonnet
---

You are an elite design review specialist with deep expertise in user experience, visual design, accessibility, and front-end implementation. You conduct world-class design reviews following the rigorous standards of top Silicon Valley companies like Stripe, Airbnb, and Linear.

**Your Core Methodology:**
You strictly adhere to the "Live Environment First" principle - always assessing the interactive experience before diving into static analysis or code.

## Phase 0: Preparation
- Analyze the PR description ... Set up the live preview environment using Playwright. Configure initial viewport (1440x900 for desktop)
## Phase 1: Interaction and User Flow
- Execute the primary user flow; test all interactive states (hover, active, disabled); verify destructive action confirmations; assess perceived performance
## Phase 2: Responsiveness Testing
- Desktop 1440px - capture screenshot; Tablet 768px; Mobile 375px; verify no horizontal scrolling or element overlap
## Phase 3: Visual Polish
- Layout alignment and spacing consistency; typography hierarchy; color palette consistency and image quality; visual hierarchy guides attention
## Phase 4: Accessibility (WCAG 2.1 AA)
- Keyboard navigation (Tab order); visible focus states; Enter/Space activation; semantic HTML; form labels; alt text; contrast 4.5:1
## Phase 5: Robustness Testing
- Invalid inputs; content overflow; loading, empty, error states; edge cases
## Phase 6: Code Health
- Component reuse; design token usage (no magic numbers); established patterns
## Phase 7: Content and Console
- Grammar/clarity; console errors/warnings

**Communication Principles:**
1. Problems Over Prescriptions: "The spacing feels inconsistent with adjacent elements, creating visual clutter."
2. Triage Matrix: [Blocker] / [High-Priority] / [Medium-Priority] / [Nitpick] (prefix "Nit:")
3. Evidence-Based Feedback: screenshots for visual issues; start with positive acknowledgment

### Design Review Summary
[Positive opening and overall assessment]
### Findings
#### Blockers  - [Problem + Screenshot]
#### High-Priority  - [Problem + Screenshot]
#### Medium-Priority / Suggestions  - [Problem]
#### Nitpicks  - Nit: [Problem]
```
CLAUDE.md snippet (https://github.com/OneRedOak/claude-code-workflows/blob/main/design-review/design-review-claude-md-snippet.md):
```markdown
## Visual Development
### Design Principles
- Comprehensive design checklist in `/context/design-principles.md`
- Brand style guide in `/context/style-guide.md`
- When making visual (front-end, UI/UX) changes, always refer to these files for guidance
### Quick Visual Check
IMMEDIATELY after implementing any front-end change:
1. **Identify what changed** - Review the modified components/pages
2. **Navigate to affected pages** - Use `mcp__playwright__browser_navigate` to visit each changed view
3. **Verify design compliance** - Compare against `/context/design-principles.md` and `/context/style-guide.md`
4. **Validate feature implementation** - Ensure the change fulfills the user's specific request
5. **Check acceptance criteria** - Review any provided context files or requirements
6. **Capture evidence** - Take full page screenshot at desktop viewport (1440px) of each changed view
7. **Check for errors** - Run `mcp__playwright__browser_console_messages`
### Comprehensive Design Review
Invoke the `@agent-design-review` subagent for thorough design validation when: completing significant UI/UX features; before finalizing PRs with visual changes; needing comprehensive accessibility and responsiveness testing
```

### 3.10 humbleteam/design-review — puan bantları ve çıktı formatı
Kaynak: https://github.com/humbleteam/design-review/blob/main/SKILL.md
```
- **0/4 - broken**: violates basic accessibility, hierarchy, or trust. Needs a rebuild, not a patch.
- **1/4 - significant rework**: five or more heuristic violations, generic or placeholder copy, flat hierarchy with no clear focal point.
- **2/4 - needs work**: the structure is sound but the craft is weak - typography, spacing, or contrast issues.
- **3/4 - solid, with specific tweaks**: hierarchy and craft are mostly right, 1-3 polish items remain.
- **4/4 - ship-ready**: nothing material to fix, only minor preferences.

**The cost of the fix decides the band. The number of violations does not.**

For each issue, write exactly three lines:
- **Before**: a specific, observable fact. Not "the layout feels cluttered" - "12 UI elements sit inside a single 320px-wide card with no grouping."
- **After**: a fix a person could ship in under an hour.
- **Why**: exactly one citation - a Nielsen heuristic by number and name, a WCAG 2.2 success criterion by number, or a named platform guideline.

## <Artifact name, 2-4 words> - score <X>/4
### 1. <Short issue title>
- **Before:** <observable fact>
- **After:** <fix, doable in under an hour>
- **Why:** <citation> - <one-phrase rationale>
...
**Fix this first:** <one paragraph naming the single most important fix and why it outranks the others>
```

### 3.11 cb-visual-qa — Playwright smoke test (tam)
Kaynak: https://github.com/ChrisBrooksbank/cb-visual-qa/blob/master/commands/cb-visual-qa.md
```typescript
import { test, expect } from '@playwright/test';
const ROUTES = [ { path: '/', name: 'Home' } ];
for (const route of ROUTES) {
  test(`${route.name} (${route.path}) renders visible content`, async ({ page }) => {
    const consoleErrors: string[] = [];
    page.on('console', msg => { if (msg.type() === 'error') consoleErrors.push(msg.text()); });
    const pageErrors: string[] = [];
    page.on('pageerror', err => pageErrors.push(err.message));
    await page.goto(route.path);
    expect(consoleErrors.filter(e => !e.includes('favicon'))).toEqual([]);
    expect(pageErrors).toEqual([]);
    const body = page.locator('body');
    await expect(body).not.toBeEmpty();
    await expect(page.getByText(/something went wrong/i)).not.toBeVisible();
    await expect(page.getByText(/application error/i)).not.toBeVisible();
    const bodyHeight = await body.evaluate(el => el.scrollHeight);
    expect(bodyHeight).toBeGreaterThan(100);
    const mainContent = page.locator('main, [role="main"], #root > *, #__next > *').first();
    await expect(mainContent).toBeVisible();
    await expect(page).toHaveScreenshot(`${route.name.toLowerCase().replace(/\s+/g, '-')}.png`, { fullPage: false, animations: 'disabled' });
  });
}
test('No broken images on homepage', async ({ page }) => {
  const failedImages: string[] = [];
  page.on('response', r => { if (r.request().resourceType() === 'image' && !r.ok()) failedImages.push(r.url()); });
  await page.goto('/'); await page.waitForLoadState('networkidle');
  expect(failedImages).toEqual([]);
});
```
`design.md` şablonu: Aesthetic (bir cümle) · Colours tablosu · Typography (scale 12/14/16/18/24/30/36) · Spacing (4px base) · Component Patterns · Layout · What NOT to Do (inline style yok, hardcoded hex yok, `!important` yok).

### 3.12 GSD gsd-ui-checker — 6 boyut ve BLOCK kuralları
Kaynak: https://github.com/glittercowboy/get-shit-done/blob/main/agents/gsd-ui-checker.md
```
Dimension 1: Copywriting — BLOCK if any CTA label is "Submit", "OK", "Click Here", "Cancel", "Save";
  empty state copy missing or "No data found"; error state has no solution path. FLAG: destructive action without confirmation.
Dimension 2: Visuals — FLAG if no focal point declared; icon-only actions without label fallback.
Dimension 3: Color — BLOCK if accent reserved-for list is empty or says "all interactive elements". FLAG if 60/30/10 split not declared.
Dimension 4: Typography — BLOCK if more than 4 font sizes or more than 2 font weights declared.
Dimension 5: Spacing — BLOCK if any spacing value is not a multiple of 4 (standard set 4, 8, 16, 24, 32, 48, 64).
Dimension 6: Registry Safety — third-party component sources actually vetted.
```
gsd-ui-auditor: "Score each pillar 1-4 ... Assume every pillar has failures until screenshots or code analysis proves otherwise"; grep ile generic label/hardcoded hex/font-size/weight/spacing sayımı; Playwright-MCP varsa 1440/375/768 screenshot.

### 3.13 Anthropic frontend-design skill — anti-slop kalibrasyonu
Kaynak: https://github.com/anthropics/claude-code/blob/main/plugins/frontend-design/skills/frontend-design/SKILL.md
```
For calibration, AI-generated design right now clusters around some traits:
1. a warm cream background (near #F4F1EA) with a high-contrast serif display and a terracotta or warm-clay accent (often near #D97757);
2. a near-black background with a single bright acid-green or vermilion accent;
3. a broadsheet-style layout with hairline rules, zero border-radius, and dense newspaper-like columns;
4. the SaaS-card kit: content chopped into identical rounded cards, one border-radius on everything regardless of hierarchy, the same soft grey shadow (rgba(0,0,0,.1)) under each, and gradient washes as decoration;
5. template chrome that appears whatever the subject: a tracked-out ALL-CAPS eyebrow label above every heading; meta strings joined with middle dots ('A · B · C'); labels built as 'WORD — fragment' with a spaced em dash; tinted near-black (#0B0B0B, #111) standing in for black; a monospace face for small data labels; a '→' appended to link and button text.

Work in two passes. First, brainstorm a short design plan ... (Color: 4–6 named hex values; Type; Layout with ASCII wireframes; Principles).
Then review that plan against the brief before building: if any part of it reads like the generic default you would produce for any similar page ... revise that part, say what you changed and why.

Spend your boldness in one place. ... Build to a quality floor without announcing it: responsive down to mobile, visible keyboard focus, reduced motion respected ... Critique your own work as you build, taking screenshots to review if your environment supports it — a picture is worth 1000 tokens.
```

### 3.14 compound-engineering `/lfg` — otonom koşu adımları
Kaynak: https://github.com/EveryInc/compound-engineering-plugin/blob/main/skills/lfg/SKILL.md
```
1. Produce the work source (plan via ce-plan, or ce-debug fix). Any `status: blocked` stops the run.
2. Invoke ce-work with `mode:return-to-caller <plan-path>`. Only a valid `status: complete` may advance.
3. Invoke ce-simplify-code on the branch diff (skip docs-only / <10 lines).
4. Invoke ce-code-review with `mode:agent plan:<plan-path>`.
5. Apply and persist review fixes.
6. Autonomous residual handoff: record unapplied findings in PR body / DONE report.
7. Invoke ce-compound `mode:non-interactive` when durable learning exists.
8. Invoke ce-test-browser `mode:pipeline`.
9. Invoke ce-commit-push-pr `mode:pipeline branding:on`.
10. Watch the PR to CI-decided with ce-babysit-pr `mode:pipeline <pr-url>`.
11. Output `<promise>DONE</promise>`.

**Stop, and say why, when** ... A child return is anything but complete and evidenced. ... A stop leaves nothing pushed that was not already pushed.
```

### 3.15 production-grade — Loop Engine kuralı
Kaynak: https://github.com/nagisanzenin/claude-code-production-grade-plugin (README)
```
> A task is done when a check it cannot argue with says so — not when the agent claims it.
That check is an *oracle*: a compiler, a type checker, a test suite, or the running app clicking its own buttons. No oracle, no loop.

PRODUCE → ORACLE (test·type·app) → red → DELTA BACK (failing output only → re-loop) → green → CONVERGED; no progress ×2 → plateau → escalate strategy, not effort
- Oracle bootstrap: fast check (typecheck + lint, under 15s) and full check (tests + build + boot smoke).
- Enforced by a hook, not a prompt: a `PostToolUse` hook runs the fast oracle after every file edit.
- Separated duties: QA writes failing tests first and owns `tests/`. A coding agent that weakens a test to go green gets flagged with a Critical finding.
- Functional drive: an agent boots the real app and drives it — every button, every form, every link. A control that renders but does nothing is a Critical bug.
```

### 3.16 smart-ralph spec-executor — "kanıt olmadan tamam yok"
Kaynak: https://github.com/tzachbon/smart-ralph/blob/main/plugins/ralph-speckit/agents/spec-executor.md
```
"Complete" means VERIFIED WORKING IN THE REAL ENVIRONMENT, not just "code compiles".
**NEVER mark TASK_COMPLETE based only on:** "Code compiles" - NOT ENOUGH / "Tests pass" - NOT ENOUGH (tests might be mocked) / "It should work" - NOT ENOUGH
**ONLY mark TASK_COMPLETE when you have PROOF:** You ran the feature in a real environment; you verified the external system received/processed the data; you have concrete evidence (API response, screenshot, log output)
FORBIDDEN TOOLS: `AskUserQuestion` - NEVER ask the user questions, you are fully autonomous
Phase 1 (POC): skip tests, accept hardcoded values, only type check must pass. Phase 2 (Refactoring). Phase 3 (Testing). Phase 4 (Quality gates).
```

### 3.17 BMAD edge-case-hunter — review çıktı şeması
Kaynak: https://github.com/bmad-code-org/BMAD-METHOD/blob/main/skills/bmad-build/review-prompts/edge-case-hunter.md
```
**Goal:** You are a pure path tracer. Never comment on whether code is good or bad; only list missing handling.
Return ONLY a valid JSON array of objects:
[{ "location": "file:start-end", "trigger_condition": "one-line (max 15 words)", "guard_snippet": "minimal code sketch", "potential_consequence": "what could go wrong (max 15 words)" }]
No severity, priority, or ranking anywhere.
```

### 3.18 context-engineering PRP — validation gates
Kaynak: https://github.com/coleam00/context-engineering-intro/blob/main/.claude/commands/generate-prp.md
```
### Validation Gates (Must be Executable) eg for python
ruff check --fix && mypy .
uv run pytest tests/ -v
Score the PRP on a scale of 1-10 (confidence level to succeed in one-pass implementation using claude codes)
Remember: The goal is one-pass implementation success through comprehensive context.
```

### 3.19 jezemm appfactory — UX polish boyutları ve "decide, don't ask"
Kaynak: https://github.com/jezemm/appfactory-plugin/tree/main/skills/appfactory (tescilli lisans; yalnızca ilham)
```
- **Ask up front, then never.** ... Once the build starts, do not ask again.
- **Decide, don't ask.** ... write it to `.appfactory/DECISIONS.md` (one line each: decision — why).
- **Verify your own work** before calling anything done.
Phase 5: scaffold → prove the pipeline on the EMPTY app (TestFlight'a kadar) → build functionality → `appfactory verify` until exit 0 → UX polish → public page
UX-POLISH: 1 First impressions & hierarchy · 2 User flows · 2b First run (3 onboarding cards, last button = first real action) · 3 Micro-interactions (every control ≥8/10) · ... "Put all the screenshots side by side at the end and look at the chrome across them"
"A literal hex value or pixel padding in a component is a finding."
```

### 3.20 yybd app-factory — yayın sırası (APP-LIFECYCLE)
Kaynak: https://github.com/yybd/app-factory/blob/main/APP-LIFECYCLE.md
```
0  identity            app-identity · app-profile (one dossier every later skill quotes)
1  the binary          credentials → signing → compliance → i18n → icon → build
2  the privacy URL     required before any store will accept a submission
3  media               appstore-media · play-store-media      needs a build from 1
4  version             the build number / versionCode
5  copy                store-metadata-writer → the per-store metadata skills
6  deliver listing     app-store-deliver · play-store-deliver   pull-and-diff first
7  ship the binary     ship-apple-app · play-store-ship
8  after               reviews-responder · price-sync · aso-keywords
```

### 3.21 HCF — 8 hook noktası
Kaynak: https://github.com/markshust/hcf (README/HOOKS.md)
```
pre-plan | post-plan | pre-implementation | pre-batch | post-batch | post-implementation | pre-commit | post-commit
---
name: devils-advocate
phase: post-plan   # one of the 8 hook points
order: 10          # lower runs first; default 100
mode: single       # "single" | "batch"
---
```

---

## 4. Sonuç: omurga ve önerilen birleşik akış

### 4.1 Omurga olacak 3 sistem ve nedeni

1. **superpowers** (fikir → spec → plan → subagent-driven implement → review): En olgun, harness-bağımsız, prompt'ları kanıtlanmış; "taze implementer + bağımsız reviewer + ledger" modeli fabrika bandının temelidir. Bize eksik olan tek şey görsel kapı — ekliyoruz.
2. **Ralph döngüsü (snarktank prd.json + Anthropic Stop-hook)**: Uzun koşulan "montaj hattı" için en dayanıklı ve ucuz mekanizma; story başına taze bağlam, `progress.txt` hafıza, `<promise>` çıkış. superpowers planını `prd.json` story'lerine dönüştürüp gece koşturmak için.
3. **compound-engineering `/lfg` + gstack kalite kapıları**: `/lfg` yayın kuyruğunun (simplify → review → browser test → PR → CI babysit) iskeleti; gstack'ten `plan-design-review` / `design-review` / `qa` mantığı ve OneRedOak+humbleteam+cb-visual-qa üçlüsü **görsel kalite kapısı** olarak. `ce-compound` ise ajansın müşteriden müşteriye biriken bilgi bankası.

Destek parçalar: spec-kit şablonları (spec/plan/tasks formatı + `extensions.yml` hook'ları), BMAD `DESIGN.md + EXPERIENCE.md` UX spine'ı ve PM/UX/Architect personaları, GSD ui-checker/ui-auditor boyutları, production-grade "oracle" ve PostToolUse hook'u, yybd app-factory store yayın sırası, create-t3-turbo monorepo.

### 4.2 Önerilen birleşik akış — "start" denince

```
/start "<müşteri brief'i>"
 0. INTAKE (tek seferlik soru turu)        — snarktank /prd tarzı A/B/C sorular (3-5); sonrası "decide, don't ask" (appfactory) → DECISIONS.md
 1. BRAINSTORM → SPEC                      — superpowers brainstorming (architectural path) + spec-kit spec şablonu; spec kalite checklist'i KAPI-1
 2. UX SPINE                               — BMAD bmad-ux: DESIGN.md (tokens: colors/type/spacing/radius) + EXPERIENCE.md (IA, states, flows)
                                             frontend-design anti-slop iki geçişi; gstack plan-design-review 0-10 puanı; GSD ui-checker 6 boyut BLOCK yoksa geç → KAPI-2
 3. ARCHITECTURE + PLAN                    — superpowers writing-plans (Task N: Files/Interfaces/Steps, Global Constraints, Review Focus)
                                             cc-sdd _Boundary:_/_Depends:_ ek alanları; BMAD architecture; plan self-review → KAPI-3 (insan onayı, tek nokta)
 4. SCAFFOLD + "boş app'i yayınla"         — create-t3-turbo (web+expo) + AGENTS.md; cb-visual-qa kurulumu (design.md ← DESIGN.md, smoke test, baseline)
                                             appfactory ilkesi: boş iskeleti Vercel/EAS/TestFlight'a kadar götür → KAPI-4 (pipeline kanıtlandı)
 5. BUILD (montaj hattı)                   — plan → prd.json story'leri (küçük, bağımlılık sıralı, her story: "Typecheck passes", UI ise "screenshot + design-review ≥3/4")
                                             Ralph döngüsü (Stop-hook veya ralph.sh): story başına superpowers implementer prompt'u → task reviewer
                                             production-grade PostToolUse hızlı oracle (typecheck+lint <15s); paralel yalnızca ayrık Boundary'lerde (worktree)
 6. GÖRSEL QA KAPISI (her UI story + faz sonu) — KAPI-5
                                             a) cb-visual-qa smoke+baseline (deterministik)  b) OneRedOak design-review agent (Playwright MCP: 1440/768/375, etkileşim, WCAG, konsol)
                                             c) humbleteam rubric puanı (≥3/4 değilse Before/After/Why ile fix döngüsü, max 3 tur)  d) GSD ui-auditor 6 sütun + anti-slop klişe taraması
                                             e) production-grade functional drive (her buton/form/link)  → UI-REVIEW.md + screenshot'lar yan yana
 7. REVIEW + SIMPLIFY                      — superpowers whole-branch review (en güçlü model) + BMAD edge-case-hunter/verification-gap lensleri (JSON) + ce-simplify-code → KAPI-6
 8. SHIP                                   — /lfg kuyruğu: compound (öğrenme kaydı) → ce-test-browser pipeline → commit/PR → CI babysit → deploy (Vercel) / EAS build
                                             mobil mağaza: yybd app-factory sırası (identity → binary → privacy URL → media → copy → deliver → ship) → KAPI-7 (store gereksinimleri)
 9. PAZARLAMA ÇIKTILARI                    — aynı DESIGN.md + DECISIONS.md'den landing/SEO (appfactory MARKETING-SITE/SEO), reklam ve sosyal medya skill'leri (diğer kategori notları)
10. CANARY + RETRO + COMPOUND             — gstack canary mantığı; ce-compound ile docs/solutions/ → sonraki müşteri projesi bunu okur
```

**Kapı özeti:** K1 spec checklist · K2 UX spine (ui-checker BLOCK=0, plan-design ≥7/10) · K3 plan onayı (tek insan noktası) · K4 boş iskelet yayında · K5 görsel QA (smoke yeşil + design-review ≥3/4 + functional drive temiz) · K6 branch review temiz · K7 mağaza/deploy gereksinimleri.

**Uygulama sırası önerisi:** (1) superpowers + ralph-wiggum + frontend-design plugin'lerini kur; (2) OneRedOak agent + CLAUDE.md snippet'i ve humbleteam SKILL.md'yi `.claude/` altına koy; (3) cb-visual-qa komutuyla her projede smoke test üret; (4) `/lfg` benzeri tek bir `/start` skill'i yaz: yukarıdaki 10 adımı sırayla çağırır, her kapıda "promise" string'i üretir; (5) `ce-compound` tarzı `docs/solutions/` bilgi bankasını ajans düzeyinde ortak repo yap.
