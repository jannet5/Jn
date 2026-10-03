# Çalışma günlüğü — Hazır AI sistem ve prompt kataloğu

Tarih: 2026-10-03 · Ortam: Claude Code bulut oturumu (Linux konteyner, Opus 5.5 yapılandırması; gerçek
model/çaba UI seçimini bu günlük kanıtlamaz) · Dal: `claude/youthful-rubin-7bjeo4`

> Gizlilik: Kullanıcının tam sohbeti, `kaynak.txt` ve `gorev.md` **bu public depoya konmadı**; yalnız
> özel çalışma alanında ve özel ZIP içinde duruyor. Aşağıda istek kendi cümlelerimle özetlendi.

## 1. Ne istendi?

- Kaynak 27 satır, baştan sona okundu; görev özetindeki satır numaralarıyla (3, 5, 7, 15, 17) uyumlu.
- İlk istek "insanlar AI'ya nasıl sistemler kurdurmuş, nasıl uzun prompt yazmışlar" araştırmasıydı.
- **Son kullanıcı düzeltmesi (satır 15, 17) önceliklidir:** Kullanıcı bunu kastetmediğini, asıl
  isteğin *hazır template prompt'lar ve hazır sistem tarifleri olan siteler/sistemler* olduğunu,
  oraya gidip "bunu kullanayım" diyebilmek istediğini, her alan/kategori olabileceğini söyledi.
- Satır 19-25: kullanıcı konudan koptuğunu, amacın açıkça anlatılmasını istedi → README'nin ilk
  cümlesi ve `KATALOG.md` başındaki "Hangisiyle başlamalıyım?" tablosu bu yüzden var.
- Satır 3, 7, 11: ChatGPT Pro hesabıyla birlikte araştırma istendi → bkz. Sorun 1.

Kabul ölçütleri: K1 hazır kurulmuş sistemler, K2 her örnekte prompt + kullanım yolu, K3 kategori ve
somut fayda. Eşleme `harita.md` §1'de.

## 2. Kullanılan gerçek araçlar ve komutlar (sırayla)

