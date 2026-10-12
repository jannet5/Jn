# Design with Claude Code: A Crash Course — UI Collective — 17 dk — https://www.youtube.com/watch?v=ljJuOxTsrtY

> Not: Transkript otomatik çeviri/dublaj kaynaklı (model adları "Opus 3.5 / Opus 5 / Opus 4.5" karışık, "skill" yerine "tool/instrument/Claude demo" geçiyor). Prompt'lar transkriptteki haliyle aktarıldı; anlam olarak "skill" okunmalı.

## Tek paragraf: ne yapıyor, sonuç ne
Tasarımcı Kirk (UI Collective), Claude Design ile Claude Code'un `/design` aracını aynı prompt'la (medikal servis ayarlar sayfası) karşılaştırıp Claude Code'un daha iddialı layout'lar ürettiğini gösteriyor (ör. ikincil sidebar'lı ayarlar sayfası; canvas üzerinde sürükleme, renk/state değiştirme, mini design system). Ana dersler: (1) tüm sayfa yerine **tek odaklı bileşenden** (4 farklı finans widget'ı) başla, beğenileni temel alıp sayfayı ondan türet; (2) "AI slop"u azaltmak için açık kaynak **Impeccable** skill'ini (68k yıldız; `distill`, `quieter` gibi komutlar) en sonda "temizlik" olarak kullan (token pahalı); (3) **Mobbin MCP connector** ile gerçek uygulama ekranlarından moodboard (Clinical daylight / Warm paper / Instrumental dark) üret → seçilen yönden PDF stil rehberi → PDF'ten tekrar kullanılabilir **brand skill** → yeni ekranlar bu skill ile tutarlı üretiliyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code `/design` (design tool/skill, canvas'ta düzenleme) vs Claude Design
- Impeccable — "the missing design vocabulary for AI agents", ücretsiz, açık kaynak; komutlar: `distill`, `quieter` (dokümanında tam liste). Açıklamada link yok; bilinen adres: https://github.com/pbakaus/impeccable (impeccable.style) — kurulum: dokümandaki terminal komutu (marketplace bazen çalışmıyor).
- Mobbin (≈600k ekran, akış filtreleri) + Mobbin MCP connector; %20 indirim: http://mobbin.com/uicollective
- Claude.ai sohbet (PDF'ten skill üretme), Opus modelleri
- Açıklamadaki diğer linkler: https://uicollective.co/ ; Learn AI Design https://youtu.be/r-MPiu0E4W4 ; Learn Design Systems https://youtu.be/lsgVRCJ4fko ; Figma Variables https://youtu.be/L-tpK7Eeuow ; Build a Design System https://youtu.be/opTANvl9G1g ; Train Claude on Your Design System https://youtu.be/VezxOhvoXqU ; Design to Developer Handoff https://youtu.be/ALkqhXv0GPk ; Multi-Brand DS https://youtu.be/EyeO-yWSkYE ; https://designersmash.com/ ; https://designsystemlabs.co/ ; https://collectivekit.co/

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:00:25] Aynı prompt (medical service settings page) → Claude Design: tek kolon, şablon gibi "formulaic" sonuç.
2. [00:00:35] Claude Code'da `/design` + aynı prompt → daha iddialı layout (settings için ikincil sidebar). Canvas'ta eleman taşıma, renk ve state değiştirme, proje başına küçük design system [00:02:46].
3. [00:03:15] Odaklı yöntem: 4 ayrı finans widget'ı, aynı örnek veri, farklı layout/stil/tipografi/veri görselleştirme → beğenilen widget'tan tüm sayfa üretildi [00:05:18].
4. [00:05:35] AI slop: tamamen yok edilemez; azaltmak için Impeccable kur (terminalden).
5. [00:07:46] Bilerek "AI slop" kartlar ürettirdi → "Quieter these cards" (komut adı ilk kelime olunca daha iyi sonuç) → sakin, yumuşak kartlar. `distill` örneği: emoji dolu kalabalık kartı ana bilgiye indirgeme.
6. [00:09:15] Kural: Impeccable çok token yer → tasarım bitince en sonda bir kez "tidy up".
7. [00:09:51] Mobbin MCP'yi Claude'a bağla → moodboard prompt'u → 3 yön: "clinical daylight", "warm paper", "instrumental dark", her biri hangi rakip uygulamalardan geldiğiyle.
8. [00:13:12] "Warm paper" seçildi → stil rehberi PDF (renkler, kombinasyonlar, accent/semantic renkler, uygulama kuralları, fontlar, layout, doku, bileşenler).
9. [00:14:42] Claude.ai'de PDF'i ekleyip yeniden kullanılabilir skill'e dönüştür (do/don't, checklist).
10. [00:15:38] Claude Code'da skill'i çağırıp "mobile dashboard" istedi → brand rehberine uyan ekran; yeni chat'lerde tekrar screenshot/tarif gerekmiyor.

