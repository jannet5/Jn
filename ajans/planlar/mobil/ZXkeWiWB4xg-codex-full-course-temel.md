# Codex - Full Course for Beginners — Tech With Tim — 34 dk — https://www.youtube.com/watch?v=ZXkeWiWB4xg

## Tek paragraf: ne yapıyor, sonuç ne
OpenAI Codex masaüstü uygulamasının özellik turu; mobil uygulama yapmıyor. Model/reasoning seçimi, plugin'ler (Computer Use, Chrome eklentisi, GitHub, Drive...), bilgisayar kontrolü demosu (Notepad, Spotify→Chrome), proje/thread düzeni, yerleşik tarayıcı ile web uygulaması (multiplayer chess: WebSocket backend + DB + frontend) testi, tarayıcıda **annotate** ile eleman seçip hedefli UI değişikliği, slash komutları (`/status`, `/compact`, `/fork`, `/plan`, `/goal`, `/review`), worktree, `AGENTS.md` ile kalıcı hafıza ve zamanlanmış görevler. Bizim için değeri: Claude Code'daki karşılıklarıyla eşlenebilecek iş akışı fikirleri (goal döngüsü, review, AGENTS.md/CLAUDE.md, annotate ile görsel geri bildirim).

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Codex desktop (Windows/Mac; ChatGPT Plus $20'dan itibaren; API key önerilmiyor), IDE extension (Cursor/VS Code/Windsurf), CLI (Linux/sunucu), Cloud (tarayıcı)
- Codex Docs: https://learn.chatgpt.com/docs
- Modeller: GPT 5.5 (varsayılan, medium reasoning, fast mode açık), GPT 5.4, GPT 5.4 mini (küçük UI değişikliği), 5.3 Codex Spark (çok hızlı, limit bitince)
- Plugin'ler: Computer Use, Chrome (Codex Chrome extension), GitHub, Google Drive, Presentations, Spreadsheets, Slack, Gmail
- Wispr Flow (dikte): https://ref.wisprflow.ai/TechWithTim-jul26
- Diğer açıklama linkleri: boot.dev sponsor https://boot.dev/?promo=TECHWITHTIM ; Hostinger VPS https://www.hostinger.com/techwithtim10 ; Openclaw kurulum https://www.hostinger.com/techwithtim ; newsletter https://techwithtim.net/newsletter

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
**Kurulum ve arayüz**
1. [00:01:39] Codex indir, ChatGPT hesabıyla giriş.
2. [00:03:20] Arayüz: model seçici, dikte, onay modları (Ask for approval / Approve for me / Full access), skill/context ekleme, `/` komut listesi, projeler; sağda Browser + Terminal paneli; Settings'te "everyday work" modu; Usage & billing.
3. [00:06:33] Model: GPT 5.5 + medium; zor iş high/extra high (4-10x daha yavaş, 30 dk sürebilir); fast mode açık.

**Plugin ve bilgisayar kontrolü**
4. [00:08:23] Plugins → Computer Use kur (izinler), Chrome plugin + Codex Chrome extension (connected), GitHub bağla.
5. [00:10:40] `@computer` referansıyla: Notepad aç, not yaz; ajan kendi imleciyle çalışır, kullanıcı aynı anda bilgisayarı kullanabilir (mavi çerçeve).
6. [00:12:14] "computer, open up Spotify, find Drake's most popular song..." → Spotify yok → Chrome'dan yaptı.

**Proje ve kodlama**
7. [00:13:10] New project → "Start from scratch" → `chess game`; seçenekler: Codex web / Work locally / New worktree; otomatik git `main` branch; Full access modu.
8. [00:14:24] Chess MVP prompt'u → dosyalar oluşuyor; sağ panelde diff, branch, commit/push, terminal, "Open in Cursor".
9. [00:16:01] Linkler Codex içi tarayıcıda açılıyor; `localhost:3000` ikinci sekmede ikinci oyuncu → multiplayer + ELO/leaderboard çalışıyor.
10. [00:17:13] Tarayıcı araçları: screenshot (panoya → chat'e yapıştır), **Annotate**: HTML elemanı seç + not yaz ("başlık daha iyi olsun, chess logosu", "tahta rengi / dark mode") → "go" → hedefli değişiklik.

**Komutlar**
11. [00:19:08] `/status` (5 saatlik ve 7 günlük limit, context), context dairesi; `/compact` (%70-90 dolunca; özetleyip yeni bağlam), otomatik compact da var; `/fork` (local veya yeni worktree'ye kopya); chat pin.
12. [00:22:05] `/plan` → chess.com klonu genişletme prompt'u → Codex soruları (core three modes, username profiles, small bundled puzzle library) → plan dosyası repo'ya yazılıyor.
13. [00:24:45] `/goal` → hedef + bitiş kriteri ver → plan-uygula-kontrol et döngüsü hedef sağlanana kadar (çok kullanım harcar).
14. [00:26:40] Worktree: paralel ajanlar için repo kopyası; ortam yapılandırması gerekir.
15. [00:27:59] `/review` → base branch'e karşı veya uncommitted changes → hata yorumları → düzelt; push öncesi her zaman.
16. [00:29:03] Hafıza: yeni chat önceki oturumu hatırlamaz (thread'leri yavaşça arar) → `AGENTS.md` oluşturt (her yeni konuşmada otomatik yüklenir) → "what are the project conventions?" anında cevap.
17. [00:33:08] Scheduled tasks: şablonlar (daily brief, weekly review, project monitor), chat veya worktree'de, proje/saat/model seçimi; bilgisayar açık olmalı. MCP, hooks, git environments part 2'ye bırakıldı.

## Kullandığı prompt'lar (varsa aynen)
Computer use [00:10:40]:
```
Open up Notepad. Close any of the existing windows that are there. And then write a simple note and describe your ideal day. I know you're a computer, but just come up with something.
```
```
computer, open up Spotify, find Drake's most popular song, and then tell me what it is and play it.
```
Chess MVP [00:14:24]:
```
I want to create a simple chess game. I want to have a backend which is using websockets and kind of keeping track of scores and ratings and stuff in a database and people's usernames. And then I want to have a front end where we can actually play this. I want it to be multiplayer. So I want two people to be able to play against each other. We can keep it simple, have an MVP to start and then build everything out.
```
Annotate notları [00:17:48]:
```
I don't really like this title. I think we can make it look a lot better and put a chess logo.
```
```
can we change the color of the board or have like a white or dark mode or maybe change the square colors?
```
Plan [00:22:42]:
```
I want to expand our chess game. I want to have a better landing page so that a user can see their stats and they have the ability to either play a game or play a puzzle or go to like a practice board. I want to generate a few different game modes and I want to really polish this up and make it kind of like a chess.com clone. Let's start with just the MVP features. Can you generate a plan for this? Ask me any questions you need to.
```
Goal [00:25:57]:
```
keep running and implement the entire plan until the plan is fully finished everything is tested everything is working and we are confident to deploy this.
```
AGENTS.md [00:31:40]:
```
I would like you to create an agents.md file with all of the conventions for for this project, specifically the text stack that we're using, how to run it, and what we've set up so far.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Tasarım odaklı değil. Tek ilgili şey: yerleşik tarayıcıda **Annotate** ile ekrandaki elemanı seçip ona bağlı not yazmak → genel "daha güzel yap" yerine eleman-hedefli UI değişikliği. Screenshot'ı doğrudan chat'e verme.

## Hatalar ve çözümleri
- Computer use ikinci komutta notu uzatmadı ("thinking"de kaldı) → çözülmedi, başka örneğe geçti [00:12:14].
- Spotify kurulu değil → ajan kendisi Chrome'a geçti (Chrome izni istendi).
- Yeni chat projeyi hatırlamıyor → `AGENTS.md` [00:29:03].
- Context dolunca kalite düşüyor → `/compact` %70-90'da elle.
- Paralel ajan çakışması → worktree.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- "Goal döngüsü" aşaması: plan dosyası onaylandıktan sonra ajan "plan tamamen uygulanana, testler geçene kadar devam" bitiş kriteriyle koşsun (Claude Code'da plan checklist'i + hook/loop ile).
- Push/teslim öncesi zorunlu `/review` (Claude Code: `/code-review` veya `/review`) adımı.
- Her projede `CLAUDE.md`/`AGENTS.md` ajana ürettirilsin ve güncel tutulsun: stack, çalıştırma komutu, kurulanlar, tasarım kuralları.
- Context yönetimi kuralı: %70+'da compact veya yeni oturum; özellik başına yeni chat.
- Görsel geri bildirim: Expo web/simülatör ekran görüntüsü + eleman işaretleme ile hedefli UI düzeltme turu (Claude Code'da browser/preview ile).
- Paralel iş için worktree (tasarım varyantları ayrı worktree'lerde denenebilir).
- Fast/küçük modelleri yalnızca küçük UI rötuşu için kullan; mimari ve plan yüksek reasoning'le.
