# Build Your First App with Claude Code in 20 Minutes (The Right Way) — Code with Beto — 20 dk — https://www.youtube.com/watch?v=sneNR5KnRJU

## Tek paragraf: ne yapıyor, sonuç ne
Eski Expo developer success engineer Beto, "web sitesi değil gerçek mobil uygulama" vurgusuyla `create-instant-app` (Expo + Claude seçenekleri) ile proje + InstantDB backend kuruyor, Expo Go ile iOS ve Android'de aynı anda test ediyor, **Expo ekibinin `building-native-ui` skill'i** ve InstantDB skill'lerini project scope'ta kuruyor, plan mode'da veri şemasına odaklanan bir prompt veriyor; sonra Figma'dan (kendi Minima uygulaması) **iki ekran görüntüsü** yapıştırıp "native behavior on iOS" isteyerek UI'yı iyileştiriyor. Sonuç: gerçek zamanlı senkronize, native form sheet, liquid glass butonlu, scroll'da animasyonlu başlıklı paylaşımlı to-do uygulaması.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- Expo Go (iOS/Android), VS Code + Claude Code eklentisi (kurulum videosu: https://youtu.be/i5wd1-QGE1A).
- **InstantDB** (gerçek zamanlı DB, ücretsiz katman): https://www.instantdb.com?utm_source=beto — `create-instant-app` CLI (framework: "mobile with Expo", AI: Claude).
- Expo Router (form sheet / bottom sheet route).
- Skill'ler: **building-native-ui** (Expo ekibi) + **InstantDB skills** — kurulum komutları burada: https://codewithbeto.dev/blog/my-favorite-claude-skills (komut çalıştırınca ajan seçiminde Claude Code'u `Space` ile işaretle, scope: project).
- Figma tasarımları (ücretsiz, Minima Task Manager): https://codewithbeto.dev (Designs bölümü).
- Kitap "From Idea to App Store with Claude Code": https://cwb.sh/book?r=yt-build-app-with-ai , https://cwb.sh/book?r=yt ; RN kursu: https://cwb.sh/rn?r=yt ; Pro: https://cwb.sh/pro?r=yt ; Discord: https://cwb.sh/discord?r=yt ; bülten: https://cwb.sh/newsletter?r=yt ; diğer: https://apple.co/4tvT4wF , https://cwb.sh/join , https://cwb.sh/gh?r=yt , https://cwb.sh/inkigo?r=yt , https://cwb.sh/platano?r=yt , https://github.com/sponsors/betomoedano

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:00:36] Telefona Expo Go; InstantDB'de hesap aç (uygulama oluşturma, CLI yapacak); VS Code.
2. [00:01:53] VS Code → Open Folder → masaüstünde `do app` klasörü; Claude Code eklentisini kur (`Shift+Cmd+X`).
3. [00:03:05] Terminal: `npx create-instant-app` (InstantDB ana sayfasındaki komut) → "mobile with Expo" → AI: Claude.
4. [00:04:32] "Create a new app?" → Enter; `Tab` ile isim alanı → `do app video` → InstantDB dashboard'da backend oluştu.
5. [00:05:25] `npm run start` → iOS'ta kamera ile QR, Android'de Expo Go "Scan QR code" → hello world renk seçici iki cihazda gerçek zamanlı senkron.
6. [00:06:32] **Yeni terminal** (sunucu terminaline dokunma) → blog'daki building-native-ui komutu → Claude Code seç, project scope → InstantDB docs'taki "Instant skills" komutu → aynı seçimler. (`clear` ile terminali temizle.)
7. [00:09:01] Claude eklenti panelinde **plan mode** (`Shift+Tab`) → to-do prompt'u. Stil için token harcama, önce işlev/veri.
8. [00:12:10] Planı incele: no auth (istenirse InstantDB magic link), form sheet, **veri şeması** (emoji, title, description, status, createdAt) → değişiklik varsa kod yazılmadan önce iste.
9. [00:13:25] "Yes and auto accept" → komut izinlerine yes.
10. [00:14:05] Cihazı salla → Reload; "+" → bottom sheet; iki telefonda gerçek zamanlı test.
11. [00:15:24] Figma'dan iki screenshot kopyala → Claude'a `Cmd+V` → UI iyileştirme prompt'u.
12. [00:18:33] Android'de öğeler kesik, buton çalışmıyor, iOS başlık animasyonu yok → hepsini tek mesajda tarif et → düzeltildi. Context %50'yi geçince yeni session aç.

