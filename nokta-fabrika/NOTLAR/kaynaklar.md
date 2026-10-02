# Kaynaklar (nokta araştırması)

Yöntem notu: GitHub API ve github.com HTML sayfaları bu ortamda erişilemedi (API 403). Dosya içerikleri `raw.githubusercontent.com` üzerinden, dizin listeleri WebFetch ile okundu. WebFetch çıktıları küçük bir model özeti olduğundan "(özet)" ile işaretli bilgiler birebir doğrulanmış değildir. Kod kopyalanmadı; yalnızca davranış/mimari notları alındı.

## A. Açık kaynak liste/görev/alışveriş uygulamaları

| # | Uygulama | Link | Lisans | Okunan |
|---|---|---|---|---|
| 1 | Tasks.org | https://github.com/tasks/tasks | GPL-3.0 (LICENSE okundu) | Evet, kaynak |
| 2 | Fossify Notes (checklist) | https://github.com/FossifyOrg/Notes | GPL-3.0 (LICENSE okundu) | Evet, kaynak |
| 3 | Aisleron | https://github.com/aisleron/aisleron | AGPL-3.0 (LICENSE başı okundu; README özeti: "App Store Exception") | Evet, kaynak |
| 4 | GroceryGenius | https://github.com/DanielRendox/GroceryGenius | GPL-3.0 (LICENSE başı okundu; ikonlar ticari kullanıma kapalı, README özeti) | README + 1 ekran dosyası |
| 5 | Notally | https://github.com/OmGodse/Notally | GPL-3.0 (özet; LICENSE dosyası yolunda 404) | Evet, DragCallback |
| 6 | OpenTasks | https://github.com/dmfs/opentasks | Apache-2.0 (LICENSE başı okundu) | Sadece README/dizin özeti |
| 7 | KitchenOwl | https://github.com/TomBursch/kitchenowl | AGPL-3.0 (LICENSE başı okundu) | Sadece README başı; Flutter (benim bilgim, bu oturumda doğrulanmadı) |
| 8 | Shopping List App (Coding-Meet) | https://github.com/Coding-Meet/ShoppingListApp | Lisans dosyası bulunamadı (404) | README: Compose + MVI + Room + Koin |
| 9 | Markor | https://github.com/gsantner/markor | LICENSE.txt var (telif başlığı okundu, tür doğrulanmadı) | Sadece README başı; kaynak okunmadı |
| 10 | ShoppingList (Adam Bullock, F-Droid) | https://f-droid.org/packages/com.wbpxre150.shoppinglist/ | MIT (WebSearch özeti) | Repo bulunamadı, sadece F-Droid özeti |

Bulunamayan/ulaşılamayan: Simple Mobile Tools/Fossify "Tasks" repo'su (raw 404), ByteHamster/ShoppingList, nvllsvm/Shopping-List, openintents/shoppinglist (raw 404, yolu bilmiyorum). Bring/OurGroceries kapalı kaynak, bakılmadı.

