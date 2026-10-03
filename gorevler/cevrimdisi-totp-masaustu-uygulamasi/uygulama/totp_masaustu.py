# -*- coding: utf-8 -*-
"""
Çevrimdışı TOTP (2FA) Masaüstü Kod Üretici
==========================================

Tamamen yerel çalışan, internete bağlanmayan 2FA kod üretici.

* Arayüz      : customtkinter (koyu tema varsayılan, sabit pencere)
* 2FA motoru  : pyotp (RFC 6238 TOTP; SHA1 / 6 hane / 30 sn varsayılan)
* Depolama    : keyring -> Windows'ta yalnız Windows Credential Manager
                (WinVaultKeyring). Bilinmeyen/düz metin kasalar reddedilir
                (fail-closed). Gizli anahtarlar KOD İÇİNE veya düz metin
                dosyasına ASLA yazılmaz.

Kurulum (Windows, PowerShell):
    py -m pip install customtkinter pyotp keyring
Çalıştırma:
    py totp_masaustu.py

keyring kayıtları listeleyemediği için hesap ADLARI da yine keyring içinde,
nesil numaralı parçalar + SHA-256'lı tek bir işaretçi kaydıyla tutulur
(yarım kalan yazım eski listeyi bozmaz). Diskte hiçbir dosya oluşturulmaz.
"""

from __future__ import annotations

import hashlib
import json
import math
import secrets
import sys
import time
import unicodedata
import urllib.parse
from dataclasses import dataclass, field

import keyring
import keyring.errors
import pyotp

UYGULAMA_ADI = "Çevrimdışı TOTP"
SURUM = "1.0.1"

# Kaynak gereksinimi: keyring.set_password("My2FAApp", hesap_adi, gizli_anahtar)
SERVIS = "My2FAApp"
# Hesap listesi (yalnız adlar) keyring'de nesil numaralı parçalar halinde durur;
# hangi neslin geçerli olduğunu tek bir işaretçi kaydı söyler (atomik geçiş).
LISTE_ONEKI = "__hesap_listesi_"
ISARETCI = "__hesap_listesi_aktif__"
ESKI_LISTE_ONEKI = "__hesap_listesi_"  # 1.0.0 biçimi: __hesap_listesi_<i>__
SAGLIK_ANAHTARI = "__saglik_testi__"
# Windows Credential Manager blob sınırı 2560 bayt; keyring UTF-16 yazar.
# 900 karakterlik parçalar (<= 1800 bayt) güvenli pay bırakır.
PARCA_BOYU = 900
MAKS_AD_UZUNLUGU = 64
VARSAYILAN_PERIYOT = 30

# Kasa politikası: yalnız bu arka uçlar kabul edilir; bilinmeyen her şey reddedilir.
IZINLI_BACKENDLER = {
    "win32": frozenset({"keyring.backends.Windows.WinVaultKeyring"}),
    "darwin": frozenset({"keyring.backends.macOS.Keyring"}),
    "linux": frozenset({"keyring.backends.SecretService.Keyring",
                        "keyring.backends.libsecret.Keyring"}),
}


class TotpHatasi(ValueError):
    """Kullanıcıya gösterilecek doğrulama hataları (mesajlar sır içermez)."""


class KasaHatasi(TotpHatasi):
    """Kasa politikası veya kasa işlemi başarısız; işlem yapılmadı/geri alındı."""


def _kontrol_karakteri_var(metin: str) -> bool:
    return any(unicodedata.category(c).startswith("C") for c in metin)


# ---------------------------------------------------------------------------
# 1) Gizli anahtar çözümleme ve TOTP hesaplama
# ---------------------------------------------------------------------------

@dataclass(frozen=True)
class TotpAyari:
    """Bir hesabın TOTP parametreleri. Sır repr/str çıktısında görünmez."""

    secret: str = field(repr=False)
    digits: int = 6
    period: int = VARSAYILAN_PERIYOT
    algorithm: str = "SHA1"
    issuer: str = ""

    @property
    def standart_mi(self) -> bool:
        # GitHub ve çoğu servis: SHA1 / 6 hane / 30 sn
        return (self.digits, self.period, self.algorithm) == (6, 30, "SHA1")

    def totp(self) -> pyotp.TOTP:
        digest = {"SHA1": hashlib.sha1, "SHA256": hashlib.sha256,
                  "SHA512": hashlib.sha512}[self.algorithm]
        return pyotp.TOTP(self.secret, digits=self.digits,
                          digest=digest, interval=self.period)

    def kod(self, zaman: float | None = None) -> str:
        return self.totp().at(time.time() if zaman is None else zaman)

    def kalan(self, zaman: float | None = None) -> float:
        """Bu hesabın kendi periyoduna göre kalan saniye."""
        return kalan_saniye(self.period, zaman)

    def kasa_degeri(self) -> str:
        """keyring'e yazılacak değer.

        Standart hesapta yalnızca base32 anahtar yazılır (kaynaktaki
        set_password biçimi). Standart dışı hesapta parametreleri korumak
        için otpauth:// URI yazılır; o da yalnız kasada durur.
        """
        if self.standart_mi:
            return self.secret
        q = {"secret": self.secret, "digits": self.digits,
             "period": self.period, "algorithm": self.algorithm}
        if self.issuer:
            q["issuer"] = self.issuer
        return "otpauth://totp/hesap?" + urllib.parse.urlencode(q)


