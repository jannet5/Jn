# Veri ve olcum aleti (Melodi Zil arastirma)

Hepsi `/home/user/data/<ad>/` altinda. Ortam: `/home/user/venv-data` (numpy scipy librosa soundfile mir_eval yt-dlp). Calistirma hep `python -I`.

## Veri setleri
| Set | Yol | Icerik | Lisans / not |
|---|---|---|---|
| MIR-1K | `/home/user/data/mir1k/veri/MIR-1K/` (1.2 GB acik; `zipdir/MIR-1K.zip` 1.0 GB) | 1000 karaoke klibi (3-13 sn, 16 kHz stereo; **sol=enstruman, sag=vokal**), `PitchLabel/*.pv` (MIDI yarim ton, 0=sessiz, **20 ms adim, ilk kare 20 ms**) | Akademik/arastirma kullanimi (Hsu & Jang). Kaynak: HF `AnhP/Mir-1k-use-DJCM-training` (MIR-1K.zip). Orijinal site (mirlab.org) denenmedi. |
| Vocadito | `/home/user/data/vocadito/veri/` (130 MB) | 40 solo sarki (Audio/*.wav), `Annotations/F0/*_f0.csv` (5.8 ms), `Annotations/Notes/*_notesA1.csv, _notesA2.csv` (baslangic, Hz, sure) | CC BY 4.0 (Bittner ve ark.). Kaynak: zenodo 5578807. |
| MIR-1K turetilmis | `/home/user/data/mir1k/ses/{mix,vokal}/` | olc.py ilk kullanimda yazar: mix=(sol+sag)/2, vokal=sag | - |
| YouTube pop | `/home/user/data/youtube/` | **INMEDI** (asagiya bak) | - |

Ek set denemesi (c): zaman bitti; MedleyDB (Zenodo, erisim talebi ister), RWC (lisansli, ucretli), MIR-ST500 (aciklamalar sadece Google Drive/GitHub'da; github.com ve drive burada denenmedi) -> alinamadi. Sonraki adim: HF'de `MIR-ST500`/`MedleyDB-melody` aynasi aramak.

## Olcum aleti
```
OMP_NUM_THREADS=2 nice -n 10 /home/user/venv-data/bin/python -I \
  /home/user/Jn/melodi-zil/araclar/degerlendirme/olc.py --aday <ad> --veri mir1k|vocadito [--mod mix|vokal] [--limit N] [--out x.json]
```
- Aday: `arastirma/adaylar/<ad>.py` icinde `extract(wav_path) -> (times_sn, f0_hz)` (f0<=0 sessiz). Istege bagli `notes(wav_path) -> (intervals Nx2 sn, pitches_hz N)`; yoksa f0'dan otomatik nota uretilir (`arastirma/olc/yardim.py: f0_to_notes`, adaya adil olsun diye ref de MIR-1K'da ayni yontemle uretilir).
- `--limit N`: klipleri esit aralikli secer (tek sarkici yanlisi olmasin).
- Sutunlar: RPA, RCA, OA, VR, VFA (mir_eval.melody), `RPA_oktavsiz` (tahmin ref*2^k, |k|<=2 ise dogru), nota P/R/F (mir_eval.transcription, onset_tolerance=0.05, offset yok), RTF (islem sn / ses sn).
- Cikti: `arastirma/sonuc/<ad>_<veri>.json` (MIR-1K: `<ad>_mir1k_mix.json` / `_vokal.json`; mix icin `<ad>_mir1k.json` kopyasi). Icinde `ortalama` ve klip bazli `klipler`.
- Vocadito referans notalari: annotator 1. MIR-1K referans notalari f0 etiketinden turetilir (insan nota etiketi yok).
- Yontem notu: ref ve tahmin farkli zaman izgarasinda olabilir; mir_eval yeniden ornekler.

## Referans: ornek_pyin (librosa.pyin, ML'siz) — `arastirma/adaylar/ornek_pyin.py`
| Veri | klip | RPA | RCA | OA | nota F | RTF |
|---|---|---|---|---|---|---|
| Vocadito (solo) | 40 | 0.965 | 0.967 | 0.832 | 0.546 | 0.035 |
| MIR-1K mix | 100 | 0.297 | 0.462 | 0.316 | 0.180 | 0.036 |
| MIR-1K vokal (sag kanal) | 100 | 0.960 | 0.965 | 0.869 | 0.739 | 0.037 |

Yorum: temiz solo ses cok kolay; karisimda klasik yontem coker (RPA %30). Hedef (>=%80 RPA, F>=0.65) karisimda vokal ayirma + guclu f0 modeli ister. Karsilastirma esigi: MIR-1K mix RPA.

## YouTube durumu
Indirme **basarisiz**: `arastirma/olc/yt_indir.sh` (yt-dlp 2026.8.19, 5 farkli player_client). Neden: sunucu IP'si icin YouTube "Sign in to confirm you're not a bot" (HTTP 429 + bot kontrolu); web/mweb/web_safari sadece storyboard (resim) formati veriyor, ses yok. node JS runtime (`--js-runtimes node`) eklendi, degismedi. Invidious (yewtu.be) API 403, ses ucu sadece HTML donuyor; Piped 526. Cozum adaylari: (1) kullanicinin cookies.txt dosyasi (`--cookies`), (2) bgutil PO-token saglayici eklentisi, (3) kullanicidan 5-7 sarkinin mp3/wav'ini yuklemesi, (4) uygulamada cihaz IP'sinden indirme (telefonun kendi IP'si bot kontrolune takilmaz; gercek test telefonda olur).
