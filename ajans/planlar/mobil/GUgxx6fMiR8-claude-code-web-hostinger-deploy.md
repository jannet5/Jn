# How to Build an App With Claude Code - Full Tutorial for Beginners — Tech With Tim — 24 dk — https://www.youtube.com/watch?v=GUgxx6fMiR8

## Tek paragraf: ne yapıyor, sonuç ne
Mobil değil, **web** (Next.js + Tailwind portfolyo landing page). Claude Code'u terminal kurulumuyla alıp Cursor içinde eklentiyle kullanıyor; asıl katkısı süreç: önce "bana soru sor, spec.md yaz" ile planlama, sonra "Keep it MVP" ile tek seferde inşa, Git kurulumu ve her değişiklikte otomatik commit, son olarak Hostinger hosting + **Hostinger MCP** ile doğal dilden deploy ("Deploy my project now"). Cursor içindeki Claude Code oturumunun MCP'yi görmediği sorunu ve config'i elle ekleyerek çözümü gösteriliyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code (terminal kurulumu, `curl -fsSL ...` komutu sitede): https://claude.com/product/claude-code — Opus 4.7 (varsayılan; ucuz için Sonnet/Haiku), effort: medium, mod: "edit automatically".
- Cursor (VS Code fork'u): https://cursor.com/get-started — Claude Code eklentisi + **Hostinger** eklentisi/connector.
- Next.js + Tailwind; Git (lokal repo).
- Hostinger (Web hosting, ücretsiz domain, API token, MCP): https://www.hostinger.com/techwithtim , https://www.hostinger.com/techwithtim10 , https://www.hostg.xyz/SHJNq — kupon `techwithtim` (%10, 12+ ay).
- Wispr Flow (sesle dikte): https://ref.wisprflow.ai/techwithtim ; bülten: https://techwithtim.net/newsletter

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:00:36] Claude Code'u terminal komutuyla kur, `claude` ile çalıştır, giriş yap (premium abonelik gerekli).
2. [00:01:47] Cursor kur → File > Open Folder → `my site` klasörü; Cursor agent sekmesini kapat.
3. [00:04:00] Extensions: Claude Code + Hostinger connector kur.
4. [00:05:05] Claude Code panelinde "New session"; alt kısımda edit automatically / ask before edits / plan mode; effort medium; `/` → model seçimi.
5. [00:06:42] Planlama: düşünceleri dök + "ask me questions" + "markdown dosyası oluştur" → Claude çoktan seçmeli sorular sordu (amaç: personal brand, yapı: tek uzun landing, estetik: dark & modern, stack: Next.js + Tailwind).
6. [00:11:13] İçerik için "kendimi araştır" dedi → `spec.md` üretildi; gözden geçir.
7. [00:11:45] "Keep it MVP … run the site" → ~5-6 dk sonra localhost:3001.
8. [00:14:38] Git repo + her değişiklikte commit talimatı.
9. [00:15:44] Hostinger: plan seç ($3.99/ay), domain al, kayıt; ilk kurulumda zip'lenmiş proje klasörünü yükleyip deploy (zorunlu ilk yükleme).
10. [00:19:35] hPanel → API → token üret ("never expires", ad: CloudCode) → Cursor'daki Hostinger eklentisine yapıştır → Connect.
11. [00:20:43] Claude Code MCP'yi görmedi → Hostinger API sayfasındaki MCP config'i kopyalayıp Claude'a "Add this configuration to my Claude Code" de, token'ı değiştir; yeni oturum, `/mcp` → Hostinger connected.
12. [00:23:14] "Deploy my project now…" → site adı değişikliğiyle yeniden yayın.

## Kullandığı prompt'lar (varsa aynen)
```
Hi, I want to make a portfolio website for myself, Tim Mercica, or Tech with Tim. I don't really know exactly what I want to be on the website, but I want to put some of my prior work experience. I want to put some of my skills, characteristics, and maybe reasons why I should get hired, and maybe some different projects from my GitHub I want to showcase on there as well. Now, can you ask me a bunch of questions about the design, what the pages should look like, just any information that you need to help me build this site. Let's get a clean kind of spec and plan going before we start building anything. I'd like you to create a markdown file that's going to have all of the information for the site before we start building. Again, ask me any questions that you need.
```
```
Okay, the spec looks great. I want you to build out the full website right now. Keep it MVP. Don't make it too complicated, and then run the site so I can view it on my computer.
```
```
This is great. What I would like you to do now is to create a Git repo and just save all of the work as a commit, and I would like you to continually keep creating commits anytime we make a change so I can roll back to something if a mistake happens in the future.
```
```
Hey, what websites and domains do I have with Hostinger?
```
```
Add this configuration to my Claude Code.
```
```
Deploy my project now. Change the name of the site rather than timrusicka to be timtech.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Tasarım kararlarını Claude'un sorularına cevap olarak önceden sabitledi (dark & modern, tek sayfa) ve spec'e yazdırdı; ayrı skill/design system yok.
- Logo, renk teması, sayfa düzeni gibi bilgileri baştan vermeyi öneriyor [00:07:16].

## Hatalar ve çözümleri
- Claude Code'un ihtiyaç duyduğu bir araç kurulu değilse: "give me the instructions on how to install this" de [00:12:53].
- Cursor içindeki Claude Code oturumu, eklentinin eklediği Hostinger MCP'yi görmedi (Cursor agent'ı görüyordu) → Hostinger API sayfasındaki config'i Claude Code'a eklet, yeni oturum aç, `/mcp` ile doğrula [00:20:43–00:22:39].
- Hostinger kurulum sihirbazı ilk adımda dosya yüklemeyi zorunlu tutuyor → proje klasörünü zip'leyip yükle.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Aşama 0 "spec röportajı": müşteri brief'i + "ask me any questions … create a markdown file (spec.md)" kalıbı; Claude'un çoktan seçmeli sorularını müşteri formuna dönüştür.
- "Keep it MVP" + küçük, ayrık görevler kuralı CLAUDE.md'ye.
- Git: ilk adımda repo + "her anlamlı değişiklikte commit" talimatı CLAUDE.md'de kalıcı olsun.
- Platform MCP'si ile doğal dilden deploy fikri: bizde karşılığı **EAS** (eas-cli komutlarını Claude'a çalıştırtmak) ve gerekirse Expo MCP; işletmelere landing page satılıyorsa Hostinger MCP pratik bir yan ürün.
- MCP sorun giderme adımı: `/mcp` ile kontrol, IDE eklentisinin eklediği MCP'nin Claude Code'a geçmeyebileceği notu.
- Maliyet ayarı: plan/spec için yüksek model, rutin işler için Sonnet; effort medium.
