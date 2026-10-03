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
  (MIT) tam. *(Sürüm 1'de diğerlerinden kısa alıntı vardı; sürüm 2'de lisansı CC0/MIT olmayan tüm alıntılar
  çıkarıldı — bkz. §9.)*
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
   403. *(Sürüm 1'de girdilerde "tarayıcıda açılır" deniyordu; bu doğrulanmamış bir garantiydi. Sürüm 2'de
   "bu oturumda doğrulanamadı; normal tarayıcıda açıldığı test edilmedi" olarak düzeltildi.)*
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

## 7. Kalıcı teslim — sürüm 1 (geçmiş kayıt; sürüm 2 teslimi §9'da)

Bu bölüm teslim adımında güncellendi — bkz. aşağıdaki "Teslim kaydı".

### Teslim kaydı

1. **Public dal push + geri okuma:** commit `d21d61427dbe75e3fdd46cdba4a14f4ce01f3229` →
   `origin/claude/youthful-rubin-7bjeo4`. `git fetch` sonrası yerel ve uzak HEAD aynı; GitHub MCP ile
   klasör listesi uzaktan okundu (9 öğe, 26 dosya). Bu commit'te yalnız araştırma çıktısı var; özel
   kaynaklar yok (dosya adı geçişleri dışında `grep` ile kontrol edildi).
2. **Özel ZIP (bulut özel çalışma alanında, depoya girmez; sürüm 2'de yerine temiz ürün ZIP'i geldi):** `hazir-ai-sistem-ve-prompt-katalogu.zip`
   - SHA-256: `73728aa5a6b9ef9d4879cf77886c56fb7a576c35ec866a99fb4c2e7d0f74c4bf`
   - İçerik: tüm ürün dosyaları + `_ozel/` (kaynak.txt, gorev.md, `repo-dal.bundle`) + `MANIFEST.sha256` (30 dosya)
   - Geri okuma: `sha256sum -c` OK → açıldı → manifest 30/30 OK → ürün dosyaları repo ile `diff -r`
     birebir aynı → bundle'dan `git clone` HEAD `d21d614…` ve klasör repo ile aynı → açılan kopyada
     Chromium testi yeniden GEÇTİ.
   - Not: Bu günlük girdisi ZIP'ten *sonra* yazıldı; ZIP içindeki günlükte bu "Teslim kaydı" maddeleri yoktur.
3. **Özel Artifact sayfası:** https://claude.ai/artifact/BU3RnW9nUN8AN2yBoKKjyH (index.html; yalnız
   sahibi açabilir, paylaşım sayfanın Share menüsünden).

## 8. Kalanlar / sonraki adım

- Açık engel: ChatGPT Pro ile ikinci görüş — kullanıcı `KATALOG.md` sonundaki prompt'u kendi
  hesabında çalıştırabilir; sonuçlar gelirse `veri/katalog.json`'a eklenip
  `python3 araclar/olustur.py && python3 araclar/dogrula.py` ile yeniden üretilir.
- Windows/telefon gerçek cihaz testi yapılmadı (emülasyon yapıldı).

## 9. Sürüm 2 — bağımsız inceleme sonrası lisans düzeltmesi

### İstenen (inceleme bulguları, kendi cümlelerimle)
Uzak HEAD `5e3777df4576e110ce9f0b07a05652cbc35bb837` üzerindeki statik incelemede şunlar istendi:
1. Kopyalanan her prompt için kaynak, tam dosya/permalink, commit/tarih, sahip, lisans ve yeniden dağıtım sınırı alanları.
2. MIT tam metni ve telif bildirimi, CC0 dayanak belgesi teslimde, ilgili kopyalarla birlikte bulunsun.
3. Public depo ya da kaynak linki lisans izni sayılmasın. Ticari galeriler, kullanıcı metinleri ve sızdırılmış
   sistem prompt'ları serbest diye sunulmasın. Belirsiz lisanslı tam metin çıkarılsın, yerine özgün açıklama ve
   link konsun. Lisans bilinmiyorsa "bilinmiyor" yazılsın.
4. Filtre ve kopyalama bu sınırları korusun ve bu anlamlı biçimde test edilsin.
5. Google arayüzü araştırması ve ChatGPT Pro ortak araştırmasının yapılmadığı açıkça korunsun. Bot korumalı
   4 site için "normal tarayıcıda açılır" garantisi verilmesin.
6. Harita güncellensin. Temiz, ürün-köklü ZIP + SHA-256 ve uzak tam commit geri okuması verilsin. Başka projelerin
   geçmişi, ham sohbet ve özel dosyalar pakete girmesin.

### Yapılanlar ve gerçek komutlar
- `git ls-remote https://github.com/<depo> HEAD` → 20 kaynak deponun tam commit SHA'sı.
- `curl https://raw.githubusercontent.com/<depo>/<commit>/LICENSE*` → lisans dosyaları. Sonuçlar:
  - MIT: Fabric, agency-agents, superpowers, VoltAgent, BMAD, spec-kit, wshobson, awesome-copilot, Zie619, DAIR.
  - CC0: awesome-cursorrules; prompts.chat'in prompt içeriği (ikili lisans).
  - GPL-3.0 beyanı: ai-boost, x1xhlol. AGPL-3.0 beyanı: CL4R1T4S.
  - CC BY-NC-ND 4.0: awesome-claude-code. Değiştirilmiş Apache-2.0: Dify.
  - **Lisans dosyası yok:** jujumilk3, crewAI-examples ve anthropics/skills (kökte).
