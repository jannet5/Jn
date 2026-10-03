"""Arayüz kabul sürücüsü: gerçek pencereyi açar, kullanıcı akışını sürer, ölçer.

Windows'ta (veya Wine'da) gerçek Windows Credential Manager'ı ayrı bir test
servis adıyla kullanır; gerçek "My2FAApp" kayıtlarına dokunmaz. Linux'ta bellek
içi test kasası açık izinle kullanılır. Anahtar herkese açık bir örnektir.

Pano, uygulamanın DIŞINDAN okunur/yazılır (Linux: xclip, Windows: ayrı Tk kökü),
böylece "başka uygulamanın panosu" ve "kapanıştan sonra pano" gerçekten ölçülür.

Kullanım:  python kanit/gui_kabul.py <cikti_oneki>
"""
import json
import os
import subprocess
import sys
import time
import tkinter

KOK = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..")
sys.path.insert(0, os.path.join(KOK, "uygulama"))
sys.path.insert(0, os.path.join(KOK, "testler"))
from tkinter import messagebox  # noqa: E402

import totp_masaustu as tm  # noqa: E402

ONEK = sys.argv[1] if len(sys.argv) > 1 else "gui"
SAHTE = "JBSWY3DPEHPK3PXP"
SERVIS = "My2FAApp-gui-kabul"
WIN = sys.platform == "win32"
sonuc = {"platform": sys.platform, "adimlar": []}


def adim(ad, ok, detay=""):
    sonuc["adimlar"].append({"adim": ad, "gecti": bool(ok), "detay": str(detay)})
    print(("GEÇTİ " if ok else "KALDI ") + ad + (" | " + str(detay) if detay else ""), flush=True)


if WIN:
    def yeni_kasa():
        return tm.Kasa(SERVIS)  # varsayılan politika: yalnız WinVaultKeyring
else:
    from conftest import TEST_IZNI, BellekKasasi
    BELLEK = BellekKasasi()

    def yeni_kasa():
        return tm.Kasa(SERVIS, kr=BELLEK, izinli=TEST_IZNI)

kayitlar = []
messagebox.showerror = lambda baslik, metin: kayitlar.append(("hata", metin))
messagebox.askyesno = lambda baslik, metin: kayitlar.append(("soru", metin)) or True

# -- uygulama dışı pano erişimi -------------------------------------------------
_yardimci = None
AKTIF = None  # şu an açık uygulama penceresi (pano okurken olay döngüsü için)


def dis_pano_oku():
    if WIN:
        try:
            return _yardimci.clipboard_get()
        except tkinter.TclError:
            return ""
    # Pano sahibi bu süreçteki Tk penceresi olabilir: okurken olay döngüsü dönmeli,
    # yoksa xclip'in seçim isteğine kimse cevap veremez (kilitlenme).
    p = subprocess.Popen(["xclip", "-o", "-selection", "clipboard"],
                         stdout=subprocess.PIPE, stderr=subprocess.DEVNULL, text=True)
    son = time.time() + 5
    while p.poll() is None and time.time() < son:
        if AKTIF is not None:
            try:
                AKTIF.update()
            except tkinter.TclError:
                pass
        time.sleep(0.02)
    if p.poll() is None:
        p.kill()
        return "<zaman aşımı>"
    return p.stdout.read() if p.returncode == 0 else ""


def dis_pano_yaz(metin):
    if WIN:
        _yardimci.clipboard_clear()
        _yardimci.clipboard_append(metin)
        _yardimci.update()
    else:
        subprocess.run(["xclip", "-i", "-selection", "clipboard"], input=metin, text=True,
                       check=True, timeout=5)
        time.sleep(0.3)


def bekle(app, sn):
    son = time.time() + sn
    while time.time() < son:
        app.update()
        time.sleep(0.02)


def ekran(ad):
    yol = os.path.join(os.path.dirname(__file__), f"{ONEK}-{ad}.png")
    if WIN:
        from PIL import ImageGrab
        ImageGrab.grab().save(yol)
    else:
        subprocess.run(["import", "-window", "root", yol], check=True)
    return yol


def resizable_degil(app):
    # app.resizable() sorgusu Windows'ta CTk başlık çubuğunu yeniden çizdirir; Tk'ye doğrudan sorulur.
    return tuple(map(int, app.tk.splitlist(app.tk.call("wm", "resizable", app._w)))) == (0, 0)


