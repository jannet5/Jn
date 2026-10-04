# Teslim kaydı

## Tur 2 — paketleme dilimi (kod commit'i `b9376b4062563421d7b70c5ab0e6d601d30f05ce`)
- `medortak-ortak-tibbi-pdf-soru-cevap-v2.zip`: bu klasörün `git archive` çıktısı.
  SHA-256 `595803228521f0f4c9556099a695086c2be0c7e5bfe3f6e848e532ba34547c77`
- `medortak-gorev-dali-v2.bundle`: görev dalının git bundle'ı.
  SHA-256 `731b4882776a51b4b9c5cfa77c5c15834b364e14144c17b298181ad92b695edd`
- **Geri okuma:**
  - `sha256sum -c` iki dosya için de OK.
  - Bundle'dan klonlanan dalın son commit'i `b9376b4`.
  - ZIP açıldı ve içinden `pytest` çalıştı: 23/23 geçti. Çeviri modelleri ZIP'e dahil değil; `MEDPDF_MT_DIR` ile verildi.
- **Çeviri modelleri:** Depoya ve ZIP'e girmiyor; `scripts/fetch_mt_models.sh` ile sabit sürümlerden üretiliyor. Üretilen `model.bin` SHA-256 değerleri:
  - bible-trk: `e210de21d3fc714fa9730973e8c8a1a3eeb0af53dea08533cb4a0a4f599aa11d`
  - opus-tr-es: `5139fa1dac2078e207fb36c440a9f6e72d83aa9922149d060b592dbecb18371c`
  - Docker içinde ve yerelde üretilenler birebir aynı.
- `788ddaf` sonrasında değişen tek klasör `gorevler/ortak-tibbi-pdf-soru-cevap-sistemi`.

## Tur 1 (kod commit'i `316f88b`)
- ZIP SHA-256 `7a637edb82ef4799bf28a61d1f52fb19ac50f7d0e10eb7371235be6b001217ce`
- bundle SHA-256 `466dbcaa1cc16784cc9264e0ac15fe60efbf6116a8884596881155fa7cd38a04`
