# Android bildirim çubuğu ile eşleşme tablosu

Hedef: Android'in bildirim çubuğunun (NotificationManager + notification shade) çalışma mantığını backend + frontend olarak aynen uygulamak.
Bu tablo, her davranışın nerede uygulandığını, nasıl test edildiğini ve **bilginin kaynağını** açıkça yazar. "Doğrulandı" yalnızca bu oturumda
resmi/yayımlanmış bir kaynakla teyit edilenler içindir; diğerleri bilinen Android davranışına göre uygulanmıştır ve işaretlidir.

| # | Android davranışı | Burada | Test | Bilgi durumu |
|---|---|---|---|---|
| 1 | Aynı `(paket, id, tag)` ile `notify` mevcut bildirimi **yerinde günceller** | `Shade.post` | `aynı (pkg,id,tag) yerinde güncellenir` | Doğrulandı ([Notifee/Android belgeleri](https://docs.page/invertase/notifee/react-native/reference/Interface.NotificationAndroid)) |
| 2 | Bildirim için **kanal** şart (Android 8+); kanalsız gönderi gösterilmez | `unknown_channel` hatası | `bilinmeyen kanal reddedilir` | Doğrulandı ([kanal belgesi](https://developer.android.com/training/notify-user/channels)) |
| 3 | Kanal önemi 0–4; **uygulama oluşturduktan sonra değiştiremez**, kullanıcı değiştirir | `createChannel` (ad/açıklama güncellenir, önem kilitli), `setChannelImportance` | `uygulama kanalın önemini değiştiremez…` | Doğrulandı (kanal belgesi) |
| 4 | HIGH: ses + heads-up · DEFAULT: ses · LOW: sessiz · MIN: sessiz, durum çubuğunda simge yok · NONE: hiçbir yerde | `computeAlert`, `statusBar` | `uyarı kararı…`, `bölümler ve durum çubuğu…` | Doğrulandı (kanal belgesi) |
| 5 | `setOnlyAlertOnce`: güncellemede tekrar uyarmaz | `computeAlert` | `güncelleme yeniden uyarır; onlyAlertOnce ile uyarmaz` | Doğrulandı ([Android başvurusu](https://developer.android.google.cn/reference/kotlin/android/app/Notification.Builder)) |
| 6 | Bölümler: **Konuşmalar**, uyaran, sessiz (Android 11+) | `sectionOf`, `Shade.shade()` | `bölümler` | Doğrulandı ([9to5Google](https://9to5google.com/?p=339996)); sessiz konuşmanın sessiz bölümde kalması yaklaşıktır |
| 7 | **Süregelen (ongoing)** bildirim kaydırılamaz, ertelenemez, "Tümünü temizle" ona dokunmaz | `dismiss`/`snooze` → 409, `clearAll` atlar | `süregelen bildirim kaydırılamaz…`, e2e | Genel Android bilgisi, **bu oturumda kaynakla doğrulanmadı** |
| 8 | Bölüm bazlı "Temizle" ve genel "Tümünü temizle" | `clearAll({section})` | `bölüm bazlı tümünü temizle`, e2e | Genel bilgi, doğrulanmadı |
| 9 | Dokununca `contentIntent`; `autoCancel` ise bildirim kalkar | `click` | `dokunma: autoCancel…` | `setAutoCancel` anlamı genel bilgi; "süregelen + autoCancel kalır" kısmı **doğrulanmadı** |
| 10 | Gruplama: `setGroup` + özet; tek çocuklu grup özet göstermez | `#entries`, `#groupEntry` | `açık grup…` | Gruplama ve özet kuralı doğrulandı ([grup belgesi](https://developer.android.com/training/notify-user/group)); "tek çocuk" ayrıntısı genel bilgi |
| 11 | **Otomatik gruplama**: gruplanmamış bildirimler eşiğe ulaşınca uygulama başına gruplanır | `AUTOGROUP_AT_COUNT = 4` | `otomatik gruplama…` | Eşik değeri (4) **doğrulanmadı**; sabit olarak değiştirilebilir |
| 12 | Grubu kaydırmak kapatılabilir çocukları kaldırır; çocuksuz özet sistem tarafından silinir | `dismissGroup`, `#pruneSummaries` | `grubu kaydırmak…` | Genel bilgi, doğrulanmadı |
| 13 | **Erteleme**: çubuktan kalkar, süre dolunca yeniden uyararak döner; erteleme sırasındaki güncelleme gizli kalır | `snooze`, `tick` | `erteleme: gizlenir…` | Android'de 15 dk/30 dk/1 sa/2 sa seçenekleri genel bilgi; "güncelleme gizli kalır" AOSP `SnoozeHelper` anımsamasıdır, **doğrulanmadı** |
| 14 | `setTimeoutAfter`: süre dolunca bildirim kendiliğinden kalkar | `tick` (REASON.TIMEOUT) | `zaman aşımı` | Genel bilgi, doğrulanmadı |
| 15 | `deleteIntent`: yalnızca **kullanıcı** kapatınca uygulamaya haber gider; `cancel` ile gitmez | `dismissed` olayı (SSE `?pkg=`) | `kaydırarak kapatma deleteIntent…`, `cancel…`, API SSE testi | Genel bilgi, doğrulanmadı |
| 16 | Eylem düğmeleri ve satır içi yanıt (`RemoteInput`) uygulamaya geri döner | `action` + `action` olayı | `eylem: yanıt eylemi metin ister`, e2e | Genel bilgi |
| 17 | **Rahatsız Etme**: ses/heads-up bastırılır, bildirim yine çubuğa düşer; alarm/çağrı kategorisi geçebilir | `computeAlert`, `setDnd` | `Rahatsız Etme…`, e2e | Yaklaşıktır: gerçek DND'nin zamanlama, "yalnızca öncelikli" ve kişi listeleri yok |
| 18 | Uygulamayı/kanalı kullanıcı kapatınca mevcut bildirimleri de kalkar | `setAppBlocked`, `setChannelImportance(0)` | `NONE kanal ve engelli uygulama…` | Genel bilgi |
| 19 | Bildirim geçmişi (Android 11+) | `history` + arayüz paneli | API ve e2e geçmiş testleri | Varlığı doğrulandı ([Android Kaynak](https://source.android.com/docs/core/display/notification-history)); burada Notivo'nun sınırsız/aranabilir geçmişi uygulanmıştır |
| 20 | Uygulama başına rozet sayısı | `badges` | `bölümler ve durum çubuğu…` | Genel bilgi |
| 21 | Uygulama başına canlı bildirim sınırı (aşılırsa gönderim reddedilir) | `MAX_LIVE_PER_APP = 50` → 429 | `uygulama başına canlı bildirim sınırı…` | Sınır olgusu genel bilgi; **değer 50 doğrulanmadı** |

## Notivo'dan taşınanlar (Android uygulamasının `Logic.kt`/`NotiListener.kt` mantığı)

| Özellik | Burada |
|---|---|
| ENGELLE / SESSİZ kuralları (kelime + uygulama) | `rules`, `matchRule` — ENGELLE hiç göstermez; SESSİZ yalnızca geçmişe yazar |
| Kopya engeli (aynı içerik, 10 sn) | **Farklı anahtarla** gelen kopya atlanır; var olan bildirimin güncellemesi hiçbir zaman kopya sayılmaz |
| "Bu mesaj silindi" tespiti | Yer tutucu saklanmaz, **en yeni** aynı-konuşma mesajı işaretlenir; çubukta özgün metin + uyarı etiketi kalır |
| Türkçe İ/I/ı duyarlı arama ve kural eşleme | `norm()` |
| CSV dışa aktarma, JSON yedek/geri yükleme | CSV hücreleri formül enjeksiyonuna karşı korunur |
| Saklama süresi | `retentionDays` (varsayılan 30), saatte bir temizlik |

**Bilinçli sapma:** Android bir konuşmayı tek bildirimde yerinde günceller; Notivo ise her mesajı saklar. Bu yüzden metni değişen bir güncellemede eski hal geçmişe
`REPLACED` kaydı olarak yazılır (süregelen/ilerleme bildirimleri hariç: onlar geçmişi doldurmasın).

## Taşınmayanlar / yapılmayanlar (dürüst liste)

- Notivo Android uygulamasının **yıldız**, okundu bayrağı, biyometrik kilit, günlük özet, istatistik ekranı. İstenmedi, eklenmedi.
- Android'in kendisinde olup burada olmayanlar: kilit ekranı görünürlüğü, baloncuklar, kanal grupları, ses/titreşim desenleri, kişi/öncelik sıralaması,
  DND zamanlaması.
- Bu sistem, **başka uygulamaların bildirimlerini yakalayan** bir dinleyici değildir (Android'deki `NotificationListenerService` rolü yok): bildirimleri uygulamalar
  HTTP API ile **gönderir**. Android uygulamasını bu sunucuya bağlamak istenirse ayrı bir iş olur.