def enter_bas(app):
    app.anahtar_girdi._entry.focus_force()
    app.update()
    app.anahtar_girdi._entry.event_generate("<Return>")
    app.update()


kasa = yeni_kasa()
for ad in kasa.adlar():
    kasa.sil(ad)

# ============ A) Fail-closed: izinsiz kasa, Ekle düğmesi ve Enter kısayolu ============
class IzinsizKasa:
    def __init__(self):
        self.cagrilar = []

    def get_password(self, *a):
        self.cagrilar.append(a)
        return None

    def set_password(self, *a):
        self.cagrilar.append(a)

    def delete_password(self, *a):
        self.cagrilar.append(a)


izinsiz = IzinsizKasa()
app = tm.uygulama_olustur(tm.Kasa(SERVIS + "-izinsiz", kr=izinsiz))
bekle(app, 2.0)
adim("İzinsiz kasa: uygulama fail-closed açıldı",
     not app.kasa_hazir and app.ekle_btn.cget("state") == "disabled"
     and kayitlar and kayitlar[-1][0] == "hata", kayitlar[-1][1].splitlines()[1] if kayitlar else "")
app.ad_girdi.insert(0, "Enter-denemesi")
app.anahtar_girdi.insert(0, SAHTE)
enter_bas(app)
app.hesap_ekle()  # doğrudan çağrı da engellenmeli
adim("Enter ve doğrudan hesap_ekle kapalı Ekle'yi aşamadı",
     izinsiz.cagrilar == [], f"arka uç çağrısı={len(izinsiz.cagrilar)}; durum='{app.durum.cget('text')}'")
ekran("0-izinsiz-kasa")
app.destroy()

# ============ B) Normal akış ============
app = AKTIF = tm.uygulama_olustur(yeni_kasa())
bekle(app, 3.0)  # Windows'ta başlık çubuğu rengi için pencere bir kez gizlenip açılır
if WIN:
    _yardimci = tkinter.Tk()
    _yardimci.withdraw()
adim("Pencere sabit boyutlu ve koyu tema",
     resizable_degil(app) and __import__("customtkinter").get_appearance_mode() == "Dark",
     f"geometri={app.winfo_width()}x{app.winfo_height()}")
adim("Güvenli kasa + sağlık testi geçti", app.kasa_hazir, app.durum.cget("text"))
adim("Boş liste mesajı", app.bos_etiket.winfo_ismapped())
ekran("1-bos")

app.ad_girdi.insert(0, "Hatalı")
app.anahtar_girdi.insert(0, "BU-GECERSIZ-1")
app.ekle_btn.invoke()
bekle(app, 0.3)
adim("Geçersiz anahtar reddedildi, hata mesajı girdiyi yansıtmıyor",
     kayitlar[-1][0] == "hata" and kasa.adlar() == [] and "GECERSIZ" not in kayitlar[-1][1],
     kayitlar[-1][1])
app.ad_girdi.delete(0, "end")
app.anahtar_girdi.delete(0, "end")

app.ad_girdi.insert(0, "GitHub - ornek")
app.anahtar_girdi.insert(0, "jbsw y3dp ehpk 3pxp")
adim("Gizli anahtar maskeli girilir", app.anahtar_girdi.cget("show") == "•")
app.ekle_btn.invoke()
bekle(app, 0.3)
# Enter ile ekleme (pozitif kontrol: kısayol, kasa hazırken çalışır); ad URI etiketinden
app.anahtar_girdi.insert(0, f"otpauth://totp/GitHub:ikinci?secret={SAHTE}&issuer=GitHub")
enter_bas(app)
bekle(app, 0.3)
app.anahtar_girdi.insert(0, f"otpauth://totp/Ornek60:hesap?secret={SAHTE}&period=60&issuer=Ornek60")
app.ekle_btn.invoke()
bekle(app, 0.5)
beklenen_adlar = ["GitHub - ornek", "GitHub:ikinci", "Ornek60:hesap"]
adim("Üç hesap eklendi (düğme + Enter + 60 sn URI)", kasa.adlar() == beklenen_adlar, kasa.adlar())
adim("Ekleme sonrası anahtar alanı temizlendi", app.anahtar_girdi.get() == "")

