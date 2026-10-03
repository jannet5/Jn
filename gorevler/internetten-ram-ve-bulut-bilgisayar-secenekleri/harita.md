# Harita — hedef → bağımlılık → A/B/C → uygulama → test → teslim

```
HEDEF: Kullanıcıya RAM / Drive / bulut PC farkını doğru ve kanıtlı anlatmak,
       kendi PC'sinde doğrulayabileceği salt-okunur araç vermek
  │
  ├─ B1 Kaynak okuma (özel kaynak.txt, 5 satır) ──► 3 soru: [S1] RAM kiralama, [S2] Drive C: kadar dolu, [S3] sanal PC'yi kendi PC'den kullanma
  │
  ├─ B2 Araştırma (resmi + topluluk + akademik)
  │     S1 ← Microsoft page file belgesi, arXiv bellek ayrıştırma, ram-dl, elevenforum
  │     S2 ← Google Drive Yardım (10838124, 13401938, 17196458, 13470231), Google Workspace yönetici belgesi
  │          (DriveFS kayıt defteri: ContentCachePath / DefaultMountPoint), Cryptomator topluluğu
  │     S3 ← Windows App belgesi, Windows 365 fiyat + deneme şartları (kart, otomatik ücret), Shadow teklifler
  │          (canlı yeniden doğrulandı: Neo Lite/Neo/Power Lite/Power/Neo Pro/Power Pro) + SSS, AWS WorkSpaces, Azure (3. taraf)
  │
  ├─ B3 Yol seçimi (teslim biçimi)
  │     A: Yalnız rapor                         → kabul "somut çıktı/kanıt" ister, yetersiz
  │     B: Rapor + salt-okunur PowerShell teşhis betiği + testler  ← SEÇİLDİ
  │        (Windows'ta ek kurulum gerektirmez; ayar değiştirmez/silmez; S2 ilişkisini yalnız kanıt varsa doğrular, yoksa 'bilinmiyor' der)
  │     C: GUI uygulama / web servisi           → gereksiz teknoloji, araştırma görevini ürün inşasına çevirir
  │
  ├─ B4 Uygulama
  │     araclar/ram-drive-tani.ps1
  │       • Drive ↔ yerel disk kararı KANITA bağlı: ContentCachePath (kayıt defteri) + kapasite
  │         → DOGRULANDI | ESLESMIYOR | ADAY (yalnız kapasite benzerliği = bilinmiyor) | BILINMIYOR
  │       • Önbellek taraması sınırlı (200000 öğe / 20 sn), bağlantı izlemez; Tam | Kismi(EN AZ) | Yok
  │       • Tek yazma: -CiktiJson (var olan dosyayı -UzerineYaz olmadan ezmez)
  │     araclar/kaynak-kontrol.sh    (kaynak URL erişim kontrolü)
  │
  ├─ B5 Test
  │     T1 araclar/testler/tani-test.ps1 — 34 kontrol (7 örnek JSON + enjekte listeleyici + gerçek klasör + çıktı güvenliği)
  │        yanlış pozitif · eşit kapasiteli bağımsız diskler (kanıtlı/kanıtsız) · erişim hatası · öğe/süre sınırında kesilme
  │     T1b root olmayan kullanıcıyla gerçek chmod 000 erişim hatası (TANI_YASAK_KLASOR) — 35/35
  │     T1c mutasyon: eski "kapasite = kesin" davranışı geri konunca 3 test KALDI (testler anlamlı)
  │     T2 PowerShell ayrıştırıcı sözdizimi hatası = 0
  │     T3 Windows dışı platformda canlı toplama açık hata verir
  │     T4 kaynak-kontrol.sh
  │     T5 (YAPILMADI) Gerçek Windows + Drive for desktop + kullanıcı PC'si; hesap/ödeme/gecikme — yapılmış sayılmaz
  │
  └─ B6 Kalıcı teslim
        • Public depo: claude/wizardly-bohr-coy8w8 dalı, yalnız bu klasör (özel kaynak yok)
        • Ürün-köklü ZIP: uzak commit'ten `git archive` ile yalnız bu klasör (kişisel kaynak / Git geçmişi YOK)
          + tam SHA-256 + uzak klonla birebir karşılaştırma
        • Kişisel kaynak.txt yalnız özel scratchpad'de, ZIP'e ve depoya girmez
```

## Kabul ölçütleri ve durum

| # | Ölçüt | Kanıt | Durum |
|---|---|---|---|
| K1 | Fiziksel RAM kiralama beklentisi doğru açıklandı | README §1, rapor KÖ1, betikte `RAM_FIZIKSEL`/`SANAL_BELLEK` + T1 testleri | ✅ |
| K2 | Bulut PC bağlantı + ücret karşılaştırıldı | README §3 tablo, rapor KÖ2: tam paket adı, saat sınırı, ülke/sayfa, vergi hariç, tarih, kaynak; Windows 365 kart + otomatik ücret şartı; Türkiye'de ülke uygunluğu ile gecikme ayrı | ✅ belge · ❌ hesap/ödeme/gecikme denenmedi |
| K3 | Drive bellek diye sunulmadı; kişisel teşhis kanıtsız kesinleştirilmedi | README §2, rapor KÖ3, betikte `DRIVE_YANSITMA_DOGRULANDI/ADAY/ESLESMIYOR/ILISKI_BILINMIYOR`, `DRIVE_KOTA`, `DRIVE_BELLEK_DEGIL` + T1 | ✅ mantık · ❌ kullanıcının Windows'unda çalıştırılmadı (T5) |

## Yol kırılırsa
- Bir fiyat sayfası değişir veya kapanırsa `araclar/kaynak-kontrol.sh` "KONTROL" yazar. O zaman sayfa yeniden araştırılır ve tablo güncellenir.
- Betik Windows'ta hata verirse `-CiktiJson` ile ham veri alınır, `-GirdiJson` ile Linux'ta yeniden üretilir ve test eklenir.
- Devam komutu: [DEVAM.md](DEVAM.md)
