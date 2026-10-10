# How the Claude Code team uses Claude Code — Claude (Anthropic) — 22 dk — https://www.youtube.com/watch?v=S-sYlFiGFv8
> Kaynak notu: Transkript: var (yt-dlp, 2026-10-10) — `arastirma/youtube/transkriptler/S-sYlFiGFv8.txt` (konuşmacı ayrımı yok). Konuşmacılar (açıklamadan): Thariq Shihipar, Sid Bidasaria, Robert Boyce. Bölüm zaman damgaları açıklamadan. Mekanik ayrıntılar (`/goal`, Workflow, Routine sözdizimi) videoda **geçmez**; `kaynaklar/S-sYlFiGFv8-dynamic-workflows-dokumani.md`, `kaynaklar/eSP7PLTXNy8-routines-dokumani.md`, `kaynaklar/S-sYlFiGFv8-anthropic-ekipleri-nasil-kullaniyor.md`'den gelir → "(doküman)". Güven: **yüksek** (ne ve nasıl söylendiği transkriptten).

## Tek paragraf
Claude Code ekibinden üç mühendis, bir yıl önce "prompt → geri bildirim → izin onayı" olan kullanımın bugün neye dönüştüğünü anlatıyor: işlerinin **%70-80'i Claude Tag** (Slack-native ajan) üzerinden, kalan %20'de TUI/desktop'u "rafine etmek ya da Claude'ları mikro-yönetmek" için açıyorlar; tek tek araç çağrısını izlemekten "bir hedefim var, hedefi modele veriyorum"a geçtiler. Teknoloji iki ayda bir değiştiği için harness özelliklerine bağlanmıyorlar (to-do listesi örneği: Sonnet 3.5 için hayati, bir yıl sonra gereksiz). AskUserQuestion'ın yerini soru soran HTML artifact'leri alıyor. Laptop → uzak dev box → Claude Code on the web → routines yolculuğu. Code review'da Claude nit'leri kendisi kapatıyor, insan büyük resme bakıyor; review'daki fan-out + adversarial doğrulama **workflows**'u doğurdu ("MapReduce problemi"). Claude Tag, Claude Tag ile inşa ediliyor; üç temel primitif: **doğrulama, code review, geri bildirim (veri/olay deposu)**. Bölümler: 0:35 araç çağrısından hedeflere; 2:17 iki ayda bir değişen teknoloji; 4:48 AskUserQuestion, artifacts, Claude Tag; 6:41 uzaktan loop ve routine; 8:52 code review → dynamic workflows; 14:04 Claude Tag'i Claude Tag ile inşa; 18:37 eski mühendislikten ne eksik.

## Önerdiği iş akışı (adım adım)
1. **Hedef ver, görev değil** (0:35): "a zoomed out view of, like, I have a goal and I'd like to achieve this goal. And you just kind of give that goal to the model to achieve for you." Örnek (Thariq): funnel'ı kendi fikrini dikte etmek yerine "let me ask Claude to improve the funnel ... maybe I can give my idea as an example". (doküman: `/goal` koşulu.)
2. **Netleştirme: AskUserQuestion → artifact** (4:48): AskUserQuestion önce planlamadan sonra sabitti, sonra araç oldu; bugün "I just create an artifact. And the artifact asks me questions ... in HTML, it has diagrams and mockups".
3. **Claude Tag / Slack** (0:35, 14:04): işi Slack'ten ver; ajan ürün bağlamına ve ekip kararlarına erişir → karar kalitesi artar. Arayüz transkriptten ayrık: Claude ne zaman ne söyleyeceğine kendisi karar verir ("being forced even to let Claude cook").
4. **Bulutta sürekli çalışma → loops & routines** (6:41): laptop kapanınca ajan durur → uzak dev box → Claude Code on the web (hosted container; dev ortamına erişim kurmak zahmetli ama "10x my productivity") → routine: "every day go and look at all the feedback that we're getting and ... bucket them into buckets of importance and fix the ones that it actually has high confidence in fixing." Oturum sınırını aşıp "bir seviye yukarıdan" prompt'lama. (doküman: `/schedule`, `/loop`.)
5. **Code review'u yeniden böl** (8:52): nit'leri Claude bulur ve kendisi düzeltir; insan reviewer büyük resme (API neden böyle, servis sınırları neden orada) odaklanır; Claude bu bağlamı toplayıp insana devreder.
6. **Fan-out + adversarial doğrulama → workflows** (8:52): bug avında geniş fan-out; "for each bug, you might do an adversarial review where you ask it to look at the bug from three different ... perspectives and see if the bug is actually real" → sadece önemli bulgular kalır. Aynı kalıp performans, deep research, tatil planı. Topolojiyi Claude kendisi tasarlar; for-döngüsü deterministik olduğu için güven verir. (doküman: Workflow agent/pipeline/parallel, `/code-review`, `/deep-research`.)
7. **Ajanın dev döngüsünü kolaylaştır** (14:04): "making sure that the development environment and dev loop are really, really easy for Claude to use. It can just, like, do everything that I, as a human need to do to build the software and test that it's working end to end."
8. **Doğrula + kanıt** (14:04): ekibin PR'larında Claude "tests it, it sends me screenshots"; TUI değişikliğinde "I asked it to record itself using the TUI"; insan son olarak klonlayıp elle dener (ileride gerek kalmayacağı beklentisi).
9. **Geri bildirim döngüsü**: ürüne event ekle, dahili yayınla, Claude Tag izlesin ve biri geri bildirim verince seni etiketlesin; metriklere göre iyileştirmeyi yine hedef olarak ver.
10. **Harness'i sadeleştir** (2:17): model büyüdükçe "all these like features that we built ... We don't need those. We can get rid of them"; daha büyük işler için gereken araçlar ise değişir.

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- "70 to 80% of my work happens on Claude Tag now, and for 20% of the work, I will maybe open up the TUI or the desktop app to like, refine something. Or if I want to be micromanaging my Claudes".
- To-do anekdotu: "you would give it like five things to do, and it would do three things and just give up ... a year from then ... you don't really need the to do list anymore ... you got to be very unattached to the things you're building."
- Routine prompt'u (sözlü): "every day go and look at all the feedback ... bucket them into buckets of importance and fix the ones that it actually has high confidence in fixing."
- Adversarial review: "look at the bug from three different, you know, opinions or perspectives and see if the bug is actually real."
- "Claude is actually really good at making its own harnesses." / "it's like a MapReduce problem ... you need to filter back for human consumption".
- Üç primitif: "there's verification, there's code review, and there's getting feedback."
- (doküman) `/goal` → "A separate evaluator re-checks it after every turn and Claude keeps working until the goal resolves." ; Workflow: "use a workflow to review every file changed in this PR for correctness issues, then merge the per-file findings into one ranked summary"; Routine: "the prompt must be self-contained and explicit about what to do and what success looks like."

