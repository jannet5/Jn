"""Türkçe, maddeler arası boşluklu, düz anlatımlı kullanım raporu."""

from datetime import timedelta

from . import kota as kota_mod
from .kota import PENCERE_ADLARI
from .zaman import sure_metni, tarih_saat

ANA_PENCERELER = ("five_hour", "seven_day")


def sayi(n):
    """Türkçe binlik ayırıcı: 1234567 → 1.234.567"""
    return f"{int(n):,}".replace(",", ".")


def yuzde(x):
    """Yüzdeyi gereksiz ondalık olmadan yazar: 61.0 → 61, 0.5 → 0,5"""
    if x is None:
        return "?"
    yuv = round(x, 2)
    metin = f"{yuv:.2f}".rstrip("0").rstrip(".")
    return metin.replace(".", ",")


def kisaltilmis(n):
    if n >= 1_000_000:
        return f"yaklaşık {n / 1_000_000:.1f} milyon".replace(".", ",")
    if n >= 10_000:
        return f"yaklaşık {n / 1000:.0f} bin"
    return sayi(n)


def kisa_ad(oturum):
    """Kimliğin ilk 8 karakteri; çakışan kimliklerde ayırt edici ek korunur."""
    for ayrac in ("@", "#"):
        if ayrac in oturum:
            kok, _, ek = oturum.partition(ayrac)
            return f"{kok[:8]}{ayrac}{ek}"
    return oturum[:8]


def es_zamanli_oturumlar(ozetler, hedef, bas, bit):
    """Hedef dışındaki, [bas, bit] aralığında isteği olan yerel oturumlar."""
    sonuc = []
    for k, oz in ozetler.items():
        if k == hedef or oz.ilk is None:
            continue
        if oz.ilk <= bit and oz.son >= bas:
            sonuc.append(oz)
    return sonuc


def pencere_tokenleri(okuma, bas, bit):
    toplam, oturum_basi = 0, {}
    for istek in okuma.istekler.values():
        if istek.zaman and bas <= istek.zaman <= bit:
            toplam += istek.toplam
            oturum_basi[istek.oturum] = oturum_basi.get(istek.oturum, 0) + istek.toplam
    return toplam, oturum_basi


def rapor_verisi(okuma, ozetler, oturum, goruntuler, simdi, kota_kaynagi_var):
    veri = {"oturum": oturum.oturum, "istek_sayisi": oturum.istek_sayisi,
            "ilk": oturum.ilk, "son": oturum.son, "sayac": dict(oturum.sayac),
            "yan_zincir_istek": oturum.yan_zincir_istek,
            "yan_zincir_sayac": dict(oturum.yan_zincir_sayac),
            "modeller": dict(oturum.modeller), "toplam": oturum.toplam,
            "okuma": {"dosya": len(okuma.dosyalar), "bozuk_satir": okuma.bozuk_satir,
                      "yarim_son_satir": okuma.yarim_son_satir,
                      "tekrar_satir_ayiklanan": okuma.tekrar_satir,
                      "dosyalar_arasi_tekrar": okuma.dosyalar_arasi_tekrar},
            "kota_kaynagi_var": kota_kaynagi_var, "pencereler": {}, "farklar": {}}
    adlar = list(ANA_PENCERELER) + sorted({g.pencere for g in goruntuler} - set(ANA_PENCERELER))
    for p in adlar:
        veri["pencereler"][p] = kota_mod.guncel_durum(goruntuler, p, simdi)
        if oturum.ilk is not None:
            # Bitiş = son isteğin tamamlanması; istek zaman damgası yanıtın
            # ilk bloğunun yazıldığı an olduğundan küçük bir pay bırakılır.
            veri["farklar"][p] = kota_mod.fark_hesapla(
                goruntuler, p, oturum.ilk - timedelta(seconds=1), oturum.son, simdi)
    return veri


