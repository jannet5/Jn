import json
import re
import multiprocessing
import os
import subprocess
import sys
import tempfile
import unittest
from datetime import datetime, timedelta, timezone

KOK = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.insert(0, KOK)

from sohbet_olcer import kota, rapor, transkript  # noqa: E402
from sohbet_olcer.zaman import epoch_ayristir  # noqa: E402

T0 = datetime(2026, 10, 3, 12, 0, tzinfo=timezone.utc)
RESET5 = int((T0 + timedelta(hours=4)).timestamp())
RESET7 = int((T0 + timedelta(days=6)).timestamp())


def ts(dakika, saniye=0):
    return (T0 + timedelta(minutes=dakika, seconds=saniye)).isoformat().replace("+00:00", "Z")


def asistan(oturum, mid, dakika, girdi=10, cikti=100, yazma=1000, okuma=5000,
            blok="text", yan=False, saniye=0, req=None):
    return {"type": "assistant", "sessionId": oturum, "isSidechain": yan,
            "requestId": req or f"req_{mid}", "timestamp": ts(dakika, saniye),
            "uuid": f"u-{mid}-{blok}-{dakika}-{saniye}",
            "message": {"id": mid, "model": "claude-test", "content": [{"type": blok}],
                        "usage": {"input_tokens": girdi, "output_tokens": cikti,
                                  "cache_creation_input_tokens": yazma,
                                  "cache_read_input_tokens": okuma}}}


def yaz(yol, kayitlar, son_satir_yarim=None):
    os.makedirs(os.path.dirname(yol), exist_ok=True)
    with open(yol, "w") as f:
        for k in kayitlar:
            f.write(json.dumps(k) + "\n")
        if son_satir_yarim:
            f.write(son_satir_yarim)


def ccr_olay(dakika, u5, u7, oturum="s", reset5=RESET5, reset7=RESET7):
    return {"created_at": ts(dakika), "rate_limit_event": {"internal_anthropic_catchall": {
        "session_id": oturum, "rate_limit_info": {
            "status": "allowed", "rateLimitType": "five_hour", "resetsAt": reset5,
            "unifiedWindows": {"five_hour": {"resetsAt": reset5, "utilization": u5},
                               "seven_day": {"resetsAt": reset7, "utilization": u7}}}}}}


class Tekillestirme(unittest.TestCase):
    def setUp(self):
        self.d = tempfile.mkdtemp()

    def test_ayni_yanit_birden_cok_satir_bir_kez_sayilir(self):
        yol = os.path.join(self.d, "a.jsonl")
        yaz(yol, [asistan("A", "m1", 0, blok="thinking"), asistan("A", "m1", 0, blok="text", saniye=1),
                  asistan("A", "m1", 0, blok="tool_use", saniye=2), asistan("A", "m2", 1)])
        ok = transkript.oku([yol])
        oz = transkript.oturumlara_ayir(ok)["A"]
        self.assertEqual(oz.istek_sayisi, 2)
        self.assertEqual(oz.sayac["output_tokens"], 200)
        self.assertEqual(oz.sayac["cache_read_input_tokens"], 10000)
        self.assertEqual(ok.tekrar_satir, 2)

    def test_akis_sirasinda_farkli_cikti_degerinde_en_buyuk_alinir(self):
        yol = os.path.join(self.d, "a.jsonl")
        yaz(yol, [asistan("A", "m1", 0, cikti=1), asistan("A", "m1", 0, cikti=350, saniye=3)])
        oz = transkript.oturumlara_ayir(transkript.oku([yol]))["A"]
        self.assertEqual(oz.sayac["output_tokens"], 350)

    def test_devam_ettirilen_oturum_kopyasi_cift_sayilmaz(self):
        yaz(os.path.join(self.d, "A.jsonl"), [asistan("A", "m1", 0), asistan("A", "m2", 1)])
        # B, A'nın geçmişini kopyalamış (resume) + kendi isteği
        yaz(os.path.join(self.d, "B.jsonl"), [asistan("B", "m1", 5), asistan("B", "m2", 5),
                                               asistan("B", "m3", 6)])
        ok = transkript.oku([self.d])
        oz = transkript.oturumlara_ayir(ok)
        self.assertEqual(oz["A"].istek_sayisi, 2)
        self.assertEqual(oz["B"].istek_sayisi, 1)
        self.assertEqual(ok.dosyalar_arasi_tekrar, 2)


