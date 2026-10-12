# Claude Design and Claude Code | Idea to iOS App in 30min — Eamonn Cottrell — 28 dk — https://www.youtube.com/watch?v=L_tpLe8pIn8

## Tek paragraf: ne yapıyor, sonuç ne
Tasarımcı olmayan Eamonn, "Progress and Perfection" yan projesi için bir **ezber ayeti (scripture memory) iOS uygulamasını** 30 dakikada Claude Design (claude.ai/design, research preview) ile high-fidelity prototip olarak tasarlatıyor: kendi Beehiiv sayfasının ekran görüntüsü + serbest tarif veriyor, Claude Design soru-cevap formuyla (çeviri, dokunma mekaniği, grain miktarı, palet, varyasyon sayısı) eksikleri topluyor, JSX bileşenleri ve HTML prototip üretiyor; canlı "Tweaks" paneliyle palet/font/mod/mekanik değiştiriliyor; bir tur düzeltme prompt'u ile ekranlar iyileştiriliyor. Sonra "Hand off to Claude Code" ile dışa aktarılan komut Claude Desktop'taki Claude Code'a (Opus 4.7, effort: extra high) yapıştırılıyor; çıktı veritabanısız, verileri Swift'te gömülü, durumu UserDefaults'ta tutan **native SwiftUI** projesi + README (Xcode kurulum adımları). Xcode'da çalıştırma bir sonraki videoya bırakılıyor. Ana eksi: Claude Design'ın ayrı haftalık limiti — 30 dk'da %21-26.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Claude Design** (claude.ai/design; Anthropic Labs, research preview) — "High fidelity prototype" modu; diğer seçenekler: slide deck from template, other. Girdi olarak design system, ekran görüntüsü, codebase, Figma dosyası/linki, eskiz (Excalidraw benzeri çizim alanı) kabul ediyor.
  - Duyuru: https://www.anthropic.com/news/claude-design-anthropic-labs
  - Başlangıç rehberi: https://support.claude.com/en/articles/14604416-get-started-with-claude-design
