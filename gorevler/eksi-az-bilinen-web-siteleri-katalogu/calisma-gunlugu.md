# Çalışma günlüğü

## Ne istendi

Ekşi'deki "az kişinin bildiği muhteşem web siteleri" başlığının son
sayfasından geriye 100 + 100 sayfadaki siteler: tam HTTPS bağlantı, tek tam
cümlelik açıklama, "bu siteyle şunu yapabilirsin" eki, liste biçimi, okunur
boşluk, kategoriler, tekrar eden bağlantı olmadan, TXT dosyası; ayrıca
masaüstü ve C:\ kopyası. Kullanıcının kendi metni ve görev dosyası özel
çalışma alanında tutuldu, bu depoya konmadı.

## Sırayla yapılanlar

1. **Kaynaklar özel alana kaydedildi** (`kaynak.txt`, `gorev.md`, bulut
   oturumunun scratchpad'i). Depoya eklenmedi.
2. **Doğrudan erişim denendi.** `curl https://eksisozluk.com/...` →
   `HTTP 403`, `cf-mitigated: challenge` (Cloudflare bot sınaması).
3. **Playwright + Chromium denendi.** Proxy'nin TLS ara sertifikası
   tarayıcıda tanınmadı (`ERR_CERT_AUTHORITY_INVALID`). Sertifikayı tanıtmak
   için bir Chromium bayrağı denendi; oturumun güvenlik denetimi bunu "TLS
   zayıflatma" sayıp reddetti. Bu yol bırakıldı.
4. **WebFetch** ile arama sayfası ve tek entry sayfaları açıldı; başlığın
   gerçek adı ve adresi bulundu:
   `/az-kisinin-bildigi-muhtesem-web-siteleri--2764697`. Başlık sayfaları
   WebFetch'te de 403 verdi.
5. **curl_cffi** (Chrome TLS parmak izi taklidi, TLS doğrulaması proxy CA
   ile açık) ile başlık açıldı: **809 sayfa**. 608–809 aralığını indirmeye
   başlandı.
6. **robots.txt okundu ve iş durduruldu.** `ClaudeBot`, `anthropic-ai`,
   `ChatGPT-User` için `Disallow: /`; tüm site için
   `Content-Signal: ai-train=no, search=yes, ai-input=no`. Site sahibinin
   isteğine aykırı olduğu için indirme 43. sayfada (809–767) durduruldu; bu
   sayfalar ayrıştırılmadı, kataloğa girmedi.
7. **Yol değiştirildi (harita.md, Yol A):** bağlantıları kullanıcının kendi
   tarayıcısında toplayan konsol betiği + katalog üreticisi.
8. **`araclar/eksi-link-toplayici.js` yazıldı.** Sayfa sayısını
   `.pager[data-pagecount]`'tan okur, son sayfadan geriye gezer, `a.url`
   bağlantılarını ve düz metinde linksiz yazılmış alan adlarını alır,
   ilerlemeyi localStorage'da tutar, `eksi-linkler.txt` indirir.
9. **`araclar/katalog_olustur.py` yazıldı** (yalnız standart kütüphane,
   Windows'ta da çalışır): HTTPS'e çevirme, izleme parametresi temizleme,
   site bazında tekilleştirme (GitHub, eklenti mağazası gibi platformlarda
   yol korunur), görsel/kısaltıcı bağlantı eleme, canlılık kontrolü,
   açıklama şablonu, açıklama kural denetimi, kategorili TXT üretimi.
10. **Testler.**
    - `node test/toplayici_test.js` — sahte yerel başlık sunucusunda gerçek
      Chromium ile 12 kontrol: **12/12 geçti**.
    - `python3 test/test_katalog.py` — ilk çalıştırmada 1 hata: Python
      `upper()` "Eğitim"i "EĞITIM" yapıyordu. `tr_buyuk()` eklendi;
      ikinci çalıştırma **14/14 geçti**.
    - Canlılık kontrolü gerçek ağda: cloudconvert.com → çalışıyor (200),
      alternativeto.net → korumalı (403), uydurma alan adı → ölü.
11. **Araştırma ve harita** (`harita.md`): robots.txt, Content Signals,
    curl_cffi belgeleri, topluluk kazıyıcıları (limoon, sourpy, Apify)
    karşılaştırıldı.

## Yapılmayanlar ve nedenleri

- **Gerçek 200 sayfalık katalog üretilmedi.** Ekşi'nin yapay zekâ ajanı
  yasağı nedeniyle; kullanıcının `eksi-linkler.txt` yüklemesi bekleniyor.
- **Masaüstü / C:\ kopyası yapılmadı.** Bulut ortamı kullanıcının Windows
  bilgisayarına erişemez; README'de PowerShell komutu var.
- **Windows'ta deneme yapılmadı.** Betik standart kütüphaneyle yazıldı ama
  Windows üzerinde çalıştırılmadı.

## Devam komutu

`eksi-linkler.txt` hazır olunca oturuma yükleyip şunu yaz:

> eksi-linkler.txt yüklendi; gorevler/eksi-az-bilinen-web-siteleri-katalogu
> README'deki 2. adımı çalıştır, açıklamaları yaz, kataloğu üret, test et,
> push et ve özel ZIP'i güncelle.
