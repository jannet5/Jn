# Çalışma günlüğü — Ortak tıbbi PDF soru-cevap sistemi

**Tarih:** 2026-10-03
**Ortam:** Claude Code bulut konteyneri (Linux), Python 3.11, Node 22
**Dal:** `claude/bold-hawking-5ed6gv`

## 1. Ne istendi
Kaynak sohbetin tamamı okundu: 25 satır, son kullanıcı düzeltmeleri satır 21–23'te. İstenen ürün şu özellikleri taşıyor:
- Tıp öğrencileri PDF yükler ve bu PDF'ler herkesin kullandığı ortak bir kütüphanede toplanır (dayanışma).
- Giriş/çıkış yapma zorunluluğu yoktur.
- Mobil ve web uyumludur, dünyaya açıktır: İspanyol bir kullanıcı, Türk'ün yüklediği PDF'ten yanıt alabilir.
- Ücretlendirme bağışa dayanır.
- Limit şeffaftır: sağ tarafta görünür ve işin ortasında aniden kesmez.
- Herkese eşit ücretsiz hak verilir; çok kullanan öder, ama az kullanana kart dayatılmaz. Sistem kişiye göre davranır.

Ek olarak:
- `gorev.md` dosyasındaki üç kabul maddesi hedef alındı: ortak kütüphane, Türkçe PDF ↔ İspanyolca soru, şeffaf ve kesintisiz kota.
- `gorev.md`, kaynakta adı geçen "medical" klasörünün bulunamadığını söylüyor. O klasördeki belgeler elimde olmadığı için içerikleri uydurulmadı; ürün yalnız kaynak sohbete göre kuruldu.

## 2. Gizlilik
- `kaynak.txt` ve görev metni yalnız bulut çalışma alanında, depo dışında tutuldu (`/tmp/claude-0/ozel-kaynak/`).
- Public depoya kaynak sohbet metni, kullanıcı yolu ya da özel bağlam push edilmedi.

## 3. Araştırma
- Bir alt ajan web araması yaptı. Okunan ve yalnız aramada görülen kaynaklar ayrı ayrı işaretlendi; ayrıntılar [arastirma.md](arastirma.md) dosyasında.
- **Bulgu:** Benzer araçların tamamı (ChatPDF, NotebookLM, Humata, AMBOSS AI vb.) kişisel kütüphane mantığıyla çalışıyor ve hesap istiyor. Girişsiz, ortak ve çok dilli bir kütüphaneye rastlanmadı.
- **Topluluk tarafı:** HN'de "limit işin ortasında kesti" şikâyetleri var. Bu, iş bitirme payı kararını destekledi.
- **Not:** Reddit, arama aracında engelli olduğu için okunamadı.

## 4. Kararlar
Ayrıntı [harita.md](harita.md) dosyasında.

| Karar | Gerekçe |
|---|---|
| **A yolu:** yerel çok dilli gömme modeli + alıntı yanıtı, isteğe bağlı Claude özeti | Anahtar ve ödeme gerektirmiyor; uydurmaz, her yanıtta sayfa kaynağı var. |
| **Model:** `paraphrase-multilingual-MiniLM-L12-v2` (fastembed, ONNX) | Resmî listede var, ~240 MB, önek gerektirmiyor. e5-small listede yok, e5-large ise ~2 GB. |
| **Sunucu ve arayüz:** FastAPI + SQLite + tek sayfa PWA | Tek süreç, tek dosyalık veritabanı; telefona ana ekrandan kurulabiliyor. |
| **Kota:** aylık eşit hak → destek kredisi → iş bitirme payı | Kaynak satır 21–23'e doğrudan karşılık geliyor. |
| **Ödeme:** kart yerine bağış bağlantısı + yönetici destek kodu | "Kart koymak bunaltıcı" isteğine uyuyor. Stripe müşteri nesnesi ve kart gerektirdiği için ileriye bırakıldı. |

## 5. Kullanılan gerçek komutlar
```bash
pip install fastapi uvicorn python-multipart pymupdf fastembed numpy pytest httpx playwright
# model indirme ve diller arası deneme: ES soru ↔ TR cümle benzerliği 0.44, ilgisiz cümle 0.16
python3 scripts/make_sample_pdfs.py samples
pytest -v tests/
MEDPDF_MONTHLY_FREE=8 uvicorn app.main:app --port 8765 &
CHROMIUM_PATH=/opt/pw-browsers/chromium python3 scripts/e2e.py http://127.0.0.1:8765 samples shots/
dockerd & ; docker build --secret id=ca,src=/root/.ccr/ca-bundle.crt -t medortak:test .
docker run -p 8799:8000 medortak:test   # sağlık, yükleme, İspanyolca soru curl ile denendi
```

