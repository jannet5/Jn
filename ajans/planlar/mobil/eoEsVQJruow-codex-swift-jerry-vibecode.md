# Build a FULL Mobile App with Codex (Full Guide) — Riley Brown — 27 dk — https://www.youtube.com/watch?v=eoEsVQJruow

## Tek paragraf: ne yapıyor, sonuç ne
OpenAI Codex masaüstü uygulaması + Xcode ile (Expo değil, native Swift/SwiftUI) "Jerry" adlı iMessage tarzı bir "uygulama yapan uygulama" kuruyor: kullanıcı Jerry ile sohbet ediyor, arka planda Claude Agent SDK (hosted bridge) Vibe Code CLI'yi çağırıp web/mobil uygulamayı üretip host ediyor, dönen link uygulama içi tam ekran preview'da açılıyor. Tasarımı Codex'e paralel olarak Paper (AI ajanlar için Figma benzeri) MCP ile çizdiriyor; Paper'daki ekran linklerini Codex'e verip birebir uygulatıyor. Sonradan Twitter/X tarzı "preview üstte, altta sohbet input'u" overlay ve OpenAI Whisper ile ses kaydı butonu (animasyonlu dalga) ekleniyor. Yapı dağınık ve doğrusal değil; değeri iş akışında: paralel thread'ler, steer mesajları, plan mode, ekran görüntüsüyle UI düzeltme, tasarım aracını MCP ile ajanın kendisine çizdirme.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- OpenAI Codex (desktop): https://openai.com/codex — plan mode (Shift+Tab), steer, çoklu thread
- Claude Agent SDK (Anthropic): https://docs.anthropic.com — API key: platform.claude.com
- Vibe Code CLI + skill: https://vibecode.dev (Riley'nin kendi şirketi; sandbox + hosting, mobil ve web aynı şekilde web preview'da açılır)
- Paper (AI-native tasarım aracı, MCP ile ajan çiziyor) — paper.design (açıklamada link yok)
- Xcode + Swift (iOS app template "App", gerçek iPhone'da çalıştırma)
- OpenAI Whisper (ses → metin)
- Notion (prompt saklama), akış diyagramı görseli prompt'a eklendi

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:00:45] Replit mobil uygulamasını eleştiriyor (küçük, kalabalık) → hedef: iMessage gibi basit bir vibe coding app.
2. [00:01:29] Prompt'u Notion'da hazırladı + akış diyagramı (user message → Jerry/Agent SDK → Vibe Code CLI → app URL → otomatik preview).
3. [00:03:29] Xcode → New Project → App → ad `Jerry` → "Hello world" şablon.
4. [00:04:05] Codex → New project → **en üst** `Jerry` klasörünü seç (içindeki alt `Jerry` değil) → yeni thread.
5. [00:05:28] Prompt + diyagram görseli → Shift+Tab ile plan mode.
6. [00:05:56] Paralelde ikinci thread: Paper'da boş board aç → "Please design the four most prominent screens here in paper.design. Use the MCP tool to make the design." → Inbox, Conversation, Preview, Build result ekranları.
7. [00:07:05] Steer: "focus most of your attention on the chat and the preview pages. Keep it simple."
8. [00:07:52] Plan soruları: agent runtime → "hosted bridge"; ilk milestone → web + mobil ikisi (Vibe Code CLI ikisini de web preview'da açıyor).
9. [00:08:44] Paper'da ekranları seçip link kopyala → Codex'e steer ile tasarım düzeltmesi (preview tam ekran, Safari ikonu sağ üstte, back butonu, ana sayfa "Build with Jerry" + büyük J, proje adını AI ile ilk prompt'tan üret).
10. [00:09:24] API key'ler: Claude (platform.claude.com) + vibecode.dev key → planı onayla → key'leri follow-up olarak yapıştır.
11. [00:10:48] İlk build ~7+ dk (ilk prompt en uzunu). Xcode Play → gerçek iPhone.
12. [00:11:37] "The data couldn't be read because it's missing" → Xcode log'unu kopyalayıp Codex'e.
13. [00:12:31] Cevap gelmiyor → ekran görüntüsü + "normal Claude Agent SDK sohbeti gibi olsun, Vibe Code'u sonra ekleriz".
14. [00:13:14] Mesaj balonları bozuk → yeni thread'de ekran görüntüsü + iMessage prompt'u (çok satır, hizalı, üç nokta typing indicator).
15. [00:14:14] Agent'ın Vibe Code CLI'ye erişimi yok → vibecode.dev kurulum talimatını kopyalayıp yapıştır; yeni projede skills dosyası olmadığını fark edip Codex'e söyledi → her yeni Agent SDK sohbeti CLI+skill kurulu başlasın.
16. [00:16:22] Test: "Can you please build a simple landing page for Joe and the Juice Smoothies? Make it simple." → timeout hatası → "This does take a while to run. It will take like 5 minutes or so... figure out what you need to change."
17. [00:17:32] Preview otomatik açılıyor ama mesajdaki linke tıklayınca dış tarayıcıya gidiyor → "linke tıklayınca da preview açılsın".
18. [00:19:37] "Please create a mobile app that is a to-do list app in the style of Notion. Make it cool." → çalıştı.
19. [00:19:37] Paralel thread: Paper'daki ana sayfa tasarımına göre home redesign + "See more" ile projeler sayfası, attach butonu yok. (İlk denemede görseli eklemeyi unuttu → "I want it to look like the image I just gave you".)
20. [00:21:38] Preview'ı daha tam ekran yap: üstteki beyaz bar gitsin, close butonu overlay, alttaki boşluk azalsın.
21. [00:22:40] X/Twitter'ın link açma tarzı ekran görüntüsü → preview'ın altında sohbet input'u (aynı konuşma), voice butonu yer tutucu.
22. [00:24:40] Ses: boşken siyah buton + küçük ses dalgası ikonu, konuşurken ince animasyon, OpenAI Whisper ile metne çevir.
23. [00:25:58] Sesle değişiklik: "light mode, very light gray, change the font, add one feature" + markdown (bold, bullet) render düzeltmesi.

## Kullandığı prompt'lar (varsa aynen)
Ana fikir (ekranda Notion'dan, sözle özetlendi) [00:01:29]: Swift app that builds web apps and mobile apps; Replit klonu ama iMessage gibi; "Jerry" adlı akıllı geliştiriciyle mesajlaşma hissi; mesajlar telefonda lokal; Claude Agent SDK; Vibe Code CLI sandbox+host; son mesajdaki son link otomatik preview'da açılsın.

Paper [00:06:16]:
```
Please design the four most prominent screens here in paper.design. Use the MCP tool to make the design.
```
Steer [00:07:05]:
```
By the way, focus most of your attention on the chat and the preview pages. Keep it simple.
```
Tasarım düzeltme (Paper linkiyle) [00:08:44]:
```
On the preview page, can you please don't show the link? And also, don't show the open automatically from Jerry's latest link. And then make the Safari icon at the top right. And then have the the back button to Jerry there as well to minimize the space. I want this preview to basically take up the whole screen. Additionally, I want the home page should be don't have messages at the top. Just have it be a big photo of of Jerry on the right, which is just like the black J. And then the text at the top should just be like build with Jerry. And then in the chats it should just have a name of the project. And so use AI to name whatever my first prompt is name the project that. And so instead of it having a J it should just have like a little app icon like a mobile app icon there.
```
Sohbet önce çalışsın [00:12:31]:
```
yo, now it's working. It says Jerry is thinking through the build. By the way, like I want to chat with this just like any ordinary agent. The Vibe code CLI skill will allow us to create apps when we ask it. And so we're basically just chatting as this as if this was just Claude code. I can talk to it and so I'm not seeing any response by the agent. And so this should just be like a chat response. And I should get a response when the AI responds. And when we add the Vibe code CLI, it'll respond with a link to our application. That's how this will work. So please focus on making this just be like a chat with the with the Claude agent SDK. And then we'll start adding in the Vibe code functionality and testing if the apps actually open on the phone.
```
iMessage görünümü [00:13:14]:
```
This is very messed up. Uh can we fix this, please? I want to make it look like iMessage chat with multiple lines. Make sure everything is aligned. And please have the three dots like in iMessage when AI is responding.
```
Linkler preview'da [00:18:53]:
```
So, now, anytime there's any message that has a link, it automatically opens in app [...] But when I click one of the links, it doesn't do that. And so I want this to open the preview. Please figure out how to do this.
```
Home redesign [00:19:37]:
```
Please redesign this page right here. This is the front main page. Keep the settings menu. We need that, but other than that, have it look like this, but add a projects page that can be accessed to see a full page of projects somewhere in the list. Like at the bottom of the list that shows on the main page, it should show see more at the bottom, please. [...] And also, no need to add the attach button.
```
Tam ekran preview [00:21:38]:
```
Is it possible to make the web preview full screen? Actually, more full screen and basically not have the part at the top that is white, right? [...] the close button can be an overlay and we don't need app preview and we could make the part on the bottom not take up as much as well.
```
X tarzı alt sohbet [00:22:40]:
```
See this screenshot? You see how when I opened it on Twitter, it has the part of the app on the bottom? Can you make that the chat just like put a little chat bubble there where I would type into like in text, you know, that's at the bottom? Just put that there so I can actually type into the message as I use the app. And it should be about this height, so keep it vertically where it is, right? It's very pushed up towards the top. That's okay. I actually want you to have this little overlay, so when you open this up, the little input chat should be there as if in I should be in that same conversation. And then this allows me to like see that I can type while I'm using the app preview. This will be really cool. And put a little voice record button. Uh we'll add voice record later. For now, just put a voice record button there similar to this user interface, but this is kind of how I want it to open when I open one of these links.
```
Ses butonu [00:24:40]:
```
Please make the input area in the chat and in the preview have a voice record button. This should be there when there is nothing typed. So, before anything is typed, it should be a black button with a small sound wave icon. And when pressed, it should make a cool animation when I speak. Subtle, but it should record my voice and enter it in the app or enter it in the text area so I can send a message or type in it without opening keyboard and viewing the app that open on preview. Please use OpenAI Whisper. I'll give you a key if you need it.
```
Sesle stil değişikliği [00:25:58]:
```
Please actually make this app light mode. It should be very light gray, like almost white, but light gray, and completely change the font. And then add one feature that you think would be cool.
```
```
Please make the bold formatting and bullets, etc., render properly in that because right here it's just like not showing up the right way.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Önce tasarım aracında (Paper, MCP ile ajan çiziyor) ekranlar → ekran linki/seçimi Codex'e referans. Kod ajanı "boş kağıttan" tasarlamıyor.
- Referans uygulama dili: iMessage (balonlar, üç nokta typing indicator), X/Twitter in-app browser (preview + alt input), Safari ikonu. Tanıdık bir native uygulamayı "şunun gibi" diye adlandırmak.
- Ekran görüntüsü ile geri bildirim: bozuk hizalama, boş alan, beyaz bar — her seferinde screenshot sürükle-bırak.
- Ses butonu: boşken siyah + dalga ikonu, basınca "subtle" animasyon; metin yazılınca gönder butonuna dönüşme kalıbı.
- Tam ekran preview, close overlay, minimal chrome; ana sayfada büyük marka harfi (J), projelerde mini app ikonu, "See more".

## Hatalar ve çözümleri
- "The data couldn't be read because it's missing" → Xcode konsol log'u Codex'e [00:11:37].
- Ajan cevap vermiyor ("Jerry is thinking...") → önce saf sohbeti çalıştır, aracı sonra ekle [00:12:31].
- Mesaj formatı bozuk → screenshot + iMessage prompt'u [00:13:14].
- Agent SDK'nın Vibe Code CLI/skill erişimi yok, yeni projede skills dosyası yok → kurulum talimatını yapıştır, her yeni oturumda kurulu olsun [00:14:14–00:15:20].
- Uzun üretimde timeout → süreyi (~5 dk) belirtip düzelttirdi [00:17:07].
- Linke tıklayınca dış tarayıcı → in-app preview [00:18:53].
- Her seferinde uygulamayı silip yeniden kurmak gerekiyordu → lokal sohbet DB'si siliniyor (çözülmedi).
- Redesign'da görseli eklemeyi unuttu → yanlış sonuç; görseli ekleyip tekrar.
- Markdown (bold/bullet) render edilmiyor → ayrı prompt.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Tasarım-önce adımı: kod ajanından önce bir tasarım tuvalinde (Paper MCP, ya da Claude Code design/canvas) 3-4 ana ekranı ajan çizsin; sonra ekran linki/görseli kod prompt'una referans olarak verilsin.
- Paralel thread düzeni: bir thread plan/kod, bir thread tasarım, bir thread hata düzeltme; Claude Code'da worktree/ayrı oturumla taklit.
- "Bilinen native uygulamaya benzet" kuralı: her ekran için referans ver (iMessage sohbet, X in-app browser, Apple Wallet kart vs.) — CLAUDE.md'de "her ekran prompt'u bir referans uygulama içerir".
- Sohbet/AI asistan modülü şablonu (işletmelere chatbot ekranı): iMessage balonları, typing indicator (üç nokta animasyonu), çok satır hizalama, markdown render (`react-native-markdown-display` vb.), boşken mikrofon / yazınca gönder butonu.
- Ses girişi bileşeni: Whisper (veya `expo-speech-recognition`) + kayıt sırasında Reanimated dalga animasyonu.
- In-app browser: linkleri dışarı atma; `expo-web-browser` / WebView ile uygulama içinde, close overlay ile aç.
- Debug kalıbı: cihaz log'unu (Xcode/Metro) aynen ajana yapıştır; UI hatasında her zaman screenshot.
- Ajan özellikleri ekleyen uygulamalarda önce "düz sohbet çalışsın", araç/skill sonra.
- Not: video Swift; Expo fabrikamız için birebir kod alınmaz, iş akışı ve UI kalıpları alınır.
