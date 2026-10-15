import http from 'node:http';
import { createHash, timingSafeEqual } from 'node:crypto';
import { readFile, stat } from 'node:fs/promises';
import { join, normalize, extname, sep } from 'node:path';
import { openDb } from './db.js';
import { Shade, ApiError } from './shade.js';

const MAX_BODY = 256 * 1024;
const MAX_SSE_CLIENTS = 100;
const LOOPBACK = new Set(['localhost', '127.0.0.1', '[::1]', '::1']);
const MIME = {
  '.html': 'text/html; charset=utf-8',
  '.js': 'text/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.svg': 'image/svg+xml',
  '.json': 'application/json; charset=utf-8',
  '.ico': 'image/x-icon',
};
const CSP = "default-src 'self'; img-src 'self' data:; style-src 'self'; script-src 'self'; connect-src 'self'; frame-ancestors 'none'; base-uri 'none'; form-action 'self'";

const digest = (s) => createHash('sha256').update(String(s)).digest();
function tokenOk(given, token) {
  return typeof given === 'string' && timingSafeEqual(digest(given), digest(token));
}

function sendJson(res, status, body, extra = {}) {
  const data = JSON.stringify(body);
  res.writeHead(status, {
    'content-type': 'application/json; charset=utf-8',
    'cache-control': 'no-store',
    'x-content-type-options': 'nosniff',
    ...extra,
  });
  res.end(data);
}

async function readJson(req) {
  const ct = (req.headers['content-type'] ?? '').toLowerCase();
  // Tarayıcının preflight'sız "basit" çapraz-site isteklerini (form POST) reddetmek için JSON zorunlu.
  if (!ct.startsWith('application/json')) throw new ApiError(415, 'unsupported_media_type', 'Content-Type: application/json gerekli');
  const chunks = [];
  let size = 0;
  for await (const c of req) {
    size += c.length;
    if (size > MAX_BODY) throw new ApiError(413, 'payload_too_large', `Gövde ${MAX_BODY} bayttan büyük olamaz`);
    chunks.push(c);
  }
  if (size === 0) return {};
  try {
    return JSON.parse(Buffer.concat(chunks).toString('utf8'));
  } catch {
    throw new ApiError(400, 'invalid_json', 'Geçersiz JSON');
  }
}

/**
 * Sunucuyu kurar (dinlemeye başlatmaz).
 * @param {{dbPath?: string, token?: string, now?: () => number, webRoot?: string, tickMs?: number}} o
 */
