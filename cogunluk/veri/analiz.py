#!/usr/bin/env python3
"""Bölüm 01 (veri haritası) için tekrarlanabilir analiz.

Girdi : veri/ekser_ayetleri_tam.json  (Arapçasında أكثر kalıbı geçen 81 ham eşleşme)
        veri/kuran_tam.json           (bütün Kur'an; ar = Osmanî, tr = Diyanet İşleri meali)
        veri/chapters_quran_com.json  (api.quran.com/api/v4/chapters?language=tr; yoksa indirilir)
Çıktı : veri/analiz.json              (sınıflama + sayılar)
        veri/svg/*.svg                (sayılardan üretilen SVG parçaları; bölüm HTML'ine gömülür)

Çalıştırma:  python3 -I veri/analiz.py
SINIFLAMA (yüklem, özne türü, kim) elle yapılmıştır; yazarına aittir. Kod yalnızca sayar,
doğrular (assert) ve çizer. Belirsiz olan '?' olarak bırakılmıştır.
"""
import json, os, re, sys, urllib.request
from collections import Counter, OrderedDict

D = os.path.dirname(os.path.abspath(__file__))
P = lambda *a: os.path.join(D, *a)
K = json.load(open(P('kuran_tam.json'), encoding='utf-8'))
AR, TR = K['ar'], K['tr']
RAW = json.load(open(P('ekser_ayetleri_tam.json'), encoding='utf-8'))
RAW_KEYS = [x['ayet'] for x in RAW]

CH_FILE = P('chapters_quran_com.json')
if not os.path.exists(CH_FILE):
    urllib.request.urlretrieve('https://api.quran.com/api/v4/chapters?language=tr', CH_FILE)
CH = {c['id']: c for c in json.load(open(CH_FILE, encoding='utf-8'))['chapters']}


def strip(s):
    s = re.sub('[\u064B-\u065F\u0670\u06D6-\u06ED\u0640]', '', s)
    return s.replace('\u0671', '\u0627').replace('\u0623', '\u0627').replace('\u0625', '\u0627').replace('\u0622', '\u0627')


def sure(key):
    return int(key.split(':')[0])


# ---------------------------------------------------------------- 1) KAPSAM DIŞI
# Arapçada أكثر geçiyor ama "çoğu şöyledir" türünden bir çoğunluk hükmü değil:
# karşılaştırma ("daha çok mal"), fiil ("çok tartıştın", "bozgunculuğu artırdılar"), şeylerin çokluğu.
KAPSAM_DISI = OrderedDict([
    ('4:12',  'karşılaştırma: "bundan fazla" (miras payı)'),
    ('9:69',  'karşılaştırma: "malları ve çocukları daha çok"'),
    ('11:32', 'fiil: "çok tartıştın" (أكثرت)'),
    ('17:6',  'karşılaştırma, olumlu: "sayıca daha çok kıldık" (أكثر نفيرا)'),
    ('18:34', 'karşılaştırma: "malca senden zenginim"'),
    ('18:54', 'üstünlük: "insan en çok tartışan varlıktır" (çoğunluk hükmü değil, tür niteliği)'),
    ('27:76', 'şeylerin çokluğu: "ayrılığa düştükleri şeyin çoğunu" (kişi değil, konu)'),
    ('28:78', 'karşılaştırma: "daha çok topladı" (Kârûn kıssası)'),
    ('30:9',  'karşılaştırma: "onlardan çok imar etmişlerdi"'),
    ('34:35', 'karşılaştırma: "malları ve çocukları en çok olan biziz" (müşriklerin iddiası)'),
    ('40:82', 'karşılaştırma: "kendilerinden daha çok ve daha güçlü"'),
    ('58:7',  'karşılaştırma: "az veya çok" (Allah'+"'"+'ın ilmi/ma'+"'"+'iyyeti)'),
    ('89:12', 'fiil: "bozgunculuğu artırdılar" (فأكثروا)'),
])

# ---------------------------------------------------------------- 2) ELLE SINIFLAMA (68 ayet)
# alanlar: yuklem (birincil), ikinci (varsa ek yüklemler), ozne: A / A* / B / ?
#   A  = Arapçada lafzen "insanların çoğu" (أكثر الناس) ya da "yeryüzündekilerin çoğu"
#   A* = zamirle ("çoğu"), öncülü genel "insan(lar)"; bağlam belirli bir topluluğa daraltmıyor
#   B  = belirli topluluk (kim alanı)       ?  = öncül belirsiz, uydurulmadı
YK = {  # yüklem kısaltmaları (Arapça ifade)
    'bilmez': 'لا يعلمون / يجهلون', 'iman': 'لا يؤمنون', 'sukur': 'لا يشكرون', 'yuz': 'أعرض',
    'fasik': 'فاسقون', 'zan': 'يتبع إلا ظنا', 'ortak': 'مشركون', 'akil': 'لا يعقلون',
    'kufur': 'كافرون / كفورا / كاذبون', 'hak': 'للحق كارهون', 'diger': '—'}
S = {}
def s(key, yuklem, ozne, kim, guven='yüksek', ikinci=(), detay='', not_=''):
    S[key] = dict(yuklem=yuklem, ikinci=list(ikinci), ozne=ozne, kim=kim, guven=guven, detay=detay, not_=not_)

MEK = 'Mekkeli/Arap müşrikler ve inkârcılar'
KE = 'Kitap ehli'
GK = 'geçmiş kavim/ümmet'
DG = 'diğer belirli grup'
# --- A: lafzen "insanların çoğu"
for k, y, d in [('2:243', 'şükretmez', 'لا يشكرون'), ('7:187', 'bilmez', 'لا يعلمون'), ('11:17', 'iman etmez', 'لا يؤمنون'),
                ('12:21', 'bilmez', 'لا يعلمون'), ('12:38', 'şükretmez', 'لا يشكرون'), ('12:40', 'bilmez', 'لا يعلمون'),
                ('12:68', 'bilmez', 'لا يعلمون'), ('12:103', 'iman etmez', 'بمؤمنين'), ('13:1', 'iman etmez', 'لا يؤمنون'),
                ('16:38', 'bilmez', 'لا يعلمون'), ('30:6', 'bilmez', 'لا يعلمون'), ('30:30', 'bilmez', 'لا يعلمون'),
                ('34:28', 'bilmez', 'لا يعلمون'), ('34:36', 'bilmez', 'لا يعلمون'), ('40:57', 'bilmez', 'لا يعلمون'),
                ('40:59', 'iman etmez', 'لا يؤمنون'), ('40:61', 'şükretmez', 'لا يشكرون'), ('45:26', 'bilmez', 'لا يعلمون')]:
    s(k, y, 'A', 'insanlar (genel)', detay=d)
