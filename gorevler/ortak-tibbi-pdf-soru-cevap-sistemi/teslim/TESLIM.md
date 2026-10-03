# Teslim kaydı

- Kod commit'i: `316f88b88642c1ad21650c28870be03c40078f1b` (dal `claude/bold-hawking-5ed6gv`). Push edildi, uzaktaki commit yereldekiyle aynı.
- `medortak-ortak-tibbi-pdf-soru-cevap.zip` (bu klasörün o commit'teki `git archive` çıktısı)
  SHA-256 `7a637edb82ef4799bf28a61d1f52fb19ac50f7d0e10eb7371235be6b001217ce`
- `medortak-gorev-dali.bundle` (görev dalının git bundle'ı)
  SHA-256 `466dbcaa1cc16784cc9264e0ac15fe60efbf6116a8884596881155fa7cd38a04`
- Geri okuma: `sha256sum -c` iki dosya için de OK. Bundle'dan klonlama başarılı. ZIP açıldı ve içinden `pytest` çalıştırıldı: 5/5 geçti.
- ZIP'te kaynak sohbet metni ya da özel yol olmadığı taramayla doğrulandı.
