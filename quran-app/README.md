# Kur'an Oku (Android)

Sadece Kur'an okumak için yapılmış hafif bir Android uygulaması. Reklam yok, internet izni yok, hesap yok.
Kur'an metninin tamamı uygulamanın içinde geliyor. APK yaklaşık **2 MB**.

## Özellikler
- **Medine mushafının birebir aynısı.** Kral Fahd Matbaası'nın (KFGQPC) Hafs metni ve yazı tipi; 604 sayfa, her sayfada 15 satır, her kelime basılı mushaftaki satırında. Ayet gülleri yazı tipinin kendi süsleridir.
- **Kaldığın yerden açılır.** Sayfalar mushaf gibi sağdan sola çevrilir. İstersen "Akan yazı" düzenine geçip çok büyük yazıyla okuyabilirsin.
- **Tek kutudan arama:** `ma` yazınca → Mâide, Mâûn · `20` yazınca → 20. Cüz · `s 302` yazınca → 302. sayfa · `2:255` ya da `bakara 255` yazınca → o ayet açılır ve vurgulanır. Aksanlı ya da aksansız, Türkçe, Latin ya da Arapça yazılabilir; küçük yazım hataları da tolere edilir.
- **Yumuşak yazı büyütme:** İki parmakla azar azar büyütülür. Görünüm panelinde %1 adımlı kaydırıcı ve yumuşak geçişli −/+ düğmeleri var (%65 – %255).
- **Sayfa görünümleri:** Mushaf (dokulu fildişi kağıt, kabartma yazı, altın çerçeve), Sade (beyaz/siyah), Lacivert, Gece, Mavi, Sepya.
- **Renkler:** 14 sayfa rengi, 12 yazı rengi ve istediğin her rengi seçebileceğin özel renk seçici. Doku ve çerçeve ayrı ayrı açılıp kapatılabilir. Yazıyla sayfa rengi birbirine çok yakınsa uyarı çıkar.
- **İçindekiler:** Sureler, Cüzler, Yer imleri. Yer imini silince "Geri al" çıkar.
- İstersen okurken ekran kapanmaz. Ayarlar ve yer imleri telefon yedeğiyle yeni telefona taşınır.

## Kurulum (telefona)
1. `dist/KuranOku-1.1.0.apk` dosyasını telefona indir.
2. Dosyaya dokun. "Bilinmeyen kaynaklardan yüklemeye izin ver" uyarısı çıkarsa izin ver ve **Yükle**'ye bas.
3. Android 8.0 ve üstü telefonlarda çalışır.

## Geliştirme
```bash
export ANDROID_HOME=/path/to/android-sdk
./gradlew :app:testDebugUnitTest     # arama ve veri testleri
./gradlew :app:assembleRelease       # imzalı APK (keystore.properties gerekir)
```
- Veri: `araclar/veri_uret_qpc.py` (quran.com QDC API, mushaf=5 KFGQPC Hafs → `assets/quran.tsv`, `assets/mushaf.tsv`, `assets/surahs.tsv`). Sayfa verisi `araclar/` içindeki curl komutuyla indirilir (bkz. fabrika/DERSLER.md).
- Kağıt dokusu: `araclar/doku_uret.py` → `res/drawable-nodpi/paper_texture.png`
- Tasarım kaynağı: `fabrika/DESIGN.md`. Ürün kararları `fabrika/` klasöründe.
- İmza anahtarı git'e konmadı; anahtar dosyası ve şifresi kullanıcıya ayrıca gönderildi. Güncellemeler aynı anahtarla imzalanmalı.

## Kaynaklar ve lisanslar
- Kur'an metni ve yazı tipi: Kral Fahd Kur'an-ı Kerim Matbaası (KFGQPC Hafs Uthmanic Script, değiştirilmeden). Satır düzeni: quran.com / Quran Foundation. Cüz/hizb numaraları: Tanzil.
- İkonlar: Lucide (ISC).
