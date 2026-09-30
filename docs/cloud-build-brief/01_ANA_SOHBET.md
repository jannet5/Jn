# S1 — ANA SOHBET (Yönetici + Araştırmacı + Birleştirici)

> Bu prompt'un başına `10_URUN_SPEC.md` içeriğini yapıştır.

Sen bu projenin baş mühendisi/yöneticisisin. Ürün spesifikasyonu yukarıda. **Sıfırdan başlıyorsun**, önceden yapılmış bir kod yok. Her şey bulutta.

## Aşama 1 — İnternet araştırması (kendin düşünmek zorunda kalma; önce başkalarının yaptığına bak)
İnternette **gerçekten ara** ve şunları bul:
- Benzer açık kaynak Android görev/alışveriş listesi uygulamaları (GitHub, F-Droid). Mimarilerini, sürükle-sırala, üstünü çizme, bubble/overlay yaklaşımlarını incele. Kodu **anla**, notla; birebir yazmak zorunda değilsin.
- Yüzen balon (bubble/overlay) için en iyi teknik (SYSTEM_ALERT_WINDOW vs Android Bubbles API vs foreground service), sürüm kısıtları, pil/izin sorunları.
- Hızlı liste + drag&drop için en iyi kütüphane/yaklaşım (Compose reorderable, ItemTouchHelper vb.) ve performans ipuçları.
- İnsanların "AI ile mobil uygulama yaparken işe yarayan araçlar" hakkında yazdıkları: MCP sunucuları, skill'ler, ajan iş akışları, UI üretim/tasarım araçları (ör. tasarım sistemleri, UI kütüphaneleri, ekran görüntüsü tabanlı geri bildirim), CI/APK üretim ve Drive'a yükleme yolları, Play Store hazırlık kontrol listeleri.
- Kaliteli, "slop olmayan" mobil arayüz referansları ve tasarım sistemleri.

Her kaynağı `NOTLAR/kaynaklar.md` dosyasına **link + 2-3 cümle: ne öğrendim, projeye nasıl uyar** olarak yaz. Klasör yapısı:
```
NOTLAR/
  kaynaklar.md      # tüm linkler + çıkarımlar
  sistemler.md      # bulunan repoların/sistemlerin mimari özeti
  araclar.md        # araç/MCP/skill/ajan adayları, artı-eksi
  fikirler.md       # "bunu ve şunu birleştirebiliriz" fikirleri
  kararlar.md       # marka, logo, tasarım, teknik kararlar ve gerekçeleri
  harita_A.md / harita_B.md / harita_C.md / harita_D.md
  sonuc_karsilastirma.md
```
Bağlamını kaybetmemek için düzenli olarak bu dosyalara yaz ve oradan oku (tek seferde her şeyi aklında tutma).

## Aşama 2 — Üç farklı harita (kombinasyon)
Araştırmadan, birbirinden **gerçekten farklı** 3 araç/yaklaşım kombinasyonu üret (ör. farklı UI stack'i, farklı tasarım kaynağı, farklı ajan/araç düzeni, farklı bubble tekniği). Her harita için:
- Kullanılacak araçlar/MCP/skill/ajan düzeni, teknoloji yığını (Kotlin+Compose / diğer), tasarım referansları,
- Adım adım akış, riskler, "başarı nasıl ölçülür" kriteri.
Bunları `harita_A/B/C.md` ve her biri için **bağımsız çalıştırılabilir "BRIEF"** olarak çıkar. Bu BRIEF'ler ayrı sohbetlere (S2/S3/S4) verilecek; her BRIEF kendi başına anlaşılır olmalı ve ürün spec'ine atıf yapmalı.

**Bana (kullanıcıya) BRIEF'leri hazır ver, sonra dur ve S2–S4 raporlarını bekle.** Kendin A/B/C'yi uygulama.

## Aşama 3 — Karşılaştırma ve 4. Harita (kümülatif)
Raporlar gelince: hangi araç/yaklaşım gerçekten işe yaradı, hangisi sıkıcı/boş çıktı — `sonuc_karsilastirma.md` (kanıtla, puanla). Sonra **Harita D**: üç haritanın kazanan parçalarını kümülatif birleştir, uygulamayı **sen** yap.

## Aşama 4 — Teslim
- Release APK (+AAB), tüm testler/kanıtlar, Drive'a yükleme + link.
- Play Store hazırlık paketi (metin, izinler, gizlilik taslağı).
- Sonda: 4 haritanın kısa tablosu (araçlar, süre, sonuç) ve senin tavsiyen.

Kurallar: uydurma yok — doğrulanmayanı doğrulanmadı yaz. Marka/logo/renk/arayüz kararları senin; gerekçele. Gizli anahtarları açığa çıkarma.
