// Gerçek Chromium'da arayüzü sürer. Çalıştırma: npm run e2e
// Chromium yolu: CHROMIUM_PATH ya da /opt/pw-browsers altındaki sürüm.
import { chromium } from 'playwright-core';
import { mkdirSync, existsSync, readdirSync } from 'node:fs';
import { fileURLToPath } from 'node:url';
import { dirname, join } from 'node:path';
import assert from 'node:assert/strict';
import http from 'node:http';
import { readFileSync } from 'node:fs';
import { createApp } from '../../server/app.js';

const here = dirname(fileURLToPath(import.meta.url));
const OUT = join(here, 'out');
mkdirSync(OUT, { recursive: true });

function chromiumPath() {
  if (process.env.CHROMIUM_PATH) return process.env.CHROMIUM_PATH;
  const root = process.env.PLAYWRIGHT_BROWSERS_PATH ?? '/opt/pw-browsers';
  for (const d of existsSync(root) ? readdirSync(root) : []) {
    const p = join(root, d, 'chrome-linux', 'chrome');
    if (d.startsWith('chromium-') && existsSync(p)) return p;
  }
  throw new Error('Chromium bulunamadı; CHROMIUM_PATH ayarlayın');
}

// MODE=http (varsayılan): gerçek sunucuya karşı. MODE=card: sunucusuz kart sürümüne (card/dist) karşı.
const MODE = process.env.MODE === 'card' ? 'card' : 'http';
const SQLJS_URL = 'https://cdnjs.cloudflare.com/ajax/libs/sql.js/1.13.0/sql-asm.js';
const DIST = join(here, '..', '..', 'card', 'dist');
let app = null;
let staticServer = null;
let base;

if (MODE === 'http') {
  app = createApp({ webRoot: join(here, '..', '..', 'web'), tickMs: 500 });
  await new Promise((r) => app.server.listen(0, '127.0.0.1', r));
  base = `http://127.0.0.1:${app.server.address().port}`;
} else {
  // Artifact çerçevesini taklit eder: doctype/head'i ekler ve yalnızca kendi dosyalarına + cdnjs betiğine izin veren sıkı bir CSP uygular.
  const TYPES = { '.js': 'text/javascript', '.css': 'text/css', '.html': 'text/html; charset=utf-8' };
  staticServer = http.createServer((req, res) => {
    const name = new URL(req.url, 'http://x').pathname === '/' ? 'index.html' : new URL(req.url, 'http://x').pathname.slice(1);
    const file = join(DIST, name);
    if (!file.startsWith(DIST) || !existsSync(file)) { res.writeHead(404); return res.end(); }
    let body = readFileSync(file);
    if (name === 'index.html') body = `<!doctype html><html lang="tr"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover"></head><body>${body}</body></html>`;
    res.writeHead(200, {
      'content-type': TYPES[name.slice(name.lastIndexOf('.'))] ?? 'application/octet-stream',
      'content-security-policy': "default-src 'none'; script-src 'self' https://cdnjs.cloudflare.com; style-src 'self'; img-src 'self' data:; connect-src 'self'",
    });
    res.end(body);
  });
  await new Promise((r) => staticServer.listen(0, '127.0.0.1', r));
  base = `http://127.0.0.1:${staticServer.address().port}`;
}

const api = MODE === 'http'
  ? async (method, path, body) => {
      const res = await fetch(base + path, { method, headers: { 'content-type': 'application/json' }, body: method === 'GET' ? undefined : JSON.stringify(body ?? {}) });
      return { status: res.status, json: await res.json().catch(() => null) };
    }
  : (method, path, body) => page.evaluate(async ([m, p, b]) => {
      try { return { status: 200, json: await window.__notivo.api(m, p, b) }; }
      catch (e) { return { status: e.status ?? 500, json: { error: { code: e.code, message: e.message } } }; }
    }, [method, path, body]);
