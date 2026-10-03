# Ekşi az bilinen web siteleri kataloğu

Ekşi Sözlük'teki "az kişinin bildiği muhteşem web siteleri" başlığının son
200 sayfasında (bugün 809 → 610) paylaşılan bağlantılardan kategorili,
açıklamalı bir TXT listesi üreten araçlar.

> **Durum:** Üretici ve güvenli URL denetimi hazır, testli. **Gerçek katalog
> henüz yok.** Sayfa verisini kullanıcı kendi Chrome'unda topluyor; JSON
> gelince aşağıdaki adımlar çalıştırılacak. Ayrıntı: [harita.md](harita.md).

## Girdi biçimi

Her sayfa için bir nesne. Dosya tek nesne, nesne dizisi, JSON Lines ya da bu
dosyaları içeren bir klasör olabilir. Entry metni gerekmez.

```json
{"page": 809,
 "page_url": "https://eksisozluk.com/az-kisinin-bildigi-muhtesem-web-siteleri--2764697?p=809",
 "observed_at": "2026-10-03T12:00:00Z",
 "links": [{"label": "https://savevideo.net/", "url": "https://savevideo.net/"}]}
```

## Kullanım

```
python araclar/katalog_olustur.py tekillestir sayfalar/ -o siteler.json --ust 809 --alt 610 --kisaltici-coz
python araclar/katalog_olustur.py kontrol siteler.json          # proxy IP tüneline izin vermiyorsa: --proxy-ad-ile
python araclar/katalog_olustur.py sablon siteler.json -o aciklamalar.json
#   aciklamalar.json: her adres için kategori, "ne", "yapabilirsin"
python araclar/katalog_olustur.py uret siteler.json aciklamalar.json -o katalog.txt
```

- **tekillestir:** Eksik sayfaları (`kapsam.eksik`) ve ilk 100 / sonraki 100
  aralıklarını raporlar. Yalnız birebir aynı sayfayı birleştirir (http/https,
  www ve sondaki `/` farkı). Aynı alan adındaki farklı yollar ayrı kalır ve
  `ayni_alan_farkli_yol` altında insan incelemesi için listelenir. Her
  adresin `kaynaklar` alanında hangi sayfada, hangi `page_url` ve
  `observed_at` ile, hangi etiketle geldiği tutulur.
- **Onarım ve kısaltıcılar:** `http://https//alan/…` biçimindeki çift şema
  yazım hatası kesin bir kalıpla onarılır ve kayıtta `onarildi: true` olarak
  işaretlenir. `--kisaltici-coz` verilirse t.co, bit.ly gibi kısaltıcıların
  hedefi tek adımda güvenli denetimle okunur (301/302 ya da meta-refresh).
  Hedef kural denetiminden geçmezse ya da okunamazsa kayıt gerekçesiyle atlanır.
- **kontrol:** Önce `https://example.com/` ile ağı sınar. Bu başarısız olursa
  hiçbir siteyi işaretlemeden çıkış kodu 2 ile durur. HTTP adresleri **körü
  körüne HTTPS'e çevrilmez**: önce HTTPS denenir ve yalnız 2xx dönerse HTTPS
  kullanılır. Olmazsa özgün HTTP adresi işaretlenerek kalır.
- **Durumlar:** `calisiyor` (2xx), `korumali` (401/403/405/429/503, çalıştığı
  doğrulanamadı), `olu`, `reddedildi` (güvenlik kuralı), `kontrol_edilmedi`.
  Ana listeye **yalnız `calisiyor`** girer. Korumalı ve denetlenmemiş
  adresler "ÇALIŞTIĞI DOĞRULANAMAYANLAR" bölümünde, durumlarıyla birlikte
  yazılır.
- **uret:** Eksik ya da kurala uymayan açıklama varsa çıkış kodu 1 verir.
  Kurallar: tek tam cümle, 40–220 karakter, "Bu siteyle … ." kalıbı.
  Her açıklamanın `dayanak` alanı vardır: `site_metasi` (sitenin kendi
  başlığı/açıklaması), `genel_bilgi` (yaygın bilinen hizmet) ya da `yok`.
  `yok` olanlara açıklama yazılmaz; "NE İŞE YARADIĞI DOĞRULANAMAYANLAR"
  bölümünde yalnız Ekşi etiketiyle listelenirler.

## Güvenli URL denetimi (`araclar/url_denetim.py`)

- Yalnız `http`/`https`, yalnız 80/443 portu; adreste kullanıcı:parola olamaz.
- `localhost`, `.local`, `.internal` gibi iç adlar ve noktasız adlar
  reddedilir. Özel, loopback, link-local (169.254.x, bulut metadata),
  CGNAT, multicast, ayrılmış ve IPv4 eşlemeli IPv6 adresleri de reddedilir.
- Ad her adımda **bir kez** çözülür ve dönen adreslerin **hepsi** kamu olmalı.
  Bağlantı çözülen IP'ye sabitlenir; TLS yine alan adıyla doğrulanır.
- Yönlendirmeler elle izlenir (en fazla 5 adım) ve her adım aynı kurallarla
  yeniden denetlenir.
- `--proxy-ad-ile`: Bazı proxy'ler (bu bulut ortamınınki dahil) yalnız ada
  tünel açar. Bu modda ad yine yerelde çözülüp denetlenir ama bağlantıyı
  proxy kendi çözümüyle kurar. Sonuca `ip_sabit: false` yazılır ve DNS
  yeniden çözümleme koruması proxy'nin politikasına kalır. Proxy'siz
  (ör. Windows'ta doğrudan) çalışınca IP sabitleme tam uygulanır.

## Testler

```
python test/calistir.py                 # proxy'siz ortam
python test/calistir.py --proxy-ad-ile  # IP tüneline izin vermeyen proxy arkasında
python test/calistir.py --ag-yok        # yalnız yerel testler
```

`test/cikti/test-ciktisi.txt` dosyasına gerçek çıktıyı yazar. Fixture'lar
(`test/fixture/sentetik/`) ve `test/cikti/sentetik-ornek/` **sentetiktir,
gerçek Ekşi verisi değildir**. Yerel sunucu testi 127.0.0.1:80 portunu açar;
açamazsa o testler atlanır ve çıktıda "skipped" görünür.

## Masaüstüne ve C:\ klasörüne kopyalama (Windows)

```powershell
Copy-Item .\katalog.txt "$([Environment]::GetFolderPath('Desktop'))\eksi-az-bilinen-siteler.txt"
Copy-Item .\katalog.txt "C:\eksi-az-bilinen-siteler.txt"
```

`C:\` köküne yazmak yönetici izni isteyebilir.
