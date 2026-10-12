# Claude Code Tutorial for Beginners: Build App with AI (2026) — Mikey No Code — 21 dk — https://www.youtube.com/watch?v=6q8joS_592k

## Tek paragraf: ne yapıyor, sonuç ne
VS Code'a Claude Code eklentisini kurup önce ısınma olarak bir "Wikipedia benzeri" React+Node web sitesi ve dark mode ekletiyor; sonra asıl iş olarak Question AI (aylık ~400 bin $) klonu bir **matematik ödev yardımcısı** mobil uygulaması (React Native + Expo SDK 54) yaptırıyor: 5 sekme (dashboard, saved, learn, quiz, settings), kamera/galeri ile soru fotoğrafı, OpenAI vision ile çözüm + adım adım açıklama, kaydedilenler, konu anlatımı ve quiz. Tamamen "tek prompt → test et → sonraki özellik" döngüsü; mimari, tasarım sistemi, skill, CLAUDE.md, yayın yok (yayın Skool'a satılıyor). Değeri: özellik sıralaması ve kısa prompt şablonları.

## Kullandığı araçlar / skill'ler / kütüphaneler / şablonlar (linkleriyle)
- VS Code + resmi **Claude Code** eklentisi (Anthropic onaylı).
- Git (git-scm.com/install), Node.js (nodejs.org).
- React Native + **Expo SDK 54**; Claude API key (ekranda gösteriliyor, sonra kullanılmıyor), **OpenAI API** (görüntü analizi), `.env` + `.gitignore`.
- Skill / MCP / şablon yok.
- Açıklama linkleri: https://mikeyno-code.com/Skool-claude (App Store yayın anlatımı, ücretli), https://mikeyno-code.com/newsletter , https://mikeyno-code.com/video68

## Adım adım nasıl yaptı (sırayla, her adımda hangi komut/prompt/dosya)
1. [00:01:58] VS Code → Extensions → "Claude Code" (Anthropic verified) kur, yeni pencere aç; sağ üstteki Claude ikonu ile chat paneli.
2. [00:03:43] Isınma: "Wikipedia benzeri site" prompt'u; tüm komut izinlerini onayla; Claude'un verdiği çalıştırma komutunu terminale yapıştır, localhost'ta aç.
3. [00:05:25] Mevcut projeye değişiklik: dark mode prompt'u → [00:06:11] "Launch the application." ile Claude'a sunucuyu kendisi başlattırma (tema toggle görüldü).
4. [00:06:58] Git + Node.js kurulumu (önkoşul).
5. [00:08:35] Asıl uygulama: RN+Expo SDK 54, 5 bölüm, **sadece front-end + placeholder data** → koyu temalı statik iskelet.
6. [00:09:57] Dashboard'a fotoğraf yükle/çek widget'ı + önizleme.
7. [00:10:56] API key alma (gizli tut, hardcode etme).
8. [00:13:00] OpenAI entegrasyonu: görüntüden soruyu oku, cevap + kapsamlı çözüm; `.env` ve `.gitignore` oluşturt.
9. [00:15:48] Saved bölümü: placeholder'ları kaldır, tarama + cevabı kaydet butonu.
10. [00:17:13] Learn bölümü: seviyelere göre geniş konu listesi.
11. [00:18:19] Quiz bölümü: zorluk seviyeli kısa quizler, sonda doğru cevap + açıklama.
12. [00:19:50] Uçtan uca elle test: tara → kaydet → saved'da kontrol → learn → quiz.

## Kullandığı prompt'lar (varsa aynen)
```
Please create a simple website using React and Node similar to Wikipedia.
```
```
Please add a dark mode theme for the application.
```
```
Launch the application.
```
```
Hey Claude, inside this workspace, please create a React Native Plus Expo application that utilizes SDK 54. Create a simple front-end project of a math focused homework helper. Create five main sections that are mobile optimized: dashboard, saved, learn, quiz, and settings. Create only the front end with placeholder data and don't build any actual functionality yet.
```
```
Great job. Please create a widget at the dashboard that allows the user to upload or take photos. Once a photo has been uploaded or taken, show a preview of the photo.
```
```
Please integrate OpenAI and build the image scanning functionality. Utilize OpenAI to assess the questions from the scanned image. Then show the answers as well as a complete comprehensive breakdown of how the answer was acquired. Tailor the AI to be proficient with math in particular. Here's the OpenAI API key. [Insert your key here] and make sure to create a .env file for storage. Configure it accordingly. Then set a .gitignore file. Do not hard-code any API keys or sensitive information in the app.
```
```
Let's improve the application further by creating the save section. Remove all placeholder data from the app. Then make sure all scanned images and their answers can be saved by the user to the saved section.
```
```
Now please create the learn section for our app here. Have a broad range of mathematics courses across various levels that the user can use to review. Make this as comprehensive as possible.
```
```
Now please build the actual quizzes feature of the application. Remove any remaining placeholder data and give the user a wide range of short quizzes across various difficulty levels so they can practice and learn. Show the proper answers at the end with clear explanations.
```

## Tasarım/animasyon için yaptığı özel şeyler (güzel görünmesini sağlayan ne?)
- Bilinçli tasarım çalışması yok; Claude varsayılan olarak koyu tema üretti.
- Tek UX notu: fotoğraf **önizlemesi** işlemden önce gösteriliyor (kullanıcı kareyi kontrol etsin) [00:10:09].
- "Önce front-end + placeholder, işlev sonra" sırası ekran yerleşimini erkenden netleştiriyor.

## Hatalar ve çözümleri
- Claude ikonu görünmezse: VS Code'u yeniden başlat / yeni pencere, eklenti kurulumunu kontrol et [00:03:05].
- Videoda başka hata gösterilmiyor.
- Dikkat (bizim notumuz): OpenAI key'i `.env` ile Expo istemcisine koymak key'i yine pakete gömer (`EXPO_PUBLIC_*`); üretimde backend/edge function proxy şart.

## Bizim fabrikaya alınacaklar (somut, maddeli)
- Prompt'larda framework + sürümü açıkça yaz ("React Native + Expo SDK 54"); videonun vurguladığı tek gerçek kural.
- Aşama 1 şablonu: "Sekmeler: X,Y,Z — only front end with placeholder data, no functionality yet" → sonra her özellik ayrı prompt ve her prompt'ta "Remove placeholder data" ifadesi.
- Gizli anahtar kuralı CLAUDE.md'ye: `.env` + `.gitignore`, hardcode yasak; AI anahtarları istemcide değil sunucu tarafında (Supabase Edge Function vb.).
- Görsel girişli özelliklerde standart: kamera/galeri seçimi + önizleme + onay adımı.
- "Launch the application" ile sunucuyu Claude'a başlattırma; fabrikada `npx expo start` görevi agent'a verilebilir.
- Eksik: tasarım/animasyon hiç yok → bu video ancak "özellik sıralaması" referansı; görsel kalite için diğer (Beto, Tim misjUj4Q_ho) planlara bak.
