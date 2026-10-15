import test from 'node:test';
import assert from 'node:assert/strict';
import http from 'node:http';
import { mkdtempSync, writeFileSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join } from 'node:path';
import { createApp } from '../../server/app.js';

async function start(opts = {}) {
  const web = mkdtempSync(join(tmpdir(), 'nv-web-'));
  writeFileSync(join(web, 'index.html'), '<!doctype html><title>t</title><h1>ok</h1>');
  const app = createApp({ webRoot: web, tickMs: 0, ...opts });
  await new Promise((r) => app.server.listen(0, '127.0.0.1', r));
  const port = app.server.address().port;
  const base = `http://127.0.0.1:${port}`;
  const api = async (method, path, body, headers = {}) => {
    const res = await fetch(base + path, {
      method,
      headers: { 'content-type': 'application/json', ...(opts.token ? { authorization: `Bearer ${opts.token}` } : {}), ...headers },
      body: body === undefined ? undefined : JSON.stringify(body),
    });
    let json = null;
    try {
      json = await res.json();
    } catch {
      /* gövde yok */
    }
    return { status: res.status, json, headers: res.headers };
  };
  return { app, base, port, api, close: () => app.close() };
}

function raw({ port, method = 'GET', path, headers = {}, body }) {
  return new Promise((resolve, reject) => {
    const req = http.request({ host: '127.0.0.1', port, method, path, headers }, (res) => {
      let d = '';
      res.on('data', (c) => (d += c));
      res.on('end', () => resolve({ status: res.statusCode, headers: res.headers, body: d }));
    });
    req.on('error', reject);
    if (body) req.write(body);
    req.end();
  });
}

const channel = (api, pkg = 'com.chat', importance = 4) => api('POST', `/api/apps/${pkg}/channels`, { id: 'msg', name: 'Mesajlar', importance, appName: 'Sohbet' });
const notify = (api, o = {}) => api('POST', '/api/notify', { pkg: 'com.chat', channel: 'msg', id: 1, title: 'Ayşe', text: 'Merhaba', ...o });

test('uçtan uca akış: kanal → bildirim → çubuk → dokun → geçmiş', async () => {
  const t = await start();
  try {
    assert.equal((await channel(t.api)).json.channel.importance, 4);
    const r = await notify(t.api, { conversation: true, contentIntent: 'chat://ayse' });
    assert.equal(r.status, 200);
    assert.equal(r.json.status, 'posted');
    assert.equal(r.json.section, 'conversations');
    assert.deepEqual(r.json.alerted, { sound: true, headsUp: true, suppressedByDnd: false });
    const shade = (await t.api('GET', '/api/shade')).json;
    assert.equal(shade.total, 1);
    assert.equal(shade.sections[0].entries[0].n.title, 'Ayşe');
    const click = await t.api('POST', '/api/shade/click', { key: 'com.chat|1|' });
    assert.deepEqual(click.json, { ok: true, key: 'com.chat|1|', contentIntent: 'chat://ayse', cancelled: true });
    const hist = (await t.api('GET', `/api/history?q=${encodeURIComponent('AYŞE')}`)).json.items;
    assert.equal(hist.length, 1);
    assert.equal(hist[0].removedReason, 'CLICK');
    assert.equal((await t.api('GET', '/api/apps')).json.apps[0].channels[0].id, 'msg');
  } finally {
    await t.close();
  }
});

