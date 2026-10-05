# ÖZET — zincire hemen alınacak kaynaklar (GitHub araştırması, 2026-10-05)

6 kategori, 230+ repo/kaynak incelendi (ayrıntı: `index.md` ve `notlar/`). 150+ SKILL.md / DESIGN.md / kural dosyası `kopyalar/` altında birebir duruyor. Bu dosya "ne alalım, neden, nasıl kurulur" sorusuna cevap verir.

## Teşhis: çıktılarımız neden çirkin?

Araştırmanın ortak sonucu: sorun modelin yeteneği değil, **varsayılanları yasaklayan kural dosyasının ve görsel kontrol kapısının olmaması**. Her ciddi kaynak aynı üç şeyi yapıyor:

1. **Yasak listesi** (anti-slop): Inter + mor gradyan + 3 eşit kart + emoji ikon + "Elevate/Seamless" kopyası (web); `Alert.alert`, JS modal, `elevation` gölge, `headerShown:false`, KeyboardAvoidingView (mobil, Expo'nun `native-slop.md`'sindeki 20 belirti).
2. **Brief'ten türetme**: renk/font/yoğunluk/hareket seçimi müşteri brief'inden ve bir **DESIGN.md**'den gelir, modelin ilk refleksinden değil.
3. **Ekran görüntüsüyle öz-denetim**: Playwright/agent-browser ile 3 viewport'ta screenshot → kritik → düzelt döngüsü; teslim öncesi deterministik "tell" dedektörleri.

## Hemen alınacak 8 kaynak

| # | Kaynak | Neden | Kurulum |
|---|---|---|---|
| 1 | **anthropics/skills → frontend-design** (179k★) | Jenerik "AI görünümü"ne karşı resmi, en çok kurulan (956K) skill. Plan → jeneriklik kritiği → kod akışı. Web ve mobilin ortak tasarım dili. | `npx skills add anthropics/skills@frontend-design` (Claude Code'da plugin olarak da var: `/plugin install frontend-design@claude-plugins-official`) |
| 2 | **expo/skills + Expo MCP** (resmi) | Mobil hattın temeli. `expo-design-system/native-slop.md` 20 belirtiyle tam bizim sorunumuzu tarif ediyor; `expo-ui`/`expo-router` ile **gerçek native sheet/menü/picker** (Android ham pop-up biter); `expo-animation` Emil Kowalski ile yazılmış. | `claude plugin install expo@claude-plugins-official` → proje köküne `AGENTS.md`; `claude mcp add --transport http expo https://mcp.expo.dev/mcp` |
| 3 | **vercel-labs/agent-skills** (32k★): `react-native-skills` + `web-design-guidelines` + `react-view-transitions` | Tek AGENTS.md'de 36 RN kuralı (formSheet, zeego menü, Pressable, boxShadow, FlashList) + 100+ web UI kuralı (teslim lint'i). skills.sh'de en çok kurulan mobil skill. | `npx skills add vercel-labs/agent-skills --skill vercel-react-native-skills` ve `npx skills add vercel-labs/agent-skills@web-design-guidelines` |
| 4 | **emilkowalski/skills** (44k★) + **greensock/gsap-skills** (16k★) + **Motion AI Kit** | Animasyon üçlüsü. Emil: `animate` / `animate-expo` / `review-animations` (easing-duration standardı, "ease-in yasak"). GSAP: resmi ScrollTrigger skill'i, tüm pluginler ücretsiz. Motion: `/motion` skill + llms.txt. Bunlarla "hiç animasyon yok" sorunu biter. | `npx skills add emilkowalski/skills` · `npx skills add https://github.com/greensock/gsap-skills` · `npx motion-ai` · paketler: `npm i gsap @gsap/react motion lenis`, mobil: `npx expo install react-native-reanimated react-native-worklets` |
| 5 | **DESIGN.md hattı**: google-labs-code/design.md (spec + lint, 28k★) + VoltAgent/awesome-design-md (73 marka, 120k★) + Meliwat/awesome-ios-design-md (200 mobil app, DESIGN-expo.md) + rohitg00 `break-default-aesthetic` promptu | Her müşteri projesi bir DESIGN.md ile başlar; `npx @google/design.md lint` CI kapısı olur; "X gibi olsun" için getdesign koleksiyonu; mobilde OpenTable/Starbucks/Things 3 gibi hazır DESIGN-expo.md şablonları. | `npm i -g @google/design.md` → `design.md lint DESIGN.md`; `npx getdesign@latest add notion`; Meliwat'tan `DESIGN-expo.md` kopyala; şablon taslağı `notlar/design-md.md` §2'de (13 başlık) |
| 6 | **pbakaus/impeccable** (77k★) + **Nutlope/hallmark** (30k★) | Craft ve kontrol kapısı. impeccable: `/impeccable init` (PRODUCT.md+DESIGN.md), `craft`, `critique`, `polish`, `animate`; **61 deterministik dedektör** hook olarak çalışır. hallmark: 57 slop-test, 21 sayfa makro-yapısı (her site aynı hero+3 kart olmasın), uydurma metrik yasağı. | `npx impeccable install` → `/impeccable init`; hallmark: `skills/hallmark/` → `.claude/skills/` (kopyası `kopyalar/hallmark/`) |
| 7 | **Görsel QA kapısı**: OneRedOak design-review ajanı (3.9k★) + vercel-labs/agent-browser (1M kurulum) + humbleteam/design-review rubriği | "Derlenir ama çirkin görünür" sorununun tek çözümü ajanın kendi çıktısını görmesi. 7 fazlı inceleme (1440/768/375, WCAG, konsol), CLAUDE.md "Quick Visual Check" snippet'i, 0-4 puan + Before/After/Why. Ralph döngüsünde UI story'leri bu kapıdan geçmeden kapanmaz. | `kopyalar/OneRedOak-claude-code-workflows/design-review-agent.md` → `.claude/agents/`; snippet → CLAUDE.md; `npx skills add vercel-labs/agent-browser`; Playwright MCP: `claude mcp add playwright npx @playwright/mcp@latest` |
| 8 | **Omurga: obra/superpowers** (296k★) + **snarktank/ralph** (22k★) (+ spec-kit / gstack referans) | superpowers brainstorm → spec → plan → subagent implement → review akışını ve HARD-GATE'leri verir; Ralph her story'yi taze bağlamla, `progress.txt` hafızasıyla ve zorunlu tarayıcı doğrulamasıyla kapatır. "start" deyince zincirin dönmesi bu ikisiyle olur. | `/plugin install superpowers@claude-plugins-official`; `cp ralph.sh CLAUDE.md scripts/ralph/` → `./scripts/ralph/ralph.sh --tool claude 20`; alternatif: `uv tool install specify-cli` (spec-kit), gstack `./setup` |

