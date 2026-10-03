const { chromium } = require('/opt/node-tools/node_modules/playwright');
const fs = require('fs');
(async () => {
  const b = await chromium.launch({ executablePath: undefined });
  const ctx = await b.newContext({ acceptDownloads: true });
  const page = await ctx.newPage();
  await page.goto('http://127.0.0.1:8765/p/TESTCODE1/');
  const bm = fs.readFileSync('../../capture/bookmarklet.txt','utf8').trim();
  const code = decodeURIComponent(bm.slice('javascript:'.length));
  const got = [];
  page.on('download', async d => { const p = './indirilen/' + d.suggestedFilename(); await d.saveAs(p); got.push(p); });
  await page.evaluate(code);
  await page.waitForTimeout(2500);
  console.log(JSON.stringify(got));
  await b.close();
})();
