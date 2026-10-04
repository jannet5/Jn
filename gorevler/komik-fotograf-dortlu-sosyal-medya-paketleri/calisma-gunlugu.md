# Çalışma günlüğü — Komik fotoğraf dörtlü sosyal medya paketleri

Tarih: 2026-10-03 · Ortam: Claude Code bulut (Linux). Dal: `claude/jolly-gates-1tii5c`.
Gizlilik: Kullanıcının tam sohbeti, `kaynak.txt` ve `gorev.md` yalnız özel çalışma alanında (scratchpad) tutuldu; bu public depoya konmadı.
Üçüncü kişilere ait fotoğraflar ve bunlardan üretilen slaytlar da depoya konmadı; yalnız özel ZIP'te.

## 1. Ne istendi (özet)
X'teki gibi garip/komik Türkiye fotoğrafları (cama yazılmış uyarılar vb.) → tek klasör; 4'lü karışık gruplar (1-4-7-10, sonra 2-5-8-11 …);
her gruba farklı arabesk müzik (Orhan Gencebay tarzı), müzik ve açıklamaların benzer paylaşımlardan araştırılması; Instagram ve TikTok'a gönderim.

## 2. Adımlar, araçlar, komutlar
1. Kaynaklar özel alana kaydedildi (`kaynak.txt` 27 satır, SHA-256 2aa20e60…).
2. Erişim testi (`curl`): onedio.com 200; x.com arama 200 ama yalnız JS kabuğu (giriş gerekli); nitter bağlantı reddi; Reddit 403; Ekşi Sözlük 403;
   `syndication.twitter.com` zaman tüneli 429; `cdn.syndication.twimg.com/tweet-result` (tekil gömülü tweet) **çalışıyor**.
3. WebSearch ile tür derlemeleri bulundu (Onedio uyarı/tekel/apartman/absürt listeleri, haber siteleri).
4. 192 Onedio makalesinden 1.814 X gönderi kimliği çıkarıldı, syndication ile 861 fotoğraflı tweet indirildi. Çoğu spor/yabancı mizah çıktı
   (türe uymadı); temas sayfalarıyla görsel kontrol sonucu 2 X fotoğrafı seçildi.
5. Onedio liste sayfaları ayrıştırıldı (`<section data-id>` + 1200 px görsel), haber sayfalarından `og:image` alındı.
   Temas sayfaları (`montage`) ile **her aday gözle kontrol edildi**; 48 fotoğraf seçildi.
6. Müzik/açıklama araştırması ayrı alt ajanla yapıldı: TikTok sayfaları istemci tarafında oluştuğu için Playwright Chromium kullanıldı;
   TLS doğrulaması kapatılmadı, bunun yerine `libnss3-tools` kurulup proxy CA'sı kullanıcı NSS deposuna eklendi. ~1.300 TikTok gönderisi incelendi.
7. `araclar/build.py` yazıldı: indir → numarala → grupla → slayt (IG 1080×1350, TikTok 1080×1920, bulanık arka plan + kaynak etiketi)
   → sessiz 10 sn MP4 (ffmpeg/libx264) → `PAYLASIM.txt` → `index.html` → ZIP + SHA-256.
8. `araclar/dogrula.py` ile kabul testleri koşuldu (aşağıda).

## 3. Kararlar
- Karışık sıra: kullanıcının cümlesi ("1,4,7,10'u cebe koyduk, sonra 2…") birebir → 12'lik bloklarda adım-3 gruplar.
  Klasör numaraları sabit tohumlu karıştırma ile verildi ki her dörtlüde ≥3 farklı tür olsun (ilk denemede kategori sırası tekelleri bir pakette topluyordu).
- Müzik dosyası gömülmedi (telif); şarkı uygulama içinden eklenir. Önizleme videosu bu yüzden sessiz.
- Yayın: yapılmadı (hesap erişimi yok; tarihî "onay verdim" bugünkü yetki değil). Playwright ile tarayıcı otomasyonu reddedildi:
  bulut kullanıcının tarayıcısına erişemez, platform şartları otomasyonu yasaklıyor, web arayüzünde karusele müzik yok.
- Onedio görsel başlıkları Onedio editörlerinin espri başlıklarıdır; apartman notları ve boş başlık için görsele bakarak açıklayıcı başlık yazıldı.

