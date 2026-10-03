# Harita — hedef → bağımlılıklar → A/B/C → uygulama → test → teslim

```
HEDEF
  "AI computer use çalışırken ben de bilgisayarı aynı anda kullanabilir miyim?
   Kullanamıyorsam neden, yerine ne kullanırım?" sorusuna kanıtlı cevap + kendi
   makinemde doğrulayabileceğim deney.
        │
        ▼
BAĞIMLILIKLAR
  D1 Güncel resmi doküman (OpenAI Computer Use, Anthropic computer use, Microsoft Win32/UIA)  ── karşılandı (rapor §7: 1,4,6,7,8,10,21)
  D2 Topluluk deneyimleri (GitHub issue, basın, rehberler)                                    ── karşılandı (rapor §5)
  D3 Ölçüm yöntemi: "fareyi/ön planı kim kullandı" nesnel olarak nasıl ölçülür?                ── LLMHF_INJECTED bayrağı + ön plan örnekleme
  D4 Fare kullanmayan otomasyon örneği                                                         ── UI Automation InvokePattern (Hesap Makinesi)
  D5 Gerçek Windows masaüstü                                                                   ── YOK (bulut Linux) → deney kullanıcı makinesine devredildi
        │
        ▼
YOLLAR (kullanıcının sorusuna cevap olarak)
  A  Fareye hiç dokunmayan yapılandırılmış yollar: MCP/plugin, CLI/script, headless tarayıcı, UIA
       + Aynı anda kullanım kesin; − her uygulamada yok
  B  Ajanı ayrı masaüstüne koy: VM / Windows Sandbox / ayrı PC / Agent Workspace (önizleme) / UFO² PiP
       + Computer use aynen çalışır ve siz paralel çalışırsınız; − kurulum, kaynak, bazıları önizleme
  C  Aynı oturumda computer use (bugünkü Codex Windows davranışı)
       + Kurulum yok; − fare/ön plan gaspı, siz beklersiniz
  Seçim: Önce A (mümkünse), GUI şartsa B, kısa işlerde C. Gerekçe: resmi dokümanlar
  A'yı öncelikli önerir [1][8]; B, OpenAI'nin Windows için önerdiği güvenli kalıptır [3].
        │
        ▼
UYGULAMA (bu görevin teslim ettiği gerçek araçlar — hayali ürün değil, araştırmanın deney aracı)
  U1 rapor.md                       → cevap, neden, arka plan/minimize/ayrı oturum ayrımı, A/B/C tablosu, 22 kaynak
  U2 test-kiti/Izle-GirdiGaspi.ps1  → computer use sırasında enjekte vs fiziksel fare/klavye + ön plan değişimi (tuş içeriği kaydedilmez)
  U3 test-kiti/Analiz-Et.ps1        → CSV özeti: KARAR, çakışma, ajan kaynaklı ön plan değişimi
  U4 test-kiti/UIA-ArkaPlan-Deneyi.ps1 → "Arkada" ve "Kucultulmus" senaryolarında imleç/ön plan değişmeden işlem yapılabiliyor mu
  U5 TEST-PLANI.md                  → kullanıcıyla birlikte koşturulacak T1–T5 protokolü + boş sonuç tablosu
        │
        ▼
TEST / DOĞRULAMA
  V1 PowerShell parser: 3 betik, 0 sözdizimi hatası                         ── YAPILDI (Linux pwsh 7.4.6)
  V2 Gömülü C# derlemesi (Add-Type)                                          ── YAPILDI (2/2 derlendi)
  V3 Analiz mantığı birim testleri (sentetik CSV, 13 doğrulama)              ── YAPILDI 13/13 GEÇTİ → kanit/birim-testleri.txt
  V4 Calculator AutomationId'leri resmi kaynak kodla eşleşiyor               ── YAPILDI (microsoft/calculator XAML)
  V5 Rapor bağlantıları erişilebilir                                         ── YAPILDI → kanit/baglanti-kontrolu.txt
  V6 Windows'ta canlı ölçüm (T1–T5)                                          ── YAPILMADI: Windows masaüstü gerekir (kullanıcı makinesi)
        │                        │
        │   yol kırılırsa:       └─ V6 sonucu UIA "Kucultulmus" senaryosunda KABUL=False çıkarsa
        │                           → rapor §3'te "küçültülmüş UWP askıya alınıyor" olarak işaretle,
        │                           öneriyi B (ayrı masaüstü) yönüne kaydır.
        │                           Izle-GirdiGaspi enjekte olay göremezse (ör. sanal HID sürücüsü)
        │                           → ön plan değişimi + imleç sıçraması ölçütüne dayan.
        ▼
KALICI TESLİM
  K1 Görev dalı claude/epic-einstein-m97a7d → gorevler/arka-planda-bilgisayar-kullanimi-arastirmasi/ (özel kaynak/görev dosyaları HARİÇ)
  K2 Özel ZIP (özel kaynak.txt + gorev.md dahil tüm çalışma) + SHA-256 + açıp yeniden hash ile geri okuma
  K3 Push sonrası uzak daldan geri okuma (git fetch + diff)
```

## Kaynak satırı → kabul eşlemesi

| Kaynak isteği | Harita düğümü | Kanıt |
|---|---|---|
| Fare benden alınıyor mu, aynı anda kullanabilir miyim? | D1, U1 §1–2 | rapor.md, kaynak [1][3][6][7] |
| Pencereyi alta alırsam / arkada tıklar mı? | U1 §3, U4 | rapor.md §3, UIA deneyi (Windows'ta koşturulacak) |
| Önce araştır, sonra birlikte test edelim | U5, V6 | TEST-PLANI.md (T1–T5) |
| Computer use neden var, yerine ne kullanırım? | Yollar A/B/C, U1 §4 | rapor.md §4 tablosu |
| Gerçek deney olmadan "çalışır" deme | V6 | TEST-PLANI.md sonuç tablosu "YAPILMADI" |