def _base32_dogrula(secret: str) -> str:
    temiz = "".join(secret.split()).replace("-", "").upper().rstrip("=")
    if not temiz:
        raise TotpHatasi("Gizli anahtar boş olamaz.")
    # Geçersiz karakterlerin kendisi gösterilmez: sırrın parçası olabilir.
    gecersiz = sum(c not in "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567" for c in temiz)
    if gecersiz:
        raise TotpHatasi(f"Gizli anahtarda {gecersiz} geçersiz karakter var "
                         "(Base32 yalnız A-Z ve 2-7 içerir).")
    if len(temiz) < 16:
        raise TotpHatasi("Gizli anahtar çok kısa (en az 16 Base32 karakter, 80 bit).")
    try:
        pyotp.TOTP(temiz).byte_secret()
    except Exception:
        raise TotpHatasi("Gizli anahtar Base32 olarak çözülemedi.") from None
    return temiz


def _uri_etiketi(parca: urllib.parse.ParseResult) -> tuple[str, str, str]:
    """(tam etiket, etiketteki issuer, hesap) döndürür; kontrol karakterini reddeder."""
    etiket = urllib.parse.unquote(parca.path.lstrip("/"))
    if _kontrol_karakteri_var(etiket):  # strip'ten ÖNCE: sondaki %0A da yakalanır
        raise TotpHatasi("URI etiketinde kontrol karakteri var.")
    etiket = etiket.strip()
    if ":" in etiket:
        on, hesap = etiket.split(":", 1)
        return etiket, on.strip(), hesap.strip()
    return etiket, "", etiket


def anahtar_coz(girdi: str) -> TotpAyari:
    """Kullanıcı girdisini (setup key veya otpauth:// URI) doğrular."""
    girdi = (girdi or "").strip()
    if not girdi.lower().startswith("otpauth://"):
        return TotpAyari(_base32_dogrula(girdi))

    if _kontrol_karakteri_var(girdi):
        raise TotpHatasi("URI içinde kontrol karakteri var.")
    parca = urllib.parse.urlparse(girdi)
    if parca.netloc.lower() != "totp":
        raise TotpHatasi("Yalnız TOTP desteklenir (otpauth://totp/...).")
    _etiket, etiket_issuer, _hesap = _uri_etiketi(parca)
    ciftler = urllib.parse.parse_qsl(parca.query, keep_blank_values=True)
    q: dict[str, str] = {}
    for ad, deger in ciftler:
        ad = ad.lower()
        if ad in q:  # ör. iki secret: hangisinin kullanılacağı belirsiz -> reddet
            raise TotpHatasi(f"URI'de '{ad}' parametresi birden fazla kez var.")
        if _kontrol_karakteri_var(deger):
            raise TotpHatasi(f"URI'deki '{ad}' parametresinde kontrol karakteri var.")
        q[ad] = deger.strip()

    try:
        digits = int(q.get("digits", "6"))
        period = int(q.get("period", "30"))
    except ValueError:
        raise TotpHatasi("URI içindeki digits/period sayı olmalı.") from None
    algorithm = q.get("algorithm", "SHA1").upper()
    if digits not in (6, 7, 8):
        raise TotpHatasi("Hane sayısı 6, 7 veya 8 olmalı.")
    if not 10 <= period <= 120:
        raise TotpHatasi("Periyot 10-120 saniye arasında olmalı.")
    if algorithm not in ("SHA1", "SHA256", "SHA512"):
        raise TotpHatasi("Algoritma SHA1, SHA256 veya SHA512 olmalı.")
    issuer = q.get("issuer", "")
    if issuer and etiket_issuer and issuer != etiket_issuer:
        raise TotpHatasi("URI'deki issuer parametresi etiketteki servis adıyla uyuşmuyor.")
    return TotpAyari(_base32_dogrula(q.get("secret", "")), digits, period,
                     algorithm, issuer or etiket_issuer)


