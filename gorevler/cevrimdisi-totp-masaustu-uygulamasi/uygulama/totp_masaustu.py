# -*- coding: utf-8 -*-
"""
Çevrimdışı TOTP (2FA) Masaüstü Kod Üretici
==========================================

Tamamen yerel çalışan, internete bağlanmayan 2FA kod üretici.

* Arayüz      : customtkinter (koyu tema varsayılan, sabit pencere)
* 2FA motoru  : pyotp (RFC 6238 TOTP; SHA1 / 6 hane / 30 sn varsayılan)
* Depolama    : keyring -> Windows'ta Windows Credential Manager (DPAPI ile
                kullanıcıya özel şifreli). Gizli anahtarlar KOD İÇİNE veya
                düz metin dosyasına ASLA yazılmaz.

Kurulum (Windows, PowerShell):
    py -m pip install customtkinter pyotp keyring
Çalıştırma:
    py totp_masaustu.py

keyring kayıtları listeleyemediği için hesap ADLARI da yine keyring içinde
"__hesap_listesi_N__" anahtarlarında (parçalı JSON) tutulur. Diskte hiçbir
dosya oluşturulmaz.
"""

from __future__ import annotations

import json
import sys
import time
import unicodedata
import urllib.parse
from dataclasses import dataclass

import keyring
import keyring.errors
import pyotp

UYGULAMA_ADI = "Çevrimdışı TOTP"
SURUM = "1.0.0"

# Kaynak gereksinimi: keyring.set_password("My2FAApp", hesap_adi, gizli_anahtar)
SERVIS = "My2FAApp"
# Hesap listesi (yalnız adlar) bu önekle keyring'e parça parça yazılır.
LISTE_ONEKI = "__hesap_listesi_"
# Windows Credential Manager blob sınırı 2560 bayt; keyring UTF-16 yazar.
# 900 karakterlik parçalar (<= 1800 bayt) güvenli pay bırakır.
PARCA_BOYU = 900
MAKS_AD_UZUNLUGU = 64
VARSAYILAN_PERIYOT = 30


class TotpHatasi(ValueError):
    """Kullanıcıya gösterilecek doğrulama hataları."""


# ---------------------------------------------------------------------------
# 1) Gizli anahtar çözümleme ve TOTP hesaplama
# ---------------------------------------------------------------------------

@dataclass(frozen=True)
class TotpAyari:
    """Bir hesabın TOTP parametreleri."""

    secret: str
    digits: int = 6
    period: int = VARSAYILAN_PERIYOT
    algorithm: str = "SHA1"
    issuer: str = ""

    @property
    def standart_mi(self) -> bool:
        # GitHub ve çoğu servis: SHA1 / 6 hane / 30 sn
        return (self.digits, self.period, self.algorithm) == (6, 30, "SHA1")

    def totp(self) -> pyotp.TOTP:
        import hashlib

        digest = {"SHA1": hashlib.sha1, "SHA256": hashlib.sha256,
                  "SHA512": hashlib.sha512}[self.algorithm]
        return pyotp.TOTP(self.secret, digits=self.digits,
                          digest=digest, interval=self.period)

    def kod(self, zaman: float | None = None) -> str:
        return self.totp().at(time.time() if zaman is None else zaman)

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
    gecersiz = sorted({c for c in temiz if c not in "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"})
    if gecersiz:
        raise TotpHatasi("Gizli anahtarda geçersiz karakter var: " + " ".join(gecersiz)
                         + "\n(Base32 yalnız A-Z ve 2-7 içerir.)")
    if len(temiz) < 16:
        raise TotpHatasi("Gizli anahtar çok kısa (en az 16 Base32 karakter, 80 bit).")
    try:
        pyotp.TOTP(temiz).byte_secret()
    except Exception as exc:  # pragma: no cover - savunma amaçlı
        raise TotpHatasi(f"Gizli anahtar çözülemedi: {exc}") from exc
    return temiz


def anahtar_coz(girdi: str) -> TotpAyari:
    """Kullanıcı girdisini (setup key veya otpauth:// URI) doğrular."""
    girdi = (girdi or "").strip()
    if girdi.lower().startswith("otpauth://"):
        parca = urllib.parse.urlparse(girdi)
        if parca.netloc.lower() != "totp":
            raise TotpHatasi("Yalnız TOTP desteklenir (otpauth://totp/...).")
        q = urllib.parse.parse_qs(parca.query)

        def al(ad: str, vars: str = "") -> str:
            return q.get(ad, [vars])[0].strip()

        try:
            digits = int(al("digits", "6"))
            period = int(al("period", "30"))
        except ValueError as exc:
            raise TotpHatasi("URI içindeki digits/period sayı olmalı.") from exc
        algorithm = al("algorithm", "SHA1").upper()
        if digits not in (6, 7, 8):
            raise TotpHatasi("Hane sayısı 6, 7 veya 8 olmalı.")
        if not 10 <= period <= 120:
            raise TotpHatasi("Periyot 10-120 saniye arasında olmalı.")
        if algorithm not in ("SHA1", "SHA256", "SHA512"):
            raise TotpHatasi("Algoritma SHA1, SHA256 veya SHA512 olmalı.")
        return TotpAyari(_base32_dogrula(al("secret")), digits, period,
                         algorithm, al("issuer"))
    return TotpAyari(_base32_dogrula(girdi))


