"""Çekirdek kabul testleri: TOTP doğruluğu, GitHub uyumu, kasa, çevrimdışılık."""
import base64
import hashlib
import hmac
import os
import socket
import struct
import time

import keyring
import pyotp
import pytest

import totp_masaustu as tm

# RFC 6238 Ek B test vektörleri (SHA1, anahtar = "12345678901234567890")
RFC_ANAHTAR = base64.b32encode(b"12345678901234567890").decode()
RFC_VEKTORLER = [
    (59, "94287082"), (1111111109, "07081804"), (1111111111, "14050471"),
    (1234567890, "89005924"), (2000000000, "69279037"), (20000000000, "65353130"),
]
# Yalnız test için rastgele üretilmiş, hiçbir gerçek hesaba ait olmayan anahtar
SAHTE_GITHUB_ANAHTARI = "JBSWY3DPEHPK3PXP"


def bagimsiz_totp(b32, t, digits=6, period=30, digest=hashlib.sha1):
    """RFC 6238'in pyotp'den bağımsız, sıfırdan yazılmış uygulaması."""
    anahtar = base64.b32decode(b32 + "=" * (-len(b32) % 8))
    mesaj = struct.pack(">Q", int(t) // period)
    h = hmac.new(anahtar, mesaj, digest).digest()
    o = h[-1] & 0x0F
    sayi = struct.unpack(">I", h[o:o + 4])[0] & 0x7FFFFFFF
    return str(sayi % 10 ** digits).zfill(digits)


@pytest.mark.parametrize("t,beklenen", RFC_VEKTORLER)
def test_rfc6238_vektorleri(t, beklenen):
    ayar = tm.TotpAyari(RFC_ANAHTAR, digits=8)
    assert ayar.kod(t) == beklenen
    # 6 haneli kod 8 hanelinin son 6 hanesidir
    assert tm.TotpAyari(RFC_ANAHTAR).kod(t) == beklenen[-6:]


def test_github_parametreleri_ve_bagimsiz_uygulama_ile_esitlik():
    """GitHub: SHA1 / 6 hane / 30 sn (docs.github.com). 1000 rastgele anda karşılaştır."""
    ayar = tm.anahtar_coz(SAHTE_GITHUB_ANAHTARI)
    assert (ayar.algorithm, ayar.digits, ayar.period) == ("SHA1", 6, 30)
    import random
    r = random.Random(208)
    for _ in range(1000):
        t = r.randint(0, 4_000_000_000)
        assert ayar.kod(t) == bagimsiz_totp(SAHTE_GITHUB_ANAHTARI, t)
        assert len(ayar.kod(t)) == 6 and ayar.kod(t).isdigit()


def test_sunucu_tarafi_dogrulama_simulasyonu():
    """GitHub benzeri sunucu doğrulaması: pyotp.verify ±1 pencere ile kabul eder."""
    sunucu = pyotp.TOTP(SAHTE_GITHUB_ANAHTARI)
    simdi = time.time()
    kod = tm.anahtar_coz(SAHTE_GITHUB_ANAHTARI).kod(simdi)
    assert sunucu.verify(kod, for_time=simdi, valid_window=1)
    assert not sunucu.verify(kod, for_time=simdi + 300, valid_window=1)


@pytest.mark.parametrize("girdi", [
    "jbsw y3dp ehpk 3pxp", "JBSW-Y3DP-EHPK-3PXP", "  JBSWY3DPEHPK3PXP\n", "JBSWY3DPEHPK3PXP====",
])
def test_setup_key_bicim_toleransi(girdi):
    assert tm.anahtar_coz(girdi).secret == SAHTE_GITHUB_ANAHTARI


def test_otpauth_uri_github_bicimi():
    uri = f"otpauth://totp/GitHub:ornek-kullanici?secret={SAHTE_GITHUB_ANAHTARI}&issuer=GitHub"
    ayar = tm.anahtar_coz(uri)
    assert ayar.secret == SAHTE_GITHUB_ANAHTARI and ayar.issuer == "GitHub" and ayar.standart_mi
    assert tm.onerilen_ad(uri) == "GitHub:ornek-kullanici"


def test_standart_disi_uri_parametreleri_korunur():
    uri = f"otpauth://totp/X?secret={SAHTE_GITHUB_ANAHTARI}&digits=8&period=60&algorithm=SHA256"
    ayar = tm.anahtar_coz(uri)
    assert not ayar.standart_mi
    geri = tm.anahtar_coz(ayar.kasa_degeri())
    assert geri == tm.TotpAyari(SAHTE_GITHUB_ANAHTARI, 8, 60, "SHA256", "")
    assert geri.kod(1234567890) == bagimsiz_totp(SAHTE_GITHUB_ANAHTARI, 1234567890, 8, 60, hashlib.sha256)


@pytest.mark.parametrize("girdi,parca", [
    ("", "boş"), ("ABC", "kısa"), ("JBSWY3DPEHPK3PX1", "geçersiz"),
    ("otpauth://hotp/X?secret=JBSWY3DPEHPK3PXP", "TOTP"),
    ("otpauth://totp/X?secret=JBSWY3DPEHPK3PXP&digits=4", "Hane"),
    ("otpauth://totp/X?secret=JBSWY3DPEHPK3PXP&algorithm=MD5", "Algoritma"),
])
def test_gecersiz_anahtarlar_reddedilir(girdi, parca):
    with pytest.raises(tm.TotpHatasi, match=parca):
        tm.anahtar_coz(girdi)


def test_sayac_ve_bicim():
    assert tm.kalan_saniye(30, 60.0) == 30
    assert tm.kalan_saniye(30, 89.0) == 1
    assert tm.kalan_saniye(30, 75.5) == 14.5
    assert tm.kodu_bicimle("123456") == "123 456"
    # Kod tam 30'un katında değişir, arada sabittir
    a = tm.TotpAyari(SAHTE_GITHUB_ANAHTARI)
    assert a.kod(60) == a.kod(89.999) != a.kod(90)


def test_kasa_ekle_oku_sil_ve_duz_metin_yok(bellek_kasasi, tmp_path, monkeypatch):
    monkeypatch.chdir(tmp_path)
    kasa = tm.Kasa()
    kasa.ekle("GitHub - deneme", SAHTE_GITHUB_ANAHTARI)
    # Kaynak gereksinimi: set_password("My2FAApp", hesap_adi, gizli_anahtar)
    assert keyring.get_password("My2FAApp", "GitHub - deneme") == SAHTE_GITHUB_ANAHTARI
    # "Uygulama yeniden açıldı": yeni Kasa nesnesi aynı hesapları okur
    assert [a for a, _ in tm.Kasa().hepsi()] == ["GitHub - deneme"]
    with pytest.raises(tm.TotpHatasi, match="zaten"):
        kasa.ekle("github - DENEME", SAHTE_GITHUB_ANAHTARI)
    kasa.sil("GitHub - deneme")
    assert kasa.adlar() == [] and keyring.get_password("My2FAApp", "GitHub - deneme") is None
    assert list(tmp_path.iterdir()) == []  # diske hiçbir dosya yazılmadı


def test_buyuk_hesap_listesi_parcalanir(bellek_kasasi):
    kasa = tm.Kasa()
    adlar = [f"Hesap-{i:03d}-ğüşiöç-uzun-bir-ad" for i in range(80)]
    for ad in adlar:
        kasa.ekle(ad, pyotp.random_base32())
    assert kasa.adlar() == adlar
    parcalar = [v for (s, u), v in bellek_kasasi.veri.items() if u.startswith(tm.LISTE_ONEKI)]
    assert len(parcalar) > 1 and all(len(p.encode("utf-16-le")) <= 2560 for p in parcalar)
    for ad in adlar[:75]:
        kasa.sil(ad)
    assert kasa.adlar() == adlar[75:]
    kalan = [u for (s, u) in bellek_kasasi.veri if u.startswith(tm.LISTE_ONEKI)]
    assert len(kalan) == 1  # artık parçalar temizlendi
    for ad in adlar[75:]:
        kasa.sil(ad)
    assert bellek_kasasi.veri == {}  # son hesap silinince kasada iz kalmaz


def test_bozuk_kayit_uygulamayi_dusurmez(bellek_kasasi):
    kasa = tm.Kasa()
    kasa.ekle("iyi", SAHTE_GITHUB_ANAHTARI)
    kasa.ekle("bozuk", SAHTE_GITHUB_ANAHTARI)
    keyring.set_password("My2FAApp", "bozuk", "!!!")
    sonuc = dict(kasa.hepsi())
    assert sonuc["iyi"] is not None and sonuc["bozuk"] is None


def test_ad_dogrulama():
    for kotu in ["", "   ", "__gizli", "a" * 65, "ad\x00x"]:
        with pytest.raises(tm.TotpHatasi):
            tm.ad_dogrula(kotu)
    assert tm.ad_dogrula("  GitHub  ") == "GitHub"


def test_guvensiz_backend_reddedilir():
    from keyring.backends import fail, null
    assert not tm.backend_guvenli_mi(fail.Keyring())[0]
    assert not tm.backend_guvenli_mi(null.Keyring())[0]

    class PlaintextKeyring:  # keyrings.alt düz metin kasasını taklit eder
        pass
    assert not tm.backend_guvenli_mi(PlaintextKeyring())[0]


def test_tamamen_cevrimdisi_calisir(bellek_kasasi, monkeypatch):
    """Ağ çağrısı yapılırsa test patlar: soket oluşturma yasaklandı."""
    def yasak(*a, **k):
        raise AssertionError("ağ erişimi denendi!")
    monkeypatch.setattr(socket, "socket", yasak)
    monkeypatch.setattr(socket, "create_connection", yasak)
    monkeypatch.setattr(socket, "getaddrinfo", yasak)
    kasa = tm.Kasa()
    kasa.ekle("cevrimdisi", SAHTE_GITHUB_ANAHTARI)
    assert len(kasa.ayar("cevrimdisi").kod()) == 6


def test_kaynakta_ag_kutuphanesi_yok():
    kod = open(tm.__file__, encoding="utf-8").read()
    for yasak in ("import socket", "import requests", "urllib.request", "http.client", "ntplib"):
        assert yasak not in kod
