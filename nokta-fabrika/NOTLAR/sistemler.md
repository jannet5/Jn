# Sistemler: mimari özetleri ve okunan dosyalar

Hepsi raw.githubusercontent.com üzerinden okundu. Kod kopyalanmadı. Lisanslar önemli: Tasks.org, Fossify, Notally, GroceryGenius GPL-3.0; Aisleron, KitchenOwl AGPL-3.0. Bunlardan kod alırsak bizim proje de aynı copyleft'e girer. Fikir ve davranış almak serbest, kod almak değil. Apache-2.0 olanlar (Reorderable, FloatingView, Floating-Bubble-View) bağımlılık olarak güvenle kullanılabilir (NOTICE/atıf gerekir).

## Tasks.org (GPL-3.0), https://github.com/tasks/tasks
Okunan: `README.md`, `LICENSE`, `settings.gradle.kts`, `gradle/libs.versions.toml` (grep), `app/src/main/java/org/tasks/tasklist/DragAndDropRecyclerAdapter.kt`, `TaskViewHolder.kt`. Dizin: tasklist/ altında `AdapterSectionExtensions`, `BannerAdapter`, `DiffCallback`, `DragAndDropRecyclerAdapter`, `HeaderViewHolder`, `TaskListRecyclerAdapter`, `TaskViewHolder`, `ViewHolderFactory`.
- Çok modüllü (app, data, kmp, wear, composeApp). Ana liste hâlâ RecyclerView; ComposeView sadece chip'lerde (TaskViewHolder).
- Sürükleme: `ItemTouchHelper(Callback)`; `getMovementFlags` yalnız izin verilen durumda sürükleme; `onMove` içinde `notifyItemMoved` (anlık görsel), asıl kalıcı değişiklik `clearView` (bırakma) anında `adapter.moved(...)`. Sürükleme sırasında DiffUtil sonuçları `Queue`'ya alınıyor, bırakınca `drainQueue()` (liste titremesin diye); diff tek iş parçacıklı bir dispatcher'da hesaplanıyor.
- Üstü çizme: `nameView.paintFlags or STRIKE_THRU_TEXT_FLAG` + `colorOnSurface` %38 alfa.
- Zayıf: karmaşık durum (iç içe alt görevler, girinti), kendi yorumu "too much state change happens here" ve `runBlocking` kullanımı. Bizim için fazla ağır.

## Fossify Notes (GPL-3.0), https://github.com/FossifyOrg/Notes
Okunan: `README.md`, `LICENSE`, `app/src/main/kotlin/org/fossify/notes/adapters/TasksAdapter.kt`. Dizin: adapters/ (NotesPagerAdapter, OpenNoteAdapter, TasksAdapter, WidgetAdapter), helpers/, fragments/, views/ vb.
- `TasksAdapter : MyRecyclerViewListAdapter<NoteItem>` (commons kütüphanesi) + `ItemTouchHelperContract`; `DiffUtil.ItemCallback`, `setHasStableIds(true)`.
- Sürükleme sapı: handle `OnTouchListener`, `ACTION_DOWN`'da `touchHelper.startDrag(holder)`. Yani satır kaydırma ile çakışmıyor.
- Tamamlanan satırlar için ayrı view tipi ve bir "taşı: üste/alta" çoklu seçim menüsü. STRIKE_THRU + alpha sabiti.
- Zayıf: ortak "commons" kütüphaneye bağımlı, tek başına okuması zor.

## Aisleron (AGPL-3.0), https://github.com/aisleron/aisleron
Okunan: `app/src/main/kotlin/com/aisleron/ui/shoppinglist/ShoppingListItemRecyclerViewAdapter.kt`, `ShoppingListItemMoveCallbackListener.kt`, `ProductShoppingListItem.kt`. Dizin: ui/ altında shoppinglist, aisle, shop, product, widgets vb.
- Fragment + RecyclerView, `ListAdapter` + DiffUtil. Satır tipleri: Header, Product, Empty (Location/Aisle ayrı).
- Callback: sürükleme UP/DOWN, kaydırma LEFT/RIGHT yalnız ürün satırlarında. `onRowMoved` içinde `Collections.swap` ile kopya liste + `submitList`; `onRowClear` bırakınca üstündeki öğeden raf/aisle bilgisini bulup `onListPositionChanged` ile DB'ye yazıyor (tek yazma).
- Satırda CheckBox (`chkInStock`), adet +/- butonları, not. Üstü çizme bu dosyada yok (izleme modu ve "stokta" durumu kullanılıyor).
- Güçlü: temiz Callback/Listener ayrımı, bırakınca tek DB yazımı. Zayıf: grup/aisle mantığı bizim basit liste için fazla.

