# Kullanıcı Adı Avcısı (3–7 harf, her dil)

Instagram'da **boşalmış** kısa kullanıcı adlarını sürekli kontrol edip bulunca sana haber veren araç.
Tıklanan bir ekranı var; terminal bilmen gerekmez.

## Başlatmak (en kolay yol)

1. ZIP'i bir klasöre aç.
2. **Windows:** `baslat.bat` dosyasına çift tıkla. **Mac:** `baslat.command` dosyasına çift tıkla.
   (Python yoksa pencere sana nereden kuracağını söyler; kurarken "Add python.exe to PATH" kutusunu işaretle.)
3. Tarayıcıda ekran açılır. Orada: isim ekle → dil seç, havuz kur → **Başlat**.
4. Bir isim bulununca ekranda yeşil kart çıkar, ses çalar. **"Instagram'da al"** düğmesine bas, ismi yapıştır.

**Önce kendi evinden dene:** Instagram sunucu/VPN/ofis internetini hemen yavaşlatır (429). Araç Başlat'a basınca
bunu kendisi test eder ve engelliyse sana söyler. Evdeki internet ya da telefon hotspot'u çalışır.

Gerçek Instagram'a dokunmadan denemek için: `python -m avci ui --demo`

## Önemli gerçekler

- Instagram kullanıcı adında **Arap harfi olmaz**: sadece `a-z`, rakam, `.` ve `_`. "Arapça isim" = Arapça kelimenin
  Latin yazımı (حبيبي → `habibi`). Araç bu çeviriyi her dil için yapar.
- Araç "bu isimde hesap yok" sinyalini yakalar. Bu **kesin alınabilir** demek değildir: silinen isimler aylarca tutulur,
  yasaklı/devre dışı hesaplar boş görünebilir. Bulunan isim **aday**dır; son kararı Instagram'daki isim değiştirme ekranı verir.
- İsmi **sen alırsın**. Araç hesabına girmez, şifre ya da oturum istemez.
- Instagram hız sınırı koyar; araç "yavaşla" cevabında bekler, engeli aşmaya çalışmaz. 190 bin adaylık havuzun ilk turu günler sürer.
  Bu yüzden **sıcak liste** (kendi isimlerin, 2 dakikada bir) ve kısa/okunur isimler öne alınır.

## Terminalden kullanım (isteğe bağlı)

```bash
pip install -r requirements.txt
python -m avci ui                         # tıklanan ekran
python -m avci selftest                   # bu internette kontrol çalışıyor mu?
python -m avci fetch ar tr fa             # dil listeleri (50.000 kelime)
python -m avci build --lang ar --min 3 --max 7
python -m avci build --combo 3            # tüm 3 harfliler
python -m avci add habibi qamar حبيبي     # sıcak liste
python -m avci run --minutes 30           # ekransız çalıştır
python -m avci status / found
python -m avci demo                       # sahte Instagram'la tam deneme
```

## Nasıl çalışır

1. **Aday üret:** kelime listesi (her dil) → Latin harfe → 3–7 harf → puan (kısa + okunur + sık kullanılan = önde).
2. **Sıraya koy:** `avci.db` (SQLite). Sıcak liste > bekleyenler > dolu isimlerin tekrar zamanı. Durdurup başlatırsan kaldığı yerden devam eder.
3. **Kontrol et:** Instagram'ın herkese açık profil sorgusu: boş / dolu / yavaşla / hata.
4. **Yavaşlama:** 429'da ortak hız düşer, bekleme katlanır; sakinleşince hızlanır. Hiçbir isim kaybolmaz.
5. **Tekrar kontrol:** dolu isimler periyodik tekrar bakılır (sıcak 2 dk, diğerleri 6 saat).
6. **Doğrula:** boş görünen isim 2 kez daha arayla kontrol edilir; geçici yanlış alarmlar elenir.
7. **Haber ver:** ekran + ses + `bulunanlar.txt` + (isteğe bağlı) Telegram / Discord (`TG_BOT_TOKEN`, `TG_CHAT_ID`, `DISCORD_WEBHOOK`).

## Dosyalar

| Dosya | Görev |
|---|---|
| `baslat.bat`, `baslat.command` | Çift tıkla başlat |
| `avci/webui.py`, `avci/ui.html` | Tıklanan ekran (yalnızca bu bilgisayardan erişilir) |
| `avci/translit.py` | Her dilden Latin harfe çeviri (Arapça özel destek) |
| `avci/candidates.py`, `avci/wordlists.py` | Aday üretimi, puanlama, dil listeleri |
| `avci/store.py` | Durum veritabanı |
| `avci/checkers.py`, `avci/selftest.py` | Instagram kontrolcüsü, bağlantı testi |
| `avci/engine.py`, `avci/notify.py` | Sürekli çalışan motor, bildirimler |
| `tests/test_all.py` | 23 otomatik test: `python -m unittest discover -s tests` |
