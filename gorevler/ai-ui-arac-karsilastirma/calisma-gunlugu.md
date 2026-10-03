# Çalışma günlüğü — AI UI araçlarını gerçek denemelerle karşılaştırma

## 0. Ne istendi (özet, özel sohbet metni olmadan)
X'teki "ai ui" aramasında insanların kullandığı araçları çıkar, internette ne işe yaradıklarını araştır,
plan yap, araçları tek tek gerçekten dene, hangisinin iyi sonuç verdiğini karşılaştır; otomasyon varsa kullan.
Sonradan netleşen düzeltme: istenen şey bir **mobil uygulama/APK değil**, bir **sistem/iş akışı**.

## 1. Ortam doğrulaması
- Oturum `get_session` ile okundu: model `claude-opus-5-5`, çaba `medium`, ortam `anthropic_cloud`.
- Depo `jannet5/Jn` **public** (list_repos). Bu yüzden ham kaynak metni ve görev MD'si yalnız özel
  scratchpad'de (`.../scratchpad/ozel/`) tutuldu, `.gitignore` ile dışlandı, push edilmedi.
  SHA-256: kaynak.txt `fa7f7585…5811`, gorev.md `782bdfcb…255f`.
- Araçlar: Node 22.22.0, npm 10.9.4, Python 3.11, Playwright Chromium `/opt/pw-browsers`.
- API anahtarı / hesap: ortamda yok (env taraması). Uydurulmadı.

## 2. Araştırma
- `WebFetch https://x.com/search?q=ai%20ui...` → **HTTP 402**. Yol kırıldı → web araması + resmi repolar + HN.
- `curl reddit.com/search.json` → **403**. Yerine `hn.algolia.com/api/v1` ile HN hikâye ve yorumları okundu.
- Resmi doğrulama: frontend-design SKILL.md (anthropics/claude-code), DESIGN.md spec + CLI kaynak kodu
  (`git clone google-labs-code/design.md`), shadcn registry belgesi.
- Erişim yoklaması: `ui.shadcn.com/r/...dashboard-01.json` 200, `21st.dev/r/...` **403**, `v0.app` 200 (ama hesap gerekir).
- Ayrıntı: `arastirma.md`. Harita: `harita.md`.

## 3. Plan ve harita
`harita.md`: hesapsız denenebilen dört yol seçildi (A ham Claude Code, B frontend-design süreci, C Stitch DESIGN.md CLI,
D shadcn/ui registry). Ortak brief: `akis/brief.md` (kafe zinciri haftalık satış paneli, Türkçe, mobil, klavye).

## 4. Denemeler (gerçek araçlarla)
- **D** — komutlar `akis/tarifler/d-shadcn-statik-blok.md`. Sorunlar: `shadcn init` preset sorusunda takıldı → `-p nova` ile
  çözüldü; blok `tsc` strict'te `TS6133` (kullanılmayan import) ile derlenmedi → `noUnusedLocals:false`; `/avatars/shadcn.jpg` 404.
- **A, B, C** — her biri ayrı, temiz bağlamlı alt ajanla paralel üretildi (birbirinin çıktısını görmesin diye).
  Promptlar `akis/tarifler/`. C'de gerçek CLI: `npx @google/design.md@0.4.0 lint` (ilk: 0 hata/3 uyarı → son: 0/0) ve `export`.
  B'de skill süreci + Playwright ekran görüntüsüyle 2 eleştiri turu.

## 5. Değerlendirme otomasyonu ve karşılaşılan sorunlar
- `akis/degerlendir.mjs` + `olcutler.mjs` + `puan.mjs` + `rapor.mjs`; testler `akis/test/olcutler.test.mjs` (6/6 geçti).
- Sorun 1: Chromium, bulut proxy'sinin TLS sertifikasına güvenmediği için Google Fonts yüklenmedi
  (`ERR_CERT_AUTHORITY_INVALID`) → yalnız proxy varken `ignoreHTTPSErrors`.
