"""OpenAI Codex CLI rollout dosyaları için salt okunur adaptör.

Claude koduyla ortak tek şey biçimlendirme ve genel kota hesabıdır
(``kota.Goruntu``, ``guncel_durum``, ``fark_hesapla``). Bu modül hiçbir kimlik
bilgisi ya da auth dosyası okumaz (``~/.codex/auth.json`` dahil); yalnız
Codex'in kendi yazdığı oturum kayıtlarını okur ve hiçbir dosyaya yazmaz.

Biçim, openai/codex ``codex-rs/protocol/src/protocol.rs`` kaynağına göre:

* Dosyalar: ``$CODEX_HOME/sessions/YYYY/MM/DD/rollout-*.jsonl``
  (``CODEX_HOME`` yoksa ``~/.codex``).
* Her satır ``{"timestamp", "type", "payload"}``. ``type == "session_meta"``
  satırında ``payload.id`` konuşma (thread) kimliği, ``forked_from_id``,
  ``parent_thread_id``, ``cwd``, ``cli_version`` bulunur.
* ``type == "event_msg"`` ve ``payload.type == "token_count"`` satırında
  ``info`` (``total_token_usage`` birikimli, ``last_token_usage`` son tur;
  ``null`` olabilir) ve ``rate_limits`` (``primary``/``secondary``; her biri
  ``used_percent`` 0-100, ``window_minutes``, ``resets_at`` epoch saniye;
  eski sürümlerde ``resets_in_seconds``) bulunur. ``rate_limits`` hesap
  genelidir ve o satırın zaman damgasındaki durumu gösterir.

Token sayımı: birikimli ``total_token_usage`` değerlerinin artışları
toplanır. Aynı olay iki kez yazılırsa artış 0'dır; çatallanmış (fork)
oturumun kopyaladığı eski satırlar zaman damgası ve içerikle tanınıp
yeniden sayılmaz; birikimli değer düşerse (yeni sayaç) o turun
``last_token_usage`` değeri eklenir ve olay raporlanır.
"""

import glob
import json
import os
from dataclasses import dataclass, field
from datetime import timedelta

from . import kota
from .rapor import sayi, yuzde
from .zaman import epoch_ayristir, iso_ayristir, sure_metni, tarih_saat

ALANLAR = ("input_tokens", "cached_input_tokens", "cache_write_input_tokens",
           "output_tokens", "reasoning_output_tokens", "total_tokens")


def varsayilan_kok():
    ana = os.environ.get("CODEX_HOME") or os.path.join(os.path.expanduser("~"), ".codex")
    return os.path.join(ana, "sessions")


def pencere_adi(dakika, limit_id=None):
    if dakika == 300:
        ad = "codex_five_hour"
    elif dakika == 10080:
        ad = "codex_seven_day"
    elif dakika:
        ad = f"codex_{dakika}m"
    else:
        ad = "codex_bilinmeyen"
    if limit_id and limit_id != "codex":
        ad += f"[{limit_id}]"
    return ad


def pencere_etiketi(ad):
    taban, _, limit = ad.partition("[")
    etiket = {"codex_five_hour": "Codex 5 saatlik", "codex_seven_day": "Codex haftalık",
              "codex_bilinmeyen": "Codex (süresi bilinmeyen pencere)"}.get(taban)
    if etiket is None:
        etiket = f"Codex {taban.replace('codex_', '').rstrip('m')} dakikalık"
    return etiket + (f" ({limit.rstrip(']')} limiti)" if limit else "")


@dataclass
class CodexKonusma:
    kimlik: str
    dosyalar: list = field(default_factory=list)
    meta: dict = field(default_factory=dict)
    ilk: object = None
    son: object = None
    sayac: dict = field(default_factory=dict)
    olay_sayisi: int = 0
    sayac_sifirlanma: int = 0

    def ekle(self, artis, zaman):
        for a, v in artis.items():
            self.sayac[a] = self.sayac.get(a, 0) + v
        if zaman:
            self.ilk = zaman if self.ilk is None else min(self.ilk, zaman)
            self.son = zaman if self.son is None else max(self.son, zaman)


