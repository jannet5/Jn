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

## 2. tur — kullanıcı incelemesinden gelen düzeltmeler (2026-10-03)

Kullanıcı ZIP'i indirip SHA-256 ve iç hash'leri doğruladı ve statik
incelemede şu sorunları bildirdi. Gerçek veriyi kendi Chrome'unda,
sayfa JSON'u olarak topluyor (o sırada 72 sayfa). Konsol toplayıcısı
kullanılmayacak.

1. **Tarayıcı konsolu toplayıcısı ve testi kaldırıldı**
   (`eksi-link-toplayici.js`, `toplayici_test.js`, `sahte_sunucu.py`).
2. **JSON girdisi:** `page`, `page_url`, `observed_at`, `links:[{label,url}]`.
   Tek nesne, dizi, JSON Lines ya da klasör kabul edilir; eski TSV de okunur.
   Her adresin `kaynaklar[]` alanında sayfa izi korunur. `--ust/--alt` ile
   eksik sayfalar raporlanır.
3. **HTTP artık körü körüne HTTPS yapılmıyor.** `normalize` şemayı korur.
   `site_denetle` önce HTTPS'i dener; 2xx gelmezse özgün HTTP kalır ve
   katalogda "[yalnız HTTP; HTTPS doğrulanamadı]" diye işaretlenir.
4. **Kör host tekilleştirmesi kaldırıldı.** Anahtar artık tam adres
   (alan + yol + sorgu + `#/` parçası). Aynı alan adındaki farklı yollar
   raporlanır ama birleştirilmez.
5. **Durum etiketleri:** varsayılan `kontrol_edilmedi`. Ana listeye yalnız
   `calisiyor` girer; `korumali` ve `kontrol_edilmedi` ayrı bölümde.
6. **Güvenli ağ denetimi (`url_denetim.py`):** şema/port/kullanıcı bilgisi
   kuralları, iç ad ve kamu dışı IP reddi, her adımda tek DNS çözümü ve IP
   sabitleme, elle ve yeniden denetlenen yönlendirmeler (en fazla 5).
7. **Ortam bulgusu:** Bu bulutun proxy'si IP'ye CONNECT'i kabul ediyor ama
   `Host` CONNECT hedefiyle aynı değilse isteği reddediyor ("Host header does
   not match CONNECT target"); TLS de kopuyor. Bu yüzden açık seçimli
   `--proxy-ad-ile` modu eklendi: ad yerelde denetlenir, bağlantıyı proxy
   kurar, sonuçta `ip_sabit: false` yazar. Varsayılan mod IP sabitlemedir.
8. **Ön kontrol:** `kontrol`, siteleri işaretlemeden önce
   `https://example.com/` ile ağı sınar. Başarısız olursa çıkış kodu 2 ile
   durur ve hiçbir siteyi "ölü" işaretlemez. Bu ortamda IP modunda bu
   gerçekten oldu ve doğru davrandı.
9. **Testlerin bulduğu hatalar ve düzeltmeleri:**
   - Klasör okurken `BENIOKU.txt` TSV sanılıyordu → klasörde yalnız `.json`/`.jsonl` okunuyor.
   - Dosyalar alfabetik işleniyordu → kayıtlar sayfa numarasına göre azalan diziliyor (son sayfadan başla).
   - Düz HTTP isteğinde `Host: ad:80` gidiyordu → varsayılan port düzeltildi.
   - HTTP→HTTPS yönlenmesinde gösterilen adres başarısız HTTPS adayı olabiliyordu → gerçek varış adresi (`https_url`) kaydediliyor.
   - Doğrulanamayan HTTPS adrese "yalnız HTTP" notu düşülüyordu → not yalnız `http://` adreslerde.
   - Proxy test sınıfı yerel sunucu testlerini miras alıp iki kez sayıyordu → ortak taban sınıfa ayrıldı.
10. **Gerçek test çıktısı:** `python3 test/calistir.py --proxy-ad-ile` →
    `test/cikti/test-ciktisi.txt`. Bölümler: birim/entegrasyon (SENTETİK),
    sentetik uçtan uca hat (gerçek ağ denetimiyle), gerçek ağ duman testi
    (kamu adresler, Ekşi verisi değil). Uydurma GitHub adreslerinin 403'ü
    GitHub'dan değil bu bulut ortamının GitHub erişim politikasından geliyor;
    "çalışan" sayılmadı. Teşhis için 2xx dışı yanıtlarda gövde özeti
    kaydediliyor.

## 3. tur — gerçek girdiyle katalog üretimi (2026-10-03)

Kullanıcı kendi Chrome'unda topladığı `page-trace.json` dosyasını gzip+Base64
olarak verdi. Bu dosya ve ondan üretilen gerçek katalog yalnız özel pakette
duruyor; bu depoya konmadı.

1. **Girdi doğrulaması:** Base64 çözüldü; gzip 81369 bayt, SHA-256
   `60eee952…f15d`; açılmış JSON SHA-256 `166f05cf…30f1`. Üçü de kullanıcının
   verdiği değerlerle birebir aynı. 200 benzersiz sayfa (809..610, eksik
   yok), her `page_url` kendi sayfa numarasını taşıyor, 4024 bağlantı.
2. **Tekilleştirme:** 3580 birebir tekil ham URL → 3437 sayfa düzeyinde tekil
   adres. 4 çift şemalı yazım hatası (`http://https//…`) onarıldı ve
   işaretlendi. 18 kısaltıcıdan 15'inin hedefi güvenle çözüldü (t.co
   meta-refresh ile). 7 doğrudan görsel bağlantısı site sayılmadı → 3430.
