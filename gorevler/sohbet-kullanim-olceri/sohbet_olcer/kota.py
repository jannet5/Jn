"""Hesap kotası anlık görüntüleri: okuma, normalleştirme ve fark hesabı.

Kota değerleri HER ZAMAN hesap genelidir. Claude Code'un statusline
``rate_limits`` alanı ve SDK/CCR ``rate_limit_event`` olayı, ilgili oturumun
son API yanıtına eklenmiş hesap durumunu taşır (anthropics/claude-code
#75408); oturum başına pay verilmez (#29721). Bu modül iki anlık görüntü
arasındaki farkı hesaplar ama bunu asla tek bir sohbetin tüketimi diye
etiketlemez; o yorum rapor katmanında ayrı yapılır.

Desteklenen kaynaklar:

* ``statusline``: ``sohbet_olcer kaydet`` komutunun statusline'dan yazdığı
  JSONL (``used_percentage`` 0-100, ``resets_at`` epoch saniye).
* ``rate_limit_event``: Agent SDK / ``claude -p --output-format stream-json``
  satırları ya da CCR ``list_events`` dökümü. ``utilization`` 0-1 kesirdir.
* ``elle``: kullanıcının claude.ai Ayarlar → Kullanım ekranından okuduğu
  değer (ekran tam sayı gösterdiği için çözünürlük 1 puan).
"""

import json
import os

try:
    import fcntl
except ImportError:  # Windows
    fcntl = None
    import msvcrt
from dataclasses import dataclass
from datetime import datetime, timezone

from .zaman import epoch_ayristir, iso_ayristir

PENCERE_ADLARI = {"five_hour": "5 saatlik", "seven_day": "haftalık",
                  "seven_day_opus": "haftalık (Opus)",
                  "seven_day_sonnet": "haftalık (Sonnet)",
                  "spend_limit": "harcama limiti"}
YENILENME_TOLERANSI_SN = 120


@dataclass
class Goruntu:
    zaman: datetime
    pencere: str
    kullanilan: object       # yüzde (0-100) ya da None (yalnız yenilenme biliniyor)
    cozunurluk: float        # yüzde puanı
    yenilenme: object        # datetime ya da None
    kaynak: str
    oturum: object = None

    @property
    def kalan(self):
        return None if self.kullanilan is None else max(0.0, 100.0 - self.kullanilan)


def _ondalik_basamak(deger):
    metin = repr(float(deger))
    if "e" in metin or "E" in metin:
        return 6
    tam, _, kesir = metin.partition(".")
    kesir = kesir.rstrip("0")
    return len(kesir)


def cozunurluk_cikar(goruntuler):
    """Aynı kaynak+pencere dizisindeki en fazla ondalık basamaktan çözünürlük
    çıkarır. Örn. kesir 0.3 ve 0.31 → 2 basamak → 1 puan; yüzde 23.5 → 0.1."""
    gruplar = {}
    for g in goruntuler:
        if g.kullanilan is None:
            continue
        gruplar.setdefault((g.kaynak, g.pencere), []).append(g)
    for liste in gruplar.values():
        basamak = max(getattr(g, "_ham_basamak", 0) for g in liste)
        coz = 10.0 ** (-basamak)
        for g in liste:
            if not getattr(g, "_sabit_cozunurluk", False):
                g.cozunurluk = coz
    return goruntuler


def _yeni(zaman, pencere, kullanilan, yenilenme, kaynak, oturum, basamak, sabit=None):
    g = Goruntu(zaman=zaman, pencere=pencere, kullanilan=kullanilan,
                cozunurluk=sabit if sabit is not None else 1.0,
                yenilenme=yenilenme, kaynak=kaynak, oturum=oturum)
    g._ham_basamak = basamak
    g._sabit_cozunurluk = sabit is not None
    return g


