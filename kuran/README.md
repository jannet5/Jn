# Kur'an Okuyucu

Çok hafif, hızlı, kütüphanesiz Kur'an okuyucu. Telefonda ana ekrana eklenir, ilk açılıştan sonra internetsiz çalışır.

## Özellikler
- Medine Mushafı: 604 sayfa, 30 cüz, 114 sure. Sayfalar sağdan sola çevrilir (parmağı soldan sağa kaydır = sonraki sayfa).
- Yakınlaştırma yazıyı büyütür ve satırları yeniden dizer, yana kaydırma gerekmez. `A−` / `A+` düğmeleri ya da iki parmak.
- **Git** paneli: Sure | Cüz | Sayfa yan yana. Aynı anda yalnızca biri geçerli. "Ma" yazınca Mâide... sıralanır, listeden seçince hemen gider. Sure numarası adın yanında yazar.
- Açılışta ayar sormaz: kaldığın sayfadan ve kaydırma yerinden devam eder.
- Yer imi, Gündüz / Sepya / Gece teması, ayet sonu işaretleri.

## Kur'an metni bozulmaz
- Metin Tanzil Uthmani kaynağındandır. `tools/build_data.py` yalnızca iki şey yapar: BOM karakterini atar ve kaynakta her surenin ilk ayetine eklenmiş besmele önekini ayıklar (besmele surenin başında ayrı gösterilir).
- Sayfa ve cüz sınırları iki bağımsız kaynakla (Tanzil metadata + alquran.cloud) birebir doğrulanır; uyuşmazlıkta derleme durur.
- İkinci bağımsız kaynakla (quran.com, KFGQPC) harf harf karşılaştırıldı: 6236 ayetin 6234'ünde harf iskeleti aynı. Fark yalnızca Yûsuf 12:39 ve 12:41'de: Tanzil "صَىٰحِبَىِ", KFGQPC "صَـٰحِبَىِ" yazıyor. Aynı harfin iki kodlanışı, ekranda aynı görünür. Metne dokunulmadı.
- `tools/verify_render.mjs`: 604 sayfayı tarayıcıda çizer, ekrandaki metnin veriyle birebir aynı olduğunu ve ayetlerin sırayla eksiksiz geldiğini doğrular.
- Tüm ayetlerin SHA-256 özeti `tools/CHECKSUM.txt` içindedir.
- Yazı tipi Amiri Quran (OFL), kullanılan tüm 69 karakteri ve işareti içerir. CSS'te harf aralığı, dönüşüm ve kelime bölme yoktur.

## Çalıştırma
```
cd www && python3 -m http.server 8765    # sonra http://localhost:8765
```
Telefonda: tarayıcıda aç, "Ana ekrana ekle".

## Veriyi yeniden üretme
```
python3 tools/build_data.py <ham_klasor> www/data.js
```
`<ham_klasor>`: `cloud-uthmani.json` (api.alquran.cloud/v1/quran/quran-uthmani) ve `tanzil-meta.js` (tanzil.net/res/text/metadata/quran-data.js).

## Kaynaklar ve lisanslar
- Metin: [Tanzil](https://tanzil.net) Uthmani (değiştirilmeden kullanılmalıdır). Meta veri CC BY 3.0.
- Yazı tipi: Amiri Quran, SIL Open Font License 1.1 (`www/fonts/OFL-AmiriQuran.txt`).
