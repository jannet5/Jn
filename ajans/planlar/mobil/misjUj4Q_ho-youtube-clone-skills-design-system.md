# Building a Real App with Claude Code (Start to Finish) — Tech With Tim — 22 dk — https://www.youtube.com/watch?v=misjUj4Q_ho

## Tek paragraf: ne yapıyor, sonuç ne
Web (Next.js) ama süreç fabrika için çok değerli: terminal Claude Code'da plan mode ile milestone'lu `plan.md`, **inşaattan önce** iki tasarım skill'i (Anthropic `frontend-design` + `ui-ux-pro-max`), Supabase MCP ve ImageKit plugin/skill'lerinin kurulması, sonra iskelet, ardından **/design style-guide sayfası** ile tasarım sistemini ayrı onaylatıp tüm sayfalara uygulatma. Sonuç: auth, video yükleme (ImageKit), gelişmiş player (altyazı, hız), thumbnail yakalama, yorum/like, sahte kanal verisi, mini creator studio + analytics olan bir YouTube klonu. Ana mesaj: "Kararları baştan doğru verirsen sorunsuz gider."

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code (terminal; plan mode / auto mode `Shift+Tab`), `/skills`, `/mcp`, `/plugin`.
- **Frontend Design Skill** (Anthropic): https://github.com/anthropics/claude-code/blob/main/plugins/frontend-design/skills/frontend-design/SKILL.md
- **UI UX Pro Max Skill**: https://github.com/nextlevelbuilder/ui-ux-pro-max-skill
- **Supabase MCP** (auth, DB, storage, edge functions): https://supabase.com/docs/guides/ai-tools/mcp
- **ImageKit** (CDN, adaptive bitrate, thumbnail, altyazı, analytics) — agent docs/MCP: https://imagekit.io/docs/build-with-ai#mcp-servers ; video player: https://github.com/imagekit-developer/imagekit-video-player ; ücretsiz kayıt: https://tinyurl.com/595kfr92
- Next.js + Tailwind.
- Wispr Flow: https://ref.wisprflow.ai/TechWithTim-aug26 ; diğer: https://aiagentbuilders.co/yt , https://www.hostinger.com/techwithtim , https://www.hostinger.com/techwithtim10

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:00:48] Masaüstünde `YouTube clone` klasörü, içinde `claude`; `Shift+Tab` ile **plan mode**. Sesle ihtiyaçları dök, sonunu "ask me any questions…" ile bitir.
2. [00:02:57] MVP ve tech stack'i senin seç (web/mobil/desktop? Supabase? ImageKit?); bilmiyorsan Claude'dan trade-off açıklaması iste.
3. [00:04:39] Hazır uzun plan prompt'unu yapıştır (stack "non-negotiable", milestone'lar, scope kilidi) → plan: scaffolding, Supabase schema, upload pipeline, studio manage, watch page, playlist, polish. Planı oku, `Esc`.
4. [00:06:34] `/skills` ile mevcutları gör; iki skill'in GitHub URL'sini verip "install" prompt'u → `/skills` ile doğrula.
5. [00:08:15] Supabase MCP: ayrı terminalde dokümandaki `claude mcp add ...` komutu → `claude` aç, MCP'yi onayla → `/mcp` → "needs authentication" → Authenticate → tarayıcıda giriş.
6. [00:09:53] ImageKit: `/plugin` ile marketplace repo'sunu ekle + ImageKit tools plugin'ini kur, **project scope** seç.
7. [00:11:26] ImageKit video player dokümanındaki "copy for LLM" metnini Claude'a yapıştır, planı güncellet → `plan.md`.
8. [00:12:23] `Shift+Tab` → auto mode, iskelet build. Claude 4 ImageKit değerini istedi (ID/URL endpoint, public key, private key → Developer options) → chat'e yapıştır; `npm run dev` / "spin up the server".
9. [00:14:53] Signup hatası (bkz. Hatalar) → detaylı anlatıp düzelttir.
10. [00:15:35] **Design system** prompt'u → `/design` sayfasında token'lar (accent, foreground, canvas), tipografi, butonlar, thumbnail card incele → "apply this design system to the current build".
11. [00:17:26] İterasyonlar: video boyutlandırma, görüntülenme sayacı, thumbnail (kare yakala / özel yükle), otomatik altyazı, chapters.
12. [00:20:30] Studio + analytics + sahte hesaplar prompt'u → kanallar, studio (views, watch time, subs, likes), arama, history.

