#!/usr/bin/env node
// Rakip araştırması (harita.md → Aşama 1): App Store + Google Play'de bir arama
// terimi için ilk N rakibi bulur, meta verilerini ve düşük puanlı yorumlarını toplar,
// şikâyet kelime frekansı çıkarır ve LLM'e verilecek hazır bir istem dosyası yazar.
//
// Kullanım:
//   npm install
//   node rakip.mjs --terim "habit tracker" --ulke tr --dil tr --adet 5 --yorum 100
//
// Çıktı: cikti/<terim-slug>-<ulke>/{rakipler.csv, yorumlar.csv, rapor.md, llm-istemi.md}
// Hesap, API anahtarı gerekmez; mağazaların herkese açık uç noktalarını kullanır.

import fs from 'node:fs';
import path from 'node:path';
import { createRequire } from 'node:module';
import gplay from 'google-play-scraper';

const require = createRequire(import.meta.url);
const store = require('app-store-scraper');

function argv(name, def) {
  const i = process.argv.indexOf(`--${name}`);
  return i > -1 && process.argv[i + 1] ? process.argv[i + 1] : def;
}

const TERIM = argv('terim', null);
if (!TERIM) {
  console.error('Hata: --terim zorunlu. Örnek: node rakip.mjs --terim "habit tracker" --ulke tr');
  process.exit(2);
}
const ULKE = argv('ulke', 'us');
const DIL = argv('dil', 'en');
const ADET = Number(argv('adet', 5));
const YORUM = Number(argv('yorum', 100));
const OUT = argv('cikti', path.join('cikti', `${TERIM.toLowerCase().replace(/[^a-z0-9ğüşöçı]+/gi, '-')}-${ULKE}`));

const STOP = new Set((
  'the a an and or but to of in on for with is it this that i my me you your app apps was are be ' +
  'have has had not no so very just can cant can\'t dont don\'t do does did get got its it\'s at as ' +
  'from by if they them there what when all would will one even only more also out up use using ' +
  've ile bir bu da de ve çok ama için gibi daha ne mi mı en ben sen o uygulama uygulamayı uygulamanın ' +
  'var yok olan olarak diye şey her hiç kadar sonra önce bile değil'
).split(/\s+/));

