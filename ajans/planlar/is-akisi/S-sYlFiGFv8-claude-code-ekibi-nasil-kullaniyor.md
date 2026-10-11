# How the Claude Code team uses Claude Code — Claude (Anthropic) — 22 dk — https://www.youtube.com/watch?v=S-sYlFiGFv8
> Kaynak notu: Transkript var (661 satır; yt-dlp, `arastirma/youtube/transkriptler/S-sYlFiGFv8.txt`, konuşmacı ayrımı yok). Güven: **yüksek**: ne söylendiği ve zaman damgaları transkriptten. Konuşmacılar (açıklamadan): Thariq Shihipar, Sid Bidasaria, Robert Boyce; transkriptte "Sid" workflows'u anlatan, "Robert" Claude Tag'i inşa eden olarak hitap ediliyor [00:11:08], [00:14:06]. Videoda **hiç geçmeyen** mekanik ayrıntılar (`/goal`, Workflow API, Routine/`/schedule` sözdizimi) referans dokümanlardan geliyor ve "(doküman)" diye işaretli: `arastirma/youtube/kaynaklar/S-sYlFiGFv8-dynamic-workflows-dokumani.md`, `arastirma/youtube/kaynaklar/S-sYlFiGFv8-anthropic-ekipleri-nasil-kullaniyor.md`, `arastirma/youtube/kaynaklar/eSP7PLTXNy8-routines-dokumani.md`.

## Tek paragraf
Claude Code ekibinden üç mühendis, bir yıl önceki "prompt ver → geri bildirim ver → izin onayla" kullanımının bugün neye dönüştüğünü konuşuyor. İşlerinin **%70-80'i Claude Tag** (Slack-native ajan) üzerinden gidiyor. TUI'yi ya da desktop'u kalan %20'de, bir şeyi rafine etmek ya da "Claude'larını mikro-yönetmek" için açıyorlar [00:01:23]. Tek tek araç çağrısını izlemeyi bırakıp "bir hedefim var, hedefi modele veriyorum" noktasına geçmişler [00:01:35]. Altlarındaki teknoloji "iki ayda bir" değiştiği için harness özelliklerine bağlanmıyorlar. To-do listesi Sonnet 3.5 döneminde hayatiydi, bir yıl sonra gereksiz kaldı [00:03:21]. AskUserQuestion'ın yerini soru soran HTML artifact'leri alıyor [00:06:13]. Laptop → uzak dev box → Claude Code on the web → routines yolculuğunu anlatıyorlar [00:06:44]. Code review'da nit'leri Claude kendisi kapatıyor, insan büyük resme bakıyor [00:09:16]. Review'daki fan-out ve adversarial doğrulama ihtiyacı **workflows**'u doğurmuş ("MapReduce problemi") [00:11:11]. Claude Tag de Claude Tag ile inşa ediliyor, kullanıcı arayüzü transkriptten ayrılmış [00:14:51]. Üç temel primitif sayıyorlar: **doğrulama, code review, geri bildirim** [00:17:28]. Kapanışta eski mühendislikten neyin eksik kaldığını konuşuyorlar: performans mühendisliği, piksel piksel UI çalışması. Yeni odak "fikirden prototipe, oradan prodüksiyona hız" [00:19:26].

