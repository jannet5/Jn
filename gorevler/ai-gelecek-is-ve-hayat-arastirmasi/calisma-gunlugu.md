# Çalışma Günlüğü

Tarih: 3 Ekim 2026 · Ortam: Claude Code, bulut konteyneri (Linux) · Dal: `claude/bold-bohr-so7g6t`

> Not: İstenen model/çaba ayarı görev metninde yazıyor; bu günlük o ayarın arayüzde
> gerçekten seçildiğini doğrulamaz.

## 1. Ne istendi?

Yapay zekânın meslekleri ve gündelik hayatı nasıl etkileyebileceğine dair, forum,
topluluk ve video kaynaklarına da dayanan, kitap gibi okunan Türkçe bir araştırma.
Zorunlu ölçütler: (1) forum/Medium/X/YouTube deneyimlerinin incelenmesi, (2) yazılım
ve fiziksel mesleklerin ayrı risklerle değerlendirilmesi, (3) tahminlerin kanıtlanmış
gelişmelerden ayrılması. Kullanıcının özel kaynak metni ve görev dosyası **public
depoya konmayacak**; özel çalışma alanında kalacak ve özel ZIP'e girecek.

## 2. Kaynakların okunması ve özel kaydı

- Kaynak metin ve görev dosyası baştan sona okundu.
- İkisi oturumun özel çalışma alanına (`scratchpad/ozel/`) yazıldı. Depoya kopyalanmadı.
- Kaynakta geçen Windows yolu bulut ortamından okunamaz; dosyalar sohbetle sağlandı.
- Kaynaktaki kişisel hipotez (fiziksel/tamir işlerinin değerleneceği) raporda kişisel
  ayrıntı olmadan, genel bir "sezgi" olarak test edildi (Bölüm 3).

## 3. Güncel web araştırması (resmî kaynaklar)

Araç: `WebSearch` (arama) + `WebFetch` (sayfa okuma).

| Konu | Sonuç |
|---|---|
| Stanford "Canaries" | Sayfa doğrudan okundu; 12.08.2026 güncellemesi, %19 açık |
| ILO 2025 | Doğrudan okundu |
| WEF 2025 | Sayfa 403 → arama özetleriyle, iki farklı WEF URL'si üzerinden |
| BLS elektrikçi | Doğrudan okundu (2025 verisi, 2025–35 projeksiyonu) |
| Anthropic Economic Index (Mart 2026) | Doğrudan okundu |
| METR (arXiv) | Doğrudan okundu |
| Yale Budget Lab | Fortune haberi doğrudan okundu |
| TÜİK Temmuz 2026 | Haber doğrudan okundu (31.08.2026) |
| İŞKUR 2024 | Haber doğrudan okundu |
| MYK belge zorunluluğu, Huang, Amodei, Hinton, Waymo, robotlar, Klarna | Arama özetleri |
| Türkiye usta maaşları | Haber açıldı; rakamların kaynağı **yok** → "doğrulanmamış" etiketi |

## 4. Topluluk araştırması

| Deneme | Komut/araç | Sonuç |
|---|---|---|
| Reddit JSON | `curl https://www.reddit.com/r/electricians/search.json…` | HTML engel sayfası → **kırıldı** |
| Ekşi Sözlük, Technopat | `WebFetch` | 403 → **kırıldı** |
| Hacker News | `curl https://hn.algolia.com/api/v1/search…` + Python betiği | 200 OK; 9 tartışma, ~200 yorum indirildi (özel alanda ham hâli) |
| HeatingHelp usta forumu | `WebFetch` | Okundu |
| Medium | `WebFetch` 403 → `curl https://medium.com/feed/@a_siamtanis` | RSS'ten tam metin okundu |
| X | Doğrudan erişim yok → viral tartışmayı aktaran Business Today haberi | Okundu |
| Instagram | Doğrulanabilir içerik bulunamadı | **Eksik** olarak raporlandı |

## 5. YouTube

