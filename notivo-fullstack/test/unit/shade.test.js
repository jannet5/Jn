import test from 'node:test';
import assert from 'node:assert/strict';
import { computeAlert, sectionOf, ApiError, IMPORTANCE as I, REASON } from '../../server/shade.js';
import { mk, chan, post, titles } from './helpers.js';

const code = (fn) => {
  try {
    fn();
  } catch (e) {
    assert.ok(e instanceof ApiError, `ApiError bekleniyordu: ${e}`);
    return e.code;
  }
  assert.fail('hata bekleniyordu');
};

// --- Android kanal önemi tablosu
test('uyarı kararı: HIGH ses+heads-up, DEFAULT ses, LOW/MIN sessiz', () => {
  const a = (importance, o = {}) => computeAlert({ importance, isUpdate: false, onlyAlertOnce: false, isSummary: false, dnd: { enabled: false, allow: [] }, category: '', ...o });
  assert.deepEqual(a(I.HIGH), { sound: true, headsUp: true, suppressedByDnd: false });
  assert.deepEqual(a(I.DEFAULT), { sound: true, headsUp: false, suppressedByDnd: false });
  assert.deepEqual(a(I.LOW), { sound: false, headsUp: false, suppressedByDnd: false });
  assert.deepEqual(a(I.MIN), { sound: false, headsUp: false, suppressedByDnd: false });
});

test('uyarı kararı: onlyAlertOnce yalnızca güncellemede, grup özeti hiç uyarmaz', () => {
  const base = { importance: I.HIGH, isSummary: false, dnd: { enabled: false, allow: [] }, category: '' };
  assert.equal(computeAlert({ ...base, isUpdate: true, onlyAlertOnce: true }).sound, false);
  assert.equal(computeAlert({ ...base, isUpdate: false, onlyAlertOnce: true }).sound, true); // ilk gönderim her zaman uyarır
  assert.equal(computeAlert({ ...base, isUpdate: true, onlyAlertOnce: false }).headsUp, true); // normal güncelleme yeniden uyarır
  assert.equal(computeAlert({ ...base, isUpdate: false, onlyAlertOnce: false, isSummary: true }).sound, false);
});

test('Rahatsız Etme: ses/heads-up bastırılır, izinli kategori (alarm) geçer', () => {
  const dnd = { enabled: true, allow: ['alarm'] };
  const base = { importance: I.HIGH, isUpdate: false, onlyAlertOnce: false, isSummary: false, dnd };
  assert.deepEqual(computeAlert({ ...base, category: 'msg' }), { sound: false, headsUp: false, suppressedByDnd: true });
  assert.deepEqual(computeAlert({ ...base, category: 'alarm' }), { sound: true, headsUp: true, suppressedByDnd: false });
  assert.equal(computeAlert({ ...base, importance: I.LOW, category: 'msg' }).suppressedByDnd, false); // zaten sessizdi
});

test('bölümler', () => {
  assert.equal(sectionOf(I.HIGH, true), 'conversations');
  assert.equal(sectionOf(I.DEFAULT, true), 'conversations');
  assert.equal(sectionOf(I.DEFAULT, false), 'alerting');
  assert.equal(sectionOf(I.LOW, true), 'silent'); // sessiz konuşma sessiz bölümde kalır
  assert.equal(sectionOf(I.MIN, false), 'silent');
});

// --- notify / güncelleme / iptal
test('bilinmeyen kanal reddedilir (Android 8+: kanalsız bildirim gösterilmez)', () => {
  const { shade } = mk();
  assert.equal(code(() => post(shade, {})), 'unknown_channel');
});

test('aynı (pkg,id,tag) yerinde güncellenir; farklı tag ayrı bildirimdir', () => {
  const { shade } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  assert.equal(post(shade, { text: 'bir' }).status, 'posted');
  assert.equal(post(shade, { text: 'iki' }).status, 'updated');
  assert.deepEqual(titles(shade), ['iki']);
  post(shade, { tag: 'x', text: 'üç' });
  assert.equal(titles(shade).length, 2);
});

test('güncelleme yeniden uyarır; onlyAlertOnce ile uyarmaz', () => {
  const { shade, events } = mk();
  chan(shade, 'com.chat', 'msg', I.HIGH);
  post(shade, { text: 'a' });
  post(shade, { text: 'b' });
  post(shade, { text: 'c', onlyAlertOnce: true });
  assert.equal(events.filter((e) => e.type === 'alert').length, 2);
});

