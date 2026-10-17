// Gerçek motor + gerçek ağ ile insan gibi kullanım testi.
const { chromium } = require('playwright-core');
const path = require('path');
const { start } = require('./serve');
const SHOTS = path.join(__dirname, 'shots');
const log = (...a) => console.log(...a);
(async () => {
  const srv = await start({ mock: false });
  log('sunucu', srv.url, srv.mock ? 'SAHTE' : 'GERÇEK');
  const proxy = undefined;
  const b = await chromium.launch({ executablePath: '/opt/pw-browsers/chromium', args: ['--no-sandbox'], proxy });
  const errs = [];
  // Chromium proxy'yi yerel sayfaya da uyguladığı için dış istekleri curl (proxy'li) ile karşılıyoruz.
  const { execFileSync } = require('child_process');
  const route = async (r) => {
    const req = r.request();
    if (req.method() === 'OPTIONS') return r.fulfill({ status: 204, headers: { 'access-control-allow-origin': '*', 'access-control-allow-headers': '*' } });
    try {
      const out = execFileSync('curl', ['-sS', '-m', '20', '-A', 'Koken/1.0 (test)', '-w', '\n%{http_code}', req.url()], { maxBuffer: 20e6 }).toString();
      const i = out.lastIndexOf('\n'); const status = +out.slice(i + 1); const body = out.slice(0, i);
      await r.fulfill({ status, body, headers: { 'access-control-allow-origin': '*', 'content-type': 'application/json' } });
    } catch (e) { await r.abort('failed'); }
  };
  for (const [w, h] of [[390, 844], [360, 640]]) {
    const ctx = await b.newContext({ viewport: { width: w, height: h }, deviceScaleFactor: 2, locale: 'tr-TR', ignoreHTTPSErrors: true });
    await ctx.route(/^https:\/\/(?!appassets)/, route);
    const p = await ctx.newPage();
    p.on('pageerror', (e) => errs.push(w + ' pageerror ' + e.message));
    p.on('console', (m) => { if (m.type() === 'error') errs.push(w + ' console ' + m.text().slice(0, 120)); });
    const tag = `gercek-${w}x${h}`;
    await p.goto(srv.url);
    // bekle: sonuç, bulunamadı ya da hata
    const settle = async (label) => {
      await p.waitForTimeout(150); await p.waitForFunction(() => !document.querySelector('.skel') && document.querySelector('.result, #nfTitle, .state.err'), null, { timeout: 40000 });
      const kind = await p.evaluate(() => document.querySelector('.result') ? 'sonuç' : document.querySelector('#nfTitle') ? 'bulunamadı' : 'hata:' + document.querySelector('.state.err h2').textContent);
      log(tag, label, '→', kind, '| düğüm:', await p.locator('.node').count(), '| sekme:', await p.locator('.tab').count(), '| kaynak:', (await p.locator('.src').allTextContents()).join(' / '));
      log(tag, '  yatay taşma:', await p.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth));
    };
    const search = async (word) => { await p.fill('#q', word); await p.press('#q', 'Enter'); };
    await search('Ahmet'); await p.waitForSelector('.skel', { timeout: 3000 }).catch(() => {}); await p.screenshot({ path: `${SHOTS}/${tag}-yukleniyor.png` });
    await settle('Ahmet'); await p.waitForTimeout(600); await p.screenshot({ path: `${SHOTS}/${tag}-ahmet.png` }); await p.screenshot({ path: `${SHOTS}/${tag}-ahmet-tam.png`, fullPage: true });
    await search('kitaplar'); await settle('kitaplar'); log(tag, '  via:', await p.locator('.via').textContent().catch(() => '-')); await p.screenshot({ path: `${SHOTS}/${tag}-kitaplar.png`, fullPage: true });
    await search('qwxzvbn'); await settle('olmayan'); await p.screenshot({ path: `${SHOTS}/${tag}-olmayan.png` });
    await search('ğüşiöçİıı'.repeat(8)); await settle('uzun'); await p.screenshot({ path: `${SHOTS}/${tag}-uzun.png` });
    await search('şeker'); await settle('şeker'); await p.screenshot({ path: `${SHOTS}/${tag}-seker.png`, fullPage: true });
    if (await p.locator('text=Türkçeye çevir').count()) {
      await p.locator('text=Türkçeye çevir').first().click();
      await p.waitForSelector('.tr-box', { timeout: 20000 }).catch(() => {});
      log(tag, '  çeviri:', ((await p.locator('.tr-box').first().textContent().catch(() => 'YOK')) || '').slice(0, 90));
    }
    await p.goBack(); await p.waitForTimeout(400); log(tag, '  geri →', await p.inputValue('#q'));
    await p.goForward(); await p.waitForTimeout(400); log(tag, '  ileri →', await p.inputValue('#q'));
    await ctx.setOffline(true); await search('kahve'); await p.waitForSelector('text=Çevrimdışısınız'); await p.screenshot({ path: `${SHOTS}/${tag}-cevrimdisi.png` });
    await ctx.setOffline(false); await p.click('text=Tekrar dene'); await settle('kahve (tekrar dene)');
    await p.click('#themeBtn'); await p.waitForTimeout(300); await p.screenshot({ path: `${SHOTS}/${tag}-koyu.png` });
    await ctx.close();
  }
  log('HATALAR:', JSON.stringify(errs));
  await b.close(); srv.server.close();
})().catch((e) => { console.error('TEST ÇÖKTÜ', e.message); process.exit(1); });
