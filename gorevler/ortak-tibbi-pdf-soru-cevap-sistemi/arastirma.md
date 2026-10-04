# Araştırma notları (2026-10-03)

Araştırma web aramasıyla yapıldı. **[A]** işaretli sayfalar açılıp okundu. **[G]** işaretli olanlar yalnız arama sonucunda görüldü; bunlara doğrulanmamış bilgi olarak bakın. Reddit arama aracında engelli olduğu için topluluk kaynağı olarak Hacker News ve Trustpilot kullanıldı.

## 1. Benzer ürünler — "bunu ilk biz mi yapıyoruz?"

| Ürün | Ne yapıyor | Sınır / sürtünme | Kaynak |
|---|---|---|---|
| ChatPDF | Tek kişilik PDF sohbeti | Ücretsiz: günde ~2 PDF / 50 soru. Hesap gerekiyor. Reddedilen yükleme de kotadan düşüyor. | https://elephas.app/blog/chatpdf-review.md [G], https://de.trustpilot.com/review/chatpdf.com [A] |
| NotebookLM | Kişisel defter (notebook) | Günde 50 soru, Google hesabı şart | https://elephas.app/blog/notebooklm-daily-limit [G] |
| Humata | PDF soru-cevap | Ücretsiz: ayda ~10 yanıt | https://humata.ai/pricing [G] |
| AMBOSS AI | Tıp içeriği + YZ | Yalnız ücretli üyelikte | https://support.amboss.com/hc/en-us/articles/32345265867921-AMBOSS-Pricing [G] |
| OpenEvidence | Klinik kanıt araması, kaynak gösteriyor | Hesap / klinisyen doğrulaması gerekiyor | https://www.iatrox.com/blog/openevidence-for-medical-students-is-it-enough [G] |
| YouLearn, Scholarly, RemNote vb. | Öğrenci yükleme araçları | Hepsi freemium ve hepsi hesap istiyor | https://www.youlearn.ai/blogs/best-ai-tools-medical-students-2026 [G] |

**Sonuç:** Taramada bulunan araçların hepsi kişisel kütüphane mantığıyla çalışıyor: her kullanıcı kendi PDF'ini yüklüyor ve hesap açması gerekiyor. Herkesin katkı yaptığı, girişsiz ve diller arası sorgulanabilen ortak bir kütüphaneye rastlanmadı. Kaynak sohbetteki "giriş/çıkışla uğraştırıyorlar" şikâyeti bu tabloyla örtüşüyor.

## 2. Topluluk / kullanıcı deneyimi

- Ücretli kullanıcılar bile tekrar tekrar limit mesajına takılmaktan şikâyet ediyor: https://news.ycombinator.com/item?id=49898038 [A]
- "5 saatlik limit projemi sprint ortasında durdurdu": https://news.ycombinator.com/item?id=49432879 [G]. Bu, kaynaktaki "işin ortasında pat diye kesilmesin" isteğinin genel bir sorun olduğunu gösteriyor.
- Limitlerin daraltılmasına tepki: https://news.ycombinator.com/item?id=38307209 [G]
- Girişsiz ve ücretsiz PDF sohbeti (AskYourPDF) olumlu karşılanmış: https://news.ycombinator.com/item?id=36179038 [G]
- Kaynağa dayanan sistemler, düz sohbet botlarına göre çok daha az uydurma kaynak üretiyor: https://medinform.jmir.org/2024/1/e54345/PDF [G]. Bu yüzden her yanıtta sayfa numaralı kaynak gösteriliyor.

## 3. Resmî teknik kaynaklar

- **PyMuPDF metin çıkarma:** `get_text(sort=True)` metni okuma sırasına yakın verir. Taranmış PDF'ler için ayrıca OCR gerekir. https://pymupdf.readthedocs.io/en/latest/recipes-text.html [A]
- **fastembed destekli modeller:** `paraphrase-multilingual-MiniLM-L12-v2` (384 boyut) listede var. `multilingual-e5-small` resmî listede yok, `e5-large` ise yaklaşık 2 GB. https://qdrant.github.io/fastembed/examples/Supported_Models/ [A]
- **multilingual-e5:** `query:` / `passage:` önekleri zorunlu. Seçtiğimiz MiniLM modeli önek istemediği için bu risk ortadan kalkıyor. https://huggingface.co/intfloat/multilingual-e5-large [A]
- **FastAPI dosya yükleme (UploadFile):** https://fastapi.tiangolo.com/tutorial/request-files/ [G]
- **PWA kurulabilirlik şartları:** manifest, 192 ve 512 piksellik ikon, `start_url`, `display` ve HTTPS. https://developer.mozilla.org/en-US/docs/Web/Progressive_web_apps/Guides/Making_PWAs_installable [A]

## 4. Hukuk ve tıbbi sorumluluk

- **DMCA §512 bildir-kaldır süreci, karşı bildirim, atanmış temsilci kaydı ve tekrar ihlal politikası:** https://www.copyright.gov/512/ [A]. Ürünlenen kısım şu: yüklemede hak beyanı zorunlu, "Bildir" düğmesi var ve telif bildirimi gelince belge hemen gizleniyor. **Kurucunun yapması gereken (yazılımla çözülemez):** gerçek bir temsilci atanıp https://www.copyright.gov/dmca-directory adresinde kaydedilmesi.
- **Sağlık bilgisinde açık ve dikkat çekici uyarı (FTC):** https://www.ftc.gov/news-events/news/press-releases/2016/04/ftc-releases-new-guidance-developers-mobile-health-apps [G]. Ürünlenen kısım: "Eğitim amaçlıdır" uyarısı ve her yanıtta sayfa kaynağı.

