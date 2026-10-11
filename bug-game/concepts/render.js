// SVG kartlarını Chromium ile PNG'ye çevirir.
const { chromium } = require('playwright');
const fs = require('fs'), path = require('path');
const { svgs } = require('./generate');
(async () => {
  fs.mkdirSync(path.join(__dirname, 'png'), { recursive: true });
  const b = await chromium.launch();
  const p = await b.newPage({ viewport: { width: 1280, height: 800 } });
  for (let i = 0; i < svgs.length; i++) {
    await p.setContent(`<html><body style="margin:0">${svgs[i]}</body></html>`);
    await p.screenshot({ path: path.join(__dirname, 'png', `konsept-${String(i + 1).padStart(2, '0')}.png`) });
  }
  await b.close();
  console.log('png ok');
})();
