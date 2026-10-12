# Kullanıcı Adı Avcısı (3–7 harf, her dil)

Instagram'da **boşalmış** kısa kullanıcı adlarını sürekli kontrol edip bulunca sana haber veren araç.

## Önemli gerçekler (önce bunu oku)

- Instagram kullanıcı adında **Arap harfi olmaz**. Sadece `a-z`, rakam, `.` ve `_` olur. "Arapça isim" = Arapça kelimenin Latin yazımı (حبيبي → `habibi`). Araç bu çeviriyi her dil için otomatik yapar.
- Araç "bu isimde hesap yok" sinyalini yakalar. Bu **kesin alınabilir** demek değildir (Instagram yeni silinen isimleri bir süre tutabilir). Son karar, Instagram'da ismi değiştirirken verilir. Bu yüzden bulunan isim **aday**dır.
- İsmi **sen alırsın** (Profili düzenle → Kullanıcı adı). Araç hızlı haber verir, hesabına girmez, şifre/oturum istemez.
- Instagram hız sınırı koyar. Araç 429 görünce otomatik yavaşlar ve bekler, engeli aşmaya çalışmaz. Bu yüzden çok büyük havuzlarda sıra (puan) önemlidir: kısa, okunur, sık kullanılan kelimeler önce kontrol edilir.
- **Doğrulama durumu:** Instagram'a karşı canlı test bu geliştirme ortamında yapılamadı (aşağıya bak). Mantığın tamamı sahte Instagram ile test edildi.

## Hızlı başlangıç

```bash
cd kullanici-avcisi
pip install -r requirements.txt

python -m avci demo                      # gerçek Instagram'sız tam deneme (önce bunu çalıştır)

python -m avci fetch ar tr fa            # dillerin 50.000 kelimelik listesini indir (isteğe bağlı)
python -m avci build --lang ar --min 3 --max 7        # aday havuzu (hazır Arapça liste dahil)
python -m avci build --combo 3                        # tüm 3 harfliler (17.576)
python -m avci build --pattern CVCV                   # okunur desenler
python -m avci add habibi qamar حبيبي                 # özel isimler: SICAK liste (en sık bakılır)

python -m avci run                       # sürekli çalış (Ctrl+C ile dur, kaldığı yerden devam)
python -m avci run --minutes 30          # 30 dk çalış, dur
python -m avci status                    # özet
python -m avci found                     # bulunanlar
```

## Nasıl çalışır (aşamalar)

1. **Aday üret** — kelime listesi (herhangi bir dil) → `translit.py` ile Latin harfe → 3–7 harf filtresi → puanlama (kısa + okunur + sık kullanılan = önde). Arapça için sesli harf eklenerek birden çok yazım üretilir (`حبيبي` → `habibi`, `habiby`…).
2. **Sıraya koy** — `avci.db` (SQLite). Sıcak liste > yeni adaylar > yüksek puan > en eski kontrol.
3. **Kontrol et** — Instagram'ın herkese açık profil sorgusu. Cevaplar: boş / dolu / yavaşla / hata.
4. **Yavaşlama** — 429 gelince ortak hız düşer, bekleme süresi katlanır; sakinleşince tekrar hızlanır. Hiçbir isim kaybolmaz.
5. **Tekrar kontrol** — dolu isimler periyodik tekrar bakılır (sıcak liste varsayılan 2 dk, diğerleri 6 saat), silinen/boşalan yakalansın diye.
6. **Doğrula** — boş görünen isim 2 kez daha arayla kontrol edilir; geçici yanlış alarmlar elenir.
7. **Haber ver** — ekranda büyük uyarı + ses + `bulunanlar.txt` + (isteğe bağlı) Telegram / Discord.

## Telefona bildirim (isteğe bağlı)

```bash
export TG_BOT_TOKEN=...   TG_CHAT_ID=...       # Telegram botu
export DISCORD_WEBHOOK=https://discord.com/api/webhooks/...
```

## Dosyalar

| Dosya | Görev |
|---|---|
| `avci/translit.py` | Her dilden Latin harfe çeviri (Arapça özel destek) |
| `avci/candidates.py` | Aday üretimi + puanlama |
| `avci/store.py` | Durum veritabanı (devam edebilme) |
| `avci/checkers.py` | Instagram kontrolcüsü + test için sahte Instagram |
| `avci/engine.py` | Sürekli çalışan motor, hız ayarı, doğrulama |
| `avci/notify.py` | Bildirimler |
| `tests/test_all.py` | Otomatik testler (`python -m unittest discover -s tests`) |