s('17:89', 'kâfir/nankör/yalancı', 'A', 'insanlar (genel)', detay='أبى ... إلا كفورا (nankörlükte direnmek)', not_='nankör (küfûr)')
s('25:50', 'kâfir/nankör/yalancı', 'A', 'insanlar (genel)', detay='أبى ... إلا كفورا (nankörlükte direnmek)', not_='nankör (küfûr)')
s('6:116', 'zanna uyar', 'A', 'yeryüzündekiler (genel)', ikinci=['saptırır'], detay='إن يتبعون إلا الظن', not_='ayetin asıl hükmü bir uyarıdır: çoğunluğa itaat edersen seni saptırırlar')
# --- A*: zamirle, öncül genel insan
s('7:17', 'şükretmez', 'A*', 'Âdemoğulları (İblis\'in iddiası)', 'yüksek', detay='لا تجد أكثرهم شاكرين', not_='söyleyen İblis\'tir; Kur\'an bunu 34:20\'de "iman edenlerden bir topluluk hariç" diyerek aktarır')
s('10:60', 'şükretmez', 'A*', 'insanlar (öncül: "insanlara nimeti boldur")', 'orta', detay='لا يشكرون', not_='aynı bağlamda "Allah\'a yalan uyduranlar" (10:59-60) geçer')
s('12:106', 'ortak koşar', 'A*', 'insanlar (öncül 12:103 "en-nâs")', 'orta', ikinci=['iman eder ama ortak koşar'], detay='وما يؤمن أكثرهم بالله إلا وهم مشركون', not_='İbn Abbâs: "Allah yarattı" derler, yine de ortak koşarlar (İbn Kesîr)')
s('27:73', 'şükretmez', 'A*', 'insanlar (öncül: "insanlara lütuf sahibidir")', 'yüksek', detay='لا يشكرون')
s('39:49', 'bilmez', 'A*', 'insan (el-insân, tür)', 'yüksek', detay='لا يعلمون', not_='nimeti "bilgimle kazandım" diyen insan tipi; sıkıntıda yalvarıp nimette unutan')
# --- ? belirsiz
s('28:13', 'bilmez', '?', 'öncül belirsiz', 'düşük', detay='لا يعلمون', not_='Mûsâ\'nın annesine verilen sözün gerçekliği; "çoğu" kimdir ayetten kesin okunmuyor')
s('10:55', 'bilmez', '?', 'öncül belirsiz (çevrede inkârcılar/zalimler)', 'düşük', detay='لا يعلمون', not_='10:54 zalimlerden söz eder; zamirin öncülü açık değil')
# --- B: Mekkeli/Arap müşrikler
s('6:37', 'bilmez', 'B', MEK, detay='"Ona Rabbinden bir mucize indirilseydi" diyenler')
s('6:111', 'bilmez', 'B', MEK, detay='بل... يجهلون: melekler inse de inanmazlardı')
s('8:34', 'bilmez', 'B', MEK, detay='Mescid-i Harâm\'dan engelleyenler; "Allah\'ın dostları değiller"')
s('9:8', 'fasık', 'B', MEK, 'orta', detay='ahit yapılan müşrikler (9:1-8)', not_='Tevbe 9:7-8: ahdi gözetmeyen müşrikler; "çoğu fasık" (azı ahde bağlı: 9:7)')
s('10:36', 'zanna uyar', 'B', MEK, 'orta', detay='"ortaklarınızdan hakka ileten var mı" (10:35) bağlamı')
s('16:75', 'bilmez', 'B', MEK, detay='köle/hür misali: Allah\'tan başkasına tapanlara')
s('16:83', 'kâfir/nankör/yalancı', 'B', MEK, 'orta', detay='"nimeti bilirler, sonra inkâr ederler"', not_='kâfir (küfür)')
s('16:101', 'bilmez', 'B', MEK, detay='"Sen uyduruyorsun" diyenler')
s('21:24', 'bilmez', 'B', MEK, ikinci=['yüz çevirir'], detay='tanrılar edinenler')
s('23:70', 'haktan hoşlanmaz', 'B', MEK, detay='"Onda delilik var" diyenler')
s('25:44', 'akletmez', 'B', MEK, ikinci=['dinlemez'], detay='25:41-44: Peygamber\'le alay edenler')
s('26:8', 'iman etmez', 'B', MEK, 'orta', detay='26:1-8 bağlamı: Kur\'an\'dan yüz çevirenler')
s('27:61', 'bilmez', 'B', MEK, detay='"Allah\'la beraber başka ilah mı?"')
s('28:57', 'bilmez', 'B', MEK, detay='"Seninle doğru yola girersek yurdumuzdan edilirik" diyenler')
s('29:63', 'akletmez', 'B', MEK, detay='"Gökten suyu kim indirir?" "Allah" derler')
s('31:25', 'bilmez', 'B', MEK, detay='"Gökleri kim yarattı?" "Allah" derler')
s('34:41', 'ortak koşar', 'B', MEK, detay='"Çoğu cinlere inanıyordu" (melekler konuşuyor)', not_='yüklem olumlu görünür (مؤمنون) ama nesnesi cinlerdir')
s('36:7', 'iman etmez', 'B', MEK, detay='"hüküm çoğunun aleyhine gerçekleşti"; 36:6 babaları uyarılmamış kavim')
s('39:29', 'bilmez', 'B', MEK, detay='ortaklı köle/tek efendili adam misali')
s('41:4', 'yüz çevirir', 'B', MEK, ikinci=['işitmez'], detay='أعرض أكثرهم', not_='Arapçada "أكثرهم" (onların çoğu); "insanların" kelimesini Diyanet mealine yerleştirmiş')
s('44:39', 'bilmez', 'B', MEK, 'orta', detay='44:34-35: yeniden dirilişi inkâr edenler', not_='Arapçada "أكثرهم"; "insanların" kelimesi Diyanet mealinde eklenmiş')
s('52:47', 'bilmez', 'B', MEK, detay='"zulmedenlere bundan başka da azap vardır"')
s('5:103', 'akletmez', 'B', MEK, 'orta', detay='bahîra, sâibe, vasîle, hâm adakları: cahiliye uygulaması')
# --- B: Kitap ehli
s('2:100', 'iman etmez', 'B', KE, detay='ahdi bozan Yahudi topluluğu (Medine, 2:97-101)')
s('3:110', 'fasık', 'B', KE, detay='"Kitap ehli... içlerinde inananlar var, çoğu yoldan çıkmıştır"', not_='aynı cümlede "minhumu\'l-mu\'minûn" (içlerinden inananlar da var)')
s('5:59', 'fasık', 'B', KE, detay='"Ey Kitap ehli! ... çoğunuzun fasık olması"')
# --- B: geçmiş kavimler
s('7:102', 'fasık', 'B', GK, detay='önceki şehir/ülke halkları (7:94-102); "çoğunda ahde bağlılık görmedik"', ikinci=['ahde vefasız'])
s('7:131', 'bilmez', 'B', GK, detay='Firavun\'un kavmi')
s('26:67', 'iman etmez', 'B', GK, 'orta', detay='Mûsâ kıssası sonu (26:10-68); Firavun ve çevresi', not_='refren; Mûsâ kıssasında kimin kastedildiği bağlamdan kesin okunmaz')
s('26:103', 'iman etmez', 'B', GK, 'orta', detay='İbrâhîm kıssası sonu (26:69-104)')
s('26:121', 'iman etmez', 'B', GK, 'orta', detay='Nûh kıssası sonu (26:105-122)')
s('26:139', 'iman etmez', 'B', GK, 'orta', detay='Âd/Hûd kıssası sonu (26:123-140)')
s('26:158', 'iman etmez', 'B', GK, 'orta', detay='Semûd/Sâlih kıssası sonu (26:141-159)')
s('26:174', 'iman etmez', 'B', GK, 'orta', detay='Lût kıssası sonu (26:160-175)')
s('26:190', 'iman etmez', 'B', GK, 'orta', detay='Eyke halkı/Şuayb kıssası sonu (26:176-191)')
s('30:42', 'ortak koşar', 'B', GK, detay='"daha öncekilerden çoğu ortak koşanlardı"')
s('37:71', 'diğer (sapmak)', 'B', GK, detay='"evvelki ümmetlerin çoğu sapmıştı"')
# --- B: diğer belirli gruplar
s('26:223', 'kâfir/nankör/yalancı', 'B', 'şeytanlara kulak veren günahkâr iftiracılar (kâhin tipi)', 'yüksek', detay='26:221-223: "Şeytanlar kimlere iner?" "Her günahkâr iftiracıya"', not_='yalancı; İbn Kesîr: "kâhinler ve diğer günahkâr yalancılar"')
s('49:4', 'akletmez', 'B', 'odaların ardından Peygamber\'e seslenenler (bedevî)', 'yüksek', detay='İbn Kesîr: "odaların ardından seslenen bedevîler"', not_='sebeb-i nüzûl rivayetlerinde Temîm\'den bir kişi anılır (İbn Kesîr/Ahmed)')
s('43:78', 'haktan hoşlanmaz', 'B', 'cehennemdeki suçlular (ahiret sahnesinde hitap)', 'yüksek', detay='43:74-78: nöbetçiyle konuşan cehennemlikler', not_='dünyada değil, ahiret sahnesinde söylenen bir tespit')

