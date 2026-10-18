# AMAÇ ve SORUN GİDERME — önce bunu oku

Bu belge iki kişi içindir: **aracı kullanacak kişi** ve **çalışmazsa düzeltecek kişi (ya da yapay zekâ)**.

---

## 1. Bu araç ne için yazıldı?

**Amaç:** Instagram'da **3, 4, 5, 6 ve 7 harfli kullanıcı adlarını** bulmak. Özellikle **Arapça kelimelerden** türeyen
isimler (`habibi`, `qamar`, `noor`…) istendi; ama **her dil** desteklenir (Türkçe, Rusça, Farsça, İspanyolca…).

**Nasıl bulur?** Tek seferlik değil, **sürekli** çalışır:
1. Kelime listelerinden (ya da kullanıcının yazdığı isimlerden) aday kullanıcı adları üretir.
2. Her adayı Instagram'da tek tek kontrol eder: "bu isimde hesap var mı?"
3. **Dolu** olanları unutmaz; zamanla tekrar kontrol eder. Çünkü bir isim **sonradan boşalabilir** (hesap silinir,
   hesap ismini değiştirir, yasak kalkar).
4. Bir isim boş çıkınca 2 kez daha doğrular, sonra kullanıcıya **hemen haber verir** (ekran + ses + dosya +
   isteğe bağlı Telegram/Discord). Kullanıcı ismi Instagram'dan **kendisi** alır. Rekabet hızda olduğu için "hemen" önemlidir.

**Başarı nasıl görünür?** Başlat'a basınca: *kontrol edildi* sayısı artar → *dolu çıktı* sayısı artar → zaman zaman
**BULUNDU** çıkar. (Kısa isimlerin çoğu doludur; bu normaldir.)

**Neden Arapça harfle değil, Latin harfle?** Instagram kullanıcı adında Arap harfi kabul etmez. Sadece `a-z`, rakam,
nokta ve alt çizgi. Bu yüzden Arapça kelime Latin yazıya çevrilir (`حبيبي → habibi`). Sesli harfler Arapçada yazılmadığı
için tek kelimeden birkaç yazım üretilir (`hbibi`, `habibi`, `habiby`…); en okunur olanlar öne alınır.

### Araç ne YAPMAZ (bilerek)
- İsmi **kendisi almaz**. Hesaba giriş yapmaz, şifre/oturum istemez.
- Instagram'ın hız sınırını **aşmaya çalışmaz** (proxy döndürme, taklit, vb. yok). "Yavaşla" cevabında bekler.
- "Boş" demek **kesin alınabilir** demek değildir. Silinen isimler aylarca tutulur, yasaklı hesaplar boş görünür.
  Bulunan isim **adaydır**; son kararı Instagram'daki "kullanıcı adını değiştir" ekranı verir.

---

## 2. Nasıl çalışıyor? (düzelten kişi için harita)

```
kelime (her dil) ──translit.py──► a-z adaylar ──candidates.py──► puan ──store.py (SQLite: avci.db)
                                                                              │ sıradaki isim (lease)
                                       notify.py ◄── engine.py ◄── checkers.py ──► Instagram profil sorgusu
                                  (ekran/ses/dosya)   │  hız ayarı, doğrulama      (boş / dolu / yavaşla / hata)
                                                      └── 2 kez doğrula, yanlış alarmı ele
```

| Dosya | Ne yapar | Bozulursa nerede bakılır |
|---|---|---|
| `avci/checkers.py` | **Instagram'a sorar** ve cevabı "boş/dolu/yavaşla/hata"ya çevirir (`interpret`) | **Instagram yöntemi değiştiyse burası** |
| `avci/selftest.py` | Bağlantı testi: bilinen hesap (`instagram`) dolu, uydurma isim boş çıkmalı | Başlat'ta çıkan uyarı buradan |
| `avci/engine.py` | Sürekli çalışır; 429'da bekler; boşu 2 kez doğrular; bulunanı duyurur | Hız/bekleme/doğrulama sorunları |
| `avci/store.py` | Durum veritabanı (`avci.db`): hangi isim ne zaman tekrar bakılacak | Sıra/tekrar/yavaşlık sorunları |
| `avci/translit.py` | Her dilden Latin harfe çeviri | Garip/yanlış çeviri |
| `avci/candidates.py`, `wordlists.py` | Aday üretimi, puan, dil listeleri | Aday kalitesi, liste indirme |
| `avci/webui.py`, `avci/ui.html` | Tıklanan ekran (yalnızca bu bilgisayardan açılır) | Ekran açılmıyor/donuyor |
| `avci/notify.py` | Bildirimler (ekran, ses, `bulunanlar.txt`, Telegram, Discord) | Bildirim gelmiyor |
| `avci/diagnose.py` | Tanı raporu | — |
| `avci.log` | **Kalıcı olay kaydı** (pencere kapansa da kalır) | Ne zaman ne oldu |

Instagram'a atılan tek istek türü: herkese açık profil sorgusu
`GET https://www.instagram.com/api/v1/users/web_profile_info/?username=<isim>` (`X-IG-App-ID` başlığıyla).
Cevap kuralları (`checkers.py → interpret`): **404** → boş · **200 + kullanıcı verisi** → dolu ·
**429 / 401 / 403 / yönlendirme / JSON olmayan 200** → "yavaşla" (bekle) · diğerleri → hata (30 sn sonra tekrar).

---

## 3. Çalışmıyorsa: ÖNCE BUNU YAP

**Tek komut** (ya da ekrandaki en alttaki "Tanı raporunu oluştur" düğmesi):

```
python -m avci tani
```

