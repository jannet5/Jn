"""Bağımsız incelemenin bulgularına karşılık gelen olumsuz senaryo testleri.

(1) kasa politikası fail-closed, (2) silme/indeks/geri alma, (4) URI ve sır sızıntısı.
Pano (3) ve hesaba özel sayaç gerçek pencere gerektirdiği için kanit/gui_kabul.py'de.
"""
import json
import sys

import keyring.errors
import pytest

import totp_masaustu as tm
from conftest import TEST_IZNI, BellekKasasi

SAHTE = "JBSWY3DPEHPK3PXP"  # herkese açık örnek anahtar (pyotp belgeleri)
WINVAULT = "keyring.backends.Windows.WinVaultKeyring"


# ---------------------------------------------------------------- (1) politika
class Bilinmeyen:
    """Adı 'Plaintext' içermeyen ama izin listesinde olmayan sıradan bir arka uç."""

    def get_password(self, s, u):
        raise AssertionError("politika reddetmeliydi; arka uç çağrıldı")

    set_password = delete_password = get_password


def test_bilinmeyen_backend_varsayilan_listede_reddedilir():
    with pytest.raises(tm.KasaHatasi, match="İzin verilmeyen"):
        tm.backend_dogrula(Bilinmeyen())
    assert tm.backend_guvenli_mi(Bilinmeyen())[0] is False


@pytest.mark.parametrize("platform", ["win32", "linux", "darwin", "freebsd"])
def test_her_platformda_bilinmeyen_reddedilir(platform):
    assert not tm.backend_guvenli_mi(Bilinmeyen(), platform=platform)[0]


def test_fail_ve_null_backend_reddedilir():
    from keyring.backends import fail, null
    for kr in (fail.Keyring(), null.Keyring()):
        assert not tm.backend_guvenli_mi(kr)[0]


def test_windows_yalniz_winvault_ister():
    assert tm.izinli_backendler("win32") == frozenset({WINVAULT})
    # Varsayılan (uygulamanın kullandığı) politikada Windows'ta başka hiçbir arka uç geçmez
    for kr in (BellekKasasi(), Bilinmeyen()):
        with pytest.raises(tm.KasaHatasi):
            tm.backend_dogrula(kr, platform="win32")
    from keyring.backends import Windows
    if sys.platform == "win32":
        assert "WinVaultKeyring" in tm.backend_dogrula(Windows.WinVaultKeyring(), platform="win32")


def test_zincirde_tek_yabanci_backend_bile_reddedilir():
    from keyring.backends.chainer import ChainerBackend

    class Zincir(ChainerBackend):
        def __init__(self, altlar):
            self._altlar = altlar

        @property
        def backends(self):
            return self._altlar

    assert tm.backend_guvenli_mi(Zincir([BellekKasasi()]), izinli=TEST_IZNI)[0]
    assert not tm.backend_guvenli_mi(Zincir([BellekKasasi(), Bilinmeyen()]), izinli=TEST_IZNI)[0]
    assert not tm.backend_guvenli_mi(Zincir([]), izinli=TEST_IZNI)[0]


@pytest.mark.parametrize("islem", [
    lambda k: k.ekle("x", SAHTE), lambda k: k.adlar(), lambda k: k.ayar("x"),
    lambda k: k.sil("x"), lambda k: k.hepsi(), lambda k: k.saglik_testi(),
])
def test_politikaya_uymayan_kasada_hicbir_okuma_yazma_yapilmaz(islem):
    kasa = tm.Kasa("My2FAApp", kr=Bilinmeyen())  # varsayılan platform izin listesi
    with pytest.raises(tm.KasaHatasi):
        islem(kasa)  # Bilinmeyen çağrılsaydı AssertionError olurdu


