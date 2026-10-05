# Elimizde ne var (faz 2 tasarımına girdi)

## Mevcut "uygulama-fabrikasi" skill'i (claude.ai skill, 8 aşamalı)
Fikir → Rakip&Yorum → Ürün → Marka&Tasarım (DESIGN.md) → Kurulum (Expo + NativeWind + RN Reusables) → Vitrin → Ekranlar → Kalite&Yayın.
Güçlü yanları: kapılar, DESIGN.md token zorunluluğu, tasarım denetimi scripti, yorum toplama scripti, ders döngüsü (D-00X).
Zayıf yanları (kullanıcının şikâyeti): çıktı yine "çirkin / animasyonsuz / Android'in ham pop-up'ları" olabiliyor → referans + animasyon + bileşen kalitesi katmanı eksik; skill'ler/araçlar/prompts araştırılmadan yazıldı.

## Geçmiş uygulamalardan dersler (Drive: Uygulama Fabrikası/melodi-zil, kuran-oku)
- D-004: Kullanıcı "tamamını bitir" derse kapılar onay beklemeden geçilir, çıktılar yine dosyaya yazılır.
- D-005: Ağır cihaz-üstü medya işi varsa native Kotlin + Compose istisnası.
- D-008: JVM testi yetmez; en düşük API'de gerçek Android (emülatör API 26 x86, -no-accel) akışı koşulmadan "bitti" denmez.
- D-009: Bottom sheet tam açık + scroll + navigationBarsPadding; birincil buton açılışta görünür.
- Bulut IP'si YouTube tarafından bot sayılabiliyor → alt sohbetler (farklı IP) kullan, yeniden dene.

## Faz 2'de cevaplanacak sorular
1. Mobil hattı: Expo + hangi skill'ler + hangi animasyon/bileşen seti ile "fıstık gibi" çıkar? (YouTube-1, GitHub, X sonuçları)
2. Web hattı: Next.js/Astro + hangi tasarım skill'i + animasyon (Motion/GSAP/Lenis) + görsel üretimi? (YouTube-2)
3. Reklam + sosyal medya hattı: içerik üretim otomasyonu, Meta Ads kurgusu, 30 günlük plan (YouTube-3, rakipler)
4. Orkestrasyon: ana beyin + alt sohbetler, CLAUDE.md, hooks, kalite kapıları (YouTube-4)
5. Kendi vitrinimiz: site bölüm sırası, paketler, fiyat, CTA (rakipler OZET)
