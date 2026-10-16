# Olcum aleti. Kullanim (Python -I ile; /home/user/venv-data veya venv-ml):
#   python -I olc.py --aday ornek_pyin --veri mir1k --mod mix --limit 20 [--out sonuc.json]
# Aday sozlesmesi: arastirma/adaylar/<ad>.py icinde extract(wav_path)->(times_sn, f0_hz) [f0<=0 sessiz];
# istege bagli notes(wav_path)->(intervals Nx2 sn, pitches_hz N). notes yoksa f0'dan otomatik nota uretilir.
import sys, os, json, time, argparse, importlib.util, numpy as np
KOK = "/home/user/Jn/melodi-zil/arastirma"

def modul(yol, ad):
    s = importlib.util.spec_from_file_location(ad, yol); m = importlib.util.module_from_spec(s); s.loader.exec_module(m); return m

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--aday", required=True); ap.add_argument("--veri", required=True, choices=["mir1k", "vocadito"])
    ap.add_argument("--mod", default="mix", choices=["mix", "vokal"]); ap.add_argument("--limit", type=int, default=None)
    ap.add_argument("--out", default=None)
    a = ap.parse_args()
    y = modul(f"{KOK}/olc/yardim.py", "yardim")
    cand = modul(f"{KOK}/adaylar/{a.aday}.py", "aday_" + a.aday)
    mod = a.mod if a.veri == "mir1k" else "mix"
    satir = []; t_isl = 0.0; t_ses = 0.0
    for ad in y.liste(a.veri, a.limit):
        wav, rt, rf, riv, rph = y.yukle(a.veri, ad, mod)
        import soundfile as sf
        sure = sf.info(wav).duration
        t0 = time.time()
        try:
            et, ef = cand.extract(wav); et = np.asarray(et, float); ef = np.asarray(ef, float)
            if hasattr(cand, "notes"): eiv, eph = cand.notes(wav); eiv = np.asarray(eiv, float).reshape(-1, 2); eph = np.asarray(eph, float)
            else: eiv, eph = y.f0_to_notes(et, ef)
        except Exception as e:
            print(f"HATA {ad}: {e!r}", file=sys.stderr); continue
        dt = time.time() - t0; t_isl += dt; t_ses += sure
        r = y.puanla_f0(rt, rf, et, ef); r.update(y.puanla_nota(riv, rph, eiv, eph))
        r.update({"klip": ad, "sure_sn": round(sure, 2), "islem_sn": round(dt, 2)}); satir.append(r)
        print(f"{ad:22s} RPA {r['RPA']:.3f} RCA {r['RCA']:.3f} OA {r['OA']:.3f} notaF {r['nota_F']:.3f} ({dt:.1f}s/{sure:.1f}s)", flush=True)
    if not satir: raise SystemExit("hic klip puanlanamadi")
    ks = ["RPA", "RCA", "OA", "VR", "VFA", "RPA_oktavsiz", "nota_P", "nota_R", "nota_F"]
    ort = {k: float(np.nanmean([s[k] for s in satir])) for k in ks}
    ort.update({"klip_sayisi": len(satir), "toplam_ses_sn": round(t_ses, 1), "toplam_islem_sn": round(t_isl, 1), "RTF": round(t_isl / max(t_ses, 1e-9), 3)})
    sonuc = {"aday": a.aday, "veri": a.veri, "mod": mod, "limit": a.limit, "ortalama": ort, "klipler": satir}
    out = a.out or f"{KOK}/sonuc/{a.aday}_{a.veri}" + (f"_{mod}" if a.veri == "mir1k" else "") + ".json"
    os.makedirs(os.path.dirname(os.path.abspath(out)), exist_ok=True)
    json.dump(sonuc, open(out, "w"), indent=1, ensure_ascii=False)
    # --out verilmediyse standart yola da yaz (sozlesme: sonuc/<ad>_<veri>.json)
    std = f"{KOK}/sonuc/{a.aday}_{a.veri}.json"
    if a.veri == "mir1k" and a.out is None and mod == "mix": json.dump(sonuc, open(std, "w"), indent=1, ensure_ascii=False)
    print("ORTALAMA", json.dumps(ort, ensure_ascii=False)); print("yazildi:", out)

if __name__ == "__main__": main()
