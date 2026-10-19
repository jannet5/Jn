# Harita karşılaştırması (A / B / C) — ölçülen sonuçlar

Test ortamı: KVM'siz yazılım emülatörü, Android 8.0 (API 26, x86_64), adb ile dokunuş enjeksiyonu. Başlangıç süreleri tek örnek ve emülatörde göreli; gerçek telefon değil.

| | A (Compose) | B (Views + Room) | C (sıfır bağımlılık) |
|---|---|---|---|
| APK (release, minify) | 1.264.971 B (1,21 MiB) | 849.610 B (830 KB) | 34.404 B (33,6 KB) |
| AAB | 3,04 MiB | 1,32 MiB | 53 KB |
| Birim test | 35 (4'ü Roborazzi ekran görüntüsü) | 25 | 27 |
| Soğuk açılış (`am start -W`, emülatör) | 8481 ms | 8449 ms | 5558 ms |
| Ekleme | çalıştı | çalıştı; ilk eklemeden sonra odak kayboldu (her seferinde alana dokunmak gerekti) | çalıştı; odak korundu, art arda hızlı ekleme |
| Üstünü çizme | çalıştı (turuncu dolu işaret) | çalıştı | çalıştı |
| Silme (×) | çalıştı | çalıştı | çalıştı |
| Sürükle-sırala | tutamaçla çalıştı | tutamaçla çalıştı | tutamaçla çalıştı |
| Kalıcılık (kapat-aç) | doğrulanmadı (çökme yüzünden akış kesildi) | çalıştı | çalıştı |
| Balon | **ÇÖKTÜ**: `getCurrentWindowMetrics()` API 30+, minSdk 26 | çalıştı; dokununca ana ekranın üstünde **yüzen liste paneli** açıldı | çalıştı; dokununca uygulama açıldı |
| Geri al şeridi | ekran görüntüsünde yakalanamadı (süre doldu) | test edilmedi | test edilmedi |
| Görsel kalite (ekran görüntüsü) | en iyi | iyi, tipografi güçlü | sade, temiz |

## Çıkarımlar
- **Birim testleri bunu yakalayamadı:** A'nın 35 testi geçti ama balon API 26'da çöküyor. Cihaz testi şart.
- **Balon:** B'nin panel yaklaşımı TXT'deki "balondan direkt listeye" isteğine en yakın.
- **Hız/boyut:** C ölçülebilir biçimde en küçük ve en hızlı (tek örnek).
- **Hızlı ekleme:** C'nin odak davranışı doğru, B'nin yanlış.
- **Görsel dil:** A'nın daire onay kutusu + dolu işaret + tutamaç + × düzeni en rafine.

## Harita D (kümülatif) için alınanlar
B tabanı (panel balon, yay animasyonu, Room) + C'nin odak davranışı, erişilebilirlik ve toplu temizle/paylaş özellikleri + A'nın hedef API 36 araç zinciri ve "API 30+ çağrılarını koru" dersi. Lint ile NewApi taraması.

## Harita D — cihaz sonuçları (teslim edilen APK, SHA-256 c943c79a…1c6336)
API 26 emülatörü: soğuk açılış 6198 ms (A/B ~8,5 sn, C ~5,6 sn; tek örnek, göreli). Art arda ekleme odak kaybetmeden çalıştı (B'nin hatası giderildi). Çizme, silme, tutamaçla sürükleme, kalıcılık, balon (ana ekranda rozet; hepsi tamamsa nokta), balona dokununca ana ekranın üstünde liste paneli çalıştı. Çökme yok.
Geri al şeridi: 6 sn pencerede emülatörün yavaşlığı yüzünden dokunuşlar zamanında ulaşmadı; geçici 30 sn'lik bir derlemede silinen madde geri geldi (kod doğru, 6 sn değeri gerçek cihazda denenmeli).
API 35 emülatörü: kurulum, açılış, art arda ekleme, ön plan servisi (specialUse) çalıştı, çökme yok. Bu emülatörde sistem çubuğu inset'i sıfır bildirildi ve ekran görüntüleri bozuk/siyah geldi: kenardan kenara düzeni ve panelin Android 15 görünümü doğrulanamadı.
Lint: 0 hata, 24 uyarı. Birim test: 32/32.

## 1.1.0 — zil ile hatırlatma (cihaz sonuçları, Android 8.0 emülatörü)
Teslim APK SHA-256 1d1b79ad…3d67840. Birim test 40/40 (ReminderPlan 8 yeni), lint 0 hata.
- Her satırda zil; dokununca "Tekrarla · N dakikada bir" ve "Alarm · saat" penceresi açıldı. Dakika yazınca Tekrarla kendiliğinden açıldı.
- 1 dakikalık tekrar: sistemde tam zamanlı alarm kuruldu, bildirim "Eggs — Hatırlatma · her 1 dakikada bir" ALDIM/DURDUR ile geldi, sonraki alarm kendiliğinden kuruldu. Önem düzeyi 4 (yüksek; gerçek telefonda ekran üstü açılır pencere). Emülatör ekran görüntüsünde açılır pencere yakalanamadı.
- DURDUR: tekrar ve saatli alarm silindi, bildirim kalktı. ALDIM: madde üstü çizildi, hatırlatma durdu.
- Saatli alarm 21:00'de "Eggs — Alarm" bildirimi verdi, tek seferlik kaydı silindi.
- APK yeniden kurulunca (MY_PACKAGE_REPLACED) alarmlar yeniden kuruldu. Telefon açılışı (BOOT_COMPLETED) emülatörde denenmedi.
- Testte bulunan ve düzeltilen iki hata: uygulama açıkken balon diyalog düğmelerini örtüyordu (artık uygulama ön plandayken gizleniyor); bildirimden "Durdur"/"Aldım" sonrası uygulamaya dönünce zil simgesi eski kalıyordu (geri dönüşte yenileniyor).

## 1.1.1 — düzeltme
Mağaza ekran görüntüsü çekerken bulundu: bir satıra basılı tutup hareket ettirmeden bırakınca satır büyütülmüş/gölgeli kalıyordu (bırakma animasyonu liste güncellemesiyle kesiliyordu) ve sürükleme sırasında gelen dokunuş maddenin üstünü çiziyordu. Düzeltildi: bırakınca satır animasyonsuz yerine oturuyor, satır yeniden çizilirken boyutu sıfırlanıyor, sürükleme sürerken dokunuş yok sayılıyor. Emülatörde 1,5 sn basılı tut-bırak ile doğrulandı. APK SHA-256 3be7904f…e418e28c1.

## 1.2.0 (kullanici geri bildirimi, gercek telefon)
- Alarm izni uyarisi artik tiklanacak yazi degil, "Izni aç" dugmesi; ayarlardan donunce uyari kendiliginden kalkar.
- Giris alani cok satirli: Enter yeni satir/paragraf acar, ekleme sagdaki "Ekle" dugmesiyle. Satir sonlari korunur, art arda bos satirlar tek paragraf boslugu olur; azami metin 1000 karakter.
- Duzenleme: maddeye basili tut, kipirdatmadan birak -> metin giris alanina gelir, "Duzenleniyor · Vazgeç" seridi, dugme "Kaydet". Tutamactan surukleme duzenleme acmaz. Ilk eklemede bir kez ipucu gosterilir.
- Yedek metni cok satirli ogeleri 4 bosluk iceride devam satiri olarak yazar/okur.
- Dogrulama: 43 birim testi + lint temiz; API 26 emulatorde paragrafli ekleme, bas-birak duzenleme, kaydetme ve dokununca ustunu cizme denendi. Izin dugmesi API 31+ gerektirdiginden emulatorde gorsel olarak denenmedi.

## 1.2.1
- Klavye acik ve giris alani odaktayken klavyenin hemen sag ustunde ikinci bir "Ekle" (duzenlemede "Kaydet") belirir; ustteki Ekle yerinde kalir. Geri al cubugu bu dugmenin ustune kayar. Klavye kapaninca gizlenir.
- Dogrulama: API 26 emulatorde gorundu, basinca ekledi, klavye kapaninca kayboldu. 43 test + lint temiz.

## 1.3.0 (bildirim / alarm / balon, gercek telefon geri bildirimi)
Android 15 emulatorunde (temiz kurulum) bulunan ve duzeltilenler:
- Tam zamanli alarm izni Android 14+ te varsayilan KAPALI: alarm ve tekrar dakikalarca gecikiyordu. Uyari artik hem tekrar hem alarm icin gorunur, "Izni ac" dugmesi var; kaydederken izin yoksa izin ekrani dogrudan acilir. Izin verilince alarm `exactAllowReason=permission`, pencere 0 ile kuruldu; 1 dakikalik tekrar bildirimi zamaninda geldi (importance=4).
- Bildirim izni bir kez kalici reddedilince dugme sessiz kaliyordu: artik uygulamanin bildirim ayarlari acilir. Ayarlardan kapatilan bildirim/kanal da algilanir (yalniz izin degil). Gerekce metni hatirlatmayi anlatir.
- Saatli alarm artik gercek alarm: ayri "Alarmlar" kanali, alarm sesi (USAGE_ALARM), dokunana kadar tekrar eder (INSISTENT, en fazla 10 dk), ekran kapali/kilitliyken tam ekran alarm sayfasi acilir (Aldim / Kapat). API 26 emulatorde ekran kapaliyken 23:45 alarmi ekrani acti, "Aldim" sesi kesti, maddeyi tamamladi.
- "Zorla durdur" / uretici temizleyicisi (Xiaomi vb.) tum alarmlari siliyordu ve geri kurulmuyordu (bildirim hic gelmiyordu). Alarm zamanlari kaydedilir; uygulama ya da balon her acildiginda ayni zamana yeniden kurulur. Dogrulama: zorla durdur -> 0 alarm, uygulama acilinca ayni saat (00:02:29) geri geldi.
- Balon uygulama acikken gizli oldugu icin "Balonu goster" hicbir sey yapmiyor gibi gorunuyordu: artik ana ekrana cikilir, balon hemen gorunur, kisa aciklama gosterilir. Alarm ekraninda balon gizlenir.
- Zile/silmeye biraz uzun basmak duzenleme moduna dusuruyordu: dugmelerde basili tutmak artik duzenleme acmaz.
- 43 birim testi gecti, lint 0 hata.
