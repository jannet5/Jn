# Ajans iş modeli: teklif, fiyat, müşteri bulma, reklam & sosyal — X.com bulguları

## 1. Tek kişilik AI ajansı anatomisi (Ronin, $40k MRR, article)
Kaynak: https://x.com/DeRonin_/article/2062301065312407891
- **Müşteri karışımı**: 4 anchor × $5k + 6 core × $2.5k + 4 lite × $1.25k = $40k/ay, 14 müşteri. Opex ~$750 (inference ~$350, altyapı ~$180, SaaS ~$220). Marj %90+.
- **Darboğaz müşteri değil, teslimat**. Eski model: 3 müşteri → junior al. Yeni: üretim katmanını model yapar.
- **Teklif kuralı**: "sistemle teslim edilebilir olmalı, kahramanlıkla değil." Productized, tekrarlanabilir, dar scope. "Müşteri desteğini otomatikleştiririz" = tuzak; "en sık 20 ticket tipini çözen AI support agent + aylık tuning retainer'ı" = ürün. **Retainer şart** (solo matematik ancak böyle çalışır). Outcome'a göre fiyatla, sistemle teslim et.
- **4 aşamalı pipeline**: Intake (sen, 10 dk, spec'e çevir) → Üretim (model) → QA (sen, 15-20 dk; review üretmekten 10x hızlı) → Handoff (şablon, otomatik).
- **Model roster**: ucuz workhorse (%90 üretim) + premium (%10 yüksek riskli mimari/güvenlik) + ücretsiz/lokal (temizlik). Kural: yanlış cevabın maliyeti model farkının 100 katından fazlaysa pahalıyı kullan.
- **Ölçek = skill**: her çözülen workflow skill olarak kaydedilir (prompt, config, edge case); 5. support agent 1.'nin kesri kadar sürer. Arka plan agent'ları 24/7 (izleme, içerik). Swarm: ana agent işi böler, paralel sub-agent'lar.
- **Müşteri bulma**: içerik (vaka çalışması, before/after — en büyük kanal, yavaş başlar, bileşik), kendi kendini satan niş teklif ("X tipi işletme için AI support agent, şu kadar, şu sonuç"), teslimata gömülü referans döngüsü (kazanç anında iste + küçük teşvik), hafif outbound (ayda 1-2 retainer yeter).
- **Dürüst sınır**: QA kapasitesi tavan; 14 müşteri rahat, üstünde fiyat artır/müşteri sınırla veya ilk işe alım QA kişisi (üretici değil!).
- **90 gün**: 1-30 tek productized hizmet + ilk müşteri (indirim ↔ vaka çalışması) → 31-60 teslimat motorunu kur, ilk skill'i kaydet, 3-4 müşteri ($8-12k) → 61-90 arka plan agent'ları, içerik/referans çarkı, QA checklist ($10k+).

## 2. 180 günlük $500k plan (Luke Pierce, Boom Automations, article)
Kaynak: https://x.com/lukepierceops/status/2101026188844302617
- **Teklif**: $3k ücretli audit → $30k build → $3k/ay retainer (taban; gerçekte $25-60k build, $3-8k retainer). $2-10M ciro şirketler (komitesiz $30k onayı).
- **Audit'i outreach'te önden satma**: kimse audit istemez. Outreach = sektöre özel tek acı + tek çözüm + sonuç ("account manager'ların haftada 12 saat manuel raporlama", "diş kliniğinin no-show kaybı"). Audit, discovery sonrası strateji görüşmesinde teklif edilir.
- **Huni**: 12 build ÷ %60 = 20 audit ÷ %40 = 50 discovery call/yıl = **haftada 1 nitelikli görüşme**. Tüm iş bu.
- **Fazlar**: 1-14 kurulum (tek sektör ≥90 gün, hook offer, kanal seçimi: ajans/e-ticaret → LinkedIn/X; sağlık/profesyonel → cold email + domain warmup 14 gün; 1 vaka çalışması — yoksa tanıdığa ücretsiz yap) → 15-45 kanallar açık (LinkedIn 30 bağlantı/gün veya 20-30 email/gün 4 adımlı; günde 1 post ×6; 20-30 dk/gün) → 46-90 ilk audit'ler ($3k, 2 haftada: süreç envanteri, darboğaz haritası, ROI sıralı fırsatlar, mimari, fazlı plan) → 91-135 ilk build (8-10 hafta, Slack'te günlük demo; **outreach'i teslimat sırasında durdurma**) → 136-180 retainer'lar birikir.
- **Discovery call tek işi**: strateji görüşmesini kazanmak ("bunu düşünüp haftaya 45 dk'da ne yapacağımı anlatayım" → takvimi orada aç). Strateji call'da tam sistemi scope'lama ($3k kararı $33k'ya dönüşür, kapanış yarıya düşer).
- **Parçalar**: ilk build bileşenlere ayrılır (intake flow, data layer, rapor agent'ı, dashboard shell) → aynı nişte 2. build %80 hazır. **Tek sektör seçmenin nedeni bu.**
- 6 ölüm nedeni: 2. ayda bırakmak, audit'le outreach, erken çok teklif, ödeyemeyecek şirkete satış (<$500k ARR), teslimatta outreach'i durdurmak, call'ı sonraki call kitlenmeden bitirmek.
- Kendi sayını hesapla: builds = (hedef − audit geliri − retainer) ÷ build fiyatı; audits = builds ÷ oran; calls = audits ÷ oran; haftalık = calls ÷ 50.

## 3. Yerel işletmeye satış (Corey Ganim, 4 article + 2 tweet)
- **Takipçisiz 7 müşteri bulma yolu**: yerel AI meetup (ücretsiz mekan, 20 dk sunum, email topla; 7 lead), **bu hafta 10 işletmenin kapısını çal** ("AI ile haftada 10+ saat kazandırıyorum, ücretsiz 15 dk audit?"), LinkedIn DM soru ("gününün en can sıkıcı kısmı ne?" 20/gün), tanıdıklara ücretsiz audit (ilk 3 müşteri buradan), ajans/danışmanlarla referans payı, coworking'de haftalık "AI office hours", 90 gün 3-5 post/hafta.
- **3 model**: tek dikey AI pazarlama ajansı (diş hekimi / med spa / HVAC; Claude Code ile quiz funnel, email dizisi, SEO içerik; $2-5k/ay; 1 kişi 20 müşteri $1M/yıl), skill paketi satışı ($97-297), programatik SEO lead-gen sitesi ($50-200/lead).
- **Speed-to-lead agent** (2 saatte kur, $1.5k'ya sattı): form → saniyeler içinde SMS/email/arama, niteleyici soru, randevu/teklif, sahibe özet. Veri: ilk cevaplayan %78 kazanır; 5 dk'da cevap 30 dk'ya göre 21x. Make.com + Twilio ($0.0079/SMS) + OpenAI + Calendly; $20-50/ay işletim. **Fiyat**: setup $1.5-5k + $300-1k/ay (saatlik asla). Müşteri: yerel servis (çatı, tesisat, HVAC, peyzaj; **formlarına test gönder, 1 saatte dönmezse müşteridir**), emlakçılar, **web ajanslarına white-label** ("siz siteyi yapın, ben AI'ı ekleyeyim").
- **Yönetilen AI çalışan** (Phil Goodwin, 21 kurulum): agent değil **rol** sat (ofis admin, EA, araştırma, raporlama, dosya yönetimi). Login verip bırakma → adoption yok. **Telegram grubu**: müşteri + agent + operatör (gözlemlenebilir koçluk). Haftalık **değer defteri**: her görev, normal süresi, $ karşılığı; müşteriyle baseline anlaş. Fiyat evrimi: $500-2k setup → $250/ay pilot → $1.5k setup + $500/ay → hedef $2k + $1k/ay/agent. Şeffaf pilot pitch'i: "Yönetilen agent servisi test ediyorum, kur + workflow + düşük erken fiyat." Önemli çıktıları Excel/Sheets gibi okunabilir yere yazdır.
- 10 niş agent ($2-5k/ay): teklif üretimi (müteahhit; fotoğraf+ölçü → kalemli PDF), vendor yönetimi (etkinlik), SaaS onboarding, içerik yeniden kullanım (podcast → 20 post), garanti talebi (oto), mortgage ön-nitelik…

## 4. Yerel işletme web sitesi — olmazsa olmazlar (The SEO Guy)
Kaynak: https://x.com/theseoguy_/status/2080670105931792408 — "Lovable ile öğleden sonra site çıkar; çatıcı/dişçi $10k ödemez. Ama var olan site ile para kazandıran site farklı."
**Dönüşüm**: her sayfada sağ üstte tıklanabilir telefon · fold üstünde 3 alanlı form (ad, telefon, hizmet) · gerçek fotoğraflar (stok sahte kokar) · ana sayfada yorumlar · tek net aksiyon.
**SEO**: H1'de ana keyword + şehir ("roofing company tampa", "welcome" değil) · ana sayfada 500+ kelime gerçek metin (hizmet + şehir) · **her hizmet için sayfa** · **her şehir için sayfa** · NAP (ad-adres-telefon) Google Business Profile ile birebir.
**Lead**: form anında telefona · **cevapsız arama → otomatik SMS** (%60 sesli mesaj bırakmaz).
→ Bu liste fabrika web hattının "yerel işletme" şablonunun zorunlu kontrol listesi.

## 5. Fiyat gerçekliği (web)
- tonny (TR): ajanslar pazarlama sitesi için $1-2k, 3 hafta, 2 revizyon; bakım $150/ay. Claude Code ile 1. site 6 saat ($1k gibi görünür), 2. site 3 saat ($2k gibi), 3. site 2 saatlik pipeline + portföy. Yayın Cloudflare Pages = $0/ay.
- Gipp: Çinli girişimci landing başına $1.800 (tasarım $1.200 + frontend $500 + düzeltme $150) → Claude Code ile 4 saat, <$70, "$5k ajans işi gibi". Edge: tek kişi brief-eleştiri-cila-yayın aynı öğleden sonra.
- Prajwal: Stitch → Lovable ile "designer'lar $3k alırdı, artık bedava" (30 dk tam UI).
- Luke Pierce: audit $3k / build $30k / retainer $3k — bu AI otomasyon için; web/app ajansında aynı yapı küçültülerek (audit → build → bakım) uygulanabilir.
- Tibo / Mission Control: yerel işletmeye kurulum, müşteri başı $200-500/ay, 20 müşteri ≈ $10k.
- Dentist voice agent (n8n + ElevenLabs): $24k/yıl satıldı; "kaçan aramadan ayda $6k kayıp" hikâyesiyle.
- Rork CEO: ajanslar düşük kaliteli app için $40k ister — rakip algısı.

## 6. Cold outreach ilkeleri (Dimitar Angelov, article)
Hacim önce (1000×%2 > 100×%4) · en öngörülebilir kanal (lineer matematik) · **teklif > copy** ("düz cümleyle söylesem ister mi?") · kayıptan kaçınma ("rakiplerin gerisinde kalıyorsun") · pattern interrupt · 3-5 touch (mere exposure) · karşılıklılık = spesifik içgörü · merak boşluğu ("outbound'unda bir şey fark ettim") · önce mikro-taahhüt ("bu sana uygun mu?") · **3 paragraf** (kimsin / neden ilgili / ne istiyorsun) · **4-6 cümle**, telefon ekranına sığsın · assumptive dil ("bu saatlerden hangisi uyar?") · follow-up **24-48 saat** · 3 touch (hatırlatma → doğrudan talep → "gelecek ay mı?") · toplantıların %70-80'i follow-up'tan · 4-5 touch sonra bırak, taze liste · ICP çok spesifik; batch'le (unvan, sektör, boy, acı).
Umar: cold outreach sadece başlangıçta; sonra referans + kişisel marka inbound.

## 7. Reklam hattı (Meta/Google)
- **Mike Futia Claude Code plugin** (5 skill): `/spy` (rakibin aktif reklamları, çalışma süresine göre = kanıtlanmış), `/competitors-extractor` (3-5 marka, boş açılar), `/bulk-creative` (20 varyasyon marka sesiyle), `/ad-score` (0-100, 6 boyut, harcamadan önce), `/ad-matter` (Meta MCP ile canlı hesap audit + haftalık fix listesi). Playbook'u DM ile dağıtıyor.
- **Zack (paid ads, $1.2M/ay yönetiyor)**: Claude + **Meta Ads MCP** (okuma/yazma), Higgsfield MCP (statik + Seedance video); 90 gün veri → yorgun açılar, düşük harcamada fırsat, test listesi 3 dk'da. Genç operatörler ayda 40+ kreatif test ediyor (eskiler 8-12). Blueprint: Claude instructions file (creative strategist), ICP için Reddit/Quora dil madenciliği prompt'u, rakip kreatif tersine mühendislik, hook/primary text/reels/UGC script prompt'u, native UGC > cilalı marka, statik görsel sistemi, Seedance 30 sn prompt çerçevesi, YouTube→Meta yeniden kullanım, Meta API'ye bağlı otonom izleme skill'i.
- Google Ads: AI Max için "AI Brief" (marka/mesaj kılavuzu yaz), LSA'lar P-Max'e taşınıyor (yerel işletme reklamında değişim).
- **AI UGC workflow** (Eclipse, $1.9M): ürün fotoğrafı + script prompt → 10 açı → AIDA script; Pinterest'ten yüz → %20 değiştir → Kling 3.0 turbo 5 klip; TikTok'tan b-roll; CapCut (her cümlede sahne değişsin, auto caption). Günde 5-10 reklam > mükemmeliyetçi.
- Kritarth: Nano Banana karakter (kusur = gerçekçilik; iPhone ön kamera ışığı) → viral klibi Arcads ile karakter swap.

## 8. Sosyal medya içerik hattı
- Nano Banana Pro tek prompt'la **9'lu tutarlı Instagram feed** (prompts.md #11).
- Carousel en az kullanılan, algoritmanın en çok ödüllendirdiği format; Claude ile haftada 30+ carousel kurulduktan sonra 2 saatte (maverickecom).
- Pomelli (Google Labs): KOBİ için web sitesinden marka çıkarıp içerik; Veo 3.1 animate, photoshoot.
- Veo Automation (Chrome ext.): Google Flow'da toplu Veo/Nano Banana üretimi.
- Anthropic canvas-design skill: carousel/quote card/infografik → gerçek PNG/PDF; brand-guidelines skill: markayı skill'e kodla.
- Remotion: React ile ürün demo/short-form video.
- AmirMushich: 10 font preset'i ile sosyal grafiklerde tutarlı tipografi (LTX Elements).
- Ege Beşe'nin pipeline'ı (TR): Claude Desktop skill'i viral template'leri topluyor → JSON → Claude Code skill'i DB'ye yazıp thumbnail üretiyor → admin'de taslak onayı. Aynı desen müşteri içerik takvimi için kullanılabilir.
- Coreyhaines31/marketingskills (resmi plugin marketplace'te): ads, ad-creative, copywriting, cro, emails, sms, social, video, image, seo-audit, programmatic-seo, schema, launch, pricing, offers, cold-email, prospecting, referrals, lead-magnets, popups, paywalls, aso, marketing-plan, marketing-council (sanal danışma kurulu), marketing-loops (tekrarlayan otomasyon)… Ajansın pazarlama hattı için tek paket.

## 9. Türkiye bağlamı
- X'te Türkçe "ajans kurdum, böyle müşteri buldum" thread'i **bulunamadı**; Türkçe hesaplar daha çok eğitim (tonny), araç (Kalfa OS, Vibe Kanban), kişisel ürün (Ege Beşe) paylaşıyor. Mevcut TR ajans hesapları (@dijitem, @setup34, @RebirthAjans vb.) klasik hizmet listesi; AI üretim anlatmıyorlar → **boşluk/fırsat**: AI ile üretim sürecini açık anlatan TR ajans içeriği yok.
- tonny'nin TR makalesi (premium web sitesi) fiyat çapası olarak kullanılabilir: "$1-2k / 3 hafta" → "bir öğleden sonra".
- Yiğit (JarvisAgency UK): ajans sitesini Cursor+Claude ile yeniledi, yayında.

## Bizim için çıkarımlar
1. **Teklif**: tek dikey seç (ör. klinik/diş veya restoran), paket = yerel SEO sitesi (madde 4 checklist) + speed-to-lead/cevapsız arama SMS + Google Business + aylık içerik (9'lu feed + carousel) + reklam yönetimi; **setup + retainer**. Mobil app üst paket.
2. **Fiyat yapısı**: küçük ücretli audit (site/GBP/reklam hesabı taraması, Claude ile 1 saatte) → build → aylık bakım/içerik/reklam retainer'ı. Değer defteri (lead sayısı, cevap süresi, arama) her ay rapor.
3. **Müşteri bulma ilk 30 gün**: 10 kapı + formlarını test et (1 saatte dönmeyen = lead), tanıdıklara ücretsiz audit, 1 vaka çalışması için indirimli ilk iş, haftada 3-5 build-in-public post (before/after).
4. **Teslimat pipeline'ı** Ronin'in 4 aşaması; her müşteri işi skill'e dönüşür (sektör şablonu: diş kliniği sitesi skill'i, restoran skill'i…).
5. Reklam hattı için Futia'nın 5 skill mimarisi + Meta Ads MCP + marketingskills `ads`/`ad-creative`.
