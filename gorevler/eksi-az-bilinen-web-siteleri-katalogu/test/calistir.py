#!/usr/bin/env python3
"""Tüm testleri çalıştırır ve gerçek çıktıyı test/cikti/ altına yazar.

Bölümler:
  1. Birim/entegrasyon testleri (SENTETİK fixture, yerel sunucu, sahte çözücü).
  2. Uçtan uca hat: SENTETİK sayfa JSON'ları -> tekillestir -> kontrol (gerçek ağ) -> uret.
  3. Gerçek ağ duman testi: birkaç kamu adresi + güvenlik reddi örnekleri (Ekşi verisi değil).
Kullanım: python test/calistir.py [--proxy-ad-ile] [--ag-yok]
"""
import datetime, json, os, platform, subprocess, sys

BURASI = os.path.dirname(os.path.abspath(__file__))
KOK = os.path.dirname(BURASI)
CIKTI = os.path.join(BURASI, 'cikti')
ORNEK = os.path.join(CIKTI, 'sentetik-ornek')
BETIK = os.path.join(KOK, 'araclar', 'katalog_olustur.py')
ETIKET = 'SENTETİK FIXTURE — GERÇEK EKŞİ VERİSİ DEĞİLDİR'
SENTETIK_ACIKLAMA = {
    'kategori': 'Sentetik Örnek',
    'ne': 'Bu satır, test için uydurulmuş bir adrese yazılmış sentetik bir açıklama cümlesidir.',
    'yapabilirsin': 'Bu siteyle hiçbir şey yapamazsın, çünkü yalnız katalog biçimini sınamak için uyduruldu.'}


def calis(komut, gunluk):
    gunluk.append('$ ' + ' '.join(os.path.relpath(c, KOK) if c.startswith(KOK) else c for c in komut))
    r = subprocess.run(komut, capture_output=True, text=True, cwd=KOK)
    gunluk.append((r.stdout + r.stderr).rstrip())
    gunluk.append(f'[çıkış kodu: {r.returncode}]')
    gunluk.append('')
    return r.returncode


def main():
    proxy_ad = '--proxy-ad-ile' in sys.argv
    ag_yok = '--ag-yok' in sys.argv
    os.makedirs(ORNEK, exist_ok=True)
    try:
        commit = subprocess.run(['git', 'rev-parse', '--short', 'HEAD'], capture_output=True, text=True, cwd=KOK).stdout.strip()
        kirli = subprocess.run(['git', 'status', '--porcelain', '.'], capture_output=True, text=True, cwd=KOK).stdout.strip()
    except OSError:
        commit, kirli = '?', ''
    g = [f'=== {ETIKET} ===', '',
         f'tarih (UTC): {datetime.datetime.now(datetime.timezone.utc).isoformat(timespec="seconds")}',
         f'python: {sys.version.split()[0]}  platform: {platform.platform()}',
         f'git: {commit}{" (+ commit edilmemiş değişiklik)" if kirli else ""}',
         f'proxy: {"var" if os.environ.get("HTTPS_PROXY") else "yok"}  proxy modu: {"ad" if proxy_ad else "ip"}', '']
    sonuc = {}

    g += ['## 1. Birim ve entegrasyon testleri (SENTETİK)', '']
    for t in ('test_katalog.py', 'test_url_denetim.py'):
        sonuc[t] = calis([sys.executable, os.path.join(BURASI, t), '-v'], g)

    g += ['## 2. Uçtan uca hat (SENTETİK sayfa JSON\'ları; adresler .example ve localhost)', '']
    s = os.path.join(ORNEK, 'siteler.json')
    a = os.path.join(ORNEK, 'aciklamalar.json')
    sonuc['tekillestir'] = calis([sys.executable, BETIK, 'tekillestir', os.path.join(BURASI, 'fixture', 'sentetik'),
                                  '-o', s, '--ust', '809', '--alt', '806'], g)
    if not ag_yok:
        sonuc['kontrol'] = calis([sys.executable, BETIK, 'kontrol', s] + (['--proxy-ad-ile'] if proxy_ad else []), g)
    calis([sys.executable, BETIK, 'sablon', s, '-o', a], g)
    with open(a, encoding='utf-8') as f:
        sablon = json.load(f)
    with open(a, 'w', encoding='utf-8') as f:
        json.dump({k: dict(SENTETIK_ACIKLAMA, _not=ETIKET) for k in sablon}, f, ensure_ascii=False, indent=1)
    sonuc['uret'] = calis([sys.executable, BETIK, 'uret', s, a, '-o', os.path.join(ORNEK, 'katalog.txt'),
                           '--rapor', os.path.join(ORNEK, 'rapor.json')], g)
    with open(os.path.join(ORNEK, 'katalog.txt'), encoding='utf-8') as f:
        katalog = f.read()
    with open(os.path.join(ORNEK, 'katalog.txt'), 'w', encoding='utf-8') as f:
        f.write(f'### {ETIKET} ###\n\n' + katalog)
    g += ['--- sentetik-ornek/katalog.txt ---', f'### {ETIKET} ###', katalog, '']

    if not ag_yok:
        g += ['## 3. Gerçek ağ duman testi (kamu adresler; Ekşi verisi değil)', '']
        sys.path.insert(0, os.path.join(KOK, 'araclar'))
        import url_denetim
        mod = 'ad' if proxy_ad else 'ip'
        for url in ('https://cloudconvert.com/', 'https://alternativeto.net/', 'http://example.com/',
                    'https://bu-alan-adi-yok-xyz123.com/', 'http://169.254.169.254/latest/meta-data',
                    'http://localhost/', 'https://10.0.0.1/', 'https://example.com:8443/'):
            r = url_denetim.denetle(url, proxy_modu=mod)
            g.append(f'{url} → {r["durum"]} kod={r.get("kod")} başlık="{r.get("baslik", "")[:50]}" '
                     f'zincir={[(z["kod"], z["ip"], z["ip_sabit"]) for z in r["zincir"]]} neden={r.get("neden", "")[:80]}')
        g.append('')

    basarisiz = [k for k, v in sonuc.items() if v not in (0,) and k != 'uret']
    g += ['## Özet', f'adımlar: {sonuc}', 'GENEL: ' + ('BAŞARILI' if not basarisiz else 'BAŞARISIZ: ' + ', '.join(basarisiz))]
    metin = '\n'.join(g) + '\n'
    with open(os.path.join(CIKTI, 'test-ciktisi.txt'), 'w', encoding='utf-8') as f:
        f.write(metin)
    print(metin)
    return 1 if basarisiz else 0


if __name__ == '__main__':
    sys.exit(main())
