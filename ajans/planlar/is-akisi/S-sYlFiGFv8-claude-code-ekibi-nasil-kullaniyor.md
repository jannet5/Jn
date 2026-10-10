# How the Claude Code team uses Claude Code — Claude (Anthropic) — 22 dk — https://www.youtube.com/watch?v=S-sYlFiGFv8
> Kaynak notu: Transkript yok (bulut IP engeli). Dayanak: videonun açıklaması ve **bölüm listesi** (zaman damgalı), konuşmacılar Thariq Shihipar, Sid Bidasaria, Robert Boyce; ek olarak `kaynaklar/S-sYlFiGFv8-anthropic-ekipleri-nasil-kullaniyor.md` (blog) ve `kaynaklar/S-sYlFiGFv8-dynamic-workflows-dokumani.md`, `kaynaklar/eSP7PLTXNy8-routines-dokumani.md` (bölüm başlıklarının resmi dokümanları). Güven: **orta** (ne anlatıldığı bölüm başlıklarından kesin, nasıl söylendiği bilinmiyor; "(tahmin)" işaretli yerler çıkarım).

## Tek paragraf
Claude Code ekibinden üç mühendis, bir yıl önce "prompt → geri bildirim → izin onayı" olan kullanımın bugün neye dönüştüğünü anlatıyor: kodun çoğunu **Claude Tag** (Slack içindeki Claude) üzerinden yazdırıyorlar; Claude'a **görev değil hedef** veriyorlar; modeller büyüdükçe harness özelliklerini **siliyorlar**; ürün terminalden çıkıp çok yüzeyli oldu (auto mode, workflows, routines). Bölümler: 0:35 araç çağrısından hedeflere; 2:17 iki ayda bir değişen teknoloji üzerine inşa; 4:48 AskUserQuestion, artifacts, Claude Tag; 6:41 uzaktan loop ve routine çalıştırma; 8:52 code review'un dynamic workflows'a ilham vermesi; 14:04 Claude Tag'i Claude Tag ile inşa: Slack'te doğrulama ve geri bildirim döngüleri; 18:37 eski mühendislikten ne eksik.

## Önerdiği iş akışı (adım adım)
1. **Hedef ver, görev değil** (0:35): "şu dosyayı düzenle" yerine "bu PR merge'lenebilir olsun / bu bug kullanıcıya görünmesin" gibi sonuç tanımı; Claude araç çağrılarını kendi seçer. (Doküman karşılığı: `/goal` koşulu, doğrulama kontrolü.)
2. **AskUserQuestion ile netleştirme** (4:48): belirsizlikte Claude soru sorar; mühendis cevaplar; spec netleşir. (Best-practices'teki "interview me" kalıbıyla aynı araç.)
3. **Artifacts** (4:48): çıktıyı (rapor, plan, dashboard) paylaşılabilir sayfa olarak üretme; ekip içi gözden geçirme bunun üzerinden.
4. **Claude Tag / Slack** (0:35, 14:04): işi Slack thread'inden başlat; Claude bulutta çalışır, PR açar; doğrulama ve geri bildirim döngüsü de Slack'te — Claude Tag'in kendisi bu şekilde inşa edildi (dogfooding).
5. **Loops ve Routines uzaktan** (6:41): tekrar eden işler (`/loop`) ve zamanlanmış/olay tetikli routine'ler bulutta, laptop kapalıyken; `/schedule` ile oluştur (ayrıntı: routines kaynak dosyası).
6. **Code review → dynamic workflows** (8:52): çok dosyalı review'da tek ajan tek tip hataya kayar; çözüm, bulguyu **bağımsız ajanın çürütmeye çalışması** (adversarial verify) ve bunun betikleştirilmesi → `/code-review`, Workflow (agent/pipeline/parallel), `/deep-research`.
7. **Harness'i sadeleştir** (2:17): model yeni yetenek kazandığında onu taklit eden harness özelliğini sil; iki ayda bir yeniden değerlendir. (tahmin: ekip bunu ürün kararı olarak anlatıyor; bizim için "skill/hook enflasyonu" uyarısı.)
8. **Auto mode**: izin onayı yerine sınıflandırıcı; mühendis riskli olanlarda devreye girer.

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- Video metni yok; açıklamadan aynen: "why they give Claude goals rather than tasks", "why they delete harness features as the models outgrow them", "primitives like auto mode, workflows, and routines".
- Dokümanlardan (bölümlerin karşılığı, aynen): `/goal` → "A separate evaluator re-checks it after every turn and Claude keeps working until the goal resolves." ; Workflow prompt'u: "use a workflow to review every file changed in this PR for correctness issues, then merge the per-file findings into one ranked summary"; Routine: "the prompt must be self-contained and explicit about what to do and what success looks like."

## Araçlar ve linkler
- https://code.claude.com/docs/en/overview (açıklamadan), https://x.com/ClaudeDevs
- Claude Tag (Slack), Artifacts, AskUserQuestion, `/loop`, `/schedule` (Routines), `/workflows`, `/code-review`, `/deep-research`, auto mode (`claude --permission-mode auto`)

## Hatalar / "bunu yapma" uyarıları
- Görev listesi dikte etme → hedef ver; aksi halde model mikro-yönetilmiş, insan darboğaz olur.
- Harness'e model eksiğini kapatan özellik yığma → iki ayda bir sil, aksi halde bakım yükü ve bağlam israfı.
- Tek ajanla büyük review → tek tip hataya kayar; bağımsız doğrulayıcı kullan.
- İzin onayı tıklayarak "review" yaptığını sanma → auto mode + gerçek doğrulama kontrolü.

## Bizim fabrikaya alınacaklar (somut)
1. **Hedef odaklı sipariş kartı**: her iş emri "başarı koşulu" cümlesiyle başlar (örn. "Lighthouse ≥ 90, 3 cihazda ekran görüntüsü onaylı, müşteri logosu/renkleri DESIGN.md ile uyumlu"); ana beyin bunu `/goal` olarak alt sohbete verir.
2. **Slack/Claude Tag benzeri tek giriş noktası**: müşteri talepleri bir kanala düşer, routine/cloud oturumu PR açar; insan sadece review eder. Bizde: GitHub issue → cloud session.
3. **Routine takvimi**: gece sağlık kontrolü (müşteri siteleri), haftalık içerik takvimi, aylık reklam analizi — `/schedule` ile; prompt'lar `sistem/routines/*.md`'de self-contained.
4. **Adversarial review'ı standartlaştır**: web hattında `/code-review` + "design-review" workflow (her sayfa için ayrı ajan → bulguları birleştir → bağımsız ajan çürütmeyi dener).
5. **Harness diyet günü**: her model sürümünde skill/hook/agent envanterini gözden geçir; modelin zaten yaptığı şeyi sil (`ajans/sistem/ENVANTER.md`).
6. **Artifacts ile teslim**: müşteri önizlemesi/raporu artifact sayfası olarak; yorumlar oradan geri döngüye.