test('NONE kanal ve engelli uygulama: bildirim hiç gösterilmez', () => {
  const { shade } = mk();
  chan(shade, 'com.chat', 'msg', I.NONE);
  assert.equal(post(shade, {}).status, 'blocked');
  assert.equal(shade.shade().total, 0);
  chan(shade, 'com.mail', 'in', I.DEFAULT);
  shade.setAppBlocked('com.mail', true);
  assert.equal(post(shade, { pkg: 'com.mail', channel: 'in' }).status, 'blocked');
});

test('uygulama kanalın önemini değiştiremez, kullanıcı değiştirir; NONE mevcutları kaldırır', () => {
  const { shade } = mk();
  chan(shade, 'com.chat', 'msg', I.HIGH);
  const again = shade.createChannel('com.chat', { id: 'msg', name: 'Yeni ad', importance: I.NONE });
  assert.equal(again.importance, I.HIGH);
  assert.equal(again.importanceLocked, true);
  assert.equal(again.name, 'Yeni ad');
  post(shade, {});
  shade.setChannelImportance('com.chat', 'msg', I.LOW);
  assert.equal(shade.shade().sections.find((s) => s.id === 'silent').count, 1);
  shade.setChannelImportance('com.chat', 'msg', I.NONE);
  assert.equal(shade.shade().total, 0);
  assert.equal(shade.history({ state: 'removed' })[0].removedReason, REASON.CHANNEL_BLOCKED);
});

test('uygulamayı engellemek mevcut bildirimlerini kaldırır', () => {
  const { shade } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, {});
  shade.setAppBlocked('com.chat', true);
  assert.equal(shade.shade().total, 0);
  assert.equal(shade.history()[0].removedReason, REASON.APP_BLOCKED);
});

test('bölümler ve durum çubuğu: MIN simge göstermez ama çubukta durur', () => {
  const { shade } = mk();
  chan(shade, 'com.chat', 'msg', I.HIGH);
  chan(shade, 'com.mail', 'in', I.DEFAULT);
  chan(shade, 'com.news', 'low', I.LOW);
  chan(shade, 'com.tip', 'min', I.MIN);
  post(shade, { conversation: true });
  post(shade, { pkg: 'com.mail', channel: 'in', title: 'Gmail', text: 'ileti' });
  post(shade, { pkg: 'com.news', channel: 'low', title: 'Haber', text: 'sessiz' });
  post(shade, { pkg: 'com.tip', channel: 'min', title: 'İpucu', text: 'min' });
  const s = shade.shade();
  assert.deepEqual(s.sections.map((x) => [x.id, x.count]), [['conversations', 1], ['alerting', 1], ['silent', 2]]);
  assert.deepEqual(s.statusBar.map((i) => i.pkg).sort(), ['com.chat', 'com.mail', 'com.news']); // com.tip yok
  assert.equal(s.badges['com.tip'], 1);
});

test('cancel: uygulama kaldırır, deleteIntent (dismissed) çağrılmaz', () => {
  const { shade, events } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, {});
  assert.deepEqual(shade.cancel({ pkg: 'com.chat', id: 1 }), { cancelled: true });
  assert.deepEqual(shade.cancel({ pkg: 'com.chat', id: 1 }), { cancelled: false });
  assert.equal(shade.history()[0].removedReason, REASON.APP_CANCEL);
  assert.equal(events.some((e) => e.type === 'dismissed'), false);
});

// --- kullanıcı eylemleri
test('süregelen bildirim kaydırılamaz, ertelenemez, tümünü temizle ona dokunmaz', () => {
  const { shade } = mk();
  chan(shade, 'com.music', 'play', I.LOW);
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, { pkg: 'com.music', channel: 'play', title: 'Çalıyor', text: 'şarkı', ongoing: true });
  post(shade, {});
  const key = 'com.music|1|';
  assert.equal(code(() => shade.dismiss(key)), 'ongoing');
  assert.equal(code(() => shade.snooze(key, 5)), 'ongoing');
  assert.deepEqual(shade.clearAll(), { dismissed: 1 });
  assert.deepEqual(titles(shade), ['şarkı']);
});

test('kaydırarak kapatma deleteIntent (dismissed) olayı üretir ve geçmişe yazar', () => {
  const { shade, events } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, {});
  shade.dismiss('com.chat|1|');
  assert.ok(events.some((e) => e.type === 'dismissed' && e.pkg === 'com.chat'));
  assert.equal(shade.history()[0].removedReason, REASON.USER_SWIPE);
  assert.equal(code(() => shade.dismiss('com.chat|1|')), 'not_found');
});

