# The NEW Way To Sign Web Design Clients With AI [2026] — Abou Toure — 17 dk — https://www.youtube.com/watch?v=-np6T0ljYwo

## Tek paragraf: ne anlatıyor
Abou Toure, yerel hizmet işletmelerine AI ile yapılmış web sitesi satmanın A'dan Z'ye akışını gösteriyor: (1) müşteri bulma = soğuk arama ("sitenizi zaten yaptım, 5 dakikanız varsa göstereyim" açılışı), (2) Google Maps'ten lead bulma ve kötü siteleri eleme, (3) Base44'te işletmenin Google yorumlarını yapıştırarak tek prompt'la 5-10 dakikada site üretme, (4) yayınlama/domain bağlama ve ekran paylaşımıyla müşteriye gösterme, (5) SSS: ödeme alma, kaç arama gerekir, niş seçimi. Ana fikir: tek zor iş müşteri bulmak; üretim AI ile neredeyse bedava, bu yüzden siteyi aramadan ÖNCE yap ve "zaten yaptım" de.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Kime: "high-ticket" yerel hizmet işletmeleri — örnek niş peyzaj (landscaping), tadilat (remodeling), hardscaping. Restoranı bilinçli olarak dışarıda bırakıyor.
- Niş mantığı (aynen rakamlarla): restoran sahibi yemek başına 5 USD kâr ediyorsa 1.000 USD kâr için 200 müşteri lazım; peyzajcı tek bir hardscaping işinden 5.000 USD kâr edebilir → "it's actually much easier to sell a $3,000 website to a landscaping company than it is to sell a $1,000 website to a restaurant."
- Ne: AI (Base44) ile üretilmiş, yüksek dönüşümlü tek sayfa site: hero'da iletişim formu, Google puanı, hizmetler, "neden biz", AI görselli "son işler", Google yorumları, SSS, son CTA. Form gönderimleri e-posta/telefona (Zapier ile) yönlendirilebiliyor.
- Kaça: "you can absolutely sell them for like a thousand, two thousand, three thousand dollars" (1.000–3.000 USD, tek seferlik). Örnek: "cold call today ... get a client booked for tomorrow, and close them for a thousand dollars."
- Aylık bakım/abonelik paketi: rakam vermiyor (videoda yok).
- Maliyet: Base44'te birkaç ücretsiz site hakkı, sonra ücretli plan (fiyat vermiyor); domain ayrı satın alınıyor (fiyat vermiyor).
- Ödeme: önce para al, sonra fatura gönder. Stripe (otomatik fatura), Kanada'da e-transfer, ABD'de Zelle / Cash App ("a bit unprofessional" ama düşük komisyon). Ücretsiz fatura: invoice-generator.com. Fatura önemli çünkü müşteri gider olarak yazabilsin.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
Kanal: soğuk arama (en hızlı kanal). Alternatif olarak Facebook ve Instagram videolarına atıf yapıyor (bu videoda detay yok).

Lead bulma adımları:
1. Google'da "<niş> company in <şehir>" ara (örn. "landscaping company in Toronto").
2. Maps sekmesine geç, listedeki işletmelerin sitelerini aç.
3. 100+ yorumlu işletmeleri arama ("they probably have a receptionist").
4. Sitesi olmayanları ve kötü siteleri işaretle. Kötü site ölçütleri: hero'da iletişim formu yok, hizmet bölgesi yazmıyor, başlık sadece firma adı (ziyaretçiyi harekete geçiren başlık yok), zayıf tasarım. "three out of four random websites that I opened are not good."

Arama scripti (aynen):
- Açılış: `"Hey, is this XYZ landscaping?"` ya da karşı taraf firma adını söylediyse `"Hey, how's it going?"` — firmayı doğrulama/selam.
- Sitesi VARSA:
```
"Hey, I just saw you on Google, clicked on your website, noticed it could use some improvement. So, believe it or not, I actually went ahead and built you a brand new website if you got five minutes to see it."
```
Mevcut siteyi geliştirilebilir bulduğunu ve yenisini zaten yaptığını söyleyip 5 dk ister.
- Sitesi YOKSA:
```
"Hey, I just saw you on Google, noticed you had super good reviews and then I noticed you didn't have a website and honestly I thought it was a shame you had such good work, but you didn't have anywhere to display the quality of your work. So, believe it or not, I actually went ahead and built you a brand new website if you got five minutes to see it."
```
İyi yorumlarını övüp sitesizliğin yazık olduğunu söyler, hazır siteyi göstermek ister.

Gerçek arama örneğinden itiraz karşılama cümleleri (aynen):
- Kimlik: `"Hey, my name's Antonio. I go to Hawaii CU. I'm out here studying design. And I was actually just looking at you guys on Google, noticed you had super good reviews, and clicked on your website, noticed it could use improvement. So, I actually just went ahead and built you guys one if you got like 5 minutes to see it."` — "tasarım öğrencisiyim" çerçevesiyle güven/sempati.
- "Yeni yaptırdım" itirazı: `"Really? That That's crazy cuz I was actually looking at it and I actually noticed a couple things that could use improvement. ... it's actually already built, so like you can see it. Worst-case scenario, you don't like it."` — zaten yapıldığı için risk yok mesajı.
- Net ret: `"Okay, appreciate the honesty. Do you mind if I ask why?"` — reddin sebebini sorar.
- Fiyat sınırı: `"Cuz I don't think you build websites from scratch for less than a thousand bucks, do you?"` — 1.000 USD çıpası.
- Görüşmeyi yazışmaya çevirme: `"I hear you. And And the last person who wants to waste my time is myself. So, how about I we we hop off this call, and we can kind of go back and forth via text. I'll send you what I got. ... I'll even send you a video on like how I could improve your current website and how I could have like just make your online presence better. How about that?"` — SMS + iyileştirme videosu (Loom tarzı) ile kapıyı açık tutar.
- Taahhüt: `"Would you at least watch the video, or no? Be honest with me."` — küçük bir "evet" alır.

