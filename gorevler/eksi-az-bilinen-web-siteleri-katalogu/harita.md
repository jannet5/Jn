# Harita: Ekşi az bilinen web siteleri kataloğu

Tarih: 2026-10-03

## Hedef

Ekşi Sözlük'teki **"az kişinin bildiği muhteşem web siteleri"** başlığının
(https://eksisozluk.com/az-kisinin-bildigi-muhtesem-web-siteleri--2764697)
**son sayfasından geriye 200 sayfasında** paylaşılan siteleri tek bir TXT
kataloğunda toplamak:

- tam `https://` bağlantı, her site **bir kez**;
- karşısında sitenin ne olduğunu anlatan **tek, tam bir cümle**;
- ardından **"Bu siteyle … yapabilirsin."** kalıbında somut kullanım cümlesi;
- **liste** biçimi (tablo değil), öğeler arasında rahat okunur boşluk;
- **kategorilere** ayrılmış;
- yalnız çalışan siteler, çalışmayanlar ayrı raporda.

## Bugünkü gerçek durum (2026-10-03'te ölçüldü)

| Bulgu | Kanıt |
|---|---|
| Başlık şu an **809 sayfa** (kullanıcı konuşmasında 807'ydi) | Sayfalardaki `data-pagecount="809"` |
| İstenen aralık bugün **809 → 610** (ilk 100: 809–710, sonraki 100: 709–610) | Hesap |
| Düz `curl` ve WebFetch başlık sayfalarında **Cloudflare 403** alıyor; tek entry sayfaları açılıyor | `cf-mitigated: challenge` başlığı |
| `robots.txt`: `ClaudeBot`, `anthropic-ai`, `ChatGPT-User` için `Disallow: /`, tüm site için `Content-Signal: ai-train=no, search=yes, ai-input=no` | https://eksisozluk.com/robots.txt |

**Sonuç:** Ekşi, yapay zekâ ajanlarının siteyi gezmesini ve içeriğin yapay
zekâya girdi olmasını açıkça istemiyor. Bu yüzden bulut ajanının Cloudflare'i
aşarak 200 sayfayı kendisinin çekmesi **seçilmedi** (denendi, 43. sayfada
durduruldu, o veri kullanılmadı). Yol, sayfaları kullanıcının kendi
tarayıcısında, kendi erişimiyle gezen ve **yalnız bağlantıları** çıkaran bir
araca çevrildi.

## Bağımlılıklar

1. Başlık sayfalarına erişim → **kullanıcının kendi Chrome'u** (kullanıcı
   809..610 aralığını görünür sayfadan, sayfa seçiciyle kendisi topluyor).
2. Sayfa JSON'ları (`page`, `page_url`, `observed_at`, `links:[{label,url}]`;
   entry metni yok) → `katalog_olustur.py tekillestir`.
3. Üçüncü taraf sitelere güvenli erişim (`url_denetim.py`) → çalışıyor mu,
   HTTPS gerçekten var mı.
4. Açıklamalar (`aciklamalar.json`) → kural denetimi.
5. Doğrulanmış katalog → kalıcı teslim (depo + özel ZIP, SHA-256).

## Yollar

### A — Kullanıcı Chrome'da toplar, bulut üretir (SEÇİLEN, sürüyor)

Kullanıcı sayfaları kendi tarayıcısında toplayıp JSON olarak veriyor. Bulut
tarafı yalnız bağlantıları işliyor; Ekşi metni yapay zekâya girmiyor.
İlk sürümdeki tarayıcı konsolu toplayıcısı (fetch + localStorage)
kullanılmayacağı için kaldırıldı.

### B — Ekşi'den açık izin / resmî erişim

Herkese açık API yok. Bugün uygulanamaz.

### C — Kısmi veri

JSON 200 sayfanın tamamını içermezse `tekillestir --ust 809 --alt 610`
eksik sayfaları listeler. Katalog başlığında "gelen sayfa / beklenen" ve
eksik sayfa numaraları yazılır; eksik sayfa gizlenmez.

### Reddedilen yol — Bulut ajanının Cloudflare'i aşarak çekmesi

