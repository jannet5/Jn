# Araştırma: benzer projeler ve bizim seçimimiz

Arama yöntemi: Google tarzı web aramaları (GitHub, npm, PyPI, forum, blog), sonra ilgili depoları klonlayıp kodunu okumak.

| Proje | Ne yapıyor | Anahtar? | Bize ne öğretti |
|---|---|---|---|
| [Etymology Explorer](https://github.com/thepushkarp/etymology-explorer) | Web uygulaması. Etymonline + Vikisözlük'ü kazıyıp yapay zekâ ile özetliyor | Evet (Anthropic) | Etymonline için `etymonline.com/word/<kelime>`, Vikisözlük için `en.wiktionary.org/w/api.php`. İkisini birlikte kullanmak mantıklı |
| [Tymon](https://github.com/kyn76/tymon) | Python komut satırı, Vikisözlük'ten köken çeker | Hayır | Anahtarsız Vikisözlük kazıma işe yarıyor |
| [wiktionary-scraper (npm)](https://npmjs.com/package/wiktionary-scraper) | JS kütüphanesi, İngilizce Vikisözlük köken bölümlerini ayrıştırır | Hayır | Tek ve çoklu "Etymology N" bölümleri ayrı ele alınmalı |
| [nisanyan-cli](https://www.piwheels.org/project/nisanyan-cli) (agmmnn) | Nişanyan Sözlük için komut satırı | Hayır | Uç nokta: `https://www.nisanyansozluk.com/api/words/<kelime>?session=1`, özel başlık yok. Yanıt `words[].etymologies[]` içinde `languages[0].name`, `romanizedText`, `originalText`, `definition` |
| tdk-py / sozluk (pub.dev) | TDK Güncel Sözlük sarmalayıcıları | Hayır | Uç nokta: `https://sozluk.gov.tr/gts?ara=<kelime>` |
| Apify Etymonline scraper | Ücretli barındırılan kazıyıcı | Evet | Bize uymaz |
| EtymDB | Vikisözlük'ten türetilmiş açık köken veritabanı | Hayır | Telefona yüklemek ağır, o yüzden seçmedik |

## Üç mimari ve seçim

1. **Sadece istemci (seçtiğimiz).** Telefon doğrudan Vikisözlük'e, Android'de ek olarak Nişanyan/TDK/Etymonline'a bağlanır. Sunucu yok, anahtar yok.
2. Telefona veritabanı gömmek. Hızlı ama büyük ve eskir. Kullanıcı istemedi.
3. Arada kendi sunucumuz (proxy). Hem maliyet hem bakım. Kullanıcı istemedi.

Backend zorunlu değil: Vikisözlük tarayıcıdan çağrılabilir (`origin=*` ile CORS açık). Diğer siteler tarayıcıda CORS ile kapalı olabilir, bu yüzden APK içinde küçük bir "yerel HTTP köprüsü" var (CORS'u aşar, sunucu gerektirmez).

## Bilinen sınırlar (dürüst not)
- Nişanyan, TDK ve Etymonline bu geliştirme ortamından 403/ulaşılamaz çıktı (ortam IP'si engelli). Kod, gerçek projelerin kullandığı uç noktalara göre yazıldı ve sahte yanıtla test edildi; **gerçek telefonda canlı denenmedi**.
- Vikisözlük anahtarsız ama IP başına hız sınırı var. Tek bir kullanıcı için sorun olmaz; test ortamında paylaşılan IP yüzünden sık 429 alındı.
