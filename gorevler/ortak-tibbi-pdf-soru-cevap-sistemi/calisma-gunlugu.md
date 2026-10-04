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

---
# Tur 2 — 2026-10-04: Türkçe PDF'ten İspanyolca cevap

## İstenen
Kullanıcı `788ddaf` sürümünü kaynakla karşılaştırdı ve şu eksiği tespit etti: `extractive_answer` cevabı kaynak dilinde (Türkçe) döndürüyordu. Bu turun hedefleri:
- Anahtar olmadan çalışan, kaynağa dayalı İspanyolca cevap üretmek.
- Sayı, birim, olumsuzluk ve atıfların korunduğunu denetlemek.
- Kaynakta cevap yoksa bunu açıkça söylemek.
- Başlangıç commit'ini korumak; başka görev klasörlerine dokunmamak.

## Başlangıç durumu
- `git status` temiz; HEAD ve uzak dal `788ddaf`.
- Bulut kaynakları: 4 çekirdek, 15 GB RAM. GPU yok.

## Araştırma ve gerçek denemeler
- Ayrıntılar `arastirma.md` dosyasının 6. bölümünde. Üç model bu bulutta gerçekten indirildi, CTranslate2 int8 biçimine dönüştürüldü ve aynı 10 sentetik cümlede karşılaştırıldı.
- **tc-bible-big:** En doğal sonucu verdi, ama "yüzde 7'nin altıdır" → "seis por ciento" hatası yaptı.
- **M2M100:** Aynı sayı hatasını yaptı.
- **opus-tr-es:** "eGFR" → "electroencefalograma", "°C" → "oC" hataları yaptı ve "altında" anlamını düşürdü.
- **Sonuç:** Denetimsiz çeviri tıbbi açıdan güvenli değil.
- **Ölçülen çözüm:** Çeviriden önce "yüzde 7" ifadesi "%7" yapıldı; ardından 6 aday çeviri arasından ilk doğru olan ("inferior al 7%") seçildi.
- **Elenenler:** NLLB (ticari olmayan lisans, model kartında "tıp için değil" uyarısı), Argos/LibreTranslate (doğrudan tr→es paketi yok, İngilizce pivot zorunlu), yerel küçük LLM (uydurma riski, yavaş).

## Kararlar
- **Yol A2:** Kaynak cümleler aynen seçilir, metin üretilmez. Çeviri yerel ve doğrulamalıdır. Hiçbir aday denetimi geçmezse cümle cevaba konmaz, "doğrulanamadı" olarak orijinaliyle gösterilir.
- **"Cevap yok":** Sorunun konu terimleri (genel sözcükler hariç) kaynak bölümün çevirisinde aranır. En fazla ¼'ü eksik olabilir. Aramadaki ilk 3 bölüm sırayla denenir.
- **Modeller depoya girmez:** Çeviri modelleri 315 MB olduğu için depoya konmadı. `scripts/fetch_mt_models.sh` sabit Hugging Face sürümlerinden üretiyor. Bu sürümlerden Docker içinde ve yerelde üretilen `model.bin` SHA-256 değerleri birebir aynı:
  - bible-trk: `e210de21d3fc714fa9730973e8c8a1a3eeb0af53dea08533cb4a0a4f599aa11d`
  - opus-tr-es: `5139fa1dac2078e207fb36c440a9f6e72d83aa9922149d060b592dbecb18371c`

## Sorunlar ve çözümler
1. **Oksijen sorusu yanlışlıkla "cevap yok" döndü.** Aramada en üstte başka bir sayfa çıkıyordu. Çözüm: ilk 3 bölüm sırayla deneniyor; doğru cevap 3. sıradaki bölümden geldi.
2. **Yeniden sıralama HbA1c hedef cümlesini dışarıda bıraktı (gerileme).** Çözüm: soru sözcüklerine bonus puan verildi ve en iyi 3 cümle alınıyor.
3. **Her sayfadaki "SENTETİK TEST" bildirimi cevap cümlesi olarak seçilebiliyordu.** Çözüm: sayfaların yarısından fazlasında tekrarlanan üst ve alt bilgi satırları ayıklanıyor. Bu, gerçek PDF'lerdeki üst/alt bilgiler için de bir iyileştirme.
4. **Testte iki kendi hatam vardı.**
   - `\b5 mg\b` kalıbı "0,5 mg" içinde de eşleşiyordu.
   - Önceki test dosyası aylık kotayı 6'ya indirdiği için sorular 402 aldı.
   - Testler düzeltildi; ürün kodu değişmedi.
5. **Eski test cevabın Türkçe olmasını bekliyordu.** Adı "yalnız arama" olarak değiştirildi; cevap kabulü ayrı bir dosyaya taşındı. Böylece yalnız aramanın başarılı olması çok dilli cevap başarısı gibi sayılmıyor.

## Doğrulamalar
| Kontrol | Sonuç |
|---|---|
| `pytest -v tests/` | **23/23 geçti** (78 sn). Gerçek arama ve çeviri modelleri kullanıldı, sahte (mock) çeviri yok. |
| Çok dilli cevap kabulü | 5 cevaplı İspanyolca soru → doğru sayfaya dayalı İspanyolca cevap. 3 cevapsız soru → `no_answer`. |
| Bilinen hatalı çeviriler | Modellerin bu çalışmada ürettiği 6 hatalı çevirinin 6'sını da denetim reddetti. 3 doğru çeviriyi kabul etti. |
| Model yok durumu | `mt_unavailable` hatası döndü ve alıntı kaynak dilinde gösterildi (gizlenmedi). |
| Tarayıcı E2E | Mobil ES cevap, "cevap yok" kartı, EN koyu tema ("less than 7%"), kota akışı: **E2E OK**. Ekranlar gözle incelendi. |
| Docker | Çok aşamalı imaj derlendi (1,92 GB). `--network none` ile, yani internetsiz, ES cevap ve "cevap yok" çalıştı. |