const chan = (pkg, importance = 3, name = 'Kanal', appName = pkg, id = 'ch') => api('POST', `/api/apps/${pkg}/channels`, { id, name, importance, appName });
const post = (o) => api('POST', '/api/notify', { channel: 'ch', id: 1, title: 'Başlık', text: 'metin', ...o });

const browser = await chromium.launch({ executablePath: chromiumPath(), args: ['--no-sandbox'] });
const ctx = await browser.newContext({ viewport: { width: 412, height: 860 }, locale: 'tr-TR' });
if (MODE === 'card') await ctx.addInitScript(() => localStorage.setItem('notivo-no-seed', '1'));
if (MODE === 'card') await ctx.route(SQLJS_URL, (route) => route.fulfill({ path: join(here, '..', '..', 'node_modules', 'sql.js', 'dist', 'sql-asm.js'), contentType: 'text/javascript' }));
const page = await ctx.newPage();
const problems = [];
page.on('pageerror', (e) => problems.push(`pageerror: ${e.message} ${String(e.stack).split('\n').slice(0,3).join(' / ')}`));
page.on('console', (m) => {
  if (m.type() === 'error') problems.push(`console.error: ${m.text()}`);
});

let failed = 0;
async function step(name, fn) {
  try {
    await fn();
    console.log('  ✓', name);
  } catch (e) {
    failed++;
    console.log('  ✗', name, '\n     ', String(e.message).split('\n').slice(0, 3).join('\n      '));
    await page.screenshot({ path: join(OUT, `FAIL-${failed}.png`) }).catch(() => {});
  }
}
const shot = (name) => page.screenshot({ path: join(OUT, `${name}.png`) });
const q = (s) => page.locator(s);
const sec = (title) => q(`section[aria-label="${title}"]`);

async function reset() {
  for (const a of (await api('GET', '/api/apps')).json.apps) await api('POST', '/api/cancel-all', { pkg: a.pkg });
  await api('PUT', '/api/dnd', { enabled: false });
  await page.waitForFunction(() => document.querySelector('#sections').children.length === 0);
  await q('#headsup').evaluate((el) => { el.hidden = true; el.replaceChildren(); });
}
async function swipe(locator, fraction) {
  const b = await locator.boundingBox();
  const y = b.y + Math.min(b.height / 2, 30);
  const x = b.x + b.width * 0.35;
  await page.mouse.move(x, y);
  await page.mouse.down();
  await page.mouse.move(x + b.width * fraction, y, { steps: 12 });
  await page.mouse.up();
}

console.log(`Notivo arayüz uçtan uca testi (${MODE === 'card' ? 'kart sürümü, sunucusuz' : 'HTTP sunucusu'})`);
await page.goto(base);

await step('açılış: boş durum ve canlı bağlantı', async () => {
  await q('#conn.live').waitFor();
  await q('#empty').waitFor({ state: 'visible' });
  assert.equal(await q('#clearAll').isHidden(), true);
});

await step('yüksek önemli konuşma: heads-up çıkar, "Konuşmalar" bölümüne düşer', async () => {
  await chan('com.whatsapp', 4, 'Mesajlar', 'WhatsApp', 'msg');
  const r = await post({ pkg: 'com.whatsapp', channel: 'msg', id: 1, title: 'Ayşe', text: 'Akşam görüşelim mi?', conversation: true, contentIntent: 'whatsapp://chat/ayse', actions: [{ id: 'reply', title: 'Yanıtla', reply: true }, { id: 'read', title: 'Okundu işaretle' }] });
  assert.equal(r.json.alerted.headsUp, true);
  await q('#headsup').waitFor({ state: 'visible' });
  assert.match(await q('#headsup').innerText(), /Akşam görüşelim mi\?/);
  await page.waitForTimeout(400);
  await shot('01-headsup');
  await q('#headsup button[aria-label="Kapat"]').click();
  await q('#headsup').waitFor({ state: 'hidden' });
  await sec('Konuşmalar').locator('article.card').first().waitFor();
});

