# Build a proactive agent workflow with Claude Code — Claude (Anthropic) — 22 dk — https://www.youtube.com/watch?v=eSP7PLTXNy8
> Kaynak notu: Transkript: var (yt-dlp, 2026-10-10) — `arastirma/youtube/transkriptler/eSP7PLTXNy8.txt`. Konuşmacı: Maya (Anthropic Applied AI), Code w/ Claude atölyesi. Videodaki örnek: **dokümantasyon otomasyonu** (Claude Code docs sorumlusu "Sarah"nın routine'leri). Limitler, API curl'ü, filtre sözdizimi, 9:07 kuralı, secrets uyarısı videoda **geçmez** → resmi Routines dokümanından (`kaynaklar/eSP7PLTXNy8-routines-dokumani.md`) → "(doküman)". Güven: **yüksek**.

## Tek paragraf
Video, Claude Code'u "Enter'a basmanı bekleyen araç"tan "bir şey bozulunca fark edip harekete geçen takım arkadaşı"na çeviren **Routines**'i anlatıyor. Proaktif ajan kurmanın üç zorluğu: nerede çalışacak (laptop kapanırsa oturum biter → hosting/kalıcılık/kimlik altyapısı), ne zaman tetiklenecek (cron/endpoint altyapısı), insan döngüde mi dışında mı (headless oturumu izleyememe/yönlendirememe/sürdürememe). Routine = **prompt + repo(lar) + connector'lar + trigger**; geri kalanı Anthropic'in yönetilen altyapısı; her routine izlenebilir, yönlendirilebilir, sürdürülebilir normal bir Claude Code oturumu. Üç tasarım kararı: **trigger**, **context** ("Claude'un sahip olduğu bağlam başarısının tavanıdır"), **steering** (ajan-ajan review, canlı müdahale, çıktıyı doğrulama). Gerçek örnek: Claude Code'da haftalık PR sayısı yılbaşından beri %200 arttı → tek docs mühendisi routine'lerle dokümanı otomatikleştirdi. Kapanış: "Proactive agents beat reactive agents."

## Önerdiği iş akışı (adım adım)
1. **Tekrarlayan işi seç**: "what are some tasks that you do every day that would help if they could run on a schedule or if Claude code could actually initiate these sessions for you". Videodaki örnekler: haftalık docs senkronu, issue → docs açığı, deploy doğrulayıcı, on-call araştırmacı, PM backlog triyajı.
2. **`/schedule` ile doğal dilde başlat**: Sarah'nın prompt'u (aşağıda) → Claude takip soruları sorar ("what time every week ...? ... do you want me to notify you ... Maybe ping you on Slack?") → routine'i ve ayrıntılı talimatları **Claude kendisi yazar** (web'de görünür).
3. **Trigger kararı**: zamanlı (haftalık docs diff'i) ya da olay (release kesilince release branch'i docs ile diff'le; "need docs" etiketli PR merge olunca; GitHub issue açılınca; CD pipeline'ın webhook'a POST'u). (doküman: API `POST .../routines/<id>/fire`, GitHub olay filtreleri, birden fazla trigger, tam saat yerine 9:07.)
4. **Context kararı**: gereken tüm repo'lar (docs örneğinde kaynak kod + docs repo'su), ek bağlam connector'ları (marka dili için Drive'daki pazarlama brief'leri), bildirim connector'ı (Slack). "whatever context Claude has, that's uh the ceiling of how successful Claude will be." (doküman: `claude/` branch'e push, cloud environment, gereksiz connector'ları çıkar.)
5. **Steering kararı**: (a) **ajan-ajan review** — generator/critiquer: bir routine PR açar, PR oluşunca tetiklenen ikinci routine insan gelmeden yorum bırakır; (b) **canlı müdahale** — web'de oturumu izle, soru sor, yön değiştir, geçmiş run'ı sürdür (demoda: "I've already made these changes" diyerek run'ı durdurdu); (c) **çıktıyı doğrula** — docs örneğinde değişen sayfa render edilip kontrol ediliyor.
6. **Güveni kademeli artır** (deploy doğrulayıcı): önce Claude araştırıp **go/no-go** kararı yazar, insan okur ve rollback'i birlikte yapar; "as I watch Claude work more and more and trust its decisions, I can let Claude roll back the change itself".
7. **İzle ve yinele** (doküman): `Run now`, run transkriptleri, `/schedule list|update|run`; yeşil ≠ görev başarısı; prompt'u transkripte göre düzelt, connector kapsamını daralt.

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- Sarah'nın `/schedule` prompt'u: "once a week, Please review all the new changes merged to main against our documentation repo and create a PR to update docs if you see any changes." → oluşan routine: 2 repo (kaynak + docs), "every Monday at 10:00 a.m.", GitHub + Slack.
- Olay routine'i (özet aktarım): issue açılınca "investigate the issue ... Figure out if it's to a documentation gap and then if it is ... open a PR and actually ping me in this channel."
- Deploy doğrulayıcı tasarımı: trigger = CD pipeline webhook POST; context = servis kaynak kodu + Datadog/Grafana + Slack/e-posta/Twilio; steering = "an eventual go or no-go decision to actually roll back this change".
- "A teammate notices when something breaks and does something about it." / "Proactive agents beat reactive agents."
- "You're a single /schedule command inside of Claude code away from creating your very first routine."
- (doküman) "The prompt is the most important part: the routine runs autonomously, so the prompt must be self-contained and explicit about what to do and what success looks like."
- (doküman) `/schedule daily PR review at 9am` · `/schedule tomorrow at 9am, summarize yesterday's merged PRs` · `/schedule in 2 weeks, open a cleanup PR that removes the feature flag` · `/schedule add a GitHub trigger to my nightly review for pull requests opened in acme/webapp`
- (doküman) API: `curl -X POST https://api.anthropic.com/v1/claude_code/routines/trig_.../fire -H "Authorization: Bearer sk-ant-oat01-..." -H "anthropic-beta: experimental-cc-routine-2026-04-01" -H "anthropic-version: 2023-06-01" -d '{"text": "Sentry alert SEN-4521 fired in prod."}'`
- (doküman) Filtre örnekleri: base `main` + head contains `auth-provider`; is draft = false; labels include `needs-backport`; regex tüm alanı eşler: `.*hotfix.*`.

## Araçlar ve linkler
- Videoda: `/schedule`, claude.ai → Code → Routines, GitHub olay trigger'ı, webhook/POST trigger, Slack connector, Google Drive connector, GitHub MCP, Datadog/Grafana, Twilio
- (doküman) https://code.claude.com/docs/en/routines ; GitHub App: https://github.com/apps/claude ; /docs/en/scheduled-tasks (`/loop`), /docs/en/cloud-environments, /docs/en/github-actions
- (doküman) Limitler: 100 zamanlanmış run/saat (hesap); 30 Run now+API/saat (routine); min aralık 1 saat; GitHub bağlantısı kopuksa 72 saat bekler sonra kapanır.

## Hatalar / "bunu yapma" uyarıları
- Cron + kendi altyapını kurup bakımını yapmak ("keep your hands up if you've enjoyed building all of that infra" → tek el kalktı).
- Proaktif ajanı laptop'ta çalıştırmak → kapanınca biter.
- Eksik bağlam vermek → başarının tavanı bağlamdır (docs routine'ine hem kaynak hem docs repo'su gerekir).
- Çıktıyı doğrulamadan PR'ı insana atmak → önce ajan-ajan review + render/test.
- Otomatik aksiyona (rollback) baştan tam yetki vermek → önce go/no-go raporu, güven oluştukça yetki.
- (doküman) Belirsiz prompt; tüm connector'ları açık bırakma; tam saate kurma (→ 9:07); yeşil durumu başarı sayma; gizli anahtarı env'e koyma; `/schedule` cloud oturumunda ve Console API key ile çalışmaz.

## Bizim fabrikaya alınacaklar (somut)
1. **Routine kataloğu** `ajans/sistem/routines/`: (a) `gece-saglik`: her müşteri sitesinde build+Playwright smoke+Lighthouse, eşik altıysa fix PR; (b) `haftalik-icerik`: brief'ten 7 günlük sosyal takvim + görsel prompt'ları PR; (c) `aylik-reklam`: Meta CSV → düşük performanslıları bul → varyasyon PR; (d) `pr-review`: `pull_request.opened` → ajans checklist.
2. **Generator → critiquer çifti**: her üretim routine'inin yanına PR açılınca tetiklenen bir review routine'i (tasarım/marka/erişilebilirlik); insan PR'a geldiğinde yorumlar hazır.
3. **"Değişiklik → doküman/içerik senkronu" kalıbı** (Sarah örneğinin ajans karşılığı): müşteri sitesinde ürün/menü/fiyat değişikliği merge olunca sosyal medya ve Google Business içerik taslağı PR'ı; "need-content" etiketiyle tetikleme.
4. **Deploy doğrulayıcı**: Vercel/EAS deploy webhook'u → routine smoke + metrik kontrol → go/no-go raporu Slack'e; rollback ilk aşamada insanda.
5. Her routine prompt'u şablona uyar: **Görev / Girdi (repo, dosya, payload) / Başarı koşulu / Çıktı yeri (branch, PR başlığı) / Bildirim / Yapma listesi**; ilk sürümü `/schedule` ile doğal dilde kurdur, Claude'un yazdığı talimatları dosyaya kaydet.
6. Connector hijyeni: routine başına yalnızca ilgili connector; context listesi (repo'lar + Drive brief'leri + Slack) routine dosyasında yazılı.
7. Local `/loop` (dakikalar) vs bulut routine (saat/gün) sınırı CLAUDE.md'de yazılı.
