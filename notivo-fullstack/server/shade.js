// Android bildirim çubuğunun (NotificationManager + notification shade) çalışma mantığı.
// Kaynak gerçek: SQLite. Tüm kurallar burada; HTTP katmanı sadece taşır (bkz. docs/ANDROID_PARITY.md).

import { norm, isDeletedNotice, matchRule, csvCell } from './textlogic.js';

/** Android'deki NotificationManager.IMPORTANCE_* değerleri. */
export const IMPORTANCE = Object.freeze({ NONE: 0, MIN: 1, LOW: 2, DEFAULT: 3, HIGH: 4 });
export const SECTIONS = Object.freeze([
  { id: 'conversations', title: 'Konuşmalar' },
  { id: 'alerting', title: 'Bildirimler' },
  { id: 'silent', title: 'Sessiz bildirimler' },
]);
export const REASON = Object.freeze({
  USER_SWIPE: 'USER_SWIPE',
  GROUP_SWIPE: 'GROUP_SWIPE',
  CLEAR_ALL: 'CLEAR_ALL',
  CLICK: 'CLICK',
  APP_CANCEL: 'APP_CANCEL',
  TIMEOUT: 'TIMEOUT',
  CHANNEL_BLOCKED: 'CHANNEL_BLOCKED',
  APP_BLOCKED: 'APP_BLOCKED',
  RULE_MUTE: 'RULE_MUTE',
  GROUP_EMPTY: 'GROUP_EMPTY',
  RESTORED: 'RESTORED',
  REPLACED: 'REPLACED',
});
/** Bir uygulamanın gruplanmamış bildirimi bu sayıya ulaşınca otomatik gruba alınır (AOSP GroupHelper.AUTOGROUP_AT_COUNT). */
export const AUTOGROUP_AT_COUNT = 4;
/** Bir uygulamanın aynı anda bırakabileceği canlı bildirim sayısı (Android'deki uygulama başına sınır; değer anımsamadır, doğrulanmadı). */
export const MAX_LIVE_PER_APP = 50;
export const DEDUPE_MS = 10_000;
export const DELETED_WINDOW_MS = 24 * 3_600_000;
const LIVE = "state IN ('active','snoozed')";
const DEFAULT_DND = Object.freeze({ enabled: false, allow: ['alarm', 'call'] });

export class ApiError extends Error {
  constructor(status, code, message) {
    super(message);
    this.status = status;
    this.code = code;
  }
}
const bad = (code, message) => new ApiError(400, code, message);

export const makeKey = (pkg, id, tag) => `${pkg}|${id}|${tag ?? ''}`;

/**
 * Bildirim geldiğinde ses/heads-up kararı.
 *  - HIGH: ses + heads-up, DEFAULT: yalnızca ses, LOW/MIN: sessiz.
 *  - Aynı anahtarla güncelleme + onlyAlertOnce ise yeniden uyarmaz.
 *  - Grup özeti uyarmaz (çocuklar uyarır).
 *  - Rahatsız Etme açıkken, izin verilen kategoriler (alarm, çağrı) dışında ses ve heads-up bastırılır.
 */
export function computeAlert({ importance, isUpdate, onlyAlertOnce, isSummary, dnd, category }) {
  let sound = importance >= IMPORTANCE.DEFAULT;
  let headsUp = importance >= IMPORTANCE.HIGH;
  if (isSummary || (isUpdate && onlyAlertOnce)) {
    sound = false;
    headsUp = false;
  }
  let suppressedByDnd = false;
  if (dnd?.enabled && !(dnd.allow ?? []).includes(category)) {
    suppressedByDnd = sound || headsUp;
    sound = false;
    headsUp = false;
  }
  return { sound, headsUp, suppressedByDnd };
}

/** Android 11+ bölümleri: konuşmalar, uyaran bildirimler, sessiz bildirimler. */
export function sectionOf(importance, conversation) {
  if (importance >= IMPORTANCE.DEFAULT) return conversation ? 'conversations' : 'alerting';
  return 'silent';
}

// ---------------------------------------------------------------- doğrulama

function optStr(v, name, max, def = '') {
  if (v === undefined || v === null) return def;
  if (typeof v !== 'string') throw bad(`invalid_${name}`, `${name} metin olmalı`);
  if (v.length > max) throw bad(`invalid_${name}`, `${name} en fazla ${max} karakter olabilir`);
  return v;
}
function optInt(v, name, { min, max, def }) {
  if (v === undefined || v === null) return def;
  if (!Number.isSafeInteger(v) || v < min || v > max) {
    throw bad(`invalid_${name}`, `${name} ${min}..${max} arasında tam sayı olmalı`);
  }
  return v;
}
function optBool(v, name, def = false) {
  if (v === undefined || v === null) return def;
  if (typeof v !== 'boolean') throw bad(`invalid_${name}`, `${name} true/false olmalı`);
  return v;
}
const PKG_RE = /^[A-Za-z0-9._-]{1,128}$/;
const ID_RE = /^[A-Za-z0-9._-]{1,64}$/;

function validatePkg(v) {
  if (typeof v !== 'string' || !PKG_RE.test(v)) throw bad('invalid_pkg', 'pkg 1-128 karakter; harf, rakam, . _ - içerebilir');
  return v;
}