**Sosyal medya + reklam hattı için ayrıca** (bkz. `notlar/sosyal-medya-reklam.md` §2): sergebulaev/instagram-skills (caption/carousel planner) + coreyhaines31/marketingskills (`social`, `ad-creative`, `ads`) + carousel render hattı (marcolang kuralları + idrsdev `design-system.json` + Playwright dsf 2.5714 → 1080×1350 JPEG) + oliverames/meta-mcp-server veya resmi Instagram Graph API (container→publish; IG Business + `instagram_content_publish`) + facebook-python-business-sdk (campaign→adset→ad, PAUSED başlat). Türkçe caption skill'i yok → `tr-caption` SKILL.md'yi kendimiz yazacağız.

## Önerilen birleşik zincir (web + mobil)

```
0. KURULUM (tek sefer)   superpowers + frontend-design + expo plugin + vercel/emil/gsap skill'leri + impeccable + design-review ajanı + agent-browser/Playwright MCP
1. BRIEF                 müşteri bilgisi → superpowers brainstorm (+ landing-page-design "Vibe Discovery" 4 soru) → BRIEF.md
2. DESIGN.md             brandmd/anydesign (mevcut site varsa) ya da getdesign/Meliwat şablonu → break-default-aesthetic promptu → DESIGN.md (+ DESIGN-expo.md) → `design.md lint` KAPI-1
3. SPEC + PLAN           superpowers spec/plan (ya da spec-kit); UI story'leri için hallmark makro-yapı seçimi; ui-ux-pro-max sektör satırı (restoran/klinik/saatçi)
4. BUILD (Ralph döngüsü) web: Next.js/Astro + shadcn/Magic UI + Motion/GSAP+Lenis; mobil: Expo Router + @expo/ui formSheet + zeego + sonner-native + Reanimated
                         her story: frontend-design + vercel kuralları + emil animate; placeholder yasak
5. GÖRSEL QA             agent-browser/Playwright screenshot (3 viewport / iOS+Android simülatör) → design-review ajanı → impeccable dedektörleri + hallmark slop-test → ≥3/4 değilse düzelt (max 3 tur) KAPI-2
6. REVIEW + SHIP         superpowers review → web-design-guidelines lint → deploy (Vercel) / EAS build; yybd app-factory mağaza sırası
7. PAZARLAMA             aynı DESIGN.md'den carousel/caption/reklam (sosyal medya hattı)
```

## Lisans ve risk notları

- GPL/AGPL: Im-Fran/landing-skills (GPL-3.0), gitroomhq/postiz (AGPL-3.0) — skill olarak okumak sorun değil, koda gömmek dikkat.
- Ücretli/kısıtlı: Remotion (şirket lisansı), ivangfalco/ads-skills (Commons Clause), jezemm/appfactory-plugin (tescilli, yalnızca ilham), sleek.design (SaaS), Motion+ (MCP docs ücretsiz).
- Expo MCP doküman araması ücretli EAS planı istiyor; yerel `expo-mcp` ücretsiz.
- Instagram yayınlama: yalnızca JPEG + public HTTPS URL, 100 post/24 saat; klinik müşterilerde Meta sağlık reklam politikası.
- "İkinci nesil monokültür" uyarısı (anti-ai-slop, taste-skill): cream + terracotta + Fraunces da artık bir "tell"; anti-slop skill'leri birlikte kullanınca projeler arası ayrışmayı ayrıca zorla.

## Kurulum sırası (ilk gün)

```bash
# Claude Code
/plugin install superpowers@claude-plugins-official
/plugin install frontend-design@claude-plugins-official
claude plugin install expo@claude-plugins-official
npx skills add anthropics/skills@frontend-design
npx skills add vercel-labs/agent-skills --skill vercel-react-native-skills
npx skills add vercel-labs/agent-skills@web-design-guidelines
npx skills add emilkowalski/skills
npx skills add https://github.com/greensock/gsap-skills
npx skills add nextlevelbuilder/ui-ux-pro-max-skill@ui-ux-pro-max
npx skills add vercel-labs/agent-browser
npx impeccable install
npm i -g @google/design.md
claude mcp add --transport http expo https://mcp.expo.dev/mcp
claude mcp add playwright npx @playwright/mcp@latest
# Dosya kopyaları (bu repodan)
cp -r ajans/arastirma/github/kopyalar/hallmark .claude/skills/hallmark
cp ajans/arastirma/github/kopyalar/OneRedOak-claude-code-workflows/design-review-agent.md .claude/agents/
```
