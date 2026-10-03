# Harita: hedef → bağımlılıklar → A/B/C yolları → uygulama → test → teslim

```
HEDEF: Drive'ı (G:) depo olarak doğru kullanmak + AI'ya dosyayı doğru yoldan vermek
  │
  ├─ K1  G: sürücüsü ≠ SSD (önbellek farkı)            [kaynak satır 2, 14, 18]
  │    bağımlılık: R1, R2, R3 (Google resmi)
  │    A: Akış modu + "çevrimdışı" yalnız aktif dosyalar      ← ÖNERİLEN
  │    B: Yansıtma modu (tam yerel kopya, SSD dolar)          ← internet zayıfsa
  │    C: Önbelleği ContentCachePath ile 2. diske taşı         ← C: küçükse
  │    uygulama: README §1, araç `onbellek` + `olc`
  │    kabul: kabul/04 (yerel referans ölçümü) + Windows adımları README'de
  │
  ├─ K2  Gradle/npm/git aktif çalışma dosyaları         [tam metin, kavramsal]
  │    bağımlılık: R10, R11, T1, T2, T3
  │    A: Proje C:\dev'de, Drive'a git bundle + kaynak ZIP     ← ÖNERİLEN (ek yazılım yok)
  │    B: Proje GitHub'da, Drive'a yalnız ürünler (APK/ZIP)
  │    C: Hariç tutmalı 3. parti senkron (Insync/odrive)       ← test edilmedi
  │    uygulama: araç `tara`; README §2
  │    kabul: kabul/03 (gerçek npm 70 paket + Gradle 8.14.3 derleme taraması),
  │           kabul/03b (bundle → clone → npm ci → gradle build geri yükleme)
  │
  └─ K3  Web AI yerel yol ≠ bulut yükleme/bağlayıcı izni [kaynak satır 3, 10, 17]
       bağımlılık: R5, R6, R7, R9
       A: Yerel AI aracı → G: yolunu ver                      ← kod/klasör işleri
       B: Web sohbete yükle                                  ← tek dosya, limitler dahilinde
       C: Servisin Drive bağlayıcısı (OAuth)                 ← Drive'da duran belgeler
       (link: yalnız herkese açık + servis açabiliyorsa)
       uygulama: araç `yol`; README §3 tablo
       kabul: kabul/02 (bulut ajan G:\ ve C:\ yollarını açamadı, exit 2),
              kabul/05 (aynı ajan Drive bağlayıcısıyla API sorgusu yaptı)

TEST/BUILD: araclar/test_drive_denetim.py — 13 birim testi (kabul/01)
TESLİM: git dalı claude/happy-davinci-gra3j0 → push → uzaktan geri okuma;
        özel ZIP + git bundle + SHA-256 (özel scratchpad, public depoya değil)
```

## Kabul ölçütleri

| Ölçüt | Kanıt | Durum |
|---|---|---|
| K1: G:/SSD önbellek farkı somut anlatıldı | README §1, kaynaklar R1–R3 | ✅ |
| K1: çalıştırılmış ölçüm | kabul/04 (yerel disk referansı; `onbellek` Linux'ta "desteklenmeyen" döndü) | ⚠️ Windows G: ölçümü yapılmadı — ortamda Drive for desktop yok |
| K2: Gradle/npm aktif dosyaları gerçek projede tespit | kabul/03: node_modules 630, .gradle 13 (4 .lock), build 4, .git 26 dosya | ✅ |
| K2: Drive'a güvenli taşıma yolu çalışıyor | kabul/03b: bundle verify, clone, npm ci (70 paket), gradle build exit 0 | ✅ (ilk denemedeki hata kayıtlı) |
| K3: Yerel yol ≠ bulut izni ayrımı | kabul/02 + kabul/05 | ✅ |
| Araç testleri | kabul/01: 13/13 OK | ✅ |
| Kalıcı teslim | uzak dal + geri okuma; ZIP SHA-256 | calisma-gunlugu.md sonu |

## Kırılma durumunda devam

- Bir resmi sayfa açılmazsa (R6 gibi): arama özeti + sayfa başlığı ile işaretle, iddiayı zayıf say.
- Push başarısız olursa: özel scratchpad'deki `teslim/` ZIP + `.bundle` kalıcı yedektir;
  devam komutu: `git fetch origin claude/happy-davinci-gra3j0 && git checkout claude/happy-davinci-gra3j0`
  ve `gorevler/drive-depolamasi-ve-ai-dosya-erisimi-rehberi/calisma-gunlugu.md` son bölümünden sürdür.
