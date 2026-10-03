# Çalışma günlüğü — AI ile mobil uygulama üretim kaynak haritası

## 1. Ne istendi

Kullanıcı, bir mobil uygulamanın yapay zekâ ile baştan sona nasıl yaptırılacağını gösteren; hangi repoların, SDK'ların,
MCP'lerin, araçların ve sitelerin (HTTPS bağlantılarıyla) kullanılacağını, arayüzün neyle yaptırılacağını ve rakip
araştırmasının nasıl yapılacağını anlatan bir kaynak haritası istedi. Görev özeti üç zorunlu kabul maddesi koydu:
gerçek üretim örnekleri, HTTPS bağlantı kaydı, kaynakların hangi aşamada ne işe yaradığı.
Ayrıca: güncel web + resmî + topluluk araştırması, A/B/C yolları, kabul ölçütleri, push + geri okuma, özel ZIP + SHA-256.

Kullanıcının özel kaynak metni (kaynak.txt) ve görev özeti (gorev.md) **public depoya konmadı**; yalnız oturumun özel
çalışma alanında (scratchpad `ozel-girdi/`) ve özel ZIP içinde duruyor.

## 2. Ortam doğrulaması

- `get_session` aracı: oturum modeli `claude-opus-5-5`, çaba `medium`, ortam `anthropic_cloud` (Linux). Model/çaba
  seçimi bu araç çıktısıyla doğrulandı.
- Depo `jannet5/Jn`, dal `claude/awesome-brown-ep5d6t`. Çalışma yalnız `gorevler/ai-ile-mobil-uygulama-uretim-kaynak-haritasi/`
  altında; mevcut `android-app/`, `windows-agent/`, `docs/` değiştirilmedi.

## 3. Araştırma (sırayla)

1. WebSearch ile resmî kaynak taraması: Expo MCP/agents, Flutter/Dart MCP, mobile-mcp, Figma MCP, Android Studio Agent
   Mode, Xcode 26.3 agentic coding.
2. WebFetch ile birincil doğrulama: `docs.expo.dev/agents`, `docs.expo.dev/eas/ai/mcp/`, `docs.flutter.dev/ai/mcp-server`,
   `developer.android.com/studio/gemini/add-mcp-server`, Apple `giving-external-agents-access-to-xcode.md`
   (ilk denenen Apple URL'si 404 verdi → arama ile doğru URL bulundu), Figma MCP rehberi, RevenueCat, Maestro, Sentry,
   AppTweak MCP sayfaları, mobile-mcp README.
3. Rakip araştırması araçları: AppTweak/Sensor Tower/Appfigures karşılaştırmaları, scraper kütüphaneleri, Mobbin MCP,
   ASO Skills.
4. Gerçek üretim örnekleri: Expo blogundan FERASET, CineMe, "agent fixes bugs" akışı; VGV Maestro yazısı; app-maker
   deposu. CineMe'nin mağazada gerçekten olduğu iTunes Lookup API ile doğrulandı.
5. Topluluk: Reddit bu ortamın arama aracına kapalı (HTTP 400 "not accessible") → DEV/Medium, Apple Developer Forums,
   açık kaynak README'ler, Expo blog kullanıldı. "Tom Wentworth" ve "Gramms" iddiaları birincil kaynakla doğrulanamadığı
   için haritaya alınmadı.

## 4. Kararlar

- **Önerilen yol A (Expo/React Native):** resmî Claude Code eklentisi + resmî uzak MCP + bulut iOS build; iki gerçek
  üretim vakası. B (Flutter) ve C (Native) koşullarıyla birlikte yazıldı; D (Rork vb.) yalnız kaçış/prototip yolu.
- Bu bir araştırma görevi olduğu için hayali uygulama inşa edilmedi. Bunun yerine haritanın Aşama 1'i (rakip araştırması)
  için **çalıştırılabilir bir araç** yazıldı ve gerçek veriyle çalıştırıldı.

## 5. Uygulama ve komutlar

- `rakip-arastirma/rakip.mjs` + `package.json` (app-store-scraper 0.18.0, google-play-scraper 10.1.3).
- `npm install` → `node rakip.mjs --terim "habit tracker" --ulke tr --dil tr --adet 3 --yorum 100`
  → 6 rakip, 354 yorum, 42 düşük puanlı yorum; çıktı `rakip-arastirma/cikti/habit-tracker-tr/`.
- Argümansız çalıştırma → anlaşılır hata ve çıkış kodu 2 (girdi doğrulaması test edildi).
- `npm view` ile 11 paketin varlığı ve sürümü: `kanit/npm-paket-kontrol.tsv`.

## 6. Sorunlar ve çözümler

| Sorun | Çözüm |
|---|---|
| `github.com` HTML ve `api.github.com` istekleri proxy'de 403 | Depolar `git ls-remote` ile doğrulandı (git proxy açık) |
| Bazı siteler (appfigures, lovable, producthunt, g2, medium) 403 | "bot engeli" olarak ayrı işaretlendi, arama sonuçlarıyla varlıkları görüldü |
| MCP uç noktaları 401/405 | Beklenen davranış (OAuth/POST); "OK-MCP" olarak sınıflandı |
| posthog bir çalıştırmada zaman aşımı (000) | Betiğe 3 deneme eklendi; sonra 200 |
| Apple doküman URL'si 404 | Doğru `.../giving-external-agents-access-to-xcode` adresi bulundu |

## 7. Doğrulamalar

- `bash kanit/link-kontrol.sh` → harita.md'deki 87 HTTPS bağlantı: OK=82 (4'ü canlı MCP uç noktası), BOT-ENGELİ=5, HATA=0.
  Sonuç: `kanit/link-kontrol.md`.
- Rakip betiği gerçek veriyle başarıyla çalıştı (yukarıda).
- Push sonrası uzak daldan geri okuma ve özel ZIP SHA-256 doğrulaması: §8.

## 8. Teslim

- İlk teslim commit'i `168b653` → `origin/claude/awesome-brown-ep5d6t` push edildi; yerel ve uzak HEAD aynı.
- Geri okuma: dal temiz bir klona (`git clone --depth 1`) indirildi, görev klasörü `diff -r` ile yereldekiyle
  **birebir aynı** çıktı; özel `kaynak.txt`/`gorev.md` public depoda yok.
- Özel ZIP: oturumun özel çalışma alanında (scratchpad) `ai-mobil-kaynak-haritasi-teslim.zip` olarak üretildi; içinde
  görev klasörü, özel girdiler, dalın `git bundle`'ı, `DEVAM.md` (kaldığı yerden devam komutu) ve `MANIFEST.sha256`
  var. ZIP'in kendi SHA-256'sı yanındaki `.sha256` dosyasında ve sohbet yanıtında; ZIP açılıp manifest
  `sha256sum -c` ile geri okunarak doğrulandı. ZIP public depoya konmadı.
