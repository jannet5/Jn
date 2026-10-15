import test from 'node:test';
import assert from 'node:assert/strict';
import initSqlJs from 'sql.js';
import { createApp } from '../../server/app.js';
import { Shade, ApiError } from '../../server/shade.js';
import { openSqlJsDb } from '../../card/sqljs-shim.js';
import { createLocalApi } from '../../card/local-api.js';

// Kart sürümü (sayfa içi sql.js + yerel yönlendirici) ile gerçek HTTP sunucusu AYNI isteklere AYNI yanıtı vermeli.
test('kart sürümü ve HTTP sunucusu aynı istek dizisine birebir aynı yanıtı verir', async () => {
  const SQL = await initSqlJs();
  const clockA = { t: 1_700_000_000_000 };
  const clockB = { t: 1_700_000_000_000 };
  const http = createApp({ now: () => clockA.t, tickMs: 0 });
  await new Promise((r) => http.server.listen(0, '127.0.0.1', r));
  const base = `http://127.0.0.1:${http.server.address().port}`;
  const localShade = new Shade({ db: openSqlJsDb(SQL), now: () => clockB.t });
  const local = createLocalApi(localShade);

  const viaHttp = async (method, path, body) => {
    const res = await fetch(base + path, { method, headers: { 'content-type': 'application/json' }, body: method === 'GET' ? undefined : JSON.stringify(body ?? {}) });
    return { status: res.status, json: await res.json() };
  };
  const viaLocal = async (method, path, body) => {
    try {
      return { status: 200, json: JSON.parse(JSON.stringify(await local(method, path, body ?? {}))) };
    } catch (e) {
      if (!(e instanceof ApiError)) throw e;
      return { status: e.status, json: { error: { code: e.code, message: e.message } } };
    }
  };
  let n = 0;
  const both = async (method, path, body) => {
    const [a, b] = [await viaHttp(method, path, body), await viaLocal(method, path, body)];
    assert.deepEqual(b, a, `#${++n} ${method} ${path} yanıtları ayrıştı`);
    return a;
  };
  const advance = (ms) => { clockA.t += ms; clockB.t += ms; };

  try {
    const ch = (pkg, id, importance, appName) => both('POST', `/api/apps/${pkg}/channels`, { id, name: id, importance, appName });
    await ch('com.whatsapp', 'msg', 4, 'WhatsApp');
    await ch('com.gm', 'mail', 3, 'Gmail');
    await ch('com.music', 'play', 2, 'Müzik');
    await ch('com.insta', 'n', 3, 'Instagram');
    await both('POST', '/api/apps/com.whatsapp/channels', { id: 'msg', name: 'Yeni', importance: 0 }); // önem kilitli

    await both('GET', '/api/health');
    await both('POST', '/api/notify', { pkg: 'com.whatsapp', channel: 'msg', id: 1, title: 'Ayşe', text: 'Merhaba', conversation: true, contentIntent: 'wa://ayse', actions: [{ id: 'reply', title: 'Yanıtla', reply: true }] });
    advance(2000);
    await both('POST', '/api/notify', { pkg: 'com.whatsapp', channel: 'msg', id: 1, title: 'Ayşe', text: 'İkinci mesaj', conversation: true });
    await both('POST', '/api/notify', { pkg: 'com.whatsapp', channel: 'msg', id: 1, title: 'Ayşe', text: 'This message was deleted' });
    await both('POST', '/api/notify', { pkg: 'com.gm', channel: 'mail', id: 10, group: 'inbox', title: 'Banka', text: 'Ekstre' });
    await both('POST', '/api/notify', { pkg: 'com.gm', channel: 'mail', id: 11, group: 'inbox', title: 'Kargo', text: 'Yolda' });
    await both('POST', '/api/notify', { pkg: 'com.gm', channel: 'mail', id: 1, group: 'inbox', groupSummary: true, title: 'Gelen kutusu', text: '2 yeni' });
    await both('POST', '/api/notify', { pkg: 'com.music', channel: 'play', id: 1, title: 'Şarkı', text: 'x', ongoing: true, timeoutAfterMs: 5000 });
    for (let i = 1; i <= 4; i++) await both('POST', '/api/notify', { pkg: 'com.insta', channel: 'n', id: i, title: `k${i}`, text: `t${i}` });
    await both('POST', '/api/notify', { pkg: 'com.insta', channel: 'n', id: 4, title: 'k4', text: 't4', onlyAlertOnce: true });
    // hatalar
    await both('POST', '/api/notify', { pkg: 'com.x', channel: 'yok', title: 't' });
    await both('POST', '/api/notify', { pkg: 'bad pkg', channel: 'c', title: 't' });
    await both('POST', '/api/notify', { pkg: 'com.gm', channel: 'mail', title: ' ', text: '' });
    await both('POST', '/api/shade/dismiss', { key: 'com.music|1|' }); // 409 süregelen
    await both('POST', '/api/shade/dismiss', { key: 'yok|1|' });
    await both('GET', '/api/yok');
    await both('DELETE', '/api/shade');

    await both('GET', '/api/shade');
    await both('GET', '/api/apps');
    await both('GET', '/api/history?q=ayşe');
    await both('GET', '/api/history?state=bad');

    await both('POST', '/api/rules', { keyword: 'reklam', action: 'BLOCK' });
    await both('POST', '/api/rules', { keyword: '', pkg: '', action: 'BLOCK' });
    await both('POST', '/api/notify', { pkg: 'com.gm', channel: 'mail', id: 50, title: 'REKLAM', text: 'x' });
    await both('PATCH', '/api/rules/1', { enabled: false });
    await both('GET', '/api/rules');
    await both('DELETE', '/api/rules/1');
    await both('DELETE', '/api/rules/1');

    await both('PUT', '/api/dnd', { enabled: true });
    await both('POST', '/api/notify', { pkg: 'com.whatsapp', channel: 'msg', id: 2, title: 'Zeynep', text: 'DND', conversation: true });
    await both('GET', '/api/dnd');
    await both('PUT', '/api/dnd', { enabled: false });

    await both('POST', '/api/shade/action', { key: 'com.whatsapp|1|', actionId: 'reply', text: 'tamam' });
    await both('POST', '/api/shade/action', { key: 'com.whatsapp|1|', actionId: 'reply', text: ' ' });
    await both('POST', '/api/shade/snooze', { key: 'com.whatsapp|2|', minutes: 15 });
    await both('POST', '/api/shade/snooze', { key: 'com.whatsapp|2|', minutes: 0 });
    advance(16 * 60_000);
    assert.deepEqual(localShade.tick(), http.shade.tick()); // erteleme bitti, zaman aşımı
    await both('GET', '/api/shade');
    await both('POST', '/api/shade/click', { key: 'com.whatsapp|1|' });
    await both('POST', '/api/shade/dismiss-group', { pkg: 'com.insta', group: '', section: 'alerting' });
    await both('PATCH', '/api/apps/com.gm/channels/mail', { importance: 2 });
    await both('PATCH', '/api/apps/com.gm', { blocked: true });
    await both('POST', '/api/cancel', { pkg: 'com.music', id: 1 });
    await both('POST', '/api/cancel-all', { pkg: 'com.whatsapp' });
    await both('POST', '/api/shade/clear-all', {});
    await both('POST', '/api/shade/clear-all', { section: 'yok' });
    await both('GET', '/api/history?limit=500');

    const backup = await (await fetch(`${base}/api/backup`)).json();
    await both('POST', '/api/restore', backup);
    await both('POST', '/api/restore', { app: 'baska' });
    await both('GET', '/api/history');
    assert.ok(n >= 55, `yeterli adım çalışmadı: ${n}`);
  } finally {
    await http.close();
  }
});