class OturumAyirma(unittest.TestCase):
    def setUp(self):
        self.d = tempfile.mkdtemp()

    def test_ayni_kimlik_farkli_proje_dizini_ayri_konusma(self):
        # Gözlenen gerçek durum: oturum içinden başlatılan `claude -p`,
        # CLAUDE_CODE_SESSION_ID'yi devralıp başka proje dizinine yazar.
        yaz(os.path.join(self.d, "-home-user-Jn", "S.jsonl"), [asistan("S", "m1", 0), asistan("S", "m2", 2)])
        yaz(os.path.join(self.d, "-home-user-Jn-e2e", "S.jsonl"), [asistan("S", "h1", 1, cikti=54)])
        ok = transkript.oku([self.d])
        oz = transkript.oturumlara_ayir(ok)
        self.assertEqual(sorted(oz), ["S@-home-user-Jn", "S@-home-user-Jn-e2e"])
        self.assertEqual(oz["S@-home-user-Jn"].istek_sayisi, 2)
        self.assertEqual(oz["S@-home-user-Jn-e2e"].sayac["output_tokens"], 54)
        with self.assertRaises(KeyError):  # önek belirsiz → açık hata
            transkript.oturum_bul(oz, "S")
        # Kanca gibi tek dosya okunduğunda ek yoktur
        tek = transkript.oturumlara_ayir(transkript.oku([os.path.join(self.d, "-home-user-Jn", "S.jsonl")]))
        self.assertEqual(list(tek), ["S"])

    def test_ic_ice_gecmis_eszamanli_oturumlar_ayrilir(self):
        # Aynı dosyada bile iki farklı sessionId (örn. birleştirilmiş döküm)
        kayitlar = []
        for i in range(10):
            kayitlar.append(asistan("AAAA-1", f"a{i}", i, cikti=100))
            kayitlar.append(asistan("BBBB-2", f"b{i}", i, saniye=30, cikti=7))
        yaz(os.path.join(self.d, "karisik.jsonl"), kayitlar)
        oz = transkript.oturumlara_ayir(transkript.oku([self.d]))
        self.assertEqual(oz["AAAA-1"].sayac["output_tokens"], 1000)
        self.assertEqual(oz["BBBB-2"].sayac["output_tokens"], 70)

    def test_ayni_dosyada_ayni_kimlikle_iki_konusma_zincirden_ayrilir(self):
        def k(tur, uuid, ebeveyn, **ek):
            d = {"type": tur, "uuid": uuid, "parentUuid": ebeveyn, "sessionId": "S"}
            d.update(ek)
            return d
        a1, b1 = asistan("S", "ma", 1, cikti=11), asistan("S", "mb", 1, saniye=5, cikti=22)
        a1.update(uuid="a2", parentUuid="a1")
        b1.update(uuid="b2", parentUuid="b1")
        a3 = asistan("S", "ma3", 3, cikti=33)
        a3.update(uuid="a5", parentUuid="a4")
        yaz(os.path.join(self.d, "S.jsonl"), [
            k("user", "a1", None, timestamp=ts(0)), k("user", "b1", None, timestamp=ts(0, 1)), a1, b1,
            # sıkıştırma sınırı: yeni kök ama logicalParentUuid ile aynı konuşma
            k("system", "a4", None, logicalParentUuid="a2", subtype="compact_boundary"), a3])
        ok = transkript.oku([self.d])
        oz = transkript.oturumlara_ayir(ok)
        self.assertEqual(sorted(oz), ["S#1", "S#2"])
        self.assertEqual(oz["S#1"].sayac["output_tokens"], 44)
        self.assertEqual(oz["S#2"].sayac["output_tokens"], 22)
        self.assertEqual(ok.ayni_dosyada_coklu_konusma, {"S": 2})

    def test_gunluk_ayni_kimlikli_konusmalari_karistirmaz(self):
        from sohbet_olcer import gunluk
        def arac(uuid, ebeveyn, dk, komut):
            return {"type": "assistant", "uuid": uuid, "parentUuid": ebeveyn, "sessionId": "S",
                    "timestamp": ts(dk), "message": {"id": "m" + uuid, "content": [
                        {"type": "tool_use", "id": "t" + uuid, "name": "Bash", "input": {"command": komut}}],
                        "usage": {"input_tokens": 1, "output_tokens": 1}}}
        yaz(os.path.join(self.d, "S.jsonl"), [
            {"type": "user", "uuid": "a1", "parentUuid": None, "sessionId": "S", "timestamp": ts(0), "message": {"content": "x"}},
            {"type": "user", "uuid": "b1", "parentUuid": None, "sessionId": "S", "timestamp": ts(0, 1), "message": {"content": "y"}},
            arac("a2", "a1", 1, "echo birinci"), arac("b2", "b1", 2, "echo ikinci")])
        ok = transkript.oku([self.d], kayitlari_tut=True)
        self.assertEqual(gunluk.konusma_coz(ok, "S#2"), "S#2")
        with self.assertRaises(KeyError):
            gunluk.konusma_coz(ok, "S")
        metin = gunluk.gunluk(ok, "S#2", "UTC")
        self.assertIn("echo ikinci", metin)
        self.assertNotIn("echo birinci", metin)
        self.assertIn("\n\n", metin)

    def test_alt_ajan_dosyasi_ana_oturuma_eklenir(self):
        ana = os.path.join(self.d, "proj", "S1.jsonl")
        yaz(ana, [asistan("S1", "m1", 0)])
        yaz(os.path.join(self.d, "proj", "S1", "subagents", "agent-x.jsonl"),
            [asistan("S1", "sub1", 1, yan=True, cikti=55)])
        oz = transkript.oturumlara_ayir(transkript.oku([ana]))["S1"]
        self.assertEqual(oz.istek_sayisi, 2)
        self.assertEqual(oz.yan_zincir_istek, 1)
        self.assertEqual(oz.yan_zincir_sayac["output_tokens"], 55)

    def test_yarim_son_satir_ve_bozuk_satir_atlanir(self):
        yol = os.path.join(self.d, "a.jsonl")
        with open(yol, "w") as f:
            f.write(json.dumps(asistan("A", "m1", 0)) + "\n")
            f.write("{bozuk json\n")
            f.write(json.dumps(asistan("A", "m2", 1)) + "\n")
            f.write(json.dumps(asistan("A", "m3", 2))[:40])  # yazılmakta
        ok = transkript.oku([yol])
        self.assertEqual(ok.bozuk_satir, 1)
        self.assertEqual(ok.yarim_son_satir, 1)
        self.assertEqual(transkript.oturumlara_ayir(ok)["A"].istek_sayisi, 2)

    def test_belirsiz_onek_hata_verir(self):
        yaz(os.path.join(self.d, "x.jsonl"), [asistan("abc1", "m1", 0), asistan("abc2", "m2", 0)])
        oz = transkript.oturumlara_ayir(transkript.oku([self.d]))
        with self.assertRaises(KeyError):
            transkript.oturum_bul(oz, "abc")
        self.assertEqual(transkript.oturum_bul(oz, "abc2").oturum, "abc2")