test('hata gövdeleri: 400 doğrulama, 404 yok, 405 yöntem, 409 süregelen', async () => {
  const t = await start();
  try {
    assert.equal((await t.api('POST', '/api/notify', { pkg: 'x', channel: 'nope', title: 't' })).json.error.code, 'unknown_channel');
    assert.equal((await t.api('POST', '/api/notify', { pkg: 'bad pkg', channel: 'c', title: 't' })).json.error.code, 'invalid_pkg');
    assert.equal((await t.api('GET', '/api/nothing')).status, 404);
    assert.equal((await t.api('DELETE', '/api/shade')).status, 405);
    await channel(t.api, 'com.music', 2);
    await notify(t.api, { pkg: 'com.music', ongoing: true, title: 'Çalıyor' });
    const r = await t.api('POST', '/api/shade/dismiss', { key: 'com.music|1|' });
    assert.equal(r.status, 409);
    assert.equal(r.json.error.code, 'ongoing');
    assert.equal((await t.api('POST', '/api/shade/clear-all', {})).json.dismissed, 0);
    assert.equal((await t.api('GET', '/api/shade')).json.total, 1);
  } finally {
    await t.close();
  }
});

test('CSRF: JSON olmayan değiştirici istekler ve yabancı Origin reddedilir', async () => {
  const t = await start();
  try {
    await channel(t.api);
    await notify(t.api);
    // tarayıcı formu: preflight gerektirmeyen "basit" istek
    for (const ct of ['text/plain', 'application/x-www-form-urlencoded', 'multipart/form-data; boundary=x']) {
      const r = await raw({ port: t.port, method: 'POST', path: '/api/shade/clear-all', headers: { 'content-type': ct, host: `127.0.0.1:${t.port}` }, body: 'a=b' });
      assert.equal(r.status, 415, ct);
    }
    const noCt = await raw({ port: t.port, method: 'POST', path: '/api/shade/clear-all', headers: { host: `127.0.0.1:${t.port}` } });
    assert.equal(noCt.status, 415);
    const foreign = await raw({ port: t.port, method: 'POST', path: '/api/shade/clear-all', headers: { 'content-type': 'application/json', origin: 'https://evil.example', host: `127.0.0.1:${t.port}` }, body: '{}' });
    assert.equal(foreign.status, 403);
    const same = await raw({ port: t.port, method: 'POST', path: '/api/shade/clear-all', headers: { 'content-type': 'application/json', origin: `http://127.0.0.1:${t.port}`, host: `127.0.0.1:${t.port}` }, body: '{}' });
    assert.equal(same.status, 200);
  } finally {
    await t.close();
  }
});

test('DNS rebinding: token yokken yerel olmayan Host reddedilir', async () => {
  const t = await start();
  try {
    const r = await raw({ port: t.port, path: '/api/shade', headers: { host: 'evil.example:80' } });
    assert.equal(r.status, 403);
    assert.equal(JSON.parse(r.body).error.code, 'forbidden_host');
    assert.equal((await raw({ port: t.port, path: '/api/shade', headers: { host: `localhost:${t.port}` } })).status, 200);
  } finally {
    await t.close();
  }
});

test('token: başlıksız 401; ?token= yalnızca SSE/indirme uçlarında', async () => {
  const t = await start({ token: 's3cret-token' });
  try {
    assert.equal((await fetch(`${t.base}/api/shade`)).status, 401);
    assert.equal((await fetch(`${t.base}/api/shade`, { headers: { authorization: 'Bearer yanlis' } })).status, 401);
    assert.equal((await t.api('GET', '/api/shade')).status, 200);
    assert.equal((await fetch(`${t.base}/api/shade?token=s3cret-token`)).status, 401); // sorgu dizesi bu uçta kabul edilmez
    assert.equal((await fetch(`${t.base}/api/export.csv?token=s3cret-token`)).status, 200);
    const sse = await fetch(`${t.base}/api/events?token=s3cret-token`);
    assert.equal(sse.status, 200);
    await sse.body.cancel();
    // token varken Host denetimi gerekmez (ters vekil arkasında kullanım)
    assert.equal((await raw({ port: t.port, path: '/api/shade', headers: { host: 'notivo.example', authorization: 'Bearer s3cret-token' } })).status, 200);
  } finally {
    await t.close();
  }
});

