# Tasarım planı — Kafe zinciri haftalık satış paneli

Süreç: frontend-design skill'i (plan → brife karşı gözden geçirme → kod → ekran görüntüsüyle eleştiri).

## 0. Konu, kitle, birincil iş

- **Konu:** Bir kafe zincirinin (örnek ad: *Fincan & Fırın*, İstanbul'da 4 şube) haftalık satış paneli.
- **Kitle:** Bölge müdürü / işletme sahibi. Sabah ilk kahvesini içerken telefondan veya ofiste laptoptan bakar.
- **Birincil iş:** "Bu hafta nasıl gidiyor, hangi gün iyi/kötüydü, şu an siparişlerde sorun var mı?" sorusuna 10 saniyede cevap.
- Kafe dünyasının malzemesi: kavrulmuş çekirdek tonları (yeşil çekirdekten koyu kavruma), fıstık yeşili, İznik çinisinin kobalt mavisi, menü tahtası tabelaları, fiş/adisyon.

## 1. İlk taslak (gözden geçirmeden önce)

- Renk: krem zemin #F4F1EA, kahve kahverengisi #4B2E22, terrakota vurgu #D97757, koyu metin #111111.
- Yazı: Fraunces (başlıklar, serif), Inter (gövde), IBM Plex Mono (küçük veri etiketleri).
- Yerleşim: sol menü + üstte 4 eş yuvarlatılmış özet kartı (büyük sayı, küçük etiket, yeşil/kırmızı rozet) + altında grafik kartı + tablo kartı.

```
┌──────┬───────────────────────────────────────┐
│ Menü │  Başlık                  [tarih ▾]    │
│      │ ┌─────┐┌─────┐┌─────┐┌─────┐          │
│      │ │ KPI ││ KPI ││ KPI ││ KPI │          │
│      │ └─────┘└─────┘└─────┘└─────┘          │
│      │ ┌───────────────────────────────┐     │
│      │ │ grafik                        │     │
│      │ └───────────────────────────────┘     │
│      │ ┌───────────────────────────────┐     │
│      │ │ tablo                         │     │
└──────┴───────────────────────────────────────┘
```

## 2. Brife karşı gözden geçirme — ne değişti, neden

Aynı istemi "herhangi bir satış paneli" için düşündüğümde neredeyse aynı yere vardım; yani taslak bu brife özgü değil.

| Taslak | Sorun | Revizyon |
|---|---|---|
| Krem #F4F1EA + terrakota #D97757 + serif başlık | Skill'in birinci "üretilmiş tasarım" kalıbı; "kafe = sıcak krem" ilk akla gelen. | Zemin **fıstık yeşili-gri**, vurgu **İznik kobaltı**. Kahve tonu yalnızca veride (grafik çubukları) yaşar: kahve *ölçülen şeydir*, dekor değil. |
| #111 metin | "Tonlanmış siyah" kalıbı. | Metin derin şişe yeşili **#1F2B24** — zeminle aynı aileden, siyah taklidi değil. |
| Fraunces + Inter + Plex Mono | Varsayılan aileler; mono veri etiketi kalıbı. | **Unbounded** (geniş, yuvarlak köşeli, kafe tabelası/menü tahtası karakteri) yalnızca sayılarda ve sayfa başlığında; **Onest** tüm arayüz metninde. Mono yok, rakamlar `tabular-nums`. |
| 4 eş KPI kartı, aynı radius, aynı gölge | SaaS kart kiti. | Dört ölçüt **tek bir "adisyon şeridi"**nde: tek yüzey, aralarında dikey ince çizgiler. Kart değil, bir fişin satırları gibi. |
| Grafik orta boy bir kart | Panelin karakteri yok. | **Cesaret tek yerde: grafik.** Günlük ciro, haftanın en geniş öğesi; çubuklar kavurma derecesi skalasıyla dolar (o gün ne kadar "dolu" geçti → o kadar koyu kavrum). Gün adları Unbounded ile büyük. Klavyeyle her çubuğa odaklanılır, okuma satırı o günü söyler. |
| Durum rozeti hapları (renkli pill) | Kart kitinin uzantısı. | Durum = küçük şekil + düz metin (● teslim, ◐ hazırlanıyor, ✕ iade). Renk tek başına bilgi taşımaz. |
| "GENEL BAKIŞ" gibi büyük harfli etiketler, "A · B · C" meta | Şablon süslemesi. | Cümle düzeni ("Genel bakış"), meta virgülle veya ayrı satırda. Ok (→) yok. |

## 3. Revize token sistemi

### Renk (6 ad)

| Ad | Hex | Rol |
|---|---|---|
| Fıstık | `#E3E9DC` | Sayfa zemini |
| Köpük | `#F6F8F1` | Yüzeyler (adisyon şeridi, grafik, tablo) |
| Şişe | `#1F2B24` | Metin, sol menü zemini |
| Çini | `#2448A8` | Etkileşim: seçili, odak halkası, bağlantı |
| Kavrum | `#6B3F22` | Grafik çubukları (açık→koyu skala: `#C9A27A` → `#6B3F22` → `#3E2414`) |
| Vişne | `#A3243A` | Olumsuz değişim, iade |

Olumlu değişim: Şişe tonunda koyu yeşil `#2E6B3E`. Tüm metin/zemin çiftleri ≥ 4.5:1 hedefi.

### Yazı

- **Unbounded** 500–700: sayfa başlığı ("Bu hafta"), ölçüt değerleri, grafik gün adları ve en yüksek gün değeri. Tip başlığın kendisi tasarım öğesi: geniş, sıkı izli (-0.02em).
- **Onest** 400/500/600: menü, etiketler, tablo, açıklamalar.
- Ölçek (Bringhurst'ün klasik dizisine yakın): 12 · 14 · 16 · 21 · 28 · 42 px. Gövde 16/1.5, satır < 80 karakter.

### Yerleşim

Masaüstü: koyu şişe yeşili dar sol menü; sağda içerik sola hizalı, tek kolon, en fazla ~1180 px. Başlık satırında sayfa adı + tarih aralığı seçici (radyo grubu, segment görünümlü). Altında adisyon şeridi; sonra geniş grafik; en altta son siparişler.

```
┌────────┬──────────────────────────────────────────────┐
│ Fincan │ Bu hafta              (Bu hafta|Geçen|30 gün)│
│ & Fırın│ 28 Eyl – 4 Eki, 4 şube                       │
│        │ ┌──────────┬──────────┬──────────┬─────────┐ │
│ Genel  │ │ Ciro     │ Sipariş  │ Ort.sepet│ İade    │ │  ← tek şerit
│ Şubeler│ │ ₺412.380 │ 2.914    │ ₺141,5   │ %1,8    │ │
│ Ürünler│ │ +8,2%    │ +5,1%    │ +2,9%    │ −0,4 pt │ │
│ Personel └──────────┴──────────┴──────────┴─────────┘ │
│ Ayarlar│ Günlük ciro                okuma: Cmt ₺78.400│
│        │   ▇                                          │
│        │ ▅ █ ▆ ▇ ▆ █ ▅    ← kavrum skalası, büyük     │
│        │ Pzt Sal Çar Per Cum Cmt Paz                  │
│        │ Son siparişler                               │
│        │ saat | şube | ürünler | tutar | durum        │
└────────┴──────────────────────────────────────────────┘
```

Mobil (390 px): menü üst çubuğa iner, "Menü" düğmesiyle açılan liste. Tarih seçici başlığın altında tam genişlik. Adisyon şeridi 2×2. Grafik tam genişlik, gün adları kısaltılmış. Tablo satırları iki satırlık liste öğelerine dönüşür (saat+şube / ürünler / tutar+durum).

```
┌──────────────────────────┐
│ Fincan & Fırın   [Menü]  │
│ Bu hafta                 │
│ (Bu hafta|Geçen|30 gün)  │
│ ┌──────────┬───────────┐ │
│ │ Ciro     │ Sipariş   │ │
│ ├──────────┼───────────┤ │
│ │ Ort.sepet│ İade      │ │
│ └──────────┴───────────┘ │
│ ▅ █ ▆ ▇ ▆ █ ▅            │
│ 08:42 Moda      ₺186,00  │
│ 2 latte, simit  ● Teslim │
└──────────────────────────┘
```

Hizalama: her şey sola hizalı; yalnızca sayısal tablo sütunu (tutar) sağa hizalı.

### İlkeler

1. **Kahve veridir, dekor değildir.** Kahverengi yalnızca ciroyu gösteren çubuklarda.
2. **Cesaret tek yerde:** grafik. Geri kalan her şey sessiz, disiplinli.
3. **Fiş mantığı:** ölçütler kart değil, tek adisyonun satırları.
4. **Klavye birinci sınıf:** atla bağlantısı, görünür çini odak halkası, radyo grubu ok tuşlarıyla, grafik çubukları sekmeyle okunur.
5. **Tek hareket anı:** ilk yüklemede çubuklar bir kez soldan sağa dolar; aralık değişince yeni yükseklike geçiş (kullanıcı eylemine cevap). `prefers-reduced-motion` ile kapanır.
6. **Metin işe yarar:** sade fiiller, cümle düzeni; boş durumda ne yapılacağını söyler.