## Önerdiği iş akışı (adım adım)
1. **Hedef ver, görev değil** [00:01:35]: transkriptleri ve her tool call'u izlemekten "zoomed out" bakışa geçmişler. Somut örnek funnel [00:17:05]: Thariq önce kendi fikrini dikte edecekmiş, sonra "let me ask Claude to improve the funnel" demiş. Kendi fikri yalnızca örnek olarak giriyor. (doküman: `/goal` koşulu bunun mekaniği; videoda adı geçmiyor.)
2. **Netleştirme: AskUserQuestion → artifact** [00:05:50]: AskUserQuestion önce planlamadan sonraki sabit bir adımdı, sonra modelin çağırdığı bir araca dönüştü. Aracı iyi çağırtmak uzun sürmüş. Bugün "I just create an artifact" diyor [00:06:14]: artifact soruları HTML olarak, diyagram ve mockup'larla birlikte soruyor.
3. **Kaynak bağlam: Slack'teki ajan** [00:00:54]: Claude Tag Slack'te yaşadığı için ürün bağlamına ve ekibin aldığı kararlara kendisi bakabiliyor. Bu da karar kalitesini artırıyor.
4. **Paydaş → mock → uygulama → event → izleme** [00:16:13]: (a) "who should I talk to who might be interested in it?" diye paydaş buldurmak. (b) Slack'te mockup yaptırmak, telefondan bile bakılabiliyor. (c) Uygulamayı yaptırmak. (d) Ürüne çok sayıda event eklemek ve dahilde yayına almak. (e) Claude Tag'in kullanımı izlemesi ve biri geri bildirim verince seni etiketlemesi [00:16:57]. (f) Event'lere bakıp iyileştirmeyi yine hedef olarak vermek.
5. **Bulutta sürekli çalışma → loops & routines** [00:06:44]: laptop kapanınca ajan da duruyordu. Önce uzak dev box geldi (SSH zahmetli). Sonra Claude Code on the web geldi: arka planda çalışan hosted container'lar [00:07:37]. Dev ortamına erişimi kurmak "a bit of a pain" ama "10x my productivity" [00:07:56]. Bulutta olunca routine kurulabiliyor: her gün geri bildirimleri önem kovalarına ayırıp yüksek güvenle düzeltilebilenleri düzeltmek [00:08:15]. Oturum sınırını kırıp "one level above" prompt'luyorlar [00:08:41]. (doküman: `/loop`, `/schedule`.)
6. **Code review'u yeniden böl** [00:09:16]: küçük nit'leri Claude bulup otonom düzeltiyor. İnsan reviewer büyük resme odaklanıyor: API neden böyle kurulmuş, servis sınırları neden orada çizilmiş. Claude bu bağlamı review'a çekip insana devrediyor [00:10:17].
7. **Fan-out + adversarial doğrulama → workflows** [00:10:42]: bug avında geniş fan-out yapıp sonuçları birleştiriyorlar ("test time compute"). Her bug 3 bakış açısından "gerçek mi?" diye kontrol ediliyor, geriye yalnızca dikkat isteyenler kalıyor [00:11:28]. Aynı kalıp performans sorunlarında, deep research'te ve tatil planında da işliyor [00:11:54]. Tahoe örneği: ~10 farklı arama, sıralayan ajanlar, doğrulayan ajanlar, son sonuç [00:12:31]. Topolojiyi Claude kendisi tasarlıyor [00:12:11]. Orkestrasyon kod olduğu için for-döngüsü hiçbir öğeyi atlamıyor, bu da güven veriyor [00:13:25]. (doküman: Workflow agent/pipeline/parallel, `/code-review`, `/deep-research`.)
8. **Ajanın dev döngüsünü kolaylaştır** [00:14:18]: Robert'in ana odağı bu: dev ortamı ve dev döngüsü Claude için "really, really easy" olmalı. İnsanın yaptığı her kur/test işini uçtan uca yapabilmeli. Entegre yazılım büyüdükçe bu iş zorlaşıyor [00:14:38].
9. **Arayüzü transkriptten ayır** [00:14:51]: Claude Tag Slack'e bir mesaj aracıyla yazıyor. İç monolog Slack'te görünmüyor, yalnızca tam transkripte link var. İlk başta "scary" gelmiş, sonra özgürleştirici olmuş: "Claude is choosing what to say to me and when" [00:15:40].
10. **Doğrula + kanıt** [00:17:49]: Claude Code PR'larında Claude "tests it, it sends me screenshots". TUI değişikliğinde Thariq "record itself using the TUI" demiş, sonra yine de klonlayıp elle denemiş [00:18:17]. Beklentisi ileride klonlamaya bile gerek kalmaması.
11. **Harness'i sadeleştir, ama yeni ölçeğe araç ekle** [00:04:04]: model iyileştikçe failure-mode yamaları siliniyor. Daha büyük işler için modelin "coherent" kalmasını sağlayacak yeni araçlar gerekiyor [00:04:29].

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
Videoda CLAUDE.md, skill ya da hook içeriği **gösterilmiyor**. Somut ifadeler şunlar:
- [00:01:23] "70 to 80% of my work happens on Claude Tag now"
- [00:01:47] "I have a goal and I'd like to achieve this goal."
- [00:02:42] Teknoloji "fundamentally shifts underneath you every two months".
- [00:03:32] To-do öncesi durum: "you would give it like five things to do, and it would do three things and just give up."
- [00:03:58] "you got to be very unattached to the things you're building."
- [00:04:11] Harness özellikleri "just to cover for some of the failure modes" için var.
- [00:06:14] "I just create an artifact. And the artifact asks me questions"
- [00:08:18] Routine prompt'u (sözlü): "every day go and look at all the feedback ... bucket them into buckets of importance and fix the ones that it actually has high confidence in fixing."
- [00:09:37] Eski review alışkanlığı: "three little nit picks to post", yani okuduğunu kanıtlamak için.
- [00:11:32] "look at the bug from three different ... perspectives and see if the bug is actually real."
- [00:12:11] "Claude is actually really good at making its own harnesses."
- [00:13:05] "It's like a MapReduce problem." / [00:13:09] "if I read the fan out output I'm going to go crazy."
- [00:13:19] Güveni artırmanın yolu: "throw test time compute at the problem."
- [00:13:48] "here's a for loop that's going to run over these items" → hiçbirini atlamaz.
- [00:15:50] "being forced even to let Claude cook."
- [00:16:21] "who should I talk to who might be interested in it?"
- [00:17:16] "let me ask Claude to improve the funnel ... maybe I can give my idea as an example"
- [00:17:28] "there's verification, there's code review, and there's getting feedback."
- [00:18:01] "It tests it, it sends me screenshots"
- [00:21:30] "here's what I want to accomplish. Let's like break it down"
- (doküman) `/goal`: "A separate evaluator re-checks it after every turn and Claude keeps working until the goal resolves." Workflow örneği: "use a workflow to review every file changed in this PR for correctness issues, then merge the per-file findings into one ranked summary". Routine kuralı: "the prompt must be self-contained and explicit about what to do and what success looks like."

