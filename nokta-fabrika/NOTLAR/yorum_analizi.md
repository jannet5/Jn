# Yorum analizi (2026-10-02)

## Yöntem ve örneklem (gerçek)
- Araç: `google-play-scraper` 1.2.7, ağ engeli YOK, çekim başarılı. Ham veri: `yorumlar/*.csv` (sütunlar: score, date, text).
- Her uygulama için en yeni 2 yıldız (en fazla 300) + 3 yıldız (en fazla 300) yorum; dil en/US, ayrıca tr/TR.
- Toplam 5940 yorum satırı. Uygulama başına: Google Tasks 600 en / 165 tr; Todoist 600 / 47; Microsoft To Do 600 / 146; Bring! 600 / 15; Tasks.org 242 / 3; Out of Milk 600 / 0; Google Keep 600 / en (tr çekimi hata verdi); AnyList 281 en; Listonic 600; OurGroceries 600; Floating Notes (jsvmsoft) 225; Floaty Sticky Notes 11; Floating Notes (yuhapps) 5.
- Türkçe örneklem küçük (toplam 376, çoğu Google Tasks / MS To Do). Türkçe sonuçlar yönlendirici, istatistik değil.
- Sayılar = regex anahtar kelime eşleşmesi (bir yorum birden çok temaya girebilir, gürültü payı var). Duygu analizi değil; 2-3 yıldız yorumlarında geçen konu sıklığı.
- Hariç: yalnızca 2-3 yıldız (5 ve 1 yıldız çekilmedi).

## En sık 10 tema (5940 yorum içinde eşleşen yorum sayısı)
| # | Tema | Sayı | Alıntı (orijinal) |
|---|------|------|-------|
| 1 | Bildirim / hatırlatıcı sorunları | 613 | "bazen arada hatırlatmaları kaçırıyor" (ms_todo_tr, 3★) |
| 2 | Eksik özellik istekleri (genel) | 862 (geniş regex) | "Fnctions needed: search function; ... manually sort starred items; compact display option" (google_tasks_en, 3★) |
| 3 | Bug / çökme / çalışmıyor | 477 | "everything you add to a list gets removed instantly ... App is useless now." (bring_en, 2★) |
| 4 | Silme / swipe (kazara silme dahil) | 475 | "so easy to accidentally delete onscreen" (floating_notes_jsvmsoft, 3★) |
| 5 | Senkronizasyon | 420 | "too slow to sync to the cloud, so we end up missing things" (keep_en, 2★) |
| 6 | Widget | 406 | "Tamamlanan görevler widgetta üstü çizili olarak kalsa daha iyi olurdu" (todoist_tr, 3★) |
| 7 | Abonelik / ücret duvarı | 468 | "Alarm eklemek için bile ücretli sürüme yönlendiren bir uygulama benim için yok hükmündedir." (todoist_tr, 3★) |
| 8 | Paylaşım | 317 | liste paylaşımı hem istek hem sorun kaynağı |
| 9 | Reklam | 316 | "used to be a very good shopping app. however, it now has a lot of useless features (e.g. offers, recipes, ads...)" (bring_en, 2★) |
| 10 | Kategori / mağaza düzeni | 293 | "I would love to be able to set a different category sorting for each list (multiple stores)." (listonic_en, 3★) |

Sonraki: sade UI/övgü 284, sıralama/sürükle 208, hesap/giriş 236, yavaş 117, "tamamlananı görme/üstü çizili" 79, alt görev 69, karanlık tema 25.

## Talepler
- Elle sıralama (Keep: "ridiculous that we can't sort / alphabetize our lists", keep_en 2★; Google Tasks: "manually sort starred items").
- Tamamlanmış ama silinmemiş üçüncü durum: "an item is on the list AND it has already been collected" (bring_en, 2★); "why is there not a choice to restore one item, multiple items, or all?" (ourgroceries_en, 2★).
- Widget (Bring! TR: "widget olsa tadından yenmez", 3★); hızlı not için "a widget, tapping on it would open a window, with the cursor already blinking" (floating_notes_jsvmsoft, 2★).
- Liste başına farklı kategori sırası, çoklu kalem düzenleme.
- Rengin sistem temasına uyması (floating_notes_jsvmsoft, 3★), belirli uygulamalarda balonu gizleme.

## Şikayetler
- Kazara silme, ücret duvarı ("you pay for it but it's only for removing the adds", floating_notes_jsvmsoft, 2★), reklam ve tam ekran pop-up ("full screen pop ups every time I add something to a list", listonic_en, 2★).
- Güncelleme sonrası gerileme: Listonic kategori başlıklarını kaldırdı; Keep widget bir tık fazlası.
- Veri kaybı: "app crashes and DELETE you note. I've already lost about 10 notes" (floating_notes_yuh, 3★); Bring! TR "Yedekleme sistemi dahi olmayan" (2★).
- Overlay: "it shows on top of apps your using and you have to keep moving it out the way" (floating_notes_yuh, 2★).
- Şişme: Bring! teklifler, tarifler, reklamlar.

## Sevilenler (2-3★ yorumlarının içindeki iyi yanlar)
- "nice UI, no bloat" (bring_en); "The UI is great and the features are also top notch!" (tasks_org_en, 3★); büyük ikonlu görsel liste bir kısım için artı (bring_en: "Some people might prefer this").
- Çalışma sırasında hızlı, sade ekleme.

## Tasarım / UX
- "The looks so dull" (google_tasks_en, 2★): Google Tasks sade ama sönük bulunuyor.
- Kompakt görünüm ve daha çok satır isteği (google_tasks_en).
- Gizlenemeyen banner'lar kızdırıyor (google_tasks_en, 2★).

## Çalacağımız fikirler
1. Üstü çizili öğeyi silmeden bırak + tek dokunuşla geri al / toplu geri al.
2. Hızlı ekleme: açılınca imleç hazır (widget/balon dokunuşu).
3. Balon rengi temaya uysun; karanlık tema.
4. Sürükle-sırala + liste başına kategori sırası.
5. Yedekleme/dışa aktarma (veri kaybı korkusu).
6. Kazara silmeye karşı geri al (undo) şeridi.

## Farkımız
- Reklamsız, hesapsız, çevrimdışı; ücret duvarı yok (tek seferlik ücret açıkça "tam uygulama").
- Yuvarlak yüzen balon: diğer uygulamayı örtmeden küçülür/kenara yapışır, kolay geçici gizleme.
- Üç durumlu öğe: bekliyor / alındı (üstü çizili) / silindi (geri alınabilir).
- Türkçe odaklı: Türkçe örneklem az, Türkçe arayüz/mağaza metni fırsat olabilir (kanıt zayıf).
