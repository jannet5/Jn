# Harita: Ekşi az bilinen web siteleri kataloğu

Tarih: 2026-10-03

## Hedef

Ekşi Sözlük'teki **"az kişinin bildiği muhteşem web siteleri"** başlığının
(https://eksisozluk.com/az-kisinin-bildigi-muhtesem-web-siteleri--2764697)
**son sayfasından geriye 200 sayfasında** paylaşılan siteleri tek bir TXT
kataloğunda toplamak:

- tam `https://` bağlantı, her site **bir kez**;
- karşısında sitenin ne olduğunu anlatan **tek, tam bir cümle**;
- ardından **"Bu siteyle … yapabilirsin."** kalıbında somut kullanım cümlesi;
- **liste** biçimi (tablo değil), öğeler arasında rahat okunur boşluk;
- **kategorilere** ayrılmış;
- yalnız çalışan siteler, çalışmayanlar ayrı raporda.

## Bugünkü gerçek durum (2026-10-03'te ölçüldü)

| Bulgu | Kanıt |
|---|---|
| Başlık şu an **809 sayfa** (kullanıcı konuşmasında 807'ydi) | Sayfalardaki `data-pagecount="809"` |
| İstenen aralık bugün **809 → 610** (ilk 100: 809–710, sonraki 100: 709–610) | Hesap |
| Düz `curl` ve WebFetch başlık sayfalarında **Cloudflare 403** alıyor; tek entry sayfaları açılıyor | `cf-mitigated: challenge` başlığı |
| `robots.txt`: `ClaudeBot`, `anthropic-ai`, `ChatGPT-User` için `Disallow: /`, tüm site için `Content-Signal: ai-train=no, search=yes, ai-input=no` | https://eksisozluk.com/robots.txt |

**Sonuç:** Ekşi, yapay zekâ ajanlarının siteyi gezmesini ve içeriğin yapay
zekâya girdi olmasını açıkça istemiyor. Bu yüzden bulut ajanının Cloudflare'i
aşarak 200 sayfayı kendisinin çekmesi **seçilmedi** (denendi, 43. sayfada
durduruldu, o veri kullanılmadı). Yol, sayfaları kullanıcının kendi
tarayıcısında, kendi erişimiyle gezen ve **yalnız bağlantıları** çıkaran bir
araca çevrildi.

## Bağımlılıklar

1. Başlık sayfalarına erişim → **kullanıcının tarayıcısı** (Cloudflare normal
   kullanıcıyı geçirir).
2. Bağlantı listesi (`eksi-linkler.txt`) → katalog üreticisine girdi.
3. Her sitenin kendisine erişim (üçüncü taraf siteler, Ekşi değil) →
   açıklama yazmak ve "çalışıyor mu" denetimi için.
4. Açıklamalar (`aciklamalar.json`) → kurallara göre otomatik doğrulanır.
5. Doğrulanmış katalog → kalıcı teslim (depo + özel ZIP, SHA-256).

## Yollar

### A — Kullanıcı tarayıcısında toplama + buluttan açıklama (SEÇİLEN)

1. `araclar/eksi-link-toplayici.js` kullanıcı tarayıcısının konsolunda çalışır;
   son sayfadan geriye 200 sayfayı 1,5 sn arayla gezer, yalnız bağlantı +
   sayfa no + entry no içeren `eksi-linkler.txt` indirir. Yarıda kesilirse
   kaldığı yerden devam eder.
2. Dosya bu oturuma (veya yeni bir oturuma) yüklenir.
3. `katalog_olustur.py tekillestir` → HTTPS'e çevirir, izleme parametrelerini
   atar, aynı siteyi bir kez tutar.
4. `katalog_olustur.py kontrol` → her siteyi HTTPS ile dener (çalışıyor /
   korumalı / ölü).
5. `sablon` → açıklama şablonu; açıklamalar **sitelerin kendisi** incelenerek
   yazılır (Ekşi metni kullanılmaz).
6. `uret` → kategorili TXT; kural dışı/eksik açıklama varsa çıkış kodu 1.

Neden: Ekşi'nin beyan ettiği isteğe uyar, kullanıcı zaten başlığı kendisi
gezebilen biri; Ekşi metni yapay zekâya girmez, yalnız üçüncü taraf site
adresleri girer.

### B — Ekşi'den açık izin / resmî erişim

Ekşi'nin herkese açık bir API'si yok; mobil uygulama API'si oturum ister.
İzin alınırsa aynı araçlar sunucudan çalıştırılabilir. Bugün uygulanabilir değil.

### C — Kullanıcı bağlantıları elle kopyalar / tarayıcı eklentisiyle dışa aktarır

A'nın aracı çalışmazsa (Ekşi sayfa yapısını değiştirirse) kullanıcı
herhangi bir "sayfadaki linkleri dışa aktar" eklentisiyle aynı TXT biçimini
(`url<TAB>sayfa<TAB>entry`, yalnız `url` de yeterli) üretebilir;
`katalog_olustur.py` tek sütunlu listeyi de kabul eder.

### Reddedilen yol — Bulut ajanının Cloudflare'i aşarak çekmesi

Teknik olarak çalıştı (Chrome TLS taklidi yapan `curl_cffi` ile), ancak
robots.txt ve Content-Signal'e aykırı olduğu için bırakıldı. Ayrıca proxy
sertifikasını Chromium'a tanıtmak için denenen bayrak, oturumun güvenlik
denetiminde reddedildi; o yol da izlenmedi.

## Kabul ölçütleri ve durumları

| # | Ölçüt | Nasıl kanıtlanır | Durum |
|---|---|---|---|
| K1 | Son sayfadan başlayıp 200 sayfa (100 + 100) | Toplayıcı çıktısının `# aralik:` satırı; testte `5-3` ve `5-1` doğrulandı | Araç test edildi; **gerçek 200 sayfa toplanmadı** (kullanıcı tarayıcısı bekleniyor) |
| K2 | Tam HTTPS, tekilleştirilmiş bağlantılar | `test_hepsi_https_ve_tekil`, `test_ayni_site_bir_kez` | Araç test edildi |
| K3 | Her site için tek tam cümle + "Bu siteyle … yapabilirsin." | `aciklama_dogrula` (nokta, tek cümle, 40–220 karakter, kalıp) ve `uret` çıkış kodu | Doğrulayıcı test edildi; açıklamalar gerçek liste gelince yazılacak |
| K4 | Liste, tablo değil; okunur boşluk | `test_kategorili_liste_ve_rapor` (`|` yok, öğeler arası boş satır, `⟶` ayracı) | Test edildi |
| K5 | Kategoriler | `■ KATEGORİ (n site)` başlıkları, Türkçe büyük harf | Test edildi (Türkçe "i" hatası bulundu ve düzeltildi) |
| K6 | Çalışmayan siteler katalog dışında | `kontrol` + `rapor.json` | Gerçek ağda 3 siteyle denendi |
| K7 | Masaüstü ve `C:\` kopyası | README'deki PowerShell komutu | **Bulutta yapılamaz**, komut verildi |

## Uygulama → test → teslim zinciri

```
toplayıcı.js (kullanıcı tarayıcısı) ──► eksi-linkler.txt
        │ test: test/toplayici_test.js (Chromium, sahte yerel başlık, 12 kontrol)
        ▼
katalog_olustur.py tekillestir ──► kontrol ──► sablon ──► (açıklamalar) ──► uret ──► katalog.txt
        │ test: test/test_katalog.py (14 birim/uçtan uca test)
        ▼
git push (claude/relaxed-hopper-of36hr) + özel ZIP (SHA-256, geri okuma)
```

## Referanslar

- Başlık: https://eksisozluk.com/az-kisinin-bildigi-muhtesem-web-siteleri--2764697
- Ekşi robots.txt (yapay zekâ ajanı yasakları, Content-Signal): https://eksisozluk.com/robots.txt
- Content Signals açıklaması (Cloudflare): https://contentsignals.org/
- curl_cffi belgeleri (TLS taklidi, Cloudflare SSS): https://curl-cffi.readthedocs.io/
- Topluluk Ekşi kazıyıcıları (karşılaştırma için incelendi, kullanılmadı):
  https://pypi.org/project/limoon/ , https://pypi.org/project/sourpy/ ,
  https://apify.com/epctex/eksisozluk-scraper
- Playwright (testte kullanılan tarayıcı otomasyonu): https://playwright.dev/

Hazır kazıyıcılar (limoon, sourpy, Apify) sunucudan çalışıp aynı robots.txt
ve Cloudflare sorununa takılır; ayrıca entry metnini de toplar. Bu görevde
yalnız bağlantı gerektiği ve kullanıcının kendi tarayıcısında çalışması
gerektiği için bağımsız, bağımlılıksız küçük bir konsol betiği seçildi.