test('gövde sınırı ve bozuk JSON', async () => {
  const t = await start();
  try {
    const big = await raw({ port: t.port, method: 'POST', path: '/api/notify', headers: { 'content-type': 'application/json', host: `127.0.0.1:${t.port}` }, body: JSON.stringify({ title: 'x'.repeat(300_000) }) });
    assert.equal(big.status, 413);
    const badJson = await raw({ port: t.port, method: 'POST', path: '/api/notify', headers: { 'content-type': 'application/json', host: `127.0.0.1:${t.port}` }, body: '{bozuk' });
    assert.equal(badJson.status, 400);
    assert.equal(JSON.parse(badJson.body).error.code, 'invalid_json');
  } finally {
    await t.close();
  }
});

test('statik dosyalar: güvenlik başlıkları, yol geçişi engeli', async () => {
  const t = await start();
  try {
    const home = await raw({ port: t.port, path: '/', headers: { host: `127.0.0.1:${t.port}` } });
    assert.equal(home.status, 200);
    assert.match(home.headers['content-security-policy'], /default-src 'self'/);
    assert.equal(home.headers['x-content-type-options'], 'nosniff');
    for (const p of ['/..%2f..%2fpackage.json', '/../../../etc/passwd', '/%2e%2e/%2e%2e/etc/passwd', '/..%5c..%5cetc/passwd']) {
      const r = await raw({ port: t.port, path: p, headers: { host: `127.0.0.1:${t.port}` } });
      assert.ok([400, 403, 404].includes(r.status), `${p} -> ${r.status}`);
      assert.doesNotMatch(r.body, /root:|"name"/);
    }
    assert.equal((await raw({ port: t.port, path: '/a%00b', headers: { host: `127.0.0.1:${t.port}` } })).status, 400);
    assert.equal((await raw({ port: t.port, method: 'POST', path: '/', headers: { host: `127.0.0.1:${t.port}` } })).status, 405);
  } finally {
    await t.close();
  }
});

/** SSE okuyucu: olayları sırayla toplar. */
async function sse(base, path = '/api/events') {
  const res = await fetch(base + path);
  const reader = res.body.getReader();
  const dec = new TextDecoder();
  let buf = '';
  const events = [];
  (async () => {
    try {
      for (;;) {
        const { value, done } = await reader.read();
        if (done) break;
        buf += dec.decode(value, { stream: true });
        let i;
        while ((i = buf.indexOf('\n\n')) >= 0) {
          const chunk = buf.slice(0, i);
          buf = buf.slice(i + 2);
          const line = chunk.split('\n').find((l) => l.startsWith('data: '));
          if (line) events.push(JSON.parse(line.slice(6)));
        }
      }
    } catch {
      /* iptal */
    }
  })();
  const waitFor = async (pred, ms = 2000) => {
    const end = Date.now() + ms;
    while (Date.now() < end) {
      const e = events.find(pred);
      if (e) return e;
      await new Promise((r) => setTimeout(r, 10));
    }
    assert.fail(`olay gelmedi: ${JSON.stringify(events)}`);
  };
  return { events, waitFor, close: () => reader.cancel() };
}

test('SSE: yayın, heads-up olayı ve ?pkg= süzgeci (uygulama geri çağrıları)', async () => {
  const t = await start();
  try {
    await channel(t.api, 'com.chat', 4);
    await channel(t.api, 'com.mail', 3);
    const all = await sse(t.base);
    const mine = await sse(t.base, '/api/events?pkg=com.chat');
    await notify(t.api, { pkg: 'com.mail', channel: 'msg', title: 'Posta', text: 'ileti' });
    await notify(t.api, { text: 'merhaba' });
    const alert = await all.waitFor((e) => e.type === 'alert' && e.pkg === 'com.chat');
    assert.equal(alert.headsUp, true);
    assert.equal(alert.notification.text, 'merhaba');
    await all.waitFor((e) => e.type === 'posted' && e.pkg === 'com.mail');
    // kullanıcı kapatınca uygulama deleteIntent benzeri 'dismissed' alır
    await t.api('POST', '/api/shade/dismiss', { key: 'com.chat|1|' });
    await mine.waitFor((e) => e.type === 'dismissed');
    assert.ok(mine.events.every((e) => e.pkg === 'com.chat'), 'süzgeç başka uygulamanın olayını sızdırdı');
    await all.close();
    await mine.close();
  } finally {
    await t.close();
  }
});

