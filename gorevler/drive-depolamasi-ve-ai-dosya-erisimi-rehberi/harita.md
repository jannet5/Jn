# Harita: hedef → bağımlılıklar → A/B/C yolları → uygulama → test → teslim

```
HEDEF: Drive'ı (G:) depo olarak doğru kullanmak + AI'ya dosyayı doğru yoldan vermek
  │
  ├─ K1  G: sürücüsü ≠ yerel disk (önbellek farkı, yerel alan ≠ kota)   [kaynak satır 2, 14, 18]
  │    bağımlılık: R1, R2, R3, R12 (Google resmi)
  │    A: Akış modu, yalnız aktif dosyalar çevrimdışı          ← ÖNERİLEN (disk yine dolabilir: önbellek/yükleme)
  │    B: Yansıtma modu (tam yerel kopya)                      ← internet zayıfsa
  │    C: Önbelleği ContentCachePath ile 2. diske taşı          ← C: küçükse (Windows)
  │    uygulama: README §1; araç `onbellek`, `olc`
  │    kabul: yalnız belge + araç çalışması (kabul/04: Linux, OS önbelleği olası, DriveFS yok)
  │
  ├─ K2  Gradle/npm/git aktif çalışma dosyaları         [tam metin, kavramsal]
  │    bağımlılık: R4, R10, R11, T1, T2, T3
  │    A: Proje yerel diskte, Drive'a git bundle + kaynak ZIP    ← ÖNERİLEN (ek yazılım yok)
  │    B: Proje uzak git deposunda, Drive'a yalnız ürünler
  │    C: Hariç tutma sunan 3. parti senkron aracı               ← test edilmedi
  │    uygulama: araç `tara`; README §2
  │    kabul: kabul/03 (gerçek npm + Gradle projesinin Linux'ta taranması),
  │           kabul/03b (bundle → clone → npm ci → gradle build geri yükleme)
  │
  └─ K3  Yerel yol erişimi ≠ yükleme/bağlayıcı izni      [kaynak satır 3, 10, 17]
       bağımlılık: R5, R6, R7, R9, R12
       koşullar: mount | izin/sandbox | yükleme | bağlayıcı (OAuth) | köprü | herkese açık link
       A: PC'deki yerel AI aracı → yolu ver (izin/sandbox elverirse)
       B: Web sohbete yükle (servis limitleri)
       C: Servisin Drive bağlayıcısı (OAuth, kendi izinlerin)
       uygulama: araç `yol`; README §3 tablosu
       kabul: kabul/02 (YALNIZ bu Linux bulut konteyneri: 3 Windows yolu yok, DriveFS mount yok),
              kabul/05 (YALNIZ boş bağlayıcı sorgusu hatasız döndü; hesap/dosya testi yok)

TEST: araclar/test_drive_denetim.py — 15 birim testi (kabul/01)
TESLİM: (1) Jn deposu, dal claude/happy-davinci-gra3j0, push + geri okuma
        (2) Temiz, ürün-köklü bağımsız git deposu (yalnız bu klasör; Jn geçmişi ve özel kaynak yok)
            → bundle + ZIP + SHA-256, açılıp geri doğrulandı
        Özel kaynak.txt / gorev.md yalnız oturumun özel scratchpad'inde kalır.
```

## Kabul ölçütleri ve gerçek kapsam

| Ölçüt | Kanıt | Durum |
|---|---|---|
| K1: G:/önbellek/kota farkı somut ve kaynaklı | README §1, R1–R3, R12 | ✅ belge |
| K1: G: üzerinde ölçüm | — | ❌ yapılmadı: ortamda Windows/DriveFS yok. kabul/04 yalnız aracın Linux'ta çalıştığını gösterir |
| K2: Aktif klasörler gerçek projede tespit edildi | kabul/03: node_modules 630, .gradle 13 (4 .lock), build 4, .git 38 dosya | ✅ Linux'ta |
| K2: Proje G:'de çalıştırılınca sorun gözlemi | — | ❌ yapılmadı (aynı neden) |
| K2: Drive'a güvenli taşıma yolu çalışıyor | kabul/03b: bundle verify, clone, npm ci (70 paket), gradle build exit 0 | ✅ (ilk denemedeki hata kayıtlı) |
| K2: Hariç tutma ayarının durumu | R4 okundu (ayar geçmiyor), T1 | ⚠️ belirsiz; arayüz görülmedi |
| K3: Erişim koşulları ayrıştırıldı | README §3 tablosu | ✅ belge |
| K3: Bu bulut ortamı yol metninden dosya üretmedi | kabul/02 | ✅ yalnız bu ortam için |
| K3: Bağlayıcıyla gerçek hesap/dosya erişimi | kabul/05 yalnız boş sorgu | ❌ test edilmedi (kişisel veri yetki dışı) |
| Araç testleri | kabul/01: 15/15 OK | ✅ |
| Kalıcı teslim | calisma-gunlugu.md §6 | ✅ |

## Kırılma durumunda devam

- Resmi sayfa açılmazsa (R6 gibi): arama özeti ile işaretle ve iddiayı zayıf say.
- Push başarısız olursa: özel scratchpad'deki `teslim/` (bağımsız bundle + ZIP + SHA256SUMS) yedektir.
- Devam komutu:

  ```
  git fetch origin claude/happy-davinci-gra3j0 && git checkout claude/happy-davinci-gra3j0
  ```

  Ardından `calisma-gunlugu.md` içindeki "Yapılmayanlar" maddelerini kullanıcı Windows PC'sinde README komutlarıyla tamamla.
