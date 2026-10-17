// Köken motoru testleri: CANLI en.wiktionary'ye karşı (node --test)
// Çalıştırma: cd koken/tests && node --test
'use strict';
const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { execFile } = require('node:child_process');

const Etym = require('../../web/etym.js');

// Çıktı klasörü (arayüz işçisi örnek veri olarak kullanır)
const OUT = path.join(__dirname, 'out');
fs.mkdirSync(OUT, { recursive: true });

// Sandbox ağı proxy'li ve Node fetch proxy'yi kullanmaz; curl ile gerçek ağa çıkan http
let lastCall = 0;
function curlHttp(url, o) {
  o = o || {};
  return new Promise((resolve, reject) => {
    const h = o.headers || {};
    const args = ['-s', '-i', '-m', String(Math.ceil((o.timeoutMs || 12000) / 1000)), '-A',
      h['Api-User-Agent'] ? h['Api-User-Agent'] + ' curl' : 'KokenTest/1.0 (jakeworkx@proton.me)'];
    for (const k of Object.keys(h)) args.push('-H', k + ': ' + h[k]);
    args.push('--compressed', url);
    execFile('curl', args, { maxBuffer: 30 * 1024 * 1024, encoding: 'buffer' }, (err, stdout) => {
      if (err && !stdout.length) return reject(err);
      const raw = stdout.toString('utf8');
      // Proxy "200 Connection Established" başlığını ve asıl başlıkları ayır
      const blocks = raw.split(/\r?\n\r?\n/);
      let i = 0;
      while (i < blocks.length - 1 && /^HTTP\/[\d.]+ 200 Connection Established/i.test(blocks[i])) i++;
      const head = blocks[i] || '';
      const body = blocks.slice(i + 1).join('\n\n');
      const status = Number((head.match(/^HTTP\/[\d.]+\s+(\d+)/) || [])[1]) || 0;
      const headers = {};
      head.split(/\r?\n/).slice(1).forEach((l) => { const m = l.match(/^([^:]+):\s*(.*)$/); if (m) headers[m[1].toLowerCase()] = m[2]; });
      resolve({ status, text: body, headers });
    });
  });
}

// Test için: istekleri sırala, aralarında bekle, 429'da Retry-After kadar bekleyip tekrar dene
async function politeHttp(url, o) {
  for (let attempt = 0; attempt < 6; attempt++) {
    const gap = 3000 - (Date.now() - lastCall);
    if (gap > 0) await new Promise((r) => setTimeout(r, gap));
    lastCall = Date.now();
    const r = await curlHttp(url, o);
    if (r.status === 429 || r.status === 503 || /too many requests/i.test(r.text.slice(0, 200))) {
      const ra = Math.min(Number(r.headers['retry-after']) || 20, 60);
      await new Promise((res) => setTimeout(res, ra * 1000 + 500));
      continue;
    }
    return r;
  }
  return curlHttp(url, o);
}

// Sonucu out/ klasörüne kaydeder
function save(name, res) {
  fs.writeFileSync(path.join(OUT, name + '.json'), JSON.stringify(res, null, 2), 'utf8');
}

// Her sonuçta sözleşme alanlarını doğrular
function checkShape(r) {
  for (const k of ['query', 'resolved', 'found', 'via', 'suggestions', 'entries', 'sources', 'fetchedAt']) assert.ok(k in r, 'alan eksik: ' + k);
  assert.ok(Array.isArray(r.entries) && Array.isArray(r.sources) && Array.isArray(r.suggestions));
  assert.ok(!isNaN(Date.parse(r.fetchedAt)));
  for (const s of r.sources) assert.ok(typeof s.name === 'string' && typeof s.url === 'string' && typeof s.ok === 'boolean' && typeof s.ms === 'number');
  for (const e of r.entries) {
    assert.ok(e.language && e.languageTr && Array.isArray(e.pos) && Array.isArray(e.etymologies) && /^https?:/.test(e.url));
    for (const y of e.etymologies) {
      assert.ok(typeof y.text === 'string' && y.text.length > 0);
      assert.ok(Array.isArray(y.chain) && y.chain.length >= 1 && Array.isArray(y.parts) && Array.isArray(y.cognates));
      for (const c of y.chain) {
        assert.ok(c.language && typeof c.term === 'string' && 'translit' in c && 'gloss' in c);
        assert.ok(['inherited', 'borrowed', 'derived', 'learned', 'calque', 'unknown'].includes(c.relation), 'relation: ' + c.relation);
      }
      for (const p of y.parts) assert.ok(['prefix', 'suffix', 'root', 'compound', 'word'].includes(p.kind));
    }
  }
}

