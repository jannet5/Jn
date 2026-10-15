import test from 'node:test';
import assert from 'node:assert/strict';
import { norm, isDeletedNotice, matchRule, dayBucket, csvCell } from '../../server/textlogic.js';

// Android tarafındaki LogicTest.kt ile aynı senaryolar.
test('silinen mesaj yer tutucusu tanınır, normal cümle tanınmaz', () => {
  assert.equal(isDeletedNotice('This message was deleted'), true);
  assert.equal(isDeletedNotice('  Bu mesaj silindi '), true);
  assert.equal(isDeletedNotice('🚫 This message was deleted'), true);
  assert.equal(isDeletedNotice('Merhaba, bu mesaj silindi mi?'), false);
});

test('Türkçe İ/I/ı aynı harf sayılır', () => {
  assert.equal(norm('İNDİRİM'), 'indirim');
  assert.equal(norm('ILIK'), norm('ılık'));
  assert.equal(norm('ışık'), 'işik'); // ş dönüşmez
});

test('kural eşleme: uygulama + anahtar kelime, kapalı kural atlanır', () => {
  const rules = [
    { id: 1, pkg: 'com.a', keyword: 'indirim', action: 'BLOCK', enabled: true },
    { id: 2, pkg: '', keyword: 'kod', action: 'MUTE', enabled: true },
    { id: 3, pkg: 'com.b', keyword: '', action: 'MUTE', enabled: false },
  ];
  assert.equal(matchRule(rules, 'com.a', 'Büyük İNDİRİM', '')?.id, 1);
  assert.equal(matchRule(rules, 'com.c', 'indirim', 'x'), null);
  assert.equal(matchRule(rules, 'com.c', 'Doğrulama', 'kodunuz 123')?.id, 2);
  assert.equal(matchRule(rules, 'com.b', 'any', 'any'), null);
});

test('gün kovaları', () => {
  const day = 86_400_000;
  const now = 10 * day + 12 * 3_600_000;
  assert.equal(dayBucket(now, now - 1000), 0);
  assert.equal(dayBucket(now, now - day), 1);
  assert.equal(dayBucket(now, now - 3 * day), 2);
  assert.equal(dayBucket(now, now - 9 * day), 3);
});

test('CSV hücresi tırnakları kaçırır ve formül enjeksiyonunu engeller', () => {
  assert.equal(csvCell('a "b", c'), '"a ""b"", c"');
  assert.equal(csvCell('=HYPERLINK("x")'), '"\'=HYPERLINK(""x"")"');
  assert.equal(csvCell('@cmd'), '"\'@cmd"');
});
