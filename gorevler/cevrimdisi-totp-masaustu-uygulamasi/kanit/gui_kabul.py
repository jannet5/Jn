"""Arayüz kabul sürücüsü: gerçek pencereyi açar, kullanıcı akışını sürer, ölçer.

Windows'ta (veya Wine'da) gerçek Windows Credential Manager'ı ayrı bir test
servis adıyla kullanır; gerçek "My2FAApp" kayıtlarına dokunmaz. Linux'ta bellek
içi kasa kullanır. Kullanılan anahtar yalnız test içindir, hiçbir hesaba ait değildir.

Kullanım:  python kanit/gui_kabul.py <cikti_oneki>
"""
import json
import os
import subprocess
import sys
import time

sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "uygulama"))
import keyring  # noqa: E402
import keyring.backend  # noqa: E402
from tkinter import messagebox  # noqa: E402

import totp_masaustu as tm  # noqa: E402

ONEK = sys.argv[1] if len(sys.argv) > 1 else "gui"
SAHTE = "JBSWY3DPEHPK3PXP"
SERVIS = "My2FAApp-gui-kabul"
sonuc = {"platform": sys.platform, "adimlar": []}


def adim(ad, ok, detay=""):
    sonuc["adimlar"].append({"adim": ad, "gecti": bool(ok), "detay": str(detay)})
    print(("GEÇTİ " if ok else "KALDI ") + ad + (" | " + str(detay) if detay else ""), flush=True)


if sys.platform != "win32":
    class Bellek(keyring.backend.KeyringBackend):
        priority = 1
        veri = {}

        def get_password(self, s, u):
            return self.veri.get((s, u))

        def set_password(self, s, u, p):
            self.veri[(s, u)] = p

        def delete_password(self, s, u):
            self.veri.pop((s, u), None)
    keyring.set_keyring(Bellek())

kayitlar = []
messagebox.showerror = lambda baslik, metin: kayitlar.append(("hata", metin))
messagebox.askyesno = lambda baslik, metin: kayitlar.append(("soru", metin)) or True

kasa = tm.Kasa(SERVIS)
for ad in kasa.adlar():
    kasa.sil(ad)


def bekle(app, sn):
    son = time.time() + sn
    while time.time() < son:
        app.update()
        time.sleep(0.02)


def ekran(ad):
    yol = os.path.join(os.path.dirname(__file__), f"{ONEK}-{ad}.png")
    if sys.platform == "win32":
        from PIL import ImageGrab
        ImageGrab.grab().save(yol)
    else:
        subprocess.run(["import", "-window", "root", yol], check=True)
    return yol


app = tm.uygulama_olustur(kasa)
bekle(app, 3.0)  # Windows'ta başlık çubuğu rengi için pencere bir kez gizlenip açılır
# Not: app.resizable() sorgusu Windows'ta CTk başlık çubuğunu yeniden çizdirir; Tk'ye doğrudan sorulur.
adim("Pencere sabit boyutlu ve koyu tema", tuple(map(int, app.tk.splitlist(app.tk.call("wm", "resizable", app._w)))) == (0, 0)
     and __import__("customtkinter").get_appearance_mode() == "Dark",
     f"geometri={app.winfo_width()}x{app.winfo_height()}")
adim("Güvenli kasa algılandı", app.kasa_hazir, app.durum.cget("text"))
adim("Boş liste mesajı", app.bos_etiket.winfo_ismapped(),
     f"pencere={app.state()} gorunur={app.winfo_ismapped()}")
ekran("1-bos")

# Geçersiz anahtar reddedilmeli, kasaya bir şey yazılmamalı
app.ad_girdi.insert(0, "Hatalı")
app.anahtar_girdi.insert(0, "BU-GECERSIZ-1")
app.ekle_btn.invoke()
bekle(app, 0.3)
adim("Geçersiz anahtar reddedildi", kayitlar and kayitlar[-1][0] == "hata" and kasa.adlar() == [],
     kayitlar[-1][1].splitlines()[0] if kayitlar else "")
app.ad_girdi.delete(0, "end")
app.anahtar_girdi.delete(0, "end")

