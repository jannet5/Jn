"""Kullanıcının kendi bilgisayarında tek komutla kurulum, kaldırma ve yerel
doğrulama.

* ``kur``: ``~/.claude/settings.json`` dosyasına statusline (kota kaydı) ve
  Stop kancası (her yanıt sonunda rapor) ekler. Önce yedek alır; mevcut
  kancalara dokunmaz; başka bir statusline varsa ``--statusline-degistir``
  verilmedikçe onu değiştirmez. Tekrar çalıştırmak bir şeyi çoğaltmaz.
* ``kaldir``: yalnız bu aracın eklediği girdileri siler.
* ``dogrula``: bu bilgisayardaki gerçek Claude ve Codex kayıtlarını okur;
  hangi verinin erişilebilir olduğunu, ilk raporları ve sonucu
  ``~/.claude/sohbet-olcer/yerel_dogrulama.txt`` dosyasına yazar. Kimlik
  bilgisi okumaz, hiçbir şey göndermez.
"""

import json
import os
import sys
from datetime import datetime, timezone

ISARET = "sohbet_olcer"  # komut satırında bu aracı tanıtan iz


def ayar_yolu():
    kok = os.environ.get("CLAUDE_CONFIG_DIR") or os.path.join(os.path.expanduser("~"), ".claude")
    return os.path.join(kok, "settings.json")


def _komut(alt):
    olc = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "olc.py")
    # Boşluklu yollar (C:\Users\Ad Soyad\...) için tırnak; komutta iz bulunur.
    return f'"{sys.executable}" "{olc}" {alt}'


def _yedekle(yol):
    """Bayt bayt kopya (shutil.copy2, Windows'ta CopyFile2 ister; Wine'da yok)."""
    yedek = f"{yol}.yedek-{datetime.now():%Y%m%d-%H%M%S}"
    with open(yol, "rb") as kaynak, open(yedek, "wb") as hedef:
        hedef.write(kaynak.read())
    return yedek


def _oku(yol):
    if not os.path.exists(yol):
        return {}
    with open(yol, encoding="utf-8") as f:
        metin = f.read()
    if not metin.strip():
        return {}
    veri = json.loads(metin)  # bozuksa hata ver, dosyaya dokunma
    if not isinstance(veri, dict):
        raise ValueError("settings.json bir JSON nesnesi değil")
    return veri


def _yaz(yol, veri):
    os.makedirs(os.path.dirname(yol), exist_ok=True)
    gecici = yol + ".sohbet-olcer.tmp"
    with open(gecici, "w", encoding="utf-8", newline="\n") as f:
        json.dump(veri, f, ensure_ascii=False, indent=2)
        f.write("\n")
    os.replace(gecici, yol)


def _bizim(komut):
    """Komut bu araca mı ait? (olc.py + kanca/kaydet ya da paket adı)"""
    if not isinstance(komut, str):
        return False
    return ISARET in komut or ("olc.py" in komut and ("kanca" in komut or "kaydet" in komut))


def kur(yol=None, statusline_degistir=False):
    yol = yol or ayar_yolu()
    try:
        veri = _oku(yol)
    except (ValueError, json.JSONDecodeError) as e:
        return 2, [f"settings.json okunamadı ({e}); dosyaya dokunulmadı. Önce dosyayı düzeltin."]
    notlar = []
    if os.path.exists(yol):
        yedek = _yedekle(yol)
        notlar.append(f"Yedek alındı: {yedek}")
    # Stop kancası
    hooks = veri.setdefault("hooks", {})
    stop = hooks.setdefault("Stop", [])
    var = any(_bizim(h.get("command")) for grup in stop if isinstance(grup, dict)
              for h in grup.get("hooks", []) if isinstance(h, dict))
    if var:
        notlar.append("Stop kancası zaten kurulu; değiştirilmedi.")
    else:
        stop.append({"hooks": [{"type": "command", "command": _komut("kanca")}]})
        notlar.append("Stop kancası eklendi: her yanıt sonunda kısa rapor gösterilecek.")
    # Statusline
    mevcut = veri.get("statusLine")
    if mevcut and not _bizim((mevcut or {}).get("command")) and not statusline_degistir:
        notlar.append("Başka bir statusline zaten var; değiştirilmedi. Kota kaydı için "
                      "'kur --statusline-degistir' çalıştırın (eskisi yedekte kalır). "
                      "Statusline olmadan kota 'erişilemiyor' görünür.")
    elif mevcut and _bizim(mevcut.get("command")):
        notlar.append("Statusline zaten kurulu; değiştirilmedi.")
    else:
        veri["statusLine"] = {"type": "command", "command": _komut("kaydet")}
        notlar.append("Statusline eklendi: 5 saatlik ve haftalık kota kaydedilecek.")
    _yaz(yol, veri)
    notlar.append(f"Ayar dosyası: {yol}. Claude Code'u yeniden başlatın.")
    return 0, notlar