export function validatePost(input) {
  if (!input || typeof input !== 'object' || Array.isArray(input)) throw bad('invalid_body', 'Gövde bir nesne olmalı');
  const n = {
    pkg: validatePkg(input.pkg),
    appName: optStr(input.appName, 'appName', 80),
    channel: optStr(input.channel, 'channel', 64),
    id: optInt(input.id, 'id', { min: -2_147_483_648, max: 2_147_483_647, def: 0 }),
    tag: optStr(input.tag, 'tag', 64),
    title: optStr(input.title, 'title', 200),
    text: optStr(input.text, 'text', 2000),
    bigText: optStr(input.bigText, 'bigText', 8000),
    subText: optStr(input.subText, 'subText', 100),
    category: optStr(input.category, 'category', 32),
    group: optStr(input.group, 'group', 64),
    groupSummary: optBool(input.groupSummary, 'groupSummary'),
    ongoing: optBool(input.ongoing, 'ongoing'),
    autoCancel: optBool(input.autoCancel, 'autoCancel', true),
    onlyAlertOnce: optBool(input.onlyAlertOnce, 'onlyAlertOnce'),
    conversation: optBool(input.conversation, 'conversation'),
    timeoutAfterMs: optInt(input.timeoutAfterMs, 'timeoutAfterMs', { min: 1000, max: 30 * 86_400_000, def: null }),
    when: optInt(input.when, 'when', { min: 0, max: 8_640_000_000_000_000, def: null }),
    contentIntent: optStr(input.contentIntent, 'contentIntent', 300),
    actions: [],
  };
  if (!ID_RE.test(n.channel)) throw bad('invalid_channel', 'channel zorunlu (1-64 karakter; harf, rakam, . _ -)');
  if (!n.title.trim() && !n.text.trim()) throw bad('empty_notification', 'title ya da text boş olamaz');
  if (n.groupSummary && !n.group) throw bad('invalid_group', 'groupSummary için group gerekli');
  if (input.actions !== undefined && input.actions !== null) {
    if (!Array.isArray(input.actions) || input.actions.length > 3) throw bad('invalid_actions', 'actions en fazla 3 öğeli dizi olmalı');
    n.actions = input.actions.map((a) => {
      if (!a || typeof a !== 'object') throw bad('invalid_actions', 'action nesne olmalı');
      const id = optStr(a.id, 'action.id', 32);
      if (!/^[A-Za-z0-9._-]{1,32}$/.test(id)) throw bad('invalid_actions', 'action.id geçersiz');
      const title = optStr(a.title, 'action.title', 40);
      if (!title.trim()) throw bad('invalid_actions', 'action.title boş olamaz');
      return { id, title, reply: optBool(a.reply, 'action.reply') };
    });
  }
  return n;
}

function validateChannel(input) {
  if (!input || typeof input !== 'object') throw bad('invalid_body', 'Gövde bir nesne olmalı');
  const id = optStr(input.id, 'id', 64);
  if (!ID_RE.test(id)) throw bad('invalid_id', 'kanal id geçersiz');
  const name = optStr(input.name, 'name', 80);
  if (!name.trim()) throw bad('invalid_name', 'kanal adı boş olamaz');
  return {
    id,
    name,
    description: optStr(input.description, 'description', 200),
    appName: optStr(input.appName, 'appName', 80),
    importance: optInt(input.importance, 'importance', { min: 0, max: 4, def: IMPORTANCE.DEFAULT }),
  };
}

// ---------------------------------------------------------------- çekirdek

export class Shade {
  /**
   * @param {{db: import('node:sqlite').DatabaseSync, now?: () => number, emit?: (e: object) => void}} o
   */
  constructor({ db, now = Date.now, emit = () => {} }) {
    this.db = db;
    this.now = now;
    this.emit = emit;
    this.stmts = new Map();
    this.lastPurge = 0;
  }

