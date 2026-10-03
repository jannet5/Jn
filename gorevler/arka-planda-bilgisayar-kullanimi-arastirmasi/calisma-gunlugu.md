# Çalışma günlüğü

Görev: "Arka planda bilgisayar kullanımı araştırması". Ortam: Claude Code bulut oturumu
(Linux, Windows masaüstü yok). Tarih: 2026-10-03 (UTC). Dal: `claude/epic-einstein-m97a7d`.

## 1. Ne istendi
Kullanıcı, Codex computer use çalışırken fareyi/ekranı kimin kullandığını, aynı anda kendi
işini yapıp yapamayacağını, ajanın açtığı pencereyi alta almanın sorun olup olmadığını ve
computer use'un neden var olduğunu / yerine ne kullanılabileceğini soruyor. Önce araştırma,
sonra birlikte test isteniyor. Görev tanımı ek olarak: fare kontrolünü koruyan yöntemleri
karşılaştır; arka plan ile küçültülmüş çalışmayı ayır; gerçek deney yapmadan "çalışır" deme;
bağlı harita, A/B/C alternatifler, kalıcı ve doğrulanmış teslim.

Gizlilik kararı: Kullanıcının tam sohbet metni (`kaynak.txt`) ve `gorev.md` **yalnız özel
çalışma alanında ve özel ZIP'te** tutuldu; public depoya konmadı. Bu günlük ve rapor sohbetten
alıntı yapmaz, soruları genel ifadelerle anlatır.

## 2. Adımlar, araçlar ve komutlar

1. **Kaynakları kaydetme** — `kaynak.txt` ve `gorev.md` mesajdaki tam içerikle özel çalışma
   alanına (scratchpad/ozel/) yazıldı. Windows yolu (`C:\Users\...`) okunmaya çalışılmadı;
   içerik mesajda verilmişti.
2. **Web araştırması** — `WebSearch` (standart + genişletilmiş) ve `WebFetch` ile:
   - OpenAI Computer Use dokümanı: eski adres 308 ile `learn.chatgpt.com/docs/computer-use`'a
     yönlendirdi; yeni adres okundu. Kilit cümleler: "On Windows, Computer Use runs on the active
     desktop. It can't operate in the background while you keep using the same Windows session.",
     "expect ChatGPT to move the pointer, type, and take over the foreground", "keep the target app
     visible on the active desktop", "prefer that structured integration" (MCP/plugin).
   - Anthropic: `code.claude.com/docs/en/computer-use` ve `desktop` sayfaları — Windows'ta
     computer use var ama arka plan yalnız macOS; arka plan dışında diğer pencereler gizlenir;
     önce connector/MCP → shell → tarayıcı → en son computer use.
   - Microsoft: SendInput ("serially into the keyboard or mouse input stream"), Desktops ("only
     one of these desktops at a time is active ... input desktop"), MSLLHOOKSTRUCT (LLMHF_INJECTED),
     InvokePattern.Invoke, Windows Sandbox, Experimental agentic features (Agent Workspace ayrı
     Windows oturumunda, ayrı hesapla; Insider önizlemesi).
   - Topluluk: GitHub `openai/codex` #41323 (Windows'ta mavi kaplama, gecikme, arka plan girdisi
     talebi; açık, bakımcı yanıtı yok), TechTimes, PCWorld, Daniel Vaughan rehberi, MacStories,
     Windows-MCP README (o da gerçek imleci oynatıyor), Microsoft UFO² (PiP sanal masaüstü).
