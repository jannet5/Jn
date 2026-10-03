# Hazır AI Sistem ve Prompt Kataloğu

Anlık görüntü tarihi: **2026-10-03** · 36 kaynak · 9 kategori

Gidip bakıp 'bu güzelmiş, kullanayım' diyebileceğin hazır prompt siteleri, hazır ajan/sistem paketleri, otomasyon şablonları ve gerçek ürünlerin sistem promptları. Metni yalnız açık lisanslı (CC0/MIT) kaynaklardan, commit'e sabitlenmiş kaynak ve lisans bilgisiyle birebir aldık; lisansı belirsiz veya tescilli kaynaklar için kendi açıklamamızı ve linki verdik.

> Hızlı başlangıç: Sadece kopyala-yapıştır prompt istiyorsan → **prompts.chat** ve bu paketteki `hazir-promptlar.md`. Hazır *sistem* (çok adımlı, rolleri yazılmış) istiyorsan → **Fabric**, **The Agency**, **Superpowers**. Otomasyon istiyorsan → **n8n şablonları**. Profesyonellerin uzun prompt'larının *yapısını* görmek istiyorsan → **system-prompts-and-models-of-ai-tools** (yalnız inceleme; metinleri tescillidir).

> Lisans özeti: her girdide **İçerik lisansı** satırı var. Tam lisans envanteri `LISANS-ENVANTERI.md`, üçüncü taraf telif bildirimleri `NOTICE.md`, lisans belgelerinin kopyaları `lisanslar/` klasöründe.

## İçindekiler

