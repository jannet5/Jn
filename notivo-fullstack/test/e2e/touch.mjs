// Telefon simülasyonu: gerçek dokunma olaylarıyla (CDP Input.dispatchTouchEvent) kart sürümünü sürer.
// Önce: npm run build:card. Çalıştırma: node test/e2e/touch.mjs
import { chromium } from 'playwright-core';
import http from 'node:http';
import { readFileSync, existsSync, readdirSync, mkdirSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, join } from 'node:path';
import assert from 'node:assert/strict';

const here = dirname(fileURLToPath(import.meta.url));
const DIST = join(here, '..', '..', 'card', 'dist');
const OUT = join(here, 'out');
mkdirSync(OUT, { recursive: true });
const root = process.env.PLAYWRIGHT_BROWSERS_PATH ?? '/opt/pw-browsers';
const chrome = process.env.CHROMIUM_PATH ?? readdirSync(root).filter((d) => d.startsWith('chromium-')).map((d) => join(root, d, 'chrome-linux', 'chrome')).find(existsSync);

const server = http.createServer((req, res) => {
  const name = new URL(req.url, 'http://x').pathname === '/' ? 'index.html' : new URL(req.url, 'http://x').pathname.slice(1);
  const file = join(DIST, name);
  if (!file.startsWith(DIST) || !existsSync(file)) { res.writeHead(404); return res.end(); }
  let body = readFileSync(file);
  if (name === 'index.html') body = `<!doctype html><html lang="tr"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover"></head><body>${body}</body></html>`;
  res.writeHead(200, { 'content-type': name.endsWith('.js') ? 'text/javascript' : name.endsWith('.css') ? 'text/css' : 'text/html; charset=utf-8' });
  res.end(body);
});
await new Promise((r) => server.listen(0, '127.0.0.1', r));
const base = `http://127.0.0.1:${server.address().port}`;

const browser = await chromium.launch({ executablePath: chrome, args: ['--no-sandbox'] });
const ctx = await browser.newContext({ viewport: { width: 390, height: 780 }, deviceScaleFactor: 2, isMobile: true, hasTouch: true, locale: 'tr-TR' });
await ctx.route('https://cdnjs.cloudflare.com/ajax/libs/sql.js/1.13.0/sql-asm.js', (r) => r.fulfill({ path: join(here, '..', '..', 'node_modules', 'sql.js', 'dist', 'sql-asm.js'), contentType: 'text/javascript' }));
await ctx.addInitScript(() => localStorage.setItem('notivo-no-seed', '1'));
const page = await ctx.newPage();
const cdp = await ctx.newCDPSession(page);
const problems = [];
page.on('pageerror', (e) => problems.push(e.message));
page.on('console', (m) => m.type() === 'error' && problems.push(m.text()));

const api = (method, path, body) => page.evaluate(async ([m, p, b]) => window.__notivo.api(m, p, b), [method, path, body]);
const touch = (type, x, y) => cdp.send('Input.dispatchTouchEvent', { type, touchPoints: type === 'touchEnd' ? [] : [{ x, y }] });
async function drag(fromX, fromY, toX, toY, steps = 12) {
  await touch('touchStart', fromX, fromY);
  for (let i = 1; i <= steps; i++) await touch('touchMove', fromX + ((toX - fromX) * i) / steps, fromY + ((toY - fromY) * i) / steps);
  await touch('touchEnd');
}
let failed = 0;
async function step(name, fn) {
  try { await fn(); console.log('  ✓', name); } catch (e) { failed++; console.log('  ✗', name, '\n     ', String(e.message).split('\n')[0]); await page.screenshot({ path: join(OUT, `TOUCH-FAIL-${failed}.png`) }); }
}
// Her bildirim ayrı uygulamadan: aynı uygulamanın 4+ bildirimi zaten otomatik gruba girer (Android davranışı).
const seed = async (n) => {
  for (let i = 1; i <= n; i++) {
    const pkg = `com.haber${i}`;
    await api('POST', `/api/apps/${pkg}/channels`, { id: 'ch', name: 'Haber', importance: 3, appName: `Haber ${i}` });
    await api('POST', '/api/notify', { pkg, channel: 'ch', id: 1, title: `Haber ${i}`, text: `Gündem #${i}`, contentIntent: `news://${i}` });
  }
};

console.log('Notivo kart: dokunmatik (telefon) testi');
await page.goto(base);
await page.locator('#conn.live').waitFor();

await step('yatay dokunmatik kaydırma bildirimi kapatır', async () => {
  await seed(1);
  const card = page.locator('#sections article.card').first();
  await card.waitFor();
  const b = await card.boundingBox();
  await drag(b.x + b.width * 0.3, b.y + 24, b.x + b.width * 0.95, b.y + 26);
  await page.waitForFunction(() => document.querySelectorAll('#sections article.card').length === 0);
});

await step('dikey kaydırma listeyi kaydırır, bildirimi KAPATMAZ', async () => {
  await seed(14);
  await page.waitForFunction(() => document.querySelectorAll('#sections article.card').length === 14);
  const shade = page.locator('#shade');
  const sb = await shade.boundingBox();
  const before = await shade.evaluate((el) => el.scrollTop);
  await drag(sb.x + sb.width / 2, sb.y + sb.height - 40, sb.x + sb.width / 2 + 6, sb.y + 40, 14);
  await page.waitForTimeout(400);
  const after = await shade.evaluate((el) => el.scrollTop);
  assert.ok(after > before + 50, `liste kaymadı (${before} → ${after})`);
  assert.equal(await page.locator('#sections article.card').count(), 14, 'dikey kaydırma bildirimi kapattı');
});

await step('dokunma (tap) bildirimi açar ve toast gösterir', async () => {
  const card = page.locator('#sections article.card .card-body').first();
  await card.scrollIntoViewIfNeeded();
  await card.tap();
  await page.locator('#toast').waitFor({ state: 'visible' });
  assert.match(await page.locator('#toast').innerText(), /açılıyor/i);
});

await step('düğmeler dokunmaya uygun boyutta (≥ 32 px)', async () => {
  const sizes = await page.evaluate(() => [...document.querySelectorAll('.icon-btn, .tile, .clear-all')].map((e) => { const r = e.getBoundingClientRect(); return Math.min(r.width, r.height); }));
  assert.ok(sizes.length > 0 && sizes.every((s) => s >= 32), `küçük hedefler: ${sizes.filter((s) => s < 32)}`);
});

await step('dar telefon ekranında yatay taşma yok; ekran görüntüsü', async () => {
  assert.equal(await page.evaluate(() => document.documentElement.scrollWidth <= window.innerWidth), true);
  await page.screenshot({ path: join(OUT, '07-phone-touch.png') });
});

await step('konsolda hata yok', async () => assert.deepEqual(problems, []));
await browser.close();
server.close();
console.log(failed ? `\n${failed} adım başarısız` : '\nTüm dokunmatik adımlar geçti');
process.exit(failed ? 1 : 0);