def onerilen_ad(girdi: str) -> str:
    """otpauth URI etiketinden hesap adı önerir (ör. 'GitHub:kullanici')."""
    girdi = (girdi or "").strip()
    if not girdi.lower().startswith("otpauth://"):
        return ""
    try:
        return _uri_etiketi(urllib.parse.urlparse(girdi))[0]
    except (TotpHatasi, ValueError):
        return ""


def ad_dogrula(ad: str) -> str:
    ad = unicodedata.normalize("NFC", (ad or "").strip())
    if not ad:
        raise TotpHatasi("Hesap adı boş olamaz.")
    if len(ad) > MAKS_AD_UZUNLUGU:
        raise TotpHatasi(f"Hesap adı en fazla {MAKS_AD_UZUNLUGU} karakter olabilir.")
    if ad.startswith("__"):
        raise TotpHatasi("Hesap adı '__' ile başlayamaz (iç kullanım).")
    if _kontrol_karakteri_var(ad):
        raise TotpHatasi("Hesap adında kontrol karakteri olamaz.")
    return ad


def kalan_saniye(period: int = VARSAYILAN_PERIYOT, zaman: float | None = None) -> float:
    zaman = time.time() if zaman is None else zaman
    return period - (zaman % period)


def sayac_metni(kalan: float) -> str:
    """Sayaçta gösterilecek tam saniye (29.2 -> 30 değil, 30 sn'lik dilimde 30..1)."""
    return f"{max(1, math.ceil(kalan))} sn"


def kodu_bicimle(kod: str) -> str:
    """'123456' -> '123 456' (okunabilirlik); 8 hane -> '1234 5678'."""
    yari = len(kod) // 2
    return kod[:yari] + " " + kod[yari:]


# ---------------------------------------------------------------------------
# 2) Güvenli kasa (keyring) katmanı — fail-closed
# ---------------------------------------------------------------------------

def izinli_backendler(platform: str | None = None) -> frozenset[str]:
    return IZINLI_BACKENDLER.get(platform or sys.platform, frozenset())


def _tam_ad(nesne) -> str:
    return f"{type(nesne).__module__}.{type(nesne).__name__}"


def backend_dogrula(kr=None, izinli: frozenset[str] | None = None,
                    platform: str | None = None) -> str:
    """Kasa arka ucunu izin listesine göre doğrular; uymazsa KasaHatasi.

    keyring'in ChainerBackend'i açılır: zincirdeki HER arka uç izinli olmalı
    (okuma zincirin tamamını gezdiği için tek bir yabancı arka uç bile reddedilir).
    Windows'ta yalnız WinVaultKeyring (Windows Credential Manager) kabul edilir.
    """
    from keyring.backends.chainer import ChainerBackend

    platform = platform or sys.platform
    varsayilan = izinli is None  # uygulama her zaman varsayılanla çalışır; testler açık liste verir
    izinli = izinli_backendler(platform) if varsayilan else frozenset(izinli)
    kr = keyring.get_keyring() if kr is None else kr
    altlar = list(kr.backends) if isinstance(kr, ChainerBackend) else [kr]
    if not altlar:
        raise KasaHatasi("Kullanılabilir bir kimlik bilgisi kasası bulunamadı.")
    adlar = [_tam_ad(b) for b in altlar]
    reddedilen = [a for a in adlar if a not in izinli]
    if reddedilen:
        raise KasaHatasi("İzin verilmeyen kasa arka ucu: " + ", ".join(reddedilen))
    if varsayilan and platform == "win32" and adlar != ["keyring.backends.Windows.WinVaultKeyring"]:
        raise KasaHatasi("Windows'ta yalnız Windows Credential Manager (WinVaultKeyring) kullanılır.")
    return ", ".join(a.rsplit(".", 2)[-2] + "." + a.rsplit(".", 1)[-1] for a in adlar)


def backend_guvenli_mi(kr=None, izinli=None, platform=None) -> tuple[bool, str]:
    try:
        return True, backend_dogrula(kr, izinli, platform)
    except KasaHatasi as exc:
        return False, str(exc)


