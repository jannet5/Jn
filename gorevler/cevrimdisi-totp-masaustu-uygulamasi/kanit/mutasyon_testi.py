"""Mutasyon testi: çekirdekteki güvenlik denetimleri tek tek bozulur; testler her birini yakalamalı.

Kullanım (proje klasöründe): python kanit/mutasyon_testi.py <gecici_dizin>  (venv python yolu <dizin>/venv/bin/python)
"""
import subprocess, shutil, sys, os
S=sys.argv[1]; K=os.getcwd()
orj=open('uygulama/totp_masaustu.py',encoding='utf-8').read()
mutasyonlar = {
 "M1 bilinmeyen backend kabul": ('        raise KasaHatasi("İzin verilmeyen kasa arka ucu: " + ", ".join(reddedilen))', '        pass'),
 "M2 silme doğrulaması yok": ('        if self._oku(kullanici) is not None:\n            raise KasaHatasi("Kayıt kasadan silinemedi.")', '        pass'),
 "M3 indeks dışı kayda yazma": ('        if self._oku(ad) is not None:\n            raise KasaHatasi(', '        if False:\n            raise KasaHatasi('),
 "M4 ekle geri alma yok": ('            if not self._sil_sessiz(ad):  # geri alma: eklenen sırrı kaldır', '            if True:  # mutasyon\n                raise\n            if not self._sil_sessiz(ad):'),
 "M5 sil geri yükleme yok": ('            if eski_deger is not None:\n                try:\n                    self._yaz(ad, eski_deger)', '            if False:\n                try:\n                    self._yaz(ad, eski_deger)'),
 "M6 issuer eşitliği yok": ('    if issuer and etiket_issuer and issuer != etiket_issuer:', '    if False:'),
 "M7 yinelenen parametre kabul": ('        if ad in q:  # ör.', '        if False:  # ör.'),
 "M8 strip önce (kontrol kaçar)": ('    etiket = urllib.parse.unquote(parca.path.lstrip("/"))\n', '    etiket = urllib.parse.unquote(parca.path.lstrip("/")).strip()\n'),
 "M9 sır repr'de": ('    secret: str = field(repr=False)', '    secret: str = field(repr=True)'),
 "M10 politika her işlemde değil": ('        kr = keyring.get_keyring() if self._sabit_kr is None else self._sabit_kr\n        backend_dogrula(kr, self._izinli)', '        kr = keyring.get_keyring() if self._sabit_kr is None else self._sabit_kr'),
 "M11 bütünlük denetimi yok": ('            if hashlib.sha256(metin.encode("utf-8")).hexdigest() != ozet:', '            if False:'),
}
for ad,(eski,yeni) in mutasyonlar.items():
    assert eski in orj, ad
    d=f"{S}/mut/x"; shutil.rmtree(d,ignore_errors=True); shutil.copytree(K,d,ignore=shutil.ignore_patterns('kanit'))
    open(f"{d}/uygulama/totp_masaustu.py","w",encoding='utf-8').write(orj.replace(eski,yeni,1))
    r=subprocess.run([f"{S}/venv/bin/python","-m","pytest","testler","-q","-p","no:cacheprovider"],cwd=d,capture_output=True,text=True)
    son=r.stdout.strip().splitlines()[-1]
    print(("YAKALANDI " if r.returncode else "KAÇTI     ")+ad+"  -> "+son)