## GroceryGenius (GPL-3.0), https://github.com/DanielRendox/GroceryGenius
Okunan: `README.md`, `feature/grocerylist/GroceryListScreen.kt` (importlar), dizin listeleri. 
- Tek Activity, Compose, Material 3, feature klasörleri (grocerylist, addgrocery, ...), baselineProfile modülü, Room benzeri `database` ve `sync/work`.
- README: sürükle-bırak gereken ekranlarda "composable'ları RecyclerView öğesi yapıp RecyclerView'u Compose içine gömme" yöntemi. Bu ekran dosyasında Reorderable/ItemTouchHelper importu yok (alışveriş ekranı LazyGrid, gruplu, sürükleme yok). Not: README'deki ifade Calvin-LL kütüphanesi çıkmadan önceki bir tercih olabilir (tahmin).

## Notally (GPL-3.0, özet), https://github.com/OmGodse/Notally
Okunan: `recyclerview/DragCallback.kt`; `MakeListAdapter.kt` yalnız grep (ItemTouchHelper var). Dizin: recyclerview/{adapter,viewholder}, DragCallback, ListItemListener.
- En küçük örnek: `isLongPressDragEnabled=false`, sadece UP/DOWN, `onMove`'da swap + `notifyItemMoved`, `onChildDraw` ile translation ve elevation, `clearView`'da sıfırlama. Sap ile başlatma adaptörde.

## Calvin-LL/Reorderable (Apache-2.0), https://github.com/Calvin-LL/Reorderable
Okunan: `README.md`, `LICENSE`, `reorderable/src/commonMain/kotlin/sh/calvin/reorderable/ReorderableLazyList.kt`, `ReorderableLazyCollection.kt` (grep + ilgili bölümler).
- Durum: `ReorderableLazyListState` (LazyListState sarmalayıcı). Sürüklenen öğe `zIndex(1f)` + `graphicsLayer{translationY=draggingItemOffset}` (komposizyon yerine çizim aşamasında okunan offset), bırakıldıktan sonra `previousDraggingItemOffset` animasyonu, diğerleri `animateItem()`.
- Kenar otomatik kaydırma: `scrollThreshold`, `Scroller`, `ScrollMoveMode.SWAP`; mesafeye göre hız (1..10 haritası).
- API: `onMove` geri çağırısı yalnızca sıra değişince; haptic için `performHapticFeedback` örnekleri README'de.

## Overlay kütüphaneleri
### recruit-lifestyle/FloatingView (Apache-2.0), arşivlendi
Okunan: `README.md`, `LICENSE`, `library/src/main/java/jp/co/recruit_lifestyle/android/floatingview/FloatingView.java` (1795 satır, grep), `FloatingViewManager.java` (indirildi, ayrıntılı okunmadı), `TrashView.java` (grep).
- Her yüzen öğe kendi WindowManager penceresi (FrameLayout). Bayraklar: NOT_FOCUSABLE | LAYOUT_NO_LIMITS | NOT_TOUCH_MODAL | HARDWARE_ACCELERATED; tip: API<=25 TYPE_PRIORITY_PHONE, aksi TYPE_APPLICATION_OVERLAY.
- Hareket: `VelocityTracker` ile bırakma hızı, kenara yapışma `SpringAnimation` (X için sönüm 0.7, sertlik 350, Y için ayrı spring) ve eski `ValueAnimator` + `OvershootInterpolator`; her güncelleme `updateViewLayout`. Çöp hedefi `TrashView` ayrı pencere, kapsayınca ölçek animasyonları (200 ms).
- Zayıf: arşivde, hedef SDK 28, Java; hâlâ pattern olarak değerli.

### torrydo/Floating-Bubble-View (Apache-2.0)
Okunan: `FloatingBubbleView/src/main/java/com/torrydo/floatingbubbleview/{AnimHelper,CloseBubbleBehavior,AndroidVersions,ComposeLifecycleOwner,MyBubbleLayout}.kt`, `bubble/FloatingBubble.kt`, `service/FloatingBubbleService.kt`, `helper/ViewHelper.kt` (grep). `service/ExpandableBubbleService.kt` yolu 404.
- `FloatingBubbleService` (abstract Service): overlay izni yoksa SecurityException, `startForeground` + bildirim kanalı.
- `AnimHelper`: `SpringAnimation(FloatValueHolder())` + `SpringForce` (STIFFNESS_LOW/MEDIUM, DAMPING_RATIO_LOW/MEDIUM_BOUNCY), `startSpringX`, `animateSpringPath`.
- Compose desteği: `ComposeLifecycleOwner` (SavedStateRegistryOwner + ViewModelStoreOwner) decor view'a `setViewTreeLifecycleOwner` vb. ile bağlanıyor. Servis penceresinde ComposeView'in çalışması için bu şart.
- Genişleyen görünüm (expanded) ayrı pencere; kapama balonu iki davranış (sabit/dinamik) (README özeti).

## Okunamayan / yüzeysel kalanlar
OpenTasks, KitchenOwl, Markor, Coding-Meet, wbpxre150 ShoppingList: kaynak koduna inilmedi. dofire/nicomazz/PsyGik/geecko86/HabaCo FloatingView forkları: sadece isim.
