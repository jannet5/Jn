# 3-Step Claude Code System For Pro-Level App Designs — Sean Kochel — 15 dk — https://www.youtube.com/watch?v=wiQMsQwWJQE

> Kaynak notu: Transkriptin tamamı okundu. Dört custom slash command'ın (`/extract-it`, `/expand-it`, `/merge-it`, `/design-it`) **tam metinleri transkriptte yok**; Skool grubunda ücretsiz paylaşılıyor (aşağıdaki link). Burada yalnızca videoda anlatılan işlevleri aktarıyorum; prompt'ları uydurmadım. Proje Next.js ama tasarlanan ekranlar **mobil** uygulama ekranları.

## Tek paragraf: ne yapıyor, sonuç ne
Sean, "tasarım sistemi olmadan uygulama yapmak rastgele malzemeyle 5 yıldızlı yemek pişirmek gibi" diyerek 3 aşamalı bir sistem kuruyor: (1) **İlham madenciliği** — Mobbin'den farklı dikeylerdeki iki uygulamadan (Airbnb: sıcak, davetkâr, yoğun bilgiyi net sunan; Blackbird: sade, dağınık olmayan navigasyon) ~10'ar ekran indir; (2) **Meta anlama** — `/extract-it` ile her grubun renk paleti, tipografi, metin stilleri, component stilleri ve tasarım felsefesini `competitor-analysis.md`'ye çıkar (model, `<pondering>` etiketleriyle düşünen "sanatçı ruhlu UX tasarımcısı" rolünde), `/expand-it` ile "bu hissi nasıl yaratırız" seviyesine indirip `styles.md`'ye yaz; (3) **Kodlama** — `/merge-it` ile iki stili kendi uygulama fikrine (Nano Banana ile ev iç mimari görsel üreten uygulama) füzyonla, token tabanlı kendi tasarım sistemini çıkar; `/design-it` ile her ekranın **3 varyasyonunu** ve farklı durumlarını (boş, işleniyor, limit doldu/paywall) üret. Sonuç: boş home ekranı, AI işleme ekranı ve kredi limiti ekranı için 3'er varyasyon [11:23–13:46].

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Claude Code custom slash command'lar**: `/extract-it`, `/expand-it`, `/merge-it`, `/design-it` (Cursor/Gemini CLI/Codex'te normal prompt olarak da kullanılabilir) [14:30]
- Prompt/command metinleri (ücretsiz): https://www.skool.com/tech-snack ve kaynak sayfası https://www.skool.com/tech-snack/classroom/0ca89703?md=bdbbcedc5f3a4239bd02ff112e7bf3f9
- **Mobbin** (ekran arşivi) [01:29]
- Referans uygulamalar: Airbnb, Blackbird [02:17–03:04]
- Uygulama fikri: **Nano Banana** (görsel üretim) ile ev sahibi/kiracılar için iç mimari tasarım [02:17]
- Proje: basit **Next.js** projesi [05:16]
- Çıktı dosyaları: `competitor-analysis.md`, `styles.md` (ve merge sonrası birleşik style guide)
- MCP yok.

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
**Aşama 1 — İlham madenciliği [01:19–04:22]**
1. Kendi dikeyini kopyalama; **farklı dikeylerden** his olarak uyan uygulamaları seç (iç mimari uygulaması için Airbnb + Blackbird) [02:17].
2. Her birinden ~**10 ekran** indir: o uygulamanın çekirdek estetiğini temsil eden ekranlar [03:50].
3. Gerekçe: rastgele kopyalama değil, başarılı uygulamalardan **pattern ödünç alma** [03:50].

**Aşama 2 — Meta anlama [04:22–08:15]**
4. Sorun tespiti: çoğu kişi görseli LLM'e verip "buna göre tasarla" der; UX/UI'ın **neden** öyle hissettirdiğine dair meta anlayış yok [04:33].
5. Next.js projesinde `/extract-it` çalıştır; her görsel grubu için **neyi sevdiğini** yaz → komut görselleri okur, her birini style guide'a çevirir: renk paleti, tipografi, metin stilleri, component stilleri, tasarım felsefesi → `competitor-analysis.md` [05:16–06:47].
6. Komutun özelliği: model "heady artistic" UX tasarımcısı rolünde, uygulamanın estetiği, uyduğu ilkeler ve kullanıcıya nasıl bir his verdiği üzerine düşünür; düşünce zincirini **`<pondering>` etiketlerine** sarar [06:47].
7. `/expand-it`: competitor analysis'i bir seviye derine al — istenen hissi nasıl yaratırız, tasarım sisteminin felsefesi ne; how-to + felsefe + ham style guide → **`styles.md`** [07:31–08:15].

