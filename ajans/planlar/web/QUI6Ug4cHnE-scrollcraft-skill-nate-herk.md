# I Built The Ultimate Claude Website Design Skill (steal this) — Nate Herk | AI Automation — 16 dk — https://www.youtube.com/watch?v=QUI6Ug4cHnE

> **Not:** Videonun YouTube'da altyazısı yok (otomatik altyazı da kapalı), transkript çekilemedi. Bu plan videonun açıklaması + skill'in GitHub reposu (`ajans/arastirma/youtube/kaynaklar/QUI6Ug4cHnE-scroll-craft.md`, SKILL.md + referans dosyaları) okunarak yazıldı.

## Tek paragraf: ne yapıyor, sonuç ne
Nate Herk, **Scrollcraft** adlı ücretsiz, açık kaynak Claude Code skill'ini (plugin) tanıtıyor: sıradan bir landing page'i "scroll = zaman çizgisi" mantığıyla premium, scroll-güdümlü bir deneyime çeviriyor; her projeyi aynı şablona kilitlemiyor. Skill önce kullanıcıyla 8 soruluk bir "röportaj" yapıyor (vibe, scroll yolculuğu, enerji eğrisi, his eğrisi + tek zirve anı, imza hareketi, estetik aralığı, tek dünya mı/sahneler mi, mevcut varlıklar), elde olan foto/videoyu kullanıyor ya da kie.ai ile fotogerçekçi görsel/klip üretiyor, tek HTML sayfası yazıyor ve sonunda headless tarayıcıda her scroll pozisyonunda ekran görüntüsü alıp kendi işini doğruluyor (ölü scroll, okunmayan metin, kontrast, takılmış video). Videoda AI Automation Society sitesini bu skill ile canlı yeniden tasarlıyor ve ilk taslağı odaklı bir geri bildirim turuyla iyileştiriyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **scroll-craft** repo: https://github.com/nateherkai/scroll-craft (MIT). Kurulum (Claude Code): `/plugin marketplace add nateherkai/scroll-craft` → `/plugin install nateherk-design` → `/nateherk-design:scroll-craft`. Codex/diğer ajanlar: `plugins/nateherk-design/skills/scroll-craft/` klasörünü `.agents/skills/` içine kopyala.
- İçerik: `SKILL.md` (prosedür), `references/` (uniqueness: 8 sayfa grameri + imza hareketi + fingerprint kapısı; feel: his eğrisi ve zirve; devices: 9 scroll cihazı; taste: tasarım tabanı — spacing/tip/renk/derinlik/hareket; assets: üretim ve encode; verify: doğrulama; hero-depth: katmanlı hero; approved-collection: 10 onaylı site), `engine/scrollcraft.{js,css}` (proje başına değiştirilmeyen motor), `scripts/` (doctor, workspace, kie, encode, serve, shoot).
- Gereksinimler: Node 18+, tam ffmpeg, `playwright-core` + Chrome (doğrulama), `KIE_AI_API_KEY` (sadece görsel üretilecekse; kie.ai = görsel/video API aggregator).
- Örnek siteler: aiautomationsociety.ai (koyu editoryal), nateherk.com (aydınlık "lit-glass" portfolyo), PERKFORM (filmik one-shot ürün sayfası).
- Diğer linkler: Glaido (sesle yazma), Hostinger VPS (Claude Code hosting, kod NATEHERK).

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Plugin'i kur, `node <skill>/scripts/doctor.mjs` (ffmpeg/playwright/API key kontrolü) ve `node <skill>/scripts/workspace.mjs --ensure` (builds/ + FINGERPRINTS.md kayıt defteri).
2. **Step 0 röportaj (8 soru, tek seferde):** 3-5 kelimeyle vibe + 3 referans (film/albüm/dergi; site DEĞİL), scroll yolculuğu bölüm bölüm, enerji eğrisi (nerede sakin nerede yoğun), his eğrisi + hatırlanacak TEK an, "hiçbir sitede olmayan tek şey" (imza hareketi tohumu), premium-minimal'den ne kadar uzak (brutalist/maximalist/playful/retro/dense/editorial), tek kesintisiz dünya mı ayrı sahneler mi, eldeki varlıklar. Cevaplar `builds/<ad>/BRIEF.md`'ye **kullanıcının kelimeleriyle** yazılır.
3. **Step 1 brief:** bu ne/kim için, ziyaretçi sonunda neye inanmalı (tek cümle), tek aksiyon + tek etiket, eldeki varlıklar, art direction (worlds.md). Sonra 4-7 "beat"lik yolculuk (Recognition → Tension → Turn → Substance → Range → Commitment).
4. **Step 2 gramer + kapı + skor:** 8 birbirini dışlayan sayfa gramerinden biri (filmic one-shot, chaptered editorial, live surface, continuous world, typographic poster, gallery, split stage, rhythmic cutlist); tek **imza hareketi**; **fingerprint kapısı** (yeni site önceki her siteden 6 boyutun ≥4'ünde farklı olmalı: gramer, nav, hero, akt şekli, kapanış, imza); his eğrisi → her akta cihaz ataması tablosu (scrub / pin+kinetic / reveal / pan / pointer…). Kurallar: ≥4 cihaz ailesi, aynı cihaz art arda yok, en fazla 2 scrub, tek zirve en geniş alan.
5. **Step 3 varlıklar:** `kie.mjs still "<style preamble>\n\n<scene>" out/01-hero.png --ar 16:9` → `kie.mjs shot "<camera move>" ... --dur 5` → `encode.sh` (scrub için yoğun GOP, mobil varyant). Tek stil ön-metni her prompt'ta aynen tekrar (6 görsel tek çekim gibi görünsün); her görseli kullanmadan önce bak.
6. **Step 4 build:** engine dosyalarını kopyala (asla düzenleme), token tabanlı tasarım tabanı (taste.md: 2 font ailesi max, 45-75ch ölçü, 4px spacing, 6 renk rolü + 1 vurgu, saf siyah yok, derinlik 5 araç: offset gölge, kenar ışığı, scale+blur, overlap, grain).
7. **Step 5 verify:** `shoot.mjs` headless tarayıcıyla her scroll pozisyonunu çeker; ölü scroll, hiç tam opaklığa ulaşmayan metin, kompozit sayfa üzerinde satır satır kontrast, poster'da takılı kalmış klip raporu + contact sheet.
8. Videoda: AIS sitesi için ilk taslak → odaklı geri bildirim turu (tek tur, belirli şikayetler) → final.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Çağırma: `/nateherk-design:scroll-craft` ya da doğal dil: "scrollcraft", "layered hero", "cinematic hero", "scrollytelling", "Apple-style landing page", "this looks like a template".
- Başka ajan için: `Read plugins/nateherk-design/skills/scroll-craft/SKILL.md and use it to build my website. Follow the referenced design and verification workflow.`
- Yaratıcı yetki devri: kullanıcı "use your judgment / I want to get out of your way" derse skill `Self-authored under explicit creative delegation` brief'i yazıp röportajı atlar.
- BRIEF.md zorunlu alanları: 8 konu, his eğrisi (akt başına "duygu → ekranda ne sebep oluyor"), zirve (ziyaretçinin arkadaşına söyleyeceği cümle), "It's the site where ___" cümlesi, bilinçli sessizlikler.
- Skill'in **refuse list**'i: aynı feature-card grid'leri, `01 / 06` sayaçları, "scroll to explore" ipuçları, gradient metin, em dash, uydurma istatistik, sahte dashboard, AI-moru gradient, krem+pirinç "artisan" paleti, soft matte low-poly clay diorama (varsayılan olarak yasak; dünya fotoğrafik olmalı).

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler
- **Çeşitlilik ürünün kendisi**: 4+ cihaz ailesi, hiçbir cihaz art arda iki kez; "beş bölüm aynı davranıyorsa bir bölüm beş kez gösterilmiştir".
- **His eğrisi + tek mühendislik zirvesi** (peak-end kuralı): zirve en büyük varlık bütçesini, önündeki sessizliği ve en geniş scroll alanını alır. Üç zirveli sayfanın zirvesi yoktur.
- **Katmanlı hero zorunlu**: arka plan / özne / ön plan / atmosfer bağımsız hareket eden düzlemler, gerçek alpha cutout, ortak temas noktası, metin düzlemler arasında.
- **Fingerprint kapısı**: kendi kendini tekrar etmeyi makine ile engelliyor (ajans için kritik: 20 müşteri sitesi birbirine benzemesin).
- **Tipografi tabanı**: 2 aile, boyut büyüdükçe tracking sıkılaşır, 45-75ch, satır yüksekliği ölçüyle ters orantılı, açık-koyu üzerinde 3 eksende telafi.
- **Tek stil ön-metni** ile üretilen görseller tek fotoğraf çekimi gibi görünür.
- **Kendi işini doğrulama**: headless tarayıcıda scroll-frame kontrastı ve ölü scroll tespiti; mobil ayrı art-direction.
- Gerçek marka varlıkları önce; üretim ikinci; "nothing" cevabı da geçerli (tam üretilmiş dünya).

## Hatalar ve çözümleri (repo + README'den)
- Stripped ffmpeg → "missing filter" syntax hatası gibi görünür → `doctor.mjs` gerçek build'i bulur, `SCROLLCRAFT_FFMPEG` ile override.
- `playwright-core` yanlış klasörden çözülür → build klasöründe `npm i playwright-core`.
- Tek kesintisiz kamera uçuşu (continuous chain) en pahalı ve kırılgan → sahne kesmelerini cihaz değiştirerek gizle; sadece brief "tek yolculuk" diyorsa chain.
- Video oynatmak için değil scrub için encode edilmeli (yoğun GOP), yoksa kare kare takılır.
- İlk 4 build aynı iskelete düştü (6-7 akt, ~13.7vh) → bu band artık fingerprint boyutu; aynı banda düşme.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Videoda satış anlatımı yok (açıklamada 1M$ AI ajansı playbook'u ve Skool topluluğu linki). Maliyet: skill ücretsiz; kie.ai'de bir still sentler, 5 sn klip biraz daha; "iki klipli altı aktlık sayfa küçük bir harcama".

## Bizim fabrikaya alınacaklar (somut)
1. **scroll-craft'ı premium web paketi için ana skill yap** (restoran/ürün/hizmet markaları hedefi zaten bu). Kurulum komutları yukarıda; `doctor.mjs` fabrika kurulum kontrolüne eklenecek.
2. **8 soruluk röportaj = müşteri brief formu.** Soruları Türkçeleştirip müşteri onboarding anketine koy; cevaplar BRIEF.md'ye aynen.
3. **Fingerprint registry'yi ajans genelinde tut**: her müşteri sitesi öncekilerden 4/6 boyutta farklı olsun (şablon ajansı görünmemek için).
4. **His eğrisi + tek zirve** yaklaşımını tüm site planlarına standart yap.
5. **Refuse list** ve taste.md kuralları → anti-slop kontrol listemize.
6. **Verify adımı** (headless screenshot + kontrast) → teslim öncesi otomatik kalite kapısı; mobil ayrı art-direction zorunlu.
7. Görsel üretimde "tek stil ön-metni" ve "her görseli kullanmadan önce bak" kuralı.
