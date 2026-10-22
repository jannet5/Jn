# Sende — yol haritası ve plan

Çizili (~~böyle~~) olanlar bitti ve kanıtlı. Kanıt ve ayrıntı: `docs/ISTEKLER.md` (107 istek, her
birinin durumu), `docs/SORUNLAR_COZUMLER.md` (bulunan hatalar ve nedenleri), `docs/MIMARI.md`.
Son güncelleme: 22 Ekim 2026.

## Bitenler

- ~~Çekirdek: bildirimden kayda zincir, kişiye göre gruplama, tek ekran, kanal seçici (tüm uygulamalar,
  gerçek ikonlar), geri tuşu, okundu / tertemiz yap / kopyala, resim ve profil fotoğrafı, yanıt ve
  "uygulamada aç", dinleyici sağlığı, ilk kullanım ve izin rehberi.~~
- ~~Dil: Türkçe, İngilizce, Fransızca, Arapça (sağdan sola). Dil telefondan otomatik.~~
- ~~Para: varsayılan bağış (hiçbir şey kilitlenmez), 1/3/6/12 ay destek planları, tek seferlik
  bahşişler, iki deneme varyantı kodu (`-Pmonetization=`).~~
- ~~Ad: Sende. Ad çakışma raporu (`docs/MARKA_ADI_KONTROL.md`).~~
- ~~Marka: 5 konsept panosu (`docs/marka/secim/`), geçici olarak 2 numara uygulandı (açık + koyu tema,
  gömülü yazı tipleri, uyarlanabilir ve tek renkli ikon): `python3 tools/apply_brand.py <1-5>`.~~
- ~~Mağaza ve reklam görselleri (2 numara için): Play ikonu, öne çıkan görsel, 6 ekran görüntüsü,
  3 reklam boyutu: `docs/marka/magaza/konsept-2/`, üretici `tools/make_store_assets.py`.~~
- ~~Test sistemi: 135+ birim/Robolectric/ergonomi/ekran görüntüsü testi, gerçek emülatör matrisi
  (Android 8, 11, 14, 16), küçültülmüş APK duman testi, tek komutla tüm kapılar
  (`tools/kapilar.sh`). Ayrıntı: `docs/TEST_SISTEMI.md`.~~
- ~~Dağıtım hattı: her kapı geçince sabit bağlantıdaki APK yenilenir, tarihli kopya tutulur, sürüm kodu
  artar, izin listesi ve virüs taraması yayın öncesi denetlenir.~~
- ~~Bağımsız denetim, 1. tur: 94 bulgu, gerçek olanlar düzeltildi (S-15 … S-22).~~
- ~~Araştırma 01–10 (rakipler, şikayetler, bildirim çubuğu, güvenilirlik, tasarım, YZ araçları, marka,
  Play yayın rehberi, fiyatlandırma): `docs/arastirma/`.~~

## Seni bekleyenler (ben yapamam)

1. **Marka numarası (1–5).** Seçince `apply_brand.py` + görsel üretici yeniden çalışır.
2. **Telefonda deneme:** APK bağlantısını aç, kur, bildirim erişimini ver, gerçek WhatsApp/Instagram
   mesajıyla dene (`dist/README.md`). Bilgisayarsız, tek dokunuşluk yol.
3. **Dağıtım imza sırrı:** `bash tools/make_release_key.sh` çıktısını GitHub'a `SENDE_SIGNING_B64`
   olarak ekle (ilk gerçek kullanıcıdan ÖNCE). Ben GitHub sırlarına erişemem.
4. **Ad taraması:** TÜRKPATENT / WIPO (Play'de "Sende" adlı başka bir uygulama var).
5. **Play Console:** hesap, 25 USD, kimlik, 12 test kullanıcısı × 14 gün, formlar (`docs/RELEASE.md`).
6. **Drive'a APK:** bağlayıcı ikili dosya taşıyamıyor; yerine sabit GitHub bağlantısı + QR.

## Benim sırada olanlarım

- ⏳ Araştırma 11–14 (test sistemi, cihaz içi YZ, APK dağıtımı/güven, Play politikası + KVKK): alt-ajan
  kotası 24 Ekim 06:00 UTC'de açılınca çalıştırılacak (betik hazır).
- ⏳ Denetim 2. tur: bulguları iki bağımsız şüpheciyle doğrulatma (kota ile aynı).
- ⏳ YZ'siz sürüm yayın hazır; YZ'li ayrı uygulama (yan yana kurulur, cihaz içi, internetsiz):
  tasarım araştırma 12'ye göre, ardından ikinci `applicationId` ve model katmanı.
- ⏳ Seçilen markayla uzun marka görseli (vektör) ve son görseller.
- ⏳ Son teslim: kaynak zip (tam yolla), APK, doğrulama özeti.

## Bilinen sınırlar (dürüst)

- Gerçek WhatsApp/Instagram bildirimi gerçek telefonda denenmedi (yalnız sen yapabilirsin).
- Android 8'de çapraz uygulama bildirim testi yok (`cmd notification post` Android 11'de geldi).
- Üretici pil kısıtları (Samsung/Xiaomi/Oppo) yalnız rehber ve sağlık kartıyla karşılanıyor; 7 günlük
  gerçek cihaz denemesi yapılmadı (`docs/arastirma/05`).
- Play Protect, bildirim dinleyiciyi "yüksek riskli" sayıyor; bazı ülkelerde tarayıcıdan inen APK'yı
  engelleyebilir. Türkiye'nin kapsamda olup olmadığı doğrulanamadı. Ana yol Play olmalı.