class Yuvarlama(unittest.TestCase):
    def _g(self, olaylar):
        d = tempfile.mkdtemp()
        yol = os.path.join(d, "k.json")
        with open(yol, "w") as f:
            json.dump({"data": olaylar}, f)
        return kota.dosyadan_oku(yol)[0]

    def test_iki_ondalik_kesir_bir_puan_cozunurluk(self):
        g = self._g([ccr_olay(0, 0.3, 0.12), ccr_olay(10, 0.31, 0.12)])
        f5 = kota.fark_hesapla(g, "five_hour", T0, T0 + timedelta(minutes=10))
        self.assertEqual(f5.durum, "olculdu")
        self.assertAlmostEqual(f5.deger, 1.0)
        self.assertAlmostEqual(f5.alt, 0.0)
        self.assertAlmostEqual(f5.ust, 2.0)
        f7 = kota.fark_hesapla(g, "seven_day", T0, T0 + timedelta(minutes=10))
        self.assertAlmostEqual(f7.deger, 0.0)
        self.assertEqual((f7.alt, f7.ust), (0.0, 1.0))  # 0.6 puanlık bir iş 0 görünebilir

    def test_statusline_ondalikli_yuzde(self):
        g = self._g([{"kaynak": "statusline", "ts": ts(0), "session_id": "A",
                      "rate_limits": {"five_hour": {"used_percentage": 23.5, "resets_at": RESET5}}},
                     {"kaynak": "statusline", "ts": ts(9), "session_id": "A",
                      "rate_limits": {"five_hour": {"used_percentage": 24.1, "resets_at": RESET5}}}])
        f = kota.fark_hesapla(g, "five_hour", T0, T0 + timedelta(minutes=9))
        self.assertAlmostEqual(f.cozunurluk if hasattr(f, "cozunurluk") else f.once.cozunurluk, 0.1)
        self.assertAlmostEqual(f.alt, 0.5)
        self.assertAlmostEqual(f.ust, 0.7)

    def test_tam_sayi_statusline_bir_puan(self):
        g = self._g([{"kaynak": "statusline", "ts": ts(0), "rate_limits": {"seven_day": {"used_percentage": 40, "resets_at": RESET7}}},
                     {"kaynak": "statusline", "ts": ts(5), "rate_limits": {"seven_day": {"used_percentage": 41, "resets_at": RESET7}}}])
        self.assertEqual(g[0].cozunurluk, 1.0)

    def test_pencere_yenilenince_fark_hesaplanmaz(self):
        g = self._g([ccr_olay(0, 0.9, 0.5), ccr_olay(30, 0.05, 0.5, reset5=RESET5 + 5 * 3600)])
        f = kota.fark_hesapla(g, "five_hour", T0, T0 + timedelta(minutes=30))
        self.assertEqual(f.durum, "yenilendi")
        self.assertEqual(kota.fark_hesapla(g, "seven_day", T0, T0 + timedelta(minutes=30)).durum, "olculdu")

    def test_resets_at_milisaniye_ve_saniye(self):
        self.assertEqual(epoch_ayristir(1767225600), epoch_ayristir(1767225600000))

    def test_tek_olcum_fark_yetersiz(self):
        g = self._g([ccr_olay(5, 0.3, 0.1)])
        self.assertEqual(kota.fark_hesapla(g, "five_hour", T0, T0 + timedelta(minutes=10)).durum, "yetersiz")

    def test_ilk_olcum_ilk_istekten_sonra_ve_son_olcum_erken(self):
        g = self._g([ccr_olay(1, 0.3, 0.1), ccr_olay(3, 0.32, 0.1)])
        f = kota.fark_hesapla(g, "five_hour", T0, T0 + timedelta(minutes=10))
        self.assertEqual(f.durum, "ilk_istek_sonrasi")
        self.assertTrue(f.son_eksik)


