// icon.svg -> icon-192.png ve icon-512.png (Playwright/Chromium ile)
import { createRequire } from 'module';
const require = createRequire('/opt/node-tools/node_modules/');
const { chromium } = require('playwright-core');
import fs from 'fs';
const dir = process.argv[2];
const svg = fs.readFileSync(dir + '/icon.svg', 'utf8');
const b = await chromium.launch({ executablePath: process.env.CHROME || '/opt/pw-browsers/chromium-1194/chrome-linux/chrome' });
for (const s of [192, 512]) {
  const p = await b.newPage({ viewport: { width: s, height: s } });
  await p.setContent('<body style="margin:0">' + svg.replace('<svg ', `<svg width="${s}" height="${s}" `) + '</body>');
  await p.screenshot({ path: `${dir}/icon-${s}.png` });
}
await b.close();