def _rate_limit_info(bilgi, zaman, oturum, kaynak="rate_limit_event"):
    sonuc = []
    pencereler = bilgi.get("unifiedWindows")
    if isinstance(pencereler, dict) and pencereler:
        for ad, p in pencereler.items():
            if not isinstance(p, dict):
                continue
            u = p.get("utilization")
            ok = isinstance(u, (int, float)) and not isinstance(u, bool)
            sonuc.append(_yeni(zaman, ad, float(u) * 100.0 if ok else None,
                               epoch_ayristir(p.get("resetsAt")), kaynak, oturum,
                               max(0, _ondalik_basamak(u) - 2) if ok else 0))
        return sonuc
    ad = bilgi.get("rateLimitType")
    if ad:
        u = bilgi.get("utilization")
        ok = isinstance(u, (int, float)) and not isinstance(u, bool)
        sonuc.append(_yeni(zaman, ad, float(u) * 100.0 if ok else None,
                           epoch_ayristir(bilgi.get("resetsAt")), kaynak, oturum,
                           max(0, _ondalik_basamak(u) - 2) if ok else 0))
    return sonuc


OTU_ETIKETLERI = {"Session": "five_hour", "Weekly": "seven_day", "Sonnet": "seven_day_sonnet",
                  "Fable": "seven_day_fable", "Claude Design": "seven_day_design"}


def _otu(olay, varsayilan_zaman):
    """OpenTokenUsage anlık görüntüsü. OTU bu değerleri Claude Code'un OAuth
    bilgisiyle belgelenmemiş /api/oauth/usage uç noktasından alır; bu araç
    kimlik bilgisine dokunmaz, yalnız OTU'nun kullanıcının makinesinde zaten
    sunduğu yerel JSON'u okur. Değerler tam sayı yüzde (çözünürlük 1)."""
    zaman = iso_ayristir(olay.get("fetchedAt") or olay.get("updatedAt")) or varsayilan_zaman
    sonuc = []
    for satir in olay["lines"]:
        if not isinstance(satir, dict) or satir.get("type") != "progress":
            continue
        ad = OTU_ETIKETLERI.get(satir.get("label"))
        bicim = (satir.get("format") or {}).get("kind")
        if not ad or bicim != "percent":
            continue
        kullanilan, limit = satir.get("used"), satir.get("limit") or 100.0
        ok = isinstance(kullanilan, (int, float)) and not isinstance(kullanilan, bool)
        sonuc.append(_yeni(zaman, ad, float(kullanilan) * 100.0 / float(limit) if ok else None,
                           iso_ayristir(satir.get("resetsAt")), "opentokenusage", None, 0,
                           sabit=1.0))
    return sonuc


def _olaydan(olay, varsayilan_zaman):
    """Tek bir JSON nesnesinden (stream-json satırı, CCR olayı, kaydet
    satırı) anlık görüntüleri çıkarır."""
    if not isinstance(olay, dict):
        return []
    # kaydet komutunun yazdığı statusline satırı
    if olay.get("kaynak") == "statusline" or ("rate_limits" in olay and "ts" in olay):
        zaman = iso_ayristir(olay.get("ts")) or varsayilan_zaman
        sonuc = []
        for ad, p in (olay.get("rate_limits") or {}).items():
            if not isinstance(p, dict):
                continue
            u = p.get("used_percentage")
            ok = isinstance(u, (int, float)) and not isinstance(u, bool)
            sonuc.append(_yeni(zaman, ad, float(u) if ok else None,
                               epoch_ayristir(p.get("resets_at")), "statusline",
                               olay.get("session_id"), _ondalik_basamak(u) if ok else 0))
        return sonuc
    if olay.get("kaynak") == "elle":
        return [_yeni(iso_ayristir(olay.get("ts")) or varsayilan_zaman, olay["pencere"],
                      float(olay["yuzde"]) if olay.get("yuzde") is not None else None,
                      iso_ayristir(olay.get("yenilenme")), "elle", None, 0,
                      sabit=float(olay.get("cozunurluk", 1.0)))]
    # OpenTokenUsage yerel HTTP API (GET 127.0.0.1:6736/v1/usage/claude)
    if olay.get("providerId") == "claude" and isinstance(olay.get("lines"), list):
        return _otu(olay, varsayilan_zaman)
    # CCR list_events kaydı
    if "rate_limit_event" in olay and isinstance(olay["rate_limit_event"], dict):
        ic = olay["rate_limit_event"]
        ic = ic.get("internal_anthropic_catchall", ic)
        zaman = iso_ayristir(olay.get("created_at")) or varsayilan_zaman
        bilgi = ic.get("rate_limit_info") or {}
        return _rate_limit_info(bilgi, zaman, ic.get("session_id"))
    # SDK / stream-json satırı
    if olay.get("type") == "rate_limit_event":
        zaman = (iso_ayristir(olay.get("timestamp")) or iso_ayristir(olay.get("created_at"))
                 or varsayilan_zaman)
        return _rate_limit_info(olay.get("rate_limit_info") or {}, zaman, olay.get("session_id"))
    return []


