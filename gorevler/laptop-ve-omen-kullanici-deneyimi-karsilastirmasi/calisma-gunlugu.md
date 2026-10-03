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
2. **Özel alana kayıt:** `kaynak.txt` ve `gorev.md` scratchpad'deki `ozel/` klasörüne yazıldı, `sha256sum` ile hash'i alındı. `kaynak.txt` hash'i `d2daab8a…dd5c32`.
3. **Ön arama:** 3 `WebSearch` sorgusu yapıldı. Sonuçlar arasındaki SEO çiftliği siteleri ayıklandı.
4. **Paralel araştırma:** 3 alt ajan WebSearch, WebFetch ve curl ile çalıştı:
   - (a) OMEN topluluk deneyimi ve kronik sorunlar
   - (b) Alternatiflerin güvenilirliği ve teknik verileri
   - (c) Türkiye güncel fiyatları
5. **Erişim testi (`curl`):** eksisozluk.com 403, reddit.com ve old.reddit 403, pullpush.io 429 (hız sınırı) döndü.
6. **Yol değişikliği (B yolu):** Reddit için Arctic Shift arşiv API'sine geçildi (`/api/posts/search`, `/api/comments/search`).
   - Geniş metin sorguları zaman aşımına düştü. Bu yüzden `title=` ve `after=` filtreleriyle daha dar sorgularla yeniden çekildi.
   - **Sonuç:** 8 sorguda 614 gönderi alındı, 5 başlığın yorumları okundu. Veriler `kanit/reddit-arctic-shift-ozet.json` dosyasında.
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

- 200 dışında dönen 30 bağlantının **tamamı 403**; sebep bot koruması. **Hiç 404 yok.**
- epey sayfaları aynı oturumda WebFetch ile açılabildi.
- Reddit içeriği Arctic Shift arşivinden okundu.
- Uzak dalın geri okuması ve ZIP doğrulaması için bkz. §6 ve teslim mesajı.

## 6. Kalıcı teslim ve devam komutu
- **PUBLIC depo:** `gorevler/laptop-ve-omen-kullanici-deneyimi-karsilastirmasi/`. Burada yalnızca kamuya açık, türetilmiş içerik var.
- **Özel alan:** Oturum scratchpad'inde `ozel/` klasörü (`kaynak.txt`, `gorev.md`), teslim ZIP'i ve `SHA256SUMS`. Özel metin public depoya push edilmedi.
- **Devam komutu:** Oturum kesilirse yeni oturumda şunu çalıştırın:
  `git fetch origin claude/lucid-hopper-h714gc && git checkout claude/lucid-hopper-h714gc && python3 gorevler/laptop-ve-omen-kullanici-deneyimi-karsilastirmasi/test/dogrula.py`
  Fiyatları güncellemek için `rapor.md` §5 ve `liste.md` içindeki epey bağlantılarını yeniden açmak yeterli.
