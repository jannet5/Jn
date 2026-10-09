// Kullanım: node scripts/ss.mjs <önek> [url]  → ../ss/<önek>-{375,768,1440}.png + koyu 375/1440
import { chromium } from 'playwright';
const prefix = process.argv[2] || 'v1';
const url = process.argv[3] || 'http://localhost:4321/';
const out = new URL('../../ss/', import.meta.url).pathname;
const browser = await chromium.launch({ executablePath: process.env.CHROME_PATH || '/opt/pw-browsers/chromium-1194/chrome-linux/chrome' });
const runs = [[375,'light'],[768,'light'],[1440,'light'],[375,'dark'],[1440,'dark']];
for (const [w, scheme] of runs) {
  const page = await browser.newPage({ viewport: { width: w, height: 900 }, colorScheme: scheme, deviceScaleFactor: 1 });
  const errors = [];
  page.on('console', m => m.type() === 'error' && errors.push(m.text()));
  page.on('pageerror', e => errors.push(e.message));
  await page.goto(url, { waitUntil: 'networkidle' });
  // Tüm reveal'leri tetiklemek için sayfayı aşağı kaydır
  await page.evaluate(async () => { for (let y = 0; y < document.body.scrollHeight; y += 400) { window.scrollTo({ top: y, behavior: 'instant' }); await new Promise(r => setTimeout(r, 60)); } window.scrollTo({ top: 0, behavior: 'instant' }); await new Promise(r => setTimeout(r, 1700)); });
  const overflow = await page.evaluate(() => document.documentElement.scrollWidth - window.innerWidth);
  const name = `${prefix}-${scheme === 'dark' ? 'koyu-' : ''}${w}.png`;
  await page.screenshot({ path: out + name, fullPage: true });
  console.log(name, 'taşma:', overflow, 'hata:', errors.length ? errors : 'yok');
  await page.close();
}
await browser.close();
