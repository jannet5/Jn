# Harita karşılaştırması (A / B / C) — ölçülen sonuçlar

Test ortamı: KVM'siz yazılım emülatörü, Android 8.0 (API 26, x86_64), adb ile dokunuş enjeksiyonu. Başlangıç süreleri tek örnek ve emülatörde göreli; gerçek telefon değil.

| | A (Compose) | B (Views + Room) | C (sıfır bağımlılık) |
|---|---|---|---|
| APK (release, minify) | 1.264.971 B (1,21 MiB) | 849.610 B (830 KB) | 34.404 B (33,6 KB) |
| AAB | 3,04 MiB | 1,32 MiB | 53 KB |
| Birim test | 35 (4'ü Roborazzi ekran görüntüsü) | 25 | 27 |
| Soğuk açılış (`am start -W`, emülatör) | 8481 ms | 8449 ms | 5558 ms |
| Ekleme | çalıştı | çalıştı; ilk eklemeden sonra odak kayboldu (her seferinde alana dokunmak gerekti) | çalıştı; odak korundu, art arda hızlı ekleme |
| Üstünü çizme | çalıştı (turuncu dolu işaret) | çalıştı | çalıştı |
| Silme (×) | çalıştı | çalıştı | çalıştı |
| Sürükle-sırala | tutamaçla çalıştı | tutamaçla çalıştı | tutamaçla çalıştı |
| Kalıcılık (kapat-aç) | doğrulanmadı (çökme yüzünden akış kesildi) | çalıştı | çalıştı |
| Balon | **ÇÖKTÜ**: `getCurrentWindowMetrics()` API 30+, minSdk 26 | çalıştı; dokununca ana ekranın üstünde **yüzen liste paneli** açıldı | çalıştı; dokununca uygulama açıldı |
| Geri al şeridi | ekran görüntüsünde yakalanamadı (süre doldu) | test edilmedi | test edilmedi |
| Görsel kalite (ekran görüntüsü) | en iyi | iyi, tipografi güçlü | sade, temiz |

## Çıkarımlar
- **Birim testleri bunu yakalayamadı:** A'nın 35 testi geçti ama balon API 26'da çöküyor. Cihaz testi şart.
- **Balon:** B'nin panel yaklaşımı TXT'deki "balondan direkt listeye" isteğine en yakın.
- **Hız/boyut:** C ölçülebilir biçimde en küçük ve en hızlı (tek örnek).
- **Hızlı ekleme:** C'nin odak davranışı doğru, B'nin yanlış.
- **Görsel dil:** A'nın daire onay kutusu + dolu işaret + tutamaç + × düzeni en rafine.

## Harita D (kümülatif) için alınanlar
B tabanı (panel balon, yay animasyonu, Room) + C'nin odak davranışı, erişilebilirlik ve toplu temizle/paylaş özellikleri + A'nın hedef API 36 araç zinciri ve "API 30+ çağrılarını koru" dersi. Lint ile NewApi taraması.
