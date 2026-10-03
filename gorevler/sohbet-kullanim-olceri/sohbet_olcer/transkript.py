"""Claude Code oturum transkriptlerinden (JSONL) token kullanımını okur.

Bilinen tuzaklar ve çözümleri:

* Claude Code tek bir API yanıtını içerik bloğu başına ayrı satır olarak yazar
  ve her satır aynı ``usage`` nesnesini tekrarlar (anthropics/claude-code
  #6805, #41346, #87303). Bu yüzden istekler ``message.id`` ile (yoksa
  ``requestId``, o da yoksa ``uuid``) tekilleştirilir; tekrar eden kayıtlarda
  her alanın en büyük değeri alınır.
* Devam ettirilen (resume/fork) oturumlar önceki mesajları yeni dosyaya
  kopyalayabilir; aynı anahtar birden çok dosyada görülürse en erken
  zaman damgalı kayıt esas alınır ve tekrar sayısı raporlanır.
* Dosya o anda başka bir süreç tarafından yazılıyor olabilir: yarım kalmış
  son satır ve bozuk satırlar atlanır, sayıları raporlanır.
"""

import glob
import json
import os
from dataclasses import dataclass, field
from datetime import datetime, timezone

from .zaman import iso_ayristir

ALANLAR = ("input_tokens", "output_tokens", "cache_creation_input_tokens",
           "cache_read_input_tokens", "thinking_tokens")


@dataclass
class Istek:
    anahtar: str
    oturum: str
    zaman: object
    model: str
    yan_zincir: bool
    dosya: str
    proje: str = ""
    kayit_uuid: str = ""
    sayac: dict = field(default_factory=dict)

    @property
    def toplam(self):
        return sum(self.sayac.get(a, 0) for a in ALANLAR if a != "thinking_tokens")


@dataclass
class OkumaSonucu:
    istekler: dict = field(default_factory=dict)        # anahtar -> Istek
    dosyalar: list = field(default_factory=list)
    bozuk_satir: int = 0
    yarim_son_satir: int = 0
    tekrar_satir: int = 0          # aynı istek için fazladan satır (aynı dosya)
    dosyalar_arasi_tekrar: int = 0  # aynı istek başka dosyada da var
    kayitlar: list = field(default_factory=list)       # günlük için ham kayıtlar
    ayni_kimlik_farkli_proje: dict = field(default_factory=dict)  # kimlik -> projeler
    ayni_dosyada_coklu_konusma: dict = field(default_factory=dict)  # kimlik -> konuşma sayısı
    kopuk_zincir: int = 0  # ebeveyni bulunamayan istek (ilk konuşmaya sayıldı)
    _kok_sirasi: dict = field(default_factory=dict)   # (dosya, kimlik) -> {kök: sıra}
    _ebeveynler: dict = field(default_factory=dict)   # dosya -> {uuid: ebeveyn}


def proje_dizini(dosya):
    """Transkriptin ait olduğu proje dizini. Alt ajan dosyaları
    <proje>/<oturum>/subagents/x.jsonl altında durur."""
    parcalar = os.path.normpath(os.path.abspath(dosya)).split(os.sep)
    if "subagents" in parcalar:
        i = len(parcalar) - 1 - parcalar[::-1].index("subagents")
        return os.sep.join(parcalar[: i - 1])
    return os.path.dirname(os.path.abspath(dosya))


def _usage_sayilari(usage):
    sayac = {}
    for a in ALANLAR[:-1]:
        v = usage.get(a)
        if isinstance(v, (int, float)) and not isinstance(v, bool):
            sayac[a] = int(v)
    detay = usage.get("output_tokens_details") or {}
    v = detay.get("thinking_tokens") if isinstance(detay, dict) else None
    if isinstance(v, (int, float)) and not isinstance(v, bool):
        sayac["thinking_tokens"] = int(v)
    return sayac


def transkript_dosyalari(yollar):
    """Verilen dosya/dizin listesinden .jsonl dosyalarını (alt ajan
    dosyaları dahil) toplar."""
    sonuc = []
    for yol in yollar:
        yol = os.path.expanduser(yol)
        if os.path.isdir(yol):
            sonuc.extend(sorted(glob.glob(os.path.join(yol, "**", "*.jsonl"),
                                          recursive=True)))
        elif os.path.isfile(yol):
            sonuc.append(yol)
            # Alt ajan transkriptleri: <oturum>/subagents/*.jsonl
            kok = os.path.splitext(yol)[0]
            if os.path.isdir(kok):
                sonuc.extend(sorted(glob.glob(os.path.join(kok, "**", "*.jsonl"),
                                              recursive=True)))
    goruldu, tekil = set(), []
    for s in sonuc:
        g = os.path.realpath(s)
        if g not in goruldu:
            goruldu.add(g)
            tekil.append(s)
    return tekil