const csv = (rows) => rows.map((r) => r.map((v) => {
  const s = v === undefined || v === null ? '' : String(v);
  return /[",\n]/.test(s) ? `"${s.replace(/"/g, '""')}"` : s;
}).join(',')).join('\n') + '\n';

async function guvenli(etiket, fn) {
  try { return await fn(); } catch (e) {
    console.warn(`[uyarı] ${etiket}: ${e.message}`);
    return null;
  }
}

async function main() {
  fs.mkdirSync(OUT, { recursive: true });
  const rakipler = [];
  const yorumlar = [];

  const gList = (await guvenli('play arama', () => gplay.search({ term: TERIM, num: ADET, country: ULKE, lang: DIL }))) || [];
  for (const a of gList) {
    const d = (await guvenli(`play detay ${a.appId}`, () => gplay.app({ appId: a.appId, country: ULKE, lang: DIL }))) || a;
    rakipler.push(['google_play', d.appId, d.title, d.developer, d.score?.toFixed?.(2) ?? d.score, d.ratings, d.installs,
      d.free ? 'ücretsiz' : d.priceText, d.offersIAP ? 'evet' : 'hayır', d.updated ? new Date(d.updated).toISOString().slice(0, 10) : '', d.url]);
    const r = await guvenli(`play yorum ${a.appId}`, () => gplay.reviews({ appId: a.appId, country: ULKE, lang: DIL, sort: gplay.sort.NEWEST, num: YORUM }));
    for (const y of r?.data || []) yorumlar.push(['google_play', d.appId, d.title, y.score, y.date ? new Date(y.date).toISOString().slice(0, 10) : '', y.text]);
  }

  const iList = (await guvenli('app store arama', () => store.search({ term: TERIM, num: ADET, country: ULKE, lang: DIL }))) || [];
  for (const a of iList) {
    rakipler.push(['app_store', a.appId, a.title, a.developer, a.score?.toFixed?.(2) ?? a.score, a.reviews, '',
      a.free ? 'ücretsiz' : a.price, '', a.updated ? String(a.updated).slice(0, 10) : '', a.url]);
    // App Store RSS yorumları sayfa başına 50; YORUM kadar sayfa topla (en fazla 10 sayfa).
    const sayfa = Math.min(10, Math.max(1, Math.ceil(YORUM / 50)));
    for (let p = 1; p <= sayfa; p++) {
      const r = await guvenli(`app store yorum ${a.id} s${p}`, () => store.reviews({ id: a.id, country: ULKE, page: p, sort: store.sort.RECENT }));
      if (!r || r.length === 0) break;
      for (const y of r) yorumlar.push(['app_store', a.appId, a.title, y.score, y.updated ? String(y.updated).slice(0, 10) : '', `${y.title ?? ''}. ${y.text ?? ''}`]);
    }
  }

  fs.writeFileSync(path.join(OUT, 'rakipler.csv'), csv([
    ['magaza', 'app_id', 'ad', 'gelistirici', 'puan', 'puan_sayisi', 'indirme', 'fiyat', 'uygulama_ici_satin_alma', 'guncelleme', 'url'],
    ...rakipler,
  ]));
  fs.writeFileSync(path.join(OUT, 'yorumlar.csv'), csv([['magaza', 'app_id', 'ad', 'yildiz', 'tarih', 'metin'], ...yorumlar]));

  // Düşük puanlı (1-2 yıldız) yorumlarda kelime frekansı = kaba şikâyet haritası.
  const dusuk = yorumlar.filter((y) => Number(y[3]) <= 2);
  const frek = new Map();
  for (const y of dusuk) {
    const kel = String(y[5]).toLocaleLowerCase(DIL).match(/[\p{L}]{3,}/gu) || [];
    for (const k of new Set(kel)) if (!STOP.has(k)) frek.set(k, (frek.get(k) || 0) + 1);
  }
  const ust = [...frek.entries()].sort((a, b) => b[1] - a[1]).slice(0, 30);

  const tablo = rakipler.map((r) => `| ${r[0]} | ${r[2]} | ${r[3]} | ${r[4] ?? ''} | ${r[5] ?? ''} | ${r[6] ?? ''} | ${r[7] ?? ''} | ${r[9] ?? ''} |`).join('\n');
  const rapor = `# Rakip raporu: "${TERIM}" (${ULKE}/${DIL})

Üretim zamanı: ${new Date().toISOString()}
Rakip sayısı: ${rakipler.length} · Toplanan yorum: ${yorumlar.length} · 1-2 yıldızlı yorum: ${dusuk.length}

## Rakipler

| Mağaza | Ad | Geliştirici | Puan | Puan sayısı | İndirme | Fiyat | Güncelleme |
|---|---|---|---|---|---|---|---|
${tablo}

## 1-2 yıldızlı yorumlarda en sık kelimeler (kaba şikâyet sinyali)

${ust.map(([k, n]) => `- ${k}: ${n}`).join('\n') || '- (düşük puanlı yorum bulunamadı)'}

## Sonraki adım

\`llm-istemi.md\` dosyasını Claude/ChatGPT/Gemini'ye verin; şikâyetleri temalara ayırıp
"rakiplerin çözmediği ihtiyaç → bizim MVP özelliği" tablosu üretmesini isteyin.
`;
  fs.writeFileSync(path.join(OUT, 'rapor.md'), rapor);

  const ornek = dusuk.slice(0, 120).map((y) => `- [${y[0]} · ${y[2]} · ${y[3]}★] ${String(y[5]).replace(/\s+/g, ' ').slice(0, 400)}`).join('\n');
  fs.writeFileSync(path.join(OUT, 'llm-istemi.md'), `Sen bir mobil ürün araştırmacısısın. Aşağıda "${TERIM}" kategorisindeki rakip uygulamaların
1-2 yıldızlı gerçek kullanıcı yorumları var. Görevler:
1. Şikâyetleri 5-10 temaya ayır; her tema için yorum sayısı ve 2 kısa alıntı ver.
2. Her tema için "rakiplerin çözmediği ihtiyaç" ve "bizim uygulamada karşılığı olacak özellik" yaz.
3. Özellikleri etki/çaba matrisine göre sırala; ilk sürüm (v1) için en fazla 5 özellik seç.
4. Mağaza metni (başlık ≤30 karakter, alt başlık ≤30 karakter) için 3 öneri ver.
Uydurma veri ekleme; yalnız aşağıdaki yorumlara dayan.

Rakipler (CSV özeti):
${rakipler.map((r) => `- ${r[0]}: ${r[2]} (${r[4] ?? '?'}★, ${r[5] ?? '?'} puan)`).join('\n')}

Yorumlar:
${ornek || '(düşük puanlı yorum yok)'}
`);

  console.log(`Tamam → ${OUT}`);
  console.log(`rakip=${rakipler.length} yorum=${yorumlar.length} dusuk=${dusuk.length}`);
  if (rakipler.length === 0) process.exit(1);
}

main();
