# Watch me Design a Mobile App From Scratch in 32 Minutes (Claude /design & Fable 5) — Build Great Products — 32 dk — https://www.youtube.com/watch?v=76_h94U4ztQ

> Kaynak notu: Transkriptin tamamı okundu. Kod yazılmıyor; konu **mobil uygulamanın build öncesi tasarımı** (Claude Code desktop içindeki `/design` canvas'ı) + Higgsfield MCP ile illüstrasyon + design system dokümantasyonu. Build ayrı bir videoda yapılacak.

## Tek paragraf: ne yapıyor, sonuç ne
Chris (15 yıllık ürün/tasarım danışmanı) "fikir → hemen build" yerine **Define → Design → Spec/Build** sürecini savunuyor ve "AI slop"tan kaçmanın 3 anahtarını veriyor: **iyi referanslar, fikir netliği (key screen'ler + magic moment), öğrenilmiş zevk (taste)**. Örnek: "Ride It, Share Your Adventure" — tema parkı ziyaretini **Spotify Wrapped tarzı** paylaşılabilir özete çeviren mobil uygulama. Claude Code desktop'ta boş klasörde `/design` + Dribbble referans görseli ile önce ekran **outline'ı** istiyor, sonra 4 statik ekran (full-color arka planlar, flat vektör illüstrasyon); canvas'ta font (Lexend) ve ağırlık (bold → regular) değiştiriyor; ikinci bir referansla (izometrik line-art) **alternatif yön B** ekletiyor; AI vektör illüstrasyonları kötü olduğu için Claude'a görsel üretim prompt'ları yazdırıp **Higgsfield MCP** ile (model: ChatGPT Image 2) gerçek illüstrasyonlar üretip artboard'lara yerleştiriyor; son olarak seçilen A1 ve A4 ekranlarından Product OS "design system" skill'i ile **`design.md` + `design.html`** üretiyor ve agent kurallarına (agent.md / CLAUDE.md) "bu tasarım sistemine uy" ekleniyor.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Claude Code desktop app** + **`/design`** komutu (Claude Design'ın Claude Code içine gelmiş hâli; canvas, properties panel, font/ağırlık düzenleme) [10:35–16:18]
- Model: **Fable 5**, reasoning **high** ("çoğu iş için high'dan fazlasına gerek yok") [12:20]
- **Higgsfield MCP** — higgsfield sitesinde `/mcp` sayfası → "Claude Code" seçeneği → kurulum komutunu kopyala, Claude Code oturumuna yapıştır, bağlantıyı onayla [21:26–22:09]; görsel modeli: **ChatGPT Image 2** [22:09]
- **eyedropper.design** — Chris'in tasarım sistemlerini bulutta saklayıp **MCP** ile her agent'a veren aracı; `/design` mevcut `design.md`/design system'den çalışır [13:06]
- **Product OS** (Chris'in ücretli sistemi) — **design system skill** + agent'ın design system'e uymasını sağlayan skill'ler: https://skool.com/aiapps/about [17:51, 26:44]
- Alternatif tasarım canvas araçları: **Magic Path**, **paper.design** [20:47]
- Referans: **Dribbble** (flat vektör illüstrasyonlu app; izometrik 3D line illüstrasyon), Spotify Wrapped yılları [08:59, 18:29]
- Açıklama linkleri: ücretsiz playbook'lar https://skool.com/claudebuilders/about · 1-1 https://www.buildgreatproducts.com/product-studio · 6 saatlik Claude Code kursu https://youtu.be/_0E-dzhjCoY?si=P8wW_byqWR1ykTP-

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
**A. Define (tasarım öncesi) [01:10–10:18]**
1. Ürün teklifi 6 soru: hedef müşteri, acı (pain), sonuç (outcome), mekanizma, fiyatlama, kanıt (proof) [02:26].
2. Tasarımda kritik 3 nokta: **magic moment**, landing page / **App Store listing**, **onboarding flow** [03:38].
3. Fikir süreci: fikir → pazar araştırması → zaten para ödenen "kazanan fikirler" → kendi versiyonunu farklılaştır (eski görünen kategoriyi modernize et / AI ekle bonus) → magic moment'ı belirle (Wrapped tarzı gün özeti) → key screen'leri tasarla → spec & build [06:53–08:20].
4. Wrapped ekranı kriterleri: **paylaşılabilir** (büyüme motoru), **personality archetype** ekranı (Spotify verisinde en çok paylaşılan ekran), renkli/oyuncu/scroll durduran enerji [08:59–10:18].

**B. `/design` ile ilk yön [10:35–17:03]**
5. Claude Code desktop → yeni session → yerel boş proje klasörünü seç [10:35].
6. Dribbble'dan seçtiği referans görseli yapıştır (flat vektör, renkli, düz renk arka planlar) [10:56].
7. `/design` + Prompt 1 (önce outline iste) [11:35–12:20]. Model Fable 5 high.
8. Claude'un cevabı [13:06–14:23]: klasör boş → design system yok → referanstan çıkardı: "flat chunky vector illustration, bold rounded geometric type, saturated candy palette, big radius cards, playful asymmetry (illustrations bleeding off card edges, tilted objects, small confetti)". Telefon sensörlerinden veri (accelerometer, gyro, GPS, barometer → upside down sayısı, top speed, highest point, biggest drop, airtime, distance walked, ride vs queue süresi), check-in/ride log (ride sayısı, en çok binilen, ilk ride, streak, en uzun kuyruk), türetilmiş (park personality archetype, ride DNA yüzde dağılımı, percentile flex'ler, badge/milestone, crew, shareable summary card). Önerilen ekranlar: Your day at park / Thrill stats / Your ride day / Archetype + share card.
9. Claude'un 3 sorusuna cevap: **static** mockup (keşif için doğru), **uydurma park/ride adları** (trademark), archetype ayrı ekran mı share card'la birleşik mi [14:23–15:07].
10. Canvas'ta 4 ekran → properties panel: font **Lexend**'e çevir, edit ile **bold → regular** tüm ekranlarda [15:30–16:18].

**C. Alternatif yön B [17:51–19:16]**
11. Dribbble'dan izometrik 3D line illüstrasyon + flat renk referansı → ekran görüntüsü → `/design` + Prompt 2 → canvas'a varyant seti eklendi [18:29–19:16].
12. Karşılaştırma: B'nin outline illüstrasyon + outline buton stili ve fontu daha iyi; karar: **A'nın renk yönü + B'nin illüstrasyon stili** [19:16, 22:09].

**D. Gerçek illüstrasyonlar (Higgsfield MCP) [19:16–26:27]**
13. Prompt 3: her ekranın ana illüstrasyonu için stil + konu içeren görsel üretim prompt'u → Claude prompt'ları bir **markdown dosyasına** kaydetti [20:02–21:26].
14. Higgsfield `/mcp` → Claude Code komutunu kopyala → aynı session'a yapıştır → bağlantıyı onayla [21:26–22:09].
15. "Direction A cover illustration'ı Higgsfield ile üret" + takip mesajı "use ChatGPT image 2 ... as the model" → **interrupt** ile akışı kesip modeli düzeltti [22:09–22:56].
16. Claude "minimal" alternatif önerdi (sadece coaster loop, ferris wheel, drop tower); kalabalık versiyonu seçti → Prompt 4 [22:56–23:40].
17. Görselleri artboard'lara yerleştir (Claude'un önerdiği yanıt) [24:20–25:00].
18. Rötuşlar: "Wrapped" kelimesini kaldır (Spotify trademark), Prompt 5 [25:00–25:42].
19. A1 ve A4'ü canvas'ta bir yere taşı (design system referansı) [26:27].

**E. Design system dokümantasyonu [26:44–29:59]**
20. Product OS design system skill + Prompt 6 → **`design.md`** (agent için token + stiller) ve **`design.html`** (insan için görsel rehber: renkler, tipografi, spacing, radius, elevation, component'ler, do's & don'ts) [27:46–29:12].
21. Skill, agent kural dosyalarına (agent.md ve "code.md" — muhtemelen CLAUDE.md) bu dosyalara uyma kuralını ekliyor [29:12].

## Kullandığı prompt'lar (varsa aynen)
Prompt 1 — `/design`, referans görsel ekli [11:35]:
```
/design I'd like to create three or four designs for a mobile app following the design style of the attached image reference, but with full color backgrounds on each screen. The app that I'm designing here is an app for creating shareable Spotify Wrapped style recaps of your visits to a theme park that would show you things like the number of times you've been upside down, the top speed that you've been up to that day, and also a personality archetype for your day at that theme park. Can you give me an outline of what we can actually include on these screens before you design them? And I also want to make them really visually interesting and engaging with these kind of flat, colorful vector illustrations of things like theme park rides, and visuals that are related to visiting a theme park.
```
Prompt 2 — alternatif yön, yeni referans ekli [18:29]:
```
/design Can you add an alternate set of designs which use a more isometric line art style illustration and apply this style to the cards and the buttons as well, similar to the attached image reference so that I can compare between the two design directions.
```
Prompt 3 — illüstrasyon prompt'ları [20:02]:
```
Can you give me a prompt that includes the illustration style and the subject matter for each of the main illustrations on each of the four screens that I can drop into a tool like ChatGPT image two to create a vector style illustration image for each of the illustrations that we have inside of this app.
```
(Ardından: prompt'ları bir markdown dosyasına kaydettirdi [21:26].)

Higgsfield üretimi [22:09]:
```
Generate the direction A cover illustration with Higgsfield.
```
```
Use ChatGPT image 2 to do this as the model.
```
Prompt 4 [23:40]:
```
Generate all the other images for direction A.
```
Prompt 5 — rötuş [25:42]:
```
Let's make the button on the bottom of the first screen full width for tap to start. And can you also make the image on the last screen bigger as well?
```
Prompt 6 — design system skill [28:27]:
```
Use this skill to create a design system from the screens A1 and A4 so that we have a documented design system that we can use when we start building this app.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Referansla başla**: boş klasörde bile `/design` referans görselden stil sözlüğü çıkarıyor (chunky vector, rounded geometric type, candy palette, big radius, playful asymmetry, kenardan taşan illüstrasyon, eğik objeler, konfeti).
- **Önce outline, sonra çizim**: içerik/veri envanteri (sensör + check-in + türetilmiş metrikler) belirlenmeden piksel yok.
- **Her ekran tam renkli arka plan** — Wrapped tarzı paylaşılabilirlik.
- **İki yön, sonra hibrit** (A renk + B illüstrasyon).
- **AI vektör illüstrasyonu "funky" → gerçek görsel üretim modeli** (Higgsfield + ChatGPT Image 2) ile profesyonel illüstrasyon; stil + palet + konu içeren prompt'larla tutarlı set.
- Tipografi ince ayar: Lexend, bold yerine regular.
- Tam genişlik CTA ("Tap to start"), büyük kahraman görseli.
- Paylaşılabilir **archetype** ekranı = magic moment ("You were a thrill chaser...").
- Design system iki formatta: agent için `design.md`, insan için `design.html` (do's & don'ts dahil); agent kural dosyasına bağlanıyor → tüm ekranlarda tutarlılık.

## Hatalar ve çözümleri
- AI'nın kendi çizdiği vektör illüstrasyonlar bozuk/garip ("funky") [17:03] → Claude'a görsel prompt'ları yazdırıp Higgsfield MCP ile gerçek model üzerinden üretim.
- Higgsfield yanlış/varsayılan modelle başladı → takip mesajıyla model belirtildi + **interrupt** ile akış kesildi [22:09].
- Coaster rayı bazı görsellerde tuhaf → kabul edilebilir bulundu [24:20].
- "Wrapped" kelimesi Spotify trademark'ı → metinden çıkarıldı; park/ride adları uydurma [15:07, 25:00].
- Canvas'ta bir öğeyi seçip chat'e "bunu düzenle" diye ekleme yok (Claude Design'dan farklı) → değişiklikler yazılı prompt'la [25:42–26:27].
- Boş projede design system yok → `/design` referans görselden çalıştı; ideal olan önce `design.md` / eyedropper MCP [13:06].

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Fabrika aşaması "Define"**: her müşteri için 6 soru formu (müşteri, pain, outcome, mechanism, pricing, proof) + **magic moment** + 3 kritik nokta (magic moment ekranı, App Store listing, onboarding) — tasarımdan önce zorunlu.
- **`/design` akış şablonu**: (1) referans görsel(ler) + "önce outline ver" → (2) Claude'un sorularına cevap (static, uydurma isimler) → (3) 3–4 key screen → (4) ikinci referansla alternatif yön → (5) hibrit karar.
- **Illüstrasyon hattı**: "her ekran ana illüstrasyonu için stil + palet + konu prompt'u yaz, `design/illustration-prompts.md`'ye kaydet" → Higgsfield MCP (ChatGPT Image 2) ile üret → `assets/illustrations/` → Expo'da `expo-image` ile kullan. AI'nın SVG/vektör çizmesine güvenme.
- **Higgsfield MCP** ve **eyedropper.design MCP**'yi değerlendirme listesine ekle (ikincisi: müşteri tasarım sistemlerini bulutta tutup her agent'a MCP ile vermek — çok müşterili ajans için uygun).
- **Design system çıktı standardı**: `design.md` (agent) + `design.html` (müşteriye/insana görsel rehber, do's & don'ts) → CLAUDE.md'ye kural: "Tüm UI `design.md` token ve component'lerine uyar; yeni component uydurma."
- Seçilen 2 en iyi ekrandan (ör. kapak + archetype) design system türetme — tüm ekranlardan değil.
- Trademark kontrolü checklist'e: marka kelimeleri ("Wrapped" vb.), gerçek mekân/ürün adları.
- Model ayarı notu: tasarım işleri için yüksek ama "max" olmayan reasoning yeterli.
- Paylaşılabilir "özet kartı" pattern'i (Wrapped tarzı) işletme uygulamalarında büyüme özelliği olarak şablona (ör. salon/kafe "yılın özeti").
