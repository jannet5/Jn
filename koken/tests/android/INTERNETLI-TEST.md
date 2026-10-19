# Internetli emülatör testi (2026-10-19): BAŞARILI

Ortam: Android 11 (API 30) x86_64 emülatör, yazılımsal (KVM yok). Gerçek WebView, gerçek `MainActivity`, gerçek JS köprüsü (`KokenNative`).

## Nasıl denendi
Emülatörün internet çıkışı yalnızca ortamın TLS'i yeniden sonlandıran proxy'sinden geçiyor. Bu yüzden APK'nın **test kopyası** yapıldı (`/tmp` altında, repoya girmedi). Teslim edilen APK'dan farkı yalnızca şu 3 satır/dosya:
1. `network_security_config` ile proxy CA'sına güven (res/xml + res/raw),
2. `WebView.setWebContentsDebuggingEnabled(true)` (DevTools ile sürmek için),
3. `https.proxyHost=10.0.2.2` / `https.proxyPort` (emülatörden ana makinenin proxy'sine).
Web varlıkları, `KokenNative` köprüsü ve motor teslim edilenle aynı. Teslim edilen APK sha256: `e1150749b4228a08674133409cd393489d83201eaea24f67b00b7cd143eb5db6`.

(Önceki denemede proxy CA'sını sistem deposuna koymak system_server'ı çökertmişti; bu yol sistemi değiştirmiyor.)

## Sonuçlar
- Köprü: `typeof KokenNative = object`, sürüm `1.0.0`.
- Köprü doğrudan çağrısı: `api.etimolojiturkce.com/word/kitap` → **200 + JSON gövde** (Türkçe karakterler base64 UTF-8 yolundan doğru çözüldü); `example.com` → 200. TDK bir denemede `Connection reset` verdi (aynı kaynak sonraki aramada çalıştı).
- Arama akışı ("kitap", Ara'ya basıldı, ~68 sn, yazılımsal emülatör yavaş): sonuç ekranı geldi, **4 köken bloğu**:
  - Köken 1 · Vikisözlük (Türkçe)
  - Köken 2 · Etimoloji Türkçe
  - Köken 3 · Nişanyan Sözlük (yalnız köprüyle gelen kaynak)
  - Köken 4 · TDK Güncel Sözlük
  Yol haritası: `Türkçe kitap ← Osmanlı Türkçesi كتاب ← Arapça كتاب`.
- Kanıt: `cdp-kitap.log` (ekran metni), `ekran-internet-kitap.png`.

## Eksik kalan
- İngilizce Vikisözlük bu ortamdan hız sınırına (429) takılıyor; bu testte hep ulaşılamadı. Başarılı yolu telefonda görülmeli.
- Etymonline yalnız İngilizce sözcüklerde sorulur; "kitap" için sorulmadı.
- "pijama" ve "okul" aramaları bu oturumda tamamlanmadan zaman doldu (emülatör çok yavaş).
- Gerçek telefonda denenmedi.
