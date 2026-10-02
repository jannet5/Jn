# Üç farklı harita (araç/yaklaşım kombinasyonu)

Her harita ayrı bir "sohbet" (bağımsız ajan) olarak, ayrı klasörde, diğerlerinden habersiz çalışır.

| | Harita A — "Compose + tasarım sistemi" | Harita B — "Views + Room + fizikli balon" | Harita C — "Minimal / sıfır bağımlılık" |
|---|---|---|---|
| UI | Jetpack Compose (Material3 token'ları özelleştirilmiş) | AppCompat + RecyclerView/ItemTouchHelper | Tek özel Canvas View, AndroidX UI yok |
| Sıralama | sh.calvin.reorderable (araştır, sürümü doğrula) | ItemTouchHelper + DiffUtil | Elle dokunuş + animasyon |
| Depolama | DataStore/JSON | Room (KSP/kapt; çalışmazsa SQLite helper) | Düz JSON dosyası |
| Balon | WindowManager overlay + Compose değil, klasik View | WindowManager + SpringAnimation, kenara yapışma, kaldırma hedefi | WindowManager, en basit |
| Tasarım kaynağı | Önce DESIGN.md yaz (tokenlar sayıyla), Things/Google Tasks ölçülerinden | Tasks.org/Todoist/Bring gözlemleri | Tek renk + tipografi odaklı, "ölçüsüz sade" |
| Araç odağı | Roborazzi/Paparazzi ekran görüntüsü testi dene | Açık kaynak kod incelemesi (NOTLAR/sistemler.md) | APK boyutu/başlangıç ölçümü odaklı |
| Hedef | Görsel kalite | Dayanıklılık + his | Hız + boyut |

Harita D: A, B, C raporlarındaki kazanan parçaların kümülatif birleşimi; ana oturum yapar.
Karşılaştırma ölçütleri: özellik tamlığı (kanıtlı), APK boyutu, başlangıç süresi (emülatörde göreli), test sayısı, kod karmaşıklığı, arayüz kalitesi (ekran görüntüsü), sorun sayısı.
