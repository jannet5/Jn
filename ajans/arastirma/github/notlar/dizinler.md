# Skill dizinleri (katalog siteleri) — nereden ne bulunur

Tarih: 2026-10-05. Bu dosya kategori notlarına girmeyen "katalog/dizin" sitelerini özetler. Repo bazlı notlar `mobil-skiller.md`, `web-skiller.md`, `design-md.md`, `animasyon.md`, `pipeline-fabrika.md`, `sosyal-medya-reklam.md` içinde.

| Dizin | Ne | Nasıl kullanılır | Bizim için değeri |
|---|---|---|---|
| https://skills.sh/ | Vercel'in skill dizini; kurulum sayısına göre sıralı. Kurulum: `npx skills add <owner/repo>` (Claude Code, Cursor, Copilot, Codex… hepsinde çalışır). | `npx skills add anthropics/skills` gibi; arama: `npx skills find <kelime>` veya site araması. | 5 — tek komutla skill kurma standardı; zincirimizin kurulum betiği bu komutları kullanmalı. |
| https://claudskills.com/ | 200k+ SKILL.md kataloğu; kategoriler: Design System UI, UX Research, Content, Growth & Marketing, Advertising, tag: expo. | Kategori/tag ile gez, repo linkine git, SKILL.md'yi `.claude/skills/` altına kopyala. | 3 — keşif için; kalite filtresi yok, yıldız/kurulum sayısı yok. |
| https://claudemarketplaces.com/ | Claude Code plugin/marketplace/MCP dizini. Kategoriler: Frontend Development, Design & Creative, Sales & Marketing. En çok kurulan: f/prompts.chat (157k), affaan-m/everything-claude-code (142k), obra/superpowers (137k), anthropics/skills (111k). | `claude plugin marketplace add <owner/repo>` → `claude plugin install <ad>@<marketplace>`. | 3 — marketplace keşfi. |
| https://docs.expo.dev/agents/claude/ | Expo'nun resmi Claude Code rehberi. Tek komut: `claude plugin install expo@claude-plugins-official` → Expo Skills + Expo MCP birlikte kurulur. AGENTS.md ile SDK sürümüne göre doküman yönlendirmesi. | Komutu çalıştır; `.claude/settings.json` ile projede etkinleştir (Claude Code ≥ 2.1.277). | 5 — mobil hattın temeli. Ayrıntı: `mobil-skiller.md`. |

## skills.sh en çok kurulanlar (2026-10-05)

| Sıra | Skill | Repo | Kurulum | Not |
|---|---|---|---|---|
| 1 | find-skills | vercel-labs/skills | 3.7M | Skill keşif asistanı |
| 2 | grill-me | mattpocock/skills | 1.3M | Tasarım/spec sorgulama (plan öncesi "ızgara") |
| 3 | grill-with-docs | mattpocock/skills | 1.1M | Dokümanla sorgulama |
| 4 | improve-codebase-architecture | mattpocock/skills | 1.1M | Mimari iyileştirme |
| 5 | agent-browser | vercel-labs/agent-browser | 1.0M | Tarayıcı otomasyonu → görsel QA için ekran görüntüsü |
| 6 | tdd | mattpocock/skills | 1.0M | Test odaklı geliştirme |
| 7 | frontend-design | anthropics/skills | 956K | **Jenerik "AI görünümü" karşıtı tasarım skill'i** |

Çıkarım: "güzel görünüm" için dizinlerde tek belirgin lider `anthropics/skills` → `frontend-design`. Geri kalanı (mobil, animasyon, DESIGN.md) daha küçük repolarda; kategori notlarına bak.