- `git fetch --depth 1 --filter=tree:0 <depo> <commit>` + `git log -1 --format=%cI` → commit tarihleri.
- Pakette tutulan 4 tam dosya ve prompts.csv, sabit commit'teki dosyalarla SHA-256 düzeyinde **aynı**.
- Lisansa göre sınıflandırma (36 kaynak): açık 11, kısıtlı 4, tescilli/üçüncü taraf 7, bilinmiyor 14.
- **Çıkarılan birebir metinler** (lisansı CC0/MIT değil):
  - Cursor sistem prompt'u alıntısı (tescilli; x1xhlol'ün GPL beyanı bu metne hak veremez).
  - ai-boost README'si (GPL-3.0).
  - jujumilk3 README cümlesi (lisans dosyası yok).
  - OpenAI Academy e-posta şablonu, Google Gemini PDF örneği, LangChain belge örneği (lisans belirtilmemiş).
  - Anthropic skills README şablonu ve cümlesi (kökte lisans yok).
  - Claude Academy ve Dify başlık listeleri.

  Hepsinin yerine Türkçe özgün açıklama ve link kondu; Anthropic skill biçimi için kendi örneğimizi yazdık.
- prompts.chat'in **tam CSV'si üründen çıkarıldı**. `secki_cek.py` CSV'yi sabit commit'ten indiriyor, beklenen
  SHA-256 değeriyle karşılaştırıyor ve her prompt'a `lisans_kaydi` alanlarını yazıyor.
- Yeni dosyalar:
  - `lisanslar/` (12 belge: 10 MIT, prompts.chat LICENSE + LICENSE-CC0, cursorrules CC0).
  - `NOTICE.md`: MIT telif bildirimleri ve izin metinleri tam.
  - `LISANS-ENVANTERI.md`.
  - `araclar/negatif_test.py`, `dogrulama/lisans-dogrulama.md`, `dogrulama/negatif-test.txt`.
- `index.html`:
  - Lisans filtresi eklendi: tümü / metni kopyalanabilir (CC0/MIT) / yalnız açıklama + link.
  - Kopyala düğmesi yalnız 11 birebir CC0/MIT örnekte var.
  - MIT kopyasına telif bildirimi, izin metni ve commit otomatik ekleniyor.
  - Diğer 25 kartta "Metin kopyalanmaz — kaynağa git" bağlantısı ve kopyalanmama nedeni gösteriliyor.
- Garanti ifadeleri düzeltildi:
  - Bot korumalı 4 adres için artık "test edilmedi, garanti yok" yazıyor.
  - "Google arayüzü kullanılmadı" ve "ChatGPT Pro ortak araştırması yapılmadı" notları
    `KATALOG.md`, `README.md` ve `harita.md`'de açıkça duruyor.

### Sorun ve çözümler
- `dogrula.py` CSV'yi okumadan önce alan sınırını yükseltmediği için 131 KB'lık satırda hata verdi. Sınır
  modül düzeyinde yükseltildi.
- Fabric kartında uzun commit SHA kutudan taştı (ekran görüntüsünde görüldü). CSS'e `overflow-wrap:anywhere` eklendi.
- Fabric LICENSE'ındaki telif satırı "Copyright (c) 2012-2024 Scott Chacon and others" şeklinde. Şablondan kalmış
  gibi görünse de değiştirmeden, dosyada yazdığı gibi aktarıldı.

### Doğrulamalar (bu oturumda yeniden çalıştırıldı)
- `python3 araclar/dogrula.py` → `şema hatası: 0 | lisans hatası: 0 | link: 66/70 erişildi | ana: 34/36 |
  birebir: 11/11 | seçki: True | GENEL: GEÇTİ`. Erişilemeyen 4 adres: cursor.directory 429, flowgpt.com 403,
  promptbase.com 403, Capterra 403 — bot koruması; normal tarayıcıda test edilmedi. Link sayısı 75'ten 70'e
  düştü, çünkü çıkarılan örneklerin kaynak adresleri artık girdinin kendi linkine işaret ediyor.
- `python3 araclar/negatif_test.py` → bilerek konan 4 ihlalin 4'ü de YAKALANDI: izinsiz lisans, eksik sahip
  alanı, tescilli içeriğin kopyalanabilir işaretlenmesi, yasaklı Cursor metni.
- `node araclar/ekran-testi.mjs` → 20/20 kontrol GEÇTİ. Lisansa özel 8 kontrol:
  - Kopyala düğmesi yalnız 11 CC0/MIT kartında var.
  - x1xhlol kartında düğme yok; "kopyalanmadı" notu var.
  - MIT kopyasında telif satırı, izin metni ve commit yer alıyor.
  - CC0 kopyası metni değiştirmeden aktarıyor.
  - Lisans filtresi doğru çalışıyor (11 kart + 25 kart, bunlarda 0 düğme).
  - Kategori ve lisans filtresi birlikte doğru çalışıyor.
  - 30 prompt kartının hepsinde lisans kaydı var.
- Push, uzak commit geri okuması ve ZIP'in SHA-256 değeri bu dosyanın içine yazılamaz (commit kendini içeremez).
  Bunlar teslim mesajında ve ZIP'in yanındaki `.sha256` dosyasında.

### Teslim — sürüm 2
- Ürün ZIP'i, uzak commit'ten `git archive <commit>:gorevler/hazir-ai-sistem-ve-prompt-katalogu` ile üretilir.
  ZIP'in kökü doğrudan ürün klasörüdür; içinde özel dosya, ham sohbet ya da başka projelerin geçmişi yoktur.
- Bir dosya kendi SHA-256 değerini içeremeyeceği için ZIP'in SHA-256 değeri ZIP'in yanındaki `.sha256` dosyasında
  ve teslim mesajındadır.