1. `cat > ozel/kaynak.txt` + `grep -n .` — kaynağı özel alana yazıp satır numaralarını doğruladım.
2. **WebSearch** (ortamın arama aracı; Google API'si değil) — sorgular: prompt kütüphaneleri 2026,
   prompts.chat yıldız, Fabric pattern'leri, Reddit r/PromptEngineering kütüphane yorumları,
   Superpowers/agency-agents incelemeleri, PromptBase/AIPRM görüşleri, Anthropic prompt library
   durumu, Microsoft Copilot Prompt Gallery, OpenAI cookbook, Lexica/Civitai, Gemini prompt rehberi.
3. `curl -sL -w '%{http_code}'` — 25 aday sitenin canlı durum kodu ve yönlendirmeleri.
4. **GitHub MCP `search_repositories`** — 20 deponun yıldız, güncellik, arşiv durumu (tek sorguda).
5. `curl raw.githubusercontent.com/...` — 19 README + örnek ajan/skill/pattern dosyaları birebir indirildi.
6. **WebFetch** — prompts.chat, Claude Academy, OpenAI Academy, n8n, Dify, FlowGPT, AIPRM, Poe,
   Civitai, PromptHero, LangSmith belgeleri, x1xhlol klasör listesi.
7. `pdftotext` — Google Workspace prompt rehberi PDF'inden örnek prompt birebir alındı.
8. `python3 araclar/secki_cek.py` → `python3 araclar/olustur.py` → `python3 araclar/dogrula.py` →
   `node araclar/ekran-testi.mjs` (Playwright 1.56.1 + önyüklü Chromium).
9. `git commit/push` + `git show origin/...` ile geri okuma; `zip` + `sha256sum` + `unzip` + `diff -r`.

## 3. Kaynaklar (birincil)

Tam liste ve her birinin canlı durumu: `dogrulama/link-raporu.md` (75 benzersiz HTTPS link).
Öne çıkanlar:
- https://prompts.chat · https://raw.githubusercontent.com/f/prompts.chat/main/prompts.csv (CC0)
- https://github.com/danielmiessler/Fabric · https://github.com/msitarzewski/agency-agents
- https://github.com/obra/superpowers · https://github.com/anthropics/skills · https://github.com/github/spec-kit
- https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools
- https://academy.openai.com/public/clubs/work-users-ynjqu/resources/chatgpt-for-any-role
- https://academy.claude.com/use-cases
- https://services.google.com/fh/files/misc/google_workspace_prompting_guide_abridged_smbs_startups_september2024.pdf
- https://support.microsoft.com/microsoft-365-copilot/share-your-best-prompts-with-others
- https://n8n.io/workflows/ · https://marketplace.dify.ai/

Topluluk/kullanıcı deneyimi kaynakları (WebSearch sonuç özetlerinden; Reddit sayfaları ayrıca açılmadı):
- Reddit kaynaklı tartışma özetleri: kütüphanelerin "genel şablon yığını" olup unutulması, görsel
  prompt'larda yapı eksikliği — https://reddit.sentinel-team.org/posts/1tmgbgl/snapshots/2026-05-29T18%3A03%3A22.19689Z
- agency-agents'ın Reddit gönderisinden doğuşu — https://flaviocopes.com/agency-agents/
- Superpowers incelemesi — https://www.mejba.me/superpowers-plugin-claude-code-review
- Pazaryeri karşılaştırması (ticari blog, temkinle) — https://godofprompt.ai/blog/in-depth-review-leading-ai-prompt-marketplaces/

## 4. Kararlar

- **Yol A seçildi** (statik, çevrimdışı HTML + MD + JSON). B (Sheets/Notion: kullanıcının hesabına
  yazmak gerekir, istenmedi) ve C (prompts.chat self-host: sunucu kurmak "hayali ürün" olur) reddedildi.
  Ayrıntı `harita.md` §3.
- **Tek doğruluk kaynağı** `veri/katalog.json`; HTML ve MD betikle üretiliyor → biçimler arası tutarsızlık olmaz.
- **Birebirlik ilkesi:** "birebir" etiketli her örnek, kaynak dosyada geçtiği betikle kanıtlanmak
  zorunda. Kanıtlayamadığım yerde örnek metni *kullanım tarifi* olarak etiketledim, alıntı gibi sunmadım.
- **Tam metin yalnız serbest lisanslılardan:** prompts.chat (CC0) 30 prompt tam; Fabric extract_wisdom
  (MIT) tam; diğerlerinden kısa alıntı.
- Ticari SEO blogları (God of Prompt, SurePrompts) katalogda ayrı girdi yapılmadı: kendi ürünlerini
  öven, doğrulanamayan sayılar içeriyorlar; yalnız karşılaştırma kaynağı olarak anıldı.
- Türkçe kullanım için her prompt'a "Yanıtlarını Türkçe ver." ekleme önerisi ve kullanıcının "tekte tam
  prompt" sistemi için **Türkçe ana şablon** eklendi (bizim derlememiz olduğu açıkça yazıldı).

## 5. Sorunlar ve çözümler

1. **ChatGPT Pro ile ortak araştırma yapılamadı.** Bulut oturumunun kullanıcının ChatGPT hesabına
   erişimi yok; kimlik bilgisi istemek/uydurmak uygun değil. Çözüm: bağımsız araştırma tamamlandı;
   `KATALOG.md` sonuna ChatGPT Pro'ya yapıştırılacak hazır doğrulama prompt'u eklendi. **Açık engel.**
2. **github.com HTML ve api.github.com proxy'de 403.** Çözüm: raw.githubusercontent.com + GitHub MCP
   arama. GitHub MCP dosya okuma bu oturumda yalnız `jannet5/jn` ile sınırlı olduğu için kullanılmadı;
   başka depo eklenmedi.
3. **Eski Anthropic Prompt Library adresi** artık best-practices sayfasına yönleniyor (curl kanıtı) →
   katalogda yerine Claude Academy use-cases ve best-practices girdileri kondu.
4. **Bot korumaları:** promptbase.com 403, cursor.directory 429, flowgpt.com bir kontrolde 200, sonrakinde
   403. Girdilerde "tarayıcıda açılır, bu oturumda içerik doğrulanamadı" diye işaretlendi.
5. **Giriş gerektiren galeriler:** Microsoft Copilot Prompt Gallery, LangSmith Hub, GPT Mağazası içeriği
   görülemedi → resmi destek/belge sayfalarından tarif, "giriş gerekir" notu.
6. **Gemini rehber sayfası** otomatik okuyucuda kesildi → resmi PDF indirildi, örnek prompt oradan birebir alındı.
7. **Doğrulama betiğindeki yanlış negatif:** ilk sürüm canlı CSV'de ham metin aradı, `""` kaçışları
   yüzünden 28/30 dedi. CSV ayrıştırmaya çevrildi → 30/30. İki örnek alıntı (ai-boost, LangSmith)
   ilk denemede kaynakta bulunamadı; birebir metinle/doğru `.md` kaynağıyla düzeltildi → 16/16.

## 6. Doğrulamalar (gerçek çalıştırma sonuçları)

- `python3 araclar/dogrula.py` → `şema hatası: 0 | link: 71/75 erişildi | ana: 34/36 | birebir: 16/16 |
  seçki: True | GENEL: GEÇTİ` (engelli 4 link: cursor.directory 429, flowgpt.com 403, promptbase.com 403,
  capterra 403 — hepsi bot koruması).
- Seçki: CSV anlık görüntüsü SHA-256 `c506bbf2…964c6`; bugün indirilen canlı CSV ile birebir aynı;
  30/30 prompt hem anlık görüntüde hem canlı CSV'de.
- `node araclar/ekran-testi.mjs` → 12/12 kontrol GEÇTİ: 36 kart, kategori filtresi, arama, HTTPS link,
  kopyala düğmesinin panoya gerçekten yazması, 30 prompt kartı, sayfadaki prompt'un kaynakla birebirliği,
  telefon genişliğinde (390 px) yatay kaydırma yok, JS hatası yok. Ekran görüntüleri gözle kontrol edildi
  (açık tema masaüstü, koyu tema telefon).
- **Yapılmayan testler:** Gerçek Windows bilgisayarda ve gerçek telefonda açma testi yapılmadı (Chromium
  masaüstü/telefon görünümü emülasyonu yapıldı). ChatGPT/Claude hesabında prompt'ların çalıştırılması
  yapılmadı (hesap erişimi yok).

## 7. Kalıcı teslim

Bu bölüm teslim adımında güncellendi — bkz. aşağıdaki "Teslim kaydı".

### Teslim kaydı