beklenen = tm.kodu_bicimle(tm.TotpAyari(SAHTE).kod())
gorunen = app.satirlar[0].kod_etiketi.cget("text")
if gorunen != beklenen:  # sınır anına denk geldiyse bir tik bekle
    bekle(app, 1.1)
    beklenen = tm.kodu_bicimle(tm.TotpAyari(SAHTE).kod())
    gorunen = app.satirlar[0].kod_etiketi.cget("text")
adim("Ekrandaki 6 haneli kod beklenen TOTP", gorunen == beklenen, f"ekranda={gorunen}")

a60 = tm.anahtar_coz(f"otpauth://totp/x?secret={SAHTE}&period=60")
satir60 = app.satirlar[2]
bekle(app, 0.2)
s60 = satir60.sure_etiketi.cget("text") if satir60.sure_etiketi else ""
beklenen_s60 = f"{tm.sayac_metni(a60.kalan())} / 60"
adim("60 sn'lik hesap kendi sayacını gösteriyor (30 sn hesaplarda ayrı sayaç yok)",
     abs(int(s60.split()[0]) - int(beklenen_s60.split()[0])) <= 1 and s60.endswith("/ 60")
     and app.satirlar[0].sure_etiketi is None,
     f"ekranda='{s60}' beklenen≈'{beklenen_s60}' kod={satir60.kod_etiketi.cget('text')}")
adim("60 sn'lik hesabın kodu kendi periyoduyla", satir60.kod_etiketi.cget("text") == tm.kodu_bicimle(a60.kod()))

kalan = tm.kalan_saniye()
adim("İlerleme çubuğu kalan süreyi gösteriyor", abs(app.cubuk.get() - kalan / 30) < 0.08,
     f"cubuk={app.cubuk.get():.2f} beklenen={kalan / 30:.2f} sayac={app.sayac.cget('text')}")
ekran("2-kodlar")

# ============ C) Gerçek zamanlı 30 sn pano temizleme + 30 sn kod yenileme ============
app.satirlar[0].kopyala.invoke()
bekle(app, 0.3)
t0 = time.time()
kopyalanan = dis_pano_oku()
adim("Kopyala: başka uygulamadan okunan pano = ekrandaki kod", kopyalanan == gorunen.replace(" ", "")
     or kopyalanan == tm.TotpAyari(SAHTE).kod(), f"pano={kopyalanan}")
eski_kod = app.satirlar[0].kod_etiketi.cget("text")
bekle(app, 28.5 - (time.time() - t0))
ara = dis_pano_oku()
adim("28,5. saniyede pano henüz temizlenmedi", ara == kopyalanan, f"pano={ara!r}")
bekle(app, 31.5 - (time.time() - t0))
son = dis_pano_oku()
adim("Gerçek zamanlı ~30 sn sonra pano kendiliğinden temizlendi (zamanlayıcı, elle çağrı yok)",
     son == "", f"gecen={time.time() - t0:.1f} sn pano={son!r}")
yeni_kod = app.satirlar[0].kod_etiketi.cget("text")
adim("30 sn sınırında kod otomatik yenilendi",
     yeni_kod != eski_kod and yeni_kod == tm.kodu_bicimle(tm.TotpAyari(SAHTE).kod()), f"{eski_kod} -> {yeni_kod}")

# ============ D) Sil hatası başarı gibi gösterilmez ============
gercek_sil = app.kasa._sil
app.kasa._sil = lambda ad: (_ for _ in ()).throw(tm.KasaHatasi("Kayıt kasadan silinemedi."))
app.hesap_sil("GitHub:ikinci")
bekle(app, 0.3)
app.kasa._sil = gercek_sil
adim("Silinemeyen kayıt: hata gösterildi, satır listede kaldı",
     kayitlar[-1] == ("hata", "Kayıt kasadan silinemedi.") and "silinemedi" in app.durum.cget("text")
     and [s.ad for s in app.satirlar] == beklenen_adlar, app.durum.cget("text"))

# ============ E) Kapanışta pano: kendi kodu temizlenir, başkasınınki korunur ============
app.satirlar[0].kopyala.invoke()
bekle(app, 0.3)
dis_pano_yaz("kullanicinin-baska-metni")
app.destroy()
AKTIF = None
time.sleep(0.5)
adim("Kapanışta başka uygulamanın pano içeriğine dokunulmadı",
     dis_pano_oku() == "kullanicinin-baska-metni", repr(dis_pano_oku()))

