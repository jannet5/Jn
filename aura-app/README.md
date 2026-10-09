# AURA Göz Asistanı (PWA)
Telefona kurulabilen, internetsiz çalışan göz yorgunluğu paneli.
Mantık: `../aura-sim/aura.py` (Python, testli) ↔ `aura.js` (aynı kurallar).

**Çalıştır:** `npx http-server . -p 8080` → telefonda tarayıcıdan aç → "Ana ekrana ekle".
Sensör değerleri şimdilik kaydırıcılarla simüle edilir; gerçek gözlükte BLE'den gelecek.