def test_politika_her_islemde_yeniden_denetlenir(bellek):
    izin = set(TEST_IZNI)
    kasa = tm.Kasa("My2FAApp", kr=bellek, izinli=izin)
    kasa.ekle("a", SAHTE)
    izin.clear()  # çalışma sırasında arka uç güvenilmez hale geldi
    sayi = len(bellek.cagrilar)
    with pytest.raises(tm.KasaHatasi):
        kasa.adlar()
    assert len(bellek.cagrilar) == sayi


def test_saglik_testi_bozuk_kasayi_yakalar(bellek, kasa):
    assert kasa.saglik_testi()
    assert bellek.veri == {}  # deneme kaydı geride kalmaz
    bellek.sessiz_silme_basarisiz.add(tm.SAGLIK_ANAHTARI)
    with pytest.raises(tm.KasaHatasi, match="silinemedi"):
        kasa.saglik_testi()


def test_arka_uc_ham_hata_metni_kullaniciya_gitmez(bellek, kasa):
    bellek.hata_ekle("set", "x", OSError("C:\\gizli\\yol " + SAHTE))
    with pytest.raises(tm.KasaHatasi) as e:
        kasa.ekle("x", SAHTE)
    assert SAHTE not in str(e.value) and "gizli" not in str(e.value)
    assert "OSError" in str(e.value)
    assert ("My2FAApp", "x") not in bellek.veri


# ------------------------------------------------- (2) silme / indeks / geri alma
def test_silinemeyen_kayit_basari_gostermez_ve_listede_kalir(kasa, bellek):
    kasa.ekle("GitHub", SAHTE)
    bellek.hata_ekle("delete", "GitHub", keyring.errors.PasswordDeleteError("erişim reddedildi"))
    with pytest.raises(tm.KasaHatasi, match="silinemedi"):
        kasa.sil("GitHub")
    assert kasa.adlar() == ["GitHub"] and bellek.veri[("My2FAApp", "GitHub")] == SAHTE


def test_sessizce_basarisiz_silme_de_yakalanir(kasa, bellek):
    kasa.ekle("GitHub", SAHTE)
    bellek.sessiz_silme_basarisiz.add("GitHub")  # hata vermiyor ama kayıt duruyor
    with pytest.raises(tm.KasaHatasi):
        kasa.sil("GitHub")
    assert kasa.adlar() == ["GitHub"]


def test_kasada_zaten_olmayan_kayit_listeden_temizlenebilir(kasa, bellek):
    kasa.ekle("GitHub", SAHTE)
    del bellek.veri[("My2FAApp", "GitHub")]  # örn. Kimlik Bilgileri Yöneticisi'nden elle silinmiş
    kasa.sil("GitHub")
    assert kasa.adlar() == []


def test_listede_olmayan_hesap_silinmez(kasa, bellek):
    bellek.veri[("My2FAApp", "yabanci")] = "baska-uygulama-verisi"
    with pytest.raises(tm.TotpHatasi, match="listede yok"):
        kasa.sil("yabanci")
    assert bellek.veri[("My2FAApp", "yabanci")] == "baska-uygulama-verisi"


def test_indeks_disi_mevcut_kayit_uzerine_yazilmaz(kasa, bellek):
    bellek.veri[("My2FAApp", "GitHub")] = "ONCEKI-KAYIT"
    with pytest.raises(tm.KasaHatasi, match="üzerine yazılmadı"):
        kasa.ekle("GitHub", SAHTE)
    assert bellek.veri[("My2FAApp", "GitHub")] == "ONCEKI-KAYIT"
    assert kasa.adlar() == []


@pytest.mark.parametrize("onek", [tm.ISARETCI, tm.LISTE_ONEKI + "2_0"])
def test_ekle_indeks_yazma_hatasinda_geri_alinir(kasa, bellek, onek):
    kasa.ekle("ilk", SAHTE)
    once = dict(bellek.veri)
    bellek.hata_ekle("set", onek, RuntimeError("disk dolu"), kac_kez=1)
    with pytest.raises(tm.KasaHatasi, match="geri alındı"):
        kasa.ekle("ikinci", SAHTE)
    assert bellek.veri == once  # yeni sır ve yarım parçalar kaldırıldı
    assert kasa.adlar() == ["ilk"]
    kasa.ekle("ikinci", SAHTE)  # hata geçince normal çalışır
    assert kasa.adlar() == ["ilk", "ikinci"]


