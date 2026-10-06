# How to Use Claude Skills as a Designer — Griffin Wooldridge — 9 dk — https://www.youtube.com/watch?v=Iup1WlUyj9M

## Tek paragraf: ne yapıyor, sonuç ne
Griffin, tasarımcılar için önemli Claude skill'lerini tek tek anlatıyor: **frontend-design** (Anthropic; kod yazmadan önce amaç/ton/kısıt/farklılaşma düşündürür, Inter/Arial ve "AI slop"u yasaklar), **Implement Design** (Figma'nın resmi skill'i; Figma URL → Figma MCP ile layout/auto-layout/tipografi/renk/spacing token'larını okur, SS + asset indirir, projenin bileşen kütüphanesine map'ler, sonda doğrulama checklist'i), **Theme Factory** (10 hazır renk+font teması; slayt/doküman/web sayfası/dashboard'a uygulanır), **Brand Guidelines** (Anthropic markasıyla gelen şablon; kendi hex/font/spacing/logo/ton kurallarınla değiştirip her çıktıyı marka-uyumlu zorlarsın; tasarımcı olmayanlara da dağıtılabilir), **Canvas Design** (UI değil; önce yazılı "tasarım felsefesi/manifesto", sonra PNG/PDF poster-grafik üretimi) ve **skill-creator** (Claude seni röportaj eder, SKILL.md taslağı yazar, test prompt'ları koşar, geri bildirimle revize; `.skill` dosyası olarak paylaşılır). Sonda frontend-design skill'iyle tek prompt'tan landing page üretip "playbook yüklü, niyetle çalışıyor" diyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- frontend-design: https://github.com/anthropics/claude-code/blob/main/plugins/frontend-design/skills/frontend-design/SKILL.md (metin: `kaynaklar/VMvZuhcDdnw-frontend-design-ve-ui-ux-pro-max.md`)
- Implement Design (Figma): https://mcpservers.org/agent-skills/figma/implement-design (Figma MCP bağlantısı şart; sayfa 403 verdi, Figma Community skills: https://www.figma.com/community/skills)
- Theme Factory / Brand Guidelines / Canvas Design: https://github.com/anthropics/skills (metinler: `kaynaklar/Iup1WlUyj9M-anthropic-tasarim-skilleri.md`)
- skill-creator: https://github.com/anthropics/skills/tree/main/skills/skill-creator
- Mobbin (UI referans), Framer

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Skill'i kur (Claude.ai'da Settings → Skills veya Claude Code'da plugin/`npx skills add`).
2. frontend-design yüklüyken "landing page / dashboard / component" isteyince skill otomatik devreye girer: önce purpose, tone, constraints, differentiation → sonra kod.
3. Figma → kod: Figma MCP'yi bağla → Figma linkini yapıştır → skill file key + node id'yi çözer → tasarım bağlamını çeker → SS referansı + asset indirir → projenin bileşenlerine map'ler → checklist (layout, tipografi, renk, etkileşim durumları, responsive, erişilebilirlik).
4. Theme Factory: `theme-showcase.pdf` göster → tema seç → uygula; yoksa yeni tema ürettir.
5. Brand Guidelines: Anthropic şablonunu kopyala → kendi token'larını (primary/secondary renk, heading/body font, spacing scale, do/don't) yazdır → ekip paylaşımı.
6. Canvas Design: 2 adım — felsefe metni → canvas'ta PNG/PDF.
7. Kendi skill'in: skill-creator'a "X yapan skill istiyorum" → röportaj (edge case, input/output) → SKILL.md taslağı → test prompt'ları → geri bildirim → paketle.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Prompt metni verilmiyor; skill metinleri kaynak dosyalarda aynen var. Skill anatomisi: klasör + `SKILL.md` (name, description = tetikleme koşulu, talimatlar) + opsiyonel scripts/references/assets.

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler
- frontend-design'ın "front-end aesthetics guidelines" bölümü: karakterli font eşleşmeleri, renk, hareket, mekânsal kompozisyon, atmosfer; mor gradient / beyaz üstü yuvarlak kart / Inter yasağı.
- Marka kurallarının skill olarak zorlanması → her çıktı tutarlı (ajans için müşteri başına bir brand skill).
- Figma'dan birebir uygulama + sonda doğrulama checklist'i (SS'ten kod yerine token'dan kod).
- Theme Factory ile hızlı tutarlı tema; Canvas Design ile sosyal medya/poster görselleri.

## Hatalar ve çözümleri
- SS verip "koda çevir" → kötü sonuç → Figma MCP + Implement Design skill'i.
- Marka tutarsızlığı → Brand Guidelines skill'i.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Yok.

## Bizim fabrikaya alınacaklar (somut)
1. **Müşteri başına Brand Guidelines skill'i**: onboarding'den gelen renk/font/logo/ton → `skills/marka-<musteri>/SKILL.md` (Anthropic şablonundan türet). Web, app, sosyal medya hattı aynı skill'i okur.
2. frontend-design global kurulu (zaten planda).
3. **skill-creator** ile fabrika skill'lerimizi (teardown, polish checklist, Türkçe copy sesi) üret ve test et.
4. Canvas Design → sosyal medya görsel hattı için aday araç.
5. Figma kullanılacaksa Implement Design + Figma MCP.