def _rapor(d, kota_yollari, oturum, simdi=None):
    ok = transkript.oku([d])
    oz = transkript.oturumlara_ayir(ok)
    g = []
    for y in kota_yollari:
        g.extend(kota.dosyadan_oku(y)[0])
    v = rapor.rapor_verisi(ok, oz, oz[oturum], g, simdi or T0 + timedelta(hours=1), bool(kota_yollari))
    return v, rapor.metin_raporu(v, ok, oz, "Europe/Istanbul")


class Raporlama(unittest.TestCase):
    def setUp(self):
        self.d = tempfile.mkdtemp()
        self.t = os.path.join(self.d, "t")

    def test_kota_kaynagi_yoksa_erisilemiyor(self):
        yaz(os.path.join(self.t, "A.jsonl"), [asistan("A", "m1", 0)])
        v, metin = _rapor(self.t, [], "A")
        self.assertIn("5 saatlik kota: erişilemiyor", metin)
        self.assertIn("Haftalık kota: erişilemiyor", metin)
        self.assertNotIn("TAHMİN", metin)
        self.assertNotIn("%", metin)
        self.assertIn("\n\n", metin)  # maddeler arası boşluk

    def test_kullanim_yuzdesi_olmayan_olay_yalniz_yenilenme_verir(self):
        yaz(os.path.join(self.t, "A.jsonl"), [asistan("A", "m1", 0)])
        yol = os.path.join(self.d, "s.jsonl")
        with open(yol, "w") as f:  # SDK stream-json, utilization yok (allowed durumu)
            f.write(json.dumps({"type": "rate_limit_event", "timestamp": ts(1), "rate_limit_info": {
                "status": "allowed", "resetsAt": RESET5 * 1000, "rateLimitType": "five_hour"}}) + "\n")
        v, metin = _rapor(self.t, [yol], "A")
        self.assertIn("kullanım yüzdesi erişilemiyor", metin)
        self.assertIn("Yenilenme zamanı: 3 Ekim 2026 19:00", metin)
        self.assertIn("Haftalık kota: erişilemiyor", metin)

    def test_eszamanli_kullanim_hesap_geneli_ve_tahmin_ayri(self):
        kay = []
        for i in range(10):
            kay.append(asistan("A", f"a{i}", i + 1, cikti=0, girdi=0, yazma=0, okuma=3000))
            kay.append(asistan("B", f"b{i}", i + 1, saniye=20, cikti=0, girdi=0, yazma=0, okuma=1000))
        yaz(os.path.join(self.t, "x.jsonl"), kay)
        yol = os.path.join(self.d, "k.json")
        with open(yol, "w") as f:
            json.dump({"data": [ccr_olay(0, 0.40, 0.20, "A"), ccr_olay(12, 0.44, 0.21, "A"),
                                # B'nin eskimiş (daha düşük) anlık görüntüsü
                                ccr_olay(13, 0.42, 0.20, "B")]}, f)
        v, metin = _rapor(self.t, [yol], "A")
        self.assertIn("yüzde 44 kullanıldı, yüzde 56 kaldı", metin)
        self.assertIn("eskimiş", metin)
        self.assertIn("1 başka oturum", metin)
        self.assertIn("hesap geneli bir sayıdır, yalnız bu sohbetin tüketimi değildir", metin)
        self.assertIn("TAHMİN (ölçüm değil)", metin)
        self.assertIn("yüzde 75 kadarı bu sohbette", metin)  # 30000 / 40000
        # 4 ± 1 puan × 0,75 → 2,25–3,75
        self.assertIn("2,25–3,75 puan", metin)

    def test_bayat_kota_uyarisi(self):
        yaz(os.path.join(self.t, "A.jsonl"), [asistan("A", "m1", 0)])
        yol = os.path.join(self.d, "k.json")
        with open(yol, "w") as f:
            json.dump({"data": [ccr_olay(1, 0.5, 0.1)]}, f)
        v, metin = _rapor(self.t, [yol], "A", simdi=T0 + timedelta(hours=5))
        self.assertIn("yenilenme zamanı geçti", metin)

    def test_elle_girilen_ayarlar_degeri(self):
        yaz(os.path.join(self.t, "A.jsonl"), [asistan("A", "m1", 3)])
        kayit = os.path.join(self.d, "elle.jsonl")
        for dk, y in ((0, 30), (10, 31)):
            kota.kilitli_ekle(kayit, {"kaynak": "elle", "ts": ts(dk), "pencere": "seven_day",
                                      "yuzde": y, "cozunurluk": 1.0,
                                      "yenilenme": "2026-10-09T06:00:00+00:00"})
        v, metin = _rapor(self.t, [kayit], "A")
        self.assertIn("elle girilen Ayarlar → Kullanım değeri", metin)
        self.assertIn("yüzde 30 → yüzde 31", metin)
        self.assertIn("0 ile 2 puan", metin)