## 4. Sorunlar ve çözümler
- `pbs.twimg.com` Python urllib'e 403 → curl + tarayıcı UA ile `?format=jpg&name=small` çalıştı.
- İlk kategori sırası homojen paketler üretti → tohumlu karıştırma + "≥3 tür" koşulu.
- Haber görsellerinden biri WebP çıktı → Pillow ile JPEG'e dönüştürülüyor.
- Galeri ekran görüntüsünde kırpılma → başsız Chromium'un en küçük görüntü alanı 500 px (`innerWidth`=500 ölçüldü); yine de CSS taşmaya karşı sağlamlaştırıldı.
- `__pycache__` yanlışlıkla commit'lendi → kaldırıldı, `.gitignore` eklendi.

## 5. Doğrulama (gerçek çıktı)
`python3 araclar/dogrula.py <teslim>` → TÜM KABULLER GEÇTİ:
48 satır kaynak + HTTPS; 48 foto açılıyor, SHA-256 eşleşiyor, kopya yok; sıra kuralı; her foto bir kez; 12 paket × (4 IG + 4 TikTok) boyutları,
10 sn video, PAYLASIM.txt alanları; 12 farklı şarkı; açıklama + ≥3 etiket; ZIP SHA-256, `testzip`, ZIP'ten geri okunan `kaynaklar.csv` eşleşmesi.
Görsel kontrol: 12 paketin tüm slaytları temas sayfasında incelendi; MP4'ten kare çıkarıldı.

## 6. Yapılmayan / kullanıcıda kalan
- Instagram/TikTok yayını (telefondan, `YAYINLAMA-REHBERI.md`).
- Telefon/Windows üzerinde açma testi yapılmadı (bulutta yok).
- Kullanıcının örnek verdiği "bu binayı pisleteni bulacağım" camı bulunamadı.

## 7. Yeniden üretim
```
pip install pillow   # ffmpeg gerekli
python3 araclar/build.py /çıktı/klasörü
python3 araclar/dogrula.py /çıktı/klasörü
```

## 8. Teslim boyutu düzeltmesi
İlk ZIP 32,3 MiB idi; dosya gönderim sınırı 30 MiB → reddedildi. Çözüm: orijinal JPEG baytları yeniden sıkıştırılmadan saklandı
(11 MB → 8,6 MB, kaynağa daha sadık), slayt JPEG kalitesi 88→85, video CRF 26→28. Yeni ZIP 29.112.275 bayt (27,8 MiB),
SHA-256 `9fa22357ca999d2458f71ce96860bb6a2d98af98156d76dfb4e2e94e9ee6948d`; `dogrula.py` yeniden TÜM KABULLER GEÇTİ; kullanıcıya özel dosya olarak iletildi.

## 9. GitHub teslimi (2026-10-04)
- Sohbetteki dosya kartı kullanıcının cihazında indirmede %100'de takıldı; kullanıcı GitHub linki istedi.
- Bu oturumun GitHub entegrasyonu yeni depo açamadı (`POST /user/repos` → 403 "Resource not accessible by integration").
  Fotoğraflar üçüncü kişilere ait olduğu için ZIP public `jannet5/Jn` deposuna **konmadı**.
- Kullanıcı tarafı (yerel GitHub oturumu `cengizdastan8`) bildirimi: PRIVATE `cengizdastan8/komik-foto-paketleri` deposu açıldı,
  `teslim-20261004` release'ine ZIP yüklendi, GitHub'dan geri indirilip aynı SHA-256 doğrulandı:
  - Link: https://github.com/cengizdastan8/komik-foto-paketleri/releases/tag/teslim-20261004
  - 29.112.275 bayt, SHA-256 `9fa22357ca999d2458f71ce96860bb6a2d98af98156d76dfb4e2e94e9ee6948d`
  - Kullanıcı tarafı bağımsız kontrol: 177 dosya/CRC, 48 benzersiz foto, 12 paket sıra kuralı, 12 farklı şarkı, 96 slayt ölçüsü, 12 video süresi 10,13 sn.
  - İndirmek için `cengizdastan8` hesabıyla GitHub'a giriş gerekir.
- Bu bulut oturumu o özel depoya erişemediği için release'i **kendisi doğrulayamadı**; doğrulama kullanıcının bildirdiği sonuçtur.
  Yerel ZIP'in hash'i aynı değerle eşleşiyor (`sha256sum -c` → OK).

## 10. Kapsam sınırı (güncel)
- Hazırlanmış içerik teslimi tamam (48 foto, 12 dörtlü paket, müzik listesi, açıklamalar, özel GitHub release).
- **Açık kalanlar:** Instagram/TikTok yayını yapılmadı (hesap erişimi/yetki yok); kullanıcının örnek verdiği
  "bu binayı pisleteni bulacağım" cam fotoğrafı bulunamadı; tam kare video çözümleme ve gerçek telefon/yayın kabulü yapılmadı.