  // ---- SQLite yardımcıları
  #st(sql) {
    let s = this.stmts.get(sql);
    if (!s) {
      s = this.db.prepare(sql);
      this.stmts.set(sql, s);
    }
    return s;
  }
  #run(sql, ...p) {
    return this.#st(sql).run(...p);
  }
  #get(sql, ...p) {
    return this.#st(sql).get(...p);
  }
  #all(sql, ...p) {
    return this.#st(sql).all(...p);
  }
  #tx(fn) {
    this.db.exec('BEGIN');
    try {
      const r = fn();
      this.db.exec('COMMIT');
      return r;
    } catch (e) {
      this.db.exec('ROLLBACK');
      throw e;
    }
  }
  /** İşlemi yapar, olayları işlem BAŞARIYLA bitince yayınlar. */
  #withEvents(fn) {
    const evs = [];
    const r = this.#tx(() => fn(evs));
    for (const e of evs) this.emit(e);
    return r;
  }

  // ---- ayarlar
  #setting(key, def) {
    const r = this.#get('SELECT value FROM settings WHERE key=?', key);
    if (!r) return def;
    try {
      return JSON.parse(r.value);
    } catch {
      return def;
    }
  }
  #setSetting(key, value) {
    this.#run('INSERT INTO settings(key,value) VALUES(?,?) ON CONFLICT(key) DO UPDATE SET value=excluded.value', key, JSON.stringify(value));
  }

  getDnd() {
    const d = this.#setting('dnd', DEFAULT_DND);
    return { enabled: !!d.enabled, allow: Array.isArray(d.allow) ? d.allow : [...DEFAULT_DND.allow] };
  }
  setDnd(input) {
    if (!input || typeof input !== 'object') throw bad('invalid_body', 'Gövde bir nesne olmalı');
    const cur = this.getDnd();
    const next = {
      enabled: optBool(input.enabled, 'enabled', cur.enabled),
      allow: cur.allow,
    };
    if (input.allow !== undefined) {
      if (!Array.isArray(input.allow) || input.allow.length > 10) throw bad('invalid_allow', 'allow en fazla 10 kategori içeren dizi olmalı');
      next.allow = input.allow.map((c) => optStr(c, 'allow', 32));
    }
    this.#setSetting('dnd', next);
    this.emit({ type: 'dnd', dnd: next });
    return next;
  }
  getRetentionDays() {
    return this.#setting('retentionDays', 30);
  }
  setRetentionDays(days) {
    const d = optInt(days, 'retentionDays', { min: 0, max: 3650, def: 30 });
    this.#setSetting('retentionDays', d);
    return d;
  }

  // ---- uygulamalar ve kanallar
  #upsertApp(pkg, name) {
    const existing = this.#get('SELECT * FROM apps WHERE pkg=?', pkg);
    if (!existing) {
      this.#run('INSERT INTO apps(pkg,name,blocked) VALUES(?,?,0)', pkg, name || pkg);
      return;
    }
    if (name && name !== existing.name) this.#run('UPDATE apps SET name=? WHERE pkg=?', name, pkg);
  }

  /**
   * Uygulama tarafı: kanal oluşturur. Kanal zaten varsa yalnızca ad/açıklama güncellenir;
   * önem düzeyi (importance) uygulama tarafından değiştirilemez, bunu yalnızca kullanıcı yapar.
   */
  createChannel(pkgIn, input) {
    const pkg = validatePkg(pkgIn);
    const c = validateChannel(input);
    return this.#withEvents((evs) => {
      this.#upsertApp(pkg, c.appName);
      const cur = this.#get('SELECT * FROM channels WHERE pkg=? AND id=?', pkg, c.id);
      if (cur) {
        this.#run('UPDATE channels SET name=?, description=? WHERE pkg=? AND id=?', c.name, c.description, pkg, c.id);
      } else {
        this.#run('INSERT INTO channels(pkg,id,name,description,importance) VALUES(?,?,?,?,?)', pkg, c.id, c.name, c.description, c.importance);
      }
      evs.push({ type: 'channel', pkg, channel: c.id });
      return { ...this.#channelDto(this.#get('SELECT * FROM channels WHERE pkg=? AND id=?', pkg, c.id)), created: !cur, importanceLocked: !!cur };
    });
  }

  #channelDto(c) {
    return { id: c.id, name: c.name, description: c.description, importance: c.importance, userModified: !!c.user_modified };
  }

  /** Kullanıcı tarafı: kanalın önem düzeyini değiştirir. NONE ise kanaldaki bildirimler kaldırılır. */
  setChannelImportance(pkgIn, channelId, importance) {
    const pkg = validatePkg(pkgIn);
    const imp = optInt(importance, 'importance', { min: 0, max: 4, def: undefined });
    if (imp === undefined) throw bad('invalid_importance', 'importance zorunlu');
    return this.#withEvents((evs) => {
      const ch = this.#get('SELECT * FROM channels WHERE pkg=? AND id=?', pkg, channelId);
      if (!ch) throw new ApiError(404, 'unknown_channel', 'Kanal bulunamadı');
      this.#run('UPDATE channels SET importance=?, user_modified=1 WHERE pkg=? AND id=?', imp, pkg, channelId);
      if (imp === IMPORTANCE.NONE) {
        const t = this.now();
        for (const r of this.#all(`SELECT * FROM notifications WHERE pkg=? AND channel=? AND ${LIVE}`, pkg, channelId)) {
          this.#remove(r, REASON.CHANNEL_BLOCKED, t, evs);
        }
      }
      evs.push({ type: 'channel', pkg, channel: channelId });
      return this.#channelDto(this.#get('SELECT * FROM channels WHERE pkg=? AND id=?', pkg, channelId));
    });
  }

  /** Kullanıcı tarafı: uygulamanın tüm bildirimlerini engeller / açar. */
  setAppBlocked(pkgIn, blocked) {
    const pkg = validatePkg(pkgIn);
    const b = optBool(blocked, 'blocked', undefined);
    if (b === undefined) throw bad('invalid_blocked', 'blocked zorunlu');
    return this.#withEvents((evs) => {
      if (!this.#get('SELECT pkg FROM apps WHERE pkg=?', pkg)) throw new ApiError(404, 'unknown_app', 'Uygulama bulunamadı');
      this.#run('UPDATE apps SET blocked=? WHERE pkg=?', b ? 1 : 0, pkg);
      if (b) {
        const t = this.now();
        for (const r of this.#all(`SELECT * FROM notifications WHERE pkg=? AND ${LIVE}`, pkg)) {
          this.#remove(r, REASON.APP_BLOCKED, t, evs);
        }
      }
      evs.push({ type: 'app', pkg });
      return { pkg, blocked: b };
    });
  }

  listApps() {
    const apps = this.#all('SELECT * FROM apps ORDER BY name COLLATE NOCASE');
    const chans = this.#all('SELECT * FROM channels ORDER BY name COLLATE NOCASE');
    const counts = new Map(
      this.#all("SELECT pkg, COUNT(*) AS n FROM notifications WHERE state='active' AND is_summary=0 GROUP BY pkg").map((r) => [r.pkg, r.n]),
    );
    return apps.map((a) => ({
      pkg: a.pkg,
      name: a.name,
      blocked: !!a.blocked,
      active: counts.get(a.pkg) ?? 0,
      channels: chans.filter((c) => c.pkg === a.pkg).map((c) => this.#channelDto(c)),
    }));
  }

  // ---- bildirim yayınlama (uygulama tarafı)

  /** NotificationManager.notify(): aynı (pkg,id,tag) anahtarı varsa yerinde günceller. */
  post(input) {
    const n = validatePost(input);
    const t = this.now();
    return this.#withEvents((evs) => this.#post(n, t, evs));
  }

  #post(n, t, evs) {
    this.#upsertApp(n.pkg, n.appName);
    const app = this.#get('SELECT * FROM apps WHERE pkg=?', n.pkg);
    const ch = this.#get('SELECT * FROM channels WHERE pkg=? AND id=?', n.pkg, n.channel);
    if (!ch) throw new ApiError(400, 'unknown_channel', `Kanal bulunamadı: "${n.channel}". Önce kanalı oluşturun (Android 8+ kanalsız bildirim göstermez).`);
    const key = makeKey(n.pkg, n.id, n.tag);

    // 1) Engelli uygulama ya da kapalı kanal: bildirim hiç gösterilmez.
    if (app.blocked || ch.importance === IMPORTANCE.NONE) return { status: 'blocked', key };

    const live = this.#get(`SELECT * FROM notifications WHERE key=? AND ${LIVE}`, key);
    if (!live && this.#get(`SELECT COUNT(*) AS c FROM notifications WHERE pkg=? AND ${LIVE}`, n.pkg).c >= MAX_LIVE_PER_APP) {
      throw new ApiError(429, 'too_many_notifications', `Uygulama başına en fazla ${MAX_LIVE_PER_APP} canlı bildirim olabilir; önce bazılarını iptal edin`);
    }

    // 2) Notivo kopya engeli: aynı içerik, FARKLI anahtarla 10 sn içinde tekrar gelirse atlanır.
    //    Var olan bildirimin güncellemesi (aynı anahtar) hiçbir zaman kopya sayılmaz.
    if (!live && !n.groupSummary) {
      const dup = this.#get(
        'SELECT COUNT(*) AS c FROM notifications WHERE pkg=? AND title=? AND text=? AND key<>? AND is_summary=0 AND updated_at>?',
        n.pkg, n.title, n.text, key, t - DEDUPE_MS,
      );
      if (dup.c > 0) return { status: 'duplicate', key };
    }

    // 3) Notivo kuralları
    const rules = this.#all('SELECT * FROM rules WHERE enabled=1').map((r) => ({ ...r, enabled: true }));
    const rule = n.groupSummary ? null : matchRule(rules, n.pkg, n.title, n.text);
    if (rule?.action === 'BLOCK') return { status: 'blocked_by_rule', key };

    // 4) Notivo "mesaj silindi" tespiti: yer tutucu bildirim saklanmaz, önceki mesaj işaretlenir.
    if (!n.groupSummary && isDeletedNotice(n.text)) {
      this.#run(
        `UPDATE notifications SET deleted_by_sender=1 WHERE id = (
           SELECT id FROM notifications WHERE pkg=? AND title=? AND deleted_by_sender=0 AND is_summary=0 AND updated_at>?
           ORDER BY updated_at DESC, id DESC LIMIT 1)`,
        n.pkg, n.title, t - DELETED_WINDOW_MS,
      );
      evs.push({ type: 'deleted_notice', pkg: n.pkg, title: n.title });
      return { status: 'deleted_notice', key };
    }

    const appName = app.name;
    const searchNorm = norm(`${n.title} ${n.text} ${appName}`);
    const expiresAt = n.timeoutAfterMs ? t + n.timeoutAfterMs : null;
    const whenMs = n.when ?? t;
    const actions = JSON.stringify(n.actions);

    // 5) MUTE kuralı: bildirim çubuğunda gösterilmez, geçmişe sessizce yazılır.
    if (rule?.action === 'MUTE') {
      if (live) this.#remove(live, REASON.RULE_MUTE, t, evs);
      this.#insert(n, key, t, whenMs, null, actions, searchNorm, 'removed', REASON.RULE_MUTE);
      return { status: 'muted_by_rule', key };
    }

    // 6) Yerinde güncelleme ya da yeni kayıt
    if (live) {
      // Android bir konuşmayı tek bildirim olarak yerinde günceller; Notivo ise her mesajı saklar.
      // Metin değişiyorsa eski hali geçmişe 'REPLACED' kaydı olarak yazılır (ilerleme/süregelen bildirimler hariç).
      if (!live.ongoing && !n.ongoing && !live.is_summary && !n.groupSummary && (live.title !== n.title || live.text !== n.text)) {
        this.#run(
          `INSERT INTO notifications(key,pkg,nid,tag,channel,title,text,big_text,sub_text,category,grp,is_summary,ongoing,auto_cancel,only_alert_once,
             conversation,actions,content_intent,when_ms,posted_at,updated_at,expires_at,state,removed_reason,removed_at,deleted_by_sender,search_norm)
           SELECT key || '#v' || id, pkg,nid,tag,channel,title,text,big_text,sub_text,category,grp,is_summary,ongoing,auto_cancel,only_alert_once,
             conversation,actions,content_intent,when_ms,posted_at,updated_at,NULL,'removed','REPLACED',?,deleted_by_sender,search_norm
           FROM notifications WHERE id=?`,
          t, live.id,
        );
      }
      this.#run(
        `UPDATE notifications SET channel=?, title=?, text=?, big_text=?, sub_text=?, category=?, grp=?, is_summary=?, ongoing=?,
           auto_cancel=?, only_alert_once=?, conversation=?, actions=?, content_intent=?, when_ms=?, updated_at=?, expires_at=?, search_norm=?
         WHERE id=?`,
        n.channel, n.title, n.text, n.bigText, n.subText, n.category, n.group, n.groupSummary ? 1 : 0, n.ongoing ? 1 : 0,
        n.autoCancel ? 1 : 0, n.onlyAlertOnce ? 1 : 0, n.conversation ? 1 : 0, actions, n.contentIntent, whenMs, t, expiresAt, searchNorm,
        live.id,
      );
    } else {
      this.#insert(n, key, t, whenMs, expiresAt, actions, searchNorm, 'active', null);
    }
    evs.push({ type: live ? 'updated' : 'posted', key, pkg: n.pkg });

    // Ertelenmiş bildirim güncellenirse süre dolana kadar gizli kalır (AOSP SnoozeHelper.update).
    if (live?.state === 'snoozed') return { status: 'updated', key, snoozed: true, alerted: { sound: false, headsUp: false, suppressedByDnd: false } };

    const alerted = computeAlert({
      importance: ch.importance,
      isUpdate: !!live,
      onlyAlertOnce: n.onlyAlertOnce,
      isSummary: n.groupSummary,
      dnd: this.getDnd(),
      category: n.category,
    });
    if (alerted.sound || alerted.headsUp) {
      evs.push({ type: 'alert', key, pkg: n.pkg, sound: alerted.sound, headsUp: alerted.headsUp, notification: this.#dtoByKey(key) });
    }
    return { status: live ? 'updated' : 'posted', key, alerted, section: sectionOf(ch.importance, n.conversation) };
  }

  #insert(n, key, t, whenMs, expiresAt, actions, searchNorm, state, reason) {
    this.#run(
      `INSERT INTO notifications(key,pkg,nid,tag,channel,title,text,big_text,sub_text,category,grp,is_summary,ongoing,auto_cancel,
         only_alert_once,conversation,actions,content_intent,when_ms,posted_at,updated_at,expires_at,state,removed_reason,removed_at,search_norm)
       VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)`,
      key, n.pkg, n.id, n.tag, n.channel, n.title, n.text, n.bigText, n.subText, n.category, n.group, n.groupSummary ? 1 : 0,
      n.ongoing ? 1 : 0, n.autoCancel ? 1 : 0, n.onlyAlertOnce ? 1 : 0, n.conversation ? 1 : 0, actions, n.contentIntent,
      whenMs, t, t, expiresAt, state, reason, state === 'removed' ? t : null, searchNorm,
    );
  }

  /** Uygulama tarafı: NotificationManager.cancel(). deleteIntent çağrılmaz. */
  cancel(input) {
    const pkg = validatePkg(input?.pkg);
    const id = optInt(input?.id, 'id', { min: -2_147_483_648, max: 2_147_483_647, def: 0 });
    const tag = optStr(input?.tag, 'tag', 64);
    return this.#withEvents((evs) => {
      const row = this.#get(`SELECT * FROM notifications WHERE key=? AND ${LIVE}`, makeKey(pkg, id, tag));
      if (!row) return { cancelled: false };
      this.#remove(row, REASON.APP_CANCEL, this.now(), evs);
      this.#pruneSummaries(pkg, this.now(), evs);
      return { cancelled: true };
    });
  }

  /** Uygulama tarafı: NotificationManager.cancelAll() (yalnızca o uygulamanın bildirimleri). */
  cancelAll(input) {
    const pkg = validatePkg(input?.pkg);
    return this.#withEvents((evs) => {
      const t = this.now();
      const rows = this.#all(`SELECT * FROM notifications WHERE pkg=? AND ${LIVE}`, pkg);
      for (const r of rows) this.#remove(r, REASON.APP_CANCEL, t, evs);
      return { cancelled: rows.length };
    });
  }

  // ---- kullanıcı eylemleri (bildirim çubuğu)

  #remove(row, reason, t, evs, { deleteIntent = false } = {}) {
    this.#run("UPDATE notifications SET state='removed', removed_reason=?, removed_at=?, snooze_until=NULL WHERE id=?", reason, t, row.id);
    evs.push({ type: 'removed', key: row.key, pkg: row.pkg, reason });
    // Android deleteIntent: yalnızca kullanıcı kapatınca (kaydırma / tümünü temizle) uygulamaya haber verilir.
    if (deleteIntent && !row.is_summary) evs.push({ type: 'dismissed', key: row.key, pkg: row.pkg, reason });
  }

  /** Çocuğu kalmayan grup özetlerini sistem kaldırır. */
  #pruneSummaries(pkg, t, evs) {
    const orphans = this.#all(
      `SELECT s.* FROM notifications s WHERE s.pkg=? AND s.state='active' AND s.is_summary=1 AND NOT EXISTS (
         SELECT 1 FROM notifications c WHERE c.pkg=s.pkg AND c.grp=s.grp AND c.is_summary=0 AND c.state IN ('active','snoozed'))`,
      pkg,
    );
    for (const s of orphans) this.#remove(s, REASON.GROUP_EMPTY, t, evs);
  }

  #liveActive(key) {
    const row = this.#get("SELECT * FROM notifications WHERE key=? AND state='active'", key);
    if (!row) throw new ApiError(404, 'not_found', 'Bildirim bulunamadı');
    if (row.is_summary) throw new ApiError(400, 'is_summary', 'Grup özeti doğrudan işlenemez');
    return row;
  }
  static #key(v) {
    if (typeof v !== 'string' || !v || v.length > 300) throw bad('invalid_key', 'key geçersiz');
    return v;
  }

  /** Kullanıcı kaydırarak kapatır. Süregelen (ongoing) bildirimler kapatılamaz. */
  dismiss(keyIn) {
    const key = Shade.#key(keyIn);
    return this.#withEvents((evs) => {
      const row = this.#liveActive(key);
      if (row.ongoing) throw new ApiError(409, 'ongoing', 'Süregelen bildirim kaydırılarak kapatılamaz');
      const t = this.now();
      this.#remove(row, REASON.USER_SWIPE, t, evs, { deleteIntent: true });
      this.#pruneSummaries(row.pkg, t, evs);
      return { dismissed: 1 };
    });
  }

  #rowsWithImportance() {
    return this.#all(
      `SELECT n.*, c.importance AS importance FROM notifications n JOIN channels c ON c.pkg=n.pkg AND c.id=n.channel WHERE n.state='active'`,
    );
  }

  /**
   * Grubu kaydırarak kapatır: gruptaki kapatılabilir (ongoing olmayan) tüm çocuklar gider.
   * group boş ise o uygulamanın bölümdeki gruplanmamış (otomatik gruplanan) bildirimleri kastedilir.
   */
  dismissGroup(input) {
    const pkg = validatePkg(input?.pkg);
    const group = optStr(input?.group, 'group', 64);
    const section = input?.section;
    if (!SECTIONS.some((s) => s.id === section)) throw bad('invalid_section', 'section geçersiz');
    return this.#withEvents((evs) => {
      const t = this.now();
      let n = 0;
      for (const r of this.#rowsWithImportance()) {
        if (r.pkg !== pkg || r.is_summary || r.grp !== group || r.ongoing) continue;
        if (sectionOf(r.importance, !!r.conversation) !== section) continue;
        this.#remove(r, REASON.GROUP_SWIPE, t, evs, { deleteIntent: true });
        n++;
      }
      this.#pruneSummaries(pkg, t, evs);
      return { dismissed: n };
    });
  }

  /** "Tümünü temizle": süregelen olmayan her şeyi kaldırır; section verilirse yalnızca o bölümü. */
  clearAll(input = {}) {
    const section = input?.section;
    if (section !== undefined && !SECTIONS.some((s) => s.id === section)) throw bad('invalid_section', 'section geçersiz');
    return this.#withEvents((evs) => {
      const t = this.now();
      let n = 0;
      const pkgs = new Set();
      for (const r of this.#rowsWithImportance()) {
        if (r.ongoing || r.is_summary) continue;
        if (section && sectionOf(r.importance, !!r.conversation) !== section) continue;
        this.#remove(r, REASON.CLEAR_ALL, t, evs, { deleteIntent: true });
        pkgs.add(r.pkg);
        n++;
      }
      for (const pkg of pkgs) this.#pruneSummaries(pkg, t, evs);
      return { dismissed: n };
    });
  }

  /** Bildirime dokunma (contentIntent). autoCancel açıksa ve süregelen değilse bildirim kalkar. */
  click(keyIn) {
    const key = Shade.#key(keyIn);
    return this.#withEvents((evs) => {
      const row = this.#liveActive(key);
      const t = this.now();
      evs.push({ type: 'click', key, pkg: row.pkg, contentIntent: row.content_intent });
      const cancelled = !!row.auto_cancel && !row.ongoing;
      if (cancelled) {
        this.#remove(row, REASON.CLICK, t, evs);
        this.#pruneSummaries(row.pkg, t, evs);
      }
      return { key, contentIntent: row.content_intent, cancelled };
    });
  }

  /** Eylem düğmesi ya da satır içi yanıt (RemoteInput). Sonuç, uygulamaya 'action' olayı olarak gider. */
  action(keyIn, actionId, text) {
    const key = Shade.#key(keyIn);
    return this.#withEvents((evs) => {
      const row = this.#liveActive(key);
      const act = JSON.parse(row.actions).find((a) => a.id === actionId);
      if (!act) throw new ApiError(404, 'unknown_action', 'Eylem bulunamadı');
      let reply = '';
      if (act.reply) {
        reply = optStr(text, 'text', 2000);
        if (!reply.trim()) throw bad('reply_required', 'Yanıt metni boş olamaz');
      }
      evs.push({ type: 'action', key, pkg: row.pkg, actionId, text: reply });
      return { ok: true, key, actionId };
    });
  }

  /** Ertele: bildirim çubuğundan kalkar, süre dolunca yeniden uyararak geri gelir. */
  snooze(keyIn, minutes) {
    const key = Shade.#key(keyIn);
    const m = optInt(minutes, 'minutes', { min: 1, max: 10_080, def: undefined });
    if (m === undefined) throw bad('invalid_minutes', 'minutes zorunlu (1..10080)');
    return this.#withEvents((evs) => {
      const row = this.#liveActive(key);
      if (row.ongoing) throw new ApiError(409, 'ongoing', 'Süregelen bildirim ertelenemez');
      const t = this.now();
      const until = t + m * 60_000;
      this.#run("UPDATE notifications SET state='snoozed', snooze_until=? WHERE id=?", until, row.id);
      this.#pruneSummaries(row.pkg, t, evs);
      evs.push({ type: 'snoozed', key, pkg: row.pkg, until });
      return { key, until };
    });
  }

  /** Zamanlayıcı: bildirim zaman aşımı, erteleme bitişi, geçmiş temizliği. */
  tick() {
    const t = this.now();
    return this.#withEvents((evs) => {
      let expired = 0;
      let unsnoozed = 0;
      const dnd = this.getDnd();
      for (const r of this.#all(`SELECT * FROM notifications WHERE ${LIVE} AND expires_at IS NOT NULL AND expires_at<=?`, t)) {
        this.#remove(r, REASON.TIMEOUT, t, evs);
        expired++;
      }
      for (const r of this.#all("SELECT * FROM notifications WHERE state='snoozed' AND snooze_until<=?", t)) {
        this.#run("UPDATE notifications SET state='active', snooze_until=NULL WHERE id=?", r.id);
        evs.push({ type: 'unsnoozed', key: r.key, pkg: r.pkg });
        const ch = this.#get('SELECT * FROM channels WHERE pkg=? AND id=?', r.pkg, r.channel);
        const app = this.#get('SELECT * FROM apps WHERE pkg=?', r.pkg);
        if (!ch || app?.blocked || ch.importance === IMPORTANCE.NONE) {
          this.#remove({ ...r, state: 'active' }, REASON.CHANNEL_BLOCKED, t, evs);
          continue;
        }
        unsnoozed++;
        const a = computeAlert({ importance: ch.importance, isUpdate: false, onlyAlertOnce: false, isSummary: !!r.is_summary, dnd, category: r.category });
        if (a.sound || a.headsUp) evs.push({ type: 'alert', key: r.key, pkg: r.pkg, sound: a.sound, headsUp: a.headsUp, notification: this.#dtoByKey(r.key) });
      }
      let purged = 0;
      const days = this.getRetentionDays();
      if (days > 0 && t - this.lastPurge > 3_600_000) {
        this.lastPurge = t;
        purged = this.#run("DELETE FROM notifications WHERE state='removed' AND removed_at<?", t - days * 86_400_000).changes;
      }
      return { expired, unsnoozed, purged };
    });
  }

  // ---- okuma: bildirim çubuğu ve geçmiş

  #dto(r) {
    return {
      key: r.key,
      pkg: r.pkg,
      appName: r.app_name ?? r.pkg,
      id: r.nid,
      tag: r.tag,
      channel: r.channel,
      channelName: r.channel_name ?? r.channel,
      importance: r.importance ?? null,
      title: r.title,
      text: r.text,
      bigText: r.big_text,
      subText: r.sub_text,
      category: r.category,
      group: r.grp,
      ongoing: !!r.ongoing,
      autoCancel: !!r.auto_cancel,
      conversation: !!r.conversation,
      actions: JSON.parse(r.actions),
      contentIntent: r.content_intent,
      when: r.when_ms,
      postedAt: r.posted_at,
      expiresAt: r.expires_at,
      deletedBySender: !!r.deleted_by_sender,
      clearable: !r.ongoing,
    };
  }

  #dtoByKey(key) {
    const r = this.#get(
      `SELECT n.*, c.importance AS importance, c.name AS channel_name, a.name AS app_name
         FROM notifications n JOIN channels c ON c.pkg=n.pkg AND c.id=n.channel JOIN apps a ON a.pkg=n.pkg
        WHERE n.key=? AND ${LIVE}`,
      key,
    );
    return r ? this.#dto(r) : null;
  }

  /** Şu an bildirim çubuğunda görünenler: bölümler → girdiler (tekil bildirim ya da grup). */
  shade() {
    const rows = this.#all(
      `SELECT n.*, c.importance AS importance, c.name AS channel_name, a.name AS app_name
         FROM notifications n JOIN channels c ON c.pkg=n.pkg AND c.id=n.channel JOIN apps a ON a.pkg=n.pkg
        WHERE n.state='active'`,
    );
    const summaries = new Map();
    const kids = [];
    for (const r of rows) {
      if (r.is_summary) summaries.set(`${r.pkg}|${r.grp}`, r);
      else kids.push(r);
    }

    const bySection = { conversations: [], alerting: [], silent: [] };
    for (const r of kids) bySection[sectionOf(r.importance, !!r.conversation)].push(r);

    const sections = SECTIONS.map(({ id, title }) => {
      const entries = this.#entries(bySection[id], summaries);
      return { id, title, entries, count: bySection[id].length, clearable: bySection[id].some((r) => !r.ongoing) };
    });

    // Durum çubuğu simgeleri: MIN düzeyindekiler görünmez.
    const icons = new Map();
    const badges = {};
    for (const r of kids) {
      badges[r.pkg] = (badges[r.pkg] ?? 0) + 1;
      if (r.importance < IMPORTANCE.LOW) continue;
      const cur = icons.get(r.pkg) ?? { pkg: r.pkg, appName: r.app_name, count: 0, newest: 0 };
      cur.count++;
      cur.newest = Math.max(cur.newest, r.when_ms);
      icons.set(r.pkg, cur);
    }
    const snoozed = this.#get("SELECT COUNT(*) AS c FROM notifications WHERE state='snoozed'").c;
    return {
      now: this.now(),
      dnd: this.getDnd(),
      sections,
      statusBar: [...icons.values()].sort((a, b) => b.newest - a.newest).map(({ newest, ...i }) => i),
      badges,
      total: kids.length,
      snoozed,
    };
  }

  #entries(rows, summaries) {
    const byPkg = new Map();
    for (const r of rows) {
      if (!byPkg.has(r.pkg)) byPkg.set(r.pkg, []);
      byPkg.get(r.pkg).push(r);
    }
    const entries = [];
    for (const [pkg, list] of byPkg) {
      const explicit = new Map();
      const loose = [];
      for (const r of list) {
        if (!r.grp) loose.push(r);
        else {
          if (!explicit.has(r.grp)) explicit.set(r.grp, []);
          explicit.get(r.grp).push(r);
        }
      }
      for (const [grp, kids] of explicit) {
        // Tek çocuklu grup özet göstermez; çocuk tekil bildirim gibi görünür.
        if (kids.length >= 2) entries.push(this.#groupEntry(pkg, grp, kids, summaries.get(`${pkg}|${grp}`)));
        else loose.push(kids[0]);
      }
      if (loose.length >= AUTOGROUP_AT_COUNT) entries.push(this.#groupEntry(pkg, '', loose, null));
      else for (const r of loose) entries.push({ type: 'notification', importance: r.importance, newest: r.when_ms, n: this.#dto(r) });
    }
    entries.sort((a, b) => b.importance - a.importance || b.newest - a.newest);
    return entries.map(({ importance, newest, ...e }) => e);
  }

  #groupEntry(pkg, grp, kids, summary) {
    const children = kids.map((r) => this.#dto(r)).sort((a, b) => b.when - a.when);
    return {
      type: 'group',
      importance: Math.max(...kids.map((k) => k.importance)),
      newest: Math.max(...kids.map((k) => k.when_ms)),
      pkg,
      appName: kids[0].app_name,
      group: grp,
      auto: !grp,
      summary: summary ? { title: summary.title, text: summary.text } : null,
      count: children.length,
      clearable: children.some((c) => !c.ongoing),
      children,
    };
  }

  /** Bildirim geçmişi. q: Türkçe harf duyarsız arama. state: all | active | snoozed | removed. */
  history({ q = '', pkg = '', state = 'all', limit = 200 } = {}) {
    const lim = optInt(limit, 'limit', { min: 1, max: 1000, def: 200 });
    if (!['all', 'active', 'snoozed', 'removed'].includes(state)) throw bad('invalid_state', 'state geçersiz');
    const where = [];
    const params = [];
    if (state !== 'all') {
      where.push('n.state=?');
      params.push(state);
    }
    if (pkg) {
      where.push('n.pkg=?');
      params.push(pkg);
    }
    const nq = norm(q).trim();
    if (nq) {
      where.push("instr(n.search_norm, ?) > 0");
      params.push(nq);
    }
    const rows = this.#all(
      `SELECT n.*, COALESCE(a.name, n.pkg) AS app_name, c.importance AS importance, c.name AS channel_name
         FROM notifications n LEFT JOIN apps a ON a.pkg=n.pkg LEFT JOIN channels c ON c.pkg=n.pkg AND c.id=n.channel
        ${where.length ? 'WHERE ' + where.join(' AND ') : ''}
        ORDER BY n.updated_at DESC, n.id DESC LIMIT ?`,
      ...params, lim,
    );
    return rows.map((r) => ({
      ...this.#dto(r),
      state: r.state,
      removedReason: r.removed_reason,
      removedAt: r.removed_at,
      snoozeUntil: r.snooze_until,
      deletedBySender: !!r.deleted_by_sender,
    }));
  }

  // ---- Notivo kuralları

  listRules() {
    return this.#all('SELECT * FROM rules ORDER BY id DESC').map((r) => ({ id: r.id, pkg: r.pkg, keyword: r.keyword, action: r.action, enabled: !!r.enabled }));
  }
  addRule(input) {
    if (!input || typeof input !== 'object') throw bad('invalid_body', 'Gövde bir nesne olmalı');
    const pkg = optStr(input.pkg, 'pkg', 128);
    if (pkg && !PKG_RE.test(pkg)) throw bad('invalid_pkg', 'pkg geçersiz');
    const keyword = optStr(input.keyword, 'keyword', 100).trim();
    if (!keyword && !pkg) throw bad('empty_rule', 'Kural için anahtar kelime ya da uygulama gerekli');
    if (!['BLOCK', 'MUTE'].includes(input.action)) throw bad('invalid_action', 'action BLOCK ya da MUTE olmalı');
    const r = this.#run('INSERT INTO rules(pkg,keyword,action,enabled) VALUES(?,?,?,1)', pkg, keyword, input.action);
    this.emit({ type: 'rules' });
    return { id: Number(r.lastInsertRowid), pkg, keyword, action: input.action, enabled: true };
  }
  setRuleEnabled(id, enabled) {
    const e = optBool(enabled, 'enabled', undefined);
    if (e === undefined) throw bad('invalid_enabled', 'enabled zorunlu');
    const r = this.#run('UPDATE rules SET enabled=? WHERE id=?', e ? 1 : 0, id);
    if (!r.changes) throw new ApiError(404, 'not_found', 'Kural bulunamadı');
    this.emit({ type: 'rules' });
    return { id, enabled: e };
  }
  deleteRule(id) {
    const r = this.#run('DELETE FROM rules WHERE id=?', id);
    if (!r.changes) throw new ApiError(404, 'not_found', 'Kural bulunamadı');
    this.emit({ type: 'rules' });
    return { id };
  }

  // ---- dışa aktarma / yedek

  exportCsv() {
    const lines = ['time,app,title,text'];
    for (const h of this.history({ limit: 1000 })) {
      lines.push([new Date(h.when).toISOString(), h.appName, h.title, h.text].map(csvCell).join(','));
    }
    return lines.join('\n') + '\n';
  }

  backup() {
    const items = this.history({ limit: 1000 }).map((h) => ({
      pkg: h.pkg, app: h.appName, title: h.title, text: h.text, time: h.when, category: h.category, del: h.deletedBySender,
    }));
    return { app: 'notivo', version: 1, items };
  }

  /** Yedekten geri yükler; aynı (pkg,title,text,time) olanları atlar. Kaç kayıt eklendiğini döndürür. */
  restore(data) {
    if (!data || data.app !== 'notivo' || !Array.isArray(data.items)) throw bad('invalid_backup', 'Geçerli bir Notivo yedeği değil');
    if (data.items.length > 50_000) throw bad('too_large', 'Yedek çok büyük');
    return this.#tx(() => {
      const t = this.now();
      let added = 0;
      for (const it of data.items) {
        if (!it || typeof it !== 'object' || !PKG_RE.test(it.pkg ?? '') || !Number.isSafeInteger(it.time)) continue;
        const title = String(it.title ?? '').slice(0, 200);
        const text = String(it.text ?? '').slice(0, 2000);
        const exists = this.#get('SELECT COUNT(*) AS c FROM notifications WHERE pkg=? AND title=? AND text=? AND when_ms=?', it.pkg, title, text, it.time);
        if (exists.c > 0) continue;
        const appName = String(it.app ?? it.pkg).slice(0, 80);
        this.#upsertApp(it.pkg, appName);
        this.#insert(
          { pkg: it.pkg, id: 0, tag: `restored-${added}`, channel: '', title, text, bigText: '', subText: '', category: String(it.category ?? '').slice(0, 32),
            group: '', groupSummary: false, ongoing: false, autoCancel: true, onlyAlertOnce: false, conversation: false, contentIntent: '' },
          makeKey(it.pkg, 0, `restored-${t}-${added}`), t, it.time, null, '[]', norm(`${title} ${text} ${appName}`), 'removed', REASON.RESTORED,
        );
        // Geçmişte özgün zamanıyla görünsün (sıralama updated_at'e göre).
        this.#run('UPDATE notifications SET posted_at=?, updated_at=?, removed_at=?, deleted_by_sender=? WHERE id=last_insert_rowid()', it.time, it.time, t, it.del ? 1 : 0);
        added++;
      }
      return added;
    });
  }
}