// Bir kelimeyi canlı ara, doğrula, kaydet
async function run(word, file) {
  const r = await Etym.lookup(word, { http: politeHttp, timeoutMs: 20000 });
  save(file || word, r);
  checkShape(r);
  return r;
}

// İlk (tercihli) girdinin ilk etimolojisi
function first(r, lang) {
  const e = lang ? r.entries.find((x) => x.language === lang) : r.entries[0];
  assert.ok(e, 'girdi yok: ' + lang);
  return e.etymologies[0];
}

test('Ahmet (büyük/küçük harf: ahmet de bulunmalı)', async () => {
  const r = await run('Ahmet');
  assert.equal(r.found, true);
  const t = first(r, 'Turkish');
  assert.ok(t.chain.length >= 2, 'zincir kısa: ' + JSON.stringify(t.chain.map((c) => c.term)));
  assert.ok(t.chain.some((c) => c.language === 'Arabic'));
  const r2 = await run('ahmet', 'ahmet-kucuk');
  assert.equal(r2.found, true);
  assert.equal(r2.resolved, 'Ahmet');
});

test('kitap', async () => {
  const r = await run('kitap');
  assert.equal(r.found, true);
  const t = first(r, 'Turkish');
  assert.ok(t.chain.length >= 3);
  assert.ok(t.chain.some((c) => c.language === 'Arabic'));
});

test('kitaplar -> kitap (biçim sayfası)', async () => {
  const r = await run('kitaplar');
  assert.equal(r.found, true);
  assert.ok(r.via && /kitaplar.*kitap/.test(r.via), 'via: ' + r.via);
  assert.equal(r.resolved, 'kitap');
  assert.ok(first(r, 'Turkish').chain.length >= 2);
});

test('elma', async () => {
  const r = await run('elma');
  assert.equal(r.found, true);
  assert.ok(first(r, 'Turkish').text.length > 5);
});

test('çay', async () => {
  const r = await run('çay');
  assert.equal(r.found, true);
  assert.ok(r.entries.length >= 1);
});

test('pijama (Fransızca/İngilizce/Hintçe/Farsça zinciri)', async () => {
  const r = await run('pijama');
  assert.equal(r.found, true);
  const t = first(r, 'Turkish');
  assert.ok(t.chain.length >= 4, JSON.stringify(t.chain.map((c) => c.term)));
  assert.ok(t.chain.some((c) => c.language === 'French'));
});

test('telefon', async () => {
  const r = await run('telefon');
  assert.equal(r.found, true);
  assert.ok(first(r, 'Turkish').chain.length >= 2);
});

test('algorithm (İngilizce, Arapça kökenli)', async () => {
  const r = await run('algorithm');
  assert.equal(r.found, true);
  const t = first(r, 'English');
  assert.ok(t.chain.length >= 3);
  assert.ok(t.chain.some((c) => c.language === 'Arabic'));
});

test('window', async () => {
  const r = await run('window');
  assert.equal(r.found, true);
  assert.ok(first(r, 'English').chain.length >= 2);
});

test('café', async () => {
  const r = await run('café', 'cafe');
  assert.equal(r.found, true);
  assert.ok(r.entries.some((e) => e.language === 'English'));
});

test('bilgisayar (parçalar: bilgi + say- + -ar)', async () => {
  const r = await run('bilgisayar');
  assert.equal(r.found, true);
  const t = first(r, 'Turkish');
  assert.ok(t.parts.length >= 2, 'parts: ' + JSON.stringify(t.parts));
});

test('okul', async () => {
  const r = await run('okul');
  assert.equal(r.found, true);
  assert.ok(first(r, 'Turkish').chain.length >= 2);
});

test('kahve', async () => {
  const r = await run('kahve');
  assert.equal(r.found, true);
  assert.ok(first(r, 'Turkish').chain.some((c) => c.language === 'Arabic'));
});

test('güneş', async () => {
  const r = await run('güneş');
  assert.equal(r.found, true);
  assert.ok(first(r, 'Turkish').text.length > 5);
});

test('sevgi (sev- + -gi parçaları)', async () => {
  const r = await run('sevgi');
  assert.equal(r.found, true);
  assert.ok(first(r, 'Turkish').parts.length >= 1, 'parts boş');
});

