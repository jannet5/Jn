# Claude Fable 5.1 ile Profesyonel Web Sitesi Yaptırdım (Design Skill + 21st.dev) — Emrullah Yaprak — 16 dk — https://www.youtube.com/watch?v=iCyGIF5uIzo

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu).
## Tek paragraf: ne yapıyor, sonuç ne
Claude Code desktop'ta (coding sekmesi) Fable 5.1 ile bir burgerci sitesi (ana sayfa, menü, rezervasyon, hikaye, iletişim) yaptırıyor ama tek prompt'la değil, **"Mimar – Malzeme – Usta"** sistemiyle: **Mimar** = Claude'un yeni **Design skill**'i (`/design`) ile sadece sayfa/blok iskeleti (wireframe, "plan, görsel tasarım değil"); **Malzeme** = **21st.dev MCP** ile her blok için 2-3 insan yapımı komponent adayı bulup linkleriyle listeletmek ve seçmek; **Usta** = Fable 5.1'in parçaları tek temaya uydurup kurması, Unsplash'tan görsel çekmesi ve ekran görüntüsüyle kendini denetlemesi. Tez: AI sitelerinin birbirine benzemesinin (AI slop: Inter font, mavi-mor gradyan, ortalanmış hero + 3 yuvarlak kart, "daha hızlı, daha akıllı" lafları) sebebi model değil, tasarım kararlarını yapay zekaya bırakmak. Sonuç: scroll'da büyüyen burger hero'su, filtreli menü chip'leri, animasyonlu bento galeri, yorumlar, harita/çalışma saatleri, rezervasyon takvimi, SSS ve footer'lı bir site; sonda canlı olarak header'a 21st.dev'den duman efekti ekletiyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Claude Code desktop** (Claude Desktop: https://claude.ai/download) → Code/coding sekmesi, klasör aç, konuşma başlat. Plan: Max 5x.
- Model **Fable 5.1** (açıklamaya göre Fable 5 ile aynı fiyat: 10 $ girdi / 50 $ çıktı per milyon token; cache read 1 $ → 0,25 $, %75 ucuz). Oturum ortasında efor değiştirme var.
- **Design skill** (Claude Design ile birlikte gelen; `/design` komutu; "research preview", token tüketimi yüksek). Çıktısı panelde görülüyor: tasarım yönü, okuma kılavuzu, sayfa/bölüm iskeletleri.
- **21st.dev** (https://21st.dev, ref linki: https://21st.dev/?ref=yprkemrullah) — 12.000+ insan yapımı komponent/animasyon; "Copy prompt"; ücretsiz planda günde 2 kopyalama, ücretli sınırsız (kendisi 3 aylık ~1.200 TL ödemiş).
- **21st.dev MCP** — Claude desktop'ta Customize → Connectors → Custom ile eklenmiş; MCP araçları komponent arıyor ve prompt'unu kendisi kopyalıyor. (Kurulum linki "aşağı bırakırım" deniyor ama açıklamada ayrı MCP linki yok.)
- **Unsplash** (https://unsplash.com) — ücretsiz placeholder görseller, Claude kendisi çekiyor.
- `npm run dev` → `localhost:3000`.
- Tüm prompt'lar (Drive): https://drive.google.com/file/d/170kv8ReNYtKFPvK5fm3tnbxwVfW4L4BH/view?usp=sharing — içerik videoda kısmen görülüyor, bizde yok.
- Kullanılan/önerilen 21st.dev komponentleri: scroll'da büyüyen medya hero'su (scroll expansion), selector chips, foto bento galeri, rezervasyon saat seçici, testimonials; duman için **Ethereal Whispers**.
- Diğer link (konu dışı): n8n kursu https://n8nkursu.com
- Açıklama notu: sistem Cursor, Codex gibi başka araçlarda da uygulanabilir.

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Claude Code desktop → coding sekmesi → yeni klasör → yeni konuşma, model Fable 5.1.
2. **Mimar**: `/design` + "Burgerci web sitesi; ana sayfa, menü, rezervasyon, hikaye ve iletişim" → sadece yerleşim/iskelet istendi. Çıktı: üstte menü, ortada hero "imza üçlüsü", aşağıda yan lezzetler/tatlılar ürün fotoğrafları; her sayfa için tasarım yönü + okuma kılavuzu + bölüm şeması (kağıt-kalem çizim gibi "dış iskelet").
3. **Malzeme**: Claude'a "21st.dev araçlarıyla şu blokların her biri için 2-3 component adayı bul ve linkleriyle listele: scroll'da büyüyen medya herosu, kategori seçici çipler, fotoğraf bento galerisi, …".
4. 21st.dev MCP bağlı (Customize → Connectors → custom 21st.dev) → Claude tarayıp her blok için 3 öneri + link getiriyor. Linkleri açıp canlı önizleme: scroll'da büyüyen hero seçildi (burger büyüyerek öne geliyor efekti bu), selector chip efekti beğenildi, bento galeri, rezervasyon saat seçici, testimonials.
5. Kalanlar için: "Hepsini senin seçimine bırakıyorum. Gerekli placeholder görselleri Unsplash sitesinden al koy." → Claude seçip ekliyor, Unsplash'tan görselleri çekiyor.
6. **Usta**: Fable 5.1 hem tasarımı yapıp hem kodluyor → `npm run dev` → `localhost:3000`.
7. Kontrol: hikaye bölümünde fotoğraflar Claude'un seçimi; işletme adı/sokak gibi değişkenler sonra verilir. Rezervasyon ayrı bölüm + gönderim, SSS.
8. **Canlı iterasyon**: "Header kısmındaki arka planı biraz daha hareketlendirelim. 21st.dev animations kısmında bir duman efekti eklenebilir. Seçenekleri bana ilet ve birlikte karar verelim." → 3 öneri (Ethereal Whispers vb.); biri "çok patlıyor, olmaz", Ethereal Whispers transparan yapılıp sadece duman gösterilirse iyi olur.
9. "Tamam bu şekilde olsun ama header'la birleştir; sanki sağ alttan ve sol üstten smoke efekti geliyor gibi olsun." → Claude MCP'den komponent prompt'unu kopyalayıp uyguluyor.
10. Bu noktada 5 saatlik limitin %94'ü dolmuş (2 sa 40 dk kala); sonuç videoda tam gösterilmiyor.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Mimar: `/design Bir web sitesi istiyorum. Bu burgerci web sitesi olacak. Ana sayfa, menü, rezervasyon, hikaye ve iletişim.` (yalnız yerleşim/dizayn istendi; tam metin Drive'da)
- Malzeme: `21st.dev araçlarıyla şu blokların her biri için 2-3 component adayı bul ve bana linkleriyle listele: scroll'da büyüyen medya herosu, kategori seçici çipler, fotoğraf bento galerisi, …`
- Seçim devri: `Hepsini senin seçimine bırakıyorum. Gerekli placeholder görselleri Unsplash sitesinden al koy.`
- İterasyon: `Header kısmındaki arka planı biraz daha hareketlendirelim. 21st.dev animations kısmında bir duman efekti eklenebilir. Seçenekleri bana ilet ve birlikte karar verelim.`
- Düzeltme: `Tamam bu şekilde olsun ama header'la birleştir; sanki sağ alttan ve sol üstten smoke efekti geliyor gibi olsun.`

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- **Kararı AI'ya bırakma**: "Ev yaptırırken kimse ustadan evi tasarlamasını beklemez. Seçen benim, birleştiren o."
- **AI slop ↔ ödüllü tasarım karşılaştırması** (anti-slop kuralları):
  - Inter font ↔ karakterli, cesur tipografi
  - Mavi-mor gradyan ↔ iki renkli cesur palet veya disiplinli siyah-beyaz
  - Ortalanmış hero + 3 yuvarlak kart ↔ asimetri, grid, kırık/farklı yerleşim
  - "Daha hızlı, daha akıllı" genel lafları ↔ sadece o markanın söyleyebileceği cümleler
- Önce iskelet (Design skill), sonra blok başına **insan yapımı** komponent seçimi (21st.dev), en son birleştirme + tek temaya uydurma + ekran görüntüsüyle öz-denetim.
- Animasyonlar: scroll'da büyüyen hero (yıldız ürün üstümüze geliyor), chip seçim efekti, animasyonlu bento galeri, resme tıklayınca animasyonlu gösterim, header'da duman.
- Logo: sistemsiz olsa "ikon + düz yazı" olurdu; sistemle farklı bir logo/wordmark yapısı.
- Görseller Unsplash'tan otomatik.

## Hatalar ve çözümleri
- Duman önerilerinden biri "çok patlıyor" → reddedildi; seçilen efekt transparan + header'a entegre + köşelerden gelen yön tarifiyle uyarlandı.
- **Kullanım limiti**: Fable 5.1 Max 5x planında session'ı çok hızlı tüketiyor (küçük işte %15-20, video sonunda 5 saatlik limitin %94'ü). Design skill de token yiyor. Öneri: dengeli kullan, "1 saat aralıksız + 10 alt ajan" yapma.
- 21st.dev ücretsiz plan günde 2 kopyalama → ücretli plan (veya MCP ile).

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
Müşteriye satış yok. Maliyet bilgileri: Fable 5.1 10 $/50 $ per milyon token, cache read 0,25 $ (ajans işlerinde %45'e varan tasarruf iddiası); Claude Max 5x kullanıyor; 21st.dev ücretli plan 3 aylık ~1.200 TL. Hedef "ajans kalitesinde" site.

## Bizim fabrikaya alınacaklar (somut)
1. **Mimar-Malzeme-Usta'yı üretim hattının iskeleti yap**: (a) `/design` veya planlama adımında sadece sayfa/blok listesi + tasarım yönü (kod yok), (b) her blok için 21st.dev'den 2-3 aday + link → biz/ müşteri seçer, (c) Claude birleştirir, tek temaya uydurur, ekran görüntüsüyle öz-denetim.
2. **21st.dev MCP'yi** Claude Code'a bağla (kopyalama limitine takılmadan arama + prompt çekme); sektör başına hazır blok listesi: restoran → scroll-expansion hero, kategori chip'li menü, bento galeri, rezervasyon saat seçici, testimonials, harita+çalışma saatleri, SSS.
3. **Blok listesi prompt şablonu**: "21st.dev araçlarıyla şu blokların her biri için 2-3 component adayı bul ve linkleriyle listele: …" — müşteri onayı için link listesi doğrudan gönderilebilir.
4. **Anti-slop 4 kuralı** kalite kapısına ekle (Inter yok; mavi-mor gradyan yok, 2 renk ya da siyah-beyaz; ortalanmış hero + 3 kart yok; jenerik slogan yok, sadece o markanın cümlesi).
5. Placeholder görseller için "Unsplash'tan al koy" talimatı; teslimden önce gerçek/AI üretimi görselle değiştirme.
6. İterasyon kalıbı: "Seçenekleri bana ilet ve birlikte karar verelim" → efekt önerilerini hep 3 seçenek olarak iste, sonra yön/opaklık tarifiyle ince ayar.
7. Bütçe kuralı: tasarım skill'i + büyük model pahalı — iskelet aşamasında kısa kullan, kodlama/rutin işte efor düşür; site başına token/limit takibi.
8. Drive'daki prompt paketini (https://drive.google.com/file/d/170kv8ReNYtKFPvK5fm3tnbxwVfW4L4BH/view) erişim olduğunda indirip `kaynaklar/` altına kaydet.
