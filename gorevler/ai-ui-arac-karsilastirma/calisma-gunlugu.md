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
