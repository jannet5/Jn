// SVG simgelerinden PNG üretir (chromium ile ekran görüntüsü; harici araç gerekmez).
const { chromium } = require('playwright-core');
const fs = require('fs'); const path = require('path');
const ICONS = path.resolve(__dirname, '../../web/icons');
(async () => {
  const b = await chromium.launch({ executablePath: '/opt/pw-browsers/chromium', args: ['--no-sandbox'] });
  const jobs = [['icon.svg', 'icon-192.png', 192], ['icon.svg', 'icon-512.png', 512], ['icon-maskable.svg', 'icon-maskable-512.png', 512]];
  for (const [src, out, size] of jobs) {
    const p = await b.newPage({ viewport: { width: size, height: size } });
    const svg = fs.readFileSync(path.join(ICONS, src), 'utf8');
    await p.setContent(`<body style="margin:0;background:transparent">${svg.replace('<svg ', `<svg width="${size}" height="${size}" `)}</body>`);
    await p.screenshot({ path: path.join(ICONS, out), omitBackground: true });
    await p.close(); console.log('yazıldı', out, size);
  }
  await b.close();
})();