YUKLEM_SIRA = ['bilmez', 'iman etmez', 'şükretmez', 'akletmez', 'fasık', 'kâfir/nankör/yalancı', 'ortak koşar',
               'haktan hoşlanmaz', 'zanna uyar', 'yüz çevirir', 'diğer (sapmak)']

# ---------------------------------------------------------------- doğrulamalar
tam_kume = set(RAW_KEYS)
assert len(RAW_KEYS) == len(tam_kume) == 81, 'ham liste 81 benzersiz ayet olmalı'
assert set(KAPSAM_DISI) <= tam_kume
assert set(S) <= tam_kume
kalan = tam_kume - set(KAPSAM_DISI) - set(S)
assert not kalan, f'sınıflanmamış: {sorted(kalan)}'
assert not (set(S) & set(KAPSAM_DISI))
assert len(S) == 68 and len(KAPSAM_DISI) == 13 and 68 + 13 == 81
for k, v in S.items():
    assert v['yuklem'] in YUKLEM_SIRA, (k, v['yuklem'])
    assert v['ozne'] in ('A', 'A*', 'B', '?')
# Arapça kontrol: A sınıfındaki her ayette lafzen "أكثر الناس" ya da "أكثر من في الأرض" geçmeli, diğerlerinde geçmemeli
for k, v in S.items():
    a = strip(AR[k])
    lafzen = bool(re.search(r'اكثر (الناس|من ف[يى] الارض)', a))
    assert lafzen == (v['ozne'] == 'A'), (k, v['ozne'], lafzen)
# her ham ayet meal ve Arapçada gerçekten var
for k in RAW_KEYS:
    assert 'كثر' in strip(AR[k]), k

# ---------------------------------------------------------------- 3) SAYILAR
n_hepsi = len(RAW_KEYS)
ham = {x['ayet']: x for x in RAW}
birincil = Counter(v['yuklem'] for v in S.values())
anilan = Counter()
for v in S.values():
    anilan[v['yuklem']] += 1
    for i in v['ikinci']:
        if i in YUKLEM_SIRA:
            anilan[i] += 1