def _token_paragrafi(v):
    s = v["sayac"]
    dusunme = s.get("thinking_tokens")
    cikti = f"çıktı {sayi(s.get('output_tokens', 0))}"
    if dusunme:
        cikti += f" (bunun {sayi(dusunme)} kadarı düşünme)"
    metin = (f"Bu sohbetin token kullanımı (ölçüldü, Claude Code transkriptinden): "
             f"{sayi(v['istek_sayisi'])} API isteğinde yeni girdi {sayi(s.get('input_tokens', 0))}, "
             f"{cikti}, önbelleğe yazma {sayi(s.get('cache_creation_input_tokens', 0))}, "
             f"önbellekten okuma {sayi(s.get('cache_read_input_tokens', 0))} token. "
             f"Toplam {sayi(v['toplam'])} token ({kisaltilmis(v['toplam'])}).")
    if v["yan_zincir_istek"]:
        ys = v["yan_zincir_sayac"]
        yt = sum(ys.get(a, 0) for a in ("input_tokens", "output_tokens",
                                        "cache_creation_input_tokens", "cache_read_input_tokens"))
        metin += (f" Bunun {sayi(yt)} tokeni {sayi(v['yan_zincir_istek'])} alt ajan "
                  f"isteğinden geliyor.")
    metin += (" Not: önbellekten okunan tokenler kotaya daha düşük ağırlıkla yansıyabilir; "
              "Anthropic ağırlıkları yayımlamadığı için token toplamı doğrudan kota yüzdesine "
              "çevrilemez.")
    return metin


def _pencere_paragrafi(ad, durum, tz, kota_kaynagi_var):
    baslik = PENCERE_ADLARI.get(ad, ad).capitalize() + " kota"
    if durum is None:
        if not kota_kaynagi_var:
            return (f"{baslik}: erişilemiyor. Bu çalıştırmaya hiçbir kota kaynağı verilmedi "
                    f"(statusline kaydı, rate_limit_event dökümü ya da elle girilmiş "
                    f"Ayarlar → Kullanım değeri yok).")
        return f"{baslik}: erişilemiyor. Verilen kaynaklarda bu pencere için değer yok."
    g = durum["goruntu"]
    kaynak = {"statusline": "Claude Code statusline", "rate_limit_event": "rate_limit_event olayı",
              "elle": "elle girilen Ayarlar → Kullanım değeri",
              "opentokenusage": "OpenTokenUsage yerel API'si (gayriresmî uç noktadan)"}.get(g.kaynak, g.kaynak)
    zaman = tarih_saat(g.zaman, tz)
    if g.kullanilan is None:
        metin = (f"{baslik} (hesap geneli): kullanım yüzdesi erişilemiyor; kaynak yalnız "
                 f"durum ve yenilenme zamanını verdi ({kaynak}, {zaman}).")
    else:
        coz = (f" Değer {yuzde(g.cozunurluk)} puan çözünürlükle yuvarlanmış geliyor."
               if g.cozunurluk >= 1 else "")
        metin = (f"{baslik} (hesap geneli, ölçüldü, {kaynak}, {zaman} itibarıyla): "
                 f"yüzde {yuzde(g.kullanilan)} kullanıldı, yüzde {yuzde(g.kalan)} kaldı.{coz}")
    if g.yenilenme:
        metin += f" Yenilenme zamanı: {tarih_saat(g.yenilenme, tz)}."
    else:
        metin += " Yenilenme zamanı: erişilemiyor."
    if durum["bayat"]:
        metin += (" Dikkat: bu değerin yenilenme zamanı geçti; şu anki kota daha düşük "
                  "olabilir, güncel değer erişilemiyor.")
    if durum["eski_dusuk"]:
        metin += (f" Daha sonra gelen {len(durum['eski_dusuk'])} anlık görüntü daha düşük "
                  f"gösterdi; bunlar başka oturumların eskimiş değerleri sayıldı.")
    return metin


