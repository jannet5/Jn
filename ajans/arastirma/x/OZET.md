# X.com Araştırması — ÖZET

Tarih: 2026-10-05/06 · 118 X kaynağı + 15 GitHub reposu · Detay: `index.md`, `notlar/`.

## En önemli 15 bulgu

1. **Mobil hat için fiili standart netleşmiş**: Expo + Expo Go (QR) + NativeWind + Supabase/InstantDB + RevenueCat + EAS Build; Xcode'a hiç dokunmadan App Store. Bunu Claude Code yaratıcısından Expo CTO'suna herkes aynı şekilde anlatıyor. (Vellotti, Bacon, Dikshit, Yarchi)
2. **Expo'nun resmi skill seti var** (github.com/expo/skills, `claude plugin install expo@claude-plugins-official`): router, animasyon, native-ui, design-system, data-fetching, upgrade, App Store submit. Kendi mobil skill'imizi yazmaya gerek yok, bunun üstüne çıkılır.
3. **Müşteri önizleme hattı hazır reçete**: GitHub push → EAS Workflow → her commit'te Expo Go'da açılan link; Claude Code web oturumu repoya bağlı, telefondan prompt atılır. (Evan Bacon)
4. **"AI slop" sorunu çözümlü**: Anthropic frontend-design SKILL.md (tam metni elimizde) + bir taste skill'i (Impeccable 24 komut / taste-skill) + denetim (web-design-guidelines) + **Playwright screenshot döngüsü**. Birden fazla taste skill'i birlikte kurma, çakışıyor.
5. **DESIGN.md yeni ortak dil**: Google Stitch kavramı; awesome-design-md'de Stripe/Linear/Vercel/Apple dahil 73 hazır dosya, Refero Styles'ta 2000+. "Referans ekran görüntüsü → Claude analiz → DESIGN.md → CLAUDE.md'ye @ ile import" (Vox) tasarım kapımız olabilir.
6. **Premium web sitesi reçetesi Türkçe ve kopyalanabilir** (tonny): 2 skill + 3 referans sitenin 9 ekran görüntüsü + 5 bloklu tek prompt + 3 ayrı cila geçişi (tipografi/boşluk/hareket) + 375px mobil + Cloudflare Pages. 3. sitede 2 saatlik pipeline. Ajans fiyatı $1-2k, 3 hafta.
7. **Doğrulama = kalite çarpanı**: Boris Cherny "Claude'a işini doğrulayacak yol ver → 2-3x kalite"; Anthropic "bir mühendisin bir haftasını verification skill'ine ayırması değer". Fabrikanın "fıstık gibi" kapısı burada.
8. **Skill yazma kuralları** (Anthropic/Thariq): bariz olanı yazma, Gotchas bölümü biriktir, klasör = progressive disclosure, description "ne zaman tetiklenir" yazar, setup'ı config.json'da tut, on-demand hook'lar (/careful). CLAUDE.md <200 satır, "kaldırınca hata yapar mı?" testi, her seferinde olmalıysa hook.
9. **Tek kişilik ajans matematiği** (Ronin $40k MRR): darboğaz teslimat; intake 10 dk → model üretir → QA 15 dk → handoff; her çözülen iş skill'e dönüşür, 5. müşteri 1.'nin kesri; retainer şart; marj %90.
10. **Teklif yapısı** (Luke Pierce): ücretli audit → build → aylık retainer; outreach'te audit değil tek acı + tek çözüm; haftada 1 nitelikli görüşme tüm işi döndürür; tek sektörde kal (2. build %80 hazır).
11. **Yerel işletme sitesinin zorunlu listesi** (theseoguy_): tıklanabilir telefon sağ üstte, 3 alanlı form fold üstünde, gerçek fotoğraf, yorumlar, tek CTA; H1'de hizmet+şehir, hizmet başına ve şehir başına sayfa, NAP tutarlılığı; form → anında SMS, cevapsız arama → otomatik SMS.
12. **Müşteri bulma takipçisiz** (Ganim): 10 kapı çal + "ücretsiz 15 dk audit", işletmelerin formuna test gönder (1 saatte dönmeyen = lead), tanıdıklara ücretsiz audit, ajanslara white-label, 90 gün build-in-public.
13. **App Store'da para kazanma ayrı sistem** (DamiDefi): banking/tax önce, Small Business Program'a elle başvur (%30→%15), harici ödeme referansı = red, StoreKit 2 transaction listener, 45 gün ödeme gecikmesi, 7 gün trial + bitişten 24 saat önce push.
14. **Reklam hattı Claude Code'a taşınabilir**: Futia'nın 5 skill'i (/spy, /competitors-extractor, /bulk-creative, /ad-score, /ad-matter) + Meta Ads MCP; coreyhaines31/marketingskills (50 skill, resmi marketplace) pazarlama tarafını kapatıyor.
15. **Türkçe boşluk**: X'te "AI ile işletmelere site/app satıyorum" anlatan TR hesap yok; TR ajanslar klasik hizmet listesi paylaşıyor. Kalfa OS (994 skill, TR komut ritüelleri) tek ciddi TR altyapı çalışması.

