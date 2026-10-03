# Birlikte test planı (Windows makinenizde)

Süre: ~20 dakika. Gerekenler: Windows 10/11, Codex (veya Claude Desktop) computer use açık,
Not Defteri. Betikler yalnız sayaç tutar; **hangi tuşa bastığınız kaydedilmez**.

Hazırlık (PowerShell'i bu klasörde açın):

```powershell
cd .\test-kiti
Unblock-File .\*.ps1
```

## T1 — Taban çizgisi (ajan yok)
```powershell
powershell -ExecutionPolicy Bypass -File .\Izle-GirdiGaspi.ps1 -Saniye 30
```
30 sn boyunca normal yazın/tıklayın. **Beklenen:** `KARAR: SENTETIK_GIRDI_GORULMEDI`, enjekte sayaçlar 0.

## T2 — Computer use sırasında siz de çalışın (asıl soru)
1. `Izle-GirdiGaspi.ps1 -Saniye 120` başlatın.
2. Codex'e örn. "Hesap Makinesi'ni aç ve 1234×5678 hesapla, sonra 10 kez tekrarla" deyin.
3. Bu sırada Not Defteri'ne geçip yazmaya çalışın, Hesap Makinesi'ni alta alın.

**Ölçülen:** enjekte tık/tuş sayısı, çakışma aralığı, ajan kaynaklı ön plan değişimi.
**Resmi dokümana göre beklenen:** `AJAN_GERCEK_GIRDIYI_KULLANDI`, ajan şüpheli ön plan değişimi > 0.
Ayrıca gözlem notu: yazdığınız harfler Hesap Makinesi'ne gitti mi? Ajan hata yaptı mı?

## T3 — Computer use sırasında fareye dokunmayın (kontrol)
T2'nin aynısı, ama siz hiçbir şeye dokunmayın. **Beklenen:** çakışma 0, görev daha hızlı ve hatasız biter.

## T4 — Fare kullanmayan otomasyon: "Arkada" senaryosu
```powershell
powershell -ExecutionPolicy Bypass -File .\UIA-ArkaPlan-Deneyi.ps1 -Senaryo Arkada -Json .\uia-arkada.json
```
Geri sayımda Not Defteri'ne tıklayın ve fareyi bırakın. **Kabul:** `KABUL : True`
(sonuç 3, imleç kıpırdamadı, ön plan değişmedi).

## T5 — Fare kullanmayan otomasyon: "Küçültülmüş" senaryosu
```powershell
powershell -ExecutionPolicy Bypass -File .\UIA-ArkaPlan-Deneyi.ps1 -Senaryo Kucultulmus -Json .\uia-kucuk.json
```
**Bilinmiyor — ölçülecek:** UWP Hesap Makinesi küçültülünce askıya alınabilir. `KABUL : False`
çıkarsa bu, "küçültülmüş ≠ arka plan" ayrımının kanıtıdır.

## İsteğe bağlı T6 — Ayrı masaüstü (B yolu)
Windows Pro ise Windows Sandbox'ı açın (Windows Özellikleri → Windows Sandbox), içinde
Codex computer use çalıştırın, ana masaüstünde T2'yi tekrarlayın. **Beklenen:** ana
masaüstündeki `Izle-GirdiGaspi` enjekte olay görmez (Sandbox'ın kendi girdisi vardır).

## Sonuç tablosu

| Test | Durum | Sonuç / KARAR | Not |
|---|---|---|---|
| T1 | YAPILMADI | — | Windows masaüstü gerekir |
| T2 | YAPILMADI | — | Windows + Codex computer use gerekir |
| T3 | YAPILMADI | — | 〃 |
| T4 | YAPILMADI | — | Windows gerekir |
| T5 | YAPILMADI | — | Windows gerekir |
| T6 | YAPILMADI | — | Windows Pro + Sandbox gerekir |

Çıkan CSV/JSON dosyalarını bir sonraki oturuma yüklerseniz rapor §3 ve §6 bu sonuçlarla güncellenir.

## Bilinen sınırlar
- Ajan girdiyi sanal bir HID sürücüsüyle üretirse olaylar "fiziksel" görünür; bu durumda
  ön plan değişimi ve görev penceresine düşen tuşlar gözlemi esas alınır.
- Betiği yönetici olmayan PowerShell'de çalıştırın; yönetici olarak çalışan uygulamalara
  ait olaylar yine görülür ama UIA deneyi yönetici pencerelerine erişemez (UIPI).
