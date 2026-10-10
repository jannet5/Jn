# Full Tutorial: From Idea to App with Claude Design and Claude Code in 25 Minutes — Peter Yang — 25 dk — https://www.youtube.com/watch?v=G9o8eoHzpxc

> Kaynak notu: Transkriptin tamamı (`transkriptler/G9o8eoHzpxc.txt`, 33 KB) okundu. Not: Ürün (Tastemaker) bir **web** uygulaması; mobil klasöre süreç (plan → tasarım → spec → build) için konuldu.

## Tek paragraf: ne yapıyor, sonuç ne
Peter Yang (PM), Figma kullanmadan "AI-native" 6 adımlı süreçle Tastemaker'ı (film/dizi/oyun zevk profili sitesi) tasarlayıp kuruyor: (1) kullanıcı problemini tanımla, (2) ilham ekran görüntülerinden `design.md` üret, (3) Claude Design'da 2 ana ekranı varyasyonlu prototiple, (4) tek dosya `spec.html` (PRD + Design + Tech sekmeleri; component library + veri şeması), (5) spec'i Claude Design'a geri verip tüm çekirdek ekranları + boş/edge durumları + onboarding'i tasarlat, (6) spec + design.html ile Claude Code'a "önce belirsizlikleri sor" diye build ettir. Ana mesaj: **zamanın en az %50'si planlama**; tek atışta build yok, çok sayıda screenshot'lı geri bildirim turu var. Sonuç: canlı site (mytastemaker.vercel.app), Supabase + auth, "birkaç saat" [22:13].

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Claude Code + kendi **/spec skill**'i (ücretli Substack: https://www.behindthecraft.com; "AI'a kendin de yaptırabilirsin") [00:50, 16:11]
- İlham: Mobbin, Dribbble, Monogram ekran görüntüleri [02:00–03:00]; hazır DESIGN.md'ler: "z.sh" (Nike, SpaceX, Apple, Vercel, Notion) [04:01] (transkriptte isim bu şekilde; muhtemelen bizdeki awesome-design-md kaynağıyla aynı tür)
- **Claude Design** (netleştirici sorular sorması, doğrudan düzenleme, yan panel geri bildirim, yorum, share → export zip/HTML) [05:02–12:10]; alternatifler: Paper, pen.dev, Figma AI [05:02]
- Model: "Fable'ı seviyorum ama bu işler için Opus yeterli; token tasarrufu" [06:02]
- Build: Claude Code, localhost; Supabase DB + auth; yayın Vercel (URL'den) [21:13–22:13]

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. **Problem** (Prompt A, /spec skill ile): müşteri problemi, kim için, online kanıt; en fazla 2 paragraf → Claude 3 ayrı siteyi buldu (film/TV/oyun ayrı) [01:00–02:00]. İş için yapılsaydı: daha uzun araştırma + gelir fırsatı [02:00, 23:13].
2. **design.md** (Prompt B): Monogram ekran görüntüleri yapıştır → ilkeler + renk/tipografi/spacing önerileri [03:00–04:01]. "İlham ≠ kopya; farklı kategori olduğu için tamam" [04:01].
3. **Claude Design prototip** (Prompt C, design.md ekli): 2 ekran × 2 varyasyon, tek sayfada, web önce [05:02–06:02]. Netleştirici sorular: örnek profil (dengeli generalist), varyasyon ekseni = layout (row-heavy vs grid editorial), bölümler (favoriler, son incelemeler, listeler), light + dark, "tam gerçek copy" [07:03].
4. **Zevk uygula**: Edit modunda doğrudan sil (özdeyiş cümleleri, pill'ler, henüz olmayan Follow butonu) [08:04–09:04] + yan panel geri bildirimi (Prompt D) [10:05]; birkaç tur daha; başlık copy'sini elle düzelt → save → share → export zip/HTML [11:08–12:10].
5. **spec.html** (Prompt E, zip ekli): PRD (problem, hedefler, yüzeylere bölünmüş kısa gereksinimler), Design (design.md'nin HTML'i + **component library**), Tech (stack + **veri şeması**; "DB prod'da zor değişir") [13:10–15:11]. Spec'i oku, geri bildirim ver.
6. **Tüm ekranlar** (Prompt F, spec.html Claude Design'a): creator/edit görünümü, **boş durumlar**, tam liste sayfaları, item sheet, **onboarding** (handle al, 6 favori seç, sayfanı paylaş) → gözden geçir → export zip [17:11–19:11].
7. **Build** (Prompt G, spec.html + design.html ekli) → localhost → screenshot'lı uzun geri bildirim listeleri (hover'da kalp, profil tasarıma uymuyor, review bölümü nerede, Netflix okları) → "kod değişince plan ve design dosyalarını güncel tut" [20:13–22:13].

## Kullandığı prompt'lar (varsa aynen)
- **A:** "I'm a huge fan of movies, TV shows, and games and I want one page that shows what I love across all three. Can you help me define the customer problem, who this is for, and any evidence online that the problem exists? Start with the customer problem, write up to two short paragraphs on this topic." [01:00]
- **B:** "Here are a few screenshots from Monogram. Can you create a design.md for Tastemaker based on this visual direction? The product helps people share their favorite movies, TV shows, and games on one page. Keep the interface quiet and let the cover art carry the color." [03:00]
- **C:** "I'm building TasteMaker, a shareable profile for movies, TV shows, and games. Use the attached design.md [as the visual system]… [create] the public taste profile with favorites, recent reviews, and lists… a logged-out landing page that showcases a real taste profile as an example… two variations… lay on a single page so we can compare and refine them… let's build the web version first." [05:02–06:02] (kısmen parafraz)
- **D (geri bildirim):** "I prefer 1A and 2A, the light version, with a few changes. Favorites should be six across with arrows like Netflix for navigation. Recent reviews should be full width with a cover art, a five-star rating, and a review. Lists should also be five across like the favorites. Add a left nav on this page for quick links to favorites, recent reviews, and lists." [10:05] (parafraz)
- **E:** "turn these requirements and designs into a concise spec with product design and tech tabs" [14:11]
- **F:** "create all the core screens in the requirements" + spec.html [17:11]
- **G:** "Review the spec and design and let me know if any questions before you start building or any ambiguities that I should clarify." [20:13]
- /spec skill metni: ücretli (transkriptte yok).

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- "Arayüz sessiz, rengi kapak görselleri taşısın" ilkesi → renk içerikten gelir [03:00].
- Claude'un "pithy statement", pill, işlevsiz buton gibi slop'unu **elle silme**; "slop ile iyi arasındaki fark detaya dikkat" [09:04, 12:10].
- Varyasyon → seç → birleştir (diverge/converge) [23:13].
- **Component library** spec'te zorunlu ve güncel tutulur; yoksa AI her ekranda yeni component uydurur [15:11].
- Boş/default/edge durumlar ve onboarding tasarımda baştan [18:11].

## Hatalar ve çözümleri
- İlk tasarım tek turda iyi değil → "birkaç iterasyon daha" [10:05].
- Build tasarımı tam izlemiyor (review bölümü eksik, hover davranışı) → screenshot + madde madde liste [21:13].
- Kodda yapılan ürün kararları spec'ten kopuyor → Claude'a spec/design dosyalarını güncel tutturma [21:13].
- Spec'i okumadan ekran ürettirmek token yakar [17:11].

## Bizim fabrikaya alınacaklar (somut, maddeli)
1. **Tek dosya `spec.html`** (PRD / Design / Tech sekmeli) fabrikada PLAN.md'nin yanında müşteriye gösterilebilir teslim; Tech sekmesinde veri şeması zorunlu.
2. **Sıra:** önce 2 anahtar ekranı prototiple, sonra spec yaz, sonra tüm ekranlar (boş/edge/onboarding dahil), en son kod.
3. **Component library** spec'in parçası; her yeni ekranda güncellenir (CLAUDE.md kuralı).
4. Build başlangıç prompt'u standart: "spec + design'ı incele, başlamadan belirsizlikleri sor" (Prompt G).
5. "Kod değişince spec.html ve design dosyasını güncelle" kuralı CLAUDE.md'ye.
6. design.md'yi müşterinin/rakibin değil, farklı kategoriden beğenilen ürünün ekran görüntülerinden türet; Claude Design'da netleştirici sorulara layout ekseniyle varyasyon iste.
7. Zaman bütçesi: proje süresinin ≥%50'si plan + tasarım.