- Sorun 2: ardından fontlarda ara sıra `ERR_TOO_MANY_RETRIES` (curl ile aynı URL 200) → dış istekleri `page.route` +
  `route.fetch` ile 4 kez deneyerek yükleme; iki ardışık koşu aynı puanı verdi.
- Sorun 3: klavye testi ekran görüntüsünden önce çalışıp görüntüye odak halkası bırakıyordu → sıra değişti.
- Sorun 4: HTML çubuk grafikler (B, C) ilk ölçütte "grafik yok" sayıldı → ölçüt genişletildi ve testle doğrulandı.
- Sorun 5: krem+terakota tespiti açık kremlerde gri eşiğine takıldı → ayrı HSL hesabı; testle doğrulandı.
- Eklenen kabul testi: "Geçen hafta"ya tıklayınca sayfa verisi değişiyor mu (A/B/C evet, D seçenek yok).

## 6. Sonuç
B 100, C 95, A 84, D 49. Ayrıntı ve sınırlar: `sonuc.md`. Ekran görüntüleri gözle de kontrol edildi (notlar `sonuc.md`'de).

## 7. Yapılamayanlar (nedenleriyle)
- X araması okunamadı (402); Reddit (403).
- Hesap/anahtar gerektiren araçlar denenmedi (v0, Lovable, Bolt, Stitch web, Figma Make, Magic Patterns, UX Pilot, 21st.dev Magic…).
- Windows / gerçek telefon / kullanıcı hesabı testi yapılmadı.
- Kaynaktaki eski dosya gönderme talimatları güncel yetki sayılmadı; bu iş zaten mobil uygulama değil, APK yok.

## 8. Kalıcı teslim ve doğrulama
- Dal `claude/affectionate-tesla-ii6gxy` push edildi (`710d48b`); `git ls-remote` ile uzak uç aynı commit.
- Geri okuma: dal temiz klonlandı → `npm ci`, `npm test` (6/6), `npm run karsilastir` aynı puanları verdi (84/100/95/49).
  Klonda özel kaynak metni yok (kaynak.txt/gorev.md ve özel ad/yol taraması boş).
- Özel ZIP: depo-hazır görev klasörü (node_modules hariç) + `ozel/` (tam kaynak.txt, gorev.md) + `SHA256SUMS.txt`.
  ZIP yalnız özel oturum dosyası olarak verildi, public depoya konmadı. ZIP'in kendi SHA-256'sı teslim mesajında.

## 9. Koordinatör denetimi sonrası düzeltme
- **Sorun:** önceki özel ZIP'teki `repo.bundle`, Jn dalının tüm geçmişini (12 commit; android-app/, windows-agent/,
  `android-app/keystore.properties`, `android-app/keystore/dev-release.jks` yolları) taşıyordu. Teslime uygun değildi.
  Bu dosyalar bu görevden önce Jn deposuna girmişti (ilk ekleyen commit `a61c086`, 2026-09-27); bu görevde açılmadı,
  okunmadı, değiştirilmedi.
- **Çözüm:** yalnız bu klasörün izlenen dosyaları yeni, bağımsız bir Git köküne kopyalandı; tek ürün commit'i ve
  yalnız onu içeren bundle üretildi. Eski Jn geçmişi paketlenmedi.
- **Kapsam dili düzeltildi:** C = Google'ın açık kaynak DESIGN.md CLI yaklaşımı (Stitch web hesabı değil);
  D = statik shadcn/ui bloğu, AI'sız (v0 hesabı değil). Klasörler buna göre yeniden adlandırıldı
  (`c-design-md-cli`, `d-shadcn-statik-blok`); D yeniden derlendi, tüm ölçüm yeniden koşuldu (84/100/95/49, değişmedi).
  `sonuc.md` ve `rapor.html` tek brief puanının genel araç sıralaması olmadığını açıkça yazıyor.
- Hesaplı araçlar (v0, Stitch web, Lovable, Bolt, Figma Make…): gerçek erişim yok → ölçülmedi, öyle bırakıldı.