# Ölçüm noktası: uygulamanın destroy()'u kendi temizliğini yaptıktan SONRA, pencere
# gerçekten yok edilmeden hemen ÖNCE pano dışarıdan okunur (CTk.destroy sarmalanır).
import customtkinter as _ctk  # noqa: E402
_orijinal_destroy = _ctk.CTk.destroy
KAPANIS_PANO = []


def _izleyen_destroy(self):
    KAPANIS_PANO.append(dis_pano_oku())
    _orijinal_destroy(self)


_ctk.CTk.destroy = _izleyen_destroy


def kapanis_sonrasi():
    if WIN:  # Windows panosu süreçten bağımsız kalır: kapanıştan sonra da okunur
        return dis_pano_oku()
    return "<X11: sahip kapanınca pano zaten boşalır; ölçülmedi>"


# Kontrol deneyi: uygulamanın temizliği devre dışıyken kod kapanışta panoda duruyor mu?
app = AKTIF = tm.uygulama_olustur(yeni_kasa())
bekle(app, 2.0)
app.satirlar[0].kopyala.invoke()
bekle(app, 0.3)
kontrol_kod = dis_pano_oku()
app._pano_kod = None  # yalnız kontrol için: temizlik kapalı
app.destroy()
AKTIF = None
time.sleep(0.5)
kontrol_sonra = kapanis_sonrasi()
adim("Kontrol (temizlik kapalı): kapanış anında kod hâlâ panoda -> ölçüm ayırt edici",
     kontrol_kod != "" and KAPANIS_PANO[-1] == kontrol_kod,
     f"kapanış anı={KAPANIS_PANO[-1]!r} kapanış sonrası={kontrol_sonra!r}")
if WIN:
    adim("Kontrol (temizlik kapalı): Windows'ta kod kapanıştan sonra da panoda kalıyor",
         kontrol_sonra == kontrol_kod, repr(kontrol_sonra))
    dis_pano_yaz("")

app = AKTIF = tm.uygulama_olustur(yeni_kasa())
bekle(app, 2.0)
app.satirlar[0].kopyala.invoke()
bekle(app, 0.3)
kopya = dis_pano_oku()
app.destroy()  # 30 sn dolmadan normal kapanış
AKTIF = None
time.sleep(0.5)
sonra = kapanis_sonrasi()
adim("Erken kapanış: 30 sn dolmadan kapatınca kendi kodu panodan temizlendi",
     kopya != "" and KAPANIS_PANO[-1] == "" and (sonra == "" if WIN else True),
     f"kopyalanan={kopya} kapanış anı={KAPANIS_PANO[-1]!r} kapanış sonrası={sonra!r}")

# ============ F) Yeniden açılış, silme, temizlik ============
app = tm.uygulama_olustur(yeni_kasa())
bekle(app, 1.5)
adim("Yeniden açılışta hesaplar kasadan okundu", [s.ad for s in app.satirlar] == beklenen_adlar)
ekran("3-yeniden-acilis")
app.hesap_sil("GitHub:ikinci")
bekle(app, 0.3)
adim("Silme onay sorar, kasadan kaldırır ve doğrular",
     kayitlar[-1][0] == "soru" and kasa.adlar() == ["GitHub - ornek", "Ornek60:hesap"]
     and kasa.ayar("GitHub:ikinci") is None)
for ad in list(kasa.adlar()):
    app.hesap_sil(ad)
app.destroy()
adim("Temizlik: test kayıtları kaldırıldı", yeni_kasa().adlar() == [])
if _yardimci is not None:
    _yardimci.destroy()

sonuc["ozet"] = f"{sum(a['gecti'] for a in sonuc['adimlar'])}/{len(sonuc['adimlar'])}"
print("ÖZET", sonuc["ozet"])
with open(os.path.join(os.path.dirname(__file__), f"{ONEK}-sonuc.json"), "w", encoding="utf-8") as f:
    json.dump(sonuc, f, ensure_ascii=False, indent=2)
sys.exit(0 if all(a["gecti"] for a in sonuc["adimlar"]) else 1)
