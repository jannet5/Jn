# Nota çıkarma v2 (f0 -> nota listesi)

Kod: `arastirma/ortak/nota_cikar_v2.py` (v1: `nota_cikar.py`, dokunulmadı). Ayar aleti: `arastirma/ortak/nota_v2_tara.py`,
önbellek: `arastirma/ortak/f0_onbellek_v2.py` (RMVPE çıktısını 70 klip için `/home/user/cache/f0_v2/*.npz` yazar; depoda yok).
Ham sayılar: `arastirma/sonuc/nota_v2_sonuc.json`.

İmza: `nota_cikar(t, f0, salience=None, enerji=None, **ayar) -> (intervals Nx2 sn, pitches_hz N, midi N)`.
- `f0`: RMVPE çıktısı, **salience-max > 0.4** olan karelerde Hz, diğerlerinde 0 (v1'de eşik 0.2 idi).
- `salience`: kare başına RMVPE salience-max (360 sınıfın en büyüğü, 0..1). `enerji`: kare başına RMS (16 kHz, pencere 1024, adım 160, merkezli). İkisi de `None` olabilir; o zaman ilgili adımlar atlanır.

## Nihai parametreler
| ad | değer | anlam |
|---|---|---|
| esik (f0 çıkarımında) | 0.4 | salience-max bunun altındaysa f0=0 |
| lam | 12 | Viterbi'de perde değiştirme cezası (v1: 8) |
| cap | 5 | kare maliyeti üst sınırı, yarım ton² (v1: 3) |
| gap_fill | 0.02 sn | iki yanı 1.5 yt'den yakın kısa sessiz boşlukları doldur (v1: 0.06) |
| min_dur | 0.06 sn | bundan kısa notayı komşuya kat / at (v1: 0.12) |
| kat_tol | 1 yt | katılacak komşu en çok 1 yt farklı olabilir (v1: 2) |
| merge_gap | 0.06 sn | (etkisiz: birleştirme yok, bkz. adım 5) |
| onceden_akort, akort_guc | açık, 0.5 | akort kayması Viterbi'den ÖNCE bulunur, yarı gücüyle uygulanır |
| durum_medyan | açık | kat sonrası nota perdesi = kare medyanı (akortsuz) yuvarlaması |
| bol_enj, bol_pen, bol_min | 6 dB, 0.08 sn, 0.08 sn | enerji çukuru >= 6 dB ise notayı böl (aynı perde, sert sınır) |
| bol_sal | 0 (kapalı) | salience çukuru ile bölme: karışımda zarar verdi |
| guven_esik | 0.5 | notanın ortalama salience'i < 0.5 ise at |
| gama_sure | 0 (kapalı) | ton bulma + gama çekme: zarar verdi (aşağıda) |
| oktav_sure | 0 (kapalı) | oktav sıçraması düzeltme: etkisiz |
| agirlik | 0 (kapalı) | salience ağırlıklı Viterbi: zarar |
| kayma | +0.01 sn | tüm nota başlangıç/bitişlerini 10 ms ileri al |

## Nota F (mir_eval, onset_tolerance=0.05, perde toleransı 50 cent, offset yok)
Ölçüm: aynı önbellek ve aynı referanslarla v1 ve v2 yan yana. Format: P / R / F.
Ayar yalnız Vocadito'nun ilk 20 klibi + MIR-1K "çift" 15 klibi üzerinde yapıldı; kalan klipler doğrulamadır.

| Küme | v1 ham | v1 yuvarlanmış | v2 ham | v2 yuvarlanmış |
|---|---|---|---|---|
| Vocadito ilk 20 (ayar) | 0.811/0.626/**0.702** | 0.760/0.588/**0.658** | 0.817/0.714/**0.759** | 0.761/0.667/**0.707** |
| Vocadito son 20 (doğrulama) | 0.801/0.591/**0.675** | 0.761/0.563/**0.643** | 0.848/0.699/**0.764** | 0.817/0.675/**0.737** |
| **Vocadito 40** | 0.806/0.608/**0.689** | 0.760/0.575/**0.651** | 0.832/0.707/**0.761** | 0.789/0.671/**0.722** |
| MIR-1K karışım çift 15 (ayar) | 0.477/0.550/**0.505** | 0.471/0.547/**0.501** | 0.547/0.667/**0.599** | 0.542/0.663/**0.594** |
| MIR-1K karışım tek 15 (doğrulama) | 0.506/0.599/**0.544** | 0.518/0.612/**0.556** | 0.634/0.719/**0.669** | 0.628/0.708/**0.661** |
| **MIR-1K karışım 30** | 0.492/0.575/**0.525** | 0.495/0.580/**0.528** | 0.590/0.693/**0.634** | 0.585/0.685/**0.628** |

"Yuvarlanmış" = nota perdesi yarım tona (akort kaymasıyla) yuvarlanmış çıktı; zil için kullanılan budur.

Sonuç: Vocadito (insan işaretli, solo) yuvarlanmış **0.722 >= 0.65 (hedef tamam)**, doğrulama yarısında 0.737. MIR-1K karışım yuvarlanmış **0.628 (hedefin 0.02 altı)**, ham 0.634; v1'e göre +0.10.

Önemli not: DURUM_TUR1.md'deki "MIR-1K karışım 0.665 / 0.62" bu ortamda yeniden üretilemedi. Aynı v1 kodu, eşik 0.2, 30 klip, `olc.py` referansı (etiketten türetilmiş tam yarım ton notalar) ile 0.525/0.528 çıktı. Yani 0.628 tabanına göre kıyaslanmalı, 0.665'e göre değil. MIR-1K referansı insan nota etiketi değil, 20 ms'lik tam sayı yarım ton perde etiketinden `f0_to_notes` ile türetilmiş; aşağıdaki sınırlama bu yüzden var.

Sınır: MIR-1K'da yalnız onset'e bakınca (perde gözetmeden) F = 0.80, perdeyle 0.60-0.63. Kayıp perdeden: referans notaları tam sayı yarım ton, şarkıcı ise bazen yarım tonun yarısı kadar sapıyor (ör. ref 56, ölçülen 56.8), yani referans gürültülü. 0.65'e MIR-1K'da ulaşmak için ya insan nota etiketli karışım verisi ya da daha iyi bir referans gerek.

## Ablasyon (bir özelliği değiştirip diğerlerini sabit tutarak, yuvarlanmış F; ham F parantezde)
| Değişiklik | Vocadito 40 | MIR-1K karışım 30 |
|---|---|---|
| tam v2 | 0.722 (0.761) | 0.628 (0.634) |
| akort Viterbi'den sonra (v1 gibi) | 0.666 (0.764) | 0.622 (0.622) |
| akort_guc=1 (tam güç) | 0.711 (0.768) | 0.592 (0.617) |
| akort_guc=0 (akort yok) | 0.716 (0.764) | 0.622 (0.622) |
| durum_medyan kapalı | 0.697 (0.761) | 0.618 (0.634) |
| guven_esik=0 | 0.722 (0.761) | 0.611 (0.617) |
| enerji ile onset bölme kapalı | 0.715 (0.751) | 0.628 (0.634) |
| salience ile onset bölme açık (0.12) | 0.724 (0.768) | 0.587 (0.585) |
| gama çekme açık (0.25 sn) | 0.634 (0.761) | 0.586 (0.634) |
| oktav düzeltme açık (0.25 sn) | 0.723 (0.762) | 0.628 (0.634) |
| kayma=0 | 0.720 (0.758) | 0.625 (0.631) |
| v1 değerleri (lam 8, cap 3, min_dur 0.12, gap_fill 0.06) | 0.678 (0.721) | 0.589 (0.588) |

Bulgular:
- En çok kazandıran: küçük `min_dur`/`gap_fill` (tekrar eden notaları yutmamak: R 0.58 -> 0.71), yüksek `lam`/`cap`, eşik 0.4, notaya `durum_medyan` ile perde atama, akortu Viterbi'den önce çıkarmak (yarı güçle), karışımda `guven_esik`.
- Enerji çukuruyla bölme: Vocadito'da +0.01 (ham), karışımda nötr. Salience çukuruyla bölme: karışımda zararlı (-0.04), kapalı.
- Ton bulma + gama çekme (Krumhansl-Schmuckler): **zarar verdi** (-0.09 Vocadito), çünkü kısa bir şarkı parçasında ton sık yanlış çıkıyor ve gerçek kromatik/geçiş notaları bozuluyor. Uzun notalara dokunmayan hali (<= 0.1 sn) de katkı vermedi. Önerilmez; kod duruyor, `gama_sure=0` ile kapalı.
- Oktav düzeltme: etkisiz (RMVPE'nin oktav hatası yok: oktavsız RPA = RPA), kapalı.
- Taranan aralıklar: esik 0.1-0.6, lam 3-24, cap 1.5-8, gap_fill 0-0.1, min_dur 0.06-0.2, bol_sal 0.05-0.3, bol_enj 2-12, bol_pen 0.08-0.25, bol_min 0.08-0.2, kayma -0.05..+0.05, guven_esik 0-0.8, akort_guc 0-1, agirlik 0-0.6, kat_sure 0.06-0.24. Koordinat inişi (iki tur) ile yapıldı, tam ızgara değil.
- `kayma` = +0.01 (-0.01..+0.02 arası düz; -0.03 ve +0.05 gibi büyük kaymalar Vocadito F'sini 0.72 -> 0.56 civarına düşürdü): hassas ayar, telefonda (ve gerçek şarkıda) tekrar doğrulanmalı.

## Kotlin portu için sözde-kod
Girdi: `f0[i]` (Hz, 0=sessiz), `sal[i]` (salience-max), `rms[i]`, kare adımı `hop`=0.01 sn, kare zamanı `t[i]=i*hop`.

```
HOP = 0.01
midi(f)      = 69 + 12*log2(f/440)
hz(m)        = 440 * 2^((m-69)/12)

1) HAZIRLIK
   v[i] = f0[i] > 0 ;  m[i] = v[i] ? midi(f0[i]) : 0
   // kisa bosluk doldurma (gap_fill=0.02 sn -> 2 kare)
   her ardisik sessiz kosu [i,j) icin:
       eger i>0 ve j<n ve (j-i) <= 2 ve |m[i-1]-m[j]| < 1.5:
           m[i..j-1] = dogrusal ara deger(m[i-1] -> m[j]);  v[i..j-1] = true
   // akort kaymasi (Viterbi'den once)
   sesli karelerde fr = 2*PI*(m - round(m))
   off = 0.5 * atan2( sum(sin fr), sum(cos fr) ) / (2*PI)       // [-0.25, 0.25]

2) VITERBI (her ardisik sesli kosu [i,j) icin ayri; kosu uzunlugu L=j-i)
   eger L == 1:  not ekle(i, j, round(m[i]-off), sert=true); devam
   seg[k] = m[i+k] - off
   durumlar S = tam sayilar  floor(min(seg))-1 .. ceil(max(seg))+1   (30..100 ile kirp)
   maliyet(k,s) = min((seg[k]-s)^2, CAP=5)
   cost[0][s] = maliyet(0,s)
   k = 1..L-1:
       bprev = argmin_s cost[k-1][s]          // en iyi onceki durum
       bmin  = cost[k-1][bprev] + LAM(=12)    // herhangi bir durumdan sicrama maliyeti (sicrama buyuklugunden bagimsiz)
       her s icin:
           kal = cost[k-1][s]
           eger kal <= bmin:  cost[k][s] = maliyet(k,s) + kal;   bp[k][s] = s
           degilse:           cost[k][s] = maliyet(k,s) + bmin;  bp[k][s] = bprev
   path[L-1] = argmin_s cost[L-1][s];  k = L-1..1:  path[k-1] = bp[k][path[k]]
   path degisen yerlerde notaya bol -> notalar (bas=i+a, bit=i+k (dahil degil), durum=states[path[a]], sert=true)

3) KISA NOTALARI KAT  (md = round(0.06/HOP) = 6 kare, kat_tol = 1)
   tekrarla (degisiklik kalmayana dek, her seferinde ilk bulunani isle):
       k-inci not (bit-bas) < md ise:
           onceki = k-1. not, bitisik ise (onceki.bit == bas)
           sonraki = k+1. not, bitisik ise (sonraki.bas == bit)
           aday = bitisik olup |durum - bu.durum| <= 1 olanlar
           aday varsa: durumu en yakin olanin icine kat (onceki ise onceki.bit = bu.bit; sonraki ise sonraki.bas = bu.bas)
           notayi listeden sil (aday yoksa da silinir)
   // perde yeniden ata
   her not icin: durum = round( medyan( m[bas..bit-1] yalniz sesli kareler ) - off )

4) ENERJI CUKURUYLA BOLME  (bol_enj=6 dB, pen=8 kare, minp=8 kare)
   db[i] = 20*log10(rms[i]+1e-5);  db = 3 noktali hareketli ortalama (uclar kenar degeriyle)
   her not [s,e) icin:
       aday kare i, s+minp <= i <= e-minp:
           lo = max(s, i-pen);  hi = min(e, i+pen+1)
           i, db[lo..hi-1] icinde en kucuk degilse (>, kucuk esitler kabul) atla
           sol = max(db[lo..i]);  sag = max(db[i..hi-1])
           prom = min(sol, sag) - db[i];   prom >= 6 ise aday (prom, i)
       adaylari prom'a gore buyukten kucuge sirala; ancak daha once secilmis her kesme noktasina >= minp kare
       uzaksa sec. Kesme noktalarinda notayi ayni durumla parcala; ilk parca ayni "sert" bayragini, sonrakiler sert=true tasir
   notalari bas'a gore sirala

5) BIRLESTIRME YOK (bilerek)
   Viterbi'nin urettigi tum notalar ve bolme parcalari "sert sinir" tasir; ayni perdeli yan yana notalar BIRLESTIRILMEZ.
   (v1 bunlari birlestiriyordu. Python'da sert=False denendi: Vocadito 0.722 -> 0.716, MIR-1K 0.628 -> 0.625, merge_gap 0.2 ile
   0.687/0.599; yani birlestirmek zarar veriyor, cunku tekrar eden ayni perdeli notalar gercek ayri notalar.)
   Kotlin'de adim 5 yazilmayacak. Yalniz: (bit - bas) < md olan notalari at.

6) GUVEN SUZGECI
   notanin ortalama sal[bas..bit-1] degeri < 0.5 ise at.

7) CIKTI
   aralik = [ max(0, t[bas]-HOP/2) + 0.01 , t[bit-1] + HOP/2 + 0.01 ]
   yuvarlanmis perde = durum + off       // off adim 1'deki yarim guclu akort kaymasi
   ham perde         = medyan(m[bas..bit-1])   // surekli deger
   Hz = hz(perde)
```

Notlar:
- `merge_gap` parametresi kodda duruyor ama `sert` bayrağı yüzünden etkisiz (yukarıdaki adım 5). Kotlin'de gerekmez.
- Enerji bölmesinde `rms` karışım enerjisidir (şarkıcı + eşlik). Karışımda faydası nötr, solo seste +0.01; maliyeti sıfıra yakın.
- Model çıktısından salience-max: 360 sınıflı sigmoid çıktısının kare başına en büyüğü; f0 için yerel ortalama (9 bin) v1 ile aynı (`rmvpe_model.to_local_average_cents`). Eşik 0.4, v1'deki 0.03/0.2 değil.
- Hesap yükü: Viterbi sesli kare başına <= 70 durum; 3 dk şarkı (18000 kare) telefonda ihmal edilebilir (Python'da 40 Vocadito klibi ~1 sn önbellekle).

## Yeniden üretme
```
nice -n 10 env OMP_NUM_THREADS=2 /home/user/venv-ml/bin/python -I arastirma/ortak/f0_onbellek_v2.py   # önbellek (~1 dk)
# tablo + ablasyon: nice -n 10 env OMP_NUM_THREADS=2 /home/user/venv-ml/bin/python -I arastirma/ortak/nota_v2_rapor.py  (sonuc/nota_v2_sonuc.json yazar)
# ayar aleti: arastirma/ortak/nota_v2_tara.py (Tarayici sınıfı)
```
