# Bağlı uygulama haritası

```
HEDEF: Tıp öğrencileri için girişsiz, web+mobil, ortak PDF kütüphanesi;
       her dilde soru → kaynaklı yanıt; şeffaf, adil, iş ortasında kesmeyen kota
  │
  ├─ B1 PDF'ten metin ──────────── PyMuPDF (sort=True)           → app/pdfproc.py
  ├─ B2 Diller arası eşleşme ───── çok dilli gömme (yerel ONNX)  → app/embed.py
  ├─ B3 Ortak depolama ─────────── SQLite + bellek içi vektör    → app/db.py, app/search.py
  ├─ B4 Girişsiz kimlik + kota ─── anonim cihaz + kredi defteri  → app/quota.py
  ├─ B5 Web + mobil arayüz ─────── tek sayfa PWA (TR/EN/ES)      → static/
  └─ B6 Kötüye kullanım / telif ── hak beyanı, bildir-gizle, IP sınırı, yönetici kodu
        │
        ▼
  YOL SEÇİMİ (B2 en riskli bağımlılık)
   A) Yerel çok dilli gömme + alıntı yanıtı; isteğe bağlı Claude ile soru dilinde özet  ← SEÇİLDİ
   B) Her soruda yalnız bulut LLM (RAG)  → API anahtarı ve ödeme şart; anahtar yok → kırılır
   C) Anahtar kelime (BM25) + TR/ES tıp sözlüğü → tamamen yerel ama sözlük dışı kavramı kaçırır
        │  A kırılırsa (model indirilemezse) → C'ye düş; LLM anahtarı gelirse A'nın YZ kipi açılır
        ▼
  UYGULAMA → TEST → TESLİM
   1 pytest kabul (gerçek PDF + gerçek model + HTTP API) ── tests/test_acceptance.py  ✔ 5/5
   2 tarayıcı E2E (gerçek uvicorn, masaüstü + iPhone 13) ── scripts/e2e.py           ✔
   3 görsel kontrol (5 ekran görüntüsü, açık/koyu)        ── teslim/ekranlar/         ✔
   4 Docker imajı (model gömülü)                          ── Dockerfile               (bkz. günlük)
   5 Kalıcı teslim: görev dalına push + git bundle/ZIP + SHA-256 geri okuma
```

## Neden A?
- **B2 gereksinimi:** "İspanyol, Türk'ün yüklediği PDF'ten cevap alsın." Testte `paraphrase-multilingual-MiniLM-L12-v2`, İspanyolca soruyu ilgili Türkçe sayfayla 0,64–0,72 benzerlikle eşleştirdi; ilgisiz sayfalar 0,20–0,44'te kaldı.
- **Anahtarsız çalışma:** API anahtarı ve ödeme olmadan çalışıyor; kaynak sohbetteki "ücretlendirme negatif etki yapar" ilkesine uyuyor, çünkü soru başı maliyet neredeyse sıfır.
- **Doğrulanabilirlik:** Alıntı yanıtı uydurma üretmez, her zaman sayfa numarası gösterir (tıbbi güvenlik). Claude ile soru dilinde özet yalnız `MEDPDF_ANTHROPIC_API_KEY` verilirse açılıyor; servis hata verirse alıntı yanıtına geri düşüyor (test edildi).

## Kaynak isteği → ürün davranışı
| Kaynak isteği | Ürün davranışı | Kanıt |
|---|---|---|
| "Kişiler PDF yükler, dayanışma olur" | Ortak kütüphane; aynı PDF ikinci kez eklenmez; katkı ücretsiz | test_shared_library…, ekran 1 |
| "Login/logout ile uğraştırmasın" | Hesap yok, anonim cihaz kimliği | E2E, ekranlarda giriş yok |
| "Mobil ve web uyumlu" | Duyarlı tasarım + PWA manifest/service worker | ekran 2–4 (iPhone 13) |
| "Sipanyol, Türk'ün PDF'inden cevap alsın" | Çok dilli anlamsal arama; soru dili / kaynak dili rozeti | test + ekran 2, 5 |
| "Limit sağ tarafta yazsın, bilsin" | Sağda sabit kota paneli; mobilde üstte sayaç; bedel düğmede ("Sor · 1 kredi") | ekran 1, 3, 5 |
| "İşin ortasında pat diye kesilmesin" | Uyarı seviyeleri; kredili başlayan konu "iş bitirme payı" ile sürer; kesinti yalnız yeni konuda ve açıklamalı | test_transparent_quota…, ekran 4 |
| "Herkese eşit ücretsiz hak, çok kullanan ödesin, kart dayatma" | Eşit aylık hak; gönüllü bağış → destek kodu ile kartsız ek kredi; az kullanan ödeme ekranı hiç görmez | test (redeem), ekran 3 |
| "Kişiye göre" | Ücretsiz / destek / iş bitirme payı ayrı ayrı gösteriliyor; kullanım geçmişi görünür | ekran 3–4 |


