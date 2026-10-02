# RAPOR_D - nokta, Harita D (B tabanı + A/C dersleri)

Durum: `testDebugUnitTest`, `lint`, `assembleRelease`, `bundleRelease` BAŞARILI. Cihazda/emülatörde ÇALIŞTIRILMADI; uçtan uca doğrulama ana oturumda yapılacak.

## Çıktılar (dist/)
- nokta-d.apk 967.345 bayt (~945 KB), minify + shrinkResources açık, apksigner: v2 doğrulandı (v1/v3 yok).
- nokta-d.aab 1.645.298 bayt (~1,57 MB), jarsigner doğrulandı. SHA256SUMS.txt mevcut.
- Paket app.nokta.list, ad nokta, versionName 1.0.0 / versionCode 1, minSdk 26, target/compileSdk 36.
- Keystore harita-d/keystore/ altında (git'te yok, .gitignore'da); şifre yalnız keystore.properties içinde.

## Araç zinciri
AGP 8.13.2, Kotlin 2.3.21, Gradle 8.14.3, KSP 2.3.12, Room 2.8.5 (ilk denemede uyumlu; B'deki Room 2.6.1/KSP 1.9 terk edildi), core-ktx 1.17.0, appcompat 1.7.1, recyclerview 1.4.0. Maven 429 yalnız bir kez yaşandı (KSP/stdlib), bekleyen tekrar döngüsüyle geçti.

## B'den alınan (esas)
Views+Room mimarisi, ListModel/Repo/ListController (aynı denetleyici ana ekran + panel), balon (sürükleme, yay, kaldırma hedefi, rozet), yüzen liste paneli, DM Serif Display + IBM Plex Sans + vermilyon kimliği, koyu tema, testler.
## A'dan alınan
Araç zinciri ve hedef API 36; ders: API 30+ çağrısı korumasız olmayacak. Banner/klavye düzeni.
## C'den alınan
Odak korunumu (art arda ekleme), TR erişilebilirlik metinleri, 48dp hedefler, tamamlananları sil + geri al, metin paylaşımı.

## Değişenler
1. Odak: `AddFlow` (saf) + ListController: liste `focusable=false`/`blocksDescendants`, ekleme sonrası alan odağı ve klavye yeniden istenir (ana ekran ve panelde aynı kod). Boş giriş de odağı bırakmaz.
2. API 30+: `maximumWindowMetrics` ayrı `Api30` nesnesine alındı, `SDK_INT >= 30` ile korunuyor, altı `displayMetrics`. Ana ekran inset'leri AndroidX `ViewCompat/WindowInsetsCompat` ile (sistem çubukları + IME); targetSdk 36 kenardan kenara zorunlu olduğu için eklendi. Panel penceresinde `SOFT_INPUT_ADJUST_RESIZE` (deprecated, @Suppress) korundu: B'de emülatörde çalışmıştı. lint `abortOnError=true`: 0 hata, 24 uyarı (GradleDependency, PluralsCandidate, RtlSymmetry, UseKtx, ObsoleteSdkInt vb. önemsiz).
3. Banner akış içinde (üstte), klavye açılınca liste küçülür; ilk satırı örtmez (cihazda doğrulanmadı).
4. Erişilebilirlik: satır açıklaması "metin, tamamlandı/tamamlanmadı", tıklama eylemi adlı, sil/sırala düğmeleri öğe adını söyler, TalkBack "Yukarı/Aşağı taşı" eylemleri, balon açıklaması kalan sayıyı söyler, panelde eylem düğmesi "Kapat". Tutamak 40→48dp.
5. Geri al şeridi 5→6 sn; metinler string kaynağında ve düzgün Türkçe ("Tamamlananları sil", "Metin olarak paylaş"). Etiketler Undo sınıfından çıkarıldı.
6. Bildirim izni: ActivityResult API, daha önce reddedilmişse gerekçe diyaloğu, reddedilirse bilgi; balon izinsiz da çalışır. Overlay izni yoksa banner + açıklayıcı toast. FGS başlatma/addView `runCatching` ile çökmeye karşı korumalı.
7. Manifest: specialUse + `PROPERTY_SPECIAL_USE_FGS_SUBTYPE` (B'den). Silinen: B'nin rapor/anahtar dosyaları, `notif_asked`-yalnız-bir-kez mantığı.

## Testler
32 birim testi, 0 hata (B'den 25 + QuickAddTest 7: AddFlow odak kararı, art arda ekleme, toplu temizle+geri al+tekrar, silme-temizle undo yuvası, dışa aktarma, boş içe aktarma). Odak yalnız saf karar olarak test edilir; gerçek EditText/klavye odağı test EDİLMEDİ. Gerçek Room/SQLite JVM'de test edilmedi.

## R8 / Room (mantıksal kontrol)
`data.**` keep + RoomDatabase alt sınıfları keep; release dex'inde `NoktaDb_Impl` mevcut (strings ile doğrulandı), mapping'te var. Gerçek açılış/DB okuma cihazda denenmedi.

## Bilinen eksikler / test edilmeyenler
Cihaz testi hiç yapılmadı: balon, overlay paneli klavyesi, inset davranışı (özellikle API 35+ panel), FGS specialUse Android 14+, bildirim izni akışı, TalkBack, koyu tema görünümü. Lint'te 24 uyarı kaldı. Play Console beyanları (FGS videosu, veri güvenliği, gizlilik politikası) yapılmadı. İçerik başlığı: panel yüksekliği %52 sabit.
