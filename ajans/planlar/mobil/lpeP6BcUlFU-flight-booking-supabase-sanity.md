# How to Build Real Mobile Apps with Claude - FULL COURSE — Omatsola Dev — 58 dk — https://www.youtube.com/watch?v=lpeP6BcUlFU

## Tek paragraf: ne yapıyor, sonuç ne
Claude Code + sesle yazma (Whisper Flow) ile sıfırdan "Flightly" adlı uçuş rezervasyon uygulaması (Expo SDK 54 + Expo Router) ve admin paneli kuruyor. Kullanıcı/rezervasyon/işlem/koltuk verisi Supabase'te (Postgres + RLS), içerik (havayolu, rota, uçuş) Sanity Studio'da (GROQ ile sorgulanıyor) duruyor; Sanity hazır CMS olarak "admin dashboard" görevi görüyor. Akış: kayıt/giriş → arama (nereden/nereye/tarih/sınıf/yolcu bottom sheet) → sonuç listesi (Best/Cheapest/Fastest) → detay → yolcu formu → koltuk haritası (sınıfa göre kilitli) → sahte ödeme bottom sheet → Supabase'e booking insert → booking ekranı. Son adımda koltuk kilidi gerçek Supabase sorgusuna bağlanıyor ve RLS yüzünden başka kullanıcının koltuğunun görünmemesi hatası çözülüyor. Stripe yok (mock). Tasarım tarafı derin değil; asıl değeri "önce mimari (DB + CMS), sonra ekran" sırası ve prompt kalıpları.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Expo (React Native), Expo Router, Expo SDK 54 (SDK 55 Expo Go o an yeni çıkmıştı ama 54'te kaldı), Expo Go, iOS Simulator (iPhone 17 Pro Max)
- Supabase — https://supabase.com (Auth, Postgres, Row-Level Security, SQL Editor)
- Sanity.io — https://sanity.io (Studio, şemalar JS, `npm run deploy` ile `*.sanity.studio` hosted studio, GROQ, API token "Editor")
- `@sanity/client` (seed script'te), Node 22 (`nvm install 22` / `nvm use 22`; Sanity Studio Node 20'de çalışmıyor)
- Claude Code (VS Code içinde), Whisper Flow (sesle prompt yazma)
- Açıklamada başka araç/GitHub linki yok; yalnızca iletişim: x.com/omatsoladev

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
**Kurulum**
1. [00:01:23] Mimari notlar: fiyatı 10 dk tutma (ödeme sırasında fiyat değişmesin), koltuk müsaitliği. Stack: Expo + Expo Router, Supabase (auth+DB), Sanity (içerik).
2. [00:02:01] `npx create-expo-app` (isim sorunca `flight`), SDK 54. `npm run start` → `i` ile simülatörde aç. Kural: boş şablonu önce simülatörde çalıştır, sonra paket ekle ("neyin bozduğunu bilmek için").
3. [00:03:47] supabase.com → yeni proje `flight`, güçlü şifre.
4. [00:04:40] sanity.io → yeni proje `flight`, organizasyon `dev`; sitenin verdiği ikinci komutu (mkdir değil) terminalde çalıştır → `studio` klasörü oluşur (Expo projesinin kardeşi). npm seçti.
5. [00:06:16] `cd studio` → `npm run dev` Node 22 hatası → `nvm use 22` → `npm run build` → `npm run dev` → localhost'ta Google ile login, boş Studio.

**Supabase şeması (Claude Code)**
6. [00:09:18] Supabase auth + 4 tablo + RLS prompt'u (aşağıda). Claude bir `.sql` dosyası üretti.
7. [00:10:50] SQL'i Supabase SQL Editor'a yapıştır → hata `relation public.admin_users does not exist` → Claude'a "admin kullanıcı olmasın, sadece normal kullanıcılar" dedi → admin kısmı çıkarıldı → tekrar run → başarılı; Table Editor'da bookings, seat_inventory, transactions, users.

**Sanity şeması ve admin panel**
8. [00:13:21] Claude Code'u `studio` klasöründe açtı (Expo projesindeki ajan `studio`'ya bakabilsin diye; "baştan tek üst klasör açmalıydım").
9. [00:14:47] Sanity şema prompt'u (airline, route, flight). 3 şema dosyası oluştu.
10. [00:17:28] `nvm use 22` → `npm run deploy` → studio hostname sorusu → `flight` (flight.sanity.studio). Airline/Route/Flight belge tipleri; currency USD/GBP/EUR/NGN, seat class economy/business/first, status scheduled/boarding/delayed.
11. [00:20:08] sanity.io → "Complete setup" → Project ID kopyala. Expo'ya bağlama prompt'u (`EXPO_PUBLIC_SANITY_PROJECT_ID`, `EXPO_PUBLIC_SANITY_DATASET=production`).
12. [00:21:32] Supabase → Project Settings → API Keys → "Legacy anon, service_role keys": anon key + service role key + Project URL `.env`'ye.

