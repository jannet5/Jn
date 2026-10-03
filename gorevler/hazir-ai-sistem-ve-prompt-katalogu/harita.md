# Bağlı iş haritası — Hazır AI sistem ve prompt kataloğu

Tarih: 2026-10-03 · Ortam: Claude Code bulut oturumu (Linux konteyner) · Sürüm 2 (lisans katmanı)

## 1. Hedef (kaynaktan)

Kullanıcı son düzeltmesinde şunu net söylüyor: *"insanlar nasıl uzun prompt yazıyor"* araştırması
**değil**; **hazır prompt / hazır sistem tarifi olan siteleri ve depoları** bulmak, oraya gidip
"bu güzelmiş, kullanayım" diyebileceği bir liste. Her alanda, birçok kategoride.

Kabul ölçütleri:

| # | Ölçüt | Nasıl karşılanıyor | Kanıt dosyası |
|---|---|---|---|
| K1 | Teorik tavsiye değil, **hazır kurulmuş sistemler** | 36 gerçek site/depo; canlı HTTP kontrolü, GitHub yıldızı; 20 depo commit'e sabitlendi | `dogrulama/link-raporu.md`, `veri/katalog.json` |
| K2 | Her örneğin **prompt'u ve kullanım yolu** | Her girdide örnek + adım adım kullanım; 30 tam prompt'luk kopyala-yapıştır paketi | `KATALOG.md`, `hazir-promptlar.md`, `dogrulama/prompt-dogrulama.md` |
| K3 | **Kategori ve somut fayda** anlaşılır | 9 kategori; her girdide ne işe yarar / kime göre / ücret / dikkat; HTML'de kategori filtresi | `index.html`, `KATALOG.md` |
| K4 | **Lisans katmanı** (inceleme düzeltmesi) | Birebir metin yalnız CC0-1.0/MIT kaynaktan; her kopyada kaynak + commit'e sabit tam dosya permalinki + commit/tarih + sahip + lisans + yeniden dağıtım sınırı; MIT tam metin + telif bildirimi, CC0 dayanak belgesi pakette; belirsiz/tescilli kaynaklarda metin yok, özgün açıklama + link; filtre ve kopyalama bu sınırı uyguluyor | `LISANS-ENVANTERI.md`, `NOTICE.md`, `lisanslar/`, `dogrulama/lisans-dogrulama.md`, `dogrulama/negatif-test.txt`, `dogrulama/tarayici-testi.md` |

## 2. Bağımlılıklar

```
[kaynak.txt + gorev.md]  (özel alan; depoya ve ürün ZIP'ine girmez)
        │
        ▼
[Web araştırması] ──► WebSearch (topluluk/forum; Google arayüzü DEĞİL) + WebFetch (resmi sayfa)
        │              + raw.githubusercontent.com (depo dosyaları birebir) + GitHub MCP arama
        ▼
[Lisans tespiti] ──► git ls-remote (20 depo HEAD commit'i) → raw/<commit>/LICENSE indir
        │             → shallow fetch ile commit tarihi → anlık görüntüleri sabit commit'le SHA karşılaştır
        ▼
[veri/katalog.json]  ← tek doğruluk kaynağı: kaynak_depolar (commit/tarih/lisans/sahip),
        │               her girdide icerik_lisansi + ornek.kopya (birebir lisans kaydı | özgün + neden)
        │
        ├── araclar/secki_cek.py ──► sabit commit CSV (SHA kontrollü) → veri/prompts_chat_secki.json
        │                             (30 prompt, her birinde lisans_kaydi)
        ├── araclar/olustur.py ──► KATALOG.md · hazir-promptlar.md · index.html · LISANS-ENVANTERI.md · NOTICE.md
        ▼
[araclar/dogrula.py] ──► şema + lisans kuralları + yasak metin taraması + canlı link + birebirlik (sabit commit)
[araclar/negatif_test.py] ──► geçici kopyaya 4 ihlal koy → denetimin 4'ünü de yakaladığını kanıtla
[araclar/ekran-testi.mjs] ──► Chromium: filtre/arama/kopyala + lisans sınırı akışı + ekran görüntüleri
        │
        ▼
[Kalıcı teslim] ──► (1) public dala push + uzak tam commit geri okuma
                    (2) temiz ürün-köklü ZIP (yalnız ürün; özel dosya, ham sohbet, depo geçmişi YOK) + SHA-256 + açıp karşılaştırma
```

Dış bağımlılık / açık engeller (kapanmadı, açıkça korunuyor):
- **ChatGPT Pro ile ortak araştırma yapılmadı** (kaynak satır 3, 7, 11): oturumun kullanıcının ChatGPT
  hesabına erişimi yok; uydurulmadı. Hazır doğrulama prompt'u `KATALOG.md` sonunda.