def onerilen_ad(girdi: str) -> str:
    """otpauth URI etiketinden hesap adı önerir (ör. 'GitHub:kullanici')."""
    girdi = (girdi or "").strip()
    if not girdi.lower().startswith("otpauth://"):
        return ""
    return urllib.parse.unquote(urllib.parse.urlparse(girdi).path.lstrip("/")).strip()


def ad_dogrula(ad: str) -> str:
    ad = unicodedata.normalize("NFC", (ad or "").strip())
    if not ad:
        raise TotpHatasi("Hesap adı boş olamaz.")
    if len(ad) > MAKS_AD_UZUNLUGU:
        raise TotpHatasi(f"Hesap adı en fazla {MAKS_AD_UZUNLUGU} karakter olabilir.")
    if ad.startswith("__"):
        raise TotpHatasi("Hesap adı '__' ile başlayamaz (iç kullanım).")
    if any(unicodedata.category(c).startswith("C") for c in ad):
        raise TotpHatasi("Hesap adında kontrol karakteri olamaz.")
    return ad


def kalan_saniye(period: int = VARSAYILAN_PERIYOT, zaman: float | None = None) -> float:
    zaman = time.time() if zaman is None else zaman
    return period - (zaman % period)


def kodu_bicimle(kod: str) -> str:
    """'123456' -> '123 456' (okunabilirlik); 8 hane -> '1234 5678'."""
    yari = len(kod) // 2
    return kod[:yari] + " " + kod[yari:]


# ---------------------------------------------------------------------------
# 2) Güvenli kasa (keyring) katmanı
# ---------------------------------------------------------------------------

def backend_guvenli_mi(kr=None) -> tuple[bool, str]:
    """Düz metin veya çalışmayan keyring arka uçlarını reddeder."""
    kr = kr or keyring.get_keyring()
    ad = f"{type(kr).__module__}.{type(kr).__name__}"
    altlar = getattr(kr, "backends", None)  # ChainerBackend
    if altlar is not None:
        if not altlar:
            return False, ad + " (hiç kasa yok)"
        alt_adlar = ", ".join(type(b).__name__ for b in altlar)
        return all(backend_guvenli_mi(b)[0] for b in altlar), f"{ad}[{alt_adlar}]"
    yasakli = ("keyring.backends.fail", "keyring.backends.null")
    if type(kr).__module__ in yasakli:
        return False, ad
    if any(k in type(kr).__name__ for k in ("Plaintext", "Uncrypted", "Unencrypted")):
        return False, ad
    return True, ad


