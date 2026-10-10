# Çalışma günlüğü: Laptop ve OMEN kullanıcı deneyimi karşılaştırması

- **Tarih:** 2026-10-03
- **Ortam:** Claude Code bulut oturumu (Linux konteyner)
- **Dal:** `claude/lucid-hopper-h714gc`

## 1. Ne istendi
Özel kaynak metni bu PUBLIC depoya kopyalanmadı; aşağıdaki kendi özetimdir.

- **İlk istek:** Grafik tasarım, yazılım/uygulama geliştirme ve oyun için pahalı olmayan bir laptop. RAM'i, ekran kartı ve depolaması yeterli olmalı. Ürün bağlantıları HTTPS olmalı ve en sonda **çok sade bir "bilgisayar – fiyat" listesi** istendi.
- **İki düzeltme:** Kullanıcı iki kez "insanların kullanım deneyimine bak, kronik arızalı mal olmasın" dedi.
- **Ardından:** Ekşi Sözlük ve Reddit'te OMEN için ne dendiğini sordu.
- **Son soru:** "Bu kadar bilgisayar arasında o bilgisayara mı karar verdin?" diye kararı sorguladı.

Son düzeltme esas alındı. Teslimin merkezinde üç şey var: topluluk deneyimi, kronik sorunlar ve OMEN kararının sorgulanması.

Kaynak (`kaynak.txt`) ve görev (`gorev.md`) dosyaları yalnızca özel çalışma alanına kaydedildi (bkz. §6).

## 2. Kullanılan araçlar ve komutlar (sırayla)
1. **Depo kontrolü:** `git status` ve `git branch`. Mevcut projelere (`android-app`, `windows-agent`, `docs`) dokunulmadı.
2. **Özel alana kayıt:** `kaynak.txt` ve `gorev.md` scratchpad'deki `ozel/` klasörüne yazıldı, `sha256sum` ile hash'i alındı. `kaynak.txt` SHA-256: `d2daab8a2917faf6cc3c01e4cf96686a1317b6a8598af82fe43e07a7d6dd5c32`; `gorev.md` SHA-256: `2cd4de1e2b70be9848573fa63bbcd2ae2908f4ac46824f1fdc528200f2a8b813`.
3. **Ön arama:** 3 `WebSearch` sorgusu yapıldı. Sonuçlar arasındaki SEO çiftliği siteleri ayıklandı.
4. **Paralel araştırma:** 3 alt ajan WebSearch, WebFetch ve curl ile çalıştı:
   - (a) OMEN topluluk deneyimi ve kronik sorunlar
   - (b) Alternatiflerin güvenilirliği ve teknik verileri
   - (c) Türkiye güncel fiyatları
5. **Erişim testi (`curl`):** eksisozluk.com 403, reddit.com ve old.reddit 403, pullpush.io 429 (hız sınırı) döndü.
6. **Yol değişikliği (B yolu):** Reddit için Arctic Shift arşiv API'sine geçildi (`/api/posts/search`, `/api/comments/search`).
   - Geniş metin sorguları zaman aşımına düştü. Bu yüzden `title=` ve `after=` filtreleriyle daha dar sorgularla yeniden çekildi.
   - **Sonuç (1. tur ifadesi, 2. turda düzeltildi):** 8 sorguda 614 ham kayıt alındı (= 585 benzersiz + 29 tekrar), 5 başlığın yorumları okundu. Veriler `kanit/reddit-arctic-shift-ozet.json` dosyasında.
7. **Fiyat çapraz kontrolü:**
   - `curl` ile epey.com Cloudflare engeline takıldı.
   - `WebFetch` ile 6 kilit ürün sayfası ikinci kez açıldı ve satıcı fiyatları alt ajanın bulgusuyla karşılaştırıldı. Hepsi birebir tuttu.
8. **Puanlama:** `python3` ile ağırlıklı puan hesaplandı. Ağırlıklar: ekran 25, güven 25, bellek 20, performans 15, fiyat 15.
9. **Kabul testi:** `python3 test/dogrula.py --ozel <özel kaynak> --ag` (bkz. §5).
10. **Teslim:** `git commit` ve `git push`, ardından uzak daldan geri okuma. Özel ZIP ve `SHA256SUMS` hazırlandı, ZIP açılıp hash'ler yeniden doğrulandı.