## Kullandığı prompt'lar (varsa aynen)
Odaklı widget [00:03:35]:
```
Design four separate financial widgets for a personal finance app. Create only widgets without the application's front-end interface. Experiment with different layouts, visual styles, typography, and data visualization. Use the same sample data for all four options to make them easier to compare. Each one should look like a separate approach, not just a color change.
```
Impeccable [00:08:13]:
```
Quieter these cards
```
Mobbin moodboard [00:11:34]:
```
Browse Mobbin and create a moodboard for a medical app, highlighting UI patterns, color palettes, typography, and layout inspiration
```
Stil rehberi PDF [00:13:12]:
```
I like the "warm paper" direction. Based on the examples in this group, create a guide that defines the visual style: colors, typography, layouts, textures, and interface elements. Explain how to consistently apply this in the application. Make it available for download in PDF format.
```
PDF → skill (Claude.ai) [00:14:52]:
```
convert the attached PDF with brand guidelines into a reusable Claude app design tool called Claude demo. Translate the "warm paper" direction into clear rules for typography, colors, padding, layouts, textures, and interface components. Include practical examples, dos and don'ts, and a checklist to maintain design consistency.
```
Test (özet olarak söylendi) [00:15:30]: skill çağrılıp "create a mobile dashboard for me with these elements".

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Tam sayfa yerine **tek bileşenden 4 farklı yaklaşım** (renk değişimi değil, gerçekten farklı layout/tipografi/dataviz), aynı veriyle kıyas → stil doğrulama, sonra genişletme.
- Tek kolon şablon sayfalar yerine ikincil navigasyon gibi daha "düşünülmüş" yapı.
- AI slop işaretlerini (glow, gradient, emoji kalabalığı, aşırı süs) Impeccable `quieter`/`distill` ile bastırma.
- Gerçek uygulamalardan (Mobbin) türetilmiş moodboard → isimlendirilmiş görsel yön → yazılı kural seti (renk, tipografi, padding, doku, bileşen, do/don't, checklist) → skill. Tutarlılığın kaynağı bu skill.

## Hatalar ve çözümleri
- Claude Design şablonumsu sonuç → Claude Code `/design` tercih.
- Tam sayfa üretip saatlerce prompt'la düzeltme → widget-önce yöntemi.
- Impeccable marketplace kurulumu bazen çalışmıyor → terminal komutuyla kur.
- Impeccable komutları serbest cümle yerine komut adıyla başlatılmazsa sonuç zayıf → komut adını ilk kelime yap.
- Impeccable token maliyeti → sadece en sonda bir kez.
- Yeni chat'te tasarım dili kayboluyor → brand skill.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Müşteri başına brand skill üretim hattı** (fabrikanın tasarım aşaması): Mobbin MCP ile sektör moodboard'u (2-3 isimli yön) → müşteri seçer → stil rehberi (PDF/MD) → `.claude/skills/<musteri>-brand/SKILL.md` (renk tokenları, tipografi, spacing, radius, doku, bileşen kuralları, do/don't, checklist). Tüm ekran prompt'ları bu skill'i çağırır.
- Moodboard çıktısı müşteri sunum malzemesi olarak da kullanılsın (onay belgesi).
- **Bileşen-önce kuralı**: her yeni uygulamada önce kilit bileşen (ör. ana kart/widget) için 3-4 farklı varyant, aynı örnek veriyle; seçilen varyant design system'in temeli.
- **Impeccable'ı son aşamada "anti-slop" geçişi** olarak ekle (`quieter`, `distill` vb.); her ekranda değil, teslim öncesi bir kez. Kurulumu fabrika şablonuna (terminal komutu) al; RN/Expo ekranlarında da kural setini uygulat.
- CLAUDE.md anti-slop listesi: glow/neon gölge, gereksiz gradient, emoji ikon, tek kolon ham form, aşırı kart iç içe — yasak.
- Mobbin aboneliği + MCP connector'ı değerlendir (referans ekran araştırması fabrikanın 1. aşamasına).
- Claude Code `/design` canvas'ını hızlı varyant keşfi için kullan, sonra RN koduna aktar.