def _yazici(args):
    yol, kimlik = args
    for i in range(200):
        kota.kilitli_ekle(yol, {"kaynak": "statusline", "ts": ts(0, i % 60), "session_id": kimlik,
                                "rate_limits": {"five_hour": {"used_percentage": i % 100,
                                                              "resets_at": RESET5}},
                                "dolgu": "x" * 3000})


class DisKaynaklar(unittest.TestCase):
    def test_opentokenusage_yerel_api_belge_ornegi(self):
        # docs/local-http-api.md'deki yanıt biçimi (OTU 0.7.3, d5feca2)
        ornek = {"providerId": "claude", "displayName": "Claude", "plan": "Team 5x", "lines": [
            {"type": "progress", "label": "Session", "used": 42.0, "limit": 100.0,
             "format": {"kind": "percent"}, "resetsAt": "2026-03-26T13:00:00.161Z",
             "periodDurationMs": 18000000, "color": None},
            {"type": "progress", "label": "Weekly", "used": 7.0, "limit": 100.0,
             "format": {"kind": "percent"}, "resetsAt": "2026-03-30T08:00:00Z"},
            {"type": "text", "label": "Today", "value": "$5.17 · 9.2M tokens"}]}
        d = tempfile.mkdtemp()
        yol = os.path.join(d, "otu.json")
        with open(yol, "w") as f:
            json.dump(ornek, f)
        g, _ = kota.dosyadan_oku(yol)
        p = {x.pencere: x for x in g}
        self.assertEqual(set(p), {"five_hour", "seven_day"})
        self.assertEqual(p["five_hour"].kullanilan, 42.0)
        self.assertEqual(p["five_hour"].cozunurluk, 1.0)
        self.assertEqual(p["seven_day"].kaynak, "opentokenusage")
        self.assertEqual(p["seven_day"].yenilenme.isoformat(), "2026-03-30T08:00:00+00:00")

    def test_saat_dilimi_veritabani_yoksa_istanbul_sabit(self):
        from sohbet_olcer import zaman
        eski = zaman.ZoneInfo
        try:
            def yok(_):
                raise zaman.ZoneInfoNotFoundError("tzdata yok")
            zaman.ZoneInfo = yok
            self.assertEqual(zaman.tarih_saat(T0, "Europe/Istanbul"), "3 Ekim 2026 15:00")
        finally:
            zaman.ZoneInfo = eski


