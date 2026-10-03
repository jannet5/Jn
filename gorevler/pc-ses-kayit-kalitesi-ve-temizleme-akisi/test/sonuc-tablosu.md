| Kayıt | Yöntem | DNSMOS OVRL | SIG | BAK | STOI | LUFS | Gerçek tepe | Gürültü tabanı (dBFS) |
|---|---|---|---|---|---|---|---|---|
| kayit1 | Ham kayıt (yalnız 80 Hz kesim + seviye eşitleme) | **1.19** | 1.48 | 1.20 | 0.740 | -16.2 | -1.4 | -32.0 |
| kayit1 | Audacity 3.7.3 makrosu (HPF+Noise Reduction 12 dB+Compressor+Loudness+Limiter) | **1.67** | 2.14 | 2.00 | 0.733 | -16.7 | -1.4 | -43.5 |
| kayit1 | Ses-Temizle klasik mod (ffmpeg afftdn) | **1.71** | 2.17 | 2.09 | 0.720 | -15.8 | -1.5 | -42.7 |
| kayit1 | ffmpeg arnndn (RNNoise sh modeli) | **1.92** | 2.44 | 2.29 | 0.697 | -16.0 | -1.5 | -47.5 |
| kayit1 | Ses-Temizle AI, -Guc 18 (hafif) | **2.55** | 3.31 | 2.98 | 0.760 | -16.0 | -1.5 | -43.5 |
| kayit1 | Ses-Temizle AI, -Guc 30 (doğal) | **2.85** | 3.33 | 3.61 | 0.763 | -16.1 | -1.5 | -54.8 |
| kayit1 | Ses-Temizle AI, varsayılan (tam güç) | **2.96** | 3.28 | 3.93 | 0.773 | -17.2 | -1.4 | -85.3 |
| kayit2 | Ham kayıt (yalnız 80 Hz kesim + seviye eşitleme) | **1.08** | 1.20 | 1.16 | 0.611 | -15.9 | -1.4 | -18.8 |
| kayit2 | Audacity 3.7.3 makrosu (HPF+Noise Reduction 12 dB+Compressor+Loudness+Limiter) | **1.14** | 1.28 | 1.25 | 0.591 | -16.7 | -1.4 | -37.5 |
| kayit2 | Ses-Temizle klasik mod (ffmpeg afftdn) | **1.51** | 2.04 | 1.99 | 0.591 | -15.7 | -1.5 | -36.6 |
| kayit2 | ffmpeg arnndn (RNNoise sh modeli) | **1.16** | 1.32 | 1.29 | 0.585 | -16.6 | -1.5 | -39.0 |
| kayit2 | Ses-Temizle AI, -Guc 18 (hafif) | **1.70** | 2.44 | 1.98 | 0.667 | -15.9 | -1.5 | -36.9 |
| kayit2 | Ses-Temizle AI, -Guc 30 (doğal) | **2.14** | 2.78 | 2.82 | 0.676 | -15.9 | -1.5 | -47.8 |
| kayit2 | Ses-Temizle AI, varsayılan (tam güç) | **2.46** | 2.79 | 3.81 | 0.677 | -16.9 | -1.5 | -74.2 |
| kayit1 | Temiz stüdyo referansı (üst sınır) | **3.29** | 3.59 | 4.02 | 1.000 | -27.8 | -6.0 | sayısal sessizlik |
| kayit2 | Temiz stüdyo referansı (üst sınır) | **3.23** | 3.54 | 4.02 | 1.000 | -26.3 | -6.0 | sayısal sessizlik |
