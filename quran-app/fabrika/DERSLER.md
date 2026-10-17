# DERSLER: Kur'an Oku

### D-001: Kullanıcı ara kapı istemiyor
- Kullanıcı ne dedi: "Kısım kısım yok kanka. Hep bitir full. Gerekirse bir saat çalış."
- Kök neden: Fabrika her aşama sonunda onay bekliyor; bu kullanıcı sonucu görmek istiyor.
- Yeni kural: Bu kullanıcıda aşamalar tek seferde yürütülür; sadece kullanıcının zevkine kalan seçimler (görünüm vb.) uygulamanın içine ayar olarak konur ve sonunda tek teslim yapılır.

### D-002: Seçimi kullanıcıya bırakmak yerine ayar yap
- Kullanıcı ne dedi: "Ben bir seçim yapmayayım. Oraya koy uygulama içerisinde. İsteyen istediğini seçsin."
- Yeni kural: Tasarım yönleri birbirini dışlamıyorsa (sayfa görünümü, yazı tipi) hepsi uygulamada hazır görünüm olarak sunulur; varsayılan = en "gerçek mushaf" olan.

### D-003: Kur'an metni ve düzeni uydurulmaz, doğrulanmış kaynaktan alınır
- Kullanıcı ne dedi: "Yuvarlakların üstüne gelmiş… harflerin araları bazen çok açık bazen çok kapalı… elif lam mim yanlış… var olan Kur'an'ı bulmalısın"
- Kök neden: Tanzil metni + genel Arapça yazı tipleri + elle çizilmiş ayet gülü + iki yana yaslama (justify). Justify kelimeleri kaydırıyor ama satır içi gülleri kaydırmıyordu; med işaretleri yazı tipine göre yanlış duruyordu.
- Yeni kural: Kur'an uygulamasında metin, yazı tipi ve sayfa/satır düzeni tek bir resmi kaynaktan gelir: KFGQPC Hafs metni + KFGQPC HAFS Uthmanic Script yazı tipi (değiştirilmeden) + quran.com QDC API `mushaf=5` kelime/satır verisi. Ayet gülü yazı tipinin kendi glifidir (Arapça rakam), elle çizilmez. Satırlar mushaftaki gibi kurulur; kelime arası eşit dağıtılır, taşan satır yatayda sıkıştırılır, harf aralığı asla açılmaz. Eski v4 API'nin kelime sayfa/satır alanları tutarsız (5:90 iki sayfada) — kullanılmaz. Python urllib Cloudflare'de 403 alır; curl kullanılır.
