# AJANS — Dijital Hizmet Fabrikası (çalışma alanı)

**Ne kuruyoruz:** İşletmelere (restoran, dükkan, klinik, saatçi… herhangi biri) **web sitesi + mobil uygulama + reklam yönetimi + sosyal medya içerik** hizmeti veren, yapay zekâ ile üreten bir ajans.
Hedef: "start" verildiğinde zincir baştan sona çalışsın ve ortaya **fıstık gibi** ürün çıksın (çikolata fabrikası mantığı).

## Harita (hızlı yol)
1. **Masayı doldur (veri topla)** — paralel, ayrı sohbetlerde:
   - YouTube: "AI ile mobil uygulama / web sitesi üretme" videolarını bul, transkriptlerini TXT çek.
   - X.com: skill / prompt / araç / repo paylaşımlarını topla.
   - GitHub: skill'ler, DESIGN.md kaynakları, animasyon ve UI kütüphaneleri.
   - Rakip ajanslar: web siteleri, Instagram hesapları, paketleri, ekran görüntüleri.
2. **Haritaları çıkar** — her transkript → "adım adım nasıl yaptı" planı (ayrı sohbetlerde).
3. **Zinciri kur** — `sistem/`: skill'ler + prompt'lar + araçlar + kontrol kapıları (web, app, reklam, sosyal medya hatları).
4. **Kendi vitrinimizi üret** — web sitemiz, mobil uygulamamız, Instagram/Facebook hesapları ve ilk reklam.

## Klasörler
```
ajans/
  araclar/                 yt_transcript.py (transkript), yt_ara.py (video arama)
  arastirma/
    youtube/               index.md (seçilen videolar) + transkriptler/<id>.txt + <id>.meta.json
    x/                     X.com notları (link + özet + ne öğrendik)
    github/                repo / skill / prompt notları
    rakipler/              rakip ajanslar: notlar, linkler, ss/ (ekran görüntüleri)
  planlar/                 transkriptlerden çıkan adım adım planlar
  sistem/                  fabrika zinciri: skill'ler, prompt'lar, kontrol listeleri
```

## Kurallar
- Her bulgu dosyaya yazılır (link + not + varsa SS). Sohbette kalan bilgi yok sayılır.
- Hız = doğru yöntem (terminal/kod/API), gereksiz tıklama yok. Hız ≠ baştan savma.
- İş dağıtılır: bu dosyayı okuyan her sohbet kendi görevini alır, sonucunu buraya yazar ve `ccr-11072837-dfhtyx` dalına push eder.

## Araç notları
- YouTube bu bulut IP'sini zaman zaman engelliyor. `yt_transcript.py` sırayla yt-dlp (android/web/ios…), youtube-transcript-api ve Invidious dener. Arama için `yt_ara.py` (yt-dlp → Invidious yedekli).
- Engel yenirse: kısa bekleyip tekrar dene; başka bir sohbet (başka IP) üzerinden çek.
