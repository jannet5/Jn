// Gerçek tarayıcı kabul testi (Chromium + playwright-core).
// Çalıştırma: npm run build && npm test
// Çıktı: test-output/ altına ekran görüntüleri ve sonuc.json
import { chromium } from 'playwright-core';
import { mkdir, writeFile } from 'node:fs/promises';
import { existsSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { start } from '../scripts/serve.mjs';

const OUT = fileURLToPath(new URL('../test-output/', import.meta.url));
await mkdir(OUT, { recursive: true });

const executablePath = process.env.CHROMIUM_PATH
  || ['/opt/pw-browsers/chromium-1194/chrome-linux/chrome', '/opt/pw-browsers/chromium/chrome-linux/chrome']
    .find((p) => existsSync(p));

const PORT = 4179;
const server = await start(PORT);
const URL_HTTP = `http://127.0.0.1:${PORT}/`;
const URL_FILE = new URL('../dist/index.html', import.meta.url).href;

const browser = await chromium.launch({
  executablePath,
  args: ['--use-angle=swiftshader', '--enable-unsafe-swiftshader', '--ignore-gpu-blocklist'],
});

const results = [];
function check(id, name, pass, detail) {
  results.push({ id, name, pass: Boolean(pass), detail });
  console.log(`${pass ? 'GEÇTİ ' : 'KALDI '} [${id}] ${name} — ${JSON.stringify(detail)}`);
}
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

async function openPage(url, opts = {}) {
  const ctx = await browser.newContext({ viewport: { width: 1440, height: 900 }, ...opts });
  const page = await ctx.newPage();
  const errors = [];
  page.on('pageerror', (e) => errors.push(String(e)));
  page.on('console', (m) => { if (m.type() === 'error') errors.push(m.text()); });
  await page.goto(url, { waitUntil: 'load' });
  await page.waitForFunction(() => window.__sarmal && window.__sarmal.gallery && window.__sarmal.gallery.frames > 20, null, { timeout: 20000 });
  return { ctx, page, errors };
}
const g = (page) => page.evaluate(() => ({ ...window.__sarmal.gallery }));

// Gerçek ekran görüntüsünden (kompozit sonrası) küçültülmüş parlaklık haritası
async function canvasSignature(page) {
  const png = await page.screenshot({ clip: { x: 0, y: 0, width: page.viewportSize().width, height: page.viewportSize().height } });
  return page.evaluate(async (b64) => {
    const img = new Image();
    img.src = 'data:image/png;base64,' + b64;
    await img.decode();
    const c = document.createElement('canvas');
    c.width = 160; c.height = 100;
    const ctx = c.getContext('2d');
    ctx.drawImage(img, 0, 0, 160, 100);
    const d = ctx.getImageData(0, 0, 160, 100).data;
    let sum = 0; let min = 255; let max = 0;
    const lum = [];
    for (let i = 0; i < d.length; i += 4) {
      const l = (d[i] + d[i + 1] + d[i + 2]) / 3; lum.push(l); sum += l; if (l < min) min = l; if (l > max) max = l;
    }
    return { mean: sum / lum.length, min, max, lum };
  }, png.toString('base64'));
}
function diffRatio(a, b) {
  let n = 0;
  for (let i = 0; i < a.lum.length; i++) if (Math.abs(a.lum[i] - b.lum[i]) > 12) n++;
  return n / a.lum.length;
}

// ---------------------------------------------------------------- masaüstü, http
{
  const { ctx, page, errors } = await openPage(URL_HTTP);
  await sleep(2600); // açılış geçişi
  await page.screenshot({ path: OUT + '01-hero-masaustu.png' });

  const st0 = await g(page);
  check('K0', 'WebGL spiral galeri kuruldu ve çiziyor', st0.frames > 20 && await page.evaluate(() => window.__sarmal.tileCount) === 48,
    { frames: st0.frames, tiles: await page.evaluate(() => window.__sarmal.tileCount) });

  const sig0 = await canvasSignature(page);
  check('K0b', 'Canvas boş değil (gerçek görüntü var)', sig0.max - sig0.min > 60, { min: sig0.min, max: sig0.max, mean: +sig0.mean.toFixed(1) });

  // --- 1) Fare ile dönüş
  await page.mouse.move(80, 450, { steps: 5 });
  await sleep(1500);
  const left = await g(page);
  const sigL = await canvasSignature(page);
  await page.mouse.move(1360, 450, { steps: 20 });
  await sleep(1500);
  const right = await g(page);
  const sigR = await canvasSignature(page);
  const mouseDelta = right.rotCurrent - left.rotCurrent;
  check('K1a', 'Fare X hareketi galeriyi döndürüyor', left.rotCurrent < -0.3 && right.rotCurrent > 0.3,
    { solda_rot: +left.rotCurrent.toFixed(3), sagda_rot: +right.rotCurrent.toFixed(3), fark_rad: +mouseDelta.toFixed(3) });
  check('K1b', 'Fare hareketi görüntüyü gerçekten değiştiriyor (piksel farkı)', diffRatio(sigL, sigR) > 0.05,
    { degisen_piksel_orani: +diffRatio(sigL, sigR).toFixed(3) });
  await page.mouse.move(720, 120, { steps: 5 });
  await sleep(300);

  // --- 2) Kaydırma hızıyla dönüş: boşta < yavaş < hızlı
  async function spinOver(fn) {
    const a = await g(page);
    await fn();
    const b = await g(page);
    return b.spin - a.spin;
  }
  await page.evaluate(() => window.scrollTo(0, 0));
  await sleep(1200);
  const idle = await spinOver(() => sleep(800));
  const slow = await spinOver(async () => { for (let i = 0; i < 8; i++) { await page.mouse.wheel(0, 25); await sleep(100); } });
  await sleep(1500);
  await page.mouse.wheel(0, -2000); await sleep(1800);
  const fast = await spinOver(async () => { for (let i = 0; i < 8; i++) { await page.mouse.wheel(0, 160); await sleep(100); } });
  check('K2', 'Kaydırma hızı dönüşü artırıyor (boşta < yavaş < hızlı)', idle < slow && slow < fast && fast > idle * 2,
    { bosta: +idle.toFixed(3), yavas: +slow.toFixed(3), hizli: +fast.toFixed(3) });

  // kaydırma kamerayı spiral boyunca indiriyor
  const camBefore = (await g(page)).camY;
  await page.mouse.wheel(0, 600); await sleep(1800);
  const camAfter = (await g(page)).camY;
  check('K2b', 'Kaydırma kamerayı spiral boyunca hareket ettiriyor', camAfter < camBefore - 0.3,
    { once: +camBefore.toFixed(2), sonra: +camAfter.toFixed(2) });
  await page.mouse.wheel(0, -5000); await sleep(2200);

  // --- 3) Başlık küçültüldü
  const title = await page.evaluate(() => {
    const el = document.querySelector('.hero__title');
    const r = el.getBoundingClientRect();
    return { fontSize: parseFloat(getComputedStyle(el).fontSize), height: r.height, vh: innerHeight, vw: innerWidth };
  });
  const refSize = Math.min(160, Math.max(54, 0.096 * title.vw)); // ilk sürüm: clamp(54px, 9.6vw, 160px)
  check('K3', 'Kahraman başlığı küçültüldü (ilk sürüme göre ≤ %55, yükseklik ≤ ekranın %30u)',
    title.fontSize <= refSize * 0.55 && title.height <= title.vh * 0.3,
    { yeni_px: title.fontSize, ilk_surum_px: +refSize.toFixed(1), oran: +(title.fontSize / refSize).toFixed(2), baslik_yukseklik_px: Math.round(title.height), ekran_yukseklik: title.vh });

  // --- 4) Galeri hover: karo geri çekilir + etiket
  let hoverOk = null;
  for (const [x, y] of [[860, 450], [760, 430], [960, 480], [720, 520], [1000, 400]]) {
    await page.mouse.move(x, y, { steps: 6 });
    await sleep(1400);
    const s = await g(page);
    if (s.hovered >= 0) {
      const info = await page.evaluate(() => ({
        label: document.querySelector('.cursor-label').classList.contains('is-visible'),
        text: document.querySelector('[data-label="title"]').textContent,
      }));
      hoverOk = { x, y, hovered: s.hovered, ...info };
      break;
    }
  }
  if (hoverOk) await page.screenshot({ path: OUT + '02-galeri-hover.png' });
  check('K4', 'Galeri karosu hover ile seçiliyor, etiket görünüyor', hoverOk && hoverOk.label && hoverOk.text.length > 0, hoverOk);

  // tüm reveal öğeleri sona kadar kaydırınca açılıyor
  const total = await page.evaluate(() => document.querySelectorAll('[data-reveal]').length);
  for (let y = 0; y < 14; y++) { await page.mouse.wheel(0, 700); await sleep(350); }
  await sleep(2200);
  const shown = await page.evaluate(() => document.querySelectorAll('[data-reveal].is-in').length);
  check('K5b', 'Tüm kaydırma girişleri tetikleniyor', shown === total, { gorunen: shown, toplam: total });

  // --- 6) Büyük görsel hover: görsel yavaşça geriye çekiliyor
  await page.evaluate(() => document.querySelector('#isler').scrollIntoView({ block: 'start' }));
  await sleep(2200);
  const work = page.locator('.work').first();
  const scaleOf = () => page.evaluate(() => new DOMMatrix(getComputedStyle(document.querySelector('.work .work__media canvas')).transform).a);
  const s0 = await scaleOf();
  await work.hover();
  await sleep(300);
  const s1 = await scaleOf();
  await sleep(1800);
  const s2 = await scaleOf();
  await page.screenshot({ path: OUT + '04-isler-hover.png' });
  check('K6', 'Çalışma görseli hover’da yavaşça geri çekiliyor (1.12 → 1.00)', s0 > 1.1 && s1 < s0 && s1 > s2 && Math.abs(s2 - 1) < 0.01,
    { baslangic: +s0.toFixed(3), '300ms': +s1.toFixed(3), '2.1sn': +s2.toFixed(3) });

  // hizmet satırı hover
  await page.evaluate(() => document.querySelector('#yetkinlik').scrollIntoView({ block: 'start' }));
  await sleep(1800);
  await page.locator('.services__list li').nth(2).hover();
  await sleep(1200);
  const svc = await page.evaluate(() => new DOMMatrix(getComputedStyle(document.querySelectorAll('.services__name')[2]).transform).m41);
  await page.screenshot({ path: OUT + '05-yetkinlik-hover.png' });
  check('K6b', 'Hizmet satırı hover’da kayıyor', svc > 10, { translateX: +svc.toFixed(1) });

  // --- 7) Yazı değişiklikleri galeriyi bozmadı: başa dönünce galeri hâlâ çiziyor ve tepki veriyor
  await page.evaluate(() => window.scrollTo(0, 0));
  await page.mouse.wheel(0, -20000);
  await sleep(2500);
  const f1 = (await g(page)).frames;
  await page.mouse.move(100, 450, { steps: 10 });
  await sleep(1500);
  const back = await g(page);
  const sigBack = await canvasSignature(page);
  check('K7', 'Metin/animasyon katmanı galeriyi bozmuyor (başa dönüşte çizim + fare tepkisi sürüyor)',
    back.frames > f1 && back.rotCurrent < -0.3 && sigBack.max - sigBack.min > 60,
    { kare_artisi: back.frames - f1, rot: +back.rotCurrent.toFixed(3) });

  const fullH = await page.evaluate(() => document.documentElement.scrollHeight);
  await page.screenshot({ path: OUT + '06-tam-sayfa.png', fullPage: true }).catch(() => {});
  check('K8', 'Masaüstünde konsol/sayfa hatası yok', errors.length === 0, { hatalar: errors.slice(0, 5), sayfa_yuksekligi: fullH });
  await ctx.close();
}

// ---------------------------------------------------------------- yavaş yazı girişi (temiz sayfa)
{
  const { ctx, page } = await openPage(URL_HTTP);
  await sleep(1500);
  // --- 5) Yazılar kaydırmayla yavaşça geliyor
  const before = await page.evaluate(() => {
    const el = document.querySelector('#studyo .manifesto__text .w__i');
    return { opacity: getComputedStyle(el).opacity, revealed: el.parentElement.parentElement.classList.contains('is-in') };
  });
  await page.evaluate(() => document.querySelector('#studyo').scrollIntoView({ block: 'start' }));
  await sleep(350);
  const mid = await page.evaluate(() => getComputedStyle(document.querySelector('#studyo .manifesto__text .w__i')).opacity);
  await sleep(2600);
  const after = await page.evaluate(() => getComputedStyle(document.querySelector('#studyo .manifesto__text .w__i')).opacity);
  check('K5', 'Bölüm yazısı görünmeden önce gizli, kaydırınca yavaşça (≈1.3 sn) beliriyor',
    before.opacity === '0' && !before.revealed && Number(mid) < 0.98 && after === '1',
    { once: before.opacity, '350ms_sonra': Number(mid).toFixed(2), '2.9sn_sonra': after });
  await page.screenshot({ path: OUT + '03-manifesto.png' });

  await ctx.close();
}

// ---------------------------------------------------------------- mobil
{
  const { ctx, page, errors } = await openPage(URL_HTTP, { viewport: { width: 390, height: 844 }, deviceScaleFactor: 2, isMobile: true, hasTouch: true });
  await sleep(2600);
  await page.screenshot({ path: OUT + '07-mobil-hero.png' });
  const m = await page.evaluate(() => ({
    overflow: document.documentElement.scrollWidth - innerWidth,
    title: parseFloat(getComputedStyle(document.querySelector('.hero__title')).fontSize),
  }));
  const st = await g(page);
  check('K9', 'Mobil (390px): galeri çalışıyor, yatay taşma yok', st.frames > 20 && m.overflow <= 0, { ...m, frames: st.frames });
  await page.evaluate(() => document.querySelector('#isler').scrollIntoView());
  await sleep(2500);
  await page.screenshot({ path: OUT + '08-mobil-isler.png' });
  check('K9b', 'Mobilde hata yok', errors.length === 0, errors.slice(0, 5));
  await ctx.close();
}

// ---------------------------------------------------------------- hareket azaltma
{
  const { ctx, page, errors } = await openPage(URL_HTTP, { reducedMotion: 'reduce' });
  await sleep(500);
  const r = await page.evaluate(() => ({
    smooth: window.__sarmal.smooth,
    allIn: [...document.querySelectorAll('[data-reveal]')].every((e) => e.classList.contains('is-in')),
    contactOpacity: getComputedStyle(document.querySelector('.contact__mail')).opacity,
  }));
  check('K10', 'prefers-reduced-motion: yazılar hemen görünür, yumuşak kaydırma kapalı', !r.smooth && r.allIn && r.contactOpacity === '1', r);
  check('K10b', 'Hareket azaltma modunda hata yok', errors.length === 0, errors.slice(0, 5));
  await ctx.close();
}

// ---------------------------------------------------------------- file:// (sunucusuz açılış)
{
  const { ctx, page, errors } = await openPage(URL_FILE);
  const st = await g(page);
  check('K11', 'dist/index.html çift tıklamayla (file://) açılıyor ve galeri çiziyor', st.frames > 20 && errors.length === 0, { frames: st.frames, hatalar: errors.slice(0, 3) });
  await ctx.close();
}

await browser.close();
server.close();

const passed = results.filter((r) => r.pass).length;
const summary = { tarih: new Date().toISOString(), chromium: executablePath, gecen: passed, toplam: results.length, sonuclar: results };
await writeFile(OUT + 'sonuc.json', JSON.stringify(summary, null, 2));
console.log(`\n${passed}/${results.length} kabul kontrolü geçti`);
process.exit(passed === results.length ? 0 : 1);
