# Harita — Komik fotoğraf dörtlü sosyal medya paketleri

Tarih: 2026-10-03 · Ortam: Claude Code bulut kapsayıcısı (Linux, Python 3.11, Pillow, ffmpeg, ImageMagick)

## 1. Hedef (kaynak isteğinden)

| # | İstek (özet) | Ürün karşılığı | Kabul ölçütü |
|---|---|---|---|
| H1 | X'teki gibi garip/komik Türkiye fotoğrafları (cama yazılmış uyarılar, esnaf/apartman notları) bul, **tek klasöre** doldur, yolunu ver | `komik-foto-paketleri/foto/` (48 gerçek fotoğraf) + `kaynaklar.csv` (her fotoğrafın kaynak sayfası, varsa X gönderisi, görsel URL, SHA-256) | 48 dosya açılıyor, her satırda HTTPS kaynak var, görsel kontrolden geçti |
| H2 | Dörder dörder, **karışık** sıra: 1-4-7-10 → cebe, sonra 2-5-8-11, sonra 3-6-9-12 … | `paketler/paket-01..12/` (Instagram 4:5 ve TikTok 9:16 slaytları) | 12 paket × 4 foto, her foto tam bir kez, sıra kuralı test ile doğrulandı |
| H3 | Bu tür paylaşımlarda hangi müzikler kullanılmış → liste; **her pakete farklı müzik** | `arastirma/muzik-listesi.md` + her paketin `PAYLASIM.txt` dosyasında şarkı | 12 farklı şarkı, kanıt linkli liste |
| H4 | Paylaşanların açıklama kısmına ne yazdığını kaydet, aynı mantıkla açıklama üret | `arastirma/aciklama-ornekleri.md` + her pakette IG/TikTok açıklaması ve etiketler | Her pakette açıklama + etiket |
| H5 | Instagram ve TikTok'ta tek tek gönder | **Bulutta yapılamaz** (aşağıda E1) → paketler telefonda 1-2 dakikada yayımlanacak şekilde hazır + `YAYINLAMA-REHBERI.md` | Yayımlandı denmez; engel açıkça yazılır |

## 2. Bağımlılıklar

- Fotoğraf kaynağı: x.com araması giriş istiyor (curl → yalnız JS kabuğu); Ekşi Sözlük 403 (Cloudflare); `syndication.twitter.com` zaman tüneli 429.
  Çalışan yollar: Onedio derleme sayfaları (sunucu tarafı HTML, görseller img-s*.onedio.com), haber sitelerinin `og:image` görselleri,
  tek tek X gönderileri için herkese açık `cdn.syndication.twimg.com/tweet-result` uç noktası (gömülü tweet verisi).
- Müzik: telifli; dosya olarak eklenemez/paylaşılamaz → şarkı adı + uygulama içi müzik kütüphanesi.
- Yayın: kullanıcının Instagram/TikTok oturumu kendi tarayıcısında/telefonunda; bu bulut kapsayıcısında hesap, çerez, parola yok.

## 3. Yollar (A/B/C)

### Fotoğraf toplama
- **A (seçildi):** Onedio "komik uyarı / tekel bayii / apartman / absürt kare" derlemeleri + haber fotoğrafları + gömülü X gönderileri → kaynak linkiyle indirme. Gerekçe: erişilebilir, tür birebir uyuyor, her fotoğrafın kaynağı kanıtlanabilir.
- B: Doğrudan X araması (giriş gerekli) → bulutta kimlik yok, uydurulmaz. Kullanıcı kendi hesabıyla X'te "uyarı yazısı", "apartman notu" aratıp ek bulursa `veri/secim-ham.json`'a ekleyip betiği yeniden çalıştırabilir.
- C: Ekşi Sözlük "apartman girişlerine asılan yazılar" başlığı → 403. Playwright ile denenebilir ama Cloudflare + kullanım şartları; gerek kalmadı.

### Gruplama
- **A (seçildi):** Kullanıcının tarifi birebir: her 12'lik blokta (1,4,7,10) (2,5,8,11) (3,6,9,12). Klasör numaraları sabit tohumlu karıştırmayla verildi; böylece her dörtlüde en az 3 farklı tür (tekel tabelası, uyarı, apartman notu, absürt kare, haber, X) var.
- B: Tek uzun dizi 1,4,7,…,2,5,8,… sonra 4'e bölme → paketler blok sınırında kayıyor, kullanıcının "sonra ikiyi koydun" cümlesine daha az uyuyor.
- C: Rastgele → kullanıcı açık bir desen tarif ettiği için reddedildi.

### Müzik
- **A (seçildi):** Araştırmayla kanıtlanan arabesk/dramatik şarkılar listesi; her pakete farklı şarkı; Instagram/TikTok **uygulama içi** kütüphaneden eklenir (fotoğraf karuseline tek şarkı eklenebiliyor).
- B: MP3'leri indirip videoya gömmek → telif ihlali + platform sesi kapatabilir; reddedildi. Bunun yerine **sessiz** önizleme MP4'ü üretildi.
- C: Telifsiz müzik → istenen "Orhan Gencebay dramı" etkisini vermez; yalnız iş hesabı kısıtında yedek olarak rehberde not edildi.

### Yayınlama
- **A (seçildi):** Telefondan manuel yayın; her paket için hazır slaytlar + kopyala-yapıştır açıklama + şarkı adı. Müzik eklemek yalnız mobil uygulamada mümkün.
- B: Resmî API (Instagram Graph API içerik yayınlama: Business/Creator hesap + Facebook uygulaması + izin incelemesi; TikTok Content Posting API: uygulama denetimi, denetimsiz istemci yalnız özel/gizli paylaşım) → kullanıcının uygulaması/anahtarı yok; ayrıca API ile telifli müzik eklenemiyor.
- C: Playwright ile kullanıcının açık tarayıcı oturumunu sürmek → bu bulut kapsayıcısı kullanıcının bilgisayarındaki tarayıcıya erişemez; ayrıca otomasyon hesap kısıtlaması riski taşır ve web arayüzünde karusele müzik ekleme yok. Reddedildi.

## 4. Uygulama → Test → Teslim zinciri

```
veri/secim-ham.json ──► araclar/build.py ──► foto/ (48) + kaynaklar.csv
veri/paketler.json  ──┘        │            paketler/paket-XX/{instagram,tiktok}/1-4.jpg
                               │            paketler/paket-XX/PAYLASIM.txt + sessiz mp4
                               │            index.html (çevrimdışı galeri)
                               ▼
                      araclar/dogrula.py  (sıra kuralı, benzersiz müzik, boyutlar, SHA-256, ZIP geri okuma)
                               ▼
              komik-foto-paketleri.zip + .sha256  ──► kullanıcıya özel dosya olarak gönderim
```

Public depoya yalnız betikler, metadata (linkler) ve araştırma notları gider; üçüncü kişilerin fotoğrafları,
render edilmiş slaytlar, kullanıcı sohbeti ve özel kaynak dosyaları **gitmez**.

## 5. Engeller (E)
- **E1 — Yayın:** Instagram/TikTok hesap erişimi yok ve tarihî "onay verdim" bugünkü yayın yetkisi değildir → yayımlanmadı.
- **E2 — Kullanıcının verdiği somut örnek** ("bu binayı pisleteni bulacağım" yazılı cam): web aramasında bulunamadı; X araması giriş istiyor. Benzer türde 48 gerçek fotoğrafla karşılandı.
- **E3 — Müzik dosyası:** telif nedeniyle dahil edilmedi; şarkılar uygulama içinden eklenir.