@dataclass
class CodexOkuma:
    konusmalar: dict = field(default_factory=dict)
    goruntuler: list = field(default_factory=list)
    dosyalar: list = field(default_factory=list)
    bozuk_satir: int = 0
    yarim_son_satir: int = 0
    tekrar_olay: int = 0
    kopya_olay: int = 0
    degismeyen_olay: int = 0  # sayaç değişmeden yazılmış olay (yalnız kota güncellemesi)
    planlar: set = field(default_factory=set)


def _usage(d):
    if not isinstance(d, dict):
        return None
    return {a: int(d.get(a) or 0) for a in ALANLAR}


def _satirlar(dosya, okuma):
    with open(dosya, "rb") as f:
        veri = f.read()
    parcalar = veri.split(b"\n")
    son_tam = veri.endswith(b"\n")
    for i, ham in enumerate(parcalar):
        if not ham.strip():
            continue
        try:
            yield ham, json.loads(ham.decode("utf-8"))
        except (ValueError, UnicodeDecodeError):
            if i == len(parcalar) - 1 and not son_tam:
                okuma.yarim_son_satir += 1
            else:
                okuma.bozuk_satir += 1


def dosyalari_bul(yollar):
    sonuc = []
    for yol in yollar:
        yol = os.path.expanduser(yol)
        if os.path.isdir(yol):
            sonuc.extend(sorted(glob.glob(os.path.join(yol, "**", "*.jsonl"), recursive=True)))
        elif os.path.isfile(yol):
            sonuc.append(yol)
    return sorted(set(sonuc), key=sonuc.index)


def _rate_limits(rl, zaman, kimlik, okuma):
    if not isinstance(rl, dict):
        return
    if rl.get("plan_type"):
        okuma.planlar.add(str(rl["plan_type"]))
    for anahtar in ("primary", "secondary"):
        p = rl.get(anahtar)
        if not isinstance(p, dict):
            continue
        u = p.get("used_percent")
        ok = isinstance(u, (int, float)) and not isinstance(u, bool)
        if p.get("resets_at") is not None:
            yenilenme = epoch_ayristir(p["resets_at"])
        elif p.get("resets_in_seconds") is not None and zaman:
            yenilenme = zaman + timedelta(seconds=float(p["resets_in_seconds"]))
        else:
            yenilenme = None
        g = kota._yeni(zaman, pencere_adi(p.get("window_minutes"), rl.get("limit_id")),
                       float(u) if ok else None, yenilenme, "codex_rollout", kimlik,
                       kota._ondalik_basamak(u) if ok else 0)
        okuma.goruntuler.append(g)


def oku(yollar):
    okuma = CodexOkuma()
    okuma.dosyalar = dosyalari_bul(yollar)
    gorulen_olaylar = {}
    for dosya in okuma.dosyalar:
        kimlik = None
        meta = {}
        onceki = None
        for ham, satir in _satirlar(dosya, okuma):
            if not isinstance(satir, dict):
                okuma.bozuk_satir += 1
                continue
            tur, yuk = satir.get("type"), satir.get("payload")
            zaman = iso_ayristir(satir.get("timestamp"))
            if tur == "session_meta" and isinstance(yuk, dict):
                if kimlik is None:  # ilk meta bu dosyanın konuşmasıdır
                    kimlik = yuk.get("id") or kimlik
                    meta = {k: yuk.get(k) for k in ("id", "session_id", "forked_from_id",
                                                     "parent_thread_id", "cwd", "cli_version",
                                                     "originator", "timestamp")}
                continue
            if tur != "event_msg" or not isinstance(yuk, dict) or yuk.get("type") != "token_count":
                continue
            if kimlik is None:
                taban = os.path.splitext(os.path.basename(dosya))[0]
                kimlik = taban[-36:] if len(taban) >= 36 else taban
            konusma = okuma.konusmalar.setdefault(kimlik, CodexKonusma(kimlik))
            if dosya not in konusma.dosyalar:
                konusma.dosyalar.append(dosya)
            if meta and not konusma.meta:
                konusma.meta = meta
            anahtar = (satir.get("timestamp"), json.dumps(yuk.get("info"), sort_keys=True),
                       json.dumps(yuk.get("rate_limits"), sort_keys=True))
            info = yuk.get("info") if isinstance(yuk.get("info"), dict) else None
            toplam = _usage(info.get("total_token_usage")) if info else None
            if anahtar in gorulen_olaylar:
                # Aynı olay ikinci kez: aynı dosyada tekrar yazılmış ya da çatal
                # (fork) dosyasına kopyalanmış. Sayılmaz, birikimli taban güncellenir.
                if gorulen_olaylar[anahtar] == dosya:
                    okuma.tekrar_olay += 1
                else:
                    okuma.kopya_olay += 1
                if toplam:
                    onceki = toplam
                continue
            gorulen_olaylar[anahtar] = dosya
            konusma.olay_sayisi += 1
            _rate_limits(yuk.get("rate_limits"), zaman, kimlik, okuma)
            if not toplam:
                continue
            if onceki is None:
                artis = dict(toplam)
            elif toplam["total_tokens"] >= onceki["total_tokens"]:
                artis = {a: max(0, toplam[a] - onceki[a]) for a in ALANLAR}
            else:
                konusma.sayac_sifirlanma += 1
                artis = _usage(info.get("last_token_usage")) or {a: 0 for a in ALANLAR}
            if onceki is not None and toplam == onceki:
                okuma.degismeyen_olay += 1
            konusma.ekle(artis, zaman)
            onceki = toplam
    okuma.goruntuler = kota.cozunurluk_cikar(okuma.goruntuler)
    return okuma