## Hemen uygulayabileceğimiz 10 şey

1. **`sistem/CLAUDE.md` iskeletini kur** (<200 satır): Evan Bacon'ın Expo CLAUDE.md'si + yasaklı tasarım listesi (Inter display, mor gradient, emoji ikon, left-border kart, her section fade-up, "→" buton, ALL-CAPS eyebrow, krem+terracotta default) + "yeni renk ekleme, dur ve sor" kuralı. → `notlar/mobil-uygulama.md` §2, `notlar/tasarim-animasyon.md`
2. **Skill'leri kur**: frontend-design, expo (resmi), web-design-guidelines, marketingskills, aso-skills, app-store-preflight, skill-creator, claude-code-setup. Tek taste skill'i seç (frontend-design **veya** impeccable). → `notlar/araclar.md`
3. **Web hattı tasarım kapısı**: müşteri sektörüne göre Refero/awesome-design-md'den 3 referans → Vox'un DESIGN.md prompt'u → tonny'nin 5 bloklu build prompt'u → 3 cila geçişi → Playwright 375/1440 screenshot → 20 maddelik kontrol. Hepsi `notlar/prompts.md` #2, #3, #2b.
4. **Mobil hat şablonu**: nkzw-tech/expo-app-template veya `bunx create expo` + EAS preview workflow (Bacon'ın preview.yml'i) + Matt Shumer spec prompt'u (#1) müşteri brief'i için + "gotcha moment" zorunlu alanı.
5. **Yayın öncesi kapılar**: web → Vox 20 kontrol + /impeccable audit; app → app-store-preflight + DamiDefi 7 adım + Apple zorunlu ekranlar (privacy URL, onboarding, settings, loading).
6. **Kendi vitrinimiz**: Viktor Oddy'nin "AI-powered web design agency" landing page prompt'unu (#16) kendi marka adı/renk/fontlarıyla çalıştır; Cloudflare Pages'e koy.
7. **Yerel işletme site şablonu**: theseoguy_ listesini (§11) Astro/Next şablonuna checklist olarak göm; her hizmet/şehir sayfası otomatik üretilsin; form → SMS + cevapsız arama SMS'i speed-to-lead agent'ıyla (Make/n8n + Twilio) paket.
8. **Teklif & fiyat**: küçük ücretli audit (site + Google Business + reklam hesabı, Claude ile 1 saat) → build → aylık retainer (bakım + 9'lu Instagram feed + carousel + reklam). Haftalık "değer defteri" raporu (lead sayısı, cevap süresi). → `notlar/ajans-is-modeli.md`
9. **İlk 30 gün müşteri planı**: tek dikey seç; 10 işletmenin formunu test et; 10 kapı; tanıdıklara ücretsiz audit; 1 indirimli iş ↔ vaka çalışması; haftada 3-5 before/after post (TR'de bu anlatıyı yapan yok).
10. **Compounding döngüsü**: her iş sonunda `code-simplifier` + "CLAUDE.md'ye ne eklemeliyiz?" + çözülen workflow'u skill'e çevir (Ronin/Boris). Doğrulama skill'ine (Playwright + Expo simülatör) bilinçli zaman ayır.

## Bu araştırmanın sınırları
- WebFetch oturum limitine takıldı; GitHub README'leri curl ile ham çekildi, blog yazıları (Expo blog, Anthropic "purple slop" yazısı) açılmadı → GitHub/blog hattı bunları derinleştirebilir.
- Tweet görsellerinden sadece 3'ü okundu (Bacon CLAUDE.md ×2, devnamipress gist). Diğer görsel-ağırlıklı tweet'ler (CLAUDE.md örnekleri, hook JSON'ları) açılmadı.
- "Comment X, I'll DM" ile dağıtılan playbook'lar (Futia Meta ads, Zack blueprint, dentist n8n template) alınamadı.
- Türkçe ajans/fiyat thread'i bulunamadı; Türkçe sorgular daha çok eğitim içeriği döndürdü.