def test_ilk_ekleme_isaretci_yazilamazsa_iz_kalmaz(kasa, bellek):
    bellek.hata_ekle("set", tm.ISARETCI, RuntimeError("x"), kac_kez=1)
    with pytest.raises(tm.KasaHatasi):
        kasa.ekle("ilk", SAHTE)
    assert bellek.veri == {}


def test_ekle_geri_alma_da_basarisizsa_elle_temizleme_soylenir(kasa, bellek):
    bellek.hata_ekle("set", tm.ISARETCI, RuntimeError("x"))
    bellek.hata_ekle("delete", "yeni", RuntimeError("y"))
    with pytest.raises(tm.KasaHatasi, match="elle silin"):
        kasa.ekle("yeni", SAHTE)


def test_sil_indeks_yazma_hatasinda_sir_geri_yuklenir(kasa, bellek):
    kasa.ekle("a", SAHTE)
    kasa.ekle("b", SAHTE)
    bellek.hata_ekle("set", tm.ISARETCI, RuntimeError("x"), kac_kez=1)
    with pytest.raises(tm.KasaHatasi, match="geri yüklendi"):
        kasa.sil("a")
    assert kasa.adlar() == ["a", "b"]
    assert kasa.ayar("a").secret == SAHTE


def test_yarim_kalan_yazim_eski_listeyi_bozmaz(kasa, bellek):
    """Çökme simülasyonu: yeni parçalar yazıldı, işaretçi çevrilmedi."""
    kasa.ekle("a", SAHTE)
    bellek.veri[("My2FAApp", tm.LISTE_ONEKI + "2_0__")] = '["a", "yarim'
    assert kasa.adlar() == ["a"]


def test_bozuk_indeks_fail_closed(kasa, bellek):
    kasa.ekle("a", SAHTE)
    anahtar = next(u for (_, u) in bellek.veri if u.startswith(tm.LISTE_ONEKI + "1_"))
    bellek.veri[("My2FAApp", anahtar)] = '["a", "sahte-eklenen"]'
    with pytest.raises(tm.KasaHatasi, match="bütünlük"):
        kasa.adlar()
    with pytest.raises(tm.KasaHatasi):
        kasa.ekle("b", SAHTE)


def test_eski_100_bicimi_okunur_ve_tasinir(kasa, bellek):
    bellek.veri[("My2FAApp", "__hesap_listesi_0__")] = json.dumps(["eski"])
    bellek.veri[("My2FAApp", "eski")] = SAHTE
    assert kasa.adlar() == ["eski"]
    kasa.ekle("yeni", SAHTE)
    assert ("My2FAApp", "__hesap_listesi_0__") not in bellek.veri
    assert kasa.adlar() == ["eski", "yeni"]
    kasa.sil("eski")
    kasa.sil("yeni")
    assert bellek.veri == {}


def test_eski_bicim_silinemezse_bos_listede_hesaplar_geri_gelmez(kasa, bellek):
    bellek.veri[("My2FAApp", "__hesap_listesi_0__")] = json.dumps(["eski"])
    bellek.veri[("My2FAApp", "eski")] = SAHTE
    bellek.hata_ekle("delete", "__hesap_listesi_0__", RuntimeError("x"))
    with pytest.raises(tm.KasaHatasi):
        kasa.sil("eski")  # son hesap: eski parça kesin silinmeli, yoksa hata
    assert kasa.adlar() == ["eski"]  # yanlış "silindi" durumu yok