export function createApp({ dbPath = ':memory:', token = '', now = Date.now, webRoot, tickMs = 1000 } = {}) {
  const db = openDb(dbPath);
  const clients = new Set();

  function broadcast(e) {
    const line = `data: ${JSON.stringify(e)}\n\n`;
    for (const c of clients) {
      if (c.pkg && e.pkg !== c.pkg) continue;
      // Okumayan (yavaş/donmuş) istemci belleği şişirmesin: 1 MB'tan fazla birikeni koparırız; EventSource kendiliğinden yeniden bağlanır.
      if (c.res.writableLength > 1_000_000) {
        c.res.destroy();
        clients.delete(c);
        continue;
      }
      try {
        c.res.write(line);
      } catch {
        clients.delete(c);
      }
    }
  }
  const shade = new Shade({ db, now, emit: broadcast });

  function checkAccess(req, url) {
    const host = (req.headers.host ?? '').replace(/:\d+$/, '').toLowerCase();
    if (token) {
      const auth = req.headers.authorization ?? '';
      const bearer = auth.startsWith('Bearer ') ? auth.slice(7) : '';
      // Tarayıcı gezintileri (SSE, indirme) başlık ekleyemez: yalnızca GET ve yalnızca bu uçlarda ?token= kabul edilir.
      const viaQuery = req.method === 'GET' && ['/api/events', '/api/export.csv', '/api/backup'].includes(url.pathname) ? url.searchParams.get('token') : null;
      if (!tokenOk(bearer, token) && !tokenOk(viaQuery, token)) throw new ApiError(401, 'unauthorized', 'Geçersiz ya da eksik token');
    } else if (!LOOPBACK.has(host)) {
      // Token yokken yalnızca yerel adresler (DNS rebinding savunması).
      throw new ApiError(403, 'forbidden_host', 'Token tanımlı değilse yalnızca localhost üzerinden erişilebilir');
    }
    if (req.method !== 'GET' && req.method !== 'HEAD') {
      const origin = req.headers.origin;
      if (origin) {
        let oh = '';
        try {
          oh = new URL(origin).host;
        } catch {
          /* geçersiz Origin */
        }
        if (oh !== req.headers.host) throw new ApiError(403, 'forbidden_origin', 'Farklı kaynaktan gelen istek reddedildi');
      }
    }
  }

  // ---- yönlendirme tablosu
  const routes = [];
  const route = (method, pattern, fn) => routes.push({ method, re: new RegExp(`^${pattern}$`), fn });
  const ok = (res, body = {}) => sendJson(res, 200, { ok: true, ...body });
  const dec = decodeURIComponent;

  route('GET', '/api/health', (req, res) => ok(res, { now: now() }));
  route('GET', '/api/shade', (req, res) => sendJson(res, 200, shade.shade()));
  route('GET', '/api/history', (req, res, { url }) => {
    const q = url.searchParams;
    sendJson(res, 200, {
      items: shade.history({ q: q.get('q') ?? '', pkg: q.get('pkg') ?? '', state: q.get('state') ?? 'all', limit: q.has('limit') ? Number(q.get('limit')) : 200 }),
    });
  });
  route('GET', '/api/apps', (req, res) => sendJson(res, 200, { apps: shade.listApps() }));
  route('POST', '/api/apps/([^/]+)/channels', async (req, res, { m }) => ok(res, { channel: shade.createChannel(dec(m[1]), await readJson(req)) }));
  route('PATCH', '/api/apps/([^/]+)/channels/([^/]+)', async (req, res, { m }) => {
    const body = await readJson(req);
    ok(res, { channel: shade.setChannelImportance(dec(m[1]), dec(m[2]), body.importance) });
  });
  route('PATCH', '/api/apps/([^/]+)', async (req, res, { m }) => ok(res, shade.setAppBlocked(dec(m[1]), (await readJson(req)).blocked)));

  route('POST', '/api/notify', async (req, res) => ok(res, shade.post(await readJson(req))));
  route('POST', '/api/cancel', async (req, res) => ok(res, shade.cancel(await readJson(req))));
  route('POST', '/api/cancel-all', async (req, res) => ok(res, shade.cancelAll(await readJson(req))));

  route('POST', '/api/shade/dismiss', async (req, res) => ok(res, shade.dismiss((await readJson(req)).key)));
  route('POST', '/api/shade/dismiss-group', async (req, res) => ok(res, shade.dismissGroup(await readJson(req))));
  route('POST', '/api/shade/clear-all', async (req, res) => ok(res, shade.clearAll(await readJson(req))));
  route('POST', '/api/shade/click', async (req, res) => ok(res, shade.click((await readJson(req)).key)));
  route('POST', '/api/shade/action', async (req, res) => {
    const b = await readJson(req);
    ok(res, shade.action(b.key, b.actionId, b.text));
  });
  route('POST', '/api/shade/snooze', async (req, res) => {
    const b = await readJson(req);
    ok(res, shade.snooze(b.key, b.minutes));
  });

  route('GET', '/api/dnd', (req, res) => sendJson(res, 200, shade.getDnd()));
  route('PUT', '/api/dnd', async (req, res) => sendJson(res, 200, shade.setDnd(await readJson(req))));

  route('GET', '/api/rules', (req, res) => sendJson(res, 200, { rules: shade.listRules() }));
  route('POST', '/api/rules', async (req, res) => ok(res, { rule: shade.addRule(await readJson(req)) }));
  route('PATCH', '/api/rules/(\\d+)', async (req, res, { m }) => ok(res, shade.setRuleEnabled(Number(m[1]), (await readJson(req)).enabled)));
  route('DELETE', '/api/rules/(\\d+)', async (req, res, { m }) => {
    await readJson(req); // JSON zorunluluğu tüm değiştirici isteklerde aynı
    ok(res, shade.deleteRule(Number(m[1])));
  });

  route('GET', '/api/export.csv', (req, res) => {
    res.writeHead(200, {
      'content-type': 'text/csv; charset=utf-8',
      'content-disposition': 'attachment; filename="notivo.csv"',
      'cache-control': 'no-store',
      'x-content-type-options': 'nosniff',
    });
    res.end(shade.exportCsv());
  });
  route('GET', '/api/backup', (req, res) => {
    res.writeHead(200, {
      'content-type': 'application/json; charset=utf-8',
      'content-disposition': 'attachment; filename="notivo-yedek.json"',
      'cache-control': 'no-store',
      'x-content-type-options': 'nosniff',
    });
    res.end(JSON.stringify(shade.backup()));
  });
  route('POST', '/api/restore', async (req, res) => ok(res, { added: shade.restore(await readJson(req)) }));

  route('GET', '/api/events', (req, res, { url }) => {
    if (clients.size >= MAX_SSE_CLIENTS) throw new ApiError(503, 'too_many_clients', 'Çok fazla canlı bağlantı');
    const pkg = url.searchParams.get('pkg') ?? '';
    res.writeHead(200, { 'content-type': 'text/event-stream', 'cache-control': 'no-store', connection: 'keep-alive', 'x-accel-buffering': 'no' });
    res.write('retry: 2000\n\n');
    const c = { res, pkg };
    clients.add(c);
    const ka = setInterval(() => {
      try {
        res.write(':\n\n');
      } catch {
        /* kapanan bağlantı close olayında temizlenir */
      }
    }, 25_000);
    req.on('close', () => {
      clearInterval(ka);
      clients.delete(c);
    });
  });

  async function api(req, res, url) {
    checkAccess(req, url);
    if (url.pathname === '/api/health') return ok(res, { now: now() });
    for (const r of routes) {
      if (r.method !== req.method) continue;
      const m = r.re.exec(url.pathname);
      if (m) return r.fn(req, res, { url, m });
    }
    const known = routes.some((r) => r.re.test(url.pathname));
    throw new ApiError(known ? 405 : 404, known ? 'method_not_allowed' : 'not_found', known ? 'Yöntem desteklenmiyor' : 'Uç nokta bulunamadı');
  }

  async function serveStatic(req, res, pathname) {
    if (req.method !== 'GET' && req.method !== 'HEAD') throw new ApiError(405, 'method_not_allowed', 'Yöntem desteklenmiyor');
    if (!webRoot) throw new ApiError(404, 'not_found', 'Bulunamadı');
    let rel;
    try {
      rel = decodeURIComponent(pathname);
    } catch {
      throw new ApiError(400, 'bad_path', 'Geçersiz yol');
    }
    if (rel.includes('\0')) throw new ApiError(400, 'bad_path', 'Geçersiz yol');
    if (rel.endsWith('/')) rel += 'index.html';
    const file = normalize(join(webRoot, rel));
    if (file !== webRoot && !file.startsWith(webRoot + sep)) throw new ApiError(403, 'forbidden', 'Yasak');
    let st;
    try {
      st = await stat(file);
    } catch {
      throw new ApiError(404, 'not_found', 'Bulunamadı');
    }
    if (!st.isFile()) throw new ApiError(404, 'not_found', 'Bulunamadı');
    const type = MIME[extname(file)] ?? 'application/octet-stream';
    res.writeHead(200, {
      'content-type': type,
      'cache-control': 'no-cache',
      'x-content-type-options': 'nosniff',
      'content-security-policy': CSP,
      'referrer-policy': 'no-referrer',
    });
    if (req.method === 'HEAD') return res.end();
    res.end(await readFile(file));
  }

  const server = http.createServer(async (req, res) => {
    try {
      const url = new URL(req.url, 'http://localhost');
      if (url.pathname.startsWith('/api/')) await api(req, res, url);
      else await serveStatic(req, res, url.pathname);
    } catch (e) {
      if (res.headersSent) return res.end();
      if (e instanceof ApiError) return sendJson(res, e.status, { error: { code: e.code, message: e.message } });
      console.error('beklenmeyen hata:', e);
      sendJson(res, 500, { error: { code: 'internal', message: 'Sunucu hatası' } });
    }
  });

  const timer =
    tickMs > 0
      ? setInterval(() => {
          try {
            shade.tick();
          } catch (e) {
            console.error('tick hatası:', e);
          }
        }, tickMs)
      : null;
  timer?.unref();

  return {
    server,
    shade,
    db,
    clients,
    close() {
      if (timer) clearInterval(timer);
      for (const c of clients) c.res.end();
      clients.clear();
      server.closeAllConnections?.();
      return new Promise((resolve) => server.close(() => (db.close(), resolve())));
    },
  };
}
