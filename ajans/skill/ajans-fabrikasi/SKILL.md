---
name: ajans-fabrikasi
description: Vitrin Atölyesi ajans fabrikası. Bir işletme için web sitesi, mobil uygulama, Instagram/Facebook içeriği, reklam ya da satış için örnek site istendiğinde; yeni müşteri başlatıldığında; "fabrikayı çalıştır", "müşteri geldi", "site yap", "uygulama yap", "içerik hazırla", "reklam kur", "müşteri bul" denildiğinde kullan.
---

# AJANS FABRİKASI

Yerel işletmelere (kafe, restoran, berber, klinik, butik, usta) **web sitesi + mobil uygulama + sosyal medya + reklam** üreten hat. Kullanıcı (womwom) telefondan yazar; Türkçe, samimi, kısa konuş. Kullanıcı karar vermezse kararı sen ver, `references/KARARLAR.md`'ye yaz, değiştirilebilir bırak.

**Ana depo:** GitHub `jannet5/Jn`, dal `ccr-11072837-dfhtyx`, klasör `ajans/`. Oturum başında `ajans/README.md`, `ajans/KARARLAR.md`, ilgili müşterinin `DURUM.md` + `DERSLER.md` dosyalarını oku. Depo yoksa bu skill'in `references/` ve `scripts/` klasörleri aynı içeriği taşır.

## Teşhis (neden çirkin çıkıyordu)
Model değil, kurulum: **yasak listesi + sayılarla DESIGN.md + ajanın kendi ekran görüntüsüne bakıp düzelttiği kapı** yoktu. Bu üçü zorunlu.

## Bant
```
S  SATIŞ          references/hatlar/musteri-bulma.md → önce örnek site yap, sonra "sizin için hazırladım" de
1  INTAKE         references/02-intake.md (10 dk) → BRIEF.md (boş alan = TODO, uydurma yok)
2  DESIGN.md      3-5 referans → 3 yön → biri seçilir → sayılarla DESIGN.md → KAPI-1
3  SPEC           mobilde Nick'in 5 alanı (çekirdek işlev, döngü, yardımcı, 5-7 ekran, kanca)
4  ÜRETİM         references/hatlar/web.md | mobil.md
5  GÖRSEL QA      ekran görüntüsü al (web 375/768/1440, mobil 390×844, açık+koyu), dilimleyip KENDİN bak → KAPI-2
6  CİLA           ayrı mesajlarla: tipografi → boşluk → hareket → mobil → "daha pahalı" turu
7  KONTROL        KAPI-3 (20 kontrol + 10K puanı) → teslim, KAPI-4 insan
8  PAZARLAMA      references/hatlar/sosyal-medya.md + reklam.md (aynı DESIGN.md'den)
9  DERS           her iş sonunda DERSLER.md → zincire işlenir; aynı hata iki kez yaşanmaz
```
Kapılar: `references/KAPILAR.md`. İnsan yalnız intake, yön onayı ve son kontrolde; kullanıcı "hallet/bitir" derse kapılarda bekleme, kararları DURUM.md'ye yaz.

## Başlatma
```bash
bash ajans/sistem/yeni-musteri.sh <musteri> <web|mobil|web+mobil> "<tek aksiyon>"   # klasör + CLAUDE/BRIEF/DESIGN + .claude ajan/skill + QA betikleri + Stop kancası
bash ajans/sistem/kurulum.sh ajans/musteriler/<musteri>                           # skill ve MCP kurulumu
```

## Değişmez kurallar
- Tasarımın tek kaynağı DESIGN.md. Yeni renk/font/boyut uydurma, dur ve sor.
- Sahte yorum, logo, istatistik, müşteri yok. Müşteri yokken "Örnek konsept" etiketli örnek; "En popüler" yerine "Önerilen".
- Yasaklar: mor gradyan, Inter display, emoji ikon, 3 eşit kart, her şey ortalı, her bölümde aynı fade-up, stok foto, em dash, "Elevate/Seamless", ajans jargonu. Mobil: Alert.alert, RN Button, TouchableOpacity, RN SafeAreaView, elevation, ease-in.
- Kanıtsız "bitti" yok: build + SS + kullanıcı testi (Stop kancası `scripts/web/kontrol.sh`).
- Kontrast her token için hesaplanır (≥4.5:1), tahmin edilmez.

## Stack
- **Web:** Astro + Tailwind, Archivo gibi karakterli tek aile, fiyat/iletişim `src/site.config.ts`, Cloudflare Pages. Önizleme: `scripts/web/artifact.py dist/index.html cikti.html "Ad"` → Artifact.
- **Mobil:** Expo + Expo Router + TS strict + NativeWind (yalnız düzen) + renkler `tokens.ts`'ten inline + Reanimated (`cssInterop`, `.set()/.get()`) + expo-haptics; `npx skills add expo/skills --skill '*'`; kendi Toast/AltSayfa/OnaySayfası. QA: `scripts/mobil/ss.mjs`. Teslim: SS + Expo Go/EAS Update linki.
- **Sosyal:** HTML şablon → Playwright 420×525 dsf 18/7 → 1080×1350 JPEG; caption `skills/tr-caption`; ızgara önizlemesi 3:4; her zaman onay kapısı.
- **Reklam:** ad-score (hook 25, netlik 20, teklif 20, kanıt 10, CTA 15, marka 10, <70 yayına girmez), kampanya PAUSED başlar, bütçe 150/300/600 ₺/gün.

## Fiyat (KARARLAR.md, KDV hariç)
Vitrin 2.990 ₺/ay + 4.990 kurulum · Müşteri Makinesi 5.990 ₺/ay + 9.990 (Önerilen) · Büyüme 11.990 ₺/ay + 24.990 (mobil uygulama dahil) · ilk 5 işletmeye kurulum 0 ₺ ↔ vaka çalışması izni · 12. ayda site/uygulama müşterinin · 7 günde yayın.

## Orkestrasyon (büyük işte)
Ana sohbet kod yazmaz; işi ayrı Claude Code bulut oturumlarına böler (`references/orkestrasyon.md`). Her göreve 5 alanlı kart: görev · girdi · ölçülebilir başarı · çıktı yeri · yapma listesi. Aynı anda en fazla 3-4 oturum (hesap limiti). Oturumlar 20-30 dk'da bir commit + `pull --rebase` + push. 25-60 dk sonra `send_later` ile kendini uyandır, SS'lere kendin bak, DERSLER'i zincire işle.

## Araçlar
- YouTube transkript: `scripts/yt_transcript.py <id>` (bulut IP'si engellenebilir; başka oturumdan dene). Arama: `scripts/yt_ara.py "<sorgu>"`.
- Hazır varlıklar (depoda): vitrin sitesi `ajans/musteriler/ajansimiz/`, satış demosu uygulama `ajans/musteriler/ajansimiz-app/`, Instagram paketi `ajans/musteriler/ajansimiz/sosyal/`, araştırma `ajans/arastirma/`, planlar `ajans/planlar/`.

## Kullanıcıda kalanlar
WhatsApp numarası, alan adı, Instagram/Facebook/Meta Business hesabı, reklam ödeme yöntemi, uygulamanın gerçek telefonda denenmesi. Bunlar gelince config'e yaz, içerikleri yayına al.
