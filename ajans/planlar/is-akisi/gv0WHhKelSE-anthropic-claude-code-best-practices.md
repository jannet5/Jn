# Claude Code best practices | Code w/ Claude — Anthropic — 25 dk — https://www.youtube.com/watch?v=gv0WHhKelSE
> Kaynak notu: **Transkript var (277 satır)** — `arastirma/youtube/transkriptler/gv0WHhKelSE.txt` (yt-dlp, İng. otomatik altyazı; "Cloud/Claw" = Claude). Güven: **yüksek**. Konuşmacı: Cal (Anthropic Applied AI; Claude Code çekirdek katkıcısı — system prompt, araç açıklamaları/sonuçları ve eval üzerinde çalışıyor) [00:00:41–00:02:56]. Konuşma Mayıs 2025 (Claude 4 çıkış günü) ve güncel dokümandan kısa/eski. Konuşmada **olmayan** konular (spec mülakatı, `/goal`, Stop hook, Writer/Reviewer, skills/subagents dosyaları, fan-out) yalnızca referans olarak güncel resmi dokümandan alındı: `arastirma/youtube/kaynaklar/gv0WHhKelSE-anthropic-best-practices-dokumani.md` (https://code.claude.com/docs/en/best-practices) — bunlar "(doküman)" ile işaretli. İşaretsiz her şey transkriptten.

## Tek paragraf
Anthropic'in "Code w/ Claude" konferansındaki resmi best-practices konuşması. Zihinsel model: Claude Code "terminalde her şeyi yapan, GUI'ye hiç dokunmayan iş arkadaşı" (mentor "Tony" benzetmesi) [00:03:08–00:03:45]. Mimari: Anthropic'in "simple thing that works" ilkesiyle **saf ajan** = talimat + güçlü araçlar (dosya oluştur/düzenle, terminal, MCP) + model bitti diyene kadar döngü [00:03:58–00:04:35]; kod tabanını indeksleme/embedding/RAG ile değil **agentic search** (glob, grep, find → sonuca bak → tekrar ara) ile anlar [00:04:42–00:05:37]; üstünde hafif UI, tehlikeli işlerde insanı devreye sokan izin sistemi ve güvenlik; Bedrock/Vertex'e yönlendirilebilir [00:05:43–00:06:19]. Kullanım alanları: onboarding/keşif, "düşünce ortağı" (underrated), sıfırdan ve mevcut kodda yazma (ekipte anormal yüksek test kapsamı + iyi commit/PR mesajları), SDK ile headless/CI-CD, hata ayıklama, büyük migrasyonlar (eski Java → yeni, PHP → React/Angular), git/docker/bigquery gibi CLI'lar ("sticky rebase") [00:06:33–00:10:27]. Pratikler: CLAUDE.md, izin yönetimi, CLI > MCP, bağlam yönetimi (`/clear`, `/compact`), plan + to-do izleme, "smart vibe coding" (TDD, küçük adım, lint/tsc, düzenli commit), ekran görüntüsü, paralel Claude'lar, Escape / Escape×2, MCP, headless, `/model`, "think hard", IDE entegrasyonu, changelog'u haftalık takip [00:10:34–00:21:19]. Soru-cevap: alt dizin CLAUDE.md'leri, `@` içe aktarma, kurala uymayan yorum satırları, ajanlar arası bağlam = ortak markdown dosyası [00:21:33–00:25:26].

## Önerdiği iş akışı (adım adım)
1. **Kurulum (bir kez)**
   - CLAUDE.md: Claude'un hafızası yok; oturumlar/ekip arası durum CLAUDE.md ile paylaşılır, açılışta prompt'a "geliştiricinin bıraktığı önemli talimatlar" olarak eklenir [00:10:48–00:11:22]. Proje kökü (repo'ya commit → ekip paylaşır) ve ev dizini (her projede geçerli) [00:11:22–00:11:31]. İçerik: test nasıl koşulur, proje yerleşimi (testler nerede, modüller), stil kılavuzu — "you can build these things up over time" [00:11:35–00:11:56]. İç araçlarınızı (Anthropic'te "coup") burada anlatın [00:13:56–00:14:07].
   - İzinler: okuma/arama serbest; yazma ve bash'te onay UI'ı ("yes / always allow / no") [00:12:13–00:12:33]. `Shift+Tab` = auto-accept [00:12:43]; ayarlarda belirli bash komutlarını hep onayla (örn. `npm run test`) [00:12:48–00:13:01].
   - Entegrasyon: `gh` gibi CLI'lar kur ya da MCP sunucusu bağla; iyi bilinen/belgelenmiş CLI varsa onu seç [00:13:08–00:13:50]. Bash + araçlar yetmediğinde MCP'ye bak [00:18:13–00:18:19].
   - (doküman) `/init`, `/permissions`, `claude mcp add`, hook'lar ("her seferinde istisnasız"), `.claude/skills/`, `.claude/agents/`.
2. **Keşif / onboarding**: "where is this feature implemented?" ya da git geçmişiyle hikâye anlattır [00:07:09–00:07:20].
3. **Düşünce ortağı (kod yazmadan önce)**: araştırıp 2-3 seçenek getirsin, dosyaya dokunmasın; birlikte doğrula, sonra projeye geç [00:07:25–00:07:57].
4. (doküman) **Büyük iş → önce spec**: "Interview me in detail using the AskUserQuestion tool..." → `SPEC.md` → yeni oturum.
5. **Plan + to-do**: "bunu düzelt" yerine "ara, nedenini bul, planı söyle" → planı oku/doğrula [00:15:22–00:15:41]. Büyük işte Claude to-do listesi çıkarır; izle, tuhaf bir madde görürsen Escape → "let's change the to-do list" [00:15:46–00:16:01]. (doküman: plan mode `Shift+Tab`×2, `Ctrl+G`.)
6. **Kod + doğrulama = "smart vibe coding"**: sadece Enter'a basıp sonuca bakmak cazip ama: TDD, küçük değişiklik → test koş → geçtiğini gör, her seferinde TypeScript + lint kontrolü, **düzenli commit** (raydan çıkarsa geri dön) [00:16:06–00:16:33]. Görsel: ekran görüntüsü yapıştır ya da "look at this mock.png and then build the website" [00:16:39–00:16:49]. Zor görev/bug'da "think hard" — Claude 4 araç çağrıları arasında da düşünür (açık gri metin = thinking) [00:19:25–00:20:14]. (doküman: `/goal` → Stop hook → doğrulama subagent'ı; kanıt iste.)
7. (doküman) **Review**: `/code-review` ya da temiz bağlamlı subagent: "review the diff against PLAN.md ... Report gaps, not style preferences."
8. **Commit/PR**: iş bitince "write the commit for me, write the PR message for me" [00:08:46–00:08:50].
9. **Oturum hijyeni**: bağlam penceresi 200k token; sağ altta uyarı çıkınca ya `/clear` (CLAUDE.md dışında her şeyi siler) ya `/compact` (araya "başka geliştiriciye devredeceğim, özetle" mesajı eklenir; özet yeni oturumu tohumlar) [00:14:13–00:15:09]. Escape "your best friend": ne zaman araya girip ne zaman bırakacağını bilmek kilit beceri [00:17:42–00:17:56]; **Escape×2** konuşmada geri atlar [00:17:56–00:18:06]. `/model` ve `/config` ile modeli kontrol et/değiştir [00:19:01–00:19:20].
10. **Ölçek**: aynı anda birden çok Claude (tmux/sekmeler) — "I can only do two, but I know people that do four" [00:17:07–00:17:35]. Headless/SDK ile CI-CD, GitHub Actions [00:09:01–00:09:24, 00:18:19–00:18:35]. Ajanlar arası bağlam: ortak markdown dosyası / `ticket.md` devri [00:24:45–00:25:20]. (doküman: worktrees, Writer/Reviewer, `/batch`, `claude -p` fan-out.)
11. **Güncel kal**: github.com/anthropics/claude-code changelog'u — "I check this once a week" [00:21:03–00:21:14].

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
**Konuşmadan (transkript):**
- [00:04:11] Ajan tanımı: "some instructions, some powerful tools, and you let the model just run in a loop until it decides it's done."
- [00:04:58] "We don't do any sort of indexing." → agentic search.
- [00:07:13] Onboarding: "look at this file and look at the git history and just kind of tell me a story about how this code has changed over the past couple weeks."
- [00:07:38] Düşünce ortağı: "search around and kind of figure out how we would do it and maybe report back with like two or three different options." + [00:07:47] "don't start writing any files yet".
- [00:08:50] "hey claude write the commit for me write the PR message for me."
- [00:12:54] İzin: "if you just are like tired of saying yes run npm run test you can just always approve that."
- [00:13:43] CLI vs MCP: "I would recommend using the CLI tool."
- [00:14:50] `/compact` iç işleyişi: "I need to go summarize everything ... I'm going to give this to another developer".
- [00:15:32] Plan: "Hey, I have this bug. Can you search around, figure out what's causing it, and just like tell me a plan how we're going to fix it?"
- [00:16:01] To-do düzeltme: "Hey Claude, let's change the to-do list. I think you're on the wrong path."
- [00:16:49] Görsel: "look at this mock.png and then build the website for me".
- [00:20:14] "when you're working on tasks and solving bugs, throw a think hard in there."
- [00:21:57–00:22:21] Alt dizin CLAUDE.md'leri varsayılan okunmaz (monorepo'da bağlamı patlattığı için); sadece çalışma dizini + ev dizini; ilgili alt dizindekini Claude arama sırasında okumaya teşvik edilir. [00:22:31–00:22:45] CLAUDE.md içinde `@dosya` ile başka dosyaları içe aktar.
- [00:23:18–00:23:37] Yorum satırı sorunu model kaynaklıydı: 3.7 prompt'unda "do not leave comments" olmasına rağmen; Claude 4'te büyük ölçüde düzeldi ve talimata uyum arttı.
- [00:23:59] Model değişince CLAUDE.md'yi gözden geçir: "do I still need this stuff? Maybe I can take some of it out."
- [00:25:01–00:25:11] Devir: "I need you to write some stuff in like ticket.md for another developer" → yeni oturum: "read ticket.md like another developer left this note for you".

**Dokümandan (referans, doküman):**
- CLAUDE.md örneği:
```
# Code style
- Use ES modules (import/export) syntax, not CommonJS (require)
- Destructure imports when possible (eg. import { foo } from 'bar')

# Workflow
- Be sure to typecheck when you're done making a series of code changes
- Prefer running single tests, and not the whole test suite, for performance
```
- CLAUDE.md testi: "Would removing this cause Claude to make mistakes?" Değilse sil. `IMPORTANT` tek tük. `/doctor` budama önerir.
- Doğrulama: "write a validateEmail function. example test cases: user@example.com is true, invalid is false, user@.com is false. run the tests after implementing"
- UI: "[paste screenshot] implement this design. take a screenshot of the result and compare it to the original. list differences and fix them"
- Hata: "fix it and verify the build succeeds. address the root cause, don't suppress the error"
- Mülakat: "Interview me in detail using the AskUserQuestion tool ... then write a complete spec to SPEC.md."
- Hook: "Write a hook that runs eslint after every file edit" / "Write a hook that blocks writes to the migrations folder."
- Compaction kuralı: "When compacting, always preserve the full list of modified files and any test commands"

## Araçlar ve linkler
- Konuşmada: CLAUDE.md (proje / ev dizini / `@` import), izin sistemi + auto-accept `Shift+Tab`, `gh`, MCP, `/clear`, `/compact`, to-do listesi, Escape / Escape×2, `/model`, `/config`, "think hard" (araç çağrıları arası thinking), VS Code & JetBrains entegrasyonu (açık dosyayı bilir) [00:20:20–00:20:41], Claude Code SDK/headless, GitHub Actions, Bedrock/Vertex, tmux.
- Changelog/issue: https://github.com/anthropics/claude-code
- Referans doküman: https://code.claude.com/docs/en/best-practices ; ilgili: /docs/en/memory, /docs/en/skills, /docs/en/sub-agents, /docs/en/hooks-guide, /docs/en/mcp, /docs/en/worktrees, /docs/en/headless
- Komutlar (doküman): `/init`, `/context`, `/doctor`, `/permissions`, `/hooks`, `/rewind`, `/code-review`, `/batch`, `/goal`, `claude -p`, `claude mcp add`, `claude --continue|--resume`

## Hatalar / "bunu yapma" uyarıları
- [00:16:06] Sadece Enter'a basıp sonunda bakmak → TDD + küçük adım + lint/tsc + düzenli commit; raydan çıkınca son commit'e dön.
- [00:15:22] Doğrudan "bug'ı düzelt" demek → önce plan iste, planı oku.
- [00:15:52] To-do listesini izlememek → yanlış yolu erken Escape ile kes; ama gereksiz araya girmek de kayıp — denge kilit beceri [00:17:52].
- [00:14:19] 200k bağlamı doldurup devam etmek → `/clear` ya da `/compact`.
- [00:22:02] Monorepo'da tüm CLAUDE.md'leri yüklemek → bağlam patlar; gerekenleri `@` ile al.
- [00:13:37] İyi belgelenmiş CLI varken MCP kurmak → CLI'ı tercih et.
- [00:23:28] CLAUDE.md'nin her davranışı düzelteceğini sanmak → bazı sorunlar modeldir; model değişince kuralları yeniden test et, gereksizleri at.
- [00:24:37] Karmaşık çok-ajan bağlam paylaşım mimarisi kurmak → "simple thing that works": duruma bir dosyaya yaz, modelin dosya okumasına güven.
- (doküman) Kitchen-sink oturum; iki başarısız düzeltmeden sonra `/clear`. Şişmiş CLAUDE.md → buda, kuralı hook'a çevir. "If you can't verify it, don't ship it." Reviewer her zaman bir şey bulur → sadece doğruluğu etkileyeni düzelt. Küçük işte plan mode overhead.

## Bizim fabrikaya alınacaklar (somut)
1. **Her hat için "doğrulama kontrolü" zorunlu** ("smart vibe coding" [00:16:22] + doküman): web → Playwright ekran görüntüsü + Lighthouse; app → Expo build + Maestro/screenshot; reklam → karakter limiti/politika lint betiği; sosyal → şablon doğrulama. Her küçük adımdan sonra test + lint/tsc; kontrol Stop hook'u olarak `.claude/settings.json`'a girer; geçmeden tur bitmez.
2. **Sipariş = SPEC.md** (doküman): müşteri brief'i geldiğinde ana beyin "Interview me..." kalıbıyla eksikleri sorar, `SPEC.md` üretir; üretim alt sohbeti sadece spec ile temiz bağlamda açılır.
3. **Önce 2-3 seçenek, sonra kod** [00:07:38]: her yeni hat/tasarım kararında "report back with two or three different options ... don't start writing any files yet"; seçimi insan/ana beyin yapar.
4. **Alt sohbetler arası devir = `ticket.md`** [00:25:01]: ana beyin işi not dosyasıyla devreder ("another developer left this note for you"); alt sohbet bitince aynı dosyaya sonuç yazar. Paralel alt sohbetler ortak bir durum markdown'ına check-in yapar [00:24:50]; karmaşık orkestrasyon katmanı kurulmaz.
5. **Explore→Plan→Code→Commit** her üretim sohbetinin iskeleti; plan onaylanır (kalite kapısı 1); çalışırken to-do listesi izlenir, sapınca Escape + "let's change the to-do list" [00:16:01]; **düzenli commit = geri dönüş noktası** [00:16:28].
6. **Fresh-context review** (doküman): teslim öncesi `/code-review` + "review against SPEC.md, report gaps not style" subagent'ı (kalite kapısı 2).
7. **CLAUDE.md ≤ 60 satır**, içinde: test/derleme komutları, klasör haritası, stil kuralları, iç araçlarımız (fabrika betikleri) [00:11:35, 00:14:01]; alt klasör CLAUDE.md'leri `@` ile [00:22:39]; **model sürümü değişince CLAUDE.md/skill kuralları yeniden test edilir, gereksizler atılır** [00:23:59]. Alan bilgisi → skill'lere.
8. **CLI > MCP** [00:13:43]: iyi belgelenmiş CLI varsa (gh, vercel, eas, gcloud) onu kullan; MCP sadece CLI'ı olmayan servisler için.
9. **İzin allowlist'i** [00:12:48]: sık ve güvenli komutlar (`npm run test`, lint, build, screenshot betikleri) `.claude/settings.json`'da hep-onaylı; yazma/silme/yayın komutları onaylı kalır.
10. **Teslimin son adımı: commit + PR/teslim notunu Claude yazar** [00:08:50]; her teslim tutarlı, açıklamalı.
11. **Zor iş/bug prompt şablonuna "think hard"** [00:20:14] eklenir; tasarım kararlarında görsel referans (`mock.png`) dosya olarak verilir [00:16:49].
12. **Ortak skill paketi (plugin)** (doküman): `brief-to-spec`, `design-review`, `ads-variants`, `social-calendar`, `publish-checklist` + `security-reviewer`/`design-reviewer` agent'ları.
13. **Fan-out + paralellik**: aynı şablonla çok müşteri → `claude -p` döngüsü veya `/batch` (doküman); ekip başına 2-4 paralel oturum hedefi [00:17:35]. **Oturum disiplini** CLAUDE.md'ye: "Bir müşteri = bir oturum; hat değişince /clear; uzun işte /compact; iki düzeltmeden sonra yeniden başlat."
14. **Haftalık changelog taraması** [00:21:14]: ana beyin haftada bir anthropics/claude-code changelog'unu okuyup fabrikaya etkisi olan yenilikleri not eder.
