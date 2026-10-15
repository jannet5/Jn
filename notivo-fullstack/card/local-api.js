// Sunucusuz kart sürümü için API yönlendirici: server/app.js'teki uç noktaların aynısını doğrudan Shade'e bağlar.
// (HTTP'ye özgü kısımlar — SSE, indirme, statik dosya, CSRF/token — burada yoktur.)
import { ApiError } from '../server/shade.js';

export function createLocalApi(shade) {
  const routes = [];
  const route = (method, pattern, fn) => routes.push({ method, re: new RegExp(`^${pattern}$`), fn });
  const ok = (body = {}) => ({ ok: true, ...body });
  const dec = decodeURIComponent;

  route('GET', '/api/health', () => ok({ now: shade.now() }));
  route('GET', '/api/shade', () => shade.shade());
  route('GET', '/api/history', ({ q }) => ({
    items: shade.history({ q: q.get('q') ?? '', pkg: q.get('pkg') ?? '', state: q.get('state') ?? 'all', limit: q.has('limit') ? Number(q.get('limit')) : 200 }),
  }));
  route('GET', '/api/apps', () => ({ apps: shade.listApps() }));
  route('POST', '/api/apps/([^/]+)/channels', ({ m, body }) => ok({ channel: shade.createChannel(dec(m[1]), body) }));
  route('PATCH', '/api/apps/([^/]+)/channels/([^/]+)', ({ m, body }) => ok({ channel: shade.setChannelImportance(dec(m[1]), dec(m[2]), body.importance) }));
  route('PATCH', '/api/apps/([^/]+)', ({ m, body }) => ok(shade.setAppBlocked(dec(m[1]), body.blocked)));

  route('POST', '/api/notify', ({ body }) => ok(shade.post(body)));
  route('POST', '/api/cancel', ({ body }) => ok(shade.cancel(body)));
  route('POST', '/api/cancel-all', ({ body }) => ok(shade.cancelAll(body)));

  route('POST', '/api/shade/dismiss', ({ body }) => ok(shade.dismiss(body.key)));
  route('POST', '/api/shade/dismiss-group', ({ body }) => ok(shade.dismissGroup(body)));
  route('POST', '/api/shade/clear-all', ({ body }) => ok(shade.clearAll(body)));
  route('POST', '/api/shade/click', ({ body }) => ok(shade.click(body.key)));
  route('POST', '/api/shade/action', ({ body }) => ok(shade.action(body.key, body.actionId, body.text)));
  route('POST', '/api/shade/snooze', ({ body }) => ok(shade.snooze(body.key, body.minutes)));

  route('GET', '/api/dnd', () => shade.getDnd());
  route('PUT', '/api/dnd', ({ body }) => shade.setDnd(body));

  route('GET', '/api/rules', () => ({ rules: shade.listRules() }));
  route('POST', '/api/rules', ({ body }) => ok({ rule: shade.addRule(body) }));
  route('PATCH', '/api/rules/(\\d+)', ({ m, body }) => ok(shade.setRuleEnabled(Number(m[1]), body.enabled)));
  route('DELETE', '/api/rules/(\\d+)', ({ m }) => ok(shade.deleteRule(Number(m[1]))));

  route('POST', '/api/restore', ({ body }) => ok({ added: shade.restore(body) }));

  /** HTTP sunucusuyla aynı sözleşme: başarıda JSON döner, hatada ApiError fırlatır. */
  return async function call(method, path, body = {}) {
    const url = new URL(path, 'http://local');
    let pathMatched = false;
    for (const r of routes) {
      const m = r.re.exec(url.pathname);
      if (!m) continue;
      pathMatched = true;
      if (r.method === method) return r.fn({ m, q: url.searchParams, body: body ?? {} });
    }
    throw new ApiError(pathMatched ? 405 : 404, pathMatched ? 'method_not_allowed' : 'not_found', pathMatched ? 'Yöntem desteklenmiyor' : 'Uç nokta bulunamadı');
  };
}
