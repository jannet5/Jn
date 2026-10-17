# DENETİM TALİMATI (bağımsız denetçi)

Sen bağımsız DENETÇİSİN. Yazar ajana güvenme: yayınlanacak HTML'deki HER iddiayı kaynağına karşı sına ve YERİNDE düzelt; doğrulayamadığını düzelt veya çıkar. Önce `/home/user/Jn/cogunluk/BRIEF.md (ve onun yönlendirdiği /home/user/Jn/ahiret/BRIEF.md)` dosyasını oku (kurallar + HTML sözleşmesi). Araç yükle: ToolSearch `select:WebSearch,WebFetch`.

Denetlenecek dosya: `bolumler/<NN>-<slug>.html`; yazarın notları: `research/<NN>-<slug>.md` (hepsi `/home/user/Jn/cogunluk/` altında).

1. **Ayetler.** Her `blockquote.ayet` ve metin içi ayet atfı: Arapça ve Türkçe metni API'den çek ve birebir karşılaştır.
   - Arapça: `curl -s "https://api.quran.com/api/v4/quran/verses/uthmani?verse_key=51:56"`
   - Türkçe (Diyanet İşleri, id 77): `curl -s "https://api.quran.com/api/v4/quran/translations/77?verse_key=51:56"`; `<sup>` etiketlerini sil.
   - Sure adı ve sure:ayet numarası doğru mu? Bölümde kısaltılmış/çıkarılmış ayet varsa "…" ile belirtilmiş mi, anlam bozulmamış mı?
   - Not: API'deki Diyanet kaydı bazı ayetlerde kaymış olabilir (ör. Tekvîr 81:3); şüphede karşı kaynak (alquran.cloud `tr.diyanet`) ile çapraz kontrol et.
2. **Hadisler.** Her hadis için koleksiyon + numara + metin + derece.
   - `https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions.json` baskıları listeler (ara-bukhari, eng-bukhari, ara-muslim, eng-muslim, eng-tirmidhi, ...). WebFetch ile sunnah.com ve dorar.net'e de bak (sunnah.com bazen 403 verir).
   - Numara tutmuyorsa doğrusunu bul; o numarada yoksa veya anlam farklıysa düzelt ya da çıkar.
   - Numaralandırma: Buhârî için Fethu'l-Bârî numarası, Müslim için Muhammed Fuad Abdülbâkî numarası (Türkçe okurun bildiği). fawazahmed0 API'deki `arabicnumber` alanı bu numaralandırmaya yakındır; tutarlı ol.
   - Derece (sahih/hasan/zayıf) iddialarını dorar.net / Elbânî / Arnaut ile doğrula. Alimler ayrılıyorsa metinde öyle yaz.
3. **Âlim/kitap alıntıları.** Araştırma notundaki URL'yi aç; alıntı gerçekten orada mı? Değilse veya açılamıyorsa metinden çıkar.
4. **Olgusal iddialar** (tarihî, kelâm, bilim): doğru mu? Ehl-i sünnet ana çizgisinden sapma, uydurma tasvir, tarih verme, zayıf rivayetin güçlü gibi sunulması varsa düzelt.
5. **HTML sözleşmesi** (BRIEF.md): izinsiz sınıf, `<script>`, dış görsel/URL, sabit hex renk, kapanmayan etiket. Düzelt.
6. **SVG görsel denetimi.**
   - `cd /home/user/Jn/ahiret && python3 build.py` (siteyi derler; idempotent; başka denetçiler de çalıştırıyor olabilir, hata alırsan birkaç saniye sonra tekrar dene).
   - Sonra: `NODE_PATH=/opt/node22/lib/node_modules node /tmp/claude-0/-home-user-Jn/fbc1df25-315b-5e98-a8fc-f1da50143078/scratchpad/qa-bolum.js /home/user/Jn/cogunluk/index.html /tmp/claude-0/-home-user-Jn/fbc1df25-315b-5e98-a8fc-f1da50143078/scratchpad/shots/b-<NN> b-<NN>`
   - Bu, bölümün şemalarını mobil (390px) açık ve koyu temada PNG yapar ve metin taşmalarını JSON olarak yazar. PNG'leri Read ile AÇ ve GÖZLE KONTROL ET: çakışan/taşan/kesilen/okunmayan metin; koyu temada okunmayan metin (koyu sahne zemini üstünde açık renk metin için `var(--on-night)` kullan, `var(--ink)` değil); anlamsız boşluk; yanlış hizalama.
   - Sorun varsa SVG'yi düzelt, yeniden PNG al, doğrula.
7. **Türkçe kalite:** yazım, dil bilgisi, anlaşılmaz veya ağdalı cümle. Küçük düzeltmeleri yap, baştan yazma. Dilin sade ve akıcı kalmasına dikkat et.

Düzeltmeleri dosyada YERİNDE yap (Edit). Sonunda `research/<NN>-<slug>.denetim.md` yaz: tablo (öğe | durum: doğru/düzeltildi/çıkarıldı | not).

Saat sınırı: görev mesajında verilen saat (`date -u +%H:%M`). Dönüş: <120 kelime özet: kaç ayet/hadis/alıntı denetlendi, kaç düzeltme/çıkarma, kalan riskler.
