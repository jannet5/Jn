# 20 Dakikada Profesyonel Web Sitesi: Claude Code, Fal.ai ve Kling Entegrasyonu (Professional Website in 20 Minutes) — burhan kocabıyık — 18 dk — https://www.youtube.com/watch?v=tDHWgGpZE2M

> Kaynak: transkript (diğer oturum tarafından çekildi) + açıklama. Bölüm: "yapay zeka ile web sitesi yapma ajans" (Türkçe web grubu).
## Tek paragraf: ne yapıyor, sonuç ne
Antigravity içindeki Claude Code'a bir GitHub reposunu (tasarım "skill seti"/başlangıç şablonu) yükletip "bu repoyu kullanarak profesyonel kaliteli web sitesi oluştur" diyor; ilk sürüm konu bile söylenmeden düzgün çıkıyor. Ardından ChatGPT'den bir otomasyon ajansı için açılış animasyonu fikri + Nano Banana prompt'u + Kling prompt'u alıp, Fal.ai API anahtarıyla Claude'a görseli Nano Banana 2, videoyu Kling ile ürettiriyor; videoyu hero arka planı yapıp maskeleme ve **scroll'a bağlı (Apple tarzı) video ilerlemesi** ekletiyor. Otomasyon ajansı denemesini beğenmeyince (video akıcı değil, yazılar okunmuyor) aynı akışla bir **iç tasarım şirketi** sitesi yaptırıyor; Nano Banana görselini Kling'e "kamera çok yavaş sağdan sola" ile videoya çevirtip scroll'da açılan hero yapıyor. ~15 dk'da "birinin 1.000 $ verebileceği" bir site; GitHub'a push → Vercel'de proje → custom domain.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- **Claude Code** (Antigravity içinde Extensions'tan; "agent" paneli kapatılıyor), **bypass modu**.
- **GitHub reposu (tasarım skill seti / başlangıç şablonu)** — "kod → kopyala" ile repo URL'sini Claude'a veriyor; "açıklamaya koyacağım" diyor ama **açıklamada repo linki yok**. Repo adı transkriptte geçmiyor.
- **Fal.ai** — API key (Fal panel → API Keys → ekle); "Generate → Recent" ekranından üretimleri kontrol.
- **Nano Banana 2** (Fal üzerinden görsel) — Claude ilk seferde **Flux Dev** kullanmış, beğenmeyip Nano Banana dokümantasyon sayfasını verip zorluyor.
- **Kling 3.0** (Fal üzerinden image-to-video) — "en yeni modellerinden" diye açıkça belirtiyor.
- **ChatGPT** — animasyon fikri ve Nano Banana / Kling prompt'larını yazdırmak için (Claude'a da yazdırılabilir diyor).
- **GitHub** (repo + main branch) + **Vercel** (proje import, custom domain, analytics). Alternatif: **Netlify**.
- Açıklamadaki linkler: Instantly https://refer.instantly.ai/qxs0i1s20keo , n8n https://n8n.partnerlinks.io/a3jiiy602uuf , Gumroad https://benburhan.gumroad.com/l/pjsiw , https://benburhan.gumroad.com/l/bsrxgi , DOA skool.com/doa/about.