test('DND, kurallar, engelleme uçları', async () => {
  const t = await start();
  try {
    await channel(t.api);
    assert.equal((await t.api('PUT', '/api/dnd', { enabled: true })).json.enabled, true);
    assert.equal((await notify(t.api)).json.alerted.suppressedByDnd, true);
    const rule = (await t.api('POST', '/api/rules', { keyword: 'reklam', action: 'BLOCK' })).json.rule;
    assert.equal((await notify(t.api, { id: 2, title: 'REKLAM', text: 'x' })).json.status, 'blocked_by_rule');
    assert.equal((await t.api('PATCH', `/api/rules/${rule.id}`, { enabled: false })).json.enabled, false);
    assert.equal((await t.api('DELETE', `/api/rules/${rule.id}`, {})).status, 200);
    assert.equal((await t.api('DELETE', `/api/rules/${rule.id}`, {})).status, 404);
    assert.equal((await t.api('PATCH', '/api/apps/com.chat', { blocked: true })).json.blocked, true);
    assert.equal((await notify(t.api, { id: 3 })).json.status, 'blocked');
    assert.equal((await t.api('PATCH', '/api/apps/com.chat/channels/msg', { importance: 2 })).json.channel.importance, 2);
  } finally {
    await t.close();
  }
});

test('dışa aktarma: CSV formül enjeksiyonuna karşı güvenli; yedek → geri yükleme', async () => {
  const a = await start();
  const b = await start();
  try {
    await channel(a.api);
    await notify(a.api, { title: '=HYPERLINK("http://kotu")', text: 'x' });
    const csv = await (await fetch(`${a.base}/api/export.csv`)).text();
    assert.ok(csv.includes(`"'=HYPERLINK(""http://kotu"")"`), csv);
    const backup = await (await fetch(`${a.base}/api/backup`)).json();
    assert.equal(backup.items.length, 1);
    assert.equal((await b.api('POST', '/api/restore', backup)).json.added, 1);
    assert.equal((await b.api('POST', '/api/restore', backup)).json.added, 0);
    assert.equal((await b.api('POST', '/api/restore', { app: 'baska' })).status, 400);
  } finally {
    await a.close();
    await b.close();
  }
});

test('kalıcılık: sunucu yeniden başlayınca bildirimler, kanallar, kurallar ve DND yerinde durur', async () => {
  const dir = mkdtempSync(join(tmpdir(), 'nv-db-'));
  const dbPath = join(dir, 'notivo.db');
  const first = await start({ dbPath });
  await channel(first.api);
  await notify(first.api, { text: 'yeniden başlatmadan önce' });
  await first.api('POST', '/api/rules', { keyword: 'reklam', action: 'BLOCK' });
  await first.api('PUT', '/api/dnd', { enabled: true });
  await first.api('POST', '/api/shade/snooze', { key: 'com.chat|1|', minutes: 30 });
  await first.close();

  const second = await start({ dbPath });
  try {
    assert.equal((await second.api('GET', '/api/shade')).json.snoozed, 1);
    assert.equal((await second.api('GET', '/api/shade')).json.dnd.enabled, true);
    assert.equal((await second.api('GET', '/api/rules')).json.rules[0].keyword, 'reklam');
    assert.equal((await second.api('GET', '/api/apps')).json.apps[0].channels[0].id, 'msg');
    assert.equal((await second.api('GET', '/api/history?q=yeniden')).json.items.length, 1);
  } finally {
    await second.close();
  }
});