class Kasa:
    """Hesapları keyring üzerinde saklar. Diske dosya yazmaz."""

    def __init__(self, servis: str = SERVIS):
        self.servis = servis

    # -- hesap listesi (yalnız adlar, parçalı JSON) --------------------------
    def adlar(self) -> list[str]:
        metin, i = "", 0
        while True:
            parca = keyring.get_password(self.servis, f"{LISTE_ONEKI}{i}__")
            if parca is None:
                break
            metin += parca
            i += 1
        if not metin:
            return []
        try:
            veri = json.loads(metin)
        except json.JSONDecodeError as exc:
            raise TotpHatasi("Kasadaki hesap listesi bozuk.") from exc
        return [a for a in veri if isinstance(a, str)]

    def _adlari_yaz(self, adlar: list[str]) -> None:
        metin = json.dumps(adlar, ensure_ascii=False)
        # Hesap kalmadıysa liste kaydı da kasada bırakılmaz.
        parcalar = [metin[i:i + PARCA_BOYU] for i in range(0, len(metin), PARCA_BOYU)] if adlar else []
        eski = 0
        while keyring.get_password(self.servis, f"{LISTE_ONEKI}{eski}__") is not None:
            eski += 1
        for i, p in enumerate(parcalar):
            keyring.set_password(self.servis, f"{LISTE_ONEKI}{i}__", p)
        for i in range(len(parcalar), eski):
            try:
                keyring.delete_password(self.servis, f"{LISTE_ONEKI}{i}__")
            except keyring.errors.PasswordDeleteError:
                pass

    # -- hesap işlemleri ------------------------------------------------------
    def ekle(self, ad: str, girdi: str) -> TotpAyari:
        ad = ad_dogrula(ad)
        ayar = anahtar_coz(girdi)
        adlar = self.adlar()
        if ad.casefold() in (a.casefold() for a in adlar):
            raise TotpHatasi(f"'{ad}' adında bir hesap zaten var.")
        # Önce gizli anahtar, sonra liste: yarıda kalırsa liste bozulmaz.
        keyring.set_password(self.servis, ad, ayar.kasa_degeri())
        if keyring.get_password(self.servis, ad) != ayar.kasa_degeri():
            raise TotpHatasi("Anahtar kasaya yazıldı ama geri okunamadı.")
        self._adlari_yaz(adlar + [ad])
        return ayar

    def ayar(self, ad: str) -> TotpAyari | None:
        deger = keyring.get_password(self.servis, ad)
        if deger is None:
            return None
        return anahtar_coz(deger)

    def sil(self, ad: str) -> None:
        try:
            keyring.delete_password(self.servis, ad)
        except keyring.errors.PasswordDeleteError:
            pass
        self._adlari_yaz([a for a in self.adlar() if a != ad])

    def hepsi(self) -> list[tuple[str, TotpAyari | None]]:
        """(ad, ayar) listesi; okunamayan kayıt için ayar None döner."""
        sonuc = []
        for ad in self.adlar():
            try:
                sonuc.append((ad, self.ayar(ad)))
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
            self.kopyala = ctk.CTkButton(self, text="Kopyala", width=78,
                                         command=self.kopyala_tikla)
            self.kopyala.grid(row=0, column=1, rowspan=2, padx=4)
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
            kod = self.ayar.kod()
            if kod != self.son_kod:  # yalnız değiştiğinde çiz
                self.son_kod = kod
                self.kod_etiketi.configure(text=kodu_bicimle(kod))

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

            guvenli, backend_adi = backend_guvenli_mi()
            self.kasa_hazir = guvenli
            if not guvenli:
                self.ekle_btn.configure(state="disabled")
                self.durum_yaz("Güvenli kasa bulunamadı: " + backend_adi, hata=True)
                messagebox.showerror(
                    UYGULAMA_ADI,
                    "Güvenli kimlik bilgisi kasası bulunamadı:\n" + backend_adi +
                    "\n\nAnahtarlar düz metne yazılmayacağı için ekleme kapatıldı.")
            else:
                self.durum_yaz("Kasa: " + backend_adi.rsplit(".", 1)[-1] + " · çevrimdışı")
            self.listeyi_yukle()
            self.tik()

        def durum_yaz(self, metin: str, hata: bool = False) -> None:
            self.durum.configure(text=metin, text_color="#E57373" if hata else "gray")

        def listeyi_yukle(self) -> None:
            for s in self.satirlar:
                s.destroy()
            self.satirlar.clear()
            hesaplar = self.kasa.hepsi() if self.kasa_hazir else []
            for i, (ad, ayar) in enumerate(hesaplar):
                satir = HesapSatiri(self.liste, self, ad, ayar)
                satir.grid(row=i, column=0, sticky="ew", padx=2, pady=3)
                self.satirlar.append(satir)
            if hesaplar:
                self.bos_etiket.grid_forget()
            else:
                self.bos_etiket.grid(row=0, column=0, pady=20)

        def hesap_ekle(self) -> None:
            ad = self.ad_girdi.get().strip()
            girdi = self.anahtar_girdi.get()
            if not ad:
                ad = onerilen_ad(girdi)
            try:
                self.kasa.ekle(ad, girdi)
            except TotpHatasi as exc:
                messagebox.showerror(UYGULAMA_ADI, str(exc))
                return
            except keyring.errors.KeyringError as exc:
                messagebox.showerror(UYGULAMA_ADI, f"Kasaya yazılamadı: {exc}")
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
            self.kasa.sil(ad)
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
            self._pano_is = None
            try:
                if self.clipboard_get() == self._pano_kod:
                    self.clipboard_clear()
                    self.clipboard_append("")
            except Exception:
                pass
            self._pano_kod = None

        def tik(self) -> None:
            # Her saniye saati kontrol et; 30 sn sınırında kodlar değişir.
            kalan = kalan_saniye()
            self.cubuk.set(kalan / VARSAYILAN_PERIYOT)
            self.sayac.configure(text=f"{int(kalan + 0.999)} sn",
                                 text_color="#E57373" if kalan <= 5 else ("gray90", "gray90"))
            for s in self.satirlar:
                s.guncelle()
            # Bir sonraki saniye başına hizalan (kaymayı önler).
            self._tik_is = self.after(max(50, int((1 - time.time() % 1) * 1000)), self.tik)

        def destroy(self) -> None:
            for is_ in (self._tik_is, self._pano_is):
                if is_:
                    self.after_cancel(is_)
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
        g, ad = backend_guvenli_mi()
        print(("GÜVENLİ" if g else "GÜVENSİZ") + ": " + ad)
        sys.exit(0 if g else 2)
    arayuzu_baslat()