def _satirlar(dosya, sonuc):
    with open(dosya, "rb") as f:
        veri = f.read()
    parcalar = veri.split(b"\n")
    son_tam = veri.endswith(b"\n")
    for i, ham in enumerate(parcalar):
        if not ham.strip():
            continue
        try:
            yield json.loads(ham.decode("utf-8"))
        except (ValueError, UnicodeDecodeError):
            if i == len(parcalar) - 1 and not son_tam:
                sonuc.yarim_son_satir += 1
            else:
                sonuc.bozuk_satir += 1


def oku(yollar, kayitlari_tut=False):
    sonuc = OkumaSonucu()
    sonuc.dosyalar = transkript_dosyalari(yollar)
    for dosya in sonuc.dosyalar:
        dosyada_gorulen = set()
        ebeveyn, yeni_istekler = {}, []
        for kayit in _satirlar(dosya, sonuc):
            if not isinstance(kayit, dict):
                sonuc.bozuk_satir += 1
                continue
            if kayit.get("uuid") and "parentUuid" in kayit and not kayit.get("isSidechain"):
                ebeveyn[kayit["uuid"]] = kayit.get("parentUuid") or kayit.get("logicalParentUuid")
            if kayitlari_tut:
                sonuc.kayitlar.append((dosya, kayit))
            if kayit.get("type") != "assistant":
                continue
            mesaj = kayit.get("message") or {}
            usage = mesaj.get("usage")
            if not isinstance(usage, dict):
                continue
            sayac = _usage_sayilari(usage)
            if not any(sayac.values()):
                continue  # sentetik / hata mesajları
            anahtar = mesaj.get("id") or kayit.get("requestId") or kayit.get("uuid")
            if not anahtar:
                continue
            zaman = iso_ayristir(kayit.get("timestamp"))
            mevcut = sonuc.istekler.get(anahtar)
            if mevcut is None:
                sonuc.istekler[anahtar] = Istek(
                    anahtar=anahtar,
                    oturum=kayit.get("sessionId") or os.path.splitext(os.path.basename(dosya))[0],
                    zaman=zaman, model=mesaj.get("model") or "?",
                    yan_zincir=bool(kayit.get("isSidechain")) or "subagents" in dosya.split(os.sep),
                    dosya=dosya, proje=proje_dizini(dosya), kayit_uuid=kayit.get("uuid") or "",
                    sayac=sayac)
                dosyada_gorulen.add(anahtar)
                yeni_istekler.append(sonuc.istekler[anahtar])
                continue
            if anahtar in dosyada_gorulen:
                sonuc.tekrar_satir += 1
            else:
                sonuc.dosyalar_arasi_tekrar += 1
                dosyada_gorulen.add(anahtar)
                if zaman and mevcut.zaman and zaman < mevcut.zaman:
                    mevcut.oturum = kayit.get("sessionId") or mevcut.oturum
                    mevcut.dosya = dosya
                    mevcut.proje = proje_dizini(dosya)
            for a, v in sayac.items():
                if v > mevcut.sayac.get(a, 0):
                    mevcut.sayac[a] = v
            if zaman and (mevcut.zaman is None or zaman < mevcut.zaman):
                mevcut.zaman = zaman
        _konusmalari_ayir(sonuc, ebeveyn, yeni_istekler, dosya)
        if kayitlari_tut:
            sonuc._ebeveynler[dosya] = ebeveyn
    _ayni_kimligi_ayir(sonuc)
    if kayitlari_tut:
        sonuc.kayitlar = [(d, k, _kayit_konusmasi(sonuc, d, k)) for d, k in sonuc.kayitlar]
    return sonuc


def _kayit_konusmasi(sonuc, dosya, kayit):
    """Ham bir kaydın (kullanıcı mesajı, araç çağrısı vb.) ait olduğu
    konuşma adı; isteklerle aynı kurallar (#sıra, @proje) uygulanır."""
    kimlik = kayit.get("sessionId") or os.path.splitext(os.path.basename(dosya))[0]
    ad = kimlik
    sira = sonuc._kok_sirasi.get((dosya, kimlik))
    if sira and not kayit.get("isSidechain"):
        kok = _kok(kayit.get("uuid"), sonuc._ebeveynler.get(dosya, {}), {}) if kayit.get("uuid") else None
        ad = f"{kimlik}#{sira.get(kok, 1)}"
    elif sira:
        ad = f"{kimlik}#1"
    if ad in sonuc.ayni_kimlik_farkli_proje:
        ad = f"{ad}@{os.path.basename(proje_dizini(dosya))}"
    return ad


def _kok(uuid, ebeveyn, bellek):
    """parentUuid zincirini köke kadar izler. Sıkıştırma (compact) sınırı
    logicalParentUuid ile önceki zincire bağlandığı için konuşma bölünmez.
    Zincir kopuksa (ebeveyn dosyada yok) None döner."""
    yol, u = [], uuid
    while True:
        if u in bellek:
            sonuc = bellek[u]
            break
        if u not in ebeveyn:
            sonuc = None
            break
        yol.append(u)
        if not ebeveyn[u]:
            sonuc = u
            break
        u = ebeveyn[u]
        if len(yol) > 1_000_000:
            sonuc = None
            break
    for y in yol:
        bellek[y] = sonuc
    return sonuc