def konusma_bul(okuma, kimlik):
    if kimlik in okuma.konusmalar:
        return okuma.konusmalar[kimlik]
    adaylar = [k for k in okuma.konusmalar if k.startswith(kimlik)]
    if len(adaylar) == 1:
        return okuma.konusmalar[adaylar[0]]
    if not adaylar:
        raise KeyError(f"'{kimlik}' ile eşleşen Codex konuşması yok")
    raise KeyError(f"'{kimlik}' birden çok Codex konuşmasıyla eşleşiyor: {', '.join(sorted(adaylar))}")


def _pencere_paragrafi(ad, durum, simdi, tz):
    etiket = pencere_etiketi(ad)
    g = durum["goruntu"]
    yas = sure_metni(max(0.0, (simdi - g.zaman).total_seconds())) if g.zaman else "?"
    kaynak_tarihi = tarih_saat(g.zaman, tz) if g.zaman else "bilinmiyor"
    if g.kullanilan is None:
        metin = f"{etiket} kota (hesap geneli): kullanım yüzdesi erişilemiyor."
    else:
        metin = (f"{etiket} kota (hesap geneli, ölçüldü, Codex rollout kaydından): yüzde "
                 f"{yuzde(g.kullanilan)} kullanıldı, yüzde {yuzde(g.kalan)} kaldı.")
    metin += (f" Kaynağın tarihi: {kaynak_tarihi} ({yas} önce). Bu değer o anki hesap "
              f"durumudur; o andan sonraki kullanım bu kayıtta yoktur.")
    metin += (f" Yenilenme zamanı: {tarih_saat(g.yenilenme, tz)}." if g.yenilenme
              else " Yenilenme zamanı: erişilemiyor.")
    if durum["bayat"]:
        metin += (" Dikkat: yenilenme zamanı geçti; şu anki değer daha düşük olabilir, "
                  "güncel değer erişilemiyor.")
    if durum["eski_dusuk"]:
        metin += (f" Daha sonra gelen {len(durum['eski_dusuk'])} kayıt daha düşük gösterdi; "
                  f"bunlar başka oturumların eskimiş değerleri sayıldı.")
    return metin


