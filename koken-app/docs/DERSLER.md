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

### D-004: Anlatım "kutular" gibiydi; Arapça için "kök harfleri → kalıp → sözcük" hikâyesi yoktu
- Kullanıcı ne dedi: "Direkt bana gelecek diyecek: işte şu anlama geliyor, kökü şu bilmem ne. Bayağı her şeyi anlatacak. İşte Arapça ise kökü şu şu harf, sonra bir araya gelmiş, şöyle olmuş."
- Kök neden: Anlatım etiketli kutulardı ("ANLAMI / NEREDEN GELDİ? / KÖKÜ") ve yeniden eskiye doğru ("A, B'den gelir, o da C'den") yazılmıştı; kök harfleri tek satırdı, harflerin hangi kalıpla birleşip sözcüğe dönüştüğü yoktu.
- Yeni kural: Sonuç metni ESKİDEN YENİYE akan düz Türkçe anlatımdır: ne demek → (Arapça vb. ise) kök harfleri "م + ي + ز" → hangi kalıp/bâb ile fiil → etken ortaç/mastar/üstünlük sıfatı → hangi dile nasıl geçti → Türkçedeki hâli. Metin sentezlenemeyen hiçbir şeyi uydurmaz; bilgi yoksa o cümle hiç yazılmaz. Dil adlarına Türkçe hâl ekleri doğru yazılır (Arapçadan, Latinceye, Osmanlı Türkçesinde) ve birim testiyle korunur.

### D-005: Anlatım yazı olarak kaldı; kullanıcı kelimenin KÖKTEN BAŞLAYIP AŞAĞI DOĞRU "YARATILDIĞINI" görmek istedi (her dilde)
- Kullanıcı ne dedi: "Kelimenin oluşumu aşağı doğru yaratılmaya başlayacak. Kökünden başlayacak. Kökünün ne anlama geldiğini, nereden geldiğini, nasıl oluştuğunu görmek istiyor. Ama hiç dil fark etmez: Farsça, Arapça, Fransızca, Çince. Uygulamamızın verdiği hizmet böyle olmalı, yoksa anlamı yok."
- Kök neden: Anlatım düz yazı paragrafıydı ve Arapça dışındaki diller için kök/atalar zinciri tek bir kaynak sayfasındaki listeden ibaretti; dil seçimi sabit (Türkçe/İngilizce) olduğu için Farsça "دوست" gibi sözcüklerde yanlış dil anlatılıyordu; "bileşenler" (ordinateur = ōrdināre + -tor) yanlış basamağa yazılıyordu; "kader" fiilin okunuşuna anlamı karışıyordu.
- Yeni kural: Sonuç ekranının ana parçası "OLUŞUMU" kartıdır: sözcük kökten bugüne doğru basamak basamak, yukarıdan aşağı ve sırayla belirerek "yaratılır" (KÖK → FİİL/ATA → … → BUGÜN). Her basamakta dil, yazılış, okunuş, anlam ve "bir öncekinden nasıl oluştu" cümlesi vardır; Arapça gibi kök harfli dillerde kök harfleri tek tek kutularda, kökün anlamı ve kökeni ile birlikte gösterilir. Aynı yazılış birden çok dilde varsa dil seçici çıkar; dil seçimi sayfadaki sıraya değil önceliğe (Türkçe, İngilizce, yaygın diller) göre yapılır. En eski ata kendi sayfasından derinleştirilir (en çok 2 kat geri, kök bulunursa durur). Düz yazı hâli isteğe bağlı açılır ("Yazı olarak oku"). Dile özel kod yazılmaz: veri kaynağı her dili aynı biçimde verdiği için aynı hat hepsinde çalışır; yönlendirme sayfaları (sadeleştirilmiş Çince) bir kez takip edilir.