def dosyadan_oku(yol, varsayilan_zaman=None):
    """JSON (tek nesne/dizi, CCR dökümü) ya da JSONL dosyasını okur.
    Döner: (görüntüler, bozuk satır sayısı)."""
    varsayilan_zaman = varsayilan_zaman or datetime.fromtimestamp(
        os.path.getmtime(yol), tz=timezone.utc)
    with open(yol, "rb") as f:
        ham = f.read().decode("utf-8", errors="replace")
    nesneler, bozuk = [], 0
    try:
        tum = json.loads(ham)
        if isinstance(tum, dict) and isinstance(tum.get("ccr"), dict):
            tum = tum["ccr"]
        if isinstance(tum, dict) and isinstance(tum.get("data"), list):
            nesneler = tum["data"]
        elif isinstance(tum, list):
            nesneler = tum
        else:
            nesneler = [tum]
    except ValueError:
        for satir in ham.splitlines():
            if not satir.strip():
                continue
            try:
                nesneler.append(json.loads(satir))
            except ValueError:
                bozuk += 1
    goruntuler = []
    for n in nesneler:
        goruntuler.extend(_olaydan(n, varsayilan_zaman))
    return cozunurluk_cikar(goruntuler), bozuk


def kilitli_ekle(yol, nesne):
    """Eşzamanlı yazıcılar için satırı kilitle ve tek write() ile ekler."""
    os.makedirs(os.path.dirname(os.path.abspath(yol)), exist_ok=True)
    satir = (json.dumps(nesne, ensure_ascii=False, separators=(",", ":")) + "\n").encode()
    fd = os.open(yol, os.O_WRONLY | os.O_CREAT | os.O_APPEND | getattr(os, "O_BINARY", 0), 0o600)
    try:
        if fcntl:
            fcntl.flock(fd, fcntl.LOCK_EX)
        else:
            os.lseek(fd, 0, os.SEEK_SET)
            msvcrt.locking(fd, msvcrt.LK_LOCK, 1)  # ilk baytı kilitle (10 sn dener)
            os.lseek(fd, 0, os.SEEK_END)
        os.write(fd, satir)
    finally:
        if fcntl:
            fcntl.flock(fd, fcntl.LOCK_UN)
        else:
            os.lseek(fd, 0, os.SEEK_SET)
            msvcrt.locking(fd, msvcrt.LK_UNLCK, 1)
        os.close(fd)


def ayni_donem(a, b):
    if a.yenilenme is None or b.yenilenme is None:
        return None  # bilinemiyor
    return abs((a.yenilenme - b.yenilenme).total_seconds()) <= YENILENME_TOLERANSI_SN