class Eszamanlilik(unittest.TestCase):
    def test_sekiz_surec_ayni_kayda_yazar_satir_bozulmaz(self):
        d = tempfile.mkdtemp()
        yol = os.path.join(d, "kota.jsonl")
        with multiprocessing.Pool(8) as p:
            p.map(_yazici, [(yol, f"S{i}") for i in range(8)])
        g, bozuk = kota.dosyadan_oku(yol)
        self.assertEqual(bozuk, 0)
        self.assertEqual(len(g), 1600)
        self.assertEqual({x.oturum for x in g}, {f"S{i}" for i in range(8)})


class KomutSatiri(unittest.TestCase):
    def _calistir(self, args, girdi=None):
        return subprocess.run([sys.executable, "-m", "sohbet_olcer"] + args, cwd=KOK,
                              input=girdi, capture_output=True, text=True, encoding="utf-8", timeout=60)

    def test_statusline_kaydet(self):
        d = tempfile.mkdtemp()
        kayit = os.path.join(d, "k.jsonl")
        r = self._calistir(["kaydet", "--kayit", kayit], json.dumps({
            "session_id": "S", "rate_limits": {"five_hour": {"used_percentage": 12.5, "resets_at": RESET5},
                                               "seven_day": {"used_percentage": 3, "resets_at": RESET7}}}))
        self.assertEqual(r.returncode, 0)
        self.assertEqual(r.stdout.strip(), "kota 5s %12,5 · 7g %3")
        r = self._calistir(["kaydet", "--kayit", kayit], json.dumps({"session_id": "S"}))
        self.assertEqual(r.stdout.strip(), "kota: erişilemiyor")
        self.assertEqual(len(kota.dosyadan_oku(kayit)[0]), 2)

    def test_stop_kancasi_systemmessage(self):
        d = tempfile.mkdtemp()
        t = os.path.join(d, "S9.jsonl")
        yaz(t, [asistan("S9", "m1", 0), asistan("S9", "m1", 0, saniye=1)])
        r = self._calistir(["kanca", "--kayit", os.path.join(d, "yok.jsonl"), "--cikti", d],
                           json.dumps({"session_id": "S9", "transcript_path": t}))
        self.assertEqual(r.returncode, 0, r.stderr)
        mesaj = json.loads(r.stdout)["systemMessage"]
        self.assertIn("Bu sohbet: 6.110 token (1 istek)", mesaj)
        self.assertIn("5 saatlik kota: erişilemiyor", mesaj)
        self.assertTrue(os.path.exists(os.path.join(d, "S9.txt")))

    def test_uzlastir_api_sonucu_ile_transkript(self):
        d = tempfile.mkdtemp()
        t = os.path.join(d, "t")
        yaz(os.path.join(t, "S.jsonl"), [asistan("S", "m1", 0, blok="thinking"), asistan("S", "m1", 0, blok="tool_use"),
                                          asistan("S", "m2", 1)])
        akis = os.path.join(d, "akis.jsonl")
        tam = {"input_tokens": 20, "output_tokens": 200, "cache_creation_input_tokens": 2000,
               "cache_read_input_tokens": 10000}
        with open(akis, "w") as f:
            for mid in ("m1", "m1", "m2"):
                f.write(json.dumps({"type": "assistant", "message": {"id": mid}}) + "\n")
            f.write(json.dumps({"type": "result", "usage": tam}) + "\n")
        r = self._calistir(["uzlastir", akis, "--transkript", t])
        self.assertEqual(r.returncode, 0, r.stdout)
        self.assertIn("EŞLEŞTİ; API toplamı 12220, transkript toplamı 12220; 2/2", r.stdout)
        # Tekilleştirme yapılmasaydı transkript 3 satır sayardı → uyuşmazlık yakalanmalı
        tam["output_tokens"] = 300
        with open(akis, "a") as f:
            f.write(json.dumps({"type": "result", "usage": tam}) + "\n")
        r = self._calistir(["uzlastir", akis, "--transkript", t])
        self.assertEqual(r.returncode, 1)
        self.assertIn("output_tokens: API 300 / transkript 200", r.stdout)

    def test_stream_json_rate_limit_event_gercek_bicim(self):
        # claude -p --output-format stream-json çıktısındaki gerçek satır biçimi
        d = tempfile.mkdtemp()
        yol = os.path.join(d, "a.jsonl")
        with open(yol, "w") as f:
            f.write(json.dumps({"type": "rate_limit_event", "rate_limit_info": {
                "status": "allowed", "resetsAt": 1767225600, "rateLimitType": "five_hour",
                "overageStatus": "rejected", "isUsingOverage": False, "unifiedWindows": {
                    "five_hour": {"utilization": 0.37, "resetsAt": 1767225600},
                    "seven_day": {"utilization": 0.14, "resetsAt": 1767600000}}},
                "uuid": "x", "session_id": "S"}) + "\n")
            f.write("{yarim")
        g, bozuk = kota.dosyadan_oku(yol)
        p = {x.pencere: x for x in g}
        self.assertAlmostEqual(p["five_hour"].kullanilan, 37.0)
        self.assertAlmostEqual(p["seven_day"].kalan, 86.0)
        self.assertEqual(p["five_hour"].yenilenme.isoformat(), "2026-01-01T00:00:00+00:00")
        self.assertEqual(bozuk, 1)

    def test_rapor_json(self):
        d = tempfile.mkdtemp()
        yaz(os.path.join(d, "A.jsonl"), [asistan("A", "m1", 0)])
        r = self._calistir(["rapor", "--transkript", d, "--json", "--kota", os.path.join(d, "yok")])
        v = json.loads(r.stdout)
        self.assertEqual(v["toplam"], 6110)
        self.assertIsNone(v["pencereler"]["five_hour"])