## Araçlar ve linkler
- https://code.claude.com/docs/en/overview (açıklamadan), https://x.com/ClaudeDevs
- Videoda: Claude Tag (Slack), Artifacts, AskUserQuestion, to-do list, Claude Code on the web (hosted container), loops, routines, code review bot, workflows, auto mode, memory
- (doküman) `/loop`, `/schedule`, `/workflows`, `/code-review`, `/deep-research`, `claude --permission-mode auto`

## Hatalar / "bunu yapma" uyarıları
- Her araç çağrısını/transkripti izlemek → hedef ver, sonuca bak; mikro-yönetim sadece istisna.
- Harness özelliğine bağlanmak → model büyüyünce sil (to-do listesi örneği).
- Human review'u nit avına çevirmek ("three little nit picks ... to prove that I actually was reading") → nit'ler Claude'a, insan mimari bağlama.
- Fan-out çıktısını ham okumak ("if I read the fan out output I'm going to go crazy") → filtre + doğrulama katmanı.
- Ajanı lokal laptop'a bağlı bırakmak → bulut container; dev ortam erişimini bir kez kur.

## Bizim fabrikaya alınacaklar (somut)
1. **Hedef odaklı sipariş kartı**: her iş emri "başarı koşulu" cümlesiyle başlar (örn. "Lighthouse ≥ 90, 3 cihazda ekran görüntüsü onaylı, logo/renkler DESIGN.md ile uyumlu"); ana beyin bunu `/goal` (doküman) olarak alt sohbete verir; kendi fikrimizi dikte etmek yerine "örnek" olarak ekleriz.
2. **Müşteriye soru = artifact**: brief belirsizse ana beyin müşteriye HTML soru artifact'i üretir (mockup + seçenekli sorular); cevaplar SPEC.md'ye.
3. **Tek giriş noktası (Claude Tag benzeri)**: talepler bir kanala/issue'ya düşer, cloud oturumu PR açar; insan sadece review eder. Bizde: GitHub issue → cloud session.
4. **Routine takvimi**: gece sağlık kontrolü, haftalık içerik takvimi, aylık reklam analizi; ayrıca **"geri bildirim triyajı" routine'i**: müşteri yorumlarını önem kovalarına ayır, yüksek güvenle düzeltilebilenleri düzelt (ekibin kendi kalıbı).
5. **Adversarial review standardı**: web hattında fan-out (sayfa başına ajan) → her bulgu için 3 bakış açısıyla "gerçek mi?" doğrulaması → sıralı özet; insan sadece süzülmüş bulgulara + mimari/marka kararlarına bakar.
6. **Kanıtlı teslim**: her PR'da ekran görüntüsü; app/akış değişikliğinde ekran kaydı (Claude kendini kaydeder); bunlar müşteri önizleme artifact'ine girer.
7. **Ajan dev döngüsü kolay olsun**: her müşteri repo'sunda tek komutla kur/çalıştır/test (`pnpm check`, seed veri, test hesapları) — Claude'un insanın yaptığı her doğrulamayı yapabilmesi şartı.
8. **Harness diyet günü**: her model sürümünde skill/hook/agent envanterini gözden geçir; modelin zaten yaptığı şeyi sil (`ajans/sistem/ENVANTER.md`).