await step('kısa kaydırma geri sıçrar, uzun kaydırma kapatır ve geçmişe yazar', async () => {
  const card = sec('Konuşmalar').locator('article.card').first();
  await swipe(card, 0.15);
  await page.waitForTimeout(350);
  assert.equal(await sec('Konuşmalar').locator('article.card').count(), 1);
  await swipe(card, 0.8);
  await sec('Konuşmalar').waitFor({ state: 'detached' });
  const h = (await api('GET', '/api/history?state=removed')).json.items;
  assert.equal(h[0].removedReason, 'USER_SWIPE');
});

await step('dokunma: contentIntent açılır, autoCancel bildirimi kaldırır', async () => {
  await post({ pkg: 'com.whatsapp', channel: 'msg', id: 2, title: 'Mehmet', text: 'Fotoğraflar', conversation: true, contentIntent: 'whatsapp://chat/mehmet' });
  await q('#headsup button[aria-label="Kapat"]').click();
  await sec('Konuşmalar').locator('.card-body').first().click();
  await q('#toast').getByText('Açılıyor: whatsapp://chat/mehmet').waitFor();
  await sec('Konuşmalar').waitFor({ state: 'detached' });
});

await step('süregelen bildirim: kaydırılamaz, uyarı çıkar, "Tümünü temizle" dokunmaz', async () => {
  await chan('com.spotify', 2, 'Çalan şarkı', 'Müzik', 'ch');
  await post({ pkg: 'com.spotify', id: 1, title: 'Gece Yolculuğu', text: 'Kayıp Şehir', ongoing: true });
  const card = sec('Sessiz bildirimler').locator('article.card').first();
  await card.waitFor();
  await swipe(card, 0.9);
  await q('#toast').getByText('Süregelen bildirimler kaydırılarak kapatılamaz').waitFor();
  assert.equal(await sec('Sessiz bildirimler').locator('article.card').count(), 1);
  assert.equal(await q('#clearAll').isHidden(), true); // kapatılabilir hiçbir şey yok
  await chan('com.news', 2, 'Haber', 'Haber', 'ch');
  await post({ pkg: 'com.news', id: 5, title: 'Haber', text: 'sessiz haber' });
  await q('#clearAll').waitFor({ state: 'visible' });
  await q('#clearAll').click();
  await page.waitForFunction(() => document.querySelectorAll('#sections article.card').length === 1);
  assert.match(await q('#sections').innerText(), /Gece Yolculuğu/);
});

await step('durum çubuğu ve menüde "null"/"false" metni yok; ongoing menüsünde ertele yok', async () => {
  const bar = await q('.statusbar').innerText();
  assert.doesNotMatch(bar, /null|false|undefined/);
  await sec('Sessiz bildirimler').getByRole('button', { name: /Müzik bildirim seçenekleri/ }).click();
  await q('#menuBody').getByRole('button').first().waitFor();
  const menu = await q('#menuBody').innerText();
  assert.doesNotMatch(menu, /null|false|undefined/);
  assert.doesNotMatch(menu, /Ertele/);
  await q('#dlgMenu [data-close]').click();
});

await step('açık grup: ≥2 çocuk tek grupta, açılır, grup kaydırılınca hepsi gider', async () => {
  await chan('com.google.gm', 3, 'E-posta', 'Gmail', 'ch');
  await post({ pkg: 'com.google.gm', id: 100, group: 'inbox', title: 'Banka', text: 'Ekstreniz hazır' });
  await post({ pkg: 'com.google.gm', id: 101, group: 'inbox', title: 'Kargo', text: 'Paketiniz yolda' });
  await post({ pkg: 'com.google.gm', id: 1, group: 'inbox', groupSummary: true, title: 'Gelen kutusu', text: '2 yeni ileti' });
  const group = sec('Bildirimler').locator('article.group');
  await group.waitFor();
  assert.equal((await group.locator('.count-badge').innerText()).trim(), '2');
  assert.equal(await group.locator('article.card').count(), 0);
  await group.locator('.group-head').click();
  await group.locator('article.card').first().waitFor();
  assert.equal(await group.locator('article.card').count(), 2);
  await shot('02-group-open');
  await group.locator('.group-head').click(); // daralt
  await group.locator('article.card').first().waitFor({ state: 'detached' });
  await swipe(group.locator('.group-head'), 0.1); // kısa kaydırma gruba dokunmuş sayılmamalı
  await page.waitForTimeout(350);
  assert.equal(await group.locator('article.card').count(), 0, 'kısa kaydırma grubu açmamalı');
  await swipe(group.locator('.group-head'), 0.85);
  await sec('Bildirimler').waitFor({ state: 'detached' });
});