- Claude Design özellikleri: soru formu, iPhone frame, Tweaks paneli (canlı palet/font/mod/mekanik/kelime sayısı), Edit (arka plan rengi, font, base size 16→18), Draw (çizim + not), Present (tam ekran), Share (link, duplicate, template olarak çoğalt), Export → **Hand off to Claude Code** ("developer-ready package with specs and structure").
- Claude Desktop uygulaması → Claude Code sekmesi (Chat / Cowork / Code), model **Opus 4.7**, effort seviyesi **extra high** (yeni varsayılan; low/medium/high/max arasına eklenmiş).
- Çıktı stack: SwiftUI, UserDefaults, Xcode (henüz denenmedi).
- Beehiiv (mevcut web sayfası; ekran görüntüsü kaynağı).
- Açıklamadaki diğer linkler: https://www.gotsheet.xyz/claude-skills (bülten), https://www.gotsheet.xyz/products/consulting-call-with-eamonn

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:01:50] claude.ai sidebar → Design (Chats/Projects/Artifacts/Code altında). Research preview uyarısı.
2. [00:02:30] "High fidelity prototype" seçip proje açıyor; arayüz: solda chat, ortada design file alanı, klasör sistemi, dosya bırakma alanı (images, docs, references, Figma links).
3. [00:04:12] Mevcut Beehiiv sayfasının ekran görüntüsünü yapıştırıp ilk prompt'u yazıyor (aşağıda #1). Sonunda "Tell me what else you need from me" diyerek boşlukları Claude'a bırakıyor.
4. [00:05:46] Claude Design soru formu ve verdiği cevaplar:
   - Bible translation → ESV
   - Demo ayetleri → ekran görüntüsündekiler (prompt #2), yıl içinde yenileri eklenebilsin
   - Tap-to-hide mekaniği → "tap anywhere, more than one random word disappears" — her dokunuşta ~5 rastgele kelime
   - Progress/streak takibi → minimal, evet
   - Scripture personality → "Modern"
   - Grain → 60 (çok değil)
   - Mod → dark ink on cream + night mode
   - Hangi ekranlar → Claude karar versin
   - Varyasyon → "Tweaks" (tek yön + canlı ayar paneli; alternatifler: strong direction polished / 2-3 yan yana)
   - Destek palet → warm and earthy (diğerleri: muted & monastic, dusty & modern, tweaks ile seç)
   - Siteteki ampersand (&) → app mark olarak, subtle
   - iPhone frame'de göster → evet
5. [00:08:11] Claude Design görev listesi (0/9 → 9/9): proje adı, iOS frame, kopya/starter, verse data dosyası, JSX bileşenleri, `scraps` ve `components` klasörleri.
6. [00:08:54] Kullanım kontrolü: Settings → Capabilities/Usage → "Claude design" ayrı haftalık limit, diğer limitlere sayılmıyor. %2 → %11 → %16 → %21.
7. [00:10:37] İlk taslak HTML prototip; tarayıcı sekmesi gibi açılıyor. Grain arka planı beğenmiyor.
8. [00:11:16] Tweaks: palet (Monastic, Dusty...), mod (Cream/Night — night'ı beğeniyor), font (Literary, **Newsreader** — favori), tap mechanic (random fade / first letter blanks), layout (all three screens / just memorize screen), words per tap.
9. [00:12:50] Düzeltme prompt'u (aşağıda #3): seçicide sadece referans+tarih+tek kelime, önizleme yok, üstü çizili metinleri kaldır, mekaniği tersine çevir (boş başla, dokundukça kelime ekle), toggle ekle (varsayılan boş), ayet ekranda büyük ve "cozy", Instagram tipi metin.
10. [00:17:21] Sonuç: "tap to reveal" çalışıyor, metin büyümüş. Toggle görünmüyor → chat'te soruyor; toggle var ama gizli. Edit ile base font size 16 → 18.
11. [00:19:50] Draw/Present/Share keşfi.
12. [00:20:19] Export → "Send to local coding agent" → ek detay yazıp (prompt #4) komutu kopyalıyor.
13. [00:20:56] Claude Desktop → Code sekmesi → yeni klasör "P and P iOS" → kopyalanan komutu yapıştırıyor; Opus 4.7, effort extra high.
14. [00:22:34] İzin istekleri ("allow Claude to run extract design archive" vb.) → "always allow"a rağmen tekrar soruyor.
15. [00:25:01] Claude Code planı: "native SwiftUI iOS app with all the data bundled locally, no database", mastery state UserDefaults'ta; models'tan başlıyor.
16. [00:25:30] Bitiş: 3 ekran, palet + grain overlay, humanist serif, warm creamy paper, auto dark mode. Çalıştırma: Xcode → new iOS app → stub dosyayı sil → kaynak klasörü sürükle; tüm adımlar README'de.
17. [00:26:43] `PNPIOS` dizini ve README inceleniyor: tüm kaynak tek dizinde, veri Swift'te gömülü, mastery/data/settings UserDefaults'ta.

## Kullandığı prompt'lar (varsa aynen)
1. Claude Design ilk prompt [00:04:14] (+ Beehiiv sayfası ekran görüntüsü):
```
I want to create an iOS app for Progress and Perfection, a side project of mine. You can see the screenshot for the main page. It's just a generic beehive page right now. I would like to create a very modern-looking minimalistic app, and here's the functionality that I want for it. It will load in a set of memory verses from scripture. The screen should be very aesthetically pleasing with a nice uh font to be used. Basically, the whole screen as you're using it should be the scripture verse. As you tap it, words will disappear. It's a scripture memory tool is what we're doing here. Give me a mock-up of the main screen, verse selection screen. I'll have a few different buckets of verses, so there needs to be some sort of a foldering type of system in there, but it it needs to look really really sharp. I want grainy, textured, and use this reddish hue with a couple other colors for a palette of your making. Tell me what else you need from me.
```
2. Soru formu cevabı (ayetler) [00:05:52]:
```
Let's just go ahead and use these verses, the ones that we're using so far. Use the ones in this screenshot to start these buckets. I'd like to be able to add more as the year goes by.
```
3. Düzeltme turu [00:12:40]:
```
This is a really great start. Now, I want you to change a few things. So, the main screen where I am choosing from the verses, all I want listed are the references for the verse. You can keep the date and you can keep the one-word description that I've got in there. Thank you for putting those in there. I don't want a preview of the verse in the selector. You can also take out all those strike-through text. That was just from my Google Doc where I had been keeping track of which ones I'd put in my other website. The tap mechanic works great, but I want you to do two things. Start with nothing listed, and when I tap add back in random words rather than taking them out. You can add a toggle between how this behaves also. So, starting blank versus starting with the full verse, but default should be start blank. And lastly, for the verse screen, I would love for the verse to take up more of the screen. You can take some liberties with how the font is displayed. It looks beautiful like it is. I just want it to be a little bit more cozy feeling and large, like some of these Instagram type displays of text. They should just take up a lot of the screen and don't reduce the size unless it's a long verse where you really have to reduce the size to keep it all in uh view.
```
4. Claude Code'a handoff ek detayı [00:20:29]:
```
This is going to be a iOS app. I want the bare minimum to get this thing up and running without a lot of fooling around with databases and stuff like that on my end.
```
(Handoff komutunun kendisi Claude Design tarafından üretilip kopyalandı; metni videoda okunmuyor.)

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Tasarımcı değilken "Tell me what else you need from me"**: Claude Design'ın soru formu tasarım dilini (grain yüzdesi, palet vibe'ı, kişilik, varyasyon stratejisi) kullanıcıya seçenek olarak sunuyor — tasarım kararlarını yapılandırıyor.
- Görsel kimlik: kremalı kâğıt üstüne koyu mürekkep (dark ink on cream) + night mode, %60 grain/doku overlay, kırmızımsı ana ton + warm & earthy destek palet, humanist serif (Newsreader), markadaki "&" işareti app mark.
- Tipografi odaklı ekran: ayet ekranın tamamını kaplıyor, "Instagram tipi" büyük metin; sadece uzun ayette küçültme (auto-fit).
- Etkileşim mekaniği: dokundukça rastgele N kelime belirme/kaybolma (random fade), alternatif "first letter blanks", kelime sayısı ayarlanabilir, başlangıç modu toggle'ı.
- Sade liste: seçici ekranda sadece referans + tarih + tek kelimelik etiket (önizleme yok) — bilgi yoğunluğunu azaltma.
- **Tweaks paneli**: tek tasarım yönü + canlı parametre ayarı (palet, mod, font, mekanik); ayrıca tüm 3 ekranı yan yana görme.
- iPhone frame içinde prototip, auto dark mode.

## Hatalar ve çözümleri
- Grain arka plan fazla/istenmeyen görünüm → Tweaks/düzeltme ile azaltma niyeti (grain 60 seçildi).
- Ayet listesinde üstü çizili metinler (kaynak Google Doc'tan taşınan biçim) → prompt'la kaldırıldı.
- İstenen "boş başla / tam başla" toggle'ı görünmüyor → chat'te sorunca "var ama gizli" cevabı; Tweaks panelinde de çıkmadı — çözülmeden kaldı.
- Claude Code'da "always allow" seçilmesine rağmen tekrar tekrar izin isteği → elle onaylamaya devam (çözüm: permission mode / allowlist ayarı).
- Claude Design kullanım limiti: 30 dk'da ~%21-26 haftalık kota; ekstra kullanım satın alınabiliyor. Enterprise'da tek seferlik kredi (17 Temmuz'da bitiyor).
- Belirsiz: Xcode olmadan App Store'a çıkılamayacağı varsayımı; derleme test edilmedi.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Claude Design'ı tasarım aşaması olarak kullan**: müşterinin mevcut web sitesi/ekran görüntüsü + kısa iş tarifi + "Tell me what else you need from me" → soru formuyla tasarım brief'ini topla. Çıktıyı "Hand off to Claude Code" ile al, ama hedefi SwiftUI değil **Expo + React Native** olarak belirt (handoff ek detayına "Implement in Expo (React Native), TypeScript, expo-router, Reanimated..." yaz).
- Kendi aşama-1 brief şablonumuza Claude Design'ın sorduğu eksenleri ekle: dil/içerik kaynağı, ana etkileşim mekaniği, ilerleme takibi, marka kişiliği, doku/grain yüzdesi, açık/koyu mod, palet vibe'ı (warm & earthy / muted & monastic / dusty & modern), app mark, varyasyon stratejisi.
- "Tweaks" fikri: tasarım onay aşamasında müşteriye 2-3 parametre (palet, font, mod) canlı değiştirilebilen prototip sun; seçilenleri `theme.ts` token'larına dök.
- Tipografi kuralları: içerik odaklı ekranlarda büyük, ekranı dolduran metin + sadece taşarsa küçültme (RN'de `adjustsFontSizeToFit` + `minimumFontScale`); `expo-font` ile serif (Newsreader gibi) yükle.
- Doku/grain overlay ve "cream/night" çift tema — premium hissin ucuz yolu (RN'de yarı saydam noise PNG overlay).
- Basit uygulamalarda backend'siz başla: veriyi bundle'a göm, durumu yerelde tut (Expo karşılığı: AsyncStorage / MMKV ≈ UserDefaults) — müşteri MVP'si hızlanır.
- Claude Code tarafında izin yorgunluğunu önlemek için proje `.claude/settings.json` allowlist'i / uygun permission mode hazırla.
- Maliyet notu: Claude Design ayrı haftalık kota tüketiyor (~%1/dk); müşteri başına tasarım oturumlarını planla, iterasyonları tek prompt'ta topla (videodaki gibi çok maddeli düzeltme prompt'u).
