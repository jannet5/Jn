// HTTP + SSE taşıması. Kart sürümünde bu dosyanın yerine card/transport.js yayınlanır (aynı dışa aktarımlar).
export const mode = 'http';
export const firstRun = false;

const token = () => localStorage.getItem('notivoToken') ?? '';
export const hasToken = () => !!token();
export const setToken = (t) => localStorage.setItem('notivoToken', t);

export async function api(method, path, body) {
  const headers = { 'content-type': 'application/json' };
  if (token()) headers.authorization = `Bearer ${token()}`;
  const res = await fetch(path, { method, headers, body: method === 'GET' ? undefined : JSON.stringify(body ?? {}) });
  if (res.status === 401) {
    window.dispatchEvent(new Event('notivo:unauthorized'));
    throw Object.assign(new Error('Erişim anahtarı gerekli'), { status: 401, code: 'unauthorized' });
  }
  const json = await res.json().catch(() => null);
  if (!res.ok) throw Object.assign(new Error(json?.error?.message ?? res.statusText), { status: res.status, code: json?.error?.code });
  return json;
}

/** Canlı olaylar (SSE). EventSource kopunca kendiliğinden yeniden bağlanır. */
export function connect({ onEvent, onOpen, onError }) {
  const es = new EventSource(`/api/events${token() ? `?token=${encodeURIComponent(token())}` : ''}`);
  es.onopen = () => onOpen?.();
  es.onerror = () => onError?.();
  es.onmessage = (m) => {
    try { onEvent(JSON.parse(m.data)); } catch { /* bozuk olay */ }
  };
}

/** İndirme bağlantısı (tarayıcı kendisi indirir). */
export const downloadHref = (path) => path + (token() ? `?token=${encodeURIComponent(token())}` : '');
/** Yalnızca sayfa içi (kart) sürümünde kullanılır. */
export async function download() {
  throw new Error('Bu sürümde indirme bağlantısı kullanılır');
}
