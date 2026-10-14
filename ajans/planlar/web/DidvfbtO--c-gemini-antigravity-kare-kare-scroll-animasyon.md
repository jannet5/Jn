# Web Tasarımcılarının Kabusu: Gemini 3.1 Pro + Antigravity — İsa Nurdoğdu — 12 dk — https://www.youtube.com/watch?v=DidvfbtO--c

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu).
## Tek paragraf: ne yapıyor, sonuç ne
Google'ın **Antigravity** editöründe **Gemini 3.1 Pro** ile, açıklamada paylaştığı uzun bir hazır prompt içeren **GEMINI.md** dosyasını proje klasörüne koyup "bu dosyayı oku ve bir gayrimenkul sayfası hazırla, şu görsel klasörünü kullan" diyerek tek promptla lüks villa sitesi çıkarıyor; model önce marka adı, estetik yön (tema seçenekleri), üç temel değer önerisi ve ziyaretçinin ne yapması gerektiği gibi sorular soruyor. Aynı yöntemle (GEMINI.md + ajans tanıtım TXT'si) kendi AI otomasyon ajansının landing page'ini kuruyor ve Lovable'ın aynı TXT'den ürettiği "mavi, belli ki AI" siteyle karşılaştırıyor; sonucu Framer'daki 60 $'lık ücretli şablonlarla aynı seviyede buluyor. GitHub + Vercel ile ücretsiz yayınlıyor. Son bölümde "scroll ettikçe parçalanan araba" hero animasyonu yapıyor: Nano Banana Pro ile ilk kare (araba) ve son kare (parçalanmış araba) görseli → fal üzerinde Kling 3.0 image-to-video (start+end frame, 12 sn) → MP4'ü JPG karelerine böl (289 kare) → `image/` klasörüne at → Antigravity'ye "kareleri scroll animasyonu olarak hero'ya ekle".

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Antigravity** (Google; kendi sitesinden ücretsiz indiriliyor) + model **Gemini 3.1 Pro**. Link videoda verilmiyor.
- **GEMINI.md prompt dosyası** (uzun hazır prompt, açıklamada): https://drive.google.com/file/d/11A_aMoq_3ZbNFQ90J_OpYqJUBViKYxXI/view — içeriği videoda okunmuyor, bizde henüz yok.
- Karşılaştırma için: **Lovable** (eski platform; Bolt, Emergent de anılıyor — "mavi siteler").
- Kalite referansı: **Framer** şablon mağazası (ücretli şablonlar ~60 $).
- **GitHub** + **Vercel** (Hobby plan, ücretsiz; kendi domain bağlanabilir).
- Görsel: **Nano Banana Pro** (Gemini içinde "Görüntü oluştur", Pro model) — görsel prompt'unu da Gemini'ye yazdırıyor.
- Video: **fal** platformu ("FI platformu" diye geçiyor) → **Kling 3.0** image-to-video (start image + end image); alternatif **Higgsfield AI** veya herhangi bir video model platformu.
- **MP4 → JPG dönüştürücü**: Google'da "MP4 JPG" aratıp herhangi bir online dönüştürücü; "tümünü zip olarak indir".
- Açıklamadaki diğer link: https://isanurdogdu.com

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
**A) Tek prompt site (villa demo + ajans sitesi)**
1. Antigravity'yi indir/aç → yeni klasör oluştur → GEMINI.md'yi klasöre koy.
2. Villa demosu: villa fotoğraflarını bir klasöre koy → prompt: GEMINI.md'yi oku, gayrimenkul sayfası hazırla, resim klasörünü kullan.
3. Model soru soruyor: marka adı, estetik yön (tema seçenekleri A/B/C… — "doğa" A seçildi), üç temel değer önerisi, ziyaretçi ne yapmalı. Cevaplar: "Lumina Villaları", "A seçeneği", "Kişiye özel yaşam alanı — bize ulaşın".
4. Ajans sitesi: `@GEMINI.md` → "dosyasını oku"; `@` ile ajans tanıtım TXT'sini (Opero kurumsal tanıtım belgesi) ekle → "bilgilerini barındıran yapay zeka otomasyon ajansım için bir landing page oluştur".
5. Tasarım sorusuna cevap: "epic yap, fantastik olsun" → arka planda görsel, farklı font, nav, TXT'den çekilmiş içerik, yatay yazılmış kart, soft geçişler, süreç kartları (sistem tasarımı, ihtiyaç analizi, tam entegrasyon, özel sistem çözümü), footer'da iletişim/linkler, menü tıklamaları ilgili bölüme kaydırıyor.
6. (Öneri) Web uygulaması ise paketler/fiyatlandırma bölümü eklenebilir.

**B) Yayın**
7. Antigravity'ye GitHub'a push'lamasını söyle (bilgi istiyor) → Vercel'de ücretsiz hesap, Hobby plan → New → Project → Continue with GitHub → içe aktar → domain bağla → deploy.