test('dokunma: autoCancel kaldırır; autoCancel=false ve süregelen kalır', () => {
  const { shade, events } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, { id: 1, contentIntent: 'chat://ayse' });
  post(shade, { id: 2, text: 'kalıcı', autoCancel: false });
  post(shade, { id: 3, text: 'süregelen', ongoing: true });
  assert.deepEqual(shade.click('com.chat|1|'), { key: 'com.chat|1|', contentIntent: 'chat://ayse', cancelled: true });
  assert.equal(shade.click('com.chat|2|').cancelled, false);
  assert.equal(shade.click('com.chat|3|').cancelled, false);
  assert.deepEqual(titles(shade).sort(), ['kalıcı', 'süregelen']);
  assert.equal(events.filter((e) => e.type === 'click').length, 3);
});

test('eylem: yanıt eylemi metin ister, olay uygulamaya gider', () => {
  const { shade, events } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, { actions: [{ id: 'reply', title: 'Yanıtla', reply: true }, { id: 'read', title: 'Okundu' }] });
  assert.equal(code(() => shade.action('com.chat|1|', 'reply', '  ')), 'reply_required');
  assert.equal(code(() => shade.action('com.chat|1|', 'nope')), 'unknown_action');
  shade.action('com.chat|1|', 'reply', 'tamam');
  shade.action('com.chat|1|', 'read');
  const acts = events.filter((e) => e.type === 'action');
  assert.deepEqual(acts.map((a) => [a.actionId, a.text]), [['reply', 'tamam'], ['read', '']]);
});

test('zaman aşımı (setTimeoutAfter)', () => {
  const { shade, advance } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, { timeoutAfterMs: 10_000 });
  advance(9_000);
  assert.equal(shade.tick().expired, 0);
  advance(1_000);
  assert.equal(shade.tick().expired, 1);
  assert.equal(shade.history()[0].removedReason, REASON.TIMEOUT);
});

test('erteleme: gizlenir, süre dolunca yeniden uyararak döner; erteleme sırasındaki güncelleme gizli kalır', () => {
  const { shade, advance, events } = mk();
  chan(shade, 'com.chat', 'msg', I.HIGH);
  post(shade, { text: 'ilk' });
  shade.snooze('com.chat|1|', 15);
  assert.equal(shade.shade().total, 0);
  assert.equal(shade.shade().snoozed, 1);
  advance(5 * 60_000);
  post(shade, { text: 'güncel' }); // AOSP SnoozeHelper.update: gizli kalır
  assert.equal(shade.shade().total, 0);
  advance(9 * 60_000);
  assert.equal(shade.tick().unsnoozed, 0);
  advance(60_000);
  const before = events.filter((e) => e.type === 'alert').length;
  assert.equal(shade.tick().unsnoozed, 1);
  assert.deepEqual(titles(shade), ['güncel']);
  assert.equal(events.filter((e) => e.type === 'alert').length, before + 1);
});

// --- gruplama
test('açık grup: ≥2 çocuk grup girdisi (özetle), tek çocuk tekil görünür', () => {
  const { shade } = mk();
  chan(shade, 'com.mail', 'in', I.DEFAULT);
  const m = (o) => post(shade, { pkg: 'com.mail', channel: 'in', group: 'inbox', ...o });
  m({ id: 1, title: 'A', text: 'bir' });
  let e = shade.shade().sections[1].entries;
  assert.equal(e.length, 1);
  assert.equal(e[0].type, 'notification');
  m({ id: 2, title: 'B', text: 'iki' });
  m({ id: 99, title: 'Gelen kutusu', text: '2 yeni ileti', groupSummary: true });
  e = shade.shade().sections[1].entries;
  assert.equal(e.length, 1);
  assert.equal(e[0].type, 'group');
  assert.equal(e[0].count, 2);
  assert.deepEqual(e[0].summary, { title: 'Gelen kutusu', text: '2 yeni ileti' });
  assert.equal(shade.shade().total, 2); // özet sayılmaz
});

