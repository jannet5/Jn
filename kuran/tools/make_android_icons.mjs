// icon.svg -> android/app/src/main/res/mipmap-*/ic_launcher.png
import { createRequire } from 'module';
const require = createRequire('/opt/node-tools/node_modules/');
const { chromium } = require('playwright-core');
import fs from 'fs';
const [svgPath, resDir] = process.argv.slice(2);
const svg = fs.readFileSync(svgPath, 'utf8');
const sizes = { mdpi: 48, hdpi: 72, xhdpi: 96, xxhdpi: 144, xxxhdpi: 192 };
const b = await chromium.launch({ executablePath: process.env.CHROME || '/opt/pw-browsers/chromium-1194/chrome-linux/chrome' });
for (const [d, s] of Object.entries(sizes)) {
  fs.mkdirSync(`${resDir}/mipmap-${d}`, { recursive: true });
  const p = await b.newPage({ viewport: { width: s, height: s } });
  await p.setContent('<body style="margin:0">' + svg.replace('<svg ', `<svg width="${s}" height="${s}" `) + '</body>');
  await p.screenshot({ path: `${resDir}/mipmap-${d}/ic_launcher.png` });
}
await b.close();