await step('otomatik gruplama: gruplanmamış 4 bildirim tek grupta; bölüm "Temizle" ile gider', async () => {
  await chan('com.instagram', 3, 'Etkinlik', 'Instagram', 'ch');
  for (let i = 1; i <= 3; i++) await post({ pkg: 'com.instagram', id: i, title: `k${i}`, text: 'beğendi' });
  assert.equal(await sec('Bildirimler').locator('article.group').count(), 0);
  await post({ pkg: 'com.instagram', id: 4, title: 'k4', text: 'beğendi' });
  await sec('Bildirimler').locator('article.group').waitFor();
  await sec('Bildirimler').getByRole('button', { name: 'Bildirimler bölümünü temizle' }).click();
  await sec('Bildirimler').waitFor({ state: 'detached' });
});

await step('simülatör: WhatsApp mesajı → heads-up → satır içi yanıt → uygulama bildirimi günceller', async () => {
  await reset();
  await q('#tileSim').click();
  await q('#simGrid').getByRole('button', { name: /WhatsApp mesajı/ }).click();
  await q('#headsup').waitFor({ state: 'visible' });
  await q('#headsup').getByRole('button', { name: 'Yanıtla' }).click();
  await q('#headsup input').fill('Tamam geliyorum');
  await q('#headsup input').press('Enter');
  await q('#toast').getByText('Yanıt gönderildi').waitFor();
  await sec('Konuşmalar').getByText('Sen: Tamam geliyorum').waitFor();
  const hist = (await api('GET', '/api/history')).json.items.map((x) => x.text);
  assert.ok(hist.includes('Sen: Tamam geliyorum') && hist.some((t) => !t.startsWith('Sen:')), 'önceki mesaj geçmişte kalmalı');
});

await step('Rahatsız Etme: heads-up bastırılır ama bildirim çubuğa düşer; alarm geçer', async () => {
  await reset();
  await q('#tileDnd').click();
  await q('#dndBadge').waitFor({ state: 'visible' });
  assert.equal(await q('#tileDnd').getAttribute('aria-pressed'), 'true');
  await chan('com.whatsapp', 4, 'Mesajlar', 'WhatsApp', 'msg');
  const r = await post({ pkg: 'com.whatsapp', channel: 'msg', id: 9, title: 'Sessiz', text: 'DND açık', conversation: true, category: 'msg' });
  assert.equal(r.json.alerted.suppressedByDnd, true);
  await sec('Konuşmalar').locator('article.card').first().waitFor();
  await page.waitForTimeout(300);
  assert.equal(await q('#headsup').isHidden(), true);
  await chan('com.alarm', 4, 'Alarm', 'Alarm', 'ch');
  await post({ pkg: 'com.alarm', id: 1, title: 'Alarm', text: 'Duman algılandı', category: 'alarm' });
  await q('#headsup').waitFor({ state: 'visible' });
  await q('#tileDnd').click();
  await q('#dndBadge').waitFor({ state: 'hidden' });
});

