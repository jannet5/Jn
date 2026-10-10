# How I Automated Alex Hormozi's Entire Ad Strategy With AI — Ryan Mathews — 11 dk — https://www.youtube.com/watch?v=xJ99dBYhpRI

## Tek paragraf: ne anlatıyor
Ryan Mathews (funnelarchitecture.com) bir müşterisi için 77 reklamı "klasik direct response" yöntemiyle, 8 reklamı ise Alex Hormozi'nin yeni stratejisiyle (organikte en iyi performans gösteren içeriği, CTA eklemeden, sadece bir "banner/overlay" ile reklam olarak çalıştırmak) yayınlıyor. Hormozi yöntemi tek bir reklamda 12.43x ROAS (videoyu hazırlarken 11.4x idi) üretip satış başı maliyeti 2/3 oranında düşürüyor; ama ilk partideki reklamların yarıdan fazlası tutmuyor. Bu yüzden organik veriyi (Instagram/Facebook), Meta Ads metriklerini ve Hyros atıf (attribution) verisini toplayıp Gemini ile videoları transkript + ekran üstü metin olarak işaretleyen, sonra Claude Code'a kazanan/kaybeden kalıplarını öğreten, bir puanlama rubriği ve "skill" oluşturan, sentetik müşteri avatarlarıyla senaryoları test eden ve her partide 10 hook x 3 gövde = 30 varyasyon üreten kendini besleyen bir yapay zekâ ajanı kurduğunu anlatıyor. Teknik detaya girmiyor, sistemin mimarisini gösteriyor.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Kime: Reklam çalıştıran, bol içerik üreten işletmeler (örnekteki müşteri skit, tek kişilik konuşma, ham video ve yüksek prodüksiyon video karışımı üretiyor).
- Ne: "Bu sistemi sizin için kuralım" — funnel + reklam optimizasyon ajanı kurulumu (done-for-you). Video sonunda Calendly / funnelarchitecture.com üzerinden görüşme çağrısı.
- Fiyat: rakam vermiyor.
- Paketleme: rakam/paket detayı vermiyor; "sistemi senin için kuralım, görüşme ayarla" teklifi.
- Açıklamadaki yasal not: "Results shown are individual and not typical..." (sonuçlar tipik değil uyarısı).

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Kanal: YouTube içerik → açıklama ve video sonundaki görüşme linki (Calendly).
- Mesaj (video kapanışı):
  > "So, if you're interested in just seeing if we can do this for you, I'll put my Calendly down below"
  Türkçe: "Bunu senin için yapabilir miyiz görmek istersen Calendly linkim aşağıda."
- Açıklama CTA'sı: "Interested in getting this system built for you? Book a call with me here" — sistemi kurdurmak için görüşme daveti.
- Soğuk mesaj/DM şablonu videoda yok.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Instagram/Facebook API, Meta Ads (API), Gemini API (video transkript + görsel anotasyon), Hyros (atıf/attribution), Claude Code (analiz, skill, rubrik, senaryo yazımı). n8n/Make/Zapier adı geçmiyor; prompt metni paylaşılmıyor.

Akış (sıralı):
1. Trigger: Organik içerik verisini çek → Instagram + Facebook API'den her içerik için saves, views, comments, followers (takip getirme) metrikleri + içeriğin kendisi (video dosyası).
2. En iyi organik içerikleri (özellikle en çok "save" ve en çok takipçi getiren) doğrudan Meta Ads'e reklam olarak koy; CTA ekleme, sadece üstüne "Click to grab..." tipi bir banner/overlay.
3. Gemini API → videoları transkript et + anotasyon: konuşulan metin, ekran üstü yazı, açılıştaki pattern interrupt, ilk saniyelerdeki caption. (Gerekçe: "sometimes what's on the screen as the hook is more important than the words. Sometimes the words don't even start for 5 seconds")
4. Meta Ads verisini çek → CPM, CPC ve granular reklam metrikleri; aynı reklam videolarını da Gemini'den geçir.
5. Hyros'tan gerçek satış/ROAS atıf verisini çek (Meta/Google atıfı güvenilmez; Claude yanlış veriyle öğrenmesin diye).
6. Tümünü Claude Code'a ver → kazananlar / kaybedenler / ortadakiler arasındaki farkları bul ("What's creating these outliers?").
7. Claude bir puanlama rubriği (scoring rubric) oluşturur → gelecekte üretilecek içerikleri geçmiş gerçek veriye göre puanlar.
8. Claude bir "skill" oluşturur; öğrendikleri: dönüştüren hook yazımı, hangi format en yüksek ROAS'ı getiriyor (skit / tek kişi / ham / yüksek prodüksiyon), senaryo gövdesinin yapısı, sosyal kanıtın doğru dozu, dönüşümü öldüren ve kaçınılacak şeyler. Optimize edilen metrik: görüntülenme ya da en ucuz CPC değil, en yüksek getiri.
9. Yeni reklam üretimi, iki mod:
   - "Hey, make me 30 more variations based on our winners" — kazananlardan 30 varyasyon üret.
   - "Hey, we wrote this script, right? We wrote this script here. Can you please revise it?" — elle yazılan senaryoyu skill ile revize ettir.
