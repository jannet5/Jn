# Claude Code best practices | Code w/ Claude — Anthropic — 25 dk — https://www.youtube.com/watch?v=gv0WHhKelSE
> Kaynak notu: Transkript: var (yt-dlp, 2026-10-10) — `arastirma/youtube/transkriptler/gv0WHhKelSE.txt` (İng. otomatik altyazı). Konuşmacı: Cal (Anthropic Applied AI, Claude Code çekirdek katkıcısı; system prompt/araç açıklamaları/eval). Konuşma Mayıs 2025 (Claude 4 çıkış günü) ve **dokümandan çok daha kısa/eski**: CLAUDE.md, izinler, CLI/MCP, `/clear`–`/compact`, plan+to-do, "smart vibe coding", ekran görüntüsü, çoklu Claude, Escape, headless. Spec mülakatı, `/goal`, Stop hook, Writer/Reviewer, fan-out vb. konuşmada **yok** → güncel resmi dokümandan (`kaynaklar/gv0WHhKelSE-anthropic-best-practices-dokumani.md`) gelir ve "(doküman)" ile işaretlidir. Güven: **yüksek** (konuşma transkriptten aynen; doküman kısmı dokümandan aynen).

## Tek paragraf
Anthropic'in 22 Mayıs 2025 "Code w/ Claude" konferansındaki resmi "best practices" konuşması (Cal). Önce zihinsel model: Claude Code "terminalde her şeyi yapan iş arkadaşı"; Anthropic'in "simple thing that works" ilkesiyle **saf ajan** = talimat + güçlü araçlar + model işi bitirene kadar döngü. Kod tabanını indeksleme/RAG ile değil **agentic search** (glob, grep, find) ile keşfeder. Kullanım alanları: yeni kod tabanına onboarding, "düşünce ortağı", sıfırdan/var olan kodda yazma (ekip içinde test kapsamı ve commit/PR mesajları bu yüzden çok iyi), headless/CI, büyük migrasyonlar, git/docker/bigquery gibi CLI'lar. Pratikler: CLAUDE.md, izin yönetimi, CLI araçları (MCP'ye tercih), bağlam yönetimi (`/clear`, `/compact`), plan + to-do listesi izleme, TDD'li "smart vibe coding", ekran görüntüsü, paralel Claude'lar, Escape ile araya girme, headless otomasyon, "think hard", changelog'u haftalık takip. Güncel doküman bunun üstüne doğrulama kontrolü, Explore→Plan→Code→Commit, spec mülakatı, hooks/skills/subagents ve fan-out ekler (doküman).