test('olmayan kelime: asdfqwerzx', async () => {
  const r = await run('asdfqwerzx');
  assert.equal(r.found, false);
  assert.deepEqual(r.entries, []);
  assert.ok(Array.isArray(r.suggestions));
});

// ---- Ağsız (sahte http) birim testleri ----

test('sahte http: ağ hatasında çökmez, sources hata yazar', async () => {
  const bad = async () => { throw new Error('çevrimdışı'); };
  const r = await Etym.lookup('kitap', { http: bad });
  assert.equal(r.found, false);
  assert.ok(r.sources.length >= 1 && r.sources.every((s) => s.ok === false));
});

test('sahte http: Android köprüsü kaynakları (Nişanyan/TDK/Etymonline) ayrıştırılır, hata sessizdir', async () => {
  const wikt = JSON.stringify({ parse: { title: 'kahve', text: '<div class="mw-parser-output"><div class="mw-heading mw-heading2"><h2 id="Turkish">Turkish</h2></div><div class="mw-heading mw-heading3"><h3 id="Etymology">Etymology</h3></div><p>Borrowed from <span class="etyl"><a class="extiw" title="w:Arabic language" href="https://en.wikipedia.org/wiki/Arabic_language">Arabic</a></span> <i class="Arab mention" lang="ar"><a href="/wiki/%D9%82%D9%87%D9%88%D8%A9#Arabic">قهوة</a></i>.</p><div class="mw-heading mw-heading3"><h3 id="Noun">Noun</h3></div><ol><li>coffee</li></ol></div>' } });
  const http = async (url) => {
    if (url.includes('en.wiktionary.org')) return { status: 200, text: wikt };
    if (url.includes('tr.wiktionary.org')) return { status: 429, text: '' };
    if (url.includes('nisanyansozluk')) return { status: 200, text: JSON.stringify({ words: [{ name: 'kahve', etymologies: [{ languages: [{ name: 'Arapça' }], relation: { text: '<' }, originalText: 'qahwa' }] }] }) };
    if (url.includes('sozluk.gov.tr')) return { status: 200, text: JSON.stringify([{ madde: 'kahve', lisan: 'Arapça', anlamlarListe: [{ anlam: 'Kahve ağacının çekirdeği' }] }]) };
    return { status: 403, text: 'forbidden' };
  };
  const r = await Etym.lookup('kahve', { http, extended: true });
  assert.equal(r.found, true);
  const names = r.sources.map((s) => s.name + ':' + s.ok);
  assert.ok(names.includes('Nişanyan Sözlük:true') && names.includes('TDK Güncel Sözlük:true') && names.includes('Etymonline:false'), names.join('|'));
  assert.ok(r.entries[0].etymologies.some((e) => e.source === 'tdk'));
});

test('translate: parça bölme <=450 karakter ve sahte uçla çeviri', async () => {
  const long = ('Bu bir deneme cümlesidir. ').repeat(60);
  const parts = Etym._internal.chunkText(long, 450);
  assert.ok(parts.length >= 3 && parts.every((p) => p.length <= 450));
  const http = async (url) => url.includes('translate.googleapis.com')
    ? { status: 200, text: JSON.stringify([[['merhaba', 'hello', null, null, 1]], null, 'en']) }
    : { status: 500, text: '' };
  assert.equal(await Etym.translate('hello', 'tr', { http }), 'merhaba');
  const http2 = async (url) => url.includes('mymemory')
    ? { status: 200, text: JSON.stringify({ responseStatus: 200, responseData: { translatedText: 'selam' } }) }
    : { status: 500, text: '' };
  assert.equal(await Etym.translate('hi', 'tr', { http: http2 }), 'selam');
  await assert.rejects(Etym.translate('x', 'tr', { http: async () => ({ status: 500, text: '' }) }));
});

test('makeHttp: KokenNative köprüsü base64 UTF-8 gövdeyi çözer', async () => {
  globalThis.window = globalThis;
  globalThis.KokenNative = {
    get(id, url, headers, tmo) {
      setTimeout(() => globalThis.__kokenNativeDone(id, 200, Buffer.from('çay ☕', 'utf8').toString('base64')), 5);
    }
  };
  const http = Etym.makeHttp();
  const r = await http('https://x.test/', { timeoutMs: 1000 });
  delete globalThis.KokenNative; delete globalThis.window;
  assert.equal(r.status, 200);
  assert.equal(r.text, 'çay ☕');
});