## 3. Kararlar
- **Teslim biçimi (A yolu):** Markdown rapor, sade liste, kaynak listesi ve doğrulama betiği. Bu bir araştırma işi ve kaynakta istenen ürün "sade liste + linkler"; uygulama değil. Bu yüzden gereksiz bir web uygulaması ya da sunucu kurulmadı.
- **Ekranın ağırlığı yüksek tutuldu:** Grafik tasarım için %58–62 sRGB paneller ya elendi ya da puan kaybetti.
- **Fiyat kaynağı olarak epey.com seçildi:**
  - akakce, cimri, Hepsiburada, Vatan ve MediaMarkt 403 verdi.
  - epey, pazaryeri satıcı fiyatlarını güncelleme zamanıyla birlikte gösteriyor.
  - Arama özetlerindeki eski fiyatlar güncelden %20–40 düşük çıktığı için kullanılmadı.
- **Sonuç:**
  - OMEN 16-ap0010nt önerilmedi: ekranı zayıf, arıza şikâyetleri bir küme oluşturuyor, servis şikâyetleri yoğun.
  - **Ana öneri:** Lenovo Legion 5 15IRX10, 99.999 TL.
  - **B yolu:** Casper Excalibur G915, 93.599 TL.
  - **C yolu:** Lenovo LOQ 15AHP10, 73.799 TL, artı RAM yükseltmesi.
- **Eski konuşmalar kanıt sayılmadı:** Daha önceki sohbetlerde OMEN'e karar verilmiş olması bir gerekçe olarak alınmadı; karar sıfırdan verildi.

## 4. Sorunlar ve çözümler
| Sorun | Çözüm |
|---|---|
| Reddit 403 verdi | Arctic Shift arşiv API'si kullanıldı (gerçek gönderi ve yorum metni) |
| Arctic Shift "Timeout, slow down" hatası | Başlık ve tarih filtreli dar sorgular, yeniden deneme döngüsü |
| Ekşi Sözlük 403 verdi, Wayback'te kopya yok | Arama özetleri kullanıldı ve "[A]" etiketiyle işaretlendi; rapor §7'de eksik olarak yazıldı |
| HP Community 403 verdi | Aynı sorunları anlatan Şikayetvar sayfaları curl ile açıldı |
| Fiyat siteleri 403 verdi | epey.com kullanıldı, WebFetch ile çift kontrol yapıldı |
| Günlüğe test çıktısı eklerken kabuk heredoc'u backtick'leri komut olarak çalıştırdı ve dosya bozuldu | Dosya silinip Write aracıyla yeniden yazıldı, test tekrar çalıştırıldı |

## 5. Doğrulamalar
Yerel kabul testinin çıktısı:

```
$ python3 test/dogrula.py --ozel <ozel/kaynak.txt> --ag
OK   : 77 benzersiz bağlantı, hepsi https kontrol edildi
OK   : liste.md 5 ürün satırı
OK   : sızıntı taraması (19 cümle parçası)
BİLGİ: ağ kontrolü 47/77 bağlantı 200 döndü
       403  eksisozluk.com (4 bağlantı)
       403  h30434.www3.hp.com (4) · gaming.lenovo.com (1)
       403  www.epey.com (9) · monsternotebook.com.tr (1)
       403  www.reddit.com (11)
SONUÇ: KABUL
```

- 200 dışında dönen 30 bağlantının tamamı 403 döndü (bot koruması). Bu durum o sayfaların canlı olduğunu kanıtlamaz.
- epey sayfaları aynı oturumda WebFetch ile açılabildi.
- Reddit içeriği Arctic Shift arşivinden okundu.
**2. tur test çıktısı:**
```
$ python3 test/dogrula.py --ozel <ozel/kaynak.txt> --ag
OK   : 79 benzersiz bağlantı, hepsi https kontrol edildi
OK   : liste.md 5 ürün satırı
OK   : regresyon + Reddit sayımı (614 = 585 + 29), 11 tam metin kanıtı
OK   : sızıntı taraması (19 cümle parçası)
BİLGİ: ağ kontrolü 49/79 bağlantı 200 döndü (403 = içerik görülmedi; canlılık/doğruluk kanıtı değil)
       30 × 403: eksisozluk 4, HP forum 4, Lenovo forum 1, epey 9, Monster 1, Reddit 11
SONUÇ: KABUL
```
**Düzeltme:** 1. turda yazılan "hiç 404 yok" ifadesi, 403 dönen sayfaların canlı olduğunu kanıtlamaz. Bu test yalnızca biçimi ve durum kodunu ölçer; olgusal doğruluk testi değildir.

Uzak dalın geri okuması ve ZIP doğrulaması için bkz. §7 ve teslim mesajı.

## 6. 2. tur: bağımsız inceleme düzeltmeleri
Bağımsız public kaynak incelemesi, 1. tur commit'i `279251a9ab8b2426881a91ad756427494aaf7090` için şu hataları buldu. Hepsi doğrulandı ve düzeltildi; ayrıntılar `rapor.md` §8'de.

