# CLAUDE CODE FULL KURS 3+ SAAT: Kur ve Sat (2026) — burhan kocabıyık — 186 dk — https://www.youtube.com/watch?v=Xu2SIKz8B58
> Kaynak notu: Transkript yok (bulut IP engeli). Dayanak: açıklama + 33 bölümlük zaman damgalı içindekiler (`transkriptler/Xu2SIKz8B58.aciklama.txt`). Bölüm içi ayrıntılar bilinmiyor; çıkarımlar "(tahmin)". Güven: **orta**. Bu kurs, 2 saatlik kursun (qm0ZlYezt1Q) teknik kısmını ~50 dakikaya sıkıştırıp kalan ~2 saati **iş modelleri, müşteri bulma, vaka çalışmaları ve fiyatlandırmaya** ayırıyor — ajans için en değerli Türkçe kaynak.

## Tek paragraf
İlk 50 dakika Claude Code temelleri (kurulum, CLAUDE.md ve ilk proje, komutlar/plugin'ler, sıfırdan web uygulaması, Skills ve ajan sistemi, sub-agent kurma, MCP/connector'lar); sonrası tamamen "sat" tarafı: iş modelleri ve n8n, uygulama vs otomasyon, B2B/B2C satış, **UGC video otomasyonu**, müşteri vakası **e-ticaret reklam sistemi**, **ürün görseli hizmeti**, **sosyal medya içerik üretimi**, YouTube ve kaydırmalı (carousel) içerik sistemi, sıfırdan müşteri bulma (Apify ile lead çıkarma, Gmail bağlantısı ve lead toplama), **Routines ile GitHub ve buluta deploy**, Modal'a 7/24 deploy, müşteri projelerinde çalışma yöntemi, 120.000 müşteri segmentasyonu vakası, B2C UGC motoru, **fiyatlandırma (LTV/CAC)**, viral içerik bulma uygulaması, SaaS pazarı, en çok kullandığı yetenekler (skills), SEO ile müşteri bulma vakası, ajan sistemleri (GOAT, Hermes, Paper Clip), yol haritası.

## Önerdiği iş akışı (adım adım)
**A. Teknik temel (0:00–51:14)**: kurulum → neden önemli → CLAUDE.md + ilk proje → komutlar/plugin/bağlantılar → sıfırdan web uygulaması → Skills ve ajan sistemi → sub-agent'ları sıfırdan kurma → MCP ve connector'lar. (2 saatlik kursla aynı iskelet; bkz. o plan.)
**B. İş modeli ve hizmet hatları (51:14–1:40:52)**:
1. Uygulama mı otomasyon mu, B2B mi B2C mi karar ver (59:29).
2. Hizmet hatları, her biri Claude Code + n8n ile kurulmuş birer "sistem": UGC video otomasyonu (1:04:46), e-ticaret reklam sistemi (1:10:02, müşteri vakası), ürün görseli hizmeti (1:12:28), sosyal medya içerik üretimi (1:21:15), YouTube + carousel içerik sistemi (1:27:12).
**C. Müşteri bulma (1:40:52–2:02:06)**: Apify ile hedef işletme listesi çıkar → Gmail bağlantısı ile lead topla/gönder (tahmin: Claude Code + MCP/n8n ile kişiselleştirilmiş outreach).
**D. Operasyon (2:02:06–2:17:32)**: Routines ile GitHub + buluta deploy; Modal'da 7/24 çalışan ajanlar; müşteri projelerinde çalışma yöntemi (2:12:38); 120k müşteri segmentasyonu vakası.
**E. Ürün ve fiyat (2:17:32–2:37:54)**: B2C UGC motoru; fiyatlandırma stratejisi **LTV/CAC** (2:23:08); viral içerik bulma uygulaması; SaaS pazarı.
**F. Araç kutusu ve yol haritası (2:37:54–son)**: en çok kullandığı skill'ler; SEO ile müşteri bulma vakası; ajan sistemleri GOAT/Hermes/Paper Clip; sıfırdan başarıya yol haritası.

## Verdiği somut kurallar / prompt'lar / CLAUDE.md örnekleri (aynen)
- Bölüm başlıkları aynen (açıklamadan): "Routines: GitHub ve buluta deploy", "Modal.com'a 7/24 deploy", "Müşteri projelerinde çalışma yöntemi", "Fiyatlandırma stratejisi (LTV/CAC)", "En çok kullandığım yetenekler", "Ajan sistemleri (GOAT, Hermes, Paper Clip)".
- Prompt/skill metinleri transkript olmadan alınamadı; kursun dosya paketi: https://benburhan.gumroad.com/l/bsrxgi ve https://benburhan.gumroad.com/l/pjsiw.

## Araçlar ve linkler
- Claude Code (Skills, Sub-agents, MCP/connectors, Routines, Plugins), n8n, Apify (lead scraping), Gmail (MCP/connector), Modal.com (7/24 deploy), GitHub; ajan çerçeveleri: GOAT, Hermes, Paper Clip (hangi projeler olduğu doğrulanmalı)
- İlgili videoları: Skills rehberi (oGI1YmC2L00), %95'ini 30 dk'da (nXvWTR88sfU), 2 saatlik kurs (qm0ZlYezt1Q)

## Hatalar / "bunu yapma" uyarıları
- (tahmin, bölüm yapısından) Teknik yetkinliği satmadan önce iş modelini seçmemek; "uygulama vs otomasyon" ve "B2B vs B2C" kararını erteleme.
- Fiyatı maliyet üzerinden değil LTV/CAC üzerinden kurmama.
- Ayrıntılı uyarılar transkript gelince eklenecek.

## Bizim fabrikaya alınacaklar (somut)
1. **Hizmet hattı haritası** bu kursla birebir örtüşüyor: web/app (teknik bölüm) + **reklam sistemi** (1:10:02) + **ürün görseli** (1:12:28) + **sosyal medya içerik** (1:21:15) + **carousel/YouTube** (1:27:12) → `sistem/` altında her hat için ayrı skill zinciri; bu videonun zaman damgaları hatların "örnek vaka" referansı.
2. **Müşteri bulma hattı** (1:40:52–2:02:06): Apify ile yerel işletme listesi (restoran, klinik, saatçi) → Claude Code ile kişiselleştirilmiş teklif + demo site → Gmail connector ile gönderim; haftalık **routine** olarak çalıştır.
3. **Fiyatlandırma modeli**: paketleri LTV/CAC ile kur (kurulum ücreti + aylık bakım/reklam yönetimi); `sistem/fiyat/MODEL.md`'de hesap tablosu.
4. **Operasyon**: her müşteri projesi için Routines (deploy, sağlık kontrolü), uzun süreli otomasyonlar Modal'da — 2 saatlik kurs planıyla aynı deploy standardı.
5. **UGC/ürün görseli motoru**: müşteri ürün fotoğraflarından reklam görseli ve kısa video üretimi (fal.ai/Kling benzeri; web grubu planlarına bak) → reklam hattının besleyicisi.
6. **SEO ile müşteri bulma** (2:43:17): kendi ajans sitemiz için yerel SEO içerik routine'i (haftalık blog/landing).
7. Transkript veya dosya paketi gelince: "en çok kullandığı skill'ler" listesi (2:37:54) bizim skill kataloğuyla karşılaştırılacak.
