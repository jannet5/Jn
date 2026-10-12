# Claude Code Built My Entire iOS App Company (Code to App Store) — Omar Farook | Building Beautiful Products — 30 dk — https://www.youtube.com/watch?v=J17j1wiisCs

## Tek paragraf: ne yapıyor, sonuç ne
Omar, aile için AI quiz uygulaması "Brain Fam"i (her aile üyesine yaşa göre kişiselleşen sorular, ElevenLabs ile sesli/eller-serbest quiz modu, OpenAI ile soru üretimi, puan/level) bir vibe-coding platformunda %90'a getiriyor; backend (hesap, e-posta, ödeme) haftalarca çalışmayınca kodu indirip Claude Code'a (Claude Desktop) taşıyor. Taşımadan önce eski platformun ajanına "platform nasıl çalışıyor" context MD dosyası yazdırıyor. Claude Code + Chrome'daki Claude extension (tarayıcı kontrolü) ile RevenueCat ödeme planlarını, Railway backend'ini, Namecheap DNS kayıtları + Resend OTP e-postalarını ve App Store Connect listesini (promotional text, description, keywords) neredeyse eller-serbest kuruyor. Pazarlama tarafında: simülatörden otonom ekran görüntüsü/video + Higgsfield MCP ile üretilen arka plan/stok görseller + **HTML layout** ile App Store screenshot'ları (iPhone + iPad), business context MD, **HTML brand guideline** dosyası, Leadpages MCP ile landing page + privacy/terms/support sayfaları, tek prompt'la UGC reklam videosu. Sonuç: App Store'a review için gönderilmiş uygulama + markaya uyumlu pazarlama seti. Kod yazımı detaylı gösterilmiyor; değeri "kod sonrası" yayın/pazarlama otomasyonunda.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Desktop app (Claude Code; çoklu paralel session, yerleşik terminal, background tasks, planning, preview, diff, files paneli).
- Claude in Chrome extension (tarayıcı kontrolü; servislere kullanıcı giriş yapar, gerisini Claude doldurur).
- macOS izinleri: Simulator erişimi, klasöre yazma/silme (chat içinden "Allow").
- iOS/iPad Simulator (otonom gezinme, screenshot, video kaydı).
- RevenueCat (ödeme/abonelik), Railway (backend deploy; API token elle yapıştırıldı), Resend (OTP e-posta), Namecheap (DNS kayıtları), App Store Connect.
- ElevenLabs (ses), OpenAI (soru üretimi).
- Higgsfield AI + Higgsfield MCP (görsel/video; "Supercomputer" süper ajanı; UGC reklam, arka planlar).
- Leadpages + Leadpages MCP (landing page, yasal sayfalar): https://new.leadpages.com/?utm_source...
- Brain Fam (App Store): https://apps.apple.com/gb/app/brain-f...
- Wispr Flow (sesle prompt): https://ref.wisprflow.ai/omar-farook
- Blitzit: https://www.Blitzit.app ; Granular (kohort eğitim): https://www.granular.build
- İsmi verilmeyen vibe-coding platformu (telefonda canlı önizleme, tek tuşla App Store; kodu indirmek ~$50 üst plan).

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:05:47] Problem: çocuklarla yürüyüşte birbirini quizleme → kişiselleşen aile quiz uygulaması fikri.
2. [00:07:05] Vibe-coding platformunda %90: aile üyeleri oluşturma, ilerleme takibi, puan, ses (ElevenLabs), soru (OpenAI), sesli quiz modu. Backend/hesap/e-posta/ödeme haftalarca kırık.
3. [00:10:00] Eski platformun ajanına, platformun geliştirme ortamını/altyapısını/özel framework'lerini anlatan bir **MD context dosyası** yazdırdı.
4. [00:09:47] Kodu indirdi (ücretli plan), `brainfam` klasörüne koydu; Claude Desktop'ta yeni session → klasör seç → bağlamı anlatan prompt (platforma özgü servisleri decouple et, önerdiğin local/cloud servislere geçir).
5. [00:11:59] Claude, Chrome extension kurmasını önerdi. Kullanıcı RevenueCat'e giriş yaptı → Claude tüm payment plan/opsiyonları kurdu (RevenueCat zaten vardı, yeniden bağlandı).
6. [00:12:30] Namecheap'e giriş → Claude e-posta için DNS kayıtlarını girdi; Resend ile OTP e-postaları.
7. [00:12:44] Railway hesabı açıldı → Claude backend'i kurdu; tek manuel adım: token oluşturup API key'i Claude Code'a yapıştırmak.
8. [00:13:23] App Store Connect'e giriş yapıp sekmeyi açık bıraktı → Claude promotional text, description, keywords vb. doldurdu; yasal/vergi kısımlarında "bunu sen yapmalısın, şuraya tıkla" diye yönlendirdi.
9. [00:14:06] Screenshot üretimi: Claude'a "nasıl tasarlayacaksın" diye sordu → Simulator izni + klasör yazma/silme izni verildi → Claude uygulamada otonom gezip ham screenshot + video kaydı aldı, masaüstü klasörüne koydu.
10. [00:17:33] Higgsfield MCP ile lifestyle görseller (yemek masasında/koltukta telefon kullanan aile) ve arka plan varyasyonları üretip yerelde kaydetti.
11. [00:18:17] HTML layout dosyaları: gerçek (seçilebilir) metin + ortada screenshot + Higgsfield arka plan + CSS gölge → snapshot alınıp raster PNG'ye çevrildi. Beğenmediğini söyleyip iterasyon.
12. [00:19:44] Yükleme sırasında Claude "uygulama iPad-enabled, iPad asset'i lazım" dedi → iPad simülatörünü kendi açıp screenshot üretip yükledi.
13. [00:20:27] Konuşmalardan birikmiş bilgiyle **business context MD** (pazar, problem, hedef kitle) yazdırdı; ajan UX ve metin kararlarında buna başvuruyor.
14. [00:21:40] **HTML brand guideline** üretimi (kod tabanını bilen Claude ile): about/who's it for/market/problem/why families love it, logo + lockup'lar, renkler (lavender gradient primary, turuncu CTA, semantic success/warning/error, kategori emoji'lerinden türetilen quiz renkleri), typography, kartlar, buton stilleri, voice mode butonları, settings, progress, soru-cevap, quiz karakterleri, aile avatarları, level-up, kategori loading, voice & tone.
15. [00:23:36] Brand guideline'ı Leadpages MCP ile yayınladı → URL'i Leadpages builder ajanına verip landing page üretti (animasyonlu uygulama carousel'i + early access formu).
16. [00:25:37] App Store şartı olan support URL, marketing URL, privacy policy, terms of use sayfalarını Claude yazdı, Leadpages MCP ile canlıya aldı.
17. [00:01:38] Higgsfield Supercomputer'a tek prompt → voice-over'lı UGC reklam videosu.
18. [00:05:00] App Store Connect'te "Add for Review" → gönderildi.
19. [00:27:01] Sonraki adım: Meta + analytics'i Claude Code'a bağlayıp otonom pazarlama ajanı. İş modeli: 10 ücretsiz quiz, sonra premium.

## Kullandığı prompt'lar (varsa aynen)
Higgsfield UGC reklam (transkriptte kısaltılmış hâliyle) [00:02:10]:
```
generate an app UCG style promo video for brain fam of a mom talking about how smart her kids are becoming etc etc
```
Taşıma prompt'u (transkriptteki anlatımıyla, birebir değil) [00:09:53]:
```
hey, I used this vibe coding tool to build this app so far. it's probably got some unique native services that it was running. I want to decouple all of those and run a local or cloud service that you recommend that allows me to make this app become fully functional.
```
Screenshot [00:15:18]:
```
hey generate some beautiful screenshots
```
Diğerleri (anlatım): "how are you gonna design these screenshots?", business context MD oluştur, HTML brand guideline üret, Leadpages'e yayınla, privacy policy / terms / support sayfalarını oluştur.

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Store screenshot'larında AI görsele metin yazdırmıyor**: metin HTML'de gerçek font, gerçek screenshot ortada, AI yalnızca arka plan/stok foto/efekt üretiyor → yazım hatası yok, font tutarlı. Sonra HTML → raster export.
- **HTML brand guideline** tek kaynak: renk rolleri (primary gradient, CTA rengi, semantic renkler), kategori emoji → renk eşlemesi, typography, buton/kart stilleri, uygulama içi bileşen örnekleri, voice & tone. Tüm asset'ler (screenshot, landing, yasal sayfalar) buna göre üretildiği için marka tutarlı.
- Business context MD → UX ve metin kararlarında hedef kitleye uygunluk.
- Landing page'te animasyonlu uygulama carousel'i.
- Tasarımcı gözüyle eleştiri: screenshot'larda üst padding az, metin ortalanmalı, fazla boşluk — iterasyonla düzeltilecek detaylar.

## Hatalar ve çözümleri
- Vibe-coding platformu backend'i (auth, e-posta, ödeme) haftalarca çözemedi → kodu indirip Claude Code'a taşıdı; eski ajanın yazdığı platform-context MD ile geçiş hızlandı.
- Railway: Claude token oluşturamadı → kullanıcı token'ı elle oluşturup yapıştırdı.
- App Store Connect yasal/vergi adımları → human-in-the-loop, Claude adım adım yönlendirdi.
- iPad asset eksikliği → Claude yükleme sırasında fark edip iPad simülatöründen üretti.
- Fiyatlandırma (£/$4.2–4.9 planları) pahalı olabilir notu.
- Uyarı: Yasal metinleri hiç okumadan yayına aldı ("trust the employee") — bizim için kabul edilemez; insan kontrolü gerekli.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Store screenshot hattı**: Expo uygulamasından (simulator / Maestro / `xcrun simctl io booted screenshot`) ham ekran görüntüsü → HTML/CSS şablonu (başlık metni + cihaz çerçevesi + marka arka planı) → headless Chrome (Playwright/Puppeteer) ile 1290×2796 (6.9") ve iPad 2064×2752 PNG export. AI görsel sadece arka plan için.
- **Her proje için iki zorunlu dosya**: `docs/business-context.md` (pazar, problem, hedef kitle, ton) ve `docs/brand-guideline.html` (renk token'ları, typography, buton/kart/bileşen örnekleri, voice & tone). CLAUDE.md'ye: "UI, metin ve asset üretmeden önce bu iki dosyayı oku."
- Brand guideline'daki token'lar Expo tarafında tek `theme.ts`'e bağlansın (primary gradient, CTA, semantic success/warning/error, kategori renkleri) — ham/rastgele renk kullanımı yasak.
- Yayın checklist'ine: privacy policy, terms, support URL, marketing URL (müşteri domaininde statik sayfa); iPad desteği açıksa iPad screenshot zorunluluğu (Expo: `ios.supportsTablet` kararını baştan ver).
- App Store Connect metinleri (promotional text, description, keywords) business context'ten Claude'la üretilsin; mümkünse `eas metadata` (store.config.json) ile kodla yönetilsin, tarayıcı otomasyonuna gerek kalmadan.
- Servis kurulumunda tarayıcı ajanı (Claude in Chrome) yardımcı: RevenueCat ürün/entitlement, DNS kayıtları (Resend SPF/DKIM), backend deploy. Gizli anahtarlar daima kullanıcı tarafından elle girilsin.
- Başka platformdan (Lovable/Rork/Bolt vb.) devralınan müşteri projeleri için: önce eski platform ajanına "platform-context.md" yazdır, sonra Claude Code'da decouple planı.
- Pazarlama ek hizmeti: tek prompt UGC reklam videosu (Higgsfield), landing page — müşteriye paket olarak satılabilir.
- Yasal metinler insan (veya müşteri) onayından geçmeden yayınlanmasın.
