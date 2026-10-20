# İstek kontrolü (2026-10-20): ne istendi, oldu mu, kanıtı ne

| # | İstek (kullanıcının sözü) | Durum | Kanıt (bugün kontrol edildi) |
|---|---|---|---|
| 1 | Kelime yaz, telefon internetten kökenini araştırsın; Google'da 1000 arama yok; telefona veri yüklenmesin | Oldu | Canlı motor: Ahmet, kitap, pijama, okul, bilgisayar, telefon, kahve, şeker sonuç verdi. Tarayıcıda insan gibi Ahmet ve bilgisayar denendi. APK 244 KB (veri yok). Android emülatörde (19 Ekim) kitap ve pijama sonuç verdi |
| 1b | "Kökenini ayır" (ah-met gibi parçalama) | Kısmen | bilgisayar → bilgi + sayar çıkıyor. Ahmet için parça yok, yalnız yol: Türkçe ← Osmanlı Türkçesi ← Arapça |
| 2-3 | Benzer projelere bak (GitHub dışı da), Google/forum | Oldu, anlatılmamıştı | docs/ARASTIRMA.md (16 proje bulundu, 7 repo kodu okundu) |
| 4-6 | Backend şart mı? Sunucu/anahtar olmasın | Oldu | Kodda sunucu yok; kod taramasında api key/secret/token yok |
| 7-9 | Yap, anlat, APK ver | Oldu | Release APK indirildi, sha256 repodakiyle aynı |
| 10 | GitHub linki | Oldu | github.com/jannet5/Jn/releases/download/koken-son-surum/Koken.apk |
| 11-12 | %100'de takılıyor → web yap | Kısmen | Web linki ve 3 alternatif indirme yolu var. Senin telefonunda takılmayacağının garantisi verilemez |
| 13 | Bitti mi + tek cümle + artifact link | Oldu | Kart v4 yayında |
| 14 | Hepsi okunsun, yapılsın, kart olarak verilsin | Oldu | Kart + APK dosya kartı + web dosya kartı |
| 15 | API key olmadan herkes kullansın | Oldu | 11 dış adresin hepsi anahtarsız çağrılıyor (TDK bu ortamdan bazen zaman aşımı veriyor) |
