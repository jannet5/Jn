# Claude Code Full Course 2026 | How Senior Engineers Actually Build with AI — JavaScript Mastery — 133 dk — https://www.youtube.com/watch?v=u2QqWkMv3Lg

## Tek paragraf: ne yapıyor, sonuç ne
**Bu video mobil değil.** Genel bir Claude Code iş akışı kursu: Expo/React Native yok, ürün bir Next.js web e-ticaret sitesi ("Atelier Store", Gucci'den esinlenen lüks vitrin; TypeScript + Tailwind + Better Auth + Drizzle ORM + Postgres/Neon + Stripe). Her Claude Code özelliği, ihtiyaç doğduğu anda tanıtılıyor: CLAUDE.md/AGENTS.md (kalıcı proje bağlamı), `.claude/settings.json` permissions (allow/ask/deny, `.env` okumayı engelleme), plan mode (DB şeması gibi geri alınması pahalı kararlar), skill'ler (`.claude/skills/build-ui/SKILL.md` ile tekrar eden UI süreci), üçüncü taraf skill/plugin'ler (Better Auth skills, Stripe plugin) ve subagent'larla paralel araştırma (5 explore agent, admin paneli öncesi). Ortaya çıkan sonuç: tek prompt'tan editoryal tasarım sistemi + ana sayfa + ürün detay sayfası, gerçek DB, Stripe sandbox checkout'u çalışan bir mağaza; auth/cart/checkout ve admin tarafı "ödev" olarak bırakılıyor (prompt'lar video kit'inde). Bizim için değeri kod değil, **süreç**: tasarım referansından global tasarım sistemi kurdurmak, somut tasarım kurallarını CLAUDE.md'ye yazmak, UI işini bir skill'e çevirmek, ikinci bir göz (CodeRabbit) ile güvenlik taraması.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
Açıklamadaki linkler (aynen):
- CodeRabbit: https://jsm.dev/claude-coderabbit (indirim kodu: `JSMCR`, ilk 50 kişi)
- Claude Code Guide (cheat sheet): https://jsm.dev/claude-guide
- Video Kit (Prompts, Skills — ödev prompt'ları burada): https://jsm.dev/claude-kit
- Agentic Engineering Course: https://jsm.dev/ai

Videoda geçenler:
- **Claude Code** (terminal + VS Code eklentisi; `claude --version`, `claude`, `claude "..."`, `claude -p`, `claude -c`, `claude -r`)
- **Whisper Flow** — prompt'ları sesle dikte etmek için [00:41:13]
- **front-end design skill** (yazarın yüklü skill'i; tasarım işinde otomatik tetikleniyor) [00:42:53], [01:53:27]
- **Next.js 16**, TypeScript, Tailwind, **Better Auth**, **Drizzle ORM**, **Postgres via Neon** (neon.tech, ücretsiz), **Stripe** (sandbox, test kartı 4242...)
- **Unsplash** görselleri (örnek ürün datası)
- Markdown preview VS Code eklentisi (CLAUDE.md'yi okumak için)
- **JS Mastery agentic engineering workflow** (açık kaynak skill seti): `scope`, `audit`, `architect`, `develop`, `check`, `test`, `document`, `sync`, `debug`; kurulum: `npx skills@latest add <JS Mastery Pro skills>` (tam repo adı transkriptte net değil; kit/ders linkinden) [01:41:51]
- **Better Auth skills**: `npx skills add better-auth/skills` → `create-auth`, `better-auth-best-practices` (`.agents/skills/` altına kuruluyor) [01:45:15]
- **Stripe official plugin**: `claude plugin install stripe@claude-plugins-official` → `/plugins` ile authenticate, Stripe MCP'ye bağlanır [01:47:38]
- **Anthropic skill-creator** (skill üretip eval çalıştıran skill) [01:40:15]
- **skill doctor** (kullanılmayan, context yiyen skill'leri gösteren özel skill) [01:41:01]
- **CodeRabbit AI Deep Scan** (repo genelinde güvenlik taraması) [01:10:12]
- `openssl rand -base64 32` (BETTER_AUTH_SECRET üretimi) [01:19:02]

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)

### Modül 1 — Foundations [00:04:59]
1. [00:08:00] Agentic loop: gather context → take action → verify. Araç adlarını izle: Read, Glob, Grep, Edit, Write, Bash, WebSearch, WebFetch. Yanlış yöne gidiyorsa **Esc** ile kes.
2. [00:11:14] Kurulum: claude.ai/code sitesinden; doğrulama `claude --version`. VS Code eklentisi önerilir (Cmd+Shift+P → "Claude Code").
3. [00:12:47] Plan: öğrenme için subscription; API billing CI/otomasyon için.
4. [00:13:37] `mkdir atelier-store && cd atelier-store`, VS Code'da aç, `claude` → login → "trust this folder".
5. [00:15:12] Alt bar: model, effort, context yüzdesi, mod. **Shift+Tab** modlar arasında döner. `/model` ile model değiştir.
6. [00:15:59] İlk prompt: Next.js iskeleti (aşağıda "Prompt 1"). ~2,5 dk'da bitti.
7. [00:17:56] Ctrl+C iki kez çıkış. `claude "explain how this project is structured"` (oturum açık kalır), `claude -p "..."` (tek seferlik, çıkar), pipe: `tail -n 200 app.log | claude -p "find anything unusual in these logs"` (CI için).
8. [00:19:47] Model seçimi: günlük iş için hızlı model; mimari, zor debug, büyük refactor için güçlü reasoning modeli.
9. [00:20:34] `claude -c` (son oturumu sürdür), `claude -r` (eski oturum seç), `/help`, `/clear` (alakasız yeni işe geçerken).
10. [00:22:14] Kod tabanını açıklat; Claude'un nasıl bulduğunu (glob/grep) izlemek alışkanlık olsun.

### Modül 2 — CLAUDE.md / AGENTS.md [00:24:02]
11. [00:24:50] CLAUDE.md oturum başında otomatik yüklenir. Yeni sürümde CLAUDE.md yoksa **AGENTS.md** okunuyor.
12. [00:25:42] CLAUDE.md yönergedir, engel değildir. Kesin engel → permissions / sandbox / hooks.
13. [00:26:36] Context = attention budget. CLAUDE.md "200 satırın altında" kalsın; 30–40 satır daha iyi.
14. [00:27:24] Context rot: 200K pencerede ~50K token'dan itibaren ölçülebilir düşüş. Yeni işe geçince clear.
15. [00:28:16] Seviyeler: `~/.claude/CLAUDE.md` (kişisel, tüm projeler), proje kökü `CLAUDE.md` (git'e commit), `CLAUDE.local.md` (sadece sana, git dışı), monorepo'da iç içe CLAUDE.md'ler.
16. [00:31:21] Auto memory: proje başına memory klasörü, `MEMORY.md`. Görmek için `/memory`; context dağılımı için `/context`, detay `/context all`.
17. [00:32:11] Kural: "Bu satırı silersem Claude yanlış bir şey varsayar mı?" Evet ise tut.
18. [00:33:01] İyi örnekler: package manager pnpm, `pnpm dev`, test/typecheck komutları; "use Drizzle for all database access", şema `src/db/schema`, "never write raw SQL unless requested"; storefront: "editorial ve product-focused", "reuse existing design system", "avoid dashboard-style UI", "no purple gradients".
19. [00:33:49] Kötü örnekler: "write high quality code", "make the UI beautiful", "make landing page premium". Yerine: "Use large product imagery, generous spacing, and restrained controls on storefront pages."
20. [00:34:43] Spesifik ol: "use two-space indentation", "run pnpm test before finishing", "API handlers live in src/api/handlers".
21. [00:35:33] Büyüyünce `.claude/rules/` → `testing.md`, `database.md`, `storefront.md`, `security.md`; path'e özel kurallar (sadece eşleşen dosyada yüklenir).
22. [00:35:33] `/init` → CLAUDE.md üretir; mevcut varsa değişiklik önerir. **Okumadan kabul etme**; package.json'dan çıkarılabilecek şeyleri sil, kodda olmayanı (marka hissi, "dashboard UI istemiyorum") ekle.
23. [00:37:59] CLAUDE.md'yi canlı tut; eski kuralları sil. JSM workflow'undaki `sync` skill'i son git commit'lerine bakıp CLAUDE.md/AGENTS.md'yi günceller.

### Modül 3 — Permissions [00:39:21]
24. [00:39:37] Modlar: manual (onay ister), edit automatically (accept edits), plan (salt okunur), auto (kendi onaylar, isteğe uygunluğu kontrol eder). Yazar normalde auto kullanıyor; ilk özellik için manual'a geçti.
25. [00:41:13] İlk özellik: Gucci sitesini görsel referans alıp **global tasarım sistemi** kurdurmak (Prompt 2). WebFetch/WebSearch + front-end design skill kullandı; manual modda her fetch için onay istedi.
26. [00:43:42] Kural tipleri: allow / ask / deny; değerlendirme sırası **deny → ask → allow**. `/permissions` ile görüntüle (auto modda soft allow/soft deny).
27. [00:45:24] Sonuç `src/app/globals.css` içinde: renkler, fontlar, başlıklar, butonlar.
28. [00:46:16] `.claude/settings.json` oluştur: allow → `pnpm build`, `pnpm test`, `git status` vb.; ask → `git push`; deny → `.env` dosyalarını okumak (içerik ekranda okunmadı, yapı aşağıda "Prompt'lar" bölümünde rekonstrükte).
29. [00:48:08] Test: `.env.example`'ı `.env` olarak kopyala, Claude'a "read the .env" de → "Reading that file is blocked by this session's permission settings... I didn't try to route around it with a cat" cevabı.
30. [00:49:04] Ana sayfa prompt'u (Prompt 3). Auto moda geçti (Shift+Tab).
31. [00:50:38] `pnpm dev` → localhost:3000: "Complimentary delivery and returns", hero'da uzun palto, koleksiyon şeridi (printed silk, outerwear, leather goods).
32. [00:52:16] Ürün detay sayfası (Prompt 4). Build geçti, 13 ürün sayfası statik pre-render, lint temiz, context'in %21'i harcandı. Sol görseller kayarken sağda sticky ürün bilgisi, altta önerilen ürünler.
33. [00:53:56] Felsefe: işi küçük özelliklere böl, birini yaptır, review et, kabul/geri al, sonrakine geç.

### Modül 4 — Plan Mode [00:56:23]
34. [00:56:27] Yeni işe `/clear`; `/plan` veya Shift+Tab ile plan mode. DB planı prompt'u (Prompt 5).
35. [00:58:05] Claude bir **Explore subagent** başlattı (storefront'un ürün datasını nasıl kullandığını araştırdı); subagent'a Enter ile girip izlenebiliyor.
36. [00:58:46] Claude soru sordu: stok → quantity + derived state; çoklu görsel → ayrı `product_images` tablosu; koleksiyon şeridi → kategori; ana sayfa listeleri → `created_at` sırası. Hepsi önerilen seçenekle kabul.
37. [00:59:32] Planda kontrol et: tablolar, ilişkiler, stok nerede, fiyat nasıl tutuluyor, Drizzle şeması nerede, hangi dosyalar değişiyor, nasıl verify edilecek. Zayıf plan: "add database, add products, update storefront".
38. [01:00:22] Örnek müdahale: plan "products, product_variants, inventory_locations..." derse → "Keep stock directly on the product and only create what we currently need." En ucuz düzeltme yeri plan.
39. [01:01:09] **Ctrl+G** planı markdown editörde açıp doğrudan düzenler; 5–10 dk oku. Onay plan mode'dan çıkarır → "Yes, proceed, use auto mode".
40. [01:01:54] Uygulama sonrası: "update the CLAUDE.md with only the database conventions from this decision..." (Prompt 6) → seed komutu, şema yeri vs. eklendi.
41. [01:03:27] Claude: Drizzle config, 3 tablo + şema, DB bağlantısı, migration üretimi, seed datası, ürün sorgularının DB'ye bağlanması.
42. [01:04:16] Neon: proje "Atelier Store", yakın region, Connect → connection string'i **`.env.local`**'a `DATABASE_URL` olarak yapıştır; secret'ı Claude'a asla verme.
43. [01:05:10] "Run the migration and seed now that the database URL is set." → `DATABASE_URL` shell'e ulaşmadı (hata, aşağıda) → düzeltildi, migration temiz uygulandı; Neon tablolarında categories/products/product_images doğrulandı.
44. [01:06:51] "Show me the important changes you made and explain anything that I should review." veya `git diff`. Önemli: stok sayı oldu, durum türetiliyor; tek bir mapper DB ile UI arasındaki sınır ("every query returns the product data, never a row").
45. [01:08:29] Claude sapıyorsa tekrar plan mode'a dön (replan).

### Güvenlik ara bölüm (CodeRabbit sponsorlu) [01:08:29]
46. [01:10:12] Aynı modelin kendi yazdığını denetlemesi yetmez → bağımsız ikinci göz.
47. [01:11:05] github.com/new → `atelier-store` repo; Claude'a "commit and push all the existing code to this GitHub repo" (Claude `.env`'in push'a dahil olmadığını kontrol etti; `git push` ask kuralı yüzünden onay istedi).
48. [01:11:56] CodeRabbit'e GitHub ile giriş, repo'yu yetkilendir → Security → AI Deep Scan → Scan. ~10 dk.
49. [01:14:13] Bulgular: 1 medium, 2 low, sızan secret yok. Her bulgu için: gerçek mi, ilgili mi, düzeltilmeli mi?
50. [01:15:02] Medium — zayıf `BETTER_AUTH_SECRET`: kontrol yok → token forge → hesap ele geçirme. Önce sadece araştırt (Prompt 7). Claude Better Auth kaynağını izledi: secret boş olduğu için **npm paketindeki public default secret** kullanılıyor → daha kötü. Fix: unset/default/<32 karakter/tekrar eden dolgu reddediliyor. `openssl rand -base64 32` ile secret üretildi.
51. [01:19:59] Low — newsletter formu `action="#"`, method yok → GET → e-postalar URL'e, tarayıcı geçmişine, server log'larına sızıyor. "Share for agent" → yapıştır → "Fix this."
52. [01:20:46] Low — signup user enumeration (mevcut e-posta farklı hata döndürüyor). Tam çözüm e-posta doğrulaması = email sender gerek → scope creep; **not alınıp sonraya bırakıldı** (sessizce yok sayılmadı).
53. [01:22:24] "commit and push the changes".
54. [01:23:12] `/compact` (aynı işe devam, daha küçük context) vs `/clear` (yeni iş). Yazar çoğunlukla clear kullanıyor.
55. [01:24:01] Plan mode'u her şeye kullanma: başlık/spacing değişikliği için gereksiz; DB, auth, migration, büyük refactor için evet.
56. [01:24:49] Belirsiz istekler ("build proper inventory management") için: `scope` skill (fikri challenge eder, `docs/scope` canlı belge) → `architect` (teknik kararlar) → plan mode (mevcut koda nasıl uygulanır).

### Modül 5 — Skills [01:27:44]
57. [01:28:15] Skill = `SKILL.md` içeren klasör; `/build-ui` ile elle veya açıklama eşleşince otomatik. CLAUDE.md her oturumda yüklenir, skill sadece gerektiğinde.
58. [01:29:10] Ayrım: proje gerçeği → CLAUDE.md; tekrar eden süreç → skill; tek seferlik iş → doğrudan sor.
59. [01:30:05] Oluştur: `.claude/skills/build-ui/SKILL.md` (klasör adı = komut). Global skill'ler `~/.claude/skills/`.
60. [01:30:58] Frontmatter: `name`, `description` (ne yapar + ne zaman), gövde `$ARGUMENTS` alır, adım listesi (aşağıda tam metin). Büyük referanslar ayrı dosyaya.
61. [01:32:45] **Description en önemli kısım**: skill'in tetiklenmemesinin ana sebebi zayıf description.
62. [01:33:36] `/build-ui build a new arrivals page...` (Prompt 8) → ~2 dk; server component, breadcrumb, hairline rule, empty state; yeni renk/class/bileşen yok. Log: "Skill loaded successfully" → 1 pattern search, 3 dosya okuma, 1 dizin listeleme, sonra implementasyon.
63. [01:35:25] `/build-ui add a category collection page` → ~2 dk; eyewear, leather, jewelry kategorileri.
64. [01:38:36] Skill adı/description iyi ise "/build-ui" yazmadan da tetiklenir. Yan etkili skill'ler (deploy, commit) için frontmatter'a `disable-model-invocation: true`.
65. [01:39:29] Progressive disclosure: metadata → SKILL.md → klasördeki referanslar (`references/design-system.md`, script, örnek) gerektiğinde. JSM `scope` skill'inde template, modlar (brownfield, greenfield, plan, replan), yaklaşımlar (facade, journey, skateboard, tracer bullet) ayrı dosyalarda.
66. [01:40:15] skill-creator ile skill yaz + with/without eval çalıştır.
67. [01:41:01] Skill klasörünü çöplüğe çevirme: 10 iyi skill olur, 100 rastgele olmaz. skill doctor ile kullanılmayanı sil.
68. [01:41:51] İndirilen skill'leri oku (script/komut/tool erişimi içerebilir); `npx skills` kurulumda risk etiketi gösteriyor; project veya global kurulum seçilebiliyor.
69. [01:42:35] İlk günden mükemmel skill tasarlama; kullan, hatayı gör, kural/verify adımı/description'ı düzelt.

### Modül 6 — Ship the Store (ödev) [01:43:30]
70. [01:44:22] Eksikler: auth, cart, stok zorlaması, checkout, orders, müşteri hesabı. Prompt'lar sırayla video kit'inde.
71. [01:44:22] UI sayfaları (cart, account overview, order history) → `build-ui` skill'i.
72. [01:45:15] Auth → `npx skills add better-auth/skills` → core skill'ler, Claude Code seçilir → `.agents/skills/create-auth`, `.agents/skills/better-auth-best-practices`. Auth'u **önce plan mode'da** çalıştır.
73. [01:47:38] Checkout → `claude plugin install stripe@claude-plugins-official` → `/plugins` → Stripe → authenticate (test ortamı MCP). Skill = tek süreç; plugin = skill + MCP vb. paket.
74. [01:50:51] Stripe secret key `.env`'e. Sonuç: lüks görünümlü sign-in sayfası, sepet, "sold out" + "email me when it returns", Stripe sandbox checkout (4242 test kartı), thank-you sayfası, stok düşüyor.
75. [01:52:33] Bozuk görünen sayfa düzeltme: ekran görüntüsünü Shift ile sürükle-bırak + "the design seems off. improve the UI of the thank you page. use front-end skills" → design skill yüklendi, ~1 dk'da düzeldi.

### Modül 7 — Subagents & Parallel Work [01:56:04]
76. [01:56:42] Subagent = kendi context'inde odaklı iş yapan, sonucu raporlayan ayrı agent. Değeri **context izolasyonu**, "daha akıllı" değil. "Senior staff backend engineer" gibi persona'lar bilgi katmaz.
77. [01:58:31] Admin paneli öncesi 5 paralel investigation (Prompt 9) → data model, stock, auth/admin, order lifecycle, UI patterns. ~3 dk'da ~15 dk'lık iş; ana model bekleyip birleştirdi.
78. [02:01:55] Built-in agent'lar: Explore (salt okunur araştırma), Plan, general-purpose. Doğrudan iste: "use an explore sub agent to inspect how admin authorization should fit into the current better auth implementation" / "delegate the order life cycle investigation to a separate sub agent and return the important findings". İç araç adı `Agent`.
79. [02:02:46] Admin v1 kapsamı: admin-only access, dashboard overview, ürün/kategori/sipariş yönetimi, stok güncelleme. YOK: çoklu yetki seviyesi, depo, analytics, staff davet, refund, fulfillment.
80. [02:03:40] "commit and push all the work done so far" → ikinci CodeRabbit Deep Scan (Slack bildirimi bağlanabilir).
81. [02:05:13] Admin'i plan mode'da planla; **yetkilendirme server'da** olmalı (admin linkini gizlemek yetkilendirme değil; müşteri admin URL'ini açamamalı / admin action'ı çağıramamalı). Sıra: admin access → product mgmt → inventory → orders.
82. [02:06:48] Sonunda CLAUDE.md/AGENTS.md'nin güncel uygulamayı anlattığını kontrol et → `sync` skill'i.

### Go further [02:10:24]
83. [02:09:07] İleri konular: git worktree + paralel feature agent'ları, MCP, hooks, multi-agent workflow.
84. [02:12:24] Kapanış: iyi context ver, işleri küçük tut, review et, tekrar eden süreci skill yap, ayrılabilir işi subagent'a ver, özgürlüğü yavaş artır.

## Kullandığı prompt'lar (varsa aynen)
Not: Prompt'lar sesli/ekran okumasından transkripte geçtiği gibi; tam metinler video kit'inde (https://jsm.dev/claude-kit). Transkriptteki "Better Off" = Better Auth, "pmppm" = pnpm, "envample" = `.env.example`.

Prompt 1 — iskelet [00:15:59]:
```
Set up a new Next.js e-commerce app in this folder using TypeScript, Tailwind, Better Auth, Drizzle ORM, and Postgres via Neon.

Only create the initial project structure, dependencies, configuration, .env.example and minimal integrations.

Do not build e-commerce features, full auth flows, schemas, UI, payments or deployment.
```

Prompt 2 — referanstan tasarım sistemi [00:41:13]:
```
Study the current Gucci website as a visual reference for this ecommerce application. Analyze its typography, spacing, layout, navigation, product presentation, imagery, buttons, borders, colors, and responsive behavior without copying its text, assets, or branding.

Then implement those principles in our project by setting up the global design system including typography, color tokens, spacing, containers, buttons, links, borders, and reusable responsive primitives in the appropriate Tailwind and CSS files.

Finally, briefly explain the rules you implemented and the files you changed.
```

Prompt 3 — ana sayfa [00:49:04]:
```
Build the first complete homepage for the e-commerce storefront using the design system set up in the project. Take inspiration from the way premium fashion websites like Gucci use large editorial imagery, featured collections, and product focused sections. Use simple sample product and collection data where needed with images from Unsplash or other sources. Make it responsive and verify the result when it's done.
```

Prompt 4 — ürün detay [00:52:16]:
```
Build the product detail page with large imagery, product information, price, category, and stock state. Keep it consistent with the homepage and product listing.
```

Prompt 5 — plan mode, DB [00:56:27]:
```
Inspect the current e-commerce project and plan how to replace the current product data with a real Postgres database using Drizzle ORM. For now we only need products, categories, and stock. Study how the existing storefront currently uses product data before deciding the schema. Keep this first version simple. Don't add carts, orders, payments, reviews, wish lists, or product variants yet. Show me the database structure, relationships, files that need to change, migration approach, and how the existing storefront will start using the database.
```

Prompt 6 — CLAUDE.md güncelleme [01:01:54]:
```
Update the CLAUDE.md with only the database conventions from this decision that Claude should remember in future sessions. Keep it short and don't add things that Claude can easily discover from the codebase.
```

Prompt 7 — güvenlik bulgusunu önce araştırma [01:16:43]:
```
The application accepts any explicitly configured BETTER_AUTH_SECRET while the authentication dependency only warns about short or low entropy values. A guessable shared secret allows attackers to forge authentication related tokens.

Investigate whether this is accurate for our own current Better Auth setup and explain the risk in the context of this application first, without modifying anything yet.
```

`.claude/skills/build-ui/SKILL.md` [01:30:58] (sesli anlatımdan derlendi; adımların sonu kit'te):
```
---
name: build-ui
description: Build or update customer-facing e-commerce UI using the existing design system, components, product patterns, and responsive conventions. Use when creating or significantly changing pages, sections, or customer-facing components.
---

Build or update the following UI: $ARGUMENTS

1. Inspect the current implementation and related components.
2. Search for reusable components before creating new ones.
3. Reuse the existing design system.
4. Keep the page responsive.
5. Avoid unrelated changes.
6. Run the app and verify, type check, and summarize what changed.
```

Prompt 8 — skill ile [01:33:36]:
```
/build-ui Build a new arrivals page using the latest products from the database. Keep it consistent with the homepage, product listing, and product detail pages already in the application.
```
```
/build-ui add a category collection page
```

UI düzeltme (ekran görüntüsü ile) [01:53:27]:
```
The design seems off. Improve the UI of the thank you page. Use front-end skills.
```

Prompt 9 — paralel subagent fan-out [01:58:31]:
```
Before building the admin side of our project, investigate the existing application using separate focused sub agents. Have them independently inspect:
- the products, categories and the current Drizzle data model
- stock and inventory behavior
- authentication and the safest minimal way to add admin access
- orders and the current order life cycle
- existing UI patterns we can reuse

Each sub agent should return the current behavior, relevant files, important constraints, and a recommended approach. Do not implement anything yet. Combine their findings into one concise summary when they're done.
```

`.claude/settings.json` (içerik ekranda okunmadı; anlatılan kurallarla rekonstrükte) [00:46:16]:
```json
{
  "permissions": {
    "allow": ["Bash(pnpm build)", "Bash(pnpm test)", "Bash(pnpm typecheck)", "Bash(pnpm dev)", "Bash(git status)"],
    "ask": ["Bash(git push:*)"],
    "deny": ["Read(./.env)", "Read(./.env.*)", "Read(./secrets/**)"]
  }
}
```

Diğer kısa prompt'lar: "commit and push all the existing code to this GitHub repo", "run the migration and seed now that the database URL is set", "show me the important changes you made and explain anything that I should review", "Fix this.", "commit and push the changes".

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Gerçek bir referans sitesi** verip önce analiz ettirmek (typography, spacing, layout, navigation, product presentation, imagery, buttons, borders, colors, responsive) → sonra bunu **global design system**'e (token'lar, container, primitives) dönüştürtmek. Sayfalar "rastgele" değil, tek bir sistemden türüyor.
- Tasarım sistemi tek yerde: `globals.css` + Tailwind; sonraki tüm sayfalar "use the design system set up in the project" ile bağlanıyor.
- CLAUDE.md'de **somut, ölçülebilir estetik kurallar**: "editorial and product-focused", "avoid dashboard-style UI", "no purple gradients", "large product imagery, generous spacing, restrained controls". "Make it beautiful/premium" gibi boş kurallar yok.
- `build-ui` skill'i: önce mevcut bileşenleri ara, yeniden kullan, yeni renk/class/bileşen icat etme → tutarlılık. Sonuç: "no new colors, classes, or components", hairline rule, breadcrumb, empty state.
- Yüklü **front-end design skill**'i tasarım işinde otomatik tetikleniyor (tasarım kalitesini o taşıyor).
- Ürün detay deseni: solda büyük kayan görseller, sağda sticky bilgi paneli, altta öneriler; stok durumu (sold out, "only 3 left", "email me when it returns") UI'da görünür.
- Görsel geri bildirim döngüsü: ekran görüntüsünü Claude'a sürükle-bırak + "design seems off" → design skill ile düzeltme.
- Animasyon konusunda özel bir teknik **yok** (web projesi; Reanimated/motion anlatılmıyor).

## Hatalar ve çözümleri
- [00:23:01] CLI "not logged in" görünmesi (VS Code eklentisinden login olunca) → `claude`'u yeniden çalıştırınca hesap algılandı.
- [00:42:53] Manual modda her WebFetch/WebSearch/edit için onay yorgunluğu → `.claude/settings.json` allow listesi + auto mode.
- [01:05:10] Migration/seed çalışmadı: "DATABASE_URL isn't reaching my shell" — script `.env` arıyordu, değer `.env.local`'daydı → Claude'a env-dosyası kısmını kaldırıp tekrar denemesi söylendi, bağlandı.
- [01:15:02] `BETTER_AUTH_SECRET` boş → Better Auth npm paketindeki public default secret'a düşüyordu (token forge / hesap ele geçirme) → güçlü secret doğrulaması (unset, default, <32 karakter, tekrar dolgu reddi) + `openssl rand -base64 32`.
- [01:19:59] AI'ın ürettiği placeholder newsletter formu (`action="#"`, method yok) GET ile e-postaları URL/log'a sızdırıyordu → POST/düzgün handler ile düzeltildi.
- [01:20:46] Signup user enumeration → bilerek ertelendi (e-posta doğrulaması gerektiriyor, scope creep), not alındı.
- [01:51:43] Sepet/thank-you sayfası tasarımı zayıf → ekran görüntüsü + front-end skill ile düzeltme.
- Genel: Claude yanlış varsayımı tekrarlıyorsa CLAUDE.md eksik/yanlıştır; sapıyorsa plan mode'a dönüp replan.

## Bizim fabrikaya alınacaklar (somut, maddeli)

**Mobil zincire doğrudan uygulanabilir olanlar (Expo/RN için uyarlanmış):**
1. **Referanstan tasarım sistemi aşaması**: her müşteri uygulamasında ilk UI işi, 1–2 referans uygulama/siteyi (ekran görüntüsü veya URL) analiz ettirip `theme/tokens.ts` (renk, tipografi, spacing, radius, shadow) + temel primitive'ler (Button, Text, Card, Sheet, Input, Screen container) üretmek. Prompt 2'yi Expo'ya çevir: "...implement those principles by setting up the global design system including typography, color tokens, spacing, containers, buttons... in the appropriate theme and component files". Sayfalar ancak bundan sonra.
2. **CLAUDE.md'ye somut estetik/negatif kurallar** (şikâyetimize birebir): "Never use `Alert.alert` or native Android dialogs; use our `<Sheet>`/`<Dialog>` component", "every pressable has press feedback (scale/opacity via Reanimated)", "list/screen enter animations with Reanimated `entering` presets", "no default Material look, no purple gradients, no dashboard-style screens", "generous spacing, large imagery, restrained controls". "Make it beautiful" gibi kural yazma.
3. **`build-screen` skill'i** (`build-ui` kalıbı): `.claude/skills/build-screen/SKILL.md`, `$ARGUMENTS`, adımlar: mevcut ekran/bileşenleri incele → yeniden kullanılabilir bileşen ara → design system'i kullan, yeni renk/stil icat etme → animasyon/geri bildirim ekle → `npx expo start`/typecheck/lint ile doğrula → özetle. Description'ı "Use when creating or significantly changing screens, sections, or user-facing components" şeklinde net yaz. Ağır içerik `references/design-system.md`, `references/animations.md` olarak progressive disclosure.
4. **Ekran görüntüsü geri bildirim döngüsü**: emülatör/cihaz ekran görüntüsünü Claude'a ver + "design seems off, improve the UI of X screen, use front-end/design skills". Fabrikada her ekran için "screenshot → düzelt" turu standart adım olsun.
5. **Yan etkili skill'lerde `disable-model-invocation: true`**: `eas build`, `eas submit`, store yayını, commit/deploy skill'leri yalnızca elle tetiklensin.
6. **Permissions şablonu** (`.claude/settings.json`): allow → `npx expo start`, `npx tsc --noEmit`, `npm run lint`, `npx expo-doctor`, `git status`; ask → `git push`, `eas build`, `eas submit`, `eas update`; deny → `.env*`, `credentials.json`, `*.keystore`, `*.jks`, `*.p8`, `google-services.json` (gerekirse), App Store Connect API anahtarları. Deny'ı ".env oku" testiyle doğrula.
7. **Plan mode kuralı**: veri modeli, auth, ödeme (RevenueCat/Stripe), navigasyon mimarisi, EAS/app config değişiklikleri plan mode'da; buton/spacing değişikliği değil. Planı Ctrl+G ile düzenle; onaydan sonra "update CLAUDE.md with only the conventions from this decision" (Prompt 6) ile kararı kalıcılaştır.
8. **Subagent fan-out** (Prompt 9 kalıbı): büyük özellik öncesi (ör. işletmeye admin/sipariş modülü) 4–5 paralel Explore agent: data model, auth, navigasyon, mevcut UI/animasyon desenleri, yayın config'i — her biri "current behavior, relevant files, constraints, recommended approach" döndürsün, implement etmesin.
9. **Resmi skill/plugin'leri kullan**: kütüphane sahiplerinin skill'leri (`npx skills add better-auth/skills` örneği). Expo/RN/Reanimated/Stripe/RevenueCat için resmi skill/plugin varsa kur (`claude plugin install stripe@claude-plugins-official`), ama kurmadan SKILL.md ve script'leri oku.
10. **Bağımsız güvenlik taraması**: kodu yazan modelden ayrı bir tarayıcı (CodeRabbit Deep Scan veya eşdeğeri) her büyük milestone'dan sonra; bulgu başına "gerçek mi / ilgili mi / şimdi mi" triyajı; ertelenenler not dosyasına. Önce "investigate... without modifying anything yet" ile doğrulat, sonra fix.
11. **Placeholder koddan sızıntı kontrolü**: AI'ın bıraktığı placeholder formlar/handler'lar (newsletter örneği) için yayın öncesi kontrol maddesi; auth secret'ları için güç doğrulaması (`openssl rand -base64 32`).
12. **Context hijyeni**: CLAUDE.md ≤ ~40–100 satır; `.claude/rules/` ile path bazlı kurallar (ör. `rules/animations.md` sadece `components/**`, `rules/release.md` sadece `eas.json`/`app.config.ts`); yeni ekran/iş = `/clear`; `/context` ile ne yer kaplıyor bak; ~50K token sonrası kalite düşüşüne dikkat.
13. **`sync` adımı**: her müşteri teslimi sonrası CLAUDE.md'yi son commit'lere göre güncelle (JSM `sync` skill'i veya kendi eşdeğerimiz); eski kuralları sil.
14. **İşi küçük dilimle**: "tüm uygulamayı tek prompt'ta yap" yerine ekran ekran: üret → review → kabul/geri al → sonraki. İlk iskelet prompt'unda açıkça "do not build features/UI/payments/deployment yet" kapsam sınırı koy (Prompt 1 kalıbı).

**Genel (mobil olmayan, sadece süreç bilgisi):**
- `claude -p` + pipe ile CI'da log/test analizi; `claude -c` / `-r` oturum devamı.
- Belirsiz istekler için scope → architect → plan mode ayrımı (JSM workflow).
- Git worktree ile paralel feature agent'ları, hooks, MCP: videoda sadece anılıyor, gösterilmiyor.