## Kullandığı prompt'lar (varsa aynen)
```
Hey Claude, I want to build a YouTube clone. Basically, what I want to do is I just want to fully kind of build my own version of YouTube. I want to have a creator studio. I want to have viewers being able to sign in and view videos. I want to have a really advanced video player. I'm going to use something called Image Kit for that. I want to be able to upload videos, have analytics, and build an MVP of YouTube that's fully functional. Can you ask me any questions that you need to clarify the requirements and come up with a detailed plan?
```
Hazır plan prompt'u (ekranda; transkriptte özetlendiği kadarıyla):
```
I want to build my own private YouTube, a video platform with two sides, the viewer site and the creator studio. [Viewer: home feed, watch page, playlists, dark theme. Studio: drag and drop video uploads, manage videos, one-click publish, a member only flag.]
Stack (non-negotiable): Next.js with Tailwind, Supabase for all of the auth, ImageKit for the entire media layer.
Give me a plan broken into milestones that I can build and verify one at a time. Keep the scope to exactly what I listed. Don't add [features] like search or subscriptions because I want to keep it simple for now.
```
```
Before we proceed, can you please install the following two skills? I'm going to give you the GitHub repo URLs, look through them, and then install the correct skill file for this project.
<frontend-design URL>
<ui-ux-pro-max URL>
```
```
Hey, I'm just pasting some reference from the image kit documentation. I want to make sure we're using their new video player in this build. Just take note of that. and then update the plan and we can start the build process.
```
```
When I go and sign up, what happens is it says it's successful and that it's going to send me an email. However, it never sends me an email. And then when I try to sign in with the same username and password or email and password that I created the account with, it says invalid credentials. Can you help me fix this, please?
```
Design system prompt'u (transkriptte özetlenmiş hali):
```
Set up a design system using the frontend-design skill. Dark theme only. Think premium video platform: near-black background, one restrained accent color, high contrast, tight typography. Design the design tokens, the thumbnail card, [buttons, inputs, headers…]. Show me a style tile page at /design so I can judge it before we build the real pages.
```
```
Okay, all is looking good. Now, I now want to implement the analytics from the video player so that whoever posted the video or uploaded it is able to view the analytics for that. So, let's build like a really minimal kind of YouTube studio or like creator version of the site where people can see the analytics of their own videos. And then I also want you to just stub a few random accounts on YouTube so that we can have some kind of like fake data and I can subscribe to accounts and see what happens when it's not my account.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **İki tasarım skill'i build'den önce** kuruldu: `frontend-design` + `ui-ux-pro-max` → "generic AI slop" görünümünü önlemek için.
- **Design system önce, sayfalar sonra**: token'lar + yeniden kullanılabilir bileşenler (button, card, typography, header, input) tek yerde.
- **Style tile / showcase route (`/design`)**: tüm bileşenleri tek sayfada görüp tek tek düzelttir, onayla, sonra "apply to current build".
- Kısıtlı palet tarifi: near-black zemin, **tek ölçülü accent**, yüksek kontrast, sıkı tipografi, "premium" referansı.
- Medya katmanını hazır premium bileşene (ImageKit player) bırakmak — kendi player'ını yazdırmamak.

## Hatalar ve çözümleri
- Signup "başarılı, e-posta gönderildi" diyor ama e-posta gelmiyor, sonra "invalid credentials" (Supabase e-posta doğrulama) → belirtileri ayrıntılı tarif edip Claude'a düzelttirdi [00:14:53].
- Video yanlış boyutlanıyor, views artmıyor → düzeltme prompt'u [00:17:42].
- Otomatik altyazı ilk seferde gelmedi → ilk üretim birkaç dakika sürüyor.
- Gizli anahtarları chat'e yapıştırdı ("best practice değil") — bizde `.env` ile verilmeli.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Proje açılış checklist'i: (1) plan mode + soru-cevap → `plan.md` milestone'lu, (2) tasarım skill'leri, (3) backend MCP (Supabase), (4) kullanılan servislerin agent skill/plugin'leri — **kod yazdırmadan önce**.
- Skill'leri **project scope** kur ki her müşteri reposunda aynı tasarım kuralları olsun; `frontend-design` ve `ui-ux-pro-max`'ı RN/Expo uyumluluğu açısından değerlendir (web odaklılar; RN için Expo'nun `building-native-ui` skill'i ile birlikte kullan).
- Mobil karşılığı **/design ekranı**: Expo Router'da `app/design.tsx` (dev-only) — token'lar, Button/Card/Input/Sheet/Toast, animasyonlu durumlar; müşteri onayından sonra "apply to all screens". Ham Android pop-up şikâyetine karşı Alert/Modal bileşenlerini de burada tasarlat.
- Plan prompt'unda "Stack (non-negotiable)" ve "Keep the scope to exactly what I listed. Don't add …" kalıpları.
- Her milestone'u "build and verify one at a time".
- Medya ağırlıklı müşteri uygulamalarında hazır servis (ImageKit/Mux) + docs'un "copy for LLM" metnini plana ekletme.
- Demo için "stub a few random accounts" ile sahte veri: müşteriye sunumda boş ekran göstermemek.