# ------------------------------------------------------------ (4) URI / sızıntı
@pytest.mark.parametrize("uri,parca", [
    (f"otpauth://totp/GitHub:ali?secret={SAHTE}&issuer=GitLab", "uyuşmuyor"),
    (f"otpauth://totp/X?secret={SAHTE}&secret=AAAAAAAAAAAAAAAA", "birden fazla"),
    (f"otpauth://totp/X?secret={SAHTE}&issuer=A&issuer=B", "birden fazla"),
    (f"otpauth://totp/X?secret={SAHTE}&SECRET=AAAAAAAAAAAAAAAA", "birden fazla"),
    (f"otpauth://totp/Git%00Hub:ali?secret={SAHTE}", "kontrol"),
    (f"otpauth://totp/GitHub:ali%0A?secret={SAHTE}", "kontrol"),
    (f"otpauth://totp/GitHub:ali?secret={SAHTE}&issuer=Git%1BHub", "kontrol"),
    (f"otpauth://totp/GitHub:ali?secret={SAHTE}&issuer=Git%E2%80%AEHub", "kontrol"),
])
def test_supheli_uri_reddedilir(uri, parca):
    with pytest.raises(tm.TotpHatasi, match=parca):
        tm.anahtar_coz(uri)


def test_issuer_etiket_esitse_ya_da_tekse_kabul():
    assert tm.anahtar_coz(f"otpauth://totp/GitHub:ali?secret={SAHTE}&issuer=GitHub").issuer == "GitHub"
    assert tm.anahtar_coz(f"otpauth://totp/GitHub:ali?secret={SAHTE}").issuer == "GitHub"
    assert tm.anahtar_coz(f"otpauth://totp/ali?secret={SAHTE}&issuer=GitHub").issuer == "GitHub"
    assert tm.anahtar_coz(f"otpauth://totp/GitHub%3Aali?secret={SAHTE}&issuer=GitHub").issuer == "GitHub"


def test_kontrol_karakterli_etiketten_ad_onerilmez():
    assert tm.onerilen_ad(f"otpauth://totp/Git%00Hub?secret={SAHTE}") == ""


def test_sir_repr_ve_str_icinde_gorunmez():
    ayar = tm.anahtar_coz(SAHTE)
    assert SAHTE not in repr(ayar) and SAHTE not in str(ayar)


@pytest.mark.parametrize("girdi", [
    "JBSWY3DPEHPK3PX1JBSW", "JBSW!Y3DP@EHPK#3PXP", "JBSWY3DPEHPK3PXPÇ", "JBSWY3DPEHPK3PX\x00",
    f"otpauth://totp/X?secret={SAHTE}1&digits=6",
])
def test_hata_mesajlari_sirri_veya_parcasini_icermez(girdi):
    with pytest.raises(tm.TotpHatasi) as e:
        tm.anahtar_coz(girdi)
    mesaj = str(e.value)
    for i in range(len(SAHTE) - 3):
        assert SAHTE[i:i + 4] not in mesaj
    for kotu in "!@#Ç\x00":  # rakamlar hata sayısı olarak geçebilir; 4'lü parçalar yukarıda
        if kotu in girdi:
            assert kotu not in mesaj
    assert e.value.__cause__ is None


def test_hesaba_ozel_periyot_sayaci():
    a60 = tm.anahtar_coz(f"otpauth://totp/X?secret={SAHTE}&period=60")
    assert a60.kalan(120.0) == 60 and a60.kalan(170.5) == 9.5
    assert tm.sayac_metni(a60.kalan(170.5)) == "10 sn"
    assert tm.sayac_metni(tm.kalan_saniye(30, 89.0)) == "1 sn"
    assert tm.sayac_metni(tm.kalan_saniye(30, 60.0)) == "30 sn"
    # 60 sn'lik hesabın kodu 30'da değil 60'ta değişir
    assert a60.kod(120) == a60.kod(179.9) != a60.kod(180)
