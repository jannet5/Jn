# I Built An AI Influencer Automation in N8n that Automatically Posts Videos — Dan Kieft — 12 dk — https://www.youtube.com/watch?v=R9c_JQrEtu8

## Tek paragraf: ne anlatıyor
Dan Kieft, gerçek insan gibi görünen ve konuşan "AI influencer" videolarını üretip Instagram'a kendiliğinden, günde bir tane planlanacak şekilde yükleyen üç araçlı bir sistem gösteriyor: (1) Arcads'te bir AI aktör oluşturuluyor (prompt'la görsel → konuşan aktör → farklı mekânlarda "versiyonlar"), ChatGPT'ye yazdırılan senaryolar toplu hâlde videoya çevriliyor; (2) n8n workflow'u Arcads klasöründeki tamamlanmış videoları API ile çekiyor, Cloudinary ile Instagram formatına dönüştürüyor, konuşma metninden GPT-4o-mini ile Reel açıklaması yazıyor; (3) Blotato API'si videoları her biri bir sonraki güne düşecek şekilde Instagram'a planlıyor. Örnekler: kendi "fitness influencer" erkek karakteri ve "travel influencer" kadın karakteri (Roma, Pisa, Dubai Mall versiyonları). Workflow ve adım listesi açıklamadaki Google Doc'ta ücretsiz.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- Videoda ajans/müşteri satışı yok; kendi AI influencer sayfalarını büyütmek için anlatılıyor. Sonda: "in the next video … I will explain to you how you can start making money with your AI influencer" (bu videoda para kazanma yöntemi anlatılmıyor).
- Yaratıcının geliri: Arcads, Blotato, n8n affiliate linkleri; "AI Toolkit" + newsletter lead magnet'i; iş birliği e-postası.
- Maliyet rakamları: Arcads'te konuşan aktör oluşturmak "this will cost you two credits" (kredi fiyatı vermiyor). "you need to have the premium account" — üç aracın da ücretli planı şart (fiyat vermiyor).
- Örnek: Alman turizm kurumu (German National Tourism Board) bir AI influencer yönetiyor — "run by the German government of tourism".

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
Videoda yok.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: Arcads (AI aktör + konuşan video), ChatGPT (senaryo yazımı, < 5 dk'da toplu senaryo), n8n, OpenAI API (gpt-4o-mini, Responses API), Cloudinary (video dönüştürme), Blotato (Instagram planlama API'si). Kaynak: `ajans/arastirma/youtube/kaynaklar/R9c_JQrEtu8-dan-kieft-ai-influencer-n8n.md`.

Adım 1 — Arcads'te influencer:
1. Klasör aç (ör. "travel influencer 2") → yeni proje.
2. Create actor → görsel yükle ya da prompt'la üret. Videodaki prompt: `26 years old female that is sitting in a cafe wearing a green shirt` (basit prompt yetiyor; 3 varyasyon veriyor).
3. Aktöre ne yapacağını söyle ("talk with excitement") → "turn into a talking actor" → Confirm (2 kredi).
4. Ses ayarı: ses seçimi, speed, stability, similarity, style exaggeration.
5. Senaryoyu gir → video. Örnek çıktı: "As much as I love New York, there are definitely a few things I don't love about it. The first is the noise."
6. Aynı kişinin farklı mekân versiyonları: Add actor → My actors → "new version" → yeni prompt (`she is standing in the park`). Videoda 5 versiyon: plaj, Dubai Mall, Roma (Trevi), Pisa.
7. "Batch make" — ChatGPT'ye birkaç senaryo yazdır, hepsini Arcads'e koy (fitness örneği: "If you're serious about fitness, sleep is just as important as training. Aim for 7 to 9 hours a night…").

Adım 2 — n8n workflow ("Travel Influencer", JSON'dan sıralı):
1. Trigger: Set workflow parameters (`Arcads Folder ID` = Arcads klasör URL'sindeki ID, `Blotato Instagram Account ID`).
2. HTTP GET Arcads `/v1/folders/{id}/scripts` (Basic Auth: Arcads Settings → Members → Public API key → client ID = kullanıcı, client secret = şifre).
3. Code: `videoStatus === "completed"` olan her videoya bugünden itibaren +1 gün artan tarih ata.
4. Loop Over Videos → HTTP GET Arcads `/v1/videos/{id}`.
5. HTTP POST Cloudinary upload (`upload_preset=ig_mp4`) — Instagram uyumlu mp4.
6. HTTP POST OpenAI Responses (gpt-4o-mini) → Reel açıklaması.
7. HTTP POST Blotato `/v2/media` → medya URL'si.
8. HTTP POST Blotato `/v2/posts` (`scheduledTime` = atanan tarih, platform instagram) → Loop'a dön; bitince "All videos are posted!".

Açıklama prompt'u (workflow JSON'dan AYNEN):
```
Write the Instagram description for a Reel. Respond only with the plain description, no titles, no markdown—just the description. Here's the spoken text in the reel : {{ $('Loop Over Videos').item.json.text }}
```
Türkçe: Videodaki konuşma metninden düz, markdown'sız bir Reel açıklaması (hashtag'li) yazdırıyor.

Adım 3 — Blotato: Settings → Instagram'a "login with Instagram" → "copy account ID" → n8n parametresine; Settings → API → API key → n8n "Blotato API key" credential'ı (Header Auth). Doc notu: "make sure your IG account is an creator account so you can use it in blotato."

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
- İçerik: AI avatar konuşan Reel (talking head), niş: seyahat ve fitness ipuçları.
- Sıklık: günde 1 Reel ("scheduled like once a day"); 9 video → "scheduled for the next 9 days". Öneri: "If you batch this out for at least a month … maybe once a month, you just have to create this type of content."
- Döngü: workflow her çalıştığında klasördeki yeni videoları alıp aynı işlemi tekrar ediyor.
- Reklam / bütçe: Videoda yok.

## Hatalar / uyarılar
- Ücretli planlar şart: "without one, you cannot get the API keys and you cannot get this automation to work."
- Yaratıcı da "It's not perfect yet, but we're close to it" diyor.
- Code düğümü klasördeki TÜM tamamlanmış videoları her çalıştırmada yeniden listeliyor; daha önce yayınlananları işaretleyen bir kontrol yok → tekrar çalıştırınca aynı videolar yeniden planlanabilir (JSON'dan bizim çıkarımımız).
- Cloudinary düğümü yaratıcının hesabına (`dazxntn9b`) bağlı; videoda "you can ignore transform video" deniyor ama kendi hesabınız olmadan çalışmayabilir.
- Instagram hesabı creator/business olmalı.
- AI influencer'ın gerçek insan gibi sunulması (Türkiye'de reklam/etiket mevzuatı, Meta'nın "AI info" etiketi) açısından şeffaflık riski — videoda değinilmiyor.

## Bizim ajansa alınacaklar (somut)
1. **"Dijital yüz" paketi yerel işletmeye:** Klinik, emlak, kuaför gibi işletmelerde sahibi kameraya çıkmak istemiyorsa Arcads (ya da benzeri) ile marka yüzü avatarı + haftalık 3-7 konuşan Reel. Senaryolar ChatGPT ile toplu; "AI ile oluşturulmuştur" etiketi zorunlu kuralımız olsun.
2. **Batch + planlama mantığını kopyalayalım:** Ayda bir "çekim günü" yerine ayda bir "senaryo günü": 30 senaryo → 30 video → n8n Code düğümü gibi +1 gün tarih atayan zamanlayıcı → Blotato (veya Meta Graph API) ile otomatik yayın. Müşteriye "bir ay boyunca her gün 1 Reel, siz hiçbir şey yapmadan" diye satılır.
3. **Workflow'a "yayınlandı" kaydı ekleyelim:** Google Sheet/Airtable'da video ID + durum tutalım; aynı video iki kez gitmesin. Ayrıca müşteri onayı için "onaylandı" sütunu.
4. **Açıklama prompt'unu Türkçe yerel sürüme çevirelim:** "Aşağıdaki Reel konuşma metninden Instagram açıklaması yaz; ilk satır merak uyandıran kanca, işletmenin semtini ve randevu/WhatsApp çağrısını ekle, 5 yerel hashtag, markdown yok."
5. **Arcads "versiyon" fikri = mekân çeşitliliği:** Restoran için aynı avatarı mutfakta, salonda, terasta göstermek; içerik monotonluğunu kırar. Fiyatlama: Arcads kredi maliyeti videoda verilmiyor — teklif öncesi kendi test hesabımızla video başı maliyeti ölçelim.