def metin_raporu(okuma, konusma, simdi, tz):
    p = []
    m = konusma.meta or {}
    baslik = f"Codex kullanım raporu, konuşma {konusma.kimlik}."
    if konusma.ilk:
        baslik += (f" İlk sayaç olayı {tarih_saat(konusma.ilk, tz)}, son "
                   f"{tarih_saat(konusma.son, tz)}.")
    if m.get("forked_from_id"):
        baslik += f" Bu konuşma {m['forked_from_id']} konuşmasından çatallanmış."
    if m.get("parent_thread_id"):
        baslik += f" Alt ajan konuşması; ana konuşma {m['parent_thread_id']}."
    baslik += f" Saatler {tz} saat dilimindedir."
    p.append(baslik)
    s = konusma.sayac
    if s.get("total_tokens"):
        metin = (f"Bu konuşmanın token kullanımı (ölçüldü, Codex rollout kaydındaki birikimli "
                 f"sayaçtan): girdi {sayi(s.get('input_tokens', 0))} (bunun "
                 f"{sayi(s.get('cached_input_tokens', 0))} kadarı önbellekten), çıktı "
                 f"{sayi(s.get('output_tokens', 0))} (bunun {sayi(s.get('reasoning_output_tokens', 0))} "
                 f"kadarı akıl yürütme). Toplam {sayi(s.get('total_tokens', 0))} token.")
        if konusma.sayac_sifirlanma:
            metin += (f" Birikimli sayaç {konusma.sayac_sifirlanma} kez geriye düştü; o "
                      f"turlarda yalnız son turun değeri eklendi, toplam alt sınır olabilir.")
    else:
        metin = ("Bu konuşmanın token kullanımı: erişilemiyor; kayıtta dolu bir "
                 "token_count bilgisi yok.")
    p.append(metin)
    pencereler = sorted({g.pencere for g in okuma.goruntuler},
                        key=lambda a: (a.split("[")[0] != "codex_five_hour",
                                       a.split("[")[0] != "codex_seven_day", a))
    if not any(a.startswith("codex_seven_day") for a in pencereler):
        p.append("Codex haftalık kota: erişilemiyor. Okunan rollout kayıtlarında haftalık "
                 "(10080 dakikalık) pencere bilgisi yok.")
    for ad in pencereler:
        durum = kota.guncel_durum(okuma.goruntuler, ad, simdi)
        if durum:
            p.append(_pencere_paragrafi(ad, durum, simdi, tz))
        if konusma.ilk:
            # İlk sayaç olayı o turun tüketimini zaten içerir; başlangıç ondan hemen öncedir.
            f = kota.fark_hesapla(okuma.goruntuler, ad, konusma.ilk - timedelta(milliseconds=1),
                                  konusma.son, simdi)
            if f.durum in ("olculdu", "ilk_istek_sonrasi"):
                diger = [k for k, c in okuma.konusmalar.items() if k != konusma.kimlik and c.ilk
                         and c.ilk <= f.sonra.zaman and c.son >= f.once.zaman]
                metin = (f"Konuşma süresince {pencere_etiketi(ad)} kotadaki hesap geneli değişim: "
                         f"yüzde {yuzde(f.once.kullanilan)} → yüzde {yuzde(f.sonra.kullanilan)}"
                         f" ({yuzde(f.alt)}–{yuzde(f.ust)} puan aralığında). Bu yalnız bu "
                         f"konuşmanın tüketimi değildir.")
                if diger:
                    metin += f" Bu aralıkta {len(diger)} başka Codex konuşması da sayaç yazdı."
                p.append(metin)
            elif f.durum == "yenilendi":
                p.append(f"Konuşma süresince {pencere_etiketi(ad)} kotadaki değişim: "
                         f"hesaplanamıyor; arada pencere yenilendi.")
    if okuma.planlar:
        p.append(f"Plan türü (kayıttan): {', '.join(sorted(okuma.planlar))}.")
    p.append(f"Okuma denetimi: {len(okuma.dosyalar)} rollout dosyası okundu; "
             f"{okuma.tekrar_olay} tekrar ve {okuma.kopya_olay} çatal kopyası sayaç olayı "
             f"yeniden sayılmadı"
             + (f", {okuma.bozuk_satir} bozuk satır atlandı" if okuma.bozuk_satir else "")
             + (f", yazılmakta olan {okuma.yarim_son_satir} yarım son satır atlandı"
                if okuma.yarim_son_satir else "") + ".")
    p.append("Kapsam: bu rapor yalnız Codex CLI'nin yerel rollout kayıtlarına dayanır; "
             "ChatGPT web/mobil kullanımı ve auth/kimlik bilgisi okunmaz. Codex kotası "
             "Claude kotasından ayrıdır ve birbirine eklenmez.")
    return "\n\n".join(p) + "\n"
