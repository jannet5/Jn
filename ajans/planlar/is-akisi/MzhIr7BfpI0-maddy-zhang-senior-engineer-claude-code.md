# How I use Claude Code (Senior Software Engineer Tips) — Maddy Zhang — 9 dk — https://www.youtube.com/watch?v=MzhIr7BfpI0
> Kaynak notu: Transkript: var (yt-dlp, 2026-10-10) — `arastirma/youtube/transkriptler/MzhIr7BfpI0.txt`; zaman damgaları açıklamadan (`transkriptler/MzhIr7BfpI0.aciklama.txt`). Mekanik ayrıntıların bir kısmı (dosya yolları) Anthropic dokümanıyla (`kaynaklar/gv0WHhKelSE-anthropic-best-practices-dokumani.md`) desteklendi → "(doküman)". Güven: **yüksek**.

## Tek paragraf
Eski Google mühendisi Maddy Zhang, 9 dakikada "kıdemli mühendis" Claude Code düzenini anlatıyor: AI'yı "biraz daha iyi Google araması" gibi değil **force multiplier** gibi kullan. Sıra: IDE eklentisi (açık dosya otomatik bağlama girer) → ilk iş `/init` ile CLAUDE.md (1:38; "yeni mühendis için onboarding dokümanı", 100-200 satır üstü sinyali seyreltir) → model seçimi (varsayılan Opus, limitte Sonnet, hafif iş Haiku) → her kodlamadan önce Plan Mode (2:59) → görevler arası `/clear` → haftada bir kereden fazla yapılan her şey için özel slash komutu → hooks ile build+test+typecheck **self-validation loop** (4:37) → paralel oturumlar (IDE panelleri veya git worktree, 5:40) → `.claude/agents` sub-agent'ları → skill'ler (tam playbook) → MCP (7:50) → üç ipucu: spesifik prompt, küçük adımlar, **plan → execute → review** (8:24).