## Araçlar ve linkler
- https://code.claude.com/docs/en/overview (açıklamadan), https://x.com/ClaudeDevs
- Videoda adı geçenler: Claude Tag (Slack-native ajan), Artifacts (HTML, diyagram/mockup), AskUserQuestion, to-do list, uzak dev box'lar, Claude Code on the web (hosted container), loops, routines, code review bot, workflows, auto mode, memory [00:18:45], proaktif ajan, izin sistemi [00:05:02]
- (doküman) `/loop`, `/schedule`, `/workflows`, `/code-review`, `/deep-research`, `claude --permission-mode auto`

## Hatalar / "bunu yapma" uyarıları
- Her araç çağrısını ve transkripti izlemek [00:01:35] yerine hedef verin, sonuca bakın. Mikro-yönetim istisna olmalı (%20).
- Harness özelliğine bağlanmayın [00:03:58]: model büyüyünce silin (to-do listesi örneği).
- Human review'u nit avına çevirmeyin [00:09:33]: nit'ler Claude'a, mimari bağlam insana.
- Fan-out çıktısını ham okumayın [00:13:09]: araya filtre + doğrulama katmanı koyun.
- Ajanı lokal laptop'a bağlı bırakmayın [00:06:51]: bulut container'a taşıyın, dev ortam erişimini bir kez kurun.
- Kendi fikrinizi dikte etmeyin [00:17:13]: hedefi verin, fikrinizi örnek olarak ekleyin.
- Her ayrıntıya derinlemesine dalmayın [00:20:02]: odağı "zoomed out" tutun, detay işçiliğini (CSS gradient'leri gibi) Claude'a bırakın.

## Bizim fabrikaya alınacaklar (somut)
1. **Hedef odaklı sipariş kartı**: her iş emri bir "başarı koşulu" cümlesiyle başlar (örn. "Lighthouse ≥ 90, 3 cihazda ekran görüntüsü onaylı, logo/renkler DESIGN.md ile uyumlu"). Ana beyin bunu alt sohbete `/goal` (doküman) olarak verir. Kendi fikrimizi dikte etmeyiz, "örnek" olarak ekleriz (funnel kalıbı [00:17:16]).
2. **Müşteriye soru = artifact**: brief belirsizse ana beyin müşteriye HTML soru artifact'i üretir (mockup + seçenekli sorular) [00:06:14]. Cevaplar SPEC.md'ye girer.
3. **Tek giriş noktası (Claude Tag benzeri)**: talepler bir kanala ya da issue'ya düşer, cloud oturumu PR açar, insan yalnızca review eder. Bizde: GitHub issue → cloud session. Ajan, müşteri ve marka kararlarının yazılı olduğu yere (karar defteri/SPEC) erişebilmeli [00:01:03].
4. **Routine takvimi**: gece sağlık kontrolü, haftalık içerik takvimi, aylık reklam analizi. Ayrıca bir **"geri bildirim triyajı" routine'i** [00:08:18]: müşteri yorumlarını önem kovalarına ayırır, yüksek güvenle düzeltilebilenleri düzeltir, kalanı insana raporlar.
5. **Adversarial review standardı** [00:11:28]: web hattında fan-out (sayfa başına bir ajan), ardından her bulgu için 3 bakış açısıyla "gerçek mi?" doğrulaması, sonunda sıralı özet. İnsan yalnızca süzülmüş bulgulara ve mimari/marka kararlarına bakar. Orkestrasyon kodla (for-döngüsü) yapılır ki hiçbir sayfa atlanmasın [00:13:48].
6. **Kanıtlı teslim** [00:18:01]: her PR'da ekran görüntüsü olur. App ya da akış değişikliğinde ekran kaydı alınır (Claude kendini kaydeder). Bunlar müşteri önizleme artifact'ine girer.
7. **Ajan dev döngüsü kolay olsun** [00:14:24]: her müşteri repo'sunda kur/çalıştır/test tek komutla olur (`pnpm check`, seed veri, test hesapları). Şart şu: Claude, insanın yaptığı her doğrulamayı uçtan uca yapabilmeli.
8. **Harness diyet günü** [00:04:22]: her model sürümünde skill/hook/agent envanteri gözden geçirilir, modelin zaten yaptığı şeyler silinir (`ajans/sistem/ENVANTER.md`). Daha büyük işler için eksik araç varsa o da eklenir.
9. **Yayın sonrası telemetri + izleme döngüsü** (yeni) [00:16:42]: her teslim edilen site/app'e temel event'ler (CTA tıklama, form gönderme, funnel adımları) eklenir. Bir routine bunları izler, müşteri geri bildirim bırakınca ilgili kişiyi etiketler. Funnel düşükse iyileştirme sipariş kartına hedef olarak girer.
10. **Raporlama = mesaj aracı, transkript değil** (yeni) [00:15:08]: alt oturumlar müşteriye ve ana beyne yalnızca seçilmiş özet mesaj gönderir, tam transkript link olarak eklenir. İnsan transkripti okumak zorunda kalmaz.
11. **Paydaş/onay adımı** (yeni) [00:16:21]: yeni bir iç araç ya da şablon önerilirken ilk adım olarak "bunu kim kullanır, kimin onayı gerekir?" sorusu Claude'a sordurulur. Mock Slack'e (bizde müşteri kanalına) atılır, onay gelmeden uygulamaya geçilmez.