## 5. Kota arayüzü, bağış ve faturalama

- Sayaç kullanılan şeyin yanında durmalı, yenilenme tarihi gösterilmeli ve uyarılar %75–90 civarında başlamalı: https://www.saasui.design/blog/saas-usage-quota-limits-ux-patterns [A]
- Önce yumuşak limit (uyarı), sonra kademeli sürtünme; kullanıcı "pusuya düşürülmüş" hissetmemeli: https://zuplo.com/blog/progressive-friction-for-monetized-apis [G], https://dodopayments.com/blogs/openai-usage-limits-customer-quotas [G]
- **Bağış platformları:** GitHub Sponsors (kişisel hesapta %0 kesinti), Open Collective (harcamalar şeffaf, ev sahibi kuruluş ücreti alabilir), Ko-fi. https://www.codenote.net/en/posts/github-sponsors-vs-open-collective-fees-comparison/ [G]
- **Stripe kullanım bazlı faturalama (meter events):** müşteri nesnesi şart, yani kart ve hesap bilgisi gerekiyor. Bu yüzden ücretsiz katmana uymuyor; yalnız ileride isteğe bağlı ücretli katman için düşünülebilir. https://docs.stripe.com/billing/subscriptions/usage-based/meters/configure [G]

## 6. Tur 2 (2026-10-04): Türkçe PDF'ten İspanyolca cevap, anahtar olmadan

İşaretler: **[A]** = açılıp okundu, **[G]** = yalnız arama sonucunda görüldü. Bu turda modeller bulutta gerçekten indirilip denendi; denemelerin sonuçları "Bu bulutta ölçülen" başlığı altında.

| Seçenek | Lisans / maliyet | Bu bulutta ölçülen | Karar |
|---|---|---|---|
| Anthropic Messages API (Sonnet 5 / 5.5 $2/$10, Haiku 4.5 $1/$5 / MTok) https://platform.claude.com/docs/en/about-claude/pricing [A] | Ücretli anahtar gerekiyor | Anahtar yok, denenmedi | İsteğe bağlı kip olarak kaldı |
| **opus-mt-tc-bible-big-trk-deu_eng_fra_por_spa** https://huggingface.co/Helsinki-NLP/opus-mt-tc-bible-big-trk-deu_eng_fra_por_spa [A] | Apache-2.0, ~0,2B parametre, int8 hâlinde 237 MB | 10 cümle 1,7 sn (4 çekirdek). En doğal çeviri. Ama "yüzde 7'nin altı" → "seis por ciento" hatası yaptı. | **Birincil**: normalizasyon + n-best + denetimle |
| opus-mt-tr-es https://huggingface.co/Helsinki-NLP/opus-mt-tr-es [A] | Apache-2.0, int8 hâlinde 76 MB, Tatoeba BLEU 56,3 | 1,0 sn. "eGFR" → "electroencefalograma", "altında" düştü, "°C" → "oC". | İkincil aday |
| M2M100-418M https://huggingface.co/facebook/m2m100_418M [A] | MIT, int8 hâlinde 473 MB | 3,1 sn. Aynı "seis por ciento" hatası, "çiğnetilir" → "se derrite". | Elendi |
| NLLB-200-distilled-600M https://huggingface.co/facebook/nllb-200-distilled-600M [A] | **CC-BY-NC**; model kartı "tıp alanı için değil" diyor | Lisans yüzünden denenmedi | Elendi |
| Argos Translate https://github.com/argosopentech/argos-translate [A], paket listesi https://raw.githubusercontent.com/argosopentech/argospm-index/main/index.json [A] | MIT/CC0 | Listede doğrudan tr→es yok; tr→en→es pivotu zorunlu | Elendi (iki aşamalı hata) |
| LibreTranslate https://github.com/LibreTranslate/LibreTranslate [A] | AGPL-3.0, Argos tabanlı | — | Elendi (aynı pivot + ek servis) |
| 1–3B yerel LLM (Qwen2.5-3B, llama.cpp) https://arxiv.org/pdf/2508.16431 [G] | CPU'da ~8–12 token/sn [G] | — | Elendi: tıpta serbest üretim uydurma riski taşıyor |
| CTranslate2 https://opennmt.net/CTranslate2/guides/transformers.html [A] | MIT; OPUS-MT int8, 4 iş parçacığıyla ~700 token/sn (resmî ölçüm) | Dönüştürme ve çalıştırma başarılı | Çalışma motoru |

**Neden denetim şart (kaynaklar):**
- Makine çevirisi sistemleri sayıları sık bozuyor ve bu tıbbi yanlış bilgiye yol açabiliyor: https://arxiv.org/abs/2107.08357 [A]
- Olumsuzluk en çok düşürülerek bozuluyor; yüksek kaynaklı dil çiftlerinde bile doğruluk %91,7–95,7: https://arxiv.org/abs/2107.12203 [A]
- Türkçenin eklemeli yapısı çeviri kalitesini sınırlıyor: https://www.ncbi.nlm.nih.gov/pmc/articles/PMC12453858/ [G]
- Topluluk tarafında, LibreTranslate bakımcıları kötü çeviri bildirimlerinin çoğunun Argos modellerinden kaynaklandığını söylüyor: https://community.libretranslate.com/t/github-issue-related-to-bad-translations/1101 [A]
