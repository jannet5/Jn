# Harita — hedef → bağımlılık → A/B/C → uygulama → test → teslim

```
HEDEF: Kullanıcıya RAM / Drive / bulut PC farkını doğru ve kanıtlı anlatmak,
       kendi PC'sinde doğrulayabileceği salt-okunur araç vermek
  │
  ├─ B1 Kaynak okuma (özel kaynak.txt, 5 satır) ──► 3 soru: [S1] RAM kiralama, [S2] Drive C: kadar dolu, [S3] sanal PC'yi kendi PC'den kullanma
  │
  ├─ B2 Araştırma (resmi + topluluk + akademik)
  │     S1 ← Microsoft page file belgesi, arXiv bellek ayrıştırma, ram-dl, elevenforum
  │     S2 ← Google Drive Yardım (10838124, 13401938, 17196458, 13470231), Cryptomator topluluğu
  │     S3 ← Windows App belgesi, Windows 365 fiyat sayfası, Shadow teklifler + SSS, AWS WorkSpaces, Azure (3. taraf)
  │
  ├─ B3 Yol seçimi (teslim biçimi)
  │     A: Yalnız rapor                         → kabul "somut çıktı/kanıt" ister, yetersiz
  │     B: Rapor + salt-okunur PowerShell teşhis betiği + testler  ← SEÇİLDİ
  │        (Windows'ta ek kurulum gerektirmez; hiçbir ayarı değiştirmez; S1 ve S2'yi kullanıcının PC'sinde kanıtlar)
  │     C: GUI uygulama / web servisi           → gereksiz teknoloji, araştırma görevini ürün inşasına çevirir
  │
  ├─ B4 Uygulama
  │     araclar/ram-drive-tani.ps1   (CIM sorguları + -GirdiJson test modu)
  │     araclar/kaynak-kontrol.sh    (kaynak URL erişim kontrolü)
  │
  ├─ B5 Test
  │     T1 araclar/testler/tani-test.ps1 — 3 örnek JSON, 13 kontrol (PowerShell 7.4.6, Linux)
  │     T2 PowerShell ayrıştırıcı ile sözdizimi hatası = 0
  │     T3 Windows dışı platformda canlı toplama açık hata verir (sessiz yanlış sonuç yok)
  │     T4 kaynak-kontrol.sh — 19 URL
  │     T5 (YAPILMADI) Gerçek Windows'ta canlı çalıştırma → kullanıcı adımı, README'de komut var
  │
  └─ B6 Kalıcı teslim
        • Public depo: claude/wizardly-bohr-coy8w8 dalı, yalnız bu klasör (özel kaynak yok)
        • Özel çalışma alanı: kaynak.txt + teslim ZIP + SHA-256 + geri okuma doğrulaması
```

## Kabul ölçütleri ve durum

| # | Ölçüt | Kanıt | Durum |
|---|---|---|---|
| K1 | Fiziksel RAM kiralama beklentisi doğru açıklandı | README §1, rapor KÖ1, betikte `RAM_FIZIKSEL`/`SANAL_BELLEK` + T1 testleri | ✅ |
| K2 | Bulut PC bağlantı + ücret karşılaştırıldı | README §3 tablo, rapor KÖ2 (resmi fiyatlar, istemci platformları, Türkiye notu) | ✅ (gerçek hesap/gecikme testi yok — ayrıca yazıldı) |
| K3 | Drive bellek diye sunulmadı; C: yansıtması açıklandı | README §2, rapor KÖ3, betikte `DRIVE_YANSITMA`/`DRIVE_BELLEK_DEGIL` + T1 testleri | ✅ (Windows'ta canlı çalıştırma yok — T5) |

## Yol kırılırsa
- Bir fiyat sayfası değişir veya kapanırsa `araclar/kaynak-kontrol.sh` "KONTROL" yazar. O zaman sayfa yeniden araştırılır ve tablo güncellenir.
- Betik Windows'ta hata verirse `-CiktiJson` ile ham veri alınır, `-GirdiJson` ile Linux'ta yeniden üretilir ve test eklenir.
- Devam komutu: [DEVAM.md](DEVAM.md)
