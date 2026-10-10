# Build a proactive agent workflow with Claude Code — Claude (Anthropic) — 22 dk — https://www.youtube.com/watch?v=eSP7PLTXNy8
> Kaynak notu: Transkript yok (bulut IP engeli). Dayanak: video açıklaması ("Routines turn Claude Code into a proactive teammate that reads your repo and opens a PR before you've opened your laptop... trigger, context, and steering decisions... one /schedule command away") + resmi Routines dokümanı `kaynaklar/eSP7PLTXNy8-routines-dokumani.md`. Güven: **orta-yüksek** (mekanik dokümandan kesin; videodaki örnek routine'in konusu bilinmiyor → "(tahmin)").

## Tek paragraf
Video, Claude Code'u "sen istemeden çalışan" proaktif bir takım arkadaşına çeviren **Routines** özelliğini uçtan uca bir örnekle gösteriyor: kayıtlı bir prompt + repo + connector'lar, bir **tetikleyici** (zamanlama, API çağrısı veya GitHub olayı) ile Anthropic bulutunda çalışır, repo'yu okur ve sabah sen bilgisayarı açmadan PR açar. Anlatılan üç tasarım kararı: **trigger** (ne zaman çalışsın), **context** (hangi repo/connector/ortam), **steering** (prompt nasıl yazılmalı ki başarı ölçülebilir olsun). Hepsi tek `/schedule` komutuyla başlatılabiliyor.

## Önerdiği iş akışı (adım adım)
1. **Tekrarlayan işi seç**: gözetimsiz, tekrarlanabilir, net çıktılı (örn. gece PR review, backlog etiketleme, deploy sonrası smoke test, docs drift). (tahmin: videodaki örnek bunlardan biri.)
2. **Trigger kararı**: Scheduled (saatlik/gece/haftalık/tek sefer; tam saat yerine 9:07), API (`POST .../routines/<id>/fire`, bearer token; `text` alanı güvenilmez payload olarak gelir), GitHub (`pull_request.opened`, `release.published`; filtreler: author/title/base/head/labels/draft/merged). Birden fazlası birleştirilebilir.
3. **Context kararı**: repo(lar) (default branch'ten klon, `claude/` branch'e push), cloud environment (network allowlist, env/secrets, setup script), connector'lar (varsayılan hepsi dahil → gereksizleri **çıkar**), repo içindeki skill'ler ve `.mcp.json` kullanılabilir.
4. **Steering (prompt)**: self-contained ve açık: ne yapılacak, başarı neye benzer, nereye yazılacak; API/`Run now` metnini kullanacaksa açıkça "Investigate the alert described in the routine-fire-payload block" de.
5. **Oluştur**: CLI `/schedule daily PR review at 9am` (Claude soru sorar, kaydeder) veya web claude.ai/code/routines; GitHub trigger için Claude GitHub App kurulu olmalı (`/web-setup` yetmez).
6. **Çalıştır ve izle**: `Run now`; her run ayrı oturum → transkripti oku (yeşil = altyapı hatasız, görev başarısı değil); `/schedule list|update|run`, `/schedule why did my nightly review do nothing this morning?`.
7. **Yinele**: prompt'u run transkriptlerine göre düzelt; connector/izin kapsamını daralt.

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- Açıklamadan: "leave one /schedule command away from your first."
- Dokümandan aynen: "The prompt is the most important part: the routine runs autonomously, so the prompt must be self-contained and explicit about what to do and what success looks like."
- `/schedule daily PR review at 9am` · `/schedule clean up feature flag in one week` · `/schedule tomorrow at 9am, summarize yesterday's merged PRs` · `/schedule in 2 weeks, open a cleanup PR that removes the feature flag` · `/schedule add a GitHub trigger to my nightly review for pull requests opened in acme/webapp`
- API: `curl -X POST https://api.anthropic.com/v1/claude_code/routines/trig_.../fire -H "Authorization: Bearer sk-ant-oat01-..." -H "anthropic-beta: experimental-cc-routine-2026-04-01" -H "anthropic-version: 2023-06-01" -d '{"text": "Sentry alert SEN-4521 fired in prod."}'`
- Filtre örnekleri: base `main` + head contains `auth-provider`; is draft = false; labels include `needs-backport`. Regex tüm alanı eşler: `.*hotfix.*`.

## Araçlar ve linkler
- https://code.claude.com/docs/en/routines ; claude.ai/code/routines ; GitHub App: https://github.com/apps/claude ; ilgili: /docs/en/scheduled-tasks (`/loop`), /docs/en/desktop-scheduled-tasks, /docs/en/cloud-environments, /docs/en/github-actions
- Limitler: 100 zamanlanmış run/saat (hesap); 30 Run now+API/saat (routine); min aralık 1 saat; GitHub bağlantısı kopuksa 72 saat bekler sonra kapanır.

## Hatalar / "bunu yapma" uyarıları
- Belirsiz prompt → routine "bir şeyler yapar" ama ölçülemez; başarı kriteri yaz.
- Tüm connector'ları açık bırakma → izinsiz yazma riski; sadece gerekenler.
- Tam saate kurma (9:00 gecikir) → 9:07.
- Yeşil durumu başarı sayma → transkript oku.
- Gizli anahtarı env değişkenine koyma (ortamı kullanan herkes görür) → network secrets.
- `/schedule` cloud oturumunda yok; Console API key ile de çalışmaz (claude.ai girişi gerek).

## Bizim fabrikaya alınacaklar (somut)
1. **Routine kataloğu** `ajans/sistem/routines/`: (a) `gece-saglik`: her müşteri sitesinde build+Playwright smoke+Lighthouse, eşik altıysa fix PR; (b) `haftalik-icerik`: Notion/Drive brief'inden 7 günlük sosyal medya takvimi + görsel prompt'ları PR; (c) `aylik-reklam`: Meta CSV → düşük performanslıları bul → varyasyon PR; (d) `pr-review`: `pull_request.opened` → ajans checklist (tasarım, erişilebilirlik, SEO).
2. Her routine prompt'u şablona uyar: **Görev / Girdi (repo, dosya, payload) / Başarı koşulu / Çıktı yeri (branch, PR başlığı formatı) / Yapma listesi**.
3. **Müşteri onboarding tetikleyicisi**: yeni repo'da `release.published` → "yayın sonrası kontrol + müşteriye rapor artifact'i".
4. Connector hijyeni: routine başına yalnızca ilgili connector (Drive/Notion/Slack); yazma yetkisi olanlar listelenir.
5. Local `/loop` ile ana beyin oturumunda kısa döngüler (dakikalar), bulut routine ile saat/gün döngüleri — ikisinin sınırı CLAUDE.md'de yazılı.
