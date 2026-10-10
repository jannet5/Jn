# Best Local Businesses to Sell AI Websites to in 2026 (Using Claude) — Mikey Website — 30 dk — https://www.youtube.com/watch?v=IIJbt7bxh5w

## Tek paragraf: ne anlatıyor
Mikey, yerel işletmeye "sadece hizmetleri listeleyen güzel bir sayfa" satmanın hata olduğunu, asıl satılabilir ürünün **"iki çatallı sistem" (two-prong)** olduğunu savunuyor: (1) müşteri tarafı web sitesi (randevu / rezervasyon / deneme üyeliği / talep toplar) + (2) aynı projede, formdan gelen kayıtları yöneten **dahili panel** (randevu takip, salon/mutfak panosu, katılım paneli, CRM, atölye iş panosu). Base44 içinde model seçicisinden **Claude** seçilerek, her niş için iki prompt'la (biri site, biri panel) beş niş canlı kuruluyor: diş kliniği, restoran, spor salonu, emlakçı, oto tamirci. Fiyat farkı: sade site ~300 USD, site + dahili sistem ~1.500 USD. Niş seçimi tanıdık çevreye göre yapılmalı; her build portföy / canlı demo olarak kullanılmalı.

## İş modeli: kime satıyor, ne satıyor, kaça, nasıl paketliyor
- **Kime:** Yerel işletmeler — özellikle 5 niş: diş klinikleri, restoranlar, spor salonları (gym), emlak danışmanları, oto tamir atölyeleri. Gerekçe: "many of them either have no proper website or are relying on one that really no longer supports how the business actually runs."
- **Ne:** Ön yüz web sitesi + arka uç dahili araç tek paket. Niş başına eşleşmeler:
  | Niş | Ön yüz (müşteri) | Arka uç (dahili) |
  |---|---|---|
  | Diş kliniği | Hero + randevu butonu, hizmetler, hekim ekibi, iletişim + randevu formu | Sıralanabilir randevu tablosu, durum (pending/confirmed/completed), hasta geçmişi |
  | Restoran | Hero + "reserve a table", kategorili menü (fiyatlı), hikâye, rezervasyon formu | Salon & mutfak panosu (masa durumu empty/reserved/seated/completed), gün zaman çizelgesi |
  | Gym | Hero + free trial, haftalık ders programı (boş kontenjan), 3 eğitmen, 3 fiyat paketi, deneme formu | Kapasite paneli (dolu/neredeyse dolu göstergesi), katılım/no-show, 30 günlük ders performansı |
  | Emlak | Hero + mülk arama çubuğu, 6 ilan grid, kredi (mortgage) hesaplayıcı, danışman profili, alıcı/satıcı formu | CRM: lead tablosu, aşamalar new → contacted → viewing scheduled → offer made → closed, mülk ilgisi görünümü, pipeline özeti |
  | Oto tamir | Hero + "book a service", 6 hizmet + başlangıç fiyatı, 3 yorum, araç bilgili randevu formu, konum/saat | Atölye iş panosu: received / in progress / awaiting parts / completed kartları, usta atama, günlük özet |
- **Kaça (USD):** "a simple website might go for around 300 bucks, but a complete system that includes an appointment dashboard, reservation board, attendance tracker, CRM, or workshop job board, that can be presented as a $1,500 product." Videonun girişinde: "five practical, ready-to-pitch systems worth at least around $1,500 each."
- **Paketleme kuralı (aynen):** "the important part is to sell both sides as one package from the beginning, rather than, say, treating the back end as an optional add-on later." → Panel opsiyonel eklenti değil, baştan paketin parçası.
- Aylık bakım/abonelik rakamı vermiyor.

## Müşteri bulma: hangi kanal, hangi mesaj (varsa mesaj şablonlarını aynen)
- Kanal: **mevcut tanıdıklar / sıcak çevre.** "If you know a dentist, a restaurant owner, gym coach, a real estate agent, or even a mechanic, then start there. Existing connections make the first conversation a lot easier."
- Yöntem: **canlı demo ile göster, anlatma.** "You can show a potential client the public website and then open up the internal dashboard and then explain how the two sides actually work together. And then that gives them something real to react to."
- Hazır DM / e-posta / arama scripti vermiyor — Videoda yok.

## Üretim süreci: hangi araçlar, hangi otomasyonlar (n8n/Make/Zapier akışları, prompt'lar aynen)
Araçlar: **Base44** (no-code AI uygulama oluşturucu) + model seçicide **Claude**. Gerekçe: Claude "tends to make stronger design decisions, follow business-specific instructions more accurately, and keeps the overall layout consistent across multiple sections."

Akış (her niş için aynı):
1. Base44'te yeni proje → sohbet penceresinin üstündeki model seçiciden Claude'u seç, aktif olduğunu doğrula.
2. Prompt 1 → müşteri tarafı site üretilir.
3. Ön yüz formunu test verisiyle doldur.
4. **Aynı projede** Prompt 2 → dahili panel; ön yüz formundan gelen test kayıtları panelde otomatik görünür (site ve panel aynı veritabanını paylaşır).
5. Durum güncellemelerini demo et (pending → confirmed → completed vb.).
6. Projeyi portföy / canlı demo olarak sakla.

n8n/Make/Zapier akışı yok.

