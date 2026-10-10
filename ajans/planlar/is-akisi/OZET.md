# İŞ AKIŞI ÖZETİ — Claude Code ile fabrika zinciri nasıl çalışmalı

Kaynak durumu (2026-10-10): İş akışı + Türkçe grubundaki 54 videonun **hiçbirinin transkripti alınamadı** (bulut IP'si YouTube tarafından engelli; 10+ yol denendi, ayrıntı `arastirma/youtube/index.md` üst notunda). Bu özet 6 öncelikli plana (`gv0WHhKelSE`, `S-sYlFiGFv8`, `eSP7PLTXNy8`, `qm0ZlYezt1Q`, `Xu2SIKz8B58`, `MzhIr7BfpI0`), 53 videonun açıklama/bölüm listelerine ve Anthropic'in resmi dokümanlarına (`arastirma/youtube/kaynaklar/*-dokumani.md`) dayanır. Kalan 25 A/B videonun planı transkriptler başka IP'den çekilince yazılmalı. "Herkesin ortak önerdiği" ifadesi: Anthropic dokümanı + 6 plan + açıklamalarda tekrar eden maddeler (CLAUDE.md, plan mode, doğrulama döngüsü, /clear, worktrees, sub-agents, skills, hooks, MCP, routines — 30+ açıklamanın ortak kelimeleri).

## A. Herkesin ortak olarak önerdiği 10 kural
1. **Bağlam en kıymetli kaynak.** Pencere dolunca kalite düşer. İlgisiz iş → `/clear`; iki başarısız düzeltme → `/clear` + daha iyi prompt; araştırma → subagent; `/compact <odak>`; `/btw` yan sorular için. (Anthropic, Maddy Zhang, Sloth, Burhan "token tasarrufu/bağlam yönetimi".)
2. **Kısa CLAUDE.md, sadece koddan okunamayanlar.** Komutlar, stil farkları, repo adabı, env tuzakları, mimari kararlar. Her satır için "silinse hata olur mu?" testi; şişkin dosya kuralları görünmez kılar. Alan bilgisi → skill. (Anthropic, Maddy, Burhan "proje beyni".)
3. **Explore → Plan → Code → Commit.** Çok dosyalı/bilinmeyen iş plan mode'da başlar (`Shift+Tab`), plan `Ctrl+G` ile düzenlenir, sonra uygulanır. Tek cümlelik diff için plan yok. (Anthropic, Maddy "Plan Mode kötü kodu önler", Sajjaad, Tech With Tim.)
4. **Claude'a doğrulayabileceği bir kontrol ver; kanıt iste.** Test, build, typecheck, lint, ekran görüntüsü karşılaştırma. Kapı sertliği: prompt içinde → `/goal` → Stop hook → doğrulama subagent'ı. "If you can't verify it, don't ship it." (Anthropic, Maddy "validation loop", Burhan "Chrome'da arayüz testi".)
5. **Büyük iş önce spec.** "Interview me with AskUserQuestion → SPEC.md" → **yeni oturumda** uygula. Spec dosya/arayüz adlarını, kapsam dışını ve uçtan uca doğrulama adımını içerir. (Anthropic; spec-driven videolar grubu.)
6. **Spesifik prompt, zengin bağlam.** Dosya (`@`), görsel, URL, hata metni, örnek pattern ("HotDogWidget.php gibi yap"), "fixed" tanımı. Belirsiz prompt sadece keşifte.
7. **Deterministik olmalı olanı hook yap, tavsiye olanı CLAUDE.md'de bırak.** Lint-after-edit, migrations klasörünü kilitleme, teslim öncesi check → `.claude/settings.json` hooks. (Anthropic; Burhan "hook'lar".)
8. **Tekrar eden iş = skill/slash komutu; uzman rol = subagent.** `.claude/skills/<ad>/SKILL.md` (yan etkililer `disable-model-invocation: true`), `.claude/agents/<ad>.md` (kısıtlı tools, model seçimi). Paket → plugin. (Anthropic, Maddy, Burhan, Türkçe skill videoları.)
9. **Paralelleştir ama dosya sahipliğini ayır.** Git worktree başına görev/müşteri; Writer/Reviewer iki oturum; fan-out için `/batch` veya `claude -p` döngüsü; agent teams 3-5 kişi, herkes farklı dosya seti. (Anthropic, Maddy, Burhan "worktrees", agent teams videoları.)
10. **Teslim öncesi bağımsız review ve proaktif otomasyon.** Fresh-context `/code-review` / "review against SPEC.md, report gaps not style"; tekrar eden bakım işleri Routines (`/schedule`) ile gözetimsiz; routine prompt'u self-contained + başarı ölçütü. (Anthropic ekip videosu, Routines videosu, Burhan "Routines: GitHub ve buluta deploy".)

## B. Fabrika zinciri için oturum / ajan yapısı
Hedef: "start" → zincir baştan sona → fıstık gibi ürün. Dokümanlardaki üç seviye (subagent < agent team < workflow/routine) ve 10 kural üzerine:

### B1. Katmanlar
```
[Müşteri brief / sipariş]  →  ANA BEYİN (orkestratör oturumu, kalıcı, /rename "fabrika-<musteri>")
      │  1) brief'i mülakatla SPEC.md'ye çevirir (AskUserQuestion)
      │  2) hatlara görev kartı açar (hedef + başarı koşulu + dosya sahipliği)
      │  3) sonuçları toplar, kalite kapılarından geçirir, müşteriye artifact/rapor
      ▼
HAT OTURUMLARI (temiz bağlam, worktree başına bir tane; teammate ya da ayrı cloud session)
  web-hatti/   app-hatti/   reklam-hatti/   sosyal-hatti/   (her biri .claude/agents/<hat>.md rol tanımı)
      │  içinde: Explore→Plan→Code→Verify→Review→Commit; alt işler için subagent (araştırma, review)
      ▼
KALİTE KAPILARI (deterministik)
  Stop hook: `npm run check` (lint+type+test+build) + Playwright screenshot / Expo build
  /code-review + design-review subagent (fresh context, "gaps not style")
  İnsan onayı: plan (Ctrl+G) ve teslim öncesi önizleme (artifact)
      ▼
ROUTINES (bulut, gözetimsiz)
  gece-saglik (müşteri siteleri), haftalik-icerik (sosyal takvim), aylik-reklam (CSV analiz → varyasyon PR),
  pr-review (pull_request.opened → ajans checklist), musteri-bulma (Apify → teklif → Gmail)
```

### B2. Kurallar
- **Ana beyin kod yazmaz.** Spec yazar, dağıtır, toplar, karar verir. Kod yazmaya başladıysa "Wait for your teammates to complete their tasks before proceeding".
- **Bir hat oturumu = bir worktree = bir dosya seti.** İki hat aynı dosyaya dokunmaz; ortak şeyler (DESIGN.md, marka varlıkları) ana beyin tarafından önce üretilir, hatlar okur.
- **Her görev kartı 5 alan**: Görev · Girdi (spec bölümü, dosyalar, referanslar) · Başarı koşulu (ölçülebilir) · Çıktı yeri (branch/PR formatı) · Yapma listesi. Aynı şablon routine prompt'larında.
- **Araştırma ve review asla ana bağlamda değil**: subagent (Explore, security-reviewer, design-reviewer).
- **Seviye seçimi**: tek hat, sıralı iş → tek oturum + subagent; 2-4 bağımsız hat → agent team (`CLAUDE_CODE_EXPERIMENTAL_AGENT_TEAMS=1`, 3-5 teammate) veya ayrı cloud oturumları + cross-session messaging; 10+ aynı tip iş (10 restoran sitesi) → workflow/`/batch`/`claude -p` fan-out; zaman/olay tetikli → routine.
- **Oturum hijyeni**: sipariş kalemi değişince `/clear`; iki düzeltme sonra yeni prompt; uzun oturumlar `/rename` ile isimli ve `--resume` edilebilir.
- **Harness diyeti**: her model sürümünde skill/hook/agent envanterini gözden geçir, modelin zaten yaptığını sil.
- **Kanıt zorunlu**: her teslim PR'ında test çıktısı + ekran görüntüleri + Lighthouse/benzeri skor; "bitti" kelimesi kanıtsız geçersiz.

## C. CLAUDE.md şablonu taslağı (müşteri repo'su, ≤ 60 satır)
```markdown
# <Müşteri> — <web|app> projesi

## Komutlar
- Kurulum: `pnpm i` · Geliştirme: `pnpm dev` · Tek test: `pnpm test <dosya>`
- IMPORTANT: Her değişiklik serisinden sonra `pnpm check` (lint+typecheck+test+build) çalıştır; geçmeden "bitti" deme, çıktıyı göster.
- Görsel doğrulama: `pnpm shot` (Playwright, 3 viewport) → `artifacts/shots/`; tasarım değişikliğinde referansla karşılaştır, farkları listele.

## İş akışı
- Çok dosyalı iş: plan mode'da başla, planı onaya sun; tek cümlelik düzeltmede plan yok.
- Yeni sipariş kalemi = `/clear`. Aynı sorunda 2 düzeltmeden sonra oturumu sıfırla, öğrenilenle yeni prompt.
- Araştırma ve review'ı subagent'a ver (Explore, design-reviewer, security-reviewer); ana bağlamı implementasyona ayır.
- Teslim öncesi: `/code-review` + `/teslim-hazirla` (check + shot + PR). PR'da kanıt: test çıktısı, ekran görüntüleri.
- Compaction'da değişen dosya listesini ve test komutlarını koru.

## Kod ve tasarım kuralları (varsayılandan farklı olanlar)
- Stack: Next.js 15 / Expo SDK 52 (hangisi ise); ES modules; TypeScript strict.
- Tasarım kaynağı: `DESIGN.md` (renk, tipografi, spacing, hareket). Ondan sapma yok; sapma gerekirse önce DESIGN.md'yi güncelle.
- Marka varlıkları `public/brand/`; müşteri metinleri `content/`; dışarıdan metin uydurma yok (eksikse TODO + soru).
- Erişilebilirlik: WCAG AA kontrast, klavye odak halkası, alt metin zorunlu.

## Repo adabı
- Branch: `feat/<kalem>`, `fix/<kalem>`; Türkçe commit mesajı; PR açıklamasında başarı koşulu + kanıt.
- `migrations/`, `.env*`, `public/brand/` dosyalarını hook izinsiz yazdırmaz.
- `_tmp/`, `artifacts/shots/` commit edilmez.

## Tuzaklar
- <projeye özgü: örn. "Expo'da reanimated sürümü 3.x sabit", "Vercel env NEXT_PUBLIC_ öneki">

@DESIGN.md
@docs/SPEC.md
```
Eşlik eden dosyalar: `.claude/settings.json` (Stop hook → `pnpm check`; PostToolUse Edit → lint; migrations/brand yazma engeli), `.claude/skills/` (`teslim-hazirla`, `design-review`, `brief-to-spec`, `ads-variants`, `social-calendar`), `.claude/agents/` (`design-reviewer`, `security-reviewer`, `web-hatti`, `app-hatti`, `reklam-hatti`, `sosyal-hatti`), `.mcp.json` (GitHub, Drive/Notion, Slack).

## D. Sonraki adımlar
1. Transkriptleri başka IP'li makineden çek (`yt_transcript.py`), 6 planı "(tahmin)" yerlerinden arındır, kalan 25 A/B planını yaz.
2. Burhan'ın Gumroad dosya paketindeki CLAUDE.md/skill örneklerini `kaynaklar/`a al, C bölümüyle karşılaştır.
3. `ajans/sistem/` içinde B1 yapısını dosyalara dök: `CLAUDE.md` (şablon C), `settings.json`, skill ve agent iskeletleri, `routines/*.md`.