await step('geçmiş: Türkçe harf duyarsız arama, durum süzgeci', async () => {
  await reset();
  await chan('com.shop', 3, 'Kampanya', 'Mağaza', 'ch');
  await post({ pkg: 'com.shop', id: 1, title: 'Büyük İNDİRİM', text: 'Her şey yarı fiyat' });
  await post({ pkg: 'com.shop', id: 2, title: 'Sipariş', text: 'Kargoya verildi' });
  await q('#tileHistory').click();
  await q('#histList .hist-item').first().waitFor();
  await q('#histQ').fill('indirim');
  await page.waitForFunction(() => document.querySelectorAll('#histList .hist-item').length === 1);
  assert.match(await q('#histList').innerText(), /Büyük İNDİRİM/);
  await q('#histQ').fill('');
  await q('#histState').selectOption('snoozed');  // henüz ertelenen yok
  await q('#histList').getByText('Henüz kayıt yok.').waitFor();
  await shot('03-history');
  await q('#dlgHistory [data-close]').click();
});

await step('kurallar: arayüzden BLOCK kuralı eklenir, eşleşen bildirim gösterilmez', async () => {
  await q('#tileRules').click();
  await q('#ruleKw').fill('reklam');
  await q('#ruleAction').selectOption('BLOCK');
  await q('#ruleForm button[type=submit]').click();
  await q('#ruleList').getByText('“reklam”').waitFor();
  await q('#dlgRules [data-close]').click();
  const r = await post({ pkg: 'com.shop', id: 7, title: 'REKLAM fırsatı', text: 'x' });
  assert.equal(r.json.status, 'blocked_by_rule');
  const bad = await api('POST', '/api/rules', { keyword: '', pkg: '', action: 'BLOCK' });
  assert.equal(bad.status, 400);
});

await step('menü: bildirimi 15 dakika ertele; "Ertelenen: 1" görünür', async () => {
  await reset();
  await chan('com.mail2', 3, 'Posta', 'Posta', 'ch');
  await post({ pkg: 'com.mail2', id: 1, title: 'Toplantı', text: 'Yarın 10:00' });
  await sec('Bildirimler').locator('article.card').first().waitFor();
  await sec('Bildirimler').getByRole('button', { name: /bildirim seçenekleri/ }).click();
  await q('#menuBody').getByRole('button', { name: '15 dakika' }).waitFor();
  await shot('04-menu');
  await q('#menuBody').getByRole('button', { name: '15 dakika' }).click();
  await sec('Bildirimler').waitFor({ state: 'detached' });
  await q('#snoozedChip').waitFor({ state: 'visible' });
  assert.equal((await q('#snoozedChip').innerText()).trim(), 'Ertelenen: 1');
});

await step('güvenlik: bildirim içeriği HTML olarak yorumlanmaz', async () => {
  await reset();
  await post({ pkg: 'com.mail2', id: 9, title: '<img src=x onerror="window.__xss=1">', text: '<script>window.__xss=2</script>' });
  const card = sec('Bildirimler').locator('article.card').first();
  await card.waitFor();
  assert.match(await card.innerText(), /<img src=x onerror=/);
  assert.equal(await page.evaluate(() => window.__xss), undefined);
});

await step('görünüm: karışık durum ekran görüntüsü; dar ekranda yatay taşma yok', async () => {
  await reset();
  await chan('com.whatsapp', 4, 'Mesajlar', 'WhatsApp', 'msg');
  await post({ pkg: 'com.whatsapp', channel: 'msg', id: 1, title: 'Ayşe', text: 'Akşam görüşelim mi?', conversation: true });
  await q('#headsup button[aria-label="Kapat"]').click();
  await post({ pkg: 'com.google.gm', id: 100, group: 'inbox', title: 'Banka', text: 'Ekstreniz hazır' });
  await post({ pkg: 'com.google.gm', id: 101, group: 'inbox', title: 'Kargo', text: 'Paketiniz yolda' });
  await post({ pkg: 'com.spotify', id: 1, title: 'Gece Yolculuğu', text: 'Kayıp Şehir · Sessiz Albüm', ongoing: true });
  await post({ pkg: 'com.news', id: 5, title: 'Haber', text: 'Gündem özeti' });
  await sec('Sessiz bildirimler').locator('article.card').first().waitFor();
  await shot('05-mixed');
  await page.setViewportSize({ width: 360, height: 740 });
  assert.equal(await page.evaluate(() => document.documentElement.scrollWidth <= window.innerWidth), true);
  await shot('06-narrow');
  await page.setViewportSize({ width: 412, height: 860 });
});

