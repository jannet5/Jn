# Routines (resmi doküman) — eSP7PLTXNy8 "Build a proactive agent workflow with Claude Code" için

- URL: https://code.claude.com/docs/en/routines — Çekildi 2026-10-09 (WebFetch). Video: Routines ile repo'yu okuyup sen laptopu açmadan PR açan "proaktif takım arkadaşı"; trigger + context + steering kararları; `/schedule`.

## Ne
Routine = kayıtlı Claude Code konfigürasyonu: **prompt + repo(lar) + connector'lar**; Anthropic bulutunda otomatik çalışır (laptop kapalıyken de). Tetikleyiciler: **Scheduled** (saatlik/gece/haftalık/tek sefer), **API** (POST + bearer token), **GitHub** (PR/release olayları). Bir routine birden fazla tetikleyici alabilir. Pro/Max/Team/Enterprise. Yönetim: claude.ai/code/routines veya CLI `/schedule` (`/routines` takma adı).

## Örnek kullanım kalıpları (dokümandan)
- Backlog bakımı (her gece issue'ları etiketle/ata, Slack özeti)
- Alert triage (monitoring → API fire → stack trace + son commit'ler → taslak PR)
- Özel code review (`pull_request.opened` → kendi checklist'in → inline yorumlar)
- Deploy doğrulama (CD → API fire → smoke test → go/no-go)
- Docs drift (haftalık: merge'lenen PR'lara bakıp doküman PR'ı aç)
- Library port (bir SDK'da merge → diğer dile port PR'ı)

## Kurallar / notlar
- "The prompt is the most important part: the routine runs autonomously, so the prompt must be self-contained and explicit about what to do and what success looks like."
- Her çalıştırma repo'yu default branch'ten klonlar; Claude `claude/` önekli branch açar.
- Ortam: network erişimi (Default = Trusted allowlist), env değişkenleri (gizli anahtarları **network secrets** olarak sakla), setup script (önbelleklenir).
- Connector'lar varsayılan olarak hepsi dahil → gereksizleri çıkar (yazma dahil izinsiz çalışır).
- CLI: `/schedule daily PR review at 9am`, `/schedule clean up feature flag in one week`, `/schedule tomorrow at 9am, summarize yesterday's merged PRs`, `/schedule list|update|run`, `/schedule why did my nightly review do nothing this morning?`
- Tam saat yerine 9:07 gibi seç (gecikme). Minimum aralık 1 saat.
- API fire: `curl -X POST https://api.anthropic.com/v1/claude_code/routines/<trig_id>/fire -H "Authorization: Bearer ..." -H "anthropic-beta: experimental-cc-routine-2026-04-01" -d '{"text": "..."}'` → `text` `<routine-fire-payload>` olarak **güvenilmez veri** gelir; prompt açıkça "Investigate the alert described in the routine-fire-payload block" demeli.
- GitHub trigger: Claude GitHub App kurulu olmalı; filtreler (author, title, base/head branch, labels, is draft, is merged; equals/contains/regex).
- Yeşil durum = altyapı hatasız bitti, görev başarılı demek değil; transkripti oku.
- Limitler: 100 zamanlanmış çalıştırma/saat (hesap), 30 Run now+API/saat (routine başına).

## Bizim fabrika için
- Gece routine'i: müşteri repo'larında "lint+test+screenshot" sağlık kontrolü, bozuksa PR.
- Haftalık: sosyal medya içerik takvimi üret (connector: Google Drive/Notion), reklam performansı CSV'sini analiz et.
- GitHub: her `pull_request.opened` → ajans review checklist'i (tasarım kalitesi, erişilebilirlik, performans).