## Önerdiği iş akışı (adım adım)
1. **Kurulum**: VS Code/Cursor eklentisi ya da terminal. "When most people install Claude Code, they immediately start throwing prompts at it. Don't do this." → önce `/init`.
2. **CLAUDE.md** (1:38): üç şey — "what the project does, where things live, and how work gets done": tech stack, kurallar/konvansiyonlar, kilit dosya yolları, gerçek dev komutları (test/build/lint); istersen "who you are" açıklaması. Yalın tut: "maybe 100 to 200 lines max".
3. **Model**: varsayılan Opus; limitte otomatik Sonnet; çok hafif işte Haiku.
4. **Plan Mode önce** (2:59): hangi dosyalar değişecek, mantık akışı, riskler; planı itiraz et/düzenle, sonra uygulat. "I recommend you always go use plan mode before writing a single line of code."
5. **`/clear` alışkanlığı**: her görev bitince; "Each task should be a clean session with a fresh context." Eski oturuma yukarı ok ile dönülür.
6. **Özel slash komutları**: `.claude/commands/` altında düz markdown kayıtlı prompt'lar; kural: haftada bir kereden fazla yaptığın her iş için komut.
7. **Self-validation loop** (4:37): hooks ile build + test + type checker her değişiklikten sonra; kurulumu ~20 dk; hata = blocker.
8. **Paralel çalışma** (5:40): basit yol — IDE'de birden çok panel, farklı dosyalarda farklı işler; ciddi yol — her feature/fix branch'i için `git worktree` + ayrı oturum, sonra dön, gözden geçir, merge et.
9. **Sub-agent'lar**: `.claude/agents/` — her birinin adı, izinli araçları ve odak talimatı; ana oturum güvenlik review'unu "security reviewer"a, DB sorgusunu "DB specialist"e devreder; izole bağlam.
10. **Skill'ler**: kod review, refactor, release gibi çok adımlı iş akışını kodlayan markdown; başka dosyalara referans verebilir, sub-agent'larca çağrılabilir.
11. **MCP** (7:50): GitHub, Slack, veritabanları, Perplexity; "pull comments from a GitHub PR and address them", "check Slack for the latest design feedback ... before writing code".
12. **Her şeyin özeti** (8:24): spesifik prompt, küçük adımlar, plan → execute → review.

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- CLAUDE.md içerik örneği (sözlü): build komutu "npm run dev and not yarn", API route klasörleri, "you use functional components with hooks".
- "Stick to the essentials, maybe 100 to 200 lines max. Anything more and you're actually diluting the signal."
- Doğrulama talimatı (CLAUDE.md veya hooks config'e): "After every edit, run the build, run the test, run the type check. If anything fails, treat it as a blocker and [solve] it before moving on."
- Slash komut örneği: PR summary komutu — "what changed, why, any risks, and how to test it". Kural: "anything you do more than once a week in your engineering workflow, there should be a slash command for."
- Komut vs skill: "A slash command is a quick trigger, one prompt, one action. A skill is a full playbook." / "institutional knowledge that travels with your codebase."
- Küçük adım gerekçesi: "If a change spans 15 files and breaks three things, it's hard to know where to start fixing it. If it spans two files and breaks one thing, you're back on track in 5 minutes."
- Planı atlamanın bedeli: "code that technically works, but solves the wrong problem or makes the right change in the wrong place."

## Araçlar ve linkler
- Claude Code: VS Code/Cursor eklentisi, `/init`, CLAUDE.md, Opus/Sonnet/Haiku, Plan Mode (`Shift+Tab`, doküman), `/clear`, `.claude/commands/`, hooks, `git worktree`, `.claude/agents/`, skills, MCP (GitHub, Slack, veritabanları, Perplexity)

## Hatalar / "bunu yapma" uyarıları
- AI'yı "slightly better Google search" / "fancy chatbot" gibi kullanmak (0:00).
- Kurar kurmaz prompt atmak → önce `/init`.
- Devasa CLAUDE.md ("more context is better, but it's not").
- Plan olmadan kod → yanlış problemi/yanlış yerde çözen kod, saatlerce debug.
- Oturumu "hiç kesmek istemediğin sohbet" gibi sürdürmek → bağlam dolar, kısıtlar unutulur.
- Aynı prompt'u tekrar tekrar yazmak → slash komut.
- Dev görev tek seferde → "This will go poorly."

## Bizim fabrikaya alınacaklar (somut)
1. **Doğrulama döngüsü şablonu** her müşteri repo'suna: `npm run check` = lint + typecheck + test + build tek komut; hook ile her düzenlemeden sonra; CLAUDE.md'de Maddy'nin cümlesi: "Hata = blocker, geçmeden ilerleme / bitti deme".
2. **"Haftada >1 = komut" kuralı**: fabrikada tekrar eden her adım komut/skill olur — `/teslim-hazirla` (check + screenshot + PR), `/pr-ozet` (ne değişti, neden, riskler, nasıl test edilir), `/musteri-raporu`; çok adımlı olanlar skill.
3. **Müşteri repo CLAUDE.md'si üç başlık**: proje ne yapar / neler nerede / iş nasıl yapılır (komutlar, konvansiyonlar); üst sınır bizde 60 satır (Maddy 100-200 diyor; Anthropic dokümanı daha sıkı).
4. **Worktree disiplini**: `ajans/sistem/PARALEL.md`: görev başına worktree, isimlendirme `wt/<musteri>-<is>`, ana beyin dağıtır/toplar ve merge öncesi review eder.
5. **`/clear` kuralı** CLAUDE.md'ye: "Yeni sipariş kalemi = /clear; aynı konuda 2 düzeltmeden sonra /clear + yeni prompt".
6. **Sub-agent seti**: `security-reviewer`, `design-reviewer`, `db-specialist` — her birinin izinli araçları kısıtlı.
7. **Model politikası**: üretim/mimari Opus, rutin içerik/format işleri Sonnet/Haiku (maliyet).
8. MCP: GitHub (PR yorumlarını çek ve uygula), Slack/Drive (müşteri tasarım geri bildirimi kod öncesi okunur) — her hat için standart `.mcp.json`.