ozne = Counter(v['ozne'] for v in S.values())
kimler_B = Counter(v['kim'] for v in S.values() if v['ozne'] == 'B')
kimler_grup = Counter()
for v in S.values():
    if v['ozne'] == 'B':
        kimler_grup[v['kim'] if v['kim'] in (MEK, KE, GK) else DG] += 1
# yüklem x özne
yuklem_ozne = {y: Counter(v['ozne'] for v in S.values() if v['yuklem'] == y) for y in YUKLEM_SIRA}
for y in YUKLEM_SIRA:
    assert sum(yuklem_ozne[y].values()) == birincil[y]
assert sum(birincil.values()) == 68 and sum(ozne.values()) == 68 and sum(kimler_grup.values()) == ozne['B']

# Mekkî/Medenî (quran.com chapters.revelation_place; âlimler bazı surelerde ayrılır)
def yer(key):
    return CH[sure(key)]['revelation_place']
def mm(keys):
    c = Counter(yer(k) for k in keys)
    return {'makkah': c.get('makkah', 0), 'madinah': c.get('madinah', 0)}
mm_ham = mm(RAW_KEYS); mm_hukum = mm(S.keys()); mm_ozne = {o: mm([k for k, v in S.items() if v['ozne'] == o]) for o in ('A', 'A*', 'B', '?')}
mm_kimB = {g: mm([k for k, v in S.items() if v['ozne'] == 'B' and (v['kim'] if v['kim'] in (MEK, KE, GK) else DG) == g]) for g in (MEK, KE, GK, DG)}
sure_mm = Counter(c['revelation_place'] for c in CH.values())
ayet_mm = Counter()
for c in CH.values():
    ayet_mm[c['revelation_place']] += c['verses_count']
# Kur'an'ın kendi dağılımı (karşılaştırma tabanı)
taban_ayet_pay_mekki = ayet_mm['makkah'] / sum(ayet_mm.values())
sure_say = Counter(sure(k) for k in S)          # sure -> hüküm ayeti sayısı
sure_say_ham = Counter(sure(k) for k in RAW_KEYS)
harita = {n: sure_say.get(n, 0) for n in range(1, 115)}
assert sum(harita.values()) == 68
assert sum(ayet_mm.values()) == 6236 == len(AR)
en_cok = sorted(sure_say.items(), key=lambda t: (-t[1], t[0]))

# Medenî mi? (hüküm ayetleri)
medeni_ayet = sorted([k for k in S if yer(k) == 'madinah'], key=lambda x: tuple(map(int, x.split(':'))))

# ---------------------------------------------------------------- 4) 14'LÜ PAYLAŞIM LİSTESİ ÇAPRAZ KONTROL
PAYLASIM = [('Nahl 83', '16:83'), ('Mâide 48', '5:49'), ('Rûm 42', '30:42'), ('İsrâ 89', '17:89'), ('Yûnus 92', '10:92'),
            ('Şuarâ 223', '26:223'), ('Yûnus 36', '10:36'), ('Furkân 50', '25:50'), ('Bakara 243', '2:243'),
            ('Bakara 100', '2:100'), ('Zuhruf 78', '43:78'), ('Yûsuf 106', '12:106'), ('Fussılet 4', '41:4'), ('Mâide 103', '5:103')]
paylasim = []
for etiket, k in PAYLASIM:
    a = strip(AR[k])
    kok = 'ekser (أكثر)' if re.search(r'اكثر', a) else ('kesîr (كثير)' if 'كثير' in a else 'yok')
    paylasim.append(dict(etiket=etiket, ayet=k, kalip=kok, ozne=S[k]['ozne'] if k in S else None,
                         kim=S[k]['kim'] if k in S else None))
paylasim_ekser = sum(1 for p in paylasim if p['kalip'].startswith('ekser'))
assert paylasim_ekser == 12 and len(paylasim) == 14
assert [p['ayet'] for p in paylasim if p['kalip'].startswith('kes')] == ['5:49', '10:92']

# ---------------------------------------------------------------- 5) KESÎR (كثير = "çok/birçok") YAN TARAMASI
# كثير = "çok, birçok" demektir; "çoğu/çoğunluk" anlamını baskın taşıyan أكثر'den farklıdır.
# 6236 ayette كثير geçen ayetler koddan bulundu; kişilere ilişkin "çoğu/birçoğu" hükmü taşıyanlar elle seçilip etiketlendi.
kesir_tum = [k for k in AR if 'كثير' in strip(AR[k])]
KES = OrderedDict()
def kz(k, nitelik, ozne, ne, dy):
    KES[k] = dict(nitelik=nitelik, ozne=ozne, ne=ne, diyanet_kelime=dy)