10. Sentetik kitle testi: ICP'ye ve gerçek iyi müşteri verisine dayanan 10 avatar; her avatar senaryoya "deal breakers, buying triggers, scam detector" ve ~10 soru daha üzerinden geri bildirim veriyor → geri bildirimle senaryo yeniden yazılıyor.
11. Varyasyon çıktısı: önce 9 varyasyon (3 gövde x 3 hook) idi; Claude hook'ların aşırı outlier etkisini fark edince 10 hook x 3 gövde = tek çekim partisinden 30 reklam varyasyonu.
12. Yayın: önce organik içerik olarak paylaş ve/veya Meta Ads'e koy.
13. Geri besleme döngüsü: yeni sonuçlar tekrar Gemini → Hyros → Claude Code; Claude "Bunun X nedeniyle dönüşeceğini düşünmüştüm, gerçekte nasıl oldu?" diye kendini düzeltir.
14. Planlanan ekleme: rakip reklam ve içeriklerini (view + comment verisi; save verisi alınamıyor) Gemini'ye besleyip daha düşük ağırlıkla yeni açı (angle) bulmak; YouTube outlier'ları için de aynısı.

Hormozi alıntısı (stratejinin özü, aynen):
> "Just take the ad or take the content and then just just literally put an overlay. Just put a banner that says like, "Click to to grab a thing like it People get it."
Türkçe: CTA eklemeyin, içeriği alın ve sadece "tıkla al" diyen bir banner koyun.
> "look at the content that you have that gives you the most follows, not the most views. Look at the content that has the most saves. Try running those as your ad."
Türkçe: En çok izlenen değil, en çok takip ve kaydetme getiren içeriği reklam yapın.
> "if you're putting out 20, just put all 20 up."
Türkçe: Haftada 20 içerik üretiyorsan 20'sini de reklama koy.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türleri: skit, tek kişi kameraya konuşma, ham (raw) video, yüksek prodüksiyon video; reels.
- Sıklık: Hormozi haftada ~450 içerik üretiyor; küçük hesaplar için "20 üretiyorsan 20'sini de koy".
- Reklam kurgusu:
  - Organikte kazanan içerik → CTA yok, sadece overlay/banner → Meta Ads.
  - Orta/alt huni (middle/bottom of funnel), çok kaydedilen derin içerik reklamda en iyi çalışan.
  - Test: 77 klasik reklam vs. 8 Hormozi tarzı reklam (sonra ~6 tane daha); Hormozi tarzı ilk partinin yaklaşık yarısı başarısız.
  - Kreatif formülü: 10 hook x 3 gövde = 30 varyasyon / tek çekim partisi (eskiden 3x3 = 9).
- Sonuçlar: en iyi reklam 12.43x ROAS (önce 11.4x), satış başı maliyet 2/3 düştü.
- Bütçe: rakam vermiyor (günlük/aylık harcama yok).
- Ölçüm: Meta atıfına güvenme, Hyros gibi bağımsız atıf aracı kullan.

## Hatalar / uyarılar
- Organikte iyi olan her içerik reklamda çalışmıyor: ilk partinin yarısından fazlası "flop". Filtre/rubrik şart.
- Meta ve Google'ın atıfı "really bad"; yanlış atıfla beslenen AI yanlış ders çıkarır → bağımsız atıf aracı (Hyros) gerekiyor.
- Sadece ses transkripti yeterli değil; ekran üstü hook'u yakalamak için görsel anotasyon yapan model (Gemini) gerekli.
- Rakip verisinde "save" metriği alınamıyor; rakip verisine kendi verinden daha düşük ağırlık ver.
- Açıklamada linklenen tek adres satış/görüşme sayfası (funnelarchitecture.com) — talimat gereği açılmadı; şablon/prompt/workflow linki yok.
- Video teknik kurulum (API çağrıları, prompt'lar) göstermiyor; sadece mimari.
- Sonuçlar "individual and not typical" uyarısıyla veriliyor.

## Bizim ajansa alınacaklar (somut)
1. "İçerikten reklama" paketi: Müşterinin (restoran/klinik) Instagram'ında son 30 günde en çok kaydedilen + takip getiren 3-5 Reels'i seçip CTA'sız, sadece alt banner'lı ("Rezervasyon için tıkla" / "Randevu al") Meta reklamı olarak çalıştır. Sosyal medya paketine "en iyi organik postu reklama dönüştürme" maddesi olarak ekle.
2. Haftalık otomatik rapor akışı (n8n): Instagram Graph API (saves, reach, follows) + Meta Ads Insights (CPM, CPC, sonuç başı maliyet) → Google Sheet → Claude ile "kazanan/kaybeden kalıpları" özeti. Atıf için Hyros yerine yerel işletmede WhatsApp tıklama / telefon araması / rezervasyon formu sayımı kullan.
3. Gemini ile video anotasyonu: Müşterinin Reels'lerini Gemini API'ye verip "ilk 3 saniye ekran yazısı + hook + format" etiketlemesi yap; her müşteri için Claude skill'ine (hook rehberi, kaçınılacaklar listesi) dönüştür. Bu skill ajansın "müşteriye özel AI hafızası" satış argümanı olur.
4. Kreatif formülü standardı: Her çekim gününden 10 hook x 3 gövde = 30 varyasyon çıkar (restoran: 3 gövde = menü/atmosfer/şef; hook'lar fiyat, mahalle, sosyal kanıt vb.). Reklam paketinde "aylık 30 kreatif varyasyon" vaadi.
5. Sentetik avatar kontrolü: Senaryoları yayına almadan önce Claude'da ICP'ye göre 10 avatar (ör. "Kadıköy'de 28 yaş öğrenci", "çocuklu aile") ile "deal breakers / buying triggers / scam detector" sorularıyla test eden bir prompt şablonu yaz.
6. Fiyatlandırma önerisi (bizim, videoda rakam yok): Sosyal medya paketine ek "Reklam optimizasyon döngüsü" modülü olarak sat; ROAS iddiasında bulunmadan "satış başı maliyeti düşürme" hedefiyle 3 aylık deneme.