def kaldir(yol=None):
    yol = yol or ayar_yolu()
    try:
        veri = _oku(yol)
    except (ValueError, json.JSONDecodeError) as e:
        return 2, [f"settings.json okunamadı ({e}); dosyaya dokunulmadı."]
    if not veri:
        return 0, ["Kurulu bir şey yok."]
    _yedekle(yol)
    notlar = []
    if _bizim((veri.get("statusLine") or {}).get("command")):
        del veri["statusLine"]
        notlar.append("Statusline kaldırıldı.")
    stop = (veri.get("hooks") or {}).get("Stop")
    if isinstance(stop, list):
        yeni = []
        for grup in stop:
            if isinstance(grup, dict):
                kalan = [h for h in grup.get("hooks", []) if not _bizim((h or {}).get("command"))]
                if len(kalan) != len(grup.get("hooks", [])):
                    notlar.append("Stop kancası kaldırıldı.")
                if kalan:
                    yeni.append(dict(grup, hooks=kalan))
            else:
                yeni.append(grup)
        if yeni:
            veri["hooks"]["Stop"] = yeni
        else:
            del veri["hooks"]["Stop"]
            if not veri["hooks"]:
                del veri["hooks"]
    _yaz(yol, veri)
    return 0, notlar or ["Bu araca ait girdi bulunamadı."]


def dogrula(cikti=None, tz="Europe/Istanbul"):
    """Gerçek yerel verilerle doğrulama raporu üretir."""
    from . import codex, kota, rapor, transkript
    simdi = datetime.now(timezone.utc)
    p = [f"Yerel doğrulama, {simdi.astimezone():%Y-%m-%d %H:%M} ({sys.platform}, Python "
         f"{sys.version.split()[0]})."]
    # Claude
    kok = os.path.join(os.path.dirname(ayar_yolu()), "projects")
    okuma = transkript.oku([kok]) if os.path.isdir(kok) else None
    ozetler = transkript.oturumlara_ayir(okuma) if okuma else {}
    if not ozetler:
        p.append(f"Claude Code transkripti: erişilemiyor ({kok} altında token içeren kayıt yok).")
    else:
        son = max(ozetler.values(), key=lambda o: o.son or simdi.replace(year=1970))
        p.append(f"Claude Code transkripti: {len(okuma.dosyalar)} dosya, {len(ozetler)} konuşma "
                 f"bulundu; tekilleştirilen tekrar satır {okuma.tekrar_satir}.")
        kayit = os.path.join(os.path.dirname(ayar_yolu()), "sohbet-olcer", "kota.jsonl")
        goruntuler = kota.dosyadan_oku(kayit)[0] if os.path.exists(kayit) else []
        if not goruntuler:
            p.append("Claude kota kaydı: henüz yok. 'kur' çalıştırıldıktan sonra Claude Code'da "
                     "bir mesaj gönderin; statusline ilk yanıttan sonra kotayı kaydeder.")
        veri = rapor.rapor_verisi(okuma, ozetler, son, goruntuler, simdi, bool(goruntuler))
        p.append("En son Claude konuşmasının raporu:\n\n" + rapor.metin_raporu(veri, okuma, ozetler, tz).strip())
    # Codex
    okc = codex.oku([codex.varsayilan_kok()])
    if not okc.konusmalar:
        p.append(f"Codex rollout kaydı: erişilemiyor ({codex.varsayilan_kok()} altında token_count yok).")
    else:
        son = max(okc.konusmalar.values(), key=lambda c: c.son or simdi.replace(year=1970))
        p.append(f"Codex rollout kaydı: {len(okc.dosyalar)} dosya, {len(okc.konusmalar)} konuşma.")
        p.append("En son Codex konuşmasının raporu:\n\n" + codex.metin_raporu(okc, son, simdi, tz).strip())
    # Kurulum durumu
    try:
        ayar = _oku(ayar_yolu())
    except (ValueError, json.JSONDecodeError):
        ayar = {}
    kanca = any(_bizim(h.get("command")) for g in (ayar.get("hooks") or {}).get("Stop", [])
                if isinstance(g, dict) for h in g.get("hooks", []) if isinstance(h, dict))
    sl = _bizim((ayar.get("statusLine") or {}).get("command"))
    p.append(f"Kurulum: Stop kancası {'kurulu' if kanca else 'kurulu değil'}, statusline "
             f"{'kurulu' if sl else 'kurulu değil'}.")
    metin = "\n\n".join(p) + "\n"
    cikti = cikti or os.path.join(os.path.dirname(ayar_yolu()), "sohbet-olcer", "yerel_dogrulama.txt")
    os.makedirs(os.path.dirname(cikti), exist_ok=True)
    with open(cikti, "w", encoding="utf-8") as f:
        f.write(metin)
    return metin, cikti