## Önerdiği iş akışı (adım adım)
1. **Kurulum (bir kez)**: CLAUDE.md yaz (proje/ev dizini; test komutu, proje haritası, stil kılavuzu — "build these things up over time"); izinleri ayarla ("if you just are like tired of saying yes run npm run test you can just always approve that"); auto-accept (`Shift+Tab`); `gh` gibi CLI'ları kur ya da MCP ekle; iç araçlarını CLAUDE.md'de anlat. (doküman: `/init`, `/permissions`, `claude mcp add`, **hook** = "her seferinde istisnasız", `.claude/skills/`, `.claude/agents/`.)
2. **Keşif / düşünce ortağı** (konuşma): kod yazdırmadan önce seçenek iste — "search around and kind of figure out how we would do it and maybe report back with like two or three different options. Don't start writing any files yet". Yeni kod tabanında: "look at this file and look at the git history and just kind of tell me a story about how this code has changed".
3. **Büyük iş → önce spec** (doküman): "Interview me in detail using the AskUserQuestion tool..." → SPEC.md → **yeni oturum**.
4. **Plan + to-do** (konuşma): "Hey, I have this bug. Can you search around, figure out what's causing it, and just like tell me a plan how we're going to fix it?" → planı oku/doğrula; çalışırken **to-do listesini izle**, yanlışsa Escape → "let's change the to-do list". (doküman: plan mode `Shift+Tab` ×2, `Ctrl+G` ile planı editörde düzelt, tek cümlelik diff'te planı atla.)
5. **Code + verify — "smart vibe coding"** (konuşma): TDD, küçük değişiklik → testleri koş → geçtiğini gör; her seferinde TypeScript/lint kontrolü; **düzenli commit** ("if it's kind of going off the rails, you can always fall back"). Görselde: "look at this mock.png and then build the website". (doküman: kontrolün sertliği aynı prompt → `/goal` → **Stop hook** → doğrulama subagent'ı; kanıt iste.)
6. **Review** (doküman): `/code-review` ya da fresh-context subagent: "Use a subagent to review the diff against PLAN.md ... Report gaps, not style preferences."
7. **Commit** (konuşma): "hey claude write the commit for me write the PR message for me".
8. **Oturum hijyeni** (konuşma): bağlam 200k; dolarken iki seçenek — `/clear` (CLAUDE.md hariç her şeyi siler) ya da `/compact` (özet → yeni oturumu tohumlar). Escape = "your best friend"; **Escape ×2** sohbette geri atlar. (doküman: iki başarısız düzeltmeden sonra `/clear`, `/compact <odak>`, araştırmayı subagent'a ver.)
9. **Ölçek** (konuşma): aynı anda birden çok Claude ("I know people that do four" — tmux/sekmeler); headless/SDK ile CI/CD, GitHub Actions. Ajanlar arası bağlam paylaşımı: ortak markdown dosyası (bkz. aşağıda ticket.md). (doküman: worktrees/cloud, Writer-Reviewer, `/batch`, `for ... claude -p ... --permission-mode dontAsk` fan-out.)

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
**Konuşmadan (transkript):**
- Ajan tanımı: "some instructions, some powerful tools, and you let the model just run in a loop until it decides it's done."
- CLI vs MCP: "if you're using something like um, a CLI tool that's well known and well documented ... I would recommend using the CLI tool."
- Thinking: Claude 4 araç çağrıları arasında düşünebiliyor → "when you're working on tasks and solving bugs, throw a think hard in there."
- Alt dizin CLAUDE.md'leri varsayılan okunmaz (monorepo'da bağlamı şişirdiği için); yalnız çalışma dizinindeki + ev dizinindeki okunur; diğerleri `@dosya` ile içe aktarılır.
- Model değişince CLAUDE.md'yi yeniden gözden geçir: "do I still need this stuff? Maybe I can take some of it out."
- Ajanlar arası devir (anekdot): "I need you to write some stuff in like ticket.md for another developer" → yeni oturumda "read ticket.md like another developer left this note for you".
- "I check this [changelog] once a week".

**Dokümandan (doküman):**
- CLAUDE.md örneği:
```
# Code style
- Use ES modules (import/export) syntax, not CommonJS (require)
- Destructure imports when possible (eg. import { foo } from 'bar')

# Workflow
- Be sure to typecheck when you're done making a series of code changes
- Prefer running single tests, and not the whole test suite, for performance
```
- CLAUDE.md testi: "Would removing this cause Claude to make mistakes?" Değilse sil. Tek satıra "IMPORTANT" ekle, çoğuna değil. `/doctor` budama önerir.
- Doğrulama prompt'u: "write a validateEmail function. example test cases: user@example.com is true, invalid is false, user@.com is false. run the tests after implementing"
- UI: "[paste screenshot] implement this design. take a screenshot of the result and compare it to the original. list differences and fix them"
- Hata: "the build fails with this error: [paste error]. fix it and verify the build succeeds. address the root cause, don't suppress the error"
- Mülakat: "I want to build [brief description]. Interview me in detail using the AskUserQuestion tool. Ask about technical implementation, UI/UX, edge cases, concerns, and tradeoffs. Don't ask obvious questions, dig into the hard parts I might not have considered. Keep interviewing until we've covered everything, then write a complete spec to SPEC.md."
- Writer/Reviewer: "Review the rate limiter implementation in @src/middleware/rateLimiter.ts. Look for edge cases, race conditions, and consistency with our existing middleware patterns." → "Here's the review feedback: [Session B output]. Address these issues."
- Hook prompt'ları: "Write a hook that runs eslint after every file edit" / "Write a hook that blocks writes to the migrations folder."
- Compaction kuralı (CLAUDE.md'ye): "When compacting, always preserve the full list of modified files and any test commands"
- Skill ve subagent dosya örnekleri (api-conventions, fix-issue, security-reviewer) → kaynak dosyasında aynen.

## Araçlar ve linkler
- Konuşmada: CLAUDE.md, izin sistemi/auto-accept (`Shift+Tab`), `gh`, MCP, `/clear`, `/compact`, to-do listesi, Escape / Escape×2, `/model`, `/config`, "think hard", VS Code & JetBrains entegrasyonu, Claude Code SDK/headless, GitHub Actions, changelog: github.com/anthropics/claude-code
- Doküman: https://code.claude.com/docs/en/best-practices ; ilgili: /docs/en/memory, /docs/en/skills, /docs/en/sub-agents, /docs/en/hooks-guide, /docs/en/mcp, /docs/en/worktrees, /docs/en/headless, /docs/en/workflows, /docs/en/agent-teams
- Komutlar (doküman): `/init`, `/context`, `/doctor`, `/permissions`, `/sandbox`, `/hooks`, `/plugin`, `/rewind`, `/btw`, `/rename`, `/code-review`, `/batch`, `/goal`, `/verify`, `claude -p`, `claude mcp add`, `claude --continue|--resume`, `claude --permission-mode plan|auto`

## Hatalar / "bunu yapma" uyarıları
- (konuşma) Her şeyi Enter'a basıp sonunda bakmak → TDD + küçük adım + düzenli commit; yoldan çıkınca geri dön.
- (konuşma) Yanlış yola giden to-do listesini izlememek → Escape ile müdahale et; ne zaman Escape ne zaman "bırak çözsün" ayrımı kilit beceri.
- (konuşma) Monorepo'da tüm CLAUDE.md'leri yüklemek → bağlam patlar; alt dizinleri `@` ile seçerek al.
- (konuşma) "Gereksiz yorum satırları" CLAUDE.md'ye rağmen sürdü → bu model sorunuydu (3.7), Claude 4'te büyük ölçüde düzeldi; model değişince kuralları yeniden test et.
- (doküman) Kitchen sink session → `/clear`; iki başarısız düzeltmeden sonra `/clear` + daha iyi prompt.
- (doküman) Şişmiş CLAUDE.md → acımasız buda; kuralı hook'a çevir.
- (doküman) "If you can't verify it, don't ship it." ; sonsuz keşif → kapsamı daralt/subagent.
- (doküman) Reviewer her zaman bir şey bulur → sadece doğruluk/gereksinimi etkileyenleri düzelt.
- (doküman) Plan mode'u küçük işlerde kullanma (overhead).

## Bizim fabrikaya alınacaklar (somut)
1. **Her hat için "doğrulama kontrolü" zorunlu** (doküman + konuşmadaki "smart vibe coding"): web → Playwright ekran görüntüsü + Lighthouse; app → Expo build + Maestro/screenshot; reklam → karakter limiti/politika lint betiği; sosyal → şablon doğrulama. Kontrol Stop hook'u olarak `.claude/settings.json`'a girer; geçmeden tur bitmez.
2. **Sipariş = SPEC.md** (doküman): müşteri brief'i geldiğinde ana beyin "Interview me..." kalıbıyla eksikleri sorar, `SPEC.md` üretir; üretim alt sohbeti sadece spec ile **temiz bağlamda** açılır.
3. **Önce 2-3 seçenek, sonra kod** (konuşma): her yeni hat/tasarım kararında "report back with two or three different options. Don't start writing any files yet" kalıbı; seçimi insan/ana beyin yapar.
4. **Alt sohbetler arası devir = `ticket.md`** (konuşma): ana beyin alt sohbete işi bir not dosyasıyla devreder ("another developer left this note for you"); alt sohbet bitince aynı dosyaya sonuç yazar. Bizim `SPEC.md`/teslim notu standardı bununla birleşir.
5. **Explore→Plan→Code→Commit** her üretim sohbetinin iskeleti; plan insan tarafından onaylanır (kalite kapısı 1); çalışırken to-do listesi izlenir; düzenli commit = geri dönüş noktası.
6. **Fresh-context review** (doküman): teslim öncesi `/code-review` + "review against SPEC.md, report gaps not style" subagent'ı (kalite kapısı 2).
7. **CLAUDE.md ≤ 60 satır**, sadece koddan okunamayanlar; alt klasör CLAUDE.md'leri `@` ile; **model sürümü değişince CLAUDE.md/skill kuralları yeniden test edilir** (konuşma). Alan bilgisi → skill'lere.
8. **CLI > MCP** (konuşma): iyi belgelenmiş CLI varsa (gh, vercel, eas, gcloud) MCP yerine onu kullan; MCP sadece CLI'ı olmayan servisler için.
9. **Ortak skill paketi (plugin)** (doküman): `brief-to-spec`, `design-review`, `ads-variants`, `social-calendar`, `publish-checklist` + `security-reviewer`/`design-reviewer` agent'ları.
10. **Fan-out** (doküman): aynı şablonla çok müşteri → `for ... claude -p ...` veya `/batch`. **Oturum disiplini** CLAUDE.md'ye: "Bir müşteri = bir oturum; hat değişince /clear; iki düzeltmeden sonra yeniden başlat."