## Adım adım nasıl yaptı (komut/prompt/dosya ile)
1. Antigravity → Open Folder → yeni klasör ("website profesyonel") → Extensions → Claude Code aç.
2. Prompt: GitHub reposunu kullanarak profesyonel site oluştur, önce repoyu yükle + repo URL'si → **bypass modu**.
3. Paralelde ChatGPT'ye: otomasyon ajansı sitesi için açılış animasyonu nasıl olur, Nano Banana prompt'u ve Kling prompt'u ver.
4. Claude'a: "Fal API'sini kullan" + "Nano Banana prompt: …" + "Kling prompt: …" + "web sitesi bir otomasyon ajansı sitesi olmalı (business ile alakalı)".
5. İlk sürüm (repo sayesinde) konu verilmeden de iyi; Fal'dan API key alıp yapıştırıyor.
6. Hero prompt'u (aşağıda): videoyu Hero arka planı yap, Hero'yu ortala, arka planla çakışmasın diye maskeleme.
7. İşleri kuyruğa koyuyor (önce görseller, sonra site düzeltme, sonra video ekleme, en son Vercel).
8. Claude 3 görsel üretiyor, Kling'le video yapıyor; Fal "Recent"te bakınca Flux Dev kullandığını görüyor → Nano Banana dokümantasyon linkini verip "bunu kullanarak görselleri oluşturman lazım".
9. Prompt'ları Claude'a daha iyi yazdırıyor (otomasyon için opsiyonları kendin çıkar; belki uzay gemisi konsepti).
10. Scroll-video prompt'u: sayfa aşağı kaydıkça video kare kare ilerlesin.
11. Kling modelini Fal Explore'dan bakıp **Kling 3.0** diye sabitliyor ("söylemezseniz en kolayını alır").
12. ".env dosyasına Fal API key'i koyması lazım, şu an env dosyası yok; bunu her zaman söylemek lazım."
13. "Video için önce Nano Banana ile görsel oluşturmak zorundasın" (image-to-video zinciri). Fal panelinde görünmeyince "görmüyorum" diyor; panel gecikmeli geliyormuş.
14. Sonuç beğenilmiyor: video akıcı değil, video renginden yazılar okunmuyor → "videoyu biraz arkaya koy, daha güzel bir sayfa şeklinde açılsın" + konsept değişikliği: iç tasarım şirketi sitesi. (~5 dk sürüyor.)
15. İç tasarım sitesi iyi; Nano Banana görselini Kling'le yavaş pan videosuna çevirip scroll'da açılan hero yaptırıyor → "tam istediğim".
16. Deploy: GitHub'da yeni repo → Claude'a "buraya yükle projeyi, sonra Vercel'e pushla" → main branch'e kod → Vercel → Add Project → GitHub repo (HTTPS URL) → proje; sonraki her "tekrar deploy et" otomatik son sürüm. Custom domain: Vercel proje → Domains.

## Kullandığı prompt'lar / skill metinleri (varsa aynen)
(Sözlü aktarım, altyazı hataları düzeltildi.)
- Başlangıç: "Bu GitHub reposunu kullanarak profesyonel, kaliteli bir web sitesi oluştur. Önce repoyu yükle." + repo URL
- ChatGPT'ye: "Bir otomasyon ajansı için web sitesi yapmak istiyorum. Web sitesine ilk açıldığında çok güzel bir animasyon eklemek istiyorum. Nasıl bir animasyon yapabilirim? Bununla alakalı bana prompt ver. Bir Nano Banana prompt'u ver, bir de Kling prompt'u ver."
- Claude'a: "Fal API'sini kullan. Nano Banana prompt: […] Kling prompt: […] Web sitesi bir otomasyon ajansı web sitesi olmalı."
- Hero: "Oluşturacağın video dosyasını alıp Hero Header'ın arka plan görseli yapmanı istiyorum. Ardından Hero Header'ı ortalayarak daha temiz görünmesini sağlayacaksın. Son olarak animasyon arka planının sitenin genel arka planı ile çakışmaması için doğru bir maskeleme uygulamanı istiyorum."
- Model düzeltme: (Nano Banana dokümantasyon linki) "Bunu kullanarak görselleri oluşturman lazım."
- Prompt iyileştirme: "Bunu benim için daha güzel yaz ve web sitesi çok daha kaliteli olsun. Web sitesinin içerisinde kullanacağın prompt da oldukça güzel olsun. Görsel ve video bayağı kaliteli olsun ve otomasyon içerisinde neler yapılabileceğini düşün. Otomasyon web sitesi için kendin opsiyonları çıkart." (+ "belki uzay gemisi olabilir")
- Scroll video: "Web sitesi aşağı doğru gittikçe video da birlikte gitsin. Yani aşağı doğru ben kaydırdıkça video aşama aşama ilerlesin. Videoyu farklı frame'ler gibi düşün. Sürekli video devam etmiyor; ben sayfayı aşağı indirirsem video devam ediyor. Bu şekilde yap."
- "Yeniden görsel ve video oluştur. Kling 3.0 kullan."
- "Videoyu oluşturman için önce Nano Banana ile görsel oluşturmak zorundasın."
- Düzeltme: "Video tam olarak smooth olmamış. Yazılar görünmüyor, videonun renginden dolayı yazıları göremiyorum. Videoyu biraz daha arka tarafa doğru koy ve biraz daha güzel bir sayfa şeklinde açılsın." + "Bana bir iç tasarım şirketinin web sitesini yap."
- Final hero: "Nano Banana'da oluşturduğun görsel çok güzel. O görseli Kling'e ver ve kameranın çok yavaş bir şekilde sağdan sola hareket etmesini iste. Video bittikten sonra bu videoyu web sitesine koy; video aşağı doğru gittikçe daha da açılacak şekilde olsun ve yazıyla çakışmasın. Bunu kendin ayarla."
- Deploy: "Buraya yükle projeyi." (GitHub repo) → "Sonrasında Vercel'e pushla."
- Repo içeriği/skill metni: Videoda yok.

