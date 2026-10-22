# İstek defteri

Kaynak: kurucunun "Bildirim uygulaması için istekler" dosyası (21 bölüm).
Her satır bir istek. Durum işaretleri:

- [ ] yapılmadı
- [x] yapıldı ve kanıtlandı (kanıt satırın sonunda)
- [~] kısmen yapıldı (neyin eksik olduğu yazılı)
- [-] bu ortamda yapılamaz (neden yazılı, yerine ne yapıldığı yazılı)

Bu dosya iş bitene kadar güncellenir. Son durum en altta özetlenir.

## 1. Amaç ve ürün

- [x] I-01 Tüm kanallardan gelen mesajlar tek uygulamada toplanır, kişi tek yerden görür.
  - ChannelRepository.kt + MainViewModel.kt (state combine) + MainContent.kt; ekran görüntüsü app/build/shots/01-ana-ekran-kapali.png (WhatsApp, WhatsApp Business, Instagram, Telegram tek listede); MainViewModelRobolectricTest 'first launch preselects...' yeşil. Gerçek telefonda gerçek uygulamalarla doğrulama yok.
- [x] I-02 Geri tuşu uygulamadan çıkarmaz; önce açık şeyi kapatır, ana ekranda arka plana alır.
  - MainScreen.kt BackHandler: menü -> viewModel.collapseAll() -> moveTaskToBack(true); ImageViewer Dialog, picker/destek ModalBottomSheet kendi geri tuşunu işler; MainViewModelRobolectricTest 'back closes open cards first...'. BackHandler/moveTaskToBack'in kendisi için UI testi yok, yalnız collapseAll testli.
