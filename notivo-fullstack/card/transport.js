// Kart sürümü taşıması: sunucu yok. Çekirdek (Shade) ve SQLite (sql.js) bu sayfanın içinde çalışır;
// veri bu cihazın localStorage'ında kalır. web/transport.js ile aynı dışa aktarımlar.
import { Shade } from '../server/shade.js';
import { ApiError } from '../server/shade.js';
import { openSqlJsDb } from './sqljs-shim.js';
import { createLocalApi } from './local-api.js';

export const mode = 'local';
export const hasToken = () => false;
export const setToken = () => {};
export const downloadHref = () => '#';
/** Bu cihazda kayıtlı veri yoksa true: ilk açılışta örnek bildirimler gösterilir. */
export let firstRun = false;

const STORE_KEY = 'notivo-card-db-v1';
const listeners = new Set();
let shade;
let db;
let call;

function fail(message) {
  const p = document.createElement('p');
  p.style.cssText = 'padding:24px;font:16px system-ui;';
  p.textContent = message;
  document.body.replaceChildren(p);
}

function loadSaved() {
  try {
    const b64 = localStorage.getItem(STORE_KEY);
    if (b64) return Uint8Array.from(atob(b64), (c) => c.charCodeAt(0));
  } catch {
    /* depolama yok ya da bozuk: boş başla */
  }
  return null;
}

let saveTimer;
function save() {
  try {
    const bytes = db.export();
    let bin = '';
    for (let i = 0; i < bytes.length; i += 0x8000) bin += String.fromCharCode(...bytes.subarray(i, i + 0x8000));
    localStorage.setItem(STORE_KEY, btoa(bin));
  } catch {
    /* kota dolu ya da depolama kapalı: oturum boyunca çalışır, kalıcı olmaz */
  }
}
const scheduleSave = () => {
  clearTimeout(saveTimer);
  saveTimer = setTimeout(save, 400);
};

try {
  const SQL = await window.initSqlJs();
  const saved = loadSaved();
  firstRun = !saved && !localStorage.getItem('notivo-no-seed');
  db = openSqlJsDb(SQL, saved);
  shade = new Shade({ db, now: Date.now, emit: (e) => listeners.forEach((fn) => fn(e)) });
  db.onWrite = scheduleSave;
  call = createLocalApi(shade);
  setInterval(() => {
    try { shade.tick(); } catch (e) { console.error('tick hatası:', e); }
  }, 1000);
  window.addEventListener('pagehide', save);
  document.addEventListener('visibilitychange', () => { if (document.visibilityState === 'hidden') save(); });
  window.__notivo = { api: (m, p, b) => api(m, p, b), shade }; // test ve hata ayıklama için
} catch (e) {
  fail('Veritabanı motoru (sql.js) yüklenemedi. İnternet bağlantını kontrol edip sayfayı yenile.');
  throw e;
}

export async function api(method, path, body) {
  try {
    return await call(method, path, body ?? {});
  } catch (e) {
    if (e instanceof ApiError) throw Object.assign(new Error(e.message), { status: e.status, code: e.code });
    throw e;
  }
}

export function connect({ onEvent, onOpen }) {
  listeners.add(onEvent);
  queueMicrotask(() => onOpen?.());
}

/** CSV / yedek: sayfanın "downloads" yeteneğiyle kaydedilir (kullanıcı onay verir). */
export async function download(kind) {
  const dl = await window.claude?.use?.('downloads');
  if (!dl) throw new Error('Bu görünümde dosya kaydetme kullanılamıyor');
  const csv = kind === 'csv';
  try {
    await dl.save({ filename: csv ? 'notivo.csv' : 'notivo-yedek.json', data: csv ? shade.exportCsv() : JSON.stringify(shade.backup()) });
    return true;
  } catch (e) {
    if (e?.code === 'declined') return false;
    throw new Error('Dosya kaydedilemedi');
  }
}