- [Her alan: hazır prompt kütüphaneleri](#genel) — 7 kaynak
- [İş ve ofis: resmi prompt paketleri](#ofis) — 4 kaynak
- [İçerik işleme ve kişisel üretkenlik sistemi](#icerik) — 1 kaynak
- [Yazılım ve iş: hazır ajan ekipleri / iş akışı sistemleri](#ajan) — 6 kaynak
- [Editör kuralları ve skill koleksiyonları](#kural) — 4 kaynak
- [Gerçek ürünlerin sistem promptları](#sistem) — 3 kaynak
- [Otomasyon: hazır iş akışı şablonları](#otomasyon) — 4 kaynak
- [Görsel ve video prompt galerileri](#gorsel) — 3 kaynak
- [Öğrenme: resmi prompt rehberleri ve prompt hub'ları](#rehber) — 4 kaynak

## Hangisiyle başlamalıyım?

| İhtiyacın | Git | Neden | İçerik lisansı |
|---|---|---|---|
| Her konuda hazır, kopyala-yapıştır prompt | [prompts.chat](https://prompts.chat) | Ücretsiz, 2.169 prompt | CC0-1.0 (depo beyanı) |
| Ofiste e-posta/toplantı/rapor | [OpenAI Academy paketleri](https://academy.openai.com/public/clubs/work-users-ynjqu/resources/chatgpt-for-any-role) | Resmi, kısa, şablonlu | Bilinmiyor |
| Video/makaleden not, özet, fikir | [Fabric](https://github.com/danielmiessler/Fabric) | Tek işi iyi yapan 250+ uzun sistem prompt'u | MIT |
| Tek kişilik ajans (pazarlama, satış, kod) | [The Agency](https://github.com/msitarzewski/agency-agents) | Rolleri ve teslimatları yazılmış ajanlar | MIT |
| AI ile uygulama yazdırmak | [Superpowers](https://github.com/obra/superpowers) / [Spec Kit](https://github.com/github/spec-kit) | Plan→test→kod disiplini | MIT |
| Tekrarlayan işi otomatiğe bağlamak | [n8n şablonları](https://n8n.io/workflows/) | 12.895 hazır iş akışı | Bilinmiyor |
| Görsel üretim fikri | [PromptHero](https://prompthero.com) | Görselin prompt'unu gör | Bilinmiyor |
| Kendi uzun sistem prompt'umu yazmak | [Ürün sistem prompt'ları](https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools) (yalnız yapıyı incele) + `hazir-promptlar.md` ana şablon | Profesyonel iskeleti gör | Tescilli / üçüncü taraf |

<a id="genel"></a>

## Her alan: hazır prompt kütüphaneleri

Kopyala-yapıştır prompt siteleri ve hazır sohbet botu mağazaları. Yazı, iş, eğitim, sağlık, hobi... her konu.

### prompts.chat (eski adı Awesome ChatGPT Prompts)

**Link:** https://prompts.chat  
**Tür:** Ücretsiz prompt kütüphanesi + açık kaynak depo  
**Durum:** ⭐ 171.897 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — Prompt'lar CC0-1.0 (depo beyanı), site kodu MIT.

**Ne işe yarar:** Dünyanın en büyük açık prompt kütüphanesi. 'Act as ...' (…gibi davran) rol prompt'ları: çevirmen, mülakatçı, kariyer danışmanı, Linux terminali, diyetisyen, Excel tablosu vb. Ayrıca görsel, video, Skill ve Workflow türleri var.

**Kime göre:** Herkes; özellikle 'şu iş için hazır bir prompt var mı?' diye bakanlar.

**Örnek — Job Interviewer (iş mülakatçısı) — birebir alıntı (CC0):**

```text
I want you to act as an interviewer. I will be the candidate and you will ask me the interview questions for the ${Position:Software Developer} position. I want you to only reply as the interviewer. ...
```
Bu alıntının lisans kaydı:

- Kaynak depo: `f/prompts.chat`
- Tam dosya (commit'e sabit): https://github.com/f/prompts.chat/blob/7d3f248962d1dca209d59e033524bcb86c2b26b8/prompts.csv
- Commit: `7d3f248962d1dca209d59e033524bcb86c2b26b8` (2026-10-01T12:56:12+03:00) · erişim: 2026-10-03
- Sahip: prompts.chat katkıcıları (Fatih Kadir Akın ve topluluk)
- Lisans: **CC0-1.0** · belge: `lisanslar/prompts.chat_LICENSE`, `lisanslar/prompts.chat_LICENSE-CC0`
- Yeniden dağıtım: CC0-1.0 beyanı: telif hakkından feragat; atıf zorunlu değil. Dayanak depo beyanıdır; katkıcı bazında üçüncü taraf hak kontrolü yapılmadı.

**Nasıl kullanılır:**

1. https://prompts.chat adresine gir, üstteki aramaya konu yaz (ör. 'interview', 'translator', 'diet').
2. Prompt kartında Kopyala'ya bas; ${Position:Software Developer} gibi alanları kendi bilginle değiştir.
3. ChatGPT / Claude / Gemini sohbetine yapıştır; Türkçe yanıt istiyorsan sonuna 'Yanıtlarını Türkçe ver.' ekle.
4. Toplu kullanmak istersen CSV'yi indir veya Hugging Face veri setini kullan; şirket içi kullanım için depo self-host edilebilir.

**Ücret:** Ücretsiz.  
**Topluluk / not:** GitHub'ın en çok yıldızlı prompt deposu; 2.169 prompt (3 Ekim 2026 CSV anlık görüntüsü: 1.836 metin, 312 yapılandırılmış, 21 görsel). (https://github.com/f/prompts.chat)  
**Dikkat:** Prompt'lar topluluk yazımı; kalite değişken. En iyi sonuç için kendi bağlamını (kim olduğun, hedefin) ekle.

Ek linkler: https://huggingface.co/datasets/fka/prompts.chat · https://github.com/f/prompts.chat

### ChatGPT GPT Mağazası (Explore GPTs)

**Link:** https://chatgpt.com/gpts  
**Tür:** Hazır özel sohbet botları (Custom GPT)  
**Durum:** HTTP 200 (giriş ekranı) · kontrol 2026-10-03  
**İçerik lisansı:** Tescilli / üçüncü taraf — OpenAI hizmet koşulları + her GPT'nin sahibi; GPT talimatlarının yeniden dağıtım izni yok.

**Ne işe yarar:** Başkalarının sistem prompt'u + dosya + araç ile kurup yayımladığı hazır GPT'ler: yazı, verimlilik, araştırma, programlama, eğitim, yaşam tarzı. Prompt yazmadan doğrudan kullanılır.

**Kime göre:** ChatGPT kullananlar; 'prompt'la uğraşmadan hazır asistan' isteyenler.

**Kullanım örneği (prompt mağazada gizli; botu açıp doğrudan yazarsın)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
GPT'yi aç → 'Bu PDF'teki sözleşmenin riskli maddelerini maddeler halinde çıkar' gibi doğrudan isteğini yaz.
```
Neden kopyalanmadı: OpenAI hizmet koşulları + her GPT'nin sahibi; GPT talimatlarının yeniden dağıtım izni yok. · Kaynak: https://chatgpt.com/gpts

**Nasıl kullanılır:**

1. chatgpt.com/gpts adresine ChatGPT hesabınla gir.
2. Kategoriden veya aramadan bir GPT seç, açıklamasını ve puanını oku.
3. Sohbeti başlat; beğenirsen kenar çubuğuna sabitle.
4. Kendi GPT'ni kurmak için 'Create' ile sistem prompt'unu (Instructions) yaz — bu katalogdaki örnekler başlangıç olabilir.

**Ücret:** ChatGPT hesabı gerekir; kullanım limitleri plana göre değişir.  
**Topluluk / not:** Popüler GPT'lerin sistem prompt'larını toplayan ayrı bir depo var (ai-boost/awesome-prompts). (https://github.com/ai-boost/awesome-prompts)  
**Dikkat:** Üçüncü taraf GPT'lere hassas veri yükleme; geliştiricisi sohbet içeriğini göremese de eklentili GPT'ler dış servislere veri gönderebilir.

### ai-boost/awesome-prompts — en iyi GPT'lerin prompt'ları

**Link:** https://github.com/ai-boost/awesome-prompts  
**Tür:** Açık depo: GPT Mağazası'ndaki popüler GPT'lerin sistem prompt'ları  
**Durum:** ⭐ 8.986 · kontrol 2026-10-03  
**İçerik lisansı:** Kısıtlı lisans — Depo GPL-3.0; içindeki GPT talimatları üçüncü kişilerin, lisansı belirsiz.

**Ne işe yarar:** GPT Mağazası'nda üst sıralardaki botların (yazı, akademik, kod, tasarım...) talimat metinlerini ve prompt koruma/saldırı örneklerini toplar. 'Popüler bir GPT nasıl kurulmuş?' sorusuna cevap.

**Kime göre:** Kendi Custom GPT'sini / sistem prompt'unu yazacaklar.

**Ne bulursun (özgün açıklama)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
README'de rol ve alanlara göre (kodlama, DevOps, veri, ürün, proje yönetimi...) gruplanmış prompt listeleri ve popüler GPT'lerin talimat metinleri var. Metinleri buraya kopyalamadık; depoyu açıp kendin incele.
```
Neden kopyalanmadı: Depo GPL-3.0 beyan ediyor, ancak içindeki GPT talimatları üçüncü kişilere ait; depo lisansı bu metinler üzerinde hak veremez. · Kaynak: https://github.com/ai-boost/awesome-prompts

**Nasıl kullanılır:**

1. README'de kategoriye in, beğendiğin GPT'nin prompt dosyasını aç.
2. Metni kendi Custom GPT'nin 'Instructions' alanına veya Claude Project talimatına yapıştır.
3. Bot adını, hedef kitleyi ve çıktı biçimini kendine göre değiştir.

**Ücret:** Ücretsiz erişim.  
**Topluluk / not:** ≈9 bin yıldız; prompt-injection'a karşı 'prompt protect' örnekleri de içeriyor. (https://github.com/ai-boost/awesome-prompts)  
**Dikkat:** Bazı metinler başkalarının ürünlerinden çıkarılmıştır; ticari kopyalama yerine ilham için kullan.

### FlowGPT

**Link:** https://flowgpt.com  
**Tür:** Topluluk prompt + bot + karakter platformu  
**Durum:** HTTP 200 ilk kontrolde, 403 ikinci kontrolde (bot koruması) · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Kullanıcı içerikleri; site koşulları bu çalışmada incelenmedi.

**Ne işe yarar:** Kategorilere ayrılmış (iş arama, iş, programlama, eğlence...) prompt ve botları site içinde doğrudan sohbet ederek dene, beğenirsen kopyala.

**Kime göre:** Denemeden önce prompt'u çalışırken görmek isteyenler.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Kategori seç → bir prompt/bot aç → site içi sohbette dene → işe yararsa kendi AI aracına taşı.
```
Neden kopyalanmadı: Kullanıcı içerikleri; site koşulları bu çalışmada incelenmedi. · Kaynak: https://flowgpt.com

**Nasıl kullanılır:**

1. flowgpt.com'a gir.
2. İlgi alanı kategorisinden bir prompt/bot seç.
3. Site içi sohbette dene; beğenirsen prompt metnini kopyala.

**Ücret:** Freemium (ücretsiz gezinme + ücretli üyelik).  
**Topluluk / not:** Ağırlık eğlence/karakter botlarında; iş prompt'ları için prompts.chat daha düzenli. (https://flowgpt.com)  
**Dikkat:** Otomatik kontrolde bir kez 200, sonra 403 (bot koruması) verdi; normal tarayıcıda açılıp açılmadığı test edilmedi. Karakter/rol yapma içeriği yoğun; iş amaçlı aramada filtre kullan.

### AIPRM (tarayıcı eklentisi)

**Link:** https://www.aiprm.com  
**Tür:** Chrome/Edge eklentisi: ChatGPT, Claude, Gemini içine hazır prompt menüsü  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Tescilli / üçüncü taraf — Ticari ürün; prompt lisansı belirtilmemiş: bilinmiyor.

**Ne işe yarar:** ChatGPT/Claude/Gemini arayüzünün içine hazır şablon listesi ekler; özellikle SEO, pazarlama, metin yazarlığı şablonları. Kendi özel prompt'larını da saklarsın.

**Kime göre:** Pazarlama/SEO ekipleri, sürekli aynı tür içerik üretenler.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Eklentiyi kur → ChatGPT'yi aç → AIPRM listesinden ör. bir SEO makale şablonu seç → yalnız anahtar kelimeyi yaz.
```
Neden kopyalanmadı: Ticari ürün; prompt lisansı belirtilmemiş: bilinmiyor. · Kaynak: https://www.aiprm.com

**Nasıl kullanılır:**

1. aiprm.com'dan Chrome veya Edge eklentisini kur.
2. ChatGPT/Claude/Gemini sayfasını aç; prompt listesi sayfanın içinde belirir.
3. Şablonu seç, istenen alanı (konu, anahtar kelime) doldur, gönder.

**Ücret:** Ücretsiz kütüphane + ücretli bireysel/ekip planları.  
**Topluluk / not:** Kendi sitesine göre 5.400+ herkese açık prompt; incelemelerde 'SEO/pazarlama için iyi, ücretli plan pahalı' yorumu öne çıkıyor. (https://godofprompt.ai/blog/in-depth-review-leading-ai-prompt-marketplaces/)  
**Dikkat:** Eklenti sohbet sayfasına erişir; şirket verisiyle kullanmadan önce gizlilik politikasını oku.

### PromptBase (prompt pazaryeri)

**Link:** https://promptbase.com  
**Tür:** Ücretli prompt pazaryeri  
**Durum:** HTTP 403 (bot koruması) · kontrol 2026-10-03  
**İçerik lisansı:** Tescilli / üçüncü taraf — Ticari pazaryeri; satın alınan prompt satıcı/site koşullarına tabi. Yeniden dağıtma.

**Ne işe yarar:** Tek tek satın alınan, satıcısının örnek çıktısını gösterdiği prompt'lar (metin ve görsel). Ücretsiz kaynaklarda bulamadığın çok özel bir sonuç için.

**Kime göre:** Belirli bir görsel stil veya niş iş için hazır, test edilmiş prompt'a para vermeye razı olanlar.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Örnek çıktılara bak → prompt'u satın al → gelen şablondaki [köşeli] alanları doldur.
```
Neden kopyalanmadı: Ticari pazaryeri; satın alınan prompt satıcı/site koşullarına tabi. Yeniden dağıtma. · Kaynak: https://promptbase.com

**Nasıl kullanılır:**

1. Siteye gir, model (ChatGPT, Midjourney vb.) ve konu seç.
2. Örnek çıktıları ve yorumları incele.
3. Satın al; şablonu kendi aracında doldurarak kullan.

**Ücret:** Prompt başına ücret (incelemelere göre çoğu 2–7 USD).  
**Topluluk / not:** Reddit'te 'prompt satmak AI'ın açıklığını tehdit ediyor' eleştirisi var; ücretsiz alternatifleri önce dene. (https://godofprompt.ai/blog/in-depth-review-leading-ai-prompt-marketplaces/)  
**Dikkat:** Otomatik kontrolde 403 (bot koruması) verdi; bu oturumda içerik doğrulanamadı. Normal tarayıcıda açılıp açılmadığı test edilmedi.

### Poe (hazır botlar)

**Link:** https://poe.com/explore  
**Tür:** Çok modelli sohbet platformu + topluluk botları  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Bot prompt'larının lisansı belirtilmemiş.

**Ne işe yarar:** Tek yerde birçok model ve başkalarının prompt'la kurduğu botlar; kategoriler: resmi, arama, görsel, video, ses, programlama.

**Kime göre:** Farklı modelleri ve hazır botları tek hesapla denemek isteyenler.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Explore → kategori → botu aç → doğrudan isteğini yaz.
```
Neden kopyalanmadı: Bot prompt'larının lisansı belirtilmemiş. · Kaynak: https://poe.com/explore

**Nasıl kullanılır:**

1. poe.com/explore adresine gir.
2. Kategoriden bot seç ve sohbet et.
3. Beğendiğin botun yaklaşımını kendi prompt'una uyarlayabilirsin.

**Ücret:** Freemium (puan sistemi).  
**Topluluk / not:** Keşif sayfası resmî/üçüncü taraf botları ayırıyor. (https://poe.com/explore)  
**Dikkat:** Bot sahiplerinin prompt'ları her zaman görünmez.

<a id="ofis"></a>

## İş ve ofis: resmi prompt paketleri

OpenAI, Anthropic, Google ve Microsoft'un kendi yayımladığı departman/rol bazlı hazır prompt'lar.

### OpenAI Academy — 'ChatGPT for any role' prompt paketleri

**Link:** https://academy.openai.com/public/clubs/work-users-ynjqu/resources/chatgpt-for-any-role  
**Tür:** Resmi, ücretsiz prompt paketi  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — OpenAI sayfası; içerik lisansı belirtilmemiş.

**Ne işe yarar:** İş için hazır prompt'lar: iletişim ve yazı, toplantı ve iş birliği, problem çözme, organizasyon. Ayrıca pazarlama, IT, İK, satış, ürün, mühendislik, sağlık rollerine özel paketlere link verir.

**Kime göre:** Ofis çalışanları, yöneticiler, ekip liderleri.

**Ne bulursun (özgün açıklama)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Kısa ve doldurmalı şablonlar: örneğin kime, hangi konuda ve hangi tonda yazılacağını boşluklara yazdığın bir e-posta isteği ya da toplantı öncesi sorulacak soruları çıkaran bir istek. Şablon metinlerini kopyalamadık; sayfadan kendin al.
```
Neden kopyalanmadı: OpenAI sayfasında lisans belirtilmemiş: bilinmiyor. · Kaynak: https://academy.openai.com/public/clubs/work-users-ynjqu/resources/chatgpt-for-any-role

**Nasıl kullanılır:**

1. Sayfayı aç, ilgili başlığı seç (ör. Meetings & collaboration).
2. Prompt'u kopyala, [köşeli] alanları doldur.
3. Rolüne özel paketi (ör. HR, Sales) sayfadaki linkten aç.

**Ücret:** Ücretsiz.  
**Topluluk / not:** Resmi kaynak; kısa ve şablonlu, başlangıç için ideal. (https://academy.openai.com/)  
**Dikkat:** Prompt'lar kısa; kendi şirket bağlamını eklemezsen genel sonuç alırsın.

### Claude Academy — Use cases (kullanım senaryoları)

**Link:** https://academy.claude.com/use-cases  
**Tür:** Resmi, adım adım senaryo rehberleri  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Anthropic sayfası; içerik lisansı belirtilmemiş.

**Ne işe yarar:** Departmana göre hazır senaryolar: Genel, Pazarlama, Ürün, Mühendislik, İK, Finans, Operasyon, Veri, Tasarım, Hukuk, Satış, Araştırma, Eğitim, Kişisel. Ör. 'Haftanı hazırla', 'Kullanıcı geri bildirimindeki örüntüleri analiz et', 'Olay sonrası raporu (postmortem) taslakla'.

**Kime göre:** Claude kullananlar; hazır iş senaryosunu adım adım görmek isteyenler.

**Ne bulursun (özgün açıklama)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Departmana göre gruplanmış, süresi yazılı kısa rehberler: haftalık planlama, kullanıcı geri bildirimi analizi, müşteri araştırması, finansal senaryo, olay sonrası rapor gibi işler.
```
Neden kopyalanmadı: Anthropic sayfasında içerik lisansı belirtilmemiş: bilinmiyor. · Kaynak: https://academy.claude.com/use-cases

**Nasıl kullanılır:**

1. Sayfada departman filtresini seç.
2. Senaryoyu aç; tahmini süreyi ve adımları gör.
3. Verilen prompt/adımları Claude'da uygula.

**Ücret:** Ücretsiz.  
**Topluluk / not:** Eski 'Anthropic Prompt Library' adresi artık prompting best practices sayfasına yönleniyor (3 Ekim 2026 kontrolü); hazır senaryolar bu sayfada. (https://platform.claude.com/docs/en/build-with-claude/prompt-engineering/claude-prompting-best-practices)  
**Dikkat:** Bunlar tek satır prompt değil, kısa rehberler; 'hazır prompt' arıyorsan prompts.chat daha hızlı.

### Google — Gemini for Workspace prompt rehberi

**Link:** https://workspace.google.com/learning/content/gemini-prompt-guide  
**Tür:** Resmi rehber (rol bazlı örnek prompt'lar)  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Google PDF'i; yeniden dağıtım lisansı belirtilmemiş.

**Ne işe yarar:** Gmail, Docs, Sheets, Slides içinde Gemini için rol bazlı (pazarlama, satış, İK, yönetici, müşteri hizmetleri...) örnek prompt'lar ve 'persona + görev + bağlam + biçim' kalıbı.

**Kime göre:** Google Workspace kullanan şirketler.

**Ne bulursun (özgün açıklama)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Rehber her prompt'ta dört alanı önerir: sen kimsin, ne istiyorsun (bir fiille), arka plan ne, çıktı nasıl olsun. Ardından pazarlama, satış, İK, yönetici gibi rollere göre köşeli parantezli doldurma şablonları gelir. Örnek prompt'ları kopyalamadık; PDF'ten al.
```
Neden kopyalanmadı: Google'ın telifli PDF'i; yeniden dağıtım lisansı belirtilmemiş: bilinmiyor. · Kaynak: https://workspace.google.com/learning/content/gemini-prompt-guide

**Nasıl kullanılır:**

1. Rehber sayfasını aç; rolüne uygun bölümü bul.
2. Örnek prompt'u Gmail/Docs'taki Gemini paneline yapıştır.
3. Persona/Görev/Bağlam/Biçim alanlarını kendine göre doldur.

**Ücret:** Rehber ücretsiz; Workspace'te Gemini plana bağlı.  
**Topluluk / not:** Rehber 4 alanı önerir: Persona, Task, Context, Format; ve 'görevde mutlaka bir fiil/komut olsun' der. 21 sayfalık PDF senaryo bazlı örnek prompt'lar içerir. (https://services.google.com/fh/files/misc/google_workspace_prompting_guide_abridged_smbs_startups_september2024.pdf)  
**Dikkat:** Web sayfası otomatik okuyucuda kesildi; içerik resmi PDF'ten (Eylül 2024 KOBİ sürümü) doğrulandı — tarihli olabilir.

Ek linkler: https://services.google.com/fh/files/misc/google_workspace_prompting_guide_abridged_smbs_startups_september2024.pdf

### Microsoft Copilot Prompt Gallery

**Link:** https://copilot.cloud.microsoft/prompts  
**Tür:** Resmi galeri (Word, Excel, PowerPoint, Outlook, Teams için hazır prompt'lar)  
**Durum:** HTTP 200 (Microsoft giriş ekranı) · kontrol 2026-10-03  
**İçerik lisansı:** Tescilli / üçüncü taraf — Microsoft hizmeti; kullanıcı prompt'ları yalnız kurum içinde paylaşılabilir.

**Ne işe yarar:** Microsoft 365 uygulamalarına özel hazır prompt'lar; beğendiğini kaydedip ekip arkadaşlarınla link olarak paylaşırsın.

**Kime göre:** Microsoft 365 Copilot (iş) lisansı olan şirket çalışanları.

**Kaydet/paylaş akışı (Microsoft destek sayfasından)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Prompt'u çalıştır → üzerine gelip 'Save prompt' → galeride 'Saved' sekmesi → Share → Copy link.
```
Neden kopyalanmadı: Microsoft hizmeti; kullanıcı prompt'ları yalnız kurum içinde paylaşılabilir. · Kaynak: https://support.microsoft.com/microsoft-365-copilot/share-your-best-prompts-with-others

**Nasıl kullanılır:**

1. İş hesabınla copilot.cloud.microsoft/prompts adresine gir.
2. Uygulamaya (Word, Excel...) ve göreve göre filtrele.
3. Prompt'u çalıştır, beğendiğini kaydet ve paylaş.

**Ücret:** Microsoft 365 Copilot (work) lisansı gerekir.  
**Topluluk / not:** Microsoft Learn'de galeri belgesi var. (https://learn.microsoft.com/en-us/microsoft-365/copilot/copilot-prompt-gallery)  
**Dikkat:** Giriş gerektirir; bu oturumda galeri içeriği görülemedi (giriş ekranına yönlendi).

<a id="icerik"></a>

## İçerik işleme ve kişisel üretkenlik sistemi

Video, makale, toplantı notu gibi içerikleri hazır kalıplarla özete, fikir listesine, eyleme çeviren sistemler.

### Fabric (Daniel Miessler) — 250+ hazır 'pattern'

**Link:** https://github.com/danielmiessler/Fabric  
**Tür:** Açık kaynak komut satırı aracı + hazır sistem prompt'ları  
**Durum:** ⭐ 44.153 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — MIT.

**Ne işe yarar:** Her 'pattern' tek bir işi yapan, IDENTITY/STEPS/OUTPUT bölümlü uzun bir sistem prompt'udur: extract_wisdom (videodan fikir/alıntı/tavsiye çıkar), summarize, create_quiz, analyze_claims, improve_writing, agility_story... YouTube transkripti, makale veya not üzerinde tek komutla çalışır.

**Kime göre:** Çok video/makale tüketip not çıkaranlar; 'tek tam prompt'la sistem kuran' kişiler için en iyi şablon örneği.

**Örnek — extract_wisdom pattern'inin başı (birebir, MIT):**

```text
# IDENTITY and PURPOSE

You extract surprising, insightful, and interesting information from text content. You are interested in insights related to the purpose and meaning of life, human flourishing, the role of technology in the future of humanity, artificial intelligence and its affect on humans, memes, learning, reading, books, continuous improvement, and similar topics.
```
Bu alıntının lisans kaydı:

- Kaynak depo: `danielmiessler/Fabric`
- Tam dosya (commit'e sabit): https://github.com/danielmiessler/Fabric/blob/ddf1aab968caa9adf90137a6536c768d02b237d6/data/patterns/extract_wisdom/system.md
- Commit: `ddf1aab968caa9adf90137a6536c768d02b237d6` (2026-10-02T20:31:29-07:00) · erişim: 2026-10-03
- Sahip: Copyright (c) 2012-2024 Scott Chacon and others (LICENSE dosyasındaki satır aynen)
- Lisans: **MIT** · belge: `lisanslar/Fabric_LICENSE`
- Yeniden dağıtım: MIT: kopyalama/dağıtım/ticari kullanım serbest; telif bildirimi ve MIT izin metni her kopyada bulunmalı (bkz. lisans belgesi).

**Nasıl kullanılır:**

1. Kurmadan kullanım: pattern'in system.md dosyasını aç, tamamını kopyala, ChatGPT/Claude'a yapıştır, altına metni/transkripti ekle.
2. Kurulum (macOS): brew install fabric-ai · (Windows): winget install danielmiessler.Fabric · (Linux): README'deki tek satırlık kurulum.
3. Örnek komut (README'den birebir): yt URL | fabric -p extract_wisdom  — YouTube videosunun transkriptini pattern'e verir.
4. Tüm pattern listesi ve açıklamaları: data/patterns/pattern_explanations.md

**Ücret:** Ücretsiz. Kendi AI sağlayıcı API anahtarın gerekir (veya yerel model).  
**Topluluk / not:** 44 bin+ yıldız; pattern açıklama dosyasında 255 numaralı pattern listelenmiş (3 Ekim 2026). (https://raw.githubusercontent.com/danielmiessler/Fabric/main/data/patterns/pattern_explanations.md)  
**Dikkat:** CLI kurulumu teknik; teknik değilsen sadece system.md metinlerini kopyala-yapıştır yap.

<a id="ajan"></a>

## Yazılım ve iş: hazır ajan ekipleri / iş akışı sistemleri

Tek prompt değil; rolleri, adımları ve kuralları önceden yazılmış çok-ajanlı 'hazır sistem' paketleri.

### The Agency (msitarzewski/agency-agents) — hazır AI ajans kadrosu

**Link:** https://github.com/msitarzewski/agency-agents  
**Tür:** Açık depo: departmanlara ayrılmış uzman ajan prompt'ları  
**Durum:** ⭐ 155.875 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — MIT.

**Ne işe yarar:** Mühendislik, tasarım, ücretli reklam, satış, pazarlama, ürün, proje yönetimi, test, güvenlik, destek, finans, oyun, akademik, sağlık, araştırma bölümlerinde her biri kimlik, yetenek, kurallar, teslimat ve başarı ölçütü yazılmış ajanlar (ör. Growth Hacker, Frontend Developer, Reality Checker). Reddit gönderisinden doğdu.

**Kime göre:** Tek kişilik 'ajans' kurmak isteyen girişimciler; Claude Code/Cursor/Codex/Gemini CLI kullananlar. Kod bilmeyen de prompt'u kopyalayıp kullanabilir.

**Örnek — Growth Hacker ajanının başı (birebir, MIT):**

```text
# Marketing Growth Hacker Agent

## Identity & Role Definition
Expert growth strategist specializing in rapid, scalable user acquisition and retention through data-driven experimentation and unconventional marketing tactics.
```
Bu alıntının lisans kaydı:

- Kaynak depo: `msitarzewski/agency-agents`
- Tam dosya (commit'e sabit): https://github.com/msitarzewski/agency-agents/blob/d3f71c4bb8922d3eea7576237a870dd59b3cdd52/marketing/marketing-growth-hacker.md
- Commit: `d3f71c4bb8922d3eea7576237a870dd59b3cdd52` (2026-10-01T14:35:39-05:00) · erişim: 2026-10-03
- Sahip: Copyright (c) 2025 AgentLand Contributors
- Lisans: **MIT** · belge: `lisanslar/agency-agents_LICENSE`
- Yeniden dağıtım: MIT: kopyalama/dağıtım/ticari kullanım serbest; telif bildirimi ve MIT izin metni her kopyada bulunmalı (bkz. lisans belgesi).

**Nasıl kullanılır:**

1. Kodsuz: README'deki rosterden ajan .md dosyasını aç, tamamını sohbetin başına yapıştır, sonra görevini yaz.
2. Claude Code: cp engineering/*.md ~/.claude/agents/  veya  ./scripts/install.sh --tool claude-code
3. Masaüstü uygulaması (macOS/Linux/Windows) ile tek tıkla kurulum: https://agencyagents.app
4. README'deki 'Real-World Use Cases' (MVP kurma, kampanya lansmanı) senaryolarını takip et.

**Ücret:** Ücretsiz.  
**Topluluk / not:** 155 bin+ yıldız; yazarın Reddit'te özel ajanlarını paylaşmasının ardından 12 saatte 50+ istek gelmesiyle başladığı aktarılıyor. (https://flaviocopes.com/agency-agents/)  
**Dikkat:** Ajanlar 'kişilikli' ve uzun; her işe hepsini yükleme, ihtiyacın olan 2-3 ajanı seç.

### Superpowers (obra) — kodlama ajanı için hazır çalışma yöntemi

**Link:** https://github.com/obra/superpowers  
**Tür:** Skill paketi + yazılım geliştirme metodolojisi  
**Durum:** ⭐ 294.680 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — MIT.

**Ne işe yarar:** Ajanı 'stajyer gibi hemen kod yazmaktan' alıkoyup sırayla çalıştırır: brainstorming (önce soru sor, tasarım çıkar) → writing-plans (2-5 dakikalık görevler) → test-driven-development (önce başarısız test) → systematic-debugging → kod incelemesi.

**Kime göre:** Claude Code, Codex, Cursor, Gemini CLI, Copilot CLI ile uygulama yapanlar.

**Örnek — brainstorming skill'inin tanımı (birebir, MIT):**

```text
name: brainstorming
description: "You MUST use this before any creative work - creating features, building components, adding functionality, or modifying behavior. Explores user intent, requirements and design before implementation."
```
Bu alıntının lisans kaydı:

- Kaynak depo: `obra/superpowers`
- Tam dosya (commit'e sabit): https://github.com/obra/superpowers/blob/8ca22dba9a94f28898bbce59f2537ff4d87c747d/skills/brainstorming/SKILL.md
- Commit: `8ca22dba9a94f28898bbce59f2537ff4d87c747d` (2026-09-25T11:06:27-07:00) · erişim: 2026-10-03
- Sahip: Copyright (c) 2025 Jesse Vincent
- Lisans: **MIT** · belge: `lisanslar/superpowers_LICENSE`
- Yeniden dağıtım: MIT: kopyalama/dağıtım/ticari kullanım serbest; telif bildirimi ve MIT izin metni her kopyada bulunmalı (bkz. lisans belgesi).

**Nasıl kullanılır:**

1. Claude Code: /plugin install superpowers@claude-plugins-official
2. Gemini CLI: gemini extensions install https://github.com/obra/superpowers
3. Kurulumu doğrula: yeni oturumda 'Let's make a react todo list' yaz; kod yazmadan önce brainstorming başlamalı (README'deki test).
4. Kodsuz kullanım: skills/*/SKILL.md dosyalarını okuyup kendi sistem prompt'una ilke olarak al.

**Ücret:** Ücretsiz.  
**Topluluk / not:** 294 bin+ yıldız; inceleme yazılarında 'test önce' disiplininin kod kalitesini artırdığı anlatılıyor. (https://www.mejba.me/superpowers-plugin-claude-code-review)  
**Dikkat:** Küçük işlerde süreç ağır gelebilir; TDD kuralı test yazılmadan yazılan kodu sildirir.

### BMAD-METHOD — çevik (agile) AI geliştirme sistemi

**Link:** https://github.com/bmad-code-org/BMAD-METHOD  
**Tür:** Ajan + iş akışı paketi (analist, PM, mimar, geliştirici, test rolleri)  
**Durum:** ⭐ 53.751 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — MIT.

**Ne işe yarar:** Fikirden ürüne: analist → ürün yöneticisi (PRD) → mimar → hikâyeler → geliştirme → test. Ajanlar kararları açık yazar ve sonraki adıma bağlam olarak bırakır.

**Kime göre:** Uygulamayı baştan sona yapay zekâyla planlayıp yazdırmak isteyenler.

**Örnek — Kurulum (README'den birebir):**

```text
npx skills add bmad-code-org/BMAD-METHOD
```
Bu alıntının lisans kaydı:

- Kaynak depo: `bmad-code-org/BMAD-METHOD`
- Tam dosya (commit'e sabit): https://github.com/bmad-code-org/BMAD-METHOD/blob/3cae711ea5274cf7c7cf6e173bb8d7f29cd71497/README.md
- Commit: `3cae711ea5274cf7c7cf6e173bb8d7f29cd71497` (2026-10-03T01:44:54-05:00) · erişim: 2026-10-03
- Sahip: Copyright (c) 2025 BMad Code, LLC
- Lisans: **MIT** · belge: `lisanslar/BMAD-METHOD_LICENSE`
- Yeniden dağıtım: MIT: kopyalama/dağıtım/ticari kullanım serbest; telif bildirimi ve MIT izin metni her kopyada bulunmalı (bkz. lisans belgesi).

**Nasıl kullanılır:**

1. Skill destekleyen bir kodlama aracında: npx skills add bmad-code-org/BMAD-METHOD
2. Kurulumda 'bmad' + seçtiğin modülleri işaretle.
3. Ajana 'bmad status' yaz: sürümü ve sıradaki adımı gösterir; güncelleme için 'bmad setup'.

**Ücret:** Ücretsiz.  
**Topluluk / not:** 53 bin+ yıldız; Builder ve Test Architect ek modülleri var. (https://github.com/bmad-code-org/BMAD-METHOD)  
**Dikkat:** Öğrenme eğrisi var; küçük betikler için fazla.

### GitHub Spec Kit — şartname odaklı geliştirme (SDD)

**Link:** https://github.com/github/spec-kit  
**Tür:** Resmi GitHub aracı: hazır komut/skill zinciri  
**Durum:** ⭐ 139.923 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — MIT.

**Ne işe yarar:** Önce 'anayasa' (proje ilkeleri), sonra her özellik için specify → plan → tasks → implement → converge. Hata düzeltme ve fikir değerlendirme eklentileri de var.

**Kime göre:** Copilot, Claude Code, Gemini CLI vb. ile düzenli proje yürütenler.

**Örnek — Hazır komut zinciri (README'den birebir):**

```text
/speckit-constitution Create principles focused on code quality, testing, and maintainability.
/speckit-specify Build a photo organizer with albums grouped by date and a tile preview of each album.
/speckit-plan Use Vite with vanilla JavaScript. Keep images local and store metadata in SQLite.
/speckit-tasks
/speckit-implement
```
Bu alıntının lisans kaydı:

- Kaynak depo: `github/spec-kit`
- Tam dosya (commit'e sabit): https://github.com/github/spec-kit/blob/e1fa857a7f536b22760d48c1aa9ace41df0fd1dc/README.md
- Commit: `e1fa857a7f536b22760d48c1aa9ace41df0fd1dc` (2026-10-02T16:53:33-05:00) · erişim: 2026-10-03
- Sahip: Copyright GitHub, Inc.
- Lisans: **MIT** · belge: `lisanslar/spec-kit_LICENSE`
- Yeniden dağıtım: MIT: kopyalama/dağıtım/ticari kullanım serbest; telif bildirimi ve MIT izin metni her kopyada bulunmalı (bkz. lisans belgesi).

**Nasıl kullanılır:**

1. Python 3.11+ ve uv kur.
2. uv tool install specify-cli  ardından  specify init my-project --integration copilot
3. Ajan sohbetinde /speckit-* komutlarını sırayla çalıştır.

**Ücret:** Ücretsiz.  
**Topluluk / not:** 139 bin+ yıldız. (https://github.com/github/spec-kit)  
**Dikkat:** Komut satırı gerekir.

### wshobson/agents — çok araçlı ajan eklenti pazarı

**Link:** https://github.com/wshobson/agents  
**Tür:** Plugin marketplace (Claude Code, Codex, Cursor, OpenCode, Copilot, Antigravity, Pi)  
**Durum:** ⭐ 40.170 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — MIT.

**Ne işe yarar:** 94 eklentide paketlenmiş uzman ajanlar ve skill'ler (Python geliştirme, güvenlik, DevOps, veri...). Tek komutla ihtiyacın olan paketi kurarsın.

**Kime göre:** Kodlama ajanı kullananlar.

**Örnek — Kurulum (README'den birebir):**

```text
/plugin marketplace add wshobson/agents
/plugin install python-development
```
Bu alıntının lisans kaydı:

- Kaynak depo: `wshobson/agents`
- Tam dosya (commit'e sabit): https://github.com/wshobson/agents/blob/156b7a5e7a8b93642628a339ee4039c925b34c7f/README.md
- Commit: `156b7a5e7a8b93642628a339ee4039c925b34c7f` (2026-09-28T20:45:26-04:00) · erişim: 2026-10-03
- Sahip: Copyright (c) 2024 Seth Hobson
- Lisans: **MIT** · belge: `lisanslar/wshobson-agents_LICENSE`
- Yeniden dağıtım: MIT: kopyalama/dağıtım/ticari kullanım serbest; telif bildirimi ve MIT izin metni her kopyada bulunmalı (bkz. lisans belgesi).

**Nasıl kullanılır:**

1. Claude Code'da marketplace'i ekle.
2. İhtiyacın olan eklentiyi kur.
3. Ajanı görev tanımıyla çağır.

**Ücret:** Ücretsiz.  
**Topluluk / not:** 40 bin+ yıldız, açık sorun sayısı çok düşük (4). (https://github.com/wshobson/agents)  
**Dikkat:** Çok eklenti kurmak bağlamı şişirir; seçici ol.

### VoltAgent/awesome-claude-code-subagents — 100+ alt ajan

**Link:** https://github.com/VoltAgent/awesome-claude-code-subagents  
**Tür:** Açık depo: kategorili Claude Code alt ajanları  
**Durum:** ⭐ 25.472 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — MIT.

**Ne işe yarar:** Çekirdek geliştirme, dil uzmanları, altyapı, kalite/güvenlik, veri/AI, iş, meta-orkestrasyon kategorilerinde hazır alt ajan tanımları (ör. api-designer).

**Kime göre:** Claude Code kullananlar; ajan tanımı yazmayı örnekle öğrenmek isteyenler.

**Örnek — api-designer ajanının başı (birebir):**

```text
You are a senior API designer specializing in creating intuitive, scalable API architectures with expertise in REST and GraphQL design patterns.
```
Bu alıntının lisans kaydı:

- Kaynak depo: `VoltAgent/awesome-claude-code-subagents`
- Tam dosya (commit'e sabit): https://github.com/VoltAgent/awesome-claude-code-subagents/blob/82b73821baa7a911d5b14cfb6da238b7f0db6b42/categories/01-core-development/api-designer.md
- Commit: `82b73821baa7a911d5b14cfb6da238b7f0db6b42` (2026-09-21T11:24:27+03:00) · erişim: 2026-10-03
- Sahip: Copyright (c) 2025 VoltAgent
- Lisans: **MIT** · belge: `lisanslar/awesome-claude-code-subagents_LICENSE`
- Yeniden dağıtım: MIT: kopyalama/dağıtım/ticari kullanım serbest; telif bildirimi ve MIT izin metni her kopyada bulunmalı (bkz. lisans belgesi).

**Nasıl kullanılır:**

1. İstediğin .md dosyasını ~/.claude/agents/ klasörüne kopyala.
2. Claude Code'da görevi yaz; uygun ajan otomatik seçilir veya adıyla çağrılır.

**Ücret:** Ücretsiz.  
**Topluluk / not:** 25 bin+ yıldız. (https://github.com/VoltAgent/awesome-claude-code-subagents)  
**Dikkat:** Ajan dosyalarındaki 'model:' alanını kendi erişimine göre düzenle.

<a id="kural"></a>

## Editör kuralları ve skill koleksiyonları

Cursor, Copilot, Claude gibi araçlara proje kuralı / yetenek (skill) olarak eklenen hazır dosyalar.

### Anthropic Agent Skills (anthropics/skills)

**Link:** https://github.com/anthropics/skills  
**Tür:** Resmi skill örnekleri + Agent Skills standardı  
**Durum:** ⭐ 179.478 · kontrol 2026-10-03  
**İçerik lisansı:** Kısıtlı lisans — Klasör bazında Apache-2.0 veya source-available; kök lisans dosyası yok.

**Ne işe yarar:** Claude'un dinamik yüklediği talimat klasörleri: yaratıcı/tasarım, geliştirme/teknik, kurumsal iletişim ve Claude'un kendi Word/PDF/PowerPoint/Excel skill'leri. Kendi 'tekte tam prompt'unu skill olarak paketlemenin resmi şablonu.

**Kime göre:** Claude.ai (ücretli plan), Claude Code veya Claude API kullananlar.

**Skill dosyasının biçimi (özgün örnek, bizim yazdığımız)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
---
name: haftalik-rapor
description: Haftalık satış notlarından tek sayfalık özet rapor çıkarır; kullanıcı 'haftalık rapor' dediğinde kullan.
---
# Haftalık rapor
1. Notları oku, rakamları tabloya dök.
2. En önemli 3 gelişmeyi ve 3 riski yaz.
3. Bir sonraki hafta için 5 eylem öner.
```
Neden kopyalanmadı: Depo kökünde lisans dosyası yok; README çoğu skill için Apache-2.0, belge skill'leri için source-available diyor. Bu yüzden şablon metni yerine biçimi gösteren kendi örneğimizi yazdık. · Kaynak: https://github.com/anthropics/skills

**Nasıl kullanılır:**

1. Claude Code: /plugin marketplace add anthropics/skills
2. Sonra: /plugin install document-skills@anthropic-agent-skills
3. Kurduktan sonra skill'i adıyla an: ör. 'PDF skill'ini kullanarak şu dosyadaki form alanlarını çıkar' de.
4. Claude.ai: Ayarlar'dan skill yükle (destek makalesi README'de).

**Ücret:** Depo ücretsiz; Claude.ai'de skill kullanımı ücretli planlarda.  
**Topluluk / not:** 179 bin+ yıldız; skills.sh dizini standartla uyumlu skill'leri listeler. (https://skills.sh/anthropics/skills)  
**Dikkat:** Depo 'gösterim ve eğitim amaçlı' uyarısı taşıyor; kritik işte önce test et.

### GitHub awesome-copilot

**Link:** https://github.com/github/awesome-copilot  
**Tür:** Resmi topluluk deposu: ajan, talimat, skill, plugin, cookbook  
**Durum:** ⭐ 39.651 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — Depo MIT; içerik üçüncü taraf katkıları (README uyarısı).

**Ne işe yarar:** GitHub Copilot için hazır özel ajanlar (MCP'li), dosya türüne göre otomatik uygulanan kodlama standartları, skill'ler ve iş akışı paketleri. Tarayıcıdan gezilebilir sitesi var.

**Kime göre:** VS Code / Copilot CLI kullananlar.

**Örnek — Plugin kurulumu (README'den birebir):**

```text
copilot plugin install <plugin-name>@awesome-copilot
```
Bu alıntının lisans kaydı:

- Kaynak depo: `github/awesome-copilot`
- Tam dosya (commit'e sabit): https://github.com/github/awesome-copilot/blob/143a3d976b3c1603cc8932984d5e1f28501cb5fc/README.md
- Commit: `143a3d976b3c1603cc8932984d5e1f28501cb5fc` (2026-10-02T08:21:25+10:00) · erişim: 2026-10-03
- Sahip: Copyright GitHub, Inc.
- Lisans: **MIT** · belge: `lisanslar/awesome-copilot_LICENSE`
- Yeniden dağıtım: MIT: kopyalama/dağıtım/ticari kullanım serbest; telif bildirimi ve MIT izin metni her kopyada bulunmalı (bkz. lisans belgesi).

**Nasıl kullanılır:**

1. https://awesome-copilot.github.com/agents adresinden gez.
2. Beğendiğin plugin'i copilot plugin install ile kur.
3. Talimat dosyalarını projenin .github/ klasörüne ekle.

**Ücret:** Ücretsiz depo; Copilot aboneliği ayrı.  
**Topluluk / not:** 39 bin+ yıldız; README 'üçüncü taraf içerik, kurmadan önce incele' uyarısı veriyor. (https://awesome-copilot.github.com/agents)  
**Dikkat:** Topluluk katkısı; güvenlik açısından incele.

### awesome-cursorrules (PatrickJS)

**Link:** https://github.com/PatrickJS/awesome-cursorrules  
**Tür:** Cursor proje kuralı (.mdc) koleksiyonu  
**Durum:** ⭐ 40.872 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — CC0-1.0 (depo beyanı).

**Ne işe yarar:** Frontend, backend, mobil, oyun, CSS, veritabanı, test, güvenlik, dokümantasyon başlıklarında hazır kural dosyaları. Örneğin 'Anti-Sycophancy Code Discipline': uydurma API'leri ve sahte özgüveni engelleyen 17 kural.

**Kime göre:** Cursor (ve kural dosyası destekleyen diğer editörler) kullananlar.

**Örnek — Kullanım (README'den):**

```text
Drop the `.mdc` in `.cursor/rules/`.
```
Bu alıntının lisans kaydı:

- Kaynak depo: `PatrickJS/awesome-cursorrules`
- Tam dosya (commit'e sabit): https://github.com/PatrickJS/awesome-cursorrules/blob/b044f956f021b6e8877f16781bcfc466a6a120e9/README.md
- Commit: `b044f956f021b6e8877f16781bcfc466a6a120e9` (2026-05-30T11:01:29-07:00) · erişim: 2026-10-03
- Sahip: PatrickJS ve katkıcılar
- Lisans: **CC0-1.0** · belge: `lisanslar/awesome-cursorrules_LICENSE`
- Yeniden dağıtım: CC0-1.0 depo beyanı; atıf zorunlu değil. Kural dosyaları topluluk katkısıdır, katkıcı bazında kontrol yapılmadı.

**Nasıl kullanılır:**

1. README'de teknolojine uygun kuralı bul.
2. .mdc dosyasını projendeki .cursor/rules/ klasörüne koy.
3. Ekip olarak aynı kuralları paylaş.

**Ücret:** Ücretsiz.  
**Topluluk / not:** 40 bin+ yıldız; benzer bir dizin sitesi cursor.directory (otomatik kontrolde 429 verdi, içeriği doğrulanmadı). (https://cursor.directory)  
**Dikkat:** Eski .cursorrules biçimindeki dosyalar yeni .mdc biçimine taşınmış olabilir.

### awesome-claude-code (hesreallyhim)

**Link:** https://github.com/hesreallyhim/awesome-claude-code  
**Tür:** Seçilmiş liste: skill, ajan, slash komut, hook, CLAUDE.md örnekleri  
**Durum:** ⭐ 54.990 · kontrol 2026-10-03  
**İçerik lisansı:** Kısıtlı lisans — CC BY-NC-ND 4.0: ticari kullanım ve değiştirilmiş kopya yasak.

**Ne işe yarar:** Claude Code için en iyi hazır skill'ler, ajanlar, durum satırları, hook'lar ve eklentilerin elle seçilmiş listesi; diğer depolara kapı.

**Kime göre:** Claude Code'u kişiselleştirmek isteyenler.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Listeden bir 'CLAUDE.md' veya slash komut örneği seç → kendi projene kopyala.
```
Neden kopyalanmadı: CC BY-NC-ND 4.0: ticari kullanım ve değiştirilmiş kopya yasak. · Kaynak: https://raw.githubusercontent.com/hesreallyhim/awesome-claude-code/main/README.md

**Nasıl kullanılır:**

1. README'de kategoriye göz at.
2. Bağlantılı depoya git, kurulum talimatını uygula.

**Ücret:** Ücretsiz erişim.  
**Topluluk / not:** 55 bin yıldız. (https://github.com/hesreallyhim/awesome-claude-code)  
**Dikkat:** Liste; içerikler farklı yazarların, her birini ayrı değerlendir.

<a id="sistem"></a>

## Gerçek ürünlerin sistem promptları

Cursor, Claude, ChatGPT, Lovable gibi ürünlerin içindeki uzun sistem promptları: 'profesyoneller nasıl kurmuş' sorusunun cevabı.

### system-prompts-and-models-of-ai-tools (x1xhlol)

**Link:** https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools  
**Tür:** Ürün sistem prompt'ları arşivi  
**Durum:** ⭐ 144.001 · kontrol 2026-10-03  
**İçerik lisansı:** Tescilli / üçüncü taraf — Ürün şirketlerinin tescilli metinleri; depo GPL-3.0 beyanı bu metinlere hak veremez.

**Ne işe yarar:** Cursor, Claude Code, Devin, Lovable, Manus, Notion AI, Perplexity, Replit, v0, Windsurf, Xcode vb. ürünlerin tam sistem prompt'ları ve araç tanımları. Profesyonel bir 'tekte tam prompt'un nasıl bölümlendiğini (araçlar, kurallar, örnekler) görmenin en iyi yeri.

**Kime göre:** Kendi ajan/sistem prompt'unu yazan herkes.

**Ne bulursun (özgün açıklama)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Ürün klasörlerinde (ör. Cursor) uzun sistem prompt'ları ve JSON araç tanımları var. Yapı genelde şöyle: model kimliği → her araç için 'ne zaman kullan / ne zaman kullanma' ve örnekler → davranış ve biçim kuralları. Metni buraya kopyalamadık.
```
Neden kopyalanmadı: Depo GPL-3.0 beyan ediyor, ancak içerik ürün şirketlerinin tescilli sistem prompt'ları; derleyenin lisansı bu metinler üzerinde hak veremez. Yeniden dağıtım izni yok sayılmalı. · Kaynak: https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools

**Nasıl kullanılır:**

1. Ürün klasörünü aç (ör. 'Cursor Prompts').
2. Prompt'un yapısını incele: kimlik → araçlar (ne zaman kullan / kullanma) → kurallar → örnekler.
3. Bu iskeleti kendi prompt'una uyarla (bkz. hazir-promptlar.md sonundaki Türkçe ana şablon).

**Ücret:** Ücretsiz erişim.  
**Topluluk / not:** 144 bin yıldız; son güncelleme notu 12/07/2026. (https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools)  
**Dikkat:** Sızdırılmış/çıkarılmış metinler: resmî ve güncel olmayabilir; ticari ürününe birebir kopyalama, yapıdan ilham al.

### CL4R1T4S (elder-plinius)

**Link:** https://github.com/elder-plinius/CL4R1T4S  
**Tür:** Sohbet botu sistem prompt'ları arşivi  
**Durum:** ⭐ 50.849 · kontrol 2026-10-03  
**İçerik lisansı:** Tescilli / üçüncü taraf — Ürün şirketlerinin tescilli metinleri; depo AGPL-3.0 beyanı bu metinlere hak veremez.

**Ne işe yarar:** ChatGPT, Claude, Gemini, Grok, Perplexity, Cursor, Lovable, Replit vb. sohbet ürünlerinin sistem prompt'ları; 'AI şeffaflığı' amacıyla.

**Kime göre:** Büyük sohbet ürünlerinin davranış kurallarını merak edenler.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Üretici klasörünü aç → sistem prompt'unda ton, güvenlik ve biçim kurallarının nasıl yazıldığına bak.
```
Neden kopyalanmadı: Ürün şirketlerinin tescilli metinleri; depo AGPL-3.0 beyanı bu metinlere hak veremez. · Kaynak: https://github.com/elder-plinius/CL4R1T4S

**Nasıl kullanılır:**

1. Depoda ürün klasörünü seç.
2. Biçim/ton kurallarını kendi prompt'una örnek al.

**Ücret:** Ücretsiz erişim.  
**Topluluk / not:** 50 bin+ yıldız; 'red-team' topluluğundan. (https://github.com/elder-plinius/CL4R1T4S)  
**Dikkat:** Doğruluğu üreticiler tarafından teyit edilmez; güvenlik aşma (jailbreak) amaçlı kullanma.

### leaked-system-prompts (jujumilk3)

**Link:** https://github.com/jujumilk3/leaked-system-prompts  
**Tür:** Kaynak gösterilerek doğrulanan sistem prompt arşivi  
**Durum:** ⭐ 14.957 · kontrol 2026-10-03  
**İçerik lisansı:** Tescilli / üçüncü taraf — Lisans dosyası yok; içerik üçüncü taraf ürünlere ait.

**Ne işe yarar:** Yaygın LLM servislerinin sistem prompt'ları; katkılarda doğrulanabilir kaynak veya tekrarlanabilir prompt şartı arıyor, akademik makalelerde atıf alıyor.

**Kime göre:** Daha 'kaynaklı' bir arşiv isteyenler.

**Ne bulursun (özgün açıklama)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Servis adına göre dosyalar; derleyen, katkılarda doğrulanabilir kaynak veya tekrar üretilebilir prompt şartı arıyor. Metin kopyalamadık.
```
Neden kopyalanmadı: Depoda lisans dosyası yok; içerik üçüncü taraf ürünlere ait: bilinmiyor / yeniden dağıtım izni yok sayılmalı. · Kaynak: https://github.com/jujumilk3/leaked-system-prompts

**Nasıl kullanılır:**

1. Depoda servis adına göre dosyayı aç.
2. Tarih ve kaynak bilgisine bak; eski olabilir.

**Ücret:** Ücretsiz erişim.  
**Topluluk / not:** 15 bin yıldız. (https://github.com/jujumilk3/leaked-system-prompts)  
**Dikkat:** Tarihli içerik; ürünler prompt'larını sık değiştirir.

<a id="otomasyon"></a>

## Otomasyon: hazır iş akışı şablonları

Tek tıkla içe aktarılan, AI adımlı hazır otomasyonlar (e-posta, sosyal medya, rapor, destek botu...).

### n8n Workflow Templates (resmi)

**Link:** https://n8n.io/workflows/  
**Tür:** Resmi otomasyon şablon galerisi  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Şablon lisansı tek tek belirtilmemiş.

**Ne işe yarar:** 12.895 hazır iş akışı (3 Ekim 2026): AI, satış, IT operasyon, pazarlama, belge işlemleri, destek. Ör. gelen e-postayı AI ile sınıflandır, Telegram AI asistanı, Google Sheets'e AI özet yaz. Her şablonun içinde AI adımının prompt'u hazır gelir.

**Kime göre:** Tekrarlayan işini otomatiğe bağlamak isteyen herkes (az kodlu).

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Şablonu aç → 'Use workflow' → n8n'de kimlik bilgilerini (OpenAI, Gmail vb.) bağla → AI düğümündeki prompt'u kendine göre değiştir → çalıştır.
```
Neden kopyalanmadı: Şablon lisansı tek tek belirtilmemiş. · Kaynak: https://n8n.io/workflows/

**Nasıl kullanılır:**

1. n8n.io/workflows adresinde 'AI' kategorisini seç.
2. Şablonu n8n Cloud'a veya kendi kurduğun n8n'e aktar.
3. Bağlantıları kendi hesaplarınla yetkilendir; AI düğümündeki sistem mesajını düzenle.

**Ücret:** Şablonlar ücretsiz; n8n Cloud ücretli, self-host ücretsiz (lisans koşullarına bağlı). AI servis API'leri ayrı ücretli.  
**Topluluk / not:** Topluluk tüm şablonları toplayıp aranabilir arşiv yapmış (Zie619/n8n-workflows). (https://github.com/Zie619/n8n-workflows)  
**Dikkat:** Şablon kendi hesaplarına erişir; yetkileri dar tut.

### Zie619/n8n-workflows — aranabilir n8n arşivi

**Link:** https://zie619.github.io/n8n-workflows  
**Tür:** Topluluk arşivi + arama arayüzü  
**Durum:** ⭐ 56.876 · kontrol 2026-10-03  
**İçerik lisansı:** Kısıtlı lisans — Depo kodu MIT; toplanan iş akışlarının özgün lisansı bilinmiyor.

**Ne işe yarar:** Bulunabilen tüm n8n iş akışlarını JSON olarak toplar; web arayüzünden arayıp indirirsin, içe aktarırsın.

**Kime göre:** Belirli bir entegrasyon (ör. 'Telegram + OpenAI') için örnek arayanlar.

**Örnek — Yerelde çalıştırma (README'den birebir):**

```text
docker run -p 8000:8000 zie619/n8n-workflows:latest
```
Bu alıntının lisans kaydı:

- Kaynak depo: `Zie619/n8n-workflows`
- Tam dosya (commit'e sabit): https://github.com/Zie619/n8n-workflows/blob/94007c1445d9258a7da116646b79473e7c7c3282/README.md
- Commit: `94007c1445d9258a7da116646b79473e7c7c3282` (2026-06-24T17:16:03+03:00) · erişim: 2026-10-03
- Sahip: Copyright (c) 2025 Zie619
- Lisans: **MIT** · belge: `lisanslar/n8n-workflows_LICENSE`
- Yeniden dağıtım: MIT: kopyalama/dağıtım/ticari kullanım serbest; telif bildirimi ve MIT izin metni her kopyada bulunmalı (bkz. lisans belgesi).

**Nasıl kullanılır:**

1. Web arayüzünde ara.
2. JSON'u indir; n8n'de 'Import from file' ile aç.
3. Kimlik bilgilerini bağla.

**Ücret:** Ücretsiz.  
**Topluluk / not:** 56 bin+ yıldız. (https://github.com/Zie619/n8n-workflows)  
**Dikkat:** Topluluk derlemesi; şablonları çalıştırmadan önce incele.

### Dify Marketplace — hazır AI uygulama şablonları

**Link:** https://marketplace.dify.ai/  
**Tür:** Şablon + eklenti + ajan stratejisi pazarı  
**Durum:** ⭐ 157.760 · HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Şablon lisansı belirtilmemiş; Dify kodu değiştirilmiş Apache-2.0.

**Ne işe yarar:** Tek tıkla Dify'da açılan hazır uygulamalar: 'Daily AI News Digest', 'Competitive Landscape Intelligence', 'AI Technical Research Assistant' gibi. Ayrıca araç/model eklentileri ve MCP destekli ajan stratejileri.

**Kime göre:** Kod yazmadan RAG/ajan uygulaması kurmak isteyenler.

**Ne bulursun (özgün açıklama)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Tek tıkla açılan hazır uygulamalar: günlük AI haber özeti, rakip analizi, teknik araştırma asistanı gibi. Bunların yanında eklentiler ve ajan stratejileri.
```
Neden kopyalanmadı: Marketplace şablonlarının lisansı tek tek belirtilmemiş: bilinmiyor. Dify kodu değiştirilmiş Apache-2.0. · Kaynak: https://marketplace.dify.ai/

**Nasıl kullanılır:**

1. Marketplace'te 'Templates' sekmesine gir.
2. Şablonu Dify'da aç (cloud veya self-host).
3. Model sağlayıcı anahtarını ekle, prompt düğümlerini düzenle, yayımla.

**Ücret:** Dify açık kaynak (self-host) + ücretli cloud.  
**Topluluk / not:** Dify deposu 157 bin+ yıldız. (https://github.com/langgenius/dify)  
**Dikkat:** Model API anahtarı gerekir.

### CrewAI Examples

**Link:** https://github.com/crewAIInc/crewAI-examples  
**Tür:** Kod örnekleri: çok-ajanlı 'ekip' (crew) tarifleri  
**Durum:** ⭐ 6.152 · kontrol 2026-10-02  
**İçerik lisansı:** Lisans bilinmiyor — Lisans dosyası yok.

**Ne işe yarar:** Pazarlama stratejisi, gezi planlayıcı, hisse analizi, işe alım gibi işleri rol-görev tanımlı ajan ekipleriyle yapan örnek projeler.

**Kime göre:** Python bilen ve kendi ajan ekibini kodla kurmak isteyenler.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Örnek klasörü klonla → agents.yaml / tasks.yaml içindeki rol ve görev metinlerini değiştir → çalıştır.
```
Neden kopyalanmadı: Lisans dosyası yok. · Kaynak: https://github.com/crewAIInc/crewAI-examples

**Nasıl kullanılır:**

1. Depoyu klonla, bir örnek klasörü seç.
2. README'deki kurulum adımlarını uygula, API anahtarını .env'e koy.
3. Rol/görev metinlerini kendi işine göre uyarla.

**Ücret:** Ücretsiz.  
**Topluluk / not:** 6 bin yıldız; depo ARŞİVLENMİŞ (salt okunur) görünüyor — güncel örnekler için CrewAI belgelerine bak. (https://github.com/crewAIInc/crewAI-examples)  
**Dikkat:** Arşivli: yeni CrewAI sürümleriyle bazı örnekler çalışmayabilir.

<a id="gorsel"></a>

## Görsel ve video prompt galerileri

Beğendiğin görselin prompt'unu kopyalayıp kendi görsel modelinde kullanabildiğin galeriler.

### PromptHero

**Link:** https://prompthero.com  
**Tür:** Görsel/video prompt arama motoru  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Kullanıcı içerikleri; lisans belirtilmemiş.

**Ne işe yarar:** Midjourney, Stable Diffusion, FLUX, Sora, ChatGPT Image, Nano Banana, Veo gibi modellerle üretilmiş görsellerin prompt'larını arar ve kopyalarsın.

**Kime göre:** Görsel/video üretenler, tasarımcılar, sosyal medya içerik üreticileri.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Aramaya 'product photo, studio lighting' yaz → beğendiğin görseli aç → prompt'u kopyala → kendi modelinde konu kelimesini değiştir.
```
Neden kopyalanmadı: Kullanıcı içerikleri; lisans belirtilmemiş. · Kaynak: https://prompthero.com

**Nasıl kullanılır:**

1. prompthero.com'da model ve konu ara.
2. Görseli aç, prompt ve ayarları gör.
3. Kopyalayıp kendi aracında dene.

**Ücret:** Ücretsiz gezinme + ücretli planlar.  
**Topluluk / not:** Görsel prompt kütüphanelerinde en sık önerilenlerden. (https://prompthero.com)  
**Dikkat:** Aynı prompt farklı model sürümünde farklı sonuç verir.

### Lexica

**Link:** https://lexica.art  
**Tür:** Görsel arama + üretim  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Kullanıcı içerikleri; lisans belirtilmemiş.

**Ne işe yarar:** Milyonlarca AI görselini prompt metadata'sıyla birlikte arar; kendi Aperture modeliyle üretim de yapar.

**Kime göre:** Stil keşfi yapan görsel üreticiler.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Arama → görseli aç → prompt'u kopyala → Lexica'da veya başka modelde üret.
```
Neden kopyalanmadı: Kullanıcı içerikleri; lisans belirtilmemiş. · Kaynak: https://lexica.art

**Nasıl kullanılır:**

1. lexica.art'ta stil/konu ara.
2. Prompt'u kopyala ve uyarla.

**Ücret:** Arama ücretsiz; üretim aylık plan (incelemelere göre 10–60 USD).  
**Topluluk / not:** Stable Diffusion döneminin ilk prompt arama motoru. Capterra sayfası otomatik kontrolde 403 verdi; fiyat bilgisi arama sonucu özetinden, doğrulanmadı. (https://www.capterra.com/p/10015083/Lexica/)  
**Dikkat:** Arşivin bir kısmı eski SD modellerine ait.

### Civitai

**Link:** https://civitai.com  
**Tür:** Açık görsel model + görsel topluluğu  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Kullanıcı içerikleri; model lisansları tek tek farklı.

**Ne işe yarar:** Topluluk görselleri ve bunları üreten modeller (checkpoint, LoRA). Görsel sayfalarında üretim bilgileri (prompt, ayarlar) sıklıkla paylaşılır.

**Kime göre:** Stable Diffusion / açık modellerle yerelde üretim yapanlar.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Görseli aç → paylaşılmışsa üretim bilgisini kopyala → aynı modeli indirip kendi arayüzünde (ComfyUI vb.) kullan.
```
Neden kopyalanmadı: Kullanıcı içerikleri; model lisansları tek tek farklı. · Kaynak: https://civitai.com

**Nasıl kullanılır:**

1. Görsel veya model ara.
2. Modeli ve önerilen prompt/ayarları al.
3. Yerel arayüzünde dene.

**Ücret:** Ücretsiz gezinme; site içi üretim kredili. Model lisansları tek tek farklı.  
**Topluluk / not:** Açık görsel modellerin en büyük topluluğu; ana sayfa içeriği otomatik okuyucuyla sınırlı görüldü. (https://civitai.com)  
**Dikkat:** Yetişkin içerik filtresi ayarını kontrol et; model lisansını ticari kullanım öncesi oku.

<a id="rehber"></a>

## Öğrenme: resmi prompt rehberleri ve prompt hub'ları

Kendi sistem prompt'unu yazarken model üreticilerinin resmi kalıplarını görmek için.

### Claude prompting best practices (resmi)

**Link:** https://platform.claude.com/docs/en/build-with-claude/prompt-engineering/claude-prompting-best-practices  
**Tür:** Resmi belge  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Anthropic belgesi; içerik lisansı belirtilmemiş.

**Ne işe yarar:** Claude için uzun sistem prompt'u yazma kuralları: açık talimat, bağlam/neden açıklama, örnek verme, XML etiketleriyle bölümleme, rol verme, ajan senaryoları.

**Kime göre:** Claude'a 'tekte tam prompt' veren herkes.

**Not** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Eski docs.anthropic.com/.../prompt-library adresi 3 Ekim 2026'da bu sayfaya yönleniyor (curl ile doğrulandı).
```
Neden kopyalanmadı: Anthropic belgesi; içerik lisansı belirtilmemiş. · Kaynak: https://docs.anthropic.com/en/resources/prompt-library/library

**Nasıl kullanılır:**

1. Sayfayı oku; kendi prompt'unu bölüm bölüm (rol, bağlam, görev, kurallar, çıktı biçimi) kontrol et.

**Ücret:** Ücretsiz.  
**Topluluk / not:** Anthropic yeni modellerde kısa, hedef odaklı prompt'ların uzun kural yığınlarından iyi çalıştığını vurguluyor (haberlere yansıyan Claude Code sistem prompt'u kısaltması). (https://aiweekly.co/alerts/anthropic-deletes-80-of-claude-codes-system-prompt-for-claude-5)  
**Dikkat:** Model sürümüne göre öneriler değişir; güncel sayfaya bak.

### OpenAI Cookbook (prompting rehberleri)

**Link:** https://developers.openai.com/cookbook  
**Tür:** Resmi örnek ve rehber koleksiyonu  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Site içerik lisansı bu çalışmada doğrulanmadı.

**Ne işe yarar:** Model bazlı prompting rehberleri (GPT-5 serisi), ajan iş akışları, örnek sistem prompt'ları ve kod defterleri.

**Kime göre:** API ile kendi sistemini kuranlar.

**Not** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
cookbook.openai.com artık developers.openai.com/cookbook adresine yönleniyor (3 Ekim 2026 kontrolü).
```
Neden kopyalanmadı: Site içerik lisansı bu çalışmada doğrulanmadı. · Kaynak: https://cookbook.openai.com

**Nasıl kullanılır:**

1. Model adına göre 'prompting guide' ara.
2. Örnek sistem prompt'unu kendi işine uyarla.

**Ücret:** Ücretsiz.  
**Topluluk / not:** Topluluk özetleri: 'belirsizlik bir hata; kısa ve sonuç odaklı prompt'lar kazanıyor'. (https://www.atlabs.ai/blog/gpt-5.2-prompting-guide-the-2026-playbook-for-developers-agents)  
**Dikkat:** Teknik içerik.

### Prompt Engineering Guide (DAIR.AI)

**Link:** https://www.promptingguide.ai  
**Tür:** Açık rehber + örnek prompt'lar  
**Durum:** ⭐ 78.799 · HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Açık lisans (CC0/MIT) — Rehber deposu MIT (DAIR.AI).

**Ne işe yarar:** Teknikler (few-shot, chain-of-thought, ReAct...), uygulamalar, ajanlar, RAG, bağlam mühendisliği; her teknikte örnek prompt.

**Kime göre:** Prompt yazmayı sistematik öğrenmek isteyenler.

**Kullanım** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
Techniques → bir teknik seç → örnek prompt'u kendi probleminle dene.
```
Neden kopyalanmadı: Rehber deposu MIT (DAIR.AI). · Kaynak: https://www.promptingguide.ai

**Nasıl kullanılır:**

1. Siteyi aç, Techniques veya Prompt Hub bölümüne gir.
2. Örnekleri kendi görevine uyarla.

**Ücret:** Ücretsiz.  
**Topluluk / not:** Deposu 78 bin+ yıldız. (https://github.com/dair-ai/Prompt-Engineering-Guide)  
**Dikkat:** Bir kısmı akademik; hızlı hazır prompt için uygun değil.

### LangChain / LangSmith Prompt Hub

**Link:** https://smith.langchain.com/hub  
**Tür:** Geliştirici prompt hub'ı (herkese açık prompt'lar)  
**Durum:** HTTP 200 · kontrol 2026-10-03  
**İçerik lisansı:** Lisans bilinmiyor — Kullanıcı prompt'ları doğrulanmamış; lisans tek tek belirtilmemiş.

**Ne işe yarar:** Topluluğun yayımladığı prompt'ları ara, kendi çalışma alanına çatalla (fork), kodda adıyla çek. RAG, ajan, değerlendirme prompt'ları yaygın.

**Kime göre:** LangChain/LangSmith ile uygulama geliştirenler.

**Kodda kullanım (özgün genel biçim)** — *kaynağın metni kopyalanmadı, açıklama bizim:*

```text
prompt = client.pull_prompt("<prompt-adi>:<etiket-veya-commit>")
```
Neden kopyalanmadı: LangChain belge sayfasında içerik lisansı belirtilmemiş: bilinmiyor. Bu yüzden belgedeki örneği değil, genel biçimi yazdık. · Kaynak: https://smith.langchain.com/hub

**Nasıl kullanılır:**

1. LangSmith'te Prompts bölümünden herkese açık LangChain Hub listesine geç.
2. Prompt'u ara, kendi çalışma alanına çatalla (fork).
3. Kodda client.pull_prompt ile adıyla çek (LangSmith hesabı/anahtarı gerekir).

**Ücret:** Ücretsiz katman mevcut; hesap gerekir.  
**Topluluk / not:** Belge uyarısı: herkese açık prompt'lar kullanıcı üretimi ve doğrulanmamıştır. (https://docs.langchain.com/langsmith/manage-prompts)  
**Dikkat:** Hub sayfası giriş/JS gerektirdiği için içerik bu oturumda listelenemedi.

## Sınırlar ve dürüstlük notu

- **Google arayüzünde araştırma yapılmadı.** Ortamın kendi web arama aracı (WebSearch) ve doğrudan sayfa/dosya indirme kullanıldı; gerçek Google arama arayüzü kullanılmadı.
- **ChatGPT Pro ile ortak araştırma yapılmadı:** Bu bulut oturumunun senin ChatGPT hesabına erişimi yok. Aşağıdaki prompt'u ChatGPT Pro'ya yapıştırarak listeyi ikinci bir gözle kontrol ettirebilirsin.
- **Bot korumasına takılan 4 adres** (promptbase.com 403, flowgpt.com 403, cursor.directory 429, bir Capterra sayfası 403): bu oturumda içerikleri doğrulanamadı; normal bir tarayıcıda açılıp açılmadıkları test edilmedi, garanti verilmez.
- Giriş isteyen galeriler (Microsoft Copilot Prompt Gallery, LangSmith Hub, GPT Mağazası) içerik olarak açılamadı; resmi belge/destek sayfalarından tarif edildi.
- **Lisans:** Bir deponun public olması veya kaynak linki verilmesi yeniden dağıtım izni değildir. Metni yalnız CC0/MIT kaynaklardan aldık; MIT kopyalarında telif bildirimi + izin metni `lisanslar/` ve `NOTICE.md` içinde. Bu bir hukuki görüş değildir.
- Yıldız sayıları ve HTTP durumları 3 Ekim 2026 tarihlidir; zamanla değişir.

### ChatGPT Pro'ya sorulacak hazır doğrulama prompt'u

```text
Aşağıdaki listede hazır prompt siteleri, hazır ajan/sistem paketleri ve otomasyon şablon galerileri var.
1) Her birinin bugün hâlâ aktif olup olmadığını kontrol et ve kaynak linki ver.
2) Bu listede OLMAYAN ama aynı amaçla (gidip hazır prompt/sistem bulup kullanmak) çok kullanılan 5 kaynak öner; her biri için HTTPS link, ne işe yaradığı ve ücretsiz mi olduğu.
3) Türkçe içerik sunan hazır prompt kaynakları varsa ayrıca listele.
Uydurma link verme; emin olmadığını belirt.

LİSTE:
- prompts.chat (eski adı Awesome ChatGPT Prompts): https://prompts.chat
- ChatGPT GPT Mağazası (Explore GPTs): https://chatgpt.com/gpts
- ai-boost/awesome-prompts — en iyi GPT'lerin prompt'ları: https://github.com/ai-boost/awesome-prompts
- FlowGPT: https://flowgpt.com
- AIPRM (tarayıcı eklentisi): https://www.aiprm.com
- PromptBase (prompt pazaryeri): https://promptbase.com
- Poe (hazır botlar): https://poe.com/explore
- OpenAI Academy — 'ChatGPT for any role' prompt paketleri: https://academy.openai.com/public/clubs/work-users-ynjqu/resources/chatgpt-for-any-role
- Claude Academy — Use cases (kullanım senaryoları): https://academy.claude.com/use-cases
- Google — Gemini for Workspace prompt rehberi: https://workspace.google.com/learning/content/gemini-prompt-guide
- Microsoft Copilot Prompt Gallery: https://copilot.cloud.microsoft/prompts
- Fabric (Daniel Miessler) — 250+ hazır 'pattern': https://github.com/danielmiessler/Fabric
- The Agency (msitarzewski/agency-agents) — hazır AI ajans kadrosu: https://github.com/msitarzewski/agency-agents
- Superpowers (obra) — kodlama ajanı için hazır çalışma yöntemi: https://github.com/obra/superpowers
- BMAD-METHOD — çevik (agile) AI geliştirme sistemi: https://github.com/bmad-code-org/BMAD-METHOD
- GitHub Spec Kit — şartname odaklı geliştirme (SDD): https://github.com/github/spec-kit
- wshobson/agents — çok araçlı ajan eklenti pazarı: https://github.com/wshobson/agents
- VoltAgent/awesome-claude-code-subagents — 100+ alt ajan: https://github.com/VoltAgent/awesome-claude-code-subagents
- Anthropic Agent Skills (anthropics/skills): https://github.com/anthropics/skills
- GitHub awesome-copilot: https://github.com/github/awesome-copilot
- awesome-cursorrules (PatrickJS): https://github.com/PatrickJS/awesome-cursorrules
- awesome-claude-code (hesreallyhim): https://github.com/hesreallyhim/awesome-claude-code
- system-prompts-and-models-of-ai-tools (x1xhlol): https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools
- CL4R1T4S (elder-plinius): https://github.com/elder-plinius/CL4R1T4S
- leaked-system-prompts (jujumilk3): https://github.com/jujumilk3/leaked-system-prompts
- n8n Workflow Templates (resmi): https://n8n.io/workflows/
- Zie619/n8n-workflows — aranabilir n8n arşivi: https://zie619.github.io/n8n-workflows
- Dify Marketplace — hazır AI uygulama şablonları: https://marketplace.dify.ai/
- CrewAI Examples: https://github.com/crewAIInc/crewAI-examples
- PromptHero: https://prompthero.com
- Lexica: https://lexica.art
- Civitai: https://civitai.com
- Claude prompting best practices (resmi): https://platform.claude.com/docs/en/build-with-claude/prompt-engineering/claude-prompting-best-practices
- OpenAI Cookbook (prompting rehberleri): https://developers.openai.com/cookbook
- Prompt Engineering Guide (DAIR.AI): https://www.promptingguide.ai
- LangChain / LangSmith Prompt Hub: https://smith.langchain.com/hub
```
