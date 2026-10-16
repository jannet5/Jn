# Melodi Zil - Arastirma Notlari (ezgi cikarma, mobil, YouTube, emulator)

Tarih: 2026-10-16. Sure siniri (23:36 UTC) yuzunden WebSearch ile hizli tarama yapildi; derin makale okumasi (WebFetch) yapilamadi.
Isaretler: [KAYNAK] = aramada gorulen, [BILGI] = benim onceki bilgim, bu turda DOGRULANMADI, kullanmadan once olc.

## 1) Karisik sarkidan ana ezgi: en iyi acik yontem

KARAR ONERISI: Ana yol "RMVPE dogrudan karisim uzerinde" olsun; vokal ayirmayi (BS/Mel-RoFormer) yalniz telefon disinda, A/B olcumde RPA >= +3 puan kazandirirsa ekle.

- [KAYNAK] RMVPE makalesi: ayirma adimini atlayip dogrudan polifonik sesten vokal perdesi cikarir; ayirmaya dayanan hatlarin (Spleeter + pYIN/CREPE/HARMOF0) ayirici kalitesine bagli olmasini sorun gorur. MIR-ST500'de ikinci en iyi modelden (HARMOF0) RPA'da yaklasik 6.3 puan, genel dogrulukta yaklasik 2.7 puan ustun. Not: bu karsilastirmada baz modeller Spleeter ile ayrilmis vokal uzerinde kosturulmus.
- [KAYNAK] FCPE ozeti (ikincil site, dogrula): MIR-1K'te RPA yaklasik %96.8, RMVPE ile benzer, parametre 10.6M (RMVPE 90.4M), GPU'da RMVPE'den yaklasik 5 kat hizli. FCPE telefon icin RMVPE'ye ilgi cekici alternatif; ancak MIR-1K temiz karaoke ses, pop karisimi degil.
- [KAYNAK] SwiftF0 (Agu 2025): 95.8K parametre, CPU'da CREPE'den ~42x hizli, gurultude iyi; AMA monofonik (tek ses) tasarim, karisim icin degil. basic-pitch ve PESTO icin bu turda veri bulunamadi.
- [KAYNAK] 2024-2025 SOTA yonu: Mel-RoFormer ile once vokal ayirma, sonra ayni modeli "vokal ezgi transkripsiyonu" icin ince ayar (ISMIR 2024). Yani en yeni akademik sonuclar "ayirma + ince ayar" yolunda; ama bu agir model (telefon icin uygun degil) ve ayirmasiz-RMVPE ile dogrudan RPA tablosu bulunamadi.
- Bosluk: RMVPE'nin MedleyDB sonucu bulunamadi. MedleyDB melodi bazen vokal degil enstruman; RMVPE sadece vokal egitimli, bu yuzden enstrumantal ezgide dusebilir.
- Pratik: Hedef (RPA >= %80 gercek sarkida) icin kendi olcumumuz (arastirma/sonuc/*.json) belirleyici olsun. Ayirma kazandirir mi sorusunu ayni 5-10 parcada "karisim vs ayrilmis vokal" olarak olc.

Kaynaklar:
- RMVPE makalesi: https://arxiv.org/pdf/2306.15412
- SwiftF0: https://arxiv.org/html/2508.18440v1 (benchmark: github.com/lars76/pitch-benchmark)
- Mel-RoFormer ile vokal ayirma + ezgi transkripsiyonu (ISMIR 2024): https://arxiv.org/abs/2409.04702

## 2) Telefonda (Android, arm64, CPU) calistirma

KARAR ONERISI: ORT-mobile/minimal build + RMVPE fp16 veya int8 ONNX (veya ORT formati) kullan, modeli ilk acilista indir; vokal ayirma modelini telefona koyma (ya da cok kucuk bir tane, olcumle).

- [KAYNAK] ONNX Runtime resmi mobil rehberi (1.18): hazir AAR yaklasik 24.4 MB, ozel (azaltilmis operator) build yaklasik 7.5 MB; arm64 libonnxruntime.so 16.3 MB'tan 4.0 MB'a. Minimal build ONNX formatini okuyamaz: model ORT formatina cevrilmeli. APK 30 MB siniri icin AAR'i kucultmek sart, model APK'ya girmemeli.
- [KAYNAK] RMVPE ONNX boyutlari (HF toplulugu, dogrula): fp32 yaklasik 345 MB, int8 (rmvpe_q8) yaklasik 94 MB; baska bir yuklemede 67.6 MB ve fp16 33.9 MB. Hangisi ayni mimari belli degil: kendi dosyani olc.
- [KAYNAK] RMVPE'nin Android hizi (3 dk sarki kac sn) icin yayinlanmis olcum BULUNAMADI. Olc: ORT CPU (XNNPACK) 2-4 is parcacigi, 3 dk ses. Genel kural: RTF < 1 ise gercek zamandan hizli.
- Int8: agirliklar yaklasik 4 kat kuculur; ama konvolusyon/GRU karisik modelde int8 dogrulugu bozabilir: nicemlenmis modelde RPA'yi mutlaka yeniden olc (fp16 daha guvenli).
- [BILGI, dogrulanmadi] Vokal ayirma: Spleeter TFLite ve MDX-Net/UVR ONNX modelleri onlarca MB; HTDemucs (ONNX) yuzlerce MB ve telefon CPU'sunda 3 dk icin dakikalar surer. Bu yuzden ayirma telefonda onerilmez.
- Strateji: APK < 30 MB (ORT minimal AAR ~7.5 MB + uygulama), model ilk acilista HTTPS ile indirilir (SHA-256 kontrolu, indirme ilerleme cubugu, Wi-Fi uyarisi), uygulama icinde saklanir.

Kaynaklar:
- ORT mobil rehber: https://onnxruntime.ai/docs/tutorials/mobile
- ORT ozel build: https://onnxruntime.ai/docs/build/custom.html
- RMVPE ONNX varyantlari: https://huggingface.co/TigreGotico/voiceclonnx-rvc

## 3) Perde egrisinden nota ve zil sesi

KARAR ONERISI: Perde egrisini medyan suzgecle + HMM/Viterbi nota izleyici ile notalara bol, min nota suresi ~100-120 ms uygula, tona (gam) yuvarla, oktavi 1-2 oktav araliga kat, zil icin kisa (en cok 20-30 sn) ve tekrar eden ezgi kisimini sec.

- [KAYNAK] pYIN: kare basina aday perdelerden HMM + Viterbi ile pürüzsüz yol secer; nota asamasi ayri bir HMM Viterbi'dir (Mauch & Dixon). Min sure kurali kisa yanlis notalari bastirir (Cheng, Dixon, Mauch 2015, piyano icin ama mantik ayni).
- [BILGI] Pratik adimlar: (a) guven < esik olan kareleri sessizlik say; (b) 5-9 karelik medyan suzgec; (c) cent cinsinden 50-70 cent'ten az kaymalari ayni nota say; (d) < 100 ms notalari komsuya birlestir veya at; (e) vibrato icin ortalama al.
- [BILGI] Ton bulma: Krumhansl-Schmuckler (perde sinifi histogramini 24 ton profiliyle korele et), sonra nota basina en yakin gam notasina yuvarla. Yanlis ton bulma riski var: gamda olmayan uzun notalari yuvarlamayin, yalniz kisa/kararsiz notalari yuvarlayin.
- [BILGI] Taninirlik: zil icin ritim/onset zamani, perde dogrulugundan en az o kadar onemlidir (insan beyni ritim+kontur ile taniyor). Nota sureleri ve bosluklari orijinale yakin kalsin; ayni kontur 1 oktav kaysa bile taninir (oktav katlama zararsiz), ama araliklar (yarim ses sayilari) bozulursa taninmaz. 8-bit sentez: kare/puls dalga (%25 duty), hizli atak, kisa release, tek ses (monofonik).
- Not: Bu bolumun [BILGI] maddeleri bu turda kaynakla dogrulanmadi; "tanima" icin kucuk kullanici testi (5 kisi, 10 sarki) onerilir.

Kaynaklar:
- pYIN / nota izleme: https://github.com/c4dm/pyin ve https://code.soundsoftware.ac.uk/projects/pyin
- Cheng, Dixon, Mauch (min sure, HMM): https://webspace.eecs.qmul.ac.uk/s.e.dixon/pub/2015/ChengDixonMauch-EUSIPCO2015-NoteTracking.pdf

## 4) YouTube ses akisi (2026) ve Google Play

KARAR ONERISI: YouTube linkini ana yol yapma; ANA YOL "kullanici ses dosyasi secer / Paylas ile alir / kendi kaydini yapar" olsun, YouTube cikarma ayri, kolayca kapatilabilir ve Play surumunde olmayan bir modul olsun.

- [KAYNAK] NewPipeExtractor: v0.26.3 (haziran) YouTube SABR zorlamasini baska bir player client ile asan gecici cozum getirdi; v0.26.4 (temmuz) playlist devam duzeltmesi; v0.26.5 (agustos) kucuk duzeltme. NewPipe uygulamasi 0.28.8'de SABR duzeltmesi, 0.29.x'te digerleri. Cozum cogu video icin calisir, "cocuklar icin yapilmis" videolarda calismaz. v0.27 bu turda goruldu degil: guncel durum icin GitHub release sayfasini kontrol et.
- [KAYNAK] yt-dlp PO Token Rehberi: YouTube PO token zorunlulugunu kademeli getiriyor; bazi istemciler (GVS) icin gerekli, tv istemcisi tokensiz ama cerezsiz DRM'li bicimler verebiliyor. bgutil gibi token saglayicilar artik cogu durumda "bot" kontrolunu gecmiyor. Ucuncu taraf notlari: web_embedded ve android_vr istemcileri token istemeyebilir ama siki kontrol edilen yuklemelerde calismaz; android istemcisinde androidSdkVersion alani token istegini tetikler (android_sdkless tarzi cozum sadece ses akislarinda isliyor, tek rapor).
- Sonuc: NewPipeExtractor Android'de (Java) calisabilir ama kirilgandir; her YouTube degisikliginde bozulabilir, surum guncellemesi ve "indirilemedi" B plani sart. [BILGI] "Sign in to confirm" hatasi sunucu/veri merkezi IP'lerinde daha sik gorulur; kullanici telefonunun ev IP'sinde daha seyrek (dogrulanmadi).
- [KAYNAK, zayif] Google Play: aramada YouTube indirici uygulamanin kaldirildigina dair dogrudan kaynak BULUNAMADI; bir 2026 yorum yazisi Play kurallarinin yetkisiz akis indirmesini kolaylastiran uygulamalari yasakladigini soyluyor (alintisiz). [BILGI] YouTube API Hizmet Sartlari/Play fikri mulkiyet politikasi YouTube sesi indiren uygulamalari genelde reddeder. Karar: Play'e YouTube indirme olmadan gonder.
- B plani (kisa): (1) "Dosya sec" (SAF/ACTION_OPEN_DOCUMENT, ses/video); (2) Paylas menusu (ACTION_SEND / ACTION_SEND_MULTIPLE ile gelen ses/video URI'si); (3) mikrofonla kayit (sarki cal, tut-konus), uyari: telefon mikrofonu karisik ses verir, dogruluk duser; (4) uygulama icinde YouTube yalnizca "yan yukleme (APK) surumunde" bayrakla acik.

Kaynaklar:
- NewPipeExtractor surumleri: https://github.com/TeamNewPipe/NewPipeExtractor/releases
- yt-dlp PO Token rehberi: https://github.com/yt-dlp/yt-dlp/wiki/PO-Token-Guide
- Medya indiricilerin Play'de olmama nedeni (yorum yazisi): https://www.androidheadlines.com/2026/05/beyond-the-app-store-why-the-best-media-downloaders-arent-on-google-play.html

## 5) KVM'siz Android emulator

KARAR ONERISI: x86_64 Android 8 (API 26) imaji, "-no-accel -gpu swiftshader_indirect -no-window -no-audio -no-snapshot -memory 2048" ile ac; cok yavas olursa Google API'siz (AOSP) imaji ve duz test gerektirmeyen cihaz ici adb testleri ile yetin; ARM ve Waydroid/redroid bu makinede olmaz.

- [KAYNAK] Google SDK emulatoru KVM'siz de acilir ("-no-accel"), cok yavas; ornek bayraklar: -no-accel -gpu swiftshader_indirect -no-window. Resmi dokuman swiftshader_indirect'in 36.4.9'dan itibaren kullanimdan kaldirildigini yaziyor: yeni emulatorde "-gpu software" veya "swiftshader" dene.
- [KAYNAK] Travis CI notu: x86 imajda "-memory 1500 -no-accel -gpu swiftshader" + kamera/boot animasyonu/snapshot kapatma islemis; x86, ARM imajindan hizli rapor edilmis. F-Droid yazisi: android-22..27 imajlari yeni imajlardan az kaynak ister, Google uygulamalari acilisi cok yavaslatir (AOSP/"default" imaj sec).
- [KAYNAK] Ilk acilis 5-10 dakikadan uzun surebilir; "-no-snapshot" tekrarlanabilir baslangic, bir kez acilan sonra adb ile bekleyip (sys.boot_completed) kullan.
- [KAYNAK] Redroid, Android'i host cekirdeginde konteyner olarak calistirir ve /dev/kvm kullanmaz (codersera rehberi onu KVM'siz CI icin onerir). [BILGI] Ancak host cekirdeginde binder (binder_linux) modulu gerekir; bu makinede DOGRULANDI: /dev/binder* yok, /dev/kvm yok, /proc/filesystems'te binder yok; yani redroid de calismaz. Waydroid ayni nedenle ve ekran gerektirdigi icin olmaz. Cuttlefish KVM ister; TCG ile pratik degil.
- [BILGI] Surec oldurme testi: `adb shell am kill <paket>` (yalniz arka plandaki uygulamayi oldurur, ekranda acikken calismaz), once HOME tusu ile arka plana al: `adb shell input keyevent KEYCODE_HOME`, sonra `am kill`; zorla oldurme icin `adb shell kill -9 $(pidof <paket>)` veya `am force-stop` (force-stop uygulamayi "durdurulmus" yapar, farkli: sistem durumu kaydetme yolunu denemez). Surec olumunden sonra uygulama simgesinden tekrar ac ve kaydedilen durumun (secilen dosya, ezgi sonucu) geri geldigini kontrol et.
- Geri donus: emulator calismazsa Robolectric/JVM birim testi + ORT'un masaustu (JVM) calismasiyla mantik testi, gercek cihaz testi kullaniciya birakilir.

Kaynaklar:
- Resmi emulator hizlandirma dokumani: https://developer.android.com/studio/run/emulator-acceleration
- KVM'siz emulator (2026 rehber): https://codersera.com/blog/android-emulator-docker-without-kvm/amp/
- F-Droid, GitLab CI'da emulator testi: https://f-droid.org/2021/02/24/running-emulator-tests-on-gitlab-ci.html

## Sinirlar ve eksikler

- 6 dakikalik sure sinirinda yalnizca WebSearch ozetleri okundu; makale tablolari (RMVPE Tablo 1, Mel-RoFormer, Interspeech 2025) acilmadi.
- Bulunamayanlar: RMVPE MedleyDB sonucu, RMVPE Android hizi, PESTO/basic-pitch sayilari, Play'in YouTube indirici ret kaynagi.
- [BILGI] ile isaretli satirlar dogrulanmadi.
