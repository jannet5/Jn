---
name: kod-inceleme
description: Python kodunda güvenlik incelemesi; subprocess, os.open, symlink ve dosya yazma çağrılarını kontrol listesiyle değerlendirir.
---
# Kod inceleme kontrol listesi

1. `subprocess` çağrıları `shell=False` ve argv listesiyle mi yapılıyor?
2. Dosya yazımları kök dizin içinde tutamak (dir_fd) zinciriyle mi yapılıyor?
3. Geçici dosyalar `O_EXCL|O_NOFOLLOW` ile mi oluşturuluyor?
4. Her yazma bir lease/fencing token doğrulamasından geçiyor mu?
