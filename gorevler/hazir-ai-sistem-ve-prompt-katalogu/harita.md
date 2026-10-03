# Bağlı iş haritası — Hazır AI sistem ve prompt kataloğu

Tarih: 2026-10-03 · Ortam: Claude Code bulut oturumu (Linux konteyner)

## 1. Hedef (kaynaktan)

Kullanıcı son düzeltmesinde şunu net söylüyor: *"insanlar nasıl uzun prompt yazıyor"* araştırması
**değil**; **hazır prompt / hazır sistem tarifi olan siteleri ve depoları** bulmak, oraya gidip
"bu güzelmiş, kullanayım" diyebileceği bir liste. Her alanda, birçok kategoride.

Kabul ölçütleri (görev özetinden):

| # | Ölçüt | Nasıl karşılanıyor | Kanıt dosyası |
|---|---|---|---|
| K1 | Teorik tavsiye değil, **hazır kurulmuş sistemler** | 36 gerçek site/depo; her biri canlı HTTP kontrolünden geçti, GitHub yıldızı API'den alındı | `dogrulama/link-raporu.md`, `veri/katalog.json` |
| K2 | Her örneğin **prompt'u ve kullanım yolu** | Her girdide kaynaktan birebir alınmış örnek prompt/kurulum komutu + adım adım kullanım; ayrıca 30 tam prompt'luk kopyala-yapıştır paketi | `KATALOG.md`, `hazir-promptlar.md`, `dogrulama/prompt-dogrulama.md` |
| K3 | **Kategori ve somut fayda** anlaşılır | 9 kategori, her girdide "ne işe yarar / kime göre / ücret / dikkat" alanları; HTML'de kategori filtresi | `index.html`, `KATALOG.md` |

## 2. Bağımlılıklar

```
[kaynak.txt + gorev.md]  (özel alan, depoya girmez)
        │
        ▼
[Web araştırması] ──► WebSearch (topluluk/forum) + WebFetch (resmi sayfa)
        │              + raw.githubusercontent.com (depo dosyaları birebir)
        │              + GitHub MCP arama (yıldız/güncellik)
        ▼
[veri/katalog.json]  ← tek doğruluk kaynağı (elle yazıldı, her alan kaynaklı)
        │
        ├──► araclar/olustur.py ──► KATALOG.md + index.html + hazir-promptlar.md
        │                              ▲
        │        veri/prompts_chat_secki.json (CSV'den birebir çekilen 30 prompt)
        ▼
[araclar/dogrula.py] ──► şema testi + canlı link testi + prompt birebirlik testi
        │
        ▼
[araclar/ekran-testi.mjs] ──► Chromium'da index.html gerçek görünüm + filtre/arama/kopyala akışı
        │
        ▼
[Kalıcı teslim] ──► (1) public dala push (yalnız araştırma çıktısı) + geri okuma
                    (2) özel ZIP (özel kaynaklar dahil) + SHA-256 + açıp geri okuma
```

Dış bağımlılık / engeller:
- **ChatGPT Pro ile birlikte araştırma (kaynak satır 3, 7, 11):** Bu bulut oturumunun kullanıcının
  ChatGPT hesabına erişimi yok ve olmamalı (kimlik bilgisi gerekir). Uydurulmadı. Yerine bağımsız
  araştırma yapıldı; ChatGPT Pro'ya sorulacak hazır bir doğrulama prompt'u `KATALOG.md` sonuna eklendi.
- **"Google ile" arama:** Ortamdaki arama aracı WebSearch'tür (Google API'si değil). Sonuçlar
  birincil kaynaklarda (resmi site, ham depo dosyası) ayrıca doğrulandı.
- **github.com HTML sayfaları** proxy'de 403 döndü → `raw.githubusercontent.com` + GitHub MCP
  arama ile aşıldı (yol değişikliği günlükte).
- **Giriş gerektiren galeriler** (Microsoft Copilot Prompt Gallery, Google AI Studio) içerik olarak
  açılamadı; resmi destek sayfalarından tarif edildi ve "giriş gerekir" diye işaretlendi.

## 3. A / B / C yolları

| Yol | Ne | Artı | Eksi | Karar |
|---|---|---|---|---|
| **A** | Statik katalog: `katalog.json` → tek dosyalık çevrimdışı `index.html` (arama, kategori filtresi, kopyala düğmesi) + `KATALOG.md` + kopyala-yapıştır prompt paketi | Sunucu yok, Windows/telefon tarayıcısında çift tıkla açılır, ZIP'le taşınır, kaynaklar tıklanabilir | Canlı güncellenmez (tarih damgalı anlık görüntü) | **SEÇİLDİ** |
| B | Google Sheets / Notion tablosu | Paylaşması kolay | Kullanıcının hesabına yazmak dış eylem, istenmedi; prompt metinleri tabloda okunaksız | Reddedildi |
| C | prompts.chat'i kendi sunucuna kurmak (self-host) | Tam özellikli site | Sunucu + veritabanı gerekir; kullanıcı "site göster" dedi, site kurmak hayali ürün olur | Reddedildi; katalogda "isteyen self-host edebilir" diye anlatıldı |

Yol kırılma planı: link testi bir siteyi ölü bulursa → girdiyi "erişilemedi" işaretle, aynı kategoride
alternatif öner (örn. PromptBase 403 → bot koruması; içerik WebSearch kaynaklarıyla tarif edildi).

## 4. Uygulama → test → teslim zinciri

1. `veri/katalog.json` yazıldı (36 girdi, 9 kategori).
2. `araclar/secki_cek.py` prompts.chat CSV anlık görüntüsünden 30 prompt'u **birebir** çeker.
3. `araclar/olustur.py` → `KATALOG.md`, `index.html`, `hazir-promptlar.md`.
4. `araclar/dogrula.py` → şema (her girdide kategori, fayda, örnek, kullanım, https) + canlı
   link durum kodları + seçki prompt'larının CSV ile birebir eşleşmesi.
5. `araclar/ekran-testi.mjs` → Chromium ekran görüntüsü (masaüstü + telefon genişliği),
   arama/filtre/kopyala akış testi.
6. Push + geri okuma; özel ZIP + SHA-256 + açıp karşılaştırma.

Sonuçlar ve gerçek çıktılar: `calisma-gunlugu.md` ve `dogrulama/`.