def _fark_paragrafi(ad, fark, v, okuma, ozetler, tz):
    adi = PENCERE_ADLARI.get(ad, ad)
    if fark is None or fark.durum == "yetersiz":
        return (f"Sohbet süresince {adi} kotadaki hesap geneli değişim: hesaplanamıyor; "
                f"sohbetin başı ve sonu için ayrı iki kota ölçümü yok.")
    if fark.durum == "yenilendi":
        return (f"Sohbet süresince {adi} kotadaki hesap geneli değişim: hesaplanamıyor; "
                f"iki ölçüm arasında pencere yenilendi ({tarih_saat(fark.once.yenilenme, tz)} "
                f"→ {tarih_saat(fark.sonra.yenilenme, tz)}).")
    bas, bit = fark.once.zaman, fark.sonra.zaman
    metin = (f"Sohbet süresince {adi} kotadaki hesap geneli değişim: "
             f"yüzde {yuzde(fark.once.kullanilan)} → yüzde {yuzde(fark.sonra.kullanilan)}, "
             f"yani {yuzde(fark.deger)} puan ({tarih_saat(bas, tz)} – {tarih_saat(bit, tz)}).")
    if fark.ust - fark.alt > 1e-9 and fark.once.cozunurluk >= 0.1:
        metin += (f" Sayaç yuvarlandığı için gerçek değişim {yuzde(fark.alt)} ile "
                  f"{yuzde(fark.ust)} puan arasında olabilir.")
    if fark.durum == "ilk_istek_sonrasi":
        metin += (" İlk ölçüm sohbetin ilk isteğinden sonra alındığı için o isteğin "
                  "tüketimi bu farka girmiyor; gerçek değişim bundan büyük olabilir.")
    if fark.son_eksik:
        metin += (" Son ölçüm sohbetin son isteğinden önce alındı; sonraki isteklerin "
                  "tüketimi bu farka girmiyor.")
    metin += " Bu değişim hesap geneli bir sayıdır, yalnız bu sohbetin tüketimi değildir."
    diger = es_zamanli_oturumlar(ozetler, v["oturum"], bas, bit)
    if diger:
        metin += (f" Bu aralıkta bu bilgisayardaki {len(diger)} başka oturum da istek gönderdi "
                  f"({', '.join(kisa_ad(o.oturum) for o in diger)}).")
    else:
        metin += " Bu aralıkta bu bilgisayarda başka oturum görünmüyor."
    metin += (" Başka cihazlardan, claude.ai web/mobil sohbetlerinden yapılan kullanım "
              "buradan görülemez.")
    return metin


def _pay_paragrafi(farklar, v, okuma, ozetler):
    metin = ("Bu sohbetin kota yüzdesindeki kendi payı: doğrudan ölçülemiyor. Anthropic "
             "oturum ya da sohbet başına kota payı vermiyor; elde yalnız hesap geneli "
             "yüzde var.")
    tahminler = []
    for ad, fark in farklar.items():
        if fark is None or fark.durum not in ("olculdu", "ilk_istek_sonrasi"):
            continue
        toplam, oturum_basi = pencere_tokenleri(okuma, fark.once.zaman, fark.sonra.zaman)
        bu = oturum_basi.get(v["oturum"], 0)
        if toplam == 0 or bu == 0:
            continue
        oran = bu / toplam
        tahminler.append(
            f"{PENCERE_ADLARI.get(ad, ad)} kotada yaklaşık {yuzde(fark.alt * oran)}–"
            f"{yuzde(fark.ust * oran)} puan"
            + ("" if oran > 0.999 else f" (yerel tokenlerin yüzde {yuzde(oran * 100)} kadarı bu sohbette)"))
    if tahminler:
        metin += (" TAHMİN (ölçüm değil): değişimi bu bilgisayardaki oturumlar arasında "
                  "token oranıyla paylaştırırsak " + "; ".join(tahminler) + ". "
                  "Bu tahmin başka cihaz kullanımı olmadığını ve tüm token türlerinin "
                  "kotaya eşit ağırlıkla yansıdığını varsayar; ikisi de doğrulanamaz.")
    return metin


