# PC ses kayıt kalitesi ve temizleme akışı

Yeni mikrofon almadan, mevcut PC mikrofonuyla daha temiz konuşma kaydı için hazırlanan paket. Kılavuz, Windows araçları ve ölçümlü test bir arada.

| Dosya | Ne işe yarar |
|---|---|
| [KILAVUZ.md](KILAVUZ.md) | **Buradan başlayın.** Kayıt öncesi, kayıt sırasında ve kayıt sonrası takip tabloları, ayrıca soruların cevapları. |
| `ses-temizleyici/` | Windows "düğmeleri": `Kurulum.bat`, `Kayit-Kontrol.bat`, `Ses-Temizle.bat`, `Ses-Temizle (dogal).bat`, `Ses-Temizle.ps1`, `Kurulum.ps1` |
| `audacity-makro/Ses-Temizle.txt` | Audacity 3.7.x makrosu. Sırası: 80 Hz kesim → Noise Reduction → 3:1 sıkıştırma → −16 LUFS → sınırlayıcı. |
| [arastirma.md](arastirma.md) | Audacity dahil alternatiflerin kaynaklı karşılaştırması ve kullanıcı deneyimleri. |
| [harita.md](harita.md) | Hedef → bağımlılıklar → A/B/C yolları → uygulama → test → teslim zinciri. |
| [test/TEST-RAPORU.md](test/TEST-RAPORU.md) | Ölçümlü kabul testi (DNSMOS, STOI, LUFS) ve neyin test edilemediği. |
| [calisma-gunlugu.md](calisma-gunlugu.md) | Yapılan her işin, komutun, kararın ve sorunun sıralı kaydı. |

## Sonuç

- **Kayıt ve kesme:** Audacity 3.7.x.
- **Temizlik:** `Ses-Temizle.bat`. İçinde DeepFilterNet3 var, yerelde ve internetsiz çalışır.
- **Ölçüm:** İki benzetim kaydında genel kalite (DNSMOS OVRL) şöyle çıktı:

  | | Kayıt 1 | Kayıt 2 |
  |---|---|---|
  | Ham | 1.19 | 1.08 |
  | Ses-Temizle | 2.96 | 2.46 |
  | Audacity makrosu | 1.67 | 1.14 |
  | Stüdyo referansı | 3.29 | 3.23 |

- **Sınır:** Prodüksiyon kalitesi sözü verilmez. Oda ve mikrofon mesafesi hâlâ en büyük etken.

`deep-filter.exe` bu depoda yoktur. Özel ZIP'te bulunur, ya da `Kurulum.bat` onu resmî GitHub sürümünden indirip SHA-256 ile doğrular:
- Kaynak: https://github.com/Rikorose/DeepFilterNet/releases/tag/v0.5.6
- SHA-256: `75e11fa16445f560cb6b021521ddb89e89270d13b83089705d98776f58fd7915`
- Lisans: MIT/Apache-2.0