def guncel_durum(goruntuler, pencere, simdi=None):
    """Bir pencere için en güncel bilgiyi döndürür. Aynı yenilenme
    dönemindeki değerler artan olmalıdır; farklı oturumların eski anlık
    görüntüleri (#75408) daha düşük gösterebileceğinden en son dönemdeki en
    yüksek değer alınır. Yenilenme zamanı geçmişse veri bayattır."""
    adaylar = [g for g in goruntuler if g.pencere == pencere]
    if not adaylar:
        return None
    en_son = max(adaylar, key=lambda g: g.zaman)
    donem = [g for g in adaylar if ayni_donem(g, en_son) is not False]
    olculen = [g for g in donem if g.kullanilan is not None]
    secilen = max(olculen, key=lambda g: (g.kullanilan, g.zaman)) if olculen else en_son
    simdi = simdi or datetime.now(timezone.utc)
    bayat = bool(secilen.yenilenme and secilen.yenilenme <= simdi)
    return {"goruntu": secilen, "bayat": bayat,
            "eski_dusuk": [g for g in olculen if g.kullanilan < secilen.kullanilan
                           and g.zaman > secilen.zaman]}


@dataclass
class Fark:
    pencere: str
    once: object
    sonra: object
    durum: str               # "olculdu", "yenilendi", "yetersiz", "ilk_istek_sonrasi"
    deger: object = None
    alt: object = None
    ust: object = None
    son_eksik: bool = False  # son ölçüm sohbetin son isteğinden önce


def _guvenilir(liste):
    """Bir aday kümesinden en güvenilir değeri seçer: en son görüntünün
    yenilenme dönemindekiler arasında en yüksek kullanım. Kota bir dönem
    içinde azalmadığından daha düşük ve daha yeni bir değer, başka bir
    oturumun eskimiş anlık görüntüsüdür (anthropics/claude-code #75408)."""
    en_son = max(liste, key=lambda g: g.zaman)
    donem = [g for g in liste if ayni_donem(g, en_son) is not False]
    return max(donem, key=lambda g: (g.kullanilan, g.zaman))


def fark_hesapla(goruntuler, pencere, baslangic, bitis):
    """[baslangic, bitis] aralığının hemen öncesi ve sonrası arasındaki
    hesap geneli değişimi aralık (alt-üst) olarak hesaplar.

    Yuvarlama: aynı yöntemle (aşağı yuvarlama ya da en yakına yuvarlama)
    çözünürlüğü r olan iki değerin farkı d ise gerçek fark [d-r, d+r]
    aralığındadır; kota pencere içinde azalmadığından alt sınır 0'a
    kırpılır."""
    olculen = sorted((g for g in goruntuler if g.pencere == pencere
                      and g.kullanilan is not None), key=lambda g: g.zaman)
    if not olculen:
        return Fark(pencere, None, None, "yetersiz")
    oncekiler = [g for g in olculen if g.zaman <= baslangic]
    durum = "olculdu"
    if oncekiler:
        once = _guvenilir(oncekiler)
    else:
        icerde = [g for g in olculen if baslangic < g.zaman <= bitis]
        if not icerde:
            sonrakiler = [g for g in olculen if g.zaman > bitis]
            if not sonrakiler:
                return Fark(pencere, None, None, "yetersiz")
            once = sonrakiler[0]
        else:
            once = icerde[0]
        durum = "ilk_istek_sonrasi"
    sonrakiler = [g for g in olculen if g.zaman >= bitis and g.zaman >= once.zaman]
    sonra = _guvenilir(sonrakiler) if sonrakiler else _guvenilir(olculen)
    if sonra is once:
        return Fark(pencere, once, sonra, "yetersiz")
    ayni = ayni_donem(once, sonra)
    if ayni is False:
        return Fark(pencere, once, sonra, "yenilendi")
    d = sonra.kullanilan - once.kullanilan
    r = max(once.cozunurluk, sonra.cozunurluk)
    return Fark(pencere, once, sonra, durum, deger=d,
                alt=max(0.0, d - r), ust=max(0.0, d + r), son_eksik=sonra.zaman < bitis)
