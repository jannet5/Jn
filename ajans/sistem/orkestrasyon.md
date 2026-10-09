# ORKESTRASYON — ana beyin + alt sohbetler

## Roller
- **Ana beyin (tek sohbet):** haritayı çıkarır, işi böler, alt sohbetlere net görev + çıktı yolu verir, kontrol eder, sentezler. Kendisi ağır okuma/üretim yapmaz.
- **Alt sohbetler (Claude Code cloud oturumları):** her biri tek iş, tek klasör. Görev metninde: ne, nereye yazacak, ne zaman commit, bitince ne raporlayacak.
- **Ortak hafıza = git dalı.** Sohbette kalan bilgi yok sayılır. Her alt sohbet 20-30 dk'da bir `pull --rebase` + push.

## Görev metni şablonu
```
Önce <README> oku. GÖREV: <tek iş>. NASIL: <hızlı yöntem: API/CLI/script, tıklama değil>.
ÇIKTI: <dosya yolları ve formatı>. GİT: sadece <klasör>; 20-30 dk'da commit + pull --rebase + push.
BİTİNCE: son mesajda <sayılar + 5 madde>.
```

## Dersler (bu kurulumdan)
1. **Hesap limiti gerçek darboğaz.** 7 paralel sohbet 5 saatlik limiti ~10 dakikada, haftalık limiti 3 günde zorladı. Kural: aynı anda en fazla 3-4 sohbet; görev kapsamını sayıyla sınırla ("en değerli 8 video").
2. **Toplama ≠ değer.** 400 transkript çekildi ama plan sayısı az kaldı. Kural: önce az sayıda yüksek değerli kaynak → plan → özet; toplama sonra.
3. **Kapanış talimatı:** limit yaklaşınca "yeni veri çekme, elindekinden özet çıkar, push et, bitir" mesajı gönder.
4. YouTube bulut IP'sini engelliyor; alt sohbetler farklı IP'den çeker. Araç: `ajans/araclar/yt_transcript.py` (4 yedek yol).
5. Uyandırma: `send_later` ile 25-30 dk kontrol; limitte düşen sohbete `resetsAt + 2 dk`'da "devam et".