class Kasa:
    """Hesapları keyring üzerinde saklar. Diske dosya yazmaz.

    Her okuma/yazmadan önce arka uç politikası yeniden denetlenir (fail-closed).
    Arka uç hataları ham metinleriyle değil, genel bir mesajla bildirilir.
    """

    def __init__(self, servis: str = SERVIS, kr=None, izinli: frozenset[str] | None = None):
        self.servis = servis
        self._sabit_kr = kr          # None: keyring.get_keyring() kullanılır
        self._izinli = izinli        # None: platformun izin listesi

    # -- düşük seviye, denetimli işlemler ------------------------------------
    def _kr(self):
        kr = keyring.get_keyring() if self._sabit_kr is None else self._sabit_kr
        backend_dogrula(kr, self._izinli)
        return kr

    def denetle(self) -> str:
        """Politikayı denetler; arka uç adını döndürür."""
        return backend_dogrula(self._sabit_kr, self._izinli)

    def _islem(self, ad: str, fonk, *args):
        kr = self._kr()
        try:
            return getattr(kr, fonk)(self.servis, *args)
        except keyring.errors.PasswordDeleteError:
            raise
        except Exception as exc:  # ham mesaj kullanıcıya gösterilmez
            raise KasaHatasi(f"Kasa işlemi başarısız ({ad}: {type(exc).__name__}).") from None

    def _oku(self, kullanici: str) -> str | None:
        return self._islem("okuma", "get_password", kullanici)

    def _yaz(self, kullanici: str, deger: str) -> None:
        self._islem("yazma", "set_password", kullanici, deger)
        if self._oku(kullanici) != deger:
            raise KasaHatasi("Kasaya yazılan değer geri okunamadı.")

    def _sil(self, kullanici: str) -> None:
        """Kaydı siler ve gerçekten silindiğini doğrular."""
        try:
            self._islem("silme", "delete_password", kullanici)
        except keyring.errors.PasswordDeleteError:
            pass  # zaten yoksa sorun değil; aşağıdaki doğrulama karar verir
        if self._oku(kullanici) is not None:
            raise KasaHatasi("Kayıt kasadan silinemedi.")

    def _sil_sessiz(self, kullanici: str) -> bool:
        """Yalnız artık (işaretçinin göstermediği) liste parçaları için."""
        try:
            self._sil(kullanici)
            return True
        except KasaHatasi:
            return False

    def saglik_testi(self) -> str:
        """Açılışta: denetim + yaz/oku/sil turu. Başarısızsa KasaHatasi."""
        ad = self.denetle()
        deger = secrets.token_hex(8)
        self._yaz(SAGLIK_ANAHTARI, deger)
        self._sil(SAGLIK_ANAHTARI)
        return ad

    # -- hesap listesi (yalnız adlar) ------------------------------------------
    def _isaretci(self) -> tuple[int, int, str] | None:
        deger = self._oku(ISARETCI)
        if deger is None:
            return None
        try:
            nesil, adet, ozet = deger.split(":")
            return int(nesil), int(adet), ozet
        except ValueError:
            raise KasaHatasi("Kasadaki hesap listesi işaretçisi bozuk.") from None

    def _eski_parcalar(self) -> list[str]:
        parcalar, i = [], 0
        while (p := self._oku(f"{ESKI_LISTE_ONEKI}{i}__")) is not None:
            parcalar.append(p)
            i += 1
        return parcalar

    def adlar(self) -> list[str]:
        isaretci = self._isaretci()
        if isaretci is None:
            metin = "".join(self._eski_parcalar())  # 1.0.0 biçimi (geri uyum)
        else:
            nesil, adet, ozet = isaretci
            parcalar = [self._oku(f"{LISTE_ONEKI}{nesil}_{i}__") for i in range(adet)]
            if any(p is None for p in parcalar):
                raise KasaHatasi("Kasadaki hesap listesinin bir parçası eksik.")
            metin = "".join(parcalar)
            if hashlib.sha256(metin.encode("utf-8")).hexdigest() != ozet:
                raise KasaHatasi("Kasadaki hesap listesi bütünlük denetiminden geçmedi.")
        if not metin:
            return []
        try:
            veri = json.loads(metin)
        except json.JSONDecodeError:
            raise KasaHatasi("Kasadaki hesap listesi bozuk.") from None
        return [a for a in veri if isinstance(a, str)]

    def _adlari_yaz(self, adlar: list[str]) -> None:
        """Yeni nesli yaz -> doğrula -> işaretçiyi çevir -> eskiyi temizle.

        İşaretçi çevrilmeden önceki her hata, yazılan yeni parçaları geri alır;
        eski liste olduğu gibi geçerli kalır.
        """
        eski = self._isaretci()
        eski_nesil = eski[0] if eski else 0
        eski_adet = eski[1] if eski else 0
        # 1.0.0 biçimi artıkları (geçişte silinemeyenler dahil) her seferinde sayılır.
        eski_bicim_adet = len(self._eski_parcalar())
        yazilan: list[str] = []
        if adlar:
            metin = json.dumps(adlar, ensure_ascii=False)
            parcalar = [metin[i:i + PARCA_BOYU] for i in range(0, len(metin), PARCA_BOYU)]
            nesil = eski_nesil + 1
            yeni = f"{nesil}:{len(parcalar)}:{hashlib.sha256(metin.encode('utf-8')).hexdigest()}"
            try:
                for i, p in enumerate(parcalar):
                    anahtar = f"{LISTE_ONEKI}{nesil}_{i}__"
                    yazilan.append(anahtar)
                    self._yaz(anahtar, p)
                self._yaz(ISARETCI, yeni)
            except KasaHatasi:
                # Önce işaretçiyi eski haline getir, sonra yeni parçaları kaldır.
                eski_deger = f"{eski[0]}:{eski[1]}:{eski[2]}" if eski else None
                if self._isaretci_ham() != eski_deger:
                    try:
                        if eski_deger is None:
                            self._sil(ISARETCI)
                        else:
                            self._yaz(ISARETCI, eski_deger)
                    except KasaHatasi:
                        raise KasaHatasi("Hesap listesi yazılamadı ve önceki liste geri "
                                         "yüklenemedi.") from None
                for anahtar in yazilan:
                    self._sil_sessiz(anahtar)
                raise KasaHatasi("Hesap listesi kasaya yazılamadı; değişiklik geri alındı.") from None
        else:
            self._sil(ISARETCI)  # boş liste: işaretçi kalkmazsa hata
            # İşaretçi yokken eski (1.0.0) parçalar okunur; bu yüzden onlar kesin silinmeli.
            for i in range(eski_bicim_adet):
                self._sil(f"{ESKI_LISTE_ONEKI}{i}__")
            eski_bicim_adet = 0
        # Artık kullanılmayan parçalar: yalnız ad içerir, sır içermez. Silinemezse
        # kasada artık kayıt kalır ama liste bozulmaz (işaretçi onları göstermez).
        for i in range(eski_adet):
            self._sil_sessiz(f"{LISTE_ONEKI}{eski_nesil}_{i}__")
        for i in range(eski_bicim_adet):
            self._sil_sessiz(f"{ESKI_LISTE_ONEKI}{i}__")

    def _isaretci_ham(self) -> str | None:
        try:
            return self._oku(ISARETCI)
        except KasaHatasi:
            return None

    # -- hesap işlemleri ------------------------------------------------------
    def ekle(self, ad: str, girdi: str) -> TotpAyari:
        self.denetle()  # politika uymuyorsa hiçbir şey okunmaz/yazılmaz
        ad = ad_dogrula(ad)
        ayar = anahtar_coz(girdi)
        adlar = self.adlar()
        if ad.casefold() in (a.casefold() for a in adlar):
            raise TotpHatasi(f"'{ad}' adında bir hesap zaten var.")
        if self._oku(ad) is not None:
            raise KasaHatasi(f"Kasada '{ad}' adıyla listede olmayan bir kayıt var; "
                             "üzerine yazılmadı.")
        try:
            self._yaz(ad, ayar.kasa_degeri())
        except KasaHatasi:
            self._sil_sessiz(ad)
            raise
        try:
            self._adlari_yaz(adlar + [ad])
        except KasaHatasi:
            if not self._sil_sessiz(ad):  # geri alma: eklenen sırrı kaldır
                raise KasaHatasi(f"Hesap listesi yazılamadı ve '{ad}' kaydı geri alınamadı. "
                                 f"Kimlik Bilgileri Yöneticisi'nde '{SERVIS}' altındaki "
                                 "kaydı elle silin.") from None
            raise
        return ayar

    def ayar(self, ad: str) -> TotpAyari | None:
        deger = self._oku(ad)
        if deger is None:
            return None
        return anahtar_coz(deger)

    def sil(self, ad: str) -> None:
        """Sırrı sil + doğrula, sonra listeden çıkar. Liste yazılamazsa sır geri yazılır."""
        adlar = self.adlar()
        if ad not in adlar:
            raise TotpHatasi(f"'{ad}' hesabı listede yok.")
        eski_deger = self._oku(ad)
        self._sil(ad)  # silinemezse KasaHatasi: liste değişmez
        try:
            self._adlari_yaz([a for a in adlar if a != ad])
        except KasaHatasi:
            if eski_deger is not None:
                try:
                    self._yaz(ad, eski_deger)
                except KasaHatasi:
                    raise KasaHatasi(f"'{ad}' silindi ama liste güncellenemedi; "
                                     "uygulamayı yeniden başlatın.") from None
            raise KasaHatasi(f"'{ad}' silinemedi (liste güncellenemedi); kayıt geri yüklendi.") from None

    def hepsi(self) -> list[tuple[str, TotpAyari | None]]:
        """(ad, ayar) listesi; okunamayan/eksik kayıt için ayar None döner."""
        sonuc = []
        for ad in self.adlar():
            try:
                sonuc.append((ad, self.ayar(ad)))
            except KasaHatasi:
                raise
            except TotpHatasi:
                sonuc.append((ad, None))
        return sonuc


