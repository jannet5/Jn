# How I use Claude Code (Senior Software Engineer Tips) — Maddy Zhang — 9 dk — https://www.youtube.com/watch?v=MzhIr7BfpI0
> Kaynak notu: Transkript var (161 satır, yt-dlp, `arastirma/youtube/transkriptler/MzhIr7BfpI0.txt`); baştan sona okundu, zaman damgaları transkriptten. Güven: **yüksek**. Videoda geçmeyen mekanik ayrıntılar (kısayollar, dosya biçimleri) Anthropic dokümanından referans olarak bırakıldı ve "(doküman)" ile işaretli: `kaynaklar/gv0WHhKelSE-anthropic-best-practices-dokumani.md`; açıklama metni: `transkriptler/MzhIr7BfpI0.aciklama.txt`.

## Tek paragraf
Eski Google/Amazon/IBM/Microsoft mühendisi Maddy Zhang, Claude Code'u "biraz daha iyi Google araması" ya da "fancy chatbot" gibi değil **force multiplier** gibi kullanmayı anlatıyor [00:00:00–00:00:33]. Sıra: IDE eklentisi (açık dosya otomatik bağlama girer) → ilk iş `/init` ile CLAUDE.md ("yeni mühendis için onboarding dokümanı", 100-200 satır üstü sinyali seyreltir) [00:01:31] → model seçimi (Opus varsayılan, limitte Sonnet, hafif iş Haiku) [00:02:41] → her koddan önce Plan Mode [00:02:59] → görevler arası `/clear` [00:03:30] → haftada birden fazla yapılan her iş için slash komutu [00:04:07] → hooks ile build + test + typecheck **self-validation loop** [00:04:35] → paralel oturumlar (IDE panelleri veya git worktree) [00:05:39] → `.claude/agents` sub-agent'ları [00:06:28] → skill'ler (tam playbook) [00:07:12] → MCP [00:07:46] → üç ipucu: spesifik prompt, küçük adımlar, **plan → execute → review** [00:08:19]. Ana tezi: videodaki her özellik aslında bu üç fazdan birini zorlayan bir araç [00:09:04].