`tani-raporu.txt` dosyası oluşur: Python/sistem bilgisi, havuz durumu, bağlantı testi sonucu, **Instagram'ın ham cevabı**
ve son olaylar. **Bu dosyayı sorunu çözecek kişiye gönder.** Şifre ya da kişisel bilgi içermez.

Tek bir ismin Instagram'daki ham cevabına bakmak için: `python -m avci probe instagram zqxkwjvbnm`

### Belirtiye göre ne anlama gelir

| Ne görüyorsun | Büyük ihtimalle nedeni | Ne yapılır |
|---|---|---|
| Başlat'ta **"Instagram bu internet bağlantısını yavaşlattı (429)"** | İnternet adresi engelli: sunucu, VPN, ofis, yurt ağı | Evdeki internet ya da telefon hotspot; VPN kapat. Bulut sunucularda **çalışmaz** |
| Evde de 429 geliyor | Çok hızlı denendi ya da adres geçici engelli | Hızı **Yavaş** yap, 1–2 saat bekle, tekrar dene |
| **"bilinen hesap 'dolu' görünmedi"** | Instagram cevap biçimini/yolunu değiştirmiş | `probe instagram` çalıştır; ham cevaba göre `checkers.py → interpret`'i güncelle |
| **"uydurma isim 'boş' görünmedi"** | Instagram artık olmayan hesap için 404 vermiyor | `probe zqxkwjvbnm`; ham cevabı gör; `interpret` kuralını uyarla |
| Hepsi "boş" (BULUNDU yağıyor) | Cevap yanlış yorumlanıyor (örn. giriş duvarı 200 dönüyor) | `probe` ile birkaç dolu isim dene; `interpret` yanlışsa düzelt. Başlat'taki bağlantı testi bunu normalde yakalar |
| Hepsi "dolu", hiç BULUNDU yok | **Normal olabilir.** Kısa isimlerin neredeyse hepsi doludur | Daha çok/uzun aday ekle (5–7 harf), kendi isimlerini sıcak listeye ekle, bekle |
| Sayılar hiç artmıyor | Motor "yavaşla" bekliyor ya da havuz boş | Ekranın altındaki olay kutusuna bak; `avci.log` son satırlar |
| `baslat.bat` pencere hemen kapanıyor | Python kurulu değil ya da `pip` hata verdi | Python'u python.org'dan kur ("Add python.exe to PATH" kutusunu işaretle); penceredeki hatayı gönder |
| Ekran açılmıyor | Port dolu ya da pencere kapanmış | Siyah pencerede yazan adresi tarayıcıya elle yaz; pencere açık kalmalı |
| "Bağlantı koptu" | Siyah pencere kapatılmış | `baslat.bat`'ı tekrar çalıştır; kaldığı yerden devam eder |
| Çok yavaş | **Normal.** Instagram hızı sınırlar | 190 bin aday = günler. Sıcak liste en sık, kısa isimler önce kontrol edilir |
| Arapça çeviri garip | Sesli harf tahmini | `python -m avci translit "كلمة"`; `translit.py` içindeki `AR` tablosunu düzelt |

---

## 4. Doğrulanmış mı? (dürüst durum)

| Parça | Durum |
|---|---|
| Çeviri (Arapça, Rusça, Türkçe, Yunanca…), aday üretimi, puan | **Test edildi** (otomatik testler) |
| Havuz, sıra, tekrar kontrol, devam edebilme, 190 bin adayda hız | **Test edildi** |
| Motor: bulma, yanlış alarmı eleme, "yavaşla"da bekleme | **Test edildi** (sahte Instagram ile) |
| Ekran: ekle, havuz kur, başlat, durdur, bulunan kart, telefon boyutu, güvenlik | **Gerçek tarayıcıda tıklanarak test edildi** (demo modu) |
| Instagram kontrolcüsü: ham cevap → karar, başlıklar, "yavaşla" | **Yerel sahte Instagram sunucusuyla test edildi** |
| **Gerçek Instagram'ın bugünkü cevabı** | **TEST EDİLEMEDİ.** Geliştirme ortamının internet adresi Instagram'dan 429 aldı (bulut adresleri engelli). İlk gerçek deneme kullanıcının evinde olacak. Başlat'taki bağlantı testi ve `tani` bunu hemen gösterir |

Yani en olası sorun: **Instagram'ın gerçek cevabı beklediğimizden farklı çıkması.** Bunu `tani` raporundaki
"ham cevaplar" bölümü doğrudan gösterir; düzeltme tek yerdedir (`checkers.py → interpret`).

---

## 5. Bir yapay zekâdan yardım isterken (kopyala-yapıştır)

> Ekteki proje, Instagram'da 3–7 harfli kullanıcı adlarının boşalıp boşalmadığını sürekli kontrol eden bir araç
> (amacı ve çalışma şekli `AMAC.md` dosyasında). Çalışmıyor. Aşağıda `python -m avci tani` raporu var.
> Önce `AMAC.md` ve `avci/checkers.py` dosyalarını oku, raporu yorumla, nedenini bul ve düzelt.
> Kurallar: (1) Instagram'ın hız sınırını aşmaya çalışma; proxy döndürme/taklit ekleme. (2) Değişikliği küçük tut,
> çoğunlukla `checkers.py → interpret` yeterlidir. (3) Düzeltmeden sonra `python -m unittest discover -s tests`
> geçmeli; yeni cevap biçimi için test ekle. (4) Ne değiştirdiğini düz Türkçe anlat.
>
> [tani-raporu.txt içeriğini buraya yapıştır]

---

## 6. Sürüm

Araç sürümü `avci/__init__.py` içinde. Otomatik testler: `python -m unittest discover -s tests`.
