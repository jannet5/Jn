# Telefonun kendiliğinden yeniden başlaması — teşhis paketi

Bu klasör, Android telefonun kendi kendine kapanıp açılmasının **uygulama /
yazılım** mı yoksa **donanım / güç** mü kaynaklı olduğunu kanıtla ayırmak
için hazırlandı. Yeni bir uygulama değil; telefona hiçbir şey yazmayan bir
kayıt toplama/analiz betiği ve adım adım kılavuz.

| Dosya | Ne |
|---|---|
| `teshis-kilavuzu.md` | **Buradan başla.** 3 adım: kayıt al → yorumla → güvenli testler |
| `arac/reboot_teshis.py` | `python reboot_teshis.py hepsi` → telefondan salt-okunur kayıt + `rapor.md` |
| `harita.md` | Hedef → bağımlılıklar → A/B/C yolları → uygulama → test → teslim; referanslar |
| `kabul/` | Gerçek Android 11 (emülatör) üzerinde aracın çalıştırıldığı kabul raporları |
| `testler/` | Birim + uçtan uca testler (sentetik veri; `python3 testler/test_reboot_teshis.py`) |
| `calisma-gunlugu.md` | Ne yapıldı, hangi komut, hangi kaynak, sorunlar, doğrulamalar |

## Şu anki sonuç (dürüst durum)
- **Senin telefonun için kesin neden henüz bilinmiyor.** Bu bulut ortamına
  telefonun bağlı değil; gerçek reboot kaydı, uygulama listesi ve log elimde
  yok. Uydurmadım.
- Araç gerçek bir Android sisteminde (KVM'siz emülatör) çalıştırıldı ve orada
  gerçekten olan yumuşak yeniden başlamaları (system_server Watchdog) ve yan
  yüklenen test uygulamasını doğru buldu → `kabul/`.
- Senden gereken tek gerçek girdi: telefon bağlıyken
  `python reboot_teshis.py hepsi --cikti kayit1` çıktısı (`kayit1/rapor.md`)
  ve Güvenli Mod testinin sonucu ("Güvenli Mod'da da kapandı mı?").
