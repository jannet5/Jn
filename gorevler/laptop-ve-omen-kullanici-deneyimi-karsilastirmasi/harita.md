# Harita — Laptop ve Omen kullanıcı deneyimi karşılaştırması

Tarih: 2026-10-03 · Çalışma dalı: `claude/lucid-hopper-h714gc` · Klasör: `gorevler/laptop-ve-omen-kullanici-deneyimi-karsilastirmasi/`

## 1. Hedef
Grafik tasarım + yazılım geliştirme + oyun için, "abartılı olmayan, makul fiyatlı ama özellikleri iyi" bir laptop önermek;
HP OMEN'i gerçek kullanıcı deneyimi (Ekşi Sözlük, Reddit, şikâyet siteleri, HP forumu) ve editoryal/resmî kaynaklarla
alternatiflerine karşı sınamak; sonunda "bu kadar bilgisayar arasında neden bu?" sorusuna kanıtlı cevap vermek ve
kullanıcının istediği **çok sade "bilgisayar – fiyat" listesini** HTTPS bağlantılarıyla teslim etmek.

## 2. Bağımlılıklar (sırayla)
| # | Adım | Girdi | Çıktı | Kabul ölçütü |
|---|------|-------|-------|--------------|
| D1 | İhtiyaç profili | Kaynak metin (özel alanda) | `rapor.md` §1 ihtiyaç tablosu | RAM, depolama, GPU, ekran (tasarım için renk), fiyat birlikte tanımlı |
| D2 | Omen topluluk araştırması | Ekşi, Reddit, Şikayetvar, HP Community | `rapor.md` §3 | Ekşi ve Reddit'ten ayrı ayrı somut deneyim özetleri + URL; açılamayan sayfa ayrı işaretli |
| D3 | Editoryal/resmî doğrulama | Notebookcheck, RTINGS, üretici spec | `rapor.md` §2–§4 | Her teknik iddia en az bir resmî/editoryal URL'ye bağlı |
| D4 | Alternatiflerin güvenilirliği | Aynı tür kaynaklar | `rapor.md` §4 | En az 4 alternatif model için kronik sorun + olumlu deneyim |
| D5 | Güncel Türkiye fiyatları | akakce/hepsiburada/üretici mağazaları | `liste.md`, `rapor.md` §5 | Her fiyatın satıcısı, URL'si, tarihi ve doğrulama durumu var; uydurma fiyat yok |
| D6 | Karar | D1–D5 | `rapor.md` §6 + `liste.md` | Puanlama tablosu + "neden bu" gerekçesi + "ne zaman alma" uyarıları |
| D7 | Test | Tüm teslim dosyaları | `test/dogrula.py` çıktısı | Tüm bağlantılar HTTPS, zorunlu bölümler mevcut, özel kaynak metni sızmamış, bağlantı erişim raporu üretildi |
| D8 | Kalıcı teslim | D1–D7 | Uzak dala push + geri okuma; özel ZIP + SHA-256 | Uzak daldaki dosya hash'leri yerel ile aynı; ZIP açılıp hash'ler doğrulandı |

## 3. A / B / C yolları
- **A (seçilen):** Web araştırması (WebSearch + WebFetch) → Markdown rapor + sade liste + Python doğrulama betiği → git push → özel ZIP.
  Gerekçe: İş bir araştırma işi; kaynakta istenen ürün "sade liste + linkler", uygulama değil. Markdown GitHub'da da, telefonda da okunur.
- **B (yedek, kaynak açılamazsa):** Ekşi/Reddit doğrudan açılamazsa (bot koruması/403) arama motoru özetleri + arşiv
  (web.archive.org) + aynı konuyu aktaran HP Community / Şikayetvar sayfaları; bu durumda rapor bu kısıtı açıkça yazar.
- **C (yedek, fiyat sayfaları açılamazsa):** Fiyat karşılaştırma sitelerinin arama özeti fiyatları "doğrulanmadı" etiketiyle,
  üretici resmî mağaza fiyatı ile çapraz kontrol; push başarısız olursa yalnızca özel ZIP/bundle + SHA-256 ile teslim.

## 4. Uygulama → test → teslim zinciri
```
kaynak (özel) ─▶ ihtiyaç profili ─▶ [Omen topluluk] ─┐
                                   [editoryal/resmî] ─┼─▶ karşılaştırma + puan ─▶ rapor.md / liste.md
                                   [alternatifler]  ─┤
                                   [TR fiyatları]   ─┘
rapor.md + liste.md ─▶ test/dogrula.py ─▶ git commit/push ─▶ uzak dalı geri okuma (hash)
                                                     └────▶ özel ZIP + SHA256SUMS ─▶ açıp yeniden hash
```
Yol kırılırsa: ilgili adım B/C'ye düşer, `calisma-gunlugu.md`'ye neden ve alternatif yazılır.

## 5. Kapsam dışı / yapılmayanlar
- Fiziksel cihaz testi, Windows üzerinde benchmark, mağazadan satın alma veya hesap işlemi yapılmaz (bulut ortamı).
- Fiyatlar anlıktır; kampanya ve stok değişir — liste gözlem tarihini taşır.
