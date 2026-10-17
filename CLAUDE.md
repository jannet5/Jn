# Bu depoda çalışan her Claude için kullanıcı kuralları

Kullanıcı Türkçe konuşur, telefondan yazar, bilgisayarı yoktur. Bu kurallar her oturumda geçerlidir.

## 1. Her şey okunur, her şey yapılır
- Kullanıcının bu projedeki **her mesajı** okunur; içindeki her istek bir maddedir. Hiçbiri atlanmaz.
- İş bitmeden "bitti" denmez. "Kısım kısım yok, full bitir": ara onay istenmez, aşamalar tek seferde yürütülür.
  Sadece zevke kalan seçimler (görünüm, renk, yazı tipi vb.) uygulamanın içine **ayar** olarak konur, kullanıcı seçer.
- Bitirince istek listesi çıkarılır ve her madde uygulamada gerçekten denenir (emülatör/test). Liste: `quran-app/fabrika/ISTEKLER.md`.
- Yapılamayan bir şey varsa açıkça ve tek seferde söylenir, nedeniyle birlikte; asla "yaptım" denmez.

## 2. Teslim: kart + Artifact linki
- Bitmiş iş her zaman **kart olarak** verilir: dosya `SendUserFile` ile gönderilir **ve** aynı dosya bir Artifact indirme sayfasıyla link olarak verilir.
- Android APK teslimi (kullanıcının telefonunda dosya kartı indirmesi çalışmadı):
  - Artifact sayfası: https://claude.ai/artifact/BgVRCn5d4H32wjRbp4KkxT (aynı URL'ye yeniden yayınla, yeni sayfa açma).
  - 1. yol: `downloads` yeteneğiyle Claude uygulaması kaydeder (`.apk` izinli değil → zip içinde APK).
  - 2. yol: jsDelivr linki `https://cdn.jsdelivr.net/gh/<owner>/<repo>@<branch>/<yol>.apk` (doğru APK türüyle gelir; GitHub raw `octet-stream` döner ve %100'de takılabilir).
  - 3. yol: linki kopyala → Chrome'a yapıştır.
  - Her yeni sürümde dosya adı sürümle değişir (jsDelivr önbelleği yüzünden), SHA-256 doğrulanır.
- Google Drive bağlayıcısı APK gibi büyük ikili dosya yükleyemez; Drive'a sadece belgeler yazılır. rclone kullanılmaz.

## 3. İletişim
- Kısa, samimi Türkçe ("kanka"). "Tek cümle/tek paragraf" istenirse öyle yazılır.
- Uzun iş sürerken ara ara ne yapıldığı tek satırla söylenir.

## 4. Kur'an Oku uygulaması (quran-app/)
- Metin, yazı tipi ve sayfa düzeni resmi kaynaktan: KFGQPC Hafs metni + yazı tipi (değiştirilmeden), quran.com QDC `mushaf=5` satır verisi. Elle uydurulmaz.
- Ayrıntılar: `quran-app/README.md`, `quran-app/fabrika/` (DURUM, DESIGN, DERSLER, ISTEKLER).
