# Arka planda bilgisayar kullanımı — araştırma raporu

Tarih: 2026-10-03 · Kapsam: Windows'ta AI "computer use" (özellikle Codex) çalışırken
kullanıcının aynı bilgisayarı aynı anda kullanıp kullanamayacağı, nedenleri ve alternatifler.

> Bu rapordaki her teknik iddia aşağıdaki kaynaklara dayanır. **Canlı Windows deneyi
> bu bulut oturumunda yapılmadı** (bulutta Windows masaüstü yok). Deney için hazır,
> sözdizimi ve birim testleri geçmiş araçlar `test-kiti/` klasöründedir; sonuçları
> `TEST-PLANI.md` tablosuna yazılacaktır.

---

## 1. Kısa cevap

| Soru | Cevap (Ekim 2026 itibarıyla) | Dayanak |
|---|---|---|
| Codex computer use Windows'ta fareyi devralıyor mu? | **Evet.** Windows'ta imleci hareket ettirir, yazar ve ön planı alır. | OpenAI resmi doküman [1] |
| Ben aynı anda kendi işimi yapabilir miyim? | **Aynı Windows oturumunda hayır** (güvenilir şekilde). Resmi metin: "It can't operate in the background while you keep using the same Windows session." | [1], [3] |
| Onun açtığı pencereyi alta alırsam ne olur? | Ajan ekran görüntüsüyle çalışır; hedef pencere görünmezse işi bozulur ya da pencereyi **yeniden öne getirir** (Hesap Makinesi'nin tekrar öne gelmesi bu davranışla uyumlu). Resmi öneri: "keep the target app visible on the active desktop while the task runs." | [1] |
| Ajan arkada tıklamaya devam eder mi? | Windows sürümünde ajan **gerçek imleç ve klavye kuyruğunu** kullanır; siz başka pencereye geçerseniz onun tıkları/tuşları sizin pencerenize düşebilir, sizin tuşlarınız onun penceresine gidebilir. | [1], [6], [7] |
| macOS'ta durum farklı mı? | **Evet.** Codex ve Claude macOS'ta arka planda, kendi imleciyle, siz çalışırken iş yapabiliyor. | [1], [2], [4] |
| Claude Desktop Windows'ta farklı mı? | Hayır, Windows'ta da ön planda; arka plan modu yalnız macOS. Windows'ta çalışırken diğer pencereleri **gizler**. | [4] |
| Gerçekten aynı anda çalışmanın yolu var mı? | Var: ajanı **ayrı bir masaüstü/oturumda** çalıştırmak (VM, Windows Sandbox, ayrı bilgisayar, Windows Agent Workspace önizlemesi) veya **fare kullanmayan** yöntem seçmek (MCP/plugin/CLI, headless tarayıcı, UI Automation). | §4 |

---

## 2. Neden böyle? (teknik neden)

1. **Windows'ta etkileşimli oturumda tek "girdi masaüstü" vardır.** Microsoft: "only one of
   these desktops at a time is active. This active desktop, also known as the *input desktop*,
   is the one that is currently visible to the user and that receives user input." [7]
   Yani aynı oturumda tek fare imleci ve tek klavye odağı vardır.
2. **Ekran-tıklama tipi ajanlar sentetik girdi üretir** (Windows'ta tipik olarak `SendInput`).
   Microsoft: SendInput olayları "serially into the keyboard or mouse input stream" ekler [6] —
   sizin fiziksel fare/klavyenizle **aynı akışa**. Klavye girdisi her zaman **odaktaki** pencereye gider.
   Bu yüzden ajan, hedef pencereyi öne getirmek ve imleci oraya götürmek zorundadır.
3. **Ajan ekran görüntüsüyle "görür".** Hedef pencere başka bir pencerenin altındaysa veya
   küçültülmüşse ekran görüntüsünde yoktur → ajan ya kör kalır ya da pencereyi öne alır.
4. **macOS'taki arka plan modu** işletim sistemine özgü erişilebilirlik/pencere API'leriyle
   ayrı imleç/ayrı pencere hedefleme yapar; Windows sürümünde OpenAI bunu henüz sunmuyor.
   Bunu isteyen açık bir GitHub özelliği talebi var (pencereye özel yakalama, imleci
   oynatmayan "background input", mavi kaplamayı kapatma) [5].

---

## 3. "Arka planda", "küçültülmüş" ve "ayrı oturum" aynı şey değildir

| Durum | Ne demek | Ekran-görüntüsü ajanı (Codex/Claude computer use) | UI Automation / API tabanlı otomasyon | Kullanıcı aynı anda çalışabilir mi? |
|---|---|---|---|---|
| **Ön planda** | Pencere en üstte, odak onda | Çalışır | Çalışır | Hayır — fare/klavye çakışır |
| **Arkada (başka pencerenin altında)** | Pencere açık ve çiziliyor ama üstü kapalı | Göremez → bozulur veya pencereyi öne getirir | Çoğu zaman çalışır (imleç/ön plan değişmeden) — **deneyle doğrulanacak** | UIA ile: evet; ekran ajanıyla: hayır |
| **Küçültülmüş (simge durumunda)** | Pencere çizilmiyor; UWP uygulamaları askıya alınabilir | Tamamen kör | Uygulamaya bağlı; bazıları yanıt verir, bazıları vermez — **deneyle doğrulanacak** | UIA ile: belki |
| **Ayrı oturum/masaüstü** (VM, Sandbox, ayrı kullanıcı, Agent Workspace) | Kendi imleci ve klavye odağı olan ayrı ortam | Çalışır | Çalışır | **Evet**, gerçekten paralel |
| **macOS arka plan modu** | İşletim sistemi destekli, ayrı imleç | Çalışır (Codex, Claude) | — | Evet [1][2][4] |

Bu ayrım kabul maddesi "arka plan ve minimize çalışmayı birbirinden ayır" için yapılmıştır;
"Arkada" ve "Küçültülmüş" satırlarındaki UIA davranışı `test-kiti/UIA-ArkaPlan-Deneyi.ps1`
ile **iki ayrı senaryo** olarak ölçülür.

---

## 4. Computer use neden var, yerine ne kullanılabilir?

**Neden var:** API'si, CLI'si veya eklentisi olmayan masaüstü uygulamalarını (eski programlar,
özel araçlar, donanım panelleri) otomatikleştirmenin tek genel yolu, insan gibi ekrana bakıp
tıklamaktır. Resmi dokümanlar da bunu **son çare** olarak konumlar: OpenAI "If the target app
exposes a dedicated plugin or MCP server, prefer that structured integration" der [1];
Anthropic "Computer use is the broadest and slowest, so Claude tries the most precise tool first"
der ve sırayı MCP → shell → tarayıcı uzantısı → computer use olarak verir [4][8].

### Alternatifler — fare kontrolünü koruma açısından karşılaştırma

| # | Yöntem | Fareyi/ön planı alır mı? | Aynı anda kullanım | Kısıt / maliyet | Kaynak |
|---|---|---|---|---|---|
| A1 | **MCP sunucusu / plugin / connector** (uygulamanın yapılandırılmış entegrasyonu) | Hayır | Evet | Her uygulama için yok | [1][4][8] |
| A2 | **CLI / script / API** (ör. Codex'in terminal komutları) | Hayır | Evet | Uygulamanın komut satırı/API'si olmalı | [8] |
| A3 | **Headless tarayıcı** (Playwright; Codex yerleşik tarayıcısı) — web işleri için | Hayır (headless varsayılan) | Evet | Yalnız web uygulamaları | [9][1] |
| A4 | **Windows UI Automation** (InvokePattern, pywinauto `click()`/`set_text`) | Genelde hayır (imleç hareket etmez) | Çoğu zaman evet | Uygulama UIA desteklemeli; özel çizimli arayüzlerde çalışmaz; script yazmak gerekir | [10][11] |
| B1 | **Ayrı VM (Hyper-V/VirtualBox) içinde computer use** | Yalnız VM'in fare/klavyesini | Evet | RAM/CPU, kurulum, VM içinde ayrıca giriş | [3][12] |
| B2 | **Windows Sandbox** içinde | Yalnız Sandbox'ın | Evet | Pro/Enterprise/Education gerekir; kapatınca her şey silinir | [13] |
| B3 | **Ayrı bilgisayar** + telefondan/uzaktan yönetim (Codex remote control) | O makinenin | Evet (siz ana makinedesiniz) | İkinci donanım; makine kilitsiz kalmalı | [3] |
| B4 | **Windows Agent Workspace** (Copilot Actions, deneysel) | Hayır — ayrı Windows oturumu ve ayrı ajan hesabı | Evet (resmi tasarım) | Insider önizlemesi; şu an Codex bunu kullanmıyor | [14] |
| B5 | **Microsoft UFO² Picture-in-Picture** (araştırma projesi) | Hayır — RDP loopback ile ayrı sanal masaüstü | Evet (makalede iddia) | Araştırma kodu; kurulum teknik | [15][16] |
| C1 | **Aynı oturumda Codex computer use (bugünkü durum)** | **Evet** | Hayır | Kısa görevlerde izleyip bekleyin | [1] |
| C2 | **Windows-MCP** (topluluk, Claude Desktop uzantısı) | **Evet** — gerçek imleci oynatır; fareyi oynatınca kontrol size döner | Hayır | Bu sorunu çözmez, yalnız alternatif ajan arayüzü | [17] |
| — | **macOS'ta arka plan computer use** | Hayır (ayrı imleç) | Evet | Mac gerekir; AEA/İngiltere/İsviçre'de kısıtlı | [1][2] |

### Öneri (bu kullanıcı profili için)

1. **Önce yapılandırılmış yol (A1–A3):** Görev web ise yerleşik tarayıcı/Playwright; dosya,
   kod, sistem işi ise terminal/script; uygulamanın MCP/plugin'i varsa o. Bunlar fareye hiç dokunmaz.
2. **GUI şartsa ve siz de çalışacaksanız (B1/B2):** Codex'i bir VM veya Windows Sandbox içinde
   çalıştırın; VM penceresini küçültüp kendi işinize devam edin. OpenAI'nin kendi önerdiği
   güvenli kalıp da budur [3].
3. **Kısa görevlerde (C1):** Aynı oturumda computer use'u başlatın ve görev bitene kadar
   fare/klavyeye dokunmayın; pencereyi alta almayın. Durdurmak için görev ekranındaki
   durdurma kontrolünü kullanın ("You can stop the task or take over your computer at any time" [1]).
4. **Kendi tekrarlayan masaüstü işleriniz için (A4):** UIA script'i; kitteki deney bunun bu
   makinede çalışıp çalışmadığını ölçer.

---

## 5. Topluluk ve kullanıcı deneyimleri (özet)

- GitHub `openai/codex` #41323 (28 Ağu 2026, açık): Windows'ta computer use'un masaüstünü
  yavaşlattığı, **mavi kaplama** gösterdiği ve tüm monitörü yakaladığı; pencereye özel yakalama
  ve imleci oynatmayan arka plan girdisi istendiği raporlanıyor. Bakımcı yanıtı görünmüyor [5].
  İlişkili sorunlar: #36091, #36677, #38710, #36603 (gecikme, ekran görüntüsü doğruluğu).
- Basın/rehberler (TechTimes, PCWorld, Daniel Vaughan rehberi): Windows sürümünün "foreground
  takeover" olduğu, kullanıcının aynı oturumda çalışamadığı; VM veya telefondan yönetim
  öneriliyor [3][18][19].
- MacStories ve Vaughan'ın macOS incelemesi: macOS arka plan modunda kullanıcının işini
  kesmeden çalıştığı gözlemleniyor [2][20].
- Windows-MCP README: "moving the physical mouse substantially ... returns control to the user"
  — yani o araç da gerçek imleci paylaşıyor [17].

> Topluluk kaynakları resmi değildir; tabloda resmi kaynakla çelişen bir iddia kullanılmadı.

---

## 6. Kanıt durumu (dürüst sınır)

| Kabul maddesi | Bu oturumdaki kanıt | Eksik / neden |
|---|---|---|
| Fare kontrolünü koruyan yöntemleri karşılaştır | §4 tablosu (17+ kaynakla); `test-kiti/Izle-GirdiGaspi.ps1` ölçüm aracı | Gerçek Windows ölçümü yapılmadı — bulutta Windows masaüstü yok |
| Arka plan ve minimize çalışmayı ayır | §3 tablosu; `UIA-ArkaPlan-Deneyi.ps1` iki ayrı senaryo (`Arkada`, `Kucultulmus`) | Senaryoların Windows'taki sonucu kullanıcı makinesinde koşturulmalı |
| Gerçek deney yapmadan "çalışır" deme | Rapor, UIA satırlarını "deneyle doğrulanacak" olarak işaretler; analiz aracı 13/13 birim testi geçti (sentetik veri, Linux PowerShell 7.4.6) | Birim testleri gerçek ölçüm değildir; `TEST-PLANI.md` sonuç tablosu "YAPILMADI" |

---

## 7. Kaynaklar

1. OpenAI — Computer Use (resmi doküman): https://learn.chatgpt.com/docs/computer-use (eski adres: https://developers.openai.com/codex/app/computer-use)
2. OpenAI — Use your computer with Codex: https://developers.openai.com/codex/use-cases/use-your-computer-with-codex
3. TechTimes — Codex Computer Use Now on Windows: Foreground Takeover: https://www.techtimes.com/articles/317531/20260601/openai-codex-computer-use-now-windows-foreground-takeover-europe-excluded.htm
4. Anthropic — Claude Code Desktop, "Let Claude use your computer": https://code.claude.com/docs/en/desktop
5. GitHub openai/codex #41323: https://github.com/openai/codex/issues/41323
6. Microsoft — SendInput: https://learn.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-sendinput
7. Microsoft — Desktops (input desktop): https://learn.microsoft.com/en-us/windows/win32/winstation/desktops
8. Anthropic — Computer use in the CLI: https://code.claude.com/docs/en/computer-use
9. Playwright — Running tests (headless varsayılan): https://playwright.dev/docs/running-tests
10. Microsoft — InvokePattern.Invoke: https://learn.microsoft.com/en-us/dotnet/api/system.windows.automation.invokepattern.invoke
11. pywinauto-recorder `click` (duration=-1 → WM_CLICK, imleç hareket etmez): https://pywinauto-recorder.readthedocs.io/en/stable/autosummary/pywinauto_recorder.player.click.html
12. Microsoft — Hyper-V on Windows: https://learn.microsoft.com/en-us/virtualization/hyper-v-on-windows/about/
13. Microsoft — Windows Sandbox: https://learn.microsoft.com/en-us/windows/security/application-security/application-isolation/windows-sandbox/
14. Microsoft Support — Experimental agentic features (Agent Workspace): https://support.microsoft.com/en-us/windows/ai/ai-features/experimental-agentic-features
15. Microsoft Research — UFO2: The Desktop AgentOS: https://www.microsoft.com/en-us/research/publication/ufo2-the-desktop-agentos/
16. GitHub microsoft/UFO: https://github.com/microsoft/UFO
17. GitHub CursorTouch/Windows-MCP: https://github.com/CursorTouch/Windows-MCP
18. PCWorld — Codex can control Windows 11 PCs: https://www.pcworld.com/article/3154677/openai-codex-can-finally-control-windows-11-pcs-on-its-own.html
19. Daniel Vaughan — Codex Computer Use on Windows: https://codex.danielvaughan.com/2026/06/04/codex-computer-use-windows-foreground-desktop-automation-agent-sandbox-permissions/
20. MacStories — Codex computer use incelemesi: https://macstories.net/notes/openais-new-codex-app-has-the-best-computer-use-feature-ive-ever-tested/
21. Microsoft — MSLLHOOKSTRUCT (LLMHF_INJECTED bayrağı): https://learn.microsoft.com/en-us/windows/win32/api/winuser/ns-winuser-msllhookstruct
22. microsoft/calculator kaynak kodu (AutomationId'ler): https://github.com/microsoft/calculator
