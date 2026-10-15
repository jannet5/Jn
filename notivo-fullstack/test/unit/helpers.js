import { openDb } from '../../server/db.js';
import { Shade } from '../../server/shade.js';

/** Sahte saatli, bellek içi bir Shade. */
export function mk(start = 1_700_000_000_000) {
  const clock = { t: start };
  const events = [];
  const shade = new Shade({ db: openDb(':memory:'), now: () => clock.t, emit: (e) => events.push(e) });
  return { shade, clock, events, advance: (ms) => (clock.t += ms), types: () => events.map((e) => e.type) };
}

export const chan = (shade, pkg, id, importance, extra = {}) =>
  shade.createChannel(pkg, { id, name: extra.name ?? id, importance, appName: extra.appName ?? pkg });

export const post = (shade, o) => shade.post({ pkg: 'com.chat', channel: 'msg', id: 1, title: 'Ayşe', text: 'Merhaba', ...o });

/** Bildirim çubuğundaki tüm tekil bildirim başlıkları (gruplar açılmış halde). */
export function titles(shade) {
  return shade.shade().sections.flatMap((s) => s.entries.flatMap((e) => (e.type === 'group' ? e.children : [e.n]))).map((n) => n.text || n.title);
}
