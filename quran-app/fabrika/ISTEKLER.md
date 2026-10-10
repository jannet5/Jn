# İSTEKLER: Kur'an Oku — kullanıcının istediği her şey ve durumu

| # | İstek (kullanıcının sözleriyle) | Uygulamada | Sürüm |
|---|---|---|---|
| 1 | Android, sadece Kur'an okumak için | ✅ Meal/ses/vakit yok; reklam, hesap, internet izni yok | 1.0 |
| 2 | Hafif olsun ("diğerleri çok ağır") | ✅ APK ≈ 2,6 MB, indirme gerektirmez | 1.2 |
| 3 | Arama: "Ma" yazınca altta öneri, Mâide'ye git | ✅ Canlı öneri, ilk sıra Mâide; aksansız/Latin/Arapça/yazım hatası | 1.0 |
| 4 | Cüz: "20" yazınca 20. cüze git | ✅ "20" → önce 20. Cüz; "s 302" sayfa, "2:255" ayet | 1.0 |
| 5 | Okuması güzel, harfler güzel | ✅ Kral Fahd Matbaası hattı (KFGQPC Hafs) varsayılan | 1.1 |
| 6 | Birkaç yazı/görünüm seçeneği; "hepsi uygulamada olsun, isteyen seçsin" | ✅ 4 yazı tipi + 6 hazır görünüm + özel renk | 1.2 |
| 7 | Büyütme/küçültme yavaş yavaş, zıplamadan | ✅ İki parmakla kesintisiz; kaydırıcı %1 adım; ± düğmesi yumuşak geçiş; okunan yer kaymaz | 1.2 |
| 8 | Gerçek Kur'an sayfası gibi, kağıt dokusu hissedilsin | ✅ Lifli/dokulu kağıt, kenar gölgesi, altın çerçeve | 1.0 |
| 9 | Yazı gerçekçi, kağıda işlenmiş/kabartma gibi | ✅ Doku açıkken kabartma (basılmış mürekkep) gölgesi | 1.0 |
| 10 | Beyaz zemin siyah yazı seçeneği | ✅ "Sade" görünümü | 1.0 |
| 11 | Mavi vb. renk, harf rengi, sayfa rengi seçilsin | ✅ 14 sayfa + 12 yazı rengi + serbest renk seçici; "Mavi", "Lacivert" hazır | 1.0 |
| 12 | Hem düz hem dokulu sayfa | ✅ Doku aç/kapa, çerçeve aç/kapa | 1.0 |
| 13 | Yayınlanacak kalitede, MVP değil; "kısım kısım yok, full bitir" | ✅ Testler, imzalı APK, emülatörde tüm akışlar denendi | 1.0+ |
| 14 | Ayet gülleri yazının üstüne binmesin | ✅ Gül yazı tipinin kendi glifi | 1.1 |
| 15 | Harf/kelime araları bazen çok açık bazen kapalı olmasın | ✅ Medine mushafı satır düzeni; aralar satır içinde eşit, en fazla %6 genişletme | 1.2 |
| 16 | الٓمٓ gibi işaretler doğru olsun; "var olan Kur'an'ı bul" | ✅ KFGQPC Hafs metni + 604 sayfa/15 satır resmi düzen | 1.1 |
| 17 | Link ile indirince %100'de takılmasın | ⏳ jsDelivr linki (doğru APK türü) — kullanıcı onayı bekleniyor | — |
| 18 | Artifact'ten indir, mümkünse indirince direkt aç/kur | ⏳ Artifact sayfası hazır; Android otomatik kurmaya izin vermez, "Aç" düğmesi kalır | — |
| 19 | Drive'a at (rclone kullanmadan) | ❌ Bağlayıcı APK gibi ikili dosyayı yükleyemiyor; belgeler Drive'da | — |
| 20 | Çalışan teslim yöntemini belleğe al | ⏳ Kullanıcı "işe yaradı" deyince DERSLER'e ve kalıcı kurala yazılacak | — |

## Uygulamada doğrulama (2026-10-10, sürüm 1.2.0, Android 8 emülatörü)
| İstek | Nasıl denendi | Sonuç |
|---|---|---|
| 3 | Arama kutusuna "ma" → ilk öneri Mâide → dokununca sayfa 106 | ✅ |
| 4 | "20" + Enter → 20. Cüz, sayfa 382 | ✅ |
| 5, 14–16 | Fâtiha, Bakara, Mâide sayfaları; ayet gülleri binmiyor, الٓمٓ doğru | ✅ |
| 6 | 4 yazı tipi listede, Scheherazade seçilince sayfa değişti | ✅ |
| 7 | "Büyüt" ×6 → sayfa net büyüdü, satır başı (sağ) görünür | ✅ (iki parmak emülatörde denenemedi) |
| 8–12 | Mushaf (dokulu), Sade (beyaz/siyah), Lacivert, Gece, Mavi; serbest renk seçici (ton/canlılık/açıklık) | ✅ |
| — | Akan yazı düzeni | ✅ |
| — | Yer imi ekle → İçindekiler > Yer imleri'nde görünüyor | ✅ |
| — | Uygulamayı kapatıp açınca kaldığı sayfa (105) | ✅ |
| 1–2 | APK 2,6 MB; tek izin Android'in kendi iç izni, internet izni yok | ✅ |
| 17 | jsDelivr linki: HTTP 200, tür application/vnd.android.package-archive, içerik birebir | ✅ sunucu tarafı; telefonda kullanıcı onayı bekleniyor |