kz('2:26',  'olumlu-nötr', 'insanlar', 'Allah misalle birçoğunu saptırır, birçoğunu doğru yola iletir', 'birçoğu')
kz('2:109', 'olumsuz', 'Kitap ehli', 'çoğu kıskançlıkla sizi küfre döndürmek ister', 'çoğu')
kz('5:49',  'olumsuz', 'insanlar', 'çoğu fasıktır', 'çoğu')
kz('5:62',  'olumsuz', 'Kitap ehli', 'çoğu günaha ve haksızlığa koşuşur', 'çoğu')
kz('5:64',  'olumsuz', 'Kitap ehli', 'indirilenler çoğunun azgınlığını artırır', 'çoğu')
kz('5:66',  'olumsuz', 'Kitap ehli', 'bir zümre orta yolda; çoğunun işledikleri kötü', 'çoğu')
kz('5:68',  'olumsuz', 'Kitap ehli', 'indirilenler çoğunun azgınlık ve küfrünü artırır', 'çoğu')
kz('5:71',  'olumsuz', 'Kitap ehli', 'tevbeleri kabulden sonra çoğu yine körleşti', 'çoğu')
kz('5:80',  'olumsuz', 'Kitap ehli', 'çoğunun inkâr edenleri dost edindiği', 'çoğu')
kz('5:81',  'olumsuz', 'Kitap ehli', 'çoğu fasıktır', 'çoğu')
kz('6:119', 'olumsuz', 'insanlar', 'çoğunluk heva ile bilmeden sapıtır', 'çoğunluk')
kz('7:179', 'olumsuz', 'cin ve insan', 'cehennem için birçok cin ve insan yaratıldı (kalpleri var anlamazlar)', 'birçok')
kz('9:34',  'olumsuz', 'hahamlar ve rahipler', 'çoğu insanların mallarını haksızlıkla yer', 'çoğu')
kz('10:92', 'olumsuz', 'insanlar', 'çoğu ayetlerimizden habersizdir', 'çoğu')
kz('14:36', 'olumsuz', 'insanlar', 'putlar çok insanı saptırdı (İbrâhim\'in duası)', 'çok')
kz('22:18', 'olumlu-nötr', 'insanlar', 'insanların birçoğu Allah\'a secde eder; birçoğu da azabı hak etmiştir', 'birçoğu')
kz('27:15', 'olumlu-nötr', 'mü\'min kullar', 'Dâvûd ve Süleymân: "mü\'min kullarının çoğundan üstün kıldı"', 'çoğu')
kz('30:8',  'olumsuz', 'insanlar', 'çoğu Rablerine kavuşmayı inkâr eder', 'çoğu')
kz('36:62', 'olumsuz', 'insanlar', 'şeytan sizden nice nesilleri saptırdı', 'nice')
kz('38:24', 'olumsuz', 'ortaklar', 'ortakçıların çoğu birbirine haksızlık eder; inanıp salih amel işleyenler hariç, ne kadar azdır', 'çoğu')
kz('57:16', 'olumsuz', 'Kitap ehli', 'çoğunun kalbi katılaştı, fasıklaştı', 'çoğu')
kz('57:26', 'karışık', 'Nûh ve İbrâhim soyu', 'kimi doğru yolda, birçoğu yoldan çıkmış', 'birçoğu')
kz('57:27', 'olumsuz', 'İsâ\'ya uyanlar', 'çoğu fasık (rahbâniyet icadı)', 'çoğu')
kz('71:24', 'olumsuz', 'Nûh kavminin önderleri', 'birçoğunu saptırdılar', 'birçoğu')
kz('2:249', 'olumlu-nötr', 'Tâlût\'un askerleri', '"Nice az topluluk çok topluluğa Allah\'ın izniyle galip gelmiştir"', 'çok')
kz('3:146', 'olumlu-nötr', 'peygamberlerin yanında savaşanlar', 'nice peygamberle birlikte pek çok Rabbânî savaştı', 'pek çok')
for k in KES:
    assert k in AR and 'كثير' in strip(AR[k]), k
kes_nitelik = Counter(v['nitelik'] for v in KES.values())

# ---------------------------------------------------------------- 6) "AZ" KALIPLARI (قليل / قلة)
az_tum = [k for k in AR if re.search(r'قليل|قلة', strip(AR[k]))]
YOK_AZ = {'35:18': 'yanlış eşleşme: مثقلة ("yükü ağır") içinde قلة harfleri'}
AZ = OrderedDict()
def az(k, tur, alt='', ikili=False):
    AZ[k] = dict(tur=tur, alt=alt, ikili_okuma=ikili)
KISI = 'az kişi (azınlık)'; DAV = 'az davranış (az şükür/iman/öğüt)'; DEG = 'az bedel (ثمنا قليلا)'; ZAM = 'az zaman/geçici zevk'; MIK = 'az şey/miktar/algı'
# az kişi: alt = azınlığın niteliği
for k, alt, ik in [('2:83', 'azınlık dönmeyenler (İsrailoğulları)', False), ('2:88', 'azınlık inananlar (Medine Yahudileri)', True),
                   ('2:246', 'azınlık savaşa devam edenler', False), ('2:249', 'azınlık: sudan içmeyenler; "az topluluk çok topluluğa galip"', False),
                   ('4:46', 'azınlık inananlar', True), ('4:66', 'azınlık itaat edenler', False), ('4:83', 'azınlık şeytana uymayanlar', False),
                   ('4:155', 'azınlık inananlar', True), ('5:13', 'azınlık hainlik etmeyenler', False),
                   ('11:40', 'Nûh\'la birlikte inananlar az', False), ('11:116', 'kurtulanlar az', False),
                   ('17:62', 'İblis: "azı hariç" soyunu saptıracağım', False), ('18:22', 'mağara ehlinin sayısını az kişi bilir (nötr)', False),
                   ('34:13', 'şükreden kullar az', False), ('38:24', 'iman edip salih amel işleyenler az', False),
                   ('56:14', 'sonrakilerden az (cennetin öndekileri)', False), ('26:54', 'Firavun: "bunlar az bir topluluk" (Mûsâ\'ya uyanlar)', False),
                   ('7:86', '"az idiniz, sizi çoğalttı" (nimet olarak)', False), ('8:26', '"yeryüzünde az ve zayıf idiniz" (nimet hatırlatması)', False),
                   ('28:58', 'Diyanet: "pek az kimseler oturabilmiştir" (zaman/kişi okuması belirsiz)', True)]:
    az(k, KISI, alt, ik)
for k in ['7:3', '7:10', '23:78', '27:62', '32:9', '40:58', '67:23', '69:41', '69:42', '4:142', '48:15', '33:18', '33:20']:
    az(k, DAV)
for k in ['2:41', '2:79', '2:174', '3:77', '3:187', '3:199', '5:44', '9:9', '16:95']:
    az(k, DEG)
for k in ['2:126', '3:197', '4:77', '9:38', '9:82', '16:117', '17:52', '17:76', '23:40', '23:114', '31:24', '33:16', '33:60',
          '39:8', '44:15', '73:2', '73:3', '73:11', '77:46', '51:17']:
    az(k, ZAM)
for k in ['8:43', '8:44', '12:47', '12:48', '17:74', '17:85', '34:16', '53:34']:
    az(k, MIK)