| Bulgu | Doğrulama komutu / kaynak | Sonuç |
|---|---|---|
| 614 = 585 + 29 tekrar; JSON'da yalnızca metadata var | `python3` ile URL sayımı → `614 585 29` | JSON tekilleştirildi (`sayim` alanı eklendi). Tam metin, 11 gönderi için Arctic Shift `posts/ids` ve `comments/search` uçlarından yeniden çekildi (2026-10-03T13:29Z); SHA-256 değerleri `kanit/reddit-alinti-kaniti.json` dosyasında |
| ap0091ng'nin %58,1 ölçümü TR SKU'su değil; 115 W'a boost dahil | Notebookcheck 1135455 `curl` + WebFetch, HTML SHA-256 `e305498a329dc8ee2d4700f4c74ad6f21f8c96f2ceb23de34bcaca1f51763068` | "115 W (25 W Dynamic Boost dahil)". TR ap0010nt için ilan değeri %62,5 sRGB |
| Legion AHP10/OLED ölçümü 15IRX10/IPS'e uygulanmıştı | PSREF 15IRX10 PDF (`pdftotext`), SHA-256 `8f5f914e0f191b2943817f78b60db6af0809d75804184cbfab52f1bf84c2aac5` | Ölçüm aktarımı kaldırıldı. Yeni bilgiler: 115 W TGP, 2× M.2 x4, "32 GB'a kadar" (64 GB iddiası düzeltildi) |
| LOQ için yanlış nesil kaynakları | PSREF LOQ 15AHP10 PDF, SHA-256 `c6dff2afea75fa7c754a8a7b17ea34503a80ad0d668f650343a6b917ddce9dce` | 100 W, iki M.2 yuvası (x4 + x2), %100 sRGB panel (ilan) |
| G870 şikâyetleri G915'e yüklenmişti | Şikayetvar `casper/excalibur-g915` (curl), SHA-256 `46805c5e5c6732993a6f5f2c551997c25fc58daa062088628ac4b63487c55582` | 11 G915 şikâyeti ayrı yazıldı |
| Victus fa2019nt / fa2018nt | epey sayfası (WebFetch) | Ürün numarası C21SGEA esas alındı; fiyat yenilendi: 55.692,55 TL |
| Sayısal güvenilirlik puanı ve kesin sıralama | — | Kaldırıldı; yerine kanıt türü matrisi ve koşullu öneriler kondu |
| 403 ve bağlantı testi yanlış yorumlanmıştı | — | Test açıklaması ve rapor §7 düzeltildi; teste regresyon kontrolleri eklendi |

Fiyatlar 2. turda epey'den yeniden okundu (Legion, Casper, Nitro 16S, OMEN ap0010nt, LOQ, Victus): `kanit/fiyat-anlik-2026-10-03.json`.

**Teslim paketi değişikliği:** 1. tur ZIP'inde özel kaynak dosyaları da vardı. 2. tur ZIP'i **yalnızca görev klasörünü** içeriyor. Özel `kaynak.txt` ve `gorev.md` scratchpad'deki `ozel/` klasöründe kaldı.

## 7. Kalıcı teslim ve devam komutu
- **PUBLIC depo:** `gorevler/laptop-ve-omen-kullanici-deneyimi-karsilastirmasi/`. Burada yalnızca kamuya açık, türetilmiş içerik var.
- **Özel alan:** Oturum scratchpad'inde `ozel/` klasörü (`kaynak.txt`, `gorev.md`), teslim ZIP'i ve `SHA256SUMS`. Özel metin public depoya push edilmedi.
- **Devam komutu:** Oturum kesilirse yeni oturumda şunu çalıştırın:
  `git fetch origin claude/lucid-hopper-h714gc && git checkout claude/lucid-hopper-h714gc && python3 gorevler/laptop-ve-omen-kullanici-deneyimi-karsilastirmasi/test/dogrula.py`
  Fiyatları güncellemek için `rapor.md` §5 ve `liste.md` içindeki epey bağlantılarını yeniden açmak yeterli.

## 8. 3. tur: fiyat yenilemesi (2026-10-10)
- epey'deki 5 ürün sayfası WebFetch ile yeniden açıldı. Legion 109.999 TL, Casper 92.599 TL, LOQ 84.994,10 TL, OMEN 91.999 TL; Nitro 16S'de satıcı fiyatı yok.
- `rapor.md` §5.1, `liste.md` ve `kanit/fiyat-anlik-2026-10-10.json` güncellendi. Test sonucu: KABUL.
- Kullanıcı için tek sayfalık bir "kart" yayımlandı (claude.ai Artifact, özel). İçeriği `liste.md` ile aynı.