## Önerdiği iş akışı (adım adım)
1. **Kurulum** [00:01:11]: VS Code/Cursor eklentisi (açık dosya "manually tag" etmeden bağlama girer) ya da terminal. "When most people install Claude Code, they immediately start throwing prompts at it. Don't do this." [00:01:31]
2. **CLAUDE.md** [00:01:38]: `/init` projeyi tarar ve dosyayı üretir. Üç şey: "what the project does, where things live, and how work gets done" [00:01:49] → tech stack, kurallar/konvansiyonlar, kilit dosya yolları, gerçek dev komutları (test/build/lint). İstersen "who you are" açıklaması da ekle ki "code like you" [00:02:22]. CLAUDE.md yoksa her oturum kod tabanını sıfırdan keşfeder: "That burns tokens and leads to mistakes." [00:02:09] Yalın tut: 100-200 satır üst sınır [00:02:33].
3. **Model** [00:02:41]: varsayılan Opus ("for complex engineering tasks, that matters"); limite gelince otomatik Sonnet; "super lightweight tasks" için Haiku.
4. **Plan (Plan Mode)** [00:02:59]: dosyaya dokunmadan önce kod tabanını okur, tam uygulama planı çıkarır: hangi dosyalar değişecek, mantık akışı, riskler. Planı gözden geçir, itiraz et, doğrudan düzenle; sonra uygulat [00:03:11]. (Plan Mode'a geçiş `Shift+Tab` — doküman.)
5. **`/clear` alışkanlığı** [00:03:30]: her görev bitip yenisine geçerken. Bağlam dolunca performans "degrades immensely": tuhaf kararlar, önceki kısıtları unutma, eski geçmişi özetlemeye harcanan hesap [00:03:41]. Eski oturuma yukarı ok ile dönülür [00:03:59].
6. **Özel slash komutları** [00:04:07]: `.claude/commands/` altında düz markdown, tek kelimeyle tetiklenen kayıtlı prompt'lar. Örnek: PR özeti komutu [00:04:17].
7. **Self-validation loop (kod + test)** [00:04:35]: hooks = iş akışının belirli noktalarında otomatik çalışan script'ler [00:04:46]. Üç şeyi bağla: build, testler, type checker — Claude her değişikliği kaydettiğinde çalışsın [00:04:54]. Gerekçe: Claude bir fonksiyonu taşırken testlerde edge case kaçırır; döngü varsa hata çıktısını hemen görür ve genelde müdahalesiz düzeltir [00:05:02]. Kurulum "maybe takes 20 minutes" [00:05:17]. Sonuç: oturumu bırakıp dönünce "green CI instead of a pile of errors" [00:05:32].
8. **Paralel çalışma** [00:05:39] ("what I wish I'd set up sooner"):
   - Basit yol: IDE'de birden çok panel, her biri ayrı problem (biri bug fix, biri yeni bileşen); ayrı dosyalarda kaldıkça çakışmaz [00:05:45].
   - Ciddi yol: `git worktree` — her feature/fix branch'i için ayrı dizin + ayrı Claude oturumu; "they can never step over each other" [00:05:59]. Akış: worktree aç → oturumu başlat → Slack/review/öğle yemeğiyle uğraş → dön, her oturumun çıktısını review et, merge et [00:06:14].
9. **Sub-agent'lar** [00:06:28]: ana oturumun tek iş akışı içinde açıp iş devrettiği ajanlar (worktree'lerdeki bağımsız oturumlardan farklı). `.claude/agents/` klasöründe; her birinin adı, izinli araçları ve odak talimatı var [00:06:39]. Örnek: güvenlik review'u → "security reviewer", DB sorgusu → "DB specialist" [00:06:47]. İzole bağlamda çalışır, sonucu döndürür, ana oturum temiz kalır. Kullanım yeri: kendi içinde bütün paralel işler ve "you don't want one agent's reasoning bleeding into another's" [00:07:02].
10. **Skill'ler** [00:07:12]: belirli bir iş akışını kodlayan markdown (kod review, refactor, release); oturumlar, projeler ve takım arkadaşları arasında tekrar kullanılır [00:07:15]. Çok adımlı mantık tanımlar, başka dosyalara referans verir, sub-agent'larca daha büyük akışın parçası olarak çağrılır [00:07:34].
11. **MCP** [00:07:46]: Claude'u yerel terminalin dışına çıkarır: GitHub, Slack, veritabanları, Perplexity; "configure them once" [00:07:58]. Kullanım: PR yorumlarını çekip ele almak; koddan önce Slack'teki son tasarım geri bildirimini okumak [00:08:02].
12. **Review** [00:08:49]: plan → execute (küçük adımlar) → review before merge. Kapanış özeti [00:09:16]: CLAUDE.md, koddan önce plan mode, görevler arası /clear, tekrar eden işler için komut, validation loop, izole iş için paralel ajan + sub-agent, tekrar eden akışlar skill, her zaman plan-execute-review, ileri seviye MCP.

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- [00:01:41] CLAUDE.md tanımı: "Think of it as the onboarding document you'd write for a new engineer on your team, except this one's for your AI."
- [00:02:13] CLAUDE.md içerik örnekleri: build komutu "npm run dev and not yarn", API route'larını tutan klasörler, "you use functional components with hooks".
- [00:02:33] "Stick to the essentials, maybe 100 to 200 lines max. Anything more and you're actually diluting the signal."
- [00:03:15] Planı atlamanın bedeli: "code that technically works, but solves the wrong problem or makes the right change in the wrong place." → ardından "the next hour or more debugging".
- [00:03:26] "I recommend you always go use plan mode before writing a single line of code."
- [00:03:55] "Each task should be a clean session with a fresh context."
- [00:04:17] Slash komut örneği: PR summary — "what changed, why, any risks, and how to test it".
- [00:04:24] Kural: "anything you do more than once a week in your engineering workflow, there should be a slash command for."
- [00:05:20] Doğrulama talimatı (CLAUDE.md veya hooks config'e): "After every edit, run the build, run the test, run the type check. If anything fails, treat it as a blocker and [fix] it before moving on." (transkriptte "save it" geçiyor, büyük olasılıkla "solve it".)
- [00:07:30] "A slash command is a quick trigger, one prompt, one action. A skill is a full playbook."
- [00:07:43] Skill = "institutional knowledge that travels with your codebase."
- [00:08:27] Spesifik prompt: "Vague prompts produce vague code." Belirt: hangi dosyalara dokunulacak, kısıtlar ne, beklenen davranış ne.
- [00:08:45] Küçük adım gerekçesi: 15 dosyaya yayılıp 3 şeyi bozan değişiklikte nereden başlanacağı belli olmaz; 2 dosya / 1 hata ise "back on track in 5 minutes".
- [00:09:04] "Every feature in this video ... is really just a tool that enforces one of those three phases."

## Araçlar ve linkler
- Claude Code: VS Code/Cursor eklentisi, terminal, `/init`, CLAUDE.md, Opus/Sonnet/Haiku, Plan Mode (`Shift+Tab` — doküman), `/clear`, yukarı ok ile geçmiş oturumlar, `.claude/commands/`, hooks, `.claude/agents/`, skills.
- `git worktree` (her branch için ayrı çalışma dizini).
- MCP sunucuları: GitHub, Slack, veritabanları, Perplexity.
- Video: https://www.youtube.com/watch?v=MzhIr7BfpI0 · Referans doküman: `kaynaklar/gv0WHhKelSE-anthropic-best-practices-dokumani.md`.

## Hatalar / "bunu yapma" uyarıları
- [00:00:00] AI'yı "slightly better Google search" / [00:00:30] "fancy chatbot that writes code for you" gibi kullanmak.
- [00:01:31] Kurar kurmaz prompt atmak → önce `/init`.
- [00:02:05] CLAUDE.md'siz çalışmak → her oturumda yeniden keşif, token israfı, hata.
- [00:02:29] Devasa CLAUDE.md ("more context is better, but it's not").
- [00:03:15] Plan olmadan kod → yanlış problemi / yanlış yerde çözen kod, saatlerce debug.
- [00:03:48] Oturumu "a running conversation they never want to interrupt" gibi sürdürmek → bağlam dolar, kısıtlar unutulur.
- [00:04:28] Aynı prompt'u her seferinde yeniden yazmak → slash komut.
- [00:05:05] Claude'un hatalarını yalnızca kendi review'unla yakalamaya çalışmak ("baby-sitting") → validation loop.
- [00:05:53] Paralel panelleri aynı dosyalara yönlendirmek → çakışma; ciddi paralel işte worktree.
- [00:08:38] Dev görevi tek seferde vermek: "This will go poorly."

## Bizim fabrikaya alınacaklar (somut)
1. **Doğrulama döngüsü şablonu** her müşteri repo'suna: `npm run check` = lint + typecheck + test + build tek komut; hook ile her düzenlemeden sonra (Maddy: kurulum ~20 dk [00:05:17]); CLAUDE.md'de Maddy'nin cümlesi: "Hata = blocker, geçmeden ilerleme / bitti deme". Hedef ölçüt: oturumdan dönüldüğünde "yeşil CI" [00:05:32]; hook çıktısı Claude'a geri beslenmeli ki kendisi düzeltsin.
2. **"Haftada >1 = komut" kuralı** [00:04:24]: fabrikada tekrar eden her adım komut/skill olur — `/teslim-hazirla` (check + screenshot + PR), `/pr-ozet` (ne değişti, neden, riskler, nasıl test edilir [00:04:17]), `/musteri-raporu`; tek prompt/tek eylem = komut, çok adımlı + dosya referanslı + sub-agent'ın çağırdığı = skill [00:07:30].
3. **Müşteri repo CLAUDE.md'si üç başlık** [00:01:49]: proje ne yapar / neler nerede / iş nasıl yapılır (komutlar, konvansiyonlar); "npm değil yarn" türü yanlış-komut tuzaklarını açıkça yaz [00:02:13]; üst sınır bizde 60 satır (Maddy 100-200 diyor; Anthropic dokümanı daha sıkı). Ek: kısa "kim için/kimin üslubuyla" satırı (müşteri ekibinin kod tarzı) [00:02:22].
4. **Worktree disiplini** [00:05:59]: `ajans/sistem/PARALEL.md`: görev başına worktree, isimlendirme `wt/<musteri>-<is>`, ana beyin dağıtır/toplar ve merge öncesi her oturumun çıktısını review eder [00:06:21]. Hafif işlerde aynı repo'da paralel oturum yalnızca dosya kümeleri ayrıksa [00:05:53].
5. **`/clear` kuralı** CLAUDE.md'ye [00:03:30]: "Yeni sipariş kalemi = /clear; aynı konuda 2 düzeltmeden sonra /clear + yeni prompt". Gerekçe metni olarak Maddy'nin "kısıtları unutur" uyarısı.
6. **Sub-agent seti** [00:06:39]: `security-reviewer`, `design-reviewer`, `db-specialist` — her birinin adı, izinli araçları kısıtlı, tek odak talimatı; kural: bir ajanın akıl yürütmesinin diğerine sızmaması gereken işler sub-agent'a gider [00:07:06].
7. **Model politikası** [00:02:41]: üretim/mimari Opus, rutin içerik/format işleri Sonnet/Haiku (maliyet).
8. **MCP** [00:07:58]: GitHub (PR yorumlarını çek ve uygula), Slack/Drive (müşteri tasarım geri bildirimi kod öncesi okunur — "before writing code" [00:08:05]) — her hat için standart `.mcp.json`.
9. **Görev şablonu = Maddy'nin üç alanı** [00:08:30]: her iş emri/prompt'ta zorunlu "dokunulacak dosyalar / kısıtlar / beklenen davranış" alanları; doldurulmamış iş emri hatta girmez.
10. **Diff boyutu sınırı** [00:08:45]: tek adımda hedef ≤ birkaç dosya; 10+ dosyaya yayılan iş emri plan aşamasında küçük adımlara bölünür (Maddy'nin 15 dosya / 2 dosya örneği).
11. **Fabrika aşamalarını plan → execute → review'a eşle** [00:09:04]: her araç (plan mode, hooks, sub-agent, skill) hangi fazı zorladığıyla etiketlensin; review fazı merge öncesi zorunlu kapı.
