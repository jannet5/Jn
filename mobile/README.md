# Kelime Kökeni - Mobil Uygulama

Bir kelime ya da isim yazıyorsun, uygulama telefonunun interneti üzerinden doğrudan kökenini (hangi dilden geldiği, anlamı, nasıl evrildiği) getiriyor. **Aradan geçen hiçbir sunucu yok** — telefon doğrudan iki kaynağa bağlanıyor:

1. **Nişanyan Sözlük verisi** ([etimolojiturkce.com](https://api.etimolojiturkce.com) API'si) — yaygın Türkçe kelimeler için, key gerekmez.
2. **Claude (Anthropic)** — Nişanyan'da bulunamayan kelimeler/isimler için (örn. "Ahmet" gibi özel isimler) ve ham veriyi okunaklı hale getirmek için. Bunun için kendi Anthropic API key'ini uygulamanın **Ayarlar (⚙︎)** ekranından bir kere gireceksin; key telefonunun güvenli deposunda (Android Keystore / iOS Keychain) saklanır, hiçbir yere gönderilmez.

Telefona hiçbir veritabanı inmiyor, arka planda çalışan/senin yönetmen gereken bir sunucu yok.

## En hızlı yol: hazır .apk

Uygulama zaten derlenip sana bir `.apk` dosyası olarak gönderildi. Tek yapman gereken:

1. `.apk` dosyasını telefonuna indir, dosyaya dokun.
2. Android "bilinmeyen kaynaklardan yükleme" izni isterse ver, kur.
3. Uygulamayı aç, sağ üstteki **⚙︎** ikonuna dokun, bir Anthropic API key yapıştır ([console.anthropic.com/settings/keys](https://console.anthropic.com/settings/keys)'den ücretsiz alınır).
4. Bir kelime/isim yaz, "Ara"ya bas.

Bilgisayara, Node.js'e, Expo Go'ya ihtiyaç yok. Aşağıdaki bölümler kaynak koddan kendin bir sürüm derlemek/değiştirmek istersen içindir.

## Kaynak koddan kurulum (tek seferlik, ~10 dakika)

Kod bu bulut oturumunda çalıştığı için doğrudan senin telefonuna bağlanamıyor — aşağıdaki adımları kendi bilgisayarında yapman gerekiyor. Bunu sadece **uygulamayı telefonuna kurmak için** yapıyorsun; kurulduktan sonra bilgisayara ihtiyacın kalmıyor (bkz. aşağıdaki "Kalıcı kurulum" bölümü).

1. [Node.js](https://nodejs.org) indir ve kur (LTS sürümü).
2. Bu repoyu bilgisayarına indir:
   ```bash
   git clone <bu-reponun-github-adresi>
   cd Jn/mobile
   npm install
   ```
3. Telefonuna **Expo Go** uygulamasını indir (Play Store'dan ücretsiz).
4. Bilgisayarında:
   ```bash
   npx expo start
   ```
   Terminalde bir QR kod belirecek. Expo Go'yu aç, "Scan QR code" ile okut. (Telefon ve bilgisayar farklı ağlardaysa `npx expo start --tunnel` kullan.)
5. Uygulama telefonunda açılacak. Sağ üstteki **⚙︎** ikonuna dokun, Anthropic API key'ini yapıştır ([console.anthropic.com/settings/keys](https://console.anthropic.com/settings/keys)'den ücretsiz alınır, ilk kullanım için ücretsiz kredi de tanınır).
6. Artık bir kelime/isim yaz, "Ara"ya bas.

## Kalıcı kurulum: Expo Go'suz, gerçek bir uygulama olarak

Yukarıdaki yöntemde uygulamayı her açtığında bilgisayarında `npx expo start` çalışıyor olması gerekir. Bilgisayarsız, telefona kalıcı olarak kurulan gerçek bir uygulama (.apk) istersen:

```bash
npx eas-cli@latest login       # ücretsiz Expo hesabı
npx eas-cli@latest build -p android --profile preview
```

Bu komut Expo'nun bulutunda (senin bilgisayarında değil) bir .apk dosyası derler, sana bir indirme linki verir. O linkten .apk'yı telefonuna indirip kurarsın — bir daha bilgisayara ya da Expo Go'ya ihtiyacın kalmaz. Android'de "bilinmeyen kaynaklardan yükleme"ye izin vermen gerekebilir (Ayarlar > Güvenlik).

## Güvenlik notu

Anthropic API key'ini uygulamanın kendi Ayarlar ekranına giriyorsun ve telefonun güvenli deposunda saklanıyor — bu, key'i senin dışında kimsenin göremeyeceği anlamına gelir **sen bu .apk dosyasını başkasıyla paylaşmadığın sürece**. Kendi kullanımın için bu güvenlidir. Riski daha da azaltmak için [console.anthropic.com](https://console.anthropic.com/settings/limits)'dan hesabına aylık bir harcama limiti koymanı öneririz.

## Notlar

- Sonuç kartında **"Nişanyan Sözlük"** rozeti varsa bilgi doğrulanmış bir kaynaktan geliyor demektir. **"Yapay zeka tahmini"** rozeti varsa (genelde özel isimlerde) bilgi kesin bir sözlükten değil, yapay zekanın kendi bilgisinden geliyor — kartın altında küçük bir uyarı notu da görünür.
- Aynı kelimeyi tekrar sorduğunda cevap telefonunda 1 hafta önbellekte tutulur, tekrar yapay zekaya gidip para harcanmaz.
- Son aramaların arama kutusunun altında küçük etiketler olarak görünür, onlara dokunarak tekrar arayabilirsin.
- API key girmezsen uygulama yine çalışır: Nişanyan Sözlük'te bulunan kelimeler için ham veriyi gösterir, bulunamayanlar için (isimler gibi) key eklemeni ister.
