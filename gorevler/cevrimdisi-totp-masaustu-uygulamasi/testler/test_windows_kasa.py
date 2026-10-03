"""Gerçek Windows Credential Manager testi. Yalnız Windows'ta (veya Wine'da) çalışır."""
import sys

import keyring
import pytest

import totp_masaustu as tm

pytestmark = pytest.mark.skipif(sys.platform != "win32", reason="Windows'a özel")

TEST_SERVISI = "My2FAApp-otomatik-test"  # gerçek "My2FAApp" kayıtlarına dokunmaz


def test_windows_backend_winvault():
    kr = keyring.get_keyring()
    altlar = getattr(kr, "backends", [kr])  # keyring çoğu zaman ChainerBackend döndürür
    assert [type(b).__name__ for b in altlar] == ["WinVaultKeyring"], altlar
    guvenli, ad = tm.backend_guvenli_mi(kr)
    assert guvenli and "WinVaultKeyring" in ad


def test_windows_kasada_tam_dongu():
    kasa = tm.Kasa(TEST_SERVISI)
    for ad in kasa.adlar():
        kasa.sil(ad)
    try:
        kasa.ekle("GitHub - test", "JBSWY3DPEHPK3PXP")
        kasa.ekle("Ğüşİöç türkçe", "otpauth://totp/x?secret=JBSWY3DPEHPK3PXP&digits=8")
        yeni = tm.Kasa(TEST_SERVISI)  # yeniden açılış
        assert yeni.adlar() == ["GitHub - test", "Ğüşİöç türkçe"]
        assert yeni.ayar("GitHub - test").kod(59) == tm.TotpAyari("JBSWY3DPEHPK3PXP").kod(59)
        assert yeni.ayar("Ğüşİöç türkçe").digits == 8
    finally:
        for ad in kasa.adlar():
            kasa.sil(ad)
    assert tm.Kasa(TEST_SERVISI).adlar() == []
