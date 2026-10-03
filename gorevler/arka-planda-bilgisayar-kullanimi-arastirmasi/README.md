# Arka planda bilgisayar kullanımı araştırması

**Soru:** AI "computer use" (Codex/Claude) çalışırken bilgisayarı aynı anda kullanabilir miyim?

**Kısa cevap (Ekim 2026):** Windows'ta **aynı oturumda hayır** — Codex computer use imleci
hareket ettirir, yazar ve ön planı alır; hedef pencere görünür kalmalıdır (OpenAI resmi dokümanı).
Pencereyi alta almak ajanın işini bozar veya pencereyi tekrar öne getirir. macOS'ta ise arka plan
modu var. Aynı anda çalışmak için: fare kullanmayan yollar (MCP/plugin, CLI, headless tarayıcı,
UI Automation) ya da ajanı ayrı masaüstünde çalıştırmak (VM, Windows Sandbox, ayrı PC).
Ayrıntı ve 22 kaynak: [rapor.md](rapor.md).

| Dosya | İçerik |
|---|---|
| [rapor.md](rapor.md) | Cevap, teknik neden, arka plan / küçültülmüş / ayrı oturum ayrımı, alternatifler tablosu, kaynaklar |
| [harita.md](harita.md) | Hedef → bağımlılık → A/B/C → uygulama → test → teslim zinciri |
| [TEST-PLANI.md](TEST-PLANI.md) | Windows'ta birlikte koşturulacak T1–T6 deneyleri ve sonuç tablosu |
| `test-kiti/Izle-GirdiGaspi.ps1` | Computer use sırasında enjekte/fiziksel girdi ve ön plan değişimini ölçer (tuş içeriği kaydetmez) |
| `test-kiti/Analiz-Et.ps1` | Ölçüm CSV'sini özetler ve KARAR verir |
| `test-kiti/UIA-ArkaPlan-Deneyi.ps1` | Fareyi oynatmadan Hesap Makinesi'nde işlem: "Arkada" ve "Küçültülmüş" senaryoları |
| `test-kiti/testler/Calistir-Testler.ps1` | Analiz mantığı ve C# derleme birim testleri |
| `kanit/` | Birim testi çıktısı ve bağlantı kontrolü |
| [calisma-gunlugu.md](calisma-gunlugu.md) | Yapılanların sıralı günlüğü |

**Doğrulama durumu:** sözdizimi (3/3), C# derleme (2/2), birim testleri (13/13, sentetik veri)
bulutta geçti. **Windows'taki canlı deneyler yapılmadı** — Windows masaüstü gerektirir; bkz. TEST-PLANI.md.
