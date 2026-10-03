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
