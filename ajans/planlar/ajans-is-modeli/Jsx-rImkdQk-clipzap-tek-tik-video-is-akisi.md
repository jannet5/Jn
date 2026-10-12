# How This AI Automation Creates 100 Viral TikTok Videos Daily! — AI Venture — 8 dk — https://www.youtube.com/watch?v=Jsx-rImkdQk

## Tek paragraf: ne anlatıyor
Ronaldo, Messi, Trump gibi ünlülerin AI ile üretilmiş "eğlenceli sahne" videolarını TikTok'ta paylaşarak 6 ayda iki hesabı 100.000+ takipçiye çıkardığını iddia eden anlatıcı, normalde çok araçla (Ideogram'da görsel → image-to-video aracı → kurgu → ses efekti) saatler süren üretimi ClipZap ("Clips app") adlı hepsi-bir-arada AI platformunun "Auto workflow" (node tabanlı iş akışı) özelliğiyle tek tıka indirmeyi gösteriyor. Şablon akış: metin → meta metin (prompt genişletme) → görsel → 5 varyasyon görsel → 5 video → ses efekti → otomatik kurgu. İlk denemede sonuç zayıf çıkıyor; modelleri Nano Banana (görsel) ve Kling 2.1 (video), boyutu 9:16 yapınca daha iyi sonuç alıyor. Strateji: 10 hesap aç, günde 10 video yükle, hangisi viral olursa ona devam et. Video baştan sona ClipZap affiliate tanıtımı.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
Ajans/hizmet satışı yok. Gelir modeli: TikTok hesaplarından para kazanmak ("started to make thousands of dollars every single month" — "thousands of dollars", net rakam vermiyor; para birimi USD) + affiliate (ClipZap linki, "free tokens").
- Üretim maliyeti: eski yöntem "costs money and it takes time"; ClipZap fiyatı rakam vermiyor.
- Süre: eski yöntemle "a video can take over an hour of production" → 10 hesap için "10 hours every single day"; yeni yöntemle video "in a few seconds" / "10 videos in an hour".

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
Videoda yok.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: ChatGPT (prompt yazdırma), ClipZap Auto workflow (içinde Kling, Veo, MiniMax, Nano Banana vb. modeller), TikTok. Eski yöntem için örnek: Ideogram.

Eski (manuel) akış:
1. Ideogram'da görsel ("create an image of Erling Haaland in the gym training his chest on the bench and having big muscles")
2. Her görseli image-to-video aracına koy
3. Kurgu aracında birleştir
4. Ses efekti ekle

ClipZap Auto workflow (şablon):
1. Templates → soldaki şablon → "overview" → Start
2. Text-to-text node: ChatGPT'den alınan prompt'u yapıştır (meta metne çevirir)
3. → Text-to-image (önerilen model: Nano Banana)
4. → Image-to-image (5 varyasyon)
5. → Image-to-video x5 (önerilen: Kling 2.1; MiniMax da seçilebilir)
6. → Video sound effects
7. → Edit video (videoları + sesleri birleştirir)
8. Üstteki "Run" tüm akışı, node'daki "Run" tek node'u çalıştırır; tüm node'larda boyut aynı olmalı (TikTok 9:16).
(Opsiyonel node'lar: video enhance, arka plan silme, müzik, AI avatar, lip sync.)

ChatGPT'ye verilen prompt (transkriptte geçtiği gibi) — beğendiği bir prompt'u örnek verip yeni sahne istiyor:
```
this is a prompt that I like. Can you create a prompt for a video for a Tik Tok with Lionel Messi and Ronaldo eating in a restaurant?
```

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik türü: ünlülerin AI ile üretilmiş kısa sahne videoları (spor salonu, restoranda spagetti yeme, yürüyüş), 9:16.
- Sıklık / hesap stratejisi: "start 10 accounts, upload 10 videos a day and see if one of those accounts goes viral" — 10 hesaptan 2'si viral olmuş.
- Reklam: Videoda yok; bütçe rakam vermiyor.

## Hatalar / uyarılar
- Açıklamadaki tek link ClipZap affiliate linki (https://log.cece.com/t/P3); talimat gereği açılmadı, şablon/prompt kaynağı yok.
- İlk çıktı kalitesi zayıf ("still looking a bit ... not that perfect"); model seçimi ve 9:16 boyut tutarlılığı şart.
- Takipçi/gelir iddiaları doğrulanmamış ve videonun tamamı affiliate tanıtımı.
- Bizim not (videoda yok): gerçek ünlülerin rızasız AI videoları kişilik hakları, marka/telif ve platform politikaları (TikTok AI içerik etiketi, taklit kuralları) açısından ciddi risk; çoklu hesapla toplu yükleme hesap kapatma riski taşır. Müşteri işlerinde kesinlikle kullanılmamalı.

## Bizim ajansa alınacaklar (somut)
1. Ünlü/deepfake içerik modelini ALMAYALIM; sadece teknik kalıbı alalım: "metin → görsel → 5 varyasyon → 5 kısa klip → ses → otomatik kurgu" zincirini müşterinin kendi ürün/mekân fotoğraflarıyla kuralım (ör. restoranın tabak fotoğrafından 5 açıdan kısa hareketli klip + ses efekti → 15 sn Reels).
2. Node tabanlı hepsi-bir-arada AI video aracını (ClipZap benzeri veya n8n + Kling/Veo API) "ürün tanıtım Reels'i" paketinde değerlendirelim; önce 1 müşteride kalite/maliyet testi, sonra fiyat belirleyelim (videoda fiyat yok).
3. "ChatGPT'ye beğendiğin prompt'u örnek ver, yeni sahne yazdır" yöntemini ajans içi prompt kütüphanesine alalım: sektör başına (kafe, kuaför, klinik) 10 onaylı sahne prompt'u, her ay yenisini türetelim.
4. "10 dene, kazananı büyüt" mantığını hesap çoğaltma yerine içerik varyasyonunda uygulayalım: aynı kampanya için 5-10 kısa video varyantı üretip Meta'da düşük bütçeyle test et, kazanan kreatifi ölçekle.
5. Tüm AI videolarda platformun "AI ile oluşturuldu" etiketini açık tutma kuralını teslim kontrol listesine koyalım.
6. Hepsi-bir-arada araç kullanılsa bile her node'da model ve boyut (9:16) seçimini sabitleyen ajans şablonu tutalım; videodaki gibi varsayılan modellerle ilk çıktı zayıf geliyor, bu yüzden müşteri teslimi öncesi "model seç → test → onay" adımı zorunlu olsun.
