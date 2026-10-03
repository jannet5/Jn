import os
import sys

import keyring.errors
import pytest

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "uygulama"))


class BellekKasasi:
    """Yalnız test için bellek içi kasa (diske yazmaz).

    Bilerek keyring.backend.KeyringBackend alt sınıfı DEĞİLDİR: keyring alt
    sınıfları otomatik keşfedip gerçek zincire ekler. Uygulamanın politikası
    bu sınıfı yalnız testte açıkça izin verilirse kabul eder.
    """

    def __init__(self):
        self.veri = {}
        self.cagrilar = []
        self.hata_kurallari = []  # (islem, kullanici_onek, kac_kez veya None, istisna)
        self.sessiz_silme_basarisiz = set()  # silme "başarılı" görünür ama kayıt kalır

    def _kural(self, islem, kullanici):
        for kural in self.hata_kurallari:
            k_islem, onek, kalan, istisna = kural
            if k_islem == islem and kullanici.startswith(onek) and (kalan is None or kalan > 0):
                if kalan is not None:
                    kural[2] = kalan - 1
                raise istisna

    def get_password(self, service, username):
        self.cagrilar.append(("get", username))
        self._kural("get", username)
        return self.veri.get((service, username))

    def set_password(self, service, username, password):
        self.cagrilar.append(("set", username))
        self._kural("set", username)
        self.veri[(service, username)] = password

    def delete_password(self, service, username):
        self.cagrilar.append(("delete", username))
        self._kural("delete", username)
        if username in self.sessiz_silme_basarisiz:
            return
        if (service, username) not in self.veri:
            raise keyring.errors.PasswordDeleteError("yok")
        del self.veri[(service, username)]

    def hata_ekle(self, islem, onek, istisna, kac_kez=None):
        self.hata_kurallari.append([islem, onek, kac_kez, istisna])


TEST_IZNI = frozenset({"conftest.BellekKasasi"})


@pytest.fixture
def bellek():
    return BellekKasasi()


@pytest.fixture
def kasa(bellek):
    import totp_masaustu as tm
    return tm.Kasa("My2FAApp", kr=bellek, izinli=TEST_IZNI)
