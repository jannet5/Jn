# Generate Better AI Designs in Claude Code — UI Collective — 11 dk — https://www.youtube.com/watch?v=nbk0PMS0tos

> Kaynak notu: Transkriptin tamamı okundu. Demo bir **landing page** (web), ama yöntem (referans görselli design-inspiration skill'i) mobil ekranlara birebir uygulanabilir; Mobbin'in asıl gücü zaten mobil ekran arşivi.

## Tek paragraf: ne yapıyor, sonuç ne
Kirk (UI Collective) aynı tek satırlık prompt'u ("crypto bank landing page") iki kez çalıştırıyor: önce çıplak → tipik AI çıktısı (agresif gradient, basit metrikler, sıradan CTA) [01:36]; sonra Claude Code desktop'ta Claude'a **"design-inspiration" adlı bir skill** yazdırıp, skill'in içine `images/` klasörü açtırıp, **Mobbin**'den seçtiği 6 ekran görüntüsünü (3 Linear, 3 Rocks/"Rock" sitesi) rakip bazlı alt klasörlere iyi adlarla koyduruyor. Skill "her tasarım oluşturulduğunda/değiştirildiğinde bu klasördeki referanslara bak" diyor. Aynı tek satırlık prompt bu sefer referansları inceleyerek çok daha profesyonel bir ilk taslak veriyor [08:42–09:04]. Mesaj: tekrar tekrar "şunu değiştir" demek yerine bağlamı skill'e koy; skill büyüdükçe (hero/footer/içerik bloğu klasörleri) çıktı iyileşir.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Claude Code desktop app** — kurulum dokümanı: https://code.claude.com/docs/en/desktop-quickstart [01:00]
- **Claude Skill: `design-inspiration`** (Claude'a yazdırılan, `SKILL.md` + `images/` klasörü) [02:38–05:40]
- **Mobbin** (ekran/akış/UI element arşivi; Apps + Sites bölümü, kategori ve şirket filtresi; Airbnb örneği): http://mobbin.com/uicollective (%20 indirim linki) [05:40–06:57]
- Referans markalar: Linear, Rocks [06:57]
- Diğer linkler (açıklama): UI Collective https://uicollective.co/ · X https://x.com/KirkMDesign · Design system kiti https://collectivekit.co/ · Design system hizmeti https://designsystemlabs.co/
- İlgili videolar: Build a Design System https://youtu.be/opTANvl9G1g · Complex Design System Setup https://youtu.be/L-tpK7Eeuow · AI & Design Systems https://youtu.be/XfezMs8B-O8
- MCP kullanılmıyor.

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. **Baseline**: Claude Code desktop'ta Prompt 1 → jenerik AI tasarımı (gradient ağırlıklı, basit metrikler, AI kokan kartlar) [01:25–02:21].
2. **Skill oluşturma**: Prompt 2 ile Claude'a skill yazdır [02:38–04:11]. Claude kendiliğinden yazdı: skill'i nasıl kullanacağı, görsel referanslar bölümü, referansların nasıl yorumlanacağı + `images/` klasörü [04:56]. "İdealde bu skill metnini elle cilalarsın" notu.
3. Desktop app'te **"Copy to skills"** ile skill'i skill'lerine kopyala [05:30].
4. **Mobbin araştırması**: Sites bölümü → "crypto landing page" mantığıyla dark mode örnekler; Rocks ve Linear seçildi; Sections görünümünden 6 ekran görüntüsü (3+3) [05:40–07:22]. Mobil için Apps bölümü, kategori/şirket filtresi ve "flows" (ör. onboarding) kullanılabilir [06:18].
5. Görselleri chat'e sürükle + Prompt 3 → `images/` klasörüne eklendi [07:35].
6. **Organizasyon**: Prompt 4 → `images/linear/`, `images/rocks/` alt klasörleri + anlamlı PNG adları [07:35–08:21].
7. Skill'i tekrar **Copy to skills** → eskisini replace et [08:21].
8. **After**: aynı Prompt 1 tekrar → Claude çalışırken "studied the references" diyerek skill referanslarını okudu → daha profesyonel ilk taslak [08:42–09:04].
9. Genişletme önerisi: rakipleri özellik/işlev/layout'a göre klasörle; `hero/`, `footer/`, içerik blokları, layout'lar için ayrı klasörler [09:49–10:27].

## Kullandığı prompt'lar (varsa aynen)
Prompt 1 — baseline ve after (aynı prompt) [01:36, 08:42]:
```
Please build me a landing page for a crypto bank.
```
Prompt 2 — skill oluşturma (videoda okuduğu son hâli) [04:11]:
```
Please build a Claude skill called design inspiration. Here, every time a design is created and modified, Claude will look at the files inside of this skill, which will include visual references of designs I want to mimic. This will help create designs more closely aligned with the look and feel we're going for.
```
Prompt 3 — görselleri ekleme [07:35]:
```
Please add these to the images folder for a reference.
```
Prompt 4 — düzenleme [07:35–08:00]:
```
Can you put the linear screenshots inside a linear folder and the same with rocks as well just for better organization, especially as the skill might grow. Also, name the images better.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Kalıcı görsel bağlam**: referanslar tek seferlik prompt eki değil, her tasarım/değişiklikte otomatik okunan skill'in parçası.
- **Kopyala değil, taklit et ("mimic, not copy")** ifadesi skill tanımında [03:08].
- Referans seçimi **sektör + tema** uyumlu (crypto → dark mode, Linear/Rocks gibi "slick" ürünler).
- Referanslar rakip bazlı klasörlü ve anlamlı adlı → model hangi referansın neyi temsil ettiğini anlıyor.
- Önce Mobbin'de rakip/ilham taraması, sonra AI'a besleme ("Before I feed any designs to AIs, I always come to Mobbin first") [05:40].

## Hatalar ve çözümleri
- İlk yüklemede ekran görüntüleri anlamsız adlı ve tek klasördeydi → Prompt 4 ile rakip klasörleri + iyi adlar [07:35].
- Skill güncellendiğinde desktop app'teki kopya eski kalıyor → tekrar "Copy to skills" + replace [08:21].
- Claude'un yazdığı skill metni demo için yeterli ama ham → gerçek kullanımda elle cilalanmalı [04:56].

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Fabrika skill'i: `tasarim-ilham`** — yapı: `SKILL.md` (kural: "her ekran oluşturma/değiştirmede önce `references/` altını incele; mimic, not copy; renk/tipografi/spacing/radius/hiyerarşi çıkar") + `references/<rakip-veya-marka>/<ekran-tipi>-<aciklama>.png`.
- Klasör şeması mobil için: `references/onboarding/`, `references/paywall/`, `references/home/`, `references/empty-state/`, `references/bottom-sheet/`, `references/modal-dialog/` (Android ham pop-up şikâyeti için özellikle modal/sheet/toast referansları).
- **Her müşteri projesinde ilk iş**: Mobbin Apps'te sektör + 2 farklı dikeyden 6–10 ekran görüntüsü topla, skill'e klasörlü ve iyi adlı koy (dosya adı = ekran tipi + marka + özellik).
- **Before/after kontrolü**: müşteri sunumu için aynı prompt'un skill'siz ve skill'li çıktısını yan yana göster (kaliteyi kanıtlama aracı).
- Skill metnini Claude'a ilk taslak olarak yazdır, sonra elle şu eklemelerle cilala: "Never use generic purple/blue gradients", "use the references' spacing rhythm", "native modals yerine custom bottom sheet".
- Mobbin aboneliği fabrikanın araç listesine (http://mobbin.com/uicollective).
