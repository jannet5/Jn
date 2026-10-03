# Yapay Zekâ, Gelecekte İş ve Hayat — Araştırma Paketi

3 Ekim 2026 tarihli, Türkçe, kaynaklı bir araştırma. Bu klasör depodaki diğer
projelerden bağımsızdır.

## Nereden başlamalı?

1. **[`arastirma.md`](arastirma.md)** — Kitap gibi okunacak ana metin (önsöz, 6 bölüm, sonsöz).
2. [`meslek-risk-tablosu.md`](meslek-risk-tablosu.md) — Yazılım ve fiziksel meslekler, ayrı risk puanlarıyla.
3. [`kanit-matrisi.md`](kanit-matrisi.md) — 38 iddia: kanıt mı, erken sinyal mi, tahmin mi, topluluk görüşü mü?
4. [`kaynaklar.md`](kaynaklar.md) — Tüm tam HTTPS bağlantılar ve her birinin nasıl okunduğu.
5. [`youtube-video-listesi.md`](youtube-video-listesi.md) — 16 video ve transkript engelinin dürüst kaydı.
6. [`harita.md`](harita.md) — Hedef → bağımlılık → A/B/C yolları → test → teslim zinciri.
7. [`calisma-gunlugu.md`](calisma-gunlugu.md) — Adım adım ne yapıldı, hangi komutlar, ne kırıldı.

## Tek paragrafta sonuç

Yapay zekâ dört yılda "herkesi işsiz bırakmadı"; genel istihdamda büyük bir bozulma
ölçülmedi. Ama işlerin içeriği değişiyor ve en çok **basamağın en altı** sarsılıyor:
yapay zekâya açık mesleklerde 22–25 yaş istihdamı beklenenin %19 altında. Fiziksel,
teşhis gerektiren, belge ve güvene dayalı işler (elektrik, tesisat, iklimlendirme,
enerji) önümüzdeki on yılda görece dayanıklı ve talep görüyor; ama "herkes ustalığa
koşarsa" arz artışı ve "ustanın müşterisi yoksullaşırsa" talep düşüşü riski var. En
güçlü konum, fiziksel beceriyi yapay zekâ kullanımıyla birleştiren hibrit yollar.

## Doğrulama

```bash
python3 dogrulama/kontrol.py
```

Kaynak bağlantılarının biçimini, kanıt matrisinin tutarlılığını, risk tablosunun
dayanaklarını, video sayısını ve özel içeriğin depoya sızmadığını kontrol eder.

## Bilinen eksikler

- YouTube transkriptleri bu bulut ortamından alınamadı (IP engeli). Ev bağlantısında
  `python3 dogrulama/transkript_al.py` ile tamamlanabilir.
- Instagram ve X doğrudan okunamadı (giriş gerekli); X tartışması aktaran haber üzerinden incelendi.
- Reddit, Ekşi Sözlük ve Technopat 403 döndürdü.
- Türkiye için yapay zekâ-istihdam ilişkisini ölçen bordro düzeyinde veri bulunamadı.