assert set(AZ) | set(YOK_AZ) == set(az_tum), sorted((set(AZ) | set(YOK_AZ)) ^ set(az_tum))
az_tur = Counter(v['tur'] for v in AZ.values())
az_kisi_ayet = [k for k, v in AZ.items() if v['tur'] == KISI]

# ---------------------------------------------------------------- 7) JSON
def meal(k):
    return TR[k].strip()
ayetler = []
for k in sorted(RAW_KEYS, key=lambda x: tuple(map(int, x.split(':')))):
    if k in S:
        v = S[k]
        ayetler.append(dict(ayet=k, sure=CH[sure(k)]['translated_name']['name'], yer=yer(k), kapsam='hüküm', **v,
                            ar_kelime=None, meal=meal(k) if len(meal(k)) < 400 else meal(k)[:400] + '…'))
    else:
        ayetler.append(dict(ayet=k, sure=CH[sure(k)]['translated_name']['name'], yer=yer(k), kapsam='kapsam dışı',
                            neden=KAPSAM_DISI[k], meal=meal(k) if len(meal(k)) < 400 else meal(k)[:400] + '…'))
for a in ayetler:
    a.pop('ar_kelime', None)

OUT = OrderedDict()
OUT['_aciklama'] = ('Sınıflama bu çalışmanın yazarına aittir. Veri: Kur\'an metni ve Diyanet İşleri meali (api.quran.com, çeviri id 77); '
                    'Mekkî/Medenî: api.quran.com /chapters revelation_place (âlimler bazı surelerde ayrılır). '
                    'ekser = Arapçada أكثر kalıbı; ham eşleşme 81 ayet.')
OUT['ozet'] = dict(ham_eslesme=n_hepsi, hukum_ayeti=len(S), kapsam_disi=len(KAPSAM_DISI),
                   hukum_ayeti_sure_sayisi=len(sure_say), olumlu_notr_hukum=0)
OUT['yuklem_birincil'] = {y: birincil[y] for y in YUKLEM_SIRA}
OUT['yuklem_anilan'] = {y: anilan[y] for y in YUKLEM_SIRA}
OUT['yuklem_x_ozne'] = {y: dict(yuklem_ozne[y]) for y in YUKLEM_SIRA}
OUT['ozne'] = dict(ozne)
OUT['ozne_genel_toplam'] = ozne['A'] + ozne['A*']
OUT['ozne_B_kim'] = {g: kimler_grup[g] for g in (MEK, KE, GK, DG)}
OUT['mekki_medeni'] = dict(
    ham_81=mm_ham, hukum_68=mm_hukum, ozne=mm_ozne, B_kim=mm_kimB,
    kuran_sure_sayisi=dict(sure_mm), kuran_ayet_sayisi=dict(ayet_mm),
    kuran_mekki_ayet_payi=round(taban_ayet_pay_mekki, 4),
    hukum_mekki_payi=round(mm_hukum['makkah'] / 68, 4),
    medeni_hukum_ayetleri=medeni_ayet)
OUT['sure_basina_hukum_ayeti'] = {str(n): c for n, c in sorted(sure_say.items())}
OUT['en_cok_sure'] = [dict(sure_no=n, ad=CH[n]['translated_name']['name'], yer=CH[n]['revelation_place'], adet=c) for n, c in en_cok[:10]]
OUT['paylasim_14_liste'] = paylasim
OUT['paylasim_ekser_sayisi'] = paylasim_ekser
OUT['kesir_yan_tarama'] = dict(
    aciklama='كثير = "çok/birçok". Tam sayım değil: كثير geçen ayetlerden kişilere ilişkin "çoğu/birçoğu" hükmü taşıyanlar elle seçildi.',
    kesir_geçen_ayet_sayisi=len(kesir_tum), secilen=len(KES), nitelik=dict(kes_nitelik), ayetler=KES)
OUT['az_taramasi'] = dict(
    aciklama='قليل/قلة geçen ayetler; yanlış eşleşme çıkarıldı. "Az" birçok farklı şeyi niteler; yalnızca "az kişi" grubu azınlık ifadesidir.',
    eslesen_ayet=len(az_tum), yanlis_eslesme=YOK_AZ, sayim={t: az_tur[t] for t in (KISI, DAV, DEG, ZAM, MIK)},
    az_kisi_ayetleri=az_kisi_ayet, ayrintili=AZ)
OUT['sozluk_yontem'] = dict(
    kok='ك-ث-ر',
    kaynaklar=[
        dict(eser='İbn Manzûr, Lisânü\'l-Arab, "كثر"', metin='والكُثْرُ معظم الشيء وأَكْثَرُه', anlam='"kuthr", bir şeyin büyük kısmı, çoğu',
             url='https://arabiclexicon.hawramani.com/%D9%83%D8%AB%D8%B1/'),
        dict(eser='Edward W. Lane, Arabic-English Lexicon, "كُثْرٌ"', metin='The greater, or greatest, or main, part, of a thing; the most thereof. (K)',
             url='https://arabiclexicon.hawramani.com/%D9%83%D8%AB%D8%B1/'),
        dict(eser='Zebîdî, Tâcü\'l-Arûs, "كثر" (hawramani sayfasındaki alıntı)', metin='الكُثْرُ هُوَ: مُعْظَمُ الشَّيْءِ وأَكْثَرُهُ', anlam='aynı',
             url='https://arabiclexicon.hawramani.com/%D9%83%D8%AB%D8%B1/')],
    not_='Raghıb el-İsfahânî, el-Müfredât: hawramani sayfası "وأكثرهم للحق كارهون" ayetini أكثر için örnek verir; tanım metni sayfadan doğrulanamadığı için alıntılanmadı.')
OUT['ayetler'] = ayetler

json.dump(OUT, open(P('analiz.json'), 'w', encoding='utf-8'), ensure_ascii=False, indent=1)

# ---------------------------------------------------------------- 8) SVG ÜRETİMİ (sayılardan)
SV = P('svg'); os.makedirs(SV, exist_ok=True)
F = 'style="font-family:inherit"'

