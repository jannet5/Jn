# WEB TASARIM TARİH OLDU ! (Claude Code + Stitch) — İsa Nurdoğdu — 10 dk — https://www.youtube.com/watch?v=PJ4JAim5-jQ

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu).
## Tek paragraf: ne yapıyor, sonuç ne
Önce Claude Code'a sadece ajans bilgisini içeren bir doküman verip "landing page hazırla" diyerek tipik "mavi-mor-yeşil, AI yaptığı belli" demode bir site çıkarıyor (kötü örnek). Sonra tasarımı **Google Stitch**'te (ücretsiz, Google hesabıyla, model: Gemini 3.1 Pro) yaptırıyor: Pinterest'ten beğendiği bir AI ajans sitesinin ekran görüntüsünü verip "bu tarzda, hero aynı yapıda" diyor. Stitch tipografi + marka renkleri + ton içeren bir tasarım sistemi ve **DESIGN.md** dosyası üretiyor. Stitch'ten kodu panoya kopyalayıp, DESIGN.md'yi ve ajans dokümanını proje klasörüne koyup Claude Code'a (VS Code terminalinde) "Next.js + Tailwind, component bazlı, bu kodu ve DESIGN.md'yi kullan, design sisteme sadık kal, responsive olsun" diyor. Sonuç Stitch tasarımının birebir aynısı, çalışan butonlu bir site; sonradan eklenen login sayfası da DESIGN.md sayesinde aynı tipografi/renkte. Sonunda GitHub → Vercel ile ücretsiz yayınlıyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Google Stitch** (Stitch 2.0; "Google Stitch" diye aranıyor, ücretsiz, Google hesabıyla giriş) — web/mobil (uygulama) modu; modeller: **3 Flash** (hızlı, daha düşük kalite), **3.1 Pro** (önerilen), **Redesign** (mevcut tasarımı revize: ekran görüntüsü veya **web sitesi URL'si** verilebiliyor, sitenin bilgi/tasarımını çekiyor), en altta yeni beyin fırtınası/fikir modu. Link videoda/açıklamada verilmiyor.
- İlham kaynakları: **Godly** (sonsuz kaydırmalı web tasarım galerisi), **Dribbble** (mobil uygulama/product design), **Pinterest** ("AI ajans website" araması).
- **Claude Code** — VS Code içinde, New Terminal → `claude`.
- Teknoloji: **Next.js + Tailwind CSS**, component bazlı yapı.
- **GitHub** (repo) + **Vercel** (ücretsiz deploy, "Add New → Project → Continue with GitHub → Import").
- **NotebookLM** (Google, ücretsiz) — site içeriği için video/görsel/podcast/infografik üretimi olarak öneriliyor, Claude Code'a entegre edilebildiği söyleniyor (ayrı videoda).
- Açıklamadaki tek link: https://isanurdogdu.com (kanal sahibinin sitesi).

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. (Karşılaştırma) Claude Code'a ajans bilgi dokümanını verip "kendi yapay zeka otomasyon ajansım için bir landing page hazırla" → jenerik mavi/mor/yeşil site.
2. Stitch'e gir → Web modu → model **3.1 Pro**.
3. Doğrudan prompt yazma yerine ilham topla: Godly / Dribbble / Pinterest. Pinterest'te beğendiği tasarımın ekran görüntüsünü al.
4. Ekran görüntüsünü Stitch'e ver + prompt: "Bu tarzda bir AI ajansı landing page'i oluştur. Hero section aynı yapıda olsun. Marka adı: Operico." → Generate.
5. Stitch tasarım sistemi üretiyor (tipografi, marka renkleri, marka tonu). Sağdaki editörde **Design System** → tema light/dark değiştirilebilir → **DESIGN.md**'yi indir (Download).
6. İnce ayar: bir bileşeni seç → sağ tık → seçenekler: anında prototip, yeniden üret (regenerate), mobile app versiyonu, **varyantlar** (sadece görsel / sadece font / sadece layout değiştir — tasarımı kaybetmeden spesifik değişiklik). Hero kutusunu seçip "Hero kısmının arka planındaki renkleri daha canlı yap".
7. Dışa aktar: tasarıma tıkla → **Dışa aktar → Kodu panoya kopyala**.
8. Masaüstünde temiz klasör → VS Code → New Terminal → `claude`. Klasöre ajans bilgi dokümanını (Operico .docx) ve DESIGN.md'yi koy.
9. Claude Code prompt'u (aşağıda) — dokümanı ve DESIGN.md'yi `@` ile referans göster, Stitch kodunu yapıştır.
10. Claude Code `localhost:3000`'de site veriyor → Stitch tasarımının aynısı; "Demo talep et" butonu aşağı kaydırıyor vb.
11. İterasyon: "Giriş yap dediğinde basit bir login ekranı oluştur" → `/login` sayfası, aynı tipografi (DESIGN.md "projenin kilidi/ana dosyası").
12. Deploy: Claude Code'a "Projemi GitHub'a aktarmak istiyorum" → Claude seçenek 1/2 sunuyor; önerilen: GitHub'da elle repo oluştur, repo URL'sini Claude'a ver, o push'lar.
13. Vercel'e kayıt (ücretsiz) → Add New → Project → Continue with GitHub → repoyu seç → Import → Deploy → link.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Stitch (SS ile): `Bu tarzda bir AI ajansı landing page'i oluştur. Hero section aynı yapıda olsun. Marka adı: Operico.`
- Stitch bileşen düzenleme: `Hero kısmının arka planındaki renkleri daha canlı yap.`
- Claude Code ana prompt (videoda anlatılan yapı): `Bir AI ajansı için landing page oluşturacağız. AI ajansım hakkında bilgiler için doküman: @Operico.docx. Teknoloji: Next.js ve Tailwind CSS olsun. Yapı component bazlı olsun. Frontend kodu: [Stitch'ten kopyalanan kod]. Design system dokümanı: @DESIGN.md. Bu design sisteme sadık kal. Renkleri, tipografiyi, kuralları aynen uygula. Responsive olsun (tüm cihazlarda uyumlu).` — "Bunlar genel bir prompt, sabit olarak yazabilirsiniz."
- İterasyon: `Giriş yap dediğinde basit bir login ekranı oluştur.`
- Deploy: `Projemi GitHub'a aktarmak istiyorum.`

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- Tasarımı Claude Code'a "tek atışta" bıraktırmamak: "tek atışlarda Claude Code tasarımda hâlâ zayıf, desteğe ihtiyaç duyuyor" → tasarım Stitch'te, kod Claude Code'da.
- Doğrudan prompt yerine **referans ekran görüntüsü** (Godly / Dribbble / Pinterest) ile başlamak.
- **DESIGN.md** = sitenin tasarım altyapısı; her yeni sayfada (ör. login) aynı tipografi/renk paleti korunuyor.
- Stitch'teki **varyantlar** ile sadece görsel/font/layout'u değiştirip tasarımı bozmadan denemek.
- "Design sisteme sadık kal, renkleri/tipografiyi/kuralları aynen uygula" cümlesi.
- Yan fayda: tasarımın bir kısmı ve DESIGN.md hazır verildiği için Claude Code daha az düşünüyor → **token maliyeti düşüyor**.
- Animasyon/görsel üretimi videoda yok.

## Hatalar ve çözümleri
- Sadece dokümanla "landing page hazırla" → demode mavi/mor/yeşil AI sitesi. Çözüm: Stitch'te referanslı tasarım + DESIGN.md + Stitch kodu ile Claude Code.
- Başka hata/çözüm videoda yok.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
Videoda yok. (Sadece maliyet: Stitch, GitHub, Vercel ücretsiz; "tamamen ücretsiz yapacağız". Figma hissesinin Stitch yüzünden tek günde %8 düştüğü iddiası açılışta geçiyor.)

## Bizim fabrikaya alınacaklar (somut)
1. **Stitch → DESIGN.md → Claude Code hattı** tasarım aşaması olarak: müşteri sektörüne uygun referans SS (Godly/Dribbble/Pinterest) → Stitch 3.1 Pro'da "bu tarzda [işletme] sitesi, hero aynı yapıda, marka adı X" → DESIGN.md indir → proje klasörüne koy.
2. **Stitch Redesign + URL**: müşterinin mevcut (eski) sitesinin URL'sini Stitch'e verip yeniden tasarım ürettirmek — satış görüşmesinde "sitenizin yeni hali" ön izlemesi için hızlı demo.
3. **Varyantlar** ile müşteriye 2-3 alternatif (sadece font/sadece görsel/sadece layout) sunmak; seçim sonrası DESIGN.md sabitlenir.
4. Claude Code ana prompt şablonunu fabrikaya sabit şablon olarak almak: işletme bilgi dokümanı `@` + Stitch kodu + `@DESIGN.md` + "sadık kal, renk/tipografi/kuralları aynen uygula, responsive". (Stack bizde tek dosya statik HTML ise "Next.js + Tailwind" kısmı proje tipine göre değişir.)
5. Ek sayfalar (menü, randevu, iletişim) her zaman DESIGN.md referansıyla üretilsin — tutarlılık kuralı.
6. Deploy seçeneği olarak GitHub + Vercel (Hobby, ücretsiz) akışı; repo URL'sini Claude'a verip push ettirme.
7. NotebookLM'i müşteri sitesi içerik üretimi (infografik/görsel) için değerlendirme listesine ekle (videoda detay yok).