**C) Scroll'da parçalanan ürün animasyonu**
8. Gemini'ye: "Arabayı web sitesinde parçalamak istiyorum, bana prompt ver" → aldığı prompt ile Gemini "Görüntü oluştur" (Pro) → önden araba fotoğrafı → indir.
9. İndirdiği fotoğrafı tekrar yükle + parçalanmış hal prompt'u → "patlatılmış" araba görseli. Elinde first frame + last frame var.
10. fal → Kling 3.0 → image-to-video → start image URL = ilk görsel, end image = son görsel, süre **12 sn**, ses yok; prompt (Gemini'den alınmış): yumuşak, slow motion dönüşüm videosu → Run → indir.
11. Online MP4→JPG dönüştürücüye videoyu at → kareler → "tümünü zip olarak indir" (sıra önemli: 001, 002…).
12. Antigravity'de projede `image` klasörü oluştur, tüm kareleri içine bırak.
13. Prompt: GEMINI.md'yi oku; araba için landing page oluştur; görseller `image` klasöründe, 001'den 289'a kadar; her görsel bir frame; bu görsellerin birleşiminden oluşan scroll animasyonunu en başa hero bölümüne ekle → çalışıyor.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
- Villa: `GEMINI.md dosyasını oku ve bir gayrimenkul sayfası hazırla. Verdiğim resimlerin olduğu klasörü kullan.`
- Ajans: `@GEMINI.md dosyasını oku. @Opero kurumsal tanıtım belgesi bilgilerini barındıran yapay zeka otomasyon ajansım için bir landing page (tanıtım sayfası) oluştur.`
- Tasarım sorusuna cevap: `Epic yap, fantastik olsun.`
- Görsel prompt'u istemek (Gemini): `Bu şekilde kurgulamak istiyorum, arabayı web sitesinde parçalamak istiyorum. Bana bir prompt ver.`
- Kling: `Soft bir şekilde, slow motion bir dönüşüm videosu oluştur.` (prompt'un tam metni Gemini'den alınmış, ekranda okunmuyor)
- Scroll animasyonu: `GEMINI.md dosyasını oku. Bir araba için landing page sayfası oluştur. Görseller image klasörü içerisinde, 001 ile başlayıp 289'a kadar gidiyor. Her bir görsel bir frame; bu görsellerin birleşiminden oluşan bir scroll animasyonunu en başa, hero bölümüne ekle.`
- GEMINI.md'nin içeriği videoda gösterilmiyor (yalnızca Drive linki).

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- Uzun hazır prompt dosyası (GEMINI.md) her projede bağlam olarak veriliyor; modelin **önce soru sorması** (marka, estetik yön/tema seçenekleri, değer önerisi, CTA).
- Gerçek görseller (villa fotoğrafları kullanıcı tarafından verildi) ve tam ekran görsel üstü tipografi.
- "Klasik AI sitesi gibi değil" font seçimi, estetik bar/nav, kart içi dönen animasyonlar, çok yumuşak blur geçiş efekti (Gemini 3.1 Pro'nun animasyonda başarılı olduğu vurgulanıyor).
- Kare kare (image sequence) scroll animasyonu: AI görsel (ilk/son kare) → Kling start/end frame videosu → JPG kareler → scroll'a bağlı oynatma. "Parçalanma şart değil; farklı kreatifler de yapılabilir."

## Hatalar ve çözümleri
- Lovable/Bolt/Emergent tarzı araçlar "hep mavi/mor, klasik kartlar, AI olduğu belli" → çözüm: Antigravity + Gemini 3.1 Pro + GEMINI.md.
- Kare sırası önemli (1, 2, 3, 4…) — dosya adlandırması 001–289 şeklinde verilip prompt'ta açıkça belirtiliyor.
- Başka hata videoda yok.

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
Satış süreci yok. Açılışta "basit bir web sitesi için binlerce dolar verip haftalarca beklediğimiz devir kapandı" diyor; çıktıyı Framer'daki **~60 $** ücretli şablonlarla aynı seviyede buluyor. Antigravity, GitHub, Vercel (Hobby) ücretsiz.

## Bizim fabrikaya alınacaklar (somut)
1. **GEMINI.md Drive dosyasını** (https://drive.google.com/file/d/11A_aMoq_3ZbNFQ90J_OpYqJUBViKYxXI/view) bir sonraki erişimde indirip `ajans/arastirma/youtube/kaynaklar/` altına kaydet; içindeki soru akışını (marka adı → estetik yön seçenekleri → 3 değer önerisi → ziyaretçi aksiyonu) bizim brief/keşif prompt'umuza ekle.
2. **Brief soruları kalıbı**: her sitede model önce 4 soru sorsun: marka adı, estetik yön (3 seçenek, müşteri seçer), 3 temel değer önerisi, ziyaretçinin yapacağı tek aksiyon (rezervasyon/arama/WhatsApp).
3. **Kare kare scroll hero reçetesi** (restoran: tabağın "patlatılmış" malzemelere ayrılması; klinik: ürün/cihaz; dükkan: ürün açılımı): Nano Banana Pro ilk kare → aynı görselden "parçalanmış" son kare → Kling 3.0 start+end frame, 12 sn, sessiz, slow-motion → MP4→JPG kareler (`001.jpg…`) → `image/` klasörü → "her görsel bir frame, hero'ya scroll animasyonu" prompt'u. Görsel ve video prompt'larını da LLM'e yazdırma.
4. Müşteri bilgisi tek dosya (TXT/DOCX "kurumsal tanıtım belgesi") olarak `@` ile verilsin → içerik doğru gelir.
5. Kalite ölçütü olarak "Framer'daki ücretli şablon seviyesi" karşılaştırması: teslim öncesi aynı sektörde 1-2 Framer şablonuyla yan yana bakış.
6. Antigravity + Gemini 3.1 Pro'yu Claude Code'a ikinci üretim motoru / A-B alternatifi olarak dene (aynı brief'le iki çıktı, müşteriye iki yön).
