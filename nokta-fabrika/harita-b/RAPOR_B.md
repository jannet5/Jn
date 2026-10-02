# RAPOR_B — nokta, Harita B (Views + Room + fizikli balon)

Durum: `./gradlew testDebugUnitTest assembleRelease bundleRelease` BAŞARILI (temiz makinede 1 dk 9 sn). Cihazda/emülatörde ÇALIŞTIRILMADI.

## Çıktılar (dist/)
- `nokta-b.apk` 849.610 bayt (830 KB), release, minify + shrinkResources açık, apksigner: **v2 doğrulandı** (v1/v3 yok; minSdk 26 için v2 yeterli).
- `nokta-b.aab` 1.381.435 bayt (1,32 MB), jarsigner "jar verified".
- `SHA256SUMS.txt`. Paket `app.nokta.b`, minSdk 26, **targetSdk 34 / compileSdk 34**.
- Sapma: NOTLAR/play_store 36 öneriyor. Makinede yalnız platform 34 + build-tools 34 vardı; AGP 8.5.2 ile 36 hedeflenemez ve 3 build aynı SDK'yı paylaşıyor (eşzamanlı SDK kurulumu riski). Play'e gitmeden AGP 8.9+ / platform 36'ya yükseltilmeli.

## Testler
25 birim testi, 0 hata: ListModelTest 16 (ekleme/kırpma, id benzersizliği, toggle, silme+undo konumu, tek yuvalı undo, toplu temizle+undo sırası, taşıma, geçersiz indeks, dışa aktar/içe aktar gidiş-dönüş, uzun metin), StorageTest 4 (kaydet/yükle sırası+durum, yükleme sonrası id çakışması, bozuk sıra onarımı, silme/undo kayıt anlık görüntüsü), BubbleMathTest 5 (kenar, clamp, hedef üstü).
Sınırlar: StorageTest `ItemStore` sahtesini sınar; **gerçek Room (SQLite) sorgusu JVM'de test edilmedi** (Robolectric/instrumented yok). Repo (Handler) ve tüm görünümler/animasyonlar test edilmedi.
Bir gerçek hata testle yakalandı: içe aktarmada tek başına `[x]` satırı öğe oluyordu; regex ile düzeltildi.