def metin_raporu(v, okuma, ozetler, tz):
    p = []
    if v["ilk"]:
        sure = (v["son"] - v["ilk"]).total_seconds()
        if v["istek_sayisi"] == 1:
            p.append(f"Sohbet kullanım raporu, oturum {v['oturum']}. Tek istek, "
                     f"{tarih_saat(v['ilk'], tz)}. Saatler {tz} saat dilimindedir.")
        else:
            p.append(f"Sohbet kullanım raporu, oturum {v['oturum']}. İlk istek "
                     f"{tarih_saat(v['ilk'], tz)}, son istek {tarih_saat(v['son'], tz)} "
                     f"(yaklaşık {sure_metni(sure)}). Saatler {tz} saat dilimindedir.")
    else:
        p.append(f"Sohbet kullanım raporu, oturum {v['oturum']}.")
    p.append(_token_paragrafi(v))
    for ad, durum in v["pencereler"].items():
        if ad in ANA_PENCERELER or durum is not None:
            p.append(_pencere_paragrafi(ad, durum, tz, v["kota_kaynagi_var"]))
    for ad in ANA_PENCERELER:
        if v["pencereler"].get(ad) is not None:
            p.append(_fark_paragrafi(ad, v["farklar"].get(ad), v, okuma, ozetler, tz))
    p.append(_pay_paragrafi({k: f for k, f in v["farklar"].items() if k in ANA_PENCERELER},
                            v, okuma, ozetler))
    o = v["okuma"]
    p.append(f"Okuma denetimi: {o['dosya']} transkript dosyası okundu. Aynı API yanıtı için "
             f"tekrar yazılmış {sayi(o['tekrar_satir_ayiklanan'])} satır tekilleştirildi"
             + (f", {sayi(o['dosyalar_arasi_tekrar'])} istek başka dosyada da bulundu ve bir kez sayıldı"
                if o["dosyalar_arasi_tekrar"] else "")
             + (f", aynı oturum kimliği {sum(len(x) for x in okuma.ayni_kimlik_farkli_proje.values())} "
                f"farklı proje dizininde görüldü ve ayrı konuşma sayıldı"
                if okuma.ayni_kimlik_farkli_proje else "")
             + (f", aynı dosyada aynı kimlikle yazılmış "
                f"{sum(okuma.ayni_dosyada_coklu_konusma.values())} ayrı konuşma mesaj zincirinden ayrıldı"
                if okuma.ayni_dosyada_coklu_konusma else "")
             + (f", zinciri kopuk {okuma.kopuk_zincir} istek ilk konuşmaya sayıldı"
                if okuma.kopuk_zincir else "")
             + (f", {o['bozuk_satir']} bozuk satır atlandı" if o["bozuk_satir"] else "")
             + (f", yazılmakta olan {o['yarim_son_satir']} yarım son satır atlandı"
                if o["yarim_son_satir"] else "") + ".")
    p.append("Erişilemeyen ve kapsam dışı veriler: claude.ai web/mobil sohbetlerinin token "
             "sayısı (resmi bir sayaç ya da API yok), oturum başına kota payı ve dolar "
             "maliyeti bu raporda yoktur. Abonelik kotası dolar maliyetiyle ölçülmediği için "
             "maliyet tahmini yapılmadı.")
    return "\n\n".join(p) + "\n"


def json_raporu(v):
    def donustur(x):
        if hasattr(x, "isoformat"):
            return x.isoformat()
        if isinstance(x, kota_mod.Goruntu):
            return {"zaman": x.zaman.isoformat(), "pencere": x.pencere,
                    "kullanilan_yuzde": x.kullanilan, "kalan_yuzde": x.kalan,
                    "cozunurluk_puan": x.cozunurluk,
                    "yenilenme": x.yenilenme.isoformat() if x.yenilenme else None,
                    "kaynak": x.kaynak, "oturum": x.oturum}
        if isinstance(x, kota_mod.Fark):
            return {"durum": x.durum, "son_eksik": x.son_eksik, "deger": x.deger, "alt": x.alt, "ust": x.ust,
                    "once": donustur(x.once) if x.once else None,
                    "sonra": donustur(x.sonra) if x.sonra else None,
                    "not": "hesap geneli değişim; tek sohbetin tüketimi değildir"}
        if isinstance(x, dict):
            return {k: donustur(val) for k, val in x.items()}
        if isinstance(x, list):
            return [donustur(i) for i in x]
        return x
    return donustur(v)