GERCEK = os.path.expanduser("~/.claude/projects")


@unittest.skipUnless(os.path.isdir(GERCEK) and any(
    f.endswith(".jsonl") for _, _, fs in os.walk(GERCEK) for f in fs), "gerçek transkript yok")
class GercekTranskript(unittest.TestCase):
    def test_bagimsiz_hesapla_ayni_toplam(self):
        """Araçtan bağımsız, en basit yöntemle (message.id başına ilk usage)
        toplamı hesaplar; araç aynı sonucu vermeli."""
        beklenen, goruldu = {}, set()
        for kok, _, fs in os.walk(GERCEK):
            for f in fs:
                if not f.endswith(".jsonl"):
                    continue
                with open(os.path.join(kok, f), encoding="utf-8") as fh:
                    satirlar = fh.readlines()
                for satir in satirlar:
                    try:
                        k = json.loads(satir)
                    except ValueError:
                        continue
                    m = k.get("message") or {}
                    if k.get("type") != "assistant" or not isinstance(m.get("usage"), dict):
                        continue
                    if m.get("id") in goruldu:
                        continue
                    goruldu.add(m.get("id"))
                    u = m["usage"]
                    s = beklenen.setdefault(k["sessionId"], 0)
                    beklenen[k["sessionId"]] = s + sum(u.get(a, 0) for a in (
                        "input_tokens", "output_tokens", "cache_creation_input_tokens",
                        "cache_read_input_tokens"))
        oz = transkript.oturumlara_ayir(transkript.oku([GERCEK]))
        # Canlı oturum dosyası okuma sırasında büyüyebilir; araç sonra okuduğu
        # için ondan küçük olamaz.
        for k, v in beklenen.items():
            if v:
                arac = sum(o.toplam for ad, o in oz.items() if re.split("[@#]", ad)[0] == k)
                self.assertGreaterEqual(arac, v)


if __name__ == "__main__":
    unittest.main(verbosity=2)