# Hesap 1: setup key (boşluklu küçük harf, GitHub "setup key" kopyalama biçimi)
app.ad_girdi.insert(0, "GitHub - ornek")
app.anahtar_girdi.insert(0, "jbsw y3dp ehpk 3pxp")
adim("Gizli anahtar maskeli girilir", app.anahtar_girdi.cget("show") == "•")
app.ekle_btn.invoke()
bekle(app, 0.3)
# Hesap 2: otpauth URI, ad boş -> etiket önerilir
app.anahtar_girdi.insert(0, f"otpauth://totp/GitHub:ikinci?secret={SAHTE}&issuer=GitHub")
app.ekle_btn.invoke()
bekle(app, 0.5)
adim("İki hesap eklendi ve kasaya yazıldı", kasa.adlar() == ["GitHub - ornek", "GitHub:ikinci"],
     kasa.adlar())
adim("Kasada düz base32 anahtar (kaynaktaki set_password biçimi)",
     keyring.get_password(SERVIS, "GitHub - ornek") == SAHTE)
adim("Ekleme sonrası anahtar alanı temizlendi", app.anahtar_girdi.get() == "")

beklenen = tm.kodu_bicimle(tm.TotpAyari(SAHTE).kod())
gorunen = app.satirlar[0].kod_etiketi.cget("text")
if gorunen != beklenen:  # sınır anına denk geldiyse bir tik bekle
    bekle(app, 1.1)
    beklenen = tm.kodu_bicimle(tm.TotpAyari(SAHTE).kod())
    gorunen = app.satirlar[0].kod_etiketi.cget("text")
adim("Ekrandaki 6 haneli kod beklenen TOTP", gorunen == beklenen and len(gorunen.replace(" ", "")) == 6,
     f"ekranda={gorunen}")

kalan = tm.kalan_saniye()
adim("İlerleme çubuğu kalan süreyi gösteriyor", abs(app.cubuk.get() - kalan / 30) < 0.08,
     f"cubuk={app.cubuk.get():.2f} beklenen={kalan / 30:.2f} sayac={app.sayac.cget('text')}")

app.satirlar[0].kopyala.invoke()
bekle(app, 0.3)
pano = app.clipboard_get()
adim("Kopyala: panoda boşluksuz 6 haneli kod", pano == beklenen.replace(" ", ""), f"pano={pano}")
ekran("2-kodlar")

# 30 sn sınırını bekle: kod kendiliğinden değişmeli
eski = app.satirlar[0].kod_etiketi.cget("text")
bekle(app, tm.kalan_saniye() + 1.3)
yeni = app.satirlar[0].kod_etiketi.cget("text")
adim("30 sn sınırında kod otomatik yenilendi",
     yeni != eski and yeni == tm.kodu_bicimle(tm.TotpAyari(SAHTE).kod()), f"{eski} -> {yeni}")
adim("Yeni periyotta çubuk dolu", app.cubuk.get() > 0.85, f"{app.cubuk.get():.2f}")

# Pano 30 sn sonra temizlenmeli (kopyalanan kod hâlâ panodaysa)
app.satirlar[0].kopyala.invoke()
app._pano_is and app.after_cancel(app._pano_is)
app.panoyu_temizle()
bekle(app, 0.2)
adim("Pano otomatik temizleme", app.clipboard_get() == "")

# Yeniden açılış: hesaplar kasadan geri okunmalı
app.destroy()
app = tm.uygulama_olustur(tm.Kasa(SERVIS))
bekle(app, 1.0)
adim("Yeniden açılışta hesaplar kasadan okundu",
     [s.ad for s in app.satirlar] == ["GitHub - ornek", "GitHub:ikinci"])
ekran("3-yeniden-acilis")

app.hesap_sil("GitHub:ikinci")
bekle(app, 0.3)
adim("Silme onay sorar ve kasadan kaldırır",
     kayitlar[-1][0] == "soru" and kasa.adlar() == ["GitHub - ornek"]
     and keyring.get_password(SERVIS, "GitHub:ikinci") is None)
app.hesap_sil("GitHub - ornek")
app.destroy()
adim("Temizlik: test kayıtları kaldırıldı", tm.Kasa(SERVIS).adlar() == [])

sonuc["ozet"] = f"{sum(a['gecti'] for a in sonuc['adimlar'])}/{len(sonuc['adimlar'])}"
print("ÖZET", sonuc["ozet"])
with open(os.path.join(os.path.dirname(__file__), f"{ONEK}-sonuc.json"), "w", encoding="utf-8") as f:
    json.dump(sonuc, f, ensure_ascii=False, indent=2)
sys.exit(0 if all(a["gecti"] for a in sonuc["adimlar"]) else 1)