**Ekranlar**
13. [00:24:25] Tek uzun prompt: auth ekranları + home/booking/profile tabları + "Apple liquid glass bottom tab" + arama alanları + sınıf/yolcu bottom sheet + profil (avatar, ad, logout) + sonuç ekranı (back, ortada rota barı, tarih/yolcu, filter/sort, Best/Cheapest/Fastest sekmeleri).
14. [00:26:52] Supabase → Authentication → Sign In / Providers → e-posta onayını kapat (OTP yok). `user1@mail.com` / `12345678` ile kayıt.
15. [00:29:15] Detay ekranı prompt'u ("Pay now" butonu, sadece ekran).
16. [00:30:38] `studio`'da seed prompt'u → `seedFlights.js` + `flight.csv` şablonu. sanity.io → API → Tokens → "Add API token", ad `seed`, permission **Editor**, no expiration → `.env`'ye → script'i `studio` içinde çalıştır → 5 havayolu, 10 rota, 20+ uçuş.
17. [00:36:35] Arama → GROQ sorgusu prompt'u (butona basınca çalışsın, filtre: origin city, destination city, departure date). Sonuç boş → console.log istedi → GROQ düzeltme prompt'u (route reference).
18. [00:44:40] "Pay now" → "Proceed": yolcu formu (yetişkin/çocuk sayısına göre ad, soyad, e-posta) → koltuk haritası (dolu/boş, sınıfa göre kilit) → "Pay now".
19. [00:47:00] Ödeme bottom sheet (yarım ekran) → "Pay with card" → check ikonlu "Payment successful" → booking ekranı; seat ID + yolcu adlarını Supabase'e yaz; gerekli `ALTER TABLE` SQL'ini ver → SQL Editor'da çalıştır.
20. [00:52:05] Koltuk haritasındaki "deterministic shuffle" yerine Supabase'ten confirmed booking'lerin `selected_seats` dizisini çek.
21. [00:56:35] İkinci kullanıcıda (Grace) dolu koltuk boş görünüyor → kök neden bookings RLS policy'si (kullanıcı sadece kendi booking'ini okuyabiliyor) → Claude'un verdiği SQL ile düzeltildi → çoklu kullanıcı testi tamam.