test('otomatik gruplama: gruplanmamış 4 bildirimde devreye girer', () => {
  const { shade } = mk();
  chan(shade, 'com.insta', 'n', I.DEFAULT);
  for (let i = 1; i <= 3; i++) post(shade, { pkg: 'com.insta', channel: 'n', id: i, title: `k${i}`, text: `t${i}` });
  assert.ok(shade.shade().sections[1].entries.every((x) => x.type === 'notification'));
  post(shade, { pkg: 'com.insta', channel: 'n', id: 4, title: 'k4', text: 't4' });
  const e = shade.shade().sections[1].entries;
  assert.equal(e.length, 1);
  assert.equal(e[0].type, 'group');
  assert.equal(e[0].auto, true);
  assert.equal(e[0].count, 4);
});

test('grubu kaydırmak kapatılabilir çocukları kaldırır, süregelen kalır, çocuksuz özet silinir', () => {
  const { shade } = mk();
  chan(shade, 'com.mail', 'in', I.DEFAULT);
  const m = (o) => post(shade, { pkg: 'com.mail', channel: 'in', group: 'inbox', ...o });
  m({ id: 1, text: 'bir' });
  m({ id: 2, text: 'iki' });
  m({ id: 3, text: 'üç', ongoing: true });
  m({ id: 99, title: 'Özet', text: '3 ileti', groupSummary: true });
  assert.deepEqual(shade.dismissGroup({ pkg: 'com.mail', group: 'inbox', section: 'alerting' }), { dismissed: 2 });
  assert.deepEqual(titles(shade), ['üç']);
  // süregelen kalan çocuk varken özet yaşar; çocuk da gidince özet sistem tarafından kaldırılır
  shade.cancel({ pkg: 'com.mail', id: 3 });
  assert.equal(shade.shade().total, 0);
  assert.equal(shade.history({ state: 'active' }).length, 0);
});

test('bölüm bazlı tümünü temizle', () => {
  const { shade } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  chan(shade, 'com.news', 'low', I.LOW);
  post(shade, {});
  post(shade, { pkg: 'com.news', channel: 'low', title: 'H', text: 'haber' });
  assert.deepEqual(shade.clearAll({ section: 'silent' }), { dismissed: 1 });
  assert.deepEqual(titles(shade), ['Merhaba']);
  assert.equal(code(() => shade.clearAll({ section: 'x' })), 'invalid_section');
});

// --- Notivo özellikleri
test('kopya engeli: farklı anahtarla aynı içerik 10 sn içinde atlanır, aynı anahtar güncellenir', () => {
  const { shade, advance } = mk();
  chan(shade, 'com.bank', 'otp', I.DEFAULT);
  const o = { pkg: 'com.bank', channel: 'otp', title: 'Banka', text: 'Kodunuz 4821' };
  assert.equal(shade.post({ ...o, id: 1 }).status, 'posted');
  assert.equal(shade.post({ ...o, id: 2 }).status, 'duplicate');
  assert.equal(shade.post({ ...o, id: 1 }).status, 'updated');
  advance(11_000);
  assert.equal(shade.post({ ...o, id: 3 }).status, 'posted');
});

test('kurallar: BLOCK hiç göstermez, MUTE yalnızca geçmişe yazar; Türkçe büyük harf', () => {
  const { shade, advance } = mk();
  chan(shade, 'com.shop', 'p', I.HIGH);
  shade.addRule({ keyword: 'İNDİRİM', action: 'BLOCK' });
  shade.addRule({ keyword: 'sessiz', action: 'MUTE' });
  const s = (title, id) => shade.post({ pkg: 'com.shop', channel: 'p', id, title, text: 'x' });
  assert.equal(s('Büyük indirim', 1).status, 'blocked_by_rule');
  assert.equal(s('Sessiz bildirim', 2).status, 'muted_by_rule');
  assert.equal(shade.shade().total, 0);
  const h = shade.history();
  assert.equal(h.length, 1);
  assert.equal(h[0].removedReason, REASON.RULE_MUTE);
  // kural kapatılınca aynı bildirim normal gösterilir (kopya penceresi geçtikten sonra)
  const mute = shade.listRules().find((r) => r.action === 'MUTE');
  shade.setRuleEnabled(mute.id, false);
  advance(11_000);
  assert.equal(s('Sessiz bildirim', 3).status, 'posted');
  assert.equal(shade.shade().total, 1);
});

test('kural doğrulaması', () => {
  const { shade } = mk();
  assert.equal(code(() => shade.addRule({ keyword: '', pkg: '', action: 'BLOCK' })), 'empty_rule');
  assert.equal(code(() => shade.addRule({ keyword: 'a', action: 'KILL' })), 'invalid_action');
});

