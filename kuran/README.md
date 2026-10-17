# Kur'an Okuyucu

Çok hafif, hızlı, kütüphanesiz Kur'an okuyucu. Telefonda ana ekrana eklenir, ilk açılıştan sonra internetsiz çalışır.

## Özellikler
- Medine Mushafı: 604 sayfa, 30 cüz, 114 sure. Sayfalar sağdan sola çevrilir (parmağı soldan sağa kaydır = sonraki sayfa).
- Yakınlaştırma yazıyı büyütür ve satırları yeniden dizer, yana kaydırma gerekmez. `A−` / `A+` düğmeleri ya da iki parmak.
- **Git** paneli: Sure | Cüz | Sayfa yan yana. Aynı anda yalnızca biri geçerli. "Ma" yazınca Mâide... sıralanır, listeden seçince hemen gider. Sure numarası adın yanında yazar.
- **Sayfa numarası** alt ortada tek başına görünür (kitaptaki gibi). Dokununca sayfa seçme paneli açılır.
- Açılışta ayar sormaz: kaldığın sayfadan ve kaydırma yerinden devam eder.
- **Kaldığım yer işareti:** ayet sonundaki yuvarlağa dokun, o ayet vurgulanır ve **kilitlenir**: başka yuvarlağa dokunmak ya da kaydırmak işareti oynatmaz. Kaldırmak için işaretli yuvarlağa dokunup "Kaldır"a bas. Başka sayfadayken "Kaldığım yere dön" düğmesi çıkar.
- **6 göz yormayan tema:** Gündüz, Sepya, Nane (yeşil), Gri gece, Kehribar, Siyah (+ Otomatik). Koyu temalarda saf siyah/beyaz parlaması olmasın diye yazı soluk tutulur (ana yazı kontrastı 9–11:1).
- Sure araması Türkçe adı öne alır: "a" yazınca Âl-i İmrân, A'râf... gelir; "Baqara" gibi yabancı yazımlar da bulunur.

## Kur'an metni bozulmaz
- Metin Tanzil Uthmani kaynağındandır. `tools/build_data.py` yalnızca iki şey yapar: BOM karakterini atar ve kaynakta her surenin ilk ayetine eklenmiş besmele önekini ayıklar (besmele surenin başında ayrı gösterilir).
- Sayfa ve cüz sınırları iki bağımsız kaynakla (Tanzil metadata + alquran.cloud) birebir doğrulanır; uyuşmazlıkta derleme durur.
- İkinci bağımsız kaynakla (quran.com, KFGQPC) harf harf karşılaştırıldı: 6236 ayetin 6234'ünde harf iskeleti aynı. Fark yalnızca Yûsuf 12:39 ve 12:41'de: Tanzil "صَىٰحِبَىِ", KFGQPC "صَـٰحِبَىِ" yazıyor. Aynı harfin iki kodlanışı, ekranda aynı görünür. Metne dokunulmadı.
- `tools/verify_render.mjs`: 604 sayfayı tarayıcıda çizer, ekrandaki metnin veriyle birebir aynı olduğunu ve ayetlerin sırayla eksiksiz geldiğini doğrular.
- Tüm ayetlerin SHA-256 özeti `tools/CHECKSUM.txt` içindedir.
- Yazı tipi Amiri Quran (OFL), kullanılan tüm 69 karakteri ve işareti içerir. CSS'te harf aralığı, dönüşüm ve kelime bölme yoktur.

## Çalıştırma
```
cd www && python3 -m http.server 8765    # sonra http://localhost:8765
```
Telefonda: tarayıcıda aç, "Ana ekrana ekle".

## Android uygulaması (APK ve Google Play AAB)
- `dist/kuran.apk`: doğrudan telefona kurmak için (2,2 MB, internet izni yok, tamamen çevrimdışı). Dosyayı indirip aç ("Bilinmeyen kaynaklardan yükleme"ye izin ver).
- `dist/kuran.aab`: **Google Play'e yüklenecek dosya**. Yayın adımları: `play/YAYIN_REHBERI.md`. Mağaza metinleri `play/ilan-tr.md`, görseller `play/assets/`, gizlilik politikası `../docs/kuran-gizlilik.md`.
- Hedef/derleme sürümü Android 16 (API 36), en düşük Android 7.0 (API 24). Kenardan kenara ekran ve yeni geri hareketi desteklenir.
- Tam ekran WebView içinde `www/` klasörünü çalıştırır. Web uygulaması tek kaynaktır, ayrı bir kopya yok.
- Geri hareketi Git panelini kapatır. Telefonun yazı boyutu ayarı Kur'an düzenini bozmaz (uygulamanın kendi yakınlaştırması var). Okurken ekran kararmaz. Tema değişince çubuk simgeleri de uyar.

**İmza anahtarı depoda YOK** (`android/keystore/`, `keystore.properties` git'e girmez). Anahtar ayrıca teslim edildi. Daha önce depoya girmiş eski anahtar (`kuran-release.jks`) herkese açık olduğundan bir daha kullanılmamalıdır.

Yeniden derleme (Android SDK 36, Java 17+ ve Gradle 8.13+ gerekir):
```
cd android && echo "sdk.dir=$ANDROID_HOME" > local.properties
# imza için android/keystore.properties (storeFile, storePassword, keyAlias, keyPassword) ve anahtar dosyası gerekir
gradle bundleRelease assembleRelease
# -> app/build/outputs/bundle/release/app-release.aab ve apk/release/app-release.apk
```
Notlar: Release derlemesinde lint kapalıdır (lint'in kendi bağımlılığı kısıtlı ağlarda inmiyor). `settings.gradle.kts` içindeki Google Maven Central aynası, bazı ağlarda mavenCentral()'ın getiremediği dosyalar içindir.

## Veriyi yeniden üretme
```
python3 tools/build_data.py <ham_klasor> www/data.js
```
`<ham_klasor>`: `cloud-uthmani.json` (api.alquran.cloud/v1/quran/quran-uthmani) ve `tanzil-meta.js` (tanzil.net/res/text/metadata/quran-data.js).

## Kaynaklar ve lisanslar
- Metin: [Tanzil](https://tanzil.net) Uthmani (değiştirilmeden kullanılmalıdır). Meta veri CC BY 3.0.
- Yazı tipi: Amiri Quran, SIL Open Font License 1.1 (`www/fonts/OFL-AmiriQuran.txt`).