def _konusmalari_ayir(sonuc, ebeveyn, istekler, dosya=None):
    """Aynı dosyada aynı oturum kimliğiyle birden çok bağımsız konuşma
    olabilir: bu ortamda eşzamanlı iki ``claude -p`` aynı kimliği alıp aynı
    dosyaya iç içe yazdı (gerçek gözlem). Her konuşmanın parentUuid
    zinciri ayrı bir köke çıkar; kök sayısı birden fazlaysa istekler
    ``<kimlik>#<sıra>`` olarak ayrılır. Alt ajan (sidechain) istekleri ana
    oturumda kalır."""
    bellek, gruplar = {}, {}
    for istek in istekler:
        if istek.yan_zincir or not istek.kayit_uuid:
            continue
        kok = _kok(istek.kayit_uuid, ebeveyn, bellek)
        gruplar.setdefault(istek.oturum, {}).setdefault(kok, []).append(istek)
    for kimlik, koklar in gruplar.items():
        gercek = {k: v for k, v in koklar.items() if k is not None}
        if len(gercek) <= 1:
            continue
        en_gec = datetime.max.replace(tzinfo=timezone.utc)
        sirali = sorted(gercek.items(), key=lambda kv: min(
            (i.zaman for i in kv[1] if i.zaman), default=en_gec))
        sonuc.ayni_dosyada_coklu_konusma[kimlik] = len(sirali)
        sonuc._kok_sirasi[(dosya, kimlik)] = {kok: n for n, (kok, _) in enumerate(sirali, 1)}
        for n, (_, liste) in enumerate(sirali, 1):
            for istek in liste:
                istek.oturum = f"{kimlik}#{n}"
        for istek in koklar.get(None, []):
            sonuc.kopuk_zincir += 1
            istek.oturum = f"{kimlik}#1"


def _ayni_kimligi_ayir(sonuc):
    """Aynı oturum kimliği farklı proje dizinlerinde görülürse bunlar ayrı
    konuşmalardır: örneğin bir oturumun içinden başlatılan ``claude -p``,
    ortamdaki CLAUDE_CODE_SESSION_ID'yi devralıp başka bir proje dizinine
    aynı kimlikle yazar (bu ortamda gerçek olarak gözlendi). Bunlar
    ``<kimlik>@<proje-dizini-adı>`` biçiminde ayrılır."""
    projeler = {}
    for istek in sonuc.istekler.values():
        projeler.setdefault(istek.oturum, set()).add(istek.proje)
    for kimlik, kume in projeler.items():
        if len(kume) > 1:
            sonuc.ayni_kimlik_farkli_proje[kimlik] = sorted(kume)
    for istek in sonuc.istekler.values():
        if istek.oturum in sonuc.ayni_kimlik_farkli_proje:
            istek.oturum = f"{istek.oturum}@{os.path.basename(istek.proje)}"


@dataclass
class OturumOzeti:
    oturum: str
    istek_sayisi: int = 0
    ilk: object = None
    son: object = None
    sayac: dict = field(default_factory=dict)
    yan_zincir_sayac: dict = field(default_factory=dict)
    yan_zincir_istek: int = 0
    modeller: dict = field(default_factory=dict)

    @property
    def toplam(self):
        return sum(self.sayac.get(a, 0) for a in ALANLAR if a != "thinking_tokens")


def oturumlara_ayir(okuma, baslangic=None, bitis=None):
    """İstekleri oturum kimliğine göre toplar; isteğe bağlı zaman süzgeci."""
    ozetler = {}
    for istek in okuma.istekler.values():
        if baslangic and istek.zaman and istek.zaman < baslangic:
            continue
        if bitis and istek.zaman and istek.zaman > bitis:
            continue
        oz = ozetler.setdefault(istek.oturum, OturumOzeti(istek.oturum))
        oz.istek_sayisi += 1
        for a, v in istek.sayac.items():
            oz.sayac[a] = oz.sayac.get(a, 0) + v
            if istek.yan_zincir:
                oz.yan_zincir_sayac[a] = oz.yan_zincir_sayac.get(a, 0) + v
        if istek.yan_zincir:
            oz.yan_zincir_istek += 1
        oz.modeller[istek.model] = oz.modeller.get(istek.model, 0) + 1
        if istek.zaman:
            oz.ilk = istek.zaman if oz.ilk is None else min(oz.ilk, istek.zaman)
            oz.son = istek.zaman if oz.son is None else max(oz.son, istek.zaman)
    return ozetler


def oturum_bul(ozetler, kimlik):
    """Tam ya da önek eşleşmesiyle oturum bulur. Belirsizse hata verir."""
    if kimlik in ozetler:
        return ozetler[kimlik]
    adaylar = [k for k in ozetler if k.startswith(kimlik)]
    if len(adaylar) == 1:
        return ozetler[adaylar[0]]
    if not adaylar:
        raise KeyError(f"'{kimlik}' ile eşleşen oturum transkriptte yok")
    raise KeyError(f"'{kimlik}' birden çok oturumla eşleşiyor: {', '.join(sorted(adaylar))}")