---
## Tur 2 (2026-10-04): Türkçe kaynak → İspanyolca cevap (anahtarsız)

Başlangıç commit'i `788ddaf` korundu. Bu turdaki tüm commit'ler onun üzerine eklendi.

```
EKSİK: alıntı yanıtı kaynak dilinde (TR) kalıyordu → İspanyolca soran kişi Türkçe metin okuyordu
  │
  ├─ B7 Cevap dili = soru dili ─── yerel MT (CTranslate2 int8)              → app/translate.py
  ├─ B8 Anlam koruma ─────────── sayı · birim · olumsuzluk · altında/üzerinde denetimi
  ├─ B9 "Cevap yok" ─────────── soru konu terimleri kaynak bölüm çevirisinde yoksa cevap verme
  └─ B10 Atıf ─────────────── her cevap cümlesi = tek kaynak cümle + sayfa + açılabilir orijinal
        │
        ▼
  YOL SEÇİMİ
   A2) Doğrulamalı yerel MT: OPUS-MT tc-bible-big (birincil) + opus-mt-tr-es (ikincil)   ← SEÇİLDİ
       • kaynak cümleler aynen seçilir (üretim yok) → çevrilir → denetimden geçmeyen aday reddedilir
       • "yüzde N" → "%N" normalizasyonu: ölçülen sayı hatasını giderdi
   B2) Anthropic API ─── en akıcı yol, ama anahtar/ödeme gerekir → isteğe bağlı kip olarak kaldı
   C2) Yerel LLM / Argos pivot / NLLB ─── uydurma riski, pivot hatası, ticari olmayan lisans → elendi
        │  A2 kırılırsa (model dosyası yoksa): kaynak dilinde alıntı + "mt_unavailable" uyarısı (test edildi)
        ▼
  UYGULAMA → TEST → TESLİM
   1 tests/test_multilingual_answer.py: gerçek model, bağımsız beklentiler          ✔
   2 tests/test_acceptance.py (arama, kota, güvenlik; yalnız arama adıyla ayrıldı)   ✔  toplam 23/23
   3 scripts/e2e.py: mobil ES cevap + "cevap yok" kartı + EN koyu tema                ✔
   4 Dockerfile: çok aşamalı (dönüştürme aşaması → yalnız ctranslate2 çalışma imajı)  (bkz. günlük)
   5 push + uzak commit/dosya hash geri okuma; ZIP/bundle + SHA-256
```

### Kabul: sentetik Türkçe PDF → İspanyolca soru → İspanyolca cevap
| Soru (ES) | Beklenen (testte, koddan bağımsız) | Sonuç |
|---|---|---|
| Infarto agudo: tratamiento inicial | "aspirina", s.1 | ✔ |
| HbA1c objetivo | "inferior/menos … 7 %", "seis/6 por ciento" OLMAYACAK, s.2 | ✔ "inferior al 7%" |
| Dosis de adrenalina | "0,5 mg", "5 minutos", "5 mg/0,05" OLMAYACAK, s.3 | ✔ |
| ¿Antibióticos eficaces en virales? | "no son efectivos/eficaces" (olumsuzluk), s.4 | ✔ |
| ¿Cuándo oxígeno en infarto? | "oxígeno", "inferior … 90 %", s.1 (aramada 3. sıradaki bölüm) | ✔ |
| Cetoacidosis / meningitis / crisis asmática | kaynakta yok → `no_answer`, cevap metni yok | ✔ |

Her cevap cümlesi için ayrıca şunlar denetlendi:
- Kaynak cümle, PDF'ten bağımsız okunan sayfa metninde birebir var.
- Sayılar korunmuş.
- Olumsuzluk korunmuş.
- Cevap metni İspanyolca algılanıyor.
- Cevapta atıf işareti var.