## Gerçek dış bağımlılıklar (bu turda değişmedi)
- Fiziksel telefon testi
- Canlı alan adı ve HTTPS
- Bağış hesabı bağlantısı (`MEDPDF_DONATE_URL`)
- DMCA temsilcisi
- Kaynaktaki eksik özgün "medical" klasörü
- İsteğe bağlı Claude kipi için `MEDPDF_ANTHROPIC_API_KEY` (çok dilli cevap artık buna bağlı değil)

---
# Tur 3 — 2026-10-04: ebeveyn kabulündeki anlam bağı açığı

## İstenen
Ebeveyn, `9311085` sürümünü uzak blob hash'leriyle doğruladı. Ardından `check()` fonksiyonunu Windows Python 3.11'de çalıştırdı ve dört anlam bozucu çevirinin hepsinin `issues=[]` ile kabul edildiğini gördü:
1. 10 mg ↔ 5 mL değiş tokuşu
2. A ve B dozlarının yer değiştirmesi
3. Olumsuzluğun yanlış eyleme kayması
4. pt dilinde "abaixo" yerine "acima"

İstenenler:
- Önce paketleme dilimini kalıcı olarak bitirmek.
- Sonra bu açığı aynı dalda kapatmak.
- Bağ kanıtlanamıyorsa çeviriyi göstermemek, Türkçe orijinali gerekçesiyle sunmak.
- Desteklenmeyen dili başarılı saymamak.
- "Doğrulandı" izlenimi veren ibareleri düzeltmek.

## Yapılanlar
1. **Paketleme dilimi kapatıldı.** `TESLIM.md` içine v2 hash'leri yazıldı ve push edildi (`7e4af7a`). Uzak commit ve dosya blob'u yerelle aynı.
2. **Açık yeniden üretildi.** Dört örneğin dördü de eski `check()` ile `[]` döndü; kontrol örneği (10 mg → 5 mg) ise reddedildi. Ebeveynin bulgusu doğrulandı.
3. **Araştırma** (`arastirma.md` 7. bölüm): hizalama araçları (SimAlign, awesome-align), CTranslate2 dikkat çıktısı, QE modelleri (CometKiwi/xCOMET, ticari olmayan lisans), yer tutucu yöntemi, Zemberek ve geri çeviri incelendi. Hiçbiri Türkçe için doğrulanmış ve ucuz bir kanıt sunmadığından temkinli kural tabanlı tasarım seçildi.
4. **`check()` v2** yazıldı. Kurallar `harita.md` Tur 3 bölümünde. Sorunlar artık kodlu (`quantity`, `negation_scope` gibi) ve arayüz gerekçeyi kullanıcının dilinde gösteriyor.
5. **Ek açık bulundu ve kapatıldı.** Farklı birimli iki nicelikte özneler yer değiştirince ("A 10 mg, B 5 mL" → "10 mg de B y 5 mL de A") çeviri hâlâ geçiyordu. Çeviride birebir geçen varlık sözcükleri üzerinden bölüm-bağı denetimi eklendi.
6. **Kendi hatalarım:**
   - Ayırıcı ondalık virgülü bölüyordu ("6,5" → "6" + "5").
   - "dL" birimi varlık sözcüğü sayılıyordu.
   - Bu yüzden doğru glukoz/HbA1c çevirisi gereksiz yere reddediliyordu. İkisi de düzeltildi.
7. **Raporlama tutarsızlığı:** Ret gerekçesi ikincil modelin adayından, gösterilen metin birincil modelden geliyordu. Gerekçe artık gösterilen adayla tutarlı.
8. **Çeviri birimleri** cümlenin ";" ve ":" noktalarından bölündü (`search._units`). Önce bu bölme `_sentences` içine konmuştu ve arama vurgusu testi bozuldu; bölme yalnız çeviri tarafına taşındı.
9. **Etiketler düzeltildi.** "✓ doğrulandı" kaldırıldı. API'de `check_level: "surface"` ve uygulanan denetimler listeleniyor. Arayüzde "yüzeysel denetimden geçti; tam anlam garanti edilmez — orijinalle karşılaştırın" yazıyor.

## Doğrulama
- **`pytest -v tests/`: 49/49 geçti.** Gerçek modeller kullanıldı.
  - Denetimin reddetmesi gereken 20 çeviriden 20'si reddedildi.
  - Doğru 10 çeviriden 10'u kabul edildi.
  - Desteklenmeyen dil (it) "unsupported" olarak reddedildi.
  - Gerçek modellerle es/pt/de regresyonu: 5 tekli doğru cümle beklenen olgularla kabul edildi ("500 mg", "no … aspirina", "abaixo de 7", "über 140 mmHg" vb.). A/B çoklu doz ve karışık olumsuzluk cümleleri, model doğru çevirse bile reddedildi.
  - Cevap düzeyinde: kanıtlanamayan iki cümle cevaba girmedi, orijinalleri gerekçe kodlarıyla döndü.
- **E2E OK.** Mobil ekranda "…uygun değilse trombolitik tedavi verilir" yan cümlesi çevrilmeden, "alcance de la negación dudoso" gerekçesiyle gösteriliyor. "✓" ibaresi yok.

## Kalan sınırlar
- **Özne/çatı kayması yakalanmıyor.** Örnek: "el infarto con elevación de ST proporciona la reperfusión".
- **Terim kayması yakalanmıyor.** Örnek: "hambre" (doğrusu "ayuno").
- **Yüklem tespiti düzenli ifadeyle yapılıyor.** Zemberek ile doğrulama ve dikkat tabanlı hizalama sonraki adaylar.