- [~] I-03 Marka uyumlu, tam bir marka tasarımı.
  - Brand.kt/BrandTokens.kt, tools/apply_brand.py, docs/marka/konsept-1..5 (svg+json), adaptive+mono ikon, gömülü fontlar, shots/marka/*.png; ama commit 1e2ce6f konsept 2'yi 'provisional' uyguladı, kurucu seçimi yok, uzun marka görseli ve reklam seti yok, marka rehberi bütünü eksik.
- [x] I-04 Eklenebilecek özellikleri internetten araştır.
  - docs/arastirma/01 ('Kimsede olmayan fırsat listesi'), 02 ('Hiçbirinde olmayan...'), 03 ('Fırsatlar' F1-F9, 'Ne yapalım'), 06, 07; linkli kaynaklar; docs/ROADMAP.md.
- [~] I-05 Prototip değil gerçek ürün; Google Play'e satışa çıkacak; Facebook/Instagram reklam görselleri.
  - Gerçek ürün zemini var: play flavor + PlayPurchaseGateway.kt, RELEASE.md (mağaza metinleri, data safety, imza), 70+ test yeşil (app/build/test-results). Eksik: Play yayını kullanıcı eylemi (RELEASE.md), gerçek release anahtarı yok, Facebook/Instagram reklam görselleri üretilmemiş (docs/marka'da yalnız konsept panoları).

## 2. Araştırma ve ihtiyaçları belirleme

- [~] I-06 Bir mobil uygulamanın tüm aşamalarını araştır ve sistemleştir.
  - docs/fabrika/01-ADIMLAR.md (M0-M12, kabul ölçütlü) ve 00-ILK-ISTEM.md bu uygulamanın aşamalarını sistemleştiriyor; genel 'mobil uygulamanın tüm aşamaları' kaynaklı araştırması yok, belge 68 satır ve uygulamaya özgü.
- [~] I-07 Hazır araç, site, skill, MCP kullan; kapalı kutu kodlama yapma.
  - Hazır parçalar kullanılıyor: app/build.gradle.kts (Room, DataStore, Koin, Roborazzi, Robolectric, Play Billing), OFL fontlar, docs/arastirma/07 (araç incelemesi). Skill/MCP'nin fiilen kurulup kullanıldığına dair kanıt yok (07 aitmpl CLI'yi çalıştırmamayı öneriyor).
- [~] I-08 Reddit, X, Stack Overflow, YouTube vb. global kaynaklardan araştır; linkli not al.
  - docs/arastirma/03 kaynak dizini: 23 Reddit, 11 Stack Overflow, 9 GitHub/forum, Hacker News, hepsi https linkli; ama X (WebFetch 402, 05 satır ~1006) ve YouTube (07 satır ~1342) erişilemedi, yalnız başlık/doğrulanamadı notu var. Linklerin canlılığı bu ortamda doğrulanmadı.
- [x] I-09 Rakipleri belirle: 20 GitHub, 20 Google Play, başka mecralar; https linkleriyle.
  - docs/arastirma/01: ~57 Play kartı (63 play.google.com linki), 20 uygulamalık özellik sayımı; 02: 26 klonlanmış GitHub projesi kartı (https linkli) + 16 klonlanmayan; F-Droid, Reddit, XDA, Product Hunt gibi diğer mecralar 01 'Google Play dışı mecralar'. Denetim: 01'de 65 play.google.com geçişi / 60 tekil paket id …
- [~] I-10 GitHub rakiplerini klonla, kod/mimari/arayüz notu çıkar; Play rakiplerini incele.
  - DÜŞÜRÜLDÜ. docs/arastirma/02'de 26 kart ve 'Mimari karşılaştırma' var, kod/mimari notu gerçek (dosya linkli, commit hash'li). Ama istenen 'arayüz notu' kartlarda yalnız teknoloji etiketi ('Arayüz: Compose, Material 3' / 'Views' / 'Flutter'); ekran düzeni, akış …
- [~] I-11 Rakip yorumlarındaki şikayetleri not al, bizde çöz.
  - Şikayetler notlandı: 03 K1-K22 + '10 şikayet' listesi, 10.124 Play yorumu. Uygulamada çözülenler: dinleyici yeniden bağlanma + 'Dinleme duraklamış' kartı (SendeNotificationListenerService.kt, MainContent), tekrar/kesik metin (S-09), geri alma, OemHints …
- [~] I-12 Notisave'i baştan sona incele (özellik, arayüz düzeni, davranış).
  - docs/arastirma/01 'NOTISAVE DETAYI': özellikler, ana ekran düzeni (ölçülü), ayarlar, ücretli özellikler, sevilen/sevilmeyen, davranış. Kaynak Play metni/mağaza görseli/yorumlar; uygulama çalıştırılmadı, 'sistem nasıl çalışıyor' çıkarım ('kod okunmadı').
- [~] I-13 20 uygulamanın özellikleri tek txt; bize entegre edilebilecekler ayrı txt.
  - İçerik var ama iki ayrı txt yok: docs/arastirma/01 '20 uygulamanın özellik sayımı' (satır ~1445) ve '/TekPanel için çıkarımlar - Ne yapalım' + 02 'Bize entegre edilebilecek fikirler' md bölümleri olarak. Repoda .txt dosyası yok (git ls-files).
- [~] I-14 Rakipler dışında kendi fikirlerini ve kullanım senaryolarını ekle.
  - Kendi fikirleri var: 03 'Fırsatlar' F1-F9 (yanıt bekleyen görünüm, IBAN/telefon algıla...), 01 'Kimsede olmayan fırsat listesi'. Ayrı, açık bir 'kullanım senaryoları' (berber/kuaför günü vb.) bölümü yok.
- [x] I-15 Hazır paket/şablon/skills araştır (aitmpl.com vb.), mümkün olduğunca hazır parça kullan.
  - docs/arastirma/07 §10 'aitmpl.com incelemesi' (search-index taraması, Android'e özel yalnız android-cicd/android-mcp, telemetri/güvenlik notu, tek tek liste) ve §11 Android hazır paketleri. Denetim: §10 (satır 729) ve §11 (satır 904) belgede gerçekten var, bileşen sayfa linkleri tek tek listeli.
- [~] I-16 Dağıtım: en erişilebilir ve hızlı yolu araştır (APK ve alternatifler).
  - Uygulanan yol: dist/README.md (GitHub release ek dosyası, sabit link), docs/ROADMAP.md Faz 9b, SORUNLAR_COZUMLER.md S-04/S-05 (raw/jsDelivr denendi). Alternatifleri (Drive, F-Droid, Firebase App Distribution, Obtainium vb.) karşılaştıran araştırma yok; telefonda indirme doğrulanmadı.
- [~] I-17 Şirket mantığıyla ajan sistemi: koordinatör + uzmanlar, hepsi internetli.
  - Araştırma belgelerinin başlığında 'Araştırma kolu: ...' ile paralel uzman kollar görülüyor (docs/arastirma/01-08); ajan şirketi/koordinatör düzenini tarif eden belge veya kanıt repoda yok.

## 3. Bildirimler uygulamaya düşer

- [~] I-18 Mesajlar otomatik düşer (manuel ekleme yok); mevcut bildirimler de düşer.
  - Otomatik yakalama: SendeNotificationListenerService.kt (onNotificationPosted + seçilen uygulama eklenince/bağlanınca activeNotifications yeniden işlenir), CaptureCoordinatorRobolectricTest (12 yeşil), androidTest NotificationListenerRealDeviceTest (CI'da, burada çalıştırılamaz) …
- [x] I-19 Veriler kullanıcıda kalır, başkasına gitmez.
  - Kaynak manifest yalnız RECEIVE_BOOT_COMPLETED (app/src/main/AndroidManifest.xml), CI INTERNET denetimi (.github/workflows/android-ci.yml:36; find yolu merged_manifests/sideloadRelease doğru), data_extraction_rules.xml/backup_rules.xml DB (sende_inbox.db = SendeDatabase.DATABASE_NAME) + files/media/ (MediaStore) hariç …

## 4. Kanallar ve uygulama seçimi

- [x] I-20 Telefondaki tüm uygulamalar listelenir, tikle seçilir, "Tümü" seçeneği vardır.
  - AppInventory.kt (launcher intent, QUERY_ALL_PACKAGES yok), ChannelPickerSheet.kt (onay kutuları + 'Tümü' satırı + arama), ChannelPickerViewModel.setAllVisible; app/build/shots/09-kanal-secici.png. Ekran görüntüsünde kullanılan ikonlar sahte; picker VM için birim testi yok …
- [x] I-21 Çok mesaj alanlar ve seçilenler üstte; alfabetik sıra değil.
  - ChannelOrdering.pickerBaseOrder (seçili -> geçmişli -> popüler -> A-Z) ve barOrder (mesaj sayısı); ChannelOrderingTest (6 test yeşil); shots/09 (seçililer ve sayılar üstte). Denetim notu: shot 09 sabit fixture sırasıyla çizildi, sıralamayı kanıtlamaz; asıl kanıt ChannelOrderingTest (6/6, 21 Eki 14:10 taze).
- [~] I-22 Dokunulan öğe bıraksa bile üstte kalır, eski yerine atlamaz.
  - DÜŞÜRÜLDÜ. Saf fonksiyon ChannelOrdering.withTouchedOnTop testli, ama birleşik davranış testsiz ve kodda iki açık var (çalıştırılmadı, kod okumasından): (1) MainScreen.showPicker rememberSaveable; manifestte configChanges yok, döndürmede Activity yeniden oluşur …
- [x] I-23 "Kanalları ayarla" yalnız seçim ekranını gösterir.
  - MainScreen.kt onChannels = { menuOpen=false; showPicker=true } yalnız ChannelPickerSheet açar (başlık 'Kanalları ayarla', yalnız arama+onay kutuları+Tamam); shots/06-menu-acik.png ve 09-kanal-secici.png.
- [~] I-24 Paket adı (com.whatsapp) hiçbir yerde görünmez.
  - Ana arayüzde paket adı yok: etiketler PackageManager'dan (AppInventory.kt), yoksa 'Uygulama'/boş; shots/01,05,09 temiz. Ama gizli tanılama ekranı DiagnosticsScreen.kt:38 'Son görülen paket' olarak paket adı gösteriyor ve DataExporter JSON'u sourcePackage içeriyor; otomatik test yok.
- [x] I-25 Ana ekranda yan yana kanal balonları; dokununca yalnız o kanal; en başta "Tümü".
  - ChannelBar.kt (LazyRow, 'Tümü' ilk, gerçek ikonlu balonlar), MainViewModel.select; MainViewModelRobolectricTest 'selecting a bubble filters to that app...'; shots/01-ana-ekran-kapali.png, 05-kanal-filtreli.png …
- [~] I-26 Logolar telefondaki uygulamaların gerçek ikonlarıdır.
  - AppIconLoader.kt:35 packageManager.getApplicationIcon(pkg) + AppIcon (Images.kt); ancak hiçbir test/ekran görüntüsünde gerçek ikon yok (görüntülerde ScreenshotFixtures'tan sahte harfli daireler), gerçek cihazda doğrulanmadı.
- [x] I-27 WhatsApp ve WhatsApp Business ayrı seçilir; kapalı kanal panele yansımaz.
  - Kanal=paket adı (com.whatsapp vs com.whatsapp.w4b ayrı satır, shots/09 ve 01); kapalı kanal: CaptureCoordinatorRobolectricTest 'disabling an app stops new captures' + 'an app the user did not pick is ignored', MainViewModel.kt satır ~68-71 kapalı paket konuşmalarını panelden süzer ve balon çubuğundan çıkarır …

## 5. Mesajların görünümü ve gruplanması

- [x] I-28 Aynı kişiden gelenler bir arada gruplanır.
  - InboxRepository.kt:26-40 groupBy conversationKey; InboxRepositoryRobolectricTest 'conversations group by chat...'; CaptureCoordinatorRobolectricTest 'messages of different people become different conversations'; shots/01-ana-ekran-kapali.png (Ayşe Yılmaz 3 mesaj tek kart)
- [x] I-29 Grup tek kapsayıcı kutu içinde durur.
  - ConversationCard.kt:87-150 tek Surface+kenarlık içinde başlık ve mesaj kutuları; shots/02-konusma-acik-mesajlar.png ve 03 görsel olarak doğrulandı
- [x] I-30 Dokununca en üste kayma hatası yok.
  - ScrollStabilityTest 'tapping a card deep in the list does not move the scroll position' (14 konuşma, 8. öğeye kaydır, dokun, aç/kapa, firstVisibleItemIndex sabit); sabit anahtar MainContent.kt:149 items(key = it.key) (kanıttaki 573 yanlış, dosya 171 satır). Not: test onCycle'ı sahte callback ile kuruyor …
- [~] I-31 Mesajlar kısa görünür, dokununca aynı kutuda uzar (yeni sayfa yok), tekrar dokununca kısalır; kutular eşit boyda.
  - Kısa 2 satır/dokununca aynı kutuda uzar/tekrar kısalır var: ConversationCard.kt:255 maxLines, MainViewModel.kt:136 toggleLengthened, VM testi 'tapping one message flips only that message'; shots 02/03. Eksik: kutular tam eşit boyda değil (02'de 1 satırlık kutu ~175px, 2 satırlık ~183px …
- [x] I-32 Kapsayana dokunma: altları açılır, sonra mesajlar uzar, sonra toplanır.
  - MainViewModel.kt:119-133 Collapsed->Children->Full->Collapsed (kanıttaki 398-412 yanlış satır); MainViewModelRobolectricTest 'a card of several messages steps closed to messages to full to closed' ve 'a single message card opens straight to full'; shots/01 (kapalı), 02 (mesajlar kısa), 03 (mesajlar uzun + altbilgi)
- [x] I-33 "Bildirim" etiketi yok; mesaj tam görünür (kesik değil).
  - Koddaki/kaynaklardaki hiçbir kart metninde 'Bildirim' etiketi yok (grep: yalnız izin başlığı ve Teşhis ekranı); Full seviyede maxLines=MAX (ConversationCard.kt:255), shots/03 tam metin; MessageNormalizerTest 'plain notification uses the longest of bigText and text'. Not: kapalı/kısa modda 2 satırda keser (I-31 gereği) …
- [x] I-34 Gelen görsel kutuda görünür, dokununca büyür.
  - shots/04-resimli-mesaj.png kutuda büyük görsel; ConversationCard.kt:292-311 PictureThumb tıklanır -> MainScreen.kt:198 viewerPath -> ImageViewer.kt (tam ekran, çift dokunma/çimdik zoom); CaptureCoordinatorRobolectricTest 'a picture-only message is kept'. Dokununca büyütmeyi çalıştıran otomatik UI testi yok (yalnız kod okuması)

## 6. Bildirim içeriği ve uygulamaya özgü işlemler

- [~] I-35 Kişinin profil resmi ve gelen fotoğraf görünür.
  - Kayıt+gösterim kodu var: AndroidNotificationMedia.kt (kişi ikonu/large icon, EXTRA_PICTURE, dataUri), CaptureCoordinatorRobolectricTest 'avatar and picture are stored once...', ConversationCard.kt:164 Avatar, shots/04 fotoğraf. Eksik: hiçbir ekran görüntüsünde gerçek profil fotoğrafı yok (fixture avatarPath=null …
- [x] I-36 Yanıtla / okundu gibi bildirim eylemleri çalışır.
  - LiveActionRegistryRobolectricTest 'a reply typed in Sende reaches the sending app's own reply action with the text', 'an app button fires its pending intent', 'a canceled reply intent reports failure'; ConversationCard.kt:324-413 Yanıtla/uygulama düğmeleri; shots/03 'Okundu işaretle', 'Beğen' …
- [~] I-37 Android bildirim çubuğunun güncel davranışı birebir: gruplama, genişleme, dokununca ilgili yere git.
  - Gruplama (kişi başına kart), genişleme (3 seviye), yanıt/eylemler var; araştırma docs/arastirma/04-android-bildirim-cubugu-davranisi.md. Eksik: Android'de gövdeye dokunmak uygulamayı açar, bizde dokunma kartı genişletir, 'Uygulamada aç' yalnız Full seviyedeki düğmede (ConversationCard.kt:395) …
- [~] I-38 Her uygulamanın kendi eylemleri (beğen, gör, yanıtla) bizde de görünür.
  - Kaynak uygulamanın düğmeleri gösteriliyor ve tetikleniyor: LiveActionRegistry.kt:85-95, NotificationExtractorRobolectricTest 'keeps the source app's own buttons but not its reply action', LiveActionRegistryRobolectricTest 'an app button fires its pending intent', shots/03. Eksik: en çok 3 düğme (MAX_ACTIONS) …
- [~] I-39 Dokununca ilgili mesaja/linke/canlı yayına gider (ana sayfaya değil).
  - IntentLauncher.kt:161-166 canlı contentIntent ile ilgili sohbete, yoksa uygulamanın ana ekranına düşer. Eksik: openConversation için test yok; pendingIntent.send() ActivityOptions'sız (IntentLauncher.kt:178 …

## 7. Okundu, temizleme, kopyalama

- [x] I-40 Mesaja dokunmak okundu yapmaz; yalnız "Okundu" düğmesi yapar.
  - ConversationCard.kt: başlık dokunuşu onCycle (satır 101), mesaj dokunuşu onToggle (229); yalnız ReadButton onMarkRead çağırıyor (117,131,144); MainViewModel.cycle/toggleLengthened (satır 119-138) depoya yazmıyor (kanıttaki 398-417 yanlış satır). Not: cycle/toggle VM testleri okundu durumunun değişmediğini açıkça sınamıyor …
- [~] I-41 Her mesajın yanında küçük "Okundu" yazısı (tik değil); dokununca o mesaj kalkar.
  - Küçük 'Okundu' metin düğmesi (ConversationCard.kt:314-322, tik değil), dokununca o mesaj kalkar (markRead -> InboxRepository.kt:52), VM testi 'marking read removes the message and undo brings it back', Geri al snackbar'ı …
- [~] I-42 Üstte "Ekranı tertemiz yap"; "emin misin" diye sorar.
  - Düğme+onay var: TopBar.kt:49-93 'Ekranı tertemiz yap', MainScreen.kt:231-243 ConfirmDialog; VM/Repo testleri geçiyor. Ancak MainViewModel.kt:161 clearScreen ekranda görünen (effectiveSelection) değil ham selectedPackage'ı kullanıyor …
- [~] I-43 Basılı tutunca mesaj kopyalanır.
  - Açık mesaj kutusunda basılı tutma: ConversationCard.kt:228-233 combinedClickable onLongClick -> MainScreen.kt:194-197 ClipboardManager + 'Kopyalandı' snackbar. Eksik: kapalı kart önizlemesinde (ConversationCard.kt:104-118) basılı tutma yok, önce kartı açmak gerek; kopyalamayı doğrulayan test yok
- [~] I-44 Aşağı kaydırırken kasmaz, akıcıdır.
  - Tasarım önlemleri var: LazyColumn sabit anahtar (MainContent.kt:149), snapshot map ile yalnız dokunulan kart yeniden çizilir (MainViewModel.kt:37), flowOn(Dispatchers.Default). Eksik: akıcılığı ölçen hiçbir test/benchmark yok (jank, Macrobenchmark); her kartta animateContentSize; gerçek cihaz ölçümü bu ortamda yapılamadı
- [x] I-45 Kutularda ".com.whatsapp" gibi alt yazı yok.
  - Kartta alt yazı olarak uygulama etiketi yazılıyor (ConversationCard.kt:180, MainContent.kt:152 labels[...]), paket adı değil; AppInventory.kt:10,48 'package name never is'; shots/01 'WhatsApp', 'WhatsApp Business', 'Instagram'; shots/09 seçicide de yok. Paket adı yalnız gizli Teşhis ekranında (DiagnosticsScreen.kt:38 …

## 8. İlk kullanım ve izinler

- [x] I-46 İlk açılışta zorunlu adım yok; doğrudan uygulamaya girilir.
  - MainActivity.kt:20-31 doğrudan MainScreen; onboarding ekranı/adımı yok; izin kartı bloklamayan liste öğesi (ui/access/AccessBanner.kt:23-27, MainContent.kt:126-130); ilk açılışta yaygın sohbet uygulamaları otomatik seçili (ChannelRepository.kt:25-29, VM testi 'first launch preselects...'); shots/07-izin-yok.png
- [x] I-47 İzin sebebi çok kısa anlatılır (uzatınca korkutur).
  - values-tr/strings.xml:80-82 access_body = 'Mesajlar bu ekrana böyle ulaşır. Telefonunda kalır.' (tek kısa cümle); shots/07-izin-yok.png; uzun açıklama yalnız isteğe bağlı 'Nasıl yapılır?' rehberinde
- [~] I-48 İzin düğmesi doğrudan ilgili ayar ekranına götürür, adımları atlatır.
  - AccessSettings.kt:270-283: API 30+ dinleyici ayrıntı sayfası -> dinleyici listesi -> uygulama bilgisi geri dönüşü; AccessBanner 'Erişimi aç' bunu çağırıyor (MainScreen.kt:185). Eksik: AccessSettings için test yok, gerçek cihazda hangi sayfaya düştüğü doğrulanmadı (bu ortamda yapılamaz) …
- [~] I-49 Ayar adımları görsel rehberle gösterilir (kurucunun ekran görüntüleri sonradan eklenecek).
  - 4 adımlı görsel rehber var: AccessGuideSheet.kt, shots/10-izin-rehberi.png. Eksik: çizimler kodla yapılmış sahte (Mock*) ekranlar; kurucunun gerçek ekran görüntüleri henüz eklenmemiş (istek metni 'sonradan eklenecek')
- [~] I-50 İzin verildiği halde tekrar sorma hatası olmaz.
  - NotificationAccessChecker.kt: 3 sinyalden biri yeterli (bağlı olma, isNotificationListenerAccessGranted, etkin dinleyici listesi); MainScreen.kt:90-110 ON_RESUME + 1,5 sn yoklama; SORUNLAR_COZUMLER.md S-08. Eksik: NotificationAccessChecker için birim testi yok; NotificationListenerRealDeviceTest yalnız bağlanmayı sınıyor …

## 9-10. Marka ve tasarım

- [~] I-51 100 mobil uygulama tasarımı incelenir, not alınır.
  - docs/arastirma/06-arayuz-tasarim-referanslari.md: 271 kaynak listesi ama gerçekten görülen tasarım ~27 küçük resim (10 Dribbble, 11 Behance, 4 Mobbin, 2 Google Messages; satır 79-122); bölüm 3'te ~10 uygulama notlu. 100 ayrı mobil uygulama tasarımı tek tek incelenip not alınmadı …
- [~] I-52 "AI slop" görünümünden çıkılır; AI ile mobil arayüz yapma yöntemi araştırılır.
  - Araştırma var: 06 bölüm 2 (slop işaretleri, kaçınma listesi) ve 07 (YZ ile arayüz yöntemi, üretim hattı). Uygulamada kısmen uyulmamış: app/build/shots/03 ve 13'te kart içinde kart (kaçınma listesi 'kartın içinde kart yok' diyor); seçilen Konsept 2, 06 satır 523'te Anthropic'in 1 …
- [~] I-53 Tek görselde numaralı 5 farklı marka konsepti; kurucu birini seçer.
  - docs/marka/secim/SECIM.png (2944x2219) açıldı: tek görselde numaralı 5 konsept ve gerçek uygulama render'ı var. Kurucunun seçtiğine dair kayıt yok; commit 1e2ce6f 'provisional concept 2' ve repoda başka seçim belgesi yok.
- [~] I-54 Seçilen marka tüm arayüz ve assetlerle uzun (vektör) bir görselde gösterilir, sonra uygulanır.
  - docs/marka/konsept-2.png ve konsept-2.svg açıldı: logo, ikon, renk, yazı, hareket, ses-üslup, ana ekran maketi, koyu tema var; uygulandı (tools/apply_brand.py -> ui/theme/BrandTokens.kt). Eksik: 1200x1500 pano 'uzun görsel' değil; kurucu onayı yok (geçici seçim) …
- [~] I-55 Uygulamaya özgü animasyonlar.
  - Kodda: ConversationCard.kt:94 animateContentSize(spring(markaYayı)), MainContent.kt:165 animateItem, TopBar.kt:60 ok dönüşü, TopBar.kt:123 AnimatedVisibility. Eksik: Brand.motion.expandMillis/dismissMillis hiçbir yerde okunmuyor (grep); panodaki 'Okundu ile kaybolma', 'kanal geçişi', 'rozet sayacı' animasyonları yok …
- [~] I-56 Premium his; logo var; "dandik" görünmez.
  - Logo/ikon var: res/mipmap-anydpi-v26/ic_launcher.xml (adaptif + tek renk), drawable/ic_launcher_*; ekran görüntüleri 01,06,13 tutarlı marka yazı tipleri/renkle. Eksik: uygulama içinde logo işareti yok (üst çubukta yalnız metin 'Sende'); marka geçici seçim, tasarımcı/kurucu onayı yok; 'dandik görünmez' öznel, doğrulanmadı.
- [~] I-57 Kullanıcı kuruluma güvensin (virüs korkusu).
  - dist/README.md 'Güven' bölümü; manifestte yalnız RECEIVE_BOOT_COMPLETED; CI sideload manifestinde INTERNET denetliyor (.github/workflows/android-ci.yml); uygulama içi 'Verilerin bu telefonda kalır' menüsü. Eksik: APK herkesin bildiği şifreli 'sideload-test.jks' ile imzalı (app/build.gradle.kts) -> güven vaadini zayıflatır …
- [~] I-58 Baştan sona marka sistemi (Duolingo gibi bütünlük ama Duolingo gibi görünmez).
  - Belirteç hattı var: docs/marka/konsept-N-belirtecler.json -> tools/apply_brand.py -> BrandTokens.kt, yazı tipleri, ikonlar, açık+koyu tema. Eksik: ui/common/Images.kt:81 sabit renk, 8 yerde sabit köşe (ConversationCard.kt:223,227,295; TopBar.kt:153 vb.) Brand.shapes'ı atlıyor; motion süreleri bağlı değil …
- [ ] I-59 Facebook/Instagram reklam görselleri.
  - Facebook/Instagram reklam görseli yok: docs/marka altında yalnız konsept panoları/ikon/logo var. Yalnızca ölçü araştırması var (docs/arastirma/08-marka-arastirmasi-ve-5-yon.md bölüm 12: 1080x1350, 1080x1920 boşlukları).

## 11. Sade ve rahat kullanım

- [~] I-60 Maksimum tek ekran; gereksiz her şey kalkar; tertemiz sade.
  - DÜŞÜRÜLDÜ (x->~). Eski kanıttaki 'Tek MainActivity' yanlış: AndroidManifest.xml satır 30-60'ta 3 activity var (MainActivity, capture.ShareIntakeActivity, ui.diagnostics.DiagnosticsActivity); teşhis alt sayfa değil ayrı Activity ekranı (gizli olsa da 'maksimum tek ekran' ile tam örtüşmez). Bu istek için otomatik test yok …
- [x] I-61 Menüler bir ok/slider arkasında, ekran kalabalık olmaz.
  - Doğrulandı: TopBar.kt:60 rotation animateFloatAsState(180f), :100-103 KeyboardArrowDown+rotate, MenuPanel AnimatedVisibility (expand/shrink); app/build/shots/01 (ok aşağı, menü kapalı) ve 06 (ok yukarı; Kanalları ayarla, Verilerin bu telefonda kalır, Mesajlarımı dışa aktar, Tüm verilerimi sil) açıldı ve tutuyor …
- [~] I-62 Alt butonlar telefonun gezinme tuşlarının üstüne binmez.
  - Kodda ele alınmış: MainActivity.kt enableEdgeToEdge; MainContent.kt:73 navigationBars alt boşluğu liste ve Snackbar'a ekleniyor; üç sayfada navigationBarsPadding. Eksik: bunu doğrulayan test/ekran görüntüsü yok (Roborazzi çıktılarında gezinme çubuğu yok); gerçek telefonda 3 tuş/hareket gezinmesi denenmedi.
- [~] I-63 Türkçe yazım hataları ve "?" glif sorunu yok.
  - Harfler tamam: values-tr/strings.xml okundu, ğşıİöüç doğru; app/build/shots/01,06,10,13 açıldı, '?' kutusu yok; yazı tipleri alt kümeli ama Latin-Ext içeriyor (apply_brand.py SUBSET_UNICODES). Hata: values-tr support_savings '%1$d%% tasarruf' -> shots/12-destek-plani.png'de '33% tasarruf' (Türkçede '%33 tasarruf' olmalı) …
- [~] I-64 Ekran görüntüsü alınabilir.
  - Engelleyen kod yok: app/src içinde FLAG_SECURE/setSecure/setRecentsScreenshotEnabled araması boş. Ekran görüntüsü alınabildiğini gösteren test veya gerçek cihaz doğrulaması yok.

## 12. Hız, kararlılık, güncelleme

- [~] I-65 Kasma yok, dokunma anında algılanır, "uygulama durduruldu" çökmesi yok.
  - Var: ScrollStabilityTest (karta dokununca kaydırma kayması yok), AppIconLoader LruCache + IO iş parçacığı, preview-smoke.sh (FATAL EXCEPTION araması, emülatör). Yok: ölçüm/kare süresi testi, baseline profile kaynağı, çökme yakalama (UncaughtExceptionHandler araması boş); gerçek telefonda akıcılık doğrulanmadı.
- [~] I-66 Uygulama kendi kendini günceller.
  - app/build.gradle.kts yorumu: sideload'da güncelleme kütüphanesi bilerek YOK (INTERNET gerekir); Play sürümü güncellemeyi Play'e bırakıyor (henüz yayında değil). Sideload için CI tek sabit bağlantıyı yeniliyor (dist/README.md) ama uygulama kendi kendini güncellemiyor; kullanıcı elle indirmeli. Görev #19 'In-App Update' kodda yok.

## 13. Planlama ve geliştirme biçimi

- [~] I-67 Tüm mesajlar oku, numaralı liste, plan, hepsini yap, yaptıkça üstünü çiz.
  - docs/ISTEKLER.md'de 107 numaralı istek listesi var ama hiçbir kutu işaretli değil (yaptıkça üstü çizilmemiş); görev listesi yalnız oturum içinde; docs/PLAN.md yok (git ls-files).
- [~] I-68 Mimari md: mevcut mimari, değişiklik, son mimari, sonra uygula.
  - docs/MIMARI.md: önceki hâl, değişenler, son hâl, nasıl uygulanır var. Eksikler: yazma sırası ters (çekirdek yeniden yazım 34330ed, MIMARI bfb6926'da sonra); 'assemblePreview' görevi yanlış (gerçek: assembleSideloadPreview); marka bölümü apply_brand.py'yi, flavor/ödeme/teşhis/paylaşım katmanlarını anmıyor …
- [~] I-69 En hızlı ama eksiksiz yol önce düşünülür.
  - Hızlı yol seçimi belgelenmiş: docs/PC_ONIZLEME.md ('En hızlısı: ekran görüntüsü hattı', önerilen düzen), docs/arastirma/07 (üretim hattı). Her iş için önceden 'en hızlı eksiksiz yol' planı kaydı yok (PLAN.md yok).
- [~] I-70 Kullanım amacıyla, gerçek kullanım biçimiyle test et.
  - Gerçek sistem testi: androidTest NotificationListenerRealDeviceTest (4 test, cmd notification post, gerçek dinleyici->Room), CI 4 API'lik emülatör matrisi, preview-smoke.sh; yerel koşu 112 birim/Robolectric testi 0 hata (app/build/test-results/testSideloadDebugUnitTest). Eksik: CI emülatör sonucu bu ortamda görülemedi …
- [~] I-71 Bu hali commit olarak kaydet (geri dönüş için).
  - DÜŞÜRÜLDÜ (x->~). Çalışma ağacı temiz ve işler commit'li (44 commit), ama eski kanıt bayat/yanlış: HEAD 0a9412e (8a03548 değil), 43 değil 44 commit; docs/arastirma/09 artık izleniyor (HEAD'de eklendi). Asıl sorun: 'geri dönüş noktası' olarak gösterilen 'son-surum' etiketi 4aaf99b'de …
- [x] I-72 Durma; aynı şeyi saatlerce deneme, takılırsan seçenek değiştir.
  - Doğrulandı: docs/SORUNLAR_COZUMLER.md S-14 (INTERNET şüphesi güncelleme kütüphanesinden Billing'e kaydı, iki flavor çözümü; CI android-ci.yml:36-41 sideload manifestte INTERNET'i yasaklıyor), S-05 açık ama raw+jsDelivr'e geçilmiş, S-04 raw+jsDelivr (398678e CI release varlığı gerçekten var ama S-04 kaydında anılmıyor …
- [~] I-73 Başka bir yapay zekanın sıfırdan yapabileceği "fabrika" paketi (kilometre taşlarıyla).
  - docs/fabrika/00-ILK-ISTEM.md (kopyalanacak istem, değişmez kurallar) ve 01-ADIMLAR.md (M0-M12, kabul komutları) var. Eksik: başlık 'hangi dosyalar' diyor ama adımlar dosya listelemiyor; marka belirteçleri/apply_brand.py/yazı tipleri pakete bağlı değil (M8 sıfırdan 5 konsept ister) …

## 14. Uzmanlar ve başka yapay zekalar

- [~] I-74 Ajanlar şirket gibi çalışır; hepsi internete bağlı, kodlara/projelere bakar.
  - docs/arastirma/01..09 web kaynaklı ajan araştırması var (URL'li); ama kod/proje bakan 'şirket gibi' ajan düzeni için repoda kayıt yok, bağımsız denetim görevi (#24) bekliyor
- [-] I-75 ChatGPT Pro hesabına tarayıcıdan girip araştırt (bu oturumda tarayıcı/bilgisayar aracı yok).
  - Bu ortamda tarayıcı/bilgisayar aracı yok (ISTEKLER.md satır 125 de böyle diyor)
- [-] I-76 Claude hesabına tarayıcıdan girip muhalefet ettir (aynı neden). Yerine: bağımsız muhalif ajanlar.
  - Aynı neden. Yerine geçen 'bağımsız muhalif ajan' denetimi repoda kanıtlı değil (görev #24 bekliyor)

## 15. Çalışırken iletişim

- [-] I-77 Fareni elinden almadan bilgisayar kullanımı (bu oturumda bilgisayar aracı yok).
  - Bu ortamda bilgisayar kullanım aracı yok
- [~] I-78 Yana kaydırmalı tablo verme; dar, madde madde yaz.
  - docs/MIMARI.md başında 'geniş tablo içermez' diyor ve madde madde; ama docs/RELEASE.md §1 ve §2 geniş tablo (SHA-256 sütunu, 17 satırlık CAP tablosu) içeriyor
- [ ] I-79 Hangi kodları kullandın, ne oldu: alt alta, öğretici.
  - Sohbet biçimi isteği; repoda doğrulanabilir kanıt yok (docs/SORUNLAR_COZUMLER.md yalnız sorun günlüğü, kod anlatımı değil)
- [~] I-80 "Ne kurdun" yazılım diliyle anlat.
  - docs/MIMARI.md §3-4 yazılım diliyle 'ne kuruldu' anlatıyor; ama sohbet anlatımı için kanıt yok, RELEASE.md §2 hâlâ olmayan dosyalara (ui/inbox/*, SourceAppCatalog.kt, LiveIntentRegistry.kt) işaret ediyor
- [~] I-81 Amaç ve özellikleri tireli, sohbet tarzında anlat.
  - docs/MIMARI.md §1 ve docs/RELEASE.md §6 tireli amaç/özellik listesi var; sohbet tarzı anlatım için kanıt yok
- [x] I-82 Çözülen sorunlar md dosyası; takılınca seçenek değiştir.
  - docs/SORUNLAR_COZUMLER.md (S-01..S-14, ne oldu/neden/çözüm/yeni kural; S-04 ve S-14'te yol değiştirme örnekleri); docs/fabrika/00-ILK-ISTEM.md 'iki denemede geçmezse dur, alternatif öner' kuralı

## 16. Gerçek kullanım ve test

- [~] I-83 Kullanıcı gibi test et, canlı izle; istediğim her şey yerine geldi mi kontrol et.
  - app/src/androidTest/.../NotificationListenerRealDeviceTest.kt (gerçek sistem bildirimi->Room, MessagingStyle, aynı anahtar), ScrollStabilityTest, MainViewModelRobolectricTest; eksik: gerçek WhatsApp/Instagram ve canlı izleme yok (ROADMAP.md 'sıradaki adımlar') …
- [~] I-84 3 farklı Android sürümünde test (emülatör son çare).
  - .github/workflows/android-ci.yml instrumented-tests matrisi API 26/30/34/36 (4 sürüm) tanımlı; bu ortamda çalıştırılmış sonuç/artifact yok (yalnız eski run bağlantısı RELEASE.md'de), emülatör CI'da var
- [ ] I-85 Ergonomi testi (başparmak bölgesi, buton yerleşimi), sadece kod testi değil.
  - Ergonomi/başparmak bölgesi testi yok; yalnız ChannelPickerSheet.kt:49 yorumunda niyet var. Ana ekranda menü ve 'Ekranı tertemiz yap' üstte (app/build/shots/01-ana-ekran-kapali.png), düzen ölçülmedi
- [ ] I-86 Virüs/zararlı kontrolü.
  - Virüs/zararlı taraması yok. CI yalnız INTERNET yokluğunu ve cancelNotification yasağını denetliyor (android-ci.yml), bu bir zararlı yazılım taraması değil; dist/README.md güven bölümü SHA-256 ve izin listesine dayanıyor
- [x] I-87 Otomasyonlu, tekrar kullanılabilir test sistemi.
  - Tekrarlanabilir hat: ./gradlew :app:testSideloadDebugUnitTest (JVM+Robolectric+Roborazzi), .github/workflows/android-ci.yml (unit, emülatör matrisi, preview-smoke), .github/scripts/preview-smoke.sh
- [~] I-88 Canlı izleme/log (uygulama ne yaptı görülebilsin).
  - DiagnosticsRecorder.kt + ui/diagnostics/* gizli ekranda sayaçlar, son karar, son hata var (mesaj içeriği bilerek yok); canlı akan log/izleme yok, E2E hata nedeni uygulama içinden okunuyor (commit 53155b9)

## 17. Bilgisayarda inceleme

- [x] I-89 Arayüzü bilgisayarda görmenin yolları sunulur, mantıklısı seçilir.
  - docs/PC_ONIZLEME.md 5 yol (Roborazzi PNG, Android Studio, scrcpy, bulut emülatör, Waydroid) + önerilen düzen; çalışan çıktı app/build/shots/*.png
- [x] I-90 Düzeltme için telefona gerek yok; kod üzerinden bilgisayarda çalışılır.
  - Telefonsuz düzeltme hattı çalışıyor: Roborazzi ekran görüntüleri app/build/shots/01..13-*.png ve dil/ar-ana-ekran.png, dil/fr-*.png açıldı, gerçek Türkçe/Arapça RTL çizim görüldü; docs/PC_ONIZLEME.md §1
- [x] I-91 Emülatör ağırsa alternatifler.
  - docs/PC_ONIZLEME.md §3-5: scrcpy, GitHub Actions KVM emülatörü, Appetize/BrowserStack, Waydroid; JVM'de emülatörsüz çizim ana yol

## 18. Teslim

- [~] I-92 Sistemin çalışması mühendislik diliyle baştan sona anlatılır (yapay zekaya verilecek).
  - docs/MIMARI.md (katmanlar, veri akışı) + docs/fabrika/00-ILK-ISTEM.md ve 01-ADIMLAR.md (M0-M12) var; ayrı 'mühendislik anlatımı' belgesi yok (görev #22 bekliyor), docs/RELEASE.md, README.md ve ROADMAP.md eski (35 test, olmayan dosyalar, 'Sende debug 20260927' APK)
- [ ] I-93 En sonda bir zip.
  - Repoda zip üretimi/teslim paketi yok; dist/ yalnız README.md içeriyor (zip commit 9bd0ef3 sonra kaldırılmış)
- [ ] I-94 Dosya verirken tam yolu da yazılır.
  - Sohbet kuralı; repoda kanıt yok
- [~] I-95 APK linki: tıklayınca insin, %100'de takılmasın, hızlı, hesapsız, aynı ağ şartı yok, veri başkasına gitmesin.
  - dist/README.md tek kararlı bağlantı (releases/download/son-surum/Sende.apk), CI release-apk işi (android-ci.yml) hesap gerektirmez, 2,8 MB, Content-Disposition başlığı; ama gerçek telefonda indirme DENENMEDİ, S-05 (%100'de takılma) AÇIK (docs/SORUNLAR_COZUMLER.md) …
- [~] I-96 Uygulamayı yap ve telefona yükle; beğenirsem Play.
  - APK üretiliyor (assembleSideloadPreview, preview-smoke emülatörde kurup açıyor); kullanıcının telefonuna kurulum doğrulanmadı (ROADMAP.md Faz 9b 'açık kabul maddesi'); Play yayını kullanıcı eylemi (RELEASE.md §7)
- [~] I-97 APK Drive'a yüklenir, eskiler silinir, isimde tarih ve saat (11.09.26 22:30 gibi).
  - android-ci.yml: eski asset'ler siliniyor, ad 'Sende dd.MM.yy HH.mm.apk' (Europe/Istanbul) biçiminde; ama hedef Drive değil GitHub release; Drive'a APK yüklenemedi (ROADMAP.md Faz 9b), Drive'da yok

## 19. Ülke ve dil

- [x] I-98 Tek uygulama, çok ülke; dil otomatik; açılışta soru sormadan çalışır.
  - values/ (EN varsayılan), values-tr, values-fr, values-ar hepsi 89 string + 1 plurals (sayım eşit); sistem diline göre otomatik, açılışta dil sorusu yok (kodda dil seçici yok, docs/DECISIONS.md §2); LocaleScreenshotTest -> app/build/shots/dil/ar-ana-ekran.png (RTL ayna) açıldı. Play ülke seçimi kullanıcı eylemi
- [~] I-99 İlk olarak Türkiye (Türkçe).
  - Türkçe arayüz tam (values-tr, Türkçe collator ChannelOrdering.kt, ekran görüntüleri 01..13 doğru ğ ş ı İ); Türkiye yayını için eksik: Play Console TL fiyatları ve gizlilik URL'si kullanıcıda (docs/ODEME.md, RELEASE.md §7), docs/arastirma/09-google-play-yayin-rehberi-turkiye.md git'e eklenmemiş (untracked)

## 20. Yapay zeka

- [~] I-100 Yapay zekasız sürüm yayına hazır hale gelir.
  - Sürüm kod olarak hazır (sideload/play flavor, targetSdk 36, R8 küçültme); yayın için eksik: gerçek release anahtarı yok (build.gradle.kts hasReleaseSigningConfig, release/keystore.properties yok), versionCode=1 sabit, docs/RELEASE.md eski, gizlilik politikası URL'si ve mağaza görselleri yok
- [ ] I-101 Yapay zekalı sürüm ayrı uygulama (yan yana kurulur); sonra geliştirilir.
  - YZ'li ayrı uygulama yok: app/build.gradle.kts yalnız sideload ve play flavor, YZ için ikinci applicationId yok
- [ ] I-102 YZ sürümü: cihaz içinde, internetsiz kategorize, mesaj altında YZ simgesi, o mesajla sohbet/cevap.
  - Cihaz içi YZ, kategorize, mesaj altı YZ simgesi/sohbet hiçbir yerde yok (kodda ml/llm sınıfı yok, ui/main/ConversationCard.kt'de YZ düğmesi yok)

## 21. Ödeme

- [~] I-103 Süreye göre abonelik: 1/3/6/12 ay (3 ay 10$, 6 ay = 4 ay parası, 12 ay = 9 ay parası); Türkiye.
  - PlayPurchaseGateway.kt: sende_support ürünü, monthly/quarterly/semiannual/yearly taban planları, fiyat Play'den, tasarruf % hesabı; app/build/shots/12-destek-plani.png 1/3/6/12 ay + %33/%33/%25 gösteriyor (sahte fiyatlı fixture); docs/ODEME.md 5$/10$/20$/45$ …
- [~] I-104 3 gün gizli deneme; sonunda fiyat ekranı.
  - monetization/TrialPolicy.kt + TrialPolicyTest.kt (8 test: 3 gün sessiz, sonra Paywall, 'şimdilik değil' günlük); SupportSheet trialEnded dalı var. Eksik: trialEnded=true paywall ve deneme çipi için ekran görüntüsü/UI testi yok (ScreenshotTest.kt:128 trialEnded=false), MainScreen bağlantısı testsiz
- [~] I-105 Ödeme Google Play üzerinden.
  - app/src/play/.../PlayPurchaseGateway.kt Play Billing 8.0.0 ile satın alma/onay/tüketme kodu; sideload flavor'da Billing yok (Dist.kt). Eksik: gerçek Play lisans testi/sandbox çalıştırması yok (kullanıcı eylemi), gateway birim testi yok
- [~] I-106 İki sürüm (uyaran / uyarmayan) 20'şer kişiye test edilebilir.
  - -Pmonetization=trial_silent|trial_warn (app/build.gradle.kts, MonetizationMode, ODEME.md) kodda var. Kanıtlı sorun: docs/ODEME.md iki ayrı AAB'yi iki kanala yüklemeyi söylüyor ama app/build.gradle.kts versionCode=1 sabit ve gradle özelliğiyle değişmiyor …
- [x] I-107 Zorunlu ödeme dayatması istemiyorum; bağış (donate) fikri için görüş.
  - Varsayılan donate modu hiçbir şeyi kilitlemez (MonetizationMode.DONATE, TrialPolicyTest 'donate mode never asks'); tek seferlik çay/kahve/yemek bağışı (PlayPurchaseGateway TIP_IDS, 12-destek-plani.png); docs/ODEME.md 'Neden varsayılan destek' bölümü görüşü veriyor