### Her uygulamadan öğrenilen
1. **Tasks.org**: Views tabanlı ana liste (RecyclerView + ItemTouchHelper), Compose yalnızca chip gibi parçalarda (ComposeView). Üstü çizme `Paint.STRIKE_THRU_TEXT_FLAG` + soluk renk. Sürükleme `DragAndDropRecyclerAdapter.kt` içinde; DiffUtil güncellemeleri sürükleme sırasında kuyruğa alınıyor (kasmamak için). Libs katalog Compose BOM 2026.08.00 gösteriyor (uygulama hibrit/geçişte).
2. **Fossify Notes**: Checklist `TasksAdapter.kt`; ListAdapter+DiffUtil, `setHasStableIds(true)`, ItemTouchHelper, sürükleme sapı (handle) `ACTION_DOWN`'da `startDrag` çağırıyor. Üstü çizme STRIKE_THRU + alpha. Tamamlananlar ayrı satır tipi ("CompletedTasks"), isteğe bağlı alta taşıma.
3. **Aisleron**: Fragment+RecyclerView, `ListAdapter`; ayrı `ShoppingListItemMoveCallbackListener` (ItemTouchHelper.Callback) ile sürükle (UP/DOWN) ve sola/sağa kaydır; `onRowMoved` içinde liste kopyası swap edilip `submitList`, bırakınca (`onRowClear`) tek seferde DB güncellemesi. Kaydırmayla "needed/in stock" izleme.
4. **GroceryGenius**: Tek Activity, tamamen Compose (LazyGrid). README'ye göre sürükle-bırak için Compose içine gömülü RecyclerView+ItemTouchHelper kullanılıyor (Dashboard/Settings); neden: "Compose'da resmi sürükle-bırak yok". Alışveriş listesi ekranı LazyGrid ve gruplu. Modüler feature klasörleri, baselineProfile modülü var.
5. **Notally**: Çok sade `DragCallback.kt`: uzun basma kapalı (`isLongPressDragEnabled=false`, sap ile sürükleme), `onMove`'da Collections.swap + notifyItemMoved, sürüklenen öğeye elevation, bırakınca sıfırlama. Küçük ve okunaklı örnek.
6. **OpenTasks**: Eski Java/Views, Content Provider tabanlı, CalDAV senkron. Sürükle-sırala hakkında bilgi bulunamadı. Bizim için düşük değer.
7. **KitchenOwl**: Alışveriş+tarif, sunucu gerektirir, Flutter (doğrulanmadı). Bizim yığına doğrudan uymaz.
8. **Coding-Meet ShoppingListApp**: Eğitim niteliğinde Compose örneği; sürükle yok, lisans belirsiz, kopyalanmaz.
9. **Markor**: Metin/ToDo düzenleyici, kaynak okunmadı.
10. **ShoppingList (F-Droid)**: Özellik listesi sürükle-sırala ve işaretleme içeriyor (özet); kaynak okunmadı.

## B. Sürükle-sırala kütüphaneleri

- **Calvin-LL/Reorderable (sh.calvin.reorderable)**: https://github.com/Calvin-LL/Reorderable, Apache-2.0 (LICENSE okundu). README + `ReorderableLazyList.kt` + `ReorderableLazyCollection.kt` okundu. Mimari: `rememberReorderableLazyListState(lazyListState){from,to->}` ve `ReorderableItem(key)`; sürüklenen öğe `graphicsLayer{translationY}` ile (layout dışı) kaydırılıyor, diğerleri `Modifier.animateItem()`. Kenara gelince otomatik kaydırma (scrollThreshold), `draggableHandle` / `longPressDraggableHandle`, haptic örnekleri, başlık/altlık desteği. Compose Multiplatform.
- **aclassen/ComposeReorderable**: https://github.com/aclassen/ComposeReorderable, Apache-2.0 (LICENSE başı okundu). Eski sürücü (`reorderable(state)` modifier); README'de arşiv/yerine geçen bilgi okumadım. Yeni projede Calvin-LL tercih edilir.
- **ItemTouchHelper (AndroidX RecyclerView)**: Tasks.org, Fossify, Aisleron, Notally'de gerçek kullanım okundu (bkz. sistemler.md).
- Performans kaynağı: https://developer.android.com/develop/ui/compose/performance/bestpractices (okundu, özet): kararlı `key`, `remember`ile pahalı hesap, `derivedStateOf`, okumaları ertele (lambda modifier, `drawBehind`), geriye yazma yapma. Baseline Profile ve R8 yönlendirmesi var.

## C. Yüzen balon / overlay

