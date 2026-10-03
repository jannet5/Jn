# AI ile mobil uygulama üretim kaynak haritası

- [`harita.md`](harita.md) — ana teslim: aşamalar, A/B/C yolları, MCP/SDK/araç kataloğu, gerçek örnekler, kabul ölçütleri.
- [`rakip-arastirma/`](rakip-arastirma/) — Aşama 1 için çalıştırılabilir rakip + yorum madenciliği aracı ve örnek çıktı.
- [`kanit/`](kanit/) — bağlantı kontrolü betiği/sonucu, npm paket doğrulaması.
- [`calisma-gunlugu.md`](calisma-gunlugu.md) — ne yapıldı, hangi komutlar, kararlar, sorunlar, doğrulamalar.

Yeniden doğrulama:

```bash
bash kanit/link-kontrol.sh                       # bağlantıları yeniden kontrol eder
cd rakip-arastirma && npm install && node rakip.mjs --terim "meditation" --ulke tr --dil tr
```