test('"mesaj silindi" bildirimi saklanmaz, aynı konuşmanın son mesajını işaretler', () => {
  const { shade, advance } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, { id: 1, text: 'eski mesaj' });
  advance(1000);
  post(shade, { id: 2, text: 'yanlış mesaj' });
  advance(1000);
  post(shade, { id: 3, title: 'Başkası', text: 'başka kişi' });
  advance(1000);
  assert.equal(post(shade, { id: 4, text: 'This message was deleted' }).status, 'deleted_notice');
  const h = shade.history();
  assert.equal(h.length, 3);
  assert.deepEqual(h.filter((x) => x.deletedBySender).map((x) => x.text), ['yanlış mesaj']);
  // çubuktaki bildirim özgün metniyle durur ve işaretlidir
  const n = shade.shade().sections.flatMap((s) => s.entries).map((e) => e.n).find((n) => n?.text === 'yanlış mesaj');
  assert.equal(n.deletedBySender, true);
});

test('geçmiş araması Türkçe harf duyarsız; durum süzgeci çalışır', () => {
  const { shade } = mk();
  chan(shade, 'com.shop', 'p', I.DEFAULT);
  shade.post({ pkg: 'com.shop', channel: 'p', id: 1, title: 'Büyük İNDİRİM', text: 'x', appName: 'Mağaza' });
  shade.post({ pkg: 'com.shop', channel: 'p', id: 2, title: 'Başka', text: 'y' });
  assert.deepEqual(shade.history({ q: 'indirim' }).map((h) => h.title), ['Büyük İNDİRİM']);
  assert.deepEqual(shade.history({ q: 'MAĞAZA' }).length, 2);
  shade.dismiss('com.shop|2|');
  assert.equal(shade.history({ state: 'removed' }).length, 1);
  assert.equal(shade.history({ state: 'active' }).length, 1);
  assert.equal(code(() => shade.history({ state: 'x' })), 'invalid_state');
});

test('Rahatsız Etme: bildirim çubuğuna yine düşer, sesi/heads-up bastırılır, alarm geçer', () => {
  const { shade, events } = mk();
  chan(shade, 'com.chat', 'msg', I.HIGH);
  shade.setDnd({ enabled: true });
  const r = post(shade, { text: 'sessizce' });
  assert.equal(r.alerted.suppressedByDnd, true);
  assert.equal(shade.shade().total, 1);
  assert.equal(events.some((e) => e.type === 'alert'), false);
  post(shade, { id: 2, text: 'alarm', category: 'alarm' });
  assert.equal(events.filter((e) => e.type === 'alert').length, 1);
  assert.equal(shade.shade().dnd.enabled, true);
});

test('saklama süresi: eski kapatılmış kayıtlar saatte bir temizlenir, aktifler dokunulmaz', () => {
  const { shade, advance } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, { id: 1, text: 'eski' });
  shade.dismiss('com.chat|1|');
  post(shade, { id: 2, text: 'aktif' });
  shade.setRetentionDays(1);
  advance(2 * 86_400_000);
  assert.equal(shade.tick().purged, 1);
  assert.deepEqual(shade.history().map((h) => h.text), ['aktif']);
});

test('yedek → geri yükleme: tekrarları atlar, geçmişe özgün zamanıyla yazar, bozuk yedeği reddeder', () => {
  const a = mk();
  chan(a.shade, 'com.chat', 'msg', I.DEFAULT);
  post(a.shade, { id: 1, text: 'bir "tırnaklı", virgüllü' });
  a.advance(1000);
  post(a.shade, { id: 2, text: 'iki' });
  const backup = a.shade.backup();
  const b = mk(a.clock.t + 5_000_000);
  assert.equal(b.shade.restore(backup), 2);
  assert.equal(b.shade.restore(backup), 0);
  assert.equal(b.shade.shade().total, 0); // geri yüklenenler çubuğu doldurmaz
  assert.deepEqual(b.shade.history().map((h) => h.text), ['iki', 'bir "tırnaklı", virgüllü']);
  assert.equal(code(() => b.shade.restore({ app: 'baska', items: [] })), 'invalid_backup');
  assert.ok(b.shade.exportCsv().includes('"bir ""tırnaklı"", virgüllü"'));
});