**Aşama 3 — Kodlama ve üretim [08:15–11:23]**
8. `/merge-it`: iki uygulamanın stil sistemine **kendi uygulama fikrini** füzyonla — "XYZ yapan bir uygulama, şu kitle için; bana diğer iki uygulama kadar detaylı style guide, felsefe ve implementation guide çıkar" → kendi **token tabanlı** tasarım sistemi, tek kapsamlı style guide [09:01–09:48].
9. `/design-it <ekran tarifi>`: istenen ekranın örneğini/tarifini ver → **3 varyasyon** üretir (ör. kredisi biten kullanıcıya gösterilen ekran) [09:53–10:40].
10. Durumları ayrı ayrı brainstorm et: veri yok, çok veri, hata, kullanıcı aksiyonu gerekiyor [10:40].
11. Üretilen 3 ekran × 3 varyasyon değerlendirmesi [11:23–13:46]:
    - Boş home: (1) minimalist "getting started" + fotoğraf yükle + başkalarının alanları — "clean, breathable, subtle"; (2) carousel + **before/after slider** (sosyal kanıt yeri); (3) story-driven (beğenmedi).
    - AI işleme: gradient arka planlı işleme, token akıtır gibi "cozy, natural, bold statement, modern minimal" etiketleri; açık analiz görünümü; "discovering your style: timeless, contemporary, eclectic" — 1 ve 3 favori.
    - Limit doldu: koyu versiyon uymadı; "warm and encouraging" versiyon: "bugün yeterince dönüşüm yaptın, yarını mı beklersin yoksa unlimited planı mı açarsın" — yumuşak paywall.

## Kullandığı prompt'lar (varsa aynen)
- Dört komutun tam metni videoda okunmuyor; Skool kaynak sayfasında: https://www.skool.com/tech-snack/classroom/0ca89703?md=bdbbcedc5f3a4239bd02ff112e7bf3f9
- Videoda anlatılan `/expand-it` niyeti (Sean'ın sözlü aktarımı, aynen) [07:31]:
```
Let's dive deeper on what the philosophy of this design system actually is. Then once you are done with that, I want you to put it all the how-to, the philosophy of it, the raw style guide. I want you to put all of that into a styles.markdown file.
```
- `/merge-it` niyeti (sözlü aktarım, aynen) [09:30]:
```
Hey, we're trying to build an app that does XYZ. I need you to build me a style guide and a system of philosophy and an implementation guide just like we got in detail for these other two apps, but for my specific idea based on what I am building and who I'm building it for.
```
- `/extract-it` için kullanıcı girdisi: her görsel grubu için "neyi sevdim" açıklaması (ör. Airbnb: "warm, welcoming, very clear aesthetic despite delivering a lot of information"; Blackbird: "very simple, uncluttered navigation") [02:57–03:50].

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **İki farklı dikeyden his karışımı** (sıcaklık + sadelik) → tek uygulamayı kopyalamaktan özgün sonuç.
- **"Neden çalışıyor" katmanı**: renk/tipografi listesinin ötesinde kullanıcı psikolojisi ve his; `<pondering>` ile zorunlu düşünme.
- **Token tabanlı kendi sistemi**: rakip stilini doğrudan uygulamak yasak; önce kendi ürününe uyarlanmış tokenlar.
- **Her ekran 3 varyasyon + durum matrisi** (empty / loading-AI processing / error / limit-paywall / çok veri) → profesyonel ekiplerin cilası.
- AI işleme ekranında akan etiketler ve gradient arka plan gibi "bekleme anını eğlenceli yapan" mikro içerik.

## Hatalar ve çözümleri
- Bazı varyasyonlarda **şeffaflık (transparency) sorunları** [11:56] ve mobil için optimize olmayan "janky" görünüm [12:36] → bunlar keşif (brainstorm) çıktısı; seçilen varyasyon sonra rafine edilir.
- Koyu limit ekranı uygulamanın sıcak tonuna uymadı → style guide'daki "warm" felsefesine uyan varyasyon seçildi [13:11].
- Genel hata kalıbı: görseli doğrudan LLM'e verip tasarlatmak → meta anlama adımı (extract → expand) eklenerek çözülüyor [04:33].

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **4 komutluk zinciri fabrikaya slash command olarak ekle** (`.claude/commands/`): `ilham-cikar` (extract) → `ilham-derinlestir` (expand) → `marka-birlestir` (merge) → `ekran-tasarla` (design). Sean'ın orijinal metinlerini Skool linkinden alıp uyarlayalım.
- `extract` komutuna `<pondering>` düşünme bloğu ve "senior UX designer, aesthetics + psychology + how it makes users feel" rolü konulsun.
- Çıktı dosya standardı: `design/competitor-analysis.md` → `design/styles.md` → `design/design-system.md` (token: color, typography, spacing, radius, shadow, motion). Expo tarafında bunlardan `theme.ts` üretilir.
- **Farklı dikey kuralı**: müşteri sektörüne ek olarak his olarak uyan 2 farklı dikeyden referans seç (ör. kuaför uygulaması → Airbnb sıcaklığı + bir fintech'in sadeliği).
- **Ekran durum matrisi zorunlu**: her çekirdek ekran için empty / loading / AI-processing / error / limit-paywall / dolu-veri durumları tasarlanmadan build'e geçme. Android ham `Alert` yerine bu durumların custom component'leri.
- **3 varyasyon kuralı**: `/ekran-tasarla` her ekran için 3 varyasyon üretsin, insan seçsin, seçilen rafine edilsin.
- Paywall/limit ekranı tonu marka felsefesine bağlı ("warm and encouraging") — fabrika paywall şablonuna ton parametresi.
