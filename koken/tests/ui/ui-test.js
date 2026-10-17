// Arayüz testi: sahte ya da gerçek motorla insan gibi gezinir, ekran görüntüsü alır.
const { chromium } = require('playwright-core');
const path = require('path');
const { start } = require('./serve');
const SHOTS = path.join(__dirname, 'shots');
const log = (...a) => console.log(...a);
(async () => {
  const srv = await start();
  log('sunucu', srv.url, srv.mock ? 'SAHTE' : 'GERÇEK');
  const b = await chromium.launch({ executablePath: '/opt/pw-browsers/chromium', args: ['--no-sandbox'] });
  const errs = [];
  for (const [w, h] of [[390, 844], [360, 640]]) {
    const ctx = await b.newContext({ viewport: { width: w, height: h }, deviceScaleFactor: 2, locale: 'tr-TR', colorScheme: 'light' });
    const p = await ctx.newPage();
    p.on('pageerror', (e) => errs.push(w + ' pageerror ' + e.message));
    p.on('console', (m) => { if (m.type() === 'error') errs.push(w + ' console ' + m.text()); });
    const tag = `${w}x${h}`;
    await p.goto(srv.url);
    await p.screenshot({ path: `${SHOTS}/${tag}-1-bos.png` });
    // boş gönderme
    await p.click('#goBtn');
    log(tag, 'boş gönderme ipucu:', await p.textContent('#hint'));
    // kelime yaz, ara
    await p.fill('#q', 'Ahmet'); await p.click('#goBtn');
    await p.waitForSelector('.skel'); await p.screenshot({ path: `${SHOTS}/${tag}-2-yukleniyor.png` });
    await p.waitForSelector('.result'); await p.waitForTimeout(500);
    await p.screenshot({ path: `${SHOTS}/${tag}-3-sonuc.png` });
    await p.screenshot({ path: `${SHOTS}/${tag}-3b-sonuc-tam.png`, fullPage: true });
    log(tag, 'düğüm sayısı:', await p.locator('.node').count(), 'sekme:', await p.locator('.tab').count());
    // yatay taşma
    log(tag, 'yatay taşma:', await p.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth));
    // dokunma alanları
    const small = await p.evaluate(() => [...document.querySelectorAll('button,a.brand,.chip,.tab,.links a')].filter(e => e.offsetParent).map(e => { const r = e.getBoundingClientRect(); return [e.className || e.id || e.tagName, Math.round(r.width), Math.round(r.height)]; }).filter(x => x[2] < 47.5));
    log(tag, '48px altı:', JSON.stringify(small));
    // çevir
    await p.click('text=Türkçeye çevir'); await p.waitForSelector('.tr-box'); log(tag, 'çeviri:', (await p.textContent('.tr-box')).slice(0, 50));
    // dil sekmesi
    await p.click('.tab >> nth=1'); await p.screenshot({ path: `${SHOTS}/${tag}-4-sekme-ing.png` });
    // çekimli
    await p.fill('#q', 'kitaplar'); await p.press('#q', 'Enter'); await p.waitForSelector('.via');
    log(tag, 'via:', await p.textContent('.via'));
    await p.screenshot({ path: `${SHOTS}/${tag}-5-cekimli.png` });
    // olmayan kelime
    await p.fill('#q', 'qwxzv'); await p.press('#q', 'Enter'); await p.waitForSelector('.state');
    await p.waitForFunction(() => document.querySelector('#nfTitle')); await p.screenshot({ path: `${SHOTS}/${tag}-6-yok.png` });
    // hata
    await p.fill('#q', 'hata'); await p.press('#q', 'Enter'); await p.waitForSelector('.state.err');
    await p.screenshot({ path: `${SHOTS}/${tag}-7-hata.png` });
    // çevrimdışı
    await ctx.setOffline(true); await p.waitForTimeout(200);
    await p.click('text=Tekrar dene'); await p.waitForSelector('text=Çevrimdışısınız');
    await p.screenshot({ path: `${SHOTS}/${tag}-8-cevrimdisi.png` });
    await ctx.setOffline(false);
    // geri/ileri
    await p.goBack(); await p.waitForTimeout(300); log(tag, 'geri → input:', await p.inputValue('#q'));
    await p.goForward(); await p.waitForTimeout(300); log(tag, 'ileri → input:', await p.inputValue('#q'));
    // çok uzun kelime + Türkçe karakter
    await p.fill('#q', 'ğüşiöçİıı'.repeat(10)); await p.press('#q', 'Enter'); await p.waitForSelector('#nfTitle');
    log(tag, 'uzun kelimede yatay taşma:', await p.evaluate(() => document.documentElement.scrollWidth > document.documentElement.clientWidth));
    await p.screenshot({ path: `${SHOTS}/${tag}-9-uzun.png` });
    // koyu tema (elle)
    await p.fill('#q', 'kitap'); await p.press('#q', 'Enter'); await p.waitForSelector('.result');
    await p.click('#themeBtn'); await p.waitForTimeout(300);
    await p.screenshot({ path: `${SHOTS}/${tag}-10-koyu.png` });
    log(tag, 'son aramalar:', await p.locator('#recentList .chip').allTextContents());
    await ctx.close();
  }
  log('HATALAR:', JSON.stringify(errs));
  await b.close(); srv.server.close();
})().catch((e) => { console.error('TEST ÇÖKTÜ', e); process.exit(1); });