## 6. Sorunlar ve çözümler
1. **PDF satır kaydırmaları cümleleri bölüyordu.** Doğru sayfa bulunuyor ama alıntı yarım cümle çıkıyordu. Satırlar birleştirildi, cümleler büyük harf sınırına göre ayrıldı.
2. **Diyabet sorusunda hedef cümle seçilmiyordu.** "%7'nin altı" cümlesi anlamsal sırada 3. kalıyordu. Diller arası ortak terim bonusu eklendi (HbA1c, ilaç adları, sayılar) ve alıntı en iyi 3 cümleye çıkarıldı (belge sırasıyla). Test artık "7'nin altı" ve "0,5 mg" ifadelerini birebir arıyor.
3. **Playwright, sürümüne uygun tarayıcıyı bulamadı.** Önceden kurulu `/opt/pw-browsers/chromium` kullanıldı (`CHROMIUM_PATH`); yeni tarayıcı indirilmedi.
4. **Görsel kontrolde üç hata çıktı:**
   - `hidden` öğeler CSS yüzünden görünüyordu. Bu yüzden anahtar yokken YZ kutusu, URL yokken de bağış düğmesi ekrana geliyordu.
   - Dil seçici başlığı boydan boya kaplıyordu.
   - Mobilde başlık taşıyordu.

   Üçü de düzeltildi. E2E'ye yatay taşma, dil seçicinin ekran içinde kalması ve gizli öğelerin gizli kalması için kontroller eklendi.
5. **E2E'de kota eşiği beklentisi yanlıştı.** 8 krediden 1'i kaldığında oran %12,5 olduğu için seviye "düşük" değil "uyarı" olmalıydı. Kod değil, test beklentisi düzeltildi.
6. **YZ geri düşüş testi tek başına çalışınca kütüphane boş kalıyordu.** Test kendi PDF'ini yükleyecek şekilde değiştirildi. Sorulan soru belirsiz olduğu için, alıntıda belirli bir kelime aranmıyor; kaynak sayfası (s.3) doğrulanıyor.
7. **Docker derlemesinde pip, vekil sunucunun sertifikasını reddetti.** Dockerfile'a isteğe bağlı BuildKit CA secret'ı eklendi; secret verilmediğinde etkisi yok.

## 7. Doğrulamalar
| Kontrol | Sonuç |
|---|---|
| `pytest -v tests/` | **5/5 geçti** (gerçek PDF + gerçek model + HTTP API) |
| Ortak kütüphane | A cihazı yüklüyor, B cihazı listeyi görüp soru soruyor; aynı PDF ikinci kez eklenmiyor |
| ES → TR | 3 İspanyolca soru doğru Türkçe sayfayı buldu (skor 0,64 / 0,71 / 0,72). Alıntılarda "aspirin", "7'nin altı" ve "0,5 mg" geçiyor. TR soru, EN PDF'i de buldu. |
| Kota | Uyarı seviyesi kredi bitmeden geliyor. Kredili başlayan konu payla sürüyor; yeni konu 402 ve açıklamayla duruyor. Destek kodu bir kez kullanılabiliyor. |
| Güvenlik | Hak beyanı zorunlu, PDF olmayan dosya reddediliyor, yönetici uçları 403, cihaz kimliği olmadan 401, IP başına cihaz sınırı 429. Telif bildirimi belgeyi gizliyor. |
| E2E tarayıcı | Masaüstü TR, iPhone 13 ES, masaüstü EN koyu tema: **E2E OK**. 5 ekran görüntüsü gözle incelendi. |
| Docker | İmaj derlendi (1,12 GB, model gömülü). Konteynerde sağlık kontrolü, yükleme ve İspanyolca soru çalıştı (s.3, "0,5 mg"). |

## 8. Yapılamayanlar ve engeller
- **Claude ile soru dilinde özet:** `MEDPDF_ANTHROPIC_API_KEY` yok, bu yüzden gerçek API çağrısı test edilmedi. Yalnız hata durumunda alıntıya geri düşüş test edildi.
- **Gerçek telefon ve Windows testi yapılmadı.** Mobil davranış Chromium'un iPhone 13 öykünmesiyle doğrulandı.
- **Canlı yayın yapılmadı:** alan adı, HTTPS ve sunucu hesabı yok. PWA kurulumu HTTPS gerektiriyor.
- **Bağış bağlantısı ve DMCA temsilcisi:** Gerçek hesap ve kişi bilgisi gerektiriyor, uydurulmadı. Bağış bağlantısı `MEDPDF_DONATE_URL` ile girilecek.
- **Kaynaktaki "medical" klasörü** yüklenmedi; içeriği bilinmiyor.