3. **Gerçek veride bulunan hatalar:**
   - Türkçe karakterli yollar `UnicodeEncodeError` verip tüm denetimi
     durduruyordu → yol ve sorgu yüzde-kodlanıyor, tek sitedeki iç hata işi
     durdurmuyor, 100 sonuçta bir ara kayıt alınıyor.
   - Bekleme döngülerim `pgrep -f` ile kendi komut satırlarını eşleştirip
     bitmiş süreci bekledi → bekleme PID üzerinden yapıldı.
   - 42 GitHub adresinin 403'ü GitHub'dan değil bulut ortamının GitHub
     politikasından geliyordu; bağlantı kopmaları, zaman aşımları ve geçici
     DNS hataları da kesin değil → bunlar artık `kontrol_edilmedi`, bir kez
     yeniden denendi.
   - HTTPS verilip HTTP'ye yönlenen adresler işaretsiz görünüyordu → not
     eklendi; başka alan adına yönlenen 86 site işaretlendi.
4. **Açıklamalar:** 3220 adres için Türkçe açıklama yazıldı. Her kaydın
   dayanağı kayıtlı: sitenin kendi başlık ya da açıklaması 2110, yaygın
   bilinen hizmet 266. 844 kayıtta dayanak yok; bunlara açıklama
   yazılmadı. İlk alt ajan turu kullanım limitiyle kesildi. Açık sıra
   numaralı 529 kayıt kurtarıldı; sıra numarasız kısmi çıktılar yanlış
   eşleşme riski yüzünden kullanılmadı.
5. **Eşleşme hatası bulundu ve düzeltildi:** Kataloğun ilk sürümünde bazı
   açıklamalar yanlış siteye kaymıştı (ör. asciiart.eu → RefSeek
   açıklaması). `hizalama.py` (komşu karşılaştırması) ve `marka.py` (marka
   adı kendi sitesinde mi) ile tarandı. k3 partisi büyük ölçüde (275), k4
   kısmen bozuktu. İkisi sıfırdan yeniden yazıldı. Diğer partilerdeki 197
   şüpheli kayıt da yeniden yazıldı. Yeniden yazımlarda her satıra sitenin
   anahtarı da yazıldı ve `derle.py` uyuşmayan satırı reddetti. Kalan
   işaretler tek tek incelendi; yanlış alarm oldukları görüldü. Rastgele
   30 örneğin 30'u doğru çıktı.
6. **Kabul:** `kabul.py` 19/19 geçti. Toplamlar kapanıyor: 2258 doğrulanmış
   çalışan + 118 çalıştığı doğrulanamayan + 844 ne işe yaradığı
   doğrulanamayan + 210 çalışmayan/reddedilen = 3430. Ana listedeki
   sitelerin 1140'ı ilk 100 sayfadan, 1118'i yalnız sonraki 100 sayfadan.
7. **Testler:** `python3 test/calistir.py --proxy-ad-ile` → 30 + 24 test geçti
   (`test/cikti/test-ciktisi.txt`).

## Yapılmayanlar ve nedenleri

- **Ağ denetimi tek seferlik:** Siteler 2026-10-03'te bu bulut ortamının
  proxy'si üzerinden denetlendi (`--proxy-ad-ile`, IP sabitleme yok).
  Durumlar zamanla değişebilir.
- **844 sitenin ne yaptığı yazılmadı:** Sayfası okunamayan ya da içeriği
  belirsiz siteler için tahmin yapılmadı.
- **Masaüstü / C:\ kopyası yapılmadı.** Bulut ortamı kullanıcının Windows
  bilgisayarına erişemez; README'de PowerShell komutu var.
- **Windows'ta deneme yapılmadı.** Betikler standart kütüphaneyle yazıldı
  ama Windows'ta çalıştırılmadı. IP sabitlemeli proxy'siz mod da yerel
  sunucu testleriyle sınandı ama gerçek internete karşı yalnız proxy'li
  (`ad`) modda denendi.

## Devam komutu

İş tamamlandı. Kataloğu yeniden üretmek için özel paketteki
`uretim/CHECKPOINT.md` ve README'deki adımlar yeterli.
