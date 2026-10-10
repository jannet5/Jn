# How I Use Claude Code to Make INSANE Instagram Carousels (for beginners) — Duncan Rogoff — 11 dk — https://www.youtube.com/watch?v=jFAH0txMwiI

## Tek paragraf: ne anlatıyor
"Bütün AI carousel'ler aynı görünüyor çünkü herkes 'make me a cool Instagram carousel' diyor." Çözüm: Pinterest'ten beğenilen bir görselin ekran görüntüsünü Claude chat'e verip tek prompt'la **design system (HTML) + design MD dosyası** üretmek; bu klasörü Claude Code'da açmak; Higgsfield MCP'yi custom connector olarak bağlayıp görselleri markaya uygun ürettirmek; ardından "hook → acı → adımlar → sonuç → CTA" yapısını yeniden kullanılabilir bir **skill**'e ("Aurora carousel") dönüştürmek. Skill her çalıştırmada slaytları + Instagram caption'ı + LinkedIn gönderisini üretiyor; zamanlama için Blotato öneriliyor.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Yaratıcı: eski Apple/PlayStation/Nissan art director'ü, "altı haneli AI ajansı" sahibi.
- Gelir: Skool topluluğu "Claude Code Club" — **9 $** (prompt'lar, skill'ler, carousel skill prompt'u burada; videoda verilmiyor).
- Ajans açısından: design system tek seferlik üretilir ve carousel dışında landing page, e-posta, bülten için de kullanılır ("aynı design system ile siteyi de yaptım") → markalama paketi olarak satılabilir.

## Müşteri bulma: hangi kanal, hangi mesaj
- YouTube inbound → 9 $ topluluk. Carousel CTA'sı da topluluğa ("The CTA is going to lead to the Claude Code Club").

## Üretim süreci: hangi araçlar, hangi otomasyonlar
Araçlar: Pinterest (referans), Claude desktop chat, Claude Code, Higgsfield (higsfield.ai/mcp — görsel/video), Blotato (zamanlama).
1. **Referans** — Pinterest'te "graphic design" ara, beğendiğin stilin ekran görüntüsünü al.
2. **Design system** — Claude chat'te (cowork/code değil) prompt (aynen): "Turn this into a design system in an HTML file and a design MD file." Çıktı: kurallar, açık/koyu palet, gradyan arka planlar, tipografi hiyerarşisi, kompozisyon/yerleşim + Claude'un okuyacağı design.md. "Download all".
3. **Claude Code** — Desktop'ta Code sekmesi → "open folder" (ör. `YT Carousel`), zip'i aç, klasörü `design system` olarak adlandır.
4. **Higgsfield MCP** — higsfield.ai/mcp'den URL kopyala → Claude → Connectors → Manage → "Add custom connector" → ad: Higgsfield, URL yapıştır → giriş → Allow. İsteğe bağlı "always allow".
5. **Test** — Prompt (aynen): "I want you to use the Higgsfield connector and generate an image about AI and robotics based off of the design system that is inside of the YT Carousel folder." Claude design.md'yi okuyup kendi görsel prompt'unu yazıyor ("cinematic editorial photograph of a humanoid robot torso ... near black void darkness lit by volumetric atmosphere").
6. **Carousel yapısı** — (1) kapak = hook, (2) acıyı doğrudan adlandır, (3) numaralı adımlar, (4) sonuç, (5) CTA ("comment this word and I'll send you...").
7. **Skill** — Ücretli prompt ile "Aurora carousel" skill'i oluşturuluyor; yeni skill için Claude'u yeniden başlat; `/aurora` + konu/ders metni yapıştır.
8. **Varsayılanları skill'e yaz** — Skill ilk çalıştırmada hedef kitle, teklif, ses tonunu soruyor; cevapladıktan sonra "update our skill with this information" → bir daha sormuyor. Düzeltmeler de skill'e: "update my skill to move the text a little bit higher up".
9. **Çıktı** — slaytlar + Instagram caption + LinkedIn gönderisi → kopyala ya da Blotato ile zamanla.

## Sosyal medya / reklam
- Carousel yapısı: Hook → Pain → Steps → Result → CTA.
- Tek üretimden iki platform: Instagram caption + LinkedIn post.
- Reklam yok.

## Hatalar / uyarılar
- Skill oluşturan prompt ücretli duvarın arkasında; videoda yok (biz kendimiz yazacağız).
- Higgsfield "always allow" → onaysız ücretli üretim; maliyet kontrolü kaybolur.
- Yeni skill ancak yeniden başlatma/yeni oturumda görünür.
- Higgsfield fiyatı videoda verilmiyor.

## Bizim ajansa alınacaklar (somut)
1. **Müşteri başına `design.md`** — Onboarding'de müşterinin mevcut sitesi/logo/beğendiği 1 referans → "Turn this into a design system in an HTML file and a design MD file." Aynı dosya hem web sitesi (Claude Code ile ürettiğimiz site) hem sosyal medya hem reklam kreatifinde kullanılır → web + sosyal tutarlılığı satış argümanı.
2. **Kendi carousel skill'imiz** (Claude Code skill, TR): girdi = konu/blog/hizmet; adımlar = müşteri klasöründen design.md + `musteri-profili` (hedef kitle, teklif, ses tonu — ilk çalıştırmada sorulup skill/profil dosyasına yazılır) → Hook/Acı/Adımlar/Sonuç/CTA → HTML→PNG → IG caption + LinkedIn/Facebook metni.
3. CTA kalıbı TR KOBİ'ye: "Yorumlara 'FİYAT' yaz, DM'den gönderelim" (ManyChat/DM otomasyonuna bağlanır).
4. Görsel gerektiğinde Higgsfield/Nano Banana benzeri MCP; ama "always allow" kapalı, müşteri başına aylık görsel kotası.
5. Zamanlama: Blotato yerine kendi n8n + Meta Graph API hattımız (bkz. L3NUp2XP_h0 planı), onay kapısıyla.
