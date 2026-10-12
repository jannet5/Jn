# How to Use Claude Design to Build an App (Step by Step) — Max Max — 17 dk — https://www.youtube.com/watch?v=s4E8iQBUrJA

> Kaynak notu: Transkriptin tamamı okundu. Ürün bir **web** sitesi/uygulaması (Lovable); mobil klasöre "önce tasarım, sonra işlev" süreci için konuldu. Videoda kod, CLAUDE.md veya skill yok; değer, prompt sıralaması ve "tasarımı kilitle, sonra üstüne inşa et" disiplini.

## Tek paragraf: ne yapıyor, sonuç ne
Tasarım ile işlevi aynı araçta aynı anda çözmeye çalışmanın "ya güzel ama çalışmıyor ya çalışıyor ama 2009'dan kalma" sonucunu doğurduğunu söylüyor. Çözüm: **önce Claude Design'da görsel prototip** (tek odaklı prompt; layout, tipografi, renk, glassmorphism), tasarım düzeltmelerini orada bitir, **zip olarak export et**, Lovable'ın chat kutusuna zip'i yükleyip "import et ve işlevsel yap" de; sonra her prompt'ta tek bir işlev katmanı ekle (auth + DB → AI chat widget → publish) ve her prompt'ta "mevcut görsel tasarımı değiştirme" kuralını tekrar et. Sonuç: "Novacoin" adlı Web3 portföy sitesi; kayıt/giriş, kullanıcıya özel watchlist + holdings, portföy verisini okuyan AI chatbot, canlı URL [13:07].

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Claude Design** (claude.ai içinde, doğal dille görsel prototip; export → zip) [02:17, 06:43]
- **Lovable** (zip import, auth + database, native AI, tek tık publish): https://lovablelabs.pxf.io/zzqzY0 (açıklamadaki affiliate link)
- Masterclass (reklam): https://mikeyno-code.com/Skool-base44
- Skill / MCP / repo linki yok.

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. **Claude Design'da tek odaklı prompt** ile prototip: koyu arka plan, mavi-mor → elektrik mavisi gradient, neon vurgu, glassmorphism kartlar; hero + tokenomics + 4 fazlı roadmap timeline + team + community bölümleri (Prompt 1) [04:16–04:47].
2. **Gözden geçirme**: tutarlılık, layout, tipografi, renk; küçük görsel düzeltmeleri Claude Design içinde yap — çünkü Lovable'da her görsel değişiklik daha uzun sürer ve kredi yer [05:16–06:32]. Kural: "kusursuz olmak zorunda değil ama production-ready başlangıç noktası olacak kadar cilalı olmalı".
3. **Export**: Claude Design → export → Novacoin projesini **zip** indir (layout, component'ler, styling, sayfa yapısı) [06:43].
4. **Import**: Lovable ana chat kutusuna zip'i yükle (ayrı import menüsü yok) + Prompt 2 [07:17–07:44]. Kontrol: koyu arka plan, neon, glassmorphism, tipografi, tüm bölümler aynen geldi mi [08:02]. Bu aşamada bilerek auth/DB/AI yok.
5. **Auth + DB tek prompt'ta** (Prompt 3): veri izolasyon kuralları baştan DB'ye girsin diye ikisi birlikte [08:55–09:36].
6. **Test**: hesap aç, giriş yap, dashboard'da token kaydet, holdings gir; **ikinci kullanıcıyla** giriş yapıp sadece kendi verisini gördüğünü doğrula; dashboard'un aynı görsel dili taşıdığını kontrol et [10:07–11:07].
7. **AI chatbot** (Prompt 4): kullanıcının kayıtlı watchlist/holdings verisini referans alan, dashboard içinde widget [11:24–11:53]. Portföy sorularıyla test [12:25].
8. **Publish**: Lovable'da Publish → canlı URL; editör dışında tasarımın korunduğunu kontrol et [13:07–13:34].
9. Tekrar edilebilir sıra: Claude Design → export → Lovable → auth+DB → AI özelliği → publish; "her adımın tek işi var" [15:03].

## Kullandığı prompt'lar (varsa aynen)
Prompt 1 — Claude Design [04:38]:
```
Create a modern Web3 portfolio website for a new cryptocurrency called Novacoin. Use a dark background with deep blue-purple to electric blue gradient colors, neon accent, and glassmorphism card effects. Include a bold hero section with a tagline and CTA, a tokenomic section, a four-phase roadmap timeline, a team section, and a community section. The aesthetic should feel premium and futuristic.
```
Prompt 2 — Lovable, zip ekli [07:44]:
```
Please import this Claude design website and make it functional.
```
Prompt 3 — Lovable auth + DB [09:09]:
```
Add user authentication with sign up and log in and a database that lets each authenticated user save cryptocurrency tokens to a personal watch list and log their holdings with their name, quantity, and purchase price. Display the watch list and holdings in a new dashboard section that appears only after login. All data should be private per user. Do not change the existing visual design and re-implement its style to the dashboard.
```
Prompt 4 — Lovable AI chatbot [11:47]:
```
Add a native AI chatbot to the dashboard that users can open to ask questions about their cryptocurrency portfolio. The chatbot should be able to reference the user's saved watchlist and holdings to provide relevant responses. Display it as a chat widget inside the dashboard that matches the existing dark web 3 design.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Tasarım aracı **sadece görselle** uğraşıyor (layout, styling, tipografi, yön); kodlama aracı ilk üretimde hem görsel hem işlevle uğraştığı için tasarım "daha az rafine" kalıyor [02:17–03:09].
- İlk prompt'ta somut görsel dil: renk geçişi tanımı (deep blue-purple → electric blue), neon accent, glassmorphism, "premium and futuristic" — sıfat + teknik efekt birlikte.
- Görsel düzeltmeler build'den **önce** bitiyor; sonraki tüm prompt'lar tasarıma dokunmuyor.
- Her işlev prompt'unda koruma cümlesi: "Do not change the existing visual design", "matches the existing dark web 3 design" → yeni ekranlar (dashboard, chat widget) aynı görsel sistemi miras alıyor [09:36, 11:53].

## Hatalar ve çözümleri
- Videoda gerçek hata gösterilmiyor. Önlenen sorunlar:
  - Lovable'da tasarım düzeltmek kredi ve zaman yer → düzeltmeleri Claude Design'da bitir [05:39].
  - Auth ve DB'yi ayrı prompt'larda eklemek veri izolasyonunu sonradan yamamaya yol açar → tek prompt'ta, "private per user" kuralıyla [09:36].
  - Veri yanlış hesaba bağlıysa AI sadece genel cevap verir → AI'dan önce ikinci kullanıcıyla izolasyon testi [10:21].

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Aşama ayrımı kuralı**: fabrikada "tasarım kilidi" aşaması olsun; görsel prototip (Claude Design veya `/design`) onaylanmadan Expo build aşamasına geçilmesin. Görsel değişiklikler build sonrası değil öncesi yapılır.
- **Koruma cümlesi şablonu**: build aşamasındaki her özellik prompt'unun sonuna sabit ek: "Do not change the existing visual design; new screens must reuse the existing design tokens and components." CLAUDE.md'ye kural olarak da yazılsın.
- **Tek katman = tek prompt**: önce UI iskeleti (mock veriyle), sonra auth + DB (izolasyon/RLS kuralıyla birlikte, Supabase RLS), sonra AI özelliği, sonra yayın. Her adımın sonunda görsel regresyon kontrolü (ekran görüntüsü karşılaştırma).
- **İzolasyon testi checklist'i**: iki test kullanıcısıyla giriş → yalnızca kendi verisi görünüyor mu? (Supabase RLS policy testi) — AI özelliğinden önce zorunlu.
- İlk tasarım prompt'u şablonu: [renk geçişi] + [efekt: glassmorphism/neon vb.] + [bölüm listesi] + [estetik sıfatları] yapısında, işletme türüne göre doldurulan form.
- Lovable web odaklı; bizim zincirde karşılığı: Claude Design export (HTML/zip) → Claude Code'a referans olarak verip Expo/React Native'e çevirme.