- **WindowManager TYPE_APPLICATION_OVERLAY** (API 26+, SYSTEM_ALERT_WINDOW izni): https://developer.android.com/reference/android/view/WindowManager.LayoutParams (WebFetch özeti; bazı ayrıntılar şüpheli, birebir doğrulanmadı). Pratikte FloatingView kaynağı API<=25 için TYPE_PRIORITY_PHONE, sonrası için TYPE_APPLICATION_OVERLAY kullanıyor (okundu).
- **Android 12 dokunma kısıtı**: https://developer.android.com/about/versions/12/behavior-changes-all (okundu). TYPE_APPLICATION_OVERLAY güvenilmez sayılıyor; üstündeki pencere birleşik opaklığı <=0.8 değilse dokunmalar alttaki uygulamaya geçmez/engellenir. Bizim balon küçük ve opak olsa da bu, tam ekran perdeleme yapmamamız gerektiği anlamına geliyor.
- **Bubbles API**: https://developer.android.com/develop/ui/views/notifications/bubbles (WebFetch özeti). Android 11+ için paylaşım kısayolu (long-lived shortcut) ve konuşma niteliği şart; genişleyen Activity `allowEmbedded` + `resizeableActivity` olmalı; kullanıcı baloncukları kapatabilir; doküman "devam eden iletişim" için öneriyor. Sürekli duran, kenara yapışan bir liste balonu için uygun değil.
- **Foreground service tipleri**: https://developer.android.com/develop/background-work/services/fgs/service-types ve https://developer.android.com/about/versions/14/changes/fgs-types-required (okundu). Android 14 hedefinde tip zorunlu; `specialUse` için `FOREGROUND_SERVICE_SPECIAL_USE` izni + `PROPERTY_SPECIAL_USE_FGS_SUBTYPE` `<property>`; açıklamayı Google Play inceliyor. Tip yoksa `MissingForegroundServiceTypeException`.
- **Arka plandan FGS başlatma**: https://developer.android.com/develop/background-work/services/fgs/restrictions-bg-start (okundu). Android 12+ `ForegroundServiceStartNotAllowedException`; SYSTEM_ALERT_WINDOW muafiyeti var; **Android 15 hedefinde** ek olarak görünür bir TYPE_APPLICATION_OVERLAY penceresi gerekiyor (https://developer.android.com/about/versions/15/behavior-changes-15, okundu). Önlem: önce overlay penceresini ekle ve görünür olmasını bekle, sonra FGS başlat; ya da servisi hep ön plandayken (Activity'den) başlat.
- **recruit-lifestyle/FloatingView**: https://github.com/recruit-lifestyle/FloatingView, Apache-2.0, arşivlendi (2020), README "Bubbles'a geçin" diyor. `FloatingView.java`, `TrashView.java` okundu: kenara yapışma, SpringAnimation, çöp hedefi, VelocityTracker.
- **torrydo/Floating-Bubble-View**: https://github.com/torrydo/Floating-Bubble-View, Apache-2.0 (LICENSE başı okundu). Kotlin, minSdk 21, XML ve Compose destekli (README özeti). `AnimHelper.kt`, `FloatingBubbleService.kt`, `ComposeLifecycleOwner.kt`, `AndroidVersions.kt` okundu. Servis `startForeground` çağırıyor, overlay izni yoksa SecurityException atıyor.
- Başka adaylar (sadece WebSearch sonucunda isim olarak gördüm, İÇERİĞİ OKUMADIM): dofire/Floating-Bubble-View, nicomazz/floatingview, PsyGik/FloatingView, geecko86/FloatingView, HabaCo/FloatingView.
- **SpringAnimation**: https://developer.android.com/develop/ui/views/animations/spring-animation (okundu, özet): `androidx.dynamicanimation`, `DAMPING_RATIO_*`, `STIFFNESS_*`, `setStartVelocity` ile bırakma hızını fizik animasyonuna veriyor.
- **Üretici pil kısıtları**: https://dontkillmyapp.com/ (okundu): Huawei, Xiaomi, OnePlus en sorunlu; Xiaomi'de https://dontkillmyapp.com/xiaomi (yalnızca arama özeti): "uygulamayı kilitle", "autostart" izni; MIUI'de overlay izni "Arka planda açılır pencere göster" adıyla geçiyor (arama özeti).