robots.txt ve Content-Signal'e aykırı. Denendi, 43. sayfada durduruldu,
veri kullanılmadı ve silindi.

## Kabul ölçütleri ve durumları

| # | Ölçüt | Kanıt | Durum |
|---|---|---|---|
| K1 | Son sayfadan geriye 100 + 100 sayfa | `kapsam_raporu`: `809-710` / `709-610`, eksik sayfa listesi; işleme son sayfadan başlar | **Gerçek veriyle tamam:** 200/200 sayfa, eksik yok |
| K2 | Tekil, tam bağlantı; HTTPS yalnız doğrulanınca | `site_denetle`: HTTP sessizce yükseltilmez; tam adres anahtarı farklı yolları birleştirmez | Test edildi + gerçek ağda `http://example.com/` → HTTPS doğrulandı |
| K3 | Tek tam cümle + "Bu siteyle … yapabilirsin." | `aciklama_dogrula`, `uret` çıkış kodu | **Tamam:** 3220 kayıt, 0 kural ihlali; 844 kayıt bilinçli olarak açıklamasız |
| K4 | Liste, tablo değil; okunur boşluk | `⟶` ayracı, öğeler arası boş satır, `|` yok | Test edildi |
| K5 | Kategoriler | `■ KATEGORİ (n site)`, Türkçe büyük harf | Test edildi |
| K6 | Yalnız doğrulanmış çalışanlar "çalışan" | Ana liste yalnız `calisiyor`; korumalı/denetlenmemiş ayrı bölümde | Test edildi + gerçek ağda alternativeto.net (Cloudflare 403) çalışan sayılmadı |
| K7 | Güvenli ağ denetimi | localhost, özel/link-local IP, port, şema, kullanıcı bilgisi, yönlendirme, DNS yeniden çözümleme testleri | 18 test; gerçek ağda metadata IP ve localhost reddedildi |
| K8 | Kaynak sayfa izi | `kaynaklar[]`: page, page_url, observed_at, label; katalog satırında `(sayfa …)` | Test edildi |
| K9 | Masaüstü ve `C:\` kopyası | README'deki PowerShell komutu | **Bulutta yapılamaz** |

## Uygulama → test → teslim zinciri

```
Kullanıcı Chrome'u ──► sayfa JSON'ları (809..610)
        ▼
tekillestir ──► kontrol (url_denetim) ──► sablon ──► açıklamalar ──► uret ──► katalog.txt
        │ test: test/calistir.py → test/cikti/test-ciktisi.txt (SENTETİK etiketli)
        ▼
git push (claude/relaxed-hopper-of36hr) + özel ZIP (SHA-256, geri okuma)
```

## Referanslar

- Başlık: https://eksisozluk.com/az-kisinin-bildigi-muhtesem-web-siteleri--2764697
- Ekşi robots.txt (yapay zekâ ajanı yasakları, Content-Signal): https://eksisozluk.com/robots.txt
- Content Signals açıklaması (Cloudflare): https://contentsignals.org/
- curl_cffi belgeleri (TLS taklidi, Cloudflare SSS): https://curl-cffi.readthedocs.io/
- Topluluk Ekşi kazıyıcıları (karşılaştırma için incelendi, kullanılmadı):
  https://pypi.org/project/limoon/ , https://pypi.org/project/sourpy/ ,
  https://apify.com/epctex/eksisozluk-scraper
- OWASP SSRF önleme rehberi (izin listesi, DNS rebinding, yönlendirme): https://cheatsheetseries.owasp.org/cheatsheets/Server_Side_Request_Forgery_Prevention_Cheat_Sheet.html
- Python ipaddress (`is_global`): https://docs.python.org/3/library/ipaddress.html
- RFC 2606 (testte kullanılan `.example` alan adları): https://www.rfc-editor.org/info/rfc2606/

Hazır kazıyıcılar (limoon, sourpy, Apify) sunucudan çalışıp aynı robots.txt
ve Cloudflare sorununa takılır, ayrıca entry metnini de toplar. Bu yüzden
toplama kullanıcıya bırakıldı. Üretici bağımlılıksız (yalnız Python
standart kütüphanesi) yazıldı ve Windows'ta da kurulum gerektirmez.