## Kullandığı prompt'lar (varsa aynen)
Supabase auth + tablolar [00:09:18]:
```
Set up superbase and work on the authentication. We should use full name, email and password for sign up and only email and password for sign in. Then give me SQL command to create the tables. The first table is users. It should have ID, email, full name, phone created at and updated at. The second table is bookings. It has ID, user ID, flight ID, departure date, passenger, total price, currency status, booking reference created at and updated at. The top table is transactions. It should have ID, booking ID, user ID, amount, currency, payment method, stripe transaction ID, status, created at and updated at. The fourth table is seat inventory. It has ID, flight ID, total seats, booked seats, available seats and updated at. Also add level security policies. So users can only see their own bookings and transactions. Admin can see everything. Make the setup for superbase production ready and secure. Create the EMV file. I will add the keys in there.
```
Sanity şemaları [00:15:40]:
```
I need a complete sanity setup for managing flight data create schema files for JavaScript for airline route and flight aine schema should have name code and logo route schema would Have origin, airport code, origin, city, origin, destination, destination, city and distance, flight schema should have, airline reference, router, departure time, arrival time, duration, price, currency, total seats, available seats, aircraft type, seats, class, and status. Now add the sanity key to the env file. Okay, env. And I will add the key in there.
```
Sanity ↔ Expo [00:20:37]:
```
Now connect Sanity to the Expo app so it can fetch flight data. Also update the MV file to include Expo public sanity project ID and Expo public sanity data set which defaults to production.
```
Ekranlar [00:24:34]:
```
Now let us design the screens. Start by designing the sign up and login screen and wire superbase to work from there. Also design the home screen of the flight booking app and let it have home tab, booking tab and profile tabs and let it have Apple liquid glass bottom tab navigation. We should have fields on the home screen to search for flights. We should have the now the field should be location uh where you're traveling from the destination where you are going to and field to select date you're traveling and date you want you want to return. Then we should also have a field that will bring out a bottom sheet where you can select the class if it's business class or economic class or first class from the from the same bottom sheet. We should be able to also select uh number of adults and number of children that will be traveling. That's from the same bottom sheet. And then we should have a search button on the home screen too. Then also on the profile screen, let's create just the profile with the user full name and you know those name icon avatar at the top with a log out button. Then we should have the flight search result screen which should show after the user searches for flight. At the top we should have a back button and beside the back button in the middle of the screen we should have a bar that should show the destination and location. The date and number of adults should also show on the page. Then we should have a filter and sort buttons under and then we should have a list of flights that are available with their prices. The name of the flight company of that ticket and at the top above that list we should have three tabs to filter which are best, cheapest and fastest.
```
Detay [00:38:38]:
```
Once the user clicks on or selects a ticket, we should have a detail screen that shows the details about the flight ticket. And we should also have the price there. And there should be a button below the screen that says pay now, which should lead to payment. For now, just do the screen.
```
Seed [00:40:10]:
```
add sample flights to sanity so they have as data to display. Create a script called seedflights.js that uses sanity clients to create flights. Create five airlines British Airways Luansa, Ethopian Airline, Emirates and Air France. Create 10 routes between major cities. Lagos to London, Lagos to New York, Lagos to Paris, London to New York, New York to Paris and five more random routes. Create 30 flights using the those airline and routes, various departure times like tomorrow, next week, in two weeks. Realistic prices like [...] $1,000 for short flight. $15,000 for long flights. Uh different air different aircraft types for like Boeing 787, Airbus A380. Different classes like economy or business. Various available seats from 10 to 180. Status is active. Generate instruction on how to run the script to populate sanity. Also generate a CSV template called flight.csv with columns. Aine origin, destination, departure time, arrival time, duration, minutes, price, total seats, aircraft seats, class, status, include 20 sample rows of flight.
```
Arama [00:36:43]:
```
I want the user to be able to search for flights from the home screen. So on the home screen I have input field for origin city, destination city, departure date and number of passengers. When the user fills those in and tap search I want the app to take those values. That's the origin, destination, that's the origin, the destination, the date and the passenger. Uh that's the um departure date only. query sanity for flights that match those details. Navigate to a result screen [...] and only show the flights that match. So the search function should filter flights by origin city based on the input destination city and the departure date. Uh so write the code so that the search happens when the user taps the button search flight not before. The result should show airline name, departure time, arrival time, price and available seats.
```
Debug [00:38:39] ve GROQ düzeltme [00:40:37] (ekranda yazılı, kısmen okundu):
```
Why is that happening? Also we should log our query to the console and when we search for flights we should log the request and the response to the console.
```
```
The GROQ query is failing because the flight schema uses a route reference not embedded origin destination object. Fix the query to dereference correctly. Replace lower(origin.city) with lower(route->originCity) ...
```
Yolcu formu + koltuk [00:44:24]:
```
Right now instead of the pay now button below the screen, let the button say proceed. and it should open another page with a form to fill in the details of the person that wants to travel. Uh depending on the number of maybe adults or children. So you should have fields for um first name, last name, email and also in case is not the owner of the uh account that logged in the app that wants to fly. So also after that it should take us to a seat where we can see um all the seats in the plane. Now I believe we have that data uh and we should be able to see the seat in the plane and the booked seats, the number of booked seats and the available seats. Let's also do that. And the user should be able to tap the position he wants to sit on the plane. And after that once we click um the next button there should say pay now.
```
Ödeme bottom sheet [00:46:51]:
```
When we select our seat and we click uh pay now, let it bring a bottom sheet half the screen that should show uh pay with card. And once we click it, it should um show uh in the bottom sheet payment successful with a check check button or check icon that says payment successful. And then it should take us to the booking screen where we are going to um be able to see the bookings. Now also we are not sending the seat ID and the passenger names to the form uh to superbase. So let's be able to send that to the booking as well. So let's be able to add that. So um let's do that and then you give me the command so I can enable that in superbase.
```
Gerçek koltuk kilidi [00:52:05]:
```
The seat map currently generates booked seats using a deterministic shuffle of accounts from seat inventory. Replace that with a real query. When the seat loads fetch all confirmed bookings for the current flight ID from the superbase booking table. Collect every seat ID from their selected seat arrays and use that as a booked set instead of the shuffle. The flight ID and superbase clients are already available in the screen.
```
RLS hatası [00:56:12]:
```
I logged in with another user. Okay. And a seat that I booked on another user is available in this user app for the same flight and the same date. Let's fix that.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Prompt'ta açıkça "Apple liquid glass bottom tab navigation" istedi (sonuçta gerçekten liquid glass mı emin değil — kontrol edilmeli; muhtemelen `expo-router` native tabs / `expo-glass-effect` gerekir).
- Sınıf + yetişkin/çocuk seçimi için tek bottom sheet; ödeme için yarım ekran bottom sheet + check ikonlu başarı durumu (ham Alert yerine).
- Sonuç ekranı kalıbı: back + ortada rota "pill" barı + tarih/yolcu özeti + filter/sort + Best/Cheapest/Fastest segment sekmeleri.
- Koltuk haritası: uçak düzeni, sınıflara bölünmüş (first/business/economy), seçilmeyen sınıf kilitli, dolu koltuk devre dışı.
- Profil: baş harf avatar + ad + logout. Animasyon/özel kütüphane yok.

## Hatalar ve çözümleri
- Sanity Studio Node 20'de çalışmıyor → `nvm install 22` / `nvm use 22` [00:07:32].
- SQL'de `relation public.admin_users does not exist` → admin rolünü kaldırtıp SQL'i yeniden ürettirdi [00:11:36].
- `npm run deploy` ilk denemede host name yok hatası → tekrar çalıştırıp subdomain girdi [00:18:46].
- Expo projesindeki ajan Sanity şemalarını göremiyor → ikinci Claude Code oturumunu `studio`'da açtı; ders: monorepo/tek üst klasör.
- Arama boş dönüyor → request/response console.log + GROQ'ta `route->` dereference [00:38:39–00:41:27].
- Tarih uyuşmazlığında "No flight found" → seed'deki tarihle arama yaptı.
- Koltuk haritası sahte (shuffle) → Supabase confirmed bookings'ten gerçek dolu set [00:52:05].
- Başka kullanıcının dolu koltuğu görünmüyor → bookings RLS select policy'si sadece kendi satırına izin veriyordu; koltuk bilgisini herkesin okuyabileceği şekilde policy SQL'i [00:56:35]. (Bizde: tüm booking'i açmak yerine sadece `flight_id + selected_seats` dönen view/RPC tercih edilmeli.)
- Supabase e-posta onayı → Auth ayarlarından kapattı (prod'da açık kalmalı).

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Sıra kuralı: boş Expo şablonu cihazda çalışmadan paket kurma; önce veri modeli (Supabase tablo + RLS) ve içerik şeması, sonra ekran.
- "İşletme admin paneli" ihtiyacı için hazır headless CMS (Sanity Studio, `*.sanity.studio`'ya deploy) seçeneği: müşteri ürün/menü/hizmet verisini kendi giriyor; uygulama GROQ ile okuyor. Şablon: `studio/` + `app/` tek repo (monorepo) kur ki tek Claude Code oturumu ikisini de görsün.
- Seed script kalıbı: `seed*.js` + CSV şablonu ürettir; token "Editor", `.env`'de; demo verisi elle girilmez.
- Prompt şablonu: tablo-tablo alan listesi + "RLS: user only own rows" + "give me SQL file" + "Create the .env file, I will add the keys". Admin rolü ilk sürümde yoksa açıkça "no admin" yaz.
- RLS kontrol listesi: "paylaşılan durum" (koltuk, stok, randevu slotu) için çok-kullanıcılı test zorunlu; ayrı public view veya `security definer` RPC ile sadece gereken kolonları aç.
- Fiyat kilidi (10 dk hold) ve stok/slot kilidi gibi iş kurallarını plan aşamasında yaz.
- UI kalıpları kütüphanesine ekle: seçim için bottom sheet (`@gorhom/bottom-sheet` veya native sheet), ödeme sonucu için bottom sheet + check animasyonu, segment sekmeler, koltuk/slot grid bileşeni. Ham `Alert` yasak.
- "liquid glass tab" isteğini prompt'a bırakmak yetmiyor; CLAUDE.md'de somut API (native tabs / `expo-glass-effect`) belirt ve cihazda doğrula.
- Debug alışkanlığı: veri gelmiyorsa ilk prompt "log request + response to console"; sonra şema-sorgu uyumu (reference dereference).
- Supabase Auth: geliştirmede e-posta onayı kapalı, yayından önce açma maddesi checklist'e.
