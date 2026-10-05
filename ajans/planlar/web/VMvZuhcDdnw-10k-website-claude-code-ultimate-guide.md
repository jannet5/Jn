# Build $10,000 Websites using Claude Code (Ultimate Guide) — Metics Media — 26 dk — https://www.youtube.com/watch?v=VMvZuhcDdnw

## Tek paragraf: ne yapıyor, sonuç ne
Claude Code masaüstü uygulamasıyla, iki tasarım skill'i (Anthropic'in **frontend-design** skill'i + topluluk yapımı **UI UX Pro Max**) kurup tek bir brief prompt'uyla Seattle'da bir steakhouse için tek sayfalık statik (tek HTML) site üretiyor. "10K $ site" tanımını 8 maddelik bir kontrol listesine indirgiyor (bakış açısı, tipografi, renk, hiyerarşi, görsel, hareket, mobil, görünmez kalite) ve siteyi bu listeye karşı Claude'a puanlatıp eksikleri toplu düzeltme + bölüm bölüm cursor/mikro-etkileşim turlarıyla kapatıyor. Hero görselini ElevenLabs içinde ChatGPT görsel → Veo 3.1 video → Topaz upscale zinciriyle üretiyor, 21st.dev'den scroll efekti alıyor. Sonunda zip'i Hostinger'a yükleyip özel domain'de yayınlıyor; toplam maliyet ~60 $ (Claude Pro 20 $ + hosting/domain ~43 $/yıl).

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code desktop app (Pro plan 20 $/ay) — "code mode", proje klasörü, **auto mode** (izin sormadan çalışsın)
- **frontend-design skill** (Anthropic resmi; aşırı kullanılan fontları yasaklar, cesur tasarım yönüne iter, "restrained sensory copy" yazdırır): https://github.com/anthropics/skills/tree/main/skills/frontend-design — prompt: "install this skill" (global kur)
- **UI UX Pro Max** (topluluk; 57 UI stili, 95 renk paleti, 56 font eşleşmesi; `/ui-ux-pro-max` ile çağrılır): https://github.com/nextlevelbuilder/ui-ux-pro-max-skill — prompt: "install this plugin using npm" (global kur)
- Referans kaynakları: Dribbble, Awwwards, Pinterest ("modern website design restaurant") — 3-5 site SS'i Claude'a ver
- **21st.dev** — hazır bileşen/animasyon kütüphanesi; "copy prompt" butonuyla Claude'a yapıştır (React bileşeni ise Claude statik projeye uyarlar)
- Görsel/video: ElevenLabs aggregator (ChatGPT image → Google Veo 3.1 image-to-video → Topaz upscale); görsel prompt'unu Claude yazıyor
- Font: Inter → **Geist** (Inter "AI yaptı" diye bağırıyor)
- Hosting: Hostinger Premium (statik site; backend/Node gerekiyorsa Business), 12 ay = ücretsiz domain, ~43 $/yıl
- 10K Website Checklist PDF (açıklamadaki link; 8 madde aşağıda)

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Claude Code desktop kur → code mode (Cmd/Ctrl+3) → boş proje klasörü seç.
2. Skill 1: frontend-design linkini yapıştır + "install this skill" → global.
3. Skill 2: UI UX Pro Max linki + "install this plugin using npm" → global.
4. Sol altta mod → **auto mode**.
5. Referans: Dribbble/Awwwards/Pinterest'ten 3-5 SS.
6. Brief prompt'u: `/ui-ux-pro-max` + işletme brief'i + son satır **"Ask me clarifying questions."**
7. Claude 7 soru soruyor (isim, stil yönü — 3 seçenek sunuyor: Manhattan steakhouse koyu/moody, Pacific Northwest modern, klasik chop house —, bölümler, içeriği kim yazacak, tech stack, animasyon seviyesi, ekstra). Cevap: "dark moody luxury + Pacific Northwest grounding, 5 bölüm, galeri yok, rezervasyon sistemi yok, tek sayfa". **En kritik an: cevaplar siteyi belirler.**
8. ~3 dk planlama + 4-5 dk kod → ilk sürüm (Fraunces başlık + Inter gövde; 5 hex: near-black, warm cream, oxblood, brass, slate).
9. Görsel: Claude'a "bu site için hero görsel prompt'u yaz" → ElevenLabs'te ChatGPT image → Veo 3.1 video → Topaz. Dosyayı proje klasörüne at.
10. 21st.dev'den "smooth scroll image" bileşeninin prompt'unu kopyala + steak videosunu ekle → "use this component to build the hero". Claude React'ı reddedip statik HTML'e uyarlıyor.
11. Video scroll'da oynamıyor → "video shows up but isn't scrolling" → 2 bug düzeltildi, hâlâ bozuk → "dig deeper" → Claude tarayıcıda sayfayı açıp inceliyor → 3. katmanda çözüm.
12. Hero metnini parlak görsele karşı okunur yap + scroll'da içerik reveal.
13. Checklist'i yapıştır: "Where does this site land against each of these criteria? Be honest." → strong/mixed/missing raporu.
14. Toplu düzeltme prompt'u (niyet odaklı): "We need more handcrafted micro-interactions. The lower sections feel a bit generic. We don't need to make them busier, just more expensive." → film grain, bölümler arası animasyonlu brass hairline, "reserve a table" bölümünde ember (kor) efekti, başlıklarda kelime kelime reveal… 5 değişiklik tek seferde.
15. Bölüm bölüm gez, "düz" kalanlara **birer** cursor/hareket etkileşimi: "Make the embers move like fire. Have them react to the cursor." / "Add some elegant micro movements and cursor interactions here. This section is feeling very static." → fazla belirginse "Make it more subtle, more refined." (halo cursor'ı gecikmeli takip ediyor).
16. Inter → Geist.
17. Mobil: "Do a dedicated pass on the mobile version: what should be hidden, tighter, resized" → nav collapse, wordmark spacing, küçük buton varyantı.
18. Yayın: proje klasörünün **içindekileri** seçip zip'le (klasörü değil!) → Hostinger "migrate existing site → upload backup files" → domain seç → yayın.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- `install this skill` (frontend-design linkiyle) / `install this plugin using npm` (UI UX Pro Max linkiyle)
- Brief: `/ui-ux-pro-max  [restoran brief'i: Seattle'da steakhouse …]  Ask me clarifying questions.`
- Checklist değerlendirme: `Where does this site land against each of these criteria? Be honest.` (+ 8 maddelik checklist)
- Toplu polish: `We need more handcrafted micro-interactions. The lower sections feel a bit generic. We don't need to make them busier, just more expensive.`
- Bölüm bazlı: `Make the embers move like fire. Have them react to the cursor as it moves around the page.` / `Add some elegant micro movements and cursor interactions here. This section is feeling very static.` / `Make it more subtle, more refined.`
- Font: `Swap Inter for Geist.` Mobil: `Do a dedicated pass on the mobile version. Think about what should be hidden on small screens, what should be tighter, what should resize.`
- Hata ayıklama: `The video shows up on the page but it isn't scrolling.` → `Dig deeper, open the page and inspect what's happening.`
- **10K $ site kontrol listesi (8 madde, 3 grup):** Zevk = (1) point of view / net bir yön, (2) tipografi, (3) renk (5 hex, kısıtlı palet); Öz = (4) hiyerarşi (büyük/orta/küçük okuma sırası), (5) görsel (özel foto/video veya AI üretimi), (6) hareket (scroll + cursor mikro-etkileşimler); Hissedilen kalite = (7) mobil "tasarlanmış, küçültülmüş değil", (8) görünmez şeyler (hız, bitmişlik).

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler
- İki skill birlikte: frontend-design (sessiz arka plan kuralı) + UI UX Pro Max (palet/font/stil kütüphanesi, slash komutla büyük müdahale).
- **"Ask me clarifying questions"** satırı: Claude 3 stil yönü sunuyor; seçim + spesifik cevaplar = sonradan az revizyon.
- Referans SS'leri (Dribbble/Awwwards/Pinterest) — kelimeyle zevk anlatmak yerine göster.
- Kısıtlı renk (5 hex), tipografik hiyerarşi (şehir adı dev, adres orta, açıklama küçük), kısa "chef yazmış gibi" copy ("Six dishes, one fire").
- Gerçek hareketli hero (AI video) + 21st.dev scroll bileşeni.
- Kontrol listesine karşı dürüst puanlama → niyet odaklı toplu düzeltme ("busier değil, more expensive").
- Bölüm başına **tek** restrained cursor/hareket etkileşimi; "more subtle" ile törpüle.
- Inter yasak → Geist. Film grain, brass hairline, kelime kelime reveal gibi "handcrafted" dokunuşlar.
- Mobil için ayrı tasarım turu.

## Hatalar ve çözümleri
- 21st.dev bileşeni React → statik projede Claude reddediyor, aynı efekti vanilla ile yeniden yazıyor (mimariyi koru).
- Scroll-video çalışmıyor → iki tur düzeltme yetmedi → "dig deeper" ile tarayıcıda canlı inceleme → çözüldü. Ders: ne olduğunu + ne beklediğini anlat, pes etme.
- Hero metni parlak görselde okunmuyor → okunurluk turu.
- Cursor efekti fazla belirgin → "more subtle, more refined".
- Zip hatası: klasörü değil içeriğini zip'le, yoksa domain boş sayfa açar.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Satış süreci yok; maliyet hesabı: Claude Pro 20 $ + Hostinger Premium 12 ay ~43 $ (domain + e-posta dahil) ≈ 60 $ ile "10K $ görünümlü" site. Statik site için Premium, backend'li için Business plan.

## Bizim fabrikaya alınacaklar (somut)
1. **Skill seti**: frontend-design (Anthropic) + UI UX Pro Max global kurulu; brief her zaman `/ui-ux-pro-max` + "Ask me clarifying questions" ile başlar; Claude'un sunduğu 3 stil yönü müşteriye sunulabilir (müşteri seçsin).
2. **8 maddelik 10K checklist** → kalite kapısı: sitenin ilk sürümünden sonra Claude'a "be honest" puanlatma, mixed/missing olanlar için toplu düzeltme prompt'u.
3. **Polish protokolü**: (a) toplu "more expensive, not busier" turu, (b) bölüm başına tek cursor/hareket, (c) "more subtle" törpüsü, (d) Inter→Geist, (e) mobil ayrı tur.
4. **Görsel zinciri**: Claude görsel prompt'unu yazar → image → image-to-video (Veo/Kling) → upscale; dosya proje klasörüne.
5. **21st.dev** prompt kopyalama yöntemi hero ve özel bölümler için.
6. Teslimat: tek dosya statik HTML (restoran/dükkan tipi için yeterli); zip içeriği → Hostinger/Netlify.
