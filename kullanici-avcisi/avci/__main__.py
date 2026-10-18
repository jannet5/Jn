"""Komut satırı: python -m avci <komut>"""
import argparse
import glob
import os
import sys
import tempfile

import requests

from .candidates import from_combos, from_names, from_pattern, from_words
from .checkers import InstagramProfileChecker, MockChecker
from .engine import Engine
from .notify import Notifier
from .store import Store
from .translit import to_latin

from .wordlists import WORDLISTS, fetch_lang


def cmd_fetch(a):
    for lang in a.lang:
        ok, msg = fetch_lang(lang, a.top)
        print(("✓ " if ok else "✗ ") + msg)


def cmd_ui(a):
    from .webui import serve
    serve(db_path=a.db, port=a.port, demo=a.demo, open_browser=not a.no_browser)


def cmd_selftest(a):
    from .selftest import run_selftest
    ok, msg = run_selftest(InstagramProfileChecker())
    print(("✓ " if ok else "✗ ") + msg)
    return 0 if ok else 1


def cmd_tani(a):
    """Çalışmıyorsa: tek komutla tanı raporu üretir ve dosyaya yazar."""
    from .diagnose import build_report, probe_instagram
    raws, sel = probe_instagram(InstagramProfileChecker())
    log = os.path.join(os.path.dirname(os.path.abspath(a.db)), "avci.log")
    rep = build_report(Store(a.db), log, sel, None, raws)
    with open(a.out, "w", encoding="utf-8") as f:
        f.write(rep)
    print(rep)
    print(f"\n→ Rapor '{a.out}' dosyasına da kaydedildi. Sorunu çözecek kişiye bunu gönder.")


def cmd_probe(a):
    """Instagram'ın bir isim için döndürdüğü HAM cevabı gösterir (yöntem değiştiyse nedeni burada görünür)."""
    import json
    chk = InstagramProfileChecker()
    for n in a.names:
        print(f"--- {n}")
        print(json.dumps(chk.raw(n), ensure_ascii=False, indent=2))


def _wordfiles(a):
    files = list(a.file or [])
    for lang in a.lang or []:
        found = sorted(glob.glob(os.path.join(WORDLISTS, f"{lang}_*.txt")))
        if not found:
            print(f"⚠ '{lang}' için liste yok. Önce: python -m avci fetch {lang}")
        files += found
    return files


def cmd_build(a):
    st = Store(a.db)
    total = 0
    for path in _wordfiles(a):
        n = st.add_many(from_words(path, a.min, a.max, a.digits))
        print(f"  {os.path.basename(path)}: +{n} aday")
        total += n
    for ln in a.combo or []:
        n = st.add_many(from_combos(ln, a.digits))
        print(f"  tüm {ln} harfli kombinasyonlar: +{n} aday")
        total += n
    for pat in a.pattern or []:
        n = st.add_many(from_pattern(pat))
        print(f"  desen {pat.upper()}: +{n} aday")
        total += n
    if not (_wordfiles(a) or a.combo or a.pattern):
        print("Hiçbir kaynak verilmedi. Örnek: python -m avci build --lang ar --min 3 --max 7")
    print(f"✓ Toplam yeni aday: {total}. Durum: {st.stats()}")


def cmd_add(a):
    names = list(a.names)
    if a.file:
        with open(a.file, encoding="utf-8") as f:
            names += [l.strip() for l in f if l.strip()]
    expanded = []
    for n in names:  # Arapça vb. yazılmışsa Latin karşılıklarını da ekle
        expanded += to_latin(n, digits=True) if not n.isascii() else [n.lower().lstrip("@")]
    st = Store(a.db)
    st.add_many(from_names(expanded), hot=True)
    print(f"✓ Sıcak listeye eklendi ({len(expanded)}): {', '.join(expanded[:20])}")


def cmd_run(a):
    st = Store(a.db)
    nt = Notifier(found_file=a.found_file, bell=not a.no_bell)
    eng = Engine(st, InstagramProfileChecker(), nt, rps=a.rps, workers=a.workers,
                 cool_hours=a.cool_hours, hot_seconds=a.hot_seconds)
    print(f"▶ Başladı. Hız ≈ {a.rps}/sn, {a.workers} işçi. Durdurmak: Ctrl+C (kaldığı yerden devam eder)."
          f" Durum: {st.stats()}")
    res = eng.run(minutes=a.minutes)
    print(f"■ Bitti: {res}")


def cmd_status(a):
    st = Store(a.db)
    print("Durum:", st.stats())
    print("Uzunluğa göre:")
    for r in st.by_length():
        print(f"  {r['length']} harf: toplam {r['n']}, bekleyen {r['p']}, dolu {r['t']}, bulunan {r['f']}")
    print("Sıradaki ilk adaylar:", ", ".join(st.top_pending(15)) or "-")


def cmd_found(a):
    rows = Store(a.db).found()
    if not rows:
        print("Henüz bulunan yok.")
    for r in rows:
        print(f"@{r['name']}  ({r['length']} harf, {r['src']})")


def cmd_translit(a):
    for w in a.words:
        v = to_latin(w, digits=a.digits)
        print(f"{w} -> {', '.join(v) if v else '(çevrilemedi)'}")