test('doğrulama', () => {
  const { shade } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  assert.equal(code(() => post(shade, { title: ' ', text: '' })), 'empty_notification');
  assert.equal(code(() => post(shade, { pkg: 'a b' })), 'invalid_pkg');
  assert.equal(code(() => post(shade, { id: 1.5 })), 'invalid_id');
  assert.equal(code(() => post(shade, { actions: [1, 2, 3, 4].map((i) => ({ id: `a${i}`, title: 't' })) })), 'invalid_actions');
  assert.equal(code(() => post(shade, { groupSummary: true })), 'invalid_group');
  assert.equal(code(() => post(shade, { title: 'x'.repeat(201) })), 'invalid_title');
  assert.equal(code(() => post(shade, { timeoutAfterMs: 10 })), 'invalid_timeoutAfterMs');
  assert.equal(code(() => post(shade, { ongoing: 'yes' })), 'invalid_ongoing');
  assert.equal(code(() => shade.snooze('com.chat|1|', 0)), 'invalid_minutes');
});

test('güncellemede eski metin geçmişe "REPLACED" olarak yazılır: her mesaj aranabilir kalır', () => {
  const { shade, advance } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, { text: 'ilk mesaj' });
  advance(2_000);
  post(shade, { text: 'ikinci mesaj' });
  advance(2_000);
  post(shade, { text: 'ikinci mesaj' }); // aynı metin: kopya kayıt yok
  assert.deepEqual(titles(shade), ['ikinci mesaj']);
  const h = shade.history();
  assert.deepEqual(h.map((x) => [x.text, x.state, x.removedReason]), [['ikinci mesaj', 'active', null], ['ilk mesaj', 'removed', REASON.REPLACED]]);
  assert.equal(shade.history({ q: 'ilk' }).length, 1);
});

test('süregelen/ilerleme güncellemeleri geçmişi doldurmaz; kopya engeli güncellemeyi durdurmaz', () => {
  const { shade, advance } = mk();
  chan(shade, 'com.dl', 'dl', I.LOW);
  for (let p = 0; p <= 50; p += 10) {
    shade.post({ pkg: 'com.dl', channel: 'dl', id: 1, title: 'İndiriliyor', text: `%${p}`, ongoing: true, onlyAlertOnce: true });
    advance(500);
  }
  assert.equal(shade.history().length, 1);
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, { text: 'A' });
  advance(1_000);
  post(shade, { text: 'B' });
  advance(1_000);
  assert.equal(post(shade, { text: 'A' }).status, 'updated'); // A→B→A, 10 sn içinde: güncelleme kopya sayılmaz
  assert.ok(titles(shade).includes('A') && !titles(shade).includes('B'));
});

test('"mesaj silindi": çubuktaki (en yeni) mesaj işaretlenir, önceki sürüm değil', () => {
  const { shade, advance } = mk();
  chan(shade, 'com.chat', 'msg', I.DEFAULT);
  post(shade, { text: 'önceki' });
  advance(1_000);
  post(shade, { text: 'yanlış gönderilen' });
  advance(1_000);
  assert.equal(post(shade, { text: 'Bu mesaj silindi' }).status, 'deleted_notice');
  const flagged = shade.history().filter((x) => x.deletedBySender).map((x) => x.text);
  assert.deepEqual(flagged, ['yanlış gönderilen']);
  assert.equal(shade.shade().sections.flatMap((s) => s.entries)[0].n.deletedBySender, true);
});

test('uygulama başına canlı bildirim sınırı: aşılınca 429, güncelleme ve iptalden sonra yeniden mümkün', () => {
  const { shade } = mk();
  chan(shade, 'com.spam', 'ch', I.LOW);
  for (let i = 1; i <= 50; i++) shade.post({ pkg: 'com.spam', channel: 'ch', id: i, title: `b${i}`, text: `t${i}` });
  const e = (() => { try { shade.post({ pkg: 'com.spam', channel: 'ch', id: 51, title: 'b51', text: 't51' }); } catch (x) { return x; } })();
  assert.ok(e instanceof ApiError && e.status === 429 && e.code === 'too_many_notifications');
  assert.equal(shade.post({ pkg: 'com.spam', channel: 'ch', id: 7, title: 'b7', text: 'yeni' }).status, 'updated'); // mevcut anahtar güncellenebilir
  shade.cancel({ pkg: 'com.spam', id: 1 });
  assert.equal(shade.post({ pkg: 'com.spam', channel: 'ch', id: 51, title: 'b51', text: 't51' }).status, 'posted');
  chan(shade, 'com.baska', 'ch', I.LOW);
  assert.equal(shade.post({ pkg: 'com.baska', channel: 'ch', id: 1, title: 'x', text: 'y' }).status, 'posted'); // sınır uygulama başına
});