## Kullandığı prompt'lar (varsa aynen)
```
I want to build a minimalistic share to-do list. Real-time, no login for now because it's a personal app just for me and my family. Any user can see, add, and check off tasks. The home screen should display a list of tasks with a checkbox. Using Expo router, create a bottom sheet screen to add new tasks. Each task can have an emoji, title, optional description, status, and when it was created. For now, don't worry about the styling too much. [Use Instant DB to store the data and for real-time functionality.]
```
UI iyileştirme (2 Figma screenshot ile birlikte; transkriptteki ifadeyle):
```
Use the images as inspiration to improve the UI. On iOS, we want a native behavior. Use black for the primary color and add a nice box shadow to the check box.
```
Hata düzeltme mesajı birebir okunmuyor; içerik: Android'de bazı öğeler kesiliyor, "hide completed" Android'de çalışmıyor, iOS'ta "Today" başlığı animasyonu yok.

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- **Expo'nun `building-native-ui` skill'i**: Beto'ya göre UI'nın "way better" olmasının ana nedeni; native form sheet, iOS **liquid glass** butonu, büyük başlığın scroll'da animasyonu bu skill + ekran görüntüsü ile geldi.
- Tarif yerine **görsel referans**: gerçek bir Figma tasarımından ekran görüntüsü; birden fazla görsel aynı anda.
- Kısıtlar: tek primary renk (siyah), checkbox'a box shadow, "native behavior on iOS".
- Native bileşen tercih: Expo Router form sheet/bottom sheet presentation (JS modal değil).
- İlk turda bilinçli olarak **stil yok** → token tasarrufu; stil ikinci turda referansla.

## Hatalar ve çözümleri
- Cihaz eski ekranda takılı kalıyor → cihazı salla → dev menu → Reload (Claude değişiklik yaptıktan sonra) [00:14:06].
- Android'de liste öğeleri kesik, "hide completed" butonu Android'de çalışmıyor, iOS'ta başlık animasyonu yok → belirtileri platform platform tarif et → Claude düzeltti [00:18:45].
- Emoji elle yazılıyor (garip UX) → emoji picker isteği olarak not edildi.
- Uzun oturumda kalite düşmesin: context > %50 → yeni session.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- **Zorunlu skill seti**: her müşteri projesine project scope'ta Expo `building-native-ui` + backend'in kendi skill'i (InstantDB / Supabase). Komutları blog'dan alıp fabrika kurulum script'ine sabitle: https://codewithbeto.dev/blog/my-favorite-claude-skills
- "Gerçek uygulama, web değil" kuralı: her çıktı Expo Go/dev build ile **iOS + Android gerçek cihazda** aynı anda test edilir; Android'e özel kırılmalar (kesilen içerik, çalışmayan butonlar) QA listesinde.
- Plan mode'da **önce veri şeması**: alanlar kod yazılmadan kesinleşsin (şema değişikliği = DB push/deploy maliyeti).
- İki turlu UI: (1) işlev, stil yok; (2) müşteri markası/Figma veya referans uygulama screenshot'ları + "native behavior" + tek primary renk.
- Modal/pop-up yerine Expo Router `presentation: 'formSheet'` / native sheet — "Android'in ham pop-up'ları" şikâyetine doğrudan çözüm; CLAUDE.md'ye "Alert/Modal yerine native sheet" kuralı.
- Context yönetimi kuralı: %50 üstü → yeni session.
- Hızlı prototip için `create-instant-app` (Expo + Claude) tek komutla backend+uygulama: demo/teklif aşamasında kullanılabilir.
- Beto'nun kitabı (yayın: ikon, sertifika, app review) EAS/App Store aşaması için kaynak adayı.
