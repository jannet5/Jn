# Fikirler: "bunu şundan, şunu bundan alırız"

Dayanak: kaynaklar.md ve sistemler.md'de gerçekten okunanlar. Tahmin olanlar "(tahmin)" ile işaretli.

## 1. Mimari seçim
- **Liste ekranı**: Compose (LazyColumn) + `sh.calvin.reorderable` (Apache-2.0). Gerekçe: sürüklenen öğe `graphicsLayer` ile çizim aşamasında kayıyor, diğerleri `animateItem()`; stabil `key` ile; kenar otomatik kaydırma hazır. Alternatif RecyclerView+ItemTouchHelper: Tasks/Fossify/Notally ile kanıtlanmış, ama overlay penceresinde XML şişirmek/DiffUtil yönetmek daha çok kod.
- Yedek plan (tahmin): Compose sürücüsü overlay penceresinde sorun çıkarırsa (dokunma/odak), RecyclerView+ItemTouchHelper ile "Notally tarzı" minimal DragCallback; tek dosya, küçük.
- **Satır modeli**: sabit `id` (Long) = Fossify'daki `setHasStableIds` ve Compose `key` mantığı.

## 2. Davranışları kimden alırız
- **Üstü çizme**: Tasks.org ve Fossify ikisinde de çizgi + soluk renk/alfa. Biz Compose'ta `TextDecoration.LineThrough` + alpha ~%38 (Tasks'taki 0x61 ≈ %38 değeri). Çizgiyi animasyonlu çizmek (genişlik) isteğe bağlı süs (tahmin).
- **Satıra dokun = çiz, sağda sil, sürükleme**: Fossify'ın "sap ile sürükle" fikri: çakışmayı önlemek için sürükleme sapı (Calvin-LL `draggableHandle`) veya uzun basma (`longPressDraggableHandle`). Tek dokunuş = çizme, uzun basma = sürükleme; ayrı sap daha güvenli ama yer kaplar. Önerim: sol/sağda küçük sap, ya da uzun basma + haptic.
- **Sil**: Aisleron'un LEFT/RIGHT kaydırma fikri yerine kullanıcı isteği "sağda sil düğmesi"; ek olarak Geri Al (snackbar) (tahmin, UX).
- **Tamamlananlar**: Fossify'daki gibi "çizilenleri alta taşı" seçeneği (ayar). Sıra değişince tek DB yazımı.
- **Kalıcı yazım**: Aisleron ve Tasks "bırakınca tek yazma" yapıyor; biz sürükleme boyunca yalnız bellekte listeyi değiştirip `onDragStopped`'ta Room'a sıralama yazarız. Sıra için kesirli/aralıklı `position` (tahmin) ya da toplu yeniden numaralama (küçük liste için yeterli).

## 3. Kasmama (performans) kontrol listesi
1. LazyColumn `key = { it.id }`, `contentType`.
2. Dayanıklı (stable/immutable) satır modeli; satır composable'ına yalnız gereken alanlar.
3. Sürükleme offset'i yalnız çizim aşamasında okunsun (Reorderable zaten yapıyor; kendi kodumuzda lambda modifier, `graphicsLayer {}`).
4. Sürükleme sırasında DB'den gelen Flow yeniden listeyi ezmesin: Tasks.org'un "dragging iken güncellemeleri kuyruğa al" fikri. Bizde: sürükleme boyunca `UiState` kaynağı yerel kopya.
5. Baseline Profile + R8 (GroceryGenius'ta baselineProfile modülü var, Compose bestpractices önerir).
6. Balonun pencere güncellemesi: `updateViewLayout` sadece x,y değişince, sade bir View (ImageView/küçük Canvas) ile; balon için Compose gerekmesin (tahmin, ComposeView overlay'de lifecycle sorunu çıkarıyor, bkz. torrydo `ComposeLifecycleOwner`).

## 4. Overlay (balon + panel) tasarımı
- **Teknik**: WindowManager + TYPE_APPLICATION_OVERLAY, bayraklar FloatingView'daki gibi (NOT_FOCUSABLE, NOT_TOUCH_MODAL, LAYOUT_NO_LIMITS). Bubbles API'sini kullanma: kısayol/konuşma şartı ve kullanıcı kapatabilir; FloatingView'ın kendi README'si de bu yüzden bırakılan bir yol.
- **İki pencere**: (a) küçük yuvarlak balon penceresi (hafif View), (b) açılan liste paneli ayrı pencere ya da aynı pencerenin büyümesi. Panel açılınca Compose (ComposeView + kendi LifecycleOwner/SavedStateRegistryOwner, torrydo'daki yaklaşım) kullanılır; balonda gerek yok. Klavye (yeni öğe ekleme) için panelde NOT_FOCUSABLE bayrağını kaldırıp `FLAG_ALT_FOCUSABLE_IM` yönetimi gerekebilir (tahmin, denenmedi).
- **Kenara yapışma**: FloatingView/torrydo ikisi de SpringAnimation: bırakma hızı VelocityTracker ile, X'te DAMPING_RATIO_LOW/MEDIUM_BOUNCY ve STIFFNESS_LOW~350. Bizde "sağ kenar" sabit: yalnız Y serbest, X sağa yapışsın (daha basit, tek spring).
- **Kaldırma hedefi**: FloatingView `TrashView`: ayrı pencere, balon yaklaşınca ölçek animasyonu ve yakalama. Bizde "balonu kapat" yeterli, istersen ayarlardan.
- **Servis**: ön plan servisi, Android 14+ için `foregroundServiceType="specialUse"` + `FOREGROUND_SERVICE_SPECIAL_USE` + `PROPERTY_SPECIAL_USE_FGS_SUBTYPE` (Play inceleme gerekçesi yaz). Android 15 hedefinde servisi arka plandan başlatacaksak önce görünür overlay penceresi şart (docs); güvenli yol: overlay penceresini ekle, görünürlüğünü bekle, sonra `startForeground`; veya kullanıcı ayarlar ekranındayken başlat.
- **İzin akışı**: `Settings.canDrawOverlays` + `ACTION_MANAGE_OVERLAY_PERMISSION` (bunu bu oturumda dokümandan okumadım, genel bilgi); Xiaomi'de izin adı "Arka planda açılır pencere göster", pil/autostart kilidi için kullanıcıya rehber ekranı (dontkillmyapp özeti).
- **Android 12+**: balon küçük ve opak; tam ekran yarı saydam perde yapma (0.8 opaklık sınırı).

## 5. Lisans stratejisi
- GPL/AGPL uygulamalardan (Tasks, Fossify, Aisleron, Notally, GroceryGenius) kod almıyoruz, yalnızca davranış fikri.
- Bağımlılık: Reorderable (Apache-2.0), androidx.dynamicanimation. FloatingView/torrydo'yu bağımlılık yapma: FloatingView arşivli ve eski SDK; torrydo'dan yalnız yaklaşımı öğren, kendi ~200 satırlık yöneticimizi yaz. Yazarsak atıf gerekmez; kopyalarsak Apache NOTICE gerekir.

## 6. Açık sorular (araştırılmadı)
- Kenara yapışan pencerede Compose paneli açılıp kapanırken geçiş maliyeti ve ilk açılış gecikmesi (ölçülmeli, ComposeView önceden ısıtılabilir, tahmin).
- Panelde klavye/odak davranışı cihazlarda (Samsung, Xiaomi) denenmeli.
- Calvin-LL kütüphanesinin overlay penceresindeki dokunma/gesture davranışı: kaynakta engel görmedim ama çalıştırıp denemedim.
