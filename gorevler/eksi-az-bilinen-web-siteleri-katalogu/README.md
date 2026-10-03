# Ekşi az bilinen web siteleri kataloğu

Ekşi Sözlük'teki "az kişinin bildiği muhteşem web siteleri" başlığının son
200 sayfasındaki siteleri kategorili, açıklamalı bir TXT listesine çeviren
araçlar.

> **Durum:** Araçlar hazır ve test edildi. **Katalog henüz üretilmedi**,
> çünkü Ekşi yapay zekâ ajanlarının siteyi gezmesini `robots.txt` ile
> yasaklıyor. Bağlantıları senin tarayıcında toplaman gerekiyor (aşağıda 1.
> adım, yaklaşık 6 dakika). Ayrıntı: [harita.md](harita.md).

## 1. Bağlantıları topla (senin bilgisayarında, tarayıcıda)

1. Başlığı aç: https://eksisozluk.com/az-kisinin-bildigi-muhtesem-web-siteleri--2764697
2. `F12` → **Console** sekmesi.
3. [`araclar/eksi-link-toplayici.js`](araclar/eksi-link-toplayici.js) dosyasının
   tamamını kopyalayıp yapıştır, `Enter`.
   (Chrome "allow pasting" yazmanı isterse önce onu yazıp Enter'a bas.)
4. Sayfalar teker teker gezilir (son sayfadan geriye 200 sayfa, sayfa başı
   1,5 sn). Bitince **`eksi-linkler.txt`** iner.
5. Bu dosyayı Claude oturumuna yükle. İçinde yalnız bağlantılar, sayfa ve
   entry numaraları var.

Yarıda kalırsa aynı sekmede betiği tekrar yapıştır; kaldığı yerden devam eder.
Farklı sayfa sayısı için önce konsolda
`window.__eksiLinkAyar = { sayfaAdedi: 100 }` yaz.

## 2. Kataloğu üret

```
python araclar/katalog_olustur.py tekillestir eksi-linkler.txt -o siteler.json
python araclar/katalog_olustur.py kontrol siteler.json
python araclar/katalog_olustur.py sablon siteler.json -o aciklamalar.json
#   aciklamalar.json içindeki her site için kategori, "ne" ve "yapabilirsin" doldurulur
python araclar/katalog_olustur.py uret siteler.json aciklamalar.json -o katalog.txt
```

`uret`, eksik ya da kurala uymayan açıklama varsa çıkış kodu 1 verir ve
`rapor.json` içinde hangi sitede ne eksik olduğunu yazar.

Katalogdaki her satır şöyle görünür:

```
■ DOSYA VE DÖNÜŞTÜRME  (12 site)

•  https://cloudconvert.com/   ⟶   CloudConvert, yüzlerce dosya biçimini tarayıcı üzerinden birbirine dönüştüren bir sitedir.  Bu siteyle bir videoyu program kurmadan MP3'e ya da bir PDF'i Word belgesine çevirebilirsin.

```

## 3. Masaüstüne ve C:\ klasörüne kopyala (Windows)

PowerShell'de, `katalog.txt` dosyasının bulunduğu klasörde:

```powershell
Copy-Item .\katalog.txt "$([Environment]::GetFolderPath('Desktop'))\eksi-az-bilinen-siteler.txt"
Copy-Item .\katalog.txt "C:\eksi-az-bilinen-siteler.txt"
```

`C:\` köküne yazmak yönetici izni isteyebilir; izin vermek istemezsen
`C:\Users\Public\` gibi bir klasör kullan.

## Testler

```
python test/test_katalog.py                              # 14 test
python test/sahte_sunucu.py 8765 &                       # sahte başlık sunucusu
node test/toplayici_test.js 8765                         # 12 kontrol, Playwright + Chromium
```

Sahte sunucunun tüm içeriği uydurmadır; Ekşi'den veri içermez.
