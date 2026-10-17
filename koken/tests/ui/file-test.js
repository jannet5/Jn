// dist/koken.html'in file:// altında çalıştığını doğrular (gerçek motor, curl'lü ağ).
const { chromium } = require('playwright-core');
const { execFileSync } = require('child_process');
(async () => {
  const b = await chromium.launch({ executablePath: '/opt/pw-browsers/chromium', args: ['--no-sandbox'] });
  const ctx = await b.newContext({ viewport: { width: 390, height: 844 }, locale: 'tr-TR' });
  await ctx.route(/^https:\/\//, async (r) => {
    try {
      const out = execFileSync('curl', ['-sS', '-m', '20', '-A', 'Koken/1.0 (test)', '-w', '\n%{http_code}', r.request().url()], { maxBuffer: 20e6 }).toString();
      const i = out.lastIndexOf('\n');
      await r.fulfill({ status: +out.slice(i + 1), body: out.slice(0, i), headers: { 'access-control-allow-origin': '*', 'content-type': 'application/json' } });
    } catch (e) { await r.abort('failed'); }
  });
  const p = await ctx.newPage(); const errs = [];
  p.on('pageerror', (e) => errs.push(e.message)); p.on('console', (m) => m.type() === 'error' && errs.push(m.text().slice(0, 100)));
  await p.goto('file:///home/user/Jn/koken/dist/koken.html');
  await p.fill('#q', 'kahve'); await p.press('#q', 'Enter');
  await p.waitForTimeout(200);
  await p.waitForFunction(() => !document.querySelector('.skel') && document.querySelector('.result, #nfTitle, .state.err'), null, { timeout: 40000 });
  console.log('file:// sonuç:', await p.evaluate(() => document.querySelector('.result') ? 'sonuç, düğüm=' + document.querySelectorAll('.node').length : document.querySelector('.state h2').textContent));
  console.log('yazı tipi yüklendi:', await p.evaluate(() => document.fonts.check('16px Fraunces')), '| harici istek hatası:', JSON.stringify(errs));
  await p.screenshot({ path: __dirname + '/shots/file-koken.png' });
  await b.close();
})().catch((e) => { console.error('ÇÖKTÜ', e.message); process.exit(1); });
