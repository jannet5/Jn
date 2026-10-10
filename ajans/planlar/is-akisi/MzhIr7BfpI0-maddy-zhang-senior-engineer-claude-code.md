# How I use Claude Code (Senior Software Engineer Tips) — Maddy Zhang — 9 dk — https://www.youtube.com/watch?v=MzhIr7BfpI0
> Kaynak notu: Transkript yok (bulut IP engeli). Dayanak: açıklamadaki 10 maddelik "neleri anlatıyorum" listesi + zaman damgaları (`transkriptler/MzhIr7BfpI0.aciklama.txt`); maddelerin mekanik karşılığı Anthropic dokümanından (`kaynaklar/gv0WHhKelSE-anthropic-best-practices-dokumani.md`). Güven: **orta-yüksek** (ne anlattığı kesin, kendi örnek metinleri bilinmiyor).

## Tek paragraf
Eski Google mühendisi Maddy Zhang, 9 dakikada "kıdemli mühendis" Claude Code düzenini anlatıyor: AI'yı arama motoru gibi değil sistem gibi kullan. Sıra: temiz bir CLAUDE.md (1:38), kötü kodu önleyen Plan Mode (2:59), oturumu doğru tutan **`/clear` alışkanlığı**, tekrar eden işler için **özel slash komutları**, AI'nın kendi hatasını yakalaması için **doğrulama döngüsü** (test/build/type check, 4:37), Git worktrees ile **paralel oturumlar** (5:40), karmaşık akışlar için **sub-agent ve skill'ler**, GitHub/Slack gibi araçlar için **MCP** (7:50), pratik ipuçları (8:24).

## Önerdiği iş akışı (adım adım)
1. **CLAUDE.md'yi temiz tut** (1:38): proje bağlamı, komutlar, kurallar; kısa ve güncel.
2. **Plan Mode önce** (2:59): kod yazmadan önce planı gör, düzelt, sonra uygulat → "kötü kod" baştan engellenir.
3. **`/clear` alışkanlığı**: her yeni görevde bağlamı sıfırla; oturum doğruluğunu korur.
4. **Özel slash komutları**: tekrar eden işler (örn. PR hazırlığı, test koşma, review) → `.claude/commands/` veya skill (`/komut`).
5. **Doğrulama döngüsü** (4:37): testler + build + type check'i Claude'un çalıştırabileceği şekilde tanımla; "run tests after implementing"; hata → kök neden → tekrar.
6. **Paralel ajanlar** (5:40): `git worktree` ile her görev ayrı dizin/branch, ayrı Claude oturumu.
7. **Sub-agents + skills**: araştırma/review'ı alt ajana, alan bilgisini skill'e.
8. **MCP** (7:50): GitHub, Slack vb. ile doğrudan etkileşim.

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- Açıklamadan aynen: "The importance of a clean CLAUDE.md project context", "Why Plan Mode prevents AI from writing bad code", "The clear habit that keeps AI sessions accurate", "Building a validation loop (tests, builds, type checks) so AI fixes its own mistakes", "Running multiple AI coding sessions in parallel with Git worktrees".
- Kendi CLAUDE.md/komut metinleri videoda; transkript gelince eklenecek. Doküman karşılığı CLAUDE.md örneği ve `/clear` kuralı kaynak dosyasında aynen.

## Araçlar ve linkler
- Claude Code: CLAUDE.md, Plan Mode (`Shift+Tab`), `/clear`, custom slash commands / skills, sub-agents, Git worktrees, MCP (GitHub, Slack)

## Hatalar / "bunu yapma" uyarıları
- AI'yı "biraz daha iyi arama motoru" gibi kullanmak (0:00).
- Plan olmadan kod yazdırmak → kötü kod.
- Uzun, karışık oturum → `/clear` kullanmamak doğruluğu düşürür.
- Doğrulama döngüsü olmadan AI'nın "bitti" demesine güvenmek.

## Bizim fabrikaya alınacaklar (somut)
1. **Doğrulama döngüsü şablonu** her müşteri repo'suna: `npm run check` = lint + typecheck + test + build tek komut; CLAUDE.md'de "Her değişiklikten sonra `npm run check` çalıştır, geçmeden bitti deme".
2. **Slash komut seti** (skills): `/teslim-hazirla` (check + screenshot + PR), `/review`, `/musteri-raporu` — tekrar eden fabrika adımları komut olur.
3. **Worktree disiplini**: `ajans/sistem/PARALEL.md`: görev başına worktree, isimlendirme `wt/<musteri>-<is>`, ana beyin dağıtır/toplar.
4. **`/clear` kuralı** CLAUDE.md'ye: "Yeni sipariş kalemi = /clear; aynı konuda 2 düzeltmeden sonra /clear + yeni prompt".
5. MCP: GitHub (issue→PR), Slack/Drive (müşteri iletişimi) bağlantıları her hat için standart `.mcp.json`.