if (MODE === 'card') {
  await step('kart: Proje paneli (Android / İstekler / Özellikler) açılır, indirme düğmesi ve sürüm var', async () => {
    await page.click('#tileProject');
    await page.waitForSelector('#dlgProject[open]');
    const android = await page.locator('#dlgProject .panel').innerText();
    if (!/1\.3\.0/.test(android) || !/SHA-256/.test(android)) throw new Error('Android sekmesinde sürüm/SHA yok: ' + android.slice(0, 120));
    for (const t of ['İstekler', 'Özellikler']) {
      await page.click(`#dlgProject [role=tab]:has-text("${t}")`);
      const txt = await page.locator('#dlgProject .panel').innerText();
      if (txt.length < 100) throw new Error(t + ' sekmesi boş');
    }
    await page.click('#dlgProject [data-close]');
  });
}

if (MODE === 'card') {
  await step('kart: veri bu cihazda kalır (sayfa yenilenince bildirim yerinde, ertelenen de)', async () => {
    await reset();
    await chan('com.whatsapp', 4, 'Mesajlar', 'WhatsApp', 'msg');
    await post({ pkg: 'com.whatsapp', channel: 'msg', id: 31, title: 'Kalıcı', text: 'yenileme sonrası da görünmeli', conversation: true });
    await post({ pkg: 'com.whatsapp', channel: 'msg', id: 32, title: 'Ertelenen', text: 'gizli', conversation: true });
    await sec('Konuşmalar').getByText('yenileme sonrası da görünmeli').waitFor();
    await api('POST', '/api/shade/snooze', { key: 'com.whatsapp|32|', minutes: 30 });
    await page.waitForTimeout(700); // kaydetme gecikmesi (400 ms) dolsun
    await page.reload();
    await sec('Konuşmalar').getByText('yenileme sonrası da görünmeli').waitFor();
    await q('#snoozedChip').waitFor({ state: 'visible' });
    assert.equal((await q('#snoozedChip').innerText()).trim(), 'Ertelenen: 1');
    assert.equal((await api('GET', '/api/history?q=gizli')).json.items.length, 1);
  });
}

if (MODE === 'card') {
  await step('kart: ilk açılışta örnek bildirimler hazırdır (boş ekran yok)', async () => {
    const fresh = await browser.newContext({ viewport: { width: 412, height: 860 }, locale: 'tr-TR' });
    await fresh.route(SQLJS_URL, (route) => route.fulfill({ path: join(here, '..', '..', 'node_modules', 'sql.js', 'dist', 'sql-asm.js'), contentType: 'text/javascript' }));
    const p = await fresh.newPage();
    await p.goto(base);
    await p.locator('#conn.live').waitFor();
    await p.locator('#sections article').first().waitFor();
    const text = await p.locator('#sections').innerText();
    assert.match(text, /WhatsApp/);
    assert.match(text, /Gmail/);
    assert.match(text, /Gece Yolculuğu/);
    assert.equal(await p.locator('#empty').isHidden(), true);
    await p.screenshot({ path: join(OUT, '08-first-run.png') });
    await fresh.close();
  });
}

await step('tarayıcı konsolunda hata/CSP ihlali yok', async () => {
  assert.deepEqual(problems, [], JSON.stringify(problems));
});

await browser.close();
await app?.close();
staticServer?.close();
console.log(failed ? `\n${failed} adım başarısız` : '\nTüm uçtan uca adımlar geçti');
console.log(`Ekran görüntüleri: ${OUT}`);
process.exit(failed ? 1 : 0);