## Mimari
- `core/ListModel` saf Kotlin: liste, undo (tek yuvalı: Silindi / ClearedDone), taşıma, dışa/içe aktarma. `core/Repo` tek kaynak; ana iş parçacığında değişir, her değişiklikte değişmez anlık görüntü tek iş parçacıklı executor ile Room'a yazılır (`replaceAll` tek transaction; liste yüzlerce satır olduğundan yeterli). Hem Activity hem balon paneli aynı `ListController`'ı kullanır (davranış ayrışmaz).
- Room 2.6.1 + KSP 1.9.24-1.0.20 ilk denemede çalıştı; kapt/SQLiteOpenHelper yedeğine gerek kalmadı.
- Liste: RecyclerView + özel DiffUtil (içerik payload'ı ile yalnız tamamlanma animasyonu) + ItemTouchHelper (tutamaç `startDrag` + uzun basma). Sürükleme sırasında yalnız adaptör yerelde `notifyItemMoved`, model bırakınca bir kez güncellenir; sürüklerken gelen güncellemeler bekletilir.
- Davranış: satıra dokun = üstü çizilir (çizgi soldan sağa 200 ms, daire dolar 200 ms), sağdaki × = sil (satır sağında), silme/toplu temizlemede 5 sn "Geri al" şeridi, yeni öğe en üste girer, açılışta imleç hazır, Alınanları temizle, yedek paylaş (düz metin `[ ]/[x]`), panodan ekle.
- Balon: `BubbleService` (foreground, `specialUse` + property + izinler), WindowManager TYPE_APPLICATION_OVERLAY. Sağ kenara yapışma `SpringAnimation` (STIFFNESS_MEDIUM, damping 0.6, hafif sekme); Y'de `FlingAnimation` (bırakma hızı, sınırlı); iki eksende sürükleme; sürüklerken altta kaldırma hedefi, üstüne bırakınca balon geçici gizlenir ve bildirimde "Balonu göster" eylemi çıkar; balonda kalan sayı. Dokununca overlay panel (aynı liste, zemin karartma, Geri/dış dokunuş kapatır). Konum (Y) SharedPreferences'ta; döndürmede yeniden yapışır. Yerleşim hesapları `BubbleMath`'te saf, testli.
- Ana ekran izin yoksa "Balon için izin gerek" şeridi; Android 13+ bildirim izni bir kez istenir.

## Tasarım kararları ve gerekçe
- Ad: **nokta** (varsayılan korundu): ad, logo ve balonun tek bir şekli — dolu bir nokta.
- Logo: koyu mürekkep zemin üstünde vermilyon nokta (adaptif + monokrom ikon). Gradyan, gölge/kart yok.
- Renk: kâğıt zemin `#F4F1EA` / mürekkep `#1C1A17`, TEK vurgu vermilyon `#D9461E` (koyu temada `#F0663D`). Vurgu yalnız anlam taşır: balon, "alındı" dairesi, geri-al eylemi (Things'in "renk = anlam" ilkesi, tasarim_referanslari.md). Mor/indigo kaçınıldı (ai_slop_onleme.md). Koyu tema `values-night`, sistem temasına uyar.
- Tipografi: **DM Serif Display** (yalnız "nokta" başlığı ve boş durum cümlesi) + **IBM Plex Sans** Regular/Medium (gövde). Tek bilinçli eş; Inter/Roboto değil. Türkçe karakterler için Latin alt kümesine indirgendi (pyftsubset; üç font toplam ~175 KB).
- Ölçüler: 4/8 grid, satır min 56 dp, yatay 16 dp, daire 22 dp, dokunma hedefleri 48 dp, ayraç 1 dp, düz satır (kart/gölge yok). Süreler 150/200 ms. Sürüklenen satır 1.02x + yükselti + dokunsal geri bildirim (Things gözlemi).
- Boş durum: illüstrasyon/emoji yok, tek cümle.

## Kullanılan kaynaklar (gerçekten okunanlar)
- ORTAK_SPEC.md, HARITALAR.md; NOTLAR: ai_slop_onleme.md, tasarim_referanslari.md (Things sayıları 3. taraf, doğrulanmamış), yorum_analizi.md, play_store_gereksinimleri.md, araclar.md (yalnız bilgi).
- `NOTLAR/sistemler.md` ve `kaynaklar.md` bulunamadı (hiç oluşmadı); bu yüzden GitHub'dan doğrudan okudum: **tasks/tasks** `app/src/main/java/org/tasks/tasklist/DragAndDropRecyclerAdapter.kt` (ItemTouchHelper.Callback + DiffUtil birlikte; sürükleme sırasında güncellemeleri kuyruğa alma, durum değişimini `onSelectedChanged`/`clearView`'de işleme fikri) ve **recruit-lifestyle/FloatingView** `FloatingView.java`, `TrashView.java` (dokunma eşiği, basılı ölçek, VelocityTracker, kenara OvershootInterpolator ile yapışma, kaldırma hedefi akışı). Kod kopyalanmadı; fikirler kendi kodumda yeniden yazıldı (Tasks.org Compose değil RecyclerView kullanan eski satır yolundan esinlendim).
- Fontlar: IBM/plex (OFL) `IBMPlexSans-Regular/Medium.ttf`, google/fonts (OFL) `DMSerifDisplay-Regular.ttf`.
- Todoist/Bring doğrudan incelenmedi; yalnız NOTLAR özetleri dolaylı etkili.

## Sorunlar ve çözümler
- GitHub API ağaç listesi erişimi yok; `git clone --depth 1` çalıştı.
- google/fonts'ta IBM Plex Sans yolu 404; IBM/plex deposundan alındı.
- İçe aktarma `[x]` hatası (yukarıda). Maven 429 yaşanmadı. Room/KSP sorunsuz.
- Derleme uyarısı: `SOFT_INPUT_ADJUST_RESIZE` deprecated (API 30+ için pencere inset'iyle değişmeli; işlev bozmuyor).

## TEST EDİLMEYENLER (ana oturum cihazda doğrulamalı; uçtan uca doğrulandığı İDDİA EDİLMİYOR)
1. Overlay izin akışı, bildirim izni, FGS `specialUse` başlatma (Android 14+ davranışı).
2. Balonun gerçek sürüklenmesi, yay hissi, Y atılması, kaldırma hedefi eşiği (56 dp yarıçap tahmin), geçici gizleme + bildirimden geri getirme.
3. Overlay panelinde klavye: pencere focusable + ADJUST_RESIZE; panelde klavye/odak, Geri tuşu, panel yüksekliği (ekranın %52'si) gerçek cihazda ayarlanmalı.
4. ItemTouchHelper sürükleme akıcılığı, tutamaç + uzun basma; undo şeridi zamanlaması.
5. Gerçek Room veritabanı (R8 sonrası dahil), süreç öldürülünce veri korunumu, minify'lı APK'nın açılışı.
6. Koyu tema görünümü, ekran döndürme, farklı yoğunluk; ikon görünümü; TalkBack.
7. Play Console beyanları (FGS videosu, izin gerekçesi, veri güvenliği) yapılmadı.
