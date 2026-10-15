# Rakip uygulama özellik listesi ile Notivo karşılaştırması

İlk istek: "uygulamanın özellik açısından eksikleri olmasın, internetteki checklist'e bak". Hazır, tek bir resmi checklist bulunamadı; bu yüzden rakip
uygulamaların gerçek özellik listelerinden bir checklist çıkarıldı ve Notivo ile satır satır karşılaştırıldı.

## Kaynaklar (ne okundu, ne okunamadı)

| Kaynak | Durum |
|---|---|
| [Android Ally: 11 en iyi bildirim uygulaması](https://www.androidally.com/best-notification-apps/) | Okundu: Pushover, Notification Manager, Notification Blocker & Cleaner, Notisave, FilterBox, Notification History, Notification History Log, Recent Notifications |
| [TechCrunch: Pinnit](https://techcrunch.com/2024/09/30/pinnit-android-app-lets-you-search-through-your-notification-history) | Okundu: geçmiş, arama, sıralama, uygulama süzgeci, tarih seçici, kaçırılanı sabitleme, özel hatırlatıcı |
| Play Store sayfaları (Notisave, NotiLog) | **Okunamadı**: sayfa içeriği araca kesilmiş geldi. Bu iki uygulama için daha önceki arama özetleri kullanıldı (geçmiş, arama, uygulama süzgeci, sesli okuma, "okundu işareti bırakmadan okuma") |
| Genel notification-history uygulaması aramaları | Okundu: BuzzKill, DoNotNotify, NotiFilter, ReNotify özet özellikleri |

## Karşılaştırma

| Rakip özelliği | Kaynak | Notivo 1.3.0 |
|---|---|---|
| Bildirim geçmişi, kapatılanları saklama, arama | hepsi | Var |
| Uygulamaya göre zaman çizelgesi | Notification History Log | Var (Uygulamaya göre görünüm, varsayılan) |
| Uygulamayı kara listeye alma / belirli uygulamaları saklamama | History Log, ReNotify | Var |
| Filtre kuralları: gizle, kapat, sessize al | FilterBox, BuzzKill, DoNotNotify | Var (ENGELLE / SESSİZ; kelime + uygulama) |
| Hassas içeriği saklamama | FilterBox | Var (gizli bildirimleri atlama) |
| Parmak izi kilidi | FilterBox | Var |
| Yedekle / geri yükle | History Log | Var (JSON), CSV dışa aktarma |
| Gün limiti ile otomatik silme | Recent Notifications | Var (7/30/90/süresiz) |
| Günlük özet / istatistik | FilterBox | Var |
| Kaydırarak silme, favoriler | Notification History, History Log | Var (yıldız) |
| Okundu işareti bırakmadan okuma | Notisave | Var (yalnızca dinler) |
| Zamanlama / erteleme | Notification History, ReNotify | Var (15 dk – 12 sa, yeniden başlatmada kalıcı) |
| **Kaçırılan bildirimi geri koyma / yeniden gösterme** | FilterBox, ReNotify, Pinnit | **Eklendi 1.3.0**: özgün saat ve uygulama simgesiyle, sessiz kanalda |
| **Metni kopyalama** | FilterBox | **Eklendi 1.3.0** |
| **Sıralama (yeni/eski)** | Pinnit | **Eklendi 1.3.0** |
| **Tarih filtresi** | Pinnit, Notification History (premium) | **Eklendi 1.3.0**: tüm zaman, bugünkü, dünkü, 7 gün, 30 gün |
| **Kendi hatırlatıcı bildirimi** | Pinnit | **Eklendi 1.3.0**: başlık, not, 15 dk – yarın |
| **Sesli okuma (TTS)** | NotiLog, Shouter | **Eklendi 1.3.0** (cihazda denenmedi) |
| **Ana ekran widget'ı** | Notification History Log, Pushover | **Eklendi 1.3.0**: okunmamış sayısı + en yeni 3 (cihazda denenmedi) |
| Bildirim sesini değiştiren kural | FilterBox | **Yapılmadı**: Android başka uygulamanın bildirim sesini uygulama tarafından değiştirmeye izin vermez |
| Renk temaları | FilterBox | Kısmen: Material You dinamik renk + koyu tema (özel tema seçici yok) |
| Çöp temizleme, görev öldürücü, oyun hızlandırıcı | Notification Manager (AIO) | **Yapılmadı**: bildirim yönetimi dışı, istenmedi |

İngilizce çeviri ve Play görselleri bu kaynakların hiçbirinde bir özellik olarak geçmiyor; istenmediği için eklenmedi.