def cmd_demo(a):
    """Gerçek Instagram'a dokunmadan tüm sistemi gösterir: sahte dünyada isimler zamanla boşalır."""
    import time
    tmp = tempfile.mkdtemp(prefix="avci-demo-")
    st = Store(os.path.join(tmp, "demo.db"))
    seed = os.path.join(WORDLISTS, "ar_seed.txt")
    st.add_many(from_words(seed, 3, 7))
    names = [r[0] for r in st.db.execute("SELECT name FROM names ORDER BY score DESC").fetchall()]
    watch = names[:2]
    st.add_many(from_names(watch), hot=True)
    release = {watch[0]: 3.0, watch[1]: 7.0}
    chk = MockChecker(taken=names, release_at=release, limit_every=17)
    nt = Notifier(found_file=os.path.join(tmp, "bulunanlar.txt"), bell=False)
    eng = Engine(st, chk, nt, rps=25, workers=3, hot_seconds=0.8, cool_hours=1,
                 confirm=2, confirm_gap=0.2)
    print(f"DEMO: {len(names)} aday, sahte Instagram. '{watch[0]}' 3. sn'de, '{watch[1]}' 7. sn'de boşalacak.")
    t0 = time.time()
    res = eng.run(minutes=0.25)
    print(f"Sonuç ({time.time()-t0:.0f} sn): {res}")
    ok = set(res["bulunanlar"]) == set(watch)
    print("✓ DEMO BAŞARILI: boşalan iki isim yakalandı." if ok else "✗ DEMO BAŞARISIZ")
    return 0 if ok else 1


def main(argv=None):
    p = argparse.ArgumentParser(prog="avci", description="Instagram kullanıcı adı avcısı (3-7 harf, her dil)")
    p.add_argument("--db", default="avci.db", help="durum dosyası (varsayılan avci.db)")
    sub = p.add_subparsers(dest="cmd", required=True)

    s = sub.add_parser("fetch", help="bir dilin kelime listesini indir (ar, tr, fa, ru, es, fr, de, hi, id ...)")
    s.add_argument("lang", nargs="+")
    s.add_argument("--top", type=int, default=50000)
    s.set_defaults(fn=cmd_fetch)

    s = sub.add_parser("build", help="aday havuzu oluştur")
    s.add_argument("--lang", nargs="*", help="indirilmiş/hazır dil listeleri (ör. ar tr)")
    s.add_argument("--file", nargs="*", help="kendi kelime dosyan (satır başına bir kelime, her dil)")
    s.add_argument("--combo", nargs="*", type=int, help="tüm harf kombinasyonları (ör. 3 -> 17.576 aday)")
    s.add_argument("--pattern", nargs="*", help="desen: C ünsüz, V ünlü (ör. CVCV)")
    s.add_argument("--min", type=int, default=3)
    s.add_argument("--max", type=int, default=7)
    s.add_argument("--digits", action="store_true", help="rakamlı yazımlara izin ver (7abibi gibi)")
    s.set_defaults(fn=cmd_build)

    s = sub.add_parser("add", help="özel isimleri SICAK listeye ekle (en sık kontrol edilir)")
    s.add_argument("names", nargs="*")
    s.add_argument("--file")
    s.set_defaults(fn=cmd_add)

    s = sub.add_parser("run", help="sürekli kontrol et (Ctrl+C ile durur, devam eder)")
    s.add_argument("--minutes", type=float, help="bu kadar dakika çalış, sonra dur (boşsa sonsuz)")
    s.add_argument("--rps", type=float, default=0.4, help="saniyede en fazla istek (varsayılan 0.4)")
    s.add_argument("--workers", type=int, default=2)
    s.add_argument("--cool-hours", type=float, default=6.0, help="dolu isimleri kaç saatte bir tekrar kontrol et")
    s.add_argument("--hot-seconds", type=float, default=120.0, help="sıcak listeyi kaç saniyede bir kontrol et")
    s.add_argument("--found-file", default="bulunanlar.txt")
    s.add_argument("--no-bell", action="store_true")
    s.set_defaults(fn=cmd_run)

    s = sub.add_parser("status", help="özet durum")
    s.set_defaults(fn=cmd_status)
    s = sub.add_parser("found", help="bulunan isimler")
    s.set_defaults(fn=cmd_found)

    s = sub.add_parser("translit", help="bir kelimenin Latin yazımlarını göster (test)")
    s.add_argument("words", nargs="+")
    s.add_argument("--digits", action="store_true")
    s.set_defaults(fn=cmd_translit)

    s = sub.add_parser("demo", help="gerçek Instagram'sız tam deneme")
    s.set_defaults(fn=cmd_demo)

    s = sub.add_parser("ui", help="tıklanan ekranı aç (önerilen)")
    s.add_argument("--port", type=int, default=8765)
    s.add_argument("--demo", action="store_true", help="sahte Instagram ile dene")
    s.add_argument("--no-browser", action="store_true")
    s.set_defaults(fn=cmd_ui)

    s = sub.add_parser("selftest", help="bu internet bağlantısında kontrol çalışıyor mu?")
    s.set_defaults(fn=cmd_selftest)

    s = sub.add_parser("tani", help="ÇALIŞMIYORSA: tanı raporu üret (tani-raporu.txt)")
    s.add_argument("--out", default="tani-raporu.txt")
    s.set_defaults(fn=cmd_tani)

    s = sub.add_parser("probe", help="bir ismin Instagram'daki HAM cevabını göster (hata ayıklama)")
    s.add_argument("names", nargs="+")
    s.set_defaults(fn=cmd_probe)

    a = p.parse_args(argv)
    return a.fn(a) or 0


if __name__ == "__main__":
    sys.exit(main())
