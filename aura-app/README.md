# AURA Göz Asistanı (PWA)
Telefona kurulabilen, internetsiz çalışan göz yorgunluğu paneli.
Mantık: `../aura-sim/aura.py` (Python, testli) ↔ `aura.js` (aynı kurallar).
Kamera: `vision-core.js` (saf mantık, `node vision-core.test.js`) + `vision.js` (MediaPipe, `vendor/` içinde çevrimdışı). Görüntü cihazdan çıkmaz.

**Çalıştır:** `npx http-server . -p 8080` → telefonda tarayıcıdan aç → "Ana ekrana ekle".
Sensör değerleri şimdilik kaydırıcılarla simüle edilir; gerçek gözlükte BLE'den gelecek.

## Test
- `node vision-core.test.js` (12 mantık testi)
- `test/mkclosed.js` yüz fotoğrafından açık/kapalı göz kareleri üretir (gerçek algılayıcıdan geçirir),
  `test/cam-blink-e2e.js` bunları sahte webcam olarak uygulamaya verip kırpma sayımını ölçer.
  Not: kapalı gözler fotoğraf üzerinde yapay çizilmiştir; gerçek insan kırpması değildir.