### Prompt'lar (transkriptte geçtiği gibi)
Diş kliniği — site (müşteri yüzü):
```
Create a professional website for a dental clinic, include a hero section with a bold headline and a book appointment button along with a services section listing our core dental services with brief descriptions, a team section showing our dentists with their name, their title, and a short bio, and a contact section with our location, opening hours, and an appointment request form.
```
Diş kliniği — dahili panel (randevu tablosu + hasta geçmişi):
```
Create an internal admin dashboard for our dental clinic that shows every single appointment request submitted through our website in a sortable table with patient name, phone number, preferred date, requested service, and status. Let me update each appointment status between pending, confirmed, and completed, and add a patient records view showing the full appointment history per patient.
```
Restoran — site (menü + rezervasyon):
```
Create a website for a restaurant. Include a hero section with the restaurant name, a tagline, and a reserve a table button, a full menu organized by category including starters, mains, desserts, and drinks with item names and prices, an about section telling our story and describing the atmosphere, and a reservation form with fields for name, phone, date, time, and party size.
```
Restoran — salon/mutfak panosu:
```
Create an internal floor and kitchen management view for our restaurant showing all active reservations organized by table, time slot, and party size. Let staff update each table status between empty, reserved, seated, and completed, and add a timeline view showing the full day's reservation schedule.
```
Gym — site (deneme üyeliği odaklı):
```
Create a website for a gym. Include a hero section with a bold motivational headline and a start your free trial button. A class schedule showing the weekly timetable with class name, trainer, time, and available spots. A trainer section with three trainers, each showing their name, specialty, and a short bio. A membership section with three pricing tiers and their features. And a trial sign-up form with name, email, phone, and preferred class.
```
Gym — eğitmen paneli (kapasite, katılım, 30 gün performans):
```
Create an internal trainer dashboard showing every class with its current sign-up count against total capacity, with a visual indicator for classes that are nearly full or fully booked. Add a member attendance view showing who signed up for each class, and whether they attended or were a no-show, and a class performance summary showing average attendance rate and total sign-ups per class over the past 30 days.
```
Oto tamir — site (anlatımdaki kısa hâli; ekranda daha uzun):
```
Create a website for an auto repair shop include a hero section with a bold headline, a testimonial section, a booking form, and a location and opening hours section.
```
- Emlak sitesi, emlak CRM'i ve atölye panosu prompt'ları yalnızca ekranda gösteriliyor, transkriptte tam metin yok. Anlatılan içerik: emlak sitesi = hero + mülk arama çubuğu + "get in touch", 6 öne çıkan ilan (foto yer tutucu, adres, fiyat, oda/banyo, "view property"), danışman profili (bio, yıl, satılan mülk sayısı), mortgage hesaplayıcı (fiyat, peşinat, faiz → aylık taksit), alıcı/satıcı formu. Emlak CRM = lead tablosu (ad, e-posta, telefon, talep türü, tarih), takip aşamaları, mülk ilgisi görünümü, pipeline özeti. Atölye = 4 sütunlu sürükle-bırak iş kartları (received, in progress, awaiting parts, completed), kartta müşteri + araç marka/model/yıl + hizmet + atanan usta, üstte günlük özet.

## Sosyal medya / reklam: içerik türleri, post sıklığı, reklam kurgusu, bütçe
Videoda yok.

## Hatalar / uyarılar
- Videodaki uyarı: hizmetleri listeleyen statik site satmak → "nobody wants to pay any real money for it". Arka ucu sonradan "eklenti" diye satmak da hata.
- Uyarı: Bölümler birbirinden kopuk görünürse site "unfinished" hissi verir; Claude bu yüzden seçiliyor.
- Emlak ve atölye prompt'ları yalnızca ekranda gösteriliyor; transkriptte tam metin yok, yukarıda içerik özetlendi.
- Açıklamadaki linkler (Skool masterclass, "video227" Base44 yönlendirmesi) kurs/affiliate olduğu için açılmadı; şablon / workflow linki yok.
- Video Base44 masterclass'ını (normalde 499 USD, "ücretsiz") tanıtan bir reklam içeriyor; Base44 tavsiyesi tarafsız olmayabilir.
- Transkript otomatik altyazı: "Basecamp 44 / Baseplate 44" = Base44.

## Bizim ajansa alınacaklar (somut)
1. **"Site + Panel" iki katmanlı paket:** Türkiye'deki her niş için ön yüz + dahili panel şablonunu baştan tek paket satalım. Örn. "Klinik Başlangıç" (sadece site) / "Klinik Sistem" (site + randevu paneli + hasta geçmişi). Mikey'nin oranı 300 → 1.500 USD (≈5x); biz de panelli paketi sade sitenin ~4–5 katı fiyatlayalım, paneli asla "sonra eklenir" diye sunmayalım.
2. **5 niş demo kütüphanesi kuralım:** diş kliniği, restoran (QR menü + rezervasyon + salon panosu), spor salonu/pilates (deneme dersi + kapasite), emlak ofisi (ilan + kredi hesaplayıcı + CRM), oto servis (randevu + atölye iş panosu). Yukarıdaki prompt'ları Türkçeleştirip Base44 / Lovable / Claude Code ile birer canlı demo üretelim; satış görüşmesinde önce site, sonra panel gösterilsin.
3. **Türkiye'ye uyarlama:** Formlara WhatsApp butonu, KVKK onayı, TL fiyat alanı ekleyelim. Restoran panosunda "boş / rezerve / oturdu / tamamlandı" masa durumlarını; oto serviste "parça bekleniyor" sütununu birebir koruyalım.
4. **İlk müşteriler sıcak çevreden:** Ekipteki herkes tanıdığı 1 diş hekimi / restoran / spor hocası / emlakçı / tamirci listesini çıkarsın; ilk görüşmede canlı demo linki + "sizin işletmenizin adıyla 24 saatte hazırlarız" teklifi.
5. **Mobil uygulama köprüsü:** Dahili panel zaten bir uygulama; panelli pakette panelin PWA / mobil sürümünü üst paket olarak sunalım (personel telefondan masa / randevu güncellesin).
