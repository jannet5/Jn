# Yönetim Planı — Bulutta Sıfırdan Android Yapılacaklar Uygulaması

Bu bir devir değil, **sıfırdan yeni bir proje**. Her şey bulutta (lokal makine yok) çalışır.
Aşağıdaki dosyalar, ChatGPT Pro (Brave) → Work bölümünde açılacak sohbetlere **kopyala-yapıştır** verilir.

## Sohbet yapısı

| Sohbet | Yapıştırılacak dosya | Ne yapar |
|---|---|---|
| **S1 – Ana sohbet** (tek, uzun ömürlü) | `01_ANA_SOHBET.md` | İnternet araştırması, not defteri, 3 harita tanımı, karşılaştırma, 4. harita (kümülatif), APK + Drive |
| **S2 – Harita A** (ayrı) | `02_HARITA_A.md` | Sadece A kombinasyonuyla uygulamayı yapar, APK üretir |
| **S3 – Harita B** (ayrı) | `03_HARITA_B.md` | Sadece B kombinasyonuyla yapar |
| **S4 – Harita C** (ayrı) | `04_HARITA_C.md` | Sadece C kombinasyonuyla yapar |

Ortak ürün tanımı: `10_URUN_SPEC.md` — her sohbete **ekle** (her prompt buna atıf yapar).

## Akış (sıra önemli)

1. **S1** açılır → araştırma + `NOTLAR/` klasörü + 3 harita (A/B/C) tanımı çıkar.
2. S1 çıktısındaki "Harita A/B/C brief"leri, S2/S3/S4 promptlarındaki `{{BRIEF}}` yerine yapıştırılır. S2–S4 **paralel** çalışır.
3. S2–S4 bitince her biri: APK'yı Drive'a atar + `RAPOR.md` (ne kullandı, ne işe yaradı, ne sıkıcı/işe yaramaz çıktı) üretir.
4. Raporlar S1'e geri yapıştırılır → S1 karşılaştırır, **4. haritayı** (üçünün en iyilerinin kümülatif birleşimi) yapar, 4. APK'yı Drive'a atar.
5. Sen 4 APK'yı Drive'dan indirip telde denersin, hangisi en iyi diye karar verirsin.

## Not — varsayım
"Bazı işler tek sohbette, bazı haritalar ayrı sohbetlerde" isteğini şöyle yorumladım: araştırma/yönetim/birleştirme/4. harita = tek ana sohbet; A, B, C = ayrı sohbetler. Farklıysa söyle, tabloyu düzeltirim.

## Dürüst sınır
Ben (bu oturum) senin Brave/ChatGPT hesabında sohbet açamam; bu dosyalar hazır yapıştırma paketi. Yönetimi bu plan + S1 üzerinden yaparsın; istersen bana raporları getir, karşılaştırmayı ben de yaparım.