# ---------------------------------------------------------------------------
# 3) Arayüz (customtkinter)
# ---------------------------------------------------------------------------

def uygulama_olustur(kasa: Kasa | None = None):  # pragma: no cover - GUI kabulünde sürülür
    """Pencereyi kurar ve döndürür (mainloop çağırmaz)."""
    import customtkinter as ctk
    from tkinter import messagebox

    PANO_TEMIZLEME_SN = 30

    class HesapSatiri(ctk.CTkFrame):
        def __init__(self, ana, uyg, ad: str, ayar: TotpAyari | None):
            super().__init__(ana, corner_radius=8)
            self.uyg, self.ad, self.ayar = uyg, ad, ayar
            self.grid_columnconfigure(0, weight=1)
            ctk.CTkLabel(self, text=ad, anchor="w",
                         font=ctk.CTkFont(size=13, weight="bold")).grid(
                row=0, column=0, sticky="w", padx=(10, 4), pady=(6, 0))
            self.kod_etiketi = ctk.CTkLabel(
                self, text="--- ---", anchor="w",
                font=ctk.CTkFont(family="Consolas", size=24, weight="bold"),
                text_color="#4FC3F7")
            self.kod_etiketi.grid(row=1, column=0, sticky="w", padx=(10, 4), pady=(0, 6))
            # Standart dışı periyotlu hesap kendi geri sayımını gösterir (alt çubuk 30 sn içindir).
            self.sure_etiketi = None
            if ayar is not None and ayar.period != VARSAYILAN_PERIYOT:
                self.sure_etiketi = ctk.CTkLabel(self, text="", text_color="gray", width=70)
                self.sure_etiketi.grid(row=1, column=1, sticky="n")
            self.kopyala = ctk.CTkButton(self, text="Kopyala", width=78,
                                         command=self.kopyala_tikla)
            self.kopyala.grid(row=0, column=1, padx=4, pady=(6, 0))
            ctk.CTkButton(self, text="Sil", width=40, fg_color="#5A2A2A",
                          hover_color="#7A3030",
                          command=lambda: uyg.hesap_sil(ad)).grid(
                row=0, column=2, rowspan=2, padx=(0, 8))
            if ayar is None:
                self.kod_etiketi.configure(text="okunamadı", text_color="#E57373")
                self.kopyala.configure(state="disabled")
            self.son_kod = ""
            self.guncelle()

        def guncelle(self) -> None:
            if self.ayar is None:
                return
            simdi = time.time()
            kod = self.ayar.kod(simdi)
            if kod != self.son_kod:  # yalnız değiştiğinde çiz
                self.son_kod = kod
                self.kod_etiketi.configure(text=kodu_bicimle(kod))
            if self.sure_etiketi is not None:
                self.sure_etiketi.configure(
                    text=f"{sayac_metni(self.ayar.kalan(simdi))} / {self.ayar.period}")

        def kopyala_tikla(self) -> None:
            if self.ayar is None:
                return
            self.uyg.panoya_kopyala(self.ayar.kod(), self.ad)

    class Uygulama(ctk.CTk):
        def __init__(self, kasa: Kasa):
            super().__init__()
            self.kasa = kasa
            self.satirlar: list[HesapSatiri] = []
            self._pano_kod = None
            self._pano_is = None
            self._tik_is = None
            self.title(f"{UYGULAMA_ADI} {SURUM}")
            self.geometry("440x560")
            self.resizable(False, False)  # kaynak: sabit pencere boyutu
            self.grid_columnconfigure(0, weight=1)
            self.grid_rowconfigure(1, weight=1)

            # -- Üst kısım: hesap ekleme -------------------------------------
            ust = ctk.CTkFrame(self)
            ust.grid(row=0, column=0, sticky="ew", padx=10, pady=(10, 5))
            ust.grid_columnconfigure(0, weight=1)
            self.ad_girdi = ctk.CTkEntry(ust, placeholder_text="Hesap adı (ör. GitHub - kullanıcı)")
            self.ad_girdi.grid(row=0, column=0, columnspan=2, sticky="ew", padx=8, pady=(8, 4))
            self.anahtar_girdi = ctk.CTkEntry(
                ust, placeholder_text="Gizli anahtar (setup key) veya otpauth:// URI", show="•")
            self.anahtar_girdi.grid(row=1, column=0, sticky="ew", padx=(8, 4), pady=(0, 8))
            self.ekle_btn = ctk.CTkButton(ust, text="Ekle", width=70, command=self.hesap_ekle)
            self.ekle_btn.grid(row=1, column=1, padx=(0, 8), pady=(0, 8))
            self.anahtar_girdi.bind("<Return>", lambda _e: self.hesap_ekle())

            # -- Orta kısım: hesap listesi -----------------------------------
            self.liste = ctk.CTkScrollableFrame(self, label_text="Hesaplar")
            self.liste.grid(row=1, column=0, sticky="nsew", padx=10, pady=5)
            self.liste.grid_columnconfigure(0, weight=1)
            self.bos_etiket = ctk.CTkLabel(
                self.liste, text="Henüz hesap yok. Yukarıdan ekleyin.", text_color="gray")

            # -- Alt kısım: geri sayım + durum -------------------------------
            alt = ctk.CTkFrame(self, fg_color="transparent")
            alt.grid(row=2, column=0, sticky="ew", padx=10, pady=(5, 10))
            alt.grid_columnconfigure(0, weight=1)
            self.cubuk = ctk.CTkProgressBar(alt)
            self.cubuk.grid(row=0, column=0, sticky="ew", padx=(0, 8))
            self.sayac = ctk.CTkLabel(alt, text="30 sn", width=48)
            self.sayac.grid(row=0, column=1)
            self.durum = ctk.CTkLabel(alt, text="", anchor="w", text_color="gray")
            self.durum.grid(row=1, column=0, columnspan=2, sticky="ew")

            self.kasa_hazir = False
            try:
                backend_adi = self.kasa.saglik_testi()
                self.kasa_hazir = True
            except KasaHatasi as exc:
                self.kasa_kapat(str(exc))
            if self.kasa_hazir:
                self.durum_yaz("Kasa: " + backend_adi + " · çevrimdışı")
                self.listeyi_yukle()
            else:
                self.bos_etiket.grid(row=0, column=0, pady=20)
            self.protocol("WM_DELETE_WINDOW", self.destroy)
            self.tik()

        def kasa_kapat(self, neden: str) -> None:
            """Fail-closed: kasa güvenilmezse ekleme/silme kapatılır."""
            self.kasa_hazir = False
            self.ekle_btn.configure(state="disabled")
            self.durum_yaz("Güvenli kasa kullanılamıyor; ekleme kapatıldı.", hata=True)
            messagebox.showerror(
                UYGULAMA_ADI,
                "Güvenli kimlik bilgisi kasası kullanılamıyor:\n" + neden +
                "\n\nAnahtarlar başka yere yazılmayacağı için ekleme kapatıldı.")

        def durum_yaz(self, metin: str, hata: bool = False) -> None:
            self.durum.configure(text=metin, text_color="#E57373" if hata else "gray")

        def listeyi_yukle(self) -> None:
            for s in self.satirlar:
                s.destroy()
            self.satirlar.clear()
            hesaplar = []
            if self.kasa_hazir:
                try:
                    hesaplar = self.kasa.hepsi()
                except KasaHatasi as exc:
                    self.kasa_kapat(str(exc))
            for i, (ad, ayar) in enumerate(hesaplar):
                satir = HesapSatiri(self.liste, self, ad, ayar)
                satir.grid(row=i, column=0, sticky="ew", padx=2, pady=3)
                self.satirlar.append(satir)
            if hesaplar:
                self.bos_etiket.grid_forget()
            else:
                self.bos_etiket.grid(row=0, column=0, pady=20)

        def hesap_ekle(self) -> None:
            # Enter kısayolu da buraya gelir: kapalı "Ekle" düğmesi bu yolla aşılamaz.
            if not self.kasa_hazir or self.ekle_btn.cget("state") == "disabled":
                self.durum_yaz("Kasa kullanılamıyor; hesap eklenmedi.", hata=True)
                return
            ad = self.ad_girdi.get().strip()
            girdi = self.anahtar_girdi.get()
            if not ad:
                ad = onerilen_ad(girdi)
            try:
                self.kasa.ekle(ad, girdi)
            except TotpHatasi as exc:  # KasaHatasi dahil; mesajlar sır içermez
                messagebox.showerror(UYGULAMA_ADI, str(exc))
                return
            self.ad_girdi.delete(0, "end")
            self.anahtar_girdi.delete(0, "end")  # anahtar ekranda kalmasın
            self.focus_set()  # odak kalksın; ipucu metinleri geri gelsin
            for girdi_alani in (self.ad_girdi, self.anahtar_girdi):
                if hasattr(girdi_alani, "_entry_focus_out"):
                    girdi_alani._entry_focus_out()
            self.listeyi_yukle()
            self.durum_yaz(f"'{ad}' eklendi ve kasaya şifreli kaydedildi.")

        def hesap_sil(self, ad: str) -> None:
            if not messagebox.askyesno(
                    UYGULAMA_ADI,
                    f"'{ad}' silinsin mi?\n\nServiste 2FA'yı kapatmadan silerseniz "
                    "kurtarma kodlarınız olmadan hesaba giremeyebilirsiniz."):
                return
            if not self.kasa_hazir:
                self.durum_yaz("Kasa kullanılamıyor; silinmedi.", hata=True)
                return
            try:
                self.kasa.sil(ad)
            except TotpHatasi as exc:  # silinemeyen kayıt başarı gibi gösterilmez
                messagebox.showerror(UYGULAMA_ADI, str(exc))
                self.durum_yaz(f"'{ad}' silinemedi.", hata=True)
                self.listeyi_yukle()
                return
            self.listeyi_yukle()
            self.durum_yaz(f"'{ad}' silindi.")

        def panoya_kopyala(self, kod: str, ad: str) -> None:
            self.clipboard_clear()
            self.clipboard_append(kod)
            self.update()  # pano sahipliğini hemen al
            self._pano_kod = kod
            if self._pano_is:
                self.after_cancel(self._pano_is)
            self._pano_is = self.after(PANO_TEMIZLEME_SN * 1000, self.panoyu_temizle)
            self.durum_yaz(f"'{ad}' kodu kopyalandı ({PANO_TEMIZLEME_SN} sn sonra panodan silinir).")

        def panoyu_temizle(self) -> None:
            """Pano hâlâ bizim kopyaladığımız kodu tutuyorsa boşaltır.

            Kullanıcı arada başka bir şey kopyaladıysa ona dokunulmaz. Windows pano
            geçmişi (Win+V) / bulut panosu veya çökme durumları için garanti yoktur.
            """
            self._pano_is = None
            try:
                if self._pano_kod is not None and self.clipboard_get() == self._pano_kod:
                    self.clipboard_clear()
                    self.clipboard_append("")
                    self.update()
            except Exception:
                pass  # pano boş veya başka uygulamada: dokunulacak bir şey yok
            self._pano_kod = None

        def tik(self) -> None:
            # Her saniye saati kontrol et; 30 sn sınırında kodlar değişir.
            kalan = kalan_saniye()
            self.cubuk.set(kalan / VARSAYILAN_PERIYOT)
            self.sayac.configure(text=sayac_metni(kalan),
                                 text_color="#E57373" if kalan <= 5 else ("gray90", "gray90"))
            for s in self.satirlar:
                s.guncelle()
            # Bir sonraki saniye başına hizalan (kaymayı önler).
            self._tik_is = self.after(max(50, int((1 - time.time() % 1) * 1000)), self.tik)

        def destroy(self) -> None:
            # Normal kapanış: zamanlayıcıyı beklemeden, pano hâlâ bizim koddaysa temizle.
            if self._pano_is:
                self.after_cancel(self._pano_is)
                self._pano_is = None
            if self._pano_kod is not None:
                self.panoyu_temizle()
            if self._tik_is:
                self.after_cancel(self._tik_is)
                self._tik_is = None
            super().destroy()

    ctk.set_appearance_mode("dark")  # kaynak: Dark Mode varsayılan
    ctk.set_default_color_theme("blue")
    return Uygulama(kasa or Kasa())


def arayuzu_baslat() -> None:  # pragma: no cover
    uygulama_olustur().mainloop()


if __name__ == "__main__":
    for akis in (sys.stdout, sys.stderr):
        # Windows konsolu cp1252/cp857 olabilir; Türkçe karakter hata vermesin.
        if akis is not None and hasattr(akis, "reconfigure"):
            akis.reconfigure(errors="replace")
    if "--surum" in sys.argv:
        print(f"{UYGULAMA_ADI} {SURUM}")
        sys.exit(0)
    if "--kasa-tani" in sys.argv:
        try:
            print("GÜVENLİ: " + Kasa().saglik_testi())
            sys.exit(0)
        except KasaHatasi as exc:
            print("GÜVENSİZ: " + str(exc))
            sys.exit(2)
    arayuzu_baslat()