- **Gerçek Google arayüzü araştırması yapılmadı:** ortamın WebSearch aracı ve doğrudan sayfa/dosya
  indirme kullanıldı.
- **Bot korumasına takılan 4 adres** (promptbase.com, flowgpt.com, cursor.directory, bir Capterra sayfası):
  içerikleri doğrulanamadı; normal tarayıcıda açıldıkları test edilmedi, garanti verilmez.
- **github.com HTML sayfaları** proxy'de 403 → `raw.githubusercontent.com` + `git ls-remote` ile aşıldı.
- **Giriş gerektiren galeriler** (Microsoft Copilot Prompt Gallery, LangSmith Hub, GPT Mağazası) içerik olarak
  açılamadı; resmi destek/belge sayfalarından tarif edildi.
- Lisans değerlendirmesi hukuki görüş değildir; prompts.chat CC0 beyanı katkıcı bazında doğrulanmadı.

## 3. A / B / C yolları

### 3a. Ürün biçimi

| Yol | Ne | Artı | Eksi | Karar |
|---|---|---|---|---|
| **A** | Statik katalog: `katalog.json` → tek dosyalık çevrimdışı `index.html` + `KATALOG.md` + prompt paketi | Sunucu yok, çift tıkla açılır, ZIP'le taşınır | Canlı güncellenmez (tarihli anlık görüntü) | **SEÇİLDİ** |
| B | Google Sheets / Notion tablosu | Paylaşması kolay | Kullanıcının hesabına yazmak dış eylem, istenmedi | Reddedildi |
| C | prompts.chat'i self-host etmek | Tam özellikli site | Sunucu gerekir; site kurmak hayali ürün olur | Reddedildi |

### 3b. Lisans katmanı (sürüm 2)

| Yol | Ne | Artı | Eksi | Karar |
|---|---|---|---|---|
| **A** | Metni yalnız CC0/MIT kaynaktan birebir al; her kopyaya commit'e sabit lisans kaydı; diğerlerinde özgün açıklama + link; kopyalama/filtre bunu uygulasın | Yeniden dağıtılan her metnin dayanağı belgeli; belirsiz metin pakette yok | Bazı kaynaklarda "gerçek örnek" görülemez, linke gitmek gerekir | **SEÇİLDİ** |
| B | Tüm örnekleri kısa alıntı olarak bırakıp "alıntı hakkı" varsaymak | Örnekler zengin kalır | Ülkeye göre değişen hukuki varsayım; inceleme bunu reddetti | Reddedildi |
| C | Hiç birebir metin koymamak, yalnız link | En düşük risk | Kullanıcının "kopyala-kullan" ihtiyacını karşılamaz (30 CC0 prompt kaybolur) | Reddedildi |

Yol kırılma planı: bir kaynağın lisansı değişir veya sabit commit'teki dosya beklenen SHA'yı vermezse
`dogrula.py` / `secki_cek.py` hata verir → girdi özgün açıklamaya çevrilir.

## 4. Uygulama → test → teslim zinciri (sürüm 2'de yapılanlar)

1. 20 deponun HEAD commit'i (`git ls-remote`) ve tarihi (shallow fetch) alındı; LICENSE dosyaları o
   commit'ten indirildi. Pakette tutulan 4 tam dosya + prompts.csv'nin sabit commit'teki SHA'yla aynı olduğu doğrulandı.
2. Lisansa göre sınıflandırma: açık 11 · kısıtlı 4 · tescilli/üçüncü taraf 7 · bilinmiyor 14.
3. Lisansı belirsiz/tescilli 9 kaynaktaki birebir örnek **çıkarıldı**, özgün açıklama yazıldı
   (Cursor prompt'u, OpenAI/Google/LangChain örnekleri, GPL'li README'ler, lisanssız depo cümlesi, Anthropic README şablonu).
4. prompts.chat tam CSV üründen çıkarıldı; seçki sabit commit'ten SHA kontrolüyle çekiliyor.
5. `lisanslar/` (12 belge), `NOTICE.md` (MIT telif + izin metinleri), `LISANS-ENVANTERI.md` üretildi.
6. HTML: lisans filtresi; kopyala düğmesi yalnız CC0/MIT birebir örneklerde; MIT kopyasına telif +
   izin metni + commit otomatik ekleniyor; tescilli kartlarda "kopyalanmaz — kaynağa git".
7. Testler: `dogrula.py` (lisans kuralları + 8 yasak metin × 8 dosya), `negatif_test.py`, `ekran-testi.mjs` (20 kontrol).
8. Push + uzak tam commit geri okuma; temiz ürün-köklü ZIP + SHA-256 + açıp diff.

Sonuçlar ve gerçek çıktılar: `calisma-gunlugu.md` ve `dogrulama/`.
