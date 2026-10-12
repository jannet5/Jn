// Kullanım: node qa/ss.mjs [önek]  — dist/'i sunar, 390×844 açık+koyu, kullanıcı akışlarını tıklayarak SS alır.
import { createServer } from 'node:http';
import { readFile } from 'node:fs/promises';
import { existsSync } from 'node:fs';
import { join, extname, dirname } from 'node:path';
import { fileURLToPath } from 'node:url';
import { createRequire } from 'node:module';

const require = createRequire(import.meta.url);
let pw;
try { pw = require('playwright'); } catch { pw = require(join(process.env.NODE_PATH || '/root/.npm-global/lib/node_modules', 'playwright')); }
const { chromium } = pw;

const kok = join(dirname(fileURLToPath(import.meta.url)), '..');
const dist = join(kok, 'dist');
const cikti = join(kok, '..', 'ss');
const onek = process.argv[2] || 'tur1';
const tip = { '.html': 'text/html', '.js': 'text/javascript', '.css': 'text/css', '.png': 'image/png', '.ttf': 'font/ttf', '.ico': 'image/x-icon', '.json': 'application/json' };

const sunucu = createServer(async (req, res) => {
  let p = decodeURIComponent(req.url.split('?')[0]);
  if (p === '/') p = '/index.html';
  let f = join(dist, p);
  if (!existsSync(f) && existsSync(f + '.html')) f += '.html';
  if (!existsSync(f)) f = join(dist, 'index.html');
  res.writeHead(200, { 'content-type': tip[extname(f)] || 'application/octet-stream' });
  res.end(await readFile(f));
}).listen(0);
const url = `http://127.0.0.1:${sunucu.address().port}`;

const tarayici = await chromium.launch({ executablePath: '/opt/pw-browsers/chromium-1194/chrome-linux/chrome' });
const hatalar = [];
const bekle = (ms) => new Promise((r) => setTimeout(r, ms));

for (const sema of ['light', 'dark']) {
  const ctx = await tarayici.newContext({ viewport: { width: 390, height: 844 }, deviceScaleFactor: 2, colorScheme: sema, isMobile: true, hasTouch: true });
  const s = await ctx.newPage();
  s.on('console', (m) => m.type() === 'error' && hatalar.push(`${sema} ${s.url()}: ${m.text()}`));
  s.on('pageerror', (e) => hatalar.push(`${sema} pageerror: ${e.message}`));
  const ss = async (ad, tam = false) => { await bekle(700); await s.screenshot({ path: join(cikti, `${onek}-${ad}-${sema}.png`), fullPage: tam }); };
  const tikla = async (metin) => { await s.getByText(metin, { exact: true }).first().click(); await bekle(250); };

  // Ana ekran
  await s.goto(url + '/'); await s.waitForLoadState('networkidle'); await ss('01-ana');
  await ss('01-ana-tam', true);

  // Kafe: boş → 3 damga → 10 damga kutlama → sıfırla sheet
  await s.getByText('Dijital sadakat kartı').click(); await s.waitForURL('**/kafe'); await ss('02-kafe-bos');
  for (let i = 0; i < 3; i++) await tikla('Damga ekle');
  await ss('03-kafe-3damga');
  for (let i = 0; i < 7; i++) await tikla('Damga ekle');
  await ss('04-kafe-kutlama');
  await tikla('Kartı sıfırla'); await ss('05-kafe-onay-sheet');
  await tikla('Sıfırla');

  // Berber: saat seçilmemiş → seçili → onay sheet → onaylandı
  await s.goto(url + '/berber'); await s.waitForLoadState('networkidle'); await ss('06-berber-bos');
  await tikla('16:00'); await ss('07-berber-secili');
  await s.getByText(/için randevu al$/).click(); await ss('08-berber-onay-sheet');
  await tikla('Onayla'); await ss('09-berber-onaylandi');
  await ss('09-berber-onaylandi-tam', true);

  // Restoran: menü → ürün ekle → sepet sheet → sipariş
  await s.goto(url + '/restoran'); await s.waitForLoadState('networkidle'); await ss('10-restoran');
  await s.getByLabel('Izgara köfte sepete ekle').click(); await bekle(200);
  await s.getByLabel('Izgara köfte artır').click(); await bekle(200);
  await tikla('Tatlı'); await s.getByLabel('Künefe sepete ekle').click();
  await ss('11-restoran-sepetli');
  await s.getByText(/^Sepeti gör/).click(); await ss('12-restoran-sepet-sheet');
  await tikla('WhatsApp’tan sipariş ver'); await ss('13-restoran-siparis-toast');

  // İletişim
  await s.goto(url + '/iletisim'); await s.waitForLoadState('networkidle'); await ss('14-iletisim');
  await tikla('WhatsApp’tan yaz'); await ss('15-iletisim-toast');
  await tikla('Demoyu sıfırla'); await ss('16-iletisim-sifirla-sheet');
  await tikla('Sıfırla');

  // Yatay taşma kontrolü
  for (const r of ['/', '/kafe', '/berber', '/restoran', '/iletisim']) {
    await s.goto(url + r); await bekle(500);
    const tasma = await s.evaluate(() => document.documentElement.scrollWidth - window.innerWidth);
    if (tasma > 0) hatalar.push(`${sema} ${r}: yatay taşma ${tasma}px`);
  }
  await ctx.close();
}
await tarayici.close();
sunucu.close();
console.log(hatalar.length ? 'HATALAR:\n' + [...new Set(hatalar)].join('\n') : 'Konsol hatası / taşma yok');
