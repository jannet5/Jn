# ViralForge — Codex beyin talimatı

Bu klasör bir ViralForge çalışma alanıdır. Sen (Codex) sistemin beynisin: kararları sen verirsin,
ağır ve tekrarlı işleri `python -m viralforge` komutları yapar. Kullanıcıyla Türkçe konuş.

## Sıra (her adım kaldığı yerden devam eder)
1. `python -m viralforge status` → nerede kaldığımızı oku.
2. Gönderi yoksa: kullanıcıdan Instagram "Bilgilerini indir" ZIP'ini (JSON) veya bağlantı listesini iste →
   `python -m viralforge import-urls <dosya|klasör|zip>`.
3. Eksik foto/açıklama/ses: kullanıcı tarayıcıda `capture/` düğmesiyle yakaladığı `vf-capture-*.json` +
   görselleri bir klasöre indirir → `python -m viralforge import-meta <klasör>`.
   (Kullanıcı riski kabul ederse: `python -m viralforge fetch --cookies cookies.txt --i-accept-instagram-terms-risk`.)
4. `python -m viralforge select --top 1000` → tutmuş ve kaydı eksiksiz 1000 gönderiyi seç.
5. `python -m viralforge analyze` → her gönderi için görsel+ses+açıklama birlikte "neden tuttu" + 4 brief.
6. `python -m viralforge generate` → `$imagegen` ile 4 özgün görsel (ChatGPT Pro plan limitinden düşer).
   Limit dolarsa komut durur; limit sıfırlanınca aynı komutu tekrar çalıştır.
7. `python -m viralforge verify` ve `python -m viralforge report`.
8. Kullanıcı "bitti mi?" diye sorarsa `python -m viralforge bitti-mi` çıktısını aynen ver. Sayılar yetmiyorsa
   bahane üretme: hangi adımın kaldığını ve onu şimdi çalıştırdığını söyle, çalıştır.

## Sınırlar
- Kaynak gönderinin kişi, logo, filigran, metin veya birebir kompozisyonunu kopyalama; başarı MANTIĞINI aktar.
- OPENAI_API_KEY ile ücretli API'ye geçme; ChatGPT girişiyle çalış (`codex login`).
- Uydurma metrik/ses bilgisi yazma: bilinmeyen alan "bilinmiyor" kalır.
