# Telefonun kendiliğinden yeniden başlaması — teşhis paketi

Bu klasör, Android telefonun kendi kendine kapanıp açılmasının **uygulama /
yazılım** mı yoksa **donanım / güç** mü kaynaklı olduğunu kanıtla ayırmak
için hazırlandı. Yeni bir uygulama değil; telefona hiçbir şey yazmayan bir
kayıt toplama/analiz betiği ve adım adım kılavuz.

| Dosya | Ne |
|---|---|
| `teshis-kilavuzu.md` | **Buradan başla.** 3 adım: kayıt al → yorumla → güvenli testler |
| `arac/reboot_teshis.py` | `python reboot_teshis.py hepsi` → salt-okunur, izin listeli toplama; `rapor-yerel.md` + serbest metinsiz `paylasim.zip`/SHA-256 |
| `harita.md` | Hedef → bağımlılıklar → A/B/C yolları → uygulama → test → teslim; referanslar |
| `kabul/` | Emülatörde araç doğrulaması (maskelenmiş). **Kullanıcının telefonu için kabul değildir** |
| `testler/` | Birim + uçtan uca testler (sentetik veri; Linux/macOS `python3 testler/test_reboot_teshis.py`, Windows `py testler\test_reboot_teshis.py`). Sahte adb açık Python komutuyla enjekte edilir; PATH'teki gerçek adb'ye düşülürse tuzak yakalar |
| `calisma-gunlugu.md` | Ne yapıldı, hangi komut, hangi kaynak, sorunlar, doğrulamalar |

## Şu anki sonuç (dürüst durum)
- **Senin telefonun için kesin neden henüz bilinmiyor.** Bu bulut ortamına
  telefonun bağlı değil; gerçek reboot kaydı, uygulama listesi ve log elimde
  yok. Uydurmadım.
- Bu ortamda resmi `adb devices -l` çıktısı boş: fiziksel telefon bağlı
  görünmüyor. Emülatör sonuçları yalnız aracın doğrulamasıdır.
- Senden gereken gerçek girdi: telefon bağlıyken
  `python reboot_teshis.py hepsi --cikti kayit1 --goster <kendi.paket.adın>`
  sonucu olan `kayit1/paylasim.zip` (yerel raporu paylaşma) ve Güvenli Mod
  testinin sonucu (`--guvenli-mod kapanmadi|kapandi`).
- Gizlilik: tam getprop/logcat/dropbox/paket listesi ham hâliyle diske yazılmaz;
  ama `rapor-yerel.md`/`ozet-yerel.json` **kişisel veri içerir** (uygulama adları,
  kurulum zamanları, maskelenmiş hata metni) — yalnız yerel. Paylaşım paketinde
  serbest metin yoktur (yalnız izinli hata sınıfları/kategoriler), uygulamalar
  takma adlıdır; paket geçici klasörde üretilip geri okunarak tanımlı örüntüler için
  denetlenir ve yalnız geçerse yerine konur. Denetim sonucu bir garanti değildir.