## "Çirkin değil, fıstık gibi" olmasını sağlayan şeyler (tasarım kuralları, animasyon, font, görsel üretimi)
- **Hazır tasarım reposu/skill seti** baştan verilince konu söylenmeden bile "mükemmel" ilk sürüm.
- **Apple tarzı scroll-scrub hero video**: video otomatik oynamıyor, scroll pozisyonuna göre kare kare ilerliyor.
- Görsel → video zinciri: Nano Banana 2 görsel → Kling 3.0 image-to-video, **çok yavaş kamera pan'i (sağdan sola)**.
- Hero: video arka plan + ortalanmış başlık + arka planla çakışmayan **maskeleme**; yazı videonun önünde okunur olmalı (video "arkaya").
- **Model adını açıkça yaz** (Flux Dev gibi zayıf modele düşmesin).
- Konsept seçimi önemli: aynı akış otomasyon ajansında sönük, iç tasarımda (görsel ağırlıklı sektör) çok iyi.
- Font/renk kuralı: Videoda yok.

## Hatalar ve çözümleri
- Claude görsel için **Flux Dev** seçti → Nano Banana dokümantasyon linki verip "bunu kullan".
- Kling modeli belirsiz → Fal Explore'dan bakıp "Kling 3.0".
- **.env yok**, API key açıkta → "Fal API key'i her zaman environment dosyasına koy" demek gerekiyor.
- Fal "Recent" panelinde üretim görünmüyor → panel gecikmeli; beklemek gerekti.
- Scroll videosu akıcı değil + video rengi yazıyı okutmuyor → videoyu arka plana it, maskele, konsepti değiştir.
- Vercel trafiği artınca pahalılaşıyor (uyarı).

## Fiyatlandırma / müşteriye satış anlatıyorsa: nasıl satıyor, kaça
- Satış süreci anlatılmıyor; sadece değer/maliyet iddiaları:
  - Eskiden **5.000–10.000 $** masraflı, haftalar süren işler → "belki **2 $**'a, 10–15 dakikada", kod bilmeden.
  - Final iç tasarım sitesi için: "Bu web sitesine birisi rahat **1.000 $** veriyordu."
  - "Bunu müşterilere satmak için de kullanabilirsiniz" (klasör adı örneğinde).
- Vercel: trafik çok artarsa pahalı olur.

## Bizim fabrikaya alınacaklar (somut)
1. **Kendi "başlangıç reposu"muz**: tasarım skill'leri + bileşen şablonu + CLAUDE.md'li bir GitHub template repo; her proje "bu repoyu kullanarak … sitesi oluştur, önce repoyu yükle" ile başlar.
2. **Scroll-scrub hero modülü**: Nano Banana 2 görsel → Kling 3.0 image-to-video (yavaş pan, 5–10 sn) → scroll'a bağlı kare ilerletme + maskeleme + okunur başlık. Restoran, iç mimar, otel, güzellik salonu gibi görsel sektörlerde standart "wow" paketi.
3. **Fal kuralları dosyası**: kullanılacak model adları sabit (görsel: Nano Banana 2; video: Kling 3.0), API key yalnızca `.env`'de, önce görsel sonra video, üretim sayısı limiti.
4. **Prompt üretim adımı**: hero konsepti + Nano Banana + Kling prompt'larını LLM'e sektör bazlı yazdır ("opsiyonları kendin çıkar").
5. **QA kontrolü**: hero'da yazı/video kontrastı, scroll akıcılığı — başarısızsa video arka plana + overlay.
6. **Sektör seçimi**: görsel ağırlıklı sektörlerde bu akış çok daha etkili; ilk satış hedeflerini buna göre seç.
7. **Deploy**: GitHub repo → Vercel import (bir kez) → sonrası "tekrar deploy et" ile otomatik; müşteri domain'i Vercel Domains'ten; yüksek trafikli müşteride Vercel maliyetine dikkat (Netlify/Hostinger alternatifi).
8. **Fiyat çapası**: "Bu sitenin benzerine 1.000 $ verilir" — bizim maliyetimiz Fal başına birkaç dolar.
