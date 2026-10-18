# Köken: dersler (Uygulama Fabrikası biçimi)

### D-001: Sonuç ekranı sözlük kaydı gibiydi, kullanıcı "direkt bana anlatsın" istedi
- Kullanıcı ne dedi: "Güzel de istediğim gibi değil. Direkt bana gelecek diyecek: işte şu anlama geliyor, kökü şu bilmem ne. Bayağı her şeyi anlatacak."
- Kök neden: Ürün tanımı (Aşama 01-03) onaylatılmadan doğrudan koda geçildi. Sonuç ekranı kaynak sitelerin verisini (sekmeler, İngilizce metin, teknik zincir) olduğu gibi gösterdi; "tek cevap, düz Türkçe anlatım" beklentisi hiç yazıya geçmemişti.
- Yeni kural: Sonuç ekranının ilk görünen kartı HER ZAMAN düz Türkçe bir anlatımdır: tek cümlelik özet, anlamı, nereden geldiği, kaynak sözcüğün anlamı ve kökü. Ham kaynak verisi (sekmeler, İngilizce metin) altta, varsayılan KAPALI bir bölümdür. Kullanıcıya görünen metinde İngilizce kalmamalı (çeviri başarısız olursa bile "İngilizce kaldı" diye açıkça işaretlenir).

### D-002: Emülatör testinde eski sürüm sessizce çalışmaya devam etti
- Kullanıcı ne dedi: (yok; testte yakalandı) Yeni sürüm kurulamadı (`INSTALL_FAILED_UPDATE_INCOMPATIBLE`, debug ≠ release imzası) ve eski arayüz ekranda kaldı.
- Kök neden: `adb install` çıktısı kontrol edilmeden ekran görüntüsüne güvenildi.
- Yeni kural: Her emülatör testinden önce `adb install` çıktısında "Success" görülür ve `dumpsys package` ile `lastUpdateTime` doğrulanır; imza farkında önce `adb uninstall` yapılır. Emülatörün proxy portu oturumdan oturuma değişebilir: `10.0.2.2:${HTTPS_PROXY##*:}` ile her seferinde yeniden ayarlanır.

### D-003: Tek bir kaynağa ulaşılamayınca sonuç sessizce eksik kalıyordu
- Kullanıcı ne dedi: (yok; testte yakalandı) Yavaş ağda İngilizce Wiktionary isteği zaman aşımına uğradı, "Nereden geldi?" ve kök hiç görünmedi, hata mesajı da yoktu.
- Kök neden: Başarısız istekler "kayıt yok" gibi yutuluyordu.
- Yeni kural: Ağ hatası olan istek bir kez yeniden denenir; yine olmazsa sonuç "eksik" işaretlenir ve ekranda "Bazı kaynaklara ulaşılamadı, tekrar dene" şeridi çıkar. Eksik sonuç önbelleğe alınmaz.
