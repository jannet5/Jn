# ORKESTRASYON — ana beyin + alt sohbetler

## Roller
- **Ana beyin (tek sohbet):** haritayı çıkarır, işi böler, alt sohbetlere net görev + çıktı yolu verir, kontrol eder, sentezler. Kendisi ağır okuma/üretim yapmaz.
- **Alt sohbetler (Claude Code cloud oturumları):** her biri tek iş, tek klasör. Görev metninde: ne, nereye yazacak, ne zaman commit, bitince ne raporlayacak.
- **Ortak hafıza = git dalı.** Sohbette kalan bilgi yok sayılır. Her alt sohbet 20-30 dk'da bir `pull --rebase` + push.

## Görev kartı (her alt sohbet ve rutin aynı 5 alan)
1. **Görev:** tek iş, tek cümle
2. **Girdi:** SPEC/BRIEF bölümü, dosyalar, referanslar
3. **Başarı koşulu:** ölçülebilir (Lighthouse ≥90, KAPI-2 ≥3/4, 8 plan + OZET)
4. **Çıktı yeri:** klasör, dal, commit sıklığı
5. **Yapma listesi:** dokunulmayacak dosyalar, yasak işler, sayı sınırı

## Seviye seçimi
Tek hat sıralı iş → tek oturum + alt ajan · 2-4 bağımsız hat → ayrı cloud oturumları (her biri kendi klasörü) · 10+ aynı tip iş (10 restoran örneği) → toplu fan-out · zaman tetikli bakım → rutin (gece site sağlığı, haftalık içerik, aylık reklam raporu).

## Doğrulama kancası
Müşteri projesinde `.claude/settings.json` Stop kancası `scripts/kontrol.sh`'yi çalıştırır (build + SS + test). Kanıtsız "bitti" geçersiz. Şablon: `sablon/web/settings.json`.

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
