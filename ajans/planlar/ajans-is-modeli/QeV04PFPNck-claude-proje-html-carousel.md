# How to Create UNLIMITED Viral AI Carousels in Claude — digitalSamaritan (Krish) — 5 dk — https://www.youtube.com/watch?v=QeV04PFPNck

## Tek paragraf: ne anlatıyor
Claude'un görsel modeli olmadığı hâlde Instagram carousel üretmenin yolu: görsel üretmek yerine Claude'a her slaydı ayrı görsel olarak dışa aktarılabilen **HTML kodu** yazdırmak. Önce iyi performans gösteren referans carousel görselleri Claude'a yüklenip ortak tasarım dili JSON olarak çıkarılıyor; bu JSON bir Claude Project'in talimatlarına yapıştırılıyor. Sonra herhangi bir içerik (örnekte bülten yazısı) "bunu carousel'e çevir" denerek 7 slaytlık, ilerleme çubuklu, doğru fontlu carousel'e dönüşüyor; slaytlar PNG indiriliyor. Kapanış uyarısı: elinde Canva şablonu varsa Canva connector daha iyi; "AI ile yapabiliyor olman, yapman gerektiği anlamına gelmez" — aşırı mühendislik yapma.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Satış yok; yaratıcı kendi hesapları (practicaly.ai Instagram/TikTok) için kullanıyor, ücretsiz bülten → kitle.
- Çoklu hesap: proje talimatına "hangi hesap için?" sorusu eklenmiş → tek proje ile birden fazla marka. Bizim için doğrudan "müşteri başına marka seçimi" kalıbı.
- Fiyat bilgisi yok.

## Müşteri bulma: hangi kanal, hangi mesaj
- Yok (inbound içerik). Kullanılabilir argüman: "Görsel AI'ın bozuk yazı sorunu yok — gerçek fontlarla, koddan üretilen slayt."

## Üretim süreci: hangi araçlar, hangi otomasyonlar
Araçlar: Claude (chat + Projects), isteğe bağlı Canva connector. Görsel modeli yok.
1. **Stil çıkarma** — Başarılı referans görselleri yükle, prompt (aynen): "Extract the style, design, color, fonts, etc. from all these images. Ignore the written content. Send me the description in a JSON format."
2. **Proje kurulumu** — Claude → Projects → yeni proje; açıklama opsiyonel. Instructions'a JSON yapıştırılır + rol talimatı (aynen): "You're an Instagram carousel design system. When a user asks you to create a carousel, generate a fully self-contained swappable HTML carousel where every slide is designed to be exported as an individual image for Instagram posting. Step one, collect brand details before generating any carousel." + "tasarım sistemini kullan" + "hangi hesap için olduğunu sor" adımları.
3. **Üretim** — İçeriği (bülten yazısı) yapıştır: "transform this article into an Instagram carousel". Claude marka/hesabı ve palet onayını soruyor → slayt dökümü → HTML (7 slayt, toggle, progress bar).
4. **Dışa aktarma** — Slaytları indir; indirme hata verirse "turn all that into a PNG file" iste.
5. **Alternatif** — Hazır Canva şablonu varsa Claude içinde Canva connector kullan, sıfırdan üretme.

## Sosyal medya / reklam
- Format: bilgi/rehber carousel ("guide" infografikleri), ilerleme çubuğu öneriliyor.
- Kaynak içerik: uzun yazı (bülten/blog) → carousel'e dönüştürme (repurpose).
- Reklam yok.

## Hatalar / uyarılar
- Claude artifact'ten doğrudan indirme bazen çalışmıyor → PNG'ye çevirt.
- Prompt'un tamamı gösterilmiyor (sadece ilk cümleler okunuyor); JSON şeması da gösterilmiyor.
- Aşırı mühendislik uyarısı: şablon varsa Canva yeterli.
- Yaratıcı "hiç hata görmüyorum" diyor ama tek örnek; Türkçe karakter/font denetimi bizde ayrıca gerekli.

## Bizim ajansa alınacaklar (somut)
1. Müşteri onboarding'inde "stil çıkarma" adımı: müşterinin (veya beğendiği rakibin) en iyi 6-10 gönderisi → yukarıdaki prompt → `marka-stil.json`. Müşteri klasöründe sabit dosya olur.
2. Carousel'i görsel modelle değil **HTML → PNG** ile üretelim (Claude Code + headless Chrome/Playwright ile 1080x1350 ekran görüntüsü). Türkçe karakterler (ş, ğ, İ) ve gerçek marka fontu garanti; maliyet sıfır.
3. Tek "carousel üretici" talimatı, ilk adım "hangi müşteri?" sorusu → müşteri klasöründen JSON + logo + renkleri çeker (videodaki çoklu hesap kalıbı).
4. Repurpose hattı: müşteri blog yazısı / web sitesi hizmet sayfası → 7 slayt carousel (kapak, 5 değer slaydı, CTA), ilerleme çubuğu standart.
5. Müşteride Canva şablonu varsa onu kullan; satışta "Canva şablonunuzu koruyoruz" seçeneği.
