# Agent Teams (resmi doküman) — NruhNt5cxqg / ggvKF8F01VI / -1K_ZWDKpU0 / RpUTF_U4kiw için ortak kaynak

- URL: https://code.claude.com/docs/en/agent-teams — Çekildi 2026-10-09 (WebFetch).

## Ne / ne zaman
- Deneysel, varsayılan kapalı: `CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS=1` (settings.json `env` ya da shell).
- Bir **team lead** oturumu + bağımsız **teammate** oturumları; paylaşılan **task list** + **mailbox** mesajlaşma. Teammate'ler birbirine doğrudan yazar; kullanıcı da her teammate'e doğrudan yazabilir.
- Önce hafif seçenekleri düşün: **subagents** (tek oturum içinde, sonuç ana ajana döner, daha ucuz) ve **cross-session messaging**.
- En iyi kullanım: araştırma/review, birbirinden bağımsız modüller, rakip hipotezlerle debugging, katmanlar arası (frontend/backend/test) iş. Sıralı işler, aynı dosya düzenlemeleri, çok bağımlılık → tek oturum/subagent.
- Karşılaştırma tablosu: Subagent = kendi bağlamı, sonuç çağırana döner, ana ajan yönetir, düşük token. Agent team = tam bağımsız, doğrudan mesajlaşma, öz-koordinasyon + ortak görev listesi, yüksek token.

## Komut/prompt örnekleri (aynen)
- "I'm designing a CLI tool that helps developers track TODO comments across their codebase. Spawn three teammates to explore this from different angles: one on UX, one on technical architecture, one playing devil's advocate."
- "Spawn 4 teammates to refactor these modules in parallel. Use Sonnet for each teammate."
- Plan mode'da lead → "Spawn an architect teammate to refactor the authentication module." (teammate önce planlar, lead onaylar)
- "Spawn a teammate using the security-reviewer agent type to audit the auth module." (`.claude/agents/` tanımı teammate olarak kullanılabilir)
- Paralel review: "Spawn three teammates to review PR #142: - One focused on security implications - One checking performance impact - One validating test coverage. Have them each review and report findings."
- Rakip hipotezler: "Users report the app exits after one message instead of staying connected. Spawn 5 agent teammates to investigate different hypotheses. Have them talk to each other to try to disprove each other's theories, like a scientific debate. Update the findings doc with whatever consensus emerges."
- "Wait for your teammates to complete their tasks before proceeding"
- "Ask the researcher teammate to shut down"
- Görünüm: `teammateMode: "auto" | "tmux" | "in-process" | "iterm2"` (`claude --teammate-mode auto`); tmux ile bölünmüş paneller.

## Best practices (dokümandan)
- Teammate'ler CLAUDE.md/MCP/skill'leri yükler ama lead'in konuşma geçmişini görmez → spawn prompt'una görev detayını yaz (örnek: "Review the authentication module at src/auth/ for security vulnerabilities. Focus on token handling, session management, and input validation. The app uses JWT tokens stored in httpOnly cookies. Report any issues with severity ratings.").
- 3-5 teammate ile başla; "Three focused teammates often outperform five scattered ones." Teammate başına 5-6 görev.
- Her teammate farklı dosya setine sahip olsun (aynı dosya = ezme).
- Araştırma/review ile başla, sonra paralel implementasyon.
- Hook'larla kalite kapısı: `TeammateIdle`, `TaskCreated`, `TaskCompleted` (exit 2 → geri bildirim gönder, devam ettir).
- İzinler lead'den miras; teammate izin istekleri lead'e düşer → önceden allowlist.
- Sınırlar: resume ile teammate'ler geri gelmez; oturum başına tek takım; iç içe takım yok; lead sabit.

## Bizim fabrika için
- "Ana beyin" = lead; hatlar (web / app / reklam / sosyal) = `.claude/agents/` tanımlı teammate rolleri; ortak görev listesi = sipariş kalemleri; dosya sahipliği hat bazında ayrılır.