def svg_wrap(w, h, label, body):
    return f'<svg viewBox="0 0 {w} {h}" role="img" aria-label="{label}">\n{body}\n</svg>\n'

# --- SVG-1: sure haritası (114 hücre, 10 sütun)
def harita_svg():
    cs, gap, x0, y0 = 44, 4, 8, 8
    cols = 10
    rows = (114 + cols - 1) // cols
    W = x0 * 2 + cols * (cs + gap) - gap
    legend_y = y0 + rows * (cs + gap) + 14
    H = legend_y + 118
    out = []
    for n in range(1, 115):
        r, c = divmod(n - 1, cols)
        x, y = x0 + c * (cs + gap), y0 + r * (cs + gap)
        cnt = harita[n]
        med = CH[n]['revelation_place'] == 'madinah'
        if cnt == 0:
            fill, op, tcol = 'var(--surface)', 1, 'var(--ink-soft)'
        elif cnt == 1:
            fill, op, tcol = 'var(--accent)', .28, 'var(--ink)'
        elif cnt <= 3:
            fill, op, tcol = 'var(--accent)', .55, 'var(--ink)'
        else:
            fill, op, tcol = 'var(--accent)', 1, 'var(--accent-ink)'
        stroke = 'var(--gold)' if med else 'var(--line)'
        sw = 3 if med else 1.5
        out.append(f'<rect x="{x}" y="{y}" width="{cs}" height="{cs}" rx="5" style="fill:{fill};fill-opacity:{op};stroke:{stroke};stroke-width:{sw}"/>')
        out.append(f'<text x="{x + cs / 2}" y="{y + cs / 2 + 6}" text-anchor="middle" {F} style="font-family:inherit;font-size:16px;font-weight:{700 if cnt else 400};fill:{tcol}">{n}</text>')
    # lejant
    ly = legend_y
    items = [('yok', 'var(--surface)', 1, 'var(--ink)'), ('1', 'var(--accent)', .28, 'var(--ink)'), ('2–3', 'var(--accent)', .55, 'var(--ink)'), ('4+', 'var(--accent)', 1, 'var(--accent-ink)')]
    out.append(f'<text x="{x0}" y="{ly + 14}" style="font-family:inherit;font-size:16px;fill:var(--ink)">Her kutu bir sure; sayı = sure numarası. Dolgu = o surede kaç ayet:</text>')
    x = x0
    for lab, fl, op, tc in items:
        out.append(f'<rect x="{x}" y="{ly + 28}" width="44" height="30" rx="5" style="fill:{fl};fill-opacity:{op};stroke:var(--line);stroke-width:1.5"/>')
        out.append(f'<text x="{x + 22}" y="{ly + 49}" text-anchor="middle" style="font-family:inherit;font-size:16px;font-weight:700;fill:{tc}">{lab}</text>')
        x += 56
    out.append(f'<rect x="{x0}" y="{ly + 72}" width="44" height="30" rx="5" style="fill:var(--surface);stroke:var(--gold);stroke-width:3"/>')
    out.append(f'<text x="{x0 + 56}" y="{ly + 93}" style="font-family:inherit;font-size:16px;fill:var(--ink)">altın çerçeve = Medenî sure (api.quran.com)</text>')
    label = (f'Kur\'an\'ın 114 suresinin haritası. "Çoğu" hükmü taşıyan {len(S)} ayet {len(sure_say)} surede dağılmıştır; '
             'en yoğun sureler: ' + ', '.join(f"{CH[n]['translated_name']['name']} {c}" for n, c in en_cok[:6]))
    return svg_wrap(W, H, label, '\n'.join(out))

# --- SVG-2: yüklemlere göre yatay çubuk, özneye göre bölünmüş
def yuklem_svg():
    W, left, right = 540, 12, 44
    bw = W - left - right
    mx = max(birincil.values())
    unit = bw / mx
    rowh = 46
    out = []
    y = 6
    for yk in YUKLEM_SIRA:
        n = birincil[yk]
        out.append(f'<text x="{left}" y="{y + 16}" style="font-family:inherit;font-size:16px;fill:var(--ink)">{yk}</text>')
        x = left
        for o, col in (('A', 'var(--accent)'), ('A*', 'var(--sky)'), ('B', 'var(--gold)'), ('?', 'var(--ink-soft)')):
            c = yuklem_ozne[yk].get(o, 0)
            if c:
                w = c * unit
                out.append(f'<rect x="{x:.1f}" y="{y + 22}" width="{w:.1f}" height="18" style="fill:{col};stroke:var(--bg);stroke-width:1.5"/>')
                x += w
        out.append(f'<text x="{x + 6:.1f}" y="{y + 37}" style="font-family:inherit;font-size:16px;font-weight:700;fill:var(--ink)">{n}</text>')
        y += rowh
    ly = y + 8
    leg = [('var(--accent)', f'A · "insanların çoğu" ({ozne["A"]})'), ('var(--sky)', f'A* · zamirle, öncül genel insan ({ozne["A*"]})'),
           ('var(--gold)', f'B · belirli topluluk ({ozne["B"]})'), ('var(--ink-soft)', f'? · öncül belirsiz ({ozne["?"]})')]
    for i, (col, tx) in enumerate(leg):
        out.append(f'<rect x="{left}" y="{ly + i * 26}" width="18" height="18" style="fill:{col}"/>')
        out.append(f'<text x="{left + 28}" y="{ly + 15 + i * 26}" style="font-family:inherit;font-size:16px;fill:var(--ink)">{tx}</text>')
    H = ly + 4 * 26 + 6
    label = 'Yüklemlere göre ayet sayısı (yatay çubuk): ' + ', '.join(f'{y} {birincil[y]}' for y in YUKLEM_SIRA) + '. Çubuklar özne türüne göre bölünmüştür.'
    return svg_wrap(W, H, label, '\n'.join(out))