3. **Karar: ne teslim edilecek?** Bu bir araştırma görevi; hayali bir uygulama yazılmadı.
   Ancak "önce araştır sonra test edelim" isteği ve "deneysiz iddia kurma" kabulü için,
   kullanıcının kendi makinesinde koşturabileceği **ölçüm araçları** yazıldı:
   - `Izle-GirdiGaspi.ps1`: düşük seviye fare/klavye kancası ile enjekte (yazılımsal) ve
     fiziksel olayları ayrı sayar, ön plan penceresini örnekler. Tuş kodu saklanmaz.
   - `Analiz-Et.ps1`: CSV'den çakışma, ajan kaynaklı ön plan değişimi, KARAR üretir.
   - `UIA-ArkaPlan-Deneyi.ps1`: Hesap Makinesi'nde UIA InvokePattern ile 1+2=; imleç ve ön
     plan önce/sonra karşılaştırılır; "Arkada" ve "Kucultulmus" ayrı senaryolar.
   Neden PowerShell: Windows'ta kurulumsuz çalışır (Python/pywinauto kurulumu gerektirmez).
4. **AutomationId doğrulaması** — `curl` ile `raw.githubusercontent.com/microsoft/calculator`
   XAML dosyaları indirildi; `num1Button`, `num2Button`, `plusButton`, `equalButton`,
   `clearButton`, `CalculatorResults` kimliklerinin gerçek olduğu görüldü.
5. **Test ortamı** — Bulutta PowerShell yoktu. `curl` ile resmi PowerShell 7.4.6 Linux paketi
   (github.com/PowerShell/PowerShell releases) scratchpad'e indirildi; depoya eklenmedi.
6. **Doğrulamalar**
   - PowerShell parser ile 3 betik: 0 sözdizimi hatası.
   - Gömülü C# kodları `Add-Type` ile derlendi (2/2). (user32 çağrıları Linux'ta çalıştırılmadı.)
   - `testler/Calistir-Testler.ps1`: 13/13 GEÇTİ — sentetik CSV senaryoları (ajansız, ajan
     pencereyi öne getiriyor + çakışma, hatalı girdi, CSV gidiş-dönüş, C# derleme).
     Çıktı: `kanit/birim-testleri.txt`.
   - Rapor bağlantıları `curl -L` ile denetlendi: `kanit/baglanti-kontrolu.txt`.
     github.com ve microsoft.com/research curl'e 403 verdi (bot engeli); bu sayfalar aynı
     oturumda WebFetch ile başarıyla okunmuştu.

## 3. Sorunlar ve çözümler
| Sorun | Çözüm |
|---|---|
| OpenAI doküman adresi 308 yönlendirme | Yeni adres (`learn.chatgpt.com`) okundu, raporda ikisi de verildi |
| `pywinauto` resmi HowTo sayfasında click/click_input bilgisi yok | pywinauto-recorder dokümanı kaynak gösterildi; asıl deney UIA InvokePattern ile kuruldu (Microsoft resmi API) |
| UFO² PiP bilgisi GitHub README'de yok | Microsoft Research yayın sayfası/makale özeti kaynak alındı; "araştırma projesi" olarak işaretlendi |
| Bulutta Windows yok | Canlı deney yapılmadı; TEST-PLANI.md sonuç tablosu "YAPILMADI"; araçlar Linux'ta derleme/birim testinden geçirildi |
| Hesap Makinesi'ni betiğin açması pencereyi öne getirir | İlk açılış ölçüme dahil edilmedi; "Arkada" senaryosunda geri sayımla kullanıcı başka pencereye geçer |

## 4. Sonuç ve kalan iş
- Tamamlanan: araştırma raporu, harita, test kiti, test planı, kanıt dosyaları, kalıcı teslim.
- Kalan (kullanıcı makinesi gerekir): T1–T6 Windows deneyleri. CSV/JSON çıktıları bir sonraki
  oturuma yüklenirse rapor §3/§6 güncellenecek.
- USB vb. donanım bağlantısı bu görev için **gerekmez**.

## 5. Teslim
- Public dal: `gorevler/arka-planda-bilgisayar-kullanimi-arastirmasi/` (özel kaynaklar hariç).
- Özel ZIP: tüm dosyalar + `ozel/kaynak.txt` + `ozel/gorev.md`, SHA-256 ile; ZIP açılıp her
  dosyanın hash'i yeniden hesaplanarak geri okuma doğrulandı (değerler sohbet yanıtında).