1. `pip install youtube-transcript-api yt-dlp`
2. `yt-dlp --flat-playlist "ytsearch6:<sorgu>"` ile 7 sorguda (Türkçe + İngilizce) gerçek
   arama → 16 video seçildi.
3. **Yol A:** `youtube-transcript-api` → 16/16 `IpBlocked`.
4. **Yol B:** `yt-dlp --write-auto-subs` → `HTTP 429`, ardından "Sign in to confirm you're not a bot".
5. **Yol C:** youtubetotranscript.com (403), youtube-transcript.io (içerik yok),
   Invidious/Piped aynaları (bot doğrulaması) → hepsi kırıldı.
6. **Karar:** Video içerikleri uydurulmadı. Videolardaki ana figürlerin sözleri yazılı
   kaynaklarla doğrulandı. Ev bağlantısında çalıştırılacak `dogrulama/transkript_al.py`
   hazırlandı.

## 6. Kararlar

- **Teslim biçimi Markdown araştırma paketi:** Görev bir araştırma; uygulama veya sunucu
  gereksizdi.
- **Beş sınıflı etiket sistemi** (KANIT / ERKEN SİNYAL / TAHMİN / TOPLULUK / DOĞRULANMAMIŞ):
  "Tahminleri kanıttan ayır" ölçütünü ölçülebilir hale getirmek için.
- **Çıkar çatışması notları:** Huang (veri merkezi inşasından kazanan şirket) ve Amodei
  (bu araştırmayı yapan modelin şirketi) sözleri diğer tahminlerle aynı etikette tutuldu.
- **Abartılı rakamlar** (300 bin $ elektrikçi) raporda resmî medyanla yan yana verildi.
- Ham HN yorum dökümleri depoya konmadı (gereksiz büyüklük); bağlantıları kaynak
  listesinde, ham hâlleri özel ZIP'te.

## 7. Doğrulama sonuçları (gerçek çıktılar)

1. **Kabul betiği:** `python3 dogrulama/kontrol.py` → 130 kontrol, `SONUC: TUMU GECTI`.
2. **Negatif test (betik gerçekten hata yakalıyor mu?):** Paketin bir kopyasına bilerek
   6 hata eklendi (geçersiz sınıf, olmayan kaynak kodu, `http://` bağlantı, özel metinden
   bir ifade, `kaynak.txt` dosyası). Betik 6/6'sını yakaladı: `SONUC: 6 KONTROL KALDI`.
   Kopya sonra silindi.
3. **Push:** `git push -u origin claude/bold-bohr-so7g6t` → başarılı (commit `da8564b`).
4. **Geri okuma:** Uzak dal temiz bir klasöre `git clone --depth 1` ile indirildi; 10
   dosyanın SHA-256 değerleri yereldekilerle `diff` ile karşılaştırıldı → **birebir aynı**.
   Klonda kabul betiği yeniden çalıştırıldı → `TUMU GECTI`.
5. **Mevcut projelere dokunulmadı:** `git diff --stat f489a09 HEAD` yalnızca bu klasördeki
   10 dosyayı gösterdi.
6. **Özel ZIP:** Özel kaynak, görev dosyası, ham HN yorumları, Medium RSS metni ve
   deponun git bundle'ı özel çalışma alanında ZIP'lendi; SHA-256 değeri ayrı bir
   `.sha256` dosyasına yazıldı, ZIP açılıp içerik hash'leri ve bundle (bundle'dan klonlanıp HEAD uzak dalla karşılaştırılarak)
   geri okunarak doğrulandı. ZIP public depoya **konmadı**.

## 8. Yapılamayanlar (açıkça)

- YouTube transkriptleri (IP engeli; 3 yol denendi).
- Instagram; X'in doğrudan okunması; Reddit, Ekşi Sözlük, Technopat (403/giriş).
- Türkiye için yapay zekâ–istihdam ilişkisini ölçen bordro düzeyinde veri bulunamadı.
- Windows, telefon veya kullanıcı hesabı testi gerekmedi; yapılmadı.