# --- SVG-3: oranlar (yığılmış %100 çubuklar)
def oran_svg():
    W, left = 540, 12
    bw = W - 2 * left
    out = []
    y = 4
    def panel(baslik, parcalar, y):
        o = [f'<text x="{left}" y="{y + 16}" style="font-family:inherit;font-size:16px;font-weight:700;fill:var(--ink)">{baslik}</text>']
        tot = sum(c for _, c, _ in parcalar)
        x = left
        for _, c, col in parcalar:
            if not c:
                continue
            w = bw * c / tot
            o.append(f'<rect x="{x:.1f}" y="{y + 26}" width="{w:.1f}" height="28" style="fill:{col};stroke:var(--bg);stroke-width:1.5"/>')
            if w >= 30:
                o.append(f'<text x="{x + w / 2:.1f}" y="{y + 46}" text-anchor="middle" style="font-family:inherit;font-size:16px;font-weight:700;fill:var(--bg)">{c}</text>')
            x += w
        ly = y + 62
        lx = left
        for i, (lab, c, col) in enumerate(parcalar):
            if not c:
                continue
            o.append(f'<rect x="{left}" y="{ly + i * 22 - 0}" width="14" height="14" style="fill:{col}"/>')
            o.append(f'<text x="{left + 22}" y="{ly + 13 + i * 22}" style="font-family:inherit;font-size:16px;fill:var(--ink)">{lab}: {c}</text>')
        return o, ly + 22 * sum(1 for p in parcalar if p[1]) + 12
    P1 = [('genel insan ("insanların çoğu" ve zamirle genel)', ozne['A'] + ozne['A*'], 'var(--accent)'),
          ('belirli topluluk', ozne['B'], 'var(--gold)'), ('öncül belirsiz', ozne['?'], 'var(--ink-soft)')]
    o, y = panel(f'1 · Kim için söyleniyor? ({len(S)} ayet)', P1, y); out += o
    P2 = [('Mekkeli/Arap müşrikler, inkârcılar', kimler_grup[MEK], 'var(--gold)'), ('geçmiş kavimler', kimler_grup[GK], 'var(--sky)'),
          ('Kitap ehli', kimler_grup[KE], 'var(--accent)'), ('diğer belirli grup', kimler_grup[DG], 'var(--ink-soft)')]
    o, y = panel(f'2 · Belirli topluluk dediğimiz kim? ({ozne["B"]} ayet)', P2, y); out += o
    P3 = [('Mekkî surelerde', mm_hukum['makkah'], 'var(--accent)'), ('Medenî surelerde', mm_hukum['madinah'], 'var(--gold)')]
    o, y = panel(f'3 · Mekkî / Medenî ({len(S)} ayet)', P3, y); out += o
    label = ('Oranlar: ' + f'{ozne["A"] + ozne["A*"]} ayet genel insan, {ozne["B"]} belirli topluluk, {ozne["?"]} belirsiz; '
             f'belirli topluluk içinde Mekkeli müşrikler {kimler_grup[MEK]}, geçmiş kavimler {kimler_grup[GK]}, Kitap ehli {kimler_grup[KE]}, diğer {kimler_grup[DG]}; '
             f'Mekkî {mm_hukum["makkah"]}, Medenî {mm_hukum["madinah"]}')
    return svg_wrap(W, y, label, '\n'.join(out))

# --- SVG-4: "az" ifadeleri
def az_svg():
    W, left, right = 540, 12, 44
    bw = W - left - right
    items = [(KISI, 'var(--gold)'), (DAV, 'var(--accent)'), (DEG, 'var(--sky)'), (ZAM, 'var(--ink-soft)'), (MIK, 'var(--line)')]
    mx = max(az_tur.values())
    unit = bw / mx
    out = []
    y = 6
    for lab, col in items:
        n = az_tur[lab]
        out.append(f'<text x="{left}" y="{y + 16}" style="font-family:inherit;font-size:16px;fill:var(--ink)">{lab}</text>')
        out.append(f'<rect x="{left}" y="{y + 22}" width="{n * unit:.1f}" height="18" style="fill:{col};stroke:var(--ink-soft);stroke-width:{1 if col == "var(--line)" else 0}"/>')
        out.append(f'<text x="{left + n * unit + 6:.1f}" y="{y + 37}" style="font-family:inherit;font-size:16px;font-weight:700;fill:var(--ink)">{n}</text>')
        y += 46
    label = '"Az" (قليل) kalıplarının türlere göre ayet sayısı: ' + ', '.join(f'{l} {az_tur[l]}' for l, _ in items)
    return svg_wrap(W, y + 4, label, '\n'.join(out))

for ad, fn in (('01-harita', harita_svg), ('02-yuklem', yuklem_svg), ('03-oranlar', oran_svg), ('04-az', az_svg)):
    open(os.path.join(SV, ad + '.svg'), 'w', encoding='utf-8').write(fn())

# ---------------------------------------------------------------- 9) ÖZET YAZDIR
if __name__ == '__main__':
    print('ham', n_hepsi, '| hüküm', len(S), '| kapsam dışı', len(KAPSAM_DISI))
    print('yüklem (birincil):', dict(birincil))
    print('yüklem (anılan):', {y: anilan[y] for y in YUKLEM_SIRA})
    print('özne:', dict(ozne), '| B kim:', dict(kimler_grup))
    print('Mekkî/Medenî hüküm:', mm_hukum, '| ham:', mm_ham, '| Kur\'an ayet payı Mekkî:', round(taban_ayet_pay_mekki, 3), '| sure:', dict(sure_mm), '| ayet:', dict(ayet_mm))
    print('Medenî hüküm ayetleri:', medeni_ayet)
    print('sure sayısı (hüküm):', len(sure_say), '| en çok:', [(n, CH[n]['translated_name']['name'], c) for n, c in en_cok[:10]])
    print('paylaşım:', [(p['etiket'], p['ayet'], p['kalip'], p['ozne']) for p in paylasim])
    print('kesîr geçen ayet:', len(kesir_tum), '| seçilen:', len(KES), dict(kes_nitelik))
    print('az eşleşen:', len(az_tum), '| tür:', dict(az_tur), '| az kişi:', len(az_kisi_ayet))
