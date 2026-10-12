# Build AI Websites For Local Businesses In 2026 [Lovable Guide] — Abou Toure — 12 dk — https://www.youtube.com/watch?v=OZ_ZriNypbg

## Tek paragraf: ne anlatıyor
Abou Toure, hizmet işletmesi sahibine (peyzaj, inşaat, tesisatçı, elektrikçi) hitap ederek yüksek dönüşümlü bir siteyi deneyimsiz biri olarak 30 dakikanın altında nasıl kuracağını gösteriyor: ChatGPT'ye "bana soru sor, Lovable için yapılandırılmış prompt hazırla" diyerek işletme bilgisini topluyor (sesli yazmayla ~12 dk), çıkan prompt'u Lovable'a yapıştırıyor, PicsArt'ın ücretsiz AI logo aracıyla logo üretip Lovable'a "logoyu uygun yerlere ekle" diyor, sonra lovable.app alt alan adıyla ücretsiz yayınlayıp ücretli planda Squarespace domain'i bağlıyor. Video aslında hem DIY rehberi hem de kendi ajansı (tourremarketing.com) için lead kapısı.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Kime: hizmet işletmeleri (landscaping, construction, plumber, electrician); kendi ajansı ağırlıklı olarak peyzaj şirketlerine hizmet veriyor.
- Ne: hero + CTA'lar + "request a quote" iletişim formu (talep e-postaya düşer) + animasyonlar + slider + logo + proje görselleri olan "luxurious brand" tarzı site.
- Kaça (ajans fiyatı): rakam vermiyor. Videonun sonunda "daha profesyonel site lazımsa bana ulaşın" diye ajansına yönlendiriyor.
- Araç maliyeti: Lovable ücretsiz başlanabiliyor; domain bağlamak için "you can pay like 20, 30 bucks" (aylık 20–30 USD, ücretli plan). PicsArt logo üretici ücretsiz.
- Konumlandırma: yüksek dönüşümlü site = ileride reklam açacaksan başkasına para ödemene gerek kalmaz ("if you ever want to run ads to the website, you don't need to pay someone else").

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Kanal: YouTube içerik (DIY eğitim) → video sonunda CTA → tourremarketing.com ve sosyal medya. Açıklamada mentorluk başvuru linki ve Instagram.
- Video sonu CTA (aynen): `"if you ever need a professional website that looks better than what we have right here, then just hit me up."` — ücretsiz rehberi izleyen işletme sahibini "daha iyisi için bana gel" ile ajansa çeker.
- Soğuk mesaj/arama şablonu: Videoda yok.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: ChatGPT → Lovable (lovable.dev) → PicsArt Logo Generator → Lovable publish → Squarespace domain.

Akış:
1. ChatGPT'ye bilgi toplama prompt'u (aynen, transkriptte geçtiği gibi):
```
I have a landscaping company. I want you to ask me as many questions as you'd like about it so you can give lovable.dev AI website builder information about my business in a structured way for it to build a high converting website with a brand and image I want. My goal is to gain the trust of the homeowners for them to trust me with a $50,000 hardscaping project.
```
ChatGPT'nin işletme hakkında soru sorup Lovable için yapılandırılmış prompt çıkarmasını sağlar; son cümledeki "hedef" nişe göre değiştirilir (tesisatçı: çok sayıda küçük günlük iş; büyük inşaat: yeni yapı projeleri).
2. Soruları sesli yazmayla cevapla (7 soru ~2 dk; toplam "under 12 minutes"; "take like 15 minutes"). Renk tercihini burada söyle (videoda ChatGPT renk vermedi). "make me like a little luxurious brand" gibi marka tonu ekle.
3. ChatGPT'den final prompt'u iste → tamamını kopyala → Lovable'a yapıştır → bekle.
4. Paralelde logo: "PicsArt logo generation" → firma adı (örn. "Abu Landscaping") → renk (yeşil) → stil "statement" → generate; beğenmezsen "show more results".
5. Logoyu Lovable'a yükle, prompt (aynen): `"add this logo to the website and appropriate places"` — header ve footer'a ekledi. "You can be very brief and vague to lovable."
6. Eksikleri kısa prompt'larla tamamla: telefon numarası ekle, kendi proje fotoğraflarını arka plana koy.
7. Publish → ücretsiz: `<isim>.lovable.app`; ücretli: "Add custom domain" → "Connect domain" → Squarespace ile yetkilendir → doğrulama ("can take up to 72 hours ... it took ... like 2 minutes").
8. Toplam: "Super quick and under 30 minutes."

İyileştirme önerisi: işletme sahibinin yüzünü siteye koymak dönüşümü ve güveni artırır.
Otomasyon (n8n/Make/Zapier): Videoda yok (form talebinin e-postaya düştüğünü söylüyor, kurulum göstermiyor).

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: "işletme sahibi için kendin yap" eğitim videosu + sonunda hizmet CTA'sı; demo siteyi canlı tutup açıklamada linkliyor (kanıt).
- Post sıklığı, reklam kurgusu, bütçe: Videoda yok.

## Hatalar / uyarılar
- Kendi itirafı: "this website ... is not the absolute best, but it is super, super good for you to start" — Lovable çıktısı başlangıç seviyesi; sektöre göre iyileştirme gerekir.
- Ücretsiz planda custom domain bağlanamıyor; ücretli plan gerekli.
- Domain propagasyonu 72 saate kadar sürebilir.
- Rengi baştan ChatGPT'ye söylemezsen prompt'ta renk çıkmayabilir.
- Açıklamadaki linkler yalnızca mentorluk başvurusu ve Instagram (kurs/sosyal → atlandı); açılacak şablon/prompt linki yok.

## Bizim ajansa alınacaklar (somut)
1. İki aşamalı prompt standardı: (a) "Soru sorucu" ChatGPT/Claude prompt'unu Türkçeleştir: "Bir <diş kliniği> işletiyorum. Lovable için yapılandırılmış bilgi çıkarabilmen için bana istediğin kadar soru sor... Hedefim <hastaların bize 30.000 TL'lik implant tedavisi için güvenmesi>." (b) çıktıyı Lovable'a ver. Müşteri ön görüşmesinde bu soruları sesli kayıtla 15 dk'da doldur.
2. Satış görüşmesi içinde canlı demo: müşteriyle 15 dk soru-cevap → Lovable'da 10 dk'da site → aynı toplantıda göster. "30 dakikada siteniz yayında" mesajıyla kapanış.
3. Logo paketi: logosu olmayan esnafa PicsArt/AI logo + site birlikte "kurumsal kimlik başlangıç paketi" olarak sun (ek ücret kalemi).
4. Teslimat kontrol listesine ekle: sahibinin yüz fotoğrafı, telefon/WhatsApp butonu, gerçek iş fotoğrafları, teklif formu → e-posta/WhatsApp bildirimi, custom domain (.com.tr).
5. İçerik pazarlaması: "Kendi sitenizi 30 dakikada kurun" tarzı Türkçe YouTube/Reels eğitimleri + sonunda "daha profesyonelini biz yapalım" CTA'sı; demo siteleri açıklamada canlı tut.