Not: Tam script ve itiraz dokümanı Instagram DM ile ("cold calling script") veriliyor; videoda paylaşılmıyor.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Google Maps → Base44 → Base44 domain satın alma/bağlama → Zapier (form bildirimi) → Stripe/invoice-generator.com.

Akış:
1. Trigger: Maps'te sitesiz/kötü siteli işletme bulundu.
2. İşletmenin Google yorumlarından bir kısmını kopyala ("You don't need to make it fancy").
3. Base44'e "temel açıklama" prompt'u + yapıştırılan yorumlar → gönder, hiçbir şeye dokunma. Süre: "definitely under 10 minutes".
4. Önizleme kontrolü: hero (4.5 yıldız Google), faydalar, iletişim formu, hizmetler, neden biz, AI görselli son işler (animasyonlu), Google yorumları, SSS, son CTA.
5. "Publish app" → canlı URL; toplantıda gizli sekmede açıp URL çubuğunu silerek ekran paylaşımıyla göster (müşteri AI ile yapıldığını bilmesin: "you don't want them to know that it's made fully with AI").
6. Satış sonrası: "Connect a custom domain" → Base44 içinden domain ara/al → yayınla ("No client is going to want to have like a .base44.com website ever").
7. İletişim formu → üçüncü parti entegrasyonla müşterinin e-postasına; Zapier ile telefona bildirim (videoda kurulmuyor).

Prompt: Videoda ekranda yazılıyor ama seslendirilmiyor; tam metin transkriptte yok. İstenen içerik bölümleri (transkriptten çıkarılan): hero'da iletişim formu, faydalar, hizmetler, neden biz, son işler, yorumlar, SSS, son CTA, "high converting". Tam prompt Instagram DM ("prompts") ile veriliyor.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
Videoda yok. (Yalnızca kendi Instagram'ını DM ile lead mıknatısı olarak kullanıyor: "cold calling script" / "prompts" yazana doküman gönderiyor.)

## Hatalar / uyarılar
- Kurulum işleriyle (logo, şirket tescili) oyalanma; para kazandıran tek iş müşteri bulmak.
- Restoran gibi düşük kârlı nişler 1.000 USD'yi bile zor öder; high-ticket niş seç.
- 100+ yorumlu işletmelerde resepsiyonist engeline takılırsın.
- Sitenin "zaten yapılmış" olduğunu söylemek kritik — müşteriye görüşmeye girmek için sebep verir.
- AI ile yapıldığını müşteriye söyleme (yazarın tavsiyesi; etik/şeffaflık açısından bizim için tartışmalı).
- Kaç aramada müşteri gelir: rakam vermiyor; bölgeye, satış becerisine, özgüvene bağlı diyor.
- Zelle/Cash App profesyonel değil; mutlaka fatura kes.
- Açıklamadaki linkler: ücretsiz kurs (go.aboutoure.com, kurs satış — atlandı), Instagram (atlandı), Base44 affiliate (atlandı). Açılacak şablon/prompt/doküman linki yok; script ve prompt dokümanı yalnızca Instagram DM ile veriliyor → erişilemedi.

## Bizim ajansa alınacaklar (somut)
1. "Siteniz hazır" soğuk arama scriptini Türkçeleştir: "Merhaba, Google'da işletmenizi gördüm, yorumlarınız çok iyi ama siteniz yok/eski. Açıkçası size yeni bir site hazırladım, 5 dakikanız varsa göstereyim." + itiraz kartları ("yeni yaptırdım" → "zaten hazır, beğenmezseniz hiçbir yükümlülük yok"; "ilgilenmiyorum" → "nedenini sorabilir miyim?"; kapanış → "WhatsApp'tan link + 2 dk'lık iyileştirme videosu atayım, izler misiniz?").
2. Hazır site üretim hattı: Maps'ten aday listesi (100'den az yorum, sitesiz/kötü siteli) → Google yorumlarını kopyala → Base44/Lovable'a standart "yerel işletme" prompt'u (hero'da form + WhatsApp butonu, hizmetler, neden biz, yorumlar, SSS, CTA) → 10 dk'da önizleme linki. Günlük hedef: aramadan önce 5 hazır demo.
3. Niş önceliği: restoran yerine birim kârı yüksek işler (diş/estetik klinik, tadilat-mutfak, klima/kombi servisi, avukat, oto servis). Restorana tek seferlik site yerine düşük giriş + aylık sosyal medya paketi sun.
4. Fiyat çıpası: high-ticket nişe tek seferlik site için Türkiye karşılığı bir "kurulum" fiyatı (videodaki 1.000–3.000 USD'nin yerel uyarlaması) + domain/hosting/bakım için aylık ücret; kapanışta "önce ödeme, sonra e-fatura" kuralı.
5. Lead mıknatısı: Instagram'da "SCRIPT" / "PROMPT" yazana DM ile doküman gönderen otomasyon (ManyChat benzeri) — esnaf hedefli içerikte "SITE" yazana ücretsiz demo.
6. Form bildirimi: sitelerdeki teklif formunu WhatsApp/e-posta bildirimine bağlayan Zapier/n8n akışını standart teslimata koy (müşteriye "talepler doğrudan telefonunuza düşer" vaadi).
