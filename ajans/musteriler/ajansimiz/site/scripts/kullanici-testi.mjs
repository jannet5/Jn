// KAPI-3 "gerçek kullanıcı gibi" testi: menü, SSS, linkler, klavye, reduced-motion, büyük metin
import { chromium } from 'playwright';
const url = process.argv[2] || 'http://localhost:4321/';
const b = await chromium.launch({ executablePath: process.env.CHROME_PATH || '/opt/pw-browsers/chromium-1194/chrome-linux/chrome' });
const sonuc = [];
const ok = (ad, kosul, ek = '') => sonuc.push(`${kosul ? 'GEÇTİ' : 'KALDI'} · ${ad}${ek ? ' · ' + ek : ''}`);
let p = await b.newPage({ viewport: { width: 375, height: 800 }, hasTouch: true, isMobile: true });
await p.goto(url, { waitUntil: 'networkidle' });
// mobil menü
await p.tap('.mnav summary');
ok('Mobil menü açılıyor', await p.locator('.mnav[open]').count() === 1);
await p.tap('.mnav-panel a[href="#paketler"]'); await p.waitForTimeout(900);
ok('Menü linki kapanıyor ve kaydırıyor', await p.locator('.mnav[open]').count() === 0 && await p.evaluate(() => Math.abs(document.getElementById('paketler').getBoundingClientRect().top) < 120));
// SSS
const d = p.locator('.faq details').first(); await d.locator('summary').tap();
ok('SSS açılıyor', await d.evaluate(e => e.open)); await d.locator('summary').tap(); ok('SSS kapanıyor', !(await d.evaluate(e => e.open)));
// linkler
const links = await p.$$eval('a', as => as.map(a => ({ h: a.getAttribute('href'), t: a.textContent.trim() })));
const kirik = links.filter(l => !l.h).length;
const anchors = links.filter(l => l.h && l.h.startsWith('#') && l.h.length > 1).map(l => l.h);
const eksik = await p.evaluate(a => a.filter(h => !document.querySelector(h)), anchors);
ok('Kırık çapa link yok', eksik.length === 0 && kirik === 0, eksik.join(','));
const wa = links.filter(l => l.h && l.h.startsWith('https://wa.me/'));
ok('WhatsApp linkleri config\'den', wa.length >= 4 && wa.every(l => l.h === wa[0].h), `${wa.length} adet`);
ok('Görünür yer tutucu metin yok', !(await p.evaluate(() => /XXXX|TODO|lorem/i.test(document.body.innerText))));
// v2: sektör çipleri ilgili örneğe kaydırıyor
for (const id of ['kafe', 'berber', 'klinik', 'butik']) {
  await p.evaluate(() => window.scrollTo({ top: 0, behavior: 'instant' }));
  await p.tap(`.chip[href="#ornek-${id}"]`); await p.waitForTimeout(900);
  const top = await p.evaluate(i => document.getElementById('ornek-' + i).getBoundingClientRect().top, id);
  ok(`Sektör çipi → ${id} örneği`, top > -10 && top < 200, `top=${Math.round(top)}`);
}
// v2: fiyatlar config'deki değerlerle sayfada
const metin = await p.evaluate(() => document.body.innerText);
const fiyatlar = ['2.990 ₺', '5.990 ₺', '11.990 ₺', '4.990 ₺', '9.990 ₺', '24.990 ₺', 'İlk 5 işletmeye kurulum', 'KDV hariç', '12 ay sonra site ve uygulama sizin'];
const yok = fiyatlar.filter(f => !metin.includes(f));
ok('Fiyat, lansman ve KDV notu görünür', yok.length === 0, yok.join(','));
ok('Mobilde karşılaştırma kart, tablo gizli', await p.evaluate(() => !document.querySelector('table.kars').offsetParent && document.querySelectorAll('#karsilastirma article').length === 3));
ok('SSS: 12 ay / iptal / reklam bütçesi', ['12 ay sonra ne olur?', 'İptal edebilir miyim?', 'Reklam bütçesi kimin?'].every(q => metin.includes(q)));
// dokunma alanları
const kucuk = await p.$$eval('a, summary, button', els => els.filter(e => { const r = e.getBoundingClientRect(); return r.width > 1 && r.height > 1 && r.height < 44 && getComputedStyle(e).display !== 'inline'; }).map(e => e.textContent.trim().slice(0, 30)));
ok('Dokunma alanı ≥44px (blok öğeler)', kucuk.length === 0, kucuk.join(' | '));
await p.close();
// klavye
p = await b.newPage({ viewport: { width: 1440, height: 900 } }); await p.goto(url);
await p.keyboard.press('Tab'); const ilk = await p.evaluate(() => document.activeElement.textContent.trim());
ok('İlk Tab: içeriğe geç linki', ilk === 'İçeriğe geç');
ok('Masaüstünde karşılaştırma tablosu görünür', await p.evaluate(() => !!document.querySelector('table.kars').offsetParent));
let odak = 0; for (let i = 0; i < 12; i++) { await p.keyboard.press('Tab'); const s = await p.evaluate(() => { const c = getComputedStyle(document.activeElement); return c.outlineStyle !== 'none' || c.boxShadow !== 'none'; }); if (s) odak++; }
ok('Odak halkası görünür', odak === 12, `${odak}/12`);
await p.close();
// reduced motion
p = await b.newPage({ viewport: { width: 375, height: 800 }, reducedMotion: 'reduce' }); await p.goto(url);
ok('Reduced-motion: başlık hemen görünür, defter dolu', await p.evaluate(() => getComputedStyle(document.querySelector('.hw > span')).transform === 'none' && getComputedStyle(document.querySelector('.ledger-line')).strokeDashoffset === '0px'));
await p.close();
// %200 metin
p = await b.newPage({ viewport: { width: 375, height: 800 } }); await p.goto(url);
await p.addStyleTag({ content: 'html{font-size:200%} body{font-size:32px}' });
ok('Büyük metinde yatay taşma yok', await p.evaluate(() => document.documentElement.scrollWidth <= innerWidth));
await b.close();
console.log(sonuc.join('\n'));
