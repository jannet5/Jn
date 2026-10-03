import os
import sys

import keyring
import keyring.backend
import pytest

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "uygulama"))


class BellekKasasi(keyring.backend.KeyringBackend):
    """Yalnız test için bellek içi kasa (diske yazmaz)."""

    # keyring tüm KeyringBackend alt sınıflarını otomatik keşfeder; bu sınıf
    # gerçek zincire karışmasın diye "uygun değil" (viable=False) işaretlenir.
    @keyring.backend.properties.classproperty
    def priority(cls):
        raise RuntimeError("yalnız test için")

    def __init__(self):
        super().__init__()
        self.veri = {}

    def get_password(self, service, username):
        return self.veri.get((service, username))

    def set_password(self, service, username, password):
        self.veri[(service, username)] = password

    def delete_password(self, service, username):
        if (service, username) not in self.veri:
            raise keyring.errors.PasswordDeleteError("yok")
        del self.veri[(service, username)]


@pytest.fixture
def bellek_kasasi():
    eski = keyring.get_keyring()
    kb = BellekKasasi()
    keyring.set_keyring(kb)
    yield kb
    keyring.set_keyring(eski)
